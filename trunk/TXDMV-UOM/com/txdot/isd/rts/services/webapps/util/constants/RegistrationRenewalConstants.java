package com.txdot.isd.rts.services.webapps.util.constants;

/*
 *
 * RegistrationRenewalConstants.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/31/2005	Java 1.4 work
 *							defect 7889 Ver 5.2.3
 * B Brown		04/28/2006  Add 2 testing constants
 * 							add MQ_TEST_QRY
 * 							add DO_PURGE
 * 							defect 8554 Ver 5.2.3
 * K Harrell	05/29/2009	add REG102_COL_PLATE_NO, REG102_COL_REC_NAME,
 * 							 REG102_COL_ITRNT_DATE, REG102_COL_CNTY_DATE,
 * 							 REG102_COL_STATUS
 * 							defect 8749 Ver Defect_POS_F 
 * B Brown		12/04/2009	add TXO_PROCESS_REFUND_RESULT
 * 							defect 10019 ver Defect_POS_H	
 * B Brown		01/21/2011	add UPDATE_ITRNT_DATA_FEES
 * 							defect 10714 ver POS_670     
 * S Carlin		09/07/2011	add GET_VEHICLE_COLORS
 * 							defect 10985 ver POS_681
 * ---------------------------------------------------------------------
 */

/**
 * This is a Constants class for internet registration renewal  
 *
 * @version	6.8.1	09/07/2011
 * @author	James Giroux
 * <br>Creation Date:		10/09/2001 17:08:57
 */

public class RegistrationRenewalConstants
{
	// Function Ids
	public final static int GET_REG_REN_VEHICLE = 1;
	public final static int DO_REG_RENEWAL = 2;

	// query the county list, not a frequent operation
	public final static int QUERY_COUNTY = 3;

	// From TxDOT to TexasOnline request
	public final static int DO_REFUND = 4;
	public final static int DO_BATCH_PAYMENT = 5;

	// Batch job control from TxO
	public final static int TXO_DO_BATCH_REFUND = 6;
	public final static int TXO_DO_BATCH_PAYMENT = 7;
	public final static int TXO_DO_BATCH_STATUS = 8;
	public final static int TXO_DO_BATCH_EMAIL = 9;
	public final static int TXO_DO_BATCH_ALL = 10;
	public final static int DO_REG_REN_QRY = 11;

	// defect 8554
	public final static int MQ_TEST_QRY = 12;
	public final static int DO_PURGE = 13;
	public final static int GET_ITRNT_DATA_BLOB = 14;
	// end defect 8554
	
	// defect 10019
	public final static int TXO_PROCESS_REFUND_RESULT = 15;
	// end defect 10019
	
	// defect 10714
	public final static int UPDATE_ITRNT_DATA_FEES = 16;
	// end defect 10714

	// defect 10985
	public final static int GET_VEHICLE_COLORS = 17;
	// end defect 10985

	// Which application
	public static final int NO_REGISTRATION_APP = 0;
	public static final int NEW_REGISTRATION_APP = 1;
	public static final int OLD_REGISTRATION_APP = 2;

	// defect 8749
	public final static int REG102_COL_PLATE_NO = 0;
	public final static int REG102_COL_REC_NAME = 1;
	public final static int REG102_COL_ITRNT_DATE = 2;
	public final static int REG102_COL_CNTY_DATE = 3;
	public final static int REG102_COL_STATUS = 4;
	// end defect 8749 
}
