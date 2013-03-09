package com.txdot.isd.rts.services.reports.reports;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.CertifiedLienholderData;
import com.txdot.isd.rts.services.data.ElectronicTitleHistoryData;
import com.txdot.isd.rts.services.data.ElectronicTitleHistoryUIData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.reports.funds.GenFeeReport;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenElectronicTitleReport.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2009	Created
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	07/10/2009	Implement new LienholderData
 * 							modify generateLienholdersException() 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport(),
 * 							 generateLienholdersExceptionPage(),
 * 							 generateOfficeExceptionPage()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	08/18/2009	add generateLienholdersExceptionHeader() 
 * 							modify generateLienholdersExceptionPage 
 * 							defect 10167 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */

/**
 * Generates the Electronic Title Report 
 *
 * @version	Defect_POS_F	08/18/2009
 * @author	Kathy Harrell 
 * <br>Creation Date:		03/20/2009
 */
public class GenElectronicTitleReport extends ReportTemplate
{
	private int ciOfcSize = 0;
	private int ciOfcTotal = 0;
	private int ciRptTotal = 0;

	private String csOfcNo;

	private Vector cvELTRptData = new Vector();
	private Vector cvLienhldrWithNoData = new Vector();
	private Vector cvOfcIdWithNoData = new Vector();

	private Hashtable chtOfcLienholder = new Hashtable();
	private Hashtable chtOffices = new Hashtable();
	private Hashtable chtTotalLienholder = new Hashtable();

	private ElectronicTitleHistoryData caETTLHstryData = null;
	private ElectronicTitleHistoryUIData caETtlHstryUIData =
		new ElectronicTitleHistoryUIData();

	private final static int COL_ONE = 1; 
	private final static int COL_EMPID_LENGTH = 8;
	private final static int COL_EMPID_STARTPT = 152;
	private final static int COL_LIENHOLDERID_LENGTH = 11;
	private final static int COL_LIENHOLDERID_STARTPT = 32;
	private final static int COL_LIENHOLDERNAME_LENGTH = 30;
	private final static int COL_LIENHOLDERNAME_STARTPT = 48;
	private final static int COL_OWNER_LENGTH = 30;
	private final static int COL_OWNER_STARTPT = 119;
	private final static int COL_PLATENO_LENGTH = 8;
	private final static int COL_PLATENO_STARTPT = 105;
	private final static int COL_TRANSCD_LENGTH = 7;
	private final static int COL_TRANSCD_STARTPT = 1;
	private final static int COL_TRANSID_LENGTH = 17;
	private final static int COL_TRANSID_STARTPT = 11;
	private final static int COL_VIN_LENGTH = 22;
	private final static int COL_VIN_STARTPT = 80;
	private final static int NUM_ROWS_FOR_HDR_TOTAL = 3;
	private final static int TOTAL_FIELD_WIDTH = 10;
	private final static String DASHES = "----------";
	private final static String EMPID_HDR = "EMPID";
	private final static String LIENHOLDER_NAME_HDR =
		"LIENHOLDER NAME(S)";
	private final static String LIENHOLDERID_HDR = "LIENHOLDER ID";
	private final static String NO_RECS_FOR_LIEN =
		"NO RECORDS FOUND FOR LIENHOLDERS: ";
	private final static String NO_RECS_FOR_OFCS =
		"NO RECORDS FOUND FOR OFFICES: ";
	private final static String OWNR_NAMES_HDR = "OWNER NAME(S)";
	private final static String PLTNO_HDR = "PLATE NO";
	private final static String TOTALS = " TOTALS:";
	private final static String TRANS_TYPE = "TRANSACTION TYPE";
	private final static String TRANSACTIONID_HDR = "TRANSACTION ID";
	private final static String TRANSCD_HDR = "TRANSCD";
	private final static String TXT_EXCEPTION = "EXCEPTION";
	private final static String VIN_HDR = "VIN";
	private final static int WRA_HDR_LENGTH = 21;
	private final static int WRA_HDR_STARTPT = 1;
	private final static int WRA_RSLTS_STARTPT = 22;

