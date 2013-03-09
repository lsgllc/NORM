package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.HolidayData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * Holiday.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/03/2011	Created
 * 							defect 9919 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/** 
 * This class allows user to access RTS_HOLIDAY
 * 
 * @version	6.9.0		10/03/2011 
 * @author	Kathy Harrell 
 * @since 				10/03/2011  17:56:17 
 */
public class Holiday
{
	private String csMethod; 
	private DatabaseAccess caDA;
	
	/**
	 * Holiday constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public Holiday(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Query RTS_HOLIDAY
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryHoliday() throws RTSException
	{
		csMethod = "qryHoliday"; 
		
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ " HOLIDAYDATE,"
				+ " HOLIDAYNAME "
				+ " FROM "
				+ " RTS.RTS_HOLIDAY "
				+ " WHERE ALLAGENCIESCLOSEDINDI = 1 "
				+ "	ORDER BY HOLIDAYDATE");

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + " - SQL - Begin");
			
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				HolidayData laHolidayData =
					new HolidayData();
				
				laHolidayData.setHolidayDate(
					caDA.getIntFromDB(lrsQry, "HolidayDate"));

				laHolidayData.setHolidayName(
					caDA.getStringFromDB(lrsQry, "HolidayName"));

				// Add element to the Vector
				lvRslt.addElement(laHolidayData);
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
				csMethod + " - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
}
