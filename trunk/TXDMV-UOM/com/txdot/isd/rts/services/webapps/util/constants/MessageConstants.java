package com.txdot.isd.rts.services.webapps.util.constants;

/*
 *
 * MessageConstants.java 
 *
 * c Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown  	01/16/2004  Added message VEHICLE_ALREADY_IN_CART, 
 * 							Message number 206.
 *           	           	defect 6698 Ver 5.1.5 Fix 2
 * B Brown  	06/16/2004 	Added message EMPTY_SHOPPING_CART_WARNING, 
 *                   	   	Message number 207.
 *                    	  	defect 6790 Ver 5.2.0
 * K Harrell	10/31/2005	Java 1.4 Work 
 * 							defect 7889 Ver 5.2.3 
 * B Brown  	01/26/2006  Added message number 218
 *							MSG_RENEWAL_IN_PROCESS
 *                      	defect 8365 Ver 5.2.2 fix 8. 
 * B Brown  	03/07/2007  Adding messages for Internet Order
 *                      	defect 9120 Ver Special Plates. 
 * B Brown		08/23/2007	Adding msg 346 for PLP character limit
 * 							defect 9119 Ver Special Plates.
 * B Brown		04/17/2008	Adding msg 347 to let the user know their
 * 							Special plate payment is in process.
 * 							defect 9621 Ver 5.6.1.
 * B Brown		02/25/2009	Adding msg 351 to let the user know there
 * 							was a problem updating their shopping cart
 * 							info to our database.
 * 							Also add back in msg 219 which was part of
 * 							defect 9621.
 * 							defect 9945 Ver Defect_POS_E.
 * B Brown		07/12/2010	Adding msg 252 to let the user know their
 * 							internet plate registration may not have
 * 							a payment that can be completed.
 * 							Add MSG_CHECK_PYMNT_STATUS.
 * 							defect 10541 Ver POS_650 
 * ---------------------------------------------------------------------
 */

/**  
 * This class is responsible for maintaining browser error messages
 * for internet registration and address changes.
 * The message text is actually in the txdot_messages.txt file, that
 * in production, is on the Tx Online web server.
 *
 * @version POS_650				07/12/2010
 * @author 	C Chen
 * <br>Creation Date: 			Unknown/2001
 */

public class MessageConstants
{

	// All these messages are in the txt_messages.txt file

	// Common Messages
	public final static String MSG_MAINFRAME_DOWN = "1";
	public final static String MSG_VEHICLE_NOT_FOUND = "2";
	public final static String MSG_CANNOT_PROCESS_VEHICLE = "3";
	public final static String MSG_COUNTY_INACTIVE = "4";
	public final static String MSG_SYSTEM_ERROR = "99";

	// Address change
	public final static String MSG_SUCCESSFUL_ADDRESS_CHANGE = "100";
	public final static String MSG_NOT_ELIGIBLE_FOR_ADDRESS_CHANGE =
		"101";
	public final static String MSG_ADDR_CHG_THANK_YOU = "150";

	// Registration renewal
	public final static String MSG_SUCCESSFUL_REG_RENEWAL = "200";
	public final static String MSG_NOT_ELIGIBLE_FOR_REG_RENEWAL = "201";
	public final static String MSG_RECENTLY_RENEWED = "202";
	// unlikely, passed an empty vector for registration renewal
	public final static String MSG_NO_REG_RENEWAL_TO_MAKE = "203";
	public final static String MSG_NOT_WITHIN_90_DAY_WINDOW = "205";
	//defect 6698
	public final static String VEHICLE_ALREADY_IN_CART = "206";
	//end defect 6698

	//defect 6790
	public final static String EMPTY_SHOPPING_CART_WARNING = "207";
	//end defect 6790
	public final static String MSG_REG_EXPIRED = "210";
	
	public static final String MSG_SELECT_COUNTY = "211";

	public final static String MSG_CREDIT_CARD_DENIED = "215";
	public final static String MSG_CREDIT_CARD_MISSING_INFO = "216";
	public final static String MSG_EPAY_NO_RESPONSE = "217";
	// defect 8365
	public final static String MSG_RENEWAL_IN_PROCESS="218";
	// end defect 8365

	public final static String MSG_REG_REN_THANK_YOU = "250";
	// defect 9945
	public final static String MSG_SHOPPING_CART_UPDATE_ERROR = "251";
	// end defect 9945
	
