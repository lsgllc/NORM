package com.txdot.isd.rts.server.v21.vehicleinfo.data;
/*
 * WsVehicleInfoV21Indicator.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/29/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * Defines the Indicator data being returned to V21.
 *
 * @version	3_Amigos_PH_A	01/29/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		01/29/2008 16:30:36
 */
public class WsVehicleInfoV21Indicator
{
	private String csIndiName = "";
	private String csIndiValue = "";
	
	/**
	 * Get the Indicator Name.
	 * 
	 * @return String
	 */
	public String getIndiName()
	{
		return csIndiName;
	}

	/**
	 * Get the Indicator AbstractValue.
	 * 
	 * @return String
	 */
	public String getIndiValue()
	{
		return csIndiValue;
	}

	/**
	 * Set the Indicator Name.
	 * 
	 * @param asIndiName
	 */
	public void setIndiName(String asIndiName)
	{
		csIndiName = asIndiName;
	}

	/**
	 * Set the Indicator AbstractValue.
	 * 
	 * @param asIndiValue
	 */
	public void setIndiValue(String asIndiValue)
	{
		csIndiValue = asIndiValue;
	}
}
