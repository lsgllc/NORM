package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaSpecialOwnerV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/02/2006	Update offset to new format
 * 							defect 6701 Ver 5.3.0
 * J Rue		10/19/2006	Move process from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue		10/19/2006	Add process to set HOOPSREGPLTNO
 * 							add setHoopsRegPltNoSpclPltCancelPlt()
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/27/2006	Redo offsets to match MF buffer string
 * 							delete setHoopsRegPltNoSpclPltCancelPlt()
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		02/01/2007	Clean up JavaDoc
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 9086 ver Special Plate
 * J Rue		02/01/2007	Update JavaDoc
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/03/2008	New attributes for V21 MF version V
 * 							defect 9557 Defect_POS_A
 * K Harrell	04/25/2008	Updated for Cancelled Plate Doc No 
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 9557 Defect_POS_A
 * J Rue		04/27/2007	Delect unused variables
 * 							defect 8984 Ver Special Plates
 * J Rue		05/06/2008	Order variables by offset position
 * 							defect 8984 Ver 3_AMIGOS_PH_B
 * J Rue		05/06/2008	Set Ver to Defect_POS_A
 * 							defect 8984 Ver Defect_POS_A
 * J Rue		06/12/2008	MFAccess, SendTrans to handle new V21 columns
 * 							modify all class variables,
 * 								setVehicleInquiryDataFromSpecialOwnerResponse(), 
 * 							defect 9557 Ver 5.6.0
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables,
 * 								setVehicleInquiryDataFromSpecialOwnerResponse(), 
 * 							defect 9655 Ver Defect_POS_D
 * K Harrell	07/03/2009	Implement new Data Structures
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 10112 Ver Defect_POS_F 
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * K Harrell 	06/22/2010  Copy version U to version V for MF
 *        					MF version V. Modified All.
 *        					defect 10492 Ver 6.5.0
 * M Reyes		10/06/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		01/03/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0  
  * B Woodson	01/20/2012	Copy version V to T for MF Version T 
 * 							deleted default constructor
 * 							defect 11251 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into SpecialOwner Data. 
 * Used by the server side business layer - Exempts/TERPS. 
 *
 * @version	6.10.0 			01/20/2012
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:10:32
 */
public class MfbaSpecialOwnerT
{
	MfAccess laMfAccess = new MfAccess();

	//define the offsets and lengths
	// Special Rgis Offsets
	//	RegPltId
	private final static int SPCL_REG_PLT_NO_OFFSET = 0;
	private final static int SPCL_REG_PLT_NO_LENGTH = 7;
	private final static int SPCL_REG_EXP_YR_OFFSET = 7;
	private final static int SPCL_REG_EXP_YR_LENGTH = 4;
	//	OWNR
	private final static int OWNER_ID_OFFSET = 11;
	private final static int OWNER_ID_LENGTH = 9;
	private final static int OWNR_TTL_NAME1_OFFSET = 20;
	private final static int OWNR_TTL_NAME1_LENGTH = 30;
	private final static int OWNR_TTL_NAME2_OFFSET = 50;
	private final static int OWNR_TTL_NAME2_LENGTH = 30;
	//	OWNRADDR
	private final static int OWNR_ST1_OFFSET = 80;
	private final static int OWNR_ST1_LENGTH = 30;
	private final static int OWNR_ST2_OFFSET = 110;
	private final static int OWNR_ST2_LENGTH = 30;
	private final static int OWNR_CITY_OFFSET = 140;
	private final static int OWNR_CITY_LENGTH = 19;
	private final static int OWNR_STATE_OFFSET = 159;
	private final static int OWNR_STATE_LENGTH = 2;
	private final static int OWNR_ZPCD_OFFSET = 161;
	private final static int OWNR_ZPCD_LENGTH = 5;
	private final static int OWNR_ZPCDP4_OFFSET = 166;
	private final static int OWNR_ZPCDP4_LENGTH = 4;
	private final static int OWNR_CNTRY_OFFSET = 170;
	private final static int OWNR_CNTRY_LENGTH = 4;
	private final static int REG_CLASS_CD_OFFSET = 181;
	private final static int REG_CLASS_CD_LENGTH = 3;
	private final static int REG_EFF_DATE_OFFSET = 184;
	private final static int REG_EFF_DATE_LENGTH = 8;
	private final static int REG_EXP_MO_OFFSET = 192;
	private final static int REG_EXP_MO_LENGTH = 2;
	private final static int REG_ISSUE_DATE_OFFSET = 194;
	private final static int REG_ISSUE_DATE_LENGTH = 8;
	private final static int SPCL_REG_PLT_CD_OFFSET = 202;
	private final static int SPCL_REG_PLT_CD_LENGTH = 8;
	private final static int SPCL_REG_HOOPSREGPLTNO_OFFSET = 210;
	private final static int SPCL_REG_HOOPSREGPLTNO_LENGTH = 7;
	private final static int PLTBIRTHDATE_OFFSET = 217;
	private final static int PLTBIRTHDATE_LENGTH = 6;