	/** 
	 * Internal Data class to track Transaction Counts per Entity
	 *
	 * @version	Defect_POS_E	03/20/2009
	 * @author	Kathy Harrell 
	 * <br>Creation Date:		03/20/2009
	 */
	private class EntityCountData implements Comparable
	{
		private int ciTransCount;
		private String csEntityId;

		/**
		 *  Compare To  
		 */
		public int compareTo(Object aaObject)
		{
			String lsId = ((EntityCountData) aaObject).getEntityId();
			return lsId.compareTo(csEntityId);
		}
		/** 
		 * Increment counter 
		 * 
		 * @param aiCount
		 */
		public void addTransCount(int aiCount)
		{
			ciTransCount = ciTransCount + aiCount;
		}
		/**
		 * Get value of TransCount 
		 * 
		 * @return
		 */
		public int getTransCount()
		{
			return ciTransCount;
		}
		/**
		 * Get value of EntityId
		 * 
		 * @return String 
		 */
		public String getEntityId()
		{
			return csEntityId;
		}
		/**
		 * Set value of TransCount
		 * 
		 * @param aiCount
		 */
		public void setTransCount(int aiCount)
		{
			ciTransCount = aiCount;
		}
		/**
		 * Set value of EntityId  (Key)  
		 * 
		 * @param asId
		 */
		public void setEntityId(String asId)
		{
			csEntityId = asId;
		}
	}
	public static void main(String[] args)
	{
		// Not currently implemented 
	}

	/**
	 * GenElectronicTitleReport constructor
	 */
	public GenElectronicTitleReport()
	{
		super();
	}

	/**
	 * GenElectronicTitleReport constructor
	 */
	public GenElectronicTitleReport(
		String asRptString,
		ReportProperties aaRptProperties,
		ElectronicTitleHistoryUIData aaUIData)
	{
		super(asRptString, aaRptProperties);
		this.caETtlHstryUIData = aaUIData;
	}

	/**
	 * Add Lien  
	 */
	private void addAsLienWithData(String asPermLienhldrId)
	{
		// Lien Count per Office 
		EntityCountData laLienCtData = new EntityCountData();

		if (chtOfcLienholder.containsKey(asPermLienhldrId))
		{
			laLienCtData =
				(EntityCountData) chtOfcLienholder.get(
					asPermLienhldrId);
		}
		else
		{
			laLienCtData.setEntityId(asPermLienhldrId);
		}
		laLienCtData.addTransCount(1);
		chtOfcLienholder.put(asPermLienhldrId, laLienCtData);

		// Total Lien Count  
		EntityCountData laTotalLienCtData = new EntityCountData();

		if (chtTotalLienholder.containsKey(asPermLienhldrId))
		{
			laTotalLienCtData =
				(EntityCountData) chtTotalLienholder.get(
					asPermLienhldrId);
		}
		else
		{
			laTotalLienCtData.csEntityId = asPermLienhldrId;
		}
		laTotalLienCtData.addTransCount(1);
		chtTotalLienholder.put(asPermLienhldrId, laTotalLienCtData);
	}

	/**
	 * Add Found OfcIssuanceNo 
	 */
	private void addAsOfficeWithData(String asOfcNo)
	{
		if (!chtOffices.containsKey(asOfcNo))
		{
			chtOffices.put(asOfcNo, asOfcNo);
		}
	}

	/**
	 * Identify Lienholders Without Data
	 */
	private void findLienhldrIdsWithoutData()
	{
		Vector lvLienData =
			caETtlHstryUIData.getSelectedPermLienhldrId();

		for (int i = 0; i < lvLienData.size(); i++)
		{
			String lsPermLienhldrId =
				lvLienData.elementAt(i).toString();

			if (!chtTotalLienholder.containsKey(lsPermLienhldrId))
			{
				cvLienhldrWithNoData.add(lsPermLienhldrId);
			}
		}
	}

	/**
	 * Identify Offices Without Data
	 */
	private void findOfficesWithoutData()
	{
		Vector lvOfficeData = caETtlHstryUIData.getSelectedOffices();
		for (int i = 0; i < lvOfficeData.size(); i++)
		{
			csOfcNo = lvOfficeData.elementAt(i).toString();

			if (!chtOffices.containsKey(csOfcNo))
			{
				cvOfcIdWithNoData.add(csOfcNo);
			}
		}
	}

