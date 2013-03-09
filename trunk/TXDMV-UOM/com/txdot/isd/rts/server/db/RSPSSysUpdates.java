package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RSPSSysUpdateData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RSPSSysUpdates.java
 * 
 * (c) Texas Department of Transportation  2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/19/2004	new class
 *							defect 7135 Ver 5.2.1
 * K Harrell	03/04/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	03/10/2010	deprecate class
 * 							defect 10239 Ver POS_640  
 * ---------------------------------------------------------------------
 */

/**
 * This class handles sql for RTS_RSPS_SYSUPDT.
 * 
 * @version	POS_640			03/10/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		07/19/2001 08:50:10
 * @deprecated
 */

public class RSPSSysUpdates extends RSPSSysUpdateData
{
	protected DatabaseAccess caDA;
	
	/**
	 * RSPSSysUpdates constructor comment.
	 */
	public RSPSSysUpdates(DatabaseAccess aaDA)
	{
		super();
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_RSPS_SYSUPDT	
	 * 
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryRSPSSysUpdate() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryRSPSSysUpdate - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "SysUpdt,"
				+ "SysUpdtDesc,"
				+ "AvailTimeStmp,"
				+ "SysUpdtFileName "
				+ "FROM RTS.RTS_RSPS_SYSUPDT ");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRSPSSysUpdate - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(Log.SQL, this, " - qryRSPSSysUpdate - SQL - End");

			while (lrsQry.next())
			{
				RSPSSysUpdateData laData = new RSPSSysUpdateData();
				laData.setSysUpdate(
					caDA.getStringFromDB(lrsQry, "SysUpdt"));
				laData.setSysUpdateDescription(
					caDA.getStringFromDB(lrsQry, "SysUpdtDesc"));
				laData.setDateAvailable(
					caDA.getRTSDateFromDB(lrsQry, "AvailTimeStmp"));
				laData.setSysUpdateFileName(
					caDA.getStringFromDB(lrsQry, "SysUpdtFileName"));

				// Add element to the Vector
				lvRslt.addElement(laData);

			} //End of While 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryRSPSSysUpdate - End ");
			return (lvRslt);
		}
		catch (SQLException leSqlEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRSPSSysUpdate - SQL Exception "
					+ leSqlEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSqlEx);
		}
	} //END OF QUERY METHOD	
}
