package com.txdot.isd.rts.services.reports.reports;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.ExemptAuditData;
import com.txdot.isd.rts.services.data.ExemptAuditUIData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
/*
 *
 * GenExemptAuditReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Ralph		10/11/2006	New Class - Exempts
 * J Ralph		11/15/2006	Modify for MfDwn Indicator.  When voided
 * K Harrell				show Voided Notation over PriorRegClassCd
 * 							modify COL_OWNERTTL_STARTPT, formatdata(),
 * 							formatReport()
 * 							defect 9017 Ver Exempts
 * J Ralph		11/20/2006	Change Owner Name Column Header text
 * 							modify HDR_CURROWNER_STARTPT,
 * 							HDR2_OWNRNAMES_STARTPT, EXMPT_CURR_OWNER_HDR,
 * 							EXMPT_OWNR_NAMES_HDR, generateHeader() 
 * 							defect 9021 Ver Exempts
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	08/14/2009	Create Exeception Page for Offices w/ no 
 * 							Data. Removed blank line under column header. 
 * 							add cbException, ciOfcId, ciOfcSize, 
 * 							  caExmptAuditData, cvOfcWithData,
 * 							  cvOfcIdWithNoData 
 * 							add addAsOfficeWithData(), 
 * 								findOfficesWithoutData(), 
 * 								generateExceptionPage(), 
 *								generateReportCriteriaHeader()
 *							delete cbRptData, csOfcId			 
 * 							modify formatReport(), generateHeader(), 
 * 								formatData() 
 * 							defect 10169 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */

/**
 * Generates the Exempt Audit Report
 *
 * @version	Defect_POS_F	08/14/2009	
 * @author	John Ralph
 * @author  Kathy Harrell 
 * <br>Creation Date:		10/11/2006 13:12:02
 */

public class GenExemptAuditReport extends ReportTemplate
{
	// defect 10169
	private boolean cbException = false;
	private int ciOfcId;
	private int ciOfcSize;
	private ExemptAuditData caExmptAuditData;
	private Vector cvOfcWithData = new Vector();
	private Vector cvOfcIdWithNoData = new Vector();
	// end defect 10169

	// defect 9017 
	private boolean cbMfDown = false;
	//end defect 9017

	private ExemptAuditUIData caExUIData = null;
	protected Vector cvExemptRptData = new Vector();

	private static final int COL_EXMPTINDI_STARTPT = 40;
	private static final int COL_EXMPTINDI_LENGTH = 2;
	// defect 9017 
	private static final int COL_OWNERTTL_STARTPT = 9;
	// end defect 9017 
	private static final int COL_OWNERTTL_LENGTH = 30;
	private static final int COL_PLATEDESC_STARTPT = 72;
	private static final int COL_PLATEDESC_LENGTH = 27;
	private static final int COL_PLATENO_STARTPT = 101;
	private static final int COL_PLATENO_LENGTH = 7;
	private static final int COL_REGCLASS_STARTPT = 45;
	private static final int COL_REGCLASS_LENGTH = 25;
	private static final int COL_TRANSCD_STARTPT = 1;
	// defect 9017
	private static final int COL_TRANSCD_LENGTH = 7;
	// end defect 9017 
	private static final int COL_TRANSEMPID_STARTPT = 111;
	private static final int COL_TRANSEMPID_LENGTH = 17;
	// defect 9021
	private static final int HDR_CURROWNER_STARTPT = 20;
	// end defect 9021
	private static final int HDR_PRIOR_NEW1_STARTPT = 37;
	private static final int HDR_PRIOR_NEW2_STARTPT = 52;
	private static final int HDR_PRIOR_NEW3_STARTPT = 81;
	private static final int HDR_PRIOR_NEW4_STARTPT = 100;
	private static final int HDR_TRANS_STARTPT = 1;
	private static final int HDR_TRANSID_STARTPT = 112;
	private static final int HDR2_CODE_STARTPT = 1;
	private static final int HDR2_EMPID_STARTPT = 114;
	private static final int HDR2_EXMPTINDI_STARTPT = 36;
	// defect 9021
	private static final int HDR2_OWNRNAMES_STARTPT = 17;
	// end defect 9021
	private static final int HDR2_PLATEDESC_STARTPT = 81;
	private static final int HDR2_PLATENO_STARTPT = 100;
	private static final int HDR2_REGCLASS_STARTPT = 50;
	private static final int WRA_HDR_STARTPT = 1;
	private static final int WRA_HDR_LENGTH = 21;
	private static final int WRA_RSLTS_STARTPT = 22;

