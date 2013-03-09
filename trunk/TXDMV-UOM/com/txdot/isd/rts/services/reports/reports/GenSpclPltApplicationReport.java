package com.txdot.isd.rts.services.reports.reports;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.reports.funds.GenFeeReport;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
/*
 *
 * GenSpclPltApplicationReport.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/17/2007	Created template
 * 							defect 9085 Ver Special Plates
 * T Pederson	03/26/2007	set column headers and report format 
 * 							defect 9123 Ver Special Plates  
 * T Pederson	04/05/2007	Set format and totals 
 * 							defect 9123 Ver Special Plates  
 * T Pederson	05/18/2007	Reorganized the way report is generated.
 * 							Process through data returned from SQL
 * 							instead of offices selected. 
 * 							defect 9123 Ver Special Plates
 * K Harrell	06/01/2007	Implement new TransCds for Special
 * 							Plate Application. Print Phone No iff 
 * 							non-blank. 
 * 							modify formatdata()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/03/2007	Organize according to new requirements
 * 							Redefined column starts to put transcd at
 * 							end.
 * 							add addAsOfficeWithData(),
 * 							   findOfficesWithoutdata() 
 * 							delete findOfficeInUIList(), j, k 
 * 							modify formatdata() 
 * 							defect 9201, 9207, 9218 Ver Special Plates
 * K Harrell	08/04/2007	Zip+4 not printing. MfgPltNo truncated
 * 							modify column lengths, starting points
 * 							defect 9222,9216 Ver Special Plates
 * K Harrell	08/22/2007	Blank page due to Null Pointer Exception when
 * 							only one county selected w/ no data
 * 							modify formatReport()
 * 							defect 9268  Ver Special Plates
 * K Harrell	07/10/2009	Implement new OwnerData
 * 							modify formatdata()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	08/17/2009	Implement new generateFooter(boolean)
 * 							Removed check for MFDown 
 * 							delete cbMFDown  
 * 							modify formatReport(), generateExceptionPage() 
 * 							defect 8628 Ver Defect_POS_F     
 * ---------------------------------------------------------------------
 */

/**
 * Generates the Special Plate Application Report
 *
 * @version	Defect_POS_F	08/17/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/17/2007	18:41:00
 */

public class GenSpclPltApplicationReport extends ReportTemplate
{
	private String csOfcId;
	private String csResComptCntyNo;

	private int ciPosTotal = 0;
	private int ciInternetTotal = 0;
	private int ciRptTotal = 0;
	private int ciRptPosTotal = 0;
	private int ciRptInternetTotal = 0;
	private int ciOfcSize = 0;
	private int ciSpclPltRptDataSize = 0;

	// defect 8628 
	// private boolean cbMfDown = false;
	// end defect 8628

	private SpecialPlateTransactionHistoryData caSpclPltTransHstryData =
		null;
	private SpecialPlateUIData caSpclPltUIData = null;
	private Vector cvSpclPltRptData = new Vector();
	private Vector cvOfcWithData = new Vector();
	private Vector cvOfcIdWithNoData = new Vector();

