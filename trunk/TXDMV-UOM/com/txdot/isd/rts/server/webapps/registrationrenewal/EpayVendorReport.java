package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.util.Vector;

import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;

import com.txdot.isd.rts.server.webapps.reports.VendorReportData;
import com.txdot.isd.rts.server.webapps.reports.VendorReportHtmlData;
import com.txdot.isd.rts.server.webapps.util.Log;

/*
 * EpayVendorReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History 
 * Name     	Date        Description
 * ------------ ----------- --------------------------------------------
 * B Brown		07/03/2003	For CQU 6310,changed the get vendorReport
 * 							method to look at central time, since epay
 * 							recently moved to Dallas. Am using constant:
 *                       	RegRenProcessingConstants.
 * 							ITRANS_RPT_CUTOFF_TIME to achieve this.
 * B Brown		07/17/2003	Defect6358 - Undo CQU 6310,changes
 * 							going back to Eastern time because Global
 *                       	payments system stayed Eastern time.
 * S Johnston	02/25/2005	Code Cleanup for Java 1.4.2 upgrade
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * S Johnston	07/14/2005  Enhance logging for failed fetching / 
 * 							parsing of the Epay Vendor Report
 *							modify getVendorReport()
 *							defect 8293 ver 5.2.2 Fix 5 
 * Ray Rowehl	07/28/2005	Code cleanup from informal walkthrough of 
 * 							defect 8293.
 * 							defect 7889 Ver 5.2.3
 * Ray Rowehl	08/01/2005	Code cleanup from walkthrough.
 * 							Should be using defect 7889 for this work.
 * 							defect 7889 Ver 5.2.3
 * B Brown  	05/01/2006  When matching the refund to the original 
 *       					capure, search for the original capture
 *       					using the "*" character at the end of the 
 *       					trace number.
 * 							Also, put the "R" back at the end of trace 
 * 							number for the report. 
 * 							add ASTERISK.
 * 							rename getRefundTracenum to 
 * 							getRefundTracenos
 * 							rename TXT_ variables to STR_
 * 							deprecate getOrigTraceNo()
 *       					modify fillRefundOrigTransDate()
 *       					modify getRefundTraceNo()
 *       					modify getRefundTraceNum() 
 * 							modify display()
 * 							modify getVendorReport()
 *       					defect 8609 Ver 5.2.2 Fix 8
 * B Brown		07/27/2006	Look at the properties file value of 
 * 							getVendorPayRptUseProxy()() to see if 
 *							the Vendor Payment report process will use 
 *							proxy or not.
 *							modify getVendorReport()
 *							defect 8368 Ver 5.2.4 
 * B Brown		12/16/2008	Since the capture trace numbers are no 
 * 							longer suffixed by "A", we must get the 
 * 							preauth trace number, then get the original 
 * 							capture date/time, and card type from the 
 *							pre-auth details.	
 * 							add STR_PREAUTH_REQUEST.
 * 							add fillOrigTransDateAndCardType() 
 * 							modify getOrigTraceNo(), getRefundTraceNo(),
 * 							getRefundTraceNos(), getVendorReport()
 * 							defect 9878 Ver Defect_POS_C 
 * B Brown		12/31/2008	Access refunds by refund trace numnber 
 * 							instead of by preauth.
 * 							modify getRefundTraceNos(), 
 * 							getVendorReport()
 * 							defect 9878 Ver Defect_POS_C
 * B Brown		01/20/2009	Add refund original trans date to the 
 * 							report.
 * 							deprecate fillRefundOrigTransDate()
 * 							deprecate fillOrigTransDateAndCardType()
 * 							add fillRefundOrigTransDate(String)
 * 							modify fillCardType(), getVendorReport()  
 * 							defect 9907 Ver Defect_POS_D 
 * K Harrell	06/19/2009	Use ReportConstant vs.
 * 							 RegRenProcessingConstants. 
 * 							Additional cleanup. 
 * 							delete STR_PREAUTH_REQUEST, ASTERISK,
 * 							 STR_CAPTURE_REQUEST, STR_CODE_CAPTURE
 * 							delete getOrigTraceNo()
 * 							modify fillCardType()  
 * 							defect 10023 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods for Epay Vendor Reports.
 * 
 * @version	Defect_POS_F	06/19/2009   
 * @author	Clifford Chen
 * <br>Creation Date:		07/26/2002 15:44:48 
 */
