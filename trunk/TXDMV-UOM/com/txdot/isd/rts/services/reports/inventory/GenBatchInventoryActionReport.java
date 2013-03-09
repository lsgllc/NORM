package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * GenBatchInventoryActionReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arredondo 	10/05/2001	New Class
 * Ray Rowehl	01/31/2002	Pass the selected date in on the vector.
 *							Work on making the header show on the first 
 *							page. do not print the Item year if it is 0
 *							defect 889
 * Ray Rowehl	02/01/2002	Add method for building the Entity Id String 
 * 							for section F and G.
 *							create checkForNewPageNeeded to handle 
 *							creating new pages. ove the Section Headers 
 *							into static.
 *							defect 1458
 * Ray Rowehl	02/10/2002	This is a significant rewrite of this class.
 *							Work on duplicate headers being generated.
 *							Also change end of page detection.
 *							defect 1653)
 * Ray Rowehl	02/23/2002	move checkForNewPageNeeded outside of the
 * 							for	loops. 
 *							defect 2213
 * Ray Rowehl	04/26/2002	defect 3684
 * Ray Rowehl	04/30/2002	defect 3686
 * E LyBrand	07/11/2003	Expand space for detail line item 
 * 							description in methods: partA() and partH()
 *							defect 6071
 * E LyBrand	08/13/2003	Expand space for detail line item 
 * 							description in methods: 
 * 							Parts B,C,D,E,F,G, and I.
 *							defect 6071 Ver 5.1.4  (again)
 * Min Wang		12/03/2003	Correct misspelling of Subcontractor.
 *							modify buildEntityString()
 *                          Defect 6404. Version 5.1.5 fix2.
 * Min Wang		12/03/2003	Print ISSUEDFROM and DASHLINE before 
 * 							TransWsId in Part I. Print InvLocIdCd and 
 * 							DASHLINE before InvId in Part I
 *							mdify partI()
 *							defect 6383  Ver 5.1.5  Fix 2
 * Min Wang		12/03/2003	Print ISSUEDBY and DASHLINE and show 
 * 							TransEmpId in Part I
 *							modify partI()
 *							defect 6684  Ver 5.1.5  Fix 2
 * Ray Rowehl	01/05/2004	Remove extra () in for statements.
 *							Also remove extra maintenance comments in 
 *							method JavaDocs
 *							modify partC(), partH()
 *							defect 6737  Ver 5.1.5 Fix 2
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modified definitions of constants to static 
 * 							final
 *							add variables as noted by PCR 34 comments
 * 							add handlePageBreak(int,Vector),
 *							handlePageBreak(int,Vector,Vector),
 *							partJ(),partJHeader(),
 *							partK(),partKHeader()
 * 							Ver 5.2.0
 * K Harrell	05/04/2004	Move over starting column for Part J & 
 *							K column headers / data
 *							modify constants
 *							defect 7068  Ver 5.2.0 
 * K Harrell	05/04/2004	Do not print column headers for Part J &
 *							part K if no data
 *							modify partJ(),partK()
 *							defect 7069  Ver 5.2.0
 * K Harrell	05/17/2004	Do not print column header for Part A
 *							if no items exist for that part. 
 *							Problem since RTS II
 *							modify partA()
 *							defect 7069  Ver 5.2.0
 * K Harrell	05/20/2004	Reduce space prior to Total row for Part J
 *							modify partJ()
 *							defect 7109  Ver 5.2.0
 * K.H. & M.W.	05/27/2004	Do not print column headers for Part A 
 *							if no data.
 *							add partAHeader(Vector avResults)
 *							deprecate partAHeader()
 *							modify partA()
 *							defect 7069  Ver 5.2.0
 * Min Wang		06/25/2004  Fix incorrect heading printing ( from Part A
 *							to Part I)
 *							modify partA(), partB(), partC(), partD(),
 *							partE(), partF(), partG(), partH(), and 
 *							partI()
 *							defect 6364  Ver 5.2.1
 * Min Wang   	07/06/2004	Fix two Part F sections on report.
 *							delete constant WHITE_SPACE
 *							modify constant BUFFER_SPACE_HEADER
 *							modify printNewPage()
 *							defect 7128 Ver 5.2.1
 * K Harrell	07/31/2004	Modify Part K; Use "Printed" vs. "Sold"
 *							Only print reprint count if >0 & vin !=""
 *							modify constants
 *							modify partKHeader()
 *							modify partK()
 *							defect 7401  Ver 5.2.1
 * K Harrell	10/10/2004	Include PrintNo in Part K
 *							modify constants
 *							modify partKHeader()
 *							defect 7598  Ver 5.2.1
 * K Harrell	10/10/2004	Print Headings Printed,Reprinted,Voided
 *							in that order
 *							modify constants
 *							modify partKHeader(),partK()
 *							defect 7612  Ver 5.2.1
 * K Harrell	10/11/2004	Modify for standard variable naming
 * 							conventions
 *							modify partJHeader(),partJ(),partKHeader(),
 *							partK()
 *							PCR 34 coding cleanup 
 * K Harrell	10/11/2004	Modify Part K Header to use RSPS ID vs.
 *							Origin, OriginId.  Include Disk No
 *							modify constants
 *							modify partKHeader(),partK()
 *							defect 7613 Ver 5.2.1
 * K Harrell	10/12/2004	Specify 3 as numLinesNeeded for partK()
 *							detail
 *							modify partK()
 *							defect 7616  Ver 5.2.1 
 * S Johnston	05/12/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from report.
 * 							modify partA(), partAHeader(),
 * 							partB(), partBHeader(), partC(), 
 * 							partCHeader(), partD(), partDHeader(),
 * 							partE(), partEHeader(), partF(), 
 * 							partFHeader(), partG(), partGHeader(),
 * 							partH(), partHHeader(), partI(), 
 * 							partIHeader(), partJ(), partJHeader(),
 * 							partK(), partKHeader()
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Min Wang		08/17/2005	Make more room for Item Description on 
 * 							partk of report.
 * 							modify partK(), partKHeader()
 * 							defect 8333 Ver 5.2.2 Fix 6
 * Min Wang     08/23/2005	Reformat the partK of report.
 * 							modify  KCOLUMN2, KCOLUMN4, KCOLUMN5, 
 * 							KCOLUMN6, KCOLUMN7, KCOLUMN8, KCOLUMN9, 
 * 							KCOLUMN3_OFFSET, KCOLUMN4_OFFSET, 
 * 							START_DASH_LONG, START_TOTAL_K, 
 * 							START_DASHK.
 * 							defect 8336 Ver 5.2.2. Fix 7
 * Ray Rowehl	09/14/2005	Constants work	
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	09/26/2005	Constants work
 * 							add formulateTransId()
 * 							defect 7890 Ver 5.2.3
 * Min Wang		07/20/2006  Pass parameters into formulateTransId()
 * 							to correctly display originating transaction 
 * 							id in Part C on the BIAR.
 * 							modify partA(), partB(), partC(), partH(), 
 * 							partI()
 * 							remove formulateTransId()
 * 							defect 8860 Ver 5.2.3
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	07/14/2010	add BIA_ITEM_NO_J_STARTPT, 
 * 							  BIA_ITEM_NO_J_OFFSET
 * 							modify BIA_EMP_ID_J_STARTPT,
 * 							 BIA_ITEM_DESC_J_STARTPT,
 * 							 BIA_ITEM_YEAR_J_STARTPT,
 * 							 BIA_ITEM_NUMBER_J_STARTPT,
 * 							 BIA_ITEM_NUMBER_J_OFFSET, 
 * 							 BIA_VIN_J_STARTPT,
 * 							 BIA_TRANSID_J_STARTPT,
 * 							 BIA_QTY_J_STARTPT
 * 							modify partJ()
 * 							defect 10491 Ver 6.5.0   
 * ---------------------------------------------------------------------
 */

/** 
 * This class generates the Inventory Action Report.
 *
 * @version	6.5.0 			07/14/2010
 * @author	Becky Arredond
 * <br>Creation Date:		09/13/2001
 */

public class GenBatchInventoryActionReport extends ReportTemplate
{
	// Alignment of column headers
	private static final int BIA_LENGTH_TOTAL_PRINTED = 5;

	// define field length
	private static final int BIA_ITEM_CODE_DESC_LENGTH = 27;

	// Identifing the headers, starting points and lengths
	private static final int BIA_DEFAULT_START_PT = 2;
	private static final int BIA_DEFAULT_LENGTH = 132;

	private static final int BIA_ITEM_DESC_ABCDEHI_STARTPT =
		BIA_DEFAULT_START_PT;
	private static final int BIA_ITEM_DESC_ABCDEHI_LENGTH = 25;

	private static final int BIA_ITEM_YEAR_ABCDEHI_STARTPT = 40;
	private static final int BIA_ITEM_YEAR_ABCDEHI_LENGTH = 6;

	private static final int BIA_ITEM_DESC_FG_STARTPT = 33;
	private static final int BIA_ITEM_DESC_FG_LENGTH =
		BIA_ITEM_DESC_ABCDEHI_LENGTH;

	private static final int BIA_ITEM_YEAR_FG_STARTPT = 64; //ITEM YEAR
	private static final int BIA_ITEM_YEAR_FG_LENGTH = 4;

	private static final int BIA_ITEM_NUMBER_I_STARTPT = 48;
	private static final int BIA_ITEM_NUMBER_I_LENGTH = 10;

	private static final int BIA_QTY_ABCDEH_STARTPT = 46; //QUANTITY
	private static final int BIA_QTY_ABCDEH_LENGTH = 8;

	private static final int BIA_QTY_FG_STARTPT = 71;
	private static final int BIA_QTY_FG_LENGTH = BIA_QTY_ABCDEH_LENGTH;

	// min and max qty
	private static final int BIA_QTY_MINMAX_FG_STARTPT = 81;
	private static final int BIA_QTY_MINMAX_FG_LENGTH =
		BIA_QTY_ABCDEH_LENGTH;

	private static final int BIA_BEGIN_ITMNO_ABCDEH_STARTPT = 56;
	private static final int BIA_BEGIN_ITMNO_ABCDEH_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;

	private static final int BIA_END_ITMNO_ABCDEH_STARTPT = 68;
	private static final int BIA_END_ITMNO_ABCDEH_LENGTH =
		InventoryConstant.MAX_ITEM_LENGTH;

