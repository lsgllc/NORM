package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.data.TitlePackageReportData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * TitlePackageReportSQL.jave 
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	07/09/2002  Added code for "CC Fee"
 * K Harrell   	07/18/2002  Added code for Void (CQU100004473, 4485)
 * K Harrell   	07/23/2002  Added code for Annual Plate (4463)
 * K Harrell	04/06/2004	deprecated qryQuickCounterReportPlates()
 *							qryQuickCounterReportFees(),
 *							qryQuickCounterReportPayment()
 *							Ver 5.2.0
 * S Johnston	03/15/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify qryQuickCounterReportFees(),
 * 							qryQuickCounterReportPayment(),
 * 							qryQuickCounterReportPlates(),
 * 							qryTitlePackageReport(),
 *							defect 7896 Ver 5.2.3 
 * B Hargrove	04/14/2005	Deleted code for Quick Counter.
 * 							delete qryQuickCounterReportFees(),
 * 							qryQuickCounterReportPayment(),
 * 							qryQuickCounterReportPlates()
 *							defect 7955 Ver 5.2.3 
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3
 * K Harrell	10/17/2005	Moved from services.reports.reports
 * 							TitlePackageReportData moved to services.data
 * 							defect 7896 Ver 5.2.3  
 * K Harrell	03/18/2009	Added ETtlCd to select query.
 * 							modify qryTitlePackageReport()
 * 							defect 9976 Ver Defect_POS_E 
 * K Harrell	08/17/2009	delete qryTitlePageReport(TitlePackageReportData) 
 * 							defect 8628 Ver Defect_POS_F  
 * K Harrell	09/20/2010	add TtlExmnIndi
 * 							modify qryTitlePackageReport()
 * 							defect 10013 Ver 6.6.0 
 * K Harrell	10/02/2010	add outer join to include next day void 
 * 							modify qryTitlePackageReport()
 * 							defect 10013 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */
/**
 * Methods to Retrieve Data for Reports w/in Reports Module
 *
 * @version	6.6.0 			10/02/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/24/2001	15:52:10
 */
public class TitlePackageReportSQL
{
	DatabaseAccess caDA;

