package com.txdot.isd.rts.server.webapps.reports;import java.util.Vector;import com.txdot.isd.rts.services.cache.OfficeIdsCache;import com.txdot.isd.rts.services.data.InternetDepositReconData;import com.txdot.isd.rts.services.data.OfficeIdsData;import com.txdot.isd.rts.services.data.ReportSearchData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.reports.ReportProperties;import com.txdot.isd.rts.services.reports.ReportTemplate;import com.txdot.isd.rts.services.reports.funds.GenFeeReport;import com.txdot.isd.rts.services.util.Dollar;import com.txdot.isd.rts.services.util.RTSDate;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.CommonConstant;import com.txdot.isd.rts.services.util.constants.ReportConstant;import com	.txdot	.isd	.rts	.services	.webapps	.util	.constants	.RegRenProcessingConstants;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;import com.txdot.isd.rts.server.db.InternetDepositReconHstry;/* * GenInternetDepositReconReport.java * * (c) Texas Department of Transportation 2009 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	02/08/2009	Created  * 							defect 9935 Ver Defect_POS_D * K Harrell	02/12/2009	Modified in refactor of classes/methods  * 							where IVTRS changed to Internet  * 							defect 9935 Ver Defect_POS_D   * K Harrell	02/18/2009	Use RegRenewalConstant for AMEX,DISC  * 							delete AMERICANEXPRESS_ABBR, *					  		  DISCOVER_ABBR *							defect 9935 Ver Defect_POS_D * K Harrell	06/18/2009	Add/Use new msg if data not yet provided * 							by vendor.  * 							add DATA_NOT_YET_PROVIDED_PROCESSED,  *							DATA NOT YET PROVIDED BY VENDOR,  *							DATA_NOT_YET_PROVIDED_SYMBOL, *							DATA_NOT_YET_PROVIDED_FOOTNOTE *							add isDataProvided(),  *							 generateNoDataOrNotProvidedMsg(), *							 generateDataNotYetProvided() *							modify formatReport(), 	 *							 generateExceptionPage() * 							defect 10011 Ver Defect_POS_F * K Harrell	08/13/2009	Implement new generateFooter(boolean)  * 							modify formatReport(), generateExceptionPage() * 							defect 8628 Ver Defect_POS_F     * --------------------------------------------------------------------- *//** * Generates the Internet Deposit Reconciliation Report * * @version	Defect_POS_F	08/13/2009 * @author	Kathy Harrell * <br>Creation Date:		02/08/2009 *//* &GenInternetDepositReconReport& */public class GenInternetDepositReconReport extends ReportTemplate{	// Dollar/* &GenInternetDepositReconReport'caAMEXTotal& */	private Dollar caAMEXTotal = new Dollar("0");/* &GenInternetDepositReconReport'caDISCTotal& */	private Dollar caDISCTotal = new Dollar("0");/* &GenInternetDepositReconReport'caMCTotal& */	private Dollar caMCTotal = new Dollar("0");/* &GenInternetDepositReconReport'caTempTotal& */	private Dollar caTempTotal = new Dollar("0");/* &GenInternetDepositReconReport'caVISATotal& */	private Dollar caVISATotal = new Dollar("0");/* &GenInternetDepositReconReport'caDepositReconData& */	private InternetDepositReconData caDepositReconData = null;/* &GenInternetDepositReconReport'caRptSearchData& */	private ReportSearchData caRptSearchData = null;	// int /* &GenInternetDepositReconReport'ciBeginDate& */	private int ciBeginDate = 0;/* &GenInternetDepositReconReport'ciEndDate& */	private int ciEndDate = 0;/* &GenInternetDepositReconReport'ciNewDate& */	private int ciNewDate = 0;/* &GenInternetDepositReconReport'ciNumDays& */	private int ciNumDays = 0;/* &GenInternetDepositReconReport'ciPrevAMDate& */	private int ciPrevAMDate = 0;	// String /* &GenInternetDepositReconReport'csDateRangeHdr& */	private String csDateRangeHdr = "DEPOSIT DATE";/* &GenInternetDepositReconReport'csDateRangeTxt& */	private String csDateRangeTxt = new String();	// Vector /* &GenInternetDepositReconReport'cvDatesWithData& */	private Vector cvDatesWithData = new Vector();/* &GenInternetDepositReconReport'cvDatesWithNoData& */	private Vector cvDatesWithNoData = new Vector();/* &GenInternetDepositReconReport'cvDepositReconData& */	private Vector cvDepositReconData = new Vector();	// Constants /* &GenInternetDepositReconReport'AMOUNT_HDR& */	private static final String AMOUNT_HDR = "AMOUNT";/* &GenInternetDepositReconReport'CARD_NO_COL_LENGTH& */	private static final int CARD_NO_COL_LENGTH = 5;/* &GenInternetDepositReconReport'CARD_TYPE_HDR& */	private static final String CARD_TYPE_HDR = "CREDIT CARD TYPE";/* &GenInternetDepositReconReport'COL_CARD_LAST4& */	private static final int COL_CARD_LAST4 = 50;/* &GenInternetDepositReconReport'COL_CARD_LAST4_DETAIL& */	private static final int COL_CARD_LAST4_DETAIL = 57;/* &GenInternetDepositReconReport'COL_CARD_TOTAL& */	private static final int COL_CARD_TOTAL = 61;/* &GenInternetDepositReconReport'COL_CARD_TYPE& */	private static final int COL_CARD_TYPE = 1;/* &GenInternetDepositReconReport'COL_NO_DEPOSIT_DATES& */	private static final int COL_NO_DEPOSIT_DATES = 6;/* &GenInternetDepositReconReport'COL_PYMNT_AMT& */	private static final int COL_PYMNT_AMT = 81;/* &GenInternetDepositReconReport'COL_PYMNT_AMT_DETAIL& */	private static final int COL_PYMNT_AMT_DETAIL = 65;/* &GenInternetDepositReconReport'COL_TOL_TRANS_DATE& */	private static final int COL_TOL_TRANS_DATE = 100;/* &GenInternetDepositReconReport'COL_TRACE_NO& */	private static final int COL_TRACE_NO = 25;/* &GenInternetDepositReconReport'COL_UNDERSCORE& */	private static final int COL_UNDERSCORE = 77;/* &GenInternetDepositReconReport'CONTINUED_ABBR& */	private static final String CONTINUED_ABBR = "    (cont'd)";/* &GenInternetDepositReconReport'DAILY_TOTALS& */	private static final String DAILY_TOTALS = "DAILY TOTALS";/* &GenInternetDepositReconReport'DATES_WITH_NO_DEPOSITS& */	private static final String DATES_WITH_NO_DEPOSITS =		"DATES WITH NO DEPOSITS";/* &GenInternetDepositReconReport'DEFAULT_COL_LENGTH& */	private static final int DEFAULT_COL_LENGTH = 22;/* &GenInternetDepositReconReport'EXCEPTION_RPT& */	private static final String EXCEPTION_RPT = "EXCEPTION REPORT";/* &GenInternetDepositReconReport'EXCEPTION_UNDERSCORE& */	private static final String EXCEPTION_UNDERSCORE =		"----------------------";/* &GenInternetDepositReconReport'LAST4_OF_CARD_HDR& */	private static final String LAST4_OF_CARD_HDR =		"LAST 4 DIGITS OF CARD";/* &GenInternetDepositReconReport'MULTIPLE_SUFFIX& */	private static final String MULTIPLE_SUFFIX = "S";	// defect 1011	//private static final int EXCEPTION_DATE_LENGTH = 10; /* &GenInternetDepositReconReport'DATA_NOT_YET_PROVIDED_PROCESSED& */	private static final String DATA_NOT_YET_PROVIDED_PROCESSED =		"DATA NOT YET PROVIDED BY VENDOR";/* &GenInternetDepositReconReport'DATA_NOT_YET_PROVIDED_SYMBOL& */	private static final String DATA_NOT_YET_PROVIDED_SYMBOL = "   *";/* &GenInternetDepositReconReport'DATA_NOT_YET_PROVIDED_FOOTNOTE& */	private static final String DATA_NOT_YET_PROVIDED_FOOTNOTE =		"(*) " + DATA_NOT_YET_PROVIDED_PROCESSED;	// end defect 1011/* &GenInternetDepositReconReport'NUM_LINES_FOR_SUBTOTAL& */	private static final int NUM_LINES_FOR_SUBTOTAL = 5;/* &GenInternetDepositReconReport'NUM_LINES_FOR_TOTAL& */	private static final int NUM_LINES_FOR_TOTAL = 9;/* &GenInternetDepositReconReport'PYMNT_AMT_COL_LENGTH& */	private static final int PYMNT_AMT_COL_LENGTH = 15;/* &GenInternetDepositReconReport'TOL_TRANS_DATE_HDR& */	private static final String TOL_TRANS_DATE_HDR =		"TOL TRANSACTION DATE";/* &GenInternetDepositReconReport'TOP_OF_DETAIL_DATA_LINE& */	private static final int TOP_OF_DETAIL_DATA_LINE = 12;/* &GenInternetDepositReconReport'TOTAL& */	private static final String TOTAL = " TOTAL";/* &GenInternetDepositReconReport'TRACE_NO_HDR& */	private static final String TRACE_NO_HDR = "TRACE NUMBER";/* &GenInternetDepositReconReport'UNDERSCORE& */	private static final String UNDERSCORE = "----------";/* &GenInternetDepositReconReport'VISA_MC_CARD& */	private static final String VISA_MC_CARD = "VISA/MC";/* &GenInternetDepositReconReport'WRA_HDR_LENGTH& */	private static final int WRA_HDR_LENGTH = 21;/* &GenInternetDepositReconReport'WRA_HDR_STARTPT& */	private static final int WRA_HDR_STARTPT = 1;/* &GenInternetDepositReconReport'WRA_RSLTS_STARTPT& */	private static final int WRA_RSLTS_STARTPT = 22;/* &GenInternetDepositReconReport.main& */	public static void main(String[] args)	{	}	/**	 * GenInternetDepositReconReport constructor	 *//* &GenInternetDepositReconReport.GenInternetDepositReconReport& */	public GenInternetDepositReconReport()	{		super();	}	/**	 * GenInternetDepositReconReport constructor	 *//* &GenInternetDepositReconReport.GenInternetDepositReconReport$1& */	public GenInternetDepositReconReport(		String asRptString,		ReportProperties aaRptProperties,		ReportSearchData aaRptSearchData)	{		super(asRptString, aaRptProperties);		this.caRptSearchData = aaRptSearchData;		ciBeginDate = caRptSearchData.getDate1().getAMDate();		ciEndDate = caRptSearchData.getDate2().getAMDate();		ciNumDays = ciEndDate - ciBeginDate + 1;		csDateRangeTxt = caRptSearchData.getDate1().toString();		if (ciNumDays > 1)		{			csDateRangeHdr = csDateRangeHdr + MULTIPLE_SUFFIX;			csDateRangeTxt =				csDateRangeTxt					+ CommonConstant.STR_SPACE_ONE					+ GenFeeReport.THROUGH_HEADER_STRING					+ CommonConstant.STR_SPACE_ONE					+ caRptSearchData.getDate2().toString();		}	}	/**	 * Add Found Date 	 *//* &GenInternetDepositReconReport.addAsDateWithData& */	private void addAsDateWithData()	{		Integer liDate =			new Integer(				caDepositReconData.getBankDepositDate().getAMDate());		if (!cvDatesWithData.contains(liDate))		{			cvDatesWithData.addElement(liDate);		}	}	/**	 * Identify Dates Without Data	 *//* &GenInternetDepositReconReport.findDatesWithoutData& */	private void findDatesWithoutData()	{		for (int i = caRptSearchData.getDate1().getAMDate();			i <= caRptSearchData.getDate2().getAMDate();			i++)		{			Integer liDate = new Integer(i);			if (!cvDatesWithData.contains(liDate))			{				cvDatesWithNoData.add(liDate);			}		}	}	/**	 * Format Report Data 	 * 	 * @paramabPrintCardType 	 *//* &GenInternetDepositReconReport.formatData& */	private void formatData(boolean abPrintCardType)	{		if (caRpt.getCurrX() == TOP_OF_DETAIL_DATA_LINE			|| abPrintCardType)		{			String lsCardType = caDepositReconData.getPymntCardType();			lsCardType =				abPrintCardType					? lsCardType					: lsCardType + CONTINUED_ABBR;			caRpt.print(lsCardType, COL_CARD_TYPE, DEFAULT_COL_LENGTH);		}		// Trace Number 		caRpt.print(			caDepositReconData.getItrntTraceNo(),			COL_TRACE_NO,			DEFAULT_COL_LENGTH);		// Last 4 Card No  			caRpt.print(			caDepositReconData.getLast4PymntCardNo(),			COL_CARD_LAST4_DETAIL,			CARD_NO_COL_LENGTH);		// Amount  		caRpt.print(			UtilityMethods.addPadding(				caDepositReconData.getPymntAmt().toString(),				DEFAULT_COL_LENGTH,				CommonConstant.STR_SPACE_ONE),			COL_PYMNT_AMT_DETAIL,			DEFAULT_COL_LENGTH);		// TOL TransactionDate  		caRpt.print(			caDepositReconData.getTOLTransDate().getYYYYMMDDDate() + "",			COL_TOL_TRANS_DATE + 5,			DEFAULT_COL_LENGTH);		caRpt.blankLines(ReportConstant.NUM_LINES_1);		// Accumulate Results  		String lsPymntCardType = caDepositReconData.getPymntCardType();		Dollar laPymntAmt = caDepositReconData.getPymntAmt();		if (lsPymntCardType			.equals(RegRenProcessingConstants.AMERICANEXPRESS_ABBR))		{			caAMEXTotal = caAMEXTotal.add(laPymntAmt);		}		else if (			lsPymntCardType.equals(				RegRenProcessingConstants.DISCOVER_ABBR))		{			caDISCTotal = caDISCTotal.add(laPymntAmt);		}		else if (			lsPymntCardType.equals(				RegRenProcessingConstants.MASTERCARD_ABBR))		{			caMCTotal = caMCTotal.add(laPymntAmt);		}		else if (			lsPymntCardType.equals(				RegRenProcessingConstants.VISA_ABBR))		{			caVISATotal = caVISATotal.add(laPymntAmt);		}		caTempTotal = caTempTotal.add(laPymntAmt);		generatePageBreak(2);	}	/**	 * The formatReport() generates the Internet Deposit Recon Report	 *	 * @param avResults Vector	 *//* &GenInternetDepositReconReport.formatReport& */	public void formatReport(Vector avResults)	{		cvDepositReconData = avResults;		// Sort by CardType w/in Date 		UtilityMethods.sort(cvDepositReconData);		String lsPrevPymntCardType = new String();		String lsNewPymntCardType = new String();		try		{			for (int i = 0; i < avResults.size(); i++)			{				boolean lbPrintCardType = false;				caDepositReconData =					(InternetDepositReconData) avResults.elementAt(i);				lsNewPymntCardType =					caDepositReconData.getPymntCardType();				ciNewDate =					caDepositReconData.getBankDepositDate().getAMDate();				if (ciNewDate != ciPrevAMDate)				{					if (ciPrevAMDate != 0)					{						generateCardTypeTotal(							lsPrevPymntCardType,							caTempTotal,							true);						generateDateTotal();						generateFooter();					}					addAsDateWithData();					initAccumulators();					generateHeader();					lbPrintCardType = true;				}				else				{					if (!lsNewPymntCardType.equals(lsPrevPymntCardType)						& i						> 0)					{						generateCardTypeTotal(							lsPrevPymntCardType,							caTempTotal,							true);						lbPrintCardType = true;					}				}				lsPrevPymntCardType = lsNewPymntCardType;				formatData(lbPrintCardType);				ciPrevAMDate = ciNewDate;			}			// Check for no data returned 			if (avResults.size() == 0)			{				generateHeader();				// defect 10011 				generateNoDataOrNotProvidedMsg();				// end defect 10011				// defect 8682 				// generateEndOfReport();				// generateFooter();				generateFooter(true);				// end defect 8628 			}			else			{				generateCardTypeTotal(					lsNewPymntCardType,					caTempTotal,					true);				generateDateTotal();				findDatesWithoutData();								// defect 8628 				boolean lbException =					cvDatesWithNoData.size() > 0						&& cvDatesWithData.size() > 0						&& ciNumDays > 1;				generateFooter(!lbException);				if (lbException)				{					generateExceptionPage();					// generateFooter();					generateFooter(true);					// end defect 8628 				}			}		}		catch (Exception aeEx)		{			System.err.println(ReportConstant.RPT_8001_RPT_ERR);			aeEx.printStackTrace(System.out);			RTSException leRTSEx =				new RTSException(RTSException.JAVA_ERROR, aeEx);			leRTSEx.writeExceptionToLog();		}	}	/**	 * Currently not implemented	 *//* &GenInternetDepositReconReport.generateAttributes& */	public void generateAttributes()	{ // empty code block	}	/**	 * Prints the total number of transactions for a Date 	 *//* &GenInternetDepositReconReport.generateCardTypeTotal& */	private void generateCardTypeTotal(		String asCardType,		Dollar aaTotal,		boolean abInterim)	{		generatePageBreak(NUM_LINES_FOR_SUBTOTAL);		int liNumLines = abInterim ? 2 : 1;		String lsTotal =			UtilityMethods.addPadding(				asCardType,				11,				CommonConstant.STR_SPACE_ONE)				+ UtilityMethods.addPadding(					aaTotal.printDollar(),					PYMNT_AMT_COL_LENGTH,					CommonConstant.STR_SPACE_ONE);		if (abInterim)		{			caRpt.print(				UNDERSCORE,				COL_UNDERSCORE,				UNDERSCORE.length());			caRpt.blankLines(ReportConstant.NUM_LINES_1);		}		caRpt.print(lsTotal, COL_CARD_TOTAL, lsTotal.length());		caRpt.blankLines(liNumLines);		caTempTotal = new Dollar("0");	}	/**	 * Adds the column header text to the report	 *//* &GenInternetDepositReconReport.generateColumnHeaders& */	private void generateColumnHeaders()	{		// Card Type 		caRpt.print(			CARD_TYPE_HDR,			COL_CARD_TYPE,			CARD_TYPE_HDR.length());		// Trace Number 		caRpt.print(TRACE_NO_HDR, COL_TRACE_NO, TRACE_NO_HDR.length());		// Last 4 Card Number 			caRpt.print(			LAST4_OF_CARD_HDR,			COL_CARD_LAST4,			LAST4_OF_CARD_HDR.length());		// Amount 		caRpt.print(AMOUNT_HDR, COL_PYMNT_AMT, AMOUNT_HDR.length());		// TOL Transaction Date  		caRpt.print(			TOL_TRANS_DATE_HDR,			COL_TOL_TRANS_DATE,			TOL_TRANS_DATE_HDR.length());		caRpt.nextLine();		caRpt.drawDashedLine();	}	/**	 * Generate No Yet Provided Message	 *//* &GenInternetDepositReconReport.generateDataNotYetProvided& */	public void generateDataNotYetProvided()	{		caRpt.center("*********************************************");		caRpt.nextLine();		caRpt.center(			"****** " + DATA_NOT_YET_PROVIDED_PROCESSED + " ******");		caRpt.nextLine();		caRpt.center("*********************************************");		caRpt.nextLine();	}	/**	 * Prints the total number of transactions for a Date 	 *//* &GenInternetDepositReconReport.generateDateTotal& */	private void generateDateTotal()	{		generatePageBreak(NUM_LINES_FOR_TOTAL);		if (caRpt.getCurrX() != TOP_OF_DETAIL_DATA_LINE)		{			caRpt.blankLines(ReportConstant.NUM_LINES_1);		}		caRpt.print(DAILY_TOTALS, COL_TRACE_NO, DAILY_TOTALS.length());		// AMEX		generateCardTypeTotal(			RegRenProcessingConstants.AMERICANEXPRESS_ABBR,			caAMEXTotal,			false);		// DISC		generateCardTypeTotal(			RegRenProcessingConstants.DISCOVER_ABBR,			caDISCTotal,			false);		// VISA/MC 		generateCardTypeTotal(			VISA_MC_CARD,			caVISATotal.add(caMCTotal),			false);		caRpt.print(UNDERSCORE, COL_UNDERSCORE, UNDERSCORE.length());		Dollar laDateTotal =			caMCTotal.add(caDISCTotal).add(caVISATotal).add(				caAMEXTotal);		// TOTAL 				String lsTotal =			TOTAL				+ UtilityMethods.addPadding(					laDateTotal.printDollar(),					PYMNT_AMT_COL_LENGTH,					CommonConstant.STR_SPACE_ONE);		caRpt.blankLines(ReportConstant.NUM_LINES_1);		caRpt.print(			lsTotal,			COL_PYMNT_AMT_DETAIL + 1,			lsTotal.length());	}	/**	 * Prints the list of dates with no data returned	 * 	 *//* &GenInternetDepositReconReport.generateExceptionPage& */	private void generateExceptionPage()	{		if (cvDatesWithNoData.size() > 0)		{			caRpt.print(caRptProps.getUniqueName());			caRpt.center(caRpt.csName);			caRpt.center(caRptProps.getOfficeIssuanceName());			caRpt.center(EXCEPTION_RPT);			generateReportCriteriaHeader();			caRpt.print(DATES_WITH_NO_DEPOSITS);			caRpt.nextLine();			caRpt.print(EXCEPTION_UNDERSCORE);			caRpt.blankLines(ReportConstant.NUM_LINES_1);			DatabaseAccess laDBA = new DatabaseAccess();			boolean lbNoDataFromVendor = false;			for (int i = 0; i < cvDatesWithNoData.size(); i++)			{				int liDate =					((Integer) cvDatesWithNoData.get(i)).intValue();				String lsDate =					new RTSDate(RTSDate.AMDATE, liDate).toString();				// defect 10011				// Add "*" if data has not been successfully 				//  processed from the Vendor  				boolean lbCollected = isDataProvided(lsDate, laDBA);				lbNoDataFromVendor = lbNoDataFromVendor || !lbCollected;				String lsPrint =					lbCollected						? lsDate						: lsDate + DATA_NOT_YET_PROVIDED_SYMBOL;				caRpt.print(					lsPrint,					COL_NO_DEPOSIT_DATES,					lsPrint.length());				caRpt.nextLine();				// end defect 10011 			}			laDBA = null;			caRpt.nextLine();			// defect 10011 			// Add a footnote to explain the "*" 			if (lbNoDataFromVendor)			{				caRpt.print(DATA_NOT_YET_PROVIDED_FOOTNOTE);			}			// end defect 10011			// defect 8628  			// generateEndOfReport();			// end defect 8628 		}	}	/**	 * The generateHeader() adds text that will be printed when needed	 *//* &GenInternetDepositReconReport.generateHeader& */	private void generateHeader()	{		OfficeIdsData caOfcIds =			OfficeIdsCache.getOfcId(caRptSearchData.getIntKey1());		String csRptOfcName = caOfcIds.getOfcName();		caRpt.print(caRptProps.getUniqueName());		caRpt.center(caRpt.csName);		caRpt.center(caRptProps.getOfficeIssuanceName());		if (ciNewDate == 0)		{			csRptOfcName = EXCEPTION_RPT;		}		else		{			csRptOfcName =				new RTSDate(RTSDate.AMDATE, ciNewDate).toString();		}		caRpt.center(csRptOfcName);		caRpt.blankLines(ReportConstant.NUM_LINES_1);		generateReportCriteriaHeader();		generateColumnHeaders();	}	/**	 * Generate "NO DATA FOUND" or "DATA NOT YET PROVIDED BY VENDOR" msg	 *	 *//* &GenInternetDepositReconReport.generateNoDataOrNotProvidedMsg& */	private void generateNoDataOrNotProvidedMsg()	{		DatabaseAccess laDBA = new DatabaseAccess();		String lsDate =			new RTSDate(RTSDate.AMDATE, ciBeginDate).toString();		if (isDataProvided(lsDate, laDBA))		{			generateNoRecordFoundMsg();		}		else		{			generateDataNotYetProvided();		}		laDBA = null;	}	/**	 * Generate the page break with lines required	 * 	 * @param aiLinesRequired int	 *//* &GenInternetDepositReconReport.generatePageBreak& */	private void generatePageBreak(int aiLinesRequired)	{		if (getNoOfDetailLines() < aiLinesRequired)		{			generateFooter();			generateHeader();		}	}	/**	 * Adds the report criteria header text to the report	 *//* &GenInternetDepositReconReport.generateReportCriteriaHeader& */	private void generateReportCriteriaHeader()	{		// Workstation Id 		caRpt.print(			ReportConstant.WORKSTATION_ID,			WRA_HDR_STARTPT,			WRA_HDR_LENGTH);		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getWorkstationId(),			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getWorkstationId())				.length());		caRpt.nextLine();		// Requested By 		caRpt.print(			ReportConstant.REQUESTED_BY,			WRA_HDR_STARTPT,			WRA_HDR_LENGTH);		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getRequestedBy(),			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getRequestedBy())				.length());		// Date Range 		caRpt.nextLine();		caRpt.print(csDateRangeHdr, WRA_HDR_STARTPT, WRA_HDR_LENGTH);		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ csDateRangeTxt,			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ csDateRangeTxt)				.length());		caRpt.nextLine();		caRpt.blankLines(ReportConstant.NUM_LINES_2);	}	/** 	 * Initialize Accumulators 	 *	 *//* &GenInternetDepositReconReport.initAccumulators& */	private void initAccumulators()	{		caAMEXTotal = new Dollar("0");		caDISCTotal = new Dollar("0");		caMCTotal = new Dollar("0");		caVISATotal = new Dollar("0");	}	/**	 * Is Data Provided	 * 	 * @param asDate	 * @param aaDBA 	 * @return boolean	 *//* &GenInternetDepositReconReport.isDataProvided& */	private boolean isDataProvided(String asDate, DatabaseAccess aaDBA)	{		boolean lbProvided = false;		try		{			InternetDepositReconHstry laItrntDepReconHstry =				new InternetDepositReconHstry(aaDBA);			aaDBA.beginTransaction();			lbProvided =				laItrntDepReconHstry.qryBatchDepositDate(asDate);			aaDBA.endTransaction(DatabaseAccess.COMMIT);		}		catch (RTSException aeRTSEx)		{			try			{				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);				aeRTSEx.printStackTrace();			}			catch (RTSException aeRTSEx1)			{				aeRTSEx1.printStackTrace();			}		}		return lbProvided;	}	/**	 * Currently not implemented	 *//* &GenInternetDepositReconReport.queryData& */	public Vector queryData(String asQuery)	{		return null;	}}/* #GenInternetDepositReconReport# */