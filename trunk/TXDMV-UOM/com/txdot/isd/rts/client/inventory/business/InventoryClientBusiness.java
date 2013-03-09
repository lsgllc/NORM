package com.txdot.isd.rts.client.inventory.business;

import java.util.Vector;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.inventory.GenInventoryAllocateReport;
import com.txdot.isd.rts.services.reports.inventory.GenInventoryDeletedReport;
import com.txdot.isd.rts.services.reports.inventory.GenInventoryProfileReport;
import com.txdot.isd.rts.services.reports.inventory.InventoryHoldReport;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * InventoryClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Rakesh D.	10/05/2001	Added GenInvDelReport.
 * Min Wang		02/20/2003  Modefied saveReports().	Defect CQU100005530.
 * Min Wang		07/07/2003  Add case InventoryConstant.
 *							CALCULATE_INVENTORY_UNKNOWN in processData().
 *							Reflow Server Business to reduce the number 
 *							of quiries required to issue an item.
 *							Defect 6076
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							modify printReports(Vector)
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		05/26/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore.
 *							modify saveReports(Object)
 *							defect 7078 Ver 5.2.0
 * K Harrell	10/25/2004	removed calls to isFirstTransactionofDay()
 *							modify allocInvItm(),delInvItm(),recvInvToDb()
 *							defect 7581  Ver 5.2.2
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	03/18/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							add'l Javadoc cleanup
 * 							deprecated printReports() 
 * 							defect 7899  Ver 5.2.3 
 * Ray Rowehl	06/27/2005	Cleanup comments to meet standards.
 * 							Remove unused methods.
 * 							Create constants for Reports.
 * 							Match constants to ReportConstants where 
 * 							possible.
 * 							delete printReports()
 * 							defect 7890 Ver 5.2.3  
 * Ray Rowehl	07/06/2005	Rename some ReportConstants used.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/06/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Removed duplicate function requests
 * 							modify processData()
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/08/2005	Found that several reports are not printed
 * 							except from report viewer.  Reports:
 * 							GENERATE_INVENTORY_ALLOCATION_REPORT,
 * 							GENERATE_INVENTORY_RECEIVED_REPORT, 
 * 							GENERATE_INVENTORY_DEL_REPORT
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	12/02/2005	Found that the line.separator was not used 
 * 							correctly.  Was showing "null" on the report.
 * 							modify saveReports()
 * 							defect 7890 Ver 5.2.3
 * Min Wang		04/27/2006 	Fix no report generated when doing Inv Del 
 * 							and Allocation with View Report is not 
 * 							selected. 
 * 							modify processData()
 * 							defect 8740 Ver 5.2.3 
 * Ray Rowehl	02/17/2007	Add a main method to facilitate testing 
 * 							server side functions.
 * 							Use ReportsConstants where they were already
 * 							defined.
 * 							add main()
 * 							defect 9116 Ver Special Plates
 * Min Wang		03/14/2007	Add case for VI Rejection Report.
 * 							add GENERATE_VI_REJECTION_REPORT
 * 							defect 9117 Ver Special Plates
 * Min Wang		09/05/2007	Consolidate the case statements since this 
 * 							logic does the same thing.
 * 							modify processData()
 *							defect 8745 Ver Defect_POS_B
 * K Harrell	08/16/2009	Implement UtilityMethods.addPCLandSaveReport()
 * 							Implement ReportProperties.initClient()
 * 							Additional Cleanup. 
 * 							add recvInvToDB(Object,int) 
 * 							delete recvIntvToDb(Object,int,int), 
 * 							 saveReports()
 * 							modify processData(), genInvAlloctnRpt(), 
 * 							 genInvDelRpt(), genInvItmsOnHoldRpt(), 
 * 							 genInvProfileRpt() 
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This class contains a method which passes the module name, function 
 * id, and data to the communication layer.
 *
 * @version	Defect_POS_F	08/16/2009 
 * @author	Charlie Walker
 * <br>Creation Date:		09/04/2001 15:49:46
 */
public class InventoryClientBusiness
{
	private static final String RPT_NAME_3001_INV_DEL = "INVDEL";
	private static final String RPT_NAME_3011_INV_ALLOC = "INVALL";
	private static final String RPT_NAME_3041_INV_ON_HOLD = "INVHLD";
	private static final String RPT_NAME_3051_INV_PROF = "INVPRF";
	private static final String RPT_TITLE_3041_INV_ON_HLD =
		"INVENTORY ITEMS ON HOLD REPORT";
	private static final String RPT_TITLE_3051_INV_PROF =
		"INVENTORY PROFILE REPORT";

