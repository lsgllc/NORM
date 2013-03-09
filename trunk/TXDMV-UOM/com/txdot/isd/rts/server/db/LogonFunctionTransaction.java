package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.LogonFunctionTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * LogonFunctionTransaction.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/15/2001	Added postLogonFunctionTransaction for 
 *							SendTrans
 * K Harrell	10/17/2001	Added TransPostedLANIndi for SendTrans
 *                      	Altered purgeLogonFunctionTransaction for 
 *							TransPostedMfIndi
 * R Hicks		07/12/2002 	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/07/2002	Altered qryMaxLogonFunctionTransaction
 *           		        added "Union"
 *							defect 4581  
 * K Harrell	12/04/2002  Altered qryMaxLogonFunctionTransaction to
 *          				always return data.
 *							defect 4961 
 * K Harrell	01/27/2003	Pass param for purgeLogonFunctionTransaction
 *							defect 5310 
 * K Harrell	06/05/2003 	Add parameters for startofc aiEndOfc
 *                      	add qryLogonFunctionTransaction(int,int)
 * 							defect 6227  
 * K Harrell	11/03/2005	Java 1.4 Work
 *							Remove unnecessary imports
 *							deleted qryMaxLogonFunctionTransaction()
 *							defect 7899 Ver 5.2.3 
 * Ray Rowehl	09/27/2006	Update the insert to also insert the new 
 * 							VersionCd column.
 * 							Update the SendTrans query to only select 
 * 							rows that match the current server MfVersionNo.
 * 							modify insLogonFunctionTransaction(),
 * 								qryLogonFunctionTransaction()
 * 							defect 8959 Ver FallAdminTables
 * Ray Rowehl	10/03/2006	Found a missing comma.
 * 							modify insLogonFunctionTransaction()
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeLogonFunctionTransaction()
 * 							defect 9825 Ver Defect_POS_D   
 * K McKee      09/13/2011  Added qry for EmpId
 * 							added qryLogonFunctionTransactionEmpID()
 * 							defect 10729
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_LOG_FUNC_TRANS 
 * 
 * @version	Defect_POS_D	01/19/2009
 * @author 	Kathy Harrell
 * <br> Creation Date:		09/17/2001 17:11:34  
 */

