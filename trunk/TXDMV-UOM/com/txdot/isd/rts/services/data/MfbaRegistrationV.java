package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.MfAccess;

/*
 *
 * MfbaRegistrationV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/27/2006	Update offset to new format
 * 							defect 6701 Ver Exempts
 * J Rue		10/03/2006	Add HOOPS, ORGNO and PLTBIRTHDATE
 * 							modify setRegistrationDataFromMf()
 * 							defect 6701 Ver Exempts
 * K Harrell	10/09/2006	Modified REG_PLT_CD_OFFSET  (added 7)
 * 							defect 6701 Ver Exempts  
 * J Rue		10/10/2006	RegPltAge will be set = -1 for all records.
 * 							modify setRegistrationDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		10/19/2006	Added SpclRegId	
 * 							modify setRegistrationDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Move process from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue /		10/25/2006	Update comments and JavaDoc
 * 	K Harrell				Corrected RECPNT_NAME_OFFSET
 * 							defect 6701 Ver Exempts
 * J Rue		10/25/2006	Move Rcpt_Name offsets/length to associated 
 * 							variables
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	MFVersion V is not in use.
 * 							deprecate class
 * 							defect 9086 Ver Special Plates
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/07/2008	New attributes for MF version 
 * 							add DISSOCIATECD_OFFSET,DISSOCIATECD_LENGTH,
 * 							V21PLTID_OFFSET, V21PLTID_LENGTH, 
 * 							PRISMLVLCD_OFFSET, PRISMLVLCD_LENGTH
 * 							modify setRegistrationDataFromMF()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		04/08/2008	CustBaseRegFee and DieselFee no longer 
 * 							reference
 * 							modify setRegistrationDataFromMf()
 * 							defect 9581 Ver Defect_POS_A
 * K Harrell	04/14/2008	Adjust offsets for Cancel Plates DocNo
 * 							defect 9557 Ver Defect_POS_A 
 * J Rue		04/30/2008	Replace setDissocisteCd with setPltRmvCd
 * 							modify setRegistrationDataFromMF()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9581 Ver Defect_POS_A
 * J Rue		06/12/2008	Change DISSOCIATECD to PLTRMVDCD
 * 							modify setRegistrationDataFromMF()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setRegistrationDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		11/10/2008	Adjust EmissionCd offset
 * 							modify EMISSIONSOURCECD, CLAIMCOMPTCNTYNO,
 * 								FILETIERCD, CANCSTKRINDI, CANCPLTINDI
 * 							defect 9833 Ver Defect_POS_B
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		01/09/2009	Remove OrgNo. Adjust offsets
 * 							modify setRegistrationDataFromMf()
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		02/25/2009	Adjust offsets for ELT
 * 							Remove Cancel Stickers and OrgNo
 * 							modify setRegistrationDataFromMF()
 * 							defect 9961 Ver Defect_POS_E
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							modify setRegistrationDataFromMf()
 * 							delete CANC_STKR_EXP_YR_OFFSET, 
 * 							CANC_STKR_EXP_YR_LENGTH, CANC_STKR_DATE_OFFSET, 
 * 							CANC_STKR_DATE_LENGTH, CANC_STKR_CD_OFFSET,
 * 							CANC_STKR_CD_LENGTH, CANC_STKR_VIN_OFFSET, 
 * 							CANC_STKR_VIN_LENGTH, CANC_STKR_INDI_OFFSET,
 * 							CANC_STKR_INDI_LENGTH
 * 							defect 9953 Ver Defect_POS_F
 * J Rue		10/06/2009	Adjust offsets for RCCPI version T
 * 							modify all class variables,
 * 								setRegistrationDataFromMf()
 * 							defect 10244 Ver Defect_POS_G
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * M Reyes		04/06/2010	Added ST_OFCL_REELECTION_INDI_OFFSET,
 * 							ST_OFCL_REELECTION_INDI_OFFSET,
 * 							RECPNTEMAIL_OFFSET, RECPNTEMAIL_LENGTH,
 * 							VTRREGEMRGCD1_OFFSET, VTRREGEMRGCD1_LENGTH
 * 							VTRREGEMRGCD2_OFFSET, VTRREGEMRGCD2_LENGTH
 * 							defect 10378 Ver POS_640
 * K Harrell	06/15/2010  RegistrationData.setSORelectionIndi() 
 * 							refactored to setEMailRenwlReqCd()
 * 							add EMAIL_RENWL_REQ_CD_OFFSET,
 * 							 EMAIL_RENWL_REQ_CD_LENGTH
 * 							delete ST_OFCL_REELECTION_INDI_OFFSET,
 * 							ST_OFCL_REELECTION_INDI_LENGTH
 * 							modify setRegistrationDataFromMf()
 * 							defect 10508 Ver 6.5.0
 * K Harrell	06/22/2010	Copied from U (which had already been changed
 * 							in refactoring. 
 * 							defect 10492 Ver 6.5.0 
 * M Reyes		10/05/2010	Copy version V to version T for MF
 * 							MF version U. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		01/03/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670 
 * M Reyes		01/21/2011  Modified FILE_TIER_CD_OFFSET
 * 							and CANC_PLT_INDI_OFFSET
 * 							defect 10710 Ver POS_670
 * K Harrell	11/01/2011	Copy version U to V for MF Version V
 * 							modify FILE_TIER_CD_OFFSET,
 * 							  CANC_PLT_INDI_OFFSET 
 * 							defect 11045 Ver 6.9.0      
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into Registration Data. 
 * Used by the server side business layer. 
 *
 * @version	6.9.0  			11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		09/26/2006 14:10:32
 */