	/**
	 * TitlePackageReportSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TitlePackageReportSQL(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query Data for Title Package Report
	 * 
	 * @param aaReportSearchData ReportSearchData
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryTitlePackageReport(ReportSearchData aaReportSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTitlePackageReport - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		Vector lvWkIds = aaReportSearchData.getVector();
		StringBuffer lsIds = new StringBuffer(" ( ");
		for (int i = 0; i < lvWkIds.size() - 1; i++)
		{
			lsIds.append((String) lvWkIds.get(i) + ", ");
		}
		lsIds.append((String) lvWkIds.get(lvWkIds.size() - 1) + " ) ");

		ResultSet laResult;

		// defect 10013 
		// add TtlExmnIndi 
		// add outer join logic for next day void 
		lsQry.append(
			"SELECT DISTINCT B.OFCISSUANCENO,A.TRANSAMDATE,"
				+ "A.TRANSWSID,A.TRANSTIME,A.TRANSCD,"
				+ "A.BATCHNO,C.ACCTITMCD,A.VOIDEDTRANSINDI,C.ITMPRICE,"
				+ "A.ETTLCD,TTLEXMNINDI, D.TRANSAMDATE as VOIDTRANSAMDATE "
				+ " FROM "
				+ "RTS.RTS_TRANS_HDR B,RTS.RTS_TR_FDS_DETAIL C, "
				+ " RTS.RTS_MV_FUNC_TRANS A "
				+ "LEFT OUTER JOIN "
				+ "RTS.RTS_TRANS D ON "
				+ "A.OFCISSUANCENO = D.VOIDOFCISSUANCENO AND "
				+ "A.TRANSAMDATE =D.VOIDTRANSAMDATE AND "
				+ "A.TRANSWSID = D.VOIDTRANSWSID AND "
				+ "A.TRANSTIME = D.VOIDTRANSTIME "
				+ " WHERE"
				+ " B.TRANSTIMESTMP IS NOT NULL AND "
				+ "A.TRANSCD IN ( 'TITLE','CORTTL','REJCOR','DTAORD',"
				+ "'DTAORK') AND "
				+ "A.OFCISSUANCENO = ? AND A.SUBSTAID = ? AND"
				+ " A.TRANSAMDATE= ? AND A.TRANSWSID IN "
				+ lsIds
				+ " AND "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE=B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ "A.SUBSTAID = C.SUBSTAID AND "
				+ "A.TRANSAMDATE=C.TRANSAMDATE AND "
				+ "A.TRANSWSID = C.TRANSWSID AND "
				+ "A.CUSTSEQNO = C.CUSTSEQNO AND "
				+ "A.TRANSTIME = C.TRANSTIME AND "
				+ "A.TRANSCD = C.TRANSCD AND "
				+ "C.ACCTITMCD = 'TITLE' "
				+ "UNION "
				+ "SELECT DISTINCT B.OFCISSUANCENO,A.TRANSAMDATE,A.TRANSWSID,"
				+ "A.TRANSTIME,A.TRANSCD,"
				+ "A.BATCHNO,' ' AS ACCTITMCD,A.VOIDEDTRANSINDI,0 AS"
				+ " ITMPRICE, A.ETTLCD, TTLEXMNINDI, D.TRANSAMDATE as VOIDTRANSAMDATE FROM "
				+ "RTS.RTS_TRANS_HDR B, "
				+ " RTS.RTS_MV_FUNC_TRANS A "
				+ "LEFT OUTER JOIN "
				+ "RTS.RTS_TRANS D ON "
				+ "A.OFCISSUANCENO = D.VOIDOFCISSUANCENO AND "
				+ "A.TRANSAMDATE=D.VOIDTRANSAMDATE AND "
				+ "A.TRANSWSID = D.VOIDTRANSWSID AND "
				+ "A.TRANSTIME = D.VOIDTRANSTIME "
				+ "WHERE B.TRANSTIMESTMP IS NOT "
				+ " NULL AND "
				+ "A.TRANSCD IN ( 'TITLE','CORTTL','REJCOR','DTAORD',"
				+ "'DTAORK') AND "
				+ "A.OFCISSUANCENO = ? AND A.SUBSTAID = ? AND "
				+ "A.TRANSAMDATE= ? AND A.TRANSWSID IN "
				+ lsIds
				+ " AND "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE=B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND NOT EXISTS"
				+ " (SELECT * FROM RTS.RTS_TR_FDS_DETAIL C WHERE "
				+ "A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ "A.SUBSTAID = C.SUBSTAID AND "
				+ "A.TRANSAMDATE=C.TRANSAMDATE AND "
				+ "A.TRANSWSID = C.TRANSWSID AND "
				+ "A.CUSTSEQNO = C.CUSTSEQNO AND "
				+ "A.TRANSTIME = C.TRANSTIME AND "
				+ "A.TRANSCD = C.TRANSCD AND "
				+ "C.ACCTITMCD = 'TITLE') ORDER BY 3,6,4 ");
		// end defect 10013 

		try
		{
			for (int i = 0; i < 2; i++)
			{
				// OfcIssuanceNo
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaReportSearchData.getIntKey1())));

				// SubstaId
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaReportSearchData.getIntKey2())));

				// TransAMDate
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							Integer.parseInt(
								aaReportSearchData.getKey2()))));
			}

			Log.write(
				Log.SQL,
				this,
				" - qryTitlePackageReport - SQL - Begin");

			laResult = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.SQL,
				this,
				" - qryTitlePackageReport - SQL - End");

			while (laResult.next())
			{
				TitlePackageReportData laTitlePackageReportData =
					new TitlePackageReportData();

				laTitlePackageReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResult, "OfcIssuanceNo"));
				laTitlePackageReportData.setTransAMDate(
					caDA.getIntFromDB(laResult, "TransAMDate"));
				laTitlePackageReportData.setTransWsId(
					caDA.getIntFromDB(laResult, "TransWsId"));
				laTitlePackageReportData.setTransTime(
					caDA.getIntFromDB(laResult, "TransTime"));
				laTitlePackageReportData.setTransCd(
					caDA.getStringFromDB(laResult, "TransCd"));
				laTitlePackageReportData.setBatchNo(
					caDA.getStringFromDB(laResult, "BatchNo"));
				laTitlePackageReportData.setAcctItmCd(
					caDA.getStringFromDB(laResult, "AcctItmCd"));
				laTitlePackageReportData.setVoidedTransIndi(
					caDA.getIntFromDB(laResult, "VoidedTransIndi"));
				laTitlePackageReportData.setItmPrice(
					caDA.getDollarFromDB(laResult, "ItmPrice"));
				laTitlePackageReportData.setETtlCd(
					caDA.getIntFromDB(laResult, "ETtlCd"));

				// defect 10013 
				laTitlePackageReportData.setTtlExmnIndi(
					caDA.getIntFromDB(laResult, "TtlExmnIndi"));
					
				laTitlePackageReportData.setVoidTransAMDate(	
					caDA.getIntFromDB(laResult, "VoidTransAMDate"));
				// end defect 10013 

				// Add element to the Vector
				lvRslt.addElement(laTitlePackageReportData);
			}
			laResult.close();
			laResult = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTitlePackageReport - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTitlePackageReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTitlePackageReport - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}
}