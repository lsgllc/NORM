package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.APPCHeader;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 * MfbaInventoryInvoiceU.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/14/2006	New Class
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Update JavaDoc
 * 							defect 9086 Ver Special Plates
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								formatInventoryInvoiceData()
 * 							defect 9833 Ver ELT_MfAccess
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
 * M Reyes		12/31/2010  Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * This class parses the MainFrame response for retrieving an Inventory 
 * Used by the server side business layer - Exempts/TERPS. 
 *
 * @version	6.7.0		12/31/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		10/14/2006 12:34:00
 */
public class MfbaInventoryInvoiceU
{
	// define location of number of records
	private static final int IL_VI_NO_OF_RECS_OFFSET = 145;
	private static final int IL_VI_NO_OF_RECS_LENGTH = 3;
	private static final int IL_VI_LENGTH_OF_INVCITMS_RECORD = 39;

	// define the lengths and offsets of Invoice Acknowledgement 
	// fields
	private static final int IL_VIA_INVC_NO_OFFSET = 0;
	private static final int IL_VIA_INVC_NO_LENGTH = 6;
	private static final int IL_VIA_OFC_ISSUANCE_NO_OFFSET = 6;
	private static final int IL_VIA_OFC_ISSUANCE_NO_LENGTH = 3;
	private static final int IL_VIA_OFC_ISSUANCE_CD_OFFSET = 9;
	private static final int IL_VIA_OFC_ISSUANCE_CD_LENGTH = 2;
	private static final int IL_VIA_INV_ITM_ORDER_DATE_OFFSET = 11;
	private static final int IL_VIA_INV_ITM_ORDER_DATE_LENGTH = 8;
	private static final int IL_VIA_SPCL_SRVC_EMP_ID_OFFSET = 19;
	private static final int IL_VIA_SPCL_SRVC_EMP_ID_LENGTH = 7;

	// define all lengths and offsets to extract data for 
	// Items Response 
	private static final int IL_VIR_ITM_CD_OFFSET = 0;
	private static final int IL_VIR_ITM_CD_LENGTH = 8;
	private static final int IL_VIR_INV_ITM_YR_OFFSET = 8;
	private static final int IL_VIR_INV_ITM_YR_LENGTH = 4;
	private static final int IL_VIR_INVC_ITM_BEG_NO_OFFSET = 12;
	private static final int IL_VIR_INVC_ITM_BEG_NO_LENGTH = 10;
	private static final int IL_VIR_INVC_ITM_END_NO_OFFSET = 22;
	private static final int IL_VIR_INVC_ITM_END_NO_LENGTH = 10;
	private static final int IL_VIR_INVC_ITM_ORDER_QTY_OFFSET = 32;
	private static final int IL_VIR_INVC_ITM_ORDER_QTY_LENGTH = 7;

	// MfAccess Object for using utility methods
	private MfAccess caMfAccess;

	/**
	 * MfbaInventoryInvoiceV.java Constructor
	 * 
	 * @param aaMfAccess
	 */
	public MfbaInventoryInvoiceU(MfAccess aaMfAccess)
	{
		caMfAccess = aaMfAccess;
	}