public class MfbaRegistrationV
{
	//define all offsets and lengths
	// Note: offset is -1 minus from CICS file
	// Cancel Plates
	private final static int CANC_PLT_DATE_OFFSET = 11;
	private final static int CANC_PLT_DATE_LENGTH = 8;
	private final static int CANC_REG_EXP_MO_OFFSET = 19;
	private final static int CANC_REG_EXP_MO_LENGTH = 2;
	private final static int CANC_PLT_VIN_OFFSET = 21;
	private final static int CANC_PLT_VIN_LENGTH = 22;
	// defect 9961
	//	No longer used
	// Note: Always use the HOOPSREGPLTNO at 866 (Regis Reg)
	//		 HOOPSREGPLTNO at 51 is for Cancel Plate and is not used. 
	//		 jr)
	//	private final static int CANC_STKR_EXP_YR_OFFSET = 85;
	//	private final static int CANC_STKR_EXP_YR_LENGTH = 4;
	//	private final static int CANC_STKR_DATE_OFFSET = 89;
	//	private final static int CANC_STKR_DATE_LENGTH = 8;
	//	private final static int CANC_STKR_CD_OFFSET = 97;
	//	private final static int CANC_STKR_CD_LENGTH = 8;
	//	private final static int CANC_STKR_VIN_OFFSET = 105;
	//	private final static int CANC_STKR_VIN_LENGTH = 22;
	// end defect 9961
	// Reg Data
	private final static int REG_PLT_NO_OFFSET = 564;
	private final static int REG_PLT_NO_LENGTH = 7;
	private final static int REG_EXP_YR_OFFSET = 571;
	private final static int REG_EXP_YR_LENGTH = 4;
	private final static int REG_STKR_NO_OFFSET = 592;
	private final static int REG_STKR_NO_LENGTH = 10;
	private final static int REG_STKR_CD_OFFSET = 602;
	private final static int REG_STKR_CD_LENGTH = 8;
	private final static int RES_COMPT_CNTY_NO_OFFSET = 616;
	private final static int RES_COMPT_CNTY_NO_LENGTH = 3;
	private final static int REG_CLASS_CD_OFFSET = 619;
	private final static int REG_CLASS_CD_LENGTH = 3;
	private final static int REG_EFF_DATE_OFFSET = 622;
	private final static int REG_EFF_DATE_LENGTH = 8;
	private final static int REG_EXP_MO_OFFSET = 630;
	private final static int REG_EXP_MO_LENGTH = 2;
	private final static int REG_ISSUE_DATE_OFFSET = 632;
	private final static int REG_ISSUE_DATE_LENGTH = 8;
	private final static int REG_PLT_CD_OFFSET = 640;
	private final static int REG_PLT_CD_LENGTH = 8;
	// Customer fees
	private final static int CUST_ACTUAL_REG_FEE_OFFSET = 648;
	private final static int CUST_ACTUAL_REG_FEE_LENGTH = 6;
	private final static int CUST_ACTUAL_REG_FEE_DECIMAL = 2;
	// Reciepient Name Data
	private final static int RECPNT_NAME_OFFSET = 654;
	private final static int RECPNT_NAME_LENGTH = 30;
	private final static int RENWL_MAILNG_ST1_OFFSET = 684;
	private final static int RENWL_MAILNG_ST1_LENGTH = 30;
	private final static int RENWL_MAILNG_ST2_OFFSET = 714;
	private final static int RENWL_MAILNG_ST2_LENGTH = 30;
	private final static int RENWL_MAILNG_CITY_OFFSET = 744;
	private final static int RENWL_MAILNG_CITY_LENGTH = 19;
	private final static int RENWL_MAILNG_STATE_OFFSET = 763;
	private final static int RENWL_MAILNG_STATE_LENGTH = 2;
	private final static int RENWL_MAILNG_ZPCD_OFFSET = 765;
	private final static int RENWL_MAILNG_ZPCD_LENGTH = 5;
	private final static int RENWL_MAILNG_ZPCDP4_OFFSET = 770;
	private final static int RENWL_MAILNG_ZPCDP4_LENGTH = 4;
	private final static int RENWL_MAIL_RTRN_INDI_OFFSET = 774;
	private final static int RENWL_MAIL_RTRN_INDI_LENGTH = 1;
	// Indicators
	private final static int DPS_SAFETY_SUSPN_INDI_OFFSET = 775;
	private final static int DPS_SAFETY_SUSPN_INDI_LENGTH = 1;
	private final static int HVY_VEH_USE_TAX_INDI_OFFSET = 776;
	private final static int HVY_VEH_USE_TAX_INDI_LENGTH = 1;
	private final static int REG_INVLD_INDI_OFFSET = 777;
	private final static int REG_INVLD_INDI_LENGTH = 1;
	private final static int REG_HOT_CK_INDI_OFFSET = 779;
	private final static int REG_HOT_CK_INDI_LENGTH = 1;
	private final static int PLTS_SEIZD_INDI_OFFSET = 780;
	private final static int PLTS_SEIZD_INDI_LENGTH = 1;
	private final static int STKR_SEIZD_INDI_OFFSET = 781;
	private final static int STKR_SEIZD_INDI_LENGTH = 1;

