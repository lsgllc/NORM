package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * GenInventoryAllocateReport.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * E LyBrand	10/03/2001	New Class
 * Min Wang		10/28/2002  Modified formatReport().
 * 							defect 4972 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from report and
 * 							reformat report.
 * 							modify generateColumnHeadings(),
 * 							printDataline(), multiple constants
 * 							defect 8269 Ver 5.2.2. Fix 6
 * K Harrell	08/12/2005	Add'l formatting work
 * 							Do not index past '-' on description
 *							modify generateColumnHeadings,
 *							printDataline()
 *							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	09/26/2005	Constants work.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	04/25/2006	Adjust headings to accommodate maximum 
 * 							Subcontractor Name Length on From & To
 * 							modify generateColumnHeadings()
 * 							defect 8327 Ver 5.2.3 	
 * K Harrell	01/02/2009	Streamline formatReport / remove use of 
 * 							interim arrays. 
 * 							modify formatReport()
 * 							defect 8383 Ver Defect_POS_D 	 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */

/**
 * Prepares Inventory Allocate Report
 *
 * @version	Defect_POS_F	08/10/2009
 * @author	Edward LyBrand
 * <br>Creation Date:		10/03/2001 07:35:55
 */

public class GenInventoryAllocateReport extends ReportTemplate
{
	private static final int SAMPLE_ITMYR_1 = 2001;

	//character locations for print
	private final static int START_POINT_01 = 1;
	private final static int START_POINT_37 = 37;
	private final static int START_POINT_50 = 50;
	private final static int START_POINT_52 = 52;
	private final static int START_POINT_64 = 64;
	private final static int START_POINT_68 = 68;
	private final static int START_POINT_79 = 79;
	private final static int START_POINT_83 = 83;
	private final static int START_POINT_99 = 99;

	//print string lengths
	private final static int LENGTH_03 = 3;
	private final static int LENGTH_04 = 4;
	private final static int LENGTH_05 = 5;
	private final static int LENGTH_06 = 6;
	private final static int LENGTH_08 = 8;
	private final static int LENGTH_10 = 10;
	private final static int LENGTH_12 = 12;
	private final static int LENGTH_14 = 14;
	private final static int LENGTH_17 = 17;
	private final static int LENGTH_32 = 32;
	// defect 8327
	// Resize to 60 (from 45) to accommodate 30 char description  
	private final static int LENGTH_60 = 60;
	// end defect 8327 

	//literals for print
	private static final String HDR_ALLOCATE_FROM = "ALLOCATE FROM:";
	private static final String HDR_ALLOCATE_TO = "ALLOCATE TO:";
	private static final String HDR_BEGIN = "BEGIN";
	private static final String HDR_END = "END";
	private static final String HDR_DESCRIPTION = "DESCRIPTION";
	private static final String HDR_ITEM = "ITEM";
	private static final String HDR_NUMBER = "NUMBER";
	private static final String HDR_QUANTITY = "QUANTITY";
	private static final String HDR_TRANSACTION_ID = "TRANSACTION ID";
	private static final String HDR_YEAR = "YEAR";

	private static final String SAMPLE_FILE_NAME =
		"c:\\RTS\\RPT\\INVALLOC.TXT";
	private static final String SAMPLE_INVID = "MO";
	private static final String SAMPLE_ITMCD_1 = "WS";
	private static final String SAMPLE_ITMDESC_1 =
		"WS - windshield sticker";
	private static final String SAMPLE_ITMNO_1 = "99WC";
	private static final String SAMPLE_ITMNO_2 = "98WC";
	private static final String SAMPLE_LOC = "MCLENNAN COUNTY";
	private static final String SAMPLE_QUERY = "Select * FROM RTS_TBL";
	private static final String SAMPLE_TRANSID_1 = "25610137164111159";
	private static final String SAMPLE_TRANSID_2 = "25610137164111160";

	/**
	 * GenInventoryAllocateReport constructor
	 */
	public GenInventoryAllocateReport(
		String asRptString,
		ReportProperties asRptProperties)
	{
		super(asRptString, asRptProperties);
	}

