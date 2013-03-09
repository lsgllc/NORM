package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.VehicleBodyTypesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * VehicleBodyTypes.java
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
 * K Harrell	04/04/2005	Remove parameter from qryVehicleBodyTypes()	
 * 							defect 7846 Ver 5.2.3  
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */
 
/**
 * This class allows user to access RTS_VEH_BODY_TYPES 
 * 
 *
 * @version	5.2.3			05/03/2005
 * @author	Kathy Harrell
 * <b>Creation Date:		08/31/2001 16:25:26 
 */

public class VehicleBodyTypes extends VehicleBodyTypesData
{
    DatabaseAccess caDA;
/**
 * VehicleBodyTypes constructor 
 *
 * @param  aaDA DatabaseAccess
 * @throws RTSException 
 */
public VehicleBodyTypes(DatabaseAccess aaDA) throws RTSException
{
	caDA = aaDA;
}
/**
 * Method to Query RTS.RTS_VEH_BDY_TYPES
 *
 * @return Vector
 * @throws RTSException 
 */
public Vector qryVehicleBodyTypes()
	throws RTSException
{
	Log.write(Log.METHOD, this, " - qryVehicleBodyTypes - Begin");
	
	StringBuffer lsQry = new StringBuffer();
	
	Vector lvRslt = new Vector();
	
	ResultSet lrsQry;
	
	lsQry.append(
		"SELECT VehBdyType, VehBdyTypeDesc FROM RTS.RTS_VEH_BDY_TYPES");
	
	try
	{
		Log.write(Log.SQL, this, " - qryVehicleBodyTypes - SQL - Begin");
		lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
		Log.write(Log.SQL, this, " - qryVehicleBodyTypes - SQL - End");

		while (lrsQry.next())
		{
			VehicleBodyTypesData laVehicleBodyTypesData =
				new VehicleBodyTypesData();
			laVehicleBodyTypesData.setVehBdyType(
				caDA.getStringFromDB(lrsQry, "VehBdyType"));
			laVehicleBodyTypesData.setVehBdyTypeDesc(
				caDA.getStringFromDB(lrsQry, "VehBdyTypeDesc"));
			// Add element to the Vector
			lvRslt.addElement(laVehicleBodyTypesData);
		} //End of While
		
		lrsQry.close();
		caDA.closeLastDBStatement();
		lrsQry = null;
		Log.write(Log.METHOD, this, " - qryVehicleBodyTypes - End ");
		return (lvRslt);
		
	}
	catch (SQLException leSQLEx)
	{
		Log.write(
			Log.SQL_EXCP,
			this,
			" - qryVehicleBodyTypes - SQL Exception " + leSQLEx.getMessage());
		throw new RTSException(RTSException.DB_ERROR, leSQLEx);
	}
} //END OF QUERY METHOD
} //END OF CLASS
