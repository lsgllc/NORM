package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 * MfbaInvoiceG.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/14/2006	New Class
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify formatInventoryInvoiceData()
 * 							defect 6701 Ver Exempts
 * J Rue		02/02/2007	Add MFVersion U code. 
 * 							modify formatInventoryInvoiceData()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/26/2007	Clean up code
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify formatInventoryInvoiceData()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * This class directs the parsing of the MainFrame response from 
 * retreiving an Inventory Invoice.
 *
 * @version	Special Plates	04/26/2007
 * @author	Ray Rowehl
 * <br>Creation Date:		10/14/2006 10:31:00
 */
public class MfbaInventoryInvoiceG
{
	/**
	 * Check which version of CICS - Cobol programs we are communicating
	 * with and format the data based on the decision.
	 * 
	 * @param aaMfAccess MfAccess
	 * @param asMfInvoiceAckResponse String
	 * @param asMfInvoiceItemsResponse String
	 * @return MFInventoryAllocationData
	 * @throws RTSException
	 */
	public MFInventoryAllocationData formatInventoryInvoiceData(
			MfAccess aaMfAccess,
			String asMfInvoiceAckResponse,
			String asMfInvoiceItemsResponse)
			throws RTSException
		{
			MFInventoryAllocationData laMFInventoryAllocationData = null;
			
			// defect 9086
			//	Add MFVersion U data format process
			//
			// Determine processing path from CICS/PGM version
			//
			//	T version - Exempts/TERPS
			//  U version - Special Plates
			//  V version - Prior to Exempts (Not used by production)
			//
			//	T version - Exempts/TERPS
			if (SystemProperty
				.getMFInterfaceVersionCode()
				.equals(ApplicationControlConstants.SC_MFA_VERSION_T))
			{
				// Use the T Formatter
				MfbaInventoryInvoiceT laInventoryInvoiceT =
					new MfbaInventoryInvoiceT(aaMfAccess);
				laMFInventoryAllocationData =
				laInventoryInvoiceT.formatInventoryInvoiceData(
				asMfInvoiceAckResponse,
				asMfInvoiceItemsResponse);

			}
			
			//	U version - Special Plates
			else if (
				SystemProperty.getMFInterfaceVersionCode().equals(
					ApplicationControlConstants.SC_MFA_VERSION_U))
			{
				MfbaInventoryInvoiceU laInventoryInvoiceU =
					new MfbaInventoryInvoiceU(aaMfAccess);
				laMFInventoryAllocationData =
				laInventoryInvoiceU.formatInventoryInvoiceData(
				asMfInvoiceAckResponse,
				asMfInvoiceItemsResponse);

			}
			
			//	V version - Prior to Exempts (Not used by production)
			else if (
				SystemProperty.getMFInterfaceVersionCode().equals(
					ApplicationControlConstants.SC_MFA_VERSION_V))
			{
				// Use the V Formatter
				MfbaInventoryInvoiceV laInventoryInvoiceT =
					new MfbaInventoryInvoiceV(aaMfAccess);
				laMFInventoryAllocationData =
				laInventoryInvoiceT.formatInventoryInvoiceData(
				asMfInvoiceAckResponse,
				asMfInvoiceItemsResponse);
			}
			// end defect 9086

			return laMFInventoryAllocationData;
		}
}
