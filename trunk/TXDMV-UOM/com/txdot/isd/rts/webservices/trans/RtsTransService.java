package com.txdot.isd.rts.webservices.trans;

import java.util.Vector;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetSpecialPlateTransaction;
import com.txdot.isd.rts.server.db.InternetSpecialPlateTransactionFees;
import com.txdot.isd.rts.server.db.WebServicesTransactionHistory;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.data.WebServicesTransactionHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.trans.data.RtsTransRequest;
import com.txdot.isd.rts.webservices.trans.data.RtsTransRequestV1;
import com.txdot.isd.rts.webservices.trans.data.RtsTransResponse;

/*
 * RtsTransService.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/01/2008	Created class.
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/03/2008	Improved logging to show response.
 * 							modify logRequest(), processData()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/11/2008	Catch NullPointer since we seem to be 
 * 							getting some from inventory.
 * 							modify processData()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/14/2008	Re-order the uow so inv goes before trans.
 * 							modify processOrderRequest()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/16/2008	If the trace number we are inserting for 
 * 							exists, reject it.
 * 							modify processOrderRequest()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/17/2008	Modify logging to check log request before 
 * 							processing.
 * 							add logResponse()
 * 							modify logRequest(), processData()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	01/26/2009	Add check to ensure dba connection is not 
 * 							null before writing the reult to the 
 * 							tracking table.
 * 							modify processData()
 * 							defect 9804 Ver Defect_POS_D
 * Ray Rowehl	12/18/2009	Remove check that prevents inserting the
 * 							transaction is one already exists.
 * 							Set the return code in the response to the 
 * 							return code from the method call.
 * 							Re-organize imports.
 * 							modify processData(),
 * 								processOrderRequest()
 * 							defect 10294 Ver Defect_POS_H
 * Ray Rowehl	01/04/2010	Remove some of the logging since it is 
 * 							not helpful and just takes space.
 * 							Also combine some lines to save space.
 * 							modify logRequest()
 * 							defect 10304 Ver Defect_POS_H
 * Ray Rowehl	01/06/2010	Rename month to meet standard abbreviations.
 * 							modify logRequest()
 * 							defect 10304 Ver Defect_POS_H
 * K Harrell	02/09/2010	Implement PltValidityTerm vs. PltTerm 
 * 							modify logRequest() 
 * 							defect 10366 Ver POS_640
 * Ray Rowehl	03/15/2010	Add processDataV1() to handle new version 
 * 							of requests.
 * 							Modify processData to now format data and 
 * 							call processDataV1().
 * 							add processDataV1()
 * 							modify logRequest(), logResponse(),
 * 								processData(), processOrderRequest()
 * 							defect 10401 Ver 6.4.0 
 * Ray Rowehl	03/22/2010	Make sure the tracking data has been 
 * 							committed before attempting the trans inserts.
 * 							modify processOrderRequest()
 * 							defect 10401 Ver 6.4.0 
 * Ray Rowehl	03/24/2010	Make sure the SpApp data has been 
 * 							committed before attempting the trans create.
 * 							modify processOrderRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	03/26/2010	Work on logging changes.
 * 							Now using WebServiceHistoryData.
 * 							Remove fromReserve references.
 * 							delete logRequest(), logResponse() 
 * 							modify logRequest(), processDataV1()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/12/2010	Add the additional service ids in.
 * 							VPDEL, VPREV, VPUNAC.
 * 							Implement trans hist log insert.
 * 							modify processDataV1()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/23/2010	Add logic to retry trans create if dup
 * 							key error.
 * 							add INT_MAX_WAIT_TIME_FOR_TRANS_CREATE,
 * 								MSG_RETRY, MSG_RTSEX_ON_TRANS_CREATE,
 * 								MSG_SLEEP_INTERRUPTED
 * 							modify processOrderRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/29/2010	Modify exception message intake to only give
 * 							the caller the first 30 characters.
 * 							modify processDataV1()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	06/16/2010	The check for duplicate exception needs to 
 * 							check against detail message as opposed to
 * 							message.
 * 							modify processOrderRequest()
 * 							defect 10513 Ver 6.4.0
 * Ray Rowehl	06/22/2010	Did not consider the dba rollback in the 	
 * 							change.  reflow slightly.
 * 							modify processOrderRequest()
 * 							defect 10513 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the Web Service for Transaction Requests.
 * 
 * <p>Note that requests are really stored on a table pending turn
 * around processing.
 *
 * @version	6.4.0 			06/22/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		06/01/2008 14:06:53
 */
