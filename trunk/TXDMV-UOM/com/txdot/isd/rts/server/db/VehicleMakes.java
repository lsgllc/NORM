package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.VehicleMakesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * VehicleMakes.java
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
 * K Harrell	04/04/2005	Remove parameter from qryVehicleMakes()	
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */
 
/**
 * This class allows user to access RTS_VEH_MAKES.
 * 
 * @version	5.2.3			06/19/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		08/31/2001 16:28:45
 */
 
public class VehicleMakes extends VehicleMakesData
{
	DatabaseAccess caDA;
/**
 * VehicleMakes constructor 
 *
 * @param aaDA DatabaseAccess
 * @throws RTSException 
 */
public VehicleMakes(DatabaseAccess aaDA) throws RTSException
{
	caDA = aaDA;
}
/**
* Method to Query RTS.RTS_VEH_MAKES
*
* @return Vector 
* @throws RTSException	
*/
public Vector qryVehicleMakes()
	throws RTSException
{
	Log.write(Log.METHOD, this, " - qryVehicleMakes - Begin");
	
	StringBuffer lsQry = new StringBuffer();
	
	Vector lvRslt = new Vector();
	
	ResultSet lrsQry;
	
	lsQry.append("SELECT VEHMK,VEHMKDesc FROM RTS.RTS_VEH_MAKES");
	
	try
	{
		Log.write(Log.SQL, this, " - qryVehicleMakes - SQL - Begin");
		lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
		Log.write(Log.SQL, this, " - qryVehicleMakes - SQL - End");
		
		while (lrsQry.next())
		{
			VehicleMakesData laVehicleMakesData = new VehicleMakesData();
			laVehicleMakesData.setVehMk(caDA.getStringFromDB(lrsQry, "VehMk"));
			laVehicleMakesData.setVehMkDesc(
				caDA.getStringFromDB(lrsQry, "VehMkDesc"));
			// Add element to the Vector
			lvRslt.addElement(laVehicleMakesData);
		} //End of While
		
		lrsQry.close();
		caDA.closeLastDBStatement();
		lrsQry = null;
		Log.write(Log.METHOD, this, " - qryVehicleMakes - End ");
		return (lvRslt);
	}

	catch (SQLException leSQLEx)
	{
		Log.write(
			Log.SQL_EXCP,
			this,
			" - qryVehicleMakes - SQL Exception " + leSQLEx.getMessage());
		throw new RTSException(RTSException.DB_ERROR, leSQLEx);
	}
} //END OF QUERY METHOD
} //END OF CLASS
