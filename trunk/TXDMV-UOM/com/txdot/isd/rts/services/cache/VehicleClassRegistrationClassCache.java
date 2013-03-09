package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleClassRegistrationClassData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * VehicleClassRegistrationClassCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	09/07/2001  Added Methods 
 * N Ting		09/19/2001  Added Hashtable methods
 * K Harrell	06/19/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The VehicleClassRegistrationClassCache class provides static method to 
 * retrieve a vector of VehicleClassRegistrationClassData objects based 
 * on a key
 *
 * <p>VehicleClassRegistrationClassCache is being initialized and 
 * populated by the CacheManager when the system starts up.  The data 
 * will be stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	5.2.3		06/19/2005
 * @author	Nancy Ting 
 * <br>Creation Date: 	09/05/2001 11:56:06
 */

public class VehicleClassRegistrationClassCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	* A hashtable of vectors with vehClassCd as key
	*/

	private static Hashtable shtVehClassRegClass = new Hashtable();

	private final static long serialVersionUID = -947865747312877713L;
	/**
	 * VehicleClassRegistrationClassCache constructor comment.
	 */
	public VehicleClassRegistrationClassCache()
	{
		super();
	}
	/**
	 * Returns the function id of the VehicleClassRegistrationClassCache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{

		return CacheConstant.VEHICLE_CLASS_REGISTRATION_CLASS_CACHE;
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
		return shtVehClassRegClass;
	}
	/**
	* Returns a vector of VehicleClassRegistrationClassData objects 
	* whose dates are effective.
	*
	* @param asVehClassCd String 
	* @param aiEffDate int
	* @return Vector 
	*/

	public static Vector getVehClassRegClassDescs(
		String asVehClassCd,
		int aiEffDate)
	{

		Object laObj = shtVehClassRegClass.get(asVehClassCd);

		if (laObj instanceof Vector)
		{
			Vector lvData = (Vector) laObj;

			Vector lvReturn = new Vector();

			for (int i = 0; i < lvData.size(); i++)
			{
				VehicleClassRegistrationClassData laData =
					(VehicleClassRegistrationClassData) lvData.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
					lvReturn.addElement(laData);
				}
			}

			if (lvReturn.size() == 0)
			{
				return null;
			}
			else
			{
				UtilityMethods.sort(lvReturn);
				return lvReturn;
			}
		}

		else
		{
			return null;
		}
	}
	/**
	* TESTING, TESTING
	*/
	public static void main(String[] aarrArgs)
	{

		try
		{
			Vector lvData = new Vector();

			VehicleClassRegistrationClassData laData1 =
				new VehicleClassRegistrationClassData();
			laData1.setVehClassCd("PASS");
			laData1.setRegClassCd(25);
			laData1.setRegClassCdDesc("PASSENGER < 6000");
			laData1.setRTSEffDate(20010101);
			laData1.setRTSEffEndDate(20010229);

			lvData.addElement(laData1);

			VehicleClassRegistrationClassData laData2 =
				new VehicleClassRegistrationClassData();
			laData2.setVehClassCd("PASS");
			laData2.setRegClassCd(25);
			laData2.setRegClassCdDesc("PASSENGER < 6000");
			laData2.setRTSEffDate(20010301);
			laData2.setRTSEffEndDate(20010328);
			lvData.addElement(laData2);

			VehicleClassRegistrationClassData laData3 =
				new VehicleClassRegistrationClassData();
			laData3.setVehClassCd("TRK<=1");
			laData3.setRegClassCd(35);
			laData3.setRegClassCdDesc("TRUCK-LESS/EQL. 1 TON");
			laData3.setRTSEffDate(20010201);
			laData3.setRTSEffEndDate(20010328);
			lvData.addElement(laData3);

			VehicleClassRegistrationClassData laData4 =
				new VehicleClassRegistrationClassData();
			laData4.setVehClassCd("PASS");
			laData4.setRegClassCd(26);
			laData4.setRegClassCdDesc("PASSENGER >=6000");
			laData4.setRTSEffDate(20010101);
			laData4.setRTSEffEndDate(20010229);

			lvData.addElement(laData4);

			VehicleClassRegistrationClassCache laCache =
				new VehicleClassRegistrationClassCache();

			laCache.setData(lvData);

			lvData =
				VehicleClassRegistrationClassCache
					.getVehClassRegClassDescs(
					"TRK<=1",
					20010201);

			for (int i = 0; i < lvData.size(); i++)
			{
				VehicleClassRegistrationClassData laData =
					(
						VehicleClassRegistrationClassData) lvData
							.elementAt(
						i);
				System.out.println(
					laData.getVehClassCd()
						+ "---"
						+ laData.getRegClassCd()
						+ "---"
						+ laData.getRegClassCdDesc()
						+ "---"
						+ laData.getRTSEffDate());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * 
	 * @param avVehClassRegClassData Vector 
	 */

	public void setData(Vector avVehClassRegClassData)
	{

		shtVehClassRegClass.clear();
		for (int i = 0; i < avVehClassRegClassData.size(); i++)
		{
			VehicleClassRegistrationClassData laData =
				(
					VehicleClassRegistrationClassData) avVehClassRegClassData
						.get(
					i);

			String lsVehClassCd = laData.getVehClassCd();
			if (shtVehClassRegClass.containsKey(lsVehClassCd))
			{
				Vector lvData =
					(Vector) shtVehClassRegClass.get(lsVehClassCd);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laData);
				shtVehClassRegClass.put(lsVehClassCd, lvData);
			}
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
		shtVehClassRegClass = ahtHashtable;
	}
}
