package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * V21RequestIdToRTSTransId.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/04/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	06/14/2010	add TransId to Insert
 * 							modify insV21RequestIdToRTSTransId()
 * 							defect 10505 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database 
 *
 * @version	6.5.0			06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		02/04/2008 18:05:00 
 */
public class V21RequestIdToRTSTransId
{
	DatabaseAccess caDA;

	/**
	 * V21RequestIdToRTSTransId constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public V21RequestIdToRTSTransId(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert into RTS.RTS_V21REQID_TO_RTSTRANSID 
	 * 
	 * @param  aaV21RequestData	V21RequestData
	 * @throws RTSException 
	 */
	public V21RequestData insV21RequestIdToRTSTransId(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insV21RequestIdToRTSTransId - Begin");

		Vector lvValues = new Vector();

		// defect 10505 
		String lsIns =
			"INSERT into RTS.RTS_V21REQID_TO_RTSTRANSID("
				+ "V21ReqId,"
				+ "OfcissuanceNo,"
				+ "TransWsId,"
				+ "TransAMDate,"
				+ "TransTime, "
				+ "TransId) "
				+ "Values ("
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ? )";
		// end defect 10505 

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
						aaV21RequestData
							.getTransactionKey()
							.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData
							.getTransactionKey()
							.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData
							.getTransactionKey()
							.getTransAMDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData
							.getTransactionKey()
							.getTransTime())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaV21RequestData.getTransactionKey().getTransId()));
			// end defect 10505 

			Log.write(
				Log.SQL,
				this,
				"insV21RequestIdToRTSTransId - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insV21RequestIdToRTSTransId - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insV21RequestIdToRTSTransId - End");

			return aaV21RequestData;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insV21RequestIdToRTSTransId - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
}