	/**
	 * InventoryClientBusiness constructor comment.
	 */
	public InventoryClientBusiness()
	{
		super();
	}

	/**
	 * Send a test request through to the Server Side.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// set up to test PLP handling
		InventoryAllocationData laTestRec =
			new InventoryAllocationData();

		laTestRec.setItmCd("ARTS");
		laTestRec.setInvItmYr(0);
		laTestRec.setISA(false);
		laTestRec.setUserPltNo(false);
		laTestRec.setItrntReq(false);
		laTestRec.setOfcIssuanceNo(170);
		laTestRec.setTransWsId(0);
		laTestRec.setTransAmDate(new RTSDate("").getAMDate());
		laTestRec.setTransEmpId("MWANG");
		laTestRec.setRequestorIpAddress("R25700000");
		laTestRec.setRequestorRegPltNo("PTP60F");

		// Test the get next vi item 
		try
		{
			RTSApplicationController.setDBUp(true);
			InventoryClientBusiness laICB =
				new InventoryClientBusiness();
			laICB.processData(
				GeneralConstant.INVENTORY,
				InventoryConstant.INV_GET_NEXT_VI_ITEM_NO,
				laTestRec);

			System.out.println("Successful");
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println(aeRTSEx);
			aeRTSEx.printStackTrace();
		}
	}

	/**
	 * Since transaction headers can only be generated on the client, 
	 * need to generate the Inventory Allocation transaction header and
	 * pass it to the server.
	 *
	 * @param aaData Object 
	 * @return InventoryAllocationUIData
	 * @throws RTSException   
	 */
	private Object allocInvItm(Object aaData) throws RTSException
	{
		Vector lvData = (Vector) aaData;

		// Create the transaction header for an allocation transaction.
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();
			
		Transaction laTrans = new Transaction(TransCdConstant.INVALL);

		// Inventory Allocation transactions are created @ the server.   
		// Add TransHdr for CustSeqNo 0 if necessary
		// defect 7581
		laTrans.setUpFirstTransactionOfDay();
		// end defect 7581
		 
		TransactionHeaderData laTransHdr =
			laTrans.addTransHeader(laCompleteTransactionData, true);

		// Set the transaction header as the third element of the vector
		lvData.addElement(laTransHdr);

		// Make the call to the server to complete the inventory 
		// allocation.
		return Comm.sendToServer(
			GeneralConstant.INVENTORY,
			InventoryConstant.ALLOC_INVENTORY_ITEMS,
			lvData);
	}

	/**
	 * Since transaction headers can only be generated on the client, 
	 * need to generate the Inventory Delete transaction header and 
	 * pass it to the server.
	 *
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object delInvItm(Object aaData) throws RTSException
	{
		// Create the transaction header for a delete transaction
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();
			
		Transaction laTrans = new Transaction(TransCdConstant.INVDEL);

		// Inventory Delete transactions are created @ the server.   
		// Add TransHdr for CustSeqNo 0 if necessary
		// defect 7581 
		laTrans.setUpFirstTransactionOfDay();
		// end defect 7581 
		
		TransactionHeaderData laTransHdr =
			laTrans.addTransHeader(laCompleteTransactionData, true);
		Vector lvData = new Vector();
		lvData.addElement(aaData);
		lvData.addElement(laTransHdr);

		// Make the call to the server to complete the inventory delete.
		return Comm.sendToServer(
			GeneralConstant.INVENTORY,
			InventoryConstant.DELETE_INVENTORY_ITEM,
			lvData);
	}

	/**
	 * Perform the data setup to generate the Inventory Allocation 
	 * Report.
	 *
	 * @param aaData Object - The data required for the report. 
	 * @return Object 
	 * @throws RTSException
	 */
	private Object genInvAlloctnRpt(Object aaData) throws RTSException
	{
		// defect 8628 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3011_INVENTORY_ALLOCATION_REPORT_ID);
				
		laRptProps.initClientInfo();
		// end defect 8628 

		GenInventoryAllocateReport laGIAR =
			new GenInventoryAllocateReport(
				ReportConstant.RPT_3011_INVENTORY_ALLOCATE_REPORT_TITLE,
				laRptProps);

		Vector lvQueryResults =
			(Vector) ((Vector) aaData).get(CommonConstant.ELEMENT_1);
		laGIAR.formatReport(lvQueryResults);

		// defect 8628 
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGIAR.getFormattedReport().toString(),
				RPT_NAME_3011_INV_ALLOC,
				ReportConstant.RPT_3011_INVENTORY_ALLOCATE_REPORT_TITLE,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);

