package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyAuthCfgData;
import com.txdot.isd.rts.services.data.WebAgencyAuthData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyAuth.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell 	01/05/2011  Renamings per standards 
 *        					defect 10708 Ver 6.7.0 
 * K Harrell	03/22/2011	add qryWebAgencyAuthAndCfg() 
 * 							defect 10768 Ver 6.7.1 
 * D Hamilton	07/07/2011  Added parameter for where clause in updWebAgencyAuth()
 * 							defect 10718 Ver 6.8.0
 * K McKee      08/15/2011  add delWebAgencyAuth()
 *                          defect 10729 Ver 6.8.1
 * K McKee      10/20/2011  modify qryWebAgencyAuthAndCfg()
 * 							defect 11151 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNCY_AUTH 
 *
 * @version	6.9.0 			10/20/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyAuth
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyAuth.java Constructor
	 * 
	 */
	public WebAgencyAuth(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS_WEB_AGNCY_AUTH
	 *
	 * @param aiIdntyNo 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyAuth(int aiIdntyNo) throws RTSException
	{
		csMethod = "qryWebAgencyAuth";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNCYAUTHIDNTYNO,"
				+ "OFCISSUANCENO,"
				+ "AGNCYIDNTYNO, "
				+ "SUBCONID,  "
				+ "DELETEINDI,  "
				+ "CHNGTIMESTMP  "
				+ "from RTS.RTS_WEB_AGNCY_AUTH "
				+ " WHERE "
				+ "AGNCYIDNTYNO =? ");

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
				WebAgencyAuthData laData = new WebAgencyAuthData();
				laData.setAgncyAuthIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyAuthIdntyNo"));
				laData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));
				laData.setSubconId(
					caDA.getIntFromDB(lrsQry, "SubconId"));
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
	 * Method to Query RTS_WEB_AGNCY_AUTH, RTS_WEB_AGNCY_AUTH_CFG 
	 *
	 * @param aiIdntyNo 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyAuthAndCfg(int aiIdntyNo)
		throws RTSException
	{
		csMethod = "qryWebAgencyAuthAndCfg";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();
		//defect 11151 
		
		//Added RTS.RTS_OFFICE_IDS to select and deleteindi's  

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "A.AGNCYAUTHIDNTYNO,"
				+ "A.OFCISSUANCENO,"
				+ "AGNCYIDNTYNO, "
				+ "SUBCONID,  "
				+ "KEYENTRYCD,"
				+ "ISSUEINVINDI,"
				+ "EXPPROCSNGCD,"
				+ "EXPPROCSNGMOS,"
				+ "MAXSUBMITDAYS,"
				+ "MAXSUBMITCOUNT, "
				+ "OFCNAME, "
				+ "A.CHNGTIMESTMP  "
				+ " FROM RTS.RTS_WEB_AGNCY_AUTH A, "
				+ " RTS.RTS_WEB_AGNCY_AUTH_CFG B,  "
				+ " RTS.RTS_OFFICE_IDS C "
				+ " WHERE "
				+ " A.AGNCYAUTHIDNTYNO = B.AGNCYAUTHIDNTYNO "
				+ " AND A.DELETEINDI = 0 " 
				+ "	AND B.DELETEINDI = 0 "
				+ " AND A.OFCISSUANCENO = C.OFCISSUANCENO "
				+ " AND AGNCYIDNTYNO =? ");

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

				laData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

				laData.setSubconId(
					caDA.getIntFromDB(lrsQry, "SubconId"));

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
				
				laData.setOfcName(
					caDA.getStringFromDB(lrsQry, "OfcName"));
				
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
	 * Method to Insert into RTS.RTS_WEB_AGNCY_AUTH
	 *
	 * @param  aaData	WebAgencyAuthData
	 * @return WebAgencyAuthData
	 * @throws RTSException 
	 */
	public WebAgencyAuthData insWebAgencyAuth(WebAgencyAuthData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyAuth";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_AUTH("
				+ "OfcIssuanceNo,"
				+ "AgncyIdntyNo,"
				+ "SubconId, "
				+ "DeleteIndi, "
				+ "ChngTimestmp) "
				+ " VALUES ( "
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
						aaData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSubconId())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			ResultSet lrsQry;
			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as AgncyAuthIdntyNo, ChngTimestmp from "
					+ " RTS.RTS_WEB_AGNCY_AUTH";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo =
					caDA.getIntFromDB(lrsQry, "AgncyAuthIdntyNo");
				RTSDate laChngTimeStmp =
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp");
				aaData.setAgncyAuthIdntyNo(liIdntyNo);
				aaData.setChngTimestmp(laChngTimeStmp);
				break;
			}

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
			return aaData;
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
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Purge RTS_WEB_AGNCY_AUTH
	 * 
	 * @param aiPurgeDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeWebAgencyAuth(int aiPurgeDays) throws RTSException
	{
		csMethod = "purgeWebAgencyAuth";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelData =
			"DELETE FROM RTS.RTS_WEB_AGNCY_AUTH "
				+ " WHERE "
				+ " CHNGTIMESTMP "
				+ " < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();

		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeDays)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Method to update RTS.RTS_WEB_AGNCY_AUTH
	 * 
	 * @param  aaData WebAgencyAuthData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgencyAuth(WebAgencyAuthData aaData)
		throws RTSException
	{
		csMethod = "updWebAgencyAuth";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_AUTH "
					+ " SET "
					+ " OfcIssuanceNo = ?, "
					+ " AgncyIdntyNo = ?, "
					+ " SubconId = ?, "
					+ " DeleteIndi = ?, "
					+ " Chngtimestmp = Current Timestamp "
					+ " WHERE "
					+ " AgncyAuthIdntyNo = ?";

			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));

			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));

			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSubconId())));

			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			// DHamilton - Added parameter for where clause.
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthIdntyNo())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");

			return (liNumRows);
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
	 * Method to update RTS.RTS_WEB_AGNCY_AUTH and set the delete indicator
	 * 
	 * @param  aaData WebAgencyAuthData	
	 * @throws RTSException 
	 */
	public void delWebAgencyAuth(WebAgencyAuthData aaData)
		throws RTSException
	{
		csMethod = "delWebAgencyAuth";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_AUTH "
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
 }
