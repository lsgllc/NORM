package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleDieselTonData;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * VehicleDieselTonCache.java
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
 * The VehicleDieselTonCache class provides static method to 
 * retrieve a vector of VehicleClassRegistrationClassData objects based 
 * on a key
 *
 * <p>VehicleDieselTonCache is being initialized and populated by the 
 * CacheManager when the system starts up.  The data will be stored 
 * in memory and thus will be accessible until the system shuts down.
 * 
 * @version	5.2.3		06/19/2005
 * @author	Michael Abernethy 
 * <br>Creation Date: 	08/28/2001   
 */

public class VehicleDieselTonCache extends GeneralCache
{
	private static Hashtable shtVehDieselTon;

	private final static long serialVersionUID = -2487493583756491177L;

	/**
	 * Creates a VehDieselTonCache.
	 */
	public VehicleDieselTonCache()
	{
		super();

		shtVehDieselTon = new Hashtable();
	}
	/**
	 * Returns the cache function id
	 * 
	 * @return int  
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.VEHICLE_DIESEL_TON_CACHE;
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
		return shtVehDieselTon;
	}
	/**
	 * Returns a VehicleDieselTonData object that has a matching regClassCd and whether
	 * 
	 * @param asRegClassCd String
	 * @param aaVehTon Dollar
	 * @param aiEffDate int
	 * @return VehicleDieselTonData
	 */
	public static VehicleDieselTonData getVehDieselTon(
		String lsRegClassCd,
		Dollar laVehTon,
		int liEffDate)
	{
		Object laObject = shtVehDieselTon.get(lsRegClassCd);
		
		if (laObject instanceof Vector)
		{
			Vector lvData = (Vector) laObject;
			for (int i = 0; i < lvData.size(); i++)
			{
				VehicleDieselTonData laData =
					(VehicleDieselTonData) lvData.get(i);

				if ((liEffDate >= laData.getRTSEffDate())
					&& (liEffDate <= laData.getRTSEffEndDate())
					&& (laVehTon.compareTo(laData.getBegDieselVehTon()) == 1
						|| laVehTon.compareTo(laData.getBegDieselVehTon())
							== 0)
					&& (laVehTon.compareTo(laData.getEndDieselVehTon())
						== -1
						|| laVehTon.compareTo(laData.getEndDieselVehTon())
							== 0))
				{
					return laData;
				}
			}
			return null;
		}
		else
		{
			return null;
		}

	}
	/**
	 * Test main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		VehicleDieselTonData laData1 = new VehicleDieselTonData();
		laData1.setBegDieselVehTon(new Dollar("4.56"));
		laData1.setEndDieselVehTon(new Dollar("4.58"));
		laData1.setRegClassCd(4);
		laData1.setRTSEffDate(19990405);
		laData1.setRTSEffEndDate(19990408);

		VehicleDieselTonData laData2 = new VehicleDieselTonData();
		laData2.setBegDieselVehTon(new Dollar("4.55"));
		laData2.setEndDieselVehTon(new Dollar("4.56"));
		laData2.setRegClassCd(4);
		laData2.setRTSEffDate(19990405);
		laData2.setRTSEffEndDate(19990408);

		VehicleDieselTonData laData3 = new VehicleDieselTonData();
		laData3.setBegDieselVehTon(new Dollar("4.56"));
		laData3.setEndDieselVehTon(new Dollar("4.58"));
		laData3.setRegClassCd(4);
		laData3.setRTSEffDate(19990407);
		laData3.setRTSEffEndDate(19990408);

		Vector lvData = new Vector();
		lvData.add(laData1);
		lvData.add(laData2);
		lvData.add(laData3);

		VehicleDieselTonCache laVehDieselTonCache =
			new VehicleDieselTonCache();
		laVehDieselTonCache.setData(lvData);

		VehicleDieselTonData laData4 =
			VehicleDieselTonCache.getVehDieselTon(
				"4",
				new Dollar("4.57"),
				19990406);

		System.out.println("Test 1 = " + (laData4 == laData1));

		VehicleDieselTonData laData5 =
			VehicleDieselTonCache.getVehDieselTon(
				"4",
				new Dollar("4.57"),
				19990409);

		System.out.println("Test 2 = " + (laData5 != laData1));

	}
	/**
	 *  Sets the data in the hashtable
	 * 
	 * @param avData Vector
	 */
	public void setData(Vector avData)
	{
		shtVehDieselTon.clear();

		for (int i = 0; i < avData.size(); i++)
		{
			VehicleDieselTonData laData =
				(VehicleDieselTonData) avData.get(i);

			String lsPrimaryKey = "" + laData.getRegClassCd();

			if (shtVehDieselTon.containsKey(lsPrimaryKey))
			{
				Vector lvData =
					(Vector) shtVehDieselTon.get(lsPrimaryKey);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.add(laData);
				shtVehDieselTon.put(lsPrimaryKey, lvData);
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
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtVehDieselTon = ahtHashtable;
	}
}
