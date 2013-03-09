package com.txdot.isd.rts.services.reports.reports;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SetAsideTransactionReportData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenSetAsideTransactionReport.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/03/2001	New Class
 * Min Wang   	10/04/2001	Add Constants for positioning
 * E LyBrand	07/16/2003 	Added 'void instructions' above
 *							column heading lines
 * 							defect 5894
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3  
 * K Harrell	10/17/2005	Use UtilityMethods.getTransId() to add 
 * 							padding for OfcIssuanceNo,TransWsId,
 * 							TransTime. SetAsideTransactionReportData			  
 * 							moved to services.data. 
 * 							modify formatReport()  
 * 							defect 8103 Ver 5.2.3
 * K Harrell	11/08/2005	Modified name to Set-Aside Transaction Report
 * 							modify main() 
 * 							defect 8379 Ver 5.2.3  	
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F     
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the completed Set-Aside Transactions Report.
 *
 * @version	Defect_POS_F  	08/10/2009
 * @author	Min Wang
 * <br>Creation Date:		09/06/2001 07:35:55
 */
public class GenSetAsideTransactionReport extends ReportTemplate
{
	private static final String REPORT_DATE_HEADER = "REPORTED DATE:";

	private static final String TRANSACTION_TBL_HEADER1 = "TRANSACTION";
	private static final int TRANSACTION_START_PT = 1;
	private static final int TRANSACTION_ID_LENGTH = 17;

	private static final String WS_TBL_HEADER1 = "WORKSTATION";
	private static final int WS_ID_START_PT = 22;
	private static final int WS_ID_LENGTH = 11;

	private static final String CUST_TBL_HEADER1 = "CUSTOMER";
	private static final int CUST_NUM_START_PT = 36;
	private static final int CUST_NUM_LENGTH = 8;
	private static final int CUST_NAME_START_PT = 46;
	private static final int CUST_NAME_LENGTH = 30;

	private static final String TRANS_TBL_HEADER1 = "TRANS";
	private static final int TRANS_TYPE_START_PT = 81;
	private static final int TRANS_TYPE_LENGTH = 8;

	private static final String ID_TBL_HEADER2 = "ID";
	private static final int ID_TRANS_START_PT = 5;
	private static final int ID_WS_START_PT = 26;

	private static final String NUM_TBL_HEADER2 = "NUMBER";
	private static final int NUM_START_PT = 37;

	private static final String NAME_TBL_HEADER2 = "NAME";
	private static final int NAME_START_PT = 48;
	private static final int NAME_LENGTH = 30;

	private static final String TYPE_TBL_HEADER2 = "TYPE";
	private static final int TYPE_START_PT = 81;

	private static final String VIN_TBL_HEADER2 = "VIN";
	private static final int VIN_START_PT = 91;
	private static final int VIN_LENGTH = 20;

	private static final int CUST_NO_START_PT = 38;

	private static final int LENGTH3 = 3;

	private static final String NO_INCOMPL_TRANS_MSG =
		"...No Incomplete Transactions Found...";

	private static final String VOID_INSTRUCTIONS =
		"THE TRANSACTIONS LISTED BELOW SHOULD BE VOIDED BY THE COUNTY";

	/**
	 * GenSetAsideTransactionReport constructor
	 */
	public GenSetAsideTransactionReport()
	{
		super();
	}

