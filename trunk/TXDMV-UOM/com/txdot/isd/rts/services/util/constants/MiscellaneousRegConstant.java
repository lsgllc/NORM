package com.txdot.isd.rts.services.util.constants;

/*
 *
 * MiscellaneousRegConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/21/2005	Code Cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	07/19/2009	delete ZIP_P4_LENGTH, ZIP_LENGTH,
 * 							 INCORRECT_FIELD_ENTRY,INVALID_STATE_ENTRY
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	07/26/2009	add MRG021_COL_CUSTID, MRG021_COL_NAME,	
 * 							 MRG021_COL_ADDRESS, MRG021_COL_TYPE, 
 * 							 MRG021_COL_PLACARDS, MRG023_COL_PLACARD,
 * 							 MRG023_COL_DESCRIPTION, 
 * 							 MRG023_COL_ISSUE_DATE, MRG023_COL_EXP_DATE,
 * 							 MRG023_COL_TYPE, PERMANENT_PLCRD_ITMCD, 
 *							 TEMPORARY_PLCRD_ITMCD
 * 							delete MOBILITY_MNEMONIC,
 * 							 NONMOBILITY_MNEMONIC, MOBLTY_PLCRD_ITMCD,
 * 							 NONMOBLTY_PLCRD_ITMCD
 * 							modify REPLACE_TRANSCD_PREFIX, 
 * 							 DELETE_TRANSCD_PREFIX
 *							defect 10133 Ver Defect_POS_F 
 * K Harrell	05/25/2010	add PRMTINQ
 * 							defect 10491 Ver 6.5.0
 * K Harrell	06/25/2010	add ITMCD_30PT, ITMCD_30MCPT,
 * 							 ITMCD_OTPT,ITMCD_OTMCPT, 
 * 							 MIN_30_DAY_PERMITS_FOR_MSG, 
 * 							 MAX_PRMT_INQ_MOS
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/14/2010	delete MAX_PRMT_INQ_MOS
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	10/23/2010	add MAX_DP_ROWS_FOR_CICS
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	01/07/2011	add TIMED_PERMIT_VENDOR_BSN_TYPE_CD, 
 * 							ITMCD_FDPT, ITMCD_72PT, ITMCD_144PT 
 * 							defect 10726 Ver 6.7.0 
 * K Harrell	05/28/2011  add DSABLD_PLCRD_HASACTIVE,
 * 							 DSABLD_PLCRD_MAXACTIVE_EXPDATE
 * 							defect 10831 Ver 6.8.0 
 * K Harrell	06/22/2011	add PERMIT_REGULAR_VEHTYPECD,
 * 							 PERMIT_MOTORCYCLE_VEHTYPECD
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	01/11/2012	add MRG023_COL_DELETE_REASON
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Miscellaneous Registration 
 *
 * @version	6.10.0  		01/11/2012
 * @author	Joseph Kwik
 * <br>Creation Date:		08/23/2001 11:31:32
 */

