package com.txdot.isd.rts.services.util.constants;

/*
 * GeneralConstant
 *
 * (c) Texas Department of Transportation  2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/05/2001	Add comments
 * RS Chandel	11/27/2001	Added WEBAPPS for Phase II
 * B Arredondo	12/18/2002	Fixing Defect# 5147. Added Code 
 *                              for screen title on MSDialog
 * Ray Rowehl	07/12/2003	Add constant for ping.
 *							Defect 6110
 * Ray Rowehl	06/17/2005	Add JavaDoc Statements to document the 
 * 							values.
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add constants for Module Names.  This is 
 * 							copied initially from Local Options.
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		04/02/2007	Added constant for Help.
 * 							add HELP
 * 							defect 7768 Ver Broadcast Message
 * K Harrell	02/12/2007	Added constants for Special Plates
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	08/16/2007	Change SpecialPlates to 18
 * 							modify SPECIALPLATES
 * 							defect 9116 Ver Special Plates
 * K Harrell	02/05/2008	Added constant for VISION21 
 * 							defect 9546 Ver 3 Amigos PH A 
 *----------------------------------------------------------------------
 */

/**
 * Constants to define module identifiers
 *
 * @version	Special Plates	02/05/2008 
 * @author	Nancy Ting
 * <br>Creation Date:		08/14/2001 17:12:22
 */

public class GeneralConstant
{
	// Constants for module names 
	// ordered by name
	/**
	 * The called module is Accounting.
	 * 
	 * <p>The menu item for this is Accounting.
	 * @see TXT_MODULE_NAME_ACCOUNTING
	 * 
	 * <p>This value is 5.
	 */
	public static final int ACCOUNTING = 5;
	/**
	 * The text naming this module.
	 * 
	 * @see ACCOUNTING
	 */
	public static final String TXT_MODULE_NAME_ACCOUNTING =
		"Accounting";

	/**
	 * The called module is Funds.
	 * 
	 * <p>The menu item for this is Funds.
	 * @see TXT_MODULE_NAME_FUNDS
	 * 
	 * <p>This value is 3. 
	 */
	public static final int FUNDS = 3;
	/**
	 * The text naming this module.
	 * 
	 * @see FUNDS
	 */
	public static final String TXT_MODULE_NAME_FUNDS = "Funds";

	/**
	 * The called module is Common.
	 * 
	 * <p>There is no menu entry for this module.
	 * This module handles tasks that are common across modules.  
	 * A good example of this would be Fees.
	 * 
	 * <p>This module is also known as Non Event Centric.
	 * 
	 * <p>This value is 6. 
	 */
	public final static int COMMON = 6;

	/**
	 * The called module is General.
	 * 
	 * <p>There are no menu items for this module.
	 * This module is usually used for cache build.
	 * 
	 * <p>This value is 13.
	 */
	// cache resides in general module
	public static final int GENERAL = 13;

	/**
	 * The called module is Inquiry.
	 * 
	 * <p>The menu item for this is Customer, Inquiry.
	 * @see TXT_MODULE_NAME_INQUIRY
	 * 
	 * <p>This value is 7.
	 */
	public final static int INQUIRY = 7;
	/**
	 * The text naming this module.
	 * @see INQUIRY
	 */
	public static final String TXT_MODULE_NAME_INQUIRY = "Inquiry";

	//	Code for PhaseII, WebApps
	/**
	 * The called module is Internet.
	 * 
	 * <p>This value is 16.
	 */
	public static final int INTERNET = 16;
	/**
	 * The called module is Internet Address Change.
	 * 
	 * <p>This value is 14.
	 */
	public static final int INTERNET_ADDRESS_CHANGE = 14;
	/**
	 * The called module is Internet Registration Renewal.
	 * 
	 * <p>This value is 15.
	 */
	public static final int INTERNET_REG_REN = 15;
	/**
	 * The called module is Internet Registration Renewal Processing.
	 * 
	 * <p>This value is 16.
	 */
	public static final int INTERNET_REG_REN_PROCESSING = 16;

	/**
	 * The called module is Help Processing.
	 * 
	 * <p>This value is 17.
	 */
	public static final int HELP = 17;

	/**
	 * The called module is Inventory.
	 * 
	 * <p>The menu item for this is Inventory.
	 * @see TXT_MODULE_NAME_INVENTORY
	 * 
	 * <p>This value is 12.
	 */
	public static final int INVENTORY = 12;
	/**
	 * The text naming this module.
	 * @see INVENTORY
	 */
	public static final String TXT_MODULE_NAME_INVENTORY = "Inventory";

