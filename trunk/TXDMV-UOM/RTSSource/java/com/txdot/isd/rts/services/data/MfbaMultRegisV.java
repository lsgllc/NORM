package com.txdot.isd.rts.services.data;import java.util.Vector;import com.txdot.isd.rts.server.dataaccess.MfAccess;import com.txdot.isd.rts.services.util.Dollar;import com.txdot.isd.rts.services.util.constants.CommonConstant;/* * * MfbaMultRegisV.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * J Rue		10/17/2006	Add offsets and code * 							defect 6701 Ver Exempts * J Rue		10/18/2006	Update offsets to CICS T version * 							Add HOOPS, ORGNO and PLTBIRTHDATE * 							defect 6701 Ver Exempts * J Rue		10/20/2006	Move process from MfAccess * 							defect 6701 Ver Exempts * J Rue		10/20/2006	Move constants from method to class level * 							modify setMultipleRegistrationDataFromMf() * 							defect 6701 Ver Exempts * J Rue		11/07/2006	Adjust REG_REF_AMT_LENGTH * 							Reset RECORD_LENGTH = 332 * 							change liREGIS_RECORD_LENGTH to RECORD_LENGTH * 							modify setMultipleRegistrationDataFromMf() * 							defect 6701 Ver Exempts * J Rue		02/01/2007	Add offsets and code * 							defect 9086 Ver Special Plates * J Rue		02/02/2007	Update JavaDoc * 							defect 9086 Ver Special Plates * J Rue		06/06/2007	Capture SpclRegId * 							modify setMultipleRegistrationDataFromMf() * 							defect 9086 Ver Special Plates * J Rue		04/07/2008	Copy version U to version V for * 							MF version V * 							modify all * 							defect 9557 Ver Defect_POS_A * J Rue		04/08/2008	CustBaseRegFee and DieselFee not longer  * 							reference * 							modify setMultipleRegistrationDataFromMf() * 							defect 9581 Ver 3_AMIGOS_PH_B * J Rue		04/29/2008	Adjust offsets * 							deefct 9557 Ver Defect_POS_A * J Rue		06/04/2008	Set Ver Defect_POS_A * 							defect 9557 Ver Defect_POS_A * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode * 							modify all class variables, * 								setMultipleRegistrationDataFromMf() * 							defect 9833 Ver ELT_MfAccess * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U * 							modify all class variables * 							defect 9655 Ver Defect_POS_D * J Rue		01/09/2009	Remove OrgNo, RegPltAge, DISSOCIATECD. * 							 Adjust offsets * 							modify setMultipleRegistrationDataFromMf() * 							defect 9655 Ver Defect_POS_D * J Rue		02/25/2009	Arrange class variables from lowest offset * 							to highest. Include PLTRMVCD * 							add PLT_RMV_CD_OFFSET, PLT_RMV_CD_LENGTH * 							modify setMultipleRegistrationDataFromMf() * 							defect 9961 Ver Defect_POS_E * J Rue		10/06/2009	Adjust offsets for RCCPI version T * 							Copy setMultipleRegistrationDataFromMf() from * 							MfbaMultRegisV to MfbaMultRegisT. No change * 							required from V version * 							modify all class variables, * 								setMultipleRegistrationDataFromMf() * 							defect 10244 Ver Defect_POS_G * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc. * 							defect 10244 Ver Defect_POS_G * M Reyes		03/03/2010	Copy version T to version U for MF * 							MF version U. Modified All. * 							defect 10378 Ver POS_640 * M Reyes		04/06/2010	Modified RECORD_LENGTH. Added * 							ST_OFCL_REELECTION_INDI_OFFSET, * 							ST_OFCL_REELECTION_INDI_LENGTH, * 							RECPNTEMAIL_OFFSET, RECPNTEMAIL_LENGTH * 							VTRREGEMRGCD1_OFFSET, VTRREGEMRGCD1_LENGTH * 							VTRREGEMRGCD2_OFFSET, VTRREGEMRGCD2_LENGTH * 							defect 10378 Ver POS_640 * K Harrell	06/15/2010	RegistrationData.setSORelectionIndi()  * 							refactored to setEMailRenwlReqCd() * 							add EMAIL_RENWL_REQ_CD_OFFSET, * 							 EMAIL_RENWL_REQ_CD_LENGTH * 							delete ST_OFCL_REELECTION_INDI_OFFSET, * 							ST_OFCL_REELECTION_INDI_LENGTH * 							modify setMultipleRegistrationDataFromMf() * 							defect 10508 Ver 6.5.0    * K Harrell 	06/22/2010  Copy version U to version V for MF *        					MF version V. Modified All. *        					defect 10492 Ver 6.5.0 * M Reyes		10/04/2010	Copy version V to version T for MF * 							MF version T. Modified All. * 							defect 10595 Ver 6.6.0 * M Reyes		12/31/2010	Copy version T to version U for MF * 							version U. Modified All. * 							defect 10710 Ver 6.7.0 * K Harrell	11/01/2011	Copy version U to V for MF Version V  * 							defect 11045 Ver 6.9.0   * --------------------------------------------------------------------- *//** * Parse CICS transactions into MultRegisFromMF Data.  * Used by the server side business layer.  * * @version	6.9.0  			11/01/2011 * @author	Jeff Rue * <br>Creation Date:		10/17/2006 16:40:32 *//* &MfbaMultRegisV& */public class MfbaMultRegisV{/* &MfbaMultRegisV'NUMBER_OF_RECORDS_OFFSET& */	private final static int NUMBER_OF_RECORDS_OFFSET = 145;/* &MfbaMultRegisV'NUMBER_OF_RECORDS_LENGTH& */	private final static int NUMBER_OF_RECORDS_LENGTH = 3;	// defect 10378/* &MfbaMultRegisV'RECORD_LENGTH& */	private final static int RECORD_LENGTH = 380;	// end defect 10378	//remove the header from the response/* &MfbaMultRegisV'liHEADER_LENGTH& */	private final static int liHEADER_LENGTH = 256;	//define all offsets and lengths	// Regis Rec/* &MfbaMultRegisV'REG_PLT_NO_OFFSET& */	private final static int REG_PLT_NO_OFFSET = 0;/* &MfbaMultRegisV'REG_PLT_NO_LENGTH& */	private final static int REG_PLT_NO_LENGTH = 7;/* &MfbaMultRegisV'REG_EXP_YR_OFFSET& */	private final static int REG_EXP_YR_OFFSET = 7;/* &MfbaMultRegisV'REG_EXP_YR_LENGTH& */	private final static int REG_EXP_YR_LENGTH = 4;/* &MfbaMultRegisV'REG_STKR_NO_OFFSET& */	private final static int REG_STKR_NO_OFFSET = 28;/* &MfbaMultRegisV'REG_STKR_NO_LENGTH& */	private final static int REG_STKR_NO_LENGTH = 10;/* &MfbaMultRegisV'REG_STKR_CD_OFFSET& */	private final static int REG_STKR_CD_OFFSET = 38;/* &MfbaMultRegisV'REG_STKR_CD_LENGTH& */	private final static int REG_STKR_CD_LENGTH = 8;/* &MfbaMultRegisV'RES_COMPT_CNTY_NO_OFFSET& */	private final static int RES_COMPT_CNTY_NO_OFFSET = 52;/* &MfbaMultRegisV'RES_COMPT_CNTY_NO_LENGTH& */	private final static int RES_COMPT_CNTY_NO_LENGTH = 3;/* &MfbaMultRegisV'REG_CLASS_CD_OFFSET& */	private final static int REG_CLASS_CD_OFFSET = 55;/* &MfbaMultRegisV'REG_CLASS_CD_LENGTH& */	private final static int REG_CLASS_CD_LENGTH = 3;/* &MfbaMultRegisV'REG_EFF_DATE_OFFSET& */	private final static int REG_EFF_DATE_OFFSET = 58;/* &MfbaMultRegisV'REG_EFF_DATE_LENGTH& */	private final static int REG_EFF_DATE_LENGTH = 8;/* &MfbaMultRegisV'REG_EXP_MO_OFFSET& */	private final static int REG_EXP_MO_OFFSET = 66;/* &MfbaMultRegisV'REG_EXP_MO_LENGTH& */	private final static int REG_EXP_MO_LENGTH = 2;/* &MfbaMultRegisV'REG_ISSUE_DATE_OFFSET& */	private final static int REG_ISSUE_DATE_OFFSET = 68;/* &MfbaMultRegisV'REG_ISSUE_DATE_LENGTH& */	private final static int REG_ISSUE_DATE_LENGTH = 8;/* &MfbaMultRegisV'REG_PLT_CD_OFFSET& */	private final static int REG_PLT_CD_OFFSET = 76;/* &MfbaMultRegisV'REG_PLT_CD_LENGTH& */	private final static int REG_PLT_CD_LENGTH = 8;	// CUST FEES/* &MfbaMultRegisV'CUST_ACTUAL_REG_FEE_OFFSET& */	private final static int CUST_ACTUAL_REG_FEE_OFFSET = 84;/* &MfbaMultRegisV'CUST_ACTUAL_REG_FEE_LENGTH& */	private final static int CUST_ACTUAL_REG_FEE_LENGTH = 6;/* &MfbaMultRegisV'CUST_ACTUAL_REG_FEE_DECIMAL& */	private final static int CUST_ACTUAL_REG_FEE_DECIMAL = 2;	//RENWLDATA/* &MfbaMultRegisV'RECPNT_NAME_OFFSET& */	private final static int RECPNT_NAME_OFFSET = 90;/* &MfbaMultRegisV'RECPNT_NAME_LENGTH& */	private final static int RECPNT_NAME_LENGTH = 30;/* &MfbaMultRegisV'RENWL_MAILNG_ST1_OFFSET& */	private final static int RENWL_MAILNG_ST1_OFFSET = 120;/* &MfbaMultRegisV'RENWL_MAILNG_ST1_LENGTH& */	private final static int RENWL_MAILNG_ST1_LENGTH = 30;/* &MfbaMultRegisV'RENWL_MAILNG_ST2_OFFSET& */	private final static int RENWL_MAILNG_ST2_OFFSET = 150;/* &MfbaMultRegisV'RENWL_MAILNG_ST2_LENGTH& */	private final static int RENWL_MAILNG_ST2_LENGTH = 30;/* &MfbaMultRegisV'RENWL_MAILNG_CITY_OFFSET& */	private final static int RENWL_MAILNG_CITY_OFFSET = 180;/* &MfbaMultRegisV'RENWL_MAILNG_CITY_LENGTH& */	private final static int RENWL_MAILNG_CITY_LENGTH = 19;/* &MfbaMultRegisV'RENWL_MAILNG_STATE_OFFSET& */	private final static int RENWL_MAILNG_STATE_OFFSET = 199;/* &MfbaMultRegisV'RENWL_MAILNG_STATE_LENGTH& */	private final static int RENWL_MAILNG_STATE_LENGTH = 2;/* &MfbaMultRegisV'RENWL_MAILNG_ZPCD_OFFSET& */	private final static int RENWL_MAILNG_ZPCD_OFFSET = 201;/* &MfbaMultRegisV'RENWL_MAILNG_ZPCD_LENGTH& */	private final static int RENWL_MAILNG_ZPCD_LENGTH = 5;/* &MfbaMultRegisV'RENWL_MAILNG_ZPCDP4_OFFSET& */	private final static int RENWL_MAILNG_ZPCDP4_OFFSET = 206;/* &MfbaMultRegisV'RENWL_MAILNG_ZPCDP4_LENGTH& */	private final static int RENWL_MAILNG_ZPCDP4_LENGTH = 4;/* &MfbaMultRegisV'RENWL_MAIL_RTRN_INDI_OFFSET& */	private final static int RENWL_MAIL_RTRN_INDI_OFFSET = 210;/* &MfbaMultRegisV'RENWL_MAIL_RTRN_INDI_LENGTH& */	private final static int RENWL_MAIL_RTRN_INDI_LENGTH = 1;	//INDICATORS/* &MfbaMultRegisV'DPS_SAFETY_SUSPN_INDI_OFFSET& */	private final static int DPS_SAFETY_SUSPN_INDI_OFFSET = 211;/* &MfbaMultRegisV'DPS_SAFETY_SUSPN_INDI_LENGTH& */	private final static int DPS_SAFETY_SUSPN_INDI_LENGTH = 1;/* &MfbaMultRegisV'HVY_VEH_USE_TAX_INDI_OFFSET& */	private final static int HVY_VEH_USE_TAX_INDI_OFFSET = 212;/* &MfbaMultRegisV'HVY_VEH_USE_TAX_INDI_LENGTH& */	private final static int HVY_VEH_USE_TAX_INDI_LENGTH = 1;/* &MfbaMultRegisV'REG_INVLD_INDI_OFFSET& */	private final static int REG_INVLD_INDI_OFFSET = 213;/* &MfbaMultRegisV'REG_INVLD_INDI_LENGTH& */	private final static int REG_INVLD_INDI_LENGTH = 1;/* &MfbaMultRegisV'REG_HOT_CK_INDI_OFFSET& */	private final static int REG_HOT_CK_INDI_OFFSET = 215;/* &MfbaMultRegisV'REG_HOT_CK_INDI_LENGTH& */	private final static int REG_HOT_CK_INDI_LENGTH = 1;/* &MfbaMultRegisV'PLTS_SEIZD_INDI_OFFSET& */	private final static int PLTS_SEIZD_INDI_OFFSET = 216;/* &MfbaMultRegisV'PLTS_SEIZD_INDI_LENGTH& */	private final static int PLTS_SEIZD_INDI_LENGTH = 1;/* &MfbaMultRegisV'STKR_SEIZD_INDI_OFFSET& */	private final static int STKR_SEIZD_INDI_OFFSET = 217;/* &MfbaMultRegisV'STKR_SEIZD_INDI_LENGTH& */	private final static int STKR_SEIZD_INDI_LENGTH = 1;	// defect 10508 	// defect 10378	// New attribute for version U	//private final static int ST_OFCL_REELECTION_INDI_OFFSET = 218;	//private final static int ST_OFCL_REELECTION_INDI_LENGTH = 1;/* &MfbaMultRegisV'EMAIL_RENWL_REQ_CD_OFFSET& */	private final static int EMAIL_RENWL_REQ_CD_OFFSET = 218;/* &MfbaMultRegisV'EMAIL_RENWL_REQ_CD_LENGTH& */	private final static int EMAIL_RENWL_REQ_CD_LENGTH = 1;	// end defect 10378	// end defect 10508 /* &MfbaMultRegisV'REG_REF_AMT_OFFSET& */	private final static int REG_REF_AMT_OFFSET = 219;/* &MfbaMultRegisV'REG_REF_AMT_LENGTH& */	private final static int REG_REF_AMT_LENGTH = 6;/* &MfbaMultRegisV'REG_REF_AMT_DECIMAL& */	private final static int REG_REF_AMT_DECIMAL = 2;/* &MfbaMultRegisV'NOTFYING_CITY_OFFSET& */	private final static int NOTFYING_CITY_OFFSET = 225;/* &MfbaMultRegisV'NOTFYING_CITY_LENGTH& */	private final static int NOTFYING_CITY_LENGTH = 19;	//PREV REGIS/* &MfbaMultRegisV'PREV_PLT_NO_OFFSET& */	private final static int PREV_PLT_NO_OFFSET = 244;/* &MfbaMultRegisV'PREV_PLT_NO_LENGTH& */	private final static int PREV_PLT_NO_LENGTH = 7;/* &MfbaMultRegisV'PREV_EXP_MO_OFFSET& */	private final static int PREV_EXP_MO_OFFSET = 251;/* &MfbaMultRegisV'PREV_EXP_MO_LENGTH& */	private final static int PREV_EXP_MO_LENGTH = 2;/* &MfbaMultRegisV'PREV_EXP_YR_OFFSET& */	private final static int PREV_EXP_YR_OFFSET = 253;/* &MfbaMultRegisV'PREV_EXP_YR_LENGTH& */	private final static int PREV_EXP_YR_LENGTH = 4;/* &MfbaMultRegisV'VEH_GROSS_WT_OFFSET& */	private final static int VEH_GROSS_WT_OFFSET = 257;/* &MfbaMultRegisV'VEH_GROSS_WT_LENGTH& */	private final static int VEH_GROSS_WT_LENGTH = 6;/* &MfbaMultRegisV'TIRE_TYPE_CD_OFFSET& */	private final static int TIRE_TYPE_CD_OFFSET = 263;/* &MfbaMultRegisV'TIRE_TYPE_CD_LENGTH& */	private final static int TIRE_TYPE_CD_LENGTH = 1;/* &MfbaMultRegisV'REG_PLT_OWNR_NAME_OFFSET& */	private final static int REG_PLT_OWNR_NAME_OFFSET = 264;/* &MfbaMultRegisV'REG_PLT_OWNR_NAME_LENGTH& */	private final static int REG_PLT_OWNR_NAME_LENGTH = 30;	// Add SpclRegId/* &MfbaMultRegisV'SPCL_REG_ID_OFFSET& */	private final static int SPCL_REG_ID_OFFSET = 294;/* &MfbaMultRegisV'SPCL_REG_ID_LENGTH& */	private final static int SPCL_REG_ID_LENGTH = 9;	// Add HOOPS, ORGNO and PLTBIRTHDATE/* &MfbaMultRegisV'HOOPS_REG_PLT_NO_OFFSET& */	private final static int HOOPS_REG_PLT_NO_OFFSET = 303;/* &MfbaMultRegisV'HOOPS_REG_PLT_NO_LENGTH& */	private final static int HOOPS_REG_PLT_NO_LENGTH = 7;/* &MfbaMultRegisV'PLT_BIRTH_DATE_OFFSET& */	private final static int PLT_BIRTH_DATE_OFFSET = 310;/* &MfbaMultRegisV'PLT_BIRTH_DATE_LENGTH& */	private final static int PLT_BIRTH_DATE_LENGTH = 6;	// defect 9961	//	Add PLTRMVCD/* &MfbaMultRegisV'PLT_RMV_CD_OFFSET& */	private final static int PLT_RMV_CD_OFFSET = 316;/* &MfbaMultRegisV'PLT_RMV_CD_LENGTH& */	private final static int PLT_RMV_CD_LENGTH = 1;	// end defect 9961	// defect 9557	//	Add DISSOCIATECD, V21PLTID, PRISMLVLCD/* &MfbaMultRegisV'V21PLTID_OFFSET& */	private final static int V21PLTID_OFFSET = 317;/* &MfbaMultRegisV'V21PLTCD_LENGTH& */	private final static int V21PLTCD_LENGTH = 10;/* &MfbaMultRegisV'PRISM_LVL_CD_OFFSET& */	private final static int PRISM_LVL_CD_OFFSET = 327;/* &MfbaMultRegisV'PRISM_LVL_CD_LENGTH& */	private final static int PRISM_LVL_CD_LENGTH = 1;	// end defect 9557	// end defect 10378	// defect 10378	// New Attributes for version U/* &MfbaMultRegisV'RECPNTEMAIL_OFFSET& */	private final static int RECPNTEMAIL_OFFSET = 328;/* &MfbaMultRegisV'RECPNTEMAIL_LENGTH& */	private final static int RECPNTEMAIL_LENGTH = 50;/* &MfbaMultRegisV'VTRREGEMRGCD1_OFFSET& */	private final static int VTRREGEMRGCD1_OFFSET = 378;/* &MfbaMultRegisV'VTRREGEMRGCD1_LENGTH& */	private final static int VTRREGEMRGCD1_LENGTH = 1;/* &MfbaMultRegisV'VTRREGEMRGCD2_OFFSET& */	private final static int VTRREGEMRGCD2_OFFSET = 379;/* &MfbaMultRegisV'VTRREGEMRGCD2_LENGTH& */	private final static int VTRREGEMRGCD2_LENGTH = 1;	// end defect 10378	/**	 * 	 *//* &MfbaMultRegisV.MfbaMultRegisV& */	public MfbaMultRegisV()	{		super();	}/* &MfbaMultRegisV.main& */	public static void main(String[] args)	{	}	/**	 * Sets and returns multiple registration data from the mainframe	 * response. 	 * 	 * @param asMfRegisResponse String	 * @return Vector	 *//* &MfbaMultRegisV.setMultipleRegistrationDataFromMf& */	public Vector setMultipleRegistrationDataFromMf(String asMfRegisResponse)	{		Vector lvRegistrationDataContainer = new Vector();		MfAccess laMFAccess = new MfAccess();		if (!(asMfRegisResponse.equals(CommonConstant.STR_SPACE_EMPTY))			&& (asMfRegisResponse != null))		{			//get the number of records value from mainframe header			int NUMBER_OF_ELEMENTS =				Integer					.valueOf(						laMFAccess.getStringFromZonedDecimal(							asMfRegisResponse.substring(								NUMBER_OF_RECORDS_OFFSET,								NUMBER_OF_RECORDS_OFFSET									+ NUMBER_OF_RECORDS_LENGTH)))					.intValue();			asMfRegisResponse =				asMfRegisResponse.substring(liHEADER_LENGTH);			// the offset based on the record that is picked from the			//  mf response			int liRecordOffset = 0;			for (int i = 0; i < NUMBER_OF_ELEMENTS; i++)			{				//set the record offset				liRecordOffset = i * RECORD_LENGTH;				//create registration data				RegistrationData laRegistrationData =					new RegistrationData();				//set all values from REGIS				//				// defetc 9086				//	Add SpclRegId				laRegistrationData.setSpclRegId(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + SPCL_REG_ID_OFFSET,									liRecordOffset										+ SPCL_REG_ID_OFFSET										+ SPCL_REG_ID_LENGTH)))						.intValue());				// end defect 9086				// defect 6701				//	Add ORGNO, HOOPS and PLTBIRTHDATE				laRegistrationData.setHoopsRegPltNo(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + HOOPS_REG_PLT_NO_OFFSET,							liRecordOffset								+ HOOPS_REG_PLT_NO_OFFSET								+ HOOPS_REG_PLT_NO_LENGTH)));				// defect 9655				//	Remove reference				//				laRegistrationData.setOrgNo(				//					laMFAccess.trimMfString(				//						asMfRegisResponse.substring(				//							liRecordOffset + ORGNO_OFFSET,				//							liRecordOffset				//								+ ORGNO_OFFSET				//								+ ORGNO_LENGTH)));				// end defect 9655				laRegistrationData.setPltBirthDate(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ PLT_BIRTH_DATE_OFFSET,									liRecordOffset										+ PLT_BIRTH_DATE_OFFSET										+ PLT_BIRTH_DATE_LENGTH)))						.intValue());				// end defect 6701				laRegistrationData.setDpsSaftySuspnIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ DPS_SAFETY_SUSPN_INDI_OFFSET,									liRecordOffset										+ DPS_SAFETY_SUSPN_INDI_OFFSET										+ DPS_SAFETY_SUSPN_INDI_LENGTH)))						.intValue());				laRegistrationData.setHvyVehUseTaxIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ HVY_VEH_USE_TAX_INDI_OFFSET,									liRecordOffset										+ HVY_VEH_USE_TAX_INDI_OFFSET										+ HVY_VEH_USE_TAX_INDI_LENGTH)))						.intValue());				laRegistrationData.setNotfyngCity(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + NOTFYING_CITY_OFFSET,							liRecordOffset								+ NOTFYING_CITY_OFFSET								+ NOTFYING_CITY_LENGTH)));				laRegistrationData.setPrevExpMo(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + PREV_EXP_MO_OFFSET,									liRecordOffset										+ PREV_EXP_MO_OFFSET										+ PREV_EXP_MO_LENGTH)))						.intValue());				laRegistrationData.setPrevExpYr(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + PREV_EXP_YR_OFFSET,									liRecordOffset										+ PREV_EXP_YR_OFFSET										+ PREV_EXP_YR_LENGTH)))						.intValue());				laRegistrationData.setPrevPltNo(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + PREV_PLT_NO_OFFSET,							liRecordOffset								+ PREV_PLT_NO_OFFSET								+ PREV_PLT_NO_LENGTH)));				laRegistrationData.setRecpntName(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RECPNT_NAME_OFFSET,							liRecordOffset								+ RECPNT_NAME_OFFSET								+ RECPNT_NAME_LENGTH)));				laRegistrationData.setRegClassCd(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ REG_CLASS_CD_OFFSET,									liRecordOffset										+ REG_CLASS_CD_OFFSET										+ REG_CLASS_CD_LENGTH)))						.intValue());				laRegistrationData.setRegEffDt(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ REG_EFF_DATE_OFFSET,									liRecordOffset										+ REG_EFF_DATE_OFFSET										+ REG_EFF_DATE_LENGTH)))						.intValue());				laRegistrationData.setRegExpMo(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + REG_EXP_MO_OFFSET,									liRecordOffset										+ REG_EXP_MO_OFFSET										+ REG_EXP_MO_LENGTH)))						.intValue());				laRegistrationData.setRegExpYr(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + REG_EXP_YR_OFFSET,									liRecordOffset										+ REG_EXP_YR_OFFSET										+ REG_EXP_YR_LENGTH)))						.intValue());				laRegistrationData.setRegIssueDt(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ REG_ISSUE_DATE_OFFSET,									liRecordOffset										+ REG_ISSUE_DATE_OFFSET										+ REG_ISSUE_DATE_LENGTH)))						.intValue());				// defect 9655, 6701				//	RegPltAge will be set to -1 for all records.				//				laRegistrationData.setRegPltAge(				//					Integer				//						.valueOf(				//							laMFAccess.getStringFromZonedDecimal(				//								asMfRegisResponse.substring(				//									liRecordOffset + REG_PLT_AGE_OFFSET,				//									liRecordOffset				//										+ REG_PLT_AGE_OFFSET				//										+ REG_PLT_AGE_LENGTH)))				//						.intValue());				//	RegPltAge will be set to -1 for all records.				laRegistrationData.setRegPltAge(-1);				// end defect 9655, 6701				laRegistrationData.setRegPltCd(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + REG_PLT_CD_OFFSET,							liRecordOffset								+ REG_PLT_CD_OFFSET								+ REG_PLT_CD_LENGTH)));				laRegistrationData.setRegPltNo(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + REG_PLT_NO_OFFSET,							liRecordOffset								+ REG_PLT_NO_OFFSET								+ REG_PLT_NO_LENGTH)));				laRegistrationData.setRegPltOwnrName(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + REG_PLT_OWNR_NAME_OFFSET,							liRecordOffset								+ REG_PLT_OWNR_NAME_OFFSET								+ REG_PLT_OWNR_NAME_LENGTH)));				laRegistrationData.setRegStkrCd(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + REG_STKR_CD_OFFSET,							liRecordOffset								+ REG_STKR_CD_OFFSET								+ REG_STKR_CD_LENGTH)));				laRegistrationData.setRegStkrNo(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + REG_STKR_NO_OFFSET,							liRecordOffset								+ REG_STKR_NO_OFFSET								+ REG_STKR_NO_LENGTH)));				laRegistrationData.setRenwlMailRtrnIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ RENWL_MAIL_RTRN_INDI_OFFSET,									liRecordOffset										+ RENWL_MAIL_RTRN_INDI_OFFSET										+ RENWL_MAIL_RTRN_INDI_LENGTH)))						.intValue());				laRegistrationData.setResComptCntyNo(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ RES_COMPT_CNTY_NO_OFFSET,									liRecordOffset										+ RES_COMPT_CNTY_NO_OFFSET										+ RES_COMPT_CNTY_NO_LENGTH)))						.intValue());				laRegistrationData.setTireTypeCd(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + TIRE_TYPE_CD_OFFSET,							liRecordOffset								+ TIRE_TYPE_CD_OFFSET								+ TIRE_TYPE_CD_LENGTH)));				laRegistrationData.setVehGrossWt(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ VEH_GROSS_WT_OFFSET,									liRecordOffset										+ VEH_GROSS_WT_OFFSET										+ VEH_GROSS_WT_LENGTH)))						.intValue());				laRegistrationData.setRegInvldIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ REG_INVLD_INDI_OFFSET,									liRecordOffset										+ REG_INVLD_INDI_OFFSET										+ REG_INVLD_INDI_LENGTH)))						.intValue());				laRegistrationData.setRegHotCkIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ REG_HOT_CK_INDI_OFFSET,									liRecordOffset										+ REG_HOT_CK_INDI_OFFSET										+ REG_HOT_CK_INDI_LENGTH)))						.intValue());				laRegistrationData.setPltSeizedIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ PLTS_SEIZD_INDI_OFFSET,									liRecordOffset										+ PLTS_SEIZD_INDI_OFFSET										+ PLTS_SEIZD_INDI_LENGTH)))						.intValue());				laRegistrationData.setStkrSeizdIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ STKR_SEIZD_INDI_OFFSET,									liRecordOffset										+ STKR_SEIZD_INDI_OFFSET										+ STKR_SEIZD_INDI_LENGTH)))						.intValue());				// defect 10508 				// defect 10378				// SOREELECTIONINDI				laRegistrationData.setEMailRenwlReqCd(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset										+ EMAIL_RENWL_REQ_CD_OFFSET,									liRecordOffset										+ EMAIL_RENWL_REQ_CD_OFFSET										+ EMAIL_RENWL_REQ_CD_LENGTH)))						.intValue());				// end defect 10378				// end defect 10508 				//Set the address fields				AddressData laAddressData = new AddressData();				laAddressData.setCity(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RENWL_MAILNG_CITY_OFFSET,							liRecordOffset								+ RENWL_MAILNG_CITY_OFFSET								+ RENWL_MAILNG_CITY_LENGTH)));				laAddressData.setSt1(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RENWL_MAILNG_ST1_OFFSET,							liRecordOffset								+ RENWL_MAILNG_ST1_OFFSET								+ RENWL_MAILNG_ST1_LENGTH)));				laAddressData.setSt2(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RENWL_MAILNG_ST2_OFFSET,							liRecordOffset								+ RENWL_MAILNG_ST2_OFFSET								+ RENWL_MAILNG_ST2_LENGTH)));				laAddressData.setState(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RENWL_MAILNG_STATE_OFFSET,							liRecordOffset								+ RENWL_MAILNG_STATE_OFFSET								+ RENWL_MAILNG_STATE_LENGTH)));				laAddressData.setZpcd(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RENWL_MAILNG_ZPCD_OFFSET,							liRecordOffset								+ RENWL_MAILNG_ZPCD_OFFSET								+ RENWL_MAILNG_ZPCD_LENGTH)));				laAddressData.setZpcdp4(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RENWL_MAILNG_ZPCDP4_OFFSET,							liRecordOffset								+ RENWL_MAILNG_ZPCDP4_OFFSET								+ RENWL_MAILNG_ZPCDP4_LENGTH)));				laRegistrationData.setRenwlMailAddr(laAddressData);				//set the amount/fees fields				StringBuffer laCustActlRegFeeBuffer =					new StringBuffer(						laMFAccess.getStringFromZonedDecimal(							asMfRegisResponse.substring(								liRecordOffset									+ CUST_ACTUAL_REG_FEE_OFFSET,								liRecordOffset									+ CUST_ACTUAL_REG_FEE_OFFSET									+ CUST_ACTUAL_REG_FEE_LENGTH)));				if (laCustActlRegFeeBuffer.charAt(0) != '-')					laCustActlRegFeeBuffer.insert(						CUST_ACTUAL_REG_FEE_LENGTH							- CUST_ACTUAL_REG_FEE_DECIMAL,						'.');				else					laCustActlRegFeeBuffer.insert(						CUST_ACTUAL_REG_FEE_LENGTH							- CUST_ACTUAL_REG_FEE_DECIMAL							+ 1,						'.');				//laCustActlRegFee.setValue(				//	laCustActlRegFeeBuffer.toString());				Dollar laCustActlRegFee =					new Dollar(laCustActlRegFeeBuffer.toString());				laRegistrationData.setCustActlRegFee(laCustActlRegFee);				StringBuffer laRegRefAmtBuffer =					new StringBuffer(						laMFAccess.getStringFromZonedDecimal(							asMfRegisResponse.substring(								liRecordOffset + REG_REF_AMT_OFFSET,								liRecordOffset									+ REG_REF_AMT_OFFSET									+ REG_REF_AMT_LENGTH)));				if (laRegRefAmtBuffer.charAt(0) != '-')				{					laRegRefAmtBuffer.insert(						REG_REF_AMT_LENGTH - REG_REF_AMT_DECIMAL,						'.');				}				else				{					laRegRefAmtBuffer.insert(						REG_REF_AMT_LENGTH - REG_REF_AMT_DECIMAL + 1,						'.');				}				Dollar laRegRefAmt =					new Dollar(laRegRefAmtBuffer.toString());				//laRegRefAmt.setValue(laRegRefAmtBuffer.toString()); 				laRegistrationData.setRegRefAmt(laRegRefAmt);				lvRegistrationDataContainer.addElement(					laRegistrationData);				// defect 9961				//	reference PLTRMVCD				laRegistrationData.setPltRmvCd(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + PLT_RMV_CD_OFFSET,									liRecordOffset										+ PLT_RMV_CD_OFFSET										+ PLT_RMV_CD_LENGTH)))						.intValue());				// end defect 9961				// V21PLTID				laRegistrationData.setV21PltId(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								asMfRegisResponse.substring(									liRecordOffset + V21PLTID_OFFSET,									liRecordOffset										+ V21PLTID_OFFSET										+ V21PLTCD_LENGTH)))						.intValue());				// PRISMLVLCD				laRegistrationData.setPrismLvlCd(					laMFAccess.getStringFromZonedDecimal(						asMfRegisResponse.substring(							liRecordOffset + PRISM_LVL_CD_OFFSET,							liRecordOffset								+ PRISM_LVL_CD_OFFSET								+ PRISM_LVL_CD_LENGTH)));				// defect 10378				// RECPNTEMAIL				laRegistrationData.setRecpntEMail(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + RECPNTEMAIL_OFFSET,							liRecordOffset								+ RECPNTEMAIL_OFFSET								+ RECPNTEMAIL_LENGTH)));				// VTRREGEMRGCD1				laRegistrationData.setVTRRegEmrgCd1(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + VTRREGEMRGCD1_OFFSET,							liRecordOffset								+ VTRREGEMRGCD1_OFFSET								+ VTRREGEMRGCD1_LENGTH)));				// VTRREGEMRGCD2				laRegistrationData.setVTRRegEmrgCd2(					laMFAccess.trimMfString(						asMfRegisResponse.substring(							liRecordOffset + VTRREGEMRGCD2_OFFSET,							liRecordOffset								+ VTRREGEMRGCD2_OFFSET								+ VTRREGEMRGCD2_LENGTH)));				// end defect 10378			}		} // end of if 		return lvRegistrationDataContainer;	}}/* #MfbaMultRegisV# */