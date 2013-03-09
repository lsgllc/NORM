package com.txdot.isd.rts.services.util.constants;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * CommonConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/26/2001	Added ADD_TRANS, END_TRANS, POST_TRANS
 * MAbs/TP		06/05/2002	MultiRecs in Archive CQU100004019
 * Ray Rowehl	10/06/2002	Add constants for Void Processing
 * 							Defect CQU100004745
 * K Harrell	03/21/2004	5.2.0 Merge.
 * 							Added constants. 
 * 							See below those annotations with PCR 34
 * 							Ver 5.2.0
 * K Harrell	10/10/2004	Rename BlackBox variables => RSPS
 *							defect 7608  Ver 5.2.1
 * K Harrell	11/12/2004	Rename WRITE_INV_TO_CACHE to
 *							 ADD_TRANS_IRENEW
 *							defect 6720  Ver 5.2.2
 * K Harrell	05/02/2005	Rename INV014 to INV003
 * 							defect 6966 Ver 5.2.3 
 * Ray Rowehl	07/09/2005	Add some commonly used constants for 
 * 							use through out the system.
 * 							add STR_DASH, STR_SPACE_EMPTY, STR_SPACE_ONE
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	07/12/2005	Add Vector element constants
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	07/30/2005	Add commonly used constant for 
 * 							use through out the system.
 * 							add STR_ZERO, several elements
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/06/2005	Add commonly used constants
 * 							add NOT_FOUND, STR_SLASH, 
 * 							SYSTEM_LINE_SEPARATOR
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/08/2005	Add commonly used constants
 * 							add STR_COLON, STR_COMMA
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/16/2005	Move in some common constants from
 * 							Local Options.
 * 							defect 7891 Ver 5.2.3
 * J Rue		08/17/2005	Add USA, TX constant
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	08/18/2005	Add common constants from Inventory.
 * 							Setup text for Buttons.
 * 							defect 7890 Ver 5.2.3
 * J Rue		08/17/2005	Add ERROR constant
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	08/23/2005	Adding constants from CommonServerBusiness.
 * 							defect 7885 Ver 5.2.3
 * J Rue		08/23/2005	Move Title Frame constants over to here
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/25/2005	Add "null" constant
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/30/2005	Set line.separator as parameter for
 * 							System.getproperty(String)
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		10/06/2005	Add constant for the number of days to
 * 							to keep in the log file when you prune.
 * 							add PRUNE_LOG_FILE_DAYS
 * 							defect 8366 Ver. 5.2.3
 * J Rue		11/02/2005	Add constant ", "
 * 							add STR_COMMA_SPACE
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		12/01/2005	Add constant "="
 * 							add STR_EQUAL
 * 							defect 8442 Ver 5.2.2 Fix 7
 * Min Wang		10/02/2006  Add constant '-'
 * 							add CHAR_DASH
 * 							defect 8938 Ver FallAdminTables
 * B Hargrove	09/28/2006	Add constant 4 (FeeCalcCat = 4 = No Regis Fees)
 * 							add NOREGISFEES
 * 							defect 8900 Ver 5.3.0
 * K Harrell	09/28/2006	Add STR_DOUBLE_QUOTE, RTS_TBCREATOR,
 * 							PILOT_TBCREATOR
 * 							defect 8900 Ver 5.3.0 
 * K Harrell	10/07/2006	Add constants for DB calls involving 
 * 							Exempt Audit table.
 * 							add MSG_DB_CALL_EXMPTAUDIT_START,
 * 							    MSG_DB_CALL_EXMPTAUDIT_SUCCESS =
 * 							defect 8900 Ver 5.3.0 
 * T Pederson	10/09/2006	Add ZERO_DOLLAR  
 * 							defect 8900 Ver Exempts
 * Jeff S.		10/12/2006	Add STR_POUND
 * 							defect 8900 Ver Exempts
 * J Ralph		10/20/2006  Added TBL_TXT_OFFICEISSNO,
 * 								  TBL_TXT_OFFICENAME, TBL_TXT_OFFICENO,
 * 								  TXT_EXPORT, TXT_PERIOD_CSV, TXT_REPORT,
 * 								  TXT_SAVE_IN_COLON_SPACE, TXT_SELECT_ALL,
 * 								  TXT_SELECT_ONE_COLON
 * 							defect 8900 Ver Exempts
 * J Ralph		10/25/2006	Added STR_QUESTION_MARK, TXT_OVERWRITE
 * 							defect 8900 Ver Exempts
 * Jeff S.		04/02/2007	Add constant "@"
 * 							add STR_AT
 * 							defect 7768 Ver Broadcast Message
 * J rue		01/23/2007	Add SPCLREGID
 * 							defect 9086 Ver Special Plates Full
 * K Harrell	02/06/2007	Added MSG_DB_CALL_SR_FUNC_START,
 *							  MSG_DB_CALL_SR_FUNC_SUCCESS, 
 *							  MSG_DB_CALL_SP_TRANS_HSTRY_START,
 *							  MSG_DB_CALL_SP_TRANS_HSTRY_SUCCESS 
 *							Modified text of several DB Start/Successful
 *							constants to match table. 
 *							defect 9085  Ver Special Plates
 * K Harrell	02/12/2007	Added INET_OFC_CD 
 * 							defect 9085 Ver Special Plates
 * J Rue		02/23/2007	Add SEARCH_SPECIAL_PLATES
 *							defect 9086  Ver Special Plates
 * Jeff S.		03/27/2007	Added new Constants that are used
 * 							mainly by the IVTRS Project.
 * 							defect 9121 Ver Special Plates
 * K Harrell	04/26/2007	add GET_VI_SPCL_PLT_VECTOR,
 *							 SAVE_VI_SPCL_PLT_VECTOR,
 *							 GET_SAVED_SPCL_PLT, SAVE_SPCL_PLT,
 *							 COMPLETE_TRANS_QUESTION
 *							defect 9085 Ver Special Plates
 * K Harrell	05/21/2007	add PROC_IADDR = 46; 
 *							defect 9085 Ver Special Plates 
 * K Harrell	06/05/2007	Renamed xxx_MFG_SPCL_PLT_VECTOR to 
 * 							xxx_VI_SPCL_PLT_VECTOR 
 * 							defect 9085 Ver Special Plates 
 * Jeff S.		07/05/2007	Added new Constants.
 * 							add STR_BACK_SLASH
 * 							defect 9121 Ver Special Plates
 * K Harrell	10/16/2007	add PASSENGER_PLT, TRUCK_PLT,
 * 							 PASS_LE_6000_REGCLASSCD,
 * 							 TRK_LE_1_TON_REGCLASSCD 
 * 							defect 9362 Ver Special Plates 2
 * B Hargrove	11/20/2007	Added new Constants.
 * 							add TONLY_REGCLASSCD, TONLY_REGPLTCD
 * 							defect 9337 Ver Special Plates 2
 * Min Wang		12/04/2007	Added new constants.
 * 							add NOT_REQUIRED, VERIFIED, VERIFY_MANUALLY
 * 							defect 9459 Ver FRVP
 * Ray Rowehl	01/18/2008	Added new constants for V21 responses
 * 							defect 9502 Ver 3_Amigos_PH_A
 * B Hargrove	01/31/2008	Added new constants for V21VTN
 * 							(Vision 21 Vehicle Transaction Notification)
 * 							defect 9502 Ver 3_Amigos_PH_A
 * B Hargrove	02/07/2008	Added new constants for V21PLD
 * 							(Vision 21 Plate Disposition)
 * 							defect 9502 Ver 3_Amigos_PH_A
 *  J Rue		03/31/2008	Add DLRGDN_MAX_LENGTH = 10
 * 							defect 9585 Ver 3_Amigos_PH_B
 * K Harrell	04/22/2008	Add Constants for Plate Disposition processing
 * 							add PLT_DISPOSITION_UNCHANGED, 
 *							 PLT_WITH_OWNER, PLT_WITH_VEHICLE,
 *							 PLT_DISPOSED
 * 							defect 9582 Ver 3_Amigos_PH_B
 * B Brown		06/17/2008	Added new constant to process MyPlates
 * 							transcd's VPAPPL and VPAPPR.
 * 							add PROC_VENDOR_PLATES
 * 							defect 9711 Ver MyPlates_POS.	
 * K Harrell	09/02/2008	Changed Txt for Admin Log to mixed case.	
 * 							Modify TXT_ADMIN_LOG_ADD, 
 * 							  TXT_ADMIN_LOG_DELETE, TXT_ADMIN_LOG_REVISE
 * 							defect 8595 Ver Defect_POS_B 
 * 							defect 9711 Ver MyPlates_POS.
 * Min Wang		09/02/2008	display whole words for verified and 
 * 							verify manually.	
 * 							modify VERIFIED, VERIFY_MANUALLY
 * 							defect 9647 Ver Defect_POS_B
 * K Harrell	10/21/2008	add MSG_DB_CALL_DSABLD_PLCRD_TRANS_START
 * 							defect 9831 Ver Defect_POS_B	
 * K Harrell	10/22/2008	Add DEFAULT_TRANSEMPID 
 * 							defect 9847 Ver Defect_POS_B 
 * K Harrell	11/01/2008	add MSG_DB_CALL_DSABLD_PLCRD_CUST_START
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	03/05/2009	add LENGTH_CNTRY 
 * 							defect 9971 Ver Defect_POS_E  
 * K Harrell	03/12/2009	add MSG_DB_CALL_ETTL_HSTRY_START	
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	04/01/2009	add MAX_INDI_NO_SCROLL
 * 							defect 9971 Ver Defect_POS_E
 * B Hargrove	06/01/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							DTA_DISK_WRITE \ DTA_MEDIA_WRITE 
 * 							delete DTA_DISK
 * 							defect 10075 Ver Defect_POS_F  
 * B Hargrove	06/11/2009  Add constants for frame table columns.
 * 							add CTL005_COL_MODYR, CTL005_COL_MAKE,
 * 							CTL005_COL_VIN, CTL005_COL_PLTNO,
 * 							CTL005_COL_EXPMO, CTL005_COL_EXPYR,
 * 							CTL005_COL_OWNRNAME, 
 * 							REG002_COL_OWNRNAME, REG002_COL_PLTNO
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	06/15/2009	add BTN_TXT_PREV, BTN_TXT_NEXT, BTN_TXT_LAST,
 * 							 BTN_TXT_FIRST
 * 							defect 10086 Ver Defect_POS_F
 * J Zwiener	06/18/2009	add LENGTH_PLTNO, LENGTH_DOCNO,
 *  						LENGTH_VIN, BOTH_ADDL_AND_CUMUL
 * 							defect 10091 Ver Defect_POS_F
 * K Harrell	07/03/2009	add INQ004REG_COL_MODYR, INQ004REG_COL_MAKE,
 * 							 INQ004REG_COL_VIN, INQ004REG_COL_PLTNO,
 * 							 INQ004REG_COL_EXPMO, INQ004REG_COL_EXPYR,
 * 							 INQ004REG_COL_NAME1,DEFAULT_PRIVACY_OPT_CD
 * 							defect 10117 Ver Defect_POS_F
 * K Harrell	07/19/2009	add MIN_COUNTY_NO, MAX_COUNTY_NO,
 * 							 LENGTH_OWNERID, LENGTH_UNIT_NO,LENGTH_MAKE,
 * 							 TX_DEFAULT_STATE, TX_NOT_DEFAULT_STATE, 
 * 							 LENGTH_PREV_OWNR_CITY, LENGTH_PREV_OWNR_NAME, 
 * 							 TXT_ADDRESS_COLON, TXT_ID_COLON,
 * 							 TXT_PHONE_NO_COLON   
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	08/11/2009	Modifications for new DB2 Driver 
 * 							add DUPLICATE_KEY_EXCEPTION 
 * 							delete TXT_SQL0803
 * 							defect 10164 Ver Defect_POS_E'/F
 * K Harrell	09/15/2009	add SQL_BEGIN, SQL_END, SQL_METHOD_BEGIN,
 * 							 SQL_METHOD_END, SQL_EXCEPTION
 * 							defect 10164 Ver Defect_POS_F
 * K Harrell	10/16/2009	add MAX_NO_ADDRESS_LINES
 * 							defect 10191 Ver Defect_POS_G 
 * Ray Rowehl	10/22/2009	Add Constant for max mfg plate number.
 * 							add LENGTH_MFG_PLTNO_MAX
 * 							defect 10253 Ver Defect_POS_G
 * K Harrell	12/15/2009	add DTA_ADD_TRANS
 * 							add INV007_COL_YEAR, INV007_COL_ITMDESC,
 *							 INV007_COL_ITMNO
 * 							delete DTA_MEDIA_WRITE
 *   						defect 10290 Ver Defect_POS_H
 * Min Wang		01/04/2010  Add constant for tire type.
 * 							add TIRE_TYPE_PNEUMATIC, 
 * 								TIRE_TYPE_SOLID
 * 							defect 10317 Ver Defect_POS_H
 * K Harrell	01/25/2010	add KEY008_NO_PLT_FLG 
 * 							defect 10339 Ver Defect_POS_H
 * K Harrell	02/16/2010	add LENGTH_EMAIL, TXT_EMAIL
 * 							defect 10372 Ver POS_640 
 * K Harrell	03/08/2010	add TXT_ADMIN_LOG_SUBSTA_SUMMARY,
 *							 TXT_ADMIN_LOG_COUNTY_WIDE,
 *							 TXT_ADMIN_LOG_RERUN_REPORT, 
 *							 TXT_CERTFD_LIENHLDR
 *							defect 10168 Ver POS_640 
 * Ray Rowehl	04/19/2010	Add constants for Trans Web Services.
 * 							add DBL_PYMNTAMT_VALUE_MAX, 
 * 							INT_ITRNTTRACENO_LENGTH_MAX, 
 * 							INT_ORGNO_LENGTH_MAX, 
 * 							INT_PYMNTORDERID_LNGTH_MAX, 
 * 							INT_PYMNTORDERID_LNGTH_MAX,
 * 							L_SPCLREGID_VALUE_MAX, STR_CALLER_MC,
 * 							STR_CITY_MC, STR_ITRNTTRACENO_MC, 
 * 							STR_MFGPLTNO_MC, STR_MISSING_MC, STR_ORGNO_MC, 
 * 							STR_PLTCD_MC, STR_PLTEXPMO_MC, STR_PLTEXPYR_MC,
 * 							STR_PLTOWNRNAME1_MC, STR_PLTOWNRNAME2_MC, 
 * 							STR_PLTOWNRPHONE_MC, STR_PLTNO_MC, 
 * 							STR_PLTVALIDITYTERM_MC, STR_PYMNTAMT_MC,
 * 							STR_PYMNTORDERID_MC, STR_RESERVREASNCD_MC,
 * 							STR_RESCOMPTCNTYNO_MC, STR_SESSIONID_MC,
 *   						STR_STATE_MC, STR_STREETLINE1_MC, 
 * 							STR_STREETLINE2_MC, STR_TXT_EMPTY,
 * 							STR_ZIPP4_MC, STR_ZIP_MC 
 * 							defect 10401 Ver 6.4.0
 * K Harrell	05/24/2010	add PRMT_PRMTNO, PRMT_VIN, PRMT_LSTNAME,
 *    						PRMT_BSNNAME, PRMT_PRMTID, INQ004PRMT_COL_MODYR, 
 * 							INQ004PRMT_COL_MAKE, INQ004PRMT_COL_VIN,
 * 							INQ004PRMT_COL_PRMTNO,INQ004PRMT_COL_PRMTTYPE,
 * 				  			INQ004PRMT_COL_EXPMOYR, INQ004PRMT_COL_NAME, 
 * 							MSG_DB_CALL_PRMT_TRANS_START,
 * 							MSG_DB_CALL_PRMT_TRANS_SUCCESS, 
 * 							GET_VI_PRMT_VECTOR, SAVE_VI_PRMT_VECTOR, 
 * 							SEARCH_PERMIT,LENGTH_BUSINESS_NAME, 
 * 							LENGTH_FIRST_NAME, LENGTH_MI_NAME, 
 * 							LENGTH_LAST_NAME, LENGTH_PERMIT_NO, 
 * 							LENGTH_OTPT_PT   
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	06/14/2010  add LENGTH_CUSTSEQNO
 * 							defect 10505 Ver 6.5.0
 * K Harrell	07/14/2010	add OFCISSUANCECD_HQ, OFCISSUANCECD_REGION,
 * 							 OFCISSUANCECD_COUNTY 
 *							defect 10491 Ver 6.5.0 
 * K Harrell	07/18/2010	add LENGTH_VIN_MAX 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	09/22/2010	add INQ002_COL_TRANSID, INQ002_COL_DATE,
 * 								INQ002_COL_TRANSCDDESC,
 * 								INQ002_COL_REGPLTNO,
 * 								INQ002_COL_EMPID, INQ002_COL_OFCNAME
 * 							defect 10598 Ver 6.6.0  
 * B Brown		12/08/2010	add PROC_EREMINDER
 * 							defect 10610 Ver 6.7.0	
 * K Harrell	12/23/2010	add MSG_DB_CALL_SPCL_PLT_PRMT_START,
 * 							 BARCODE_SUFFIX, PERMIT_SUFFIX
 * 							defect 10700 Ver 6.7.0  						
 * T Pederson	01/03/2011	add STR_PERCENT
 * 							defect 10683 Ver 6.7.0  
 * K Harrell	01/12/2011	add COUNTY_ABBR, REGION_ABBR,REGIONAL_OFFICE
 * 							defect 10726 Ver 6.7.0
 * K Harrell	01/21/2011  add PROCS_WEB_SUB, MAX_POS_CUSTSEQNO,
 * 							MAX_POS_TRANSTIME
 * 							defect 10734 Ver 6.7.0
 * K Harrell	06/01/2011	add MSG_DB_CALL_MOD_PRMT_TRANS_HSTRY_START,
 * 							MSG_DB_CALL_MOD_PRMT_TRANS_HSTRY_SUCCESS
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	06/19/2011	add INQ008_COL_TRANSID, INQ008_COL_DATE,
 * 							 INQ008_COL_PRMTTYPE, INQ008_COL_EXPMOYR,
 * 							 INQ008_COL_MODYR, INQ008_COL_MAKE,
 * 							 INQ008_COL_VIN, INQ008_COL_APPLICANT,
 * 							 INQ008_COL_EMPID, INQ008_COL_OFCNAME
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	06/22/2011  add VINA_MOTORCYCLE_VEHTYPECD,
 * 							 VINA_TRUCK_VEHTYPECD, 
 * 							 VINA_PASSENGER_VEHTYPECD, 
 * 							 VINA_UNKNOWN_VEHTYPECD,
 * 							 VEHCLASSCD_MOTORCYCLE
 * 							defect 10844 Ver 6.8.0 
 * Ray Rowehl	08/15/2011	Add Confirmed for Insurance Check.
 * 							add CONFIRMED
 * 							defect 10119 Ver 6.8.1
 * K Harrell	09/12/2011	add MSG_DB_CALL_LOG_FUNC_TRANS_START,
 * 							  MSG_DB_CALL_LOG_FUNC_TRANS_SUCCESS,
 * 							  MSG_DB_CALL_FRAUD_LOG_START,
 * 							  MSG_DB_CALL_FRAUD_LOG_SUCCESS
 * 							defect 10994 Ver 6.8.1  
 * ---------------------------------------------------------------------
 */

