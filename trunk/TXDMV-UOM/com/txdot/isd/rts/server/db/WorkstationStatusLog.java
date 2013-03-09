package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WorkstationStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WorkstationStatusLog.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/03/2010	Created 
 * 							defect 8087 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class handles insert/purge of RTS_WS_STATUS_LOG records.
 *
 * @version	POS_640 		04/03/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:		04/03/2010 10:57:17
*/
public class WorkstationStatusLog
{
	private DatabaseAccess caDA;

	private String csMethod = new String();

	/**
	 * WorkstationStatusLog.java Constructor
	 * 
	 */
	public WorkstationStatusLog()
	{
		super();
	}

	/**
	 * WorkstationStatusLog constructor passing DatabaseAccess.
	 *
	 * @param aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public WorkstationStatusLog(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/** 
	 * Insert into RTS_WS_STATUS_LOG 
	 * 
	 * @param aaDBWksStatusData
	 * @throws RTSException
	 */
	public void insWorkstationStatusLog(WorkstationStatusData aaDBWksStatusData)
		throws RTSException
	{
		csMethod = " - insWorkstationStatusLog";

		Vector lvValues = new Vector();

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		try
		{
			String lsIns =
				"Insert into RTS.RTS_WS_STATUS_LOG(OfcissuanceNo,"
					+ " SubstaId, WsId, RTSVersion, RTSVersionDate,"
					+ " Chngtimestmp,CPName, CdSrvrCPName,CdSrvrIndi,"
					+ " LastRestartTstmp,Jarsize,ServletHost,"
					+ " ServletPort,LogInsrtTimestmp) "
					+ " (Select OfcissuanceNo, SubstaId, WsId, "
					+ " RTSVersion, RTSVersionDate,Chngtimestmp,"
					+ " CPName, CdSrvrCPName,CdSrvrIndi,"
					+ " LastRestartTstmp,Jarsize,ServletHost,"
					+ " ServletPort,Current Timestamp "
					+ " FROM "
					+ " RTS.RTS_WS_STATUS "
					+ " WHERE "
					+ " OfcissuanceNo = ? AND"
					+ " Substaid = ? AND "
					+ " Wsid = ?)";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDBWksStatusData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDBWksStatusData.getSubStaId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDBWksStatusData.getWSid())));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF INSERT LOG METHOD

	/**
	 * Method to Delete from RTS.RTS_WS_STATUS_LOG for Purge
	 *
	 * @param  aiNumDays int 
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeWorkstationStatusLog(int aiNumDays)
		throws RTSException
	{
		csMethod = " - purgeWorkstationStatusLog";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_WS_STATUS_LOG WHERE  "
				+ " days(Current Date) - days(LogInsrtTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
}
