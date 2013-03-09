package com.txdot.isd.rts.webservices.ses;

import java.util.Vector;

import org.apache.soap.SOAPException;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.WebServicesServiceHistorySql;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncy;
import com.txdot.isd.rts.webservices.agncy.util.RtsAgncyHelper;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntSecurityWS;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;
import com.txdot.isd.rts.webservices.agnt.util.RtsAgntHelper;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.ses.data.RtsSessionRequest;
import com.txdot.isd.rts.webservices.ses.data.RtsSessionResponse;
import com.txdot.isd.rts.webservices.ses.util.RtsSesHelper;

/*
 * RtsAgntLoginService.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/03/2011	Initial load.
 * 							defect 10670 Ver 670
 * Min Wang		03/11/2011	Make max message length 100.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	03/18/2011	Add Logout function.  
 * 							Provide better error numbers.
 * 							add processLogout()
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	03/22/2011	Add Rollback!
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Min Wang		03/21/2011	Use right constant for error msg.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	03/30/2011	Remove Batch List processing.
 * 							delete getBatchStatus()
 * 							modify processLogin(), processLogout()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/01/2011	Pass the ReqIpAddr to get agent list.
 * 							modify getAgentData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/04/2011	Throw right error when the user password 
 * 							combination is not valid.
 * 							Add the look up for the last successful Login.
 * 							add setLastSuccessfulLogin()
 * 							modify processLogin()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/08/2011	Return a null calendar if there is no last
 * 							successful logon.
 * 							modify setLastSuccessfulLogin()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl 	04/20/2011	Bypass authentication!
 * 							modify getAgentData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/28/2011	modify error number to be unique.	
 * 							modify processLogin()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/02/2011	Catch SOAPException and report eDir Server 
 * 							error.
 * 							modify getAgentData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/05/2011	Remove work around check for pasword#1.
 * 							modify getAgentData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/06/2011	Return generic web agent error if we get 
 * 							unexpected errors.
 * 							modify processLogin(), processLogout()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	06/11/2011	Work on blocking an empty password.
 * 							modify getAgentData()
 * 							defect 10670 Ver 6.8.0
 * Dan Hamilton	06/11/2011	Rework getAgencyList() to properly return the
 * 							agency list after the change to RtsWebAgntWS.
 * 							defect 10670 Ver 6.8.0
 * K McKee		10/05/2011  Added logging for Generic Error 2300
 * 							defect 10729 Ver 6.9.0
 * R Pilon		01/23/2012	Modified exceptions thrown for agent authentication.
 * 							modify getAgentData()
 * 							defect 11190 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * The web service to handle Agent Login.
 *
 * @version	6.10.0			01/23/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		01/03/2011 15:28:45
 */

public class RtsAgntLoginService
{



	/**
	 * Receive transaction requests and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsRenewalRcptResponse[]
	 */
	public RtsSessionResponse[] processData(RtsSessionRequest[] aarrRequest)
	{
		RtsSessionResponse[] larrResponse =
			new RtsSessionResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsAgntLoginService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			try
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();

				// log the request
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsSrvcHstryData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);

				laDBA.beginTransaction();

