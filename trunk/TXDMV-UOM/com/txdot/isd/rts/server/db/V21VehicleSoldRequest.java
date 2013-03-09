package com.txdot.isd.rts.server.db;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * V21VehicleSoldRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/04/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/20/2008	Modified column name V21UniqueId to V21VTNId 
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/25/2008	Add RegPltNo
 * 							defect 9546 Ver 3 Amigos PH A 
 * ---------------------------------------------------------------------
 */
/**
 * This class provides access to RTS.RTS_V21_VEH_SOLD_REQ  
 *
 * @version	3 Amigos PH A	02/25/2008
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/04/2007  13:45:00 
 */
public class V21VehicleSoldRequest
{
	DatabaseAccess caDA;

	/**
	 * V21VehicleSoldRequest constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public V21VehicleSoldRequest(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert into RTS.RTS_V21_VEH_SOLD_REQ  
	 * 
	 * @param  aaV21RequestData
	 * @throws RTSException 
	 */
	public void insV21VehicleSoldRequest(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insV21VehicleSoldRequest - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_V21_VEH_SOLD_REQ("
				+ "V21ReqId,"
				+ "V21VTNId,"
				+ "DocNo,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "SpclRegId,"
				+ "VehSoldDate,"
				+ "RegPltNo,"
				+ "VTNSource) VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21ReqId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21IntrfcLogId())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getDocNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegExpMo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegExpYr())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaV21RequestData.getSpclRegId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getVehSoldDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegPltNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getVTNSource())));

			Log.write(
				Log.SQL,
				this,
				"insV21VehicleSoldRequest - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insV21VehicleSoldRequest - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"insV21VehicleSoldRequest - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insV21VehicleSoldRequest - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
