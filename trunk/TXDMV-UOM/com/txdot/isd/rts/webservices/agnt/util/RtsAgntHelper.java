package com.txdot.isd.rts.webservices.agnt.util;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import com.soaprsps.objects.LoginObject;
import com.soaprsps.objects.UserObject;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.WebAgency;
import com.txdot.isd.rts.server.db.WebAgent;
import com.txdot.isd.rts.server.db.WebAgentSecurity;
import com.txdot.isd.rts.server.db.WebSession;
import com.txdot.isd.rts.services.data.WebAgencyData;
import com.txdot.isd.rts.services.data.WebAgentData;
import com.txdot.isd.rts.services.data.WebAgentSecurityData;
import com.txdot.isd.rts.services.data.WebSessionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntSecurityWS;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;
import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.ses.data.RtsSessionRequest;
import com.txdot.isd.rts.webservices.ses.util.RtsLDAPSearch;
import com.txdot.isd.rts.webservices.ses.util.RtsSecurityUser;

/*
 * RtsAgntHelper.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/28/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/17/2011	Work on handling login after finding 
 * 							multiple agencies.		
 * 							modify logSessionData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/04/2011	Work on fixing error numbers
 * 							modify logSessionData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/07/2011	Work on adding Agent.
 * 							add updateInsertAgentInfo(),
 * 								insertWebAgent(),
 * 								insertWebAgentSecrty()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/28/2011	Use a different error message if there are 
 * 							multiple sessions.
 * 							modify logSessionData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/09/2011	Add update processing.
 * 							add updateWebAgent(), updateWebAgentSecrty()
 * 							modify updateInsertAgentInfo()
 * 							defect 10718 Ver 6.7.1
 * K McKee		06/09/2011  Added getAgentsForAgenciesList() method
 * 							add getAgentsForAgenciesList()
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/15/2011	Modify how we pull agent security records 
 * 							phase 1.
 * 							modify getAgentList()
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/20/2011	Removed a bunch of old stuff for RtsWebAgntWS.
 * 							Need to add it back for RtsWebAgntSecurityWS.
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/30/2011	Add delete functionality.
 * 							add deleteAgent(), deleteWebAgent(), 
 * 								deleteWebAgentSecrty()
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	07/11/2011	Working on adding LDAP search capability.
 * 							defect 10718 Ver 6.8.0
 * 
 * Kathy McKEe  08/08/2011  Added check for user name = null to fix null
 *                          pointer exception  updateInsertAgentInfo()
 *                          defect 10718 Ver 6.8.1
 *
 * Kathy McKee  08/10/2011  Checked for empty list from ldap and return for
 *                          new agent add
 *                          defect 10718 Ver 6.8.1    
 * R Pilon		08/23/2011	Verified return values were not null.  If 
 * 							null, set to empty string. Also, commented out
 * 							logic to for Active Directory agent search.
 * 							modify formatAgentFromLdapData(), 
 * 							  getAgentByName()
 * 							defect 10980 Ver 6.8.1
 * D Hamilton	08/29/2011	In updateInsertAgentInfo() remove check for isDmvUserIndi, 
 * 							since all userids are now in edir
 * Kathy McKee  08/30/2011  Commented out the web session error.  We will
 *                          address this when the service is called outside
 *                          of WebAgent
 *                          defect 10729  Ver 6.8.1
 * R Pilon		08/31/2011	Use TXD0T-V21Alias instead of TXDOT-Alias in
 * 							eDir verification. 
 * 							modify eDirAddAgent()
 * 							defect 10980 Ver 6.8.1   
 * 
 * K McKee      09/01/2011  setUpdtngAgntIdntyNo  
 * 							methods deleteWebAgentSecrty(), updateWebAgent(),
 *                          and updateWebAgentSecrty()
 *                          defect 10729  Ver 6.8.1
 * K McKee      09/28/2011  changed qryWebAgentSecurity to qryWebAgentSecurityForAgent
 * 							method getAgentByName()
 * 							defect 10729 Ver 6.8.1
 * K McKee      10/05/2011  added error logging
 * 							defect 10729 Ver 6.9.0
 * R Pilon		10/19/2011	Modified loading of soaprsps credentials to
 * 							be dynamically loaded from SystemProperty.
 * 							modify eDirLogin()
 * 							defect 11116 Ver 6.8.2
 * R Pilon		10/20/2011	Mask the password returned from the RSPS soap 
 * 							service call.
 * 							modify eDirAddAgent()
 * 							defect 11042 Ver 6.9.0
 * R Pilon		12/19/2011	Added logic to call the SOAPRSPS service  to 
 * 							reset a passwords.
 * 							add eDirResetPassword() and resetAgentPassword()
 * 							defect 11188 Ver 6.9.0
 * D Hamilton	12/22/2011	Added logic to call the SOAPRSPS service to 
 * 							update user attributes in eDir.
 * 							modify eDirAddAgent() and updateInsertAgentInfo()
 * 							add eDirUpdateAgent() and formatPhoneNumberForEDir()
 * 							defect 11198 Ver 6.10.0
 * R Pilon		01/19/2012	Modify resest password logic to return status and 
 * 							rename eDir.... methods.
 * 							modify resetEDirPassword(), resetAgentPassword(), 
 * 							  updateInsertAgentInfo()
 * 							delete eDirAddAgent(), eDirLogin(), eDirLogout(),
 * 							  eDirResetPassword(), eDirUpdateAgent()
 * 							add addEDirAgent(), loginEDir(), logoutEDir(),
 * 							  resetEDirPassword(), updateEDirAgent(),
 * 							  RESET_PWD_RESPONSE_TXT, USERNAME_IS_NULL
 * 							defect 11252 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Helper class to provide common methods for handling Agent information.
 *
 * @version	6.10.0			01/19/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		01/28/2011 15:02:39
 */

