package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.CloseOutHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * CloseOutHistory.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/15/2001  Added where "summaryeffdate is null" to 
 *							qryCloseOutHistory
 *                          (Rerun Substation Summary)
 * K Harrell	10/19/2001  Renamed delCloseOutHistory to 
 *							purgeCloseOutHistory
 * K Harrell	12/13/2001  Added qryCloseOutHstryMaxDate()
 * K Harrell	01/03/2002  Altered updCloseOutHstry for timestamp
 * K Harrell	01/04/2002  Altered qryCloseOutHistoryMaxDate if no
 *							records exist
 * K Harrell	03/23/2002  Altered qryCloseOutHistoryMaxDate 
 *							- last time!
 * K Harrell	03/26/2002  Added qryCloseOutHistoryMaxCloseDate 
 * MAbs			04/18/2002	Fixing error in County Wide Report 
 *							defect 3455
 * R Hicks 		07/12/2002	Add call to closeLastStatement() after a
 *							query
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							CloseOutHistoryData Object
 * 							modify purgeCloseOutHistory() 
 * 							defect 8423 Ver 5.2.3  
 * K Harrell	09/15/2008	Purge records where records no longer exist
 * 							for cashdrawer in RTS_CASH_WS_IDS.  Return 
 * 							number of records deleted.  
 * 							modify purgeCloseOutHistory()   
 * 							defect 8683 Ver Defect_POS_B
 * K Harrell	03/19/2010	delete unused method.
 * 							delete insCloseOutHistory() 
 * 							defect 10239 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
* This class contains methods to access RTS_CLOSEOUT_HSTRY
* 
* @version	POS_640			03/19/2010
* @author	Kathy Harrell
* <br>Creation Date:		09/24/2001 16:27:55 
*/

public class CloseOutHistory extends CloseOutHistoryData
{
	DatabaseAccess caDA;

