package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * GenFeeReport.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/14/2001	New Class
 * Ray Rowehl 	09/18/2001	Reflow logic to have better reuse.
 * Min Wang		09/20/2001	Add logic for Employee Breaks.
 * Min Wang		09/28/2001	Add constants
 * Min Wang		10/05/2001	Allow for primary break on either
 * 							WsId or EmpId.  Also add
 *							logic for intermediate break.
 * Min Wang		11/07/2001	Add Internet Columns
 * M Rajangam	?			move data classes to services.data.
 * Min Wang		02/20/2002	improve page break handling.
 * Min Wang		04/26/2002  print Qty for summary.
 * Min Wang		05/03/2002 	print entity total for internet amount
 * Min Wang					defect 3759.
 * M Listberger 07/23/2002  Corrected alignment of decimals when
 *                          the amount, including special
 * 							characters is 12 characters is
 * 							length.  Changed length of dashes
 * 							and amount length to accomodate
 *                          up to twelve numbers and characters.
 *                          -999,999.00 to 9,999,999.00
 *                          Correct defect CQU100004501.
 * B Arredondo	02/04/2003	Defect 4625. Modified length size of
 * 							columns from 12 to 11 in method
 * 							printLine() so decimals can line up.
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/26/2005	Do not reference closeoutbegtstmp when
 * 							reporting by date range. 
 * 							modify createHeader()
 * 							defect 7896 Ver 5.2.3 
 * K Harrell	06/03/2009	Use getClockTimeNoMs(), Implement 
 * 							FundsConstant.  Delete unused constants.
 * 							Convert constants from public to private
 * 							 as appropriate.  
 * 							delete GRAND_TOTAL_STRING, TOTAL_FOR_STRING,
 * 							 SUMMARY_PAGE_NO_TOTAL, SUMMARY_PAGE_TOTAL,
 * 							 START_PT_8, START_PT_71, LENGTH_8, 
 * 							 LENGTH_10, LENGTH_15, START_PT_90
 * 							modify createHeader(), formatReport(), 
 * 							 buildData() 
 * 							defect 9943 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Fees Report.
 *
 * @version	Defect_POS_F	08/10/2009
 * @author	Min Wang
 * <br>Creation Date:		09/09/2001 07:35:55
 */
public class GenFeeReport extends ReportTemplate
{

	// boolean	 
	private boolean cbGenSumPage = false;
	private boolean cbNewPageDone = false; // desc on new page

	// int 
	private int k = 0;

	// Vector 
	public Vector cvFeesStatVec = new Vector();

	// Object 
	public ReportStatusData caFeesStatus = new ReportStatusData();

	private static final boolean NOTOTALS = false;
	private static final boolean SHOWTOTALS = true;
	private static final int AMT_QTY_LENGTH = 12;
	private static final int AMT_QTY_START_PT_107 = 103;
	private static final int AMT_QTY_START_PT_121 = 121;
	private static final int AMT_QTY_START_PT_50 = 50;
	private static final int AMT_QTY_START_PT_67 = 67;
	private static final int AMT_QTY_START_PT_75 = 75;
	private static final int AMT_QTY_START_PT_87 = 85;
	private static final int CUST_LENGTH = 8;
	private static final int CUST_START_PT = 52;
	private static final int DEALER_LENGTH = 12;
	private static final int DEALER_START_PT = 85;
	private static final int EMPTY_WSID = -1;
	private static final int END_OF_PAGE_WHITE_SPACE = 6;
	private static final int INTNET_LENGTH = 8;
	private static final int INTNET_START_PT = 105;
	private static final int LENGTH_11 = 11;
	private static final int LENGTH_12 = 12;
	private static final int LENGTH_30 = 30;
	private static final int LENGTH_4 = 4;
	private static final int LENGTH_5 = 5;
	private static final int LINES_1 = 1;
	private static final int LINES_2 = 2;
	private static final int START_PT_1 = 1;
	private static final int START_PT_100 = 100;
	private static final int START_PT_112 = 112;
	private static final int START_PT_118 = 118;
	private static final int START_PT_129 = 129;
	private static final int START_PT_15 = 15;
	private static final int START_PT_2 = 2;
	private static final int START_PT_20 = 20;
	private static final int START_PT_38 = 38;
	private static final int START_PT_46 = 46;
	private static final int START_PT_57 = 57;
	private static final int START_PT_63 = 63;
	private static final int START_PT_70 = 70;
	private static final int START_PT_75 = 75;
	private static final int START_PT_79 = 79;
	private static final int START_PT_81 = 81;
	private static final int START_PT_84 = 84;
	private static final int START_PT_93 = 93;
	private static final int SUBCON_LENGTH = 13;
	private static final int SUBCON_START_PT = 67;
	private static final int SUMMARY_LENGTH = 40;
	private static final int SUMMARY_STARTPT = 1;
	private static final int TOTAL_LENGTH = 5;
	private static final int TOTAL_START_PT = 125;

	// Public ??
	public static final String TRANSACTION_HEADER_STRING =
		"TRANSACTIONS FROM";

	private static final String AMT_QTY_TBL_HEADER2 = "AMOUNT   QTY";
	private static final String BLANK_SPACE = " ";
	private static final String CASHDRAWER_HEADER_STRING =
		"CASH DRAWER ";
	private static final String CASHDRAWER_SUMMARY_STRING =
		"SUMMARY FOR CASH DRAWER ";
	private static final String CASHDRAWER_TOTAL_STRING =
		"TOTAL FOR CASH DRAWER  ";
	private static final String CLOSE_AFTER_SUBSTA_SUM =
		"CLOSEOUTS AFTER SUBSTATION SUMMARY";
	private static final String COLON_STRING = ":";
	private static final String COUNTYWIDE_TOTAL_STRING =
		"GRAND TOTALS";
	private static final String CUST_TBL_HEADER1 = "CUSTOMER";
	private static final String DATE_RANGE = "DATE RANGE";
	private static final String DEALER_TBL_HEADER1 = "DEALER TITLE";
	private static final String DESC_TBL_HEADER2 = "DESCRIPTION";
	private static final String EMPLOYEE_HEADER_STRING = "EMPLOYEE";
	private static final String EMPLOYEE_SUMMARY_STRING =
		"SUMMARY FOR EMPLOYEE ";
	private static final String EMPLOYEE_TOTAL_STRING =
		"TOTAL FOR EMPLOYEE ";
	private static final String FOR_CLOSE = "FOR LAST CLOSEOUT";
	private static final String INTNET_TBL_HEADER1 = "INTERNET";
	private static final String LAST_CLOSEOUT = "SINCE LAST CLOSEOUT";
	private static final String LAST_CURRENT_STATUS =
		"SINCE LAST CURRENT STATUS";
	private static final String NOT_DEFINED = "REPORT TYPE NOT DEFINED";
	private static final String REPORT_DATE_STRING = "REPORT DATE ";
	private static final String REPORT_SUMMARY_STRING =
		"SUMMARY FOR REPORT:";
	private static final String REPORT_TOTAL_STRING = "REPORT TOTAL:";
	private static final String REPORT_TYPE_STRING = "REPORT TYPE";
	private static final String SUBCON_TBL_HEADER1 = "SUBCONTRACTOR";
	private static final String SUBSTATION_SUMMARY_STRING =
		"SUMMARY FOR SUBSTATION ";
	private static final String SUBSTATION_TOTAL_STRING =
		"SUBSTATION TOTALS";
	private static final String SUMMARY_EMPLOYEE_STRING =
		"SUMMARY FOR EMPLOYEE ";
	private static final String SUMMARY_WORKSTATION_STRING =
		"SUMMARY FOR CASH DRAWER ";
	public static final String THROUGH_HEADER_STRING = "THROUGH";
	private static final String TOTAL_STRING = "TOTAL ";
	private static final String TOTAL_TBL_HEADER1 = "TOTAL";

	// defect 9943 
	// Not Used 
	//	public static final String GRAND_TOTAL_STRING = "GRAND TOTALS";
	//	public static final String TOTAL_FOR_STRING = "TOTAL FOR ";			
	//	public static final int SUMMARY_PAGE_NO_TOTAL = 0;
	//	public static final int SUMMARY_PAGE_TOTAL = 1;
	//	public static final int START_PT_8 = 8;
	//	public static final int START_PT_71 = 71;
	//	public static final int LENGTH_8 = 8;
	//	public static final int LENGTH_10 = 10;
	//	public static final int LENGTH_15 = 15;
	//	public static final int START_PT_90 = 90;
	// end defect 9943 

