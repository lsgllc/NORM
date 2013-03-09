package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;

/*
 * WebAgent.java
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
 * Ray Rowehl	04/29/2011	Rename getEmpPhone() to getPhone().
 * 							Also had to qualify Phone since it is 2 tables.
 * 							modify qryWebAgent(RtsWebAgntWS)
 * 							defect 10718 Ver 6.7.1
 * Ray Rowehl	05/04/2011	Add toUpperCase on UserName for the where
 * 							clause so we will be case insensitive on 
 * 							lookup.
 * 							Also added a where clause for email.
 * 							modify qryWebAgent(RtsWebAgntWS)
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/09/2011	Modify update to also update the name fields.
 * 							modify updWebAgent()
 * 							defect 10718 Ver 6.7.1
 * K McKee		06/09/2011	Added qryWebAgentsForCounty() to retrieve
 *                          the users for county locations
 * 							defect 10718 Ver 6.7.1
 * Ray Rowehl	06/20/2011	Clean up old RTSWebAgntWs stuff.
 * 							modify qryWebAgent(RtsWebAgntWS),
 * 								qryWebAgentsForCounty()
 * 							defect 10718 Ver 6.8.0
 * Kathy McKEe	08/03/2011	Added .trim()
 * 							defect 10718 Ver 6.8.1
 * Kathy McKee	09/01/2011	Added UpdtngAgntIdntyNo
 * 							method updWebAgent()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee	09/23/2011	Added UpdtngAgntIdntyNo
 * 							method insWebAgent()
 * 							defect 10729 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNT 
 *
 * @version	6.8.0			06/20/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 * 
 * 
 */

