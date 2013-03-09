package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * MfbaFundsRemittanceU.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/26/2007	Add offsets and code
 * 							defect 8983 Ver Special Plates
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setMultipleFundsDueDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables,
 * 								setMultipleFundsDueDataFromMf()
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		02/25/2009	Arrange class variables from lowest offset
 * 							to highest
 * 							defect 9961 Ver Defect_POS_E
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640 
 * K Harrell 	06/22/2010  Copy version U to version V for MF
 *        					MF version V. Modified All.
 *        					defect 10492 Ver 6.5.0 
 * M Reyes		10/04/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver 6.6.0
 * M Reyes		12/31/2010	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * This class parses the MainFrame response for retrieving an Inventory 
 * Used by the server side business layer - Special Plates. 
 *
 * @version	6.7.0 		12/31/2010
 * @author	Jeff Rue
 * <br>Creation Date:		04/26/2007 14:44:32
 */
public class MfbaFundsRemittanceU
{
	//define all offsets and lengths
	final int COMPT_CNTY_NO_OFFSET = 0;
	final int COMPT_CNTY_NO_LENGTH = 3;
	final int FUNDS_RPT_DATE_OFFSET = 3;
	final int FUNDS_RPT_DATE_LENGTH = 8;
	final int RPTNG_DATE_OFFSET = 11;
	final int RPTNG_DATE_LENGTH = 8;
	final int FUNDS_CAT_OFFSET = 19;
	final int FUNDS_CAT_LENGTH = 14;
	final int FUNDS_RCVNG_ENT_OFFSET = 33;
	final int FUNDS_RCVNG_ENT_LENGTH = 9;
	final int FUNDS_RCVD_AMT_OFFSET = 42;
	final int FUNDS_RCVD_AMT_LENGTH = 11;
	final int FUNDS_RCVD_AMT_DECIMAL = 2;
	final int FUNDS_DUE_DATE_OFFSET = 53;
	final int FUNDS_DUE_DATE_LENGTH = 8;
	final int ENT_DUE_AMT_OFFSET = 61;
	final int ENT_DUE_AMT_LENGTH = 11;
	final int ENT_DUE_AMT_DECIMAL = 2;

	// the length of one FUNDS A/R record from mainframe. 
	final int FUNDSAR_RECORD_LENGTH = 72;
	// Find the number of elements from output length. the # 
	//  in header is not correct. 	
	final int OUPUT_LENGTH_OFFSET = 115;
	final int OUTPUT_LENGTH_LENGTH = 5;
	//remove the header from the response
	final int liHEADER_LENGTH = 256;

