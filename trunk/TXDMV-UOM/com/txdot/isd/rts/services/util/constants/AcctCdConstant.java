package com.txdot.isd.rts.services.util.constants;

/*
 *
 * AcctCdConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	12/29/2004	Added REGISEMI constant. Edited comments.
 *							defect 6208 Ver 5.2.2
 * K Harrell	07/09/2008	add TTL_TRNSFR_PNLTY_2008,
 * 							 TTL_TRNSFR_PNLTY_2008_START_DATE 
 * 							defect 9742 Ver MyPlates_POS
 * K Harrell	10/27/2008	add BPM, BTM, RPNM, RTNM 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/01/2008	add TEMP_ADDL_WT_1_MO
 * 							defect 8586 Ver Defect_POS_B
 * B Hargrove	07/13/2009	add MISC_FEE_CODE, MISC_FEE_CODE_REGION, 
 * 							VET_FUND_CODE, VET_FUND_CODE_REGION
 * 							defect 10122 Ver Defect_POS_F
 * K Harrell	07/25/2009	add PDC, TDC
 * 							delete BTM, RTNM
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	05/24/2010	add PRMDUP 
 * 							defect 10491 Ver 6.5.0 
 * B Hargrove	08/04/2011	add PARKS_FUND_CODE, PARKS_FUND_CODE_REGION
 * 							defect 10965 Ver 6.8.1
 * K McKee      01/26/2012  add MAIL_CODE
 * 							defect 11240 Ver 6.10.0
 * B Hargrove	01/31/2012	Reg Collection IRP codes
 * 							add REGCOL_IRPCAB, REGCOL_IRPOS, REGCOL_IRPTX
 * 							defect 11218 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Account Item Codes
 *
 * @version	6.10.0			01/31/2012
 * @author	Sunil Govindappa
 * <br>Creation Date:		09/12/2001 11:36:51
 */

public class AcctCdConstant
{
	/* Account Codes Constants */
	public final static String REG_TRNSFR = "TRANS";
	public final static String REGISEMI = "REGISEMI";
	public final static String SALES_TAX = "SLSTX";
	public final static String SALES_TAX_PNLTY = "SLSTXPEN";
	public final static String TITLE = "TITLE";
	public final static String TTL_TRNSFR_PNLTY = "DELPEN";
	// defect 9742 
	public final static String TTL_TRNSFR_PNLTY_2008 = "DELPEN1";
	public final static int TTL_TRNSFR_PNLTY_2008_START_DATE = 20080101;
	// end defect 9742 

	// defect 8586 
	public final static String TEMP_ADDL_WT_1_MO = "TAWPTMO";
	// end defect 8586 

	// defect 10122
	public final static String MISC_FEE_CODE = "MSC";
	public final static String MISC_FEE_CODE_REGION = "MISC-R";
	public final static String VET_FUND_CODE = "VET-FUND";
	public final static String VET_FUND_CODE_REGION = "VET-FD-R";
	// end defect 10122
	// defect 10965
	public final static String PARKS_FUND_CODE = "STPARK";
	public final static String PARKS_FUND_CODE_REGION = "STPARK-R";
	// end defect 10965
    // defect 11240
	public final static String MAIL_CODE = "MAIL";
	// end defect 11240
	// defect 10133
	// public final static String BTM = "BTM";
	// public final static String RTNM = "RTNM"; 
	public final static String PDC = "PDC";
	public final static String TDC = "TDC";
	// end defect 10133

	// defect 9831 
	// Continue to use to analyze placard as Permanent vs. Temporary
	// although no longer issued.   (HB3095 - defect 10133)  
	public final static String BPM = "BPM";
	public final static String RPNM = "RPNM";
	// end defect 9831
	
	// defect 10491 
	// Duplicate Permit Receipt 
	public final static String PRMDUP = "PRMDUP"; 
	// end defect 10491  

	// defect 11218 
	// Regional Collections IRP 
	public final static String REGCOL_IRPCAB = "CABCRD-R";
	public final static String REGCOL_IRPOS = "IRPFEE-R";
	public final static String REGCOL_IRPTX = "IRPTX-R";
	// end defect 11218
}
