package com.txdot.isd.rts.services.reports.localoptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenDealerReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	09/06/2001	New Class
 * Ray Rowehl	09/21/2001	Add Constants for positioning
 * B Hargrove	01/08/2004	Change DLRCITY_LENGTH from 18 to 19.
 *                          defect 6393. Rel 5.1.5 Fix 2
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3     
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	07/12/2005  JavaDoc/Formatting/Variable Name Cleanup
 * 							Format Phone Number as (XXX)YYY-YYYY
 *							modify formatReport() 
 *							defect 7861  Ver 5.2.3   
 * K Harrell	05/20/2008	Add Column for State/Cntry after City
 * 							add DLRSTATE_CNTRY_HEADER,
 * 							  DLRSTATE_CNTRY_STARTPT, 
 * 							  DLRSTATE_CNTRY_LENGTH
 * 							delete LINES_TWO
 * 							modify DLRNAME_HEADER, starting points as 
 * 							  appropriate
 * 							modify formatReport()
 * 							defect 9654 Ver Defect POS A
 * K Harrell	07/12/2009	Implement new DealerData() 
 * 							modify formatReport(), queryData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add cbIdSort 	
 * 							add GenDealerReport(String,ReportProperties,
 * 							 boolean)
 * 							modify formatReport()
 * 							defect 10250 Ver Defect_POS_G               
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Dealer List Report.
 *
 * @version	Defect_POS_G	10/13/2009
 * @author	Ray Rowehl
 * <br>Creation Date:		09/06/2001 07:35:55
 */
public class GenDealerReport extends ReportTemplate
{
	// defect 10250
	private boolean cbIdSort;
	// end defect 10250  
	
	private static final String DEALER_REPORT_HEADER = "DEALER REPORT";
	private static final String DEALER_FILE_NAME = "C:\\DLRLST.RPT";

	// defect 9654
	private static final int DLRID_STARTPT = 3; //6 
	private static final int DLRID_LENGTH = 5;
	private static final String DLRID_HEADER = "DLRID";

	private static final int DLRNAME_STARTPT = 12; // 17 
	private static final int DLRNAME_LENGTH = 30;

	private static final String DLRNAME_HEADER = "NAME/PHONE NUMBER";

	private static final int DLRADDR_STARTPT = 45; //49 
	private static final int DLRADDR_LENGTH = 30;
	private static final String DLRADDR_HEADER =
		"ADDRESS/CONTACT PERSON";

	private static final int DLRCITY_STARTPT = 78; // 80
	private static final int DLRCITY_LENGTH = 19;
	private static final String DLRCITY_HEADER = "CITY";

	private static final int DLRSTATE_CNTRY_STARTPT = 100;
	private static final int DLRSTATE_CNTRY_LENGTH = 11;
	private static final String DLRSTATE_CNTRY_HEADER = "STATE/CNTRY";

	private static final int DLRZIP_STARTPT = 114; // 100
	private static final int DLRZIP_LENGTH = 5;
	private static final String DLRZIP_HEADER = "ZIP";

	private static final int DLRZP4_STARTPT = 124; // 108
	private static final int DLRZP4_LENGTH = 4;
	private static final String DLRZP4_HEADER = "ZP+4";

	private static final int END_OF_PAGE_WHITESPACE = 3; // 2
	private static final int LINES_ONE = 1;
	//private static final int LINES_TWO = 2;
	// end defect 9654

	/**
	 * GenDealerReport constructor
	 */
	public GenDealerReport()
	{
		super();
	}