	private final static int COL_ADDRESS_STARTPT = 74;
	private final static int COL_ADDRESS_LENGTH = 34;
	private final static int COL_OWNER_STARTPT = 42;
	private final static int COL_OWNER_LENGTH = 30;
	private final static int COL_PLATETYPE_STARTPT = 13;
	private final static int COL_PLATETYPE_LENGTH = 27;
	private final static int COL_PLATENO_STARTPT = 1;
	private final static int COL_PLATENO_LENGTH = 8;
	private final static int COL_PHONEEMAIL_STARTPT = 110;
	private final static int COL_PHONE_LENGTH = 15;
	private final static int COL_EMAIL_LENGTH = 50;
	private final static int COL_TRANSCD_STARTPT = 160;
	private final static int COL_TRANSCD_LENGTH = 7;
	private final static int COL_TRANSID_STARTPT = 130;
	private final static int COL_TRANSID_LENGTH = 17;
	private final static int COL_EMPLOYEE_STARTPT = 150;
	private final static int COL_EMPLOYEE_LENGTH = 8;
	private final static int COL_TRANSCNT_STARTPT = 116;
	private final static int COL_TRANSCNT_LENGTH = 18;
	private final static int COL_TRANSTTL_STARTPT = 76;
	private final static int COL_TRANSTTL_LENGTH = 10;
	private final static int COL_RPTTOTAL_STARTPT = 68;
	private final static int COL_RPTTOTAL_LENGTH = 6;
	private final static int COL_RPTPOSTOTAL_STARTPT = 64;
	private final static int COL_RPTPOSTOTAL_LENGTH = 10;
	private final static int COL_RPTINTTOTAL_STARTPT = 59;
	private final static int COL_RPTINTTOTAL_LENGTH = 15;
	private final static int HDR_CURRENT_STARTPT = 42;
	private final static int HDR_EMPLOYEE_STARTPT = 150;
	private final static int HDR_PLATE_NO_STARTPT = 1;
	private final static int HDR_PLATE_TYPE_STARTPT = 13;
	private final static int HDR_PHONE_STARTPT = 110;
	private final static int HDR_TRANS_STARTPT = 160;
	private final static int HDR_TRANSID_STARTPT = 130;
	private final static int HDR2_CODE_STARTPT = 160;
	private final static int HDR2_ORGANIZATION_STARTPT = 13;
	private final static int HDR2_OWNRNAMES_STARTPT = 42;
	private final static int HDR2_ADDRESS_STARTPT = 74;
	private final static int HDR2_MFG_PLATENO_STARTPT = 1;
	private final static int HDR2_EMAIL_STARTPT = 110;
	private final static int WRA_HDR_STARTPT = 1;
	private final static int WRA_HDR_LENGTH = 21;
	private final static int WRA_RSLTS_STARTPT = 22;

	private final static  String APPL_TYPE = "APPLICATION TYPE";
	private final static  String TXT_ALL = "ALL";
	private final static  String TXT_PERSONALIZED = "PERSONALIZED";
	private final static  String TXT_EXCEPTION = "EXCEPTION";

	private final static String SPCLPLT_TRANS_HDR = "TRANS";
	private final static String SPCLPLT_CODE_HDR = "CODE";
	private final static String SPCLPLT_PLTNO_HDR = "PLATE NO/";
	private final static String SPCLPLT_MFGPLTNO_HDR = "MFG PLT NO";
	private final static String SPCLPLT_PLT_TYPE_HDR = "PLATE TYPE/";
	private final static String SPCLPLT_ORGANIZATION_HDR =
		"ORGANIZATION";
	private final static String SPCLPLT_CURR_OWNER_HDR = "CURRENT";
	private final static String SPCLPLT_OWNR_NAMES_HDR =
		"OWNER NAME(S)";
	private final static String SPCLPLT_ADDRESS_HDR = "ADDRESS";
	private final static String SPCLPLT_PHONE_HDR = "PHONE/";
	private final static String SPCLPLT_EMAIL_HDR = "E-MAIL";
	private final static String SPCLPLT_TRANSID_HDR = "TRANSACTION ID";
	private final static String SPCLPLT_EMPID_HDR = "EMPLOYEE";

	private final static String SPCLPLT_POSTRANSCOUNT_TXT =
		"POS COUNT:";
	private final static String SPCLPLT_INTERNETCOUNT_TXT =
		"INTERNET COUNT:";
	private final static String SPCLPLT_TRANSCOUNT_TXT = "TOTAL COUNT:";
	private final static String SPCLPLT_POS_TOTAL_TXT = "POS TOTAL:";
	private final static String SPCLPLT_INTERNET_TOTAL_TXT =
		"INTERNET TOTAL:";
	private final static String SPCLPLT_TOTAL_TXT = "TOTAL:";

	/**
	 * GenExemptAuditReport constructor
	 */
	public GenSpclPltApplicationReport()
	{
		super();
	}

	/**
	 * GenExemptAuditReport constructor
	 */
	public GenSpclPltApplicationReport(
		String asRptString,
		ReportProperties aaRptProperties,
		SpecialPlateUIData aaUIData)
	{
		super(asRptString, aaRptProperties);
		this.caSpclPltUIData = aaUIData;
	}

