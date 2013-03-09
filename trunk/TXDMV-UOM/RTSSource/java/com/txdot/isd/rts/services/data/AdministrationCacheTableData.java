package com.txdot.isd.rts.services.data;import java.io.Serializable;import com.txdot.isd.rts.services.util.*;/* * AdministrationCacheTableData.java *  * (c) Texas Department of Transportation  2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	06/19/2005	Java 1.4 Work * 							moved to services.data * 							delete cRSubscriptionTimestmp, associated * 							get/set methods  * 							defect 7899 Ver 5.2.3   *---------------------------------------------------------------------- *//** * This Data class contains attributes and get and set methods for  * AdministrationCacheTableData. Primary key is the dataObjectName,  * officeIssuanceId and SubstationId *  * @version	5.2.3		06/19/2005 * @author	Administrator * <br>Creation Date:	08/30/2001  15:05:28 *---------------------------------------------------------------*//* &AdministrationCacheTableData& */public class AdministrationCacheTableData implements Serializable{	// int /* &AdministrationCacheTableData'ciOfcIssuanceNo& */	protected int ciOfcIssuanceNo;/* &AdministrationCacheTableData'ciSubstaId& */	protected int ciSubstaId;	// Object /* &AdministrationCacheTableData'caChngTimestmp& */	protected RTSDate caChngTimestmp;	// String/* &AdministrationCacheTableData'csCacheFileName& */	protected String csCacheFileName;/* &AdministrationCacheTableData'csCacheObjectName& */	protected String csCacheObjectName;/* &AdministrationCacheTableData'csCacheTblName& */	protected String csCacheTblName;/* &AdministrationCacheTableData'serialVersionUID& */	private final static long serialVersionUID = -649870268181388904L;	/**	 * AdministrationCacheTableData constructor comment.	 *//* &AdministrationCacheTableData.AdministrationCacheTableData& */	public AdministrationCacheTableData()	{		super();	}	/**	 * Return value of CacheFileName 	 *  	 * @return  String 	 *//* &AdministrationCacheTableData.getCacheFileName& */	public final String getCacheFileName()	{		return csCacheFileName;	}	/**	 * Return value of CacheObjectName	 *  	 * @return  String 	 *//* &AdministrationCacheTableData.getCacheObjectName& */	public final String getCacheObjectName()	{		return csCacheObjectName;	}	/**	 * Return value of CacheTblName	 *  	 * @return  String 	 *//* &AdministrationCacheTableData.getCacheTblName& */	public final String getCacheTblName()	{		return csCacheTblName;	}	/**	 * Return value of ChngTimestmp	 *  	 * @return  RTSDate 	 *//* &AdministrationCacheTableData.getChngTimestmp& */	public final RTSDate getChngTimestmp()	{		return caChngTimestmp;	}	/**	 * Return value of OfcIssuanceNo	 *  	 * @return  int 	 *//* &AdministrationCacheTableData.getOfcIssuanceNo& */	public final int getOfcIssuanceNo()	{		return ciOfcIssuanceNo;	}	/**	 * Return value of SubstaId	 *  	 * @return  int 	 *//* &AdministrationCacheTableData.getSubstaId& */	public final int getSubstaId()	{		return ciSubstaId;	}	/**	 * Set value of CacheFileName	 *  	 * @param asCacheFileName   String 	 *//* &AdministrationCacheTableData.setCacheFileName& */	public final void setCacheFileName(String asCacheFileName)	{		csCacheFileName = asCacheFileName;	}	/**	 * Set value of CacheObjectName	 *  	 * @param asCacheObjectName   String 	 *//* &AdministrationCacheTableData.setCacheObjectName& */	public final void setCacheObjectName(String asCacheObjectName)	{		csCacheObjectName = asCacheObjectName;	}	/**	 * Set value of ChngTimestmp	 *  	 * @param asCacheTblName   String 	 *//* &AdministrationCacheTableData.setCacheTblName& */	public final void setCacheTblName(String asCacheTblName)	{		csCacheTblName = asCacheTblName;	}	/**	 * Set value of aChngTimestmp	 * 	 * @param aaChngTimestmp   RTSDate 	 *//* &AdministrationCacheTableData.setChngTimestmp& */	public final void setChngTimestmp(RTSDate aaChngTimestmp)	{		caChngTimestmp = aaChngTimestmp;	}	/**	 * Set value of OfcIssuanceNo	 *	 * @param aiOfcIssuanceNo   int 	 *//* &AdministrationCacheTableData.setOfcIssuanceNo& */	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)	{		ciOfcIssuanceNo = aiOfcIssuanceNo;	}	/**	 * Set value of SubstaId	 * 	 * @param aiSubstaId   int 	 *//* &AdministrationCacheTableData.setSubstaId& */	public final void setSubstaId(int aiSubstaId)	{		ciSubstaId = aiSubstaId;	}}/* #AdministrationCacheTableData# */