public class LogonFunctionTransaction
	extends LogonFunctionTransactionData
{
	DatabaseAccess caDA;

	/**
	 * LogonFunctionTransaction constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public LogonFunctionTransaction(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_LOG_FUNC_TRANS
	 * 
	 * @param  aaLogFuncTransData LogonFunctionTransactionData	
	 * @throws RTSException
	 */
	public void insLogonFunctionTransaction(LogonFunctionTransactionData aaLogFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insLogonFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		// defect 8959
		String lsIns =
			"INSERT into RTS.RTS_LOG_FUNC_TRANS ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "WsId,"
				+ "SysDate,"
				+ "SysTime,"
				+ "EmpId,"
				+ "SuccessfulIndi,"
				+ "TransPostedMfIndi,"
				+ "VersionCd"
				+ ") VALUES ("
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";
		// end defect 8959

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSysDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSysTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSuccessfulIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getTransPostedMfIndi())));

			// defect 8959
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getMfVersionNo())));
			// end defect 8959

			Log.write(
				Log.SQL,
				this,
				"insLogonFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insLogonFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insLogonFunctionTransaction - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insLogonFunctionTransaction - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	}

	/**
	 * Method to update a RTS.RTS_LOG_FUNC_TRANS w/ 
	 *  TransPostedMfIndi = 1
	 * 
	 * @param  aaLogFuncTransData LogonFunctionTransactionData	
	 * @throws RTSException  
	 */
	public void postLogonFunctionTransaction(LogonFunctionTransactionData aaLogFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"postLogonFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_LOG_FUNC_TRANS SET "
				+ "TransPostedMfIndi = 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "WsId = ? AND "
				+ "SysDate = ? AND "
				+ "SysTime = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSysDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLogFuncTransData.getSysTime())));
			Log.write(
				Log.SQL,
				this,
				"postLogonFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"postLogonFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"postLogonFunctionTransaction - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"postLogonFunctionTransaction - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	}

	/**
	* Method to Delete from RTS.RTS_LOG_FUNC_TRANS for Purge
	* 
	* @param  aiSysDate  int
	* @return int
	* @throws RTSException 
	*/
	public int purgeLogonFunctionTransaction(int aiSysDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeLogonFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		// Pass parameter for purgeLogonFunctionTransaction
		String lsDel =
			"DELETE FROM RTS.RTS_LOG_FUNC_TRANS WHERE "
				+ "TransPostedMFIndi = 1 and SysDate <= ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSysDate)));
			Log.write(
				Log.SQL,
				this,
				"purgeLogonFunctionTransaction - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeLogonFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeLogonFunctionTransaction - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeLogonFunctionTransaction - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	}

	/**
	 * Method to Query RTS.RTS_LOG_FUNC_TRANS for SendTrans.
	 * 
	 * @param  aiStartOfc	int
	 * @param  aiEndOfc		int
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryLogonFunctionTransaction(
		int aiStartOfc,
		int aiEndOfc)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryLogonFunctionTransaction - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 8959
		// add VersionCd to where clause
		// add aiStartOfc and aiEndOfc
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "WsId,"
				+ "SysDate,"
				+ "SysTime,"
				+ "EmpId,"
				+ "SuccessfulIndi,"
				+ "TransPostedMfIndi, "
				+ "1 as TransPostedLANIndi "
				+ "FROM RTS.RTS_LOG_FUNC_TRANS  "
				+ "WHERE TransPostedMfIndi = 0 "
				+ " and OfcIssuanceNo between "
				+ aiStartOfc
				+ " and "
				+ aiEndOfc
				+ " AND VersionCd = "
				+ SystemProperty.getMainFrameVersion());
		// end defect 8959

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryLogonFunctionTransaction - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryLogonFunctionTransaction - SQL - End");

			while (lrsQry.next())
			{
				LogonFunctionTransactionData laLogonFunctionTransactionData =
					new LogonFunctionTransactionData();
				laLogonFunctionTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laLogonFunctionTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laLogonFunctionTransactionData.setWsId(
					caDA.getIntFromDB(lrsQry, "WsId"));
				laLogonFunctionTransactionData.setSysDate(
					caDA.getIntFromDB(lrsQry, "SysDate"));
				laLogonFunctionTransactionData.setSysTime(
					caDA.getIntFromDB(lrsQry, "SysTime"));
				laLogonFunctionTransactionData.setEmpId(
					caDA.getStringFromDB(lrsQry, "EmpId"));
				laLogonFunctionTransactionData.setSuccessfulIndi(
					caDA.getIntFromDB(lrsQry, "SuccessfulIndi"));
				laLogonFunctionTransactionData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMfIndi"));
				//SendTrans 
				laLogonFunctionTransactionData.setTransPostedLANIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedLANIndi"));
				// Add element to the Vector
				lvRslt.addElement(laLogonFunctionTransactionData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryLogonFunctionTransaction - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryLogonFunctionTransaction - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}
	/**
	 * Method to Query RTS.RTS_LOG_FUNC_TRANS for EmpId.
	 * 
	 * @param  aiOfcIssuanceNo	int
	 * @param  aiWsId		int
	 * @param  aiSysDate
	 * @return String
	 * @throws RTSException 
	 */
	public String  qryLogonFunctionTransactionEmpID(
		int aiOfcIssuanceNo,
		int aiWsId,
		int aiSysDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryLogonFunctionTransactionEmpID - Begin");

		StringBuffer lsQry = new StringBuffer();
		
		Vector lvValues = new Vector();


		String lsEmpId  = "";
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "EmpId "
				+ "FROM RTS.RTS_LOG_FUNC_TRANS  "
				+ "WHERE OfcIssuanceNo = ? "
				+ " AND WsId = ? "
				+ " AND SysDate = ? "
				
				);
		
		lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOfcIssuanceNo)));
		
		lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiWsId)));
		
		lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSysDate)));
		
		lsQry.append("ORDER BY SYSTIME DESC ");


		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryLogonFunctionTransactionEmpID - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryLogonFunctionTransactionEmpID - SQL - End");

			while (lrsQry.next())
			{
				lsEmpId = caDA.getStringFromDB(lrsQry, "EmpId");
				break;
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryLogonFunctionTransactionEmpID - End ");
			return (lsEmpId);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryLogonFunctionTransactionEmpID - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}
		
}