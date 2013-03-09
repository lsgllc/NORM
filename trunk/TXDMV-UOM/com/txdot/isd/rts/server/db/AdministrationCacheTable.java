package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AdministrationCacheTableData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * AdministrationCacheTable.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/12/2001	Removed Insert,Update,Delete
 * 							Added arguments for Select, order clause
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	04/01/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	09/04/2008	Modify so that update of RTS_ADMIN_CACHE is 
 * 							not duplicated for each table.  Also, update
 * 							Chngtimestmp for RTS_ADMIN_CACHE when updated.
 * 							add qrySubstaId(), updAdminCache()
 * 							defect 8721 Ver Defect_POS_B                    
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ADMIN_CACHE
 *
 * @version	Defect_POS_B	09/04/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		09/06/2001 18:12:28  
 */

public class AdministrationCacheTable
	extends AdministrationCacheTableData
{
	DatabaseAccess caDA;

	/**
	 * AdministrationCacheTable constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public AdministrationCacheTable(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to query RTS.RTS_ADMIN_CACHE
	 *
	 * @param  aaAdministrationCacheTable AdministrationCacheTableData 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryAdministrationCacheTable(AdministrationCacheTableData aaAdministrationCacheTableData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryAdministrationCacheTable - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "CacheObjectName,"
				+ "CacheTblName,"
				+ "CacheFileName,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_ADMIN_CACHE where OfcIssuanceNo = ? "
				+ "and SubstaId = ? order by 1,2,3 ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdministrationCacheTableData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdministrationCacheTableData.getSubstaId())));
			Log.write(
				Log.SQL,
				this,
				" - qryAdministrationCacheTable - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryAdministrationCacheTable - SQL - End");

			while (lrsQry.next())
			{
				AdministrationCacheTableData laAdministrationCacheTableData =
					new AdministrationCacheTableData();
				laAdministrationCacheTableData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laAdministrationCacheTableData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laAdministrationCacheTableData.setCacheObjectName(
					caDA.getStringFromDB(lrsQry, "CacheObjectName"));
				laAdministrationCacheTableData.setCacheTblName(
					caDA.getStringFromDB(lrsQry, "CacheTblName"));
				laAdministrationCacheTableData.setCacheFileName(
					caDA.getStringFromDB(lrsQry, "CacheFileName"));
				laAdministrationCacheTableData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laAdministrationCacheTableData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryAdministrationCacheTable - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryAdministrationCacheTable - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_ADMIN_CACHE  
	 * 
	 * @param aiOfcIssuanceNo
	 * @param aiSubstaId
	 * @param asCacheTblName   
	 * @throws RTSException 
	 */
	public void updAdminCache(
		int aiOfcIssuanceNo,
		int aiSubstaId,
		String asCacheTblName)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - updAdminCache - Begin");

		Vector lvValues = new Vector();
		String lsWhereSubstaId = "";
		String lsUpd = "";

		int liSubstaId =
			qrySubstaId(aiOfcIssuanceNo, aiSubstaId, asCacheTblName);

		if (liSubstaId == 0)
		{
			lsWhereSubstaId =
				" ( SubstaId = 0 OR SubstaId in (Select SubstaId from "
					+ " RTS.RTS_SUBSTA_SUBSCR where OfcIssuanceNo = ? "
					+ " and TblSubstaId = 0 and TblName = ?  )) ";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOfcIssuanceNo)));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(asCacheTblName)));
		}
		else
		{
			lsWhereSubstaId = " SubstaId =" + aiSubstaId;
		}

		lsUpd =
			"UPDATE RTS.RTS_ADMIN_CACHE SET "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ lsWhereSubstaId
				+ " AND "
				+ "OfcIssuanceNo = ? AND "
				+ "CacheTblName in ( ? , 'RTS_ADMIN_CACHE')";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiOfcIssuanceNo)));

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(asCacheTblName)));
		try
		{
			Log.write(Log.SQL, this, "updAdminCache - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updAdminCache - SQL - End");
			Log.write(Log.METHOD, this, "updAdminCache - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updAdminCache - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	* Method to Determine "Owning" SubstaId  
	* 
	* @param aiOfcIssuanceNo
	* @param aiSubstaId
	* @param asCacheTblName
	* @throws RTSException	
	*/
	public int qrySubstaId(
		int aiOfcIssuanceNo,
		int aiSubstaId,
		String asCacheTblName)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySubstaId - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		int liSubstaId = Integer.MIN_VALUE;

		lsQry.append(
			"SELECT TblSubstaId  "
				+ " from RTS.RTS_SUBSTA_SUBSCR "
				+ "where "
				+ " TblName = ?  and "
				+ " OfcIssuanceNo = ? and  "
				+ " SubstaId = ? ");

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(asCacheTblName)));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiOfcIssuanceNo)));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiSubstaId)));

		try
		{
			Log.write(Log.SQL, this, " - qrySubstaId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qrySubstaId - SQL - End");

			while (lrsQry.next())
			{
				liSubstaId = caDA.getIntFromDB(lrsQry, "TblSubstaId");
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qrySubstaId - End ");
			if (liSubstaId == Integer.MIN_VALUE)
			{
				liSubstaId = aiSubstaId;
			}
			return (liSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstaId - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}