public class WebAgent
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgent Constructor
	 * 
	 */
	public WebAgent(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS_WEB_AGNT
	 *
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgent(int aiIdntyNo) throws RTSException
	{
		csMethod = "qryWebAgent";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNTIDNTYNO,"
				+ "USERNAME,"
				+ "DMVUSERINDI,"
				+ "FSTNAME,"
				+ "MINAME,"
				+ "LSTNAME,"
				+ "PHONE,"
				+ "EMAIL,"
				+ "INITOFCNO,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP "
				+ "from RTS.RTS_WEB_AGNT "
				+ " WHERE AGNTIDNTYNO = ? ");

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
				WebAgentData laData = new WebAgentData();

				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));

				laData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));

				laData.setDMVUserIndi(
					caDA.getIntFromDB(lrsQry, "DMVUserIndi"));

				laData.setFstName(
					caDA.getStringFromDB(lrsQry, "FstName"));

				laData.setMIName(
					caDA.getStringFromDB(lrsQry, "MIName"));

				laData.setLstName(
					caDA.getStringFromDB(lrsQry, "LstName"));

				laData.setEMail(caDA.getStringFromDB(lrsQry, "EMail"));

				laData.setPhone(caDA.getStringFromDB(lrsQry, "Phone"));

				laData.setInitOfcNo(
					caDA.getIntFromDB(lrsQry, "InitOfcNo"));

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
	 * Method to Query RTS_WEB_AGNT for web service
	 *
	 * @param 	aaRtsWebAgent
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgent(RtsWebAgntWS aaRtsWebAgent)
		throws RTSException
	{
		csMethod = "qryWebAgentWS";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNTIDNTYNO,"
				+ "USERNAME,"
				+ "DMVUSERINDI,"
				+ "FSTNAME,"
				+ "MINAME,"
				+ "LSTNAME,"
				+ "PHONE,"
				+ "EMAIL,"
				+ "INITOFCNO,"
				+ "CHNGTIMESTMP "
				+ "from RTS.RTS_WEB_AGNT "
				+ "WHERE DeleteIndi = 0 ");

		// If agent identity was pased, use it.
		if (aaRtsWebAgent.getAgntIdntyNo() > 0)
		{
			lsQry.append("AND AGNTIDNTYNO = ? ");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getAgntIdntyNo())));
		}

		// if UserName was passed use that
		if (aaRtsWebAgent.getUserName() != null
			&& aaRtsWebAgent.getUserName().trim().length() > 0)
		{
			lsQry.append("AND upper(USERNAME) = ? ");

			// defect 10670
			// add toUpperCase
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getUserName().toUpperCase())));
			// end defect 10670

		}

		// if Last Name was passed use that
		if (aaRtsWebAgent.getLstName() != null
			&& aaRtsWebAgent.getLstName().trim().length() > 0)
		{
			lsQry.append("AND upper(LSTNAME) like ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getLstName().toUpperCase() + "%")));

		}
		
		// if First Name was passed use that
		if (aaRtsWebAgent.getFstName() != null
			&& aaRtsWebAgent.getFstName().trim().length() > 0)
		{
			lsQry.append("AND upper(FSTNAME) like ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getFstName().toUpperCase() + "%")));

		}
		
		// if Last Name was passed use that
		if (aaRtsWebAgent.getMiName() != null
			&& aaRtsWebAgent.getMiName().trim().length() > 0)
		{
			lsQry.append("AND upper(MINAME) = ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getMiName().toUpperCase())));

		}
		
		// if Phone was passed use that
		if (aaRtsWebAgent.getPhone() != null
			&& aaRtsWebAgent.getPhone().trim().length() > 0)
		{
			lsQry.append("AND PHONE = ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getPhone())));

		}
		
		// defect 10670
		// if EMail was passed use that
		if (aaRtsWebAgent.getEMail() != null
			&& aaRtsWebAgent.getEMail().trim().length() > 0)
		{
			lsQry.append("AND EMAIL = ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.getEMail())));

		}
		// end defect 10670

		// only include the dmv indi is it is needed.
		if (aaRtsWebAgent.isCheckDMVIndi())
		{
			// Set DMV Indi.
			lsQry.append("AND DMVUSERINDI = ? ");

			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaRtsWebAgent.isDmvUserIndi())));
		}

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				RtsWebAgntWS laData = new RtsWebAgntWS();

				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));

				laData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));

				laData.setDmvUserIndi(
					caDA.getBooleanFromDB(lrsQry, "DMVUserIndi"));

				laData.setFstName(
					caDA.getStringFromDB(lrsQry, "FstName"));

				laData.setMiName(
					caDA.getStringFromDB(lrsQry, "MIName"));

				laData.setLstName(
					caDA.getStringFromDB(lrsQry, "LstName"));

				laData.setEMail(caDA.getStringFromDB(lrsQry, "EMail"));

				// defect 10718
				laData.setPhone(
					caDA.getStringFromDB(lrsQry, "Phone"));
				// end defect 10718

				laData.setInitOfcNo(
					caDA.getIntFromDB(lrsQry, "InitOfcNo"));

				laData.setChngTimeStmp(
					(caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"))
						.getCalendar());

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
	 * Method to Insert into RTS.RTS_WEB_AGNT 
	 *
	 * @param  aaData	WebAgentData
	 * @return WebAgentData
	 * @throws RTSException 
	 */
	public WebAgentData insWebAgent(WebAgentData aaData)
		throws RTSException
	{

		csMethod = "insWebAgent";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNT("
				+ "USERNAME,"
				+ "DMVUSERINDI,"
				+ "FSTNAME,"
				+ "MINAME,"
				+ "LSTNAME,"
				+ "PHONE,"
				+ "EMAIL,"
				+ "INITOFCNO,"
				+ "DELETEINDI,"
				+ "UPDTNGAGNTIDNTYNO,"
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
				+ "CURRENT TIMESTAMP)";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getUserName())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDMVUserIndi())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getFstName())));

			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getMIName())));

			// 5
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getLstName())));

			// 6 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getPhone())));

			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getEMail())));

			// 8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getInitOfcNo())));

			// 9 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			
			// 10 
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getUpdtngAgntIdntyNo())));
			

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			ResultSet lrsQry;
			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as AgntIdntyNo from "
					+ " RTS.RTS_WEB_AGNT";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo = caDA.getIntFromDB(lrsQry, "AgntIdntyNo");
				aaData.setAgntIdntyNo(liIdntyNo);
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
			Log.write(Log.SQL_EXCP, csMethod, "****  Error occurred during Agent insert -- EDIR agent would have been added and email sent for" + 
					" USERNAME = " + aaData.getUserName() + " FSTNAME = " + aaData.getFstName() +
					" LSTNAME = " + aaData.getLstName() + " EMAIL = " + aaData.getEMail() + 
					" PHONE = " + aaData.getPhone() + " INITOFCNO = " + aaData.getInitOfcNo() +
					" DMVUSERINDI = " + aaData.getDMVUserIndi() + " The Agent was not added to WebAgent  ****");
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to update RTS.RTS_WEB_AGNT
	 * 
	 * @param  aaData WebAgentData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgent(WebAgentData aaData) throws RTSException
	{
		csMethod = "updWebAgent";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNT SET FstName = ?," 
					+ "MiName = ?, "
					+ "LstName = ?, "					
					+ "Phone = ?, "
					+ "EMail = ?,"
					+ "DeleteIndi = ?,"
					+ "UpdtngAgntIdntyNo = ?,"
					+ "ChngTimestmp = CURRENT TIMESTAMP where "
					+ "AgntIdntyNo = ?";
					
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getFstName())));
					
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getMIName())));
					
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getLstName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getPhone())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getEMail())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getUpdtngAgntIdntyNo())));
						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntIdntyNo())));

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
	 * Method to Query RTS_WEB_AGNT -- Users for County locations service
	 *
	 * @param	aaRtsWebAgntWS
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgentsForCounty(RtsWebAgntWS aaRtsWebAgntWS)
		throws RTSException
	{
		csMethod = "qryWebAgentsForCounty";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
		"SELECT "
			+ "E.AGNTIDNTYNO,"
			+ "AGNTSECRTYIDNTYNO,"
			+ "F.AGNCYIDNTYNO,"
			+ "USERNAME,"
			+ "DMVUSERINDI,"
			+ "FSTNAME,"
			+ "MINAME,"
			+ "LSTNAME,"
			+ "NAME1,"
			+ "E.PHONE,"
			+ "E.EMAIL,"
			+ "E.INITOFCNO,"
			+ "E.CHNGTIMESTMP,"
			+ "FROM RTS.RTS_WEB_AGNCY A, "
			+ "RTS.RTS_WEB_AGNCY_AUTH B, "
			+ "RTS.RTS_WEB_AGNCY_AUTH_CFG C, "
			+ "RTS.RTS_OFFICE_IDS D, "
			+ "RTS.RTS_WEB_AGNT E, "
			+ "WHERE A.AGNCYIDNTYNO = B.AGNCYIDNTYNO "
			+ "AND B.AGNCYAUTHIDNTYNO = C.AGNCYAUTHIDNTYNO "
			+ "AND E.AGNTIDNTYNO =  F.AGNTIDNTYNO "
			+ "AND A.DELETEINDI = 0 "
			+ "AND B.DELETEINDI = 0 "
			+ "AND C.DELETEINDI = 0 "
			+ "AND B.OFCISSUANCENO = D.OFCISSUANCENO ");

		// Use the appropriate where clause
		if (aaRtsWebAgntWS.getInitOfcNo() > 0)
		{
			// Use the OfcIssuanceNo
			lsQry.append("AND B.OFCISSUANCENO = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
			aaRtsWebAgntWS.getInitOfcNo())));
		}

		lsQry.append(" ORDER BY F.AGNCYIDNTYNO");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
	
			while (lrsQry.next())
			{
				RtsWebAgntWS laData = new RtsWebAgntWS();
	
				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));
	
				laData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));
	
				laData.setDmvUserIndi(
					caDA.getBooleanFromDB(lrsQry, "DMVUserIndi"));
	
				laData.setFstName(
					caDA.getStringFromDB(lrsQry, "FstName"));
	
				laData.setMiName(
					caDA.getStringFromDB(lrsQry, "MIName"));
	
				laData.setLstName(
					caDA.getStringFromDB(lrsQry, "LstName"));
	
				laData.setEMail(caDA.getStringFromDB(lrsQry, "EMail"));
	
				// defect 10718
				laData.setPhone(
					caDA.getStringFromDB(lrsQry, "Phone"));
				// end defect 10718
	
				laData.setInitOfcNo(
					caDA.getIntFromDB(lrsQry, "InitOfcNo"));
	
				laData.setChngTimeStmp(
					(caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"))
						.getCalendar());
	
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
	}
}
