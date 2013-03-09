package com.txdot.isd.rts.services.reports.localoptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;

/*
 * GenEventSecurityReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/07/2001	New Class
 * Min Wang		09/06/2001	New Class
 * Min Wang   	09/21/2001	Add Constants for positioning
 * Min Wang		06/30/2002  (CQU1000004240) Reduced white space 
 * Min Wang 	10/01/2003	Add User Name for xp.
 *                          defect 6616 version 5.1.6.
 * Min Wang		02/02/2004	Modifed queryData() to setUserName() 
 *							for running stand alone.
 *							modify formatReport() and queryData()
 *                          defect 6616 version 5.1.6.
 * Min Wang		05/26/2004	line up for user last, first, and mid name.
 *							change constants
 *                          defect 7115 Ver 5.2.0
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3 
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	01/27/2009	New Formatting 
 * 							add UNDERLINE
 * 							delete EMP_ACCESS_TEXT, DOUBLEDASHES
 * 				  			modify	EMP_ID_NAME_TEXT,
 * 							  EVENT_NAME_LENGTH, USER_NAME_START_PT 
 * 							modify formatReport() 
 * 							defect 7116 Ver Defect POS D
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F                     
 * ----------------------------------------------------------------------
 */
/**
 * This class formats the Event Security Report
 * 
 * @version Defect_POS_F 	08/10/2009
 * @author	Min Wang
 * <br>Creation Date:		09/06/2001 07:35:55 
 */
public class GenEventSecurityReport extends ReportTemplate
{
	private String CONTINUED_TEXT = "(continued ...)";
	private int CONTINUED_TEXT_LENGTH = 15;
	private int CONTINUED_TEXT_START_PT = 1;
	private int EMP_ID_LENGTH = 7;

	// defect 7116
	//	private String EMP_ACCESS_TEXT =
	//		"THE FOLLOWING EMPLOYEES HAVE ACCESS:";
	//private final static int DOUBLEDASHES = 36;
	// private String EMP_ID_NAME_TEXT = 
	// "(EMPLOYEE ID AND NAME (ID, LAST, FIRST, MIDDLE INITIAL)";
	private String EMP_ID_NAME_TEXT = "USER NAME    EMPID       NAME ";
	private String UNDERLINE = "---------    -----       ---- ";
	private final static int EVENT_NAME_LENGTH = 100; // 30
	private final static int USER_NAME_START_PT = 30; // 10
	// end defect 7116 

	//defect 7115
	private int EMP_NAME_LENGTH = 30; //20
	private int EMP_NAME_START_PT = 55; //20
	//end defect 7115

	private final static int EVENT_NAME_START_PT = 8;
	private final static int END_OF_PAGE_WHITE_SPACE = 3;
	private final static int TOP_OF_DETAIL_DATA_LINE = 14;
	private final static int NUMBER_OF_SPACE = 2;
	private final static int DETAIL_LINE = 77;
	private final static int ONE_ROW = 1;
	private final static int USER_NAME_LENGTH = 13;

	/**
	 * GenEventSecurityReport constructor
	 */
	public GenEventSecurityReport()
	{
		super();
	}

