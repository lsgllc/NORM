package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.InventoryHistoryUIData;
import com
	.txdot
	.isd
	.rts
	.services
	.data
	.InventoryReceiveHistoryReportData;
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
 * GenReceiveInventoryHistoryReport.java
 *
 * (c) Texas Department of Transporation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Arredondo	09/05/2001	New Class
 * Ray Rowehl	01/29/2002	Fix errors documented in
 *							(CQU100001244) and (CQU100001143)
 * Min Wang		10/31/2002  Modified 
 * 							GenReceiveInventoryHistoryReport()
 * 							Defect (CQU100005001).
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from report and 
 * 							reformat report.
 * 							modify formatReport(), 
 * 							modify multiple constants
 * 							defect 8269 Ver 5.2.2. Fix 6
 * Ray Rowehl	09/29/2005	Constants work	
 * 							defect 7890 Ver 5.2.3
 * K Harrell	07/13/2007	Added 1 to RECINV_LENGTH to fit "INVOICE"
 * 							in column header.
 * 							modify RECINV_LENGTH
 * 							defect 9085 Ver Special Plates 	
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/12/2009	Reorganize to create as single report with 
 * 							Exception Page 
 * 							add ciOfcSize, cvOfcWithData, 
 * 							 cvOfcIdWithNoData, caInvHistUIData,
 *  						 cvHeader, cvTable
 * 							add formatData(),addAsOfficeWithData(),
 * 							 generatePageBreak(), generateExceptionPage(),
 * 							 setOfcName()
 * 							delete WHITE_SPACE, formatTransId()
 * 							modify formatReport() 
 * 							defect 10207 Ver Defect_POS_G 	     	
 * ---------------------------------------------------------------------
 */

/**
 * This class formats the Receive Inventory History Report.
 *
 * @version	Defect_POS_G	10/12/2009
 * @author	Becky Arredondo
 * <br>Creation Date:		09/05/2001 13:22:58
 */

public class GenReceiveInventoryHistoryReport extends ReportTemplate
{
	//	defect 10207
	private int ciOfcSize;
	private Vector cvOfcWithData = new Vector();
	private Vector cvOfcIdWithNoData = new Vector();
	private InventoryHistoryUIData caInvHistUIData;
	private Vector cvHeader = new Vector();
	private Vector cvTable = new Vector();
	// end defect 10207

	//Identifying the headers, starting pts, and lengths
	private static final int RECSUB_STARTPT = 1;
	private static final int RECSUB_LENGTH = 3;
	private static final int RECITEM1_STARTPT = 8;
	private static final int RECITEM1_LENGTH = 25;
	private static final int RECITEM2_STARTPT = 36;
	private static final int RECITEM2_LENGTH = 4;
	private static final int RECBEGIN_STARTPT = 58;
	private static final int RECBEGIN_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int RECEND_STARTPT = 69;
	private static final int RECEND_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int RECINV_STARTPT = 83;
	// defect 
	private static final int RECINV_LENGTH =
		InventoryConstant.INVOICE_NUMBER_LENGTH + 1;
	private static final int RECINV1_STARTPT = 92;
	private static final int RECINV1_LENGTH = 7;
	private static final int RECEMP_STARTPT = 103;
	private static final int RECEMP_LENGTH = 8;
	private static final int RECTRNS_STARTPT = 114;
	private static final int RECTRNS_LENGTH = 17;
	private static final int RECSTA_STARTPT = 1;
	private static final int RECSTA_LENGTH = 3;
	private static final int RECDESC_STARTPT = 8;
	private static final int RECDESC_LENGTH = 25;
	private static final int RECYEAR_STARTPT = 36;
	private static final int RECYEAR_LENGTH = 4;
	private static final int RECQTY_STARTPT = 45;
	private static final int RECQTY_LENGTH = 8;
	private static final int RECNO_STARTPT = 54;
	private static final int RECNO_STARTPT2 = 69;
	private static final int RECNO_LENGTH = 10;
	private static final int RECNO1_STARTPT = 65;
	private static final int RECNO1_LENGTH = 10;
	private static final int RECNO2_STARTPT = 83;
	private static final int RECNO2_LENGTH = 7;
	private static final int RECMOD_STARTPT = 92;
	private static final int RECMOD_LENGTH = 8;
	private static final int RECID_STARTPT = 103;
	private static final int RECID_LENGTH = 8;
	private static final int RECID1_STARTPT = 114;
	private static final int RECID1_LENGTH = 17;

