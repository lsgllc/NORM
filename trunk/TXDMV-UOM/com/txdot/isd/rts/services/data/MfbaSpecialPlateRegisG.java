package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.ApplicationControlConstants;

/*
 *
 * MfbaTitleG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		01/26/2007	Set up call to call the offset process.
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setSpecialPlatesRegisDataFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setSpecialPlatesRegisDataFromMF()
 * 							defect 9086 Special Plates
 * J Rue		04/08/2008	Create a new method to reset fields
 * 							Reset RegPltNo = NOPLATE if DissociateCd > 0
 * 							add resetFields()
 * 							defect 9630 Ver 3_AMIGOS_PH_B
 * J Rue		05/06/2008	Make call to set RegPltNo and RmvRegPltNo if
 * 							RmvPltCd > 0 
 * 							modify setSpecialPlatesRegisDataFromMF()
 * 							defect 9630 Ver 3_AMIGOS_PH_B
 * J Rue		11/12/2008	Move defect 9630 so all MF Versions can 
 * 							reset RegPltNo to "NOPLATE"
 * 							modify setSpecialPlatesRegisDataFromMF()
 * 							defect 9833 Ver Defect_POS_B
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Special Plates Regis Data on the TxDOT
 *  mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Defect_POS_B	11/12/2008
 * @author	Jeff Rue
 * <br>Creation Date:		01/26/2007 14:00:32
 */
public class MfbaSpecialPlateRegisG
{
	/**
	 * 
	 */
	public MfbaSpecialPlateRegisG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfSpclPltRegisResponse String
	 * @return SpecialPlatesRegisData
	 */
	public SpecialPlatesRegisData setSpecialPlatesRegisDataFromMF(String asMfSpclPltRegisResponse)
	{
		//Create Data objects
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();
		MfbaSpecialPltRegisT laMfbaSpclPltsRegisT =
			new MfbaSpecialPltRegisT();
		MfbaSpecialPltRegisU laMfbaSpclPltsRegisU =
			new MfbaSpecialPltRegisU();
		MfbaSpecialPltRegisV laMfbaSpclPltsRegisV =
			new MfbaSpecialPltRegisV();

		// defect 9086
		//	Add MFVersion U data format process
		//
		// Determine processing path from CICS/PGM version
		//
		//	T version - Exempts/TERPS 	 (Not used by production)
		//  U version - Special Plates
		//  V version - Prior to Exempts (Not used by production)
		//
		//	T version - Exempts/TERPS
		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			laSpclPltRegisData =
				laMfbaSpclPltsRegisT.setSpclPltRegisDataFromMF(
					asMfSpclPltRegisResponse);
			laMfbaSpclPltsRegisT = null;
		}

		//	U version - Special Plates
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laSpclPltRegisData =
				laMfbaSpclPltsRegisU.setSpclPltRegisDataFromMF(
					asMfSpclPltRegisResponse);
			laMfbaSpclPltsRegisU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laSpclPltRegisData =
				laMfbaSpclPltsRegisV.setSpclPltRegisDataFromMF(
					asMfSpclPltRegisResponse);
			laMfbaSpclPltsRegisV = null;
			// defect 9630
			// Save RegPltNo to RmvRegPltNo and Reset RegPltNo = "NOPLATE"
			//	 if RmvPltCd > 0
//			laSpclPltRegisData.resetFields(laSpclPltRegisData);
			// end defect 9630
		}
		// defect 9833
		// Move defect 9630 from inside "V" version to all versions
		laSpclPltRegisData.resetFields(laSpclPltRegisData);
		// end defect 9833
		// end defect 9086

		return laSpclPltRegisData;
	}
}
