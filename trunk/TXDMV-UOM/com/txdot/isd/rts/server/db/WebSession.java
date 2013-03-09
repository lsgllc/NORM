package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebSessionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebSession.java
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
 * Ray Rowehl	02/09/2011	Add WebSession to the qry.
 * 							defect 10670 Ver 6.7.0 
 * Ray Rowehl	03/02/2011	Add a delete method.
 *  						add delWebSession()
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/18/2011	Modify qry to also take in the 
 * 							agntsecrtyidntyno.  This means less logic.
 * 							Also added check for older than 15 minutes. 
 * 							modify qryWebSession()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	03/22/2011	Modify query to more properly handle a mix
 * 							of where clause parameters.
 * 							modify qryWebSession()
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	04/01/2011	Modify insert and query to handle ReqIpAddr.
 * 							modify insWebSession(), qryWebSession()
 * 							defect 10670 Ver 6.7.1
 * Kathy McKee  10/12/2011  Modify qryWebSession() remove LASTACCSTIMESTMP
 *                          compare with current time - 15 minutes.
 * 							defect 10729 Ver 6.8.2
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_SESSION 
 *
 * @version	6.9.0			10/12/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebSession
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebSession Constructor
	 * 
	 */
	public WebSession(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to delete RTS.RTS_WEB_SESSION
	 * 
	 * @param  aaData WebSessionData	
	 * @return int
	 * @throws RTSException 
	 */
	public int delWebSession(WebSessionData aaData)
		throws RTSException
	{
		csMethod = "delWebSession";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"DELETE FROM RTS.RTS_WEB_SESSION "
					+ "WHERE "
					+ "WebSessionId = ?";

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getWebSessionId())));

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
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_WEB_SESSION 
	 *
	 * @param  aaData	WebSessionData	
	 * @throws RTSException 
	 */
	public void insWebSession(WebSessionData aaData)
		throws RTSException
	{
		csMethod = "insWebSession";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_SESSION("
				+ "WebSessionId,"
				+ "EDirSessionId,"
				+ "AgntSecrtyIdntyNo,"
				+ "ReqIpAddr,"
				+ "LastAccsTimestmp )"
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
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getWebSessionId())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getEDirSessionId())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntSecrtyIdntyNo())));
						
			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getReqIpAddr())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
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
	 * Method to Query RTS_WEB_SESSION 
	 *
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebSession(WebSessionData aaData) throws RTSException
	{
		csMethod = "qryWebSession";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "WEBSESSIONID,"
				+ "EDIRSESSIONID,"
				+ "LASTACCSTIMESTMP,"
				+ "AGNTSECRTYIDNTYNO, "
				+ "REQIPADDR "
				+ "from RTS.RTS_WEB_SESSION "
				+ "where ");
				

			boolean lbAnd = false;
			
			// If sessionId was passed, use it.
			if (aaData.getWebSessionId() != null
			&& aaData.getWebSessionId().length() > 0)
			{
				lsQry.append("WEBSESSIONID = ? ");
			
				lvValues.addElement(
							new DBValue(
								Types.CHAR,
								DatabaseAccess.convertToString(
									aaData.getWebSessionId())));
				lbAnd = true;
			}

			// if AgntSecrtyIdntyNo is greater than 0, use it.
			if (aaData.getAgntSecrtyIdntyNo() > 0)
			{
				if (lbAnd)
				{
					lsQry.append("and ");
				}
				lsQry.append("AGNTSECRTYIDNTYNO = ? ");
			
				lvValues.addElement(
							new DBValue(
								Types.INTEGER,
								DatabaseAccess.convertToString(
									aaData.getAgntSecrtyIdntyNo())));
				lbAnd = true;
			}
		
//		if (lbAnd)
//		{
//			lsQry.append("and ");
//		}
//		lsQry.append("LASTACCSTIMESTMP > (Current Timestamp - 15 Minutes)");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebSessionData laData = new WebSessionData();

				laData.setWebSessionId(
					caDA.getStringFromDB(lrsQry, "WebSessionId"));

				laData.setEDirSessionId(
					caDA.getStringFromDB(lrsQry, "EDirSessionId"));

				laData.setLastAccsTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "LastAccsTimestmp"));

				laData.setAgntSecrtyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo"));
					
				laData.setReqIpAddr(
					caDA.getStringFromDB(lrsQry, "ReqIpAddr"));

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
	 * Method to update RTS.RTS_WEB_SESSION
	 * 
	 * @param  aaData WebSessionData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebSession(WebSessionData aaData)
		throws RTSException
	{
		csMethod = "updWebSession";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_SESSION Set LastAccsTimestmp = "
					+ "Current Timestamp, " 
					+ "AgntSecrtyIdntyNo = ? "					+ "WHERE "
					+ "WebSessionId = ?";

			lvValues.addElement(
							new DBValue(
								Types.INTEGER,
								DatabaseAccess.convertToString(
									aaData.getAgntSecrtyIdntyNo())));
									
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getWebSessionId())));

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

}
