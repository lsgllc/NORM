package com.txdot.isd.rts.services.util.constants;

/*
 *
 * AccountingConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/21/2005	RTS 5.2.3 Code Cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	01/25/2008	add MAX_RGN_COLL_INV_QTY 
 * 							defect 9519 Ver 3 Amigos Prep 
 * K Harrell 	02/14/2008	reassign MAX_RGN_COLL_INV_QTY to 3
 * 							defect 9519 Ver 3 Amigos Prep
 * ---------------------------------------------------------------------
 */

/**
 * Constants for the Accounting Module
 *
 * @version	3 Amigos Prep	02/14/2008 
 * @author	Mike Abernethy
 * <br>Creation Date:		07/17/2001 16:50:57
 */

public class AccountingConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int REMIT_FUNDS_DUE_RECORDS = 1;
	public final static int RETRIEVE_FUNDS_DUE_SUMMARY_RECORDS = 2;
	public final static int RETRIEVE_ACCOUNT_CODES = 3;
	public final static int PROCESS_HOT_CHECK_CREDIT = 4;
	public final static int PROCESS_HOT_CHECK_REDEEM = 5;
	public final static int PROCESS_DEDUCT_HOT_CHECK_CREDIT = 6;
	public final static int ITEM_SEIZED = 7;
	public final static int VOID_PAYMENT = 8;
	public final static int PROCESS_REFUND = 9;
	public final static int SEARCH_PAYMENT_RECORDS = 10;
	public final static int REGIONAL_INVENTORY = 11;

	public final static java.lang.String DATA = "DATA";
	public final static java.lang.String TRACE_NO = "TRACE_NO";
	public final static java.lang.String PAYMENT_COMPLETE =
		"PAYMENT_COMPLETE";
	public final static java.lang.String UPDATE_DATA = "UPDATE_DATA";
	public final static java.lang.String OFC = "OFC";
	public final static java.lang.String SUB = "SUB";
	
	// defect 9519 
	public final static int MAX_RGN_COLL_INV_QTY = 3; 
	// end defect 9519 
}
