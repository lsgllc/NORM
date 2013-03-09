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
 * PaymentReportCloseOutShowSource.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Listberger	11/15/2001	Added column and totals for internet
 * M Listberger	05/10/2002  deleted private int
 * 							WIDOW_ORPHAN_LINES = 5; not used.
 *                          removed extra colon for total summary.
 * S Govindappa	05/15/2002  Fixed CQU100003942 by Adding the
 * 							local setFooter method to move the 
 *                          Page No printed left by four
 * 							characters.
 * Ray Rowehl	05/28/2002	call printTransaction more liberally
 *							to allow report lines
 *							to fill the page more completely.
 *							Changed this class so it closes out
 *							each report line with a	"nextLine".
 *							This allows for better control of
 *							printing.  touch appendDetailLine,
 *							printTransaction, 
 *							processAndPrintTotalInformation,
 *							processAtBreak,	and
 *							processSummaryData.
 *							Make REPORT_FOOTER_LINES = 5.
 *							defect 3847
 * M. Listberger 07/02/2002  Added code to display
 * 							"Current Status" instead of
 * 							"Cash Drawer" when selecting the
 * 							current status option. 
 * 							Modified printMainHeader() method.
 * 							CQU100004402.
 * Ray Rowehl	01/05/2004	Make Report contants static and
 * 							refer to them as static.  Also did
 * 							some minor code formatting.
 * 							modify CASH_DRAWER_NONE,
 * 							CASH_DRAWER_EMP, EMP_NONE,
 * 							EMP_CASH_DRAWER, main(),
 *							processAndPrintTotalInformation(),
 *							setSortKeysForByCashDrawer(),
 *							setSubHeaderIdValue(),
 *							setSummaryForText()
 * 							defect 6733 ver 5.1.5 fix 2
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							add getPymtStatus(), getPymtStatVec()
 *							defect 7896 Ver 5.2.3
 * K Harrell	10/24/2005	Constant renaming 
 * 							defect 8379 Ver 5.2.3
 * K Harrell	12/29/2005	ArrayIndexOutOfBounds error, referencing
 * 							element 0 of empty vector.   
 * 							modify formatReport(),
 * 							processAndPrintTotalInformation() 
 * 							defect 8499 Ver 5.2.3
 * J Ralph		01/27/2006	FormFeed constant cleanup
 * 							Source format
 * 						 	modify generateFooter()
 * 							defect 8524 Ver 5.2.3
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs()
 * 							modify generateFooter(), formatReport(),
 * 							 setSubHeaderIdValue()
 * 							defect 9943 Ver Defect_POS_F     
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	08/25/2009	Unneeded local generateFooter().  
 * 							 Inconsistent placement Page No. w/ 
 * 							 ReportTemplate.  
 * 							delete generateFooter() 
 * 							defect 8628 Ver Defect_POS_F    	 
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Payment Close Out Show Source Reports
 * THIS REPORT NEEDS TO BE PRINTED LANDSCAPE
 *
 * @version	Defect_POS_F	08/25/2009
 * @author  Margaret Listberger
 * <br>Creation Date: 		09/07/2001 14:45:34
 */
public class PaymentReportCloseOutShowSource extends ReportTemplate
{
	private FundsData caFundsData;

	public int ciEntity = 0;
	public int ciPrimarySplit = 0;

	// set  up hold keys that will be used in "at break" logic
	private String csHoldKey1;
	private String csHoldKey2;
	private String csHoldKey3;
	private String csHoldKey4;
	private String csStartDate;
	private String csEndDate;

	// set up variables to capture the variable report text strings to
	// be used
	private String csHeaderIdValue;
	private String csSubHeaderIdValue;

	private String csCashDrawerIndiText;
	private String csCashDrawerIndiTextTotals;
	private String csSummaryFor;
	private String csTotalFor;

	// set up variable to hold the payment description code
	private String csHoldDescription;

	// string to concatenate and workstation id and they will print on
	// the summary page
	private String csWsIDsForHeader = "";

	// hold the fee source code	
	private int ciHoldFeeSourceCode;

	// sets up the type of sort to be performed, see setUpBreakByCodes()
	public int ciBreakByCode;

	// used for indicating whether or not a new line is needed
	// (for printing)
	private int ciPrintLineIndex = 1;

	// set up switch to indicate whether or not we are print the summary
	// page
	private boolean cbSummaryPageNeeded = false;
	private boolean cbLastRecord = false;

	// set up a vector for grand total accumulation.  Will use to print
	// summary.
	private Vector cvSummaryData = new Vector();
	private Vector cvSummaryDataForEntity = new Vector();

	// used for page breaking
	private Vector cvPrintVector = new Vector();

	// set up an accumulator for qty
	private int ciItemCount = 0;

	// field for detail lines that are available
	private int ciDetailLines = 0;

	// field to count how many employees there are in a cash drawer
	private int ciKey2Count = 0;

	// set up constants for cash drawer indicators
	private static final int CASH_DRAWER_INDI_FOR_SORT = 0;
	private static final int NON_CASH_DRAWER_INDI_FOR_SORT = 1;

	// set up dollar fields used in accumulating
	private int ciItemCountCust = 0;
	private int ciItemCountSubCon = 0;
	private int ciItemCountDealer = 0;
	private int ciItemCountInternet = 0;
	private int ciCurrFeeSourceCd = 0;

	private Dollar NEW_DOLLAR = new Dollar("0");

	private Dollar caTotalForKey1Cust = NEW_DOLLAR;
	private Dollar caTotalForKey1SubCon = NEW_DOLLAR;
	private Dollar caTotalForKey1Dealer = NEW_DOLLAR;
	private Dollar caTotalForKey1Internet = NEW_DOLLAR;
	private Dollar caTotalForKey1 = NEW_DOLLAR;

	private Dollar caTotalForKey2Cust = NEW_DOLLAR;
	private Dollar caTotalForKey2SubCon = NEW_DOLLAR;
	private Dollar caTotalForKey2Dealer = NEW_DOLLAR;
	private Dollar caTotalForKey2Internet = NEW_DOLLAR;
	private Dollar caTotalForKey2 = NEW_DOLLAR;

	private Dollar caTotalForCashDrawerIndiCust = NEW_DOLLAR;
	private Dollar caTotalForCashDrawerIndiSubCon = NEW_DOLLAR;
	private Dollar caTotalForCashDrawerIndiDealer = NEW_DOLLAR;
	private Dollar caTotalForCashDrawerIndiInternet = NEW_DOLLAR;
	private Dollar caTotalForCashDrawerIndi = NEW_DOLLAR;

	private Dollar caTotalForPayDescCust = NEW_DOLLAR;
	private Dollar caTotalForPayDescSubCon = NEW_DOLLAR;
	private Dollar caTotalForPayDescDealer = NEW_DOLLAR;
	private Dollar caTotalForPayDescInternet = NEW_DOLLAR;
	private Dollar caTotalForAllPayDesc = NEW_DOLLAR;

	//  Fee source constants
	private static final int FEE_SOURCE_CUST = 1;
	private static final int FEE_SOURCE_SUBCON = 2;
	private static final int FEE_SOURCE_DEALER = 3;
	private static final int FEE_SOURCE_INTERNET = 5;

	// Sort constant
	public static final int CASH_DRAWER_NONE = 0;
	public static final int CASH_DRAWER_EMP = 1;
	public static final int EMP_NONE = 2;
	public static final int EMP_CASH_DRAWER = 3;

	// set at break variables
	private static final int BREAK_KEY_1 = 1;
	// Cash Drawer, Employee, or Substation
	private static final int BREAK_KEY_2 = 2;
	// Cash Drawer, Employee, or None
	private static final int BREAK_KEY_3 = 3;
	// Cash Drawer/Non-cash drawer indi (sort order)
	private static final int BREAK_KEY_4 = 4; // Payment Description

	//    constants for report format

	// Initialize LEFT, CENTER, and RIGHT variables
	private static final int LEFT = 0;
	private static final int CENTER = 1;
	private static final int RIGHT = 2;

	// defect 3847
	// reserve 5 lines at bottom to ensure better grouping.
	// this is needed because the method of grouping report lines has
	// changed
	private final int REPORT_FOOTER_LINES = 5;
	// end defect 3847

	// standard column lengths for column headings and data
	private static final int HDR_LENGTH1 = 25;
	private static final int AMOUNT_LENGTH = 16;
	private static final int QTY_LENGTH = 5;

