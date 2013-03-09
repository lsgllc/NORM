package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.DeleteReasonsCache;
import com.txdot.isd.rts.services.data.DeleteReasonsData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * GenInventoryDeletedReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arredondo 	08/30/2001	New Class
 * Min Wang 	01/29/2002	CQU100000895) Fixed	Trans ID
 *							Item Year should not show if it is 0
 * Min Wang		01/30/2002	(CQU100000904) Fixed
 *    						Item Code
 *							Reason Description
 * Min Wang		05/21/2002	(CQU1000004003) Fixed Reformat 
 * 							begin and end number to fit the
 * 							numbers are 10 digits on the deleted 
 * 							inventory report.
 * Ray Rowehl	01/05/2004	Remove extra () from for statement.
 * 							modified formatReport()
 * 							defect 6739  Ver 5.1.5 Fix 2
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3       
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3  
 * Min Wang		08/01/2005	Remove item code from report.
 * 							delete multiple constants
 * 							modity INVREASN_STARTPT
 * 							modify formatReport() 
 * 							defect 8269 Ver 5.2.2 Fix 6
 * K Harrell	08/12/2005 	Add'l formatting work. Modified constants 
 * 							to left justify report. 
 *							Now using "NUMBER" vs. "NO" 
 *							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	09/27/2005	Constants work for 5.2.3
 * 							defect 7890 Ver 5.2.3.     
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F   
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Inventory Deleted Report.
 *
 * @version	Defect_POS_F	08/10/2009	
 * @author	Becky Arredondo
 * <br>Creation Date:		08/30/2001 08:01:22
 */
public class GenInventoryDeletedReport extends ReportTemplate
{
	private static final int NUMBER_OF_LINES_PER_RECORD = 2;

	//identifing header start pts and lengths
	private static final int INVBEGIN_STARTPT = 52;
	private static final int INVBEGIN_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int INVDESC_STARTPT = 2;
	private static final int INVDESC_LENGTH = 27;
	private static final int INVDEL_STARTPT = 78;
	private static final int INVDEL_LENGTH = 30;
	private static final int INVEND_STARTPT = 64;
	private static final int INVEND_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int INVITEM1_STARTPT = 02;
	private static final int INVITEM1_LENGTH = 27;
	private static final int INVITEM2_STARTPT = 36;
	private static final int INVITEM2_LENGTH = 4;
	private static final int INVNUMBER_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int INVNUMBER1_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;
	private static final int INVQTY_STARTPT = 43;
	private static final int INVQTY_LENGTH = 8;
	private static final int INVREASNCD_STARTPT = 78; //DELETE
	private static final int INVREASNCD_LENGTH = 30;
	private static final int INVREASN_STARTPT = 2;
	private static final int INVTRANSID_STARTPT = 113;
	private static final int INVTRANSID_LENGTH = 20;
	private static final int INVYEAR_STARTPT = 36;
	private static final int INVYEAR_LENGTH = 4;

	private static final String DELREASONDESC = "REASON DESCRIPTION: ";

	// header text
	private static final String INVBEGIN_HEADER = "BEGIN";
	private static final String INVDEL_HEADER = "DELETE";
	private static final String INVDESC_HEADER = "DESCRIPTION";
	private static final String INVEND_HEADER = "END";
	private static final String INVITEM_HEADER = "ITEM";
	private static final String INVNUMBER_HEADER = "NUMBER";
	private static final String INVQTY_HEADER = "QUANTITY";
	private static final String INVREASNCD_HEADER = "REASON CODE";
	private static final String INVTRANSID_HEADER = "TRANSACTION ID";
	private static final String INVYEAR_HEADER = "YEAR";

	private static final int SAMPLE_DELREASCD = 0;
	private static final int SAMPLE_ITMQTY = 54;
	private static final int SAMPLE_ITMYR = 2001;
	private static final int SAMPLE_OFC = 208;

