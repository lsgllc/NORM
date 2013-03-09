package com.txdot.isd.rts.services.reports.miscellaneous;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VoidUIData;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;

/*
 * GenVoidReport.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * E LyBrand	10/25/2001	New Class
 * BTulsiani	05/08/2002	Added next line for double spacing #3812
 * BBrown	    05/17/2002	Added k = k + 2 in for loop in formatReport
 * /RTaylor					method to account for next line for 
 * 							double spacing #3964
 * S Govindappa 07/29/2002	Fixed CQU100004309. Increased the space
 * 							available for printing voided transaction 
 * 							description 
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F       
 * ---------------------------------------------------------------------
 */
/**
 * Prepares the Void Report
 *
 * @version	Defect_POS_F  	08/10/2009
 * @author	Edward LyBrand 
 * <br>Creation Date:		10/25/2001 03:36:22
 */
public class GenVoidReport extends ReportTemplate
{
	// character locations for print
	private final static int siCharStart_02 = 2;
	private final static int siCharStart_09 = 9;
	private final static int siCharStart_60 = 60;
	private final static int siCharStart_65 = 65;
	private final static int siCharStart_88 = 88;
	private final static int siCharStart_92 = 92;
	private final static int siCharStart_107 = 107;
	private final static int siCharStart_108 = 108;
	private final static int siCharLength_06 = 6;
	private final static int siCharLength_15 = 15;
	private final static int siCharLength_16 = 16;
	private final static int siCharLength_17 = 17;
	private final static int siCharLength_23 = 23;
	private final static int siCharLength_50 = 50;
	private final static int siCharLength_55 = 55;
	// housekeepin' for print
	private final String ssColHdg_Voided = "VOIDED";
	private final String ssColHdg_Void = "'VOID'";
	private final String ssColHdg_InvVoid = "'INVENTORY VOID'";
	private final String ssColHdg_TransDesc = "TRANSACTION DESCRIPTION";
	private final String ssColHdg_TransNumber = "TRANSACTION NO.";
	private final String ssColHdg_ColLine =
		"---------------------------------------------";
	// defect 7896
	// removed unused local variables
//	private String csRunTimeFormated;
//	private String csRunDateFormated = "";
	// end defect 7896