	// Cancel Plate offsets
	private final static int CANC_PLT_REG_PLT_NO_OFFSET = 223;
	private final static int CANC_PLT_REG_PLT_NO_LENGTH = 7;
	private final static int CANC_REG_EXP_YR_OFFSET = 230;
	private final static int CANC_REG_EXP_YR_LENGTH = 4;
	private final static int CANC_PLT_DATE_OFFSET = 234;
	private final static int CANC_PLT_DATE_LENGTH = 8;
	private final static int CANC_REG_EXP_MO_OFFSET = 242;
	private final static int CANC_REG_EXP_MO_LENGTH = 2;
	private final static int CANC_PLT_VIN_OFFSET = 244;
	private final static int CANC_PLT_VIN_LENGTH = 22;
	private final static int CANC_PLT_REG_PLT_CD_OFFSET = 266;
	private final static int CANC_PLT_REG_PLT_CD_LENGTH = 8;
	private final static int CANC_PLT_HOOPSREGPLTNO_OFFSET = 274;
	private final static int CANC_PLT_HOOPSREGPLTNO_LENGTH = 7;
	// defect 9557 
	private final static int CANC_PLT_DOCNO_OFFSET = 281;
	private final static int CANC_PLT_DOCNO_LENGTH = 17;
	private final static int CANC_PLT_INDI_OFFSET = 298;
	private final static int CANC_PLT_INDI_LENGTH = 1;
	// end defect 9557 

