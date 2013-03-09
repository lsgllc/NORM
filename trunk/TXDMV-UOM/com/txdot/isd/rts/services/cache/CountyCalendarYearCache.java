package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CountyCalendarYearData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * CountyCalendarYearCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/07/2001	getCntyCalndrYr returns CountyCalendarYearData
 * 							instead of Vector
 * K Harrell	06/15/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */
/**
 * The CountyCalenderYearCache class provides the static method to 
 * retrieve a particular CountyCalenderYearData based 
 * on OfcIssuanceNo and FscalYr.
 *
 * @version	5.2.3		06/15/2005
 * @author	Joe Peters
 * <br>Creation Date: 	08/30/2001 
 *
 */
public class CountyCalendarYearCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static Hashtable shtCntyCalndrYr = new Hashtable();

	private final static long serialVersionUID = 8118808159938126344L;
	/**
	 * CntyCalndrYrCache constructor comment.
	 */
	public CountyCalendarYearCache()
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
		return CacheConstant.COUNTY_CALENDAR_YEAR_CACHE;
	}
	/**
	 * Get the CountyCalendarYearData based on OfcIssuanceNo and 
	 * FscalYr.
	 *  
	 * @param aiOfcIssuanceNo int
	 * @param aiFsclYr int
	 * @return CountyCalendarYearData 
	 */
	public static CountyCalendarYearData getCntyCalndrYr(
		int aiOfcIssuanceNo,
		int aiFsclYr)
		throws RTSException
	{
		Object aaObject =
			shtCntyCalndrYr.get(getKey(aiOfcIssuanceNo, aiFsclYr));
		if (aaObject != null)
		{
			return (CountyCalendarYearData) aaObject;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtCntyCalndrYr;
	}
	/**
	 * Get the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiFsclYr int 
	 * @return String  
	 */
	public static String getKey(int aiOfcIssuanceNo, int aiFsclYr)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiFsclYr)});
	}
	/**
	 * Clear and populate the hashtable with the vector
	 * avCntyCalndrYrData
	 * 
	 * @param avCntyCalndrYrDataVector Vector
	 */
	public void setData(Vector avCntyCalndrYrData)
	{
		//reset the hashtable
		try
		{
			shtCntyCalndrYr.clear();
			for (int i = 0; i < avCntyCalndrYrData.size(); i++)
			{
				CountyCalendarYearData laData =
					(CountyCalendarYearData) avCntyCalndrYrData.get(i);
					
				shtCntyCalndrYr.put(
					getKey(
						laData.getOfcIssuanceNo(),
						laData.getFscalYr()),
					laData);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
	}
	/**
	 * Set the hashtable
	 * 
	 * @param ahtCntyCalndrYr  Hashtable
	 */
	public void setHashtable(Hashtable ahtCntyCalndrYr)
	{
		shtCntyCalndrYr = ahtCntyCalndrYr;
	}
}
