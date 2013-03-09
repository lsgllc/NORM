package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaRegistrationG.java
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
 * 							modify setRegistrationDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge code
 * 							modify setRegistrationDataFromMF()
 * 							defect 6701, Ver Exempts
 * J Rue		10/20/2006	Add reference to MfbaRegistrationV
 * 							modify setRegistrationDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setRegistrationDataFromMF()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setRegistrationDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setRegistrationDataFromMF()
 * 							defect 8984 Ver Special Plates
 * J Rue		05/06/2008	Make call to set RegPltNo and RmvRegPltNo if
 * 							RmvPltCd > 0 
 * 							modify setRegistrationDataFromMF()
 * 							defect 9630 Ver 3_AMIGOS_PH_B
 * J Rue		11/12/2008	Move defect 9630 so all MF Versions can 
 * 							reset RegPltNo to "NOPLATE"
 * 							modify setRegistrationDataFromMF()
 * 							defect 9833 Ver Defect_POS_B
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Registration Data on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Defect_POS_B	11/12/2008
 * @author	Jeff Rue
 * <br>Creation Date:		09/27/2006 14:00:32
 */
public class MfbaRegistrationG
{
	/**
	 * 
	 */
	public MfbaRegistrationG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return RegistrationData
	 */
	public RegistrationData setRegistrationDataFromMF(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		RegistrationData laRegistrationData = new RegistrationData();
		MfbaRegistrationT laMfbaRegistrationT = new MfbaRegistrationT();
		MfbaRegistrationU laMfbaRegistrationU = new MfbaRegistrationU();
		MfbaRegistrationV laMfbaRegistrationV = new MfbaRegistrationV();

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
			laRegistrationData = 
				laMfbaRegistrationT.setRegistrationDataFromMf(
					asMfTtlRegResponse);
			laMfbaRegistrationT = null;
		}
		// end defect 9086
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laRegistrationData = 
				laMfbaRegistrationU.setRegistrationDataFromMf(
					asMfTtlRegResponse);
			laMfbaRegistrationU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laRegistrationData = 
				laMfbaRegistrationV.setRegistrationDataFromMf(
					asMfTtlRegResponse);
			laMfbaRegistrationV = null;
//			// defect 9630
//			// Save RegPltNo to RmvRegPltNo and Reset RegPltNo = "NOPLATE"
//			//	 if RmvPltCd > 0
//			laRegistrationData.resetFields(laRegistrationData);
//			// end defect 9630
		}
		// defect 9833
		// Move defect 9630 from inside "V" version to all versions
		laRegistrationData.resetFields(laRegistrationData);
		// end defect 9833
		// end defect 9086

		return laRegistrationData;
	}	
}
