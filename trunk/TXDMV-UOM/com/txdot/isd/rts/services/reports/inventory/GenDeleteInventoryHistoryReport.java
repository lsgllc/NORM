package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.InventoryDeleteHistoryReportData;
import com.txdot.isd.rts.services.data.InventoryHistoryUIData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * GenDeleteInventoryHistoryReport
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arredondo  08/31/2001	New Class
 * Ray Rowehl	01/28/2002	Bring in a new Vector to include
 * 							InventoryHistoryUIData  show the date range
 *							Use Report Substa name to show the county 
 *							name.  Remove right align out of header.
 *							Other improvements.
 *							Got concurrance from Bob Tanner to string 
 *							the counties into one report stream
 *							defect 1143
 * Min Wang		02/03/2003	Modified formatReport() to print the deleted 
 * 							reason text.CQU100005376.
 * Min Wang		05/02/2003  Modified formatReport() to ptint employee 
 * 							id. 
 * 							defect 6018.
 * Ray Rowehl	01/05/2004	Remove extra () from for statement
 * 							modify formatReport()
 * 							defect 6738  5.1.5 fix 2
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify main()
 * 							defect 7896 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from report.
 * 							modify formatReport(), multiple constants
 * 							defect 8269 Ver 5.2.2
 * Ray Rowehl	09/26/2005	Constants work	
 * 							defect 7890 Ver 5.2.3
 * K Harrell	07/13/2007	Added 1 to DELQTY_LENGTH to fit "QUANTITY"
 * 							in column header.
 * 							modify DELQTY_LENGTH
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	10/12/2009	Reorganize to create as single report with 
 * 							Exception Page 
 * 							add ciOfcSize, cvOfcWithData, 
 * 							 cvOfcIdWithNoData, caInvHistUIData,
 *							 cvHeader, cvTable
 *							add formatData(), addAsOfficeWithData(), 
 *							 generatePageBreak(), generateExceptionPage(), 
 *							 setOfcName()
 *							delete NUMBER_DETAIL_LINES,WHITE_SPACE 
 *							modify formatReport() 
 * 							defect 10207 Ver Defect_POS_G 
 * ---------------------------------------------------------------------
 */

/**
 * This class formats the Delete Inventory History Report.
 *
 * @version Defect_POS_G  	10/12/2009
 * @author  Becky Arredondo
 * <br>Creation Date:		08/31/2001 14:13:38
 */

public class GenDeleteInventoryHistoryReport extends ReportTemplate
{
	//defect 10207
	private int ciOfcSize;
	private Vector cvOfcWithData = new Vector();
	private Vector cvOfcIdWithNoData = new Vector();
	private InventoryHistoryUIData caInvHistUIData;
	private Vector cvHeader = new Vector();
	private Vector cvTable = new Vector();
	// end defect 10207

	private static final int DELBEGIN_STARTPT = 61;
	private static final int DELBEGIN_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int DELDESC_STARTPT = 9; //ITEM
	private static final int DELDESC_LENGTH = 25;
	private static final int DELEND_STARTPT = 72;
	private static final int DELEND_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int DELID_STARTPT = 114; //TRANSACTION
	private static final int DELID_STARTPT1 = 112; //TRANSACTION
	private static final int DELID_LENGTH = 17;
	private static final int DELITEM1_STARTPT = 9; //DESCRIPTION
	private static final int DELITEM1_LENGTH = 25;
	private static final int DELITEM2_STARTPT = 42; //YEAR
	private static final int DELITEM2_LENGTH = 4;

	private static final int DELQTY_STARTPT = 49;
	// defect 9085
	//private static final int DELQTY_LENGTH = InventoryConstant.MAX_QTY_LENGTH+1;
	private static final int DELQTY_LENGTH =
		InventoryConstant.MAX_QTY_LENGTH + 1;
	//	end defect 9085
	private static final int DEL_BEGNO_STARTPT = 61;
	//BEGIN -- right align
	private static final int DEL_BEGNO_STARTPT1 = 55;
	private static final int DEL_BEGNO_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int DEL_ENDNO_STARTPT = 72;
	//END -- right align
	private static final int DEL_ENDNO_STARTPT2 = 66;
	private static final int DEL_ENDNO_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int DELREAS_STARTPT = 83;
	private static final int DELREAS_HEADER_LENGTH = 25;
	private static final int DELREAS_DETAIL_LENGTH =
		DELREAS_HEADER_LENGTH + 5;
	private static final int DELREAS_STARTPT_2 = 81;
	private static final int DELSTA_STARTPT = 1; //SUB STATION
	private static final int DELSTA_LENGTH = 3;
	private static final int DELSUB_STARTPT = 1; //SUB STATION
	private static final int DELSUB_LENGTH = 3;
	private static final int DELTRANS_STARTPT = 114; //ID
	private static final int DELTRANS_LENGTH = 17;
	private static final int DELTXT_LENGTH = 110;
	private static final int DELYEAR_STARTPT = 42; //YEAR
	private static final int DELYEAR_LENGTH = 4;
	private static final int RECSUB_STARTPT = 1; //STATION

