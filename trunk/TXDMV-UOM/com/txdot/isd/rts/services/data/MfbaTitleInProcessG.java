package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaTitleInProcessG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/28/2006	New class for TIP
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setTitleInProcessDataFromMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setTitleInProcessDataFromMfResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/26/2007	String is passed in, not TtlInPrcs object
 * 							modify setTitleInProcessDataFromMfResponse()
 * 							defect 8983 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setTitleInProcessDataFromMfResponse()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Title In Process on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	04/26/2007
 * @author	Jeff Rue
 * <br>Creation Date:		10/27/2006 09:44:32
 */
 
public class MfbaTitleInProcessG
{
	/**
	 * 
	 */
	public MfbaTitleInProcessG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asTitleInProcessResponse String
	 * @return TitleInProcessData
	 */
	public TitleInProcessData setTitleInProcessDataFromMfResponse(
		String asMfResponse)
	{
		//Create Data objects
		TitleInProcessData laTitleInProcessData = new TitleInProcessData();
		MfbaTitleInProcessT laMfbaTIPT = new MfbaTitleInProcessT();
		MfbaTitleInProcessU laMfbaTIPU = new MfbaTitleInProcessU();
		MfbaTitleInProcessV laMfbaTIPV = new MfbaTitleInProcessV();

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
			laTitleInProcessData = 
				laMfbaTIPT.setTitleInProcessDataFromMfResponse(
					asMfResponse);
			laMfbaTIPT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laTitleInProcessData = laMfbaTIPU.
				setTitleInProcessDataFromMfResponse(
					asMfResponse);
			laMfbaTIPU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laTitleInProcessData = laMfbaTIPV.
				setTitleInProcessDataFromMfResponse(
					asMfResponse);
			laMfbaTIPV = null;
		}
		// end defect 9086
		
		return laTitleInProcessData;
	}	
}
