package com.txdot.isd.rts.client.common.business;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 *
 * CommonUtil.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/18/2005	Move to client.common.business.
 *							defect 7705 Ver 5.2.3
 * B Hargrove	08/22/2005	Code cleanup for Java 1.4. Format,
 * 							Hungarian notation, use constant for max 
 * 							veh weight, etc. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Common Utility Class
 *
 * @version	5.2.3			08/22/2005
 * @author	Joseph Peters
 * <br>Creation Date:		10/14/2001 13:19:25
 */

public class CommonUtil
{
	/**
	 * CommonUtil constructor comment.
	 */
	public CommonUtil()
	{
		super();
	}
	/**
	 * Validate vehicle weights
	 * 
	 * @return VehicleInquiryData
	 * @param VehicleInquiryData
	 */
	public VehicleInquiryData validateVehWts(VehicleInquiryData aaVehData)
	{

		if (aaVehData.getMfVehicleData().getRegData().getVehGrossWt()
			> RegistrationConstant.MAX_VEH_WEIGHT)
		{
			aaVehData.getMfVehicleData().getRegData().setVehGrossWt(
		RegistrationConstant.MAX_VEH_WEIGHT);
		}
		if (aaVehData.getMfVehicleData().getVehicleData().getVehEmptyWt()
			> RegistrationConstant.MAX_VEH_WEIGHT)
		{
			aaVehData.getMfVehicleData().getVehicleData().
				setVehEmptyWt(RegistrationConstant.MAX_VEH_WEIGHT);
		}
		if (aaVehData.getMfVehicleData().getVehicleData().getVehEmptyWt()
			< aaVehData.getMfVehicleData().getRegData().getVehGrossWt())
		{
			aaVehData.getMfVehicleData().getRegData().
				setVehCaryngCap(aaVehData.getMfVehicleData().
				getRegData().getVehGrossWt() -
				aaVehData.getMfVehicleData().getVehicleData().
					getVehEmptyWt());
		}
		else
		{
			aaVehData.getMfVehicleData().getRegData().
				setVehGrossWt(aaVehData.getMfVehicleData().
				getVehicleData().getVehEmptyWt());
		}
		return aaVehData;
	}
}
