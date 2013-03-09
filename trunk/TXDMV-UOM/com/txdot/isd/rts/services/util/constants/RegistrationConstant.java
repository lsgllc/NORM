package com.txdot.isd.rts.services.util.constants;


/*
 *
 * RegistrationConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/18/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class constants 
 * 							 - CHECK_DISK_INVENTORY 
 *							 - ADD_DISK_TRANS_AND_END_TRANS
 *							 - COPY_AND_VALIDATE_SUBCON_DISKETTE
 *							 - RELEASE_INV014_ITM 
 *							Removed PROCESS_ISSUE_INV 
 * 							Ver 5.2.0
 * K Harrell	03/02/2005	INV014 renamed to INV003 
 * 							defect 6966 Ver 5.2.3 
 * B Hargrove	08/22/2005	Add constant for max Vehicle Weight (80000). 
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	10/31/2007	add MIN_SUBCON_MONTHS, MAX_SUBCON_MONTHS
 * 							defect 9097 Special Plates 2
 * J Rue		05/06/2008	Add 'NOPLATE'
 * 							defect 9630 Ver Defect_POS_A 
 * J Rue		06/12/2008	Change Ver to Defect_POS_A
 * 							defect 9630 Ver Defect_POS_A
 * K Harrell	09/02/2008	modify OUT_OF_COUNTY (to upper case) 
 * 							defect 7860 
 * B Hargrove	06/01/2009  Add Flashdrive option to RSPS Subcon. 
 * 				 	 		modify / rename COPY_AND_VALIDATE_SUBCON_DISKETTE
 * 							to COPY_AND_VALIDATE_SUBCON_MEDIA
 * 							defect 10064 Ver Defect_POS_F
 * K Harrell	07/03/2009	add APPR 
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	04/20/2010	add ATV_REGCLASSCD
 * 							defect 10453 Ver POS_640 
 * K Harrell	04/26/2010	add ROV_REGCLASSCD
 * 							defect 10453 Ver POS_640
 * K Harrell	06/19/2010	add labels for Renewal Notification 
 * 							Panels, Radio Buttons, etc. 
 * 							add PAPER_RENWL_NOTFN, EMAIL_RENWL_NOTFN,
 * 							 BOTH_RENWL_NOTFN, RENWL_NOTFN_CDS, 
 * 							 PAPER_RENWL_NOTFN_RADIO_LABEL,
 * 							 EMAIL_RENWL_NOTFN_RADIO_LABEL,
 * 							 BOTH_RENWL_NOTFN_RADIO_LABEL,
 * 							 RENWL_NOTFN_PANEL_LABEL 
 * 							defect 10508 Ver 6.5.0
 * K Harrell	07/10/2010	add label for E-Reminder; comment out 6/19 
 * 							additions; saving for future implementation.  
 * 							add EREMINDER, EREMINDER_CHKBX_LABEL
 * 							defect 10508 Ver 6.5.0  
 * B Hargrove	10/11/2010	Add Token Trailer RegClassCd constant. 
 * 							add REGCLASSCD_TOKEN_TRLR
 * 							defect 10623 Ver 6.6.0  
 * K Harrell	03/22/2011	add KEYED_KEYTYPECD, SCAN_KEYTYPECD, 
 * 							GRACE_EXPPROCSNGCD, EXPIRED_EXPPROCSNGCD, 
 * 							NONE_EXPPROCSNGCD, OPEN_BATCHSTATUSCD, 
 * 							CLOSE_BATCHSTATUSCD  
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	04/04/2011	add SUBMIT_BATCHSTATUSCD,
 * 							  INPROCESS_BATCHSTATUSCD,
 * 							  APPROVED_BATCHSTATUSCD  
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	04/06/2011	add WEB_AGNT_MAX_BATCH_ROWS,
 *							  WEB_AGNT_MAX_SEARCH_RANGE_DIFF, 
 *							  BATCHES_ENTRY, MAXREACHED_ENTRY, 
 *							  COUNTY_AGNCYTYPECD 
 *							defect 10768 Ver 6.7.1  
 * K Harrell	10/15/2011	add REGCLASSCD_TITLE_ONLY, 
 *							 REGCLASSCD_TRAVEL_TRAILER
 *							defect 11049 Ver 6.9.0 
 * K Harrell	10/24/2011	add EXP_REG_CITATION, EXP_REG_INVALID_REASON,
 * 							EXP_REG_VALID_REASON
 * 							defect 11126 Ver 6.9.0 
 * K Harrell	11/05/2011	add OWNR_ADDR_TYPE_CD,RCPNT_ADDR_TYPE_CD
 * 							defect 11137 Ver 6.9.0 
 * K McKee      06/11/2012  modify WEB_AGNT_MAX_BATCH_ROWS from 200 to 1000
 * 							defect 11372 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
 * Registration constants class is used to define the Registration
 * Client Business methods.
 * 
 * @version	7.0.0  			06/11/2012
 * @author	Joseph Kwik
 * @since 					08/29/2001
 */