	/**
	 * Starts the application in standalone mode.
	 * This is done to test the report.
	 *
	 * @param aarrArgs String[] of command-line arguments
	 *
	 * <p>args[0] is for ReportType.  This defines the style of report
	 * requested. 1 means to break out the Payment Type (Customer,
	 * Subcon, Dealer). 0 means there will not be a break out by Payment
	 * Type.  There is just a total of the money and quantity by Item.
	 * This will be replaced by another method of aqcuiring this flag
	 * later.
	 *
	 * <p>args[1] is for Entity Break.
	 *
	 * <p>args[2] is for Primary Split.
	 *
	 * This will be replaced by another method of aqcuiring this flag
	 *  later.
	 */
	public static void main(String[] aarrArgs)
	{
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5241");

		GenFeeReport laGenRpt =
			new GenFeeReport("FEE REPORT", laRptProps);

		// instantiate the FundsReportData to pass formatting
		// information to the formatReport method.
		FundsReportData laFundsReportData = new FundsReportData();
		laFundsReportData.setEntity(FundsConstant.CASH_DRAWER);
		laFundsReportData.setPrimarySplit(FundsConstant.NONE);
		laFundsReportData.setShowSourceMoney(false);
		laFundsReportData.setRange(FundsConstant.SINCE_CLOSE);
		laFundsReportData.setFromRange(new RTSDate());
		laFundsReportData.setToRange(new RTSDate());

		if (aarrArgs.length > 0)
			// args[0] contains report type.  0 is simple format.
			// 1 is Payment Breakout.  Multiple columns.
		{
			if (Integer.parseInt(aarrArgs[0]) != 0)
			{
				laFundsReportData.setShowSourceMoney(true);
			}
			else
			{
				laFundsReportData.setShowSourceMoney(false);
			}
		}
		if (aarrArgs.length > 1)
			// args[1] contains indicator for Level 1 Break. 
			// 50 is Workstation Id Break.
			// 51 is Employee Break.
			// 52 is Substation Summary
			// 59 is County Wide
		{
			laFundsReportData.setEntity(Integer.parseInt(aarrArgs[1]));
		}

		if (aarrArgs.length > 2)
			// args[2] contains indicator for Level 2 Break.
			// 53 has Primary Split Break off
			// 50 is Workstation Id Break.
			// 51 is Employee Break.
		{
			laFundsReportData.setPrimarySplit(
				Integer.parseInt(aarrArgs[2]));
		}

		// instantiate the FundsData to pass FundsReportData to
		// the formatReport method.
		FundsData laFundsData = new FundsData();
		laFundsData.setFundsReportData(laFundsReportData);
		laFundsData.setOfficeIssuanceNo(170);
		laFundsData.setSubStationId(0);
		laFundsData.setCashWsId(115);

		// set up the cashdrawers vector in FundsData
		Vector lvCashDrwrs = new Vector();
		CashWorkstationCloseOutData laCashDrwr1 =
			new CashWorkstationCloseOutData();
		laCashDrwr1.setCashWsId(0);
		lvCashDrwrs.addElement(laCashDrwr1);
		CashWorkstationCloseOutData laCashDrwr2 =
			new CashWorkstationCloseOutData();
		laCashDrwr2.setCashWsId(100);
		lvCashDrwrs.addElement(laCashDrwr2);
		CashWorkstationCloseOutData laCashDrwr3 =
			new CashWorkstationCloseOutData();
		laCashDrwr3.setCashWsId(200);
		lvCashDrwrs.addElement(laCashDrwr3);
		laFundsData.setCashDrawers(lvCashDrwrs);

		// set up the Employees vector in FundsData
		Vector lvEmployees = new Vector();
		EmployeeData laEmployee1 = new EmployeeData();
		laEmployee1.setEmployeeId("MINWANG");
		lvEmployees.addElement(laEmployee1);
		EmployeeData laEmployee2 = new EmployeeData();
		laEmployee2.setEmployeeId("LISAQIN");
		lvEmployees.addElement(laEmployee2);
		laFundsData.setEmployees(lvEmployees);

		// This is supposed to be the query to run.  For now just doing
		// a fake result.  So the query string is not important yet.
		String lsQuery = "Select * FROM RTS_TBL";

		Vector lvQueryResults = laGenRpt.queryData(lsQuery);

		// call the report formating method.  This is where the real 
		// work of this class happens. lvQueryResults is the vector
		// containing the results of the query. FundsData contains
		// information on primary and secondary breaks. There is also an
		// indicator as to which column type to print.
		laGenRpt.formatReport(lvQueryResults, laFundsData);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\WKS#FEE.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGenRpt.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\WKS#FEE.RPT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisible(true);

			// One way to Print Report.
			//Process p = Runtime.getRuntime().exec("cmd.exe /c copy
			// c:\\TitlePkgRpt.txt prn");

		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * GenFeeReport constructor
	 */
	public GenFeeReport()
	{
		super();
	}

	/**
	 * GenFeeReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenFeeReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Data is brought in from the database query result vector and 
	 * then built into a TreeMap.  The database query result vector
	 * is of FeeReportTypeData.  This data class does not properly
	 * organize the data.  This method organizes the data into
	 * FeeReportData so that it is better organized for generating the
	 * report. <p>Entity determines if CashWsId or TransEmpId is the
	 * major part of the key.  If Entity is CashWsId, then TransEmpId
	 * comes as the second part of the key.  If Entity is Employee, then
	 * CashWsId comes as the second part of the key.
	 * There is also a default key that does not use either CashWsId or
	 * TransEmpId as part of the key.
	 * 
	 * @param avResults Vector
	 * @param aiEntity int
	 * @return aaSummaryPageData TreeMap
	 */
	public TreeMap buildData(Vector avResults, int aiEntity)
	{
		// This 0 Dollar is used to initialize dollar fields
		Dollar laZeroDollar = new Dollar(0.00);

		// Declare the TreeMap
		TreeMap laDataMap = new TreeMap();

		// declare vector iterator variable
		int i = 0;

		while (i < avResults.size())
		{

			FeeTypeReportData laDataLineIn =
				(FeeTypeReportData) avResults.elementAt(i);

			// declare the key to be used for containkeys, gets,
			// puts, and removes.
			String lsMapKey = "";

			/**
			 * The format of the key will vary depending on Entity Break
			 * Type. There is a default key creation that does not
			 * include either CashWsId or TransEmpId. An Entity type of
			 * Substation uses the default key.
			 * <p>The key is built as follows:
			 * <ol><li>Either CashWsId or TransEmpId is the first part
			 * of the key depending on the Entity Type.
			 * <li>Either CashWsId or TransEmpId depending Primary Split
			 * Type. It is the reverse of the previous level. Need to
			 * check for null? <li>PayableTypeCd.  This is different
			 * than the query order by. But, this is the level three
			 * break. <li>AcctItmGrpCd will control partial order of 
			 * AcctItmCdDesc's.	<li>AcctItmCrdtIndi will provide a
			 * partial order of AcctItmCdDesc's <li>AcctItmCdDesc is the
			 * final part of the key.
			 * </ol> 
			 */

			switch (aiEntity)
			{
				// This key has CashWsId as the major break with
				// TransEmpId as the second break.
				case (FundsConstant.CASH_DRAWER) :
					{
						lsMapKey =
							String.valueOf(laDataLineIn.getCashWsId())
								+ laDataLineIn.getTransEmpId()
								+ laDataLineIn.getPayableTypeCd()
								+ laDataLineIn.getAcctItmGrpCd()
								+ laDataLineIn.getAcctItmCrdtIndi()
								+ laDataLineIn.getAcctItmCdDesc();
						break;
					}
					// This key has EmpId has the major break with
					// CashWsId as the second Break	
				case (FundsConstant.EMPLOYEE) :
					{
						lsMapKey =
							laDataLineIn.getTransEmpId()
								+ String.valueOf(
									laDataLineIn.getCashWsId())
								+ laDataLineIn.getPayableTypeCd()
								+ laDataLineIn.getAcctItmGrpCd()
								+ laDataLineIn.getAcctItmCrdtIndi()
								+ laDataLineIn.getAcctItmCdDesc();
						break;
					}
					// the default does not consider either CashWsId
					// or EmpId as part of the key.
				default :
					{
						lsMapKey =
							laDataLineIn.getPayableTypeCd()
								+ laDataLineIn.getAcctItmGrpCd()
								+ laDataLineIn.getAcctItmCrdtIndi()
								+ laDataLineIn.getAcctItmCdDesc();
					}
			}

			/** 
			 * Check to see a record line has already been started for
			 * this key combination. If so, update it.  Otherwise, 
			 * insert it. ContainsKey returns a boolean.  True if it is 
			 * there, null(?) if not.
			 */

			if (laDataMap.containsKey(lsMapKey))
			{
				// There is a record.  
				FeeReportData laDataLineOut =
					(FeeReportData) laDataMap.get(lsMapKey);

				// Remove the current record from the map.
				laDataMap.remove(lsMapKey);

				/**
				 * Switch on FeeSourceCd.  This is how we determine
				 * which group the data belongs to.
				 */
				switch (laDataLineIn.getFeeSourceCd())
				{
					// defect 9943 
					// Use FundsConstant for Source Of Money  
					//case (1) :
					case (FundsConstant.FEE_SOURCE_CUST) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laDataLineOut.getCustAcctItmAmt().add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setCustAcctItmQty(
								laDataLineOut.getCustAcctItmQty()
									+ laDataLineIn.getItmQty());
							laDataLineOut.setTotalItmAmt(
								laDataLineOut.getTotalItmAmt().add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setTotalItmQty(
								laDataLineOut.getTotalItmQty()
									+ laDataLineIn.getItmQty());
							break;
						}
						//case (2) :
					case (FundsConstant.FEE_SOURCE_SUBCON) :
						{
							laDataLineOut.setSubconAcctItmAmt(
								laDataLineOut
									.getSubconAcctItmAmt()
									.add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setSubconAcctItmQty(
								laDataLineOut.getSubconAcctItmQty()
									+ laDataLineIn.getItmQty());
							laDataLineOut.setTotalItmAmt(
								laDataLineOut.getTotalItmAmt().add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setTotalItmQty(
								laDataLineOut.getTotalItmQty()
									+ laDataLineIn.getItmQty());
							break;
						}
						//case (3) :
					case (FundsConstant.FEE_SOURCE_DEALER) :
						{
							laDataLineOut.setDealerAcctItmAmt(
								laDataLineOut
									.getDealerAcctItmAmt()
									.add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setDealerAcctItmQty(
								laDataLineOut.getDealerAcctItmQty()
									+ laDataLineIn.getItmQty());
							laDataLineOut.setTotalItmAmt(
								laDataLineOut.getTotalItmAmt().add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setTotalItmQty(
								laDataLineOut.getTotalItmQty()
									+ laDataLineIn.getItmQty());
							break;
						}
						//case (5) :
					case (FundsConstant.FEE_SOURCE_INTERNET) :
						{
							laDataLineOut.setInternetAcctItmAmt(
								laDataLineOut
									.getInternetAcctItmAmt()
									.add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setInternetAcctItmQty(
								laDataLineOut.getInternetAcctItmQty()
									+ laDataLineIn.getItmQty());
							laDataLineOut.setTotalItmAmt(
								laDataLineOut.getTotalItmAmt().add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setTotalItmQty(
								laDataLineOut.getTotalItmQty()
									+ laDataLineIn.getItmQty());
							break;
						}
						//case (0) :
					case (FundsConstant.FEE_SOURCE_DO_NOT_SHOW) :
						//Case 0 means that ShowSourceOfMoney = false
						{
							laDataLineOut.setTotalItmAmt(
								laDataLineOut.getTotalItmAmt().add(
									laDataLineIn.getItmPrice()));
							laDataLineOut.setTotalItmQty(
								laDataLineOut.getTotalItmQty()
									+ laDataLineIn.getItmQty());
							break;
							/**
							 * There should not be any other choice, but
							 * allow for the fact that there are other
							 * FeeSourceCds.
							 */
						}
					default :
						{
							// empty code block
						}
				} // end switch

				// Always add to the Total.
				laDataMap.put(lsMapKey, laDataLineOut);
			} // end if

			// There is not an existing record, so create and insert one
			else
			{
				FeeReportData laDataLineOut = new FeeReportData();
				laDataLineOut.setCashWsId(laDataLineIn.getCashWsId());
				laDataLineOut.setTransEmpId(
					laDataLineIn.getTransEmpId());
				laDataLineOut.setPayableTypeCd(
					laDataLineIn.getPayableTypeCd());
				laDataLineOut.setPayableTypeCdDesc(
					laDataLineIn.getPayableTypeCdDesc());
				laDataLineOut.setAcctItmCdDesc(
					laDataLineIn.getAcctItmCdDesc());
				laDataLineOut.setAcctItmCrdtIndi(
					laDataLineIn.getAcctItmCrdtIndi());
				laDataLineOut.setAcctItmGrpCd(
					laDataLineIn.getAcctItmGrpCd());
				laDataLineOut.setFeeSourceCd(
					laDataLineIn.getFeeSourceCd());

				/**
				 * Switch on FeeSourceCd.  This is how we determine
				 * which group the data belongs to.
				 */
				switch (laDataLineIn.getFeeSourceCd())
				{
					//case (1) :
					case (FundsConstant.FEE_SOURCE_CUST) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setCustAcctItmQty(
								laDataLineIn.getItmQty());
							laDataLineOut.setSubconAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setTotalItmQty(
								laDataLineIn.getItmQty());
							break;
						}
						//case (2) :
					case (FundsConstant.FEE_SOURCE_SUBCON) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setSubconAcctItmQty(
								laDataLineIn.getItmQty());
							laDataLineOut.setDealerAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setTotalItmQty(
								laDataLineIn.getItmQty());
							break;
						}
						//case (3) :
					case (FundsConstant.FEE_SOURCE_DEALER) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setDealerAcctItmQty(
								laDataLineIn.getItmQty());
							laDataLineOut.setInternetAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setTotalItmQty(
								laDataLineIn.getItmQty());
							break;
						}
						//case (5) :
					case (FundsConstant.FEE_SOURCE_INTERNET) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setInternetAcctItmQty(
								laDataLineIn.getItmQty());
							laDataLineOut.setTotalItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setTotalItmQty(
								laDataLineIn.getItmQty());
							break;
						}
						//case (0) :
					case (FundsConstant.FEE_SOURCE_DO_NOT_SHOW) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalItmAmt(
								laDataLineIn.getItmPrice());
							laDataLineOut.setTotalItmQty(
								laDataLineIn.getItmQty());
							break;
						}
						// end defect 9943 
					default :
						{
							laDataLineOut.setCustAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laZeroDollar);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalItmAmt(laZeroDollar);
							laDataLineOut.setTotalItmQty(0);
						}
				} // end switch
				laDataMap.put(lsMapKey, laDataLineOut);
			} // end else

			// increment the vector iterator
			i = i + 1;
		} // end while loop
		return laDataMap;
	}

	/**
	 * Create the Report's Header.
	 * 
	 * @param asEntityId String - Used to build the header
	 * @param aaFundsData FundsData	- Contains the object containing
	 *  format information.
	 * @return Vector
	 */
	public Vector createHeader(
		String asEntityId,
		FundsData aaFundsData)
	{

		FundsReportData laFundsReportData =
			(FundsReportData) aaFundsData.getFundsReportData();
		Vector lvHeader = new Vector();
		String lsFromRange = null;
		String lsToRange = null;

		switch (laFundsReportData.getEntity())
		{
			// put in the cash drawer header 
			case (FundsConstant.CASH_DRAWER) :
				{
					lvHeader.addElement(CASHDRAWER_HEADER_STRING);
					lvHeader.addElement(asEntityId);
					Vector lvSelectedCashDrawers =
						(Vector) aaFundsData.getSelectedCashDrawers();

					if (laFundsReportData.getRange()
						!= FundsConstant.DATE_RANGE)
					{
						int i = 0;
						while (i < lvSelectedCashDrawers.size())
						{
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvSelectedCashDrawers
										.get(
									i);
							String lsCashWsId =
								Integer.toString(
									laDrawer.getCashWsId());

							if (lsCashWsId.equals(asEntityId))
							{
								// defect 9943 
								// Use getClockTimeNoMs()
								lsFromRange =
									laDrawer
										.getCloseOutBegTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutBegTstmp()
											.getClockTimeNoMs();
								//			.getClockTime()
								//			.substring(0,8);
								lsToRange =
									laDrawer
										.getCloseOutEndTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutEndTstmp()
											.getClockTimeNoMs();
								//			.getClockTime()
								//			.substring(0,8);
								// end defect 9943 
								i = lvSelectedCashDrawers.size();
							}
							else
								i++;
						}
					}

					for (int l = 0;
						l < aaFundsData.getSelectedCashDrawers().size();
						l++)
					{
						CashWorkstationCloseOutData laCashDrwrObj =
							(CashWorkstationCloseOutData) aaFundsData
								.getSelectedCashDrawers()
								.elementAt(l);
						int liCashDrwr = laCashDrwrObj.getCashWsId();
						if (asEntityId
							.equals(Integer.toString(liCashDrwr)))
						{
							// defect 9943 
							laCashDrwrObj.setRptStatus(
							//"Report Generated");
							FundsConstant.REPORT_GENERATED);
							// end defect 9943  
							break;
						}
					}
					break;
				}
				// put in the employee header
			case (FundsConstant.EMPLOYEE) :
				{
					lvHeader.addElement(EMPLOYEE_HEADER_STRING);
					lvHeader.addElement(asEntityId);

					for (int l = 0;
						l < aaFundsData.getSelectedEmployees().size();
						l++)
					{
						EmployeeData laEmployeeObj =
							(EmployeeData) aaFundsData
								.getSelectedEmployees()
								.elementAt(
								l);
						String lsEmployeeId =
							laEmployeeObj.getEmployeeId();
						if (asEntityId.equals(lsEmployeeId))
						{
							// defect 9943
							laEmployeeObj.setRptStatus(
							//"Report Generated");
							FundsConstant.REPORT_GENERATED);
							// end defect 9943 
							break;
						}
						//else lEmployeeObj.setRptStatus("");
					}
					break;

					// do not add this section to the header
				}
			default :
				{
					// empty code block
				}
		}
		// handle adding Report Type to header
		// do not add if Substation Summary or CountyWide
		if (laFundsReportData.getEntity() != FundsConstant.SUBSTATION
			&& laFundsReportData.getEntity() != FundsConstant.COUNTYWIDE)
		{
			lvHeader.addElement(REPORT_TYPE_STRING);

			// Determine the report type
			switch (laFundsReportData.getRange())
			{
				case (FundsConstant.AFTER_SUBSTATION) :
					{
						lvHeader.addElement(CLOSE_AFTER_SUBSTA_SUM);
						break;
					}
				case (FundsConstant.SINCE_CURRENT) :
					{
						lvHeader.addElement(LAST_CURRENT_STATUS);
						break;
					}
				case (FundsConstant.SINCE_CLOSE) :
					{
						lvHeader.addElement(LAST_CLOSEOUT);
						break;
					}
				case (FundsConstant.LAST_CLOSE) :
					{
						lvHeader.addElement(FOR_CLOSE);
						break;
					}
				case (FundsConstant.DATE_RANGE) :
					{
						lvHeader.addElement(DATE_RANGE);
						// defect 9943 
						// Use getClockTimeNoMs() 
						lsFromRange =
							laFundsReportData.getFromRange().toString()
								+ " "
								+ laFundsReportData
									.getFromRange()
									.getClockTimeNoMs();
						//			.getClockTime()
						//			.substring(0,8);
						lsToRange =
							laFundsReportData.getToRange().toString()
								+ " "
								+ laFundsReportData
									.getToRange()
									.getClockTimeNoMs();
						//			.getClockTime()
						//			.substring(0,8);
						// end defect 9943 
						break;
					}
				default :
					{
						lvHeader.addElement(NOT_DEFINED);
					}
			}

		}
		// If Substation Summary or CountyWide, print Report Date
		// Otherwise, print From - To - Date Range.
		// If not generating summary page, print to and from ranges
		if (laFundsReportData.getRange() == FundsConstant.DATE_RANGE
			|| ((laFundsReportData.getEntity()
				== FundsConstant.CASH_DRAWER)
				&& (!cbGenSumPage)))
			if (laFundsReportData.getEntity()
				!= FundsConstant.SUBSTATION
				|| laFundsReportData.getEntity()
					!= FundsConstant.COUNTYWIDE)
			{
				if (laFundsReportData.getEntity()
					== FundsConstant.CASH_DRAWER
					|| laFundsReportData.getRange()
						== FundsConstant.DATE_RANGE)
				{
					// normal transaction to and from headers	
					lvHeader.addElement(TRANSACTION_HEADER_STRING);
					lvHeader.addElement(
						lsFromRange + " THROUGH " + lsToRange);
				}
			}
			else
			{
				// this adds the batch run date to the header	
				lvHeader.addElement(REPORT_DATE_STRING);
				// ******not real string.  needs approval.**********
				lvHeader.addElement(
					laFundsReportData.getFromRange().toString());
			}
		return lvHeader;
	}

	/**
	 * This method creates a summary section based on data passed in a
	 * TreeMap.
	 * 
	 * @param abColumnLayout boolean - which type of report to print
	 * @param abPrintTotal boolean - print total switch 
	 * @param aaSummaryData TreeMap
	 * @param lvHeader Vector - print headings on new page
	 * @param lvTable Vector - print column headings on new page
	 */
	public void createSummary(
		boolean abColumnLayout,
		boolean abPrintTotal,
		TreeMap aaSummaryData,
		Vector avHeader,
		Vector avTable)
	{
		// this object provides for the summaries total line
		FeeReportData laSummaryTotalLine = new FeeReportData();
		Dollar laZeroDollar = new Dollar(0.00);
		laSummaryTotalLine.setTotalItmAmt(laZeroDollar);
		laSummaryTotalLine.setCustAcctItmAmt(laZeroDollar);
		laSummaryTotalLine.setDealerAcctItmAmt(laZeroDollar);
		laSummaryTotalLine.setSubconAcctItmAmt(laZeroDollar);
		laSummaryTotalLine.setInternetAcctItmAmt(laZeroDollar);

		// this object provides for Payable Type Cd total lines
		FeeReportData laSummarySubTotalLine = new FeeReportData();
		laSummarySubTotalLine.setTotalItmAmt(laZeroDollar);
		laSummarySubTotalLine.setCustAcctItmAmt(laZeroDollar);
		laSummarySubTotalLine.setDealerAcctItmAmt(laZeroDollar);
		laSummarySubTotalLine.setSubconAcctItmAmt(laZeroDollar);
		laSummarySubTotalLine.setInternetAcctItmAmt(laZeroDollar);

		// this object will contain the individual "detail" lines of this section
		FeeReportData laSummaryDataLine = new FeeReportData();
		laSummaryDataLine.setTotalItmAmt(laZeroDollar);
		laSummaryDataLine.setCustAcctItmAmt(laZeroDollar);
		laSummaryDataLine.setDealerAcctItmAmt(laZeroDollar);
		laSummaryDataLine.setSubconAcctItmAmt(laZeroDollar);
		laSummaryDataLine.setInternetAcctItmAmt(laZeroDollar);

		String lsPrePayableTypeCdDesc = "";
		// to provide Payable Type Code Breaks

		// Iteration will provide for sequential access to the data.
		// Must make a collection of values that will then be
		// provided to the Iterator.  TreeMap ensures data is sorted.
		Collection laDataCollection = aaSummaryData.values();
		Iterator laDataList = laDataCollection.iterator();

		// print the report summary string
		if (abPrintTotal)
		{
			this.caRpt.print(
				REPORT_SUMMARY_STRING,
				SUMMARY_STARTPT,
				SUMMARY_LENGTH);
			this.caRpt.blankLines(LINES_1);
		}

		// loop through the TreeMap as long as there are elements 
		// to process
		while (laDataList.hasNext())
		{

			// make sure paging will occur if needed
			if (needsPageBreak())
			{
				takeAPageBreak(avHeader, avTable); // desc on new page
			}

			// nextElement retrieves the next element in the list.
			// the object is cast into FeeReportData since that is how
			// it was added to the TreeMap.
			laSummaryDataLine = (FeeReportData) laDataList.next();

			// when PayableTypeCd changes, take a break.
			if (!laSummaryDataLine
				.getPayableTypeCdDesc()
				.equals(lsPrePayableTypeCdDesc))
			{
				// if lsPrePayableTypeCdDesc is an empty string, don't
				// have any totals yet.  So , do not print the total
				// line for the break.
				if (!lsPrePayableTypeCdDesc.equals(""))
				{
					// print the total line for the last item code
					laSummarySubTotalLine.setAcctItmCdDesc(
						TOTAL_STRING + lsPrePayableTypeCdDesc);
					generateTotal(
						abColumnLayout,
						this.caRpt.singleDashes(LENGTH_11),
						laSummarySubTotalLine);
					// make sure paging will occur if needed
				}
				caRpt.nextLine();
				// Print PayableTypeCdDesc on the first dataline
				printPayableTypeCdDesc(
					abColumnLayout,
					laSummaryDataLine.getPayableTypeCdDesc());
				// desc on new page
				// set the prePayableTypeCdDesc to current PayableTyepCdDesc for future breaks
				lsPrePayableTypeCdDesc =
					laSummaryDataLine.getPayableTypeCdDesc();
			}
			printLine(
				abColumnLayout,
				NOTOTALS,
				laSummaryDataLine,
				true);
			// close out this line
			this.caRpt.nextLine();

			// add up the subtotals
			laSummarySubTotalLine.setTotalItmAmt(
				laSummarySubTotalLine.getTotalItmAmt().add(
					laSummaryDataLine.getTotalItmAmt()));
			laSummarySubTotalLine.setTotalItmQty(
				laSummarySubTotalLine.getTotalItmQty()
					+ (laSummaryDataLine.getTotalItmQty()));
			laSummarySubTotalLine.setCustAcctItmAmt(
				laSummarySubTotalLine.getCustAcctItmAmt().add(
					laSummaryDataLine.getCustAcctItmAmt()));
			laSummarySubTotalLine.setCustAcctItmQty(
				laSummarySubTotalLine.getCustAcctItmQty()
					+ (laSummaryDataLine.getCustAcctItmQty()));
			laSummarySubTotalLine.setDealerAcctItmAmt(
				laSummarySubTotalLine.getDealerAcctItmAmt().add(
					laSummaryDataLine.getDealerAcctItmAmt()));
			laSummarySubTotalLine.setDealerAcctItmQty(
				laSummarySubTotalLine.getDealerAcctItmQty()
					+ (laSummaryDataLine.getDealerAcctItmQty()));
			laSummarySubTotalLine.setSubconAcctItmAmt(
				laSummarySubTotalLine.getSubconAcctItmAmt().add(
					laSummaryDataLine.getSubconAcctItmAmt()));
			laSummarySubTotalLine.setSubconAcctItmQty(
				laSummarySubTotalLine.getSubconAcctItmQty()
					+ (laSummaryDataLine.getSubconAcctItmQty()));

			laSummarySubTotalLine.setInternetAcctItmAmt(
				laSummarySubTotalLine.getInternetAcctItmAmt().add(
					laSummaryDataLine.getInternetAcctItmAmt()));
			laSummarySubTotalLine.setInternetAcctItmQty(
				laSummarySubTotalLine.getInternetAcctItmQty()
					+ (laSummaryDataLine.getInternetAcctItmQty()));

			// Add up the final totals
			laSummaryTotalLine.setTotalItmAmt(
				laSummaryTotalLine.getTotalItmAmt().add(
					laSummaryDataLine.getTotalItmAmt()));
			laSummaryTotalLine.setTotalItmQty(
				laSummaryTotalLine.getTotalItmQty()
					+ (laSummaryDataLine.getTotalItmQty()));

			laSummaryTotalLine.setCustAcctItmAmt(
				laSummaryTotalLine.getCustAcctItmAmt().add(
					laSummaryDataLine.getCustAcctItmAmt()));
			laSummaryTotalLine.setCustAcctItmQty(
				laSummaryTotalLine.getCustAcctItmQty()
					+ (laSummaryDataLine.getCustAcctItmQty()));

			laSummaryTotalLine.setDealerAcctItmAmt(
				laSummaryTotalLine.getDealerAcctItmAmt().add(
					laSummaryDataLine.getDealerAcctItmAmt()));
			laSummaryTotalLine.setDealerAcctItmQty(
				laSummaryTotalLine.getDealerAcctItmQty()
					+ (laSummaryDataLine.getDealerAcctItmQty()));

			laSummaryTotalLine.setSubconAcctItmAmt(
				laSummaryTotalLine.getSubconAcctItmAmt().add(
					laSummaryDataLine.getSubconAcctItmAmt()));
			laSummaryTotalLine.setSubconAcctItmQty(
				laSummaryTotalLine.getSubconAcctItmQty()
					+ (laSummaryDataLine.getSubconAcctItmQty()));

			laSummaryTotalLine.setInternetAcctItmAmt(
				laSummaryTotalLine.getInternetAcctItmAmt().add(
					laSummaryDataLine.getInternetAcctItmAmt()));
			laSummaryTotalLine.setInternetAcctItmQty(
				laSummaryTotalLine.getInternetAcctItmQty()
					+ (laSummaryDataLine.getInternetAcctItmQty()));

		}
		// print the total line for the last PayableTypeCd
		laSummarySubTotalLine.setAcctItmCdDesc(
			TOTAL_STRING + lsPrePayableTypeCdDesc);
		generateTotal(
			abColumnLayout,
			this.caRpt.singleDashes(LENGTH_11),
			laSummarySubTotalLine);

		if (abPrintTotal)
		{
			// print the total line summarizing the summary page.
			laSummaryTotalLine.setAcctItmCdDesc(REPORT_TOTAL_STRING);
			generateTotal(
				abColumnLayout,
				this.caRpt.doubleDashes(LENGTH_11),
				laSummaryTotalLine);
		}
	}

	/**
	 * Create the Report's Column Headings.
	 * 
	 * @param abShowSourceMoney boolean
	 * @return Vector
	 */
	public Vector createTableHeaders(boolean abShowSourceMoney)
	{
		Vector lvTable = new Vector();
		if (abShowSourceMoney) // print payment type break out columns
		{
			// This vector contains the first of two rows of column headings
			Vector lvRow1 = new Vector();
			ColumnHeader laColumnA1 =
				new ColumnHeader(
					CUST_TBL_HEADER1,
					CUST_START_PT,
					CUST_LENGTH);
			ColumnHeader laColumnA2 =
				new ColumnHeader(
					SUBCON_TBL_HEADER1,
					SUBCON_START_PT,
					SUBCON_LENGTH);
			ColumnHeader laColumnA3 =
				new ColumnHeader(
					DEALER_TBL_HEADER1,
					DEALER_START_PT,
					DEALER_LENGTH);
			ColumnHeader laColumnA4 =
				new ColumnHeader(
					INTNET_TBL_HEADER1,
					INTNET_START_PT,
					INTNET_LENGTH);
			ColumnHeader laColumnA5 =
				new ColumnHeader(
					TOTAL_TBL_HEADER1,
					TOTAL_START_PT,
					TOTAL_LENGTH);

			lvRow1.addElement(laColumnA1);
			lvRow1.addElement(laColumnA2);
			lvRow1.addElement(laColumnA3);
			lvRow1.addElement(laColumnA4);
			lvRow1.addElement(laColumnA5);
			lvTable.addElement(lvRow1);
			//Adding ColumnHeader Information

			// this vector contains the second of two rows of column headings 
			Vector lvRow2 = new Vector();
			ColumnHeader laColumnB1 =
				new ColumnHeader(
					DESC_TBL_HEADER2,
					START_PT_1,
					LENGTH_11);
			ColumnHeader laColumnB2 =
				new ColumnHeader(
					AMT_QTY_TBL_HEADER2,
					AMT_QTY_START_PT_50,
					AMT_QTY_LENGTH);
			ColumnHeader laColumnB3 =
				new ColumnHeader(
					AMT_QTY_TBL_HEADER2,
					AMT_QTY_START_PT_67,
					AMT_QTY_LENGTH);
			ColumnHeader laColumnB4 =
				new ColumnHeader(
					AMT_QTY_TBL_HEADER2,
					AMT_QTY_START_PT_87,
					AMT_QTY_LENGTH);
			ColumnHeader laColumnB5 =
				new ColumnHeader(
					AMT_QTY_TBL_HEADER2,
					AMT_QTY_START_PT_107,
					AMT_QTY_LENGTH);
			ColumnHeader laColumnB6 =
				new ColumnHeader(
					AMT_QTY_TBL_HEADER2,
					AMT_QTY_START_PT_121,
					AMT_QTY_LENGTH);
			lvRow2.addElement(laColumnB1);
			lvRow2.addElement(laColumnB2);
			lvRow2.addElement(laColumnB3);
			lvRow2.addElement(laColumnB4);
			lvRow2.addElement(laColumnB5);
			lvRow2.addElement(laColumnB6);
			lvTable.addElement(lvRow2);
			//Adding ColumnHeader Information
		}
		else // simple column layout
			{
			// This vector contains the first of two rows of column
			// headings
			Vector lvRow1 = new Vector();
			ColumnHeader laColumnA1 =
				new ColumnHeader(TOTAL_STRING, START_PT_79, LENGTH_5);
			lvRow1.addElement(laColumnA1);
			lvTable.addElement(lvRow1);
			//Adding ColumnHeader Information

			// this vector contains the second of two rows of column
			// headings
			Vector lvRow2 = new Vector();
			ColumnHeader laColumnB1 =
				new ColumnHeader(
					DESC_TBL_HEADER2,
					START_PT_1,
					LENGTH_12);
			ColumnHeader laColumnB2 =
				new ColumnHeader(
					AMT_QTY_TBL_HEADER2,
					AMT_QTY_START_PT_75,
					AMT_QTY_LENGTH);
			lvRow2.addElement(laColumnB1);
			lvRow2.addElement(laColumnB2);
			lvTable.addElement(lvRow2);
			//Adding ColumnHeader Information
		}

		return lvTable;
	}

	/**
	 * This version of the formatReport method is required by the
	 * Template. It does nothing!
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * This is the formatReport method.  
	 * It does the actual report build.
	 * 
	 * <p>It is the method called from the Funds Business Servlet.
	 *
	 * <p>This class does not use the data object provided by the
	 * database directly. It instead uses a data object that
	 * consolidates the data into a usable format. It uses a TreeMap to 
	 * store and sort this data. An Iterator is used to get the objects 
	 * back out in sequential order.
	 * 
	 * <p>FundsData is passed to this method.
	 * FundsReportData comes from FundsData.
	 * FundsReportData contains indicators on Entity and Primary Split
	 * types as well as a column layout indicator.
	 *
	 * <ol><li>ColumnLayout determines if the data is broken out into
	 * payment source or not.  Payment Source shows summary information 
	 * by Customer, Dealer, and Subcontrator as well as a Total Summary.
	 * Simple column layout just shows the Total Summary.
	 * <li>Entity Break determines what kind of major break to take.
	 * It can be by Workstation, Employee, or Substation Summary.  
	 * This break will force all other breaks.
	 * The Entity can not be the same as Major Split.  (ie.Entity can
	 * not be Workstation if Primary Split is Workstation and vice
	 * versa.) <li>Primary Split Break determines if there is a middle
	 * level break to take. There can be a Primary Split of None,
	 * Workstation, or Employee. Primary Split can not match the Entity.
	 * (ie. Entity and Primary Split can not both be Workstation or
	 * Employee Id) This break also forces the Payable Type Code Break.
	 * <li>There is a low level break on Payable Type Code.
	 * </ol>
	 * <p>A TreeMap is used to store up data to be shown in Summaries.
	 *
	 * @param avResults Vector
	 * @param aaFundsData FundsData
	 */
	public void formatReport(Vector avResults, FundsData aaFundsData)
	{

		// this separates the FundsReportData out of the FundsData
		FundsReportData laFundsReportData =
			(FundsReportData) aaFundsData.getFundsReportData();

		Vector lvHeader = new Vector();
		Dollar laZeroDollar = new Dollar(0.00);

		// This vector will contain the information for the column
		// headings
		Vector lvTable =
			createTableHeaders(laFundsReportData.isShowSourceMoney());

		/**
		 * PayableTypeCd Break Totals Storage object.
		 * It is of FeeReportData type.
		 * Dollar fields are initialized to use the add function.
		 */
		FeeReportData laPayableTypeCdTotals = new FeeReportData();
		laPayableTypeCdTotals.setTotalItmAmt(laZeroDollar);
		laPayableTypeCdTotals.setCustAcctItmAmt(laZeroDollar);
		laPayableTypeCdTotals.setDealerAcctItmAmt(laZeroDollar);
		laPayableTypeCdTotals.setSubconAcctItmAmt(laZeroDollar);
		laPayableTypeCdTotals.setInternetAcctItmAmt(laZeroDollar);

		/**
		 * Primary Split Break Totals Storage object.
		 * It is of FeeReportData type.
		 * Dollar fields are initialized to use the add function.
		 */
		FeeReportData laPrimarySplitTotals = new FeeReportData();
		laPrimarySplitTotals.setTotalItmAmt(laZeroDollar);
		laPrimarySplitTotals.setCustAcctItmAmt(laZeroDollar);
		laPrimarySplitTotals.setDealerAcctItmAmt(laZeroDollar);
		laPrimarySplitTotals.setSubconAcctItmAmt(laZeroDollar);
		laPrimarySplitTotals.setInternetAcctItmAmt(laZeroDollar);

		/**
		 * Entity Break Totals Storage object.
		 * It is of FeeReportData type.
		 * Dollar fields are initialized to use the add function.
		 */
		FeeReportData laEntityTotals = new FeeReportData();
		laEntityTotals.setTotalItmAmt(laZeroDollar);
		laEntityTotals.setCustAcctItmAmt(laZeroDollar);
		laEntityTotals.setDealerAcctItmAmt(laZeroDollar);
		laEntityTotals.setSubconAcctItmAmt(laZeroDollar);
		laEntityTotals.setInternetAcctItmAmt(laZeroDollar);

		/**
		 * PrimarySplit Summary Data.  This TreeMap.
		 * Used to print the totals line.
		 */
		TreeMap laPrimarySplitSummaryData = new TreeMap();

		/**
		 * Entity Summary Data.  This TreeMap
		 * Used to print the totals line.
		 */
		TreeMap laEntitySummaryData = new TreeMap();

		/**
		 * Entity Break Count.  This is used to determine if a summary
		 * page is to be printed.  If the count is greated than one, a
		 * summary page is printed.
		 */
		int liEntityCount = 0;

		/**
		 * PrimarySplit Break Count.  This is used to determine if a
		 * summary section is to be printed.  If the count is greated
		 * than one, a summary section is printed.
		 */
		int liPrimarySplitCount = 0;

		// This string will hold the list of Ids to show on the Summary
		// Page
		String lsEntityIdList = "";

		// This string will be send to createHeader
		// Also used for end of detail data processing.
		String lsEntityID = "";

		// used for end of detail data processing
		String lsPrePayableTypeCdDesc = "";

		caFeesStatus.setReportName(FundsConstant.FEES_REPORT);

		if (avResults.size() > 0 && avResults != null)
		{
			/**
			 * Use buildData to build up the TreeMap with the formatted
			 * data used to create this report.
			 */
			TreeMap laDataMap =
				buildData(avResults, laFundsReportData.getEntity());

			Collection laDataCollection = laDataMap.values();
			Iterator laDataList = laDataCollection.iterator();

			/**
			 * dataline will contain the current row retrieved from the
			 * TreeMap.  It is of FeeReportData type.
			 * Pre-load with the first record so break values can be set
			 */
			FeeReportData laDataline =
				(FeeReportData) laDataMap.get(laDataMap.firstKey());

			// This int contains the compare to field for CashWsId
			// Breaks. It is initialized to -1 to show it does not have
			// a valid wsid. 
			int liPreCashWsId = EMPTY_WSID;

			//This string contains the compare to field for Employee ID
			String lsPreEmpId = "";

			// This string contains the compare to field for
			// PayableTypeCd Description 
			String lsPrePayableTypeCd = "";

			/**
			 * Initialize Entity values.
			 */
			switch (laFundsReportData.getEntity())
			{
				case (FundsConstant.CASH_DRAWER) :
					{
						lsEntityIdList =
							lsEntityIdList
								+ String.valueOf(
									laDataline.getCashWsId())
								+ BLANK_SPACE;
						lsEntityID =
							String.valueOf(laDataline.getCashWsId());
						liPreCashWsId = laDataline.getCashWsId();
						break;
					}
				case (FundsConstant.EMPLOYEE) :
					{
						lsEntityIdList =
							lsEntityIdList
								+ laDataline.getTransEmpId()
								+ BLANK_SPACE;
						lsEntityID = laDataline.getTransEmpId();
						lsPreEmpId = laDataline.getTransEmpId();
						break;
					}
					// no Entity break
				default :
					{
						// empty code block
					}
			}

			/**
			 * liEntityCount has the count of the number of Ids used for
			 * Entity Break processing.  If the count is higher than 1,
			 * a Summary Page will be printed.
			 */
			liEntityCount = liEntityCount + 1;

			// this vector contains the additional Heading Details
			// regenerate each time there is a Entity break.

			lvHeader = createHeader(lsEntityID, aaFundsData);
			/**
			 * Loop through the data as long as hasNext indicates there 
			 * is data
			 */
			while (laDataList.hasNext())
			{
				generateHeader(lvHeader, lvTable);
				cbNewPageDone = true; // desc on new page

				// check to see what kind of summary line we need to put
				// at the begining of the page. if PrimarySplit is off,
				// use Entity to print summary line.
				if (laFundsReportData.getPrimarySplit()
					== FundsConstant.NONE)
				{
					switch (laFundsReportData.getEntity())
					{
						// print summary line for the current 
						// Cash Drawer
						case (FundsConstant.CASH_DRAWER) :
							{
								this.caRpt.print(
									SUMMARY_WORKSTATION_STRING
										+ lsEntityID
										+ COLON_STRING,
									SUMMARY_STARTPT,
									SUMMARY_LENGTH);
								this.caRpt.blankLines(LINES_1);
								break;
							}
							// print summary line for the current Employee
						case (FundsConstant.EMPLOYEE) :
							{
								this.caRpt.print(
									SUMMARY_EMPLOYEE_STRING
										+ lsEntityID
										+ COLON_STRING,
									SUMMARY_STARTPT,
									SUMMARY_LENGTH);
								this.caRpt.blankLines(LINES_1);
								break;
							}
							// take no action. 
						default :
							{
								// empty code block
							}
					} // end switch
				} // end if Primary Split is NONE

				int j =
					this.caRptProps.getPageHeight()
						- END_OF_PAGE_WHITE_SPACE;

				for (k = 0; k <= j; k++)
				{
					if (laDataList.hasNext())
					{

						laDataline = (FeeReportData) laDataList.next();

						// ----------- Entity Break Start --------------
						// if Entity changes, it is time for a page
						// break.
						if ((laFundsReportData.getEntity()
							== FundsConstant.EMPLOYEE
							&& !lsPreEmpId.equals(
								laDataline.getTransEmpId()))
							|| (laFundsReportData.getEntity()
								== FundsConstant.CASH_DRAWER)
							&& liPreCashWsId != laDataline.getCashWsId())
						{
							// closeout PayableTypeCd since we are
							// closing out at Entity.
							// use AcctItmCdDesc to show the Totals
							// message.
							laPayableTypeCdTotals.setAcctItmCdDesc(
								TOTAL_STRING + lsPrePayableTypeCdDesc);
							generateTotal(
								laFundsReportData.isShowSourceMoney(),
								this.caRpt.singleDashes(12),
								laPayableTypeCdTotals);

							lsPrePayableTypeCd = "";
							lsPrePayableTypeCdDesc =
								laDataline.getPayableTypeCdDesc();

							// check to see if a new page is needed.
							// if so, do it		  
							if (needsPageBreak())
							{
								takeAPageBreak(lvHeader, lvTable);
							}

							// if taking primary split breaks, need to
							// close that out at this time too.
							if (laFundsReportData.getPrimarySplit()
								!= FundsConstant.NONE)
							{

								this.caRpt.blankLines(LINES_1);

								// setup text for totals line.
								switch (laFundsReportData
									.getPrimarySplit())
								{
									// Set up for Employee
									case (FundsConstant.EMPLOYEE) :
										{
											laPrimarySplitTotals
												.setAcctItmCdDesc(
												EMPLOYEE_TOTAL_STRING
													+ lsPreEmpId
													+ COLON_STRING);
											break;
										}
										// Set up for cash drawer
									case (FundsConstant.CASH_DRAWER) :
										{
											laPrimarySplitTotals
												.setAcctItmCdDesc(
												CASHDRAWER_TOTAL_STRING
													+ String.valueOf(
														liPreCashWsId)
													+ COLON_STRING);
											break;
										}
										// default is to just say TOTALS
									default :
										{
											laPrimarySplitTotals
												.setAcctItmCdDesc(
												TOTAL_STRING);
										}
								} // end switch

								generateTotal(
									laFundsReportData
										.isShowSourceMoney(),
									this.caRpt.doubleDashes(12),
									laPrimarySplitTotals);
								this.caRpt.blankLines(LINES_1);

								// check to see if a new page is needed.
								// if so, do it	
								if (needsPageBreak())
								{
									takeAPageBreak(lvHeader, lvTable);
								}

								// if there is more than one id on
								// Primary Split.  Show the summary
								// section.
								if (liPrimarySplitCount > 1)
								{

									// Print the Entity Summary Line
									// before printing the Summary
									// Section
									switch (laFundsReportData
										.getEntity())
									{
										// print summary line for the
										// current Cash Drawer
										case (
											FundsConstant
												.CASH_DRAWER) :
											{
												this.caRpt.print(
													SUMMARY_WORKSTATION_STRING
														+ lsEntityID
														+ COLON_STRING,
													SUMMARY_STARTPT,
													SUMMARY_LENGTH);
												this.caRpt.blankLines(
													LINES_1);
												break;
											}
											// print summary line for the
											// current Employee
										case (FundsConstant.EMPLOYEE) :
											{
												this.caRpt.print(
													SUMMARY_EMPLOYEE_STRING
														+ lsEntityID
														+ COLON_STRING,
													SUMMARY_STARTPT,
													SUMMARY_LENGTH);
												this.caRpt.blankLines(
													LINES_1);
												break;
											}
											// take no action. 
										default :
											{
												// empty code block
											}
									} // end switch

									createSummary(
										laFundsReportData
											.isShowSourceMoney(),
										NOTOTALS,
										laPrimarySplitSummaryData,
										lvHeader,
										lvTable);
								} // end if on Primary Split Count

							} // end if Primary Split exists 
							// reset the pre "Primary Split" id.
							switch (laFundsReportData
								.getPrimarySplit())
							{
								case (FundsConstant.EMPLOYEE) :
									{
										lsPreEmpId = "";
										break;
									}
								case (FundsConstant.CASH_DRAWER) :
									{
										// 0 is a valid value for wsid.
										//  so use -1 to say it is not set.
										liPreCashWsId = EMPTY_WSID;
										break;
									}
									// do not reset anything..
								default :
									{
										// empty code block
									}
							}

							// Make sure totals are cleared even if
							// totals are not printed!
							laPrimarySplitTotals.setTotalItmAmt(
								laZeroDollar);
							laPrimarySplitTotals.setTotalItmQty(0);
							laPrimarySplitTotals.setCustAcctItmAmt(
								laZeroDollar);
							laPrimarySplitTotals.setCustAcctItmQty(0);
							laPrimarySplitTotals.setDealerAcctItmAmt(
								laZeroDollar);
							laPrimarySplitTotals.setDealerAcctItmQty(0);
							laPrimarySplitTotals.setSubconAcctItmAmt(
								laZeroDollar);
							laPrimarySplitTotals.setSubconAcctItmQty(0);
							laPrimarySplitTotals.setInternetAcctItmAmt(
								laZeroDollar);
							laPrimarySplitTotals.setInternetAcctItmQty(
								0);
							laPrimarySplitSummaryData = new TreeMap();

							// check to see if a new page is needed.
							//  if so, do it		
							if (needsPageBreak())
							{
								takeAPageBreak(lvHeader, lvTable);
							}

							// setup entity total string printing if
							// needed. also handle all Entity values
							// settings
							switch (laFundsReportData.getEntity())
							{
								case (FundsConstant.CASH_DRAWER) :
									{
										laEntityTotals
											.setAcctItmCdDesc(
											CASHDRAWER_TOTAL_STRING
												+ String.valueOf(
													liPreCashWsId)
												+ COLON_STRING);
										lsEntityID =
											String.valueOf(
												laDataline
													.getCashWsId());
										lsEntityIdList =
											lsEntityIdList
												+ String.valueOf(
													laDataline
														.getCashWsId())
												+ BLANK_SPACE;
										liPreCashWsId =
											laDataline.getCashWsId();
										liEntityCount =
											liEntityCount + 1;
										break;
									}
								case (FundsConstant.EMPLOYEE) :
									{
										laEntityTotals
											.setAcctItmCdDesc(
											EMPLOYEE_TOTAL_STRING
												+ lsPreEmpId
												+ COLON_STRING);
										lsEntityID =
											laDataline.getTransEmpId();
										lsEntityIdList =
											lsEntityIdList
												+ laDataline
													.getTransEmpId()
												+ BLANK_SPACE;
										lsPreEmpId =
											laDataline.getTransEmpId();
										liEntityCount =
											liEntityCount + 1;
										break;
									}
								case (FundsConstant.SUBSTATION) :
									{
										laEntityTotals
											.setAcctItmCdDesc(
											SUBSTATION_TOTAL_STRING
												+ COLON_STRING);
										break;
									}
								case (FundsConstant.COUNTYWIDE) :
									{
										laEntityTotals
											.setAcctItmCdDesc(
											COUNTYWIDE_TOTAL_STRING
												+ COLON_STRING);
										break;
									}
									// this should not happen..
									// just set up a totals string to print totals
								default :
									{
										laEntityTotals
											.setAcctItmCdDesc(
											TOTAL_STRING);
									}
							} // end switch

							// if there is more than one id on Primary
							// Split or there is no Primary Split
							// show the totals line..
							if (liPrimarySplitCount > 1
								|| laFundsReportData.getPrimarySplit()
									== FundsConstant.NONE)
							{
								// Close out the Entity page because of
								// a Entity break.
								generateTotal(
									laFundsReportData
										.isShowSourceMoney(),
									this.caRpt.doubleDashes(12),
									laEntityTotals);
							} //end if Primary Split > 1

							// Make sure totals are cleared even if
							// totals are not printed!
							laEntityTotals.setTotalItmAmt(laZeroDollar);
							laEntityTotals.setTotalItmQty(0);
							laEntityTotals.setCustAcctItmAmt(
								laZeroDollar);
							laEntityTotals.setCustAcctItmQty(0);
							laEntityTotals.setDealerAcctItmAmt(
								laZeroDollar);
							laEntityTotals.setDealerAcctItmQty(0);
							laEntityTotals.setSubconAcctItmAmt(
								laZeroDollar);
							laEntityTotals.setSubconAcctItmQty(0);
							laEntityTotals.setInternetAcctItmAmt(
								laZeroDollar);
							laEntityTotals.setInternetAcctItmQty(0);

							// Set PrimarySplit count back to 0 on a
							// Entity break.	
							liPrimarySplitCount = 0;

							// this vector contains the additional
							// Heading Details regenerate each time
							// there is a Entity break.
							lvHeader =
								createHeader(lsEntityID, aaFundsData);

							// Start a new page for the new entity					
							takeAPageBreak(lvHeader, lvTable);
							k = this.caRpt.getCurrX();

							// check to see what kind of summary line
							// we need to put at the begining of the
							// page. if PrimarySplit is off, use Entity
							// to print summary line.
							if (laFundsReportData.getPrimarySplit()
								== FundsConstant.NONE)
							{
								switch (laFundsReportData.getEntity())
								{
									// print summary line for the
									// current Cash Drawer
									case (FundsConstant.CASH_DRAWER) :
										{
											this.caRpt.print(
												SUMMARY_WORKSTATION_STRING
													+ lsEntityID
													+ COLON_STRING,
												SUMMARY_STARTPT,
												SUMMARY_LENGTH);
											this.caRpt.blankLines(
												LINES_1);
											break;
										}

										// print summary line for the
										// current Employee
									case (FundsConstant.EMPLOYEE) :
										{
											this.caRpt.print(
												SUMMARY_EMPLOYEE_STRING
													+ lsEntityID
													+ COLON_STRING,
												SUMMARY_STARTPT,
												SUMMARY_LENGTH);
											this.caRpt.blankLines(
												LINES_1);
											break;
										}
										// take no action. 
									default :
										{
											// empty code block
										}

								} // end switch
							} // end if Primary Split is NONE

						} // ----- end if for Entity Break -------------

						// --------------- Primary Split ---------------
						// Primary Split break will occur when the
						// previous employee and the current employee
						// are not equal and Primary Split is on
						// Employee. A Primary Split break will occur if
						// the previous wsid and the current wsid
						// are not equal and Primary Split is on
						// Workstation. This break is only taken if the
						// Primary Split Break Indicator is on.
						if ((laFundsReportData.getPrimarySplit()
							== FundsConstant.EMPLOYEE
							&& !lsPreEmpId.equals(
								laDataline.getTransEmpId()))
							|| (laFundsReportData.getPrimarySplit()
								== FundsConstant.CASH_DRAWER)
							&& liPreCashWsId != laDataline.getCashWsId())
						{
							// if Primary Split Pre Id is empty, do not
							// have totals yet just need to show
							// begining of PrimarySplit Group otherwise
							// print totals 
							if ((laFundsReportData.getPrimarySplit()
								== FundsConstant.EMPLOYEE
								&& !lsPreEmpId.equals(""))
								|| (laFundsReportData.getPrimarySplit()
									== FundsConstant.CASH_DRAWER)
								&& liPreCashWsId != EMPTY_WSID)
							{
								// closeout PayableTypeCd since closing
								// out Primary Split. use AcctItmCdDesc
								// to show the Totals message.
								laPayableTypeCdTotals.setAcctItmCdDesc(
									TOTAL_STRING
										+ lsPrePayableTypeCdDesc);

								generateTotal(
									laFundsReportData
										.isShowSourceMoney(),
									this.caRpt.singleDashes(12),
									laPayableTypeCdTotals);

								lsPrePayableTypeCd = "";
								lsPrePayableTypeCdDesc =
									laDataline.getPayableTypeCdDesc();

								// Set up the Primary Split Total line
								switch (laFundsReportData
									.getPrimarySplit())
								{
									// primary split by employee
									case (FundsConstant.EMPLOYEE) :
										{
											laPrimarySplitTotals
												.setAcctItmCdDesc(
												EMPLOYEE_TOTAL_STRING
													+ lsPreEmpId
													+ COLON_STRING);
											lsPreEmpId = "";
											break;
										}
										// primary split by cash drawer
									case (FundsConstant.CASH_DRAWER) :
										{
											laPrimarySplitTotals
												.setAcctItmCdDesc(
												CASHDRAWER_TOTAL_STRING
													+ String.valueOf(
														liPreCashWsId)
													+ COLON_STRING);
											// 0 is a valid value for wsid.
											// so use -1 to say it is not
											// set.
											liPreCashWsId = EMPTY_WSID;
											break;
										}
										// take no action
									default :
										{
											laPrimarySplitTotals
												.setAcctItmCdDesc(
												TOTAL_STRING);
										}
								} // end switch

								// check to see if a new page is needed.
								// if so, do it		
								if (needsPageBreak())
								{
									takeAPageBreak(lvHeader, lvTable);
								}
								generateTotal(
									laFundsReportData
										.isShowSourceMoney(),
									this.caRpt.doubleDashes(12),
									laPrimarySplitTotals);
							} //end if more than one primary split count

							this.caRpt.blankLines(LINES_1);

							// print the Summary line for Primary Split
							switch (laFundsReportData
								.getPrimarySplit())
							{

								// primary split by employee
								case (FundsConstant.EMPLOYEE) :
									{
										lsPreEmpId =
											laDataline.getTransEmpId();
										this.caRpt.print(
											SUMMARY_EMPLOYEE_STRING
												+ lsPreEmpId
												+ COLON_STRING,
											SUMMARY_STARTPT,
											SUMMARY_LENGTH);
										break;
									}
									// primary split by cash drawer
								case (FundsConstant.CASH_DRAWER) :
									{
										liPreCashWsId =
											laDataline.getCashWsId();
										// Print out CASH WORKSTATION Header
										// before the first data line
										this.caRpt.print(
											SUMMARY_WORKSTATION_STRING
												+ String.valueOf(
													liPreCashWsId)
												+ COLON_STRING,
											SUMMARY_STARTPT,
											SUMMARY_LENGTH);
										break;
									}
									// take no action
								default :
									{
										// empty code block
									}
							}
							this.caRpt.blankLines(LINES_1);
							liPrimarySplitCount =
								liPrimarySplitCount + 1;
						} // end if Primary Split Break Check
						// ---------- end Primary Split ----------------

						// --------- Payable Type Code Break -----------
						// PayableTypeCd break occurs when the previous
						// PayableTypeCd and the current PayableTypeCd
						// are not equal.  This break will print a total
						// line for the previous
						// PayableTypeCd and will print the next
						// PayableTypeCd.
						if (!lsPrePayableTypeCd
							.equals(laDataline.getPayableTypeCd()))
						{
							// print the total line.
							// if the previous PayableTypeCd is an empty
							// string, do not print a total line.
							// empty string signifies the begining of a
							// group of data.
							if (!lsPrePayableTypeCd.equals(""))
							{
								// item code totals are kept in the
								// SubTotals object of 
								// FeeTotalsData type. 
								laPayableTypeCdTotals.setAcctItmCdDesc(
									TOTAL_STRING
										+ lsPrePayableTypeCdDesc);
								generateTotal(
									laFundsReportData
										.isShowSourceMoney(),
									this.caRpt.singleDashes(12),
									laPayableTypeCdTotals);
							}
							this.caRpt.blankLines(LINES_1);

							// check to see if a new page is needed.
							//  if so, do it			
							if (needsPageBreak())
							{
								takeAPageBreak(lvHeader, lvTable);
							}

							// Print out Payable Type Code Description
							// on the first data line
							printPayableTypeCdDesc(
								laFundsReportData.isShowSourceMoney(),
								laDataline.getPayableTypeCdDesc());

							lsPrePayableTypeCd =
								laDataline.getPayableTypeCd();
							lsPrePayableTypeCdDesc =
								laDataline.getPayableTypeCdDesc();
						}
						// --------- End PayableTypeCd Break -----------

						// maintain the PayableTypeCd Totals.
						laPayableTypeCdTotals.setTotalItmAmt(
							laPayableTypeCdTotals.getTotalItmAmt().add(
								laDataline.getTotalItmAmt()));
						laPayableTypeCdTotals.setTotalItmQty(
							laPayableTypeCdTotals.getTotalItmQty()
								+ (laDataline.getTotalItmQty()));
						laPayableTypeCdTotals.setCustAcctItmAmt(
							laPayableTypeCdTotals
								.getCustAcctItmAmt()
								.add(
								laDataline.getCustAcctItmAmt()));
						laPayableTypeCdTotals.setCustAcctItmQty(
							laPayableTypeCdTotals.getCustAcctItmQty()
								+ (laDataline.getCustAcctItmQty()));
						laPayableTypeCdTotals.setDealerAcctItmAmt(
							laPayableTypeCdTotals
								.getDealerAcctItmAmt()
								.add(
								laDataline.getDealerAcctItmAmt()));
						laPayableTypeCdTotals.setDealerAcctItmQty(
							laPayableTypeCdTotals.getDealerAcctItmQty()
								+ (laDataline.getDealerAcctItmQty()));
						laPayableTypeCdTotals.setSubconAcctItmAmt(
							laPayableTypeCdTotals
								.getSubconAcctItmAmt()
								.add(
								laDataline.getSubconAcctItmAmt()));
						laPayableTypeCdTotals.setSubconAcctItmQty(
							laPayableTypeCdTotals.getSubconAcctItmQty()
								+ (laDataline.getSubconAcctItmQty()));

						laPayableTypeCdTotals.setInternetAcctItmAmt(
							laPayableTypeCdTotals
								.getInternetAcctItmAmt()
								.add(
								laDataline.getInternetAcctItmAmt()));
						laPayableTypeCdTotals.setInternetAcctItmQty(
							laPayableTypeCdTotals
								.getInternetAcctItmQty()
								+ (laDataline.getInternetAcctItmQty()));

						// maintain the Primary Split Totals
						if (laFundsReportData.getPrimarySplit()
							!= FundsConstant.NONE)
						{
							// add up PrimarySplit break totals.
							laPrimarySplitTotals.setTotalItmAmt(
								laPrimarySplitTotals
									.getTotalItmAmt()
									.add(
									laDataline.getTotalItmAmt()));
							laPrimarySplitTotals.setTotalItmQty(
								laPrimarySplitTotals.getTotalItmQty()
									+ (laDataline.getTotalItmQty()));
							laPrimarySplitTotals.setCustAcctItmAmt(
								laPrimarySplitTotals
									.getCustAcctItmAmt()
									.add(
									laDataline.getCustAcctItmAmt()));
							laPrimarySplitTotals.setCustAcctItmQty(
								laPrimarySplitTotals
									.getCustAcctItmQty()
									+ (laDataline.getCustAcctItmQty()));
							laPrimarySplitTotals.setDealerAcctItmAmt(
								laPrimarySplitTotals
									.getDealerAcctItmAmt()
									.add(
									laDataline.getDealerAcctItmAmt()));
							laPrimarySplitTotals.setDealerAcctItmQty(
								laPrimarySplitTotals
									.getDealerAcctItmQty()
									+ (laDataline.getDealerAcctItmQty()));
							laPrimarySplitTotals.setSubconAcctItmAmt(
								laPrimarySplitTotals
									.getSubconAcctItmAmt()
									.add(
									laDataline.getSubconAcctItmAmt()));
							laPrimarySplitTotals.setSubconAcctItmQty(
								laPrimarySplitTotals
									.getSubconAcctItmQty()
									+ (laDataline.getSubconAcctItmQty()));
							laPrimarySplitTotals.setInternetAcctItmAmt(
								laPrimarySplitTotals
									.getInternetAcctItmAmt()
									.add(
									laDataline
										.getInternetAcctItmAmt()));
							laPrimarySplitTotals.setInternetAcctItmQty(
								laPrimarySplitTotals
									.getInternetAcctItmQty()
									+ (laDataline
										.getInternetAcctItmQty()));

							// Update the PrimarySplit break summary
							updateSummaryData(
								laPrimarySplitSummaryData,
								laDataline);
						}

						// maintain the Entity Totals
						laEntityTotals.setTotalItmAmt(
							laEntityTotals.getTotalItmAmt().add(
								laDataline.getTotalItmAmt()));
						laEntityTotals.setTotalItmQty(
							laEntityTotals.getTotalItmQty()
								+ (laDataline.getTotalItmQty()));
						laEntityTotals.setCustAcctItmAmt(
							laEntityTotals.getCustAcctItmAmt().add(
								laDataline.getCustAcctItmAmt()));
						laEntityTotals.setCustAcctItmQty(
							laEntityTotals.getCustAcctItmQty()
								+ (laDataline.getCustAcctItmQty()));
						laEntityTotals.setDealerAcctItmAmt(
							laEntityTotals.getDealerAcctItmAmt().add(
								laDataline.getDealerAcctItmAmt()));
						laEntityTotals.setDealerAcctItmQty(
							laEntityTotals.getDealerAcctItmQty()
								+ (laDataline.getDealerAcctItmQty()));
						laEntityTotals.setSubconAcctItmAmt(
							laEntityTotals.getSubconAcctItmAmt().add(
								laDataline.getSubconAcctItmAmt()));
						laEntityTotals.setSubconAcctItmQty(
							laEntityTotals.getSubconAcctItmQty()
								+ (laDataline.getSubconAcctItmQty()));

						laEntityTotals.setInternetAcctItmAmt(
							laEntityTotals.getInternetAcctItmAmt().add(
								laDataline.getInternetAcctItmAmt()));
						laEntityTotals.setInternetAcctItmQty(
							laEntityTotals.getInternetAcctItmQty()
								+ (laDataline.getInternetAcctItmQty()));

						// Update the Summary data with this dataline
						updateSummaryData(
							laEntitySummaryData,
							laDataline);

						// call the print line formatting method.
						printLine(
							laFundsReportData.isShowSourceMoney(),
							NOTOTALS,
							laDataline,
							true);
						this.caRpt.nextLine();
						// k++;
						k = this.caRpt.getCurrX();
						// make sure our line count is right	

					} // end hasNext if
				} // end of for

				// Close out all break levels of report.
				// the summary page processing starts off
				// once hasNext goes to false, we can check for Summary
				// Page Need.
				if (!laDataList.hasNext())
				{
					// print the totals for the last PayableTypeCd
					// processed.
					laPayableTypeCdTotals.setAcctItmCdDesc(
						TOTAL_STRING
							+ laDataline.getPayableTypeCdDesc());
					generateTotal(
						laFundsReportData.isShowSourceMoney(),
						this.caRpt.singleDashes(12),
						laPayableTypeCdTotals);

					// Process the Primary Split Break.
					// Print the Primary Split Total Line
					if (laFundsReportData.getPrimarySplit()
						!= FundsConstant.NONE)
					{
						switch (laFundsReportData.getPrimarySplit())
						{
							case (FundsConstant.EMPLOYEE) :
								{
									laPrimarySplitTotals
										.setAcctItmCdDesc(
										EMPLOYEE_TOTAL_STRING
											+ lsPreEmpId
											+ COLON_STRING);
									break;
								}
							case (FundsConstant.CASH_DRAWER) :
								{
									laPrimarySplitTotals
										.setAcctItmCdDesc(
										CASHDRAWER_TOTAL_STRING
											+ String.valueOf(
												liPreCashWsId)
											+ COLON_STRING);
									break;
								}
								// set up a default line
							default :
								{
									laPrimarySplitTotals
										.setAcctItmCdDesc(
										TOTAL_STRING);
								}
						}
						generateTotal(
							laFundsReportData.isShowSourceMoney(),
							this.caRpt.doubleDashes(12),
							laPrimarySplitTotals);

						// if there was more than one id for
						// PrimarySplit, print the Summary Section
						if (liPrimarySplitCount > 1)
						{
							String lsIdForPrimarySplit = new String();
							// create the summary string for this
							// Entity Id
							switch (laFundsReportData.getEntity())
							{
								// set up employee
								case (FundsConstant.EMPLOYEE) :
									{
										lsIdForPrimarySplit =
											EMPLOYEE_SUMMARY_STRING
												+ lsPreEmpId
												+ COLON_STRING;
										break;
									}
								case (FundsConstant.CASH_DRAWER) :
									{
										lsIdForPrimarySplit =
											CASHDRAWER_SUMMARY_STRING
												+ String.valueOf(
													liPreCashWsId)
												+ COLON_STRING;
										break;
									}
								case (FundsConstant.SUBSTATION) :
									{
										lsIdForPrimarySplit =
											SUBSTATION_SUMMARY_STRING
												+ COLON_STRING;
										break;
									}
									// Set up a default line
								default :
									{
										lsIdForPrimarySplit = "Summary";
									}
							}

							// Print out Primary Split Id Header before
							// showing the summary
							this.caRpt.blankLines(1);
							this.caRpt.print(
								lsIdForPrimarySplit,
								SUMMARY_STARTPT,
								SUMMARY_LENGTH);
							this.caRpt.blankLines(LINES_1);
							//	close out line

							// put the summary section on the page
							createSummary(
								laFundsReportData.isShowSourceMoney(),
								NOTOTALS,
								laPrimarySplitSummaryData,
								lvHeader,
								lvTable);

						} // end if on Primary Split Count
					} // end if on check for Primary Split

					//**************************************************
					//**************************************************
					// The intent here is to print a total line if
					// Primary Split count is greater than 1
					// or there is no Primary Split!!

					// if there was more than one id for PrimarySplit or
					// there is no primary split, print the totals
					if (liPrimarySplitCount > 1
						|| laFundsReportData.getPrimarySplit()
							== FundsConstant.NONE)
					{
						// set up to print the totals line for this
						// entity id
						switch (laFundsReportData.getEntity())
						{
							case (FundsConstant.EMPLOYEE) :
								{
									laEntityTotals.setAcctItmCdDesc(
										EMPLOYEE_TOTAL_STRING
											+ lsEntityID
											+ COLON_STRING);
									break;
								}
							case (FundsConstant.CASH_DRAWER) :
								{
									laEntityTotals.setAcctItmCdDesc(
										CASHDRAWER_TOTAL_STRING
											+ lsEntityID
											+ COLON_STRING);
									break;
								}
							case (FundsConstant.SUBSTATION) :
								{
									laEntityTotals.setAcctItmCdDesc(
										SUBSTATION_TOTAL_STRING
											+ COLON_STRING);
									break;
								}
							case (FundsConstant.COUNTYWIDE) :
								{
									laEntityTotals.setAcctItmCdDesc(
										COUNTYWIDE_TOTAL_STRING
											+ COLON_STRING);
									break;
								}
							default :
								{
									laEntityTotals.setAcctItmCdDesc(
										TOTAL_STRING);
								}
						}

						// print the total for the entity id
						generateTotal(
							laFundsReportData.isShowSourceMoney(),
							this.caRpt.doubleDashes(12),
							laEntityTotals);
					}
					//**************************************************
					//**************************************************

					// print the totals for the last Entity item.
					// only print a summary page if there was more than
					// 1 item for Entity break
					if (liEntityCount > 1)
					{

						// put out the footer for this page.  Summary
						// will be on a new page.
						generateFooter();

						// this vector contains the additional Heading
						// Details regenerate each time there is a
						// Entity break.
						cbGenSumPage = true;
						lvHeader =
							createHeader(lsEntityIdList, aaFundsData);

						// generate the header for the summary page
						generateHeader(lvHeader, lvTable);

						// this method gets the summary data and formats
						// it to the page
						createSummary(
							laFundsReportData.isShowSourceMoney(),
							SHOWTOTALS,
							laEntitySummaryData,
							lvHeader,
							lvTable);
					}

					this.caRpt.blankLines(LINES_2);
					k++;
					// check to see if a new page is needed.  if so,
					// do it		
					if (needsPageBreak())
					{
						takeAPageBreak(lvHeader, lvTable);
					}
					//generateEndOfReport();
				} // end of !hasNext
				//******************************************************
				//******************************************************
				// defect 8628 
				//generateFooter();
				generateFooter(true); 
				// end defect 8628 
			} // end of while
		} // end if

		if (laFundsReportData.getEntity() == FundsConstant.CASH_DRAWER)
		{
			for (int i = 0;
				i < aaFundsData.getSelectedCashDrawers().size();
				i++)
			{
				CashWorkstationCloseOutData laCashDrwrObj =
					(CashWorkstationCloseOutData) aaFundsData
						.getSelectedCashDrawers()
						.elementAt(i);
				String lsReportStatus = laCashDrwrObj.getRptStatus();
				EntityStatusData laDrawer = new EntityStatusData();
				laDrawer.setCashWsId(laCashDrwrObj.getCashWsId());

				// defect 9943 
				if ((lsReportStatus != null)
					//&& lsReportStatus.equals("Report Generated"))
					&& lsReportStatus.equals(
						FundsConstant.REPORT_GENERATED))
				{

					laDrawer.setReportStatus(
					//"Report Generated");
					FundsConstant.REPORT_GENERATED);
				}
				else
				{
					laDrawer.setReportStatus(
					//"No Transactions");
					FundsConstant.NO_TRANSACTIONS);
				}
				// end defect 9943 
				cvFeesStatVec.add(laDrawer);
			}
		}

		if (laFundsReportData.getEntity() == FundsConstant.EMPLOYEE)
		{
			for (int i = 0;
				i < aaFundsData.getSelectedEmployees().size();
				i++)
			{
				EmployeeData laEmployeeObj =
					(EmployeeData) aaFundsData
						.getSelectedEmployees()
						.elementAt(
						i);
				String lsReportStatus = laEmployeeObj.getRptStatus();
				EntityStatusData laEmployee = new EntityStatusData();
				laEmployee.setEmployeeId(laEmployeeObj.getEmployeeId());
				// defect 9943 
				if ((lsReportStatus != null)
					&& lsReportStatus.equals(
					//"Report Generated"))
				FundsConstant
						.REPORT_GENERATED))
				{
					laEmployee.setReportStatus(
					//"Report Generated");
					FundsConstant.REPORT_GENERATED);
				}
				else
				{
					laEmployee.setReportStatus(
					//"No Transactions");
					FundsConstant.NO_TRANSACTIONS);
				}
				// end defect 9943 
				cvFeesStatVec.add(laEmployee);
			}
		}
	} //formatReport

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Format and print the total line.
	 * Print the Dashed Lines required.
	 * Call printLine to print out the data.
	 * Then, zero out the totals.
	 * 
	 * @param abColumnLayout boolean- this is the Column Layout
	 * 	Indicator.
	 * @param asDashes String - Dashes.  This is the type of dash to
	 * 	print.
	 * @param aaTotals FeeReportData - Reference to the FeeReportData
	 * 	Object used. 
	 */
	public void generateTotal(
		boolean abColumnLayout,
		String asDashes,
		FeeReportData aaTotals)
	{
		if (cbNewPageDone)
		{
			cbNewPageDone = false; // desc on new page
		}
		else
		{
			if (abColumnLayout) // Totals are in Payment column format
			{
				this.caRpt.rightAlign(asDashes, START_PT_46, LENGTH_12);
				this.caRpt.rightAlign(asDashes, START_PT_63, LENGTH_12);
				this.caRpt.rightAlign(asDashes, START_PT_81, LENGTH_12);
				this.caRpt.rightAlign(
					asDashes,
					START_PT_100,
					LENGTH_12);
				this.caRpt.rightAlign(
					asDashes,
					START_PT_118,
					LENGTH_12);
				this.caRpt.nextLine();
			}
			else // totals are in simple column format
				{
				this.caRpt.rightAlign(asDashes, START_PT_70, LENGTH_12);
				this.caRpt.nextLine();
			}
		}
		printLine(abColumnLayout, SHOWTOTALS, aaTotals, false);

		this.caRpt.nextLine();
		Dollar laZeroDollar = new Dollar(0.00);
		aaTotals.setTotalItmAmt(laZeroDollar);
		aaTotals.setTotalItmQty(0);
		aaTotals.setCustAcctItmAmt(laZeroDollar);
		aaTotals.setCustAcctItmQty(0);
		aaTotals.setDealerAcctItmAmt(laZeroDollar);
		aaTotals.setDealerAcctItmQty(0);
		aaTotals.setSubconAcctItmAmt(laZeroDollar);
		aaTotals.setSubconAcctItmQty(0);

		aaTotals.setInternetAcctItmAmt(laZeroDollar);
		aaTotals.setInternetAcctItmQty(0);
	}
	/**
	 * Determine if a page break is needed.  Return a boolean indicating
	 * what the result is.
	 * 
	 * @return boolean
	 */
	public boolean needsPageBreak()
	{
		// if CurrX is greater than the projected end of detail lines,
		// return true
		if (this.caRpt.getCurrX()
			>= (this.caRptProps.getPageHeight()
				- END_OF_PAGE_WHITE_SPACE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Print the detail line.
	 * <p>This routine prints a line of the report.
	 * The line can be from detail data or a total line.
	 * 
	 * @param abColumnLayout boolean
	 * @param abPrintZeroDollar boolean
	 * @param aaDataLine FeeReportData
	 * @param abShowTotal boolean
	 */
	public void printLine(
		boolean abColumnLayout,
		boolean abPrintZeroDollar,
		FeeReportData aaDataLine,
		boolean abShowTotal)
	{
		Dollar laZeroDollar = new Dollar(0.00);

		/**
		 * There are two variations on Column Layout in this report.
		 * The Columns can be laid out in Payment Type format.
		 * This means there is a column for Customer, Subcontractor,
		 * Dealer, Internet Data and Total Sales.
		 * Simple layout means that only the Total Sales column is
		 * printed.
		 */

		if (cbNewPageDone) // desc on new page
		{ // desc on new page
			printPayableTypeCdDesc(abColumnLayout, // desc on new page 
			aaDataLine.getPayableTypeCdDesc()); // desc on new page
			cbNewPageDone = false; // desc on new page
		} // desc on new page

		if (abColumnLayout) // print payment type break out columns
		{

			// Print the detail description on each detail line
			this.caRpt.print(
				aaDataLine.getAcctItmCdDesc(),
				START_PT_15,
				LENGTH_30);

			// format and print the customer amounts on each detail line
			if (!aaDataLine.getCustAcctItmAmt().equals(laZeroDollar)
				|| abPrintZeroDollar)
			{
				this.caRpt.rightAlign(
					aaDataLine
						.getCustAcctItmAmt()
						.printDollar()
						.substring(
						1,
						aaDataLine
							.getCustAcctItmAmt()
							.printDollar()
							.length()),
					START_PT_46,
					LENGTH_11);
			}
			if (aaDataLine.getCustAcctItmQty() > 0 && abShowTotal)
			{
				this.caRpt.rightAlign(
					String.valueOf(aaDataLine.getCustAcctItmQty()),
					START_PT_57,
					LENGTH_4);
			}

			// format and print the subcontractor amounts on each detail
			// line
			if (!aaDataLine.getSubconAcctItmAmt().equals(laZeroDollar)
				|| abPrintZeroDollar)
			{
				this.caRpt.rightAlign(
					aaDataLine
						.getSubconAcctItmAmt()
						.printDollar()
						.substring(
						1,
						aaDataLine
							.getSubconAcctItmAmt()
							.printDollar()
							.length()),
					START_PT_63,
					LENGTH_11);
			}
			if (aaDataLine.getSubconAcctItmQty() > 0 && abShowTotal)
			{
				this.caRpt.rightAlign(
					String.valueOf(aaDataLine.getSubconAcctItmQty()),
					START_PT_75,
					LENGTH_4);
			}

			// format and print the dealer amounts on each detail line
			if (!aaDataLine.getDealerAcctItmAmt().equals(laZeroDollar)
				|| abPrintZeroDollar)
			{
				this.caRpt.rightAlign(
					aaDataLine
						.getDealerAcctItmAmt()
						.printDollar()
						.substring(
						1,
						aaDataLine
							.getDealerAcctItmAmt()
							.printDollar()
							.length()),
					START_PT_81,
					LENGTH_11);
			}
			if (aaDataLine.getDealerAcctItmQty() > 0 && abShowTotal)
			{
				this.caRpt.rightAlign(
					String.valueOf(aaDataLine.getDealerAcctItmQty()),
					START_PT_93,
					LENGTH_4);
			}

			// format and print the internet amounts on each detail line
			if (!aaDataLine
				.getInternetAcctItmAmt()
				.equals(laZeroDollar)
				|| abPrintZeroDollar)
			{
				this.caRpt.rightAlign(
					aaDataLine
						.getInternetAcctItmAmt()
						.printDollar()
						.substring(
						1,
						aaDataLine
							.getInternetAcctItmAmt()
							.printDollar()
							.length()),
					START_PT_100,
					LENGTH_11);
			}
			if (aaDataLine.getInternetAcctItmQty() > 0 && abShowTotal)
			{
				this.caRpt.rightAlign(
					String.valueOf(aaDataLine.getInternetAcctItmQty()),
					START_PT_112,
					LENGTH_4);
			}

			// format and print the overall amounts on each detail line
			if (!aaDataLine.getTotalItmAmt().equals(laZeroDollar)
				|| abPrintZeroDollar)
			{
				this.caRpt.rightAlign(
					aaDataLine
						.getTotalItmAmt()
						.printDollar()
						.substring(
						1,
						aaDataLine
							.getTotalItmAmt()
							.printDollar()
							.length()),
					START_PT_118,
					LENGTH_11);
			}
			if (aaDataLine.getTotalItmQty() > 0 && abShowTotal)
			{
				this.caRpt.rightAlign(
					String.valueOf(aaDataLine.getTotalItmQty()),
					START_PT_129,
					LENGTH_4);
			}
		}
		else
		{
			// Print the detail description on each detail line
			this.caRpt.print(
				aaDataLine.getAcctItmCdDesc(),
				START_PT_38,
				LENGTH_30);

			// format and print the overall amounts on each detail line
			if (!aaDataLine.getTotalItmAmt().equals(laZeroDollar)
				|| abPrintZeroDollar
				|| true)
			{
				this.caRpt.rightAlign(
					aaDataLine
						.getTotalItmAmt()
						.printDollar()
						.substring(
						1,
						aaDataLine
							.getTotalItmAmt()
							.printDollar()
							.length()),
					START_PT_70,
					LENGTH_12);
			}
			if (aaDataLine.getTotalItmQty() > 0 && abShowTotal)
			{
				this.caRpt.rightAlign(
					String.valueOf(aaDataLine.getTotalItmQty()),
					START_PT_84,
					LENGTH_4);
			}
		}
	}

	/**
	 * Prints the Payable Type Cd Desc.
	 * 
	 * @param isShowSourceMoney boolean
	 * @param asPayableTypeCdDesc String
	 */
	public void printPayableTypeCdDesc(
		boolean isShowSourceMoney,
		String asPayableTypeCdDesc)
	{
		// Print out Payable Type Code Description on the first
		// data line
		if (isShowSourceMoney)
		{
			this.caRpt.print(
				asPayableTypeCdDesc,
				START_PT_2,
				LENGTH_12);
		}
		else
		{
			this.caRpt.print(
				asPayableTypeCdDesc,
				START_PT_20,
				LENGTH_12);
		}
		cbNewPageDone = false;
	}

	/**
	 * Method to populate the data to be displayed on the report
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{

		// this is faked data..

		Vector laData = new Vector();

		// Fee Source 1 is Customer
		// Fee Source 2 is Subcontractor
		// Fee Source 3 is Dealer
		// Fee Source 5 is Internet Data

		// These form record 1
		FeeTypeReportData laDataLineA1 = new FeeTypeReportData();
		laDataLineA1.setCashWsId(100);
		laDataLineA1.setPayableTypeCd("1");
		laDataLineA1.setPayableTypeCdDesc("TITLE");
		laDataLineA1.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineA1.setTransEmpId("RTSUSER");
		laDataLineA1.setFeeSourceCd(1);
		laDataLineA1.setItmQty(2);
		Dollar laTempDollarA1 = new Dollar("26.00");
		laDataLineA1.setItmPrice(laTempDollarA1);
		laData.addElement(laDataLineA1);

		FeeTypeReportData laDataLineA2 = new FeeTypeReportData();
		laDataLineA2.setCashWsId(100);
		laDataLineA2.setPayableTypeCd("1");
		laDataLineA2.setPayableTypeCdDesc("TITLE");
		laDataLineA2.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineA2.setTransEmpId("RTSUSER");
		laDataLineA2.setFeeSourceCd(2);
		laDataLineA2.setItmQty(2);
		Dollar laTempDollarA2 = new Dollar("26.00");
		laDataLineA2.setItmPrice(laTempDollarA2);
		laData.addElement(laDataLineA2);

		FeeTypeReportData laDataLineA3 = new FeeTypeReportData();
		laDataLineA3.setCashWsId(100);
		laDataLineA3.setPayableTypeCd("1");
		laDataLineA3.setPayableTypeCdDesc("TITLE");
		laDataLineA3.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineA3.setTransEmpId("RTSUSER");
		laDataLineA3.setFeeSourceCd(3);
		laDataLineA3.setItmQty(2);
		Dollar laTempDollarA3 = new Dollar("26.00");
		laDataLineA3.setItmPrice(laTempDollarA3);
		laData.addElement(laDataLineA3);

		// This forms record 2
		FeeTypeReportData laDataLineB1 = new FeeTypeReportData();
		laDataLineB1.setCashWsId(100);
		laDataLineB1.setPayableTypeCd("2");
		laDataLineB1.setPayableTypeCdDesc("REGISTRATION");
		laDataLineB1.setAcctItmCdDesc("AUTOMATION FEE");
		laDataLineB1.setTransEmpId("MINWANG");
		laDataLineB1.setFeeSourceCd(1);
		laDataLineB1.setItmQty(2);
		Dollar laTempDollarB1 = new Dollar("26.00");
		laDataLineB1.setItmPrice(laTempDollarB1);
		laData.addElement(laDataLineB1);

		// This forms record 3
		FeeTypeReportData laDataLineC1 = new FeeTypeReportData();
		laDataLineC1.setCashWsId(100);
		laDataLineC1.setPayableTypeCd("2");
		laDataLineC1.setPayableTypeCdDesc("REGISTRATION");
		laDataLineC1.setAcctItmCdDesc("REFLECTORIZATION FEE");
		laDataLineC1.setTransEmpId("MINWANG");
		laDataLineC1.setFeeSourceCd(1);
		laDataLineC1.setItmQty(2);
		Dollar laTempDollarC1 = new Dollar("123456.60");
		laDataLineC1.setItmPrice(laTempDollarC1);
		laData.addElement(laDataLineC1);

		// This forms record 4
		FeeTypeReportData laDataLineD1 = new FeeTypeReportData();
		laDataLineD1.setCashWsId(100);
		laDataLineD1.setPayableTypeCd("2");
		laDataLineD1.setPayableTypeCdDesc("REGISTRATION");
		laDataLineD1.setAcctItmCdDesc("WINDSHIELD STICKER X12");
		laDataLineD1.setTransEmpId("MINWANG");
		laDataLineD1.setFeeSourceCd(1);
		laDataLineD1.setItmQty(2);
		Dollar laTempDollarD1 = new Dollar("-89.10");
		laDataLineD1.setItmPrice(laTempDollarD1);
		laData.addElement(laDataLineD1);

		// This formas record 5
		FeeTypeReportData laDataLineE1 = new FeeTypeReportData();
		laDataLineE1.setCashWsId(100);
		laDataLineE1.setPayableTypeCd("2");
		laDataLineE1.setPayableTypeCdDesc("REGISTRATION");
		laDataLineE1.setAcctItmCdDesc("WINDSHIELD STICKER X12");
		laDataLineE1.setTransEmpId("MINWANG");
		laDataLineE1.setFeeSourceCd(1);
		laDataLineE1.setItmQty(2);
		Dollar laTempDollarE1 = new Dollar("-89.10");
		laDataLineE1.setItmPrice(laTempDollarE1);
		laData.addElement(laDataLineE1);

		// This forms record 6
		FeeTypeReportData laDataLineI1 = new FeeTypeReportData();
		laDataLineI1.setCashWsId(100);
		laDataLineI1.setPayableTypeCd("2");
		laDataLineI1.setPayableTypeCdDesc("REGISTRATION");
		laDataLineI1.setAcctItmCdDesc("WINDSHIELD STICKER X12");
		laDataLineI1.setTransEmpId("MINWANG");
		laDataLineI1.setFeeSourceCd(5);
		laDataLineI1.setItmQty(2);
		Dollar laTempDollarI1 = new Dollar("26.00");
		laDataLineI1.setItmPrice(laTempDollarI1);
		laData.addElement(laDataLineI1);

		// This forms record 7
		FeeTypeReportData laDataLineI2 = new FeeTypeReportData();
		laDataLineI2.setCashWsId(100);
		laDataLineI2.setPayableTypeCd("2");
		laDataLineI2.setPayableTypeCdDesc("REGISTRATION");
		laDataLineI2.setAcctItmCdDesc("WINDSHIELD STICKER X12");
		laDataLineI2.setTransEmpId("MINWANG");
		laDataLineI2.setFeeSourceCd(5);
		laDataLineI2.setItmQty(2);
		laDataLineI1.setItmPrice(laTempDollarI1);
		laData.addElement(laDataLineI1);

		return laData;
	}

	/**
	 * This method does a page break.
	 * 
	 * @param avHeader Vector
	 * @param avTable Vector
	 */
	public void takeAPageBreak(Vector avHeader, Vector avTable)
	{
		// close out the current page
		generateFooter();

		// Start the new page
		generateHeader(avHeader, avTable);

		cbNewPageDone = true; // desc on new page
	}

	/**
	 * Maintain the data for the summary which is stored in a TreeMap.
	 * Data is either added (put) or updated (get, remove, put).
	 * 
	 * @param aaSummaryPageData TreeMap
	 * @param aaDataLine FeeReportData
	 */
	public void updateSummaryData(
		TreeMap aaSummaryPageData,
		FeeReportData aaDataLine)
	{
		// instantiate lTotalLine to hold the Detail Summary Line
		FeeReportData laTotalLine = new FeeReportData();

		String lsMapKey =
			aaDataLine.getPayableTypeCd()
				+ aaDataLine.getAcctItmGrpCd()
				+ aaDataLine.getAcctItmCrdtIndi()
				+ aaDataLine.getAcctItmCdDesc();

		// check to see if a total line has already been started for
		// this accounting item if so, update it.  Otherwise insert it.
		if (aaSummaryPageData.containsKey(lsMapKey))
		{

			laTotalLine =
				(FeeReportData) aaSummaryPageData.get(lsMapKey);
			// Add to the existing totals
			laTotalLine.setCustAcctItmAmt(
				laTotalLine.getCustAcctItmAmt().add(
					aaDataLine.getCustAcctItmAmt()));
			laTotalLine.setCustAcctItmQty(
				laTotalLine.getCustAcctItmQty()
					+ (aaDataLine.getCustAcctItmQty()));
			laTotalLine.setDealerAcctItmAmt(
				laTotalLine.getDealerAcctItmAmt().add(
					aaDataLine.getDealerAcctItmAmt()));
			laTotalLine.setDealerAcctItmQty(
				laTotalLine.getDealerAcctItmQty()
					+ (aaDataLine.getDealerAcctItmQty()));
			laTotalLine.setSubconAcctItmAmt(
				laTotalLine.getSubconAcctItmAmt().add(
					aaDataLine.getSubconAcctItmAmt()));
			laTotalLine.setSubconAcctItmQty(
				laTotalLine.getSubconAcctItmQty()
					+ (aaDataLine.getSubconAcctItmQty()));

			laTotalLine.setInternetAcctItmAmt(
				laTotalLine.getInternetAcctItmAmt().add(
					aaDataLine.getInternetAcctItmAmt()));
			laTotalLine.setInternetAcctItmQty(
				laTotalLine.getInternetAcctItmQty()
					+ (aaDataLine.getInternetAcctItmQty()));

			laTotalLine.setTotalItmAmt(
				laTotalLine.getTotalItmAmt().add(
					aaDataLine.getTotalItmAmt()));
			laTotalLine.setTotalItmQty(
				laTotalLine.getTotalItmQty()
					+ (aaDataLine.getTotalItmQty()));

			aaSummaryPageData.remove(lsMapKey);
			aaSummaryPageData.put(lsMapKey, laTotalLine);
		}
		else
		{
			// set the descriptions
			laTotalLine.setPayableTypeCdDesc(
				aaDataLine.getPayableTypeCdDesc());
			laTotalLine.setAcctItmCdDesc(aaDataLine.getAcctItmCdDesc());
			// set the totals
			laTotalLine.setCustAcctItmAmt(
				aaDataLine.getCustAcctItmAmt());
			laTotalLine.setCustAcctItmQty(
				aaDataLine.getCustAcctItmQty());
			laTotalLine.setDealerAcctItmAmt(
				aaDataLine.getDealerAcctItmAmt());
			laTotalLine.setDealerAcctItmQty(
				aaDataLine.getDealerAcctItmQty());
			laTotalLine.setSubconAcctItmAmt(
				aaDataLine.getSubconAcctItmAmt());
			laTotalLine.setSubconAcctItmQty(
				aaDataLine.getSubconAcctItmQty());

			laTotalLine.setInternetAcctItmAmt(
				aaDataLine.getInternetAcctItmAmt());
			laTotalLine.setInternetAcctItmQty(
				aaDataLine.getInternetAcctItmQty());

			laTotalLine.setTotalItmAmt(aaDataLine.getTotalItmAmt());
			laTotalLine.setTotalItmQty(aaDataLine.getTotalItmQty());
			aaSummaryPageData.put(lsMapKey, laTotalLine);
		}
	}
}
