package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * AdministrationLog.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/17/2001	Purge passes TransAMDate
 *							modify delAdministrationLog(); 
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							AdministrationLogData Object
 * 							modify purgeAdministrationLog() 
 * 							defect 8423 Ver 5.2.3  
 * K Harrell	11/09/2006	Modify purge for consistency; Use <= vs. <
 * 							modify purgeAdministrationLog() 
 * 							defect 9100 Ver Exempts  
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insAdministrationLog()    
 *	 						defect 9847 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeAdministrationLog()
 * 							defect 9825 Ver Defect_POS_D  
 * K Harrell	03/18/2010	Remove unused method.
 * 							delete qryAdministrationLog()
 * 							defect 10239  Ver POS_640   
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ADMIN_LOG 
 *
 * @version	POS_640			03/18/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001 11:45:49 
 */
public class AdministrationLog extends AdministrationLogData
{
	DatabaseAccess caDA;

	/**
	 * AdministrationLog constructor comment.
	 * 
	 * @param  aaDA 	DatabaseAccess
	 * @throws RTSException 
	 */
	public AdministrationLog(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_ADMIN_LOG
	 * 
	 * @param  aaAdminLogData	AdministrationLogData	
	 * @throws RTSException 
	 */
	public void insAdministrationLog(AdministrationLogData aaAdminLogData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insAdministrationLog - Begin");

		Vector lvValues = new Vector();

		// defect 9847 
		if (aaAdminLogData.getTransEmpId() == null
			|| aaAdminLogData.getTransEmpId().trim().length() == 0)
		{
			aaAdminLogData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		String lsIns =
			"INSERT into RTS.RTS_ADMIN_LOG ("
				+ "OfcIssuanceNo,"
				+ "SubStaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "TransTime,"
				+ "TransEmpId,"
				+ "Entity,"
				+ "Action,"
				+ "EntityValue,"
				+ "TransTimestmp ) VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " CURRENT TIMESTAMP )";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdminLogData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdminLogData.getSubStaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdminLogData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdminLogData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAdminLogData.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaAdminLogData.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaAdminLogData.getEntity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaAdminLogData.getAction())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaAdminLogData.getEntityValue())));
			Log.write(
				Log.SQL,
				this,
				"insAdministrationLog - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insAdministrationLog - SQL - End");
			Log.write(Log.METHOD, this, "insAdministrationLog - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insAdministrationLog - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	* Method to Delete from RTS.RTS_ADMIN_LOG
	* Note: Delete only in Purge - older than 90 days
	*
	* @param  aiPurgeDate int 
	* @return int 	
	* @throws RTSException 
	*/
	public int purgeAdministrationLog(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeAdministrationLog - Begin");

		Vector lvValues = new Vector();

		// defect 9011
		// use "<=" vs. "<" 
		String lsDel =
			"DELETE FROM RTS.RTS_ADMIN_LOG WHERE TRANSAMDATE <= ? ";
		// end defect 9011			

		// defect 8423
		// use passed int 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeAMDate)));
		// end defect 8423

		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeAdministrationLog - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

			Log.write(
				Log.SQL,
				this,
				"purgeAdministrationLog - SQL - End");
			Log.write(Log.METHOD, this, "purgeAdministrationLog - End");
			return liNumRows;
			// end defect 9825   
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeAdministrationLog - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	// defect 10239 
	//	/**
	//	 * Method to Query RTS.RTS_ADMIN_LOG
	//	 * 
	//	 * @param  aaAdminLogData	AdministrationLogData 
	//	 * @return Vector 	
	//	 * @throws RTSException
	//	 */
	//	public Vector qryAdministrationLog(AdministrationLogData aaAdminLogData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - qryAdministrationLog - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		Vector lvRslt = new Vector();
	//
	//		Vector lvValues = new Vector();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ "OfcIssuanceNo,"
	//				+ "SubStaId,"
	//				+ "TransAMDate,"
	//				+ "TransWsId,"
	//				+ "TransTime,"
	//				+ "TransEmpId,"
	//				+ "Entity,"
	//				+ "Action,"
	//				+ "EntityValue,"
	//				+ "TransTimestmp "
	//				+ "FROM RTS.RTS_ADMIN_LOG "
	//				+ "WHERE OFCISSUANCENO = ? AND SUBSTAID = ? "
	//				+ "ORDER BY TransAMDate,TransWsId,TransTime");
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaAdminLogData.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaAdminLogData.getSubStaId())));
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryAdministrationLog - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryAdministrationLog - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				AdministrationLogData laAdminLogData =
	//					new AdministrationLogData();
	//				laAdminLogData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	//				laAdminLogData.setSubStaId(
	//					caDA.getIntFromDB(lrsQry, "SubStaId"));
	//				laAdminLogData.setTransAMDate(
	//					caDA.getIntFromDB(lrsQry, "TransAMDate"));
	//				laAdminLogData.setTransWsId(
	//					caDA.getIntFromDB(lrsQry, "TransWsId"));
	//				laAdminLogData.setTransTime(
	//					caDA.getIntFromDB(lrsQry, "TransTime"));
	//				laAdminLogData.setTransEmpId(
	//					caDA.getStringFromDB(lrsQry, "TransEmpId"));
	//				laAdminLogData.setEntity(
	//					caDA.getStringFromDB(lrsQry, "Entity"));
	//				laAdminLogData.setAction(
	//					caDA.getStringFromDB(lrsQry, "Action"));
	//				laAdminLogData.setEntityValue(
	//					caDA.getStringFromDB(lrsQry, "EntityValue"));
	//				laAdminLogData.setTransTimestmp(
	//					caDA.getRTSDateFromDB(lrsQry, "TransTimestmp"));
	//				// Add element to the Vector
	//				lvRslt.addElement(laAdminLogData);
	//			} //End of While
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryAdministrationLog - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryAdministrationLog - Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 
} //END OF CLASS
