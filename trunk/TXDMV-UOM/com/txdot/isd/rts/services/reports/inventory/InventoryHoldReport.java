package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.reports.funds.InventorySummaryReportData;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 * InventoryHoldReport.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * M Wang		08/19/2002  Added generateNoRecordFoundMsg().
 * 							(CQU100004633)
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896
 * Min Wang		07/20/2005	Remove item code from report.
 * 							delete multiple constants
 * 							modify  ITEMCDDESC_STARTPT
 * 							modify formatReport() 
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	09/30/2005	Moved the data classes to services.data.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */
/**
 * How the user generates the Inventory Hold Report:
 *
 * 1) User selects Inventory option
 * 2) User selects Hold/Release suboption
 * 3) System does an inquiry for items on hold, and hold items pop up in a list  
 * 4) If hold button is clicked, an empty line item pops up.
 * 5) Can pick other items to put on hold, using a drop down.
 * 6) Choosing an item to put on hold, adds it to the list in #3. 
 * 7) Can highlight an entry and click release, to remove from hold list.
 * 8) Cannot issue inventory unless its been released.
 * 9) The user selects enter to print the report.
 *
 * @version	Defect_POS_F	08/10/2009
 * @author	Min Wang
 * <br>Creation Date:		09/20/2001 10:50:18
 */
public class InventoryHoldReport extends ReportTemplate
{
	private static final int INVLOCID_INVID_STARTPT = 1;
	private static final int INVLOCID_INVID_LENGTH = 11;
	// defect 8269
	//private static final int ITEMCD_STARTPT = 17;
	//private static final int ITEMCD_LENGTH = 9;
	//private static final int ITEMCDDESC_STARTPT = 27;
	private static final int ITEMCDDESC_STARTPT = 17;
	// defect 8269
	private static final int ITEMCDDESC_LENGTH = 27;
	private static final int ITEMYEAR_STARTPT = 55;
	private static final int ITEMYEAR_LENGTH = 9;
	private static final int QUANTITY_STARTPT = 69;
	private static final int QUANTITY_LENGTH = 8;
	private static final int INVITMNO_STARTPT = 80;
	private static final int INVITMNO_LENGTH = 12;
	private static final int INVITMENDNO_STARTPT = 98;
	private static final int INVITMENDNO_LENGTH = 10;
	private static final String INVLOCID_INVID_HEADER = "ENTITY & ID";
	private static final String INVITMENDNO_HEADER = "END NUMBER";
	private static final String INVITMNO_HEADER = "BEGIN NUMBER";
	// defect 8269
	//private static final String ITEMCD_HEADER = "ITEM CODE";
	//private static final String ITEMDESC_HEADER = "& DESCRIPTION";
	private static final String ITEMDESC_HEADER = "DESCRIPTION";
	// end defect 8269
	private static final String ITEMYEAR_HEADER = "ITEM YEAR";
	private static final String QUANTITY_HEADER = "QUANTITY";

	/**
	 * InventorySummaryReport constructor
	 */
	public InventoryHoldReport()
	{
		super();
	}

