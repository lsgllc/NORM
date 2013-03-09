package com.txdot.isd.rts.services.cache;import java.io.Serializable;import java.security.acl.LastOwnerException;import java.util.Hashtable;import java.util.Vector;import com	.txdot	.isd	.rts	.services	.data	.WebServicesServiceActionVersionListData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.constants.CacheConstant;/* * WebServicesServiceActionVersionCache.java * * (c) Texas Department of Transportation 2008 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	07/07/2008	Created class. * 							defect 9675 Ver MyPlates_POS * --------------------------------------------------------------------- *//** * Cache handler for RTS_SRVC_ACTN_VERSION list data. * * @version	MyPlates_POS	07/07/2008 * @author	Ray Rowehl * <br>Creation Date:		07/07/2008 15:07:56 *//* &WebServicesServiceActionVersionCache& */public class WebServicesServiceActionVersionCache	extends GeneralCache	implements Serializable{/* &WebServicesServiceActionVersionCache'shtWsSavList& */	private static Hashtable shtWsSavList = new Hashtable();	/**	 * Create the lookup key for the cache.	 * 	 * @param asSrvcName	 * @param aiActnId	 * @param aiVersion	 * @return String	 *//* &WebServicesServiceActionVersionCache.createKey& */	private String createKey(		String asSrvcName,		int aiActnId,		int aiVersion)	{		return asSrvcName + aiActnId + aiVersion;	}	/** 	 * Return the cache function number.	 * 	 * @see com.txdot.isd.rts.services.cache.GeneralCache#getCacheFunctionId()	 *//* &WebServicesServiceActionVersionCache.getCacheFunctionId& */	public int getCacheFunctionId()	{		return CacheConstant.WS_SAV_LIST;	}	/**	 * Return the hash table.	 * 	 * @see com.txdot.isd.rts.services.cache.GeneralCache#getHashtable()	 *//* &WebServicesServiceActionVersionCache.getHashtable& */	public Hashtable getHashtable()	{		return shtWsSavList;	}	/**	 * Get the Service-Action-Version id.	 * 	 * @param asServiceName	 * @param aiActionId	 * @param aiVersion	 * @return int	 *//* &WebServicesServiceActionVersionCache.getSavId& */	public int getSavId(		String asServiceName,		int aiActionId,		int aiVersion)	{		int liSavId = -1;		String lsKeyValue =			createKey(asServiceName, aiActionId, aiVersion);		WebServicesServiceActionVersionListData laObject =			(WebServicesServiceActionVersionListData) shtWsSavList.get(				lsKeyValue);		if (laObject != null)		{			liSavId = laObject.getSavId();		}		return liSavId;	}	/**	 * Set up the cache hash table.	 * 	 * @see com.txdot.isd.rts.services.cache.GeneralCache#setData(java.util.Vector)	 *//* &WebServicesServiceActionVersionCache.setData& */	public void setData(Vector avDataVector) throws RTSException	{		shtWsSavList.clear();		for (int i = 0; i < avDataVector.size(); i++)		{			WebServicesServiceActionVersionListData laData =				(					WebServicesServiceActionVersionListData) avDataVector						.get(					i);			String lsKeyValue =				createKey(					laData.getSrvcName(),					laData.getActnId(),					laData.getVersion());			shtWsSavList.put(new String(lsKeyValue), laData);		}	}	/**	 * @see com.txdot.isd.rts.services.cache.GeneralCache#setHashtable(java.util.Hashtable)	 *//* &WebServicesServiceActionVersionCache.setHashtable& */	public void setHashtable(Hashtable ahtHashtable)	{		shtWsSavList = ahtHashtable;	}}/* #WebServicesServiceActionVersionCache# */