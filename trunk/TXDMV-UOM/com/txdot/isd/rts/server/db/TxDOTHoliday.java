package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TxDOTHolidayData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * TxDOTHoliday.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/05/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/04/2011	deprecated
 * 							defect 9919 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_TXDOT_HOLIDAY
 *
 * @version	6.9.0	10/04/2011
 * @author	Kathy Harrell
 * @since			03/05/2007 17:34:00
 * @deprecated
 */
public class TxDOTHoliday extends TxDOTHolidayData
{
	DatabaseAccess caDA;

	/**
	 * TxDOTHoliday constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TxDOTHoliday(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS_TXDOT_HOLIDAY
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTxDOTHoliday() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTxDOTHoliday - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ " HOLIDAYDATE,"
				+ " HOLIDAYDESC"
				+ " FROM "
				+ " RTS.RTS_TXDOT_HOLIDAY "
				+ "	ORDER BY HOLIDAYDATE");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTxDOTHoliday - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryTxDOTHoliday - SQL - End");

			while (lrsQry.next())
			{
				TxDOTHolidayData laTxDOTHolidayData =
					new TxDOTHolidayData();
				laTxDOTHolidayData.setHolidayDate(
					caDA.getIntFromDB(lrsQry, "HolidayDate"));

				laTxDOTHolidayData.setHolidayDesc(
					caDA.getStringFromDB(lrsQry, "HolidayDesc"));

				// Add element to the Vector
				lvRslt.addElement(laTxDOTHolidayData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryTxDOTHoliday - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTxDOTHoliday - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

} //END OF CLASS