package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;

/*
 *
 * GenInvDetailReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/07/2001	New Class
 * Min Wang   	09/21/2001	Add Constants for positioning
 * Min Wang		10/25/2001  Add FundsData to pass values
 *							from client side
 * Min Wang		05/01/2002	Increase DESC_LENGTH to 27.
 *							defect 3728
 * Min Wang		05/03/2002	Increase TOTAL_LENGTH to 40
 *							defect 3728
 * M Listberger	07/12/2002  Correct extra line space after deading.  
 *                          defect 4432.
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments
 * 							Added logic for Reprinted Inventory 
 *							modify ITEM_DESC_START_PT,
 *							ITEM_YEAR_START_PT, ITEM_NUM_START_PT
 *							add REPRINTED_TBL_HEADER2,
 *							REPRINTED_START_PT,	REPRINTED_LENGTH 
.*							modify formatReport(), totalOfItm()
 *		 					Ver 5.2.0	
 * K Harrell	04/26/2004	Modified column variables to move starting
 *							points over 3
 *							defect 5952 Ver 5.2.0
 * K Harrell	05/07/2004	Moved over Inventory Item No Column
 *							Reassigned ITM_NUM_DETAIL_START_PT and
 * 							ITM_NUM_DETAIL_START_PT 
 *							defect 7084 Ver 5.2.0
 * K Harrell	05/27/2004	removed ":" from CASH_DRAWER_HEADER,
 *							TRANS_FROM_HEADER, EMPLOYEE ID
 *							defect 7097  Ver 5.2.0
 * K Harrell	10/26/2004  Print Total Reprint Quantity even if 0
 *							modify totalOfItm()
 *							defect 7678  Ver 5.2.1 Fix 1
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/28/2005	Do not reference closeoutbegtstmp when
 * 							reporting by date range. All variables 
 * 							private. Rename cvInvDStatVec to 
 * 							cvInvDStatusVec. 
 * 							add getInvDStatusVec() 
 * 							modify createHeader()
 * 							defect 7896 Ver 5.2.3 
 * K Harrell	06/03/2009	Use getClockTimeNoMs(). Implement 
 * 							FundsConstant constants.  Add'l cleanup. 
 * 							modify createHeader(), formatReport() 
 * 							defect 9943 Ver Defect_POS_F 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport(Vector,FundsData) 
 * 							defect 8628 Ver Defect_POS_F   
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Inventory Detail Report.
 *
 * @version	Defect_POS_F 	08/10/2009
 * @author	Min Wang
 * <br>Creation Date: 		09/09/2001
 */
public class GenInvDetailReport extends ReportTemplate
{
	// boolean
	private boolean cbNewPageDone = false;

	// int 
	private int k;
	
	// Vector 
	private Vector cvInvDStatusVec = new Vector();

	// Object 
	 private ReportStatusData caInvDStatus = new ReportStatusData();
	 
	private static final int DESC_LENGTH = 27;
	private static final int END_OF_PAGE_WHITE_SPACE = 6;
	private static final int INV_CODE_LENGTH = 24;
	private static final int INV_CODE_START_PT = 109;
	private static final int ITEM_DESC_START_PT = 5;
	private static final int ITEM_LENGTH = 4;
	private static final int ITEM_NUM_START_PT = 45;
	private static final int ITEM_YEAR_START_PT = 33;
	private static final int ITM_NUM_DETAIL_LENGTH = 11;
	private static final int ITM_NUM_DETAIL_START_PT = 40;
	private static final int LENGTH_13 = 13;
	private static final int NUM_LENGTH = 6;
	private static final int OFC_NUM_DETAIL_START_PT = 55;
	private static final int ONE_LENGTH = 1;
	private static final int ONE_LINE = 1;
	private static final int REPRINTED_LENGTH = 9;
	private static final int REPRINTED_START_PT = 98;
	private static final int REUSED_LENGTH = 6;
	private static final int REUSED_START_PT = 91;
	private static final int SOLD_LENGTH = 5;
	private static final int SOLD_START_PT = 77;
	private static final int START_PT_1 = 1;
	private static final int THREE_LENGTH = 3;
	private static final int TOTAL_LENGTH = 40;
	private static final int TOTAL_START_PT = 37;
	private static final int TR_AM_DATE_DETAIL_LENGTH = 5;
	private static final int TR_AM_DATE_DETAIL_START_PT = 61;
	private static final int TR_ID_DETAIL_LENGTH = 3;
	private static final int TR_TIME_DETAIL_LENGTH = 6;
	private static final int TR_TIME_DETAIL_START_PT = 66;
	private static final int TR_WSID_DETAIL_START_PT = 58;
	private static final int TRANS_ID_LENGTH = 14;
	private static final int TRANS_ID_START_PT = 56;
	private static final int VOIDED_LENGTH = 6;
	private static final int VOIDED_START_PT = 83;
	private static final int YEAR_LENGTH = 4;

