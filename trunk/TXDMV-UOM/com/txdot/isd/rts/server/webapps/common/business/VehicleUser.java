package com.txdot.isd.rts.server.webapps.common.business;

import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.data.VehicleUserData;

/*
 * VehicleUser.java
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
 * Vehicle User
 *  
 * @version	5.2.3		05/04/2005
 * @author	Administrator
 * <br>Creation Date:	11/01/2001 16:19:28
 */
public class VehicleUser
{
	/**
	 * VehicleUser constructor comment.
	 */
	public VehicleUser()
	{
		super();
	}
	/**
	 * Set Vehicle User Data
	 *  
	 * @param aaVehInqData VehicleInquiryData
	 * @return VehicleUserData 
	 */
	public static VehicleUserData setVehUserData(VehicleInquiryData aaVehInqData)
	{
		// Get various data objects from VehicleInquiryData object.
		MFVehicleData laMFVehicleData = aaVehInqData.getMfVehicleData();
		RegistrationData laRegData = laMFVehicleData.getRegData();

		// Set VehicleUserData from the various objects
		VehicleUserData laVehUserData = new VehicleUserData();
		laVehUserData.setAddress(laRegData.getRenwlMailAddr());
		laVehUserData.setRecipientName(laRegData.getRecpntName());

		return laVehUserData;
	}
}
