package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.webservices.ren.data.RtsRenewalRequest;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebServicesRenewalLookupHistory.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/14/2011	Created
 * 							defect 10768 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * This class provides access to RTS.RTS_SRVC_WEB_REN_LOOKUP_HSTRY 
 *
 * @version	6.7.1			03/14/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		03/14/2011 11:51:17 
 */
public class WebServicesRenewalLookupHistory
{
	DatabaseAccess caDA;

	String csMethod = new String();

	/**
	 * WebServicesRenewalLookupHistory.java Constructor
	 * 
	 * 
	 */
	public WebServicesRenewalLookupHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SRVC_WEB_REN_LOOKUP_HSTRY 
	 *
	 * @param  aaRequest	
	 * @throws RTSException 
	 */
	public void insWebServicesRenewalLookupHistory(RtsRenewalRequest aaRequest)
		throws RTSException
	{
		csMethod = "insWebServicesRenewalLookupHistory";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_SRVC_WEB_REN_LOOKUP_HSTRY ("
				+ "SavReqId,"
				+ "DocNo,"
				+ "RegPltNo,"
				+ "Last4VIN "
				+ ") VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ? ) ";

		try
		{
			// 1  SAVREQID - INTEGER 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaRequest.getRequestIdntyNo())));

			// 2  DOCNO - CHAR 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRequest.getDocNo())));

			// 3  REGPLTNO - CHAR 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRequest.getRegPltNo())));

			// 4  LAST4VIN - CHAR 
			lvValues.addElement(
				new DBValue(Types.CHAR, aaRequest.getLast4OfVin()));

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

}
