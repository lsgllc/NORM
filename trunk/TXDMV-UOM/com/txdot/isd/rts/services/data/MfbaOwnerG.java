package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaOwnerG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/28/2006	Remove G from method name
 * 							modify setRegistrationDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		10/11/2006	Replace class constants with 
 * 							ApplicationControlConstants and
 * 							MessageConstants
 * 							modify setOwnerDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge code
 * 							modify setOwnerDataFromMF()
 * 							defect 6701, Ver Exempts
 * J Rue		10/20/2006	Add reference to MfbaOwnerV
 * 							modify setOwnerDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setOwnerDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setOwnerDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Owner Data on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	02/02/2007
 * @author	Jeff Rue
 * <br>Creation Date:		09/27/2006 14:00:32
 */
public class MfbaOwnerG
{
	/**
	 * 
	 */
	public MfbaOwnerG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return OwnerData
	 */
	public OwnerData setOwnerDataFromMF(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		OwnerData laOwnerData = new OwnerData();
		MfbaOwnerT laMfbaOwnerT = new MfbaOwnerT();
		MfbaOwnerU laMfbaOwnerU = new MfbaOwnerU();
		MfbaOwnerV laMfbaOwnerV = new MfbaOwnerV();

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
			laOwnerData = 
				laMfbaOwnerT.setOwnerDataFromMF(
					asMfTtlRegResponse);
			laMfbaOwnerT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laOwnerData = 
				laMfbaOwnerU.setOwnerDataFromMF(
					asMfTtlRegResponse);
			laMfbaOwnerU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laOwnerData = 
				laMfbaOwnerV.setOwnerDataFromMF(
					asMfTtlRegResponse);
			laMfbaOwnerV = null;
		}
		// end defect 9086
		
		// defect 6701
		//	Check has been moved to server.MfAccess.getMfResponce()
//		else
//		{
//			// Turn off throws RTSException
//			// Turn off throw RTSException until Client can resolve RTS 
//			// Mainframe Down
//			//
//			//	Throw RTSException to set RTS Mainframe Down		
////			throw new RTSException(
////				RTSException.MF_DOWN,
////				SystemProperty.getMFInterfaceVersionCode()
////					+ MessageConstants.SE_MF_VERSION_NOT_IMPLEMENTED,
////				MessageConstants.SF_MFACCESS_ERROR);
//		}
//		// end defect 6701

		return laOwnerData;
	}	
}

