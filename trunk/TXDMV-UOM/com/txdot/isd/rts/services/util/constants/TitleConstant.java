package com.txdot.isd.rts.services.util.constants;

/*
 *
 * TitleConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/25/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class constant 
 * 							STICKER_RECEIPT_REPORT
 * 							Ver 5.2.0
 * K Harrell	07/05/2005	delete DEALER_DATA
 * 							defect 8283 Ver 5.2.3 			 
 * T Pederson	09/08/2006	Added GET_PRIVATE_PARTY_VALUE constant 
 * 							defect 8926 Ver 5.2.5
 * K Harrell	04/25/2008	add OWNR_RETAINED_PREFIX
 * 							defect 9634 Ver Defect_POS_A
 * K Harrell	06/09/2008	add MAX_POS_DEALERID, REQD_MF_OWNERID_LENGTH
 * 							defect 9635 Ver Defect_POS_A 
 * K Harrell	03/04/2009	add NON_NEGOTIABLE_ETTLCD,
 * 							NEGOTIABLE_PAPER_ETTLCD,ELECTRONIC_ETTLCD
 * 							defect 9969 Ver Defect_POS_E
 * B Hargrove	03/12/2009	Add column constants for DTA007 Dealer
 * 							Preliminary table.
 * 							add DTA007_COL_PRINTED, DTA007_COL_PROCESSED, 
 * 							DTA007_COL_ETITLE, DTA007_COL_FORM31, 
 * 							DTA007_COL_FEES
 * 							defect 9977 Ver Defect_POS_E
 * B Hargrove	05/22/2009  Add Flashdrive option to DTA. 
 * 				 	 		add DATA_FROM_FLASHDRIVE
 * 							defect 10075 Ver Defect_POS_F 
 * K Harrell	06/22/2009	add TTLPROCSCD_REJECTED 
 * 							defect 10023 Ver Defect_POS_F 
 * K Harrell	07/06/2009	add LIENHLDR1, LIENHLDR2,
 * 							 LIENHLDR3
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	12/15/2009	add DTA_MAX_DEALER_ID_LENGTH,
 * 							 DTA_MAX_DEALER_SEQNO_LENGTH, 
 * 							 DTA_BATCH_NO_LENGTH,
 * 							 DTA_MAX_RECORDS, DTA_SKIP_LABEL
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	10/04/2010	add  ETITLE_SYMBOL
 * 							defect 10598 Ver 6.6.0
 * K Harrell	10/22/2010	add CCO_COMPARE_DAYS
 * 							defect 10639 Ver 6.6.0  
 * K Harrell	05/29/2011	add FRAUDCD_MGMT
 * 							defect 10865 Ver 6.8.0 
 * K Harrell	10/15/2011	add STATE_CNTRY_MAX_LEN,
 * 							 REQUIRED, NOT_REQUIRED
 * 							defect 11004 Ver 6.9.0 
 *---------------------------------------------------------------
*/
/**
 * Constants for function id of title classes
 * 
 * @version	6.9.0			10/15/2011
 * @author	Todd Pederson
 * <br>Creation Date:		08/30/2001 
 */
public class TitleConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int GET_DLR_DATA_ONID = 1;
	public final static int GET_DLR_TTL_DATA_FROM_HARDDRIVE = 2;
	public final static int IS_VEH_ALLOWED_TO_BE_TITLED = 3;
	public final static int VALIDATION002 = 4;
	public final static int LIENHOLDER_DATA = 5;
	public final static int GET_OWNR_EVID_CDS = 6;
	public final static int DATA_FROM_DISK = 7;
	// defect 10075
	public final static int DATA_FROM_FLASHDRIVE = 8;
	// end defect 10075
	public final static int GET_NUM_DOC_RECORD = 9;
	public final static int GENERATE_PRELIMINARY_DEALER_REPORT = 20;
	public final static int GENERATE_CCO_DOCUMENT = 21;
	public final static int GENERATE_COA_DOCUMENT = 22;
	public final static int DELETE_TITLE_IN_PROCESS = 23;
	public final static int PROCS_KEYBRD = 24;
	public final static int TTL_IN_PROCESS = 25;
	public final static int GENERATE_STICKER_RECEIPT_REPORT = 26;
	public final static int SALVAGE = 27;
	// defect 8926
	public final static int GET_PRIVATE_PARTY_VALUE = 28;
	// end defect 8926
	
	// defect 10865 
	public final static int FRAUDCD_MGMT = 29;
	// end defect 10865  

	public final static String NOT_REBUILT = "0";
	public final static String SALVAGE_LOSS_UNKNOWN = "1";
	public final static String REBUILT_SALVAGE_75_94 = "2";
	public final static String REBUILT_SALVAGE_95_PLUS = "3";
	public final static String REBUILT_SALVAGE_ISSUED = "4";

	// defect 9634 
	public final static String OWNR_RETAINED_PREFIX = "OR";
	// end defect 9634 

	// defect 9635 
	public final static int MAX_POS_DEALERID = 999;
	public final static int REQD_MF_OWNERID_LENGTH = 9;
	// end defect 9635  

	// defect 9969 
	public final static int NON_NEGOTIABLE_ETTLCD = 1;
	public final static int NEGOTIABLE_PAPER_ETTLCD = 2;
	public final static int ELECTRONIC_ETTLCD = 3;
	// end defect 9969

	// defect 9977 
	public final static int DTA007_COL_PRINTED = 0;
	public final static int DTA007_COL_PROCESSED = 1;
	public final static int DTA007_COL_ETITLE = 2;
	public final static int DTA007_COL_FORM31 = 3;
	public final static int DTA007_COL_FEES = 4;
	// end defect 9977

	// defect 10023 
	public final static String TTLPROCSCD_REJECTED = "R";
	// end defect 10023 

	// defect 10112 
	public final static Integer LIENHLDR1 = new Integer(1);
	public final static Integer LIENHLDR2 = new Integer(2);
	public final static Integer LIENHLDR3 = new Integer(3);
	// end defect 10112 
	
	// defect 10290
	public final static int DTA_MAX_DEALER_ID_LENGTH = 3;
	public final static int DTA_MAX_DEALER_SEQNO_LENGTH = 3; 
	public final static int DTA_BATCH_NO_LENGTH = 7;
	public final static int DTA_MAX_RECORDS = 25; 
	public final static String DTA_SKIP_LABEL = "*** SKIP ***";
	// end defect 10290 
	
	//	defect 10598 
	 public final static String ETITLE_SYMBOL = "E"; 
	 // end defect 10598 

	// defect 10639 
	public final static int CCO_COMPARE_DAYS = 30; 
	// end defect 10639 

	// defect 11004
	public final static int STATE_CNTRY_MAX_LEN = 2;
	public final static boolean REQUIRED = true; 
	public final static boolean NOT_REQUIRED = false;

	// end defect 11004
	
}