	private static final int SAMPLE_AMDATE_1 = 37107;
	private static final int SAMPLE_OFC_1 = 208;
	private static final int SAMPLE_ITMQTY_1 = 54;
	private static final int SAMPLE_ITMYR_1 = 2001;
	private static final int SAMPLE_SUBSTAID_1 = 0;
	private static final int SAMPLE_TIME_1 = 64407;
	private static final int SAMPLE_WS_1 = 100;

	//	Identifying the headers
	private static final String DEL_BEGIN_HEADER = "BEGIN";
	private static final String DEL_DESC_HEADER = "DESCRIPTION";
	private static final String DEL_END_HEADER = "END";
	private static final String DEL_ID_HEADER = "ID";
	private static final String DEL_ITEM_HEADER = "ITEM";
	private static final String DEL_NUMBER_HEADER = "NUMBER";
	private static final String DEL_QUANTITY_HEADER = "QUANTITY";
	private static final String DEL_REASON_HEADER = "REASON";
	private static final String DEL_STA_HEADER = "STA";
	private static final String DEL_SUB_HEADER = "SUB";
	private static final String DEL_TRANS_HEADER = "TRANSACTION";
	private static final String DEL_YEAR_HEADER = "YEAR";

	private static final String SAMPLE_EMPID_1 = "EMPID= JEANETT";
	private static final String SAMPLE_EMPID_2 = "EMPID= SHOLDER";
	private static final String SAMPLE_EMPID_3 = "EMPID= SHEILA2";
	private static final String SAMPLE_ITMCD_1 = "DPS";
	private static final String SAMPLE_ITMCD_2 = "DPP";
	private static final String SAMPLE_ITMCD_3 = "IPP";
	private static final String SAMPLE_ITMDESC_1 =
		"DISABLED PERSON PLT";
	private static final String SAMPLE_ITMNO_1 = "157280PB";
	private static final String SAMPLE_ITMNO_3 = "7280PB";
	private static final String SAMPLE_QUERY = "Select * FROM RTS_TBL";
	private static final String SAMPLE_REASON_1 = "DAMAGED";

	private static final String SAMPLE_REPORT_FILENAME =
		"c:\\DelInvHistRpt.txt";

	private static final String TXT_EMPID_EQUAL = "EMPID = ";
	private static final String TXT_SPACE_TO_SPACE = " TO ";
	private static final String TXT_HISTORY_DATE_FROM =
		"HISTORY DATE FROM";

	/**
	 * GenDeleteInventoryHistoryReport constructor
	 */
	public GenDeleteInventoryHistoryReport()
	{
		super();
	}

	/**
	 * GenDeleteInventoryHistoryReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenDeleteInventoryHistoryReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Add Found OfcIssuanceNo 
	 */
	private void addAsOfficeWithData(int aiOfcId)
	{
		String lsOfcIssuanceNo = "" + aiOfcId;

		if (!cvOfcWithData.contains(lsOfcIssuanceNo))
		{
			cvOfcWithData.addElement(lsOfcIssuanceNo);
		}
	}
	
	/**
	 * 
	 * Identify Offices Without Data
	 */
	private void findOfficesWithoutData()
	{
		for (int i = 0; i < ciOfcSize; i++)
		{
			String lsOfcId =
				((caInvHistUIData.getSelectedCounties())
					.elementAt(i)
					.toString());

			if (!cvOfcWithData.contains(lsOfcId))
			{
				cvOfcIdWithNoData.add(lsOfcId);
			}
		}
	}
	
