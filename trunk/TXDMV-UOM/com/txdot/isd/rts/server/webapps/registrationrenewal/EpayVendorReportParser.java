
package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.txdot.isd.rts.server.webapps.reports.VendorReportData;
import com.txdot.isd.rts.server.webapps.reports.VendorReportHtmlData;

import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;

/*
 * EpayVendorReportParser.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History 
 * Name     	Date        Description
 * ------------ ----------- --------------------------------------------
 * Clifford		10/09/2002	Defect 4795, Failed Refund transaction
 * 							handling.
 * 							modified getRecord(), filter().
 * Clifford		11/08/2002	Defect 4969, Failed Capture transaction
 * 							handling	
 * Clifford		03/04/2003	Defect 5459, Failed Refund transaction
 * 							handling enhancement.
 * 							modified getRecord().
 * S Johnston	02/25/2005	Code Cleanup for Java 1.4.2 upgrade
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * S Johnston	07/14/2005  Enhance logging for failed parsing of Epay
 *							Vendor Report
 *							add cvRequestURLs
 *							added setRequestURLs(Vector)
 *							modify findNoRecords(), main(), parseData()
 *							defect 8293 Ver 5.2.2 Fix 5
 * Ray Rowehl	07/29/2005	Code cleanup from informal walkthrough on 
 * 							defect 8293
 * 							Create constants, format code, clean out 
 * 							some old defect comments.
 * 							defect 7889 Ver 5.2.3
 * Ray Rowehl	08/01/2005	Clean up from defect 8293 walkthru.
 * 							Should be using defect 7889 for this.
 * 							add STRING_NOT_FOUND
 * 							defect 7889 Ver 5.2.3. 
 * Bob Brown 	11/21/2005  Code for the "@" character in traceno.
 *       					If there is a "@" in traceno, it means
 *       					the new capture traceno code on the TxO
 *       					side is adding the "@".
 *       					modify getRecord(String).
 *       					defect 7999 Ver 5.2.2 fix 7 
 * Bob Brown 	12/21/2005  Make sure failed refunds show up on the
 *       					Vendor Payment Report, and in the correct
 *       					format. 
 *       					modify getRecord(String)
 *       					modify filter(Vector)
 *      					defect 8489 Ver 5.2.2 fix 8
 * Bob Brown 	05/01/2006  Add the ability to parse Epay records based
 *       					on the Epay site the code is retrieving 
 *       					from: either the new or old site.
 *       					This is accomplished by looking at the
 *       					EpayVendorParseMethodVersion parameter value
 * 							in the appropriate properties file.
 * 							Change TXT_ variables to STR_       					
 *      					modify findNoRecords(String)
 *       					modify getNextRecordString()
 *       					modify getRecord(String)
 *       					modify goToRecordStart()
 *       					modify filter(Vector)
 * 							modify isDataInRange
 * 							modify setDateinfo
 *       					defect 8609 Ver 5.2.2 Fix 8
 * Bob Brown    06/22/2006  Added a check for "null" trans status
 *       					due to new html from new Epay site.
 *       					modify filter(), getRecord()
 *       					defect 8609 Ver 5.2.2 Fix 8 
 * Bob Brown	12/16/2008	Add credit card type
 * 							modify getRecord()
 * 							defect 9878 Ver Defect_POS_C
 * Bob Brown	01/20/2009  Add code to get the transaction history
 * 							href for accessing the original capture
 * 							date.
 * 							add parseTransHistoryHref()	
 * 							defect 9907 Ver Defect_POS_D
 * Bob Brown	01/29/2009  Reverse previous changes
 * 							deprecate  parseTransHistoryHref()	
 * 							defect 9907 Ver Defect_POS_D 
 * ---------------------------------------------------------------------
 */ 

/**
 * Parse the vendor report from html text source.
 * 
 * @version	Defect_POS_D	01/29/2009   
 * @author	Administrator
 * <br>Creation Date:       02/26/2002 11:39:42
 */
public class EpayVendorReportParser
{
	private static final String ERR_MSG_FAILED_TO_GET_VPR_DATA =
		"Failed to retrieve the Epay Vendor Report data";
	private static final String ERR_MSG_NO_REC_FOUND =
		"EpayVendorReportParser, No record found";

