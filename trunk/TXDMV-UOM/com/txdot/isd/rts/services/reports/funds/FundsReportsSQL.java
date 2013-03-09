package com.txdot.isd.rts.services.reports.funds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * FundsReportsSQL.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	08/14/2002  CQU10004600: Add DeleteIndi = 0
 * K Harrell	10/26/2004	Add qualifiers to SQL 
 *							modify qryCloseOutStatistics()
 *							deprecate qryCloseOutStatisticsReport(),
 *							  qryPaymentSummaryReport()
 *							defect 7668 Ver 5.2.2
 * K Harrell	04/14/2005	Delete qryCloseOutStatisticsReport(),
 *							  qryPaymentSummaryReport()
 *							defect 8163 Ver 5.2.3 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify qryCloseOutStatistics()
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/17/2009	Modify for clarity; i.e. Use loop to add 
 * 							 data to parameter Vector vs. 12 addElement()
 * 							modify qryCloseOutStatistics()
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * SQL for CloseOut Statistics report
 *
 * @version	Defect_POS_F	08/17/2009
 * @author: Kathy Harrell
 * <br>Creation Date:		09/25/2001  09:49:48
 */
public class FundsReportsSQL
{
	DatabaseAccess caDA;
	/**
	 * FundsReportsSQL constructor
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public FundsReportsSQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	* Method to determine CashWsIds where transactions have occurred
	* 
	* @param aaFundsData FundsData
	* @return Vector 	 
	* @throws RTSException
	*/
	public Vector qryCloseOutStatistics(
		FundsData aaFundsData) throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCloseOutStatistics - Begin");
		StringBuffer laSBQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		ResultSet laRSQry;

