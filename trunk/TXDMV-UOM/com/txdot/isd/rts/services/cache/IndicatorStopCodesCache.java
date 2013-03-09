package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.IndicatorStopCodesData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * IndicatorStopCodesCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy 	08/31/2001 	Finished class
 * K Harrell	06/16/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3
 * B Hargrove	01/17/2008	add getIndiStopCdVec()
 * 							defect 9502 Ver 3 Amigos Prep  
 * B Hargrove	01/14/2008	Add method to return a vector of all
 * 							Indi Stop Codes.
 * 							add getIndiStopCdVec()
 * 							defect 9502 Ver 3_Amigos_PH_A 
 *----------------------------------------------------------------------
 */

/**
 * The IndicatorStopCodesCache class provides static methods to 
 * retrieve a DocumentTypesData based on different input parameters.
 *
 * <p>IndicatorStopCodesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	3 Amigos Prep		01/17/2008 
 * @author	Michael Abernethy 
 * <br>Creation Date: 	 	  
 *
 */

public class IndicatorStopCodesCache extends GeneralCache
{
	private static Hashtable shtIndiStopCds;

	private final static long serialVersionUID = -3514275333143724024L;

	/**
	 * Creates an IndiStopCdsCache.
	 */
	public IndicatorStopCodesCache()
	{
		super();
		shtIndiStopCds = new Hashtable();
	}
	
	/**
	 * Returns the cache function id
	 * 
	 * @return int  
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.INDICATOR_STOP_CODES_CACHE;
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
		return shtIndiStopCds;
	}
	
	/**
	 * Returns an IndicatorStopCodesData given the indiName, 
	 * indiFieldValue, and transCd
	 * 
	 * @param asIndiName
	 * @param asIndiFieldValue
	 * @param asTransCd
	 * @return IndicatorStopCodesData
	 */
	public static IndicatorStopCodesData getIndiStopCd(
		String asIndiName,
		String asIndiFieldValue,
		String asTransCd)
	{
		String lsKey =
			UtilityMethods.constructPrimaryKey(
				new String[] {
					asIndiName,
					asIndiFieldValue,
					asTransCd });
		Object laObject = shtIndiStopCds.get(lsKey);

		if (laObject instanceof IndicatorStopCodesData)
		{
			return (IndicatorStopCodesData) laObject;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Get a vector of Indicator Stop Codes objects
	 * 
	 * @return Vector
	 */
	public static Vector getIndiStopCdVec()
	{
		if (shtIndiStopCds.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturnVector = new Vector();
			for (Enumeration e = shtIndiStopCds.elements();
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
		IndicatorStopCodesData laIndiStopCdsData1 =
			new IndicatorStopCodesData();
		laIndiStopCdsData1.setIndiName("JNKCD");
		laIndiStopCdsData1.setIndiFieldValue("1");
		laIndiStopCdsData1.setIndiTransCd("A");

		IndicatorStopCodesData laIndiStopCdsData2 =
			new IndicatorStopCodesData();
		laIndiStopCdsData2.setIndiName("JNKCD");
		laIndiStopCdsData2.setIndiFieldValue("2");
		laIndiStopCdsData2.setIndiTransCd("B");

		Vector lvIndiStopCdsData = new Vector();
		lvIndiStopCdsData.add(laIndiStopCdsData1);
		lvIndiStopCdsData.add(laIndiStopCdsData2);

		IndicatorStopCodesCache laIndiStopCdsCache =
			new IndicatorStopCodesCache();
		laIndiStopCdsCache.setData(lvIndiStopCdsData);

		IndicatorStopCodesData laIndiStopCdsData =
			IndicatorStopCodesCache.getIndiStopCd("JNKCD", "1", "A");
		System.out.println(
			"Test = " + (laIndiStopCdsData == laIndiStopCdsData1));

	}
	
	/**
	 * Sets the data in the hashtable
	 * 
	 * @param avData Vector 
	 */
	public void setData(Vector avData)
	{
		shtIndiStopCds.clear();

		for (int i = 0; i < avData.size(); i++)
		{
			IndicatorStopCodesData laIndiStopCdsData =
				(IndicatorStopCodesData) avData.get(i);
			String[] larrKeys = new String[3];
			larrKeys[0] = laIndiStopCdsData.getIndiName();
			larrKeys[1] = laIndiStopCdsData.getIndiFieldValue();
			larrKeys[2] = laIndiStopCdsData.getIndiTransCd();
			String lsPrimaryKey =
				UtilityMethods.constructPrimaryKey(larrKeys);
			shtIndiStopCds.put(lsPrimaryKey, laIndiStopCdsData);
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
		shtIndiStopCds = ahtHashtable;
	}
}
