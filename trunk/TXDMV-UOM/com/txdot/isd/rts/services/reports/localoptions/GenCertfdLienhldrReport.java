package com.txdot.isd.rts.services.reports.localoptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.CertifiedLienholderData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenCertfdLienhldrReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Zwiener	03/03/2009	New Class for Certified Lienholder Report
 * 							defect  Ver Defect_POS_E
 * K Harrell	07/12/2009	Implement new CertifiedLienholderData
 * 							modify formatReport()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean)
 * 							modify END_OF_PAGE_WHITESPACE 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add cbIdSort 	
 * 							add GenCertfdLienhldrReport(String,ReportProperties,
 * 							 boolean)
 * 							modify formatReport()
 * 							defect 10250 Ver Defect_POS_G     
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Certified Lienholder Report.
 *
 * @version	Defect_POS_G 	10/13/2009
 * @author	Jim Zwiener
 * <br>Creation Date:		03/03/2009
 */
public class GenCertfdLienhldrReport extends ReportTemplate
{
	// defect 10250
	private boolean cbIdSort;
	// end defect 10250  

	private static final String LIENHOLDER_REPORT_HEADER =
		"CERTIFIED LIENHOLDER REPORT";
	private static final String LIENHOLDER_FILE_NAME =
		"C:\\CERTLIEN.RPT";

	private static final int LHLDRID_STARTPT = 3;
	private static final int LHLDRID_LENGTH = 11;
	private static final String LHLDRID_HEADER = "LHLDR ID";

	private static final int LHLDRNAME_STARTPT = 17;
	private static final int LHLDRNAME_LENGTH = 30;
	private static final String LHLDRNAME_HEADER = "NAME";

	private static final int LHLDRADDR_STARTPT = 50;
	private static final int LHLDRADDR_LENGTH = 30;
	private static final String LHLDRADDR_HEADER = "ADDRESS";

	private static final int LHLDRCITY_STARTPT = 83;
	private static final int LHLDRCITY_LENGTH = 19;
	private static final String LHLDRCITY_HEADER = "CITY";

	private static final int LHLDRSTATE_CNTRY_STARTPT = 105;
	private static final int LHLDRSTATE_CNTRY_LENGTH = 11;
	private static final String LHLDRSTATE_CNTRY_HEADER = "STATE/CNTRY";

	private static final int LHLDRZIP_STARTPT = 119;
	private static final int LHLDRZIP_LENGTH = 5;
	private static final String LHLDRZIP_HEADER = "ZIP";

	private static final int LHLDRZP4_STARTPT = 127;
	private static final int LHLDRZP4_LENGTH = 4;
	private static final String LHLDRZP4_HEADER = "ZP+4";

	private static final int LHLDRETTL_STARTPT = 134;
	private static final int LHLDRETTL_LENGTH = 4;
	private static final String LHLDRETTL_HEADER = "ETTL";

	private static final int LHLDREFFDATE_STARTPT = 141;
	private static final int LHLDREFFDATE_LENGTH = 8;
	private static final String LHLDREFFDATE_HEADER = "EFF DATE";

	private static final int LHLDRENDDATE_STARTPT = 152;
	private static final int LHLDRENDDATE_LENGTH = 8;
	private static final String LHLDRENDDATE_HEADER = "END DATE";

	// defect 8628
	// Set to the max no lines for Certified Lienholder  	
	private static final int END_OF_PAGE_WHITESPACE = 2;
	// end defect 8628 

	private static final int LINES_ONE = 1;

	/**
	 * GenCertfdLienhldrReport constructor
	 */
	public GenCertfdLienhldrReport()
	{
		super();
	}

	/**
	 * GenCertfdLienhldrReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenCertfdLienhldrReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * GenCertfdLienhldrReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 * @param abIdSort boolean  
	 */
	public GenCertfdLienhldrReport(
		String asRptName,
		ReportProperties aaRptProps,
		boolean abIdSort)
	{
		super(asRptName, aaRptProps);
		cbIdSort = abIdSort;
	}