	/**
	 * Format Report Data 
	 */
	private void formatdata()
	{
		String lsLienName1 = caETTLHstryData.getCertfdLienHldrName1();

		if (caETTLHstryData.getVoidedTransIndi() == 1)
		{
			lsLienName1 = ReportConstant.STR_VOIDED;
		}
		else
		{
			ciOfcTotal++;
			addAsLienWithData(caETTLHstryData.getPermLienhldrId());
		}
		// TransCd 
		caRpt.print(
			caETTLHstryData.getTransCd(),
			COL_TRANSCD_STARTPT,
			COL_TRANSCD_LENGTH);

		// TransId 
		caRpt.print(
			caETTLHstryData.getTransId(),
			COL_TRANSID_STARTPT,
			COL_TRANSID_LENGTH);

		// PermLienholder Id 
		caRpt.print(
			caETTLHstryData.getPermLienhldrId(),
			COL_LIENHOLDERID_STARTPT,
			COL_LIENHOLDERID_LENGTH);

		// Lienholder Name 
		caRpt.print(
			lsLienName1,
			COL_LIENHOLDERNAME_STARTPT,
			COL_LIENHOLDERNAME_LENGTH);

		// VIN 
		caRpt.print(
			caETTLHstryData.getVIN(),
			COL_VIN_STARTPT,
			COL_VIN_LENGTH);

		// PLATE NO 
		caRpt.print(
			caETTLHstryData.getRegPltNo(),
			COL_PLATENO_STARTPT,
			COL_PLATENO_LENGTH);

		// OWNER NAME(S) 
		caRpt.print(
			caETTLHstryData.getOwnrTtlName1(),
			COL_OWNER_STARTPT,
			COL_OWNER_LENGTH);
		caRpt.print(
			caETTLHstryData.getTransEmpId(),
			COL_EMPID_STARTPT,
			COL_EMPID_LENGTH);

		String lsLienName2 = caETTLHstryData.getCertfdLienHldrName2();
		String lsOwnrName2 = caETTLHstryData.getOwnrTtlName2();

		if (!UtilityMethods.isEmpty(lsLienName2)
			|| !UtilityMethods.isEmpty(lsOwnrName2))
		{
			caRpt.nextLine();

			if (!UtilityMethods.isEmpty(lsLienName2))
			{
				caRpt.print(
					lsLienName2,
					COL_LIENHOLDERNAME_STARTPT,
					COL_LIENHOLDERNAME_LENGTH);
			}
			if (!UtilityMethods.isEmpty(lsOwnrName2))
			{
				caRpt.print(
					lsOwnrName2,
					COL_OWNER_STARTPT,
					COL_OWNER_LENGTH);

			}
		}
		caRpt.blankLines(ReportConstant.NUM_LINES_2);
		generatePageBreak(ReportConstant.NUM_LINES_2);
	}

