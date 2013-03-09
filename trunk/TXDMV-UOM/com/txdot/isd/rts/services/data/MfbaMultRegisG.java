package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaMultRegisG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/20/2006	Add reference to MfbaMultRegisV
 * 							modify setMultipleRegistrationDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setMultipleRegistrationDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setMultipleRegistrationDataFromMf()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setMultipleRegistrationDataFromMf()
 * 							defect 8984 Ver Special Plates
 * J Rue		05/06/2008	Make call to set RegPltNo and RmvRegPltNo if
 * 							RmvPltCd > 0 
 * 							modify setMultipleRegistrationDataFromMf()
 * 							defect 9630 Ver 3_AMIGOS_PH_B
 * J Rue		11/12/2008	Move defect 9630 so all MF Versions can 
 * 							reset RegPltNo to "NOPLATE"
 * 							modify setMultipleRegistrationDataFromMf()
 * 							defect 9833 Ver Defect_POS_B
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS Mulp Regis Data From MF on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Defect_POS_B	11/12/2008
 * @author	Jeff Rue
 * <br>Creation Date:		10/17/2006 14:00:32
 */
public class MfbaMultRegisG 
{
	
	/**
	 * 
	 */
	public MfbaMultRegisG() 
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return Vector
	 */
	public Vector setMultipleRegistrationDataFromMf(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		Vector lvRtnData = new Vector();
		MfbaMultRegisT laMfbaMultRegisT = new MfbaMultRegisT();
		MfbaMultRegisU laMfbaMultRegisU = new MfbaMultRegisU();
		MfbaMultRegisV laMfbaMultRegisV = new MfbaMultRegisV();
		RegistrationData laRegistrationData = new RegistrationData();

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
			lvRtnData = 
				laMfbaMultRegisT.setMultipleRegistrationDataFromMf(
					asMfTtlRegResponse);
			laMfbaMultRegisT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			lvRtnData = 
				laMfbaMultRegisU.setMultipleRegistrationDataFromMf(
					asMfTtlRegResponse);
			laMfbaMultRegisU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			lvRtnData = 
				laMfbaMultRegisV.setMultipleRegistrationDataFromMf(
					asMfTtlRegResponse);
			laMfbaMultRegisV = null;
			// defect 9630
			// Save RegPltNo to RmvRegPltNo and Reset RegPltNo = "NOPLATE"
			//	 if RmvPltCd > 0
//			lvRtnData = laRegistrationData.resetFieldsMultRegis(lvRtnData);
			// end defect 9630
		}
		// defect 9833
		// Move defect 9630 from inside "V" version to all versions
		lvRtnData = laRegistrationData.resetFieldsMultRegis(lvRtnData);
		// end defect 9833
		// end defect 9086
		return lvRtnData;
	}	
}
