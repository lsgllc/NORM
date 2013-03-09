package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaPartialSpclRegisDataG.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		01/31/2007	New process 
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 6701 Ver 5.3.0
 * J Rue		02/02/2007	Clean up comments 
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setMFPartialSpclRegisDataFromMfResponse()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Partial Special Plates Data on the 
 * TxDOT mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates		04/27/2007
 * @author	Jeff Rue
 * <br>Creation Date:			01/31/2007 09:00:32
 */
public class MfbaPartialSpclPltDataG
{
	/**
	 * 
	 */
	public MfbaPartialSpclPltDataG()
	{
		super();
	}
	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return Vector
	 */
	public Vector setMFPartialSpclRegisDataFromMfResponse(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		MfbaPartialSpclPltDataT laMfbaPrtlSpclRegisDataT =
			new MfbaPartialSpclPltDataT();
		MfbaPartialSpclPltDataU laMfbaPrtlSpclRegisDataU =
			new MfbaPartialSpclPltDataU();
		MfbaPartialSpclPltDataV laMfbaPrtlSpclRegisDataV =
			new MfbaPartialSpclPltDataV();
		Vector lvPartialDataContainer = new Vector();

		// defect 9086
		//	Add MFVersion U data format process
		//
		// Determine processing path from CICS/PGM version
		//
		//	T version - Exempts/TERPS	 (Not used by production)
		//  U version - Special Plates
		//  V version - Prior to Exempts (Not used by production)
		//
		//	T version - Exempts/TERPS (Not used by production)
		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			lvPartialDataContainer =
				laMfbaPrtlSpclRegisDataT
					.setMFPartialDataFromMfResponse(
					asMfTtlRegResponse);
			laMfbaPrtlSpclRegisDataT = null;
		}

		//	U version - Special Plates
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			lvPartialDataContainer =
				laMfbaPrtlSpclRegisDataU
					.setMFPartialDataFromMfResponse(
					asMfTtlRegResponse);
			laMfbaPrtlSpclRegisDataU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			lvPartialDataContainer =
				laMfbaPrtlSpclRegisDataV
					.setMFPartialDataFromMfResponse(
					asMfTtlRegResponse);
			laMfbaPrtlSpclRegisDataV = null;
		}
		// end defect 9086

		return lvPartialDataContainer;
	}
}
