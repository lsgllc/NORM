package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DisabledPlacardDeleteReasonData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * DisabledPlacardDeleteReason.java
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
 * Calls to RTS_DSABLD_PLCRD_DEL_REASN  
 *
 * @version	Defect_POS_B	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class DisabledPlacardDeleteReason
	extends DisabledPlacardDeleteReasonData
{
	DatabaseAccess caDA;

	/**
	 * DisabledPlacardDeleteReason constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DisabledPlacardDeleteReason(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD_DEL_REASN
	 *
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryDisabledPlacardDeleteReason() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryDisabledPlacardDeleteReason - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "DelReasnCd,"
				+ "DelReasnDesc, "
				+ "DelUseIndi, "
				+ "ReplUseIndi "
				+ "FROM RTS.RTS_DSABLD_PLCRD_DEL_REASN");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDisabledPlacardDeleteReason - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryDisabledPlacardDeleteReason - SQL - End");

			while (lrsQry.next())
			{
				DisabledPlacardDeleteReasonData laDPDelReasnData =
					new DisabledPlacardDeleteReasonData();
				laDPDelReasnData.setDelReasnCd(
					caDA.getIntFromDB(lrsQry, "DelReasnCd"));
				laDPDelReasnData.setDelReasnDesc(
					caDA.getStringFromDB(lrsQry, "DelReasnDesc"));
				laDPDelReasnData.setDelUseIndi(
					caDA.getIntFromDB(lrsQry, "DelUseIndi"));
				laDPDelReasnData.setReplUseIndi(
					caDA.getIntFromDB(lrsQry, "ReplUseIndi"));
				// Add element to the Vector
				lvRslt.addElement(laDPDelReasnData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryDisabledPlacardDeleteReason - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDisabledPlacardDeleteReason - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
