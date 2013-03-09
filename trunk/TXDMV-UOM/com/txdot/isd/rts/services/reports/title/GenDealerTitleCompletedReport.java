package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.CreditCardFeesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.*;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * GenDealerTitleCompletedReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue  		09/19/2001	Added comments
 * J Rue		06/04/2002	Modify totalRejectedTitles() to 
 *							check for diskette entry and 
 *							generateBodyRpt()
 *							to not print "SKIP" if no more records, 
 *							Keyboard
 *							defect 4206 
 * J Rue		06/20/2002	align dealer total fees with 
 *							transaction fees, 
 *							method totalsDlrCompletedRpt()
 *							defect 4248
 * J Rue		07/09/2002	Align the dealer fees on dealer 
 *							completed report
 *							modify totalsDlrCompletedRpt()
 *							defect 4430  
 * J Rue		07/09/2002	call isItmBreak in 
 *							GenDealerTitlePreliminaryReport for 
 *							boolean return of  printing the 
 *							SKIP message. 
 *							modify generateBodyRpt().
 *							defect 4436
 * J Rue		07/25/2002	Align dealer fees from keyboard entry. 
 *							modify printDealerFees()
 *							defect 4430
 * S Govindappa	08/05/2002	Added PCR25 changes in printPymntMethod
 *							to display the credit card fee included 
 *							in the Dealer Title Completed Report.
 *							defect 4483
 * J Rue 		10/2/2002	Remove the condition for 'NOT KEYBOARD', 
 *							print "REJECTED" remark. 
 *							modify generateBodyRpt()
 *							defect 4802, 
 * J Rue		11/05/2002	Modify loop for rejected transactions.
 *							modify generateBodyRpt()
 *							defect 5026
 * J Rue		08/22/2003	Display all DlrFees for report.
 *							modify totalsDlrCompleteedReport(), 
 *							accumulatedDlrFees()
 *							defect 6506 Ver 5.1.5
 * J Rue		09/10/2003	Replace cTotalDealerFees 
 *							(does not include rejected trans) with 
 *							cdDlrFees.
 *							modify calDlrRTSdiff(), printFeesDiffHdr()
 *							defect 6549 Ver 5.1.5 
 * J Rue		09/12/2003	Format javaFile/javaDoc comments
 * J Rue		09/08/2004	Do not display keyboard trans rejected 
 *							unless a form31 number was entered
 *							modify printInvIssued(), generateBodyRpt
 *							defect 7528 Ver 5.2.1
 * K Harrell	12/16/2004	JavaDoc/Formatting/Variable Name Cleanup
 *							to match DealerTitleData Cleanup
 *							defect 7736 Ver 5.2.2
 * J Rue		12/30/2004	If first transaction then do not check for 
 *							skip Form31No.
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							modify generateBodyRpt()
 *							defect 7838 Ver 5.2.2
 * J Rue		01/04/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							defect 7838 vER 5.2.2
 * K Harrell	05/22/2005	Java 1.4 Work; Constant rename from Receipt
 * 							Template. 
 * 							deprecate isItmBreak(),printFeesDiffHdr()
 * 							defect 7896 Ver 5.2.3 
 * J Rue		06/14/2005	New method to print Form31No from
 * 							InventoryData object.
 * 							new printForm31No(Vector, int, String)
 * 							deprecate printForm31No(String, int)
 * 							modify generateBodyRpt()
 * 							defect 7982 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify generateBodyRpt(),
 * 							generateHeaderColumns(),
 * 							getCacheDealerInfo(), printDealerFees()
 * 							printPymntMethod(), queryData()
 *							defect 7896 Ver 5.2.3
 * J Rue		12/15/2005	Reinstate printFeesDiffHdr()
 * 							Set print character length using correct 
 * 							constant.
 * 							modify printFeesDiffHdr() 
 * 							modify totalsDlrCompletedRpt()
 * 							defect 7898 Ver 5.2.3
 * J Rue  		01/03/2006	Add check for Dealer Id < 1 print 000
 * 							modify generateHeaderColumns()
 * 							defect 7898 Ver 5.2.3
 * J Rue		01/14/2009	Comment out unused code lines. 
 * 							Rename csOldForm31 to csPrevForm31No
 * 							Save current Form31No to PrevForm31No
 * 							avPymntData is not used in generateBodyRpt()
 * 							isItmBreak() move to MediaValidations	
 * 							modify generateBodyRpt(), formatReport()
 * 							defect 9045 Ver Defect_POS_D           
 * B Hargrove	03/30/2009	Add column for 'Electronic Title' indicator
 * 							(ETtlRqst). Adjust columns accordingly.
 * 							modify generateBodyRpt(),
 * 							generateHeaderColumns(), 
 * 							totalsDlrCompletedRpt()
 * 							defect 9977 Ver Defect_POS_E
 * B Hargrove	06/01/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							ciTotalDisketteRecs / ciTotalInputRecs
 * 							TOTALTRANSFROMDISKTEXT / TOTALTRANSFROMINPUT
 * 							totalDiskettesRecs() / totalInputRecs()
 * 							modify totalsDlrCompletedRpt()
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	07/03/2009	Implement new DealerData. Add'l Cleanup. 
 * 							delete isItmBreak(), printForm31No(Sting,int)
 * 							modify queryData(), getCacheDealerInfo()
 * 							defect 9666, 10112 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	12/16/2009	DTA Cleanup
 * 							add buildDealerPrtData()
 * 							delete SKIPPED, toPhoneFormat(), 
 * 							 getCacheDealerInfo() 
 * 							refactor caDealerTitle to caDlrTtlData
 * 							modify generateBodyRpt()   
 * 							defect 10290 Ver Defect_POS_H  
 * K Harrell	01/02/2009	Standardize printing of DealerId/BatchNo 
 * 							add csDlrBatchNo
 * 							delete STR_ZEROS
 * 							modify generateHeaderColumns(), 
 * 							 totalInputRecs(), totalKeyBoardEntry(),
 * 							 totalRejectedTitles()  
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	10/04/2010	delete XTEXT
 * 							modify generateBodyRpt() 
 * 							defect 10013 Ver 6.6.0   
 * ---------------------------------------------------------------------
 */
