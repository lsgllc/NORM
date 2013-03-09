package com.txdot.isd.rts.webservices.agnt;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntRequest;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntResponse;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;
import com.txdot.isd.rts.webservices.agnt.util.RtsAgntHelper;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.ses.util.RtsSesHelper;

/*
 * RtsWebAgntService.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/20/2011	Initial load.
 * 							defect 10718 Ver 6.7.0
 * Min Wang		03/11/3011	Use the consistant for MSG length.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/01/2011	Send an empty string in place of the 
 * 							ReqIpAddr on session helper.
 * 							modify processAgentListRequest()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/07/2011	Working on the add agent process.
 * 							add processUpdateInsertAgent()
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/05/2011	Populate error message desc for return.
 * 							Use generic error number when we don't know
 * 							what other number to use.
 * 							modify processData()
 * 							defect 10718 Ver 6.7.1
 * Ray Rowehl	05/25/2011	Add validation of input.
 * 							modify processData()
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/08/2011	Add validation of access.
 * 							modify processData()
 * 							defect 10718 Ver 6.8.0
 * K McKee		06/09/2011	Added processAgentsForCounty() method
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/30/2011	Add delete agent processing.
 * 							add processDeleteAgent()
 * 							defect 10718 Ver 6.8.0
 * K McKee		08/22/2011	Added rollback logic  
 *							modify processData()
 * 							defect 10729 Ver 6.8.1
 * K McKee  	10/05 2011  Added logging for Exceptions
 * 							defect 10729 Ver 6.9.0
 * K McKee	    12/05/2011  Rearranged methods
 *                          defect 10729 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Web service to handle Agent data for Web Agent.
 *
 * @version	6.9.0			12/05/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/20/2011  12:00:01
 */

public class RtsWebAgntService
{
	/**
	 * Receive Agent Info requests and stores them for future action.
	 * 
	 * @param aarrRequest  (RtsWebAgntRequest[])
	 * @return RtsWebAgntResponse[]
	 */
	public RtsWebAgntResponse[] processData(RtsWebAgntRequest[] aarrRequest)
	{

		RtsWebAgntResponse[] larrResponse =
			new RtsWebAgntResponse[aarrRequest.length];
		DatabaseAccess laDBA = new DatabaseAccess();

		RtsSesHelper laSesHlpr = new RtsSesHelper();
		
		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsAgntService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());
			RtsWebAgntResponse laNewResponse =
				new RtsWebAgntResponse();

			try
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				
				laSesHlpr.getSession(
					aarrRequest[i].getSessionId(),
					aarrRequest[i].getCaller(),
					laDBA);

				// log the request
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsSrvcHstryData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				laDBA.beginTransaction();

				// validate the input data
				aarrRequest[i].validateAgntRequest();

