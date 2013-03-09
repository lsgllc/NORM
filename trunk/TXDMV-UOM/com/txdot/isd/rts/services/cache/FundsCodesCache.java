package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * FundsCodesCache.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/27/2008	Created
 * 							defect 6949 Ver Defect POS A
 * ---------------------------------------------------------------------
 */

/**
 * The FundsCodesCache class provides static methods to 
 * retrieve a FundsCodesData based on different input parameters.
 *
 * <p>FundsCodesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	Defect POS A	03/27/2008 
 * @author	K Harrell
 * <br>Creation Date:		03/27/2008 11:52:00
 */

public class FundsCodesCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static Hashtable shtFundsCodesCache = new Hashtable();

	private static final long serialVersionUID = 2598456556203518178L;
	
	/**
	 * FundsCodesCache default constructor
	 */
	public FundsCodesCache()
	{
		super();
	}

	/**
	 * Get the cache function id
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.FUNDS_CODES_CACHE;
	}

	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtFundsCodesCache;
	}

	/**
	 * Get the key used in the hashtable
	 * 
	 * @param asFundsCat
	 * @param asFundsRcvngEnt
	 * @return String  
	 * @throws RTSException 
	 */
	public static String getKey(
		String asFundsCat,
		String asFundsRcvngEnt)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] { asFundsCat, asFundsRcvngEnt });
	}

	/**
	 * Get the FundsCodes data
	 * 
	 * @param asFundsCat,
	 * @param asFundsRcvngEnt
	 * @return FundsCodesData 
	 * @throws RTSException 
	 */
	public static FundsCodesData getFundsCodes(
		String asFundsCat,
		String asFundsRcvngEnt)
		throws RTSException
	{

		String lsPrimaryKey = getKey(asFundsCat, asFundsRcvngEnt);

		Object laReturnData = shtFundsCodesCache.get(lsPrimaryKey);

		if (laReturnData != null)
		{
			return (FundsCodesData) laReturnData;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * 
	 * @param avFundsCodesData Vector
	 * @throws RTSException 
	 */
	public void setData(Vector avFundsCodesData) throws RTSException
	{
		//reset data
		shtFundsCodesCache.clear();

		for (int i = 0; i < avFundsCodesData.size(); i++)
		{
			FundsCodesData laFundsCodesData =
				(FundsCodesData) avFundsCodesData.get(i);

			String lsPrimaryKey =
				getKey(
					laFundsCodesData.getFundsCat(),
					laFundsCodesData.getFundsRcvngEnt());
			shtFundsCodesCache.put(lsPrimaryKey, laFundsCodesData);
		}
	}

	/**
	 * Set the hashtable
	 * 
	 * @param ahtFundsCodesCache Hashtable 
	 */
	public void setHashtable(Hashtable ahtFundsCodesCache)
	{
		shtFundsCodesCache = ahtFundsCodesCache;
	}
}
