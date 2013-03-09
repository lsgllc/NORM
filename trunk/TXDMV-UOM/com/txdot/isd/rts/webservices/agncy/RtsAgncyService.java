package com.txdot.isd.rts.webservices.agncy;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.agncy.data.RtsAgncyRequest;
import com.txdot.isd.rts.webservices.agncy.data.RtsAgncyResponse;
import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncy;
import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncyAuthCfg;
import com.txdot.isd.rts.webservices.agncy.util.RtsAgncyHelper;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.ses.util.RtsSesHelper;

/*
 * RtsAgncyService.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/05/2011	Initial load.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	01/24/2011	Restructure for new sql setup.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	03/11/2011	Make max message length 100.
 * 							modify processData()
 * 							defect 10718 Ver 6.7.0
 * Min Wang		03/11/3011	Use the consistant for MSG length.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	03/17/2011	Add call to check session validity.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Min Wang		03/21/3011	Use right consistant for MSG length.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/25/2011	Add validation check to the insert/update 
 * 							process.
 * 							modify processData()
 * 							defect 10718 Ver 6.8.0
 * Kathy McKee  08/19/2011  Add call to delete agency
 *							modify processData()
 * 							add processDeleteAgencyRequest()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  08/22/2011  Added rollback logic  
 *							modify processData()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  09/15/2011  Added call to deleteAgencyAgentSecurity  
 *							modify processDeleteAgencyRequest()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  10/05 2011  Added logging for Exceptions
 * 							defect 10729 Ver 6.9.0
 * Kathy McKee  10/20/2011  Modifications for Auth and Auth Cfg array
 *                          modify processDeleteAgencyRequest()
 *                          defect 11151 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Web Service to provide Query and Maintenance of Agency Data.
 *
 * @version	6.9.0 			10/20/2011 
 * @author	Ray Rowehl
 * <br>Creation Date:		01/05/2011 13:47:27
 */

public class RtsAgncyService
{
	
	/**
	 * Receive Agency Info requests and stores them for future action.
	 * 
	 * @param aarrRequest   (RtsAgncyRequest[])
	 * @return RtsAgncyResponse[]
	 */
	public RtsAgncyResponse[] processData(RtsAgncyRequest[] aarrRequest)
	{
		 
		RtsAgncyResponse[] larrResponse =
			new RtsAgncyResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsAgncyService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());
			
			RtsAgncyResponse laNewResponse =
				new RtsAgncyResponse();

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
				
				// if the session is not valid, the helper will 
				// throw an RTSException.
				RtsSesHelper laSesHlpr = new RtsSesHelper();
				laSesHlpr.getSession(aarrRequest[i].getSessionId(), 
					Integer.parseInt(aarrRequest[i].getCaller()), 
					laDBA);

				switch (aarrRequest[i].getAction())
				{
					case WebServicesActionsConstants
						.RTS_AGENCY_INSERT_UPDATE :
						{
							// add validation of data before insert/update
							aarrRequest[i].validateAgncyRequest();
							
							processAddUpdateAgencyRequest(aarrRequest[i], laNewResponse, laDBA);
							
							processAgencyListRequest(
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
						.RTS_AGENCY_DELETE :
						{
							// Begin defect 10769 08/15
							aarrRequest[i].validateAgncyRequest();
							
							laNewResponse = new RtsAgncyResponse();
								
							processDeleteAgencyRequest(
								aarrRequest[i],
								laNewResponse,
								laDBA);

							larrResponse[i] = laNewResponse;

							laRtsSrvcHstryData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsSrvcHstryData.setSuccessfulIndi(true);
							break;
							// End defect 10769 08/15
						}
					case WebServicesActionsConstants
						.RTS_AGENCY_LIST_REQUEST :
						{
							if (!laSesHlpr.hasAgencyAuthAccess(0) && !laSesHlpr.hasAgencyInfoAccess(0))
							{
								throw new RTSException(ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
							}
							
							laNewResponse = new RtsAgncyResponse();

							processAgencyListRequest(
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
				Log.write(Log.APPLICATION, this, "processData -  ******  ERROR - 2300 ****   " +  aeEx.toString());
				laNewResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
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
							if (laNewResponse.getErrMsgNo() !=
								ErrorsConstant.MSG_UNSUBMITTED_BATCHES_FOR_DELETED_AGENCY)  
							{
								laDBA.endTransaction(DatabaseAccess.ROLLBACK);
								laDBA.beginTransaction();
							}
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
				Log.write(Log.APPLICATION, this, "processData -  had a problem closing the dba " ); 
			}
		}
		//	END  10729   08/22
		return larrResponse;
	}

	/**
	 * Get the Agency List as requested.
	 * 
	 * @param aaRequest  (RtsAgncyRequest)
	 * @param aaResponse (RtsAgncyResponse)
	 * @param aaDBA      (DatabaseAccess)
	 */
	private void processAgencyListRequest(
		RtsAgncyRequest aaRequest,
		RtsAgncyResponse aaResponse,
		DatabaseAccess aaDBA)
	{
		try
		{
			aaResponse.setErrMsgNo(0);
			aaResponse.setVersionNo(0);
			aaResponse.setErrMsgDesc("");

			RtsWebAgncy laRequestDtl = aaRequest.getRtsWebAgncyInput();
			RtsWebAgncy[] larrRWA = null;

			RtsAgncyHelper laAgncyHelper = new RtsAgncyHelper();

			Vector lvRWA =
				laAgncyHelper.getAgencyList(laRequestDtl, aaDBA);

			// translate the data
			larrRWA = new RtsWebAgncy[lvRWA.size()];
			lvRWA.toArray(larrRWA);
			aaResponse.setRtsWebAgncyOut(larrRWA);
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() > 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aeRTSEx.setCode(-1);
			}
			if (aeRTSEx.getMessage() != null
				&& aeRTSEx.getMessage().length() > 0)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getMessage());
			}
		}
	}

