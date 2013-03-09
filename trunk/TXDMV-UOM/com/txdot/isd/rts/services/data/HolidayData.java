/**
 * 
 */
package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * HolidayData.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/03/2007  Created
 *							defect 9919  Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for HolidayData
 * 
 * @version 6.9.0 	10/03/2011
 * @author Kathy Harrell
 * @since 			10/03/2011	17:26:17
 */
public class HolidayData implements Serializable
{

	// int
	private int ciHolidayDate;

	// String
	private String csHolidayName;
	
	private static final long serialVersionUID = -334924801292675676L;

	/**
	 * Return value of ciHolidayDate
	 * 
	 * @return int
	 */
	public int getHolidayDate()
	{
		return ciHolidayDate;
	}

	/**
	 * Return value of csHolidayName
	 * 
	 * @return
	 */
	public String getHolidayName()
	{
		return csHolidayName;
	}

	/**
	 * Set value of ciHolidayDate
	 * 
	 * @param aiHolidayDate
	 */
	public void setHolidayDate(int aiHolidayDate)
	{
		ciHolidayDate = aiHolidayDate;
	}

	/**
	 * Set value of csHolidayName
	 * 
	 * @param asHolidayName
	 */
	public void setHolidayName(String asHolidayName)
	{
		csHolidayName = asHolidayName;
	}

}
