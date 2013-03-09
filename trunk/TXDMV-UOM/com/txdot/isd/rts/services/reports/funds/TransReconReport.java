package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.data.EmployeeData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.ReportStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * TransReconReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Listberger	05/16/2002 	removed unused import statements.
 *                          corrected right alignment of payment type.
 * M Listberger	05/20/2002 	added logic to prevent the printing of
 *                          cash 0.00 twice when a payment record is not
 *                          found and it is the first transaction
 *                          (e.g. internet renewal).
 * M Listberger	06/06/2002 	added logic to process void non-cash 
 *							transactions (VOIDNC).
 *                          This correct the formatting problems
 * 							encountered	when the Internet Renewal void
 * 							transaction was added.
 * M Listberger	07/01/2002 	Added code to include leading zero for
 * 							office issuance number in the transaction id
 * 							number.
 * 							appendCustomerSetTransactionsToPrintVector 
 *							method.
 *                          defect 4389.
 * M Listberger	08/01/2002 	Added code to incorporate IRENEW and INET
 * 							CHARGE. Defect 4541. Added break statement
 * 							in processTransReconInventoryDataObject to 
 * 							escape after finding a match with invy and
 * 							fees. Defect 4549.  Added logic for Void
 * 							Transaction Code so "INET CHARGE" will show
 * 							up for a void transaction that is associated
 * 							with an Internet transaction.
 * M Listberger	08/21/2002 	Added if logic in process inventory method
 * 							to allow the printing of all void inventory
 * 							items.
 *                          Defect 4618.
 * B. Brown     10/01/2002 	For defect #4737 changed the lsCoreKey to a 
 *							three digit format for trnsWsID, and changed 
 *							lsKey values to use CashWsId (instead of 
 *							transWsID) with a three digit format, so
 *							these keys get built with the same number of
 *							characters.  This was particularly important
 *							for places like Ozona, which share a cash
 *							drawer between two workstations. The methods
 *							changed were:
 *							setUpCurrFeesSearchKey,
 *							setUpCurrInvySearchKey,
 *							setUpCurrPaySearchKey,
 *							setUpCurrTransSearchKeys.
 * S Govindappa	02/19/2002	Fix defect 4955. Made changes to 
 *							processTransReconInventoryDataObject(..) to
 *                          print all the inventory items for regional 
 *							collections without fees.
 * K Harrell	05/27/2004	Removed ":" after "REPORT TYPE" for
 *						 	constant REPORT_TYPE
 *							defect 7097  Ver 5.2.0
 * K Harrell	11/16/2004	General Formatting
 *							Pad employee id to 7 characters
 *							add padEmpId()
 *							modify processTransReconTransactionData(),
 *							setUpCurrFeesSearchKey(),
 *							setUpCurrInvySearchKey(),
 *							setUpCurrPaySearchKey,
 *							setUpCurrTransSearchKeys()
 *							formatted all methods. 
 *							defect 7417  Ver 5.2.2 
 * S Johnston	06/15/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	10/24/2005	Constant Renaming
 * 							defect 8379 Ver 5.2.3
 * K Harrell	08/22/2008	If last transaction of multiple trans set 
 * 							is VOID/VOIDNC && INVVD, does not print 
 * 							"CUSTOMER SUBTOTAL:"  
 * 							modify processTransReconTransactionData()
 * 							defect 9049 Ver Defect_POS_B
 * K Harrell	08/22/2008	Prints ### for overflow for Payment although
 * 							Fees handles.
 * 							delete FEES_AMT_RPT_LABEL_LENGTH,
 * 							 FEES_AMT_RPT_LABEL_STARTPT,
 * 							 FEES_AMT_RPT_$_STARTPT,
 * 							 FEES_AMT_RPT_TOTAL_STARTPT 
 * 							modify $_FEES_STARTPT,FEES_ITEM_PRICE_LENGTH
 *  						modify appendFeesTotalsToPrintVector(), 
 * 							 appendPaymentAmountToPrintVector(),
 * 							 appendPrimaryTotalsToPrintVector(),
 * 							 appendReportTotalsToPrintVector()
 * 							defect 9091 Ver Defect_POS_B 
 * K Harrell	09/24/2008	Add comment for removed variable
 * 							modify appendReportTotalsToPrintVector()
 * 							defect 9091 Ver Defect_POS_B
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs(),
 * 							modify createMainHeadingVectors()
 * 							defect 9943 Ver Defect_POS_F
 * K Harrell	07/26/2009	HB3095:  Implement new AcctItmCds for 
 * 							Temp/Perm Disabled Placard. Modify so that
 * 							doesn't combine lines for TDC/PDC. 
 * 							modify processTransReconFeesDataObject()
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F   
 * --------------------------------------------------------------------- 
 */
/**
 * Method to create the Fund Transaction Reconciliation Report.
 *
 * @version	Defect_POS_F	08/10/2009
 * @author	Administrator
 * <br>Creation Date:		09/17/2001 13:18:36 
 */
public class TransReconReport extends ReportTemplate
{
	// used to find out if we are processing just and inventory item or 
	// a fees item that needs to check for an invy item
	private int ciInvyKeyLength = 0;
	private int ciTypeOfBreak = 0;
	// field for detail lines that are available
	private int ciDetailLines = 0;
	// indexes to track where we need to keep reading from
	private int ciPaymentIndex = 0;
	private int ciFeesIndex = 0;
	private int ciInventoryIndex = 0;
	private int ciPrintLineIndex = 0;
	private int ciHoldPrintLineIndex = 0;
	private int ciHoldFeeInvyPrintLineIndex = 0;
	private int ciHoldInvyPrintLineIndex = 0;
	private int ciTypeOfPrintVector = 0;

	private final int ADD_TO_PV_IMMED = 0;
	private final int FEES_WITH_INVY = 1;
	private final int INVY_NO_FEES = 2;
	private final int LINE_INDI_GENERAL = 0;
	private final int LINE_INDI_PAYMENT = 1;
	private final String IRENEW = "IRENEW";
	private final String INET_CHRG = "INET CHARGE";
	private boolean REPORT_TOTAL_LINE = true;
	private boolean cbChangeDuePrinted;
	private boolean cbPayFeesInvyDetail = false;
	private boolean cbHoldPrintVector = false;
	// set up switch to indicate if we need to draw a single dashed line
	private boolean cbSingleDashedLineNeeded;
	private boolean cbPaymentRecordFound = false;
	private boolean cbFeesOrInvyRecordFound = false;
	private Vector cvHeader = new Vector();
	private Vector cvTable = new Vector();
	private Vector cvPrintVector = new Vector();
	private Vector cvHoldFeesWithInvy = new Vector();
	private Vector cvHoldInvy = new Vector();
	private String csTypeOfChangeDue;
	private String csSummaryFor = null;
	private String csTotalFor = null;
	private String csHoldSummaryId = null;
	private String csHoldInvyKey = "0";
	private String csHoldTransCd = "";
	private final int AM_DATE = 2;
	private final int LABEL_CHANGE_LENGTH = 13;
	// Sort constant
	// Combination of entity and primary split
	public final int CASH_DRAWER_NONE = 0;
	public final int CASH_DRAWER_EMP = 1;
	public final int EMP_NONE = 2;
	public final int EMP_CASH_DRAWER = 3;
	// number of lines needed to print out the totals of the report,
	// need to take care of widow/orphan
	// Initialize LEFT, CENTER, and RIGHT variables
	private final int LEFT = 0;
	private final int CENTER = 1;
	private final int RIGHT = 2;
	private final int REPORT_FOOTER_LINES = 2;
	private final String INVVD = "INVVD";
	//  transaction code there is an inentory item to be voided.
	private final String VOID = "VOID";
	private final String VOIDNC = "VOIDNC"; // void non-cash
	private final String EMPLOYEE = "EMPLOYEE";
	private final String CASH_DRAWER = "CASH DRAWER";
	private final String WORKSTATION = "WORKSTATION";
	private final String BY = "BY";
	private final String PAYMENT = "PAYMENT";
	private final String REPORT = "REPORT";
	private final String CHANGE = "CHANGE";
	private final String CASH_CHANGE = "CASH CHANGE";
	private final String CHECK_CHANGE = "CHECK CHANGE";
	private final String FEES = "FEES";
	private final String CUSTOMER_SUBTOTAL = "CUSTOMER SUBTOTAL:";
	private final Dollar NEW_DOLLAR = new Dollar("0.00");
	// set up dollar fields to accumulate customer subtotal per
	// transaction used when a customer set has multiple transaction
	// within it, for example "Permanent Disabled Placard Receipt",
	// "Registration Renewal", and "Duplicate Receipt".  Each of the
	// above items would have a subtotal
	// set up dollar fields to accumulate customer set totals.  Totals 
	// of all transactions
	// customer subtotals
	private Dollar caTotalFeesForCustomerSubtotal = NEW_DOLLAR;
	// witing a customer set
	private Dollar caTotalPaymentForCustomerSet = NEW_DOLLAR;
	private Dollar caTotalCashChangeForCustomerSet = NEW_DOLLAR;
	private Dollar caTotalCheckChangeForCustomerSet = NEW_DOLLAR;
	private Dollar caTotalFeesForCustomerSet = NEW_DOLLAR;
	// set up dollar fields to accumulate totals based on a primary 
	// split
	private Dollar caTotalPaymentForPrimarySplit = NEW_DOLLAR;
	private Dollar caTotalCashChangeForPrimarySplit = NEW_DOLLAR;
	private Dollar caTotalCheckChangeForPrimarySplit = NEW_DOLLAR;
	private Dollar caTotalFeesForPrimarySplit = NEW_DOLLAR;
	// set up dollar fields to accumulate report totals
	private Dollar caTotalPaymentForReport = NEW_DOLLAR;
	private Dollar caTotalCashChangeForReport = NEW_DOLLAR;
	private Dollar caTotalCheckChangeForReport = NEW_DOLLAR;
	private Dollar caTotalFeesForReport = NEW_DOLLAR;
	// main heading
	private final String TRANS_FROM = "TRANSACTIONS FROM";
	// first column
	private final String HEADER_CUST_NAME =
		"CUSTOMER NAME - TRANSACTION";
	private final String HEADER_TRANS_ID = "TRANSACTION ID";
	private final int COL_CUST_SET_STARTPT = 1;
	private final int COL_DESC_TRANS_ID_STARTPT = 3;
	private final int COL_CUST_TRANS_ID_HDR_LENGTH = 28;
	private final int COL_CUST_SET_LENGTH = 30;
	private final int COL_CUST_NAME_LENGTH = 125;
	private final int COL_TRANS_DETAIL_LINE_LENGTH = 66;
	private final int CUST_SET_PAY_STARTPT_INDENT = 20;
	private final int CUST_SET_PAY_LENGTH_INDENT = 25;
	private final int CUST_SET_FEES_STARTPT_INDENT = 93;
	// second column
	private final String HEADER_PAYMENT = "PAYMENT";
	private final String HEADER_TYPE = "TYPE";
	private final int COL_PAY_TYPE_STARTPT = 32;
	private final int COL_PAY_TYPE_LENGTH = 13;
	// set point for dollar sign
	private final String DOLLAR_SIGN = "$";
	private final int DOLLAR_SIGN_LENGTH = 1;
	private final int $_PAYMENT_STARTPT = 46;
	// third column
	private final int COL_PAY_AMOUNT_STARTPT = 47;
	private final int COL_PAY_AMOUNT_LENGTH = 11;
	// fourth column
	private final int $_CHANGE_STARTPT = 60;
	private final int COL_CASH_START_STARTPT = 61;
	private final int COL_CASH_LENGTH = 11;
	private final String HEADER_CASH = "CASH";
	private final String HEADER_CHANGE_SLASH = "CHANGE/";
	private final String HEADER_CHECK = "CHECK";
	private final String HEADER_CHANGE_ASTERISK = "CHANGE*";
	// this is used when printing out check change items.  Can't 
	//concatenate and right justify and same time
	// and still have the decimals line up.
	private final String ASTERISK = "*";
	private final int COL_ASTERISK_STARTPT = 72;
	private final int COL_ASTERISK_LENGTH = 1;
	// fifth column
	private final String HEADER_FEE_ITEM = "FEE/ITEM";
	private final String HEADER_FEES_DESCRIPTION = "DESCRIPTION";
	private final int COL_FEES_DESC_STARTPT = 74;
	private final int COL_FEES_DESC_LENGTH = 28;
	// columns 6 - 8; first line
	private final String HEADER_ITEM = "ITEM";
	// column 6
	private final String HEADER_YEAR = "YEAR";
	private final int FEES_ITEM_YEAR_STARTPT = 102;
	private final int FEES_ITEM_YEAR_LENGTH = 4;
	// column 7
	private final String HEADER_NUMBER = "NUMBER";
	private final int FEES_ITEM_NUM_STARTPT = 107;
	private final int FEES_ITEM_NUM_LENGTH = 11;
	// dollar sign for fees
	private final int $_FEES_STARTPT = 120;
	// column 8
	private final String HEADER_PRICE = "PRICE";
	private final int FEES_ITEM_PRICE_STARTPT = 120;
	private final int FEES_ITEM_PRICE_LENGTH = 11;
	private final int FEES_AMT_RPT_TOTAL_DASHES_STARTPT = 118;
	private final int FEES_AMT_RPT_TOTAL_LENGTH = 14;
	// set up constant for the text "Customer Set:"  This text will be
	// concatenated with the actual customer set number.
	// The may be additional concatenation with LABEL_TOTAL
	private final String CUST_SET = "CUSTOMER SET";
	// set up constand for the text "TOTAL: "  This is used in various
	// places on the report
	private final String TOTAL = "TOTAL";
	private final String THROUGH = " THROUGH ";
	private final String REPORT_TYPE = "REPORT TYPE";
	private String CLOSE_AFTER_SUBSTA_SUM =
		"CLOSEOUTS AFTER SUBSTATION SUMMARY";
	private String LAST_CURRENT_STATUS = "SINCE LAST CURRENT STATUS";
	private String DATE_RANGE = "DATE RANGE";
	private String LAST_CLOSEOUT = "SINCE LAST CLOSEOUT";
	private String FOR_CLOSE = "FOR LAST CLOSEOUT";
	// defect 8379 
	private String CLOSEOUT = "CLOSEOUT";
	// end defect 8379
	// set up the dashed lines used to separate totals	
	private final String SINGLE_DASHES_FEES =
		this.caRpt.singleDashes(FEES_ITEM_PRICE_LENGTH);
	private final String DOUBLE_DASHES_PAYMENT =
		this.caRpt.doubleDashes(COL_PAY_AMOUNT_LENGTH);
	public ReportStatusData cReportStatusData = new ReportStatusData();
	public Vector cvTrrStatVec = new Vector();

	/**
	 * TransReconReport constructor
	 */
	public TransReconReport()
	{
		super();
	}

