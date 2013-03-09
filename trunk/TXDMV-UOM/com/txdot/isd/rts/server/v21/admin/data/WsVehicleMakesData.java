package com.txdot.isd.rts.server.v21.admin.data;
/*
 * WsVehicleMakesData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/28/2007	New class
 * 							defect 9502 Ver FRVP
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setVehicleMakeDescription()
 * 							delete setVehicleMakeDesc()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Vehicle Make.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		12/28/2008 12:04:56
 */
public class WsVehicleMakesData
{
	private String csVehicleMake = "";
	private String csVehicleMakeDescription = "";

	/**
	 * Returns Vehicle Make.
	 * 
	 * @return String
	 */
	public String getVehicleMake()
	{
		return csVehicleMake;
	}

	/**
	 * Returns Vehicle Make Description.
	 * 
	 * @return String
	 */
	public String getVehicleMakeDescription()
	{
		return csVehicleMakeDescription;
	}

	/**
	 * Sets Vehicle Make.
	 * 
	 * @param asVehicleMake
	 */
	public void setVehicleMake(String asVehicleMake)
	{
		csVehicleMake = asVehicleMake;
	}

	/**
	 * Sets Vehicle Make Description. 
	 * 
	 * @param asVehicleMakeDescription
	 */
	public void setVehicleMakeDescription(String asVehicleMakeDescription)
	{
		csVehicleMakeDescription = asVehicleMakeDescription;
	}

}
