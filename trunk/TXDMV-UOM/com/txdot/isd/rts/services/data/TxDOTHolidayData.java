package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * TxDOTHolidayData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	03/05/2007  Created
 *							defect 9085  Ver Special Plates
 * K Harrell	10/08/2011	deprecated
 * 							defect 9919 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * TxDOTHolidayData
 *
 * @version	6.9.0 			10/08/2011
 * @author	Kathy Harrell
 * @since 					03/05/2007 17:47 
 **/

public class TxDOTHolidayData implements Serializable
{
	// int 
	private int ciHolidayDate;

	// String 
	private String csHolidayDesc;

	static final long serialVersionUID = -5136604956725395619L;

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
	 * Return value of csHolidayDesc
	 * 
	 * @return
	 */
	public String getHolidayDesc()
	{
		return csHolidayDesc;
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
	 * Set value of csHolidayDesc
	 * 
	 * @param asHolidayDesc
	 */
	public void setHolidayDesc(String asHolidayDesc)
	{
		csHolidayDesc = asHolidayDesc;
	}

}
