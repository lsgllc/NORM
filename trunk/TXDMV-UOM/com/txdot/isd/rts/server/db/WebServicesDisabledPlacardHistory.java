package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.webservices.dsabldplcrd.data.RtsDsabldPlcrdRequest;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebServicesDisabledPlacardHistory.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/27/2010	Created
 * 							defect 10607 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * WebServicesDisabledPlacardHistory.java 
 *
 * @version	6.6.0			09/27/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/27/2010 09/27/2010
 */
public class WebServicesDisabledPlacardHistory
{
	private DatabaseAccess caDA;
	
	private String csMethod = new String();

	/**
	 * WebServicesDisabledPlacardHistory.java Constructor
	 * 
	 */
	public WebServicesDisabledPlacardHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SRVC_DSABLD_PLCRD_HSTRY
	 *
	 * @param  aiSavReqId 
	 * @param  aaRequest	
	 * @throws RTSException 
	 */
	public void insWebServicesDisabledPlacardHistory(
		int aiSavReqId,
		RtsDsabldPlcrdRequest aaRequest)
		throws RTSException
	{
		csMethod = "insWebServicesInventoryHistory";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_SRVC_DSABLD_PLCRD_HSTRY("
				+ "SavReqId,"
				+ "InvItmNo,"
				+ "RowCount "
				+ ") VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " 0 ) ";

		try
		{
			// 1  SAVREQID - INTEGER 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSavReqId)));

			// 2  INVITMNO - CHAR 
			lvValues.addElement(
				new DBValue(Types.CHAR, aaRequest.getInvItmNo()));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Update RTS.RTS_SRVC_DSABLD_PLCRD_HSTRY
	 *
	 * @param  aiSavReqId 
	 * @param  aiRowCount	
	 * @throws RTSException 
	 */
	public void updWebServicesDisabledPlacardHistory(
		int aiSavReqId,
		int aiRowCount)
		throws RTSException
	{
		csMethod = "updWebServicesDisabledPlacardHistory";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"UPDATE RTS.RTS_SRVC_DSABLD_PLCRD_HSTRY "
				+ " SET ROWCOUNT = ? WHERE "
				+ " SAVREQID = ? ";

		try
		{
			// 1  ROWCOUNT - INTEGER 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiRowCount)));
					
			// 2  SAVREQID - INTEGER 
					 lvValues.addElement(
						 new DBValue(
							 Types.INTEGER,
							 DatabaseAccess.convertToString(aiSavReqId)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			int liNumRows = caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
}
