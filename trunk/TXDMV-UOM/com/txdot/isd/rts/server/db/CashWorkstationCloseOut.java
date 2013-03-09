package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * CashWorkstationCloseOut.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	11/07/2001	Added qryLastCloseOut
 * K Harrell   	01/02/2001	modify qryCashWorkstationCloseOut(),
 *                          qrySinceSubstationSummary
 * K Harrell   	03/18/2002  Added update of Admin_Cache
 * R Hicks  	07/12/2002	Add call to closeLastStatement() after a 
 * 							query
 * K Harrell 	08/06/2002	Assign MIN_RTS_DATE for null 
 * 							CurrStatTimestmp
 *							defect 4579
 * K Harrell  	08/14/2002	Add "DeleteIndi = 0 AND " 
 *							defect 4600 
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	05/05/2005	Implement new SQL functions, Coalesce &
 *							Left Outer Join to remove exposure for 
 *							null CurrStatTimestmp
 *							modify qryCashWorkstationCloseOut() 
 *							defect 8156 Ver 5.2.3
 * K Harrell	12/16/2005	Use RTS_MIN_DATE 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	03/17/2006	Do not update RTS_ADMIN_CACHE on update
 * 							of RTS_CASH_WS_IDS
 * 							modify updCashWorkstationIds()  
 * 							defect 8623 Ver 5.2.3   
 * K Harrell	08/07/2008	Use MIN(CloseoutBegTstmp) v.s. MAX(..)
 * 							when reporting since last SubstationSummary.
 * 							Add'l minor cleanup.
 * 							modify qrySinceSubstationSummary()
 * 							defect 8913 Ver MyPlates_POS 							 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database
 * 
 * This object is used in Funds Reporting to determine:
 *   - date ranges for reporting
 *   - whether transactions exist for the determined date range
 *
 * @version	MyPlates_POS	08/07/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001 11:38:04 
 */