	/**
	 * GenSetAsideTransactionReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenSetAsideTransactionReport(
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
		RTSDate laToday = new RTSDate();
		RTSDate laYesterday = laToday.add(RTSDate.DATE, -1);

		Vector lvHeader = new Vector();
		lvHeader.addElement(REPORT_DATE_HEADER);

		//adding additional header information
		lvHeader.addElement(laYesterday.toString());

		Vector lvMore = new Vector();
		// defect 6308
		// added instructional message
		lvMore.addElement(VOID_INSTRUCTIONS);
		// end defect 6308 

		Vector lvTable = new Vector();

		Vector lvColumnA = new Vector();
		ColumnHeader laColumnA1 =
			new ColumnHeader(
				TRANSACTION_TBL_HEADER1,
				TRANSACTION_START_PT,
				TRANSACTION_ID_LENGTH);
		ColumnHeader laColumnA2 =
			new ColumnHeader(
				WS_TBL_HEADER1,
				WS_ID_START_PT,
				WS_ID_LENGTH);
		ColumnHeader laColumnA3 =
			new ColumnHeader(
				CUST_TBL_HEADER1,
				CUST_NUM_START_PT,
				CUST_NUM_LENGTH);
		ColumnHeader laColumnA4 =
			new ColumnHeader(
				CUST_TBL_HEADER1,
				CUST_NAME_START_PT,
				CUST_NAME_LENGTH);
		ColumnHeader laColumnA5 =
			new ColumnHeader(
				TRANS_TBL_HEADER1,
				TRANS_TYPE_START_PT,
				TRANS_TYPE_LENGTH);
		lvColumnA.addElement(laColumnA1);
		lvColumnA.addElement(laColumnA2);
		lvColumnA.addElement(laColumnA3);
		lvColumnA.addElement(laColumnA4);
		lvColumnA.addElement(laColumnA5);
		lvTable.addElement(lvColumnA);

		//Adding ColumnHeader Information
		Vector lvColumnB = new Vector();
		ColumnHeader laColumnB1 =
			new ColumnHeader(
				ID_TBL_HEADER2,
				ID_TRANS_START_PT,
				TRANSACTION_ID_LENGTH);
		ColumnHeader laColumnB2 =
			new ColumnHeader(
				ID_TBL_HEADER2,
				ID_WS_START_PT,
				WS_ID_LENGTH);
		ColumnHeader laColumnB3 =
			new ColumnHeader(
				NUM_TBL_HEADER2,
				NUM_START_PT,
				CUST_NUM_LENGTH);
		ColumnHeader laColumnB4 =
			new ColumnHeader(
				NAME_TBL_HEADER2,
				NAME_START_PT,
				CUST_NAME_LENGTH);
		ColumnHeader laColumnB5 =
			new ColumnHeader(
				TYPE_TBL_HEADER2,
				TYPE_START_PT,
				TRANS_TYPE_LENGTH);
		ColumnHeader laColumnB6 =
			new ColumnHeader(
				VIN_TBL_HEADER2,
				VIN_START_PT,
				TRANS_TYPE_LENGTH);

		lvColumnB.addElement(laColumnB1);
		lvColumnB.addElement(laColumnB2);
		lvColumnB.addElement(laColumnB3);
		lvColumnB.addElement(laColumnB4);
		lvColumnB.addElement(laColumnB5);
		lvColumnB.addElement(laColumnB6);
		lvTable.addElement(lvColumnB);

		// Adding Transaction Info

		SetAsideTransactionReportData laDataline =
			new SetAsideTransactionReportData();

		int i = 0;
		if (!(avResults == null) && (avResults.size() > 0))
		{
			while (i < avResults.size())
			{
				//Loop through the results
				generateHeader(lvHeader, lvMore, lvTable);
				int j = getNoOfDetailLines();

				//Get Available lines on the page
				System.out.println(j);
				for (int k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						laDataline =
							(
								SetAsideTransactionReportData) avResults
									.elementAt(
								i);

						// defect 8103
						// Use UtilityMethods.getTransId() 
						//  to generate TransId 
						String lsTransId =
							UtilityMethods.getTransId(
								laDataline.getOfcIssuanceNo(),
								laDataline.getTransWsId(),
								laDataline.getTransAMDate(),
								laDataline.getTransTime());

						caRpt.print(
							lsTransId,
							TRANSACTION_START_PT,
							TRANSACTION_ID_LENGTH);
						// end defect 8103 	

						// WsId 
						String lsWsId =
							UtilityMethods.addPadding(
								String.valueOf(
									laDataline.getTransWsId()),
								LENGTH3,
								"0");

						caRpt.print(lsWsId, ID_WS_START_PT, LENGTH3);

						// CustSeqNo 
						caRpt.rightAlign(
							String.valueOf(laDataline.getCustSeqNo()),
							CUST_NO_START_PT,
							LENGTH3);

						// CustName1 
						caRpt.print(
							laDataline.getCustName1(),
							CUST_NAME_START_PT,
							NAME_LENGTH);

						// TransCd 
						caRpt.print(
							laDataline.getTransCd(),
							TYPE_START_PT,
							TRANS_TYPE_LENGTH);

						// VIN 
						caRpt.print(
							laDataline.getVIN(),
							VIN_START_PT,
							VIN_LENGTH);

						caRpt.nextLine();
						k = caRpt.getCurrX();
						i = i + 1;
					} //end outer if
				} //end for
				
				// defect 8628 
				//if (i >= avResults.size()) // last page
				//{
				//	caRpt.nextLine();
				//	generateEndOfReport();
				//	} //end if
				//caRpt.nextLine();
				//generateFooter();
				generateFooter(i >= avResults.size());
				// end defect 8628  
				
			} //end while
		}
		// no records found
		else
		{
			generateHeader(lvHeader, lvMore, lvTable);
			caRpt.nextLine();
			caRpt.center(NO_INCOMPL_TRANS_MSG);
			generateFooter(true);
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
		// defect 8379 
		// Use info from ReportConstant for Id and Title 
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.COMPL_SETASIDE_REPORT_ID);
		GenSetAsideTransactionReport laGSATR =
			new GenSetAsideTransactionReport(
				ReportConstant.COMPL_SETASIDE_REPORT_TITLE,
				laRptProps);
		// end defect 8379 

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS.RTS_TABLE";
		Vector lvQueryResults = laGSATR.queryData(lsQuery);
		laGSATR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\INCTRN#.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGSATR.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport =
				new FrmPreviewReport("c:\\INCTRN#.RPT");
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
					+ "com.txdot.isd.rts.services.reports.reports.GenSetAsideTransactionReport");
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

		SetAsideTransactionReportData laDataLine =
			new SetAsideTransactionReportData();
		laDataLine.setOfcIssuanceNo(161);
		laDataLine.setTransWsId(0);
		laDataLine.setTransAMDate(12345);
		laDataLine.setTransTime(000);
		laDataLine.setCustSeqNo(22);
		laDataLine.setCustName1("Min Wang");
		laDataLine.setTransCd("Title");
		laDataLine.setVIN("abcdefghigklmnopqrst");
		lvData.addElement(laDataLine);

		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		lvData.addElement(laDataLine);
		return lvData;
	}
}
