package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.IndicatorDescriptionsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * IndicatorDescriptionsCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy 	08/31/2001 	Finished class
 * K Harrell	06/16/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	04/11/2010	get all data for ResrvReasonCd
 * 							 (or any IndiName) 
 * 							add getIndiDescs(String)
 * 							defect 10441 Ver POS_640   
 *----------------------------------------------------------------------
 */

/**
 * The IndicatorDescriptionsCache class provides static methods to 
 * retrieve a DocumentTypesData based on different input parameters.
 *
 * <p>IndicatorDescriptionsCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	POS_640		04/11/2010
 * @author	Michael Abernethy 
 * <br>Creation Date: 	 	  
 *
 */

public class IndicatorDescriptionsCache extends GeneralCache
{
	private static Hashtable shtIndiDescs;

	/**
	 *	Flag to return all Indicator Descriptions.
	 */
	public final static int GET_ALL = 0;

	/**
	 *  Flag to return only Indicator Descriptions with "JNKCD" as its indicator name.
	 */
	public final static int JNKCD = 1;

	private final static String JNKCDSTRING = "JNKCD";
	private final static String DELIM = "#";

	private final static long serialVersionUID = 7016191439236698693L;
	/**
	 * Creates an IndicatorDescriptionsCache.
	 */
	public IndicatorDescriptionsCache()
	{
		super();
		shtIndiDescs = new Hashtable();
	}
	/**
	 * Returns the cache function id
	 * @return int the cache function id
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.INDICATOR_DESCRIPTIONS_CACHE;
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
		return shtIndiDescs;
	}

	/**
	 * Returns a IndicatorDescriptionData given an indicator description
	 * and an indicator field value.
	 * 
	 * @param  asIndiName
	 * @param  asIndiFieldValue
	 * @return IndicatorDescriptionsData 
	 */
	public static IndicatorDescriptionsData getIndiDesc(
		String asIndiName,
		String asIndiFieldValue)
	{
		String lsKey = asIndiName + DELIM + asIndiFieldValue;
		Object laObject = shtIndiDescs.get(lsKey);
		if (laObject instanceof IndicatorDescriptionsData)
		{
			return (IndicatorDescriptionsData) laObject;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns a vector of IndicatorDescriptionsData given a flag.
	 * <p>Flags are either GET_ALL to return all records or JNKCD to 
	 * return only records with "JNKCD" as its indicator name.
	 * 
	 * @param  aiFlag
	 * @return Vector
	 * @throws RTSException   
	 */
	public static Vector getIndiDescs(int aiFlag) throws RTSException
	{
		Vector lvValues = new Vector(shtIndiDescs.values());

		if (aiFlag == GET_ALL)
		{
			return lvValues;
		}
		else if (aiFlag == JNKCD)
		{
			Vector lvReturn = new Vector();
			for (int i = 0; i < lvValues.size(); i++)
			{
				IndicatorDescriptionsData laData =
					(IndicatorDescriptionsData) lvValues.get(i);
				if (laData.getIndiName().equals(JNKCDSTRING))
				{
					lvReturn.add(laData);
				}
			}
			return lvReturn;
		}
		else
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new Exception("Illegal Flag"));
		}
	}

	/**
	 * Return vector of Indicator Description Data for given IndiName
	 * 
	 * @param asIndiName
	 * @return Vector; 
	 */
	public static Vector getIndiDescs(String asIndiName)
	{
		asIndiName = asIndiName.trim();
		Vector lvValues = new Vector(shtIndiDescs.values());
		Vector lvReturn = new Vector();
		for (int i = 0; i < lvValues.size(); i++)
		{
			IndicatorDescriptionsData laData =
				(IndicatorDescriptionsData) lvValues.elementAt(i);

			if (laData.getIndiName().trim().equals(asIndiName))
			{
				lvReturn.addElement(laData);
			}
		}
		return lvReturn;
	}

	/**
	 * Test Main
	 * 
	 * @param  aarrArgs String[]
	 * @throws RTSException 
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		IndicatorDescriptionsData laIndi1 =
			new IndicatorDescriptionsData();
		laIndi1.setIndiName("JNKCD");
		laIndi1.setIndiDesc("A");
		laIndi1.setIndiFieldValue("1");
		IndicatorDescriptionsData laIndi2 =
			new IndicatorDescriptionsData();
		laIndi2.setIndiName("JNFFFKCD");
		laIndi2.setIndiDesc("A");
		laIndi2.setIndiFieldValue("2");
		IndicatorDescriptionsData laIndi3 =
			new IndicatorDescriptionsData();
		laIndi3.setIndiName("JNFKCD");
		laIndi3.setIndiDesc("B");
		laIndi3.setIndiFieldValue("2");
		Vector lvValues = new Vector();
		lvValues.add(laIndi1);
		lvValues.add(laIndi2);
		lvValues.add(laIndi3);
		IndicatorDescriptionsCache laIndiDescCache =
			new IndicatorDescriptionsCache();
		laIndiDescCache.setData(lvValues);

		Vector lvReturn1 =
			IndicatorDescriptionsCache.getIndiDescs(
				IndicatorDescriptionsCache.JNKCD);
		if (lvReturn1.get(0) == laIndi1)
		{
			System.out.println("Test 1 = true");
		}
		else
		{
			System.out.println("Test 1 = false");
		}

		Vector lvReturn2 =
			IndicatorDescriptionsCache.getIndiDescs(
				IndicatorDescriptionsCache.GET_ALL);
		if (lvReturn2.size() == 3)
		{
			System.out.println("Test 2 = true");
		}
		else
		{
			System.out.println("Test 2 = false");
		}

		IndicatorDescriptionsData lvReturn3 =
			IndicatorDescriptionsCache.getIndiDesc("JNFKCD", "2");
		if (lvReturn3 == laIndi3)
		{
			System.out.println("Test 3 = true");
		}
		else
		{
			System.out.println("Test 3 = false");
		}

	}

	/**
	 *  Sets the data in the hashtable
	 * 
	 * @param avData Vector 
	 */
	public void setData(Vector avData)
	{
		shtIndiDescs.clear();

		for (int i = 0; i < avData.size(); i++)
		{
			IndicatorDescriptionsData laData =
				(IndicatorDescriptionsData) avData.get(i);
			String lsPrimaryKey =
				laData.getIndiName()
					+ DELIM
					+ laData.getIndiFieldValue();
			shtIndiDescs.put(lsPrimaryKey, laData);
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
		shtIndiDescs = ahtHashtable;
	}
}
