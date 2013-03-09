package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;

/*
 *
 * MfbaSpecialOwnerG.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/02/2006	Remove G from method name
 * 							modify 
 * 							  setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/11/2006	Replace class constants with 
 * 							ApplicationControlConstants and
 * 							MessageConstants
 * 							modify 
 * 							 setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Turn off throw RTSException until Client
 * 							can resolve RTS MF Down
 * 							modify 
 * 							 setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701, Ver Exempts
 * J Rue		10/19/2006	Add reference to MfbaSpecialOwnerV
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	MF Version test move to 
 * 							MfAccess.getResponce()
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		02/01/2007	Add MFVersion U code. 
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	Delete exception code of version is incorrect
 * 							modify setVehicleInquiryDataFromSpecialOwnerResponse()
 * 							defect 8984 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Set path for CICS transactions Special Owner Data on the TxDOT 
 * mainframe. 
 * Used by the server side business layer. 
 *
 * @version	Special Plates	02/01/2007
 * @author	Jeff Rue
 * <br>Creation Date:		10/02/2006 09:00:32
 */
public class MfbaSpecialOwnerG
{
	/**
	 * 
	 */
	public MfbaSpecialOwnerG()
	{
		super();
	}

	/**
	 * Set CICS transaction path base on MF version.
	 * 
	 * @param asMfTtlRegResponse String
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData 
		setVehicleInquiryDataFromSpecialOwnerResponse(
			String asMfTtlRegResponse)
	{
		// CICS Version codes
		MfbaSpecialOwnerT laSpecialOwnerT = new MfbaSpecialOwnerT();
		MfbaSpecialOwnerU laSpecialOwnerU = new MfbaSpecialOwnerU();
		MfbaSpecialOwnerV laSpecialOwnerV = new MfbaSpecialOwnerV();
		
		//Create Data objects to be added to VehicleData
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();

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
			laVehicleInquiryData =
				laSpecialOwnerT
					.setVehicleInquiryDataFromSpecialOwnerResponse(
					asMfTtlRegResponse);
			laSpecialOwnerT = null;
		}
		
		//	U version - Special Plates
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			laVehicleInquiryData =
				laSpecialOwnerU
					.setVehicleInquiryDataFromSpecialOwnerResponse(
					asMfTtlRegResponse);
			laSpecialOwnerU = null;
		}

		//	V version - Prior to Exempts (Not used by production)
		else if (SystemProperty.getMFInterfaceVersionCode().equals(
			ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			laVehicleInquiryData =
				laSpecialOwnerV
					.setVehicleInquiryDataFromSpecialOwnerResponse(
					asMfTtlRegResponse);
			laSpecialOwnerV = null;
		}
		// end defect 9086

		return laVehicleInquiryData;
	}
}