	// delimiter for finding the records


	private static final String RECORDS_FOUND = " records found";

	private static final String STR_AMPERSAND = "&";

	private static final String STR_GREATER_THAN = ">";
	private static final String STR_HTTP_BLANK_SPACE = "&nbsp";
	private static final String STR_START_NEW_LINE = "\r\n";
    
    // defect 8609
	private static final String STR_REFUND_FAILED = "REFUND FAILED";
    
	private static final String STR_DASHED_LINE =
		"===================================";
	private static final String STR_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	private static final String STR_DATE_FORMAT_FOR_PARSE =
		"yyyyMMdd HH:mm:ss";
	private static final String STR_EPAYVENDORREPORTPARSER_COMMA =
		"EpayVendorReportParser, ";
	private static final String STR_IMPROPERLY_FORMATTED_VPR_BEGIN =
		"Improperly formatted VPR Result on: ";
	private static final String STR_IMPROPERLY_FORMATTED_VPR_END =
		"End Improperly formated VPR Result.";
	// end defect 8609	

	private static final String STR_TRACE_NUM = "TRACE_NUM=";

	private static final int GO_IN_5_POSITIONS = 5;
	private static final int GO_IN_9_POSTIONS = 9;
	private static final int GO_IN_10_POSITIONS = 10;
	private static final int STRING_NOT_FOUND = -1;

	// transaction type constants.	
	public static final int CAPTURE = 0;
	public static final int REFUND = 1;
	// parser filter mode constants
	private static final int DATE_FILTER = 0;
	public static final int NO_FILTER = 1;

	// raw data for parsing
	// defect 9878
	// private Vector cvVendorHtmlData;
	protected Vector cvVendorHtmlData;
	// end defect 9878
	// parsed results
	private Vector cvVendorReportData;
	// filter mode.
	private int ciFilterMode;
	// the caFromDate date csToDate apply the filter	
	private Date caFromDate;
	// the csToDate date csToDate apply the filter
	private Date csToDate;
	// default csToDate capture
	private int ciType = CAPTURE;
	// orginal vendor report retrieved caFromDate Epay in html format
	private String csVendorHtml;
	// Number of transactions in the result 
	private int ciNoRecords;
	// defect 8293
	private Vector cvRequestURLs = new Vector();
	// end defect 8293
	
	// defect 8609
	private static final String RECORD_DELIM_LOWERCASE = "</tr>";
	private static final String RECORD_DELIM_UPPERCASE = "</TR>";
	//private static final String STR_NO_DATA_INDICATOR = "</center";
	private static final String STR_NO_DATA_INDICATOR_PARSE_1_2 = 
	"transactions";
	private static final String STR_NO_DATA_INDICATOR_PARSE_1_1 = 
	"</center";
	 private static final String STR_BEGINING_OF_DATA_SEARCH_PARSE_1_2 =
	 "<p>";
	 private static final String STR_BEGINING_OF_DATA_SEARCH_PARSE_1_1 =
	 "Number of transactions returned matching this criteria:";
	 // private static final String STR_REPORT_VERSION_1_1 = "1.1";
	private static final String STR_REPORT_VERSION_1_2 = "1.2";
	private static final String STR_START_TABLE_DATA_LOWERCASE = "<td>";
	private static final String STR_CLOSE_TABLE_DATA_LOWERCASE = "</td>";
	private static final String STR_START_TABLE_DATA_UPPERCASE = "<TD>";
	private static final String STR_CLOSE_TABLE_DATA_UPPERCASE = "</TD>";
	// end defect 8609
	/**
	 * EpayVendorReportParser constructor.
	 */
	public EpayVendorReportParser()
	{
		cvVendorReportData = new Vector();
		cvVendorHtmlData = new Vector();
		// default csToDate date filter, filtering out
		// the records out of the time range.
		ciFilterMode = DATE_FILTER;
	}

