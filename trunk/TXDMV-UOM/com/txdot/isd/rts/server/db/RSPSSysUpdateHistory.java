package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RSPSSysUpdateHistoryData;
import com.txdot.isd.rts.services.data.RSPSWsStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RSPSSysUpdateHistory.java
 * 
 * (c) Texas Department of Transportation  2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/19/2004	new class
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/20/2004	add method to do delete
 *							add delRSPSWsSysupdtHstry()
 *							defect 7135 Ver 5.2.1
 * Min Wang		11/02/2004	Delete of a sysUpdate if it is in history
 *							but not in XML.
 *							Also allow insert to swich to update if
 *							the row already exists.
 *							modify insRSPSWsSysupdtHstry(),
 *							updRSPSWsSysupdtHstry()
 *							defect 7671 Ver 5.2.1.1
 * K Harrell	11/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	08/11/2009	Modifications for new DB2 Driver 
 * 							modify insRSPSWsupdtHstry()  
 * 							defect 10164 Ver Defect_POS_E'     
 * ---------------------------------------------------------------------
 */

/**
 * This class handles sql for RTS_RSPS_WS_SYSUPDT_HSTRY.
 * 
 * @version	Defect_POS_E'	08/11/2009
 * @author	Ray Rowehl
 * <br>Creation Date:		07/19/2004 08:56:10
 */

