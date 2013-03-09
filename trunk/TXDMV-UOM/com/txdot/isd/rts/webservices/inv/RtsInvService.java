package com.txdot.isd.rts.webservices.inv;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.WebServicesInventoryHistory;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.inv.data.RtsInvRequest;
import com.txdot.isd.rts.webservices.inv.data.RtsInvResponse;

/*
 * RtsInvService.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/02/2008	Created class.
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/20/2008	Added logging to write the request out to 
 * 							the app log.
 * 							add logRequest()
 * 							defect 9679 Ver MyPlates_POS
 * Min Wang		06/24/2008	Confirm that requested inv is still on hold
 * 							for the customer.
 * 							add processInvHoldConfirm()
 * 							modify processData()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/25/2008	Release the hold when requested.
 * 							Finished up for Min since she had to go back
 * 							to helping out with 5.7.0.
 * 							add processInvHoldRelease()
 * 							modify processData()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/03/2008	Add the result to the log writing.
 * 							modify logRequest(), processData()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/17/2008	Split out response and request logging.
 * 							add logResponse()
 * 							modify logRequest(), processData()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	01/26/2009	Add a check to ensure the connection is 
 * 							still active before attempting to log
 * 							the response.
 * 							Found that we were not setting successful 
 * 							to false on call errors.
 * 							modify processData()
 * 							defect 9804 Ver Defect_POS_D
 * Ray Rowehl	10/15/2009	Allow Vendor Plates to use pattern plates.
 * 							This means changing IssuePlate to determine 
 * 							the kind of request and make the appropriate
 * 							ISB call.
 * 							modify processCheckPltAvail(),
 * 								processIssuePlate()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	10/21/2009	We forgot to set the status code to 2!
 * 							modify processCheckPltAvail(),
 * 								processIssuePlate()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	10/27/2009	Something changed in how validate PLP works.
 * 							Changing to match ISD result.
 * 							We are not supposed to do a hold when 
 * 							checking availablity.
 * 							modify processCheckPltAvail(),
 * 								processIssuePlate()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	03/04/2010	Change constant for calling issue plate 
 * 							number.
 * 							Modify processing to issue pattern plates
 * 							when a plate number is not provided.
 * 							modify processData(), processIssuePlate()
 * 							defect 10400 Ver 6.4.0
 * Ray Rowehl	04/29/2010	Modify exception message intake to only give
 * 							the caller the first 30 characters.
 * 							modify processData()
 * 							defect 10400 Ver 6.4.0
 * Ray Rowehl	05/05/2010	Log to the new Inv history table instead 
 * 							of writing request data to the logs.
 * 							organize imports.
 * 							delete logRequest(), logResponse()	
 * 							modify processData()
 * 							defect 10400 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the Web Service for Inventory Requests.
 *
 * @version	6.4.0			05/05/2010
 * @author	Min Wang
 * <br>Creation Date:		06/02/2008 03:50:00
 */

