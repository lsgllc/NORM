package com.txdot.isd.rts.server.webapps.common.business;

import com.txdot.isd.rts.services.data.*;

/*
 * VehicleBase.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * Vehicle Base Data
 *  
 * @version	5.2.3		05/04/2005
 * @author	James Giroux
 * <br>Creation Date:	10/08/2001 17:52:35
 */
public class VehicleBase
{
	/**
	 * VehicleBase constructor comment.
	 */
	public VehicleBase()
	{
		super();
	}
	/**
	 * Set Vehicle Base Data
	 *  
	 * @param aaVehicleInquiryData VehicleInquiryData
	 * @return Object VehicleBaseData 
	 */
	public static VehicleBaseData setVehicleBaseData(VehicleInquiryData aaVehicleInquiryData)
	{
		// Get various data objects from VehicleInquiryData object.
		MFVehicleData laMFVehicleData =
			aaVehicleInquiryData.getMfVehicleData();
		RegistrationData laRegistrationData =
			laMFVehicleData.getRegData();
		VehicleData laVehicleData = laMFVehicleData.getVehicleData();
		TitleData laTitleData = laMFVehicleData.getTitleData();

		// Set VehicleBaseData from the various objects
		VehicleBaseData laVehicleBaseData = new VehicleBaseData();
		laVehicleBaseData.setPlateNo(laRegistrationData.getRegPltNo());
		laVehicleBaseData.setOwnerCountyNo(
			String.valueOf(laRegistrationData.getResComptCntyNo()));
		laVehicleBaseData.setVin(laVehicleData.getVin());
		laVehicleBaseData.setDocNo(laTitleData.getDocNo());
		return laVehicleBaseData;
	}
}
