package com.txdot.isd.rts.webservices.ses.util;

import java.util.Vector;

import org.apache.soap.SOAPException;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.WebAgent;
import com.txdot.isd.rts.server.db.WebAgentSecurity;
import com.txdot.isd.rts.server.db.WebSession;
import com.txdot.isd.rts.services.data.WebAgentSecurityData;
import com.txdot.isd.rts.services.data.WebSessionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntSecurityWS;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;
import com.txdot.isd.rts.webservices.ses.data.RtsSessionRequest;

/*
 * RtsSesHelper.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/09/2011	Initial load.
 * 							Formulate methods to handle session 
 * 							initiation and verification.
 * 							defect 10670 Ver 670
 * Ray Rowehl	03/02/2011	Set up the eDir lookup to only connect if 
 * 							the username contains "CRAZY"
 * 							defect 10670 Ver 670
 * Min Wang    	03/11/2011  modify authUser()
 * 							defect 10670 Ver 671
 * Ray Rowehl	03/17/2011	Commit after deleting the session.
 * 							modify doLogout()
 * 							defect 10670 Ver 671
 * Ray Rowehl	03/17/2011	Modify Session lookup to actually work.
 * 							modify getSession()
 * 							defect 10670 Ver 671
 * Ray Rowehl	04/04/2011	Throw a more appropriate error when there is
 * 							a problem with finding the agent.
 * 							modify lookupAgntSecrtyRec()
 * 							defect 10670 Ver 671
 * Ray Rowehl	04/20/2011	Comment out authentication for now.
 * 							modify authUser()
 * 							defect 10670 Ver 671
 * Ray Rowehl	04/26/2011	Comment out call to SoapRSPS processing on
 * 							logout.		
 * 							modify doLogout()
 * 							defect 10670 Ver 671
 * Ray Rowehl	04/28/2011	Modify error constant to be unique.
 * 							modify lookupAgntSecrtyRec()
 * 							defect 10670 Ver 671
 * Ray Rowehl	05/02/2011	Throw SOAPException if needed from authUser.
 * 							modify authUser()
 * 							defect 10670 Ver 671
 * Ray Rowehl	06/09/2011	Add a converting method to take agent security
 * 							identity number as a string and convert it to 
 * 							an int.  Then call the lookup routine.
 * 							Add check for Agent Auth Access.
 * 							add getSession(String, String, DatabaseAccess),
 * 								hasAgentAuthAccess()
 * 							defect 10718 Ver 6.8.0
 * R Pilon		08/29/2011	Removed call to check Active Directory when 
 * 							authenticating user.
 * 							modify authUser()
 * 							defect 10980 Ver 6.8.1
 * K McKee      08/31/2011  Commented out Throw and inserted Log for
 *                          multiple active session to correct
 *                          modify getSession()
 *                          defect 10729 Ver 6.8.1
 * K McKee      10/05/2011  Added logging for Generic Error 2300
 *                          defect 10729 Ver 6.9.0
 * K McKee      10/14/2011  Commented out log for multiple sessions
 *                          defect 10729 Ver 6.9.0
 * --------------------------------------------------------------------
 */

/**
 * Provides utility methods to manage a users session.
 *
 * @version	6.9.0			10/14/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		02/09/2011 08:05:09
 */

public class RtsSesHelper
{
	private RtsWebAgntWS[] carrAgentData = null;

