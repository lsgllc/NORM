package com.txdot.isd.rts.services.reports.localoptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.LienholderData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenLienHolderReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	09/06/2001	New Class
 * Ray Rowehl	09/20/2001	Use setFooter and nextLine to properly 
 * 							position the generateFooter start.
 * Ray Rowehl	09/20/2001	Add constants for positioning
 * B Hargrove	01/08/2004	Change LIENHOLDERID_LENGTH from 5 to
 * 							3, and LIENHOLDERID_START_PT from 1 to 3 so 
 * 							that "ID" header lines up correctly.
 *                          Defect 6697. Rel 5.1.5 fix 2.
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3        
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	08/28/2008	Copied/Modified from GenDealerReport to 
 * 							incorporate non-USA Addresses
 * 							defect 8727 Ver Defect_POS_B  
 * K Harrell	03/07/3009	Incorporate LienholdersData.getAddressData()
 * 							modify formatReport(), queryData()
 * 							defect 9969 Ver Defect_POS_E        
 * K Harrell	08/10/2009	Implement new LienholderData 
 * 							modify formatReport(), queryData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify END_OF_PAGE_WHITESPACE 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add cbIdSort 	
 * 							add GenLienHolderReport(String,ReportProperties,
 * 							 boolean)
 * 							modify formatReport()
 * 							defect 10250 Ver Defect_POS_G           	
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the LienHolders List Report.
 *
 * @version	Defect_POS_G	10/13/2009
 * @author	Ray Rowehl
 * <br>Creation Date:		09/06/2001 07:35:55
 */
public class GenLienHolderReport extends ReportTemplate
{
	//	defect 10250
	private boolean cbIdSort;
	// end defect 10250  

	private static final String LIENHOLDER_REPORT_HEADER =
		"LIENHOLDER REPORT";
	private static final String LIENHOLDER_FILE_NAME =
		"C:\\LHLDRLST.RPT";

	private static final int LHLDRID_STARTPT = 3;
	private static final int LHLDRID_LENGTH = 5;
	private static final String LHLDRID_HEADER = "LHLDRID";

	private static final int LHLDRNAME_STARTPT = 12;
	private static final int LHLDRNAME_LENGTH = 30;

	private static final String LHLDRNAME_HEADER = "NAME";

	private static final int LHLDRADDR_STARTPT = 45;
	private static final int LHLDRADDR_LENGTH = 30;
	private static final String LHLDRADDR_HEADER = "ADDRESS";

	private static final int LHLDRCITY_STARTPT = 78;
	private static final int LHLDRCITY_LENGTH = 19;
	private static final String LHLDRCITY_HEADER = "CITY";

	private static final int LHLDRSTATE_CNTRY_STARTPT = 100;
	private static final int LHLDRSTATE_CNTRY_LENGTH = 11;
	private static final String LHLDRSTATE_CNTRY_HEADER = "STATE/CNTRY";

	private static final int LHLDRZIP_STARTPT = 114;
	private static final int LHLDRZIP_LENGTH = 5;
	private static final String LHLDRZIP_HEADER = "ZIP";

	private static final int LHLDRZP4_STARTPT = 124;
	private static final int LHLDRZP4_LENGTH = 4;
	private static final String LHLDRZP4_HEADER = "ZP+4";

	// defect 8628 
	private static final int END_OF_PAGE_WHITESPACE = 2; // 3;
	// end defect 8628 
	private static final int LINES_ONE = 1;

	/**
	 * GenLienHolderReport constructor
	 */
	public GenLienHolderReport()
	{
		super();
	}

	/**
	 * GenLienHolderReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenLienHolderReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * GenLienHolderReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 * @param abIdSort boolean  
	 */
	public GenLienHolderReport(
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

		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);
		lvRow1.addElement(laColumn3);
		lvRow1.addElement(laColumn4);
		lvRow1.addElement(laColumn5);
		lvRow1.addElement(laColumn6);
		lvRow1.addElement(laColumn7);

		lvTable.addElement(lvRow1);

