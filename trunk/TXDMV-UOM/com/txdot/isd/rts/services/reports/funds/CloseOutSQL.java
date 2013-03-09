package com.txdot.isd.rts.services.reports.funds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * CloseOutSQL.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * S Johnston	03/11/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify qryCloseOutRange()
 *							defect 7896 Ver 5.2.3
 * K Harrell	04/28/2005	Funds/SQL class review
 * 							deprecate class
 * 							defect 8163 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * CloseOutSQL
 *
 * @version	5.2.3 			04/28/2005
 * @author: Kathy Harrell
 * <br>Creation Date:		10/01/2001 15:22:28
 * @deprecated
 */
public class CloseOutSQL
{
	public DatabaseAccess caDA;
	
	/**
	 * CloseOutSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public CloseOutSQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	* Method to Determine CashWsIds, Date Range for CloseOut
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector
	* @throws RTSException 	
	*/
	public Vector qryCloseOutRange(GeneralSearchData
		aaGeneralSearchData) throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCloseOutRange - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laRSQry;
		// TRANSACTIONS EXIST 
		lsQry.append(
			" SELECT DISTINCT 'Y' AS TransSinceCloseOut,A.CashWsId," +			"CloseOutEndTstmp + 1 MICROSECOND AS CloseOutBegTstmp," +			" CURRENT TIMESTAMP AS CloseOutEndTstmp "
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B "
			+ " WHERE "
			+ " B.OfcIssuanceNo = ? AND " // 1
			+ " B.SubstaId = ? AND " //2
			+ " B.CloseOutReqWsId= ? AND " //3
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId=B.CashWsId AND"
			+ " B.CloseoutReqIndi=1 AND"
			+ " A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp)" +			  " FROM RTS.RTS_CLOSEOUT_HSTRY B"
			+ " WHERE "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId = B.CashWsId) ");
		lvValues.addElement(new DBValue(Types.INTEGER,
			// defect 7896
			// changed call to be statically accessed
			DatabaseAccess.convertToString(
			aaGeneralSearchData.getIntKey1())));
			// end defect 7896
		lvValues.addElement(new DBValue(Types.INTEGER,
			// defect 7896
			// changed call to be statically accessed
			DatabaseAccess.convertToString(
			aaGeneralSearchData.getIntKey2())));
			// end defect 7896
		lvValues.addElement(new DBValue(Types.INTEGER,
			// defect 7896
			// changed call to be statically accessed
			DatabaseAccess.
			convertToString(aaGeneralSearchData.getIntKey3())));
			// end defect 7896
		// Trantimestmp is not null and TransCd != "CLSOUT"
		lsQry.append(" AND EXISTS "	+ " (SELECT * FROM "
			+ " RTS.RTS_TRANS_HDR C, " + " RTS.RTS_TRANS D "
			+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
			+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND " 
			//CloseOutHstry:TransHdr
			+" A.SubstaId      = C.SubstaId      AND "
			+ " A.CashWsId = C.CashWsId AND "
			+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
			//TransHdr:Trans
			+" C.SubstaId      = D.SubstaId      AND "
			+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
			+ " C.TRANSWSID =  D.TRANSWSID AND "
			+ " C.CUSTSEQNO =D.CUSTSEQNO "
			+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp) ");
		// NO TRANSACTIONS EXIST 
		lsQry.append(" UNION "
			+ " SELECT DISTINCT 'N' AS TransSinceCloseOut," 
			+ " A.CashWsId, CloseOutEndTstmp + 1 MICROSECOND AS" 
			+ " CloseOutBegTstmp, "
			+ " CURRENT TIMESTAMP AS CloseOutEndTstmp "
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B "
			+ " WHERE "
			+ " B.CloseoutReqIndi = 1 AND "
			+ " B.OfcIssuanceNo = ? AND "
			+ " B.SubstaId = ? AND "
			+ " B.CloseOutReqWsId = ? AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId = B.SubstaId AND "
			+ " A.CashWsId = B.CashWsId AND "
			+ " A.CloseOutEndTstmp = (SELECT MAX(CloseOutEndTstmp)" +			  " FROM RTS.RTS_CLOSEOUT_HSTRY B "
			+ " WHERE "
			+ " A.OfcIssuanceNo =B.OfcIssuanceNo AND "
			+ " A.SubstaId = B.SubstaId AND "
			+ " A.CashWsId = B.CashWsId) ");
		lvValues.addElement(
			// defect 7896
			// changed call to be statically accessed
			new DBValue(Types.INTEGER,
			 	DatabaseAccess.convertToString(
			 	aaGeneralSearchData.getIntKey1())));
			// end defect 7896
		lvValues.addElement(
			// defect 7896
			// changed call to be statically accessed
			new DBValue(Types.INTEGER,
			  	DatabaseAccess.convertToString(
			  	aaGeneralSearchData.getIntKey2())));
			// end defect 7896
		lvValues.addElement(
			// defect 7896
			// changed call to be statically accessed
			new DBValue(Types.INTEGER,
			  	DatabaseAccess.convertToString(
			  	aaGeneralSearchData.getIntKey3())));
			// end defect 7896
		lsQry.append(" AND NOT EXISTS "
			+ " (SELECT * FROM "
			+ " RTS.RTS_TRANS_HDR C, "
			+ " RTS.RTS_TRANS D "
			+ " WHERE D.TRANSCD <> 'CLSOUT' AND "
			+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND " 
			//CloseOutHstry:TransHdr
			+ " A.SubstaId      = C.SubstaId      AND "
			+ " A.CashWsId = C.CashWsId AND "
			+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND " 
			//TransHdr:Trans
			+ " C.SubstaId      = D.SubstaId      AND "
			+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
			+ " C.TRANSWSID =  D.TRANSWSID AND "
			+ " C.CUSTSEQNO =D.CUSTSEQNO "
			+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp) ");
		// No CloseOutHstry Record Exists 
		lsQry.append(" UNION"
			+ " SELECT 'Y' AS TransSinceCloseOut,A.CashWsId," +			  " MIN(A.TRANSTIMESTMP) AS CloseOutBegTstmp,"
			+ " CURRENT TIMESTAMP AS CloseOutEndTstmp "
			+ " FROM RTS.RTS_TRANS_HDR A, RTS.RTS_CASH_WS_IDS B"
			+ " WHERE "
			+ " B.OfcIssuanceNo = ? AND "
			+ " B.SubstaId = ? AND "
			+ " B.CloseOutReqWsId= ? AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			//JOIN TransHdr:CashWsIds
			+" A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId=B.CashWsId AND "
			+ " B.CloseoutReqIndi=1 AND"
			+ "  NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY" +			  " C WHERE "
			+ " A.OfcIssuanceNo = C.OfcIssuanceNo AND "
			+ " A.SubstaId      = C.SubstaId      AND "
			+ " A.CashWsId=C.CashWsId AND "
			+ " GROUP BY A.CashWsId ORDER BY 1,2");
		lvValues.addElement(new DBValue(Types.INTEGER,
			// defect 7896
			// changed to be statically referenced
			DatabaseAccess.convertToString(
			aaGeneralSearchData.getIntKey1())));
			// end defect 7896
		lvValues.addElement(new DBValue(
				Types.INTEGER,
			// defect 7896
			// changed to be statically referenced
			DatabaseAccess.convertToString(
			aaGeneralSearchData.getIntKey2())));
			// end defect 7896			
		lvValues.addElement(new DBValue(Types.INTEGER,
			// defect 7896
			// changed to be statically referenced			
			DatabaseAccess.convertToString(
			aaGeneralSearchData.getIntKey3())));
			// end defect 7896
		try
		{
			Log.write(Log.SQL, this,
				" - qryCloseOutRange - SQL - Begin");
			laRSQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryCloseOutRange - SQL - End");
			while (laRSQry.next())
			{
				CloseOutStatisticsReportData
					laCloseOutStatisticsReportData =
					new CloseOutStatisticsReportData();
				laCloseOutStatisticsReportData.setTransSinceCloseOut(
					caDA.getStringFromDB(
					laRSQry, "TransSinceCloseOut"));
				laCloseOutStatisticsReportData.setCashWsId(
					caDA.getIntFromDB(laRSQry, "CashWsId"));
				laCloseOutStatisticsReportData.setCloseOutBegTstmp(
					caDA.getRTSDateFromDB(laRSQry, "CloseOutBegTstmp"));
				laCloseOutStatisticsReportData.setCloseOutEndTstmp(
					caDA.getRTSDateFromDB(laRSQry, "CloseOutEndTstmp"));
				// Add element to the Vector
				lvRslt.addElement(laCloseOutStatisticsReportData);
			} //End of While 
			laRSQry.close();
			laRSQry = null;
			Log.write(Log.METHOD, this, " - qryCloseOutRange - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(Log.SQL_EXCP,	this,
				" - qryCloseOutRange - Exception "
				+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
}