	/**
	 * GenVoidReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenVoidReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * format report: heading column heading, body, footer
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		//create variables, arrays, and so forth
		boolean lbMoreRecords = true; //mo' recs for processing
		int liNumberOfRecords = avResults.size();
		//number of records read
		int liRecordIndex = 0; //record index value
		String[] larrTransDescription = new String[liNumberOfRecords];
		//event-to-be-voided description
		String[] larrTransactionId = new String[liNumberOfRecords];
		//fabricated transaction id
		String[] larrTargetInvTransId = new String[liNumberOfRecords];
		//inventory-to-be-voided transid
		String[] larrTargetEveTransId = new String[liNumberOfRecords];
		//event-to-be-voided transid
		VoidUIData laDataline = new VoidUIData();

		//get # of available printlines
		int liPrintLinesAvailable = getNoOfDetailLines();

		//build arrays, add data elements
		for (int i = 0; i < liNumberOfRecords; i++)
		{
			laDataline = (VoidUIData) avResults.elementAt(i);
			larrTransDescription[i] = laDataline.getVoidDescription();
			larrTransactionId[i] =
				String.valueOf(laDataline.getTransId());
			larrTargetInvTransId[i] =
				String.valueOf(laDataline.getTargetInventoryTransId());
			larrTargetEveTransId[i] =
				String.valueOf(laDataline.getTargetEventTransId());
		} //end for loop

		//print report heading, column headings 
		generateColumnHeadings(liRecordIndex);

		//get number of printlines available
		liPrintLinesAvailable = getNoOfDetailLines();

		//test for raw input
		switch (liNumberOfRecords)
		{
			case 0 : //no recs found
				{
					lbMoreRecords = false;
					break;
				}
			default :
				{
					// empty code block
				}
		} //end switch

		//print body
		try
		{
			while (lbMoreRecords)
			{
				// account for next line for double spacing 
				// #3964 - k = k + 2
				for (int k = 0;
					k < liPrintLinesAvailable - 1;
					k = k + 2)
				{
					if (liRecordIndex < liNumberOfRecords)
					{
						printDataline(
							larrTransDescription[liRecordIndex],
							larrTargetEveTransId[liRecordIndex],
							larrTransactionId[liRecordIndex],
							larrTargetInvTransId[liRecordIndex]);
						caRpt.nextLine();
						liRecordIndex++;
					}
					else
					{
						lbMoreRecords = false;
					}
				} //end for loop: more data lines
				if (lbMoreRecords)
				{
					//print footer
					generateFooter();
					//print heading, column headings 
					generateColumnHeadings(liRecordIndex);
					//Get available lines for the body
					liPrintLinesAvailable = getNoOfDetailLines();
				} // end if      
			} //end while...more recs
		} //end try block
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in formatReport() of "
					+ "com.txdot.isd.rts.services.reports.GenTitlePackageReport");
			aeEx.printStackTrace(System.out);
		} //end catch block

		// defect 8628 
		//generateEndOfReport();
		//generateFooter();
		generateFooter(true); 
		// end defect 8628 
	}

	/**
	 * generateAttributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * creates column heading and intermediate text
	 * 
	 * @param aaI int
	 */
	public void generateColumnHeadings(int aiI)
	{
		//create containers
		Vector lvHeader = new Vector();
		Vector lvCenter = new Vector();
		Vector lvTable = new Vector();

		//print report name
		lvCenter.addElement("VOID REPORT BY VOIDED TRANSACTION NO.");

		generateHeader(lvHeader, lvCenter, lvTable);

		//print column headings
		caRpt.blankLines(1);
		caRpt.print(ssColHdg_Voided, siCharStart_09, siCharLength_06);
		caRpt.print(ssColHdg_Voided, siCharStart_65, siCharLength_06);
		caRpt.print(ssColHdg_Void, siCharStart_92, siCharLength_06);
		caRpt.print(ssColHdg_InvVoid, siCharStart_107, siCharLength_16);
		caRpt.blankLines(1);
		caRpt.print(
			ssColHdg_TransDesc,
			siCharStart_02,
			siCharLength_23);
		caRpt.print(
			ssColHdg_TransNumber,
			siCharStart_60,
			siCharLength_15);
		caRpt.print(
			ssColHdg_TransNumber,
			siCharStart_88,
			siCharLength_15);
		caRpt.print(
			ssColHdg_TransNumber,
			siCharStart_108,
			siCharLength_15);
		caRpt.blankLines(1);
		caRpt.print(ssColHdg_ColLine, siCharStart_02, siCharLength_50);
		caRpt.print(ssColHdg_ColLine, siCharStart_60, siCharLength_17);
		caRpt.print(ssColHdg_ColLine, siCharStart_88, siCharLength_17);
		caRpt.print(ssColHdg_ColLine, siCharStart_108, siCharLength_17);
		caRpt.blankLines(1);
	}