		// format for Lienholder id so it has the leading zeroes
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		LienholderData laDataline = new LienholderData();
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
						// defect 10112 
						laDataline =
							(LienholderData) avResults.elementAt(i);

						String lsId =
							laThreeDigits.format(laDataline.getId());

						this.caRpt.print(
							lsId,
							LHLDRID_STARTPT,
							LHLDRID_LENGTH);
						this.caRpt.print(
							laDataline.getName1(),
							LHLDRNAME_STARTPT,
							LHLDRNAME_LENGTH);
						// end defect 10112 

						// defect 9969 
						// Use AddressData 
						AddressData laAddrData =
							laDataline.getAddressData();

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
							String lsCntryZip =
								laAddrData.getCntryZpcd();
							this.caRpt.print(
								lsCntryZip,
								LHLDRZIP_STARTPT,
								lsCntryZip.length());
						}
						liLineCount = 1;
						this.caRpt.blankLines(LINES_ONE);
						// close out line

						// defect 10112 
						// if either Name2 || St2 print 
						// on next line and assign 'liExtraLine'  
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
						// end defect 9969 

						this.caRpt.blankLines(LINES_ONE);
						// close out line and skip a blank line

						// update k (page count) with the current line number
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
			// defect 8628 
			//generateEndOfReport();
			//generateFooter();
			generateFooter(true);
			// end defect 8628  
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
			new ReportProperties(ReportConstant.LIENHOLDER_REPORT_ID);
		GenLienHolderReport laGenDlrRpt =
			new GenLienHolderReport(
				LIENHOLDER_REPORT_HEADER,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery =
			"Select * FROM RTS.RTS_LIENHOLDERS WHERE DELETEINDI =0";
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
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
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
		// This is faked lvData
		Vector lvData = new Vector();

		// defect 10112
		// Implement new LienholderData:  Name1, Name2, Id 
		// defect 9969 
		LienholderData laDataline = new LienholderData();
		AddressData laAddrData = new AddressData();
		laDataline.setAddressData(laAddrData);
		laDataline.setOfcIssuanceNo(1);
		laDataline.setSubstaId(1);
		laDataline.setId(1);
		laDataline.setName1("Ray's Fast Cars");
		laDataline.setName2("Fast Ray");

		laAddrData.setSt1("125 E 11 Street");
		laAddrData.setSt2(" ");
		laAddrData.setCity("Austin");
		laAddrData.setState("TX");
		laAddrData.setZpcd("78701");
		laAddrData.setZpcdp4(" ");
		lvData.addElement(laDataline);

		LienholderData laDataline1 = new LienholderData();
		AddressData laAddrData1 = new AddressData();
		laDataline1.setAddressData(laAddrData1);
		laDataline1.setOfcIssuanceNo(1);
		laDataline1.setSubstaId(1);
		laDataline1.setId(2);
		laDataline1.setName1("Steve's Slow Cars");
		laDataline1.setName2("");

		laAddrData.setSt1("125 E 11 Street");
		laAddrData.setSt2(" ");
		laAddrData.setCity("Austin");
		laAddrData.setState("NM");
		laAddrData.setZpcd("78701");
		laAddrData.setZpcdp4(" ");
		lvData.addElement(laDataline1);

		LienholderData laDataline2 = new LienholderData();
		AddressData laAddrData2 = new AddressData();
		laDataline2.setAddressData(laAddrData2);
		laDataline2.setOfcIssuanceNo(1);
		laDataline2.setSubstaId(1);
		laDataline2.setId(10);
		laDataline2.setName1("Jane's Junky Cars");
		laDataline2.setName2("Jazzy Jane");

		laAddrData2.setSt1("125 E 11 Street");
		laAddrData2.setSt2("Hard To Find");
		laAddrData2.setCity("Bubba Town");
		laAddrData2.setState("TX");
		laAddrData2.setZpcd("78701");
		laAddrData2.setZpcdp4("1234");
		lvData.addElement(laDataline2);
		// end defect 9969 
		// end defect 10112 

		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);
		lvData.addElement(laDataline2);

		return lvData;
	}
}