	// defect 10508
	// defect 10378
	// New Attributes for verion U 
	//private final static int ST_OFCL_REELECTION_INDI_OFFSET = 782;
	//private final static int ST_OFCL_REELECTION_INDI_LENGTH = 1;
	// end defect 10378
	private final static int EMAIL_RENWL_REQ_CD_OFFSET = 782;
	private final static int EMAIL_RENWL_REQ_CD_LENGTH = 1;
	// end defect 10508 

	// defect 10378
	// Modify Offset
	private final static int REG_REF_AMT_OFFSET = 783;
	private final static int REG_REF_AMT_LENGTH = 6;
	private final static int REG_REF_AMT_DECIMAL = 2;
	private final static int NOTFYING_CITY_OFFSET = 789;
	private final static int NOTFYING_CITY_LENGTH = 19;
	// Previous Reg
	private final static int PREV_PLT_NO_OFFSET = 808;
	private final static int PREV_PLT_NO_LENGTH = 7;
	private final static int PREV_EXP_MO_OFFSET = 815;
	private final static int PREV_EXP_MO_LENGTH = 2;
	private final static int PREV_EXP_YR_OFFSET = 817;
	private final static int PREV_EXP_YR_LENGTH = 4;

	private final static int VEH_GROSS_WT_OFFSET = 821;
	private final static int VEH_GROSS_WT_LENGTH = 6;
	private final static int TIRE_TYPE_CD_OFFSET = 827;
	private final static int TIRE_TYPE_CD_LENGTH = 1;
	private final static int REG_PLT_OWNR_NAME_OFFSET = 828;
	private final static int REG_PLT_OWNR_NAME_LENGTH = 30;