	public static void main(String[] args)
	{
	}

	/**
	 * The formatReport() generates the Exempt Audit Report.
	 *
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		cvSpclPltRptData = avResults;
		ciOfcSize = caSpclPltUIData.getSelectedOffices().size();
		ciSpclPltRptDataSize = cvSpclPltRptData.size();
		String lsPrevResComptCntyNo = "";
		// defect 9268 
		// Initialize to 1st element in case no data found and only one
		// county 
		csResComptCntyNo =
			caSpclPltUIData
				.getSelectedOffices()
				.elementAt(0)
				.toString();
		// end defect 9268 

		// defects 9201, 9207 
		try
		{
			for (int i = 0; i < ciSpclPltRptDataSize; i++)
			{
				caSpclPltTransHstryData =
					(
						SpecialPlateTransactionHistoryData) cvSpclPltRptData
							.elementAt(
						i);
				csResComptCntyNo =
					String.valueOf(
						caSpclPltTransHstryData.getResComptCntyNo());

				if (csResComptCntyNo == null
					|| !csResComptCntyNo.equals(lsPrevResComptCntyNo))
				{
					csOfcId = csResComptCntyNo;
					if (lsPrevResComptCntyNo != "")
					{
						generateTransTotal();
						generateFooter();
					}
					addAsOfficeWithData();
					generateHeader();
				}
				formatdata();
				lsPrevResComptCntyNo = csResComptCntyNo;
			}

			findOfficesWithoutData();

			// Check for no data returned for office(s) selected
			if (ciSpclPltRptDataSize == 0)
			{
				// Single page when only one office selected
				if (ciOfcSize == 1)
				{
					generateHeader();
					generateNoRecordFoundMsg();
					// defect 8628 
					//generateEndOfReport();
					//generateFooter();
					generateFooter(true);
					// end defect 8628  
				}
			}
			else
			{
				generateTransTotal();

				// defect 8628 
				//if (ciOfcSize == 1)
				//{
				//	generateEndOfReport();
				//}
				//generateFooter();
				// end defect 8628 
				generateFooter(ciOfcSize == 1);

			}
			// Create exception page when more than one office selected
			// and no data returned for at least one of the selections
			if (cvOfcIdWithNoData.size() > 0 && ciOfcSize > 1)
			{
				generateExceptionPage();
			}
			// Finish report when one office selected and data returned
			if (ciOfcSize > 1 && ciSpclPltRptDataSize > 0)
			{
				generateRptTotal();
				// defect 8628 
				//generateEndOfReport();
				//generateFooter();
				generateFooter(true);
				// end defect 8628  
			}
			// end defects 9201, 9207

			// defect 8628 
			// Never Used  
			//	if (cbMfDown)
			//	{
			//		int ciCurrPos = caRpt.getCurrX();
			//		int ciPgLngth = caRptProps.getPageHeight();
			//		int ciRRUPos = ((ciPgLngth - ciCurrPos) - 3);
			//		caRpt.blankLines(ciRRUPos);
			//		caRpt.print(ReportConstant.REC_RET_UNAVAIL);
			//	}
			// end defect 8628 
		}
		catch (Exception aeEx)
		{
			System.err.println(ReportConstant.RPT_6001_RPT_ERR);
			aeEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
		}
	}

	/**
	 * 
	 * Identify Offices Without Data
	 * 
	 */
	private void findOfficesWithoutData()
	{
		for (int i = 0; i < ciOfcSize; i++)
		{
			csOfcId =
				((caSpclPltUIData.getSelectedOffices())
					.elementAt(i)
					.toString());

			if (!cvOfcWithData.contains(csOfcId))
			{
				cvOfcIdWithNoData.add(csOfcId);
			}
		}
	}