	private static final int BIA_EMP_ABCH_STARTPT = 85; //EMPLOYEE ID
	private static final int BIA_EMP_ABCH_LENGTH = 8;

	private static final int BIA_ISSUED_DATE_ABCH_STARTPT = 103;
	private static final int BIA_ISSUED_DATE_ABCH_LENGTH = 10;

	private static final int BIA_ISSUED_BY_I_STARTPT = 60; //ISSUED BY
	private static final int BIA_ISSUED_BY_I_LENGTH = 7;

	private static final int BIA_ISSUED_FROM_I_STARTPT = 77;
	private static final int BIA_ISSUED_FROM_I_LENGTH = 7;

	private static final int BIA_TRANSID_ABCHI_STARTPT = 114;
	private static final int BIA_TRANSID_ABCHI_LENGTH = 17;

	private static final int BIA_ENTITY_FG_STARTPT =
		BIA_DEFAULT_START_PT;
	private static final int BIA_ENTITY_FG_LENGTH = 20;

	private static final int BIA_PROFILE_FG_STARTPT = 90;
	private static final int BIA_PROFILE_FG_LENGTH = 8;

	private static final int BIA_ALLOC_I_STARTPT = 88;
	private static final int BIA_ALLOC_I_LENGTH = 10;

	private static final int BIA_DEL_H_STARTPT = BIA_DEFAULT_START_PT;
	private static final int BIA_DEL_H_LENGTH = 40;

	private static final int BIA_VOID_C_STARTPT = 80;
	private static final int BIA_VOID_C_LENGTH = 23;

	private final static int BIA_TOTAL_J_STARTPT = 105;
	private static final int BIA_TOTAL_K_STARTPT = 93;

	private static final int BIA_WSID_J_STARTPT = BIA_DEFAULT_START_PT;
	// defect 10491 
	//	private static final int BIA_EMP_ID_J_STARTPT = 11;
	//	private static final int BIA_ITEM_DESC_J_STARTPT = 21;
	//	private static final int BIA_ITEM_YEAR_J_STARTPT = 48;
	//	private static final int BIA_VIN_J_STARTPT = 71;
	//	private static final int BIA_TRANSID_J_STARTPT = 94;
	//	private static final int BIA_QTY_J_STARTPT = 120;
	private static final int BIA_EMP_ID_J_STARTPT = 9;
	private static final int BIA_ITEM_DESC_J_STARTPT = 19;
	private static final int BIA_ITEM_YEAR_J_STARTPT = 46;
	private static final int BIA_ITEM_NUMBER_J_STARTPT = 60;
	private static final int BIA_ITEM_NUMBER_J_OFFSET = 2;
	private static final int BIA_VIN_J_STARTPT = 81;
	private static final int BIA_TRANSID_J_STARTPT = 104;
	private static final int BIA_QTY_J_STARTPT = 125;
	// end defect 10491 

	private static final int BIA_WSID_J_OFFSET = 0;
	private static final int BIA_EMP_ID_J_OFFSET = 0;
	private static final int BIA_ITEM_YEAR_J_OFFSET = 2;
	private static final int BIA_VIN_J_OFFSET = -6;
	private static final int BIA_TRANSID_J_OFFSET = -4;
	private static final int BIA_QTY_J_OFFSET = 2;

	private static final int BIA_RSPS_WSID_K_STARTPT =
		BIA_DEFAULT_START_PT;
	private static final int BIA_ITEM_DESC_K_STARTPT = 12;
	private static final int BIA_ITEM_YEAR_K_STARTPT = 42;
	private static final int BIA_DISK_NO_K_STARTPT = 54;
	private static final int BIA_PLATE_NO_K_STARTPT = 64;
	private static final int BIA_VIN_K_STARTPT = 83;
	private static final int BIA_PRINT_QTY_K_STARTPT = 105;
	private static final int BIA_REPRNT_QTY_K_STARTPT = 115;
	private static final int BIA_VOID_QTY_K_STARTPT = 126;
	private static final int BIA_RSPS_WSID_K_OFFSET = 0;
	private static final int BIA_ITEM_YEAR_K_OFFSET = 2;
	private static final int BIA_DISK_NO_K_OFFSET = 0;
	private static final int BIA_PLATE_NO_K_OFFSET = 0;
	private static final int BIA_VIN_K_OFFSET = -6;

	private static final int LENGTH_STICKER_QTY_DETAIL = 4;
	private static final int LENGTH_DISK_NO = 4;

	private static final int WHITE_SPACE_DETAIL_SHORT = 3;
	private static final int WHITE_SPACE_DETAIL_LONG = 4;
	private static final int WHITE_SPACE_HEADER = 9;

	// The part letters have been incorporated into these
	// constants to help locate where they are used.
	// Please update the names when used elsewhere.
	private static final String BIA_ALLOC_I_HEADER = "ALLOCATED";
	private static final String BIA_BEGIN_ABCDEH_HEADER = "BEGIN";
	private static final String BIA_BY_I_HEADER = "BY";
	private static final String BIA_CURR_FG_HEADER = "CURRENT";
	private static final String BIA_DATE_ABCHI_HEADER = "DATE";
	//right align
	private static final String BIA_DATE_ABCDEFGHIJK_STRING = "DATE";
	private static final String BIA_DELETED_H_HEADER = "DELETED";
	private static final String BIA_DESCRIPTION_ABCDEFGHI_HEADER =
		"DESCRIPTION";
	private static final String BIA_DISK_K_NO = "DISK NO";
	private static final String BIA_EMPLOYEE_ABCH_HEADER = "EMPLOYEE";
	private static final String BIA_EMP_ID_J = "EMP ID";
	private static final String BIA_END_ABCDEH_HEADER = "END";
	//right align
	private static final String BIA_ENTITY_FG_HEADER = "ENTITY";
	private static final String BIA_FROM_I_HEADER = "FROM";
	private static final String BIA_ISSUED_ABCI_HEADER = "ISSUED";
	private static final String BIA_ID_ABCH_HEADER = "ID";
	private static final String BIA_ITEM_ABCEGHI_HEADER = "ITEM";
	private static final String BIA_ITEM_DESCRIPTION_JK =
		"ITEM DESCRIPTION";
	// defect 10491 
	private static final String BIA_ITEM_NO_ABCEGHI_HEADER =
		"ITEM NUMBER";
	// end defect 10491 
	private static final String BIA_ITEM_YEAR_JK = "ITEM YEAR";
	private static final String BIA_MAX_G_HEADER = "MAXIMUM";
	private static final String BIA_MIN_F_HEADER = "MINIMUM";
	private static final String BIA_NUMBER_ABCDEHI_HEADER = "NUMBER";
	private static final String BIA_PLATE_NO_K = "PLATE NO ";
	private static final String BIA_PRINTED_K = "PRINTED";
	private static final String BIA_PROFILE_FG_HEADER = "PROFILE";
	private static final String BIA_QTY_J = "QTY";
	private static final String BIA_QUANTITY_ABCDEFGH_HEADER =
		"QUANTITY";
	private static final String BIA_REPRINTED_K = "REPRINTED";
	private static final String RSPS_ID_K = "RSPS ID";
	private static final String BIA_TO_HEADER = "TO";
	private static final String BIA_TOTAL_JK = "TOTAL";
	private static final String BIA_TRANS_HEADER = "TRANS";
	private static final String BIA_TRANS_ID = "TRANS ID";
	private static final String BIA_USED_HEADER = "USED";
	private static final String BIA_VIN = "VIN";
	private static final String BIA_VOIDED = "VOIDED";
	private static final String BIA_VOIDTRANS_HEADER =
		"VOIDING TRANSACTION ==>";
	private static final String BIA_WS_ID = "WS ID";
	private static final String BIA_YEAR_HEADER = "YEAR";

	private static final String BIA_PARTA_HEADER =
		"PART A:  THE FOLLOWING INVENTORY ITEMS WERE ISSUED AND NOT"
			+ " REMOVED FROM INVENTORY:                                  "
			+ "     ==========";
	private static final String BIA_PARTB_HEADER =
		"PART B:  THE FOLLOWING INVENTORY ITEMS WERE MARKED AS "
			+ "RE-ISSUED VOIDED INVENTORY:                           "
			+ "             ==========";
	private static final String BIA_PARTC_HEADER =
		"PART C:  THE FOLLOWING TRANSACTIONS WITH INVENTORY WERE "
			+ "VOIDED:                                                 "
			+ "         ==========";
	private static final String BIA_PARTD_HEADER =
		"PART D:  THE FOLLOWING INVENTORY ITEMS (WHICH WERE ON HOLD"
			+ " DUE TO TECHNICAL PROBLEMS) WERE RETURNED TO INVENTORY:   "
			+ "     ==========";
	private static final String BIA_PARTE_HEADER =
		"PART E:  THE FOLLOWING INVENTORY ITEMS ARE CURRENTLY ON"
			+ " HOLD BY THE HOLD/RELEASE EVENT:                       "
			+ "           ==========";
	private static final String BIA_PARTF_HEADER =
		"PART F:  THE FOLLOWING ENTITIES ARE BELOW THEIR MINIMUM "
			+ "INVENTORY LEVEL:                                        "
			+ "         ==========";
	private static final String BIA_PARTG_HEADER =
		"PART G:  THE FOLLOWING ENTITIES ARE ABOVE THEIR MAXIMUM"
			+ " INVENTORY LEVEL:                                      "
			+ "           ==========";
	private static final String BIA_PARTH_HEADER =
		"PART H:  THE FOLLOWING INVENTORY WAS DELETED USING THE "
			+ "INVENTORY DELETE EVENT (EXCLUDES VOIDS):               "
			+ "           ==========";
	private static final String BIA_PARTI_HEADER =
		"PART I:  THE FOLLOWING INVENTORY ITEMS WERE MISMATCHED:"
			+ "                                                       "
			+ "           ==========";
	private static final String BIA_PARTJ_HEADER =
		" PART J:  THE FOLLOWING INVENTORY ITEMS WERE REPRINTED ON"
			+ " POS:                                                    "
			+ "       ==========";
	private static final String BIA_PARTK_HEADER =
		" PART K: THE FOLLOWING INVENTORY ITEMS WERE PRINTED ON"
			+ " RSPS:                                                "
			+ "             ==========";

