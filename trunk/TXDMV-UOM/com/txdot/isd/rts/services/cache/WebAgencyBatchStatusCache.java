package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyBatchStatusData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * WebAgencyBatchStatusCache.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/29/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0    
 * ---------------------------------------------------------------------
 */

/**
 * Cache for RTS_WEB_AGNCY_BATCH_STATUS 
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/29/2010 9:47:17 
 */
public class WebAgencyBatchStatusCache
	extends GeneralCache
	implements java.io.Serializable
{

	static final long serialVersionUID = -3568388905554279631L;

	private static Hashtable shtBatchAgncyBatchStatusCache =
		new Hashtable();

	/**
	 * WebAgencyBatchStatusCache constructor comment.
	 */
	public WebAgencyBatchStatusCache()
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
		return CacheConstant.WEB_AGENCY_BATCH_STATUS_CACHE;
	}

	/**
	 * Get all WebAgencyBatchStatusData
	 *  
	 * @return Vector
	 */

	public static Vector getWebAgencyBatchStatus()
	{
		Vector lvReturn = new Vector();
		Enumeration e = shtBatchAgncyBatchStatusCache.elements();
		while (e.hasMoreElements())
		{
			WebAgencyBatchStatusData laData =
				(WebAgencyBatchStatusData) e.nextElement();
			lvReturn.addElement(laData);
		}

		if (lvReturn.size() == 0)
		{
			return null;
		}
		else
		{
			return lvReturn;
		}
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
		return shtBatchAgncyBatchStatusCache;
	}
	/**
	 * Clear and populate the hashtable with the vector avWebAgencyBatchStatusData
	 * 
	 * @param avWebAgencyBatchStatusData Vector
	 */
	public void setData(Vector avWebAgencyBatchStatusData)
	{
		//reset the hashtable
		shtBatchAgncyBatchStatusCache.clear();
		for (int i = 0; i < avWebAgencyBatchStatusData.size(); i++)
		{
			WebAgencyBatchStatusData laData =
				(
					WebAgencyBatchStatusData) avWebAgencyBatchStatusData
						.get(
					i);
			shtBatchAgncyBatchStatusCache.put(
				laData.getBatchStatusCd(),
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
		shtBatchAgncyBatchStatusCache = ahtHashtable;
	}
}