public class CashWorkstationCloseOut
	extends CashWorkstationCloseOutData
{
	DatabaseAccess caDA;
	/**
	 * CashWorkstationCloseOut constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public CashWorkstationCloseOut(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Insert into RTS.RTS_CLOSEOUT_HSTRY
	 * 
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData
	 * @throws RTSException
	 */
	public void insCloseOutHistory(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insCloseOutHistory - Begin");

		Vector lvValues = new Vector();

		String lsBeginTstmp =
			"'"
				+ aaCashWsCloseOutData.getCloseOutBegTstmp().getDB2Date()
				+ "'";
		String lsEndTstmp =
			"'"
				+ aaCashWsCloseOutData.getCloseOutEndTstmp().getDB2Date()
				+ "'";
		String lsIns =
			"INSERT into RTS.RTS_CLOSEOUT_HSTRY ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "CashWsId,"
				+ "CloseOutBegTstmp,"
				+ "CloseOutEndTstmp) "
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ lsBeginTstmp
				+ " ,"
				+ lsEndTstmp
				+ " )";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));

			Log.write(
				Log.SQL,
				this,
				"insCloseOutHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insCloseOutHistory - SQL - End");

			Log.write(Log.METHOD, this, "insCloseOutHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insCloseOutHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	
	/**
	 * Method to determine dates (Current Status /Last Closeout) for 
	 * FUN001/FUN007
	 *
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData 
	 * @return Vector
	 * @throws RTSException 
	 */

	public Vector qryCashWorkstationCloseOut(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{

		Log.write(
			Log.METHOD,
			this,
			" - qryCashWorkstationCloseOut - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		// defect 8156 
		// Use new DB2 functions Coalesce, Left Outer Join 
		// to reduce SQL to 1 call vs. UNION of 3 
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "A.SubstaId,"
				+ "A.CashWsId,"
				+ "Coalesce(CurrStatTimestmp,'"
				+ FundsConstant.RTS_MIN_DATE
				+ "') as CurrStatTimestmp,"
				+ "Coalesce(Max(CloseOutEndTstmp), '"
				+ FundsConstant.RTS_MIN_DATE
				+ "') as CloseOutEndTstmp "
				+ "FROM RTS.RTS_CASH_WS_IDS A "
				+ "LEFT OUTER JOIN RTS.RTS_CLOSEOUT_HSTRY B ON ");

		// defect 4600  
		// Add DeleteIndi = 0 
		lsQry.append(
			"A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ "A.SubstaId = B.SubstaId AND "
				+ "A.CashWsId = B.CashWsId "
				+ "WHERE A.DeleteIndi = 0 AND ");
		// end defect 4600 

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" A.CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			"A.OfcIssuanceNo = ? AND "
				+ "A.SubstaId = ? "
				+ "GROUP BY a.OfcIssuanceno,A.SubstaId, A.CashWsId, A.CurrStatTimestmp "
				+ "ORDER BY 3");

		
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getSubstaId())));

			Log.write(
				Log.SQL,
				this,
				" - qryCashWorkstationCloseOut - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCashWorkstationCloseOut - SQL - End");

			while (lrsQry.next())
			{
				CashWorkstationCloseOutData laCashWorkstationCloseOutData =
					new CashWorkstationCloseOutData();
				laCashWorkstationCloseOutData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCashWorkstationCloseOutData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCashWorkstationCloseOutData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCashWorkstationCloseOutData.setCurrStatTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "CurrStatTimestmp"));
				laCashWorkstationCloseOutData.setCloseOutEndTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutEndTstmp"));
				// Add element to the Vector

				lvRslt.addElement(laCashWorkstationCloseOutData);

			} //End of While
			// end defect 8156 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCashWorkstationCloseOut - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCashWorkstationCloseOut - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to determine CashWsIds, Date Range for CloseOut
	 * 
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCloseOutRange(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCloseOutRange - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		// Transactions Exist and CloseOutHstry Record Exists
		// defect 4600 
		// Add DeleteIndi = 0 
		lsQry.append(
			" SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,1 AS "
				+ " TransSinceCloseOut,A.CashWsId,CloseOutEndTstmp + "
				+ " 1 MICROSECOND AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, "
				+ " RTS.RTS_CASH_WS_IDS B "
				+ " WHERE "
				+ " B.DeleteIndi = 0 AND "
				+ " B.OfcIssuanceNo = ? AND  "
				+ " B.SubstaId = ? AND "
				+ " B.CloseOutReqWsId= ? AND "
				+ " B.CashWsId = ? AND "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId=B.CashWsId AND"
				+ " B.CloseoutReqIndi=1 AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCloseOutReqWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCashWsId())));
		lsQry.append(
			" A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp) FROM "
				+ " RTS.RTS_CLOSEOUT_HSTRY B"
				+ " WHERE "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId = B.CashWsId "
				+ " AND EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp)) ");
		// Transactions Exist and No CloseOutHstry Record Exists 
		lsQry.append(
			" UNION"
				+ " SELECT A.OFCISSUANCENO,A.SUBSTAID,1 AS "
				+ " TransSinceCloseOut,A.CashWsId, MIN(A.TRANSTIMESTMP) "
				+ " AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_TRANS_HDR A, RTS.RTS_CASH_WS_IDS B"
				+ " WHERE "
				+ " B.DeleteIndi = 0 AND "
				+ " B.OfcIssuanceNo = ? AND "
				+ " B.SubstaId = ? AND "
				+ " B.CloseOutReqWsId= ? AND "
				+ " B.CashWsId= ? AND "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId=B.CashWsId AND "
				+ " B.CloseoutReqIndi=1 AND "
				+ " NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY C "
				+ "WHERE "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId=C.CashWsId ) "
				+ " GROUP BY A.OfcIssuanceNo,A.SubstaId,A.CashWsId ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCloseOutReqWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCashWsId())));

		// No Transactions Exist and CloseOutHstry Record Exists 
		lsQry.append(
			" UNION SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID, 0 AS "
				+ " TransSinceCloseOut,A.CashWsId,CloseOutEndTstmp + "
				+ " 1 MICROSECOND AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B "
				+ " WHERE "
				+ " B.DeleteIndi = 0 AND "
				+ " B.OfcIssuanceNo = ? AND "
				+ " B.SubstaId = ? AND "
				+ " B.CloseOutReqWsId= ? AND "
				+ " B.CashWsId= ? AND "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId=B.CashWsId AND"
				+ " B.CloseoutReqIndi=1 AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCloseOutReqWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCashWsId())));

		lsQry.append(
			" A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY B"
				+ " WHERE "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId = B.CashWsId "
				+ " AND NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp)) ");
		// No Transactions Exist and No CloseOutHstry Record Exists 
		lsQry.append(
			" UNION"
				+ " SELECT b.OFCISSUANCENO,b.SUBSTAID,0 AS "
				+ " TransSinceCloseOut,B.CashWsId,'"
				+ FundsConstant.RTS_MIN_DATE
				+ "' AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS B"
				+ " WHERE "
				+ " B.DeleteIndi = 0 and "
				+ " B.OfcIssuanceNo = ? AND "
				+ " B.SubstaId = ? AND "
				+ " B.CloseOutReqWsId= ? AND "
				+ " B.CashWsId= ? AND "
				+ " Not Exists "
				+ " (Select * from RTS.RTS_TRANS_HDR D Where "
				+ " B.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " B.SubstaId      = D.SubstaId      AND "
				+ " B.CashWsId=D.CashWsId  AND "
				+ " D.Transtimestmp is not null ) AND "
				+ " B.CloseoutReqIndi=1 AND "
				+ " NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY C "
				+ " WHERE "
				+ " B.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " B.SubstaId      = C.SubstaId      AND "
				+ " B.CashWsId=C.CashWsId ) "
				+ " ORDER BY 1,2");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCloseOutReqWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getCashWsId())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCloseOutRange - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryCloseOutRange - SQL - End");
			while (lrsQry.next())
			{
				CashWorkstationCloseOutData laCashWorkstationCloseOutData =
					new CashWorkstationCloseOutData();
				laCashWorkstationCloseOutData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCashWorkstationCloseOutData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCashWorkstationCloseOutData.setTransSinceCloseOut(
					caDA.getIntFromDB(lrsQry, "TransSinceCloseOut"));
				laCashWorkstationCloseOutData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCashWorkstationCloseOutData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutBegTstmp"));
				laCashWorkstationCloseOutData.setCloseOutEndTstmp(
					aaCashWsCloseOutData.getCurrentTimestmp());
				// Add element to the Vector
				lvRslt.addElement(laCashWorkstationCloseOutData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryCloseOutRange - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCloseOutRange - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Determine CashWsIds, Date Range for CloseOut
	 *
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCurrentStatus(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCurrentStatus - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// 4600 Add DeleteIndi = 0 
		// Transactions Exist and CurrStatTimestmp is not Null
		lsQry
			.append(
				" SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,1 AS "
				+ " TransSinceCloseOut,A.CashWsId,CurrStatTimestmp + "
				+ " 1 MICROSECOND AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A "
				+ " WHERE "
				+ " A.DeleteIndi = 0 AND "
				+ " A.OfcIssuanceNo = ? AND " // 1
		+" A.SubstaId = ? AND A.CurrStatTimestmp is not null AND ");
		//2
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}
		lsQry.append(
			" EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP > A.CurrStatTimestmp) ");
		// Transactions Exist and CurrStatTimestmp is Null
		lsQry.append(
			" UNION ALL "
				+ " SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,1 AS "
				+ "TransSinceCloseOut,A.CashWsId,'"
				+ FundsConstant.RTS_MIN_DATE
				+ "' AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A "
				+ " WHERE "
				+ " A.DeleteIndi = 0 AND "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND A.CurrStatTimestmp is null AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP is not null) ");
		// No Transactions Exist and CurrStatTimestmp is not Null
		lsQry.append(
			" UNION ALL "
				+ " SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,0 AS "
				+ " TransSinceCloseOut,A.CashWsId,CurrStatTimestmp + 1 "
				+ " MICROSECOND AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A "
				+ " WHERE "
				+ " A.DeleteIndi = 0 AND "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND A.CurrStatTimestmp is not null AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}
		lsQry.append(
			" NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP > A.CurrStatTimestmp) ");
		// No Transactions Exist and CurrStatTimestmp is Null
		lsQry.append(
			" UNION ALL "
				+ " SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,0 AS "
				+ " TransSinceCloseOut,A.CashWsId,'"
				+ FundsConstant.RTS_MIN_DATE
				+ "' AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A "
				+ " WHERE "
				+ " A.DeleteIndi = 0 AND "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND A.CurrStatTimestmp is null AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}
		lsQry.append(
			" NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP is not null) ");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCurrentStatus - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryCurrentStatus - SQL - End");
			while (lrsQry.next())
			{
				CashWorkstationCloseOutData laCashWorkstationCloseOutData =
					new CashWorkstationCloseOutData();
				laCashWorkstationCloseOutData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCashWorkstationCloseOutData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCashWorkstationCloseOutData.setTransSinceCloseOut(
					caDA.getIntFromDB(lrsQry, "TransSinceCloseOut"));
				laCashWorkstationCloseOutData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCashWorkstationCloseOutData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutBegTstmp"));
				// Not from DB 
				laCashWorkstationCloseOutData.setCloseOutEndTstmp(
					aaCashWsCloseOutData.getCurrentTimestmp());
				// Add element to the Vector
				lvRslt.addElement(laCashWorkstationCloseOutData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryCurrentStatus - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCurrentStatus - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Determine CashWsIds, Date Range for Last CloseOut
	 * 
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryLastCloseOut(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryLastCloseOut - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// CloseOutHstry Exists; Transactions Exist
		lsQry.append(
			" SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,"
				+ " 1 AS TransSinceCloseOut,A.CashWsId,CloseOutBegTstmp, "
				+ " CloseOutEndTstmp "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY B"
				+ " WHERE "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId = B.CashWsId ) "
				+ " AND EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP between A.CloseOutBegTstmp"
				+ " and a.CloseOutEndTstmp) ");
		// No CloseOutHstry Exists   (Transactions may or may not exist - we don't care!) 
		lsQry.append(
			" UNION ALL "
				+ " SELECT A.OFCISSUANCENO,A.SUBSTAID,0 AS "
				+ " TransSinceCloseOut,A.CashWsId, Current Timestamp "
				+ " AS CloseOutBegTstmp, Current Timestamp as "
				+ " CloseOutEndTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY C WHERE "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId=C.CashWsId ) "
				+ " GROUP BY A.OfcIssuanceNo,A.SubstaId,A.CashWsId ");

		// No Transactions Exists; CloseOutHstry Record Exists 
		lsQry.append(
			" UNION ALL SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID, "
				+ " 0 AS TransSinceCloseOut,A.CashWsId,CloseOutBegTstmp,"
				+ " CloseOutEndTstmp "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY B"
				+ " WHERE "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ " A.SubstaId      = B.SubstaId      AND "
				+ " A.CashWsId = B.CashWsId ) "
				+ " AND NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP between A.CloseOutBegTstmp "
				+ "and A.CloseOutEndTstmp) ");
		lsQry.append(" ORDER BY 1,2,4 ");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryLastCloseOut - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryLastCloseOut - SQL - End");
			while (lrsQry.next())
			{
				CashWorkstationCloseOutData lCashWorkstationCloseOutData =
					new CashWorkstationCloseOutData();
				lCashWorkstationCloseOutData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				lCashWorkstationCloseOutData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				lCashWorkstationCloseOutData.setTransSinceCloseOut(
					caDA.getIntFromDB(lrsQry, "TransSinceCloseOut"));
				lCashWorkstationCloseOutData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				lCashWorkstationCloseOutData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutBegTstmp"));
				lCashWorkstationCloseOutData.setCloseOutEndTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutEndTstmp"));
				// Add element to the Vector
				lvRslt.addElement(lCashWorkstationCloseOutData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryLastCloseOut - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryLastCloseOut - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Determine CashWsIds, Date Range since last CloseOut
	 *
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData 
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qrySinceLastCloseOut(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySinceLastCloseOut - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// CloseOut Exists and Transactions Exist Since Last CloseOut
		lsQry.append(
			" SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,1 AS "
				+ "TransSinceCloseOut,A.CashWsId,CloseOutEndTstmp + "
				+ "1 MICROSECOND AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}
		lsQry.append(
			" A.CloseOutBegTstmp = (Select MAX(CloseOutBegTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B where"
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid and "
				+ " A.CashWsId = B.CashWsId) "
				+ " AND EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT'AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp) ");

		// CloseOut Exists and No Transactions Exist Since CloseOut
		lsQry.append(
			" union all SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,0 AS TransSinceCloseOut,A.CashWsId,CloseOutEndTstmp + 1 MICROSECOND AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));
		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" A.CloseOutBegTstmp = (Select MAX(CloseOutBegTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B "
				+ " where A.CashWsId = B.CashWsId and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid ) "
				+ " AND NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp) ");

		// No CloseOut Exists; Transactions Exist Since Last CloseOut
		lsQry.append(
			" UNION ALL SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,1 "
				+ "AS TransSinceCloseOut,A.CashWsId,'"
				+ FundsConstant.RTS_MIN_DATE
				+ "' AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A  "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" NOT EXISTS (Select * from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B "
				+ " where A.CashWsId = B.CashWsId and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid) "
				+ " AND EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP is not null) ");

		// No CloseOut Exists; No Transactions Exist
		lsQry.append(
			" UNION ALL SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,0 "
				+ "AS TransSinceCloseOut,A.CashWsId,'"
				+ FundsConstant.RTS_MIN_DATE
				+ "' AS CloseOutBegTstmp "
				+ " FROM RTS.RTS_CASH_WS_IDS A  "
				+ " WHERE "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}

		lsQry.append(
			" NOT EXISTS (Select * from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B "
				+ " where A.CashWsId = B.CashWsId and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid) "
				+ " AND NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP is not null) ");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySinceLastCloseOut - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySinceLastCloseOut - SQL - End");
			while (lrsQry.next())
			{
				CashWorkstationCloseOutData laCashWorkstationCloseOutData =
					new CashWorkstationCloseOutData();
				laCashWorkstationCloseOutData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCashWorkstationCloseOutData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCashWorkstationCloseOutData.setTransSinceCloseOut(
					caDA.getIntFromDB(lrsQry, "TransSinceCloseOut"));
				laCashWorkstationCloseOutData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCashWorkstationCloseOutData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutBegTstmp"));
				// Do Not get from DB 
				laCashWorkstationCloseOutData.setCloseOutEndTstmp(
					aaCashWsCloseOutData.getCurrentTimestmp());
				// Add element to the Vector
				lvRslt.addElement(laCashWorkstationCloseOutData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySinceLastCloseOut - End ");
			return (lvRslt);

		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySinceLastCloseOut - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Determine Date Range for CloseOuts Since 
	 * Substation Summary
	 *
	 * @param  aaCashWsCloseOutData CashWorkstationCloseOutData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qrySinceSubstationSummary(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySinceSubstationSummary - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// CloseOut Exists and Transactions Exist Since Substation Summary
		lsQry.append(
			" SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,"
				+ " 1 AS TransSinceCloseOut,A.CashWsId,A.CloseOutBegTstmp,"
				+ " G.CloseOutEndTstmp"
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CLOSEOUT_HSTRY G "
				+ " WHERE "
				+ " A.OfcIssuanceno = G.OfcIssuanceNo and "
				+ " A.SubstaId = G.SubstaId and "
				+ " A.CashWsId = G.CashWsId and "
				+ " A.SummaryEffDate is null and "
				+ " G.SummaryEffDate is null and "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" a.CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}
		// defect 8913 
		// Use MIN(CloseOutBegTstmp) vs. MAX(CloseOutBegTstmp) 
		lsQry.append(
			" A.CloseOutBegTstmp = (Select MIN(CloseOutBegTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B where "
				+ " B.SummaryEffDate is Null and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid and "
				+ " A.CashWsId = B.CashWsId ) and "
				+ " G.CloseOutEndTstmp = "
				+ " (Select MAX(CloseOutEndTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B where "
				+ " B.SummaryEffDate is Null and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid and "
				+ " A.CashWsId = B.CashWsId ) "
				+ " AND EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT'AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP between A.CloseOutBegTstmp and "
				+ " G.CloseOutEndTstmp) ");
		// end defect 8913 
		
		// CloseOut Exists and No Transactions Exist Since Substation Summary
		lsQry.append(
			" union all SELECT DISTINCT A.OFCISSUANCENO,A.SUBSTAID,"
				+ " 0 AS TransSinceCloseOut,A.CashWsId,A.CloseOutBegTstmp, "
				+ " G.CloseOutEndTstmp"
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CLOSEOUT_HSTRY G "
				+ " WHERE "
				+ " A.OfcIssuanceno = G.OfcIssuanceNo and "
				+ " A.SubstaId = G.SubstaId and "
				+ " A.CashWsId = G.CashWsId and "
				+ " A.SummaryEffDate is null and "
				+ " G.SummaryEffDate is null and "
				+ " A.OfcIssuanceNo = ? AND "
				+ " A.SubstaId = ? AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCashWsCloseOutData.getSubstaId())));

		if (aaCashWsCloseOutData.getCashWsId() != Integer.MIN_VALUE)
		{
			lsQry.append(" a.CashWsId = ? AND ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));
		}
		// defect 8913 
		// Use MIN(CloseOutBegTstmp) vs. MAX(CloseOutBegTstmp)
		lsQry.append(
			" A.CloseOutBegTstmp = (Select MIN(CloseOutBegTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B where "
				+ " B.SummaryEffDate is Null and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid and "
				+ " A.CashWsId = B.CashWsId ) and "
				+ " G.CloseOutEndTstmp = "
				+ " (Select MAX(CloseOutEndTstmp) from "
				+ " RTS.RTS_CLOSEOUT_HSTRY B where "
				+ " B.SummaryEffDate is Null and "
				+ " a.ofcissuanceno =b.ofcissuanceno and "
				+ " a.substaid = b.substaid and "
				+ " A.CashWsId = B.CashWsId ) "
				+ " AND NOT EXISTS "
				+ " (SELECT * FROM "
				+ " RTS.RTS_TRANS_HDR C, "
				+ " RTS.RTS_TRANS D "
				+ " WHERE D.TRANSCD <> 'CLSOUT'AND "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ " A.SubstaId      = C.SubstaId      AND "
				+ " A.CashWsId = C.CashWsId AND "
				+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
				+ " C.SubstaId      = D.SubstaId      AND "
				+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
				+ " C.TRANSWSID =  D.TRANSWSID AND "
				+ " C.CUSTSEQNO =D.CUSTSEQNO "
				+ " AND C.TRANSTIMESTMP between A.CloseOutBegTstmp and "
				+ " G.CloseOutEndTstmp) ");
		// end defect 8913 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySinceSubstationSummary - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySinceSubstationSummary - SQL - End");

			while (lrsQry.next())
			{
				CashWorkstationCloseOutData laCashWorkstationCloseOutData =
					new CashWorkstationCloseOutData();
				laCashWorkstationCloseOutData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCashWorkstationCloseOutData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCashWorkstationCloseOutData.setTransSinceCloseOut(
					caDA.getIntFromDB(lrsQry, "TransSinceCloseOut"));
				laCashWorkstationCloseOutData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCashWorkstationCloseOutData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutBegTstmp"));
				laCashWorkstationCloseOutData.setCloseOutEndTstmp(
					caDA.getRTSDateFromDB(lrsQry, "CloseOutEndTstmp"));
				// Add element to the Vector
				lvRslt.addElement(laCashWorkstationCloseOutData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySinceSubstationSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySinceSubstationSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to update RTS.RTS_CASH_WS_IDS
	 * 
	 * @param  aaCashWorkstationIdsData CashWorkstationCloseOutData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updCashWorkstationIds(CashWorkstationCloseOutData aaCashWsCloseOutData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updCashWorkstationIds - Begin");

		Vector lvValues = new Vector();

		boolean lbResetIndi = false;

		String lsCurrentTimestamp =
			aaCashWsCloseOutData.getCurrStatTimestmp().getDB2Date();

		try
		{
			// Fixed every Update 
			String lsUpd =
				"UPDATE RTS.RTS_CASH_WS_IDS SET CloseOutReqIndi = ?, "
					+ " ChngTimestmp = CURRENT TIMESTAMP ";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCloseOutReqIndi())));
			String lsWhere =
				"WHERE "
					+ "OfcIssuanceNo = ? AND SubstaId = ? AND CashWsId = ? ";

			// For Current Status 
			if (aaCashWsCloseOutData.getCloseOutReqWsId()
				== Integer.MIN_VALUE)
			{
				lsUpd = lsUpd + " ,CloseOutReqWsId = null ";
			}
			else // For CloseOut Request 
				{
				lbResetIndi = true;
				lsUpd = lsUpd + " ,CloseOutReqWsId = ?  ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCashWsCloseOutData
								.getCloseOutReqWsId())));
			}

			if (aaCashWsCloseOutData.getCurrStatTimestmp() != null)
			{
				lsUpd =
					lsUpd
						+ " , CurrStatTimestmp = '"
						+ lsCurrentTimestamp
						+ "'";
			}

			// Where Clause 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsCloseOutData.getCashWsId())));

			if (lbResetIndi)
			{
				//Altered where clause 
				lsWhere =
					lsWhere
						+ " and (CloseOutReqIndi = 0 or "
						+ " (CloseOutReqWsId = ? and "
						+ " CloseOutReqIndi = 1))";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCashWsCloseOutData
								.getCloseOutReqWsId())));
			}
			lsUpd = lsUpd + lsWhere;

			Log.write(
				Log.SQL,
				this,
				"updCashWorkstationIds - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updCashWorkstationIds - SQL - End");
			Log.write(Log.METHOD, this, "updCashWorkstationIds - End");
			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updCashWorkstationIds - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
