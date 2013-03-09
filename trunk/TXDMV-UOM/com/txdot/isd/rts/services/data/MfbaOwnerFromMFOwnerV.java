package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.server.dataaccess.APPCHeader;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaOwnerFromMFOwnerV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/27/2007	Add offsets and code
 * 							defect 8983 Ver Special Plates
 * J Rue		04/03/2008	Copy version U to version V
 * 							modify all
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setOwnerDataFromMfOwner()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		02/25/2009	Arrange class variables from lowest offset
 * 							to highest
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new OwnerData 
 * 							modify setOwnerDataFromMfOwner()
 * 							defect 10112 Ver Defect_POS_F
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
 * 							defect 10595 Ver POS_660
 * M Reyes		12/31/2010	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into Owner Data on the TxDOT 
 * mainframe.   
 * Used by the server side business layer - Special Plates.
 *
 * @version	6.9.0 			11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		04/26/2007 18:30:32
 */
public class MfbaOwnerFromMFOwnerV
{
	MfAccess laMfAccess = new MfAccess();

	//define all lengths and offsets (from doc 660)
	final int OWNR_ID_OFFSET = 0;
	final int OWNR_ID_LENGTH = 9;
	final int OWNR_TTL_NAME1_OFFSET = 9;
	final int OWNR_TTL_NAME1_LENGTH = 30;
	final int OWNR_TTL_NAME2_OFFSET = 39;
	final int OWNR_TTL_NAME2_LENGTH = 30;
	final int OWNR_ST1_OFFSET = 69;
	final int OWNR_ST1_LENGTH = 30;
	final int OWNR_ST2_OFFSET = 99;
	final int OWNR_ST2_LENGTH = 30;
	final int OWNR_CITY_OFFSET = 129;
	final int OWNR_CITY_LENGTH = 19;
	final int OWNR_STATE_OFFSET = 148;
	final int OWNR_STATE_LENGTH = 2;
	final int OWNR_ZPCD_OFFSET = 150;
	final int OWNR_ZPCD_LENGTH = 5;
	final int OWNR_ZPCDP4_OFFSET = 155;
	final int OWNR_ZPCDP4_LENGTH = 4;
	final int OWNR_CNTRY_OFFSET = 159;
	final int OWNR_CNTRY_LENGTH = 4;
	// defect 10246 
	//	final int PRIVACY_OPT_CD_OFFSET = 163;
	//	final int PRIVACY_OPT_CD_LENGTH = 1;
	// end defect 10246 

	/**
	 * 
	 */
	public MfbaOwnerFromMFOwnerV()
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
	public OwnerData setOwnerDataFromMfOwner(String asMfResponse)
	{
		//create the return object
		OwnerData laOwnerData = new OwnerData();
		APPCHeader laAppcHeader = new APPCHeader();

		//get the length of output from MF
		//	Get output length from APPcHeader
		//	Set number of records
		int liOutputLength =
			laAppcHeader.rtnMfOutputLength(asMfResponse);

		if (liOutputLength == 164)
		{
			//Strip off the header in response
			String lsMfOwnerResponse =
				asMfResponse.substring(
					laAppcHeader.getAPPCHeaderRecord().length);

			if (!(lsMfOwnerResponse
				.equals(CommonConstant.STR_SPACE_EMPTY))
				&& (lsMfOwnerResponse != null))
			{
				laOwnerData.setOwnrId(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_ID_OFFSET,
							OWNR_ID_OFFSET + OWNR_ID_LENGTH)));

				// defect 10112 
				laOwnerData.setName1(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_TTL_NAME1_OFFSET,
							OWNR_TTL_NAME1_OFFSET
								+ OWNR_TTL_NAME1_LENGTH)));
				laOwnerData.setName2(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_TTL_NAME2_OFFSET,
							OWNR_TTL_NAME2_OFFSET
								+ OWNR_TTL_NAME2_LENGTH)));
				// end defect 10112

				// defect 10246 
				//				laOwnerData.setPrivacyOptCd(
				//					Integer
				//						.valueOf(
				//							laMfAccess.getStringFromZonedDecimal(
				//								lsMfOwnerResponse.substring(
				//									PRIVACY_OPT_CD_OFFSET,
				//									PRIVACY_OPT_CD_OFFSET
				//										+ PRIVACY_OPT_CD_LENGTH)))
				//						.intValue());
				// end defect 10246 

				//set the address
				AddressData laAddressData = new AddressData();
				laAddressData.setCity(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_CITY_OFFSET,
							OWNR_CITY_OFFSET + OWNR_CITY_LENGTH)));
				laAddressData.setCntry(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_CNTRY_OFFSET,
							OWNR_CNTRY_OFFSET + OWNR_CNTRY_LENGTH)));
				laAddressData.setSt1(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_ST1_OFFSET,
							OWNR_ST1_OFFSET + OWNR_ST1_LENGTH)));
				laAddressData.setSt2(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_ST2_OFFSET,
							OWNR_ST2_OFFSET + OWNR_ST2_LENGTH)));
				laAddressData.setState(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_STATE_OFFSET,
							OWNR_STATE_OFFSET + OWNR_STATE_LENGTH)));
				laAddressData.setZpcd(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_ZPCD_OFFSET,
							OWNR_ZPCD_OFFSET + OWNR_ZPCD_LENGTH)));
				laAddressData.setZpcdp4(
					laMfAccess.trimMfString(
						lsMfOwnerResponse.substring(
							OWNR_ZPCDP4_OFFSET,
							OWNR_ZPCDP4_OFFSET + OWNR_ZPCDP4_LENGTH)));
				// defect 10112
				laOwnerData.setAddressData(laAddressData);
				// end defect 10112 
			}
		}
		return laOwnerData;
	}
}