	private static final String CASH_DRAWER_HEADER = "CASH DRAWER";
	private static final String CLOSE_AFTER_SUBSTA_SUM =
		"CLOSEOUTS AFTER SUBSTATION SUMMARY";
	private static final String DATE_RANGE = "DATE RANGE";
	private static final String DESC_TBL_HEADER2 = "DESCRIPTION";
	private static final String EMPLOYEE_ID_HEADER = "EMPLOYEE ID";
	private static final String FOR_CLOSE = "FOR CLOSEOUT";
	private static final String INV_CODE_TBL_HEADER2 = "INVENTORY CODE";
	private static final String INVVD = "INVVD";
	private static final String ITEM_TBL_HEADER1 = "ITEM";
	private static final String LAST_CLOSEOUT = "SINCE LAST CLOSEOUT";
	private static final String LAST_CURRENT_STATUS =
		"SINCE LAST CURRENT STATUS";
	private static final String NOT_DEFINED = "REPORT TYPE NOT DEFINED";
	private static final String NUM_TBL_HEADER2 = "NUMBER";
	private static final String ONE_DASH = "-";
	private static final String REPORT_TYPE_HEADER = "REPORT TYPE";
	private static final String REPRINTED_TBL_HEADER2 = "REPRINTED";
	private static final String REUSE = "R";
	private static final String REUSE_VOIDED =
		"-REUSE VOIDED INVENTORY";
	private static final String REUSED_TBL_HEADER2 = "REUSED";
	private static final String SLD = "S";
	private static final String SOLD_TBL_HEADER2 = "SOLD";
	private static final String TOTAL = "TOTAL ";
	private static final String TRANS_FROM_HEADER = "TRANSACTION FROM ";
	private static final String TRANS_ID_TBL_HEADER2 = "TRANSACTION ID";
	private static final String U_ITEM = "U-ITEM NUMBER NOT FOUND";
	private static final String UNAVAILABLE = "X-SERVER UNAVAILABLE";
	private static final String UNKNOWN = "**UNKNOWN**";
	private static final String VOID = "V";
	private static final String VOIDED = "V";
	private static final String VOIDED_TBL_HEADER2 = "VOIDED";
	private static final String YEAR_TBL_HEADER2 = "YEAR";

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] an array of command-line arguments
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		CacheManager.loadCache();
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5161");
		GenInvDetailReport laGenRpt =
			new GenInvDetailReport(
				"INVENTORY DETAIL REPORT",
				laRptProps);
		//default values
		FundsReportData laFundsReportData = new FundsReportData();
		laFundsReportData.setEntity(FundsConstant.CASH_DRAWER);
		laFundsReportData.setPrimarySplit(FundsConstant.NONE);
		laFundsReportData.setRange(FundsConstant.SINCE_CLOSE);
		laFundsReportData.setFromRange(new RTSDate());
		laFundsReportData.setToRange(new RTSDate());
		if (aarrArgs.length > 0)
			// args[0] contains indicator for entity break 
			// 50 is WsId
			// 51 is EmpId
			// 53 is none
		{
			laFundsReportData.setEntity(Integer.parseInt(aarrArgs[0]));
		}
		if (aarrArgs.length > 1)
			// args[1] contains indicator for primary split break. 
			// 50 is WsId
			// 51 is EmpId
			// 53 is none
		{
			laFundsReportData.setPrimarySplit(
				Integer.parseInt(aarrArgs[1]));
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
		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryResults = laGenRpt.queryData(lsQuery);
		laGenRpt.formatReport(lvQueryResults, laFundsData);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\WKS#INVD.RPT");
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
				new FrmPreviewReport("c:\\WKS#INVD.RPT");
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
			// Process p = Runtime.getRuntime().exec("cmd.exe
			// /c copy c:\\TitlePkgRpt.txt prn");
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
	 * GenInvDetailRepor constructor
	 */
	public GenInvDetailReport()
	{
		super();
	}

	/**
	 * GenInvDetailReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenInvDetailReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Create the Report's Header.
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
			(FundsReportData) aaFundsData.getFundsReportData();
		Vector lvHeader = new Vector();
		String lsFromRange = null;
		String lsToRange = null;

		switch (laFundsReportData.getEntity())
		{
			case (FundsConstant.CASH_DRAWER) :

				lvHeader.addElement(CASH_DRAWER_HEADER);
				lvHeader.addElement(asEntityId);
				Vector lvSelectedCashDrawers =
					(Vector) aaFundsData.getSelectedCashDrawers();

				int i = 0;

				if (laFundsReportData.getRange()
					!= FundsConstant.DATE_RANGE)
				{
					while (i < lvSelectedCashDrawers.size())
					{
						CashWorkstationCloseOutData laDrawer =
							(
								CashWorkstationCloseOutData) lvSelectedCashDrawers
									.get(
								i);
						String CashWsId =
							Integer.toString(laDrawer.getCashWsId());
						if (CashWsId.equals(asEntityId))
						{
							// defect 9943 
							lsFromRange =
								laDrawer
									.getCloseOutBegTstmp()
									.toString()
									+ " "
									+ laDrawer
										.getCloseOutBegTstmp()
										.getClockTimeNoMs();
							//.getClockTime().substring(0,8);
							lsToRange =
								laDrawer
									.getCloseOutEndTstmp()
									.toString()
									+ " "
									+ laDrawer
										.getCloseOutEndTstmp()
										.getClockTimeNoMs();
							//.getClockTime().substring(0,8);
							// end defect 9943 
							i = lvSelectedCashDrawers.size();
						}
						else
						{
							i++;
						}
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
						//laCashDrwrObj.setRptStatus("Report Generated");
						laCashDrwrObj.setRptStatus(
							FundsConstant.REPORT_GENERATED);
						break;
						// end defect 9943 
					}
				}
				break;

			case (FundsConstant.EMPLOYEE) :
				{
					lvHeader.addElement(EMPLOYEE_ID_HEADER);
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
								FundsConstant.REPORT_GENERATED);
							//"Report Generated");
							// end defect 9943  
							break;
						}
					}
					break;
				}
				// do not add this section to the header
			default :
				{
					// empty code block
				}
		}
		lvHeader.addElement(REPORT_TYPE_HEADER);

		// Determine what the report type is
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
					// defect 9943 
					lvHeader.addElement(DATE_RANGE);
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
					break;
				}
			default :
				{
					lvHeader.addElement(NOT_DEFINED);
				}
		}
		// normal transaction to and from headers
		if (laFundsReportData.getEntity() == FundsConstant.CASH_DRAWER
			|| laFundsReportData.getRange() == FundsConstant.DATE_RANGE)
		{
			lvHeader.addElement(TRANS_FROM_HEADER);
			lvHeader.addElement(lsFromRange + " THROUGH " + lsToRange);
		}
		return lvHeader;
	}

	/**
	 * formatReport method
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * Format the Inventory Detail Report.
	 * <p>There are three levels of breaks in this report.
	 * The Query takes care of ordering the data in the proper order.
	 * <ol><li>The entityId break determines page breaks if needed.
	 * It can be by Cash Workstation or by Employee Id.
	 * <li>The primary split break is optional.  It can be off,
	 * Employee Id, or Cash Workstation.  This level groups data on the
	 * page. <li>The lowest level break is by Inventory Item.
	 * The only totals are carried at this level.
	 * </ol>
	 * 
	 * @param avResults Vector
	 * @param aaFundsData FundsData
	 * @throws RTSException
	 */
	public void formatReport(Vector avResults, FundsData aaFundsData)
		throws RTSException
	{
		FundsReportData laFundsReportData =
			(FundsReportData) aaFundsData.getFundsReportData();
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector(); //adding first row table header
		ColumnHeader laColumnA1 =
			new ColumnHeader(
				ITEM_TBL_HEADER1,
				ITEM_DESC_START_PT,
				ITEM_LENGTH);
		ColumnHeader laColumnA2 =
			new ColumnHeader(
				ITEM_TBL_HEADER1,
				ITEM_YEAR_START_PT,
				ITEM_LENGTH);
		ColumnHeader laColumnA3 =
			new ColumnHeader(
				ITEM_TBL_HEADER1,
				ITEM_NUM_START_PT,
				ITEM_LENGTH);
		lvRow1.addElement(laColumnA1);
		lvRow1.addElement(laColumnA2);
		lvRow1.addElement(laColumnA3);
		lvTable.addElement(lvRow1);
		Vector lvRow2 = new Vector(); //adding second row talbe header
		ColumnHeader laColumnB1 =
			new ColumnHeader(
				DESC_TBL_HEADER2,
				ITEM_DESC_START_PT,
				DESC_LENGTH);
		ColumnHeader laColumnB2 =
			new ColumnHeader(
				YEAR_TBL_HEADER2,
				ITEM_YEAR_START_PT,
				YEAR_LENGTH);
		ColumnHeader laColumnB3 =
			new ColumnHeader(
				NUM_TBL_HEADER2,
				ITEM_NUM_START_PT,
				NUM_LENGTH);
		ColumnHeader laColumnB4 =
			new ColumnHeader(
				TRANS_ID_TBL_HEADER2,
				TRANS_ID_START_PT,
				TRANS_ID_LENGTH);
		ColumnHeader laColumnB5 =
			new ColumnHeader(
				SOLD_TBL_HEADER2,
				SOLD_START_PT,
				SOLD_LENGTH);
		ColumnHeader laColumnB6 =
			new ColumnHeader(
				VOIDED_TBL_HEADER2,
				VOIDED_START_PT,
				VOIDED_LENGTH);
		ColumnHeader laColumnB7 =
			new ColumnHeader(
				REUSED_TBL_HEADER2,
				REUSED_START_PT,
				REUSED_LENGTH);
		ColumnHeader laColumnB8 =
			new ColumnHeader(
				REPRINTED_TBL_HEADER2,
				REPRINTED_START_PT,
				REPRINTED_LENGTH);
		ColumnHeader laColumnB9 =
			new ColumnHeader(
				INV_CODE_TBL_HEADER2,
				INV_CODE_START_PT,
				INV_CODE_LENGTH);

		lvRow2.addElement(laColumnB1);
		lvRow2.addElement(laColumnB2);
		lvRow2.addElement(laColumnB3);
		lvRow2.addElement(laColumnB4);
		lvRow2.addElement(laColumnB5);
		lvRow2.addElement(laColumnB6);
		lvRow2.addElement(laColumnB7);
		lvRow2.addElement(laColumnB8);
		lvRow2.addElement(laColumnB9);
		lvTable.addElement(lvRow2);
		InventoryDetailReportData laDataline =
			new InventoryDetailReportData();
		InventoryServerBusiness laInvServerBusinessData =
			new InventoryServerBusiness();
		InventoryAllocationUIData laInvAllocUIData =
			new InventoryAllocationUIData();
		int liSoldTotal = 0;
		int liVoidedTotal = 0;
		int liReusedTotal = 0;
		int liReprntedTotal = 0;
		int liPreItmYear = 0;
		String lsPreItmDesc = "";
		String lsPreItmNo = "";
		String lsAllocate = "";
		String lsEntityIdList = "";
		caInvDStatus.setReportName(FundsConstant.INVENTORYD_REPORT);
		int i = 0;
		if (avResults.size() > 0
			&& avResults != null) // data exists.  create report.
		{
			laDataline =
				(InventoryDetailReportData) avResults.elementAt(i);
			// Setup pre transempid and pre wsid to start up.
			String lsPreTransEmpId = laDataline.getTransEmpId();
			int liPreCashWsId = laDataline.getCashWsId();
			// get the list of cash drawers
			if (laFundsReportData.getEntity()
				== FundsConstant.EMPLOYEE)
				for (int l = 0;
					l < aaFundsData.getSelectedEmployees().size();
					l++)
				{
					EmployeeData laEmployeeObj =
						(EmployeeData) aaFundsData
							.getSelectedEmployees()
							.elementAt(
							i);
					lsEntityIdList =
						lsEntityIdList
							+ " "
							+ laEmployeeObj.getEmployeeId();
				}
			String lsEntityId = "";

			if (laFundsReportData.getEntity()
				== FundsConstant.CASH_DRAWER)
			{
				lsEntityId = String.valueOf(laDataline.getCashWsId());
			} //end if
			else
			{
				lsEntityId = laDataline.getTransEmpId();
			} //end else
			lvHeader = createHeader(lsEntityId, aaFundsData);
			//format for TransWsId so it has the leading zeroes
			DecimalFormat laThreeDigits = new DecimalFormat("000");
			DecimalFormat laSixDigits = new DecimalFormat("000000");

			while (i < avResults.size())
			{ //loop through the results
				generateHeader(lvHeader, lvTable);
				// this.cRpt.blankLines(1);
				// commented out Change 4432
				// print the primary split item at the start of the page
				if (laFundsReportData.getPrimarySplit()
					== FundsConstant.CASH_DRAWER)
				{
					this.caRpt.print(
						CASH_DRAWER_HEADER,
						START_PT_1,
						LENGTH_13);
					this.caRpt.println(
						String.valueOf(laDataline.getCashWsId()));
				}
				if (laFundsReportData.getPrimarySplit()
					== FundsConstant.EMPLOYEE)
				{
					this.caRpt.print(
						EMPLOYEE_ID_HEADER,
						START_PT_1,
						LENGTH_13);
					this.caRpt.println(laDataline.getTransEmpId());
				}
				if (lsPreItmDesc.equals(laDataline.getItmCdDesc()))
				{
					this.caRpt.nextLine();
					this.caRpt.print(
						laDataline.getItmCdDesc(),
						ITEM_DESC_START_PT,
						DESC_LENGTH);
					if (laDataline.getInvItmYr() != 0)
					{
						this.caRpt.print(
							Integer.toString(laDataline.getInvItmYr()),
							ITEM_YEAR_START_PT,
							YEAR_LENGTH);
					}
				}
				//******************************************************
				int j =
					this.caRptProps.getPageHeight()
						- END_OF_PAGE_WHITE_SPACE;
				//******************************************************
				//get available lines on the page
				for (k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						laDataline =
							(
								InventoryDetailReportData) avResults
									.elementAt(
								i);
						// EntityId Break
						if ((laFundsReportData.getEntity()
							== FundsConstant.CASH_DRAWER
							&& liPreCashWsId != laDataline.getCashWsId())
							|| (laFundsReportData.getEntity()
								== FundsConstant.EMPLOYEE
								&& !lsPreTransEmpId.equals(
									laDataline.getTransEmpId())))
							// testing for new page.
						{
							// item break has been forced
							// PCR 34
							totalOfItm(
								liSoldTotal,
								liVoidedTotal,
								liReusedTotal,
								liReprntedTotal,
								lsPreItmDesc,
								liPreItmYear);
							lsPreItmNo = "";
							liSoldTotal = 0;
							liVoidedTotal = 0;
							liReusedTotal = 0;
							liReprntedTotal = 0;
							lsPreItmDesc = "";
							// avoid printing total when entityId
							// changes!! end page because a new page
							// will start for the new entityId
							generateFooter();
							lvHeader.clear();
							liPreCashWsId = laDataline.getCashWsId();
							lsPreTransEmpId =
								laDataline.getTransEmpId();
							if (laFundsReportData.getEntity()
								== FundsConstant.CASH_DRAWER)
							{
								lsEntityId =
									String.valueOf(
										laDataline.getCashWsId());
							}
							else
							{
								lsEntityId = laDataline.getTransEmpId();
							}
							lvHeader =
								createHeader(lsEntityId, aaFundsData);
							generateHeader(lvHeader, lvTable);
							if (laFundsReportData.getPrimarySplit()
								== FundsConstant.CASH_DRAWER)
							{
								this.caRpt.print(
									CASH_DRAWER_HEADER,
									START_PT_1,
									LENGTH_13);
								this.caRpt.println(
									String.valueOf(
										laDataline.getCashWsId()));
							}
							if (laFundsReportData.getPrimarySplit()
								== FundsConstant.EMPLOYEE)
							{
								this.caRpt.print(
									EMPLOYEE_ID_HEADER,
									START_PT_1,
									LENGTH_13);
								this.caRpt.println(
									laDataline.getTransEmpId());
							} //end if
							//*************************
							// reset k after new page
							k = this.caRpt.getCurrX();
							//*************************
						} // end entityId break
						// primary split Break 
						if ((laFundsReportData.getPrimarySplit()
							== FundsConstant.CASH_DRAWER
							&& liPreCashWsId != laDataline.getCashWsId())
							|| (laFundsReportData.getPrimarySplit()
								== FundsConstant.EMPLOYEE
								&& !lsPreTransEmpId.equals(
									laDataline.getTransEmpId())))
						{
							// item break has been forced
							totalOfItm(
								liSoldTotal,
								liVoidedTotal,
								liReusedTotal,
								liReprntedTotal,
								lsPreItmDesc,
								liPreItmYear);
							//**************************
							if (needsPageBreak())
							{
								takeAPageBreak(lvHeader, lvTable);
							}
							//***************************
							lsPreItmNo = "";
							liSoldTotal = 0;
							liVoidedTotal = 0;
							liReusedTotal = 0;
							liReprntedTotal = 0;
							lsPreItmDesc = "";
							// avoid printing total when primary 
							// split changes!!
							// this.cRpt.blankLines(2);
							this.caRpt.blankLines(1);
							//  k=k+1;
							if (laFundsReportData.getPrimarySplit()
								== FundsConstant.CASH_DRAWER)
							{
								this.caRpt.print(
									CASH_DRAWER_HEADER,
									START_PT_1,
									LENGTH_13);
								this.caRpt.println(
									String.valueOf(
										laDataline.getCashWsId()));
								liPreCashWsId =
									laDataline.getCashWsId();
							}
							if (laFundsReportData.getPrimarySplit()
								== FundsConstant.EMPLOYEE)
							{
								this.caRpt.print(
									EMPLOYEE_ID_HEADER,
									START_PT_1,
									LENGTH_13);
								this.caRpt.println(
									laDataline.getTransEmpId());
								lsPreTransEmpId =
									laDataline.getTransEmpId();
							}
						}
						// end primary split break 
						//Item break  
						if (!lsPreItmDesc
							.equals(laDataline.getItmCdDesc())
							// if desc or year changes
							|| liPreItmYear != laDataline.getInvItmYr())
							// do ItemDesc break
						{
							if (!lsPreItmDesc.equals(""))
							{ //print totals if totals have been
								// accumulated
								totalOfItm(
									liSoldTotal,
									liVoidedTotal,
									liReusedTotal,
									liReprntedTotal,
									lsPreItmDesc,
									liPreItmYear);
								//**************************
								if (needsPageBreak())
								{
									takeAPageBreak(lvHeader, lvTable);
								}
								//***************************
								lsPreItmNo = "";
								liSoldTotal = 0;
								liVoidedTotal = 0;
								liReusedTotal = 0;
								liReprntedTotal = 0;
							}
							// k=k+2;     
							this.caRpt.nextLine();
							this.caRpt.print(
								laDataline.getItmCdDesc(),
								ITEM_DESC_START_PT,
								DESC_LENGTH);
							// print the item desc once
							if (laDataline.getInvItmYr() != 0)
							{
								this.caRpt.print(
									Integer.toString(
										laDataline.getInvItmYr()),
									ITEM_YEAR_START_PT,
									YEAR_LENGTH);
							}
							// print the item year once
							lsPreItmDesc = laDataline.getItmCdDesc();
							// set to the new item Desc
							liPreItmYear = laDataline.getInvItmYr();
							// set to the new item Year
						}
						// end Item break
						if (laDataline.getInvLocIdCd().equals("W")
							|| laDataline.getInvLocIdCd().equals("E")
							|| laDataline.getInvLocIdCd().equals("C")
							|| laDataline.getInvLocIdCd().equals("D")
							|| laDataline.getInvLocIdCd().equals("S"))
						{
							lsAllocate =
								laDataline.getInvLocIdCd()
									+ ONE_DASH
									+ laDataline.getInvId();
						} //end if 
						else
						{
							if (laDataline.getInvLocIdCd().equals("U"))
							{
								lsAllocate = U_ITEM;
							} //end inner if
							else
							{
								if (laDataline
									.getInvLocIdCd()
									.equals("V"))
								{
									lsAllocate =
										laDataline.getInvLocIdCd()
											+ REUSE_VOIDED;
								} //end if
								else
								{
									if (laDataline
										.getInvLocIdCd()
										.equals("X"))
									{
										lsAllocate = UNAVAILABLE;
									} //end if
									else
									{
										lsAllocate = UNKNOWN;
									} //end if	
								} //end else	
							} //end inner else	
						} //end out else
						// to add the star for out of sequence items.
						// use GET_NEXT_INVENTORY_NO
						// to compare current inventory item number to
						// calculate inventory item number.
						String lsStar = "";
						if (lsPreItmNo != "")
						{
							laInvAllocUIData.setItmCd(
								laDataline.getItmCd());
							laInvAllocUIData.setInvItmNo(lsPreItmNo);
							laInvAllocUIData.setInvItmYr(
								laDataline.getInvItmYr());
							laInvAllocUIData =
								(
									InventoryAllocationUIData) laInvServerBusinessData
										.processData(
									GeneralConstant.INVENTORY,
									InventoryConstant
										.GET_NEXT_INVENTORY_NO,
									laInvAllocUIData);
							if (!laDataline
								.getInvItmNo()
								.equals(
									laInvAllocUIData.getInvItmEndNo())
								&& !laDataline.getInvItmNo().equals(
									lsPreItmNo))
							{
								lsStar = "*";
							}
						}
						lsPreItmNo = laDataline.getInvItmNo();
						this.caRpt.rightAlign(
							lsStar + laDataline.getInvItmNo(),
							ITM_NUM_DETAIL_START_PT,
							ITM_NUM_DETAIL_LENGTH);
						String lsOfcNo =
							laThreeDigits.format(
								laDataline.getOfcIssuanceNo());
						this.caRpt.print(
							lsOfcNo,
							OFC_NUM_DETAIL_START_PT,
							TR_ID_DETAIL_LENGTH);
						String lsWsIdNo =
							laThreeDigits.format(
								laDataline.getTransWsId());
						this.caRpt.print(
							lsWsIdNo,
							TR_WSID_DETAIL_START_PT,
							TR_ID_DETAIL_LENGTH);
						this.caRpt.print(
							Integer.toString(
								laDataline.getTransAMDate()),
							TR_AM_DATE_DETAIL_START_PT,
							TR_AM_DATE_DETAIL_LENGTH);
						String lsTransTime =
							laSixDigits.format(
								laDataline.getTransTime());
						this.caRpt.print(
							lsTransTime,
							TR_TIME_DETAIL_START_PT,
							TR_TIME_DETAIL_LENGTH);
						if (laDataline.getTransCd().equals(INVVD))
						{
							this.caRpt.print(
								VOIDED,
								VOIDED_START_PT + 2,
								ONE_LENGTH);
							liVoidedTotal++;
						}
						else
						{
							if (laDataline
								.getInvLocIdCd()
								.equals(VOID))
							{
								this.caRpt.print(
									REUSE,
									REUSED_START_PT + 2,
									ONE_LENGTH);
								liReusedTotal++;
							}
							else
							{
								this.caRpt.print(
									SLD,
									SOLD_START_PT + 1,
									ONE_LENGTH);
								liSoldTotal++;
							}
						}
						if (laDataline.getReprntQty() > 0)
						{
							int liReprntedNum =
								laDataline.getReprntQty() - 1;
							String lsReprnted = "";
							if (liReprntedNum > 0)
							{
								lsReprnted = "" + liReprntedNum;
							}
							this.caRpt.print(
								lsReprnted,
								REPRINTED_START_PT + 3,
								lsReprnted.length());
							liReprntedTotal += liReprntedNum;
							// End PCR 34
						}
						this.caRpt.print(
							lsAllocate,
							INV_CODE_START_PT,
							INV_CODE_LENGTH);
						this.caRpt.blankLines(1);
						//**********************************************
						// set k to currx
						k = this.caRpt.getCurrX();
						//**********************************************
						i++;
					} // end of outer if
					//**************************************************
					else
					{
						k = j; // set k so we will exit the for loop
					}
					//**************************************************
				} // end of for
				if (i >= avResults.size()) // last page
				{
					//have to pass an int to this method for page number
					//**************************************************
					if (needsPageBreak())
					{
						takeAPageBreak(lvHeader, lvTable);
					}
					//**************************************************
					totalOfItm(
						liSoldTotal,
						liVoidedTotal,
						liReusedTotal,
						liReprntedTotal,
						lsPreItmDesc,
						liPreItmYear);
					// defect 8628
					//this.caRpt.nextLine(); 
					// generateEndOfReport();
				}
				// generateFooter();
				generateFooter(true); 
				// end defect 8628 
			} // end of while
		} // end if data records found
		if (laFundsReportData.getEntity() == FundsConstant.CASH_DRAWER)
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
					//lsReportStatus.equals("Report Generated"))
				{
					//laDrawer.setReportStatus("Report Generated");
					laDrawer.setReportStatus(
						FundsConstant.REPORT_GENERATED);
				}
				else
				{
					//laDrawer.setReportStatus("No Transactions");
					laDrawer.setReportStatus(
						FundsConstant.NO_TRANSACTIONS);
				}
				// end defect 9943
				cvInvDStatusVec.add(laDrawer);
			}
		}
		if (laFundsReportData.getEntity() == FundsConstant.EMPLOYEE)
		{
			for (int j = 0;
				j < aaFundsData.getSelectedEmployees().size();
				j++)
			{
				EmployeeData laEmployeeObj =
					(EmployeeData) aaFundsData
						.getSelectedEmployees()
						.elementAt(
						j);
				String lsReportStatus = laEmployeeObj.getRptStatus();
				EntityStatusData laEmployee = new EntityStatusData();
				laEmployee.setEmployeeId(laEmployeeObj.getEmployeeId());
				// defect 9943 
				if ((lsReportStatus != null)
					//&& lsReportStatus.equals("Report Generated"))
					&& lsReportStatus.equals(
						FundsConstant.REPORT_GENERATED))
				{
					//laEmployee.setReportStatus("Report Generated");
					laEmployee.setReportStatus(
						FundsConstant.REPORT_GENERATED);
				}
				else
				{
					//laEmployee.setReportStatus("No Transactions");
					laEmployee.setReportStatus(
						FundsConstant.NO_TRANSACTIONS);
				}
				// end defect 9943 
				cvInvDStatusVec.add(laEmployee);
			}
		}
	} //end of formatReport

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Return caInvDStatus 
	 * 
	 */
	public ReportStatusData getInvDStatus()
	{
		return caInvDStatus;
	}
	
	/**
	 * Return cvInvDStatusVec 
	 * 
	 */
	public Vector getInvDStatusVec()
	{
		return cvInvDStatusVec;
	}

	/**
	 * Determine if a page break is needed.  Return a boolean indicating
	 * what the result is.
	 * 
	 * @return boolean
	 */
	private boolean needsPageBreak()
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
	 * Method to populate the data to be displyed on the report
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// this is faked data..
		// Need to get real data structure that relates to this report.
		Vector lvData = new Vector();
		InventoryDetailReportData laDataLine1 =
			new InventoryDetailReportData();
		laDataLine1.setItmCd("AP");
		laDataLine1.setItmCdDesc("ANTIQUE PLT");
		laDataLine1.setInvItmYr(1998);
		laDataLine1.setInvItmNo("3R25");
		laDataLine1.setOfcIssuanceNo(161);
		laDataLine1.setTransWsId(100);
		laDataLine1.setTransAMDate(36941);
		laDataLine1.setTransTime(145851);
		laDataLine1.setInvLocIdCd("S");
		laDataLine1.setInvId("001");
		laDataLine1.setTransCd("INVVD");
		laDataLine1.setTransEmpId("USERID2");
		laDataLine1.setCashWsId(000);
		lvData.addElement(laDataLine1);
		InventoryDetailReportData laDataLine2 =
			new InventoryDetailReportData();
		laDataLine2.setItmCd("AP");
		laDataLine2.setItmCdDesc("ANTIQUE PLT");
		laDataLine2.setInvItmYr(2003);
		laDataLine2.setInvItmNo("3R25");
		laDataLine2.setOfcIssuanceNo(161);
		laDataLine2.setTransWsId(100);
		laDataLine2.setTransAMDate(36941);
		laDataLine2.setTransTime(145851);
		laDataLine2.setInvLocIdCd("S");
		laDataLine2.setInvId("001");
		laDataLine2.setTransCd("INVVD");
		laDataLine2.setTransEmpId("USERID1");
		laDataLine2.setCashWsId(000);
		lvData.addElement(laDataLine2);
		InventoryDetailReportData laDataLine3 =
			new InventoryDetailReportData();
		laDataLine3.setItmCd("AP");
		laDataLine3.setItmCdDesc("ANTIQUE PLT");
		laDataLine3.setInvItmYr(2003);
		laDataLine3.setInvItmNo("3U25");
		laDataLine3.setOfcIssuanceNo(161);
		laDataLine3.setTransWsId(100);
		laDataLine3.setTransAMDate(36941);
		laDataLine3.setTransTime(145851);
		laDataLine3.setInvLocIdCd("S");
		laDataLine3.setInvId("001");
		laDataLine3.setTransEmpId("USERID1");
		laDataLine3.setCashWsId(000);
		lvData.addElement(laDataLine3);
		InventoryDetailReportData laDataLine4 =
			new InventoryDetailReportData();
		laDataLine4.setItmCd("CBP");
		laDataLine4.setItmCdDesc("CITY BUS PLT");
		laDataLine4.setInvItmYr(0);
		laDataLine4.setInvItmNo("T10000");
		laDataLine4.setOfcIssuanceNo(161);
		laDataLine4.setTransWsId(100);
		laDataLine4.setTransAMDate(36941);
		laDataLine4.setTransTime(145851);
		laDataLine4.setInvLocIdCd("S");
		laDataLine4.setInvId("001");
		laDataLine4.setTransEmpId("USERID1");
		laDataLine4.setCashWsId(100);
		lvData.addElement(laDataLine4);
		InventoryDetailReportData laDataLine5 =
			new InventoryDetailReportData();
		laDataLine5.setItmCd("CBP");
		laDataLine5.setItmCdDesc("CITY BUS PLT");
		laDataLine5.setInvItmYr(0);
		laDataLine5.setInvItmNo("T13104");
		laDataLine5.setOfcIssuanceNo(161);
		laDataLine5.setTransWsId(100);
		laDataLine5.setTransAMDate(36941);
		laDataLine5.setTransTime(145851);
		laDataLine5.setInvLocIdCd("V");
		laDataLine5.setInvId("001");
		laDataLine5.setTransEmpId("USERID1");
		laDataLine5.setCashWsId(100);
		lvData.addElement(laDataLine5);
		InventoryDetailReportData laDataLine6 =
			new InventoryDetailReportData();
		laDataLine6.setItmCd("CBP");
		laDataLine6.setItmCdDesc("CITY BUS PLT");
		laDataLine6.setInvItmYr(0);
		laDataLine6.setInvItmNo("T13104");
		laDataLine6.setOfcIssuanceNo(161);
		laDataLine6.setTransWsId(100);
		laDataLine6.setTransAMDate(36941);
		laDataLine6.setTransTime(145851);
		laDataLine6.setInvLocIdCd("V");
		laDataLine6.setInvId("001");
		laDataLine6.setTransEmpId("USERID1");
		laDataLine6.setCashWsId(100);
		lvData.addElement(laDataLine6);
		InventoryDetailReportData laDataLine7 =
			new InventoryDetailReportData();
		laDataLine7.setItmCd("CBP");
		laDataLine7.setItmCdDesc("CITY BUS PLT");
		laDataLine7.setInvItmYr(0);
		laDataLine7.setInvItmNo("T13101");
		laDataLine7.setOfcIssuanceNo(161);
		laDataLine7.setTransWsId(100);
		laDataLine7.setTransAMDate(36941);
		laDataLine7.setTransTime(145851);
		laDataLine7.setInvLocIdCd("S");
		laDataLine7.setInvId("001");
		laDataLine7.setTransEmpId("USERID1");
		laDataLine7.setCashWsId(100);
		lvData.addElement(laDataLine7);
		InventoryDetailReportData laDataLine8 =
			new InventoryDetailReportData();
		laDataLine8.setItmCd("CBP");
		laDataLine8.setItmCdDesc("CITY BUS PLT");
		laDataLine8.setInvItmYr(0);
		laDataLine8.setInvItmNo("T13104");
		laDataLine8.setOfcIssuanceNo(161);
		laDataLine8.setTransWsId(101);
		laDataLine8.setTransAMDate(36941);
		laDataLine8.setTransTime(145851);
		laDataLine8.setInvLocIdCd("S");
		laDataLine8.setInvId("001");
		laDataLine8.setTransEmpId("USERID1");
		laDataLine8.setCashWsId(200);
		lvData.addElement(laDataLine8);
		InventoryDetailReportData laDataLine9 =
			new InventoryDetailReportData();
		laDataLine9.setItmCd("CBP");
		laDataLine9.setItmCdDesc("CITY BUS PLT");
		laDataLine9.setInvItmYr(0);
		laDataLine9.setInvItmNo("T13104");
		laDataLine9.setOfcIssuanceNo(161);
		laDataLine9.setTransWsId(101);
		laDataLine9.setTransAMDate(36941);
		laDataLine9.setTransTime(145851);
		laDataLine9.setInvLocIdCd("S");
		laDataLine9.setInvId("001");
		laDataLine9.setTransEmpId("USERID1");
		laDataLine9.setCashWsId(200);
		lvData.addElement(laDataLine9);
		InventoryDetailReportData laDataLine10 =
			new InventoryDetailReportData();
		laDataLine10.setItmCd("CBP");
		laDataLine10.setItmCdDesc("CITY BUS PLT");
		laDataLine10.setInvItmYr(0);
		laDataLine10.setInvItmNo("T13104");
		laDataLine10.setOfcIssuanceNo(161);
		laDataLine10.setTransWsId(101);
		laDataLine10.setTransAMDate(36941);
		laDataLine10.setTransTime(145851);
		laDataLine10.setInvLocIdCd("S");
		laDataLine10.setInvId("001");
		laDataLine10.setTransEmpId("USERID1");
		laDataLine10.setCashWsId(200);
		lvData.addElement(laDataLine10);
		InventoryDetailReportData laDataLine11 =
			new InventoryDetailReportData();
		laDataLine11.setItmCd("CBP");
		laDataLine11.setItmCdDesc("CITY BUS PLT");
		laDataLine11.setInvItmYr(0);
		laDataLine11.setInvItmNo("T13104");
		laDataLine11.setOfcIssuanceNo(161);
		laDataLine11.setTransWsId(101);
		laDataLine11.setTransAMDate(36941);
		laDataLine11.setTransTime(145851);
		laDataLine11.setInvLocIdCd("S");
		laDataLine11.setInvId("001");
		laDataLine11.setTransEmpId("USERID1");
		laDataLine11.setCashWsId(200);
		lvData.addElement(laDataLine11);
		InventoryDetailReportData laDataLine12 =
			new InventoryDetailReportData();
		laDataLine12.setItmCd("COTTON");
		laDataLine12.setItmCdDesc("COTTON PLT");
		laDataLine12.setInvItmYr(0);
		laDataLine12.setInvItmNo("C0249V");
		laDataLine12.setOfcIssuanceNo(161);
		laDataLine12.setTransWsId(100);
		laDataLine12.setTransAMDate(36941);
		laDataLine12.setTransTime(145851);
		laDataLine12.setInvLocIdCd("S");
		laDataLine12.setInvId("001");
		laDataLine12.setTransEmpId("USERID1");
		laDataLine12.setCashWsId(300);
		lvData.addElement(laDataLine12);
		InventoryDetailReportData laDataLine13 =
			new InventoryDetailReportData();
		laDataLine13.setItmCd("COTTON");
		laDataLine13.setItmCdDesc("COTTON PLT");
		laDataLine13.setInvItmYr(0);
		laDataLine13.setInvItmNo("C0250V");
		laDataLine13.setOfcIssuanceNo(161);
		laDataLine13.setTransWsId(100);
		laDataLine13.setTransAMDate(36941);
		laDataLine13.setTransTime(145851);
		laDataLine13.setInvLocIdCd("S");
		laDataLine13.setInvId("001");
		laDataLine13.setTransEmpId("USERID1");
		laDataLine13.setCashWsId(300);
		lvData.addElement(laDataLine13);
		return lvData;
	}

	/**
	 * This method does a page break.
	 * 
	 * @param avHeader Vector
	 * @param avTable Vector
	 */
	private void takeAPageBreak(Vector avHeader, Vector avTable)
	{
		// close out the current page
		generateFooter();
		// Start the new page
		generateHeader(avHeader, avTable);
		cbNewPageDone = true; // desc on new page
	}

	/**
	 * Generate Total Line.
	 * 
	 * @param aiSoldTotal int
	 * @param aiVoidedTotal int
	 * @param aiReusedTotal int
	 * @param aiReprntedTotal int
	 * @param asPreItmDesc String
	 * @param aiPreItmYear int  
	 */
	private void totalOfItm(
		int aiSoldTotal,
		int aiVoidedTotal,
		int aiReusedTotal,
		int aiReprntedTotal,
		String asPreItmDesc,
		int aiPreItmYear)
	{
		this.caRpt.nextLine();
		if (aiPreItmYear != 0)
		{
			this.caRpt.print(
				TOTAL
					+ asPreItmDesc
					+ " "
					+ Integer.toString(aiPreItmYear)
					+ ":",
				TOTAL_START_PT,
				TOTAL_LENGTH);
		}
		else
		{
			this.caRpt.print(
				TOTAL + asPreItmDesc + ":",
				TOTAL_START_PT,
				TOTAL_LENGTH);
		}
		this.caRpt.print(
			Integer.toString(aiSoldTotal),
			SOLD_START_PT + 1,
			THREE_LENGTH);
		this.caRpt.print(
			Integer.toString(aiVoidedTotal),
			VOIDED_START_PT + 2,
			THREE_LENGTH);
		this.caRpt.print(
			Integer.toString(aiReusedTotal),
			REUSED_START_PT + 2,
			THREE_LENGTH);
		this.caRpt.print(
			Integer.toString(aiReprntedTotal),
			REPRINTED_START_PT + 3,
			THREE_LENGTH);
		this.caRpt.blankLines(ONE_LINE);
		k++;
	}
}
