package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateGroupIdData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * PlateGroupIdCache.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created
 * 							defect 9085  Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * The PlateGroupIdCache class provides static methods to 
 * retrieve a DocumentTypesData based on different input parameters.
 *
 * <p>PlateGroupIdCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Special Plates	01/31/2007
 * @author	Kathy Harrell  
 * <br>Creation Date: 		01/31/2007  17:40:00	  
 *
 */

public class PlateGroupIdCache
	extends GeneralCache
	implements java.io.Serializable
{

	static final long serialVersionUID = 819233115662299197L;

	private static Hashtable shtPlateGroupIdCache = new Hashtable();

	/**
	 * PlateGroupIdCache constructor comment.
	 */
	public PlateGroupIdCache()
	{
		super();

	}
	/**
	 * Return the CacheConstant for this cache type.
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.PLT_GRP_ID_CACHE;
	}
	/**
	 * Gets the PlateGroupId Data Object.  Returns null if object 
	 * does not exist.
	 * 
	 * @param  asPltGrpId String
	 * @return PlateGroupIdData
	 */
	public static PlateGroupIdData getPltGrpId(String asPltGrpId)
	{
		Object laObject = shtPlateGroupIdCache.get(asPltGrpId);
		if (laObject != null)
		{
			return (PlateGroupIdData) laObject;
		}
		else
		{
			return null;
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
		return shtPlateGroupIdCache;
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avPltGrpIdData 
	 * 
	 * @param avPltGrpIdData	Vector
	 */
	public void setData(Vector avPltGrpIdData)
	{
		//reset the hashtable
		shtPlateGroupIdCache.clear();

		for (int i = 0; i < avPltGrpIdData.size(); i++)
		{
			PlateGroupIdData laData =
				(PlateGroupIdData) avPltGrpIdData.get(i);

			shtPlateGroupIdCache.put(laData.getPltGrpId(), laData);
		}
		return; 
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
		shtPlateGroupIdCache = ahtHashtable;
	}
}
