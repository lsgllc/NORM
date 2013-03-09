package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.WebServicesActionsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * WebServicesActionsSql.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/02/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_ACTN table.
 *
 * @version	MyPlates_POS	07/02/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		07/02/2008 09:35:35
 */
public class WebServicesActionsSql
{
	DatabaseAccess caDA;
	
	/**
	 * WebServicesSql.java Constructor
	 * 
	 * <p>Sets the DatabaseAccess object.
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public WebServicesActionsSql(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Query RTS.RTS_ACTN
	 *
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryRtsActn() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryRtsActn - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT ActnId, ActnDesc FROM RTS.RTS_ACTN ORDER BY ActnId");

		try
		{
			Log.write(Log.SQL, this, " - qryRtsActn - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryRtsActn - SQL - End");

			while (lrsQry.next())
			{
				WebServicesActionsData laWebServicesActionsData =
					new WebServicesActionsData();
				laWebServicesActionsData.setActnId(
					caDA.getIntFromDB(lrsQry, "ActnId"));
				laWebServicesActionsData.setActnDesc(
					caDA.getStringFromDB(lrsQry, "ActnDesc"));

				lvRslt.addElement(laWebServicesActionsData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryRtsActn - End ");
			return (lvRslt);
		}

		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRtsActn - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

}