	private static final String RECBEGIN_HEADER = "BEGIN";
	private static final String RECEMP_HEADER = "EMPLOYEE";
	private static final String RECEND_HEADER = "END";
	private static final String RECID_HEADER = "ID";
	private static final String RECINV_HEADER = "INVOICE";
	private static final String RECITEM_HEADER = "ITEM";
	private static final String RECMOD_HEADER = "MODIFIED";
	private static final String RECNO_HEADER = "NUMBER";
	private static final String RECQTY_HEADER = "QUANTITY";
	private static final String RECSTA_HEADER = "STA";
	private static final String RECSUB_HEADER = "SUB";
	private static final String RECTRNS_HEADER = "TRANSACTION";
	private static final String RECDESC_HEADER = "DESCRIPTION";
	private static final String RECYEAR_HEADER = "YEAR";

	private static final String SAMPLE_FILENAME =
		"c:\\RecInvHistRpt.RPT";
	private static final String SAMPLE_QUERY = "Select * FROM RTS_TBL";

	private static final String TXT_NO = "NO";
	private static final String TXT_YES = "YES";

	private static final String TXT_HISTORY_DATE_FROM =
		"HISTORY DATE FROM";
	private static final String TXT_SPACE_TO_SPACE = " TO ";

	/**
	 * GenReceiveInventoryHistoryReport constructor
	 */
	public GenReceiveInventoryHistoryReport()
	{
		super();
	}

	/**
	 * GenReceiveInventoryHistoryReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenReceiveInventoryHistoryReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
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
	 * Format/Print single row 
	 * 
	 * @param aaInvRecHistRptData
	 */
	private void formatData(InventoryReceiveHistoryReportData aaInvRecHistRptData)
	{
		this.caRpt.center(
			String.valueOf(aaInvRecHistRptData.getSubStaId()),
			RECSUB_STARTPT,
			RECSUB_LENGTH);
			
		this.caRpt.print(
			aaInvRecHistRptData.getItmCdDesc(),
			RECDESC_STARTPT,
			RECDESC_LENGTH);

		// only print the year if it is greater than zero
		if (aaInvRecHistRptData.getInvItmYr() > 0)
		{
			this.caRpt.print(
				String.valueOf(aaInvRecHistRptData.getInvItmYr()),
				RECYEAR_STARTPT,
				RECYEAR_LENGTH);
		}

		this.caRpt.rightAlign(
			String.valueOf(aaInvRecHistRptData.getInvQty()),
			RECQTY_STARTPT,
			RECQTY_LENGTH);
		this.caRpt.rightAlign(
			aaInvRecHistRptData.getInvItmNo(),
			RECNO_STARTPT,
			RECNO_LENGTH);
		this.caRpt.rightAlign(
			aaInvRecHistRptData.getInvEndNo(),
			RECNO1_STARTPT,
			RECNO1_LENGTH);
		this.caRpt.rightAlign(
			aaInvRecHistRptData.getInvcNo(),
			RECNO2_STARTPT,
			RECNO2_LENGTH);

		String lsYN =
			aaInvRecHistRptData.isModified() ? TXT_YES : TXT_NO;

		this.caRpt.center(lsYN, RECMOD_STARTPT, RECMOD_LENGTH);

		this.caRpt.print(
			aaInvRecHistRptData.getTransEmpId(),
			RECID_STARTPT,
			RECID_LENGTH);

		//Create TransId
		String lsTransId =
			UtilityMethods.getTransId(
				aaInvRecHistRptData.getOfcIssuanceNo(),
				aaInvRecHistRptData.getTransWsId(),
				aaInvRecHistRptData.getTransAMDate(),
				aaInvRecHistRptData.getTransTime());

		this.caRpt.print(lsTransId, RECTRNS_STARTPT, RECTRNS_LENGTH);
		this.caRpt.nextLine();
		generatePageBreak(ReportConstant.NUM_LINES_2);
	}

