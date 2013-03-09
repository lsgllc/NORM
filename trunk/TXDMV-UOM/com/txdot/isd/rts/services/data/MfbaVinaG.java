package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaVinaG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/16/2006	Update JavaDocs
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Standardize error message for CICS versions 
 * 							not implamented
 * 							modify setMfVehicleDataFromVINAResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge
 * 							modify setMfVehicleDataFromVINAResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Add reference to MfbaVinaG
 * 							modify setMfVehicleDataFromVINAResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setMfVehicleDataFromVINAResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setMfVehicleDataFromVINAResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setMfVehicleDataFromVINAResponse()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions VINA Data on the TxDOT mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	04/27/2007
 * @author	Marx Rajangam
 * <br>Creation Date:		10/16/2006 11:07:32
 */
public class MfbaVinaG 
{
	/**
	 * 
	 */
	public MfbaVinaG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return MFVehicleData
	 */
	public MFVehicleData setMfVehicleDataFromVINAResponse(
		String asMfTtlRegResponse)
		throws RTSException
	{
		//Create Data objects
		MFVehicleData laMFVehicleData = new MFVehicleData();
		MfbaVinaT laMfbaVinaT = new MfbaVinaT();
		MfbaVinaU laMfbaVinaU = new MfbaVinaU();
		MfbaVinaV laMfbaVinaV = new MfbaVinaV();

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
			laMFVehicleData = laMfbaVinaT.
				setMfVehicleDataFromVINAResponse(asMfTtlRegResponse);
			laMfbaVinaT = null;
		}

		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laMFVehicleData = laMfbaVinaU.
				setMfVehicleDataFromVINAResponse(asMfTtlRegResponse);
			laMfbaVinaU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laMFVehicleData = laMfbaVinaV.
				setMfVehicleDataFromVINAResponse(asMfTtlRegResponse);
			laMfbaVinaV = null;
		}
		// end defect 9086
		
		return laMFVehicleData;
	}		
}
