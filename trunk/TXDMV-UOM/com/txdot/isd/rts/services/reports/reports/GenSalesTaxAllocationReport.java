package com.txdot.isd.rts.services.reports.reports;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.data.FundsPaymentData;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * GenSalesTaxAllocationReport.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * JRue			08/20/2002	Defect 4594, added code to bring in the
 * /BArredondo				correct amount of sales tax.
 * B Arredondo	08/23/2002	Added code to display sales tax amount 
 * 							as a dollar with commas and dollar sign.
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3  
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F    
 *----------------------------------------------------------------------
 */
/**
 * The Sales Tax Allocation class provides methods that are commonly
 * used to format report data.
 *
 * <p>(9/19/2001) Sales Tax Allocation will print a grand total of 
 * accumulate sales tax collected for a user input county number and 
 * report date.  The report date is set to the last day of the month.
 *
 * @version	Defect_POS_F 	08/10/2009
 * @author	Jeff Rue
 * <br>Creation Date:		09/24/2001 10:29:28
 */
public class GenSalesTaxAllocationReport extends ReportTemplate
{
	private final static String ssComptMessage =
		"Transfer to Comptroller Provided Form 14 - 115";
	private final static String ssFilingPeroidText = "Filing Period";
	private final static String ssGrossTaxCollectionText =
		"Gross Motor Vehicle Sales and Use Tax Collected (enter on line 2)";

	/**
	 * GenQuickCounterReport constructor
	 */
	public GenSalesTaxAllocationReport()
	{
		super();
	}

	/**
	 * GenQuickCounterReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenSalesTaxAllocationReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * The formatReport method processes the data and prints the report.
	 * There is only one line of data to be processed
	 * 
	 * @param avResults Vector  
	 */
	public void formatReport(Vector avResults)
	{
		try
		{
			if (avResults == null || avResults.size() == 0)
			{
				noRecordsFoundMsg();
				return;
			}
			// Initialize index for the first record
			// int liFirstRecord = 0;

			// Intiialize String stuff
			String lsFundsCategory = "SLSTX";
			RTSDate laFilingDate = new RTSDate();
			Dollar laSalesTaxAllocationAmt = new Dollar("0.00");
			// Define data object
			FundsDueData laDataLine = new FundsDueData();

			// Find the sales tax Allocation amount from the input vector
			for (int i = 0; i < avResults.size(); i++)
			{
				laDataLine = (FundsDueData) avResults.elementAt(i);
				if (laDataLine
					.getFundsCategory()
					.equals(lsFundsCategory))
				{
					laFilingDate = laDataLine.getReportingDate();
					// Report filing period
					laSalesTaxAllocationAmt =
						laDataLine.getEntDueAmount();
					// Total sales tax allocation amount
				} // End if
			} // End for loop

			// Print Header/Columns
			generateHeaderColumns();

			// Print Comptroller message
			caRpt.print(ssComptMessage, 1, 60);
			caRpt.blankLines(2);

			// Print filing period
			caRpt.print(ssFilingPeroidText, 1, 15);
			caRpt.rightAlign(laFilingDate.toString(), 85, 15);
			caRpt.blankLines(2);

			// Print sales tax collected
			caRpt.print(ssGrossTaxCollectionText, 1, 70);
			caRpt.rightAlign(
				laSalesTaxAllocationAmt.printDollar(),
				85,
				15);
			caRpt.blankLines(1);

			// defect 8628         
			// generateEndOfReport();
			// generateFooter();
			generateFooter(true); 
			// end defect 8628
			 
		} // end try
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in formatReport() of "
					+ "com.txdot.isd.rts.services.reports.SalesTaxAllocationReport");
			aeEx.printStackTrace(System.out);
		} // end catch block
	}

	/**
	 * Generate Attributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * The headerColumn method adds text that will be printed when needed.
	 * The vectors lvHeader and lvTable will be null.
	 */
	public void generateHeaderColumns()
	{
		// No additional header information or 
		// columns heading for Sales Tax Allocation Report.
		Vector lvHeader = new Vector();
		// Adding header information, set to null
		Vector lvTable = new Vector();
		// Adding column information, set to null
		// Print header 
		generateHeader(lvHeader, lvTable);
	}

	/**
	 * Gen Sales Tax Allocatiion
	 */
	public void GenSalesTaxAllocatiion()
	{
		// empty code block
	}

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5061");
		GenSalesTaxAllocationReport laGPR =
			new GenSalesTaxAllocationReport(
				"SALES TAX ALLOCATION REPORT",
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = "DUMMY STRING PASSED TO A METHOD";
		Vector lvQueryResults = laGPR.queryData(lsQuery);
		laGPR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\SalesTaxRpt.txt");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGPR.caRpt.getReport().toString());
		laPout.close();

		// Formatting the report
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\SalesTaxRpt.txt");
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
			// "cmd.exe /c copy c:\\SalesTaxRpt.txt prn");
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
	 * no Records Found Msg
	 */
	public void noRecordsFoundMsg()
	{
		// Print Header/Columns
		generateHeaderColumns();
		caRpt.center("A FUNDS RECORD CANNOT BE FOUND.");
		
		// defect 8628 
		//generateEndOfReport();
		//generateFooter();
		generateFooter(true); 
		// end defect 8628
	}
	
	/**
	 * queryData method simulates the data into a vector for testing.
	 * report data. 
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		FundsPaymentData laDataLine = new FundsPaymentData();

		Vector lvResults = new Vector();

		Dollar laSalesTaxAlloctionAmt = new Dollar("2770461.54");
		RTSDate laSalesTaxAllocationDate = new RTSDate(2001, 10, 30);

		// Generating Demo data to display.	
		laDataLine.setTotalPaymentAmount(laSalesTaxAlloctionAmt);
		laDataLine.setReportingDate(laSalesTaxAllocationDate);
		lvResults.addElement(laDataLine);

		return lvResults;
	}
}
