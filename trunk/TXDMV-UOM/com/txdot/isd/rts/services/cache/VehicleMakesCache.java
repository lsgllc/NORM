package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleMakesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * VehicleMakesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/10/2010	add getVehMkDesc()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * The VehicleMakesCache class provides static method to 
 * retrieve a vector of VehicleClassRegistrationClassData objects based 
 * on a key
 *
 * <p>VehicleMakesCache is being initialized and populated by the 
 * CacheManager when the system starts up.  The data will be stored 
 * in memory and thus will be accessible until the system shuts down.
 * 
 * @version	6.5.0		06/10/2010
 * @author	Sunil Govindappa 
 * <br>Creation Date: 	09/05/2001 19:51  
 */

public class VehicleMakesCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* Vehicle Makes hashtable
	*/
	private static Hashtable shtVehMks = new Hashtable();

	private final static long serialVersionUID = 5924347384269217947L;
	/**
	 * VehicleMakesCache default constructor 
	 */
	public VehicleMakesCache()
	{
		super();
	}
	/**
	 * Returns the function id of the Vehicle Makes Cache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.VEHICLE_MAKES_CACHE;
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
		return shtVehMks;
	}

	/**
	 * Return Vehicle Make Description  
	 * 
	 * @return String 
	 */
	public static Vector getVehMkDesc(String asVehMk)
	{
		Vector lvReturn = new Vector(); 
		
		if (shtVehMks.size() != 0)
		{
			for (Enumeration e = shtVehMks.elements();
				e.hasMoreElements();
				)
			{
				VehicleMakesData laVehMksData = (VehicleMakesData) e.nextElement();
				String lsVehMk = laVehMksData.getVehMk(); 
				if (lsVehMk != null && lsVehMk.equals(asVehMk)) 
				{
					lvReturn.add(laVehMksData.getVehMkDesc());
				} 
			}
		}
		UtilityMethods.sort(lvReturn);  
		return lvReturn; 
	}

	/**
	 * Get a vector of Vehicle Make objects
	 * 
	 * @return Vector
	 */
	public static Vector getVehMks()
	{
		if (shtVehMks.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturn = new Vector();
			for (Enumeration e = shtVehMks.elements();
				e.hasMoreElements();
				)
			{
				lvReturn.addElement(e.nextElement());

			}
			return lvReturn;

		}
	}
	/**
	 * Test main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvData = new Vector();

			VehicleMakesData laData1 = new VehicleMakesData();
			laData1.setVehMk("AAA");
			laData1.setVehMkDesc("AAA VehMk Desc");
			lvData.addElement(laData1);

			VehicleMakesData laData2 = new VehicleMakesData();
			laData2.setVehMk("BBB");
			laData2.setVehMkDesc("BBB VehMk Desc");
			lvData.addElement(laData2);

			VehicleMakesData laData3 = new VehicleMakesData();
			laData3.setVehMk("CCC");
			laData3.setVehMkDesc("CCC VehMk Desc");
			lvData.addElement(laData3);

			VehicleMakesData laData4 = new VehicleMakesData();
			laData4.setVehMk("DDD");
			laData4.setVehMkDesc("DDD VehMk Desc");
			lvData.addElement(laData4);

			VehicleMakesCache laVMCache = new VehicleMakesCache();

			laVMCache.setData(lvData);

			lvData = VehicleMakesCache.getVehMks();

			int i = 0;

			for (Enumeration e = lvData.elements();
				e.hasMoreElements();
				)
			{
				i++;
				VehicleMakesData laData =
					(VehicleMakesData) e.nextElement();

				System.out.println(
					""
						+ i
						+ "--"
						+ laData.getVehMk()
						+ "--"
						+ laData.getVehMkDesc());
			}

			System.out.println("done");

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Set the data of the cache to contain the elements of the vector
	 * being passed in.
	 * 
	 * @param  avVehMkData Vector
	 * @throws RTSException 
	 */
	public void setData(Vector avVehMkData) throws RTSException
	{
		shtVehMks.clear();

		if (avVehMkData == null)
		{
			return;
		}

		for (int i = 0; i < avVehMkData.size(); i++)
		{
			VehicleMakesData laVehicleMakesData =
				(VehicleMakesData) avVehMkData.get(i);
			shtVehMks.put(
				laVehicleMakesData.getVehMk()
					+ "#"
					+ laVehicleMakesData.getVehMkDesc(),
				laVehicleMakesData);
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
		shtVehMks = ahtHashtable;
	}
}
