package com.txdot.isd.rts.services.reports.localoptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenPublishingReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/06/2001	New Class
 * Min Wang   	09/21/2001	Add Constants for positioning
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	11/01/2005	Constant Cleanup. Pad substaid with " "
 * 							to align columns. Added static final
 * 							to constants. Do not skip the first line
 * 							on the report. 
 * 							defect 8379 Ver 5.2.3 
 * K Harrell	08/29/2009	Implement new generateFooter(boolean) 
 * 							Corrected page break handling. 
 * 							Add feature	to keep substation rows together.
 *							add cvTable
 * 							add generatePageBreak(), getSubstaRowCount()
 * 							modify END_OF_PAGE_WHITE_SPACE 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Publishing Report.
 *
 * @version	Defect_POS_F 	08/29/2009
 * @author	Min Wang
 * <br>Creation Date:		09/06/2001 07:35:55
 */
public class GenPublishingReport extends ReportTemplate
{
	// defect 8628 
	private Vector cvTable = new Vector();
	// end defect 8628 

	private static final String ID_HEADER = "ID";
	private static final int ID_LENGTH = 2;
	private static final int ID_START_PT = 1;

	private static final String SUBSTA_NAME_HEADER = "SUBSTATION NAME";
	private static final int SUBSTA_NAME_LENGTH = 40;
	private static final int SUBSTA_NAME_PT = 7;

	private static final String UPDATE_HEADER = "UPDATE ACCESS";
	private static final int UPDATE_LENGTH = 13;
	private static final int UPDATE_PT = 85;

	private static final String TABLE_NAME_HEADER = "PUBLISHED TABLES";
	private static final int TABLE_NAME_LENGTH = 18;
	private static final int TABLE_NAME_PT = 48;

	// defect 8628 
	// private static final int END_OF_PAGE_WHITE_SPACE = 3;
	private static final int END_OF_PAGE_WHITE_SPACE = 2;
	// end defect 8628 
	
	private static final String UPDT_ACCESS_ALLOWED = "Allowed";
	private static final String UPDT_ACCESS_UNALLOWED = "Not Allowed";

	/**
	 * GenPublishingReport constructor
	 */
	public GenPublishingReport()
	{
		super();
	}

	/**
	 * GenPublishingReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenPublishingReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Format Report method
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// defect 8628 
		Vector lvColumn = new Vector();

		ColumnHeader laColumn1 =
			new ColumnHeader(ID_HEADER, ID_START_PT, ID_LENGTH);
		ColumnHeader laColumn2 =
			new ColumnHeader(
				SUBSTA_NAME_HEADER,
				SUBSTA_NAME_PT,
				SUBSTA_NAME_LENGTH);
		ColumnHeader laColumn3 =
			new ColumnHeader(
				TABLE_NAME_HEADER,
				TABLE_NAME_PT,
				TABLE_NAME_LENGTH);
		ColumnHeader laColumn4 =
			new ColumnHeader(UPDATE_HEADER, UPDATE_PT, UPDATE_LENGTH);

		lvColumn.addElement(laColumn1);
		lvColumn.addElement(laColumn2);
		lvColumn.addElement(laColumn3);
		lvColumn.addElement(laColumn4);

		cvTable.addElement(lvColumn);

		int liPreSubstaId = 0;
		String lsUpdtAccess = "";

		generateHeader(new Vector(), cvTable);

		if (!(avResults == null) && (avResults.size() > 0))
		{
			for (int i = 0; i < avResults.size(); i++)
			{
				PublishingReportData laDataline =
					(PublishingReportData) avResults.elementAt(i);

				// defect 8379		
				if (liPreSubstaId != laDataline.getSubstaId())
				{
					if (liPreSubstaId != 0)
					{
						caRpt.nextLine();

						// Determine row count for next substation 
						int liSubstaRowCount =
							getSubstaRowCount(
								avResults,
								i,
								laDataline.getSubstaId());
								
						// Page Break if all rows will not fit on page  
						generatePageBreak(
							liSubstaRowCount + END_OF_PAGE_WHITE_SPACE);
					}

					// Left Padding 
					String lsSubstaId =
						Integer.toString(laDataline.getSubstaId());
					lsSubstaId =
						UtilityMethods.addPadding(
							lsSubstaId,
							ID_LENGTH,
							" ");
					caRpt.print(lsSubstaId, ID_START_PT, ID_LENGTH);
					// end defect 8379

					caRpt.print(
						laDataline.getSubstaName(),
						SUBSTA_NAME_PT,
						SUBSTA_NAME_LENGTH);
					liPreSubstaId = laDataline.getSubstaId();
				} 
				
				caRpt.print(
					laDataline.getTblName(),
					TABLE_NAME_PT,
					TABLE_NAME_LENGTH);

				if (laDataline.getTblUpdtIndi() != 0)
				{
					lsUpdtAccess = UPDT_ACCESS_ALLOWED;
				}
				else
				{
					lsUpdtAccess = UPDT_ACCESS_UNALLOWED;
				}
				caRpt.print(lsUpdtAccess, UPDATE_PT, UPDATE_LENGTH);
				caRpt.nextLine();
			}
			//	if (i >= avResults.size())
			//	{
			//		this.caRpt.nextLine();
			//		generateEndOfReport();
			//	}
			//	generateFooter();
		}
		// no records found
		else
		{
			generateNoRecordFoundMsg();
			//generateEndOfReport();
			//generateFooter();
		}
		generateFooter(true);
		// end defect 8628
	} //end formatReport

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
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
			generateHeader(new Vector(), cvTable);
		}
	}

	/** 
	 * Get Row Count for Current Substation 
	 * 
	 * @param avData
	 */
	private int getSubstaRowCount(
		Vector avResults,
		int aiCurrentRow,
		int aiSubstaId)
	{
		int liSubscrRows = 1;
		for (int j = aiCurrentRow + 1; j < avResults.size(); j++)
		{
			PublishingReportData laDataline1 =
				(PublishingReportData) avResults.elementAt(j);

			if (laDataline1.getSubstaId() == aiSubstaId)
			{
				liSubscrRows = liSubscrRows + 1;
			}
			else
			{
				break;
			}
		}
		return liSubscrRows;
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
			new ReportProperties(ReportConstant.PUBLISHING_REPORT_ID);
		GenPublishingReport laGenRpt =
			new GenPublishingReport(
				ReportConstant.PUBLISHING_REPORT_TITLE,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lsQueryResults = laGenRpt.queryData(lsQuery);
		laGenRpt.formatReport(lsQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\PUBLISH.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGenRpt.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\PUBLISH.RPT");
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
					+ "GenPublishingReport");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Method to populate the data to be displayed on the report
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// this is faked data..
		// Need to get real data structure that relates to this report.
		Vector lvData = new Vector();

		PublishingReportData laDataLine = new PublishingReportData();
		laDataLine.setSubstaId(1);
		laDataLine.setSubstaName("MCGREGOR");
		laDataLine.setTblName("RTS_SECURITY");
		//dataLine.setTblSubstaId(1);
		laDataLine.setTblUpdtIndi(0);
		laDataLine.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine);

		PublishingReportData laDataLine1 = new PublishingReportData();
		laDataLine1.setSubstaId(1);
		laDataLine1.setSubstaName("MCGREGOR");
		laDataLine1.setTblName("RTS_SECURITY");
		//dataLine1.setTblSubstaId(1);
		laDataLine1.setTblUpdtIndi(1);
		laDataLine1.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine1);

