package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * SecurityCache.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Added comments
 * Ray Rowehl	10/02/2003	Add support to handle windows cache login
 *							modify setData()
 *							code formatting
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/01/2003	Rename AdUserId to SysUserId
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/18/2003	Rename SysUserId to UserName
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards.
 *							defect 6445 Ver 5.1.6
 * K Harrell	02/19/2004	Corrected a few preexisting typos.
 *							Ver 5.1.6
 * K Harrell	03/28/2004	Remove reference to isWindowsPlatform()
 *							defect 6955
 *							Ver 5.2.0
 * K Harrell	06/19/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * The SecurityCache class provides static methods to 
 * retrieve a particular SecurityData based 
 * on different input parameters.
 *
 * <p>SecurityCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version 5.2.0  			06/19/2004
 * @author Nancy Ting
 * <br>Creation date: 		08/16/2001 15:56:57
 */

public class SecurityCache
	extends AdminCache
	implements java.io.Serializable
{
	private static Hashtable shtSecurity = new Hashtable();
	private final static long serialVersionUID = 2286537120535404964L;
	/**
	 * SecurityCache default constructor.
	 */
	public SecurityCache()
	{
		super();
	}
	/**
	 * Get the cache function id
	 * @return the cachefunction id
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.SECURITY_CACHE;
	}
	/**
	 * Get the internally stored hashtable
	 * @return the internally stored hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtSecurity;
	}
/**
 * Get the key used in the hashtable
 * @return the key
 * @param aiOfcIssuanceNo the office issuance number
 * @param aiSubStaId the substation id
 * @param asUserName
 */
public static String getKey(
	int aiOfcIssuanceNo,
	int aiSubStaId,
	String asUserName)
	throws RTSException
{
	// defect 6955
	// replace strEmpId with asUserName
	return UtilityMethods.constructPrimaryKey(
		new String[] {
			String.valueOf(aiOfcIssuanceNo),
			String.valueOf(aiSubStaId),
			asUserName });
	// end defect 6955 
}
/**
 * Get the SecurityData based on office issuance number,
 * substation id and employee id
 * 
 * @return com.txdot.isd.rts.services.cache.SecurityData
 * @param aiOfcIssuanceNo office issuance number
 * @param aiSubStaId substation id
 * @param asUserName
 */
public static SecurityData getSecurity(
	int aiOfcIssuanceNo,
	int aiSubStaId,
	String asUserName)
	throws RTSException
{
	// defect 6955
	// replace asEmpid w/ asUserName
	String lsPrimaryKey = getKey(aiOfcIssuanceNo, aiSubStaId, asUserName);
	// end defect 6955 
	Object loReturnData = shtSecurity.get(lsPrimaryKey);
	if (loReturnData != null)
	{
		return (SecurityData) loReturnData;
	}
	else
	{
		return null;
	}
}
/**
 * Clear and populate the hashtable with the vector 
 * @param avSecurityData vector of SecurityData
 * to populate the hashtable
 */
public void setData(Vector avSecurityData) throws RTSException
{
	//reset data
	shtSecurity.clear();
	for (int i = 0; i < avSecurityData.size(); i++)
		{
		SecurityData lSecurityData = (SecurityData) avSecurityData.get(i);
		// defect 6955 
		// defect 6445
		// set the key to match the os
		String lsPrimaryKey = "";
		//if (UtilityMethods.isWindowsPlatform())
		//{
		// on windows, use UserName as part of the key
		lsPrimaryKey =
			getKey(
				lSecurityData.getOfcIssuanceNo(),
				lSecurityData.getSubstaId(),
				lSecurityData.getUserName());
		//}
		//else
		//{
		// on os/2, use empid as part of the key.
		//lsPrimaryKey =
		//	getKey(
		//		lSecurityData.getOfcIssuanceNo(),
		//		lSecurityData.getSubstaId(),
		//		lSecurityData.getEmpId());
		//}
		// end 6445
		// end 6955
		shtSecurity.put(lsPrimaryKey, lSecurityData);
	}
}
	/**
	 * Set the hashtable
	 * @param ahSecurity the new hashtable
	 */
	public void setHashtable(Hashtable ahSecurity)
	{
		shtSecurity = ahSecurity;
	}
}