	/**
	 * Formulates the From Location String.
	 * 
	 * @param aaDataline InventoryAllocationUIData
	 * @return String
	 */
	private String formatFromLocation(InventoryAllocationUIData aaDataline)
	{
		String lsReturn = CommonConstant.STR_SPACE_EMPTY;
		String lsLocIdCode = aaDataline.getFromInvLocIdCd();
		if (lsLocIdCode.equals(InventoryConstant.CHAR_C))
		{
			lsReturn = aaDataline.getFromLoc();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_W))
		{
			lsReturn =
				InventoryConstant.STR_W.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getFromInvIdName();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_D))
		{
			lsReturn =
				InventoryConstant.STR_D.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getFromInvIdName();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_S))
		{
			lsReturn =
				InventoryConstant.STR_S.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getFromInvIdName();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_E))
		{
			lsReturn =
				InventoryConstant.STR_E.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getFromInvIdName();
		}
		return lsReturn;
	}

	/**
	 * Formats the To Location String.
	 * 
	 * @param aaDataline InventoryAllocationUIData
	 * @return String
	 */
	private String formatToLocation(InventoryAllocationUIData aaDataline)
	{
		String lsReturn = CommonConstant.STR_SPACE_EMPTY;

		String lsLocIdCode = aaDataline.getInvLocIdCd();
		if (lsLocIdCode.equals(InventoryConstant.CHAR_C))
		{
			String lsValue = aaDataline.getInvId();
			if (lsValue.equals(CommonConstant.STR_ZERO))
			{
				lsReturn =
					aaDataline.getToLoc()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_COLON
						+ CommonConstant.STR_SPACE_ONE
						+ InventoryConstant.TXT_CENTRAL;
			}
			else
			{
				lsReturn =
					aaDataline.getToLoc()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_DASH
						+ CommonConstant.STR_SPACE_ONE
						+ lsValue;
			}
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_W))
		{
			lsReturn =
				InventoryConstant.STR_W.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getToInvIdName();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_D))
		{
			lsReturn =
				InventoryConstant.STR_D.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getToInvIdName();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_S))
		{
			lsReturn =
				InventoryConstant.STR_S.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getToInvIdName();
		}
		else if (lsLocIdCode.equals(InventoryConstant.CHAR_E))
		{
			lsReturn =
				InventoryConstant.STR_E.toUpperCase()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ aaDataline.getToInvIdName();
		}
		return lsReturn;
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// defect 8383 
		// Streamline method 
		boolean lbMoreRecords = true;
		int liNumberOfRecords = avResults.size();
		int liRecordIndex = 0;

		// Print Column Headings 
		InventoryAllocationUIData laFirstInvData =
			(InventoryAllocationUIData) avResults.elementAt(0);

		generateColumnHeadings(
			formatFromLocation(laFirstInvData),
			formatToLocation(laFirstInvData));

		// get # of available printlines
		int liPrintLinesAvailable = getNoOfDetailLines();

		// print body
		try
		{
			while (lbMoreRecords)
			{
				// Process until (no more records || must start new page)  
				for (int k = 0;
					(lbMoreRecords && k < liPrintLinesAvailable - 1);
					k++)
				{
					if (liRecordIndex < liNumberOfRecords)
					{
						InventoryAllocationUIData laInvData =
							(
								InventoryAllocationUIData) avResults
									.elementAt(
								liRecordIndex);
						printDataline(
							laInvData.getItmCdDesc(),
							laInvData.getInvItmYr(),
							laInvData.getInvQty(),
							laInvData.getInvItmNo(),
							laInvData.getInvItmEndNo(),
							laInvData.getTransId());
						liRecordIndex++;
					}
					else
					{
						lbMoreRecords = false;
					}
				} // end for loop
				
				if (lbMoreRecords)
				{
					// print footer
					generateFooter();

					// print heading, intermediate text, column headings 
					generateColumnHeadings(
						formatFromLocation(laFirstInvData),
						formatToLocation(laFirstInvData));

				} // end if
			} //end while
		} //end try block
		// end defect 8383 
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		} //end catch block
		
		// defect 8628 
		// generateEndOfReport();
		// generateFooter();
		generateFooter(true); 
		// end defect 8628 
	}

	/**
	 * Generate Attributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Creates column heading and intermediate text
	 * 
	 * @param asAllocateFrom String
	 * @param asAllocationTo String
	 */
	public void generateColumnHeadings(
		String asAllocateFrom,
		String asAllocationTo)
	{
		//create containers
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		Vector lvRow_3 = new Vector(); //blank line
		Vector lvRow_4 = new Vector();
		Vector lvRow_5 = new Vector();

		//populate column labels

		//populate row 1 labels
		// defect 8327 
		// Use length of 60 vs. 45 to accommodate longer names	
		ColumnHeader laIntColumn_1_1 =
			new ColumnHeader(
				HDR_ALLOCATE_FROM,
				START_POINT_01,
				LENGTH_60);
		ColumnHeader laIntColumn_1_2 =
			new ColumnHeader(
				HDR_ALLOCATE_TO,
				START_POINT_50,
				LENGTH_60);
		lvRow_1.addElement(laIntColumn_1_1);
		lvRow_1.addElement(laIntColumn_1_2);

		// populate row 2 labels
		ColumnHeader laIntColumn_2_1 =
			new ColumnHeader(asAllocateFrom, START_POINT_01, LENGTH_60);
		ColumnHeader laIntColumn_2_2 =
			new ColumnHeader(asAllocationTo, START_POINT_50, LENGTH_60);
		// end defect 8327 	
		lvRow_2.addElement(laIntColumn_2_1);
		lvRow_2.addElement(laIntColumn_2_2);

		//populate column for line 3 "blank"
		ColumnHeader laColumn_3_1 =
			new ColumnHeader(
				CommonConstant.STR_SPACE_ONE,
				START_POINT_01,
				LENGTH_04);
		lvRow_3.addElement(laColumn_3_1);

		//populate column labels for print row 4
		ColumnHeader laColumn_4_1 =
			new ColumnHeader(HDR_ITEM, START_POINT_01, LENGTH_04);
		ColumnHeader laColumn_4_2 =
			new ColumnHeader(HDR_ITEM, START_POINT_37, LENGTH_04);
		ColumnHeader laColumn_4_3 =
			new ColumnHeader(HDR_BEGIN, START_POINT_68, LENGTH_05);
		ColumnHeader laColumn_4_4 =
			new ColumnHeader(HDR_END, START_POINT_83, LENGTH_03);
		lvRow_4.addElement(laColumn_4_1);
		lvRow_4.addElement(laColumn_4_2);
		lvRow_4.addElement(laColumn_4_3);
		lvRow_4.addElement(laColumn_4_4);

		//populate column labels for print row 5
		ColumnHeader laColumn_5_1 =
			new ColumnHeader(
				HDR_DESCRIPTION,
				START_POINT_01,
				LENGTH_12);
		ColumnHeader laColumn_5_2 =
			new ColumnHeader(HDR_YEAR, START_POINT_37, LENGTH_04);
		ColumnHeader laColumn_5_3 =
			new ColumnHeader(HDR_QUANTITY, START_POINT_52, LENGTH_08);
		ColumnHeader laColumn_5_4 =
			new ColumnHeader(HDR_NUMBER, START_POINT_68, LENGTH_06);
		ColumnHeader laColumn_5_5 =
			new ColumnHeader(HDR_NUMBER, START_POINT_83, LENGTH_06);
		ColumnHeader laColumn_5_6 =
			new ColumnHeader(
				HDR_TRANSACTION_ID,
				START_POINT_99,
				LENGTH_14);
		lvRow_5.addElement(laColumn_5_1);
		lvRow_5.addElement(laColumn_5_2);
		lvRow_5.addElement(laColumn_5_3);
		lvRow_5.addElement(laColumn_5_4);
		lvRow_5.addElement(laColumn_5_5);
		lvRow_5.addElement(laColumn_5_6);

		//add all rows to table
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		lvTable.addElement(lvRow_3);
		lvTable.addElement(lvRow_4);
		lvTable.addElement(lvRow_5);

		generateHeader(lvHeader, lvTable);
	}

	/**
	 * starts the application to produce Inventory Allocate Report
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		//create new graph of report class
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3011_INVENTORY_ALLOCATION_REPORT_ID);
		GenInventoryAllocateReport laGpr =
			new GenInventoryAllocateReport(
				ReportConstant.RPT_3011_INVENTORY_ALLOCATE_REPORT_TITLE,
				laRptProps);

		//extract dummy data for display
		String lsQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laGpr.queryData(lsQuery);
		laGpr.formatReport(lvQueryResults);

		//write completed report to local hard drive
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

		liPout.print(laGpr.caRpt.getReport().toString());
		liPout.close();

		//display report in preview window
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
			laFrmPreviewReport.setVisible(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * printDataline() prints body text of Inventory Allocate Report
	 * 
	 * @param asDescription String
	 * @param aiItmYr int
	 * @param aiInvQty int
	 * @param asInvItmNo String
	 * @param asInvItmEndNo String
	 * @param asTransId String
	 */
	private void printDataline(
		String asDescription,
		int aiItmYr,
		int aiInvQty,
		String asInvItmNo,
		String asInvItmEndNo,
		String asTransId)
	{
		//description
		this.caRpt.print(
			asDescription.trim(),
			START_POINT_01,
			LENGTH_32);

		//year
		if (aiItmYr != 0)
		{
			this.caRpt.print(
				String.valueOf(aiItmYr),
				START_POINT_37,
				LENGTH_04);
		}
		else
		{
			this.caRpt.print(
				CommonConstant.STR_SPACE_EMPTY,
				START_POINT_37,
				LENGTH_04);
		}

		// quantity
		this.caRpt.rightAlign(
			String.valueOf(aiInvQty),
			START_POINT_50,
			LENGTH_10);

		// begin number
		this.caRpt.rightAlign(asInvItmNo, START_POINT_64, LENGTH_10);

		// end number	
		this.caRpt.rightAlign(asInvItmEndNo, START_POINT_79, LENGTH_10);

		//transid		
		this.caRpt.print(
			String.valueOf(asTransId),
			START_POINT_99,
			LENGTH_17);

		this.caRpt.blankLines(1);
	}

	/**
	 * queryData() mimics data from db.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		//create container
		Vector lvResults = new Vector();

		//test rec1
		InventoryAllocationUIData laDataline =
			new InventoryAllocationUIData();
		laDataline.setItmCd(SAMPLE_ITMCD_1);
		laDataline.setItmCdDesc(SAMPLE_ITMDESC_1);
		laDataline.setInvItmYr(SAMPLE_ITMYR_1);
		laDataline.setInvQty(1);
		laDataline.setInvItmNo(SAMPLE_ITMNO_1);
		laDataline.setInvItmEndNo(SAMPLE_ITMNO_1);
		laDataline.setTransId(SAMPLE_TRANSID_1);
		laDataline.setFromLoc(SAMPLE_LOC);
		laDataline.setFromInvId(SAMPLE_INVID);
		laDataline.setToLoc(SAMPLE_LOC);
		laDataline.setInvId(SAMPLE_INVID);
		lvResults.addElement(laDataline);

		//test rec2
		InventoryAllocationUIData laDataline1 =
			new InventoryAllocationUIData();
		laDataline1.setItmCd(SAMPLE_ITMCD_1);
		laDataline1.setItmCdDesc(SAMPLE_ITMDESC_1);
		laDataline1.setInvItmYr(SAMPLE_ITMYR_1);
		laDataline1.setInvQty(1);
		laDataline1.setInvItmNo(SAMPLE_ITMNO_2);
		laDataline1.setInvItmEndNo(SAMPLE_ITMNO_2);
		laDataline1.setTransId(SAMPLE_TRANSID_2);
		laDataline1.setFromLoc(SAMPLE_LOC);
		laDataline1.setFromInvId(SAMPLE_INVID);
		laDataline1.setToLoc(SAMPLE_LOC);
		laDataline1.setInvId(SAMPLE_INVID);
		lvResults.addElement(laDataline1);

		return lvResults;
	}
}
