package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RegistrationClassData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * RegistrationClassCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/24/2001	setData() method created
 * R Duggirala	08/29/2001	Add comments
 * J Peters		10/16/2001	Added Compare method 
 * K Harrell	06/17/2005	Java 1.4 Work 
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * The RegistrationClassCache class provides static method to 
 * retrieve a vector of RegistrationClassCache objects based upon key 
 *
 * <p>RegistrationClassCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	5.2.3 			06/17/2005
 * @author	Rakesh Duggirala
 * <br>Creation Date:		01/04/2002 13:23:30 
 */

public class RegistrationClassCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	* A hashtable of vectors with vehClassCd+regClassCd as key
	*/

	private static Hashtable shtRegisClass = new Hashtable();

	private final static long serialVersionUID = -4006624115876151467L;
	/**
	* RegistrationClassCache constructor comment.
	*/

	public RegistrationClassCache()
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
		return CacheConstant.REGISTRATION_CLASS_CACHE;
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
		return shtRegisClass;
	}
	/**
	 * Returns the RegistrationClassData object based on the input 
	 * String asVehClassCd, int aiRegClassCd, int aiEffDate
	 * 
	 * @param asVehClassCd String
	 * @param aiRegClassCd int
	 * @param aiEffDate int
	 * @return RegistrationClassData  
	 */

	public static RegistrationClassData getRegisClass(
		String asVehClassCd,
		int aiRegClassCd,
		int aiEffDate)
	{

		RegistrationClassData laRegClassData = null;

		String lsPrimaryKey =
			UtilityMethods.constructPrimaryKey(
				new String[] {
					asVehClassCd,
					String.valueOf(aiRegClassCd)});

		if (shtRegisClass.containsKey(lsPrimaryKey))
		{
			Vector lvRegClassData =
				(Vector) shtRegisClass.get(lsPrimaryKey);

			for (int i = 0; i < lvRegClassData.size(); i++)
			{
				RegistrationClassData laData =
					(RegistrationClassData) lvRegClassData.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
					laRegClassData = laData;
					break;
				}
			}
		}
		return laRegClassData;

	}
	/**
	 * Returns a vector of RegistrationClassData objects whose dates are
	 * effective.
	 *
	 * @param aiEffDate int
	 * @return Vector 
	 */

	public static Vector getVehClassCds(int aiEffDate)
	{

		Vector lvReturn = new Vector();
		Vector lvDistinct = new Vector();
		boolean lbDuplicateFlag = false;

		for (Enumeration e1 = shtRegisClass.elements();
			e1.hasMoreElements();
			)
		{
			Vector lvData = (Vector) e1.nextElement();
			for (int i = 0; i < lvData.size(); i++)
			{
				RegistrationClassData laData =
					(RegistrationClassData) lvData.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
					lvReturn.addElement(laData);
				}
			}
		}

		if (lvReturn.size() == 0)
		{
			return null;
		}
		else
		{
			for (int i = 0; i < lvReturn.size(); i++)
			{
				RegistrationClassData laResultData =
					(RegistrationClassData) lvReturn.get(i);
				for (int j = 0; j < lvDistinct.size(); j++)
				{
					RegistrationClassData laDistinctData =
						(RegistrationClassData) lvDistinct.get(j);
					if (laResultData
						.getVehClassCd()
						.equals(laDistinctData.getVehClassCd()))
					{
						lbDuplicateFlag = true;
					}
				}

				if (!lbDuplicateFlag)
				{
					lvDistinct.addElement(laResultData);
				}
				lbDuplicateFlag = false;

			}

			UtilityMethods.sort(lvDistinct);
			return lvDistinct;
		}
	}
	/**
	 * Test Main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] args)
	{

		try
		{
			Vector lvData = new Vector();

			RegistrationClassData laData1 = new RegistrationClassData();
			laData1.setVehClassCd("V_Cd_02");
			laData1.setRegClassCd(1);
			laData1.setRTSEffDate(20010101);
			laData1.setRTSEffEndDate(20010328);
			lvData.addElement(laData1);

			RegistrationClassData laData2 = new RegistrationClassData();
			laData2.setVehClassCd("V_Cd_022");
			laData2.setRegClassCd(2);
			laData2.setRTSEffDate(20010301);
			laData2.setRTSEffEndDate(20010328);
			lvData.addElement(laData2);

			RegistrationClassData laData3 = new RegistrationClassData();
			laData3.setVehClassCd("V_Cd_012");
			laData3.setRegClassCd(1);
			laData3.setRTSEffDate(20010201);
			laData3.setRTSEffEndDate(20010328);
			lvData.addElement(laData3);

			RegistrationClassData laData4 = new RegistrationClassData();
			laData4.setVehClassCd("V_Cd_012");
			laData4.setRegClassCd(29);
			laData4.setRTSEffDate(20010101);
			laData4.setRTSEffEndDate(20010301);
			lvData.addElement(laData4);

			RegistrationClassCache laRegistrationClassCache =
				new RegistrationClassCache();

			laRegistrationClassCache.setData(lvData);

			RegistrationClassData laData =
				RegistrationClassCache.getRegisClass(
					"V_Cd_012",
					1,
					20010201);

			System.out.println(
				laData.getVehClassCd()
					+ "---"
					+ laData.getRegClassCd()
					+ "---"
					+ laData.getRTSEffDate());

			lvData = RegistrationClassCache.getVehClassCds(20010206);

			for (int i = 0; i < lvData.size(); i++)
			{
				laData = (RegistrationClassData) lvData.elementAt(i);
				System.out.println(
					laData.getVehClassCd()
						+ "---"
						+ laData.getRegClassCd()
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
	 * Clear and populate the hashtable with the vector regisClassDataVector
	 * 
	 * @param avRegisClassData Vector 
	 */

	public void setData(Vector avRegisClassData)
	{
		shtRegisClass.clear();

		for (int i = 0; i < avRegisClassData.size(); i++)
		{
			RegistrationClassData laData =
				(RegistrationClassData) avRegisClassData.get(i);
			String lsVehClassCd = laData.getVehClassCd();
			int liRegClassCd = laData.getRegClassCd();
			String lsPrimaryKey =
				UtilityMethods.constructPrimaryKey(
					new String[] {
						lsVehClassCd,
						String.valueOf(liRegClassCd)});

			if (shtRegisClass.containsKey(lsPrimaryKey))
			{
				Vector lvData =
					(Vector) shtRegisClass.get(lsPrimaryKey);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laData);
				shtRegisClass.put(lsPrimaryKey, lvData);
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
		shtRegisClass = ahtHashtable;
	}
}