	/**
	 * TransReconReport constructor
	 *
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public TransReconReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * appendChangeToPrintVector
	 *
	 * @param aaDollar Dollar
	 * @param asChangeType String
	 * @param abTotalLine boolean
	 * @param asTypeOfTotal String
	 * @throws RTSException
	 */
	public void appendChangeToPrintVector(
		Dollar aaDollar,
		String asChangeType,
		boolean abTotalLine,
		String asTypeOfTotal)
		throws RTSException
	{
		String lsAmount = "";
		// print the dollar sign
		buildPrintVector(
			DOLLAR_SIGN,
			$_CHANGE_STARTPT,
			DOLLAR_SIGN_LENGTH,
			LEFT,
			LINE_INDI_PAYMENT);
		// check to see if change amount is longer than the space that
		// is allocated
		if ((aaDollar.printDollar()).substring(1).length()
			> COL_CASH_LENGTH)
		{
			lsAmount = getAmountOverflowSymbol(COL_CASH_LENGTH);
		}
		else
		{
			lsAmount = (aaDollar.printDollar()).substring(1);
		}
		// print the amount
		buildPrintVector(
			lsAmount,
			COL_CASH_START_STARTPT,
			COL_CASH_LENGTH,
			RIGHT,
			LINE_INDI_PAYMENT);
		// if it is check change, print out the asterisk, not this
		// cannot be concatenated to the dollar amount because
		// with right justification, the asterisk will line up under the
		// last 0.  In other word, decimals won't line up
		if (asChangeType.equals(CHECK_CHANGE))
		{
			buildPrintVector(
				ASTERISK,
				COL_ASTERISK_STARTPT,
				COL_ASTERISK_LENGTH,
				LEFT,
				LINE_INDI_PAYMENT);
		}
		// print the type of change if we are print out a total line.
		// Detail lines do not have the change type printed out
		if (abTotalLine)
		{
			buildPrintVector(
				asChangeType,
				COL_FEES_DESC_STARTPT,
				LABEL_CHANGE_LENGTH,
				LEFT,
				LINE_INDI_PAYMENT);
		}
		// check to see if we are are printing out the report totals,
		// if we are the  we need to see if we need to go to the next
		// line
		if (asTypeOfTotal.equals(REPORT))
		{
			// we are printing Cash Change and there is a value for
			// check change
			if ((asChangeType.equals(CASH_CHANGE))
				&& (!caTotalCheckChangeForReport.equals(NEW_DOLLAR)))
			{
				ciPrintLineIndex++;
			}
		}
	}

