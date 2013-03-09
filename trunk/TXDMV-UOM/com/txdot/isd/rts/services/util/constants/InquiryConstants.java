package com.txdot.isd.rts.services.util.constants;

/*
 * InquiryConstants.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	07/01/2005	Class Creation
 * S Johnston	07/06/2005	Removed unused variables / Cleanup
 * T Pederson	02/22/2007	Added constants for new frame INQ005 
 * 							defect 9123 Ver Special Plates
 * K Harrell	05/19/2007	Modify TITLE_FRM_INQ005 for consistency
 * 							delete ENTER
 *							defect 9085 Ver Special Plates
 * K Harrell	05/20/2009	add TXT_AUDIT_TRAIL_TRANS_ID
 * 							defect 10058 Ver Defect_POS_E
 * K Harrell	02/16/2010	INQ006 Labels in caps. 
 * 							modify TXT_RENEWAL_RECIPIENT_INFO, 
 * 							 TXT_VEHICLE_LOC_INFO
 * 							defect 10372 Ver POS_640 
 * ---------------------------------------------------------------------
 */
 
/**
 * InquiryConstants
 *
 * @version	POS_640			02/16/2010
 * @author	S Johnston
 * <br>Creation Date:		07/01/2005 15:03:23
 */
public class InquiryConstants
{
	// FrmCustomerNameINQ008 Constants.
	public static final String TITLE_FRM_INQ008 =
		"Customer Name   INQ008";
	public static final String TXT_ENTER_CUST_NAME =
		"Enter Customer Name:";

	// FrmInquiryOwnerAddressessINQ006 Constants
	public static final String TITLE_FRM_INQ006 =
		"Inquiry Owner Addressess   INQ006";
	public static final String TXT_OWNER_ID = "Owner Id:";
	public static final String TXT_RENEWAL_RECIPIENT_INFO =
		"Renewal Recipient Information:";
	public static final String TXT_UNIT_NO = "Unit No:";
	public static final String TXT_VEHICLE_LOC_INFO =
		"Vehicle Location Information:";

	// FrmVehicleInqAddlInfoINQ003 Constants.
	public static final String TITLE_FRM_INQ003 =
		"Vehicle Inquiry Addl Info   INQ003";

	// defect 10058 
	public static final String TXT_AUDIT_TRAIL_TRANS_ID =
		"AuditTrailTransId:";
	// end defect 10058 

	public static final String TXT_PREVIOUS_OWNER_INFO =
		"Previous Owner Information:";
	public static final String TXT_REGISTRATION_INFO =
		"Registration Information:";
	public static final String TXT_OTHER_INFO = "Other Information:";
	public static final String TXT_SALES_TAX_INFO =
		"Sales Tax Information:";
	public static final String TXT_ISSUE_DATE = "Issue Date:";
	public static final String TXT_EFFECTIVE_DATE = "Effective Date:";
	public static final String TXT_FEE = "Fees Paid:";
	public static final String TXT_NAME_CITY = "Name/City& State:";
	public static final String TXT_PREV_EXP_MO = "Previous Exp Mo/Yr:";
	public static final String TXT_PREV_PLT_NO = "Previous Plate No:";
	public static final String TXT_SALES_PRICE = "Sales Price:";
	public static final String TXT_SALES_TAX_PD = "Sales Tax Paid:";
	public static final String TXT_TIRE_TYPE = "Tire Type:";
	public static final String TXT_TRADE_IN = "Trade In:";

	// defect 9123
	// FrmSpecialPlateInquiryInfoINQ005 Constants
	public static final String TITLE_FRM_INQ005 =
		"Special Plate Inquiry Info    INQ005";
	public static final String TXT_OWNER = "Owner:";
	public static final String TXT_SPECIAL_PLATE = "Special Plate:";
	// end defect 9123
}
