package com.txdot.isd.rts.services.util.constants;

/*
 * CacheConstant.java
 *  
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/05/2001	Add comments
 * K Harrell	08/05/2003	add constants for TERP 
 * 							defect 6447 
 * Ray Rowehl	07/14/2004	add constants for RSPS
 *							add RSPS_WS_STATUS
 *							defect 7135 Ver 5.2.1
 * K Harrell	04/04/2005	add constant for RTS_REG_ADDL_FEE
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	06/12/2005	add RTS_CLASS_TO_PLATE_CACHE,
 *  						RTS_PLATE_TO_STICKER_CACHE, 
 * 							RTS_CLASS_TO_PLATE_DESCRIPTION_CACHE
 * 							delete REGISTRATION_PLATE_STICKER_CACHE,
 *  						REGISTRATION_PLATE_STICKER_DESCRIPTION_CACHE
 * 							defect 8218 Ver 5.2.3
 * K Harrell	07/06/2005	delete GET_MISC_DATA, GET_SUBCON_DATA,
 * 							GET_ALL_SUBCON_DATA 
 * 							defect 8283 Ver 5.2.3
 * K Harrell	07/11/2005	delete RSPS_SYSUPDT
 * 							defect 8281 Ver 5.2.3
 * K Harrell	03/17/2006	delete PAYMENT_ACCOUNT_CACHE  
 * 							defect 8623 Ver 5.2.3  
 * K Harrell	01/31/2007	add PLT_TYPE_CACHE, PLT_GRP_ID_CACHE, 
 * 							ORG_NO_CACHE, PLT_SURCHARGE_CACHE
 * 							defect 9085  Ver Special Plates
 * K Harrell	02/10/2007	add VEH_CLASS_SPCL_PLT_DESC_CACHE
 *						 	defect 9085  Ver Special Plates
 * K Harrell	03/05/2007	add TXDOT_HOLIDAY_CACHE
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/26/2007	added SPCL_PLT_FXD_EXP_MO
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/11/2008	delete CASH_WORKSTATION_IDS_CACHE,
 *   						REGISTRATION_RENEWALS_CACHE
 * 							defect 8282 Ver Defect POS A  
 * K Harrell	03/27/2008	add FUNDS_CODES 
 * 							defect 6949 Ver Defect POS A
 * K Harrell	04/02/2008	add TTL_TRNSFR_ENT, TTL_TRNSFR_PNLTY_EXMPT_CD,
 * 							TTL_TRNSFR_PNLTY_FEE 
 * 							defect 9583 Ver Defect POS A
 * K Harrell	05/21/2008	delete TTL_TRNSFR_PNLTY_EXMPT_CD
 * 							defect 9583 Ver Defect POS A
 * K Harrell	06/22/2008	restore TTL_TRNSFR_PNLTY_EXMPT_CD
 * 							defect 9724 Ver Defect POS A  
 * 							defect 9583 Ver Tres Amigos PH B
 * Ray Rowehl	07/07/2008	Add entry for Web Services 
 * 							Service-Action-Version list.
 * 							add WS_SAV_LIST
 * 							defect 9675 Ver MyPlates_POS  
 * K Harrell	10/21/2008	Add entry for DSABLD_PLCRD_CUST_ID_TYPE, 
 * 							 DSABLD_PLCRD_DELETE_REASONS
 * 							add DSABLD_PLCRD_CUST_ID_TYPE_CACHE,
 *							 DSABLD_PLCRD_DEL_REASN_CACHE
 *							defect 9831 Ver Defect_POS_B 
 * K Harrell	02/26/2009  add CERTFD_LIENHLDR_CACHE
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	03/24/2010	add PLATE_SYMBOL_CACHE
 * 							defect 10366 Ver POS_640  
 * K Harrell	03/24/2010	add POSTAL_STATE_CACHE
 * 							defect 10396 Ver POS_640
 * K Harrell	04/03/2010	add OFFICE_TIMEZONE_CACHE 
 * 							defect 10427 Ver POS_640   
 * K Harrell	12/08/2010	add REGISTRATION_CLASS_FEE_GRP_CACHE
 * 							defect 10695 Ver 6.7.0 
 * K Harrell	12/29/2010	add WEB_AGENCY_BATCH_STATUS_CACHE, 
 *							WEB_AGENCY_TYPE_CACHE
 *							defect 10708 Ver 6.7.0 
 * K Harrell	01/05/2011	add VEH_COLOR_CACHE
 * 							defect 10712 Ver 6.7.0 
 * K Harrell	01/07/2011	add BUSINESS_PARTNER_CACHE
 * 							defect 10726 Ver 6.7.0 
 * K Harrell	01/16/2011	add BATCH_REPORT_MANAGEMENT_CACHE
 * 							defect 10701 Ver 6.7.0
 * K Harrell	10/08/2011	add HOLIDAY_CACHE
 * 							delete TXDOT_HOLIDAY_CACHE
 * 							defect 9919 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Function Id of cache classes.
 *
 * @version	6.9.0	 		10/08/2011
 * @author	Nancy Ting
 * <br>Creation Date: 		08/14/2001 15:00:16
 */

