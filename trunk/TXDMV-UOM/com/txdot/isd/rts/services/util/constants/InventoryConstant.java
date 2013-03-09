package com.txdot.isd.rts.services.util.constants;

import com.txdot.isd.rts.services.util.Log;

/*
 *
 * InventoryConstant.JAVA
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		08/25/2002	Fixed defect(CQU100004668) added constants
 *							for recalculation.
 * Min Wang 	09/25/2002	Fixed defect(CQU100004734) added constant
 *							VERIFY_INVENTORY_ITEM_FOR_ISSUE.
 * Min Wang		01/08/2003	Added constant  RECALC_CHECK_INV. 
 *							defect 5201
 * Ray Rowehl	02/20/2003	Added constant  GET_WS_IDS
 * K Harrell	01/25/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class constants
 * 							 - DEFAULT_SELECTION
 *							 - DEFAULT_ABBR
 *							 - RECALC_UPDT_INV
 *							 - Reassigned RECALC_CHECK_INV
 * 							Ver 5.2.0	
 * Ray Rowehl	04/06/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, order the number constants
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	06/17/2005	Add constants to determine max inventory
 * 							item and qty string lengths.
 * 							Added JavaDoc statements to document some
 * 							of the values.
 * 							add MAX_QTY_LENGTH, MAX_ITEM_LENGTH
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	07/06/2005	Add new constants for ranges.
 * 							Add some javaDocs.
 * 							add RANGE_BEGIN_END, RANGE_BEGIN_WITHIN,
 * 								RANGE_BEGIN_OUTSIDE, RANGE_WITHIN_END,
 * 								RANGE_END_OUTSIDE, RANGE_LEFT, 
 * 								RANGE_OUTSIDE, RANGE_RIGHT, 
 * 								RANGE_WITHIN, HOLD_INV_NOT, 
 * 								HOLD_INV_USER, HOLD_INV_SYSTEM
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/09/2005	Add new constants for Invoice Prefixes
 * 							add INVOICE_PREFIX_ANNUAL, 
 * 								INVOICE_PREFIX_DUMMY,
 * 								INVOICE_PREFIX_SUPPLEMENTAL
 * 							defect 7890 Ver 5.2.3	
 * Ray Rowehl	07/12/2005	Add new constants to support computation
 * 							add COMPUTE_CURRENT_ITEM, COMPUTE_NEXT_ITEM,
 * 								COMPUTE_PREVIOUS_ITEM
 * 							defect 7890 Ver 5.2.3
 * Min Wang		07/27/2005	modify constant for removing item code.
 * 							modify DEFAULT_SELECTION
 * 							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	08/06/2005	Remove duplicate function codes
 * 							delete PRINT_INVENTORY_RECEIVED_REPORT
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/08/2005	Add Inventory Processing Codes.
 * 							add INV_PROCSNGCD_NORMAL,
 * 								INV_PROCSNGCD_RESTRICTED,
 * 								INV_PROCSNGCD_SPECIAL 
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/12/2005	Add more error numbers.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/16/2005	Move error number and msg constants to 
 * 							the new errorsconstant class.
 * 							defect 7890 Ver 5.2.3
 * T Pederson	04/05/2006	Add space to TXT_WORKSTATION_ID so it will 
 * 							display properly.  
 * 							modify TXT_WORKSTATION_ID 
 * 							defect 8675 Ver 5.2.3
 * Min Wang		04/10/2006	Update TXT_INVOICE_NO_COLON
 * 							defect 8653 Ver 5.2.3
 * Min Wang		04/11/2006  Update TXT_INVOICE_NO_COLON
 * 							defect 8653 Ver 5.2.3
 * Min Wang		02/09/2007	Create constant for getting next Virtual 
 * 							Inventory Item Number.
 * 							add INV_GET_NEXT_VI_ITEM_NO
 * 							defect 9117 Ver Special Plates  
 * Ray Rowehl	02/26/2007	Add constant for VI PLP Verification method.
 * 							Add new hold codes.
 * 							add INV_VI_VALIDATE_PER_PLT, VI_DELETE_ITEM
 * 							defect 9116 Ver Special Plates
 * Min Wang		02/28/2007	Add constant for Virtual Inventory Report.
 * 							add CUR_VIRTUAL
 * 							DEFECT 9117 Ver Special Plates
 * Ray Rowehl	03/09/2007	Add constant to allow IVTRS to validate 
 * 							a PLP without holding it.
 * 							add INV_VI_VALIDATE_PLP_NO_HOLD
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	03/12/2007	Add constant to control max computed qty.
 * 							Mainframe is still limited in what they can 
 * 							handle.
 * 							add INV_MAX_COMPUTED_QTY
 * 							defect 9116 Ver Special Plates
 * Min Wang		03/14/2007	Add constant for VI rejection report.
 * 							add GENERATE_VI_REJECTION_REPORT
 * 							defect 9117 Ver Special Plates
 * Ray Rowehl	05/21/2007	Update some documentation to ensure it 
 * 							mentions the ViItmCd as optional.
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/04/2007	Add constant to indicate we want to update
 * 							the transtime of a selected record.
 * 							add INV_VI_UPDATE_TIMESTAMP
 * 							defect 9116 Ver Special Plates
 * Min Wang		04/09/2008	Capitalized “more”.
 * 							modify TXT_SELECT_ONE_OR_MORE
 * 							defect 9180 Ver Defect_POS_A
 * K Harrell	09/04/2008	Entries in Admin_Log to Mixed Case 
 * 							modify TXT_INVPROFILE
 * 							defect 8595 Ver Defect_POS_B
 * Ray Rowehl	01/12/2009	Allow the MAX_QTY to be changed to support 
 * 							testing.
 * 							add setMaxQty()
 * 							modify MAX_QTY
 * 							defect 9714 Ver Defect_POS_D
 * K Harrell	10/12/2009	add INV015_COL_OFFICE_NO, 
 * 							 INV015_COL_OFFICE_NAME,
 *  						 INV015_COL_LAST_ACTIVITY,
 * 							 INV024_COL_ITEM_DESCR,
 * 							 INV024_COL_ITEM_YEAR,
 * 							 TXT_SELECT_SEARCH_TYPE_COLON,
 * 							 TXT_LAST_ACTIVITY  
 * 							delete TXT_SELECT_SEARCH_TYPE,
 * 							 TXT_LAST_ACTIVITY
 * 							defect 10207 Defect_POS_G
 * ---------------------------------------------------------------------
 */

