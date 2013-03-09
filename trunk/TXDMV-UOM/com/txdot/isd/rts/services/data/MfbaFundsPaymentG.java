package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaFundsPaymentG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/11/2006	Replace class constants with 
 * 							ApplicationControlConstants and
 * 							MessageConstants
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 6701 Ver EXEMPT
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge code
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 6701, Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		02/02/2007	Add MFVersion U code. 
 * 							modify setOwnerDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/26/2007	remove non-reference variables and inports
 * 							defect 8983 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Funds/Payment Data on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	04/26/2007
 * @author	Jeff Rue
 * <br>Creation Date:		10/11/2006 08:58:32
 */
public class MfbaFundsPaymentG
{
	/**
	 * 
	 */
	public MfbaFundsPaymentG()
	{
		super();
	}

	public static void main(String[] args)
	{
	}
	
	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return Vector
	 */
	public Vector setMultipleFundsPaymentDataFromMf(
		String asMfFundsPaymentResponse)
		throws RTSException
	{
		//Create Data objects
		Vector lvFundsPaymentData = new Vector();
		MfbaFundsPaymentT laMfbaFundsPaymentT = new MfbaFundsPaymentT();
		MfbaFundsPaymentU laMfbaFundsPaymentU = new MfbaFundsPaymentU();
		MfbaFundsPaymentV laMfbaFundsPaymentV = new MfbaFundsPaymentV();

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
			lvFundsPaymentData = 
				laMfbaFundsPaymentT.setMultipleFundsPaymentDataFromMf(
					asMfFundsPaymentResponse);
			laMfbaFundsPaymentT = null;
		}
		
		//	U version - Special Plates
		if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			lvFundsPaymentData = 
				laMfbaFundsPaymentU.setMultipleFundsPaymentDataFromMf(
					asMfFundsPaymentResponse);
			laMfbaFundsPaymentU = null;
		}
		
		//	V version - Prior to Exempts (Not used by production)
		if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			lvFundsPaymentData = 
				laMfbaFundsPaymentV.setMultipleFundsPaymentDataFromMf(
					asMfFundsPaymentResponse);
			laMfbaFundsPaymentV = null;
		}
		// end defect 9086

		return lvFundsPaymentData;
	}		
}
