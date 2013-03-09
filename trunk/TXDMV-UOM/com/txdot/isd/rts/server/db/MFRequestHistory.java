package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * MFRequestHistory.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/31/2010	Created
 * 							defect 10462 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_MF_REQ_HSTRY 
 *
 * @version	6.5.0			07/31/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		07/31/2010 16:30:17
 */
public class MFRequestHistory
{
	DatabaseAccess caDA;
	String csMethod;

	/**
	 * MFRequestHistory.java Constructor
	 * 
	 */
	public MFRequestHistory(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Purge from RTS.RTS_MF_REQ_HSTRY
	 *
	 * @param  int aiPurgeDays
	 * @return int 	
	 * @throws RTSException 
	 */
	public int purgeMFReqHstry(int aiPurgeDays) throws RTSException
	{
		csMethod = "purgeMFReqHstry";
		Log.write(Log.METHOD, this, csMethod + " - Begin");
		Vector lvValues = new Vector();

		String lsStmt =
			"DELETE FROM RTS.RTS_MF_REQ_HSTRY WHERE REQDATE < "
				+ " CURRENT DATE - ? DAYS";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeDays)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsStmt, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

}