/**
 * This class contains the constants for Inventory Processing
 *
 * @version	Defect_POS_G	10/12/2009
 * @author	Charlie Walker
 * @author	Ray Rowehl
 * @author	Min Wang
 * <br>Creation Date:		08/29/2001  
 */

public class InventoryConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;

	//	Function Constants - InventoryServerBusiness
	/**
	 * This function Adds, Deletes, or Modifies Inventory Invoice 
	 * Items.
	 * 
	 * <p>This value is 1.
	 */
	public final static int ADD_MODIFY_DELETE_INVENTORY_ITEM = 1;
	/**
	 * This value is 2.
	 */
	public final static int ALLOC_INVENTORY_ITEMS = 2;
	/**
	 * This value is 3.
	 */
	public final static int CALCULATE_INVENTORY_UNKNOWN = 3;
	/**
	 * This value is 4.
	 */
	public final static int DELETE_INVENTORY_ITEM = 4;
	/**
	 * This value is 5.
	 */
	public final static int DELETE_INVENTORY_PROFILE = 5;
	/**
	 * This value is 6.
	 */
	public final static int GET_INVENTORY_FROM_MF = 6;
	/**
	 * This value is 7.
	 */
	public final static int GET_INVENTORY_PROFILE = 7;
	/**
	 * This value is 8.
	 */
	public final static int GET_NEXT_INVENTORY_NO = 8;
	/**
	 * This value is 9.
	 */
	public final static int ISSUE_INVENTORY = 9;
	/**
	 * This value is 10.
	 */
	public final static int RECEIVE_INVENTORY_TO_DB = 10;
	/**
	 * This value is 11.
	 */
	public final static int VERIFY_INVENTORY_ITEM_IN_DB = 11;
	/**
	 * This value is 12.
	 */
	public final static int RETRIEVE_ALLOCATION_DISPLAY_DATA = 12;
	/**
	 * This value is 13.
	 */
	public final static int RETRIEVE_INVENTORY_PROFILE_DISPLAY_DATA =
		13;
	/**
	 * This value is 14.
	 */
	public final static int ADD_INVENTORY_PROFILE = 14;
	/**
	 * This value is 15.
	 */
	public final static int REVISE_INVENTORY_PROFILE = 15;
	/**
	 * This value is 16.
	 */
	public final static int RETRIEVE_REGIONAL_COUNTIES = 16;
	/**
	 * This value is 17.
	 */
	public final static int RETRIEVE_INV_ITEMS = 17;
	/**
	 * This value is 18.
	 */
	public final static int RETRIEVE_SUBSTATION_DATA = 18;
	/**
	 * This value is 19.
	 */
	public final static int RETRIEVE_INQUIRY_ENTITY_DATA = 19;
	/**
	 * This value is 20.
	 */
	public final static int GET_INVENTORY_ALLOCATION_DATA = 20;
	/**
	 * This value is 21.
	 */
	public final static int UPDATE_INVENTORY_STATUS_CD = 21;
	/**
	 * This value is 22.
	 */
	public final static int RETRIEVE_INVENTORY_ITEMS_FOR_INQUIRY = 22;
	/**
	 * This value is 23.
	 */
	public final static int DELETE_FOR_ISSUE_INVENTORY = 23;
	/**
	 * This value is 24.
	 */
	public final static int GET_INVENTORY_RANGE_IN_DB = 24;
	/**
	 * This value is 25.
	 */
	public final static int VERIFY_INVENTORY_ITEM_FOR_ISSUE = 25;
	//Function Constants - InventoryReport
	/**
	 * This value is 50.
	 */
	public final static int GENERATE_INVENTORY_ALLOCATION_REPORT = 50;
	/** 
	 * This value is 51.
	 */
	public final static int GENERATE_INVENTORY_RECEIVED_REPORT = 51;
	/**
	 * This value is 52.
	 */
	public final static int GENERATE_INVENTORY_INQUIRY_REPORT = 52;
	/**
	 * This value is 53.
	 */
	public final static int GENERATE_INVENTORY_PROFILE_REPORT = 53;
	/**
	 * This value is 54.
	 */
	public final static int GENERATE_INVENTORY_ITEMS_ONHOLD_REPORT = 54;
	/**
	 * This value is 55.
	 */
	public final static int GENERATE_INVENTORY_DEL_REPORT = 55;
	/**
	 * This value is 56.
	 */
	public final static int GENERATE_RECEIVE_INVENTORY_HISTORY_REPORT =
		56;
	/**
	 * This value is 57. 
	 */
	public final static int GENERATE_DELETE_INVENTORY_HISTORY_REPORT =
		57;
	/**
	 * This value is 58.
	 */
	public final static int GENERATE_INVENTORY_ACTION_REPORT = 58;
	/**
	 * This value is 59.
	 */
	public final static int PRINT_INVENTORY_ALLOCATION_REPORT = 59;
	// defect 9117
	/**
	 * This value is 59.
	 */
	public final static int GENERATE_VI_REJECTION_REPORT = 60;
	// end defect 9117
	/**
	 * This value is 61.
	 */
	public final static int PRINT_INVENTORY_ITEMS_ONHOLD_REPORT = 61;
	/**
	 * This value is 62.
	 */
	public final static int PRINT_INVENTORY_DEL_REPORT = 62;
	//Function Constants - Transactions
	/**
	 * This value is 70.
	 */
	public final static int GET_INV_FUNC_TRANS_AND_TR_INV_DETAIL = 70;

	//	get Workstation Id List
	/**
	 * This value is 80.
	 */
	public final static int GET_WS_IDS = 80;

	//Recalculate InvPtrnSeqNo
	/**
	 * This value is 100.
	 */
	public final static int RECALC_GET_INV = 100;
	/**
	 * This value is 101.
	 */
	public final static int RECALC_UPDT_INV = 101;
	/**
	 * This value is 102.
	 */
	public final static int RECALC_CHECK_INV = 102;
	/**
	 * Check to see if a Virtual Item is found for a plate input.
	 * 
	 * <p>This value is 103.
	 */
	public final static int RECALC_CHECK_VI = 103;

	/**
	 * Presents the next available item from Virtual Inventory that 
	 * supports the request.
	 * 
	 * <p>Routed to Inventory Server Business.
	 * 
	 * <p>Calling parameters are:
	 * <ul>
	 * <li>GeneralConstant.INVENTORY
	 * <li>InventoryConstant.INV_GET_NEXT_VI_ITEM_NO (this)
	 * <li>InventoryAllocationData
	 * <eul>
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * new Item Number.  This object also contains the reference to 
	 * the Virtual Item Code used to look up the item number.
	 * 
	 * <p>Throws an RTSException if there is any problem.
	 * 
	 * <p>Values that need to be set to get an item number are:
	 * <ul>
	 * <li>ItmCd
	 * <li>InvItmYr
	 * <li>ISA
	 * <li>UserPltNo (by definition this should be false)
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <li>ReqIPAddr   (Control Point on POS)
	 * <li>RequestorRegPltNo
	 * <eul>
	 * 
	 * <p>This value is 205.
	 */
	public final static int INV_GET_NEXT_VI_ITEM_NO = 205;

	/**
	 * Validates a Special Plate Application for a Personalized option 
	 * on a Special Plate.  If the plate number passes, it is put on
	 * hold for the requestor.
	 * 
	 * <p>Routed to Inventory Server Business.
	 * 
	 * <p>Calling parameters are:
	 * <ul>
	 * <li>GeneralConstant.INVENTORY
	 * <li>InventoryConstant.INV_VI_VALIDATE_PER_PLT (this)
	 * <li>InventoryAllocationData
	 * <eul>
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * validated Item Number.  
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * The exception may contain an error code indicating what part 
	 * of the validation failed.
	 * 
	 * <p>Values that need to be set to validate the plate number are:
	 * <ul>
	 * <li>ItmCd
	 * <li>InvItmYr
	 * <li>InvItmNo
	 * <li>InvItmEndNo (by definition this is the same as the InvItmNo) 
	 * <li>ISA
	 * <li>UserPltNo (by definition this should be true)
	 * <li>PatrnSeqCd = 0
	 * <li>PatrnSeqNo = 0
	 * <li>InvQty = 1
	 * <li>InvcNo = ""
	 * <li>InvcDate = 0
	 * <li>MfgPltNo
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <li>ReqIPAddr
	 * <li>RequestorRegPltNo
	 * <eul>
	 * 
	 * <p>This value is 206.
	 */
	public final static int INV_VI_VALIDATE_PER_PLT = 206;

	/**
	 * Delete the specified item from Virtual Inventory.
	 * 
	 * <p>Values that need to delete an item be are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>ItmCd
	 * <li>ViItmCd 
	 * <li>InvItmYr
	 * <eul>
	 * 
	 * <p>If the object has not been calculated, it will be calculated.
	 * 
	 * <p>This value is 207.
	 */
	public final static int INV_VI_DELETE_ITEM = 207;

	/**
	 * Confirm Special Plate Application is complete
	 * for Virtual Inventory item.
	 * 
	 * <p>Routed to Inventory Server Business.
	 * 
	 * <p>Calling parameters are:
	 * <ul>
	 * <li>GeneralConstant.INVENTORY
	 * <li>InventoryConstant.INV_VI_ITEM_APPLICATION_COMPLETE (this)
	 * <li>InventoryAllocationData
	 * <eul>
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * inv status code showing complete.  
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * 
	 * <p>Values that need to be set to confirm the order status are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>PatrnSeqNo
	 * <li>PatrnSeqCd
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <eul>
	 * 
	 * <p>This value is 208.
	 */
	public final static int INV_VI_ITEM_APPLICATION_COMPLETE = 208;

	/**
	 * Used to comfirm that a previously held plate is still on hold.
	 * The hold timestamp is updated if it is.
	 * 
	 * <p>Routed to Inventory Server Business.
	 * 
	 * <p>Calling parameters are:
	 * <ul>
	 * <li>GeneralConstant.INVENTORY
	 * <li>InventoryConstant.INV_VI_CONFIRM_HOLD (this)
	 * <li>InventoryAllocationData
	 * <eul>
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * inv status code showing on hold.  
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * 
	 * <p>Values that need to be set to confirm the order status are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>PatrnSeqNo
	 * <li>PatrnSeqCd
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <eul>
	 * 
	 * <p>This value is 209
	 */
	public final static int INV_VI_CONFIRM_HOLD = 209;

	/**
	 * Used to release a hold on an item.
	 * 
	 * <p>Routed to Inventory Server Business.
	 * 
	 * <p>Calling parameters are:
	 * <ul>
	 * <li>GeneralConstant.INVENTORY
	 * <li>InventoryConstant.INV_VI_RELEASE_HOLD (this)
	 * <li>InventoryAllocationData
	 * <eul>
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * inv status code showing no hold.  
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * 
	 * <p>Values that need to be set to confirm the order status are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>PatrnSeqNo
	 * <li>PatrnSeqCd
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <eul>
	 * 
	 * <p>This value is 210
	 */
	public final static int INV_VI_RELEASE_HOLD = 210;

	/**
	 * Used to run through the PLP Validatation process but does 
	 * not hold the item in Virtual Inventory.
	 * 
	 * <p>Routed to Inventory Server Business.
	 * 
	 * <p>Calling parameters are:
	 * <ul>
	 * <li>GeneralConstant.INVENTORY
	 * <li>InventoryConstant.INV_VI_VALIDATE_PLP_NO_HOLD (this)
	 * <li>InventoryAllocationData
	 * <eul>
	 * 
	 * <p>Returns a boolean indicating availablility.  
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * 
	 * <p>Values that need to be set to confirm the order status are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>ItmCd
	 * <li>InetReq = true (this is for IVTRS)
	 * <eul>
	 * 
	 * <p>This value is 211
	 */
	public final static int INV_VI_VALIDATE_PLP_NO_HOLD = 211;

	/**
	 * Purges Virtual Inventory that is marked as complete.
	 * 
	 * <p>This value is 212.
	 */
	public final static int INV_VI_PURGE = 212;

	/**
	 * Releases all Virtual Inventory on System Hold where the timeout
	 * has expired.
	 * 
	 * <p>This value is 213.
	 */
	public final static int INV_VI_RELEASE_ALL_SYSTEM_HOLDS = 213;

	/**
	 * Puts the specified item on hold.
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * inv status code showing the item on hold.  
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * 
	 * <p>Values that need to be set to confirm the order status are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <eul>
	 * 
	 * <p>This value is 214.
	 */
	public final static int INV_VI_UPDATE_INV_STATUS_CD = 214;

	/**
	 * Inserts the specified Virtual Inventory row into Virtual 
	 * Inventory.  This row is an InventoryAllocationData object that 
	 * has already been populated and calculated.
	 * 
	 * <p>Returns an InventoryAllocationData Object containing the 
	 * inv status code showing the item on hold. 
	 * 
	 * <p>Values that need to be set to insert the row are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <eul> 
	 * 
	 * <p>If the item is not calculated, it will be calculated.
	 * 
	 * <p>Throws an RTSException if there is any problem.  
	 * 
	 * <p>This value is 215.
	 */
	public final static int INV_VI_INSERT_ROW = 215;

	/**
	 * Attempts to put the requested item on hold.
	 * 
	 * <p>If the item is not found, then it is inserted for the 
	 * requestor and put on hold.
	 * 
	 * <p>Values that need to be set to insert the row are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>InvStatusCd
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransEmpId
	 * <eul> 
	 * 
	 * <p>Throws an RTSException if there is any unhandlable problem.
	 * 
	 * <p>This value is 216.
	 */
	public final static int INV_VI_UPDATE_INV_STATUS_CD_RECOVER = 216;

	/**
	 * Attempts to update a VI Item with a time.
	 * The time provided should match the transaction time.
	 * This signals that the item is committed unless canceled by the
	 * requestor.
	 * 
	 * <p>Values that need to be set to insert the row are:
	 * <ul>
	 * <li>InvItmNo
	 * <li>InvItmEndNo
	 * <li>InvQty
	 * <li>ItmCd
	 * <li>ViItmCd (If empty, it will be looked up)
	 * <li>InvItmYr
	 * <li>InetReq
	 * <li>OfcIssuanceNo
	 * <li>TransWsId
	 * <li>TransAmDate
	 * <li>TransTime
	 * <li>TransEmpId
	 * <eul> 
	 * 
	 * <p>Throws an RTSException if there is any unhandlable problem.
	 * 
	 * <p>This value is 217
	 */
	public final static int INV_VI_UPDATE_TRANSTIME = 217;

	////////////////////////////////////////////////////////////////
	/**
	 * This value is 200.
	 */
	public final static int ITM_INDI_NO_CHANGE = 200;
	/**
	 * This value is 201.
	 */
	public final static int ITM_INDI_MODIFIED = 201;
	/**
	 * This value is 202.
	 */
	public final static int ITM_INDI_NEW = 202;
	/**
	 * This value is 203.
	 */
	public final static int ITM_INDI_REMOVED = 203;
	/**
	 * This value is 204.
	 */
	public final static int ITM_INDI_IGNORED = 204;

	//Allocation Constants
	public final static String CHAR_A = "A";
	public final static String CHAR_W = "W";
	public final static String CHAR_S = "S";
	public final static String CHAR_D = "D";
	public final static String CHAR_E = "E";
	public final static String CHAR_C = "C";
	public final static String STR_A = "Server";
	public final static String STR_W = "Workstation";
	public final static String STR_S = "Subcontractor";
	public final static String STR_D = "Dealer";
	public final static String STR_E = "Employee";
	public final static String STR_C = "Central";

	//Invoice Constants
	public final static String CHAR_STOCK = CHAR_A;
	public final static String CHAR_CENTRAL = CHAR_C;
	public final static String STR_STOCK = "Stock";
	public final static String STR_CENTRAL = "Central";
	public final static String DEST_ONLY = "Destination";
	public final static String ALL_SUBSTAS = "All";
	public final static String SELCTD_SUBSTAS = "Select";
	public final static String DELETE = "Delete";
	public final static String CHECK = "Check";
	public final static String FAILED = "Failed";
	public final static String VERIFIED_OK =
		"ITEM VERIFIED AND MATCHES THE INVOICE.";
	public final static String MODIFIED_OK =
		"ITEM MODIFIED AND VALIDATED.";
	public final static String USER_DELETED = "ITEM DELETED BY USER.";
	public final static String ADDED_OK = "ITEM ADDED OK.";
	public final static String REJECTED = "ITEM REJECTED BY SYSTEM:";
	public final static String WRONG_QTY_OR_NO =
		"ITEM HAS WRONG QUANTITY OR NUMBER";
	public final static String RCVE = "RCVE";
	public final static String ADD = "ADD";
	public final static String MODFY = "MOD";
	public final static String DEL = "DEL";

	//Inquiry Constants
	public final static String EMP = "EMPLOYEE";
	public final static String WS = "WORKSTATION";
	public final static String DLR = "DEALER";
	public final static String SUBCON = "SUBCONTRACTOR";
	public final static String ITM_TYPES = "ITEM TYPE(S) AND YEAR";
	public final static String SPECIFIC_ITM =
		"SPECIFIC ITEM NUMBER AND YEAR";
	public final static String CNTRL = "CENTRAL";
	public final static String CUR_BAL = "CURRENT BALANCE";
	// defect 9117
	public final static String CUR_VIRTUAL = "CURRENT VIRTUAL";
	// end defect 9117
	public final static String CUR_BAL_HISTORY =
		"CURRENT BALANCE AND HISTORY";
	public final static String HISTORY = "HISTORY";
	public final static String MAIN_OFC = "MAIN OFFICE";
	public final static String SUBSTA_OFC = "SUBSTATION";

	//Issue Inventory Constants
	/**
	 * Transaction was voided.
	 */
	public final static String VOID = "V";
	/**
	 * Database was down when issuing the inventory.
	 */
	public final static String DB_DOWN = "X";
	/**
	 * Issued Inventory was not found in the system.
	 */
	public final static String NOT_FOUND = "U";

	// Range Constants
	/**
	 * The actual range Begin and End matches the requested range.
	 */
	public final static String RANGE_BEGIN_END = "BE";
	/**
	 * The actual range Begin matches the requested range, but ends
	 * within the requested range.
	 */
	public final static String RANGE_BEGIN_WITHIN = "BW";
	/**
	 * The actual range Begin matches the requested range.
	 * The actual range ends beyond the request range.
	 */
	public final static String RANGE_BEGIN_OUTSIDE = "BO";
	/**
	 * The actual range begins within the requested range.
	 * The actual range ends with the end of the requested range.
	 */
	public final static String RANGE_WITHIN_END = "EW";
	/**
	 * The actual range begins within the requested range end.
	 * The actual range ends outside of the requested range.
	 */
	public final static String RANGE_END_OUTSIDE = "EO";
	/**
	 * The actual range begins to the left of the requested range 
	 * and ends within the requested range.
	 */
	public final static String RANGE_LEFT = "L";
	/**
	 * The actual range starts before the requested range.
	 * The actual range ends after the requested range.
	 */
	public final static String RANGE_OUTSIDE = "O";
	/**
	 * The actual range begins to within the requested range and 
	 * ends to the right of the requested range.
	 */
	public final static String RANGE_RIGHT = "R";
	/**
	 * The actual range is completely contained with in the requested
	 * range.
	 */
	public final static String RANGE_WITHIN = "W";

	//Inventory Validation Constants
	/**
	 * The maximum length for an inventory item string is 10.
	 */
	public final static int MAX_ITEM_LENGTH = 10;
	/**
	 * The minimum quantity that can be specified is 0.
	 */
	public final static int MIN_QTY = 0;

	// defect 9714
	/**
	 * The maximum quantity that can be specified is 9,999,999.
	 * 
	 * <p>Note this can be over ridden for testing!
	 */
	public static int MAX_QTY = 9999999;
	// end defect 9714

	/**
	 * The maximum quantity that can be computed is 2,147,483,647.
	 * 
	 * <p>This is the maximum that the db2 field can accept.
	 */
	public final static int INV_MAX_COMPUTED_QTY = 2147483647;

	/**
	 * The maximum quantity length is 7 positions.
	 */
	public final static int MAX_QTY_LENGTH = 7;
	/**
	 * PLP, ROP, and OldPlt can only come in quantities of 1.
	 */
	public final static String STR_PLP_QTY = "1";

	//Hashtable Constants for Exception Handling
	public final static String ITM_CD_DESC = "ItmCdDesc";
	public final static String YR = "Yr";
	public final static String QTY = "Qty";
	public final static String BEG_NO = "BegNo";
	public final static String END_NO = "EndNo";

	/**
	 * Inventory is available to issue.
	 * It is not on hold.
	 */
	public final static int HOLD_INV_NOT = 0;
	/**
	 * Inventory is on user hold.
	 * It is not available to issue.
	 */
	public final static int HOLD_INV_USER = 1;
	/**
	 * Inventory is on system hold.
	 * This usually means we are in the process of issuing the item.
	 */
	public final static int HOLD_INV_SYSTEM = 2;
	/**
	 * Inventory is on VTR Reserve.
	 */
	public final static int HOLD_INV_RESERVE = 3;
	/**
	 * Inventory is on hold because there is a conflict with
	 * physical inventory.
	 */
	public final static int HOLD_INV_CONFLICT_ALLOC = 4;
	/**
	 * Inventory is on hold because there is a computation problem.
	 * The Inventory must have the computation problem resolved.
	 */
	public final static int HOLD_INV_COMPUTE_PROB = 5;
	/**
	 * The Special Plates Application has been completed.
	 * This inventory is available to be purged.
	 */
	public final static int HOLD_INV_ORDER_COMPLETE = 6;

	/**
	 * Normal Inventory Processing.
	 */
	public final static int INV_PROCSNGCD_NORMAL = 1;
	/**
	 * Special Plate Inventory Processing (allowed at county).
	 */
	public final static int INV_PROCSNGCD_SPECIAL = 2;
	/**
	 * Restricted Inventory Processing (not allowed at county).
	 */
	public final static int INV_PROCSNGCD_RESTRICTED = 3;
	/**
	 * Compute the current item
	 */
	public final static int COMPUTE_CURRENT_ITEM = 1;
	/**
	 * Compute the previous item.
	 */
	public final static int COMPUTE_PREVIOUS_ITEM = -1;
	/**
	 * Compute the next item.
	 */
	public final static int COMPUTE_NEXT_ITEM = 2;

	/**
	 * This is the prefix for an Annual Invoice.
	 */
	public final static String INVOICE_PREFIX_ANNUAL = "A";
	/**
	 * This is the prefix for a "Dummy" Invoice.
	 */
	public final static String INVOICE_PREFIX_DUMMY = "Z";
	/** 
	 * This is the prefix for a Supplemental Invoice.
	 */
	public final static String INVOICE_PREFIX_SUPPLEMENTAL = "S";

	/** 
	 * This is the prefix for a Virtual Inventory Invoice.
	 */
	public final static String INVOICE_PREFIX_VIRTUAL = "V";
	/**
	 * Number of characters an invoice number must be.
	 * This is both the minimum and the maximum.
	 */
	public final static int INVOICE_NUMBER_LENGTH = 6;

	//	defect 8269
	//public final static String DEFAULT_SELECTION =
	//	"PSP - PASSENGER PLT";
	public final static String DEFAULT_SELECTION = "PASSENGER PLT";
	// end defect 8269
	public final static String DEFAULT_ABBR = "PSP";

	// Precursor constants used below
	/**
	 * @value "Id"
	 */
	public static final String TXT_ID = "Id";
	/**
	 * @value "Name"
	 */
	public static final String TXT_NAME = "Name";

	/**
	 * @value "NO"
	 */
	public static final String TXT_NO_UC = "NO";

	// defect 8653
	/**
	 * @value "No"
	 */
	public static final String TXT_NO_MC = "No";
	// end defect 8653

	// Commoncly used Screen Constants
	/**
	 * @value "All"
	 */
	public static final String TXT_ALL = "All";
	/**
	 * @value "ALL EMPLOYEES"
	 */
	public static final String TXT_ALL_EMPLOYEES = "ALL EMPLOYEES";
	/**
	 * @value "All Substations"
	 */
	public static final String TXT_ALL_SUBSTATIONS = "All Substations";
	/**
	 * @value "Allocate"
	 */
	public static final String TXT_ALLOCATE = "Allocate";
	/**
	 * @value "Allocation Locations:"
	 */
	public static final String TXT_ALLOCATION_LOCATIONS =
		"Allocation Locations:";
	/**
	 * @value "Begin Date:"
	 */
	public static final String TXT_BEGIN_DATE_COLON = "Begin Date:";
	/**
	 * @value "Begin No"
	 */
	public static final String TXT_BEGIN_NO = "Begin No";
	/**
	 * @value "Central"
	 */
	public static final String TXT_CENTRAL = "Central";

	/**
	 * @value ", Do you want to continue?"
	 */
	public static final String TXT_COMMA_CONTINUE_QUESTION =
		CommonConstant.STR_COMMA
			+ CommonConstant.STR_SPACE_ONE
			+ CommonConstant.TXT_CONTINUE_QUESTION;
	/**
	 * @value "Are these values correct?"
	 */
	public static final String TXT_CORRECTVALUES_QUESTION =
		"Are these values correct?";
	/**
	 * @value "Current Balance"
	 */
	public static final String TXT_CURRENT_BALANCE = "Current Balance";
	/**
	 * @value "Date Range"
	 */
	public static final String TXT_DATE_RANGE = "Date Range";
	/**
	 * @value "Dealer"
	 */
	public static final String TXT_DEALER = "Dealer";
	/**
	 * @value "Dealer Id"
	 */
	public static final String TXT_DEALER_ID =
		TXT_DEALER + CommonConstant.STR_SPACE_ONE + TXT_ID;
	/**
	 * @value "Dealer Id:"
	 */
	public static final String TXT_DEALER_ID_COLON =
		TXT_DEALER_ID + CommonConstant.STR_COLON;
	/**
	 * @value "Dealer Name"
	 */
	public static final String TXT_DEALER_NAME =
		TXT_DEALER + CommonConstant.STR_SPACE_ONE + TXT_NAME;
	/**
	 * @value "DEFAULT"
	 */
	public static final String TXT_DEFAULT = "DEFAULT";
	/**
	 * @value "Delete"
	 */
	public static final String TXT_DELETE = "Delete";
	/**
	 * @value "Delete History Report"
	 */
	public static final String TXT_DELETE_HIST_RPT =
		"Delete History Report";
	/**
	 * @value "Delete Location"
	 */
	public static final String TXT_DELETE_LOCATION = "Delete Location";
	/**
	 * @value "Delete Location:"
	 */
	public static final String TXT_DELETE_LOCATION_COLON =
		TXT_DELETE_LOCATION + CommonConstant.STR_COLON;
	/**
	 * @value "Destination:" 
	 */
	public static final String TXT_DESTINATION = "Destination";
	/**
	 * @value "Destination:"
	 */
	public static final String TXT_DESTINATION_COLON =
		TXT_DESTINATION + CommonConstant.STR_COLON;
	/**
	 * @value "Destination Only"
	 */
	public static final String TXT_DESTINATION_ONLY =
		TXT_DESTINATION + " Only";
	/**
	 * @value "Employee"
	 */
	public static final String TXT_EMPLOYEE = "Employee";
	/**
	 * @value "Employee Id"
	 */
	public static final String TXT_EMPLOYEE_ID =
		TXT_EMPLOYEE + CommonConstant.STR_SPACE_ONE + TXT_ID;
	/**
	 * @value "Employee Id:"
	 */
	public static final String TXT_EMPLOYEE_ID_COLON =
		TXT_EMPLOYEE_ID + CommonConstant.STR_COLON;
	/**
	 * @value "Employee Name"
	 */
	public static final String TXT_EMPLOYEE_NAME =
		TXT_EMPLOYEE + CommonConstant.STR_SPACE_ONE + TXT_NAME;
	/**
	 * @value "End Date:"
	 */
	public static final String TXT_END_DATE_COLON = "End Date:";
	/**
	 * @value "End No"
	 */
	public static final String TXT_END_NO = "End No";
	/**
	 * @value "Enter the Report Date:"
	 */
	public static final String TXT_ENTER_REPORT_DATE_COLON =
		"Enter the Report Date:";
	/**
	 * @value "Entity"
	 */
	public static final String TXT_ENTITY = "Entity";
	/**
	 * @value "Entity:"
	 */
	public static final String TXT_ENTITY_COLON =
		TXT_ENTITY + CommonConstant.STR_COLON;
	/**
	 * @value "Entity Id"
	 */
	public static final String TXT_ENTITY_ID =
		TXT_ENTITY + CommonConstant.STR_SPACE_ONE + TXT_ID;
	/**
	 * @value "Entity & Id"
	 */
	public static final String TXT_ENTITY_AND_ID =
		TXT_ENTITY + " & " + TXT_ID;
	/**
	 * @value "Entity Selection:"
	 */
	public static final String TXT_ENTITY_SELECTION_COLON =
		TXT_ENTITY + " Selection" + CommonConstant.STR_SPACE_ONE;
	/**
	 * @value "Exception Report"
	 */
	public static final String TXT_EXCEPTION_REPORT =
		"Exception Report";
	/**
	 * @value "FROM:"
	 */
	public static final String TXT_FROM_COLON = "FROM:";
	/**
	 * @value "From Entity:"
	 */
	public static final String TXT_FROM_ENTITY_COLON = "From Entity:";
	/**
	 * @value "FROM Location"
	 */
	public static final String TXT_FROM_LOCATION = "FROM Location";
	/**
	 * @value "History"
	 */
	public static final String TXT_HISTORY = "History";

	/**
	 * @value "Inventory Inquiry"
	 */
	public static final String TXT_INV_INQ = "Inventory Inquiry";
	/**
	 * @value "Inventory Inquiry History Dates"
	 */
	public static final String TXT_INV_INQ_HIST_DATES =
		"Inventory Inquiry History Dates";
	/**
	 * @value "Inventory Inquiry History Dates:"
	 */
	public static final String TXT_INV_INQ_HIST_DATES_COLON =
		TXT_INV_INQ_HIST_DATES + CommonConstant.STR_COLON;
	/**
	 * @value "Inventory Inquiry Selection"
	 */
	public static final String TXT_INV_INQ_SELECTION =
		"Inventory Inquiry Selection";
	/**
	 * @value "Inventory Inquiry Selection:"
	 */
	public static final String TXT_INV_INQ_SELECTION_COLON =
		TXT_INV_INQ_SELECTION + CommonConstant.STR_COLON;
	/**
	 * @value "Inventory Inquiry Selection By:"
	 */
	public static final String TXT_INV_INQ_SELECTION_BY_COLON =
		TXT_INV_INQ_SELECTION + " By" + CommonConstant.STR_COLON;
	/**
	 * @value "Inventory Inquiry Type"
	 */
	public static final String TXT_INV_INQ_TYPE =
		"Inventory Inquiry Type";
	/**
	 * @value "Inventory Inquiry Type:"
	 */
	public static final String TXT_INV_INQ_TYPE_COLON =
		TXT_INV_INQ_TYPE + CommonConstant.STR_COLON;

	// defect 8595 
	/**
	 * @value "InvProfile"
	 */
	public static final String TXT_INVPROFILE = "InvProfile";
	// end defect 8595 

	/**
	 * @value " now will be put in "
	 */
	public static final String TXT_INVOICE_WILL_BE_PUT_IN =
		" now will be put in ";
	/**
	 * @value " inventory, Do you want to continue?"
	 */
	public static final String TXT_INVOICE_RECV =
		" inventory, Do you want to continue?";
	/**
	 * @value "Invoice"
	 */
	public static final String TXT_INVOICE = "Invoice";
	/**
	 * @value "Invoice "
	 */
	public static final String TXT_INVOICE_SPACE =
		TXT_INVOICE + CommonConstant.STR_SPACE_ONE;

	// defect 8653
	/**
	 * @value "Invoice No:"
	 */
	public static final String TXT_INVOICE_NO_COLON =
		TXT_INVOICE_SPACE + TXT_NO_MC + CommonConstant.STR_COLON;
	// end defect 8653

	/**
	 * @value "Item Description"
	 */
	public static final String TXT_ITEM_DESCRIPTION =
		"Item Description";
	/**
	 * @value "Item Number:"
	 */
	public static final String TXT_ITEM_NUMBER_COLON = "Item Number:";
	/**
	 * @value "Item Year"
	 */
	public static final String TXT_ITEM_YEAR = "Item Year";
	/**
	 * @value "Invoice Number"
	 */
	public static final String TXT_INVOICE_NUMBER = "Invoice Number";
	
	// defect 10207 
	//	/**
	//	 * @value "Last Insert Date"
	//	 */
	//	public static final String TXT_LAST_INSERT_DATE =
	//		"Last Insert Date";
	// end defect 10207 

	/**
	 * @value "Last Activity"
	 */
	public static final String TXT_LAST_ACTIVITY = "Last Activity";
	
	/**
	 * @value "Main Office"
	 */
	public static final String TXT_MAIN_OFFICE = "Main Office";
	/**
	 * @value "MAIN OFFICE"
	 */
	public static final String TXT_MAIN_OFFICE_UC =
		TXT_MAIN_OFFICE.toUpperCase();
	/**
	 * @value "Max"
	 */
	public static final String TXT_MAX = "Max";
	/**
	 * @value "Maximum Quantity:"
	 */
	public static final String TXT_MAX_QTY_COLON = "Maximum Quantity:";
	/**
	 * @value "Min"
	 */
	public static final String TXT_MIN = "Min";
	/**
	 * @value "Minimum Quantity:"
	 */
	public static final String TXT_MIN_QTY_COLON = "Minimum Quantity:";
	/**
	 * @value "Next Item"
	 */
	public static final String TXT_NEXT_ITEM = "Next Item";

	/**
	 * @value "Office Issuance No"
	 */
	public static final String TXT_OFFICE_ISSUANCE_NO =
		"Office Issuance No";
	/**
	 * @value "Office Location"
	 */
	public static final String TXT_OFFICE_LOCATION = "Office Location";
	/**
	 * @value "Office Name"
	 */
	public static final String TXT_OFFICE_NAME = "Office Name";

	/**
	 * @value "Order Date:"
	 */
	public static final String TXT_ORDER_DATE_COLON = "Order Date:";
	/**
	 * @value "Prompt For Next Item:  "
	 */
	public static final String TXT_PROMPT_FOR_NEXT_ITEM_COLON =
		"Prompt For Next Item:  ";
	/**
	 * @value "Quantity"
	 */
	public static final String TXT_QUANTITY = "Quantity";
	/**
	 * @value "Reason Code"
	 */
	public static final String TXT_REASON_CODE = "Reason Code";
	/**
	 * @value "Reason Description"
	 */
	public static final String TXT_REASON_DESCRIPTION =
		"Reason Description";
	/**
	 * @value "Receive History Report"
	 */
	public static final String TXT_RECEIVE_HIST_RPT =
		"Receive History Report";
	/**
	 * @value "Receive Into:"
	 */
	public static final String TXT_RECEIVE_INTO_COLON = "Receive Into:";
	/**
	 * @value "Report Text:"
	 */
	public static final String TXT_REPORT_TEXT_COLON = "Report Text:";
	/**
	 * @value "Select all counties"
	 */
	public static final String TXT_SELECT_ALL_COUNTIES =
		"Select all counties";
	/**
	 * @value "Select All Item(s)"
	 */
	public static final String TXT_SELECT_ALL_ITEMS =
		"Select All Item(s)";
	/**
	 * @value "Select Begin Date & End Date For History:"
	 */
	public static final String TXT_SELECT_BEG_END_DATE_HIST =
		"Select Begin Date & End Date For History:";
	/**
	 * @value "Select FROM" 
	 */
	public static final String TXT_SELECT_FROM = "Select FROM ";
	/**
	 * @value "Select FROM Entity:"
	 */
	public static final String TXT_SELECT_FROM_ENTITY_COLON =
		"Select FROM Entity:";
	/**
	 * @value "Select FROM Location:"
	 */
	public static final String TXT_SELECT_FROM_LOCATION_COLON =
		"Select FROM Location:";
	/**
	 * @value "Selected item detailed status:"
	 */
	public static final String TXT_SELCTDITM_DETAILED_STATUS =
		"Selected item detailed status:";
	/**
	 * @value "Select Substation(s)  " 
	 */
	public static final String TXT_SELECT_SUBSTATIONS =
		"Select Substation(s)  ";
	/**
	 * @values "Item Type(s) and Year"
	 */
	public static final String TXT_ITEM_TYPES_AND_YEAR =
		"Item Type(s) and Year";
	/**
	 * @value "Select All Ranges"
	 */
	public static final String TXT_SELECT_ALL_RANGES =
		"Select All Ranges";
	/**
	 * @value "Select Item Type:"
	 */
	public static final String TXT_SELECT_ITEM_TYPE_COLON =
		"Select Item Type:";
	/**
	 * @value "Select One"
	 */
	public static final String TXT_SELECT_ONE = "Select One";
	/**
	 * @value "Select One:"
	 */
	public static final String TXT_SELECT_ONE_COLON =
		TXT_SELECT_ONE + CommonConstant.STR_COLON;
	/**
	 * @value "Select One or Both:"
	 */
	public static final String TXT_SELECT_ONE_OR_BOTH_COLON =
		TXT_SELECT_ONE + " or Both:";
	/**
	 * @value "Select one or more"
	 */
	// defect 9180
	//public static final String TXT_SELECT_ONE_OR_MORE =
	//	TXT_SELECT_ONE + " or more";
	public static final String TXT_SELECT_ONE_OR_MORE =
		TXT_SELECT_ONE + " or More";
	// end defect 9180
	/**
	 * @value "Select one or more:"
	 */
	public static final String TXT_SELECT_ONE_OR_MORE_COLON =
		TXT_SELECT_ONE_OR_MORE + CommonConstant.STR_COLON;
	/**
	 * @value "Select Report:"
	 */
	public static final String TXT_SELECT_REPORT_COLON =
		"Select Report:";
		
	// defect 10207 
	/**
	 * @value "Select Search Type:"
	 */
	public static final String TXT_SELECT_SEARCH_TYPE_COLON =
		"Select Search Type:";
	// end defect 10207  
	
	/**
	 * @value "Select Substation Office:"
	 */
	public static final String TXT_SELECT_SUBSTA_COLON =
		"Select Substation Office:";
	/**
	 * @value "Select TO" 
	 */
	public static final String TXT_SELECT_TO = "Select TO ";
	/**
	 * @value "Select TO Entity:"
	 */
	public static final String TXT_SELECT_TO_ENTITY_COLON =
		"Select TO Entity:";
	/**
	 * @value "Select TO Location:"
	 */
	public static final String TXT_SELECT_TO_LOCATION_COLON =
		"Select TO Location:";
	/**
	 * @value "Selection Criteria:"
	 */
	public static final String TXT_SELECTION_CRITERIA_COLON =
		"Selection Criteria:";
	/**
	 * @value "Server"
	 */
	public static final String TXT_SERVER = "Server";
	/**
	 * @value "Specific Entity"
	 */
	public static final String TXT_SPECIFIC_ENTITY = "Specific Entity";
	/**
	 * @value "Specific From Entity"
	 */
	public static final String TXT_SPECIFIC_FROM_ENTITY =
		"Specific From Entity";
	/**
	 * @value "Specific Item Number and Year"
	 */
	public static final String TXT_SPECIFIC_ITEM_NO_AND_YEAR =
		"Specific Item Number and Year";
	/**
	 * @value "Specific To Entity"
	 */
	public static final String TXT_SPECIFIC_TO_ENTITY =
		"Specific To Entity";
	/**
	 * @value "Subcontractor"
	 */
	public static final String TXT_SUBCONTRACTOR = "Subcontractor";
	/**
	 * @value "Subcontractor Id"
	 */
	public static final String TXT_SUBCONTRACTOR_ID =
		TXT_SUBCONTRACTOR + CommonConstant.STR_SPACE_ONE + TXT_ID;
	/**
	 * @value "Subcontractor Id:"
	 */
	public static final String TXT_SUBCONTRACTOR_ID_COLON =
		TXT_SUBCONTRACTOR_ID + CommonConstant.STR_COLON;
	/**
	 * @value "Subcontractor Name"
	 */
	public static final String TXT_SUBCONTRACTOR_NAME =
		TXT_SUBCONTRACTOR + CommonConstant.STR_SPACE_ONE + TXT_NAME;
	/**
	 * @value "Substation"
	 */
	public static final String TXT_SUBSTATION = "Substation";
	/**
	 * @value "Substation Location"
	 */
	public static final String TXT_SUBSTA_LOCATION =
		TXT_SUBSTATION + " Location";
	/**
	 * @value "Substation Location:"
	 */
	public static final String TXT_SUBSTA_LOCATION_COLON =
		TXT_SUBSTA_LOCATION + CommonConstant.STR_COLON;
	/**
	 * @value "TO:"
	 */
	public static final String TXT_TO_COLON = "TO:";
	/**
	 * @value "To Entity:"
	 */
	public static final String TXT_TO_ENTITY_COLON = "To Entity:";
	/**
	 * @value "To Location"
	 */
	public static final String TXT_TO_LOCATION = "To Location";
	/**
	 * @value "Verification On:"
	 */
	public static final String TXT_VERIFICATION_ON_COLON =
		"Verification On:";
	/**
	 * @value "View Inventory Allocate Report"
	 */
	public static final String TXT_VIEW_INV_ALLOC_RPT =
		"View Inventory Allocate Report";
	/**
	 * @value "View Inventory Delete Report"
	 */
	public static final String TXT_VIEW_INVENTORY_DELETE_REPORT =
		"View Inventory Delete Report";
	/**
	 * @value "View Inventory on Hold Report"
	 */
	public static final String TXT_VIEW_INV_HOLD_RPT =
		"View Inventory on Hold Report";
	/**
	 * @value "View Inventory Received Report"
	 */
	public static final String TXT_VIEW_INV_REC_RPT =
		"View Inventory Received Report";
	/**
	 * @value "Workstation"
	 */
	public static final String TXT_WORKSTATION = "Workstation";
	/**
	 * @value "Workstation Id"
	 */
	public static final String TXT_WORKSTATION_ID =
		TXT_WORKSTATION + CommonConstant.STR_SPACE_ONE + TXT_ID;
	/**
	 * @value "Workstation Id:"
	 */
	public static final String TXT_WORKSTATION_ID_COLON =
		TXT_WORKSTATION_ID + CommonConstant.STR_COLON;
	/**
	 * @value "Year"
	 */
	public static final String TXT_YEAR = "Year";
	/**
	 * @value "Year:"
	 */
	public static final String TXT_YEAR_COLON =
		TXT_YEAR + CommonConstant.STR_COLON;
	/**
	 * @value "YES"
	 */
	public static final String TXT_YES_UC = "YES";

	// this must follow the fields referenced in here.
	/**
	 * @value "Item Description\n>>>> Reason Description"
	 */
	public static final String TXT_ITEM_DESCRIPTION_REASON_DESC =
		TXT_ITEM_DESCRIPTION + "\n>>>> " + TXT_REASON_DESCRIPTION;
		
	/**
	 * Allow an override for testing.
	 * 
	 * @param aiNewMax
	 */
	public static void setMaxQty(int aiNewMax)
	{
		Log.write(
			Log.START_END,
			new Integer(aiNewMax),
			"Max Inventory Qty has been changed! " + aiNewMax);
		MAX_QTY = aiNewMax;
	}

	// defect 10207 
	public final static int INV015_COL_OFFICE_NO = 0;
	public final static int INV015_COL_OFFICE_NAME = 1;
	public final static int INV015_COL_LAST_ACTIVITY = 2;
	public final static int INV024_COL_ITEM_DESCR = 0;
	public final static int INV024_COL_ITEM_YEAR = 1;
	// end defect 10207 

}
