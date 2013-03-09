package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ProductServiceData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * ProductServiceCache.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Work 
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * The ProductServiceCache class provides static method to 
 * retrieve a vector of ProductServiceCache objects based upon key 
 *
 * <p>ProductServiceCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	5.2.3 			06/17/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		01/04/2002 13:23:30 
 */

public class ProductServiceCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static java.util.Hashtable shtProductServices =
		new Hashtable();
	/**
	 * ProductServiceCache constructor comment.
	 */
	public ProductServiceCache()
	{
		super();
	}
	/**
	 * Return vector of all ProductServiceData
	 * 
	 * @return Vector
	 */
	public static Vector getAllProductServiceData()
	{
		if (shtProductServices != null)
		{
			return new Vector(shtProductServices.values());
		}
		else
		{
			return new Vector();
		}
	}
	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.PRODUCT_SERVICE_CACHE;
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
		return shtProductServices;
	}
	/**
	 * Clear and populate the hashtable with the vector
	 *  
	 * @param avDataVector Vector
	 */
	public void setData(Vector avProductServicesData)
		throws RTSException
	{
		shtProductServices.clear();
		
		for (int i = 0; i < avProductServicesData.size(); i++)
		{
			ProductServiceData laData =
				(ProductServiceData) avProductServicesData.get(i);
			shtProductServices.put(laData.getPrdctSrvcDesc(), laData);
		}
	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable	Hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtProductServices = ahtHashtable;
	}
}
