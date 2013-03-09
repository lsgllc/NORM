package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaTitleG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/28/2006	Remove G from method name
 * 							modify setTitleDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		10/11/2006	Replace class constants with 
 * 							ApplicationControlConstants and
 * 							MessageConstants
 * 							Clean up todos
 * 							modify setTitleDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge code
 * 							modify setTitleDataFromMF()
 * 							defect 6701, Ver Exempts
 * J Rue		10/20/2006	Add refernce to MfbaTitleT
 * 							modify setTitleDataFromMF
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setTitleDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setTitleDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setTitleDataFromMF()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Title Data on the TxDOT mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	02/02/2007
 * @author	Jeff Rue
 * <br>Creation Date:		09/26/2006 14:00:32
 */
public class MfbaTitleG
{
	/**
	 * 
	 */
	public MfbaTitleG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return TitleData
	 */
	public TitleData setTitleDataFromMF(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		TitleData laTitleData = new TitleData();
		MfbaTitleT laMfbaTitleT = new MfbaTitleT();
		MfbaTitleU laMfbaTitleU = new MfbaTitleU();
		MfbaTitleV laMfbaTitleV = new MfbaTitleV();

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
			laTitleData = laMfbaTitleT.setTitleDataFromMf(
								asMfTtlRegResponse);
			laMfbaTitleT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laTitleData = laMfbaTitleU.setTitleDataFromMf(
								asMfTtlRegResponse);
			laMfbaTitleU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laTitleData = laMfbaTitleV.setTitleDataFromMf(
								asMfTtlRegResponse);
			laMfbaTitleV = null;
		}
		// end defect 9086
		
		return laTitleData;
	}	
}
