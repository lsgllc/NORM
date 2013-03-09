package com.txdot.isd.rts.services.data;

import java.util.Vector;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MfbaFundsPaymentT.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		11/03/2006	Remove Batch_NO
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		02/02/2007	Update JavaDoc
 * 							defect 9086 Ver Special Plates
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setMultipleFundsPaymentDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables,
 * 								setMultipleFundsPaymentDataFromMf()
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		02/25/2009	Arrange class variables from lowest offset
 * 							to highest
 * 							defect 9961 Ver Defect_POS_E
 * J Rue		10/06/2009	Arrange class variables from lowest offset
 * 							to highest
 * 							defect 10244 Ver Defect_POS_G
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
 * 							defect 10595 Ver POS_660
 * M Reyes		12/31/2010	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_667
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0     
 * B Woodson	01/20/2012	Copy version V to T for MF Version T 
 * 							deleted default constructor
 * 							defect 11251 Ver 6.10.0
 * ---------------------------------------------------------------------
 */
/**
 * Sets and returns multiple funds payment data objects from
 * the mainframe response. 
 * Used by the server side business layer. 
 *
 * @version	6.10.0 			01/20/2012
 * @author	Jeff Rue
 * <br>Creation Date:		10/11/2006 08:10:32
 */
public class MfbaFundsPaymentT
{
	// Header from the response
	private final static int HEADER_LENGTH = 256;
	//the length of one FUNDS A/R record from mainframe. 
	private final static int FUNDS_PAYMENT_RECORD_LENGTH = 90;

