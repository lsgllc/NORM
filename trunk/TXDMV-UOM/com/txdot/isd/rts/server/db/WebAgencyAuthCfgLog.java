package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyAuthCfgLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyAuthCfgLog.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell 	01/05/2011 	Renamings per standards 
 *        					defect 10708 Ver 6.7.0
 * K Harrell	04/26/2011	remove references to PilotIndi 
 * 							defect 10708 Ver 6.7.0 	   
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNCY_AUTH_CFG_LOG 
 *
 * @version	6.7.1			04/26/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyAuthCfgLog
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyAuthCfg.java Constructor
	 * 
	 */
	public WebAgencyAuthCfgLog(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY_AUTH_CFG_Log  
	 *
	 * @param  aaData	WebAgencyAuthCfgLogData	
	 * @throws RTSException 
	 */
	public void insWebAgencyAuthCfgLog(WebAgencyAuthCfgLogData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyAuthCfgLog";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_AUTH_CFG_LOG("
				+ "AGNCYAUTHIDNTYNO,"
				+ "KEYENTRYCD,"
				+ "ISSUEINVINDI,"
				+ "EXPPROCSNGCD,"
				+ "EXPPROCSNGMOS,"
				+ "MAXSUBMITDAYS,"
				+ "MAXSUBMITCOUNT,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP, "
				+ "UPDTNGACTN, "
				+ "UPDTNGAGNTIDNTYNO, "
				+ "UPDTNGIPADDR) "
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthIdntyNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getKeyEntryCd())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getIssueInvIndi())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getExpProcsngCd())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getExpProcsngMos())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMaxSubmitDays())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMaxSubmitCount())));

			// 8						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			// 9 						
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getChngTimestmp())));
			// 10 						
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getUpdtngActn())));
			// 11 						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));
			// 12 						
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getUpdtngIPAddr())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	/**
	 * Purge RTS_WEB_AGNCY_AUTH_CFG_LOG 
	 * 
	 * @param aiPurgeDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeWebAgencyAuthCfgLog(int aiPurgeDays)
		throws RTSException
	{
		csMethod = "purgeWebAgencyAuthCfgLog";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelData =
			"DELETE FROM RTS.RTS_WEB_AGNCY_AUTH_CFG_LOG "
				+ " WHERE DELETEINDI = 1 AND "
				+ " CHNGTIMESTMP "
				+ " < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();

		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeDays)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

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
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}
}
