package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TxDOTHolidayData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 *
 * TxDOTHolidayCache.java
 *
 * (c) Texas Department of Transportation 2007
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/05/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/08/2007	deleted getHolidayDate()
 * 							added isHoliday()	
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/08/2011	deprecated 
 * 							defect 9919 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */
/**
 * TxDOTHolidayCache class extends GeneralCache.
 * The TxDOTHolidayCache class provides static methods to 
 * retrieve a particular or all TxDOTHolidayData based 
 * on the input parameter.
 * 
 * @version	6.9.0	10/08/2011
 * @author 	Kathy Harrell
 * @since			03/05/2007  18:24:00 
 * @deprecated
 */

public class TxDOTHolidayCache extends GeneralCache
{
	static final long serialVersionUID = -4525746145758868289L;
	
	/**
	* A hashtable of vectors with HolidayDate as key
	*/
	private static Hashtable shtTxDOTHoliday = new Hashtable();

	/**
	 * TxDOTHolidayCache default constructor.  Calls super();
	 */
	public TxDOTHolidayCache()
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
		//return CacheConstant.TXDOT_HOLIDAY_CACHE;
		return 0; 
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
		return shtTxDOTHoliday;
	}
	/**
	 * Gets the TxDOTHoliday data object.  
	 * Returns null if object does not exist.
	 * 
	 * @param aiDate int
	 * @return boolean
	 */
	public static boolean isHoliday(int aiDate)
	{
		boolean lbHoliday = false; 
		Object laObject = shtTxDOTHoliday.get(new Integer(aiDate));
		if (laObject != null)
		{
			lbHoliday = true;
		}
		return lbHoliday;
	}
	/**
	 * Gets the TxDOTHoliday data object vector.  Returns null if no 
	 * objects exist.
	 * 
	 * @return Vector 
	 */
	public static Vector getAllTxDOTHolidays()
	{

		Vector lvTxDOTHolidayData = new Vector();
		Enumeration e = shtTxDOTHoliday.elements();

		while (e.hasMoreElements())
		{
			TxDOTHolidayData laTxDOTHolidayData =
				(TxDOTHolidayData) e.nextElement();
			lvTxDOTHolidayData.addElement(laTxDOTHolidayData);
		}

		if (lvTxDOTHolidayData.size() == 0)
		{
			return null;
		}
		else
		{
			return lvTxDOTHolidayData;
		}
	}
	/**
	 * Main method for testing TxDOT Holiday Cache class methods
	 * 
	 * @param  aarrArgs String[]
	 * @throws RTSException
	 */
	public static void main(String[] args) throws RTSException
	{
		try
		{
			Vector lvData = new Vector();

			TxDOTHolidayData laData1 = new TxDOTHolidayData();
			laData1.setHolidayDate(20070124);
			laData1.setHolidayDesc("HOLIDAY - 1");
			lvData.addElement(laData1);

			TxDOTHolidayData laData2 = new TxDOTHolidayData();
			laData2.setHolidayDate(20070217);
			laData2.setHolidayDesc("HOLIDAY - 2");
			lvData.addElement(laData2);

			TxDOTHolidayData laData3 = new TxDOTHolidayData();
			laData3.setHolidayDate(20070717);
			laData3.setHolidayDesc("HOLIDAY - 3");
			lvData.addElement(laData3);
			TxDOTHolidayCache laTxDOTHolidayCache =
				new TxDOTHolidayCache();
			laTxDOTHolidayCache.setData(lvData);

			lvData = TxDOTHolidayCache.getAllTxDOTHolidays();

			for (int i = 0; i < lvData.size(); i++)
			{
				TxDOTHolidayData laTxDOTHolidayData =
					(TxDOTHolidayData) lvData.get(i);
				System.out.println(
					"TxDOT Holiday date: "
						+ laTxDOTHolidayData.getHolidayDate());
				System.out.println(
					"TxDOT Holiday desc: "
						+ laTxDOTHolidayData.getHolidayDesc());
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
	 * Clear and populate the hashtable with the vector avTxDOTHolidayData.
	 * 
	 * Populates TxDOTHolidayData vector into a hashtable.
	 * 
	 * @param avTxDOTHolidayData Vector 
	 * 
	 */
	public void setData(Vector avTxDOTHolidayData)
	{
		//reset the hashtable
		shtTxDOTHoliday.clear();
		for (int i = 0; i < avTxDOTHolidayData.size(); i++)
		{
			TxDOTHolidayData laData =
				(TxDOTHolidayData) avTxDOTHolidayData.get(i);
			shtTxDOTHoliday.put(new Integer(laData.getHolidayDate()), laData);
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
		shtTxDOTHoliday = ahtHashtable;
	}
}