	/**
	 * GenEventSecurityReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenEventSecurityReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Format Report
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();

		EventSecurityReportData laDataline =
			new EventSecurityReportData();
		String lsPreEmpAccsCd = "";

		int i = 0;
		boolean lbNewPageDone = false;

		if (!(avResults == null) && (avResults.size() > 0))
		{
			while (i < avResults.size()) //Loop through the results
			{
				generateHeader(lvHeader, lvTable);
				lbNewPageDone = true;

				int j =
					caRptProps.getPageHeight()
						- END_OF_PAGE_WHITE_SPACE;
				//Get available lines on the page;
				System.out.println(j);
				for (int k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						laDataline =
							(
								EventSecurityReportData) avResults
									.elementAt(
								i);
						if (lsPreEmpAccsCd
							.equals(laDataline.getEventName())
							&& lbNewPageDone)
						{
							caRpt.print(
								CONTINUED_TEXT,
								CONTINUED_TEXT_START_PT,
								CONTINUED_TEXT_LENGTH);
							caRpt.blankLines(NUMBER_OF_SPACE);
							caRpt.print(
								laDataline.getEventName(),
								EVENT_NAME_START_PT,
								EVENT_NAME_LENGTH);
							caRpt.blankLines(NUMBER_OF_SPACE);
							// defect 7116 
							// caRpt.center(EMP_ACCESS_TEXT);
							// caRpt.nextLine();
							//caRpt.center(EMP_ID_NAME_TEXT);
							caRpt.print(
								EMP_ID_NAME_TEXT,
								USER_NAME_START_PT,
								EMP_ID_NAME_TEXT.length());
							caRpt.nextLine();
							caRpt.print(
								UNDERLINE,
								USER_NAME_START_PT,
								UNDERLINE.length());
							//caRpt.nextLine();
							// end defect 7116 
							caRpt.nextLine();
							lbNewPageDone = false;
						} //end if

						// this is faked data..
						if (!lsPreEmpAccsCd
							.equals(laDataline.getEventName()))
						{
							lsPreEmpAccsCd = laDataline.getEventName();

							if ((caRpt.getCurrX() % DETAIL_LINE)
								> TOP_OF_DETAIL_DATA_LINE)
								// avoid printing double dash line on
								// top of the page  
							{
								// defect 7116 
								// caRpt.center(
								//		caRpt.doubleDashes(
								//			DOUBLEDASHES));
								caRpt.nextLine();
								caRpt.nextLine();
								// end defect 7116 
							} //end if

							if (caRpt.getCurrX() >= 70)
							{
								generateFooter();
								generateHeader(lvHeader, lvTable);
							} //end if

							lbNewPageDone = false;
							caRpt.print(
								laDataline.getEventName(),
								EVENT_NAME_START_PT,
								EVENT_NAME_LENGTH);

							caRpt.blankLines(NUMBER_OF_SPACE);
							// defect 7116
							// caRpt.center(EMP_ACCESS_TEXT);
							// caRpt.nextLine();
							// caRpt.center(EMP_ID_NAME_TEXT);
							caRpt.print(
								EMP_ID_NAME_TEXT,
								USER_NAME_START_PT,
								EMP_ID_NAME_TEXT.length());
							caRpt.nextLine();
							caRpt.print(
								UNDERLINE,
								USER_NAME_START_PT,
								UNDERLINE.length());
							// end defect 7116 
							caRpt.nextLine();

						} // end inner if
						//defect 6616
						//add user name
						caRpt.print(
							laDataline.getUserName(),
							USER_NAME_START_PT,
							USER_NAME_LENGTH);
						caRpt.print(
							laDataline.getEmpId(),
							USER_NAME_START_PT,
							EMP_ID_LENGTH);
						// defect 7116 
						// use new constants 
						caRpt.print(
							laDataline.getEmpLastName()
								+ ", "
								+ laDataline.getEmpFirstName()
								+ " "
								+ laDataline.getEmpMI(),
							EMP_NAME_START_PT,
							EMP_NAME_LENGTH);
						//end defect 6616
						// end defect 7116 

						caRpt.nextLine();
						k = caRpt.getCurrX();
						i++;

					} //end outer if
				} //end for
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

			} //end while
		}
		else
		{
			if (avResults.size() < ONE_ROW) // no data found
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
	} //end formatReport

	/**
	 * Currently not implemented
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
			new ReportProperties("RTS.POS.4002");
		GenEventSecurityReport laGSR =
			new GenEventSecurityReport(
				"EVENT SECURITY REPORT",
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryResults = laGSR.queryData(lsQuery);
		laGSR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\SECEVT.RPT");
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
			laFrmPreviewReport = new FrmPreviewReport("c:\\SECEVT.RPT");
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
			//Process p = Runtime.getRuntime().exec(
			//"cmd.exe /c copy c:\\TitlePkgRpt.txt prn");

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
	 * Method to populate the data to be displyed on the report
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// this is faked data..
		// Need to get real data structure that relates to this report.
		Vector lvData = new Vector();

		EventSecurityReportData laDataline =
			new EventSecurityReportData();
		laDataline.setEventName("TITLE/REGISTRATION");
		//defect 6616
		//add User Name
		laDataline.setUserName("MINWANG");
		//end defect 6616
		laDataline.setEmpId("MINWANG");
		laDataline.setEmpLastName("Wang");
		laDataline.setEmpFirstName("MIn");
		laDataline.setEmpMI("R");
		lvData.addElement(laDataline);

		EventSecurityReportData laDataline1 =
			new EventSecurityReportData();
		laDataline1.setEventName("TITLE/REGISTRATION");
		laDataline1.setEmpId("MIN1ONE");
		laDataline1.setEmpLastName("One");
		laDataline1.setEmpFirstName("Min");
		laDataline1.setEmpMI("1");
		lvData.addElement(laDataline1);

		EventSecurityReportData laDataline2 =
			new EventSecurityReportData();
		laDataline2.setEventName("TITLE/REGISTRATION");
		laDataline2.setEmpId("LISAQIN");
		laDataline2.setEmpLastName("Qin");
		laDataline2.setEmpFirstName("Lisa");
		laDataline2.setEmpMI("M");
		lvData.addElement(laDataline2);

		EventSecurityReportData laDataline3 =
			new EventSecurityReportData();
		laDataline3.setEventName("TITLE/REGISTRATION");
		laDataline3.setEmpId("LISATWO");
		laDataline3.setEmpLastName("TWO");
		laDataline3.setEmpFirstName("Lisa");
		laDataline3.setEmpMI("M");
		lvData.addElement(laDataline3);

		EventSecurityReportData laDataline4 =
			new EventSecurityReportData();
		laDataline4.setEventName("SECURITY");
		laDataline4.setEmpId("MINWANG");
		laDataline4.setEmpLastName("Wang");
		laDataline4.setEmpFirstName("Min");
		laDataline4.setEmpMI("B");
		lvData.addElement(laDataline4);

		EventSecurityReportData laDataline5 =
			new EventSecurityReportData();
		laDataline5.setEventName("SECURITY");
		laDataline5.setEmpId("MIN1ONE");
		laDataline5.setEmpLastName("One");
		laDataline5.setEmpFirstName("Min");
		laDataline5.setEmpMI("1");
		lvData.addElement(laDataline5);

		EventSecurityReportData laDataline6 =
			new EventSecurityReportData();
		laDataline6.setEventName("SECURITY");
		laDataline6.setEmpId("LISAQIN");
		laDataline6.setEmpLastName("Qin");
		laDataline6.setEmpFirstName("Lisa");
		laDataline6.setEmpMI("M");
		lvData.addElement(laDataline6);

		EventSecurityReportData laDataline7 =
			new EventSecurityReportData();
		laDataline7.setEventName("TOW TRUCK");
		laDataline7.setEmpId("LISAQIN");
		laDataline7.setEmpLastName("Qin");
		laDataline7.setEmpFirstName("Lisa");
		laDataline7.setEmpMI("M");
		lvData.addElement(laDataline7);

		EventSecurityReportData laDataline8 =
			new EventSecurityReportData();
		laDataline8.setEventName("TOW TRUCK");
		laDataline8.setEmpId("LISAQIN");
		laDataline8.setEmpLastName("Qin");
		laDataline8.setEmpFirstName("Lisa");
		laDataline8.setEmpMI("M");
		lvData.addElement(laDataline8);

		EventSecurityReportData laDataline9 =
			new EventSecurityReportData();
		laDataline9.setEventName("TOW TRUCK");
		laDataline9.setEmpId("LISAQIN");
		laDataline9.setEmpLastName("Qin");
		laDataline9.setEmpFirstName("Lisa");
		laDataline9.setEmpMI("M");
		lvData.addElement(laDataline9);

		EventSecurityReportData laDataline10 =
			new EventSecurityReportData();
		laDataline10.setEventName("TOW TRUCK");
		laDataline10.setEmpId("LISAQIN");
		laDataline10.setEmpLastName("Qin");
		laDataline10.setEmpFirstName("Lisa");
		laDataline10.setEmpMI("M");
		lvData.addElement(laDataline10);

		EventSecurityReportData laDataline11 =
			new EventSecurityReportData();
		laDataline11.setEventName("TOW TRUCK");
		laDataline11.setEmpId("LISAQIN");
		laDataline11.setEmpLastName("Qin");
		laDataline11.setEmpFirstName("Lisa");
		laDataline11.setEmpMI("M");
		lvData.addElement(laDataline11);

		EventSecurityReportData laDataline12 =
			new EventSecurityReportData();
		laDataline12.setEventName("TOW TRUCK");
		laDataline12.setEmpId("LISAQIN");
		laDataline12.setEmpLastName("Qin");
		laDataline12.setEmpFirstName("Lisa");
		laDataline12.setEmpMI("M");
		lvData.addElement(laDataline12);

		EventSecurityReportData laDataline13 =
			new EventSecurityReportData();
		laDataline13.setEventName("TOW TRUCK");
		laDataline13.setEmpId("LISAQIN");
		laDataline13.setEmpLastName("Qin");
		laDataline13.setEmpFirstName("Lisa");
		laDataline13.setEmpMI("M");
		lvData.addElement(laDataline13);

		EventSecurityReportData laDataline14 =
			new EventSecurityReportData();
		laDataline14.setEventName("TOW TRUCK");
		laDataline14.setEmpId("LISAQIN");
		laDataline14.setEmpLastName("Qin");
		laDataline14.setEmpFirstName("Lisa");
		laDataline14.setEmpMI("M");
		lvData.addElement(laDataline14);

		EventSecurityReportData laDataline15 =
			new EventSecurityReportData();
		laDataline15.setEventName("TOW TRUCK");
		laDataline15.setEmpId("LISAQIN");
		laDataline15.setEmpLastName("Qin");
		laDataline15.setEmpFirstName("Lisa");
		laDataline15.setEmpMI("M");
		lvData.addElement(laDataline15);

		EventSecurityReportData laDataline16 =
			new EventSecurityReportData();
		laDataline16.setEventName("TITLE/REGISTRATION");
		laDataline16.setEmpId("MIN1ONE");
		laDataline16.setEmpLastName("One");
		laDataline16.setEmpFirstName("Min");
		laDataline16.setEmpMI("1");
		lvData.addElement(laDataline16);

		EventSecurityReportData laDataline17 =
			new EventSecurityReportData();
		laDataline17.setEventName("TITLE/REGISTRATION");
		laDataline17.setEmpId("LISAQIN");
		laDataline17.setEmpLastName("Qin");
		laDataline17.setEmpFirstName("Lisa");
		laDataline17.setEmpMI("M");
		lvData.addElement(laDataline17);

		EventSecurityReportData laDataline18 =
			new EventSecurityReportData();
		laDataline18.setEventName("TITLE/REGISTRATION");
		laDataline18.setEmpId("LISATWO");
		laDataline18.setEmpLastName("TWO");
		laDataline18.setEmpFirstName("Lisa");
		laDataline18.setEmpMI("M");
		lvData.addElement(laDataline18);

		EventSecurityReportData laDataline19 =
			new EventSecurityReportData();
		laDataline19.setEventName("SECURITY");
		laDataline19.setEmpId("MINWANG");
		laDataline19.setEmpLastName("Wang");
		laDataline19.setEmpFirstName("Min");
		laDataline19.setEmpMI("B");
		lvData.addElement(laDataline19);

		EventSecurityReportData laDataline20 =
			new EventSecurityReportData();
		laDataline20.setEventName("SECURITY");
		laDataline20.setEmpId("MIN1ONE");
		laDataline20.setEmpLastName("One");
		laDataline20.setEmpFirstName("Min");
		laDataline20.setEmpMI("1");
		lvData.addElement(laDataline20);

		EventSecurityReportData laDataline21 =
			new EventSecurityReportData();
		laDataline21.setEventName("SECURITY");
		laDataline21.setEmpId("LISAQIN");
		laDataline21.setEmpLastName("Qin");
		laDataline21.setEmpFirstName("Lisa");
		laDataline21.setEmpMI("M");
		lvData.addElement(laDataline21);

		EventSecurityReportData laDataline22 =
			new EventSecurityReportData();
		laDataline22.setEventName("TOW TRUCK");
		laDataline22.setEmpId("LISAQIN");
		laDataline22.setEmpLastName("Qin");
		laDataline22.setEmpFirstName("Lisa");
		laDataline22.setEmpMI("M");
		lvData.addElement(laDataline22);

		EventSecurityReportData laDataline23 =
			new EventSecurityReportData();
		laDataline23.setEventName("TOW TRUCK");
		laDataline23.setEmpId("LISAQIN");
		laDataline23.setEmpLastName("Qin");
		laDataline23.setEmpFirstName("Lisa");
		laDataline23.setEmpMI("M");
		lvData.addElement(laDataline23);

		return lvData;
	}
}
