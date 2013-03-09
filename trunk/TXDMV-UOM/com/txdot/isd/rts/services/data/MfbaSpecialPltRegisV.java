package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaSpecialPltRegisV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		02/02/2007	Add JavaDoc
 * 							defect 9086, Ver Special Plates
 * J Rue		02/08/2007	Change SPCLFee from int to Dollar
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/19/2007	Modify to use SpclPltRegisData
 * 							OwnerData, AddressData
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 9085 Ver Special Plates
 * J Rue		02/21/2007	Set RegPltAge = -1
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/22/2007	Rename setSpecRegId to setSpclRegId
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Add Add offsets and code
 * 							defect 9086, Ver Special Plates
 * K Harrell	05/24/2007	Corrected typo on ResComptCntyNo
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/04/2008	Prep for next release 
 * 							defect 9546 Ver 3 Amigos PH A   
 * J Rue		05/06/2008	Add new fields for CICS version V
 * 							add setSpclPltRegisDataFromMF()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/17/2008	Remove reference to 6 digit DlrGDN
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setSpclPltRegisDataFromMF()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 9655 Ver Defect_POS_D
 * K Harrell	01/07/2009	SpclPltRegisData refactor of RegExpMo/Yr ==> 
 * 							PltExpMo/Yr
 * 							defect 9864 Ver Defect_POS_D
 * J Rue		02/26/2009	Adjust offsets for ELT
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new Data Objects 
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 10112 Ver Defect_POS_F
 * J Zwiener	09/01/2009	Set Original Plate Expiration Month
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 10097 Ver Defect_POS_F   
 * J Rue		10/05/2009	Adjust offsets for RCCPI version T
 * 							modify all class variables,
 * 								setSpclPltRegisDataFromMF()
 * 							defect 10244 Ver Defect_POS_G
 * K Harrell	10/14/2009	delete PRIVACY_OPT_CD_OFFSET,
 * 							PRIVACY_OPT_CD_LENGTH
 * 							modify setSpclPltRegisDataFromMF()
 * 							defect 10246 Ver Defect_POS_G       
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * K Harrell	11/23/2009	Merge Issue; 10097 duplicated.
 * 							Ver Defect_POS_H
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * M Reyes		04/06/2010	Added
 * 							RESRV_EFF_DATE_OFFSET,RESRV_EFF_DATE_LENGTH,
 * 							RESRV_REASN_CD_OFFSET,RESRV_REASN_CD_LENGTH
 * 							MKTNG_ALLOWD_INDI_OFFSET, 
 * 							MKTNG_ALLOWD_INDI_LENGTH,FIN_DOC_NO_OFFSET,
 * 							FIN_DOC_NO_LENGTH, VENDOR_TRANS_DATE_OFFSET,
 * 							VENDOR_TRANS_DATE_LENGTH, 
 * 							ITRNT_TRACE_NO_OFFSET,ITRNT_TRACE_NO_LENGTH,
 * 							AUCTN_PLT_INDI_OFFSET,AUCTN_PLT_INDI_LENGTH,
 * 							AUCTN_PD_AMT_OFFSET, AUCTN_PD_AMT_LENGTH,
 * 							AUCTN_PD_AMT_DECIMAL.
 * 							Modified PLT_OWNR_DLR_GDN_OFFSET,
 * 							PLT_OWNR_DLR_GDN_LENGTH.
 * 							defect 10378 Ver POS_640  
 * K Harrell 	06/22/2010  Copy version U to version V for MF
 *        					MF version V. Modified All.
 *        					defect 10492 Ver 6.5.0
 * K Harrell 	06/22/2010  add ELECTIONPNDNGINDI_OFFSET, 
 * 							 ELECTIONPNDNGINDI_LENGTH 
 *        					modify setSpclPltRegisDataFromMF()
 *        					defect 10492 Ver 6.5.0
 * K Harrell	06/28/2010	modify offsets after ELECTIONPNDNGINDI_OFFSET 
 * 							by +1
 * 							defect 10492 Ver 6.5.0 
 * M Reyes		10/06/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		01/10/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0   
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into SpecialPlateRegis. 
 * Used by the server side business layer. 
 *
 * @version	6.9.0			11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		04/27/2007 14:09:32
 */
