package com.txdot.isd.rts.services.util.constants;/* *  * LocalOptionConstant.java *   * (c) Texas Department of Transportation  2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	09/07/2003	Add handling for Active Directory Prefix. *							add USER_ID_MAX_LENGTH, *							USER_ID_MIN_LENGTH *							defect 6445  Ver 5.1.6 * Ray Rowehl	12/30/2003	Rename USER_ID_MAX_LENGTH and  *									USER_ID_MIN_LENGTH *							to USER_NAME_MAX_LENGTH and  *									USER_NAME_MIN_LENGTH *							to reflect field name *							defect 6445 Ver 5.1.6 * Ray Rowehl	02/19/2004	reflow to meet updated java standards *							defect 6445 Ver 5.1.6 * Ray Rowehl	02/24/2004	add User Name Prefix length *							add USER_NAME_PREFIX_LENGTH *							defect 6445 Ver 5.1.6 * Min Wang		07/14/2004	add constants for RSPS *							add GET_RSPS_IDS, GET_RSPS_UPDT,  *								PROCESS_RSPS_UPDT *							defect 7135 Ver 5.2.1 * Ray Rowehl	08/02/2004	Add constant for RSPS Processing *							add GET_SYSUPDATE_LIST *							defect 7135 Ver 5.2.1 * Ray Rowehl	11/11/2004	Set the max length for User Name Base Id *							depending on whether or not there is a *							prefix. *							add USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX, *								USER_NAME_BASE_ID_MAX_LENGTH_PREFIX *							defect 7604 Ver 5.2.2 * K Harrell	01/14/2005 	Remove constants for UPDT_XXX as not *							used *							defect 7901 Ver 5.2.3  * K Harrell	01/14/2005	Correct spelling for RETREIVE_CREDIT_FEES *							defect 7902 Ver 5.2.3 * K Harrell	01/14/2005	Remove unused constants for Login *							defect 7903 Ver 5.2.3  * Ray Rowehl	06/11/2005	Code Cleanup * 							remove extra * from file doc. * 							defect 7891 Ver 5.2.3 * Ray Rowehl	08/24/2005	Add constants from Local Options * 							defect 7891 Ver 5.2.3 * K Harrell	04/20/2007	Remove constants no longer required with * 							 new SystemProperties.  * 							delete COUNTY,REGION,VTR * 							defect 9085 Ver Special Plates  * K Harrell	09/02/2008  add RTS_CRDT_CARD_FEE, RTS_DEALERS, * 							 RTS_LIENHOLDERS, RTS_MISC,  RTS_PYMNT_ACCT, * 							 RTS_RSPS_WS_STATUS, RTS_SECURITY,  * 							 RTS_SUBCON, RTS_SUBSTA_SUBSCR  * 							defect 8721 Ver Defect_POS_B * J Zwiener	02/27/2009	add GENERATE_CERTFD_LIENHLDR_RPT * 							defect 9968 Ver Defect_POS_E   * Ray Rowehl	07/13/2009	Modify Login status constants to ensure * 							they are unique. * 							Remove unused constants. * 							add LOGIN_FAIL * 							delete INVALID_USERID_PASSWORD_COMBINATION_ERROR, * 								LOGIN_WRONG_PASSWORD * 							modify LOGIN_SUCCESS,  * 								LOGIN_SUCCESS_FROM_CACHE  * 							defect 10103 Ver Defect_POS_F * K Harrell	01/18/2011	add OPT008_COL_RPT_NUMBER,  * 								OPT008_COL_RPT_DESC,  * 								OPT008_COL_RPT_AUTOPRINT * 							defect 10701 Ver 6.7.0  * --------------------------------------------------------------------- *//** * Constants for Local Options. * * @version	6.7.0 			01/18/2011 * @author	Ashish Majahan * <br>Creation Date: 		08/13/2001 13:51:16 *//* &LocalOptionConstant& */public class LocalOptionConstant{/* &LocalOptionConstant'NO_DATA_TO_BUSINESS& */	public final static int NO_DATA_TO_BUSINESS =		GeneralConstant.NO_DATA_TO_BUSINESS;/* &LocalOptionConstant'PRNT_SECRTY_CHNG_RPT& */	public static final int PRNT_SECRTY_CHNG_RPT = 1;/* &LocalOptionConstant'PRNT_EMP_SECRTY_RPT& */	public static final int PRNT_EMP_SECRTY_RPT = 2;/* &LocalOptionConstant'PRNT_EVENT_SECRTY_RPT& */	public static final int PRNT_EVENT_SECRTY_RPT = 3;/* &LocalOptionConstant'PRNT_CONNECTION_STATUS_RPT& */	public static final int PRNT_CONNECTION_STATUS_RPT = 4;/* &LocalOptionConstant'PRNT_PUBLISHING_RPT& */	public static final int PRNT_PUBLISHING_RPT = 5;/* &LocalOptionConstant'PRNT_DLR_RPT& */	public static final int PRNT_DLR_RPT = 6;/* &LocalOptionConstant'PRNT_SUBCON_RPT& */	public static final int PRNT_SUBCON_RPT = 7;/* &LocalOptionConstant'PRNT_LIENHLDR_RPT& */	public static final int PRNT_LIENHLDR_RPT = 8;/* &LocalOptionConstant'ADD_DLR& */	public static final int ADD_DLR = 9;/* &LocalOptionConstant'DEL_DLR& */	public static final int DEL_DLR = 11;/* &LocalOptionConstant'ADD_SUBCON& */	public static final int ADD_SUBCON = 12;/* &LocalOptionConstant'DEL_SUBCON& */	public static final int DEL_SUBCON = 13;/* &LocalOptionConstant'ADD_LIENHLDR& */	public static final int ADD_LIENHLDR = 15;/* &LocalOptionConstant'GET_DLR_DATA_ONID& */	public static final int GET_DLR_DATA_ONID = 16;/* &LocalOptionConstant'GET_SUBCON_DATA_ONID& */	public static final int GET_SUBCON_DATA_ONID = 17;/* &LocalOptionConstant'GENERATE_DLR_RPT& */	public static final int GENERATE_DLR_RPT = 19;/* &LocalOptionConstant'GENERATE_ACTIVE_RPT& */	public static final int GENERATE_ACTIVE_RPT = 20;/* &LocalOptionConstant'GENERATE_SUBCON_RPT& */	public static final int GENERATE_SUBCON_RPT = 21;/* &LocalOptionConstant'GENERATE_LIENHLDR_RPT& */	public static final int GENERATE_LIENHLDR_RPT = 22;/* &LocalOptionConstant'REVISE_DLR& */	public static final int REVISE_DLR = 23;/* &LocalOptionConstant'REVISE_LIENHLDR& */	public static final int REVISE_LIENHLDR = 24;/* &LocalOptionConstant'REVISE_SUBCON& */	public static final int REVISE_SUBCON = 25;/* &LocalOptionConstant'GENERATE_SECRTY_CHNG_RPT& */	public static final int GENERATE_SECRTY_CHNG_RPT = 26;/* &LocalOptionConstant'GENERATE_EMP_SECRTY_RPT& */	public static final int GENERATE_EMP_SECRTY_RPT = 27;/* &LocalOptionConstant'GENERATE_EVENT_SECRTY_RPT& */	public static final int GENERATE_EVENT_SECRTY_RPT = 28;/* &LocalOptionConstant'GET_EMP_DATA_ONID& */	public static final int GET_EMP_DATA_ONID = 29;/* &LocalOptionConstant'CANCEL_EMP_ACCS_RIGHTS& */	public static final int CANCEL_EMP_ACCS_RIGHTS = 30;/* &LocalOptionConstant'ADD_EMP_ACCS_RIGHTS& */	public static final int ADD_EMP_ACCS_RIGHTS = 31;/* &LocalOptionConstant'REVISE_EMP_ACCS_RIGHTS& */	public static final int REVISE_EMP_ACCS_RIGHTS = 32;/* &LocalOptionConstant'DEL_EMP_ACCS_RIGHTS& */	public static final int DEL_EMP_ACCS_RIGHTS = 33;/* &LocalOptionConstant'SUPV_OVERIDE& */	public static final int SUPV_OVERIDE = 34;/* &LocalOptionConstant'RESET_PASSWORD& */	public static final int RESET_PASSWORD = 35;/* &LocalOptionConstant'GENERATE_PUBLISHING_RPT& */	public static final int GENERATE_PUBLISHING_RPT = 37;/* &LocalOptionConstant'SERVER_PLUS_ENABLE_DISABLE& */	public static final int SERVER_PLUS_ENABLE_DISABLE = 38;/* &LocalOptionConstant'UPDT_PUBLISHING& */	public static final int UPDT_PUBLISHING = 39;/* &LocalOptionConstant'GET_LIENHLDR_DATA_ONID& */	public static final int GET_LIENHLDR_DATA_ONID = 40;/* &LocalOptionConstant'DEL_LIENHLDR& */	public static final int DEL_LIENHLDR = 41;/* &LocalOptionConstant'LOGIN& */	public static final int LOGIN = 42;/* &LocalOptionConstant'CHANGE_PASS& */	public static final int CHANGE_PASS = 43;/* &LocalOptionConstant'PUB_UPDT& */	public static final int PUB_UPDT = 44;/* &LocalOptionConstant'REVISE_PUB& */	public static final int REVISE_PUB = 45;/* &LocalOptionConstant'SERVER_PLUS& */	public static final int SERVER_PLUS = 46;/* &LocalOptionConstant'SUPER_OVERRIDE_LOOKUP& */	public static final int SUPER_OVERRIDE_LOOKUP = 47;	//Used to get the security data for current employee/* &LocalOptionConstant'ADD_CURRENT_EMP_DATA_ONID& */	public static final int ADD_CURRENT_EMP_DATA_ONID = 48;/* &LocalOptionConstant'RETRIEVE_CREDIT_FEES& */	public static final int RETRIEVE_CREDIT_FEES = 49;/* &LocalOptionConstant'UPDATE_CREDIT_DB& */	public static final int UPDATE_CREDIT_DB = 50;	/**  Codes for Payment Account Update *//* &LocalOptionConstant'GET_PAYMENT_ACCOUNTS& */	public static final int GET_PAYMENT_ACCOUNTS = 60;/* &LocalOptionConstant'ADD_PAYMENT_ACCOUNT& */	public static final int ADD_PAYMENT_ACCOUNT = 61;/* &LocalOptionConstant'REVISE_PAYMENT_ACCOUNT& */	public static final int REVISE_PAYMENT_ACCOUNT = 62;/* &LocalOptionConstant'DELETE_PAYMENT_ACCOUNT& */	public static final int DELETE_PAYMENT_ACCOUNT = 63;	// Codes for handling AD Interface/* &LocalOptionConstant'AD_ADD_USER& */	public final static int AD_ADD_USER = 70;/* &LocalOptionConstant'AD_UPDT_USER& */	public final static int AD_UPDT_USER = 71;/* &LocalOptionConstant'AD_DEL_USER& */	public final static int AD_DEL_USER = 72;/* &LocalOptionConstant'AD_RESET_PSWD& */	public final static int AD_RESET_PSWD = 73;	// defect 7135	// Codes for handling RSPS Update/* &LocalOptionConstant'GET_RSPS_IDS& */	public final static int GET_RSPS_IDS = 74;/* &LocalOptionConstant'GET_RSPS_UPDT& */	public final static int GET_RSPS_UPDT = 75;/* &LocalOptionConstant'PROCESS_RSPS_UPDT& */	public final static int PROCESS_RSPS_UPDT = 76;/* &LocalOptionConstant'GET_SYSUPDATE_LIST& */	public final static int GET_SYSUPDATE_LIST = 77;	// end defect 7135	// defect 9968/* &LocalOptionConstant'GENERATE_CERTFD_LIENHLDR_RPT& */	public final static int GENERATE_CERTFD_LIENHLDR_RPT = 78;	// end defect 9968	// defect 10701 /* &LocalOptionConstant'UPDATE_BATCH_RPT_MGMT& */	public final static int UPDATE_BATCH_RPT_MGMT = 79;/* &LocalOptionConstant'GET_BATCH_RPT_MGMT& */	public final static int GET_BATCH_RPT_MGMT = 80;	// end defect 10701 	// defect 8721 /* &LocalOptionConstant'RTS_CRDT_CARD_FEE& */	public final static String RTS_CRDT_CARD_FEE = "RTS_CRDT_CARD_FEE";/* &LocalOptionConstant'RTS_DEALERS& */	public final static String RTS_DEALERS = "RTS_DEALERS";/* &LocalOptionConstant'RTS_LIENHOLDERS& */	public final static String RTS_LIENHOLDERS = "RTS_LIENHOLDERS";/* &LocalOptionConstant'RTS_MISC& */	public final static String RTS_MISC = "RTS_MISC";/* &LocalOptionConstant'RTS_PYMNT_ACCT& */	public final static String RTS_PYMNT_ACCT = "RTS_PYMNT_ACCT";/* &LocalOptionConstant'RTS_RSPS_WS_STATUS& */	public final static String RTS_RSPS_WS_STATUS =		"RTS_RSPS_WS_STATUS";/* &LocalOptionConstant'RTS_SECURITY& */	public final static String RTS_SECURITY = "RTS_SECURITY";/* &LocalOptionConstant'RTS_SUBCON& */	public final static String RTS_SUBCON = "RTS_SUBCON";/* &LocalOptionConstant'RTS_SUBSTA_SUBSCR& */	public final static String RTS_SUBSTA_SUBSCR = "RTS_SUBSTA_SUBSCR";	// end defect 8721	// defect 10701 /* &LocalOptionConstant'RTS_BATCH_RPT_MGMT& */	public final static String RTS_BATCH_RPT_MGMT =		"RTS_BATCH_RPT_MGMT";	// end defect 10701 	// defect 10103	///**  	// * Error Code returned when logon fails in validating userid/	// * password.	// */	//public final static int INVALID_USERID_PASSWORD_COMBINATION_ERROR =	//	54;	// end defect 10103	// defect 9085 	// public final static int COUNTY = 3;	// public final static int REGION = 2;	// public final static int VTR = 1;	// end defect 9085 	// defect 10103/* &LocalOptionConstant'LOGIN_FAIL& */	public final static int LOGIN_FAIL = 81;/* &LocalOptionConstant'LOGIN_SUCCESS& */	public final static int LOGIN_SUCCESS = 80;/* &LocalOptionConstant'LOGIN_SUCCESS_FROM_CACHE& */	public final static int LOGIN_SUCCESS_FROM_CACHE = 89;	// end defect 10103	// defect 6445 	// UserId Lengths/* &LocalOptionConstant'USER_NAME_MAX_LENGTH& */	public final static int USER_NAME_MAX_LENGTH = 11;/* &LocalOptionConstant'USER_NAME_MIN_LENGTH& */	public final static int USER_NAME_MIN_LENGTH = 6;/* &LocalOptionConstant'USER_NAME_PREFIX_LENGTH& */	public final static int USER_NAME_PREFIX_LENGTH = 4;/* &LocalOptionConstant'EMP_ID_MAX_LENGTH& */	public final static int EMP_ID_MAX_LENGTH = 7;	// defect 7604	// Allow non-prefix base ids to to a length of 8.	// Base ids with a prefix can only be 7./* &LocalOptionConstant'USER_NAME_BASE_ID_MAX_LENGTH_PREFIX& */	public final static int USER_NAME_BASE_ID_MAX_LENGTH_PREFIX =		EMP_ID_MAX_LENGTH;/* &LocalOptionConstant'USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX& */	public final static int USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX = 8;	// end defect 7604/* &LocalOptionConstant'EMP_ID_MIN_LENGTH& */	public final static int EMP_ID_MIN_LENGTH = 2;/* &LocalOptionConstant'LENGTH_DEALER_ID& */	public static final int LENGTH_DEALER_ID = 3;/* &LocalOptionConstant'MIN_DEALERID& */	public static final int MIN_DEALERID = 0;/* &LocalOptionConstant'MAX_DEALERID& */	public static final int MAX_DEALERID = 999;/* &LocalOptionConstant'LENGTH_SUBCON_ID& */	public static final int LENGTH_SUBCON_ID = 3;/* &LocalOptionConstant'MIN_SUBCON_ID& */	public static final int MIN_SUBCON_ID = 0;/* &LocalOptionConstant'MAX_SUBCON_ID& */	public static final int MAX_SUBCON_ID = 999;/* &LocalOptionConstant'LENGTH_LIENHOLDER_ID& */	public static final int LENGTH_LIENHOLDER_ID = 3;/* &LocalOptionConstant'TXT_CONTACT_PERSON_COLON& */	public static final String TXT_CONTACT_PERSON_COLON =		"Contact PersonOld:";/* &LocalOptionConstant'TXT_DEALER_ID_3DIGITS_COLON& */	public static final String TXT_DEALER_ID_3DIGITS_COLON =		"Dealer Id (three digits):";/* &LocalOptionConstant'TXT_TELEPHONE_10DIGITS_COLON& */	public static final String TXT_TELEPHONE_10DIGITS_COLON =		"Telephone Number (ten digits):";/* &LocalOptionConstant'MSG_USER_NAME_NOT_ACCEPTED& */	public static final String MSG_USER_NAME_NOT_ACCEPTED =		"New User Name was not accepted";/* &LocalOptionConstant'CTL001_FRAME_TITLE_USER_NAME_REJECTED& */	public static final String CTL001_FRAME_TITLE_USER_NAME_REJECTED =		"New User Name Rejected       CTL001";/* &LocalOptionConstant'TXT_USER_NAME_CHANGED& */	public static final String TXT_USER_NAME_CHANGED =		"User Name has changed to ";/* &LocalOptionConstant'MSG_TO_COMPLETE_PRESS_REVISE& */	public static final String MSG_TO_COMPLETE_PRESS_REVISE =		"To complete resetting \n the password you must"			+ " \n press \"REVISE\"";/* &LocalOptionConstant'TXT_EQUAL_CHECKED_DURING_SESSION& */	public static final String TXT_EQUAL_CHECKED_DURING_SESSION =		"=Checked during \n session";/* &LocalOptionConstant'TXT_MIDDLE_INIT_COLON& */	public static final String TXT_MIDDLE_INIT_COLON = "Middle Init:";/* &LocalOptionConstant'TXT_LAST_NAME_COLON& */	public static final String TXT_LAST_NAME_COLON = "Last Name:";/* &LocalOptionConstant'TXT_FIRST_NAME_COLON& */	public static final String TXT_FIRST_NAME_COLON = "First Name:";/* &LocalOptionConstant'TXT_EMPLOYEE_ID_COLON& */	public static final String TXT_EMPLOYEE_ID_COLON = "Employee Id:";/* &LocalOptionConstant'TXT_EMPLOYEE_NAME_COLON& */	public static final String TXT_EMPLOYEE_NAME_COLON =		"Employee Name:";/* &LocalOptionConstant'TXT_USER_NAME_COLON& */	public static final String TXT_USER_NAME_COLON = "User Name:";/* &LocalOptionConstant'TXT_SELECT_IF_NEEDED_COLON& */	public static final String TXT_SELECT_IF_NEEDED_COLON =		"Select if needed:";/* &LocalOptionConstant'TXT_USER_NAME_PREFIX& */	public static final String TXT_USER_NAME_PREFIX =		"Use User Name Prefix";/* &LocalOptionConstant'TXT_ENABLE_USER_NAME_SEARCH& */	public static final String TXT_ENABLE_USER_NAME_SEARCH =		"Enable User Name for Search";/* &LocalOptionConstant'TXT_RESET_PASSWORD& */	public static final String TXT_RESET_PASSWORD = "Reset Password";/* &LocalOptionConstant'MSG_RESET_PASSWORD& */	public static final String MSG_RESET_PASSWORD =		"Do you want to reset the password?";/* &LocalOptionConstant'MSG_EMP_ID_WILL_BE_DELETED& */	public static final String MSG_EMP_ID_WILL_BE_DELETED =		"Employee Id will be deleted. Do you \n want to continue?";/* &LocalOptionConstant'MSG_EMP_ID_WILL_BE_UPDATED& */	public static final String MSG_EMP_ID_WILL_BE_UPDATED =		"Access rights will be updated. Do you want to continue?";/* &LocalOptionConstant'MSG_NEW_EMP_WILL_BE_ADDED& */	public static final String MSG_NEW_EMP_WILL_BE_ADDED =		"A new Employee Id will be added. \nDo you want to continue?";/* &LocalOptionConstant'LENGTH_PREFIX_NUMBERS& */	public static final int LENGTH_PREFIX_NUMBERS = 3;/* &LocalOptionConstant'LENGTH_NAME_EMP_SEC& */	public static final int LENGTH_NAME_EMP_SEC = 15;/* &LocalOptionConstant'LENGTH_MIDDLE_INIT& */	public static final int LENGTH_MIDDLE_INIT = 1;/* &LocalOptionConstant'CHECK_ICON& */	public static final String CHECK_ICON =		new String("tickLocalOptions");/* &LocalOptionConstant'TXT_SUBCONTRACTOR_ID_COLON& */	public static final String TXT_SUBCONTRACTOR_ID_COLON =		"Subcontractor Id:";	// defect 10701/* &LocalOptionConstant'OPT008_COL_RPT_NUMBER& */	public final static int OPT008_COL_RPT_NUMBER = 0;/* &LocalOptionConstant'OPT008_COL_RPT_DESC& */	public final static int OPT008_COL_RPT_DESC = 1;/* &LocalOptionConstant'OPT008_COL_RPT_AUTOPRINT& */	public final static int OPT008_COL_RPT_AUTOPRINT = 2;	// end defect 10701 }/* #LocalOptionConstant# */