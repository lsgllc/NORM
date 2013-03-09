package com.txdot.isd.rts.server.db;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/* 
 * DisabledPlacardMVDIExportHistory.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created
 * 							defect 9831 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_DSABLD_PLCRD 
 *
 * @version	Defect_POS_B	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 **/
public class DisabledPlacardMVDIExportHistory
{
	DatabaseAccess caDA;

	/**
	 * DisabledPlacardMVDIExportHistory constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DisabledPlacardMVDIExportHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY
	 * 
	 * @throws RTSException 	
	 */
	public void insDisabledPlacardMVDIExportHistory()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insDisabledPlacardMVDIExportHistory - Begin");

		String lsIns =
			"INSERT INTO RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY( "
				+ "EXPTIMESTMP )"
				+ " VALUES (CURRENT TIMESTAMP) ";

		try
		{
			Log.write(
				Log.SQL,
				this,
				"insDisabledPlacardMVDIExportHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, new Vector());
			Log.write(
				Log.SQL,
				this,
				"insDisabledPlacardMVDIExportHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insDisabledPlacardMVDIExportHistory - End");
		}

		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insDisabledPlacardMVDIExportHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD 
}
