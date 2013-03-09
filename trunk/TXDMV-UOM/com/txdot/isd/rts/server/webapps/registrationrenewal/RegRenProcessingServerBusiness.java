package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;
import com.txdot.isd.rts.server.webapps.reports.GenInternetDepositReconReport;
import com.txdot.isd.rts.server.webapps.reports.GenInternetTransReport;
import com.txdot.isd.rts.server.webapps.reports.GenVendorReport;
import com.txdot.isd.rts.server.webapps.util.Log;

/*
 *
 * RegRenProcessingServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ------------	-----------	--------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/14/2002	Set OfcissaunceId & Substaid for genAddress
 *                          ChangeReport.Failure to set these values was
 *                          causing an RTSException when generating the
 *                          footer of the report.
 *							CQU100003811.
 * Clifford		09/03/2002	CQU100003700. DB down handling. getNext() 
 *                          change
 * Clifford 	09/25/2002	CQU100004681. REG103 Escape, added undoCheck
 *                          out()
 * Clifford 	11/12/2002	CQU100005009. potential double processing, 
 *                          modified checkoutVeh,
 * Clifford		03/31/2003	CQU100004885, PCR24, add getSpecialPlate
 *                          AddressChangeReport
 * B Brown	    07/03/2003	For CQU 6310,changed the getVendorReport 
 *                          method to look at central time,
 *                          since epay recently moved to Dallas. Am 
 *                          using constant:RegRenProcessingConstants.
 *                          ITRANS_RPT_CUTOFF_TIME to achieve this.
 * B Brown		07/17/2003	Defect6358 - Undo CQU 6310,changes  going 
 *                          back to Eastern time because Global
 *                          payments system stayed Eastern time.
 * B Brown		08/20/2003   CQU100004885  Added body of method get
 *                          SpecialPlateAddressChangeReport. Added
 *                          case RegRenProcessingConstants.GET_SPCL_PLT_
 *                          ADDR_CHG_RPT: to method processData.
 *                          Added method processSpecialPlateAddrChg
 *                          Report(). 
 * B Brown		05/17/2004	During county processing, the last "hold" 
 *                          record being processed, if left in hold, 
 *                          was coming out "in process"
 *							modified method getNext.
 *							defect 6380 Ver 5.2.0
 * Jeff S.		02/25/2005	Get code up to standards. Changed a 
 * 							non-static call to a static call.
 * 							modify genAddressChangeReport(), 
 * 								checkoutVeh(), genInternetTransReport(),
 * 								genPaymentReport(), getNext(), 
 * 								getRenewalCount(), getTransaction(), 
 * 								getVeh(), processRenewal(), 
 * 								processSpecialPlateAddrChgReport(),
 * 								qryStatus(), renewVehAddress(),
 * 								setTXInfo(), undoCheckout()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/17/2005	Deprecated not used method.
 * 							deprecate genPaymentReport()
 * 							defect 7889 Ver 5.2.3
 * K Harrell	10/05/2005	Additional Java 1.4 Cleanup 
 * 							deprecate processSpecialPlateAddrChgReport()
 * 							defect 7889 Ver 5.2.3   
 * K Harrell	02/09/2009	Remove code associated with Special Plates
 * 							Address Change Report
 * 							delete getSpecialPlateAddressChangeReport(),
 * 							  processSpecialPlateAddrChgReport(),
 * 							  getCountyName(),genPaymentReport() 
 * 							modify processData()
 * 							defect 9941 Ver Defect_POS_D
 * K Harrell	02/09/2009	Add processing for Deposit Reconciliation 
 * 							Report. Add insert into Admin_Log for 
 * 							 other Reports 
 * 							add genDepositReconReport()
 * 							modify processData(),genAddressChangeReport(),
 * 							 genInternetTransReport(),getVendorReport() 
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	02/12/2009	Modified in refactor of classes/methods 
 * 							where IVTRS changed to Internet 
 * 							defect 9935 Ver Defect_POS_D
 * Bob Brown	02/27/2009	Batch sends ReportSearchData vs. Vector
 * 							Added conditional processing 
 * 							modify getVendorReport() 
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	04/06/2009	Batch DepositReconReport to have 7 copies 
 * 							modify genDepositReconReport()
 * 							defect 10022 Ver Defect_POS_E   
 * K Harrell	04/08/2009	Modify Batch DepositReconReport to expand 
 * 							reporting range as appropriate. 
 * 							modify genDepositReconReport()
 * 							defect 10027 Ver Defect_POS_E   
 * K Harrell	06/15/2009	Copy csNextScreen to new ReportSearchData
 * 							modify genDepositReconReport() 
 * 							defect 10011 Ver Defect_POS_F 
 * K Harrell	08/18/2009	Use ReportConstant
 * 							add initReportProperties() 
 * 							modify genAddressChangeReport(), 
 * 							 genDepositReconReport(), 
 * 							 genInternetTransReport(),
 * 							 getVendorReport() 
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	09/23/2009	Retain Next Screen for Vendor Payment Report 
 * 							modify getVendorReport()
 * 							defect 10023 Ver Defect_POS_F
 * B Brown		11/17/2009	When the clerk declines an IRENEW plate,
 * 							look for other vehicles with the same trace
 * 							number (shopping cart), having the same 
 * 							dollar amount. If you find any, just update 
 * 							cntystatuscd to refund pending(6), and do 
 * 							not make the Epay call. Let INET total up 
 * 							the shopping cart amount and refund the 
 * 							shopping cart all at once. 
 * 							modify processRenewal()
 * 							defect 10040 Ver Defect_POS_H
 * B Brown		02/17/2010	Ensure that only declines execute the 
 * 							InternetTransaction.qryPymntamt() method.
 * 							modify processRenewal()
 * 							defect 10379 Ver Defect_POS_640
 * K Harrell	02/27/2010	Remove Internet Address Change Report 
 * 							modify processData()
 * 							defect 10387 Ver POS_640 
 * K Harrell	03/10/2010	Remove unused method. 
 * 							delete renewVehAddress()
 * 							modify processData()
 * 							defect 10239 Ver POS_640  
 * K Harrell	09/24/2010	modify chkoutVeh(), getNext() 
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * The class serves as the server business layer for county processing,
 * internet processing, and batch processing
 * of internet registration renewals.
 *
 * @version	6.6.0 			09/24/2010
 * @author	George Donoso
 * <br>Creation Date:		10/10/2001 15:08:10
 */

