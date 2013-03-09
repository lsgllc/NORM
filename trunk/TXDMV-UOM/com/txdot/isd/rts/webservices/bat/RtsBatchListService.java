package com.txdot.isd.rts.webservices.bat;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.webservices.bat.data.RtsBatchListRequest;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchListResponse;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchListSummaryLine;
import com.txdot.isd.rts.webservices.bat.util.RtsBatchHelper;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;

import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RtsBatchListService.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/04/2011	Initial load.
 * 							defect 10673 Ver 6.7.0
 * Min Wang		03/11/2011	Make max message length 100.
 * 							modify processData()
 * 							defect 10673 Ver 6.7.1
 * Min Wang		03/21/3011	Use the right consistant for MSG length.
 * 							modify processData()
 * 							defect 10673 Ver 6.7.1
 * K Harrell	04/208/2011	...working 
 * 							defect 10785 Ver 6.7.1  
 * K McKee  	10/05 2011  Added logging for Exceptions
 * 							defect 10729 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Web Service to return all batches for a chosen Agency.
 *
 * @version	6.7.1			04/20/2011
 * @author	Ray Rowehl
 * @author  Kathy Harrell 
 * <br>Creation Date:		01/04/2011 13:24:03
 */

public class RtsBatchListService
{
	/**
	 * Receive transaction requests and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsRenewalRcptResponse[]
	 */
	public RtsBatchListResponse[] processData(RtsBatchListRequest[] aarrRequest)
	{
		RtsBatchListResponse[] larrResponse =
			new RtsBatchListResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsBatchListService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			RtsBatchListResponse laNewResponse =
				new RtsBatchListResponse();

			// Return Search Criteria in Response Object  
			laNewResponse.setSearchBatchStatusCd(
				aarrRequest[i].getSearchBatchStatusCd());
			laNewResponse.setSearchStartDate(
				aarrRequest[i].getSearchStartDate());
			laNewResponse.setSearchEndDate(
				aarrRequest[i].getSearchEndDate());

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
					case WebServicesActionsConstants.RTS_BATCH_SUBMIT :
						{
							// Update Batch to Submitted
							processStatusChange(
								laDBA,
								aarrRequest[i],
								RegistrationConstant
									.SUBMIT_BATCHSTATUSCD);

							// Return updated Batch List  
							processBatchListRequestLine(
								aarrRequest[i],
								laNewResponse,
								laDBA);

							break;
						}
					case WebServicesActionsConstants
						.RTS_BATCH_APPROVAL :
						{
							// Update Batch to Approved 
							processStatusChange(
								laDBA,
								aarrRequest[i],
								RegistrationConstant
									.APPROVED_BATCHSTATUSCD);

							// Return updated Batch List
							processBatchListRequestLine(
								aarrRequest[i],
								laNewResponse,
								laDBA);
							break;

						}
					case WebServicesActionsConstants
						.RTS_BATCH_LIST_REQUEST :
						{
							processBatchListRequestLine(
								aarrRequest[i],
								laNewResponse,
								laDBA);
							break;
						}
					default :
						{
						Log.write(Log.APPLICATION, this, "processData -  ******  ERROR - 2300 ****  invalid Action Type");
							throw new RTSException(
								ErrorsConstant
									.ERR_NUM_WEBAGNT_GENERAL_ERROR);
						}
				}
			}
			catch (RTSException aeRTSEx)
			{
				laNewResponse.setErrMsgNo(
					aeRTSEx.getCode() == 0
						? ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR
						: aeRTSEx.getCode());
			}
			catch (Exception aeEx)
			{
				laNewResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				Log.write(Log.APPLICATION, this, "processData - Exception -- ******  ERROR - 2300 ****" +  aeEx.toString());
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
						if (laNewResponse.getErrMsgNo() != 0
							&& laNewResponse.getErrMsgNo()
								!= ErrorsConstant
									.ERR_NUM_WEBAGNT_BATCH_TOO_MANY_ROWS)
						{
							laDBA.endTransaction(
								DatabaseAccess.ROLLBACK);
							// UOW #2 END 

							// UOW #3 BEGIN  
							laDBA.beginTransaction();
						}
					}

					// Update request status in RTS_SRVC_HSTRY 
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsSrvcHstryData);
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					// UOW #2 (OR #3)  END  
				}
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(Log.APPLICATION, this, "processData - Exception -- " + " problem closing the dba ");
			}
		}

		return larrResponse;
	}

	/**
	 * Process Requests to Submit or Approve Batch
	 * 
	 * @param aaDBA
	 * @param aaRequest
	 * @param asNewStatus
	 * @throws RTSException
	 */
	private void processStatusChange(
		DatabaseAccess aaDBA,
		RtsBatchListRequest aaRequest,
		String asNewStatus)
		throws RTSException
	{
		RtsBatchHelper laRtsBatchHelper =
			new RtsBatchHelper(aaRequest, null, aaDBA);
		laRtsBatchHelper.processStatusChange(asNewStatus);
	}

	/**
	 * Process a request for a list of batches.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 * @throws RTSException
	 */
	private void processBatchListRequestLine(
		RtsBatchListRequest aaRequest,
		RtsBatchListResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsBatchHelper laBatHelper =
			new RtsBatchHelper(aaRequest, null, aaDBA);
		Hashtable lhtReturn = laBatHelper.getBatchList();

		Vector lvBatchList =
			(Vector) lhtReturn.get(RegistrationConstant.BATCHES_ENTRY);

		boolean lbMaxReached =
			((Boolean) lhtReturn
				.get(RegistrationConstant.MAXREACHED_ENTRY))
				.booleanValue();

		RtsBatchListSummaryLine[] larrBatchListSummary =
			new RtsBatchListSummaryLine[lvBatchList.size()];

		lvBatchList.toArray(larrBatchListSummary);

		aaResponse.setBatchSummaryLine(larrBatchListSummary);

		aaResponse.setErrMsgNo(
			lbMaxReached
				? ErrorsConstant.ERR_NUM_WEBAGNT_BATCH_TOO_MANY_ROWS
				: 0);
	}

}
