package com.txdot.isd.rts.services.util.constants;/* * * MiscellaneousRegConstant.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	06/21/2005	Code Cleanup * 							defect 7885 Ver 5.2.3 * K Harrell	07/19/2009	delete ZIP_P4_LENGTH, ZIP_LENGTH, * 							 INCORRECT_FIELD_ENTRY,INVALID_STATE_ENTRY * 							defect 10127 Ver Defect_POS_F  * K Harrell	07/26/2009	add MRG021_COL_CUSTID, MRG021_COL_NAME,	 * 							 MRG021_COL_ADDRESS, MRG021_COL_TYPE,  * 							 MRG021_COL_PLACARDS, MRG023_COL_PLACARD, * 							 MRG023_COL_DESCRIPTION,  * 							 MRG023_COL_ISSUE_DATE, MRG023_COL_EXP_DATE, * 							 MRG023_COL_TYPE, PERMANENT_PLCRD_ITMCD,  *							 TEMPORARY_PLCRD_ITMCD * 							delete MOBILITY_MNEMONIC, * 							 NONMOBILITY_MNEMONIC, MOBLTY_PLCRD_ITMCD, * 							 NONMOBLTY_PLCRD_ITMCD * 							modify REPLACE_TRANSCD_PREFIX,  * 							 DELETE_TRANSCD_PREFIX *							defect 10133 Ver Defect_POS_F  * K Harrell	05/25/2010	add PRMTINQ * 							defect 10491 Ver 6.5.0 * K Harrell	06/25/2010	add ITMCD_30PT, ITMCD_30MCPT, * 							 ITMCD_OTPT,ITMCD_OTMCPT,  * 							 MIN_30_DAY_PERMITS_FOR_MSG,  * 							 MAX_PRMT_INQ_MOS * 							defect 10491 Ver 6.5.0   * K Harrell	07/14/2010	delete MAX_PRMT_INQ_MOS * 							defect 10491 Ver 6.5.0  * K Harrell	10/23/2010	add MAX_DP_ROWS_FOR_CICS * 							defect 10607 Ver 6.6.0  * K Harrell	01/07/2011	add TIMED_PERMIT_VENDOR_BSN_TYPE_CD,  * 							ITMCD_FDPT, ITMCD_72PT, ITMCD_144PT  * 							defect 10726 Ver 6.7.0  * K Harrell	05/28/2011  add DSABLD_PLCRD_HASACTIVE, * 							 DSABLD_PLCRD_MAXACTIVE_EXPDATE * 							defect 10831 Ver 6.8.0  * K Harrell	06/22/2011	add PERMIT_REGULAR_VEHTYPECD, * 							 PERMIT_MOTORCYCLE_VEHTYPECD * 							defect 10844 Ver 6.8.0   * K Harrell	01/11/2012	add MRG023_COL_DELETE_REASON * 							defect 11214 Ver 6.10.0  * --------------------------------------------------------------------- *//** * Constants for Miscellaneous Registration  * * @version	6.10.0  		01/11/2012 * @author	Joseph Kwik * <br>Creation Date:		08/23/2001 11:31:32 *//* &MiscellaneousRegConstant& */public class MiscellaneousRegConstant{/* &MiscellaneousRegConstant'NO_DATA_TO_BUSINESS& */	public final static int NO_DATA_TO_BUSINESS =		GeneralConstant.NO_DATA_TO_BUSINESS;/* &MiscellaneousRegConstant'INIT_TIMED_PERMIT& */	public final static int INIT_TIMED_PERMIT = 1;/* &MiscellaneousRegConstant'INIT_DISABLED_PARKING_CARD& */	public final static int INIT_DISABLED_PARKING_CARD = 2;/* &MiscellaneousRegConstant'INIT_TEMP_ADDL_WEIGHT& */	public final static int INIT_TEMP_ADDL_WEIGHT = 3;/* &MiscellaneousRegConstant'MINIMUM_YEAR& */	public final static int MINIMUM_YEAR = 1900;/* &MiscellaneousRegConstant'MAXIMUM_YEAR& */	public final static int MAXIMUM_YEAR = 2099;/* &MiscellaneousRegConstant'INCORRECT_MODEL_YEAR_ENTRY& */	public final static String INCORRECT_MODEL_YEAR_ENTRY =		"Model Year is not valid.";/* &MiscellaneousRegConstant'INVALID_EXP_MONTH_ENTRY& */	public final static String INVALID_EXP_MONTH_ENTRY =		"Expiration month must be between 1 and 12";/* &MiscellaneousRegConstant'INVALID_EXP_YEAR_ENTRY& */	public final static String INVALID_EXP_YEAR_ENTRY =		"Expiration year is not valid";/* &MiscellaneousRegConstant'ERROR_TITLE& */	public final static String ERROR_TITLE = "ERROR!";	// defect 9831 /* &MiscellaneousRegConstant'CUST_ID& */	public final static int CUST_ID = 1;/* &MiscellaneousRegConstant'PLACARD_NUMBER& */	public final static int PLACARD_NUMBER = 2;/* &MiscellaneousRegConstant'DISABLED_NAME& */	public final static int DISABLED_NAME = 3;/* &MiscellaneousRegConstant'INSTITUTION_NAME& */	public final static int INSTITUTION_NAME = 4;/* &MiscellaneousRegConstant'SEARCH& */	public final static int SEARCH = 20;/* &MiscellaneousRegConstant'INSERT& */	public final static int INSERT = 21;/* &MiscellaneousRegConstant'UPDATE& */	public final static int UPDATE = 22;/* &MiscellaneousRegConstant'GENERATE_DSABLD_PLCRD_REPORT& */	public final static int GENERATE_DSABLD_PLCRD_REPORT = 23;/* &MiscellaneousRegConstant'GENERATE_DSABLD_PLCRD_EXPORT& */	public final static int GENERATE_DSABLD_PLCRD_EXPORT = 24;/* &MiscellaneousRegConstant'UPDATE_MVDI_EXP_HSTRY& */	public final static int UPDATE_MVDI_EXP_HSTRY = 25;/* &MiscellaneousRegConstant'CHKIFVOIDABLE& */	public final static int CHKIFVOIDABLE = 26;/* &MiscellaneousRegConstant'RESETINPROCS& */	public final static int RESETINPROCS = 27;/* &MiscellaneousRegConstant'SETINPROCS& */	public final static int SETINPROCS = 28;/* &MiscellaneousRegConstant'REINSTATE& */	public final static int REINSTATE = 29; /* &MiscellaneousRegConstant'DP_ADD_TRANS_TYPE_CD& */	public final static int DP_ADD_TRANS_TYPE_CD = 0;/* &MiscellaneousRegConstant'DP_DEL_TRANS_TYPE_CD& */	public final static int DP_DEL_TRANS_TYPE_CD = 1;/* &MiscellaneousRegConstant'DP_UNDEL_TRANS_TYPE_CD& */	public final static int DP_UNDEL_TRANS_TYPE_CD = 2;	/* &MiscellaneousRegConstant'PLACARD_NO& */	public final static String PLACARD_NO = "Placard No:";/* &MiscellaneousRegConstant'PLACARD_DESC& */	public final static String PLACARD_DESC = "Placard Desc:";/* &MiscellaneousRegConstant'ISSUE_DATE& */	public final static String ISSUE_DATE = "Issue Date:";/* &MiscellaneousRegConstant'EXPIRATION_DATE& */	public final static String EXPIRATION_DATE = "Expiration Date:";/* &MiscellaneousRegConstant'ISSUE_TWO_PLACARDS& */	public final static String ISSUE_TWO_PLACARDS =		"Issue Two Placards";/* &MiscellaneousRegConstant'DELETE_REASON& */	public final static String DELETE_REASON = "Delete Reason:";/* &MiscellaneousRegConstant'REPLACE_REASON& */	public final static String REPLACE_REASON = "Replace Reason:";/* &MiscellaneousRegConstant'CHARGE_FEE& */	public final static String CHARGE_FEE = "Charge Fee";/* &MiscellaneousRegConstant'PERMANENT_MNEMONIC& */	public final static String PERMANENT_MNEMONIC = "P";/* &MiscellaneousRegConstant'TEMPORARY_MNEMONIC& */	public final static String TEMPORARY_MNEMONIC = "T";/* &MiscellaneousRegConstant'INQ& */	public final static String INQ = "INQ";	// end defect 9831 	// defect 10133	// public final static String REPLACE_TRANSCD_PREFIX = "RP";	// public final static String DELETE_TRANSCD_PREFIX = "DL";	// public final static String MOBILITY_MNEMONIC = "M";	// public final static String NONMOBILITY_MNEMONIC = "N";	// public final static String MOBLTY_PLCRD_ITMCD = "PDC";	// public final static String NONMOBLTY_PLCRD_ITMCD = "TDC";/* &MiscellaneousRegConstant'REPLACE_TRANSCD_PREFIX& */	public final static String REPLACE_TRANSCD_PREFIX = "RPL";/* &MiscellaneousRegConstant'DELETE_TRANSCD_PREFIX& */	public final static String DELETE_TRANSCD_PREFIX = "DEL";/* &MiscellaneousRegConstant'REINSTATE_TRANS_CD_PREFIX& */	public final static String REINSTATE_TRANS_CD_PREFIX = "REI";/* &MiscellaneousRegConstant'RENEW_TRANS_CD_PREFIX& */	public final static String RENEW_TRANS_CD_PREFIX = "REN";/* &MiscellaneousRegConstant'PERMANENT_PLCRD_ITMCD& */	public final static String PERMANENT_PLCRD_ITMCD = "PDC";/* &MiscellaneousRegConstant'TEMPORARY_PLCRD_ITMCD& */	public final static String TEMPORARY_PLCRD_ITMCD = "TDC";/* &MiscellaneousRegConstant'MRG021_COL_CUSTID& */	public final static int MRG021_COL_CUSTID = 0;/* &MiscellaneousRegConstant'MRG021_COL_NAME& */	public final static int MRG021_COL_NAME = 1;/* &MiscellaneousRegConstant'MRG021_COL_ADDRESS& */	public final static int MRG021_COL_ADDRESS = 2;/* &MiscellaneousRegConstant'MRG021_COL_TYPE& */	public final static int MRG021_COL_TYPE = 3;/* &MiscellaneousRegConstant'MRG021_COL_PLACARDS& */	public final static int MRG021_COL_PLACARDS = 4;/* &MiscellaneousRegConstant'MRG023_COL_PLACARD& */	public final static int MRG023_COL_PLACARD = 0;/* &MiscellaneousRegConstant'MRG023_COL_DESCRIPTION& */	public final static int MRG023_COL_DESCRIPTION = 1;/* &MiscellaneousRegConstant'MRG023_COL_ISSUE_DATE& */	public final static int MRG023_COL_ISSUE_DATE = 2;/* &MiscellaneousRegConstant'MRG023_COL_EXP_DATE& */	public final static int MRG023_COL_EXP_DATE = 3;/* &MiscellaneousRegConstant'MRG023_COL_TYPE& */	public final static int MRG023_COL_TYPE = 4;	// end defect 10133		// defect 11214/* &MiscellaneousRegConstant'MRG023_COL_DELETE_REASON& */	public final static int MRG023_COL_DELETE_REASON = 5;	// end defect 11214 	// defect 10491 /* &MiscellaneousRegConstant'PRMINQ& */	public final static String PRMINQ = "PRMINQ";/* &MiscellaneousRegConstant'PRMTINQ& */	public final static int PRMTINQ = 29;/* &MiscellaneousRegConstant'ITMCD_30PT& */	public final static String ITMCD_30PT = "30PT";/* &MiscellaneousRegConstant'ITMCD_30MCPT& */	public final static String ITMCD_30MCPT = "30MCPT";/* &MiscellaneousRegConstant'ITMCD_OTPT& */	public final static String ITMCD_OTPT = "OTPT";/* &MiscellaneousRegConstant'ITMCD_OTMCPT& */	public final static String ITMCD_OTMCPT = "OTMCPT";/* &MiscellaneousRegConstant'MIN_30_DAY_PERMITS_FOR_MSG& */	public final static int MIN_30_DAY_PERMITS_FOR_MSG = 3;	// end defect 10491 	// defect 10607 /* &MiscellaneousRegConstant'MAX_DP_ROWS_FOR_CICS& */	public final static int MAX_DP_ROWS_FOR_CICS = 5;	// end defect 10607	// defect 10726/* &MiscellaneousRegConstant'ITMCD_FDPT& */	public final static String ITMCD_FDPT = "FDPT";/* &MiscellaneousRegConstant'ITMCD_72PT& */	public final static String ITMCD_72PT = "72PT";/* &MiscellaneousRegConstant'ITMCD_144PT& */	public final static String ITMCD_144PT = "144PT";/* &MiscellaneousRegConstant'TIMED_PERMIT_VENDOR_BSN_TYPE_CD& */	public final static String TIMED_PERMIT_VENDOR_BSN_TYPE_CD =		"BULKPERMIT";	// end defect 10726	// defect 10831 /* &MiscellaneousRegConstant'DSABLD_PLCRD_HASACTIVE& */	public final static String DSABLD_PLCRD_HASACTIVE = "DPHASACTIVE";/* &MiscellaneousRegConstant'DSABLD_PLCRD_MAXACTIVE_EXPDATE& */	public final static String DSABLD_PLCRD_MAXACTIVE_EXPDATE =		"DPMAXACTIVEEXPYRMO";	// end defect 10831	 	// defect 10844 /* &MiscellaneousRegConstant'PERMIT_REGULAR_VEHTYPECD& */	public final static String PERMIT_REGULAR_VEHTYPECD = "R";/* &MiscellaneousRegConstant'PERMIT_MOTORCYCLE_VEHTYPECD& */	public final static String PERMIT_MOTORCYCLE_VEHTYPECD = "M";	// end defect 10844 }/* #MiscellaneousRegConstant# */