package com.txdot.isd.rts.services.reports.reports;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ReprintData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Matrix;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * GenReprintStickerReport.java
 *
 * (c) Texas Department of Transporation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *	 						Ver 5.2.0
 * K Harrell	07/30/2004	Remove REQUEST DATE from Header
 *							modify generateHeaderColumns()
 *							defect 7385  Ver 5.2.1
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3   
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport(Vector,Map)
 * 							defect 8628 Ver Defect_POS_F      
 * ---------------------------------------------------------------------
 */
/**
 * Generate the Reprint Sticker Report
 *
 * @version	Defect_POS_F  	08/10/2009
 * @author	Nancy Ting
 * <br>Creation Date: 		09/04/2002
 */
public class GenReprintStickerReport extends ReportTemplate
{
	private final static String TEXT_BEGIN_DATE = "BEGIN DATE";
	private final static String TEXT_END_DATE = "END DATE";
	private final static String TEXT_OFCISSUANCENO = "OFFISSUANCENO";
	private final static String TEXT_ORIGINATION = "ORIGINATION";
	private final static String TEXT_ITEM_CODE = "ITEM CODE";
	private final static String TEXT_ITEM_DESC = "ITEM DESC";
	private final static String TEXT_ITEM_YEAR = "ITEM YEAR";
	private final static String TEXT_QTY = "QTY";
	private final static int COLUMN1 = 3;
	private final static int COLUMN2 = 25;
	private final static int COLUMN3 = 44;
	private final static int COLUMN4 = 67;
	private final static int COLUMN5 = 97;
	private final static int COLUMN6 = 116;
	private final static int COLUMN1_OFFSET = 5;
	private final static int COLUMN2_OFFSET = 3;
	private final static int COLUMN3_OFFSET = 3;
	private final static int COLUMN4_OFFSET = -3;
	private final static int COLUMN5_OFFSET = 3;
	private final static int COLUMN6_OFFSET = 2;
	private final static int START_DASH = 111;
	private final static String DASH = "-------------";
	private final static String DOUBLEDASH = "=============";
	private final static String TEXT_TOTAL = "TOTAL";
	private final static int START_TEXT_TOTAL = 100;
	private Map aaProperties;

	/**
	 * GenReprintStickerReport constructor
	 */
	public GenReprintStickerReport()
	{
		super();
	}

