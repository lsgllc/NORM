package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaOwnerV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/17/2006	Add JavaDoc
 * 							defect 6701, Ver Exempts
 * J Rue		10/20/2006	Update comments
 * 							modify setOwnerDataFromMF
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Move process from MfAccess
 * 							defect 8983 Ver Exempts
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Update JavaDoc
 * 							defect 9086 Ver Special Plates
 * J Rue		04/03/2008	New attributes for V21 MF version V
 * 							modify setOwnerDataFromMF()
 * 							defect 9557 Ver Defect_POS_A
 * K Harrell	04/14/2008	Adjust offsets for Cancel Plates DocNo
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A 
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setOwnerDataFromMF()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		12/30/2008	Adjust offsets for OrgNo, RegPltAge, 
 * 							DISSOCIATECD.  
 * 							modify all class variablesdefect 9655 Ver Defect_POS_D
 * 							defect 9655 Special Plates
 * J Rue		02/25/2009	Adjust offsets for MFInterfaceVersionCode V
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new OwnerData 
 * 							modify setOwnerDataFromMF()
 * 							defect 10112 Ver Defect_POS_F
 * J Rue		10/06/2009	Adjust offsets for RCCPI version T
 * 							modify all class variables
 * 							defect 10244 Ver Defect_POS_G
 * K Harrell	10/14/2009	No longer assign PrivacyOptCd
 * 							delete PRIVACY_OPT_CD_OFFSET,
 *							 PRIVACY_OPT_CD_LENGTH  
 * 							modify setOwnerDataFromMfOwner()
 * 							defect 10246 Ver Defect_POS_G     
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * K Harrell 	06/22/2010  Copy version U to version V for MF
 *        					MF version V. Modified All.
 *        					defect 10492 Ver 6.5.0 
 * M Reyes		10/05/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver 6.6.0 
 * M Reyes		12/31/2010	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ves 6.7.0
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into OwnerData. 
 * Used by the server side business layer. 
 *
 * @version	6.9.0 	 		11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:10:32
 */
public class MfbaOwnerV
{
	//define all lengths and offsets (from doc 660)
	// Note: offset is -1 minus from CICS file
	// TITLE-REC-OUT
	private final static int OWNR_ID_OFFSET = 1047;
	private final static int OWNR_ID_LENGTH = 9;
	private final static int OWNR_TTL_NAME1_OFFSET = 1056;
	private final static int OWNR_TTL_NAME1_LENGTH = 30;
	private final static int OWNR_TTL_NAME2_OFFSET = 1086;
	private final static int OWNR_TTL_NAME2_LENGTH = 30;
	private final static int OWNR_ST1_OFFSET = 1116;
	private final static int OWNR_ST1_LENGTH = 30;
	private final static int OWNR_ST2_OFFSET = 1146;
	private final static int OWNR_ST2_LENGTH = 30;
	private final static int OWNR_CITY_OFFSET = 1176;
	private final static int OWNR_CITY_LENGTH = 19;
	private final static int OWNR_STATE_OFFSET = 1195;
	private final static int OWNR_STATE_LENGTH = 2;
	private final static int OWNR_ZPCD_OFFSET = 1197;
	private final static int OWNR_ZPCD_LENGTH = 5;
	private final static int OWNR_ZPCDP4_OFFSET = 1202;
	private final static int OWNR_ZPCDP4_LENGTH = 4;
	private final static int OWNR_CNTRY_OFFSET = 1206;
	private final static int OWNR_CNTRY_LENGTH = 4;
	// defect 10246
	// private final static int PRIVACY_OPT_CD_OFFSET = 1157;
	// private final static int PRIVACY_OPT_CD_LENGTH = 1;
	// end defect 10246 

	/**
	 * 
	 */
	public MfbaOwnerV()
	{
		super();
	}

	public static void main(String[] args)
	{
	}
	/**
	 * Sets and returns Owner data from the mainframe response. 
	 * 
	 * @param asMfResponse String
	 * @return OwnerData
	 */
	public OwnerData setOwnerDataFromMF(String asMfResponse)
	{
		//create the return object
		OwnerData laOwnerData = new OwnerData();
		String lsMfTtlRegResponse = asMfResponse;
		MfAccess laMFAccess = new MfAccess();

		if (!(asMfResponse.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (lsMfTtlRegResponse != null))
		{
			laOwnerData.setOwnrId(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_ID_OFFSET,
						OWNR_ID_OFFSET + OWNR_ID_LENGTH)));

			// defect 10112 
			laOwnerData.setName1(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_TTL_NAME1_OFFSET,
						OWNR_TTL_NAME1_OFFSET
							+ OWNR_TTL_NAME1_LENGTH)));

			laOwnerData.setName2(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_TTL_NAME2_OFFSET,
						OWNR_TTL_NAME2_OFFSET
							+ OWNR_TTL_NAME2_LENGTH)));
			// end defect 10112 

			// defect 10246 
			//			laOwnerData.setPrivacyOptCd(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							lsMfTtlRegResponse.substring(
			//								PRIVACY_OPT_CD_OFFSET,
			//								PRIVACY_OPT_CD_OFFSET
			//									+ PRIVACY_OPT_CD_LENGTH)))
			//					.intValue());
			// end defect 10246 

			//set the address
			AddressData laAddressData = new AddressData();
			laAddressData.setCity(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_CITY_OFFSET,
						OWNR_CITY_OFFSET + OWNR_CITY_LENGTH)));

			laAddressData.setCntry(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_CNTRY_OFFSET,
						OWNR_CNTRY_OFFSET + OWNR_CNTRY_LENGTH)));

			laAddressData.setSt1(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_ST1_OFFSET,
						OWNR_ST1_OFFSET + OWNR_ST1_LENGTH)));

			laAddressData.setSt2(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_ST2_OFFSET,
						OWNR_ST2_OFFSET + OWNR_ST2_LENGTH)));

			laAddressData.setState(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_STATE_OFFSET,
						OWNR_STATE_OFFSET + OWNR_STATE_LENGTH)));

			laAddressData.setZpcd(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_ZPCD_OFFSET,
						OWNR_ZPCD_OFFSET + OWNR_ZPCD_LENGTH)));

			laAddressData.setZpcdp4(
				laMFAccess.trimMfString(
					lsMfTtlRegResponse.substring(
						OWNR_ZPCDP4_OFFSET,
						OWNR_ZPCDP4_OFFSET + OWNR_ZPCDP4_LENGTH)));

			laOwnerData.setAddressData(laAddressData);
		} // end of if
		return laOwnerData;
	}
}