public class RegRenProcessingServerBusiness
{
	/**
	 * RegRenProcessingServerBusiness constructor comment.
	 */
	public RegRenProcessingServerBusiness()
	{
		super();
	}

	/** 
	 * Check out the vehicle.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	private Object checkoutVeh(Object aaData)
	{
		Vector lvResult = null;
		if (aaData instanceof Hashtable)
		{
			Hashtable lhtData = (Hashtable) aaData;

			int liOrigStatusCount = 0;
			try
			{
				// look at the count of original status
				// should be 1
				liOrigStatusCount = qryStatus(lhtData);
			}
			catch (RTSException aeRTSEx)
			{
				return aeRTSEx;
			}
			if (liOrigStatusCount != 1)
			{
				// the transaction has been checked out (and processed)
				// by someone else.
				lvResult = new Vector();
				lvResult.add("0");
				lvResult.add("AlreadyCheckedoutOrProcessed");
				return lvResult;
			}

			// The transaction is still in the original status
			DatabaseAccess laDBAccess = new DatabaseAccess();
			
			// defect 10598 
			Vector lvInProcsTrans = new Vector();
			// end defect 10598 

			try
			{
				InternetTransaction laItrntTrans =
					new InternetTransaction(laDBAccess);
				InternetData laItrntData = new InternetData(laDBAccess);

				// UOW #1 BEGIN
				laDBAccess.beginTransaction();

				Object laCheckOutResult =
					laItrntTrans.updateStatus(lhtData);

				Object laCTData =
					laItrntData.qryItrntDataComplTransData(lhtData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 

				// defect 10598 
				// Add Vector of Pending Transactions to
				//  returned vector.  
				String lsVIN =
					((CompleteTransactionData) laCTData)
						.getOrgVehicleInfo()
						.getVehicleData()
						.getVin();
				GeneralSearchData laGSD = new GeneralSearchData();
				laGSD.setKey1("VIN");
				laGSD.setKey2(lsVIN);
				
				// UOW #2 BEGIN 
				Transaction laTransactionSQL =
					new Transaction(laDBAccess);
				laDBAccess.beginTransaction();
				lvInProcsTrans =
					laTransactionSQL.qryInProcessTransaction(laGSD);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 

				lvResult = new Vector();
				lvResult.add(laCheckOutResult);
				lvResult.add(laCTData);
				lvResult.add(lvInProcsTrans);
				// end defect 10598 
				return lvResult;
			}
			catch (RTSException aeRTSEx)
			{
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (RTSException aeRTSEx2)
				{
				}
				return aeRTSEx;
			}
		}
		return null;
	}

	/**
	 * Do an Internet refund.
	 * 
	 * @param ahtData Hashtable
	 */
	private void doRefund(Hashtable ahtData)
	{

		Log.fine("RegRenProcessingServerBusiness, doRefund, Enter");
		RefundData laRefundData = new RefundData();
		laRefundData.setRefAmt((String) ahtData.get("RefAmt"));
		laRefundData.setOrigTraceNo((String) ahtData.get("TraceNo"));
		laRefundData.setPymtOrderId(
			(String) ahtData.get("PymntOrderId"));

		VehicleBaseData laVehBaseData = new VehicleBaseData();
		laVehBaseData.setPlateNo((String) ahtData.get("RegPltNo"));
		laVehBaseData.setVin((String) ahtData.get("VIN"));
		laVehBaseData.setDocNo((String) ahtData.get("DocNo"));
		laVehBaseData.setOwnerCountyNo(
			(String) ahtData.get("CntyIssuanceNo"));

		laRefundData.setVehBaseData(laVehBaseData);

		try
		{
			Refund laRefund = new Refund();
			Log.finer(
				"RegRenProcessingServerBusiness, doRefund, Refund obj initiated");
			laRefund.doRefund(laRefundData);
		}
		catch (Exception aeEx)
		{
			// County Client directly calling doRefund,
			// if it fails, does not need to report to County client,
			// let the batch program to pickup at a later
			// time and try again till it succeeds.
			Log.error(aeEx);
		}
		Log.fine("RegRenProcessingServerBusiness, doRefund, Exit");
	}

