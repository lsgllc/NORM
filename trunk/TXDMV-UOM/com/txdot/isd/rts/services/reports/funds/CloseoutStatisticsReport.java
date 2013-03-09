package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * CloseoutStatisticsReport.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * M Listberger	09/25/2001	New Class
 * K Harrell	04/28/2005	Funds/SQL class review
 * 							defect 8163 Ver 5.2.3 
 * S Johnston	06/28/2005	Code Cleanup for Java 1.4.2 upgrade
 *							defect 7896 Ver 5.2.3
 * K Harrell	12/16/2005	Use FundsConstant.RTS_MIN_DATE 
 * 							defect 7896 Ver 5.2.3 
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs()
 * 							delete TIME_LENGTH,TIME_STARTPT_SUBSTRING
 *							modify formatReport()
 * 							defect 9943 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify END_OF_PAGE_WHITESPACE 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Closeout Statistics Report.
 *
 * @version	Defect_POS_F	08/10/2009
 * @author 	Margaret Listberger
 * <br>Creation Date:		09/25/2001 16:39:55 
 */
public class CloseoutStatisticsReport extends ReportTemplate
{
	// Initialize LEFT, CENTER, and RIGHT variables
	private static final int CENTER = 1;
	private static final int REPORT_FOOTER_LINES = 2;
	// defect 9943
	//private static final int TIME_LENGTH = 8;
	//private static final int TIME_STARTPT_SUBSTRING = 0;
	// end defect 9943 
	// First line of column headings
	private static final int CSR_TRANS_STARTPT = 108;
	private static final int CSR_TRANS_LENGTH = 20;
	private static final String CSR_TRANS_HEADER = "TRANSACTION(S)";
	// Second line of column headings
	private static final String CSR_TRANS_HEADER1 = "SINCE";
	// Third line of column headings
	private static final int CSR_BEG_STARTPT = 39;
	private static final int CSR_BEG_LENGTH = 10;
	private static final String CSR_BEG_HEADER = "BEGINNING";
	private static final int CSR_END_STARTPT = 81;
	private static final int CSR_END_LENGTH = 10;
	private static final String CSR_END_HEADER = "ENDING";
	private static final String CSR_TRANS_HEADER2 = "LAST";
	// fourth line of column headings
	private static final int CSR_CD_STARTPT = 12;
	private static final int CSR_CD_LENGTH = 12;
	private static final String CSR_CD_HEADER = "CASH DRAWER";
	private static final int CSR_DATE_STARTPT1 = 29;
	private static final int CSR_TIME_STARTPT1 = 49;
	private static final int CSR_DATE_STARTPT2 = 69;
	private static final int CSR_TIME_STARTPT2 = 90;
	private static final int CSR_DATE_TIME_LENGTH = 10;
	private static final String CSR_DATE_HEADER = "DATE";
	private static final String CSR_TIME_HEADER = "TIME";
	private static final String CSR_TRANS_HEADER3 = "CLOSE OUT";
	/**
	 * CloseoutStatisticsReport constructor
	 */
	public CloseoutStatisticsReport()
	{
		super();
	}
	/**
	 * CloseoutStatisticsReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public CloseoutStatisticsReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}
	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		Vector lvHeader = new Vector();
		// vector to hold all Header elements
		Vector lvTable = new Vector();
		// vector to hold all header rows 
		Vector lvHeaderRow1 = new Vector(); // first header row
		Vector lvHeaderRow2 = new Vector(); // second header row
		Vector lvHeaderRow3 = new Vector(); // third header row
		Vector lvHeaderRow4 = new Vector(); // fourth header row
		//  Set up the first header row and move it to header table 
		ColumnHeader laColumn1 =
			new ColumnHeader(
				CSR_TRANS_HEADER,
				CSR_TRANS_STARTPT,
				CSR_TRANS_LENGTH,
				CENTER);
		lvHeaderRow1.addElement(laColumn1);
		lvTable.addElement(lvHeaderRow1);
		//  Set up the second header row and move it to header table 
		ColumnHeader laColumn2 =
			new ColumnHeader(
				CSR_TRANS_HEADER1,
				CSR_TRANS_STARTPT,
				CSR_TRANS_LENGTH,
				CENTER);
		lvHeaderRow2.addElement(laColumn2);
		lvTable.addElement(lvHeaderRow2);
		//  Set up the third header row and move it to header table 
		ColumnHeader laColumn3 =
			new ColumnHeader(
				CSR_BEG_HEADER,
				CSR_BEG_STARTPT,
				CSR_BEG_LENGTH,
				CENTER);
		ColumnHeader laColumn4 =
			new ColumnHeader(
				CSR_END_HEADER,
				CSR_END_STARTPT,
				CSR_END_LENGTH,
				CENTER);
		ColumnHeader laColumn5 =
			new ColumnHeader(
				CSR_TRANS_HEADER2,
				CSR_TRANS_STARTPT,
				CSR_TRANS_LENGTH,
				CENTER);
		lvHeaderRow3.addElement(laColumn3);
		lvHeaderRow3.addElement(laColumn4);
		lvHeaderRow3.addElement(laColumn5);
		lvTable.addElement(lvHeaderRow3);
		//  Set up the fourth header row and move it to header table
		ColumnHeader laColumn6 =
			new ColumnHeader(
				CSR_CD_HEADER,
				CSR_CD_STARTPT,
				CSR_CD_LENGTH,
				CENTER);
		ColumnHeader laColumn7 =
			new ColumnHeader(
				CSR_DATE_HEADER,
				CSR_DATE_STARTPT1,
				CSR_DATE_TIME_LENGTH,
				CENTER);
		ColumnHeader laColumn8 =
			new ColumnHeader(
				CSR_TIME_HEADER,
				CSR_TIME_STARTPT1,
				CSR_DATE_TIME_LENGTH,
				CENTER);
		ColumnHeader laColumn9 =
			new ColumnHeader(
				CSR_DATE_HEADER,
				CSR_DATE_STARTPT2,
				CSR_DATE_TIME_LENGTH,
				CENTER);
		ColumnHeader laColumn10 =
			new ColumnHeader(
				CSR_TIME_HEADER,
				CSR_TIME_STARTPT2,
				CSR_DATE_TIME_LENGTH,
				CENTER);
		ColumnHeader laColumn11 =
			new ColumnHeader(
				CSR_TRANS_HEADER3,
				CSR_TRANS_STARTPT,
				CSR_TRANS_LENGTH,
				CENTER);
		lvHeaderRow4.addElement(laColumn6);
		lvHeaderRow4.addElement(laColumn7);
		lvHeaderRow4.addElement(laColumn8);
		lvHeaderRow4.addElement(laColumn9);
		lvHeaderRow4.addElement(laColumn10);
		lvHeaderRow4.addElement(laColumn11);
		lvTable.addElement(lvHeaderRow4);
		//Adding ColumnHeader Information	
		// create new data oject
		CloseOutStatisticsReportData laCloseOutStatisticsReportData =
			new CloseOutStatisticsReportData();
		String lsBegDate;
		String lsEndDate;
		String lsRTSDateBegTime;
		String lsEndTime;
		int i = 0;
		if (!(avResults == null) && (avResults.size() > 0))
		{
			while (i < avResults.size()) //Loop through the results
			{
				generateHeader(lvHeader, lvTable);
				// produce the header
				int j = getNoOfDetailLines() - REPORT_FOOTER_LINES;
				//Get Available lines on the page
				for (int k = 0; k <= j; k++)
				{
					if (i < avResults.size())
					{
						laCloseOutStatisticsReportData =
							(
								CloseOutStatisticsReportData) avResults
									.elementAt(
								i);
						// get and print out workstationID
						DecimalFormat laThreeDigits =
							new DecimalFormat("000");
						String lsId =
							laThreeDigits.format(
								laCloseOutStatisticsReportData
									.getCashWsId());
						caRpt.center(
							lsId,
							CSR_CD_STARTPT,
							CSR_CD_LENGTH);
						// get and print the beginning trans date in mm/dd/yyyy format                     
						RTSDate laBegDateRTSDate =
							(RTSDate) laCloseOutStatisticsReportData
								.getCloseOutBegTstmp();
						if (laBegDateRTSDate
							.equals(
								new RTSDate(
									FundsConstant.RTS_MIN_DATE)))
						{
							lsBegDate = "";
						}
						else
						{
							lsBegDate = laBegDateRTSDate.toString();
						}
						caRpt.print(
							lsBegDate,
							CSR_DATE_STARTPT1,
							CSR_DATE_TIME_LENGTH);
						// get and print the beginning trans time in
						// hh:mm:ss format (military time).
						if (laBegDateRTSDate
							.equals(
								new RTSDate(
									FundsConstant.RTS_MIN_DATE)))
						{
							lsRTSDateBegTime = "";
						}
						else
						{
							// defect 9943
							lsRTSDateBegTime =
								laBegDateRTSDate.getClockTimeNoMs();
							//.getClockTime()
							//.substring(
							//TIME_STARTPT_SUBSTRING,
							//TIME_LENGTH);
							// end defect 9943
						}
						caRpt.print(
							lsRTSDateBegTime,
							CSR_TIME_STARTPT1,
							CSR_DATE_TIME_LENGTH);
						// get and print the ending trans date in
						// mm/dd/yyyy format                     
						RTSDate laEndDateRTSDate =
							(RTSDate) laCloseOutStatisticsReportData
								.getCloseOutEndTstmp();
						if (laEndDateRTSDate
							.equals(
								new RTSDate(
									FundsConstant.RTS_MIN_DATE)))
						{
							lsEndDate = "";
						}
						else
						{
							lsEndDate = laEndDateRTSDate.toString();
						}
						caRpt.print(
							lsEndDate,
							CSR_DATE_STARTPT2,
							CSR_DATE_TIME_LENGTH);
						// get and print the ending trans time in 
						//hh:mm:ss format (military time).
						if (laEndDateRTSDate
							.equals(
								new RTSDate(
									FundsConstant.RTS_MIN_DATE)))
						{
							lsEndTime = "";
						}
						else
						{
							// defect 9943
							lsEndTime =
								laEndDateRTSDate.getClockTimeNoMs();
							//.getClockTime()
							//.substring(
							//TIME_STARTPT_SUBSTRING,
							//TIME_LENGTH);
							// end defect 9943 
						}
						caRpt.print(
							lsEndTime,
							CSR_TIME_STARTPT2,
							CSR_DATE_TIME_LENGTH);
						// get and print the TransSinceCloseOut flag
						caRpt.center(
							laCloseOutStatisticsReportData
								.getTransSinceCloseOut(),
							CSR_TRANS_STARTPT,
							CSR_TRANS_LENGTH);
						caRpt.nextLine();
						i++;
					}
				}
				// defect 8628 
				//if (i >= (avResults.size() - 1))
				//{
				//	generateEndOfReport();
				//}
				//generateFooter();
				generateFooter(i >= (avResults.size() - 1));
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
	 * 
	 */
	public void generateAttributes()
	{
		// empty code block
	}
	
	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.5201");
		CloseoutStatisticsReport laCSR =
			new CloseoutStatisticsReport(
				"CLOSE OUT STATISTICS",
				laRptProps);
		// Generating Demo data to display.
			String lsQuery =
				" SELECT 'Y' AS TransSinceCloseOut,A.CashWsId,"
					+ "CloseOutBegTstmp,CloseOutEndTstmp "
					+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B"
					+ " WHERE "
					+ " B.OfcIssuanceNo = 170 AND " // FROM SYSTEM PARAMETERS
		+" B.SubstaId = 0 AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId=B.CashWsId AND"
			+ " A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp)"
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY B"
			+ " WHERE "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId = B.CashWsId)"
			+ " AND EXISTS (SELECT * FROM RTS.RTS_TRANS_HDR C,"
			+ " RTS.RTS_TRANS D WHERE D.TRANSCD <> 'CLSOUT' AND "
			+ " C.OfcIssuanceNo = 170 AND "
			+ " C.SubstaId = 0 AND "
			+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
			+ " C.SubstaId      = D.SubstaId      AND "
			+ " A.CashWsId = C.CashWsId AND "
			+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
			+ " C.TRANSWSID =                D.TRANSWSID AND "
			+ " C.CUSTSEQNO =D.CUSTSEQNO "
			+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp) "
			+ " UNION "
			+ " SELECT ' ' AS TransSinceCloseOut, A.CashWsId,"
			+ " CloseOutBegTstmp , "
			+ " CloseOutEndTstmp "
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B "
			+ " WHERE "
			+ " B.OfcIssuanceNo = 170 AND "
			+ " B.SubstaId = 0 AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId = B.SubstaId AND "
			+ " A.CashWsId = B.CashWsId AND "
			+ " A.CloseOutEndTstmp = (SELECT MAX(CloseOutEndTstmp)"
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY B "
			+ " WHERE "
			+ " A.OfcIssuanceNo =B.OfcIssuanceNo AND "
			+ " A.SubstaId = B.SubstaId AND "
			+ " A.CashWsId = B.CashWsId) "
			+ " AND NOT EXISTS "
			+ " (SELECT * FROM RTS.RTS_TRANS_HDR C, RTS.RTS_TRANS D "
			+ " WHERE D.TRANSCD <> 'CLSOUT'AND "
			+ " C.OfcIssuanceNo = 170 AND "
			+ " C.SubstaId = 0 AND "
			+ " A.CashWsId = C.CashWsId AND "
			+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND " //Trans:TransHdr
	+" C.SubstaId      = D.SubstaId      AND "
		+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
		+ " C.TRANSWSID=D.TRANSWSID AND "
		+ " C.CUSTSEQNO=D.CUSTSEQNO"
		+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp)"
		+ " UNION"
		+ " SELECT 'Y' AS TransSinceCloseOut,A.CashWsId,'"
		+ FundsConstant.RTS_MIN_DATE
		+ "' AS CloseoutBegTstmp,'"
		+ FundsConstant.RTS_MIN_DATE
		+ "' AS CloseoutEndTstmp "
		+ " FROM RTS.RTS_TRANS_HDR A, RTS.RTS_CASH_WS_IDS B"
		+ " WHERE "
		+ " B.OfcIssuanceNo = 170 AND "
		+ " B.SubstaId = 0 AND "
		+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			//JOIN CloseOutHstry:CashWsIds
	+" A.SubstaId      = B.SubstaId      AND "
		+ " A.CashWsId=B.CashWsId AND "
		+ "  NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY C"
		+ " WHERE A.CashWsId=C.CashWsId)"
		+ " ORDER BY 2";
		Vector lvQueryResults = laCSR.queryData(lsQuery);
		laCSR.formatReport(lvQueryResults);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\CloseoutStatsRpt.txt");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laCSR.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\CloseoutStatsRpt.txt");
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
			//Process p = Runtime.getRuntime().exec(
			//"cmd.exe /c copy c:\\TitlePkgRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main()"
					+ " of com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}
	
	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvCloseOutStatisticsReportData = new Vector();
		RTSDate laBegDateRTSDate =
			new RTSDate(2001, 3, 29, 4, 26, 41, 2);
		RTSDate laEndDateRTSDate =
			new RTSDate(2001, 4, 2, 15, 12, 23, 5);
		CloseOutStatisticsReportData laCloseOutStatisticsReportData =
			new CloseOutStatisticsReportData();
		laCloseOutStatisticsReportData.setCashWsId(000);
		laCloseOutStatisticsReportData.setCloseOutBegTstmp(
			laBegDateRTSDate);
		laCloseOutStatisticsReportData.setCloseOutEndTstmp(
			laEndDateRTSDate);
		laCloseOutStatisticsReportData.setTransSinceCloseOut("Y");
		lvCloseOutStatisticsReportData.addElement(
			laCloseOutStatisticsReportData);
		for (int i = 0; i < 150; i++)
		{
			lvCloseOutStatisticsReportData.addElement(
				laCloseOutStatisticsReportData);
		}
		return lvCloseOutStatisticsReportData;
	}
}
