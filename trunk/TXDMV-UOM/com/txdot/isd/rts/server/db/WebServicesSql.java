package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.VehicleMakesData;
import com.txdot.isd.rts.services.data.WebServicesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * WebServicesSql.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/26/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * This class provides sql access to RTS_SRVC.
 *
 * @version	MyPlates_POS	06/26/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		06/26/2008 16:33:55
 */
public class WebServicesSql
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
	public WebServicesSql(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_SRVC
	 *
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryRtsSrvc() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryRtsSrvc - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT SrvId, SrvcName, SrvcDesc FROM RTS.RTS_SRVC");

		try
		{
			Log.write(Log.SQL, this, " - qryRtsSrvc - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryRtsSrvc - SQL - End");

			while (lrsQry.next())
			{
				WebServicesData laWebServicesData =
					new WebServicesData();
				laWebServicesData.setSrvId(
					caDA.getIntFromDB(lrsQry, "SrvId"));
				laWebServicesData.setSrvcName(
					caDA.getStringFromDB(lrsQry, "SrvcName"));
				laWebServicesData.setSrvcDesc(
					caDA.getStringFromDB(lrsQry, "SrvcDesc"));

				lvRslt.addElement(laWebServicesData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryRtsSrvc - End ");
			return (lvRslt);
		}

		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRtsSrvc - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}
}
