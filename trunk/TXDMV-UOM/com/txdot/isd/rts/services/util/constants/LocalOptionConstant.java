package com.txdot.isd.rts.services.util.constants;

/*
 * 
 * LocalOptionConstant.java
 *  
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	09/07/2003	Add handling for Active Directory Prefix.
 *							add USER_ID_MAX_LENGTH,
 *							USER_ID_MIN_LENGTH
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	12/30/2003	Rename USER_ID_MAX_LENGTH and 
 *									USER_ID_MIN_LENGTH
 *							to USER_NAME_MAX_LENGTH and 
 *									USER_NAME_MIN_LENGTH
 *							to reflect field name
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/19/2004	reflow to meet updated java standards
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/24/2004	add User Name Prefix length
 *							add USER_NAME_PREFIX_LENGTH
 *							defect 6445 Ver 5.1.6
 * Min Wang		07/14/2004	add constants for RSPS
 *							add GET_RSPS_IDS, GET_RSPS_UPDT, 
 *								PROCESS_RSPS_UPDT
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/02/2004	Add constant for RSPS Processing
 *							add GET_SYSUPDATE_LIST
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	11/11/2004	Set the max length for User Name Base Id
 *							depending on whether or not there is a
 *							prefix.
 *							add USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX,
 *								USER_NAME_BASE_ID_MAX_LENGTH_PREFIX
 *							defect 7604 Ver 5.2.2
 * K Harrell	01/14/2005 	Remove constants for UPDT_XXX as not
 *							used
 *							defect 7901 Ver 5.2.3 
 * K Harrell	01/14/2005	Correct spelling for RETREIVE_CREDIT_FEES
 *							defect 7902 Ver 5.2.3
 * K Harrell	01/14/2005	Remove unused constants for Login
 *							defect 7903 Ver 5.2.3 
 * Ray Rowehl	06/11/2005	Code Cleanup
 * 							remove extra * from file doc.
 * 							defect 7891 Ver 5.2.3
 * Ray Rowehl	08/24/2005	Add constants from Local Options
 * 							defect 7891 Ver 5.2.3
 * K Harrell	04/20/2007	Remove constants no longer required with
 * 							 new SystemProperties. 
 * 							delete COUNTY,REGION,VTR
 * 							defect 9085 Ver Special Plates 
 * K Harrell	09/02/2008  add RTS_CRDT_CARD_FEE, RTS_DEALERS,
 * 							 RTS_LIENHOLDERS, RTS_MISC,  RTS_PYMNT_ACCT,
 * 							 RTS_RSPS_WS_STATUS, RTS_SECURITY, 
 * 							 RTS_SUBCON, RTS_SUBSTA_SUBSCR 
 * 							defect 8721 Ver Defect_POS_B
 * J Zwiener	02/27/2009	add GENERATE_CERTFD_LIENHLDR_RPT
 * 							defect 9968 Ver Defect_POS_E  
 * Ray Rowehl	07/13/2009	Modify Login status constants to ensure
 * 							they are unique.
 * 							Remove unused constants.
 * 							add LOGIN_FAIL
 * 							delete INVALID_USERID_PASSWORD_COMBINATION_ERROR,
 * 								LOGIN_WRONG_PASSWORD
 * 							modify LOGIN_SUCCESS, 
 * 								LOGIN_SUCCESS_FROM_CACHE 
 * 							defect 10103 Ver Defect_POS_F
 * K Harrell	01/18/2011	add OPT008_COL_RPT_NUMBER, 
 * 								OPT008_COL_RPT_DESC, 
 * 								OPT008_COL_RPT_AUTOPRINT
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Local Options.
 *
 * @version	6.7.0 			01/18/2011
 * @author	Ashish Majahan
 * <br>Creation Date: 		08/13/2001 13:51:16
 */

