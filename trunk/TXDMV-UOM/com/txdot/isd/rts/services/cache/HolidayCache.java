package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.HolidayData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * HolidayCache.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/03/2011	Created
 * 							defect 9919 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * HolidayCache class extends GeneralCache. The HolidayCache class provides
 * static methods to retrieve a particular or all HolidayData based on the input
 * parameter.
 * 
 * @version 6.9.0 	10/03/2011
 * @author 	Kathy Harrell
 * @since 			10/03/2011
 */

public class HolidayCache extends GeneralCache
{
	/**
	 * A hashtable of vectors with HolidayDate as key
	 */
	private static Hashtable shtHoliday = new Hashtable();

	private static final long serialVersionUID = 7352038417408880968L;

	/**
	 * HolidayCache default constructor. Calls super();
	 */
	public HolidayCache()
	{
		super();
	}

	/**
	 * Implements the GeneralCache.getCacheFunctionId() abstract method. Return
	 * the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.HOLIDAY_CACHE;
	}

	/**
	 * Get the internally stored Hashtable.
	 * 
	 * <P>
	 * Class that inherits from Admin cache is required to implement this
	 * method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtHoliday;
	}

	/**
	 * Gets the Holiday data object. Returns null if object does not exist.
	 * 
	 * @param aiDate
	 *            int
	 * @return boolean
	 */
	public static boolean isHoliday(int aiDate)
	{
		boolean lbHoliday = false;
		Object laObject = shtHoliday.get(new Integer(aiDate));
		if (laObject != null)
		{
			lbHoliday = true;
		}
		return lbHoliday;
	}

	/**
	 * Gets the Holiday data object vector. Returns null if no objects exist.
	 * 
	 * @return Vector
	 */
	public static Vector getAllHolidays()
	{

		Vector lvHolidayData = new Vector();
		Enumeration e = shtHoliday.elements();

		while (e.hasMoreElements())
		{
			HolidayData laHolidayData = (HolidayData) e.nextElement();
			lvHolidayData.addElement(laHolidayData);
		}

		if (lvHolidayData.size() == 0)
		{
			return null;
		}
		else
		{
			return lvHolidayData;
		}
	}

	/**
	 * Implements the GeneralCache.setData() abstract method. Clear and populate
	 * the hashtable with the vector avHolidayData.
	 * 
	 * Populates HolidayData vector into a hashtable.
	 * 
	 * @param avHolidayData
	 *            Vector
	 * 
	 */
	public void setData(Vector avHolidayData)
	{
		// reset the hashtable
		shtHoliday.clear();
		for (int i = 0; i < avHolidayData.size(); i++)
		{
			HolidayData laData = (HolidayData) avHolidayData.get(i);
			shtHoliday
					.put(new Integer(laData.getHolidayDate()), laData);
		}
	}

	/**
	 * Set the internally stored Hashtable.
	 * 
	 * <P>
	 * Class that inherits from Admin cache is required to implement this
	 * method.
	 * 
	 * @param ahtHashtable
	 *            Hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtHoliday = ahtHashtable;
	}
}