public class RtsTransService
{
	// defect 10401
	//	/**
	//	 * Log the request out to the rtsapp.log.
	//	 * 
	//	 * @param aaRequest
	//	 */
	//	private void logRequest(RtsTransRequestV1 aaRequest)
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
	//		// defect 10304
	//		// Remove this print
	//		// Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Ofc     " + aaRequest.getPltOwnrName1);
	//		// end defect 10304
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Ownr 1  " + aaRequest.getPltOwnrName1());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Ownr 2  " + aaRequest.getPltOwnrName2());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Addr 1   " + aaRequest.getPltOwnrAddr().getStreetLine1());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Addr 2   " + aaRequest.getPltOwnrAddr().getStreetLine2());
	//		// defect 10304
	//		// combine city and state
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"City /St "
	//				+ aaRequest.getPltOwnrAddr().getCity()
	//				+ " / "
	//				+ aaRequest.getPltOwnrAddr().getState());
	//		// Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"State    " + aaRequest.getPltOwnrAddr().getState());
	//
	//		// combine Zip and Zip + 4
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Zip      "
	//				+ aaRequest.getPltOwnrAddr().getZip()
	//				+ " - "
	//				+ aaRequest.getPltOwnrAddr().getZipP4());
	//		//Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Zip+4    " + aaRequest.getPltOwnrAddr().getZipP4());
	//		// end defect 10304
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Phone    " + aaRequest.getPltOwnrPhone());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"EMail    " + aaRequest.getPltOwnrEmailAddr());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Plate    " + aaRequest.getPltNo());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Plate Cd " + aaRequest.getPltCd());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Mfg Plt  " + aaRequest.getMfgPltNo());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Org No   " + aaRequest.getOrgNo());
	//		// defect 10401
	//		//Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Reserve  " + aaRequest.isFromReserve());
	//		// end defect 10401
	//		Log.write(Log.SQL_EXCP, this, "ISA      " + aaRequest.isIsa());
	//		Log.write(Log.SQL_EXCP, this, "PLP      " + aaRequest.isPlp());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Trace No " + aaRequest.getItrntTraceNo());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Pymnt St " + aaRequest.getItrntPymntStatusCd());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Order Id " + aaRequest.getPymntOrderId());
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Price    " + aaRequest.getPymntAmt());
	//		// defect 10304
	//		// combine month and year
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"Mo / Yr  "
	//				+ aaRequest.getPltExpMo()
	//				+ " / "
	//				+ aaRequest.getPltExpYr());
	//		// Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Year     " + aaRequest.getPltExpYr());
	//		// end defect 10304
	//
	//		// defect 10366 
	//		Log.write(Log.SQL_EXCP, this,
	//		//"Term     " + aaRequest.getPltTerm());
	//		"Term     " + aaRequest.getPltValidityTerm());
	//		// end defect 10366 
	//
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"County   " + aaRequest.getResComptCntyNo());
	//		// defect 10304
	//		// Remove this print.  It is just clogging the logs
	//		//Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Strt Tm  " + aaRequest.getInitReqTimeStmp().);
	//		//Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Send Tm  " + aaRequest.getEpaySendTimeStmp());
	//		//Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"Scve Tm  " + aaRequest.getEpayRcveTimeStmp());
	//
	//		// we don't really need this marker
	//		// Log.write(
	//		//	Log.SQL_EXCP,
	//		//	this,
	//		//	"---------------------------------------");
	//		// end defect 10304
	//	}
	//
	//	/**
	//	 * Log the request out to the rtsapp.log.
	//	 * 
	//	 * @param aaRequest
	//	 */
	//	private void logResponse(
	//		RtsTransRequestV1 aaRequest,
	//		RtsTransResponse aaResponse)
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
	//		Log.write(
	//			Log.SQL_EXCP,
	//			this,
	//			"---------------------------------------");
	//
	//		if (aaResponse != null)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"MSG NO  " + aaResponse.getErrMsgNo());
	//		}
	//		else
	//		{
	//			Log.write(Log.SQL_EXCP, this, "Response is NULL");
	//		}
	//
	//	}
	// end defect 10401

	// defect 10401
	private static final int INT_MAX_WAIT_TIME_FOR_TRANS_CREATE = 5;

	private static final String MSG_RETRY =
		"Retrying trans request for VP ";
	private static final String MSG_RTSEX_ON_TRANS_CREATE =
		"RTSException processing trans request for VP";
	private static final String MSG_SLEEP_INTERRUPTED =
		"Sleep interrupted ";
	// end defect 10401
	/**
	 * Receives transaction requests and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsTransResponse[]
	 */
	public RtsTransResponse[] processData(RtsTransRequest[] aarrRequest)
	{
		// defect 10401
		// modify to convert to V1 type requests and then call 
		// processDataV1() to process the requests
		RtsTransResponse[] larrResponse =
			new RtsTransResponse[aarrRequest.length];
		RtsTransRequestV1[] larrRequest =
			new RtsTransRequestV1[aarrRequest.length];

		for (int i = 0; i < aarrRequest.length; i++)
		{
			// copy the request over to V1
			larrRequest[i] = new RtsTransRequestV1(aarrRequest[i]);
		}

		// send the request up to V1 processing
		larrResponse = processDataV1(larrRequest);

		// end defect 10401

		return larrResponse;
	}

	/**
	 * Receives transaction requests (V1) and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsTransResponse[]
	 */
	public RtsTransResponse[] processDataV1(RtsTransRequestV1[] aarrRequest)
	{
		RtsTransResponse[] larrResponse =
			new RtsTransResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsTransService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laWebSrvcHstryData =
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
				laSPT.logInsert(laWebSrvcHstryData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);

				try
				{
					// do at least a basic data check
					aarrRequest[i].validateTransRequest();
				}
				catch (RTSException aeRTSExV)
				{
					laDBA.beginTransaction();

					WebServicesTransactionHistoryData laWebSrvcTransHistData =
						new WebServicesTransactionHistoryData(
							laWebSrvcHstryData.getSavReqId(),
							aarrRequest[i]);
					WebServicesTransactionHistory laWebSrvcTransHist =
						new WebServicesTransactionHistory(laDBA);
					laWebSrvcTransHist
						.insWebServicesTransactionHistory(
						laWebSrvcTransHistData);

					laDBA.endTransaction(DatabaseAccess.COMMIT);
					throw aeRTSExV;
				}

				laDBA.beginTransaction();

				WebServicesTransactionHistoryData laWebSrvcTransHistData =
					new WebServicesTransactionHistoryData(
						laWebSrvcHstryData.getSavReqId(),
						aarrRequest[i]);
				WebServicesTransactionHistory laWebSrvcTransHist =
					new WebServicesTransactionHistory(laDBA);
				laWebSrvcTransHist.insWebServicesTransactionHistory(
					laWebSrvcTransHistData);

				laDBA.endTransaction(DatabaseAccess.COMMIT);
				laDBA.beginTransaction();

				if (liSAVId > 0)
				{
					// if we did not find a valid SAVId
					// do not process the request.

					// process through requests
					switch (aarrRequest[i].getAction())
					{
						case WebServicesActionsConstants
							.RTS_TRANS_VP_DELETE_SP :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_PORT :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_REDO :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_REVISE :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_ORDER_RESERVE :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_RESERVE_PLT_NO :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_RESTYLE_PLT_NO :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_UNACCEPTABLE :
							{
								// Drop into ProcessOrderRequest
							}
						case WebServicesActionsConstants
							.RTS_TRANS_VP_ORDER_NEW :
							{
								RtsTransResponse laResponse =
									new RtsTransResponse();
								// Insert the SpApp_Trans row.
								int liRC =
									processOrderRequest(
										aarrRequest[i],
										laDBA);

								// set the error number to 0 to show success
								larrResponse[i] = laResponse;
								// defect 10294
								larrResponse[i].setErrMsgNo(liRC);
								// end defect 10294

								larrResponse[i].setVersionNo(1);

								laWebSrvcHstryData.setSuccessfulIndi(
									true);
								// defect 10294
								laWebSrvcHstryData.setErrMsgNo(liRC);
								// end defect 10294

								break;
							}
						default :
							{
								larrResponse[i] = null;
								break;
							}
					}
				}
				else
				{
					larrResponse[i] = null;
				}

			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.writeExceptionToLog();
				laWebSrvcHstryData.setSuccessfulIndi(false);

				RtsTransResponse laResponse = new RtsTransResponse();
				if (aeRTSEx.getCode() != 0)
				{
					laResponse.setErrMsgNo(aeRTSEx.getCode());
				}

				if (aeRTSEx.getDetailMsg() != null
					&& aeRTSEx.getDetailMsg().length() > 0)
				{
					// defect 10401
					// limit the amount of info being sent back
					if (aeRTSEx.getDetailMsg().length() > 30)
					{
						laResponse.setErrMsgDesc(
						aeRTSEx.getDetailMsg().substring(0, 30));
					}
					else
					{
						laResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
					}
					// end defect 10401
				}
				laResponse.setVersionNo(1);
				larrResponse[i] = laResponse;

				if (laDBA != null)
				{
					try
					{
						laDBA.endTransaction(DatabaseAccess.ROLLBACK);
						laDBA.beginTransaction();
					}
					catch (RTSException aeRTSEx2)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							" Could not RollBack");
					}
				}
			}
			catch (NullPointerException aeNPEx)
			{
				System.out.println(
					"RtsTransService had a problem "
						+ "processing trans request NullPtr");
				aeNPEx.printStackTrace();
				laWebSrvcHstryData.setSuccessfulIndi(false);

				RtsTransResponse laResponse = new RtsTransResponse();
				larrResponse[i] = laResponse;

				if (laDBA != null)
				{
					try
					{
						laDBA.endTransaction(DatabaseAccess.ROLLBACK);
						laDBA.beginTransaction();
					}
					catch (RTSException aeRTSEx2)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							" Could not RollBack NPR");
					}

					if (aeNPEx.getLocalizedMessage() != null
						&& aeNPEx.getLocalizedMessage().length() > 0)
					{
						System.out.println(
							"Error MSG = "
								+ aeNPEx.getLocalizedMessage());
						laWebSrvcHstryData.setErrMsgNo(-2);
						// defect 10401
						laResponse.setVersionNo(1);
						// end defect 10401
						larrResponse[i].setErrMsgNo(-2);
					}
				}
			}

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
					laSPT.logUpdate(laWebSrvcHstryData);

					laDBA.endTransaction(DatabaseAccess.COMMIT);

					laDBA = null;
				}
			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					"RtsTransService had a problem closing the dba");
			}
		}

		return larrResponse;
	}

	/**
	 * Process the new Order request.
	 * Insert the SpApp_Trans.
	 * 
	 * @param aaRequest
	 * @param laDBA
	 * @throws RTSException
	 */
	private int processOrderRequest(
		RtsTransRequestV1 aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		// defect 10401
		// This was already done before the call to come in here.
		// commit tracking data before attempting trans data.
		//aaDBA.endTransaction(DatabaseAccess.COMMIT);
		//aaDBA.beginTransaction();
		// end defect 10401

		// build spapp trans
		SpecialPlateItrntTransData laSPITD = aaRequest.buildSPITD();

		// Setup spapp sql
		InternetSpecialPlateTransaction laISPT =
			new InternetSpecialPlateTransaction(aaDBA);

		// insert the trans data
		laISPT.insSPItrntTrans(laSPITD);

		// handle the fees data
		Vector lvFeesData = aaRequest.buildFees();
		if (lvFeesData != null)
		{
			laSPITD.setFeesData(lvFeesData);
			InternetSpecialPlateTransactionFees laISPTF =
				new InternetSpecialPlateTransactionFees(aaDBA);

			int liMaxReqPltNo =
				laISPT.qryMaxReqPltNoReqId(aaRequest.getPltNo());

			laISPTF.insSPItrntTransFees(liMaxReqPltNo, laSPITD);
		}
		// defect 10401
		// commit SpApp Trans data before attempting to create trans.
		aaDBA.endTransaction(DatabaseAccess.COMMIT);
		aaDBA.beginTransaction();

		// defect 10513
		// this has to be moved down into the loop.
		//if (aaRequest.hasInventory())
		//{
		//	// only process inventory if it is expected.
		//
		//	// move to after secondary commit
		//	// update the inventory with a transtime.
		//	InventoryAllocationData laIAD = aaRequest.buildIAD();
		//	InventoryServerBusiness laISB =
		//		new InventoryServerBusiness("VP");
		//	Vector lvInvProcess = new Vector(2);
		//	lvInvProcess.add(aaDBA);
		//	lvInvProcess.add(laIAD);
		//	laISB.processData(
		//		GeneralConstant.INVENTORY,
		//		InventoryConstant.INV_VI_UPDATE_TRANSTIME,
		//		lvInvProcess);
		//}
		// end defect 10401
		// end defect 10513

		// defect 10401
		// Retry on dup insert into trans
		// we do not want to retry more than 5 times.
		// Session will time out if we go too long.
		for (int i = 0; i < INT_MAX_WAIT_TIME_FOR_TRANS_CREATE; i++)
		{
			try
			{
				// defect 10513
				if (aaRequest.hasInventory())
				{
					// only process inventory if it is expected.

					// move to after secondary commit
					// update the inventory with a transtime.
					InventoryAllocationData laIAD = aaRequest.buildIAD();
					InventoryServerBusiness laISB =
						new InventoryServerBusiness("VP");
					Vector lvInvProcess = new Vector(2);
					lvInvProcess.add(aaDBA);
					lvInvProcess.add(laIAD);
					laISB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.INV_VI_UPDATE_TRANSTIME,
						lvInvProcess);
				}
				// end defect 10513
				
				// This is the trans create section.
				CommonServerBusiness laCommServerBuss =
					new CommonServerBusiness();
				Vector lvCaller = new Vector(2);
				lvCaller.add(aaDBA);
				lvCaller.add(laSPITD);
				laCommServerBuss.processData(
					GeneralConstant.COMMON,
					CommonConstant.PROC_VENDOR_PLATES,
					lvCaller);
				break;
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					MSG_RTSEX_ON_TRANS_CREATE);

				// Check to see if this is a duplicate key.
				// defect 10513
				// Shoud be using detail msg.
				if (aeRTSEx.getDetailMsg() != null
					&& aeRTSEx.getDetailMsg().indexOf(
						CommonConstant.DUPLICATE_KEY_EXCEPTION)
						> -1)
				{
					// end defect 10513
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_RETRY
							+ aaRequest.getPltNo()
							+ CommonConstant.STR_SPACE_ONE
							+ i);
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException aeIEx)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							MSG_SLEEP_INTERRUPTED + i);
					}
					// defect 10513
					aaDBA.beginTransaction();
					// end defect 10513
					continue;
				}
				else
				{
					// Some other exception.  Just throw it.
					throw aeRTSEx;
				}
			}
		}
		// end defect 10401

		// if we did not throw an exception, return 0
		return 0;
	}
}
