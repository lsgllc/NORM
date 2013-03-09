package com.txdot.isd.rts.webservices.bat;

import com.txdot.isd.rts.webservices.bat.data.RtsBatchDetailRequest;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchDetailResponse;
import com.txdot.isd.rts.webservices.bat.util.RtsBatchHelper;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;

import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RtsBatchDetailService.java
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
 * Min Wang		03/21/3011	Use right constant for MSG length.
 * 							modify processData()
 * 							defect 10670 Ver 6.7.1
 * K Harrell	04/20/2011	...working 
 * 							defect 10785 Ver 6.7.1 
 * K McKee      10/05/2011  Added logging for errors
 * 							defect 10729 Ver 6.9.0
 * K McKee		12/30/2011  Added plate search action
 * 							modify processData()
 * 							add processPlateSearchRequest()
 * 							defect 11239 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Web Service to handle the details of a Batch.
 *
 * @version	6.10.0			12/30/2011
 * @author	Ray Rowehl
 * @author  Kathy Harrell 
 * <br>Creation Date:		01/19/2011  7:16:04
 */

public class RtsBatchDetailService
{
	/**
	 * Return Transactions for Given Batch 
	 * 
	 * @param aaRequest RtsBatchDetailRequest
	 * @param aaResponse RtsBatchDetailResponse
	 * @param aaDBA 	DatabaseAccess
	 * @throws RTSException
	 */
	private void processBatchDetailRequest(
		RtsBatchDetailRequest aaRequest,
		RtsBatchDetailResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsBatchHelper laRtsBatchHelper =
			new RtsBatchHelper(null, aaRequest, aaDBA);
		laRtsBatchHelper.getBatchDetail(aaResponse);
	}

	/**
	 * Process Lookup Requests
	 * 
	 * @param aarrRequest RtsBatchDetailRequest[]
	 * @return RtsBatchDetailResponse[]
	 */
	public RtsBatchDetailResponse[] processData(RtsBatchDetailRequest[] aarrRequest)
	{
		RtsBatchDetailResponse[] larrResponse =
			new RtsBatchDetailResponse[aarrRequest.length];

		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsBatchDetailService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			RtsBatchDetailResponse laNewResponse =
				new RtsBatchDetailResponse();

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
					case WebServicesActionsConstants
						.RTS_BATCH_DETAIL_REPRINT :
						{
							processReprintRequest(
								aarrRequest[i],
								laDBA);
							break;
						}
					case WebServicesActionsConstants
						.RTS_BATCH_DETAIL_VOID :
						{
							processVoidRequest(aarrRequest[i], laDBA);
							break;
						}
					case WebServicesActionsConstants
						.RTS_BATCH_DETAIL_REQUEST :
						{
							processBatchDetailRequest(
								aarrRequest[i],
								laNewResponse,
								laDBA);
							break;
						}

					case WebServicesActionsConstants
						.RTS_BATCH_PLATE_SEARCH :
					{
						//	defect 11239
						processPlateSearchRequest(
							aarrRequest[i],
							laNewResponse,
							laDBA);
						break;
						// end defect 11239 
					}
					
					default :
						{
							Log.write(Log.APPLICATION, this, "processData -  ******  ERROR - 2300 ****  invalid Action Type");
							throw new RTSException(
								ErrorsConstant
									.ERR_NUM_WEBAGNT_GENERAL_ERROR);
						}
				}
				laNewResponse.setErrMsgNo(0);
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
				Log.write(Log.APPLICATION, this, "processData - Exception --  ******  ERROR - 2300 ****" +  aeEx.toString());
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
				Log.write(Log.APPLICATION, this, "processData - Exception -- " + " problem closing the dba " );
			}
		}
		return larrResponse;
	}
	/**
	 * Return Transactions for Given Plate 
	 * 
	 * @param aaRequest RtsBatchDetailRequest
	 * @param aaResponse RtsBatchDetailResponse
	 * @param aaDBA 	DatabaseAccess
	 * @throws RTSException
	 */
	private void processPlateSearchRequest(
		RtsBatchDetailRequest aaRequest,
		RtsBatchDetailResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsBatchHelper laRtsBatchHelper =
			new RtsBatchHelper(null, aaRequest, aaDBA);
		laRtsBatchHelper.getBatchDetailForPlate(aaResponse);
	}

	/**
	 * Process Reprint Request
	 * 
	 * @param aaRequest    RtsBatchDetailRequest
	 * @param aaDBA        DatabaseAccess
	 * @throws RTSException
	 */
	private void processReprintRequest(
		RtsBatchDetailRequest aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsBatchHelper laRtsBatchHelper =
			new RtsBatchHelper(null, aaRequest, aaDBA);
		laRtsBatchHelper.reprintBatchItem();
	}

	/**
	 * Process Void Request
	 * 
	 * @param aaRequest    RtsBatchDetailRequest
	 * @param aaDBA        DatabaseAccess
	 * @throws RTSException
	 */
	private void processVoidRequest(
		RtsBatchDetailRequest aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		RtsBatchHelper laRtsBatchHelper =
			new RtsBatchHelper(null, aaRequest, aaDBA);
		laRtsBatchHelper.voidBatchItem();
	}

}