public class LocalOptionConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public static final int PRNT_SECRTY_CHNG_RPT = 1;
	public static final int PRNT_EMP_SECRTY_RPT = 2;
	public static final int PRNT_EVENT_SECRTY_RPT = 3;
	public static final int PRNT_CONNECTION_STATUS_RPT = 4;
	public static final int PRNT_PUBLISHING_RPT = 5;
	public static final int PRNT_DLR_RPT = 6;
	public static final int PRNT_SUBCON_RPT = 7;
	public static final int PRNT_LIENHLDR_RPT = 8;
	public static final int ADD_DLR = 9;
	public static final int DEL_DLR = 11;
	public static final int ADD_SUBCON = 12;
	public static final int DEL_SUBCON = 13;

	public static final int ADD_LIENHLDR = 15;
	public static final int GET_DLR_DATA_ONID = 16;
	public static final int GET_SUBCON_DATA_ONID = 17;

	public static final int GENERATE_DLR_RPT = 19;
	public static final int GENERATE_ACTIVE_RPT = 20;
	public static final int GENERATE_SUBCON_RPT = 21;
	public static final int GENERATE_LIENHLDR_RPT = 22;
	public static final int REVISE_DLR = 23;
	public static final int REVISE_LIENHLDR = 24;
	public static final int REVISE_SUBCON = 25;
	public static final int GENERATE_SECRTY_CHNG_RPT = 26;
	public static final int GENERATE_EMP_SECRTY_RPT = 27;
	public static final int GENERATE_EVENT_SECRTY_RPT = 28;
	public static final int GET_EMP_DATA_ONID = 29;
	public static final int CANCEL_EMP_ACCS_RIGHTS = 30;
	public static final int ADD_EMP_ACCS_RIGHTS = 31;
	public static final int REVISE_EMP_ACCS_RIGHTS = 32;
	public static final int DEL_EMP_ACCS_RIGHTS = 33;
	public static final int SUPV_OVERIDE = 34;
	public static final int RESET_PASSWORD = 35;
	public static final int GENERATE_PUBLISHING_RPT = 37;
	public static final int SERVER_PLUS_ENABLE_DISABLE = 38;
	public static final int UPDT_PUBLISHING = 39;
	public static final int GET_LIENHLDR_DATA_ONID = 40;
	public static final int DEL_LIENHLDR = 41;
	public static final int LOGIN = 42;
	public static final int CHANGE_PASS = 43;
	public static final int PUB_UPDT = 44;
	public static final int REVISE_PUB = 45;
	public static final int SERVER_PLUS = 46;
	public static final int SUPER_OVERRIDE_LOOKUP = 47;

	//Used to get the security data for current employee
	public static final int ADD_CURRENT_EMP_DATA_ONID = 48;
	public static final int RETRIEVE_CREDIT_FEES = 49;
	public static final int UPDATE_CREDIT_DB = 50;

	/**  Codes for Payment Account Update */
	public static final int GET_PAYMENT_ACCOUNTS = 60;
	public static final int ADD_PAYMENT_ACCOUNT = 61;
	public static final int REVISE_PAYMENT_ACCOUNT = 62;
	public static final int DELETE_PAYMENT_ACCOUNT = 63;

	// Codes for handling AD Interface
	public final static int AD_ADD_USER = 70;
	public final static int AD_UPDT_USER = 71;
	public final static int AD_DEL_USER = 72;
	public final static int AD_RESET_PSWD = 73;

	// defect 7135
	// Codes for handling RSPS Update
	public final static int GET_RSPS_IDS = 74;
	public final static int GET_RSPS_UPDT = 75;
	public final static int PROCESS_RSPS_UPDT = 76;
	public final static int GET_SYSUPDATE_LIST = 77;
	// end defect 7135

	// defect 9968
	public final static int GENERATE_CERTFD_LIENHLDR_RPT = 78;
	// end defect 9968

	// defect 10701 
	public final static int UPDATE_BATCH_RPT_MGMT = 79;
	public final static int GET_BATCH_RPT_MGMT = 80;
	// end defect 10701 

	// defect 8721 
	public final static String RTS_CRDT_CARD_FEE = "RTS_CRDT_CARD_FEE";
	public final static String RTS_DEALERS = "RTS_DEALERS";
	public final static String RTS_LIENHOLDERS = "RTS_LIENHOLDERS";
	public final static String RTS_MISC = "RTS_MISC";
	public final static String RTS_PYMNT_ACCT = "RTS_PYMNT_ACCT";
	public final static String RTS_RSPS_WS_STATUS =
		"RTS_RSPS_WS_STATUS";
	public final static String RTS_SECURITY = "RTS_SECURITY";
	public final static String RTS_SUBCON = "RTS_SUBCON";
	public final static String RTS_SUBSTA_SUBSCR = "RTS_SUBSTA_SUBSCR";
	// end defect 8721

	// defect 10701 
	public final static String RTS_BATCH_RPT_MGMT =
		"RTS_BATCH_RPT_MGMT";
	// end defect 10701 

	// defect 10103

	///**  
	// * Error Code returned when logon fails in validating userid/
	// * password.
	// */
	//public final static int INVALID_USERID_PASSWORD_COMBINATION_ERROR =
	//	54;
	// end defect 10103

	// defect 9085 
	// public final static int COUNTY = 3;
	// public final static int REGION = 2;
	// public final static int VTR = 1;
	// end defect 9085 

	// defect 10103
	public final static int LOGIN_FAIL = 81;
	public final static int LOGIN_SUCCESS = 80;
	public final static int LOGIN_SUCCESS_FROM_CACHE = 89;
	// end defect 10103

	// defect 6445 
	// UserId Lengths
	public final static int USER_NAME_MAX_LENGTH = 11;
	public final static int USER_NAME_MIN_LENGTH = 6;
	public final static int USER_NAME_PREFIX_LENGTH = 4;
	public final static int EMP_ID_MAX_LENGTH = 7;

	// defect 7604
	// Allow non-prefix base ids to to a length of 8.
	// Base ids with a prefix can only be 7.
	public final static int USER_NAME_BASE_ID_MAX_LENGTH_PREFIX =
		EMP_ID_MAX_LENGTH;
	public final static int USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX = 8;
	// end defect 7604

	public final static int EMP_ID_MIN_LENGTH = 2;

	public static final int LENGTH_DEALER_ID = 3;
	public static final int MIN_DEALERID = 0;
	public static final int MAX_DEALERID = 999;
	public static final int LENGTH_SUBCON_ID = 3;
	public static final int MIN_SUBCON_ID = 0;
	public static final int MAX_SUBCON_ID = 999;

	public static final int LENGTH_LIENHOLDER_ID = 3;

	public static final String TXT_CONTACT_PERSON_COLON =
		"Contact PersonOld:";
	public static final String TXT_DEALER_ID_3DIGITS_COLON =
		"Dealer Id (three digits):";
	public static final String TXT_TELEPHONE_10DIGITS_COLON =
		"Telephone Number (ten digits):";

	public static final String MSG_USER_NAME_NOT_ACCEPTED =
		"New User Name was not accepted";

	public static final String CTL001_FRAME_TITLE_USER_NAME_REJECTED =
		"New User Name Rejected       CTL001";

	public static final String TXT_USER_NAME_CHANGED =
		"User Name has changed to ";

	public static final String MSG_TO_COMPLETE_PRESS_REVISE =
		"To complete resetting \n the password you must"
			+ " \n press \"REVISE\"";

	public static final String TXT_EQUAL_CHECKED_DURING_SESSION =
		"=Checked during \n session";

	public static final String TXT_MIDDLE_INIT_COLON = "Middle Init:";

	public static final String TXT_LAST_NAME_COLON = "Last Name:";

	public static final String TXT_FIRST_NAME_COLON = "First Name:";

	public static final String TXT_EMPLOYEE_ID_COLON = "Employee Id:";

	public static final String TXT_EMPLOYEE_NAME_COLON =
		"Employee Name:";

	public static final String TXT_USER_NAME_COLON = "User Name:";

	public static final String TXT_SELECT_IF_NEEDED_COLON =
		"Select if needed:";

	public static final String TXT_USER_NAME_PREFIX =
		"Use User Name Prefix";

	public static final String TXT_ENABLE_USER_NAME_SEARCH =
		"Enable User Name for Search";

	public static final String TXT_RESET_PASSWORD = "Reset Password";

	public static final String MSG_RESET_PASSWORD =
		"Do you want to reset the password?";

	public static final String MSG_EMP_ID_WILL_BE_DELETED =
		"Employee Id will be deleted. Do you \n want to continue?";

	public static final String MSG_EMP_ID_WILL_BE_UPDATED =
		"Access rights will be updated. Do you want to continue?";

	public static final String MSG_NEW_EMP_WILL_BE_ADDED =
		"A new Employee Id will be added. \nDo you want to continue?";

	public static final int LENGTH_PREFIX_NUMBERS = 3;

	public static final int LENGTH_NAME_EMP_SEC = 15;

	public static final int LENGTH_MIDDLE_INIT = 1;

	public static final String CHECK_ICON =
		new String("tickLocalOptions");

	public static final String TXT_SUBCONTRACTOR_ID_COLON =
		"Subcontractor Id:";

	// defect 10701
	public final static int OPT008_COL_RPT_NUMBER = 0;
	public final static int OPT008_COL_RPT_DESC = 1;
	public final static int OPT008_COL_RPT_AUTOPRINT = 2;
	// end defect 10701 
}