public class EpayVendorReport
{
	private static final String MSG_ERR_NO_RECORD =
		"EpayVendorReport, No record found";
	private static final String MSG_GETVENDORREPORT_BEGIN =
		"EpayVendorReport, getVendorReport, Enter";
	private static final String MSG_GETVENDORREPORT_EXIT =
		"EpayVendorReport, getVendorReport, Exit";
	private static final String MSG_GETVENDORREPORT_FETCHED =
		"EpayVendorReport, getVendorReport, capture and refund Html"
			+ " fetched";
	private static final String MSG_GETVENDORREPORT_PARSED =
		"EpayVendorReport, getVendorReport, capture and refund Html"
			+ " parsed";
	private static final String MSG_GETVENDORREPORT_REFUND_FETCHED =
		"EpayVendorReport, getVendorReport, refund Orig trans Html"
			+ " fetched";
	private static final String MSG_GETVENDORREPORT_REFUND_ORIG_DATE =
		"EpayVendorReport, getVendorReport, refund orig trans date"
			+ " filled";
	private static final String MSG_GETVENDORREPORT_REFUND_PARSED =
		"EpayVendorReport, getVendorReport, refund orig trans Html"
			+ " parsed";

	private static final String STR_CODE_REFUND = "R";

	// defect 8609
	private static final String STR_EPAYVENDORRPT_COMMA =
		"EpayVendorReport, ";
	private static final String STR_REFUND_REQUEST = "CCCR";
	private static final String STR_RECORDS_FOUND = " records found";
	// end defect 8609

	// defect 10023 
	// Not Used 
	//	private static final String STR_PREAUTH_REQUEST = "CCAO";
	//	private static final String ASTERISK = "*";
	//	private static final String STR_CAPTURE_REQUEST = "CCCAP";
	//	private static final String STR_CODE_CAPTURE = "A";
	// end defect 10023 

	// The vendor report data vector
	private Vector cvVendorReportData;
	// The starting index of the refund data
	private int ciRefundStartIndex = -1;
	// The vector holding the capture transactions for the refunds
	private Vector cvVendorReportRefundData;

	/**
	 * EpayVendorReport default constructor.
	 */
	public EpayVendorReport()
	{
		super();
	}

	/**
	 * Display Epay Vendor Report.
	 */
	public void display()
	{
		if (cvVendorReportData.size() == CommonConstant.ELEMENT_0)
		{
			System.out.println(MSG_ERR_NO_RECORD);
		}
		if (cvVendorReportData.size() > 0)
		{
			// defect 8609
			System.out.println(
				STR_EPAYVENDORRPT_COMMA
					+ cvVendorReportData.size()
					+ STR_RECORDS_FOUND);
			// end defect 8609		
		}
		for (int i = 0; i < cvVendorReportData.size(); ++i)
		{
			VendorReportData laData =
				(VendorReportData) cvVendorReportData.elementAt(i);
			System.out.println(
				i
					+ CommonConstant.STR_SPACE_ONE
					+ laData.getItrntTraceNo()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ laData.getOrderId()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ laData.getTransDate()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ laData.getOrigTransDate()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ laData.getCardNo()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ laData.getAmount());
		}
	}