	private static final String DASH = "--------------------";
	private static final int DASH_STARTPT = 113;
	private static final String DASH_K =
		"-------------------------------";
	private static final int DASH_K_STARTPT = 102;
	private static final String DOUBLEDASH = "====================";
	private static final String DOUBLEDASH_K =
		"===============================";

	//	constants for sample data
	private static final String SAMPLE_EMP_ID = "0232301";
	private static final int SAMPLE_INV_QTY = 54;
	private static final String SAMPLE_ITEM_CODE = "DPS";
	private static final String SAMPLE_ITEM_CODE_DESC =
		"DISABLED PERSON PLT";
	private static final int SAMPLE_ITEM_YEAR = 2001;
	private static final String SAMPLE_ITM_END_NO = "157282PB";
	private static final String SAMPLE_ITM_NO = "157280PB";
	private static final int SAMPLE_OFC_NO = 11;
	private static final int SAMPLE_TRANS_AM_DATE = 371071;
	private static final int SAMPLE_TRANS_TIME = 64407;
	private static final int SAMPLE_WS_ID = 100;

	private static final String TXT_RTS990273 =
		" RTS990273-I-THERE ARE NO RECORDS TO REPORT";
	private static final String TXT_TEST_REPORT_FILE =
		"BatchInvActionRpt.txt";

	private static final String TXT_CENTRAL_DASH_UC =
		InventoryConstant.CNTRL + CommonConstant.STR_DASH;
	private static final String TXT_DEALER_DASH_UC =
		InventoryConstant.DLR + CommonConstant.STR_DASH;
	private static final String TXT_EMPLOYEE_DASH_UC =
		InventoryConstant.EMP + CommonConstant.STR_DASH;
	private static final String TXT_SUBCONTRACTOR_DASH_UC =
		InventoryConstant.SUBCON + CommonConstant.STR_DASH;
	private static final String TXT_WORKSTATION_DASH_UC =
		InventoryConstant.WS + CommonConstant.STR_DASH;

	private int ciDetailLinesPerPage = 0;
	private int ciDetailLinesUsed = 0;
	private boolean cbNewPageDone = false;

	/**
	 * GenBatchInventoryActionReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public GenBatchInventoryActionReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Create a string to describe the Entity being reported on.
	 * 
	 * @param aaDataIn InventoryProfileReportData
	 */
	private String buildEntityString(InventoryProfileReportData aaDataIn)
	{
		String lsEntityString = CommonConstant.STR_SPACE_EMPTY;
		// STR_C is Central or Server
		if (aaDataIn.getEntity().equals(InventoryConstant.CHAR_C))
		{
			lsEntityString = TXT_CENTRAL_DASH_UC + aaDataIn.getId();
		}
		// STR_D is for Dealer
		if (aaDataIn.getEntity().equals(InventoryConstant.CHAR_D))
		{
			lsEntityString = TXT_DEALER_DASH_UC + aaDataIn.getId();
		}
		// STR_E is for Employee
		if (aaDataIn.getEntity().equals(InventoryConstant.CHAR_E))
		{
			lsEntityString = TXT_EMPLOYEE_DASH_UC + aaDataIn.getInvId();
		}
		// STR_S is for Subcontractor
		if (aaDataIn.getEntity().equals(InventoryConstant.CHAR_S))
		{
			lsEntityString =
				TXT_SUBCONTRACTOR_DASH_UC + aaDataIn.getId();
		}
		// STR_W is for Workstation
		if (aaDataIn.getEntity().equals(InventoryConstant.CHAR_W))
		{
			lsEntityString = TXT_WORKSTATION_DASH_UC + aaDataIn.getId();
		}
		return lsEntityString;
	}

	/**
	 * This method goes in and computes Number of Detail Lines left on 
	 * the page.
	 */
	private void computeNumberOfPageLines()
	{
		ciDetailLinesPerPage =
			this.caRptProps.getPageHeight() - WHITE_SPACE_DETAIL_LONG;
	}

	/**
	 * This is the normal starting point for reports.
	 * It is not used in this class.
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// this method is not used in this class
	}

	//	/**
	//	 * Formulate Transaction Id for print.
	//	 * 
	//	 * @param laDataline
	//	 * @return String
	//	 * @deprecated
	//	 */
	//	private String formulateTransId(
	//		int aiOfcNo,
	//		int aiWsId,
	//		int aiAMDate,
	//		int aiTime)
	//	{
	//		// defect 8860
	//		return UtilityMethods.addPadding(
	//			new String[] {
	//				String.valueOf(aiOfcNo),
	//				String.valueOf(aiWsId),
	//				String.valueOf(aiAMDate),
	//				String.valueOf(aiTime)},
	//			new int[] {
	//				CommonConstant.LENGTH_OFFICE_ISSUANCENO,
	//				CommonConstant.LENGTH_TRANS_WSID,
	//				CommonConstant.LENGTH_TRANSAMDATE,
	//				CommonConstant.LENGTH_TRANS_TIME },
	//			CommonConstant.STR_ZERO);
	//		// defect 8860
	//	}

	/**
	 * Generate Attributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * If there were no records for the section, use this to show that.
	 * Create a new page if needed. 
	 * 
	 * @param avHeader Vector
	 * @param avTable Vector
	 */
	private void genNoRecordsToReport(Vector avHeader, Vector avTable)
	{
		if (isNewPageNeeded(WHITE_SPACE_HEADER))
		{
			printNewPage(avHeader, avTable);
		}
		// if a new page has been created, we do not need column
		// headings. otherwise, we do.
		if (cbNewPageDone)
		{
			cbNewPageDone = false;
		}
		else
		{
			generateHeader(avTable);
		}
		caRpt.print(TXT_RTS990273);
		caRpt.nextLine();
		//cRpt.nextLine();
		cbNewPageDone = false;
	}

	/**
	 * Handle Page Break
	 * 
	 * @param aiNumLinesNeeded int
	 * @param avHeader Vector
	 * @param avTable Vector
	 * @return boolean
	 */
	private boolean handlePageBreak(
		int aiNumLinesNeeded,
		Vector avHeader,
		Vector avTable)
	{
		int liNumLinesLeft = getNoOfDetailLines();
		if (liNumLinesLeft < aiNumLinesNeeded)
		{
			generateFooter();
			generateHeader(avHeader, avTable);
			return true;
		}
		return false;
	}

	/**
	 * Determines if a new page is needed.
	 * 
	 * @param aiBufferSpaceNeeded int determines amount of buffer 
	 *  space to leave.
	 * @return boolean
	 */
	private boolean isNewPageNeeded(int aiBufferSpaceNeeded)
	{
		// if the curent page is up to the page break point,
		// return true.
		return (
			this.caRpt.getCurrX()
				>= this.caRptProps.getPageHeight() - aiBufferSpaceNeeded);
	}