	private static final String EXMPT_TRANS_HDR = "TRANS";
	// defect 9021
	private static final String EXMPT_CURR_OWNER_HDR = "CURRENT";
	// end defect 9021	
	private static final String EXMPT_PRIOR_NEW_HDR = "PRIOR/NEW";
	private static final String EXMPT_TRANSID_HDR = "TRANSACTION ID/";
	private static final String EXMPT_CODE_HDR = "CODE";
	// defect 9021
	private static final String EXMPT_OWNR_NAMES_HDR = "OWNER NAME(S)";
	// end defect 9021
	private static final String EXMPT_EXMPTINDI_HDR = "EXEMPT INDI";
	private static final String EXMPT_REGCLASS_DESC_HDR =
		"REGIS CLASS DESC";
	private static final String EXMPT_PLT_DESC_HDR = "PLATE DESC";
	private static final String EXMPT_PLTNO_HDR = "PLATE NO";
	private static final String EXMPT_EMPID_HDR = "EMPLOYEE ID";
	private final String EXCEPTION = "EXCEPTION";

	/**
	 * GenExemptAuditReport constructor
	 */
	public GenExemptAuditReport()
	{
		super();
	}

	/**
	 * GenExemptAuditReport constructor
	 */
	public GenExemptAuditReport(
		String asRptString,
		ReportProperties aaRptProperties,
		ExemptAuditUIData aaUIData)
	{
		super(asRptString, aaRptProperties);
		this.caExUIData = aaUIData;
	}

	public static void main(String[] args)
	{
	}

	/**
	 * Add Found OfcIssuanceNo 
	 */
	private void addAsOfficeWithData()
	{
		String lsOfcIssuanceNo =
			String.valueOf(caExmptAuditData.getOfcIssuanceNo());

		if (!cvOfcWithData.contains(lsOfcIssuanceNo))
		{
			cvOfcWithData.addElement(lsOfcIssuanceNo);
		}
	}

	/**
	 * 
	 * Identify Offices Without Data
	 */
	private void findOfficesWithoutData()
	{
		for (int i = 0;
			i < caExUIData.getSelectedOffices().size();
			i++)
		{
			String lsOfcId =
				((caExUIData.getSelectedOffices())
					.elementAt(i)
					.toString());

			if (!cvOfcWithData.contains(lsOfcId))
			{
				cvOfcIdWithNoData.add(lsOfcId);
			}
		}
	}

	/**
	 * The formatReport() generates the Exempt Audit Report.
	 *
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		cvExemptRptData = avResults;

		// defect 10127
		// Reorganize to print only one row in formatData, 
		//  print Exception Report  
		ciOfcSize = caExUIData.getSelectedOffices().size();
		int liPrevOfcId = 0;

		try
		{
			ciOfcId =
				((Integer) caExUIData
					.getSelectedOffices()
					.elementAt(0))
					.intValue();

			for (int i = 0; i < cvExemptRptData.size(); i++)
			{
				caExmptAuditData =
					(ExemptAuditData) cvExemptRptData.elementAt(i);

				ciOfcId = caExmptAuditData.getOfcIssuanceNo();

				if (ciOfcId != liPrevOfcId)
				{
					addAsOfficeWithData();

					if (liPrevOfcId != 0)
					{
						generateFooter();
					}
					generateHeader();
				}
				formatdata();
				liPrevOfcId = ciOfcId;
			}

			findOfficesWithoutData();

			if (cvExemptRptData.size() == 0 && ciOfcSize == 1)
			{
				generateHeader();
				generateNoRecordFoundMsg();
				generateFooter(true);
			}
			else
			{
				if (cvExemptRptData.size() != 0)
				{
					if (cbMfDown)
					{
						int ciCurrPos = caRpt.getCurrX();
						int ciPgLngth = caRptProps.getPageHeight();
						int ciRRUPos = ((ciPgLngth - ciCurrPos) - 3);
						caRpt.blankLines(ciRRUPos);
						caRpt.print(ReportConstant.REC_RET_UNAVAIL);
					}
					generateFooter(ciOfcSize == 1);
				}

				if (cvOfcIdWithNoData.size() > 0 && ciOfcSize > 1)
				{
					cbException = true;
					generateExceptionPage();
				}
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			System.err.println(ReportConstant.RPT_5051_RPT_ERR);
			aeNFEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNFEx);
			leRTSEx.writeExceptionToLog();
		}
		catch (Exception aeEx)
		{
			System.err.println(ReportConstant.RPT_5051_RPT_ERR);
			aeEx.printStackTrace(System.out);

			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
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
			generateHeader();
			caRpt.print("NO RECORDS FOUND FOR OFFICES: ");
			caRpt.blankLines(ReportConstant.NUM_LINES_1);
			int liColStartPt = 1;
			int liOffices = cvOfcIdWithNoData.size();
			for (int i = 0; i < liOffices; i++)
			{
				String lsOfcId = cvOfcIdWithNoData.get(i).toString();
				OfficeIdsData caOfcIds =
					OfficeIdsCache.getOfcId(Integer.parseInt(lsOfcId));
				String csRptOfcName = caOfcIds.getOfcName();
				if (liColStartPt + csRptOfcName.length()
					> caRpt.getWidth())
				{
					caRpt.nextLine();
					liColStartPt = 1;
				}
				caRpt.print(
					csRptOfcName,
					liColStartPt,
					csRptOfcName.length());
				if (i != liOffices - 1)
				{
					caRpt.print(CommonConstant.STR_COMMA);
				}
				liColStartPt = liColStartPt + csRptOfcName.length() + 3;
			}
			generateFooter(true);
		}
	}

	/**
	 * The generateHeader() adds text that will be printed when needed
	 */
	private void generateHeader()
	{
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);