	private static final String SAMPLE_FILE_NAME =
		"c:\\InvDeletedRpt.txt";
	private static final String SAMPLE_ITMCD_1 = "DPP";
	private static final String SAMPLE_ITMCD_2 = "PLP";
	private static final String SAMPLE_ITMCD_3 = "DPS";
	private static final String SAMPLE_ITMDESC_1 =
		"DISABLED PERSON PLT";
	private static final String SAMPLE_ITMDESC_2 = "PERSONALIZED PLT";
	private static final String SAMPLE_ITMDESC_3 = "PEEPING PETER PLT";
	private static final String SAMPLE_ITMNO_1 = "16455516WB";
	private static final String SAMPLE_ITMNO_2 = "1SPBL";
	private static final String SAMPLE_ITMNO_3 = "9SPBS";

	private static final String SAMPLE_QUERY = "Select * FROM RTS_TBL";
	private static final String SAMPLE_REASON_TXT_1 =
		"REASON DESCRIPTION: LOST CAR AND CANNOT FIND";
	private static final String SAMPLE_REASON_TXT_2 =
		"REASON DESCRIPTION: WHERE'S THE CAR?";
	private static final String SAMPLE_REASON_TXT_3 =
		"REASON DESCRIPTION: TOO SEXY FOR MY CAR";

	/**
	 * GenInventoryDeletedReport constructor
	 */
	public GenInventoryDeletedReport()
	{
		super();
	}

