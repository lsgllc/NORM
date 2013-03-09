package com.txdot.isd.rts.services.webapps.util.constants;
/*
 * ServiceConstants.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/05/2007	Created Class.
 * 							defect 9120 Ver Special Plates
 * Bob B.		08/15/2007	Added SPI_ACTION_GET_PLATE_INFO_FOR_DEV
 * 							defect 9119 Ver Special Plates
 * Bob B.		01/16/2008	Add constant for getting SP images
 * 							add SPI_ACTION_GET_PLATE_IMAGES,
 * 								SPI_ACTION_GET_PLATE_IMAGE_NAMES
 * 							defect 9473 Ver Tres Amigos Prep 
 * ---------------------------------------------------------------------
 */

/**
 * Constants used on both the Web Service and Web Service Client.
 *
 * @version	Tres Amigos Prep	01/16/2008
 * @author	jseifer
 * <br>Creation Date:			03/05/2007 15:19:00
 */
public class ServiceConstants
{
	// Abstract Response - application-level acknowledgement codes
	public static final String AR_ACK_FAILURE = "Failure";
	public static final String AR_ACK_FAILURE_WITH_WARNING =
		"FailureWithWarning";
	public static final String AR_ACK_SUCCESS = "Success";
	public static final String AR_ACK_SUCCESS_WITH_WARNING =
		"SuccessWithWarning";

	// Module  - County Info Constants
	public static final int CI_ACTION_GET_ALL_CNTY_INFO = 1;

	// Error - Severity Codes
	public final static String E_SEVERITY_ERROR = "Error";
	public final static String E_SEVERITY_WARNING = "Warning";
	
	//	Module  - Special Plate Info Constants
	public static final int SPI_ACTION_GET_GROUP_INFO = 5;
	public static final int SPI_ACTION_GET_GROUPS = 3;
	public static final int SPI_ACTION_GET_MENU_ITEMS = 2;
	public static final int SPI_ACTION_GET_PLATE_INFO = 4;
	public static final int SPI_ACTION_GET_PLATE_INFO_FOR_DEV = 6;
	// defect 9473
	public static final int SPI_ACTION_GET_PLATE_IMAGES = 7;
	public static final int SPI_ACTION_GET_PLATE_IMAGE_NAMES = 18;
	// end defect 9473
	// Fees Constants
	public static final String SPI_FEE_ADD_SET = "ADDITIONAL SET FEE";
	public static final String SPI_FEE_ADD_SET_RENEW =
		"ADDITIONAL SET RENEWAL FEE";
	public static final String SPI_FEE_CONV = "CONVENIENCE FEE";
	public static final String SPI_FEE_PERSONALIZATION =
		"PERSONALIZATION FEE";
	public static final String SPI_FEE_REPLACEMENT =
		"REPLACEMENT FEE";
	public static final String SPI_FEE_SINGLE_SET = "SINGLE SET FEE";
	public static final String SPI_FEE_SINGLE_SET_RENEW =
		"SINGLE SET RENEW FEE";
	// Design Types
	public static final String SPI_PLT_DESIGN_TYPE_MOTORCYCLE = "mc";
	public static final String SPI_PLT_DESIGN_TYPE_MOTORCYCLE_WHOLE =
		"mc whole";
	public static final String SPI_PLT_DESIGN_TYPE_QUARTER = "1/4";
	public static final String SPI_PLT_DESIGN_TYPE_THIRD = "1/3";
	public static final String SPI_PLT_DESIGN_TYPE_WHOLE = "whole";

	// Module  - Transaction Access Constants
	public static final int TA_ACTION_INSERT_TRANS = 8;
	public static final int TA_ACTION_UPDATE_TRANS = 9;
	public static final int VI_ACTION_CANCEL_ORDER = 16;
	public static final int VI_ACTION_CONFIRM_HOLD = 12;
	public static final int VI_ACTION_CONFIRM_ORDER = 13;

	// Module  - Virtual Inventory Constants
	public static final int VI_ACTION_GET_NEXT_ITEM = 10;
	public static final int VI_ACTION_RELEASE_HOLD = 11;
	public static final int VI_ACTION_VALIDATE_PLP = 14;
	public static final int VI_ACTION_VALIDATE_PLP_NORESERVE = 15;

	// Module  - Vehicle Validation Constants
	public static final int VV_ACTION_VALIDATE_SP = 17;
}
