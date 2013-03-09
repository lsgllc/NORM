package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaPartialSpclPltDataV.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		01/31/2007	Update offset to new format
 * 							deefct 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/23/2007	Add RegPltCd
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Update offset to new format
 * 							defect 9086 Ver Special Plates
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setMFPartialDataFromMfResponse()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		02/25/2009	Clean up JavaDoc
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
 * 							defect 10595 Ver POS_670
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0    
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into MfPartial  Special Plate. 
 * Used by the server side business layer - Special Plates. 
 *
 * @version	6.9.0  			11/01/2011
 * @author	Jeff Rue
 * <br>Creation Date:		04/27/2007 15:59:32
 */
public class MfbaPartialSpclPltDataV
{
	//define all lengths and offsets
	private final static int REG_PLT_NO_OFFSET = 0;
	private final static int REG_PLT_NO_LENGTH = 7;
	private final static int PLT_OWNR_NAME1_OFFSET = 7;
	private final static int PLT_OWNR_NAME1_LENGTH = 30;
	private final static int REG_EXP_MO_OFFSET = 37;
	private final static int REG_EXP_MO_LENGTH = 2;
	private final static int REG_EXP_YR_OFFSET = 39;
	private final static int REG_EXP_YR_LENGTH = 4;
	private final static int SPCL_REG_ID_OFFSET = 43;
	private final static int SPCL_REG_ID_LENGTH = 9;
	private final static int SPCL_DOC_NO_OFFSET = 52;
	private final static int SPCL_DOC_NO_LENGTH = 17;
	private final static int DEL_INDI_OFFSET = 69;
	private final static int DEL_INDI_LENGTH = 1;
	private final static int MFG_STATUS_CD_OFFSET = 70;
	private final static int MFG_STATUS_CD_LENGTH = 1;
	private final static int REG_PLT_CD_OFFSET = 71;
	private final static int REG_PLT_CD_LENGTH = 8;

	// get the number of records
	// Data from the header record
	private final static int NO_OF_RECS_OFFSET = 145;
	private final static int NO_OF_RECS_LENGTH = 3;
	private final static int MFPARTIAL_RECORD_LENGTH = 79;

	//remove the header from the response
	private final static int HEADER_LENGTH = 256;

	/**
	 * 
	 */
	public MfbaPartialSpclPltDataV()
	{
		super();
	}

	/**
	 * Sets the MfPartialSpclPlt data object from the mainframe response. 
	 * Uses CICS PGMS R08 
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
				MFPartialSpclPltData laMFPartialData =
					new MFPartialSpclPltData();

				laMFPartialData.setRegPltNo(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + REG_PLT_NO_OFFSET,
							liRecordOffset
								+ REG_PLT_NO_OFFSET
								+ REG_PLT_NO_LENGTH)));

				laMFPartialData.setPltOwnrName1(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + PLT_OWNR_NAME1_OFFSET,
							liRecordOffset
								+ PLT_OWNR_NAME1_OFFSET
								+ PLT_OWNR_NAME1_LENGTH)));

				laMFPartialData.setRegExpMo(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfPartialResponse.substring(
									liRecordOffset + REG_EXP_MO_OFFSET,
									liRecordOffset
										+ REG_EXP_MO_OFFSET
										+ REG_EXP_MO_LENGTH)))
						.intValue());

				laMFPartialData.setRegExpYr(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfPartialResponse.substring(
									liRecordOffset + REG_EXP_YR_OFFSET,
									liRecordOffset
										+ REG_EXP_YR_OFFSET
										+ REG_EXP_YR_LENGTH)))
						.intValue());

				laMFPartialData.setSpclRegId(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfPartialResponse.substring(
									liRecordOffset + SPCL_REG_ID_OFFSET,
									liRecordOffset
										+ SPCL_REG_ID_OFFSET
										+ SPCL_REG_ID_LENGTH)))
						.intValue());

				laMFPartialData.setSpclDocNo(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + SPCL_DOC_NO_OFFSET,
							liRecordOffset
								+ SPCL_DOC_NO_OFFSET
								+ SPCL_DOC_NO_LENGTH)));

				laMFPartialData.setDelIndi(
					Integer
						.valueOf(
							laMfAccess.getStringFromZonedDecimal(
								asMfPartialResponse.substring(
									liRecordOffset + DEL_INDI_OFFSET,
									liRecordOffset
										+ DEL_INDI_OFFSET
										+ DEL_INDI_LENGTH)))
						.intValue());

				laMFPartialData.setMFGStatusCd(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + MFG_STATUS_CD_OFFSET,
							liRecordOffset
								+ MFG_STATUS_CD_OFFSET
								+ MFG_STATUS_CD_LENGTH)));

				laMFPartialData.setRegPltCd(
					laMfAccess.trimMfString(
						asMfPartialResponse.substring(
							liRecordOffset + REG_PLT_CD_OFFSET,
							liRecordOffset
								+ REG_PLT_CD_OFFSET
								+ REG_PLT_CD_LENGTH)));

				//add this object to the vector
				lvMfPartialContainer.addElement(laMFPartialData);
			}
		}

		//return the result
		return lvMfPartialContainer;
	}
}
