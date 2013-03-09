package com.txdot.isd.rts.server.webapps.reports;

import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.server.webapps.util.Log;

import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;

/*
 * GenVendorReport.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford		07/29/2002	Add Original Trasaction Date Column.
 * Clifford		10/09/2002	Defect 4795, Failed Refund transaction handling	
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * Bob Brown	12/16/2008	Add credit card type to the report
 * 							add TOTAL_AMT_STARTPT,CREDIT_CARD_TYPE_
 * 							STARTPT,CREDIT_CARD_TYPE_LENGTH,CREDIT_CARD
 * 							_TYPE_HEADER
 * 							modify formatReport()
 * 							defect 9878 Ver Defect_POS_C
 * Bob Brown	12/30/2008	Only show seperate credit card totals if 
 * 							there are no trace numbers suffixed with "A"
 * 							modify formatReport()
 * 							defect 9878 Ver Defect_POS_C
 * K Harrell	08/15/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F    	
 *----------------------------------------------------------------------
 */

/**
 * Generate Vendor Report
 *  
 * @version	Defect_POS_F	08/15/2009
 * @author	Administrator
 * <br>Creation Date:		02/25/2002 16:27:25
 */
public class GenVendorReport extends ReportTemplate
{

	private int TRACENO_STARTPT = 2;
	private int TRACENO_LENGTH = 18;
	private String TRACENO_HEADER = "INTERNET TRACE NO.";

	private int TRANS_DATE_STARTPT = 22;
	private int TRANS_DATE_LENGTH = 20;
	private String TRANS_DATE_HEADER = "TRANSACTION DATE";

	private int ORIG_TRANS_DATE_STARTPT = 44;
	private int ORIG_TRANS_DATE_LENGTH = 20;
	private String ORIG_TRANS_DATE_HEADER = "ORIG. TRANS. DATE";

	private int CREDIT_CARD_STARTPT = 66;
	// defect 9878
//	private int CREDIT_CARD_LENGTH = 20;
//	private String CREDIT_CARD_HEADER = "CREDIT CARD#(Last 4)";
	private int TOTAL_AMT_STARTPT = 56;
	private int CREDIT_CARD_LENGTH = 10;
	private String CREDIT_CARD_HEADER = "CC#-Last 4";
	
	private int CREDIT_CARD_TYPE_STARTPT = 78;
	private int CREDIT_CARD_TYPE_LENGTH = 4;
	private String CREDIT_CARD_TYPE_HEADER = "TYPE";
	// end defect 9878	

	private int FEES_STARTPT = 88;
	private int FEES_LENGTH = 10;
	private String FEES_HEADER = "FEES";

	private int END_OF_PAGE_WHITESPACE = 1;
	private int LINES_TWO = 2;
	private int LINES_ONE = 1;
	private String csDateHeader = "";

	// Page header beyond page 2.
	private Vector cvHeader;
	private Vector cvHeaderTable;
	
	/**
	 * GenVendorReport constructor comment.
	 */
	public GenVendorReport()
	{
		super();
	}
	
