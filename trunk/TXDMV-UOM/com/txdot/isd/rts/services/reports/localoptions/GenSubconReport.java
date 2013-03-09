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
import com.txdot.isd.rts.services.data.SubcontractorData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenSubconReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	09/06/2001	New Class
 * Ray Rowehl	09/21/2001 	Add constants for positioning
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3        
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	09/09/2008	Add headings to columns. Add "final static" 
 * 							to variables.  
 * 							add SUBCONADDRESS_HEADER, SUBCON_CITY_HEADER,
 * 							 SUBCONZPCD_HEADER. 
 * 							modify SUBCONNAME_HEADER 
 * 							modify formatReport()
 * 							defect 9812 Ver Defect_POS_B
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add cbIdSort 	
 * 							add GenSubconReport(String,ReportProperties,
 * 							 boolean)
 * 							modify formatReport()
 * 							defect 10250 Ver Defect_POS_G 
 * K Harrell	02/19/2010	Implement new SubcontractorData. Add State, 
 * 							 ZP+4 column 
 * 							add SUBCONSTATE_HEADER, SUBCONSTATE_LENGTH,
 * 							 SUBCONZP4_HEADER, SUBCONSTATE_STARTPT,
 * 							 SUBCONZP4_STARTPT
 * 							modify SUBCONZPCD_STARTPT
 * 							delete SUBCONZPDASH_VALUE, 
 * 							 SUBCONZPDASH_STARTPT, SUBCONZPDASH_LENGTH 
 * 							modify formatReport(), queryData()
 * 							defect 10161 Ver POS_640                    
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Subcontractor List Report.
 *
 * @version	POS_640			02/19/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		09/06/2001 14:35:55
 */
public class GenSubconReport extends ReportTemplate
{
	// defect 10250
	private boolean cbIdSort;
	// end defect 10250  

	private final static int SUBCONID_STARTPT = 4;
	private final static int SUBCONID_LENGTH = 3;
	private final static int SUBCONNAME_STARTPT = 10;
	private final static int SUBCONNAME_LENGTH = 30;
	private final static int SUBCONSTREET_STARTPT = 45;
	private final static int SUBCONSTREET_LENGTH = 30;
	private final static int SUBCONCITY_STARTPT = 80;
	private final static int SUBCONCITY_LENGTH = 20;
	private final static int SUBCONZPCD_LENGTH = 5;
	private final static int SUBCONZP4_LENGTH = 4;
	private final static String SUBCONID_HEADER = "ID";
	private final static String SUBCONNAME_HEADER = "NAME";
	private final static String SUBCONADDRESS_HEADER = "ADDRESS";
	private final static String SUBCONCITY_HEADER = "CITY";
	private final static String SUBCONZPCD_HEADER = "ZIP";

	// defect 10161
	// modify 
	private final static int SUBCONZPCD_STARTPT = 113;
	// new  
	private final static String SUBCONSTATE_HEADER = "STATE";
	private final static int SUBCONSTATE_STARTPT = 103;
	private final static int SUBCONSTATE_LENGTH = 5;
	private final static String SUBCONZP4_HEADER = "ZP+4";
	private final static int SUBCONZP4_STARTPT = 122;
	// end defect 10161 

	/**
	 * GenSubconReport constructor
	 */
	public GenSubconReport()
	{
		super();
	}

	/**
	 * GenSubconReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenSubconReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * GenSubconReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 * @param abIdSort boolean  
	 */
	public GenSubconReport(
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

		// no additional header information
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		ColumnHeader laColumn1 =
			new ColumnHeader(
				SUBCONID_HEADER,
				SUBCONID_STARTPT,
				SUBCONID_LENGTH);
		ColumnHeader laColumn2 =
			new ColumnHeader(
				SUBCONNAME_HEADER,
				SUBCONNAME_STARTPT,
				SUBCONNAME_LENGTH);
		ColumnHeader laColumn3 =
			new ColumnHeader(
				SUBCONADDRESS_HEADER,
				SUBCONSTREET_STARTPT,
				SUBCONSTREET_LENGTH);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				SUBCONCITY_HEADER,
				SUBCONCITY_STARTPT,
				SUBCONCITY_LENGTH);
		ColumnHeader laColumn5 =
			new ColumnHeader(
				SUBCONZPCD_HEADER,
				SUBCONZPCD_STARTPT,
				SUBCONZPCD_LENGTH);

		// defect 10161
		ColumnHeader laColumn_State =
			new ColumnHeader(
				SUBCONSTATE_HEADER,
				SUBCONSTATE_STARTPT,
				SUBCONSTATE_LENGTH);