	/**
	 * Verify Session is valid.
	 * 
	 * @param asSessionId
	 * @param aiAgntSecrtyIdntyNo
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	public int getSession(
		String asSessionId,
		int aiAgntSecrtyIdntyNo,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		WebSession laSesSQL = new WebSession(aaDBA);
		WebSessionData laSessionData = new WebSessionData();

		// Make sure this is the only session the user is on
		laSessionData.setAgntSecrtyIdntyNo(aiAgntSecrtyIdntyNo);
		laSessionData.setWebSessionId(null);
		Vector lvSessionLookup = laSesSQL.qryWebSession(laSessionData);
//		if (lvSessionLookup.size() != 1 && aiAgntSecrtyIdntyNo != 0)
//		{
//			Log.write(Log.APPLICATION, this, "-getSession - AgntSecrtyIdntyNo - " + aiAgntSecrtyIdntyNo + " - Multiple Active Session");
// 			throw new RTSException(
//				ErrorsConstant.ERR_NUM_WEBAGNT_SESSION_ERR); 
//		}

		// Check for the active session.
		laSessionData.setWebSessionId(asSessionId);
		laSessionData.setAgntSecrtyIdntyNo(aiAgntSecrtyIdntyNo);
		lvSessionLookup = laSesSQL.qryWebSession(laSessionData);
		if (lvSessionLookup.size() < 1)
		{
			Log.write(Log.APPLICATION, this, " - getSession - AgntSecrtyIdntyNo - " + aiAgntSecrtyIdntyNo + " - no active session");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_SESSION_ERR);
		}

		// Good session
		laSesSQL.updWebSession(laSessionData);

		WebSessionData laSessionDataResult =
			(WebSessionData) lvSessionLookup.elementAt(0);

		if (laSessionDataResult.getAgntSecrtyIdntyNo() > 0)
		{
			lookupAgntSecrtyRec(
				aaDBA,
				laSessionDataResult.getAgntSecrtyIdntyNo());
		}

		return laSessionDataResult.getAgntSecrtyIdntyNo();
	}

	/**
	 * Accept agent security number as a string, convert it to int, and 
	 * then call the real method.
	 * 
	 * @param asSessionId
	 * @param asAgntSecrtyIdntyNo
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	public int getSession(
		String asSessionId,
		String asAgntSecrtyIdntyNo,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		int liAgntSecrtyIdntyNo = 0;
		try
		{
			liAgntSecrtyIdntyNo = Integer.parseInt(asAgntSecrtyIdntyNo);
			liAgntSecrtyIdntyNo =
				getSession(asSessionId, liAgntSecrtyIdntyNo, aaDBA);
		}
		catch (NumberFormatException aeNFEx)
		{
			carrAgentData = null;
		}
		return liAgntSecrtyIdntyNo;
	}

	/**
	 * Lookup and Set the Agent Security Record.
	 * 
	 * @param aaDBA
	 * @param aiAgntSecrtyIdntyNo
	 * @throws RTSException
	 */
	private void lookupAgntSecrtyRec(
		DatabaseAccess aaDBA,
		int aiAgntSecrtyIdntyNo)
		throws RTSException
	{
		// get the data starting with agent security identity number first.
		WebAgentSecurity laAgntSecSQL = new WebAgentSecurity(aaDBA);
		Vector lvAgntSecrtyData =
			laAgntSecSQL.qryWebAgentSecurity(aiAgntSecrtyIdntyNo);

		// If the count is not 1, there is a session problem!
		if (lvAgntSecrtyData.size() != 1)
		{
			Log.write(Log.APPLICATION, this, " - lookupAgntSecrtyRec  " + "Error 2338  - agent security data not found");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_TAB_ERR);
		}

		// format the security data.
		RtsWebAgntSecurityWS laLookupAgntSec =
			new RtsWebAgntSecurityWS(
				(WebAgentSecurityData) lvAgntSecrtyData.elementAt(0));
		RtsWebAgntSecurityWS[] larrLookupAgntSecrty =
			new RtsWebAgntSecurityWS[1];
		larrLookupAgntSecrty[0] = laLookupAgntSec;

		// get the agent data
		WebAgent laSQL = new WebAgent(aaDBA);
		RtsWebAgntWS laLookupAgnt = new RtsWebAgntWS();
		laLookupAgnt.setAgntIdntyNo(laLookupAgntSec.getAgntIdntyNo());
		Vector lvAgentData = laSQL.qryWebAgent(laLookupAgnt);