	// defect 10387 
	//	/**
	//	 * Setup the Generate Address Change Report
	//	 * 
	//	 * @param aaData 
	//	 * @return Object
	//	 * @throws RTSException
	//	 */
	//	private Object genAddressChangeReport(Object aaData)
	//		throws RTSException
	//	{
	//		DatabaseAccess laDBAccess = new DatabaseAccess();
	//		try
	//		{
	//			// defect 9935
	//			Vector lvData = (Vector) aaData;
	//			ReportSearchData laRptSearchData =
	//				(ReportSearchData) lvData.get(0);
	//			AdministrationLogData aaLogData =
	//				(AdministrationLogData) lvData.get(1);
	//
	//			// UOW #1 BEGIN 
	//			// ADMIN LOG 
	//			laDBAccess.beginTransaction();
	//			AdministrationLog laAdminLog =
	//				new AdministrationLog(laDBAccess);
	//			laAdminLog.insAdministrationLog(aaLogData);
	//			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
	//			// UOW #1 END
	//			// end defect 9935  
	//
	//			// defect 10023 
	//			// UOW #2 BEGIN 
	//			// REPORT PROPERTIES 
	//			ReportProperties laRptProps =
	//				initReportProperties(
	//					laRptSearchData,
	//					laDBAccess,
	//					ReportConstant.RPT_2001_ADDR_CHNG_ID);
	//			// UOW #2 END
	//
	//			String lsFromDate = laRptSearchData.getKey2();
	//			String lsToDate = laRptSearchData.getKey3();
	//
	//			// UOW #3 BEGIN 
	//			laDBAccess.beginTransaction();
	//			InternetTransaction laItrntTrans =
	//				new InternetTransaction(laDBAccess);
	//			Vector lvQueryResults =
	//				laItrntTrans.qryAddressChangeReport(laRptSearchData);
	//			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
	//			// UOW #3 END 
	//
	//			String[] larrDateHdr =
	//				getReportHeader(
	//					lsFromDate,
	//					lsToDate,
	//					RegRenProcessingConstants.IADDR);
	//			Vector lvKeyData = new Vector();
	//			lvKeyData.add(larrDateHdr[0]);
	//
	//			GenAddressChangeReport laAddrChgRpt =
	//				new GenAddressChangeReport(
	//					ReportConstant.RPT_2001_ADDR_CHNG_TITLE,
	//					laRptProps,
	//					lvKeyData);
	//
	//			laAddrChgRpt.formatReport(lvQueryResults);
	//
	//			ReportSearchData laReportSearchData =
	//				new ReportSearchData(
	//					laAddrChgRpt.getFormattedReport().toString(),
	//					ReportConstant.RPT_2001_ADDR_CHG_FILENAME,
	//					ReportConstant.RPT_2001_ADDR_CHNG_TITLE,
	//					ReportConstant.RPT_7_COPIES,
	//					ReportConstant.PORTRAIT);
	//
	//			laReportSearchData.setNextScreen(
	//				laRptSearchData.getNextScreen());
	//
	//			return laReportSearchData;
	//			// end defect 10023 
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
	//			throw aeRTSEx;
	//		}
	//	}
	// end defect 10387 