	/**
	 * 
	 */
	public MfbaFundsRemittanceU()
	{
		super();
	}
	public static void main(String[] args)
	{
	}
	/**
	 * Sets and returns multiple funds payment data objects from
	 * the mainframe response. 
	 * 
	 * @return Vector
	 * @param asMfFundsPaymentResponse String
	 * @throws RTSException
	 */
	public Vector setMultipleFundsDueDataFromMf(String asMfFundsDueResponse)
		throws RTSException
	{
		MfAccess laMFAccess = new MfAccess();

		// create the retrun object 
		Vector lvFundsDueDataContainer = new Vector();
		final int NUMBER_OF_ELEMENTS =
			Integer.parseInt(
				asMfFundsDueResponse.substring(
					OUPUT_LENGTH_OFFSET,
					OUPUT_LENGTH_OFFSET + OUTPUT_LENGTH_LENGTH))
				/ FUNDSAR_RECORD_LENGTH;

		asMfFundsDueResponse =
			asMfFundsDueResponse.substring(liHEADER_LENGTH);

		// the offset based on the record that is picked from the mf
		//  response
		int liRecordOffset = 0;
		if (!(asMfFundsDueResponse
			.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfFundsDueResponse != null))
		{
			for (int liIndex = 0;
				liIndex < NUMBER_OF_ELEMENTS;
				liIndex++)
			{
				//set the record offset
				liRecordOffset = liIndex * FUNDSAR_RECORD_LENGTH;
				//create registration data
				FundsDueData laFundsDueData = new FundsDueData();
				//set all values from FundsDueData
				laFundsDueData.setComptCountyNo(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfFundsDueResponse.substring(
									liRecordOffset
										+ COMPT_CNTY_NO_OFFSET,
									liRecordOffset
										+ COMPT_CNTY_NO_OFFSET
										+ COMPT_CNTY_NO_LENGTH)))
						.toString());
				laFundsDueData.setFundsCategory(
					laMFAccess.trimMfString(
						asMfFundsDueResponse.substring(
							liRecordOffset + FUNDS_CAT_OFFSET,
							liRecordOffset
								+ FUNDS_CAT_OFFSET
								+ FUNDS_CAT_LENGTH)));
				laFundsDueData.setFundsReceivingEntity(
					laMFAccess.trimMfString(
						asMfFundsDueResponse.substring(
							liRecordOffset + FUNDS_RCVNG_ENT_OFFSET,
							liRecordOffset
								+ FUNDS_RCVNG_ENT_OFFSET
								+ FUNDS_RCVNG_ENT_LENGTH)));
				//set all dates
				laFundsDueData.setFundsReportDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfFundsDueResponse.substring(
										liRecordOffset
											+ FUNDS_RPT_DATE_OFFSET,
										liRecordOffset
											+ FUNDS_RPT_DATE_OFFSET
											+ FUNDS_RPT_DATE_LENGTH)))
							.intValue()));
				laFundsDueData.setReportingDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfFundsDueResponse.substring(
										liRecordOffset
											+ RPTNG_DATE_OFFSET,
										liRecordOffset
											+ RPTNG_DATE_OFFSET
											+ RPTNG_DATE_LENGTH)))
							.intValue()));
				laFundsDueData.setFundsDueDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfFundsDueResponse.substring(
										liRecordOffset
											+ FUNDS_DUE_DATE_OFFSET,
										liRecordOffset
											+ FUNDS_DUE_DATE_OFFSET
											+ FUNDS_DUE_DATE_LENGTH)))
							.intValue()));
				//set all amuonts
				StringBuffer laFundsRcvdAmtBuffer =
					new StringBuffer(
						laMFAccess.getStringFromZonedDecimal(
							asMfFundsDueResponse.substring(
								liRecordOffset + FUNDS_RCVD_AMT_OFFSET,
								liRecordOffset
									+ FUNDS_RCVD_AMT_OFFSET
									+ FUNDS_RCVD_AMT_LENGTH)));
				if (!(laFundsRcvdAmtBuffer.charAt(0) == '-'))
				{
					laFundsRcvdAmtBuffer.insert(
						FUNDS_RCVD_AMT_LENGTH - FUNDS_RCVD_AMT_DECIMAL,
						'.');
				}
				else
				{
					laFundsRcvdAmtBuffer.insert(
						FUNDS_RCVD_AMT_LENGTH
							- FUNDS_RCVD_AMT_DECIMAL
							+ 1,
						'.');
				}
				Dollar laFundsRcvdAmt =
					new Dollar(laFundsRcvdAmtBuffer.toString());
				laFundsDueData.setFundsReceivedAmount(laFundsRcvdAmt);
				StringBuffer laEntDueAmtBuffer =
					new StringBuffer(
						laMFAccess.getStringFromZonedDecimal(
							asMfFundsDueResponse.substring(
								liRecordOffset + ENT_DUE_AMT_OFFSET,
								liRecordOffset
									+ ENT_DUE_AMT_OFFSET
									+ ENT_DUE_AMT_LENGTH)));
				if (!(laEntDueAmtBuffer.charAt(0) == '-'))
				{
					laEntDueAmtBuffer.insert(
						ENT_DUE_AMT_LENGTH - ENT_DUE_AMT_DECIMAL,
						'.');
				}
				else
				{
					laEntDueAmtBuffer.insert(
						ENT_DUE_AMT_LENGTH - ENT_DUE_AMT_DECIMAL + 1,
						'.');
				}
				Dollar laEntDueAmt =
					new Dollar(laEntDueAmtBuffer.toString());
				laFundsDueData.setEntDueAmount(laEntDueAmt);
				lvFundsDueDataContainer.addElement(laFundsDueData);
			}
		} // end of if
		return lvFundsDueDataContainer;
	}
}
