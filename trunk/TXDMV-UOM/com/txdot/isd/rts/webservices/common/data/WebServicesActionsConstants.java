package com.txdot.isd.rts.webservices.common.data;

/*
 * WebServicesActionsConstants.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/28/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * Ray Rowehl	06/03/2008	Add action constant for new orders. 
 * 							add RTS_TRANS_VP_ORDER_NEW
 * 							defect 9680 Ver MyPlates_POS
 * Min Wang		06/03/2008	Add action constant for check plt avail.
 * 							add RTS_INV_CHECK_PLATE_AVAIL,
 * 								RTS_INV_ISSUE_PERSONALIZED
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/18/2008	Add action constant for order from Reserve.
 * 							add RTS_TRANS_VP_ORDER_RESERVE
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	06/18/2008	Add action constant to issue a Reserved plate.
 * 							add RTS_INV_ISSUE_RESERVED
 * 							defect 9679 Ver MyPlates_POS
 * Min Wang		06/24/2008	Add action constant for inv hold confirm.
 * 							add RTS_INV_HOLD_CONFIRM
 * 							defect 9679 Ver MyPlates_POS 
 * Ray Rowehl	06/25/2008	Add action constant for inv hold release.
 * 							add RTS_INV_HOLD_RELEASE
 * 							defect 9679 Ver MyPlates_POS 
 * Mark Reyes	06/25/2008  Add action constant for Plate Types.
 * 							add RTS_ADM_PLATE_TYPE
 * 							defect 9677 Ver MyPlates_POS
 * Min Wang		07/10/2008	Add action constant for WebApps
 * 							add RTS_WEBAPPS_GET_SP_APP_TRANS 
 * 							defect 9676 Ver MyPlates_POS
 * Ray Rowehl	03/04/2010	Rename constant to reflect overall purpose.
 * 							add RTS_INV_ISSUE_PLATE_NUMBER 
 * 							delete RTS_INV_ISSUE_PERSONALIZED
 * 							defect 10400 Ver 6.4.0	
 * B Hargrove	03/12/2010	Add action constant for WebApps
 * 							add RTS_VEH_SPEC_PLT_REGIS 
 * 							defect 10402 Ver 6.4.0	
 * Ray Rowehl	03/24/2010	Add action constant to handle porting a 
 * 							plate number.
 * 							add RTS_TRANS_VP_RESTYLE_PLT_NO
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/08/2010	Add action constant to handle putting a 
 * 							plate number on Reserve.
 * 							add RTS_TRANS_VP_DELETE_SP,
 * 								RTS_TRANS_VP_RESERVE_PLT_NO,
 * 								RTS_TRANS_VP_REVISE,
 * 								RTS_TRANS_VP_UNACCEPTABLE
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	12/08/2010	Add constants for WebSub Renewal
 * 							add RTS_REN_PLATE_LOOKUP, RTS_REN_PLATE_POST,
 * 								RTS_REN_DOCNO 
 * 							defect 10670 Ver 6.7.0
 * K Harrell	12/13/2010	Add constant for Public Get Vehicle 
 * 							add RTS_VEH_PUBLIC_
 * 							defect 10684 Ver 6.7.0
 * Ray Rowehl	01/04/2011	Add constant for Web Agent Login 
 * 							add RTS_AGENT_LOGIN
 * 							defect 10670 Ver 6.7.0 
 * Ray Rowehl	01/05/2011	Add constant for Agency List
 * 							add RTS_AGENCY_LIST_REQUEST
 * 							defect 10718 Ver 6.7.0 
 * Ray Rowehl	01/20/2011	Add constant for Agent List.
 * 							add RTS_AGENT_LIST_REQUEST
 * 							defect 10719 Ver 6.7.0
 * Ray Rowehl	01/21/2011	Round out the constants for Web Agent.
 * 							add RTS_BATCH_SUBMIT, RTS_BATCH_APPROVAL,
 * 								RTS_BATCH_DETAIL_REPRINT, 
 * 								RTS_BATCH_DETAIL_VOID, 
 * 								RTS_AGENCY_INSERT_UPDATE,
 * 								RTS_AGENCY_DELETE, 
 * 								RTS_AGENT_INSERT_UPDATE,
 * 								RTS_AGENT_DELETE
 * 							defect 10719 Ver 6.7.0
 * Ray Rowehl	03/02/2011	Add constant for Logout.
 * 							add RTS_AGENT_LOGOUT
 * 							defect 10670 Ver 6.7.0
 * K McKee		01/06/2012  Add constant for plate search
 * 							Add RTS_BATCH_PLATE_SEARCH
 * 							defect 11239 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Defines all the Action numbers used for RTS Web Services.
 *
 * @version	6.10.0			01/06/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		05/28/2008 11:11:55
 */

public class WebServicesActionsConstants
{
	/**
	 * Take the default action of returning null.
	 */
	public static final int RTS_DEFAULT_ACTION = 0;

	/**
	 * Web Service Action number for getting county information 
	 * from OfficeIds and returning it to the caller.
	 */
	public static final int RTS_ADM_COUNTY_INFO = 31;

	/**
	 * Web Service Action number for getting plate type 
	 * from PlateType and returning it to the caller.
	 */
	public static final int RTS_ADM_PLATE_TYPE = 33;

