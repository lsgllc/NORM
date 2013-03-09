package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OfficeTimeZoneData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * OfficeTimeZone.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/03/2010	Created
 * 							defect 10427 Ver POS_640  
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_OFC_TIMEZONE  
 *
 * @version	POS_640			04/03/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		04/03/2010 11:46:17
 */
public class OfficeTimeZone
{
	private String csMethod = new String();

	private DatabaseAccess caDA;

	/**
	 * OfficeTimeZone Constructor
	 */
	public OfficeTimeZone()
	{
		super();
	}

	/**
	 * OfficeTimeZone Constructor
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public OfficeTimeZone(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_OFC_TIMEZONE 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryOfficeTimeZone() throws RTSException
	{
		csMethod = " - qryOfficeTimeZone";

		Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_BEGIN);

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvResult = new Vector();

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "TimeZone "
				+ "FROM RTS.RTS_OFC_TIMEZONE ");
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (lrsQry.next())
			{
				OfficeTimeZoneData laOfcTZData =
					new OfficeTimeZoneData();

				laOfcTZData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				laOfcTZData.setTimeZone(
					caDA.getStringFromDB(lrsQry, "TimeZone"));

				lvResult.add(laOfcTZData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
			return (lvResult);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
					
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
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
	} //END OF QUERY METHOD
} //END OF CLASS
