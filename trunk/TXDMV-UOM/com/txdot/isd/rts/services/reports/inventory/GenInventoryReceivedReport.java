package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.MFInventoryAllocationData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * GenInventoryReceivedReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arredondo	10/08/2001	New Class
 * B Arredondo	04/09/2002	Fixed bug 3436 Length of Itm Name
 * Ray Rowehl	05/08/2002	Defect CQU100003790
 *							runtime rolls on to next page if
 *							there are 19 items..  fixed
 * Ray Rowehl	05/30/2002	Work on spacing if Item Number is a 
 * 							length of 12.
 *							Also rightAlign Qty.
 *							defect CQU100004195 is also related.
 *							defect CQU100004151
 * B Arredondo	07/01/2002	Defect CQU100004323--Runtime rolls
 * 							to next page after 19 items--fixed.
 * Min Wang		12/03/2003	Print OrigInvQty, OrigInvItmNo, and 
 *							OrigInvItmEndNo when they are not
 *							new and not dummy. 
 *							Modified formatReport() and 
 *							Reorganized the header of class.
 *							Defect 6512. Version 5.1.5 Fix2.
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3           
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3  
 * Min Wang		08/01/2005	Remove item code from report.
 * 							delete multiple constants
 * 							modify multiple constants
 * 							defect 8269 Ver 5.2.2 Fix 6
 * K Harrell	08/12/2005 	Add'l formatting work
 *							defect 8269 Ver 5.2.2 Fix 6   
 * Ray Rowehl	09/29/2005	Constants work
 * 							defect 7890 Ver 5.2.3 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport(MFInventoryAllocationData)
 * 							defect 8628 Ver Defect_POS_F     
 * ---------------------------------------------------------------------
 */

/**
 * This class generates the Inventory Received Report
 *
 * @version	Defect_POS_F	08/10/2009 
 * @author	Becky Arredondo
 * <br>Creation Date:		10/08/2001 03:49:24
 */

public class GenInventoryReceivedReport extends ReportTemplate
{
	// lengths in the order of appearance on the report lines
	private static final int INVITEM1_LENGTH = 27;
	private static final int INVITEM2_LENGTH = 4;
	private static final int INVDESC_LENGTH = 114;
	private static final int INVINV_LENGTH = 35;
	private static final int INVDASH_LENGTH = 35;
	private static final int INVQTY_LENGTH = 8;
	private static final int INVNO_LENGTH = 12;
	private static final int INVNO1_LENGTH = 12;
	private static final int INV_LENGTH = 1;
	private static final int INVREC_LENGTH = 35;
	private static final int INVDASH1_LENGTH = 35;
	private static final int INVQTY1_LENGTH = 8;
	private static final int INVNO2_LENGTH = 12;
	private static final int INVNO3_LENGTH = 12;

	// Starting Points in order on the line
	private static final int INVITEM1_STARTPT = 2;
	private static final int INVDESC_STARTPT = 2;
	private static final int INVITEM2_STARTPT = 35;
	private static final int INVDASH_STARTPT = 45;
	private static final int INVINV_STARTPT = 45;
	private static final int INVQTY_STARTPT = 45;
	private static final int INVNO_STARTPT = 53;
	private static final int INVNO1_STARTPT = 67;
	private static final int INV_STARTPT = 85;
	private static final int INVDASH1_STARTPT = 91;
	private static final int INVREC_STARTPT = 91;
	private static final int INVQTY1_STARTPT = 91;
	private static final int INVNO2_STARTPT = 100;
	private static final int INVNO3_STARTPT = 114;

	private static final int WHITESPACE = 5;

	//	Identifying the headers
	private static final String INVITEM_HEADER = "ITEM";
	private static final String INVDESC_HEADER = "DESCRIPTION";
	private static final String INVYEAR_HEADER = "YEAR";
	private static final String INVQTY_HEADER = "QUANTITY";
	private static final String INVBEGIN_HEADER = "BEGIN ";
	//right align
	private static final String INVEND_HEADER = "END  "; //right align
	private static final String INVNO_HEADER = "NUMBER"; //right align

	private static final String INVINV_HEADER = "INVOICED ITEM";
	//center
	private static final String INV_HEADER = "|";
	private static final String INVREC_HEADER = "RECEIVED ITEM";
	//center
	private static final String INVDASH_HEADER =
		"--------------------------------------";

	private static final String SAMPLE_FILENAME =
		"c:\\InvReceivedRpt.RPT";
	private static final String SAMPLE_QUERY = "Select * FROM RTS_TBL";