	/**
	 * genDepositReconReport
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genDepositReconReport(Object aaData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			ReportSearchData laRptSearchData = new ReportSearchData();

			// defect 10023  			
			// default to Batch Properties
			String lsRptFileName =
				ReportConstant
					.RPT_2004_INTERNET_DEPOSIT_RECON_BATCH_FILENAME;

			int liNumRptFiles = ReportConstant.RPT_7_COPIES;

			// If Vector, initiated by Clerk 
			if (aaData instanceof Vector)
			{
				lsRptFileName =
					ReportConstant
						.RPT_2004_INTERNET_DEPOSIT_RECON_ONLINE_FILENAME;

				liNumRptFiles = ReportConstant.RPT_1_COPY;

				Vector lvDepositReconData = (Vector) aaData;
				laRptSearchData =
					(ReportSearchData) lvDepositReconData.get(0);
				AdministrationLogData aaLogData =
					(AdministrationLogData) lvDepositReconData.get(1);

				// UOW #1A BEGIN
				laDBAccess.beginTransaction();
				AdministrationLog laAdminLog =
					new AdministrationLog(laDBAccess);
				laAdminLog.insAdministrationLog(aaLogData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1A END 
			}
			else
			{
				laRptSearchData = (ReportSearchData) aaData;

				// defect 10027 
				// Client will send same date for Min/Max 
				// Server will override if appropriate 
				// UOW #1B BEGIN
				laDBAccess.beginTransaction();
				InternetDepositReconHstry laItrntDepReconHstry =
					new InternetDepositReconHstry(laDBAccess);
				Hashtable lhtDates =
					laItrntDepReconHstry
						.qryBatchDepositReconRptDateRange();
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1B END 

				RTSDate laMinDate = null;
				RTSDate laMaxDate = null;
				if (lhtDates != null)
				{
					laMinDate =
						(RTSDate) lhtDates.get(
							RegRenProcessingConstants.MINDATE);
					laMaxDate =
						(RTSDate) lhtDates.get(
							RegRenProcessingConstants.MAXDATE);
					if (laMinDate != null
						&& laMinDate.compareTo(laRptSearchData.getDate1())
							< 0)
					{
						laRptSearchData.setDate1(laMinDate);
					}
					if (laMaxDate != null
						&& laMaxDate.compareTo(laRptSearchData.getDate2())
							> 0)
					{
						laRptSearchData.setDate2(laMaxDate);
					}
				}
				// end defect 10027 

			}
			// UOW #2 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					ReportConstant.RPT_2004_INTERNET_DEPOSIT_RECON_ID);
			// UOW #2 END 

			// UOW #3 BEGIN 
			// Report Data
			laDBAccess.beginTransaction();
			InternetDepositRecon laItrntDepositRecon =
				new InternetDepositRecon(laDBAccess);
			Vector lvQueryResults =
				laItrntDepositRecon.qryInternetDepositRecon(
					laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 

			GenInternetDepositReconReport laGrp =
				new GenInternetDepositReconReport(
					ReportConstant
						.RPT_2004_INTERNET_DEPOSIT_RECON_TITLE,
					laRptProps,
					laRptSearchData);

			laGrp.formatReport(lvQueryResults);

			ReportSearchData laReportSearchData =
				new ReportSearchData(
					laGrp.getFormattedReport().toString(),
					lsRptFileName,
					ReportConstant
						.RPT_2004_INTERNET_DEPOSIT_RECON_TITLE,
					liNumRptFiles,
					ReportConstant.PORTRAIT);

			// defect 10011
			laReportSearchData.setNextScreen(
				laRptSearchData.getNextScreen());
			// end defect 10011 

			return laReportSearchData;
			// end defect 10023  
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Generate Internet Transaction Report.
	 * 
	 * @param aaSearchKeys Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object genInternetTransReport(Object aaData)
		throws RTSException
	{

		DatabaseAccess laDBAccess = new DatabaseAccess();
		laDBAccess.beginTransaction();

		try
		{
			// defect 9935 
			Vector lvData = (Vector) aaData;

			ReportSearchData laRptSearchData =
				(ReportSearchData) lvData.get(0);

			AdministrationLogData aaLogData =
				(AdministrationLogData) lvData.get(1);

			// UOW #1 BEGIN 	
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(aaLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			// end defect 9935 

			// defect 10023 
			// UOW #2 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					ReportConstant.RPT_2002_ITRNT_TRANS_RECON_ID);
			// UOW #2 END 

			String lsFromDate = laRptSearchData.getKey2();
			String lsToDate = laRptSearchData.getKey3();

			String[] larrDateHdr =
				getReportHeader(
					lsFromDate,
					lsToDate,
					RegRenProcessingConstants.ITRANS);

			Vector lvKeyData = new Vector();
			lvKeyData.add(larrDateHdr[0]);

			RTSDate laFrom =
				new RTSDate(
					UtilityMethods
						.parseDate(
							larrDateHdr[1],
							"MM/dd/yyyy HH:mm:ss")
						.getTime());

			RTSDate laTo =
				new RTSDate(
					UtilityMethods
						.parseDate(
							larrDateHdr[2],
							"MM/dd/yyyy HH:mm:ss")
						.getTime());
			laRptSearchData.setDate1(laFrom);
			laRptSearchData.setDate2(laTo);

			// UOW #3 BEGIN
			laDBAccess.beginTransaction();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);
			// Report Data 		
			Vector lvQueryResults =
				laItrntTrans.qryTransactionReport(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END

			// Format Report 
			GenInternetTransReport laItrntTransRpt =
				new GenInternetTransReport(
					ReportConstant.RPT_2002_ITRNT_TRANS_RECON_TITLE,
					laRptProps,
					lvKeyData);

			laItrntTransRpt.formatReport(lvQueryResults);

			ReportSearchData laReportSearchData =
				new ReportSearchData(
					laItrntTransRpt.getFormattedReport().toString(),
					ReportConstant.RPT_2002_ITRNT_TRANS_RECON_FILENAME,
					ReportConstant.RPT_2002_ITRNT_TRANS_RECON_TITLE,
					ReportConstant.RPT_7_COPIES,
					ReportConstant.PORTRAIT);

			// defect 10011
			laReportSearchData.setNextScreen(
				laRptSearchData.getNextScreen());
			// end defect 10011 

			return laReportSearchData;
			// end defect 10023 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			return aeRTSEx;
		}
	}

	/**
	 * Get the next Internet Record.
	 * 
	 * @param aiFunctionID int
	 * @param aaData Object
	 * @return Object
	 */
	private Object getNext(int aiFunctionID, Object aaData)
	{

		if (aaData instanceof Hashtable)
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();

			try
			{
				InternetTransaction laItrntTrans =
					new InternetTransaction(laDBAccess);

				InternetData laItrntData = new InternetData(laDBAccess);
				// UOW #1 BEGIN 	
				laDBAccess.beginTransaction();
				InternetRegRecData laInetData =
					laItrntTrans.qryNext(
						aiFunctionID,
						(Hashtable) aaData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 	

				if (laInetData != null)
				{
					// find one
					// fill necessary check out parameters

					// defect 6380 start
					// Check the last record updated, to see if its the 
					// same as the next one to be processed.
					if (((Hashtable) aaData)
						.get("PlateNo")
						.equals(
							laInetData
								.getCompleteRegRenData()
								.getVehBaseData()
								.getPlateNo()))
					{
						return null;
					}
					else
						// end defect 6380 
						{
						((Hashtable) aaData).put(
							"RegPltNo",
							laInetData
								.getCompleteRegRenData()
								.getVehBaseData()
								.getPlateNo());
						((Hashtable) aaData).put(
							"VIN",
							laInetData
								.getCompleteRegRenData()
								.getVehBaseData()
								.getVin());
						((Hashtable) aaData).put(
							"DocNo",
							laInetData
								.getCompleteRegRenData()
								.getVehBaseData()
								.getDocNo());
						((Hashtable) aaData).put(
							"CntyStatusCd",
							laInetData.getStatus());
						((Hashtable) aaData).put(
							"NewStatus",
							CommonConstants.IN_PROCESS + "");

						// UOW #2 BEGIN 
						laDBAccess.beginTransaction();
						// Checkout  (Update)						
						String lsCheckOutResult =
							(String) laItrntTrans.updateStatus(
								(Hashtable) aaData);

						// Get the CompleteTransactionData
						CompleteTransactionData laTransData =
							laItrntData.qryItrntDataComplTransData(
								(Hashtable) aaData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// UOW #2 END 

						if (lsCheckOutResult != null
							&& lsCheckOutResult.equals("1"))
						{
							// defect 10598
							// Add Vector of Pending Transactions 
							String lsVIN =
								laTransData
									.getOrgVehicleInfo()
									.getVehicleData()
									.getVin();

							// UOW #3 BEGIN
							Transaction laTransactionSQL =
								new Transaction(laDBAccess);
							laDBAccess.beginTransaction();
							GeneralSearchData laGSD =
								new GeneralSearchData();
							laGSD.setKey1("VIN");
							laGSD.setKey2(lsVIN);
							Vector lvInProcsTrans =
								laTransactionSQL.qryInProcessTransaction(
									laGSD);
							laDBAccess.endTransaction(
								DatabaseAccess.COMMIT);
							// UOW #3 END 

							Vector lvResult = new Vector();
							lvResult.add(laInetData);
							lvResult.add(laTransData);
							lvResult.add(lvInProcsTrans);
							// end defect 10598 
							
							return lvResult;
						}
					}
				}
			}
			catch (RTSException aeRTSEx)
			{
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (RTSException aeRTSEx2)
				{
				}
				return aeRTSEx;
			}
		}
		return null;
	}

	/**
	 * Get the Renewal Count.
	 * 
	 * @param aaData Object
	 * @return Integer
	 * @throws RTSException
	 */
	private Integer getRenewalCount(Object aaData) throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		InternetTransaction laItrntTrans =
			new InternetTransaction(laDBAccess);

		try
		{
			// UOW #1 BEGIN 	
			laDBAccess.beginTransaction();
			Integer laInteger =
				new Integer(laItrntTrans.qryItrntRenewCount(aaData));
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return laInteger;
			// UOW #1 END 	
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Get the Report Header.
	 * 
	 * @param asFromDate String
	 * @param asToDate String
	 * @param aiReportType int
	 * @return String[]
	 */
	private String[] getReportHeader(
		String asFromDate,
		String asToDate,
		int aiReportType)
	{
		if (asFromDate != null && asFromDate.length() > 0)
		{
			asFromDate =
				UtilityMethods.formatDate(
					asFromDate,
					"yyyyMMdd",
					"MM/dd/yyyy HH:mm:ss");
		}

		if (asToDate != null && asToDate.length() > 0)
		{
			asToDate =
				UtilityMethods.formatDate(
					asToDate,
					"yyyyMMdd",
					"MM/dd/yyyy")
					+ " 23:59:59";
		}
		else
		{
			asToDate =
				UtilityMethods.formatToday("MM/dd/yyyy HH:mm:ss");
		}

		String lsFrom = (asFromDate == null) ? "    " : asFromDate;
		String lsTo = UtilityMethods.formatToday("MM/dd/yyyy HH:mm:ss");

		boolean lbToDateIsToday = false;
		lbToDateIsToday =
			UtilityMethods.isToday(asToDate, "MM/dd/yyyy HH:mm:ss");
		if (!lbToDateIsToday)
		{
			lsTo = asToDate;
		}

		if (aiReportType == RegRenProcessingConstants.ITRANS)
		{
			lsFrom =
				lsFrom.substring(0, 11)
					+ ReportConstant.ITRANS_RPT_CUTOFF_TIME;
			if (!lbToDateIsToday)
			{
				lsTo =
					lsTo.substring(0, 11)
						+ ReportConstant.ITRANS_RPT_CUTOFF_TIME;
				lsTo =
					UtilityMethods.addOneDay(
						lsTo,
						"MM/dd/yyyy HH:mm:ss");
			}
		}

		if (aiReportType == RegRenProcessingConstants.VENDOR)
		{
			lsFrom =
				lsFrom.substring(0, 11)
					+ ReportConstant.VENDOR_RPT_CUTOFF_TIME;
			if (!lbToDateIsToday)
			{
				lsTo =
					lsTo.substring(0, 11)
						+ ReportConstant.VENDOR_RPT_CUTOFF_TIME;
				lsTo =
					UtilityMethods.addOneDay(
						lsTo,
						"MM/dd/yyyy HH:mm:ss");
			}
			else
			{
				// the vendor report time is eastern time, show eastern time
				lsTo =
					UtilityMethods.addOneHour(
						lsTo,
						"MM/dd/yyyy HH:mm:ss");
			}
		}

		String lsHeader = lsFrom + " THROUGH " + lsTo;
		if (aiReportType == RegRenProcessingConstants.ITRANS)
		{
			lsHeader += " (CENTRAL TIME)";
		}
		if (aiReportType == RegRenProcessingConstants.VENDOR)
		{
			lsHeader += " (EASTERN TIME)";
		}

		return new String[] { lsHeader, lsFrom, lsTo };
	}

	/**
	 * Get the Internet Transaction.
	 * 
	 * @param aaSearchKeys Object
	 * @return Object
	 */
	private Object getTransaction(Object aaSearchKeys)
	{
		if (aaSearchKeys instanceof Hashtable)
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();

			// defect 10598
			Vector lvReturn = new Vector();
			// end defect 10598 
			try
			{
				InternetData laItrntData = new InternetData(laDBAccess);
				// UOW #1 BEGIN	
				laDBAccess.beginTransaction();
				CompleteTransactionData laCTData =
					laItrntData.qryItrntDataComplTransData(
						(Hashtable) aaSearchKeys);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				lvReturn.add(laCTData);
				// UOW #1 END 

				// defect 10598 
				// UOW #2 BEGIN
				Transaction laTransactionSQL =
					new Transaction(laDBAccess);
				String lsVIN =
					laCTData
						.getOrgVehicleInfo()
						.getVehicleData()
						.getVin();
				GeneralSearchData laGSD = new GeneralSearchData();
				laGSD.setKey1("VIN");
				laGSD.setKey2(lsVIN);

				laDBAccess.beginTransaction();
				Vector lvPndngTrans =
					laTransactionSQL.qryInProcessTransaction(laGSD);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				lvReturn.add(lvPndngTrans);
				// UOW #2 END 
				// end defect 10598 
				
				return lvReturn;
			}
			catch (RTSException aeRTSEx)
			{
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (RTSException aeRTSEx2)
				{
				}
				return aeRTSEx;
			}

		}
		return null;
	}

	/**
	 * Get the Vehicle.
	 * 
	 * @param aaSearchKeys Object
	 * @return Object
	 */
	private Object getVeh(Object aaSearchKeys)
	{
		if (aaSearchKeys.getClass().isInstance(new Hashtable()))
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();

			try
			{
				InternetTransaction laItrntTrans =
					new InternetTransaction(laDBAccess);
				// UOW #1 BEGIN 	
				laDBAccess.beginTransaction();
				Hashtable lhtRenewals =
					laItrntTrans.qryItrntRenew(
						(Hashtable) aaSearchKeys);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END  
				return lhtRenewals;

			}
			catch (RTSException aeRTSEx)
			{
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (RTSException aeRTSEx2)
				{
				}
				return aeRTSEx;
			}
		}
		return null;
	}

