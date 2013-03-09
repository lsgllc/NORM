package com.txdot.isd.rts.services.util.constants;

/*
 *
 * MiscellaneousConstant
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/07/2002	Add GET_TRANS_FOR_VOID_INDI.
 * 							defect 4746. 
 * K Harrell	01/25/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class variables. 
 * 							(Renumbered from 22 - 26 to 23 - 27)
 * 							 - REPRINT_ONE_RECEIPT 
 *							 - GET_LAST_CSN 
 *							 - OVERRIDE_NEEDED
 *							 - UPDATE_REPRINT_STICKER
 *							 - GET_TRANS_FOR_VOID_INDI
 * 							Ver 5.2.0
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3	
 * K Harrell	09/16/2008	add OVERRIDE_MSG_DEALER_RECEIPT,
 * 							 OVERRIDE_MSG_SUBCON_RECEIPT, 
 * 							 OVERRIDE_MSG_SALVAGE_TYPE_RECEIPT,
 *							 OVERRIDE_MSG_NOT_LAST_RECEIPT,
 *							 MAP_ENTRY_SHOW, MAP_ENTRY_DATA,
 *							 MAP_ENTRY_CASHRECEIPT,MAP_ENTRY_MSG
 *							defect 7283 Ver Defect_POS_B 
 * K Harrell	06/09/2010	add GET_VOID_PRMT_DATA
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	01/29/2011	add CUS001_COL_CUSTSEQNO, 
 * 							  CUS001_COL_TRANSNAME
 * 							defect 10734 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */
/**
 * Constants for function ids of Miscellaneous Module
 * 
 * @version	6.7.0  			01/29/2011
 * @author	Bobby Tulsiani
 * <br>Creation Date:		08/27/2001 17:03:59
 */
public class MiscellaneousConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int GET_PENDING_TRANSACTIONS = 10;
	public final static int COMPLETE_TRANSACTION = 11;
	public final static int GET_VOID_TRANSACTION = 12;
	public final static int VOID_TRANSACTION = 13;
	public final static int GET_MV_FUNC_TRANS = 14;
	public final static int GET_VOID_RTS_TRANS = 15;
	public final static int COMPLETE_PENDING_TRANS = 16;
	public final static int PRINT_RECEIPTS = 17;
	public final static int SET_VOID_INDI = 18;
	public final static int QRY_VOID_TRANS_HDR = 19;
	public final static int GET_VOID_MVF_DATA = 20;
	public final static int CHECK_MF_TRANS = 21;
	public final static int GET_TRANS_FOR_VOID_INDI = 22;
	public final static int REPRINT_ONE_RECEIPT = 23;
	public final static int GET_LAST_CSN = 24;
	public final static int OVERRIDE_NEEDED = 25;
	public final static int UPDATE_REPRINT_STICKER = 26;

	// defect 10491 
	public final static int GET_VOID_PRMT_DATA = 27;
	// end defect 10491 

	// defect 7283
	public final static String OVERRIDE_MSG_DEALER_RECEIPT =
		"DEALER TITLE RECEIPT / REPORT";
	public final static String OVERRIDE_MSG_SUBCON_RECEIPT =
		"SUBCONTRACTOR RENEWAL RECEIPT";
	public final static String OVERRIDE_MSG_SALVAGE_TYPE_RECEIPT =
		"SALVAGE TYPE RECEIPT";
	public final static String OVERRIDE_MSG_NOT_LAST_RECEIPT =
		"NOT LAST RECEIPT";
	public final static String MAP_ENTRY_SHOW = "SHOW";
	public final static String MAP_ENTRY_DATA = "DATA";
	public final static String MAP_ENTRY_MSG = "MSG";
	public final static String MAP_ENTRY_CASHRECEIPT = "CASHRECEIPT";
	// end defect 7283 

	// defect 10734
	public final static int CUS001_COL_CUSTSEQNO = 0;
	public final static int CUS001_COL_TRANSNAME = 1;
	// end defect 10734 

}
