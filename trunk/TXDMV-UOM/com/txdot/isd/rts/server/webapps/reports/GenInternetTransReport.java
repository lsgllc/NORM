package com.txdot.isd.rts.server.webapps.reports;

import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.CommonConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.RegRenProcessingConstants;

/*
 * GenInternetTransReport.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * K Harrell	08/05/2009	Cleanup.  Identify final static constants 
 * 							 appropriately. 
 * 							add TYPE_HEADER, TYPE_LENGTH, TYPE_STARTPT	
 * 							delete COL1_HEADER, COL1_LENGTH, 
 * 							 COL1_STARTPT, DETAIL_HEADER, FEES_HEADER,
 *  						UTI_HEADER, UTI_LENGTH, UTI_STARTPT 
 * 							modify formatReport() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/15/2009	Implement new generateFooter(boolean) 
 * 							delete LINES_TWO, LINES_ONE  
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	02/17/2010	Remove reference to AddressChangeReportData
 * 							modify queryData()
 * 							defect 10387 Ver POS_640    
 *----------------------------------------------------------------------
 */

/**
 * Generate Internet Trans Report
 *  
 * @version	POS_640			02/17/2010
 * @author	Administrator
 * <br>Creation Date:		10/17/2001 16:04:02
 */
public class GenInternetTransReport extends ReportTemplate
{
	/**
	 * Starts the application.
	 * 
	 * @param args an array of command-line arguments
	 */
	public static void main(java.lang.String[] args)
	{
		// empty code block
	}

	private Vector cvHeader;
	private Vector cvHeaderTable;
	private Vector cvKeyData = null;

	private final static String COUNT_HEADER = "TRANS COUNT";
	private final static int COUNT_LENGTH = 15;
	private final static int COUNT_STARTPT = 103;
	private final static int DETAIL_LENGTH = 36;
	private final static int DETAIL_STARTPT = 42;
	private final static int END_OF_PAGE_WHITESPACE = 2;
	private final static int FEES_LENGTH = 20;
	private final static int FEES_STARTPT = 80;
	private final static int LINES_ONE = 1;
	private final static int LINES_TWO = 2;
	private final static String TYPE_HEADER = "TYPE";
	private final static int TYPE_LENGTH = 24;
	private final static int TYPE_STARTPT = 15;

	// defect 10112 
	// private String COL1_HEADER = "";
	// private int COL1_LENGTH = 5;
	// private int COL1_STARTPT = 6;
	// private String DETAIL_HEADER = "";
	//	private String FEES_HEADER = "";
	// end defect 10112 

	/**
	 * GenInternetTransReport constructor
	 */
	public GenInternetTransReport()
	{
		super();
	}

	/**
	 * GenInternetTransReport constructor
	 * 
	 * @param String asRptName
	 * @param ReportProperties aaRptProps
	 */
	public GenInternetTransReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * GenInternetTransReport constructor
	 * 
	 * @param String asRptName
	 * @param ReportProperties aaRptProps
	 * @param Vector avKeyData
	 */
	public GenInternetTransReport(
		String asRptName,
		ReportProperties aaRptProps,
		Vector avKeyData)
	{
		super(asRptName, aaRptProps);

		cvKeyData = avKeyData;
	}
	/**
	 * Format the data into the report
	 * The Vector avResults contains 4 Vectors
	 * Each of the 4 are individual resultset objects
	 * 
	 * @param Vector avResults
	 */
	public void formatReport(Vector avResults)
	{
		cvHeader = new Vector();

		// vector to contain rows of column headings
		cvHeaderTable = new Vector();

		// vector for the first row of column headings
		Vector lvRow1 = new Vector();

		// defect 10112 
		// Remove unused column headers 
		// column header
		ColumnHeader laColumn1 =
			new ColumnHeader(TYPE_HEADER, TYPE_STARTPT, TYPE_LENGTH);
		ColumnHeader laColumn2 =
			new ColumnHeader(COUNT_HEADER, COUNT_STARTPT, COUNT_LENGTH);

		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);

		// column header
		//		ColumnHeader laColumn1 =
		//			new ColumnHeader(COL1_HEADER, COL1_STARTPT, COL1_LENGTH);

		// column header
		//		ColumnHeader laColumn3 =
		//			new ColumnHeader(
		//				DETAIL_HEADER,
		//				DETAIL_STARTPT,
		//				DETAIL_LENGTH);

		// column header
		//ColumnHeader laColumn4 =
		//	new ColumnHeader(FEES_HEADER, FEES_STARTPT, FEES_LENGTH);

		// lvRow1.addElement(laColumn3);
		// lvRow1.addElement(laColumn4);
		// lvRow1.addElement(laColumn5);
		// end defect 10112

		cvHeaderTable.addElement(lvRow1);

		//obtain additional key data and add into header
		if (cvKeyData != null && cvKeyData.size() > 0)
		{
			Object laObj = cvKeyData.get(0);

			if (laObj instanceof String)
			{
				String lsHdr = (String) laObj;
				cvHeader.add("TRANSACTIONS FROM:    ");
				cvHeader.add(lsHdr);
			}
		}

		InternetTransReportData laDataline =
			new InternetTransReportData();

		double ldTotalFees = 0; // keep track of total changes
		double ldNewFees = 0;
		double ldHoldFees = 0;
		double ldDeclineFees = 0;
		double ldApproveFees = 0;

		int liItemCount = 0; // keep track of number of section entries
		int liTotalCount = 0;
		// keep trach of number of total entries in report
		int i = 0; // i will be used to get each object.

		//format double to 2 decimal places - would prefer to use BigDecimal in future
		//default locale is Locale.setDefault(Locale.ENGLISH)
		DecimalFormat laDecForm = new DecimalFormat("####.00");

		if ((avResults == null) && (avResults.size() > 0))
		{
			generateHeader(cvHeader, cvHeaderTable);
			generateNoRecordFoundMsg();
			generateFooter(true);

			return;
		}

		generateHeader(cvHeader, cvHeaderTable);
		Vector lvNewResults = (Vector) avResults.get(0);
		Vector lvHoldResults = (Vector) avResults.get(1);
		Vector lvDeclineResults = (Vector) avResults.get(2);
		Vector lvApproveResults = (Vector) avResults.get(3);
		Vector lvInProcessResults = (Vector) avResults.get(4);

		//////////////////////////////////////////////////////////////////////////
		// received transactions

		// create the heading area using the additional header 
		// information and column headers
		caRpt.blankLines(LINES_TWO);
		caRpt.print("RECEIVED TRANSACTIONS", TYPE_STARTPT, TYPE_LENGTH);
		caRpt.blankLines(LINES_TWO);
		caRpt.print("Internet Trace No.", TYPE_STARTPT, TYPE_LENGTH);
		caRpt.rightAlign("FEES", FEES_STARTPT, FEES_LENGTH);
		caRpt.blankLines(LINES_ONE);

		while (i < lvNewResults.size())
			//Loop through results (within paging area)
		{

			int j = getRemainingLinesAndHandleFooter();

			// Output available lines (or less) of data to this page.
			for (int k = 0; k <= j; k++)
			{
				if (i < lvNewResults.size())
				{
					laDataline =
						(
							InternetTransReportData) lvNewResults
								.elementAt(
							i);

					ldNewFees += laDataline.getPaymentAmt();
					caRpt.print(
						laDataline.getTraceNo(),
						TYPE_STARTPT,
						TYPE_LENGTH);
					caRpt.rightAlign(
						String.valueOf(
							laDecForm.format(
								laDataline.getPaymentAmt())),
						FEES_STARTPT,
						FEES_LENGTH);
					caRpt.blankLines(LINES_ONE);

					ldTotalFees += laDataline.getPaymentAmt();
					liItemCount++;

					i = i + 1; // increment i to to get next record

				}

			}

			// Footer can appear here.
			getRemainingLinesAndHandleFooter();

			//print section summary        		
			if (i >= lvNewResults.size())
			{
				// if i is equal to or greater than results size
				// then we are out of data.  Print end of report. 
				caRpt.blankLines(LINES_ONE);
				caRpt.rightAlign(
					"TOTAL",
					DETAIL_STARTPT,
					DETAIL_LENGTH);
				caRpt.rightAlign(
					"$" + laDecForm.format(ldNewFees),
					FEES_STARTPT,
					FEES_LENGTH);
				caRpt.print(
					String.valueOf(liItemCount),
					COUNT_STARTPT,
					COUNT_LENGTH);
			}

		}

		liTotalCount += liItemCount;

		//reset
		i = 0;
		liItemCount = 0;

		//////////////////////////////////////////////////////////////////////////
		// hold transactions