	/**
	 * GenVendorReport constructor comment.
	 * 
	 * @param asRptName java.lang.String
	 * @param aaRptProps com.txdot.isd.rts.services.reports.ReportProperties
	 */
	public GenVendorReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}
	
	/**
	 * GenVendorReport constructor comment.
	 * 
	 * @param asRptName java.lang.String
	 * @param aaRptProps com.txdot.isd.rts.services.reports.ReportProperties
	 * @param asHeader java.lang.String
	 */
	public GenVendorReport(
		String asRptName,
		ReportProperties aaRptProps,
		String asHeader)
	{
		super(asRptName, aaRptProps);
		csDateHeader = asHeader;
	}
	/**
	 * Format Report
	 * 
	 * @param avResults java.util.Vector	
	 */
	public void formatReport(java.util.Vector avResults)
	{

		Log.fine("GenVendorReport, formatReport, Enter");

		cvHeader = new Vector();
		cvHeaderTable = new Vector();
		// vector to contain rows of column headings

		Vector lvHeaderRow = new Vector();
		// vector for the first row of column headings
		// there is only one row.

		// column header
		ColumnHeader laTraceNoHeader =
			new ColumnHeader(
				TRACENO_HEADER,
				TRACENO_STARTPT,
				TRACENO_LENGTH);

		// column header
		ColumnHeader laTransDateHeader =
			new ColumnHeader(
				TRANS_DATE_HEADER,
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);

		// column header
		ColumnHeader laOrigTransDateHeader =
			new ColumnHeader(
				ORIG_TRANS_DATE_HEADER,
				ORIG_TRANS_DATE_STARTPT,
				ORIG_TRANS_DATE_LENGTH);

		// column header
		ColumnHeader laCardNoHeader =
			new ColumnHeader(
				CREDIT_CARD_HEADER,
				CREDIT_CARD_STARTPT,
				CREDIT_CARD_LENGTH);
				
		// defect 9878
		ColumnHeader laCardTypeHeader =
			new ColumnHeader(
				CREDIT_CARD_TYPE_HEADER,
				CREDIT_CARD_TYPE_STARTPT,
				CREDIT_CARD_TYPE_LENGTH);		
		// end defect 9878		

		// column header, Fees --> note right alignment.
		ColumnHeader laFeesHeader =
			new ColumnHeader(
				FEES_HEADER,
				FEES_STARTPT + 4,
				FEES_LENGTH);

		lvHeaderRow.addElement(laTraceNoHeader);
		lvHeaderRow.addElement(laTransDateHeader);
		lvHeaderRow.addElement(laOrigTransDateHeader);
		lvHeaderRow.addElement(laCardNoHeader);
		// defect 9878
		lvHeaderRow.addElement(laCardTypeHeader);
		// end defect 9878
		lvHeaderRow.addElement(laFeesHeader);

		cvHeaderTable.addElement(lvHeaderRow);

		//obtain additional key data and add into header
		if (csDateHeader != null)
		{
			cvHeader.add("TRANSACTIONS FROM:    ");
			cvHeader.add(csDateHeader);
		}

		VendorReportData laDataline = null;

		double ldTotalFees = 0; // keep track of total changes
		// defect 9878
		double ldTotalVisaFees = 0;
		double ldTotalMCFees = 0;
		double ldTotalAmexFees = 0;
		double ldTotalDiscFees = 0;
		
		int liTotalVisaCharges = 0;
		int liTotalMCCharges = 0;
		int liTotalAmexCharges = 0;
		int liTotalDiscCharges = 0;
		
		boolean lbTraceSuffixedWithA = false;
		// end defect 9878

		// int liTotalCount = 0;
		// keep trach of number of total entries in report
		int i = 0; // i will be used to get each object.

		// format double to 2 decimal places - would prefer to use BigDecimal in future
		// default locale is Locale.setDefault(Locale.ENGLISH)
		DecimalFormat laDecForm = new DecimalFormat("####.00");

		if ((avResults == null) || (avResults.size() <= 0))
		{
			generateHeader(cvHeader, cvHeaderTable);
			generateNoRecordFoundMsg();
			// defect 8628
			// generateFooter();
			generateFooter(true);
			// end defect 8628 
			return;
		}

		generateHeader(cvHeader, cvHeaderTable);

		Log.finer(
			"GenVendorReport, formatReport, report header generated");

		Vector lvCaptureAndRefund = new Vector();
		Vector lvFailedRefund = new Vector();
		for (int liIndex = 0; liIndex < avResults.size(); liIndex++)
		{
			VendorReportData laData =
				(VendorReportData) avResults.elementAt(liIndex);
			if (laData.getType() == VendorReportData.REFUND_FAILED)
				lvFailedRefund.add(laData);
			else
				lvCaptureAndRefund.add(laData);
		}

		//Loop through results
		while (i < lvCaptureAndRefund.size())
		{

			int j = getRemainingLinesAndHandleFooter();

			// Output available number of lines data to the page.
			for (int k = 0; k <= j; k++)
			{

				if (i < lvCaptureAndRefund.size())
				{
					laDataline =
						(
							VendorReportData) lvCaptureAndRefund
								.elementAt(
							i);

					this.caRpt.print(
						laDataline.getItrntTraceNo(),
						TRACENO_STARTPT,
						TRACENO_LENGTH);
					this.caRpt.print(
						laDataline.getTransDate(),
						TRANS_DATE_STARTPT,
						TRANS_DATE_LENGTH);
					this.caRpt.print(
						laDataline.getOrigTransDate(),
						ORIG_TRANS_DATE_STARTPT,
						ORIG_TRANS_DATE_LENGTH);
					this.caRpt.print(
						laDataline.getCardNo(),
						CREDIT_CARD_STARTPT,
						CREDIT_CARD_LENGTH);
					// defect 9878
					this.caRpt.print(
						laDataline.getCardType(),
						CREDIT_CARD_TYPE_STARTPT,
						CREDIT_CARD_TYPE_LENGTH);
					if (laDataline.getCardType().equals("VISA"))
					{						
						ldTotalVisaFees += laDataline.getAmount();
						liTotalVisaCharges += 1;
					}
					else if (laDataline.getCardType().equals("MAST"))
					{						
						ldTotalMCFees += laDataline.getAmount();
						liTotalMCCharges += 1;
					}
					else if (laDataline.getCardType().equals("AMEX"))
					{						
						ldTotalAmexFees += laDataline.getAmount();
						liTotalAmexCharges += 1;
					}
					else if (laDataline.getCardType().equals("DISC"))
					{						
						ldTotalDiscFees += laDataline.getAmount();
						liTotalDiscCharges += 1;
					}
					else
					{
						lbTraceSuffixedWithA = true;										
					}
					// end defect 9878	
					this.caRpt.rightAlign(
						laDataline.getAmountString(),
						FEES_STARTPT,
						FEES_LENGTH);
					this.caRpt.nextLine();

					ldTotalFees += laDataline.getAmount();
					i = i + 1; // increment i to to get next record
				}
			}
		}
		
		// Footer can appear here.
		// defect 9878 - put the totals on a new page	
		Log.finer(
			"GenVendorReport, formatReport, report content printed");	
		if (lbTraceSuffixedWithA)
		{
			getRemainingLinesAndHandleFooter();
			// Summary for all
			this.caRpt.blankLines(LINES_ONE);
			this.caRpt.rightAlign(
				"TOTAL",
				TRACENO_STARTPT,
				TRACENO_LENGTH);
			this.caRpt.rightAlign(
				"" + lvCaptureAndRefund.size(),
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);
			this.caRpt.rightAlign(
				"TOTAL AMOUNT",
				CREDIT_CARD_STARTPT,
				CREDIT_CARD_LENGTH);
		}
		else
		{
			generateFooter();
			this.generateTotalHeader(cvHeader);

			//		this.caRpt.blankLines(LINES_ONE);
			this.caRpt.rightAlign(
				"TOTAL VISA",
				TRACENO_STARTPT,
				TRACENO_LENGTH);
			this.caRpt.rightAlign(
				"" + liTotalVisaCharges,
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);
			this.caRpt.rightAlign(
				"TOTAL VISA AMOUNT",
				TOTAL_AMT_STARTPT,
				CREDIT_CARD_LENGTH);
			this.caRpt.rightAlign(
				"$" + laDecForm.format(ldTotalVisaFees),
				FEES_STARTPT,
				FEES_LENGTH);

			this.caRpt.blankLines(LINES_ONE);
			this.caRpt.rightAlign(
				"TOTAL MAST",
				TRACENO_STARTPT,
				TRACENO_LENGTH);
			this.caRpt.rightAlign(
				"" + liTotalMCCharges,
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);
			this.caRpt.rightAlign(
				"TOTAL MAST AMOUNT",
				TOTAL_AMT_STARTPT,
				CREDIT_CARD_LENGTH);
			this.caRpt.rightAlign(
				"$" + laDecForm.format(ldTotalMCFees),
				FEES_STARTPT,
				FEES_LENGTH);

			this.caRpt.blankLines(LINES_ONE);
			this.caRpt.rightAlign(
				"TOTAL AMEX",
				TRACENO_STARTPT,
				TRACENO_LENGTH);
			this.caRpt.rightAlign(
				"" + liTotalAmexCharges,
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);
			this.caRpt.rightAlign(
				"TOTAL AMEX AMOUNT",
				TOTAL_AMT_STARTPT,
				CREDIT_CARD_LENGTH);
			this.caRpt.rightAlign(
				"$" + laDecForm.format(ldTotalAmexFees),
				FEES_STARTPT,
				FEES_LENGTH);

			this.caRpt.blankLines(LINES_ONE);
			this.caRpt.rightAlign(
				"TOTAL DISC",
				TRACENO_STARTPT,
				TRACENO_LENGTH);
			this.caRpt.rightAlign(
				"" + liTotalDiscCharges,
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);
			this.caRpt.rightAlign(
				"TOTAL DISC AMOUNT",
				TOTAL_AMT_STARTPT,
				CREDIT_CARD_LENGTH);
			this.caRpt.rightAlign(
				"$" + laDecForm.format(ldTotalDiscFees),
				FEES_STARTPT,
				FEES_LENGTH);

			// Summary for all
			this.caRpt.blankLines(LINES_TWO);
			this.caRpt.rightAlign(
				"TOTAL",
				TRACENO_STARTPT,
				TRACENO_LENGTH);
			this.caRpt.rightAlign(
				"" + lvCaptureAndRefund.size(),
				TRANS_DATE_STARTPT,
				TRANS_DATE_LENGTH);
			this.caRpt.rightAlign("     TOTAL AMOUNT",
			// CREDIT_CARD_STARTPT,
			TOTAL_AMT_STARTPT,
			CREDIT_CARD_LENGTH);
		}
		// end defect 9878
		
		this.caRpt.rightAlign(
			"$" + laDecForm.format(ldTotalFees),
			FEES_STARTPT,
			FEES_LENGTH);

		// Footer can appear here.
		getRemainingLinesAndHandleFooter();

		// defect 4795, Failed Refunds, begin
		i = 0;
		if (lvFailedRefund.size() > 0)
		{
			this.caRpt.blankLines(1);
			getRemainingLinesAndHandleFooter();
			this.caRpt.blankLines(1);
			getRemainingLinesAndHandleFooter();

			while (i < lvFailedRefund.size())
			{
				int j = getRemainingLinesAndHandleFooter();

				// Output available number of lines data to the page.
				for (int k = 0; k <= j; k++)
				{

					if (i < lvFailedRefund.size())
					{
						laDataline =
							(
								VendorReportData) lvFailedRefund
									.elementAt(
								i);

						this.caRpt.print(
							laDataline.getItrntTraceNo(),
							TRACENO_STARTPT,
							TRACENO_LENGTH);
						this.caRpt.print(
							laDataline.getTransDate(),
							TRANS_DATE_STARTPT,
							TRANS_DATE_LENGTH);
						this.caRpt.print(
							laDataline.getOrigTransDate(),
							ORIG_TRANS_DATE_STARTPT,
							ORIG_TRANS_DATE_LENGTH);
						this.caRpt.print(
							laDataline.getCardNo(),
							CREDIT_CARD_STARTPT,
							CREDIT_CARD_LENGTH);
						// defect 9878
						this.caRpt.print(
							laDataline.getCardType(),
							CREDIT_CARD_TYPE_STARTPT,
							CREDIT_CARD_TYPE_LENGTH);					
						// end defect 9878		
						this.caRpt.rightAlign(
							laDataline.getAmountString(),
							FEES_STARTPT,
							FEES_LENGTH);
						this.caRpt.nextLine();

						i = i + 1; // increment i to to get next record
					}
				}
			}
		}
		// defect 4795, Failed Refunds, end
		// defect 8628 
		generateFooter(true);
		// end defect 8628  
		Log.fine("GenVendorReport, formatReport, Exit");
	}
	
	/**
	 * Generate Attributes
	 * This abstract method must be implemented in all subclasses
	 *  
	 */
	public void generateAttributes()
	{
		// empty code block
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
			this.generateHeader(cvHeader, cvHeaderTable);
			j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;
		}
		return j;
	}
	
	/**
	 * QueryData
	 * 
	 * @param String asQuery
	 */
	public java.util.Vector queryData(String asQuery)
	{
		return null;
	}
}