		PublishingReportData laDataLine2 = new PublishingReportData();
		laDataLine2.setSubstaId(2);
		laDataLine2.setSubstaName("MCGREGOR");
		laDataLine2.setTblName("RTS_SUBCON");
		//dataLine2.setTblSubstaId(2);
		laDataLine2.setTblUpdtIndi(0);
		laDataLine2.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine2);

		PublishingReportData laDataLine3 = new PublishingReportData();
		laDataLine3.setSubstaId(2);
		laDataLine3.setSubstaName("MCGREGOR");
		laDataLine3.setTblName("RTS_SECURITY");
		//dataLine3.setTblSubstaId(2);
		laDataLine3.setTblUpdtIndi(1);
		laDataLine3.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine3);

		PublishingReportData laDataLine4 = new PublishingReportData();
		laDataLine4.setSubstaId(2);
		laDataLine4.setSubstaName("MCGREGOR");
		laDataLine4.setTblName("RTS_SUBCON");
		//dataLine4.setTblSubstaId(2);
		laDataLine4.setTblUpdtIndi(0);
		laDataLine4.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine4);

		PublishingReportData laDataLine5 = new PublishingReportData();
		laDataLine5.setSubstaId(1);
		laDataLine5.setSubstaName("MCGREGOR");
		laDataLine5.setTblName("RTS_SECURITY");
		//dataLine5.setTblSubstaId(1);
		laDataLine5.setTblUpdtIndi(1);
		laDataLine5.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine5);

		PublishingReportData laDataLine6 = new PublishingReportData();
		laDataLine6.setSubstaId(1);
		laDataLine6.setSubstaName("MCGREGOR");
		laDataLine6.setTblName("RTS_DEALERS");
		//dataLine6.setTblSubstaId(1);
		laDataLine6.setTblUpdtIndi(0);
		laDataLine6.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine6);

		PublishingReportData laDataLine7 = new PublishingReportData();
		laDataLine7.setSubstaId(1);
		laDataLine7.setSubstaName("MCGREGOR");
		laDataLine7.setTblName("RTS_SECURITY");
		//dataLine7.setTblSubstaId(1);
		laDataLine7.setTblUpdtIndi(0);
		laDataLine7.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine7);

		PublishingReportData laDataLine8 = new PublishingReportData();
		laDataLine8.setSubstaId(3);
		laDataLine8.setSubstaName("MCGREGOR");
		laDataLine8.setTblName("RTS_SECURITY");
		//dataLine8.setTblSubstaId(3);
		laDataLine8.setTblUpdtIndi(0);
		laDataLine8.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine8);

		PublishingReportData laDataLine9 = new PublishingReportData();
		laDataLine9.setSubstaId(3);
		laDataLine9.setSubstaName("MCGREGOR");
		laDataLine9.setTblName("RTS_SECURITY");
		//dataLine9.setTblSubstaId(3);
		laDataLine9.setTblUpdtIndi(1);
		laDataLine9.setOfcIssuanceNo(161);
		lvData.addElement(laDataLine9);

		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);
		lvData.addElement(laDataLine9);

		return lvData;
	}
}
