package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.IndicatorStopCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * IndicatorStopCodes.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryIndicatorStopCodes()
 * 							defect 7846 Ver 5.2.3     
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                   
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_INDI_STOP_CDS.
 *
 * @version	5.2.3		06/19/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 14:53:22 
 */

public class IndicatorStopCodes extends IndicatorStopCodesData
{
	DatabaseAccess caDA;
	/**
	 * IndicatorStopCodes constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public IndicatorStopCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_INDI_STOP_CDS
	 *
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryIndicatorStopCodes()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIndicatorStopCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "IndiTransCd,"
				+ "IndiName,"
				+ "IndiFieldValue,"
				+ "IndiStopCd "
				+ "FROM RTS.RTS_INDI_STOP_CDS");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryIndicatorStopCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryIndicatorStopCodes - SQL - End");

			while (lrsQry.next())
			{
				IndicatorStopCodesData laIndicatorStopCodesData =
					new IndicatorStopCodesData();
				laIndicatorStopCodesData.setIndiTransCd(
					caDA.getStringFromDB(lrsQry, "IndiTransCd"));
				laIndicatorStopCodesData.setIndiName(
					caDA.getStringFromDB(lrsQry, "IndiName"));
				laIndicatorStopCodesData.setIndiFieldValue(
					caDA.getStringFromDB(lrsQry, "IndiFieldValue"));
				laIndicatorStopCodesData.setIndiStopCd(
					caDA.getStringFromDB(lrsQry, "IndiStopCd"));
				// Add element to the Vector
				lvRslt.addElement(laIndicatorStopCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryIndicatorStopCodes - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIndicatorStopCodes - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS