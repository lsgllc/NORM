package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import java.io.FileOutputStream;


import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

import com.txdot.isd.rts.server.funds.FundsServerBusiness;

/*
 *
 * InventorySummaryReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arredondo 	06/12/2002  Defect-CQU100004243 Inserting the line
 *							"END OF REPORT" at the end of each separate
 *							report or at the end of report summary.
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *			 				add QTYREPRINTED_STARTPT, REPRINTED_HEADER,
 *							csTotQtyReprnted
 *							modified other variables as noted
 *							modify formatReport(Vector),
 *							formatReport(Vector, FundsData),
 *							printSummaryLineTotal(), 
 *							modify printSummaryLine
 *							Comment out unused variable declarations.
 * 							Ver 5.2.0
 * K Harrell	07/15/2004	Right align reprint statistics
 *							modify printSummaryLine(),
 *							printSummaryLineTotal()
 *							defect 7339  Ver 5.2.0
 * K Harrell	07/18/2004 	Total Reprint Quantity incorrectly printing
 *							blank vs. total reprints in some cases.
 *							modify printSummaryLineTotal()
 *							modify printSummaryLine()
 *							defect 7347  Ver 5.2.0 
 * S Johnston	05/11/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							add getInvSStatVec(), getInvSStatus()
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/26/2005	Do not reference closeoutbegtstmp when
 * 							reporting by date range. All variables
 * 							private.  
 * 							modify createHeader()
 * 							defect 7896 Ver 5.2.3
 * K Harrell	06/03/2009	Use getClockTimeNoMs(), FundsConstant
 * 							modify createHeader(), formatReport()
 * 							defect 9943 Ver Defect_POS_F 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F     	
 * ---------------------------------------------------------------------			                           
 */

/**
 * This program formats the report for Inventory Summary Report, which
 * is part of Funds Reporting.  
 * 
 * How the user generates the Inventory Summary Report:
 *
 * 1) After an end of day closeout
 * 2) User selects Funds option
 * 3) User selects Balance Reports suboption
 * 4) User can select Cash Drawer OR Employee - Substation going away
 * 5) User can also Select a Primary split of report by:
 *    None, Cash Drawer, or employee  
 * 6) There is also a "Show source of money" check box - this doesn't
 *    make sense for inventory - not money or a transaction
 * 7) User makes their choices and clicks on the Enter button.
 * 8) A list of cash drawers (or employees) appears with the Cash Drawer
 * 	  choice
 * 9) The user selects one or more of the list lines, and OK.
 *10) User can then select one of four check boxes:
 *    "Last close out", "Since Last Current Status",
 *    "Since Last Close Out", "Specific Date Range"
 *11) User can then select one or more reports, with a check box
 *    to display the report before printing.
 *
 * @version	Defect_POS_F	08/10/2009
 * @author	Bob Brown
 * <br>Creation Date:		10/01/2001
 */
public class InventorySummaryReport extends ReportTemplate
{
	// boolean 
	private boolean cbGenSumPage = false;
	private boolean cbMultipleEntity = false;
	private boolean cbSplitby = false;

	// String 
	private String csEntity;
	private String csInvItmYr;
	private String csReportTotalTxt;
	private String csTotQtyReprnted;
	private String csTotQtyReused;
	private String csTotQtySold;
	private String csTotQtyVoided;

	// Vector 
	private Vector cvInventoryEntityTotalLine = new Vector();
	private Vector cvInventoryReportTotalLine = new Vector();
	private Vector cvInvSStatVec = new Vector();

	// Object 
	private ReportStatusData caInvSStatus = new ReportStatusData();

	// Constant
	private final static int ENTITY_TOTAL_TXT_LENGTH = 18;
	private final static int ENTITY_TOTAL_TXT_STARTPT = 3;
	private final static int ENTITY_TXT_LENGTH = 8;
	private final static int ENTITY_TXT_STARTPT = 22;
	private final static int ITEMDESC_LENGTH = 30;
	private final static int ITEMDESC_STARTPT = 20; // PCR 34
	private final static int ITEMYEAR_LENGTH = 4;
	private final static int ITEMYEAR_STARTPT = 55; // PCR 34
	private final static int QTYREPRINTED_STARTPT = 95; // PCR 34
	private final static int QTYREUSED_LENGTH = 8;
	private final static int QTYREUSED_STARTPT = 85; // PCR 34
	private final static int QTYSOLD_LENGTH = 8;
	private final static int QTYSOLD_STARTPT = 65; // PCR 34
	private final static int QTYVOIDED_LENGTH = 8;
	private final static int QTYVOIDED_STARTPT = 75; // PCR 34
	private final static int SPLITBY_LENGTH = 12;
	private final static int TOTAL_TXT_LENGTH = 13;
	private final static int TOTAL_TXT_STARTPT = 13;