	/**
	 * GenDealerReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenDealerReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}
	
		/**
		 * GenDealerReport constructor
		 * 
		 * @param asRptName String
		 * @param aaRptProps ReportProperties
		 * @param abIdSort boolean  
		 */
		public GenDealerReport(
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
		// column header for dealer id
		ColumnHeader laColumn1 =
			new ColumnHeader(DLRID_HEADER, DLRID_STARTPT, DLRID_LENGTH);
		// column header for dealer name and phone number
		ColumnHeader laColumn2 =
			new ColumnHeader(
				DLRNAME_HEADER,
				DLRNAME_STARTPT,
				DLRNAME_LENGTH);
		// column header for dealer address and contact name
		ColumnHeader laColumn3 =
			new ColumnHeader(
				DLRADDR_HEADER,
				DLRADDR_STARTPT,
				DLRADDR_LENGTH);
		// column header for dealer city
		ColumnHeader laColumn4 =
			new ColumnHeader(
				DLRCITY_HEADER,
				DLRCITY_STARTPT,
				DLRCITY_LENGTH);

		// defect 9654 
		// column header for State/Cntry 
		ColumnHeader laColumn5 =
			new ColumnHeader(
				DLRSTATE_CNTRY_HEADER,
				DLRSTATE_CNTRY_STARTPT,
				DLRSTATE_CNTRY_LENGTH);

		// column header for dealer zipcode
		ColumnHeader laColumn6 =
			new ColumnHeader(
				DLRZIP_HEADER,
				DLRZIP_STARTPT,
				DLRZIP_LENGTH);

		// column header for dealer zip plus 4
		ColumnHeader laColumn7 =
			new ColumnHeader(
				DLRZP4_HEADER,
				DLRZP4_STARTPT,
				DLRZP4_LENGTH);

		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);
		lvRow1.addElement(laColumn3);
		lvRow1.addElement(laColumn4);
		lvRow1.addElement(laColumn5);
		lvRow1.addElement(laColumn6);
		lvRow1.addElement(laColumn7);
		// end defect 9654 

		lvTable.addElement(lvRow1);

		// format for dealer id so it has the leading zeroes
		DecimalFormat laThreeDigits = new DecimalFormat("000");
		
		// defect 10112
		// data object for a row of dealer data  
		DealerData laDataline = new DealerData();
		
		// i will be used to get each object.
		int i = 0; 
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
							(DealerData) avResults.elementAt(i);
						String lsId =
							laThreeDigits.format(
								laDataline.getId());
						this.caRpt.print(
							lsId,
							DLRID_STARTPT,
							DLRID_LENGTH);
						this.caRpt.print(
							laDataline.getName1(),
							DLRNAME_STARTPT,
							DLRNAME_LENGTH);
						this.caRpt.print(
							laDataline.getAddressData().getSt1(),
							DLRADDR_STARTPT,
							DLRADDR_LENGTH);
						this.caRpt.print(
							laDataline.getAddressData().getCity(),
							DLRCITY_STARTPT,
							DLRCITY_LENGTH);

						// defect 9654 
						if (laDataline.getAddressData().isUSA())
						{
							if (laDataline
								.getAddressData().getState()
								.trim()
								.length()
								== 0)
							{
								laDataline.getAddressData().setState(
									CommonConstant.STR_TX);
							}
							this.caRpt.print(
								laDataline.getAddressData().getState(),
								DLRSTATE_CNTRY_STARTPT,
								DLRSTATE_CNTRY_LENGTH);
							this.caRpt.print(
								laDataline.getAddressData().getZpcd(),
								DLRZIP_STARTPT,
								DLRZIP_LENGTH);
							this.caRpt.print(
								laDataline.getAddressData().getZpcdp4(),
								DLRZP4_STARTPT,
								DLRZP4_LENGTH);
						}
						else
						{
							this.caRpt.print(
								laDataline.getAddressData().getCntry(),
								DLRSTATE_CNTRY_STARTPT,
								DLRSTATE_CNTRY_LENGTH);
							String lsCntryZip =
								laDataline.getAddressData().getCntryZpcd();
								
							this.caRpt.print(
								lsCntryZip,
								DLRZIP_STARTPT,
								lsCntryZip.length());
						}
						liLineCount = 1;
						this.caRpt.blankLines(LINES_ONE);
						// close out line

						// if either DealerName2 || Street2 print 
						// on next line and assign 'liExtraLine'  
						if (laDataline.getName2().trim().length()
							!= 0
							|| laDataline.getAddressData().getSt2().trim().length()
								!= 0)
						{
							this.caRpt.print(
								laDataline.getName2(),
								DLRNAME_STARTPT,
								DLRNAME_LENGTH);
							this.caRpt.print(
								laDataline.getAddressData().getSt2(),
								DLRADDR_STARTPT,
								DLRADDR_LENGTH);
							this.caRpt.blankLines(LINES_ONE);
							liLineCount = liLineCount + 1;
						}

