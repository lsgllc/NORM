package com.txdot.isd.rts.services.reports.funds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.db.Substation;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenSubStationSummaryReports.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/13/2001	New class.  will copy from the other classes
 *							as much as possible.
 * Ray Rowehl	01/04/2002	Correct errors shown by testing.
 *							will get formal documentation later.
 * M Rajangam	02/??/2002	Move data classes to services.data
 * Ray Rowehl	02/21/2002	Improve page breaks.
 *							Improve money column headers.
 *							Print "TOTAL" for non-Cash Drawer Stuff.
 *							Print double dash lines before Report Totals.
 *							add handling to turn Qty printing off and on.
 *							Other small formatting changes.
 *							defect 1958
 * Ray Rowehl	02/22/2002	Reflow columns in money sections to allow 
 *							description to show more completely.  This  
 *							is eeded because the Internet columns were 
 *							added.
 *							Add method to generate the no trans msg.
 *							defect 1964
 * Ray Rowehl	02/26/2002	Print qty in payment section for payments  
 *							that re not cash
 *							defect 2265
 * Ray Rowehl	03/07/2002	The first column of Payment Report should be 
 *							Payment Type. not description..
 *							Go ahead and reflow header code to fit in 72 
 *							columns.
 *							defect 1964
 * Ray Rowehl	03/14/2002	Re-fix Payment Type Column Header
 *							defect 2755)
 * Ray Rowehl	03/20/2002	Work on paging problem with fees.
 *							Also work on Payment. If print line is below
 *							a certain level, create a new page.  This is
 *							not	needed for Inventory because there is no  
 *							total printing. 
 *							defect 3066
 * Ray Rowehl	04/17/2002	reflow alignment for payment and fees 
 *							sections.
 *							defect 3490
 * Ray Rowehl	04/26/2002	Move headers a little more
 *							defect 3490
 * Ray Rowehl	05/10/2002	Need to show non-cash even if there is no 
 *							cash data on payment report.
 *							defect 3852
 * Ray Rowehl	05/29/2002	Add more checks for the need to page break.
 *							There was some problem with running out of 
 *							space.
 *							defect 4092
 * B Arredondo	02/04/2003	Increased the length of the description field
 *							so non-resident outstate permit would fit.
 *							defect 5191 
 * E LyBrand	06/25/2003	Increased field length from 25 to 29 for 
 *							item description. 
 *							modify genInvSection().
 *							defect 6067. 
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add REPRINTED_TBL_HEADER, 
 *							REPRINTED_START_POSITION,
 *							REPRINTED_HEADER_START_POSITION
 *							modify createInvTableHeaders(),
 *							genInvSection()
 * 							Ver 5.2.0
 * K Harrell	10/27/2004 	Modify to use FundsData.getWorkstationId()
 *							for report header
 *							modify formatReport(FundsData)
 *							defect 7681 Ver 5.2.2
 * K Harrell	03/21/2005	Modifications for smaller units of work.
 *							plus Standardization Work throughout class
 *							Most variables to private
 *							New constants 
 *							defect 7077 Ver 5.2.2 Fix 3 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3 
 * K Harrell	08/17/2009	Implement new generateFooter(boolean), 
 * 							 ReportsServerBusiness().initReportProperties()
 * 							modify formatReport(), generateExceptionPage() 
 * 							defect 8628 Ver Defect_POS_F     
 * ---------------------------------------------------------------------
 */
/**
 * The Substation Summary is made up of three sections.  
 * They are the Payment Section, Fees Section, and Inventory Summary 
 * Section.  Each of these are esentially the same as the stand alone 
 * Funds reports.  However, the more complicated components required 
 * to generate the full range of options on these reports will not 
 * exist here.
 *
 * @version  Defect_POS_F  	08/17/2009
 * @author	Ray Rowehl
 * <br>Creation Date:		12/13/2001 07:35:39  
 */
public class GenSubStationSummaryReports extends ReportTemplate
{
	public final static String REPORT_DATE_STRING = "REPORT DATE";

	private final static Dollar ZERODOLLAR = new Dollar(0.00);
	private final static String REPORT_TOTAL_STRING = "REPORT TOTAL";
	private final static String COLON_STRING = ":";
	private final static String ONE_SPACE_STRING = " ";
	private final static String CASH_DRAWER_OPER_STRING =
		"   CASH DRAWER OPERATIONS:";
	private final static String NON_CASH_DRAWER_OPER_STRING =
		"   NON-CASH DRAWER OPERATIONS:";
	private final static String TOTAL_STRING = "TOTAL";
	// Generic Lengths
	private static final int LENGTH_04 = 4;
	private final static int LENGTH_05 = 5;
	private final static int LENGTH_06 = 6;
	private final static int LENGTH_13 = 13;
	private final static int LENGTH_20 = 20;
	private final static int LENGTH_27 = 28;
	private final static int LENGTH_29 = 29;
	// Description positioning
	private final static String DESC_TBL_HEADER2 = "DESCRIPTION";
	private final static int START_PT_1 = 1;
	// Payment first column description
	private final static String PAYMENT_TYPE_HEADER1_STRING = "PAYMENT";
	private final static String PAYMENT_TYPE_HEADER2_STRING = "TYPE";
	private final static int PAYMENT_TYPE_HEADER_LENGTH = 7;
	// Customer Information positioning
	private final static String CUST_TBL_HEADER1 = "CUSTOMER";
	private final static int CUST_START_PT = 36;
	private final static int CUST_LENGTH = 8;
	private final static int DETAIL_CUST_AMT_START = 29;
	private final static int DETAIL_CUST_QTY_START = 43;
	// Subcon Information positioning
	private final static String SUBCON_TBL_HEADER1 = "SUBCONTRACTOR";
	private final static int SUBCON_START_PT = 56; // CQU3490 was 57
	private final static int SUBCON_LENGTH = 13;
	private final static int DETAIL_SUBCON_AMT_START = 50;
	private final static int DETAIL_SUBCON_QTY_START = 64;
	// Dealer Title Header positioning
	private final static String DEALER_TBL_HEADER1 = "DEALER TITLE";
	private final static int DEALER_START_PT = 77; // CQU3490 was 79
	private final static int DEALER_LENGTH = 12;
	private final static int DETAIL_DEALER_AMT_START = 71;
	private final static int DETAIL_DEALER_QTY_START = 85;
	// Internet Header positioning
	private final static String INTNET_TBL_HEADER1 = "INTERNET";
	private final static int INTNET_START_PT = 97; // CQU3490 was 99
	// (CQU100001958)
	private final static int INTNET_LENGTH = 8;
	private final static int DETAIL_INTNET_AMT_START = 92;
	private final static int DETAIL_INTNET_QTY_START = 106;
	// Total Header positioning
	private final static String TOTAL_TBL_HEADER1 = "TOTAL";
	private final static int TOTAL_START_PT = 120;
	private final static int TOTAL_LENGTH = 5;
	private final static int DETAIL_TOTAL_AMT_START = 113;
	private final static int DETAIL_TOTAL_QTY_START = 127;
	// Amount Qty part of header information	// (CQU100001958)
	private final static String AMT_QTY_TBL_HEADER2 = "AMOUNT   QTY";
	private final static int AMT_QTY_START_PT_CUST = 36;
	private final static int AMT_QTY_START_PT_SUBCON = 56;
	// CQU3490 was 57
	private final static int AMT_QTY_START_PT_DEALER = 77;
	// CQU3490 was 79
	private final static int AMT_QTY_START_PT_INTNET = 97;
	// CQU3490 was 99	
	private final static int AMT_QTY_START_PT_TOTAL = 120;
	private final static int AMT_QTY_LENGTH = 13;
	// Inventory Header Stuff
	private final static String SOLD_TBL_HEADER = "SOLD";
	private final static String VOIDED_TBL_HEADER = "VOIDED";
	private final static String REUSED_TBL_HEADER = "REUSED";
	// PCR 34
	private final static String REPRINTED_TBL_HEADER = "REPRINTED";
	private final static int REPRINTED_START_POSITION = 87;
	// End PCR 34
	private final static int REUSED_START_POSITION = 75;
	// (CQU100001958)
	private final static int VOIDED_START_POSITION = 63;
	// (CQU100001958)
	private final static int SOLD_START_POSITION = 50;
	private final static int ITEMYEAR_START_POSITION = 40;
	private final static int ITEMCODE_START_POSITION = 10;
	private final static int ITEMDESC_START_POSITION2 = 16;
	private final static int ITEMDESC_START_POSITION1 =
		ITEMDESC_START_POSITION2 + 3;
	private final static String QUANTITY_TBL_HEADER = "QUANTITY";
	private final static String ITEM_TBL_HEADER = "ITEM";
	private final static String YEAR_TBL_HEADER = "YEAR";
	// No trans to process message
	private final static String NO_TRANS_MSG_STRING =
		"THERE ARE NO TRANSACTIONS TO PROCESS";
	// Constants for use in line spacing
	private final static int LINES_1 = 1;
	private final static int LINES_2 = 2;
	// number of lines available to print per page.
	// includes header lines
	//private int ciDetailLinesPerPage = 0;
	// declare the white space to leave at the bottom of a report
	private final static int BUFFER_SPACE_DETAIL = 3;
	//	(CQU100002213)
	private final static int BUFFER_SPACE_HEADER = 7;
	// 	(CQU100002213)
	private final static int END_OF_PAGE_WHITE_SPACE = 3;
	// defect 7077 
	// Constants to be used in case Statements
	private final static int CUSTOMER = 1;
	private final static int SUBCONTRACTOR = 2;
	private final static int DEALER = 3;
	private final static int INTERNET = 5;
	private final static int NOSOURCE = 0;
	private final static int SUBTOTAL = 1;
	private final static int FINALTOTAL = 2;

