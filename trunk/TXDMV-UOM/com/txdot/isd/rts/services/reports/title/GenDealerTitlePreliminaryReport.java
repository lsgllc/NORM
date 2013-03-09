package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.*;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * GenDealerTitlePreliminaryReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue 		09/19/2001	Added comments
 * J Rue		07/09/2002	Add logic to handle SKIP of Form 31 numbers.
 *							modify isItmBreak()
 *							defect 4436
 * J Rue  		09/09/2002	Added new method to build dealer data
 *							if dealer id is invalid.
 *						  	add buildDealerPrtData()
 *							modify formatReport(Vector)
 *							defect 4701
 * J Rue		12/02/2004	Catch NumberFormatException if Form31No is 
 *							not numeric
 *							modify formatReport(), isItmBreak()
 *							defect 7692 Ver 5.2.2
 * J Rue		12/30/2004	Reset variable to original value.
 * K Harrell				JavaDoc/Formatting/Variable Name Cleanup
 *							modify isItmBreak()
 *							defect 7838 Ver 5.2.2
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3   
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3 
 * J Rue		12/31/2008	Modify the conditional to check for 
 * 							isItmBreak() first
 * 							modify printDataLine()
 * 							defect 9045 Ver Defect_POS_D  
 * J Rue		01/14/2009	Comment out unused code lines. 
 * 							Rename csOldForm31 to csPrevForm31No
 * 							isItmBreak() move to MediaValidations
 * 							delect isItmBreak()	
 * 							modify printDataLine()
 * 							defect 9045 Ver Defect_POS_D            
 * J Rue		01/21/2009	Add <p> to javadoc. Correct deftc to defect 
 * 							modify printDataLine()
 * 							defect 9045 Ver Defect_POS_D            
 * B Hargrove	03/12/2009	Add column for 'Electronic Title' indicator
 * 							(ETtlRqst). Adjust columns accordingly.
 * 							modify formatReport(),
 * 							generateHeaderColumns(), printDataLine()
 * 							defect 9977 Ver Defect_POS_E
 * B Hargrove	06/02/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							modify TOTALTRANSTEXT, printDataLine(),
 * 							printTotalFeesTrans()
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	07/06/2009	Implement new Dealer Data.  Present all 
 * 							 DealerData info on Report. Additional 
 * 							 cleanup.
 * 							add buildDealerPrtData(), 
 * 							 generateHeaderColumns()  
 * 							add START_POS_COL_HDR_ETITLE,
 * 							 START_POS_COL_HDR_FORM31,
 * 							 START_POS_COL_HDR_FEES, 
 * 							 START_POS_COL_DETAIL_ETITLE,
 * 							 START_POS_COL_DETAIL_SKIP,
 * 							 START_POS_COL_DETAIL_FEES,
 * 							 START_POS_SUMMARY_TOTAL_TRANS,
 * 							 START_POS_SUMMARY_TOTAL_NO_OF_RECORDS
 * 							delete START_POS_27, START_POS_91,
 * 							 START_POS_93, START_POS_28, START_POS_30,
 * 							 START_POS_46, START_POS_50, START_POS_76,
 * 							 START_POS_79
 * 							delete buildDealerPrtData(Vector), 
 * 							 generateHeaderColumns(Vector), 
 * 							 toPhoneFormat(), getDlrBatchNo(), 
 * 							 setDlrBatchNo() 
 * 							modify generateHeaderColumns(), 
 * 							printTotalFeesTrans()
 * 							defect 9666, 10112 Ver Defect_POS_F 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	12/28/2009	DTA Cleanup
 * 							delete SKIP
 * 							add handleBreak() 
 * 							refactor caDealerTitle to caDlrTtlData
 * 							modify generateBody(), buildDealerPrtData(),
 * 							 printDataLine()    
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/02/2009	Standardize printing of DealerId/BatchNo  
 * 							modify buildDealerPrtData() 
 * 							defect 10290 Ver Defect_POS_H    
 * K Harrell	10/04/2010	modify formatReport()
 * 							defect 10013 Ver 6.6.0     
 * ---------------------------------------------------------------------
 */
