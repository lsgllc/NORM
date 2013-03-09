package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 *
 * PaymentReportCloseOut.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Listberger	04/03/2002 	added comments
 * M Listberger 05/16/2002 	removed extra colon from summary for cash 
 *                         	drawer when split by employee.
 * M Listberger 07/02/2002 	Added code to display "Current Status"
 * 							instead	of "Cash Drawer" when selecting the
 * 							current	of status option.
 *                          Modified printMainHeader() method.
 * 							CQU100004402.
 * M Listberger 07/19/2002 	added code to correct display of report
 * 							status.
 *                         	Modified setSubHeaderIdValue.
 *                          CQU100004286.  Correct Logic to print
 * 							correctly formatted MainHeader.
 * M Listberger 08/26/2002 	Fixed the information that is displayed for 
 * 							the last record when there is a transaction
 * 							that does not have an associated payment
 * 							record.
 * K Harrell    02/07/2003 	CQU100005274; Error when break by Employee 
 *							w/ 0.00 payments for all employees on given 
 *							cashdrawer.
 * Ray Rowehl	01/05/2004	Make report constants static and refer to
 * 							them as static.
 * 							Also formatted code.
 * 							modify CASH_DRAWER_NONE, CASH_DRAWER_EMP,
 * 							EMP_NONE, EMP_CASH_DRAWER,
 * 							SUBSTA_NONE, SUBSTA_EMP,
 * 							SUBSTA_CASH_DRAWER, 
 *							CASH_DRAWER_INDI_FOR_SORT,
 *							NON_CASH_DRAWER_INDI_FOR_SORT,
 *							main(),
 *							processAndPrintTotalInformation(),
 *							setHeaderDateRange(),
 *							setSortKeysForByCashDrawer(),
 *							setSortKeysForByEmployee(),
 *							setSubHeaderIdValue(),
 *							setSummaryForText()
 * 							defect 6732  fix 5.1.5 fix 2
 * K Harrell	05/27/2004	Removed ":" after "REPORT TYPE" for
 *						 	constant REPORT_TYPE
 *							defect 7097  Ver 5.2.0 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							add getPymtStatus(), getPymtStatVec()
 *							defect 7896 Ver 5.2.3 
 * K Harrell	10/24/2005	Constant renaming 
 * 							defect 8379 Ver 5.2.3 
 * K Harrell	06/08/2009	Use getClockTimeNoMs() 
 * 							modify setSubHeaderIdValue(), formatReport(),
 * 							 processAtBreak()
 * 							defect 9943 Ver Defect_POS_F 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */
/**
 * This program formats the report for Payment close out, which
 * is part of funds.  It breaks the data down between cash and non-cash
 * types.
 *
 * It utilizes Payment Type Report Data and PaymentTypeReportSQL.
 *
 * the PrintVector is used to store the lines for all the cash items.
 * Once we encounter non-cash items or a new cash drawer/employee the
 * print vector is printed out.  This is necessary for making sure the
 * transaction does not get a page break in the middle of it.
 *
 *
 * @version	Defect_POS_F 	08/10/2009
 * @author	M. Listberger
 * <br>Creation Date:		09/07/2001 14:45:34  
 */
public class PaymentReportCloseOut extends ReportTemplate
{
	// set up graph variable for funds data
	private FundsData caFundsData;
	// set up switch to indicate whether or not we are print the
	// summary page
	private boolean cbSummaryPageNeeded = false;
	private boolean cbLastRecord = false;
	// variables for entity and primary split 
	public int ciEntity = 0;
	public int ciPrimarySplit = 0;
	// index for print vector
	private int ciPrintLineIndex = 1;
	//  used to determine if a summary needs to be printed on the
	//  current page
	private int ciKey2Count = 0;
	// sets up the type of sort to be performed,
	// see setUpBreakByCodes()
	public int ciBreakByCode;
	// set up an accumulator for qty
	private int ciTotalQty = 0;
	// defect 6732
	// make constants static
	// set up constants for cash drawer indicators
	private static final int CASH_DRAWER_INDI_FOR_SORT = 0;
	private static final int NON_CASH_DRAWER_INDI_FOR_SORT = 1;
	// Sort constant
	public static final int CASH_DRAWER_NONE = 0;
	public static final int CASH_DRAWER_EMP = 1;
	public static final int EMP_NONE = 2;
	public static final int EMP_CASH_DRAWER = 3;
	public static final int SUBSTA_NONE = 4;
	public static final int SUBSTA_EMP = 5;
	public static final int SUBSTA_CASH_DRAWER = 6;
	// end defect 6732
	// set at break variables
	private final int BREAK_KEY_1 = 1;
	// Cash Drawer, Employee, or Substation
	private final int BREAK_KEY_2 = 2;
	// Cash Drawer, Employee, or None
	private final int BREAK_KEY_3 = 3;
	// Cash Drawer/Non-cash drawer indi (sort order) 
	private final int BREAK_KEY_4 = 4; // Payment Description
	// Initialize LEFT, CENTER, and RIGHT variables
	private final int LEFT = 0;
	private final int CENTER = 1;
	private final int RIGHT = 2;
	private final int REPORT_FOOTER_LINES = 2;
	// number of lines needed to print out the totals of the report,
	// set up hold keys that will be used in "at break" logic
	private String csHoldKey1;
	private String csHoldKey2;
	private String csHoldKey3;
	private String csHoldKey4;
	// set up variables to capture the variable report text strings to
	// be used
	private String csHeaderWsId;
	private String csCashDrawerIndiText;
	private String csCashDrawerIndiTextTotals;
	private String csHeaderIdValue;
	private String csSubHeaderIdValue;
	private String csStartDate;
	private String csEndDate;
	private String csSummaryFor;
	private String csTotalFor;
	// set up variable to hold the payment description code
	private String csHoldDescription;
	// string to concatenate and workstation id and they will print
	// on the summary page
	private String csWsIDsForHeader = "";
	// set up a vector for grand total accumulation.  Will use to
	// print summary.
	private Vector cvSummaryData = new Vector();
	private Vector cvSummaryDataForEntity = new Vector();
	// set up vector for printing
	private Vector cvPrintVector = new Vector();
	// set up dollar fields used in accumulating
	private Dollar NEW_DOLLAR = new Dollar("0");
	private Dollar caTotalForItem = NEW_DOLLAR;
	private Dollar caTotalForCashDrawerType = NEW_DOLLAR;
	private Dollar caTotalForKey2 = NEW_DOLLAR;
	private Dollar caTotalForKey1 = NEW_DOLLAR;
	// main heading
	private final String TRANS_FROM = "TRANSACTIONS FROM";
	// heading for first column 
	private final String DESCRIPTION_HEADER = "DESCRIPTION";
	private final int DESCRIPTION_STARTPT = 15;
	private final int DESCRIPTION_LENGTH = 40;
	// heading for second column
	private final String AMOUNT_HEADER = "AMOUNT";
	private final int AMOUNT_STARTPT = 70;
	private final int AMOUNT_LENGTH = 18;
	// heading for third column
	private final String TOTAL = "TOTAL";
	private final String QTY_HEADER = "QTY";
	private final int QTY_STARTPT = 91;
	private final int QTY_LENGTH = 5;
	// accomodate for indentation of type of operation line and total
	private final int CASH_DRAWR_INDI_STARTPT = 18;
	private final int CASH_DRAWER_INDI_LENGTH = 37;
	// accomodate for indentation of data lines 
	private final int DATALINE_STARTPT = 21;
	private final int DATALINE_LENGTH = 34;
	// used for setReportNum set Report Title
	public String csReportNum = "";
	public String csReportName = FundsConstant.PAYMENT_REPORT;
	// set up constants for subtitles and total lines
	// dashes for total lines
	String SINGLE_DASHES = this.caRpt.singleDashes(AMOUNT_LENGTH);
	String DOUBLE_DASHES = this.caRpt.doubleDashes(AMOUNT_LENGTH);
	public ReportStatusData caPymtStatus = new ReportStatusData();
	public Vector cvPymtStatVec = new Vector();
	private final String THROUGH = " THROUGH ";
	private final String REPORT_TYPE = "REPORT TYPE";
	private final String LAST_CURRENT_STATUS =
		"SINCE LAST CURRENT STATUS";
	private final String DATE_RANGE = "DATE RANGE";
	private final String NOT_DEFINED = "REPORT TYPE NOT DEFINED";
	private final String CURRENT_STATUS = "CURRENT STATUS";
	// defect 8379 
	private final String CLOSEOUT_AFTER_SUBSTA_SUM =
		"CLOSEOUTS AFTER SUBSTATION SUMMARY";
	private final String LAST_CLOSEOUT = "SINCE LAST CLOSEOUT";
	private final String FOR_CLOSEOUT = "FOR LAST CLOSEOUT";
	private final String CLOSEOUT = "CLOSEOUT";
	// end defect 8379 

