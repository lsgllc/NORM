package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InventoryProfileData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * GenInventoryProfileReport.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/07/2001	New Class
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from report.
 * 							modify formatReport()
 * 							defect 8269 Ver 5.2.2 Fix
 * Ray Rowehl	09/29/2005	Work on constants
 * 							defect 7890 Ver 5.2.3
 * K Harrell	08/17/2009	Implement generateFooter(boolean)
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This class formats the completed inventory profile Report.
 *
 * @version	Defect_POS_F	08/17/2009
 * @author	Min Wang
 * <br>Creation Date:		10/07/2001 07:35:55
 */

public class GenInventoryProfileReport extends ReportTemplate
{
	private static final int ENTITY_BREAK_LINES = 2;
	private static final int RESERVE_FOOTER_LINES = 4;

	private static final String SAMPLE_FILE_NAME = "c:\\INVPRF.RPT";
	private static final String SAMPLE_QUERY =
		"Select * FROM RTS.RTS_TABLE";
	private static final String TXT_NO = "NO";
	private static final String TXT_YES = "YES";

	private String ENTITY_TBL_HEADER = "ENTITY";
	private int ENTITY_START_PT = 1;
	private int ENTITY_LENGTH = 14;

	private String ID_TBL_HEADER = "ID";
	private int ID_START_PT = 21;
	private int ID_LENGTH = 7;
	private int ID_START_PT_RIGHT = 17;

	private String ITM_DESC_TBL_HEADER = "ITEM DESCRIPTION";
	private int ITM_DESC_START_PT = 29;
	private int ITM_DESC_LENGTH = 40;

	private String YEAR_TBL_HEADER = "YEAR";
	private int YEAR_START_PT = 74;
	private int YEAR_LENGTH = 4;

	private String MAXIMUM_TBL_HEADER = "MAXIMUM";
	private int MAXIMUM_START_PT = 84;
	private int MAXIMUM_LENGTH = 7;

	private String MINIMUM_TBL_HEADER = "MINIMUM";
	private int MINIMUM_START_PT = 97;
	private int MINIMUM_LENGTH = 7;

	private String NEXT_ITM__TBL_HEADER = "NEXT ITEM";
	private int NEXT_ITM_START_PT = 109;
	private int NEXT_ITM_LENGTH = 9;

	private String SERVER = "SERVER";
	private String lsNextItm = "";

	/**
	 * GenInventoryProfileReport constructor
	 */
	public GenInventoryProfileReport()
	{
		super();
	}

