package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DisabledPlacardCustomerIdTypeData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * DisabledPlacardCustomerIdTypeCache.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver Defect _POS_B
 * ---------------------------------------------------------------------
 */

/**
 * The DisabledPlacardCustomerIdTypeCache class provides static methods 
 * to retrieve a DisabledPlacardCustomerIdTypeData based on provided 
 * key.
 *
 * <p>DisabledPlacardCustomerIdTypeCache is being initialized and 
 * populated by the CacheManager when the system starts up.  The data 
 * will be stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Defect _POS_B	10/27/2008	
 * @author	K Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class DisabledPlacardCustomerIdTypeCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtDsabldPlcrdCustIdTypeCache =
		new Hashtable();

	static final long serialVersionUID = 6064347671096293553L;

	/**
	 * DisabledPlacardCustomerIdTypeCache constructor comment.
	 */
	public DisabledPlacardCustomerIdTypeCache()
	{
		super();
	}

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.DSABLD_PLCRD_CUST_ID_TYPE_CACHE;
	}

	/**
	 * Returns the DisabledPlacardCustomerIdTypeData object based on
	 * the input asCustIdTypeCd
	 *
	 * @param asCustIdTypeCd
	 * @return DisabledPlacardCustomerIdTypeData 
	 */
	public static DisabledPlacardCustomerIdTypeData getDsabldPlcrdCustIdType(int aiCustIdTypeCd)
	{
		Integer liCustIdTypeCd = new Integer(aiCustIdTypeCd);
		Object laObject =
			shtDsabldPlcrdCustIdTypeCache.get(liCustIdTypeCd);

		return laObject == null
			? null
			: (DisabledPlacardCustomerIdTypeData) laObject;
	}

	/**
	 * Get all DisabledPlacardCustomerIdTypeData
	 *  
	 * @return Vector
	 */
	public static Vector getDsabldPlcrdCustIdType()
	{
		Vector lvReturn = new Vector();
		Enumeration e = shtDsabldPlcrdCustIdTypeCache.elements();
		while (e.hasMoreElements())
		{
			DisabledPlacardCustomerIdTypeData laData =
				(DisabledPlacardCustomerIdTypeData) e.nextElement();
			lvReturn.addElement(laData);
		}
		return lvReturn.size() == 0 ? null : lvReturn;
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
		return shtDsabldPlcrdCustIdTypeCache;
	}

	/**
	 * Clear and populate the hashtable with the vector avData
	 * 
	 * @param avData Vector
	 */
	public void setData(Vector avData)
	{
		//reset the hashtable
		shtDsabldPlcrdCustIdTypeCache.clear();

		for (int i = 0; i < avData.size(); i++)
		{
			DisabledPlacardCustomerIdTypeData laData =
				(DisabledPlacardCustomerIdTypeData) avData.get(i);

			shtDsabldPlcrdCustIdTypeCache.put(
				new Integer(laData.getCustIdTypeCd()),
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
		shtDsabldPlcrdCustIdTypeCache = ahtHashtable;
	}
}
