package com.txdot.isd.rts.services.reports.accounting;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.data.FundsUpdateData;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * GenFundsRemittance.java
 *
 * (c) Texas Department of Transportation 2001
 *----------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/18/2002	Refer to method remarks
 * JRue/BTuls   05/02/2002	defect 3737
 * JRue/BTuls	05/07/2002	Sort added 
 * 							defect 3784
 * J Kwik		05/10/2002	sort due date ascending not descending
 * 							defect 3784  
 * J Rue		05/29/2002	Redesigned the for loop to
 * 							include the last record in the sorted
 * 							vector. Method formatReport(Vector)
 * 							defect 4117 
 * M Listberger 10/18/2002  Corrected code so 
 * 							"*** Void Transaction ***" appears as part
 * 							of the heading.  The logical was set up but
 * 							was not getting set to true in the case of a
 * 							void. Changed name of csVoid to cbVoid to 
 *                          conform to naming standards (b for boolean).
 * 							Removed nextLine() that was in
 * 							genreateHeadingsColumn when it was a void
 * 							transaction.  RTS I indicates that it is to
 * 							be single spaced.
 * 							defect 4690  
 * S Johnston	05/10/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3
 * K Harrell	05/19/2005	Renaming FundsUpdateData elements
 * 							defect 7899 Ver 5.2.3  
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify generateHeadingsColumns()
 *							defect 7896 Ver 5.2.3
 * K Harrell	10/20/2005	Java 1.4 work
 * 							defect 7896 Ver 5.2.3
 * K Harrell	08/17/2009	Cleanup. Implement meaningful constant 
 * 							names. 
 * 							modify formatReport(), 
 * 							 generateHeadingsColumns(), printDataLine()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	08/28/2009	More Cleanup. Discovered existing page break  
 * 							error.
 * 							add caFundsUpdate, cdPymntTotal, csTransId
 * 							add generatePageBreak(), generateHeader()
 * 							delete generateHeadingsColumns()
 * 							modify formatReport(), printFinalRptMessage() 
 * 							defect 8628 Ver Defect_POS_F  	
 *----------------------------------------------------------------------
 */
/**
 * prepares Funds Remittance Report
 * 
 * @version	Defect_POS_F	08/28/2009
 * @author	Jeff Rue
 * <br>Creation Date:		10/03/2001 07:35:55 
 */
public class GenFundsRemittance extends ReportTemplate
{
	private boolean cbPymtEFT = false;
	private boolean cbVoid = false;

	// defect 8628
	private FundsUpdateData caFundsUpdate = null;
	private Dollar cdPymntTotal = new Dollar(0);
	private String csTransId = new String();

	// Report Heading  
	private final static int PAYMENT_TOTAL_COL_START = 1;
	private final static int REQUESTED_BY_COL_START = 2;
	private final static int METHOD_OF_PAYMENT_COL_START = 8;
	private final static int DATE_COL_START = 10;

	// Column Heading and Detail 
	private final static int COL_HDR_FUNDS_REPORT_DATE_START = 33;
	private final static int COL_HDR_REPORTING_DATE_START = 53;
	private final static int COL_HDR_PAYMENT_AMT_COL_START = 73;
	private final static int COL_HDR_FUNDS_CATEGORY_START = 93;
	private final static int PAYMENT_AMT_COL_START = 63;
	private final static int TRANSACTION_ID_COL_START = 93;

	private final static int DOLLAR_LENGTH = 17;
	// end defect 8628 

