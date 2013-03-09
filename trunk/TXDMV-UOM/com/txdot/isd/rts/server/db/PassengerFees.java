package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.PassengerFeesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * PassengerFees.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryPassengerFees()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                   
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PASS_FEES
 * 
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br> Creation Date:	08/31/2001 17:18:34 
 */

public class PassengerFees extends PassengerFeesData
{
	DatabaseAccess caDA;
	/**
	 * PassengerFees constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PassengerFees(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_PASS_FEES
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryPassengerFees()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPassengerFees - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RegClassCd,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "BegModlYr,"
				+ "EndModlYr,"
				+ "RegFee "
				+ "FROM RTS.RTS_PASS_FEES");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryPassengerFees - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryPassengerFees - SQL - End");

			while (lrsQry.next())
			{
				PassengerFeesData laPassengerFeesData =
					new PassengerFeesData();
				laPassengerFeesData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laPassengerFeesData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laPassengerFeesData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laPassengerFeesData.setBegModlYr(
					caDA.getIntFromDB(lrsQry, "BegModlYr"));
				laPassengerFeesData.setEndModlYr(
					caDA.getIntFromDB(lrsQry, "EndModlYr"));
				laPassengerFeesData.setRegFee(
					caDA.getDollarFromDB(lrsQry, "RegFee"));
				// Add element to the Vector
				lvRslt.addElement(laPassengerFeesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryPassengerFees - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPassengerFees - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