public class CacheConstant
{
	public final static int STATIC_CACHE_TABLE_CACHE = 1;
	public final static int ADMINISTRATION_CACHE_TABLE_CACHE = 2;
	public final static int ACCOUNT_CODES_CACHE = 3;
	public final static int ASSIGNED_WORKSTATION_IDS_CACHE = 4;
	public final static int COMMON_FEES_CACHE = 6;
	public final static int COMMERCIAL_VEHICLE_WEIGHTS_CACHE = 7;
	public final static int COUNTY_CALENDAR_YEAR_CACHE = 8;
	public final static int DEALERS_CACHE = 9;
	public final static int DELETE_REASONS_CACHE = 10;
	public final static int DMVTR_MESSAGE_CACHE = 11;
	public final static int DOCUMENT_TYPES_CACHE = 12;
	public final static int EMPLOYEE_CACHE = 13;
	public final static int ERROR_MESSAGES_CACHE = 14;
	public final static int INDICATOR_DESCRIPTIONS_CACHE = 15;
	public final static int INDICATOR_STOP_CODES_CACHE = 16;
	public final static int INVALID_LETTER_CACHE = 17;
	public final static int INVENTORY_PATTERNS_CACHE = 18;
	public final static int ITEM_CODES_CACHE = 19;
	public final static int LIENHOLDERS_CACHE = 20;
	public final static int MISCELLANEOUS_CACHE = 21;
	public final static int OFFICE_CODES_CACHE = 22;
	public final static int OFFICE_IDS_CACHE = 23;
	public final static int OWNER_EVIDENCE_CODES_CACHE = 24;
	public final static int PASSENGER_FEES_CACHE = 25;
	public final static int PAYMENT_STATUS_CODES_CACHE = 26;
	public final static int PAYMENT_TYPE_CACHE = 27;
	public final static int REGISTRATION_CLASS_CACHE = 28;
	public final static int REGISTRATION_WEIGHT_FEES_CACHE = 32;
	public final static int REPORT_CATEGORY_CACHE = 33;
	public final static int REPORTS_CACHE = 34;
	public final static int SALES_TAX_CATEGORY_CACHE = 35;
	public final static int SECURITY_CACHE = 36;
	public final static int SUBCONTRACTOR_CACHE = 37;
	public final static int SUBSTATION_CACHE = 38;
	public final static int SUBSTATION_SUBSCRIPTION_CACHE = 39;
	public final static int TAX_EXEMPT_CODE_CACHE = 40;
	public final static int TRANSACTION_CODES_CACHE = 41;
	public final static int VEHICLE_BODY_TYPES_CACHE = 42;
	public final static int VEHICLE_CLASS_REGISTRATION_CLASS_CACHE = 43;
	public final static int VEHICLE_DIESEL_TON_CACHE = 44;
	public final static int VEHICLE_MAKES_CACHE = 45;

	public final static int REFRESH_STATIC_CACHE_AT_SERVER = 47;

	public final static int PRODUCT_SERVICE_CACHE = 51;
	public final static int CREDIT_CARD_FEES_CACHE = 52;
	// defect 6447 
	public final static int TITLE_TERP_FEE_CACHE = 53;
	public final static int TITLE_TERP_PERCENT_CACHE = 54;
	// end defect 6447

	// defect 7135
	public final static int RSPS_WS_STATUS = 55;
	// end defect 7135

	// defect 8104
	public final static int REGISTRATION_ADDITIONAL_FEE_CACHE = 57;
	// end defect 8104 

	// defect 8218 
	public final static int CLASS_TO_PLATE_CACHE = 58;
	public final static int PLATE_TO_STICKER_CACHE = 59;
	public final static int CLASS_TO_PLATE_DESCRIPTION_CACHE = 60;
	// end defect 8218 

	// defect 9085 
	public final static int PLT_TYPE_CACHE = 61;
	public final static int PLT_GRP_ID_CACHE = 62;
	public final static int ORG_NO_CACHE = 63;
	public final static int PLT_SURCHARGE_CACHE = 64;
	public final static int VEH_CLASS_SPCL_PLT_TYPE_DESC_CACHE = 65;
	
	// defect 9919 
	//public final static int TXDOT_HOLIDAY_CACHE = 66;
	public final static int HOLIDAY_CACHE = 66;
	// end defect 9919 
	
	public final static int SPCL_PLT_FXD_EXP_MO = 67;
	// end defect 9085

	// defect 6949 
	public final static int FUNDS_CODES_CACHE = 68;
	// end defect 6949  

	// defect 9583 
	public final static int TTL_TRNSFR_ENT_CACHE = 69;
	public final static int TTL_TRNSFR_PNLTY_FEE_CACHE = 70;
	// end defect 9583

	// defect 9724 
	public final static int TTL_TRNSFR_PNLTY_EXMPT_CD_CACHE = 71;
	// end defect 9274  

	// defect 9675
	public final static int WS_SAV_LIST = 71;
	// end defect 9675

	// defect 9831
	public final static int DSABLD_PLCRD_CUST_ID_TYPE_CACHE = 72;
	public final static int DSABLD_PLCRD_DEL_REASN_CACHE = 73;
	// end defect 9831
	
	// defect 9969 
	public final static int CERTFD_LIENHLDR_CACHE = 74;
	// end defect 9969 
	
	// defect 10366 
	public final static int PLATE_SYMBOL_CACHE = 75;
	// end defect 10366
	
	// defect 10396  
	public final static int POSTAL_STATE_CACHE = 76;
	// end defect 10396 
	
	// defect 10427 
	public final static int OFFICE_TIMEZONE_CACHE = 77; 
	// end defect 10427 
	
	// defect 10695 
	public final static int REGISTRATION_CLASS_FEE_GRP_CACHE = 78; 
	// end defect 10695 
	
	// defect 10708 
	public final static int WEB_AGENCY_BATCH_STATUS_CACHE = 79; 
	public final static int WEB_AGENCY_TYPE_CACHE = 80; 
	// end defect 10708 
	
	// defect 10712 
	public final static int VEHICLE_COLOR_CACHE = 81; 
	// end defect 10712 
	
	// defect 10726 
	public final static int BUSINESS_PARTNER_CACHE = 82;
	// end defect 10726 
	
	// defect 10701
	public final static int BATCH_REPORT_MANAGEMENT_CACHE = 83; 
	// end defect 10701 
	
 }