	/**
	 * PaymentReportCloseOut constructor
	 */
	public PaymentReportCloseOut()
	{
		super();
	}
	/**
	 * PaymentReportCloseOut constructor
	 * 
	 * @param asRptString String
	 * @param asRptProperties ReportProperties
	 */
	public PaymentReportCloseOut(
		String asRptString,
		ReportProperties asRptProperties)
	{
		super(asRptString, asRptProperties);
	}

	/**
	 * This method accumules the dollar amount for the payment
	 * description code, the total for the individual
	 * work station, the total for the individual employee, and the
	 * total for the cash drawer type.  The totals are used when
	 * printing out summaries.
	 * 
	 * @param aaPaymentAmt Dollar
	 * @throws RTSException
	 */
	public void accumulateDollarAmounts(Dollar aaPymntTypeAmt)
		throws RTSException // accumulated totals for this payment Type
	{
		// accumulate for the payment description code
		caTotalForItem = (Dollar) aaPymntTypeAmt.add(caTotalForItem);
		// accumulate total for workstation id
		caTotalForKey1 = (Dollar) aaPymntTypeAmt.add(caTotalForKey1);
		// accumulate for employee
		caTotalForKey2 = (Dollar) aaPymntTypeAmt.add(caTotalForKey2);
		// accumulate for with cash drawer or non-cash drawer
		caTotalForCashDrawerType =
			(Dollar) aaPymntTypeAmt.add(caTotalForCashDrawerType);
	}

	/**
	 * appendBlankLine
	 * 
	 * @throws RTSException 
	 */
	public void appendBlankLine() throws RTSException
	{
		buildPrintVector("", 1, 1, LEFT);
		ciPrintLineIndex++;
	}

	/**
	 * appendDescriptionToPrintVector
	 * 
	 * @param asDescription String
	 * @param aiStartPoint int
	 * @param aiLength int
	 * @throws RTSException
	 */
	public void appendDescriptionToPrintVector(
		String asDescription,
		int aiStartPoint,
		int aiLength)
		throws RTSException
	{
		buildPrintVector(asDescription, aiStartPoint, aiLength, LEFT);
	}

	/**
	 * appendDetailLine
	 * 
	 * @throws RTSException
	 */
	public void appendDetailLine() throws RTSException
	{
		if (!(caTotalForItem.equals(NEW_DOLLAR)))
		{
			appendDescriptionToPrintVector(
				csHoldDescription,
				DATALINE_STARTPT,
				DATALINE_LENGTH);
			appendItemAmountAndQty();
		}
	}