	/**
	 * Prints the list of offices with no data returned
	 * 
	 */
	private void generateExceptionPage()
	{
		if (cvOfcIdWithNoData.size() > 0)
		{
			caRpt.print(caRptProps.getUniqueName());
			caRpt.center(caRpt.csName);
			caRpt.center(TXT_EXCEPTION);
			generateReportCriteriaHeader();
			caRpt.print("NO RECORDS FOUND FOR OFFICES: ");
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			int liColStartPt = 1;
			for (int i = 0; i < cvOfcIdWithNoData.size(); i++)
			{
				if (liColStartPt > 130)
				{
					caRpt.nextLine();
					liColStartPt = 1;
				}
				String lsOfcId = cvOfcIdWithNoData.get(i).toString();
				OfficeIdsData caOfcIds =
					OfficeIdsCache.getOfcId(Integer.parseInt(lsOfcId));
				String csRptOfcName = caOfcIds.getOfcName();
				caRpt.print(
					csRptOfcName,
					liColStartPt,
					csRptOfcName.length());
				if (i != cvOfcIdWithNoData.size() - 1)
				{
					caRpt.print(CommonConstant.STR_COMMA);
				}
				liColStartPt = liColStartPt + csRptOfcName.length() + 3;
			}

			// defect 8628  
			//if (cvOfcIdWithNoData.size() == ciOfcSize)
			//{
			//	generateEndOfReport();
			//}
			//generateFooter();
			generateFooter(cvOfcIdWithNoData.size() == ciOfcSize);
			// end defect 8628 
		}
	}

	/**
	 * Prints the total number of transactions
	 * 
	 * 
	 */
	private void generateRptTotal()
	{
		if (ciRptTotal > 0)
		{
			caRpt.print(caRptProps.getUniqueName());
			caRpt.center(caRpt.csName);
			caRpt.center(ReportConstant.RPT_6001_SUMMARY);
			generateReportCriteriaHeader();
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			caRpt.print(
				SPCLPLT_POS_TOTAL_TXT,
				COL_RPTPOSTOTAL_STARTPT,
				COL_RPTPOSTOTAL_LENGTH);
			caRpt.print(
				"" + ciRptPosTotal,
				COL_TRANSTTL_STARTPT,
				COL_TRANSTTL_LENGTH);
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			caRpt.print(
				SPCLPLT_INTERNET_TOTAL_TXT,
				COL_RPTINTTOTAL_STARTPT,
				COL_RPTINTTOTAL_LENGTH);
			caRpt.print(
				"" + ciRptInternetTotal,
				COL_TRANSTTL_STARTPT,
				COL_TRANSTTL_LENGTH);
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			caRpt.print(
				SPCLPLT_TOTAL_TXT,
				COL_RPTTOTAL_STARTPT,
				COL_RPTTOTAL_LENGTH);
			caRpt.print(
				"" + ciRptTotal,
				COL_TRANSTTL_STARTPT,
				COL_TRANSTTL_LENGTH);
		}
	}

	/**
	 * Prints the total number of transactions for an office
	 * 
	 * 
	 */
	private void generateTransTotal()
	{
		if (ciPosTotal > 0 || ciInternetTotal > 0)
		{
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			caRpt.print(
				SPCLPLT_POSTRANSCOUNT_TXT,
				COL_TRANSCNT_STARTPT,
				COL_TRANSCNT_LENGTH);
			caRpt.print(
				"" + ciPosTotal,
				COL_TRANSTTL_STARTPT,
				COL_TRANSTTL_LENGTH);
			caRpt.nextLine();
			caRpt.print(
				SPCLPLT_INTERNETCOUNT_TXT,
				COL_TRANSCNT_STARTPT,
				COL_TRANSCNT_LENGTH);
			caRpt.print(
				"" + ciInternetTotal,
				COL_TRANSTTL_STARTPT,
				COL_TRANSTTL_LENGTH);
			caRpt.nextLine();
			caRpt.print(
				SPCLPLT_TRANSCOUNT_TXT,
				COL_TRANSCNT_STARTPT,
				COL_TRANSCNT_LENGTH);
			caRpt.print(
				"" + (ciPosTotal + ciInternetTotal),
				COL_TRANSTTL_STARTPT,
				COL_TRANSTTL_LENGTH);
			ciRptTotal = ciRptTotal + ciPosTotal + ciInternetTotal;
			ciRptPosTotal = ciRptPosTotal + ciPosTotal;
			ciRptInternetTotal = ciRptInternetTotal + ciInternetTotal;
			ciPosTotal = 0;
			ciInternetTotal = 0;
		}
	}