	private static final String TXT_DESTINATION = "DESTINATION";
	private static final String TXT_INVOICE_NO = "INVOICE NO";
	private static final String TXT_SPACE_IN_SPACE = " in ";
	private static final String TXT_TRANSID = "TRANSID";

	/**
	 * GenInventoryReceivedReport constructor
	 */
	public GenInventoryReceivedReport()
	{
		super();
	}

	/**
	 * GenInventoryReceivedReport constructor
	 */
	public GenInventoryReceivedReport(
		String asRptName,
		ReportProperties asRptProps)
	{
		super(asRptName, asRptProps);
	}

	/**
	 * Actual formatting of report.
	 * 
	 * @param aaMFInvAllcData MFInventoryAllocationData
	 */
	public void formatReport(MFInventoryAllocationData aaMFInvAllcData)
	{
		//Creating Vectors
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();
		Vector lvRow3 = new Vector();
		Vector lvRow4 = new Vector();

		//Adding additional Header information
		lvHeader.addElement(TXT_INVOICE_NO);
		lvHeader.addElement(
			aaMFInvAllcData.getMFInvAckData().getInvcNo());
		lvHeader.addElement(TXT_TRANSID);
		lvHeader.addElement(
			aaMFInvAllcData.getTransCd() == null
				? CommonConstant.STR_SPACE_EMPTY
				: aaMFInvAllcData.getTransCd());
		lvHeader.addElement(TXT_DESTINATION);
		lvHeader.addElement(
			aaMFInvAllcData.getMFInvAckData().getDest().toUpperCase()
				+ TXT_SPACE_IN_SPACE
				+ aaMFInvAllcData
					.getMFInvAckData()
					.getRcveInto()
					.toUpperCase());

		Vector lvResults = (Vector) aaMFInvAllcData.getInvAlloctnData();

		//Column Headers for Row 1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				INVINV_HEADER,
				INVINV_STARTPT,
				INVINV_LENGTH,
				ColumnHeader.CENTER);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(INV_HEADER, INV_STARTPT, INV_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				INVREC_HEADER,
				INVREC_STARTPT,
				INVREC_LENGTH,
				ColumnHeader.CENTER);

		//Column Headers for Row 2            
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				INVDASH_HEADER,
				INVDASH_STARTPT,
				INVDASH_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(INV_HEADER, INV_STARTPT, INV_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				INVDASH_HEADER,
				INVDASH1_STARTPT,
				INVDASH1_LENGTH);

