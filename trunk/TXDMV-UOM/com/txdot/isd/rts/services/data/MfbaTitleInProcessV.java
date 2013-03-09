package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaTitleInProcessV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/27/2006	Move code from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue		10/27/2006	Update offset to new format
 * 							defect 6701 Ver Exempts
 * J Rue		02/02/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		04/26/2007	Remove header from MfResponse. came from 
 * 							MfAccess()
 * 							modify setTitleInProcessDataFromMfResponse()
 * 							deefct 8983 Ver Special Plates
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setTitleInProcessDataFromMfResponse()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		03/12/2009	Remove code used for testing (Defect 8983)
 * 							modify setTitleInProcessDataFromMfResponse()
 * 							defect 9965 Ver Defect_POS_E
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * K Harrell	06/22/2010 	Copy version U to version V for MF
 * 							MF version V. Modified All.
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
 * Parse CICS transactions into TitleInProcessData. 
 * Used by the server side business layer - Exempts/TERPS. 
 *
 * @version	6.9.0			11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		09/26/2006 09:53:32
 */
public class MfbaTitleInProcessV
{
	//define all the lengths and offsets
	private final static int DOC_NO_OFFSET = 0;
	private final static int DOC_NO_LENGTH = 17;
	private final static int VEH_MK_OFFSET = 17;
	private final static int VEH_MK_LENGTH = 4;
	private final static int OWNR_TTL_NAME1_OFFSET = 21;
	private final static int OWNR_TTL_NAME1_LENGTH = 30;
	private final static int DPSSTLN_INDI_OFFSET = 51;
	private final static int DPSSTLN_INDI_LENGTH = 1;
	private final static int OFC_ISSUANCE_NO_OFFSET = 52;
	private final static int OFC_ISSUANCE_NO_LENGTH = 3;
	private final static int TRANS_AMDATE_OFFSET = 55;
	private final static int TRANS_AMDATE_LENGTH = 5;
	private final static int OWNRSHP_EVID_CD_OFFSET = 60;
	private final static int OWNRSHP_EVID_CD_LENGTH = 2;
	private final static int VIN_OFFSET = 62;
	private final static int VIN_LENGTH = 22;

	/**
	 * 
	 */
	public MfbaTitleInProcessV()
	{
		super();
	}

	public static void main(String[] args)
	{
	}
	/**
	 * Sets all fields in the <code>TitleInProcess</code> data object
	 * from mainframe response. 
	 * 
	 * @param asTitleInProcessResponse String
	 * @return TitleInProcessData
	 */
	public TitleInProcessData setTitleInProcessDataFromMfResponse(String asTitleInProcessResponse)
	{
		MfAccess laMFAccess = new MfAccess();

		//create return object
		TitleInProcessData laTitleInProcessData =
			new TitleInProcessData();

		if (!(asTitleInProcessResponse
			.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asTitleInProcessResponse != null))
		{
			laTitleInProcessData.setDocNo(
				laMFAccess.trimMfString(
					asTitleInProcessResponse.substring(
						DOC_NO_OFFSET,
						DOC_NO_OFFSET + DOC_NO_LENGTH)));
			laTitleInProcessData.setVehMk(
				laMFAccess.trimMfString(
					asTitleInProcessResponse.substring(
						VEH_MK_OFFSET,
						VEH_MK_OFFSET + VEH_MK_LENGTH)));
			laTitleInProcessData.setOwnrTtlName1(
				laMFAccess.trimMfString(
					asTitleInProcessResponse.substring(
						OWNR_TTL_NAME1_OFFSET,
						OWNR_TTL_NAME1_OFFSET
							+ OWNR_TTL_NAME1_LENGTH)));
			laTitleInProcessData.setDPSStlnIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asTitleInProcessResponse.substring(
								DPSSTLN_INDI_OFFSET,
								DPSSTLN_INDI_OFFSET
									+ DPSSTLN_INDI_LENGTH)))
					.intValue());
			laTitleInProcessData.setOfcIssuanceNo(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asTitleInProcessResponse.substring(
								OFC_ISSUANCE_NO_OFFSET,
								OFC_ISSUANCE_NO_OFFSET
									+ OFC_ISSUANCE_NO_LENGTH)))
					.intValue());
			laTitleInProcessData.setOwnrshpEvidCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asTitleInProcessResponse.substring(
								OWNRSHP_EVID_CD_OFFSET,
								OWNRSHP_EVID_CD_OFFSET
									+ OWNRSHP_EVID_CD_LENGTH)))
					.intValue());
			laTitleInProcessData.setVIN(
				laMFAccess.trimMfString(
					asTitleInProcessResponse.substring(
						VIN_OFFSET,
						VIN_OFFSET + VIN_LENGTH)));
			//set all date fields
			laTitleInProcessData.setTransAMDate(
				new RTSDate(
					RTSDate.AMDATE,
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asTitleInProcessResponse.substring(
									TRANS_AMDATE_OFFSET,
									TRANS_AMDATE_OFFSET
										+ TRANS_AMDATE_LENGTH)))
						.intValue()));
		}

		//return the object
		return laTitleInProcessData;
	}
}
