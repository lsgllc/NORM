package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.ApplicationControlConstants;

/*
 *
 * MfbaOwnerFromMFOwnerG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/27/2007	Add code to call MFVersionCd data procedures
 * 							defect 8983 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into . 
 * Used by the server side business layer - Special Plates.
 *
 * @version	Special Plates	04/27/2007
 * @author	Jeff Rue
 * <br>Creation Date:		04/26/2007 18:34:32
 */
public class MfbaOwnerFromMFOwnerG
{

	/**
	 * 
	 */
	public MfbaOwnerFromMFOwnerG()
	{
		super();
	}
	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return OwnerData
	 */
	public OwnerData setOwnerDataFromMfOwner(String asMfResponse)
	{
		//Create Data objects
		OwnerData laOwnerData = new OwnerData();
		MfbaOwnerFromMFOwnerT laMfbaOwnerFromMfT =
			new MfbaOwnerFromMFOwnerT();
		MfbaOwnerFromMFOwnerU laMfbaOwnerFromMfU =
			new MfbaOwnerFromMFOwnerU();
		MfbaOwnerFromMFOwnerV laMfbaOwnerFromMfV =
			new MfbaOwnerFromMFOwnerV();

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
			laOwnerData =
				laMfbaOwnerFromMfT.setOwnerDataFromMfOwner(
					asMfResponse);
			laMfbaOwnerFromMfT = null;
		}

		//	U version - Special Plates
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laOwnerData =
				laMfbaOwnerFromMfU.setOwnerDataFromMfOwner(
					asMfResponse);
			laMfbaOwnerFromMfU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laOwnerData =
				laMfbaOwnerFromMfV.setOwnerDataFromMfOwner(
					asMfResponse);
			laMfbaOwnerFromMfV = null;
		}
		// end defect 9086

		return laOwnerData;
	}
}