	/**
	 * GenReprintStickerReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenReprintStickerReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * Format Report
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * Format Report
	 * 
	 * @param lvRecords Vector
	 * @param laMap Map
	 */
	public void formatReport(Vector lvRecords, Map laMap)
	{
		try
		{
			aaProperties = laMap;
			generateHeaderColumns(aaProperties);
			// since the report displays summaries for each ofc,
			// create a matrix, where each row
			// contains the data for one ofc
			Matrix laMatrix = sortByOfc(lvRecords);
			generateBodyRpt(laMatrix);
			// defect 8628 
			// generateEndOfReport();
			// generateFooter();
			generateFooter(true); 
			// end defect 8628 
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in formatReport() of "
					+ "com.txdot.isd.rts.services.reports.title");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Generate Attributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Generate Body Rpt
	 * 
	 * @param laMatrix Matrix
	 */
	protected void generateBodyRpt(Matrix laMatrix)
	{
		// cycle through the matrix, so we can 
		// sum up each ofc
		// the matrix vector contains vectors, where
		// each vector contains ReprintStickerReportData
		// with the same ofc
		int liTotalReprints = 0;
		for (int i = 0; i < laMatrix.size(); i++)
		{
			int liOfficeReprints = 0;
			String lsPreviousOrigination = "";
			String lsPreviousStickerCode = "";
			int liPreviousYear = 0;
			for (int j = 0; j < laMatrix.size(i); j++)
			{
				ReprintData laData = (ReprintData) laMatrix.get(i, j);
				// column 1 only print if it's the first record
				if (j == 0)
				{
					String lsOfc = "" + laData.getOfcIssuanceNo();
					caRpt.print(
						lsOfc,
						COLUMN1 + COLUMN1_OFFSET,
						lsOfc.length());
				}
				// column 2
				String lsOrig = laData.getOrigin();
				if (!lsOrig.equals(lsPreviousOrigination))
				{
					caRpt.print(
						lsOrig,
						COLUMN2 + COLUMN2_OFFSET,
						lsOrig.length());
				}
				// column 3
				String lsItemCode = laData.getItmCd();
				if (!(lsOrig.equals(lsPreviousOrigination)
					&& lsItemCode.equals(lsPreviousStickerCode)
					&& laData.getItmYr() != liPreviousYear))
				{
					caRpt.print(
						lsItemCode,
						COLUMN3 + COLUMN3_OFFSET,
						lsItemCode.length());
				}
				// column 4
				String lsItemDesc = laData.getItmCdDesc();
				if (!(lsOrig.equals(lsPreviousOrigination)
					&& lsItemCode.equals(lsPreviousStickerCode)
					&& laData.getItmYr() != liPreviousYear))
				{
					caRpt.print(
						lsItemDesc,
						COLUMN4 + COLUMN4_OFFSET,
						lsItemDesc.length());
				}
				// column 5
				String lsYear = "" + laData.getItmYr();
				caRpt.print(
					lsYear,
					COLUMN5 + COLUMN5_OFFSET,
					lsYear.length());
				// column 6
				String lsQty = "" + laData.getPrntQty();
				caRpt.print(
					lsQty,
					COLUMN6 + COLUMN6_OFFSET - lsQty.length(),
					lsQty.length());
				liOfficeReprints += laData.getPrntQty();
				// store values before getting next record
				lsPreviousOrigination = lsOrig;
				lsPreviousStickerCode = lsItemCode;
				liPreviousYear = laData.getItmYr();
				boolean lbDidPageBreak = handlePageBreak(1);
				if (!lbDidPageBreak)
				{
					caRpt.nextLine();
				}
			}
			liTotalReprints += liOfficeReprints;
			handlePageBreak(3);
			// print out total reprints for this ofc
			caRpt.print(DASH, START_DASH, DASH.length());
			caRpt.nextLine();
			String lsOfficeReprints = "" + liOfficeReprints;
			caRpt.print(
				lsOfficeReprints,
				COLUMN6 + COLUMN6_OFFSET - lsOfficeReprints.length(),
				lsOfficeReprints.length());
			caRpt.blankLines(1);
			caRpt.nextLine();
		}
		handlePageBreak(3);
		// print out total reprints for all ofc's
		caRpt.nextLine();
		caRpt.blankLines(1);
		caRpt.print(DOUBLEDASH, START_DASH, DOUBLEDASH.length());
		caRpt.nextLine();
		caRpt.print(TEXT_TOTAL, START_TEXT_TOTAL, TEXT_TOTAL.length());
		String lsTotalReprints = "" + liTotalReprints;
		caRpt.print(
			lsTotalReprints,
			COLUMN6 + COLUMN6_OFFSET - lsTotalReprints.length(),
			lsTotalReprints.length());
	}
	
	/**
	 * Generate Header Columns
	 * 
	 * @param laMap Map
	 */
	private void generateHeaderColumns(Map laMap)
	{
		// Define Header/Column vectors to be passed
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		// provided date range information
		RTSDate laStartDate =
			(RTSDate) laMap.get(ReportConstant.BEGIN_DATE);
		RTSDate laEndDate =
			(RTSDate) laMap.get(ReportConstant.END_DATE);
		// defect 7385
		//vctHeader.addElement(TEXT_REQUEST_DATE);
		//vctHeader.addElement(
		//	""
		//		+ RTSDate.getCurrentDate()
		//		+ " "
		//		+ RTSDate.getCurrentDate().getTime());
		// end defect 7385
		lvHeader.addElement(TEXT_BEGIN_DATE);
		lvHeader.addElement(laStartDate.toString());
		lvHeader.addElement(TEXT_END_DATE);
		lvHeader.addElement(laEndDate.toString());
		//Adding ColumnHeader Information	
		Vector lvRows = new Vector();
		ColumnHeader laColumn1 =
			new ColumnHeader(
				TEXT_OFCISSUANCENO,
				COLUMN1,
				TEXT_OFCISSUANCENO.length());
		ColumnHeader laColumn2 =
			new ColumnHeader(
				TEXT_ORIGINATION,
				COLUMN2,
				TEXT_ORIGINATION.length());
		ColumnHeader laColumn3 =
			new ColumnHeader(
				TEXT_ITEM_CODE,
				COLUMN3,
				TEXT_ITEM_CODE.length());
		ColumnHeader laColumn4 =
			new ColumnHeader(
				TEXT_ITEM_DESC,
				COLUMN4,
				TEXT_ITEM_DESC.length());
		ColumnHeader laColumn5 =
			new ColumnHeader(
				TEXT_ITEM_YEAR,
				COLUMN5,
				TEXT_ITEM_YEAR.length());
		ColumnHeader laColumn6 =
			new ColumnHeader(TEXT_QTY, COLUMN6, TEXT_QTY.length());
		//Additing ColumnHeader1 Information	
		lvRows.addElement(laColumn1);
		lvRows.addElement(laColumn2);
		lvRows.addElement(laColumn3);
		lvRows.addElement(laColumn4);
		lvRows.addElement(laColumn5);
		lvRows.addElement(laColumn6);
		lvTable.addElement(lvRows);
		generateHeader(lvHeader, lvTable);
	}

	/**
	 * Handle Page Break
	 * 
	 * @param aiNumLinesNeeded int
	 */
	private boolean handlePageBreak(int aiNumLinesNeeded)
	{
		int liNumLinesLeft = getNoOfDetailLines();
		if (liNumLinesLeft < aiNumLinesNeeded)
		{
			generateFooter();
			generateHeaderColumns(aaProperties);
			return true;
		}
		return false;
	}

	/**
	 * Query Data
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		return null;
	}

	/**
	 * Sort By Ofc
	 * 
	 * @param avVector Vector
	 * @return Matrix
	 */
	private Matrix sortByOfc(Vector avVector)
	{
		int liCurrentPos = 0;
		int liSize = avVector.size();
		Matrix laMatrix = new Matrix();
		while (liCurrentPos < liSize)
		{
			ReprintData laRD = (ReprintData) avVector.get(liCurrentPos);
			laMatrix.add(new Vector());
			laMatrix.add(laMatrix.size() - 1, laRD);
			while (liCurrentPos + 1 < liSize
				&& laRD.getOfcIssuanceNo()
					== ((ReprintData) avVector.get(liCurrentPos + 1))
						.getOfcIssuanceNo())
			{
				laMatrix.add(
					laMatrix.size() - 1,
					(ReprintData) avVector.get(liCurrentPos + 1));
				liCurrentPos++;
			}
			liCurrentPos++;
		}
		return laMatrix;
	}
}