	/**
	 * appendCustomerSetHeadingToPrintVector
	 * 
	 * @param asCustSeqNo String
	 * @param aaRTSDate RTSDate
	 * @throws RTSException
	 */
	public void appendCustomerSetHeadingToPrintVector(
		String asCustSeqNo,
		RTSDate aaRTSDate)
		throws RTSException
	{
		String lsCustomerSet = " ";
		lsCustomerSet =
			CUST_SET + ": " + asCustSeqNo + " (" + aaRTSDate + ")";
		//
		buildPrintVector(
			lsCustomerSet,
			COL_CUST_SET_STARTPT,
			COL_CUST_SET_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
	}

	/**
	 * This method prints out the totals for each customer set.
	 * It includes payments, change due, and item price
	 *
	 * @param asHoldSeqNo String
	 * @throws RTSException
	 */
	public void appendCustomerSetTotalsToPrintVector(String asHoldSeqNo)
		throws RTSException
	{
		String lsLabel =
			CUST_SET + " " + asHoldSeqNo + " " + TOTAL + ":";
		appendSingleDashesToPrintVector();
		appendPaymentAmountToPrintVector(
			lsLabel,
			caTotalPaymentForCustomerSet);
		// if we have cash change print out the value
		if (!caTotalCashChangeForCustomerSet.equals(NEW_DOLLAR))
		{
			appendChangeToPrintVector(
				caTotalCashChangeForCustomerSet,
				CASH_CHANGE,
				REPORT_TOTAL_LINE,
				CUST_SET);
		}
		else
		{
			// if we have Check Change print out the value
			if (!caTotalCheckChangeForCustomerSet.equals(NEW_DOLLAR))
			{
				appendChangeToPrintVector(
					caTotalCheckChangeForCustomerSet,
					CHECK_CHANGE,
					REPORT_TOTAL_LINE,
					CUST_SET);
			}
		}
		appendFeesTotalsToPrintVector(
			lsLabel,
			caTotalFeesForCustomerSet);
		//	this.cRpt.blankLines(1);
		ciPrintLineIndex += 1;
		resetAccumulatorsCustomerSet();
	}

	/**
	 * This method prints out the remaining transaction lines for each
	 * transaction
	 * 
	 * @param aaTRRTransData
	 * 	TransactionReconciliationReportTransactionData
	 * @param asTransTime String
	 * @param asHoldTransCd String
	 * @throws RTSException
	 */
	public void appendCustomerSetTransactionsToPrintVector(
		TransactionReconciliationReportTransactionData aaTRRTransData,
		String asTransTime,
		String asHoldTransCd)
		throws RTSException
	{
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		String lsWsId =
			laThreeDigits.format(aaTRRTransData.getTransWsId());
		// added code to produce leading zero for office issuance No
		// < 100.
		String lsOfficeIssuanceNo =
			laThreeDigits.format(aaTRRTransData.getOfcIssuanceNo());
		String lsNameAndTransDesc = "";
		String lsTransInfoLine = "";
		if (!(asHoldTransCd.equals(VOID))
			&& !(asHoldTransCd.equals(VOIDNC))) // not an inventory void
		{
			// second line of transaction data
			// set up the field for customer name and the description of
			// the transaction
			if (!aaTRRTransData.getCustName1().equals("")
				|| aaTRRTransData.getCustName1().equals("null"))
			{
				lsNameAndTransDesc =
					aaTRRTransData.getCustName1()
						+ " - "
						+ aaTRRTransData.getTransCdDesc();
			}
			else
			{
				lsNameAndTransDesc = aaTRRTransData.getTransCdDesc();
			}
			buildPrintVector(
				lsNameAndTransDesc,
				COL_DESC_TRANS_ID_STARTPT,
				COL_CUST_NAME_LENGTH,
				LEFT,
				LINE_INDI_GENERAL);
			ciPrintLineIndex++;
		}
		// third line of transaction data
		// set up the fields for transaction id information
		lsTransInfoLine =
			//	String.valueOf(aTRRTransData.getOfcIssuanceNo())
	lsOfficeIssuanceNo
		+ lsWsId
		+ String.valueOf(aaTRRTransData.getTransAMDate())
		+ asTransTime
		+ ", "
		+ WORKSTATION
		+ " "
		+ String.valueOf(aaTRRTransData.getTransWsId())
		+ " "
		+ BY
		+ " "
		+ aaTRRTransData.getTransEmpId();
		buildPrintVector(
			lsTransInfoLine,
			COL_DESC_TRANS_ID_STARTPT,
			COL_TRANS_DETAIL_LINE_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
	}

	/**
	 * Append Customer Subtotals
	 * 
	 * @throws RTSException
	 */
	public void appendCustomerSubtotals() throws RTSException
	{
		appendSingleDashesToPrintVector();
		appendFeesTotalsToPrintVector(
			CUSTOMER_SUBTOTAL,
			caTotalFeesForCustomerSubtotal);
		caTotalFeesForCustomerSubtotal = NEW_DOLLAR;
	}

	/**
	 * Append Fees Totals To Print Vector
	 * 
	 * @param asLabel String
	 * @param aaDollar Dollar
	 * @throws RTSException
	 */
	public void appendFeesTotalsToPrintVector(
		String asLabel,
		Dollar aaDollar)
		throws RTSException
	{
		// defect 9091
		//String lsAmount = ""; 
		int liDeltaLength =
			aaDollar.printDollar().length() - (FEES_ITEM_PRICE_LENGTH);
		liDeltaLength = Math.max(liDeltaLength, 0);

		// print out the Fees report Total		
		buildPrintVector(
			asLabel,
			CUST_SET_FEES_STARTPT_INDENT - liDeltaLength,
			CUST_SET_PAY_LENGTH_INDENT,
			RIGHT,
			LINE_INDI_GENERAL);
		// print the dollar sign
		buildPrintVector(
			DOLLAR_SIGN,
			$_FEES_STARTPT - liDeltaLength,
			DOLLAR_SIGN_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
		// check to see if change amount is longer than the space that
		// is allocated
		String lsAmount = (aaDollar.printDollar()).substring(1);
		// print the amount of the fee
		buildPrintVector(
			lsAmount,
			FEES_ITEM_PRICE_STARTPT - liDeltaLength,
			FEES_ITEM_PRICE_LENGTH + liDeltaLength,
			RIGHT,
			LINE_INDI_GENERAL);
		// end defect 9091 

		ciPrintLineIndex++;
		cbPayFeesInvyDetail = true;
	}

	/**
	 * Append Inventory Detail Line To Print Vector
	 * 
	 * @param asItmCdDesc String
	 * @param aiInvItemYr int
	 * @param asInvItmNo String
	 * @param abMatchingFeesItmCd boolean
	 * @throws RTSException
	 */
	public void appendInvyDetailLineToPrintVector(
		String asItmCdDesc,
		int aiInvItemYr,
		String asInvItmNo,
		boolean abMatchingFeesItmCd)
		throws RTSException
	{
		buildPrintVector(
			asItmCdDesc,
			COL_FEES_DESC_STARTPT,
			COL_FEES_DESC_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
		cbPayFeesInvyDetail = true;
		// if there is an item year, print it
		if (aiInvItemYr > 0)
		{
			buildPrintVector(
				String.valueOf(aiInvItemYr),
				FEES_ITEM_YEAR_STARTPT,
				FEES_ITEM_YEAR_LENGTH,
				LEFT,
				LINE_INDI_GENERAL);
			cbPayFeesInvyDetail = true;
		}
		// if there is an item number, print it
		if (asInvItmNo != null)
		{
			buildPrintVector(
				asInvItmNo,
				FEES_ITEM_NUM_STARTPT,
				FEES_ITEM_NUM_LENGTH,
				RIGHT,
				LINE_INDI_GENERAL);
			cbPayFeesInvyDetail = true;
		}
	}

	/**
	 * Append No Payment Amount
	 * 
	 * @param asPaymentType String
	 * @throws RTSException
	 */
	public void appendNoPaymentAmount(String asPaymentType)
		throws RTSException
	{
		buildPrintVector(
			asPaymentType,
			COL_PAY_TYPE_STARTPT,
			COL_PAY_TYPE_LENGTH,
			RIGHT,
			LINE_INDI_PAYMENT);
		// print the dollar sign for the payment amount
		buildPrintVector(
			DOLLAR_SIGN,
			$_PAYMENT_STARTPT,
			DOLLAR_SIGN_LENGTH,
			LEFT,
			LINE_INDI_PAYMENT);
		// print the amount of the payment
		buildPrintVector(
			(NEW_DOLLAR.printDollar()).substring(1),
			COL_PAY_AMOUNT_STARTPT,
			COL_PAY_AMOUNT_LENGTH,
			RIGHT,
			LINE_INDI_PAYMENT);
		ciPrintLineIndex++;
	}

	/**
	 * Append Payment Amount To Print Vector
	 * 
	 * @param asLabel String
	 * @param aaDollar Dollar
	 * @throws RTSException
	 */
	public void appendPaymentAmountToPrintVector(
		String asLabel,
		Dollar aaDollar)
		throws RTSException
	{
		// defect 9091 
		//String lsAmount = "";
		int liDeltaLength =
			aaDollar.printDollar().length() - COL_PAY_AMOUNT_LENGTH;
		liDeltaLength = java.lang.Math.max(liDeltaLength, 0);

		// print the label
		buildPrintVector(
			asLabel,
			CUST_SET_PAY_STARTPT_INDENT - liDeltaLength,
			CUST_SET_PAY_LENGTH_INDENT,
			RIGHT,
			LINE_INDI_PAYMENT);
		// print the dollar sign for the payment amount
		buildPrintVector(
			DOLLAR_SIGN,
			$_PAYMENT_STARTPT - liDeltaLength,
			DOLLAR_SIGN_LENGTH,
			LEFT,
			LINE_INDI_PAYMENT);
		// print the amount of the payment
		String lsAmount = (aaDollar.printDollar()).substring(1);
		buildPrintVector(
			lsAmount,
			COL_PAY_AMOUNT_STARTPT - liDeltaLength,
			COL_PAY_AMOUNT_LENGTH + liDeltaLength,
			RIGHT,
			LINE_INDI_PAYMENT);
		// end defect 9091 

		cbPayFeesInvyDetail = true;
	}

	/**
	 * Append Primary Totals To Print Vector
	 * 
	 * @throws RTSException
	 */
	public void appendPrimaryTotalsToPrintVector() throws RTSException
	{
		// leave a blank line between last customer set and report
		// totals print the double dashed lines for the fees column
		buildPrintVector("", 0, 0, LEFT, LINE_INDI_GENERAL);
		ciPrintLineIndex++;
		// print the double dashed lines for the payment column
		buildPrintVector(
			DOUBLE_DASHES_PAYMENT,
			COL_PAY_AMOUNT_STARTPT,
			COL_PAY_AMOUNT_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);

		// print the double dashed lines for the fees column
		// defect 9091 
		buildPrintVector(
			DOUBLE_DASHES_PAYMENT,
			FEES_AMT_RPT_TOTAL_DASHES_STARTPT,
			FEES_AMT_RPT_TOTAL_LENGTH,
			RIGHT,
			LINE_INDI_GENERAL);
		// end defect 9091 
		ciPrintLineIndex++;
		appendPaymentAmountToPrintVector(
			csTotalFor,
			caTotalPaymentForPrimarySplit);
		// print out cash change if there is any
		if (!caTotalCashChangeForPrimarySplit.equals(NEW_DOLLAR))
		{
			appendChangeToPrintVector(
				caTotalCashChangeForPrimarySplit,
				CASH_CHANGE,
				REPORT_TOTAL_LINE,
				REPORT);
		}
		// print out check change if there is any
		if (!caTotalCheckChangeForPrimarySplit.equals(NEW_DOLLAR))
		{
			appendChangeToPrintVector(
				caTotalCheckChangeForPrimarySplit,
				CHECK_CHANGE,
				REPORT_TOTAL_LINE,
				REPORT);
		}
		appendFeesTotalsToPrintVector(
			csTotalFor,
			caTotalFeesForPrimarySplit);
		ciPrintLineIndex++;
	}

	/**
	 * appendReportTotalsToPrintVector
	 * 
	 * @throws RTSException
	 */
	public void appendReportTotalsToPrintVector() throws RTSException
	{
		String lsLabel = PAYMENT + " " + REPORT + " " + TOTAL + ":";

		// leave a blank line between last customer set and report 
		// totals print the double dashed lines for the fees column
		buildPrintVector("", 0, 0, LEFT, LINE_INDI_GENERAL);
		ciPrintLineIndex++;
		// print the double dashed lines for the payment column
		buildPrintVector(
			DOUBLE_DASHES_PAYMENT,
			COL_PAY_AMOUNT_STARTPT,
			COL_PAY_AMOUNT_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
		// print the double dashed lines for the fees column
		buildPrintVector(
			DOUBLE_DASHES_PAYMENT,
			FEES_AMT_RPT_TOTAL_DASHES_STARTPT,
			FEES_AMT_RPT_TOTAL_LENGTH,
			RIGHT,
			LINE_INDI_GENERAL);
		ciPrintLineIndex++;
		appendPaymentAmountToPrintVector(
			lsLabel,
			caTotalPaymentForReport);
		// print out cash change if there is any
		if (!caTotalCashChangeForReport.equals(NEW_DOLLAR))
		{
			appendChangeToPrintVector(
				caTotalCashChangeForReport,
				CASH_CHANGE,
				REPORT_TOTAL_LINE,
				REPORT);
		}
		// print out check change if there is any
		if (!caTotalCheckChangeForReport.equals(NEW_DOLLAR))
		{
			appendChangeToPrintVector(
				caTotalCheckChangeForReport,
				CHECK_CHANGE,
				REPORT_TOTAL_LINE,
				REPORT);
		}
		lsLabel = FEES + " " + REPORT + " " + TOTAL + ":";

		// print out the Fees report Total
		appendFeesTotalsToPrintVector(lsLabel, caTotalFeesForReport);
		// end defect 9091 
	}

	/**
	 * Append Single Dashes To Print Vector
	 * 
	 * @throws RTSException
	 */
	public void appendSingleDashesToPrintVector() throws RTSException
	{
		buildPrintVector(
			SINGLE_DASHES_FEES,
			FEES_ITEM_PRICE_STARTPT,
			FEES_ITEM_PRICE_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
		ciPrintLineIndex++;
	}

	/**
	 * Append Summary Header
	 *
	 * @throws RTSException 
	 */
	public void appendSummaryHeader() throws RTSException
	{
		// print out the Fees report Total		
		buildPrintVector(
			csSummaryFor,
			COL_CUST_SET_STARTPT,
			COL_TRANS_DETAIL_LINE_LENGTH,
			LEFT,
			LINE_INDI_GENERAL);
		ciPrintLineIndex++;
		// leave a blank line between header and customer set
		buildPrintVector("", 0, 0, LEFT, LINE_INDI_GENERAL);
		ciPrintLineIndex++;
	}

	/**
	 * Build Print Vector
	 * 
	 * @param asDesc String
	 * @param aiStartPoint int
	 * @param aiLength int
	 * @param aiJustification int
	 * @param aiLineTypeIndi int
	 * @throws RTSException
	 */
	public void buildPrintVector(
		String asDesc,
		int aiStartPoint,
		int aiLength,
		int aiJustification,
		int aiLineTypeIndi)
		throws RTSException
	{
		PrintVector laPrintVector = new PrintVector();
		laPrintVector.setDesc(asDesc);
		laPrintVector.setStartPoint(aiStartPoint);
		laPrintVector.setLength(aiLength);
		laPrintVector.setJustification(aiJustification);
		laPrintVector.setTypeOfLineIndi(aiLineTypeIndi);
		switch (ciTypeOfPrintVector)
		{
			case FEES_WITH_INVY :
				{
					laPrintVector.setLineNumber(
						ciHoldFeeInvyPrintLineIndex);
					cvHoldFeesWithInvy.addElement(laPrintVector);
					break;
				}
			case INVY_NO_FEES :
				{
					laPrintVector.setLineNumber(
						ciHoldInvyPrintLineIndex);
					cvHoldInvy.addElement(laPrintVector);
					break;
				}
			default :
				{
					laPrintVector.setLineNumber(ciPrintLineIndex);
					cvPrintVector.addElement(laPrintVector);
					break;
				}
		}
	}

	/**
	 * createMainHeadingVectors
	 * 
	 * @param aaFundsData FundsData
	 * @throws RTSException
	 */
	public void createMainHeadingVectors(FundsData aaFundsData)
		throws RTSException
	{
		// set variables to be used if specify date range is selected
		String lsStartDate = "";
		String lsEndDate = "";
		// set up a vector for additional main heading information
		Vector lvHeader = new Vector();
		Vector lvVector = aaFundsData.getSelectedCashDrawers();
		// set up a vector to contain all the information in the column
		// heading
		Vector lvTable = new Vector();
		// set up vectors for the each row of the column heading data
		Vector lvHeaderRow1 = new Vector();
		Vector lvHeaderRow2 = new Vector();
		Vector lvHeaderRow3 = new Vector();
		Vector lvHeaderRow4 = new Vector();
		// additional main headings
		switch (aaFundsData.getFundsReportData().getEntity())
		{
			case FundsConstant.CASH_DRAWER :
				{
					CashWorkstationCloseOutData laDrawer =
						(CashWorkstationCloseOutData) lvVector.get(0);
					lvHeader.addElement(ReportConstant.CASH_DRAWER);
					lvHeader.addElement(
						String.valueOf(laDrawer.getCashWsId()));
					break;
				}
			case FundsConstant.EMPLOYEE :
				{
					lvHeader.addElement(ReportConstant.EMPLOYEE);
					EmployeeData laEmpData = new EmployeeData();
					laEmpData =
						(EmployeeData) aaFundsData
							.getSelectedEmployees()
							.elementAt(
							0);
					lvHeader.addElement(laEmpData.getEmployeeId());
					break;
				}
			default :
				{
					// error message
				}
		}
		lvHeader.addElement(REPORT_TYPE);
		switch (aaFundsData.getFundsReportData().getRange())
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
					lvHeader.addElement(FOR_CLOSE);
					break;
				}
			case FundsConstant.SINCE_CLOSE :
				{
					lvHeader.addElement(LAST_CLOSEOUT);
					break;
				}
			case FundsConstant.CLOSE_OUT_FOR_DAY :
				{
					lvHeader.addElement(CLOSEOUT);
					break;
				}
			default :
				{
					// error message
					lvHeader.addElement(ReportConstant.CASH_DRAWER);
				}
		}
		if (aaFundsData.getFundsReportData().getRange()
			== FundsConstant.DATE_RANGE)
			// && (ciTypeOfBreak == CASH_DRAWER_NONE ||
			// ciTypeOfBreak == CASH_DRAWER_EMP))
		{
			// defect 9943 
			lsStartDate =
				aaFundsData
					.getFundsReportData()
					.getFromRange()
					.toString()
					+ " "
					+ aaFundsData
						.getFundsReportData()
						.getFromRange()
						.getClockTimeNoMs();
			//.getClockTime().substring(0, 8);
			lsEndDate =
				aaFundsData
					.getFundsReportData()
					.getToRange()
					.toString()
					+ " "
					+ aaFundsData
						.getFundsReportData()
						.getToRange()
						.getClockTimeNoMs();
			//.getClockTime().substring(0,8);
			// end defect 9943  

			lvHeader.addElement(TRANS_FROM);
			lvHeader.addElement(lsStartDate + THROUGH + lsEndDate);
		}
		else
		{
			Vector lvDrawers =
				(Vector) aaFundsData.getSelectedCashDrawers();
			CashWorkstationCloseOutData laDrawer =
				(CashWorkstationCloseOutData) lvDrawers.get(0);

			// defect 9943 
			lsStartDate =
				laDrawer.getCloseOutBegTstmp().toString()
					+ " "
					+ laDrawer.getCloseOutBegTstmp().getClockTimeNoMs();
			//.getClockTime().substring(0,8);
			lsEndDate =
				laDrawer.getCloseOutEndTstmp().toString()
					+ " "
					+ laDrawer.getCloseOutEndTstmp().getClockTimeNoMs();
			//.getClockTime().substring(0,8);
			// end defect 9943 

			lvHeader.addElement(TRANS_FROM);
			lvHeader.addElement(lsStartDate + THROUGH + lsEndDate);
		}
		// set up the column headers
		ColumnHeader laColumn1 =
			new ColumnHeader(
				HEADER_CASH,
				COL_CASH_START_STARTPT,
				COL_CASH_LENGTH);
		lvHeaderRow1.addElement(laColumn1);
		lvTable.addElement(lvHeaderRow1);
		ColumnHeader laColumn2 =
			new ColumnHeader(
				HEADER_CHANGE_SLASH,
				COL_CASH_START_STARTPT,
				COL_CASH_LENGTH);
		lvHeaderRow2.addElement(laColumn2);
		lvTable.addElement(lvHeaderRow2);
		ColumnHeader laColumn3 =
			new ColumnHeader(
				HEADER_CUST_NAME,
				COL_DESC_TRANS_ID_STARTPT,
				COL_CUST_TRANS_ID_HDR_LENGTH);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				HEADER_PAYMENT,
				COL_PAY_TYPE_STARTPT,
				COL_PAY_TYPE_LENGTH,
				RIGHT);
		ColumnHeader laColumn5 =
			new ColumnHeader(
				HEADER_CHECK,
				COL_CASH_START_STARTPT,
				COL_CASH_LENGTH);
		ColumnHeader laColumn6 =
			new ColumnHeader(
				HEADER_FEE_ITEM,
				COL_FEES_DESC_STARTPT,
				COL_FEES_DESC_LENGTH);
		ColumnHeader laColumn7 =
			new ColumnHeader(
				HEADER_ITEM,
				FEES_ITEM_YEAR_STARTPT,
				FEES_ITEM_YEAR_LENGTH);
		ColumnHeader laColumn8 =
			new ColumnHeader(
				HEADER_ITEM,
				FEES_ITEM_NUM_STARTPT,
				FEES_ITEM_NUM_LENGTH,
				CENTER);
		ColumnHeader laColumn9 =
			new ColumnHeader(
				HEADER_ITEM + " ",
				FEES_ITEM_PRICE_STARTPT,
				FEES_ITEM_PRICE_LENGTH,
				RIGHT);
		lvHeaderRow3.addElement(laColumn3);
		lvHeaderRow3.addElement(laColumn4);
		lvHeaderRow3.addElement(laColumn5);
		lvHeaderRow3.addElement(laColumn6);
		lvHeaderRow3.addElement(laColumn7);
		lvHeaderRow3.addElement(laColumn8);
		lvHeaderRow3.addElement(laColumn9);
		lvTable.addElement(lvHeaderRow3);
		ColumnHeader laColumn10 =
			new ColumnHeader(
				HEADER_TRANS_ID,
				COL_DESC_TRANS_ID_STARTPT,
				COL_CUST_TRANS_ID_HDR_LENGTH);
		ColumnHeader laColumn11 =
			new ColumnHeader(
				HEADER_TYPE + "   ",
				COL_PAY_TYPE_STARTPT,
				COL_PAY_TYPE_LENGTH,
				RIGHT);
		ColumnHeader laColumn12 =
			new ColumnHeader(
				HEADER_PAYMENT,
				COL_PAY_AMOUNT_STARTPT,
				COL_PAY_AMOUNT_LENGTH,
				RIGHT);
		ColumnHeader laColumn13 =
			new ColumnHeader(
				HEADER_CHANGE_ASTERISK,
				COL_CASH_START_STARTPT,
				COL_CASH_LENGTH);
		ColumnHeader laColumn14 =
			new ColumnHeader(
				HEADER_FEES_DESCRIPTION,
				COL_FEES_DESC_STARTPT,
				COL_FEES_DESC_LENGTH);
		ColumnHeader laColumn15 =
			new ColumnHeader(
				HEADER_YEAR,
				FEES_ITEM_YEAR_STARTPT,
				FEES_ITEM_YEAR_LENGTH);
		ColumnHeader laColumn16 =
			new ColumnHeader(
				HEADER_NUMBER,
				FEES_ITEM_NUM_STARTPT,
				FEES_ITEM_NUM_LENGTH,
				CENTER);
		ColumnHeader laColumn17 =
			new ColumnHeader(
				HEADER_PRICE,
				FEES_ITEM_PRICE_STARTPT,
				FEES_ITEM_PRICE_LENGTH,
				RIGHT);
		lvHeaderRow4.addElement(laColumn10);
		lvHeaderRow4.addElement(laColumn11);
		lvHeaderRow4.addElement(laColumn12);
		lvHeaderRow4.addElement(laColumn13);
		lvHeaderRow4.addElement(laColumn14);
		lvHeaderRow4.addElement(laColumn15);
		lvHeaderRow4.addElement(laColumn16);
		lvHeaderRow4.addElement(laColumn17);
		lvTable.addElement(lvHeaderRow4);
		cvTable = lvTable;
		cvHeader = lvHeader;
		// produce the header
		generateHeader(cvHeader, cvTable);
	}

	/**
	 * Find Inventory Record
	 * 
	 * @param asInvyKeyWithFeeCd String
	 * @param avInventoryDataObject Vector
	 * @param asTransFeesInvyKey String
	 * @throws RTSException
	 */
	public void findInventoryRecord(
		String asInvyKeyWithFeeCd,
		Vector avInventoryDataObject,
		String asTransFeesInvyKey)
		throws RTSException
	{
		String lsCurrInvyKeyWithNoFeeCd = "";
		String lsCurrInvyKeyWithFeeCd = "";
		boolean lbHaveInvItmNo = false;
		// set up format for TransTime
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		if (avInventoryDataObject != null
			&& (avInventoryDataObject.size() > 0))
		{
			while (ciInventoryIndex < avInventoryDataObject.size())
				//Loop through the results
			{
				TransactionReconciliationReportInventoryData laTRRInventoryData =
					(
						TransactionReconciliationReportInventoryData) avInventoryDataObject
							.elementAt(
						ciInventoryIndex);
				// format trans time with leading zeros	
				String lsTransTime =
					laSixDigits.format(
						laTRRInventoryData.getTransTime());
				// set lsTransactionDataKey with the fields from the
				// current record
				lsCurrInvyKeyWithNoFeeCd =
					String.valueOf(
						laTRRInventoryData.getOfcIssuanceNo())
						+ String.valueOf(
							laTRRInventoryData.getTransWsId())
						+ String.valueOf(
							laTRRInventoryData.getTransAMDate())
						+ lsTransTime;
				lsCurrInvyKeyWithFeeCd =
					lsCurrInvyKeyWithNoFeeCd
						+ laTRRInventoryData.getItmCd();
				// Checking to see if we need to keep reading the
				// Inventory data object for the curr invy key we are
				// looking for.  If the current invy data key is less
				// than the key coming from the transaction file, we
				// need to keep reading invy until we find a match or
				// the invy key becomes greater than the search value
				// key coming in. 
				if (Long.parseLong(lsCurrInvyKeyWithNoFeeCd)
					< Long.parseLong(asTransFeesInvyKey))
				{
					ciInventoryIndex += 1;
					csHoldInvyKey = lsCurrInvyKeyWithNoFeeCd;
				}
				else
				{ // begin  curr Core Key <= Core Key coming in.
					// check to see if  the core part of the keys match
					if (lsCurrInvyKeyWithNoFeeCd
						.equals(asTransFeesInvyKey))
					{ // begin core Key equals core search key
						// check to see if the core keys with the fee code
						// match
						if (lsCurrInvyKeyWithFeeCd
							.equals(asInvyKeyWithFeeCd))
						{
							// if there is an item year, print it on the
							// same line as the fee information
							if (laTRRInventoryData.getInvItmYr() > 0)
							{
								//		this.cRpt.print(
								buildPrintVector(
									String.valueOf(
										laTRRInventoryData
											.getInvItmYr()),
									FEES_ITEM_YEAR_STARTPT,
									FEES_ITEM_YEAR_LENGTH,
									LEFT,
									LINE_INDI_GENERAL);
							}
							// if there is an item number, print it
							if (laTRRInventoryData.getInvItmNo()
								!= null)
							{
								//		this.cRpt.rightAlign(
								buildPrintVector(
									laTRRInventoryData.getInvItmNo(),
									FEES_ITEM_NUM_STARTPT,
									FEES_ITEM_NUM_LENGTH,
									RIGHT,
									LINE_INDI_GENERAL);
								ciInventoryIndex += 1;
								lbHaveInvItmNo = true;
							}
							csHoldInvyKey = lsCurrInvyKeyWithNoFeeCd;
							break;
						} // end of found a match with Core + Fee cd key
						// begin of core keys match, but there is
						// not a matching fee code
						else
						{
							// check to see if the core + fee cd key
							// from the current record is less
							// than what is being searched for.  If it
							// is less than, we need to place it
							// in a vector so it can be printed after
							// all the fees have been printed out.
							int i =
								lsCurrInvyKeyWithFeeCd.compareTo(
									asInvyKeyWithFeeCd);
							if (i < 0)
							{
								ciTypeOfPrintVector = INVY_NO_FEES;
								// value of CurrKey + FeeCd is less than
								// Search Key + fee code that is coming 
								// in. keep processing the inventory
								// vector until we find a match or we
								// exceed the search value. if there is 
								// an item year, print it on the same
								// line as the fee information
								if (laTRRInventoryData.getInvItmYr()
									> 0)
								{
									//		this.cRpt.print(
									buildPrintVector(
										String.valueOf(
											laTRRInventoryData
												.getInvItmYr()),
										FEES_ITEM_YEAR_STARTPT,
										FEES_ITEM_YEAR_LENGTH,
										LEFT,
										LINE_INDI_GENERAL);
								}
								// if there is an item number, print it
								if (laTRRInventoryData.getInvItmNo()
									!= null)
								{
									//		this.cRpt.rightAlign(
									buildPrintVector(
										laTRRInventoryData
											.getInvItmNo(),
										FEES_ITEM_NUM_STARTPT,
										FEES_ITEM_NUM_LENGTH,
										RIGHT,
										LINE_INDI_GENERAL);
									ciInventoryIndex += 1;
									lbHaveInvItmNo = true;
								}
								ciInventoryIndex += 1;
								ciTypeOfPrintVector = ADD_TO_PV_IMMED;
							}
							//  CurrKey + FeeCd is greater than or
							// equal to the Search Key + fee code that
							// is coming in.
							else
							{
								// exit the method.  Next Fee record will
								// call this method.
								csHoldInvyKey =
									lsCurrInvyKeyWithNoFeeCd;
								break;
							}
						} // end of core keys match, but there is not a
						// matching fee code
					} // begin core Key equals core search key

					// core key does not equal search key and it is
					// greater than the search key.
					// exit the method.
					else
					{
						break;
					}
				} // begin  curr Core Key <= Core Key coming in.
			} // while (ciInventoryIndex < avInventoryDataObject.size())
			//Loop through the results
		} // end of 	if(!(avInventoryDataObject == null)
		// && (avInventoryDataObject.size() > 0))
		return;
	}

	/**
	 * This method is used the main program that launches the methods
	 * to process the following objects in the order listed:
	 *
	 * ProcessTransReconTransactionData
	 * ProcessTransReconPaymentData
	 * ProcessTransReconFeesDate
	 * ProcessTransReconInventoryData
	 *
	 * ProcessTransReconTransactionData is the main driver of this
	 * class.  It calls the other methods within it.  There are more 
	 * detailed comments in the method processTransReconTransactionData(
	 * vector);
	 * 
	 * @param avResults[] Vector
	 * @param aaFundsData FundsData
	 * @throws RTSException
	 */
	public void formatReport(Vector avResults[], FundsData aaFundsData)
		throws RTSException
	{
		// Get the appropriate Report Number and Title
		int liReportType = aaFundsData.getFundsReportData().getRange();
		setReportNum(liReportType);
		ciTypeOfBreak = setUpBreakByCode(aaFundsData);
		// print the main header
		createMainHeadingVectors(aaFundsData);
		// get number of lines that available for printing	
		ciDetailLines = getNoOfDetailLines() - REPORT_FOOTER_LINES;
		// start processing the vectors
		processTransReconTransactionData(avResults);
		// defect 8628 
		//this.caRpt.nextLine();
		//generateEndOfReport();
		//generateFooter();
		generateFooter(true);
		// end defect 8628  
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
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Get Amount Overflow Symbol
	 * 
	 * @param aiLength int
	 * @return String
	 * @throws RTSException
	 */
	public String getAmountOverflowSymbol(int aiLength)
		throws RTSException
	{
		String lsOverflow = "";
		for (int i = 0; i < aiLength; i++)
		{
			if (i == aiLength - 3)
			{
				lsOverflow = lsOverflow + ".";
				// insert the decimal point
			}
			else
			{
				lsOverflow += "#";
			}
		}
		lsOverflow.trim();
		return lsOverflow;
	}

	/**
	 * Starts the application.
	 *
	 * @param aarrArgs String[] of command-line arguments
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		// Instantiating a new Report Class
		// set up blank title to be replaced once the new pmy object is 
		// created.
		ReportProperties aaRptProps = new ReportProperties("");
		TransReconReport laTRR = new TransReconReport("", aaRptProps);
		FundsData laFundsData = new FundsData();
		// queryDataVectors has all the information needed.  
		Vector[] lvCreatedData = laTRR.queryDataVectors("");
		if (lvCreatedData[4] != null && lvCreatedData[4].size() > 0)
		{
			laFundsData = (FundsData) lvCreatedData[4].elementAt(0);
		}
		laTRR.formatReport(lvCreatedData, laFundsData);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\TransReconRpt.txt");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laTRR.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\TransReconRpt.txt");
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
			// change setVisible to setVisibleRTS
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
	 * Pad the Employee Id to 7 characters
	 * 
	 * @param asEmpId String
	 * @return String
	 */
	public String padEmpId(String asEmpId)
	{
		asEmpId = UtilityMethods.addPaddingRight(asEmpId, 7, " ");
		return asEmpId;
	}

	/**
	 * Print Transaction
	 * 
	 * @throws RTSException
	 */
	public void printTransaction() throws RTSException
	{
		int k = 0;
		boolean lbNeedNewPage = true;
		boolean lbPrintedPaymentRecords = false;
		int liLineNumber = 1;
		ciPrintLineIndex = 1; // reset the field used in the vector.
		if (cvPrintVector != null && cvPrintVector.size() > 0)
		{
			PrintVector laPld = new PrintVector();
			laPld =
				(PrintVector) cvPrintVector.elementAt(
					cvPrintVector.size() - 1);
			int liNumLinesToBePrinted = laPld.getLineNumber();
			// if the number of total lines in the transaction is
			// greater than the number of maximum number of detail lines
			// available on a page, special formatting is required.
			//
			// First, go to a new page.
			//  
			// Next, print the first line of the transaction detail and 
			// all of the payment records.
			//
			// After all payment records have been printed out, generate
			// a footer and a header to create a new page.
			//
			// print the next two lines to the transaction detail and
			// begin printing the fees items.  need to keep checking to 
			// see if a new page should be created because of running
			// out of space at the bottom of the page.
			//
			// Do as many new pages as required and total the customer
			// set.
			//
			// Normal processing can resume after entire transaction has
			// completed print processing.
			//
			// If the number of lines to be printed is less that total
			// detail lines available for a page, check to see if the
			// transaction will print on the current page.  If not,
			// printer the footer and the header.
			if ((this.caRptProps.getPageHeight()
				- this.caRpt.getCurrX()
				- REPORT_FOOTER_LINES)
				< liNumLinesToBePrinted)
			{
				// the transaction will fit on one page but there is not
				// enough room available to print the transaction.
				if (liNumLinesToBePrinted <= ciDetailLines)
				{
					generateFooter();
					generateHeader(cvHeader, cvTable);
					k = 0;
				}
				else
				{
					// the transaction will not fit on one page, so need
					// to start aa new page, except if it is the first
					// transaction of the report.
					if (this.caRpt.getPages() != 1
						|| (this.caRpt.getPages() == 1
							&& this.caRpt.getCurrX() > 20))
					{
						generateFooter();
						generateHeader(cvHeader, cvTable);
						k = 0;
					}
				}
			}
			int i = 0;
			while (cvPrintVector != null && i < cvPrintVector.size())
			{
				laPld = (PrintVector) cvPrintVector.elementAt(i);
				// this is the code that handles the special processing
				// of a transaction
				// that requires more than one page
				if (liNumLinesToBePrinted > ciDetailLines)
					// more than one page needed
				{
					if (!lbPrintedPaymentRecords)
					{
						if (laPld.getTypeOfLineIndi()
							== LINE_INDI_PAYMENT)
						{
							lbPrintedPaymentRecords = true;
						}
					}
					else
					{
						// need to put in a page break after all of the
						// payments have been printed.
						if (lbPrintedPaymentRecords
							&& laPld.getTypeOfLineIndi()
								!= LINE_INDI_PAYMENT
							&& lbNeedNewPage)
						{
							lbNeedNewPage = false;
							generateFooter();
							generateHeader(cvHeader, cvTable);
							k = 0;
						}
					}
					// check to see if the number of lines alread
					// printed exceeds what is available, if it does
					// exceed that amount generate a footer	and header.
					if (k > ciDetailLines
						&& liLineNumber != laPld.getLineNumber())
					{
						generateFooter();
						generateHeader(cvHeader, cvTable);
						k = 0;
					}
				}
				// end of special proessing.
				if (laPld.getLineNumber() != liLineNumber)
				{
					this.caRpt.nextLine();
					liLineNumber = laPld.getLineNumber();
					// count the number of lines  printed on the page	
					k++;
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
	}

	/**
	 * Process Change Due Record
	 * 
	 * @param aaTRRPayData TransactionReconciliationReportPaymentData
	 * @throws RTSException
	 */
	public void processChangeDueRecord(TransactionReconciliationReportPaymentData aaTRRPayData)
		throws RTSException
	{
		Dollar laChangeDueForTrans = NEW_DOLLAR;
		laChangeDueForTrans = aaTRRPayData.getPymntTypeAmt();
		if (aaTRRPayData.getPymntTypeCdDesc().equals(HEADER_CASH))
		{
			csTypeOfChangeDue = CASH_CHANGE;
			caTotalCashChangeForCustomerSet =
				caTotalCashChangeForCustomerSet.addNoRound(
					laChangeDueForTrans);
			// accumulate totals for cash change for primary split
			if (ciTypeOfBreak == EMP_CASH_DRAWER
				|| ciTypeOfBreak == CASH_DRAWER_EMP)
			{
				caTotalCashChangeForPrimarySplit =
					caTotalCashChangeForPrimarySplit.addNoRound(
						laChangeDueForTrans);
			}
			// accumulate the report total for cash change
			caTotalCashChangeForReport =
				caTotalCashChangeForReport.addNoRound(
					laChangeDueForTrans);
		}
		else
		{
			csTypeOfChangeDue = CHECK_CHANGE;
			// accumulate the check change total for the customer set
			caTotalCheckChangeForCustomerSet =
				caTotalCheckChangeForCustomerSet.addNoRound(
					laChangeDueForTrans);
			// accumulate the check change total for the primary split
			if (ciTypeOfBreak == EMP_CASH_DRAWER
				|| ciTypeOfBreak == CASH_DRAWER_EMP)
			{
				caTotalCheckChangeForPrimarySplit =
					caTotalCheckChangeForPrimarySplit.addNoRound(
						laChangeDueForTrans);
			}
			// accumulate the report total for cash change
			caTotalCheckChangeForReport =
				caTotalCheckChangeForReport.addNoRound(
					laChangeDueForTrans);
		}
		// deduct the amount of the change from the Payment total for
		// the customer set
		caTotalPaymentForCustomerSet =
			caTotalPaymentForCustomerSet.subtractNoRound(
				laChangeDueForTrans);
		// deduct the amount of the change from the primary split
		// payment total	
		if (ciTypeOfBreak == EMP_CASH_DRAWER
			|| ciTypeOfBreak == CASH_DRAWER_EMP)
		{
			caTotalPaymentForPrimarySplit =
				caTotalPaymentForPrimarySplit.subtractNoRound(
					laChangeDueForTrans);
		}
		// accumulate totals for Report - payments
		caTotalPaymentForReport =
			caTotalPaymentForReport.subtractNoRound(
				laChangeDueForTrans);
	}

	/**
	 * Process Hold Inventory Vector 
	 * 
	 * @throws RTSException
	 */
	public void processHoldInventoryVector() throws RTSException
	{
		int j = 0;
		int liHoldLineNumber = 0;
		while (j < cvHoldInvy.size())
		{
			PrintVector laPrintVector =
				(PrintVector) cvHoldInvy.elementAt(j);
			if (j == 0)
			{
				ciPrintLineIndex++;
				liHoldLineNumber = laPrintVector.getLineNumber();
			}
			else
			{
				if (laPrintVector.getLineNumber() != liHoldLineNumber)
				{
					ciPrintLineIndex++;
					liHoldLineNumber = laPrintVector.getLineNumber();
				}
			}
			buildPrintVector(
				laPrintVector.getDesc(),
				laPrintVector.getStartPoint(),
				laPrintVector.getLength(),
				laPrintVector.getJustification(),
				laPrintVector.getTypeOfLineIndi());
			j++;
		}
	}

	/**
	 * Process Payment Record
	 *
	 * @param aaTRRPayData TransactionReconciliationReportPaymentData
	 * @throws RTSException
	 */
	public void processPaymentRecord(TransactionReconciliationReportPaymentData aaTRRPayData)
		throws RTSException
	{
		// accumulate totals for customer set - payments
		caTotalPaymentForCustomerSet =
			caTotalPaymentForCustomerSet.addNoRound(
				aaTRRPayData.getPymntTypeAmt());
		// accumulate totals for primary split
		if (ciTypeOfBreak == EMP_CASH_DRAWER
			|| ciTypeOfBreak == CASH_DRAWER_EMP)
		{
			caTotalPaymentForPrimarySplit =
				caTotalPaymentForPrimarySplit.addNoRound(
					aaTRRPayData.getPymntTypeAmt());
		}
		// accumulate totals for Report - payments
		caTotalPaymentForReport =
			caTotalPaymentForReport.addNoRound(
				aaTRRPayData.getPymntTypeAmt());
		// print the payment type descriptions	
		buildPrintVector(
			aaTRRPayData.getPymntTypeCdDesc(),
			COL_PAY_TYPE_STARTPT,
			COL_PAY_TYPE_LENGTH,
			RIGHT,
			LINE_INDI_PAYMENT);
		// print the dollar sign for the payment amount
		buildPrintVector(
			DOLLAR_SIGN,
			$_PAYMENT_STARTPT,
			DOLLAR_SIGN_LENGTH,
			LEFT,
			LINE_INDI_PAYMENT);
		// print the amount of the payment
		buildPrintVector(
			(aaTRRPayData.getPymntTypeAmt().printDollar()).substring(1),
			COL_PAY_AMOUNT_STARTPT,
			COL_PAY_AMOUNT_LENGTH,
			RIGHT,
			LINE_INDI_PAYMENT);
		if (!cbChangeDuePrinted)
			// will only have one change due type per transaction.
			// Either check or change, but not both
		{ // have change
			if (!caTotalCashChangeForCustomerSet.equals(NEW_DOLLAR))
			{
				appendChangeToPrintVector(
					caTotalCashChangeForCustomerSet,
					csTypeOfChangeDue,
					false,
					"");
			}
			else if (
				!caTotalCheckChangeForCustomerSet.equals(NEW_DOLLAR))
			{
				appendChangeToPrintVector(
					caTotalCheckChangeForCustomerSet,
					csTypeOfChangeDue,
					false,
					"");
			}
		}
		// Set to true once the payment record is printed.  If there is
		// change due it will be printed and if there isn't change due,
		// we don't want the processTransReconPaymentDataObject to print
		// a blank line.
		cbChangeDuePrinted = true;
		ciPrintLineIndex++;
	}

	/**
	 * Process Transaction Recon Fees Data Object
	 * 
	 * @param avFeesDataObject Vector
	 * @param avInventoryDataObject Vector
	 * @param asTransFeesKey String
	 * @throws RTSException
	 */
	public String processTransReconFeesDataObject(
		Vector avFeesDataObject,
		Vector avInventoryDataObject,
		String asTransFeesKey)
		throws RTSException
	{
		boolean lbNeedToPrintRecord = false;
		String lsFindInventoryKeyWithItmCd;
		String lsCurrFeesDataKey = "";
		int k = 0;
		ciHoldPrintLineIndex = 1;
		// set up format for TransTime, Need to have leading zeros for
		// comparing strings
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		while (ciFeesIndex < avFeesDataObject.size())
			//Loop through the results
		{
			TransactionReconciliationReportFeesData laTRRFeesData =
				(
					TransactionReconciliationReportFeesData) avFeesDataObject
						.elementAt(
					ciFeesIndex);
			cbHoldPrintVector = false;
			lbNeedToPrintRecord = false;
			// format trans time with leading zeros, Need to have
			// leading zeros for string comparison.	
			// set up a current fees data key to match to
			// TransactionDataKey
			lsCurrFeesDataKey = setUpCurrFeesSearchKey(laTRRFeesData);
			// Checking to see if we need to keep reading the Fees data
			// object for the key we are looking for.
			// If the current fees key is less the trans fees key that
			// was passed in, we need to keep reading fees until 
			// we find a match or the fees key becomes greater than the
			// search value key coming in. 
			k = lsCurrFeesDataKey.compareTo(asTransFeesKey);
			// If the current Fees data key is less than the fees key
			// coming from the transaction,
			// we need to keep reading fees until we find a match or 
			// the fee key becomes greater than the search value key
			// coming in. 
			if (k < 0)
				// curr fee key is less than key we are looking for,
				// keep reading.
			{
				ciFeesIndex++;
			}
			else
			{ // current Fees key is equal to or greater than the
				// Transaction key that was passed in
				if (k == 0)
					//  current fees key matches the Transaction Fees
					// key that is passed in
				{
					cbFeesOrInvyRecordFound = true;
					if ((avInventoryDataObject != null
						&& avInventoryDataObject.size() > 0)
						&& (ciInventoryIndex
							< avInventoryDataObject.size()))
					{
						// if the fees key is greater than the inventory
						// key that is on hold, need to go and see if 
						// there is a matching inventory item on the
						// inventory vector.  If the hold invy key is 
						// greater, we have either processed all the
						// necessary inventory items and/or moved beyond
						// what we are looking for.  The matching
						// inventory item needs to be printed on the
						// same line as the fee cd item.
						int j =
							lsCurrFeesDataKey.compareTo(csHoldInvyKey);
						// the current fee key is greater than the invy
						// key, go check the inventory file and there is
						// an item code

						if (j >= 0
							&& laTRRFeesData.getAcctItmCd() != null)
						{
							// defect 10133 
							if (!laTRRFeesData.isHB3095Placard())
							{
								// set up the key
								lsFindInventoryKeyWithItmCd =
									lsCurrFeesDataKey
										+ laTRRFeesData.getAcctItmCd();
								lbNeedToPrintRecord =
									processTransReconInventoryDataObject(
										avInventoryDataObject,
										lsFindInventoryKeyWithItmCd,
										laTRRFeesData.getItmPrice(),
										laTRRFeesData
											.getAcctItmCdDesc());
								lsFindInventoryKeyWithItmCd = "";
							}
							// end defect 10133 
						}
					}
					if (ciInventoryIndex > avInventoryDataObject.size()
						|| !lbNeedToPrintRecord)
					{
						// print the fee/item description
						ciTypeOfPrintVector = ADD_TO_PV_IMMED;
						buildPrintVector(
							laTRRFeesData.getAcctItmCdDesc(),
							COL_FEES_DESC_STARTPT,
							COL_FEES_DESC_LENGTH,
							LEFT,
							LINE_INDI_GENERAL);
					}
					// if there is an amount print the dollar sign and
					// the amount
					if (laTRRFeesData.getItmPrice() != null)
					{
						if (!lbNeedToPrintRecord)
						{
							// print the dollar sign
							buildPrintVector(
								DOLLAR_SIGN,
								$_FEES_STARTPT,
								DOLLAR_SIGN_LENGTH,
								LEFT,
								LINE_INDI_GENERAL);
							// print the amount of the fee
							buildPrintVector(
								(
									laTRRFeesData
										.getItmPrice()
										.printDollar())
										.substring(
									1),
								FEES_ITEM_PRICE_STARTPT,
								FEES_ITEM_PRICE_LENGTH,
								RIGHT,
								LINE_INDI_GENERAL);
							ciPrintLineIndex++;
						}
						// accumulate totals to the customer subtotal
						caTotalFeesForCustomerSubtotal =
							caTotalFeesForCustomerSubtotal.addNoRound(
								laTRRFeesData.getItmPrice());
						// accumulate the totals for primary split
						caTotalFeesForPrimarySplit =
							caTotalFeesForPrimarySplit.addNoRound(
								laTRRFeesData.getItmPrice());
						// accumulate fees for the customer set
						caTotalFeesForCustomerSet =
							caTotalFeesForCustomerSet.addNoRound(
								laTRRFeesData.getItmPrice());
						// accumulate fees for the report totals
						caTotalFeesForReport =
							caTotalFeesForReport.addNoRound(
								laTRRFeesData.getItmPrice());
					} // end of have an item price
					// track the index of the fees object, we can start
					// at that location when we come in the next time
					cbSingleDashedLineNeeded = true;
					ciFeesIndex++;
				} // end of current fees key is equal to the Transaction
				// Fee key that is passed in
				else
				{ // the current fees key is greater the the Transaction
					// Fee key that is passed in
					break;
				}
			} // end of else for curr key >= to key passed in.
		} // end of while
		return lsCurrFeesDataKey;
	} // end of primary statement

	/**
	 * Process Transaction Recon Inventory Data Object
	 * 
	 * @param avInventoryDataObject Vector
	 * @param asTransInvyKey String
	 * @param aaFees Dollar
	 * @param aiFeesDescription String
	 * @throws RTSException
	 */
	public boolean processTransReconInventoryDataObject(
		Vector avInventoryDataObject,
		String asTransInvyKey,
		Dollar aaFees,
		String aiFeesDescription)
		throws RTSException
	{
		String lsCurrInventoryKey = "";
		int k = 0;
		boolean lbMatchingFeesItmCd = false;
		// set up format for TransTime
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		while (ciInventoryIndex < avInventoryDataObject.size())
			//Loop through the results
		{
			TransactionReconciliationReportInventoryData laTRRInventoryData =
				(
					TransactionReconciliationReportInventoryData) avInventoryDataObject
						.elementAt(
					ciInventoryIndex);
			// format trans time with leading zeros.  Need to have
			// leading zeros for string comparison.	
			String lsTransTime =
				laSixDigits.format(laTRRInventoryData.getTransTime());
			// set up a current pay data key to match to
			// TransactionDataKey
			lsCurrInventoryKey =
				setUpCurrInvySearchKey(laTRRInventoryData);
			// checking to see if the key being passed in is longer than
			// the base Invy key.  This key is only longer than the core
			// key if we are passing it in from the 
			// processReconFeesDataObjects. The fee item code is 
			// appended to the core key.  The key passed in from
			// processTransReconTransactionData does not have the 
			// appended item code appended to it.  So when the lengh of 
			// the key passed in is greater than ciInvyKeyLength, it is
			// telling us that we are processing a Fees record.  
			// ciInvyKeyLength is set in 
			// processTransReconTransactionData.
			if (asTransInvyKey.length() > ciInvyKeyLength)
				lsCurrInventoryKey =
					lsCurrInventoryKey + laTRRInventoryData.getItmCd();
			// if the current Inventory key is less than what we are
			// searching for, keep reading the vector until there
			// is a match or the current inventory key excedes the
			// search key.   If the Transaction invy key is greater,
			// we have either processed all the necessary inventory
			// items or moved beyond what we are looking for.
			k = lsCurrInventoryKey.compareTo(asTransInvyKey);
			if (k < 0) // current key is less than key passed in
			{
				// check to see if the current base inventory key is the
				// same as the core inventory key that is passed in.
				// this is the inventory key without the item cd.  If
				// they are the same need to place those inventory items
				// in a hold vector that will be printed out after all
				// the fee items have been printed out.
				int j =
					lsCurrInventoryKey.substring(
						0,
						ciInvyKeyLength).compareTo(
						(asTransInvyKey.substring(0, ciInvyKeyLength)));
				if (j == 0)
					// core keys match.  Only when we are searching for
					// an item that has a key code
				{
					cbFeesOrInvyRecordFound = true;
					lbMatchingFeesItmCd = false;
					if (csHoldTransCd.equals(VOID)
						|| csHoldTransCd.equals(VOIDNC))
					{
						ciTypeOfPrintVector = ADD_TO_PV_IMMED;
					}
					else
					{
						ciTypeOfPrintVector = INVY_NO_FEES;
					}
					appendInvyDetailLineToPrintVector(
						laTRRInventoryData.getItmCdDesc(),
						laTRRInventoryData.getInvItmYr(),
						laTRRInventoryData.getInvItmNo(),
						lbMatchingFeesItmCd);
					ciHoldInvyPrintLineIndex++;
					cbHoldPrintVector = false;
					ciTypeOfPrintVector = ADD_TO_PV_IMMED;
				}
				ciInventoryIndex++;
				cbSingleDashedLineNeeded = true;
			} // end of current key is less than the key passed in
			else
			{ // current key is equal to or greater than the key passed
				if (k == 0)
					// current inventory key is the same as the key passed
				{
					cbFeesOrInvyRecordFound = true;
					// printing an item that has an item code appended
					// to it.
					if (lsCurrInventoryKey.length() > ciInvyKeyLength)
					{
						// has an inventory item associated, print later
						lbMatchingFeesItmCd = true;
						ciTypeOfPrintVector = FEES_WITH_INVY;
						// print out the detail line
						appendInvyDetailLineToPrintVector(
							aiFeesDescription,
							laTRRInventoryData.getInvItmYr(),
							laTRRInventoryData.getInvItmNo(),
							lbMatchingFeesItmCd);
						// print the dollar sign
						buildPrintVector(
							DOLLAR_SIGN,
							$_FEES_STARTPT,
							DOLLAR_SIGN_LENGTH,
							LEFT,
							LINE_INDI_GENERAL);
						// print the amount of the fee
						buildPrintVector(
							(aaFees.printDollar()).substring(1),
							FEES_ITEM_PRICE_STARTPT,
							FEES_ITEM_PRICE_LENGTH,
							RIGHT,
							LINE_INDI_GENERAL);
						ciHoldFeeInvyPrintLineIndex++;
					}
					else
					{
						lbMatchingFeesItmCd = false;
						if (csHoldTransCd.equals(VOID)
							|| csHoldTransCd.equals(VOIDNC))
						{
							ciTypeOfPrintVector = ADD_TO_PV_IMMED;
						}
						else
						{
							ciTypeOfPrintVector = INVY_NO_FEES;
						}
						// print out the detail line
						appendInvyDetailLineToPrintVector(
							laTRRInventoryData.getItmCdDesc(),
							laTRRInventoryData.getInvItmYr(),
							laTRRInventoryData.getInvItmNo(),
							lbMatchingFeesItmCd);
						ciHoldInvyPrintLineIndex++;
					}
					// If the are associates fee items the key lengh is
					// greater than the base lengh.  Because there is
					// still more of the fee record to print out, the 
					// process Fees method generates the nextline().  
					// This code is used when we are processing invy
					// records through the 
					// processTransReconInventoryDataObject method.
					if (asTransInvyKey.length() == ciInvyKeyLength)
						ciPrintLineIndex++;
					// track the index of the fees object, we can start
					// at that location when we come in the next time
					ciInventoryIndex++;
					cbSingleDashedLineNeeded = true;
					// added break statement to prevent duplicate
					// printing of an inventory item
					// defect 4549 and defect 4618.
					if (!csHoldTransCd.equals(VOID)
						&& !csHoldTransCd.equals(VOIDNC)
						&& ciTypeOfPrintVector != INVY_NO_FEES)
					{
						break;
					}
				} // end if (k == 0)  current key equals key passed in.
				else
				{ // current inventory, which may include itmcd is
					// greater than what we are searching for.
					break;
				}
			} // end  else current key is equal to or greater than the
			// key passed in.
		} // while (ciInventoryIndex < avInventoryDataObject.size())
		//Loop through the results
		csHoldInvyKey =
			lsCurrInventoryKey.substring(0, ciInvyKeyLength);
		return lbMatchingFeesItmCd;
	} // end of primary statement

	/**
	 * Process Transaction Recon Payment Data Object
	 * 
	 * @param avPaymentDataObject Vector
	 * @param asTransPaymentKey String
	 * @throws RTSException
	 */
	public String processTransReconPaymentDataObject(
		Vector avPaymentDataObject,
		String asTransPaymentKey)
		throws RTSException
	{
		String lsCurrPayDataKey = "";
		String lsCustSeqNo = "000000";
		cbChangeDuePrinted = false;
		int k = 0;
		// set up format for sequence number
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		if (!(avPaymentDataObject == null)
			&& (avPaymentDataObject.size() > 0))
		{
			while (ciPaymentIndex < avPaymentDataObject.size())
			{ //Loop through the results
				TransactionReconciliationReportPaymentData laTRRPayData =
					(
						TransactionReconciliationReportPaymentData) avPaymentDataObject
							.elementAt(
						ciPaymentIndex);
				// set sequence number to accomodate six digits.  Need 
				// to have leading zeros for string comparison.	
				lsCustSeqNo =
					laSixDigits.format(laTRRPayData.getCustSeqNo());
				// set up a current pay data key to match to
				// TransactionDataKey
				lsCurrPayDataKey = setUpCurrPaySearchKey(laTRRPayData);
				// if the current payment key is less than the 
				// Transaction Payment key , need to keep reading the 
				// vector until we find a match or current payment key 
				// exceeds the transaction payment key.  Once we have
				// exceed the transaction payment key we have either 
				// processed all the necessary payments or moved beyond 
				// what we are looking for.
				k = lsCurrPayDataKey.compareTo(asTransPaymentKey);
				// If the current payment data key is less than the 
				// transaction payment key, keep reading the vector,
				if (k < 0)
					// current payment key is less than the transaction
					// payment key passed in.
				{
					ciPaymentIndex++;
				}
				else
				{
					// the current payment key either matches or exceeds the
					// transaction payment key passed in.
					if (k == 0) // keys match print the payment type
					{
						// track the index of the payment object, we can
						// start at that location when we come in the
						// next time
						cbPaymentRecordFound = true;
						ciPaymentIndex++;
						if (laTRRPayData
							.getRecordType()
							.equals(CHANGE))
						{
							processChangeDueRecord(laTRRPayData);
						}
						else
						{
							processPaymentRecord(laTRRPayData);
						}
					} // end payment keys match
					else
						// current payment key exceeds the transaction
						// payment key passed in
						{
						// Change due must be printed on the same line 
						// as the first payment amount.  If there is a 
						// payment amount, change due is printed at that
						// time.  However, if there isn't a payment 
						// amount, need to print  out the change due on 
						// the first line.  The change due amount is the
						// first record  read in for that sequence 
						// number and was put in the accumulators.  The 
						// amount in the accumulator will be printed 
						// out.  There will only be one type of change 
						// due per transaction.  It will either be 
						// "Cash Change" or "Check Change".  
						if (!cbChangeDuePrinted)
							// will only have one change due type per 
							// transaction.  Either check or change, but not 
							// both
						{ // have change
							if (!caTotalCashChangeForCustomerSet
								.equals(NEW_DOLLAR))
							{
								appendChangeToPrintVector(
									caTotalCashChangeForCustomerSet,
									csTypeOfChangeDue,
									false,
									"");
								ciPrintLineIndex++;
								cbChangeDuePrinted = true;
							}
							else if (
								!caTotalCheckChangeForCustomerSet
									.equals(
									NEW_DOLLAR))
							{
								appendChangeToPrintVector(
									caTotalCheckChangeForCustomerSet,
									csTypeOfChangeDue,
									false,
									"");
								ciPrintLineIndex++;
								cbChangeDuePrinted = true;
							}
						}
						break;
					}
				} // end of else payment data key is less than trans key 
				//coming in
			} // while (ciPaymentIndex < avPaymentDataObject.size()) 
			//Loop through the results
		} // end if(!(avPaymentDataObject == null) && 
		// (avPaymentDataObject.size() > 0))
		// did not find any records that matched record type of change 
		// or payment for this trans and it is not part of a customer 
		// set added && ciPaymentIndex != 0 to make sure 1st record does
		// not get duplicated.
		if (!cbPaymentRecordFound
			&& caTotalFeesForCustomerSubtotal.equals(NEW_DOLLAR)
			&& ciPaymentIndex != 0)
		{
			appendNoPaymentAmount(HEADER_CASH);
		}
		return lsCurrPayDataKey;
	} // end of primary statement

	/**
	 * The purpose of this program is to process the data for the
	 * transaction reconciliation report.  There are four data objects 
	 * that will be used:TransactionReconciliationReportTransactionData, 
	 * TransactionReconciliationReportData, 
	 * TransactionReconciliationReportFeesData, and 
	 * TransactionReconciliationReportInventoryData.  The SQL returns 
	 * sorted data.
	 *
	 * The Main keys are:
	 * Transaction:  OfcIssuanceNo + TransWsId + TransAMDate + CustSeqNo
	 * 	+ TransTime
	 * Payment: OfcIssuanceNo + TransWsId +  TransAMDate + CustSeqNo 
	 * Fees and Inventory:  OfcIssuanceNo + TransWsId +  TransAMDate 
	 *  + CustSeqNo + TransTime 	 
	 *
	 * Note:  only one cash drawer/employee can be selected at one time.
	 *
	 * Overall processing:
	 * Initialize variables
	 * Instantiate a TransactionReconciliationReportTransactionData obj
	 * Set up a while loop to process all transactions.
	 * The general processing is:
	 *		1) 	start the transactions.
	 *	    	for each new transaction that have different Sequence 
	 *			number, except "VOIDS" & "VOIDSNC"
	 *	    		a)	part of the 1 transaction is put immediately 
	 *					into the print vector
	 *	    		b) 	For printing purposes, we do not move to the 
	 *					next line because the first payment record must 
	 *					be printed on the same line as this transaction.
	 *	    2)	Check for payment records.
	 *	    	only for each new transaction that have different cust 
	 *			seq no and is not a void
	 *	    	a)	if there are multiple transactions with the same 
	 *				CustSeqNo, the Customer Set Titles and payment 
	 *				information	are only printed upon have a new 
	 *				Customer Set number.
	 *			a)	if there are not any associated payment records 
	 *				Payment description of "Cash" and amount of "$ 0.00"
	 *				are forced to print.  (only when it is a new 
	 *				Customer set).
	 *		    b)	The can be two types of associated Payment records -
	 *				PAYMENT and CHANGE.  If there is a payment record of
	 *				type "CHANGE" for a transaction, it will always be 
	 *				listed first.  There can only be one CHANGE record 
	 *				per transaction.
	 *		    	All other payment records will be of type PAYMENT.
	 *		    	1.  If there is CHANGE the description and amount 
	 *					are stored in a variable because it needs to be 
	 *					printed to the right of the first PAYMENT record
	 *					we have	for this transaction, and we have not 
	 *					reached the PAYMENT Record.
	 *		   		2.	Next, we will process the first	PAYMENT record. 
	 *					It can be put into the printVector immediately. 
	 *					The processing of the payment record checks to
	 *		   			see if we have a CHANGE record to be printed, if
	 *					we do, it is added to the printVector.  After 
	 *					this processing, 1 is added to the 
	 *					printlineindex to indicate newline.
	 *		   		3.	The remaining associated payment records are 
	 *					added to the print vector, with a new line 
	 *					insterted after appending the payment amount.
	 *				4.	After payments for the transactions for the 
	 *					transaction are found and put in the print 
	 *					vector, return to processing the remainder of 
	 *					the transaction.
	 *		4)	There is a second line of transaction information to be 
	 *			added to the printVector and a new line is to be 
	 *			inserted.
	 *		5)	There is a third line of transaction information to be 
	 *			added.  DO NOT insert a newline.  Process the associated
	 *			fee and invenotry records for this transaction.
	 *		6)	Processing the fees and inventory is complex because of 
	 *			the print order they must be in	and the way the files 
	 *			are sorted.  The fees and inventory records come over 
	 *			pre-sorted (see the appropriate SQL statements).  There 
	 *			may or may not be fee record(s) for the transaction.  
	 *			And there may or may not be Inventory record(s) for the 
	 *			transaction.  The printed order on the reports is as 
	 *			follows:  Fee records that do not have an assoicated
	 *			Inventory Record, Fee records that have an associated 
	 *			Inventory record. and then the Inventory Records.  
	 *			Within the 3 groups, the data must stay sorted in order 
	 *			of the item description (the records are passed in 
	 *			sorted, so we don't "want to get them out of order"
	 *			within their subsets).
	 *			When each fee record is read, the inventory vector needs
	 *			to be checked for a matching record.
	 *			a)	read a fee record and build the key to be matched to
	 *				the inventory record.
	 *			b)	start to process the inventory record and build the 
	 *				key to be matched to the fee key.
	 *				1.	If the Inventory Key is less than the Fee key, 
	 *					and the inventory item is not part of the 
	 *					transaction. The next inventory record is read.
	 *					This continues until the Inventory transaction 
	 *					key is greater than the Fee transaction key or
	 *					the keys match.  If the Inventory key is 
	 *					greater, we escape from the inventory loop and 
	 *					return to the processFees method.  If the keys 
	 *					match go to step 2.
	 *				2.  If the Inventory Key is less than the Fee key, 
	 *					but the inventory item is part of the 
	 *					transaction.
	 *					The inventory item is put in the holdInvyNoFees
	 *					vector.  This vector has its own newline index 
	 *					and it is incremented by 1 after the item and 
	 *					amount is placed in the holdInvyNoFess vector.
	 *					The processing of the inventory records will 
	 *					continue until we find a match or the inventory 
	 *					key becomes greater than the fees key.  This 
	 *					vector gets	additional elements throughout the 
	 *					processing of the fees vector.  This vector will 
	 *					be appended to the print vector after the 
	 *					HoldFeesWithInvy vector has been appended to the
	 *					print vector.
	 *				3.  If the Inventory key equals the Fee key.  The 
	 *					fees item description (which was passed in), the 
	 *					inventory itm cd, the inventory itm amount, and 
	 *					the fees item amount (which was passed in) are 
	 *					put into the HoldFeesWithInvy vector, and the 
	 *					HoldFeesWithInvy newline indicator must be 
	 *					incremented by one.  Return	to processFees.
	 *			c) 	If there weren't any associated inventory found for 
	 *				this fee record, append the	item description and 
	 *				amount to the print vector, immediately.  Increment 
	 *				the newline	index for the vector by 1.
	 *			d)	Continue processing the fees vector until all the 
	 *				fees for this transaction have been processed.  
	 *				Return to processTransRecon.
	 *		7.	If there are any records in the HoldInvyWithFees vector,
	 *			they will be appended to the print vector at this time.
	 *		8.  If there are any records in the HoldInvyNoFees vector, 
	 *			they will be appended to the print vector at this time.
	 *		9.	There may be more records in the inventory vector that 
	 *			are part of the transaction, but their key is greater 
	 *			than what was processed in the processFees method.  
	 *			These inventory records need to be captured.  The 
	 *			processInventory method is invoked and the matching 
	 *			items are appended to the print vector immediately.  The
	 *			newline indicator must be incremented by one after each 
	 *			inventory transaction that is appended
	 *		10.	It's time to print out the print vector.  There is a 
	 *			check to see if the entire print vector will fit on the 
	 *			space that remains on the page.  If not, we go to the 
	 *			next page.
	 *
	 *	Detailed information:
	 *		1.  The Customer Set: x (MM/DD/YYYY) is only printed for the
	 *			first transaction of the Customer Set.  If there are 
	 *			multiple transactions with in the customer set, the
	 *			remaining transactions do not have the Customer Set 
	 *			heading, and when there is a change in transaction time 
	 *			a customer subtotal is printed out.  Except when there 
	 *			is a inventory void.  The TransactionId, workstation ID,
	 *			 and trans emp id are printed on subsequent lines.	
	 *		2.  If there is a primary split (Cash Drawer/Emp or 
	 *			Emp/Cashdrawer), and there is only one individual item 
	 *			in the primary split (e.g Cash Drawer/Emp and the 
	 *			returned results only have one employee), a summary 
	 *			total line is not printed.  Only, the report total line 
	 *			will be printed.
	 * 
	 * @param avResults Vector[]
	 */
	public void processTransReconTransactionData(Vector[] avResults)
		throws RTSException
	{
		int k = 0;
		int liPrimarySplitCount = 0;
		String lsCurrTransactionDataKey = null;

		// defect 9049
		int liTransCount = 0;
		String lsNextSeqNum;
		//String lsCustomerSet;
		// end defect 9049 

		String lsHoldTransactionDataKey = null;
		String lsHoldPayKey = "0";
		String lsHoldFeesKey = "0";
		String lsCurrTransPayKey, lsCurrTransFeeKey, lsCurrTransInvyKey;

		String lsTransTime, lsSeqNum;
		String lsHoldSeqNo = "000000";
		String[] ls = new String[2];
		String lsHoldSummaryID = null;
		// set up a field to determine if we will need to have subtotals
		// printed out
		boolean lbNeedCustomerSubtotal = false;
		// capture the size of the vectors.
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		// set up vectors for the four type of objects that are returned
		// to us
		Vector lvTRRTransaction = avResults[0];
		Vector lvTRRPayment = avResults[1];
		Vector lvTRRFees = avResults[2];
		Vector lvTRRInventory = avResults[3];
		// set up a field to convert TransAMDate to RTSDate so it is
		// displayed in format "(MM/DD/YYYY)" on the report	
		RTSDate laRTSDateTransAM = new RTSDate();
		// instantiate a new object for the TransactionData object
		TransactionReconciliationReportTransactionData laTRRTransData =
			new TransactionReconciliationReportTransactionData();

		int i = 0;
		// Loop through the results
		while (i < lvTRRTransaction.size())
		{
			// get the current record
			laTRRTransData =
				(
					TransactionReconciliationReportTransactionData) lvTRRTransaction
						.elementAt(
					i);

			// defect 9049 
			// get next record if available 
			if (i < lvTRRTransaction.size() - 1)
			{
				TransactionReconciliationReportTransactionData laNextTRRTransData =
					(
						TransactionReconciliationReportTransactionData) lvTRRTransaction
							.elementAt(
						i + 1);
				lsNextSeqNum =
					laSixDigits.format(
						laNextTRRTransData.getCustSeqNo());
			}
			else
			{
				lsNextSeqNum = null;
			}
			// end defect 9049

			// format trans time and sequence number with leading zeros,
			// Need to have leading zeros for string comparison.	
			lsTransTime =
				laSixDigits.format(laTRRTransData.getTransTime());
			lsSeqNum =
				laSixDigits.format(laTRRTransData.getCustSeqNo());
			// reset the print line indexes for the hold vectors
			ciHoldInvyPrintLineIndex = 0;
			ciHoldFeeInvyPrintLineIndex = 0;
			// must clear on the vectors of any previous data.
			// this is done with each new transaction
			cvHoldFeesWithInvy.clear();
			cvHoldInvy.clear();
			ciTypeOfPrintVector = ADD_TO_PV_IMMED;
			// initialize 
			cbFeesOrInvyRecordFound = false;
			// if the sequence number on the prior record does not equal
			// the current record's sequence number reset the hold trans
			// code so the proper trans code detail prints out.  This is 
			// used when processing voids.

			// defect 9049 
			// Keep counters within transaction set 
			if (Integer.parseInt(lsHoldSeqNo)
				!= Integer.parseInt(lsSeqNum))
			{
				csHoldTransCd = "";
				liTransCount = 0;
			}
			else if (
				csHoldTransCd.equals(VOID)
					&& laTRRTransData.getTransCd().equals(VOID)
					|| (csHoldTransCd.equals(VOIDNC)
						&& laTRRTransData.getTransCd().equals(VOIDNC)))
			{
				csHoldTransCd = "";
			}
			liTransCount++;
			// end defect 9049

			// set lsTransactionDataKey with the fields from the current
			// record.  do not include transtime, because the payment 
			// records do have have an associated timestamp.
			lsCurrTransactionDataKey =
				String.valueOf(laTRRTransData.getOfcIssuanceNo())
					+ String.valueOf(laTRRTransData.getTransWsId())
					+ String.valueOf(laTRRTransData.getTransAMDate())
					+ lsSeqNum
					+ String.valueOf(laTRRTransData.getCashWsId())
					+ padEmpId(laTRRTransData.getTransEmpId());
			// initialize the length of our key without transtime or an 
			// item code. this length will determine what the "core" key
			// is.  When we process inventory, we look at the core key
			// to find out if the inventory record is part of the 
			// transaction, but does not have a matching fee key.	
			int liTransKeyBaseLength =
				lsCurrTransactionDataKey.length();
			// append the time, so we can tell if we are withing the 
			// same customer set, but working the next transaction.
			lsCurrTransactionDataKey += lsTransTime;
			// set up a Transaction Search keys for Payment, Fees, and 
			// Inventory vectors.
			ls = setUpCurrTransSearchKeys(laTRRTransData);
			lsCurrTransPayKey = ls[0];
			lsCurrTransFeeKey = ls[1];
			lsCurrTransInvyKey = lsCurrTransFeeKey;
			// same as fee, but used for clarity within the methods
			// transaction key information, including time, does not 
			// match
			if ((lsHoldTransactionDataKey == null)
				|| (!(lsCurrTransactionDataKey
					.equals(lsHoldTransactionDataKey))))
			{
				// used for the result of compareTo
				int p = 0;
				if (lsHoldTransactionDataKey == null)
					// nothing to compare
				{
					p--;
				}
				// hold transaction data key has a value
				else
				{
					// get the transaction key without the transtime 
					// appended to it	
					String lsTemp =
						lsCurrTransactionDataKey.substring(
							0,
							liTransKeyBaseLength);
					// Use it to see if we are within the same customer 
					// set but a different transaction.
					p = lsTemp.compareTo(lsHoldTransactionDataKey);

					// same customer set but different transaction
					if (p == 0)
					{
						// defect 9049
						// Add restrictions that must either be 
						//  - Only 2 transactions in trans set 
						//      VOID/VOIDNC && INVVD
						//  - Not the last VOID/VOIDNC && INVVD set 
						if ((csHoldTransCd.equals(VOID)
							|| csHoldTransCd.equals(VOIDNC))
							&& laTRRTransData.getTransCd().equals(INVVD)
							&& (liTransCount == 2
								|| (lsNextSeqNum != null
									&& lsNextSeqNum.equals(lsSeqNum))))
							// end defect 9049 
						{
							lbNeedCustomerSubtotal = false;
						}
						else
						{
							lbNeedCustomerSubtotal = true;
						}
						if (!csHoldTransCd.equals(VOID)
							&& !csHoldTransCd.equals(VOIDNC))
						{
							appendCustomerSubtotals();
							printTransaction();
							this.caRpt.nextLine();
						}
					}

				}
				// Different Customer Sequence Number 
				if (p != 0) // does not match previous record.
				{
					if (i != 0) // not first record
					{
						if (lbNeedCustomerSubtotal)
						{
							appendCustomerSubtotals();
						}
						appendCustomerSetTotalsToPrintVector(lsHoldSeqNo);
						printTransaction();
						cbPaymentRecordFound = false;
						lbNeedCustomerSubtotal = false;
						this.caRpt.nextLine();
						this.caRpt.nextLine();
					}
					// convert TransAMDate to RTSDate
					laRTSDateTransAM =
						new RTSDate(
							AM_DATE,
							laTRRTransData.getTransAMDate());
					// we had print out, so reset the flag
					cbPayFeesInvyDetail = false;
					lbNeedCustomerSubtotal = false;
					// print the customer set label
					ciPrintLineIndex = 1;
					// start counting the print lines needed
					// if there is a primary split we need to have a 
					// "Summary for . . . " heading
					if (ciTypeOfBreak == CASH_DRAWER_EMP
						|| ciTypeOfBreak == EMP_CASH_DRAWER)
					{
						if (lsHoldSummaryID == null)
						{
							setSummaryForText(laTRRTransData);
							appendSummaryHeader();
							lsHoldSummaryID = csHoldSummaryId;
						}
						else
						{
							if (!(lsHoldSummaryID
								.equals(
									laTRRTransData.getTransEmpId())))
							{
								appendPrimaryTotalsToPrintVector();
								// defect 9091 
								// Removed added space prior to label 
								buildPrintVector(
									"",
									0,
									0,
									LEFT,
									LINE_INDI_GENERAL);
								// end defect 9091 
								printTransaction();
								resetAccumulatorsPrimarySplit();
								setSummaryForText(laTRRTransData);
								appendSummaryHeader();
								lsHoldSummaryID = csHoldSummaryId;
								liPrimarySplitCount++;
							}
						}
					}
					else
					{
						resetAccumulatorsPrimarySplit();
					}
					appendCustomerSetHeadingToPrintVector(
						String.valueOf(laTRRTransData.getCustSeqNo()),
						laRTSDateTransAM);
				}
				// set the hold keys;
				lsHoldTransactionDataKey =
					lsCurrTransactionDataKey.substring(
						0,
						liTransKeyBaseLength);
				lsHoldSeqNo =
					String.valueOf(laTRRTransData.getCustSeqNo());
			}
			//--------------------  Process Payment  -------------------
			// added code to print out INET CHARGE as the payment 
			// description when an INET transaction has been voided.  
			// SQL and TransData oject were modified	
			if (laTRRTransData.getTransCd().equals(VOIDNC)
				&& laTRRTransData.getVoidTransCd().equals(IRENEW))
			{
				ciTypeOfPrintVector = ADD_TO_PV_IMMED;
				appendNoPaymentAmount(INET_CHRG);
			}
			else
			{
				// check to see if we need to process payment records.  
				// If the current key is >= what is on hold, check the 
				// file
				k = lsCurrTransPayKey.compareTo(lsHoldPayKey);
				// the current key is less than what is being held,  go 
				// look at the file if there are recs and we haven't 
				// reached the end of the file
				if (k >= 0)
				{
					if ((lvTRRPayment != null
						&& lvTRRPayment.size() > 0)
						&& (ciPaymentIndex < lvTRRPayment.size()))
					{
						lsHoldPayKey =
							processTransReconPaymentDataObject(
								lvTRRPayment,
								lsCurrTransPayKey);
					}
				}
				if ((!(cbPaymentRecordFound))
					&& (!csHoldTransCd.equals(VOIDNC)
						&& !csHoldTransCd.equals(VOID)))
				{
					ciTypeOfPrintVector = ADD_TO_PV_IMMED;
					// print the payment type descriptions
					if (laTRRTransData.getTransCd().equals(IRENEW))
					{
						appendNoPaymentAmount(INET_CHRG);
					}
					else
					{
						appendNoPaymentAmount(HEADER_CASH);
					}
				}
			}
			//--------- print the 2 remaining lines for that transaction
			appendCustomerSetTransactionsToPrintVector(
				laTRRTransData,
				lsTransTime,
				csHoldTransCd);
			//--------------------------------------------  Process Fees
			// check to see if we need to process Fees records.  If the 
			// current key is >= what is on hold, check the file
			k = lsCurrTransFeeKey.compareTo(lsHoldFeesKey);
			// the current key is less than what is being held,  go look
			// at the file if there are recs and we haven't reached the 
			// end of the file
			if ((k >= 0)
				&& (lvTRRFees != null && lvTRRFees.size() > 0)
				&& (ciFeesIndex <= lvTRRFees.size()))
			{
				lsHoldFeesKey =
					processTransReconFeesDataObject(
						lvTRRFees,
						lvTRRInventory,
						lsCurrTransFeeKey);
			}
			// Inventory items that don't have a matching fees key are 
			// always printed after all fees have been processed.
			// That is what we may have a hold vector.  print inventory 
			// records that were captured while processing fees
			if (cvHoldFeesWithInvy != null
				&& cvHoldFeesWithInvy.size() > 0)
			{
				int j = 0;
				int liHoldLineNumber = 0;
				ciTypeOfPrintVector = ADD_TO_PV_IMMED;
				while (j < cvHoldFeesWithInvy.size())
				{
					PrintVector laPrintVector =
						(PrintVector) cvHoldFeesWithInvy.elementAt(j);
					if (j == 0)
					{
						ciPrintLineIndex++;
						liHoldLineNumber =
							laPrintVector.getLineNumber();
					}
					else
					{
						if (laPrintVector.getLineNumber()
							!= liHoldLineNumber)
						{
							ciPrintLineIndex++;
							liHoldLineNumber =
								laPrintVector.getLineNumber();
						}
					}
					buildPrintVector(
						laPrintVector.getDesc(),
						laPrintVector.getStartPoint(),
						laPrintVector.getLength(),
						laPrintVector.getJustification(),
						laPrintVector.getTypeOfLineIndi());
					j++;
				}
				ciPrintLineIndex++;
			}
			// print inventory records that were captured while that 
			// have no fee associated
			if (cvHoldInvy != null && cvHoldInvy.size() > 0)
			{
				ciTypeOfPrintVector = ADD_TO_PV_IMMED;
				processHoldInventoryVector();
				cvHoldInvy.clear();
				// reset the hold inventory vector		
				ciPrintLineIndex++;
			}
			//---------------------------------------  Process Inventory			
			// The inventory file is also processed within fees.  We 
			// need to see if there are any Invy records that were not
			// processed within the fees items.  For examle an itmcd 
			// descripton of "Passenger Plate" has an inventory code
			// but no associated fee.
			k = lsCurrTransInvyKey.compareTo(csHoldInvyKey);
			// the current key is >=  than what is being held, and there 
			// any invy items,  and we haven't reached the end of the 
			// file go and check for remaining Invy items.  Using only 
			// the core key.
			if ((k >= 0)
				&& (lvTRRInventory != null && lvTRRInventory.size() > 0)
				&& (ciInventoryIndex < lvTRRInventory.size()))
			{
				processTransReconInventoryDataObject(
					lvTRRInventory,
					lsCurrTransInvyKey,
					NEW_DOLLAR,
					"");
			}
			if (cbFeesOrInvyRecordFound == false)
			{
				ciPrintLineIndex++;
			}
			else
			{
				ciTypeOfPrintVector = ADD_TO_PV_IMMED;
				processHoldInventoryVector();
				cvHoldInvy.clear(); // reset the hold inventory vector
				ciPrintLineIndex++;
			}
			csHoldTransCd = laTRRTransData.getTransCd();
			i++;
		}
		// need to print subtotal lines (if any) when we are done 
		// processing
		if (lbNeedCustomerSubtotal)
		{
			appendCustomerSubtotals();
			//	this.cRpt.nextLine();
		}
		appendCustomerSetTotalsToPrintVector(lsHoldSeqNo);
		if (lsHoldSummaryID != null && liPrimarySplitCount > 0)
		{
			appendPrimaryTotalsToPrintVector();
			buildPrintVector("", 1, 1, LEFT, LINE_INDI_GENERAL);
		}
		// print the report totals and associated lines at the end of 
		// the report
		appendReportTotalsToPrintVector();
		printTransaction();
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvdataLine = new Vector();
		Vector myDataline = new Vector();
		Dollar lPymntTypeAmtDollar = new Dollar("100.00");
		TransactionReconciliationReportTransactionData laTRRTransactionData =
			new TransactionReconciliationReportTransactionData();
		laTRRTransactionData =
			new TransactionReconciliationReportTransactionData();
		laTRRTransactionData.setTransAMDate(321456);
		laTRRTransactionData.setCustSeqNo(1);
		laTRRTransactionData.setCashWsId(100);
		laTRRTransactionData.setTransTime(112231);
		laTRRTransactionData.setTransEmpId("BUGS BUNNY");
		laTRRTransactionData.setTransCdDesc(
			"XXXXXXX- PERMANENT DESABLED PLACARD RECEIPT");
		laTRRTransactionData.setCustName1("KERMIT T. FROG");
		laTRRTransactionData.setCustSeqNo(1);
		laTRRTransactionData.setTransCdDesc("1611037192092558");
		lvdataLine.addElement(laTRRTransactionData);
		return lvdataLine;
	}

	/**
	 * Query Data Vectors
	 * 
	 * @param asQuery String
	 * @return Vector[]
	 * @throws RTSException
	 */
	public Vector[] queryDataVectors(String asQuery)
		throws RTSException
	{
		return null;
	}

	/**
	 * Reset Accumulators Customer Set
	 * 
	 * @throws RTSException
	 */
	public void resetAccumulatorsCustomerSet() throws RTSException
	{
		caTotalFeesForCustomerSubtotal = NEW_DOLLAR;
		caTotalCashChangeForCustomerSet = NEW_DOLLAR;
		caTotalCheckChangeForCustomerSet = NEW_DOLLAR;
		caTotalFeesForCustomerSet = NEW_DOLLAR;
		caTotalPaymentForCustomerSet = NEW_DOLLAR;
	}

	/**
	 * Reset Accumulators Primary Split
	 * 
	 * @throws RTSException
	 */
	public void resetAccumulatorsPrimarySplit() throws RTSException
	{
		// reset dollar fields to accumulate totals based on a primary split
		caTotalPaymentForPrimarySplit = NEW_DOLLAR;
		caTotalCashChangeForPrimarySplit = NEW_DOLLAR;
		caTotalCheckChangeForPrimarySplit = NEW_DOLLAR;
		caTotalFeesForPrimarySplit = NEW_DOLLAR;
	}

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
			case FundsConstant.DATE_RANGE : // 5231
				{
					// empty code block
				}
			case FundsConstant.SINCE_CLOSE :
				{
					// empty code block
				}
			case FundsConstant.SINCE_CURRENT :
				{
					// empty code block
				}
			case FundsConstant.AFTER_SUBSTATION :
				{
					// empty code block
				}
			case FundsConstant.LAST_CLOSE : //  Last CloseOut 
				{
					// empty code block
				}
			case FundsConstant.CURRENT_STATUS :
				{
					// empty code block
				}
			case FundsConstant.CLOSE_OUT_FOR_DAY :
				{
					// empty code block
				}
			case 7 : // substation
				{
					// empty code block
				}
			case 8 : // county_wide
				{
					this.caRptProps.setUniqueName(
						ReportConstant.TRANSACTION_RECON_REPORT_ID);
					break;
				}
			default :
				{
					// rts exception for report error
					break;
				}
		}
	}

	/**
	 * Set Summary For Text
	 * 
	 * @param aaTRRdata TransactionReconciliationReportTransactionData
	 */
	public void setSummaryForText(TransactionReconciliationReportTransactionData aaTRRdata)
	{
		switch (ciTypeOfBreak)
		{
			case EMP_CASH_DRAWER :
				{
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_CASH_DRAWER
							+ ": "
							+ Integer.toString(aaTRRdata.getTransWsId());
					csTotalFor =
						CASH_DRAWER
							+ " "
							+ Integer.toString(aaTRRdata.getTransWsId())
							+ " "
							+ TOTAL
							+ ":";
					csHoldSummaryId =
						Integer.toString(aaTRRdata.getTransWsId());
					break;
				}
			case CASH_DRAWER_EMP :
				{
					csSummaryFor =
						ReportConstant.SUMMARY_FOR_EMPLOYEE.trim()
							+ ": "
							+ aaTRRdata.getTransEmpId();
					csTotalFor =
						EMPLOYEE
							+ " "
							+ aaTRRdata.getTransEmpId()
							+ " "
							+ TOTAL
							+ ":";
					csHoldSummaryId = aaTRRdata.getTransEmpId();
					break;
				}
			default :
				{
					// empty code block
				}
		}
	}

	/**
	 * setUpBreakByCode
	 * 
	 * @param aaFundsData FundsData
	 * @return int
	 * @throws RTSException
	 */
	public int setUpBreakByCode(FundsData aaFundsData)
		throws RTSException
	{
		// add additional main headings
		switch (aaFundsData.getFundsReportData().getEntity())
		{
			case FundsConstant.CASH_DRAWER :
				{
					switch (aaFundsData
						.getFundsReportData()
						.getPrimarySplit())
					{
						case FundsConstant.NONE :
							{
								return CASH_DRAWER_NONE;
							}
						case FundsConstant.EMPLOYEE :
							{
								return CASH_DRAWER_EMP;
							}
						default :
							{
								// error message
								return 0;
							}
					} // end primary split for entity cash drawer 	
				} // end of entity cash drawer
			case FundsConstant.EMPLOYEE :
				{
					switch (aaFundsData
						.getFundsReportData()
						.getPrimarySplit())
					{
						case FundsConstant.NONE :
							{
								return EMP_NONE;
							}
						case FundsConstant.CASH_DRAWER :
							{
								return EMP_CASH_DRAWER;
							}
						default :
							{
								// error message
								return 0;
							}
					} // end primary split for entity Employee
				} // end of entity Employee
		} // end of switch of for all entities
		return 0;
	}

	/**
	 * Set Up Current Fees Search Key
	 * 
	 * @param aaTransReconFeesData
	 *  TransactionReconciliationReportFeesData
	 * @return String
	 * @throws RTSException
	 */
	public String setUpCurrFeesSearchKey(TransactionReconciliationReportFeesData aaTransReconFeesData)
		throws RTSException
	{
		String lsKey = "";
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		String lsTransTime = "000000";
		String lsCustSeqNo = "000000";
		// format trans time and sequence number with leading zeros,
		// Need to have leading zeros for string comparison.	
		lsTransTime =
			laSixDigits.format(aaTransReconFeesData.getTransTime());
		// format the cust sequence number with leading zeros.
		lsCustSeqNo =
			laSixDigits.format(aaTransReconFeesData.getCustSeqNo());
		//defect 4737
		String lsCoreKey =
			String.valueOf(aaTransReconFeesData.getOfcIssuanceNo())
				+ String.valueOf(
					laThreeDigits.format(
						aaTransReconFeesData.getTransWsId()))
				+ String.valueOf(aaTransReconFeesData.getTransAMDate())
				+ lsCustSeqNo
				+ lsTransTime;
		switch (ciTypeOfBreak)
		{
			case CASH_DRAWER_NONE :
				{
					// set the fee lsKey
					// defect 4737
					lsKey =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconFeesData.getCashWsId()))
							+ lsCoreKey;
					break;
				}
			case CASH_DRAWER_EMP :
				{
					// set the fee/Inventory lsKey
					// defect 4737
					// defect 7417
					// Pad EmpId to 7 characters
					lsKey =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconFeesData.getCashWsId()))
						// + String.valueOf(aTransReconFeesData.getTransEmpId())
	+padEmpId(aaTransReconFeesData.getTransEmpId()) + lsCoreKey;
					// end defect 7417
					break;
				}
			case EMP_NONE :
				{
					// set the fee/Inventory lsKey
					// defect 7417
					// pad EmpId to 7 characters 
					// lsKey = String.valueOf(
					// aTransReconFeesData.getTransEmpId())
					lsKey =
						padEmpId(aaTransReconFeesData.getTransEmpId())
							+ lsCoreKey;
					// end defect 7417	
					break;
				}
			case EMP_CASH_DRAWER :
				{
					// set the fee/Inventory lsKey
					// defect 4737change
					// defect 7417
					// Pad EmpId to 7 characters 
					// lsKey = String.valueOf(aTransReconFeesData
					// .getTransEmpId())
					lsKey =
						padEmpId(aaTransReconFeesData.getTransEmpId())
							+ String.valueOf(
								laThreeDigits.format(
									aaTransReconFeesData
										.getCashWsId()))
							+ lsCoreKey;
					// end defect 7417 	
				}
		}
		return lsKey;
	}

	/**
	 * Set Up Current Inventory Search Key
	 * 
	 * @param aaTransReconInvyData
	 * 	TransactionReconciliationReportInventoryData
	 * @return String
	 * @throws RTSException
	 */
	public String setUpCurrInvySearchKey(TransactionReconciliationReportInventoryData aaTransReconInvyData)
		throws RTSException
	{
		String lsKey = "";
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		String lsTransTime = "000000";
		String lsCustSeqNo = "000000";
		// format trans time and sequence number with leading zeros,
		// Need to have leading zeros for string comparison.	
		lsTransTime =
			laSixDigits.format(aaTransReconInvyData.getTransTime());
		lsCustSeqNo =
			laSixDigits.format(aaTransReconInvyData.getCustSeqNo());
		//defect 4737change
		String lsCoreKey =
			String.valueOf(aaTransReconInvyData.getOfcIssuanceNo())
				+ String.valueOf(
					laThreeDigits.format(
						aaTransReconInvyData.getTransWsId()))
				+ String.valueOf(aaTransReconInvyData.getTransAMDate())
				+ lsCustSeqNo;
		switch (ciTypeOfBreak)
		{
			case CASH_DRAWER_NONE :
				{
					// set the fee lsKey
					//defect 4737
					lsKey =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconInvyData.getCashWsId()))
							+ lsCoreKey
							+ lsTransTime;
					break;
				}
			case CASH_DRAWER_EMP :
				{
					// set the fee/Inventory lsKey
					// defect 4737
					// defect 7417
					// pad EmpId to 7 characters 
					lsKey =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconInvyData.getCashWsId()))
						// + String.valueOf(aTransReconInvyData.getTransEmpId())
	+padEmpId(aaTransReconInvyData.getTransEmpId())
		+ lsCoreKey
		+ lsTransTime;
					// end defect 7471 		
					break;
				}
			case EMP_NONE :
				{
					// set the fee/Inventory lsKey
					// defect 7417
					// lsKey =	String.valueOf(aTransReconInvyData.getTransEmpId())
					lsKey =
						padEmpId(aaTransReconInvyData.getTransEmpId())
							+ lsCoreKey
							+ lsTransTime;
					// end defect 7417		
					break;
				}
			case EMP_CASH_DRAWER :
				{
					// set the fee/Inventory lsKey
					// defect 4737
					// defect 7417
					// lsKey =
					// String.valueOf(aTransReconInvyData.getTransEmpId())
					lsKey =
						padEmpId(aaTransReconInvyData.getTransEmpId())
							+ String.valueOf(
								laThreeDigits.format(
									aaTransReconInvyData
										.getCashWsId()))
							+ lsCoreKey
							+ lsTransTime;
				}
		}
		return lsKey;
	}

	/**
	 * Set Up Current Pay Search Key
	 * 
	 * @param aaTransReconPaymentData TransactionReconciliationReportPaymentData
	 * @return String
	 * @throws RTSException
	 */
	public String setUpCurrPaySearchKey(TransactionReconciliationReportPaymentData aaTransReconPaymentData)
		throws RTSException
	{
		String lsHoldSeqNo = "000000";
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		String lsKey = "";
		// format trans time and sequence number with leading zeros,
		// Need to have leading zeros for string comparison.	
		lsHoldSeqNo =
			laSixDigits.format(aaTransReconPaymentData.getCustSeqNo());
		//defect 4737change
		String lsCoreKey =
			String.valueOf(aaTransReconPaymentData.getOfcIssuanceNo())
				+ String.valueOf(
					laThreeDigits.format(
						aaTransReconPaymentData.getTransWsId()))
				+ String.valueOf(
					aaTransReconPaymentData.getTransAMDate());
		switch (ciTypeOfBreak)
		{
			case CASH_DRAWER_NONE :
				{
					// set the payment key with only the ws before the 
					// core value
					// defect 4737
					lsKey =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconPaymentData.getCashWsId()))
							+ lsCoreKey
							+ lsHoldSeqNo;
					break;
				}
			case CASH_DRAWER_EMP :
				{
					// set the payment key with only the ws before the core
					// value
					// defect 4737 
					// defect 7417
					// Pad Employee Id to 7 characters 
					lsKey =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconPaymentData.getCashWsId()))
							+ padEmpId(
								aaTransReconPaymentData
									.getTransEmpId())
						// + String.valueOf(aTransReconPaymentData
		// .getTransEmpId())
	+lsCoreKey + lsHoldSeqNo;
					// end defect 7417
					break;
				}
			case EMP_NONE :
				{
					// set the payment key with only the empid before the 
					// core value
					// defect 7417
					// Pad empId to 7 characters
					// lsKey = String.valueOf(aTransReconPaymentData
					// .getTransEmpId())
					lsKey =
						padEmpId(
							aaTransReconPaymentData.getTransEmpId())
							+ lsCoreKey
							+ lsHoldSeqNo;
					// end defect 7417 
					break;
				}
			case EMP_CASH_DRAWER :
				{
					// set the payment key with only the ws before the core
					// value
					// defect 4737 change
					// defect 7471
					// Pad EmpId to 7 characters
					// lsKey = String.valueOf(
					// aTransReconPaymentData.getTransEmpId())
					lsKey =
						padEmpId(
							aaTransReconPaymentData.getTransEmpId())
							+ String.valueOf(
								laThreeDigits.format(
									aaTransReconPaymentData
										.getCashWsId()))
							+ lsCoreKey
							+ lsHoldSeqNo;
					// end defect 7471 	
				}
		}
		return lsKey;
	}

	/**
	 * Setup the Current Transaction Search Keys 
	 * 
	 * @param aaTransReconReportData
	 *  TransactionReconciliationReportTransactionData
	 * @return String[]
	 * @throws RTSException
	 */
	public String[] setUpCurrTransSearchKeys(TransactionReconciliationReportTransactionData aaTransReconReportData)
		throws RTSException
	{
		String[] lsKey = new String[2];
		String lsHoldSeqNo = "000000";
		DecimalFormat laSixDigits = new DecimalFormat("000000");
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		String lsTransTime = "000000";
		// format trans time and sequence number with leading zeros,
		// Need to have leading zeros for string comparison.	
		lsTransTime =
			laSixDigits.format(aaTransReconReportData.getTransTime());
		lsHoldSeqNo =
			laSixDigits.format(aaTransReconReportData.getCustSeqNo());
		//defect 4737 	
		String lsCoreKey =
			String.valueOf(aaTransReconReportData.getOfcIssuanceNo())
				+ String.valueOf(
					laThreeDigits.format(
						aaTransReconReportData.getTransWsId()))
				+ String.valueOf(aaTransReconReportData.getTransAMDate())
				+ lsHoldSeqNo;
		switch (ciTypeOfBreak)
		{
			case CASH_DRAWER_NONE :
				{
					// set the payment lsKey with only the ws before the 
					// core value
					// defect 4737
					lsKey[0] =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconReportData.getCashWsId()))
							+ lsCoreKey;
					// set the fee lsKey
					// defect 4737
					lsKey[1] =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconReportData.getCashWsId()))
							+ lsCoreKey
							+ lsTransTime;
					break;
				}
			case CASH_DRAWER_EMP :
				{
					// set the payment lsKey with only the ws before the
					// core value
					// defect 4737
					// defect 7417
					// Pad EmpId to 7 characters
					lsKey[0] =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconReportData.getCashWsId()))
						// + String.valueOf(aTransReconReportData
		//	.getTransEmpId())
	+padEmpId(aaTransReconReportData.getTransEmpId()) + lsCoreKey;
					// end defect 7417
					// set the fee/Inventory lsKey
					// defect 4737
					// defect 7417
					lsKey[1] =
						String.valueOf(
							laThreeDigits.format(
								aaTransReconReportData.getCashWsId()))
						// + String.valueOf(aTransReconReportData
		// .getTransEmpId())
	+padEmpId(aaTransReconReportData.getTransEmpId())
		+ lsCoreKey
		+ lsTransTime;
					// end defect 7417
					break;
				}
			case EMP_NONE :
				{
					// set the payment lsKey with only the empid before the
					// core value
					// defect 7417
					// lsKey[0] = String.valueOf(aTransReconReportData
					// .getTransEmpId())	
					lsKey[0] =
						padEmpId(
							aaTransReconReportData.getTransEmpId())
							+ lsCoreKey;
					// set the fee/Inventory lsKey
					// lsKey[1] = String.valueOf(aTransReconReportData
					// .getTransEmpId())
					lsKey[1] =
						padEmpId(
							aaTransReconReportData.getTransEmpId())
							+ lsCoreKey
							+ lsTransTime;
					// end defect 7417 	
					break;
				}
			case EMP_CASH_DRAWER :
				{
					// set the payment lsKey with only the ws before the
					// core value
					// defect 4737
					// defect 7417
					// lsKey[0] = String.valueOf(aTransReconReportData
					// .getTransEmpId())
					lsKey[0] =
						padEmpId(
							aaTransReconReportData.getTransEmpId())
							+ String.valueOf(
								laThreeDigits.format(
									aaTransReconReportData
										.getCashWsId()))
							+ lsCoreKey;
					// set the fee/Inventory lsKey
					// defect 4737change
					// lsKey[1] = String.valueOf(aTransReconReportData
					// .getTransEmpId())
					lsKey[1] =
						padEmpId(
							aaTransReconReportData.getTransEmpId())
							+ String.valueOf(
								laThreeDigits.format(
									aaTransReconReportData
										.getCashWsId()))
							+ lsCoreKey
							+ lsTransTime;
					// end defect 7417
				}
		}
		ciInvyKeyLength = lsKey[1].length();
		return lsKey;
	}
}