						// defect 7861
						// format Dealer Phone No 
						if (laDataline
							.getPhoneNo()
							.trim()
							.length()
							!= 0
							&& laDataline
								.getContact()
								.trim()
								.length()
								!= 0)
						{

							String lsDealerPhoneNo =
								UtilityMethods.addPadding(
									laDataline.getPhoneNo(),
									10,
									" ");
							if (lsDealerPhoneNo.trim().length() > 0)
							{
								lsDealerPhoneNo =
									"("
										+ lsDealerPhoneNo.substring(0, 3)
										+ ")"
										+ lsDealerPhoneNo.substring(3, 6)
										+ "-"
										+ lsDealerPhoneNo.substring(6);
							}
							// end defect 10112 
							
							this.caRpt.print(
							//laDataline.getDealerPhoneNo(),
							lsDealerPhoneNo,
								DLRNAME_STARTPT,
								DLRNAME_LENGTH);
							// end defect 7861

							this.caRpt.print(
								laDataline.getContact(),
								DLRADDR_STARTPT,
								DLRADDR_LENGTH);
							this.caRpt.blankLines(LINES_ONE);
							liLineCount = liLineCount + 1;
						}
						this.caRpt.blankLines(LINES_ONE);
						// close out line and skip a blank line

						// update k (page count) with the current line number
						k = k + liLineCount;
						// end defect 9654 

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
			new ReportProperties(ReportConstant.DEALER_REPORT_ID);
		GenDealerReport laGenDlrRpt =
			new GenDealerReport(DEALER_REPORT_HEADER, laRptProps);

		// Generating Demo data to display.
		String lsQuery =
			"Select * FROM RTS.RTS_DEALERS WHERE DELETEINDI =0";
		Vector lvQueryResults = laGenDlrRpt.queryData(lsQuery);
		laGenDlrRpt.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream lpfsFOut;
		PrintStream laPrntOut = null;

		try
		{
			laOutputFile = new File(DEALER_FILE_NAME);
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
			laFrmPreviewReport = new FrmPreviewReport(DEALER_FILE_NAME);
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
		// defect 10112 
		Vector lvData = new Vector();

		DealerData laDealerData = new DealerData();
		laDealerData.setOfcIssuanceNo(1);
		laDealerData.setSubstaId(1);
		laDealerData.setId(001);
		laDealerData.setName1("Ray's Fast Cars");
		laDealerData.getAddressData().setSt1("125 E 11 Street");
		laDealerData.getAddressData().setCity("Austin");
		laDealerData.getAddressData().setZpcd("78701");
		laDealerData.getAddressData().setZpcdp4(" ");
		laDealerData.setPhoneNo("5124657501");
		laDealerData.setContact("Fast Ray Rowehl");
		lvData.addElement(laDealerData);

		DealerData laDealerData1 = new DealerData();
		laDealerData1.setOfcIssuanceNo(1);
		laDealerData1.setSubstaId(1);
		laDealerData1.setId(002);
		laDealerData1.setName1("Sam's Slow Cars");
		laDealerData1.getAddressData().setSt1("125 E 11 Street");
		laDealerData1.getAddressData().setCity("Austin");
		laDealerData1.getAddressData().setZpcd("78701");
		laDealerData1.getAddressData().setZpcdp4(" ");
		laDealerData1.setPhoneNo("5124657501");
		laDealerData1.setContact("Slow Sam");
		lvData.addElement(laDealerData1);

		DealerData laDealerData2 = new DealerData();
		laDealerData2.setOfcIssuanceNo(1);
		laDealerData2.setSubstaId(1);
		laDealerData2.setId(010);
		laDealerData2.setName1("Jane's Junky Cars");
		laDealerData2.getAddressData().setSt1("125 E 11 Street");
		laDealerData2.getAddressData().setCity("Austin");
		laDealerData2.getAddressData().setZpcd("78701");
		laDealerData2.getAddressData().setZpcdp4("1234");
		laDealerData2.setPhoneNo("5124657501");
		laDealerData2.setContact("Jazzy Jane");
		// end defect 10112 
		
		lvData.addElement(laDealerData2);

		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);
		lvData.addElement(laDealerData2);

		return lvData;
	}
}