public class MfbaSpecialPltRegisV
{
	//define all lengths and offsets (from doc 660)
	// Note: offset is -1 minus from CICS file
	// SPCL-PLT-REGIS
	private final static int SPCL_REG_ID_OFFSET = 0;
	private final static int SPCL_REG_ID_LENGTH = 9;
	private final static int HOOPS_REG_PLT_NO_OFFSET = 9;
	private final static int HOOPS_REG_PLT_NO_LENGTH = 7;
	private final static int REG_PLT_NO_OFFSET = 16;
	private final static int REG_PLT_NO_LENGTH = 7;
	//	Rename REG_EXP_YR_OFFSET to PLT_EXP_YR_OFFSET
	private final static int PLT_EXP_YR_OFFSET = 23;
	private final static int PLT_EXP_YR_LENGTH = 4;
	private final static int MFG_PLT_NO_OFFSET = 27;
	private final static int MFG_PLT_NO_LENGTH = 8;
	// PLTOWNRDATA
	private final static int PLT_OWNR_ID_OFFSET = 35;
	private final static int PLT_OWNR_ID_LENGTH = 9;
	private final static int PLT_OWNR_NAME1_OFFSET = 44;
	private final static int PLT_OWNR_NAME1_LENGTH = 30;
	private final static int PLT_OWNR_NAME2_OFFSET = 74;
	private final static int PLT_OWNR_NAME2_LENGTH = 30;
	// PLTOWNRADDR
	private final static int PLT_OWNR_ST1_OFFSET = 107;
	private final static int PLT_OWNR_ST1_LENGTH = 30;
	private final static int PLT_OWNR_ST2_OFFSET = 137;
	private final static int PLT_OWNR_ST2_LENGTH = 30;
	private final static int PLT_OWNR_CITY_OFFSET = 167;
	private final static int PLT_OWNR_CITY_LENGTH = 19;
	private final static int PLT_OWNR_STATE_OFFSET = 186;
	private final static int PLT_OWNR_STATE_LENGTH = 2;
	// PLTOWNRZIPCODE
	private final static int PLT_OWNR_ZIP_CD_OFFSET = 188;
	private final static int PLT_OWNR_ZIP_CD_LENGTH = 5;
	private final static int PLT_OWNR_ZIP_CD_P4_OFFSET = 193;
	private final static int PLT_OWNR_ZIP_CD_P4_LENGTH = 4;
	private final static int PLT_OWNR_CNTRY_OFFSET = 197;
	private final static int PLT_OWNR_CNTRY_LENGTH = 4;
	// DATA	
	private final static int PLT_OWNR_PHONE_OFFSET = 201;
	private final static int PLT_OWNR_PHONE_LENGTH = 10;
	private final static int PLT_OWNR_EMAIL_OFFSET = 211;
	private final static int PLT_OWNR_EMAIL_LENGTH = 50;
	private final static int PLT_OWNR_OFC_CD_OFFSET = 261;
	private final static int PLT_OWNR_OFC_CD_LENGTH = 2;
	private final static int PLT_OWNR_DIST_OFFSET = 263;
	private final static int PLT_OWNR_DIST_LENGTH = 3;

	private final static int TRANS_CD_OFFSET = 266;
	private final static int TRANS_CD_LENGTH = 6;
	private final static int REG_CLASS_CD_OFFSET = 272;
	private final static int REG_CLASS_CD_LENGTH = 3;
	//	Rename REG_EXP_MO_OFFSET to PLT_EXP_MO_OFFSET
	private final static int PLT_EXP_MO_OFFSET = 275;
	private final static int PLT_EXP_MO_LENGTH = 2;
	private final static int PLT_APPL_DATE_OFFSET = 277;
	private final static int PLT_APPL_DATE_LENGTH = 8;
	private final static int REG_PLT_CD_OFFSET = 285;
	private final static int REG_PLT_CD_LENGTH = 8;
	private final static int PLT_BIRTH_DATE_OFFSET = 293;
	private final static int PLT_BIRTH_DATE_LENGTH = 6;
	private final static int RES_COMPT_CNTY_NO_OFFSET = 299;
	private final static int RES_COMPT_CNTY_NO_LENGTH = 3;
	private final static int ADDL_SET_INDI_OFFSET = 302;
	private final static int ADDL_SET_INDI_LENGTH = 1;
	private final static int MFG_STATUS_CD_OFFSET = 303;
	private final static int MFG_STATUS_CD_LENGTH = 1;
	private final static int ORG_NO_OFFSET = 304;
	private final static int ORG_NO_LENGTH = 4;
	private final static int SAUDIT_TRAIL_TRANS_ID_OFFSET = 308;
	private final static int SAUDIT_TRAIL_TRANS_ID_LENGTH = 17;
	private final static int DEL_INDI_OFFSET = 325;
	private final static int DEL_INDI_LENGTH = 1;