/**
 * The Dealer Title Preliminary Report class provides methods that are 
 * commonly used to format report data.
 *
 * <p>genDealerTitlePreliminaryReport will print a grand total of 
 * 		accumulate sales tax collected for a user input county number 
 *		and report date.
 *
 * <p>The report date is set to the last day of the month.
 *
 * <p>note: It is assumed that the " *** SKIP ***" will be included
 * in the data object used.
 * 
 * @version	6.6.0		 	10/04/2010
 * @author	R DUGGIR
 * <br>Creation Date:		11/12/2001 11:02:28
 */
public class GenDealerTitleCompletedReport extends ReportTemplate
{
	// Constant variable
	private final static String FORM31 = "FORM31";
	private final static String CRTCARDTEXT = " AS CREDIT CARD FEE)";
	private final static String INCLUDETEXT = "(INCLUDES $ ";
	private final static String CHECK = "CHECK";
	private final static String CHARGE = "CHARGE";
	private final static String ZERO_DOLLAR = "0.00";
	private final static String STR_EMPTY =
		CommonConstant.STR_SPACE_EMPTY;

	//	boolean 
	boolean cbCreditFeeIncMsgIndi = true;

	// int 
	private int ciTotalApproveRecs = 0;
	private int ciTotalInputRecs = 0;
	private int ciTotalManualEntered = 0;
	private int ciTotalRejectedTitles = 0;
	// defect 9977
	private int ciTotalETitles = 0;
	// end defect 9977

	// defect 10290
	//	String 
	private String csDlrBatchNo = new String();
	// end defect 10290 

	// Object
	// defect 10290 
	//private DealerTitleData caDealerTitle = new DealerTitleData();
	private DealerTitleData caDlrTtlData = new DealerTitleData();
	// end defect 10290 
	private Dollar caDlrFees = new Dollar(ZERO_DOLLAR);
	private Dollar caRTSFees = new Dollar(ZERO_DOLLAR);
	private Dollar caTotalDealerFees = new Dollar(ZERO_DOLLAR);
	private Dollar caTotalFeesDue = new Dollar(ZERO_DOLLAR);
	private MFVehicleData caMFVehData = new MFVehicleData();
	private ReceiptTemplate caRcpt = new ReceiptTemplate();

	// Vector
	Vector cvFeesData = new Vector();

	// Define Static Headers
	private final static String FORMAT31NUMBERTEXT = "FORM 31 NUMBER";
	private final static String TRANSIDTEXT = "TRANSACTION ID";
	private final static String FEESCALCUDLRTEXT =
		"FEES CALCULATED BY DEALER";
	private final static String FEESCALCURTSTEXT =
		"FEES CALCULATED BY RTS";
	private final static String INVENTORYTEXT = "INVENTORY";
	private final static String TOTALFEESDLRTEXT =
		"TOTAL FEES CALCULATED BY DEALER ";
	private final static String TOTALFEESDUETEXT = "TOTAL FEES DUE ";
	private final static String METHODPYMNTTEXT = "METHOD OF PAYMENT:";
	private final static String TOTALTRANSFROMINPUT =
		"TOTAL # OF TRANSACTIONS PROCESSED FROM EXTERNAL MEDIA:";
	private final static String TRANSREJECTEDTEXT =
		"TRANSACTIONS REJECTED:";
	private final static String TRANSENTEREDTEXT =
		" TRANSACTIONS ENTERED:";
	private final static String TRANSAPPROVEDTEXT =
		"TRANSACTIONS APPROVED:";
	private final static String AMTCALCUDLRTEXT =
		"AMOUNT CALCULATED BY DEALER IS MORE THAN FEES DUE BY ";
	private final static String AMTCALCURTSTEXT =
		"AMOUNT CALCULATED BY DEALER IS LESS THAN FEES DUE BY ";
	private final static String AMTDIFFTEXT =
		"DIFFERENCE IN FEES DUE AND FEES CALCULATED BY DEALER ";
	private final static String ENTEREDTEXT = "ENTERED";
	private final static String DEALERBATCHTEXT = "DEALER BATCH NO";
	// defect 9977
	private final static String ETITLESTEXT = "              ETITLES:";
	private final static String ETITLETEXT = "ETITLE";
	// end defect 9977
	
