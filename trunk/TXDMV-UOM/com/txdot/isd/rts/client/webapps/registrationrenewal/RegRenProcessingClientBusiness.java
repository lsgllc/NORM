package com.txdot.isd.rts.client.webapps.registrationrenewal;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.InternetRegRecData;
import com.txdot.isd.rts.services.data.InternetTransactionData;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 *
 * RegRenProcessingClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Chen		09/03/2002	CQU100003700. DB down handling.
 * C. Chen		09/23/2002	CQU100004681. REG103 Escape, added 
 *							undoCheckout().
 * C. Chen		03/31/2003	CQU100004885, PCR24, add 
 *							printSpecialPlateAddressChangeReport
 * B Brown		08/20/2003  CQU100004885  Added code to method 
 *							printSpecialPlateAddressChangeReport() 
 *							and added case RegRenProcessingConstants.
 *							GET_SPCL_PLT_ADDR_CHG_RPT: to method 
 *							processData. Added method 
 *							printSpecialPlateAddressChangeReport
 *							(ReportSearchData aData) to call the 
 *							processData and printReport methods.
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							modify printReport(Vector)
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		05/27/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore.
 *							modify saveReports(Object)
 *							defect 7078 Ver 5.2.0
 * Bob Brown	08/16/2004	Coded a return in method process Data,
 *                          so the VPR case processing does not fall
 *                          thru to the next case statement.
 *							defect 7112 Ver 5.2.1
 * Bob Brown    11/30/2004  Changed the spelling of Intenet to Internet.
 *                          Modify method 
 *                          printVendorReport(ReportSearchData).
 *                          Add main(String []),write(String,String).
 *                          Add 3 graph variables.
 *                          defect 7577. Version 5.2.2
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		02/17/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7889 ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3  
 * K Harrell	02/09/2009	Modify to remove reference to Special 
 * 							Plates Address Chng Report.  Also, remove
 * 							code associated with getting countyinfo
 *							from Server as now retrieving from cache. 
 *							Additional Class cleanup. 
 * 							delete getCountyName()
 * 							 printSpecialPlateAddressChangeReport(),
 * 							 printSpecialPlateAddressChangeReport(ReportSearchData)
 * 							modify processData() 
 * 							defect 9941 Ver Defect_POS_D 
 * K Harrell	02/09/2009	Modify to include new Internet Deposit 
 * 							Reconciliation Report. Removed unused 
 * 							calls now processing through processData()
 *  						delete getAddressChangeReport(),
 * 							 getInternetTransReport(), getNextVeh(),
 * 							 getTransaction,(),getVeh(),processRenewal(),
 * 							 setTXInfo() 
 * 							 modify processData() 
 * 							defect 9935 Ver Defect_POS_D  
 * K Harrell	08/17/2009	Implement UtilityMethods.addPCLandSaveReport()
 * 							add VENDOR_PAYMENT_RPT, 
 * 						 	 VENDOR_PAYMENT_RPT_COMPLETED, 
 * 							 VENDOR_PAYMENT_RPT_EXCEPTION_PREFACE,
 * 							 VENDOR_PAYMENT_RPT_FAILED 
 * 							add checkOutVeh(Object)
 * 							delete saveReports(), checkOutVeh(int, int, Object), 
 * 							 main(), write(), csReportCompleted,
 * 							 csReportFailed, csReportException   
 * 							modify processData(),
 * 							 printVendorReport(ReportSearchData),
 * 							 printVendorReport(),  
 * 							 printReport() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	02/27/2010	Remove Internet Address Change Report
 * 							modify processData()  
 * 							defect 10387 Ver POS_640 
 * K Harrell	03/10/2010	Remove reference to Update Renewal Address
 * 							Not used. 
 * 							modify processData() 
 * 							defect 10239 Ver POS_640
 * K Harrell	12/26/2010	delete VENDOR_PAYMENT_RPT,
 * 							 VENDOR_PAYMENT_RPT_COMPLETED,
 * 							 VENDOR_PAYMENT_RPT_EXCEPTION_PREFACE,
 * 							 VENDOR_PAYMENT_RPT_FAILED
 * 							delete printVendorReport(), 
 * 							 printVendorReport(ReportSearchData),
 * 							 printReport()  
 * 							defect 10694 Ver 6.7.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class handles the client processing of internet renewals, as 
 * well as batch proceses like the Vendor Payment report.
 * See the processData(int,int,Object) method for all the processes 
 * handled by this class.
 *
 * @version	6.7.0			12/26/2010
 * @author	Administrator
 * <br>Creation Date:		10/10/2001 13:01:13
 */