	private final static String CASH_DRAWER_TOTAL_TXT =
		"CASH DRAWER TOTAL:";
	private final static String CLOSE_AFTER_SUBSTA_SUM =
		"CLOSEOUTS AFTER SUBSTATION SUMMARY";
	private final static String DATE_RANGE = "DATE RANGE";
	private final static String DESC_HEADER = "DESCRIPTION";
	private final static String EMPLOYEE_TOTAL_TXT = "EMPLOYEE TOTAL:";
	private final static String FOR_CLOSE = "FOR LAST CLOSEOUT";
	private final static String ITEM_HEADER = "ITEM";
	private final static String LAST_CLOSEOUT = "SINCE LAST CLOSEOUT";
	private final static String LAST_CURRENT_STATUS =
		"SINCE LAST CURRENT STATUS";
	private final static String NOT_DEFINED = "REPORT TYPE NOT DEFINED";
	private final static String QUANTITY_HEADER = "QUANTITY";
	private final static String REPORT_TOTAL_TXT = "REPORT TOTAL:";
	private final static String REPRINTED_HEADER = "REPRINTED";
	private final static String REUSED_HEADER = "REUSED";
	private final static String SOLD_HEADER = "SOLD";
	private final static String SPLITBYCASHD_HEADER = "CASH DRAWER";
	private final static String SPLITBYEMP_HEADER = "EMPLOYEE ID";
	private final static String VOIDED_HEADER = "VOIDED";
	private final static String YEAR_HEADER = "YEAR";

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5221");
		// need to load "- CURRENT STATUS -" OR
		// need to load "- CLOSE OUT -" OR
		// need to load "- LAST CLOSE OUT -" OR
		// need to load "- CLOSE OUTS AFTER SUBSTATION SUMMARY -" OR
		// need to load "- SINCE LAST CLOSE OUT -" OR
		// need to load "- SINCE LAST CURRENT STATUS -" OR
		// need to load "- BY DATE RANGE -"
		String lsStatus = "INVENTORY SUMMARY REPORT";
		InventorySummaryReport laISR =
			new InventorySummaryReport(lsStatus, laRptProps);
		//cRptProps is of type ReportProperties, which was 
		//instantiated in the main method by the following code:
		//
		// InventorySummaryReport isr = new InventorySummaryReport(status, aRptProps);
		//
		// this code invokes the following code:
		//
		// public InventorySummaryReport(String asRptName, ReportProperties aRptProps)
		// {
		//   super(asRptName, aRptProps);
		// }
		// when super ... executes, the following code runs,:
		// because InventorySummaryReport, extends Report template
		//
		// public ReportTemplate(String asRptName, ReportProperties aRptProps) 
		// {
		// cRpt = new Report(asRptName,aRptProps.getPageWidth(), aRptProps.getPageHeight());
		// cRptProps = aRptProps;
		// }
		//
		// So, after the above code gets executed, cRpt is the repotr object
		// with report name and page size, while cRptProps at this point
		// contains a reference to ReportProperties, with its set methods
		//  
		//
		// the following was sql taken from the AM code for this report
		//	
		// Select All Transactions from selected CASHWSID's since Last Close Out
		// SELECT  H.CASHWSID, 'VOIDED', C.ITMCDDESC, B.INVITMYR, SUM(B.INVQTY)  
		// FROM RTS.RTS_TRANS A, RTS.RTS_TR_INV_DETAIL B, RTS.RTS_ITEM_CODES C,RTS.RTS_TRANS_HDR H 
		// WHERE A.TRANSWSID = B.TRANSWSID AND  A.TRANSCD ='INVVD' AND  A.TRANSAMDATE = B.TRANSAMDATE AND A.CUSTSEQNO = 
		//   B.CUSTSEQNO AND  A.TRANSTIME = B.TRANSTIME AND B.ITMCD=C.ITMCD AND B.DELINVREASNCD = 5 AND  A.TRANSAMDATE = 
		//   H.TRANSAMDATE AND A.TRANSWSID = H.TRANSWSID AND A.CUSTSEQNO = H.CUSTSEQNO AND   B.INVQTY>=1  AND H.CASHWSID IN 
		//   ( 100)  AND (  (H.CASHWSID=100 AND  H.TRANSTIMESTMP BETWEEN '2000-05-03-11.53.53.000000'  AND '2001-05-07-  
		//   4.46.18.000000' ) ) GROUP BY H.CASHWSID, C.ITMCDDESC, B.INVITMYR
		// Select All Transactions from all CASHWSID's since Last Close Out 
		// SELECT  H.CASHWSID, 'VOIDED', C.ITMCDDESC, B.INVITMYR, SUM(B.INVQTY)  
		// FROM RTS.RTS_TRANS A, RTS.RTS_TR_INV_DETAIL B, RTS.RTS_ITEM_CODES C,RTS.RTS_TRANS_HDR H 
		// WHERE A.TRANSWSID = B.TRANSWSID AND  A.TRANSCD ='INVVD' AND  A.TRANSAMDATE = B.TRANSAMDATE AND A.CUSTSEQNO = 
		//   B.CUSTSEQNO AND  A.TRANSTIME = B.TRANSTIME AND   B.ITMCD=C.ITMCD AND  B.DELINVREASNCD = 5 AND  A.TRANSAMDATE = 
		//   H.TRANSAMDATE AND A.TRANSWSID = H.TRANSWSID AND A.CUSTSEQNO = H.CUSTSEQNO AND   B.INVQTY>=1  AND (  (H.CASHWSID=0 
		//   AND  H.TRANSTIMESTMP BETWEEN '2001-05-10-00.00.00.000000'  AND '2001-05-17-09.30.54.000000' ) OR  (H.CASHWSID=100 
		//   AND  H.TRANSTIMESTMP BETWEEN '2000-05-03-11.53.53.000000'  AND '2001-05-07-14.46.18.000000' ) OR  (H.CASHWSID=200 
		//   AND  H.TRANSTIMESTMP BETWEEN '2001-05-10-00.00.00.000000'  AND '2001-05-17-09.30.54.000000' ) ) GROUP BY H.CASHWSID, 
		//   C.ITMCDDESC, B.INVITMYR
		// the following happens prior to the select statement
		// See the class files documentation
		/* 4) User can select Cash Drawer OR Employee Or Substation
			 * 5) User can also Select a Primary split of report by:
			 *    None, Cash Drawer, or employee  
			 * 6) There is also a "Show source of money" check box.
			 * 7) User makes their choices and clicks on the Enter button.
			 * 8) A list of cash drawers appears with the Cash Drawer choice
		*/
		// Generating Demo data to display.
		String lsQuery =
			"SELECT  H.CASHWSID, 'VOIDED', C.ITMCDDESC, B.INVITMYR, SUM(B.INVQTY)"
				+ " FROM RTS.RTS_TRANS A, RTS.RTS_TR_INV_DETAIL B, RTS.RTS_ITEM_CODES C,RTS.RTS_TRANS_HDR H"
				+ " WHERE A.TRANSWSID = B.TRANSWSID AND  A.TRANSCD ='INVVD' AND  A.TRANSAMDATE = B.TRANSAMDATE AND A.CUSTSEQNO = "
				+ "   B.CUSTSEQNO AND  A.TRANSTIME = B.TRANSTIME AND B.ITMCD=C.ITMCD AND B.DELINVREASNCD = 5 AND  A.TRANSAMDATE = "
				+ "   H.TRANSAMDATE AND A.TRANSWSID = H.TRANSWSID AND A.CUSTSEQNO = H.CUSTSEQNO AND   B.INVQTY>=1  AND H.CASHWSID IN "
				+ "   ( 100)  AND (  (H.CASHWSID=100 AND  H.TRANSTIMESTMP BETWEEN '2000-05-03-11.53.53.000000'  AND '2001-05-07- "
				+ "   4.46.18.000000' ) ) GROUP BY H.CASHWSID, C.ITMCDDESC, B.INVITMYR";
		Vector lvQueryResults = laISR.queryData(lsQuery);
		laISR.formatReport(lvQueryResults);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\INVSUMMARY.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laISR.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\INVSUMMARY.RPT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\TitlePkgRpt.txt prn");
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
	 * InventorySummaryReport constructor
	 */
	public InventorySummaryReport()
	{
		super();
	}