				switch (aarrRequest[i].getAction())
				{
					case WebServicesActionsConstants.RTS_AGENT_LOGIN :
						{
							// perform login
							RtsSessionResponse laNewResponse =
								new RtsSessionResponse();
							processLogin(
								aarrRequest[i],
								laNewResponse,
								laDBA,
								liSAVId);
							larrResponse[i] = laNewResponse;

							laRtsSrvcHstryData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
						}
					case WebServicesActionsConstants.RTS_AGENT_LOGOUT :
						{
							// perform logout
							RtsSessionResponse laNewResponse =
								new RtsSessionResponse();
							processLogout(
								aarrRequest[i],
								laNewResponse,
								laDBA);
							larrResponse[i] = laNewResponse;

							laRtsSrvcHstryData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
						}
					default :
						{
							larrResponse[i] = null;
							break;
						}
				}
			}
			catch (RTSException aeRTSEx)
			{
				// make sure we do a rollback if needed.
				if (laDBA != null)
				{
					try
					{
						laDBA.endTransaction(DatabaseAccess.ROLLBACK);
					}
					catch (RTSException aeRTSE2)
					{
						aeRTSE2.writeExceptionToLog();
					}
				}

				RtsSessionResponse laNewResponse =
					new RtsSessionResponse();
				if (aeRTSEx.getCode() != 0)
				{
					laNewResponse.setErrMsgNo(aeRTSEx.getCode());
				}
				else
				{
					// TODO  Get a better error message!!
				
					laNewResponse.setErrMsgNo(
						ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);

				}
				if (aeRTSEx.getDetailMsg() != null)
				{
					// limit the amount of info being sent back
					if (aeRTSEx.getDetailMsg().length()
						> ErrorsConstant.ERR_MSG_MAX_LNG_WEB_SERVICES)
					{
						laNewResponse.setErrMsgDesc(
							aeRTSEx.getDetailMsg().substring(
								0,
								ErrorsConstant
									.ERR_MSG_MAX_LNG_WEB_SERVICES));
					}
					else
					{
						laNewResponse.setErrMsgDesc(
							aeRTSEx.getDetailMsg());
					}
				}

				larrResponse[i] = laNewResponse;

				laRtsSrvcHstryData.setSuccessfulIndi(false);
				laRtsSrvcHstryData.setErrMsgNo(
					laNewResponse.getErrMsgNo());
			}

			try
			{
				if (laDBA != null)
				{
					if (!laDBA.isConnected())
					{
						laDBA.beginTransaction();
					}

					// write out the update to the tracking row.
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsSrvcHstryData);

					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(Log.APPLICATION, this, "processData - Exception -- " + " problem closing the dba" );
			}
		}

		return larrResponse;
	}

	/**
	 * Process the Login Request
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processLogin(
		RtsSessionRequest aaRequest,
		RtsSessionResponse aaResponse,
		DatabaseAccess aaDBA,
		int aiSAVId)
	{
		// recompiling in Java 1.4.2
		try
		{
			aaResponse.setErrMsgNo(-1);
			aaResponse.setErrMsgDesc("");
			aaResponse.setVersionNo(0);

			getAgentData(aaRequest, aaResponse, aaDBA);

			if (aaResponse.getRtsWebAgntWs().length == 0)
			{
				// defect 10670
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_TAB2_ERR);
				// end defect 10670
			}

			// Lookup last Successful TimeStamp
			setLastSuccessfulLogin(
				aaRequest,
				aaResponse,
				aaDBA,
				aiSAVId);

			getAgencyList(aaResponse, aaDBA);

			//getBatchStatus(aaResponse, aaDBA);

			aaResponse.setErrMsgNo(0);

		}
		catch (RTSException aeRTSEx)
		{
			aaResponse.setRtsWebAgntWs(null);
			aaResponse.setRtsWebAgncy(null);
			//aaResponse.setRtsBatchListSummaryLines(null);

			if (aeRTSEx.getCode() > 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				// defect 10670
				aaResponse.setErrMsgNo(ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				aeRTSEx.setCode(ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				aeRTSEx.setDetailMsg(null);
				// end defect 10670
			}

			if (aeRTSEx.getDetailMsg() != null
				&& aeRTSEx.getDetailMsg().length() > 0)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
			else
			{
				if (ErrorMessagesCache.getErrMsg(aeRTSEx.getCode())
					!= null)
				{
					aaResponse.setErrMsgDesc(
						ErrorMessagesCache
							.getErrMsg(aeRTSEx.getCode())
							.getErrMsgDesc());
				}
				else
				{
					aaResponse.setErrMsgDesc("");
				}
			}
		}

	}
	/**
	 * Get the Agency list matching the Agent.
	 * 
	 * @param aaResponse
	 * @param aaDBA
	 * @param larrRtsWebAgntWS
	 * @throws RTSException
	 */
	private void getAgencyList(
		RtsSessionResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsWebAgntWS[] larrRtsWebAgntWS = aaResponse.getRtsWebAgntWs();

		// Get related Agencies.
		RtsAgncyHelper laAgncyHelper = new RtsAgncyHelper();
		Vector lvAgencyList = new Vector();

		// Move the agent outside the loop since there is now only 1 element
		RtsWebAgntWS laAgencyAgentData = larrRtsWebAgntWS[0];
		// loop over the list of agent security elements
		for (int i = 0; i < laAgencyAgentData.getAgntSecurity().length; i++)
		{
			RtsWebAgncy laWebAgencyData = new RtsWebAgncy();
			// get the Agency number from the current agent security element
			laWebAgencyData.setAgncyIdntyNo(
				(laAgencyAgentData.getAgntSecurity())[i].getAgncyIdntyNo());
			Vector lvTempData =
				laAgncyHelper.getAgencyList(laWebAgencyData, aaDBA);
			lvAgencyList.addAll(lvTempData);
		}

		RtsWebAgncy[] larrWebAgency =
			new RtsWebAgncy[lvAgencyList.size()];
		lvAgencyList.toArray(larrWebAgency);
		aaResponse.setRtsWebAgncy(larrWebAgency);
	}

	/**
	 * Get the Agent Data.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 * @return
	 * @throws RTSException
	 */
	private RtsWebAgntWS[] getAgentData(
		RtsSessionRequest aaRequest,
		RtsSessionResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{

		String lsLoginResponse = "";
		// Verify the password only on the first pass.
		if (aaRequest.getAgntSecrtyIdntyNo() < 1
			&& aaRequest.getAction()
				== WebServicesActionsConstants.RTS_AGENT_LOGIN)
		{
			try
			{
				// TODO
				// Work on empty password issue
				if (aaRequest.getPswd() == null
				|| aaRequest.getPswd().length() < 1)
				{
					// defect 11190
					throw new RTSException(
							ErrorsConstant.ERR_NUM_USERID_PWD_REQUIRED);
					// end defect 11190
				}
				// Check the user and password
				RtsSesHelper laSesHlpr = new RtsSesHelper();
				// remove aaRequest.isDmvUserIndi() 
				lsLoginResponse =
					laSesHlpr.authUser(
						aaRequest.getCaller(),
						aaRequest.getPswd());
				//		aaRequest.isDmvUserIndi());
				if (lsLoginResponse.equals("false")
					|| lsLoginResponse.indexOf(":") > -1
					|| lsLoginResponse.equals(""))
				{
					// defect 11190
					throw new RTSException(
							ErrorsConstant.ERR_NUM_WEBAGNT_UNKNOWN_AUTH_LDAP_ERR);
					// end defect 11190
				}
			}
			catch (SOAPException aeSoapEx)
			{
				// defect 11190
				aeSoapEx.printStackTrace();
				// end defect 11190
				throw new RTSException(
						ErrorsConstant.ERR_NUM_WEBAGNT_EDIR_SOAP_ERR);
			}
		}

		// lookup the Agent data
		RtsWebAgntWS laAgent = new RtsWebAgntWS();
		laAgent.setUserName(aaRequest.getCaller());
		laAgent.setDmvUserIndi(aaRequest.isDmvUserIndi());

		// TODO
		laAgent.setCheckDMVIndi(false);

		if (laAgent.getAgntSecurity() == null)
		{
			RtsWebAgntSecurityWS[] larrWebAgntSec = new RtsWebAgntSecurityWS[1];
			RtsWebAgntSecurityWS laWebAgntSecRow = new RtsWebAgntSecurityWS();
			larrWebAgntSec[0] = laWebAgntSecRow;
			laAgent.setAgntSecurity(larrWebAgntSec);
		}
		
		if (aaRequest.getAgntSecrtyIdntyNo() > 0)
		{
			(laAgent.getAgntSecurity())[0].setAgntSecrtyIdntyNo(aaRequest.getAgntSecrtyIdntyNo());
		}

		RtsAgntHelper laAgntHelper = new RtsAgntHelper();
		RtsWebAgntWS[] larrRtsWebAgntWS = null;

		String lsEdirSessionID = null;

		larrRtsWebAgntWS =
			laAgntHelper.getAgentForLogin(
				laAgent,
				aaRequest,
				aaDBA,
				lsEdirSessionID);

		aaResponse.setRtsWebAgntWs(larrRtsWebAgntWS);
		return larrRtsWebAgntWS;
	}

	//	/**
	//	 * Get Batch Statuses.
	//	 * 
	//	 * @param aaResponse
	//	 * @param aaDBA
	//	 * @throws RTSException
	//	 */
	//	private void getBatchStatus(
	//		RtsSessionResponse aaResponse,
	//		DatabaseAccess aaDBA)
	//		throws RTSException
	//	{
	//		RtsBatchHelper laBatchHelper = new RtsBatchHelper();
	//		RtsWebAgncy[] larrWebAgncy = aaResponse.getRtsWebAgncy();
	//		Vector lvBatchStatus = new Vector();
	//
	//		for (int i = 0; i < larrWebAgncy.length; i++)
	//		{
	//			RtsBatchListRequest laBatchListReq =
	//				new RtsBatchListRequest();
	//			laBatchListReq.setAgencyIdntyNo(
	//				larrWebAgncy[i].getAgncyIdntyNo());
	//
	//			// if we know who the caller is, use that for security
	//			if (aaResponse.getRtsWebAgntWs().length == 1)
	//			{
	//				laBatchListReq.setCaller(
	//					String.valueOf(
	//						aaResponse
	//							.getRtsWebAgntWs()[0]
	//							.getAgntSecrtyIdntyNo()));
	//			}
	//			else
	//			{
	//				laBatchListReq.setCaller("0");
	//			}
	//
	//			Vector lvBatTemp =
	//				laBatchHelper.getBatchList(laBatchListReq, aaDBA);
	//			lvBatchStatus.addAll(lvBatTemp);
	//		}
	//		RtsBatchListSummaryLine[] larrBatchStatuses =
	//			new RtsBatchListSummaryLine[lvBatchStatus.size()];
	//		lvBatchStatus.toArray(larrBatchStatuses);
	//
	//		aaResponse.setRtsBatchListSummaryLines(larrBatchStatuses);
	//	}
	/**
	 * Set the Last Successful Login Timestamp.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 * @param aiSAVId
	 * @throws RTSException
	 */
	private void setLastSuccessfulLogin(
		RtsSessionRequest aaRequest,
		RtsSessionResponse aaResponse,
		DatabaseAccess aaDBA,
		int aiSAVId)
		throws RTSException
	{
		WebServicesServiceHistorySql laWSHSql =
			new WebServicesServiceHistorySql(aaDBA);
		RTSDate laReqRtsDate =
			laWSHSql.qryMaxSelectedSavForCaller(
				aaRequest.getCaller(),
				aiSAVId,
				0);

		if (laReqRtsDate != null)
		{
			aaResponse.setLastLoginSuccessful(
				laReqRtsDate.getCalendar());
		}
		else
		{
			aaResponse.setLastLoginSuccessful(null);
		}
	}

	/**
	 * Process the Logout Request
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processLogout(
		RtsSessionRequest aaRequest,
		RtsSessionResponse aaResponse,
		DatabaseAccess aaDBA)
	{

		try
		{
			aaResponse.setErrMsgNo(-1);
			aaResponse.setErrMsgDesc("");
			aaResponse.setVersionNo(0);

			RtsSesHelper laSesHlpr = new RtsSesHelper();
			//			if (!aaRequest.isDmvUserIndi())
			//			{
			//				RtsLDAPeDirAgnt laRtsLDAPeDirAgnt = new RtsLDAPeDirAgnt();
			//				try 
			//				{
			//					laRtsLDAPeDirAgnt.logout(aaRequest.getSessionId());
			//				} 
			//				catch (RemoteException leRex) 
			//				{
			//					leRex.printStackTrace();
			//				}
			//			}
			int liNumberOfRows = 0;
			try
			{
				liNumberOfRows = laSesHlpr.doLogout(aaRequest, aaDBA);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			aaDBA.endTransaction(DatabaseAccess.COMMIT);

			if (liNumberOfRows != 1)
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_SESSION_ERR);
			}

			aaResponse.setErrMsgNo(0);

		}
		catch (RTSException aeRTSEx)
		{
			aaResponse.setRtsWebAgntWs(null);
			aaResponse.setRtsWebAgncy(null);
			//aaResponse.setRtsBatchListSummaryLines(null);

			if (aeRTSEx.getCode() > 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				// defect 10670
				aeRTSEx.printStackTrace();
				aaResponse.setErrMsgNo(ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				aeRTSEx.setCode(ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				aeRTSEx.setDetailMsg(null);
				// end defect 10670
			}

			if (aeRTSEx.getDetailMsg() != null
				&& aeRTSEx.getDetailMsg().length() > 0)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
			else
			{
				if (ErrorMessagesCache.getErrMsg(aeRTSEx.getCode())
					!= null)
				{
					aaResponse.setErrMsgDesc(
						ErrorMessagesCache
							.getErrMsg(aeRTSEx.getCode())
							.getErrMsgDesc());
				}
				else
				{
					aaResponse.setErrMsgDesc("");
				}
			}
		}

	}

}