/**
 * <p>The Dealer Title Preliminary Report class provides methods that are 
 * commonly used to format report data.
 *
 * <p>genDealerTitlePreliminaryReport will print a grand 
 * total of accumulate sales tax collected for a user input county 
 * number and report date.
 * 
 * <p>The report date is set to the last day of the month.
 * Note: It is assumed that the " *** SKIP ***" will be included in the 
 * data object used.
 *
 * @version	6.6.0			10/04/2010
 * @author	R DUGGIR
 * <br>Creation Date:		11/12/2001 11:02:41
 */
public class GenDealerTitlePreliminaryReport extends ReportTemplate
{
	// String 
	private String csDlrBatchNo = SPACE_EMPTY;

	// Vector
	private Vector cvDlrHeaderData = new Vector();

	// Constants 	
	private final static String DLRBATCHNOTEXT = "DEALER BATCH NO";
	// defect 9977
	private final static String ETITLETEXT = "ETITLE";
	// end defect 9977 
	private final static String FEECALCUTEXT =
		"FEES CALCULATED BY DEALER";
	private final static String FORM31NUMBERTEXT = "FORM 31 NUMBER";
	private final static String SKIP = "***SKIP***";
	private final static String SPACE_EMPTY =
		CommonConstant.STR_SPACE_EMPTY;
	private final static int START_POS_COL_DETAIL_ETITLE = 30;
	private final static int START_POS_COL_DETAIL_FEES = 79;
	private final static int START_POS_COL_DETAIL_SKIP = 50;
	private final static int START_POS_COL_HDR_ETITLE = 28;
	private final static int START_POS_COL_HDR_FEES = 76;
	private final static int START_POS_COL_HDR_FORM31 = 46;
	private final static int START_POS_SUMMARY_TOTAL_NO_OF_RECORDS = 91;
	private final static int START_POS_SUMMARY_TOTAL_TRANS = 27;
	private final static String STR_ZERO = "0.00";
	private final static String TOTALFEESTEXT =
		"TOTAL FEES CALCULATED BY DEALER:";

	private final static String TOTALTRANSTEXT =
		"TOTAL # OF TRANSACTIONS PROCESSED FROM INPUT MEDIA:";

	/**
	 * Main Method - used to start class as an application
	 * 
	 * @param aarrArgs String[] of command line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.2151");

		GenDealerTitlePreliminaryReport laGPR =
			new GenDealerTitlePreliminaryReport(
				"DEALER PRELIMINARY REPORT",
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryDTAPreliminary = laGPR.queryData(lsQuery);
		laGPR.formatReport(lvQueryDTAPreliminary);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream lpfsFileOutputStream;
		PrintStream laPrintStream = null;
		try
		{
			laOutputFile = new File("d:\\RTS\\RPT\\DLRPRELM.RPT");
			lpfsFileOutputStream = new FileOutputStream(laOutputFile);
			laPrintStream = new PrintStream(lpfsFileOutputStream);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPrintStream.print(laGPR.caRpt.getReport().toString());
		laPrintStream.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("d:\\RTS\\RPT\\DLRPRELM.RPT");
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
			// "cmd.exe /c copy c:\\QuickCtrRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of com.txdot.isd.rts."
					+ "client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * GenDealerTitlePreliminaryReport constructor
	 */
	public GenDealerTitlePreliminaryReport()
	{
		super();
	}

	/**
	 * GenDealerTitlePreliminaryReport constructor
	 *
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenDealerTitlePreliminaryReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Build DealerInfo from input media.
	 * 
	 * @param avDlrData Vector
	 */
	private void buildDealerPrtData(Vector avDlrtTtlData)
	{
		// defect 10290 
		DealerData laDealerData = new DealerData();

		DealerTitleData laDlrTtlData =
			(DealerTitleData) avDlrtTtlData.elementAt(0);

		csDlrBatchNo = laDlrTtlData.getDlrBatchNo();

		if (Transaction.getDTADealerData() != null)
		{
			laDealerData = Transaction.getDTADealerData();
		}
		// end defect 10290 

		cvDlrHeaderData.add(csDlrBatchNo);
		cvDlrHeaderData.addAll(laDealerData.getDealerInfoVector());
		return;
	}