	public static void main(String[] args)
	{
	}
	/**
	 * Sets and returns the Special Owner data from the mainframe 
	 * spl. owner response, CICS version T. 
	 * 
	 * @param VehicleInquiryData String buffer from MF
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData setVehicleInquiryDataFromSpecialOwnerResponse(
		String asMfSpecialOwnerResponse)
	{
		//create the return object
		MFVehicleData laMFVehicleData = new MFVehicleData();
		VehicleData laVehicleData = new VehicleData();
		RegistrationData laRegistrationData = new RegistrationData();
		OwnerData laOwnerData = new OwnerData();
		TitleData laTitleData = new TitleData();

		//create the salvage data container
		SalvageData laSalvageData = new SalvageData();
		Vector laSalvageContainer = new Vector();
		laSalvageContainer.addElement(laSalvageData);
		AddressData laAddressData = new AddressData();
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();

		// Move date to appropriate data classes if buffer string is
		//	populated from MF.
		if (asMfSpecialOwnerResponse != null
			&& (!(asMfSpecialOwnerResponse
				.equals(CommonConstant.STR_SPACE_EMPTY))))
		{
			// Get CancelPltIndi
			int liCancPltIndi =
				Integer
					.valueOf(
						laMfAccess.getStringFromZonedDecimal(
							asMfSpecialOwnerResponse.substring(
								CANC_PLT_INDI_OFFSET,
								CANC_PLT_INDI_OFFSET
									+ CANC_PLT_INDI_LENGTH)))
					.intValue();

			// If not Cancel Plate (Special Regis) do the following:
			if (liCancPltIndi == 0)
			{
				/**
				 * Set Spec-Regis
				 */
				laOwnerData.setOwnrId(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNER_ID_OFFSET,
							OWNER_ID_OFFSET + OWNER_ID_LENGTH)));

				// defect 10112 
				laOwnerData.setName1(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_TTL_NAME1_OFFSET,
							OWNR_TTL_NAME1_OFFSET
								+ OWNR_TTL_NAME1_LENGTH)));

				laOwnerData.setName2(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_TTL_NAME2_OFFSET,
							OWNR_TTL_NAME2_OFFSET
								+ OWNR_TTL_NAME2_LENGTH)));
				// end defect 10112 

				/**
				 * Set Address Data
				 */
				laAddressData.setCity(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_CITY_OFFSET,
							OWNR_CITY_OFFSET + OWNR_CITY_LENGTH)));

				laAddressData.setCntry(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_CNTRY_OFFSET,
							OWNR_CNTRY_OFFSET + OWNR_CNTRY_LENGTH)));

				laAddressData.setSt1(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_ST1_OFFSET,
							OWNR_ST1_OFFSET + OWNR_ST1_LENGTH)));

				laAddressData.setSt2(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_ST2_OFFSET,
							OWNR_ST2_OFFSET + OWNR_ST2_LENGTH)));

				laAddressData.setState(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_STATE_OFFSET,
							OWNR_STATE_OFFSET + OWNR_STATE_LENGTH)));

				laAddressData.setZpcd(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_ZPCD_OFFSET,
							OWNR_ZPCD_OFFSET + OWNR_ZPCD_LENGTH)));

				laAddressData.setZpcdp4(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							OWNR_ZPCDP4_OFFSET,
							OWNR_ZPCDP4_OFFSET + OWNR_ZPCDP4_LENGTH)));

				// defect 10112 
				laOwnerData.setAddressData(laAddressData);
				// end defect 10112 

				/**
				 * Set Registration Data
				 */
				laRegistrationData.setPltBirthDate(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									PLTBIRTHDATE_OFFSET,
									PLTBIRTHDATE_OFFSET
										+ PLTBIRTHDATE_LENGTH)))
						.intValue());

				//no data object has a trans cd
				laRegistrationData.setRegClassCd(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									REG_CLASS_CD_OFFSET,
									REG_CLASS_CD_OFFSET
										+ REG_CLASS_CD_LENGTH)))
						.intValue());

				laRegistrationData.setRegEffDt(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									REG_EFF_DATE_OFFSET,
									REG_EFF_DATE_OFFSET
										+ REG_EFF_DATE_LENGTH)))
						.intValue());

				laRegistrationData.setRegExpMo(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									REG_EXP_MO_OFFSET,
									REG_EXP_MO_OFFSET
										+ REG_EXP_MO_LENGTH)))
						.intValue());

				laRegistrationData.setRegExpYr(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									SPCL_REG_EXP_YR_OFFSET,
									SPCL_REG_EXP_YR_OFFSET
										+ SPCL_REG_EXP_YR_LENGTH)))
						.intValue());

				laRegistrationData.setRegIssueDt(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									REG_ISSUE_DATE_OFFSET,
									REG_ISSUE_DATE_OFFSET
										+ REG_ISSUE_DATE_LENGTH)))
						.intValue());

				laRegistrationData.setRegPltCd(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							SPCL_REG_PLT_CD_OFFSET,
							SPCL_REG_PLT_CD_OFFSET
								+ SPCL_REG_PLT_CD_LENGTH)));

				laRegistrationData.setRegPltNo(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							SPCL_REG_PLT_NO_OFFSET,
							SPCL_REG_PLT_NO_OFFSET
								+ SPCL_REG_PLT_NO_LENGTH)));

				laRegistrationData.setHoopsRegPltNo(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							SPCL_REG_HOOPSREGPLTNO_OFFSET,
							SPCL_REG_HOOPSREGPLTNO_OFFSET
								+ SPCL_REG_HOOPSREGPLTNO_LENGTH)));
			}
			/**
			 * Set Cancel Plate data
			 */
			else
			{
				laRegistrationData.setCanclPltDt(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									CANC_PLT_DATE_OFFSET,
									CANC_PLT_DATE_OFFSET
										+ CANC_PLT_DATE_LENGTH)))
						.intValue());

				laRegistrationData.setCancRegExpMo(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									CANC_REG_EXP_MO_OFFSET,
									CANC_REG_EXP_MO_OFFSET
										+ CANC_REG_EXP_MO_LENGTH)))
						.intValue());

				laRegistrationData.setRegExpYr(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									CANC_REG_EXP_YR_OFFSET,
									CANC_REG_EXP_YR_OFFSET
										+ CANC_REG_EXP_YR_LENGTH)))
						.intValue());

				// defect 9557 
				laRegistrationData.setCancPltDocNo(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							CANC_PLT_DOCNO_OFFSET,
							CANC_PLT_DOCNO_OFFSET
								+ CANC_PLT_DOCNO_LENGTH)));
				// end defect 9557				

				laRegistrationData.setCancPltIndi(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfSpecialOwnerResponse.substring(
									CANC_PLT_INDI_OFFSET,
									CANC_PLT_INDI_OFFSET
										+ CANC_PLT_INDI_LENGTH)))
						.intValue());

				laRegistrationData.setRegPltNo(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							CANC_PLT_REG_PLT_NO_OFFSET,
							CANC_PLT_REG_PLT_NO_OFFSET
								+ CANC_PLT_REG_PLT_NO_LENGTH)));

				laRegistrationData.setRegPltCd(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							CANC_PLT_REG_PLT_CD_OFFSET,
							CANC_PLT_REG_PLT_CD_OFFSET
								+ CANC_PLT_REG_PLT_CD_LENGTH)));

				laRegistrationData.setHoopsRegPltNo(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							CANC_PLT_HOOPSREGPLTNO_OFFSET,
							CANC_PLT_HOOPSREGPLTNO_OFFSET
								+ CANC_PLT_HOOPSREGPLTNO_LENGTH)));

				laRegistrationData.setCancPltVin(
					laMfAccess.trimMfString(
						asMfSpecialOwnerResponse.substring(
							CANC_PLT_VIN_OFFSET,
							CANC_PLT_VIN_OFFSET
								+ CANC_PLT_VIN_LENGTH)));
			}
		}

		laMFVehicleData.setOwnerData(laOwnerData);
		laMFVehicleData.setRegData(laRegistrationData);
		laMFVehicleData.setVctSalvage(laSalvageContainer);
		laMFVehicleData.setTitleData(laTitleData);
		laMFVehicleData.setVehicleData(laVehicleData);
		laVehicleInquiryData.setMfVehicleData(laMFVehicleData);

		return laVehicleInquiryData;
	}
}
