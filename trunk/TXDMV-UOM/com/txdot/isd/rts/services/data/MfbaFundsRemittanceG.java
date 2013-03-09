package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaFundsRemittanceG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/26/2007	Add offsets and code
 * 							defect 8983 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Funds/Remittance Data on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	04/26/2007
 * @author	Jeff Rue
 * <br>Creation Date:		04/26/2007 13:19:32
 */
public class MfbaFundsRemittanceG
{

	/**
	 * 
	 */
	public MfbaFundsRemittanceG()
	{
		super();
	}
	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return Vector
	 */
	public Vector setMultipleFundsPaymentDataFromMf(
		String asMfFundsDueResponse)
		throws RTSException
	{
		//Create Data objects
		Vector lvFundsRemittData = new Vector();
		MfbaFundsRemittanceT laMfbaFundsRemittanceT = 
			new MfbaFundsRemittanceT();
		MfbaFundsRemittanceU laMfbaFundsRemittanceU = 
			new MfbaFundsRemittanceU();
		MfbaFundsRemittanceV laMfbaFundsRemittanceV = 
			new MfbaFundsRemittanceV();

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
			lvFundsRemittData = 
				laMfbaFundsRemittanceT.setMultipleFundsDueDataFromMf(
				asMfFundsDueResponse);
			laMfbaFundsRemittanceT = null;
		}
		
		//	U version - Special Plates
		if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			lvFundsRemittData = 
				laMfbaFundsRemittanceU.setMultipleFundsDueDataFromMf(
					asMfFundsDueResponse);
			laMfbaFundsRemittanceU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			lvFundsRemittData = 
				laMfbaFundsRemittanceV.setMultipleFundsDueDataFromMf(
					asMfFundsDueResponse);
			laMfbaFundsRemittanceV = null;
		}
		// end defect 9086
		
		return lvFundsRemittData;
	}		
}
