package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaPartialDataG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/02/2006	Remove G from method name
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 6701 Ver 5.3.0
 * J Rue		10/11/2006	Replace class constants with 
 * 							ApplicationControlConstants and
 * 							MessageConstants
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 6701 Ver EXEMPT
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							Clean up merge code
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 6701, Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		01/31/2007	Clean up JavaDoc
 * 							defect 9086 Ver Special Plates
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up comments 
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/06/2008	Make call to set RegPltNo and RmvRegPltNo if
 * 							RmvPltCd > 0 
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9630 Ver 3_AMIGOS_PH_B
 * J Rue		11/12/2008	Move defect 9630 so all MF Versions can 
 * 							reset RegPltNo to "NOPLATE"
 * 							modify setMFPartialDataFromMfResponse()
 * 							defect 9833 Ver Defect_POS_B
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Partial Data on the TxDOT mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Defect_POS_B	11/12/2008
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:00:32
 */
public class MfbaPartialDataG
{
	public MfbaPartialDataG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return Vector
	 */
	public Vector setMFPartialDataFromMfResponse(
		String asMfTtlRegResponse)
	{
		//Create Data objects
		MfbaPartialDataT laMfbaPartialDataT = new MfbaPartialDataT();
		MfbaPartialDataU laMfbaPartialDataU = new MfbaPartialDataU();
		MfbaPartialDataV laMfbaPartialDataV = new MfbaPartialDataV();
		Vector laPartialDataContainer = new Vector();
		TitleData laTitledata = new TitleData();
		

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
			laPartialDataContainer = 
				laMfbaPartialDataT.setMFPartialDataFromMfResponse(
					asMfTtlRegResponse);
			laMfbaPartialDataT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laPartialDataContainer = 
				laMfbaPartialDataU.setMFPartialDataFromMfResponse(
					asMfTtlRegResponse);
			laMfbaPartialDataU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laPartialDataContainer = 
				laMfbaPartialDataV.setMFPartialDataFromMfResponse(
					asMfTtlRegResponse);
			laMfbaPartialDataV = null;
			// defect 9630
			// Save RegPltNo to RmvRegPltNo and Reset RegPltNo = "NOPLATE"
			//	 if RmvPltCd > 0
//			laPartialDataContainer = 
//				laTitledata.resetFields(laPartialDataContainer);
			// end defect 9630
		}
		// defect 9833
		// Move defect 9630 from inside "V" version to all versions
		laPartialDataContainer = 
			laTitledata.resetFields(laPartialDataContainer);
		// end defect 9833
		// end defect 9086

		return laPartialDataContainer;
	}	

	
}