public class RSPSSysUpdateHistory extends RSPSSysUpdateHistoryData
{
	protected DatabaseAccess caDA;
	/**
	 * RSPSSysUpdateHistory constructor comment.
	 * 
	 * @throws RTSException
	 */
	public RSPSSysUpdateHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		super();
		caDA = aaDA;
	}
	
	/**
	 * Method to delete any laptop entries for
	 * a location.
	 * 
	 * @param  aaData RSPSSysUpdateHistoryData
	 * @throws RTSException 
	 */
	public void delRSPSSysWsUpdateHstry(RSPSSysUpdateHistoryData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delRSPSSysWsUpdateHstry - Begin");

		try
		{
			StringBuffer lsQry = new StringBuffer();
			Vector lvValues = new Vector();

			lsQry.append(
				"DELETE FROM RTS.RTS_RSPS_WS_SYSUPDT_HSTRY "
					+ "WHERE OfcIssuanceNo = ? "
					+ "AND RSPSId like '"
					+ aaData.getRSPSId()
					+ "'");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));
			Log.write(
				Log.SQL,
				this,
				" - delRSPSSysWsUpdateHstry - SQL - Begin");
			caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - delRSPSSysWsUpdateHstry - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"delRSPSSysWsUpdateHstry - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delRSPSSysWsUpdateHstry - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	
	/**
	 * Method to Insert into RTS.RTS_RSPS_WS_SYSUPDT_HSTRY
	 * 
	 * @param  aaData RSPSSysUpdateHistoryData	
	 * @throws RTSException 	
	 */
	public void insRSPSWsSysupdtHstry(RSPSSysUpdateHistoryData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - insRSPSWsSysupdtHstry - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_RSPS_WS_SYSUPDT_HSTRY ("
				+ "OfcIssuanceNo, "
				+ "RSPSId, "
				+ "Sysupdt, "
				+ "AppliedTimeStmp, "
				+ "DeleteIndi) "
				+ " Values "
				+ "("
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?)";

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
						aaData.getRSPSId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getSysUpdate())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getAppliedTimeStamp())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			Log.write(
				Log.SQL,
				this,
				"insRSPSWsSysupdtHstry - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insRSPSWsSysupdtHstry - SQL - End");

			Log.write(Log.METHOD, this, "insRSPSWsSysupdtHstry - End");
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10164 
			// defect 7671
			// if this is a 803, try to update
			//if (aeRTSEx.getDetailMsg().indexOf("SQL0803") > -1)
			if (aeRTSEx
				.getDetailMsg()
				.indexOf(CommonConstant.DUPLICATE_KEY_EXCEPTION)
				> -1)
			{
				// end defect 10164 

				try
				{
					this.updRSPSWsSysupdtHstry(aaData);
				}
				catch (RTSException aeRTSEx1)
				{
					throw aeRTSEx1;
				}
			}
			else
			{
				// this is the original code of the catch.
				Log.write(
					Log.SQL_EXCP,
					this,
					"insRSPSWsSysupdtHstry - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
			// end defect 7671
		}
	} //END OF INSERT METHOD
	
	/**
	 * Method to Query RTS.RTS_RSPS_WS_SYSUPDT_HSTRY
	 * 
	 * @param  RSPSSysUpdateData aaData	
	 * @return Vector 	
	 * @throws RTSException 
	 */
	public Vector qryRSPSSysWsUpdateHstry(RSPSSysUpdateHistoryData aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryRSPSSysWsUpdateHstry - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		Vector lvRslt = new Vector();
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo, "
				+ "RSPSId, "
				+ "SysUpdt, "
				+ "AppliedTimeStmp "
				+ "FROM RTS.RTS_RSPS_WS_SYSUPDT_HSTRY "
				+ "WHERE OFCISSUANCENO = ? "
				+ "AND RSPSID = ? "
				+ "AND DELETEINDI = 0");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(aaData.getRSPSId())));

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryRSPSSysWsUpdateHstry - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.SQL,
				this,
				" - qryRSPSSysWsUpdateHstry - SQL - End");

			while (lrsQry.next())
			{
				RSPSSysUpdateHistoryData laData =
					new RSPSSysUpdateHistoryData();
				laData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laData.setRSPSId(
					caDA.getStringFromDB(lrsQry, "RSPSId"));
				laData.setSysUpdate(
					caDA.getStringFromDB(lrsQry, "SysUpdt"));
				laData.setAppliedTimeStamp(
					caDA.getRTSDateFromDB(lrsQry, "AppliedTimeStmp"));

				// Add element to the Vector
				lvRslt.addElement(laData);

			} //End of While 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRSPSSysWsUpdateHstry - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRSPSSysWsUpdateHstry - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD	
	
	/**
	 * Method to Query RTS.RTS_RSPS_WS_SYSUPDT_HSTRY where
	 * applied to a selected location.
	 * Returns a vector with FileName and Count
	 * 
	 * @param  aaData RSPSSysUpdateData 
	 * @param  avRSPSIds Vector  	
	 * @return Vector 	
	 * @throws RTSException 
	 */
	public Vector qryRSPSSysWsUpdtsHstryForLoc(
		RSPSWsStatusData aaData,
		Vector avRSPSIds)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryRSPSSysWsUpdtsHstryForLoc - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		Vector lvRslt = new Vector();
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "A.RSPSID as RSPSID, "
				+ "A.SYSUPDT as SYSUPDT, "
				+ "B.SYSUPDTFILENAME as SYSUPDTFILENAME "
				+ "FROM RTS.RTS_RSPS_WS_SYSUPDT_HSTRY A, "
				+ "RTS.RTS_RSPS_SysUpdt B "
				+ "Where A.ofcissuanceno = ? "
				+ "And A.rspsid in ( ");

		if (avRSPSIds.size() > 0)
		{
			// get the first machine name
			lsQry.append("'" + (String) avRSPSIds.elementAt(0) + "'");

			// build the list of all other machine names
			for (int j = 1; j < avRSPSIds.size(); j++)
			{
				lsQry.append(
					", '" + (String) avRSPSIds.elementAt(j) + "'");
			}
		}
		else
		{
			lsQry.append(" '" + "'");
		}

		lsQry.append(
			" ) And A.SYSUPDT = B.SYSUPDT "
				+ "And A.DELETEINDI = 0 "
				+ "Order by B.SYSUPDTFILENAME");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryRSPSSysWsUpdtsHstryForLoc - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.SQL,
				this,
				" - qryRSPSSysWsUpdtsHstryForLoc - SQL - End");

			while (lrsQry.next())
			{
				String lsRSPSId =
					caDA.getStringFromDB(lrsQry, "RSPSID");
				String lsSysUpdt =
					caDA.getStringFromDB(lrsQry, "SYSUPDT");
				String lsSysUpdtFileName =
					caDA.getStringFromDB(lrsQry, "SYSUPDTFILENAME");
				Vector lvRow = new Vector(3);
				lvRow.add(lsRSPSId);
				lvRow.add(lsSysUpdt);
				lvRow.add(lsSysUpdtFileName);

				// Add element to the Vector
				lvRslt.addElement(lvRow);

			} //End of While 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRSPSSysWsUpdtsHstryForLoc - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRSPSSysWsUpdtsHstryForLoc - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
		
	/**
	 * Method to update RTS.RTS_RSPS_WS_SYSUPDT_HSTRY
	 * 
	 * @param  aaData RSPSSysUpdateHistoryData	
	 * @throws RTSException 	
	 */
	public void updRSPSWsSysupdtHstry(RSPSSysUpdateHistoryData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - updRSPSWsSysupdtHstry - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"Update RTS.RTS_RSPS_WS_SYSUPDT_HSTRY Set "
				+ "AppliedTimeStmp = ?, "
				+ "DeleteIndi = ? "
				+ "Where OfcIssuanceNo = ? "
				+ "And RSPSId = ? "
				+ "And SysUpdt = ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getAppliedTimeStamp())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRSPSId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getSysUpdate())));

			Log.write(
				Log.SQL,
				this,
				"updRSPSWsSysupdtHstry - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updRSPSWsSysupdtHstry - SQL - End");

			Log.write(Log.METHOD, this, "updRSPSWsSysupdtHstry - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updRSPSWsSysupdtHstry - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
}