	/**
	 * This is normally used for testing class.
	 * It is not used here because it is difficult to set up.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties(ReportConstant.RPT_9901_IAR_REPORT_ID);
		GenBatchInventoryActionReport laBar =
			new GenBatchInventoryActionReport(
				ReportConstant.RPT_9901_IAR_REPORT_TITLE,
				laRptProps);

		Vector lvQueryResults =
			laBar.queryData(CommonConstant.STR_SPACE_EMPTY);
		laBar.formatReport(lvQueryResults);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(TXT_TEST_REPORT_FILE);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laBar.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(TXT_TEST_REPORT_FILE);
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
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Generate Part A of the IAR.
	 * 
	 * @param avResults Vector
	 */
	public void partA(Vector avReportData)
	{
		// compute the number of lines available for use 
		computeNumberOfPageLines();
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();

		Vector lvTable = partAHeader(lvResults);

		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		// always print the header entering Section A. 
		generateHeader(lvHeader, lvTable);

		cbNewPageDone = true;

		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// if there was a new page done, do not reprint the detail 
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// set DetailLinesUsed to be CurrX, also start K with
				// DetailLines 	
				ciDetailLinesUsed = this.caRpt.getCurrX();

				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing 
						// detail lines	
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}

						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						if (laDataline.getInvItmYr() > 0)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								BIA_ITEM_YEAR_ABCDEHI_STARTPT,
								BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						}
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_ABCDEH_STARTPT,
							BIA_QTY_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
							BIA_BEGIN_ITMNO_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							BIA_END_ITMNO_ABCDEH_STARTPT,
							BIA_END_ITMNO_ABCDEH_LENGTH);
						this.caRpt.print(
							laDataline.getTransEmpId(),
							BIA_EMP_ABCH_STARTPT,
							BIA_EMP_ABCH_LENGTH);
						this.caRpt.rightAlign(
							new RTSDate(
								RTSDate.AMDATE,
								laDataline.getTransAMDate())
								.toString(),
							BIA_ISSUED_DATE_ABCH_STARTPT,
							BIA_ISSUED_DATE_ABCH_LENGTH);
						// defect 8860
						//this.caRpt.print(
						//	formulateTransId(laDataline),
						this
							.caRpt
							.print(
								UtilityMethods.getTransId(
									laDataline.getOfcIssuanceNo(),
									laDataline.getTransWsId(),
									laDataline.getTransAMDate(),
									laDataline.getTransTime()),
						// end defect 8860
						BIA_TRANSID_ABCHI_STARTPT,
							BIA_TRANSID_ABCHI_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			cbNewPageDone = true;
			// we do want a new page out of Part A if there are no
			// records
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part A.
	 * 
	 * @param avResults Vector
	 * @return Vector
	 */
	public Vector partAHeader(Vector avResults)
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();

		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTA_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);

		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);

		if (avResults.size() > CommonConstant.ELEMENT_0)
		{
			//Column Headers for Row1
			Vector lvRow_1 = new Vector();
			Vector lvRow_2 = new Vector();

			ColumnHeader laColumn_1_1 =
				new ColumnHeader(
					BIA_ITEM_ABCEGHI_HEADER,
					BIA_ITEM_DESC_ABCDEHI_STARTPT,
					BIA_ITEM_DESC_ABCDEHI_LENGTH);
			ColumnHeader laColumn_1_2 =
				new ColumnHeader(
					BIA_ITEM_ABCEGHI_HEADER,
					BIA_ITEM_YEAR_ABCDEHI_STARTPT,
					BIA_ITEM_YEAR_ABCDEHI_LENGTH);
			ColumnHeader laColumn_1_3 =
				new ColumnHeader(
					BIA_BEGIN_ABCDEH_HEADER,
					BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
					BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
					ColumnHeader.RIGHT);
			ColumnHeader laColumn_1_4 =
				new ColumnHeader(
					BIA_END_ABCDEH_HEADER,
					BIA_END_ITMNO_ABCDEH_STARTPT,
					BIA_END_ITMNO_ABCDEH_LENGTH,
					ColumnHeader.RIGHT);
			ColumnHeader laColumn_1_5 =
				new ColumnHeader(
					BIA_EMPLOYEE_ABCH_HEADER,
					BIA_EMP_ABCH_STARTPT,
					BIA_EMP_ABCH_LENGTH);
			ColumnHeader laColumn_1_6 =
				new ColumnHeader(
					BIA_ISSUED_ABCI_HEADER,
					BIA_ISSUED_DATE_ABCH_STARTPT,
					BIA_ISSUED_DATE_ABCH_LENGTH,
					ColumnHeader.RIGHT);
			ColumnHeader laColumn_1_7 =
				new ColumnHeader(
					BIA_TRANS_HEADER,
					BIA_TRANSID_ABCHI_STARTPT,
					BIA_TRANSID_ABCHI_LENGTH);

			//Alignment of column headers to rows 1
			lvRow_1.addElement(laColumn_1_1);
			lvRow_1.addElement(laColumn_1_2);
			lvRow_1.addElement(laColumn_1_3);
			lvRow_1.addElement(laColumn_1_4);
			lvRow_1.addElement(laColumn_1_5);
			lvRow_1.addElement(laColumn_1_6);
			lvRow_1.addElement(laColumn_1_7);

			//Column Headers for Row2
			ColumnHeader laColumn_2_1 =
				new ColumnHeader(
					BIA_DESCRIPTION_ABCDEFGHI_HEADER,
					BIA_ITEM_DESC_ABCDEHI_STARTPT,
					BIA_ITEM_DESC_ABCDEHI_LENGTH);
			ColumnHeader laColumn_2_2 =
				new ColumnHeader(
					BIA_YEAR_HEADER,
					BIA_ITEM_YEAR_ABCDEHI_STARTPT,
					BIA_ITEM_YEAR_ABCDEHI_LENGTH);
			ColumnHeader laColumn_2_3 =
				new ColumnHeader(
					BIA_QUANTITY_ABCDEFGH_HEADER,
					BIA_QTY_ABCDEH_STARTPT,
					BIA_QTY_ABCDEH_LENGTH);
			ColumnHeader laColumn_2_4 =
				new ColumnHeader(
					BIA_NUMBER_ABCDEHI_HEADER,
					BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
					BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
					ColumnHeader.RIGHT);
			ColumnHeader laColumn_2_5 =
				new ColumnHeader(
					BIA_NUMBER_ABCDEHI_HEADER,
					BIA_END_ITMNO_ABCDEH_STARTPT,
					BIA_END_ITMNO_ABCDEH_LENGTH,
					ColumnHeader.RIGHT);
			ColumnHeader laColumn_2_6 =
				new ColumnHeader(
					BIA_ID_ABCH_HEADER,
					BIA_EMP_ABCH_STARTPT,
					BIA_EMP_ABCH_LENGTH);
			ColumnHeader laColumn_2_7 =
				new ColumnHeader(
					BIA_DATE_ABCHI_HEADER,
					BIA_ISSUED_DATE_ABCH_STARTPT,
					BIA_ISSUED_DATE_ABCH_LENGTH,
					ColumnHeader.RIGHT);
			ColumnHeader laColumn_2_8 =
				new ColumnHeader(
					BIA_ID_ABCH_HEADER,
					BIA_TRANSID_ABCHI_STARTPT,
					BIA_TRANSID_ABCHI_LENGTH);

			//Alignment of column headers to rows 2
			lvRow_2.addElement(laColumn_2_1);
			lvRow_2.addElement(laColumn_2_2);
			lvRow_2.addElement(laColumn_2_3);
			lvRow_2.addElement(laColumn_2_4);
			lvRow_2.addElement(laColumn_2_5);
			lvRow_2.addElement(laColumn_2_6);
			lvRow_2.addElement(laColumn_2_7);
			lvRow_2.addElement(laColumn_2_8);

			//Adding ColumnHeader Information
			lvTable.addElement(lvRow_0);
			lvTable.addElement(lvRow_1);
			lvTable.addElement(lvRow_2);
		}
		else
		{
			lvTable.addElement(lvRow_0);
		}
		return lvTable;
	}

	/**
	* Generate Part B of the IAR.
	 * 
	 * @param avReportData Vector
	 */
	public void partB(Vector avReportData)
	{
		// bring the elements in off of the input vector
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partBHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// start k with detail lines used
				ciDetailLinesUsed = this.caRpt.getCurrX();

				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing 
						// detail lines 
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);

						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						if (laDataline.getInvItmYr() > 0)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								BIA_ITEM_YEAR_ABCDEHI_STARTPT,
								BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						}
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_ABCDEH_STARTPT,
							BIA_QTY_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
							BIA_BEGIN_ITMNO_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							BIA_END_ITMNO_ABCDEH_STARTPT,
							BIA_END_ITMNO_ABCDEH_LENGTH);
						this.caRpt.print(
							laDataline.getTransEmpId(),
							BIA_EMP_ABCH_STARTPT,
							BIA_EMP_ABCH_LENGTH);
						this.caRpt.rightAlign(
							new RTSDate(
								RTSDate.AMDATE,
								laDataline.getTransAMDate())
								.toString(),
							BIA_ISSUED_DATE_ABCH_STARTPT,
							BIA_ISSUED_DATE_ABCH_LENGTH);
						// defect 8860
						//this.caRpt.print(
						//		formulateTransId(laDataline),
						this
							.caRpt
							.print(
								UtilityMethods.getTransId(
									laDataline.getOfcIssuanceNo(),
									laDataline.getTransWsId(),
									laDataline.getTransAMDate(),
									laDataline.getTransTime()),
						//end defect 8860
						BIA_TRANSID_ABCHI_STARTPT,
							BIA_TRANSID_ABCHI_LENGTH);

						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part B.
	 * 
	 * @return Vector
	 */
	public Vector partBHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTB_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_BEGIN_ABCDEH_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_END_ABCDEH_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				BIA_EMPLOYEE_ABCH_HEADER,
				BIA_EMP_ABCH_STARTPT,
				BIA_EMP_ABCH_LENGTH);
		ColumnHeader laColumn_1_6 =
			new ColumnHeader(
				BIA_ISSUED_ABCI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_7 =
			new ColumnHeader(
				BIA_TRANS_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 1
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		lvRow_1.addElement(laColumn_1_5);
		lvRow_1.addElement(laColumn_1_6);
		lvRow_1.addElement(laColumn_1_7);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_ABCDEH_STARTPT,
				BIA_QTY_ABCDEH_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_EMP_ABCH_STARTPT,
				BIA_EMP_ABCH_LENGTH);
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				BIA_DATE_ABCHI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		lvRow_2.addElement(laColumn_2_6);
		lvRow_2.addElement(laColumn_2_7);
		lvRow_2.addElement(laColumn_2_8);

		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part C of the IAR.
	 * 
	 * @param avResults Vector
	 */
	public void partC(Vector avReportData)
	{
		// bring the elements in off of the input vector
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partCHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail 
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// set K to DetailLinesUsed 
				ciDetailLinesUsed = this.caRpt.getCurrX();
				// defect 6732
				// remove extra () from for statement 
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k = k + 2)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing 
						// detail lines
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);

						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						if (laDataline.getInvItmYr() > 0)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								BIA_ITEM_YEAR_ABCDEHI_STARTPT,
								BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						}
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_ABCDEH_STARTPT,
							BIA_QTY_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
							BIA_BEGIN_ITMNO_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							BIA_END_ITMNO_ABCDEH_STARTPT,
							BIA_END_ITMNO_ABCDEH_LENGTH);
						this.caRpt.print(
							laDataline.getTransEmpId(),
							BIA_EMP_ABCH_STARTPT,
							BIA_EMP_ABCH_LENGTH);

						this.caRpt.rightAlign(
							new RTSDate(
								RTSDate.AMDATE,
								laDataline.getVoidTransAMDate())
								.toString(),
							BIA_ISSUED_DATE_ABCH_STARTPT,
							BIA_ISSUED_DATE_ABCH_LENGTH);
						// defect 8860
						//this.caRpt.print(
						//	formulateTransId(laDataline),
						this
							.caRpt
							.print(
								UtilityMethods.getTransId(
									laDataline.getVoidOfcIssuanceNo(),
									laDataline.getVoidTransWsId(),
									laDataline.getVoidTransAMDate(),
									laDataline.getVoidTransTime()),
						// end defect 8860
						BIA_TRANSID_ABCHI_STARTPT,
							BIA_TRANSID_ABCHI_LENGTH);
						this.caRpt.nextLine();
						this.caRpt.print(
							BIA_VOIDTRANS_HEADER,
							BIA_VOID_C_STARTPT,
							BIA_VOID_C_LENGTH);
						this.caRpt.rightAlign(
							new RTSDate(
								RTSDate.AMDATE,
								laDataline.getTransAMDate())
								.toString(),
							BIA_ISSUED_DATE_ABCH_STARTPT,
							BIA_ISSUED_DATE_ABCH_LENGTH);
						// defect 8860
						//this.caRpt.print(
						//	formulateTransId(laDataline),
						this
							.caRpt
							.print(
								UtilityMethods.getTransId(
									laDataline.getOfcIssuanceNo(),
									laDataline.getTransWsId(),
									laDataline.getTransAMDate(),
									laDataline.getTransTime()),
						// end defect 8860
						BIA_TRANSID_ABCHI_STARTPT,
							BIA_TRANSID_ABCHI_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part C.
	 * 
	 * @return Vector
	 */
	public Vector partCHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTC_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_BEGIN_ABCDEH_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_END_ABCDEH_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				BIA_EMPLOYEE_ABCH_HEADER,
				BIA_EMP_ABCH_STARTPT,
				BIA_EMP_ABCH_LENGTH);
		ColumnHeader laColumn_1_6 =
			new ColumnHeader(
				BIA_ISSUED_ABCI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_7 =
			new ColumnHeader(
				BIA_TRANS_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 1 
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		lvRow_1.addElement(laColumn_1_5);
		lvRow_1.addElement(laColumn_1_6);
		lvRow_1.addElement(laColumn_1_7);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_ABCDEH_STARTPT,
				BIA_QTY_ABCDEH_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_EMP_ABCH_STARTPT,
				BIA_EMP_ABCH_LENGTH);
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				BIA_DATE_ABCHI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		lvRow_2.addElement(laColumn_2_6);
		lvRow_2.addElement(laColumn_2_7);
		lvRow_2.addElement(laColumn_2_8);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part D of the IAR.
	 * 
	 * @param avReportData Vector
	 */
	public void partD(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partDHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// set K to DetailLinesUsed	  
				ciDetailLinesUsed = this.caRpt.getCurrX();
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing
						// detail lines 
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						if (laDataline.getInvItmYr() > 0)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								BIA_ITEM_YEAR_ABCDEHI_STARTPT,
								BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						}
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_ABCDEH_STARTPT,
							BIA_QTY_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
							BIA_BEGIN_ITMNO_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							BIA_END_ITMNO_ABCDEH_STARTPT,
							BIA_END_ITMNO_ABCDEH_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part D.
	 * 
	 * @return Vector
	 */
	public Vector partDHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTD_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_BEGIN_ABCDEH_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_END_ABCDEH_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		//Alignment of column headers to row 1 
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_ABCDEH_STARTPT,
				BIA_QTY_ABCDEH_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part E of the IAR.
	 * 
	 * @param avReportData Vector
	 */
	public void partE(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partEHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// set K to DetailLinesUsed	
				ciDetailLinesUsed = this.caRpt.getCurrX();
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing
						// detail lines 	
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						if (laDataline.getInvItmYr() > 0)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								BIA_ITEM_YEAR_ABCDEHI_STARTPT,
								BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						}
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_ABCDEH_STARTPT,
							BIA_QTY_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
							BIA_BEGIN_ITMNO_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							BIA_END_ITMNO_ABCDEH_STARTPT,
							BIA_END_ITMNO_ABCDEH_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = this.caRpt.getCurrX();
					}
					else
					{
						// set k so we can get out of the for loop	
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part E.
	 * 
	 * @return Vector
	 */
	public Vector partEHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTE_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_BEGIN_ABCDEH_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_END_ABCDEH_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		//Alignment of column headers to row 1
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_ABCDEH_STARTPT,
				BIA_QTY_ABCDEH_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part F of the IAR..
	 * 
	 * @param avReportData Vector
	 */
	public void partF(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partFHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryProfileReportData laDataline =
			new InventoryProfileReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// if there was a new page done, do not reprint the 
				// detail headers. 
				// otherwise, set the flag to false.
				if (cbNewPageDone)
				{
					cbNewPageDone = false;
				}
				else
				{
					generateHeader(lvTable);
				}
				// set K to DetailLinesUsed 
				ciDetailLinesUsed = this.caRpt.getCurrX();
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing
						// detail lines 
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryProfileReportData) lvResults
									.elementAt(
								i);
						this.caRpt.print(
							buildEntityString(laDataline),
							BIA_ENTITY_FG_STARTPT,
							BIA_ENTITY_FG_LENGTH);
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_FG_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						if (laDataline.getInvItmYr() > 0)
						{
							this.caRpt.print(
								String.valueOf(
									laDataline.getInvItmYr()),
								BIA_ITEM_YEAR_FG_STARTPT,
								BIA_ITEM_YEAR_FG_LENGTH);
						}
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_FG_STARTPT,
							BIA_QTY_FG_LENGTH);
						this.caRpt.center(
							String.valueOf(laDataline.getMinQty()),
							BIA_QTY_MINMAX_FG_STARTPT,
							BIA_QTY_MINMAX_FG_LENGTH);
						this.caRpt.print(
							laDataline.getId(),
							BIA_PROFILE_FG_STARTPT,
							BIA_PROFILE_FG_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = this.caRpt.getCurrX();
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part F.
	 * 
	 * @return Vector
	 */
	public Vector partFHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTF_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_FG_STARTPT,
				BIA_ITEM_DESC_FG_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_FG_STARTPT,
				BIA_ITEM_YEAR_FG_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_CURR_FG_HEADER,
				BIA_QTY_FG_STARTPT,
				BIA_QTY_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_MIN_F_HEADER,
				BIA_QTY_MINMAX_FG_STARTPT,
				BIA_QTY_MINMAX_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				BIA_PROFILE_FG_HEADER,
				BIA_PROFILE_FG_STARTPT,
				BIA_PROFILE_FG_LENGTH);
		//Alignment of column headers to row 1
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		lvRow_1.addElement(laColumn_1_5);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_ENTITY_FG_HEADER,
				BIA_ENTITY_FG_STARTPT,
				BIA_ENTITY_FG_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_FG_STARTPT,
				BIA_ITEM_DESC_FG_LENGTH);
		ColumnHeader laColumn10 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_FG_STARTPT,
				BIA_ITEM_YEAR_FG_LENGTH);
		ColumnHeader laColumn11 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_FG_STARTPT,
				BIA_QTY_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn12 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_MINMAX_FG_STARTPT,
				BIA_QTY_MINMAX_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn13 =
			new ColumnHeader(
				BIA_USED_HEADER,
				BIA_PROFILE_FG_STARTPT,
				BIA_PROFILE_FG_LENGTH);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn10);
		lvRow_2.addElement(laColumn11);
		lvRow_2.addElement(laColumn12);
		lvRow_2.addElement(laColumn13);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part G of the IAR
	 * 
	 * @param avReportData Vector
	 */
	public void partG(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partGHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryProfileReportData laDataline =
			new InventoryProfileReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				ciDetailLinesUsed = this.caRpt.getCurrX();
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing 
						// detail lines 
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryProfileReportData) lvResults
									.elementAt(
								i);
						this.caRpt.print(
							buildEntityString(laDataline),
							BIA_ENTITY_FG_STARTPT,
							BIA_ENTITY_FG_LENGTH);
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_FG_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						this.caRpt.print(
							String.valueOf(laDataline.getInvItmYr()),
							BIA_ITEM_YEAR_FG_STARTPT,
							BIA_ITEM_YEAR_FG_LENGTH);
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_FG_STARTPT,
							BIA_QTY_FG_LENGTH);
						this.caRpt.center(
							String.valueOf(laDataline.getMaxQty()),
							BIA_QTY_MINMAX_FG_STARTPT,
							BIA_QTY_MINMAX_FG_LENGTH);
						this.caRpt.print(
							laDataline.getId(),
							BIA_PROFILE_FG_STARTPT,
							BIA_PROFILE_FG_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop  
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part G
	 * 
	 * @return Vector
	 */
	public Vector partGHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTG_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_FG_STARTPT,
				BIA_ITEM_DESC_FG_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_FG_STARTPT,
				BIA_ITEM_YEAR_FG_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_CURR_FG_HEADER,
				BIA_QTY_FG_STARTPT,
				BIA_QTY_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_MAX_G_HEADER,
				BIA_QTY_MINMAX_FG_STARTPT,
				BIA_QTY_MINMAX_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				BIA_PROFILE_FG_HEADER,
				BIA_PROFILE_FG_STARTPT,
				BIA_PROFILE_FG_LENGTH);
		//Alignment of column headers to row 1
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		lvRow_1.addElement(laColumn_1_5);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_ENTITY_FG_HEADER,
				BIA_ENTITY_FG_STARTPT,
				BIA_ENTITY_FG_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_FG_STARTPT,
				BIA_ITEM_DESC_FG_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_FG_STARTPT,
				BIA_ITEM_YEAR_FG_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_FG_STARTPT,
				BIA_QTY_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_MINMAX_FG_STARTPT,
				BIA_QTY_MINMAX_FG_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_USED_HEADER,
				BIA_PROFILE_FG_STARTPT,
				BIA_PROFILE_FG_LENGTH);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		lvRow_2.addElement(laColumn_2_6);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part H of the IAR.
	 * 
	 * @param avReportData Vector
	 */
	public void partH(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partHHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail 
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// set K to DetailLinesUsed 
				ciDetailLinesUsed = this.caRpt.getCurrX();

				// remove extra () from for statment  
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k = k + 2)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing
						// detail lines 
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);
						// use Item 1 length 
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						this.caRpt.print(
							String.valueOf(laDataline.getInvItmYr()),
							BIA_ITEM_YEAR_ABCDEHI_STARTPT,
							BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						this.caRpt.center(
							String.valueOf(laDataline.getInvQty()),
							BIA_QTY_ABCDEH_STARTPT,
							BIA_QTY_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
							BIA_BEGIN_ITMNO_ABCDEH_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmEndNo(),
							BIA_END_ITMNO_ABCDEH_STARTPT,
							BIA_END_ITMNO_ABCDEH_LENGTH);
						this.caRpt.print(
							laDataline.getTransEmpId(),
							BIA_EMP_ABCH_STARTPT,
							BIA_EMP_ABCH_LENGTH);
						this.caRpt.rightAlign(
							String.valueOf(
								new RTSDate(
									RTSDate.AMDATE,
									laDataline.getTransAMDate())
									.toString()),
							BIA_ISSUED_DATE_ABCH_STARTPT,
							BIA_ISSUED_DATE_ABCH_LENGTH);
						// defect 8860
						//this.caRpt.print(
						//	formulateTransId(laDataline),
						this
							.caRpt
							.print(
								UtilityMethods.getTransId(
									laDataline.getOfcIssuanceNo(),
									laDataline.getTransWsId(),
									laDataline.getTransAMDate(),
									laDataline.getTransTime()),
						// end defect 8860
						BIA_TRANSID_ABCHI_STARTPT,
							BIA_TRANSID_ABCHI_LENGTH);
						this.caRpt.nextLine();
						this.caRpt.print(
							laDataline.getDelInvReasn(),
							BIA_DEL_H_STARTPT,
							BIA_DEL_H_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part H.
	 * 
	 * @return Vector
	 */
	public Vector partHHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTH_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_BEGIN_ABCDEH_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_END_ABCDEH_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_END_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				BIA_EMPLOYEE_ABCH_HEADER,
				BIA_EMP_ABCH_STARTPT,
				BIA_EMP_ABCH_LENGTH);
		ColumnHeader laColumn_1_6 =
			new ColumnHeader(
				BIA_DELETED_H_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_7 =
			new ColumnHeader(
				BIA_TRANS_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 1
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		lvRow_1.addElement(laColumn_1_5);
		lvRow_1.addElement(laColumn_1_6);
		lvRow_1.addElement(laColumn_1_7);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_QUANTITY_ABCDEFGH_HEADER,
				BIA_QTY_ABCDEH_STARTPT,
				BIA_QTY_ABCDEH_LENGTH);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_BEGIN_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_END_ITMNO_ABCDEH_STARTPT,
				BIA_BEGIN_ITMNO_ABCDEH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_EMP_ABCH_STARTPT,
				BIA_EMP_ABCH_LENGTH);
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				BIA_DATE_ABCHI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_9 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 2
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		lvRow_2.addElement(laColumn_2_6);
		lvRow_2.addElement(laColumn_2_8);
		lvRow_2.addElement(laColumn_2_9);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part I of the IAR
	 * 
	 * @param avReportData Vector
	 */
	public void partI(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvResults =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partIHeader();
		//Adding additional Header information
		lvHeader.addElement(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.addElement(lsReportDate);
		InventoryActionReportData laDataline =
			new InventoryActionReportData();
		int i = 0;
		if (!(lvResults == null)
			&& (lvResults.size() > CommonConstant.ELEMENT_0))
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail 
			// headers.  
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			while (i < lvResults.size())
			{
				//Loop through the results
				// set K to DetailLinesUsed	
				ciDetailLinesUsed = this.caRpt.getCurrX();
				for (int k = ciDetailLinesUsed;
					k <= ciDetailLinesPerPage;
					k++)
				{
					if (i < lvResults.size())
					{
						// turn off new page done boolean if doing 
						// detail lines 
						if (cbNewPageDone)
						{
							cbNewPageDone = false;
						}
						laDataline =
							(
								InventoryActionReportData) lvResults
									.elementAt(
								i);
						this.caRpt.print(
							laDataline.getItmCdDesc(),
							BIA_ITEM_DESC_ABCDEHI_STARTPT,
							BIA_ITEM_CODE_DESC_LENGTH);
						this.caRpt.print(
							String.valueOf(laDataline.getInvItmYr()),
							BIA_ITEM_YEAR_ABCDEHI_STARTPT,
							BIA_ITEM_YEAR_ABCDEHI_LENGTH);
						this.caRpt.rightAlign(
							laDataline.getInvItmNo(),
							BIA_ITEM_NUMBER_I_STARTPT,
							BIA_ITEM_NUMBER_I_LENGTH);
						this.caRpt.print(
							InventoryConstant.CHAR_E
								+ CommonConstant.STR_DASH
								+ laDataline.getTransEmpId(),
							BIA_ISSUED_BY_I_STARTPT,
							SAMPLE_OFC_NO);

						// add ISSUEDFROM and DASHLINE before TransWsId
						this.caRpt.print(
							InventoryConstant.CHAR_W
								+ CommonConstant.STR_DASH
								+ String.valueOf(
									laDataline.getTransWsId()),
							BIA_ISSUED_FROM_I_STARTPT,
							BIA_ISSUED_FROM_I_LENGTH);
						// add InvLocIdCd and DASHLINE before InvId    
						this.caRpt.print(
							laDataline.getInvLocIdCd()
								+ CommonConstant.STR_DASH
								+ laDataline.getInvId(),
							BIA_ALLOC_I_STARTPT,
							BIA_ALLOC_I_LENGTH);
						this.caRpt.rightAlign(
							new RTSDate(
								RTSDate.AMDATE,
								laDataline.getTransAMDate())
								.toString(),
							BIA_ISSUED_DATE_ABCH_STARTPT,
							BIA_ISSUED_DATE_ABCH_LENGTH);
						// defect 8860
						//this.caRpt.print(
						//	formulateTransId(laDataline),
						this
							.caRpt
							.print(
								UtilityMethods.getTransId(
									laDataline.getOfcIssuanceNo(),
									laDataline.getTransWsId(),
									laDataline.getTransAMDate(),
									laDataline.getTransTime()),
						// end defect 8860
						BIA_TRANSID_ABCHI_STARTPT,
							BIA_TRANSID_ABCHI_LENGTH);
						this.caRpt.nextLine();
						i = i + 1;
						ciDetailLinesUsed = this.caRpt.getCurrX();
						k = ciDetailLinesUsed;
					}
					else
					{
						// set k so we can get out of the for loop
						k = ciDetailLinesPerPage + 1;
					}
				}
				if (isNewPageNeeded(WHITE_SPACE_DETAIL_LONG))
				{
					if (lvResults.size() > i)
					{
						printNewPage(lvHeader, lvTable);
					}
				}
			}
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_2);
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part I.
	 * 
	 * @return Vector
	 */
	public Vector partIHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_0 = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_0_1 =
			new ColumnHeader(
				BIA_PARTI_HEADER,
				BIA_DEFAULT_START_PT,
				BIA_DEFAULT_LENGTH);
		//Alignment of column headers to row 0
		lvRow_0.addElement(laColumn_0_1);
		//Column Headers for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_2 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_1_3 =
			new ColumnHeader(
				BIA_ITEM_ABCEGHI_HEADER,
				BIA_ITEM_NUMBER_I_STARTPT,
				BIA_ITEM_NUMBER_I_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_4 =
			new ColumnHeader(
				BIA_ISSUED_ABCI_HEADER,
				BIA_ISSUED_BY_I_STARTPT,
				BIA_ISSUED_BY_I_LENGTH);
		ColumnHeader laColumn_1_5 =
			new ColumnHeader(
				BIA_ISSUED_ABCI_HEADER,
				BIA_ISSUED_FROM_I_STARTPT,
				BIA_ISSUED_FROM_I_LENGTH);
		ColumnHeader laColumn_1_8 =
			new ColumnHeader(
				BIA_ALLOC_I_HEADER,
				BIA_ALLOC_I_STARTPT,
				BIA_ALLOC_I_LENGTH);
		ColumnHeader laColumn_1_9 =
			new ColumnHeader(
				BIA_ISSUED_ABCI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_1_10 =
			new ColumnHeader(
				BIA_TRANS_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 1
		lvRow_1.addElement(laColumn_1_1);
		lvRow_1.addElement(laColumn_1_2);
		lvRow_1.addElement(laColumn_1_3);
		lvRow_1.addElement(laColumn_1_4);
		lvRow_1.addElement(laColumn_1_5);
		lvRow_1.addElement(laColumn_1_8);
		lvRow_1.addElement(laColumn_1_9);
		lvRow_1.addElement(laColumn_1_10);
		//Column Headers for Row2
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_DESCRIPTION_ABCDEFGHI_HEADER,
				BIA_ITEM_DESC_ABCDEHI_STARTPT,
				BIA_ITEM_DESC_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_YEAR_HEADER,
				BIA_ITEM_YEAR_ABCDEHI_STARTPT,
				BIA_ITEM_YEAR_ABCDEHI_LENGTH);
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_NUMBER_ABCDEHI_HEADER,
				BIA_ITEM_NUMBER_I_STARTPT,
				BIA_ITEM_NUMBER_I_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_BY_I_HEADER,
				BIA_ISSUED_BY_I_STARTPT,
				BIA_ISSUED_BY_I_LENGTH);
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_FROM_I_HEADER,
				BIA_ISSUED_FROM_I_STARTPT,
				BIA_ISSUED_FROM_I_LENGTH);
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_TO_HEADER,
				BIA_ALLOC_I_STARTPT,
				BIA_ALLOC_I_LENGTH);
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				BIA_DATE_ABCHI_HEADER,
				BIA_ISSUED_DATE_ABCH_STARTPT,
				BIA_ISSUED_DATE_ABCH_LENGTH,
				ColumnHeader.RIGHT);
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				BIA_ID_ABCH_HEADER,
				BIA_TRANSID_ABCHI_STARTPT,
				BIA_TRANSID_ABCHI_LENGTH);
		//Alignment of column headers to row 
		lvRow_2.addElement(laColumn_2_1);
		lvRow_2.addElement(laColumn_2_2);
		lvRow_2.addElement(laColumn_2_3);
		lvRow_2.addElement(laColumn_2_4);
		lvRow_2.addElement(laColumn_2_5);
		lvRow_2.addElement(laColumn_2_6);
		lvRow_2.addElement(laColumn_2_7);
		lvRow_2.addElement(laColumn_2_8);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_0);
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Generate Part J; POS Reprints
	 * 
	 * @param avReportData Vector
	 */
	public void partJ(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvReprintData =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partJHeader();
		//Adding additional Header information
		lvHeader.add(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.add(lsReportDate);
		if (lvReprintData != null
			&& lvReprintData.size() > CommonConstant.ELEMENT_0)
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail 
			// headers. 
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			int liTotalReprints = 0;
			int i = 0;
			while (i < lvReprintData.size())
			{
				ReprintData laReprintData =
					(ReprintData) lvReprintData.get(i);
				int liGroupReprints = 0;
				int k = i;
				while (k < lvReprintData.size())
				{
					ReprintData laNextReprintData =
						(ReprintData) lvReprintData.get(k);
					if (laReprintData.getWsId()
						!= laNextReprintData.getWsId())
					{
						break;
					}
					// column 1 	
					String lsWsID =
						CommonConstant.STR_SPACE_EMPTY
							+ laNextReprintData.getWsId();
					caRpt.print(
						lsWsID,
						BIA_WSID_J_STARTPT + BIA_WSID_J_OFFSET,
						lsWsID.length());
					// column 2
					String lsEmp = laNextReprintData.getEmpId();
					caRpt.print(
						lsEmp,
						BIA_EMP_ID_J_STARTPT + BIA_EMP_ID_J_OFFSET,
						lsEmp.length());
					// column 3 
					ItemCodesData laItmcd =
						(ItemCodesData) ItemCodesCache.getItmCd(
							laNextReprintData.getItmCd());
					String lsItemCdDesc = laItmcd.getItmCdDesc();
					caRpt.print(
						lsItemCdDesc,
						BIA_ITEM_DESC_J_STARTPT,
						lsItemCdDesc.length());

					// column 4		
					int liItmYr = laNextReprintData.getItmYr();
					if (liItmYr != 0)
					{
						String lsItemYear =
							CommonConstant.STR_SPACE_EMPTY
								+ laNextReprintData.getItmYr();
						caRpt.print(
							lsItemYear,
							BIA_ITEM_YEAR_J_STARTPT
								+ BIA_ITEM_YEAR_J_OFFSET,
							lsItemYear.length());
					}
					
					// defect 10491 
					// Print InvItmNo  (for Permits) 
					// column 5 
					String lsInvItmNo = laNextReprintData.getInvItmNo();
					if (lsInvItmNo != null)
					{
						caRpt.print(
							lsInvItmNo,
							BIA_ITEM_NUMBER_J_STARTPT
								+ BIA_ITEM_NUMBER_J_OFFSET,
							lsInvItmNo.length());
					}
					// end defect 10491 

					// column 6
					String lsVIN = laNextReprintData.getVIN();
					caRpt.print(
						lsVIN,
						BIA_VIN_J_STARTPT + BIA_VIN_J_OFFSET,
						lsVIN.length());

					// column 7
					String lsTransId = laNextReprintData.getTransId();
					caRpt.print(
						lsTransId,
						BIA_TRANSID_J_STARTPT + BIA_TRANSID_J_OFFSET,
						lsTransId.length());

					// column 8 
					int liPrintQty = laNextReprintData.getPrntQty();
					String lsQty =
						String.valueOf(laNextReprintData.getPrntQty());
					caRpt.print(
						lsQty,
						BIA_QTY_J_STARTPT
							+ BIA_QTY_J_OFFSET
							- lsQty.length()
							+ 1,
						lsQty.length());
					boolean lbDidPageBreak =
						handlePageBreak(
							WHITE_SPACE_DETAIL_SHORT,
							lvHeader,
							lvTable);
					if (!lbDidPageBreak)
					{
						caRpt.nextLine();
					}
					liGroupReprints = liGroupReprints + liPrintQty;
					k++;
				}
				handlePageBreak(
					WHITE_SPACE_DETAIL_SHORT,
					lvHeader,
					lvTable);
				// print out total reprints for this origin
				caRpt.print(DASH, DASH_STARTPT, DASH.length());
				caRpt.nextLine();
				String lsGroupReprints =
					CommonConstant.STR_SPACE_EMPTY + liGroupReprints;
				caRpt.print(
					lsGroupReprints,
					BIA_QTY_J_STARTPT
						+ BIA_QTY_J_OFFSET
						- lsGroupReprints.length()
						+ 1,
					lsGroupReprints.length());
				caRpt.blankLines(1);
				caRpt.nextLine();
				liTotalReprints += liGroupReprints;
				i = k;
			}
			handlePageBreak(
				WHITE_SPACE_DETAIL_SHORT,
				lvHeader,
				lvTable);
			// print out total reprints for all origins
			caRpt.print(DOUBLEDASH, DASH_STARTPT, DOUBLEDASH.length());
			caRpt.nextLine();
			caRpt.print(
				BIA_TOTAL_JK,
				BIA_TOTAL_J_STARTPT,
				BIA_TOTAL_JK.length());
			String lsTotalReprints =
				CommonConstant.STR_SPACE_EMPTY + liTotalReprints;
			caRpt.print(
				lsTotalReprints,
				BIA_QTY_J_STARTPT
					+ BIA_QTY_J_OFFSET
					- lsTotalReprints.length()
					+ 1,
				lsTotalReprints.length());
			caRpt.nextLine();
			//caRpt.nextLine();
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
	}

	/**
	 * Create the column headers for Part J.
	 * 
	 * @return Vector
	 */
	public Vector partJHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_PARTJ_HEADER,
				0,
				BIA_PARTJ_HEADER.length());
		//Alignment of lColumn headers to row 0
		lvRow_1.add(laColumn_1_1);
		//Column Headers for Row1
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				BIA_WS_ID,
				BIA_WSID_J_STARTPT,
				BIA_WS_ID.length());
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_EMP_ID_J,
				BIA_EMP_ID_J_STARTPT,
				BIA_EMP_ID_J.length());
		// defect 8269
		//ColumnHeader laColumnB3 =
		//	new ColumnHeader(ITEM_CODE, JCOLUMN3, ITEM_CODE.length());
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_ITEM_DESCRIPTION_JK,
				BIA_ITEM_DESC_J_STARTPT,
				BIA_ITEM_DESCRIPTION_JK.length());
		// defect 8269
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_ITEM_YEAR_JK,
				BIA_ITEM_YEAR_J_STARTPT,
				BIA_ITEM_YEAR_JK.length());

		ColumnHeader laColumn_2_4b =
			new ColumnHeader(
				BIA_ITEM_NO_ABCEGHI_HEADER,
				BIA_ITEM_NUMBER_J_STARTPT,
				BIA_ITEM_NO_ABCEGHI_HEADER.length());

		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_VIN,
				BIA_VIN_J_STARTPT,
				BIA_VIN.length());
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_TRANS_ID,
				BIA_TRANSID_J_STARTPT,
				BIA_TRANS_ID.length());
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				BIA_QTY_J,
				BIA_QTY_J_STARTPT,
				BIA_QTY_J.length());
		//Alignment of lColumn headers to row 1
		lvRow_2.add(laColumn_2_1);
		lvRow_2.add(laColumn_2_2);
		lvRow_2.add(laColumn_2_3);
		lvRow_2.add(laColumn_2_4);
		lvRow_2.add(laColumn_2_4b);
		lvRow_2.add(laColumn_2_5);
		lvRow_2.add(laColumn_2_6);
		lvRow_2.add(laColumn_2_7);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Print Part K of Inventory Action Report, i.e. data from 
	 * RTS.RTS_RSPS_PRNT for the selected date.  
	 * 
	 * @param avReportData Vector
	 */
	public void partK(Vector avReportData)
	{
		// bring the elements in off of the input vector 
		String lsReportDate =
			(String) avReportData.elementAt(CommonConstant.ELEMENT_0);
		Vector lvReprintData =
			(Vector) avReportData.elementAt(CommonConstant.ELEMENT_1);
		//Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = partKHeader();
		//Adding additional Header information
		lvHeader.add(BIA_DATE_ABCDEFGHIJK_STRING);
		lvHeader.add(lsReportDate);
		if (lvReprintData != null
			&& lvReprintData.size() > CommonConstant.ELEMENT_0)
		{
			// start a new page if needed for the header
			if (isNewPageNeeded(WHITE_SPACE_HEADER))
			{
				printNewPage(lvHeader, lvTable);
			}
			// if there was a new page done, do not reprint the detail 
			// headers. 
			// otherwise, set the flag to false.
			if (cbNewPageDone)
			{
				cbNewPageDone = false;
			}
			else
			{
				generateHeader(lvTable);
			}
			int liTotalPrinted = 0;
			int liTotalVoided = 0;
			int liTotalReprinted = 0;
			int i = 0;
			while (i < lvReprintData.size())
			{
				ReprintData laReprintData =
					(ReprintData) lvReprintData.get(i);
				int liGroupPrinted = 0;
				int liGroupVoided = 0;
				int liGroupReprints = 0;
				int k = i;
				//int liPriorDiskNo = -1; 
				while (k < lvReprintData.size())
				{
					ReprintData laNextReprintData =
						(ReprintData) lvReprintData.get(k);
					// Group changes when Origin/OriginId changes
					if (!laReprintData
						.getScannerId()
						.equals(laNextReprintData.getScannerId()))
					{
						break;
					}
					String lsRSPSId = laNextReprintData.getScannerId();
					caRpt.print(
						lsRSPSId,
						BIA_RSPS_WSID_K_STARTPT
							+ BIA_RSPS_WSID_K_OFFSET,
						lsRSPSId.length());
					// column 2 - ITMCD
					ItemCodesData laItmcd =
						(ItemCodesData) ItemCodesCache.getItmCd(
							laNextReprintData.getItmCd());
					String lsItemCdDesc = laItmcd.getItmCdDesc();
					caRpt.print(
						lsItemCdDesc,
						BIA_ITEM_DESC_K_STARTPT,
						lsItemCdDesc.length());
					// column 3 - ITMYR		
					String lsItemYear =
						CommonConstant.STR_SPACE_EMPTY
							+ laNextReprintData.getItmYr();
					caRpt.print(
						lsItemYear,
						BIA_ITEM_YEAR_K_STARTPT
							+ BIA_ITEM_YEAR_K_OFFSET,
						lsItemYear.length());
					// column 4 - DISKNO
					String lsDiskNo = CommonConstant.STR_SPACE_EMPTY;
					if (laNextReprintData.getDiskNum()
						!= CommonConstant.NOT_FOUND)
					{
						lsDiskNo =
							CommonConstant.STR_SPACE_EMPTY
								+ laNextReprintData.getDiskNum();
					}
					lsDiskNo =
						UtilityMethods.addPadding(
							lsDiskNo,
							LENGTH_DISK_NO,
							CommonConstant.STR_SPACE_ONE);
					caRpt.print(
						lsDiskNo,
						BIA_DISK_NO_K_STARTPT + BIA_DISK_NO_K_OFFSET,
						lsDiskNo.length());
					// column 5 - PLATENO
					String lsPlateNo = CommonConstant.STR_SPACE_ONE;
					if (laNextReprintData.getRegPltNo() != null)
					{
						lsPlateNo = laNextReprintData.getRegPltNo();
					}
					caRpt.print(
						lsPlateNo,
						BIA_PLATE_NO_K_STARTPT + BIA_PLATE_NO_K_OFFSET,
						lsPlateNo.length());
					// column 6 - VIN
					String lsVIN = CommonConstant.STR_SPACE_ONE;
					if (laNextReprintData.getVIN() != null)
					{
						lsVIN = laNextReprintData.getVIN();
					}
					caRpt.print(
						lsVIN,
						BIA_VIN_K_STARTPT + BIA_VIN_K_OFFSET,
						lsVIN.length());
					// column 7 - PRNTQTY
					String lsPrinted = CommonConstant.STR_SPACE_EMPTY;
					if (laNextReprintData.getPrntQty() > 0)
					{
						lsPrinted =
							lsPrinted + laNextReprintData.getPrntQty();
					}
					lsPrinted =
						UtilityMethods.addPadding(
							lsPrinted,
							LENGTH_STICKER_QTY_DETAIL,
							CommonConstant.STR_SPACE_ONE);
					caRpt.rightAlign(
						lsPrinted,
						BIA_PRINT_QTY_K_STARTPT,
						LENGTH_STICKER_QTY_DETAIL);
					liGroupPrinted += laNextReprintData.getPrntQty();
					// column 8 - REPRINT
					int liReprints = laNextReprintData.getReprntQty();
					String lsReprints = CommonConstant.STR_SPACE_EMPTY;
					if (liReprints > 0)
					{
						lsReprints =
							lsReprints
								+ laNextReprintData.getReprntQty();
					}
					lsReprints =
						UtilityMethods.addPadding(
							lsReprints,
							LENGTH_STICKER_QTY_DETAIL,
							CommonConstant.STR_SPACE_ONE);
					caRpt.rightAlign(
						lsReprints,
						BIA_REPRNT_QTY_K_STARTPT + 1,
						LENGTH_STICKER_QTY_DETAIL);
					liGroupReprints += laNextReprintData.getReprntQty();
					// column 9 - VOIDQTY
					String lsVoids = CommonConstant.STR_SPACE_EMPTY;
					if (laNextReprintData.getVoided() > 0)
					{
						lsVoids =
							lsVoids + laNextReprintData.getVoided();
					}
					lsVoids =
						UtilityMethods.addPadding(
							lsVoids,
							LENGTH_STICKER_QTY_DETAIL,
							CommonConstant.STR_SPACE_ONE);
					caRpt.rightAlign(
						lsVoids,
						BIA_VOID_QTY_K_STARTPT,
						LENGTH_STICKER_QTY_DETAIL);
					liGroupVoided =
						liGroupVoided + laNextReprintData.getVoided();
					boolean lbDidPageBreak =
						handlePageBreak(
							WHITE_SPACE_DETAIL_SHORT,
							lvHeader,
							lvTable);
					if (!lbDidPageBreak)
					{
						caRpt.nextLine();
					}
					k++;
				}
				handlePageBreak(
					WHITE_SPACE_DETAIL_SHORT,
					lvHeader,
					lvTable);
				// print out total reprints for this RSPS Id
				caRpt.print(DASH_K, DASH_K_STARTPT, DASH_K.length());
				caRpt.nextLine();
				String lsGroupPrinted =
					UtilityMethods.addPadding(
						String.valueOf(liGroupPrinted),
						BIA_LENGTH_TOTAL_PRINTED,
						CommonConstant.STR_SPACE_ONE);
				String lsGroupVoided =
					UtilityMethods.addPadding(
						String.valueOf(liGroupVoided),
						BIA_LENGTH_TOTAL_PRINTED,
						CommonConstant.STR_SPACE_ONE);
				String lsGroupReprints =
					UtilityMethods.addPadding(
						String.valueOf(liGroupReprints),
						BIA_LENGTH_TOTAL_PRINTED,
						CommonConstant.STR_SPACE_ONE);
				caRpt.print(
					lsGroupPrinted,
					BIA_PRINT_QTY_K_STARTPT - 1,
					BIA_LENGTH_TOTAL_PRINTED);
				caRpt.print(
					lsGroupReprints,
					BIA_REPRNT_QTY_K_STARTPT,
					BIA_LENGTH_TOTAL_PRINTED);
				caRpt.print(
					lsGroupVoided,
					BIA_VOID_QTY_K_STARTPT - 1,
					BIA_LENGTH_TOTAL_PRINTED);
				caRpt.blankLines(1);
				caRpt.nextLine();
				// Add to Group Totals 
				liTotalPrinted = liTotalPrinted + liGroupPrinted;
				liTotalVoided = liTotalVoided + liGroupVoided;
				liTotalReprinted = liTotalReprinted + liGroupReprints;
				i = k;
			}
			handlePageBreak(
				WHITE_SPACE_DETAIL_SHORT,
				lvHeader,
				lvTable);
			// print out total reprints for all RSPS Ids
			caRpt.nextLine();
			caRpt.blankLines(1);
			caRpt.print(
				DOUBLEDASH_K,
				DASH_K_STARTPT,
				DOUBLEDASH_K.length());
			caRpt.nextLine();
			caRpt.print(
				BIA_TOTAL_JK,
				BIA_TOTAL_K_STARTPT,
				BIA_TOTAL_JK.length());
			String lsTotalPrinted =
				UtilityMethods.addPadding(
					String.valueOf(liTotalPrinted),
					BIA_LENGTH_TOTAL_PRINTED,
					CommonConstant.STR_SPACE_ONE);
			String lsTotalVoided =
				UtilityMethods.addPadding(
					String.valueOf(liTotalVoided),
					BIA_LENGTH_TOTAL_PRINTED,
					CommonConstant.STR_SPACE_ONE);
			String lsTotalReprints =
				UtilityMethods.addPadding(
					String.valueOf(liTotalReprinted),
					BIA_LENGTH_TOTAL_PRINTED,
					CommonConstant.STR_SPACE_ONE);
			caRpt.print(
				lsTotalPrinted,
				BIA_PRINT_QTY_K_STARTPT - 1,
				BIA_LENGTH_TOTAL_PRINTED);
			caRpt.print(
				lsTotalReprints,
				BIA_REPRNT_QTY_K_STARTPT,
				BIA_LENGTH_TOTAL_PRINTED);
			caRpt.print(
				lsTotalVoided,
				BIA_VOID_QTY_K_STARTPT - 1,
				BIA_LENGTH_TOTAL_PRINTED);
		}
		else
		{
			lvTable.remove(CommonConstant.ELEMENT_1);
			genNoRecordsToReport(lvHeader, lvTable);
		}
		// Close out the report.  This is the last section.

		// defect 8628 
		//generateEndOfReport();
		//generateFooter();
		generateFooter(true);
		// end defect 8628 
	}

	/**
	 * Create the column headers for Part K.
	 * 
	 * @return Vector
	 */
	public Vector partKHeader()
	{
		Vector lvTable = new Vector();
		Vector lvRow_1 = new Vector();
		Vector lvRow_2 = new Vector();
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(
				BIA_PARTK_HEADER,
				0,
				BIA_PARTK_HEADER.length());
		//Alignment of column headers to row 0
		lvRow_1.add(laColumn_1_1);

		//Column Headers for Row1
		// Use RSPSID vs. ORIGIN/ORIGIN ID
		ColumnHeader laColumn_2_1 =
			new ColumnHeader(
				RSPS_ID_K,
				BIA_RSPS_WSID_K_STARTPT,
				RSPS_ID_K.length());
		ColumnHeader laColumn_2_2 =
			new ColumnHeader(
				BIA_ITEM_DESCRIPTION_JK,
				BIA_ITEM_DESC_K_STARTPT,
				BIA_ITEM_DESCRIPTION_JK.length());
		ColumnHeader laColumn_2_3 =
			new ColumnHeader(
				BIA_ITEM_YEAR_JK,
				BIA_ITEM_YEAR_K_STARTPT,
				BIA_ITEM_YEAR_JK.length());
		ColumnHeader laColumn_2_4 =
			new ColumnHeader(
				BIA_DISK_K_NO,
				BIA_DISK_NO_K_STARTPT,
				BIA_DISK_K_NO.length());

		// Include Plate NO
		ColumnHeader laColumn_2_5 =
			new ColumnHeader(
				BIA_PLATE_NO_K,
				BIA_PLATE_NO_K_STARTPT,
				BIA_PLATE_NO_K.length());
		ColumnHeader laColumn_2_6 =
			new ColumnHeader(
				BIA_VIN,
				BIA_VIN_K_STARTPT,
				BIA_VIN.length());
		ColumnHeader laColumn_2_7 =
			new ColumnHeader(
				BIA_PRINTED_K,
				BIA_PRINT_QTY_K_STARTPT,
				BIA_PRINTED_K.length());
		ColumnHeader laColumn_2_8 =
			new ColumnHeader(
				BIA_REPRINTED_K,
				BIA_REPRNT_QTY_K_STARTPT,
				BIA_REPRINTED_K.length());
		ColumnHeader laColumn_2_9 =
			new ColumnHeader(
				BIA_VOIDED,
				BIA_VOID_QTY_K_STARTPT,
				BIA_VOIDED.length());
		//Alignment of lColumn headers to row 1
		lvRow_2.add(laColumn_2_1);
		lvRow_2.add(laColumn_2_2);
		lvRow_2.add(laColumn_2_3);
		lvRow_2.add(laColumn_2_4);
		lvRow_2.add(laColumn_2_5);
		lvRow_2.add(laColumn_2_6);
		lvRow_2.add(laColumn_2_7);
		lvRow_2.add(laColumn_2_8);
		lvRow_2.add(laColumn_2_9);
		//Adding ColumnHeader Information
		lvTable.addElement(lvRow_1);
		lvTable.addElement(lvRow_2);
		return lvTable;
	}

	/**
	 * Closes out page and starts a new one.
	 * 
	 * @param avHeader Vector
	 * @param avTable Vector
	 */
	private void printNewPage(Vector avHeader, Vector avTable)
	{
		// if the curent page is up to the page break point,
		// take the break. 
		generateFooter();
		generateHeader(avHeader, avTable);
		// let the class know a new page has just been done
		cbNewPageDone = true;
		ciDetailLinesUsed = this.caRpt.getCurrX();
	}

	/**
	 * This is used to create dummy data.
	 * This method id not used in this class.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvData = new Vector();
		InventoryDeleteHistoryReportData laDataline =
			new InventoryDeleteHistoryReportData();
		laDataline.setItmCd(SAMPLE_ITEM_CODE);
		laDataline.setItmCdDesc(SAMPLE_ITEM_CODE_DESC);
		laDataline.setInvItmYr(SAMPLE_ITEM_YEAR);
		laDataline.setInvQty(SAMPLE_INV_QTY);
		laDataline.setInvItmNo(SAMPLE_ITM_NO);
		laDataline.setInvEndNo(SAMPLE_ITM_END_NO);
		laDataline.setTransEmpId(SAMPLE_EMP_ID);
		laDataline.setTransAMDate(SAMPLE_TRANS_AM_DATE);
		laDataline.setOfcIssuanceNo(SAMPLE_OFC_NO);
		laDataline.setTransWsId(SAMPLE_WS_ID);
		laDataline.setTransAMDate(SAMPLE_TRANS_AM_DATE);
		laDataline.setTransTime(SAMPLE_TRANS_TIME);
		lvData.addElement(laDataline);
		return lvData;
	}
}