	/**
	 * Fill Refund Original TransDate.
	 * @deprecated
	 */
	public void fillRefundOrigTransDate()
	{

		// cvVendorReportData is the html for the capture request
		// that matches the refund, which becomes refund report lines
		// appended to the VPR payment captures
		for (int i = ciRefundStartIndex;
			i < cvVendorReportData.size();
			++i)
			// cvVendorReportRefundData is each cvVendorReportData html 
			// "line" that contains the refund data for the report  	
		{
			VendorReportData laReportData =
				(VendorReportData) cvVendorReportData.elementAt(i);
			for (int j = 0; j < cvVendorReportRefundData.size(); ++j)
			{
				VendorReportData laRefundData =
					(
						VendorReportData) cvVendorReportRefundData
							.elementAt(
						j);
				String lsRefundTraceNo =
					getRefundTraceNo(laRefundData.getItrntTraceNo());
				if (lsRefundTraceNo
					.equalsIgnoreCase(laReportData.getItrntTraceNo()))
				{
					laReportData.setOrigTransDate(
						laRefundData.getTransDate());
					// defect 8609
					// Put the "R" back at the end of the trace num for the report
					laReportData.setItrntTraceNo(
						laReportData.getItrntTraceNo().substring(
							0,
							laReportData.getItrntTraceNo().length()
								- 1)
							+ STR_CODE_REFUND);
					// end defect 8609	
					// resolved for this one, no need to go for the next
					break;
				}

			}
		}
	}
	/**
	 * Fill Original Trans Date
	 * 
	 * @param asTransHistoryHtml String
	 * 
	 */
	public void fillRefundOrigTransDate(String asTransHistoryHtml)
	{
		// ciRefundStartIndex is the report line where this refund is
		VendorReportData laReportData =
			(VendorReportData) cvVendorReportData.elementAt(
				ciRefundStartIndex);

		if (asTransHistoryHtml.indexOf("CCCAP") > -1)
		{
			// get the orig trans date 	
			int liStartofTDCaptureCodeHTML =
				asTransHistoryHtml.indexOf("CCCAP") - 5;
			int liOrigTransDateStart =
				asTransHistoryHtml.substring(
					0,
					liStartofTDCaptureCodeHTML).lastIndexOf(
					"<td>")
					+ 4;
			String lsOrigTransDateHTML =
				asTransHistoryHtml.substring(
					liOrigTransDateStart,
					asTransHistoryHtml.length());
			int liOrigTransDateEnd = lsOrigTransDateHTML.indexOf(" ");
			String lsOrigTransDate =
				lsOrigTransDateHTML.substring(0, liOrigTransDateEnd);

			laReportData.setOrigTransDate(lsOrigTransDate);
		}
	}

	/**
	* Fill Original Trans Date and card type
	* 
	* @param asRefundTraceNo String
	* @return String - The Original Trace Number
	* @deprecated
	*/