	/**
	 * Format Data for single row 
	 * 
	 * @param aaInvDelHistRptData
	 */
	private void formatData(InventoryDeleteHistoryReportData aaInvDelHistRptData)
	{
		this.caRpt.center(
			String.valueOf(aaInvDelHistRptData.getSubStaId()),
			DELSTA_STARTPT,
			DELSTA_LENGTH);
		this.caRpt.print(
			aaInvDelHistRptData.getItmCdDesc(),
			DELDESC_STARTPT,
			DELDESC_LENGTH);
		// only print the item year if it is greater
		// than zero 
		if (aaInvDelHistRptData.getInvItmYr() > 0)
		{
			this.caRpt.print(
				String.valueOf(aaInvDelHistRptData.getInvItmYr()),
				DELYEAR_STARTPT,
				DELYEAR_LENGTH);
		}
		this.caRpt.center(
			String.valueOf(aaInvDelHistRptData.getInvQty()),
			DELQTY_STARTPT,
			DELQTY_LENGTH);
		this.caRpt.rightAlign(
			aaInvDelHistRptData.getInvItmNo(),
			DEL_BEGNO_STARTPT1,
			DEL_BEGNO_LENGTH);
		this.caRpt.rightAlign(
			aaInvDelHistRptData.getInvEndNo(),
			DEL_ENDNO_STARTPT2,
			DEL_ENDNO_LENGTH);
		this.caRpt.print(
			aaInvDelHistRptData.getDelInvReasn(),
			DELREAS_STARTPT_2,
			DELREAS_DETAIL_LENGTH);

		//Create TransId
		String lsTransId =
			UtilityMethods.getTransId(
				aaInvDelHistRptData.getOfcIssuanceNo(),
				aaInvDelHistRptData.getTransWsId(),
				aaInvDelHistRptData.getTransAMDate(),
				aaInvDelHistRptData.getTransTime());

		this.caRpt.print(lsTransId, DELID_STARTPT1, DELID_LENGTH);
		this.caRpt.nextLine();

		this
			.caRpt
			.print(
				aaInvDelHistRptData.getDelInvReasnTxt(),
				RECSUB_STARTPT,
		DELTXT_LENGTH);
		
		this.caRpt.print(
			TXT_EMPID_EQUAL + aaInvDelHistRptData.getTransEmpId(),
			DELTRANS_STARTPT,
			DELTRANS_LENGTH);
		this.caRpt.nextLine();
		this.caRpt.nextLine();
		generatePageBreak(ReportConstant.NUM_LINES_2);
	}