	/**
	 * Add Data
	 * 
	 * @param aiType int
	 * @param avHtmlData Vector
	 */
	public void addData(int aiType, Vector avHtmlData)
	{
		for (int i = 0; i < avHtmlData.size(); ++i)
		{
			String lsData = (String) avHtmlData.elementAt(i);
			addData(new VendorReportHtmlData(aiType, lsData));
		}
	}

	/**
	 * Add Data
	 * 
	 * @param aaData VendorReportHtmlData
	 */
	public void addData(VendorReportHtmlData aaData)
	{
		cvVendorHtmlData.add(aaData);
	}

	/**
	 * Clear Data
	 */
	public void clearData()
	{
		cvVendorHtmlData = new Vector();
		cvVendorReportData = new Vector();
	}

	/**
	 * Display record status.
	 * Output is to standard out.
	 */
	public void display()
	{
		if (cvVendorReportData.size() == CommonConstant.ELEMENT_0)
		{
			System.out.println(ERR_MSG_NO_REC_FOUND);
		}
		if (cvVendorReportData.size() > CommonConstant.ELEMENT_0)
		{
			System.out.println(
				STR_EPAYVENDORREPORTPARSER_COMMA
					+ cvVendorReportData.size()
					+ RECORDS_FOUND);
		}

		for (int i = 0; i < cvVendorReportData.size(); ++i)
		{
			VendorReportData aaData =
				(VendorReportData) cvVendorReportData.elementAt(i);
			System.out.println(
				i
					+ CommonConstant.STR_SPACE_ONE
					+ aaData.getItrntTraceNo()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ aaData.getOrderId()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ aaData.getTransDate()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ aaData.getCardNo()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ aaData.getAmount());
		}
	}

	/**
	 * Filter out records that are not failed refunds
	 * 
	 * @param avVendorReportData Vector
	 * @return Vector
	 */
	private Vector filter(Vector avVendorReportData)
	{
		int i = 0;
		while (i < avVendorReportData.size())
		{
			VendorReportData laData =
				(VendorReportData) avVendorReportData.elementAt(i);
			// failed refund, begin
			// defect 8489
			// put failed refunds back on the VPR by commenting out the below code.
			// Defect 4795, failed refund, begin
//			if (laData.getType() == VendorReportData.REFUND_FAILED)
//			{
//				// Failed refund transaction does not have good
//				// transaction date, cannot verify its date range.
//				i = i + 1;
//				continue;
//			}
			// defect 4795 failed refund, end
			// end defect 8489

//			defect 8609
		   if (laData.getTransDate() == null ||
			   laData.getTransDate().equals("") ||
			   laData.getTransDate().equals(STR_HTTP_BLANK_SPACE))
		   {
				avVendorReportData.remove(i);
		   }
		   else
		   {   
				if(!isDataInRange(laData))
				{
			 		avVendorReportData.remove(i);
				}
				else
				{
					// defect 8609 
					if(laData.getTransStatus().equals("null"))
				 	{
				  		avVendorReportData.remove(i);
				 	}
					// end defect 8609
			 		else
					{
			 			i++;
					}
				}
		   }
		   // end defect 8609
		}
		return avVendorReportData;
	}

	/**
	 * This method parses the result HTML
	 *
	 * @param asRequestURL String
	 * @throws Exception
	 */
	private void findNoRecords(String asRequestURL) throws Exception
	{
		// strip off the header info
		int liStart = 0;
		String lsStart = null;
		
		// defect 8609
		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			lsStart = STR_BEGINING_OF_DATA_SEARCH_PARSE_1_2;
		}
		else
		{
			lsStart = STR_BEGINING_OF_DATA_SEARCH_PARSE_1_1;
		}
		// end defect 8609
		