	private final static String TRACE_NUMBER_TEXT = "Trace Number";
	private final static String TRANSID_TEXT = "Transaction ID";
	private final static String PAYMENT_TEXT = "Payment";
	private final static String COLON_TEXT = ": ";
	private final static String TOTAL_TEXT = "Total";
	private final static String DATE_TEXT = "Date";
	private final static String METHOD_EFT_TEXT = "Method: EFT (Suffix";
	private final static String METHOD_CHECK_TEXT = "Method: CHECK (No";
	private final static String CLOSE_PARA_TEXT = ")";
	private final static String REQUESTED_BY_TEXT = "Requested By";
	private final static String FUNDS_TEXT = "Funds";
	private final static String REPORTING_TEXT = "Reporting";
	private final static String REPORT_TEXT = "Report";
	private final static String AMOUNT_TEXT = "Amount";
	private final static String CATEGORY_TEXT = "Category";
	private final static String END_MSG_TEXT =
		"*** Please return a copy of this report"
			+ " with your check payment. ***";
	private final static String END_MSG_EFT_TEXT =
		"*** Please retain this report for your records ***";

	/**
	 * GenFundsRemittance default constructor
	 */
	public GenFundsRemittance()
	{
		super();
	}

	/**
	 * GenFundsRemittance(String, ReportProperties) constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenFundsRemittance(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Format the Remittance Report
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		CompleteTransactionData laCTData =
			(CompleteTransactionData) avResults.get(0);

		// defect 8628 
		// Reorganize for simplicity, handling of page break 
		csTransId = laCTData.getTransId();
		
		cbVoid = laCTData.getTransCode().substring(0, 4).equals("VOID");
		
		caFundsUpdate = laCTData.getFundsUpdate();

		int liNumberOfRecords = caFundsUpdate.getFundsDue().size();

		cdPymntTotal =
			totalPymnts(caFundsUpdate.getFundsDue(), liNumberOfRecords);

		generateHeader();

		for (int i = 0; i < liNumberOfRecords; i++)
		{
			FundsDueData laFundsDue =
				(FundsDueData) caFundsUpdate.getFundsDue().elementAt(i);

			printDataLine(
				laFundsDue.getFundsReportDate(),
				laFundsDue.getReportingDate(),
				laFundsDue.getRemitAmount(),
				laFundsDue.getFundsCategory());

			generatePageBreak(2);
		}

		printFinalRptMessage();

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
	 * Print the custom design header
	 *
	 */
	private void generateHeader()
	{
		// Build/Convert int to String
		String lsTraceNo =
			TRACE_NUMBER_TEXT
				+ COLON_TEXT
				+ String.valueOf(caFundsUpdate.getTraceNo());
		String lsTransId = TRANSID_TEXT + COLON_TEXT + csTransId;
		String lsPayment =
			PAYMENT_TEXT
				+ " "
				+ TOTAL_TEXT
				+ COLON_TEXT
				+ cdPymntTotal.printDollar();
		String lsDate =
			DATE_TEXT
				+ COLON_TEXT
				+ caFundsUpdate.getFundsPaymentDate();
		String lsMethod = "";
		String lsTransEmpId =
			REQUESTED_BY_TEXT
				+ COLON_TEXT
				+ caFundsUpdate.getTransEmpId();
		// Build and print the header
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(
			caRptProps.getOfficeIssuanceName()
				+ " ("
				+ caFundsUpdate.getOfcIssuanceNo()
				+ CLOSE_PARA_TEXT);
		caRpt.blankLines(1);

		// defec t8628 
		// Trace No
		caRpt.print(
			lsTraceNo,
			PAYMENT_TOTAL_COL_START,
			lsTraceNo.length());

		// Trans Id
		caRpt.print(
			lsTransId,
			TRANSACTION_ID_COL_START,
			lsTransId.length());
		caRpt.blankLines(1);

		// Void Notation if Applicable 
		if (cbVoid)
		{
			caRpt.center("*** VOID TRANSACTION ***");
		}
		else
		{
			caRpt.blankLines(1);
		}

		// Payment Total 
		caRpt.print(
			lsPayment,
			PAYMENT_TOTAL_COL_START,
			lsPayment.length());
		caRpt.blankLines(1);

		// Date 
		caRpt.print(lsDate, DATE_COL_START, lsDate.length());
		caRpt.blankLines(1);

		// Method 
		lsMethod =
			generateMethodOfPymntType(
				caFundsUpdate.getCheckNo(),
				caFundsUpdate.getAccountNoCd());
		caRpt.print(
			lsMethod,
			METHOD_OF_PAYMENT_COL_START,
			lsMethod.length());
		caRpt.blankLines(1);

		// Requested By 
		caRpt.print(
			lsTransEmpId,
			REQUESTED_BY_COL_START,
			lsTransEmpId.length());
		caRpt.blankLines(2);

		// Column Headers 
		// Line 1 
		caRpt.print(
			FUNDS_TEXT,
			COL_HDR_FUNDS_REPORT_DATE_START,
			FUNDS_TEXT.length());
		caRpt.blankLines(1);

		// Line 2
		caRpt.print(
			REPORT_TEXT,
			COL_HDR_FUNDS_REPORT_DATE_START,
			REPORT_TEXT.length());
		caRpt.print(
			REPORTING_TEXT,
			COL_HDR_REPORTING_DATE_START,
			REPORTING_TEXT.length());
		caRpt.print(
			PAYMENT_TEXT,
			COL_HDR_PAYMENT_AMT_COL_START,
			PAYMENT_TEXT.length());
		caRpt.print(
			FUNDS_TEXT,
			COL_HDR_FUNDS_CATEGORY_START,
			FUNDS_TEXT.length());
		caRpt.blankLines(1);

		// Line 3
		caRpt.print(
			DATE_TEXT,
			COL_HDR_FUNDS_REPORT_DATE_START,
			DATE_TEXT.length());
		caRpt.print(
			DATE_TEXT,
			COL_HDR_REPORTING_DATE_START,
			DATE_TEXT.length());
		caRpt.print(
			AMOUNT_TEXT,
			COL_HDR_PAYMENT_AMT_COL_START,
			AMOUNT_TEXT.length());
		caRpt.print(
			CATEGORY_TEXT,
			COL_HDR_FUNDS_CATEGORY_START,
			CATEGORY_TEXT.length());
		// end defect 8628 
		caRpt.blankLines(3);
	}