		//Column Headers for Row 3
		ColumnHeader laColumn_3_1 =
			new ColumnHeader(
				INVITEM_HEADER,
				INVITEM1_STARTPT,
				INVITEM1_LENGTH);
		ColumnHeader laColumn_3_2 =
			new ColumnHeader(
				INVITEM_HEADER,
				INVITEM2_STARTPT,
				INVITEM2_LENGTH);
		ColumnHeader laColumn_3_3 =
			new ColumnHeader(
				INVBEGIN_HEADER,
				INVNO_STARTPT,
				INVNO_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_3_4 =
			new ColumnHeader(
				INVEND_HEADER,
				INVNO1_STARTPT,
				INVNO1_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_3_5 =
			new ColumnHeader(INV_HEADER, INV_STARTPT, INV_LENGTH);
		ColumnHeader laColumn_3_6 =
			new ColumnHeader(
				INVBEGIN_HEADER,
				INVNO2_STARTPT,
				INVNO2_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_3_7 =
			new ColumnHeader(
				INVEND_HEADER,
				INVNO3_STARTPT,
				INVNO3_LENGTH,
				ColumnHeader.RIGHT);

		//Column Headers for Row 4
		ColumnHeader laColumn_4_1 =
			new ColumnHeader(
				INVDESC_HEADER,
				INVITEM1_STARTPT,
				INVITEM1_LENGTH);
		ColumnHeader laColumn_4_2 =
			new ColumnHeader(
				INVYEAR_HEADER,
				INVITEM2_STARTPT,
				INVITEM2_LENGTH);
		ColumnHeader laColumn_4_3 =
			new ColumnHeader(
				INVQTY_HEADER,
				INVQTY_STARTPT,
				INVQTY_LENGTH);
		ColumnHeader laColumn_4_4 =
			new ColumnHeader(
				INVNO_HEADER,
				INVNO_STARTPT,
				INVNO_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_5 =
			new ColumnHeader(
				INVNO_HEADER,
				INVNO1_STARTPT,
				INVNO1_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_6 =
			new ColumnHeader(INV_HEADER, INV_STARTPT, INV_LENGTH);
		ColumnHeader laColumn_4_7 =
			new ColumnHeader(
				INVQTY_HEADER,
				INVQTY1_STARTPT,
				INVQTY1_LENGTH);
		ColumnHeader laColumn_4_8 =
			new ColumnHeader(
				INVNO_HEADER,
				INVNO2_STARTPT,
				INVNO2_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_9 =
			new ColumnHeader(
				INVNO_HEADER,
				INVNO3_STARTPT,
				INVNO3_LENGTH,
				ColumnHeader.RIGHT);

		//Alignment of column headers to rows
		lvRow1.addElement(laColumn_1_1);
		lvRow1.addElement(laColumn_1_2);
		lvRow1.addElement(laColumn_1_3);

		lvRow2.addElement(laColumn_2_1);
		lvRow2.addElement(laColumn_2_2);
		lvRow2.addElement(laColumn_2_3);

		lvRow3.addElement(laColumn_3_1);
		lvRow3.addElement(laColumn_3_2);
		lvRow3.addElement(laColumn_3_3);
		lvRow3.addElement(laColumn_3_4);
		lvRow3.addElement(laColumn_3_5);
		lvRow3.addElement(laColumn_3_6);
		lvRow3.addElement(laColumn_3_7);

		lvRow4.addElement(laColumn_4_1);
		lvRow4.addElement(laColumn_4_2);
		lvRow4.addElement(laColumn_4_3);
		lvRow4.addElement(laColumn_4_4);
		lvRow4.addElement(laColumn_4_5);
		lvRow4.addElement(laColumn_4_6);
		lvRow4.addElement(laColumn_4_7);
		lvRow4.addElement(laColumn_4_8);
		lvRow4.addElement(laColumn_4_9);

		//Adding ColumnHeader Information	
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRow2);
		lvTable.addElement(lvRow3);
		lvTable.addElement(lvRow4);

		Vector lvMore = new Vector();
		String lsTxt = aaMFInvAllcData.getRptText();
		String lsRptWidth =
			this.caRpt.blankSpaces(
				this.caRptProps.getPageWidth() - lsTxt.length());
		lvMore.addElement(lsTxt + lsRptWidth);

		InventoryAllocationUIData laInvAllcUIData =
			new InventoryAllocationUIData();

		int i = 0;
		if (!(lvResults == null) && (lvResults.size() > 0))
		{
			while (i < lvResults.size()) //Loop through the results
			{
				generateHeader(lvHeader, lvMore, lvTable);
				// runtime running off end of page.
				int j = this.caRptProps.getPageHeight() - WHITESPACE;
				//Get Available lines on the page
				for (int k = this.caRpt.getCurrX(); k < j; k = k + 3)
				{
					if (i < lvResults.size())
					{
						laInvAllcUIData =
							(
								InventoryAllocationUIData) lvResults
									.elementAt(
								i);
						int liStatusIndicator =
							laInvAllcUIData.getOrigItmModfyIndi();

						if (liStatusIndicator
							!= InventoryConstant.ITM_INDI_IGNORED)
						{
							ItemCodesData laItmCds =
								ItemCodesCache.getItmCd(
									laInvAllcUIData.getItmCd());
							this.caRpt.print(
								laItmCds == null
									? CommonConstant.STR_SPACE_EMPTY
									: laItmCds.getItmCdDesc(),
								INVITEM1_STARTPT,
								INVITEM1_LENGTH);
							int year = laInvAllcUIData.getInvItmYr();
							this.caRpt.print(
								year == 0
									? CommonConstant.STR_SPACE_EMPTY
									: String.valueOf(year),
								INVITEM2_STARTPT,
								INVITEM2_LENGTH);
							// Only print OrigInvQty, OrigInvItmNo, 
							// OrigInvItmEndNo when they are not new and 
							// not dummy.
							if (liStatusIndicator
								!= InventoryConstant.ITM_INDI_NEW
								&& !aaMFInvAllcData
									.getMFInvAckData()
									.getInvcNo()
									.substring(0, 1)
									.equalsIgnoreCase(
										InventoryConstant
											.INVOICE_PREFIX_DUMMY))
							{
								this.caRpt.rightAlign(
									String.valueOf(
										laInvAllcUIData
											.getOrigInvQty()),
									INVQTY_STARTPT,
									INVQTY_LENGTH);
								this.caRpt.rightAlign(
									laInvAllcUIData.getOrigInvItmNo(),
									INVNO_STARTPT,
									INVNO_LENGTH);
								this.caRpt.rightAlign(
									laInvAllcUIData
										.getOrigInvItmEndNo(),
									INVNO1_STARTPT,
									INVNO1_LENGTH);
							}

							if (liStatusIndicator
								!= InventoryConstant.ITM_INDI_REMOVED)
							{
								this.caRpt.rightAlign(
									String.valueOf(
										laInvAllcUIData.getInvQty()),
									INVQTY1_STARTPT,
									INVQTY1_LENGTH);
								this.caRpt.rightAlign(
									laInvAllcUIData.getInvItmNo(),
									INVNO2_STARTPT,
									INVNO2_LENGTH);
								this.caRpt.rightAlign(
									laInvAllcUIData.getInvItmEndNo(),
									INVNO3_STARTPT,
									INVNO3_LENGTH);
							}

							this.caRpt.nextLine();
							this.caRpt.print(
								laInvAllcUIData.getStatusDesc() == null
									|| laInvAllcUIData
										.getStatusDesc()
										.equals(
										InventoryConstant.VERIFIED_OK)
										? CommonConstant.STR_SPACE_EMPTY
										: laInvAllcUIData.getStatusDesc(),
								INVDESC_STARTPT,
								INVDESC_LENGTH);
							this.caRpt.nextLine();
							this.caRpt.blankLines(1);
						}
						i = i + 1;
					}
				}
				// defect 8628 
				//if (i > lvResults.size() - 1)
				//{
				//	generateEndOfReport();
				//}
				//generateFooter();
				generateFooter(i > lvResults.size() - 1);
				// end defect 8628  
			}
		}
		else
		{
			generateHeader(lvHeader, lvTable);
			generateNoRecordFoundMsg();
			// defect 8628 
			// generateFooter();
			generateFooter(true);
			// end defect 8628 
		}
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * This version is not actually used.
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * generateAttributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * main - used to start class as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3021_INVENTORY_RECEIVE_REPORT_ID);
		GenInventoryReceivedReport laIRR =
			new GenInventoryReceivedReport(
				ReportConstant.RPT_3021_INVENTORY_RECEIVE_REPORT_TITLE,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laIRR.queryData(lsQuery);
		laIRR.formatReport(lvQueryResults);

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

		laPout.print(laIRR.caRpt.getReport().toString());
		laPout.close();

		// Display the report
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
			// defect 7590
			// change setVisible to setVisibleRTS
			laFrmPreviewReport.setVisible(true);
			// end defect 7590
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Query Data
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// look at constants later
		// Generating Demo data to display.
		Vector lvData = new Vector();

		InventoryReceiveHistoryReportData laInvRecRptData =
			new InventoryReceiveHistoryReportData();
		laInvRecRptData.setItmCd("DPP");
		laInvRecRptData.setItmCdDesc("DISABLED PERSON PLT");
		laInvRecRptData.setInvItmYr(2001);
		laInvRecRptData.setInvQty(54);
		laInvRecRptData.setInvItmNo("1SPBL");
		laInvRecRptData.setInvEndNo("9SPBS");
		//	lInvRecRptData.setInvQty(54);
		//	lInvRecRptData.setInvItmNo("1SPBL");
		//	lInvRecRptData.setInvEndNo("9SPBS");
		laInvRecRptData.setDelInvReasnTxt(
			"ITEM REJECTED BY SYSTEM: IT LOOKED FUNNY.");
		lvData.addElement(laInvRecRptData);

		InventoryReceiveHistoryReportData laInvRecRptData1 =
			new InventoryReceiveHistoryReportData();
		laInvRecRptData1.setItmCd("DPS");
		laInvRecRptData1.setItmCdDesc("PERSONALIZED PLT");
		laInvRecRptData1.setInvItmYr(2001);
		laInvRecRptData1.setInvQty(50);
		laInvRecRptData1.setInvItmNo("1541SPBL");
		laInvRecRptData1.setInvEndNo("1549SPBS");
		//	lInvRecRptData1.setInvQty(54);
		//	lInvRecRptData1.setInvItmNo("1541SPBL");
		//	lInvRecRptData1.setInvEndNo("1549SPBS");
		laInvRecRptData1.setDelInvReasnTxt(
			"ITEM ACCEPTED AND EATEN BY SYSTEM.");
		lvData.addElement(laInvRecRptData1);
		return lvData;
	}
}