/**
 * Constants for function id of Common Module
 * 
 * @version 6.8.1				09/12/2011
 * @author	Joseph Peters
 * @since						08/27/2001 14:05:31
 */

public class CommonConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;

	// defect 9585
	//	add DLRGDN_MAX_LENGTH
	public final static int DLRGDN_MAX_LENGTH = 10;
	// end defect 9585
	// defect 8366
	// This is the number of days to keep in the log files
	// when you prune them.
	public final static int PRUNE_LOG_FILE_DAYS = 7;
	// end defect 8366
	public final static int GET_VEH = 1;
	public final static int ADD_TRANS = 2;
	public final static int END_TRANS = 3;
	public final static int POST_TRANS = 4;
	public final static int GET_SAVED_VEH = 5;
	public final static int GET_MISC_DATA = 6;
	public final static int SAVE_VEH = 7;
	public static final int GET_NEXT_COMPLETE_TRANS_VC = 8;
	public static final int CAL_FEES = 9;
	public static final int IS_NEXT_VC_REG029 = 10;
	public static final int VALIDATE = 11;
	public static final int CHECK_NEXT_AVAIL = 12;
	public static final int GET_SAVED_INVENTORY = 13;
	public static final int SAVE_INVENTORY = 14;
	public static final int CANCEL_TRANS = 15;
	public static final int SET_ASIDE = 16;
	public static final int GET_TRANS_PAYMENT = 17;
	public static final int PUT_ON_HOLD = 18;
	public static final int GET_VEH_MISC = 19;
	public static final int SAVE_VEH_MISC = 20;

	// TODO is this used?  Appears not.  See CCB
	public static final int HOLD_INV003_ITM = 21;
	// end defect 6966 
	public static final int CANCEL_SELECTED_TRANS = 22;
	public static final int TAKE_OFF_HOLD = 23;
	public static final int PROC_INTERNET_ADDR_CHNG = 24;
	public static final int PROC_TRANS = 25;
	public static final int ADD_TRANS_IRENEW = 26;
	public static final int SAVE_TIME_PERMIT = 27;
	public static final int GET_TIME_PERMIT = 28;
	//Use for same vehicle
	public static final int CLEAR_SAVE_VEH_INFO = 29;
	public static final int TAKE_OFF_HOLD_TEMP = 30;
	public static final int REMOVE_INVENTORY = 31;
	public static final int APP_MF_ERR_LOG = 32;
	public static final int MULTI_ARCHIVE = 33;
	// PCR 34
	// defect 10075	
	//public static final int DTA_DISK_WRITE = 34;
	public static final int DTA_ADD_TRANS = 34;
	// end defect 10075
	public static final int REPRINT_STICKER = 35;
	// END PCR 34
	public static final int ADD_TRANS_FOR_VOID = 40;
	public static final int END_TRANS_FOR_VOID = 41;
	// defect 8900
	public static final int NOREGISFEES = 4;
	// end 8900

	// defect 9085
	public static final int GET_VI_SPCL_PLT_VECTOR = 42;
	public static final int SAVE_VI_SPCL_PLT_VECTOR = 43;
	public static final int GET_SAVED_SPCL_PLT = 44;
	public static final int SAVE_SPCL_PLT = 45;
	public static final int PROC_IAPPL = 46;
	// end defect 9085

	// defect 9502  
	public static final int PROC_V21VTN = 47;
	public static final int PROC_V21PLD = 48;
	// end defect 9502

	// defect 9711
	public static final int PROC_VENDOR_PLATES = 49;
	// end defect 9711

	// defect 10491 
	public static final int GET_VI_PRMT_VECTOR = 50;
	public static final int SAVE_VI_PRMT_VECTOR = 51;
	public static final int OFCISSUANCECD_HQ = 1;
	public static final int OFCISSUANCECD_REGION = 2;
	public static final int OFCISSUANCECD_COUNTY = 3;
	// end defect 10491 

	// defect 9582 
	public static final int PLT_DISPOSITION_UNCHANGED = 0;
	public static final int PLT_WITH_OWNER = 1;
	public static final int PLT_WITH_VEHICLE = 2;
	public static final int PLT_DISPOSED = 3;
	// end defect 9582 

	//MainFrame keys
	// defect 9086
	//	 Add SPCLREGID
	public final static String SPCL_REG_ID = "SPCLREGID";
	// end defect
	public final static String DOC_NO = "DOCNO";
	public final static String REG_PLATE_NO = "REGPLTNO";
	public final static String REG_STICKER_NO = "REGSTKRNO";
	public final static String VIN = "VIN";
	public final static String INVOICE_NO = "INVCNO";
	public final static String COUNTY_NO = "CNTYNO";
	public final static String OWNER_ID = "OWNRID";
	public final static String FUNDS_REPORT_DATE = "FDSRPTDATE";
	public final static String FUNDS_DUE_DATE = "FDSDUEDATE";
	public final static String FUNDS_PAYMENT_DATE = "FDSPMTDATE";
	public final static String REPORTING_DATE = "RPTNGDATE";
	public final static String TRACE_NO = "FDSTRACENO";
	public final static String CHECK_NO = "FDSCHECKNO";

	// defect 10491 
	public final static String PRMT_PRMTNO = "PRMTNO";
	public final static String PRMT_VIN = "VIN";
	public final static String PRMT_LSTNAME = "CUSTLSTNAM";
	public final static String PRMT_BSNNAME = "CUSTBSNNAM";
	public final static String PRMT_PRMTID = "PRMTID";
	// end defect 10491 

	public final static int VALID_HELD = 1;
	public final static int NOT_EXISTED_ITEM = 2;
	public final static int NOT_EXISTED_ITEM_REUSED = 3;
	public final static int NOT_EXISTED_ITEM_REISSUED = 4;
	public final static int MISMATCHED_ITEM = 5;

	public static final int SEARCH_ACTIVE_INACTIVE = 0;
	public static final int SEARCH_ARCHIVE = 1;
	public static final int SEARCH_SPECIAL_PLATES = 2;
	// defect 10491 
	public static final int SEARCH_PERMIT = 3;
	// end defect 10491 

	public final static int MULTI_MULTI = 30;
	public final static int MULTI_SINGLE = 31;

	// PCR 34
	// defect 10075
	// Removed, was not being used
	//public final static String DTA_DISK = "DTA_DISK";
	// end defect 10075
	public final static String RSPS_DTA = "DTA";
	public final static String RSPS_SUB = "SUB";
	public final static String POS_DTA = "D";
	public final static String POS_SUB = "S";
	public final static String REPORT_POS_DTA = "POS-DTA";
	public final static String REPORT_POS_SUB = "POS-SUB";
	public final static String REPORT_RSPS_DTA = "RSPSDTA";
	public final static String REPORT_RSPS_SUB = "RSPSSUB";
	public final static String POS = "POS";
	// END PCR 34

	// defect 8900
	public final static String RTS_TBCREATOR = "RTS";
	public final static String PILOT_TBCREATOR = "PILOT";
	// end defect 8900

	// defect 9368 
	public static final String PASSENGER_PLT = "PSP";
	public static final String TRUCK_PLT = "TKP";
	public static final int PASS_LE_6000_REGCLASSCD = 25;
	public static final int TRK_LE_1_TON_REGCLASSCD = 35;
	// end defect 9368 

	// defect 9085 
	public final static int INET_OFC_CD = 5;
	// end defect 9085 

	// defect 7885
	// common ints
	//	constants to get elements out of a vector
	public final static int ELEMENT_0 = 0;
	public final static int ELEMENT_1 = 1;
	public final static int ELEMENT_2 = 2;
	public final static int ELEMENT_3 = 3;
	public final static int ELEMENT_4 = 4;
	public final static int ELEMENT_5 = 5;
	public final static int ELEMENT_6 = 6;
	public final static int ELEMENT_7 = 7;
	public final static int ELEMENT_8 = 8;
	public final static int ELEMENT_9 = 9;

	// common lengths
	public static final int LENGTH_CITY = 19;
	// defect 9971
	public static final int LENGTH_CNTRY = 4;
	// end defect 9971
	public static final int LENGTH_CONTACT_PERSON = 30;
	public static final int LENGTH_NAME = 30;
	public static final int LENGTH_OFFICE_ISSUANCENO = 3;
	public static final int LENGTH_STREET = 30;
	public static final int LENGTH_STATE = 2;
	public static final int LENGTH_TELEPHONE_NUMBER = 10;
	public static final int LENGTH_TRANSAMDATE = 5;
	public static final int LENGTH_TRANS_WSID = 3;
	public static final int LENGTH_TRANS_TIME = 6;
	public final static int LENGTH_YEAR = 4;
	public static final int LENGTH_ZIPCODE = 5;
	public static final int LENGTH_ZIP_PLUS_4 = 4;
	public static final int LENGTH_CNTRY_ZIP = 9;
	// defect 10127 
	public final static int LENGTH_PREV_OWNR_CITY = 11;
	public final static int LENGTH_PREV_OWNR_NAME = 24;
	// end defect 10127 

	// defect 10505 
	public final static int LENGTH_CUSTSEQNO = 4;
	// end defect 10505 

	// defect 10191
	public final static int MAX_NO_ADDRESS_LINES = 3;
	// end defect 10191 

	// defect 10491 
	public final static int LENGTH_BUSINESS_NAME = 60;
	public final static int LENGTH_FIRST_NAME = 30;
	public final static int LENGTH_MI_NAME = 1;
	public final static int LENGTH_LAST_NAME = 30;
	public final static int LENGTH_PERMIT_NO = 7;
	public final static int LENGTH_OTPT_PT = 32;
	public static final int LENGTH_VIN_MAX = 22;
	// end defect 10491 

	// defect 10253
	/**
	 * Max Length of a Manufacturing Plate Number.
	 */
	public static final int LENGTH_MFG_PLTNO_MAX = 8;
	// end defect 10253

	// defect 10091
	public static final int LENGTH_PLTNO = 7;
	public static final int LENGTH_DOCNO = 17;
	public static final int LENGTH_VIN = 17;

	public static final int BOTH_ADDL_AND_CUMUL = 2;
	// end defect 10091

	// defect 10372 
	public static final int LENGTH_EMAIL = 50;
	public static final String TXT_EMAIL = "E-Mail:";
	// end defect 10372  

	// Usual indicator for not found
	public final static int NOT_FOUND = -1;

	//	Common button text
	public static final String BTN_TXT_ADD = "Add";
	public static final String BTN_TXT_CANCEL = "Cancel";
	public static final String BTN_TXT_DELETE = "Delete";
	public static final String BTN_TXT_ENTER = "Enter";
	public static final String BTN_TXT_HELP = "Help";
	public static final String BTN_TXT_HOLD = "Hold";
	public static final String BTN_TXT_MODIFY = "Modify";
	public static final String BTN_TXT_OK = "Ok";
	public static final String BTN_TXT_PRINT = "Print";
	public static final String BTN_TXT_RELEASE = "Release";
	public static final String BTN_TXT_REVISE = "Revise";
	public static final String BTN_TXT_VIEW = "View";

	// defect 10086
	public static final String BTN_TXT_PREV = "Prev";
	public static final String BTN_TXT_NEXT = "Next";
	public static final String BTN_TXT_LAST = "Last";
	public static final String BTN_TXT_FIRST = "First";
	// end defect 10086 

	public static final String FONT_JLIST = "monospaced";

	// common string definitions used every where
	// See ScreenConstant for constants just for the gui.
	// defect 7768
	public final static String STR_AT = "@";
	// end defect 7768
	public final static String STR_COLON = ":";
	public final static String STR_COMMA = ",";
	public final static String STR_DASH = "-";
	public final static String STR_DOUBLE_QUOTE = "\"";
	public final static String STR_EQUAL = "=";
	public final static String STR_PERIOD = ".";
	// defect 10683
	public final static String STR_PERCENT = "%";
	// end defect 10683
	// defect 8900
	public final static String STR_POUND = "#";
	public final static String STR_QUESTION_MARK = "?";
	//	end defect 8900
	public final static String STR_SLASH = "/";
	public final static String STR_OPEN_PARENTHESES = "(";
	public final static String STR_CLOSE_PARENTHESES = ")";
	public final static String STR_SPACE_EMPTY = "";
	public final static String STR_SPACE_ONE = " ";
	public final static String STR_SPACE_TWO = "  ";
	public final static String STR_SPACE_THREE = "   ";
	public final static String STR_COMMA_SPACE = ", ";
	public final static String STR_ZERO = "0";
	public final static String STR_ZERO_DOLLAR = "0.00";
	public final static String STR_USA = "USA";
	public final static String STR_TX = "TX";
	public final static String STR_ERROR = "ERROR";
	public final static String STR_EXEMPT = "EXEMPT";
	public final static String STR_NULL = "null";
	// defect 9121
	// Added new String constants
	public static final String STR_QUOTE = "\"";
	public static final String STR_GREATER_THAN = ">";
	public static final String STR_LESS_THAN = "<";
	public static final String STR_OPEN_BRACKET = "[";
	public static final String STR_CLOSE_BRACKET = "]";
	public static final String STR_AMPERSAND = "&";
	public static final String STR_PLUS_SIGN = "+";
	public static final String STR_BACK_SLASH = "\\";
	// end defect 9121

	public final static String SYSTEM_LINE_SEPARATOR =
		System.getProperty("line.separator");

	// defect 8595 
	public static final String TXT_ADMIN_LOG_ADD = "Add";
	public static final String TXT_ADMIN_LOG_DELETE = "Delete";
	public static final String TXT_ADMIN_LOG_REVISE = "Revise";
	// end defect 8595

	// defect 10168
	public static final String TXT_ADMIN_LOG_RERUN_REPORT = "Rerun Rpt";
	public static final String TXT_ADMIN_LOG_SUBSTA_SUMMARY =
		"Substa Summary";
	public static final String TXT_ADMIN_LOG_COUNTY_WIDE = "CountyWide";
	public static final String TXT_ADMIN_LOG_CERTFD_LIENHLDR =
		"Certfd Lienhldr";
	// end defect 10168 

	public static final String TXT_CITY_COLON = "City:";
	public static final String TXT_DEALER = "Dealer";
	public static final String TXT_LIENHOLDER = "Lienholder";
	public static final String TXT_NAME_COLON = "Name:";
	public static final String TXT_STREET_COLON = "Street:";
	// defect 10127 
	public static final String TXT_ADDRESS_COLON = "Address:";
	public static final String TXT_ID_COLON = "Id:";
	public static final String TXT_PHONE_NO_COLON = "Phone No:";
	// end defect 10127 
	public static final String TXT_SUBCON = "Subcon";
	public static final String TXT_SUBCON_UC = TXT_SUBCON.toUpperCase();
	public static final String TXT_ZIP_CODE_COLON = "Zip Code:";
	// defect 8900
	public static final String TXT_EXPORT = "Export";
	public static final String TXT_PERIOD_CSV = ".CSV";
	public static final String TXT_REPORT = "Report";
	public static final String TXT_SAVE_IN_COLON_SPACE = "Save In: ";
	public static final String TXT_SELECT_ALL = "Select All";
	public static final String TXT_SELECT_ONE_COLON = "Select One:";
	// end defect 8900	
	public static final String VALID_INTS = "0123456789";
	public static final String VALID_LETTERS =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	// common dollars
	// defect 8900
	public static final Dollar ZERO_DOLLAR =
		new Dollar(STR_ZERO_DOLLAR);

	// Common table model text
	public static final String TBL_TXT_OFFICEISSNO =
		"Office Issuance No";
	public static final String TBL_TXT_OFFICENAME = "Office Name";
	public static final String TBL_TXT_OFFICENO = "Office No";
	// end defect 8900

	/**
	 * @value "Do you want to continue?"
	 */
	public static final String TXT_CONTINUE_QUESTION =
		"Do you want to continue?";

	// defect 8900
	public static final String TXT_OVERWRITE =
		"Are you sure you want to overwrite ";
	// defect end 8900

	// defect 9085 
	public static final String TXT_COMPLETE_TRANS_QUESTION =
		"Are you sure you want to complete this transaction?";
	// end defect 9085 

	public static final String TXT_UNKNOWN = "Unknown";

	public static final String ITRNT_BACKOFFICE_TRANSNAME =
		"Internet BackOffice";

	public static final String MSG_DB_CALL_INET_TRANS_FAILED =
		"Failed DB call to InternetTransaction";

	public static final String MSG_DB_CALL_INET_TRANS_SUCCESS =
		"Successful DB call to InternetTransaction";

	public static final String TXT_INPUT_TYPE_INVALID =
		"Input Type invalid.";

	public static final String TXT_INSERT_IADDR_IRNR_TRANS_HDR_ERROR =
		"Error on IADDR/IRNR TransHeader Insert for ";

	public static final String MSG_DB_CALL_EXMPTAUDIT_START =
		"Starting DB call to ExemptAudit";

	public static final String MSG_DB_CALL_EXMPTAUDIT_SUCCESS =
		"Successful DB call to ExemptAudit";

	// FundFunctionTransaction
	public static final String MSG_DB_CALL_FUNDFUNC_START =
		"Starting DB call to FundFunctionTransaction";

	public static final String MSG_DB_CALL_FUNDFUNC_SUCCESS =
		"Successful DB call to FundFunctionTransaction";

	// TransactionFundsDetail 
	public static final String MSG_DB_CALL_TR_FDS_DETAIL_START =
		"Starting DB call to TransactionFundsDetail";

	public static final String MSG_DB_CALL_TR_FDS_DETAIL_SUCCESS =
		"Successful DB call to TransactionFundsDetail";

	// InventoryFunctionTransaction
	public static final String MSG_DB_CALL_INV_FUNC_START =
		"Starting DB call to InventoryFunctionTransaction";

	public static final String MSG_DB_CALL_INV_FUNC_SUCCESS =
		"Successful DB call to InventoryFunctionTransaction";

	// TransactionInventoryDetail
	public static final String MSG_DB_CALL_TR_INV_DTL_START =
		"Starting DB call to TransactionInventoryDetail";

	public static final String MSG_DB_CALL_TR_INV_DTL_SUCCESS =
		"Successful DB call to TransactionInventoryDetail";

	// MotorVehicleFunctionTransaction
	public static final String MSG_DB_CALL_MV_FUNC_START =
		"Starting DB call to MotorVehicleFunctionTransaction";

	public static final String MSG_DB_CALL_MV_FUNC_SUCCESS =
		"Successful DB call to MotorVehicleFunctionTransaction";

	// defect 9831 
	// DisabledPlacardTransaction
	public static final String MSG_DB_CALL_DSABLD_PLCRD_TRANS_START =
		"Starting DB call to DisabledPlacardTransaction";

	// DisabledPlacardCustomer 
	public static final String MSG_DB_CALL_DSABLD_PLCRD_CUST_START =
		"Starting DB call to DisabledPlacardCustomer";
	// end defect 9831 

	// defect 10700 
	public static final String MSG_DB_CALL_SPCL_PLT_PRMT_START =
		"Starting DB call to SpecialPlatePermit";
	// end defect 10700 

	// defect 10491 
	// PermitTransaction
	public static final String MSG_DB_CALL_PRMT_TRANS_START =
		"Starting DB call to PermitTransaction";

	public static final String MSG_DB_CALL_PRMT_TRANS_SUCCESS =
		"Successful DB call to PermitTransaction";
	// end defect 10491
	
	// defect 10994 
	//  LogonFunctionTransaction 
	public static final String MSG_DB_CALL_LOG_FUNC_TRANS_START =
		"Starting DB call to LogonFunctionTransaction";

	public static final String MSG_DB_CALL_LOG_FUNC_TRANS_SUCCESS =
		"Successful DB call to LogonFunctionTransaction";
	
	public static final String MSG_DB_CALL_FRAUD_LOG_START =
		"Starting DB call to LogonFunctionTransaction";

	public static final String MSG_DB_CALL_FRAUD_LOG_SUCCESS =
		"Successful DB call to LogonFunctionTransaction";
	// end defect 10994 

	// defect 10844 
	// ModifyPermitTransactionHistory 
	public static final String MSG_DB_CALL_MOD_PRMT_TRANS_HSTRY_START =
		"Starting DB call to ModifyPermitTransactionHistory";

	public static final String MSG_DB_CALL_MOD_PRMT_TRANS_HSTRY_SUCCESS =
		"Successful DB call to ModifyPermitTransactionHistory";
	// end defect 10844 

	// defect 9085 
	// SpecialRegistrationFunctionTransaction
	public static final String MSG_DB_CALL_SR_FUNC_START =
		"Starting DB call to SpecialRegistrationFunctionTransaction";

	public static final String MSG_DB_CALL_SR_FUNC_SUCCESS =
		"Successful DB call to SpecialRegistrationFunctionTransaction";

	// SpecialPlateTransactionHistory
	public static final String MSG_DB_CALL_SP_TRANS_HSTRY_START =
		"Starting DB call to SpecialPlateTransactionHistory";

	public static final String MSG_DB_CALL_SP_TRANS_HSTRY_SUCCESS =
		"Successful DB call to SpecialPlateTransactionHistory";
	// end defect 9085

	// defect 9969 
	// ElectronicTitleHistory
	public static final String MSG_DB_CALL_ETTL_HSTRY_START =
		"Starting DB call to ElectronicTitleHistory";
	// end defect 9969 

	// Trans 
	public static final String MSG_DB_CALL_TRANS_START =
		"Starting DB call to Transaction";

	public static final String MSG_DB_CALL_TRANS_SUCCESS =
		"Successful DB call to Transaction";

	public static final String MSG_DB_CALL_ALL_SUCCESS =
		"Successful DB call to all tables";

	public static final String MSG_FAILED_DB_CALL =
		"Failed DB call to a table";

	public static final String MSG_ROLLBACK_ISSUED = "Rollback Issued";

	public static final String MSG_START_DB_CALL_FOR_MISC =
		"Starting DB call to Miscellaneous";

	public static final String TXT_MISC_DATA_OFFICE_ERR =
		" Miscellaneous Data found with Office Issuance No: ";

	public static final String TXT_AND_SUBSTA_COLON = " & Substation: ";

	public static final String MSG_DB_CALL_FOR_MISC_SUCCESS =
		"Successful DB call to Miscellaneous";

	public static final String MSG_DB_CALL_FOR_MISC_UNSUCCESS =
		"Failed DB call to Miscellaneous";

	public static final String MSG_DB_CALL_TRANS_PYMNT_START =
		"Starting DB call to TransactionPayment";

	public static final String MSG_DB_CALL_TRANS_PYMNT_COMPLETE =
		"Complete DB call to TransactionPayment";

	public static final String MSG_DB_CALL_TRANS_PYMNT_ERROR =
		"Error in DB call to TransactionPayment";

	public static final String MSG_DB_CALL_REPRINT_STICKER_START =
		"Starting DB call to ReprintStickerDaily";

	public static final String MSG_DB_CALL_REPRINT_STICKER_SUCCESS =
		"Successful DB call to ReprintStickerDaily";

	// defect 10164 
	//public static final String TXT_SQL0803 = "SQL0803";
	public static final String DUPLICATE_KEY_EXCEPTION =
		"DuplicateKeyException";
	// end defect 10164  

	public static final String MSG_DB_CALL_REPRINT_STICKER_FAILED =
		"Failed DB call to ReprintStickerDaily";

	public static final String MSG_DB_CALL_TRANS_HDR_START =
		"Starting DB call to TransactionHeader";

	public static final String MSG_POSTTRANS_UPDATE_FAILED =
		"Update in PostTrans failed";

	public static final String TXT_FUNCTION_NOT_FOUND =
		"Function not found";

	public static final String MSG_OFFICEIDS_NULL =
		"OfficeCodesData is null";

	public static final String TXT_INSERT_IADDR_IRNR_TRANS_HDR =
		" Insert IADDR/IRNR TransHeader for ";

	public static final String MSG_DB_CALL_INET_TRANS_START =
		"Starting DB call to InternetTransaction";

	public static final String STR_SINGLE_QUOTE = "'";
	// end defect 7885

	// defect 8938
	public static final char CHAR_DASH = '-';
	// end defect 8938

	// defect 9337
	public static final int TONLY_REGCLASSCD = 99;
	public static final String TONLY_REGPLTCD = "TONLY";
	// end defect 9337

	//defect 9459
	// defect 9647
	public static final String VERIFIED = " Verified ";
	public static final String VERIFY_MANUALLY = " Verify Manually ";
	// end defect 9647
	public static final String NOT_REQUIRED = "Not Required";
	// end defect 9459

	// defect 9502
	/**
	 * Call was successful.
	 */
	public static final String SUCCESS = "SUCCESS";

	/**
	 * Request was Invalid.
	 */
	public static final String INVALID_REQ = "INVALID";

	/**
	 * Could not select just one record!
	 */
	public static final String MULTIPLE_RECS = "MULTIRECS";

	public static final String V21_NOT_FOUND = "NOTFOUND";

	// V21 
	public static final int V21_OFCISSUANCENO = 298;
	public static final int V21_SUBSTAID = 0;
	// V21 VTN
	public static final int V21VTN_TRANSWSID = 999;
	public static final String V21VTN_EMPID = "V21VTN";
	public static final String V21VTN_BACK_OFFICE_TRANS =
		"V21VTN BackOffice";
	// V21 PLD
	public static final int V21PLD_TRANSWSID = 998;
	public static final String V21PLD_EMPID = "V21PLD";
	public static final String V21PLD_BACK_OFFICE_TRANS =
		"V21PLD BackOffice";
	// end defect 9502

	// defect 9847 
	public static final String DEFAULT_TRANSEMPID = "LGNERR";
	// end defect 9847 

	//	defect 9971
	public static final int MAX_INDI_NO_SCROLL = 4;
	// end defect 9971 

	// defect 9953
	public final static int CTL005_COL_MODYR = 0;
	public final static int CTL005_COL_MAKE = 1;
	public final static int CTL005_COL_VIN = 2;
	public final static int CTL005_COL_PLTNO = 3;
	public final static int CTL005_COL_EXPMO = 4;
	public final static int CTL005_COL_EXPYR = 5;
	public final static int CTL005_COL_OWNRNAME = 6;
	public final static int REG002_COL_OWNRNAME = 0;
	public final static int REG002_COL_PLTNO = 1;
	// end defect 9953

	// defect 10112 
	public final static int INQ004REG_COL_MODYR = 0;
	public final static int INQ004REG_COL_MAKE = 1;
	public final static int INQ004REG_COL_VIN = 2;
	public final static int INQ004REG_COL_PLTNO = 3;
	public final static int INQ004REG_COL_EXPMO = 4;
	public final static int INQ004REG_COL_EXPYR = 5;
	public final static int INQ004REG_COL_NAME1 = 6;

	public final static int DEFAULT_PRIVACY_OPT_CD = 3;
	// end defect 10112 

	// defect 10491 
	public final static int INQ004PRMT_COL_MODYR = 0;
	public final static int INQ004PRMT_COL_MAKE = 1;
	public final static int INQ004PRMT_COL_VIN = 2;
	public final static int INQ004PRMT_COL_PRMTNO = 3;
	public final static int INQ004PRMT_COL_PRMTTYPE = 4;
	public final static int INQ004PRMT_COL_EXPMOYR = 5;
	public final static int INQ004PRMT_COL_NAME = 6;
	// end defect 10491 

	// defect 10598 
	public final static int INQ002_COL_TRANSID = 0;
	public final static int INQ002_COL_DATE = 1;
	public final static int INQ002_COL_TRANSCDDESC = 2;
	public final static int INQ002_COL_REGPLTNO = 3;
	public final static int INQ002_COL_EMPID = 4;
	public final static int INQ002_COL_OFCNAME = 5;
	// end defect 10598 

	// defect 10844 
	public final static int INQ008_COL_TRANSID = 0;
	public final static int INQ008_COL_DATE = 1;
	public final static int INQ008_COL_PRMTTYPE = 2;
	public final static int INQ008_COL_EXPMOYR = 3;
	public final static int INQ008_COL_MODYR = 4;
	public final static int INQ008_COL_MAKE = 5;
	public final static int INQ008_COL_VIN = 6;
	public final static int INQ008_COL_APPLICANT = 7;
	public final static int INQ008_COL_EMPID = 8;
	public final static int INQ008_COL_OFCNAME = 9;
	public final static String VINA_MOTORCYCLE_VEHTYPECD = "M";
	public final static String VINA_TRUCK_VEHTYPECD = "T";
	public final static String VINA_PASSENGER_VEHTYPECD = "P";
	public final static String VINA_UNKNOWN_VEHTYPECD = "U";
	public final static String VEHCLASSCD_MOTORCYCLE = "MTRCYCLE"; 
	// end defect 10844 

	// defect 10127 
	public final static int LENGTH_OWNERID = 9;
	public final static int LENGTH_UNIT_NO = 6;
	public final static int LENGTH_MAKE = 4;
	public final static int MIN_COUNTY_NO = 1;
	public final static int MAX_COUNTY_NO = 254;
	public final static boolean TX_DEFAULT_STATE = true;
	public final static boolean TX_NOT_DEFAULT_STATE = false;
	// end defect 10127

	// defect 10164 
	public final static String SQL_BEGIN = " - SQL - Begin";
	public final static String SQL_END = " - SQL - End";
	public final static String SQL_METHOD_BEGIN = " - Begin";
	public final static String SQL_METHOD_END = " - End";
	public final static String SQL_EXCEPTION = " - Exception ";
	// end defect 10164 

	// defect 10290 
	public final static int INV007_COL_YEAR = 0;
	public final static int INV007_COL_ITMDESC = 1;
	public final static int INV007_COL_ITMNO = 2;
	// end defect 10290 

	// defect 10317
	public final static String TIRE_TYPE_PNEUMATIC = "P";
	public final static String TIRE_TYPE_SOLID = "S";
	// end defect 10317

	// defect 10339 
	public final static int KEY008_NO_PLT_FLG = 2;
	// end defect 10339

	// defect 10401
	public static final double DBL_PYMNTAMT_VALUE_MAX = 99999999999.99;

	public static final int INT_ITRNTTRACENO_LENGTH_MAX = 15;
	public static final int INT_ORGNO_LENGTH_MAX = 4;
	public static final int INT_PYMNTORDERID_LNGTH_MAX = 8;

	public static final int L_SPCLREGID_VALUE_MAX = 999999999;

	public static final String STR_CALLER_MC = "Caller";
	public static final String STR_CITY_MC = "City";
	public static final String STR_ITRNTTRACENO_MC = "ItrntTraceNo";
	public static final String STR_MFGPLTNO_MC = "MfgPltNo";
	public static final String STR_MISSING_MC = "Missing";
	public static final String STR_ORGNO_MC = "OrgNo";
	public static final String STR_PLTCD_MC = "PltCd";
	public static final String STR_PLTEXPMO_MC = "PltExpMo";
	public static final String STR_PLTEXPYR_MC = "PltExpYr";
	public static final String STR_PLTOWNRNAME1_MC = "PltOwnrName1";
	public static final String STR_PLTOWNRNAME2_MC = "PltOwnrName2";
	public static final String STR_PLTOWNRPHONE_MC = "PltOwnrPhone";
	public static final String STR_PLTNO_MC = "PltNo";
	public static final String STR_PLTVALIDITYTERM_MC =
		"PltValidityTerm";
	public static final String STR_PYMNTAMT_MC = "PymntAmt";
	public static final String STR_PYMNTORDERID_MC = "PymntOrderId";
	public static final String STR_RESERVREASNCD_MC = "ReservReasnCd";
	public static final String STR_RESCOMPTCNTYNO_MC = "ResComptCntyNo";
	public static final String STR_SESSIONID_MC = "SessionId";
	public static final String STR_STATE_MC = "State";
	public static final String STR_STREETLINE1_MC = "StreetLine1";
	public static final String STR_STREETLINE2_MC = "StreetLine2";
	public static final String STR_TOO_LONG_MC = "Too Long";
	public static final String STR_TXT_EMPTY = "EMPTY";
	public static final String STR_ZIPP4_MC = "ZipP4";
	public static final String STR_ZIP_MC = "Zip";
	// end defect 10401

	// defect 10610
	public static final int PROC_EREMINDER = 52;
	// end defect 10610

	// defect 10734 
	public static final int PROC_WEB_SUB = 53;
	// end defect 10734   

	// defect 10700 
	public static final String BARCODE_SUFFIX = "B";
	public static final String PERMIT_SUFFIX = "P";
	// end defect 10700

	// defect 10726 
	public static final String COUNTY_ABBR = " CTY";
	public static final String REGION_ABBR = " RO";
	public static final String REGIONAL_OFFICE = "REGIONAL OFFICE";
	// defect 10726

	// defect 10734 
	public static final int MAX_POS_CUSTSEQNO = 6999;
	public static final int MAX_POS_TRANSTIME = 249999;
	// end defect 10734  

	// defect 10119
	public static String CONFIRMED = "Confirmed";
	// end defect 10119

}
