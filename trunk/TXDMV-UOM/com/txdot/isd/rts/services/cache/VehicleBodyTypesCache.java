package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleBodyTypesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * VehicleBodyTypesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The VehicleBodyTypesCache class provides static method to 
 * retrieve a vector of VehicleClassRegistrationClassData objects based 
 * on a key
 *
 * <p>VehicleBodyTypesCache is being initialized and populated by the 
 * CacheManager when the system starts up.  The data will be stored 
 * in memory and thus will be accessible until the system shuts down.
 * 
 *
 * @version	5.2.3		06/19/2005
 * @author	Sunil Govindappa 
 * <br>Creation Date: 	09/05/2001 19:51  
 */

public class VehicleBodyTypesCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* Vehicle Body Types hashtable
	*/
	private static Hashtable shVehBdyTypes = new Hashtable();

	private final static long serialVersionUID = -8769317355852197722L;
	/**
	 * VehicleBodyTypesCache default constructor.
	 */
	public VehicleBodyTypesCache()
	{
		super();
	}
	/**
	 * Returns the function id of the Vehicle Body Types Cache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.VEHICLE_BODY_TYPES_CACHE;
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
		return shVehBdyTypes;
	}
	/**
	 * Get a vector of Vehicle Body Types objects
	 * 
	 * @return Vector
	 */
	public static Vector getVehBdyTypesVec()
	{
		if (shVehBdyTypes.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturnVector = new Vector();
			for (Enumeration e = shVehBdyTypes.elements();
				e.hasMoreElements();
				)
			{
				lvReturnVector.addElement(e.nextElement());

			}
			return lvReturnVector;

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
			Vector lvInputVec = new Vector();

			VehicleBodyTypesData lvVBTD1 = new VehicleBodyTypesData();
			lvVBTD1.setVehBdyType("AAA");
			lvVBTD1.setVehBdyTypeDesc("AAA desc");
			lvInputVec.addElement(lvVBTD1);

			VehicleBodyTypesData lvVBTD2 = new VehicleBodyTypesData();
			lvVBTD2.setVehBdyType("BBB");
			lvVBTD2.setVehBdyTypeDesc("BBB desc");
			lvInputVec.addElement(lvVBTD2);

			VehicleBodyTypesData lvVBTD3 = new VehicleBodyTypesData();
			lvVBTD3.setVehBdyType("CCC");
			lvVBTD3.setVehBdyTypeDesc("CCC desc");
			lvInputVec.addElement(lvVBTD3);

			VehicleBodyTypesData lvVBTD4 = new VehicleBodyTypesData();
			lvVBTD4.setVehBdyType("DDD");
			lvVBTD4.setVehBdyTypeDesc("DDD desc");
			lvInputVec.addElement(lvVBTD4);

			VehicleBodyTypesCache lvVBTCache =
				new VehicleBodyTypesCache();
			lvVBTCache.setData(lvInputVec);

			lvInputVec = VehicleBodyTypesCache.getVehBdyTypesVec();

			int i = 0;

			for (Enumeration e = lvInputVec.elements();
				e.hasMoreElements();
				)
			{
				i++;
				VehicleBodyTypesData laData =
					(VehicleBodyTypesData) e.nextElement();

				System.out.println(
						""
						+ i
						+ "--"
						+ laData.getVehBdyType()
						+ "--"
						+ laData.getVehBdyTypeDesc());
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
	 * @param avVehBdyTypsData Vector
	 */
	public void setData(Vector avVehBdyTypsDataVector)
		throws RTSException
	{
		shVehBdyTypes.clear();
		if (avVehBdyTypsDataVector == null)
		{
			return;
		}
		for (int i = 0; i < avVehBdyTypsDataVector.size(); i++)
		{
			VehicleBodyTypesData laVehBdyTypsData =
				(VehicleBodyTypesData) avVehBdyTypsDataVector.get(i);

			shVehBdyTypes.put(
				laVehBdyTypsData.getVehBdyType()
					+ "#"
					+ laVehBdyTypsData.getVehBdyTypeDesc(),
				laVehBdyTypsData);
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
		shVehBdyTypes = ahtHashtable;
	}
}