	/**
	 * Get the Vendor Payment Report.
	 * 
	 * @param aaSearchKeys Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object getVendorReport(Object aaData) throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// defect 9935 
			ReportSearchData laRptSearchData = null;

			// defect 10023 			
			// default to Batch Properties
			String lsRptFileName =
				ReportConstant.RPT_2003_VENDOR_PAYMENT_BATCH_FILENAME;

			int liNumRptFiles = ReportConstant.RPT_7_COPIES;

			// Online Sends Vector; Batch sends just ReportSearchData
			if (aaData instanceof Vector)
			{
				lsRptFileName =
					ReportConstant
						.RPT_2003_VENDOR_PAYMENT_ONLINE_FILENAME;
				liNumRptFiles = ReportConstant.RPT_1_COPY;
				// end defect 10023  

				Vector lvData = (Vector) aaData;
				laRptSearchData = (ReportSearchData) lvData.get(0);

				AdministrationLogData aaLogData =
					new AdministrationLogData();

				aaLogData = (AdministrationLogData) lvData.get(1);

				// UOW #1 BEGIN
				laDBAccess.beginTransaction();
				AdministrationLog laAdminLog =
					new AdministrationLog(laDBAccess);
				laAdminLog.insAdministrationLog(aaLogData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 
			}
			else
			{
				laRptSearchData = (ReportSearchData) aaData;
			}
			// end defect 9935

			// UOW #2 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					ReportConstant.RPT_2003_VENDOR_PAYMENT_ID);
			// UOW #2 END 

			String lsFromDate = laRptSearchData.getKey2();
			String lsToDate = laRptSearchData.getKey3();

			// defect 6358
			String[] larrDateHdr =
				getReportHeader(
					lsFromDate,
					lsToDate,
					RegRenProcessingConstants.VENDOR);

			// Get vendor report data			 				
			Vector lvVendorReportData =
				getVendorReportData(laRptSearchData);

			// defect 10023 
			// Use ReportConstant for Title
			// format the vendor report
			GenVendorReport laVendorRpt =
				new GenVendorReport(
					ReportConstant.RPT_2003_VENDOR_PAYMENT_TITLE,
					laRptProps,
					larrDateHdr[0]);
			// end defect 10023 

			laVendorRpt.formatReport(lvVendorReportData);

			// defect 10023
			ReportSearchData laReportSearchData =
				new ReportSearchData(
					laVendorRpt.getFormattedReport().toString(),
					lsRptFileName,
					ReportConstant.RPT_2003_VENDOR_PAYMENT_TITLE,
					liNumRptFiles,
					ReportConstant.PORTRAIT);

			laReportSearchData.setNextScreen(
				laRptSearchData.getNextScreen());

			return laReportSearchData;
			// end defect 10023  
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.error(
				"RegRenProcessingServerBusiness: "
					+ UtilityMethods.getStackTrace(aeRTSEx));
			throw aeRTSEx;
		}
		catch (Exception aeEx)
		{
			Log.error(
				"RegRenProcessingServerBusiness: "
					+ UtilityMethods.getStackTrace(aeEx));
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		finally
		{
			Log.fine(
				"RegRenProcessingServerBusiness, "
					+ "getVendorReport, Exit");
		}
	}

	/**
	 * Get the Vendor Report Data.
	 * 
	 * @param aaSearchKeys ReportSearchData
	 * @return Vector
	 * @throws RTSException
	 */
	private Vector getVendorReportData(ReportSearchData aaSearchKeys)
		throws RTSException
	{
		try
		{
			Log.fine(
				"RegRenProcessingServerBusiness, getVendorReportData, Enter");

			String lsFromDate = aaSearchKeys.getKey2();
			String lsToDate = aaSearchKeys.getKey3();

			// Global payment cutoff 1:00am	Central Time
			// Epay is eastern time so cutoff is 2:00am
			// To Date should be added one day more for search		
			lsToDate = UtilityMethods.addOneDay(lsToDate, "yyyyMMdd");

			int liOfcIssuanceNo = aaSearchKeys.getIntKey1();
			String lsVendorId = String.valueOf(2000 + liOfcIssuanceNo);

			EpayVendorReport laVendorReport = new EpayVendorReport();
			Vector lvVendorReportData =
				laVendorReport.getVendorReport(
					lsVendorId,
					lsFromDate,
					lsToDate);

			Log.fine(
				"RegRenProcessingServerBusiness, "
					+ "getVendorReportData, Exit");
			return lvVendorReportData;
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
	}

	/** 
	 * Init ReportProperties
	 * 
	 * @param aaDBAccess
	 * @param aaRptSearchData
	 * @param asReportId 
	 * @return ReportProperties
	 * @throws RTSException
	 */
	private ReportProperties initReportProperties(
		ReportSearchData aaRptSearchData,
		DatabaseAccess aaDBAccess,
		String asReportId)
		throws RTSException
	{
		ReportsServerBusiness laRptSvrBusiness =
			new ReportsServerBusiness();

		return laRptSvrBusiness.initReportProperties(
			aaRptSearchData,
			aaDBAccess,
			asReportId);
	}

	/**
	 * Setup the various Internet Reports
	 * 
	 * @param aiModule int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		try
		{
			switch (aiFunctionId)
			{

				case RegRenProcessingConstants.GET_PROC_VEHICLE :
					{
						return getVeh(aaData);
					}
				case RegRenProcessingConstants.GET_NEXT_NEW :
					{
						return getNext(aiFunctionId, aaData);
					}
				case RegRenProcessingConstants.GET_NEXT_HOLD :
					{
						return getNext(aiFunctionId, aaData);
					}
				case RegRenProcessingConstants.GET_NEXT_ANY :
					{
						return getNext(aiFunctionId, aaData);
					}
				case RegRenProcessingConstants.PROC_REG_RENEWAL :
					{
						return processRenewal(aaData);
					}

					// defect 10239
					// case RegRenProcessingConstants.PROC_REG_RENEWAL_ADDR :
					//	{
					//		return renewVehAddress(aaData);
					//	}
					// end defect 10239

				case RegRenProcessingConstants.VEH_CHECKOUT :
					{
						return checkoutVeh(aaData);
					}
				case RegRenProcessingConstants.UNDO_CHECKOUT :
					{
						return undoCheckout(aaData);
					}

					// defect 10387 
					//	case RegRenProcessingConstants.GET_ADDR_CHANGE_RPT :
					//		{
					//			return genAddressChangeReport(aaData);
					//		}
					// end defect 10387

				case RegRenProcessingConstants.GET_TRANS_RPT :
					{
						return genInternetTransReport(aaData);
					}
				case RegRenProcessingConstants.GET_TX :
					{
						return getTransaction(aaData);
					}
				case RegRenProcessingConstants.SET_TX_INFO :
					{
						return setTXInfo(aaData);
					}
					// defect 9935
				case RegRenProcessingConstants.GET_VENDOR_RPT :
					{
						return getVendorReport(aaData);
					}
				case RegRenProcessingConstants.GET_DEPOSIT_RECON_RPT :
					{
						return genDepositReconReport(aaData);
					}
					// end defect 9935 
				case RegRenProcessingConstants.GET_RENEWAL_COUNT :
					{
						return getRenewalCount(aaData);
					}
				case RegRenProcessingConstants.PROC_REFUND_RESULT :
					{
						return processRefundResult(aaData);
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		return null;
	}

	/**
	 * Get the Process Refund Result.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	private Object processRefundResult(Object aaData)
	{
		Refund laRefund = new Refund();
		boolean lbResult =
			laRefund.processRefundResult((RefundData) aaData);
		return new Boolean(lbResult);
	}

	/**
	 * Process Renewal.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object processRenewal(Object aaData) throws RTSException
	{
		Object laResult = null;

		if (aaData instanceof Hashtable)
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);
			try
			{
				// UOW #1 BEGIN 
				laDBAccess.beginTransaction();
				laResult =
					laItrntTrans.updateRenewal((Hashtable) aaData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 
			}
			catch (RTSException aeRTSEx)
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				throw aeRTSEx;
			}

			Hashtable lhtData = (Hashtable) aaData;
			int liCntyStatusCd =
				(new Integer((String) lhtData.get("CntyStatusCd")))
					.intValue();

			if (liCntyStatusCd
				== CommonConstants.DECLINED_REFUND_PENDING)
			{
				// defect 10040, 10379
				// only doRefund if the are no other plates in the same
				// shopping cart with the same pymntamt.
				// If there is more than 1 vehicle in the shopping cart
				// with the same pymtamt, let the nightly INET process
				// add up the amounts and refund the whole cart.  
				try
				{
					// UOW #2 BEGIN 
					laDBAccess.beginTransaction();
					if (laItrntTrans.qryPymntamt((Hashtable) aaData)
						< 1)
					{
						doRefund(lhtData);
					}
					laDBAccess.endTransaction(DatabaseAccess.NONE);
					// UOW #2 END 
				}
				catch (RTSException aeRTSEx)
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
					throw aeRTSEx;
				}
				// end defect 10379, 10040
			}

		}
		return laResult;
	}

	/**
	 * Query the Status of the Internet Renewal.
	 * 
	 * @param ahtData Hashtable
	 * @return int
	 * @throws RTSException
	 */
	private int qryStatus(Hashtable ahtData) throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN
			laDBAccess.beginTransaction();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);
			int liNumRows = laItrntTrans.qryStatus(ahtData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			return liNumRows;
		}
		catch (RTSException aeEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeEx;
		}

	}

	//	/**
	//	 * Renew Vehicle Address.
	//	 * 
	//	 * @param aaData Object
	//	 * @return Object
	//	 */
	//	private Object renewVehAddress(Object aaData)
	//	{
	//		Object laResult = null;
	//		if (aaData.getClass().isInstance(new Hashtable()))
	//		{
	//
	//			DatabaseAccess laDBAccess = new DatabaseAccess();
	//			try
	//			{
	//				// UOW #1 BEGIN 
	//				laDBAccess.beginTransaction();
	//				InternetTransaction laItrntTrans =
	//					new InternetTransaction(laDBAccess);
	//				laResult =
	//					laItrntTrans.updateRenewalAddress(
	//						(Hashtable) aaData);
	//				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
	//				// UOW #1 END 
	//				return laResult;
	//			}
	//			catch (RTSException aeRTSEx)
	//			{
	//				try
	//				{
	//					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
	//					return aeRTSEx;
	//				}
	//				catch (RTSException aeRTSEx2)
	//				{
	//				}
	//			}
	//		}
	//		return laResult;
	//	}

	/**
	 * Set the TX Info.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	private Object setTXInfo(Object aaData)
	{
		Object lsResult = null;
		if (aaData.getClass().isInstance(new TransactionCacheData()))
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();

			try
			{
				// UOW #1 BEGIN
				laDBAccess.beginTransaction();
				InternetTransaction laItrntTrans =
					new InternetTransaction(laDBAccess);
				lsResult =
					laItrntTrans.updateTXInfo(
						(TransactionCacheData) aaData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 
			}
			catch (RTSException aeRTSEx)
			{
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
					return aeRTSEx;
				}
				catch (RTSException aeRTSEx2)
				{
				}
			}
		}
		return lsResult;
	}

	/**
	 * Undo the Checkout.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object undoCheckout(Object aaData) throws RTSException
	{
		Hashtable lhtHashTable = (Hashtable) aaData;
		Object laRet = null;
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN
			laDBAccess.beginTransaction();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);
			laRet = laItrntTrans.updateCntyStatus(lhtHashTable);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				return aeRTSEx;
			}
			catch (RTSException aeRTSEx2)
			{
			}
		}
		return laRet;
	}
}
