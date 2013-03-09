package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgentSecurityLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgentSecurityLog.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	04/26/2011	remove reference to DashAccs 
 * 							defect 10708 Ver 6.7.0 	 
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGENT_SECURITY_LOG 
 *
 * @version	6.7.0			12/28/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgentSecurityLog
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgentSecurityLog Constructor
	 * 
	 */
	public WebAgentSecurityLog(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Insert into RTS.RTS_WEB_AGNT_SECURITY_LOG  
	 *
	 * @param  aaData	WebAgentSecurityData	
	 * @throws RTSException 
	 */
	public void insWebAgentSecurityLog(WebAgentSecurityLogData aaData)
		throws RTSException
	{
		csMethod = "insWebAgentSecurityLog";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNT_SECURITY_LOG("
				+ "AGNTSECRTYIDNTYNO,"
				+ "AGNTIDNTYNO,"
				+ "AGNCYIDNTYNO,"
				+ "AGNCYAUTHACCS,"
				+ "USERAUTHACCS,"
				+ "AGNCYINFOACCS,"
				+ "BATCHACCS,"
				+ "RENWLACCS,"
				+ "REPRNTACCS,"
				+ "VOIDACCS,"
				+ "RPTACCS,"
				+ "APRVBATCHACCS,"
				+ "SUBMITBATCHACCS,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP, "
				+ "UPDTNGAGNTIDNTYNO,"
				+ "UPDTNGACTN,"
				+ "UPDTNGIPADDR "
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ? )";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntSecrtyIdntyNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntIdntyNo())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthAccs())));

			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntAuthAccs())));

			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyInfoAccs())));
		
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getBatchAccs())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRenwlAccs())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getReprntAccs())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getVoidAccs())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRptAccs())));
			// 12
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAprvBatchAccs())));

			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSubmitBatchAccs())));

			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			// 15
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getChngTimestmp())));

			// 16
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getUpdtngActn())));

			// 17
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));
			// 18								 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getUpdtngIPAddr())));

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