	/**
	 * InventorySummaryReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public InventorySummaryReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * createHeader
	 * 
	 * @param asEntityId String 
	 * @param aaFundsData FundsData
	 * @return Vector
	 */
	public Vector createHeader(
		String asEntityId,
		FundsData aaFundsData)
	{
		FundsReportData laFundsReportData =
			aaFundsData.getFundsReportData();
		Vector lvHeader = new Vector();
		String lsFromRange = "";
		String lsToRange = "";
		switch (laFundsReportData.getEntity())
		{
			case (FundsConstant.CASH_DRAWER) :
				{
					Vector lvSelectedCashDrawers =
						(Vector) aaFundsData.getSelectedCashDrawers();
					if ((csReportTotalTxt == REPORT_TOTAL_TXT)
						&& (lvSelectedCashDrawers.size() > 1))
					{
						csEntity = "CASH DRAWER(S)";
					}
					else
					{
						csEntity = "CASH DRAWER";
					}
					lvHeader.addElement(csEntity);
					lvHeader.addElement(asEntityId);
					for (int i = 0;
						i < lvSelectedCashDrawers.size();
						i++)
					{
						CashWorkstationCloseOutData laDrawer =
							(
								CashWorkstationCloseOutData) lvSelectedCashDrawers
									.get(
								i);
						String lsCashWsId =
							Integer.toString(laDrawer.getCashWsId());
						if (lsCashWsId.equals(asEntityId))
						{
							if (laFundsReportData.getRange()
								!= FundsConstant.DATE_RANGE)
							{
								lsFromRange =
									laDrawer
										.getCloseOutBegTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutBegTstmp()
											.getClockTimeNoMs();
								//.getClockTime()
								//.substring(0,8);
								lsToRange =
									laDrawer
										.getCloseOutEndTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutEndTstmp()
											.getClockTimeNoMs();
								//.getClockTime()
								//.substring(0,8);
							}
							laDrawer.setRptStatus(
								FundsConstant.REPORT_GENERATED);
							//laDrawer.setRptStatus("Report Generated");
							break;
						}
					}
					break;
				}
			case (FundsConstant.EMPLOYEE) :
				{
					Vector lvSelectedEmployees =
						(Vector) aaFundsData.getSelectedEmployees();

					if ((csReportTotalTxt == REPORT_TOTAL_TXT)
						&& (lvSelectedEmployees.size() > 1))
					{
						csEntity = "EMPLOYEE ID(S)";
					}
					else
					{
						csEntity = "EMPLOYEE ID";
					}

					lvHeader.addElement(csEntity);
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
						if (lsEmployeeId.equals(asEntityId))
						{
							laEmployeeObj.setRptStatus(
							// defect 9943
							FundsConstant.REPORT_GENERATED);
							//	"Report Generated");
							// end defect 9943 
							break;
						}
					}
					break;
				}
			default :
				{
					// empty code block
				}
		}
		lvHeader.addElement("REPORT TYPE");

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
					lsFromRange =
						laFundsReportData.getFromRange().toString()
							+ " "
							+ laFundsReportData
								.getFromRange()
								.getClockTimeNoMs();
					//.getClockTime().substring(0,8);

					lsToRange =
						laFundsReportData.getToRange().toString()
							+ " "
							+ laFundsReportData
								.getToRange()
								.getClockTimeNoMs();
					//.getClockTime().substring(0,8);
					// end defect 9943 

