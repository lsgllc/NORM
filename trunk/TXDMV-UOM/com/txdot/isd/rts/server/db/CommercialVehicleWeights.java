package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.CommercialVehicleWeightsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * CommercialVehicleWeights.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryCommercialVehicleWeights()
 * 							defect 7846 Ver 5.2.3   
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_COMM_VEH_WTS
 * 
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 17:07:49 
 */

public class CommercialVehicleWeights
	extends CommercialVehicleWeightsData
{
	DatabaseAccess caDA;
	/**
	 * CommercialVehicleWeights constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public CommercialVehicleWeights(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_COMM_VEH_WTS
	 *
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryCommercialVehicleWeights() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryCommercialVehicleWeights - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "VehTon,"
				+ "VehTonDesc,"
				+ "MinCaryngCap,"
				+ "MinGrossWtAllowble "
				+ "FROM RTS.RTS_COMM_VEH_WTS");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCommercialVehicleWeights - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryCommercialVehicleWeights - SQL - End");

			while (lrsQry.next())
			{
				CommercialVehicleWeightsData laCommVehWtsData =
					new CommercialVehicleWeightsData();
				laCommVehWtsData.setVehTon(
					caDA.getDollarFromDB(lrsQry, "VehTon"));
				laCommVehWtsData.setVehTonDesc(
					caDA.getStringFromDB(lrsQry, "VehTonDesc"));
				laCommVehWtsData.setMinCaryngCap(
					caDA.getIntFromDB(lrsQry, "MinCaryngCap"));
				laCommVehWtsData.setMinGrossWtAllowble(
					caDA.getIntFromDB(lrsQry, "MinGrossWtAllowble"));
				// Add element to the Vector
				lvRslt.addElement(laCommVehWtsData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCommercialVehicleWeights - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCommercialVehicleWeights - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

} //END OF CLASS
