package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RSPSWsStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RSPSWsStatus.java
 * 
 * (c) Texas Department of Transportation  2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/15/2004	new class
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/04/2004	add method to get the list of laptops
 *							add qryRSPSWsStatusLaptopList()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/18/2004	Order Query results
 *							modify qryRSPSWsStatus()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/20/2004	Add a way to delete rows
 *							modify delRSPSWsStatus()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/20/2004	Add ScanEngine column handling
 *							modify insRSPSWsStatus(), qryRSPSWsStatus(),
 *							updRSPSWsStatus()
 *							defect 7135 Ver 5.2.1
 * K Harrell	12/23/2004	import statement cleanup.
 *							JavaDoc/Variable Name/Formatting cleanup
 *							Omit qualifier for substaid when update 
 *							AdminCache for RTS_RSPS_WS_STATUS
 *							added call to upAdminCache() to 
 *							delRSPSWsStatus()
 *							modify delRSPSWsStatus(),updAdminCache()
 *							defect 7815 Ver 5.2.2
 * K Harrell	12/29/2004	Make qryRSPSWsStatus more granular so
 *							that can query on Ofcissuanceno, LocIdCd,
 *							LocId
 *							Formatting/JavaDoc/Variable Name cleanup 
 *							modify qryRSPSWsStatus() 
 *							defect 7810 Ver 5.2.2
 * Jeff S.		10/10/2005	Now collecting the log date.  The date that
 * 							the laptop was flashed. 
 * 							modify qryRSPSWsStatus(), updRSPSWsStatus(),
 * 								insRSPSWsStatus()
 *							defect 8381 Ver 5.2.3
 * K Harrell	11/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/02/2008	Removed update to Admin Cache as now 
 * 							called from LocalServerBusiness 
 * 							modify delRSPSWsStatus(), insRSPSWsStatus()
 * 							 updRSPSWsStatus()
 * 							defect 8721 Ver Defect_POS_B 
 * K Harrell	09/05/2008	Return number of rows on delete 
 * 							modify delRSPSWsStatus() 
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	03/19/2010	delete unused method.
 * 							delete qryRSPSWsStatusCount()
 * 							defect 10239 Ver POS_640  
 * ---------------------------------------------------------------------
 */

/**
 * This class handles sql for RTS_RSPS_WS_STATUS.
 * 
 * @version	POS_640 		03/19/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		07/15/2004 16:50:10
 */

public class RSPSWsStatus extends RSPSWsStatusData
{
	protected DatabaseAccess caDA;

	/**
	 * Constructor
	 *
	 * @param  aaDA
	 * @throws RTSException
	 */
	public RSPSWsStatus(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to delete any laptop entries for a location.
	 *
	 * @param  aaData RSPSWsStatusData
	 * @throws RTSException
	 */
	public int delRSPSWsStatus(RSPSWsStatusData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delRSPSWsStatus - Begin");

		try
		{
			StringBuffer lsQry = new StringBuffer();

			Vector lvValues = new Vector();

			lsQry.append(
				"DELETE FROM RTS.RTS_RSPS_WS_STATUS "
					+ "WHERE OfcIssuanceNo = ? "
					+ "AND LocIdCd = ? "
					+ "AND LocId = ?");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aaData.getLocId())));