public class RegRenProcessingClientBusiness
{
	// defect 10694 
	// defect 8628 
	//	private static final String VENDOR_PAYMENT_RPT =
	//		"Internet Vendor Electronic Payment Transaction Report";

	//	private static final String VENDOR_PAYMENT_RPT_COMPLETED =
	//		VENDOR_PAYMENT_RPT + " completed";
	//
	//	private static final String VENDOR_PAYMENT_RPT_EXCEPTION_PREFACE =
	//		VENDOR_PAYMENT_RPT + "\n";
	//
	//	private static final String VENDOR_PAYMENT_RPT_FAILED =
	//		VENDOR_PAYMENT_RPT + " failed";
	// end defect 8628 
	// end defect 10694 

	/**
	 * RegRenProcessingServerBusiness constructor comment.
	 */
	public RegRenProcessingClientBusiness()
	{
		super();
	}

	/**
	 * checkOutVeh
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object checkOutVeh(Object aaData) throws RTSException
	{
		InternetRegRecData laData = (InternetRegRecData) aaData;

		//update record status to 'Checked Out'
		Hashtable lhmHTable = new Hashtable();
		lhmHTable.put(
			"TransWsId",
			SystemProperty.getWorkStationId() + "");
		lhmHTable.put(
			"SubstaId",
			SystemProperty.getSubStationId() + "");
		lhmHTable.put(
			"RegPltNo",
			laData
				.getCompleteRegRenData()
				.getVehBaseData()
				.getPlateNo());
		lhmHTable.put(
			"VIN",
			laData.getCompleteRegRenData().getVehBaseData().getVin());
		lhmHTable.put(
			"DocNo",
			laData.getCompleteRegRenData().getVehBaseData().getDocNo());
		lhmHTable.put("CntyStatusCd", laData.getStatus());
		lhmHTable.put("NewStatus", CommonConstants.IN_PROCESS + "");

		return Comm.sendToServer(
			GeneralConstant.INTERNET_REG_REN_PROCESSING,
			RegRenProcessingConstants.VEH_CHECKOUT,
			lhmHTable);
	}

	/**
	 * prefillInventory
	 * 
	 * @param aaOrigCompTransData CompleteTransactionData
	 * @return CompleteTransactionData
	 * @throws RTSException
	 */
	public CompleteTransactionData prefillInventory(CompleteTransactionData aaOrigCompTransData)
		throws RTSException
	{
		CompleteTransactionData laPrefillCompTransData =
			(CompleteTransactionData) Comm.sendToServer(
				GeneralConstant.INVENTORY,
				InventoryConstant.ISSUE_INVENTORY,
				aaOrigCompTransData);

		return laPrefillCompTransData;
	}

	//	/**
	//	 * printReport
	//	 * 
	//	 * @param avReports Vector
	//	 * @return Object
	//	 * @throws RTSException
	//	 */
	//	private Object printReport(Vector avReports) throws RTSException
	//	{
	//		ReportSearchData laRptSearchData =
	//			(ReportSearchData) avReports.get(0);
	//
	//		// defect 8628 
	//		String lsFileName = laRptSearchData.getKey1();
	//
	//		if (!UtilityMethods.isEmpty(lsFileName))
	//		{
	//			Print laPrint = new Print();
	//			// end defect 8628 
	//			laPrint.sendToPrinter(lsFileName);
	//		}
	//		return laRptSearchData;
	//	}

	//	/**
	//	 * Print the Vendor Report for the past day.
	//	 * Entry point for automatic printing.
	//	 * 
	//	 * @return String
	//	 */
	//	public String printVendorReport()
	//	{
	//		ReportSearchData laRptSearchData = new ReportSearchData();
	//
	//		// defect 8628 
	//		laRptSearchData.initForClient(ReportConstant.BATCH);
	//		// end defect 8628
	//
	//		String lsToday =
	//			com
	//				.txdot
	//				.isd
	//				.rts
	//				.services
	//				.webapps
	//				.util
	//				.UtilityMethods
	//				.formatToday(
	//				"yyyyMMdd");
	//
	//		String lsYesterday =
	//			com
	//				.txdot
	//				.isd
	//				.rts
	//				.services
	//				.webapps
	//				.util
	//				.UtilityMethods
	//				.addDay(
	//				lsToday,
	//				"yyyyMMdd",
	//				-1);
	//
	//		laRptSearchData.setKey2(lsYesterday);
	//		laRptSearchData.setKey3(lsYesterday);
	//
	//		return printVendorReport(laRptSearchData);
	//	}

