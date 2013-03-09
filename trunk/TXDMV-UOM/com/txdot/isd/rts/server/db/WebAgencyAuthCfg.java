package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyAuthCfgData;
import com.txdot.isd.rts.services.data.WebAgencyAuthData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyAuthCfg.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell 	01/05/2011 	Renamings per standards 
 *        					defect 10708 Ver 6.7.0  
 * K Harrell	01/10/2011	add PilotIndi,DeleteIndi 
 * 							defect 10708 Ver 6.7.0
 * K Harrell	04/26/2011	remove references to PilotIndi 
 * 							defect 10708 Ver 6.7.0 	  
 * K.McKee      07/06/2011  corrected updWebAgencyAuthCfg
 * 							defect 10708 Ver 6.8.0
 * K.McKee      07/07/2011  added reference to PilotIndi to correct null pointer
 * 							defect 10708 Ver 6.8.0
 * K McKee      08/16/2011  Add delWebAgencyAuthCfg()
 * 							defect 10729 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNCY_AUTH_CFG 
 *
 * @version	6.8.1			08/18/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyAuthCfg
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyAuthCfg.java Constructor
	 * 
	 */
	public WebAgencyAuthCfg(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS_WEB_AGNCY_AUTH_CFG
	 *
	 * @param  aiIdntyNo 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyAuthCfg(int aiIdntyNo)
		throws RTSException
	{
		csMethod = " qryWebAgencyAuthCfg";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNCYAUTHIDNTYNO,"
				+ "KEYENTRYCD,"
				+ "ISSUEINVINDI,"
				+ "EXPPROCSNGCD,"
				+ "EXPPROCSNGMOS,"
				+ "MAXSUBMITDAYS,"
				+ "MAXSUBMITCOUNT,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP "
				+ "from RTS.RTS_WEB_AGNCY_AUTH_CFG "
				+ "WHERE AGNCYAUTHIDNTYNO = ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiIdntyNo)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyAuthCfgData laData =
					new WebAgencyAuthCfgData();

				laData.setAgncyAuthIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyAuthIdntyNo"));

				laData.setKeyEntryCd(
					caDA.getStringFromDB(lrsQry, "KeyEntryCd"));

				laData.setIssueInvIndi(
					caDA.getIntFromDB(lrsQry, "IssueInvIndi"));

				laData.setExpProcsngCd(
					caDA.getStringFromDB(lrsQry, "ExpProcsngCd"));

				laData.setExpProcsngMos(
					caDA.getIntFromDB(lrsQry, "ExpProcsngMos"));
				laData.setMaxSubmitCount(
					caDA.getIntFromDB(lrsQry, "MaxSubmitCount"));

				laData.setMaxSubmitDays(
					caDA.getIntFromDB(lrsQry, "MaxSubmitDays"));

				laData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));

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

	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY_AUTH_CFG  
	 *
	 * @param  aaData	WebAgencyAuthCfgData	
	 * @throws RTSException 
	 */
	public void insWebAgencyAuthCfg(WebAgencyAuthCfgData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyAuthCfg";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_AUTH_CFG("
				+ "AGNCYAUTHIDNTYNO,"
				+ "KEYENTRYCD,"
				+ "ISSUEINVINDI,"
				+ "EXPPROCSNGCD,"
				+ "EXPPROCSNGMOS,"
				+ "MAXSUBMITDAYS,"
				+ "MAXSUBMITCOUNT,"
				+ "PILOTINDI,"
				+ "DELETEINDI,"
				+ "UPDTNGAGNTIDNTYNO, "
				+ "CHNGTIMESTMP) "
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
				+ " Current Timestamp)";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthIdntyNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getKeyEntryCd())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getIssueInvIndi())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getExpProcsngCd())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getExpProcsngMos())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMaxSubmitDays())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMaxSubmitCount())));

			// 8						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPilotIndi())));

			// 9						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			//10
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getUpdtngAgntIdntyNo())));

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
	 * Method to update RTS.RTS_WEB_AGNCY_AUTH_CFG
	 * 
	 * @param  aaData WebAgencyAuthCfgData	
	 * @return int
	 * @throws RTSException 
	 */
	public void updWebAgencyAuthCfg(WebAgencyAuthCfgData aaData)
		throws RTSException
	{
		csMethod = "updWebAgencyAuthCfg";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_AUTH_CFG "
					+ " SET "
					+ " KeyEntryCd = ?, "
					+ " IssueInvIndi = ?, "
					+ " ExpProcsngCd = ?, "
					+ " ExpProcsngMos = ?, "
					+ " MaxSubmitDays = ?, "
					+ " MaxSubmitCount = ?, "
					+ " DeleteIndi = ?, "
					+ " UPDTNGAGNTIDNTYNO = ?, "
					+ " Chngtimestmp = Current Timestamp "
					+ " where "
					+ " AgncyAuthIdntyNo = ?";

			// 1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getKeyEntryCd())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getIssueInvIndi())));

			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getExpProcsngCd())));

			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getExpProcsngMos())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMaxSubmitDays()))); 
			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMaxSubmitCount())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			// 8
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getUpdtngAgntIdntyNo())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthIdntyNo())));
						
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
	 
	/**
	 * Method to update RTS.RTS_WEB_AGNCY_AUTH_CFG
	 * and set the delete indicator
	 * 
	 * @param  aaData WebAgencyAuthData	
	 * @throws RTSException 
	 */
	public void delWebAgencyAuthCfg(WebAgencyAuthData aaData)
		throws RTSException
	{
		csMethod = "delWebAgencyAuthCfg";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_AUTH_CFG "
					+ " SET "
					+ " DeleteIndi = ?, "
					+ " Chngtimestmp = Current Timestamp "
					+ " where "
					+ " AgncyAuthIdntyNo = ?";

			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			//  
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthIdntyNo())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	// End defect 10729  
}