	/**
	 * Determine the type of payment method.
	 * 
	 * @param asCheckNo String:	Check number
	 * @param aiAccountNo int: Checking account number
	 * @return String
	 */
	private String generateMethodOfPymntType(
		String asCheckNo,
		int aiAccountNo)
	{
		String lsMethod = "";
		if (asCheckNo.equals(""))
		{
			String lsAccountNum = Integer.toString(aiAccountNo);
			lsMethod =
				METHOD_EFT_TEXT
					+ ": "
					+ lsAccountNum.substring(lsAccountNum.length() - 1)
					+ CLOSE_PARA_TEXT;
			cbPymtEFT = true;
		} // end if
		else
		{
			lsMethod =
				METHOD_CHECK_TEXT
					+ COLON_TEXT
					+ asCheckNo
					+ CLOSE_PARA_TEXT;
		} // end else
		return lsMethod;
	}

	/**
	 * Generate the page break with lines required
	 * 
	 * @param aiLinesRequired int
	 */
	private void generatePageBreak(int aiLinesRequired)
	{
		if (getNoOfDetailLines() < aiLinesRequired)
		{
			generateFooter();
			generateHeader();
		}
	}

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		//create new graph of report class
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.2311");
		GenFundsRemittance laGPR =
			new GenFundsRemittance(
				"FUNDSREMITTANCE VERIFICATION REPORT",
				laRptProps);
		//extract dummy data for display
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryResults = laGPR.queryData(lsQuery);
		laGPR.formatReport(lvQueryResults);
		//write completed report to local hard drive
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\RTS\\RPT\\FNDREM.TXT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "GenFundsRemittance");
			aeIOEx.printStackTrace(System.out);
		}
		laPout.print(laGPR.caRpt.getReport().toString());
		laPout.close();
		//display report in preview window
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RPT\\FNDREM.TXT");
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
			//print report
			//Process p = 
			//Runtime.getRuntime().exec("cmd.exe /c copy
			//c:\\TitlePkgRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "GenFundsRemittance");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Print the data line.
	 * 
	 * @param aaFundsReportDt RTSDate
	 * @param aaFundsReportingDt RTSDate
	 * @param aaPaymentAmt Dollar
	 * @param asFundsCategory String
	 */
	private void printDataLine(
		RTSDate aaFundsReportDt,
		RTSDate aaFundsReportingDt,
		Dollar aaPaymentAmt,
		String asFundsCategory)
	{
		// defect 8628 
		// Funds Report Date 
		caRpt.print(
			aaFundsReportDt.toString(),
			COL_HDR_FUNDS_REPORT_DATE_START,
			aaFundsReportDt.toString().length());

		// Funds Reporting Date   (ReportING Date )
		caRpt.print(
			aaFundsReportingDt.toString(),
			COL_HDR_REPORTING_DATE_START,
			aaFundsReportingDt.toString().length());

		// Payment Amount 
		caRpt.rightAlign(
			aaPaymentAmt.printDollar(),
			PAYMENT_AMT_COL_START,
			DOLLAR_LENGTH);

		// Funds Category
		caRpt.print(
			asFundsCategory,
			COL_HDR_FUNDS_CATEGORY_START,
			asFundsCategory.length());
		// end defect 8628 

		caRpt.blankLines(1);
	}

	/**
	 * Print final meaasge at end of report..
	 */
	private void printFinalRptMessage()
	{
		// defect 8628 
		generatePageBreak(4);
		// end defect 8628 
		
		caRpt.blankLines(3);
		if (cbPymtEFT)
		{
			caRpt.center(END_MSG_EFT_TEXT);
		}
		else
		{
			caRpt.center(END_MSG_TEXT);
		}
	}

	/**
	 * Test data
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		//create container
		Vector lvRemittance1 = new Vector();
		Vector lvResults = new Vector();
		Dollar laRemitt1 = new Dollar("1175.00");
		// Set RTS Date
		int liRTSDateType = 1;
		int liBeginDT = 20011021;
		int liEndDT = 20011017;
		RTSDate laPymntDt = new RTSDate(liRTSDateType, liBeginDT);
		RTSDate laRptDt = new RTSDate(liRTSDateType, liEndDT);
		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		// Build record 1
		FundsUpdateData laFundsUpdate1 = new FundsUpdateData();
		FundsDueData laFundsDue1 = new FundsDueData();
		laFundsUpdate1.setAccountNoCd(1234556789);
		laFundsUpdate1.setTraceNo(1022723);
		laFundsUpdate1.setFundsPaymentDate(laPymntDt);
		laFundsUpdate1.setCheckNo("4444");
		laFundsUpdate1.setTransEmpId("JEFFRUE");
		laFundsUpdate1.setOfcIssuanceNo(161);
		// Build Remittance from Funds Due data object	
		laFundsDue1.setRemitAmount(laRemitt1);
		laFundsDue1.setFundsCategory("TITLECOMP");
		laFundsDue1.setFundsReportDate(laRptDt);
		laFundsDue1.setReportingDate(laRptDt);
		for (int i = 0; i < 100; i++)
		{
			lvRemittance1.addElement(laFundsDue1);
		}

		laFundsUpdate1.setFundsDue(lvRemittance1);
		laTransData.setFundsUpdate(laFundsUpdate1);
		lvResults.addElement(laTransData);

		return lvResults;
	}

	/**
	 * Total payments
	 * 
	 * @param avFundsUpdate Vector
	 * @param aiNumOfRecs
	 * @return Dollar
	 */
	private Dollar totalPymnts(Vector avFundsPymnt, int aiNumOfRecs)
	{

		Dollar laPymntTotal = new Dollar("0.00");

		for (int i = 0; i < aiNumOfRecs; i++)
		{
			FundsDueData laFundsDue =
				(FundsDueData) avFundsPymnt.elementAt(i);
			laPymntTotal =
				laPymntTotal.add(laFundsDue.getRemitAmount());
		}
		return laPymntTotal;
	}
}