	/**
	 * The formatReport() generates the Electronic Title Report
	 *
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		cvELTRptData = avResults;
		ciOfcSize = caETtlHstryUIData.getSelectedOffices().size();
		int liResultSize = cvELTRptData.size();
		String lsPrevOfcNo = "";
		csOfcNo =
			caETtlHstryUIData
				.getSelectedOffices()
				.elementAt(0)
				.toString();

		try
		{
			for (int i = 0; i < liResultSize; i++)
			{
				caETTLHstryData =
					(
						ElectronicTitleHistoryData) cvELTRptData
							.elementAt(
						i);

				csOfcNo =
					String.valueOf(caETTLHstryData.getOfcIssuanceNo());

				if (!csOfcNo.equals(lsPrevOfcNo))
				{
					if (lsPrevOfcNo != "")
					{
						generateTransTotal();
						generateFooter();
					}

					addAsOfficeWithData(csOfcNo);
					chtOfcLienholder = new Hashtable();
					generateHeader();
				}
				formatdata();
				lsPrevOfcNo = csOfcNo;
			}

			findOfficesWithoutData();
			findLienhldrIdsWithoutData();

			// Check for no data returned for office(s) selected
			if (liResultSize == 0)
			{
				// Single page when only one office selected
				if (ciOfcSize == 1)
				{
					generateHeader();
					generateNoRecordFoundMsg();
					// defect 8628 
					// generateEndOfReport();
					// generateFooter();
					generateFooter(true);
					// end defect 8628  

				}
				else
				{
					generateOfficeExceptionPage();
				}
			}
			else
			{
				generateTransTotal();
				if (ciOfcSize == 1 && cvLienhldrWithNoData.size() == 0)
				{
					// defect 8628 
					// generateEndOfReport();
					// generateFooter();
					generateFooter(true);
					// end defect 8628  
				}
				else
				{
					generateFooter();
					// Create exception page when more than one office selected
					// and no data returned for at least one of the selections
					if (cvOfcIdWithNoData.size() > 0)
					{
						generateOfficeExceptionPage();
					}
					if (cvLienhldrWithNoData.size() > 0)
					{
						generateLienholdersExceptionPage();
					}
					// Finish report when more than one office with data 
					if (chtOffices.values().size() > 1)
					{
						generateRptTotal();
						// defect 8628 
						// generateEndOfReport();
						// generateFooter();
						generateFooter(true);
						// end defect 8628  
					}
				}
			}
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
		caRpt.print(
			TRANSCD_HDR,
			COL_TRANSCD_STARTPT,
			TRANSCD_HDR.length());

		caRpt.print(
			TRANSACTIONID_HDR,
			COL_TRANSID_STARTPT,
			TRANSACTIONID_HDR.length());

		caRpt.print(
			LIENHOLDERID_HDR,
			COL_LIENHOLDERID_STARTPT,
			LIENHOLDERID_HDR.length());

		caRpt.print(
			LIENHOLDER_NAME_HDR,
			COL_LIENHOLDERNAME_STARTPT,
			LIENHOLDER_NAME_HDR.length());

		caRpt.print(VIN_HDR, COL_VIN_STARTPT, VIN_HDR.length());

		caRpt.print(PLTNO_HDR, COL_PLATENO_STARTPT, PLTNO_HDR.length());

		caRpt.print(
			OWNR_NAMES_HDR,
			COL_OWNER_STARTPT,
			OWNR_NAMES_HDR.length());

		caRpt.print(EMPID_HDR, COL_EMPID_STARTPT, EMPID_HDR.length());

		caRpt.nextLine();
		caRpt.drawDashedLine();
	}

	/**
	 * The generateHeader() adds text that will be printed when needed
	 */
	private void generateHeader()
	{
		OfficeIdsData caOfcIds =
			OfficeIdsCache.getOfcId(Integer.parseInt(csOfcNo));
		String csRptOfcName = caOfcIds.getOfcName();

		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		if (caRptProps.getOfficeIssuanceName().equals(csRptOfcName))
		{
			csRptOfcName = ReportConstant.RPT_5071_SUMMARY;
		}
		caRpt.center(csRptOfcName);
		caRpt.blankLines(ReportConstant.NUM_LINES_1);
		generateReportCriteriaHeader();
		generateColumnHeaders();
	}
	/** 
	 * 
	 * Method description
	 * 
	 */
	private void generateLienholdersExceptionHeader()
	{
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(TXT_EXCEPTION);
		generateReportCriteriaHeader();
		caRpt.print(NO_RECS_FOR_LIEN);
		caRpt.blankLines(ReportConstant.NUM_LINES_1);
	}
	
