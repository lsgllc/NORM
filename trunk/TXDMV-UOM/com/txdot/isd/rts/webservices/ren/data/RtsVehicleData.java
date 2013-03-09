package com.txdot.isd.rts.webservices.ren.data;
/*
 * RtsVehicleData.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/15/2011	Initial load.
 * 							Defect 10670 Ver 6.7.0
 * Ray Rowehl	02/07/2011	Add new fields from table	
 * 							Defect 10670 Ver 6.7.0
 * Ray Rowehl	02/09/2011	Add Vehicle Model
 * 							Defect 10670 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Vehicle Class used for Web Agent.
 *
 * @version	6.7.0			02/09/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/15/2011 12:46:56
 */

public class RtsVehicleData
{
	private int ciVehModlYr;
	private String csVehMk = "";
	private String csVehModl = "";
	private String csVIN = "";

	/**
	 * Get Vehicle Make.
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}
	
	/**
	 * Get the Vehicle Model.
	 * 
	 * @return String
	 */
	public String getVehModl()
	{
		return csVehModl;
	}
	
	/**
	 * Get Vehicle Model Year.
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Get the Vehicle Identification Number
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}
	
	/**
	 * Set Vehicle Make.
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set the Vehicle Model.
	 * 
	 * @param asVehModl
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * Set Vehicle Model Year.
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Set the Vehicle Identification Number
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

}
