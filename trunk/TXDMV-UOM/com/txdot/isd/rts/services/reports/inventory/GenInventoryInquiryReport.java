package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.Dealer;
import com.txdot.isd.rts.server.db.Security;
import com.txdot.isd.rts.server.db.Subcontractor;

/*
 * 
 * GenInventoryInquiryReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * E LyBrand 	10/12/2001	New Class
 * BTulsiani 	04/17/2002  Added fixes for bug 3481
 * Jeff Rue		05/08/2002	Modified printTotalQty for bug 3791
 * Ray Rowehl	05/17/2002	Fix Defect 3935 for Specific Item in
 * 							History.
 * Kathy Harrell			Add handling for null date on headings.
 * Min Wang					Also fix duplicate heading call
 * Ray Rowehl	05/23/2002	defect 3935
 *							Only print the history heading on
 *							selected item if there is not a current
 *							balance.
 *				05/29/2002	Also need to print the whole vector for
 *							the	history of a specific item.
 *							set up to print column headings for a 
 *							specific item that has current balance
 *							and history..
 * Ray Rowehl	05/29/2002	Extend the dash line on Specific Item 
 *							to the page width.
 *							defect 4142
 * Ray Rowehl	05/30/2002	Make the item length 12 so it will right
 * 							justify.  also right align the column 
 * 							headers for itemno.
 *							defect 4142
 * Min Wang		07/31/2002  Fixed Specific Item Number match begin
 * 							and end number.
 * Min Wang		08/30/2002	Fixed defect CQU100004689 for centering 
 * 							"History" on the rtspos3031.
 * Min Wang		09/04/2002	Fixed defect CQU100004715 listing the
 * 							item twice same plate number on history
 * 							report. 
 * Min Wang		11/07/2002	Modified processHistoryRpt().
 * 							defect 5043 
 * Min Wang		01/07/2002	Modified formatReport() and
 * 							processHistoryRpt to fix the last 
 *  						item is duplicated on the second page of
 * 							the rpt. 
 * 							defect 5216
 * Min Wang		12/03/2003  Test if a new page is needed before
 * 							print End of Report.
 * 							Modified formatReport()
 *   						defect 6671. Version 5.1.5 Fix 2 	
 * Min Wang		05/19/2005	Add check to see which kind of data is
 * 							being used to generate the column 
 * 							headings when at the end of the report.  
 * 							modify formatReport()	
 * 							defect 8204	Ver 5.2.3	
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3   
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify getCachePlateTypeDesc(), main()
 *							defect 7896 Ver 5.2.3 
 * Min Wang		08/01/2005  Remove item code from report.
 * 							delete multiple constants
 * 							modify generateCurrCoulmns(), 
 * 							generateExceptionColumn(), 
 * 							generateHistColumns(), printCurrBalDataline(),
 * 							printExceptionDataLine(), printHistDataline()
 * 							defect 8269 Ver 5.2.2 Fix 6
 * K Harrell	08/12/2005 	Add'l formatting work
 *							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	09/29/2005	Constants work for 5.2.3
 * 							defect 7890 Ver 5.2.3  
 * Min Wang		04/04/2007	display status on the report.	
 * 							modify printCurrBalDataline()
 * 							defect 9117 Ver Special Plates 
 * Min Wang		07/18/2007	add inv status case 2 and constants.
 * 							add TXT_STATUS_C, TXT_STATUS_S, TXT_STATUS_U
 * 							modify printCurrBalDataline()
 *  				 		defect 9117 Ver Special Plates 
 * Min Wang		08/15/2007	Print Available Quantity if the report type 
 * 							is Virtual.
 * 							add DTL_STARTPT_50, TXT_AVAILABLE_QUANTITY
 * 							modify printTotalQty()
 * 							defect 9254 Ver Special Plates
 * K Harrell	07/12/2009	Implement new DealerData()
 * 							modify getCacheDlrIdName() 
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	02/18/2010 	Implement new SubcontractorData
 * 							modify getCacheSubconIdName()
 * 							defect 10161 Ver POS_640     
 * ---------------------------------------------------------------------
 */

/**
 * This class generates the Inventory Inquiry Report
 *
 * @version	POS_640  		02/18/2010 
 * @author  Edward LyBrand
 * <br>Creation Date: 		10/12/2001 05:16:32
 */

public class GenInventoryInquiryReport extends ReportTemplate
{
	private static final int NUMBER_DASHES_TOTAL = 10;
	private static final int DELETE_CODE_VOID = 5;
	private static final int ITEMNOLENGTH = 12;
	private static final int NUMBER_OF_BLANK_LINES = 2;
	private static final int RESERVE_FOOTER_LINES = 3;
	private static final int WHITE_SPACE = 6;

	//	Index for Inventory UI Data
	private static final int POS_INV_INQ_DATA = 0;
	//	Index for Current Balance vector
	private static final int POS_CUR_BAL_VECTOR = 1;
	//	Index for History vector 
	private static final int POS_HIS_BAL_VECTOR = 2;

	private final static int HDR_STARTPT_67 = 67;

	//Column positions for row 1, current balance
	private final static int HDR_CUR_STARTPT_1_1 = 4;
	private final static int HDR_CUR_STARTPT_1_2 = 37;
	private final static int HDR_CUR_STARTPT_1_3 = 43;
	private final static int HDR_CUR_STARTPT_1_4 = 58;
	private final static int HDR_CUR_STARTPT_1_5 = 76;

	//Column positions for row 2, current balance
	private final static int HDR_CUR_STARTPT_2_1 = 4;
	private final static int HDR_CUR_STARTPT_2_2 = 37;
	private final static int HDR_CUR_STARTPT_2_3 = 43;
	private final static int HDR_CUR_STARTPT_2_4 = 58;
	private final static int HDR_CUR_STARTPT_2_5 = 76;
	private final static int HDR_CUR_STARTPT_2_6 = 90;
	private final static int HDR_CUR_STARTPT_2_7 = 102;
	private final static int HDR_CUR_STARTPT__2_8 = 112;

	//Column position for row 1, history balance
	private final static int HDR_HIS_STARTPT_1_1 = 4;
	private final static int HDR_HIS_STARTPT_1_2 = 37;
	private final static int HDR_HIS_STARTPT_1_3 = 43;
	private final static int HDR_HIS_STARTPT_1_4 = 58;
	private final static int HDR_HIS_STARTPT_1_5 = 90;
	private final static int HDR_HIS_STARTPT_1_6 = 111;

	//Column position for row 2, history balance
	private final static int HDR_HIS_STARTPT_2_1 = 4;
	private final static int HDR_HIS_STARTPT_2_2 = 37;
	private final static int HDR_HIS_STARTPT_2_3 = 43;
	private final static int HDR_HIS_STARTPT_2_4 = 58;
	private final static int HDR_HIS_STARTPT_2_5 = 78;
	private final static int HDR_HIS_STARTPT_2_6 = 90;
	private final static int HDR_HIS_STARTPT_2_7 = 111;
	private final static int HDR_HIS_STARTPT_2_8 = 119;
	private final static int HDR_HIS_STARTPT_2_9 = 125;

	private final static int DTL_LENGTH_01 = 1;
	private final static int DTL_LENGTH_04 = 4;
	private final static int DTL_LENGTH_132 = 132;

	private final static int DTL_STARTPT_01 = 1;
	private final static int DTL_STARTPT_04 = 4;
	private final static int DTL_STARTPT_08 = 8;
	private final static int DTL_STARTPT_37 = 37;
	// defect
	private final static int DTL_STARTPT_50 = 50;
	// end defect 
	private final static int DTL_STARTPT_54 = 54;
	private final static int DTL_STARTPT_74 = 74;
	private final static int DTL_STARTPT_76 = 76;

	private static final String DECIMAL_FORMAT_3DIGITS = "000";
	private static final String DECIMAL_FORMAT_6DIGITS = "000000";
	
	private static final String HDR_BEGIN = "BEGIN ";
	private static final String HDR_CODE = "CODE";
	private static final String HDR_DESCRIPTION = "DESCRIPTION";
	private static final String HDR_END = "END   ";
	private static final String HDR_ID = "ID";
	private static final String HDR_ITEM = "ITEM";
	private static final String HDR_NUMBER = "NUMBER";
	private static final String HDR_ON_HAND = "ON HAND";
	private static final String HDR_QUANTITY = "QUANTITY";
	private static final String HDR_STATUS = "STATUS";
	private static final String HDR_YEAR = "YEAR";

	private static final String MSG_EXCEPTION_OCCURRED =
		"Exception occurred in formatReport() of com.txdot.isd.rts."
			+ "services.reports.GenTitlePackageReport";
	private static final String MSG_NO_RECORDS_FOR_TYPE =
		"** NO RECORDS WERE FOUND FOR THE FOLLOWING ITEM TYPES(S) **";

	private static final String SAMPLE_FILE_NAME =
			"c:\\RTS\\RPT\\INVINQ.TXT";
		private static final String SAMPLE_QUERY = "Select * FROM RTS_TBL";
		
	private static final String TXT_CENTRAL = "CENTRAL";
	private static final String TXT_CODE = "CODE";
	private static final String TXT_CURRENT_BALANCE = "CURRENT BALANCE";
	private static final String TXT_DEALER = "DEALER";
	private static final String TXT_EMPLOYEE = "EMPLOYEE";
	private static final String TXT_EXCEPTION_REPORT =
		"EXCEPTION REPORT";
	private static final String TXT_HELD = "HELD";
	private static final String TXT_HISTORY = "HISTORY";
	private static final String TXT_HISTORY_DATE_FROM =
		"HISTORY DATE FROM";
	private static final String TXT_ID_COLON = " ID: ";
	private static final String TXT_INQUIRY_BY = "INQUIRY BY";
	private static final String TXT_ITEM_TYPE_AND_YEAR =
		"ITEM TYPE(S) AND YEAR";
	private static final String TXT_NOT_KNOWN = "NOT KNOWN";
	private static final String TXT_SPACE_DASH_SPACE =
		CommonConstant.STR_SPACE_ONE
			+ CommonConstant.STR_DASH
			+ CommonConstant.STR_SPACE_ONE;
	private static final String TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR =
		"SPECIFIC ITEM NUMBER AND YEAR";
	private static final String TXT_SUBCONTRACTOR = "SUBCONTRACTOR";
	private static final String TXT_TOTAL_QUANTITY = "TOTAL QUANTITY";
	// defect 9254
	private static final String TXT_AVAILABLE_QUANTITY = 
				"AVAILABLE QUANTITY";
	// end defect 9254
	private static final String TXT_TRANS = "TRANS";
	private static final String TXT_TRANSACTION = "TRANSACTION";
	private static final String TXT_TYPE = "TYPE";
	private static final String TXT_VOID = "VOID";
	private static final String TXT_WORKSTATION = "WORKSTATION";
	private static final String TXT_STATUS_S = "HELD-S";
	private static final String TXT_STATUS_U = "HELD-U";
	private static final String TXT_STATUS_C = "HELD-C";
	//housekeeping
	private int ciPrintLinesAvailable;
	private String csPrevRptType;
	private String csPrevInvId;
	private String csPrevItmCd;
	private int ciPrevInvItmYr;

	public int ciTotalQty = 0;
	public int ciRecordIndex = 0;
	private int ciPrntRowAdj = 0;

	//	Class vectors
	Vector cvCurrBal = new Vector();
	Vector cvHistBal = new Vector();
	Vector cvExcCurr = new Vector();
	Vector cvExcHist = new Vector();

	//	define data object
	InventoryInquiryReportData caInvInqCurrBal =
		new InventoryInquiryReportData();
	TransactionInventoryDetailData caInvInqHistBal =
		new TransactionInventoryDetailData();
	InventoryInquiryUIData caInvInqUIData =
		new InventoryInquiryUIData();

	/**
	 * GenInventoryInquiryReport default constructor
	 */
	public GenInventoryInquiryReport()
	{
		// empty code block
	}

	/**
	 * General Inventory Inquiry Report constructor
	 * 
	 * @param asRptString String
	 * @param asRptProperties ReportProperties
	 */
	public GenInventoryInquiryReport(
		String asRptString,
		ReportProperties asRptProperties)
	{
		super(asRptString, asRptProperties);
	}

