package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaSalvageV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/02/2006	Update offset to new format
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Move process from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Update JavaDoc
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/04/2008	New format MF version V
 * 							modify all
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/05/2008	Order offset/length by offsets
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/11/2008	Change constant from liSALVAGE_RECORD_LENGTH
 * 							to SALVAGE_RECORD_LENGTH
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setMultipleSalvageDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables,
 * 								setMultipleSalvageDataFromMf()
 * 							defect 9655 Ver Defect_POS_D
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
 * 							defect 10595 Ver 6.6.0
 * M Reyes		01/03/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver 6.7.0
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0       
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into SalvageData. 
 * Used by the server side business layer - Exempts/TERPS. 
 *
 * @version	 6.9.0			11/01/2011	
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:10:32
 */

public class MfbaSalvageV
{
	private final static int JNK_CD_OFFSET = 0;
	private final static int JNK_CD_LENGTH = 2;
	private final static int JNK_DATE_OFFSET = 2;
	private final static int JNK_DATE_LENGTH = 8;
	private final static int OTHR_STATE_CNTRY_OFFSET = 10;
	private final static int OTHR_STATE_CNTRY_LENGTH = 2;
	private final static int OTHR_GOVT_TTL_NO_OFFSET = 12;
	private final static int OTHR_GOVT_TTL_NO_LENGTH = 11;
	private final static int OWNRSHP_EVID_CD_OFFSET = 23;
	private final static int OWNRSHP_EVID_CD_LENGTH = 2;
	private final static int SALV_YARD_NO_OFFSET = 25;
	private final static int SALV_YARD_NO_LENGTH = 9;
	private final static int LIEN2_NOT_RLSD_INDI_OFFSET = 34;
	private final static int LIEN2_NOT_RLSD_INDI_LENGTH = 1;
	private final static int LIEN3_NOT_RLSD_INDI_OFFSET = 35;
	private final static int LIEN3_NOT_RLSD_INDI_LENGTH = 1;
	private final static int LIEN_NOT_RLSD_INDI_OFFSET = 36;
	private final static int LIEN_NOT_RLSD_INDI_LENGTH = 1;

	// defect 9557
	private final static int SALVAGE_RECORD_LENGTH = 37;
	// end defect 9557

	//remove the header from the response
	final int HEADER_LENGTH = 256;
	final int NUMBER_OF_RECORDS_LENGTH = 3;
	//define all lengths and offsets
	//get the number of records value from mainframe header
	final int NUMBER_OF_RECORDS_OFFSET = 145;

	/**
	 * 
	 */
	public MfbaSalvageV()
	{
		super();
	}

	public static void main(String[] args)
	{
	}
	/**
	 * Sets and returns multiple Salvage data objects from the mainframe
	 * response. 
	 * 
	 * @param asMfJunkResponse String
	 * @return SalvageData
	 */
	public Vector setMultipleSalvageDataFromMf(String asMfJunkResponse)
	{
		//create the salavage vector
		Vector laVctSalvageContainer = new Vector();
		MfAccess laMFAccess = new MfAccess();

		if (!(asMfJunkResponse.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfJunkResponse != null))
		{
			//get the number of records value from mainframe header
			final int liNumberOfElements =
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfJunkResponse.substring(
								NUMBER_OF_RECORDS_OFFSET,
								NUMBER_OF_RECORDS_OFFSET
									+ NUMBER_OF_RECORDS_LENGTH)))
					.intValue();

			//remove the header from the response
			asMfJunkResponse =
				asMfJunkResponse.substring(HEADER_LENGTH);

			// the offset based on the record that is picked from the
			// mf response
			int liRecordOffset = 0;
			for (int i = 0; i < liNumberOfElements; i++)
			{
				//set the record offset
				liRecordOffset = i * SALVAGE_RECORD_LENGTH;
				//create the salvage Data object
				SalvageData laSalvageData = new SalvageData();
				//get data in object
				laSalvageData.setSlvgCd(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfJunkResponse.substring(
									liRecordOffset + JNK_CD_OFFSET,
									liRecordOffset
										+ JNK_CD_OFFSET
										+ JNK_CD_LENGTH)))
						.intValue());

				laSalvageData.setOthrStateCntry(
					laMFAccess.trimMfString(
						asMfJunkResponse.substring(
							liRecordOffset + OTHR_STATE_CNTRY_OFFSET,
							liRecordOffset
								+ OTHR_STATE_CNTRY_OFFSET
								+ OTHR_STATE_CNTRY_LENGTH)));

				laSalvageData.setOthrGovtTtlNo(
					laMFAccess.trimMfString(
						asMfJunkResponse.substring(
							liRecordOffset + OTHR_GOVT_TTL_NO_OFFSET,
							liRecordOffset
								+ OTHR_GOVT_TTL_NO_OFFSET
								+ OTHR_GOVT_TTL_NO_LENGTH)));

				laSalvageData.setSalvYardNo(
					laMFAccess.trimMfString(
						asMfJunkResponse.substring(
							liRecordOffset + SALV_YARD_NO_OFFSET,
							liRecordOffset
								+ SALV_YARD_NO_OFFSET
								+ SALV_YARD_NO_LENGTH)));

				laSalvageData.setOwnrEvdncCd(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfJunkResponse.substring(
									liRecordOffset
										+ OWNRSHP_EVID_CD_OFFSET,
									liRecordOffset
										+ OWNRSHP_EVID_CD_OFFSET
										+ OWNRSHP_EVID_CD_LENGTH)))
						.intValue());

				laSalvageData.setLienNotRlsedIndi(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfJunkResponse.substring(
									liRecordOffset
										+ LIEN_NOT_RLSD_INDI_OFFSET,
									liRecordOffset
										+ LIEN_NOT_RLSD_INDI_OFFSET
										+ LIEN_NOT_RLSD_INDI_LENGTH)))
						.intValue());

				laSalvageData.setLienNotRlsedIndi2(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfJunkResponse.substring(
									liRecordOffset
										+ LIEN2_NOT_RLSD_INDI_OFFSET,
									liRecordOffset
										+ LIEN2_NOT_RLSD_INDI_OFFSET
										+ LIEN2_NOT_RLSD_INDI_LENGTH)))
						.intValue());

				laSalvageData.setLienNotRlsedIndi3(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfJunkResponse.substring(
									liRecordOffset
										+ LIEN3_NOT_RLSD_INDI_OFFSET,
									liRecordOffset
										+ LIEN3_NOT_RLSD_INDI_OFFSET
										+ LIEN3_NOT_RLSD_INDI_LENGTH)))
						.intValue());

				//set JunkDate
				laSalvageData.setSlvgDt(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfJunkResponse.substring(
										liRecordOffset
											+ JNK_DATE_OFFSET,
										liRecordOffset
											+ JNK_DATE_OFFSET
											+ JNK_DATE_LENGTH)))
							.intValue()));

				//add this salvage object to the vector
				laVctSalvageContainer.addElement(laSalvageData);
			} // end of the for loop

			if (liNumberOfElements < 1)
			{
				SalvageData laSalvageData = new SalvageData();
				laVctSalvageContainer.addElement(laSalvageData);
			}
		} // end of if

		//return filled SalvageData object
		return laVctSalvageContainer;
	}
}
