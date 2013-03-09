package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.DeleteReasonsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * DeleteReasons.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	04/04/2005	Remove parameter from qryDeleteReasons()
 * 							defect 7846 Ver 5.2.3
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3                    
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_DELETE_REASONS
 *
 * @version	5.2.3		06/30/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 14:21:45
 */

public class DeleteReasons extends DeleteReasonsData
{
	DatabaseAccess caDA;
	/**
	 * DeleteReasons constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DeleteReasons(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_DELETE_REASONS
	 *
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryDeleteReasons()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDeleteReasons - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "DelInvReasnCd,"
				+ "DelInvReasn "
				+ "FROM RTS.RTS_DELETE_REASONS");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDeleteReasons - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryDeleteReasons - SQL - End");

			while (lrsQry.next())
			{
				DeleteReasonsData laDeleteReasonsData =
					new DeleteReasonsData();
				laDeleteReasonsData.setDelInvReasnCd(
					caDA.getIntFromDB(lrsQry, "DelInvReasnCd"));
				laDeleteReasonsData.setDelInvReasn(
					caDA.getStringFromDB(lrsQry, "DelInvReasn"));
				// Add element to the Vector
				lvRslt.addElement(laDeleteReasonsData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryDeleteReasons - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDeleteReasons - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