	/**
	 * Prints the list of Lienholders with no data returned
	 */
	private void generateLienholdersExceptionPage()
	{
		if (cvLienhldrWithNoData.size() > 0)
		{

			// defect 10167
			// Do not sort by PermLienhldrId;  Sort on RPR007 by
			//  Selected Row Number  
			//UtilityMethods.sort(cvLienhldrWithNoData); 
			generateLienholdersExceptionHeader();
			
			int liLastRowNo =  cvLienhldrWithNoData.size() -1 ; 
			// end defect 10167
			
			for (int i = 0; i < cvLienhldrWithNoData.size(); i++)
			{
				String lsLienhldrId =
					cvLienhldrWithNoData.get(i).toString();
					
				CertifiedLienholderData caCertLienData =
					CertifiedLienholderCache.getLatestCertfdLienhldr(
						lsLienhldrId);
						
				if (caCertLienData != null)
				{
					// defect 10112 
					String lsLienName = caCertLienData.getName1();
					// end defect 10112 

					lsLienName = lsLienhldrId + "   " + lsLienName;
					caRpt.print(
						lsLienName,
						COL_ONE,
						lsLienName.length());
					caRpt.nextLine();
					
					// defect 10167 
					if (i != liLastRowNo
						&& getNoOfDetailLines()
							< ReportConstant.NUM_LINES_2)
					{
						generateFooter();
						generateLienholdersExceptionHeader();
					}
					// end defect 10167 
				}
			}
		}
		// defect 8628 
		//if (chtOffices.values().size() == 1 || ciRptTotal == 0)
		//{
		//	generateEndOfReport();
		//}
		//generateFooter();
		generateFooter(
			chtOffices.values().size() == 1 || ciRptTotal == 0);
		// end defect 8628  
	}

