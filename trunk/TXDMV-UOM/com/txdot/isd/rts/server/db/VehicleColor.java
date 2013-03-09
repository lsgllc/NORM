package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleColorData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * VehicleColor.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/05/2011	Created
 * 							defect 10712 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains method to access RTS_VEH_COLOR
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/05/2011 12:49:17
 */
public class VehicleColor
{
	String csMethod;
	DatabaseAccess caDA;

	/**
	 * VehicleColor.java Constructor
	 * 
	 */
	public VehicleColor(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Query RTS_VEH_COLOR
	 *
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryVehColor() throws RTSException
	{
		csMethod = "qryVehColor";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "VEHCOLORCD,"
				+ "VEHCOLORDESC,"
				+ "VEHCOLORGRPCD "
				+ "from RTS.RTS_VEH_COLOR ");
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				VehicleColorData laData = new VehicleColorData();

				laData.setVehColorCd(
					caDA.getStringFromDB(lrsQry, "VehColorCd"));

				laData.setVehColorDesc(
					caDA.getStringFromDB(lrsQry, "VehColorDesc"));

				laData.setVehColorGrpCd(
					caDA.getStringFromDB(lrsQry, "VehColorGrpCd"));

				// Add element to the Vector
				lvRslt.addElement(laData);
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
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

}
