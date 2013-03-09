package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.UtilityMethods;

import com.txdot.isd.rts.server.dataaccess.MfAccess;

/*
 * MfbaPartialPermitDataU.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/08/2010	Created
 * 							defect 10492 Ver 6.5.0 
 * K Harrell	06/20/2010	modified Partial Length to 145 
 * 							delete NO_OF_RECS_OFFSET, NO_OF_RECS_LENGTH
 * 							modify NO_OF_RECS_OFFSET, 
 * 							 setMFPartialPermitDataFromMfResponse()
 * 							defect 10492 Ver 6.5.0
 * K Harrell	07/14/2010	add EXPTIME_OFFSET, EXPTIME_LENGTH
 * 							delete VEHREGPLTNO_OFFSET, 
 * 							  VEHREGPLTNO_LENGTH 
 * 							modify MFPARTIAL_RECORD_LENGTH 
 * 							modify setMFPartialPermitDataFromMfResponse()
 * 							defect 10492 Ver 6.5.0 
 * M Reyes		10/05/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		12/31/2010	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transaction Data into MFPartialPermitData.
 *
 * @version	6.7.0			12/31/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		06/08/2010  16:17:17 
 */
public class MfbaPartialPermitDataU
{

	// TODO MfAccess - many methods should be static 
	MfAccess caMfAccess = new MfAccess();

	// String 
	String csMfResponseData = new String();

	private final static int PRMTISSUANCEID_OFFSET = 0;
	private final static int PRMTISSUANCEID_LENGTH = 17;

	private final static int CUSTNAME_OFFSET = 17;
	private final static int CUSTNAME_LENGTH = 60;

	private final static int PRMTNO_OFFSET = 77;
	private final static int PRMTNO_LENGTH = 7;

	private final static int ITMCD_OFFSET = 84;
	private final static int ITMCD_LENGTH = 8;

	private final static int EXPDATE_OFFSET = 92;
	private final static int EXPDATE_LENGTH = 8;

	private final static int VEHMK_OFFSET = 100;
	private final static int VEHMK_LENGTH = 4;

	private final static int VEHMODLYR_OFFSET = 104;
	private final static int VEHMODLYR_LENGTH = 4;

	private final static int VIN_OFFSET = 108;
	private final static int VIN_LENGTH = 22;

	private final static int EXPTIME_OFFSET = 130;
	private final static int EXPTIME_LENGTH = 6;

	// get the number of records
	// Data from the header record
	private final static int MFPARTIAL_RECORD_LENGTH = 136;
	private final static int NO_OF_RECS_OFFSET = 145;
	private final static int NO_OF_RECS_LENGTH = 3;
	private final static int MAX_NO_PARTIALS = 200;

	//remove the header from the response
	private final static int HEADER_LENGTH = 256;
	// get the number of records

	/**
	 * MfbaPartialPermitDataV.java Constructor
	 * 
	 */
	public MfbaPartialPermitDataU()
	{
		super();
	}

	/**
	 * get Integer Data 
	 * 
	 * @param aiRecordOffset
	 * @param aiItmOffset
	 * @param aiLength
	 * 
	 * @return int 
	 */
	private int getIntegerData(
		int aiRecordOffset,
		int aiItmOffset,
		int aiLength)
	{
		return Integer
			.valueOf(
				caMfAccess.getStringFromZonedDecimal(
					getStringData(
						aiRecordOffset,
						aiItmOffset,
						aiLength)))
			.intValue();
	}

	/**
	 * get String Data
	 * 
	 * @param aiRecordOffset
	 * @param aiItmOffset
	 * @param aiLength
	 * 
	 * @return String 
	 */
	private String getStringData(
		int aiRecordOffset,
		int aiItmOffset,
		int aiLength)
	{
		int liStart = aiRecordOffset + aiItmOffset;
		int liEnd = liStart + aiLength;

		return (
			caMfAccess.trimMfString(
				csMfResponseData.substring(liStart, liEnd)));
	}

	/**
	 * Sets the MfPartial data object from the mainframe response. 
	 *
	 * @param asMfPartialResponse 
	 * @return Vector 
	 */
	public Vector setMFPartialPermitDataFromMfResponse(String asMfPartialResponse)
	{
		// Create the return Vector
		Vector lvMfPartialContainer = new Vector();
		MfAccess laMfAccess = new MfAccess();

		int liRecordOffset = 0;

		if (!UtilityMethods.isEmpty(asMfPartialResponse))
		{
			csMfResponseData =
				asMfPartialResponse.substring(HEADER_LENGTH);

			int liNO_OF_RECS =
				Integer
					.valueOf(
						laMfAccess.getStringFromZonedDecimal(
							asMfPartialResponse.substring(
								NO_OF_RECS_OFFSET,
								NO_OF_RECS_OFFSET
									+ NO_OF_RECS_LENGTH)))
					.intValue();

			int liNumRecords = Math.min(liNO_OF_RECS, MAX_NO_PARTIALS);

			for (int i = 0; i < liNumRecords; i++)
			{
				liRecordOffset = i * MFPARTIAL_RECORD_LENGTH;

				MFPartialPermitData laMFPartialData =
					new MFPartialPermitData();

				laMFPartialData.setPrmtIssuanceId(
					getStringData(
						liRecordOffset,
						PRMTISSUANCEID_OFFSET,
						PRMTISSUANCEID_LENGTH));

				laMFPartialData.setCustName(
					getStringData(
						liRecordOffset,
						CUSTNAME_OFFSET,
						CUSTNAME_LENGTH));

				laMFPartialData.setPrmtNo(
					getStringData(
						liRecordOffset,
						PRMTNO_OFFSET,
						PRMTNO_LENGTH));

				laMFPartialData.setItmCd(
					getStringData(
						liRecordOffset,
						ITMCD_OFFSET,
						ITMCD_LENGTH));

				laMFPartialData.setExpDate(
					getIntegerData(
						liRecordOffset,
						EXPDATE_OFFSET,
						EXPDATE_LENGTH));

				laMFPartialData.setVehMk(
					getStringData(
						liRecordOffset,
						VEHMK_OFFSET,
						VEHMK_LENGTH));

				laMFPartialData.setVehModlYr(
					getIntegerData(
						liRecordOffset,
						VEHMODLYR_OFFSET,
						VEHMODLYR_LENGTH));

				laMFPartialData.setVin(
					getStringData(
						liRecordOffset,
						VIN_OFFSET,
						VIN_LENGTH));

				laMFPartialData.setExpTime(
					getIntegerData(
						liRecordOffset,
						EXPTIME_OFFSET,
						EXPTIME_LENGTH));

				lvMfPartialContainer.addElement(laMFPartialData);
			}
		}
		return lvMfPartialContainer;
	}
}
