package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FeeSummaryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * FeeSummary.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/18/2002	Altered to exclude FeeSourceCd = 0
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 * 							deleted insFeeSummary(),updFeeSummary,
 * 							qryFeeSummary()
 *							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_FEE_SUMMARY 
 *
 * @version	5.2.3		06/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:	09/17/2001  17:54:20 
 */

public class FeeSummary extends FeeSummaryData
{
	DatabaseAccess caDA;
	/**
	 * FeeSummary constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public FeeSummary(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	* Method to Insert into RTS.RTS_FEE_SUMMARY for Check Change
	* RTS I - FEESUMI3
	* 
	* @param  aaFeeSummaryData  FeeSummaryData
	* @throws RTSException  
	*/
	public void insFeeSummaryForCheckChange(FeeSummaryData aaFeeSummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insFeeSummaryForCheckChange - Begin");

		String lsSummaryEffDate =
			Integer.toString(aaFeeSummaryData.getSummaryEffDate());

		// defect 4912 
		// Substation Summary Performance
		Vector lvValues = new Vector();

		String lsIns =
			" INSERT INTO RTS.RTS_FEE_SUMMARY "
				+ " ( "
				+ " OFCISSUANCENO, "
				+ " SUBSTAID, "
				+ " SUMMARYEFFDATE, "
				+ " FEESOURCECD, "
				+ " ACCTITMCD, "
				+ " CASHDRAWERINDI, "
				+ " TOTALACCTITMQTY, "
				+ " TOTALACCTITMAMT) "
				+ " SELECT "
				+ " A.OFCISSUANCENO, "
				+ " A.SUBSTAID, "
				+ lsSummaryEffDate
				+ "  ,A.FEESOURCECD, "
				+ " 'CHKCHNG', "
				+ " 0, "
				+ " COUNT(*), "
				+ " SUM(-1*B.CHNGDUE) "
				+ " FROM "
				+ "  RTS. RTS_TRANS_HDR      A, "
				+ "   RTS.RTS_TRANS_PAYMENT B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ? AND  "
				+ " A.SUBSTAID = ? AND  "
				+ " A.SUMMARYEFFDATE = ? AND "
				+ " B.CHNGDUEPYMNTTYPECD = 2 AND "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " A.CUSTSEQNO   = B.CUSTSEQNO "
				+ " GROUP BY A.OFCISSUANCENO, A.SUBSTAID, "
				+ " A.FEESOURCECD ";
		try
		{
			//1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getOfcIssuanceNo())));
			//2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSubstaId())));
			//3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				"insFeeSummaryForCheckChange - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insFeeSummaryForCheckChange - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insFeeSummaryForCheckChange - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insFeeSummaryForCheckChange - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	 * Method to Insert into RTS.RTS_FEE_SUMMARY for VOIDS
	 * RTS I - FEESUMI4
	 *
	 * @param  aaFeeSummaryData  FeeSummaryData	
	 * @throws RTSException
	 */
	public void insFeeSummaryForVoid(FeeSummaryData aaFeeSummaryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insFeeSummaryForVoid - Begin");

		String lsSummaryEffDate =
			Integer.toString(aaFeeSummaryData.getSummaryEffDate());

		// defect 4912
		// Substation Summary Performance 
		Vector lvValues = new Vector();
		String lsIns =
			" INSERT INTO RTS.RTS_FEE_SUMMARY "
				+ " ( "
				+ " OFCISSUANCENO, "
				+ " SUBSTAID, "
				+ " SUMMARYEFFDATE, "
				+ " FEESOURCECD, "
				+ " ACCTITMCD, "
				+ " CASHDRAWERINDI, "
				+ " TOTALACCTITMQTY, "
				+ " TOTALACCTITMAMT) "
				+ " SELECT "
				+ " A.OFCISSUANCENO, "
				+ " A.SUBSTAID, "
				+ lsSummaryEffDate
				+ " ,A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI, "
				+ "  -1*SUM(B.ITMQTY), "
				+ "  SUM(B.ITMPRICE) "
				+ " FROM "
				+ "  RTS. RTS_TRANS_HDR    A, "
				+ "  RTS.RTS_TR_FDS_DETAIL B "
				+ "  WHERE "
				+ "  A.FEESOURCECD <> 0 AND "
				+ " B.ITMQTY > 0 AND "
				+ "   A.OFCISSUANCENO = ? AND  "
				+ "   A.SUBSTAID = ? AND "
				+ "   A.SUMMARYEFFDATE = ? AND "
				+ "   A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ "   A.SUBSTAID  = B.SUBSTAID  AND  "
				+ "   A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "   A.TRANSWSID = B.TRANSWSID AND "
				+ "   A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "   B.TRANSCD IN ('VOID','VOIDNC') "
				+ " GROUP BY "
				+ "  A.OFCISSUANCENO, "
				+ "  A.SUBSTAID, "
				+ "  A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI "
				+ " UNION ALL "
				+ " SELECT "
				+ " A.OFCISSUANCENO, "
				+ " A.SUBSTAID,  "
				+ lsSummaryEffDate
				+ " ,A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI, "
				+ "  -1*COUNT(*), "
				+ "  SUM(B.ITMPRICE) "
				+ " FROM "
				+ "  RTS. RTS_TRANS_HDR    A, "
				+ "  RTS.RTS_TR_FDS_DETAIL B "
				+ " WHERE "
				+ "  A.FEESOURCECD <> 0 AND "
				+ "   ITMQTY = 0 AND "
				+ "   A.OFCISSUANCENO = ? AND  "
				+ "   A.SUBSTAID = ? AND "
				+ "   A.SUMMARYEFFDATE = ? AND "
				+ "   A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ "   A.SUBSTAID  = B.SUBSTAID AND  "
				+ "   A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "   A.TRANSWSID = B.TRANSWSID AND "
				+ "   A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "   B.TRANSCD IN ('VOID','VOIDNC') "
				+ " GROUP BY "
				+ "  A.OFCISSUANCENO, "
				+ "  A.SUBSTAID,  "
				+ "  A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI ";

		try
		{
			//1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getOfcIssuanceNo())));
			//2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSubstaId())));
			//3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSummaryEffDate())));
			//4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getOfcIssuanceNo())));
			//5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSubstaId())));
			//6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				"insFeeSummaryForVoid - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insFeeSummaryForVoid - SQL - End");
			Log.write(Log.METHOD, this, "insFeeSummaryForVoid - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insFeeSummaryForVoid - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	 * Method to Insert into RTS.RTS_FEE_SUMMARY from 
	 * RTS_TR_FDS_DETAIL
	 * 
	 * RTS I - FEESUMI1
	 *
	 * @param  aaFeeSummaryData FeeSummaryData		
	 * @throws RTSException 
	 */
	public void insFeeSummaryFromTrFdsDetail(FeeSummaryData aaFeeSummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insFeeSummaryFromTrFdsDetail - Begin");

		String lsSummaryEffDate =
			Integer.toString(aaFeeSummaryData.getSummaryEffDate());

		Vector lvValues = new Vector();

		// defect 4912
		// Substation Summary Performance
		String lsIns =
			"INSERT INTO RTS.RTS_FEE_SUMMARY "
				+ " ( "
				+ " OFCISSUANCENO, "
				+ " SUBSTAID, "
				+ " SUMMARYEFFDATE, "
				+ " FEESOURCECD, "
				+ " ACCTITMCD, "
				+ " CASHDRAWERINDI, "
				+ " TOTALACCTITMQTY, "
				+ " TOTALACCTITMAMT) "
				+ " SELECT "
				+ " A.OfcIssuanceNo, "
				+ " A.SubstaId, "
				+ lsSummaryEffDate
				+ " ,A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI, "
				+ "  SUM(B.ITMQTY) , "
				+ "  SUM(B.ITMPRICE) "
				+ " FROM "
				+ "  RTS. RTS_TRANS_HDR    A, "
				+ "  RTS.RTS_TR_FDS_DETAIL B, "
				+ "  RTS.RTS_TRANS C "
				+ " WHERE "
				+ " A.FEESOURCECD <> 0 AND "
				+ "   A.OfcIssuanceNo = ? and "
				+ "   A.SubstaId = ? and  "
				+ "   A.SUMMARYEFFDATE = ? AND "
				+ "   A.OfcIssuanceNo = B.OfcIssuanceNo and  "
				+ "   A.SubstaId =  B.SubstaId and  "
				+ "   A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "   A.TRANSWSID = B.TRANSWSID AND "
				+ "   A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "    B.OfcIssuanceNo = C.OfcIssuanceNo and  "
				+ "   B.SubstaId =  C.SubstaId and  "
				+ "   B.TRANSAMDATE = C.TRANSAMDATE AND "
				+ "   B.TRANSWSID = C.TRANSWSID AND "
				+ "   B.CUSTSEQNO = C.CUSTSEQNO AND "
				+ "   B.TRANSTIME = C.TRANSTIME AND "
				+ "  B.ITMQTY > 0 AND "
				+ "  C.TRANSCD NOT IN ('VOID','VOIDNC') "
				+ " GROUP BY "
				+ "  A.OfcIssuanceNo, "
				+ "  A.SubstaId, "
				+ "  A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI "
				+ " UNION ALL "
				+ " SELECT "
				+ " A.OfcIssuanceNo,  "
				+ " A.SubstaId, "
				+ lsSummaryEffDate
				+ ", A.FEESOURCECD, "
				+ " B.ACCTITMCD, "
				+ " B.CASHDRAWERINDI, "
				+ " COUNT(*), "
				+ " SUM(B.ITMPRICE) "
				+ " FROM "
				+ "  RTS. RTS_TRANS_HDR    A, "
				+ "  RTS.RTS_TR_FDS_DETAIL B "
				+ " WHERE "
				+ " A.FEESOURCECD <> 0 AND "
				+ "   A.OfcIssuanceNo = ? and  "
				+ "   A.SubstaId = ? and   "
				+ "   A.SUMMARYEFFDATE = ? AND "
				+ "   A.OfcIssuanceNo = B.OfcIssuanceNo and "
				+ "   A.SubstaId =  B.SubstaId and  "
				+ "   A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "   A.TRANSWSID = B.TRANSWSID AND "
				+ "   A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "   B.ITMQTY = 0 AND "
				+ "   B.TRANSCD NOT IN ('VOID','VOIDNC') "
				+ " GROUP BY "
				+ "  A.OFCISSUANCENO, "
				+ "  A.SUBSTAID,  "
				+ "  A.FEESOURCECD, "
				+ "  B.ACCTITMCD, "
				+ "  B.CASHDRAWERINDI ";
		try
		{
			//1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getOfcIssuanceNo())));
			//2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSubstaId())));
			//3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSummaryEffDate())));
			//4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getOfcIssuanceNo())));
			//5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSubstaId())));
			//6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFeeSummaryData.getSummaryEffDate())));

			Log.write(
				Log.SQL,
				this,
				"insFeeSummaryFromTrFdsDetail - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insFeeSummaryFromTrFdsDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insFeeSummaryFromTrFdsDetail - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insFeeSummaryFromTrFdsDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
} //END OF CLASS
