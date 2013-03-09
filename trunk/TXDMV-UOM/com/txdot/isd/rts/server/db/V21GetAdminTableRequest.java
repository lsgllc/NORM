package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * V21GetAdminTableRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/04/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * ---------------------------------------------------------------------
 */
/**
 * This class provides access to RTS.RTS_V21_GET_ADMIN_TBL_REQ 
 *
 * @version	3 Amigos PH A	02/04/2008
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/04/2007  13:17:00 
 */
public class V21GetAdminTableRequest
{
	DatabaseAccess caDA;

	/**
	 * V21GetAdminTableRequest constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public V21GetAdminTableRequest(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert into RTS.RTS_V21_GET_ADMIN_TBL_REQ 
	 * 
	 * @param  aaV21RequestData
	 * @throws RTSException 
	 */
	public void insV21GetAdminTableRequest(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insV21GetAdminTableRequest - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_V21_GET_ADMIN_TBL_REQ("
				+ "V21ReqId,"
				+ "RTSTBLNAME) VALUES ( "
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
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRTSTblName())));

			Log.write(
				Log.SQL,
				this,
				"insV21GetAdminTableRequest - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insV21GetAdminTableRequest - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"insV21GetAdminTableRequest - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insV21GetAdminTableRequest - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