	// SPCREGINDI, HOOPS, ORGNO, and Plate Birth Date
	private final static int SPC_REG_INDI_OFFSET = 858;
	private final static int SPC_REG_INDI_LENGTH = 9;
	private final static int HOOPSREGPLTNO_OFFSET = 867;
	private final static int HOOPSREGPLTNO_LENGTH = 7;
	// defect 9961
	//	No longer used
	//	private final static int ORGNO_OFFSET = 927;
	//	private final static int ORGNO_LENGTH = 4;
	// end defect 9961
	private final static int PLTBIRTHDATE_OFFSET = 874;
	private final static int PLTBIRTHDATE_LENGTH = 6;
	// defect 9557
	//	New Attributes for version V
	private final static int PLTRMVDCD_OFFSET = 880;
	private final static int PLTRMVDCD_LENGTH = 1;
	private final static int V21PLTID_OFFSET = 881;
	private final static int V21PLTID_LENGTH = 10;
	private final static int PRISMLVLCD_OFFSET = 891;
	private final static int PRISMLVLCD_LENGTH = 1;
	// end defect 9557
	// end defect 10378
	// defect 10378
	// New Attributes for verion U
	private final static int RECPNTEMAIL_OFFSET = 892;
	private final static int RECPNTEMAIL_LENGTH = 50;
	private final static int VTRREGEMRGCD1_OFFSET = 942;
	private final static int VTRREGEMRGCD1_LENGTH = 1;
	private final static int VTRREGEMRGCD2_OFFSET = 943;
	private final static int VTRREGEMRGCD2_LENGTH = 1;
	// end defect 10378
	// defect 10378
	// Modify offsets
	private final static int EXMPT_INDI_OFFSET = 1417;
	private final static int EXMPT_INDI_LENGTH = 1;
	private final static int EMISSION_SOURCE_CD_OFFSET = 1666;
	private final static int EMISSION_SOURCE_CD_LENGTH = 1;
	private final static int CLAIM_COMPT_CNTY_NO_OFFSET = 1667;
	private final static int CLAIM_COMPT_CNTY_NO_LENGTH = 3;
	
	// defect 11045 
	//private final static int FILE_TIER_CD_OFFSET = 1695;
	//private final static int CANC_PLT_INDI_OFFSET = 1696;
	private final static int FILE_TIER_CD_OFFSET = 1703;
	private final static int CANC_PLT_INDI_OFFSET = 1704;
	// end defect 11045
	
	private final static int FILE_TIER_CD_LENGTH = 1;
	private final static int CANC_PLT_INDI_LENGTH = 1;

	
	/**
	 * 
	 */
	public MfbaRegistrationV()
	{
		super();
	}

	public static void main(String[] args)
	{
	}
	/**
	 * Constructs the registrationData object. 
	 * 
	 * @return RegistrationData
	 * @param asMfResponse String
	 */
	public RegistrationData setRegistrationDataFromMf(String asMfResponse)
	{
		//create registration data
		RegistrationData laRegistrationData = new RegistrationData();
		String lsMfTtlRegResponse = asMfResponse;
		MfAccess laMFAccess = new MfAccess();

		//set all values from REGIS
		// Set Cancel Plates/Cancel Stickers
		if (!(asMfResponse.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (lsMfTtlRegResponse != null))
		{
			laRegistrationData.setCanclPltDt(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								CANC_PLT_DATE_OFFSET,
								CANC_PLT_DATE_OFFSET
									+ CANC_PLT_DATE_LENGTH)))
					.intValue());