			Log.write(
				Log.SQL,
				this,
				" - delRSPSWsStatus - SQL - Begin");
			// defect 8595 
			// Return number of rows on delete 
			// Use caDA.executeDBInsertUpdateDelete() vs. 
			// caDA.executeDBQuery(lsQry.toString(), lvValues);
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsQry.toString(),
					lvValues);
			Log.write(Log.SQL, this, " - delRSPSWsStatus - SQL - End");
			Log.write(Log.METHOD, this, "delRSPSWsStatus - End");

			// defect 8721
			// Use common method from LocalOptionsServerBusiness
			//updAdminCache(aaData);
			// end defect 8721
			return liNumRows;
			// end defect 8595 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delRSPSWsStatus - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Insert into RTS.RTS_RSPS_WS_STATUS
	 * 
	 * @param  aaData RSPSWsStatusData	
	 * @throws RTSException 
	 */
	public void insRSPSWsStatus(RSPSWsStatusData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - insRSPSWsStatus - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_RSPS_WS_STATUS ("
				+ "OfcIssuanceNo, "
				+ "LocIdCd, "
				+ "LocId, "
				+ "RSPSId, "
				+ "IpAddr, "
				+ "RSPSVersion, "
				+ "RSPSVersionTimeStmp, "
				+ "RSPSJarSize, "
				+ "RSPSJarSizeTimeStmp, "
				+ "RSPSJarDate, "
				+ "RSPSJarDateTimeStmp, "
				+ "DBVersion, "
				+ "DBVersionTimeStmp, "
				+ "DatLvl, "
				+ "DatLvlTimeStmp, "
				+ "ScanEngine, "
				+ "ScanEngineTimeStmp, "
				+ "LastProcsdHostName, "
				+ "LastProcsdUserName, "
			// defect 8381
		//+ "LastProcsdTimeStmp"
	+"LastProcsdTimeStmp, " + "LastRSPSProcsdTimeStmp"
			// end defect 8381
	+") "
		+ " Values "
		+ "("
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
		+ "?, "
			// defect 8381
	+"?, "
			// end defect 8381
	+"?)";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aaData.getLocId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getIPAddr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSVersion())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getRSPSVersionTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarSize())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarSizeTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarDate().toString())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarDateTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getDbVersion())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getDbVersionTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDATLvl())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getDATLvlTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getScanEngine())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getScanEngineTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLastProcsdHostName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLastProcsdUserName())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getLastProcsdTimeStmp())));
			// defect 8381
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getLastRSPSProcsdTimeStmp())));
			// end defect 8381

			Log.write(Log.SQL, this, "insRSPSWsStatus - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insRSPSWsStatus - SQL - End");

			// defect 8721
			// Use common method from LocalOptionsServerBusiness
			// updAdminCache(aaData);
			// end defect 8721  

			Log.write(Log.METHOD, this, "insRSPSWsStatus - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insRSPSWsStatus - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Query RTS.RTS_RSPS_WS_STATUS
	 * 
	 * @param  aaData RSPSWsStatusData
	 * @return Vector 
	 * @throws RTSException 	
	 */
	public Vector qryRSPSWsStatus(RSPSWsStatusData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryRSPSWsStatus - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry
			.append(
				"SELECT "
				+ "OfcIssuanceNo,"
				+ "LocIdCd,"
				+ "LocId,"
				+ "RSPSId,"
				+ "IpAddr,"
				+ "RSPSVersion,"
				+ "RSPSVersionTimeStmp,"
				+ "RSPSJarSize,"
				+ "RSPSJarSizeTimeStmp,"
				+ "RSPSJarDate,"
				+ "RSPSJarDateTimeStmp,"
				+ "DbVersion,"
				+ "DbVersionTimeStmp,"
				+ "DatLvl,"
				+ "DatLvlTimeStmp,"
				+ "ScanEngine, "
				+ "ScanEngineTimeStmp, "
				+ "LastProcsdTimeStmp,"
		// defect 8381
		+"LastRSPSProcsdTimeStmp,"
		// end defect 8381
		+"LastProcsdHostName, "
			+ "LastProcsdUserName "
			+ "FROM RTS.RTS_RSPS_WS_STATUS "
			+ "WHERE OfcIssuanceNo = ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));
		// defect 7810
		// Retrieve data by LocIdCd, LocId if available
		// add the RSPS individual laptop part if provided
		if (aaData.getLocIdCd() != null
			&& aaData.getLocIdCd().trim().length() > 0)
		{
			lsQry.append("AND LocIdCd = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLocIdCd())));
		}
		if (aaData.getLocId() != 0)
		{
			lsQry.append("AND LocId = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aaData.getLocId())));
		}
		if (aaData.getRSPSId() != null
			&& aaData.getRSPSId().length() > 0)
		{
			lsQry.append("AND RSPSId = ?");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSId())));
		}
		// end defect 7810 
		lsQry.append(" ORDER BY LocIdCd," + " LocId, " + " RSPSId");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRSPSWsStatus - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryRSPSWsStatus - SQL - End");
			while (lrsQry.next())
			{
				RSPSWsStatusData laDATA = new RSPSWsStatusData();
				laDATA.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laDATA.setLocIdCd(
					caDA.getStringFromDB(lrsQry, "LocIdCd"));
				laDATA.setLocId(caDA.getIntFromDB(lrsQry, "LocId"));
				laDATA.setRSPSId(
					caDA.getStringFromDB(lrsQry, "RSPSId"));
				laDATA.setIPAddr(
					caDA.getStringFromDB(lrsQry, "IpAddr"));
				laDATA.setRSPSVersion(
					caDA.getStringFromDB(lrsQry, "RSPSVersion"));
				laDATA.setRSPSVersionTimeStmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"RSPSVersionTimeStmp"));
				laDATA.setRSPSJarSize(
					caDA.getStringFromDB(lrsQry, "RSPSJarSize"));
				laDATA.setRSPSJarSizeTimeStmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"RSPSJarSizeTimeStmp"));
				laDATA.setRSPSJarDate(
					caDA.getRTSDateFromDB(lrsQry, "RSPSJarDate"));
				laDATA.setRSPSJarDateTimeStmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"RSPSJarDateTimeStmp"));
				laDATA.setDbVersion(
					caDA.getStringFromDB(lrsQry, "DbVersion"));
				laDATA.setDbVersionTimeStmp(
					caDA.getRTSDateFromDB(lrsQry, "DbVersionTimeStmp"));
				laDATA.setDATLvl(caDA.getIntFromDB(lrsQry, "DatLvl"));
				laDATA.setDATLvlTimeStmp(
					caDA.getRTSDateFromDB(lrsQry, "DatLvlTimeStmp"));
				laDATA.setScanEngine(
					caDA.getStringFromDB(lrsQry, "ScanEngine"));
				laDATA.setScanEngineTimeStmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"ScanEngineTimeStmp"));
				laDATA.setLastProcsdTimeStmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"LastProcsdTimeStmp"));
				// defect 8381
				laDATA.setLastRSPSProcsdTimeStmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"LastRSPSProcsdTimeStmp"));
				// end defect 8381
				laDATA.setLastProcsdHostName(
					caDA.getStringFromDB(lrsQry, "LastProcsdHostName"));
				laDATA.setLastProcsdUserName(
					caDA.getStringFromDB(lrsQry, "LastProcsdUserName"));
				// Add element to the Vector
				lvRslt.addElement(laDATA);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryRSPSWsStatus - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRSPSWsStatus - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD	

	// defect 10239 
	//	/**
	//	 * Method to count RTS.RTS_RSPS_WS_STATUS records for selected
	//	 * OfcIssuanceNo, Type, and Id.
	//	 *
	//	 * @param  aaData RSPSWsStatusData
	//	 * @return Vector 
	//	 * @throws RTSException 	
	//	 */
	//	public Vector qryRSPSWsStatusCount(RSPSWsStatusData aaData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - qryRSPSWsStatusCount - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		Vector lvValues = new Vector();
	//
	//		Vector lvRslt = new Vector();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ "count(*) COUNT "
	//				+ "FROM RTS.RTS_RSPS_WS_STATUS "
	//				+ "WHERE OfcIssuanceNo = ? "
	//				+ "AND LocIdCd = ? "
	//				+ "AND LocId = ? ");
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaData.getOfcIssuanceNo())));
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(aaData.getLocIdCd())));
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(aaData.getLocId())));
	//
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryRSPSWsStatusCount - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryRSPSWsStatusCount - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				Integer liCount =
	//					new Integer(caDA.getIntFromDB(lrsQry, "COUNT"));
	//				// Add element to the Vector
	//				lvRslt.addElement(liCount);
	//			} //End of While 
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryRSPSWsStatusCount - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryRSPSWsStatusCount - SQL Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD	
	// end defect 10239 

	/**
	 * Method to get the laptop list for selected
	 * OfcIssuanceNo, Type, and Id.
	 * 
	 * @param  aaData RSPSWsStatusData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryRSPSWsStatusLaptopList(RSPSWsStatusData aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryRSPSWsStatusLaptopList - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RSPSID "
				+ "FROM RTS.RTS_RSPS_WS_STATUS "
				+ "WHERE OfcIssuanceNo = ? "
				+ "AND LocIdCd = ? "
				+ "AND LocId = ? "
				+ "ORDER BY 1");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(aaData.getLocIdCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getLocId())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRSPSWsStatusLaptopList - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryRSPSWsStatusLaptopList - SQL - End");

			while (lrsQry.next())
			{
				String lsRSPSId =
					caDA.getStringFromDB(lrsQry, "RSPSID");
				// Add element to the Vector
				lvRslt.addElement(lsRSPSId);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRSPSWsStatusLaptopList - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRSPSWsStatusLaptopList - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD	

	// defect 8721 
	//	/**
	//	 * Method to update RTS.RTS_ADMIN_CACHE for action against 
	//	 * RTS_RSPS_WS_STATUS
	//	 *
	//	 * @param  aaData RSPSWsStatusData
	//	 * @throws RTSException 
	//	 */
	//	public void updAdminCache(RSPSWsStatusData aaData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - updAdminCache - Begin");
	//
	//		Vector lvValues = new Vector();
	//
	//		String lsUpd = "";
	//
	//		lsUpd =
	//			"UPDATE RTS.RTS_ADMIN_CACHE SET "
	//				+ "ChngTimestmp = CURRENT TIMESTAMP "
	//				+ "WHERE CacheTblName = 'RTS_RSPS_WS_STATUS ' AND "
	//				+ "OfcIssuanceNo = ? ";
	//		// defect 7815
	//		// Omit SubstaId Qualifier
	//		//AND "
	//		//+ lsWhereSubstaId;
	//		// end defect 7815 
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaData.getOfcIssuanceNo())));
	//
	//		try
	//		{
	//			Log.write(Log.SQL, this, "updAdminCache - SQL - Begin");
	//			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
	//			Log.write(Log.SQL, this, "updAdminCache - SQL - End");
	//			Log.write(Log.METHOD, this, "updAdminCache - End");
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"updAdminCache - Exception - " + aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	}
	// end defect 8721 

	/**
	 * Method to update RTS.RTS_RSPS_WS_STATUS
	 *
	 * @param  aaData RSPSWsStatusData	
	 * @return int 
	 * @throws RTSException 	
	 */
	public int updRSPSWsStatus(RSPSWsStatusData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updRSPSWsStatus - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_RSPS_WS_STATUS SET "
				+ "IpAddr = ?, "
				+ "RSPSVersion = ?, "
				+ "RSPSVersionTimeStmp = ?, "
				+ "RSPSJarSize = ?, "
				+ "RSPSJarSizeTimeStmp = ?, "
				+ "RSPSJarDate = ?, "
				+ "RSPSJarDateTimeStmp = ?, "
				+ "DBVersion = ?, "
				+ "DBVersionTimeStmp = ?, "
				+ "DatLvl = ?, "
				+ "DatLvlTimeStmp = ?, "
				+ "ScanEngine = ?, "
				+ "ScanEngineTimeStmp = ?, "
				+ "LastProcsdHostName = ?, "
				+ "LastProcsdUserName = ?, "
			// defect 8381
		//+ "LastProcsdTimeStmp = ? "
	+"LastProcsdTimeStmp = ?, " + "LastRSPSProcsdTimeStmp = ? "
			// end defect 8381
	+"WHERE "
		+ "OfcIssuanceNo = ? AND "
		+ "LocIdCd = ? AND "
		+ "LocId = ? AND "
		+ "RSPSID = ?";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getIPAddr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSVersion())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getRSPSVersionTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarSize())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarSizeTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarDate().toString())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getRSPSJarDateTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getDbVersion())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getDbVersionTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDATLvl())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getDATLvlTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getScanEngine())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getScanEngineTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLastProcsdHostName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLastProcsdUserName())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getLastProcsdTimeStmp())));
			// defect 8381
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getLastRSPSProcsdTimeStmp())));
			// end defect 8381
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aaData.getLocId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSId())));

			Log.write(Log.SQL, this, "updRSPSWsStatus - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updRSPSWsStatus - SQL - End");
			Log.write(Log.METHOD, this, "updRSPSWsStatus - End");

			// defect 8721
			// Use common method from LocalOptionsServerBusiness
			// updAdminCache(aaData);
			// end defect 8721  
			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updRSPSWsStatus - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
}
