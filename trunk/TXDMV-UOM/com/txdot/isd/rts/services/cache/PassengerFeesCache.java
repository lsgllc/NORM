package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PassengerFeesData;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 *
 * PassengerFeesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/07/2001	Change getPassFee to static.
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  			 
 * ---------------------------------------------------------------------
 */

/**
 * The PassengerFeesCache class provides static methods to 
 * retrieve a PassengerFeesData object based 
 * on different input parameters.
 *
 * <p>PassengerFeesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	5.2.3		06/17/2005 
 * @author	Todd Pederson
 * <br>Creation Date: 	 
 */

public class PassengerFeesCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* A hashtable of vectors with regClassCd as key
	*/
	private static Hashtable shtPassFees = new Hashtable();

	private final static long serialVersionUID = 2416646113294390503L;

	/**
	 * PassFeesCache constructor comment.
	 */
	public PassengerFeesCache()
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
		return CacheConstant.PASSENGER_FEES_CACHE;
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return the Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtPassFees;
	}
	/**
	 * Returns the PassengerFeesData object based on the input 
	 * asRegClassCd, aiEffDate and aiVehModlYr.
	 * 
	 * @param asRegClassCd String
	 * @param aiEffDate  int
	 * @param aiVehModlYr int
	 * @return PassengerFeesData 
	 */

	public static PassengerFeesData getPassFee(
		String lsRegClassCd,
		int aiEffDate,
		int aiVehModlYr)
	{

		Object laObject = shtPassFees.get(lsRegClassCd);

		if (laObject instanceof Vector)
		{
			Vector lvData = (Vector) laObject;

			for (int i = 0; i < lvData.size(); i++)
			{
				PassengerFeesData laData =
					(PassengerFeesData) lvData.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate())
					&& (aiVehModlYr >= laData.getBegModlYr())
					&& (aiVehModlYr <= laData.getEndModlYr()))
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
		try
		{
			Vector lvData = new Vector();

			PassengerFeesData laData1 = new PassengerFeesData();
			laData1.setRTSEffDate(20000101);
			laData1.setRTSEffEndDate(20001231);
			laData1.setRegClassCd(25);
			laData1.setRegFee(new Dollar(40.50));
			laData1.setBegModlYr(0);
			laData1.setEndModlYr(1994);
			lvData.addElement(laData1);

			PassengerFeesData laData2 = new PassengerFeesData();
			laData2.setRTSEffDate(20000101);
			laData2.setRTSEffEndDate(20001231);
			laData2.setRegClassCd(25);
			laData2.setRegFee(new Dollar(50.50));
			laData2.setBegModlYr(1995);
			laData2.setEndModlYr(1997);
			lvData.addElement(laData2);

			PassengerFeesData laData3 = new PassengerFeesData();
			laData3.setRTSEffDate(20000101);
			laData3.setRTSEffEndDate(20001231);
			laData3.setRegClassCd(25);
			laData3.setRegFee(new Dollar(58.50));
			laData3.setBegModlYr(1998);
			laData3.setEndModlYr(9999);
			lvData.addElement(laData3);

			PassengerFeesData laData4 = new PassengerFeesData();
			laData4.setRTSEffDate(20010101);
			laData4.setRTSEffEndDate(99991231);
			laData4.setRegClassCd(25);
			laData4.setRegFee(new Dollar(40.50));
			laData4.setBegModlYr(0);
			laData4.setEndModlYr(1995);
			lvData.addElement(laData4);

			PassengerFeesData laData5 = new PassengerFeesData();
			laData5.setRTSEffDate(20010101);
			laData5.setRTSEffEndDate(99991231);
			laData5.setRegClassCd(25);
			laData5.setRegFee(new Dollar(50.50));
			laData5.setBegModlYr(1996);
			laData5.setEndModlYr(1998);
			lvData.addElement(laData5);

			PassengerFeesData laData6 = new PassengerFeesData();
			laData6.setRTSEffDate(20010101);
			laData6.setRTSEffEndDate(99991231);
			laData6.setRegClassCd(25);
			laData6.setRegFee(new Dollar(58.50));
			laData6.setBegModlYr(1999);
			laData6.setEndModlYr(9999);
			lvData.addElement(laData6);

			PassengerFeesData laData7 = new PassengerFeesData();
			laData7.setRTSEffDate(20010101);
			laData7.setRTSEffEndDate(99991231);
			laData7.setRegClassCd(8);
			laData7.setRegFee(new Dollar(40.50));
			laData7.setBegModlYr(0);
			laData7.setEndModlYr(1995);
			lvData.addElement(laData7);

			PassengerFeesData laData8 = new PassengerFeesData();
			laData8.setRTSEffDate(20010101);
			laData8.setRTSEffEndDate(99991231);
			laData8.setRegClassCd(8);
			laData8.setRegFee(new Dollar(50.50));
			laData8.setBegModlYr(1996);
			laData8.setEndModlYr(1998);
			lvData.addElement(laData8);

			PassengerFeesData laData9 = new PassengerFeesData();
			laData9.setRTSEffDate(20010101);
			laData9.setRTSEffEndDate(99991231);
			laData9.setRegClassCd(8);
			laData9.setRegFee(new Dollar(58.50));
			laData9.setBegModlYr(1999);
			laData9.setEndModlYr(9999);
			lvData.addElement(laData9);

			PassengerFeesCache laCache = new PassengerFeesCache();
			laCache.setData(lvData);

			PassengerFeesData laData =
				PassengerFeesCache.getPassFee("25", 20000829, 1999);

			System.out.println(laData.getRegFee());

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Clear and populate the hashtable with the vector dataVector
	 * 
	 * @param avPassFeesData Vector 
	 */
	public void setData(Vector avPassFeesData)
	{

		shtPassFees.clear();

		for (int i = 0; i < avPassFeesData.size(); i++)
		{
			PassengerFeesData laData =
				(PassengerFeesData) avPassFeesData.get(i);
			String lsPrimaryKey = "" + laData.getRegClassCd();
			if (shtPassFees.containsKey(lsPrimaryKey))
			{
				Vector lvPassFeesData =
					(Vector) shtPassFees.get(lsPrimaryKey);
				lvPassFeesData.add(laData);
			}
			else
			{
				Vector lvPassFeesData = new Vector();
				lvPassFeesData.add(laData);
				shtPassFees.put(lsPrimaryKey, lvPassFeesData);
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
		shtPassFees = ahtHashtable;
	}
}