		// if there is more than one agent returned, we have bad problems!
		if (lvAgentData.size() != 1)
		{
			Log.write(Log.APPLICATION, this, " - lookupAgntSecrtyRec  " + "Error 2338  - multiple agent security");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_TAB_ERR);
		}

		// set up the data.
		laLookupAgnt = (RtsWebAgntWS) lvAgentData.elementAt(0);
		laLookupAgnt.setAgntSecurity(larrLookupAgntSecrty);
		carrAgentData = new RtsWebAgntWS[1];
		carrAgentData[0] = laLookupAgnt;
	}

	/**
	 * Authenticate User and Password.
	 * 
	 * @param asUser
	 * @param asPswd
	 * @param abDmvUser
	 * @return boolean
	 */
	public String authUser(String asUser, String asPswd)
		throws SOAPException, RTSException
	{
		//		return "true";
		// recompiling in Java 1.4.2
		String lsVerified = "false";

		// defect 10980
		// commented below as part of defect 10980
//		RtsLDAPAD laAD = new RtsLDAPAD();
//		// ****	lsVerified = new Boolean(laAD.login(asUser, asPswd)).toString();
//		if (laAD.login(asUser, asPswd))
//		{
//			lsVerified = "true";
//		}
//		else
//		{
			RtsLDAPeDir laEdir = new RtsLDAPeDir();
			lsVerified = laEdir.login(asUser, asPswd);
//		}
		// end defect 10980
		return lsVerified;
	}

	/**
	 * Process ths Logout Request
	 * 
	 * @param aaRequest
	 * @param aaDBA
	 * @return int
	 * @throws RTSException
	 */
	public int doLogout(
		RtsSessionRequest aaRequest,
		DatabaseAccess aaDBA)
		throws Exception
	{
		WebSession laSesSQL = new WebSession(aaDBA);
		WebSessionData laSessionData = new WebSessionData();
		laSessionData.setWebSessionId(aaRequest.getSessionId());
		Vector lvSessionLookup = laSesSQL.qryWebSession(laSessionData);
		if (lvSessionLookup.size() == 1)
		{
			// remove the session table data first
			laSesSQL.delWebSession(laSessionData);
			aaDBA.endTransaction(DatabaseAccess.COMMIT);

			if (!aaRequest.isDmvUserIndi())
			{
				WebSessionData laData =
					(WebSessionData) lvSessionLookup.elementAt(0);
				// Do not attempt to call the SOAPRSPS for now TODO
				//				RtsLDAPeDirAgnt laRtsLDAPeDirAgnt = new RtsLDAPeDirAgnt();
				//				try 
				//				{
				//					laRtsLDAPeDirAgnt.logout(laData.getEDirSessionId());
				//				} 
				//				catch (RemoteException leRex) 
				//				{
				//					leRex.printStackTrace();
				//				}
			}

			return 0;
		}
		else
		{
			Log.write(Log.APPLICATION, this, "doLogout - Session lookup returned zero or multiple rows" );
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_SESSION_ERR);
		}
	}
	/**
	 * Return the RtsWebAgntWS object.
	 * 
	 * @return RtsWebAgntWS[]
	 */
	public RtsWebAgntWS[] getAgentData()
	{
		return carrAgentData;
	}

	/**
	 * Check to see if User has Agency Information Access.
	 * 
	 * @param aiOfcissuanceNo
	 * @return boolean
	 */
	public boolean hasAgencyInfoAccess(int aiOfcissuanceNo)
	{
		boolean lbAccess = false;
		if (carrAgentData != null)
		{
			for (int i = 0; i < carrAgentData.length; i++)
			{
				lbAccess =
					(carrAgentData[i].getAgntSecurity())[0]
						.isAgncyAuthAccs();
			}
		}
		return lbAccess;
	}

	/**
	 * Check to see if user has Submit Batch Access.
	 * 
	 * @return boolean
	 */
	public boolean hasSubmitBatchAccess()
	{
		boolean lbAccess = false;
		if (carrAgentData != null)
		{
			for (int i = 0; i < carrAgentData.length; i++)
			{
				lbAccess =
					(carrAgentData[i].getAgntSecurity())[0]
						.isSubmitBatchAccs();
			}
		}
		return lbAccess;
	}

	/**
	 * Check to see if User has Agency Auth Access.
	 * 
	 * @param aiOfcissuanceNo
	 * @return boolean
	 */
	public boolean hasAgencyAuthAccess(int aiOfcissuanceNo)
	{
		boolean lbAccess = false;
		for (int i = 0; i < carrAgentData.length; i++)
		{
			lbAccess =
				(carrAgentData[i].getAgntSecurity())[0]
					.isAgncyAuthAccs();
		}
		return lbAccess;
	}

	/**
	 * Check to see if User has Agent Auth Access.
	 * 
	 * @return boolean
	 */
	public boolean hasAgentAuthAccess()
	{
		boolean lbAccess = false;
		if (carrAgentData != null)
		{
			for (int i = 0; i < carrAgentData.length; i++)
			{
				lbAccess =
					(carrAgentData[i].getAgntSecurity())[0]
						.isAgntAuthAccs();
			}
		}
		return lbAccess;
	}

}
