package com.txdot.isd.rts.client.misc.business;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientBusiness;

import com.txdot.isd.rts.services.cache.MiscellaneousCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.miscellaneous.GenVoidReport;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * MiscClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ---------------------------------------------------------------------
 * BTul/NTing	04/29/2002	Added code to CompletVoid for Region Voids 
 *							defect 3583
 * Min Wang		07/01/2002 	only reprint receipt when receipt file
 * 							exists.
 *							defect 4545 
 * Min Wang		10/07/2002	Modified completeVoidTransaction().Change 
 *							so that Void Indi update is written to 
 *							cache instead of just posting to server.
 *							Use Transaction.transactioncontrol to 
 *							determine if MVFunc Data is needed in 
 *							completeVoidTransaction.
 *							defect 4746
 * Ray Rowehl	11/11/2002	Rearrange void so updates occur as one unit 
 *							of work. Also all work is cached.
 *							defect 4745
 * Min Wang		03/20/2003  Modified printVoidTransactionReport().
 *							defect 5781.
 * K Harrell	07/18/2003  Void fails to build Transaction Header when 
 *							attempt to retry after error.
 *							defect 6293
 *							modify completeVoidTransaction()
 * Min Wang		06/30/2003	Clean up handling of Void report
 *							modify printVoidTransactionReport()
 *							defect 6252
 * K Harrell	10/27/2003	CacheTransAMDate,CacheTransTime not set for
 *							the "VOID" component of VOID Transaction 
 *							set. TransactionCacheData.getDateTime() 
 *							shows date/time of original transaction in 
 *							ShowCache.  
 *							modify completeVoidTransaction()
 *						    defect 6038 Ver 5.1.5 Fix 1
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							Changed pritn command to pass the transCd
 *							for printVoidTransactionReport(Vector).  Let
 *							PrintDocManager handle the printing of dups.
 *							modify printReceipts(Object),
 *							printVoidTransactionReport(Vector)
 *							defect 6848, 6898 Ver 5.1.6
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add ciSelectedCSN
 *							add getLastCSN()
 *							add getReceipts()
 *							add overrideStatus()
 *							modify processData()
 * 							Ver 5.2.0
 * K Harrell	04/19/2004	Create transaction for Reprint Sticker
 *							modify printReceipts()
 *							defect 7018 Ver 5.2.0
 * K Harrell	04/21/2004	Do not update database on reprint receipt
 *							if exception on reprint receipt
 *							modify printReceipts()
 *							defect 5122 Ver 5.2.0
 * K Harrell	04/27/2004	Mark transactions as voided if appropriate
 *							modify getReceipts(Object)
 *							defect 7019 Ver 5.2.0
 * K Harrell	05/05/2004	Open Cashdrawer when void with payment
 *							Originally coded 2/13/2003 & lost
 *							modify completeVoidTransaction()
 *							defect 5480 Ver 5.2.0
 * K Harrell	05/07/2004	rework 7019
 *							add updVoidTransLog()
 *							modify completeVoidTransaction()
 *							defect 7019 Ver 5.2.0
 * Jeff S.		05/14/2004	Make print return exception so that if there 
 *							was an exception during printing the sticker 
 *							the prntinvqty filed would not get updated.
 *							modify printReceipts(Object)
 *							defect 7067 Ver. 5.2.0
 * K Harrell	05/17/2004	Disable cash receipt on Reprint Receipt if
 *							any transactions is voided.
 *							modify getReceipts(Object)
 *							defect 7019 (cont'd)  Ver 5.2.0
 * Jeff S.		05/26/2004	Changed call to getVoidPageProps() to
 *							getDefaultPrinterProps() b/c they where
 *							doing the same thing and it was easier to
 *							maintain if there was only one method to get
 *							the page props from.
 *							modify printVoidTransactionReport(Vector)
 *							defect 7078 Ver 5.2.0
 * K Harrell	06/30/2004	Do not create a RPRSTK transaction when
 * 							printing a cash register receipt.
 *							modify printReceipts()
 *							defect 7261 Ver 5.2.0
 * K Harrell	07/01/2004	Throw an exception if an error encountered 
 *							modify printReceipts()
 *							defect 7217 Ver 5.2.0
 * K Harrell	07/02/2004	Assign hashmap of reprint sticker data
 *							to ReprintStickerTransData
 *							modify printReceipts()
 *							defect 7284 Ver 5.2.0
 * K Harrell	08/20/2004 	Call postTrans() to process 
 * 							TransactionCacheData
 *							modify printReceipts()
 *							defect 7460 Ver 5.2.1
 * K Harrell	10/12/2004	Add one second wait between reprint receipts
 *							to prevent duplicate transactions.
 *							modify printReceipts()
 *							defect 7460 Ver 5.2.1
 * K Harrell	10/12/2004	remove call to Transaction
 * 							.isFirstTransactionOfDay()
 *							defect 7581 Ver 5.2.1
 * K Harrell	10/14/2004	verify that payment amount !=0 when 
 *							determining whether to open cash drawer.
 *							modify completeVoidTransaction()
 *							defect 5480 (enhanced) Ver 5.2.1
 * Ray Rowehl	11/24/2004	Turn off cumulative trans when doing a Void.
 *							Also looked at source formatting within the
 *							method
 *							modify completeVoidTransaction()
 *							defect 7531 Ver 5.2.2
 * K Harrell	01/14/2005	Remove unnecessary imports.
 *							defect 7760 Ver 5.2.3
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							Note that organizing imports took away
 * 							the work of defect 7760.
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * J Zwiener	03/01/2005	Java 1.4
 * 							defect 7892
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7892 Ver 5.2.3
 * K Harrell	08/23/2005	Include HCKITM in TransCds which generate
 * 							VOIDNC.
 * 							modify completeVoidTransaction()  
 * 							defect 8345 Ver 5.2.3
 * K Harrell	11/13/2005	Use TransCdsData.getCumulativeTransCd() 
 * 							to setCumulativeTransCd for VOID/VOIDNC  
 * 							modify completeVoidTransaction()  
 * 							defect 7759 Ver 5.2.3
 * K Harrell	05/29/2008	Show Supervisor Override if Salvage type 
 * 							event.
 * 							modify getReceipts()
 * 							defect 9636 Ver 3 Amigos PH B
 * K Harrell	09/16/2008	Show Supervisor Override Reason on 
 * 							Reprint Receipt 
 * 							modify getReceipts(Object)
 * 							defect 7283 Ver Defect_POS_B   
 * K Harrell	08/17/2009	Implement ReportProperties.initClientInfo(), 
 * 							UtilityMethods.addPCLandSaveReport(),
 * 							delete getReceipts() 
 * 							modify printVoidTransactionReport(),
 * 							 completeVoidTransaction(), overrideStatus()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	09/11/2009	Restored adding of PCL to printVoidTransaction()
 * 							Does not follow 'standard' 
 * 							modify printVoidTransactionReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	06/09/2010	add Void Processing for Permit Transactions
 * 							modify completeVoidTransaction()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/30/2010	add support for VoidTransIndi in RTS_PRMT_TRANS
 *							modify completeVoidTransaction()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/18/2010	Include Duplicate Permit PRMT_TRANS in void
 * 							modify completeVoidTransaction()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	12/27/2010	Permit File Name added to Receipt Log
 * 							modify getReceipts() 
 * 							defect 10700 Ver 6.7.0
 * K Harrell	03/03/2011	Reassign TransTime to TransactionHeaderData
 * 							modify printReceipts() 
 * 							defect 10756 Ver 6.7.0 
 * K Harrell	04/08/2011	use ReceiptLogData.printFileExists() to 
 * 							determine if should print 
 * 							modify printReceipts()
 * 							defect 10796 Ver 6.7.1 
 * K Harrell	06/19/2011	add getPrmt()
 * 							modify getReceipts()  
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	07/08/2011	Show "Not Available" for MODPT if original 
 * 							permit application on same workstation was 
 * 							voided.  
 * 							Previously displaying 'VOIDED' 
 * 							modify getReceipts() 
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	07/13/2011	Do not allow reprint in Server Down for 
 * 							Timed Permit Application or Modify Permit
 * 							modify getReceipts() 
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	11/18/2011	Include code for reprinting for VTR 275 
 * 							modify printReceipts() 
 * 							defect 11052 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * Miscellaneous client business layer
 *
 * @version	 6.9.0 	11/18/2011
 * @author	Bobby Tulsiani
 * @since 			09/05/2001 13:30:59
 */
public class MiscClientBusiness
{
	private int ciSelectedCSN;

	/**
	 * MiscClientBusiness constructor comment.
	 */
	public MiscClientBusiness()
	{
		super();
	}

	/**
	 * Process Void Request.
	 *
	 * @param aiModule int 
	 * @param aiFunctionId int 
	 * @param aaData Object 
	 * @throws RTSException
	 */
	private Object completeVoidTransaction(Object aaData)
		throws RTSException
	{
		//Set vector to take transaction data, and Transaction
		// HeaderData
		Vector lvInput = (Vector) aaData;
		Vector lvTransactions = (Vector) lvInput.get(0);
		Vector lvData = (Vector) lvInput.get(1);
		Vector lvVoidUIData = new Vector();

		// defect 7759
		// Use to Save Void TransCd; Initialize to VOIDNC;  
		String lsVoidTransCd = TransCdConstant.VOIDNC;
		// end defect 7759  

		// Overall TransCache Vector to post
		Vector lvTransCacheObjects = new Vector();
		//Initalize objects    
		TransactionHeaderData laTransactionHeaderData =
			new TransactionHeaderData();
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		MFVehicleData laMFVehicleData = new MFVehicleData();
		CompleteTransactionData laCTData2 = null;

		// defect 5480
		boolean lbPaymentExists = false;
		// end defect 5480

		// 
		// defect 6293  Use 3 separate try blocks; Issue
		//				transaction.reset() on
		//				error from 2nd try block.
		//                
		try
		{
			//Test the transaction against MF.
			// if not posted, we can void..
			for (int m = 0; m < lvTransactions.size(); m++)
			{
				VoidTransactionData laTransaction =
					(VoidTransactionData) lvTransactions.get(m);
				if (laTransaction.getTransPostedMfIndi() == 1)
				{
					GeneralSearchData laGSData =
						new GeneralSearchData();
					laGSData.setKey1(laTransaction.getTransactionId());
					processData(
						GeneralConstant.MISC,
						MiscellaneousConstant.CHECK_MF_TRANS,
						laGSData);
				} // TransPostedMfIndi() == 1
			} // for (int m = 0; m < lvTransactions.size();..
		} // end try
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType() == RTSException.MF_DOWN)
			{
				// defect 8628 
				//throw new RTSException(331);
				throw new RTSException(
					ErrorsConstant.ERR_NUM_CANNOT_RETRIEVE_CANNOT_VOID);
				// end defect 8628  
			}
			else
			{
				throw aeRTSEx;
			}
		}

		try
		{
			//For each transaction, begin the void process
			for (int i = 0; i < lvTransactions.size(); i++)
			{
				CompleteTransactionData laCTData =
					new CompleteTransactionData();
				VoidTransactionData laVoidTransactionData =
					(VoidTransactionData) lvTransactions.get(i);
				VoidUIData laVoidUIData = new VoidUIData();
				laCTData.setVoidOfcIssuanceNo(
					laVoidTransactionData.getOfcIssuanceNo());
				laCTData.setVoidSubstaId(
					laVoidTransactionData.getSubstaId());
				laCTData.setVoidTransAMDate(
					laVoidTransactionData.getTransAMDate());
				laCTData.setVoidTransWsId(
					laVoidTransactionData.getTransWsId());
				laCTData.setVoidCustSeqNo(
					laVoidTransactionData.getCustSeqNo());
				laCTData.setVoidTransTime(
					laVoidTransactionData.getTransTime());
				laCTData.setVoidTransCd(
					laVoidTransactionData.getTransCd());

				//Qry transaction Header to get Trans Name	
				laTransactionHeaderData =
					(TransactionHeaderData) processData(GeneralConstant
						.MISC,
						MiscellaneousConstant.QRY_VOID_TRANS_HDR,
						laVoidTransactionData);

				if (laTransactionHeaderData != null)
				{
					laCTData.setVoidTransName(
						laTransactionHeaderData.getTransName());
				}

				// defect 4746
				// Get MVF data for all eligible transactions.   
				TransactionControl laTC =
					(
						TransactionControl) Transaction
							.shTransactionControl
							.get(
						laVoidTransactionData.getTransCd());

				if (laTC != null)
				{
					if (laTC.isBuildMVFuncTrans())
					{
						laMFVehicleData =
							(MFVehicleData) processData(GeneralConstant
								.MISC,
								MiscellaneousConstant.GET_VOID_MVF_DATA,
								laVoidTransactionData);
						laCTData.setVehicleInfo(laMFVehicleData);
					}
					// defect 10491 
					// Permit Application && Duplicate Permit  
					else if (
						UtilityMethods.printsPermit(
							laVoidTransactionData.getTransCd()))
					{
						PermitData laPrmtData =
							(PermitData) processData(GeneralConstant
								.MISC,
								MiscellaneousConstant
									.GET_VOID_PRMT_DATA,
								laVoidTransactionData);
						laCTData.setTimedPermitData(laPrmtData);
					}
					// end defect 10491 
				}
				// end defect 4746

				laCTData.setVoidTransactionHeaderData(
					laTransactionHeaderData);

				// If HOTCK, HOTDED, REFUND or HCKITM, create VOIDNC 
				//  vs. VOID.    Do not create INVVD. 
				if (laVoidTransactionData
					.getTransCd()
					.equals(TransCdConstant.HOTCK)
					|| laVoidTransactionData.getTransCd().equals(
						TransCdConstant.HOTDED)
					|| laVoidTransactionData.getTransCd().equals(
						TransCdConstant.REFUND)
					|| laVoidTransactionData.getTransCd().equals(
						TransCdConstant.HCKITM))
				{
					laCTData.setTransCode(TransCdConstant.VOIDNC);

					// Code to wait for 1 second between transactions.
					// Without this pause, there's a duplicate key error
					Thread.sleep(1000);
					Vector lvVoidNC =
						(Vector) laCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.ADD_TRANS_FOR_VOID,
							laCTData);
					// put the transcache objects in the Overall 
					// TransCache Vector
					lvTransCacheObjects.addAll(
						(Vector) lvVoidNC.elementAt(0));
					laVoidUIData.setTransId(
						laVoidTransactionData.getTransactionId());
					laVoidUIData.setTargetEventTransId(
						(String) lvVoidNC.elementAt(1));
					laVoidUIData.setTargetInventoryTransId("");
					laVoidUIData.setVoidDescription(
						laVoidTransactionData.getTransCdDesc());
				}
				//Else perform normal void
				else
				{
					if (laVoidTransactionData
						.getTransCd()
						.equals(TransCdConstant.IRENEW))
					{
						laCTData.setTransCode(TransCdConstant.VOIDNC);
					}
					else
					{
						laCTData.setTransCode(TransCdConstant.VOID);
						lsVoidTransCd = TransCdConstant.VOID;
					}
					// First call to VOID
					// Code to wait for 1 second between transactions.
					// Without this pause, there is a duplicate key error.
					Thread.sleep(1000);
					Vector lvVoid =
						(Vector) laCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.ADD_TRANS_FOR_VOID,
							laCTData);
					// put the transcache objects in the Overall
					// TransCache Vector
					lvTransCacheObjects.addAll(
						(Vector) lvVoid.elementAt(0));
					laVoidUIData.setTransId(
						laVoidTransactionData.getTransactionId());
					laVoidUIData.setTargetEventTransId(
						(String) lvVoid.elementAt(1));
					laVoidUIData.setVoidDescription(
						laVoidTransactionData.getTransCdDesc());

					// Second call to INVVD, if there is inventorydata  	
					if (laVoidTransactionData.getInventoryIndi() == 1)
					{
						// Code to wait for 1 second between
						// transactions.
						// Without this pause, there is a duplicate key
						// error.
						Thread.sleep(1000);
						laCTData.setTransCode(TransCdConstant.INVVD);
						Vector lvINVVD =
							(
								Vector) laCommonClientBusiness
									.processData(
								GeneralConstant.COMMON,
								CommonConstant.ADD_TRANS_FOR_VOID,
								laCTData);
						// put the transcache objects in the Overall
						// TransCache Vector
						lvTransCacheObjects.addAll(
							(Vector) lvINVVD.elementAt(0));
						laVoidUIData.setTransId(
							laVoidTransactionData.getTransactionId());
						laVoidUIData.setTargetInventoryTransId(
							(String) lvINVVD.elementAt(1));
					}
				}
				lvVoidUIData.add(laVoidUIData);
			} //for (int i = 0; i < lvTransactions.size(); i++)

			//Call void Transaction	
			laCTData2 =
				(CompleteTransactionData) processData(GeneralConstant
					.MISC,
					MiscellaneousConstant.VOID_TRANSACTION,
					lvTransactions);

			//If voiding all transactions, set indicator to true
			if (lvTransactions.size() == lvData.size())
			{
				laCTData2.setVoidAllIndi(1);
			}
			else
			{
				laCTData2.setVoidAllIndi(0);
			}
			//Call End Transaction process

			if (Transaction.getTransactionHeaderData() == null)
			{
				Transaction laTransaction =
					new Transaction(laCTData2.getTransCode());
				Transaction.setTransactionHeaderData(
					laTransaction.addTransHeader(laCTData2, true));
			}

			Vector lvPayment =
				(Vector) laCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.END_TRANS_FOR_VOID,
					laCTData2);

			// put the transcache objects in the Overall TransCache
			// Vector
			lvTransCacheObjects.addAll((Vector) lvPayment);
			// defect 5480
			// Check if Payment Record Written  
			for (int k = 0; k < lvPayment.size(); k++)
			{
				TransactionCacheData laTransactionCacheData =
					(TransactionCacheData) lvPayment.elementAt(k);
				if (laTransactionCacheData.getObj()
					instanceof com
						.txdot
						.isd
						.rts
						.services
						.data
						.TransactionPaymentData)
				{
					TransactionPaymentData laTransPaymentData =
						(TransactionPaymentData) laTransactionCacheData
							.getObj();
					if (laTransPaymentData
						.getPymntTypeAmt()
						.subtract(laTransPaymentData.getChngDue())
						.compareTo(new Dollar(0.00))
						!= 0)
					{
						lbPaymentExists = true;
						break;
					}
				}
			}
			// end defect 5480

			// defect 4746
			// process to set up void indi
			//
			// new void indi process.  this now uses cache to process
			Vector lvTransObjectsToVoid =
				(Vector) Comm.sendToServer(
					GeneralConstant.MISC,
					MiscellaneousConstant.GET_TRANS_FOR_VOID_INDI,
					lvTransactions);
			// defect 6038
			// Call setCacheTransAMDate,setCacheTransTime for each
			// transaction component
			RTSDate laCurrentDateTime = new RTSDate();
			// end defect 6038

			// put objects in vector
			for (int j = 0; j < lvTransObjectsToVoid.size(); j++)
			{
				// get out the object to update
				Object laTransObj = lvTransObjectsToVoid.elementAt(j);
				// determine which type of object this is
				// handle TransactionData
				if (laTransObj instanceof TransactionData)
				{
					((TransactionData) laTransObj).setVoidedTransIndi(
						1);
					// defect 6038 
					((TransactionData) laTransObj).setCacheTransAMDate(
						laCurrentDateTime.getAMDate());
					((TransactionData) laTransObj).setCacheTransTime(
						laCurrentDateTime.get24HrTime());
					// end defect 6038 
				}
				// handle MotorVehicleFunctionTransactionData
				else if (
					laTransObj
						instanceof MotorVehicleFunctionTransactionData)
				{
					(
						(
							MotorVehicleFunctionTransactionData) laTransObj)
								.setVoidedTransIndi(
						1);
					// defect 6038 
					(
						(
							MotorVehicleFunctionTransactionData) laTransObj)
								.setCacheTransAMDate(
						laCurrentDateTime.getAMDate());
					(
						(
							MotorVehicleFunctionTransactionData) laTransObj)
								.setCacheTransTime(
						laCurrentDateTime.get24HrTime());
					// end defect 6038 
				}
				// handle InventoryFunctionTransactionData
				else if (
					laTransObj
						instanceof InventoryFunctionTransactionData)
				{
					(
						(
							InventoryFunctionTransactionData) laTransObj)
								.setVoidedTransIndi(
						1);
					// defect 6038 
					(
						(
							InventoryFunctionTransactionData) laTransObj)
								.setCacheTransAMDate(
						laCurrentDateTime.getAMDate());
					(
						(
							InventoryFunctionTransactionData) laTransObj)
								.setCacheTransTime(
						laCurrentDateTime.get24HrTime());
					// end defect 6038 
				}
				// handle FundFunctionTransactionData
				else if (
					laTransObj instanceof FundFunctionTransactionData)
				{
					(
						(
							FundFunctionTransactionData) laTransObj)
								.setVoidedTransIndi(
						1);
					// defect 6038 
					(
						(
							FundFunctionTransactionData) laTransObj)
								.setCacheTransAMDate(
						laCurrentDateTime.getAMDate());
					(
						(
							FundFunctionTransactionData) laTransObj)
								.setCacheTransTime(
						laCurrentDateTime.get24HrTime());
					// end defect 6038 
				}
				// defect 10491 
				// handle PermitTransactionData
				else if (laTransObj instanceof PermitTransactionData)
				{
					(
						(
							PermitTransactionData) laTransObj)
								.setVoidedTransIndi(
						1);
					(
						(
							PermitTransactionData) laTransObj)
								.setCacheTransAMDate(
						laCurrentDateTime.getAMDate());
					(
						(
							PermitTransactionData) laTransObj)
								.setCacheTransTime(
						laCurrentDateTime.get24HrTime());
				}
				// end defect 10491

				//Add object to cache Vector
				TransactionCacheData laTransactionCacheData =
					new TransactionCacheData();
				laTransactionCacheData.setProcName(
					TransactionCacheData.VOID);
				laTransactionCacheData.setObj(laTransObj);
				lvTransCacheObjects.addElement(laTransactionCacheData);
			}

		} // end try 
		catch (RTSException aeRTSEx)
		{
			com.txdot.isd.rts.services.common.Transaction.reset();
			throw aeRTSEx;
		}
		catch (InterruptedException aeIEx)
		{
			com.txdot.isd.rts.services.common.Transaction.reset();
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIEx);
			throw leRTSEx;
		}

		try
		{
			// write vector to cache
			if (lvTransCacheObjects.size() > 0)
			{
				Transaction laTransaction = new Transaction();
				laTransaction.processCacheVector(lvTransCacheObjects);

				// defect 7759 
				// Use TransCdsData.getCumulativeTransCd() for TransCd 
				// defect 7531
				// turn the Cumulative Trans Indi off so
				// follow on transactions can not do same vehicle.
				TransactionCodesData laTransCdsData =
					TransactionCodesCache.getTransCd(lsVoidTransCd);
				Transaction.setCumulativeTransIndi(
					laTransCdsData.getCumulativeTransCd());
				//Transaction.setCumulativeTransIndi(0);
				// end defect 7531
				// end defect 7759 

				// defect 7019
				// Update Void Transaction Log
				updVoidTransLog(lvTransactions);
				// end 7019

				// defect 5480
				// Open Cashdrawer if Payment
				if (lbPaymentExists)
				{
					try
					{
						CashDrawer.open();
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError((javax.swing.JFrame) null);
					}
				} // end defect 5480 
			}
			//end defect 4746
		} // end try
		catch (RTSException aeRTSEx)
		{
			com.txdot.isd.rts.services.common.Transaction.reset();
			throw aeRTSEx;
		}

		setLastCSN();
		//PRINT VOID TRANSACTION REPORT
		printVoidTransactionReport(lvVoidUIData);
		// end defect 6293

		return lvVoidUIData;
	}

	/**
	 * Get Last Customer Sequence No
	 * 
	 * @return Object
	 * @throws RTSException  
	 */
	private Object getLastCSN() throws RTSException
	{
		try
		{
			// 
			// PCR 34 
			// Determine status of d:\rts\rcpt\rcptrec.log
			//
			RTSDate laToday = RTSDate.getCurrentDate();
			String lsDate = "";
			Vector lvVector = new Vector();
			// Read CustSeq.Dat
			File laCustSeqDatFile = new File("custseq.dat");
			if (!laCustSeqDatFile.exists())
			{
				lvVector.add("****");
			}
			else
			{
				FileInputStream lpfsFISCustSeqDat =
					new FileInputStream(laCustSeqDatFile);
				BufferedReader laBuffRdr =
					new BufferedReader(
						new InputStreamReader(lpfsFISCustSeqDat));
				String lsLine = laBuffRdr.readLine();
				lsDate = lsLine.substring(lsLine.indexOf(",") + 1);
				if (!laToday.toString().equals(lsDate))
				{
					lvVector.add("****");
				}
				else
				{
					File laCumRcptLogFile =
						new File(
							SystemProperty.getReceiptsDirectory()
								+ SystemProperty.getCumRcptLogFileName());

					if (!laCumRcptLogFile.exists())
					{
						laCumRcptLogFile.createNewFile();
						lvVector.add("****");
					}
					else
					{
						FileInputStream lpfsFISCumRcptLog =
							new FileInputStream(laCumRcptLogFile);
						if (lpfsFISCumRcptLog.available() == 0)
						{
							lvVector.add("****");
						}
						else
						{
							ObjectInputStream laObjectInputStream =
								new ObjectInputStream(lpfsFISCumRcptLog);
							lvVector =
								(Vector) laObjectInputStream
									.readObject();
							laObjectInputStream.close();
							lpfsFISCumRcptLog.close();
						}
					}
				}
			}
			return lvVector;
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				aeCNFEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				aeIOEx);
		}
	}

	/**
	 * Method retrieves the receipt by examining the lastcsn.dat file,
	 * the rcptlog directory
	 *
	 * @param  aaData Object 
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getReceipts(Object aaData) throws RTSException
	{
		try
		{
			Vector lvReceipts = new Vector();
			String lsFileNum = "csn" + (String) aaData;

			FileInputStream lpfsRcptLog =
				new FileInputStream(
					SystemProperty.getReceiptsDirectory()
						+ lsFileNum.trim()
						+ "\\Rcptlog");
			BufferedReader laBuffRdrRcptLog =
				new BufferedReader(new InputStreamReader(lpfsRcptLog));

			String lsLine = "";
			String lsTransCode = "";

			// defect 7019
			// set count for voided transactions
			int liVoidedCount = 0;
			// read file of voided transactions

			File laVoidTransLogFile =
				new File(
					SystemProperty.getReceiptsDirectory()
						+ SystemProperty.getVoidTransLogFileName());
			Vector lvVoidTrans = new Vector();
			if (laVoidTransLogFile.exists())
			{
				FileInputStream lpfsFISVoidTransLog =
					new FileInputStream(laVoidTransLogFile);
				ObjectInputStream laOISVoidTransLog =
					new ObjectInputStream(lpfsFISVoidTransLog);
				lvVoidTrans = (Vector) laOISVoidTransLog.readObject();
				laOISVoidTransLog.close();
			}
			// end defect 7019

			//Read each line in the Rcptlog.  The index of each comma
			//will be used to input the values
			while ((lsLine = laBuffRdrRcptLog.readLine()) != null)
			{
				ReceiptLogData laReceiptLogData = new ReceiptLogData();
				int liCommaIndex1 = lsLine.indexOf(",");

				// Receipt File Name 
				String lsDir =
					SystemProperty.getReceiptsDirectory() + lsFileNum;
				laReceiptLogData.setRcptFileName(
					lsDir.trim()
						+ "\\"
						+ lsLine.substring(0, liCommaIndex1));
				int liCommaIndex2 =
					lsLine.indexOf(",", liCommaIndex1 + 1);

				// BarCode File Name 
				String lsBarCode =
					lsLine.substring(liCommaIndex1 + 1, liCommaIndex2);
				if (!lsBarCode.equals("") && !lsBarCode.equals(" "))
					laReceiptLogData.setBarCodeFile(
						lsDir.trim() + "\\" + lsBarCode);

				// defect 10700 
				// Permit File Name 
				int liCommaIndex2b =
					lsLine.indexOf(",", liCommaIndex2 + 1);
				String lsPermit =
					lsLine.substring(liCommaIndex2 + 1, liCommaIndex2b);
				if (!UtilityMethods.isEmpty(lsPermit))
					laReceiptLogData.setPermitFile(
						lsDir.trim() + "\\" + lsPermit);

				int liCommaIndex3 =
					lsLine.indexOf(",", liCommaIndex2b + 1);
				// defect 10844 
				String lsTransId =
					lsLine
						.substring(liCommaIndex2b + 1, liCommaIndex3)
						.trim();
				laReceiptLogData.setTransId(lsTransId);
				int liCommaIndex4 =
					lsLine.indexOf(",", liCommaIndex3 + 1);
				String lsTransType =
					lsLine.substring(liCommaIndex3 + 1, liCommaIndex4);
				int liCommaIndex5 =
					lsLine.indexOf(",", liCommaIndex4 + 1);
				laReceiptLogData.setVIN(
					lsLine.substring(liCommaIndex4 + 1, liCommaIndex5));

				// defect 10844 
				int liCommaIndex6 =
					lsLine.indexOf(",", liCommaIndex5 + 1);
				laReceiptLogData.setTransCd(
					lsLine.substring(liCommaIndex5 + 1, liCommaIndex6));
				lsTransCode = laReceiptLogData.getTransCd();

				int liLastIndex = lsLine.lastIndexOf(",");
				// defect 10844
				String lsPrmtIssuanceId =
					lsLine
						.substring(liLastIndex + 1, lsLine.length())
						.trim();
				laReceiptLogData.setPrmtIssuanceId(lsPrmtIssuanceId);
				// end defect 10844

				// defect 7019
				// Mark transaction as voided if appropriate
				boolean lbVoided = false;
				boolean lbReprintAvail = true;

				for (int i = 0; i < lvVoidTrans.size(); i++)
				{
					// defect 10844 
					String lsVoidTransId = (String) lvVoidTrans.get(i);

					if (lsVoidTransId.equals(lsTransId))
					{
						lbVoided = true;
						break;
					}
					// If voided permit, i.e. in MODPT, then cannot reprint
					else if (lsVoidTransId.equals(lsPrmtIssuanceId))
					{
						lbReprintAvail = false;
						break;
					}
				}

				// defect 7019 
				if (lbVoided)
				{
					lsTransType = "** VOIDED ** " + lsTransType;
					liVoidedCount = liVoidedCount + 1;
				}
				else if (lbReprintAvail)
				{
					if (!UtilityMethods
						.isEmpty(laReceiptLogData.getPrmtIssuanceId()))
					{
						PermitData laPrmtData =
							getPrmt(lsTransId, lsPrmtIssuanceId);

						lbReprintAvail =
							laPrmtData.getNoMFRecs() != 0;

						if (lbReprintAvail
							&& !UtilityMethods.isEmpty(
								laPrmtData.getLastTransId())
							&& !UtilityMethods.isEmpty(
								laReceiptLogData.getTransId()))
						{
							TransactionIdData laAuditTransId =
								new TransactionIdData(
									laPrmtData.getLastTransId());

							TransactionIdData laTransId =
								new TransactionIdData(
									laReceiptLogData
										.getTransId()
										.trim());

							if (laAuditTransId.compareTo(laTransId)
								> 0)
							{
								lbReprintAvail = false;
							}
						}

					}
				}
				if (!lbReprintAvail)
				{
					lsTransType = "** NOT AVAILABLE ** " + lsTransType;
				}
				// end defect 10844 

				laReceiptLogData.setTransType(lsTransType);

				// end defect 7019 

				lvReceipts.add(laReceiptLogData);
			}

			Integer liCSN = new Integer((String) aaData);
			ciSelectedCSN = liCSN.intValue();
			String lsLastCSN = (String) ((Vector) getLastCSN()).get(0);
			Integer liLastCSN =
				new Integer(
					lsLastCSN.substring(0, lsLastCSN.indexOf(" ")));

			// defect 7283 
			// Show reason for Supervisor Override 

			// defect 9636 
			// Show Supervisor Override for HQ Salvage type events
			// Renamed boolean from lbShowCashRegReceipt 
			String lsMsg = CommonConstant.STR_SPACE_EMPTY;

			boolean lbShowSupervisorOverride = true;

			if (lsTransCode.equals(TransCdConstant.DTANTD)
				|| lsTransCode.equals(TransCdConstant.DTANTK)
				|| lsTransCode.equals(TransCdConstant.DTAORD)
				|| lsTransCode.equals(TransCdConstant.DTAORK)
				|| lsTransCode.equals("DLRCOMPL"))
			{
				lsMsg =
					MiscellaneousConstant.OVERRIDE_MSG_DEALER_RECEIPT;
			}
			else if (lsTransCode.equals(TransCdConstant.SBRNW))
			{
				lsMsg =
					MiscellaneousConstant.OVERRIDE_MSG_SUBCON_RECEIPT;
			}
			else if (
				UtilityMethods.getEventType(lsTransCode).equals(
					TransCdConstant.SLVG_EVENT_TYPE))
			{
				lsMsg =
					MiscellaneousConstant
						.OVERRIDE_MSG_SALVAGE_TYPE_RECEIPT;
			}
			else if (!liCSN.equals(liLastCSN))
			{
				lsMsg =
					MiscellaneousConstant.OVERRIDE_MSG_NOT_LAST_RECEIPT;
			}
			else
			{
				lbShowSupervisorOverride = false;
			}

			Map laMap = new HashMap();
			// defect 7019
			// add new element to map to indicate whether to enable
			// Print Cash Receipt
			laMap.put(
				MiscellaneousConstant.MAP_ENTRY_CASHRECEIPT,
				new Boolean(liVoidedCount == 0));
			// end defect 7019
			laMap.put(MiscellaneousConstant.MAP_ENTRY_DATA, lvReceipts);
			laMap.put(
				MiscellaneousConstant.MAP_ENTRY_SHOW,
				new Boolean(lbShowSupervisorOverride));
			laMap.put(MiscellaneousConstant.MAP_ENTRY_MSG, lsMsg);
			// end defect 9636
			// end defect 7283 

			return laMap;
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"No receipt found with chosen Customer Sequence Number",
				"No Receipt");
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				aeCNFEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				aeIOEx);
		}
	}

	/**
	 * Process Supervisor Override Status
	 * 
	 * @param aaData Object
	 * @return Boolean
	 * @throws RTSException 
	 */
	private Boolean overrideStatus(Object aaData) throws RTSException
	{
		//Get from Admin cache
		MiscellaneousData laMiscellaneousData =
			MiscellaneousCache.getMisc(
				SystemProperty.getOfficeIssuanceNo(),
				SystemProperty.getSubStationId());

		// defect 8628 
		// Use ErrorsConstant
		if (laMiscellaneousData == null)
		{
			//throw new RTSException(150);
			throw new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}

		String lsPassword = laMiscellaneousData.getSupvOvrideCd();

		lsPassword = UtilityMethods.decryptPassword(lsPassword);

		if (lsPassword.trim().equals((String) aaData))
		{
			return new Boolean(true);
		}
		else
		{
			//new RTSException(150);
			throw new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		// end defect 8628 
	}

	/**
	 * Print the receipt(s) from the reprint function
	 * 
	 * @param  aaData Object
	 * @throws RTSException
	 */
	public Object printReceipts(Object aaData) throws RTSException
	{
		//Initialize and open ftp connection
		Print laPrint = new Print();
		Vector lvReceipts = (Vector) aaData;
		int liTransTime = 0;
		int liPrevTransTime = 0;
		Transaction laTransaction =
			new Transaction(TransCdConstant.RPRSTK);

		laTransaction.setUpFirstTransactionOfDay();

		//For each receipt, get the name, and send to printer
		for (int i = 0; i < lvReceipts.size(); i++)
		{
			ReceiptLogData laRcptLogData =
				(ReceiptLogData) lvReceipts.get(i);
			String lsRcptName = laRcptLogData.getRcptFileName();
			String lsTransCd = laRcptLogData.getTransCd();

			if (laRcptLogData.printFileExists())
			{
				// defect 11052 
				if (lsRcptName.endsWith(".JPG"))
				{
					(new PrintGraphics()).printForm275JPG(lsRcptName);
				}
				else
				{
					// end defect 11052 
					
					// defect 7067
					// Make print return exception so that if there was an
					// exception during printing the sticker the prntinvqty
					// filed would not get updated.
					laPrint.setReturnException(true);
					laPrint.sendToPrinter(lsRcptName, lsTransCd);
					// reset it back to false in case print is used again
					laPrint.setReturnException(false);
					// end defect 7067

					// defect 7018
					// setup for creating "RPRSTK" transaction
					// defect 7261
					// do not create a transaction if Cash Register Receipt 
					if (!(laRcptLogData.getTransId().equals("")
							|| laRcptLogData.getTransCd().equals("CSHREG")))
					{
						// defect 7460
						// Manage TransTime to avoid duplicate TransId
						RTSDate laCurrentDateTime = new RTSDate();
						liTransTime = laCurrentDateTime.get24HrTime();
						if (liTransTime == liPrevTransTime)
						{
							try
							{
								Thread.sleep(1000);
								laCurrentDateTime = new RTSDate();
								liTransTime =
									laCurrentDateTime.get24HrTime();
							}
							catch (InterruptedException aeIntEx)
							{
							}
						}
						liPrevTransTime = liTransTime;

						// Create CompleteTransaction
						CompleteTransactionData laCompleteTransactionData =
							new CompleteTransactionData();
						laCompleteTransactionData.setTransTime(liTransTime);
						// end defect 7460
						laCompleteTransactionData.setTransCode(
								TransCdConstant.RPRSTK);
						laCompleteTransactionData.setTransAMDate(
								laCurrentDateTime.getAMDate());

						TransactionHeaderData laTransactionHeaderData =
							laTransaction.addTransHeader(
									laCompleteTransactionData,
									true);
						// end defect 7018

						// defect 10756 
						laTransactionHeaderData.setTransTime(liTransTime);
						// end defect 10756 

						ReceiptLogData laReceiptLogData =
							(ReceiptLogData) lvReceipts.get(i);

						Map laMap = new HashMap();
						laMap.put(
								"OFC",
								new Integer(
										SystemProperty.getOfficeIssuanceNo()));
						laMap.put(
								"SUB",
								new Integer(SystemProperty.getSubStationId()));
						laMap.put(
								"WSID",
								new Integer(SystemProperty.getWorkStationId()));
						laMap.put(
								"EMPID",
								SystemProperty.getCurrentEmpId());
						laMap.put("DATA", laReceiptLogData);
						laMap.put("CSN", new Integer(ciSelectedCSN));
						// defect 7018
						// Add TransactionHeaderData and
						// CompleteTransactionData
						laMap.put("TRANSHDR", laTransactionHeaderData);
						laMap.put("CTDATA", laCompleteTransactionData);

						// defect 7284
						// assign map to ReprintStickerTransData 
						ReprintStickerTransData laRprtStkrTransData =
							new ReprintStickerTransData();
						laRprtStkrTransData.setReprintStickerHashMap(laMap);
						// end defect 7018

						TransactionCacheData laTransCacheData =
							new TransactionCacheData();
						laTransCacheData.setProcName(
								TransactionCacheData.INSERT);
						laTransCacheData.setObj(laRprtStkrTransData);

						Vector lvVector = new Vector();
						lvVector.add(laTransCacheData);

						try
						{
							// defect 7460
							// Use cache to process request 
							laTransaction.processCacheVector(lvVector);
							// end defect 7460  
						}
						catch (RTSException aeRTSEx)
						{
							// defect 7217
							throw aeRTSEx;
							// end defect 7217 
						}
					}
					// end defect 7261
					
					// defect 11052 
				}
				// end defect 11052 
			}
		}
		return aaData;
	}

	/**
	 * Print the void transaction report
	 *
	 * @param  aaVoidData Vector 
	 * @throws RTSException 
	 */
	private Object printVoidTransactionReport(Vector aaVoidData)
		throws RTSException
	{
		// defect 8628 
		// Use ReportConstant, initClientInfo()   
		ReportProperties laRptProps =
			new ReportProperties(ReportConstant.RPT_9001_REPORT_ID);

		laRptProps.initClientInfo();
		// end defect 8628 

		GenVoidReport laGenRpt =
			new GenVoidReport(
				ReportConstant.RPT_9001_REPORT_TITLE,
				laRptProps);

		//Generate report	
		laGenRpt.formatReport(aaVoidData);

		//Set report paramters
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGenRpt.getFormattedReport().toString(),
				ReportConstant.RPT_9001_FILENAME,
				ReportConstant.RPT_9001_REPORT_TITLE,
				ReportConstant.RPT_1_COPY,
				ReportConstant.PORTRAIT);

		String lsPageProps = Print.getDefaultPageProps();

		String lsRpt =
			lsPageProps
				+ System.getProperty("line.separator")
				+ laRptSearchData.getKey1();

		String lsFileName =
			UtilityMethods.saveReport(
				lsRpt,
				laRptSearchData.getKey2(),
				laRptSearchData.getIntKey1());

		if (lsFileName != null)
		{
			Print laPrint = new Print();
			laPrint.sendToPrinter(
				lsFileName,
				laRptSearchData.getKey2());
		}
		return laRptSearchData;
	}

	/**
	 * Process data method to direct requests to client layer or on 
	 * to server layer.
	 * 
	 * @param 	aiModule int
	 * @param 	aiFunctionId int 
	 * @param 	aaData Object 
	 * @throws 	RTSException
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		//Pass the call to the communication layer
		switch (aiFunctionId)
		{
			case (MiscellaneousConstant.COMPLETE_TRANSACTION) :
				{
					return completeVoidTransaction(aaData);
				}
			case (MiscellaneousConstant.PRINT_RECEIPTS) :
				{
					return printReceipts(aaData);
				}
				// PCR 34
			case (MiscellaneousConstant.REPRINT_ONE_RECEIPT) :
				{
					return getReceipts(aaData);
				}
			case MiscellaneousConstant.GET_LAST_CSN :
				{
					return getLastCSN();
				}
			case MiscellaneousConstant.OVERRIDE_NEEDED :
				{
					return overrideStatus(aaData);
				}
				// End PCR 34 	
			default :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
		}
	}

	/**
	 * Get PermitData from MF
	 * 
	 * @param asTransId 
	 * @param asPrmtIssuanceId
	 * @return PermitData
	 * @throws RTSException
	 */
	private PermitData getPrmt(
		String asTransId,
		String asPrmtIssuanceId)
		throws RTSException
	{
		PermitData laPrmtData = new PermitData();

		MiscellaneousRegClientBusiness laMscRegClientBusiness =
			new MiscellaneousRegClientBusiness();

		GeneralSearchData laGSD = new GeneralSearchData();
		laGSD.setKey1(CommonConstant.PRMT_PRMTID);
		laGSD.setKey2(asPrmtIssuanceId);
		laGSD.setKey3(TransCdConstant.RPRPRM);
		laGSD.setKey4(asTransId);

		try
		{
			laPrmtData =
				(PermitData) laMscRegClientBusiness.processData(
					GeneralConstant.MISCELLANEOUSREG,
					MiscellaneousRegConstant.PRMTINQ,
					laGSD);
		}
		catch (RTSException aeRTSEx)
		{
			laPrmtData.setMFDwnCd(1);
		}
		return laPrmtData;
	}

	/**
	 * Method updates Lastcsn.dat file with the current CustSeqNo
	 *
	 * @throws RTSException 
	 */
	public void setLastCSN() throws RTSException
	{
		String lsReceiptRootDir = SystemProperty.getReceiptsDirectory();
		String lsLocationForReceipt = lsReceiptRootDir;
		File laLastCSNFile =
			new File(lsLocationForReceipt + "\\lastcsn.dat");

		//If lastcsn exists, delete it
		if (laLastCSNFile.exists())
		{
			try
			{
				laLastCSNFile.delete();
			}
			catch (Exception aeIOEx)
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			}
		}

		//Create new lastcsn.dat
		try
		{
			laLastCSNFile.createNewFile();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
		try
		{
			//Write blank line to newly created lastcsn.dat	  
			FileOutputStream lpfsFileOutStream =
				new FileOutputStream(
					laLastCSNFile.getAbsolutePath(),
					true);
			OutputStreamWriter laOutStreamWtr =
				new OutputStreamWriter(lpfsFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutStreamWtr);
			laBuffWtr.write("");
			laBuffWtr.newLine();
			laBuffWtr.flush();
			laBuffWtr.close();
		}
		catch (FileNotFoundException aeFileIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeFileIOEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
	}

	/**
	 * Write VoidTransId to Void Trans Log
	 *
	 * @param avTransVector Vector
	 */
	private void updVoidTransLog(Vector avTransVector)
	{
		try
		{
			// If voiding transaction from prior date, do nothing to
			// VoidTransLog
			// Current AMDate 
			RTSDate laRTSDate = new RTSDate();

			VoidTransactionData laVoidTrans =
				(VoidTransactionData) avTransVector.get(0);

			if (laVoidTrans.getTransAMDate() != laRTSDate.getAMDate())
			{
				return;
			}
			// Voiding transaction from current date  
			boolean lbFileExists = false;
			// Vector of Voided TransIds to be added to Log
			Vector lvTransIds = new Vector();
			// Determine status of Void Trans Log 
			File laVoidTransLogFile =
				new File(
					SystemProperty.getReceiptsDirectory()
						+ SystemProperty.getVoidTransLogFileName());
			if (laVoidTransLogFile.exists())
			{
				FileInputStream lpfsFISVoidTransLog =
					new FileInputStream(laVoidTransLogFile);
				if (lpfsFISVoidTransLog.available() != 0)
				{
					ObjectInputStream in =
						new ObjectInputStream(lpfsFISVoidTransLog);
					lvTransIds = (Vector) in.readObject();
					in.close();
				}
				lbFileExists = true;
			}
			if (!lbFileExists)
			{
				laVoidTransLogFile.createNewFile();
			}
			for (int i = 0; i < avTransVector.size(); i++)
			{
				VoidTransactionData laTransaction =
					(VoidTransactionData) avTransVector.get(i);
				String lsVoidTransId =
					UtilityMethods.addPadding(
						new String[] {
							String.valueOf(
								laTransaction.getOfcIssuanceNo()),
							String.valueOf(
								laTransaction.getTransWsId()),
							String.valueOf(
								laTransaction.getTransAMDate()),
							String.valueOf(
								laTransaction.getTransTime())},
						new int[] { 3, 3, 5, 6 },
						"0");

				// Add Void Trans Id to VoidTransLog
				lvTransIds.add(0, lsVoidTransId);
			}
			ObjectOutputStream laOOSVoidTransLog =
				new ObjectOutputStream(
					new FileOutputStream(laVoidTransLogFile));
			laOOSVoidTransLog.writeObject(lvTransIds);
			laOOSVoidTransLog.flush();
			laOOSVoidTransLog.close();
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			aeCNFEx.printStackTrace();
		}
	}
}