public class RegistrationConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int INIT_REG003 = 1;
	public final static int INIT_ADDL_INFO = 2;
	//Registration Modify Constants
	public final static int REG_MODIFY_VOLUNTARY = 10;
	public final static int REG_MODIFY_APPREHENDED = 11;
	public final static int REG_MODIFY_REG = 12;
	//Subcontractor Constants
	public final static int GET_SUBCON_ALLOCATED_INV = 30; //for REG006
	public final static int INIT_SUBCON_RENWL = 31; //for REG007	
	public final static int GET_NEXT_INV = 32; //for REG007
	public final static int VALIDATE_PLT = 33;
	public final static int VALIDATE_STKR = 34;
	public final static int RECAL_SUBCON_FEES = 35;
	public final static int PROCESS_SUBCON_RENWL = 36;
	public final static int RESTORE_SUBCON_BUNDLE = 37;
	//REG006 redirect
	public final static int HOLD_INV003_ITM = 38;
	public final static int CANCEL_SUBCON = 39;
	public final static int CANCEL_HELD_SUBCON = 40;
	public final static int GENERATE_SUBCON_REPORT_DRAFT = 41;
	public final static int GENERATE_SUBCON_REPORT_FINAL = 45;
	public final static int DEL_SELECTED_SUBCON_RENWL_RECORD = 42;
	public final static int PROC_COMPLETE_SUBCON_RENWL = 43;
	public final static int MODIFY_SUBCON_RENWL_RECORD = 44;
	public final static int RELEASE_HELD_INV = 45;
	// PCR 34
	public final static int CHECK_DISK_INVENTORY = 46;
	public final static int ADD_DISK_TRANS_AND_END_TRANS = 47;
	public final static int PROCESS_ISSUE_INV = 54;
	// defect 10064
	//public final static int COPY_AND_VALIDATE_SUBCON_DISKETTE = 55;
	public final static int COPY_AND_VALIDATE_SUBCON_MEDIA = 55;
	// end defect 10064	
	public final static int RELEASE_INV003_ITM = 56;
	// End PCR 34

	// defect 7860 
	// Make upper case for consistency 
	public final static String OUT_OF_COUNTY = "OUT OF COUNTY";
	// end defect 7860 

	// defect 7885
	public final static int MAX_VEH_WEIGHT = 80000;
	// end defect 7885

	// defect 9097 
	public final static int MIN_SUBCON_MONTHS = 6;
	public final static int MAX_SUBCON_MONTHS = 15;
	// end defect 9097 

	// defect 9630
	public final static String NOPLATE = "NOPLATE";
	// end defect 9630

	// defect 10112 
	public final static String APPR = "APPR";
	// end defect 10112 

	// defect 10453 
	public final static int ATV_REGCLASSCD = 7;
	public final static int ROV_REGCLASSCD = 92;
	// end defect 10453 
	// defect 10623 
	public final static int REGCLASSCD_TOKEN_TRLR = 33;
	// end defect 10623 

	// defect 11049 
	public final static int REGCLASSCD_TITLE_ONLY = 99; 
	public final static int REGCLASSCD_TRAVEL_TRAILER = 38;
	// end defect 11049 
	
	// defect 10508
	public final static int EREMINDER = 1;
	public final static String EREMINDER_CHKBX_LABEL = "E-Reminder";

	// TODO (KPH) 
	// Saving for possible future implementation  	 
	//	public final static int PAPER_RENWL_NOTFN = 0;
	//	public final static int EMAIL_RENWL_NOTFN = 1;
	//	public final static int BOTH_RENWL_NOTFN = 2;
	//	
	//	public static final Hashtable RENWL_NOTFN_CDS = new Hashtable();
	//
	//	static {
	//		RENWL_NOTFN_CDS.put(new Integer(PAPER_RENWL_NOTFN), "Paper");
	//		RENWL_NOTFN_CDS.put(new Integer(EMAIL_RENWL_NOTFN), "E-Mail");
	//		RENWL_NOTFN_CDS.put(
	//			new Integer(BOTH_RENWL_NOTFN),
	//			"Paper & E-Mail");
	//	}
	//	public final static String PAPER_RENWL_NOTFN_RADIO_LABEL = "Paper";
	//	public final static String EMAIL_RENWL_NOTFN_RADIO_LABEL = "E-Mail";
	//	public final static String BOTH_RENWL_NOTFN_RADIO_LABEL = "Both";
	//	public final static String RENWL_NOTFN_PANEL_LABEL =
	//		"Renewal Notification:";
	// end defect 10508  
	
	// defect 10768 
	public final static String KEYED_KEYTYPECD = "K";
	public final static String SCAN_KEYTYPECD = "S";
	
	public final static String EXPIRED_EXPPROCSNGCD = "E";
	public final static String GRACE_EXPPROCSNGCD = "G"; 
	public final static String NONE_EXPPROCSNGCD = "N"; 
	
	public final static String OPEN_BATCHSTATUSCD = "O";  
	public final static String CLOSE_BATCHSTATUSCD = "C";
	public final static String SUBMIT_BATCHSTATUSCD = "S";
	public final static String INPROCESS_BATCHSTATUSCD = "I";
	public final static String APPROVED_BATCHSTATUSCD = "A";
	// end defect 10768
	
	// defect 10768 
	public final static int WEB_AGNT_MAX_SEARCH_RANGE_DIFF = 30;
	// defect 11372
	public final static int WEB_AGNT_MAX_BATCH_ROWS = 1000;
	// end defect 11372
	public final static String BATCHES_ENTRY = "BATCHES";
	public final static String MAXREACHED_ENTRY = "MAXREACHED";
	public final static String TXDMVHQ_AGNCYTYPECD = "1"; 
	public final static String REGIONALSC_AGNCYTYPECD = "2"; 
	public final static String COUNTY_AGNCYTYPECD = "3"; 
	// end defect 10768
	

	// defect 11126
	public final static int EXP_REG_CITATION = 3;
	public final static int EXP_REG_INVALID_REASON = 2;
	public final static int EXP_REG_VALID_REASON = 1;
	// end defect 11126 
	
	// defect 11137 
	public final static String OWNR_ADDR_TYPE_CD  = "O"; 
	public final static String RCPNT_ADDR_TYPE_CD  = "R";
	// end defect 11137 
}