	/**
	 * InventoryHoldReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public InventoryHoldReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
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
		Vector lvColumn1 = new Vector();

		//LOADING COLUMN HEADERS
		ColumnHeader laColumn1 =
			new ColumnHeader(
				INVLOCID_INVID_HEADER,
				INVLOCID_INVID_STARTPT,
				INVLOCID_INVID_LENGTH);
		// defect 8269
		//ColumnHeader laColumn2 =
		//	new ColumnHeader(
		//		ITEMCD_HEADER,
		//		ITEMCD_STARTPT,
		//		ITEMCD_LENGTH);
		// end defect 8269
		ColumnHeader laColumn3 =
			new ColumnHeader(
				ITEMDESC_HEADER,
				ITEMCDDESC_STARTPT,
				ITEMCDDESC_LENGTH);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				ITEMYEAR_HEADER,
				ITEMYEAR_STARTPT,
				ITEMYEAR_LENGTH);
		ColumnHeader laColumn5 =
			new ColumnHeader(
				QUANTITY_HEADER,
				QUANTITY_STARTPT,
				QUANTITY_LENGTH);
		ColumnHeader laColumn6 =
			new ColumnHeader(
				INVITMNO_HEADER,
				INVITMNO_STARTPT,
				INVITMNO_LENGTH);
		ColumnHeader laColumn7 =
			new ColumnHeader(
				INVITMENDNO_HEADER,
				INVITMENDNO_STARTPT,
				INVITMENDNO_LENGTH);
		lvColumn1.addElement(laColumn1);
		// defect 8269
		//lvColumn1.addElement(laColumn2);
		// end defect 8269
		lvColumn1.addElement(laColumn3);
		lvColumn1.addElement(laColumn4);
		lvColumn1.addElement(laColumn5);
		lvColumn1.addElement(laColumn6);
		lvColumn1.addElement(laColumn7);
		lvTable.addElement(lvColumn1);

		InventoryAllocationData laDataline =
			new InventoryAllocationData();

		if (!(avResults == null) && (avResults.size() > 0))
		{
			int i = 0;
			while (i < avResults.size())
			{ // Loop through the results
				generateHeader(lvHeader, lvTable);
				int j = getNoOfDetailLines() - 2;
				// Get Available lines on the  page
				// System.out.println("no of detail lines 
				// for the page = " + j);
				for (int k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						laDataline =
							(
								InventoryAllocationData) avResults
									.elementAt(
								i);

						if (laDataline
							.getInvLocIdCd()
							.equals(InventoryConstant.CHAR_C)
							&& laDataline.getInvId().equals("0"))
						{
							this.caRpt.print(
								laDataline.getInvLocIdCd() + " ",
								INVLOCID_INVID_STARTPT,
								INVLOCID_INVID_LENGTH);
						}
						else
						{
							this.caRpt.print(
								laDataline.getInvLocIdCd()
									+ "  "
									+ laDataline.getInvId(),
								INVLOCID_INVID_STARTPT,
								INVLOCID_INVID_LENGTH);
						}
						// defect 8269
						//this.caRpt.print(
						//	ITEMCD_STARTPT,
						//	ITEMCD_LENGTH);
						// end defect 8269
						this.caRpt.print(
							(ItemCodesCache
								.getItmCd(laDataline.getItmCd()))
								.getItmCdDesc(),
							ITEMCDDESC_STARTPT,
							ITEMCDDESC_LENGTH);
						if (laDataline.getInvItmYr() == 0)
						{
							this.caRpt.center(
								" ",
								ITEMYEAR_STARTPT,
								ITEMYEAR_LENGTH);
						}
						else
						{
							this.caRpt.center(
								String.valueOf(
									laDataline.getInvItmYr()),
								ITEMYEAR_STARTPT,
								ITEMYEAR_LENGTH);
						}
						this.caRpt.rightAlign(
							String.valueOf(laDataline.getInvQty()),
							QUANTITY_STARTPT,
							QUANTITY_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							INVITMNO_STARTPT,
							INVITMNO_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							INVITMENDNO_STARTPT,
							INVITMENDNO_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
					}
				}
				// defect 8628 
				//if (i >= avResults.size())
				//{
				//	generateEndOfReport();
				//}
				//generateFooter();
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
	 * Generate Attributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Generate No Record Found Msg
	 */
	public void generateNoRecordFoundMsg()
	{
		caRpt.center("**********************************");
		caRpt.nextLine();
		caRpt.center("******   NO DATA FOUND   ******");
		caRpt.nextLine();
		caRpt.center("**********************************");
		caRpt.nextLine();
	}