	/**
	 * Print the Dealer Preliminary Report
	 *
	 * @param avDealerResults Vector 
	 */
	public void formatReport(Vector avDlrTtlData)
	{
		int liNumOfDlrData = avDlrTtlData.size();
		int liNumOfRecsPrinted = 0;
		Dollar laTotalFees = new Dollar(STR_ZERO);

		// Build the Dealer info from the Diskette 
		buildDealerPrtData(avDlrTtlData);

		try
		{
			generateHeaderColumns();

			// defect 10290 
			// Use handleBreak() 
			for (int liPrintLineIndex = 0;
				liPrintLineIndex < liNumOfDlrData;
				liPrintLineIndex++)
			{
				// Print the detail line, ETitle, FORM31 and FEES
				DealerTitleData laDlrTitleData =
					(DealerTitleData) avDlrTtlData.elementAt(
						liPrintLineIndex);

				// defect 9977
				// Add ETitle
				String lsETitle = SPACE_EMPTY;
				if (laDlrTitleData.isETtlRqst())
				{
					// defect 10013 
					//lsETitle = "X";
					lsETitle = TitleConstant.ETITLE_SYMBOL;
					// end defect 10013 
				}
				if (laDlrTitleData.isSkipCurrObj())
				{
					this.caRpt.print(
						SKIP,
						START_POS_COL_DETAIL_SKIP,
						SKIP.length());
					this.caRpt.nextLine();
				}
				else
				{
					printDataLine(
						lsETitle,
						laDlrTitleData.getForm31No(),
						laDlrTitleData.getFee());
					// end defect 9977

					laTotalFees =
						laTotalFees.add(laDlrTitleData.getFee());
					liNumOfRecsPrinted++;
				}
				handleBreak(1);
			}
			// end defect 10290 
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in formatReport() of "
					+ "com.txdot.isd.rts.services.reports.GenDealerTitlePreliminaryReport");
			aeException.printStackTrace(System.out);
		}

		// Print subtotals and grand totals
		printTotalFeesTrans(liNumOfRecsPrinted, laTotalFees);

