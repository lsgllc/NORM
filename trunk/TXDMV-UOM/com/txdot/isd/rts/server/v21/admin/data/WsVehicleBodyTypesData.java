package com.txdot.isd.rts.server.v21.admin.data;
/*
 * WsVehicleBodyTypesData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/11/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Vehicle Body Types.
 *
 * @version	3_Amigos_PH_A	01/11/2008
 * @author	B Hargrove
 * <br>Creation Date:		01/10/2008 08:44
 */
public class WsVehicleBodyTypesData
{
	private String csVehicleBodyType = "";
	private String csVehicleBodyDescription = "";
	
	/**
	 * Returns Vehicle Body Type.
	 * 
	 * @return String
	 */
	public String getVehicleBodyType()
	{
		return csVehicleBodyType;
	}
	
	/**
	 * Returns Vehicle Body Type Description.
	 * 
	 * @return String
	 */
	public String getVehicleBodyDescription()
	{
		return csVehicleBodyDescription;
	}
	
	/**
	 * Sets Vehicle Body Type.
	 * 
	 * @param String asVehicleBodyType
	 */
	public void setVehicleBodyType(String asVehicleBodyType)
	{
		csVehicleBodyType = asVehicleBodyType;
	}
	
	/**
	 * Sets Vehicle Body Type Description.
	 * 
	 * @param String asVehicleBodyDescription
	 */
	public void setVehicleBodyDescription(String asVehicleBodyDescription)
	{
		csVehicleBodyDescription = asVehicleBodyDescription;
	}
}
