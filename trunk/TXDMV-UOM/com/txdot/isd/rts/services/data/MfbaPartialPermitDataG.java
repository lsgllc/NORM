package com.txdot.isd.rts.services.data;

import java.util.Vector;

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
 * MfbaPartialPermitDataG.java
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
 * Set path for CICS transactions Partial Permits 
 *
 * @version	6.5.0 			06/08/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:		06/08/2010 16:19:17 
 */
public class MfbaPartialPermitDataG
{

	/**
	 * MfbaPartialPermitDataG.java Constructor
	 * 
	 */
	public MfbaPartialPermitDataG()
	{
		super();
	}

	/**
	 * Set Vector of Partial Permit Data according to Version. 
	 *
	 * @param asMfResponse
	 * @return Vector
	 */
	public Vector setMfPartialPermitDatafromMfResponse(String asMfResponse)
	{

		//Create Data objects
		// T Version 
		MfbaPartialPermitDataT laMfbaPartialPermitDataT =
			new MfbaPartialPermitDataT();
		MfbaPartialPermitDataU laMfbaPartialPermitDataU =
			new MfbaPartialPermitDataU();

		MfbaPartialPermitDataV laMfbaPartialPermitDataV =
			new MfbaPartialPermitDataV();
		Vector laPartialDataContainer = new Vector();
		// T version  
		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			laPartialDataContainer =
				laMfbaPartialPermitDataT
					.setMFPartialPermitDataFromMfResponse(
					asMfResponse);
			laMfbaPartialPermitDataT = null;
		}
		// U version  
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laPartialDataContainer =
				laMfbaPartialPermitDataU
					.setMFPartialPermitDataFromMfResponse(
					asMfResponse);
			laMfbaPartialPermitDataU = null;
		}

		//	V version  
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laPartialDataContainer =
				laMfbaPartialPermitDataV
					.setMFPartialPermitDataFromMfResponse(
					asMfResponse);
			laMfbaPartialPermitDataV = null;
		}

		return laPartialDataContainer;
	}

}