	/**
	 * The called module is Local Options.
	 * 
	 * <p>The menu item for this is Local Options.
	 * @see TXT_MODULE_NAME_LOCAL_OPTIONS
	 * 
	 * <p>This value is 1.
	 */
	public static final int LOCAL_OPTIONS = 1;
	/**
	 * The text naming this module.
	 * @see LOCAL_OPTIONS
	 */
	public static final String TXT_MODULE_NAME_LOCAL_OPTIONS =
		"Local Options";

	/**
	 * The called module is Misc.
	 * 
	 * The menu item for this is Misc.
	 * @see TXT_MODULE_NAME_MISCELLANEOUS
	 * 
	 * This value is 8.
	 */
	public final static int MISC = 8;
	/**
	 * The text naming this module.
	 * @see MISC
	 */
	public static final String TXT_MODULE_NAME_MISCELLANEOUS =
		"Miscellaneous";

	/**
	 * The called module is Misc. Reg.
	 * 
	 * <p>The menu item is Customer, Misc. Reg.
	 * @see TXT_MODULE_NAME_MISCELLANEOUS_REGISTRATION
	 * 
	 * <p>This value is 9.
	 */
	public final static int MISCELLANEOUSREG = 9;
	/**
	 * The text naming this module.
	 * @see MISCELLANEOUSREG
	 */
	public static final String TXT_MODULE_NAME_MISCELLANEOUS_REGISTRATION =
		"Miscellaneous Registration";

	/**
	 * The called module is Registration.
	 * 
	 * <p>The menu item for this is Customer, Registration.
	 * @see TXT_MODULE_NAME_REGISTRATION_ONLY
	 * 
	 * <p>This value is 2.
	 */
	public static final int REGISTRATION = 2;
	/**
	 * The text naming this module.
	 * @see REGISTRATION
	 */
	public static final String TXT_MODULE_NAME_REGISTRATION_ONLY =
		"Registration Only";

	/**
	 * Used by the application to check on server status after
	 * a down situation was encountered.
	 * 
	 * <p>This value is 0.
	 */
	public static final int PING = 0;

	/**
	 * The called module is Reports.
	 * 
	 * <p>The menu item is Reports.
	 * @see TXT_MODULE_NAME_REPORTS
	 * 
	 * <p>This value is 10.
	 */
	public final static int REPORTS = 10;
	/**
	 * The text naming this module.
	 * @see REPORTS
	 */
	public static final String TXT_MODULE_NAME_REPORTS = "Reports";

	/**
	 * The called module is Title.
	 * 
	 * <p>The menu item for this is Customer, Title
	 * @see TXT_MODULE_NAME_TITLE_REGISTRATION
	 * 
	 * <p>This value is 4.
	 */
	public static final int TITLE = 4;
	/**
	 * The text naming this module.
	 * @see TITLE
	 */
	public static final String TXT_MODULE_NAME_TITLE_REGISTRATION =
		"Title/Registration";
	/**
	 * This is a sub module of Title.
	 * @see TITLE
	 */
	public static final String TXT_MODULE_SUB_TITLE_STATUS_CHANGE =
		"Status Change";

	/**
	 * The called module is System Control Batch.
	 * 
	 * <p>There is no menu item associated 
	 */
	public final static int SYSTEMCONTROLBATCH = 11;

	//Code for screen title on MSDialog 
	public static final String DTA004 = "Copy Confirmation    DTA004";

	public static final String ERROR_DESC = "ERROR OBJECT";

	/**
	 * The called module is Special Plates
	 * 
	 * <p>The menu item for this is Special Plates.
	 * @see TXT_MODULE_NAME_SPECIALPLATES
	 * 
	 * <p>This value is 18.
	 */
	public static final int SPECIALPLATES = 18;

	/**
	 * The text naming this module.
	 * 
	 * @see SPECIAL PLATES 
	 */
	public static final String TXT_MODULE_NAME_SPECIALPLATES =
		"Special Plates";

	/**
	 * The called module is Vision21
	 * 
	 * <p>This value is 19.
	 */
	public static final int VISION21 = 19;

	/**
	 * This is used to indicate there are no deep business functions
	 * to be called.  We are just moving on the the next screen.
	 */
	public static final int NO_DATA_TO_BUSINESS = -1;
}
