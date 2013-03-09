package com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.vehiclevalidation
	.business;

import com.txdot.isd.rts.server.webapps.common.business.SearchVehicle;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.common
	.business
	.AbstractBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.common
	.data
	.AbstractResponse;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.common
	.data
	.DefaultResponse;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.vehiclevalidation
	.data
	.VehicleValidationRequest;
import com.txdot.isd.rts.services.cache.ClassToPlateCache;
import com
	.txdot
	.isd
	.rts
	.services
	.cache
	.VehicleClassSpclPltTypeDescCache;
import com.txdot.isd.rts.services.data.ClassToPlateData;
import com.txdot.isd.rts.services.data.VehicleBaseData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.CommonConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.ServiceConstants;

/*
 * VehicleValidationBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob  B.		03/05/2007	Created class.
 * 							defect 9119 Ver Special Plates 
 * Steve C		05/31/2011	Vehicle validation for speical plates
 *							defect 10456 ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * Object used to handle the Vehicle Validation Requests.
 *
 * @version	Special Plates	03/05/2007
 * @author	Bob Brown
 * <br>Creation Date:		03/05/2007 14:30:00
 */
public class VehicleValidationBusiness extends AbstractBusiness
{

	public Object processData(Object aaObject)
	{
		VehicleValidationRequest laValidationReq =
			(VehicleValidationRequest) aaObject;

		System.out.println(
			"RegPltNo = " + laValidationReq.getRegPltNo());
		System.out.println(
			"LastFourVin = " + laValidationReq.getLastFourVin());
		System.out.println(
			"RegPltCd = " + laValidationReq.getRegPltCd());
		DefaultResponse laDefResp = new DefaultResponse();

		VehicleBaseData laVehBaseData = new VehicleBaseData();
		laVehBaseData.setPlateNo(laValidationReq.getRegPltNo());
		laVehBaseData.setVin(laValidationReq.getLastFourVin());

		SearchVehicle laSearchVehicle =
			new SearchVehicle(laVehBaseData);
			
		VehicleInquiryData laVehicle = null;
		try
		{
			Object laMFObj = laSearchVehicle.getVehicle();
			if (laMFObj instanceof VehicleInquiryData)
			{
				laVehicle = (VehicleInquiryData) laMFObj;
			}
			else
			{
				//TODO code an error?
				laDefResp.setAck(ServiceConstants.AR_ACK_FAILURE);
				return laDefResp;
			}
		}
		catch (RTSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (laValidationReq.getAction())
		{
			case ServiceConstants.VV_ACTION_VALIDATE_SP :
			{
				/*
				boolean lbValidCombo = VehicleClassSpclPltTypeDescCache.isValidVehPltCombo(
					laVehicle
						.getMfVehicleData()
						.getVehicleData()
						.getVehClassCd(),
					laValidationReq.getRegPltCd());
				*/

				boolean lbValidCombo = false;
				java.util.List plateData = ClassToPlateCache.getClassToPlate(laVehicle.getMfVehicleData().getRegData().getRegClassCd(),laValidationReq.getRegPltCd(),new RTSDate().getYYYYMMDDDate());
				if(plateData!=null && plateData.size()>0)
					lbValidCombo = true;
				
				if (lbValidCombo)
				{
					laDefResp.setAck(ServiceConstants.AR_ACK_SUCCESS);
				}
				else 
				{
					laDefResp.setAck(
						ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
				}
				break;
			}
				
			default :
				//TODO Log
				return null;
		}
		return laDefResp;
	}

}