		// defect 4600
		// Add " B.DeleteIndi = 0 AND " 
		// CloseOutHstry exists and Transactions exist 
		laSBQry.append(" SELECT 'Y' AS TransSinceCloseOut,A.CashWsId,"
			+ "CloseOutBegTstmp,CloseOutEndTstmp "
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B"
			+ " WHERE "
			+ " B.DeleteIndi = 0 AND "
			+ " B.OfcIssuanceNo = ? AND " // FROM SYSTEM PARAMETERS
			+ " B.SubstaId = ? AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId=B.CashWsId AND"
			+ " A.CloseOutEndTstmp =(SELECT MAX(CloseOutEndTstmp)"
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY B"
			+ " WHERE "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId = B.CashWsId)"
			+ " AND EXISTS (SELECT * FROM RTS.RTS_TRANS_HDR C, "
			+ " RTS.RTS_TRANS D WHERE D.TRANSCD <> 'CLSOUT' AND "
			+ " C.OfcIssuanceNo = ? AND "
			+ " C.SubstaId = ? AND "
			+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND "
			+ " C.SubstaId      = D.SubstaId      AND "
			+ " A.CashWsId = C.CashWsId AND "
			+ " C.TRANSAMDATE= D.TRANSAMDATE AND "
			+ " C.TRANSWSID = D.TRANSWSID AND "
			+ " C.CUSTSEQNO = D.CUSTSEQNO "
			+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp) ");

		// CloseOutHstry exists and No Transactions exist
		laSBQry.append(" UNION "
			+ " SELECT ' ' AS TransSinceCloseOut, A.CashWsId,"
			+ " CloseOutBegTstmp , "
			+ " CloseOutEndTstmp "
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY A, RTS.RTS_CASH_WS_IDS B "
			+ " WHERE "
			+ " B.DeleteIndi = 0 AND " //4600
			+ " B.OfcIssuanceNo = ? AND "
			+ " B.SubstaId = ? AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId = B.SubstaId AND "
			+ " A.CashWsId = B.CashWsId AND "
			+ " A.CloseOutEndTstmp = (SELECT MAX(CloseOutEndTstmp)"
			+ " FROM RTS.RTS_CLOSEOUT_HSTRY B "
			+ " WHERE "
			+ " A.OfcIssuanceNo =B.OfcIssuanceNo AND "
			+ " A.SubstaId = B.SubstaId AND "
			+ " A.CashWsId = B.CashWsId) "
			+ " AND NOT EXISTS "
			+ " (SELECT * FROM RTS.RTS_TRANS_HDR C, RTS.RTS_TRANS D "
			+ " WHERE D.TRANSCD <> 'CLSOUT'AND "
			+ " C.OfcIssuanceNo = ? AND "
			+ " C.SubstaId = ? AND "
			+ " A.CashWsId = C.CashWsId AND "
			+ " C.OfcIssuanceNo = D.OfcIssuanceNo AND " //Trans:TransHdr
			+ " C.SubstaId      = D.SubstaId      AND "
			+ " C.TRANSAMDATE=D.TRANSAMDATE AND "
			+ " C.TRANSWSID=D.TRANSWSID AND "
			+ " C.CUSTSEQNO=D.CUSTSEQNO"
			+ " AND C.TRANSTIMESTMP > A.CloseOutEndTstmp)");

		// No CloseOutHstry exists and Transactions exist
		// defect 7668
		// add qualifier for ofcissuanceno,substaid on not exists clause
		// in following 2 select stmts  
		laSBQry.append(" UNION"
			+ " SELECT 'Y' AS TransSinceCloseOut,A.CashWsId, '"
			+ FundsConstant.RTS_MIN_DATE
			+ "' as CloseOutBegTstmp, "
			+ "'"
			+ FundsConstant.RTS_MIN_DATE
			+ "' as CloseOutEndTstmp "
			+ " FROM RTS.RTS_TRANS_HDR A, RTS.RTS_CASH_WS_IDS B"
			+ " WHERE "
			+ " B.DeleteIndi = 0 AND " //4600
			+ " B.OfcIssuanceNo = ? AND "
			+ " B.SubstaId = ? AND "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			//JOIN CloseOutHstry:CashWsIds
			+" A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId=B.CashWsId AND "
			+ " NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY C"
			+ " WHERE "
			+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND A.SUBSTAID ="
			+ " C.SUBSTAID "
			+ " AND A.CASHWSID = C.CASHWSID)");

		// No CloseOutHstry exists and No Transactions exist
		laSBQry.append(" UNION"
			+ " SELECT ' ' AS TransSinceCloseOut,B.CashWsId, '"
			+ FundsConstant.RTS_MIN_DATE
			+ "' as CloseOutBegTstmp, "
			+ "'"
			+ FundsConstant.RTS_MIN_DATE
			+ "' as CloseOutEndTstmp "
			+ " FROM RTS.RTS_CASH_WS_IDS B"
			+ " WHERE "
			+ " B.DeleteIndi = 0 AND " //4600
			+ " B.OfcIssuanceNo = ? AND "
			+ " B.SubstaId = ? AND "
			+ " Not Exists (Select * from RTS.RTS_TRANS_HDR A WHERE "
			+ " A.OfcIssuanceNo = B.OfcIssuanceNo AND "
			+ " A.SubstaId      = B.SubstaId      AND "
			+ " A.CashWsId=B.CashWsId)  AND "
			+ " NOT EXISTS (SELECT * FROM RTS.RTS_CLOSEOUT_HSTRY"
			+ " C WHERE "
			+ " B.OFCISSUANCENO = C.OFCISSUANCENO AND B.SUBSTAID ="
			+ " C.SUBSTAID "
			+ " AND B.CASHWSID = C.CASHWSID)"
			+ " ORDER BY 2");
		// end defect 7668 
		try
		{
			// defect 8628 
			for (int i = 0; i< 6; i++)
			{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			}
			// end defect 8628 
			
			Log.write(Log.SQL, this,
				" - qryCloseOutStatistics - SQL - Begin");

			laRSQry = caDA.executeDBQuery(laSBQry.toString(), lvValues);

			Log.write(Log.SQL, this,
				" - qryCloseOutStatistics - SQL - End");
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
			Log.write(Log.METHOD, this,
				" - qryCloseOutStatistics - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
				" - qryCloseOutStatistics - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}