	/**
	 * Make available print line adjustment.
	 * 
	 * @param aiCntr int
	 * @return int
	 */
	private int adjPrntLinesAvail(int aiCntr)
	{
		int liRtn = aiCntr;

		// Set counter index
		if (getPrntRowAdj() > 0)
		{
			liRtn = aiCntr + getPrntRowAdj();
		} // end if

		setPrntRowAdj(0);

		return liRtn;
	}

	/**
	 * Build Excpt Curr Bal
	 */
	private void buildExcptCurrBal()
	{
		String lsItmCd = CommonConstant.STR_SPACE_EMPTY;
		String lsItmCdCurr = CommonConstant.STR_SPACE_EMPTY;
		String lsItmCdYr = CommonConstant.STR_SPACE_EMPTY;
		String lsItmCdYrCurr = CommonConstant.STR_SPACE_EMPTY;
		String lsPltCdDesc = CommonConstant.STR_SPACE_EMPTY;
		boolean lbFound = true;

		Vector lvHoldData = new Vector();
		cvExcCurr = new Vector();

		if (caInvInqUIData
			.getRptType()
			.equals(InventoryConstant.CUR_BAL)
			|| caInvInqUIData.getRptType().equals(
				InventoryConstant.CUR_BAL_HISTORY))
		{
			if (cvCurrBal.size() == 0)
			{
				// Capture all the Item codes selected
				for (int i = 0;
					i < caInvInqUIData.getItmCds().size();
					i++)
				{
					lsItmCd =
						(String) caInvInqUIData.getItmCds().elementAt(
							i);
					lvHoldData.addElement(lsItmCd);
					lsPltCdDesc = getCachePlateTypeDesc(lsItmCd);
					lvHoldData.addElement(lsPltCdDesc);

					lsItmCdYr =
						String.valueOf(
							(Integer) caInvInqUIData
								.getInvItmYrs()
								.elementAt(
								i));
					if (lsItmCdYr.equals(CommonConstant.STR_ZERO))
					{
						lsItmCdYr = CommonConstant.STR_SPACE_EMPTY;
					}
					lvHoldData.addElement(lsItmCdYr);
					cvExcCurr.addElement(lvHoldData);
					lvHoldData = new Vector();
				} // end for loop
			} // end if
			else
			{
				// Capture only the Item codes not found
				for (int i = 0;
					i < caInvInqUIData.getItmCds().size();
					i++)
				{
					lsItmCd =
						(String) caInvInqUIData.getItmCds().elementAt(
							i);
					lsItmCdYr =
						String.valueOf(
							(Integer) caInvInqUIData
								.getInvItmYrs()
								.elementAt(
								i));
					for (int j = 0; j < cvCurrBal.size(); j++)
					{
						caInvInqCurrBal =
							(
								InventoryInquiryReportData) cvCurrBal
									.elementAt(
								j);
						lsItmCdCurr = caInvInqCurrBal.getItmCd();
						lsItmCdYrCurr =
							String.valueOf(
								caInvInqCurrBal.getInvItmYr());
						if (lsItmCd.equals(lsItmCdCurr)
							&& lsItmCdYr.equals(lsItmCdYrCurr))
						{
							lbFound = true;
							break;
						}
						else
						{
							lbFound = false;
						}

					} // end for loop, current balance
					if (!lbFound)
					{
						lvHoldData.addElement(lsItmCd);
						lsPltCdDesc = getCachePlateTypeDesc(lsItmCd);
						lvHoldData.addElement(lsPltCdDesc);
						if (lsItmCdYr.equals(CommonConstant.STR_ZERO))
						{
							lsItmCdYr = CommonConstant.STR_SPACE_EMPTY;
						}
						lvHoldData.addElement(lsItmCdYr);
						cvExcCurr.addElement(lvHoldData);
						lvHoldData = new Vector();
					} // end if
				} // end for loop, user selection
			} // end else
		} // end if/Current balance exception report
		else
		{
			cvExcCurr = new Vector();
		} // end else
	}

	/**
	 * Build Excpt Hist
	 */
	private void buildExcptHist()
	{
		String lsItmCd = CommonConstant.STR_SPACE_EMPTY;
		String lsItmCdHist = CommonConstant.STR_SPACE_EMPTY;
		String lsItmCdYr = CommonConstant.STR_SPACE_EMPTY;
		String lsItmCdYrHist = CommonConstant.STR_SPACE_EMPTY;
		String lsPltCdDesc = CommonConstant.STR_SPACE_EMPTY;
		boolean lbFound = true;

		Vector lvHoldData = new Vector();
		cvExcHist = new Vector();

		if (caInvInqUIData
			.getRptType()
			.equals(InventoryConstant.HISTORY)
			|| caInvInqUIData.getRptType().equals(
				InventoryConstant.CUR_BAL_HISTORY))
		{
			if (cvHistBal == null || cvHistBal.size() == 0)
			{
				// Capture all the Item codes selected
				for (int i = 0;
					i < caInvInqUIData.getItmCds().size();
					i++)
				{
					lsItmCd =
						(String) caInvInqUIData.getItmCds().elementAt(
							i);
					lvHoldData.addElement(lsItmCd);
					lsPltCdDesc = getCachePlateTypeDesc(lsItmCd);
					lvHoldData.addElement(lsPltCdDesc);

					lsItmCdYr =
						String.valueOf(
							(Integer) caInvInqUIData
								.getInvItmYrs()
								.elementAt(
								i));
					if (lsItmCdYr.equals(CommonConstant.STR_ZERO))
					{
						lsItmCdYr = CommonConstant.STR_SPACE_EMPTY;
					}
					lvHoldData.addElement(lsItmCdYr);
					cvExcHist.addElement(lvHoldData);
					lvHoldData = new Vector();
				} // end for loop
			} // end if
			else
			{
				// Capture only the Item codes not found
				for (int i = 0;
					i < caInvInqUIData.getItmCds().size();
					i++)
				{
					lsItmCd =
						(String) caInvInqUIData.getItmCds().elementAt(
							i);
					lsItmCdYr =
						String.valueOf(
							(Integer) caInvInqUIData
								.getInvItmYrs()
								.elementAt(
								i));
					for (int j = 0; j < cvHistBal.size(); j++)
					{
						caInvInqHistBal =
							(
								TransactionInventoryDetailData) cvHistBal
									.elementAt(
								j);
						lsItmCdHist = caInvInqHistBal.getItmCd();
						lsItmCdYrHist =
							String.valueOf(
								caInvInqHistBal.getInvItmYr());
						if (lsItmCd.equals(lsItmCdHist)
							&& lsItmCdYr.equals(lsItmCdYrHist))
						{
							lbFound = true;
							break;
						}
						else
							lbFound = false;
					} // end for loop, history
					if (!lbFound)
					{
						lvHoldData.addElement(lsItmCd);
						lsPltCdDesc = getCachePlateTypeDesc(lsItmCd);
						lvHoldData.addElement(lsPltCdDesc);
						if (lsItmCdYr.equals(CommonConstant.STR_ZERO))
						{
							lsItmCdYr = CommonConstant.STR_SPACE_EMPTY;
						}
						lvHoldData.addElement(lsItmCdYr);
						cvExcHist.addElement(lvHoldData);
						lvHoldData = new Vector();
					} // end if
				} // end for loop, user selection
			} // end else
		} // end if/Current balance exception report
		else
		{
			cvExcHist = new Vector();
		} // end else
	}

	/**
	 * Find the start position position for a string
	 * 
	 * @param aiColLngth int - length of longest column header
	 * @param asStr String
	 * @return int - pointer position
	 */
	private int center(int aiColLngth, String asStr)
	{
		int liMidPoint = (int) aiColLngth / 2;
		int liStartPoint = (liMidPoint - (int) asStr.length() / 2);

		return liStartPoint;
	}

	/**
	 * Calculate the character length og the input string.
	 * 
	 * @param asInputStr String
	 * @return int
	 */
	public int charLength(String asInputStr)
	{
		int lsCharLngth = asInputStr.length();

		return lsCharLngth;
	}