	/**
	 * Process Add / Update Agency Request.
	 * 
	 * @param aaRequest   (RtsAgncyRequest)
	 * @param aaResponse  (RtsAgncyResponse)
	 * @param aaDBA       (DatabaseAccess)
	 * @throws RTSException
	 */
	private void processAddUpdateAgencyRequest(
		RtsAgncyRequest aaRequest,
		RtsAgncyResponse aaResponse,
		DatabaseAccess aaDBA) throws RTSException
	{
		RtsAgncyHelper laAgncyHelper = new RtsAgncyHelper();
		RtsWebAgncy laRtsWebAgencyData = new RtsWebAgncy(aaRequest);
		laAgncyHelper.addUpdateAgency(laRtsWebAgencyData, aaDBA);
	}
 
	
	/**
	 * Delete the Agency by updating the delete indicator
	 * 
	 * @param aaRequest  (RtsAgncyRequest)
	 * @param aaResponse (RtsAgncyResponse) 
	 * @param aaDBA      (DatabaseAccess)
	 */
	private void processDeleteAgencyRequest(
			RtsAgncyRequest aaRequest,
			RtsAgncyResponse aaResponse,
			DatabaseAccess aaDBA)  throws RTSException
	{

		aaResponse.setErrMsgNo(0);
		aaResponse.setVersionNo(0);
		aaResponse.setErrMsgDesc("");

		RtsWebAgncy laRequest = aaRequest.getRtsWebAgncyInput();

		RtsAgncyHelper laAgncyHelper = new RtsAgncyHelper();
		// defect 11151
		RtsWebAgncyAuthCfg laRtsWebAgncyAuthCfg = laRequest.getAgncyAuthCfgs()[0];
		// end defect 11151
		// Set the delete indicator for the agency auth
		laAgncyHelper.deleteAgencyAuth(laRtsWebAgncyAuthCfg, aaDBA);
		// Set the delete indicator for the agency auth cfg
		laAgncyHelper.deleteAgencyAuthCfg(laRtsWebAgncyAuthCfg, aaDBA);
		// defect 10729 Begin
		// Set the delete indicator for the agent security 
		laAgncyHelper.deleteAgencyAgentSecurity(laRequest, aaDBA);
		// defect 10729 End
		
		// if there are open batches, process the delete but set 
		// the error message to display on the window
		
		// Set the delete indicator for the agency
		laAgncyHelper.deleteAgency(laRequest, aaDBA);

		// Set any non submitted batches to submitted and display
		// a message

		int liOpenBatchCount = 
			laAgncyHelper.updPendingWebAgencyBatch(laRtsWebAgncyAuthCfg, aaDBA);

		if (liOpenBatchCount > 0)
		{
			aaResponse.setErrMsgNo(
					ErrorsConstant.MSG_UNSUBMITTED_BATCHES_FOR_DELETED_AGENCY);
			aaResponse.setErrMsgDesc(
					ErrorMessagesCache.getErrMsgDesc(
							aaResponse.getErrMsgNo()));
		}
	}
}