	public void fillOrigTransDateAndCardType(String asVendorPreauthDetailsHtml)
	{
		String lsCardType = "";
		String lsOrigTransDate = "";
		VendorReportData laReportData =
			(VendorReportData) cvVendorReportData.elementAt(
				ciRefundStartIndex);
		//		for (int i = 0; i < avVendorPreauthDetailsHtml.size(); i++)
		//		{
		//			String csVendorHtml =
		//				(String) aoVendorPreauthDetailsHtml;
		if (asVendorPreauthDetailsHtml.indexOf("Card Type") > -1)
		{
			// get the orig trans date 	
			int liStartofCardTypeLabel =
				asVendorPreauthDetailsHtml.indexOf("Card Type");
			String lsHTMLFromCardTypeLabel =
				asVendorPreauthDetailsHtml.substring(
					liStartofCardTypeLabel);
			int liStartofCardType =
				lsHTMLFromCardTypeLabel.indexOf("input");
			lsCardType =
				lsHTMLFromCardTypeLabel.substring(
					liStartofCardType + 7,
					liStartofCardType + 11);
			laReportData.setCardType(lsCardType);
			// searching for REN FEE cause that string is close
			// to the original trans date
			if (lsHTMLFromCardTypeLabel.indexOf("REN FEE") > -1)
			{
				int liStartofCardFeeLabel =
					lsHTMLFromCardTypeLabel.indexOf("REN FEE");
				String lsHTMLFromRenFeeLabel =
					lsHTMLFromCardTypeLabel.substring(
						liStartofCardFeeLabel);
				int liStartofTD = lsHTMLFromRenFeeLabel.indexOf("<td>");
				String lsHTMLAfterTD =
					lsHTMLFromRenFeeLabel.substring(liStartofTD + 4);
				int liStartofNextTD = lsHTMLAfterTD.indexOf("<td>");
				lsOrigTransDate =
					lsHTMLAfterTD.substring(
						liStartofNextTD + 4,
						liStartofNextTD + 23);
				laReportData.setOrigTransDate(lsOrigTransDate);
			}
		}
		//	  }
	}
	/**
	 * Fill Card type
	 * 
	 * @param asRefundTraceNo String
	 * @return String - The Original Trace Number
	 */
	public String fillCardType(String lsVendorRefundDetailsHtml)
	{
		String lsCardType = "";
		// defect 9907
		//		String lsOrigTransDate = "";
		// end defect 9907
		VendorReportData laReportData =
			(VendorReportData) cvVendorReportData.elementAt(
				ciRefundStartIndex);

		// defect 9907 - change to asVendorPreauthDetailsHtml to 
		// lvVendorRefundDetailsHtml
		//		if (asVendorPreauthDetailsHtml.indexOf("Card Type") > -1)
		//		{
		//			// get the orig trans date 	
		//			int liStartofCardTypeLabel =
		//			asVendorPreauthDetailsHtml.indexOf("Card Type");
		//			String lsHTMLFromCardTypeLabel =
		//			asVendorPreauthDetailsHtml.substring(liStartofCardTypeLabel);
		if (lsVendorRefundDetailsHtml.indexOf("Card Type") > -1)
		{
			// get the card type 	
			int liStartofCardTypeLabel =
				lsVendorRefundDetailsHtml.indexOf("Card Type");
			String lsHTMLFromCardTypeLabel =
				lsVendorRefundDetailsHtml.substring(
					liStartofCardTypeLabel);
			// end defect 9907	
			int liStartofCardType =
				lsHTMLFromCardTypeLabel.indexOf("input");

			lsCardType =
				lsHTMLFromCardTypeLabel.substring(
					liStartofCardType + 7,
					liStartofCardType + 11);
			laReportData.setCardType(lsCardType);

			// defect 10023
			// None of this is used  	
			// searching for REN FEE cause that string is close
			// to the original trans date
			//if (lsHTMLFromCardTypeLabel.indexOf("REN FEE") > -1)
			//{
			//	int liStartofCardFeeLabel =
			//		lsHTMLFromCardTypeLabel.indexOf("REN FEE");
			//String lsHTMLFromRenFeeLabel =
			//	lsHTMLFromCardTypeLabel.substring(
			//		liStartofCardFeeLabel);
			//int liStartofTD =
			//	lsHTMLFromRenFeeLabel.indexOf("<td>");						
			//String lsHTMLAfterTD =
			//	lsHTMLFromRenFeeLabel.substring(
			//	   liStartofTD + 4);		
			//int liStartofNextTD =
			//	lsHTMLAfterTD.indexOf("<td>");
			// defect 9907
			// OrigTransDate not determined here		
			//				lsOrigTransDate =
			//					lsHTMLAfterTD.substring(
			//					liStartofNextTD + 4,
			//					liStartofNextTD + 23);
			//				laReportData.setOrigTransDate(lsOrigTransDate);
			// end defect 9907		
			//}
			// end defect 10023  
		}
		String lsTransHistoryURL = "";
		if (lsVendorRefundDetailsHtml
			.indexOf("/customerservice/TransHistory")
			> -1)
		{
			int liStartofHref =
				lsVendorRefundDetailsHtml.indexOf(
					"/customerservice/TransHistory");
			String lsHrefHtmlStart =
				lsVendorRefundDetailsHtml.substring(liStartofHref);
			int liRightBracket = lsHrefHtmlStart.indexOf(">");
			lsTransHistoryURL =
				lsHrefHtmlStart.substring(0, liRightBracket - 1);
		}
		return lsTransHistoryURL;
	}

