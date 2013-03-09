package com.txdot.isd.rts.server.webapps.registrationrenewal;

import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.webapps.common.business.CommonEligibility;

/*
 * RegistrationEligibility.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/26/2005	First modified to recognize movement of 
 * 							qryItemCodes() referenced in specialPlate()
 * 							However specialPlate() not used. 
 * 							Mysterious use of csMessage. 
 * 							delete csMessage 
 * 							deprecate specialPlate(),legalRestraint(),
 * 							offHighwayUseOnly(),recentlyRenewed(),
 * 							getMessage() 
 * 							modify isEligible(),additionalEligible()
 * 							renamed methods to comply with standards 
 * 							defect 7889 Ver 5.2.3 
 * K Harrell	02/16/2007	Added check for validity of Special Plate 
 *							modify isEligible()
 *							defect 9119 Ver Special Plate
 *----------------------------------------------------------------------
 */

/**
 * Determine registration renewal eligibility, using CommonEligibility,
 * and additional checks that are specific for registration renewal.
 *  
 * @version	Special Plate	02/16/2007 
 * @author	Administrator
 * <br>Creation Date:		10/08/2001 16:25:52
 */
public class RegistrationEligibility
{
	private VehicleInquiryData caVehInquiryData;
	private MFVehicleData caMfVehicleData;

	// defect 7889 
	// Use constant in the referencing programs  
	// general not eligible for registration renewal message	
	// private String csMessage =
	//	MessageConstants.MSG_NOT_ELIGIBLE_FOR_REG_RENEWAL;
	// end defect 7889 

	/**
	 * Registration Eligibility constructor
	 *  
	 * @param aaVehInquiryData VehicleInquiryData
	 */
	public RegistrationEligibility(VehicleInquiryData aaVehInquiryData)
	{
		this.caVehInquiryData = aaVehInquiryData;
		this.caMfVehicleData = caVehInquiryData.getMfVehicleData();
	}

	/**
	 * Is Additional Eligible
	 *
	 * @throws RTSException
	 * @return boolean 
	 */
	private boolean additionalEligible() throws RTSException
	{
		// defect 7889 
		// Condensed logic & structured code for single return
		boolean lbReturn = true;

		if (isVehicleWeight55000LbOrMore()
			|| hasCountyScofflaw()
			|| hasTrafficWarrant()
			|| isDPSEmissionPrgmNoncompliance()
			|| isTNRCCEmissionPrgmNoncompliance()
			|| isPlateSeized()
			|| isStickerSeized())
		{
			lbReturn = false;
		}
		return lbReturn;
		// end defect 7889 
	}

	/**
	 * Has County Scofflaw
	 *
	 * @return boolean  
	 */
	private boolean hasCountyScofflaw()
	{
		return caMfVehicleData.getRegData().getClaimComptCntyNo() != 0;
	}

	/**
	 * Is DPS Emission Prgm Noncompliance
	 *
	 * @return boolean  
	 */
	private boolean isDPSEmissionPrgmNoncompliance()
	{
		return caMfVehicleData
			.getRegData()
			.getEmissionSourceCd()
			.equalsIgnoreCase("D");
	}

	/**
	 * Get Message
	 *
	 * @return String 
	 * @deprecated  
	 */
	//	public String getMessage()
	//	{
	//		//return csMessage;
	//		return "";
	//	}

	/**
	 * Is Eligibility
	 *  
	 * @return boolean
	 * @throws RTSException
	 */
	public boolean isEligible() throws RTSException
	{
		// defect 7884
		// Structured calls for single return.  Removed need for setting
		// message in CommonEligibility. Consolidated calls.  

		boolean lbReturn = true;

		CommonEligibility laComElig =
			new CommonEligibility(caVehInquiryData);
			
		if (!laComElig.isEligibleForRenewal() 
			|| !additionalEligible())
		{
			lbReturn = false;
		}
		return lbReturn;

		// csMessage is actually a constant
		// laComElig.setMessage(
		//	MessageConstants.MSG_NOT_ELIGIBLE_FOR_REG_RENEWAL);

		// Combined calls 
		// if (!laComElig.eligibleForRenewal())

		//{
		//     csMessage = laComElig.getMessage();
		//     return false;
		//}

		// eligibility check specific for registration renewal
		// if (!additionalEligible())
		// {
		//     return false;
		// } 
		// recentlyRenewed always returned false 
		// if (recentlyRenewed()))
		//{
		//	return false;
		//}
		// return true; 
		// end defect 7884 
	}

	/**
	 * Has Legal Restraint
	 *  
	 * @return boolean
	 * @deprecated 
	 */
	//	private boolean legalRestraint()
	//	{
	//		return caMfVehicleData
	//			.getTitleData()
	//			.getLegalRestrntNo()
	//			.length()
	//			!= 0;
	//	}

	/**
	 * offHighway Use Only
	 *  
	 * @return boolean
	 * @deprecated
	 */
	//	private boolean offHighwayUseOnly()
	//	{
	//		return caMfVehicleData.getTitleData().getDocTypeCd() == 2;
	//	}

	/**
	 * Is Plate Seized
	 *  
	 * @return boolean
	 */
	private boolean isPlateSeized()
	{
		return caMfVehicleData.getRegData().getPltSeizedIndi() != 0;
	}

	/**
	 * Is Recently Renewed
	 * This is what James proposed not in the document.
	 * 
	 * @deprecated  
	 * @return boolean
	 */
	//	private boolean recentlyRenewed()
	//	{
	//		// check from the internet_renewal table that
	//		// if it has been recently renewed, if it is 
	//		// the case, not eligible to renew again.
	//		// Handled in RegistrationRenewalServerBusiness
	//		// always return false here
	//		return false;
	//	}

	/**
	 * Is Special Plate
	 *  
	 * @return boolean
	 * @throws RTSException
	 * @deprecated 
	 */
	//	private boolean specialPlate() throws RTSException
	//	{
	//		ItemCodes laItemCodes = new ItemCodes(new DatabaseAccess());
	//		ItemCodesData laItemCodesData =
	//			laItemCodes.qryItemCodes(
	//				caMfVehicleData.getRegData().getRegPltCd());
	//
	//		if (laItemCodesData.getInvProcsngCd() == 2)
	//			// Todd -->
	//			// In the RTS_Item_codes table, use plate code as the ItmCd
	//			// if the InvProcsngCd==2 then it is a special plate
	//			return true;
	//
	//		// not a special plate
	//		return false;
	//	}

	/**
	 * Is Sticker Seized
	 *  
	 * @return boolean
	 */
	private boolean isStickerSeized()
	{
		return caMfVehicleData.getRegData().getStkrSeizdIndi() != 0;
	}

	/**
	 * Is TNRCC Emission Prgm Noncompliance
	 *  
	 * @return boolean
	 */
	private boolean isTNRCCEmissionPrgmNoncompliance()
	{
		return caMfVehicleData
			.getRegData()
			.getEmissionSourceCd()
			.equalsIgnoreCase("T");
	}

	/**
	 * Has Traffic Warrant
	 *  
	 * @return boolean
	 */
	private boolean hasTrafficWarrant()
	{
		return (
			caMfVehicleData.getRegData().getNotfyngCity() != null
				&& caMfVehicleData.getRegData().getNotfyngCity().length()
					> 0);
	}

	/**
	 * Is Vehicle Weight 55000 Lb Or More
	 *  
	 * @return boolean
	 */
	private boolean isVehicleWeight55000LbOrMore()
	{
		return caMfVehicleData.getRegData().getVehGrossWt() >= 55000;
	}
}
