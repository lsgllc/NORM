package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AdministrationCacheTableData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
/*
 * AdministrationCacheTableCache.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Add comments
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */
/**
 * The Administration Cache Table Cache class is the table of 
 * contents of all the admin cache.  It provides static methods to 
 * retrieve a list of AdministrationCacheTableData
 *
 * <p>AdministrationCacheTableCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * used to determine whether a particular Cache needs to be updated
 * partially or entirely.
 *
 * @version 5.2.3		07/01/2005   
 * @author	Nancy Ting
 * <br>Creation Date: 	08/10/2001 15:04:09 
 */

public class AdministrationCacheTableCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* Administration Cache hashtable
	*/
	private static Hashtable shAdmCacheTbl = new Hashtable();
	private final static long serialVersionUID = 853595514127756585L;

	/**
	 * AdmCacheTblCache default constructor
	 */
	public AdministrationCacheTableCache()
	{
		super();
	}
	/**
	 * Get a vector of Administration Cache Table objects
	 * 
	 * @return Vector 
	 */
	public static Vector getAdmCacheTbl()
	{
		if (shAdmCacheTbl.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturnVector = new Vector();
			for (Enumeration e = shAdmCacheTbl.elements();
				e.hasMoreElements();
				)
			{
				lvReturnVector.addElement(e.nextElement());

			}
			return lvReturnVector;

		}
	}
	/**
	 * Get the Administration Cache Table Data based on office issuance
	 * number, substation id, and admin cache name
	 * 
	 * 
	 * @param aiOfcIssuanceNo 	int
	 * @param aiSubStaId 		int
	 * @param asAdminCacheName  String
	 * @return AdministrationCacheTableData
	 * @throws RTSException 
	 */
	public static AdministrationCacheTableData getAdminCacheTbl(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		String asAdminCacheName)
		throws RTSException
	{
		Object loReturnData =
			shAdmCacheTbl.get(
				getKey(aiOfcIssuanceNo, aiSubStaId, asAdminCacheName));
		if (loReturnData != null)
		{
			return (AdministrationCacheTableData) loReturnData;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Returns the function id of the administration cache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.ADMINISTRATION_CACHE_TABLE_CACHE;
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
		return shAdmCacheTbl;
	}
	/**
	 * Returns the key that is used to in the internal hashtable
	 * 
	 * @param aiOfcIssuanceNo 	int
	 * @param aiSubStaId 		int
	 * @param asAdminCacheName 	String
	 * @return String
	 * @throws RTSException 
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		String asAdminCacheName)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				asAdminCacheName });
	}
	/**
	 * Set the data of the cache to contain the elements of the vector
	 * being passed in.
	 * 
	 * @param  avAdmCacheTblDataVector Vector
	 * @throws RTSException 
	 */
	public void setData(Vector avAdmCacheTblDataVector)
		throws RTSException
	{
		shAdmCacheTbl.clear();
		if (avAdmCacheTblDataVector == null)
		{
			return;
		}
		for (int i = 0; i < avAdmCacheTblDataVector.size(); i++)
		{
			AdministrationCacheTableData lAdministrationCacheTableData =
				(
					AdministrationCacheTableData) avAdmCacheTblDataVector
						.get(
					i);
			shAdmCacheTbl.put(
				getKey(
					lAdministrationCacheTableData.getOfcIssuanceNo(),
					lAdministrationCacheTableData.getSubstaId(),
					lAdministrationCacheTableData.getCacheObjectName()),
				lAdministrationCacheTableData);
		}
	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable HashTable 
	 */
	public void setHashtable(java.util.Hashtable ahtHashtable)
	{
		shAdmCacheTbl = ahtHashtable;
	}
}