				switch (aarrRequest[i].getAction())
				{
					case WebServicesActionsConstants
						.RTS_AGENT_INSERT_UPDATE :
						{
							 laNewResponse =
								new RtsWebAgntResponse();
								
							if (!laSesHlpr.hasAgentAuthAccess())
							{
								throw new RTSException(ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
							}

							// Process the update  / insert
							processUpdateInsertAgent(
								aarrRequest[i],
								laDBA);

							// get the new list
							processAgentListRequest(
								aarrRequest[i],
								laNewResponse,
								laDBA);

							larrResponse[i] = laNewResponse;

							laRtsSrvcHstryData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
						}
					case WebServicesActionsConstants.RTS_AGENT_DELETE :
						{
							 laNewResponse =
								new RtsWebAgntResponse();
								
							if (!laSesHlpr.hasAgentAuthAccess())
							{
								throw new RTSException(ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
							}

							// Process the delete
							processDeleteAgent(
								aarrRequest[i],
								laDBA);

							// get the new list
							processAgentListRequest(
								aarrRequest[i],
								laNewResponse,
								laDBA);

							larrResponse[i] = laNewResponse;

							laRtsSrvcHstryData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
						}
					case WebServicesActionsConstants
						.RTS_AGENT_LIST_REQUEST :
						{
							laNewResponse = new RtsWebAgntResponse();
							processAgentListRequest(aarrRequest[i],
									laNewResponse, laDBA);
							
							larrResponse[i] = laNewResponse;
							
							laRtsSrvcHstryData.setErrMsgNo(larrResponse[i]
									.getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
						}
					case WebServicesActionsConstants.RTS_AGENT_FOR_COUNTY :
						{
							 laNewResponse =
								new RtsWebAgntResponse();
								processAgentsForCounty(
								aarrRequest[i],
								laNewResponse,
								laDBA);

							larrResponse[i] = laNewResponse;

							laRtsSrvcHstryData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
						}
					case WebServicesActionsConstants.RTS_AGENT_RESET_PASSWORD :
						{
							laNewResponse = new RtsWebAgntResponse();

							if (!laSesHlpr.hasAgentAuthAccess())
							{
								throw new RTSException(ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
							}
							
							processResetPassword(aarrRequest[i]);
							
							laNewResponse.setErrMsgNo(0);
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
			//  10729   08/22
			catch (RTSException aeRTSEx)
			{
				laNewResponse.setErrMsgNo(
					aeRTSEx.getCode() == 0
						? ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR
						: aeRTSEx.getCode());
//				 limit the amount of info being sent back
				if (aeRTSEx.getDetailMsg().length()
					> ErrorsConstant.ERR_MSG_MAX_LNG_WEB_SERVICES)
				{
					laNewResponse.setErrMsgDesc(
						aeRTSEx.getDetailMsg().substring(
							0,
							ErrorsConstant
								.ERR_MSG_MAX_LNG_WEB_SERVICES));
				}
			}
			catch (Exception aeEx)
			{
				laNewResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				Log.write(Log.APPLICATION, this, "processData - Exception   ******  ERROR - 2300 ****  " + aeEx.toString());
			}

			if (laNewResponse.getErrMsgNo() != 0)
			{
				laNewResponse.setErrMsgDesc(
					ErrorMessagesCache.getErrMsgDesc(
						laNewResponse.getErrMsgNo()));
			}

			larrResponse[i] = laNewResponse;
			laRtsSrvcHstryData.setSuccessfulIndi(
				laNewResponse.getErrMsgNo() == 0);
			laRtsSrvcHstryData.setErrMsgNo(laNewResponse.getErrMsgNo());

			try
			{
				if (laDBA != null)
				{
					if (!laDBA.isConnected())
					{
						laDBA.beginTransaction();
					}
					else
					{
						if (laNewResponse.getErrMsgNo() != 0)
						{
							laDBA.endTransaction(
									DatabaseAccess.ROLLBACK);
							laDBA.beginTransaction();
						}
					}

					// Update request status in RTS_SRVC_HSTRY 
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsSrvcHstryData);
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					  
				}
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(Log.APPLICATION, this, "processData - RTSException -- " + " problem closing the dba " );
			}
		}
		//	END  10729   08/22
		return larrResponse;
	}

	/**
	 * Get the Agent List as requested.
	 * 
	 * @param aaRequest  (RtsWebAgntRequest)
	 * @param aaResponse (RtsWebAgntResponse)
	 * @param aaDBA      (DatabaseAccess)
	 */
	private void processAgentListRequest(
		RtsWebAgntRequest aaRequest,
		RtsWebAgntResponse aaResponse,
		DatabaseAccess aaDBA)
	{
		try
		{
			aaResponse.setErrMsgNo(-1);
			aaResponse.setVersionNo(0);
			aaResponse.setErrMsgDesc("");

			RtsWebAgntWS[] larrRtsWebAgntWSIn = aaRequest.getAgnt();
			for (int i = 0; i < larrRtsWebAgntWSIn.length; i++)
			{
				RtsWebAgntWS laAgntWS = larrRtsWebAgntWSIn[i];
				laAgntWS.setCheckDMVIndi(false);
				RtsAgntHelper laAgntHelper = new RtsAgntHelper();
				RtsWebAgntWS[] larrRtsWebAgntWSOut = null;

				larrRtsWebAgntWSOut =
					laAgntHelper.getAgentList(
						laAgntWS,
						aaDBA);
				aaResponse.setRtsWebAgnt(larrRtsWebAgntWSOut);
			}
			aaResponse.setErrMsgNo(0);
		}
		catch (RTSException aeRTSEx)
		{
			
			aaResponse.setRtsWebAgnt(null);

			if (aeRTSEx.getCode() > 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aaResponse.setErrMsgNo(-1);
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
	 * Process Update / Insert Agent requests.
	 * 
	 * @param aaRequest (RtsWebAgntRequest)
	 * @param aaDBA     (DatabaseAccess)
	 */
	private void processUpdateInsertAgent(
		RtsWebAgntRequest aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsWebAgntWS[] larrRtsWebAgntWSIn = aaRequest.getAgnt();
		for (int i = 0; i < larrRtsWebAgntWSIn.length; i++)
		{
			RtsAgntHelper laAgntHelper = new RtsAgntHelper();
			RtsWebAgntWS laAgntWS = larrRtsWebAgntWSIn[i];
			laAgntWS.setUpdtngAgntIdntyNo(Integer.parseInt(aaRequest.getCaller()));
			laAgntHelper.updateInsertAgentInfo(
				laAgntWS,
				aaDBA);
		}
	}
	
	/**
	 * Process Reset Password for Agent requests.
	 * 
	 * @param aaRequest (RtsWebAgntRequest)
	 * @param aaDBA     (DatabaseAccess)
	 */
	private void processResetPassword(
		RtsWebAgntRequest aaRequest)
		throws RTSException
	{
		RtsWebAgntWS[] larrRtsWebAgntWSIn = aaRequest.getAgnt();
		for (int i = 0; i < larrRtsWebAgntWSIn.length; i++)
		{
			RtsAgntHelper laAgntHelper = new RtsAgntHelper();
			RtsWebAgntWS laAgntWS = larrRtsWebAgntWSIn[i];
			laAgntWS.setUpdtngAgntIdntyNo(Integer.parseInt(aaRequest.getCaller()));
			laAgntHelper.resetAgentPassword(laAgntWS);
		}
	}
	
	/**
	 * Process Delete Requests.
	 * 
	 * @param aaRequest  (RtsWebAgntRequest)
	 * @param aaDBA      (DatabaseAccess)
	 * @throws RTSException
	 */
	private void processDeleteAgent(
		RtsWebAgntRequest aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsWebAgntWS[] larrRtsWebAgntWSIn = aaRequest.getAgnt();
		for (int i = 0; i < larrRtsWebAgntWSIn.length; i++)
		{
			RtsAgntHelper laAgntHelper = new RtsAgntHelper();
			RtsWebAgntWS laAgntWS = larrRtsWebAgntWSIn[i];
			laAgntWS.setUpdtngAgntIdntyNo(Integer.parseInt(aaRequest.getCaller()));
		
			laAgntHelper.deleteAgent(
				laAgntWS,
				aaDBA);
		}
	}	
	/**
	 * Get the List of users for each location in a County as requested.
	 * 
	 * @param aaRequest  (RtsWebAgntRequest)
	 * @param aaResponse (RtsWebAgntResponse)
	 * @param aaDBA      (DatabaseAccess)
	 */
	private void processAgentsForCounty(
		RtsWebAgntRequest aaRequest,
		RtsWebAgntResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		aaResponse.setErrMsgNo(-1);
		aaResponse.setVersionNo(0);
		aaResponse.setErrMsgDesc("");

		RtsWebAgntWS[] larrRtsWebAgntWSIn = aaRequest.getAgnt();
		RtsWebAgntWS laRtsWebAgntWS = larrRtsWebAgntWSIn[0];
	
		RtsAgntHelper laAgntHelper = new RtsAgntHelper();
		RtsWebAgntWS[] larrRtsWebAgntWSOut = null;
		larrRtsWebAgntWSOut =
			laAgntHelper.getAgentsForCountyList(laRtsWebAgntWS,aaDBA);
	
		aaResponse.setRtsWebAgnt(larrRtsWebAgntWSOut);
		aaResponse.setErrMsgNo(0);
	}
	 
}