	/**
	 * Format the data into the report
	 * 
	 * @param avReportIn Vector
	 */
	public void formatReport(Vector avReportIn)
	{
		// InventoryHistoryUIData object - Element 0
		caInvHistUIData =
			(InventoryHistoryUIData) avReportIn.get(
				CommonConstant.ELEMENT_0);
				
		// Query Results - Element 1
		Vector lvResults =
			(Vector) avReportIn.get(CommonConstant.ELEMENT_1);

		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();

		//Column Headers for Row 1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				RECSUB_HEADER,
				RECSUB_STARTPT,
				RECSUB_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				RECITEM_HEADER,
				RECITEM1_STARTPT,
				RECITEM1_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				RECITEM_HEADER,
				RECITEM2_STARTPT,
				RECITEM2_LENGTH);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				RECBEGIN_HEADER,
				RECBEGIN_STARTPT,
				RECBEGIN_LENGTH);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				RECEND_HEADER,
				RECEND_STARTPT,
				RECEND_LENGTH);
		ColumnHeader laColumn_1_6 =
			new ColumnHeader(
				RECINV_HEADER,
				RECINV_STARTPT,
				RECINV_LENGTH);
		ColumnHeader laColumn_1_7 =
			new ColumnHeader(
				RECINV_HEADER,
				RECINV1_STARTPT,
				RECINV1_LENGTH);
		ColumnHeader laColumn_1_8 =
			new ColumnHeader(
				RECEMP_HEADER,
				RECEMP_STARTPT,
				RECEMP_LENGTH);
		ColumnHeader laColumn_1_9 =
			new ColumnHeader(
				RECTRNS_HEADER,
				RECTRNS_STARTPT,
				RECTRNS_LENGTH);

		//Column Headers for Row 2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				RECSTA_HEADER,
				RECSTA_STARTPT,
				RECSTA_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				RECDESC_HEADER,
				RECDESC_STARTPT,
				RECDESC_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				RECYEAR_HEADER,
				RECYEAR_STARTPT,
				RECYEAR_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				RECQTY_HEADER,
				RECQTY_STARTPT,
				RECQTY_LENGTH);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				RECNO_HEADER,
				RECBEGIN_STARTPT /* RECNO_STARTPT*/
		, RECNO_LENGTH);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				RECNO_HEADER,
				RECNO_STARTPT2,
				RECNO1_LENGTH);
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				RECNO_HEADER,
				RECNO2_STARTPT,
				RECNO2_LENGTH);
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				RECMOD_HEADER,
				RECMOD_STARTPT,
				RECMOD_LENGTH);
		ColumnHeader laColumn_2_9 =
			new ColumnHeader(RECID_HEADER, RECID_STARTPT, RECID_LENGTH);
		ColumnHeader laColumn_2_10 =
			new ColumnHeader(
				RECID_HEADER,
				RECID1_STARTPT,
				RECID1_LENGTH);

		//Alignment of column headers to rows    
		lvRow1.addElement(laColumn_1_1);
		lvRow1.addElement(laColumn_1_2);
		lvRow1.addElement(laColumn_1_3);
		lvRow1.addElement(laColumn_1_4);
		lvRow1.addElement(laColumn_1_5);
		lvRow1.addElement(laColumn_1_6);
		lvRow1.addElement(laColumn_1_7);
		lvRow1.addElement(laColumn_1_8);
		lvRow1.addElement(laColumn_1_9);

		lvRow2.addElement(laColumn_2_1);
		lvRow2.addElement(laColumn_2_2);
		lvRow2.addElement(laColumn_2_3);
		lvRow2.addElement(laColumn_2_4);
		lvRow2.addElement(laColumn_2_5);
		lvRow2.addElement(laColumn_2_6);
		lvRow2.addElement(laColumn_2_7);
		lvRow2.addElement(laColumn_2_8);
		lvRow2.addElement(laColumn_2_9);
		lvRow2.addElement(laColumn_2_10);

		// defect 10207 
		// Reorganize to print for all selected offices where data
		//  exists;  also print Exception Report

		// Add Header data according to whether Invoice or Date Range
		if (caInvHistUIData.getInvoiceIndi() > 0)
		{
			cvHeader.addElement(RECINV_HEADER);
			cvHeader.addElement(caInvHistUIData.getInvoiceNo());
		}
		else
		{
			cvHeader.addElement(TXT_HISTORY_DATE_FROM);

			cvHeader.addElement(
				caInvHistUIData.getBeginDate().toString()
					+ TXT_SPACE_TO_SPACE
					+ caInvHistUIData.getEndDate().toString());
		}

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

				InventoryReceiveHistoryReportData laInvRecHistRptData =
					(
						InventoryReceiveHistoryReportData) lvResults
							.elementAt(
						i);

				liOfcId = laInvRecHistRptData.getOfcIssuanceNo();

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
				formatData(laInvRecHistRptData);
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
			System.err.println(ReportConstant.RPT_3072_RPT_ERR);
			aeNFEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNFEx);
			leRTSEx.writeExceptionToLog();
		}
		catch (Exception aeEx)
		{
			System.err.println(ReportConstant.RPT_3072_RPT_ERR);
			aeEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
		}
		// end defect 10207 
	}

	/**
	 * Generate Attributes
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
	 * Run query and return results.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// come back to this later
		// Generating Demo data to display.
		Vector lvData = new Vector();

		InventoryReceiveHistoryReportData laInvRecHistRptData =
			new InventoryReceiveHistoryReportData();
		laInvRecHistRptData.setSubStaId(0);
		laInvRecHistRptData.setItmCd("DPP");
		laInvRecHistRptData.setItmCdDesc("DISABLED PERSON PLT");
		laInvRecHistRptData.setInvItmYr(2001);
		laInvRecHistRptData.setInvQty(54);
		laInvRecHistRptData.setInvItmNo("1SPBL");
		laInvRecHistRptData.setInvEndNo("9SPBS");
		laInvRecHistRptData.setInvcNo("S41960");
		laInvRecHistRptData.setInvcCorrIndi(0);
		laInvRecHistRptData.setTransEmpId("JACKIEW");
		laInvRecHistRptData.setOfcIssuanceNo(208);
		laInvRecHistRptData.setTransWsId(100);
		laInvRecHistRptData.setTransAMDate(371071);
		laInvRecHistRptData.setTransTime(64407);
		lvData.addElement(laInvRecHistRptData);

		InventoryReceiveHistoryReportData laInvRecHistRptData1 =
			new InventoryReceiveHistoryReportData();
		laInvRecHistRptData1.setSubStaId(0);
		laInvRecHistRptData1.setItmCd("TLP");
		laInvRecHistRptData1.setItmCdDesc("TRLR PLT");
		laInvRecHistRptData1.setInvItmYr(2001);
		laInvRecHistRptData1.setInvQty(50);
		laInvRecHistRptData1.setInvItmNo("50XHJM");
		laInvRecHistRptData1.setInvEndNo("99XHJM");
		laInvRecHistRptData1.setInvcNo("S41960");
		laInvRecHistRptData1.setInvcCorrIndi(0);
		laInvRecHistRptData1.setTransEmpId("JACKIEW");
		laInvRecHistRptData1.setOfcIssuanceNo(208);
		laInvRecHistRptData1.setTransWsId(100);
		laInvRecHistRptData1.setTransAMDate(371071);
		laInvRecHistRptData1.setTransTime(64407);
		lvData.addElement(laInvRecHistRptData1);

		InventoryReceiveHistoryReportData laInvRecHistRptData2 =
			new InventoryReceiveHistoryReportData();
		laInvRecHistRptData2.setSubStaId(0);
		laInvRecHistRptData2.setItmCd("FTP");
		laInvRecHistRptData2.setItmCdDesc("FARM TRUCK PLT");
		laInvRecHistRptData2.setInvItmYr(2001);
		laInvRecHistRptData2.setInvQty(100);
		laInvRecHistRptData2.setInvItmNo("N1V675");
		laInvRecHistRptData2.setInvEndNo("N1V774");
		laInvRecHistRptData2.setInvcNo("S41960");
		laInvRecHistRptData2.setInvcCorrIndi(0);
		laInvRecHistRptData2.setTransEmpId("JACKIEW");
		laInvRecHistRptData2.setOfcIssuanceNo(208);
		laInvRecHistRptData2.setTransWsId(100);
		laInvRecHistRptData2.setTransAMDate(371071);
		laInvRecHistRptData2.setTransTime(64407);
		lvData.addElement(laInvRecHistRptData2);

		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);
		lvData.addElement(laInvRecHistRptData2);

		return lvData;
	}

	/**
	 * Main method for stand alone testing.
	 *  
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant
					.RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_ID);

		GenReceiveInventoryHistoryReport laIHR =
			new GenReceiveInventoryHistoryReport(
				ReportConstant
					.RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_TITLE,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laIHR.queryData(lsQuery);
		laIHR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(SAMPLE_FILENAME);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laIHR.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport = new FrmPreviewReport(SAMPLE_FILENAME);
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
