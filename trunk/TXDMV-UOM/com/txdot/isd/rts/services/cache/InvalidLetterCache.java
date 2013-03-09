package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InvalidLetterData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * InvalidLetterCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * The InvalidLetterCache class provides static methods to 
 * retrieve a list of InvalidLetterData based on the item code 
 * input parameter.
 *
 * <p>InvalidLetterCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	5.2.3		06/17/2005 
 * @author Kathy Harrell
 * <br>Creation Date:
 */

public class InvalidLetterCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	 * Hash table of vectors using itmCd as key.
	 */
	private static Hashtable shtInvldLtr = new Hashtable();

	private final static long serialVersionUID = -5300966026988147792L;
	/**
	 * Default InvalidLetterCache constructor.
	 */
	public InvalidLetterCache()
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
		return CacheConstant.INVALID_LETTER_CACHE;
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
		return shtInvldLtr;
	}
	/**
	* Return vector of invalid letters for given item code
	*
	* @param  asItmd String  
	* @return Vector 
	*/

	public static Vector getInvldLtrs(String asItmCd)
	{
		Object laObject = shtInvldLtr.get(asItmCd);

		if (laObject instanceof Vector)
		{
			Vector lvData = (Vector) laObject;
			return lvData;
		}

		else
		{
			return null;
		}

	}
	/**
	* Test main. 
	* 
	* @param aarrArgs String[]
	*/

	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvData = new Vector();

			InvalidLetterData laData1 = new InvalidLetterData();
			laData1.setItmCd("TKP");
			laData1.setInvldLtrCombo("DV");
			lvData.addElement(laData1);

			InvalidLetterData laData2 = new InvalidLetterData();
			laData2.setItmCd("TKP");
			laData2.setInvldLtrCombo("TT");
			lvData.addElement(laData2);

			InvalidLetterData laData3 = new InvalidLetterData();
			laData3.setItmCd("");
			laData3.setInvldLtrCombo("KKK");
			lvData.addElement(laData3);

			InvalidLetterData laData4 = new InvalidLetterData();
			laData4.setItmCd("");
			laData4.setInvldLtrCombo("SHT");
			lvData.addElement(laData4);

			InvalidLetterCache laInvLtrCache = new InvalidLetterCache();
			laInvLtrCache.setData(lvData);

			String lsChkItmCd = "";

			System.out.println(">> " + lsChkItmCd);

			Object laObject =
				InvalidLetterCache.getInvldLtrs(lsChkItmCd);

			if (laObject instanceof Vector)
			{
				lvData = (Vector) laObject;
				
				for (int i = 0; i < lvData.size(); i++)
				{
					InvalidLetterData laData =
						(InvalidLetterData) lvData.get(i);
					String lsInvldLtrCombo = laData.getInvldLtrCombo();
					System.out.println(
						lsChkItmCd + " " + lsInvldLtrCombo);
				}

				System.out.println("done");
			}
			else
			{
				System.out.println(lsChkItmCd + " " + "Found Nothing");
			}
		}

		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
	/**
	* Clear and populate the hashtable with the vector 
	* avInvalidLetterData
	*
	* @param avInvalidLetterData Vector 
	*/

	public void setData(Vector avInvalidLetterData)
	{
		//reset hash table
		shtInvldLtr.clear();
		
		for (int i = 0; i < avInvalidLetterData.size(); i++)
		{
			InvalidLetterData laData =
				(InvalidLetterData) avInvalidLetterData.get(i);
			String lsPrimaryKey = laData.getItmCd();
			
			if (shtInvldLtr.containsKey(lsPrimaryKey))
			{
				Vector lvData = (Vector) shtInvldLtr.get(lsPrimaryKey);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laData);
				shtInvldLtr.put(lsPrimaryKey, lvData);
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
		shtInvldLtr = ahtHashtable;
	}
}
