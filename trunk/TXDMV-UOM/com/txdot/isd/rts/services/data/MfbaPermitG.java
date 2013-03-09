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
 * MfbaPermitG.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/08/2010	Created
 * 							defect 10492 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions PermitData 
 *
 * @version	6.5.0			06/08/2010 
 * @author	Kathy Harrell
 * <br>Creation Date:		06/08/2010 18:09:17 
 */
public class MfbaPermitG
{

	/**
	 * MfbaPermitG.java Constructor
	 * 
	 */
	public MfbaPermitG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfResponse String
	 * @return PermitData
	 */
	public PermitData setPermitDataFromMfResponse(String asMfResponse)
	{
		//Create Data objects
		PermitData laPrmtData = new PermitData();
		MfbaPermitT laMfbaPermitT = new MfbaPermitT();
		MfbaPermitU laMfbaPermitU = new MfbaPermitU();
		MfbaPermitV laMfbaPermitV = new MfbaPermitV();

		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			laPrmtData =
				laMfbaPermitT.setPermitDataFromMfResponse(asMfResponse);
			laMfbaPermitT = null;
		}
		//	U version - Special Plates
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laPrmtData =
				laMfbaPermitU.setPermitDataFromMfResponse(asMfResponse);
				
			laMfbaPermitU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laPrmtData =
				laMfbaPermitV.setPermitDataFromMfResponse(asMfResponse);
			laMfbaPermitV = null;
		}
		return laPrmtData;
	}
}
