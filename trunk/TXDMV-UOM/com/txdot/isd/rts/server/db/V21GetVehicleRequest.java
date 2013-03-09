package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * V21GetVehicleRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/03/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/18/2010  Add code to insert VinaLookupIndi
 * 							modify insV21GetVehicleRequest()
 * 							defect 10337 Ver POS_640  
 * ---------------------------------------------------------------------
 */
/**
 * This class provides access to RTS.RTS_V21_GET_VEH_REQ  
 *
 * @version	POS_640			02/18/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/03/2007  13:09:00 
 */
public class V21GetVehicleRequest
{
	DatabaseAccess caDA;

	/**
	 * V21GetVehicleRequest constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public V21GetVehicleRequest(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert into RTS.RTS_V21_GET_VEH_REQ
	 * 
	 * @param  aaV21RequestData
	 * @throws RTSException 
	 */
	public void insV21GetVehicleRequest(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insV21GetVehicleRequest - Begin");

		Vector lvValues = new Vector();

		// defect 10337 
		String lsIns =
			"INSERT into RTS.RTS_V21_GET_VEH_REQ("
				+ "V21ReqId,"
				+ "DOCNO,"
				+ "VIN,"
				+ "LAST4VIN,"
				+ "REGPLTNO, "
				+ "VINALOOKUPINDI)"
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";
		// end defect 10337 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21ReqId())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getDocNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getVIN())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getLast4VIN())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegPltNo())));

			// defect 10337 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getVinaLookupIndi())));
			// end defect 10337 

			Log.write(
				Log.SQL,
				this,
				"insV21GetVehicleRequest - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insV21GetVehicleRequest - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insV21GetVehicleRequest - End");

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insV21GetVehicleRequest - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
}