	//define all offsets and lengths
	private final static int COMPT_CNTY_NO_OFFSET = 0;
	private final static int COMPT_CNTY_NO_LENGTH = 3;
	private final static int FUNDS_PYMNT_DATE_OFFSET = 3;
	private final static int FUNDS_PYMNT_DATE_LENGTH = 8;
	private final static int FUNDS_RCVD_DATE_OFFSET = 11;
	private final static int FUNDS_RCVD_DATE_LENGTH = 8;
	private final static int ACCNT_NO_CD_OFFSET = 19;
	private final static int ACCNT_NO_CD_LENGTH = 1;
	private final static int TRACE_NO_OFFSET = 20;
	private final static int TRACE_NO_LENGTH = 9;
	private final static int TRANS_EMP_ID_OFFSET = 29;
	private final static int TRANS_EMP_ID_LENGTH = 7;
	private final static int OFC_ISSUANCE_NO_OFFSET = 36;
	private final static int OFC_ISSUANCE_NO_LENGTH = 3;
	private final static int PYMNT_TYPE_CD_OFFSET = 39;
	private final static int PYMNT_TYPE_CD_LENGTH = 2;
	private final static int PYMNT_STATUS_CD_OFFSET = 41;
	private final static int PYMNT_STATUS_CD_LENGTH = 1;
	private final static int CK_NO_OFFSET = 42;
	private final static int CK_NO_LENGTH = 7;
	private final static int FUNDS_RPT_DATE_OFFSET = 49;
	private final static int FUNDS_RPT_DATE_LENGTH = 8;
	private final static int RPTNG_DATE_OFFSET = 57;
	private final static int RPTNG_DATE_LENGTH = 8;
	private final static int PYMNT_AMT_OFFSET = 65;
	private final static int PYMNT_AMT_LENGTH = 11;
	private final static int PYMNT_AMT_DECIMAL = 2;
	private final static int FUNDS_CAT_OFFSET = 76;
	private final static int FUNDS_CAT_LENGTH = 14;
	private final static int OUTPUT_LENGTH_OFFSET = 115;
	private final static int OUTPUT_LENGTH_LENGTH = 5;

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
	public Vector setMultipleFundsPaymentDataFromMf(String asMfFundsPaymentResponse)
		throws RTSException
	{
		//create the return object 
		Vector laFundsPaymentDataContainer = new Vector();
		MfAccess laMFAccess = new MfAccess();

		//Get the number of elements 
		//	(the number set in Header record is not correct)
		final int NUMBER_OF_ELEMENTS =
			Integer.parseInt(
				asMfFundsPaymentResponse.substring(
					OUTPUT_LENGTH_OFFSET,
					OUTPUT_LENGTH_OFFSET + OUTPUT_LENGTH_LENGTH))
				/ FUNDS_PAYMENT_RECORD_LENGTH;

		// Remove the header from the response
		asMfFundsPaymentResponse =
			asMfFundsPaymentResponse.substring(HEADER_LENGTH);

		// the offset based on the record that is picked from the
		//  mf response
		int liRecordOffset = 0;

		if (!(asMfFundsPaymentResponse
			.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfFundsPaymentResponse != null))
		{
			for (int i = 0; i < NUMBER_OF_ELEMENTS; i++)
			{
				//set the record offset
				liRecordOffset = i * FUNDS_PAYMENT_RECORD_LENGTH;

				//create Funds Payment data
				FundsPaymentData laFundsPaymentData =
					new FundsPaymentData();

				//set all values from FundsPaymentData
				laFundsPaymentData.setComptCountyNo(
					Integer
						.valueOf(
							laMFAccess.getStringFromZonedDecimal(
								asMfFundsPaymentResponse.substring(
									liRecordOffset
										+ COMPT_CNTY_NO_OFFSET,
									liRecordOffset
										+ COMPT_CNTY_NO_OFFSET
										+ COMPT_CNTY_NO_LENGTH)))
						.toString());

				laFundsPaymentData.setAccountNoCode(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + ACCNT_NO_CD_OFFSET,
							liRecordOffset
								+ ACCNT_NO_CD_OFFSET
								+ ACCNT_NO_CD_LENGTH)));

				laFundsPaymentData.setCheckNo(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + CK_NO_OFFSET,
							liRecordOffset
								+ CK_NO_OFFSET
								+ CK_NO_LENGTH)));

				laFundsPaymentData.setFundsCategory(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + FUNDS_CAT_OFFSET,
							liRecordOffset
								+ FUNDS_CAT_OFFSET
								+ FUNDS_CAT_LENGTH)));

				laFundsPaymentData.setOfcIssuanceNo(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + OFC_ISSUANCE_NO_OFFSET,
							liRecordOffset
								+ OFC_ISSUANCE_NO_OFFSET
								+ OFC_ISSUANCE_NO_LENGTH)));

				laFundsPaymentData.setPaymentStatusCode(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + PYMNT_STATUS_CD_OFFSET,
							liRecordOffset
								+ PYMNT_STATUS_CD_OFFSET
								+ PYMNT_STATUS_CD_LENGTH)));

				laFundsPaymentData.setPaymentTypeCode(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + PYMNT_TYPE_CD_OFFSET,
							liRecordOffset
								+ PYMNT_TYPE_CD_OFFSET
								+ PYMNT_TYPE_CD_LENGTH)));

				laFundsPaymentData.setTransEmpId(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + TRANS_EMP_ID_OFFSET,
							liRecordOffset
								+ TRANS_EMP_ID_OFFSET
								+ TRANS_EMP_ID_LENGTH)));

				laFundsPaymentData.setTraceNo(
					laMFAccess.trimMfString(
						asMfFundsPaymentResponse.substring(
							liRecordOffset + TRACE_NO_OFFSET,
							liRecordOffset
								+ TRACE_NO_OFFSET
								+ TRACE_NO_LENGTH)));

				//set all dates
				laFundsPaymentData.setFundsPaymentDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfFundsPaymentResponse.substring(
										liRecordOffset
											+ FUNDS_PYMNT_DATE_OFFSET,
										liRecordOffset
											+ FUNDS_PYMNT_DATE_OFFSET
											+ FUNDS_PYMNT_DATE_LENGTH)))
							.intValue()));

				if (Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfFundsPaymentResponse.substring(
								liRecordOffset + FUNDS_RCVD_DATE_OFFSET,
								liRecordOffset
									+ FUNDS_RCVD_DATE_OFFSET
									+ FUNDS_RCVD_DATE_LENGTH)))
					.intValue()
					== 0)
				{
					laFundsPaymentData.setFundsReceivedDate(null);
				}
				else
				{
					laFundsPaymentData.setFundsReceivedDate(
						new RTSDate(
							RTSDate.YYYYMMDD,
							Integer
								.valueOf(
									laMFAccess
										.getStringFromZonedDecimal(
										asMfFundsPaymentResponse
											.substring(
											liRecordOffset
												+ FUNDS_RCVD_DATE_OFFSET,
											liRecordOffset
												+ FUNDS_RCVD_DATE_OFFSET
												+ FUNDS_RCVD_DATE_LENGTH)))
								.intValue()));
				}
				laFundsPaymentData.setFundsReportDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfFundsPaymentResponse.substring(
										liRecordOffset
											+ FUNDS_RPT_DATE_OFFSET,
										liRecordOffset
											+ FUNDS_RPT_DATE_OFFSET
											+ FUNDS_RPT_DATE_LENGTH)))
							.intValue()));

				laFundsPaymentData.setReportingDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer
							.valueOf(
								laMFAccess.getStringFromZonedDecimal(
									asMfFundsPaymentResponse.substring(
										liRecordOffset
											+ RPTNG_DATE_OFFSET,
										liRecordOffset
											+ RPTNG_DATE_OFFSET
											+ RPTNG_DATE_LENGTH)))
							.intValue()));

				//set all amounts
				StringBuffer laPymntAmtBuffer =
					new StringBuffer(
						laMFAccess.getStringFromZonedDecimal(
							asMfFundsPaymentResponse.substring(
								liRecordOffset + PYMNT_AMT_OFFSET,
								liRecordOffset
									+ PYMNT_AMT_OFFSET
									+ PYMNT_AMT_LENGTH)));

				if (!(laPymntAmtBuffer.charAt(0) == '-'))
				{
					laPymntAmtBuffer.insert(
						PYMNT_AMT_LENGTH - PYMNT_AMT_DECIMAL,
						'.');
				}
				else
				{
					laPymntAmtBuffer.insert(
						PYMNT_AMT_LENGTH - PYMNT_AMT_DECIMAL + 1,
						'.');
				}
				Dollar laPymntAmt =
					new Dollar(laPymntAmtBuffer.toString());
				laFundsPaymentData.setTotalPaymentAmount(laPymntAmt);

				laFundsPaymentDataContainer.addElement(
					laFundsPaymentData);
			}
		} // end of if
		return laFundsPaymentDataContainer;
	}
}