	/**
	 * Format the data into the report
	 *
	 * @param avResults Vector 
	 */
	public void formatReport(Vector avResults)
	{
		Vector lvHeader = new Vector();
		
		// defect 10250 
		lvHeader.addElement(ReportConstant.SORT_ORDER_HDG);
		lvHeader.addElement(
			cbIdSort
				? ReportConstant.SORT_ORDER_ID
				: ReportConstant.SORT_ORDER_NAME);
		// end defect 10250 

		// vector to contain additional heading information
		// no additional heading information needed.  empty vector
		Vector lvTable = new Vector();
		// vector to contain rows of column headings
		Vector lvRow1 = new Vector();
		// vector for the first row of column headings
		// there is only one row.
		// column header for Lienholder id
		ColumnHeader laColumn1 =
			new ColumnHeader(
				LHLDRID_HEADER,
				LHLDRID_STARTPT,
				LHLDRID_LENGTH);
		// column header for Lienholder name and phone number
		ColumnHeader laColumn2 =
			new ColumnHeader(
				LHLDRNAME_HEADER,
				LHLDRNAME_STARTPT,
				LHLDRNAME_LENGTH);
		// column header for Lienholder address and contact name
		ColumnHeader laColumn3 =
			new ColumnHeader(
				LHLDRADDR_HEADER,
				LHLDRADDR_STARTPT,
				LHLDRADDR_LENGTH);
		// column header for Lienholder city
		ColumnHeader laColumn4 =
			new ColumnHeader(
				LHLDRCITY_HEADER,
				LHLDRCITY_STARTPT,
				LHLDRCITY_LENGTH);

		// column header for State/Cntry 
		ColumnHeader laColumn5 =
			new ColumnHeader(
				LHLDRSTATE_CNTRY_HEADER,
				LHLDRSTATE_CNTRY_STARTPT,
				LHLDRSTATE_CNTRY_LENGTH);

		// column header for Lienholder zipcode
		ColumnHeader laColumn6 =
			new ColumnHeader(
				LHLDRZIP_HEADER,
				LHLDRZIP_STARTPT,
				LHLDRZIP_LENGTH);

		// column header for Lienholder zip plus 4
		ColumnHeader laColumn7 =
			new ColumnHeader(
				LHLDRZP4_HEADER,
				LHLDRZP4_STARTPT,
				LHLDRZP4_LENGTH);

		// column header for ETTL
		ColumnHeader laColumn8 =
			new ColumnHeader(
				LHLDRETTL_HEADER,
				LHLDRETTL_STARTPT,
				LHLDRETTL_LENGTH);

		// column header for RTS Effective Date
		ColumnHeader laColumn9 =
			new ColumnHeader(
				LHLDREFFDATE_HEADER,
				LHLDREFFDATE_STARTPT,
				LHLDREFFDATE_LENGTH);

		// column header for RTS End Date
		ColumnHeader laColumn10 =
			new ColumnHeader(
				LHLDRENDDATE_HEADER,
				LHLDRENDDATE_STARTPT,
				LHLDRENDDATE_LENGTH);

		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);
		lvRow1.addElement(laColumn3);
		lvRow1.addElement(laColumn4);
		lvRow1.addElement(laColumn5);
		lvRow1.addElement(laColumn6);
		lvRow1.addElement(laColumn7);
		lvRow1.addElement(laColumn8);
		lvRow1.addElement(laColumn9);
		lvRow1.addElement(laColumn10);

		lvTable.addElement(lvRow1);

		CertifiedLienholderData laDataline =
			new CertifiedLienholderData();
		AddressData laAddrData = new AddressData();