	//	/**
	//	 * Get Original Trace Number.
	//	 * 
	//	 * @param asRefundTraceNo String
	//	 * @return String - The Original Trace Number
	//	 * @deprecated
	//	 */
	//	private static String getOrigTraceNo(String asRefundTraceNo)
	//	{
	//		return asRefundTraceNo.substring(
	//			0,
	//			asRefundTraceNo.length() - 1);
	//			// defect 9878
	//			// + STR_CODE_CAPTURE;
	//			// end defect 9878
	//	}

	/**
	 * (static) Get Refund Trace Number.
	 * 
	 * @param asTraceNo String
	 * @return String - The Refund Trace Number
	 */
	private static String getRefundTraceNo(String asTraceNo)
	{
		// defect 9878
		// defect 8609
		return asTraceNo.substring(0, asTraceNo.length() - 1)
			+ STR_CODE_REFUND;
		// return asTraceNo.substring(0, asTraceNo.length() - 1) + ASTERISK;
		// end defect 8609
		// end defect 9878
	}

	/**
	 * Get Refund Trace Number.
	 * 
	 * @return Vector
	 */
	public Vector getRefundTraceNos()
	{
		Vector lvRefundTraceNum = new Vector();
		for (int i = 0; i < cvVendorReportData.size(); ++i)
		{
			VendorReportData laData =
				(VendorReportData) cvVendorReportData.elementAt(i);
			if (laData.getAmount() < 0)
			{
				String lsRefundTraceNo = laData.getItrntTraceNo();
				// defect 9878
				// defect 8609
				//String lsOrigTraceNo = getOrigTraceNo(lsRefundTraceNo);
				//lvRefundTraceNum.add(lsOrigTraceNo); 
				lvRefundTraceNum.add(lsRefundTraceNo);
				// end defect 8609
				// end defect 9878

				//				set the start index if not set.
				if (ciRefundStartIndex == -1)
				{
					ciRefundStartIndex = i;
				}
			}
		}
		return lvRefundTraceNum;
	}