	// standard column names
	private static final String AMOUNT_HDR = "AMOUNT";
	private static final String QTY_HDR = "QTY";

	// main heading
	private static final String TRANS_FROM = "TRANSACTIONS FROM";

	// first line of column heading
	private static final String CUST_HDR = "CUSTOMER";
	private static final String SUBCON_HDR = "SUBCONTRACTOR";
	private static final String DEALER_HDR = "DEALER";
	private static final String INTERNET_HDR = "INTERNET";
	private static final String TOTAL_HDR = "TOTAL";

	// second line of column heading
	private static final int DESCRIPTION_STARTPT = 3;
	private static final int DESCRIPTION_LENGTH = 38;
	private static final String DESCRIPTION_HEADER = "DESCRIPTION";

	private static final int CUST_AMT_STARTPT = 41;
	private static final int CUST_QTY_STARTPT = 58;

	private static final int SUBCON_AMT_STARTPT = 64;
	private static final int SUBCON_QTY_STARTPT = 81;

	private static final int DEALER_AMT_STARTPT = 87;
	private static final int DEALER_QTY_STARTPT = 104;

	private static final int INTERNET_AMT_STARTPT = 110;
	private static final int INTERNET_QTY_STARTPT = 127;

	private static final int TOTAL_AMT_STARTPT = 133;
	private static final int TOTAL_QTY_STARTPT = 150;

	// accomodate for indentation of type of operation line and total
	private static final int CASH_DRAWR_INDI_STARTPT = 6;
	private static final int CASH_DRAWER_INDI_LENGTH = 35;

	// accomodate for indentation of data lines 
	private static final int DATALINE_STARTPT = 8;
	private static final int DATALINE_LENGTH = 30;

	// used for setReportNum set Report Title
	public String csReportNum = "";
	public String csReportName = FundsConstant.PAYMENT_REPORT;

	// dashes for total lines
	String SINGLE_DASHES = this.caRpt.singleDashes(AMOUNT_LENGTH);
	String DOUBLE_DASHES = this.caRpt.doubleDashes(AMOUNT_LENGTH);

	public ReportStatusData caPymtStatus = new ReportStatusData();
	public Vector cvPymtStatVec = new Vector();

	private static final String THROUGH = " THROUGH ";
	private static final String REPORT_TYPE = "REPORT TYPE: ";
	private static final String LAST_CURRENT_STATUS =
		"SINCE LAST CURRENT STATUS";
	private static final String DATE_RANGE = "DATE RANGE";
	private static final String NOT_DEFINED = "REPORT TYPE NOT DEFINED";
	private static final String CURRENT_STATUS = "CURRENT STATUS";

	// defect 8379 
	// Constant renaming; Close Out => Closeout  
	private static final String CLOSE_AFTER_SUBSTA_SUM =
		"CLOSEOUTS AFTER SUBSTATION SUMMARY";
	private static final String LAST_CLOSEOUT = "SINCE LAST CLOSEOUT";
	private static final String FOR_CLOSEOUT = "FOR LAST CLOSEOUT";
	private final String CLOSEOUT = "CLOSEOUT";
	// end defect 8379

	/**
	 * PaymentReportCloseOutShowSource constructor
	 */
	public PaymentReportCloseOutShowSource()
	{
		super();
	}

	/**
	 * PaymentReportCloseOutShowSource constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public PaymentReportCloseOutShowSource(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * This method accumules the dollar amount for the payment
	 * description code, the total for the individual work station, the 
	 * total for the individual employee, and the total for the cash
	 * drawer type.  The totals are used when printing out summaries.
	 * 
	 * @param aaPymntTypeAmt Dollar
	 * @param aiItemCount int
	 * @param aiFeeSourceCode int
	 * @throws RTSException
	 */
	public void accumulateDollarAmounts(
		Dollar aaPymntTypeAmt,
		int aiItemCount,
		int aiFeeSourceCode)
		throws RTSException
	// accumulated totals for this payment Type
	{
		switch (aiFeeSourceCode)
		{
			case FEE_SOURCE_CUST : // customer
				{
					caTotalForKey1Cust =
						caTotalForKey1Cust.add(aaPymntTypeAmt);
					caTotalForKey2Cust =
						caTotalForKey2Cust.add(aaPymntTypeAmt);
					caTotalForCashDrawerIndiCust =
						caTotalForCashDrawerIndiCust.add(
							aaPymntTypeAmt);
					caTotalForPayDescCust =
						caTotalForPayDescCust.add(aaPymntTypeAmt);
					ciItemCountCust += aiItemCount;
					break;
				}
			case FEE_SOURCE_SUBCON : // subcontractor
				{
					caTotalForKey1SubCon =
						caTotalForKey1SubCon.add(aaPymntTypeAmt);
					caTotalForKey2SubCon =
						caTotalForKey2SubCon.add(aaPymntTypeAmt);
					caTotalForCashDrawerIndiSubCon =
						caTotalForCashDrawerIndiSubCon.add(
							aaPymntTypeAmt);
					caTotalForPayDescSubCon =
						caTotalForPayDescSubCon.add(aaPymntTypeAmt);
					ciItemCountSubCon += aiItemCount;
					break;
				}
			case FEE_SOURCE_DEALER : // dealer
				{
					caTotalForKey1Dealer =
						caTotalForKey1Dealer.add(aaPymntTypeAmt);
					caTotalForKey2Dealer =
						caTotalForKey2Dealer.add(aaPymntTypeAmt);
					caTotalForCashDrawerIndiDealer =
						caTotalForCashDrawerIndiDealer.add(
							aaPymntTypeAmt);
					caTotalForPayDescDealer =
						caTotalForPayDescDealer.add(aaPymntTypeAmt);
					ciItemCountDealer += aiItemCount;
					break;
				}
			case FEE_SOURCE_INTERNET : // internet
				{
					caTotalForKey1Internet =
						caTotalForKey1Internet.add(aaPymntTypeAmt);
					caTotalForKey2Internet =
						caTotalForKey2Internet.add(aaPymntTypeAmt);
					caTotalForCashDrawerIndiInternet =
						caTotalForCashDrawerIndiInternet.add(
							aaPymntTypeAmt);
					caTotalForPayDescInternet =
						caTotalForPayDescInternet.add(aaPymntTypeAmt);
					ciItemCountInternet += aiItemCount;
					break;
				}
		}
		// accumulated totals for all types
		caTotalForKey1 = caTotalForKey1.add(aaPymntTypeAmt);
		caTotalForKey2 = caTotalForKey2.add(aaPymntTypeAmt);
		caTotalForCashDrawerIndi =
			caTotalForCashDrawerIndi.add(aaPymntTypeAmt);
		caTotalForAllPayDesc = caTotalForAllPayDesc.add(aaPymntTypeAmt);

		ciItemCount += aiItemCount;
	}

	/**
	 * Append Blank Line
	 * 
	 * @throws RTSException
	 */
	public void appendBlankLine() throws RTSException
	{
		buildPrintVector("", 1, 1, LEFT);
		ciPrintLineIndex++;
	}

	/**
	 * Append Description To Print Vector
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
	 * This method prints the detail line from processSummaryVector.
	 *
	 * @exception RTSException 
	 */
	public void appendDetailLine() throws RTSException
	{
		if (!(caTotalForPayDescCust.equals(NEW_DOLLAR))
			|| !(caTotalForPayDescDealer.equals(NEW_DOLLAR))
			|| !(caTotalForPayDescInternet.equals(NEW_DOLLAR))
			|| !(caTotalForPayDescSubCon.equals(NEW_DOLLAR)))
		{
			appendDescriptionToPrintVector(
				csHoldDescription,
				DATALINE_STARTPT,
				DATALINE_LENGTH);
			appendItemAmountAndQty();

			// defect 3487
			// allow print lines to spread across pages..
			printTransaction();
			// end defect 3487
		}
	}