		// defect 8628 
		generateFooter(true);
		// end defect 8628 
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Create Heading and column formats
	 * Set up column headings
	 *
	 * @param avDealerHeaderData Vector 
	 */
	public void generateHeaderColumns()
	{
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();

		// Adding Header Information
		lvHeader.add(DLRBATCHNOTEXT);

		lvHeader.add(csDlrBatchNo);

		Vector lvRow1 = new Vector();

		ReceiptTemplate laCharLngth = new ReceiptTemplate();

		// defect 9977
		// Add ETitle
		ColumnHeader laColumn1 =
			new ColumnHeader(
				ETITLETEXT,
				START_POS_COL_HDR_ETITLE,
				laCharLngth.charLength(ETITLETEXT));

		ColumnHeader laColumn2 =
			new ColumnHeader(
				FORM31NUMBERTEXT,
				START_POS_COL_HDR_FORM31,
				laCharLngth.charLength(FORM31NUMBERTEXT));

		ColumnHeader laColumn3 =
			new ColumnHeader(
				FEECALCUTEXT,
				START_POS_COL_HDR_FEES,
				laCharLngth.charLength(FEECALCUTEXT));

		lvRow1.add(laColumn1);
		lvRow1.add(laColumn2);
		lvRow1.add(laColumn3);
		lvTable.add(lvRow1);
		// end defect 9977

		generateHeader(lvHeader, cvDlrHeaderData, lvTable);
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
			generateHeaderColumns();
		}
	}

	/**
	 * Print Data line
	 * Print SKIP if applicable - Form31No.
	 *
	 * @param asETitle String
	 * @param asForm31No String
	 * @param aaFees Dollar
	 */
	private void printDataLine(
		String asETitle,
		String asForm31No,
		Dollar aaFees)
	{
		ReceiptTemplate laCharLngth = new ReceiptTemplate();

		// defect 10290 
		//  Handled w/ new vector content 
		//		// defect 9045
		//		// Print "SKIP" if 
		//		//		Current or Prev Form31No not empty 
		//		//		Current or Prev Form31No are not equal 
		//		//		Form31No Prev_Next numbers not in next sequential 
		//		//			order
		//		//		Not first record  
		//		// if (aiRecNo != 0 && isItmBreak(asForm31No))
		//		if ((!asForm31No.equals(SPACE_EMPTY)
		//			&& !csPrevForm31No.equals(SPACE_EMPTY))
		//			&& (asForm31No.compareTo(csPrevForm31No) != 0)
		//			&& MediaValidations.isItmBreak(csPrevForm31No, asForm31No)
		//			&& aiRecNo != 0)
		//		{
		//			this.caRpt.print(
		//				SKIP,
		//				START_POS_COL_DETAIL_SKIP,
		//				SKIP.length());
		//			this.caRpt.nextLine();
		//		}
		// end defect 10290 

		this.caRpt.print(
			asETitle,
			START_POS_COL_DETAIL_ETITLE,
			laCharLngth.charLength(asETitle));

		this.caRpt.print(
			asForm31No,
			START_POS_COL_DETAIL_SKIP,
			laCharLngth.charLength(asForm31No));

		// Add Fees to data line
		if (aaFees == null)
		{
			aaFees = new Dollar(STR_ZERO);
		}
		this.caRpt.rightAlign(
			aaFees.printDollar(),
			START_POS_COL_DETAIL_FEES,
			13);
		this.caRpt.blankLines(1);
	}

	/**
	 * Print Total Fees Information
	 *
	 * @param aiTotalRecords int
	 * @param aaTotalFees Dollar
	 */
	private void printTotalFeesTrans(
		int aiTotalRecords,
		Dollar aaTotalFees)
	{
		ReceiptTemplate laCharLngth = new ReceiptTemplate();
		this.caRpt.blankLines(2);

		this.caRpt.print(
			TOTALFEESTEXT,
			START_POS_COL_HDR_FORM31,
			laCharLngth.charLength(TOTALFEESTEXT));

		this.caRpt.rightAlign(
			aaTotalFees.printDollar(),
			START_POS_COL_DETAIL_FEES,
			13);

		this.caRpt.blankLines(1);
		this.caRpt.print(
			TOTALTRANSTEXT,
			START_POS_SUMMARY_TOTAL_TRANS,
			laCharLngth.charLength(TOTALTRANSTEXT));

		this.caRpt.rightAlign(
			String.valueOf(aiTotalRecords),
			START_POS_SUMMARY_TOTAL_NO_OF_RECORDS,
			laCharLngth.charLength(String.valueOf(aiTotalRecords)));
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 *
	 * @param asQuery String
	 * @return Vector 
	 */
	public Vector queryData(String asQuery)
	{
		Dollar laItmPrice1 = new Dollar(123.78);

		Vector lvResultsDealer = new Vector();

		// Define data objects for Dealer Data objects
		// defect 10112 
		DealerTitleData laDlrTtlData = new DealerTitleData();
		laDlrTtlData.setDealerSeqNo("001");
		laDlrTtlData.setForm31No("A000666");
		laDlrTtlData.setFee(laItmPrice1); // get Fees payed

		// defect 8626
		DealerData laDealerData = new DealerData();
		laDealerData.setId(001);
		laDealerData.setName1("AGC AUTOMOTIVE");
		AddressData laAddrData = laDealerData.getAddressData();
		laAddrData.setSt1("1313 MOCKINGBIRD LN");
		laAddrData.setCity("AZLE");
		laAddrData.setZpcd("76020");
		laAddrData.setZpcdp4("9999");
		laDealerData.setPhoneNo("(817)456-7891");
		laDealerData.setContact("JEFF RUE");
		// end defect 8626 

		// Add Dealer Data to the returned vector
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		lvResultsDealer.add(laDlrTtlData);
		// end defect 10112 
		return lvResultsDealer;
	}
}