package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.StaticCacheTableData;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * StaticCache.java 
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	08/30/2001	Created
 * R Hicks		07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	09/09/2003	Alter to make call to RTS_STATIC_CACHE_PLUS
 * 							added qryStaticCachePlus
 * 							altered qryStaticCache
 * 							defect 6548. Version 5.1.5 Fix 0  
 * K Harrell	12/31/2003	Alter to select based upon CacheVersionNo
 *							modified qryStaticCache()
 * 							deprecate qryStaticCachePlus()
 *							defect 6555. Version 5.1.5 Fix 2
 * K Harrell	01/18/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							deleted deprecated qrySTaticCachePlus() 
 *							Ver 5.2.3
 * K Harrell	03/04/2005	Java 1.4 Work
 *							deleted deprecated qrySTaticCachePlus()
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryStaticCache() 
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005  services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/**
 * Static Cache class allows the user to access from RTS_STATIC_CACHE.
 *  
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br> Creation Date:	08/31/2001 15:13:30  
 */

public class StaticCache extends StaticCacheTableData
{
	protected DatabaseAccess caDA;
	/**
	 * StaticCache constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public StaticCache(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_STATIC_CACHE
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryStaticCache()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryStaticCache - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 6555
		// Determine CacheVersionNo from SystemProperty.
		// Remove call to StaticCachePlus
		int liCacheVersionNo = SystemProperty.getCacheVersionNo();

		// defect 6548 
		// Select contents of StaticCachePlus
		// lvRslt = qryStaticCachePlus();
		// end defect 6548

		// end defect 6555

		lsQry.append(
			"SELECT "
				+ "CacheObjectName,"
				+ "CacheTblName,"
				+ "CacheFileName,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_STATIC_CACHE Where CacheVersionNo <= "
				+ liCacheVersionNo
				+ " Order by CacheTblName");

		try
		{
			Log.write(Log.SQL, this, " - qryStaticCache - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryStaticCache - SQL - End");

			while (lrsQry.next())
			{
				StaticCacheTableData laStaticCacheData =
					new StaticCacheTableData();
				laStaticCacheData.setCacheObjectName(
					caDA.getStringFromDB(lrsQry, "CacheObjectName"));
				laStaticCacheData.setCacheTblName(
					caDA.getStringFromDB(lrsQry, "CacheTblName"));
				laStaticCacheData.setCacheFileName(
					caDA.getStringFromDB(lrsQry, "CacheFileName"));
				laStaticCacheData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laStaticCacheData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryStaticCache - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryStaticCache - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD	
}