	/**
	 * appendItemAmountAndQty
	 * 
	 * @throws RTSException
	 */
	public void appendItemAmountAndQty() throws RTSException
	{
		buildPrintVector(
			caTotalForItem.printDollar().substring(1),
			AMOUNT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		if (ciTotalQty > 0 && !(csHoldDescription.equals("CASH")))
		{
			buildPrintVector(
				String.valueOf(ciTotalQty),
				QTY_STARTPT,
				QTY_LENGTH,
				RIGHT);
		}
		ciPrintLineIndex++;
	}

	/**
	 * appendTotalForWsId
	 * 
	 * @param aaTotal Dollar
	 * @throws RTSException
	 */
	public void appendTotalForWsId(Dollar aaTotal) throws RTSException
	{
		buildPrintVector(
			aaTotal.printDollar().substring(1),
			AMOUNT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		ciPrintLineIndex++;
	}

	/**
	 * appendTotalsForCashDrawerIndiToPrintVector
	 * 
	 * @param aaTotal Dollar
	 * @throws RTSException
	 */
	public void appendTotalsForCashDrawerIndiToPrintVector(Dollar aaTotal)
		throws RTSException
	{
		buildPrintVector(
			SINGLE_DASHES,
			AMOUNT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		ciPrintLineIndex++;
		appendDescriptionToPrintVector(
			csCashDrawerIndiTextTotals,
			CASH_DRAWR_INDI_STARTPT,
			CASH_DRAWER_INDI_LENGTH);
		buildPrintVector(
			aaTotal.printDollar().substring(1),
			AMOUNT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		ciPrintLineIndex++;
	}

	/**
	 * buildPrintVector
	 * 
	 * @param asDesc String
	 * @param aiStartPoint int
	 * @param aiLength int
	 * @param aiJustification int
	 * @throws RTSException
	 */
	public void buildPrintVector(
		String asDesc,
		int aiStartPoint,
		int aiLength,
		int aiJustification)
		throws RTSException
	{
		PrintVector laPrintVector = new PrintVector();
		laPrintVector.setLineNumber(ciPrintLineIndex);
		laPrintVector.setDesc(asDesc);
		laPrintVector.setStartPoint(aiStartPoint);
		laPrintVector.setLength(aiLength);
		laPrintVector.setJustification(aiJustification);
		cvPrintVector.addElement(laPrintVector);
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * This is the format section of the report for payment close out
	 * and its many varriations
	 * 
	 * @param avResults Vector
	 * @param aaFundsData FundsData
	 * @throws RTSException
	 */
	public void formatReport(Vector avResults, FundsData aaFundsData)
		throws RTSException
	{
		caFundsData = aaFundsData;
		String lsCurrKey1 = " ";
		String lsCurrKey2 = " ";
		String lsCurrKey3 = " ";
		String lsCurrKey4 = " ";
		caPymtStatus.setReportName(FundsConstant.PAYMENT_REPORT);
		// create new data oject
		PaymentTypeReportData laPmyData = new PaymentTypeReportData();
		// set up hold records based on the contents of the first record
		laPmyData = (PaymentTypeReportData) avResults.elementAt(0);
		// Get the type of operation:  cash drawer/non-cash drawer
		// the value we want is in key 3
		setTextForCashDrawerIndi(Integer.parseInt(laPmyData.getKey3()));
		// select value to be inserted into CashWsId, 
		csWsIDsForHeader = String.valueOf(laPmyData.getKey1()) + " ";
		// set the hold keys and other variable text
		setHoldKeys(
			laPmyData.getKey1(),
			laPmyData.getKey2(),
			laPmyData.getKey3(),
			laPmyData.getKey4());
		csHeaderIdValue = String.valueOf(laPmyData.getKey1());
		setSubHeaderIdValue();
		// print the main and column headings
		printMainHeader();
		// print the summary info description for first record
		appendDescriptionToPrintVector(
			csSummaryFor,
			DESCRIPTION_STARTPT,
			DESCRIPTION_LENGTH);
		ciPrintLineIndex++;
		appendBlankLine();
		// print the description "Cash Drawer" or "Non-Cash Drawer"
		// heading
		appendDescriptionToPrintVector(
			csCashDrawerIndiText,
			CASH_DRAWR_INDI_STARTPT,
			CASH_DRAWER_INDI_LENGTH);
		ciPrintLineIndex++;
		// set up int i for the loop processing	
		int i = 0;
		if (!(avResults == null) && (avResults.size() > 0))
		{
			while (i < avResults.size()) //Loop through the results
			{
				// get the current line of data
				laPmyData =
					(PaymentTypeReportData) avResults.elementAt(i);
				// set up local variables with current data
				lsCurrKey1 = laPmyData.getKey1();
				lsCurrKey2 = laPmyData.getKey2();
				lsCurrKey3 = laPmyData.getKey3();
				lsCurrKey4 = laPmyData.getKey4();
				processCashDrawers(
					lsCurrKey1,
					lsCurrKey2,
					lsCurrKey3,
					lsCurrKey4);
				// accumulate all of the various totals
				accumulateDollarAmounts(laPmyData.getPymntTypeAmt());
				ciTotalQty += laPmyData.getPymntTypeQty();
				i++;
			} // while
			// print out the last record and store it
			cbLastRecord = true;
			processLastRecord(
				lsCurrKey1,
				lsCurrKey2,
				lsCurrKey3,
				lsCurrKey4);
			//check to see if we have more than one workstation
			// get the first record
			laPmyData = (PaymentTypeReportData) avResults.elementAt(0);
			String lsFirstId = String.valueOf(laPmyData.getKey1());
			// get the last record
			laPmyData =
				(PaymentTypeReportData) avResults.elementAt(
					(avResults.size() - 1));
			String lsLastId = String.valueOf(laPmyData.getKey1());
			if (!lsFirstId.equals(lsLastId))
				// if the id's do not match
			{
				cbSummaryPageNeeded = true;
				generateFooter();
				processSummaryVector(cvSummaryData);
			}
		}

		// defect 8628
		// this.caRpt.nextLine(); 
		// generateEndOfReport();
		// generateFooter();
		generateFooter(true);
		// end defect 8628 
		 
		//Clear Status if function is not Close Out for the Day, 
		//If it is closeoutday, status has already been set in
		// generatePaymentReport
		if (aaFundsData.getFundsReportData().getRange()
			!= FundsConstant.CLOSE_OUT_FOR_DAY)
		{
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
						//&& lsReportStatus.equals("Report Generated"))
					{
						laDrawer.setReportStatus(
							FundsConstant.REPORT_GENERATED);
						//laDrawer.setReportStatus("Report Generated");						
					}
					else
					{
						laDrawer.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//laDrawer.setReportStatus("No Transactions");						
					}
					// end defect 9943 
					if (laCashDrwrObj.getBsnDateTotalAmt() != null)
					{
						laDrawer.setBsnDateTotalAmt(
							laCashDrwrObj.getBsnDateTotalAmt());
					}
					cvPymtStatVec.add(laDrawer);
				}
			}
			if (aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE)
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
						//&& lsReportStatus.equals("Report Generated"))						
					{
						laEmployee.setReportStatus(
							FundsConstant.REPORT_GENERATED);
						//laEmployee.setReportStatus("Report Generated");						
					}
					else
					{
						laEmployee.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//laEmployee.setReportStatus("No Transactions");						
					}
					// end defect 9943 
					cvPymtStatVec.add(laEmployee);
				}
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
	 * Get Payment Status
	 * 
	 * @return ReportStatusData
	 */
	public ReportStatusData getPymtStatus()
	{
		return caPymtStatus;
	}

	/**
	 * Get Payment Status Vector
	 * 
	 * @return Vector
	 */
	public Vector getPymtStatVec()
	{
		return cvPymtStatVec;
	}

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] an array of command-line arguments
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		FundsData laFundsData = new FundsData();
		// Instantiating a new Report Class
		// set up blank title to be replaced once the new pmy object
		// is created.
		// ReportProperties aRptProps = new ReportProperties("");
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5211");
		laRptProps.setPageOrientation(ReportConstant.PORTRAIT);
		PaymentReportCloseOut laPmy =
			new PaymentReportCloseOut("", laRptProps);
		// Get the appropriate Report Number and Title
		int liReportType = 2; // set up for testing
		laPmy.setReportNum(liReportType);
		//laPmy.setReportName(liReportType);
		laPmy.caRpt.csName = laPmy.csReportName;
		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryResults = laPmy.queryData(lsQuery);
		laPmy.setUpBreakByCode();
		switch (laPmy.ciBreakByCode)
		{
			case PaymentReportCloseOut.CASH_DRAWER_NONE :
				{
					// empty code block
				}
			case PaymentReportCloseOut.CASH_DRAWER_EMP :
				{
					laPmy.setSortKeysForByCashDrawer(lvQueryResults);
					break;
				}
			case PaymentReportCloseOut.EMP_NONE :
				{
					// empty code block
				}
			case PaymentReportCloseOut.EMP_CASH_DRAWER :
				{
					laPmy.setSortKeysForByEmployee(lvQueryResults);
					break;
				}
		}
		// Sort the results
		UtilityMethods.sort(lvQueryResults);
		laPmy.formatReport(lvQueryResults, laFundsData);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\PayCloseOutRpt.txt");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laPmy.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\PayCloseOutRpt.txt");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			// defect 7590
			// changed setVisible to setVisibleRTS
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
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
	 * printMainHeader
	 * 
	 * @throws RTSException
	 */
	public void printMainHeader() throws RTSException
	{
		// vector for first row of column headings
		Vector lvHeaderRow1 = new Vector();
		// vector for second row of column headings
		Vector lvHeaderRow2 = new Vector();
		// vector for all column headings
		Vector lvTable = new Vector(); // vector for column headings
		Vector lvHeader = new Vector();
		// vector for main heading info
		// add additional main headings
		switch (ciEntity)
		{
			case FundsConstant.CASH_DRAWER :
				{
					lvHeader.addElement(ReportConstant.CASH_DRAWER);
					break;
				}
			case FundsConstant.EMPLOYEE :
				{
					lvHeader.addElement(ReportConstant.EMPLOYEE);
					break;
				}
			case FundsConstant.SUBSTATION :
				{
					lvHeader.addElement(ReportConstant.SUBSTATION);
					break;
				}
			default :
				{
					// error message
					lvHeader.addElement(ReportConstant.CASH_DRAWER);
				}
		}
		if (cbSummaryPageNeeded)
			// if we are doing the summary page
		{
			lvHeader.addElement(csWsIDsForHeader);
		}
		else
		{
			lvHeader.addElement(csHeaderIdValue);
		}
		lvHeader.addElement(REPORT_TYPE);
		switch (caFundsData.getFundsReportData().getRange())
		{
			case FundsConstant.AFTER_SUBSTATION :
				{
					lvHeader.addElement(CLOSEOUT_AFTER_SUBSTA_SUM);
					break;
				}
			case FundsConstant.SINCE_CURRENT :
				{
					lvHeader.addElement(LAST_CURRENT_STATUS);
					break;
				}
			case FundsConstant.DATE_RANGE :
				{
					lvHeader.addElement(DATE_RANGE);
					break;
				}
			case FundsConstant.LAST_CLOSE :
				{
					lvHeader.addElement(FOR_CLOSEOUT);
					break;
				}
			case FundsConstant.SINCE_CLOSE :
				{
					lvHeader.addElement(LAST_CLOSEOUT);
					break;
				}
			case FundsConstant.CLOSE_OUT_FOR_DAY :
				{
					// defect 8379 
					lvHeader.addElement(CLOSEOUT);
					// end defect 8379
					break;
				}
			case FundsConstant.CURRENT_STATUS : // change 4402
				{
					lvHeader.addElement(CURRENT_STATUS);
					break;
				}
			default :
				{
					// error message
					lvHeader.addElement(NOT_DEFINED);
					// 4402 changed from "cash drawer".
				}
		}
		// add additional main headings
		// if ( (ciEntity == FundsConstant.CASH_DRAWER
		// || cFundsData.getFundsReportData().getRange()
		// == FundsConstant.DATE_RANGE)
		if (caFundsData.getFundsReportData().getRange()
			== FundsConstant.DATE_RANGE
			|| ((ciEntity == FundsConstant.CASH_DRAWER)
				&& (!cbSummaryPageNeeded)))
		{
			lvHeader.addElement(TRANS_FROM);
			lvHeader.addElement(csStartDate + THROUGH + csEndDate);
		}
		// Set up the first line of the column headings
		ColumnHeader laColumn1 =
			new ColumnHeader(
				TOTAL,
				QTY_STARTPT,
				QTY_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow1.addElement(laColumn1);
		lvTable.addElement(lvHeaderRow1);
		//Additing ColumnHeader Information	
		// Set up the second line of the column headings
		ColumnHeader laColumn2 =
			new ColumnHeader(
				DESCRIPTION_HEADER,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH);
		lvHeaderRow2.addElement(laColumn2);
		ColumnHeader laColumn3 =
			new ColumnHeader(
				AMOUNT_HEADER,
				AMOUNT_STARTPT,
				AMOUNT_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn3);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				QTY_HEADER,
				QTY_STARTPT,
				QTY_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn4);
		// put the second line of column headings in the table vector
		lvTable.addElement(lvHeaderRow2);
		// produce the header
		generateHeader(lvHeader, lvTable);
	}

	/**
	 * Print Transaction
	 * 
	 * @throws RTSException
	 */
	public void printTransaction() throws RTSException
	{
		int liLineNumber = 1;
		// initialize to one because that is the first line number
		// coming in
		ciPrintLineIndex = 1; // reset the field used in the vector.
		if (cvPrintVector != null && cvPrintVector.size() > 0)
		{
			PrintVector laPld = new PrintVector();
			laPld =
				(PrintVector) cvPrintVector.elementAt(
					cvPrintVector.size() - 1);
			int liNumLinesToBePrinted = laPld.getLineNumber();
			// check to see if we have enough room on the page to print
			// out the transaction within a customer
			// set, if not new page is needed.
			// LinesAvailable = Total Detail Lines Avail
			// (set in FormatReport) - Total Detail Lines Used for Page
			// (CurrX).
			// liNumLinesToBePrinted = the last line number that was
			// set in the vector.
			// There will be several elements with the same line number
			// but different start and
			// end points.  Just look at the last one to get the
			// maximum of lines needed.
			if ((this.caRptProps.getPageHeight()
				- this.caRpt.getCurrX()
				- REPORT_FOOTER_LINES)
				< liNumLinesToBePrinted)
			{
				generateFooter();
				// generateHeader(cvHeader, cvTable);
				printMainHeader();
			}
			int i = 0;
			while (cvPrintVector != null && i < cvPrintVector.size())
			{
				laPld = (PrintVector) cvPrintVector.elementAt(i);
				if (laPld.getLineNumber() != liLineNumber)
				{
					this.caRpt.nextLine();
					liLineNumber = laPld.getLineNumber();
				}
				switch (laPld.getJustification())
				{
					case LEFT :
						{
							this.caRpt.print(
								laPld.getDesc(),
								laPld.getStartPoint(),
								laPld.getLength());
							break;
						}
					case RIGHT :
						{
							this.caRpt.rightAlign(
								laPld.getDesc(),
								laPld.getStartPoint(),
								laPld.getLength());
							break;
						}
					case CENTER :
						{
							this.caRpt.center(
								laPld.getDesc(),
								laPld.getStartPoint(),
								laPld.getLength());
							break;
						}
				} // end of switch
				i++;
			} // end of while
		}
		cvPrintVector.clear();
		ciPrintLineIndex = 1;
	}

	/**
	 * Process and Print the Total Information.
	 * 
	 * @throws RTSException
	 */
	public void processAndPrintTotalInformation() throws RTSException
	{
		setSubHeaderIdValue();
		setSummaryForText(csSubHeaderIdValue);
		switch (ciBreakByCode)
		{
			case PaymentReportCloseOut.CASH_DRAWER_NONE :
				{
					// by cash drawer and pay desc
				}
			case PaymentReportCloseOut.EMP_NONE :
				{
					csCashDrawerIndiText =
						ReportConstant.CASH_DRAWER_OPER;
					csCashDrawerIndiTextTotals =
						ReportConstant.CASH_DRAWER_OPER;
					appendBlankLine();
					// print out the double dashes
					buildPrintVector(
						DOUBLE_DASHES,
						AMOUNT_STARTPT,
						AMOUNT_LENGTH,
						RIGHT);
					ciPrintLineIndex++;
					// print out the text "Total For"
					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);
					// get the value in key1 to be concatenated with Total
					// for.  It will either be wsid or empid
					appendTotalForWsId(caTotalForKey1);
					break;
				}
			case PaymentReportCloseOut.CASH_DRAWER_EMP :
				// by cash drawer, employee pay desc
				{
					appendBlankLine();
					// print the double dashed line
					buildPrintVector(
						DOUBLE_DASHES,
						AMOUNT_STARTPT,
						AMOUNT_LENGTH,
						RIGHT);
					ciPrintLineIndex++;
					// print out the text "Total For EMPID"
					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);
					// need to do a total for the emp.  Append to print
					// vector
					appendTotalForWsId(caTotalForKey2);
					// set up variables for the printing of the summary
					// at the bottom of the page
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_CASH_DRAWER
							+ csHoldKey1
							+ ":";
					csTotalFor =
						ReportConstant.TOTAL_FOR_CASH_DRAWER
							+ csHoldKey1
							+ ":";
					break;
				}
			case PaymentReportCloseOut.EMP_CASH_DRAWER :
				{
					appendBlankLine();
					// print the double dashes
					buildPrintVector(
						DOUBLE_DASHES,
						AMOUNT_STARTPT,
						AMOUNT_LENGTH,
						RIGHT);
					ciPrintLineIndex++;
					// print out "Total For cashdrawerid"
					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);
					// need to total for the cash drawer
					appendTotalForWsId(caTotalForKey2);
					// set up variables for the printing of the summary at
					// the bottom of the page                    
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_EMPLOYEE
							+ csHoldKey1
							+ ":";
					csTotalFor =
						ReportConstant.TOTAL_FOR_EMPLOYEE
							+ csHoldKey1
							+ ":";
					break;
				}
		} // end switch ciBreakByCode
		// only print the summary out if there is more than one
		// key 2 on the report (Cash Drawer or Employee)
		if (ciKey2Count > 0)
		{
			appendBlankLine();
			appendBlankLine();
			// print the summary header
			// removed extra colon after csSummaryFor
			appendDescriptionToPrintVector(
				csSummaryFor,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH);
			ciPrintLineIndex++;
			appendBlankLine();
			processSummaryVector(cvSummaryDataForEntity);
			appendTotalsForCashDrawerIndiToPrintVector(caTotalForCashDrawerType);
			appendBlankLine();
			// insert double dashes 
			buildPrintVector(
				DOUBLE_DASHES,
				AMOUNT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			appendBlankLine();
			buildPrintVector(
				csTotalFor,
				CASH_DRAWR_INDI_STARTPT,
				CASH_DRAWER_INDI_LENGTH,
				LEFT);
			appendTotalForWsId(caTotalForKey1);
		} // end if ciNumEmp > 0
	}