	/**
	 * GenSetAsideTransactionReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenInventoryProfileReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Formats the Report
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();

		Vector lvColumn = new Vector();
		ColumnHeader laColumn1 =
			new ColumnHeader(
				ENTITY_TBL_HEADER,
				ENTITY_START_PT,
				ENTITY_LENGTH);
		ColumnHeader laColumn2 =
			new ColumnHeader(ID_TBL_HEADER, ID_START_PT, ID_LENGTH);
		ColumnHeader laColumn3 =
			new ColumnHeader(
				ITM_DESC_TBL_HEADER,
				ITM_DESC_START_PT,
				ITM_DESC_LENGTH);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				YEAR_TBL_HEADER,
				YEAR_START_PT,
				YEAR_LENGTH);
		ColumnHeader laColumn5 =
			new ColumnHeader(
				MAXIMUM_TBL_HEADER,
				MAXIMUM_START_PT,
				MAXIMUM_LENGTH);
		ColumnHeader laColumn6 =
			new ColumnHeader(
				MINIMUM_TBL_HEADER,
				MINIMUM_START_PT,
				MINIMUM_LENGTH);
		ColumnHeader laColumn7 =
			new ColumnHeader(
				NEXT_ITM__TBL_HEADER,
				NEXT_ITM_START_PT,
				NEXT_ITM_LENGTH);
		lvColumn.addElement(laColumn1);
		lvColumn.addElement(laColumn2);
		lvColumn.addElement(laColumn3);
		lvColumn.addElement(laColumn4);
		lvColumn.addElement(laColumn5);
		lvColumn.addElement(laColumn6);
		lvColumn.addElement(laColumn7);
		lvTable.addElement(lvColumn);
		//Additing ColumnHeader Information

		InventoryProfileData laDataline = new InventoryProfileData();

		int i = 0;
		if (!(avResults == null) && (avResults.size() > 0))
		{
			// Set up the pre stuff for breaks
			laDataline = (InventoryProfileData) avResults.elementAt(0);
			String lsPreEntity = laDataline.getEntity();
			String lsPreId = laDataline.getId();

			while (i < avResults.size())
			{ //Loop through the results
				generateHeader(lvHeader, lvTable);
				boolean lbNewPage = true;
				int j =
					this.caRptProps.getPageHeight()
						- RESERVE_FOOTER_LINES;
				//Get Available lines on the page
				for (int k = this.caRpt.getCurrX(); k <= j; k++)
				{
					if (i < avResults.size())
					{
						laDataline =
							(InventoryProfileData) avResults.elementAt(
								i);

						// if the Entity or the Id changes, print two
						// blank lines
						if (!lsPreEntity.equals(laDataline.getEntity())
							|| !lsPreId.equals(laDataline.getId()))
						{
							if (!lbNewPage)
							{
								this.caRpt.blankLines(
									ENTITY_BREAK_LINES);
							}
							lsPreEntity = laDataline.getEntity();
							lsPreId = laDataline.getId();
						}

						String lsEntity = laDataline.getEntity();
						// this is a special case since CHAR_C matches
						// to STR_A
						if (lsEntity.equals(InventoryConstant.CHAR_C))
						{
							this.caRpt.print(
								SERVER,
								ENTITY_START_PT,
								ENTITY_LENGTH);
						}
						else if (
							lsEntity.equals(InventoryConstant.CHAR_W))
						{
							this.caRpt.print(
								InventoryConstant.STR_W.toUpperCase(),
								ENTITY_START_PT,
								ENTITY_LENGTH);
						}
						else if (
							lsEntity.equals(InventoryConstant.CHAR_D))
						{
							this.caRpt.print(
								InventoryConstant.STR_D.toUpperCase(),
								ENTITY_START_PT,
								ENTITY_LENGTH);
						}
						else if (
							lsEntity.equals(InventoryConstant.CHAR_E))
						{
							this.caRpt.print(
								InventoryConstant.STR_E.toUpperCase(),
								ENTITY_START_PT,
								ENTITY_LENGTH);
						}
						else if (
							lsEntity.equals(InventoryConstant.CHAR_S))
						{
							this.caRpt.print(
								InventoryConstant.STR_S.toUpperCase(),
								ENTITY_START_PT,
								ENTITY_LENGTH);
						}
						if (lsEntity.equals(InventoryConstant.CHAR_C))
						{
							this.caRpt.rightAlign(
								CommonConstant.STR_SPACE_EMPTY,
								ID_START_PT_RIGHT,
								ID_LENGTH);
						}
						else
						{
							this.caRpt.rightAlign(
								laDataline.getId(),
								ID_START_PT_RIGHT,
								ID_LENGTH);
						}
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							ITM_DESC_START_PT,
							ITM_DESC_LENGTH);
						int liYear = laDataline.getInvItmYr();
						if (liYear == 0)
						{
							this.caRpt.print(
								CommonConstant.STR_SPACE_EMPTY,
								YEAR_START_PT,
								YEAR_LENGTH);
						}
						else
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								YEAR_START_PT,
								YEAR_LENGTH);
						}
						this.caRpt.rightAlign(
							String.valueOf(laDataline.getMaxQty()),
							MAXIMUM_START_PT,
							MAXIMUM_LENGTH);
						this.caRpt.rightAlign(
							String.valueOf(laDataline.getMinQty()),
							MINIMUM_START_PT,
							MINIMUM_LENGTH);

						if (laDataline.getNextAvailIndi() == 1)
						{
							lsNextItm = TXT_YES;
						}
						else
						{
							lsNextItm = TXT_NO;
						}

						this.caRpt.rightAlign(
							lsNextItm,
							NEXT_ITM_START_PT,
							NEXT_ITM_LENGTH);
						this.caRpt.nextLine();
						k = this.caRpt.getCurrX();
						i = i + 1;
						lbNewPage = false;
					} //end outer if
				} //end for
				// defect 8628 
				//if (i >= avResults.size()) // last page
				//{
				//	this.caRpt.nextLine();
				//	generateEndOfReport();
				//} //end if
				// this.caRpt.nextLine();
				// generateFooter();
				generateFooter(i >= avResults.size());
				// end defect 8628  
			} //end while
		}
		else //no records found
		{
			generateHeader(lvHeader, lvTable);
			this.caRpt.nextLine();
			generateNoRecordFoundMsg();
			// defect 8628 
			generateFooter(true);
			// end defect 8628 
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
			new ReportProperties(
				ReportConstant.RPT_3051_INVENTORY_PROFILE_REPORT_ID);
		GenInventoryProfileReport laGIPR =
			new GenInventoryProfileReport(
				ReportConstant.RPT_3051_INVENTORY_PROFILE_REPORT_TITLE,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laGIPR.queryData(lsQuery);
		laGIPR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File liOutputFile;
		FileOutputStream liFout;
		PrintStream liPout = null;
		try
		{
			liOutputFile = new File(SAMPLE_FILE_NAME);
			liFout = new FileOutputStream(liOutputFile);
			liPout = new PrintStream(liFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		liPout.print(laGIPR.caRpt.getReport().toString());
		liPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport = new FrmPreviewReport(SAMPLE_FILE_NAME);
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
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
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

		InventoryProfileData laDataLine = new InventoryProfileData();
		laDataLine.setEntity("SERVER");
		laDataLine.setId("CONRADM");
		laDataLine.setItmCd("WS");
		laDataLine.setItmCdDesc("WINDSHIELD STICKER");
		laDataLine.setInvItmYr(2002);
		laDataLine.setMaxQty(100);
		laDataLine.setMinQty(0);
		laDataLine.setNextAvailIndi(0);
		lvData.addElement(laDataLine);

		InventoryProfileData laDataLine1 = new InventoryProfileData();
		laDataLine1.setEntity("EMPLOYEE");
		laDataLine1.setId("DEFAULT");
		laDataLine1.setItmCd("WS");
		laDataLine1.setItmCdDesc("WINDSHIELD STICKER");
		laDataLine1.setInvItmYr(2001);
		laDataLine1.setMaxQty(10);
		laDataLine1.setMinQty(10);
		laDataLine1.setNextAvailIndi(1);
		lvData.addElement(laDataLine1);

		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		lvData.addElement(laDataLine1);
		return lvData;
	}
}
