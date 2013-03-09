package com.txdot.isd.rts.services.reports.title;

import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * GenStickerReceiptReport.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New Class. Ver 5.2.0			 
 * T Pederson	07/26/2004	Change header for expiration date, write 
 * 							"Invalid" to report if unable to convert 
 *							date
 * 							defect 7337, 7338 Ver 5.2.1
 * B Hargrove	07/27/2004	Was printing 'null' if no Scanner\Processor 
 * 							Number. Skip this header if nothing to print.
 *							modify generateHeaderColumns()
 * 							defect 7253 Ver 5.2.1
 * B Hargrove	07/27/2004	Was printing 0 if no Disk Number. Skip this 
 * 							header if there is no Disk Number to print.
 *							modify generateHeaderColumns()
 * 							defect 7254 Ver 5.2.1
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							methods generateBodyRpt()
 *							defect 7496 Ver 5.2.1
 * J Rue		11/23/2004	Re-formated the column heading and data 
 *							lines to satisfy this defect.
 *							Per Mark R., set print data line to single 
 *							space.
 *							modify generateBodyRpt(), 
 *								   generateHeaderColumns()
 *							defect 7380 Ver 5.2.2
 * J Rue		11/24/2004	Remove VOID from report
 *							modify generateBodyRpt(), 
 *								   generateHeaderColumns()
 *							defect 7589 Ver 5.2.2
 * J Rue		12/07/2004	Constants were renamed to better define the
 *							variables.
 *							defect 7380 Ver 5.2.2
 * K Harrell	01/26/2005	Do not print record if RSPS printNum = 0. 
 *							Also, pad month with "0".  
 *							modify generatebodyRpt()
 *							defect 7915 Ver 5.2.2
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning
 * 							modify generateHeaderColumns()
 * 							modify generateBodyRpt()
 * 							defect 8217 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3                 
 * B Hargrove	06/03/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							modify START_TEXT_NUM_TRANS, TEXT_NUM_TRANS
 * 							TEXT_MEDIA_NUMBER (refactor/rename to 
 * 							TEXT_MEDIA_NUMBER
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	07/06/2009	Implement new DealerData()
 * 							modify getCacheDealerInfo(), toPhoneFormat()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	12/16/2009	refactor object names for DealerTitleData
 * 							add buildDealerPrtData()
 * 							delete getCacheDealerInfo() 
 * 							defect 10290 Ver Defect_POS_H        
 * ---------------------------------------------------------------------
 */
/**
 * Generate the Sticker Receipt Report
 * 
 * @version	Defect_POS_H 	12/16/2009
 * @author	Michael Abernethy
 * <br>Creation Date:		09/03/2002
 */
public class GenStickerReceiptReport extends ReportTemplate
{
	// Column Print Start Position
	private final static int FORM31_COL = 5;
	private final static int PLTNO_COL = 16;
	private final static int EXP_COL = 37;
	private final static int MO_YR_COL = 36;
	private final static int VIN_COL = 61;
	private final static int STKR_COL = 79;
	private final static int TYPE_COL = 79;
	private final static int PRINTED_COL = 91;
	private final static int REPRINTED_COL = 99;
	private final static int ORIG_COL = 112;
	private final static int DATE_TIME_COL = 114;

	// Adjustment to column heading positions
	private final static int PLTNO_DATA = 19;
	private final static int EXPIRATION_DATA = 35;
	private final static int VIN_DATA = 54;
	private final static int STKR_DATA = 80;
	private final static int PRINTED_DATA = 94;
	private final static int REPRINTED_DATA = 103;
	private final static int ORIG_DATA = 111;
	private final static int START_DASH = 52;
	private final static int START_TEXT_NUM_TRANS = 19;
	private final static int START_TEXT_NUM_PRNTED = 31;
	private final static int START_TEXT_NUM_REPRINTED = 29;
	private final static int START_TOTALS = 60;

	// Header Additional Info
	private final static String DEALER_BATCH = "DEALER BATCH NO";
	private final static String TEXT_MEDIA_NUMBER =
		"RSPS SEQUENCE NUMBER";
	private final static String TEXT_SCANNER = "SCANNER/PROCESSOR ID";

	// Column Headings
	private final static String FORM31 = "FORM31";
	private final static String PLATE_NUMBER = "PLATE NUMBER";
	private final static String EXP = "EXP";
	private final static String MO_YR = "MO/YR";
	private final static String VIN = "VIN";
	private final static String STKR = "STKR";
	private final static String TYPE = "TYPE";
	private final static String PRINTED = "PRINTED";
	private final static String REPRINTED = "REPRINTED";
	private final static String ORIG_PRNT = "ORIGINAL PRINT";
	private final static String DATE_TIME = "DATE/TIME";

	// Summary Headings
	private final static String TEXT_NUM_TRANS =
		"TOTAL TRANSACTIONS ON EXTERNAL MEDIA:";
	private final static String TEXT_NUM_PRNTED =
		"STICKER/RECEIPTS PRINTED:";
	private final static String TEXT_NUM_REPRINTED =
		"STICKER/RECEIPTS REPRINTED:";
	private final static String DASH = "--------------------------";

	/**
	 * GenStickerReceiptReport constructor
	 */
	public GenStickerReceiptReport()
	{
		super();
	}

	/**
	 * GenStickerReceiptReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenStickerReceiptReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 *
	 * @param avDealer Vector
	 */
	public void formatReport(Vector avDealer)
	{
		try
		{
			DealerTitleData laBaseDlrTtlData =
				(DealerTitleData) avDealer.get(0);
			generateHeaderColumns(laBaseDlrTtlData);
			generateBodyRpt(avDealer);
			// defect 8628 
			//generateEndOfReport();
			//generateFooter();
			generateFooter(true);
			// end defect 8628  
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in formatReport() of "
					+ "com.txdot.isd.rts.services.reports.title");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Generate the body of the Sticker/Receipt Report
	 * 
	 * @param avDealerData Vector
	 */
	protected void generateBodyRpt(Vector avDlrTtlData)
	{
		int liNumTrans = 0;
		int liNumPrinted = 0;
		int liNumReprinted = 0;
		// defect 7915 
		//  1) Only print transactions where printed at RSPS 
		//  2) Pad month with "0"
		//  3) Right justify Reprinted column
		// 	4) Do not include POS print numbers  
		// (only applicable after POS processing)
		for (int i = 0; i < avDlrTtlData.size(); i++)
		{
			DealerTitleData laDlrTtlData =
				(DealerTitleData) avDlrTtlData.get(i);

			// defect 8217
			//	Reset getters to match DealerTitleData's 
			//if (laDlrData.getBlackBoxPrintNum() > 0)
			if (laDlrTtlData.getRSPSPrntInvQty() > 0)
				// end defect 8217
			{
				// 	Print Form31 Number
				String lsForm31No = laDlrTtlData.getForm31No();
				caRpt.print(
					lsForm31No,
					FORM31_COL,
					lsForm31No.length());

				// Print Plate Number
				String lsPlateNum = laDlrTtlData.getNewPltNo();
				if (lsPlateNum == null)
				{
					lsPlateNum = "      ";
				}
				caRpt.print(
					lsPlateNum,
					PLTNO_DATA,
					lsPlateNum.length());

				// Print Exp Mo/Yr 
				String lsExpMonth =
					UtilityMethods.addPadding(
						Integer.toString(laDlrTtlData.getNewRegExpMo()),
						2,
						"0");
				String lsExpDate =
					lsExpMonth
						+ "/"
						+ String.valueOf(laDlrTtlData.getNewRegExpYr());
				caRpt.print(
					lsExpDate,
					EXPIRATION_DATA,
					lsExpDate.length());

				// Print VIN
				String lsVin =
					laDlrTtlData
						.getMFVehicleData()
						.getVehicleData()
						.getVin();
				if (lsVin == null)
				{
					lsVin = "                 ";
				}
				caRpt.print(lsVin, VIN_DATA, lsVin.length());

				// Print Sticker type
				String lsStickerDesc =
					laDlrTtlData
						.getMFVehicleData()
						.getRegData()
						.getRegStkrCd();
				if (lsStickerDesc == null)
				{
					lsStickerDesc = "  ";
				}
				caRpt.print(
					lsStickerDesc,
					STKR_DATA,
					lsStickerDesc.length());

				// Print Printed/Reprinted/Original Print Date
				// if ((laDlrData.getBlackBoxPrintNum() + 
				// laDlrData.getPosPrintNum()) > 0)
				caRpt.print("1", PRINTED_DATA, "1".length());
				liNumPrinted++;

				// Do not total RSPS and POS sticker printed
				// if ((laDlrData.getBlackBoxPrintNum() + 
				// laDlrData.getPosPrintNum()) > 1)

				// defect 8217
				//	Reset getters to match DealerTitleData's 
				//if ((laDlrData.getBlackBoxPrintNum()) > 1)
				if ((laDlrTtlData.getRSPSPrntInvQty()) > 1)
				{
					int liPrintNum =
						laDlrTtlData.getRSPSPrntInvQty() - 1;
					String lsPrintNum = "" + liPrintNum;
					int liReprintLoc =
						REPRINTED_DATA + 1 - lsPrintNum.length();
					caRpt.print(
						lsPrintNum,
						liReprintLoc,
						lsPrintNum.length());
					liNumReprinted += liPrintNum;
				}

				// Print Original Print Date
				if (laDlrTtlData.getRSPSOrigPrntDate() != null
					&& laDlrTtlData.getRSPSOrigPrntDate().length() > 0)
				{
					String lsOrigPrntDate =
						laDlrTtlData.getRSPSOrigPrntDate();
					String lsYr = lsOrigPrntDate.substring(0, 4);
					String lsMo = lsOrigPrntDate.substring(4, 6);
					String lsDay = lsOrigPrntDate.substring(6, 8);
					String lsHour = lsOrigPrntDate.substring(8, 10);
					String lsMins = lsOrigPrntDate.substring(10);
					String lsDateTime =
						lsMo
							+ "/"
							+ lsDay
							+ "/"
							+ lsYr
							+ " "
							+ lsHour
							+ ":"
							+ lsMins;
					if (laDlrTtlData.getRSPSOrigPrntDate().length()
						> 0)
					{
						caRpt.print(
							lsDateTime,
							ORIG_DATA,
							lsDateTime.length());
					}
				}
				caRpt.nextLine();
			}
		}
		liNumTrans = avDlrTtlData.size();
		// end defect 7915 
		caRpt.print(DASH, START_DASH, DASH.length());
		caRpt.blankLines(1);
		caRpt.nextLine();

		// Print total transactions on diskette 
		caRpt.print(
			TEXT_NUM_TRANS,
			START_TEXT_NUM_TRANS,
			TEXT_NUM_TRANS.length());
		String lsNumTrans = "" + liNumTrans;
		caRpt.print(
			lsNumTrans,
			START_TOTALS - lsNumTrans.length(),
			lsNumTrans.length());
		caRpt.nextLine();

		// Print total printed 
		caRpt.print(
			TEXT_NUM_PRNTED,
			START_TEXT_NUM_PRNTED,
			TEXT_NUM_PRNTED.length());
		String lsTotPrinted = "" + liNumPrinted;
		caRpt.print(
			lsTotPrinted,
			START_TOTALS - lsTotPrinted.length(),
			lsTotPrinted.length());
		caRpt.nextLine();

		// Print total reprinted 
		caRpt.print(
			TEXT_NUM_REPRINTED,
			START_TEXT_NUM_REPRINTED,
			TEXT_NUM_REPRINTED.length());
		String lsNumReprinted = "" + liNumReprinted;
		caRpt.print(
			lsNumReprinted,
			START_TOTALS - lsNumReprinted.length(),
			lsNumReprinted.length());
		caRpt.nextLine();
	}

	/**
	 * Generate column headings for the Sticker/Receipt report
	 * 
	 * @param  aaDlrTtlData DealerTitleData
	 * @throws RTSException 
	 */
	private void generateHeaderColumns(DealerTitleData aaDlrTtlData)
		throws RTSException
	{
		DecimalFormat laThreeDecimal = new DecimalFormat("000");
		// Define Header/Column vectors to be passed
		Vector lvHeader = new Vector();
		Vector lvDetails = new Vector();
		Vector lvTable = new Vector();

		// get Dealer Batch Number and Sequence Number
		String lsDealerId =
			laThreeDecimal.format(aaDlrTtlData.getDealerId());
		String lsDealerSeqNo = aaDlrTtlData.getDealerSeqNo();
		String lsBatchSeqNo = lsDealerId + "-" + lsDealerSeqNo;

		lvHeader.add(DEALER_BATCH);
		if (lsBatchSeqNo != null)
		{
			lvHeader.add(lsBatchSeqNo);
		}
		if (aaDlrTtlData.getRSPSId() != null
			&& !aaDlrTtlData.getRSPSId().equals(""))
		{
			lvHeader.add(TEXT_SCANNER);
			lvHeader.add("" + aaDlrTtlData.getRSPSId());
		}

		if (aaDlrTtlData.getRSPSDiskSeqNo() > 0)
		{
			lvHeader.add(TEXT_MEDIA_NUMBER);
			lvHeader.add("" + aaDlrTtlData.getRSPSDiskSeqNo());
		}
		// Add Dealer Info to vector
		lvDetails = buildDealerPrtData();

		//	The cloumn headings are split into 2 print lines
		//	Line 1 "EXP"/"STKR"/"ORIGINAL PRINT"
		// Adding ColumnHeader1 Information
		Vector lvRows = new Vector();
		ColumnHeader laColumn1 =
			new ColumnHeader(EXP, EXP_COL, EXP.length());
		ColumnHeader laColumn2 =
			new ColumnHeader(STKR, STKR_COL, STKR.length());
		ColumnHeader laColumn3 =
			new ColumnHeader(ORIG_PRNT, ORIG_COL, ORIG_PRNT.length());
		lvRows.add(laColumn1);
		lvRows.add(laColumn2);
		lvRows.add(laColumn3);
		lvTable.add(lvRows);

		//	The cloumn headings are split into 2 print lines
		//	Line 2 is a pre-existing print line with verbage changes
		//  Change Date to Form31No
		//	Change EXPIRATION DATE to MO/YR
		//	Change STICKER DESC to TYPE
		//	Change SOLD to PRINTED
		//  Add DATE/TIME
		// Adding ColumnHeader2 Information	
		lvRows = new Vector();
		laColumn1 =
			new ColumnHeader(FORM31, FORM31_COL, FORM31.length());
		laColumn2 =
			new ColumnHeader(
				PLATE_NUMBER,
				PLTNO_COL,
				PLATE_NUMBER.length());
		laColumn3 = new ColumnHeader(MO_YR, MO_YR_COL, MO_YR.length());
		ColumnHeader laColumn4 =
			new ColumnHeader(VIN, VIN_COL, VIN.length());
		ColumnHeader laColumn5 =
			new ColumnHeader(TYPE, TYPE_COL, TYPE.length());
		ColumnHeader laColumn6 =
			new ColumnHeader(PRINTED, PRINTED_COL, PRINTED.length());
		ColumnHeader laColumn8 =
			new ColumnHeader(
				REPRINTED,
				REPRINTED_COL,
				REPRINTED.length());
		ColumnHeader laColumn9 =
			new ColumnHeader(
				DATE_TIME,
				DATE_TIME_COL,
				DATE_TIME.length());

		//Adding ColumnHeader1 Information	
		lvRows.add(laColumn1);
		lvRows.add(laColumn2);
		lvRows.add(laColumn3);
		lvRows.add(laColumn4);
		lvRows.add(laColumn5);
		lvRows.add(laColumn6);
		lvRows.add(laColumn8);
		lvRows.add(laColumn9);
		lvTable.add(lvRows);
		generateHeader(lvHeader, lvDetails, lvTable);
	}

	/**
	 * buildDealerPrtData
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector buildDealerPrtData() throws RTSException
	{
		Vector lvDealerInfo = new Vector();

		DealerData laDealerData = Transaction.getDTADealerData();

		if (laDealerData != null)
		{
			lvDealerInfo = laDealerData.getDealerInfoVector();
		}
		return lvDealerInfo;

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
}