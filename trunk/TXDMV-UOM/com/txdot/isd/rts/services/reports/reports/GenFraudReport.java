package com.txdot.isd.rts.services.reports.reports;

import java.util.Vector;

import com.txdot.isd.rts.services.data.FraudLogData;
import com.txdot.isd.rts.services.data.FraudStateData;
import com.txdot.isd.rts.services.data.FraudUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.reports.funds.GenFeeReport;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenFraudReport.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/09/2011  Created
 * 							defect 10900 Ver 6.8.0 
 * K Harrell	07/05/2011	Printing DocNo for Vin (typo) 
 * 							modify formatdata()
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Generate Suspected Fraud Report
 *
 * @version	6.8.0			07/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/09/2011	17:27:17 
 */
public class GenFraudReport extends ReportTemplate
{
	private int ciRptDataSize;

	private Vector cvFraudPRptData = new Vector();

	private FraudUIData caFraudUIData;
	private FraudLogData caFraudLogData;

	private static final int COL_ACTION_LENGTH = 6;
	private static final int COL_ACTION_STARTPT = 44;

	private static final int COL_DATE_LENGTH = 10;
	private static final int COL_DATE_STARTPT = 1;

	private static final int COL_DOCNO_LENGTH = 17;
	private static final int COL_DOCNO_STARTPT = 52;

	private static final int COL_EMPID_LENGTH = 7;
	private static final int COL_EMPID_STARTPT = 35;

	private static final int COL_FRAUD_REASON_LENGTH = 26;
	private static final int COL_FRAUD_REASON_STARTPT = 105;

	private static final int COL_OFC_LENGTH = 3;
	private static final int COL_OFC_STARTPT = 24;

	private static final int COL_REGPLTNO_LENGTH = 8;
	private static final int COL_REGPLTNO_STARTPT = 96;

	private static final int COL_TIME_LENGTH = 8;
	private static final int COL_TIME_STARTPT = 14;

	private static final int COL_VIN_LENGTH = 22;
	private static final int COL_VIN_STARTPT = 71;

	private static final int COL_WSID_LENGTH = 4;
	private static final int COL_WSID_STARTPT = 29;

	private static final String ACTION_TYPE = "ACTION TYPE";
	private static final String ACTION_COL_HDR = "ACTION";
	private static final String DATE_COL_HDR = "DATE";
	private static final String DOC_NO_COL_HDR = "DOCUMENT NO";
	private static final String EMPID_COL_HDR = "EMPID";
	private static final String FRAUD_TYPE = "FRAUD TYPE";
	private static final String FRAUD_REASON_COL_HDR = "FRAUD REASON";
	private static final String OFC_COL_HDR = "OFC";
	private static final String REGPLTNO_COL_HDR = "PLATE";
	private static final String TIME_COL_HDR = "TIME";
	private static final String VIN_COL_HDR = "VIN";
	private static final String WSID_COL_HDR = "WSID";

	private static final int WRA_HDR_LENGTH = 21;
	private static final int WRA_HDR_STARTPT = 1;
	private static final int WRA_RSLTS_STARTPT = 22;

	/**
	 * GenFraudReport.java Constructor
	 * 
	 */
	public GenFraudReport()
	{
		super();
	}

	/**
	 * GenFraudReport constructor
	 */
	public GenFraudReport(
		String asRptString,
		ReportProperties aaRptProperties,
		FraudUIData aaUIData)
	{
		super(asRptString, aaRptProperties);
		this.caFraudUIData = aaUIData;
	}

	/**
	 * Format Report Data 
	 * 
	 */
	private void formatdata()
	{
		// Date 
		RTSDate laDate =
			new RTSDate(
				RTSDate.AMDATE,
				caFraudLogData.getTransAMDate());
		laDate.setTime(caFraudLogData.getTransTime());

		caRpt.print(
			laDate.getMMDDYYYY(),
			COL_DATE_STARTPT,
			COL_DATE_LENGTH);

		// Time 
		caRpt.print(
			laDate.getClockTimeNoMs(),
			COL_TIME_STARTPT,
			COL_TIME_LENGTH);

		// Ofc 
		caRpt.print(
			"" + caFraudLogData.getOfcIssuanceNo(),
			COL_OFC_STARTPT,
			COL_OFC_LENGTH);

		// WsId 
		caRpt.print(
			"" + caFraudLogData.getTransWsId(),
			COL_WSID_STARTPT,
			COL_WSID_LENGTH);

		// TransEmpId 
		caRpt.print(
			caFraudLogData.getTransEmpId(),
			COL_EMPID_STARTPT,
			COL_EMPID_LENGTH);

		// Action 	
		String lsActn =
			caFraudLogData.getAddFraudIndi() == 1 ? "Add" : "Delete";

		caRpt.print(lsActn, COL_ACTION_STARTPT, COL_ACTION_LENGTH);

		// DocNo 
		caRpt.print(
			caFraudLogData.getDocNo(),
			COL_DOCNO_STARTPT,
			COL_DOCNO_LENGTH);

		// VIN
		caRpt.print(
			caFraudLogData.getVIN(),
			COL_VIN_STARTPT,
			COL_VIN_LENGTH);

		// RegPltNo 
		caRpt.print(
			caFraudLogData.getRegPltNo(),
			COL_REGPLTNO_STARTPT,
			COL_REGPLTNO_LENGTH);

		// Fraud Reason 				
		caRpt.print(
			caFraudLogData.getFraudDesc(),
			COL_FRAUD_REASON_STARTPT,
			COL_FRAUD_REASON_LENGTH);

		// 2 Blank Lines
		caRpt.blankLines(2);
	}

