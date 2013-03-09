package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaPartialDataV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/02/2006	Update offset to new format
 * 							deefct 6701 Ver Exempts
 * J Rue		10/06/2006	Clean up offsets
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Move process from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		04/04/2008	Add DissociatedCd
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		04/30/2008	setPltRmvCd() replaced setDissociateCd()
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/06/2008	Correct offset for PLTRMVCD_OFFSET
 * 							modify PLTRMVCD_OFFSET
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setMFPartialDataFromMfResponse()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables,
 * 								setMFPartialDataFromMfResponse()
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		02/25/2009	Update JavaDoc
 * 							defect 9961 Ver Defect_POS_E
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
 * Parse CICS transactions into MfPartial. 
 * Used by the server side business layer - Exempts/TERPS. 
 *
 * @version	6.9.0   			11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:10:32
 */
public class MfbaPartialDataV
{
	//define all lengths and offsets
	private final static int DOC_NO_OFFSET = 0;
	private final static int DOC_NO_LENGTH = 17;
	private final static int VEH_MK_OFFSET = 17;
	private final static int VEH_MK_LENGTH = 4;
	private final static int VEH_MODL_YR_OFFSET = 21;
	private final static int VEH_MODL_YR_LENGTH = 4;
	private final static int HOOPS_REG_PLT_NO_OFFSET = 25;
	private final static int HOOPS_REG_PLT_NO_LENGTH = 7;
	private final static int OWNR_TTL_NAME1_OFFSET = 32;
	private final static int OWNR_TTL_NAME1_LENGTH = 30;
	private final static int VIN_OFFSET = 62;
	private final static int VIN_LENGTH = 22;
	// defect 9630
	//	Add DissociatedCd
	//	private final static int DISSOCIATEDCD_OFFSET = 84;
	//	private final static int DISSOCIATEDCD_LENGTH = 1;
	private final static int PLTRMVCD_OFFSET = 84;
	private final static int PLTRMVCD_LENGTH = 1;
	// end defect 9630

	// get the number of records
	// Data from the header record
	private final static int NO_OF_RECS_OFFSET = 145;
	private final static int NO_OF_RECS_LENGTH = 3;
	private final static int MFPARTIAL_RECORD_LENGTH = 85;

	//remove the header from the response
	private final static int HEADER_LENGTH = 256;

	/**
	 * 
	 */
	public MfbaPartialDataV()
	{
		super();
	}

	public static void main(String[] args)
	{
	}

	/**
	 * Sets the MfPartial data object from the mainframe response. 
	 * Uses CICS PGMS R01, R02, R03, R04 and R05 
	 * 
	 * @param asMfPartialReponse String
	 * @return Vector
	 */
	public Vector setMFPartialDataFromMfResponse(String asMfPartialResponse)
	{
		//create the return object
		Vector lvMfPartialContainer = new Vector();
		MfAccess laMfAccess = new MfAccess();

		int liRecordOffset = 0;
		if (!(asMfPartialResponse
			.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfPartialResponse != null))
		{
			int liNO_OF_RECS =
				Integer
					.valueOf(
						laMfAccess.getStringFromZonedDecimal(
							asMfPartialResponse.substring(
								NO_OF_RECS_OFFSET,
								NO_OF_RECS_OFFSET
									+ NO_OF_RECS_LENGTH)))
					.intValue();

			asMfPartialResponse =
				asMfPartialResponse.substring(HEADER_LENGTH);

			for (int i = 0; i < liNO_OF_RECS; i++)
			{
				//set the record offset
				liRecordOffset = i * MFPARTIAL_RECORD_LENGTH;

				//create the Partial Data object
				MFPartialData laMFPartialData = new MFPartialData();

				laMFPartialData.setDocNo(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + DOC_NO_OFFSET,
							liRecordOffset
								+ DOC_NO_OFFSET
								+ DOC_NO_LENGTH)));

				laMFPartialData.setOwnrTtlName(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + OWNR_TTL_NAME1_OFFSET,
							liRecordOffset
								+ OWNR_TTL_NAME1_OFFSET
								+ OWNR_TTL_NAME1_LENGTH)));

				laMFPartialData.setVehMk(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + VEH_MK_OFFSET,
							liRecordOffset
								+ VEH_MK_OFFSET
								+ VEH_MK_LENGTH)));

				laMFPartialData.setVehModlYr(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfPartialResponse.substring(
									liRecordOffset + VEH_MODL_YR_OFFSET,
									liRecordOffset
										+ VEH_MODL_YR_OFFSET
										+ VEH_MODL_YR_LENGTH)))
						.intValue());

				laMFPartialData.setRegPltNo(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + HOOPS_REG_PLT_NO_OFFSET,
							liRecordOffset
								+ HOOPS_REG_PLT_NO_OFFSET
								+ HOOPS_REG_PLT_NO_LENGTH)));

				laMFPartialData.setVin(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + VIN_OFFSET,
							liRecordOffset + VIN_OFFSET + VIN_LENGTH)));

				// defect 9630
				//	Set DissociatedCd
				//	Try/catch is included until conversion is complete
				//	DissociatedCd is numeric and >= 0
				//	setPltRmvCd() replaced setDissociateCd()
				try
				{
					laMFPartialData.setPltRmvCd(
						Integer
							.valueOf(
								laMfAccess.getStringFromZonedDecimal(
									asMfPartialResponse.substring(
										liRecordOffset
											+ PLTRMVCD_OFFSET,
										liRecordOffset
											+ PLTRMVCD_OFFSET
											+ PLTRMVCD_LENGTH)))
							.intValue());
				}
				catch (NumberFormatException aeNFExc)
				{
					laMFPartialData.setPltRmvCd(0);
				}
				// end defect 9630

				//add this object to the vector
				lvMfPartialContainer.addElement(laMFPartialData);
			}
		}
		//return the result
		return lvMfPartialContainer;
	}
}