public class RtsInvService
{
	/**
	 * Receive transaction requests and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsTransResponse[]
	 */
	public RtsInvResponse[] processData(RtsInvRequest[] aarrRequest)
	{
		RtsInvResponse[] larrResponse =
			new RtsInvResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsInvService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			// defect 10400
			// logRequest(aarrRequest[i]);
			// end defect 10400

			try
			{

				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();

				// log the request
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsSrvcHstryData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);

				// defect 10400
				try
				{
					// do basic data check.
					aarrRequest[i].validateInvRequest();
				}
				catch (RTSException aeRTSExV)
				{
					laDBA.beginTransaction();

					WebServicesInventoryHistory laWebSrvcInvHist =
						new WebServicesInventoryHistory(laDBA);
					laWebSrvcInvHist
						.insWebServicesInventoryHistory(
					laRtsSrvcHstryData.getSavReqId(), aarrRequest[i]);

					laDBA.endTransaction(DatabaseAccess.COMMIT);
					throw aeRTSExV;
				}
				
				// Add WSInvHistory tracking
				laDBA.beginTransaction();

				WebServicesInventoryHistory laWebSrvcInvHist =
					new WebServicesInventoryHistory(laDBA);
				laWebSrvcInvHist
					.insWebServicesInventoryHistory(
				laRtsSrvcHstryData.getSavReqId(), aarrRequest[i]);

				laDBA.endTransaction(DatabaseAccess.COMMIT);
				// end defect 10400
				
				laDBA.beginTransaction();

				//logRequest(aarrRequest[i]);

				switch (aarrRequest[i].getAction())
				{
					case WebServicesActionsConstants
						.RTS_INV_CHECK_PLATE_AVAIL :
						{
							RtsInvResponse laNewResponse =
								new RtsInvResponse();
							processCheckPltAvail(
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
						.RTS_INV_ISSUE_RESERVED :
						{
							// allow reserved to drop through to 
							// personalized!
							
							// defect 10400
						}
					case WebServicesActionsConstants
						.RTS_INV_ISSUE_PLATE_NUMBER :
						{
							// end defect 10400
							RtsInvResponse laNewResponse =
								new RtsInvResponse();
							processIssuePlate(
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
						.RTS_INV_HOLD_CONFIRM :
						{
							RtsInvResponse laNewResponse =
								new RtsInvResponse();
							processInvHoldConfirm(
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
						.RTS_INV_HOLD_RELEASE :
						{
							RtsInvResponse laNewResponse =
								new RtsInvResponse();
							processInvHoldRelease(
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
				// defect 10400
				// System.out.println("RtsInvService had an RTSEx");
				// end defect 10400
				RtsInvResponse laNewResponse = new RtsInvResponse();
				if (aeRTSEx.getCode() != 0)
				{
					laNewResponse.setErrMsgNo(aeRTSEx.getCode());
				}
				else
				{
					laNewResponse.setErrMsgNo(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				}
				if (aeRTSEx.getDetailMsg() != null)
				{
					// defect 10400
					// limit the amount of info being sent back
					if (aeRTSEx.getDetailMsg().length() > 30)
					{
					laNewResponse.setErrMsgDesc(
						aeRTSEx.getDetailMsg().substring(0, 30));
					}
					else
					{
						laNewResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
					}
					// end defect 10400
				}
				laNewResponse.setInvItmNo("");
				larrResponse[i] = laNewResponse;

				// defect 9804
				laRtsSrvcHstryData.setSuccessfulIndi(false);
				laRtsSrvcHstryData.setErrMsgNo(
					laNewResponse.getErrMsgNo());
				// end defect 9804
			}

			// defect 10400
			// logResponse(aarrRequest[i], larrResponse[i]);
			// end defect 10400

			try
			{
				if (laDBA != null)
				{
					// defect 9804
					if (!laDBA.isConnected())
					{
						laDBA.beginTransaction();
					}
					// end 9804

					// write out the update to the tracking row.
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsSrvcHstryData);

					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					"RtsInvService had a problem closing the dba");
			}
		}

		return larrResponse;
	}

// defect 10400
//	/**
//	 * Log the request out to the rtsapp.log.
//	 * 
//	 * @param aaRequest
//	 */
//	private void logRequest(RtsInvRequest aaRequest)
//	{
//		// write the request to the log
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"=======================================");
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Action  " + aaRequest.getAction());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Version " + aaRequest.getVersionNo());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Caller  " + aaRequest.getCaller());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Session " + aaRequest.getSessionId());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Ofc     " + aaRequest.getRequestingOfcIssuanceNo());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Item Cd " + aaRequest.getItmCd());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Item Yr " + aaRequest.getItmYr());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Item    " + aaRequest.getItmNo());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Mfg No  " + aaRequest.getManufacturingPltNo());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Reg Plt " + aaRequest.getRegPltNo());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Reserve " + aaRequest.isFromReserveFlag());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"ISA     " + aaRequest.isIsaFlg());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"PLP     " + aaRequest.isPlpFlag());
//	}
//
//	/**
//	 * Log the request out to the rtsapp.log.
//	 * 
//	 * @param aaRequest
//	 */
//	private void logResponse(
//		RtsInvRequest aaRequest,
//		RtsInvResponse aaResponse)
//	{
//		// write the request to the log
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"=======================================");
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Action  " + aaRequest.getAction());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Version " + aaRequest.getVersionNo());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Caller  " + aaRequest.getCaller());
//		Log.write(
//			Log.SQL_EXCP,
//			this,
//			"Session " + aaRequest.getSessionId());
//
//		Log.write(Log.SQL_EXCP, this, "-----------------------------");
//
//		if (aaResponse != null)
//		{
//			Log.write(
//				Log.SQL_EXCP,
//				this,
//				"MSG NO  " + aaResponse.getErrMsgNo());
//			if (aaResponse.getErrMsgDesc() != null
//				&& aaResponse.getErrMsgDesc().trim().length() > 0)
//			{
//				Log.write(
//					Log.SQL_EXCP,
//					this,
//					"MSG DESC " + aaResponse.getErrMsgDesc());
//			}
//		}
//		else
//		{
//			Log.write(Log.SQL_EXCP, this, "Response is NULL");
//		}
//	}
// end defect 10400

	/**
	 * Process Check Plate Avail request.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processCheckPltAvail(
		RtsInvRequest aaRequest,
		RtsInvResponse aaResponse,
		DatabaseAccess aaDBA)
	{
		InventoryAllocationData laIAD = aaRequest.BuildIAD();

		Vector lvRequest = new Vector(2);
		
		lvRequest.add(aaDBA);
		lvRequest.add(laIAD);

		try
		{
			InventoryServerBusiness laISB =
				new InventoryServerBusiness("VP");
			// defect 10253
			Boolean laDataResponse = new Boolean(false);
			if (laIAD.isUserPltNo())
			{
				// end defect 10253
				laDataResponse =
					(Boolean) laISB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.INV_VI_VALIDATE_PLP_NO_HOLD,
						lvRequest);
				// defect 10253
			}
			else
			{
				Vector lvResponseData =
					(Vector) laISB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant
							.INV_VI_UPDATE_INV_STATUS_CD_RECOVER,
						lvRequest);
				// TODO Need to review Validation.
				// For now, no exception is good.
				laDataResponse = new Boolean(true);
			}
			// end defect 10253
			if (laDataResponse != null
				&& laDataResponse.booleanValue())
			{
				aaResponse.setErrMsgNo(0);
				aaResponse.setInvItmNo(laIAD.getInvItmNo());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				aaResponse.setInvItmNo("");
			}
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
			if (aeRTSEx.getDetailMsg() != null)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
			aaResponse.setInvItmNo("");
		}
	}

	/**
	 * Process Plate Issue request.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processIssuePlate(
		RtsInvRequest aaRequest,
		RtsInvResponse aaResponse,
		DatabaseAccess aaDBA)
	{
		InventoryAllocationData laIAD = aaRequest.BuildIAD();

		// TransTime should not be set yet.
		laIAD.setTransTime(0);

		Vector lvRequest = new Vector(2);
		lvRequest.add(aaDBA);
		// defect 10253
		laIAD.setInvStatusCd(InventoryConstant.HOLD_INV_SYSTEM);
		// end defect 10253
		lvRequest.add(laIAD);

		try
		{
			InventoryServerBusiness laISB =
				new InventoryServerBusiness("VP");
			// defect 10253
			Boolean laDataResponse = new Boolean(false);
			if (laIAD.isUserPltNo())
			{
				// This is personalized plate processing.
				// TODO Need validation rules!
				// For now, no exception is good.
				InventoryAllocationData laIADResponse =
					(InventoryAllocationData) laISB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.INV_VI_VALIDATE_PER_PLT,
						lvRequest);
				// TODO Need validation rules!
				// For now, no exception is good.
				laDataResponse = new Boolean(true);
				// defect 10400
			}
			else if (laIAD.getInvItmNo().length() > 0)
			{
				// Pattern processing where the plate number is provided
				// end defect 10400
				Vector lvResponseData = 
					(Vector) laISB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant
							.INV_VI_UPDATE_INV_STATUS_CD_RECOVER,
						lvRequest);
				// defect 10400
				// TODO Need validation rules!
				// For now, no exception is good.
				laDataResponse = new Boolean(true);
			}
			else
			{
				// Pattern processing where we are doing a get next.
				InventoryAllocationData laResponseData = 
					(InventoryAllocationData) laISB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant
							.INV_GET_NEXT_VI_ITEM_NO,
						lvRequest);
						
				// return the new item number.
				laIAD.setInvItmNo(laResponseData.getInvItmNo());
				// end defect 10400
				// TODO Need validation rules!
				// For now, no exception is good.
				laDataResponse = new Boolean(true);
			}
			// end defect 10253
			
			if (laDataResponse != null
				&& laDataResponse.booleanValue())
			{
				aaResponse.setErrMsgNo(0);
				aaResponse.setInvItmNo(laIAD.getInvItmNo());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				aaResponse.setInvItmNo("");
			}
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
			if (aeRTSEx.getDetailMsg() != null)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
			aaResponse.setInvItmNo("");
		}
	}

	/**
	 * Process Inventory Hold Confirm.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processInvHoldConfirm(
		RtsInvRequest aaRequest,
		RtsInvResponse aaResponse,
		DatabaseAccess aaDBA)
	{
		InventoryAllocationData laIAD = aaRequest.BuildIAD();

		// TransTime should not be set yet.
		laIAD.setTransTime(0);

		Vector lvRequest = new Vector(2);
		lvRequest.add(aaDBA);
		lvRequest.add(laIAD);

		try
		{
			InventoryServerBusiness laISB =
				new InventoryServerBusiness("VP");
			InventoryAllocationData laDataReponse =
				(InventoryAllocationData) laISB.processData(
					GeneralConstant.INVENTORY,
					InventoryConstant.INV_VI_CONFIRM_HOLD,
					lvRequest);

			if (laDataReponse != null)
			{
				aaResponse.setErrMsgNo(0);
				aaResponse.setInvItmNo(laIAD.getInvItmNo());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				aaResponse.setInvItmNo("");
			}
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
			if (aeRTSEx.getDetailMsg() != null)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
			aaResponse.setInvItmNo("");
		}
	}

	/**
	 * Process Inventory Hold Release.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processInvHoldRelease(
		RtsInvRequest aaRequest,
		RtsInvResponse aaResponse,
		DatabaseAccess aaDBA)
	{
		InventoryAllocationData laIAD = aaRequest.BuildIAD();

		// TransTime should not be set yet.
		laIAD.setTransTime(0);

		Vector lvRequest = new Vector(2);
		lvRequest.add(aaDBA);
		lvRequest.add(laIAD);

		try
		{
			InventoryServerBusiness laISB =
				new InventoryServerBusiness("VP");
			InventoryAllocationData laDataReponse =
				(InventoryAllocationData) laISB.processData(
					GeneralConstant.INVENTORY,
					InventoryConstant.INV_VI_RELEASE_HOLD,
					lvRequest);

			if (laDataReponse != null)
			{
				aaResponse.setErrMsgNo(0);
				aaResponse.setInvItmNo(laIAD.getInvItmNo());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				aaResponse.setInvItmNo("");
			}
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
			if (aeRTSEx.getDetailMsg() != null)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
			aaResponse.setInvItmNo("");
		}
	}
}