	/**
	 * CloseOutHistory constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public CloseOutHistory(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	// defect 10239 
	//	/**
	//	 * Method to Insert into RTS.RTS_CLOSEOUT_HSTRY
	//	 * 
	//	 * @param  aaCloseOutHstryData CloseOutHistoryData	
	//	 * @throws RTSException
	//	 */
	//	public void insCloseOutHistory(CloseOutHistoryData aaCloseOutHstryData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, "insCloseOutHistory - Begin");
	//
	//		Vector lvValues = new Vector();
	//
	//		String lsIns =
	//			"INSERT into RTS.RTS_CLOSEOUT_HSTRY ("
	//				+ "OfcIssuanceNo,"
	//				+ "SubstaId,"
	//				+ "CashWsId,"
	//				+ "CloseOutBegTstmp,"
	//				+ "CloseOutEndTstmp ) "
	//				+ " VALUES ( "
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ? )";
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaCloseOutHstryData.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaCloseOutHstryData.getSubstaId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaCloseOutHstryData.getCashWsId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.TIMESTAMP,
	//					DatabaseAccess.convertToString(
	//						aaCloseOutHstryData.getCloseOutBegTstmp())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.TIMESTAMP,
	//					DatabaseAccess.convertToString(
	//						aaCloseOutHstryData.getCloseOutEndTstmp())));
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"insCloseOutHistory - SQL - Begin");
	//			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
	//			Log.write(Log.SQL, this, "insCloseOutHistory - SQL - End");
	//			Log.write(Log.METHOD, this, "insCloseOutHistory - End");
	//
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"insCloseOutHistory - Exception - "
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	} //END OF INSERT METHOD
	// end defect 10239 

	/**
	 * Method to Delete from RTS.RTS_CLOSEOUT_HSTRY
	 * 
	 * @param  aiPurgeAMDate int	
	 * @throws RTSException
	 */
	public int purgeCloseOutHistory(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeCloseOutHistory - Begin");

		// defect 8683
		Vector lvValues1 = new Vector();
		Vector lvValues2 = new Vector();

		String lsDel1 =
			"DELETE FROM RTS.RTS_CLOSEOUT_HSTRY A WHERE "
				+ " NOT EXISTS (SELECT * FROM RTS.RTS_CASH_WS_IDS B "
				+ " where A.OfcissuanceNo = B.OfcIssuanceNo and "
				+ " A.SubstaId = B.SubstaId and A.CashWsId = B.CashWsId)";

		String lsDel2 =
			"DELETE FROM RTS.RTS_CLOSEOUT_HSTRY A WHERE "
				+ " A.SUMMARYEFFDATE <= ?  and CloseOutEndTstmp < "
				+ " (Select max(CloseOutEndTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B "
				+ " where A.OfcissuanceNo = B.OfcIssuanceNo and "
				+ " A.SubstaId = B.SubstaId and A.CashWsId = B.CashWsId)";

		try
		{
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log.write(
				Log.SQL,
				this,
				"purgeCloseOutHistory - SQL - Begin");

			int liNumRows1 =
				caDA.executeDBInsertUpdateDelete(lsDel1, lvValues1);

			int liNumRows2 =
				caDA.executeDBInsertUpdateDelete(lsDel2, lvValues2);

			Log.write(
				Log.SQL,
				this,
				"purgeCloseOutHistory - SQL - End");
			Log.write(Log.METHOD, this, "purgeCloseOutHistory - End");

			return liNumRows1 + liNumRows2;
			// end defect 8683
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeCloseOutHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_CLOSEOUT_HSTRY for Rerun Substation 
	 * Summary
	 * 
	 * @param  aaCloseOutHstryData  CloseOutHistoryData 	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCloseOutHistory(CloseOutHistoryData aaCloseOutHstryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCloseOutHistory - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "CashWsId,"
				+ "CloseOutBegTstmp,"
				+ "CloseOutEndTstmp,"
				+ "SummaryEffDate "
				+ "FROM RTS.RTS_CLOSEOUT_HSTRY where OfcIssuanceNo = ? "
				+ "and SubstaId = ? and SummaryEffDate is null "
				+ "order by 3,4 ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));

			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutHistory - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutHistory - SQL - End");

			while (lrsQry.next())
			{
				CloseOutHistoryData laCloseOutHstryData =
					new CloseOutHistoryData();
				laCloseOutHstryData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCloseOutHstryData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCloseOutHstryData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCloseOutHstryData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutBegTstmp"));
				laCloseOutHstryData.setCloseOutEndTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutEndTstmp"));
				laCloseOutHstryData.setSummaryEffDate(
					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
				// Add element to the Vector
				lvRslt.addElement(laCloseOutHstryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryCloseOutHistory - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCloseOutHistory - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

	} //END OF QUERY METHOD

	/**
	 * Method to determine MAX(DATE(CLOSEOUTENDTSTMP))... from 
	 * RTS.RTS_CloseOutHistory
	 *
	 * @param  aaCloseOutHstryData  CloseOutHistoryData	
	 * @return int 
	 * @throws RTSException 
	 */
	public int qryCloseOutHistoryMaxCloseDate(CloseOutHistoryData aaCloseOutHstryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryCloseOutHistoryMaxCloseDate - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liSummaryEffDate = 0;

		lsQry.append(
			"SELECT "
				+ " DAYS(DATE(CLOSEOUTENDTSTMP)) - 693596 AS "
				+ " SummaryEffDate "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A where "
				+ " OfcIssuanceNo = ? and SubstaId = ? and "
				+ " SummaryEffDate is null and "
				+ " A.CLOSEOUTENDTSTMP = "
				+ " (SELECT MAX(CLOSEOUTENDTSTMP) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY B "
				+ " WHERE A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID )"
				+ " UNION ALL "
				+ " SELECT DAYS(CURRENT DATE) - 693597 as "
				+ " SummaryEffDate "
				+ " from RTS.RTS_MISC  "
				+ " WHERE OFCISSUANCENO = ? AND SUBSTAID = ? AND "
				+ " NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY "
				+ " where OfcIssuanceNo = ? and SubstaId = ? and "
				+ " SummaryEffDate is null) ");
		try
		{
			//SET 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));
			//SET 2      
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));
			//SET 3      
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));

			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutHistoryMaxCloseDate - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutHistoryMaxCloseDate - SQL - End");

			while (lrsQry.next())
			{
				liSummaryEffDate =
					caDA.getIntFromDB(lrsQry, "SummaryEffDate");
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCloseOutHistoryMaxCloseDate - End ");
			return (liSummaryEffDate);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCloseOutHistoryMaxCloseDate - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query MAX(DATE(CLOSEOUTENDTSTMP) from 
	 * RTS.RTS_CloseOutHistory
	 * 
	 * @param  aaCloseOutHstryData CloseOutHistoryData	
	 * @return int
	 * @throws RTSException
	 */
	public int qryCloseOutHistoryMaxDate(CloseOutHistoryData aaCloseOutHstryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryCloseOutHistoryMaxDate - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liSummaryEffDate = 0;

		lsQry.append(
			"SELECT DISTINCT  "
				+ " SUMMARYEFFDATE "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A where "
				+ " OfcIssuanceNo = ? and "
				+ " SubstaId = ? and SummaryEffDate is not null and "
				+ " A.SUMMARYEFFDATE = (SELECT MAX(SUMMARYEFFDATE) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY B "
				+ " WHERE A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID )"
				+ " UNION ALL "
				+ " SELECT DAYS(CURRENT DATE) - 693597 as SummaryEffDate"
				+ " from RTS.RTS_MISC  "
				+ " WHERE OFCISSUANCENO = ? AND SUBSTAID = ? AND "
				+ " NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY "
				+ " where OfcIssuanceNo = ? and SubstaId = ? and "
				+ " SummaryEffDate is not null) ");
		try
		{
			//SET 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));
			//SET 2      
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));
			//SET 3      
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));

			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutHistoryMaxDate - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutHistoryMaxDate - SQL - End");

			while (lrsQry.next())
			{
				liSummaryEffDate =
					caDA.getIntFromDB(lrsQry, "SummaryEffDate");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCloseOutHistoryMaxDate - End ");
			return (liSummaryEffDate);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCloseOutHistoryMaxDate - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update a CloseOutHistory
	 *
	 * @param  aaCloseOutHstryData CloseOutHistoryData	
	 * @throws RTSException
	 */
	public void updCloseOutHistory(CloseOutHistoryData aaCloseOutHstryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updCloseOutHistory - Begin");

		Vector lvValues = new Vector();

		String lsBeginTstmp =
			"'"
				+ aaCloseOutHstryData.getCloseOutBegTstmp().getDB2Date()
				+ "'";

		String lsEndTstmp =
			"'"
				+ aaCloseOutHstryData.getCloseOutEndTstmp().getDB2Date()
				+ "'";

		String lsUpd =
			"UPDATE RTS.RTS_CLOSEOUT_HSTRY SET "
				+ "SummaryEffDate = ? "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "CashWsId = ? AND "
				+ "CloseOutBegTstmp = "
				+ lsBeginTstmp
				+ " AND CloseOutEndTstmp = "
				+ lsEndTstmp;
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSummaryEffDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCloseOutHstryData.getCashWsId())));

			Log.write(
				Log.SQL,
				this,
				"updCloseOutHistory - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, "updCloseOutHistory - SQL - End");
			Log.write(Log.METHOD, this, "updCloseOutHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updCloseOutHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
