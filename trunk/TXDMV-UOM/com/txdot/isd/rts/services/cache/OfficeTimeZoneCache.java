package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OfficeTimeZoneData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * OfficeTimeZoneCache.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/03/2010	Created
 * 							defect 10427 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * OfficeTimeZoneCache.java class extends GeneralCache.
 * The OfficeTimeZoneCache.java class provides static methods to 
 * retrieve TimeZone for a given OfcIssuanceNo 
 * on the input parameter.
 *
 * @version	POS_640			04/03/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		04/03/2010	11:52:17 
 */
public class OfficeTimeZoneCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	 * A hashtable of vectors with OfcIssuanceNo as key
	 */
	private static Hashtable shtOfcTimeZone = new Hashtable();
	
	private static final String MOUNTAIN_TIME_ZONE = "M";

	static final long serialVersionUID = -5155159634012326553L;

	/**
	 * OfficeTimeZoneCache Constructor
	 * 
	 */
	public OfficeTimeZoneCache()
	{
		super();
	}

	/**
	 * Implements the GeneralCache.getCacheFunctionId() abstract method.
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.OFFICE_TIMEZONE_CACHE; 
	}
	
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtOfcTimeZone;
	}
	
	/**
	 * Returns true if Mountain Zone
	 * 
	 * @param aiOfcIssuanceNo
	 * @return boolean 
	 */
	public static boolean isMountainTimeZone(int aiOfcIssuanceNo) 
	{
		return getTimeZone(aiOfcIssuanceNo).equals(MOUNTAIN_TIME_ZONE); 
	}

	/**
	 * Gets the office code data object.  
	 * Returns null if object does not exist.
	 * 
	 * @param aiOfcIssuanceNo int
	 * @return String 
	 */
	public static String getTimeZone(int aiOfcIssuanceNo)
	{
		Object laObject =
			shtOfcTimeZone.get(new Integer(aiOfcIssuanceNo));

		String lsTimeZone = null; 

		if (laObject != null)
		{
			lsTimeZone = ((OfficeTimeZoneData) laObject).getTimeZone();
		}
		return lsTimeZone == null ? new String() : lsTimeZone;
	}

	/**
	 * Implements the GeneralCache.setData() abstract method.
	 * Clear and populate the hashtable with the vector avOfficeCodesData.
	 * 
	 * Populates OfficeTimeZoneData vector into a hashtable.
	 * 
	 * @param avOfficeCodesData Vector 
	 * 
	 */
	public void setData(Vector avOfcTimeZoneData)
	{
		//reset the hashtable
		shtOfcTimeZone.clear();

		for (int i = 0; i < avOfcTimeZoneData.size(); i++)
		{
			OfficeTimeZoneData laData =
				(OfficeTimeZoneData) avOfcTimeZoneData.get(i);

			shtOfcTimeZone.put(
				new Integer(laData.getOfcIssuanceNo()),
				laData);
		}
	}

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable Hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtOfcTimeZone = ahtHashtable;
	}
}
