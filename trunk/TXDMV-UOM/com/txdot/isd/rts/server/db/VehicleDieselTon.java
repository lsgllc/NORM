package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.VehicleDieselTonData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * VehicleDieselTon.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks 		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryVehicleDieselTon()	
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
 
/**
 * This class allows user to access RTS_VEH_DIESEL_TON 
 * 
 * @version	5.2.3			06/19/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/06/2001 10:09:32 
 */
 
public class VehicleDieselTon extends VehicleDieselTonData
{
	DatabaseAccess caDA;
/**
 * VehicleDieselTon constructor 
 *
 * @param  aaDA DatabaseAccess
 * @throws RTSException 
 */
public VehicleDieselTon(DatabaseAccess aaDA) throws RTSException
{
	caDA = aaDA;
}
/**
 * Method to Query RTS.RTS_VEH_DIESEL_TON
 *
 * @return Vector
 * @throws RTSException
 */
public Vector qryVehicleDieselTon()
	throws RTSException
{
	Log.write(Log.METHOD, this, " - qryVehicleDieselTon - Begin");
	
	StringBuffer lsQry = new StringBuffer();
	
	Vector lvRslt = new Vector();
	
	ResultSet lrsQry;
	
	lsQry.append(
		"SELECT "
			+ "RegClassCd,"
			+ "RTSEffDate,"
			+ "RTSEffEndDate,"
			+ "BegDieselVehTon,"
			+ "EndDieselVehTon,"
			+ "DieselFeePrcnt "
			+ "FROM RTS.RTS_VEH_DIESEL_TON ");
	try
	{
		Log.write(Log.SQL, this, " - qryVehicleDieselTon - SQL - Begin");
		lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
		Log.write(Log.SQL, this, " - qryVehicleDieselTon - SQL - End");
		
		while (lrsQry.next())
		{
			VehicleDieselTonData laVehicleDieselTonData = new VehicleDieselTonData();
			laVehicleDieselTonData.setRegClassCd(
				caDA.getIntFromDB(lrsQry, "RegClassCd"));
			laVehicleDieselTonData.setRTSEffDate(
				caDA.getIntFromDB(lrsQry, "RTSEffDate"));
			laVehicleDieselTonData.setRTSEffEndDate(
				caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
			laVehicleDieselTonData.setBegDieselVehTon(
				caDA.getDollarFromDB(lrsQry, "BegDieselVehTon"));
			laVehicleDieselTonData.setEndDieselVehTon(
				caDA.getDollarFromDB(lrsQry, "EndDieselVehTon"));
			laVehicleDieselTonData.setDieselFeePrcnt(
				caDA.getIntFromDB(lrsQry, "DieselFeePrcnt"));
			// Add element to the Vector
			lvRslt.addElement(laVehicleDieselTonData);
		} //End of While
		
		lrsQry.close();
		caDA.closeLastDBStatement();
		lrsQry = null;
		Log.write(Log.METHOD, this, " - qryVehicleDieselTon - End ");
		return (lvRslt);
	}
	catch (SQLException leSQLEx)
	{
		Log.write(
			Log.SQL_EXCP,
			this,
			" - qryVehicleDieselTon - SQL Exception " + leSQLEx.getMessage());
		throw new RTSException(RTSException.DB_ERROR, leSQLEx);
	}
} //END OF QUERY METHOD
} //END OF CLASS