	/**
	 * GenInventoryDeletedReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenInventoryDeletedReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Formatting data for the report.
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();

		//Column Headers for Row 1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				INVITEM_HEADER,
				INVITEM1_STARTPT,
				INVITEM1_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				INVITEM_HEADER,
				INVITEM2_STARTPT,
				INVITEM2_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				INVBEGIN_HEADER,
				INVBEGIN_STARTPT,
				INVBEGIN_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				INVEND_HEADER,
				INVEND_STARTPT,
				INVEND_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				INVDEL_HEADER,
				INVDEL_STARTPT,
				INVDEL_LENGTH);

		//Column Headers for Row 2  
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				INVDESC_HEADER,
				INVDESC_STARTPT,
				INVDESC_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				INVYEAR_HEADER,
				INVYEAR_STARTPT,
				INVYEAR_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				INVQTY_HEADER,
				INVQTY_STARTPT,
				INVQTY_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				INVNUMBER_HEADER,
				INVBEGIN_STARTPT,
				INVNUMBER_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				INVNUMBER_HEADER,
				INVEND_STARTPT,
				INVNUMBER1_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				INVREASNCD_HEADER,
				INVREASNCD_STARTPT,
				INVREASNCD_LENGTH);
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				INVTRANSID_HEADER,
				INVTRANSID_STARTPT,
				INVREASNCD_LENGTH);

		//Alignment of column headers to rows 
		lvRow1.addElement(laColumn_1_1);
		lvRow1.addElement(laColumn_1_2);
		lvRow1.addElement(laColumn_1_3);
		lvRow1.addElement(laColumn_1_4);
		lvRow1.addElement(laColumn_1_5);

		lvRow2.addElement(laColumn_2_1);
		lvRow2.addElement(laColumn_2_2);
		lvRow2.addElement(laColumn_2_3);
		lvRow2.addElement(laColumn_2_4);
		lvRow2.addElement(laColumn_2_5);
		lvRow2.addElement(laColumn_2_6);
		lvRow2.addElement(laColumn_2_7);

		//Adding ColumnHeader Information	
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRow2);

		InventoryAllocationUIData laDataline =
			new InventoryAllocationUIData();

		int i = SAMPLE_DELREASCD;
		if (!(avResults == null)
			&& (avResults.size() > SAMPLE_DELREASCD))
		{
			while (i < avResults.size())
			{
				//Loop through the results
				generateHeader(lvHeader, lvTable);
				int j = getNoOfDetailLines();
				//Get Available lines on the page
				for (int k = SAMPLE_DELREASCD;
					k <= j;
					k = k + NUMBER_OF_LINES_PER_RECORD)
				{
					if (i < avResults.size())
					{
						laDataline =
							(
								InventoryAllocationUIData) avResults
									.elementAt(
								i);
						String lsDesc = laDataline.getItmCdDesc();
						this.caRpt.print(
							lsDesc,
							INVITEM1_STARTPT,
							INVITEM1_LENGTH);

						if (laDataline.getInvItmYr()
							!= SAMPLE_DELREASCD)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								INVYEAR_STARTPT,
								INVYEAR_LENGTH);
						}

						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							INVQTY_STARTPT,
							INVQTY_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							INVBEGIN_STARTPT,
							INVNUMBER_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							INVEND_STARTPT,
							INVNUMBER1_LENGTH);
						DeleteReasonsData laDRD =
							DeleteReasonsCache.getDelReason(
								laDataline.getDelInvReasnCd());
						this.caRpt.print(
							laDRD.getDelInvReasn(),
							INVREASNCD_STARTPT,
							INVREASNCD_LENGTH);
						this.caRpt.print(
							laDataline.getTransId(),
							INVTRANSID_STARTPT,
							INVTRANSID_LENGTH);
						this.caRpt.nextLine();

						this.caRpt.print(
							DELREASONDESC
								+ laDataline.getDelInvReasnTxt(),
							INVREASN_STARTPT,
							(DELREASONDESC
								+ laDataline.getDelInvReasnTxt())
								.length());
						this.caRpt.nextLine();

						i = i + 1;
					}
				}
				// defect 8628 
				//if (i > avResults.size() - 1)
				//{
				//	generateEndOfReport();
				//}
				//generateFooter();
				generateFooter(i > avResults.size() - 1);
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
	 * Generate Attributes
	 * 
	 * @param Report int
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Main method for stand alone testing.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3001_INVENTORY_DELETED_REPORT_ID);
		GenInventoryDeletedReport laIdr =
			new GenInventoryDeletedReport(
				ReportConstant.RPT_3001_INVENTORY_DELETE_REPORT_TITLE,
				laRptProps);

		// Generating Demo data to display.
		String lsQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laIdr.queryData(lsQuery);
		laIdr.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(SAMPLE_FILE_NAME);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laIdr.caRpt.getReport().toString());
		laPout.close();

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
					System.exit(SAMPLE_DELREASCD);
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
	 * Data generated for demo
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvData = new Vector();

		InventoryAllocationUIData laDataline =
			new InventoryAllocationUIData();
		laDataline.setItmCd(SAMPLE_ITMCD_1);
		laDataline.setItmCdDesc(SAMPLE_ITMDESC_1);
		laDataline.setInvItmYr(SAMPLE_ITMYR);
		laDataline.setInvQty(SAMPLE_ITMQTY);
		laDataline.setInvItmNo(SAMPLE_ITMNO_1);
		laDataline.setInvItmEndNo(SAMPLE_ITMNO_1);
		laDataline.setDelInvReasnCd(SAMPLE_DELREASCD);
		laDataline.setOfcIssuanceNo(SAMPLE_OFC);
		laDataline.setDelInvReasnTxt(SAMPLE_REASON_TXT_1);
		lvData.addElement(laDataline);

		InventoryAllocationUIData laDataline1 =
			new InventoryAllocationUIData();
		laDataline1.setItmCd(SAMPLE_ITMCD_2);
		laDataline1.setItmCdDesc(SAMPLE_ITMDESC_2);
		laDataline1.setInvItmYr(SAMPLE_ITMYR);
		laDataline1.setInvQty(SAMPLE_ITMQTY);
		laDataline1.setInvItmNo(SAMPLE_ITMNO_2);
		laDataline1.setInvItmEndNo(SAMPLE_ITMNO_2);
		laDataline1.setDelInvReasnCd(SAMPLE_DELREASCD);
		laDataline1.setOfcIssuanceNo(SAMPLE_OFC);
		laDataline1.setDelInvReasnTxt(SAMPLE_REASON_TXT_2);
		lvData.addElement(laDataline1);

		InventoryAllocationUIData laDataline2 =
			new InventoryAllocationUIData();
		laDataline2.setItmCd(SAMPLE_ITMCD_3);
		laDataline2.setItmCdDesc(SAMPLE_ITMDESC_3);
		laDataline2.setInvItmYr(SAMPLE_ITMYR);
		laDataline2.setInvQty(SAMPLE_ITMQTY);
		laDataline2.setInvItmNo(SAMPLE_ITMNO_3);
		laDataline2.setInvItmEndNo(SAMPLE_ITMNO_3);
		laDataline2.setDelInvReasnCd(SAMPLE_DELREASCD);
		laDataline2.setOfcIssuanceNo(SAMPLE_OFC);
		laDataline2.setDelInvReasnTxt(SAMPLE_REASON_TXT_3);
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
