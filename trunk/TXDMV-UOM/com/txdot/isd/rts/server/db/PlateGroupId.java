package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateGroupIdData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * PlateGroupId.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_PLT_GRP_ID 
 *
 * @version	Special Plates	01/31/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		01/31/2007 17:11:00
 */
public class PlateGroupId extends PlateGroupIdData
{
	DatabaseAccess caDA;

	/**
	 * PlateGroupId constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PlateGroupId(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_PLT_GROUP_ID
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryPlateGroupId() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPlateGroupId - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ " PLTGRPID,"
				+ " PLTGRPDESC,"
				+ " PLTGRPDESCINET1,"
				+ " PLTGRPDESCINET2,"
				+ " PLTGRPTYPECD, "
				+ " CUSTPLTSMAXNO "
				+ " FROM RTS.RTS_PLT_GRP_ID");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryPlateGroupId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryPlateGroupId - SQL - End");

			while (lrsQry.next())
			{
				PlateGroupIdData laPlateGroupIdData =
					new PlateGroupIdData();
				laPlateGroupIdData.setPltGrpId(
					caDA.getStringFromDB(lrsQry, "PLTGRPID"));
				laPlateGroupIdData.setPltGrpDesc(
					caDA.getStringFromDB(lrsQry, "PLTGRPDESC"));
				laPlateGroupIdData.setPltGrpDescInet1(
					caDA.getStringFromDB(lrsQry, "PLTGRPDESCINET1"));
				laPlateGroupIdData.setPltGrpDescInet2(
					caDA.getStringFromDB(lrsQry, "PLTGRPDESCINET2"));
				laPlateGroupIdData.setPltGrpTypeCd(
					caDA.getStringFromDB(lrsQry, "PLTGRPTYPECD"));
				laPlateGroupIdData.setCustPltsMaxNo(
					caDA.getIntFromDB(lrsQry, "CUSTPLTSMAXNO"));
				// Add element to the Vector
				lvRslt.addElement(laPlateGroupIdData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryPlateGroupId - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPlateGroupId - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