	/**
	 * The generateHeader() adds text that will be printed when needed
	 */
	private void generateHeader()
	{
		OfficeIdsData caOfcIds =
			OfficeIdsCache.getOfcId(Integer.parseInt(csResComptCntyNo));
		String csRptOfcName = caOfcIds.getOfcName();

		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		if (caRptProps.getOfficeIssuanceName().equals(csRptOfcName))
		{
			csRptOfcName = ReportConstant.RPT_6001_SUMMARY;
		}
		caRpt.center(csRptOfcName);
		caRpt.blankLines(ReportConstant.NUM_LINES_1);
		generateReportCriteriaHeader();
		generateColumnHeaders();
	}

	/**
	 * Adds the report criteria header text to the report
	 * 
	 * 
	 */
	private void generateReportCriteriaHeader()
	{
		caRpt.print(
			ReportConstant.WORKSTATION_ID,
			WRA_HDR_STARTPT,
			WRA_HDR_LENGTH);
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caRptProps.getWorkstationId(),
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caRptProps.getWorkstationId())
				.length());
		caRpt.nextLine();
		caRpt.print(
			ReportConstant.REQUESTED_BY,
			WRA_HDR_STARTPT,
			WRA_HDR_LENGTH);
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caRptProps.getRequestedBy(),
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caRptProps.getRequestedBy())
				.length());
		caRpt.nextLine();
		caRpt.print(APPL_TYPE, WRA_HDR_STARTPT, WRA_HDR_LENGTH);

		String lsApplType = TXT_ALL;
		if (caSpclPltUIData.isPersonalizedOnly())
		{
			lsApplType = TXT_PERSONALIZED;
		}
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ lsApplType,
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ lsApplType)
				.length());
		caRpt.nextLine();
		caRpt.print(
			GenFeeReport.TRANSACTION_HEADER_STRING,
			WRA_HDR_STARTPT,
			WRA_HDR_LENGTH);
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caSpclPltUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ GenFeeReport.THROUGH_HEADER_STRING
				+ CommonConstant.STR_SPACE_ONE
				+ caSpclPltUIData.getEndDate(),
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caSpclPltUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ GenFeeReport.THROUGH_HEADER_STRING
				+ CommonConstant.STR_SPACE_ONE
				+ caSpclPltUIData.getEndDate())
				.length());
		caRpt.nextLine();
		caRpt.blankLines(ReportConstant.NUM_LINES_2);

	}

	/**
	 * Adds the column header text to the report
	 * 
	 * 
	 */
	private void generateColumnHeaders()
	{
		caRpt.print(
			SPCLPLT_PLTNO_HDR,
			HDR_PLATE_NO_STARTPT,
			SPCLPLT_PLTNO_HDR.length());
		caRpt.print(
			SPCLPLT_PLT_TYPE_HDR,
			HDR_PLATE_TYPE_STARTPT,
			SPCLPLT_PLT_TYPE_HDR.length());
		caRpt.print(
			SPCLPLT_CURR_OWNER_HDR,
			HDR_CURRENT_STARTPT,
			SPCLPLT_CURR_OWNER_HDR.length());
		caRpt.print(
			SPCLPLT_PHONE_HDR,
			HDR_PHONE_STARTPT,
			SPCLPLT_PHONE_HDR.length());
		caRpt.print(
			SPCLPLT_TRANS_HDR,
			HDR_TRANS_STARTPT,
			SPCLPLT_TRANS_HDR.length());
		caRpt.nextLine();
		caRpt.print(
			SPCLPLT_MFGPLTNO_HDR,
			HDR2_MFG_PLATENO_STARTPT,
			SPCLPLT_MFGPLTNO_HDR.length());
		caRpt.print(
			SPCLPLT_ORGANIZATION_HDR,
			HDR2_ORGANIZATION_STARTPT,
			SPCLPLT_ORGANIZATION_HDR.length());
		caRpt.print(
			SPCLPLT_OWNR_NAMES_HDR,
			HDR2_OWNRNAMES_STARTPT,
			SPCLPLT_OWNR_NAMES_HDR.length());
		caRpt.print(
			SPCLPLT_ADDRESS_HDR,
			HDR2_ADDRESS_STARTPT,
			SPCLPLT_ADDRESS_HDR.length());
		caRpt.print(
			SPCLPLT_EMAIL_HDR,
			HDR2_EMAIL_STARTPT,
			SPCLPLT_EMAIL_HDR.length());
		caRpt.print(
			SPCLPLT_TRANSID_HDR,
			HDR_TRANSID_STARTPT,
			SPCLPLT_TRANSID_HDR.length());
		caRpt.print(
			SPCLPLT_EMPID_HDR,
			HDR_EMPLOYEE_STARTPT,
			SPCLPLT_EMPID_HDR.length());
		caRpt.print(
			SPCLPLT_CODE_HDR,
			HDR2_CODE_STARTPT,
			SPCLPLT_CODE_HDR.length());
		caRpt.nextLine();
		caRpt.drawDashedLine();
		//caRpt.blankLines(ReportConstant.NUM_LINES_1);

	}

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Format Report Data 
	 * 
	 */
	private void formatdata()
	{
		String lsMfDown = CommonConstant.STR_SPACE_EMPTY;
		String lsPlateType = caSpclPltTransHstryData.getRegPltCd();
		ItemCodesData laItemCodesData =
			ItemCodesCache.getItmCd(lsPlateType);
		lsPlateType = laItemCodesData.getItmCdDesc();
		if (caSpclPltTransHstryData.getVoidedTransIndi() == 1)
		{
			lsPlateType = ReportConstant.STR_VOIDED;
		}
		else
		{
			// Increment the transaction count for this office
			if (UtilityMethods
				.isSPAPPL(caSpclPltTransHstryData.getTransCd()))
			{
				ciPosTotal++;
			}
			else
			{
				ciInternetTotal++;
			}
		}
		caRpt.print(
			caSpclPltTransHstryData.getRegPltNo(),
			COL_PLATENO_STARTPT,
			COL_PLATENO_LENGTH);
		caRpt.print(
			lsPlateType,
			COL_PLATETYPE_STARTPT,
			COL_PLATETYPE_LENGTH);

		// defect 10112 
		OwnerData laPltOwnrData =
			caSpclPltTransHstryData.getPltOwnerData();
		AddressData laPltAddrData = laPltOwnrData.getAddressData();

		caRpt.print(
		//caSpclPltTransHstryData.getPltOwnrName1(),
		laPltOwnrData.getName1(), COL_OWNER_STARTPT, COL_OWNER_LENGTH);

		caRpt.print(
		//caSpclPltTransHstryData.getPltOwnrSt1(),
		laPltAddrData.getSt1(),
			COL_ADDRESS_STARTPT,
			COL_ADDRESS_LENGTH);
		// end defect 10112  

		String lsSpclPltOwnrPhoneNo =
			caSpclPltTransHstryData.getPltOwnrPhone();

		if (lsSpclPltOwnrPhoneNo.length() != 0)
		{
			UtilityMethods.addPadding(
				caSpclPltTransHstryData.getPltOwnrPhone(),
				10,
				" ");
			lsSpclPltOwnrPhoneNo =
				"("
					+ lsSpclPltOwnrPhoneNo.substring(0, 3)
					+ ")"
					+ lsSpclPltOwnrPhoneNo.substring(3, 6)
					+ "-"
					+ lsSpclPltOwnrPhoneNo.substring(6);
		}
		caRpt.print(
			lsSpclPltOwnrPhoneNo,
			COL_PHONEEMAIL_STARTPT,
			COL_PHONE_LENGTH);
		caRpt.print(
			UtilityMethods.getTransId(
				caSpclPltTransHstryData.getOfcIssuanceNo(),
				caSpclPltTransHstryData.getTransWsId(),
				caSpclPltTransHstryData.getTransAMDate(),
				caSpclPltTransHstryData.getTransTime()),
			COL_TRANSID_STARTPT,
			COL_TRANSID_LENGTH);
		caRpt.print(
			caSpclPltTransHstryData.getTransEmpId(),
			COL_EMPLOYEE_STARTPT,
			COL_EMPLOYEE_LENGTH);
		caRpt.print(
			caSpclPltTransHstryData.getTransCd() + lsMfDown,
			COL_TRANSCD_STARTPT,
			COL_TRANSCD_LENGTH);
		caRpt.nextLine();
		caRpt.print(
			caSpclPltTransHstryData.getMfgPltNo(),
			COL_PLATENO_STARTPT,
			COL_PLATENO_LENGTH);

		String lsOrgName =
			OrganizationNumberCache.getOrgName(
				caSpclPltTransHstryData.getRegPltCd(),
				caSpclPltTransHstryData.getOrgNo());
		caRpt.print(
			lsOrgName,
			COL_PLATETYPE_STARTPT,
			COL_PLATETYPE_LENGTH);

		// defect 10112 
		caRpt.print(
		//caSpclPltTransHstryData.getPltOwnrName2(),
		laPltOwnrData.getName2(), COL_OWNER_STARTPT, COL_OWNER_LENGTH);

		//		String lsZpCd =
		//			generateZipCdP4(
		//				caSpclPltTransHstryData.getPltOwnrZpCd(),
		//				caSpclPltTransHstryData.getPltOwnrZpCd4(),
		//				caSpclPltTransHstryData.getPltOwnrState());
		//
		//		String lsCityStateZpCd =
		//			generateCityCntry(
		//				caSpclPltTransHstryData.getPltOwnrCity(),
		//				caSpclPltTransHstryData.getPltOwnrState(),
		//				caSpclPltTransHstryData.getPltOwnrCntry(),
		//				lsZpCd);

		//if (caSpclPltTransHstryData.getPltOwnrSt2()
		// != null & caSpclPltTransHstryData.getPltOwnrSt2()
		//!= "")

		boolean lbSt2 = !UtilityMethods.isEmpty(laPltAddrData.getSt2());

		if (lbSt2)
		{
			caRpt.print(laPltAddrData.getSt2(),
			//caSpclPltTransHstryData.getPltOwnrSt2(),
			COL_ADDRESS_STARTPT, COL_ADDRESS_LENGTH);
		}
		else
		{
			caRpt.print(
			//lsCityStateZpCd,
			laPltAddrData.getCityStateCntryZip(),
				COL_ADDRESS_STARTPT,
				COL_ADDRESS_LENGTH);
		}
		caRpt.print(
			caSpclPltTransHstryData.getPltOwnrEMail(),
			COL_PHONEEMAIL_STARTPT,
			COL_EMAIL_LENGTH);

		if (lbSt2)
		{
			//		if (caSpclPltTransHstryData.getPltOwnrSt2()
			//			!= null & caSpclPltTransHstryData.getPltOwnrSt2()
			//			!= "")
			//		{
			caRpt.nextLine();
			caRpt.print(
				laPltAddrData.getCityStateCntryZip(),
				COL_ADDRESS_STARTPT,
				COL_ADDRESS_LENGTH);
		}
		// end defect 10112

		caRpt.blankLines(ReportConstant.NUM_LINES_2);
		generatePageBreak(ReportConstant.NUM_LINES_2);
	}

	/**
	 * Generate the page break with lines required
	 * 
	 * @param aiLinesRequired int
	 */
	private void generatePageBreak(int aiLinesRequired)
	{
		if (getNoOfDetailLines() < aiLinesRequired)
		{
			generateFooter();
			generateHeader();
		}
	}
	/**
	 * Add Found ResComptCntyNo, OfcIssuanceNo 
	 */
	private void addAsOfficeWithData()
	{
		String lsResComptCntyNo =
			String.valueOf(caSpclPltTransHstryData.getResComptCntyNo());
		String lsOfcIssuanceNo =
			String.valueOf(caSpclPltTransHstryData.getOfcIssuanceNo());

		if (!cvOfcWithData.contains(lsResComptCntyNo))
		{
			cvOfcWithData.addElement(lsResComptCntyNo);
		}
		if (!cvOfcWithData.contains(lsOfcIssuanceNo))
		{
			cvOfcWithData.addElement(lsOfcIssuanceNo);
		}
	}

	/**
	 * Currently not implemented
	 */
	public Vector queryData(String asQuery)
	{
		return null;
	}
}