	// defect 10541
	public final static String MSG_CHECK_PYMNT_STATUS = "252";
	// end defect 10541
	
	// defect 9120
	public static final String MSG_THANK_YOU_ORDER = "301";
	public static final String MSG_PLP_AVA = "302";
	public static final String MSG_PLP_NOT_AVA = "303";
	public static final String MSG_SP_NOT_AVA = "304";
	public static final String MSG_PLP_CHK_ENTER_PLT = "305";
	public static final String MSG_PLP_NO_LONGER_AVA = "306";
	public static final String MSG_PLP_CHK_MAX_LENGTH = "307";
	public static final String MSG_ENTER_PLP_CHOICE = "308";
	public static final String MSG_PLP_CHOICE_INVALID_CHAR = "309";
	public static final String MSG_VEHICLE_NOT_VALIDATED = "310";
	public static final String MSG_CART_EMPTY = "311";
	public static final String MSG_NO_RECEIPT_ITEM = "312";
	public static final String MSG_CART_ITEM_DELETED = "313";
	public static final String MSG_SESSION_EXPIRED = "314";
	public static final String MSG_CONFIRM_CANCEL = "315";
	public static final String MSG_CAN_NOT_ADD_TO_CART = "316";
	public static final String MSG_INSERT_FAILED = "317";
	public static final String MSG_CART_ITEM_NOT_EXIST = "318";
	public static final String MSG_CART_FAILED_TO_GET_NEXT = "319";
	public static final String MSG_ORDER_SELECTION_EMPTY = "320";
	public static final String MSG_FORM_ERROR = "321";
	public static final String MSG_FORM_ERROR_FIELD_NOT_VALID_LEN = "322";
	public static final String MSG_FORM_ERROR_FIELD_NOT_VALID_NUM = "323";
	public static final String MSG_FORM_ERROR_NAME = "324";
	public static final String MSG_FORM_ERROR_NAME2 = "325";
	public static final String MSG_FORM_ERROR_ADDRESS = "326";
	public static final String MSG_FORM_ERROR_ADDRESS2 = "327";
	public static final String MSG_FORM_ERROR_CITY = "328";
	public static final String MSG_FORM_ERROR_STATE = "329";
	public static final String MSG_FORM_ERROR_CNTY = "330";
	public static final String MSG_FORM_ERROR_ZIP = "331";
	public static final String MSG_FORM_ERROR_ZIP4 = "332";
	public static final String MSG_FORM_ERROR_AREA_CODE = "333";
	public static final String MSG_FORM_ERROR_PHONE = "334";
	public static final String MSG_FORM_ERROR_EMAIL = "335";
	public static final String MSG_FORM_ERROR_WRONG_BROWSER = "336";
	public static final String MSG_FORM_ERROR_PLP_RULE_3_1 = "337";
	public static final String MSG_FORM_ERROR_PLP_RULE_5_1 = "338";
	public static final String MSG_PLT_NA = "339";
	public static final String MSG_GRP_NA = "340";
	public static final String MSG_FORM_ERROR_BILL_NAME = "341";
	public static final String MSG_FORM_ERROR_CARD_NUM = "342";
	public static final String MSG_FORM_ERROR_CARD_SEC_CODE = "343";
	public static final String MSG_FORM_ERROR_INVALID_AMT = "344";
	public static final String MSG_FORM_ERROR_BILL_NAME_NOT_VALID = "345";
	//	defect 9119
	public static final String MSG_FORM_ERROR_PLP_RULE_NOMORE_THAN_6 = "346";
	//	end defect 9119
	// defect 9621
	public static final String MSG_SPECIAL_PLATE_PAYMENT_IN_PROCESS = "347";
	public static final String MSG_RENEWAL_PAYMENT_IN_PROCESS = "219";
	// end defect 9621


	//	help screens
	public static final String HELP_SINGLE_SET = "351";
	public static final String HELP_PERSONALIZE = "352"; 
	public static final String HELP_CHECK_AVAILABILITY = "353";
	public static final String HELP_PLATENO = "354";
	public static final String HELP_LAST4VIN = "355";
	public static final String HELP_PERSONALIZE_PULLDOWNS = "356";
	public static final String HELP_ADDL_SET = "357";
	public static final String HELP_ISA = "358";
	// end defect 9120

	/**
	 * MessageConstants constructor comment.
	 */
	public MessageConstants()
	{
		super();
	}
}