	/**
	 * GenSubStationSummaryReports constructor
	 */
	public GenSubStationSummaryReports()
	{
		super();
	}

	/**
	 * GenSubStationSummaryReports constructor 
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenSubStationSummaryReports(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * Data is brought in from the database query result vector and 
	 * then built into a TreeMap.  The database query result vector
	 * is of FeeSummaryReportData.  This data class does not properly 
	 * organize the data.  This method organizes the data into 
	 * CountyWideReportMoneyData so that it is better organized for 
	 * generating the report.
	 * 
	 * @param avResults Vector - Data results in
	 * @param aaDataMap TreeMap - Starting TreeMap to be added to.
	 * @return TreeMap	
	 */
	public TreeMap buildFeesData(Vector avResults, TreeMap aaDataMap)
	{
		// process through the input vector
		for (int i = 0; i < avResults.size(); i++)
		{
			FeeSummaryReportData laDataLineIn =
				(FeeSummaryReportData) avResults.elementAt(i);
			// declare the key to be used for containkeys, gets, puts, 
			// and removes.
			String lsMapKey = "";
			/**
			 * The key is built as follows:
			 * <li>PayableTypeCd is the major sort key.
			 * <li>PayableTypeCdDesc supplements PayableTypeCd. 
			 * <li>AcctItmGrpCd will control partial order of AcctItmCdDesc's.
			 * <li>AcctItmCrdtIndi will provide a partial order of AcctItmCdDesc's
			 * <li>AcctItmCdDesc is the final part of the key.
			 * </ol> 
			 */
			lsMapKey =
				laDataLineIn.getPayableTypeCd()
					+ laDataLineIn.getPayableTypeCdDesc()
					+ laDataLineIn.getAcctItmGrpCd()
					+ laDataLineIn.getAcctItmCrdtIndi()
					+ laDataLineIn.getAcctItmCdDesc();
			/** 
			 * Check to see a record line has already been started for 
			 * this key combination. If so, update it.  
			 * Otherwise, insert it.
			 * ContainsKey returns a boolean.  
			 * True if it is there, null(?) if not.
			 */
			if (aaDataMap.containsKey(lsMapKey))
			{
				/**
				 * We found a matching record on the TreeMap.
				 * Update with the new data.
				 * Update is accomplished by reading in the data, 
				 * updating the object, removing the object from the 
				 * TreeMap, and then writting the record back out.
				 */
				CountyWideReportFeeData laDataLineOut =
					(CountyWideReportFeeData) aaDataMap.get(lsMapKey);
				// Remove the current record from the map.
				aaDataMap.remove(lsMapKey);
				/**
				 * Switch on FeeSourceCd.  This is how we determine 
				 * which group the data belongs to.
				 */
				switch (laDataLineIn.getFeeSourceCd())
				{
					/**
					 * Case 1 means this is Customer data.
					 */
					case (CUSTOMER) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laDataLineOut.getCustAcctItmAmt().add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setCustAcctItmQty(
								laDataLineOut.getCustAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * Case 2 means this is Subcontractor data.
						 */
					case (SUBCONTRACTOR) :
						{
							laDataLineOut.setSubconAcctItmAmt(
								laDataLineOut
									.getSubconAcctItmAmt()
									.add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setSubconAcctItmQty(
								laDataLineOut.getSubconAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * Case 3 means this is Dealer data.
						 */
					case (DEALER) :
						{
							laDataLineOut.setDealerAcctItmAmt(
								laDataLineOut
									.getDealerAcctItmAmt()
									.add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setDealerAcctItmQty(
								laDataLineOut.getDealerAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * Case 5 means this is Internet data.
						 */
					case (INTERNET) :
						{
							laDataLineOut.setInternetAcctItmAmt(
								laDataLineOut
									.getInternetAcctItmAmt()
									.add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setInternetAcctItmQty(
								laDataLineOut.getInternetAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							break;
						}
					case (NOSOURCE) : //Case 0 means that ShowSourceOfMoney = false
						{
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn.getTotalAcctItmAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * There should not be any other choice, but allow for the fact that
						 * there are other FeeSourceCd's.
						 */
					default :
						{
							// empty code block
						}
				} // end switch
				aaDataMap.put(lsMapKey, laDataLineOut);
			} // end if
			/**
			 * There is not an existing record.
			 * Create and add it to the TreeMap.
			 */
			else
			{
				CountyWideReportFeeData laDataLineOut =
					new CountyWideReportFeeData();
				laDataLineOut.setPayableTypeCd(
					laDataLineIn.getPayableTypeCd());
				laDataLineOut.setPayableTypeCdDesc(
					laDataLineIn.getPayableTypeCdDesc());
				laDataLineOut.setAcctItmCdDesc(
					laDataLineIn.getAcctItmCdDesc());
				laDataLineOut.setAcctItmCrdtIndi(
					laDataLineIn.getAcctItmCrdtIndi());
				laDataLineOut.setAcctItmGrpCd(
					laDataLineIn.getAcctItmGrpCd());
				laDataLineOut.setFeeSourceCd(
					laDataLineIn.getFeeSourceCd());
				/**
				 * Switch on FeeSourceCd.  This is how we determine 
				 * which group the data belongs to.
				 */
				switch (laDataLineIn.getFeeSourceCd())
				{
					/**
					 * Case 1 means this is Customer data.
					 */
					case (CUSTOMER) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setCustAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * Case 2 means this is Subcontractor data.
						 */
					case (SUBCONTRACTOR) :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setSubconAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * Case 3 means this is Dealer data.
						 */
					case (DEALER) :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setDealerAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * Case 5 means this is Internet.
						 */
					case (INTERNET) :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setInternetAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							break;
						}
						/**
						 * This should not happen.  But, there are other FeeSourceCd's.
						 * This allows for that possibility.
						 */
					case (NOSOURCE) : //Case 0 means that ShowSourceOfMoney = false
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalAcctItmAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getTotalAcctItmQty());
							break;
						}
					default :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setTotalAcctItmQty(0);
						}
				} // end switch
				aaDataMap.put(lsMapKey, laDataLineOut);
			} // end else
		} // end while loop
		return aaDataMap;
	}

	/**
	 * Data is brought in from the database query result vector and 
	 * then built into a TreeMap.  The database query result vector
	 * is of PaymentSummaryReportData.  This data class does not 
	 * properly organize the data.  This method organizes the data into 
	 * CountyWideReportPaymentData so that it is better organized for 
	 * generating the report.
	 * 
	 * @param avResults Vector - Data results in
	 * @param aaDataMap TreeMap - Starting TreeMap to be added to.
	 * @return TreeMap
	 */
	public TreeMap buildPaymentDataCash(
		Vector avResults,
		TreeMap aaDataMap)
	{
		// declare the key to be used for containkeys, gets, puts, and removes.
		String lsMapKey = "";
		// process through the input vector
		for (int i = 0; i < avResults.size(); i++)
		{
			PaymentSummaryReportData laDataLineIn =
				(PaymentSummaryReportData) avResults.elementAt(i);
			/**
			 * The key is built as follows:
			 * <ol>
			 * <li>PymntTypeCdDesc is the key. 
			 * </ol> 
			 */
			lsMapKey = laDataLineIn.getPymntTypeCdDesc();
			/** 
			 * Check to see a record line has already been started for this key combination.
			 * If so, update it.  Otherwise, insert it.
			 * ContainsKey returns a boolean.  True if it is there, null(?) if not.
			 */
			if (aaDataMap.containsKey(lsMapKey))
			{
				/**
				 * We found a matching record on the TreeMap.
				 * Update with the new data.
				 * Update is accomplished by reading in the data, updating the object, removing the
				 * object from the TreeMap, and then writting the record back out.
				 */
				CountyWideReportPaymentData laDataLineOut =
					(CountyWideReportPaymentData) aaDataMap.get(
						lsMapKey);
				// Remove the current record from the map.
				aaDataMap.remove(lsMapKey);
				/**
				 * Switch on FeeSourceCd.  This is how we determine which group
				 * the data belongs to.
				 */
				switch (laDataLineIn.getFeeSourceCd())
				{
					/**
					 * Case 1 means this is Customer data.
					 */
					case (CUSTOMER) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laDataLineOut.getCustAcctItmAmt().add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setCustAcctItmQty(
								laDataLineOut.getCustAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * Case 2 means this is Subcontractor data.
						 */
					case (SUBCONTRACTOR) :
						{
							laDataLineOut.setSubconAcctItmAmt(
								laDataLineOut
									.getSubconAcctItmAmt()
									.add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setSubconAcctItmQty(
								laDataLineOut.getSubconAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * Case 3 means this is Dealer data.
						 */
					case (DEALER) :
						{
							laDataLineOut.setDealerAcctItmAmt(
								laDataLineOut
									.getDealerAcctItmAmt()
									.add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setDealerAcctItmQty(
								laDataLineOut.getDealerAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * Case 5 means this is Internet data.
						 */
					case (INTERNET) :
						{
							laDataLineOut.setInternetAcctItmAmt(
								laDataLineOut
									.getInternetAcctItmAmt()
									.add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setInternetAcctItmQty(
								laDataLineOut.getInternetAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							break;
						}
					case (NOSOURCE) : //Case 0 means that ShowSourceOfMoney = false
						{
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineOut.getTotalAcctItmAmt().add(
									laDataLineIn
										.getTotalPymntTypeAmt()));
							laDataLineOut.setTotalAcctItmQty(
								laDataLineOut.getTotalAcctItmQty()
									+ laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * There should not be any other choice, but allow for the fact that
						 * there are other FeeSourceCd's.
						 */
					default :
						{
							// empty code block
						}
				} // end switch
				aaDataMap.put(lsMapKey, laDataLineOut);
			} // end if
			/**
			 * There is not an existing record.
			 * Create and add it to the TreeMap.
			 */
			else
			{
				CountyWideReportPaymentData laDataLineOut =
					new CountyWideReportPaymentData();
				laDataLineOut.setPymntTypeCd(
					laDataLineIn.getPymntTypeCd());
				laDataLineOut.setPymntTypeCdDesc(
					laDataLineIn.getPymntTypeCdDesc());
				laDataLineOut.setFeeSourceCd(
					laDataLineIn.getFeeSourceCd());
				/**
				 * Switch on FeeSourceCd.  This is how we determine which group
				 * the data belongs to.
				 */
				switch (laDataLineIn.getFeeSourceCd())
				{
					/**
					 * Case 1 means this is Customer data.
					 */
					case (CUSTOMER) :
						{
							laDataLineOut.setCustAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setCustAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * Case 2 means this is Subcontractor data.
						 */
					case (SUBCONTRACTOR) :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setSubconAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * Case 3 means this is Dealer data.
						 */
					case (DEALER) :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setDealerAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * Case 5 means this is Internet data.
						 */
					case (INTERNET) :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setInternetAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							break;
						}
						/**
						 * This should not happen.  But, there are other
						 * FeeSourceCd's.
						 * This allows for that possibility.
						 */
					case (NOSOURCE) : //Case 0 means that ShowSourceOfMoney = false
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								laDataLineIn.getTotalPymntTypeAmt());
							laDataLineOut.setTotalAcctItmQty(
								laDataLineIn.getPymntTypeQty());
							break;
						}
					default :
						{
							laDataLineOut.setCustAcctItmAmt(ZERODOLLAR);
							laDataLineOut.setCustAcctItmQty(0);
							laDataLineOut.setSubconAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setSubconAcctItmQty(0);
							laDataLineOut.setDealerAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setDealerAcctItmQty(0);
							laDataLineOut.setInternetAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setInternetAcctItmQty(0);
							laDataLineOut.setTotalAcctItmAmt(
								ZERODOLLAR);
							laDataLineOut.setTotalAcctItmQty(0);
						}
				} // end switch
				aaDataMap.put(lsMapKey, laDataLineOut);
			} // end else
		} // end while loop
		return aaDataMap;
	}

	/**
	 * Create the Fee Report Headings
	 *
	 * @return Vector
	 */
	public Vector createFeeTableHeaders()
	{
		Vector lvTable = new Vector();
		// This vector contains the first of two rows of column headings
		Vector lvRow1 = new Vector();
		ColumnHeader laColumnHdrA1 =
			new ColumnHeader(
				CUST_TBL_HEADER1,
				CUST_START_PT,
				CUST_LENGTH);
		ColumnHeader laColumnHdrA2 =
			new ColumnHeader(
				SUBCON_TBL_HEADER1,
				SUBCON_START_PT,
				SUBCON_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrA3 =
			new ColumnHeader(
				DEALER_TBL_HEADER1,
				DEALER_START_PT,
				DEALER_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrA4 =
			new ColumnHeader(
				INTNET_TBL_HEADER1,
				INTNET_START_PT,
				INTNET_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrA5 =
			new ColumnHeader(
				TOTAL_TBL_HEADER1,
				TOTAL_START_PT,
				TOTAL_LENGTH);
		lvRow1.addElement(laColumnHdrA1);
		lvRow1.addElement(laColumnHdrA2);
		lvRow1.addElement(laColumnHdrA3);
		lvRow1.addElement(laColumnHdrA4);
		lvRow1.addElement(laColumnHdrA5);
		lvTable.addElement(lvRow1); //Adding ColumnHeader Information
		// this vector contains the second of two rows of column headings 
		Vector lvRow2 = new Vector();
		ColumnHeader laColumnHdrB1 =
			new ColumnHeader(DESC_TBL_HEADER2, START_PT_1, LENGTH_20);
		ColumnHeader laColumnHdrB2 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_CUST,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrB3 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_SUBCON,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrB4 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_DEALER,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrB5 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_INTNET,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrB6 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_TOTAL,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		lvRow2.addElement(laColumnHdrB1);
		lvRow2.addElement(laColumnHdrB2);
		lvRow2.addElement(laColumnHdrB3);
		lvRow2.addElement(laColumnHdrB4);
		lvRow2.addElement(laColumnHdrB5);
		lvRow2.addElement(laColumnHdrB6);
		lvTable.addElement(lvRow2); //Adding ColumnHeader Information
		return lvTable;
	}

	/**
	 * Create the Report Header.
	 * 
	 * @param aaFundsData FundsData - Contains the object containing 
	 *  format info
	 * @return Vector
	 */
	public Vector createHeader(FundsData aaFundsData)
	{
		Vector lvHeader = new Vector();
		// this adds the batch run date to the header	
		lvHeader.addElement(REPORT_DATE_STRING);
		lvHeader.addElement(aaFundsData.getSummaryEffDate().toString());
		return lvHeader;
	}

	/**
	 * Create the Inventory Report Headings
	 * 
	 * @return Vector
	 */
	public Vector createInvTableHeaders()
	{
		Vector lvTable = new Vector();
		// This vector contains the first of two rows of column headings
		Vector lvRow1 = new Vector();
		ColumnHeader laColumnHdrA1 =
			new ColumnHeader(
				ITEM_TBL_HEADER,
				ITEMDESC_START_POSITION1,
				LENGTH_20);
		ColumnHeader laColumnHdrA2 =
			new ColumnHeader(
				ITEM_TBL_HEADER,
				ITEMYEAR_START_POSITION,
				LENGTH_04);
		ColumnHeader laColumnHdrA3 =
			new ColumnHeader(
				QUANTITY_TBL_HEADER,
				SOLD_START_POSITION,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrA4 =
			new ColumnHeader(
				QUANTITY_TBL_HEADER,
				VOIDED_START_POSITION,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrA5 =
			new ColumnHeader(
				QUANTITY_TBL_HEADER,
				REUSED_START_POSITION,
				AMT_QTY_LENGTH);
		// PCR 34										
		ColumnHeader laColumnHdrA6 =
			new ColumnHeader(
				QUANTITY_TBL_HEADER,
				REPRINTED_START_POSITION,
				AMT_QTY_LENGTH);
		// End PCR 34
		lvRow1.addElement(laColumnHdrA1);
		lvRow1.addElement(laColumnHdrA2);
		lvRow1.addElement(laColumnHdrA3);
		lvRow1.addElement(laColumnHdrA4);
		lvRow1.addElement(laColumnHdrA5);
		// PCR 34
		lvRow1.addElement(laColumnHdrA6);
		// End PCR 34
		lvTable.addElement(lvRow1); //Adding ColumnHeader Information
		// this vector contains the second of two rows of column headings 
		Vector lvRow2 = new Vector();
		ColumnHeader laColumnHdrB1 =
			new ColumnHeader(
				DESC_TBL_HEADER2,
				ITEMDESC_START_POSITION2,
				LENGTH_20);
		ColumnHeader laColumnHdrB2 =
			new ColumnHeader(
				YEAR_TBL_HEADER,
				ITEMYEAR_START_POSITION,
				LENGTH_04);
		ColumnHeader laColumnHdrB3 =
			new ColumnHeader(
				SOLD_TBL_HEADER,
				SOLD_START_POSITION,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrB4 =
			new ColumnHeader(
				VOIDED_TBL_HEADER,
				VOIDED_START_POSITION,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrB5 =
			new ColumnHeader(
				REUSED_TBL_HEADER,
				REUSED_START_POSITION,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrB6 =
			new ColumnHeader(
				REPRINTED_TBL_HEADER,
				REPRINTED_START_POSITION,
				AMT_QTY_LENGTH);
		lvRow2.addElement(laColumnHdrB1);
		lvRow2.addElement(laColumnHdrB2);
		lvRow2.addElement(laColumnHdrB3);
		lvRow2.addElement(laColumnHdrB4);
		lvRow2.addElement(laColumnHdrB5);
		// PCR 34
		lvRow2.addElement(laColumnHdrB6);
		// End PCR 34
		lvTable.addElement(lvRow2); //Adding ColumnHeader Information
		return lvTable;
	}

	/**
	 * Create the Payment Report Headings
	 *
	 * @return Vector
	 */
	public Vector createPaymentTableHeaders()
	{
		Vector lvTable = new Vector();
		// This vector contains the first of two rows of column headings
		Vector lvRow1 = new Vector();
		ColumnHeader laColumnHdrA1 =
			new ColumnHeader(
				PAYMENT_TYPE_HEADER1_STRING,
				START_PT_1,
				PAYMENT_TYPE_HEADER_LENGTH);
		ColumnHeader laColumnHdrA2 =
			new ColumnHeader(
				CUST_TBL_HEADER1,
				CUST_START_PT,
				CUST_LENGTH);
		ColumnHeader laColumnHdrA3 =
			new ColumnHeader(
				SUBCON_TBL_HEADER1,
				SUBCON_START_PT,
				SUBCON_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrA4 =
			new ColumnHeader(
				DEALER_TBL_HEADER1,
				DEALER_START_PT,
				DEALER_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrA5 =
			new ColumnHeader(
				INTNET_TBL_HEADER1,
				INTNET_START_PT,
				INTNET_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrA6 =
			new ColumnHeader(
				TOTAL_TBL_HEADER1,
				TOTAL_START_PT,
				TOTAL_LENGTH);
		lvRow1.addElement(laColumnHdrA1);
		lvRow1.addElement(laColumnHdrA2);
		lvRow1.addElement(laColumnHdrA3);
		lvRow1.addElement(laColumnHdrA4);
		lvRow1.addElement(laColumnHdrA5);
		lvRow1.addElement(laColumnHdrA6);
		lvTable.addElement(lvRow1); //Adding ColumnHeader Information
		// this vector contains the second of two rows of column headings 
		Vector lvRow2 = new Vector();
		ColumnHeader laColumnHdrB1 =
			new ColumnHeader(
				PAYMENT_TYPE_HEADER2_STRING,
				START_PT_1,
				PAYMENT_TYPE_HEADER_LENGTH);
		ColumnHeader laColumnHdrB2 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_CUST,
				AMT_QTY_LENGTH);
		ColumnHeader laColumnHdrB3 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_SUBCON,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrB4 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_DEALER,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrB5 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_INTNET,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		ColumnHeader laColumnHdrB6 =
			new ColumnHeader(
				AMT_QTY_TBL_HEADER2,
				AMT_QTY_START_PT_TOTAL,
				AMT_QTY_LENGTH);
		// (CQU100001958)
		lvRow2.addElement(laColumnHdrB1);
		lvRow2.addElement(laColumnHdrB2);
		lvRow2.addElement(laColumnHdrB3);
		lvRow2.addElement(laColumnHdrB4);
		lvRow2.addElement(laColumnHdrB5);
		lvRow2.addElement(laColumnHdrB6);
		lvTable.addElement(lvRow2); //Adding ColumnHeader Information
		return lvTable;
	}

	/**
	 * Print the detail line for Fees Data sections.
	 * The line can be from detail data or a total line.
	 * <p>Copied from GenFeeReport by Min Wang.
	 * <p>
	 * 
	 * @param aaDataLine CountyWideReportMoneyData
	 * @param abTotals boolean - This indicates we are processing a 
	 *  Totals line.
	 * @param abQtyShow	boolean - Determines if Qty should show
	 */
	public void feesPrintLine(
		CountyWideReportFeeData aaDataLine,
		boolean abTotals,
		boolean abQtyShow)
	{
		if (abTotals) // this is a total line.  Print the Totals Description on the left margin
		{
			caRpt.print(
				aaDataLine.getAcctItmCdDesc(),
				START_PT_1,
				LENGTH_27);
		}
		else // this is a detail line.  Print the description at the normal place
			{
			caRpt.print(
				aaDataLine.getAcctItmCdDesc(),
				START_PT_1,
				LENGTH_27);
		}
		// format and print the customer amounts on each detail line
		if (!aaDataLine.getCustAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine.getCustAcctItmAmt().printDollar().substring(
					1,
					aaDataLine
						.getCustAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_CUST_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getCustAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getCustAcctItmQty()),
				DETAIL_CUST_QTY_START,
				LENGTH_05);
		}
		// format and print the subcontractor amounts on each detail line
		if (!aaDataLine.getSubconAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getSubconAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getSubconAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_SUBCON_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getSubconAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getSubconAcctItmQty()),
				DETAIL_SUBCON_QTY_START,
				LENGTH_05);
		}
		// format and print the dealer amounts on each detail line
		if (!aaDataLine.getDealerAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getDealerAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getDealerAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_DEALER_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getDealerAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getDealerAcctItmQty()),
				DETAIL_DEALER_QTY_START,
				LENGTH_05);
		}
		// format and print the internet amounts on each detail line
		if (!aaDataLine.getInternetAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getInternetAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getInternetAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_INTNET_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getInternetAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getInternetAcctItmQty()),
				DETAIL_INTNET_QTY_START,
				LENGTH_05);
		}
		// format and print the overall amounts on each detail line
		if (!aaDataLine.getTotalAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getTotalAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getTotalAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_TOTAL_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getTotalAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getTotalAcctItmQty()),
				DETAIL_TOTAL_QTY_START,
				LENGTH_05);
		}
		caRpt.nextLine();
	}

	/**
	 * This processes the Totals Lines.
	 *
	 * @param aaDataLine CountyWideReportMoneyData
	 * @param aiTotalType int - This indicates we are processing a 
	 *  Totals line.
	 */
	public void feesPrintTotalLine(
		CountyWideReportFeeData aaDataLine,
		int aiTotalType)
	{
		String lsDashedLineForTotal = "";
		// 
		switch (aiTotalType)
		{
			case SUBTOTAL :
				{
					lsDashedLineForTotal =
						caRpt.singleDashes(LENGTH_13);
					break;
				}
			case FINALTOTAL :
				{
					lsDashedLineForTotal =
						caRpt.doubleDashes(LENGTH_13);
					break;
				}
			default :
				{
					lsDashedLineForTotal =
						caRpt.singleDashes(LENGTH_13);
				}
		}
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_CUST_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_SUBCON_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_DEALER_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_INTNET_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_TOTAL_AMT_START,
			LENGTH_13);
		caRpt.nextLine();
		feesPrintLine(aaDataLine, true, false);
		caRpt.nextLine();
	}

	/**
	 * This method drives the creation of the SubStation Summary Report.
	 * The Substation summary is formed by basically creating a Payment Report,
	 * a Fees Report, and an Inventory Summary Report.  However, the data is setup
	 * different from these reports as run under funds.
	 * So, we will create them seperately from the original classes.
	 * If opportunity presents itself, we can look at merging later.
	 * It is also felt that there would be too much conditional processing.
	 * Fees and Payment where designed to also be called for Substation Summary originally.
	 * Come back to this subject later.
	 *
	 * <p>The original concept has been changed completely.
	 * Now we are just changing values within the report as each section is encountered.
	 * It will look more like an ordinary report to the rest of the system.
	 * Need to verify this methodology works. 
	 *
	 * @param aaFundsData FundsData 
	 */
	public void formatReport(FundsData aaFundsData)
	{
		// defect 7077
		// Reorganize for maximum DB concurrency
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		// Instantiating GeneralSearchData  
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		laGenSearchData.setIntKey1(aaFundsData.getOfficeIssuanceNo());
		laGenSearchData.setIntKey2(aaFundsData.getSubStationId());
		laGenSearchData.setIntKey3(
			aaFundsData.getSummaryEffDate().getAMDate());

		try
		{
			CountyWideSQL laCWSQL = new CountyWideSQL(laDatabaseAccess);

			// UOW #1 BEGIN			
			ReportsServerBusiness laRptSrvrBus =
				new ReportsServerBusiness();
			caRptProps =
				laRptSrvrBus.initReportProperties(
					aaFundsData.getReportSearchData(),
					laDatabaseAccess,
					ReportConstant.SUBSTATION_PYMNT_REPORT_ID);
			// UOW #1 END 
			
			//----------------------------------------------------------------
			// This section generates the Payment portion of the report
			//
			// set up the Report Id for Payment
			caRpt.csName = ReportConstant.SUBSTATION_PYMNT_REPORT_TITLE;

			// UOW #2 BEGIN 
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsCash =
				laCWSQL.qryPaymentSubstationSummary(laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			// UOW #3 BEGIN 
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsNonCash =
				laCWSQL.qryNonCashDrawerSubstationSummary(
					laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END

			// create vector to pass in objects
			Vector lvDataOutPayment = new Vector();
			lvDataOutPayment.addElement(lvQueryResultsCash);
			lvDataOutPayment.addElement(lvQueryResultsNonCash);
			lvDataOutPayment.addElement(aaFundsData);
			genPaymentSection(lvDataOutPayment);
			//----------------------------------------------------------------
			// This section generates the Fees portion of the report
			//
			// set up the Report Id for Fees
			caRptProps.setUniqueName(
				ReportConstant.SUBSTATION_FEES_REPORT_ID);
			caRpt.csName = ReportConstant.SUBSTATION_FEES_REPORT_TITLE;

			// UOW #4 BEGIN 
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsFees =
				laCWSQL.qryFeeSummarySubstationSummary(laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #4 END

			// create vector to pass in objects
			Vector lvDataOutFees = new Vector();
			lvDataOutFees.addElement(lvQueryResultsFees);
			lvDataOutFees.addElement(aaFundsData);
			genFeesSection(lvDataOutFees);
			//----------------------------------------------------------------
			// This section generates the Inventory Summary portion of the report
			//
			caRptProps.setUniqueName(
				ReportConstant.SUBSTATION_INV_REPORT_ID);
			caRpt.csName = ReportConstant.SUBSTATION_INV_REPORT_TITLE;

			// UOW #5 BEGIN 
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsInv =
				laCWSQL.qryInventorySummarySubstationSummary(
					laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #5 END

			Vector lvDataOutInv = new Vector();
			lvDataOutInv.addElement(lvQueryResultsInv);
			lvDataOutInv.addElement(aaFundsData);
			genInvSection(lvDataOutInv);
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDatabaseAccess.endTransaction(
					DatabaseAccess.ROLLBACK);
				aeRTSEx.printStackTrace();
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx1.printStackTrace();
			}
		}
		finally
		{
			laDatabaseAccess = null;
		}
		// end defect 7077 
	}

	/**
	 * Required to be implemented, but not used in this class.
	 * take no action..
	 *
	 * @param avDummyVector Vector
	 */
	public void formatReport(Vector avDummyVector)
	{
		// empty code block
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Adds the no transactions to process message.
	 */
	public void generateNoTransMessage()
	{
		caRpt.blankLines(LINES_2);
		caRpt.println(NO_TRANS_MSG_STRING);
	}

	/**
	 * This method generates the Fees Report part of Substation Summary 
	 *  
	 * @param Vector lvDataIn - Vector containing results set from 
	 *  query and fundsdata
	 * @throws RTSException 
	 */
	public void genFeesSection(Vector lvDataIn) throws RTSException
	{
		// instantiate the FundsData object to be populated in the for loop
		FundsData laFundsData = null;
		// instantiate the Vector for the resultset to be populated in the for loop
		Vector lvResults = null;
		// get the objects off of the lvDataIn vector
		for (int i = 0; i < lvDataIn.size(); i++)
		{
			Object laObject = lvDataIn.elementAt(i);
			// cast laObject into lvResults
			if (laObject instanceof Vector)
			{
				lvResults = (Vector) laObject;
			}
			// cast object into FundsData
			if (laObject instanceof FundsData)
			{
				laFundsData = (FundsData) laObject;
			}
		} // end of parsing of datain vector
		// Header Vector
		Vector lvHeader = createHeader(laFundsData);
		// This vector will contain the information for the column headings
		Vector lvTable = createFeeTableHeaders();
		if (lvResults != null && lvResults.size() > 0)
		{
			// build the data for reporting
			TreeMap laDataMap = new TreeMap();
			laDataMap = buildFeesData(lvResults, laDataMap);
			Collection laDataCollection = laDataMap.values();
			Iterator laDataList = laDataCollection.iterator();
			/**
			 * PayableTypeCd Break Totals Storage object.
			 * It is of CountyWideReportMoneyData type.
			 * Dollar fields are initialized to use the add function.
			 */
			CountyWideReportFeeData laPayableTypeCdTotals =
				new CountyWideReportFeeData();
			laPayableTypeCdTotals.initializeMoneyAndQty();
			/**
			 * Substation Totals Storage object.
			 * It is of CountyWideReportMoneyData type.
			 * Dollar fields are initialized to use the add function.
			 */
			CountyWideReportFeeData laSubstationTotals =
				new CountyWideReportFeeData();
			laSubstationTotals.initializeMoneyAndQty();
			// If there is data, start the report
			if (laDataMap != null && laDataList.hasNext())
			{
				// holder for PayableTypeCodeDesc.  Used at break printing.
				String lsPrePayableTypeCodeDesc = "";
				// number of detail lines that can be used
				int liWorkingLines =
					caRptProps.getPageHeight()
						- END_OF_PAGE_WHITE_SPACE;
				// CQU100001958
				// set up the data object
				CountyWideReportFeeData laDataLine =
					new CountyWideReportFeeData();
				// get the first data object
				laDataLine =
					(CountyWideReportFeeData) laDataMap.get(
						laDataMap.firstKey());
				// process through the data vector
				while (laDataList.hasNext())
				{
					generateHeader(lvHeader, lvTable);
					// process through the page
					for (int j = 0; j < liWorkingLines; j++)
					{
						// get the next data object
						laDataLine =
							(CountyWideReportFeeData) laDataList.next();
						// check for PayableTypeCodeDesc break
						if (!lsPrePayableTypeCodeDesc
							.equals(laDataLine.getPayableTypeCdDesc()))
						{
							// there is totals data, print it out
							if (!lsPrePayableTypeCodeDesc.equals(""))
							{
								// set up the string to print with the total line
								laPayableTypeCdTotals.setAcctItmCdDesc(
									TOTAL_TBL_HEADER1
										+ ONE_SPACE_STRING
										+ lsPrePayableTypeCodeDesc
										+ COLON_STRING);
								// CQU100001958
								// page break if needed.							// CQU100003066
								if (
									isNewPageNeeded(BUFFER_SPACE_HEADER))
								{
									printNewPage(lvHeader, lvTable);
								}
								// print a normal total line
								feesPrintTotalLine(
									laPayableTypeCdTotals,
									SUBTOTAL);
								laPayableTypeCdTotals
									.initializeMoneyAndQty();
								caRpt.nextLine();
							} // end if there are totals to print
							// page break if needed.							// CQU100003066
							if (isNewPageNeeded(BUFFER_SPACE_DETAIL))
							{
								printNewPage(lvHeader, lvTable);
							}
							// print the PayableType Desc to start
							caRpt.blankLines(LINES_1);
							caRpt.print(
								laDataLine.getPayableTypeCdDesc());
							lsPrePayableTypeCodeDesc =
								laDataLine.getPayableTypeCdDesc();
							caRpt.blankLines(LINES_1);
						} // end if check for PayableType Break
						// page break if needed.							
						// CQU100003066
						if (isNewPageNeeded(BUFFER_SPACE_DETAIL))
						{
							printNewPage(lvHeader, lvTable);
						}
						// print the line
						feesPrintLine(laDataLine, false, true);
						// update j to reflect current lines
						j = caRpt.getCurrX();
						//add up the totals
						laPayableTypeCdTotals.addUpTotals(laDataLine);
						laSubstationTotals.addUpTotals(laDataLine);
						// check to see if there is still data to process
						// if not, exit for loop.
						if (!laDataList.hasNext())
						{
							// set up the string to print with the total line
							laPayableTypeCdTotals.setAcctItmCdDesc(
								TOTAL_TBL_HEADER1
									+ ONE_SPACE_STRING
									+ lsPrePayableTypeCodeDesc
									+ COLON_STRING);
							// CQU100001958
							// page break if needed.							
							// CQU100003066
							if (isNewPageNeeded(BUFFER_SPACE_HEADER))
							{
								printNewPage(lvHeader, lvTable);
							}
							// print a normal total line
							feesPrintTotalLine(
								laPayableTypeCdTotals,
								SUBTOTAL);
							caRpt.blankLines(LINES_1);
							// add for defect on page too long...
							// page break if needed.							
							// CQU100004092
							if (isNewPageNeeded(BUFFER_SPACE_HEADER))
							{
								printNewPage(lvHeader, lvTable);
							}
							// Set up the description for the totals
							laSubstationTotals.setAcctItmCdDesc(
								REPORT_TOTAL_STRING);
							// CQU100001958
							// print the substation total line.
							feesPrintTotalLine(
								laSubstationTotals,
								FINALTOTAL);
							// add for defect on page too long...
							// page break if needed.							
							// CQU100004092
							if (isNewPageNeeded(BUFFER_SPACE_HEADER))
							{
								printNewPage(lvHeader, lvTable);
							}
							caRpt.blankLines(LINES_1);
							// defect 8628 
							// this.generateEndOfReport();
							// end defect 8628 
							break;
						} // end of check to see if all the data nas been processed.
					} // end for

					// defect 8628 
					//generateFooter();
					generateFooter(!laDataList.hasNext());
					// end defect 8628 
				} // while there is data
			} // end if vector is null check
		} // end if
		else
		{
			generateHeader(lvHeader, lvTable);
			generateNoTransMessage();
			// defect 8628 
			//generateFooter();
			generateFooter(true);
			// end defect 8628 
		}
	} // end of method

	/**
	 * This method generates the Inventory Summary part of Substation Summary.
	 *
	 * @param avDataIn Vector
	 * @throws RTSException 
	 */
	public void genInvSection(Vector avDataIn) throws RTSException
	{
		// instantiate the FundsData object to be populated in the for loop
		FundsData laFundsData = new FundsData();

		// instantiate the Vector for the resultset to be populated in the for loop
		Vector lvResults = new Vector();

		// get the objects off of the lvDataIn vector
		for (int i = 0; i < avDataIn.size(); i++)
		{
			Object laObject = avDataIn.elementAt(i);
			// cast laObject into lvResults
			if (laObject instanceof Vector)
			{
				lvResults = (Vector) laObject;
			}
			// cast object into FundsData
			if (laObject instanceof FundsData)
			{
				laFundsData = (FundsData) laObject;
			}
		}
		Vector lvHeader = createHeader(laFundsData);
		// This vector will contain the information for the column headings
		Vector lvTable = createInvTableHeaders();
		// If there is data, start the report
		if (lvResults != null && lvResults.size() > 0)
		{
			// vector counter
			int liResultsPointer = 0;
			// number of detail lines that can be used
			int liWorkingLines =
				caRptProps.getPageHeight() - END_OF_PAGE_WHITE_SPACE;
			// CQU100001958
			// set up the data object
			InventorySummaryReportData laDataLine =
				new InventorySummaryReportData();
			// get the first data object
			laDataLine =
				(InventorySummaryReportData) lvResults.elementAt(
					liResultsPointer);
			//liResultsPointer = liResultsPointer + 1;
			// process through the data vector
			while (liResultsPointer < lvResults.size())
			{
				// defect 8628 
				boolean lbEOF = false;
				// end defect 8628 
				generateHeader(lvHeader, lvTable);
				// process through the page
				for (int j = 0; j < liWorkingLines; j++)
				{
					// get the next data object
					laDataLine =
						(
							InventorySummaryReportData) lvResults
								.elementAt(
							liResultsPointer);
					// defect 6067, created new character length spec for 29 characters
					caRpt.print(
						laDataLine.getItmCdDesc(),
						ITEMCODE_START_POSITION,
						LENGTH_29);
					// end defect 6067
					// Only print the year if it has value
					if (laDataLine.getInvItmYr() > 0)
					{
						caRpt.print(
							String.valueOf(laDataLine.getInvItmYr()),
							ITEMYEAR_START_POSITION,
							LENGTH_04);
					}
					// Only print the amount sold if it is greater than zero
					if (laDataLine.getTotalItmQtySold() > 0)
					{
						caRpt.rightAlign(
							String.valueOf(
								laDataLine.getTotalItmQtySold()),
							SOLD_START_POSITION,
							LENGTH_06);
					}
					// Only print the amount voided if it is greater than zero
					if (laDataLine.getTotalItmQtyVoid() > 0)
					{
						caRpt.rightAlign(
							String.valueOf(
								laDataLine.getTotalItmQtyVoid()),
							VOIDED_START_POSITION,
							LENGTH_06);
					}
					// Only print the amout reused if it is greater than zero
					if (laDataLine.getTotalItmQtyReuse() > 0)
					{
						caRpt.rightAlign(
							String.valueOf(
								laDataLine.getTotalItmQtyReuse()),
							REUSED_START_POSITION,
							LENGTH_06);
					}
					// PCR 34
					//				Only print the amount reprinted if it is greater than zero
					if (laDataLine.getTotalItmQtyReprnt() > 0)
					{
						caRpt.rightAlign(
							String.valueOf(
								laDataLine.getTotalItmQtyReprnt()),
							REPRINTED_START_POSITION,
							LENGTH_06);
					}
					// End PCR 34
					caRpt.nextLine();
					liResultsPointer = liResultsPointer + 1;
					// check to see if there is still data to process
					// if not, exit for loop.
					if (liResultsPointer >= lvResults.size())
					{
						// defect 8628
						//		caRpt.blankLines(LINES_1);
						//		this.generateEndOfReport();
						lbEOF = true;
						break;
						// end defect 8628 
					}
				} // end for

				// defect 8628 
				// generateFooter();
				generateFooter(lbEOF);
				// end defect 8628  
			}
		}
		else
		{
			generateHeader(lvHeader, lvTable);
			generateNoTransMessage();
			// defect 8628 
			//generateFooter();
			generateFooter(true);
			// end defect 8628 
		}
	}

	/**
	 * This method generates the Payment Report part of Substation Summary.
	 *
	 * @param avDataIn Vector
	 * @throws RTSException 
	 */
	public void genPaymentSection(Vector avDataIn) throws RTSException
	{
		// instantiate the FundsData object to be populated in the for loop
		FundsData laFundsData = null;

		// instantiate the Vector for the resultset of Cash Data
		Vector lvResultsCash = null;

		// instantiate the Vector for the resultset of Non-Cash Data
		Vector lvResultsNonCash = null;

		// get the objects off of the lvDataIn vector
		for (int i = 0; i < avDataIn.size(); i++)
		{
			Object laObject = avDataIn.elementAt(i);

			// cast laObject into Vector (lvResultsNonCash) 
			if (laObject instanceof Vector && lvResultsCash != null)
			{
				lvResultsNonCash = (Vector) laObject;
			}

			// cast laObject into Vector (lvResultsCash)
			if (laObject instanceof Vector && lvResultsCash == null)
			{
				lvResultsCash = (Vector) laObject;
			}
			// cast object into laFundsData
			if (laObject instanceof FundsData)
			{
				laFundsData = (FundsData) laObject;
			}
		} // end of parsing of datain vector

		// Header Vector
		Vector lvHeader = createHeader(laFundsData);

		// This vector will contain the information for the column headings
		Vector lvTable = createPaymentTableHeaders();
		if ((lvResultsCash == null || lvResultsCash.size() == 0)
			&& (lvResultsNonCash == null || lvResultsNonCash.size() == 0))
		{
			// there were no records found.  Show no records found message.
			generateHeader(lvHeader, lvTable);
			generateNoTransMessage();
			generateFooter(true);
		}
		else
		{
			// found data.  lets process it.
			// build the Cash data for reporting
			TreeMap laDataMapCash = new TreeMap();
			laDataMapCash =
				buildPaymentDataCash(lvResultsCash, laDataMapCash);
			Collection laDataCollection = laDataMapCash.values();
			Iterator laDataList = laDataCollection.iterator();
			/**
			 * Cash Operation Break Totals Storage object.
			 * It is of CountyWideReportMoneyData type.
			 * Dollar fields are initialized to use the add function.
			 */
			CountyWideReportPaymentData laCashOperationTotals =
				new CountyWideReportPaymentData();
			laCashOperationTotals.initializeMoneyAndQty();
			/**
			 * Substation Totals Storage object.
			 * It is of CountyWideReportMoneyData type.
			 * Dollar fields are initialized to use the add function.
			 */
			CountyWideReportPaymentData laSubstationTotals =
				new CountyWideReportPaymentData();
			laSubstationTotals.initializeMoneyAndQty();

			// booleans to track when detail reporting is done.
			// CQU100003852
			boolean lbMoreCashData = true;
			boolean lbMoreNonCashData = true;
			// boolean to know if this is the first page
			// CQU100003852
			boolean lbFirstPage = true;
			// number of detail lines that can be used
			int liWorkingLines =
				caRptProps.getPageHeight() - END_OF_PAGE_WHITE_SPACE;
			// CQU100001958 
			// holder for PayableTypeCodeDesc.  Used at break printing.
			// set up the data object
			CountyWideReportPaymentData laDataLine =
				new CountyWideReportPaymentData();
			// get the first data object i it is there
			if (laDataMapCash.size() > 0)
			{
				laDataLine =
					(CountyWideReportPaymentData) laDataMapCash.get(
						laDataMapCash.firstKey());
			}
			// Set up the big processing loop.
			// This will process both vectors until the end.
			while (lbMoreCashData && lbMoreNonCashData)
			{
				// print the footer if this is not the first page
				// CQU100003852
				if (lbFirstPage)
				{
					lbFirstPage = false;
				}
				else
				{
					generateFooter();
				}
				// put up the page header
				generateHeader(lvHeader, lvTable);
				// print cash drawer header if this switch is on.
				// cash should not be more than one line
				if (lbMoreCashData)
				{
					// Always print Cash string
					caRpt.println(CASH_DRAWER_OPER_STRING);
					caRpt.nextLine();
				}
				// This is the cash processing piece.  It runs first
				// If there is cash data, start the cash section of the report
				if (laDataMapCash != null && laDataList.hasNext())
				{
					// process through the data vector
					while (laDataList.hasNext())
					{
						// process through the page
						for (int j = 0; j < liWorkingLines; j++)
						{
							// get the next data object
							laDataLine =
								(CountyWideReportPaymentData) laDataList
									.next();
							// page break if needed.							// CQU100003066
							if (isNewPageNeeded(BUFFER_SPACE_DETAIL))
							{
								printNewPage(lvHeader, lvTable);
							}
							// print the line
							// if the payment is not cash, print the qty.		// CQU100002265
							if (laDataLine.getPymntTypeCd() != 1)
								// CQU100002265
							{ // CQU100002265
								paymentPrintLine(
									laDataLine,
									false,
									true);
								// CQU100002265
							} // CQU100002265
							else // CQU100002265
								{ // CQU100002265
								// do not print the qty if this is cash			// CQU100002265
								paymentPrintLine(
									laDataLine,
									false,
									false);
								// CQU100002265
							} // CQU100002265
							// update j to reflect the current line				// CQU100003066
							j = caRpt.getCurrX(); // CQU100003066
							//add up the totals
							laCashOperationTotals.addUpTotals(
								laDataLine);
							laSubstationTotals.addUpTotals(laDataLine);
							// break out when there is no more data
							if (!laDataList.hasNext())
							{
								break;
							}
						} // end for
					} // while there is cash data
				}
				if (!laDataList.hasNext())
				{
					// page break if needed.							
					// CQU100003066
					if (isNewPageNeeded(BUFFER_SPACE_HEADER))
					{
						printNewPage(lvHeader, lvTable);
					}
					// Set up the description for the totals
					laCashOperationTotals.setPymntTypeCdDesc(
						TOTAL_STRING);
					// print the substation total line.
					paymentPrintTotalLine(
						laCashOperationTotals,
						SUBTOTAL);
					// (CQU100001958)
					caRpt.blankLines(LINES_2);
					// turn off processing for cash data  // CQU100003852 
					lbMoreCashData = false;
				}
				// end of Cash Processing
				// Start of non-cash processing
				if (!lbMoreCashData
					&& lvResultsNonCash != null
					&& lvResultsNonCash.size() > 0)
				{
					// page break if needed.							
					// CQU100003066
					if (isNewPageNeeded(BUFFER_SPACE_DETAIL))
					{
						printNewPage(lvHeader, lvTable);
					}
					printNonCashData(
						lvResultsNonCash,
						laSubstationTotals,
						lvHeader,
						lvTable);
				}
				if (!lbMoreCashData)
				{
					lbMoreNonCashData = false;
				}
				// end of non-cash processing
				// Report Total Processing
				if (!lbMoreCashData && !lbMoreNonCashData)
				{
					caRpt.blankLines(LINES_1);
					// Set up the description for the totals
					laSubstationTotals.setPymntTypeCdDesc(
						REPORT_TOTAL_STRING);
					// print the substation total line.
					paymentPrintTotalLine(
						laSubstationTotals,
						FINALTOTAL);

					// defect 8628 
					// print the end of report message
					//this.generateEndOfReport();
					// close out the page
					// generateFooter();
					generateFooter(true);
					// end defect 8628 
				}
				// end Report Total Processing
			} // end while there is data CQU100003852
		} // end if vector is null check
	}

	/**
	 * Determines if a new page is needed.
	 * int passed in determines amount of buffer space to leave.
	 * 
	 * @param  aiBufferSpaceNeeded int
	 * @return boolean
	 */
	private boolean isNewPageNeeded(int aiBufferSpaceNeeded)
	{
		// if the curent page is up to the page break point, return true. 
		//(CQU100002213)
		return (
			caRpt.getCurrX()
				>= caRptProps.getPageHeight() - aiBufferSpaceNeeded);
	}

	/**
	 * This method attempts to provide a way to test in stand alone mode.
	 *
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// instantiate the FundsReportData object and set the values
		FundsReportData laFundsReportData = new FundsReportData();
		laFundsReportData.setEntity(FundsConstant.SUBSTATION);
		laFundsReportData.setPrimarySplit(FundsConstant.NONE);
		laFundsReportData.setShowSourceMoney(true);
		// note that the amdate needs to be updated to match current test 
		//data.
		laFundsReportData.setFromRange(new RTSDate());
		// instantiate the FundsData object and set values
		FundsData laFundsData = new FundsData();
		laFundsData.setOfficeIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laFundsData.setSubStationId(SystemProperty.getSubStationId());
		laFundsData.setCashWsId(SystemProperty.getWorkStationId());
		// add FundsReportData to FundsData
		laFundsData.setFundsReportData(laFundsReportData);
		// Instantiating a new Report Class 
		GenSubStationSummaryReports laGenSubStatRpts =
			new GenSubStationSummaryReports();
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		// formatReport in the Template does not support throwing 
		//RTSException
		//try
		//{
		// Query will be done in formatReport
		laGenSubStatRpts.formatReport(laFundsData);
		//}
		//catch (RTSException rtse)
		//{
		//	rtse.printStackTrace();
		//	System.exit(0);
		//}
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFileOutStream;
		PrintStream laPrintStream = null;
		try
		{
			laOutputFile = new File(laGenSearchData.getKey2());
			laFileOutStream = new FileOutputStream(laOutputFile);
			laPrintStream = new PrintStream(laFileOutStream);
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
		}
		laPrintStream.print(
			laGenSubStatRpts.caRpt.getReport().toString());
		laPrintStream.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(laGenSearchData.getKey2());
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\TitlePkgRpt.txt prn");
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.services.reports.reports.GenSubstationSummaryReports");
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * Print the detail line for payment section.
	 * <p>This routine prints a money line for the selected report.
	 * The line can be from detail data or a total line.
	 * <p>
	 * 
	 * @param aaDataLine CountyWideReportMoneyData
	 * @param abTotals boolean - This indicates we are processing a 
	 *  Totals line.
	 * @param abQtyShow	boolean - Determines if Qty should show
	 */
	public void paymentPrintLine(
		CountyWideReportPaymentData aaDataLine,
		boolean abTotals,
		boolean abQtyShow)
	{
		if (abTotals)
		{
			// this is a total line.  
			// Print the Totals Description on the left margin
			caRpt.print(
				aaDataLine.getPymntTypeCdDesc(),
				START_PT_1,
				LENGTH_27);
		}
		else
		{
			// this is a detail line.  
			// Print the description at the normal place
			caRpt.print(
				aaDataLine.getPymntTypeCdDesc(),
				START_PT_1,
				LENGTH_27);
		}
		// format and print the customer amounts on each detail line
		if (!aaDataLine.getCustAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine.getCustAcctItmAmt().printDollar().substring(
					1,
					aaDataLine
						.getCustAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_CUST_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getCustAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getCustAcctItmQty()),
				DETAIL_CUST_QTY_START,
				LENGTH_05);
		}
		// format and print the subcontractor amounts on each detail line
		if (!aaDataLine.getSubconAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getSubconAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getSubconAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_SUBCON_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getSubconAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getSubconAcctItmQty()),
				DETAIL_SUBCON_QTY_START,
				LENGTH_05);
		}
		// format and print the dealer amounts on each detail line
		if (!aaDataLine.getDealerAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getDealerAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getDealerAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_DEALER_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getDealerAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getDealerAcctItmQty()),
				DETAIL_DEALER_QTY_START,
				LENGTH_05);
		}
		// format and print the internet amounts on each detail line
		if (!aaDataLine.getInternetAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getInternetAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getInternetAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_INTNET_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getInternetAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getInternetAcctItmQty()),
				DETAIL_INTNET_QTY_START,
				LENGTH_05);
		}
		// format and print the overall amounts on each detail line
		if (!aaDataLine.getTotalAcctItmAmt().equals(ZERODOLLAR)
			|| abTotals)
		{
			caRpt.rightAlign(
				aaDataLine
					.getTotalAcctItmAmt()
					.printDollar()
					.substring(
					1,
					aaDataLine
						.getTotalAcctItmAmt()
						.printDollar()
						.length()),
				DETAIL_TOTAL_AMT_START,
				LENGTH_13);
		}
		if (aaDataLine.getTotalAcctItmQty() > 0 && abQtyShow)
		{
			caRpt.rightAlign(
				String.valueOf(aaDataLine.getTotalAcctItmQty()),
				DETAIL_TOTAL_QTY_START,
				LENGTH_05);
		}
		caRpt.nextLine();
	}

	/**
	 * This processes the Totals Lines for Payment.
	 * <p>
	 * 
	 * @param aaDataLine CountyWideReportPaymentData
	 * @param aiTotalType int - This indicates we are processing a Totals line.
	 */
	public void paymentPrintTotalLine(
		CountyWideReportPaymentData aaDataLine,
		int aiTotalType)
	{
		String lsDashedLineForTotal = "";
		// 
		switch (aiTotalType)
		{
			case SUBTOTAL :
				{
					lsDashedLineForTotal =
						caRpt.singleDashes(LENGTH_13);
					break;
				}
			case FINALTOTAL :
				{
					lsDashedLineForTotal =
						caRpt.doubleDashes(LENGTH_13);
					break;
				}
			default :
				{
					lsDashedLineForTotal =
						caRpt.singleDashes(LENGTH_13);
				}
		}
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_CUST_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_SUBCON_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_DEALER_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_INTNET_AMT_START,
			LENGTH_13);
		caRpt.print(
			lsDashedLineForTotal,
			DETAIL_TOTAL_AMT_START,
			LENGTH_13);
		caRpt.nextLine();
		paymentPrintLine(aaDataLine, true, false);
		caRpt.nextLine();
	}

	/**
	 * Closes out page and starts a new one.
	 * 
	 * @param avHeader Vector
	 * @param avTable Vector
	 */
	private void printNewPage(Vector avHeader, Vector avTable)
	{
		generateFooter();
		generateHeader(avHeader, avTable);
	}

	/**
	 * process the NonCash Payment Data
	 * 
	 * @param avDataIn 	Vector - holds the NonCash Payment Data
	 * @param aaSubstationTotals CountyWideReportPaymentData - Holds the Substation Totals
	 * @param lvHeader	Vector	- report header info
	 * @param lvTable	Vector	- report column headers
	 */
	public void printNonCashData(
		Vector avDataIn,
		CountyWideReportPaymentData aaSubstationTotals,
		Vector avHeader,
		Vector avTable)
	{
		// set up the treemap
		TreeMap laDataMap = new TreeMap();
		laDataMap = buildFeesData(avDataIn, laDataMap);

		// Setup the Iterator
		Collection laDataCollection = laDataMap.values();
		Iterator laDataList = laDataCollection.iterator();

		// Print the group description
		caRpt.println(NON_CASH_DRAWER_OPER_STRING);
		caRpt.nextLine();

		// instantiate the data in line
		CountyWideReportFeeData laDataLine =
			new CountyWideReportFeeData();

		// instantiate the totals line
		CountyWideReportFeeData laTotals =
			new CountyWideReportFeeData();
		laTotals.initializeMoneyAndQty();

		while (laDataList.hasNext())
		{
			// page break if needed.							
			// CQU100003066
			if (isNewPageNeeded(BUFFER_SPACE_DETAIL))
			{
				printNewPage(avHeader, avTable);
			}
			laDataLine = (CountyWideReportFeeData) laDataList.next();
			feesPrintLine(laDataLine, false, true);
			laTotals.addUpTotals(laDataLine);
			// add data to Substation Totals
			aaSubstationTotals.setCustAcctItmAmt(
				aaSubstationTotals.getCustAcctItmAmt().add(
					laDataLine.getCustAcctItmAmt()));
			aaSubstationTotals.setSubconAcctItmAmt(
				aaSubstationTotals.getSubconAcctItmAmt().add(
					laDataLine.getSubconAcctItmAmt()));
			aaSubstationTotals.setDealerAcctItmAmt(
				aaSubstationTotals.getDealerAcctItmAmt().add(
					laDataLine.getDealerAcctItmAmt()));
			aaSubstationTotals.setInternetAcctItmAmt(
				aaSubstationTotals.getInternetAcctItmAmt().add(
					laDataLine.getInternetAcctItmAmt()));
			aaSubstationTotals.setTotalAcctItmAmt(
				aaSubstationTotals.getTotalAcctItmAmt().add(
					laDataLine.getTotalAcctItmAmt()));
		}
		// page break if needed.							
		// CQU100003066
		if (isNewPageNeeded(BUFFER_SPACE_HEADER))
		{
			printNewPage(avHeader, avTable);
		}
		// Set up the description for the totals
		laTotals.setAcctItmCdDesc(TOTAL_STRING);
		// correct which desc field to use (CQU100001958)
		// print the total line.
		feesPrintTotalLine(laTotals, SUBTOTAL);
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		return null;
	}

	/**
	 * This method returns test data for testing the Fees 
	 * Section of the report
	 * 
	 * @param asQuery 
	 * @return Vector
	 */
	public Vector queryDataFees(String asQuery)
	{
		// this is faked data..
		Vector lvData = new Vector();

		// Fee Source 1 is Customer
		// Fee Source 2 is Subcontractor
		// Fee Source 3 is Dealer
		// Fee Source 5 is Internet Data
		FeeSummaryReportData laDataLineA1 = new FeeSummaryReportData();
		laDataLineA1.setFeeSourceCd(1);
		laDataLineA1.setPayableTypeCd(1);
		laDataLineA1.setPayableTypeCdDesc("TITLE");
		laDataLineA1.setAcctItmGrpCd(0);
		laDataLineA1.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineA1.setTotalAcctItmQty(1);
		laDataLineA1.setTotalAcctItmAmt(new Dollar("1.00"));
		lvData.addElement(laDataLineA1);

		FeeSummaryReportData laDataLineB1 = new FeeSummaryReportData();
		laDataLineB1.setFeeSourceCd(2);
		laDataLineB1.setPayableTypeCd(1);
		laDataLineB1.setPayableTypeCdDesc("TITLE");
		laDataLineB1.setAcctItmGrpCd(0);
		laDataLineB1.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineB1.setTotalAcctItmQty(2);
		laDataLineB1.setTotalAcctItmAmt(new Dollar("2.00"));
		lvData.addElement(laDataLineB1);

		FeeSummaryReportData laDataLineC1 = new FeeSummaryReportData();
		laDataLineC1.setFeeSourceCd(3);
		laDataLineC1.setPayableTypeCd(1);
		laDataLineC1.setPayableTypeCdDesc("TITLE");
		laDataLineC1.setAcctItmGrpCd(0);
		laDataLineC1.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineC1.setTotalAcctItmQty(3);
		laDataLineC1.setTotalAcctItmAmt(new Dollar("3.00"));
		lvData.addElement(laDataLineC1);

		FeeSummaryReportData laDataLineD1 = new FeeSummaryReportData();
		laDataLineD1.setFeeSourceCd(4);
		laDataLineD1.setPayableTypeCd(1);
		laDataLineD1.setPayableTypeCdDesc("TITLE");
		laDataLineD1.setAcctItmGrpCd(0);
		laDataLineD1.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineD1.setTotalAcctItmQty(4);
		laDataLineD1.setTotalAcctItmAmt(new Dollar("4.00"));
		lvData.addElement(laDataLineD1);

		FeeSummaryReportData laDataLineE1 = new FeeSummaryReportData();
		laDataLineE1.setFeeSourceCd(5);
		laDataLineE1.setPayableTypeCd(1);
		laDataLineE1.setPayableTypeCdDesc("TITLE");
		laDataLineE1.setAcctItmGrpCd(0);
		laDataLineE1.setAcctItmCdDesc("TITLE APPLICATION FEE");
		laDataLineE1.setTotalAcctItmQty(5);
		laDataLineE1.setTotalAcctItmAmt(new Dollar("5.00"));
		lvData.addElement(laDataLineE1);
		return lvData;
	}

	/**
	 * This method returns test data for testing the Inventory 
	 * Summary Section of the report
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryDataInvSumm(String asQuery)
	{
		// this is faked data..
		Vector lvData = new Vector();

		InventorySummaryReportData laDataLineA1 =
			new InventorySummaryReportData();
		laDataLineA1.setItmCdDesc("Passenger Plate");
		laDataLineA1.setInvItmYr(0);
		laDataLineA1.setTotalItmQtySold(30);
		laDataLineA1.setTotalItmQtyVoid(20);
		laDataLineA1.setTotalItmQtyReuse(10);
		lvData.addElement(laDataLineA1);

		InventorySummaryReportData laDataLineB1 =
			new InventorySummaryReportData();
		laDataLineB1.setItmCdDesc("Windshield Sticker");
		laDataLineB1.setInvItmYr(2001);
		laDataLineB1.setTotalItmQtySold(30);
		laDataLineB1.setTotalItmQtyVoid(20);
		laDataLineB1.setTotalItmQtyReuse(10);
		lvData.addElement(laDataLineB1);
		return lvData;
	}

	/**
	 * This method returns test data for testing the Payment Section of 
	 * the report
	 */
	public Vector queryDataPayment(String asQuery)
	{
		// this is faked data..
		Vector lvData = new Vector();

		// Fee Source 1 is Customer
		// Fee Source 2 is Subcontractor
		// Fee Source 3 is Dealer
		// Fee Source 5 is Internet Data

		PaymentSummaryReportData laDataLineA1 =
			new PaymentSummaryReportData();
		laDataLineA1.setFeeSourceCd(1);
		laDataLineA1.setPymntTypeCd(1);
		laDataLineA1.setPymntTypeCdDesc("TITLE");
		laDataLineA1.setPymntTypeQty(1);
		laDataLineA1.setTotalPymntTypeAmt(new Dollar("1.00"));
		lvData.addElement(laDataLineA1);

		PaymentSummaryReportData laDataLineB1 =
			new PaymentSummaryReportData();
		laDataLineB1.setFeeSourceCd(2);
		laDataLineB1.setPymntTypeCd(1);
		laDataLineB1.setPymntTypeCdDesc("TITLE");
		laDataLineB1.setPymntTypeQty(2);
		laDataLineB1.setTotalPymntTypeAmt(new Dollar("2.00"));
		lvData.addElement(laDataLineB1);

		PaymentSummaryReportData laDataLineC1 =
			new PaymentSummaryReportData();
		laDataLineC1.setFeeSourceCd(3);
		laDataLineC1.setPymntTypeCd(1);
		laDataLineC1.setPymntTypeCdDesc("TITLE");
		laDataLineC1.setPymntTypeQty(3);
		laDataLineC1.setTotalPymntTypeAmt(new Dollar("3.00"));
		lvData.addElement(laDataLineC1);

		PaymentSummaryReportData laDataLineD1 =
			new PaymentSummaryReportData();
		laDataLineD1.setFeeSourceCd(4);
		laDataLineD1.setPymntTypeCd(1);
		laDataLineD1.setPymntTypeCdDesc("TITLE");
		laDataLineD1.setPymntTypeQty(4);
		laDataLineD1.setTotalPymntTypeAmt(new Dollar("4.00"));
		lvData.addElement(laDataLineD1);

		PaymentSummaryReportData laDataLineE1 =
			new PaymentSummaryReportData();
		laDataLineE1.setFeeSourceCd(5);
		laDataLineE1.setPymntTypeCd(1);
		laDataLineE1.setPymntTypeCdDesc("TITLE");
		laDataLineE1.setPymntTypeQty(5);
		laDataLineE1.setTotalPymntTypeAmt(new Dollar("5.00"));
		lvData.addElement(laDataLineE1);
		return lvData;
	}
	/**
	 * This method returns test data for testing the NonCash 
	 * Payment Section of the report
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryDataPaymentNonCash(String asQuery)
	{
		// this is faked data..
		Vector lvData = new Vector();

		// Fee Source 1 is Customer
		// Fee Source 2 is Subcontractor
		// Fee Source 3 is Dealer
		// Fee Source 5 is Internet Data

		FeeSummaryReportData laDataLineA1 = new FeeSummaryReportData();
		laDataLineA1.setFeeSourceCd(1);
		laDataLineA1.setPayableTypeCd(1);
		laDataLineA1.setPayableTypeCdDesc("TITLE");
		laDataLineA1.setAcctItmGrpCd(0);
		laDataLineA1.setAcctItmCdDesc("CASH");
		laDataLineA1.setTotalAcctItmQty(1);
		laDataLineA1.setTotalAcctItmAmt(new Dollar("1.00"));
		lvData.addElement(laDataLineA1);

		FeeSummaryReportData laDataLineB1 = new FeeSummaryReportData();
		laDataLineB1.setFeeSourceCd(2);
		laDataLineB1.setPayableTypeCd(1);
		laDataLineB1.setPayableTypeCdDesc("TITLE");
		laDataLineB1.setAcctItmGrpCd(0);
		laDataLineB1.setAcctItmCdDesc("CASH");
		laDataLineB1.setTotalAcctItmQty(2);
		laDataLineB1.setTotalAcctItmAmt(new Dollar("2.00"));
		lvData.addElement(laDataLineB1);

		FeeSummaryReportData laDataLineC1 = new FeeSummaryReportData();
		laDataLineC1.setFeeSourceCd(3);
		laDataLineC1.setPayableTypeCd(1);
		laDataLineC1.setPayableTypeCdDesc("TITLE");
		laDataLineC1.setAcctItmGrpCd(0);
		laDataLineC1.setAcctItmCdDesc("CASH");
		laDataLineC1.setTotalAcctItmQty(3);
		laDataLineC1.setTotalAcctItmAmt(new Dollar("3.00"));
		lvData.addElement(laDataLineC1);

		FeeSummaryReportData laDataLineD1 = new FeeSummaryReportData();
		laDataLineD1.setFeeSourceCd(4);
		laDataLineD1.setPayableTypeCd(1);
		laDataLineD1.setPayableTypeCdDesc("TITLE");
		laDataLineD1.setAcctItmGrpCd(0);
		laDataLineD1.setAcctItmCdDesc("CASH");
		laDataLineD1.setTotalAcctItmQty(4);
		laDataLineD1.setTotalAcctItmAmt(new Dollar("4.00"));
		lvData.addElement(laDataLineD1);

		FeeSummaryReportData laDataLineE1 = new FeeSummaryReportData();
		laDataLineE1.setFeeSourceCd(5);
		laDataLineE1.setPayableTypeCd(1);
		laDataLineE1.setPayableTypeCdDesc("TITLE");
		laDataLineE1.setAcctItmGrpCd(0);
		laDataLineE1.setAcctItmCdDesc("CASH");
		laDataLineE1.setTotalAcctItmQty(5);
		laDataLineE1.setTotalAcctItmAmt(new Dollar("5.00"));
		lvData.addElement(laDataLineE1);
		return lvData;
	}
}
