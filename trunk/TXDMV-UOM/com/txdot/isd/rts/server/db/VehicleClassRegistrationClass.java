package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.VehicleClassRegistrationClassData;
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
 * K Harrell  	12/05/2001	Removed comparison of date.
 * R Hicks 		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryVehicleClassRegistrationClass()	
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3   
 * B Hargrove	09/23/2010	Add new DfltRegClassCdIndi column
 * 							modify qryVehicleClassRegistrationClass()
 * 							defect 10600 Ver 6.6.0
 * ---------------------------------------------------------------------
 */
 
/**
 * This class allows user to access RTS.RTS_REGIS_CLASS joined 
 * to RTS.RTS_COMMON_FEES
 *
 * @version	Ver 6.6.0		09/23/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/06/2001 12:16:32 
 */
 
public class VehicleClassRegistrationClass
    extends VehicleClassRegistrationClassData
{
    DatabaseAccess caDA;
/**
 * VehicleClassRegistrationClass constructor 
 *
 * @param aaDA DatabaseAccess
 * @throws RTSException 
 */
public VehicleClassRegistrationClass(DatabaseAccess aaDA)
	throws RTSException
{
	caDA = aaDA;
}
/**
 * Method to Query RTS.RTS_REGIS_CLASS joined to RTS.RTS_COMMON_FEES
 * for description of RegClassCd available to VehClassCd
 * 
 * @return Vector
 * @throws RTSException 
 */
public Vector qryVehicleClassRegistrationClass()
	throws RTSException
{
	Log.write(
		Log.METHOD,
		this,
		" - qryVehicleClassRegistrationClass - Begin");
	
	StringBuffer lsQry = new StringBuffer();
	
	Vector lvRslt = new Vector();
	
	ResultSet lrsQry;
	
	// defect 10600
	// add DfltRegClassCdIndi 
	lsQry.append(
		"SELECT DISTINCT "
			+ "VehClassCd,"
			+ "A.RegClassCd,"
			+ "A.DfltRegClassCdIndi,"
			+ "A.RTSEffDate,"
			+ "A.RTSEffEndDate,"
			+ "RegClassCdDesc "
			+ "FROM RTS.RTS_REGIS_CLASS A, RTS.RTS_COMMON_FEES B WHERE A.REGCLASSCD = B.REGCLASSCD");
	// end defect 10600
	try
	{
		Log.write(
			Log.SQL,
			this,
			" - qryVehicleClassRegistrationClass - SQL - Begin");
		lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
		Log.write(
			Log.SQL,
			this,
			" - qryVehicleClassRegistrationClass - SQL - End");
		
		while (lrsQry.next())
		{
			VehicleClassRegistrationClassData laVehicleClassRegistrationClassData =
				new VehicleClassRegistrationClassData();
			laVehicleClassRegistrationClassData.setVehClassCd(
				caDA.getStringFromDB(lrsQry, "VehClassCd"));
			laVehicleClassRegistrationClassData.setRegClassCd(
				caDA.getIntFromDB(lrsQry, "RegClassCd"));
			// defect 10600
			// Add DfltRegClassCdIndi 
			laVehicleClassRegistrationClassData.setDfltRegClassCdIndi(
				caDA.getIntFromDB(lrsQry, "DfltRegClassCdIndi"));
			// end defect 10600
			laVehicleClassRegistrationClassData.setRTSEffDate(
				caDA.getIntFromDB(lrsQry, "RTSEffDate"));
			laVehicleClassRegistrationClassData.setRTSEffEndDate(
				caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
			laVehicleClassRegistrationClassData.setRegClassCdDesc(
				caDA.getStringFromDB(lrsQry, "RegClassCdDesc"));
			// Add element to the Vector
			lvRslt.addElement(laVehicleClassRegistrationClassData);
		} //End of While
		
		lrsQry.close();
		caDA.closeLastDBStatement();
		lrsQry = null;
		Log.write(
			Log.METHOD,
			this,
			" - qryVehicleClassRegistrationClass - End ");
		return (lvRslt);
	}
	catch (SQLException leSQLEx)
	{
		Log.write(
			Log.SQL_EXCP,
			this,
			" - qryVehicleClassRegistrationClass - SQL Exception "
				+ leSQLEx.getMessage());
		throw new RTSException(RTSException.DB_ERROR, leSQLEx);
	}
} //END OF QUERY METHOD
} //END OF CLASS