		liStart = csVendorHtml.indexOf(lsStart);
		// defect 8293
		if (liStart != STRING_NOT_FOUND)
		{
			// Retrieve a properly formatted Request Screen
			csVendorHtml = csVendorHtml.substring(liStart);
			
			int liNoRecordEnd =  0;
			
			// defect 8609
			if (CommunicationProperty
				.getEpayVendorParseMethodVersion()
				.equals(STR_REPORT_VERSION_1_2))
			{
				liNoRecordEnd = 
				csVendorHtml.indexOf(STR_NO_DATA_INDICATOR_PARSE_1_2);
			}
			else
			{
				liNoRecordEnd = 
				csVendorHtml.indexOf(STR_NO_DATA_INDICATOR_PARSE_1_1);
			}
			// end defect 8609
			
			if (liNoRecordEnd != STRING_NOT_FOUND)
			{
				String lsNoRecords =
					csVendorHtml.substring(
						lsStart.length(),
						liNoRecordEnd);
				lsNoRecords = lsNoRecords.trim();
				ciNoRecords = Integer.parseInt(lsNoRecords);
			}
			else
			{
				ciNoRecords = 0;
			}
		}
		else
		{
			// Log the returned HTML String
			ciNoRecords = 0;
			// defect 8609
			String lsError =
				STR_START_NEW_LINE
					+ STR_DASHED_LINE
					+ STR_START_NEW_LINE
					+ STR_IMPROPERLY_FORMATTED_VPR_BEGIN
					+ asRequestURL
					+ STR_START_NEW_LINE
					+ STR_DASHED_LINE
					+ STR_START_NEW_LINE
					+ csVendorHtml
					+ STR_DASHED_LINE
					+ STR_START_NEW_LINE
					+ STR_IMPROPERLY_FORMATTED_VPR_END
					+ STR_START_NEW_LINE
					+ STR_DASHED_LINE;
			// end defect 8609		
			com.txdot.isd.rts.services.util.Log.write(
				com.txdot.isd.rts.services.util.Log.SQL_EXCP,
				this,
				lsError);
			throw new Exception(ERR_MSG_FAILED_TO_GET_VPR_DATA);
		}
		// end defect 8293
	}

	/**
	 * getNextRecordString
	 * 
	 * @return String
	 */
	private String getNextRecordString()
	{
		String lsRecord = null;
		// defect 8609
		int liIndex = csVendorHtml.indexOf(RECORD_DELIM_LOWERCASE);
		if (liIndex < 0)
		{
			liIndex = csVendorHtml.indexOf(RECORD_DELIM_UPPERCASE);
		}
		
		lsRecord = csVendorHtml.substring(0, liIndex);
		// advance the content - either delimiter is the correct length
		csVendorHtml =
			csVendorHtml.substring(
				liIndex + RECORD_DELIM_UPPERCASE.length());
		// end defect 8609		
		
		return lsRecord;
	}

	/**
	 * Parse the record off of the form.
	 * 
	 * @param asRecord String
	 * @return VendorReportData
	 */
	private VendorReportData getRecord(String asRecord)
	{
		// trace number
		// defect 8609
		int liTraceNoStart = asRecord.indexOf(STR_TRACE_NUM);
		// end defect 8609
		int liTraceNoEnd = asRecord.indexOf(STR_AMPERSAND);
		String lsTraceNo =
			asRecord.substring(
				liTraceNoStart + GO_IN_10_POSITIONS,
				liTraceNoEnd);
		asRecord = asRecord.substring(liTraceNoEnd + 1);
		// defect 9878
		String lsCardType = "";
		// end defect 9878
		// defect 7999		
		if (lsTraceNo.indexOf("@") > -1)
		{
			lsTraceNo =
				lsTraceNo.substring(0, lsTraceNo.indexOf("@"))
					+ lsTraceNo.substring(lsTraceNo.length() - 1);
		}
		// defect 9878
		else if (lsTraceNo.substring(lsTraceNo.length() - 1).equals("V"))
		{		
			lsCardType = "VISA";
		}
		else if (lsTraceNo.substring(lsTraceNo.length() - 1).equals("X"))
		{		
			lsCardType = "AMEX";
		}
		else if (lsTraceNo.substring(lsTraceNo.length() - 1).equals("M"))
		{		
			lsCardType = "MAST";
		}
		else if (lsTraceNo.substring(lsTraceNo.length() - 1).equals("D"))
		{		
			lsCardType = "DISC";
		}
		// end defect 9878		
		// end defect 7999

		// vendor id	
		int liVendorIdEnd = asRecord.indexOf(STR_AMPERSAND);
		// defect 8609
		// String lsVendorId = asRecord.substring(10, liVendorIdEnd);
		// end defect 8609
		asRecord = asRecord.substring(liVendorIdEnd + 1);

		// order id
		int liOrderIdEnd = asRecord.indexOf(STR_GREATER_THAN);
		String lsOrderId = asRecord.substring(GO_IN_9_POSTIONS, liOrderIdEnd);
		asRecord = asRecord.substring(liOrderIdEnd + 1);

		//  part1, begin
		// skip 2 </TD> or &nbsp

		int liIndex = 0;

		for (int i = 0; i < 2; ++i)
		{
			// defect 8609
			if (CommunicationProperty
				.getEpayVendorParseMethodVersion()
				.equals(STR_REPORT_VERSION_1_2))
			{
				liIndex = asRecord.indexOf(STR_HTTP_BLANK_SPACE);
			}
			else
				liIndex = asRecord.indexOf(STR_CLOSE_TABLE_DATA_LOWERCASE);
			if (liIndex < 0)
			{
				liIndex = asRecord.indexOf(STR_CLOSE_TABLE_DATA_UPPERCASE);

			}
			asRecord = asRecord.substring(liIndex + GO_IN_5_POSITIONS);
			// end defect 8609
		}

		int liTransStatusStart = 0;

		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			// defect 8609
			// int liTransStatusStart = asRecord.indexOf(STR_GREATER_THAN);
			liTransStatusStart =
				asRecord.indexOf(STR_START_TABLE_DATA_LOWERCASE);
			if (liTransStatusStart < 0)
			{
				liTransStatusStart =
					asRecord.indexOf(STR_START_TABLE_DATA_UPPERCASE);
			}

			// end defect 8609
		}
		else
		{
			liTransStatusStart = asRecord.indexOf(STR_GREATER_THAN);
		}

		int liTransStatusEnd = 0;
		
		// defect 8609
		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			liTransStatusEnd = asRecord.indexOf(STR_HTTP_BLANK_SPACE);
		}
		else
		{
			liTransStatusEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA_LOWERCASE);
			if (liTransStatusEnd < 0)
			{
				liTransStatusEnd =
					asRecord.indexOf(STR_CLOSE_TABLE_DATA_UPPERCASE);
			}

		}
		// end defect 8609

		// defect 8609 - new
		//String lsTransStatus =
		//	asRecord.substring(liTransStatusStart + 1, liTransStatusEnd);
		String lsTransStatus =
			asRecord.substring(liTransStatusStart + 4, liTransStatusEnd);
		// end defect 8609 new
		
		asRecord = asRecord.substring(liTransStatusEnd + GO_IN_5_POSITIONS);
		//  part1, end

		int liTransDateStart = 0;
		int liTransDateEnd = 0;
		String lsTransDate = null;
		
		// defect 8609
		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			// transaction date time
			
			//int liTransDateStart = asRecord.indexOf(STR_GREATER_THAN);
			liTransDateStart = asRecord.indexOf(STR_START_TABLE_DATA_LOWERCASE);
			if (liTransDateStart < 0)
			{
				liTransDateStart =
					asRecord.indexOf(STR_START_TABLE_DATA_UPPERCASE);
			}
			// liTransDateEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liTransDateEnd = asRecord.indexOf(STR_HTTP_BLANK_SPACE);
			lsTransDate =
				asRecord.substring(liTransDateStart + 4, liTransDateEnd);
		}
		else
		{
			liTransDateStart = asRecord.indexOf(STR_GREATER_THAN);
			// liTransDateEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liTransDateEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA_LOWERCASE);
			if (liTransDateEnd < 0)
			{
				liTransDateEnd =
					asRecord.indexOf(STR_CLOSE_TABLE_DATA_UPPERCASE);
			}
			lsTransDate =
				asRecord.substring(liTransDateStart + 1, liTransDateEnd);
		}
		// end defect 8609

		asRecord = asRecord.substring(liTransDateEnd + GO_IN_5_POSITIONS);

		int liCardNoStart = 0;
		int liCardNoEnd = 0;
		String lsCardNo = null;

		// defect 8609
		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			liCardNoStart = asRecord.indexOf(STR_START_TABLE_DATA_LOWERCASE);
			//liCardNoEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liCardNoEnd = asRecord.indexOf(STR_HTTP_BLANK_SPACE);
			lsCardNo = asRecord.substring(liCardNoStart + 4, liCardNoEnd);
		}
		else
		{
			liCardNoStart = asRecord.indexOf(STR_GREATER_THAN);
			//liCardNoEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liCardNoEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA_LOWERCASE);
			if (liCardNoEnd < 0)
			{
				liCardNoEnd =
					asRecord.indexOf(STR_CLOSE_TABLE_DATA_UPPERCASE);
			}
			lsCardNo = asRecord.substring(liCardNoStart + 1, liCardNoEnd);
		}
		// end defect 8609

		asRecord = asRecord.substring(liCardNoEnd + GO_IN_5_POSITIONS);
		
		int liCardNameIndexEnd = 0;
		
		// defect 8609
		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			liCardNameIndexEnd = asRecord.indexOf(STR_HTTP_BLANK_SPACE);
		}
		else
		{
			//int liCardNameIndexEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liCardNameIndexEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA_LOWERCASE);
			if (liCardNameIndexEnd < 0)
			{
				liCardNameIndexEnd =
					asRecord.indexOf(STR_CLOSE_TABLE_DATA_UPPERCASE);
			}			
		}
		// end defect 8609
				
		asRecord = asRecord.substring(liCardNameIndexEnd + GO_IN_5_POSITIONS);

		// fees
		int liFeeStart = 0;
		int liFeeEnd = 0;
		String lsFee = null;

		// defect 8609
		if (CommunicationProperty
			.getEpayVendorParseMethodVersion()
			.equals(STR_REPORT_VERSION_1_2))
		{
			liFeeStart = asRecord.indexOf(STR_START_TABLE_DATA_LOWERCASE);
			if (liFeeStart < 0)
			{
				asRecord.indexOf(STR_START_TABLE_DATA_UPPERCASE);
			}
			//liFeeEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liFeeEnd = asRecord.indexOf(STR_HTTP_BLANK_SPACE);
			lsFee = asRecord.substring(liFeeStart + 4, liFeeEnd);
		}
		else
		{
			liFeeStart = asRecord.indexOf(STR_GREATER_THAN);
			//liFeeEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA);
			liFeeEnd = asRecord.indexOf(STR_CLOSE_TABLE_DATA_LOWERCASE);
			if (liFeeEnd < 0)
			{
				liFeeEnd =
					asRecord.indexOf(STR_CLOSE_TABLE_DATA_UPPERCASE);
			}	
			lsFee = asRecord.substring(liFeeStart + 1, liFeeEnd);
		}
		// end defect 8609

		if (ciType == REFUND)
		{
			lsFee = CommonConstant.STR_DASH + lsFee;
		}

		// OK, here we get the data
		VendorReportData laVendorReportData = new VendorReportData();
		laVendorReportData.setItrntTraceNo(lsTraceNo);
		laVendorReportData.setOrderId(lsOrderId);
		laVendorReportData.setTransDate(lsTransDate);
		laVendorReportData.setCardNo(lsCardNo);
		// defect 9878
		laVendorReportData.setCardType(lsCardType);
		// end defect 9878
		laVendorReportData.setAmountString(lsFee);
		
		// new defect 8609
		laVendorReportData.setTransStatus(lsTransStatus);
		// end defect 8609


		//  failed refund, begin
		if (ciType == CAPTURE)
		{
			// Capture transaction.
			if (laVendorReportData
				.getTransDate()
				.equalsIgnoreCase(STR_HTTP_BLANK_SPACE)
				|| STR_HTTP_BLANK_SPACE.equalsIgnoreCase(lsTransStatus))
			{
				// Failed Capture transaction ==> no show. 
				return null;
			}
			else
			{
				// Good capture transaction.
				laVendorReportData.setType(VendorReportData.CAPTURE);
			}
		}
		else
		{
			// refund transaction.
			if (laVendorReportData
				.getTransDate()
				.equalsIgnoreCase(STR_HTTP_BLANK_SPACE)
				// CQ 5459, (enhancement), part2, begin
				|| STR_HTTP_BLANK_SPACE.equalsIgnoreCase(lsTransStatus))
				// CQ 5459, (enhancement), part2, end	
			{
				// defect 8489
				//laVendorReportData.setTransDate(STR_REFUND_FAILED);
				// defect 8609
				laVendorReportData.setCardNo(
					laVendorReportData.getCardNo() + " " + STR_REFUND_FAILED);
				// end defect 8609	
				laVendorReportData.setType(VendorReportData.REFUND_FAILED);
				// end defect 8489	
			}
			else
			{
				laVendorReportData.setType(VendorReportData.REFUND);
			}
		}
		return laVendorReportData;
	}

	/**
	 * Get the report data records
	 * 
	 * @return Vector
	 */
	protected Vector getRecords()
	{
		Vector lvRecords = new Vector();
		for (int i = 0; i < ciNoRecords; ++i)
		{
			String lsRecord = getNextRecordString();
			VendorReportData laVendorReportData = getRecord(lsRecord);
			if (laVendorReportData != null)
			{
				lvRecords.add(laVendorReportData);
			}
		}
		return lvRecords;
	}

	/**
	 * Go to Record start.
	 * That comes right after a table record end.
	 */
	private void goToRecordStart()
	{
		// go once for the delimiter </TR>
		// defect 8609
		int liIndex = csVendorHtml.indexOf(RECORD_DELIM_LOWERCASE);
		if (liIndex < 0)
		{
			liIndex = csVendorHtml.indexOf(RECORD_DELIM_UPPERCASE);
			csVendorHtml =
				csVendorHtml.substring(
					liIndex + RECORD_DELIM_UPPERCASE.length());
		}
		else
		{
			csVendorHtml =
				csVendorHtml.substring(
					liIndex + RECORD_DELIM_LOWERCASE.length());
		}
		// end defect 8609

	}

	/**
	 * isDataInRange
	 * 
	 * @param aaData VendorReportData
	 * @return boolean
	 */
	private boolean isDataInRange(VendorReportData aaData)
	{
		// defect 8609
		SimpleDateFormat laSDF = new SimpleDateFormat(STR_DATE_FORMAT);
		// end defect 8609
		String lsTransDate = aaData.getTransDate();
		Date laDate = laSDF.parse(lsTransDate, new ParsePosition(0));

		if (laDate.getTime() >= caFromDate.getTime()
			&& laDate.getTime() < csToDate.getTime())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// dynamic data
		String lsVendorId = "2246";
		String lsFromDate = "20020611";
		String lsToDate = "20020620";
		// for testing purpose only	 
		EpayVendorReportFetcher laFetcher =
			new EpayVendorReportFetcher();
		laFetcher.setVendorId(lsVendorId);
		// //////////////////////////////////////////
		// do NOT set a date range which is too large
		laFetcher.setDateRange(lsFromDate, lsToDate);
		laFetcher.addPage();
		laFetcher.setPaymentType("CCCR");
		laFetcher.addPage();
		EpayVendorReportParser laParser = new EpayVendorReportParser();
		laParser.setDateInfo(lsFromDate, lsToDate, "03:00:00");
		try
		{
			Vector lvResult = laFetcher.fetch();
			laParser.addData(
				new VendorReportHtmlData(
					CAPTURE,
					(String) lvResult.elementAt(
						CommonConstant.ELEMENT_0)));
			laParser.addData(
				new VendorReportHtmlData(
					REFUND,
					(String) lvResult.elementAt(
						CommonConstant.ELEMENT_1)));
			// defect 8293
			// The vector is never read
			//Vector lvReportData = 
			laParser.parse();
			// end defect 8293
			laParser.display();
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Run through the parsing.
	 * 
	 * @return Vector
	 * @throws Exception
	 */
	public Vector parse() throws Exception
	{
		for (int i = 0; i < cvVendorHtmlData.size(); ++i)
		{
			VendorReportHtmlData laData =
				(VendorReportHtmlData) cvVendorHtmlData.elementAt(i);
			ciType = laData.getType();
			csVendorHtml = laData.getHtml();
			Vector lvVendorReportData =
				parseData((String) cvRequestURLs.get(i));
			if (lvVendorReportData != null)
			{
				cvVendorReportData.addAll(lvVendorReportData);
			}
		}
		return cvVendorReportData;
	}
	
	/**
	 * Get the href for the details query.
	 * 
	 * @return Vector
	 * @throws Exception
	 */
	public String parseHref(int aiIndex) throws Exception
	{
		String lsHrefURL = "";
//		for (int i = 0; i < cvVendorHtmlData.size(); ++i)
//		{
			VendorReportHtmlData laData =
				(VendorReportHtmlData) cvVendorHtmlData.elementAt(aiIndex);
			csVendorHtml = laData.getHtml();
			if (csVendorHtml.indexOf("/customerservice/DisplayDetails")
				> -1)
			{
				int liStartofHref =
					csVendorHtml.indexOf(
						"/customerservice/DisplayDetails");
				String lsHrefHtmlStart =
					csVendorHtml.substring(liStartofHref);
				int liRightBracket = lsHrefHtmlStart.indexOf(">");
				lsHrefURL =
					lsHrefHtmlStart.substring(0, liRightBracket - 1);
//				break;
			}

//		}
		return lsHrefURL;
	}
	
	/**
	 * Get the href for the details query.
	 * 
	 * @return Vector
	 * @throws Exception
	 * @deprecate
	 */
	public String parseTransHistoryHref(int aiIndex) throws Exception
	{
		String lsHrefURL = "";
//		for (int i = 0; i < cvVendorHtmlData.size(); ++i)
//		{
			VendorReportHtmlData laData =
				(VendorReportHtmlData) cvVendorHtmlData.elementAt(aiIndex);
			csVendorHtml = laData.getHtml();
			if (csVendorHtml.indexOf("/customerservice/TransHistory")
				> -1)
			{
				int liStartofHref =
					csVendorHtml.indexOf(
						"/customerservice/TransHistory");
				String lsHrefHtmlStart =
					csVendorHtml.substring(liStartofHref);
				int liRightBracket = lsHrefHtmlStart.indexOf(">");
				lsHrefURL =
					lsHrefHtmlStart.substring(0, liRightBracket - 1);
//				break;
			}

//		}
		return lsHrefURL;
	}

	/**
	 * parseData
	 * 
	 * @param asRequestURL String
	 * @return Vector
	 * @throws Exception
	 */
	private Vector parseData(String asRequestURL) throws Exception
	{
		// defect 8293 
		// added "throws Exception"
		// end defect 8293
		findNoRecords(asRequestURL);
		Vector lvVendorReportData = null;
		if (ciNoRecords > 0)
		{
			goToRecordStart();
			lvVendorReportData = getRecords();
		}
		if (lvVendorReportData != null && ciFilterMode != NO_FILTER)
		{
			lvVendorReportData = filter(lvVendorReportData);
		}
		return lvVendorReportData;
	}

	/**
	 * Parse out Date Information
	 * 
	 * @param asNewFrom String
	 * @param asNewTo String
	 * @param asCutOffTime String
	 */
	public void setDateInfo(
		String asNewFrom,
		String asNewTo,
		String asCutOffTime)
	{
		String lsFrom =
			asNewFrom + CommonConstant.STR_SPACE_ONE + asCutOffTime;
		String lsTo =
			asNewTo + CommonConstant.STR_SPACE_ONE + asCutOffTime;
		// defect 8609	
		caFromDate =
			UtilityMethods.parseDate(lsFrom, STR_DATE_FORMAT_FOR_PARSE);
		csToDate =
			UtilityMethods.parseDate(lsTo, STR_DATE_FORMAT_FOR_PARSE);
		// end defect 8609	
	}

	/**
	 * Set Filter Mode
	 * 
	 * @param aiNewFilterMode int
	 */
	public void setFilterMode(int aiNewFilterMode)
	{
		ciFilterMode = aiNewFilterMode;
	}

	/**
	 * Sets the Request URLs
	 * 
	 * @param avRequestURLs Vector
	 */
	public void setRequestURLs(Vector avRequestURLs)
	{
		cvRequestURLs = avRequestURLs;
	}
}