	// defect 7896
	// removed unused local methods
//	/**
//	 * generateTransId() assembles transaction id
//	 * 
//	 * @param aiOfcIssuanceNo int
//	 * @param aiTransWsId int
//	 * @param aiTransAmDate int
//	 * @param aiTransTime int
//	 * @return String - Transaction Id
//	 */
//	private String generateTransId(
//		int aiOfcIssuanceNo,
//		int aiTransWsId,
//		int aiTransAmDate,
//		int aiTransTime)
//	{
//		String lsTransId = caRpt.blankSpaces(1);
//		// Declare Transaction Id variable
//
//		String lsOfcIssuaceNo = String.valueOf(aiOfcIssuanceNo);
//		String lsTransWsId = String.valueOf(aiTransWsId);
//		String lsTransAmDate = String.valueOf(aiTransAmDate);
//		String lsTransTime = String.valueOf(aiTransTime);
//
//		//build transid
//		lsTransId =
//			lsOfcIssuaceNo + lsTransWsId + lsTransAmDate + lsTransTime;
//
//		return lsTransId;
//	}

//	/**
//	 * getCurrentDate()
//	 */
//	private String getCurrentDate()
//	{
//		Date laRunDateRaw; //SystemDate
//
//		laRunDateRaw = new Date();
//		DateFormat laDateFormatter;
//		laDateFormatter =
//			DateFormat.getDateInstance(
//				DateFormat.SHORT,
//				new Locale("en", "US"));
//		csRunDateFormated = laDateFormatter.format(laRunDateRaw);
//		laDateFormatter =
//			DateFormat.getTimeInstance(
//				DateFormat.SHORT,
//				new Locale("en", "US"));
//		csRunTimeFormated = laDateFormatter.format(laRunDateRaw);
//
//		return csRunDateFormated;
//	}
	// end defect 7896

	/**
	 * starts the application to produce Void Report
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		//create new graph of report class
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.9001");
		GenVoidReport laGVR =
			new GenVoidReport("VOID REPORT", laRptProps);

		//extract dummy data for display
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryResults = laGVR.queryData(lsQuery);
		laGVR.formatReport(lvQueryResults);

		//write completed report to local hard drive
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\RTS\\RPT\\VOID.TXT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGVR.caRpt.getReport().toString());
		laPout.close();

		//display report in preview window
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RPT\\VOID.TXT");
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
			// print report
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\RTS\\RPT\\Void.TXT prn");
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
	 * printDataline prints body text of Void Report
	 * 
	 * @param asVoidDescription String
	 * @param asTargetEveTransId String
	 * @param asTransId String
	 * @param asTargetInvTransId String
	 */
	private void printDataline(
		String asVoidDescription,
		String asTargetEveTransId,
		String asTransId,
		String asTargetInvTransId)
	{
		//print detail line

		if (asVoidDescription.equals("null"))
		{
			asVoidDescription = "";
		}
		if (asTargetInvTransId.equals("null"))
		{
			asTargetInvTransId = "";
		}
		if (asTargetEveTransId.equals("null"))
		{
			asTargetEveTransId = "";
		}
		if (asTransId.equals("null"))
		{
			asTransId = "";
		}
		caRpt.print(
			asVoidDescription,
			siCharStart_02,
			siCharLength_55);
		//description of trans that was voided
		caRpt.print(
			String.valueOf(asTransId.trim()),
			siCharStart_60,
			siCharLength_17);
		//transid of trans that was voided
		caRpt.print(
			String.valueOf(asTargetEveTransId.trim()),
			siCharStart_88,
			siCharLength_17);
		//transid of successful void event
		caRpt.print(
			String.valueOf(asTargetInvTransId.trim()),
			siCharStart_108,
			siCharLength_17);
		//transid of inventory component of voided trans

		caRpt.blankLines(1);
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
		Vector avResults = new Vector();

		// defect 7896
		// removed unused local variables
//		String lsTransDescription = "";
//		String lsTransId = "";
//		String lsTargetEventTransId = "";
//		String lsTargetInventoryTransId = "";
		// end defect 7896
		
		VoidUIData laDataline = new VoidUIData();

		//populate test rec
		laDataline.setVoidDescription("TITLE APPLICATION RECEIPT");
		laDataline.setTransId("16110036174111159");
		laDataline.setTargetEventTransId("16110036174111160");
		laDataline.setTargetInventoryTransId("16110036174111161");

		// replicate test rec hundred times
		for (int i = 0; i < 100; i++)
		{
			avResults.addElement(laDataline);
		}
		return avResults;
	}
}