public class RtsAgntHelper
{
	// defect 11252
	private final static String RESET_PWD_RESPONSE_TXT = "resetPassword lsResponse = ";

	private final static String USERNAME_IS_NULL = " - UserName is null or empty string";
	// end defect 11252
	
	/**
	 * Delete Agent.
	 * 
	 * @param aaRequest
	 * @param aaDBA
	 * @throws RTSException
	 */
	public void deleteAgent(
		RtsWebAgntWS aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		// initialize to a positive number so agent delete does not 
		// occur unless there are truely no more security rows.
		int liNumOfRows = 1;
		int ciUpdtngAgntIdntyNo = getUpdtngAgntId(aaRequest.getUpdtngAgntIdntyNo(),aaDBA);
		aaRequest.setUpdtngAgntIdntyNo(ciUpdtngAgntIdntyNo);
		// delete agent security if needed
		if (aaRequest.getAgntSecurity() != null)
		{
			liNumOfRows = deleteWebAgentSecrty(aaRequest, aaDBA);
		}

		// Delete the Agent.
		if (liNumOfRows == 0)
		{
			deleteWebAgent(aaRequest, aaDBA);
		}

	}

	/**
	 * Delete the WebAgent row.
	 * 
	 * @param aaWebAgentWS
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int deleteWebAgent(
		RtsWebAgntWS aaWebAgentWS,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liNumOfRows = 0;

		WebAgent laWebAgntSQL = new WebAgent(aaDBA);
		WebAgentData laWebAgntData = new WebAgentData(aaWebAgentWS);
		laWebAgntData.setDeleteIndi(1);
		laWebAgntSQL.updWebAgent(laWebAgntData);

		return liNumOfRows;
	}

	/**
	 * Delete the matching WebAgentSecurity row.
	 * 
	 * <p>The query finds if there are any security rows left for the 
	 * agent.
	 * 
	 * @param aaRtsWebAgentWS
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int deleteWebAgentSecrty(
		RtsWebAgntWS aaRtsWebAgentWS,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liNumOfRows = 0;

		WebAgentSecurity laWebAgntSecrtySQL =
			new WebAgentSecurity(aaDBA);

		RtsWebAgntSecurityWS laRtsWebAgntSec =
			(RtsWebAgntSecurityWS) aaRtsWebAgentWS.getAgntSecurity()[0];

		WebAgentSecurityData laWebAgntSec = 
			new WebAgentSecurityData(laRtsWebAgntSec);
		laWebAgntSec.setDeleteIndi(1);
		laWebAgntSec.setUpdtngAgntIdntyNo(aaRtsWebAgentWS.getUpdtngAgntIdntyNo());
	 

		liNumOfRows =
			laWebAgntSecrtySQL.updWebAgentSecurity(laWebAgntSec);

		Vector lvWebAgntSec =
			laWebAgntSecrtySQL.qryWebAgentSecurity(
				laWebAgntSec.getAgntIdntyNo(),
				0);

		liNumOfRows = lvWebAgntSec.size();

		return liNumOfRows;
	}

	/**
	 * Add the Agent to Edir.
	 * 
	 * @param laWebAgnt
	 * @param laEdirAgent
	 * @param lsSessionID
	 * @return String
	 * @throws RTSException
	 */
	private String addEDirAgent(
		RtsWebAgntWS laWebAgnt,
		RtsLDAPeDirAgnt laEdirAgent,
		String lsSessionID)
		throws RTSException
	{
		String lsUserName = "";
		UserObject laUserObject = new UserObject();
		laUserObject.setSn(laWebAgnt.getLstName());
		laUserObject.setGivenName(laWebAgnt.getFstName());
		laUserObject.setMail(laWebAgnt.getEMail());
		// defect 11198
		// format and pass the phone number to eDir
		laUserObject.setTelephoneNumber(formatPhoneNumberForEDir(laWebAgnt));
		// end defect 11198

		try
		{
			String lsResponse =
				laEdirAgent.addUser(lsSessionID, laUserObject);

			// defect 11042
			// mask the password
			if (lsResponse != null && lsResponse.indexOf("pass= ") > 0)
			{
				StringBuffer laResponse = new StringBuffer(lsResponse);
				lsResponse = laResponse.replace(
						(laResponse.indexOf("pass= ")) + 6,
						laResponse.length(), "********").toString();
			}
			// end defect 11042
			
			System.out.println("addUser lsResponse = " + lsResponse);

			// defect 10980
			if (lsResponse.indexOf("TXDOT-V21Alias=") > -1)
			// end defect 10980
			{
				int liBeginNdx = lsResponse.indexOf('=');
				liBeginNdx = liBeginNdx + 1;
				String lsParseOut = lsResponse.substring(liBeginNdx);
				int liEndNdx = lsParseOut.indexOf("cn");
				lsUserName = lsParseOut.substring(0, liEndNdx - 1);
			}
			if (lsResponse.substring(0, 3).equals("417"))
			{
				throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			}
			// expected results
			// lsResponse = 600:User added successfully
			// TXDOT-Alias = Username = BBROWN1
			// TXDOT-Alias= BBROWN1 cn= 21058 pass= YQ8sQ?)X$1
			return lsUserName;
		}
		catch (RemoteException aeREx)
		{
			aeREx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
	}

	/**
	 * Update the Agent attributes in eDir.
	 * 
	 * @param laWebAgnt
	 * @param laEdirAgent
	 * @param lsSessionID
	 * @return String
	 * @throws RTSException
	 */
	private String updateEDirAgent(
		RtsWebAgntWS laWebAgnt,
		RtsLDAPeDirAgnt laEdirAgent,
		String lsSessionID)
		throws RTSException
	{
		String lsUserName = laWebAgnt.getUserName();
		UserObject laUserObject = new UserObject();
		laUserObject.setSn(laWebAgnt.getLstName());
		laUserObject.setGivenName(laWebAgnt.getFstName());
		laUserObject.setMail(laWebAgnt.getEMail());
		laUserObject.setTelephoneNumber(formatPhoneNumberForEDir(laWebAgnt));

		try
		{
			String lsResponse =
				laEdirAgent.modifyUser(lsSessionID, lsUserName, laUserObject);

			System.out.println("modifyUser lsResponse = " + lsResponse);

			if (lsResponse.substring(0, 3).equals("417"))
			{
				throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			}
			// expected results
			// lsResponse = 601:User modified successfully
			// cn=21189,ou=Active,o=Lookup
			return lsUserName;
		}
		catch (RemoteException aeREx)
		{
			aeREx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
	}

	/**
	 * @param laWebAgnt
	 * @return
	 */
	private String formatPhoneNumberForEDir(RtsWebAgntWS laWebAgnt)
	{
		String lsFormattedPhone = laWebAgnt.getPhone().substring(0, 3) + "-" +
								laWebAgnt.getPhone().substring(3, 6) + "-" +
								laWebAgnt.getPhone().substring(6);
		return lsFormattedPhone;
	}

	/**
	 * Login to eDir.
	 * 
	 * @param laEdirAgent
	 * @return String
	 * @throws RTSException
	 */
	private String loginEDir(RtsLDAPeDirAgnt laEdirAgent)
		throws RTSException
	{
		String lsSessionID;
		LoginObject laLoginObject = new LoginObject();
		// defect 11116
		laLoginObject.setAppname(SystemProperty.getSoapRspsApplication());
		laLoginObject.setUsername(SystemProperty.getSoapRspsUserId());
		laLoginObject.setUserpass(SystemProperty.getSoapRspsPassword());
		// end defect 11116
		laLoginObject.setIpaddress(CommonConstant.STR_SPACE_EMPTY);
		try
		{
			lsSessionID = laEdirAgent.login(laLoginObject);
			return lsSessionID;
		}
		catch (RemoteException aeREx)
		{
			aeREx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
	}

	/**
	 * Logout of eDir.
	 * 
	 * @param laEdirAgent
	 * @param lsSessionID
	 * @throws RTSException
	 */
	private void logoutEDir(
		RtsLDAPeDirAgnt laEdirAgent,
		String lsSessionID)
		throws RTSException
	{
		try
		{
			String lsResponseLO = laEdirAgent.logout(lsSessionID);
			System.out.println("logout lsResponse = " + lsResponseLO);
		}
		catch (RemoteException aeREx)
		{
			aeREx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
	}

	/**
	 * Reset eDir password.
	 * 
	 * @param aaRequest
	 * @param laEdirAgent
	 * @param lsSessionID
	 * @return String
	 * @throws RTSException
	 */
	private String resetEDirPassword(RtsWebAgntWS aaRequest,
			RtsLDAPeDirAgnt laEdirAgent, String lsSessionID)
			throws RTSException
	{
		String lsResponse = "";
		try
		{
			lsResponse = laEdirAgent.resetPassword(lsSessionID,
					aaRequest.getUserName());
			// defect 11252
			System.out.println(RESET_PWD_RESPONSE_TXT
					+ lsResponse);
			// end defect 11252
		}
		catch (RemoteException aeREx)
		{
			aeREx.printStackTrace();
			throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
		}
		return lsResponse;
	}

	/**
	 * Get agent list by name
	 * 
	 * @param aaRequest
	 * @param aaDBA
	 * @return RtsWebAgntWS[]
	 * @throws RTSException
	 */
	private RtsWebAgntWS[] getAgentByName(
		RtsWebAgntWS aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsWebAgntWS[] larrAgentData;

		// set up to lookup name
		RtsWebAgntWS laLookupAgnt = new RtsWebAgntWS();
		if (aaRequest.getUserName() != null)
		{
			laLookupAgnt.setUserName(aaRequest.getUserName());
		}
		if (aaRequest.getLstName() != null)
		{
			laLookupAgnt.setLstName(aaRequest.getLstName());
		}
		if (aaRequest.getFstName() != null)
		{
			laLookupAgnt.setFstName(aaRequest.getFstName());
		}

		// Get the list by name
		WebAgent laWebAgentSQL = new WebAgent(aaDBA);
		Vector lvAgentData = laWebAgentSQL.qryWebAgent(laLookupAgnt);

		larrAgentData = new RtsWebAgntWS[lvAgentData.size()];

		if (lvAgentData.size() > 0)
		{
			for (int i = 0; i < lvAgentData.size(); i++)
			{
				// Get the Agent Record.
				RtsWebAgntWS laRtsWebAgentWS =
					(RtsWebAgntWS) lvAgentData.elementAt(i);

				// query for the matching security records
				WebAgentSecurity laWebAgntSecSQL =
					new WebAgentSecurity(aaDBA);
				// defect 10729 begin
				Vector lvAgntSecrtyData =
					laWebAgntSecSQL.qryWebAgentSecurityForAgent(
						laRtsWebAgentWS.getAgntIdntyNo());
				//defect 10729 end
				// Parse out the security records
				RtsWebAgntSecurityWS[] larrRtsAgntSecrty =
					new RtsWebAgntSecurityWS[lvAgntSecrtyData.size()];
				for (int j = 0; j < lvAgntSecrtyData.size(); j++)
				{
					WebAgentSecurityData laWebAgentSecData =
						(
							WebAgentSecurityData) lvAgntSecrtyData
								.elementAt(
							j);
					RtsWebAgntSecurityWS laRtsAgntSec =
						new RtsWebAgntSecurityWS(laWebAgentSecData);
					larrRtsAgntSecrty[j] = laRtsAgntSec;
				}
				larrAgentData[i] = laRtsWebAgentWS;
				larrAgentData[i].setAgntSecurity(larrRtsAgntSecrty);
			}
		}
		else
		{
			RtsLDAPSearch laLdapSearch = new RtsLDAPSearch();
			RtsSecurityUser laSecurityUser;

			if (aaRequest.getUserName() != null
				&& aaRequest.getUserName().length() > 0)
			{
				try
				{
					// search eDir
					laSecurityUser =
						laLdapSearch.getEDirUserByUserId(aaRequest
							.getUserName());

					if (laSecurityUser != null)
					{
						larrAgentData =
							formatAgentFromLdapData(laSecurityUser,
								false);
					}
//					else
//					{
//						laSecurityUser =
//							laLdapSearch.getADUserByUserId(aaRequest
//								.getUserName());
//						if (laSecurityUser != null)
//						{
//							larrAgentData =
//								formatAgentFromLdapData(laSecurityUser,
//									true);
//						}
//					}
				}
				catch (NamingException aeNEx)
				{
					Log.write(Log.APPLICATION, this, "getAgentByName - Naming exception for user name = " + aaRequest.getUserName());
					aeNEx.printStackTrace();
					throw new RTSException(
						"TODO Got a naming exception!");
				}
			}
			else if (aaRequest.getLstName() != null
				&& aaRequest.getLstName().length() > 0)
			{
				try
				{
					// search eDir for all users that match first and
					// last name
					List secUsers =
						laLdapSearch.getEDirUserByFirstLastName(
							aaRequest.getLstName(), aaRequest
								.getFstName());
					/*
					 *  
					 *  k mckee -- 08/2011
					 *  If nothing found, return for new agent to be added
					 */
					// defect 10980
					if (!secUsers.isEmpty())
					{
//						return larrAgentData;
//					}
//					else
//					{
//						// search Active Directory for all users that
//						// match first and last name
//						secUsers =
//							laLdapSearch.getADUserByFirstLastName(
//								aaRequest.getLstName(), aaRequest
//									.getFstName());
//
						Iterator secUsersIter = secUsers.iterator();
						while (secUsersIter.hasNext())
						{
							laSecurityUser =
								(RtsSecurityUser) secUsersIter.next();

							if (laSecurityUser != null)
							{
								larrAgentData =
									formatAgentFromLdapData(
										laSecurityUser, true);
							}
						}
					}
					// end defect 10980
				}
				catch (NamingException aeNEx)
				{
					Log.write(Log.APPLICATION, this, "getAgentByName - Naming exception for last name = " + aaRequest.getLstName());
					aeNEx.printStackTrace();
					throw new RTSException(
						"TODO Got a naming exception!");
				}
			}
		}
		return larrAgentData;
	}

	/**
	 * Format Agent data from LDAP return.
	 * 
	 * @param laSecurityUser
	 * @param abDMVUser
	 * @return RtsWebAgntWS[]
	 */
	private RtsWebAgntWS[] formatAgentFromLdapData(
		RtsSecurityUser laSecurityUser, boolean abDMVUser)
	{
		RtsWebAgntWS[] larrAgentData;
		larrAgentData = new RtsWebAgntWS[1];
		RtsWebAgntWS laAgent = new RtsWebAgntWS();
		
		// defect 10980 
		// if any returned values are null, set as empty strings
		laAgent
			.setLstName((laSecurityUser.getLastName() != null) ? laSecurityUser
				.getLastName()
				: "");
		laAgent
			.setFstName((laSecurityUser.getFirstName() != null) ? laSecurityUser
				.getFirstName()
				: "");
		laAgent
			.setEMail((laSecurityUser.getEmailAddr() != null) ? laSecurityUser
				.getEmailAddr()
				: "");
		laAgent
			.setPhone((laSecurityUser.getPhoneNbrDigitsOnly() != null) ? laSecurityUser
				.getPhoneNbrDigitsOnly()
				: "");
		laAgent
			.setUserName((laSecurityUser.getUserId() != null) ? laSecurityUser
				.getUserId()
				: "");
		laAgent.setMiName("");
		// end defect 10980
				
		laAgent.setDmvUserIndi(abDMVUser);
		laAgent.setChngTimeStmp(new RTSDate().getCalendar());
				
		RtsWebAgntSecurityWS[] larrRtsAgntSecrty =
			new RtsWebAgntSecurityWS[1];
		RtsWebAgntSecurityWS laWebAgentSecurity = new RtsWebAgntSecurityWS();
		larrRtsAgntSecrty[0] = laWebAgentSecurity;
		laAgent.setAgntSecurity(larrRtsAgntSecrty);
		
		larrAgentData[0] = laAgent;
		
		return larrAgentData;
	}

	/**
	 * Look up Agents by Security Identity Numbers or Agency Number.
	 * 
	 * @param aaRequest
	 * @param aaDBA
	 * @return RtsWebAgntWS[]
	 * @throws RTSException
	 */
	private RtsWebAgntWS[] getAgentBySecurityIdentity(
		RtsWebAgntWS aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsWebAgntWS[] larrAgentData;
		// Get the agent security records matching the selected agency.
		WebAgentSecurity laWebAgntSecSQL = new WebAgentSecurity(aaDBA);
		Vector lvAgntSecrtyData =
			laWebAgntSecSQL.qryWebAgentSecurity(
				0,
				(aaRequest.getAgntSecurity())[0].getAgncyIdntyNo());

		larrAgentData = new RtsWebAgntWS[lvAgntSecrtyData.size()];

		for (int i = 0; i < lvAgntSecrtyData.size(); i++)
		{
			WebAgentSecurityData laWebAgentSecData =
				(WebAgentSecurityData) lvAgntSecrtyData.elementAt(i);
			RtsWebAgntSecurityWS laLookupAgntSec =
				new RtsWebAgntSecurityWS(laWebAgentSecData);
			RtsWebAgntSecurityWS[] larrLookupAgntSecrty =
				new RtsWebAgntSecurityWS[1];
			larrLookupAgntSecrty[0] = laLookupAgntSec;

			RtsWebAgntWS laLookupAgnt = new RtsWebAgntWS();
			laLookupAgnt.setAgntIdntyNo(
				laLookupAgntSec.getAgntIdntyNo());

			WebAgent laWebAgentSQL = new WebAgent(aaDBA);

			Vector lvAgentData =
				laWebAgentSQL.qryWebAgent(laLookupAgnt);
			RtsWebAgntWS laRtsWebAgentWS =
				(RtsWebAgntWS) lvAgentData.elementAt(0);
			laRtsWebAgentWS.setAgntSecurity(larrLookupAgntSecrty);
			larrAgentData[i] = laRtsWebAgentWS;
		}
		return larrAgentData;
	}

	/**
	 * Get Agent Data for Login.
	 * 
	 * @param aaRequest
	 * @param aaSessionReq
	 * @param aaDBA
	 * @param asEdirSessionID
	 * @return RtsWebAgntWS[]
	 * @throws RTSException
	 */
	public RtsWebAgntWS[] getAgentForLogin(
		RtsWebAgntWS aaRequest,
		RtsAbstractRequest aaSessionReq,
		DatabaseAccess aaDBA,
		String asEdirSessionID)
		throws RTSException
	{
		// recompiling in Java 1.4.2
		RtsWebAgntWS[] larrAgentData = null;

		WebAgent laSQL = new WebAgent(aaDBA);

		Vector lvAgentData = laSQL.qryWebAgent(aaRequest);

		// insert the agent data into the session table.
		if (lvAgentData.size() > 0)
		{
			WebSession laSessionSQL = new WebSession(aaDBA);
			WebSessionData laSessionData = new WebSessionData();
			// Bob's change
			if (asEdirSessionID == null)
			{
				laSessionData.setEDirSessionId(
					aaSessionReq.getSessionId());
			}
			else
			{
				laSessionData.setEDirSessionId(asEdirSessionID);
			}
			// end Bob's change
			laSessionData.setWebSessionId(aaSessionReq.getSessionId());
			if (aaSessionReq instanceof RtsSessionRequest)
			{
				laSessionData.setReqIpAddr(
					((RtsSessionRequest) aaSessionReq)
						.getRequestorIpAddr());
			}
			else
			{
				laSessionData.setReqIpAddr("");
			}

			WebAgentSecurity laWAS = new WebAgentSecurity(aaDBA);

			for (Iterator laIter = lvAgentData.iterator();
				laIter.hasNext();
				)
			{
				RtsWebAgntWS laElement = (RtsWebAgntWS) laIter.next();
				int laAgntIdntyNo = laElement.getAgntIdntyNo();
				Vector lvAgntSec = new Vector(1);
				if ((aaRequest.getAgntSecurity())[0]
					.getAgntSecrtyIdntyNo()
					> 0)
				{
					lvAgntSec =
						laWAS.qryWebAgentSecurity(
							(aaRequest.getAgntSecurity())[0]
								.getAgntSecrtyIdntyNo());
				}
				else
				{
					lvAgntSec =
						laWAS.qryWebAgentSecurity(laAgntIdntyNo, 0);
				}

				RtsWebAgntSecurityWS[] larrWebAgntSecWS =
					new RtsWebAgntSecurityWS[lvAgntSec.size()];

				for (int i = 0; i < lvAgntSec.size(); i++)
				{
					WebAgentSecurityData laASElement =
						(WebAgentSecurityData) lvAgntSec.elementAt(i);
					RtsWebAgntSecurityWS laNewElement =
						new RtsWebAgntSecurityWS(laASElement);
					larrWebAgntSecWS[i] = laNewElement;
				}
				laElement.setAgntSecurity(larrWebAgntSecWS);

				// set the agntsecurityidntyno for session.
				if (lvAgntSec.size() != 1)
				{
					laSessionData.setAgntSecrtyIdntyNo(0);
				}
				else
				{
					laSessionData.setAgntSecrtyIdntyNo(
						larrWebAgntSecWS[0].getAgntSecrtyIdntyNo());
				}

				if (aaSessionReq.getAction()
					== WebServicesActionsConstants.RTS_AGENT_LOGIN)
				{
					logSessionData(laSessionSQL, laSessionData);
				}
			}
		}

		// ELSE 
		// Not a valid agent.
		// Login will throw the error

		larrAgentData = new RtsWebAgntWS[lvAgentData.size()];

		lvAgentData.toArray(larrAgentData);

		return larrAgentData;
	}
	/**
	 * Run and get results from Agent query.
	 * 
	 * @param aaRequest
	 * @param aaDBA
	 * @return RtsWebAgntWS[]
	 * @throws RTSException
	 */
	public RtsWebAgntWS[] getAgentList(
		RtsWebAgntWS aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsWebAgntWS[] larrAgentData = null;

		if ((aaRequest.getUserName() != null
			&& aaRequest.getUserName().length() > 0)
			|| (aaRequest.getLstName() != null
				&& aaRequest.getLstName().length() > 0))
		{
			// Lookup by name
			larrAgentData = getAgentByName(aaRequest, aaDBA);
		}
		else
		{
			// Do lookup by Security Identity Number or 
			//  Agency Identity Number
			larrAgentData =
				getAgentBySecurityIdentity(aaRequest, aaDBA);
		}

		return larrAgentData;
	}

	/**
	 * Run and get results of Agents for a County query.
	 * 
	 * @param aaRequest
	 * @param aaDBA
	 * @return RtsWebAgntWS[]
	 * @throws RTSException
	 */
	public RtsWebAgntWS[] getAgentsForCountyList(
		RtsWebAgntWS aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		WebAgent laWebAgntSQL = new WebAgent(aaDBA);
		RtsWebAgntWS[] larrAgentData = null;

		Vector lvAgentData =
			laWebAgntSQL.qryWebAgentsForCounty(aaRequest);

		larrAgentData = new RtsWebAgntWS[lvAgentData.size()];

		lvAgentData.toArray(larrAgentData);

		return larrAgentData;
	}

	/**
	 * Insert Web Agent Data.
	 * 
	 * @param aaWebAgentWS
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int insertWebAgent(
		RtsWebAgntWS aaWebAgentWS,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liAgntIdnty = 0;

		WebAgent laWebAgntSQL = new WebAgent(aaDBA);
		WebAgentData laWebAgntData = new WebAgentData();

		laWebAgntData.setAgntIdntyNo(0);
		int ciUpdtngAgntId = getUpdtngAgntId(aaWebAgentWS.getUpdtngAgntIdntyNo(),aaDBA);
		// TODO This is not being passed at the moment!
		laWebAgntData.setDMVUserIndi(aaWebAgentWS.isDmvUserIndi()? 1 : 0);

		laWebAgntData.setInitOfcNo(aaWebAgentWS.getInitOfcNo());
		// TODO This is not being passed.  Should we get it from session?
		// TODO Note to Ray.... UpdtngAgntIdntyNo is not in RtsWebAgntWS so we can't pass it.
		// TODO Needed for logging... currently RTS_WEB_AGNT_LOG is not being stored
		laWebAgntData.setUpdtngAgntIdntyNo(ciUpdtngAgntId);

		laWebAgntData.setUserName(aaWebAgentWS.getUserName());
		laWebAgntData.setFstName(aaWebAgentWS.getFstName());
		laWebAgntData.setMIName(aaWebAgentWS.getMiName());
		laWebAgntData.setLstName(aaWebAgentWS.getLstName());
		laWebAgntData.setEMail(aaWebAgentWS.getEMail());
		laWebAgntData.setPhone(aaWebAgentWS.getPhone());
		laWebAgntData.setDeleteIndi(0);

		laWebAgntData = laWebAgntSQL.insWebAgent(laWebAgntData);

		liAgntIdnty = laWebAgntData.getAgntIdntyNo();

		return liAgntIdnty;
	}

	/**
	 * Insert the WebAgntSecrty row.
	 * 
	 * @param aaWebAgentWS
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int insertWebAgentSecrty(
		RtsWebAgntWS aaWebAgentWS,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liAgntSecrtyIdnty = 0;

		WebAgentSecurity laWebAgntSecrtySQL =
			new WebAgentSecurity(aaDBA);
		int ciUpdtngAgntIdntyNo = getUpdtngAgntId(aaWebAgentWS.getUpdtngAgntIdntyNo(),aaDBA);
		RtsWebAgntSecurityWS[] larrRtsWebAgntSecrecs =
			aaWebAgentWS.getAgntSecurity();

		for (int i = 0; i < larrRtsWebAgntSecrecs.length; i++)
		{
			WebAgentSecurityData laWebAgntSecrtyData =
				new WebAgentSecurityData(larrRtsWebAgntSecrecs[i]);

			laWebAgntSecrtyData.setDeleteIndi(0);
			laWebAgntSecrtyData.setUpdtngAgntIdntyNo(ciUpdtngAgntIdntyNo);
			laWebAgntSecrtyData =
				laWebAgntSecrtySQL.insWebAgentSecurity(
					laWebAgntSecrtyData);

			liAgntSecrtyIdnty =
				laWebAgntSecrtyData.getAgntSecrtyIdntyNo();
		}
		return liAgntSecrtyIdnty;
	}

	/**
	 * Handle inserting or updating the Session Data as needed by Login.
	 * 
	 * @param aaSessionSQL
	 * @param aaSessionData
	 * @throws RTSException
	 */
	private void logSessionData(
		WebSession aaSessionSQL,
		WebSessionData aaSessionData)
		throws RTSException
	{
		// hold the agntSecrtyIdntyNo while we check the session.
		int liTempIdnty = aaSessionData.getAgntSecrtyIdntyNo();
		aaSessionData.setAgntSecrtyIdntyNo(0);
		Vector lvCheckSession =
			aaSessionSQL.qryWebSession(aaSessionData);
		aaSessionData.setAgntSecrtyIdntyNo(liTempIdnty);

		if (lvCheckSession.size() == 0)
		{
			aaSessionSQL.insWebSession(aaSessionData);
		}
		else
		{
			if (lvCheckSession.size() == 1)
			{

/*				WebSessionData laSessData =
					(WebSessionData) lvCheckSession.elementAt(0);
				RTSDate laCompData = new RTSDate();
				laCompData.add(Calendar.MINUTE, -15);
				if (laCompData
					.compareTo(laSessData.getLastAccsTimestmp())
					< 1
					&& laSessData.getAgntSecrtyIdntyNo() > 0
					&& laSessData.getAgntSecrtyIdntyNo()
						!= aaSessionData.getAgntSecrtyIdntyNo())
				{
					// there is an active session.. 
					// This is an error.
					throw new RTSException(
						ErrorsConstant.ERR_NUM_WEBAGNT_SESSION_ERR);
				}
				else
				{*/
					// TODO Need to verify the user is not logged in more than once.
					//					String lsTempSess = aaSessionData.getWebSessionId();
					//					aaSessionData.setWebSessionId(""); 
					//					Vector lvCheckSession2 = aaSessionSQL.qryWebSession(aaSessionData);
					//					aaSessionData.setWebSessionId(lsTempSess);
					// take over the session
					aaSessionSQL.updWebSession(aaSessionData);
	//			}
			}
			else
			{
				// More than 1 session!
				// defect 10670
				Log.write(Log.APPLICATION, this, "- logSessionData " + "Error number 2340" + " - More than one session");
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_SESSION_MULT_ERR);
				// end defect 10670
			}
		}

	}

	/**
	 * Process a Reset Password request for Agent
	 * 
	 * @param aaRequest
	 * @return String
	 * @throws RTSException
	 */
	public String resetAgentPassword(RtsWebAgntWS aaRequest)
			throws RTSException
	{
		// defect 11252
		String lsResponse = "";
		// end defect 11252
		if (aaRequest.getUserName() == null
				|| aaRequest.getUserName().length() < 1)
		{
			// defect 11252
			Log.write(Log.APPLICATION, this, "resetAgentPassword - " + ErrorsConstant
					.USERNAME_MUST_BE_ENTERED + USERNAME_IS_NULL);
			// end defect 11252
			throw new RTSException(
					ErrorsConstant.USERNAME_MUST_BE_ENTERED);
		}
		else
		{
			RtsLDAPeDirAgnt laEdirAgent = new RtsLDAPeDirAgnt();

			// defect 11252
			// Login to eDir
			String lsSessionID = loginEDir(laEdirAgent);

			lsResponse = resetEDirPassword(aaRequest, laEdirAgent,
					lsSessionID);
			
			logoutEDir(laEdirAgent, lsSessionID);
		}
		return lsResponse;
		// end defect 11252
	}

	/**
	 * Process an Update / Insert request for Agent Data
	 * 
	 * @param laWebAgnt
	 */
	public void updateInsertAgentInfo(
		RtsWebAgntWS laWebAgnt,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		// insert agent if needed
		if (laWebAgnt.getAgntIdntyNo() < 1)
		{
			if (laWebAgnt.getUserName() == null
				|| laWebAgnt.getUserName().length() < 1)
			{
				if ((laWebAgnt.getAgntSecurity())[0].getAgncyIdntyNo()
					> 0)
				{
					WebAgencyData laSQLData = new WebAgencyData();
					laSQLData.setAgncyIdntyNo(
						(laWebAgnt.getAgntSecurity())[0]
							.getAgncyIdntyNo());
					WebAgency laSQL = new WebAgency(aaDBA);

					Vector lvReturnData = laSQL.qryWebAgency(laSQLData);

					if (lvReturnData.size() == 1)
					{
						WebAgencyData laReturnElement =
							(WebAgencyData) lvReturnData.elementAt(0);
						laWebAgnt.setDmvUserIndi(
							!laReturnElement.getAgncyTypeCd().equals(
								"S"));

						// create user in eDir
						/*
						 * Kmckee -- 08/08/2011  -- added check for username = null to fix null pointer
						 */
						// DHamil -- 08/29/2011  -- remove check for isDmvUserIndi, since all userids are now in edir
//						if (!aaRequest.isDmvUserIndi() && 
//							(aaRequest.getUserName() == null || aaRequest.getUserName().length() < 1))
						if (laWebAgnt.getUserName() == null || laWebAgnt.getUserName().length() < 1)
						{
							RtsLDAPeDirAgnt laEdirAgent =
								new RtsLDAPeDirAgnt();

							// defect 11252
							// Login to eDir
							String lsSessionID = loginEDir(laEdirAgent);

							String lsResponse =
								addEDirAgent(
									laWebAgnt,
									laEdirAgent,
									lsSessionID);
							
							laWebAgnt.setUserName(lsResponse);

							logoutEDir(laEdirAgent, lsSessionID);
							// end defect 11252
						}
					}
					else
					{
						Log.write(Log.APPLICATION, this,"updateInsertAgentInfo - " + " Error number 2340" + " - Multiple or no Active Session");
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_WEBAGNT_GENERAL_ERROR);
					}
				}

			}
			int liAgntIdnty = insertWebAgent(laWebAgnt, aaDBA);
			laWebAgnt.setAgntIdntyNo(liAgntIdnty);
			laWebAgnt.getAgntSecurity()[0].setAgntIdntyNo(liAgntIdnty);
		}
		else
		{
			updateWebAgent(laWebAgnt, aaDBA);

			// defect 11198
			// pass updated user attributes to eDir
			RtsLDAPeDirAgnt laEdirAgent =
				new RtsLDAPeDirAgnt();
			
			// defect 11252
			// Login to eDir
			String lsSessionID = loginEDir(laEdirAgent);
			
			String lsResponse =
				updateEDirAgent(
						laWebAgnt,
						laEdirAgent,
						lsSessionID);
			
			laWebAgnt.setUserName(lsResponse);
			
			logoutEDir(laEdirAgent, lsSessionID);
			// end defect 11252
			// end defect 11198
		}

		// insert agent security if needed
		if ((laWebAgnt.getAgntSecurity())[0].getAgntSecrtyIdntyNo()
			< 1)
		{
			int liAgntSecrtyIdnty =
				insertWebAgentSecrty(laWebAgnt, aaDBA);
			laWebAgnt.getAgntSecurity()[0].setAgntIdntyNo(
				liAgntSecrtyIdnty);
		}
		else
		{
			updateWebAgentSecrty(laWebAgnt, aaDBA);
		}
	}

	/**
	 * Update Web Agent Data for selected Agent.
	 * 
	 * @param aaWebAgentWS
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int updateWebAgent(
		RtsWebAgntWS aaWebAgentWS,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liNumOfRows = 0;

		if (aaWebAgentWS.isDataChanged())
		{
			// until the web service can be changed, the caller id will be passed in the
			// aaRequest tUpdtngAgntIdntyNo.  Retrieve the updating agent idnenty number
			// from the database and set it into the value for the delete			
			int ciUpdtngAgntIdntyNo = getUpdtngAgntId(aaWebAgentWS.getUpdtngAgntIdntyNo(),aaDBA);
			WebAgent laWebAgntSQL = new WebAgent(aaDBA);
			WebAgentData laWebAgntData = new WebAgentData();

			laWebAgntData.setAgntIdntyNo(aaWebAgentWS.getAgntIdntyNo());
			laWebAgntData.setUpdtngAgntIdntyNo(ciUpdtngAgntIdntyNo);
			laWebAgntData.setFstName(aaWebAgentWS.getFstName());
			laWebAgntData.setMIName(aaWebAgentWS.getMiName());
			laWebAgntData.setLstName(aaWebAgentWS.getLstName());
			laWebAgntData.setEMail(aaWebAgentWS.getEMail());
			laWebAgntData.setPhone(aaWebAgentWS.getPhone());

			laWebAgntSQL.updWebAgent(laWebAgntData);
		}

		return liNumOfRows;
	}

	/**
	 * Update the WebAgntSecrty row.
	 * 
	 * @param aaRtsWebAgentWS
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int updateWebAgentSecrty(
		RtsWebAgntWS aaRtsWebAgentWS,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liNumOfRows = 0;

		WebAgentSecurity laWebAgntSecrtySQL =
			new WebAgentSecurity(aaDBA);

		RtsWebAgntSecurityWS laRtsWebAgntSec =
			(RtsWebAgntSecurityWS) aaRtsWebAgentWS.getAgntSecurity()[0];
	 

		if (laRtsWebAgntSec.isDataChanged())
		{
 
			int ciUpdtngAgntIdntyNo = getUpdtngAgntId(aaRtsWebAgentWS.getUpdtngAgntIdntyNo(),aaDBA);
			WebAgentSecurityData laWebAgntSec = 
				new WebAgentSecurityData(laRtsWebAgntSec);
			laWebAgntSec.setUpdtngAgntIdntyNo(ciUpdtngAgntIdntyNo);
			 
			liNumOfRows =
				laWebAgntSecrtySQL.updWebAgentSecurity(laWebAgntSec);
		}

		return liNumOfRows;

	}
	

	/**
	 * Retreive the agent idnentity number by querying the
	 * agent security table using the Caller agntsecrtyidntyno.
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	private int getUpdtngAgntId(
		int aiAgntSecrtyIdntyNo,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int ciAgntIdntyNo = 0;
		
		// Get the agent security records matching the selected agency.
		WebAgentSecurity laWebAgntSecSQL = new WebAgentSecurity(aaDBA);
		
		Vector lvAgntSecrtyData =
			laWebAgntSecSQL.qryAgntIdntyNo(aiAgntSecrtyIdntyNo);

		for (int i = 0; i < lvAgntSecrtyData.size(); i++)
		{
			WebAgentSecurityData laWebAgentSecData =
				(WebAgentSecurityData) lvAgntSecrtyData.elementAt(i);
			
			ciAgntIdntyNo = laWebAgentSecData.getAgntIdntyNo();
			
		}
		return ciAgntIdntyNo;
	}
	
}