	// defect 10598 
	// private final static String XTEXT = "X";
	// end defect 10598 

	// Constant int
	// Define print columns
	private final static int COL_10 = 10;
	// defect 10075
	//private final static int COL_35 = 35;
	private final static int COL_38 = 38;
	// end defect 10075
	private final static int COL_48 = 48;
	private final static int COL_78 = 78;
	private final static int COL_86 = 86;
	private final static int COL_89 = 89;
	private final static int COL_54 = 54;
	private final static int COL_57 = 57;
	private final static int COL_107 = 107;
	// defect 9977
	private final static int COL_123 = 123;
	private final static int COL_126 = 126;
	// end defect 9977

	private final static String CHECKTEXT = "CHECK #";
	private final static String REJECTED = "REJECTED";
	private final static String DOLLARSIGNTEXT = "$";

	/**
	 * GenDealerTitleCompletedReport Constructor
	 */
	public GenDealerTitleCompletedReport()
	{
		// empty code block
	}

	/**
	 * GenDealerTitleCompletedReport Constructor
	 *
	 * @param asRptString String
	 * @param asRptProperties Object
	 */
	public GenDealerTitleCompletedReport(
		String asRptString,
		ReportProperties asRptProperties)
	{
		super(asRptString, asRptProperties);
	}

	/**
	 * Accumulate Dealer Fees.
	 * 
	 * @param aaDlrFees Dollar
	 */
	protected void accumulateDlrFees(Dollar aaDlrFees)
	{
		caDlrFees = caDlrFees.add(aaDlrFees);
	}

	/**
	 * Accumulate Dealer Fees.
	 * 
	 * @param aaRTSFees Dollar 
	 */
	protected void accumulateRTSFees(Dollar aaRTSFees)
	{
		caRTSFees.add(aaRTSFees);
	}

	/**
	 * Calculate the difference Dealer from RTSII.
	 *
	 * @return Dollar 
	 */
	protected Dollar calcDlrRTSDiff()
	{
		Dollar laAbs = new Dollar("-1.00");
		// defect 6549
		//	Replace cTotalDealerFees (does not include rejected trans)
		//	with caDlrFees
		Dollar laFeesDiff = caTotalFeesDue.subtract(caDlrFees);
		// check for negitive difference
		int liFeesDiffTest = caTotalFeesDue.compareTo(caDlrFees);
		// end defect 6549
		if (liFeesDiffTest < 0)
		{
			laFeesDiff = laFeesDiff.multiply(laAbs);
		}
		return laFeesDiff;
	}

