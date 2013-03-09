package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyTypeData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * WebAgencyTypeCache.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/29/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Cache for RTS_WEB_AGENCY_TYPE 
 *
 * @version	6.7.0			12/29/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/29/2010 9:47:17 
 */
public class WebAgencyTypeCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static Hashtable shtWebAgncyTypeCache = new Hashtable();

	static final long serialVersionUID = 6085253397036936804L;

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.WEB_AGENCY_TYPE_CACHE;
	}

	/**
	 * Get all WebAgencyTypeCache
	 *  
	 * @return Vector
	 */
	public static Vector getWebAgencyType()
	{
		Vector lvReturn = new Vector();
		Enumeration e = shtWebAgncyTypeCache.elements();
		while (e.hasMoreElements())
		{
			WebAgencyTypeData laData =
				(WebAgencyTypeData) e.nextElement();
			lvReturn.addElement(laData);
		}

		if (lvReturn.size() == 0)
		{
			return null;
		}
		else
		{
			return lvReturn;
		}
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
		return shtWebAgncyTypeCache;
	}
	/**
	 * Clear and populate the hashtable with the vector avWebAgencyTypeData
	 * 
	 * @param avWebAgencyTypeData Vector
	 */
	public void setData(Vector avWebAgencyTypeData)
	{
		//reset the hashtable
		shtWebAgncyTypeCache.clear();
		for (int i = 0; i < avWebAgencyTypeData.size(); i++)
		{
			WebAgencyTypeData laData =
				(WebAgencyTypeData) avWebAgencyTypeData.get(i);

			shtWebAgncyTypeCache.put(laData.getAgncyTypeCd(), laData);
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
		shtWebAgncyTypeCache = ahtHashtable;
	}
}
