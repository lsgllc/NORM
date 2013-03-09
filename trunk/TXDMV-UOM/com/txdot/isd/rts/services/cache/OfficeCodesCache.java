package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OfficeCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 *
 * OfficeCodesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		08/28/2001	Add comments
 * J Kwik		09/06/2001	Convert to Hungarian notation.
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */
/**
 * OfficeCodesCache class extends GeneralCache.
 * The OfficeCodesCache class provides static methods to 
 * retrieve a particular or all OfficeCodesData based 
 * on the input parameter.
 * 
 * @version	5.2.3		06/17/2005
 * @author 	Joseph Kwik
 * <br>Creation Date: 
 */
public class OfficeCodesCache extends GeneralCache
{

	/**
	* A hashtable of vectors with ofcIssuanceCd as key
	*/
	private static Hashtable shtOfcCds = new Hashtable();

	private final static long serialVersionUID = -4525746145758868289L;

	/**
	 * OfficeCodesCache default constructor.  Calls super();
	 */
	public OfficeCodesCache()
	{
		super();
	}
	/**
	 * Implements the GeneralCache.getCacheFunctionId() abstract method.
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.OFFICE_CODES_CACHE;
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
		return shtOfcCds;
	}
	/**
	 * Gets the office code data object.  
	 * Returns null if object does not exist.
	 * 
	 * @param aiOfcIssuanceCd int
	 * @return OfficeCodesData
	 */
	public static OfficeCodesData getOfcCd(int aiOfcIssuanceCd)
	{
		Object laObject = shtOfcCds.get(new Integer(aiOfcIssuanceCd));
		if (laObject != null)
		{
			return (OfficeCodesData) laObject;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Gets the office codes data object vector.  Returns null if no 
	 * objects exist.
	 * 
	 * @return Vector 
	 */
	public static Vector getOfcCds()
	{

		Vector lvOfficeCds = new Vector();
		Enumeration e = shtOfcCds.elements();
		while (e.hasMoreElements())
		{
			OfficeCodesData laData = (OfficeCodesData) e.nextElement();
			lvOfficeCds.addElement(laData);
		}

		if (lvOfficeCds.size() == 0)
		{
			return null;
		}
		else
		{
			return lvOfficeCds;
		}
	}
	/**
	 * Main method for testing office codes cache class methods.
	 * 
	 * @param  aarrArgs String[]
	 * @throws RTSException
	 */
	public static void main(String[] args) throws RTSException
	{
		try
		{
			Vector lvData = new Vector();

			OfficeCodesData laData1 = new OfficeCodesData();
			laData1.setOfcIssuanceCd(1);
			laData1.setOfcIssuanceCdDesc("HEADQUARTERS");
			lvData.addElement(laData1);

			OfficeCodesData laData2 = new OfficeCodesData();
			laData2.setOfcIssuanceCd(2);
			laData2.setOfcIssuanceCdDesc("REGION");
			lvData.addElement(laData2);

			OfficeCodesCache laOfficeCdsCache = new OfficeCodesCache();
			laOfficeCdsCache.setData(lvData);

			lvData = OfficeCodesCache.getOfcCds();

			for (int i = 0; i < lvData.size(); i++)
			{
				OfficeCodesData laOfficeCdsData =
					(OfficeCodesData) lvData.get(i);
				System.out.println(
					"Office code: "
						+ laOfficeCdsData.getOfcIssuanceCd());
				System.out.println(
					"Office code description: "
						+ laOfficeCdsData.getOfcIssuanceCdDesc());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
	/**
	 * Implements the GeneralCache.setData() abstract method.
	 * Clear and populate the hashtable with the vector avOfficeCodesData.
	 * 
	 * Populates officeCodesData vector into a hashtable.
	 * 
	 * @param avOfficeCodesData Vector 
	 * 
	 */
	public void setData(Vector avOfficeCodesData)
	{
		//reset the hashtable
		shtOfcCds.clear();
		for (int i = 0; i < avOfficeCodesData.size(); i++)
		{
			OfficeCodesData laData =
				(OfficeCodesData) avOfficeCodesData.get(i);
			shtOfcCds.put(
				new Integer(laData.getOfcIssuanceCd()),
				laData);
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
		shtOfcCds = ahtHashtable;
	}
}