		// data object for a row of Lienholder data
		int i = 0; // i will be used to get each object.
		if (!(avResults == null) && (avResults.size() > 0))
		{
			while (i < avResults.size()) //Loop through the results
			{
				generateHeader(lvHeader, lvTable);
				// create the heading area using the additional header 
				// information and column headers
				int j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;
				// Compute the number of available
				// lines for detail on the page
				for (int k = 0; k <= j; k++)
				{
					int liLineCount = 0;
					if (i < avResults.size())
					{
						laDataline =
							(
								CertifiedLienholderData) avResults
									.elementAt(
								i);
						laAddrData = laDataline.getAddressData();
						this.caRpt.print(
							laDataline.getPermLienHldrId(),
							LHLDRID_STARTPT,
							LHLDRID_LENGTH);

						// defect 10112
						this.caRpt.print(
							laDataline.getName1(),
							LHLDRNAME_STARTPT,
							LHLDRNAME_LENGTH);
						// end defect 10112  

						this.caRpt.print(
							laAddrData.getSt1(),
							LHLDRADDR_STARTPT,
							LHLDRADDR_LENGTH);
						this.caRpt.print(
							laAddrData.getCity(),
							LHLDRCITY_STARTPT,
							LHLDRCITY_LENGTH);

						if (laAddrData.isUSA())
						{
							this.caRpt.print(
								laAddrData.getState(),
								LHLDRSTATE_CNTRY_STARTPT,
								LHLDRSTATE_CNTRY_LENGTH);
							this.caRpt.print(
								laAddrData.getZpcd(),
								LHLDRZIP_STARTPT,
								LHLDRZIP_LENGTH);
							this.caRpt.print(
								laAddrData.getZpcdp4(),
								LHLDRZP4_STARTPT,
								LHLDRZP4_LENGTH);
						}
						else
						{
							this.caRpt.print(
								laAddrData.getCntry(),
								LHLDRSTATE_CNTRY_STARTPT,
								LHLDRSTATE_CNTRY_LENGTH);
							this.caRpt.print(
								laAddrData.getCntryZpcd(),
								LHLDRZIP_STARTPT,
								laAddrData.getCntryZpcd().length());
						}

						this.caRpt.print(
							(laDataline.isElienRdy() ? "Y" : " "),
							LHLDRETTL_STARTPT,
							LHLDRETTL_LENGTH);
						this.caRpt.print(
							"" + laDataline.getRTSEffDate(),
							LHLDREFFDATE_STARTPT,
							LHLDREFFDATE_LENGTH);
						this.caRpt.print(
							"" + laDataline.getRTSEffEndDate(),
							LHLDRENDDATE_STARTPT,
							LHLDRENDDATE_LENGTH);

						liLineCount = 1;
						this.caRpt.blankLines(LINES_ONE);
						// close out line

						// if either LienholderName2 || Street2 print 
						// on next line and assign 'liExtraLine'
						// defect 10112   
						if (laDataline.getName2().trim().length() != 0
							|| laAddrData.getSt2().trim().length() != 0)
						{
							this.caRpt.print(
								laDataline.getName2(),
								LHLDRNAME_STARTPT,
								LHLDRNAME_LENGTH);
							// end defect 10112 

							this.caRpt.print(
								laAddrData.getSt2(),
								LHLDRADDR_STARTPT,
								LHLDRADDR_LENGTH);
							this.caRpt.blankLines(LINES_ONE);
							liLineCount = liLineCount + 1;
						}

						this.caRpt.blankLines(LINES_ONE);
						// close out line and skip a blank line

						// update k (page count) with 
						//   the current line number
						k = k + liLineCount;

						// increment i to to get next record
						i = i + 1;
					}
				}
				// defect 8628 
				//	if (i >= avResults.size())
				//		// if i is equal to or greater than results size
				//		// then we are out of data.  Print end of report. 
				//	{
				//		this.caRpt.nextLine();
				//		generateEndOfReport();
				//	}
				//	generateFooter();
				generateFooter(i >= avResults.size());
				// end defect 8628 
			}
		}
		// there was no data returned.
		// so print the no records found message 
		else
		{
			generateHeader(lvHeader, lvTable);
			generateNoRecordFoundMsg();
			generateFooter(true);
		}
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		this.caRpt.printAttributes(
			"THIS NEEDS TO BE REPLACED WITH PRINT STRING");
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
			new ReportProperties(
				ReportConstant.CERTFD_LIENHLDR_REPORT_ID);
		GenLienHolderReport laGenDlrRpt =
			new GenLienHolderReport(
				LIENHOLDER_REPORT_HEADER,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS.RTS_CERTFD_LIENHLDR";
		Vector lvQueryResults = laGenDlrRpt.queryData(lsQuery);
		laGenDlrRpt.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream lpfsFOut;
		PrintStream laPrntOut = null;

		try
		{
			laOutputFile = new File(LIENHOLDER_FILE_NAME);
			lpfsFOut = new FileOutputStream(laOutputFile);
			laPrntOut = new PrintStream(lpfsFOut);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPrntOut.print(laGenDlrRpt.caRpt.getReport().toString());
		laPrntOut.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(LIENHOLDER_FILE_NAME);
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
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\TitlePkgRpt.txt prn");

		}
		catch (Throwable aeException)
		{
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Run query and return results.
	 *
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		return new Vector();
	}
}
