package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleColorData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * VehicleColorCache.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/05/2011	Created
 * 							defect 10712 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * The VehicleColorCache class provides static method to 
 * retrieve a vector of VehicleColorData objects based 
 * on a key
 *
 * <p>VehicleColorCache is being initialized and populated by the 
 * CacheManager when the system starts up.  The data will be stored 
 * in memory and thus will be accessible until the system shuts down.
 * 
 *
 * @version	6.7.0		01/05/2011
 * @author	Kathy Harrell 
 * <br>Creation Date: 	01/05/2011 12:57:17  
 */
public class VehicleColorCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	 * Vehicle Color hashtable
	 */
	private static Hashtable shtVehColor = new Hashtable();

	static final long serialVersionUID = 6351377259875655542L;

	/**
	 * VehicleColorCache default constructor.
	 */
	public VehicleColorCache()
	{
		super();
	}
	/**
	 * Returns the function id of the VehicleColorCache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.VEHICLE_COLOR_CACHE;
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public java.util.Hashtable getHashtable()
	{
		return shtVehColor;
	}

	/**
	 * Get a vector of Vehicle Color objects
	 * 
	 * @return Vector
	 */
	public static Vector getVehColorVec()
	{
		if (shtVehColor.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturnVector = new Vector();
			for (Enumeration e = shtVehColor.elements();
				e.hasMoreElements();
				)
			{
				lvReturnVector.addElement(e.nextElement());

			}
			return lvReturnVector;
		}
	}
	
	/** 
	 * Get Vehicle Color 
	 * 
	 * @param asVehColorCd
	 * @return VehicleColorData 
	 */
	public static VehicleColorData getVehColor(String asVehColorCd)
	{
		VehicleColorData laData = null;

		if (asVehColorCd != null)
		{
			laData = (VehicleColorData) shtVehColor.get(asVehColorCd);
		}
		return laData;
	}

	/**
	 * Set the data of the cache to contain the elements of the vector
	 * being passed in.
	 * 
	 * @param avVehColor Vector
	 */
	public void setData(Vector avVehColor) throws RTSException
	{
		shtVehColor.clear();
		if (avVehColor != null)
		{
			for (int i = 0; i < avVehColor.size(); i++)
			{
				VehicleColorData laData =
					(VehicleColorData) avVehColor.get(i);

				shtVehColor.put(laData.getVehColorCd(), laData);
			}
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
	public void setHashtable(java.util.Hashtable ahtHashtable)
	{
		shtVehColor = ahtHashtable;
	}
}