	/**
	 * This method controls formatting of the report
	 *
	 * <p>Vector avResults contains three objects.
	 * <ol>
	 * <li>Inventory UI Data
	 * <li>Current Balance vector
	 * <li>History vector
	 * <eol>
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		//create variables, arrays, and so forth
		//boolean lbIdBreak = true; //page break
		boolean lbMoreRecords = false; //mo' recs for processing

		// Load inventroy Inquiry UI Data into object
		caInvInqUIData =
			(InventoryInquiryUIData) avResults.elementAt(
				POS_INV_INQ_DATA);
		cvCurrBal = (Vector) avResults.elementAt(POS_CUR_BAL_VECTOR);
		cvHistBal = (Vector) avResults.elementAt(POS_HIS_BAL_VECTOR);

		//get number of printlines available for the report
		ciPrintLinesAvailable = getNoOfDetailLines();

		try
		{
			// try box
			// ****************** CURRENT BALANCE **********************

			// Print body of current report, exclude Specific Item Number
			// & Year
			//  Get the first record
			if (cvCurrBal != null
				&& cvCurrBal.size() != 0
				&& !caInvInqUIData.getInvInqBy().equals(
					TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
			{
				// print report heading, intermediate text,
				// column headings
				caInvInqCurrBal =
					(InventoryInquiryReportData) cvCurrBal.elementAt(
						CommonConstant.ELEMENT_0);
				generateColumnHeadings(
					caInvInqCurrBal.getInvItmNo(),
					TXT_CURRENT_BALANCE,
					false);
				saveCurrentItems(
					caInvInqCurrBal.getInvId(),
					caInvInqCurrBal.getItmCd(),
					TXT_CURRENT_BALANCE,
					caInvInqCurrBal.getInvItmYr());
				lbMoreRecords = true;

				// If there are more records to process,
				// print footer/column headings                
				while (lbMoreRecords)
				{ // Process records for Current Balance
					lbMoreRecords = processCurrentBalanceRpt();

					// If more Current Balance records to process
					if (lbMoreRecords)
					{
						//print footer
						generateFooter();
						//print report heading, intermediate text,
						// column headings 
						generateColumnHeadings(
							caInvInqCurrBal.getInvItmNo(),
							TXT_CURRENT_BALANCE,
							false);
					} // end if more records print footer/column heading      
				} // end while: more records

				// ********** Process last record ********** 
				// get number of printlines remaining for the report
				ciPrintLinesAvailable = getNoOfDetailLines();

				if (ciPrintLinesAvailable < RESERVE_FOOTER_LINES)
				{
					generateFooter();
					generateColumnHeadings(
						caInvInqCurrBal.getInvItmNo(),
						TXT_CURRENT_BALANCE,
						false);
				} // end if
				processLastRecord(
					caInvInqCurrBal.getInvId(),
					caInvInqCurrBal.getItmCd(),
					caInvInqCurrBal.getInvItmYr(),
					TXT_CURRENT_BALANCE);

				//Reset quanities
				setTotalQty(0);
			} //end if: current balance

			// ********************** HISTORY **************************

			setRecordIndex(0);
			// Print body of history report, exclude Specific
			// Item Number & Year
			if (cvHistBal != null
				&& cvHistBal.size() != CommonConstant.ELEMENT_0
				&& !caInvInqUIData.getInvInqBy().equals(
					TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
			{
				// Test to print footer
				if (cvCurrBal.size() > CommonConstant.ELEMENT_0)
				{
					generateFooter();
				} // end if

				//Print report heading, intermediate text, column headings
				caInvInqHistBal =
					(
						TransactionInventoryDetailData) cvHistBal
							.elementAt(
						CommonConstant.ELEMENT_0);
				generateColumnHeadings(
					caInvInqHistBal.getInvItmNo(),
					TXT_HISTORY,
					false);
				// reset - get number of printlines available for the report
				ciPrintLinesAvailable = getNoOfDetailLines();

				saveCurrentItems(
					caInvInqHistBal.getInvId(),
					caInvInqHistBal.getItmCd(),
					TXT_HISTORY,
					caInvInqHistBal.getInvItmYr());
				lbMoreRecords = true;

				while (lbMoreRecords)
				{ // Check for more records to process

					lbMoreRecords = processHistoryRpt();

					if (lbMoreRecords)
					{
						// If there are more records to process, print
						// footer/column headings
						//print footer
						generateFooter();
						//print report heading, intermediate text, column headings 
						generateColumnHeadings(
							caInvInqHistBal.getInvItmNo(),
							TXT_HISTORY,
							false);
					} // end if more records, print footer/column headings      
				} // end while: more records

				// ********** Process last record **********
				// get number of printlines remaining for the report
				ciPrintLinesAvailable = getNoOfDetailLines();

				if (ciPrintLinesAvailable < RESERVE_FOOTER_LINES)
				{
					generateFooter();
					generateColumnHeadings(
						caInvInqHistBal.getInvItmNo(),
						TXT_HISTORY,
						false);
				} // end if

				processLastRecord(
					caInvInqHistBal.getInvId(),
					caInvInqHistBal.getItmCd(),
					caInvInqHistBal.getInvItmYr(),
					TXT_HISTORY);

				//Reset quanities
				setTotalQty(0);
			} //end if: History
			// ******************* End HISTORY *************************
		} //end try block
		catch (Throwable aeEx)
		{
			System.err.println(MSG_EXCEPTION_OCCURRED);
			aeEx.printStackTrace(System.out);
		} //end catch block

		// ********* Print body of Specific Item Number & Year ********* 

		if (caInvInqUIData
			.getInvInqBy()
			.equals(TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
		{
			setRecordIndex(0);
			if (cvCurrBal != null
				&& cvCurrBal.size() > CommonConstant.ELEMENT_0)
			{
				caInvInqCurrBal =
					(InventoryInquiryReportData) cvCurrBal.elementAt(
						getRecordIndex());
				caInvInqUIData.setRptType(TXT_CURRENT_BALANCE);
				generateColumnHeadings(
					caInvInqUIData.getInvItmNo(),
					TXT_CURRENT_BALANCE,
					false);
				caInvInqCurrBal.setInvQty(caInvInqUIData.getInvQty());
				caInvInqCurrBal.setInvItmNo(
					caInvInqUIData.getInvItmNo());
				caInvInqCurrBal.setInvItmEndNo(
					caInvInqUIData.getInvItmNo());
				printCurrBalDataline();
				caRpt.print(
					caRpt.singleDashes(this.caRptProps.getPageWidth()),
					DTL_LENGTH_01,
					this.caRptProps.getPageWidth());

				// start a new page if needed.
				if (ciPrintLinesAvailable < RESERVE_FOOTER_LINES)
				{
					generateFooter();
					generateColumnHeadings(
						caInvInqHistBal.getInvItmNo(),
						TXT_HISTORY,
						false);
				} // end if

				caRpt.blankLines(NUMBER_OF_BLANK_LINES);
			} // end if - Current Balance

			if (cvHistBal != null
				&& cvHistBal.size() > CommonConstant.ELEMENT_0)
			{
				// set the record index to 0 to get the first record
				setRecordIndex(0);
				// get the first rercord
				caInvInqHistBal =
					(
						TransactionInventoryDetailData) cvHistBal
							.elementAt(
						getRecordIndex());
				// we only need Headings once here.  
				caInvInqUIData.setRptType(TXT_HISTORY);
				// only print the history heading if there is no current
				// balance
				if (cvCurrBal == null
					|| cvCurrBal.size() < CommonConstant.ELEMENT_1)
				{
					generateColumnHeadings(
						caInvInqUIData.getInvItmNo(),
						TXT_HISTORY,
						false);
				}
				else
				{
					// still need the column headings
					generateColumnHeadings(
						caInvInqUIData.getInvItmNo(),
						TXT_HISTORY,
						true);
				}

				//**************************************************
				// print history records!  Did not process the vector
				// before..

				ciPrintLinesAvailable = getNoOfDetailLines();

				saveCurrentItems(
					caInvInqHistBal.getInvId(),
					caInvInqHistBal.getItmCd(),
					TXT_HISTORY,
					caInvInqHistBal.getInvItmYr());

				lbMoreRecords = true;

				// counter to index through the history vector
				// we already have the first record.  so set to 1..
				setRecordIndex(CommonConstant.ELEMENT_1);

				// process through the records.  Note that the first
				// record has been retrieved, so print first.  Then
				// retreive the next one.
				while (lbMoreRecords)
				{
					// get number of printlines remaining for the report
					ciPrintLinesAvailable = getNoOfDetailLines();

					// start a new page if needed.
					if (ciPrintLinesAvailable < RESERVE_FOOTER_LINES)
					{
						generateFooter();
						generateColumnHeadings(
							caInvInqHistBal.getInvItmNo(),
							TXT_HISTORY,
							false);
					} // end if

					// print the detail record
					printHistDataline();

					// check to see if another record is available
					// if not, set the exit flag
					if (getRecordIndex() < cvHistBal.size())
					{
						// get the next record
						caInvInqHistBal =
							(
								TransactionInventoryDetailData) cvHistBal
									.elementAt(
								getRecordIndex());
						setRecordIndex(getRecordIndex() + 1);
					}
					else
					{
						// there are no more records.  time to leave.
						lbMoreRecords = false;
					}

				} // end while: more records

				//******************************************************
				caRpt.print(
					caRpt.singleDashes(this.caRptProps.getPageWidth()),
					DTL_STARTPT_01,
					this.caRptProps.getPageWidth());

				// start a new page if needed.
				if (ciPrintLinesAvailable < RESERVE_FOOTER_LINES)
				{
					generateFooter();
					generateColumnHeadings(
						caInvInqHistBal.getInvItmNo(),
						TXT_HISTORY,
						false);
				} // end if

				caRpt.blankLines(NUMBER_OF_BLANK_LINES);
			} // end if - History

			// **************  no records found Specific Item  *********
			else
			{
				if ((cvCurrBal == null
					|| cvCurrBal.size() < CommonConstant.ELEMENT_1)
					&& (cvHistBal == null
						|| cvHistBal.size() < CommonConstant.ELEMENT_1))
				{
					generateColumnHeadings(
						caInvInqUIData.getInvItmNo(),
						TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR,
						false);
					caRpt.blankLines(NUMBER_OF_BLANK_LINES);
					generateNoRecordFoundMsg();
					
					// defect 8628 
					// generateEndOfReport();
					// generateFooter();
					generateFooter(true);
					// end defect 8628
					  
					return;
				} // end else - No Data Found
			}
			// *********************************************************
		} // end if for Spacific Item Type and Year

		// ********* PRINT THE EXCEPTION REPORT  *********
		if (caInvInqUIData.getExceptionReport() == 1)
		{
			buildExcptCurrBal();
			// Build the Current Balance Exception Data vector
			buildExcptHist();
			// Build the History Exception Data vector
			if (cvExcCurr.size() != CommonConstant.ELEMENT_0
				|| cvExcHist.size() != CommonConstant.ELEMENT_0)
			{
				processExceptionReport();
			} // end if
		} // end if

		// test if a new page is needed before print End of Report.
		ciPrintLinesAvailable = getNoOfDetailLines();
		if (ciPrintLinesAvailable < RESERVE_FOOTER_LINES)
		{
			generateFooter();
			//test if it history or current blance
			if (caInvInqHistBal != null
				&& cvHistBal.size() > CommonConstant.ELEMENT_0)
			{
				// this is history
				generateColumnHeadings(
					caInvInqHistBal.getInvItmNo(),
					TXT_HISTORY,
					false);
			}
			else
			{
				// this is assumed to be current
				generateColumnHeadings(
					caInvInqCurrBal.getInvItmNo(),
					TXT_CURRENT_BALANCE,
					false);
			}
		}

		//print end of report
		//generateEndOfReport();
		//generateFooter();
		generateFooter(true); 
	}

	/**
	 * generateAttributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Set vector to be passed to the header.
	 * 
	 * @param asInvItm String
	 * @param asRecType String
	 * @param abColumsOnly
	 */
	private void generateColumnHeadings(
		String asInvItm,
		String asRecType,
		boolean abColumnsOnly)
	{
		// create containers
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();

		// Generate Headings for all of the reports
		lvHeader = generateHeading(asRecType);

		// Determine which columns to print, Current Balance/History
		// or Exception
		if (asRecType.equals(TXT_CURRENT_BALANCE))
		{
			lvTable = generateCurrColumns(asRecType, asInvItm);
		} // end if
		else if (asRecType.equals(TXT_HISTORY))
		{
			lvTable = generateHistColumns(asRecType, asInvItm);
		} // end if
		else if (asRecType.equals(TXT_EXCEPTION_REPORT))
		{
			lvTable = generateExceptionColumn(asInvItm);
		} // end else
		else if (asRecType.equals(TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
		{
			lvTable = generateSpecItmYrNo(asInvItm);
		} // end else

		// print the full heading is abColumnsOnly is false
		if (!abColumnsOnly)
		{
			generateHeader(lvHeader, lvTable);
		}
		else
		{
			generateHeader(lvTable);
		}
	}

	/**
	 * Generate column headers for current item reporting.
	 * 
	 * @param asRptType String
	 * @param asInvItmNo String
	 */
	public Vector generateCurrColumns(
		String asRptType,
		String asInvItmNo)
	{
		//local variables
		String lsInqBy;
		String lsInqByText;
		//String lsHistoryDate;
		int liCharStart_Center;

		//create containers
		//Vector lvCenter = new Vector();
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();
		Vector lvRow3 = new Vector();
		Vector lvRow4 = new Vector();
		Vector lvRowB = new Vector(); //blank line

		//populate the column vector
		//center add Dealer,Employee, Subcontractor or Workstation to
		// ColumnHeader, if needed
		//Parm: length of longest column header, string to be printed

		//populate column label for column report header line
		if (!((caInvInqUIData
			.getInvInqBy()
			.equals(TXT_ITEM_TYPE_AND_YEAR))
			|| (caInvInqUIData.getInvInqBy().equals(TXT_CENTRAL))))
		{
			// Dealer, Employee, Subcontracter or Specific Item Type &
			// year, call generateIdText to set id text in the column
			// header
			lsInqBy =
				generateIdText(asInvItmNo, caInvInqCurrBal.getInvId());

			// If inquiry is Specific Item Number and Year
			if (caInvInqUIData
				.getInvInqBy()
				.equals(TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
			{
				lsInqByText =
					HDR_ITEM
						+ CommonConstant.STR_SPACE_ONE
						+ HDR_NUMBER
						+ CommonConstant.STR_COLON
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_SPACE_ONE
						+ asInvItmNo;
			} // end if
			else
			{
				lsInqByText =
					caInvInqUIData.getInvInqBy()
						+ TXT_ID_COLON
						+ caInvInqCurrBal.getInvId()
						+ lsInqBy;
			} // end else

			liCharStart_Center = center(DTL_LENGTH_132, lsInqByText);
			ColumnHeader laColumn0 =
				new ColumnHeader(
					lsInqByText,
					liCharStart_Center,
					charLength(lsInqByText));
			lvRow1.addElement(laColumn0);
		} // end if

		//populate column labels for column headings line1	
		liCharStart_Center =
			center(DTL_LENGTH_132, caInvInqUIData.getRptType());
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				asRptType,
				liCharStart_Center,
				charLength(caInvInqUIData.getRptType()));
		//tbd

		//populate column label(!) for blank line
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				CommonConstant.STR_SPACE_ONE,
				DTL_STARTPT_01,
				DTL_LENGTH_04);
		//blank line

		//populate column labels for column headings line 3	
		ColumnHeader laColumn_3_1 =
			new ColumnHeader(
				HDR_ITEM,
				HDR_CUR_STARTPT_1_1,
				charLength(HDR_ITEM));
		ColumnHeader laColumn_3_2 =
			new ColumnHeader(
				HDR_ITEM,
				HDR_CUR_STARTPT_1_2,
				charLength(HDR_ITEM));
		ColumnHeader laColumn_3_3 =
			new ColumnHeader(
				HDR_BEGIN,
				HDR_CUR_STARTPT_1_3,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_3_4 =
			new ColumnHeader(
				HDR_END,
				HDR_CUR_STARTPT_1_4,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_3_5 =
			new ColumnHeader(
				HDR_ON_HAND,
				HDR_CUR_STARTPT_1_5,
				charLength(HDR_ON_HAND));

		//populate column labels for column headings line 4	
		ColumnHeader laColumn_4_1 =
			new ColumnHeader(
				HDR_DESCRIPTION,
				HDR_CUR_STARTPT_2_1,
				charLength(HDR_DESCRIPTION));
		ColumnHeader laColumn_4_2 =
			new ColumnHeader(
				HDR_YEAR,
				HDR_CUR_STARTPT_2_2,
				charLength(HDR_YEAR));
		ColumnHeader laColumn_4_3 =
			new ColumnHeader(
				HDR_NUMBER,
				HDR_CUR_STARTPT_2_3,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_4 =
			new ColumnHeader(
				HDR_NUMBER,
				HDR_CUR_STARTPT_2_4,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_5 =
			new ColumnHeader(
				HDR_QUANTITY,
				HDR_CUR_STARTPT_2_5,
				charLength(HDR_QUANTITY));
		ColumnHeader laColumn_4_6 =
			new ColumnHeader(
				HDR_STATUS,
				HDR_CUR_STARTPT_2_6,
				charLength(HDR_STATUS));
		ColumnHeader laColumn_4_7 =
			new ColumnHeader(
				TXT_CODE,
				HDR_CUR_STARTPT_2_7,
				charLength(TXT_CODE));
		ColumnHeader laColumn_4_8 =
			new ColumnHeader(
				HDR_ID,
				HDR_CUR_STARTPT__2_8,
				charLength(HDR_ID));

		//add label for report type line
		lvRow2.addElement(laColumn_1_1);

		//add label for blank line
		lvRowB.addElement(laColumn_2_1);

		//add labels for print row 3
		lvRow3.addElement(laColumn_3_1);
		lvRow3.addElement(laColumn_3_2);
		lvRow3.addElement(laColumn_3_3);
		lvRow3.addElement(laColumn_3_4);
		lvRow3.addElement(laColumn_3_5);

		//add labels for print row 3
		lvRow4.addElement(laColumn_4_1);
		lvRow4.addElement(laColumn_4_2);
		lvRow4.addElement(laColumn_4_3);
		lvRow4.addElement(laColumn_4_4);
		lvRow4.addElement(laColumn_4_5);
		lvRow4.addElement(laColumn_4_6);
		lvRow4.addElement(laColumn_4_7);
		lvRow4.addElement(laColumn_4_8);

		//add all rows to the column table
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRow2);
		lvTable.addElement(lvRowB);
		lvTable.addElement(lvRow3);
		lvTable.addElement(lvRow4);

		return lvTable;

	}
	/**
	 * Generate Exception Column
	 * 
	 * @param asRptType String
	 * @return Vector
	 */
	private Vector generateExceptionColumn(String asRptType)
	{
		int liCharStart_Center;

		//create containers
		Vector lvTable = new Vector();
		Vector lvRowB = new Vector(); //blank line

		liCharStart_Center =
			center(DTL_LENGTH_132, TXT_EXCEPTION_REPORT);
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				TXT_EXCEPTION_REPORT,
				liCharStart_Center,
				charLength(TXT_EXCEPTION_REPORT));

		Vector lvRow1 = new Vector();

		liCharStart_Center =
			center(DTL_LENGTH_132, MSG_NO_RECORDS_FOR_TYPE);
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				MSG_NO_RECORDS_FOR_TYPE,
				liCharStart_Center,
				charLength(MSG_NO_RECORDS_FOR_TYPE));

		Vector lvRow2 = new Vector();

		//populate column label for report type line
		liCharStart_Center = center(DTL_LENGTH_132, asRptType);
		ColumnHeader laColumn_3_1 =
			new ColumnHeader(
				asRptType,
				liCharStart_Center,
				charLength(asRptType));

		Vector lvRow3 = new Vector();

		//populate column labels for column headings line1

		ColumnHeader laColumn_4_1 =
			new ColumnHeader(
				HDR_ITEM,
				DTL_STARTPT_04,
				charLength(HDR_ITEM));
		ColumnHeader laColumn_4_2 =
			new ColumnHeader(
				HDR_ITEM,
				DTL_STARTPT_37,
				charLength(HDR_ITEM));
		Vector lvRow4 = new Vector();

		//populate column labels for column headings line2	
		ColumnHeader laColumn_5_1 =
			new ColumnHeader(
				HDR_DESCRIPTION,
				DTL_STARTPT_04,
				charLength(HDR_DESCRIPTION));
		ColumnHeader laColumn_5_2 =
			new ColumnHeader(
				HDR_YEAR,
				DTL_STARTPT_37,
				charLength(HDR_YEAR));
		Vector lvRow5 = new Vector();

		//add labels for print row 1
		lvRow1.addElement(laColumn_1_1);

		//add labels for print row 2
		lvRow2.addElement(laColumn_2_1);

		//add labels for print row 3
		lvRow3.addElement(laColumn_3_1);

		//add labels for print row 4
		lvRow4.addElement(laColumn_4_1);
		lvRow4.addElement(laColumn_4_2);

		//add labels for print row 5
		lvRow5.addElement(laColumn_5_1);
		lvRow5.addElement(laColumn_5_2);

		//add all rows to the column table
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRowB);
		lvTable.addElement(lvRow2);
		lvTable.addElement(lvRow3);
		lvTable.addElement(lvRowB);
		lvTable.addElement(lvRow4);
		lvTable.addElement(lvRow5);

		return lvTable;
	}

	/**
	 * Set vector to be passed to the header.
	 * 
	 * @param asRecType String
	 * @return Vector
	 */
	private Vector generateHeading(String asRecType)
	{
		//local variables
		String lsHistoryDate = CommonConstant.STR_SPACE_EMPTY;

		//create containers
		Vector lvHeader = new Vector();

		//populate header vector
		lvHeader.addElement(TXT_INQUIRY_BY);
		lvHeader.addElement(caInvInqUIData.getInvInqBy());

		// Only print the dates if they are there 
		//determine: if history report print from & to dates
		if (caInvInqUIData.getBeginDt() != null)
		{
			if (asRecType.equals(TXT_HISTORY))
			{
				lvHeader.addElement(TXT_HISTORY_DATE_FROM);
				lsHistoryDate =
					caInvInqUIData.getBeginDt().toString()
						+ TXT_SPACE_DASH_SPACE
						+ caInvInqUIData.getEndDt().toString();
				lvHeader.addElement(lsHistoryDate);
			} // end if
		}
		return lvHeader;
	}

	/**
	 * Generate Hist Columns
	 * 
	 * @param asRptType String
	 * @param asInvItmNo String
	 */
	public Vector generateHistColumns(
		String asRptType,
		String asInvItmNo)
	{
		//local variables
		String lsInqBy;
		String lsInqByText;
		//String lsHistoryDate;
		int liCharStart_Center;

		//create containers
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();
		Vector lvRow3 = new Vector(); //blank line
		Vector lvRow4 = new Vector();
		Vector lvRow5 = new Vector();

		//populate the column vector
		//center add Dealer,Employee, Subcontractor or Workstation to
		// ColumnHeader, if needed
		//Parm: length of longest column header, string to be printed

		//populate column label for column report header line
		if (!((caInvInqUIData
			.getInvInqBy()
			.equals(TXT_ITEM_TYPE_AND_YEAR))
			|| (caInvInqUIData.getInvInqBy().equals(TXT_CENTRAL))))
		{

			// If Dealer, Employee, Subcontracter or Spacific Item Type
			// & year, call generateIdText to set id text in the column
			// header
			lsInqBy =
				generateIdText(asInvItmNo, caInvInqHistBal.getInvId());
			if (caInvInqUIData
				.getInvInqBy()
				.equals(TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
			{
				lsInqByText =
					HDR_ITEM
						+ CommonConstant.STR_SPACE_ONE
						+ HDR_NUMBER
						+ CommonConstant.STR_COLON
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_SPACE_ONE
						+ lsInqBy;
			} // end if
			else
			{
				lsInqByText =
					caInvInqUIData.getInvInqBy()
						+ TXT_ID_COLON
						+ caInvInqHistBal.getInvId()
						+ lsInqBy;
			} // end else

			liCharStart_Center = center(DTL_LENGTH_132, lsInqByText);
			ColumnHeader laColumn_1_1 =
				new ColumnHeader(
					lsInqByText,
					liCharStart_Center,
					charLength(lsInqByText));
			//add label for blank line
			lvRow1.addElement(laColumn_1_1);
		} // end if

		//populate column labels for column headings line1	
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				asRptType,
				HDR_STARTPT_67,
				charLength(caInvInqUIData.getRptType()));
		//tbd

		//populate column label(!) for blank line
		ColumnHeader laColumn_3_1 =
			new ColumnHeader(
				CommonConstant.STR_SPACE_ONE,
				DTL_STARTPT_01,
				DTL_LENGTH_04);
		//blank line

		//populate column labels for column headings line2	
		ColumnHeader laColumn_4_1 =
			new ColumnHeader(
				HDR_ITEM,
				HDR_HIS_STARTPT_1_1,
				charLength(HDR_ITEM));
		ColumnHeader laColumn_4_2 =
			new ColumnHeader(
				HDR_ITEM,
				HDR_HIS_STARTPT_1_2,
				charLength(HDR_ITEM));
		ColumnHeader laColumn_4_3 =
			new ColumnHeader(
				HDR_BEGIN,
				HDR_HIS_STARTPT_1_3,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_4 =
			new ColumnHeader(
				HDR_END,
				HDR_HIS_STARTPT_1_4,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_4_5 =
			new ColumnHeader(
				TXT_TRANSACTION,
				HDR_HIS_STARTPT_1_5,
				charLength(TXT_TRANSACTION));
		ColumnHeader laColumn_4_6 =
			new ColumnHeader(
				TXT_TRANS,
				HDR_HIS_STARTPT_1_6,
				charLength(TXT_TRANS));

		//populate column labels for column headings line3	
		ColumnHeader laColumn_5_1 =
			new ColumnHeader(
				HDR_DESCRIPTION,
				HDR_HIS_STARTPT_2_1,
				charLength(HDR_DESCRIPTION));
		ColumnHeader laColumn_5_2 =
			new ColumnHeader(
				HDR_YEAR,
				HDR_HIS_STARTPT_2_2,
				charLength(HDR_YEAR));
		ColumnHeader laColumn_5_3 =
			new ColumnHeader(
				HDR_NUMBER,
				HDR_HIS_STARTPT_2_3,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_5_4 =
			new ColumnHeader(
				HDR_NUMBER,
				HDR_HIS_STARTPT_2_4,
				ITEMNOLENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_5_5 =
			new ColumnHeader(
				HDR_QUANTITY,
				HDR_HIS_STARTPT_2_5,
				charLength(HDR_QUANTITY));
		ColumnHeader laColumn_5_6 =
			new ColumnHeader(
				HDR_ID,
				HDR_HIS_STARTPT_2_6,
				charLength(HDR_ID));
		ColumnHeader laColumn_5_7 =
			new ColumnHeader(
				TXT_TYPE,
				HDR_HIS_STARTPT_2_7,
				charLength(TXT_TYPE));
		ColumnHeader laColumn_5_8 =
			new ColumnHeader(
				TXT_CODE,
				HDR_HIS_STARTPT_2_8,
				charLength(TXT_CODE));
		ColumnHeader laColumn_5_9 =
			new ColumnHeader(
				HDR_ID,
				HDR_HIS_STARTPT_2_9,
				charLength(HDR_ID));

		//add label for report type line
		lvRow2.addElement(laColumn_2_1);

		//add label for blank line
		lvRow3.addElement(laColumn_3_1);

		//add labels for print row 1
		lvRow4.addElement(laColumn_4_1);
		lvRow4.addElement(laColumn_4_2);
		lvRow4.addElement(laColumn_4_3);
		lvRow4.addElement(laColumn_4_4);
		lvRow4.addElement(laColumn_4_5);
		lvRow4.addElement(laColumn_4_6);

		//add labels for print row 2
		lvRow5.addElement(laColumn_5_1);
		lvRow5.addElement(laColumn_5_2);
		lvRow5.addElement(laColumn_5_3);
		lvRow5.addElement(laColumn_5_4);
		lvRow5.addElement(laColumn_5_5);
		lvRow5.addElement(laColumn_5_6);
		lvRow5.addElement(laColumn_5_7);
		lvRow5.addElement(laColumn_5_8);
		lvRow5.addElement(laColumn_5_9);

		//add all rows to the column table
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRow2);
		lvTable.addElement(lvRow3);
		lvTable.addElement(lvRow4);
		lvTable.addElement(lvRow5);

		return lvTable;
	}

	/**
	 * determine the report type: either a current balance or history report
	 * 
	 * @param asInvItmNo String
	 * @param asInvId String
	 * @return String
	 */
	private String generateIdText(String asInvItmNo, String asInvId)
	{
		//define local variables
		String lsInqByText = CommonConstant.STR_SPACE_EMPTY;
		String InvIdName = CommonConstant.STR_SPACE_EMPTY;

		if (caInvInqUIData.getInvInqBy().equals(TXT_DEALER))
		{
			InvIdName =
				getCacheDlrIdName(
					caInvInqUIData.getOfcIssuanceNo(),
					caInvInqUIData.getSubstaId(),
					Integer.parseInt(asInvId));
		}

		if (caInvInqUIData.getInvInqBy().equals(TXT_SUBCONTRACTOR))
		{
			InvIdName =
				getCacheSubconIdName(
					caInvInqUIData.getOfcIssuanceNo(),
					caInvInqUIData.getSubstaId(),
					Integer.parseInt(asInvId));
		}

		if (caInvInqUIData.getInvInqBy().equals(TXT_EMPLOYEE))
		{
			InvIdName =
				getCacheEmployeeName(
					caInvInqUIData.getOfcIssuanceNo(),
					caInvInqUIData.getSubstaId(),
					asInvId);
		}

		// If Dealer, Employee, Subcontracter set id text in the column
		// header
		if ((caInvInqUIData.getInvInqBy().equals(TXT_DEALER))
			|| (caInvInqUIData.getInvInqBy().equals(TXT_EMPLOYEE))
			|| (caInvInqUIData.getInvInqBy().equals(TXT_SUBCONTRACTOR)))
		{
			lsInqByText =
				CommonConstant.STR_SPACE_EMPTY
					+ CommonConstant.STR_OPEN_PARENTHESES
					+ InvIdName
					+ CommonConstant.STR_CLOSE_PARENTHESES;
		} // end if
		else
			// If Spacific Item Type and Year, set id text in the column
			// header
			if (caInvInqUIData
				.getInvInqBy()
				.equals(TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR))
			{
				lsInqByText = asInvItmNo;
			} // end else

		return lsInqByText;
	}

	/**
	 * Generate Spec Item Year No
	 * 
	 * @param asInvItmNo String
	 * @return Vector
	 */
	public Vector generateSpecItmYrNo(String asInvItmNo)
	{
		//local variables
		String lsInvItmCd = caInvInqUIData.getItmCd();
		String lsInqByText;
		String lsItmCdText;
		int liCharStart_Center;

		//create containers
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();

		//populate the column vector
		//Parm: length of longest column header, string to be printed

		//populate column label for column report header line

		// If inquiry is spacific item and year
		lsInqByText =
			HDR_ITEM
				+ CommonConstant.STR_SPACE_ONE
				+ HDR_NUMBER
				+ CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ CommonConstant.STR_SPACE_ONE
				+ asInvItmNo;
		lsItmCdText =
			HDR_ITEM
				+ CommonConstant.STR_SPACE_ONE
				+ HDR_CODE
				+ CommonConstant.STR_COLON
				+ CommonConstant.STR_SPACE_ONE
				+ CommonConstant.STR_SPACE_ONE
				+ lsInvItmCd;

		// Build the column header for specific Number and Year,
		// No Record Found	
		liCharStart_Center = center(DTL_LENGTH_132, lsInqByText);
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				lsInqByText,
				liCharStart_Center,
				charLength(lsInqByText));

		liCharStart_Center = center(DTL_LENGTH_132, lsItmCdText);
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				lsItmCdText,
				liCharStart_Center,
				charLength(lsItmCdText));

		//add labels for report type line
		lvRow1.addElement(laColumn_1_1);
		lvRow2.addElement(laColumn_2_1);

		//add all rows to the column table
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRow2);

		return lvTable;
	}

	/**
	 * Get dealer id from the Dealer cache table.
	 * 
	 * @param aiOfcssuanceNo int
	 * @param aiWsId int
	 * @param aiDlrId int
	 * @return String
	 */
	public static String getCacheDlrIdName(
		int aiOfcssuanceNo,
		int aiWsId,
		int aiDlrId)
	{
		try
		{
			Vector lvDlrData = new Vector();
			DatabaseAccess laDBA = new DatabaseAccess();
			try
			{
				laDBA.beginTransaction();
				Dealer laDbDlr = new Dealer(laDBA);
				DealerData laDlrData = new DealerData();

				laDlrData.setOfcIssuanceNo(aiOfcssuanceNo);
				laDlrData.setSubstaId(aiWsId);
				
				// defect 10112  
				laDlrData.setId(aiDlrId);
				// end defect 10112 
				
				lvDlrData = laDbDlr.qryDealer(laDlrData);
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx)
			{
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			if (lvDlrData != null)
			{
				// defect 10112  
				return ((DealerData) lvDlrData.get(0)).getName1();
				// end defect 10112 
			}
			else
			{
				return TXT_NOT_KNOWN;
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
		return TXT_NOT_KNOWN;
	}

	/**
	 * Get employee name from the security cache table.
	 * 
	 * @param aiOfcssuanceNo int
	 * @param aiSubStaId int
	 * @param asEmpId String
	 * @return String
	 */
	public String getCacheEmployeeName(
		int aiOfcssuanceNo,
		int aiSubStaId,
		String asEmpId)
	{
		try
		{
			Vector lvSecrtyData = new Vector();
			DatabaseAccess laDBA = new DatabaseAccess();
			try
			{
				laDBA.beginTransaction();
				Security laDbSecrty = new Security(laDBA);
				SecurityData laSecrtyData = new SecurityData();

				laSecrtyData.setOfcIssuanceNo(aiOfcssuanceNo);
				laSecrtyData.setSubstaId(aiSubStaId);
				laSecrtyData.setEmpId(asEmpId);
				lvSecrtyData = laDbSecrty.qrySecurity(laSecrtyData);
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx)
			{
				laDBA.endTransaction(DatabaseAccess.NONE);
				aeRTSEx.printStackTrace();
			}

			if (lvSecrtyData != null)
			{
				SecurityData laItmData =
					(SecurityData) lvSecrtyData.get(0);
				String lsName =
					laItmData.getEmpLastName()
						+ CommonConstant.STR_COMMA
						+ CommonConstant.STR_SPACE_ONE
						+ laItmData.getEmpFirstName();
				return lsName;
			} // end if
			else
				return TXT_NOT_KNOWN;
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
		return TXT_NOT_KNOWN;
	}

	/**
	 * Get Cache Plate Type Description
	 *
	 * @param asItmCd String
	 * @return String
	 */
	public static String getCachePlateTypeDesc(String asItmCd)
	{
		ItemCodesData aItmData = ItemCodesCache.getItmCd(asItmCd);
		if (aItmData != null)
		{
			return aItmData.getItmCdDesc();
		}
		else
		{
			return TXT_NOT_KNOWN;
		}
	}

	/**
	 * Get Cache Subcon Id Name
	 *
	 * @param aiOfcssuanceNo int
	 * @param aiSubId int
	 * @param aiSubconId int
	 * @return String
	 */
	public static String getCacheSubconIdName(
		int aiOfcssuanceNo,
		int aiSubId,
		int aiSubconId)
	{
		try
		{
			Vector lvSubconData = new Vector();
			DatabaseAccess laDBA = new DatabaseAccess();
			try
			{
				laDBA.beginTransaction();
				Subcontractor laDbSubcon = new Subcontractor(laDBA);
				SubcontractorData laSubconData =
					new SubcontractorData();

				laSubconData.setOfcIssuanceNo(aiOfcssuanceNo);
				laSubconData.setSubstaId(aiSubId);
				
				// defect 10161
				laSubconData.setId(aiSubconId);
				// end defect 10161 
				
				lvSubconData =
					laDbSubcon.qrySubcontractor(laSubconData);
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx)
			{
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			if (lvSubconData != null)
			{
				// defect 10161 
				return ((SubcontractorData) lvSubconData.get(0))
					.getName1();
				// end defect 10161 
			}
			else
			{
				return TXT_NOT_KNOWN;
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
		return TXT_NOT_KNOWN;
	}

	/**
	 * Get print row adjustment.
	 * 
	 * @return int
	 */
	private int getPrntRowAdj()
	{
		return ciPrntRowAdj;
	}

	/**
	 * Get Record Index
	 * 
	 * @return int
	 */
	public int getRecordIndex()
	{
		return ciRecordIndex;
	}

	/**
	 * Get Total Quantity
	 * 
	 * @return int
	 */
	public int getTotalQty()
	{
		return ciTotalQty;
	}

	/**
	 * starts component application to produce Inventory Inquiry Report
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		//create new graph of report class
		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3031_INVENTORY_INQUIRY_REPORT_ID);
		GenInventoryInquiryReport laGPR =
			new GenInventoryInquiryReport(
				ReportConstant.RPT_3031_INVENTORY_INQUIRY_REPORT_TITLE,
				laRptProps);

		//extract dummy data for display
		String lsQuery = SAMPLE_QUERY;
		Vector lvQueryResults = laGPR.queryData(lsQuery);
		laGPR.formatReport(lvQueryResults);

		//write completed report to local hard drive
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(SAMPLE_FILE_NAME);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGPR.caRpt.getReport().toString());
		laPout.close();

		//display report in preview window
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport = new FrmPreviewReport(SAMPLE_FILE_NAME);
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
	}

	/**
	 * printCurrBalDataline() prints body text of Inventory
	 *  Allocate Report
	 */
	private void printCurrBalDataline()
	{
		//Set Inventory Status Code to "HELD" if not 0.
		String lsInvStatusCd = CommonConstant.STR_SPACE_EMPTY;
		switch (caInvInqCurrBal.getInvStatusCd())
		{
			case 0 :
				{
					lsInvStatusCd = CommonConstant.STR_SPACE_EMPTY;
					break;
				}
			case 1 :
			{
				lsInvStatusCd = TXT_STATUS_U;
				break;
			}
			case 2 :
			{
				lsInvStatusCd = TXT_STATUS_S;
				break;
			}
			case 6 :
			{
				lsInvStatusCd = TXT_STATUS_C;
				break;
			}
			default :
			{
				lsInvStatusCd = TXT_HELD;
			}
		} //end switch
		//Set Inventory Id to blank if asInvLocIdCd is CENTRAL (C) or STOCK (A).
		String lsInvId = CommonConstant.STR_SPACE_EMPTY;
		if ((caInvInqCurrBal
			.getInvLocIdCd()
			.equals(InventoryConstant.CHAR_CENTRAL)
			|| caInvInqCurrBal.getInvLocIdCd().equals(
				InventoryConstant.CHAR_STOCK))
			&& (caInvInqCurrBal
				.getInvId()
				.equals(CommonConstant.STR_ZERO)))
		{
			lsInvId = CommonConstant.STR_SPACE_EMPTY;
		}
		else
		{
			lsInvId = caInvInqCurrBal.getInvId();
		}
		// **********  PRINT DATA LINE  **********
		//item code
		//description	
		this.caRpt.print(
			caInvInqCurrBal.getItmCdDesc(),
			HDR_CUR_STARTPT_2_1,
			charLength(caInvInqCurrBal.getItmCdDesc()));

		//year
		String lsInvItmYr = CommonConstant.STR_SPACE_EMPTY;
		if (caInvInqCurrBal.getInvItmYr() != 0)
		{
			lsInvItmYr = String.valueOf(caInvInqCurrBal.getInvItmYr());
			this.caRpt.print(
				lsInvItmYr,
				HDR_CUR_STARTPT_2_2,
				charLength(lsInvItmYr));
		} // end if

		//begin number		
		this.caRpt.rightAlign(
			caInvInqCurrBal.getInvItmNo(),
			HDR_CUR_STARTPT_1_3,
			ITEMNOLENGTH);

		//end number	
		this.caRpt.rightAlign(
			caInvInqCurrBal.getInvItmEndNo(),
			HDR_CUR_STARTPT_1_4,
			ITEMNOLENGTH);

		//quantity
		String lsQty = String.valueOf(caInvInqCurrBal.getInvQty());
		this.caRpt.rightAlign(lsQty, DTL_STARTPT_76, DTL_STARTPT_08);

		//hold status	
		this.caRpt.print(
			lsInvStatusCd,
			HDR_CUR_STARTPT_2_6,
			charLength(lsInvStatusCd));

		//location entity code, location	
		this.caRpt.print(
			caInvInqCurrBal.getInvLocIdCd(),
			HDR_CUR_STARTPT_2_7,
			charLength(caInvInqCurrBal.getInvLocIdCd()));

		//entity id	
		this.caRpt.print(
			lsInvId,
			HDR_CUR_STARTPT__2_8,
			charLength(lsInvId));
		this.caRpt.blankLines(1);
	}

	/**
	 * Print a data line of the exception report.
	 * 
	 * @param avExcptData Vector
	 */
	public void printExceptionDataLine(Vector avExcptData)
	{
		// item desc
		String lsItmCdDesc =
			(String) avExcptData.elementAt(CommonConstant.ELEMENT_1);
		// Item year
		String lsItmCdYr =
			(String) avExcptData.elementAt(CommonConstant.ELEMENT_2);

		//print detail line
		// description
		this.caRpt.print(
			lsItmCdDesc,
			DTL_STARTPT_04,
			charLength(lsItmCdDesc));
		// year
		this.caRpt.print(
			lsItmCdYr,
			DTL_STARTPT_37,
			charLength(lsItmCdYr));
	}

	/**
	 * printHistDataline() prints body text
	 *  of Inventory Allocate Report
	 */
	private void printHistDataline()
	{
		// Set integer format
		DecimalFormat laThreeDigits =
			new DecimalFormat(DECIMAL_FORMAT_3DIGITS);
		DecimalFormat laSixDigits =
			new DecimalFormat(DECIMAL_FORMAT_6DIGITS);

		//Set TransCd = VOID if Delete reason code = 5
		if (caInvInqHistBal.getDelInvReasnCd() == DELETE_CODE_VOID)
		{
			caInvInqHistBal.setTransCd(TXT_VOID);
		}

		//print detail line
		//description	
		this.caRpt.print(
			caInvInqHistBal.getItmCdDesc(),
			HDR_HIS_STARTPT_2_1,
			charLength(caInvInqHistBal.getItmCdDesc()));

		//year
		String lsInvItmYr = CommonConstant.STR_SPACE_EMPTY;
		if (caInvInqHistBal.getInvItmYr() != 0)
		{
			lsInvItmYr = String.valueOf(caInvInqHistBal.getInvItmYr());
			this.caRpt.print(
				lsInvItmYr,
				HDR_HIS_STARTPT_2_2,
				charLength(lsInvItmYr));
		}

		//begin number		
		this.caRpt.rightAlign(
			caInvInqHistBal.getInvItmNo(),
			HDR_HIS_STARTPT_2_3,
			ITEMNOLENGTH);

		//end number	
		this.caRpt.rightAlign(
			caInvInqHistBal.getInvEndNo(),
			HDR_HIS_STARTPT_2_4,
			ITEMNOLENGTH);

		//quantity
		String lsQty = String.valueOf(caInvInqHistBal.getInvQty());
		this.caRpt.rightAlign(
			lsQty,
			HDR_HIS_STARTPT_2_5,
			DTL_STARTPT_08);

		//transId
		String lsTransId =
			laThreeDigits.format(caInvInqHistBal.getOfcIssuanceNo())
				+ laThreeDigits.format(caInvInqHistBal.getTransWsId())
				+ String.valueOf(caInvInqHistBal.getTransAMDate())
				+ laSixDigits.format(caInvInqHistBal.getTransTime());
		this.caRpt.print(
			lsTransId,
			HDR_HIS_STARTPT_2_6,
			charLength(lsTransId));

		//TransId	
		this.caRpt.print(
			caInvInqHistBal.getTransCd(),
			HDR_HIS_STARTPT_2_7,
			charLength(caInvInqHistBal.getTransCd()));

		//InvLocIdCd	
		this.caRpt.print(
			caInvInqHistBal.getInvLocIdCd(),
			HDR_HIS_STARTPT_2_8,
			charLength(caInvInqHistBal.getInvLocIdCd()));

		//InvId
		this.caRpt.print(
			caInvInqHistBal.getInvId(),
			HDR_HIS_STARTPT_2_9,
			charLength(caInvInqHistBal.getInvId()));
		this.caRpt.blankLines(1);
	}

	/**
	 * Print dash line and total item count.
	 * 
	 * @param asRecType String
	 * @return boolean
	 */
	private boolean printTotalQty(String asRecType)
	{
		boolean lbRtn = false;

		String lsTotalQty = String.valueOf(getTotalQty());
		String lsDashes = this.caRpt.singleDashes(NUMBER_DASHES_TOTAL);
		// *************************************************************    
		//print total quantity line for History 
		if (asRecType.equals(TXT_HISTORY))
		{
			this.caRpt.rightAlign(
				lsDashes,
				DTL_STARTPT_76,
				charLength(lsDashes));
			this.caRpt.blankLines(1);
			this.caRpt.print(
				TXT_TOTAL_QUANTITY,
				DTL_STARTPT_54,
				charLength(TXT_TOTAL_QUANTITY));
			this.caRpt.rightAlign(
				lsTotalQty,
				DTL_STARTPT_76,
				charLength(lsDashes));
		}
		else
		{
			//print total quantity line for Current Balance
			this.caRpt.rightAlign(
				lsDashes,
				DTL_STARTPT_74,
				charLength(lsDashes));
			this.caRpt.blankLines(1);
			// defect 9254
			if (caInvInqUIData.getRptType().equals
				(InventoryConstant.CUR_VIRTUAL))
			{
				this.caRpt.print(
				TXT_AVAILABLE_QUANTITY,
				DTL_STARTPT_50,
				charLength(TXT_AVAILABLE_QUANTITY));
				this.caRpt.rightAlign(
				lsTotalQty,
				DTL_STARTPT_74,
				charLength(lsDashes));
			}
			else
			{
				this.caRpt.print(
				TXT_TOTAL_QUANTITY,
				DTL_STARTPT_54,
				charLength(TXT_TOTAL_QUANTITY));
				this.caRpt.rightAlign(
				lsTotalQty,
				DTL_STARTPT_74,
				charLength(lsDashes));
			}
			// end defect 9254
		} 
		// *************************************************************   

		int liPrntLinesAvail =
			getNoOfDetailLines() - RESERVE_FOOTER_LINES;

		// Set number of blank lines based on body lines remaining
		if (liPrntLinesAvail <= RESERVE_FOOTER_LINES)
		{
			this.caRpt.blankLines(1);
			setPrntRowAdj(0);
		}
		else
		{
			this.caRpt.blankLines(2);
			setPrntRowAdj(RESERVE_FOOTER_LINES);
		}

		// Determine if print lines are available.  Set page break.
		// Leave 5 blank lines if next item prints 1 data line them total
		if (liPrntLinesAvail < WHITE_SPACE)
		{
			lbRtn = true;
		}

		return lbRtn;
	}

	/**
	 * Process the Current Balance report..
	 * 
	 * @return boolean: true if more records to process
	 *				    false if no more records
	 */
	public boolean processCurrentBalanceRpt()
	{
		boolean lbIdPageBreak = true;
		boolean lbMoreRecords = true;

		ciPrintLinesAvailable =
			this.caRptProps.getPageHeight() - RESERVE_FOOTER_LINES;

		// Print total quanity for item    
		if (!(csPrevItmCd.equals(caInvInqCurrBal.getItmCd())
			&& ciPrevInvItmYr == caInvInqCurrBal.getInvItmYr()))
		{
			printCurrBalDataline();
			saveCurrentItems(
				caInvInqCurrBal.getInvId(),
				caInvInqCurrBal.getItmCd(),
				TXT_CURRENT_BALANCE,
				caInvInqCurrBal.getInvItmYr());
			setTotalQty(getTotalQty() + caInvInqCurrBal.getInvQty());
			setRecordIndex(getRecordIndex() + 1);
			caInvInqCurrBal =
				(InventoryInquiryReportData) cvCurrBal.elementAt(
					getRecordIndex());
		}

		for (int k = this.caRpt.getCurrX();
			k < ciPrintLinesAvailable;
			k++)
		{ // Continue printing the records if space is available

			// Test for last record
			if (getRecordIndex() < cvCurrBal.size() - 1)
			{
				lbIdPageBreak =
					processTotals(
						caInvInqCurrBal.getInvId(),
						caInvInqCurrBal.getItmCd(),
						caInvInqCurrBal.getInvItmYr(),
						TXT_CURRENT_BALANCE);

				if (!(lbIdPageBreak))
				{ 
					// If there is no page break, print a data line
					printCurrBalDataline();
					saveCurrentItems(
						caInvInqCurrBal.getInvId(),
						caInvInqCurrBal.getItmCd(),
						TXT_CURRENT_BALANCE,
						caInvInqCurrBal.getInvItmYr());
					setTotalQty(
						getTotalQty() + caInvInqCurrBal.getInvQty());
					setRecordIndex(getRecordIndex() + 1);
					caInvInqCurrBal =
						(
							InventoryInquiryReportData) cvCurrBal
								.elementAt(
							getRecordIndex());
					k = adjPrntLinesAvail(k);
				} // end if
				else // Page break for ID change
				{ 
					// Save the current data, More Records = true,
					// continue to the next page
					k = ciPrintLinesAvailable;
					// end for loop, print data lines
					saveCurrentItems(
						caInvInqCurrBal.getInvId(),
						caInvInqCurrBal.getItmCd(),
						TXT_CURRENT_BALANCE,
						caInvInqCurrBal.getInvItmYr());
				} // end else
			} // end if not last record or not Exception Report data
			else
			{ 
				// Set More Recored to false
				k = ciPrintLinesAvailable;
				lbMoreRecords = false;
			} // end else
		} //end for loop: more print data lines avaliable

		// Print total quanity for item, exclude last record    
		if (!(csPrevItmCd.equals(caInvInqCurrBal.getItmCd())
			&& ciPrevInvItmYr == caInvInqCurrBal.getInvItmYr())
			&& (getRecordIndex() < cvCurrBal.size() - 1))
		{
			processTotals(
				caInvInqCurrBal.getInvId(),
				caInvInqCurrBal.getItmCd(),
				caInvInqCurrBal.getInvItmYr(),
				TXT_CURRENT_BALANCE);
		}
		return lbMoreRecords;
	}

	/**
	 * Call methods to build the exception vectors, print data.
	 */
	private void processExceptionReport()
	{
		//get number of printlines available
		int ciPrintLinesAvailable = getNoOfDetailLines();
		int liMaxPrntLines = 1;

		Vector lvHoldData = new Vector();

		// Test to print footer
		if (cvCurrBal.size() > 0 || cvHistBal.size() > 0)
		{
			generateFooter();
		} // end if

		// Print Current Balance Exception Recport
		if (cvExcCurr.size() > 0)
		{
			generateColumnHeadings(
				TXT_CURRENT_BALANCE,
				TXT_EXCEPTION_REPORT,
				false);
			ciPrintLinesAvailable = getNoOfDetailLines();
			for (int i = 0; i < cvExcCurr.size(); i++)
			{
				if (liMaxPrntLines >= ciPrintLinesAvailable)
				{
					generateFooter();
					generateColumnHeadings(
						TXT_CURRENT_BALANCE,
						TXT_EXCEPTION_REPORT,
						false);
					ciPrintLinesAvailable = getNoOfDetailLines();
					liMaxPrntLines = 0;
				} // end if
				lvHoldData = (Vector) cvExcCurr.elementAt(i);
				printExceptionDataLine(lvHoldData);
				this.caRpt.blankLines(1);
				liMaxPrntLines = liMaxPrntLines + 1;
			} // end for loop
		} // end if

		//  Print the History Exception Report
		if (cvExcHist.size() > 0)
		{
			// Test to print footer
			if (cvExcCurr.size() > 0)
			{
				generateFooter();
			} // end if

			generateColumnHeadings(
				TXT_HISTORY,
				TXT_EXCEPTION_REPORT,
				false);
			ciPrintLinesAvailable = getNoOfDetailLines();
			liMaxPrntLines = 0;
			for (int i = 0; i < cvExcHist.size(); i++)
			{
				if (liMaxPrntLines >= ciPrintLinesAvailable)
				{
					generateFooter();
					generateColumnHeadings(
						TXT_HISTORY,
						TXT_EXCEPTION_REPORT,
						false);
					ciPrintLinesAvailable = getNoOfDetailLines();
					liMaxPrntLines = 0;
				} // end if
				lvHoldData = (Vector) cvExcHist.elementAt(i);
				printExceptionDataLine(lvHoldData);
				this.caRpt.blankLines(1);
				liMaxPrntLines++;
			} // end for loop
		} // end if
	}

	/**
	 * Print one page of history data..
	 * 
	 * @return boolean: true if more records to process
	 *				    false if no more records
	 */
	public boolean processHistoryRpt()
	{
		boolean lbIdPageBreak = true;
		boolean lbMoreRecords = true;

		ciPrintLinesAvailable =
			this.caRptProps.getPageHeight() - RESERVE_FOOTER_LINES;
		if (getRecordIndex() < cvHistBal.size() - 1)
		{
			// Print total quanity for item    
			if (!(csPrevItmCd.equals(caInvInqCurrBal.getItmCd())
				&& ciPrevInvItmYr == caInvInqCurrBal.getInvItmYr()))
			{
				printHistDataline();
				saveCurrentItems(
					caInvInqHistBal.getInvId(),
					caInvInqHistBal.getItmCd(),
					TXT_HISTORY,
					caInvInqHistBal.getInvItmYr());
				setTotalQty(
					getTotalQty() + caInvInqHistBal.getInvQty());
				setRecordIndex(getRecordIndex() + 1);

				// only get the next record if there is more data to process
				// The loop below will take care of the rest.
				if (getRecordIndex() < cvHistBal.size())
				{
					caInvInqHistBal =
						(
							TransactionInventoryDetailData) cvHistBal
								.elementAt(
							getRecordIndex());
				}
			}
		}
		// start k from the current line.
		// the -3 was already here on max k.?
		for (int k = this.caRpt.getCurrX();
			k < ciPrintLinesAvailable - RESERVE_FOOTER_LINES;
			k++)
		{ // Continue printing the records if space is available

			if (getRecordIndex() < cvHistBal.size() - 1)
			{
				// If this is NOT the last record 
				lbIdPageBreak =
					processTotals(
						caInvInqHistBal.getInvId(),
						caInvInqHistBal.getItmCd(),
						caInvInqHistBal.getInvItmYr(),
						TXT_HISTORY);
				if (!(lbIdPageBreak))
				{ 
					// If there is no page break, print a data line
					printHistDataline();
					saveCurrentItems(
						caInvInqHistBal.getInvId(),
						caInvInqHistBal.getItmCd(),
						TXT_HISTORY,
						caInvInqHistBal.getInvItmYr());
					setTotalQty(
						getTotalQty() + caInvInqHistBal.getInvQty());
					setRecordIndex(getRecordIndex() + 1);
					caInvInqHistBal =
						(
							TransactionInventoryDetailData) cvHistBal
								.elementAt(
							getRecordIndex());
					k = adjPrntLinesAvail(k);
				} // end if
				else
				{ 
					// Break out of the for loop, save the current data
					k = ciPrintLinesAvailable;
					// end for loop, print data lines
					saveCurrentItems(
						caInvInqHistBal.getInvId(),
						caInvInqHistBal.getItmCd(),
						TXT_HISTORY,
						caInvInqHistBal.getInvItmYr());
				} // end else
			} // end if not last record or not Exception Report data
			else
			{ 
				// Set More Recored to false
				k = ciPrintLinesAvailable;
				lbMoreRecords = false;
			} // end else
		} //end for loop: more print data lines avaliable

		if ((!lbIdPageBreak)
			&& !csPrevItmCd.equals(caInvInqCurrBal.getItmCd())
			&& ciPrevInvItmYr != caInvInqCurrBal.getInvItmYr())
		{
			processTotals(
				caInvInqHistBal.getInvId(),
				caInvInqHistBal.getItmCd(),
				caInvInqHistBal.getInvItmYr(),
				TXT_HISTORY);
			
			saveCurrentItems(
				caInvInqHistBal.getInvId(),
				caInvInqHistBal.getItmCd(),
				TXT_HISTORY,
				caInvInqHistBal.getInvItmYr());
			//end defect5057
		}
		return lbMoreRecords;
	}

	/**
	 * Print the last record.
	 * 
	 * @param asInvId String
	 * @param asItmCd String
	 * @param aiInvItmYr int
	 * @param asRecType String
	 */
	protected void processLastRecord(
		String asInvId,
		String asItmCd,
		int aiInvItmYr,
		String asRecType)
	{

		boolean lbLdPageBreak = false;

		// Set page break = true if ID changed
		// processTotals will print total if ID or ItmCd and ItmYr changed
		lbLdPageBreak =
			processTotals(asInvId, asItmCd, aiInvItmYr, asRecType);

		// If page break for Id is true, print footer/header
		if ((lbLdPageBreak))
		{
			generateFooter();
			if (asRecType.equals(TXT_CURRENT_BALANCE))
			{
				generateColumnHeadings(
					caInvInqCurrBal.getInvItmNo(),
					asRecType,
					false);
			} // end if
			else
			{
				generateColumnHeadings(
					caInvInqHistBal.getInvItmNo(),
					asRecType,
					false);
			} // end else
		} // end if

		// Print the record
		if (asRecType.equals(TXT_CURRENT_BALANCE))
		{
			printCurrBalDataline();
			setTotalQty(getTotalQty() + caInvInqCurrBal.getInvQty());
		} // end if
		else
		{
			printHistDataline();
			setTotalQty(getTotalQty() + caInvInqHistBal.getInvQty());
		} // end else

		// Print total quanity
		printTotalQty(asRecType);
	}

	/**
	 * Determine if to print total quanitities and set Id page break
	 * 
	 * @param asCurrInvId String
	 * @param asCurrItmCd String
	 * @param aiInvItmYr int
	 * @param asRecType String
	 */
	private boolean processTotals(
		String asCurrInvId,
		String asCurrItmCd,
		int aiInvItmYr,
		String asRecType)
	{
		boolean lbIdBreak = true;

		while (!(caInvInqUIData
			.getInvInqBy()
			.equals(TXT_SPECIFIC_ITEM_NUMBER_AND_YEAR)))
		{
			lbIdBreak = false;

			// test if Inventory Id has changed
			if ((caInvInqUIData.getInvInqBy().equals(TXT_EMPLOYEE))
				|| (caInvInqUIData.getInvInqBy().equals(TXT_DEALER))
				|| (caInvInqUIData
					.getInvInqBy()
					.equals(TXT_SUBCONTRACTOR))
				|| (caInvInqUIData.getInvInqBy().equals(TXT_WORKSTATION)))
			{
				if (!(csPrevInvId.equals(asCurrInvId)))
				{
					printTotalQty(asRecType);
					setTotalQty(0);
					lbIdBreak = true;
				} // end if
			} // end else/if 

			// test if Item Code and Year has changed
			if (!(csPrevItmCd.equals(asCurrItmCd)
				&& ciPrevInvItmYr == aiInvItmYr)
				&& lbIdBreak == false)
			{
				lbIdBreak = printTotalQty(asRecType);
				setTotalQty(0);
			} // end if

			break;
		} // end while

		return lbIdBreak;
	}

	/**
	 * queryData() mimics data from db.
	 * 
	 * @param asQuery String
	 * @return Vector - Records
	 */
	public Vector queryData(String asQuery)
	{
		// leave these values for another time.
		
		//create container
		Vector avResults = new Vector();

		// Set RTS Date
		int liRTSDateType = 1;
		int liBeginDT = 20011010;
		int liEndDT = 20011020;
		RTSDate laBeginDt = new RTSDate(liRTSDateType, liBeginDT);
		RTSDate laEndDt = new RTSDate(liRTSDateType, liEndDT);

		//CENTRAL-ITEM TYPES(S) $ YEAR/ DATA
		// Build record 1a
		InventoryInquiryUIData laDataline1a =
			new InventoryInquiryUIData();
		laDataline1a.setRptType(TXT_CURRENT_BALANCE);
		laDataline1a.setItmCd("DPP");
		laDataline1a.setItmCdDesc("DISABLE PERSON PLT");
		laDataline1a.setInvItmYr(0);
		laDataline1a.setInvItmNo("1PJMW");
		laDataline1a.setInvItmEndNo("9PJNF");
		laDataline1a.setBeginDt(laBeginDt);
		laDataline1a.setEndDt(laEndDt);
		laDataline1a.setInvQty(72);
		laDataline1a.setInvStatusCd(0);
		laDataline1a.setInvLocIdCd("C");
		laDataline1a.setInvId("");
		laDataline1a.setInvIdName("");
		laDataline1a.setInvInqBy("5");
		laDataline1a.setExceptionReport(0);
		for (int i = 0; i < 3; i++)
		{
			avResults.addElement(laDataline1a);
		} // end for loop

		// Build record 1b
		InventoryInquiryUIData laDataline1b =
			new InventoryInquiryUIData();
		laDataline1b.setRptType(TXT_CURRENT_BALANCE);
		laDataline1b.setItmCd("FP");
		laDataline1b.setItmCdDesc("FERTILIZER PLT");
		laDataline1b.setInvItmYr(1999);
		laDataline1b.setInvItmNo("15B177");
		laDataline1b.setInvItmEndNo("15B182");
		laDataline1b.setBeginDt(laBeginDt);
		laDataline1b.setEndDt(laEndDt);
		laDataline1b.setInvQty(5);
		laDataline1b.setInvStatusCd(1);
		laDataline1b.setInvLocIdCd("C");
		laDataline1b.setInvId("");
		laDataline1b.setInvIdName("");
		laDataline1b.setInvInqBy("5");
		laDataline1b.setExceptionReport(0);
		for (int i = 0; i < 3; i++)
		{
			avResults.addElement(laDataline1b);
		} // end for loop

		//ITEM TYPES(S) $ YEAR/WORKSTATION
		// Build record 1c
		InventoryInquiryUIData laDataline1c =
			new InventoryInquiryUIData();
		laDataline1c.setRptType(TXT_CURRENT_BALANCE);
		laDataline1c.setItmCd("CLSTKR");
		laDataline1c.setItmCdDesc("CLASSIC WINDOW STKR");
		laDataline1c.setInvItmYr(2000);
		laDataline1c.setInvItmNo("5C");
		laDataline1c.setInvItmEndNo("9C");
		laDataline1c.setBeginDt(laBeginDt);
		laDataline1c.setEndDt(laEndDt);
		laDataline1c.setInvQty(5);
		laDataline1c.setInvStatusCd(0);
		laDataline1c.setInvLocIdCd("W");
		laDataline1c.setInvId("100");
		laDataline1c.setInvIdName("");
		laDataline1c.setInvInqBy("5");
		laDataline1c.setExceptionReport(0);
		for (int i = 0; i < 10; i++)
		{
			avResults.addElement(laDataline1c);
		} // end for loop

		//ITEM TYPES(S) $ YEAR/WORKSTATION
		// Build record 1c
		InventoryInquiryUIData laDataline1d =
			new InventoryInquiryUIData();
		laDataline1d.setRptType(TXT_CURRENT_BALANCE);
		laDataline1d.setItmCd("TKP");
		laDataline1d.setItmCdDesc("TRUCK PLT");
		laDataline1d.setInvItmYr(0);
		laDataline1d.setInvItmNo("5CLK20");
		laDataline1d.setInvItmEndNo("5CLK25");
		laDataline1d.setBeginDt(laBeginDt);
		laDataline1d.setEndDt(laEndDt);
		laDataline1d.setInvQty(6);
		laDataline1d.setInvStatusCd(0);
		laDataline1d.setInvLocIdCd("W");
		laDataline1d.setInvId("100");
		laDataline1d.setInvIdName("");
		laDataline1d.setInvInqBy("5");
		laDataline1d.setExceptionReport(0);
		for (int i = 0; i < 10; i++)
		{
			avResults.addElement(laDataline1d);
		} // end for loop

		//DEALER DATA
		// Build record 1
		InventoryInquiryUIData laDataline1e =
			new InventoryInquiryUIData();
		laDataline1e.setRptType(TXT_CURRENT_BALANCE);
		laDataline1e.setItmCd("FORM31");
		laDataline1e.setItmCdDesc("FORM 31-RTS");
		laDataline1e.setInvItmYr(0);
		laDataline1e.setInvItmNo("P592288");
		laDataline1e.setInvItmEndNo("P592297");
		laDataline1e.setBeginDt(laBeginDt);
		laDataline1e.setEndDt(laEndDt);
		laDataline1e.setInvQty(10);
		laDataline1e.setInvStatusCd(1);
		laDataline1e.setInvLocIdCd("D");
		laDataline1e.setInvId("002");
		laDataline1e.setInvIdName("AGC AUTOMOTIVE");
		laDataline1e.setInvInqBy("5");
		laDataline1e.setExceptionReport(0);
		for (int i = 0; i < 25; i++)
		{
			avResults.addElement(laDataline1e);
		} // end for loop

		// Build record 1b
		InventoryInquiryUIData laDataline1f =
			new InventoryInquiryUIData();
		laDataline1f.setRptType(TXT_CURRENT_BALANCE);
		laDataline1f.setItmCd("WS");
		laDataline1f.setItmCdDesc("WINDSHIELD STICKER");
		laDataline1f.setInvItmYr(2002);
		laDataline1f.setInvItmNo("22334454WC");
		laDataline1f.setInvItmEndNo("22334463");
		laDataline1f.setBeginDt(laBeginDt);
		laDataline1f.setEndDt(laEndDt);
		laDataline1f.setInvQty(20);
		laDataline1f.setInvStatusCd(1);
		laDataline1f.setInvLocIdCd("D");
		laDataline1f.setInvId("002");
		laDataline1f.setInvIdName("AGC AUTOMOTIVE");
		laDataline1f.setInvInqBy("5");
		laDataline1f.setExceptionReport(0);
		for (int i = 0; i < 5; i++)
		{
			avResults.addElement(laDataline1f);
		} // end for loop

		// Build record 2a
		InventoryInquiryUIData laDataline2a =
			new InventoryInquiryUIData();
		laDataline2a.setRptType(TXT_CURRENT_BALANCE);
		laDataline2a.setItmCd("30MCPT");
		laDataline2a.setItmCdDesc("30 DAY MOTORCYCLE PERMIT");
		laDataline2a.setInvItmYr(0);
		laDataline2a.setInvItmNo("06767M3");
		laDataline2a.setInvItmEndNo("06866M3");
		laDataline2a.setBeginDt(laBeginDt);
		laDataline2a.setEndDt(laEndDt);
		laDataline2a.setInvQty(100);
		laDataline2a.setInvStatusCd(0);
		laDataline2a.setInvLocIdCd("D");
		laDataline2a.setInvId("002");
		laDataline2a.setInvIdName("AGC AUTOMOTIVE");
		laDataline2a.setInvInqBy("5");
		laDataline2a.setExceptionReport(0);
		for (int i = 0; i < 1; i++)
		{
			avResults.addElement(laDataline2a);
		} // end for loop

		// Build record 2b
		InventoryInquiryUIData laDataline2b =
			new InventoryInquiryUIData();
		laDataline2b.setRptType(TXT_CURRENT_BALANCE);
		laDataline2b.setItmCd("30MCPT");
		laDataline2b.setItmCdDesc("30 DAY MOTORCYCLE PERMIT");
		laDataline2b.setInvItmYr(0);
		laDataline2b.setInvItmNo("06467M3");
		laDataline2b.setInvItmEndNo("06566M3");
		laDataline2b.setBeginDt(laBeginDt);
		laDataline2b.setEndDt(laEndDt);
		laDataline2b.setInvQty(100);
		laDataline2b.setInvStatusCd(0);
		laDataline2b.setInvLocIdCd("D");
		laDataline2b.setInvId("003");
		laDataline2b.setInvIdName("HED AUSTIN SW");
		laDataline2b.setInvInqBy("5");
		laDataline2b.setExceptionReport(0);
		for (int i = 0; i < 5; i++)
		{
			avResults.addElement(laDataline2b);
		} // end for loop

		// Build record 3
		InventoryInquiryUIData laDataline3 =
			new InventoryInquiryUIData();
		laDataline3.setRptType(TXT_HISTORY);
		laDataline3.setItmCd("WS");
		laDataline3.setItmCdDesc("WINDSHIELD STICKER");
		laDataline3.setInvItmYr(2002);
		laDataline3.setInvItmNo("23455454WC");
		laDataline3.setInvItmEndNo("23455463WC");
		laDataline3.setBeginDt(laBeginDt);
		laDataline3.setEndDt(laEndDt);
		laDataline3.setInvQty(10);
		laDataline3.setInvStatusCd(0);
		laDataline3.setInvLocIdCd("D");
		laDataline3.setInvId("003");
		laDataline3.setInvIdName("HED AUSTIN SW");
		laDataline3.setInvInqBy("5");
		laDataline3.setExceptionReport(0);
		for (int i = 0; i < 52; i++)
		{
			avResults.addElement(laDataline3);
		} // end for loop

		//EMPLOYEE DATA
		// Build record 1
		InventoryInquiryUIData laDataline3a =
			new InventoryInquiryUIData();
		laDataline3a.setRptType(TXT_CURRENT_BALANCE);
		laDataline3a.setItmCd("FORM31");
		laDataline3a.setItmCdDesc("FORM 31-RTS");
		laDataline3a.setInvItmYr(0);
		laDataline3a.setInvItmNo("P592288");
		laDataline3a.setInvItmEndNo("P592297");
		laDataline3a.setBeginDt(laBeginDt);
		laDataline3a.setEndDt(laEndDt);
		laDataline3a.setInvQty(10);
		laDataline3a.setInvStatusCd(1);
		laDataline3a.setInvLocIdCd("E");
		laDataline3a.setInvId("RTSUSER");
		laDataline3a.setInvIdName("USER, RTS");
		laDataline3a.setInvInqBy("5");
		laDataline3a.setExceptionReport(0);
		for (int i = 0; i < 5; i++)
		{
			avResults.addElement(laDataline3a);
		} // end for loop

		//EMPLOYEE DATA
		// Build record 2
		InventoryInquiryUIData laDataline3b =
			new InventoryInquiryUIData();
		laDataline3b.setRptType(TXT_CURRENT_BALANCE);
		laDataline3b.setItmCd("WS");
		laDataline3b.setItmCdDesc("WINDOWSHIELD STICKER");
		laDataline3b.setInvItmYr(1999);
		laDataline3b.setInvItmNo("2460851WZ");
		laDataline3b.setInvItmEndNo("2460900WZ");
		laDataline3b.setBeginDt(laBeginDt);
		laDataline3b.setEndDt(laEndDt);
		laDataline3b.setInvQty(50);
		laDataline3b.setInvStatusCd(1);
		laDataline3b.setInvLocIdCd("E");
		laDataline3b.setInvId("RTSUSER");
		laDataline3b.setInvIdName("USER, RTS");
		laDataline3b.setInvInqBy("5");
		laDataline3b.setExceptionReport(0);
		for (int i = 0; i < 5; i++)
		{
			avResults.addElement(laDataline3b);
		} // end for loop

		//EMPLOYEE DATA
		// Build record 1
		InventoryInquiryUIData laDataline3c =
			new InventoryInquiryUIData();
		laDataline3c.setRptType(TXT_CURRENT_BALANCE);
		laDataline3c.setItmCd("FORM31");
		laDataline3c.setItmCdDesc("FORM 31-RTS");
		laDataline3c.setInvItmYr(0);
		laDataline3c.setInvItmNo("P592288");
		laDataline3c.setInvItmEndNo("P592297");
		laDataline3c.setBeginDt(laBeginDt);
		laDataline3c.setEndDt(laEndDt);
		laDataline3c.setInvQty(10);
		laDataline3c.setInvStatusCd(1);
		laDataline3c.setInvLocIdCd("E");
		laDataline3c.setInvId("JEFFRUE");
		laDataline3c.setInvIdName("RUE, JEFFREY");
		laDataline3c.setInvInqBy("5");
		laDataline3c.setExceptionReport(0);
		for (int i = 0; i < 5; i++)
		{
			avResults.addElement(laDataline3c);
		} // end for loop

		//build record 4: generate exception page
		InventoryInquiryUIData laDataline4 =
			new InventoryInquiryUIData();
		laDataline4.setRptType(TXT_CURRENT_BALANCE);
		laDataline4.setItmCd("WS");
		laDataline4.setItmCdDesc("WINDSHIELD STICKER");
		laDataline4.setInvItmYr(2002);
		laDataline4.setInvItmNo("");
		laDataline4.setInvItmEndNo("");
		laDataline4.setBeginDt(laBeginDt);
		laDataline4.setEndDt(laEndDt);
		laDataline4.setInvQty(0);
		laDataline4.setInvStatusCd(0);
		laDataline4.setInvLocIdCd("");
		laDataline4.setInvId("");
		laDataline4.setInvIdName("");
		laDataline4.setInvInqBy("3");
		laDataline4.setExceptionReport(1);
		for (int i = 0; i < 2; i++)
		{
			avResults.addElement(laDataline4);
		} // end for loop

		//build record 5: generate exception page
		InventoryInquiryUIData laDataline5 =
			new InventoryInquiryUIData();
		laDataline5.setRptType(TXT_HISTORY);
		laDataline5.setItmCd("WS");
		laDataline5.setItmCdDesc("WINDSHIELD STICKER");
		laDataline5.setInvItmYr(2002);
		laDataline5.setInvItmNo("");
		laDataline5.setInvItmEndNo("");
		laDataline5.setBeginDt(laBeginDt);
		laDataline5.setEndDt(laEndDt);
		laDataline5.setInvQty(0);
		laDataline5.setInvStatusCd(0);
		laDataline5.setInvLocIdCd("");
		laDataline5.setInvId("");
		laDataline5.setInvIdName("");
		laDataline5.setInvInqBy("3");
		laDataline5.setExceptionReport(1);
		for (int i = 0; i < 2; i++)
		{
			avResults.addElement(laDataline5);
		} // end for loop

		//build record 6: generate exception page
		InventoryInquiryUIData laDataline6 =
			new InventoryInquiryUIData();
		laDataline6.setRptType(TXT_HISTORY);
		laDataline6.setItmCd("TKP");
		laDataline6.setItmCdDesc("TRUCK PLATE");
		laDataline6.setInvItmYr(0);
		laDataline6.setInvItmNo("");
		laDataline6.setInvItmEndNo("");
		laDataline6.setBeginDt(laBeginDt);
		laDataline6.setEndDt(laEndDt);
		laDataline6.setInvQty(0);
		laDataline6.setInvStatusCd(0);
		laDataline6.setInvLocIdCd("");
		laDataline6.setInvId("");
		laDataline6.setInvIdName("");
		laDataline6.setInvInqBy("3");
		laDataline6.setExceptionReport(1);
		for (int i = 0; i < 100; i++)
		{
			avResults.addElement(laDataline6);
		} // end for loop

		return avResults;
	}

	/**
	 * Save Current Items
	 * 
	 * @param asCurrInvId String
	 * @param asCurrItmCd String
	 * @param asCurrRptType String
	 * @param aiInvItmYr int
	 */
	private void saveCurrentItems(
		String asCurrInvId,
		String asCurrItmCd,
		String asCurrRptType,
		int aiInvItmYr)
	{
		// Save current values for selected items
		csPrevInvId = asCurrInvId;
		csPrevItmCd = asCurrItmCd;
		csPrevRptType = asCurrRptType;
		ciPrevInvItmYr = aiInvItmYr;
	}

	/**
	 * Set print row adjustment.
	 * 
	 * @param aiPrntRowAdj int
	 */
	private void setPrntRowAdj(int aiPrntRowAdj)
	{
		ciPrntRowAdj = aiPrntRowAdj;
	}

	/**
	 * Set Record Index
	 * 
	 * @param aiNewRecordIndex int
	 */
	public void setRecordIndex(int aiNewRecordIndex)
	{
		ciRecordIndex = aiNewRecordIndex;
	}

	/**
	 * Set Total Quantity
	 * 
	 * @param aiNewTotalQty int
	 */
	public void setTotalQty(int aiNewTotalQty)
	{
		ciTotalQty = aiNewTotalQty;
	}
}