		// create the heading area using the additional header 
		// information and column headers
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("HELD TRANSACTIONS", TYPE_STARTPT, TYPE_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("Internet Trace No.", TYPE_STARTPT, TYPE_LENGTH);
		caRpt.rightAlign("FEES", FEES_STARTPT, FEES_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		while (i < lvHoldResults.size()) //Loop through results
		{

			int j = getRemainingLinesAndHandleFooter();

			// Output available lines (or less if there are not that many data lines) 
			// of data to this page.
			for (int k = 0; k <= j; k++)
			{
				if (i < lvHoldResults.size())
				{
					laDataline =
						(
							InternetTransReportData) lvHoldResults
								.elementAt(
							i);

					ldHoldFees += laDataline.getPaymentAmt();
					caRpt.print(
						laDataline.getTraceNo(),
						TYPE_STARTPT,
						TYPE_LENGTH);

					caRpt.rightAlign(
						String.valueOf(
							laDecForm.format(
								laDataline.getPaymentAmt())),
						FEES_STARTPT,
						FEES_LENGTH);
					caRpt.blankLines(LINES_ONE);

					ldTotalFees += laDataline.getPaymentAmt();
					liItemCount++;

					i = i + 1;
					// increment i to to get next record            

				}

			}

			// footer can occur here.
			getRemainingLinesAndHandleFooter();

			//print section summary	    
			if (i >= lvHoldResults.size())
			{
				// if i is equal to or greater than results size
				// then we are out of data.  Print end of report. 
				caRpt.blankLines(LINES_ONE);
				caRpt.rightAlign(
					"TOTAL",
					DETAIL_STARTPT,
					DETAIL_LENGTH);
				caRpt.rightAlign(
					"$" + laDecForm.format(ldHoldFees),
					FEES_STARTPT,
					FEES_LENGTH);
				caRpt.print(
					String.valueOf(liItemCount),
					COUNT_STARTPT,
					COUNT_LENGTH);
			}

		}

		liTotalCount += liItemCount;

		//reset
		i = 0;
		liItemCount = 0;

		// footer can be here.
		getRemainingLinesAndHandleFooter();

		/////////////////////////////////////////////////////////////////////////
		// declined transactions

		// create the heading area using the additional header 
		// information and column headers
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("DECLINED TRANSACTIONS", TYPE_STARTPT, TYPE_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("Internet Trace No.", TYPE_STARTPT, TYPE_LENGTH);
		caRpt.print("REASON", DETAIL_STARTPT, DETAIL_LENGTH);
		caRpt.rightAlign("FEES", FEES_STARTPT, FEES_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		while (i < lvDeclineResults.size()) //Loop through results
		{

			int j = getRemainingLinesAndHandleFooter();

			// output availabe number of lines data to this page.
			for (int k = 0; k <= j; k++)
			{
				if (i < lvDeclineResults.size())
				{
					laDataline =
						(
							InternetTransReportData) lvDeclineResults
								.elementAt(
							i);

					ldDeclineFees += laDataline.getPaymentAmt();
					caRpt.print(
						laDataline.getTraceNo(),
						TYPE_STARTPT,
						TYPE_LENGTH);

					//must convert reason code into string
					String declineReason = laDataline.getReasonCd();

					if (declineReason == null)
						declineReason = "";
					else if (
						declineReason.equalsIgnoreCase(
							RegRenProcessingConstants.RSN_ADDR))
						declineReason = "Address";
					else if (
						declineReason.equalsIgnoreCase(
							RegRenProcessingConstants.RSN_INS))
						declineReason = "Insurance";
					else if (
						declineReason.equalsIgnoreCase(
							RegRenProcessingConstants.RSN_CNTY))
						declineReason = "County";
					else if (
						declineReason.equalsIgnoreCase(
							RegRenProcessingConstants.RSN_OTHER))
						declineReason = "Other";

					//must convert status code into string + append to reason code
					int liStatusCd =
						Integer.parseInt(laDataline.getStatusCd());

					if (liStatusCd
						== CommonConstants.DECLINED_REFUND_PENDING)
						declineReason += " (Charge-back Pending)";
					else if (
						liStatusCd
							== CommonConstants.DECLINED_REFUND_APPROVED)
						declineReason += " (Charge-back Successful)";
					else if (
						liStatusCd
							== CommonConstants.DECLINED_REFUND_FAILED)
						declineReason += " (Charge-back Failed)";

					caRpt.print(
						declineReason,
						DETAIL_STARTPT,
						DETAIL_LENGTH);
					caRpt.rightAlign(
						String.valueOf(
							laDecForm.format(
								laDataline.getPaymentAmt())),
						FEES_STARTPT,
						FEES_LENGTH);
					caRpt.blankLines(LINES_ONE);

					ldTotalFees += laDataline.getPaymentAmt();
					liItemCount++;

					i = i + 1;
					// increment i to to get next record              

				}
			}

			getRemainingLinesAndHandleFooter();

			//print section summary	    
			if (i >= lvDeclineResults.size())
			{
				// if i is equal to or greater than results size
				// then we are out of data.  Print end of report. 
				caRpt.blankLines(LINES_ONE);
				caRpt.rightAlign(
					"TOTAL",
					DETAIL_STARTPT,
					DETAIL_LENGTH);
				caRpt.rightAlign(
					"$" + laDecForm.format(ldDeclineFees),
					FEES_STARTPT,
					FEES_LENGTH);
				caRpt.print(
					String.valueOf(liItemCount),
					COUNT_STARTPT,
					COUNT_LENGTH);
			}
		}

		liTotalCount += liItemCount;

		//reset
		i = 0;
		liItemCount = 0;

		/////////////////////////////////////////////////////////////////////////
		// approved transactions

		// create the heading area using the additional header 
		// information and column headers
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("APPROVED TRANSACTIONS", TYPE_STARTPT, TYPE_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("Internet Trace No.", TYPE_STARTPT, TYPE_LENGTH);
		caRpt.print("TRANS ID", DETAIL_STARTPT, DETAIL_LENGTH);
		caRpt.rightAlign("FEES", FEES_STARTPT, FEES_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		while (i < lvApproveResults.size()) //Loop through results
		{

			int j = getRemainingLinesAndHandleFooter();

			// Output available lines (or less ) of data to this page.
			for (int k = 0; k <= j; k++)
			{
				if (i < lvApproveResults.size())
				{
					laDataline =
						(
							InternetTransReportData) lvApproveResults
								.elementAt(
							i);

					ldApproveFees += laDataline.getPaymentAmt();
					caRpt.print(
						laDataline.getTraceNo(),
						TYPE_STARTPT,
						TYPE_LENGTH);

					//must include the transaction id
					caRpt.print(
						laDataline.getTransId(),
						DETAIL_STARTPT,
						DETAIL_LENGTH);

					caRpt.rightAlign(
						String.valueOf(
							laDecForm.format(
								laDataline.getPaymentAmt())),
						FEES_STARTPT,
						FEES_LENGTH);
					caRpt.blankLines(LINES_ONE);

					ldTotalFees += laDataline.getPaymentAmt();
					liItemCount++;

					i = i + 1; // increment i to to get next record

				}
			}

			getRemainingLinesAndHandleFooter();

			//print section summary	    
			if (i >= lvApproveResults.size())
			{
				// if i is equal to or greater than results size
				// then we are out of data.  Print end of report. 
				caRpt.blankLines(LINES_ONE);
				caRpt.rightAlign(
					"TOTAL",
					DETAIL_STARTPT,
					DETAIL_LENGTH);
				caRpt.rightAlign(
					"$" + laDecForm.format(ldApproveFees),
					FEES_STARTPT,
					FEES_LENGTH);
				caRpt.print(
					String.valueOf(liItemCount),
					COUNT_STARTPT,
					COUNT_LENGTH);
			}
		}

		liTotalCount += liItemCount;

		//reset
		i = 0;
		liItemCount = 0;

		//////////////////////////////////////////////////////////////////////////
		// In-Process transactions

		// create the heading area using the additional header 
		// information and column headers
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print(
			"IN-PROCESS TRANSACTIONS",
			TYPE_STARTPT,
			TYPE_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.print("Internet Trace No.", TYPE_STARTPT, TYPE_LENGTH);
		caRpt.rightAlign("FEES", FEES_STARTPT, FEES_LENGTH);

		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		while (i < lvInProcessResults.size()) //Loop through results
		{

			int j = getRemainingLinesAndHandleFooter();

			// Output available lines (or less if data lines not that many) 
			// of data to this page. 
			for (int k = 0; k <= j; k++)
			{
				if (i < lvInProcessResults.size())
				{
					laDataline =
						(
							InternetTransReportData) lvInProcessResults
								.elementAt(
							i);

					ldHoldFees += laDataline.getPaymentAmt();
					caRpt.print(
						laDataline.getTraceNo(),
						TYPE_STARTPT,
						TYPE_LENGTH);
					caRpt.rightAlign(
						String.valueOf(
							laDecForm.format(
								laDataline.getPaymentAmt())),
						FEES_STARTPT,
						FEES_LENGTH);
					caRpt.blankLines(LINES_ONE);

					ldTotalFees += laDataline.getPaymentAmt();
					liItemCount++;

					i = i + 1;
					// increment i to to get next record            

				}

			}

			getRemainingLinesAndHandleFooter();
			//print section summary	    
			if (i >= lvInProcessResults.size())
			{
				// if i is equal to or greater than results size
				// then we are out of data.  Print end of report. 
				caRpt.blankLines(LINES_ONE);
				caRpt.rightAlign(
					"TOTAL",
					DETAIL_STARTPT,
					DETAIL_LENGTH);
				caRpt.rightAlign(
					"$" + laDecForm.format(ldHoldFees),
					FEES_STARTPT,
					FEES_LENGTH);
				caRpt.print(
					String.valueOf(liItemCount),
					COUNT_STARTPT,
					COUNT_LENGTH);
			}

		}

		liTotalCount += liItemCount;

		/////////////////////////////////////////////////////////////////////////
		// summary info
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);
		getRemainingLinesAndHandleFooter();
		caRpt.blankLines(LINES_ONE);

		getRemainingLinesAndHandleFooter();
		caRpt.rightAlign(
			"TOTAL TRANSACTION COUNT",
			DETAIL_STARTPT,
			DETAIL_LENGTH);
		caRpt.print(
			String.valueOf(liTotalCount),
			COUNT_STARTPT,
			COUNT_LENGTH);

		generateFooter(true);
	}

	/**
	 * Generate Attributes
	 * This abstract method must be implemented in all subclasses
	 *  
	 */
	public void generateAttributes()
	{
		caRpt.printAttributes(
			"THIS NEEDS TO BE REPLACED WITH PRINT STRING");
	}

	/**
	 * Get Remaining Lines And Handle Footer
	 * 
	 * @return int
	 */
	private int getRemainingLinesAndHandleFooter()
	{
		int j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;

		if (j <= 0)
		{
			generateFooter();
			generateHeader(cvHeader, cvHeaderTable);
			j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;
		}
		return j;
	}

	/**
	 * Run query and return results.
	 *  
	 * @return Vector
	 * @param String asQuery
	 */
	public Vector queryData(String asQuery)
	{
		return new Vector();
		
		// defect 10387 
		// Should not be using AddressChangeReportData 
		//		StringBuffer lsQry = new StringBuffer();
		//		Vector lvRslt = new Vector();
		//		ResultSet laQry;
		//
		//		lsQry.append(
		//			"SELECT b.ofcName, a.ItrntTimeStmp, count(*) as cnt "
		//				+ "FROM RTS.RTS_ITRNT_TRANS a, RTS.RTS_OFFICE_IDS b "
		//				+ "WHERE a.ofcIssuanceNo = b.ofcIssuanceNo AND a.transCd = 'IADDR' "
		//				+ "GROUP BY b.ofcName, a.ItrntTimeStmp");
		//
		//		try
		//		{
		//
		//			DatabaseAccess laDBAccess = new DatabaseAccess();
		//			laDBAccess.beginTransaction();
		//			laQry = laDBAccess.executeDBQuery(lsQry.toString());
		//
		//			while (laQry.next())
		//			{
		//				//				AddressChangeReportData laAddrChangeRptData =
		//				//					new AddressChangeReportData();
		//				//				laAddrChangeRptData.setCountyName(
		//				//					laDBAccess.getStringFromDB(laQry, "ofcName"));
		//				//				laAddrChangeRptData.setTimeStamp(
		//				//					laDBAccess.getRTSDateFromDB(
		//				//						laQry,
		//				//						"ItrntTimeStmp"));
		//				//				laAddrChangeRptData.setNumChanges(
		//				//					laDBAccess.getIntFromDB(laQry, "cnt"));
		//
		//				// Add element to the Vector
		//				//lvRslt.addElement(laAddrChangeRptData);
		//
		//			}
		//
		//			laQry.close();
		//			laQry = null;
		//			laDBAccess.endTransaction(DatabaseAccess.NONE);
		//
		//			return (lvRslt);
		//
		//		}
		//		catch (SQLException leSQLEx)
		//		{
		//			Log.write(
		//				Log.SQL_EXCP,
		//				this,
		//				"ERROR: Address Change Report: "
		//					+ leSQLEx.getMessage());
		//			return null;
		//		}
		//		catch (RTSException leRTSEx)
		//		{
		//			Log.write(
		//				Log.SQL_EXCP,
		//				this,
		//				"ERROR: Address Change Report: "
		//					+ leRTSEx.getMessage());
		//			return null;
		//		}
	}

}
