package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * StaticCacheTableData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 			 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * StaticCacheTableData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	09/05/2001  
 */

public class StaticCacheTableData implements Serializable
{
	// Object 
	protected RTSDate caChngTimestmp;

	// String
	protected String csCacheFileName;
	protected String csCacheObjectName;
	protected String csCacheTblName;

	private final static long serialVersionUID = 6827849670042741057L;
	/**
	 * Returns the value of CacheFileName
	 * 
	 * @return  String 
	 */
	public final String getCacheFileName()
	{
		return csCacheFileName;
	}
	/**
	 * Returns the value of CacheObjectName
	 * 
	 * @return  String 
	 */
	public final String getCacheObjectName()
	{
		return csCacheObjectName;
	}
	/**
	 * Returns the value of CacheTblName
	 * 
	 * @return  String 
	 */
	public final String getCacheTblName()
	{
		return csCacheTblName;
	}
	/**
	 * Returns the value of ChngTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * This method sets the value of CacheFileName
	 * 
	 * @param aCacheFileName   String 
	 */
	public final void setCacheFileName(String asCacheFileName)
	{
		csCacheFileName = asCacheFileName;
	}
	/**
	 * This method sets the value of CacheObjectName
	 * 
	 * @param asCacheObjectName   String 
	 */
	public final void setCacheObjectName(String asCacheObjectName)
	{
		csCacheObjectName = asCacheObjectName;
	}
	/**
	 * This method sets the value of CacheTblName
	 * 
	 * @param asCacheTblName   String 
	 */
	public final void setCacheTblName(String asCacheTblName)
	{
		csCacheTblName = asCacheTblName;
	}
	/**
	 * This method sets the value of ChngTimestmp
	 * 
	 * @param aaChngTimestmp  RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
}