	/**
	 * Parse the Mainframe response to an Inventory Invoice retrieval 
	 * for CICS - Cobol Version T.
	 * 
	 * @param asMfInvoiceAckResponse String
	 * @param asMfInvoiceItemsResponse String
	 * @return MFInventoryAllocationData
	 * @throws RTSException
	 */
	public MFInventoryAllocationData formatInventoryInvoiceData(
		String asMfInvoiceAckResponse,
		String asMfInvoiceItemsResponse)
		throws RTSException
	{
		MFInventoryAllocationData laMFInventoryAllocationData = null;

		if (asMfInvoiceAckResponse != null
			&& asMfInvoiceItemsResponse != null)
		{
			laMFInventoryAllocationData =
				new MFInventoryAllocationData();

			// create the result objects
			MFInventoryAckData laMFInventoryAckData =
				new MFInventoryAckData();
			Vector laInventoryAllocationUIDataContainer = new Vector();

			// get appc header and the length of header
			APPCHeader laAppcHeader = new APPCHeader();
			final int HEADER_LENGTH =
				laAppcHeader.getAPPCHeaderRecord().length;

			//get the length of output from MF
			final int liOutputLengthOffset =
				laAppcHeader.getHDOUTOFFSET();
			final int liOutputLengthLength = laAppcHeader.getHDOUTLEN();

			int liOutputLength =
				Integer.parseInt(
					asMfInvoiceAckResponse.substring(
						liOutputLengthOffset,
						liOutputLengthOffset + liOutputLengthLength));

			int liNoOfRecs = 0;

			if (liOutputLength != 0)
			{
				if (asMfInvoiceAckResponse != null
					&& asMfInvoiceAckResponse.length() > HEADER_LENGTH)
				{
					// chop off the cics header section
					asMfInvoiceAckResponse =
						asMfInvoiceAckResponse.substring(HEADER_LENGTH);

					laMFInventoryAckData.setInvcNo(
						caMfAccess.trimMfString(
							asMfInvoiceAckResponse.substring(
								IL_VIA_INVC_NO_OFFSET,
								IL_VIA_INVC_NO_OFFSET
									+ IL_VIA_INVC_NO_LENGTH)));

					// start parsing the Invoice Header
					laMFInventoryAckData.setInvItmOrderDt(
						Integer
							.valueOf(
								caMfAccess.getStringFromZonedDecimal(
									asMfInvoiceAckResponse.substring(
										IL_VIA_INV_ITM_ORDER_DATE_OFFSET,
										IL_VIA_INV_ITM_ORDER_DATE_OFFSET
											+ IL_VIA_INV_ITM_ORDER_DATE_LENGTH)))
							.intValue());

					laMFInventoryAckData.setOfcIssuanceNo(
						Integer
							.valueOf(
								caMfAccess.getStringFromZonedDecimal(
									asMfInvoiceAckResponse.substring(
										IL_VIA_OFC_ISSUANCE_NO_OFFSET,
										IL_VIA_OFC_ISSUANCE_NO_OFFSET
											+ IL_VIA_OFC_ISSUANCE_NO_LENGTH)))
							.intValue());

					laMFInventoryAckData.setOfcIssuanceCd(
						Integer
							.valueOf(
								caMfAccess.getStringFromZonedDecimal(
									asMfInvoiceAckResponse.substring(
										IL_VIA_OFC_ISSUANCE_CD_OFFSET,
										IL_VIA_OFC_ISSUANCE_CD_OFFSET
											+ IL_VIA_OFC_ISSUANCE_CD_LENGTH)))
							.intValue());

					laMFInventoryAckData.setSpclSrvcEmpId(
						caMfAccess.trimMfString(
							asMfInvoiceAckResponse.substring(
								IL_VIA_SPCL_SRVC_EMP_ID_OFFSET,
								IL_VIA_SPCL_SRVC_EMP_ID_OFFSET
									+ IL_VIA_SPCL_SRVC_EMP_ID_LENGTH)));
				}

				// Parse out the detail items of the invoice
				if (asMfInvoiceItemsResponse != null
					&& asMfInvoiceItemsResponse.length() > HEADER_LENGTH)
				{
					liNoOfRecs =
						Integer
							.valueOf(
								caMfAccess.getStringFromZonedDecimal(
									asMfInvoiceItemsResponse.substring(
										IL_VI_NO_OF_RECS_OFFSET,
										IL_VI_NO_OF_RECS_OFFSET
											+ IL_VI_NO_OF_RECS_LENGTH)))
							.intValue();

					asMfInvoiceItemsResponse =
						asMfInvoiceItemsResponse.substring(
							HEADER_LENGTH);

					// loop through the items and parse them out.
					for (int i = 0; i < liNoOfRecs; i++)
					{
						int liRecordOffset =
							i * IL_VI_LENGTH_OF_INVCITMS_RECORD;

						InventoryAllocationUIData laInventoryAllocationUIData =
							new InventoryAllocationUIData();

						laInventoryAllocationUIData.setItmCd(
							caMfAccess.trimMfString(
								asMfInvoiceItemsResponse.substring(
									liRecordOffset
										+ IL_VIR_ITM_CD_OFFSET,
									liRecordOffset
										+ IL_VIR_ITM_CD_OFFSET
										+ IL_VIR_ITM_CD_LENGTH)));

						laInventoryAllocationUIData.setInvItmYr(
							Integer
								.valueOf(
									caMfAccess
										.getStringFromZonedDecimal(
										asMfInvoiceItemsResponse
											.substring(
											liRecordOffset
												+ IL_VIR_INV_ITM_YR_OFFSET,
											liRecordOffset
												+ IL_VIR_INV_ITM_YR_OFFSET
												+ IL_VIR_INV_ITM_YR_LENGTH)))
								.intValue());

						laInventoryAllocationUIData.setInvItmNo(
							caMfAccess.trimMfString(
								asMfInvoiceItemsResponse.substring(
									liRecordOffset
										+ IL_VIR_INVC_ITM_BEG_NO_OFFSET,
									liRecordOffset
										+ IL_VIR_INVC_ITM_BEG_NO_OFFSET
										+ IL_VIR_INVC_ITM_BEG_NO_LENGTH)));

						laInventoryAllocationUIData.setInvItmEndNo(
							caMfAccess.trimMfString(
								asMfInvoiceItemsResponse.substring(
									liRecordOffset
										+ IL_VIR_INVC_ITM_END_NO_OFFSET,
									liRecordOffset
										+ IL_VIR_INVC_ITM_END_NO_OFFSET
										+ IL_VIR_INVC_ITM_END_NO_LENGTH)));

						laInventoryAllocationUIData.setInvQty(
							Integer
								.valueOf(
									caMfAccess
										.getStringFromZonedDecimal(
										asMfInvoiceItemsResponse
											.substring(
											liRecordOffset
												+ IL_VIR_INVC_ITM_ORDER_QTY_OFFSET,
											liRecordOffset
												+ IL_VIR_INVC_ITM_ORDER_QTY_OFFSET
												+ IL_VIR_INVC_ITM_ORDER_QTY_LENGTH)))
								.intValue());

						laInventoryAllocationUIDataContainer
							.addElement(
							laInventoryAllocationUIData);
					}
				}

				//add the results to the return object
				laMFInventoryAllocationData.setMFInvAckData(
					laMFInventoryAckData);

				// add the vector containing the inventory items to 
				//  return object
				laMFInventoryAllocationData.setInvAlloctnData(
					laInventoryAllocationUIDataContainer);
			}
		}
		return laMFInventoryAllocationData;
	}
}