	// defect 10246 
	// private final static int PRIVACY_OPT_CD_OFFSET = 326;
	// private final static int PRIVACY_OPT_CD_LENGTH = 1;
	// end defect 10246

	// defect 10378
	// New attributes for U
	private final static int RESRV_EFF_DATE_OFFSET = 327;
	private final static int RESRV_EFF_DATE_LENGTH = 8;
	private final static int RESRV_REASN_CD_OFFSET = 335;
	private final static int RESRV_REASN_CD_LENGTH = 1;
	// end defect 10378 

	// defect 10378
	private final static int MFG_DATE_OFFSET = 336;
	private final static int MFG_DATE_LENGTH = 8;
	private final static int PLT_SET_NO_OFFSET = 344;
	private final static int PLT_SET_NO_LENGTH = 2;
	private final static int SPCL_DOCNO_OFFSET = 346;
	private final static int SPCL_DOCNO_LENGTH = 17;
	private final static int SPCL_FEE_OFFSET = 363;
	private final static int SPCL_FEE_LENGTH = 6;
	private final static int SPCL_FEE_DECIMAL = 2;
	// end defect 10378

	// defect 10378
	// New attribute for U
	private final static int MKTNG_ALLOWD_INDI_OFFSET = 369;
	private final static int MKTNG_ALLOWD_INDI_LENGTH = 1;
	// end defect 10378

	// defect 10378
	private final static int ISA_INDI_OFFSET = 370;
	private final static int ISA_INDI_LENGTH = 1;
	private final static int SPCL_REMKS_OFFSET = 371;
	private final static int SPCL_REMKS_LENGTH = 30;
	private final static int TRANS_EMP_ID_OFFSET = 401;
	private final static int TRANS_EMP_ID_LENGTH = 7;
	// end defect 10378

	// defect 10492
	// New for 6.4.0 
	private final static int ELECTIONPNDNGINDI_OFFSET = 408;
	private final static int ELECTIONPNDNGINDI_LENGTH = 1;
	// end defect 10492 


	// defect 10492 
	// + 1 for all Offsets (6.4.0 to 6.5.0) 
	// defect 10378
	// changed attribute for U
	private final static int PLT_OWNR_DLR_GDN_OFFSET = 409;
	private final static int PLT_OWNR_DLR_GDN_LENGTH = 10;
	// end defect 10378

	// defect 10378
	private final static int PLTRMVCD_OFFSET = 419;
	private final static int PLTRMVCD_LENGTH = 1;
	private final static int V21PLTID_OFFSET = 420;
	private final static int V21PLTID_LENGTH = 10;
	private final static int PLT_VALIDITY_TERM_OFFSET = 430;
	private final static int PLT_VALIDITY_TERM_LENGTH = 2;
	// end defect 10378

	// defect 10378
	// New attributes for U
	private final static int FIN_DOC_NO_OFFSET = 432;
	private final static int FIN_DOC_NO_LENGTH = 8;
	private final static int VENDOR_TRANS_DATE_OFFSET = 440;
	private final static int VENDOR_TRANS_DATE_LENGTH = 8;
	private final static int ITRNT_TRACE_NO_OFFSET = 448;
	private final static int ITRNT_TRACE_NO_LENGTH = 15;
	private final static int AUCTN_PLT_INDI_OFFSET = 463;
	private final static int AUCTN_PLT_INDI_LENGTH = 1;
	private final static int AUCTN_PD_AMT_OFFSET = 464;
	private final static int AUCTN_PD_AMT_LENGTH = 11;
	private final static int AUCTN_PD_AMT_DECIMAL = 2;
	// end defect 10378
	// end defect 10492 

	/**
	 *  MfbaSpecialPltRegisV constructor 
	 */
	public MfbaSpecialPltRegisV()
	{
		super();
	}