	/**
	 * Get Vendor Report.
	 *
	 * @param asVendorId String
	 * @param asFromDate String
	 * @param asToDate String
	 * @return Vector
	 * @throws Exception
	 */
	public Vector getVendorReport(
		String asVendorId,
		String asFromDate,
		String asToDate)
		throws Exception
	{
		Log.fine(MSG_GETVENDORREPORT_BEGIN);

		// (1) init fetcher
		EpayVendorReportFetcher laFetcher =
			new EpayVendorReportFetcher();
		laFetcher.setVendorId(asVendorId);
		laFetcher.setDateRange(asFromDate, asToDate);
		// defect 8368
		//laFetcher.setUseProxy(true);
		laFetcher.setUseProxy(
			CommunicationProperty.getVendorPayRptUseProxy());
		// end defect 8368

		// (2) fetch the capture and refund data for the time range	
		// adds a page with the capture setting
		laFetcher.addPage();
		// adds a page with the refund setting
		// defect 8609
		laFetcher.setPaymentType(STR_REFUND_REQUEST);

		laFetcher.addPage();
		// get the html result, sequence as in the 
		// addPage --> capture then refund. 
		Vector lvData = laFetcher.fetch();
		Log.finer(MSG_GETVENDORREPORT_FETCHED);

		// (3) parse the html result
		EpayVendorReportParser laParser = new EpayVendorReportParser();
		// defect 8293 - Send the request URLs from fetcher to parser
		laParser.setRequestURLs(laFetcher.getRequestURLs());
		// end defect 8293
		// Bob's change - to central time defect 6310
		// Defect6358 - Undo 6310 - Changed back to Eastern time 
		// - global payments is Eastern


		// defect 10023 
		// Use ReportConstant 
		laParser.setDateInfo(asFromDate, asToDate,
		//RegRenProcessingConstants.VENDOR_RPT_CUTOFF_TIME);
		ReportConstant.VENDOR_RPT_CUTOFF_TIME);
		// end defect 10023  

		// parser.setDateInfo(asFromDate, asToDate, 
		// RegRenProcessingConstants.ITRANS_RPT_CUTOFF_TIME);	
		// adds the fetched data, 1st=Capture, 2nd=Refund
		laParser.addData(
			new VendorReportHtmlData(
				EpayVendorReportParser.CAPTURE,
				(String) lvData.elementAt(0)));
		laParser.addData(
			new VendorReportHtmlData(
				EpayVendorReportParser.REFUND,
				(String) lvData.elementAt(1)));
		cvVendorReportData = laParser.parse();
		Log.finer(MSG_GETVENDORREPORT_PARSED);

		// (4) the trace number list for refunds   	
		Vector lvTraceNum = getRefundTraceNos();
		if (lvTraceNum.size() > 0)
		{
			// (5) fetch the corresponding capture transactions for the
			// refunds
			// defect 9878
			// since the capture trace numbers are no longer suffixed by 
			// "A", we must get the preauth trace number, then get 
			// the original capture date/time, and card type from the 
			// pre-auth details	
			laFetcher.setFetchMode(EpayVendorReportFetcher.TRACE_NUM);
			// defect 8609
			// defect 9878
			laFetcher.setPaymentType(STR_REFUND_REQUEST);
			// end defect 9878
			// end defect 8609

			laFetcher.addTraceNum(lvTraceNum);
			Vector lvVendorReportRefundHtml = laFetcher.fetch();
			Log.finer(MSG_GETVENDORREPORT_REFUND_FETCHED);

			// (6) parse the refund capture transactions

			laParser.clearData();
			laParser.setFilterMode(EpayVendorReportParser.NO_FILTER);

			laParser.addData(
				EpayVendorReportParser.CAPTURE,
				lvVendorReportRefundHtml);
			cvVendorReportRefundData = laParser.parse();
			Log.finer(MSG_GETVENDORREPORT_REFUND_PARSED);

			// (7) fill the refund transactions' original transaction
			// date.
			// fillRefundOrigTransDate();
			// Log.finer(MSG_GETVENDORREPORT_REFUND_ORIG_DATE);

			// // get the Href for each refund for the details fetch 
			for (int i = 0; i < lvVendorReportRefundHtml.size(); i++)
			{
				// loop through each preauth trace number html
				laFetcher.csVendorReportPageFull =
					laParser.parseHref(i);
				laFetcher.setFetchMode(
					EpayVendorReportFetcher.DISPLAY_DETAILS);
				laFetcher.cvPages.add(laFetcher.csVendorReportPageFull);
				// defect 9907
				//Vector lvVendorPreauthDetailsHtml = laFetcher.fetch();
				Vector lvVendorRefundDetailsHtml = laFetcher.fetch();

				//	Log.finer(MSG_GETVENDORREPORT_REFUND_PARSED); 
				//	fillOrigTransDateAndCardType(
				//		(String) lvVendorRefundDetailsHtml.get(i));
				laFetcher.csVendorReportPageFull =
					fillCardType(
						(String) lvVendorRefundDetailsHtml.get(0));
				//						(String) lvVendorRefundDetailsHtml.get(i));
				laFetcher.setFetchMode(
					EpayVendorReportFetcher.TRANS_HISTORY);
				laFetcher.cvPages.add(laFetcher.csVendorReportPageFull);
				//Vector lvVendorPreauthDetailsHtml = laFetcher.fetch();
				Vector lvTransHistoryHtml = laFetcher.fetch();
				//				Log.finer(MSG_GETVENDORREPORT_REFUND_PARSED); 
				fillRefundOrigTransDate(
					(String) lvTransHistoryHtml.get(0));
				// end defect 9907		
				ciRefundStartIndex = ciRefundStartIndex + 1;
			}
			// end defect 9878
			Log.finer(MSG_GETVENDORREPORT_REFUND_ORIG_DATE);
		}

		// (8) done
		Log.fine(MSG_GETVENDORREPORT_EXIT);
		return cvVendorReportData;
	}

	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		String lsVendorId = "2246";
		String lsFromDate = "20030520";
		String lsToDate = "20030521";
		try
		{
			EpayVendorReport laReport = new EpayVendorReport();
			laReport.getVendorReport(lsVendorId, lsFromDate, lsToDate);
			laReport.display();
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
}
