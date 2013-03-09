package com.txdot.isd.rts.services.util;

import com.txdot.isd.rts.services.cache.CacheManager;

/*
 *
 * PrintCache.java 
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/03/2005	Java 1.4 Cleanup 
 *							Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Method used to Print text version of Cache Files to 
 * d:\rts\rtsappl\cache directory as *.txt 
 *
 * @version	5.2.3 			11/03/2005
 * @author	R Chandel
 * <br>Creation Date:		01/29/2002 16:12:53
 */

public class PrintCache
{
	/**
	 * PrintCache constructor comment.
	 */
	public PrintCache()
	{
		super();
	}
	public static void main(String[] aarrArgs)
	{
		try
		{
			CacheManager.PrintCache();
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
}