	/**
	 * Sets and returns Special Plates Regis data from the mainframe 
	 * response. 
	 * 
	 * @param asMfResponse String
	 * @return SpecialPlatesRegisData
	 */
	public SpecialPlatesRegisData setSpclPltRegisDataFromMF(String asMfResponse)
	{
		//create the return object
		SpecialPlatesRegisData laMfSpclPltRegisData =
			new SpecialPlatesRegisData();
		String lsMfSpclPltRegisResponse = asMfResponse;
		MfAccess laMFAccess = new MfAccess();
		// defect 9085 
		OwnerData laOwnerData = new OwnerData();
		AddressData laAddrData = new AddressData();
		// end defect 9085

		if (!(lsMfSpclPltRegisResponse
			.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (lsMfSpclPltRegisResponse != null))
		{
			// defect 9086
			//	Set RegPltAge = -1, RegPltAge is not MF data
			laMfSpclPltRegisData.setRegPltAge(-1);
			// end defect 9086

			// SpclRegId
			// defect 9086
			//	rename setSpecRegId to setSpclRegId
			laMfSpclPltRegisData.setSpclRegId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								SPCL_REG_ID_OFFSET,
								SPCL_REG_ID_OFFSET
									+ SPCL_REG_ID_LENGTH)))
					.intValue());
			// end defect 9086

			// HOOPSRegPltNo
			laMfSpclPltRegisData.setHOOPSRegPltNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						HOOPS_REG_PLT_NO_OFFSET,
						HOOPS_REG_PLT_NO_OFFSET
							+ HOOPS_REG_PLT_NO_LENGTH)));

			// RegPltNo
			laMfSpclPltRegisData.setRegPltNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						REG_PLT_NO_OFFSET,
						REG_PLT_NO_OFFSET + REG_PLT_NO_LENGTH)));

			// PltExpYr
			// defect 9864
			//	Save REG_EXP_YR_OFFSET and PLT_EXP_YR_OFFSET
			laMfSpclPltRegisData.setPltExpYr(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								PLT_EXP_YR_OFFSET,
								PLT_EXP_YR_OFFSET
									+ PLT_EXP_YR_LENGTH)))
					.intValue());
			laMfSpclPltRegisData.setPltExpYr(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								PLT_EXP_YR_OFFSET,
								PLT_EXP_YR_OFFSET
									+ PLT_EXP_YR_LENGTH)))
					.intValue());
			// end defect 9864

			// MFG Plate No
			laMfSpclPltRegisData.setMfgPltNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						MFG_PLT_NO_OFFSET,
						MFG_PLT_NO_OFFSET + MFG_PLT_NO_LENGTH)));

			// PltOwnrId
			laOwnerData.setOwnrId(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_ID_OFFSET,
						PLT_OWNR_ID_OFFSET + PLT_OWNR_ID_LENGTH)));

			// defect 10112 
			// PltOwnrName1
			laOwnerData.setName1(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_NAME1_OFFSET,
						PLT_OWNR_NAME1_OFFSET
							+ PLT_OWNR_NAME1_LENGTH)));

			// PltOwnrName2
			laOwnerData.setName2(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_NAME2_OFFSET,
						PLT_OWNR_NAME2_OFFSET
							+ PLT_OWNR_NAME2_LENGTH)));
			// end defect 10112 

			// PltOwnrSt1
			laAddrData.setSt1(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_ST1_OFFSET,
						PLT_OWNR_ST1_OFFSET + PLT_OWNR_ST1_LENGTH)));

			// PltOwnrSt2
			laAddrData.setSt2(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_ST2_OFFSET,
						PLT_OWNR_ST2_OFFSET + PLT_OWNR_ST2_LENGTH)));

			// PltOwnrCity
			laAddrData.setCity(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_CITY_OFFSET,
						PLT_OWNR_CITY_OFFSET + PLT_OWNR_CITY_LENGTH)));

			// PltOwnrState
			laAddrData.setState(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_STATE_OFFSET,
						PLT_OWNR_STATE_OFFSET
							+ PLT_OWNR_STATE_LENGTH)));

			// PltOwnrZipCd
			laAddrData.setZpcd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_ZIP_CD_OFFSET,
						PLT_OWNR_ZIP_CD_OFFSET
							+ PLT_OWNR_ZIP_CD_LENGTH)));

			// PltOwnrZipCdP4
			laAddrData.setZpcdp4(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_ZIP_CD_P4_OFFSET,
						PLT_OWNR_ZIP_CD_P4_OFFSET
							+ PLT_OWNR_ZIP_CD_P4_LENGTH)));

			// PltOwnrCntry
			laAddrData.setCntry(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_CNTRY_OFFSET,
						PLT_OWNR_CNTRY_OFFSET
							+ PLT_OWNR_CNTRY_LENGTH)));

			// defect 10112 
			laOwnerData.setAddressData(laAddrData);
			// end defect 10112 

			laMfSpclPltRegisData.setOwnrData(laOwnerData);

			// PltOwnrPhone
			// Modify from int to String 
			laMfSpclPltRegisData.setPltOwnrPhoneNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_PHONE_OFFSET,
						PLT_OWNR_PHONE_OFFSET
							+ PLT_OWNR_PHONE_LENGTH)));

			// PltOwnrEMail
			laMfSpclPltRegisData.setPltOwnrEMail(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_EMAIL_OFFSET,
						PLT_OWNR_EMAIL_OFFSET
							+ PLT_OWNR_EMAIL_LENGTH)));

			// PltOwnrOfcCd
			laMfSpclPltRegisData.setPltOwnrOfcCd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_OFC_CD_OFFSET,
						PLT_OWNR_OFC_CD_OFFSET
							+ PLT_OWNR_OFC_CD_LENGTH)));

			// PltOwnrDist
			laMfSpclPltRegisData.setPltOwnrDist(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_DIST_OFFSET,
						PLT_OWNR_DIST_OFFSET + PLT_OWNR_DIST_LENGTH)));

			// TransCd
			laMfSpclPltRegisData.setTransCd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						TRANS_CD_OFFSET,
						TRANS_CD_OFFSET + TRANS_CD_LENGTH)));

			// PltOwnrPhone
			laMfSpclPltRegisData.setRegClassCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								REG_CLASS_CD_OFFSET,
								REG_CLASS_CD_OFFSET
									+ REG_CLASS_CD_LENGTH)))
					.intValue());

			// PltExpMo
			// defect 9864
			//	Save REG_EXP_YR_OFFSET and PLT_EXP_MO_OFFSET
			laMfSpclPltRegisData.setPltExpMo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								PLT_EXP_MO_OFFSET,
								PLT_EXP_MO_OFFSET
									+ PLT_EXP_MO_LENGTH)))
					.intValue());
			laMfSpclPltRegisData.setPltExpMo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								PLT_EXP_MO_OFFSET,
								PLT_EXP_MO_OFFSET
									+ PLT_EXP_MO_LENGTH)))
					.intValue());
			// end defect 9864

			// PLTAPPLDATE (RegIssueDate)
			laMfSpclPltRegisData.setPltApplDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								PLT_APPL_DATE_OFFSET,
								PLT_APPL_DATE_OFFSET
									+ PLT_APPL_DATE_LENGTH)))
					.intValue());

			// RegPltCd
			laMfSpclPltRegisData.setRegPltCd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						REG_PLT_CD_OFFSET,
						REG_PLT_CD_OFFSET + REG_PLT_CD_LENGTH)));

			// PltBirthDate
			laMfSpclPltRegisData.setPltBirthDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								PLT_BIRTH_DATE_OFFSET,
								PLT_BIRTH_DATE_OFFSET
									+ PLT_BIRTH_DATE_LENGTH)))
					.intValue());

			// ResComptCntyNo
			laMfSpclPltRegisData.setResComptCntyNo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								RES_COMPT_CNTY_NO_OFFSET,
								RES_COMPT_CNTY_NO_OFFSET
									+ RES_COMPT_CNTY_NO_LENGTH)))
					.intValue());

			// AddlSetIndi
			laMfSpclPltRegisData.setAddlSetIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								ADDL_SET_INDI_OFFSET,
								ADDL_SET_INDI_OFFSET
									+ ADDL_SET_INDI_LENGTH)))
					.intValue());

			// RegPltCd
			laMfSpclPltRegisData.setRegPltCd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						REG_PLT_CD_OFFSET,
						REG_PLT_CD_OFFSET + REG_PLT_CD_LENGTH)));

			// MFGStatusCd
			laMfSpclPltRegisData.setMFGStatusCd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						MFG_STATUS_CD_OFFSET,
						MFG_STATUS_CD_OFFSET + MFG_STATUS_CD_LENGTH)));

			// OrgNo
			laMfSpclPltRegisData.setOrgNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						ORG_NO_OFFSET,
						ORG_NO_OFFSET + ORG_NO_LENGTH)));

			// SAuditTrailTransId
			laMfSpclPltRegisData.setSAuditTrailTransId(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						SAUDIT_TRAIL_TRANS_ID_OFFSET,
						SAUDIT_TRAIL_TRANS_ID_OFFSET
							+ SAUDIT_TRAIL_TRANS_ID_LENGTH)));

			// DelIndi
			laMfSpclPltRegisData.setDelIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								DEL_INDI_OFFSET,
								DEL_INDI_OFFSET + DEL_INDI_LENGTH)))
					.intValue());

			// defect 10246 
			//			// PrivacyOptCd
			//			laMfSpclPltRegisData.setPrivacyOptCd(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							lsMfSpclPltRegisResponse.substring(
			//								PRIVACY_OPT_CD_OFFSET,
			//								PRIVACY_OPT_CD_OFFSET
			//									+ PRIVACY_OPT_CD_LENGTH)))
			//					.intValue());
			// end defect 10246 

			// defect 10378
			// New attribute for version U
			// RESRVEFFDATE 
			laMfSpclPltRegisData.setResrvEffDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								RESRV_EFF_DATE_OFFSET,
								RESRV_EFF_DATE_OFFSET
									+ RESRV_EFF_DATE_LENGTH)))
					.intValue());

			// RESRVREASNCD
			laMfSpclPltRegisData.setResrvReasnCd(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						RESRV_REASN_CD_OFFSET,
						RESRV_REASN_CD_OFFSET
							+ RESRV_REASN_CD_LENGTH)));
			// end defect 10378

			// MFGDate
			laMfSpclPltRegisData.setMFGDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									MFG_DATE_OFFSET,
									MFG_DATE_OFFSET
										+ MFG_DATE_LENGTH))))
					.intValue());

			// PltSetNo
			laMfSpclPltRegisData.setPltSetNo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									PLT_SET_NO_OFFSET,
									PLT_SET_NO_OFFSET
										+ PLT_SET_NO_LENGTH))))
					.intValue());

			// SpclDocNo
			laMfSpclPltRegisData.setSpclDocNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						SPCL_DOCNO_OFFSET,
						SPCL_DOCNO_OFFSET + SPCL_DOCNO_LENGTH)));

			// SpclFee
			StringBuffer laSPCLFeeBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						lsMfSpclPltRegisResponse.substring(
							SPCL_FEE_OFFSET,
							SPCL_FEE_OFFSET + SPCL_FEE_LENGTH)));

			if (laSPCLFeeBuffer.charAt(0) != '-')
			{
				laSPCLFeeBuffer.insert(
					SPCL_FEE_LENGTH - SPCL_FEE_DECIMAL,
					'.');
			}
			else
			{
				laSPCLFeeBuffer.insert(
					SPCL_FEE_LENGTH - SPCL_FEE_DECIMAL + 1,
					'.');
			}

			Dollar laSPCLFee = new Dollar(laSPCLFeeBuffer.toString());
			laMfSpclPltRegisData.setSpclFee(laSPCLFee);

			// defect 10378
			// New attribute for version U
			// MktngAllowdIndi
			laMfSpclPltRegisData.setMktngAllowdIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							lsMfSpclPltRegisResponse.substring(
								MKTNG_ALLOWD_INDI_OFFSET,
								MKTNG_ALLOWD_INDI_OFFSET
									+ MKTNG_ALLOWD_INDI_LENGTH)))
					.intValue());
			// end defect 10378

			// ISAIndi
			laMfSpclPltRegisData.setISAIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									ISA_INDI_OFFSET,
									ISA_INDI_OFFSET
										+ ISA_INDI_LENGTH))))
					.intValue());

			// SpclRemks
			laMfSpclPltRegisData.setSpclRemks(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						SPCL_REMKS_OFFSET,
						SPCL_REMKS_OFFSET + SPCL_REMKS_LENGTH)));

			// TransEmpId
			laMfSpclPltRegisData.setTransEmpId(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						TRANS_EMP_ID_OFFSET,
						TRANS_EMP_ID_OFFSET + TRANS_EMP_ID_LENGTH)));

			// defect 10492 
			// ElectionPndngIndi 
			laMfSpclPltRegisData.setElectionPndngIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									ELECTIONPNDNGINDI_OFFSET,
									ELECTIONPNDNGINDI_OFFSET
										+ ELECTIONPNDNGINDI_LENGTH))))
					.intValue());
			// end defect 10492 

			// defect 10378
			// defect 9557
			// PltOwnrDlrGDN
			laMfSpclPltRegisData.setPltOwnrDlrGDN(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						PLT_OWNR_DLR_GDN_OFFSET,
						PLT_OWNR_DLR_GDN_OFFSET
							+ PLT_OWNR_DLR_GDN_LENGTH)));
			// end defect 10378

			// PltRmvCd 
			laMfSpclPltRegisData.setPltRmvCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									PLTRMVCD_OFFSET,
									PLTRMVCD_OFFSET
										+ PLTRMVCD_LENGTH))))
					.intValue());

			// V21PltId 
			laMfSpclPltRegisData.setV21PltId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									V21PLTID_OFFSET,
									V21PLTID_OFFSET
										+ V21PLTID_LENGTH))))
					.intValue());

			// end 9557

			// The following fields were added for MyPlatesII
			// PLTVALIDITYTERM 
			laMfSpclPltRegisData.setPltValidityTerm(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									PLT_VALIDITY_TERM_OFFSET,
									PLT_VALIDITY_TERM_OFFSET
										+ PLT_VALIDITY_TERM_LENGTH))))
					.intValue());

			// defect 10097
			laMfSpclPltRegisData.setOrigPltExpMo(
				laMfSpclPltRegisData.getPltExpMo());
			laMfSpclPltRegisData.setOrigPltExpYr(
				laMfSpclPltRegisData.getPltExpYr());
			// end defect 10097

			// defect 10378
			// New attribute for version U
			// FinDocNo
			laMfSpclPltRegisData.setFINDocNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						FIN_DOC_NO_OFFSET,
						FIN_DOC_NO_OFFSET + FIN_DOC_NO_LENGTH)));

			// VendorTransDate
			laMfSpclPltRegisData.setVendorTransDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									VENDOR_TRANS_DATE_OFFSET,
									VENDOR_TRANS_DATE_OFFSET
										+ VENDOR_TRANS_DATE_LENGTH))))
					.intValue());
			// IntrntTraceNo
			laMfSpclPltRegisData.setItrntTraceNo(
				laMFAccess.trimMfString(
					lsMfSpclPltRegisResponse.substring(
						ITRNT_TRACE_NO_OFFSET,
						ITRNT_TRACE_NO_OFFSET
							+ ITRNT_TRACE_NO_LENGTH)));

			// AuctnPltIndi
			laMfSpclPltRegisData.setAuctnPltIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							laMFAccess.trimMfString(
								lsMfSpclPltRegisResponse.substring(
									AUCTN_PLT_INDI_OFFSET,
									AUCTN_PLT_INDI_OFFSET
										+ AUCTN_PLT_INDI_LENGTH))))
					.intValue());

			// AuctnPdAmt
			StringBuffer laAUCTNPDAmtBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						lsMfSpclPltRegisResponse.substring(
							AUCTN_PD_AMT_OFFSET,
							AUCTN_PD_AMT_OFFSET
								+ AUCTN_PD_AMT_LENGTH)));

			if (laAUCTNPDAmtBuffer.charAt(0) != '-')
			{
				laAUCTNPDAmtBuffer.insert(
					AUCTN_PD_AMT_LENGTH - AUCTN_PD_AMT_DECIMAL,
					'.');
			}
			else
			{
				laAUCTNPDAmtBuffer.insert(
					AUCTN_PD_AMT_LENGTH - AUCTN_PD_AMT_DECIMAL + 1,
					'.');
			}

			Dollar laAUCTNPDAmt =
				new Dollar(laAUCTNPDAmtBuffer.toString());
			laMfSpclPltRegisData.setAuctnPdAmt(laAUCTNPDAmt);

			// end defect 10378
		}
		return laMfSpclPltRegisData;
	}
}
