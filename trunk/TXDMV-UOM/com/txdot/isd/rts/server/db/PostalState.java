package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PostalStateData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * PostalState.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2010	Created
 * 							defect 10396 Ver POS_640 	
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PLT_SYM
 *
 * @version	POS_640			03/24/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		03/24/2010 12:31:00
 */
public class PostalState
{
	private DatabaseAccess caDA;

	private String csMethod = new String();

	/**
	 * PostalState constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public PostalState(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_POSTAL_STATE
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryPostalState() throws RTSException
	{
		csMethod = "qryPostalState";

		Log.write(Log.METHOD, this, csMethod = " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "StateAbrvn,"
				+ "StateName,"
				+ "StateTypeCd "
				+ "FROM "
				+ "RTS.RTS_POSTAL_STATE");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				PostalStateData laPostalStateData =
					new PostalStateData();
				laPostalStateData.setStateAbrvn(
					caDA.getStringFromDB(lrsQry, "StateAbrvn"));
				laPostalStateData.setStateName(
					caDA.getStringFromDB(lrsQry, "StateName"));
				laPostalStateData.setStateTypeCd(
					caDA.getStringFromDB(lrsQry, "StateTypeCd"));
				lvRslt.addElement(laPostalStateData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
}
