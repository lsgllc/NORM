package com.txdot.isd.rts.services.webapps.util.constants;

/*
 *
 * CommonConstants.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/31/2005	Java 1.4 work
 *							defect 7889 Ver 5.2.3
 * B Brown      01/25/2006  Add the following 2 variables:
 *				 			PAYMENT_IN_PROCESS
 *						    PAYMENT_PREAUTH_FAILED
 *                          defect 8365 Ver 5.2.2 fix 8.
 * B Brown      04/12/2007  Add APPLICATION
 *                          defect 9119 Ver Special Plates
 * Jeff S.      04/26/2007  Add TRANS_STATUS codes and a new payment
 * 							status code for capture failed.
 * 							add TRANS_STATUS_ACTIVE, 
 * 								TRANS_STATUS_COMPLETED, 
 * 								TRANS_STATUS_INACTIVE, 
 * 								TRANS_STATUS_PENDING, 
 * 								TRANS_STATUS_REFUND,
 * 								PAYMENT_CAPTURE_FAILED
 *                          defect 9121 Ver Special Plates
 * Jeff S.      07/05/2007  Added variables used for tracenumbers.
 * 							moved here because it is shared between TXO
 * 							and POS Server Side Batch.
 * 							add APPLICATION_CNTY, APPLICATION_CODE
 * 							defect 9121 Ver Special Plates
 * B Brown		07/09/2008	Added constant for Vendor Plate processing. 
 * 							add VENDOR_APPLICATION_CODE
 * 							defect 9711 Ver MyPlates_POS
 * B Brown		12/08/2010	Add EREMINDER and ADD_EREMINDER constant.
 * 							defect 10610 Ver POS_670
 * B Brown		05/05/2011	Change APPLICATION_CNTY to 608
 * 							defect 10807 Ver POS_671								
 * ---------------------------------------------------------------------
 */

/**
 * Common Constants used for Internet Processing
 *
 * @version	POS_671			05/05/2011
 * @author	James Giroux
 * <br>Creation Date:		10/05/2001 12:54:35
 */

public class CommonConstants
{

	// Modules (Applications)
	public final static int ADDRESS_CHANGE = 1;
	public final static int REGISTRATION_RENEWAL = 2;
	public final static int APPLICATION = 3;
	
	// defect 10610
	// EREMINDER is module name for EREMINDER checkbox
	public final static int EREMINDER = 4;
	// ADD_EREMINDER is function ID for EREMINDER process
	public final static int ADD_EREMINDER = 41;
	// end defect 10610 

	// Status Constants
	public final static int UNPAID = 1;
	public final static int NEW = 2;
	public final static int IN_PROCESS = 3;
	public final static int APPROVED = 4;
	public final static int HOLD = 5;
	public final static int DECLINED_REFUND_PENDING = 6;
	public final static int DECLINED_REFUND_APPROVED = 7;
	public final static int DECLINED_REFUND_FAILED = 8;
	
	// Special Plate Order - Trans Status Constants
	public final static String TRANS_STATUS_ACTIVE = "A";
	public final static String TRANS_STATUS_COMPLETED = "C";
	public final static String TRANS_STATUS_INACTIVE = "I";
	public final static String TRANS_STATUS_PENDING = "P";
	public final static String TRANS_STATUS_REFUND = "R";
	
	// Special Plate Order Trace No Variables
	// Used to build Trace No.
	// defect 10807
	// public static final String APPLICATION_CNTY = "601";
	public static final String APPLICATION_CNTY = "608";
	// end defect 10807
	public static final String APPLICATION_CODE = "SP";
	
	// defect 9711
	public static final String VENDOR_APPLICATION_CODE = "VP";
	// end defect 9711
	
	// The payment is separated into two epay transactions.
	// for every record, payments are preauthorized,
	// the following is payment capture StatusCd.
	public final static int PAYMENT_CAPTURE_SUCCESS = 0;
	public final static int PAYMENT_CAPTURE_RENEWAL_FAILED = 1;
	public final static int PAYMENT_CAPTURE_CONVFEE_FAILED = 2;
	public final static int PAYMENT_CAPTURE_BOTH_FAILED = 3;
	// defect 8365
	public final static int PAYMENT_IN_PROCESS = 4;
	public final static int PAYMENT_PREAUTH_FAILED = 5;
	// end defect 8365
	public final static int PAYMENT_CAPTURE_FAILED = 6;
	
	public final static int EPAY_ERROR = 99;

	public final static int BATCH_PAYMENT_FAILED = -1;

	/**
	 * CommonConstants constructor comment.
	 */
	public CommonConstants()
	{
		super();
	}
}