			laRegistrationData.setCancRegExpMo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								CANC_REG_EXP_MO_OFFSET,
								CANC_REG_EXP_MO_OFFSET
									+ CANC_REG_EXP_MO_LENGTH)))
					.intValue());

			laRegistrationData.setCancPltVin(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						CANC_PLT_VIN_OFFSET,
						CANC_PLT_VIN_OFFSET + CANC_PLT_VIN_LENGTH)));

			// defect 9961
			//	Cancel sticker no longer referenced
			//			laRegistrationData.setCancStkrCd(
			//				laMFAccess.trimMfString(
			//					lsMfTtlRegResponse.substring(
			//						CANC_STKR_CD_OFFSET,
			//						CANC_STKR_CD_OFFSET + CANC_STKR_CD_LENGTH)));
			//
			//			laRegistrationData.setCancStkrDt(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							lsMfTtlRegResponse.substring(
			//								CANC_STKR_DATE_OFFSET,
			//								CANC_STKR_DATE_OFFSET
			//									+ CANC_STKR_DATE_LENGTH)))
			//					.intValue());
			//
			//			laRegistrationData.setCancStkrExpYr(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							lsMfTtlRegResponse.substring(
			//								CANC_STKR_EXP_YR_OFFSET,
			//								CANC_STKR_EXP_YR_OFFSET
			//									+ CANC_STKR_EXP_YR_LENGTH)))
			//					.intValue());
			//
			//			laRegistrationData.setCancStkrVin(
			//				laMFAccess.trimMfString(
			//					lsMfTtlRegResponse.substring(
			//						CANC_STKR_VIN_OFFSET,
			//						CANC_STKR_VIN_OFFSET + CANC_STKR_VIN_LENGTH)));
			//
			//			laRegistrationData.setCancStkrIndi(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							lsMfTtlRegResponse.substring(
			//								CANC_STKR_INDI_OFFSET,
			//								CANC_STKR_INDI_OFFSET
			//									+ CANC_STKR_INDI_LENGTH)))
			//					.intValue());
			// end defect 9961
			laRegistrationData.setCancPltIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								CANC_PLT_INDI_OFFSET,
								CANC_PLT_INDI_OFFSET
									+ CANC_PLT_INDI_LENGTH)))
					.intValue());

			// Reg Data			
			laRegistrationData.setRegPltNo(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						REG_PLT_NO_OFFSET,
						REG_PLT_NO_OFFSET + REG_PLT_NO_LENGTH)));

			laRegistrationData.setRegExpYr(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_EXP_YR_OFFSET,
								REG_EXP_YR_OFFSET
									+ REG_EXP_YR_LENGTH)))
					.intValue());

			laRegistrationData.setRegClassCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_CLASS_CD_OFFSET,
								REG_CLASS_CD_OFFSET
									+ REG_CLASS_CD_LENGTH)))
					.intValue());

			laRegistrationData.setRegEffDt(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_EFF_DATE_OFFSET,
								REG_EFF_DATE_OFFSET
									+ REG_EFF_DATE_LENGTH)))
					.intValue());

			laRegistrationData.setRegExpMo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_EXP_MO_OFFSET,
								REG_EXP_MO_OFFSET
									+ REG_EXP_MO_LENGTH)))
					.intValue());

			laRegistrationData.setRegIssueDt(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_ISSUE_DATE_OFFSET,
								REG_ISSUE_DATE_OFFSET
									+ REG_ISSUE_DATE_LENGTH)))
					.intValue());

			// defect 6701
			//	RegPltAge will be set to -1 for all records.
			laRegistrationData.setRegPltAge(-1);
			//			laRegistrationData.setRegPltAge(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							lsMfTtlRegResponse.substring(
			//								REG_PLT_AGE_OFFSET,
			//								REG_PLT_AGE_OFFSET
			//									+ REG_PLT_AGE_LENGTH)))
			//					.intValue());
			// end defect 6701

			laRegistrationData.setRegPltCd(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						REG_PLT_CD_OFFSET,
						REG_PLT_CD_OFFSET + REG_PLT_CD_LENGTH)));

			laRegistrationData.setRegStkrNo(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						REG_STKR_NO_OFFSET,
						REG_STKR_NO_OFFSET + REG_STKR_NO_LENGTH)));

			laRegistrationData.setRegStkrCd(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						REG_STKR_CD_OFFSET,
						REG_STKR_CD_OFFSET + REG_STKR_CD_LENGTH)));

			laRegistrationData.setResComptCntyNo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								RES_COMPT_CNTY_NO_OFFSET,
								RES_COMPT_CNTY_NO_OFFSET
									+ RES_COMPT_CNTY_NO_LENGTH)))
					.intValue());

			// Customer Fees
			//	set CustActlRegFee
			StringBuffer laCustActlRegFeeBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							CUST_ACTUAL_REG_FEE_OFFSET,
							CUST_ACTUAL_REG_FEE_OFFSET
								+ CUST_ACTUAL_REG_FEE_LENGTH)));

			if (laCustActlRegFeeBuffer.charAt(0) != '-')
			{
				laCustActlRegFeeBuffer.insert(
					CUST_ACTUAL_REG_FEE_LENGTH
						- CUST_ACTUAL_REG_FEE_DECIMAL,
					'.');
			}
			else
			{
				laCustActlRegFeeBuffer.insert(
					CUST_ACTUAL_REG_FEE_LENGTH
						- CUST_ACTUAL_REG_FEE_DECIMAL
						+ 1,
					'.');
			}

			Dollar laCustActlRegFee =
				new Dollar(laCustActlRegFeeBuffer.toString());
			laRegistrationData.setCustActlRegFee(laCustActlRegFee);

			// Receipient Name Data
			laRegistrationData.setRecpntName(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RECPNT_NAME_OFFSET,
						RECPNT_NAME_OFFSET + RECPNT_NAME_LENGTH)));

			//Set the address fields
			AddressData laAddressData = new AddressData();
			laAddressData.setSt1(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RENWL_MAILNG_ST1_OFFSET,
						RENWL_MAILNG_ST1_OFFSET
							+ RENWL_MAILNG_ST1_LENGTH)));

			laAddressData.setSt2(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RENWL_MAILNG_ST2_OFFSET,
						RENWL_MAILNG_ST2_OFFSET
							+ RENWL_MAILNG_ST2_LENGTH)));

			laAddressData.setCity(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RENWL_MAILNG_CITY_OFFSET,
						RENWL_MAILNG_CITY_OFFSET
							+ RENWL_MAILNG_CITY_LENGTH)));

			laAddressData.setState(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RENWL_MAILNG_STATE_OFFSET,
						RENWL_MAILNG_STATE_OFFSET
							+ RENWL_MAILNG_STATE_LENGTH)));

			laAddressData.setZpcd(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RENWL_MAILNG_ZPCD_OFFSET,
						RENWL_MAILNG_ZPCD_OFFSET
							+ RENWL_MAILNG_ZPCD_LENGTH)));

			laAddressData.setZpcdp4(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RENWL_MAILNG_ZPCDP4_OFFSET,
						RENWL_MAILNG_ZPCDP4_OFFSET
							+ RENWL_MAILNG_ZPCDP4_LENGTH)));

			laRegistrationData.setRenwlMailAddr(laAddressData);

			laRegistrationData.setRenwlMailRtrnIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								RENWL_MAIL_RTRN_INDI_OFFSET,
								RENWL_MAIL_RTRN_INDI_OFFSET
									+ RENWL_MAIL_RTRN_INDI_LENGTH)))
					.intValue());

			// Indicators
			laRegistrationData.setDpsSaftySuspnIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								DPS_SAFETY_SUSPN_INDI_OFFSET,
								DPS_SAFETY_SUSPN_INDI_OFFSET
									+ DPS_SAFETY_SUSPN_INDI_LENGTH)))
					.intValue());

			laRegistrationData.setHvyVehUseTaxIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								HVY_VEH_USE_TAX_INDI_OFFSET,
								HVY_VEH_USE_TAX_INDI_OFFSET
									+ HVY_VEH_USE_TAX_INDI_LENGTH)))
					.intValue());

			laRegistrationData.setRegInvldIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_INVLD_INDI_OFFSET,
								REG_INVLD_INDI_OFFSET
									+ REG_INVLD_INDI_LENGTH)))
					.intValue());

			laRegistrationData.setRegHotCkIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								REG_HOT_CK_INDI_OFFSET,
								REG_HOT_CK_INDI_OFFSET
									+ REG_HOT_CK_INDI_LENGTH)))
					.intValue());

			laRegistrationData.setPltSeizedIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								PLTS_SEIZD_INDI_OFFSET,
								PLTS_SEIZD_INDI_OFFSET
									+ PLTS_SEIZD_INDI_LENGTH)))
					.intValue());

			laRegistrationData.setStkrSeizdIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								STKR_SEIZD_INDI_OFFSET,
								STKR_SEIZD_INDI_OFFSET
									+ STKR_SEIZD_INDI_LENGTH)))
					.intValue());

			// defect 10508 
			// defect 10378
			// New attributes for version U
			laRegistrationData.setEMailRenwlReqCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								EMAIL_RENWL_REQ_CD_OFFSET,
								EMAIL_RENWL_REQ_CD_OFFSET
									+ EMAIL_RENWL_REQ_CD_LENGTH)))
					.intValue());
			// end defect 10378
			// end defect 10508

			StringBuffer laRegRefAmtBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							REG_REF_AMT_OFFSET,
							REG_REF_AMT_OFFSET + REG_REF_AMT_LENGTH)));

			if (laRegRefAmtBuffer.charAt(0) != '-')
			{
				laRegRefAmtBuffer.insert(
					REG_REF_AMT_LENGTH - REG_REF_AMT_DECIMAL,
					'.');
			}
			else
			{
				laRegRefAmtBuffer.insert(
					REG_REF_AMT_LENGTH - REG_REF_AMT_DECIMAL + 1,
					'.');
			}

			Dollar laRegRefAmt =
				new Dollar(laRegRefAmtBuffer.toString());
			laRegistrationData.setRegRefAmt(laRegRefAmt);

			laRegistrationData.setNotfyngCity(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						NOTFYING_CITY_OFFSET,
						NOTFYING_CITY_OFFSET + NOTFYING_CITY_LENGTH)));

			// Previous Reg
			laRegistrationData.setPrevPltNo(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						PREV_PLT_NO_OFFSET,
						PREV_PLT_NO_OFFSET + PREV_PLT_NO_LENGTH)));

			laRegistrationData.setPrevExpMo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								PREV_EXP_MO_OFFSET,
								PREV_EXP_MO_OFFSET
									+ PREV_EXP_MO_LENGTH)))
					.intValue());

			laRegistrationData.setPrevExpYr(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								PREV_EXP_YR_OFFSET,
								PREV_EXP_YR_OFFSET
									+ PREV_EXP_YR_LENGTH)))
					.intValue());

			laRegistrationData.setVehGrossWt(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								VEH_GROSS_WT_OFFSET,
								VEH_GROSS_WT_OFFSET
									+ VEH_GROSS_WT_LENGTH)))
					.intValue());

			laRegistrationData.setTireTypeCd(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						TIRE_TYPE_CD_OFFSET,
						TIRE_TYPE_CD_OFFSET + TIRE_TYPE_CD_LENGTH)));

			laRegistrationData.setRegPltOwnrName(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						REG_PLT_OWNR_NAME_OFFSET,
						REG_PLT_OWNR_NAME_OFFSET
							+ REG_PLT_OWNR_NAME_LENGTH)));

			// set SpcRegIndi, HOOPS, ORGNO, and Plate Birth Date
			laRegistrationData.setSpclRegId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								SPC_REG_INDI_OFFSET,
								SPC_REG_INDI_OFFSET
									+ SPC_REG_INDI_LENGTH)))
					.intValue());

			// Set HOOPS
			laRegistrationData.setHoopsRegPltNo(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						HOOPSREGPLTNO_OFFSET,
						HOOPSREGPLTNO_OFFSET + HOOPSREGPLTNO_LENGTH)));

			// defect 9961
			//	No longer referenced
			// Set ORGNO
			//			laRegistrationData.setOrgNo(
			//				laMFAccess.trimMfString(
			//					lsMfTtlRegResponse.substring(
			//						ORGNO_OFFSET,
			//						ORGNO_OFFSET + ORGNO_LENGTH)));
			// end defect 9961

			// Set Plate Birth Date
			laRegistrationData.setPltBirthDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								PLTBIRTHDATE_OFFSET,
								PLTBIRTHDATE_OFFSET
									+ PLTBIRTHDATE_LENGTH)))
					.intValue());

			// defect 9557
			//	New attributes for version V
			// 	Replace setDissocisteCd with setPltRmvCd
			laRegistrationData.setPltRmvCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								PLTRMVDCD_OFFSET,
								PLTRMVDCD_OFFSET + PLTRMVDCD_LENGTH)))
					.intValue());

			laRegistrationData.setV21PltId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								V21PLTID_OFFSET,
								V21PLTID_OFFSET + V21PLTID_LENGTH)))
					.intValue());

			laRegistrationData.setPrismLvlCd(
				laMFAccess.getStringFromZonedDecimal(
					lsMfTtlRegResponse.substring(
						PRISMLVLCD_OFFSET,
						PRISMLVLCD_OFFSET + PRISMLVLCD_LENGTH)));
			// end defect 9557
			// defect 10378
			// New attributes for version U
			laRegistrationData.setRecpntEMail(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						RECPNTEMAIL_OFFSET,
						RECPNTEMAIL_OFFSET + RECPNTEMAIL_LENGTH)));

			laRegistrationData.setVTRRegEmrgCd1(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						VTRREGEMRGCD1_OFFSET,
						VTRREGEMRGCD1_OFFSET + VTRREGEMRGCD1_LENGTH)));

			laRegistrationData.setVTRRegEmrgCd2(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						VTRREGEMRGCD2_OFFSET,
						VTRREGEMRGCD2_OFFSET + VTRREGEMRGCD2_LENGTH)));
			// end defect 10378			

			laRegistrationData.setExmptIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								EXMPT_INDI_OFFSET,
								EXMPT_INDI_OFFSET
									+ EXMPT_INDI_LENGTH)))
					.intValue());

			laRegistrationData.setEmissionSourceCd(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						EMISSION_SOURCE_CD_OFFSET,
						EMISSION_SOURCE_CD_OFFSET
							+ EMISSION_SOURCE_CD_LENGTH)));

			laRegistrationData.setClaimComptCntyNo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								CLAIM_COMPT_CNTY_NO_OFFSET,
								CLAIM_COMPT_CNTY_NO_OFFSET
									+ CLAIM_COMPT_CNTY_NO_LENGTH)))
					.intValue());

			laRegistrationData.setFileTierCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfTtlRegResponse.substring(
								FILE_TIER_CD_OFFSET,
								FILE_TIER_CD_OFFSET
									+ FILE_TIER_CD_LENGTH)))
					.intValue());

		} // end of if
		return laRegistrationData;
	}
}