		return UtilityMethods.addPCLandSaveReport(laRptSearchData);
		// end defect 8628  
	}

	/**
	 * Perform the data setup to generate the Inventory Delete Report.
	 * 
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object genInvDelRpt(Object aaData) throws RTSException
	{
		// defect 8628 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3001_INVENTORY_DELETED_REPORT_ID);
				
		laRptProps.initClientInfo();
		// end defect 8628 

		GenInventoryDeletedReport laGIDR =
			new GenInventoryDeletedReport(
				ReportConstant.RPT_3001_INVENTORY_DELETE_REPORT_TITLE,
				laRptProps);

		Vector lvQueryResults = (Vector) aaData;
		laGIDR.formatReport(lvQueryResults);

		// defect 8628 
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGIDR.getFormattedReport().toString(),
				RPT_NAME_3001_INV_DEL,
				ReportConstant.RPT_3001_INVENTORY_DELETE_REPORT_TITLE,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);

		return UtilityMethods.addPCLandSaveReport(laRptSearchData);
		// end defect 8628 
	}

	/**
	 * Perform the data setup to generate the Inventory Hold Report.
	 *
	 * @param aaData Object
	 * @return Object
	 */
	private Object genInvItmsOnHoldRpt(Object aaData) throws RTSException 
	{
		// defect 8628 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3041_INVENTORY_HOLD_REPORT_ID);
				
		laRptProps.initClientInfo();
		// end defect 8628

		InventoryHoldReport laIHR =
			new InventoryHoldReport(
				RPT_TITLE_3041_INV_ON_HLD,
				laRptProps);

		Vector lvQueryResults = (Vector) aaData;

		laIHR.formatReport(lvQueryResults);

		// defect 8628 
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laIHR.getFormattedReport().toString(),
				RPT_NAME_3041_INV_ON_HOLD,
				RPT_TITLE_3041_INV_ON_HLD,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);

		return UtilityMethods.addPCLandSaveReport(laRptSearchData);
		// end defect 8628 
	}

	/**
	 * Perform the data setup to generate the Inventory Profile Report.
	 *
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object genInvProfileRpt(Object aaData) throws RTSException
	{
		// defect 8628 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3051_INVENTORY_PROFILE_REPORT_ID);
				
		laRptProps.initClientInfo();
		// end defect 8628 

		GenInventoryProfileReport laGIPR =
			new GenInventoryProfileReport(
				RPT_TITLE_3051_INV_PROF,
				laRptProps);

		Vector lvQueryResults = (Vector) aaData;

		laGIPR.formatReport(lvQueryResults);

		// defect 8628
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGIPR.getFormattedReport().toString(),
				RPT_NAME_3051_INV_PROF,
				RPT_TITLE_3051_INV_PROF,
				ReportConstant.RPT_1_COPY,
				ReportConstant.PORTRAIT);
				
		return UtilityMethods.addPCLandSaveReport(laRptSearchData);
		// end defect 8628 
	}

	/**
	 * Used to pass the module name, function id, and data from the UI  
	 * to the server or take local action as needed.
	 * 
	 * @param aiModule int - represents which module is making the call
	 * @param aiFunctionId int - internal function id
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
			case InventoryConstant.ALLOC_INVENTORY_ITEMS :
				{
					return allocInvItm(aaData);
				}
				// defect 8628
				// Reordered case statements to more easily read
				// Implement UtilityMethods.addPCLandSaveReport()
				// NOTE:  The "PRINT" requests actually just generate! 
				//        RTS returns to Main Menu. 
			case InventoryConstant.CALCULATE_INVENTORY_UNKNOWN :
				{
					ValidateInventoryPattern laValidateInvPatrn =
						new ValidateInventoryPattern();
					return laValidateInvPatrn.calcInvUnknown(aaData);
				}
			case InventoryConstant.DELETE_INVENTORY_ITEM :
				{
					return delInvItm(aaData);
				}
			case InventoryConstant.GENERATE_INVENTORY_PROFILE_REPORT :
				{
					return genInvProfileRpt(aaData);
				}
			case InventoryConstant.GENERATE_INVENTORY_RECEIVED_REPORT :
				{
					return recvInvToDB(aaData, aiModule);
				}
				// Intentionally dropping through
			case InventoryConstant
				.GENERATE_INVENTORY_ALLOCATION_REPORT :
			case InventoryConstant.PRINT_INVENTORY_ALLOCATION_REPORT :
				{
					return genInvAlloctnRpt(aaData);
				}

				// Intentionally dropping through 	
			case InventoryConstant
				.GENERATE_INVENTORY_ITEMS_ONHOLD_REPORT :
			case InventoryConstant
				.PRINT_INVENTORY_ITEMS_ONHOLD_REPORT :
				{
					return genInvItmsOnHoldRpt(aaData);
				}

				// Intentionally dropping through 
			case InventoryConstant.GENERATE_INVENTORY_DEL_REPORT :
			case InventoryConstant.PRINT_INVENTORY_DEL_REPORT :
				{
					return genInvDelRpt(aaData);
				}

				// Intentionally dropping through 
			case InventoryConstant.GENERATE_INVENTORY_INQUIRY_REPORT :
			case InventoryConstant.GENERATE_INVENTORY_ACTION_REPORT :
			case InventoryConstant
				.GENERATE_RECEIVE_INVENTORY_HISTORY_REPORT :
			case InventoryConstant
				.GENERATE_DELETE_INVENTORY_HISTORY_REPORT :
			case InventoryConstant.GENERATE_VI_REJECTION_REPORT :
				{
					return UtilityMethods.addPCLandSaveReport(
						Comm.sendToServer(
							aiModule,
							aiFunctionId,
							aaData));
				}
				// end defect 8628 
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
	 * Since transaction headers can only be generated on the client, 
	 * need to generate the Inventory Receive transaction header and 
	 * pass it to the server.
	 *
	 * @param aaData Object 
	 * @param aiModule int
	 * @return Object
	 * @throws RTSException 
	 */
	private Object recvInvToDB(Object aaData, int aiModule)
		throws RTSException
	{
		Vector lvData = (Vector) aaData;

		// Create the transaction header for a receive invoice 
		// transaction
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();
		Transaction laTrans = new Transaction(TransCdConstant.INVRCV);

		// Receive Invoice transactions are created @ the server.   
		// Add TransHdr for CustSeqNo 0 if necessary
		laTrans.setUpFirstTransactionOfDay();
		// end defect 7581 
		
		TransactionHeaderData laTransHdr =
			laTrans.addTransHeader(laCompleteTransactionData, true);
		lvData.addElement(laTransHdr);

		// defect 8628 
		// Implement UtilityMethods.addPCLandSaveReport()
		return UtilityMethods.addPCLandSaveReport(
			Comm.sendToServer(
				aiModule,
				InventoryConstant.RECEIVE_INVENTORY_TO_DB,
				lvData));
		// end defect 8628 
	}

//	/**
//	 * Method to save the reports to the report directory.
//	 * 
//	 * <p>Note that we do not have an option to print without viewing
//	 * the report.
//	 *
//	 * @param aaData Object
//	 * @return Vector
//	 * @throws RTSException 
//	 */
//	private Vector saveReports(Object aaData) throws RTSException
//	{
//		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
//		UtilityMethods laUtil = new UtilityMethods();
//		Print laPrint = new Print();
//		Vector lvReports = new Vector();
//
//		String lsPageProps = laPrint.getDefaultPageProps();
//		String lsRpt =
//			lsPageProps
//				+ Print.getPRINT_TRAY_2()
//				+ CommonConstant.SYSTEM_LINE_SEPARATOR
//				+ laRptSearchData.getKey1();
//
//		String lsFileName =
//			laUtil.saveReport(
//				lsRpt,
//				laRptSearchData.getKey2(),
//				laRptSearchData.getIntKey1());
//
//		laRptSearchData.setKey1(lsFileName);
//		laRptSearchData.setIntKey2(ReportConstant.PORTRAIT);
//		lvReports.add(laRptSearchData);
//
//		return lvReports;
//	}

}