	/**
	 * Format the string to show the Credit Card Fee Added message if 
	 * credit card is charged.
	 *
	 * @return String
	 */
	protected String formatCreditCardFeeMessage()
	{
		Dollar laZERO_DOLLAR = new Dollar(0.00);
		CreditCardFeeData laCreditFeeInfo =
			CreditCardFeesCache.getCurrentCreditCardFees(
				SystemProperty.getOfficeIssuanceNo(),
				new RTSDate());
		String lsCreditCardFeeMessage = null;
		if (laCreditFeeInfo != null
			&& (laCreditFeeInfo.getItmPrice().compareTo(laZERO_DOLLAR)
				> 0))
		{
			Dollar laCreditChargeAmt = laCreditFeeInfo.getItmPrice();
			lsCreditCardFeeMessage =
				INCLUDETEXT
					+ laCreditChargeAmt.toString()
					+ CRTCARDTEXT;
		}
		return lsCreditCardFeeMessage;
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 *
	 * @param avDealerResults Vector
	 */
	public void formatReport(Vector avDealerResults)
	{
		// empty code block
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 *
	 * @param avDlrTtlData Vector
	 * @param avPymntData Vector 
	 */
	public void formatReport(Vector avDlrTtlData, Vector avPymntData)
	{
		int liFirstElement = 0;
		try
		{
			caDlrTtlData =
				(DealerTitleData) avDlrTtlData.elementAt(
					liFirstElement);
			totalInputRecs(avDlrTtlData);
			totalRejectedTitles(avDlrTtlData);
			totalKeyBoardEntry(avDlrTtlData);
			totalApprovedTrans(avDlrTtlData);
			// Get current data, print Headers and Columns
			generateHeaderColumns(caDlrTtlData);
			generateBodyRpt(avDlrTtlData);
			totalsDlrCompletedRpt(avPymntData);
			handleBreak(2);
			// defect 8628 
			generateFooter(true);
			// end defect 8628 
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in formatReport() of"
					+ " com.txdot.isd.rts.services.reports.title");
			aeEx.printStackTrace(System.out);
		} // end catch block
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Print body of the report.
	 *  
	 * @param avDlrTtlData Vector
	 * @param avPymntData Vector 
	 * @throws RTSException
	 */
	protected void generateBodyRpt(Vector avDlrTtlData)
		throws RTSException
	{
		String lsTransId = STR_EMPTY;

		for (int liIndex = 0; liIndex < avDlrTtlData.size(); liIndex++)
		{
			caDlrTtlData =
				(DealerTitleData) avDlrTtlData.elementAt(liIndex);

			handleBreak(1);

			// defect 10290 
			if (caDlrTtlData.isSkipCurrObj())
			{
				printForm31No(
					new Vector(),
					ReceiptTemplate.COL_06,
					caDlrTtlData.getForm31No());
				this.caRpt.nextLine();
				handleBreak(1);
			}
			// end defect 10290 
			else
			{
				// Set Form31No to empty if null
				if (caDlrTtlData.getForm31No() == null)
				{
					caDlrTtlData.setForm31No(STR_EMPTY);
				}
				// end defect 9045

				// Print FORM31
				printForm31No(
					caDlrTtlData.getInventoryData(),
					ReceiptTemplate.COL_06,
					STR_EMPTY);

				// If record was not rejected
				if (!(caDlrTtlData.isRecordRejected()))
				{
					// Get TransId
					lsTransId = caDlrTtlData.getTransId();
					if (lsTransId == null
						|| caDlrTtlData.isRecordRejected())
					{
						lsTransId = CommonConstant.STR_SPACE_EMPTY;
					}
					// get number of inventory items issued
					int liInvSize = 0;
					if (caDlrTtlData.getInventoryData() != null)
					{
						liInvSize =
							caDlrTtlData.getInventoryData().size();
					}
					handleBreak(liInvSize);

					printTransId(lsTransId, ReceiptTemplate.COL_24);

					printDealerFees(
						caDlrTtlData.getFee().printDollar().substring(
							1),
						ReceiptTemplate.COL_49,
						caDlrTtlData.isKeyBoardEntry());

					// Accumulate dealer fees
					caTotalDealerFees =
						caTotalDealerFees.add(caDlrTtlData.getFee());

					// Print RTS Fees, Inventory issued
					printRTSIIFee(
						caDlrTtlData
							.getTransFee()
							.printDollar()
							.substring(
							1),
						COL_89);

					printInvIssued(
						caDlrTtlData.getInventoryData(),
						COL_107);

					caTotalFeesDue =
						caTotalFeesDue.add(caDlrTtlData.getTransFee());

					// defect 9977
					// Add ETitle
					if (caDlrTtlData.isETtlRqst())
					{
						// defect 10013 
						// caRpt.print(XTEXT, COL_126, 1);
						caRpt.print(TitleConstant.ETITLE_SYMBOL, COL_126, 1);
						// end defect 10013
						ciTotalETitles++;
					}
					// end defect 9977
				}
				else
				{
					printDealerFees(
						caDlrTtlData.getFee().printDollar().substring(
							1),
						ReceiptTemplate.COL_49,
						caDlrTtlData.isKeyBoardEntry());
					caRpt.print(REJECTED, COL_78, REJECTED.length());
				}
				caRpt.blankLines(1);
			}
		}
	}

	/**
	 * Build header information.
	 *
	 * @param  aaDealerData Object
	 * @throws RTSException
	 */
	private void generateHeaderColumns(DealerTitleData aaDlrTtlData)
		throws RTSException
	{
		// Define local variables
		// defect 10290 
		// DecimalFormat laThreeDigits = new DecimalFormat(STR_ZEROS);
		// end defect 10290 

		// Define Header/Column vectors to be passed
		Vector lvHeader = new Vector();

		// Dealer Info
		Vector lvMoreDetails = new Vector();

		// Column Headers
		Vector lvTable = new Vector();

		// defect 10290
		csDlrBatchNo = aaDlrTtlData.getDlrBatchNo();

		lvHeader.addElement(DEALERBATCHTEXT);
		lvHeader.addElement(csDlrBatchNo);

		// Add Dealer Info to vector
		lvMoreDetails = buildDealerPrtData();
		// end defect 10290

		// get employee id

		//Adding ColumnHeader Information	
		Vector lvRow1 = new Vector();
		ReceiptTemplate laCharLngth = new ReceiptTemplate();

		ColumnHeader laColumn1 =
			new ColumnHeader(
				FORMAT31NUMBERTEXT,
				ReceiptTemplate.COL_06,
				laCharLngth.charLength(FORMAT31NUMBERTEXT));

		ColumnHeader laColumn2 =
			new ColumnHeader(
				TRANSIDTEXT,
				ReceiptTemplate.COL_24,
				laCharLngth.charLength(TRANSIDTEXT));

		ColumnHeader laColumn3 =
			new ColumnHeader(
				FEESCALCUDLRTEXT,
				COL_48,
				laCharLngth.charLength(FEESCALCUDLRTEXT));
		ColumnHeader laColumn4 =
			new ColumnHeader(
				FEESCALCURTSTEXT,
				COL_78,
				laCharLngth.charLength(FEESCALCURTSTEXT));
		ColumnHeader laColumn5 =
			new ColumnHeader(
				INVENTORYTEXT,
				COL_107,
				laCharLngth.charLength(INVENTORYTEXT));
		// defect 9977
		ColumnHeader laColumn6 =
			new ColumnHeader(
				ETITLETEXT,
				COL_123,
				laCharLngth.charLength(ETITLETEXT));
		// end defect 9977

		//Adding ColumnHeader1 Information	
		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);
		lvRow1.addElement(laColumn3);
		lvRow1.addElement(laColumn4);
		lvRow1.addElement(laColumn5);
		// defect 9977
		lvRow1.addElement(laColumn6);
		// end defect 9977
		lvTable.addElement(lvRow1);

		generateHeader(lvHeader, lvMoreDetails, lvTable);
	}

	/**
	 * Build DealerInfo from input media.
	 * 
	 * @param aaDlrTtlData
	 */
	private Vector buildDealerPrtData()
	{
		DealerData laDealerData = new DealerData();

		Vector lvDealerData = new Vector();

		if (Transaction.getDTADealerData() != null)
		{
			laDealerData = Transaction.getDTADealerData();
		}

		lvDealerData.add(csDlrBatchNo);
		lvDealerData.addAll(laDealerData.getDealerInfoVector());
		return lvDealerData;
	}

	/**
	 * Handle Report Breaks
	 * 
	 * @param aiLines int
	 * @throws RTSException
	 */
	public void handleBreak(int aiLines) throws RTSException
	{
		int liTLines = this.caRptProps.getPageHeight() - 2;
		int lilines = this.caRpt.getCurrX();
		if (aiLines + lilines >= liTLines)
		{
			generateFooter();
			generateHeaderColumns(caDlrTtlData);
		}
	}

	/**
	 * Main Method
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			CacheManager.loadCache();
			// Instantiating a new Report Class 
			ReportProperties laRptProps =
				new ReportProperties("RTS.POS.2171");
			GenDealerTitleCompletedReport laGpr =
				new GenDealerTitleCompletedReport(
					"DEALER COMPLETED REPORT",
					laRptProps);
			// Generating Demo data to display.
			String lsQuery = "Select * FROM RTS_TBL";
			Vector lvQueryDTACompleted = laGpr.queryData(lsQuery);
			laGpr.formatReport(lvQueryDTACompleted);
			//Writing the Formatted String onto a local file
			File laOutputFile;
			FileOutputStream laFout;
			PrintStream laPout = null;
			laOutputFile = new File("c:\\RTS\\RPT\\DLRCOMPL.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
			laPout.print(laGpr.caRpt.getReport().toString());
			laPout.close();
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of"
					+ " com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeException.printStackTrace(System.out);
		}
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RPT\\DLRCOMPL.RPT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec("cmd.exe /c copy
			// c:\\QuickCtrRpt.txt prn");
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Print Dealer Fees.
	 * 
	 * @param asFee String
	 * @param aiCol int
	 * @param abKeyBoard boolean
	 */
	private void printDealerFees(
		String asFee,
		int aiCol,
		boolean abKeyBoard)
	{
		String lsZeroDollar = ZERO_DOLLAR;
		int liFeeCompare = 0;

		liFeeCompare = asFee.compareTo(lsZeroDollar);

		if (abKeyBoard)
		{
			caRpt.print(
				ENTEREDTEXT,
				aiCol,
				caRcpt.charLength(ENTEREDTEXT));
			caRpt.rightAlign(
				asFee,
				COL_57,
				ReceiptTemplate.AMT_LNGTH_13);
			accumulateDlrFees(caDlrTtlData.getFee());
		}
		else if (
			asFee.equals(STR_EMPTY)
				|| asFee == null
				|| liFeeCompare < 1)
		{
			caRpt.print(" ", aiCol, caRcpt.charLength(" "));
		}
		else
		{
			caRpt.rightAlign(
				asFee,
				COL_57,
				ReceiptTemplate.AMT_LNGTH_13);
			accumulateDlrFees(caDlrTtlData.getFee());
		}
	}

	/**
	 * Return the Amount difference Dealer from RTSII.
	 *
	 * @return String
	 */
	private String printFeesDiffHdr()
	{
		String lsFeesHdr = CommonConstant.STR_SPACE_EMPTY;
		// defect 6549
		// Replace cTotalDealerFees (does not include rejected trans)
		//		with cdDlrFees
		switch (caDlrFees.compareTo(caTotalFeesDue))
		{
			// end defect 6549
			case 0 :
				{
					this.caRpt.print(
						AMTDIFFTEXT,
						ReceiptTemplate.COL_06,
						caRcpt.charLength(AMTDIFFTEXT));
					lsFeesHdr =
						caRcpt.setNumOfDashes(9) + DOLLARSIGNTEXT;
					this.caRpt.print(
						lsFeesHdr,
						caRcpt.charLength(lsFeesHdr));
					break;
				}
			case 1 :
				{
					this.caRpt.print(
						AMTCALCUDLRTEXT,
						ReceiptTemplate.COL_06,
						caRcpt.charLength(AMTCALCUDLRTEXT));
					lsFeesHdr =
						caRcpt.setNumOfDashes(9) + DOLLARSIGNTEXT;
					this.caRpt.print(
						lsFeesHdr,
						caRcpt.charLength(lsFeesHdr));
					break;
				}
			default :
				{
					this.caRpt.print(
						AMTCALCURTSTEXT,
						ReceiptTemplate.COL_06,
						caRcpt.charLength(AMTCALCURTSTEXT));
					lsFeesHdr =
						caRcpt.setNumOfDashes(9) + DOLLARSIGNTEXT;
					this.caRpt.print(
						lsFeesHdr,
						caRcpt.charLength(lsFeesHdr));
					break;
				}
		}
		return lsFeesHdr;
	}

	/**
	 * Print Form31 Number.
	 * 
	 * @param avInvIssued Vector - Inventory Issued
	 * @param aiCol int - Column position to start printing
	 * @param asSkipped String - Empty, not skipped
	 *                           Not Empty, Skipped
	 */
	private void printForm31No(
		Vector avInvIssued,
		int aiCol,
		String asSkipped)
	{
		if (asSkipped.length() != 0)
		{
			caRpt.print(asSkipped, aiCol, caRcpt.charLength(asSkipped));
		}
		else
		{
			if (avInvIssued != null)
			{
				for (int liIndex = 0;
					liIndex < avInvIssued.size();
					liIndex++)
				{
					ProcessInventoryData laProcessInvData =
						new ProcessInventoryData();
					laProcessInvData =
						(ProcessInventoryData) avInvIssued.elementAt(
							liIndex);
					if (laProcessInvData.getItmCd().equals(FORM31))
					{
						caRpt.print(
							laProcessInvData.getInvItmNo(),
							aiCol,
							caRcpt.charLength(
								laProcessInvData.getInvItmNo()));
						break;
					}
				}
			}
		}
	}

	/**
	 * Print inventory issued.
	 * 
	 * @param avInvIssued Vector
	 * @param aiCol int
	 */
	private void printInvIssued(Vector avInvIssued, int aiCol)
	{
		ProcessInventoryData laProcessInvData =
			new ProcessInventoryData();
		if (avInvIssued != null)
		{
			for (int i = 0; i < avInvIssued.size(); i++)
			{
				laProcessInvData =
					(ProcessInventoryData) avInvIssued.elementAt(i);

				if (!laProcessInvData.getItmCd().equals("FORM31")
					&& ((ItemCodesData) ItemCodesCache
						.getItmCd(laProcessInvData.getItmCd()))
						.getPrintableIndi()
						== 0)
				{
					caRpt.print(
						laProcessInvData.getInvItmNo(),
						aiCol,
						caRcpt.charLength(
							laProcessInvData.getInvItmNo()));
				}
			}
		}
	}

	/**
	 * Print Payment data.
	 *  
	 * @param avPymntData Vector 
	 * @throws RTSException
	 */
	private void printPymntMethod(Vector avPymntData)
		throws RTSException
	{
		handleBreak(1);
		this.caRpt.print(
			METHODPYMNTTEXT,
			ReceiptTemplate.COL_06,
			caRcpt.charLength(METHODPYMNTTEXT));
		this.caRpt.nextLine();
		Dollar laPymntMin = new Dollar(ZERO_DOLLAR);
		int liPymntMin = 0;
		if (avPymntData != null)
		{
			handleBreak(avPymntData.size());
			for (int liIndex = 0;
				liIndex < avPymntData.size();
				liIndex++)
			{
				TransactionPaymentData laPymnt =
					(TransactionPaymentData) avPymntData.get(liIndex);
				liPymntMin =
					laPymnt.getPymntTypeAmt().compareTo(laPymntMin);
				if (liPymntMin > 0)
				{
					if (laPymnt.getPymntType().equals(CHECK))
					{
						this.caRpt.print(
							CHECKTEXT,
							ReceiptTemplate.COL_06,
							caRcpt.charLength(CHECKTEXT));
						this.caRpt.print(
							laPymnt.getPymntCkNo() == null
								? STR_EMPTY
								: laPymnt.getPymntCkNo());
						this.caRpt.print("$", 23, 1);
						this.caRpt.rightAlign(
							laPymnt
								.getPymntTypeAmt()
								.printDollar()
								.substring(
								1),
							24,
							13);
					}
					else
					{
						this.caRpt.print(
							laPymnt.getPymntType(),
							ReceiptTemplate.COL_06,
							16);
						this.caRpt.print("$", 23, 1);
						this.caRpt.rightAlign(
							laPymnt
								.getPymntTypeAmt()
								.printDollar()
								.substring(
								1),
							24,
							13);
						if (cbCreditFeeIncMsgIndi
							&& laPymnt.getPymntType().equals(CHARGE))
						{
							cbCreditFeeIncMsgIndi = false;
							String lsCreditCardFeeMessage =
								formatCreditCardFeeMessage();
							if (lsCreditCardFeeMessage != null)
							{
								caRpt.blankLines(1);
								// Move down 1 lines
								this.caRpt.print(
									lsCreditCardFeeMessage,
									ReceiptTemplate.COL_06,
									lsCreditCardFeeMessage.length());
							}
						}
					}
					caRpt.blankLines(1);
				}
			}
		}
	}

	/**
	 * Print RTSII Fees.
	 *  
	 * @param asInput String
	 * @param aiCol int 
	 */
	private void printRTSIIFee(String asInput, int aiCol)
	{
		if (asInput.equals(STR_EMPTY) || asInput == null)
		{
			caRpt.print(" ", aiCol, caRcpt.charLength(" "));
		}
		else
		{
			caRpt.rightAlign(asInput, aiCol - 8, 13);
			accumulateRTSFees(caDlrTtlData.getTransFee());
		}
	}

	/**
	 * Print Transaction Id.
	 * 
	 * @param asInput String
	 * @param aiCol int 
	 */
	private void printTransId(String asInput, int aiCol)
	{
		caRpt.print(asInput, aiCol, caRcpt.charLength(asInput));
	}

	/**
	 * setup test data for report
	 *  
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		Vector lvInvData = new Vector();

		Dollar laFee1 = new Dollar(1000.00);

		Vector lvReceipt = new Vector();

		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		caMFVehData = (MFVehicleData) laTransData.getVehicleInfo();
		ProcessInventoryData laProcessInvData =
			new ProcessInventoryData();

		// Set select data for the completed report
		// defect 10112 
		// caDealerTitle.setDlrbatchNo("001");
		// end defect 10112 
		caDlrTtlData.setDealerSeqNo("001");
		caDlrTtlData.setDealerId(001);
		caDlrTtlData.setFee(laFee1);
		caDlrTtlData.setTransFee(laFee1);
		caDlrTtlData.setForm31No("A000300");
		caDlrTtlData.setRecordRejected(false);
		caDlrTtlData.setKeyBoardEntry(false);
		caDlrTtlData.setMFVehicleData(caMFVehData);

		// InvItmNo #1
		laProcessInvData.setInvItmNo("RTG56T");
		lvInvData.addElement(laProcessInvData);

		// InvItmNo #2
		laProcessInvData.setInvItmNo("44332222WC");
		lvInvData.addElement(laProcessInvData);
		caDlrTtlData.setInventoryData(lvInvData);

		lvReceipt.addElement(caDlrTtlData);
		lvReceipt.addElement(caDlrTtlData);

		return lvReceipt;

	}

	/**
	 * Get total number of Approved records.
	 * 
	 * @param avDlrTitleData Vector
	 */
	protected void totalApprovedTrans(Vector avDlrTitleData)
	{
		for (int liIndex = 0;
			liIndex < avDlrTitleData.size();
			liIndex++)
		{
			caDlrTtlData =
				(DealerTitleData) avDlrTitleData.elementAt(liIndex);

			if (!caDlrTtlData.isRecordRejected()
				&& !caDlrTtlData.isSkipCurrObj())
			{
				ciTotalApproveRecs++;
			}
		}
	}

	/**
	 * Get total number of records from input media.
	 *
	 * @param avDlrTitleData Vector
	 */
	protected void totalInputRecs(Vector avDlrTitleData)
	{
		for (int liIndex = 0;
			liIndex < avDlrTitleData.size();
			liIndex++)
		{
			caDlrTtlData =
				(DealerTitleData) avDlrTitleData.elementAt(liIndex);

			// defect 10290 
			// if ((!caDlrTtlData.isRecordRejected()
			//	 			|| !caDlrTtlData.isSkipCurrObj())
			//	 			&& (!caDlrTtlData.isKeyBoardEntry()))
			if (!caDlrTtlData.isSkipCurrObj()
				&& !caDlrTtlData.isKeyBoardEntry())
			{
				// end defect 10290 
				ciTotalInputRecs++;
			}
		}
	}

	/**
	 * Get total number of rejected titles.
	 * 
	 * @param avDlrTitleData Vector
	 */
	protected void totalKeyBoardEntry(Vector avDlrTitleData)
	{
		for (int liIndex = 0;
			liIndex < avDlrTitleData.size();
			liIndex++)
		{
			caDlrTtlData =
				(DealerTitleData) avDlrTitleData.elementAt(liIndex);

			// defect 10290 
			// if (caDlrTtlData.isKeyBoardEntry()
			//	 && !caDlrTtlData.isRecordRejected())
			if (caDlrTtlData.isKeyBoardEntry()
				&& !caDlrTtlData.isSkipCurrObj()
				&& !caDlrTtlData.isRecordRejected())
			{
				// end defect 10290 
				ciTotalManualEntered++;
			}
		}
	}

	/**
	 * Get total number of rejected titles, External Media only.
	 *  
	 * @param avDlrTitleData Vector
	 */
	protected void totalRejectedTitles(Vector avDlrTitleData)
	{
		for (int liIndex = 0;
			liIndex < avDlrTitleData.size();
			liIndex++)
		{
			caDlrTtlData =
				(DealerTitleData) avDlrTitleData.elementAt(liIndex);

			// defect 10290 
			// if (caDlrTtlData.isRecordRejected()
			//	&& !caDlrTtlData.isKeyBoardEntry())
			if (!caDlrTtlData.isSkipCurrObj()
				&& !caDlrTtlData.isKeyBoardEntry()
				&& caDlrTtlData.isRecordRejected())
			{
				// end defect 10290 
				ciTotalRejectedTitles++;
			}
		}
	}

	/**
	 * Print the totals for Dealer Fees, RTS Generate Fees.
	 *
	 * @param avPymntData Vector
	 * @throws RTSException
	 */
	private void totalsDlrCompletedRpt(Vector avPymntData)
		throws RTSException
	{
		String lsDashes = STR_EMPTY;
		String lsUnderScores = STR_EMPTY;

		// Print underscores, dealer fees
		lsUnderScores = caRcpt.numOfUnderScores(18);
		this.caRpt.rightAlign(
			lsUnderScores,
			COL_54,
			caRcpt.charLength(lsUnderScores));

		// Move to the next line
		handleBreak(2);
		this.caRpt.blankLines(1);

		// Print total fees calculated by the dealer
		this.caRpt.print(
			TOTALFEESDLRTEXT,
			ReceiptTemplate.COL_06,
			caRcpt.charLength(TOTALFEESDLRTEXT));
		lsDashes = caRcpt.setNumOfDashes(5) + DOLLARSIGNTEXT;
		this.caRpt.print(lsDashes, caRcpt.charLength(lsDashes));
		caRpt.rightAlign(
			caDlrFees.printDollar().substring(1),
			COL_57,
			ReceiptTemplate.AMT_LNGTH_13);

		handleBreak(2);
		this.caRpt.blankLines(1);

		// Print underscores, RTS fees
		lsUnderScores = caRcpt.numOfUnderScores(18);
		this.caRpt.rightAlign(
			lsUnderScores,
			COL_78,
			caRcpt.charLength(lsUnderScores));

		handleBreak(2);
		this.caRpt.blankLines(1);

		// Print total fees due
		this.caRpt.print(
			TOTALFEESDUETEXT,
			ReceiptTemplate.COL_06,
			caRcpt.charLength(TOTALFEESDUETEXT));
		lsDashes = caRcpt.setNumOfDashes(28) + DOLLARSIGNTEXT;
		this.caRpt.print(lsDashes, caRcpt.charLength(lsDashes));
		caRpt.rightAlign(
			caTotalFeesDue.printDollar().substring(1),
			COL_86 - 2,
			COL_10);

		handleBreak(3);
		this.caRpt.blankLines(2);

		// Print amount difference Dealer from RTSII
		printFeesDiffHdr();

		Dollar laFeesDiff = calcDlrRTSDiff();
		this.caRpt.rightAlign(
			laFeesDiff.printDollar().substring(1),
			COL_86 - 2,
			COL_10);

		// Skip a line
		handleBreak(2);
		this.caRpt.blankLines(2);

		// Print method of payment
		printPymntMethod(avPymntData);

		handleBreak(2);
		this.caRpt.blankLines(2);

		// Total number of transactions on the input media
		handleBreak(4);
		this.caRpt.print(
			TOTALTRANSFROMINPUT,
			ReceiptTemplate.COL_06,
			caRcpt.charLength(TOTALTRANSFROMINPUT));

		this.caRpt.print(
			String.valueOf(ciTotalInputRecs),
			ReceiptTemplate.COL_62,
			caRcpt.charLength(String.valueOf(ciTotalInputRecs)));

		this.caRpt.blankLines(1);

		// Print transactions rejected
		this.caRpt.rightAlign(
			TRANSREJECTEDTEXT,
			COL_38,
			caRcpt.charLength(TRANSREJECTEDTEXT));
		this.caRpt.print(
			String.valueOf(ciTotalRejectedTitles),
			ReceiptTemplate.COL_62,
			caRcpt.charLength(String.valueOf(ciTotalRejectedTitles)));

		this.caRpt.blankLines(1);

		// Print Manual transactions entered
		this.caRpt.rightAlign(
			TRANSENTEREDTEXT,
			COL_38,
			caRcpt.charLength(TRANSENTEREDTEXT));
		this.caRpt.print(
			String.valueOf(ciTotalManualEntered),
			ReceiptTemplate.COL_62,
			caRcpt.charLength(String.valueOf(ciTotalManualEntered)));

		this.caRpt.blankLines(1);

		// Print transactions approved
		this.caRpt.rightAlign(
			TRANSAPPROVEDTEXT,
			COL_38,
			caRcpt.charLength(TRANSAPPROVEDTEXT));
		this.caRpt.rightAlign(
			String.valueOf(ciTotalApproveRecs),
			ReceiptTemplate.COL_62,
			caRcpt.charLength(String.valueOf(ciTotalApproveRecs)));

		this.caRpt.blankLines(1);

		// defect 9977
		this.caRpt.rightAlign(
			ETITLESTEXT,
			COL_38,
			caRcpt.charLength(ETITLESTEXT));
		this.caRpt.rightAlign(
			String.valueOf(ciTotalETitles),
			ReceiptTemplate.COL_62,
			caRcpt.charLength(String.valueOf(ciTotalETitles)));
		// end defect 9977

		this.caRpt.blankLines(1);
	}
}