	// defect 10670
	/**
	 * Web Service Action number for WebRenewal to 
	 * do the initial lookup of a record when search by
	 * Plate and Last 4 of VIN.
	 */
	public static final int RTS_REN_PLATE_LOOKUP = 50;

	/**
	 * Web Service Action number for WebRenewal to 
	 * do the post of a renewal request when search by
	 * Plate and Last 4 of VIN.
	 */
	public static final int RTS_REN_PLATE_POST = 51;

	/**
	 * Web Service Action number for WebRenewal to 
	 * do the initial lookup of a record 
	 * and post the renewal request when search by
	 * DocNo.
	 */
	public static final int RTS_REN_DOCNO = 52;
	// end defect 10670

	/**
	 * Web Service Action number for getting the appropriate
	 * Special Plate Regis record  and returning it to the caller.
	 */
	public static final int RTS_VEH_SPEC_PLT_REGIS = 61;

	/**
	 * Web Service Action number for Inventory actions.
	 */
	public static final int RTS_INV_HOLD_CONFIRM = 71;
	public static final int RTS_INV_HOLD_RELEASE = 72;

	/**
	 * This is for issuing any plate number from VI.
	 */
	public static final int RTS_INV_ISSUE_PLATE_NUMBER = 74;
	public static final int RTS_INV_ISSUE_RESERVED = 75;
	public static final int RTS_INV_CHECK_PLATE_AVAIL = 76;

	/**
	 * Web Service Action number for receiving a transaction requests
	 * for new plate orders.
	 */
	public static final int RTS_TRANS_VP_ORDER_NEW = 81;

	// defect 10401
	/**
	 * Web Service Action number to delete a SpecRegis Entry.
	 */
	public static final int RTS_TRANS_VP_DELETE_SP = 83;
	// end defect 10401

	/**
	 * Web Service Action number for receiving a transaction request 
	 * for a plate issued from Reserve.
	 */
	public static final int RTS_TRANS_VP_ORDER_RESERVE = 85;

	/**
	 * Web Service Action number for receiving a transaction request 
	 * to "restyle" a plate.
	 */
	public static final int RTS_TRANS_VP_RESTYLE_PLT_NO = 86;

	/**
	 * Web Service Action number to mark a plate number Unacceptable.
	 */
	public static final int RTS_TRANS_VP_UNACCEPTABLE = 87;

	/**
	 * Web Service Action number to Revise a plate.
	 */
	public static final int RTS_TRANS_VP_REVISE = 88;

	/**
	 * Web Service Action number to "port" a charity special plate to
	 * be a vendor plate.
	 */
	public static final int RTS_TRANS_VP_PORT = 89;

	/**
	 * Web Service Action number for receiving a transaction request 
	 * to "reserve" a plate.
	 */
	public static final int RTS_TRANS_VP_RESERVE_PLT_NO = 90;

	/**
	 * Web Service Action number for allow the vendor to "ReDo" an order.
	 */
	public static final int RTS_TRANS_VP_REDO = 91;

	/**
	 * Web Service Action number for Disabled Placard Query
	 */
	public static final int RTS_DSABLD_PLCRD = 92;

	/**
	 * Web Service Action number for receiving a inquiry request 
	 * from WebApps.
	 */
	public static final int RTS_WEBAPPS_GET_SP_APP_TRANS = 21;

	/**
	 * Web Service Action number for Public Get Vehicle Query 
	 */
	public static final int RTS_VEH_PUBLIC = 62;
	
	/**
	 * Web Service Action number for Web Agent Login.
	 */
	public static final int RTS_AGENT_LOGIN = 100;
	
	/**
	 * Web Service Action number for Web Agent Logout.
	 */
	public static final int RTS_AGENT_LOGOUT = 101;
	
	/**
	 * Web Service Action number for Web Agent Batch List Request.
	 */
	public static final int RTS_BATCH_LIST_REQUEST = 110;
	public static final int RTS_BATCH_SUBMIT = 111;
	public static final int RTS_BATCH_APPROVAL = 112;
	
	/**
	 * Web Service Action number for Web Agent Batch Detail Request.
	 */
	
	public static final int RTS_BATCH_DETAIL_REQUEST = 115;
	public static final int RTS_BATCH_DETAIL_REPRINT = 116;
	public static final int RTS_BATCH_DETAIL_VOID = 117;
	// defect 11239
	public static final int RTS_BATCH_PLATE_SEARCH = 118;
	//end defect 11239
	/**
	 * Web Service Action number for Web Agent Agency List Request
	 */
	public static final int RTS_AGENCY_LIST_REQUEST = 120;
	public static final int RTS_AGENCY_INSERT_UPDATE = 121;
	public static final int RTS_AGENCY_DELETE = 122;
	
	/**
	 * Web Service Action number to get a list of Agents for Web Agent.
	 */
	public static final int RTS_AGENT_LIST_REQUEST = 130;
	public static final int RTS_AGENT_INSERT_UPDATE = 131;
	public static final int RTS_AGENT_DELETE = 132;
	public static final int RTS_AGENT_FOR_COUNTY = 133;
	public static final int RTS_AGENT_RESET_PASSWORD = 134;
}
