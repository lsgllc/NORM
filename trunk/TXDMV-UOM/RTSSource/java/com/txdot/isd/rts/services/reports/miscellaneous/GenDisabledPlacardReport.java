package com.txdot.isd.rts.services.reports.miscellaneous;import java.util.Vector;import com.txdot.isd.rts.services.cache.OfficeIdsCache;import com.txdot.isd.rts.services.data.DisabledPlacardExportReportData;import com.txdot.isd.rts.services.data.DisabledPlacardUIData;import com.txdot.isd.rts.services.data.OfficeIdsData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.reports.ReportProperties;import com.txdot.isd.rts.services.reports.ReportTemplate;import com.txdot.isd.rts.services.reports.funds.GenFeeReport;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.CommonConstant;import com.txdot.isd.rts.services.util.constants.ReportConstant;/* * GenDisabledPlacardReport.java * * (c) Texas Department of Transportation 2008 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	10/26/2008	Created.  * 							defect 9831 Ver Defect_POS_B * K Harrell	12/30/2008	If not "All Types", Trim Application Type  * 							 in case "ADD" not included. * 							modify generateReportCriteriaHeader()  * 							defect 9874 Ver Defect_POS_D * 							modify formatReport() * 							defect 8628 Ver Defect_POS_F  * K Harrell	10/11/2011	Modify to more gracefully handle >2 placards * 							per transaction * 							add printPlacardData(), printPlacardIfSameTrans() * 							modify formatData(), formatReport(),  * 							 generatePageBreak() * 							defect 11050 Ver 6.9.0    * K Harrell	02/06/2012	"Empty Report" on No Record Found  * 							modify formatData()  * 							defect 11243 Ver 6.10.0   * K Harrell	02/06/2012	Show Delete Reason for Delete Transactions  * 							add DELETE_REASON * 							modify formatData()  * 							defect 11279 Ver 6.10.0 * K Harrell 	02/20/2012	Use 'Rei' for Reinstate vs. 'Add' * 							Collect Reinstate Trans/Rpt Total.  * 							Right align totals. Implement new Constants.  * 							add ciReinstateTotal, ciRptReinstateTotal   * 							add DP_REINSTATE_TOTAL_TXT, ADD_TRANS_TYPE, *							 DELETE_TRANS_TYPE, REINSTATE_TRANS_TYPE  * 							delete COL_RPT_TOTAL_LENGTH * 							modify generateRptTotal(), generateTransTotal(),  * 							  printPlacardData()  * 							modify COL_PLACARD_NO_STARTPT  * 							defect 11214 Ver 6.10.0   * --------------------------------------------------------------------- *//** * Generates the Disabled Placard Report * * @version	6.10.0			02/20/2012 * @author	Kathy Harrell * <br>Creation Date:		10/26/2008 *//* &GenDisabledPlacardReport& */public class GenDisabledPlacardReport extends ReportTemplate{/* &GenDisabledPlacardReport'caDPRptData& */	private DisabledPlacardExportReportData caDPRptData = null;/* &GenDisabledPlacardReport'caDPUIData& */	private DisabledPlacardUIData caDPUIData = null;/* &GenDisabledPlacardReport'cvDPRptData& */	private Vector cvDPRptData = new Vector();/* &GenDisabledPlacardReport'cvOfcWithData& */	private Vector cvOfcWithData = new Vector();/* &GenDisabledPlacardReport'cvOfcIdWithNoData& */	private Vector cvOfcIdWithNoData = new Vector();/* &GenDisabledPlacardReport'csOfcId& */	private String csOfcId;/* &GenDisabledPlacardReport'csOfcIssuanceNo& */	private String csOfcIssuanceNo;		// defect 10050 /* &GenDisabledPlacardReport'ciRowNum& */	private int ciRowNum = 0;	// end defect 10050	/* &GenDisabledPlacardReport'ciAddTotal& */	private int ciAddTotal = 0;/* &GenDisabledPlacardReport'ciDeleteTotal& */	private int ciDeleteTotal = 0;/* &GenDisabledPlacardReport'ciRptAddTotal& */	private int ciRptAddTotal = 0;/* &GenDisabledPlacardReport'ciRptDeleteTotal& */	private int ciRptDeleteTotal = 0;		// defect 11214 /* &GenDisabledPlacardReport'ciReinstateTotal& */	private int ciReinstateTotal = 0; /* &GenDisabledPlacardReport'ciRptReinstateTotal& */	private int ciRptReinstateTotal = 0;	// end defect 11214 	/* &GenDisabledPlacardReport'ciOfcSize& */	private int ciOfcSize = 0;/* &GenDisabledPlacardReport'ciRptDataSize& */	private int ciRptDataSize = 0;/* &GenDisabledPlacardReport'COL_TRANSCD_STARTPT& */	private static final int COL_TRANSCD_STARTPT = 1;/* &GenDisabledPlacardReport'COL_ACTN_STARTPT& */	private static final int COL_ACTN_STARTPT = 11;	// defect 11214 	//private static final int COL_PLACARD_NO_STARTPT = 19;/* &GenDisabledPlacardReport'COL_PLACARD_NO_STARTPT& */	private static final int COL_PLACARD_NO_STARTPT = 18;	// end defect 11214/* &GenDisabledPlacardReport'COL_EXP_DATE_STARTPT& */	private static final int COL_EXP_DATE_STARTPT = 30;/* &GenDisabledPlacardReport'COL_PLACARD_DESC_STARTPT& */	private static final int COL_PLACARD_DESC_STARTPT = 40;/* &GenDisabledPlacardReport'COL_OWNER_STARTPT& */	private static final int COL_OWNER_STARTPT = 72;/* &GenDisabledPlacardReport'COL_ADDRESS_STARTPT& */	private static final int COL_ADDRESS_STARTPT = 105;/* &GenDisabledPlacardReport'COL_ADDRESS_LENGTH& */	private static final int COL_ADDRESS_LENGTH = 30;/* &GenDisabledPlacardReport'COL_TRANSID_STARTPT& */	private static final int COL_TRANSID_STARTPT = 147;/* &GenDisabledPlacardReport'COL_EMPLOYEE_STARTPT& */	private static final int COL_EMPLOYEE_STARTPT = 137;/* &GenDisabledPlacardReport'COL_TRANSCNT_STARTPT& */	private static final int COL_TRANSCNT_STARTPT = 116;/* &GenDisabledPlacardReport'COL_TRANSCNT_LENGTH& */	private static final int COL_TRANSCNT_LENGTH = 18;/* &GenDisabledPlacardReport'COL_TRANSTTL_STARTPT& */	private static final int COL_TRANSTTL_STARTPT = 85;/* &GenDisabledPlacardReport'COL_TRANSTTL_LENGTH& */	private static final int COL_TRANSTTL_LENGTH = 10;/* &GenDisabledPlacardReport'COL_RPTTOTAL_STARTPT& */	private static final int COL_RPTTOTAL_STARTPT = 68;	// defect 11214 	//private static final int COL_RPTTOTAL_LENGTH = 13;	// end defect 11214 /* &GenDisabledPlacardReport'WRA_HDR_STARTPT& */	private static final int WRA_HDR_STARTPT = 1;/* &GenDisabledPlacardReport'WRA_HDR_LENGTH& */	private static final int WRA_HDR_LENGTH = 21;/* &GenDisabledPlacardReport'WRA_RSLTS_STARTPT& */	private static final int WRA_RSLTS_STARTPT = 22;/* &GenDisabledPlacardReport'TRANS_TYPE& */	private final String TRANS_TYPE = "TRANSACTION TYPE";/* &GenDisabledPlacardReport'TXT_ALL& */	private final String TXT_ALL = "ALL";/* &GenDisabledPlacardReport'TXT_EXCEPTION& */	private final String TXT_EXCEPTION = "EXCEPTION";/* &GenDisabledPlacardReport'DP_TRANS_HDR& */	private static final String DP_TRANS_HDR = "TRANSCD";/* &GenDisabledPlacardReport'DP_ACTN& */	private static final String DP_ACTN = "ACTN";/* &GenDisabledPlacardReport'DP_PLCRDNO_HDR& */	private static final String DP_PLCRDNO_HDR = "PLACARD";/* &GenDisabledPlacardReport'DP_EXP_DATE& */	private static final String DP_EXP_DATE = "EXP DATE";/* &GenDisabledPlacardReport'DP_PLCRD_DESC_HDR& */	private static final String DP_PLCRD_DESC_HDR =		"PLACARD DESCRIPTION";/* &GenDisabledPlacardReport'DP_OWNR_NAME_HDR& */	private static final String DP_OWNR_NAME_HDR =		"DISABLED PERSON / INSTITUTION";/* &GenDisabledPlacardReport'DP_ADDRESS_HDR& */	private static final String DP_ADDRESS_HDR = "ADDRESS";/* &GenDisabledPlacardReport'DP_TRANSID_HDR& */	private static final String DP_TRANSID_HDR = "TRANSACTION ID";/* &GenDisabledPlacardReport'DP_EMPID_HDR& */	private static final String DP_EMPID_HDR = "EMPLOYEE";/* &GenDisabledPlacardReport'DP_ADD_TOTAL_TXT& */	private static final String DP_ADD_TOTAL_TXT = "ADD TOTAL:";/* &GenDisabledPlacardReport'DP_DELETE_TOTAL_TXT& */	private static final String DP_DELETE_TOTAL_TXT = "DELETE TOTAL:";		// defect 11214 /* &GenDisabledPlacardReport'DP_REINSTATE_TOTAL_TXT& */	private static final String DP_REINSTATE_TOTAL_TXT = "REINSTATE TOTAL:";/* &GenDisabledPlacardReport'ADD_TRANS_TYPE& */	private static final int ADD_TRANS_TYPE = 0;/* &GenDisabledPlacardReport'DELETE_TRANS_TYPE& */	private static final int DELETE_TRANS_TYPE = 1;/* &GenDisabledPlacardReport'REINSTATE_TRANS_TYPE& */	private static final int REINSTATE_TRANS_TYPE = 2;	// end defect 11214		// defect 11279/* &GenDisabledPlacardReport'DELETE_REASON& */	private static final String DELETE_REASON = "DELETE REASON: "; 	// end defect 11279 	/**	 * GenDisabledPlacardReport constructor	 *//* &GenDisabledPlacardReport.GenDisabledPlacardReport& */	public GenDisabledPlacardReport()	{		super();	}	/**	 * GenDisabledPlacardReport constructor	 *//* &GenDisabledPlacardReport.GenDisabledPlacardReport$1& */	public GenDisabledPlacardReport(		String asRptString,		ReportProperties aaRptProperties,		DisabledPlacardUIData aaUIData)	{		super(asRptString, aaRptProperties);		this.caDPUIData = aaUIData;	}/* &GenDisabledPlacardReport.main& */	public static void main(String[] args)	{	}	/**	 * The formatReport() generates the Disabled Placard Report.	 *	 * @param avResults Vector	 *//* &GenDisabledPlacardReport.formatReport& */	public void formatReport(Vector avResults)	{		cvDPRptData = avResults;		ciOfcSize = caDPUIData.getSelectedOffices().size();		ciRptDataSize = cvDPRptData.size();		String lsPrevOfcIssuanceNo = "";		csOfcIssuanceNo =			caDPUIData.getSelectedOffices().elementAt(0).toString();		try		{				// defect 11050				// defect 11243 				// Replace do/while w/ while to include case where no rcd fd 				// do				while (cvDPRptData.size()!= 0 && cvDPRptData.size() > ciRowNum) 				{					// end defect 11243 										generatePageBreak(2); 					caDPRptData =						(								DisabledPlacardExportReportData) cvDPRptData								.elementAt(										ciRowNum);					csOfcIssuanceNo =						String.valueOf(caDPRptData.getOfcIssuanceNo());					if (csOfcIssuanceNo == null							|| !csOfcIssuanceNo.equals(lsPrevOfcIssuanceNo))					{						csOfcId = csOfcIssuanceNo;						if (lsPrevOfcIssuanceNo != "")						{							generateTransTotal();							generateFooter();						}						addAsOfficeWithData();						generateHeader();					}					formatData();					lsPrevOfcIssuanceNo = csOfcIssuanceNo;					ciRowNum = ciRowNum +1;				}				// defect 11243 				// while (ciRowNum<cvDPRptData.size());				// end defect 11243				// end defect 11050						findOfficesWithoutData();			// Check for no data returned for office(s) selected			if (ciRptDataSize == 0)			{				// Single page when only one office selected				if (ciOfcSize == 1)				{					generateHeader();					generateNoRecordFoundMsg();					generateFooter(true);				}			}			else			{				// defect 11050 				generatePageBreak(ReportConstant.NUM_LINES_2);				// end defect 10050 								generateTransTotal();				generateFooter(ciOfcSize == 1);			}			// Create exception page when more than one office selected			// and no data returned for at least one of the selections			if (cvOfcIdWithNoData.size() > 0 && ciOfcSize > 1)			{				generateExceptionPage();			}			// Finish report when one office selected and data returned			if (ciOfcSize > 1 && ciRptDataSize > 0)			{				generateRptTotal();				generateFooter(true);			}		}		catch (Exception aeEx)		{			System.err.println(ReportConstant.RPT_8001_RPT_ERR);			aeEx.printStackTrace(System.out);			RTSException leRTSEx =				new RTSException(RTSException.JAVA_ERROR, aeEx);			leRTSEx.writeExceptionToLog();		}	}	/**	 * 	 * Identify Offices Without Data	 * 	 *//* &GenDisabledPlacardReport.findOfficesWithoutData& */	private void findOfficesWithoutData()	{		for (int i = 0; i < ciOfcSize; i++)		{			csOfcId =				((caDPUIData.getSelectedOffices())					.elementAt(i)					.toString());			if (!cvOfcWithData.contains(csOfcId))			{				cvOfcIdWithNoData.add(csOfcId);			}		}	}	/**	 * Prints the list of offices with no data returned	 * 	 *//* &GenDisabledPlacardReport.generateExceptionPage& */	private void generateExceptionPage()	{		if (cvOfcIdWithNoData.size() > 0)		{			caRpt.print(caRptProps.getUniqueName());			caRpt.center(caRpt.csName);			caRpt.center(TXT_EXCEPTION);			generateReportCriteriaHeader();			caRpt.print("NO RECORDS FOUND FOR OFFICES: ");			caRpt.blankLines(ReportConstant.NUM_LINES_1);			int liColStartPt = 1;			for (int i = 0; i < cvOfcIdWithNoData.size(); i++)			{				if (liColStartPt > 130)				{					caRpt.nextLine();					liColStartPt = 1;				}				String lsOfcId = cvOfcIdWithNoData.get(i).toString();				OfficeIdsData caOfcIds =					OfficeIdsCache.getOfcId(Integer.parseInt(lsOfcId));				String csRptOfcName = caOfcIds.getOfcName();				caRpt.print(					csRptOfcName,					liColStartPt,					csRptOfcName.length());				if (i != cvOfcIdWithNoData.size() - 1)				{					caRpt.print(CommonConstant.STR_COMMA);				}				liColStartPt = liColStartPt + csRptOfcName.length() + 3;			}			generateFooter(cvOfcIdWithNoData.size() == ciOfcSize);		}	}	/**	 * Prints the total number of transactions	 *//* &GenDisabledPlacardReport.generateRptTotal& */	private void generateRptTotal()	{		// defect 11214 		// Add logic for RptReinstate; Right align totals. 		if (ciRptAddTotal > 0 || ciRptDeleteTotal > 0 || ciRptReinstateTotal >0)		{			caRpt.print(caRptProps.getUniqueName());			caRpt.center(caRpt.csName);			caRpt.center(ReportConstant.RPT_8001_SUMMARY);			generateReportCriteriaHeader();			caRpt.blankLines(ReportConstant.NUM_LINES_2);						// Use 			caRpt.print(				DP_ADD_TOTAL_TXT,				COL_RPTTOTAL_STARTPT,				DP_ADD_TOTAL_TXT.length());			caRpt.rightAlign(				"" + ciRptAddTotal,				COL_TRANSTTL_STARTPT,				COL_TRANSTTL_LENGTH);			caRpt.blankLines(ReportConstant.NUM_LINES_2);			caRpt.print(				DP_DELETE_TOTAL_TXT,				COL_RPTTOTAL_STARTPT,				DP_DELETE_TOTAL_TXT.length());			caRpt.rightAlign(				"" + ciRptDeleteTotal,				COL_TRANSTTL_STARTPT,				COL_TRANSTTL_LENGTH);						// Only HQ can Reinstate 			if (OfficeIdsCache.isHQ(caRptProps.getOfficeIssuanceId()))			{				caRpt.blankLines(ReportConstant.NUM_LINES_2);				caRpt.print(						DP_REINSTATE_TOTAL_TXT,						COL_RPTTOTAL_STARTPT,						DP_REINSTATE_TOTAL_TXT.length());				caRpt.rightAlign(						"" + ciRptReinstateTotal,						COL_TRANSTTL_STARTPT,						COL_TRANSTTL_LENGTH);			}		}		// end defect 11214	}	/**	 * Prints the total number of transactions for an office	 * 	 *//* &GenDisabledPlacardReport.generateTransTotal& */	private void generateTransTotal()	{		// defect 11214 		// Add logic for TransReinstate; Right align totals. 		if (ciAddTotal > 0)		{			caRpt.blankLines(ReportConstant.NUM_LINES_1);			caRpt.print(				DP_ADD_TOTAL_TXT,				COL_TRANSCNT_STARTPT,				COL_TRANSCNT_LENGTH);			//caRpt.print(			caRpt.rightAlign(				"" + ciAddTotal,				COL_TRANSTTL_STARTPT,				COL_TRANSTTL_LENGTH);			caRpt.nextLine();			ciRptAddTotal = ciRptAddTotal + ciAddTotal;			ciAddTotal = 0;		}		if (ciDeleteTotal > 0)		{			caRpt.blankLines(ReportConstant.NUM_LINES_1);			caRpt.print(				DP_DELETE_TOTAL_TXT,				COL_TRANSCNT_STARTPT,				COL_TRANSCNT_LENGTH);			//caRpt.print(			caRpt.rightAlign(				"" + ciDeleteTotal,				COL_TRANSTTL_STARTPT,				COL_TRANSTTL_LENGTH);			caRpt.nextLine();			ciRptDeleteTotal = ciRptDeleteTotal + ciDeleteTotal;			ciDeleteTotal = 0;		}		if (ciReinstateTotal > 0)		{			caRpt.blankLines(ReportConstant.NUM_LINES_1);			caRpt.print(				DP_REINSTATE_TOTAL_TXT,				COL_TRANSCNT_STARTPT,				COL_TRANSCNT_LENGTH);			//caRpt.print(			caRpt.rightAlign(				"" + ciReinstateTotal,				COL_TRANSTTL_STARTPT,				COL_TRANSTTL_LENGTH);			caRpt.nextLine();			ciRptReinstateTotal = ciRptReinstateTotal+ ciReinstateTotal;			ciReinstateTotal = 0;		}		// end defect 11214 	}	/**	 * The generateHeader() adds text that will be printed when needed	 *//* &GenDisabledPlacardReport.generateHeader& */	private void generateHeader()	{		OfficeIdsData caOfcIds =			OfficeIdsCache.getOfcId(Integer.parseInt(csOfcIssuanceNo));		String csRptOfcName = caOfcIds.getOfcName();		caRpt.print(caRptProps.getUniqueName());		caRpt.center(caRpt.csName);		caRpt.center(caRptProps.getOfficeIssuanceName());		if (caRptProps.getOfficeIssuanceName().equals(csRptOfcName))		{			csRptOfcName = ReportConstant.RPT_8001_SUMMARY;		}		caRpt.center(csRptOfcName);		caRpt.blankLines(ReportConstant.NUM_LINES_1);		generateReportCriteriaHeader();		generateColumnHeaders();	}	/**	 * Adds the report criteria header text to the report	 * 	 *//* &GenDisabledPlacardReport.generateReportCriteriaHeader& */	private void generateReportCriteriaHeader()	{		caRpt.print(			ReportConstant.WORKSTATION_ID,			WRA_HDR_STARTPT,			WRA_HDR_LENGTH);		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getWorkstationId(),			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getWorkstationId())				.length());		caRpt.nextLine();		caRpt.print(			ReportConstant.REQUESTED_BY,			WRA_HDR_STARTPT,			WRA_HDR_LENGTH);		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getRequestedBy(),			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caRptProps.getRequestedBy())				.length());		caRpt.nextLine();		caRpt.print(TRANS_TYPE, WRA_HDR_STARTPT, WRA_HDR_LENGTH);		String lsApplType = "";		if (!caDPUIData.isAddTrans()			|| !caDPUIData.isDelTrans()			|| !caDPUIData.isRplTrans()			|| !caDPUIData.isReiTrans()			|| !caDPUIData.isRenTrans())		{			lsApplType = caDPUIData.isAddTrans() ? "ADD," : "";			lsApplType =				caDPUIData.isDelTrans()					? lsApplType + " DELETE,"					: lsApplType;			lsApplType =				caDPUIData.isRplTrans()					? lsApplType + " REPLACE,"					: lsApplType;			lsApplType =				caDPUIData.isRenTrans()					? lsApplType + " RENEW,"					: lsApplType;			lsApplType =				caDPUIData.isReiTrans()					? lsApplType + " REINSTATE,"					: lsApplType;			lsApplType =				lsApplType.substring(0, lsApplType.length() - 1);			// defect 9874			// Trim spaces in case "ADD" not included			lsApplType = lsApplType.trim();			// end defect 9874		}		else		{			lsApplType = TXT_ALL;		}		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ lsApplType,			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ lsApplType)				.length());		caRpt.nextLine();		caRpt.print(			GenFeeReport.TRANSACTION_HEADER_STRING,			WRA_HDR_STARTPT,			WRA_HDR_LENGTH);		caRpt.print(			CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caDPUIData.getBeginDate()				+ CommonConstant.STR_SPACE_ONE				+ GenFeeReport.THROUGH_HEADER_STRING				+ CommonConstant.STR_SPACE_ONE				+ caDPUIData.getEndDate(),			WRA_RSLTS_STARTPT,			(CommonConstant.STR_COLON				+ CommonConstant.STR_SPACE_ONE				+ caDPUIData.getBeginDate()				+ CommonConstant.STR_SPACE_ONE				+ GenFeeReport.THROUGH_HEADER_STRING				+ CommonConstant.STR_SPACE_ONE				+ caDPUIData.getEndDate())				.length());		caRpt.nextLine();		caRpt.blankLines(ReportConstant.NUM_LINES_2);	}	/**	 * Adds the column header text to the report	 * 	 * 	 *//* &GenDisabledPlacardReport.generateColumnHeaders& */	private void generateColumnHeaders()	{		// TransCd 		caRpt.print(			DP_TRANS_HDR,			COL_TRANSCD_STARTPT,			DP_TRANS_HDR.length());		// Action 			caRpt.print(DP_ACTN, COL_ACTN_STARTPT, DP_ACTN.length());		// Placard No 		caRpt.print(			DP_PLCRDNO_HDR,			COL_PLACARD_NO_STARTPT,			DP_PLCRDNO_HDR.length());		// Exp Date 		caRpt.print(			DP_EXP_DATE,			COL_EXP_DATE_STARTPT,			DP_EXP_DATE.length());		// Placard Description 		caRpt.print(			DP_PLCRD_DESC_HDR,			COL_PLACARD_DESC_STARTPT,			DP_PLCRD_DESC_HDR.length());		// Owner Name 						caRpt.print(			DP_OWNR_NAME_HDR,			COL_OWNER_STARTPT,			DP_OWNR_NAME_HDR.length());		// Owner Address 		caRpt.print(			DP_ADDRESS_HDR,			COL_ADDRESS_STARTPT,			DP_ADDRESS_HDR.length());		// Employee 		caRpt.print(			DP_EMPID_HDR,			COL_EMPLOYEE_STARTPT,			DP_EMPID_HDR.length());		// Transaction Id 		caRpt.print(			DP_TRANSID_HDR,			COL_TRANSID_STARTPT,			DP_TRANSID_HDR.length());		caRpt.nextLine();		caRpt.drawDashedLine();	}	/**	 * Currently not implemented	 *//* &GenDisabledPlacardReport.generateAttributes& */	public void generateAttributes()	{		// empty code block	}	/**	 * Format Report Data	 *  	 *//* &GenDisabledPlacardReport.formatData& */	private void formatData()	{		// defect 11050		// Reorganize to handle more than 2 placards per transaction 		printPlacardData();				// Owner Name 						caRpt.print(			caDPRptData.getOwnerNameShort(),			COL_OWNER_STARTPT,			caDPRptData.getOwnerNameShort().length());		Vector lvAddress =			caDPRptData.getAddressData().getAddressVector(false);		String lsAddress = (String) lvAddress.elementAt(0);		// Owner Address 		caRpt.print(lsAddress, COL_ADDRESS_STARTPT, COL_ADDRESS_LENGTH);		// Employee 		caRpt.print(			caDPRptData.getEmpId(),			COL_EMPLOYEE_STARTPT,			caDPRptData.getEmpId().length());		// Transaction Id 		caRpt.print(			caDPRptData.getTransId(),			COL_TRANSID_STARTPT,			caDPRptData.getTransId().length());		if (!generatePageBreak(ReportConstant.NUM_LINES_2))		{			caRpt.nextLine();		}				printPlacardIfSameTrans(false);				// defect 11279 		// Print Delete Reason for Delete Placard TransCds 		if (caDPRptData.isDeletePlacardTransaction() && 		!UtilityMethods.isEmpty(caDPRptData.getDelReasnDesc()))		{			String lsDeleteReason = DELETE_REASON + caDPRptData.getDelReasnDesc(); 			caRpt.print(					lsDeleteReason,				COL_PLACARD_DESC_STARTPT,				lsDeleteReason.length());		}		// end defect 11279 		lsAddress = (String) lvAddress.elementAt(1);		caRpt.print(lsAddress, COL_ADDRESS_STARTPT, lsAddress.length());				if (lvAddress.size() > 2)		{				caRpt.nextLine();								printPlacardIfSameTrans(false);				lsAddress = (String) lvAddress.elementAt(2);					caRpt.print(					lsAddress,					COL_ADDRESS_STARTPT,					lsAddress.length());		}		caRpt.blankLines(ReportConstant.NUM_LINES_1);				boolean lbContinue = true; 		do		{			lbContinue = printPlacardIfSameTrans(true);		}		while (lbContinue);		if (!generatePageBreak(ReportConstant.NUM_LINES_2))		{			caRpt.blankLines(ReportConstant.NUM_LINES_1);		}		// end defect 11050 	}	/**	 * Generate the page break with lines required	 * 	 * @param aiLinesRequired int	 *//* &GenDisabledPlacardReport.generatePageBreak& */	private boolean generatePageBreak(int aiLinesRequired)	{		// defect 11050 		boolean lbPageBreak = false; 		if (getNoOfDetailLines() < aiLinesRequired)		{			generateFooter();			generateHeader();			lbPageBreak = true; 		}		return lbPageBreak;		// end defect 11050 	}		/**	 * Add Found ResComptCntyNo, OfcIssuanceNo 	 *//* &GenDisabledPlacardReport.addAsOfficeWithData& */	private void addAsOfficeWithData()	{		String lsResComptCntyNo =			String.valueOf(caDPRptData.getResComptCntyNo());		String lsOfcIssuanceNo =			String.valueOf(caDPRptData.getOfcIssuanceNo());		if (!cvOfcWithData.contains(lsResComptCntyNo))		{			cvOfcWithData.addElement(lsResComptCntyNo);		}		if (!cvOfcWithData.contains(lsOfcIssuanceNo))		{			cvOfcWithData.addElement(lsOfcIssuanceNo);		}	}	/**	 * Currently not implemented	 *//* &GenDisabledPlacardReport.queryData& */	public Vector queryData(String asQuery)	{		return null;	}	/** * Print Placard info  *  * @param aaDPRptData *//* &GenDisabledPlacardReport.printPlacardData& */	private void printPlacardData()	{		String lsPlcrdDesc = caDPRptData.getAcctItmCdDesc();		if (caDPRptData.getVoidedIndi() == 1)		{			lsPlcrdDesc = ReportConstant.STR_VOIDED;		}		// defect 11214		// Add logic for Reinstate, implement constants 		else if (caDPRptData.getTransTypeCd() == DELETE_TRANS_TYPE)		{			ciDeleteTotal++;		}		else if (caDPRptData.getTransTypeCd() == ADD_TRANS_TYPE)		{			ciAddTotal++;		}		else if (caDPRptData.getTransTypeCd() == REINSTATE_TRANS_TYPE)		{			ciReinstateTotal++; 		}				// TransCd 		caRpt.print(caDPRptData.getTransCd(), COL_TRANSCD_STARTPT, 10);		// Action 		//String lsActn =		//	caDPRptData.getTransTypeCd() == 1 ? "Del" : "Add";		String lsActn = new String(); 		switch (caDPRptData.getTransTypeCd())		{		case ADD_TRANS_TYPE:		{			lsActn = "Add"; 			break; 		}		case DELETE_TRANS_TYPE:		{			lsActn = "Del";			break; 		}		case REINSTATE_TRANS_TYPE:		{			lsActn = "Rei"; 			break; 		}		}		//caRpt.print(lsActn, COL_ACTN_STARTPT, 8);		caRpt.print(lsActn, COL_ACTN_STARTPT, 5);		// Placard No 		caRpt.print(				caDPRptData.getInvItmNo(),			//COL_PLACARD_NO_STARTPT - 1,				COL_PLACARD_NO_STARTPT,			11);		// Exp Date		String lsExpDate =			UtilityMethods.addPadding(				"" + caDPRptData.getRTSExpMo(),				2,				"0")				+ "/"				+ caDPRptData.getRTSExpYr();		//caRpt.print(lsExpDate, COL_PLACARD_NO_STARTPT, 7);		caRpt.print(lsExpDate, COL_EXP_DATE_STARTPT, 7);		// end defect 11214				// Placard Description 		caRpt.print(				lsPlcrdDesc,			COL_PLACARD_DESC_STARTPT,			lsPlcrdDesc.length());	}		/**	 * Print Placard info if from same Transaction  	 * 	 * @param abBlankLine	 * @returns boolean	 *//* &GenDisabledPlacardReport.printPlacardIfSameTrans& */	private boolean printPlacardIfSameTrans(boolean abBlankLine)	{		boolean lbContinue = false; 		if (ciRowNum < cvDPRptData.size() - 1)		{			int i = ciRowNum +1;			DisabledPlacardExportReportData laNextRptData =				(						DisabledPlacardExportReportData) cvDPRptData						.elementAt(								i);			if (caDPRptData.getTransIdntyNo()					== laNextRptData.getTransIdntyNo())			{				caDPRptData = laNextRptData;				printPlacardData();				ciRowNum = i; 				caDPRptData = laNextRptData;				lbContinue = true; 				if (abBlankLine)				{					if (!generatePageBreak(ReportConstant.NUM_LINES_2))					{						caRpt.nextLine();					}				}			}		}		return lbContinue; 	}}/* #GenDisabledPlacardReport# */