					lvHeader.addElement("TRANSACTIONS FROM");
					lvHeader.addElement(
						lsFromRange + " THROUGH " + lsToRange);
					break;
				}
			default :
				{
					lvHeader.addElement(NOT_DEFINED);
				}
		}
		if (laFundsReportData.getRange() != FundsConstant.DATE_RANGE
			&& !cbGenSumPage)
		{
			if (laFundsReportData.getEntity()
				!= FundsConstant.SUBSTATION
				|| laFundsReportData.getEntity()
					!= FundsConstant.COUNTYWIDE)
			{
				if (laFundsReportData.getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					lvHeader.addElement("TRANSACTIONS FROM");
					lvHeader.addElement(
						lsFromRange + " THROUGH " + lsToRange);
				}
			}
		}
		return lvHeader;
	}

	/**
	 * formatReport(Vector)
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * formatReport(Vector, FundsData)
	 * 
	 * @param avResults Vector
	 * @param aaFundsData FundsData
	 */
	public void formatReport(Vector avResults, FundsData aaFundsData)
	{
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		Vector lvColumn1 = new Vector();
		Vector lvColumn2 = new Vector();
		boolean lbMatchFound;
		boolean lbPrintSummaryPage = false;
		caInvSStatus.setReportName(FundsConstant.INVENTORYS_REPORT);
		this.caRptProps.setOfficeIssuanceName(
			caRptProps.getOfficeIssuanceName());
		this.caRptProps.setSubstationName(
			caRptProps.getSubstationName());
		this.caRptProps.setWorkstationId(caRptProps.getWorkstationId());
		lvHeader = createHeader("", aaFundsData);
		if (aaFundsData.getFundsReportData().getPrimarySplit()
			== FundsConstant.CASH_DRAWER)
		{
			ColumnHeader laColumn0 =
				new ColumnHeader(
					SPLITBYCASHD_HEADER,
					0,
					SPLITBY_LENGTH);
			lvColumn1.addElement(laColumn0);
		}
		else
		{
			if (aaFundsData.getFundsReportData().getPrimarySplit()
				== FundsConstant.EMPLOYEE)
			{
				ColumnHeader laColumn0 =
					new ColumnHeader(
						SPLITBYEMP_HEADER,
						0,
						SPLITBY_LENGTH);
				lvColumn1.addElement(laColumn0);
			}
		}
		//LOADING COLUMN HEADERS
		ColumnHeader laColumn1 =
			new ColumnHeader(
				ITEM_HEADER,
				ITEMDESC_STARTPT,
				ITEMDESC_LENGTH);
		ColumnHeader laColumn2 =
			new ColumnHeader(
				ITEM_HEADER,
				ITEMYEAR_STARTPT,
				ITEMYEAR_LENGTH);
		ColumnHeader laColumn3 =
			new ColumnHeader(
				QUANTITY_HEADER,
				QTYSOLD_STARTPT,
				QTYSOLD_LENGTH);
		laColumn3.setAlignment(2);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				QUANTITY_HEADER,
				QTYVOIDED_STARTPT,
				QTYVOIDED_LENGTH);
		laColumn4.setAlignment(2);
		ColumnHeader laColumn5 =
			new ColumnHeader(
				QUANTITY_HEADER,
				QTYREUSED_STARTPT,
				QTYREUSED_LENGTH);
		laColumn5.setAlignment(2);
		ColumnHeader laColumn5B =
			new ColumnHeader(
				QUANTITY_HEADER,
				QTYREPRINTED_STARTPT,
				REPRINTED_HEADER.length());
		laColumn5B.setAlignment(2);
		ColumnHeader laColumn6 =
			new ColumnHeader(
				DESC_HEADER,
				ITEMDESC_STARTPT,
				ITEMDESC_LENGTH);
		ColumnHeader laColumn7 =
			new ColumnHeader(
				YEAR_HEADER,
				ITEMYEAR_STARTPT,
				ITEMYEAR_LENGTH);
		ColumnHeader laColumn8 =
			new ColumnHeader(
				SOLD_HEADER,
				QTYSOLD_STARTPT,
				QTYSOLD_LENGTH);
		laColumn8.setAlignment(2);
		ColumnHeader laColumn9 =
			new ColumnHeader(
				VOIDED_HEADER,
				QTYVOIDED_STARTPT,
				QTYVOIDED_LENGTH);
		laColumn9.setAlignment(2);
		ColumnHeader laColumn10 =
			new ColumnHeader(
				REUSED_HEADER,
				QTYREUSED_STARTPT,
				QTYREUSED_LENGTH);
		laColumn10.setAlignment(2);
		ColumnHeader laColumn11 =
			new ColumnHeader(
				REPRINTED_HEADER,
				QTYREPRINTED_STARTPT,
				REPRINTED_HEADER.length());
		laColumn11.setAlignment(2);
		lvColumn1.addElement(laColumn1);
		lvColumn1.addElement(laColumn2);
		lvColumn1.addElement(laColumn3);
		lvColumn1.addElement(laColumn4);
		lvColumn1.addElement(laColumn5);
		lvColumn1.addElement(laColumn5B);
		lvColumn2.addElement(laColumn6);
		lvColumn2.addElement(laColumn7);
		lvColumn2.addElement(laColumn8);
		lvColumn2.addElement(laColumn9);
		lvColumn2.addElement(laColumn10);
		lvColumn2.addElement(laColumn11);
		lvTable.addElement(lvColumn1);
		lvTable.addElement(lvColumn2);

		FundsServerBusiness laFundsServerBusiness =
			new FundsServerBusiness();

		// get the data based on entity and splitby
		avResults =
			laFundsServerBusiness.sortInventoryResults(aaFundsData);
		InventoryData laDataLine = new InventoryData();
		int i = 0;
		if (!(avResults == null) && (avResults.size() > 0))
		{
			// the following code is to set the current and previous
			// break
			// point field to be the same, so we don't have to
			// check for previous
			// record not being null everytime we check for a break.
			laDataLine = (InventoryData) avResults.elementAt(i);
			String lsPrevCashWsId =
				Integer.toString(laDataLine.getCashWsId());
			String lsPrevTransEmpId = laDataLine.getTransEmpId();
			while (i < avResults.size())
			{
				//Loop through the results
				laDataLine = (InventoryData) avResults.elementAt(i);
				if (csEntity == "CASH DRAWER")
				{
					lvHeader =
						createHeader(
							Integer.toString(laDataLine.getCashWsId()),
							aaFundsData);
				}
				else
				{
					if (csEntity == "EMPLOYEE ID")
					{
						lvHeader =
							createHeader(
								laDataLine.getTransEmpId(),
								aaFundsData);
					}
				}
				generateHeader(lvHeader, lvTable);
				int j = getNoOfDetailLines() - 2;

				forloop : for (int k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						// System.out.println("k " + k);
						laDataLine =
							(InventoryData) avResults.elementAt(i);
						// check for an entity break first
						// - for a new page 
						if (((aaFundsData
							.getFundsReportData()
							.getEntity()
							== FundsConstant.CASH_DRAWER)
							&& (!Integer
								.toString(laDataLine.getCashWsId())
								.equals(lsPrevCashWsId)))
							|| ((aaFundsData
								.getFundsReportData()
								.getEntity()
								== FundsConstant.EMPLOYEE)
								&& (!laDataLine
									.getTransEmpId()
									.equals(lsPrevTransEmpId))))
						{
							cbMultipleEntity = true;
							if (cbSplitby)
							{
								if ((cvInventoryEntityTotalLine.size()
									> 0)
									&& (cvInventoryEntityTotalLine
										!= null))
								{
									UtilityMethods.sort(
										cvInventoryEntityTotalLine);
								}
								for (int l = 0;
									l
										< cvInventoryEntityTotalLine
											.size();
									l++)
								{
									InventoryData laInventoryData =
										(
											InventoryData) cvInventoryEntityTotalLine
												.elementAt(
											l);
									if (l == 0)
									{
										this.caRpt.nextLine();
										k++;
										if (csEntity == "CASH DRAWER")
										{
											this.caRpt.print(
												CASH_DRAWER_TOTAL_TXT,
												0,
												ENTITY_TOTAL_TXT_LENGTH);
											this.caRpt.print(
												lsPrevCashWsId,
												ENTITY_TXT_STARTPT,
												ENTITY_TXT_LENGTH);
											caRpt.nextLine();
										}
										else
										{
											this.caRpt.print(
												EMPLOYEE_TOTAL_TXT,
												ENTITY_TOTAL_TXT_STARTPT,
												ENTITY_TOTAL_TXT_LENGTH);
											this.caRpt.print(
												lsPrevTransEmpId,
												ENTITY_TXT_STARTPT,
												ENTITY_TXT_LENGTH);
										} //end else
									} //end if (l == 0)
									printSummaryLineTotal(laInventoryData);
									k++;
									if (k >= j)
									{
										generateFooter();
										generateHeader(
											lvHeader,
											lvTable);
										k = 0;
									}
								} // END for (int l = 0;
							} // END if (cbSplitby)
							cvInventoryEntityTotalLine.clear();
							lbPrintSummaryPage = true;
							cbSplitby = false;
							lsPrevCashWsId =
								Integer.toString(
									laDataLine.getCashWsId());
							lsPrevTransEmpId =
								laDataLine.getTransEmpId();
							break forloop;
							// break to top of while loop to generate
							// new page headers
						} // END complex if condition
						// add up quantity sold, quantity voided, and
						// quantity reused
						// when lDataLine.getItmCdDesc() and
						// getInvItmYr() are equal. Otherwise, 
						// instantiate and load these fields into a
						// new InventoryData 
						// object - for comparison and accumlation
						// Do this for entity total and report 
						// *****for report total page, add each record
						// to the unique desc-yr array ***********
						// add each dataline to the
						// cvInventoryReportTotalLine vector
						// for printing
						lbMatchFound = false;
						if (!(cvInventoryReportTotalLine == null)
							&& (cvInventoryReportTotalLine.size() > 0))
						{
							for (int l = 0;
								l < cvInventoryReportTotalLine.size();
								l++)
							{
								InventoryData laInventoryData =
									(
										InventoryData) cvInventoryReportTotalLine
											.elementAt(
										l);
								if ((laDataLine
									.getItmCdDesc()
									.equals(
										laInventoryData
											.getItmCdDesc()))
									&& (laDataLine.getInvItmYr()
										== laInventoryData.getInvItmYr()))
								{
									laInventoryData.setTotQtyReused(
										laDataLine.getQtyReused());
									laInventoryData.setTotQtySold(
										laDataLine.getQtySold());
									laInventoryData.setTotQtyVoided(
										laDataLine.getQtyVoided());
									laInventoryData.setTotQtyReprnted(
										laDataLine.getQtyReprnted());
									laInventoryData.setKey1(
										laDataLine.getItmCdDesc());
									laInventoryData.setKey2(
										Integer.toString(
											laDataLine.getInvItmYr()));
									cvInventoryReportTotalLine
										.setElementAt(
										laInventoryData,
										l);
									lbMatchFound = true;
									break;
								} //end if
							} // end for
							//load the cvInventoryReportTotalLine vector
							//with an InventoryData line object
							//called InventoryData, using the following
							//uniqueDescYear method.
							if (!lbMatchFound)
							{
								uniqueDescYearTotal(laDataLine);
							}
						}
						else
						{
							uniqueDescYearTotal(laDataLine);
						}
						//*****for entity total add each record within
						// an entity to the unique desc-yr array *****
						lbMatchFound = false;
						if (!(cvInventoryEntityTotalLine == null)
							&& (cvInventoryEntityTotalLine.size() > 0))
						{
							for (int l = 0;
								l < cvInventoryEntityTotalLine.size();
								l++)
							{
								InventoryData laInventoryData =
									(
										InventoryData) cvInventoryEntityTotalLine
											.elementAt(
										l);
								if ((laDataLine
									.getItmCdDesc()
									.equals(
										laInventoryData
											.getItmCdDesc()))
									&& (laDataLine.getInvItmYr()
										== laInventoryData.getInvItmYr()))
								{
									laInventoryData.setTotQtyReused(
										laDataLine.getQtyReused());
									laInventoryData.setTotQtySold(
										laDataLine.getQtySold());
									laInventoryData.setTotQtyVoided(
										laDataLine.getQtyVoided());
									laInventoryData.setTotQtyReprnted(
										laDataLine.getQtyReprnted());
									laInventoryData.setKey1(
										laDataLine.getItmCdDesc());
									laInventoryData.setKey2(
										Integer.toString(
											laDataLine.getInvItmYr()));
									cvInventoryEntityTotalLine
										.setElementAt(
										laInventoryData,
										l);
									lbMatchFound = true;
									break;
								}
							} // end for (int l = 0;
							// load the cvInventoryEntityTotalLine
							// vector with an InventoryData line object
							// called InventoryData, using the following
							// uniqueDescYear method.
							if (!lbMatchFound)
							{
								uniqueDescYear(laDataLine);
							}
						} // end if (!(
						else
						{
							uniqueDescYear(laDataLine);
						}
						// if primary split is by cashdrawer (CashWsId),
						// the entity must be employee
						// so we need to check for a difference in
						// employee too
						if (aaFundsData
							.getFundsReportData()
							.getPrimarySplit()
							== FundsConstant.CASH_DRAWER)
						{
							if ((!Integer
								.toString(laDataLine.getCashWsId())
								.equals(lsPrevCashWsId))
								|| (!laDataLine
									.getTransEmpId()
									.equals(lsPrevTransEmpId)))
							{
								lbPrintSummaryPage = true;
							}
							if (!Integer
								.toString(laDataLine.getCashWsId())
								.equals(lsPrevCashWsId))
							{
								cbSplitby = true;
								if (k > 0)
								{
									this.caRpt.nextLine();
									k++;
								}
							}
							if ((!Integer
								.toString(laDataLine.getCashWsId())
								.equals(lsPrevCashWsId))
								|| (k == 0))
							{
								this
									.caRpt
									.print(
										Integer.toString(
											laDataLine.getCashWsId()),
										0,
								// PCR 34 SPLITBY_STARTPT,
								SPLITBY_LENGTH);
							}
						}
						else
						{
							// if primary split is by employee, the
							// entity must be cashdrawer (CashWsId)
							// so we need to check for a difference in
							// cashdrawer too
							if (aaFundsData
								.getFundsReportData()
								.getPrimarySplit()
								== FundsConstant.EMPLOYEE)
							{
								if ((!laDataLine
									.getTransEmpId()
									.equals(lsPrevTransEmpId))
									|| (!Integer
										.toString(
											laDataLine.getCashWsId())
										.equals(lsPrevCashWsId)))
								{
									lbPrintSummaryPage = true;
								}
								if (!laDataLine
									.getTransEmpId()
									.equals(lsPrevTransEmpId))
								{
									cbSplitby = true;
									if (k > 0)
									{
										this.caRpt.nextLine();
										k++;
									}
								}
								if ((!laDataLine
									.getTransEmpId()
									.equals(lsPrevTransEmpId))
									|| (k == 0))
								{
									this
										.caRpt
										.print(
											laDataLine.getTransEmpId(),
											0,
									/* PCR 34 SPLITBY_STARTPT,*/
									SPLITBY_LENGTH);
								}
							}
						}
						printSummaryLine(laDataLine);
						lsPrevCashWsId =
							Integer.toString(laDataLine.getCashWsId());
						lsPrevTransEmpId = laDataLine.getTransEmpId();
						i = i + 1;
					}
					else
					{
						break;
					}
				} // end forloop : for (int k = 0; k <= j; k++)
				if (i >= avResults.size() - 1) // no more data
				{
					if (cbSplitby && cbMultipleEntity)
					{
						// may have to handle a page overrun here -
						// would need to pick up where the local k
						// variable left off
						// put rest of code in a loop like this for
						for (int l = 0;
							l < cvInventoryEntityTotalLine.size();
							l++)
						{
							InventoryData laInventoryData =
								(
									InventoryData) cvInventoryEntityTotalLine
										.elementAt(
									l);
							if (l == 0)
							{
								this.caRpt.nextLine();
								if (csEntity == "CASH DRAWER")
								{
									this.caRpt.print(
										CASH_DRAWER_TOTAL_TXT,
										0,
										ENTITY_TOTAL_TXT_LENGTH);
									this.caRpt.print(
										lsPrevCashWsId,
										ENTITY_TXT_STARTPT,
										ENTITY_TXT_LENGTH);
								}
								else
								{
									this.caRpt.print(
										EMPLOYEE_TOTAL_TXT,
										0,
										ENTITY_TOTAL_TXT_LENGTH);
									this.caRpt.print(
										lsPrevTransEmpId,
										ENTITY_TXT_STARTPT,
										ENTITY_TXT_LENGTH);
								} //end else
							} // end if
							printSummaryLineTotal(laInventoryData);
						} // end if (l == 0)
					} // end if (cbSplitby && cbMultipleEntity)
					// moved the (lbPrintSummaryPage) if block to after
					// => end while (i < avResults.size())
				} // end if (i >= avResults.size() - 1)
				
				// defect 8628 
				//if (!lbPrintSummaryPage)
				//{
				//	generateEndOfReport();
				//}
				// generateFooter();
				generateFooter(!lbPrintSummaryPage);
				// end defect 8628  
			} // end while (i < avResults.size())
			if (lbPrintSummaryPage)
			{
				//generateFooter();
				csReportTotalTxt = REPORT_TOTAL_TXT;
				generateSummaryPage(
					aaFundsData,
					lvHeader,
					lvTable,
					csReportTotalTxt);
			}
		} // end if (!(avResults == null) && (avResults.size() > 0))
		if (aaFundsData.getFundsReportData().getEntity()
			== FundsConstant.CASH_DRAWER)
		{
			for (i = 0;
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
					&& lsReportStatus.equals(
						FundsConstant.REPORT_GENERATED))
					//("Report Generated"))
				{
					laDrawer.setReportStatus(
						FundsConstant.REPORT_GENERATED);
					//"Report Generated");
				}
				else
				{
					laDrawer.setReportStatus(
						FundsConstant.NO_TRANSACTIONS);
					//"No Transactions");
				}
				// end defect 9943 
				cvInvSStatVec.add(laDrawer);
			}
		}
		if (aaFundsData.getFundsReportData().getEntity()
			== FundsConstant.EMPLOYEE)
		{
			for (i = 0;
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
						FundsConstant.REPORT_GENERATED))
					//"Report Generated"))
				{
					laEmployee.setReportStatus(
						FundsConstant.REPORT_GENERATED);
					//"Report Generated");
				}
				else
				{
					laEmployee.setReportStatus(
						FundsConstant.NO_TRANSACTIONS);
					//"No Transactions");
				}
				// end defect 9943 
				cvInvSStatVec.add(laEmployee);
			}
		}
	}

	/**
	 * generateAttributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * generateSummaryPage
	 * 
	 * @param aaFundsData FundsData
	 * @param avHeader Vector
	 * @param avColumnHeadings Vector
	 * @param asReportSummaryTxt String
	 */
	public void generateSummaryPage(
		FundsData aaFundsData,
		Vector avHeader,
		Vector avColumnHeadings,
		String asReportSummaryTxt)
	{
		// check to see if there is something to print
		cbGenSumPage = true;
		if (!(cvInventoryReportTotalLine == null)
			&& (cvInventoryReportTotalLine.size() > 0))
		{
			UtilityMethods.sort(cvInventoryReportTotalLine);
			InventoryData laDataLine = new InventoryData();
			String lsSelectedItems = new String();
			int i = 0;
			int j = getNoOfDetailLines() - 2;
			if (aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER)
			{
				for (i = 0;
					i < aaFundsData.getSelectedCashDrawers().size();
					i++)
				{
					CashWorkstationCloseOutData laCashDrwrObj =
						(CashWorkstationCloseOutData) aaFundsData
							.getSelectedCashDrawers()
							.elementAt(i);
					String lsReportStatus =
						laCashDrwrObj.getRptStatus();
					EntityStatusData laDrawer = new EntityStatusData();
					laDrawer.setCashWsId(laCashDrwrObj.getCashWsId());

					// defect 9943 
					if ((lsReportStatus != null)
						&& lsReportStatus.equals(
							FundsConstant.REPORT_GENERATED))
						//"Report Generated"))
					{
						// end defect 9943 
						lsSelectedItems =
							lsSelectedItems
								+ Integer.toString(
									laCashDrwrObj.getCashWsId())
								+ " ";
					}
				}
				avHeader = createHeader(lsSelectedItems, aaFundsData);
			}
			else if (
				aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.EMPLOYEE)
			{
				for (i = 0;
					i < aaFundsData.getSelectedEmployees().size();
					i++)
				{
					EmployeeData laEmployeeObj =
						(EmployeeData) aaFundsData
							.getSelectedEmployees()
							.elementAt(
							i);
					String lsReportStatus =
						laEmployeeObj.getRptStatus();
					EntityStatusData laEmployee =
						new EntityStatusData();
					laEmployee.setEmployeeId(
						laEmployeeObj.getEmployeeId());

					// defect 9943 
					if ((lsReportStatus != null)
						&& lsReportStatus.equals(
							FundsConstant.REPORT_GENERATED))
						//"Report Generated"))
					{
						// end defect 9943 
						lsSelectedItems =
							lsSelectedItems
								+ laEmployeeObj.getEmployeeId()
								+ " ";
					}
				}
				avHeader = createHeader(lsSelectedItems, aaFundsData);
			}
			// generateHeader(lvHeader, lvTable);
			generateHeader(avHeader, avColumnHeadings);
			if (asReportSummaryTxt != null)
			{
				this.caRpt.print(
					asReportSummaryTxt,
					TOTAL_TXT_STARTPT,
					TOTAL_TXT_LENGTH);
				this.caRpt.nextLine();
			}
			// start printing as long as there is still something to print
			i = 0;
			while (i < cvInventoryReportTotalLine.size())
			{
				forloop : for (int k = 0; k <= j; k++)
				{
					if (i < cvInventoryReportTotalLine.size())
					{
						laDataLine =
							(
								InventoryData) cvInventoryReportTotalLine
									.elementAt(
								i);
						printSummaryLineTotal(laDataLine);
						i = i + 1;
					}
					else
					{
						break;
					}
				} // end for
				// check for end of report	
				if (i >= cvInventoryReportTotalLine.size() - 1)
				{
					// defect 8628 
					// generateEndOfReport();
					// generateFooter();
					generateFooter(true);
					// end defect 8628  
				}
				else
				{
					generateFooter();
					generateHeader(avHeader, avColumnHeadings);
				}
			}
		}
	}

	/**
	 * Returns caInvSStatus
	 * 
	 * @return ReportStatusData
	 */
	public ReportStatusData getInvSStatus()
	{
		return caInvSStatus;
	}

	/**
	 * Returns cvInvSStatVec
	 * 
	 * @return Vector
	 */
	public Vector getInvSStatVec()
	{
		return cvInvSStatVec;
	}

	/**
	 * printSummaryLine
	 * 
	 * @param aaDataLine InventoryData
	 */
	private void printSummaryLine(InventoryData aaDataLine)
	{
		csInvItmYr = Integer.toString(aaDataLine.getInvItmYr());
		if (csInvItmYr.equals("0"))
		{
			csInvItmYr = "";
		}
		csTotQtySold = Integer.toString(aaDataLine.getQtySold());
		if (csTotQtySold.equals("0"))
		{
			csTotQtySold = "";
		}
		csTotQtyVoided = Integer.toString(aaDataLine.getQtyVoided());
		if (csTotQtyVoided.equals("0"))
		{
			csTotQtyVoided = "";
		}
		csTotQtyReused = Integer.toString(aaDataLine.getQtyReused());
		if (csTotQtyReused.equals("0"))
		{
			csTotQtyReused = "";
		}
		csTotQtyReprnted =
			Integer.toString(aaDataLine.getQtyReprnted());
		if (csTotQtyReprnted.equals("0"))
		{
			csTotQtyReprnted = "";
		}
		csTotQtyReprnted =
			UtilityMethods.addPadding(csTotQtyReprnted, 4, " ");
		this.caRpt.print(
			aaDataLine.getItmCdDesc(),
			ITEMDESC_STARTPT,
			ITEMDESC_LENGTH);
		this.caRpt.print(csInvItmYr, ITEMYEAR_STARTPT, ITEMYEAR_LENGTH);
		this.caRpt.rightAlign(
			csTotQtySold,
			QTYSOLD_STARTPT,
			QTYSOLD_LENGTH);
		this.caRpt.rightAlign(
			csTotQtyVoided,
			QTYVOIDED_STARTPT,
			QTYVOIDED_LENGTH);
		this.caRpt.rightAlign(
			csTotQtyReused,
			QTYREUSED_STARTPT,
			QTYREUSED_LENGTH);
		this.caRpt.rightAlign(
			csTotQtyReprnted,
			QTYREPRINTED_STARTPT + 5,
			csTotQtyReprnted.length());
		this.caRpt.nextLine();
	}

	/**
	 * printSummaryLineTotal
	 * 
	 * @param aaDataLine InventoryData
	 */
	private void printSummaryLineTotal(InventoryData aaDataLine)
	{
		csInvItmYr = Integer.toString(aaDataLine.getInvItmYr());
		if (csInvItmYr.equals("0"))
		{
			csInvItmYr = "";
		}
		csTotQtySold = Integer.toString(aaDataLine.getTotQtySold());
		if (csTotQtySold.equals("0"))
		{
			csTotQtySold = "";
		}
		csTotQtyVoided = Integer.toString(aaDataLine.getTotQtyVoided());
		if (csTotQtyVoided.equals("0"))
		{
			csTotQtyVoided = "";
		}
		csTotQtyReused = Integer.toString(aaDataLine.getTotQtyReused());
		if (csTotQtyReused.equals("0"))
		{
			csTotQtyReused = "";
		}

		csTotQtyReprnted =
			Integer.toString(aaDataLine.getTotQtyReprnted());
		if (csTotQtyReprnted.equals("0"))
		{
			csTotQtyReprnted = "";
		}
		csTotQtyReprnted =
			UtilityMethods.addPadding(csTotQtyReprnted, 4, " ");

		this.caRpt.print(
			aaDataLine.getItmCdDesc(),
			ITEMDESC_STARTPT,
			ITEMDESC_LENGTH);
		this.caRpt.print(csInvItmYr, ITEMYEAR_STARTPT, ITEMYEAR_LENGTH);
		this.caRpt.rightAlign(
			csTotQtySold,
			QTYSOLD_STARTPT,
			QTYSOLD_LENGTH);
		this.caRpt.rightAlign(
			csTotQtyVoided,
			QTYVOIDED_STARTPT,
			QTYVOIDED_LENGTH);
		this.caRpt.rightAlign(
			csTotQtyReused,
			QTYREUSED_STARTPT,
			QTYREUSED_LENGTH);
		this.caRpt.rightAlign(
			csTotQtyReprnted,
			QTYREPRINTED_STARTPT + 5,
			csTotQtyReprnted.length());
		this.caRpt.nextLine();
	}

	/**
	 * queryData
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		// The following data is being set in formatReport
		Vector lvData = new Vector();
		InventorySummaryReportData laDataLine1 =
			new InventorySummaryReportData();
		laDataLine1.setItmCdDesc("COMBINATION PLT");
		laDataLine1.setInvItmYr(2002);
		laDataLine1.setTotalItmQtySold(3);
		laDataLine1.setTotalItmQtyVoid(0);
		laDataLine1.setTotalItmQtyReuse(0);
		lvData.addElement(laDataLine1);
		InventorySummaryReportData laDataLine2 =
			new InventorySummaryReportData();
		laDataLine2.setItmCdDesc("DISABLED MOTORCYCLE PLT");
		laDataLine2.setInvItmYr(0);
		laDataLine2.setTotalItmQtySold(1);
		laDataLine2.setTotalItmQtyVoid(0);
		laDataLine2.setTotalItmQtyReuse(0);
		lvData.addElement(laDataLine2);
		InventorySummaryReportData laDataLine3 =
			new InventorySummaryReportData();
		laDataLine3.setItmCdDesc("FARM TRUCK PLT");
		laDataLine3.setInvItmYr(0);
		laDataLine3.setTotalItmQtySold(1);
		laDataLine3.setTotalItmQtyVoid(0);
		laDataLine3.setTotalItmQtyReuse(0);
		lvData.addElement(laDataLine3);
		InventorySummaryReportData laDataLine4 =
			new InventorySummaryReportData();
		laDataLine4.setItmCdDesc("FORM 31 RTS");
		laDataLine4.setInvItmYr(0);
		laDataLine4.setTotalItmQtySold(18);
		laDataLine4.setTotalItmQtyVoid(0);
		laDataLine4.setTotalItmQtyReuse(0);
		lvData.addElement(laDataLine4);
		InventorySummaryReportData laDataLine5 =
			new InventorySummaryReportData();
		laDataLine5.setItmCdDesc("PLATE STICKER");
		laDataLine5.setInvItmYr(2002);
		laDataLine5.setTotalItmQtySold(0);
		laDataLine5.setTotalItmQtyVoid(0);
		laDataLine5.setTotalItmQtyReuse(10);
		lvData.addElement(laDataLine5);
		InventorySummaryReportData laDataLine6 =
			new InventorySummaryReportData();
		laDataLine6.setItmCdDesc("WINDSHIELD STICKER");
		laDataLine6.setInvItmYr(2002);
		laDataLine6.setTotalItmQtySold(18);
		laDataLine6.setTotalItmQtyVoid(1);
		laDataLine6.setTotalItmQtyReuse(1);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		lvData.addElement(laDataLine6);
		return lvData;
	}

	/**
	 * uniqueDescYear
	 * 
	 * @param aaDataLine InventoryData
	 */
	private void uniqueDescYear(InventoryData aaDataLine)
	{
		InventoryData loInventoryData = new InventoryData();
		loInventoryData.setTransEmpId(aaDataLine.getTransEmpId());
		loInventoryData.setCashWsId(aaDataLine.getCashWsId());
		loInventoryData.setItmCdDesc(aaDataLine.getItmCdDesc());
		loInventoryData.setInvItmYr(aaDataLine.getInvItmYr());
		loInventoryData.setQtySold(aaDataLine.getQtySold());
		loInventoryData.setQtyVoided(aaDataLine.getQtyVoided());
		loInventoryData.setQtyReused(aaDataLine.getQtyReused());
		loInventoryData.setQtyReprnted(aaDataLine.getQtyReprnted());
		loInventoryData.setTotQtyReused(aaDataLine.getQtyReused());
		loInventoryData.setTotQtyVoided(aaDataLine.getQtyVoided());
		loInventoryData.setTotQtySold(aaDataLine.getQtySold());
		loInventoryData.setTotQtyReprnted(aaDataLine.getQtyReprnted());
		loInventoryData.setKey1(aaDataLine.getItmCdDesc());
		loInventoryData.setKey2(
			Integer.toString(aaDataLine.getInvItmYr()));
		cvInventoryEntityTotalLine.addElement(loInventoryData);
	}

	/**
	 * uniqueDescYearTotal
	 * 
	 * @param aaDataLine InventoryData
	 */
	private void uniqueDescYearTotal(InventoryData aaDataLine)
	{
		InventoryData laInventoryData = new InventoryData();
		laInventoryData.setTransEmpId(aaDataLine.getTransEmpId());
		laInventoryData.setCashWsId(aaDataLine.getCashWsId());
		laInventoryData.setItmCdDesc(aaDataLine.getItmCdDesc());
		laInventoryData.setInvItmYr(aaDataLine.getInvItmYr());
		laInventoryData.setQtySold(aaDataLine.getQtySold());
		laInventoryData.setQtyVoided(aaDataLine.getQtyVoided());
		laInventoryData.setQtyReused(aaDataLine.getQtyReused());
		laInventoryData.setQtyReprnted(aaDataLine.getQtyReprnted());
		laInventoryData.setTotQtyReused(aaDataLine.getQtyReused());
		laInventoryData.setTotQtyVoided(aaDataLine.getQtyVoided());
		laInventoryData.setTotQtySold(aaDataLine.getQtySold());
		laInventoryData.setTotQtyReprnted(aaDataLine.getQtyReprnted());
		laInventoryData.setKey1(aaDataLine.getItmCdDesc());
		laInventoryData.setKey2(
			Integer.toString(aaDataLine.getInvItmYr()));
		cvInventoryReportTotalLine.addElement(laInventoryData);
	}
}