	/**
	 * Append Double Dashes
	 * 
	 * @throws RTSException
	 */
	public void appendDoubleDashes() throws RTSException
	{ // create the double dashed lines
		buildPrintVector(
			DOUBLE_DASHES,
			CUST_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			DOUBLE_DASHES,
			SUBCON_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			DOUBLE_DASHES,
			DEALER_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			DOUBLE_DASHES,
			INTERNET_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			DOUBLE_DASHES,
			TOTAL_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		ciPrintLineIndex++;
	}

	/**
	 * Append Item Amount And Quantity
	 * 
	 * @throws RTSException
	 */
	public void appendItemAmountAndQty() throws RTSException
	{
		// print the total and total quantity for each item
		if (!(caTotalForPayDescCust.equals(NEW_DOLLAR)))
			// has a balance
		{
			buildPrintVector(
				caTotalForPayDescCust.printDollar().substring(1),
				CUST_AMT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			if (ciItemCountCust != 0
				&& !(csHoldDescription.equals("CASH")))
			{
				buildPrintVector(
					String.valueOf(ciItemCountCust),
					CUST_QTY_STARTPT,
					QTY_LENGTH,
					RIGHT);
			}
		}
		if (!(caTotalForPayDescSubCon.equals(NEW_DOLLAR)))
			// has a balance
		{
			buildPrintVector(
				caTotalForPayDescSubCon.printDollar().substring(1),
				SUBCON_AMT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			if (ciItemCountSubCon != 0
				&& !(csHoldDescription.equals("CASH")))
			{
				buildPrintVector(
					String.valueOf(ciItemCountSubCon),
					SUBCON_QTY_STARTPT,
					QTY_LENGTH,
					RIGHT);
			}
		}
		if (!(caTotalForPayDescDealer.equals(NEW_DOLLAR)))
			// has a balance
		{
			buildPrintVector(
				caTotalForPayDescDealer.printDollar().substring(1),
				DEALER_AMT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			if (ciItemCountDealer != 0
				&& !(csHoldDescription.equals("CASH")))
			{
				buildPrintVector(
					String.valueOf(ciItemCountDealer),
					DEALER_QTY_STARTPT,
					QTY_LENGTH,
					RIGHT);
			}
		}
		if (!(caTotalForPayDescInternet.equals(NEW_DOLLAR)))
			// has a balance
		{
			buildPrintVector(
				caTotalForPayDescInternet.printDollar().substring(1),
				INTERNET_AMT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			if (ciItemCountInternet != 0
				&& !(csHoldDescription.equals("CASH")))
			{
				buildPrintVector(
					String.valueOf(ciItemCountInternet),
					INTERNET_QTY_STARTPT,
					QTY_LENGTH,
					RIGHT);
			}
		}
		if (!(caTotalForAllPayDesc.equals(NEW_DOLLAR)))
			// has a balance
		{
			buildPrintVector(
				caTotalForAllPayDesc.printDollar().substring(1),
				TOTAL_AMT_STARTPT,
				AMOUNT_LENGTH,
				RIGHT);
			if (ciItemCount != 0
				&& !(csHoldDescription.equals("CASH")))
			{
				buildPrintVector(
					String.valueOf(ciItemCount),
					TOTAL_QTY_STARTPT,
					QTY_LENGTH,
					RIGHT);
			}
		}
		ciPrintLineIndex++;
	}

	/**
	 * Print the Totals.
	 * 
	 * @param aaCustTotal Dollar
	 * @param aaSubconTotal Dollar
	 * @param aaDealerTotal Dollar
	 * @param aaInternetTotal Dollar
	 * @param aaTotalAmount Dollar
	 * @throws RTSException
	 */
	public void appendTotals(
		Dollar aaCustTotal,
		Dollar aaSubconTotal,
		Dollar aaDealerTotal,
		Dollar aaInternetTotal,
		Dollar aaTotalAmount)
		throws RTSException
	{
		// print out the totals for the report
		buildPrintVector(
			aaCustTotal.printDollar().substring(1),
			CUST_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			aaSubconTotal.printDollar().substring(1),
			SUBCON_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			aaDealerTotal.printDollar().substring(1),
			DEALER_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			aaInternetTotal.printDollar().substring(1),
			INTERNET_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			aaTotalAmount.printDollar().substring(1),
			TOTAL_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		ciPrintLineIndex++;
	}

	/**
	 * Print the Total Line including the dashes.
	 * 
	 * @param aaCustTotal Dollar
	 * @param aaSubconTotal Dollar
	 * @param aaDealerTotal Dollar
	 * @param aaInternetTotal Dollar
	 * @param aaTotalAmount Dollar
	 * @throws RTSException
	 */
	public void appendTotalsForCashDrawerIndi(
		Dollar aaCustTotal,
		Dollar aaSubconTotal,
		Dollar aaDealerTotal,
		Dollar aaInternetTotal,
		Dollar aaAmountTotal)
		throws RTSException
	{
		//  Create the single dashed lines
		buildPrintVector(
			SINGLE_DASHES,
			CUST_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			SINGLE_DASHES,
			SUBCON_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			SINGLE_DASHES,
			DEALER_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			SINGLE_DASHES,
			INTERNET_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		buildPrintVector(
			SINGLE_DASHES,
			TOTAL_AMT_STARTPT,
			AMOUNT_LENGTH,
			RIGHT);
		ciPrintLineIndex++;

		appendDescriptionToPrintVector(
			csCashDrawerIndiTextTotals,
			CASH_DRAWR_INDI_STARTPT,
			CASH_DRAWER_INDI_LENGTH);

		appendTotals(
			aaCustTotal,
			aaSubconTotal,
			aaDealerTotal,
			aaInternetTotal,
			aaAmountTotal);
	}

	/**
	 * Build Print Vector
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
	 * This is the format section of the report for payment close out
	 * and its many varriations
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
		ciEntity = aaFundsData.getFundsReportData().getEntity();
		ciPrimarySplit =
			aaFundsData.getFundsReportData().getPrimarySplit();

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
		setTextForCashDrawerIndi(Integer.parseInt(laPmyData.getKey3()));
		// the value we want is in key 3

		csWsIDsForHeader = String.valueOf(laPmyData.getKey1()) + " ";

		// set the hold keys and other variable text
		setHoldKeys(
			laPmyData.getKey1(),
			laPmyData.getKey2(),
			laPmyData.getKey3(),
			laPmyData.getKey4(),
			laPmyData.getFeeSourceCd());

		csHeaderIdValue = laPmyData.getKey1();
		setSubHeaderIdValue();
		setSummaryForText(csSubHeaderIdValue);

		// print the main and column headings
		printMainHeader();

		// set up the summary header info for the first record
		appendDescriptionToPrintVector(
			csSummaryFor,
			DESCRIPTION_STARTPT,
			DESCRIPTION_LENGTH);
		ciPrintLineIndex++;
		appendBlankLine();

		appendDescriptionToPrintVector(
			csCashDrawerIndiText,
			CASH_DRAWR_INDI_STARTPT,
			CASH_DRAWER_INDI_LENGTH);
		ciPrintLineIndex++;

		// set up int i for the loop processing	
		int i = 0;

		// get number of lines that available for printing	
		ciDetailLines =
			getNoOfDetailLines() - ReportConstant.REPORT_FOOTER_LINES;

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
				ciCurrFeeSourceCd = laPmyData.getFeeSourceCd();

				processCashDrawers(
					lsCurrKey1,
					lsCurrKey2,
					lsCurrKey3,
					lsCurrKey4,
					ciCurrFeeSourceCd);

				// accumulate all of the various totals
				accumulateDollarAmounts(
					laPmyData.getPymntTypeAmt(),
					laPmyData.getPymntTypeQty(),
					ciCurrFeeSourceCd);

				i++;
			} // while

			// print out the last record and store it
			cbLastRecord = true;

			processLastRecord(
				lsCurrKey1,
				lsCurrKey2,
				lsCurrKey3,
				lsCurrKey4,
				ciCurrFeeSourceCd);

			//check to see if we have more than one workstation

			// get the first reccord
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
				printTransaction();
				generateFooter();

				cbSummaryPageNeeded = true;
				csHeaderIdValue = csWsIDsForHeader;
				printMainHeader();

				csSummaryFor = ReportConstant.SUMMARY_FOR_REPORT_TEXT;
				csTotalFor = ReportConstant.REPORT_TOTAL_TEXT;

				// put the summary description into the print vector
				appendBlankLine();
				buildPrintVector(
					csSummaryFor,
					DESCRIPTION_STARTPT,
					DESCRIPTION_LENGTH,
					LEFT);
				ciPrintLineIndex++;
				appendBlankLine();
				// defect 8499 
				// Only process if data exists 
				if (cvSummaryData.size() > 0)
				{
					processSummaryVector(cvSummaryData);
				}
				// end defect 8499 
			}
		}
		// defect 8628 
		//generateEndOfReport();
		//generateFooter();
		generateFooter(true);
		// end defect 8628  

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
					//&& lsReportStatus.equals("Report Generated"))
					&& lsReportStatus.equals(
						FundsConstant.REPORT_GENERATED))
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

				cvPymtStatVec.add(laEmployee);
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

//	/**
//	 * This Method appends the footer information each time it is called.
//	 */
//	protected void generateFooter()
//	{
//		try
//		{
//			String lsTimeZone = "";
//			RTSDate laTimeZoneAdjustedDate = new RTSDate();
//
//			if (Comm.isServer())
//			{
//				CacheManagerServerBusiness laCacheManagerServerBusiness =
//					new CacheManagerServerBusiness();
//				lsTimeZone =
//					laCacheManagerServerBusiness.getOfficeTimeZone(
//						caRptProps.getOfficeIssuanceId(),
//						caRptProps.getSubstationId());
//				laTimeZoneAdjustedDate =
//					RTSDate.getCurrentDate(lsTimeZone);
//			}
//			caRpt.setFooter(2);
//			caRpt.println(
//				"RUNDATE  " + laTimeZoneAdjustedDate.toString());
//			// defect 9943 
//			caRpt.print(
//				"RUNTIME  "
//					+ laTimeZoneAdjustedDate.getClockTimeNoMs());
//			//getClockTime().substring(0,8));
//			// end defect 9943 
//
//			DecimalFormat laTwoDigits = new DecimalFormat("#0");
//			String lsPageFormated = laTwoDigits.format(caRpt.ciPages);
//			// defect 8524
//			// FormFeed constant cleanup 
//			caRpt.rightAlign(
//				"PAGE "
//					+ lsPageFormated
//					+ " "
//					+ ReportConstant.FF
//					+ "    ");
//			// end defect 8524
//		}
//		catch (RTSException aeRTSEx)
//		{
//			// do not show Server when an exception is shown
//			// Add exception thrown though.  CQU100003811
//			//throw new RTSException(RTSException.JAVA_ERROR, e);
//			//Temp code till I go back to fix the RTSException
//			aeRTSEx.printStackTrace();
//			RTSDate laTimeZoneAdjustedDate = new RTSDate();
//			caRpt.setFooter(2);
//			caRpt.println(
//				"RUNDATE "
//					+ laTimeZoneAdjustedDate.toString()
//					+ " Exception Thrown");
//			// defect 9943
//			caRpt.print(
//				"RUNTIME " + laTimeZoneAdjustedDate.getClockTimeNoMs());
//			//getClockTime().substring(0,8));
//			// end defect 9943
//
//			DecimalFormat laTwoDigits = new DecimalFormat("#0");
//			String lsPageFormated = laTwoDigits.format(caRpt.ciPages);
//			// defect 8524
//			// FormFeed constant cleanup 
//			caRpt.rightAlign(
//				"PAGE "
//					+ lsPageFormated
//					+ " "
//					+ ReportConstant.FF
//					+ "    ");
//			// end defect 8524				
//		}
//	}

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
	 * @param aarrArgs String[] of command-line arguments
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{

		FundsData laFundsData = new FundsData();

		// Instantiating a new Report Class
		// set up blank title to be replaced once the new pmy object is
		// created.
		// ReportProperties aRptProps = new ReportProperties("");
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5211");
		laRptProps.setPageOrientation(ReportConstant.PORTRAIT);
		PaymentReportCloseOutShowSource laPmy =
			new PaymentReportCloseOutShowSource("", laRptProps);

		// Get the appropriate Report Number and Title
		laPmy.caRpt.csName = FundsConstant.PAYMENT_REPORT;

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryResults = laPmy.queryData(lsQuery);

		laPmy.setUpBreakByCode();
		switch (laPmy.ciBreakByCode)
		{
			case PaymentReportCloseOutShowSource.CASH_DRAWER_NONE :
				{
					// empty code block
				}
			case PaymentReportCloseOutShowSource.CASH_DRAWER_EMP :
				{
					laPmy.setSortKeysForByCashDrawer(lvQueryResults);
					break;
				}
			case PaymentReportCloseOutShowSource.EMP_NONE :
				{
					// empty code block
				}
			case PaymentReportCloseOutShowSource.EMP_CASH_DRAWER :
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
	 * Print Main Header
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
		if (cbSummaryPageNeeded) // if we are doing the summary page
		{
			// move the concatenated string to the variable that will be printed out
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
					lvHeader.addElement(CLOSE_AFTER_SUBSTA_SUM);
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
		// if ( (ciEntity == FundsConstant.CASH_DRAWER ||
		// cFundsData.getFundsReportData().getRange()
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
				CUST_HDR,
				CUST_AMT_STARTPT,
				HDR_LENGTH1,
				ReportConstant.CENTER);
		lvHeaderRow1.addElement(laColumn1);

		ColumnHeader laColumn2 =
			new ColumnHeader(
				SUBCON_HDR,
				SUBCON_AMT_STARTPT,
				HDR_LENGTH1,
				ReportConstant.CENTER);
		lvHeaderRow1.addElement(laColumn2);

		ColumnHeader laColumn3 =
			new ColumnHeader(
				DEALER_HDR,
				DEALER_AMT_STARTPT,
				HDR_LENGTH1,
				ReportConstant.CENTER);
		lvHeaderRow1.addElement(laColumn3);

		ColumnHeader laColumn3a =
			new ColumnHeader(
				INTERNET_HDR,
				INTERNET_AMT_STARTPT,
				HDR_LENGTH1,
				ReportConstant.CENTER);
		lvHeaderRow1.addElement(laColumn3a);

		ColumnHeader laColumn4 =
			new ColumnHeader(
				TOTAL_HDR,
				TOTAL_AMT_STARTPT,
				HDR_LENGTH1,
				ReportConstant.CENTER);
		lvHeaderRow1.addElement(laColumn4);

		lvTable.addElement(lvHeaderRow1);
		//Additing Row 1 ColumnHeader Information	

		// Set up the second line of the column headings
		ColumnHeader laColumn5 =
			new ColumnHeader(
				DESCRIPTION_HEADER,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH);
		lvHeaderRow2.addElement(laColumn5);

		ColumnHeader laColumn6 =
			new ColumnHeader(
				AMOUNT_HDR,
				CUST_AMT_STARTPT,
				AMOUNT_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn6);

		ColumnHeader laColumn7 =
			new ColumnHeader(QTY_HDR, CUST_QTY_STARTPT, QTY_LENGTH);
		lvHeaderRow2.addElement(laColumn7);

		ColumnHeader laColumn8 =
			new ColumnHeader(
				AMOUNT_HDR,
				SUBCON_AMT_STARTPT,
				AMOUNT_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn8);

		ColumnHeader laColumn9 =
			new ColumnHeader(QTY_HDR, SUBCON_QTY_STARTPT, QTY_LENGTH);
		lvHeaderRow2.addElement(laColumn9);

		ColumnHeader laColumn10 =
			new ColumnHeader(
				AMOUNT_HDR,
				DEALER_AMT_STARTPT,
				AMOUNT_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn10);

		ColumnHeader laColumn11 =
			new ColumnHeader(QTY_HDR, DEALER_QTY_STARTPT, QTY_LENGTH);
		lvHeaderRow2.addElement(laColumn11);

		ColumnHeader laColumn12 =
			new ColumnHeader(
				AMOUNT_HDR,
				INTERNET_AMT_STARTPT,
				AMOUNT_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn12);

		ColumnHeader laColumn13 =
			new ColumnHeader(QTY_HDR, INTERNET_QTY_STARTPT, QTY_LENGTH);
		lvHeaderRow2.addElement(laColumn13);

		ColumnHeader laColumn14 =
			new ColumnHeader(
				AMOUNT_HDR,
				TOTAL_AMT_STARTPT,
				AMOUNT_LENGTH,
				ReportConstant.CENTER);
		lvHeaderRow2.addElement(laColumn14);

		ColumnHeader laColumn15 =
			new ColumnHeader(QTY_HDR, TOTAL_QTY_STARTPT, QTY_LENGTH);
		lvHeaderRow2.addElement(laColumn15);

		// Adding 2 row of ColumnHeader IcsCurrencyFormatormation
		lvTable.addElement(lvHeaderRow2);

		// produce the header
		generateHeader(lvHeader, lvTable);
	}

	/**
	 * Proces through the Print Vector to print the data to the page.
	 * 
	 * @throws RTSException
	 */
	public void printTransaction() throws RTSException
	{
		int liLineNumber = 1;
		// initialize to one because that is the first line number coming in
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
			// but different start and end points.  Just look at the
			// last one to get the maximum of lines needed.
			if ((this.caRptProps.getPageHeight()
				- this.caRpt.getCurrX()
				- REPORT_FOOTER_LINES)
				< liNumLinesToBePrinted)
			{
				generateFooter();
				printMainHeader();
				// defect 3487 do not print extra blank line
				//appendBlankLine();
				// end defect 3487
			}
			else
			{
				// defect 3487 do not print extra blank line
				//appendBlankLine();
				// end defect 3487
			}

			int i = 0;

			// process through the print vector until it is all done.
			while (cvPrintVector != null && i < cvPrintVector.size())
			{
				if (i == 0)
				{
					// defect 3487 do not print blankline here
					//appendBlankLine();
					// end defect 3487
				}

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
		// defect 3487
		// closeout the last line
		this.caRpt.nextLine();
		// end defect 3487
		cvPrintVector.clear();
	}

	/**
	 * Process And Print Total Information
	 *
	 * @throws RTSException
	 */
	public void processAndPrintTotalInformation() throws RTSException
	{
		setSubHeaderIdValue();
		setSummaryForText(csSubHeaderIdValue);
		switch (ciBreakByCode)
		{
			case PaymentReportCloseOutShowSource.CASH_DRAWER_NONE :
				// by cash drawer and pay desc
				{
					// empty code block
				}
			case PaymentReportCloseOutShowSource.EMP_NONE :
				{
					csCashDrawerIndiText =
						ReportConstant.CASH_DRAWER_OPER;
					csCashDrawerIndiTextTotals =
						ReportConstant.CASH_DRAWER_OPER;
					appendBlankLine();

					appendDoubleDashes();

					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);

					appendTotals(
						caTotalForKey1Cust,
						caTotalForKey1SubCon,
						caTotalForKey1Dealer,
						caTotalForKey1Internet,
						caTotalForKey1);
					return;
				}
			case PaymentReportCloseOutShowSource.CASH_DRAWER_EMP :
				// by cash drawer, employee pay desc
				{
					appendBlankLine();
					appendDoubleDashes();

					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);

					appendTotals(
						caTotalForKey2Cust,
						caTotalForKey2SubCon,
						caTotalForKey2Dealer,
						caTotalForKey2Internet,
						caTotalForKey2);

					// set up variables for the printing of the summary at
					// the bottom of the page
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_CASH_DRAWER
							+ csHoldKey1
							+ ":";
					csTotalFor =
						ReportConstant.TOTAL_FOR_CASH_DRAWER
							+ csHoldKey1
							+ ":";
					break;
				} // end case CASH_DRAWER_EMP
			case PaymentReportCloseOutShowSource.EMP_CASH_DRAWER :
				{
					appendBlankLine();

					appendDoubleDashes();

					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);

					appendTotals(
						caTotalForKey2Cust,
						caTotalForKey2SubCon,
						caTotalForKey2Dealer,
						caTotalForKey2Internet,
						caTotalForKey2);
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
		// employee per page
		if (ciKey2Count > 0)
		{
			printTransaction();
			// print the summary header
			// defect 3487 add a blankline
			appendBlankLine();
			// end defect 3487

			appendDescriptionToPrintVector(
				csSummaryFor,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH);
			ciPrintLineIndex++;

			// defect 8499 
			// Only process if data exists
			if (cvSummaryDataForEntity.size() > 0)
			{
				processSummaryVector(cvSummaryDataForEntity);
			}
			// end defect 8499 

			appendTotalsForCashDrawerIndi(
				caTotalForCashDrawerIndiCust,
				caTotalForCashDrawerIndiSubCon,
				caTotalForCashDrawerIndiDealer,
				caTotalForCashDrawerIndiInternet,
				caTotalForCashDrawerIndi);

			appendBlankLine();

			appendDoubleDashes();
			buildPrintVector(
				csTotalFor,
				CASH_DRAWR_INDI_STARTPT,
				CASH_DRAWER_INDI_LENGTH,
				LEFT);

			appendTotals(
				caTotalForKey1Cust,
				caTotalForKey1SubCon,
				caTotalForKey1Dealer,
				caTotalForKey1Internet,
				caTotalForKey1);
		} // end if ciNumEmp > 0
	}

	/**
	 * Determine what breaks exist and handle them.
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @param aiTypeOfBreak int
	 * @param aiFeeSourceCode int
	 * @throws RTSException
	 */
	public void processAtBreak(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4,
		int aiTypeOfBreak,
		int aiFeeSourceCode)
		throws RTSException
	{
		// print the previous line of data only if it isn't the last
		// record.
		// The last record detail line is printed in the
		// processLastRecord method.	
		if (!cbLastRecord)
		{
			if (!(caTotalForPayDescCust.equals(NEW_DOLLAR))
				|| !(caTotalForPayDescDealer.equals(NEW_DOLLAR))
				|| !(caTotalForPayDescInternet.equals(NEW_DOLLAR))
				|| !(caTotalForPayDescSubCon.equals(NEW_DOLLAR)))
			{
				appendDescriptionToPrintVector(
					csHoldDescription,
					DATALINE_STARTPT,
					DATALINE_LENGTH);
				appendItemAmountAndQty();

				// defect 3487 allow groups of printlines to break across pages
				printTransaction();
				// end defect 3487

				setAmountsForStoringObjects();
			}
		}
		switch (aiTypeOfBreak)
		{
			case BREAK_KEY_1 : // break of value in Key1 (can be Cash
				// Drawer, Employee, or Substation #).
				{
					// print the total for the cash drawer type
					if (!cbLastRecord)
					{
						appendTotalsForCashDrawerIndi(
							caTotalForCashDrawerIndiCust,
							caTotalForCashDrawerIndiSubCon,
							caTotalForCashDrawerIndiDealer,
							caTotalForCashDrawerIndiInternet,
							caTotalForCashDrawerIndi);
					}

					processAndPrintTotalInformation();
					printTransaction();

					// reset the data for used for the workstation
					cvSummaryDataForEntity.clear();

					// reset the accumulators
					resetKey1Accumulators();
					resetKey2Accumulators();
					resetCashDrawerIndiTotals();
					resetPayDescAccumulators();
					ciKey2Count = 0;

					// set up the hold keys	
					csHeaderIdValue = asCurrKey1;
					// need this for the headers
					setTextForCashDrawerIndi(
						(Integer.parseInt(asCurrKey3)));
					if (!cbLastRecord)
					{
						csWsIDsForHeader += csHeaderIdValue + " ";
						// print out all workstations ids on final summary page.
					}
					break;
				}
			case BREAK_KEY_2 : // break of value in Key2 (can be Cash
				// Drawer, Employee #, this field may also be blank).
				{
					// print the totals for primary entity split
					appendTotalsForCashDrawerIndi(
						caTotalForCashDrawerIndiCust,
						caTotalForCashDrawerIndiSubCon,
						caTotalForCashDrawerIndiDealer,
						caTotalForCashDrawerIndiInternet,
						caTotalForCashDrawerIndi);

					appendBlankLine();

					appendDoubleDashes();

					// print the totals for the the split on entity
					buildPrintVector(
						csTotalFor,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH,
						LEFT);
					appendTotals(
						caTotalForKey2Cust,
						caTotalForKey2SubCon,
						caTotalForKey2Dealer,
						caTotalForKey2Internet,
						caTotalForKey2);

					printTransaction();

					ciKey2Count += 1;

					setSummaryForText(asCurrKey2);
					setTextForCashDrawerIndi(
						(Integer.parseInt(asCurrKey3)));

					// defect 3487
					// put a blank line between the totals and the next description
					appendBlankLine();
					// end defect 3487

					appendDescriptionToPrintVector(
						csSummaryFor,
						DESCRIPTION_STARTPT,
						DESCRIPTION_LENGTH);
					appendBlankLine();
					appendBlankLine();
					appendDescriptionToPrintVector(
						csCashDrawerIndiText,
						CASH_DRAWR_INDI_STARTPT,
						CASH_DRAWER_INDI_LENGTH);
					appendBlankLine();

					// reset amounts
					resetKey2Accumulators();
					resetPayDescAccumulators();
					resetCashDrawerIndiTotals();

					break;
				}
			case BREAK_KEY_3 : // break of value in key3 (break by cash
				// drawer/non cash drawer indi)
				{
					// print the summary the drawer drawer type
					appendTotalsForCashDrawerIndi(
						caTotalForCashDrawerIndiCust,
						caTotalForCashDrawerIndiSubCon,
						caTotalForCashDrawerIndiDealer,
						caTotalForCashDrawerIndiInternet,
						caTotalForCashDrawerIndi);

					// reset the amount fields	
					caTotalForCashDrawerIndi = NEW_DOLLAR;

					// get the text for the next type of Cash Drawer Indi
					setTextForCashDrawerIndi(
						(Integer.parseInt(asCurrKey3)));

					// if we aren't printing the last record, go ahead and
					// print the header for the next type of cash drawer
					if (!cbLastRecord)
					{
						appendBlankLine();
						appendDescriptionToPrintVector(
							csCashDrawerIndiText,
							CASH_DRAWR_INDI_STARTPT,
							CASH_DRAWER_INDI_LENGTH);
						appendBlankLine();
					}
					// reset the field that accumulate dollars and quantity
					// for each payment code desc
					resetCashDrawerIndiTotals();
					resetPayDescAccumulators();
					break;
				}
			case BREAK_KEY_4 : // break of value in key4
				// (break of payment desc)
				{
					resetPayDescAccumulators();
					break;
				}
		}
		// reset and load the hold keys with the current values
		setHoldKeys(
			asCurrKey1,
			asCurrKey2,
			asCurrKey3,
			asCurrKey4,
			aiFeeSourceCode);
	}

	/**
	 * Process Cash Drawers
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @param aiHoldFeeSourceCd int
	 * @throws RTSException
	 */
	public void processCashDrawers(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4,
		int aiHoldFeeSourceCd)
		throws RTSException
	{
		// check to see if the entity has changed
		// (cash drawer or Employee)
		if (asCurrKey1.equals(csHoldKey1))
		{
			// check to see if the primary split has changed
			// (cash drawer or employee)    
			if (asCurrKey2.equals(csHoldKey2))
			{
				// cash drawer indi's are the same
				if (asCurrKey3.equals(csHoldKey3))
				{
					// payment descriptions do not match
					if (!asCurrKey4.equals(csHoldKey4))
					{
						processAtBreak(
							asCurrKey1,
							asCurrKey2,
							asCurrKey3,
							asCurrKey4,
							BREAK_KEY_4,
							aiHoldFeeSourceCd);
					} // end of currKey4 not equal to csHoldKe4
				} // end of currKey3 equal to csHoldKey3
				else
				{ // currKey3 does not equal csHoldKey3
					processAtBreak(
						asCurrKey1,
						asCurrKey2,
						asCurrKey3,
						asCurrKey4,
						BREAK_KEY_3,
						aiHoldFeeSourceCd);
				} // end of currKey 3 not equalling csHold key 3
			} // end of currKey2 equals csHoldKey2
			else
			{ // currKey2 does not equal csHoldKey2
				processAtBreak(
					asCurrKey1,
					asCurrKey2,
					asCurrKey3,
					asCurrKey4,
					BREAK_KEY_2,
					aiHoldFeeSourceCd);
			} // end of currKey 2 not equal to csHoldKey2
		} // end of currKey 1  equals csHoldKey1
		else
		{ // currKey1 does not equal csHoldKey1
			processAtBreak(
				asCurrKey1,
				asCurrKey2,
				asCurrKey3,
				asCurrKey4,
				BREAK_KEY_1,
				aiHoldFeeSourceCd);
			// print the next pages header					
			generateFooter();
			printMainHeader();

			// set up the summary header info for the first record
			appendDescriptionToPrintVector(
				csSummaryFor,
				DESCRIPTION_STARTPT,
				DESCRIPTION_LENGTH);
			appendBlankLine();
			appendBlankLine();

			// set up the cash drawer heading indi.
			appendDescriptionToPrintVector(
				csCashDrawerIndiText,
				CASH_DRAWR_INDI_STARTPT,
				CASH_DRAWER_INDI_LENGTH);
			appendBlankLine();
		} // end of currKey 1 does not equals csHoldKey1
	}

	/**
	 * Process Last Record
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @param aiHoldFeeSourceCode int
	 * @throws RTSExceptin
	 */
	public void processLastRecord(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4,
		int aiHoldFeeSourceCode)
		throws RTSException
	{
		// print the last detail record
		if (!(caTotalForPayDescCust.equals(NEW_DOLLAR))
			|| !(caTotalForPayDescSubCon.equals(NEW_DOLLAR))
			|| !(caTotalForPayDescDealer.equals(NEW_DOLLAR))
			|| !(caTotalForPayDescInternet.equals(NEW_DOLLAR)))
		{
			appendDescriptionToPrintVector(
				csHoldDescription,
				DATALINE_STARTPT,
				DATALINE_LENGTH);
			appendItemAmountAndQty();
		}
		setAmountsForStoringObjects();
		// process the at break logic for each of the breaks.  need to
		// do this so totals print out.
		// start with 3 because that is the at break of cashDrawerIndi,
		// then 2 is by Employee,
		// and 1 is by cashDrawer
		processAtBreak(
			asCurrKey1,
			asCurrKey2,
			asCurrKey3,
			asCurrKey4,
			BREAK_KEY_3,
			aiHoldFeeSourceCode);
		processAtBreak(
			asCurrKey1,
			asCurrKey2,
			asCurrKey3,
			asCurrKey4,
			BREAK_KEY_1,
			aiHoldFeeSourceCode);
	}

	/**
	 * work through the Summary Vector.
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
		resetKey1Accumulators();
		resetKey2Accumulators();
		resetCashDrawerIndiTotals();
		resetPayDescAccumulators();

		ciItemCount = 0;

		// sort the summary data and set the keys,
		sortSummaryVector(avSummaryData);

		// get the first record
		PaymentTypeReportData laPmys =
			(PaymentTypeReportData) avSummaryData.elementAt(0);

		// load the initial hold variable
		csHoldKey1 = laPmys.getKey1(); // cash drawer/ non cash drawer
		csHoldKey2 = laPmys.getKey2(); // payment description code

		// get the text for the next type of Cash Drawer Indi
		setTextForCashDrawerIndi(Integer.parseInt(laPmys.getKey1()));

		// defect 3487
		// insert a blank line before the summary text
		appendBlankLine();
		// end defect 3487

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
						appendDetailLine();
						// reset the items used for accumulating totals
						resetPayDescAccumulators();
						// reset and load the hold key with the current
						// value that is read in
						csHoldKey2 = lsCurrKey2;
					} // end of current lsPymnttypeCdDesc.equals 
					//csHoldPymntTypeCdDesc
				} // end of type of cash drawer indicators matching
				else
				{ // cash drawer indi's do not match
					csHoldDescription = csHoldKey2;
					appendDetailLine();

					// print the totals for the cash drawer type
					appendTotalsForCashDrawerIndi(
						caTotalForCashDrawerIndiCust,
						caTotalForCashDrawerIndiSubCon,
						caTotalForCashDrawerIndiDealer,
						caTotalForCashDrawerIndiInternet,
						caTotalForCashDrawerIndi);

					printTransaction();

					// reset the amount fields
					resetPayDescAccumulators();
					resetCashDrawerIndiTotals();

					// get the text for the next type of Cash Drawer Ind
					setTextForCashDrawerIndi(
						Integer.parseInt(lsCurrKey1));

					// defect 3487
					// print a blank line before the summary text
					appendBlankLine();
					// end defect 3487

					// print the header for the next type of cash drawer
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
				accumulateDollarAmounts(
					laPmys.getPymntTypeAmt(),
					laPmys.getPymntTypeQty(),
					laPmys.getFeeSourceCd());
				i++;
			} // end while
		} // end if

		// reset the value of the hold key with the concatenation string
		// of all WsId's for the header page

		csHoldDescription = csHoldKey2;

		// append the description and quantity for the last item
		appendDetailLine();

		if (cbSummaryPageNeeded)
		{

			// print to totals for the type of transaction.  Cash Drawer
			// vs. non-cash drawer
			appendTotalsForCashDrawerIndi(
				caTotalForCashDrawerIndiCust,
				caTotalForCashDrawerIndiSubCon,
				caTotalForCashDrawerIndiDealer,
				caTotalForCashDrawerIndiInternet,
				caTotalForCashDrawerIndi);

			appendBlankLine();
			appendDoubleDashes();

			buildPrintVector(
				csTotalFor,
				CASH_DRAWR_INDI_STARTPT,
				CASH_DRAWER_INDI_LENGTH,
				LEFT);

			appendTotals(
				caTotalForKey1Cust,
				caTotalForKey1SubCon,
				caTotalForKey1Dealer,
				caTotalForKey1Internet,
				caTotalForKey1);

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
		Vector lvdataLine = new Vector();

		Dollar laPymntTypeAmtDollar = new Dollar("100.00");

		PaymentTypeReportData laPaymentTypeReportData =
			new PaymentTypeReportData();
		PaymentTypeReportData laPTRD_A = new PaymentTypeReportData();
		PaymentTypeReportData laPTRD_B = new PaymentTypeReportData();

		laPTRD_A.setCashDrawerIndi(1); // cash drawer
		laPTRD_A.setCashWsId(100);
		laPTRD_A.setFeeSourceCd(2);
		laPTRD_A.setPymntTypeAmt(laPymntTypeAmtDollar);
		laPTRD_A.setPymntTypeCdDesc("CHECK");
		laPTRD_A.setPymntTypeQty(55);
		laPTRD_A.setTransEmpId("128");

		lvdataLine.addElement(laPTRD_A);

		//	b.setCashDrawerIndi(1);  
		laPTRD_B.setCashDrawerIndi(0); //non-cash drawer
		laPTRD_B.setCashWsId(100);
		laPTRD_B.setFeeSourceCd(1);
		laPTRD_B.setPymntTypeAmt(laPymntTypeAmtDollar);
		laPTRD_B.setPymntTypeCdDesc("XCHECK");
		//	b.setPymntTypeCdDesc("SOMETHING");
		laPTRD_B.setPymntTypeQty(55);
		laPTRD_B.setTransEmpId("128");

		lvdataLine.addElement(laPTRD_B);

		for (int i = 0; i < 10; i++)
		{
			laPaymentTypeReportData = new PaymentTypeReportData();
			laPaymentTypeReportData.setCashDrawerIndi(0);
			laPaymentTypeReportData.setCashWsId(200);
			laPaymentTypeReportData.setFeeSourceCd(5);
			laPaymentTypeReportData.setPymntTypeAmt(
				laPymntTypeAmtDollar);
			laPaymentTypeReportData.setPymntTypeQty(55);
			laPaymentTypeReportData.setTransEmpId("212");
			laPaymentTypeReportData.setPymntTypeCdDesc("CHECK");
			lvdataLine.addElement(laPaymentTypeReportData);
		}
		for (int i = 0; i < 10; i++)
		{
			laPaymentTypeReportData = new PaymentTypeReportData();
			laPaymentTypeReportData.setCashDrawerIndi(0);
			laPaymentTypeReportData.setCashWsId(100);
			laPaymentTypeReportData.setFeeSourceCd(5);
			laPaymentTypeReportData.setPymntTypeAmt(
				laPymntTypeAmtDollar);
			laPaymentTypeReportData.setPymntTypeQty(55);
			laPaymentTypeReportData.setTransEmpId("212");
			laPaymentTypeReportData.setPymntTypeCdDesc("CHECK");
			lvdataLine.addElement(laPaymentTypeReportData);
		}
		for (int i = 0; i < 5; i++)
		{
			laPaymentTypeReportData = new PaymentTypeReportData();
			laPaymentTypeReportData.setCashDrawerIndi(0);
			laPaymentTypeReportData.setCashWsId(100);
			laPaymentTypeReportData.setFeeSourceCd(3);
			laPaymentTypeReportData.setPymntTypeAmt(
				laPymntTypeAmtDollar);
			laPaymentTypeReportData.setPymntTypeQty(55);
			laPaymentTypeReportData.setTransEmpId("130");
			laPaymentTypeReportData.setPymntTypeCdDesc("CHECK");
			lvdataLine.addElement(laPaymentTypeReportData);
		}
		/*
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
			
			a.setCashDrawerIndi(1);
			a.setCashWsId(120);
			a.setFeeSourceCd(1);
			a.setPymntTypeAmt(lPymntTypeAmtDollar);
			a.setPymntTypeCdDesc("SOMETHING");
			a.setPymntTypeQty(55);
			a.setTransEmpId("128");
			lvdataLine.addElement(a);
		
			b.setCashDrawerIndi(1);
			b.setCashDrawerIndi(0);
			b.setCashWsId(100);
			b.setFeeSourceCd(1);
			b.setPymntTypeAmt(lPymntTypeAmtDollar);
			b.setPymntTypeCdDesc("CHECK");
			b.setPymntTypeCdDesc("SOMETHING");
			b.setPymntTypeQty(55);
			b.setTransEmpId("128");
			lvdataLine.addElement(b);
			
			c.setCashDrawerIndi(1);
			c.setCashWsId(100);
			c.setFeeSourceCd(1);
			c.setPymntTypeAmt(lPymntTypeAmtDollar);
			c.setPymntTypeCdDesc("SOMETHING");
			c.setPymntTypeQty(55);
			c.setTransEmpId("129");
			lvdataLine.addElement(c);
		
			b.setCashDrawerIndi(1);
			d.setCashDrawerIndi(0);
			d.setCashWsId(100);
			d.setFeeSourceCd(1);
			d.setPymntTypeAmt(lPymntTypeAmtDollar);
			d.setPymntTypeCdDesc("CHECK");
			d.setPymntTypeCdDesc("SOMETHING");
			d.setPymntTypeQty(55);
			d.setTransEmpId("129");
			lvdataLine.addElement(d);
		
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
		return lvdataLine;
	}

	/**
	 * Reset Cash Drawer Indi Totals
	 * 
	 * @throws RTSException
	 */
	public void resetCashDrawerIndiTotals() throws RTSException
	{
		caTotalForCashDrawerIndi = NEW_DOLLAR;
		caTotalForCashDrawerIndiCust = NEW_DOLLAR;
		caTotalForCashDrawerIndiSubCon = NEW_DOLLAR;
		caTotalForCashDrawerIndiDealer = NEW_DOLLAR;
		caTotalForCashDrawerIndiInternet = NEW_DOLLAR;
	}

	/**
	 * Reset Key 1 Accumulators
	 * 
	 * @throws RTSException
	 */
	public void resetKey1Accumulators() throws RTSException
	{
		caTotalForKey1 = NEW_DOLLAR;
		caTotalForKey1Cust = NEW_DOLLAR;
		caTotalForKey1Dealer = NEW_DOLLAR;
		caTotalForKey1SubCon = NEW_DOLLAR;
		caTotalForKey1Internet = NEW_DOLLAR;
	}

	/**
	 * Reset Key 2 Accumulators
	 * 
	 * @throws RTSException
	 */
	public void resetKey2Accumulators() throws RTSException
	{
		caTotalForKey2 = NEW_DOLLAR;
		caTotalForKey2Cust = NEW_DOLLAR;
		caTotalForKey2Dealer = NEW_DOLLAR;
		caTotalForKey2SubCon = NEW_DOLLAR;
		caTotalForKey2Internet = NEW_DOLLAR;
	}

	/**
	 * Reset Pay Desc Accumulators
	 * 
	 * @throws RTSException
	 */
	public void resetPayDescAccumulators() throws RTSException
	{
		caTotalForAllPayDesc = NEW_DOLLAR;
		caTotalForPayDescCust = NEW_DOLLAR;
		caTotalForPayDescDealer = NEW_DOLLAR;
		caTotalForPayDescSubCon = NEW_DOLLAR;
		caTotalForPayDescInternet = NEW_DOLLAR;

		ciItemCount = 0;
		ciItemCountCust = 0;
		ciItemCountDealer = 0;
		ciItemCountSubCon = 0;
		ciItemCountInternet = 0;
	}

	/**
	 * setAmountsForStoringObjects
	 * 
	 * @throws RTSException
	 */
	public void setAmountsForStoringObjects() throws RTSException
	{
		Dollar laDollarZero = NEW_DOLLAR;

		if ((ciItemCountCust != 0)
			|| (!(caTotalForPayDescCust.equals(laDollarZero))))
			// has a balance
		{
			storeObjectsToAccumulateTotals(
				caTotalForPayDescCust,
				ciItemCountCust,
				FEE_SOURCE_CUST);
		}
		if ((ciItemCountSubCon != 0)
			|| (!(caTotalForPayDescSubCon.equals(laDollarZero))))
			// has a balance
		{
			storeObjectsToAccumulateTotals(
				caTotalForPayDescSubCon,
				ciItemCountSubCon,
				FEE_SOURCE_SUBCON);
		}
		if ((ciItemCountDealer != 0)
			|| (!(caTotalForPayDescDealer.equals(laDollarZero))))
			// has a balance
		{
			storeObjectsToAccumulateTotals(
				caTotalForPayDescDealer,
				ciItemCountDealer,
				FEE_SOURCE_DEALER);
		}
		if ((ciItemCountInternet != 0)
			|| (!(caTotalForPayDescInternet.equals(laDollarZero))))
			// has a balance
		{
			storeObjectsToAccumulateTotals(
				caTotalForPayDescInternet,
				ciItemCountInternet,
				FEE_SOURCE_INTERNET);
		}
	}

	/**
	 * Set the hold keys
	 * 
	 * @param asCurrKey1 String
	 * @param asCurrKey2 String
	 * @param asCurrKey3 String
	 * @param asCurrKey4 String
	 * @param aiHoldFeeSourceCode int
	 * @throws RTSException
	 */
	public void setHoldKeys(
		String asCurrKey1,
		String asCurrKey2,
		String asCurrKey3,
		String asCurrKey4,
		int aiHoldFeeSourceCode)
		throws RTSException
	{
		csHoldKey1 = asCurrKey1;
		csHoldKey2 = asCurrKey2;
		csHoldKey3 = asCurrKey3;
		csHoldKey4 = asCurrKey4;
		ciHoldFeeSourceCode = aiHoldFeeSourceCode;

		csHeaderIdValue = csHoldKey1;
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
	//
	//		if (lsReportTitle.equals(FundsConstant.PAYMENT_REPORT))
	//		{
	//			csReportName = FundsConstant.PAYMENT_REPORT;
	//		}
	//	}

	/**
	 * Set Report Num
	 * 
	 * @param aiReportType int
	 * @throws RTSException
	 */
	public void setReportNum(int aiReportType) throws RTSException
	{
		switch (aiReportType)
		{
			case FundsConstant.DATE_RANGE : // 5211
				{
					// empty code block
				}
			case FundsConstant.LAST_CLOSE : //  Last close out
				{
					// empty code block
				}
			case FundsConstant.AFTER_SUBSTATION :
				{
					// empty code block
				}
			case FundsConstant.SINCE_CLOSE :
				{
					// empty code block
				}
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
			case 7 : // 5921
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
	 * Set Sort Keys For By Cash Drawer
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
				case PaymentReportCloseOutShowSource.CASH_DRAWER_NONE :
					// by cash drawer and payment type code description,
					// no data is needed
					{
						laPaymentTypeReportData.setKey2(" ");
						break;
					}
				case PaymentReportCloseOutShowSource.CASH_DRAWER_EMP :
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
			// key 4 value will always be the Payment type code description
			laPaymentTypeReportData.setKey4(
				laPaymentTypeReportData.getPymntTypeCdDesc());

			// update the query with the keys we just set
			avQueryResults.setElementAt(laPaymentTypeReportData, i);
		}
	}
	/**
	 * Set Sort Keys For By Employee
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
			//  If the user select by employee, load that data else
			// leave blank
			switch (ciBreakByCode)
			{
				case EMP_NONE : // by Employee id and payment type code
					// description, no data is needed
					{
						laPaymentTypeReportData.setKey2(" ");
						break;
					}
				case EMP_CASH_DRAWER : // by  employee id, cash Drawer,
					// and pay desc code
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
			// key 4 value will always be the Payment type code description
			laPaymentTypeReportData.setKey4(
				laPaymentTypeReportData.getPymntTypeCdDesc());

			// update the query with the keys we just set
			avQueryResults.setElementAt(laPaymentTypeReportData, i);
		}
	}

	/**
	 * Set Sub Header Id AbstractValue
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
			case PaymentReportCloseOutShowSource.CASH_DRAWER_EMP :
				// by Workstation, employee, and pay desc code		
				{
					// empty code block	
				}
			case PaymentReportCloseOutShowSource.CASH_DRAWER_NONE :
				{
					// by workstation and pay desc code
					if (ciBreakByCode == CASH_DRAWER_EMP)
					{
						csSubHeaderIdValue = csHoldKey2;
					}
					else
					{
						csSubHeaderIdValue = csHoldKey1;
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
								//.getClockTime().substring(0,8);

								csEndDate =
									laDrawer
										.getCloseOutEndTstmp()
										.toString()
										+ " "
										+ laDrawer
											.getCloseOutEndTstmp()
											.getClockTimeNoMs();
											//.getClockTime().substring(0,8);
								// end defect 9943 
								break;
							}
						}
					}
					break;
				}
			case PaymentReportCloseOutShowSource.EMP_NONE :
				// by Employee and pay desc
				{
					csSubHeaderIdValue = csHoldKey1;
					break;
				}
			case PaymentReportCloseOutShowSource.EMP_CASH_DRAWER :
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
				if (Integer.parseInt(csHoldKey1) == liCashDrwr)
				{
					if (caFundsData.getFundsReportData().getRange()
						!= FundsConstant.CLOSE_OUT_FOR_DAY)
					{
						// defect 9943 
						laCashDrwrObj.setRptStatus(FundsConstant.REPORT_GENERATED);						
						//laCashDrwrObj.setRptStatus("Report Generated");
						// end defect 9943 
					}
					laCashDrwrObj.setBsnDateTotalAmt(caTotalForKey1);
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
				//else lEmployeeObj.setRptStatus("");
			}
		}
	}

	/**
	 * Set Summary For Text
	 * 
	 * @param asId String
	 * @throws RTSException
	 */
	public void setSummaryForText(String asId) throws RTSException
	{
		switch (ciBreakByCode)
		{
			case PaymentReportCloseOutShowSource.CASH_DRAWER_NONE :
				{
					// empty code block
				}
			case PaymentReportCloseOutShowSource.EMP_CASH_DRAWER :
				{
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_CASH_DRAWER;
					csTotalFor = ReportConstant.TOTAL_FOR_CASH_DRAWER;
					break;
				}
			case PaymentReportCloseOutShowSource.CASH_DRAWER_EMP :
				{
					// empty code block
				}
			case PaymentReportCloseOutShowSource.EMP_NONE :
				{
					csSummaryFor = ReportConstant.SUMMARY_FOR_EMPLOYEE;
					csTotalFor = ReportConstant.TOTAL_FOR_EMPLOYEE;
					break;
				}
		}
		csSummaryFor += (asId + ":");
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
			case CASH_DRAWER_INDI_FOR_SORT : // cash drawer, cash 
				{
					csCashDrawerIndiText =
						ReportConstant.CASH_DRAWER_OPER;
					csCashDrawerIndiTextTotals =
						ReportConstant.TOTAL_CASH_DRAWER_OPER;
					break;
				}
			case NON_CASH_DRAWER_INDI_FOR_SORT :
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
	 * Sort Summary Vector
	 * 
	 * @param avSummaryData Vector
	 */
	public void sortSummaryVector(Vector avSummaryData)
	{
		// set the key values
		for (int i = 0; i < avSummaryData.size(); i++)
		{
			PaymentTypeReportData laPmys =
				(PaymentTypeReportData) avSummaryData.elementAt(i);
			laPmys.setKey1(String.valueOf(laPmys.getCashDrawerIndi()));
			// when we store summary data we set this to the sort order		
			laPmys.setKey2(laPmys.getPymntTypeCdDesc());
			laPmys.setKey3(String.valueOf(laPmys.getFeeSourceCd()));
			laPmys.setKey4(" ");
			avSummaryData.setElementAt(laPmys, i);
		}
		// Sort the results
		UtilityMethods.sort(avSummaryData);
	}

	/**
	 * Store Objects To Accumulate Totals
	 *  
	 * @param aaTotal Dollar
	 * @param aiItemCount int
	 * @param aiCurrFeeSourceCd int
	 * @throws RTSException
	 */
	public final void storeObjectsToAccumulateTotals(
		Dollar aaTotal,
		int aiItemCount,
		int aiCurrFeeSourceCd)
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
			laPaymentTypeReportData.setFeeSourceCd(aiCurrFeeSourceCd);
			laPaymentTypeReportData.setPymntTypeAmt(aaTotal);
			laPaymentTypeReportData.setPymntTypeQty(aiItemCount);
			laPaymentTypeReportData.setCashDrawerIndi(
				(Integer.parseInt(csHoldKey3)));
			cvSummaryData.addElement(laPaymentTypeReportData);
			// add an element for summary page

			switch (ciBreakByCode)
			{
				case CASH_DRAWER_EMP :
					{
						// empty code block
					}
				case EMP_CASH_DRAWER :
					{
						cvSummaryDataForEntity.addElement(
							laPaymentTypeReportData);
						return;
					}
			}
		}
	}
}