	/**
	 * The formatReport() generates the Disabled Placard Report.
	 *
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		cvFraudPRptData = avResults;
		ciRptDataSize = cvFraudPRptData.size();
		generateHeader();

		try
		{
			for (int i = 0; i < ciRptDataSize; i++)
			{
				caFraudLogData =
					(FraudLogData) cvFraudPRptData.elementAt(i);

				formatdata();

				generatePageBreak(ReportConstant.NUM_LINES_2);

			}

			// Check for no data returned for office(s) selected
			if (ciRptDataSize == 0)
			{
				generateNoRecordFoundMsg();
				generateFooter(true);
			}
			else
			{
				generateFooter(true);
			}
		}
		catch (Exception aeEx)
		{
			System.err.println(ReportConstant.RPT_8001_RPT_ERR);
			aeEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
		}
	}

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Adds the column header text to the report
	 * 
	 */
	private void generateColumnHeaders()
	{
		// Date 
		caRpt.print(
			DATE_COL_HDR,
			COL_DATE_STARTPT,
			DATE_COL_HDR.length());

		// Time 
		caRpt.print(
			TIME_COL_HDR,
			COL_TIME_STARTPT,
			TIME_COL_HDR.length());

		// Ofc 
		caRpt.print(OFC_COL_HDR, COL_OFC_STARTPT, COL_OFC_LENGTH);

		// WsId 
		caRpt.print(WSID_COL_HDR, COL_WSID_STARTPT, COL_WSID_LENGTH);

		// TransEmpId 
		caRpt.print(EMPID_COL_HDR, COL_EMPID_STARTPT, COL_EMPID_LENGTH);

		// Action 	
		caRpt.print(
			ACTION_COL_HDR,
			COL_ACTION_STARTPT,
			COL_ACTION_LENGTH);

		// DocNo 
		caRpt.print(
			DOC_NO_COL_HDR,
			COL_DOCNO_STARTPT,
			COL_DOCNO_LENGTH);

		// VIN
		caRpt.print(VIN_COL_HDR, COL_VIN_STARTPT, COL_VIN_LENGTH);

		// RegPltNo 
		caRpt.print(
			REGPLTNO_COL_HDR,
			COL_REGPLTNO_STARTPT,
			COL_REGPLTNO_LENGTH);

		// Fraud Reason 				
		caRpt.print(
			FRAUD_REASON_COL_HDR,
			COL_FRAUD_REASON_STARTPT,
			COL_FRAUD_REASON_LENGTH);

		caRpt.nextLine();
		caRpt.drawDashedLine();
	}

	/**
	 * The generateHeader() adds text that will be printed when needed
	 */
	private void generateHeader()
	{
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		caRpt.blankLines(ReportConstant.NUM_LINES_1);
		generateReportCriteriaHeader();
		generateColumnHeaders();
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
	 * Adds the report criteria header text to the report
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

		// Action Type 
		if (caFraudUIData.getTransType() != 2)
		{
			caRpt.print(ACTION_TYPE, WRA_HDR_STARTPT, WRA_HDR_LENGTH);

			String lsApplType =
				caFraudUIData.getTransType() == 1 ? "ADD" : "DELETE";

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
		}
		// Fraud Type
		FraudStateData laFraudData = caFraudUIData.getFraudTypeData();

		if (!laFraudData.isAllTypes())
		{
			caRpt.print(FRAUD_TYPE, WRA_HDR_STARTPT, WRA_HDR_LENGTH);
			String lsFraudNames = laFraudData.getFraudNames();
			caRpt.print(
				CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ lsFraudNames,
				WRA_RSLTS_STARTPT,
				(CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ lsFraudNames)
					.length());
			caRpt.nextLine();
		}

		// Add'l Search Criteria 
		if (!UtilityMethods.isEmpty(caFraudUIData.getAddlSearch()))
		{
			String lsSearchType =
				caFraudUIData.getAddlSearch().toUpperCase().trim();
			lsSearchType =
				(lsSearchType.equals("REGPLTNO")
					? "PLATE NO"
					: lsSearchType);

			lsSearchType =
				(lsSearchType.equals("DOCNO")
					? "DOCUMENT NO"
					: lsSearchType);

			caRpt.print(lsSearchType, WRA_HDR_STARTPT, WRA_HDR_LENGTH);
			caRpt.print(
				CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ caFraudUIData.getSearchKey(),
				WRA_RSLTS_STARTPT,
				(CommonConstant.STR_COLON
					+ CommonConstant.STR_SPACE_ONE
					+ caFraudUIData.getSearchKey())
					.length());
			caRpt.nextLine();

		}

		caRpt.print(
			GenFeeReport.TRANSACTION_HEADER_STRING,
			WRA_HDR_STARTPT,
			WRA_HDR_LENGTH);
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caFraudUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ GenFeeReport.THROUGH_HEADER_STRING
				+ CommonConstant.STR_SPACE_ONE
				+ caFraudUIData.getEndDate(),
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caFraudUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ GenFeeReport.THROUGH_HEADER_STRING
				+ CommonConstant.STR_SPACE_ONE
				+ caFraudUIData.getEndDate())
				.length());
		caRpt.nextLine();
		caRpt.blankLines(ReportConstant.NUM_LINES_2);

	}

	/**
	 * Currently not implemented
	 */
	public Vector queryData(String asQuery)
	{
		return null;
	}
}