		// defect 10164 
		// Reorder to accommodate Header for Exception Page  

		if (cbException)
		{
			caRpt.center(EXCEPTION);
		}
		else
		{
			// OfcName where Report Requested 
			caRpt.center(caRptProps.getOfficeIssuanceName());

			// OfcName of Reported Office 
			OfficeIdsData caOfcIds = OfficeIdsCache.getOfcId(ciOfcId);
			String csRptOfcName = caOfcIds.getOfcName();

			if (caRptProps
				.getOfficeIssuanceName()
				.equals(csRptOfcName))
			{
				csRptOfcName = ReportConstant.RPT_5051_SUMMARY;
			}
			caRpt.center(csRptOfcName);
		}
		caRpt.blankLines(ReportConstant.NUM_LINES_1);

		generateReportCriteriaHeader();

		if (!cbException)
		{
			caRpt.print(
				EXMPT_TRANS_HDR,
				HDR_TRANS_STARTPT,
				EXMPT_TRANS_HDR.length());
			// defect 9021
			caRpt.print(
				EXMPT_CURR_OWNER_HDR,
				HDR_CURROWNER_STARTPT,
				EXMPT_CURR_OWNER_HDR.length());
			// end defect 9021	
			caRpt.print(
				EXMPT_PRIOR_NEW_HDR,
				HDR_PRIOR_NEW1_STARTPT,
				EXMPT_PRIOR_NEW_HDR.length());
			caRpt.print(
				EXMPT_PRIOR_NEW_HDR,
				HDR_PRIOR_NEW2_STARTPT,
				EXMPT_PRIOR_NEW_HDR.length());
			caRpt.print(
				EXMPT_PRIOR_NEW_HDR,
				HDR_PRIOR_NEW3_STARTPT,
				EXMPT_PRIOR_NEW_HDR.length());
			caRpt.print(
				EXMPT_PRIOR_NEW_HDR,
				HDR_PRIOR_NEW4_STARTPT,
				EXMPT_PRIOR_NEW_HDR.length());
			caRpt.print(
				EXMPT_TRANSID_HDR,
				HDR_TRANSID_STARTPT,
				EXMPT_TRANSID_HDR.length());
			caRpt.nextLine();
			caRpt.print(
				EXMPT_CODE_HDR,
				HDR2_CODE_STARTPT,
				EXMPT_CODE_HDR.length());
			// defect 9021
			caRpt.print(
				EXMPT_OWNR_NAMES_HDR,
				HDR2_OWNRNAMES_STARTPT,
				EXMPT_OWNR_NAMES_HDR.length());
			// end defect 9021	
			caRpt.print(
				EXMPT_EXMPTINDI_HDR,
				HDR2_EXMPTINDI_STARTPT,
				EXMPT_EXMPTINDI_HDR.length());
			caRpt.print(
				EXMPT_REGCLASS_DESC_HDR,
				HDR2_REGCLASS_STARTPT,
				EXMPT_REGCLASS_DESC_HDR.length());
			caRpt.print(
				EXMPT_PLT_DESC_HDR,
				HDR2_PLATEDESC_STARTPT,
				EXMPT_PLT_DESC_HDR.length());
			caRpt.print(
				EXMPT_PLTNO_HDR,
				HDR2_PLATENO_STARTPT,
				EXMPT_PLTNO_HDR.length());
			caRpt.print(
				EXMPT_EMPID_HDR,
				HDR2_EMPID_STARTPT,
				EXMPT_EMPID_HDR.length());
			caRpt.nextLine();
			caRpt.drawDashedLine();
			// Remove line under header/dashed line 
			//caRpt.blankLines(ReportConstant.NUM_LINES_1);
		}
		// end defect 10164 
	}

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Adds the report criteria header text to the report
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
		caRpt.print(
			ReportConstant.AUDIT_DATE_FROM,
			WRA_HDR_STARTPT,
			WRA_HDR_LENGTH);
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caExUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ ReportConstant.STR_TO
				+ CommonConstant.STR_SPACE_ONE
				+ caExUIData.getEndDate(),
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caExUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ ReportConstant.STR_TO
				+ CommonConstant.STR_SPACE_ONE
				+ caExUIData.getEndDate())
				.length());
		caRpt.nextLine();
		caRpt.blankLines(ReportConstant.NUM_LINES_2);
	}

	/**
	 * Format Report Data 
	 */
	private void formatdata()
	{
		// defect 10164 
		// Now prints just one row

		// defect 9017
		String lsMfDown = CommonConstant.STR_SPACE_EMPTY;
		if (caExmptAuditData.getMfDwnCd() == 1)
		{
			lsMfDown = ReportConstant.STR_ASTERISK;
			cbMfDown = true;
		}
		String lsPriorRegClassCdDesc =
			caExmptAuditData.getPriorRegClassCdDesc();
		if (lsPriorRegClassCdDesc == null)
		{
			lsPriorRegClassCdDesc = CommonConstant.STR_SPACE_EMPTY;
		}
		lsPriorRegClassCdDesc =
			UtilityMethods.addPaddingRight(
				lsPriorRegClassCdDesc,
				COL_REGCLASS_LENGTH,
				CommonConstant.STR_SPACE_ONE);

		if (caExmptAuditData.getVoidedTransIndi() == 1)
		{
			lsPriorRegClassCdDesc =
				ReportConstant.STR_VOIDED
					+ lsPriorRegClassCdDesc.substring(
						ReportConstant.STR_VOIDED.length());
		}
		caRpt.print(
			caExmptAuditData.getTransCd() + lsMfDown,
			COL_TRANSCD_STARTPT,
			COL_TRANSCD_LENGTH);
		// end defect 9017	
		caRpt.print(
			caExmptAuditData.getOwnrTtlName1(),
			COL_OWNERTTL_STARTPT,
			COL_OWNERTTL_LENGTH);
		// defect 9017
		if (caExmptAuditData.getPriorRegClassCdDesc() != null
			&& !caExmptAuditData.getPriorRegClassCdDesc().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			caRpt.print(
				UtilityMethods.addPadding(
					String.valueOf(
						caExmptAuditData.getPriorExmptIndi()),
					COL_EXMPTINDI_LENGTH,
					CommonConstant.STR_SPACE_ONE),
				COL_EXMPTINDI_STARTPT,
				COL_EXMPTINDI_LENGTH);
		}
		caRpt.print(
			lsPriorRegClassCdDesc,
			COL_REGCLASS_STARTPT,
			COL_REGCLASS_LENGTH);
		// end defect 9017	
		caRpt.print(
			caExmptAuditData.getPriorRegPltCdDesc(),
			COL_PLATEDESC_STARTPT,
			COL_PLATEDESC_LENGTH);
		caRpt.print(
			caExmptAuditData.getPriorRegPltNo(),
			COL_PLATENO_STARTPT,
			COL_PLATENO_LENGTH);
		caRpt.print(
			caExmptAuditData.getTransId(),
			COL_TRANSEMPID_STARTPT,
			COL_TRANSEMPID_LENGTH);
		caRpt.nextLine();
		caRpt.print(
			caExmptAuditData.getOwnrTtlName2(),
			COL_OWNERTTL_STARTPT,
			COL_OWNERTTL_LENGTH);
		caRpt.print(
			UtilityMethods.addPadding(
				String.valueOf(caExmptAuditData.getExmptIndi()),
				COL_EXMPTINDI_LENGTH,
				CommonConstant.STR_SPACE_ONE),
			COL_EXMPTINDI_STARTPT,
			COL_EXMPTINDI_LENGTH);
		caRpt.print(
			caExmptAuditData.getRegClassCdDesc(),
			COL_REGCLASS_STARTPT,
			COL_REGCLASS_LENGTH);
		caRpt.print(
			caExmptAuditData.getRegPltCdDesc(),
			COL_PLATEDESC_STARTPT,
			COL_PLATEDESC_LENGTH);
		caRpt.print(
			caExmptAuditData.getRegPltNo(),
			COL_PLATENO_STARTPT,
			COL_PLATENO_LENGTH);
		caRpt.print(
			caExmptAuditData.getTransEmpId(),
			COL_TRANSEMPID_STARTPT,
			COL_TRANSEMPID_LENGTH);
		caRpt.blankLines(ReportConstant.NUM_LINES_2);
		generatePageBreak(ReportConstant.NUM_LINES_2);
		// end defect 10164 
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
	 * Currently not implemented
	 */
	public Vector queryData(String asQuery)
	{
		return null;
	}
}