	/**
	 * Formatting data for the report.
	 * 
	 * @param avReportIn Vector
	 */
	public void formatReport(Vector avReportIn)
	{
		// get the InventoryHistoryUIData object off the vector
		caInvHistUIData = (InventoryHistoryUIData) avReportIn.get(0);
		// get the queryResults vector off of the input vector
		Vector lvResults = (Vector) avReportIn.get(1);

		// Defining vectors
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();

		// Column Header for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				DEL_SUB_HEADER,
				DELSUB_STARTPT,
				DELSUB_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				DEL_ITEM_HEADER,
				DELITEM1_STARTPT,
				DELITEM1_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				DEL_ITEM_HEADER,
				DELITEM2_STARTPT,
				DELITEM2_LENGTH);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				DEL_BEGIN_HEADER,
				DELBEGIN_STARTPT,
				DELBEGIN_LENGTH);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				DEL_END_HEADER,
				DELEND_STARTPT,
				DELEND_LENGTH);
		ColumnHeader laColumn_1_6 =
			new ColumnHeader(
				DEL_TRANS_HEADER,
				DELTRANS_STARTPT,
				DELTRANS_LENGTH);

		//Column Header for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				DEL_STA_HEADER,
				DELSTA_STARTPT,
				DELSTA_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				DEL_DESC_HEADER,
				DELDESC_STARTPT,
				DELDESC_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				DEL_YEAR_HEADER,
				DELYEAR_STARTPT,
				DELYEAR_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				DEL_QUANTITY_HEADER,
				DELQTY_STARTPT,
				DELQTY_LENGTH);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				DEL_NUMBER_HEADER,
				DEL_BEGNO_STARTPT,
				DEL_BEGNO_LENGTH);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				DEL_NUMBER_HEADER,
				DEL_ENDNO_STARTPT,
				DEL_ENDNO_LENGTH);
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				DEL_REASON_HEADER,
				DELREAS_STARTPT,
				DELREAS_HEADER_LENGTH);
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				DEL_ID_HEADER,
				DELID_STARTPT,
				DELID_LENGTH);

		//Alignment of column headers to rows 1 and 2   
		lvRow1.addElement(laColumn_1_1);
		lvRow1.addElement(laColumn_1_2);
		lvRow1.addElement(laColumn_1_3);
		lvRow1.addElement(laColumn_1_4);
		lvRow1.addElement(laColumn_1_5);
		lvRow1.addElement(laColumn_1_6);
		lvRow2.addElement(laColumn_2_1);
		lvRow2.addElement(laColumn_2_2);
		lvRow2.addElement(laColumn_2_3);
		lvRow2.addElement(laColumn_2_4);
		lvRow2.addElement(laColumn_2_5);
		lvRow2.addElement(laColumn_2_6);
		lvRow2.addElement(laColumn_2_7);
		lvRow2.addElement(laColumn_2_8);

		// defect 10207 
		// Reorganize to print for all selected offices where data
		//  exists;  also print Exception Report

		// Adding additional Header information
		cvHeader.addElement(TXT_HISTORY_DATE_FROM);
		cvHeader.addElement(
			caInvHistUIData.getBeginDate().toString()
				+ TXT_SPACE_TO_SPACE
				+ caInvHistUIData.getEndDate().toString());

		//Adding ColumnHeader Information	
		cvTable.addElement(lvRow1);
		cvTable.addElement(lvRow2);

		ciOfcSize = caInvHistUIData.getSelectedCounties().size();
		int liPrevOfcId = 0;
		try
		{
			int liOfcId =
				((Integer) caInvHistUIData
					.getSelectedCounties()
					.elementAt(0))
					.intValue();

			for (int i = 0; i < lvResults.size(); i++)
			{

				InventoryDeleteHistoryReportData laInvDelHistRptData =
					(
						InventoryDeleteHistoryReportData) lvResults
							.elementAt(
						i);

				liOfcId = laInvDelHistRptData.getOfcIssuanceNo();

				if (liOfcId != liPrevOfcId)
				{
					setOfcName(liOfcId);

					addAsOfficeWithData(liOfcId);

					if (liPrevOfcId != 0)
					{
						generateFooter();
					}

					generateHeader(cvHeader, cvTable);
				}
				formatData(laInvDelHistRptData);
				liPrevOfcId = liOfcId;
			}

			findOfficesWithoutData();

			if (lvResults.size() == 0 && ciOfcSize == 1)
			{
				setOfcName(liOfcId);
				generateHeader(cvHeader, cvTable);
				generateNoRecordFoundMsg();
				generateFooter(true);
			}
			else
			{
				if (lvResults.size() != 0)
				{
					generateFooter(ciOfcSize == 1);
				}

				if (cvOfcIdWithNoData.size() > 0 && ciOfcSize > 1)
				{
					generateExceptionPage();
				}
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			System.err.println(ReportConstant.RPT_3071_RPT_ERR);
			aeNFEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNFEx);
			leRTSEx.writeExceptionToLog();
		}
		catch (Exception aeEx)
		{
			System.err.println(ReportConstant.RPT_3071_RPT_ERR);
			aeEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
		}
		// end defect 10207 
	}

	/**
	 * Generate Attributes for Report
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Prints the list of offices with no data returned
	 * 
	 */
	private void generateExceptionPage()
	{
		if (cvOfcIdWithNoData.size() > 0)
		{
			caRptProps.setSubstationName("EXCEPTION");
			generateHeader(cvHeader, new Vector(), false);
			caRpt.print("NO RECORDS FOUND FOR OFFICES: ");
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			int liColStartPt = 1;
			int liOffices = cvOfcIdWithNoData.size();
			for (int i = 0; i < liOffices; i++)
			{
				String lsOfcId = cvOfcIdWithNoData.get(i).toString();
				OfficeIdsData caOfcIds =
					OfficeIdsCache.getOfcId(Integer.parseInt(lsOfcId));
				String csRptOfcName = caOfcIds.getOfcName();
				if (liColStartPt + csRptOfcName.length()
					> caRpt.getWidth())
				{
					caRpt.nextLine();
					liColStartPt = 1;
				}
				caRpt.print(
					csRptOfcName,
					liColStartPt,
					csRptOfcName.length());
				if (i != liOffices - 1)
				{
					caRpt.print(CommonConstant.STR_COMMA);
				}
				liColStartPt = liColStartPt + csRptOfcName.length() + 3;
			}
			generateFooter(true);
		}
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
			generateHeader(cvHeader, cvTable);
		}
	}

	/**
	 * Main method for stand alone testing.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// this will not work right now!!	
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant
					.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_ID);
		GenDeleteInventoryHistoryReport laDIHR =
			new GenDeleteInventoryHistoryReport(
				ReportConstant
					.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_TITLE,
				laRptProps);

		// Generating Demo data to display.
		String laQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laDIHR.queryData(laQuery);
		laDIHR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(SAMPLE_REPORT_FILENAME);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laDIHR.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(SAMPLE_REPORT_FILENAME);
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
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Data generated for demo.
	 * 
	 * @param asQuery String
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvData = new Vector();

		InventoryDeleteHistoryReportData laInvDelHistRptData =
			new InventoryDeleteHistoryReportData();
		laInvDelHistRptData.setSubStaId(SAMPLE_SUBSTAID_1);
		laInvDelHistRptData.setItmCd(SAMPLE_ITMCD_1);
		laInvDelHistRptData.setItmCdDesc(SAMPLE_ITMDESC_1);
		laInvDelHistRptData.setInvItmYr(SAMPLE_ITMYR_1);
		laInvDelHistRptData.setInvQty(SAMPLE_ITMQTY_1);
		laInvDelHistRptData.setInvItmNo(SAMPLE_ITMNO_1);
		laInvDelHistRptData.setInvEndNo(SAMPLE_ITMNO_1);
		laInvDelHistRptData.setDelInvReasn(SAMPLE_REASON_1);
		laInvDelHistRptData.setOfcIssuanceNo(SAMPLE_OFC_1);
		laInvDelHistRptData.setTransWsId(SAMPLE_WS_1);
		laInvDelHistRptData.setTransAMDate(SAMPLE_AMDATE_1);
		laInvDelHistRptData.setTransTime(SAMPLE_TIME_1);
		laInvDelHistRptData.setTransEmpId(SAMPLE_EMPID_1);
		lvData.addElement(laInvDelHistRptData);

		InventoryDeleteHistoryReportData laInvDelHistRptData1 =
			new InventoryDeleteHistoryReportData();
		laInvDelHistRptData1.setSubStaId(SAMPLE_SUBSTAID_1);
		laInvDelHistRptData1.setItmCd(SAMPLE_ITMCD_2);
		laInvDelHistRptData1.setItmCdDesc(SAMPLE_ITMDESC_1);
		laInvDelHistRptData1.setInvItmYr(SAMPLE_ITMYR_1);
		laInvDelHistRptData1.setInvQty(SAMPLE_ITMQTY_1);
		laInvDelHistRptData1.setInvItmNo(SAMPLE_ITMNO_1);
		laInvDelHistRptData1.setInvEndNo(SAMPLE_ITMNO_1);
		laInvDelHistRptData1.setDelInvReasn(SAMPLE_REASON_1);
		laInvDelHistRptData1.setOfcIssuanceNo(SAMPLE_OFC_1);
		laInvDelHistRptData1.setTransWsId(SAMPLE_WS_1);
		laInvDelHistRptData1.setTransAMDate(SAMPLE_AMDATE_1);
		laInvDelHistRptData1.setTransTime(SAMPLE_TIME_1);
		laInvDelHistRptData1.setTransEmpId(SAMPLE_EMPID_2);
		lvData.addElement(laInvDelHistRptData1);

		InventoryDeleteHistoryReportData laInvDelHistRptData2 =
			new InventoryDeleteHistoryReportData();
		laInvDelHistRptData2.setSubStaId(SAMPLE_SUBSTAID_1);
		laInvDelHistRptData2.setItmCd(SAMPLE_ITMCD_3);
		laInvDelHistRptData2.setItmCdDesc(SAMPLE_ITMDESC_1);
		laInvDelHistRptData2.setInvItmYr(SAMPLE_ITMYR_1);
		laInvDelHistRptData2.setInvQty(SAMPLE_ITMQTY_1);
		laInvDelHistRptData2.setInvItmNo(SAMPLE_ITMNO_3);
		laInvDelHistRptData2.setInvEndNo(SAMPLE_ITMNO_3);
		laInvDelHistRptData2.setDelInvReasn(SAMPLE_REASON_1);
		laInvDelHistRptData2.setOfcIssuanceNo(SAMPLE_OFC_1);
		laInvDelHistRptData2.setTransWsId(SAMPLE_WS_1);
		laInvDelHistRptData2.setTransAMDate(SAMPLE_AMDATE_1);
		laInvDelHistRptData2.setTransTime(SAMPLE_TIME_1);
		laInvDelHistRptData2.setTransEmpId(SAMPLE_EMPID_3);
		lvData.addElement(laInvDelHistRptData2);

		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);
		lvData.addElement(laInvDelHistRptData2);

		return lvData;
	}
	
		/**
		 * Setup Office Name for the report
		 *
		 * @param aiOfcNo 
		 */
		private void setOfcName(int aiOfcNo)
		{
			String lsOfcName = OfficeIdsCache.getOfcName(aiOfcNo);
			if (aiOfcNo == caInvHistUIData.getRequestOfcIssuanceNo())
			{
				lsOfcName = ReportConstant.RPT_5051_SUMMARY;
			}
			caRptProps.setSubstationName(lsOfcName);
		}
}
