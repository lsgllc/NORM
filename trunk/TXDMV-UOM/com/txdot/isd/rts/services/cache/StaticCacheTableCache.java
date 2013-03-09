package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.StaticCacheTableData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * StaticCacheTableCache.java  
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  			 
 * ---------------------------------------------------------------------
 */

/**
 * The Static Cache Table Cache class is the table of 
 * contents of all the static cache.  It provides static methods to 
 * retrieve a list of StaticCacheTableData
 *
 * <p>StaticCacheTableCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * used to determine whether a particular Cache needs to be updated.
 * 
 * @version	5.2.3		06/17/2005
 * @author Nancy Ting
 * <br>Creation Date:	08/14/2001 15:33:44
 * 
 */
public class StaticCacheTableCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* Static Cache hashtable
	*/
	private static Hashtable shtStaticCacheTbl = new Hashtable();

	private final static long serialVersionUID = -2566053650928794401L;

	/**
	 * StaticCacheTblCache default constructor
	 */
	public StaticCacheTableCache()
	{
		super();
	}
	/**
	 * Returns the function id of the static cache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.STATIC_CACHE_TABLE_CACHE;
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
		return shtStaticCacheTbl;
	}
	/**
	 * Get a vector of static cache table cache
	 * 
	 * @return Vector 
	 */
	public static Vector getStaticCacheTbl()
	{
		if (shtStaticCacheTbl.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturn = new Vector();
			for (Enumeration e = shtStaticCacheTbl.elements();
				e.hasMoreElements();
				)
			{
				lvReturn.addElement(e.nextElement());

			}
			return lvReturn;

		}
	}
	/**
	 * Get the static cache table data
	 * 
	 * @param asStaticCacheName String
	 * @return StaticCacheTableData 
	 */
	public static StaticCacheTableData getStaticCacheTbl(String asStaticCacheName)
	{
		Object laObject = shtStaticCacheTbl.get(asStaticCacheName);
		if (laObject != null)
		{
			return (StaticCacheTableData) laObject;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Set the data of the cache to contain the elements of the vector
	 * being passed in.
	 * 
	 * @param avStaticCacheTblData Vector
	 */
	public void setData(Vector avStaticCacheTblData)
	{
		shtStaticCacheTbl.clear();

		if (avStaticCacheTblData == null)
		{
			return;
		}
		for (int i = 0; i < avStaticCacheTblData.size(); i++)
		{
			StaticCacheTableData laData =
				(StaticCacheTableData) avStaticCacheTblData.get(i);
			shtStaticCacheTbl.put(laData.getCacheObjectName(), laData);

		}
	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable the hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtStaticCacheTbl = ahtHashtable;
	}
}