	/**
	 * main - used to run this class as an application
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.3041");

		String lsStatus = "INVENTORY SUMMARY REPORT";
		InventoryHoldReport laIHR =
			new InventoryHoldReport(lsStatus, laRptProps);

		//cRptProps is of type ReportProperties, which was 
		//instantiated in the main method by the following code:
		//
		// InventorySummaryReport ihr = new InventorySummaryReport(
		// status, aRptProps);
		//
		// this code invokes the following code:
		//
		// public InventorySummaryReport(String asRptName, 
		// ReportProperties aRptProps)
		// {
		//   super(asRptName, aRptProps);
		// }
		// when super ... executes, the following code runs,:
		// because InventorySummaryReport, extends Report template
		//
		// public ReportTemplate(String asRptName, 
		// ReportProperties aRptProps) 
		// {
		// cRpt = new Report(asRptName,aRptProps.getPageWidth(), 
		// aRptProps.getPageHeight());
		// cRptProps = aRptProps;
		// }
		//
		// So, after the above code gets executed, cRpt is the report object
		// with report name and page size, while cRptProps at this point
		// contains a reference to ReportProperties, with its set methods
		//  

		//
		// Generating Demo data to display.
		String lsQuery =
			"SELECT  H.CASHWSID, 'VOIDED', C.ITMCDDESC, B.INVITMYR,"
				+ " SUM(B.INVQTY) FROM RTS.RTS_TRANS A, "
				+ "RTS.RTS_TR_INV_DETAIL B, RTS.RTS_ITEM_CODES C,"
				+ "RTS.RTS_TRANS_HDR H"
				+ " WHERE A.TRANSWSID = B.TRANSWSID AND  A.TRANSCD "
				+ "='INVVD' AND  A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "A.CUSTSEQNO = "
				+ "   B.CUSTSEQNO AND  A.TRANSTIME = B.TRANSTIME AND "
				+ "B.ITMCD=C.ITMCD AND B.DELINVREASNCD = 5 AND  "
				+ "A.TRANSAMDATE = "
				+ "   H.TRANSAMDATE AND A.TRANSWSID = H.TRANSWSID AND "
				+ "A.CUSTSEQNO = H.CUSTSEQNO AND   B.INVQTY>=1  AND "
				+ "H.CASHWSID IN "
				+ "   ( 100)  AND (  (H.CASHWSID=100 AND  "
				+ "H.TRANSTIMESTMP BETWEEN '2000-05-03-11.53.53.000000'"
				+ "  AND '2001-05-07- "
				+ "   4.46.18.000000' ) ) GROUP BY H.CASHWSID, "
				+ "C.ITMCDDESC, B.INVITMYR";

		Vector lvQueryResults = laIHR.queryData(lsQuery);

		laIHR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\INVSUMMARY.RPT");
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
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\INVSUMMARY.RPT");
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
	 * Query Data
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		caRptProps.setOfficeIssuanceName("100");
		caRptProps.setSubstationName("15");
		caRptProps.setWorkstationId(5);

		Vector lvData = new Vector();
		InventorySummaryReportData laDataline1 =
			new InventorySummaryReportData();
		laDataline1.setItmCdDesc("COMBINATION PLT");
		laDataline1.setInvItmYr(2002);
		laDataline1.setTotalItmQtySold(3);
		laDataline1.setTotalItmQtyVoid(0);
		laDataline1.setTotalItmQtyReuse(0);
		lvData.addElement(laDataline1);

		InventorySummaryReportData laDataline2 =
			new InventorySummaryReportData();
		laDataline2.setItmCdDesc("DISABLED MOTORCYCLE PLT");
		laDataline2.setInvItmYr(0);
		laDataline2.setTotalItmQtySold(1);
		laDataline2.setTotalItmQtyVoid(0);
		laDataline2.setTotalItmQtyReuse(0);
		lvData.addElement(laDataline2);

		InventorySummaryReportData laDataline3 =
			new InventorySummaryReportData();
		laDataline3.setItmCdDesc("FARM TRUCK PLT");
		laDataline3.setInvItmYr(0);
		laDataline3.setTotalItmQtySold(1);
		laDataline3.setTotalItmQtyVoid(0);
		laDataline3.setTotalItmQtyReuse(0);
		lvData.addElement(laDataline3);

		InventorySummaryReportData laDataline4 =
			new InventorySummaryReportData();
		laDataline4.setItmCdDesc("FORM 31 RTS");
		laDataline4.setInvItmYr(0);
		laDataline4.setTotalItmQtySold(18);
		laDataline4.setTotalItmQtyVoid(0);
		laDataline4.setTotalItmQtyReuse(0);
		lvData.addElement(laDataline4);

		InventorySummaryReportData laDataline5 =
			new InventorySummaryReportData();
		laDataline5.setItmCdDesc("PLATE STICKER");
		laDataline5.setInvItmYr(2002);
		laDataline5.setTotalItmQtySold(0);
		laDataline5.setTotalItmQtyVoid(0);
		laDataline5.setTotalItmQtyReuse(10);
		lvData.addElement(laDataline5);

		InventorySummaryReportData laDataline6 =
			new InventorySummaryReportData();
		laDataline6.setItmCdDesc("WINDSHIELD STICKER");
		laDataline6.setInvItmYr(2002);
		laDataline6.setTotalItmQtySold(18);
		laDataline6.setTotalItmQtyVoid(1);
		laDataline6.setTotalItmQtyReuse(1);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);
		lvData.addElement(laDataline6);

		return lvData;
	}
}
