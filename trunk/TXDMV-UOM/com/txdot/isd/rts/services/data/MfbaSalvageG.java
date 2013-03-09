package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaSalvageG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/02/2006	Remove G from method name
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		10/11/2006	Replace class constants with 
 * 							ApplicationControlConstants and
 * 							MessageConstants
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge code
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setMultipleSalvageDataFromMf()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Salvage Data on the TxDOT mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	04/27/2007
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:00:32
 */
public class MfbaSalvageG
{
	/**
	 * 
	 */
	public MfbaSalvageG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return Vector
	 */
	public Vector setMultipleSalvageDataFromMf(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		MfbaSalvageT laMfbaSalvageT = new MfbaSalvageT();
		MfbaSalvageU laMfbaSalvageU = new MfbaSalvageU();
		MfbaSalvageV laMfbaSalvageV = new MfbaSalvageV();
		Vector laSalvageContainer = new Vector();
		

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
		if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			laSalvageContainer = 
				laMfbaSalvageT.setMultipleSalvageDataFromMf(
					asMfTtlRegResponse);
			laMfbaSalvageT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laSalvageContainer = 
			laMfbaSalvageU.setMultipleSalvageDataFromMf(
					asMfTtlRegResponse);
			laMfbaSalvageU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laSalvageContainer = 
			laMfbaSalvageV.setMultipleSalvageDataFromMf(
					asMfTtlRegResponse);
			laMfbaSalvageV = null;
		}
		// end defect 9086

		return laSalvageContainer;
	}	
}