public class MiscellaneousRegConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int INIT_TIMED_PERMIT = 1;
	public final static int INIT_DISABLED_PARKING_CARD = 2;
	public final static int INIT_TEMP_ADDL_WEIGHT = 3;

	public final static int MINIMUM_YEAR = 1900;
	public final static int MAXIMUM_YEAR = 2099;

	public final static String INCORRECT_MODEL_YEAR_ENTRY =
		"Model Year is not valid.";

	public final static String INVALID_EXP_MONTH_ENTRY =
		"Expiration month must be between 1 and 12";
	public final static String INVALID_EXP_YEAR_ENTRY =
		"Expiration year is not valid";
	public final static String ERROR_TITLE = "ERROR!";

	// defect 9831 
	public final static int CUST_ID = 1;
	public final static int PLACARD_NUMBER = 2;
	public final static int DISABLED_NAME = 3;
	public final static int INSTITUTION_NAME = 4;
	public final static int SEARCH = 20;
	public final static int INSERT = 21;
	public final static int UPDATE = 22;
	public final static int GENERATE_DSABLD_PLCRD_REPORT = 23;
	public final static int GENERATE_DSABLD_PLCRD_EXPORT = 24;
	public final static int UPDATE_MVDI_EXP_HSTRY = 25;
	public final static int CHKIFVOIDABLE = 26;
	public final static int RESETINPROCS = 27;
	public final static int SETINPROCS = 28;
	public final static int REINSTATE = 29; 
	public final static int DP_ADD_TRANS_TYPE_CD = 0;
	public final static int DP_DEL_TRANS_TYPE_CD = 1;
	public final static int DP_UNDEL_TRANS_TYPE_CD = 2;
	
	public final static String PLACARD_NO = "Placard No:";
	public final static String PLACARD_DESC = "Placard Desc:";
	public final static String ISSUE_DATE = "Issue Date:";
	public final static String EXPIRATION_DATE = "Expiration Date:";
	public final static String ISSUE_TWO_PLACARDS =
		"Issue Two Placards";
	public final static String DELETE_REASON = "Delete Reason:";
	public final static String REPLACE_REASON = "Replace Reason:";
	public final static String CHARGE_FEE = "Charge Fee";
	public final static String PERMANENT_MNEMONIC = "P";
	public final static String TEMPORARY_MNEMONIC = "T";
	public final static String INQ = "INQ";
	// end defect 9831 

	// defect 10133
	// public final static String REPLACE_TRANSCD_PREFIX = "RP";
	// public final static String DELETE_TRANSCD_PREFIX = "DL";
	// public final static String MOBILITY_MNEMONIC = "M";
	// public final static String NONMOBILITY_MNEMONIC = "N";
	// public final static String MOBLTY_PLCRD_ITMCD = "PDC";
	// public final static String NONMOBLTY_PLCRD_ITMCD = "TDC";
	public final static String REPLACE_TRANSCD_PREFIX = "RPL";
	public final static String DELETE_TRANSCD_PREFIX = "DEL";
	public final static String REINSTATE_TRANS_CD_PREFIX = "REI";
	public final static String RENEW_TRANS_CD_PREFIX = "REN";
	public final static String PERMANENT_PLCRD_ITMCD = "PDC";
	public final static String TEMPORARY_PLCRD_ITMCD = "TDC";

	public final static int MRG021_COL_CUSTID = 0;
	public final static int MRG021_COL_NAME = 1;
	public final static int MRG021_COL_ADDRESS = 2;
	public final static int MRG021_COL_TYPE = 3;
	public final static int MRG021_COL_PLACARDS = 4;

	public final static int MRG023_COL_PLACARD = 0;
	public final static int MRG023_COL_DESCRIPTION = 1;
	public final static int MRG023_COL_ISSUE_DATE = 2;
	public final static int MRG023_COL_EXP_DATE = 3;
	public final static int MRG023_COL_TYPE = 4;
	// end defect 10133
	
	// defect 11214
	public final static int MRG023_COL_DELETE_REASON = 5;
	// end defect 11214 

	// defect 10491 
	public final static String PRMINQ = "PRMINQ";
	public final static int PRMTINQ = 29;
	public final static String ITMCD_30PT = "30PT";
	public final static String ITMCD_30MCPT = "30MCPT";
	public final static String ITMCD_OTPT = "OTPT";
	public final static String ITMCD_OTMCPT = "OTMCPT";
	public final static int MIN_30_DAY_PERMITS_FOR_MSG = 3;
	// end defect 10491 

	// defect 10607 
	public final static int MAX_DP_ROWS_FOR_CICS = 5;
	// end defect 10607

	// defect 10726
	public final static String ITMCD_FDPT = "FDPT";
	public final static String ITMCD_72PT = "72PT";
	public final static String ITMCD_144PT = "144PT";
	public final static String TIMED_PERMIT_VENDOR_BSN_TYPE_CD =
		"BULKPERMIT";
	// end defect 10726

	// defect 10831 
	public final static String DSABLD_PLCRD_HASACTIVE = "DPHASACTIVE";
	public final static String DSABLD_PLCRD_MAXACTIVE_EXPDATE =
		"DPMAXACTIVEEXPYRMO";
	// end defect 10831
	 
	// defect 10844 
	public final static String PERMIT_REGULAR_VEHTYPECD = "R";
	public final static String PERMIT_MOTORCYCLE_VEHTYPECD = "M";
	// end defect 10844 
}