	/**
	 * processAtBreak
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @param aiTypeOfBreak int
	 * @throws RTSException
	 */
	public void processAtBreak(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4,
		int aiTypeOfBreak)
		throws RTSException
	{
		// print the previous line of data only if it isn't the last
		// record.
		// The last record detail line is printed in the
		// processLastRecord method.	
		if (!cbLastRecord)
		{
			appendDescriptionToPrintVector(
				csHoldDescription,
				DATALINE_STARTPT,
				DATALINE_LENGTH);
			appendItemAmountAndQty();
			storeObjectsToAccumulateTotals();
		}
		switch (aiTypeOfBreak)
		{
			case BREAK_KEY_1 :
				// break of Workstation id, employee, or workstation
				{
					// print the total for the cash drawer type
					if (!cbLastRecord)
					{
						appendTotalsForCashDrawerIndiToPrintVector(caTotalForCashDrawerType);
					}
					else
					{
						// for the last record need to insert blank line
						// because the appendTotals method above is not
						// executed, therefore the necessary blank line is
						// not inserted.
						appendBlankLine();
					}
					processAndPrintTotalInformation();
					printTransaction();
					// reset the data for used for the workstation
					cvSummaryDataForEntity.clear();
					// reset the accumulators
					resetItemAccumulators();
					caTotalForCashDrawerType = NEW_DOLLAR;
					caTotalForKey2 = NEW_DOLLAR;
					caTotalForKey1 = NEW_DOLLAR;
					ciKey2Count = 0;
					setTextForCashDrawerIndi(
						(Integer.parseInt(asCurrKey3)));
					// set up the hold keys	
					csHeaderIdValue = asCurrKey1;
					// need this for the headers
					if (!cbLastRecord)
					{
						if (caFundsData
							.getFundsReportData()
							.getEntity()
							== FundsConstant.CASH_DRAWER)
							for (int l = 0;
								l
									< caFundsData
										.getSelectedCashDrawers()
										.size();
								l++)
							{
								CashWorkstationCloseOutData laCashDrwrObj =
									(CashWorkstationCloseOutData) caFundsData
										.getSelectedCashDrawers()
										.elementAt(l);
								int liCashDrwr =
									laCashDrwrObj.getCashWsId();
								// If it is closeoutday, status has already
								// been set in generatePaymentReport
								if (caFundsData
									.getFundsReportData()
									.getRange()
									!= FundsConstant.CLOSE_OUT_FOR_DAY)
								{
									if (Integer
										.parseInt(csHeaderIdValue)
										== liCashDrwr)
									{
										// defect 9943 
										laCashDrwrObj.setRptStatus(
											//"Report Generated");
											FundsConstant.REPORT_GENERATED);
										// end defect 9943  
										break;
									}
								}
							}
						else if (
							caFundsData
								.getFundsReportData()
								.getEntity()
								== FundsConstant.EMPLOYEE)
							for (int l = 0;
								l
									< caFundsData
										.getSelectedEmployees()
										.size();
								l++)
							{
								EmployeeData laEmployeeObj =
									(EmployeeData) caFundsData
										.getSelectedEmployees()
										.elementAt(
										l);
								String lsEmployeeId =
									laEmployeeObj.getEmployeeId();
								if (csHeaderIdValue
									.equals(lsEmployeeId))
								{
									// defect 9943
									laEmployeeObj.setRptStatus(
									// "Report Generated");
									FundsConstant.REPORT_GENERATED);
									// end defect 9943  

									break;
								}
								/// else lEmployeeObj.setRptStatus("");
							}
						csWsIDsForHeader += csHeaderIdValue + " ";
						// print out all workstations ids on final
						// summary page.
					}
					break;
				}
			case BREAK_KEY_2 : // break at primary Split
				{
					// print the totals of the last cash drawer 
					appendTotalsForCashDrawerIndiToPrintVector(caTotalForCashDrawerType);
					appendBlankLine();
					// print the totals for the primary split
					// print the double dashes for the total
					buildPrintVector(
						DOUBLE_DASHES,
						AMOUNT_STARTPT,
						AMOUNT_LENGTH,
						RIGHT);
					ciPrintLineIndex++;
					// print the text "Total for"
					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);
					//  get the emp name or wsid
					appendTotalForWsId(caTotalForKey2);
					// print out the vector
					printTransaction();
					appendBlankLine();
					appendBlankLine();
					appendBlankLine();
					// keep track of multiple employees or cashwsid's
					// this will indicate if we need to print a summary
					// at the bottom of the page.
					ciKey2Count += 1;
					// print the summary header text for this person
					setSummaryForText(asCurrKey2);
					setTextForCashDrawerIndi(
						(Integer.parseInt(asCurrKey3)));
					appendDescriptionToPrintVector(
						csSummaryFor,
						DESCRIPTION_STARTPT,
						DESCRIPTION_LENGTH);
					ciPrintLineIndex++;
					appendBlankLine();
					// set up the cash drawer header indi (
					// cash drawer vs. non-cashdrawer)
					appendDescriptionToPrintVector(
						csCashDrawerIndiText,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH);
					ciPrintLineIndex++;
					resetItemAccumulators();
					caTotalForCashDrawerType = NEW_DOLLAR;
					caTotalForKey2 = NEW_DOLLAR;
					break;
				}
			case BREAK_KEY_3 : // break by cash drawer non cash drawer
				{
					// print the summary the drawer drawer type
					appendTotalsForCashDrawerIndiToPrintVector(caTotalForCashDrawerType);
					printTransaction();
					// reset the amount fields	
					caTotalForCashDrawerType = NEW_DOLLAR;
					// get the text for the next type of Cash Drawer Indi
					setTextForCashDrawerIndi(
						(Integer.parseInt(asCurrKey3)));
					// if we aren't printing the last record, go ahead
					// and print the header for the next type of cash drawer
					if (!cbLastRecord)
					{
						// need to insert blank lines between previous total
						// the the next cash drawer indi text 
						appendBlankLine();
						appendBlankLine();
						// print out the cash drawer/non cash drawer heading 
						appendDescriptionToPrintVector(
							csCashDrawerIndiText,
							CASH_DRAWR_INDI_STARTPT,
							CASH_DRAWER_INDI_LENGTH);
						ciPrintLineIndex++;
					}
					// reset the field that accumulate dollars and quantity
					// for each payment code desc
					resetItemAccumulators();
					caTotalForCashDrawerType = NEW_DOLLAR;
					break;
				}
			case BREAK_KEY_4 : // break of payment desc
				{
					resetItemAccumulators();
					break;
				}
		}
		// reset and load the csHoldPymnttypeCdDesc with the current
		// value that is read in
		setHoldKeys(asCurrKey1, asCurrKey2, asCurrKey3, asCurrKey4);
	}

	/**
	 * Process Cash Drawers
	 * 
	 * @param asCurrKey1 String 
	 * @param asCurrKey2 String 
	 * @param asCurrKey3 String 
	 * @param asCurrKey4 String 
	 * @throws RTSException
	 */
	public void processCashDrawers(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4)
		throws RTSException
	{
		if (asCurrKey1.equals(csHoldKey1))
			// check to see if we have a new data line
		{
			if (asCurrKey2.equals(csHoldKey2))
				// if employee id's match
			{
				if (asCurrKey3.equals(csHoldKey3))
					// cash drawer indi's are the same
				{
					if (!asCurrKey4.equals(csHoldKey4))
						// payment descriptions do not match
					{
						processAtBreak(
							asCurrKey1,
							asCurrKey2,
							asCurrKey3,
							asCurrKey4,
							BREAK_KEY_4);
					} // end of currKey4 not equal to csHoldKe4
				} // end of currKey3 equal to csHoldKey3
				else
				{ // currKey3 does not equal csHoldKey3
					processAtBreak(
						asCurrKey1,
						asCurrKey2,
						asCurrKey3,
						asCurrKey4,
						BREAK_KEY_3);
				} // end of currKey 3 not equalling csHold key 3
			} // end of currKey2 equals csHoldKey2
			else
			{ // currKey2 does not equal csHoldKey2
				processAtBreak(
					asCurrKey1,
					asCurrKey2,
					asCurrKey3,
					asCurrKey4,
					BREAK_KEY_2);
			} // end of currKey 2 not equal to csHoldKey2
		} // end of currKey 1  equals csHoldKey1
		else
		{ // currKey1 does not equal csHoldKey1
			processAtBreak(
				asCurrKey1,
				asCurrKey2,
				asCurrKey3,
				asCurrKey4,
				BREAK_KEY_1);
			// print the next pages header					
			generateFooter();
			printMainHeader();
			// print the description for the summary info
			appendDescriptionToPrintVector(
				csSummaryFor,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH);
			ciPrintLineIndex++;
			// add a blank line
			buildPrintVector("", 0, 0, LEFT);
			ciPrintLineIndex++;
			appendDescriptionToPrintVector(
				csCashDrawerIndiText,
				CASH_DRAWR_INDI_STARTPT,
				CASH_DRAWER_INDI_LENGTH);
			ciPrintLineIndex++;
		} // end of currKey 1 does not equals csHoldKey1
	}
	/**
	 * Process Last Record
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @throws RTSException
	 */
	public void processLastRecord(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4)
		throws RTSException
	{
		// print the last detail record
		if (!(caTotalForItem.equals(NEW_DOLLAR)))
		{
			appendDescriptionToPrintVector(
				csHoldDescription,
				DATALINE_STARTPT,
				DATALINE_LENGTH);
			appendItemAmountAndQty();
		}
		storeObjectsToAccumulateTotals();
		// process the at break logic for each of the breaks.  need to
		// do this so totals print out. start with 3 because that is the
		// at break of cashDrawerIndi, then 2 is by Employee,
		// and 1 is by cashDrawer
		processAtBreak(
			asCurrKey1,
			asCurrKey2,
			asCurrKey3,
			asCurrKey4,
			BREAK_KEY_3);
		processAtBreak(
			asCurrKey1,
			asCurrKey2,
			asCurrKey3,
			asCurrKey4,
			BREAK_KEY_1);
	}

	/**
	 * This method is used to print the summary data at the bottom of
	 * the page, if it is needed.  It will also print
	 * out the final summary page, if it is needed.  The summaries are
	 * only concerned with breaks at the Cash
	 * Drawer Indicator and the Payment Description.
	 * 
	 * @param avSummaryData Vector
	 * @throws RTSException
	 */
	public void processSummaryVector(Vector avSummaryData)
		throws RTSException
	{
		String lsCurrKey1 = " ";
		String lsCurrKey2 = " ";
		// reset the items used for accumulating totals
		resetItemAccumulators();
		caTotalForCashDrawerType = NEW_DOLLAR;
		caTotalForKey1 = NEW_DOLLAR;
		ciTotalQty = 0;
		// set the key values
		for (int i = 0; i < avSummaryData.size(); i++)
		{
			PaymentTypeReportData laPmys =
				(PaymentTypeReportData) avSummaryData.elementAt(i);
			laPmys.setKey1(String.valueOf(laPmys.getCashDrawerIndi()));
			// when we store summary data we set this to the sort order		
			laPmys.setKey2(laPmys.getPymntTypeCdDesc());
			laPmys.setKey3(" ");
			laPmys.setKey4(" ");
			avSummaryData.setElementAt(laPmys, i);
		}
		// Sort the results
		UtilityMethods.sort(avSummaryData);
		// get the first record
		PaymentTypeReportData laPmys =
			(PaymentTypeReportData) avSummaryData.elementAt(0);
		// load the initial hold variable
		csHoldKey1 = laPmys.getKey1(); // cash drawer/ non cash drawer
		csHoldKey2 = laPmys.getKey2(); // payment description code
		if (cbSummaryPageNeeded)
		{
			//csWsIDsForHeader
			csHeaderWsId = csWsIDsForHeader;
			printMainHeader();
			csSummaryFor = ReportConstant.SUMMARY_FOR_REPORT_TEXT;
			csTotalFor = ReportConstant.REPORT_TOTAL_TEXT;
			buildPrintVector(
				csSummaryFor,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH,
				LEFT);
			ciPrintLineIndex++;
			appendBlankLine();
		}
		// get the text for the next type of Cash Drawer Indi
		setTextForCashDrawerIndi(Integer.parseInt(laPmys.getKey1()));
		appendDescriptionToPrintVector(
			csCashDrawerIndiText,
			CASH_DRAWR_INDI_STARTPT,
			CASH_DRAWER_INDI_LENGTH);
		ciPrintLineIndex++;
		int i = 0;
		if (!(avSummaryData == null) && (avSummaryData.size() > 0))
		{
			while (i < avSummaryData.size()) //Loop through the results
			{
				laPmys =
					(PaymentTypeReportData) avSummaryData.elementAt(i);
				lsCurrKey1 = laPmys.getKey1();
				lsCurrKey2 = laPmys.getKey2();
				if (lsCurrKey1.equals(csHoldKey1))
					// cash drawer/on cash drawer match
				{
					if (!lsCurrKey2.equals(csHoldKey2))
						// payment descriptions do not match
					{
						csHoldDescription = csHoldKey2;
						// print the previous line of data
						appendDetailLine();
						// appendDescriptionToPrintVector(
						// csHoldDescription, DATALINE_STARTPT,
						// DATALINE_LENGTH);
						// appendItemAmountAndQty();
						// reset the items used for accumulating totals
						resetItemAccumulators();
						// reset and load the hold key with the current
						// value that is read in
						csHoldKey2 = lsCurrKey2;
					} // end of current lsPymnttypeCdDesc.equals
					// csHoldPymntTypeCdDesc
				} // end of type of cash drawer indicators matching
				else
					// cash drawer indi's do not match
					{
					csHoldDescription = csHoldKey2;
					appendDetailLine();
					// appendDescriptionToPrintVector(csHoldDescription,
					// DATALINE_STARTPT, DATALINE_LENGTH);
					// appendItemAmountAndQty();
					// print the summary the drawer drawer type
					appendTotalsForCashDrawerIndiToPrintVector(caTotalForCashDrawerType);
					printTransaction();
					appendBlankLine();
					// reset the amount fields
					resetItemAccumulators();
					// get the text for the next type of Cash Drawer
					setTextForCashDrawerIndi(
						Integer.parseInt(lsCurrKey1));
					caTotalForCashDrawerType = new Dollar("0");
					//  print the header for the next type of cash
					// drawer
					appendBlankLine();
					appendDescriptionToPrintVector(
						csCashDrawerIndiText,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH);
					ciPrintLineIndex++;
					csHoldKey1 = lsCurrKey1;
					csHoldKey2 = lsCurrKey2;
					csHoldDescription = csHoldKey2;
				} // end of cash drawer indi's not matching
				// accumulated totals for this payment Type
				accumulateDollarAmounts(laPmys.getPymntTypeAmt());
				ciTotalQty += laPmys.getPymntTypeQty();
				i++;
			}
		}
		// reset the value of the hold key with the concatenation
		// string of all WsId's for the header page
		csHoldDescription = csHoldKey2;
		// append the description and quantity for the last item
		appendDetailLine();
		if (cbSummaryPageNeeded)
		{
			// print the summary the drawer drawer type
			appendTotalsForCashDrawerIndiToPrintVector(caTotalForCashDrawerType);
			appendBlankLine();
			buildPrintVector(
				DOUBLE_DASHES,
				AMOUNT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			ciPrintLineIndex++;
			buildPrintVector(
				csTotalFor,
				CASH_DRAWR_INDI_STARTPT,
				CASH_DRAWER_INDI_LENGTH,
				LEFT);
			appendTotalForWsId(caTotalForKey1);
			appendBlankLine();
			printTransaction();
		}
	}

	/**
	 * Query Data
	 * 
	 * @param asQuery String
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvDataLine = new Vector();
		Dollar laPymntTypeAmtDollar = new Dollar("100.00");
		PaymentTypeReportData laPTR_A = new PaymentTypeReportData();
		PaymentTypeReportData laPTR_B = new PaymentTypeReportData();
		PaymentTypeReportData laPTR_C = new PaymentTypeReportData();
		PaymentTypeReportData laPRT_D = new PaymentTypeReportData();
		laPTR_A.setCashDrawerIndi(1);
		laPTR_A.setCashWsId(120);
		laPTR_A.setFeeSourceCd(1);
		laPTR_A.setPymntTypeAmt(laPymntTypeAmtDollar);
		laPTR_A.setPymntTypeCdDesc("SOMETHING");
		laPTR_A.setPymntTypeQty(55);
		laPTR_A.setTransEmpId("128");
		lvDataLine.addElement(laPTR_A);
		//	b.setCashDrawerIndi(1);
		laPTR_B.setCashDrawerIndi(0);
		laPTR_B.setCashWsId(100);
		laPTR_B.setFeeSourceCd(1);
		laPTR_B.setPymntTypeAmt(laPymntTypeAmtDollar);
		//b.setPymntTypeCdDesc("CHECK");
		laPTR_B.setPymntTypeCdDesc("SOMETHING");
		laPTR_B.setPymntTypeQty(55);
		laPTR_B.setTransEmpId("128");
		lvDataLine.addElement(laPTR_B);
		laPTR_C.setCashDrawerIndi(1);
		laPTR_C.setCashWsId(100);
		laPTR_C.setFeeSourceCd(1);
		laPTR_C.setPymntTypeAmt(laPymntTypeAmtDollar);
		laPTR_C.setPymntTypeCdDesc("SOMETHING");
		laPTR_C.setPymntTypeQty(55);
		laPTR_C.setTransEmpId("129");
		lvDataLine.addElement(laPTR_C);
		//	b.setCashDrawerIndi(1);
		laPRT_D.setCashDrawerIndi(0);
		laPRT_D.setCashWsId(100);
		laPRT_D.setFeeSourceCd(1);
		laPRT_D.setPymntTypeAmt(laPymntTypeAmtDollar);
		//d.setPymntTypeCdDesc("CHECK");
		laPRT_D.setPymntTypeCdDesc("SOMETHING");
		laPRT_D.setPymntTypeQty(55);
		laPRT_D.setTransEmpId("129");
		lvDataLine.addElement(laPRT_D);
		/*	
		for (int i = 0; i < 5; i++)
		{
			lPaymentTypeReportData = new PaymentTypeReportData();
			lPaymentTypeReportData.setCashWsId(300);
			lPaymentTypeReportData.setFeeSourceCd(2);
			lPaymentTypeReportData.setPymntTypeAmt(lPymntTypeAmtDollar);
			lPaymentTypeReportData.setPymntTypeQty(55);
			lPaymentTypeReportData.setTransEmpId("212");
			lPaymentTypeReportData.setPymntTypeCdDesc("CHECK");
			lvdataLine.addElement(lPaymentTypeReportData);
		}
		for (int i = 0; i < 5; i++)
		{
			lPaymentTypeReportData = new PaymentTypeReportData();
			lPaymentTypeReportData.setCashWsId(200);
			lPaymentTypeReportData.setFeeSourceCd(2);
			lPaymentTypeReportData.setPymntTypeAmt(lPymntTypeAmtDollar);
			lPaymentTypeReportData.setPymntTypeQty(55);
			lPaymentTypeReportData.setTransEmpId("130");
			lPaymentTypeReportData.setPymntTypeCdDesc("CHECK");
			lvdataLine.addElement(lPaymentTypeReportData);
			}
		for (int i = 0; i < 5; i++)
		{
			lPaymentTypeReportData = new PaymentTypeReportData();
			lPaymentTypeReportData.setCashWsId(100);
			lPaymentTypeReportData.setFeeSourceCd(2);
			lPaymentTypeReportData.setPymntTypeAmt(lPymntTypeAmtDollar);
			lPaymentTypeReportData.setPymntTypeQty(55);
			lPaymentTypeReportData.setTransEmpId("128");
			lPaymentTypeReportData.setPymntTypeCdDesc("CHECK");
			lvdataLine.addElement(lPaymentTypeReportData);
		}
		for (int i = 0; i < 2; i++)
		{
			lPaymentTypeReportData = new PaymentTypeReportData();
			lPaymentTypeReportData.setCashWsId(100);
			lPaymentTypeReportData.setFeeSourceCd(1);
			lPaymentTypeReportData.setPymntTypeAmt(lPymntTypeAmtDollar);
			lPaymentTypeReportData.setPymntTypeQty(55);
			lPaymentTypeReportData.setTransEmpId("129");
			lPaymentTypeReportData.setPymntTypeCdDesc("DEF");
			lvdataLine.addElement(lPaymentTypeReportData);
		}
		for (int i = 0; i < 2; i++)
		{
			lPaymentTypeReportData = new PaymentTypeReportData();
			lPaymentTypeReportData.setCashWsId(300);
			lPaymentTypeReportData.setFeeSourceCd(1);
			lPaymentTypeReportData.setPymntTypeAmt(lPymntTypeAmtDollar);
			lPaymentTypeReportData.setPymntTypeQty(55);
			lPaymentTypeReportData.setTransEmpId("129");
			lPaymentTypeReportData.setPymntTypeCdDesc("DEF");
			lvdataLine.addElement(lPaymentTypeReportData);
		}
		for (int i = 0; i < 2; i++)
		{
			lPaymentTypeReportData = new PaymentTypeReportData();
			lPaymentTypeReportData.setCashWsId(100);
			lPaymentTypeReportData.setFeeSourceCd(1);
			lPaymentTypeReportData.setPymntTypeAmt(lPymntTypeAmtDollar);
			lPaymentTypeReportData.setPymntTypeQty(55);
			lPaymentTypeReportData.setTransEmpId("129");
			lPaymentTypeReportData.setPymntTypeCdDesc("ABC");
			lvdataLine.addElement(lPaymentTypeReportData);
		}
		*/
		/*	
			c.setCashDrawerIndi(1);
		c.setCashWsId(200);
		c.setFeeSourceCd(1);
		c.setPymntTypeAmt(lPymntTypeAmtDollar);
		c.setPymntTypeCdDesc("CASH");
		c.setPymntTypeQty(55);
		c.setTransEmpId("129");
		lvdataLine.addElement(c);
		
		d.setCashDrawerIndi(1);
		d.setCashWsId(300);
		d.setFeeSourceCd(1);
		d.setPymntTypeAmt(lPymntTypeAmtDollar);
		d.setPymntTypeCdDesc("CASH");
		d.setPymntTypeQty(55);
		d.setTransEmpId("129");
		lvdataLine.addElement(d);
		
		e.setCashDrawerIndi(1);
		e.setCashWsId(300);
		e.setFeeSourceCd(1);
		e.setPymntTypeAmt(lPymntTypeAmtDollar);
		e.setPymntTypeCdDesc("CHECK");
		e.setPymntTypeQty(55);
		e.setTransEmpId("129");
		lvdataLine.addElement(e);
		*/
		return lvDataLine;
	}

	/**
	 * Reset Item Accumulators
	 * 
	 * @throws RTSException
	 */
	public void resetItemAccumulators() throws RTSException
	{
		caTotalForItem = NEW_DOLLAR;
		ciTotalQty = 0;
	}
	/**
	 * Set the Header Date Range.
	 * 
	 * @throws RTSException
	 */
	public void setHeaderDateRange() throws RTSException
	{
		Vector lvVector = caFundsData.getSelectedCashDrawers();
		switch (ciBreakByCode)
		{
			case PaymentReportCloseOut.CASH_DRAWER_NONE :
				{
					// by workstation and pay desc code
				}
			case PaymentReportCloseOut.CASH_DRAWER_EMP :
				{
					// by Workstation, employee, and pay desc code
					csSubHeaderIdValue = csHoldKey1;
					for (int i = 0; i < lvVector.size(); i++)
					{
						CashWorkstationCloseOutData drawer =
							(CashWorkstationCloseOutData) lvVector.get(
								i);
						if (Integer.parseInt(csHoldKey1)
							== drawer.getCashWsId())
						{
							csStartDate =
								drawer.getCloseOutBegTstmp().toString();
							csEndDate =
								drawer.getCloseOutEndTstmp().toString();
							break;
						}
					}
				}
			case PaymentReportCloseOut.EMP_NONE :
				{
					// by Employee and pay desc
					{
						csSubHeaderIdValue = csHoldKey1;
						break;
					}
				}
			case EMP_CASH_DRAWER :
				// by Employee, Cash Drawer,  and pay desc
				{
					{
						csSubHeaderIdValue = csHoldKey2;
						break;
					}
				}
		}
	}

	/**
	 * Set the hold keys
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @throws RTSException
	 */
	public void setHoldKeys(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4)
		throws RTSException
	{
		csHoldKey1 = asCurrKey1;
		csHoldKey2 = asCurrKey2;
		csHoldKey3 = asCurrKey3;
		csHoldKey4 = asCurrKey4;
		csHeaderWsId = csHoldKey1;
		csHoldDescription = csHoldKey4;
		setSubHeaderIdValue();
		setSummaryForText(csSubHeaderIdValue);
	}

	/**
	 * Set Report Name
	 * 
	 * @param aiReportType int
	 * @throws RTSException
	 * @deprecated 
	 */
	//	public void setReportName(int aiReportType) throws RTSException
	//	{
	//		// setting up for test should be passed in
	//		String lsReportTitle = FundsConstant.PAYMENT_REPORT;
	//		if (lsReportTitle.equals(FundsConstant.PAYMENT_REPORT))
	//		{
	//			csReportName = FundsConstant.PAYMENT_REPORT;
	//		}
	//	}

	/**
	 * Set Report Number
	 * 
	 * @param aiReportType int
	 * @throws RTSException
	 */
	public void setReportNum(int aiReportType) throws RTSException
	{
		switch (aiReportType)
		{
			case FundsConstant.DATE_RANGE : // 5211
			case FundsConstant.LAST_CLOSE : //  Last close out
			case FundsConstant.AFTER_SUBSTATION :
			case FundsConstant.SINCE_CLOSE :
			case FundsConstant.SINCE_CURRENT :
				{
					csReportNum = ReportConstant.PAYMENT_TYPE_REPORT_ID;
					break;
				}
			case FundsConstant.CURRENT_STATUS : // 5212  Current Status
				{
					csReportNum =
						ReportConstant.CURRENT_STATUS_REPORT_ID;
					break;
				}
			case FundsConstant.CLOSE_OUT_FOR_DAY :
				// 5212  Current Status
				{
					csReportNum = ReportConstant.CLOSEOUT_REPORT_ID;
					break;
				}
			case 7 :
				// 5921
				{
					csReportNum = ReportConstant.SUBSTATION_REPORT_ID;
					break;
				}
			case 8 :
				{
					csReportNum = ReportConstant.COUNTYWIDE_REPORT_ID;
					break;
				}
			default :
				{
					// rts exception for report error
					break;
				}
		}
		this.caRptProps.setUniqueName(csReportNum);
	}

	/**
	 * Set up the sort keys when CashDrawer.
	 * 
	 * @param avQueryResults Vector
	 * @throws RTSException
	 */
	public void setSortKeysForByCashDrawer(Vector avQueryResults)
		throws RTSException
	{
		// set the key values
		for (int i = 0; i < avQueryResults.size(); i++)
		{
			PaymentTypeReportData laPaymentTypeReportData =
				(PaymentTypeReportData) avQueryResults.elementAt(i);
			// Key 1 values will be the workstation id
			laPaymentTypeReportData.setKey1(
				String.valueOf(laPaymentTypeReportData.getCashWsId()));
			// key 2 will either be blank or will be emp id.  If the
			// user select by employee, load that data else leave blank
			switch (ciBreakByCode)
			{
				case CASH_DRAWER_NONE :
					// by cash drawer and payment type code description,
					// no data is needed
					{
						laPaymentTypeReportData.setKey2(" ");
						break;
					}
				case CASH_DRAWER_EMP :
					// by cash drawer, employee id, and pay desc code
					{
						laPaymentTypeReportData.setKey2(
							laPaymentTypeReportData.getTransEmpId());
						break;
					}
			}
			// key 3 values are based on the cashDrawerIndi, the report
			// must be sorted by type cash drawer, then non cash drawer
			// when we store the values for the summary we store the
			// order we want sorted
			switch (laPaymentTypeReportData.getCashDrawerIndi())
			{
				case FundsConstant.NON_CASH_DRAWER_INDI :
					// non cash drawer -- need to be second the in the sort
					{
						laPaymentTypeReportData.setKey3("1");
						break;
					}
				case FundsConstant.CASH_DRAWER_INDI :
					// cash drawer -- need to be first in the sort
					{
						laPaymentTypeReportData.setKey3("0");
						break;
					}
			}
			// key 4 value will always be the Payment type code
			// description
			laPaymentTypeReportData.setKey4(
				laPaymentTypeReportData.getPymntTypeCdDesc());
			// update the query with the keys we just set
			avQueryResults.setElementAt(laPaymentTypeReportData, i);
		}
	}

	/**
	 * Set up the sort keys when Employee.
	 * 
	 * @param avQueryResults Vector
	 * @throws RTSException
	 */
	public void setSortKeysForByEmployee(Vector avQueryResults)
		throws RTSException
	{
		// set the key values
		for (int i = 0; i < avQueryResults.size(); i++)
		{
			PaymentTypeReportData laPaymentTypeReportData =
				(PaymentTypeReportData) avQueryResults.elementAt(i);
			// Key 1 values will be the employee id
			laPaymentTypeReportData.setKey1(
				String.valueOf(
					laPaymentTypeReportData.getTransEmpId()));
			// key 2 will either be blank or will be emp id.
			// If the user select by employee, load that data else leave
			// blank
			switch (ciBreakByCode)
			{
				case PaymentReportCloseOut.EMP_NONE :
					// by Employee id and payment type code description,
					// no data is needed
					{
						laPaymentTypeReportData.setKey2(" ");
						break;
					}
				case PaymentReportCloseOut.EMP_CASH_DRAWER :
					// by  employee id, cash Drawer, and pay desc code
					{
						laPaymentTypeReportData.setKey2(
							(String
								.valueOf(
									laPaymentTypeReportData
										.getCashWsId())));
						break;
					}
			}
			// key 3 values are based on the cashDrawerIndi, the report
			// must be sorted by type cash drawer, then non cash drawer
			// when we store the values for the summary we store the
			// order we want sorted
			switch (laPaymentTypeReportData.getCashDrawerIndi())
			{
				case FundsConstant.NON_CASH_DRAWER_INDI :
					// non cash drawer -- need to be second the in the
					// sort
					{
						laPaymentTypeReportData.setKey3("1");
						break;
					}
				case FundsConstant.CASH_DRAWER_INDI :
					// cash drawer -- need to be first in the sort
					{
						laPaymentTypeReportData.setKey3("0");
						break;
					}
			}
			// key 4 value will always be the Payment type code
			// description
			laPaymentTypeReportData.setKey4(
				laPaymentTypeReportData.getPymntTypeCdDesc());
			// update the query with the keys we just set
			avQueryResults.setElementAt(laPaymentTypeReportData, i);
		}
	}

	/**
	 * Set the SubHeader Id AbstractValue.
	 * 
	 * @throws RTSException
	 */
	public void setSubHeaderIdValue() throws RTSException
	{
		Vector lvDrawers =
			(Vector) caFundsData.getSelectedCashDrawers();
		if (caFundsData.getFundsReportData().getRange()
			== FundsConstant.DATE_RANGE)
		{
			// defect 9943 
			csStartDate =
				caFundsData
					.getFundsReportData()
					.getFromRange()
					.toString()
					+ " "
					+ caFundsData
						.getFundsReportData()
						.getFromRange()
						.getClockTimeNoMs();
						//.getClockTime().substring(0, 8);
			csEndDate =
				caFundsData
					.getFundsReportData()
					.getToRange()
					.toString()
					+ " "
					+ caFundsData
						.getFundsReportData()
						.getToRange()
						.getClockTimeNoMs();
						//.getClockTime().substring(0,8);
			// end defect 9943 
		}
		switch (ciBreakByCode)
		{
			case CASH_DRAWER_EMP : // by Workstation, employee, and pay
				// desc code			
				{
					// empty code block
				}
			case CASH_DRAWER_NONE : // by workstation and pay desc code
				{
					if (ciBreakByCode == CASH_DRAWER_NONE)
					{
						csSubHeaderIdValue = csHoldKey1;
					}
					else
					{
						csSubHeaderIdValue = csHoldKey2;
					}
					if (caFundsData.getFundsReportData().getRange()
						!= FundsConstant.DATE_RANGE)
					{
						for (int i = 0; i < lvDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvDrawers
										.get(
									i);
							if (Integer.parseInt(csHoldKey1)
								== laDrawer.getCashWsId())
							{
								// defect 9943
								csStartDate =
									laDrawer
										.getCloseOutBegTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutBegTstmp()
											.getClockTimeNoMs();
											//.getClockTime()
											//.substring(0,8);
								csEndDate =
									laDrawer
										.getCloseOutEndTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutEndTstmp()
											.getClockTimeNoMs();
											//.getClockTime()
											//.substring(0,8);
								// end defect 9943
								break;
							}
						}
					}
					break;
				}
			case PaymentReportCloseOut.EMP_NONE :
				// by Employee and pay desc
				{
					csSubHeaderIdValue = csHoldKey1;
					break;
				}
			case PaymentReportCloseOut.EMP_CASH_DRAWER :
				// by Employee, Cash Drawer,  and pay desc
				{
					csSubHeaderIdValue = csHoldKey2;
					break;
				}
		}
		if (caFundsData.getFundsReportData().getEntity()
			== FundsConstant.CASH_DRAWER)
		{
			for (int l = 0;
				l < caFundsData.getSelectedCashDrawers().size();
				l++)
			{
				CashWorkstationCloseOutData laCashDrwrObj =
					(CashWorkstationCloseOutData) caFundsData
						.getSelectedCashDrawers()
						.elementAt(l);
				int liCashDrwr = laCashDrwrObj.getCashWsId();
				if (caFundsData.getFundsReportData().getRange()
					!= FundsConstant.CLOSE_OUT_FOR_DAY)
				{
					if (Integer.parseInt(csHoldKey1) == liCashDrwr)
					{
						if (caFundsData.getFundsReportData().getRange()
							!= FundsConstant.CLOSE_OUT_FOR_DAY)
						{
							// defect 9943 
							laCashDrwrObj.setRptStatus(
							FundsConstant.REPORT_GENERATED); 
							//"Report Generated");
							// end defect 9943 
						}
						laCashDrwrObj.setBsnDateTotalAmt(
							caTotalForKey1);
					}
				}
				// check to see if the cash drawer number in the
				// closeout object is greater than the cash drawer
				// number on hold.  If the Object is cash drawer number
				// is greater than what is on hold, we need to get out
				// of the for loop.  Otherwise, we need to keep
				// searching for a matching cash drawer number.  This
				// corrects defect 4286 that stated that the report
				// status was sometimes not stating the correct thing.
				if (Integer.parseInt(csHoldKey1) <= liCashDrwr)
				{
					break;
				}
			}
		}
		else if (
			caFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE)
		{
			for (int l = 0;
				l < caFundsData.getSelectedEmployees().size();
				l++)
			{
				EmployeeData laEmployeeObj =
					(EmployeeData) caFundsData
						.getSelectedEmployees()
						.elementAt(
						l);
				String lsEmployeeId = laEmployeeObj.getEmployeeId();
				if (csHoldKey1.equals(lsEmployeeId))
				{
					// defect 9943 
					//laEmployeeObj.setRptStatus("Report Generated");
					laEmployeeObj.setRptStatus(FundsConstant.REPORT_GENERATED);
					// end defect 9943 					
					
					break;
				}
			}
		}
	}

	/**
	 * setSummaryForText
	 * 
	 * @param asId String
	 * @throws RTSException
	 */
	public void setSummaryForText(String asId) throws RTSException
	{
		switch (ciBreakByCode)
		{
			case PaymentReportCloseOut.CASH_DRAWER_NONE :
				{
					// empty code block
				}
			case PaymentReportCloseOut.EMP_CASH_DRAWER :
				{
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_CASH_DRAWER;
					csTotalFor = ReportConstant.TOTAL_FOR_CASH_DRAWER;
					break;
				}
			case PaymentReportCloseOut.CASH_DRAWER_EMP :
				{
					// empty code block
				}
			case PaymentReportCloseOut.EMP_NONE :
				{
					csSummaryFor = ReportConstant.SUMMARY_FOR_EMPLOYEE;
					csTotalFor = ReportConstant.TOTAL_FOR_EMPLOYEE;
					break;
				}
		}
		csSummaryFor += asId + ":";
		csTotalFor += asId + ":";
	}

	/**
	 * This method set the text for the type of cash drawer operation
	 * that is occrring.  It is used for summary headings and totals for
	 * employees and cash drawer reports.
	 *
	 * the key passed in the the sort order of the the cashdrawerindi.
	 * 0 indicates that is is a cash drawer and 1 indicates that it is
	 * non cash drawer.  The specs indicate that cash drawer must be
	 * printed out first. This CashDrawerIndi values are 0 = Non cash
	 * drawer and 1 = cash drawer.  These values needed to be reversed
	 * for the sort to happen correctly.
	 * 
	 * @param aiCashDrawerIndiSortKey int
	 * @throws RTSException
	 */
	public void setTextForCashDrawerIndi(int aiCashDrawerIndiSortKey)
		throws RTSException
	{
		switch (aiCashDrawerIndiSortKey)
		{
			case PaymentReportCloseOut.CASH_DRAWER_INDI_FOR_SORT :
				// cash drawer
				{
					csCashDrawerIndiText =
						ReportConstant.CASH_DRAWER_OPER;
					csCashDrawerIndiTextTotals =
						ReportConstant.TOTAL_CASH_DRAWER_OPER;
					break;
				}
			case PaymentReportCloseOut.NON_CASH_DRAWER_INDI_FOR_SORT :
				// non-cash drawer
				{
					csCashDrawerIndiText =
						ReportConstant.NON_CASH_DRAWER_OPER;
					csCashDrawerIndiTextTotals =
						ReportConstant.TOTAL_NON_CASH_DRAWER_OPER;
					break;
				}
			default :
				{
					// need rts exception
					break;
				}
		}
	}

	/**
	 * Set Up Break By Code
	 * 
	 * @throws RTSException
	 */
	public void setUpBreakByCode() throws RTSException
	{
		// add additional main headings
		switch (ciEntity)
		{
			case FundsConstant.CASH_DRAWER :
				{
					switch (ciPrimarySplit)
					{
						case FundsConstant.NONE :
							{
								ciBreakByCode = CASH_DRAWER_NONE;
								return;
							}
						case FundsConstant.EMPLOYEE :
							{
								ciBreakByCode = CASH_DRAWER_EMP;
								return;
							}
						default :
							{
								// error message
								return;
							}
					} // end primary split for entity cash drawer 	
				} // end of entity cash drawer
			case FundsConstant.EMPLOYEE :
				{
					switch (ciPrimarySplit)
					{
						case FundsConstant.NONE :
							{
								ciBreakByCode = EMP_NONE;
								return;
							}
						case FundsConstant.CASH_DRAWER :
							{
								ciBreakByCode = EMP_CASH_DRAWER;
								return;
							}
						default :
							{
								// error message
								return;
							}
					} // end primary split for entity Employee
				} // end of entity Employee
		} // end of switch of for all entities
	}

	/**
	 * Store the objects for later use.
	 * 
	 * @throws RTSException
	 */
	public final void storeObjectsToAccumulateTotals()
		throws RTSException
	{
		// put in a dataobject to accumulate totals for the summary
		// page, do not do this is we are processing the summary page
		if (!cbSummaryPageNeeded)
		{
			PaymentTypeReportData laPaymentTypeReportData =
				new PaymentTypeReportData();
			laPaymentTypeReportData.setPymntTypeCdDesc(
				csHoldDescription);
			laPaymentTypeReportData.setPymntTypeAmt(caTotalForItem);
			laPaymentTypeReportData.setPymntTypeQty(ciTotalQty);
			laPaymentTypeReportData.setCashDrawerIndi(
				(Integer.parseInt(csHoldKey3)));
			cvSummaryData.addElement(laPaymentTypeReportData);
			// add an element for summary page
			switch (ciBreakByCode)
			{
				case PaymentReportCloseOut.CASH_DRAWER_EMP :
					{
						// empty code block
					}
				case PaymentReportCloseOut.EMP_CASH_DRAWER :
					{
						cvSummaryDataForEntity.addElement(
							laPaymentTypeReportData);
						return;
					}
			}
		}
	}
}