	/**
	 * Prints the list of offices with no data returned
	 */
	private void generateOfficeExceptionPage()
	{
		if (cvOfcIdWithNoData.size() > 0)
		{
			caRpt.print(caRptProps.getUniqueName());
			caRpt.center(caRpt.csName);
			caRpt.center(TXT_EXCEPTION);
			generateReportCriteriaHeader();
			caRpt.print(NO_RECS_FOR_OFCS);
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
			// if (cvOfcIdWithNoData.size() == ciOfcSize
			// 	|| (cvLienhldrWithNoData.size() == 0
			// 		&& chtOffices.values().size() == 1)
			//	|| ciRptTotal == 0)
			//{
			//	generateEndOfReport();
			//}
			// generateFooter(); 

			boolean lbEOF =
				cvOfcIdWithNoData.size() == ciOfcSize
					|| (cvLienhldrWithNoData.size() == 0
						&& chtOffices.values().size() == 1)
					|| ciRptTotal == 0;
			generateFooter(lbEOF);
			// end defect 8628 
		}
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
		caRpt.print(TRANS_TYPE, WRA_HDR_STARTPT, WRA_HDR_LENGTH);

		String lsTransType = "";
		Vector lvTransCd = caETtlHstryUIData.getSelectedTransCd();
		UtilityMethods.sort(lvTransCd);
		for (int i = 0; i < lvTransCd.size(); i++)
		{
			if (i != 0)
			{
				lsTransType = lsTransType + ", ";
			}
			lsTransType = lsTransType + (String) lvTransCd.get(i);
		}
		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ lsTransType,
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ lsTransType)
				.length());

		caRpt.nextLine();

		caRpt.print(
			GenFeeReport.TRANSACTION_HEADER_STRING,
			WRA_HDR_STARTPT,
			WRA_HDR_LENGTH);

		caRpt.print(
			CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caETtlHstryUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ GenFeeReport.THROUGH_HEADER_STRING
				+ CommonConstant.STR_SPACE_ONE
				+ caETtlHstryUIData.getEndDate(),
			WRA_RSLTS_STARTPT,
			(CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ caETtlHstryUIData.getBeginDate()
				+ CommonConstant.STR_SPACE_ONE
				+ GenFeeReport.THROUGH_HEADER_STRING
				+ CommonConstant.STR_SPACE_ONE
				+ caETtlHstryUIData.getEndDate())
				.length());
		caRpt.nextLine();
		caRpt.blankLines(ReportConstant.NUM_LINES_2);
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
			caRpt.center(ReportConstant.RPT_5071_SUMMARY);
			generateReportCriteriaHeader();
			caRpt.blankLines(ReportConstant.NUM_LINES_2);

			Vector lvCertfd = new Vector(chtTotalLienholder.values());
			UtilityMethods.sort(lvCertfd);

			caRpt.print(
				UtilityMethods.addPadding(
					TOTALS,
					COL_LIENHOLDERID_LENGTH,
					CommonConstant.STR_SPACE_ONE),
				COL_LIENHOLDERID_STARTPT,
				COL_LIENHOLDERID_LENGTH);
			caRpt.nextLine();

			int liRptTotal = 0;

			for (int i = 0; i < lvCertfd.size(); i++)
			{
				EntityCountData laData =
					(EntityCountData) lvCertfd.elementAt(i);

				caRpt.print(
					laData.csEntityId,
					COL_LIENHOLDERID_STARTPT,
					COL_LIENHOLDERID_LENGTH);

				CertifiedLienholderData laCertData =
					CertifiedLienholderCache.getLatestCertfdLienhldr(
						laData.csEntityId);

				caRpt.print(
					laCertData.getName1(),
					COL_LIENHOLDERNAME_STARTPT,
					COL_LIENHOLDERNAME_LENGTH);

				String lsTotal =
					UtilityMethods.addPadding(
						"" + laData.getTransCount(),
						TOTAL_FIELD_WIDTH,
						" ");

				liRptTotal = liRptTotal + laData.getTransCount();
				caRpt.print(
					lsTotal,
					COL_VIN_STARTPT,
					TOTAL_FIELD_WIDTH);
				caRpt.nextLine();
			}
			caRpt.print(
				UtilityMethods.addPadding(
					DASHES,
					TOTAL_FIELD_WIDTH,
					CommonConstant.STR_SPACE_ONE),
				COL_VIN_STARTPT,
				TOTAL_FIELD_WIDTH);

			caRpt.nextLine();

			caRpt.print(
				UtilityMethods.addPadding(
					"" + liRptTotal,
					TOTAL_FIELD_WIDTH,
					CommonConstant.STR_SPACE_ONE),
				COL_VIN_STARTPT,
				TOTAL_FIELD_WIDTH);

			caRpt.blankLines(ReportConstant.NUM_LINES_2);

		}
	}

	/**
	 * Prints the total number of transactions for an office
	 * 
	 */
	private void generateTransTotal()
	{
		if (ciOfcTotal > 0)
		{
			caRpt.blankLines(ReportConstant.NUM_LINES_1);

			Vector lvCertfd = new Vector(chtOfcLienholder.values());
			UtilityMethods.sort(lvCertfd);

			// Ensure enough room to print totals  
			generatePageBreak(lvCertfd.size() + NUM_ROWS_FOR_HDR_TOTAL);

			caRpt.print(
				UtilityMethods.addPadding(
					TOTALS,
					COL_LIENHOLDERID_LENGTH,
					CommonConstant.STR_SPACE_ONE),
				COL_LIENHOLDERID_STARTPT,
				COL_LIENHOLDERID_LENGTH);

			caRpt.nextLine();

			for (int i = 0; i < lvCertfd.size(); i++)
			{
				EntityCountData laData =
					(EntityCountData) lvCertfd.elementAt(i);

				caRpt.print(
					laData.csEntityId,
					COL_LIENHOLDERID_STARTPT,
					COL_LIENHOLDERID_LENGTH);

				CertifiedLienholderData laCertData =
					CertifiedLienholderCache.getLatestCertfdLienhldr(
						laData.csEntityId);

				caRpt.print(
					laCertData.getName1(),
					COL_LIENHOLDERNAME_STARTPT,
					COL_LIENHOLDERNAME_LENGTH);

				String lsTotal =
					UtilityMethods.addPadding(
						"" + laData.getTransCount(),
						TOTAL_FIELD_WIDTH,
						CommonConstant.STR_SPACE_ONE);

				caRpt.print(
					lsTotal,
					COL_VIN_STARTPT,
					TOTAL_FIELD_WIDTH);

				caRpt.nextLine();
			}

			caRpt.print(
				UtilityMethods.addPadding(
					DASHES,
					TOTAL_FIELD_WIDTH,
					CommonConstant.STR_SPACE_ONE),
				COL_VIN_STARTPT,
				TOTAL_FIELD_WIDTH);

			caRpt.nextLine();

			caRpt.print(
				UtilityMethods.addPadding(
					"" + ciOfcTotal,
					TOTAL_FIELD_WIDTH,
					CommonConstant.STR_SPACE_ONE),
				COL_VIN_STARTPT,
				TOTAL_FIELD_WIDTH);

			ciRptTotal = ciRptTotal + ciOfcTotal;
			chtOfcLienholder = new Hashtable();
			ciOfcTotal = 0;
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