	//	/**
	//	 * This method is the "driver" for getting the Vendor
	//	 * Payment report data, and printing the report.
	//	 *
	//	 * @param aaData ReportSearchData
	//	 * @return String
	//	 */
	//	private String printVendorReport(ReportSearchData aaData)
	//	{
	//		try
	//		{
	//			Vector lvVendorReport =
	//				(Vector) processData(GeneralConstant
	//					.INTERNET_REG_REN_PROCESSING,
	//					RegRenProcessingConstants.GET_VENDOR_RPT,
	//					aaData);
	//
	//			printReport(lvVendorReport);
	//
	//			// defect 8628
	//			BatchLog.write(VENDOR_PAYMENT_RPT_COMPLETED);
	//
	//			return BatchSchedule.SUCCESSFUL;
	//		}
	//		catch (RTSException leEx)
	//		{
	//			BatchLog.write(VENDOR_PAYMENT_RPT_FAILED);
	//			BatchLog.error(
	//				VENDOR_PAYMENT_RPT_EXCEPTION_PREFACE
	//					+ leEx.getDetailMsg());
	//			// end defect 8628 
	//			return BatchSchedule.UNSUCCESSFUL;
	//		}
	//	}

	/**
	 * processData
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
		switch (aiFunctionId)
		{
			case RegRenProcessingConstants.NO_DATA :
				{
					return aaData;
				}

			case RegRenProcessingConstants.VEH_CHECKOUT :
				{
					// defect 8628
					// Only one parameter needed
					return checkOutVeh(aaData);
					// end defect 8628 
				}

				// defect 10239
				// Never implemented  
				// case RegRenProcessingConstants.PROC_REG_RENEWAL_ADDR :
				// end defect 10239 

				// defect 9941				
				// Intentially dropping through
			case RegRenProcessingConstants.GET_PROC_VEHICLE :
			case RegRenProcessingConstants.PROC_REG_RENEWAL :
			case RegRenProcessingConstants.GET_NEXT_NEW :
			case RegRenProcessingConstants.GET_NEXT_HOLD :
			case RegRenProcessingConstants.GET_NEXT_ANY :
			case RegRenProcessingConstants.SET_TX_INFO :
			case RegRenProcessingConstants.GET_TX :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
				// end defect 9941 

				// defect 9935 
				// Reports 
				// Intentially dropping through
				// defect 10387 	
				// case RegRenProcessingConstants.GET_ADDR_CHANGE_RPT :
				// end defect 10387 
			case RegRenProcessingConstants.GET_TRANS_RPT :
			case RegRenProcessingConstants.GET_VENDOR_RPT :
			case RegRenProcessingConstants.GET_DEPOSIT_RECON_RPT :
				{
					// defect 10142
					return UtilityMethods.addPCLandSaveReport(
						Comm.sendToServer(
							aiModule,
							aiFunctionId,
							aaData));
					// end defect 10142 
				}
				// end defect 9935 

			default :
				{
					return null;
				}
		}
	}

	/**
	 * Undo the checkout of a transaction.
	 *
	 * @param aaData object of InternetRegRecData.
	 * @return boolean - true if the undo checkout is normally done, 
	 *		false if server/db is down and the undo action is 
	 *		written to cache.
	 * @throws RTSException which is not of server/db down.
	 */
	public boolean undoCheckout(InternetRegRecData aaData)
		throws RTSException
	{
		Hashtable lhmHTable = new Hashtable();
		lhmHTable.put(
			"VehBaseData",
			aaData.getCompleteRegRenData().getVehBaseData());
		lhmHTable.put("CntyStatusCd", aaData.getStatus());
		lhmHTable.put("Action", "UndoCheckout");
		try
		{
			Comm.sendToServer(
				GeneralConstant.INTERNET_REG_REN_PROCESSING,
				RegRenProcessingConstants.UNDO_CHECKOUT,
				lhmHTable);
		}
		catch (RTSException leEx)
		{
			// CQU100003700, DB down
			// =====================
			if (leEx.getMsgType().equals(RTSException.SERVER_DOWN)
				|| leEx.getMsgType().equals(RTSException.DB_DOWN))
			{
				InternetTransactionData laInetTransData =
					new InternetTransactionData(lhmHTable);
				laInetTransData.setTransDateTime(
					RTSDate.getCurrentDate());
				// DB/server is down, cache to disk.		 	
				TransactionCacheData laTransactionCacheData =
					new TransactionCacheData();
				laTransactionCacheData.setObj(laInetTransData);
				laTransactionCacheData.setProcName(
					TransactionCacheData.UPDATE);
				Vector lvTrans = new Vector();
				lvTrans.addElement(laTransactionCacheData);
				Transaction.writeToCache(lvTrans);
				return false;
			}
			else
			{
				// CQU100003700, DB down
				// =====================
				// error is because of something else, report. 
				throw leEx;
			}
		}
		return true;
	}
}