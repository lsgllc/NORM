package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.RegistrationWeightFeesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * RegistrationWeightFees.java
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
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryRegistrationWeightFees()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                  
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_REGIS_WT_FEES
 * 
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br>Creation Date:	08/31/2001 17:32:26
 */

public class RegistrationWeightFees extends RegistrationWeightFeesData
{
	DatabaseAccess caDA;
	/**
	 * RegistrationWeightFees constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public RegistrationWeightFees(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_REGIS_WT_FEES
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryRegistrationWeightFees()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryRegistrationWeightFees - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RegClassCd,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "BegWtRnge,"
				+ "EndWtRnge,"
				+ "TireTypeCd,"
				+ "TireTypeFee "
				+ "FROM RTS.RTS_REGIS_WT_FEES");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRegistrationWeightFees - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(
				Log.SQL,
				this,
				" - qryRegistrationWeightFees - SQL - End");

			while (lrsQry.next())
			{
				RegistrationWeightFeesData laRegWtFeesData =
					new RegistrationWeightFeesData();
				laRegWtFeesData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laRegWtFeesData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laRegWtFeesData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laRegWtFeesData.setBegWtRnge(
					caDA.getIntFromDB(lrsQry, "BegWtRnge"));
				laRegWtFeesData.setEndWtRnge(
					caDA.getIntFromDB(lrsQry, "EndWtRnge"));
				laRegWtFeesData.setTireTypeCd(
					caDA.getStringFromDB(lrsQry, "TireTypeCd"));
				laRegWtFeesData.setTireTypeFee(
					caDA.getDollarFromDB(lrsQry, "TireTypeFee"));
				// Add element to the Vector
				lvRslt.addElement(laRegWtFeesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRegistrationWeightFees - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRegistrationWeightFees - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