		ColumnHeader laColumnZpP4 =
			new ColumnHeader(
				SUBCONZP4_HEADER,
				SUBCONZP4_STARTPT,
				SUBCONZP4_LENGTH);
		// end defect 10161 

		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);
		lvRow1.addElement(laColumn3);
		lvRow1.addElement(laColumn4);

		// defect 10161 
		// State 
		lvRow1.addElement(laColumn_State);
		// end defect 10161

		lvRow1.addElement(laColumn5);

		// defect 10161
		// ZpP4 
		lvRow1.addElement(laColumnZpP4);
		// end defect 10161 

		lvTable.addElement(lvRow1); //Adding ColumnHeader Information

		DecimalFormat laThreeDigits = new DecimalFormat("000");

		SubcontractorData laDataline = new SubcontractorData();

		int i = 0;
		if (!(avResults == null) && (avResults.size() > 0))
		{
			while (i < avResults.size()) // Loop through the results
			{
				generateHeader(lvHeader, lvTable);

				// defect 10161
				int j = getNoOfDetailLines() - 2;
				int liLineCount = 0;
				// end defect 10161 

				for (int k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						laDataline =
							(SubcontractorData) avResults.elementAt(i);

						String lsId =
							laThreeDigits.format(laDataline.getId());
						this.caRpt.rightAlign(
							lsId,
							SUBCONID_STARTPT,
							SUBCONID_LENGTH);
						this.caRpt.print(
							laDataline.getName1(),
							SUBCONNAME_STARTPT,
							SUBCONNAME_LENGTH);

						// defect 10161 
						AddressData laAddrData =
							laDataline.getAddressData();
						this.caRpt.print(
							laAddrData.getSt1(),
							SUBCONSTREET_STARTPT,
							SUBCONSTREET_LENGTH);
						this.caRpt.print(
							laAddrData.getCity(),
							SUBCONCITY_STARTPT,
							SUBCONCITY_LENGTH);
						this.caRpt.print(
							laAddrData.getState(),
							SUBCONSTATE_STARTPT,
							SUBCONSTATE_LENGTH);
						this.caRpt.print(
							laAddrData.getZpcd(),
							SUBCONZPCD_STARTPT,
							SUBCONZPCD_LENGTH);
						if (laAddrData.getZpcdp4().length() > 1)
						{
							// defect 10161
							//this.caRpt.print(
							//	SUBCONZPDASH_VALUE,
							//	SUBCONZPDASH_STARTPT,
							//	SUBCONZPDASH_LENGTH);

							// end defect 10161 
							this.caRpt.print(
								laAddrData.getZpcdp4(),
								SUBCONZP4_STARTPT,
								SUBCONZP4_LENGTH);
						}
						this.caRpt.nextLine();

						liLineCount = 1;

						// if either Subcon2 || Street2 print 
						// on next line and assign 'liExtraLine'  
						if (laDataline.getName2().trim().length() != 0
							|| laDataline
								.getAddressData()
								.getSt2()
								.trim()
								.length()
								!= 0)
						{
							this.caRpt.print(
								laDataline.getName2(),
								SUBCONNAME_STARTPT,
								SUBCONNAME_LENGTH);
							this.caRpt.print(
								laAddrData.getSt2(),
								SUBCONSTREET_STARTPT,
								SUBCONSTREET_LENGTH);
							this.caRpt.nextLine();
							liLineCount = liLineCount + 1;
						}
						k = k + liLineCount;
						// end defect 10161

						this.caRpt.nextLine();
						i++;
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
			new ReportProperties("RTS.POS.4012");
		GenSubconReport laGSR =
			new GenSubconReport("Subcontractor Report", laRptProps);

		// Generating Demo data to display.
		String lsQuery =
			"Select * FROM RTS.RTS_SUBCONTRACTORS WHERE DELETEINDI =0";
		Vector lvQueryResults = laGSR.queryData(lsQuery);
		laGSR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\SUBLST.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGSR.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport = new FrmPreviewReport("c:\\SUBLST.RPT");
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
	 * Run query and return results.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// This is faked data
		Vector lvData = new Vector();

		SubcontractorData laDataline = new SubcontractorData();
		laDataline.setOfcIssuanceNo(1);
		laDataline.setSubstaId(1);
		laDataline.setId(1);
		laDataline.setName1("Ray's Fast Cars");
		AddressData laAddrData = laDataline.getAddressData();
		laAddrData.setSt1("125 E 11 Street");
		laAddrData.setCity("Austin");
		laAddrData.setZpcd("78701");
		laAddrData.setZpcdp4(" ");
		lvData.addElement(laDataline);

		SubcontractorData laDataline1 = new SubcontractorData();
		laDataline1.setOfcIssuanceNo(1);
		laDataline1.setSubstaId(1);
		laDataline1.setId(2);
		laDataline1.setName1("Sam's Slow Cars");
		AddressData laAddrData1 = laDataline1.getAddressData();
		laAddrData1.setSt1("125 E 11 Street");
		laAddrData1.setCity("Austin");
		laAddrData1.setZpcd("78701");
		laAddrData1.setZpcdp4(" ");
		lvData.addElement(laDataline1);

		SubcontractorData laDataline2 = new SubcontractorData();
		laDataline2.setOfcIssuanceNo(1);
		laDataline2.setSubstaId(1);
		laDataline2.setId(10);
		laDataline2.setName1("Jane's Junky Cars");
		AddressData laAddrData2 = laDataline2.getAddressData();
		laAddrData2.setSt1("125 E 11 Street");
		laAddrData2.setCity("BubbaTown");
		laAddrData2.setZpcd("78701");
		laAddrData2.setZpcdp4("1234");
		// end defect 10161

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
		lvData.addElement(laDataline2);

		return lvData;
	}
}
