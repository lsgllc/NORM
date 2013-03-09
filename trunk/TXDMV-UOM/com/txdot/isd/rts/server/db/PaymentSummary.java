package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PaymentSummaryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * PaymentSummary.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	10/23/2001	Removed delPaymentSummary
 *           				Added insPaymentSummary,updPaymentSummary
 * K Harrell	01/04/2002	Updated for Substation Summary
 * K Harrell	05/01/2002	Altered for CashChange,Void,Update Payment
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	10/18/2002	Substation Summary Performance
 *							defect 4912 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3  
 * K Harrell	03/19/2010	Remove unused method. 
 * 							delete qryPaymentSummary()
 * 							defect 10239 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PYMNT_SUMMARY 
 * 
 * @version	POS_640		03/19/2010
 * @author 	Kathy Harrell
 * <br> Creation Date:	09/19/2001 10:55:04
 */

public class PaymentSummary extends PaymentSummaryData
{
	DatabaseAccess caDA;

	/**
	 * PaymentSummary constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PaymentSummary(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_PYMNT_SUMMARY - in Substation 
	 * Summary
	 *
	 * @param aaPaymentSummaryData	PaymentSummaryData
	 * @throws RTSException
	 */
	public void insPaymentSummary(PaymentSummaryData aaPaymentSummaryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insPaymentSummary - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_PYMNT_SUMMARY ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "SummaryEffDate,"
				+ "FeeSourceCd,"
				+ "PymntTypeCd,"
				+ "PymntTypeQty,"
				+ "TotalPymntTypeAmt ) VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getSummaryEffDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getFeeSourceCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getPymntTypeCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getPymntTypeQty())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getTotalPymntTypeAmt())));
			Log.write(Log.SQL, this, "insPaymentSummary - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insPaymentSummary - SQL - End");
			Log.write(Log.METHOD, this, "insPaymentSummary - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insPaymentSummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Insert into RTS.RTS_PYMNT_SUMMARY 
	 * in Substation Summary
	 * 
	 * @param  aaPaymentSummaryData	PaymentSummaryData
	 * @throws RTSException
	 */
	public void insPaymentSummaryFromTransPayment(PaymentSummaryData aaPaymentSummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insPaymentSummaryFromTransPayment - Begin");

		Vector lvValues = new Vector();

		String csSummaryEffDate =
			Integer.toString(aaPaymentSummaryData.getSummaryEffDate());

		// KPH (2002.10.18): CQU10004912 Substation Summary Performance

		String lsIns =
			" INSERT INTO RTS.RTS_PYMNT_SUMMARY "
				+ " ( "
				+ " OFCISSUANCENO, "
				+ " SubstaId, "
				+ " SUMMARYEFFDATE, "
				+ " PYMNTTYPECD, "
				+ " FEESOURCECD, "
				+ " PYMNTTYPEQTY, "
				+ " TOTALPYMNTTYPEAMT) "
				+ " SELECT "
				+ "    A.OFCISSUANCENO, "
				+ "    A.SubstaId, "
				+ csSummaryEffDate
				+ "    ,B.PYMNTTYPECD, "
				+ "    A.FEESOURCECD, "
				+ "   COUNT(*), "
				+ "    SUM(B.PYMNTTYPEAMT) "
				+ "   FROM "
				+ "    RTS.RTS_TRANS_HDR      A, "
				+ "    RTS.RTS_TRANS_PAYMENT  B "
				+ " WHERE "
				+ "    A.OFCISSUANCENO = ?  AND  "
				+ "    A.SubstaId = ?       AND  "
				+ "    A.SummaryEffDate = ? AND  "
				+ "    A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ "    A.SubstaId = B.SubstaId AND  "
				+ "    A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "    A.TRANSWSID = B.TRANSWSID AND "
				+ "    A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " NOT EXISTS "
				+ " (SELECT * FROM "
				+ "    RTS.RTS_TRANS C WHERE "
				+ "    A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ "    A.SubstaId = C.SubstaId AND  "
				+ "    A.TRANSAMDATE = C.TRANSAMDATE AND "
				+ "    A.TRANSWSID = C.TRANSWSID AND "
				+ "    A.CUSTSEQNO = C.CUSTSEQNO AND "
				+ " C.TRANSCD  IN ('VOID','VOIDNC')) "
				+ " GROUP BY A.OFCISSUANCENO,A.SubstaId,"
				+ "B.PYMNTTYPECD,A.FEESOURCECD";
		try
		{
			// 1     
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getSubstaId())));
			// 3 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				"insPaymentSummaryFromTransPayment - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insPaymentSummaryFromTransPayment - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insPaymentSummaryFromTransPayment - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insPaymentSummaryFromTransPayment - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Query Change Due for Substation Summary
	 * RTS I - CHGSUMC1
	 * RTS II - Select as negative so that will add in 
	 * updPaymentSummary
	 * 
	 * @param  aaPaymentSummaryData PaymentSummaryData	
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryCashChangeForPaymentSummary(PaymentSummaryData aaPaymentSummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryCashChangeForPaymentSummary - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		int liSummaryEffDate = aaPaymentSummaryData.getSummaryEffDate();

		// KPH (2002.10.18): CQU10004912 Substation Summary 
		// Performance
		ResultSet lrsQry;

		lsQry.append(
			" SELECT "
				+ " A.OFCISSUANCENO, "
				+ " A.SubstaId, "
				+ liSummaryEffDate
				+ " as SummaryEffDate, "
				+ " 1 as PymntTypeCd, "
				+ " A.FEESOURCECD, "
				+ " 0 as PymntTypeQty,  "
				+ " -1*SUM(B.CHNGDUE) AS TotalPymntTypeAmt  "
				+ " FROM "
				+ " RTS.RTS_TRANS_HDR A, "
				+ " RTS.RTS_TRANS_PAYMENT  B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ?  AND  "
				+ " A.SubstaId = ?       AND  "
				+ " A.SummaryEffDate = ? AND  "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SubstaId = B.SubstaId AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " B.CHNGDUEPYMNTTYPECD = 1 "
				+ " GROUP BY A.OFCISSUANCENO,A.SubstaId,"
				+ " A.FEESOURCECD ");
		// 1 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaPaymentSummaryData.getOfcIssuanceNo())));
		// 2
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaPaymentSummaryData.getSubstaId())));
		// 3 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaPaymentSummaryData.getSummaryEffDate())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCashChangeForPaymentSummary - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCashChangeForPaymentSummary - SQL - End");

			while (lrsQry.next())
			{
				PaymentSummaryData laPaymentSummaryData =
					new PaymentSummaryData();
				laPaymentSummaryData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laPaymentSummaryData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laPaymentSummaryData.setSummaryEffDate(
					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
				laPaymentSummaryData.setFeeSourceCd(
					caDA.getIntFromDB(lrsQry, "FeeSourceCd"));
				laPaymentSummaryData.setPymntTypeCd(
					caDA.getIntFromDB(lrsQry, "PymntTypeCd"));
				laPaymentSummaryData.setPymntTypeQty(
					caDA.getIntFromDB(lrsQry, "PymntTypeQty"));
				laPaymentSummaryData.setTotalPymntTypeAmt(
					caDA.getDollarFromDB(lrsQry, "TotalPymntTypeAmt"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentSummaryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCashChangeForPaymentSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCashChangeForPaymentSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	// defect 10239 
	//	/**
	//	 * Method to Query RTS.RTS_PYMNT_SUMMARY
	//	 * 
	//	 * @param  aaPaymentSummaryData	PaymentSummaryData
	//	 * @return Vector
	//	 * @throws RTSException
	//	 */
	//	public Vector qryPaymentSummary(PaymentSummaryData aaPaymentSummaryData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - qryPaymentSummary - Begin");
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
	//				+ "SubstaId,"
	//				+ "SummaryEffDate,"
	//				+ "FeeSourceCd,"
	//				+ "PymntTypeCd,"
	//				+ "PymntTypeQty,"
	//				+ "TotalPymntTypeAmt "
	//				+ "FROM RTS.RTS_PYMNT_SUMMARY "
	//				+ "WHERE OFCISSUANCENO = ? AND "
	//				+ "SubstaId = ? ");
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaPaymentSummaryData.getOfcIssuanceNo())));
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaPaymentSummaryData.getSubstaId())));
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryPaymentSummary - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryPaymentSummary - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				PaymentSummaryData laPaymentSummaryData =
	//					new PaymentSummaryData();
	//				laPaymentSummaryData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	//				laPaymentSummaryData.setSubstaId(
	//					caDA.getIntFromDB(lrsQry, "SubstaId"));
	//				laPaymentSummaryData.setSummaryEffDate(
	//					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
	//				laPaymentSummaryData.setFeeSourceCd(
	//					caDA.getIntFromDB(lrsQry, "FeeSourceCd"));
	//				laPaymentSummaryData.setPymntTypeCd(
	//					caDA.getIntFromDB(lrsQry, "PymntTypeCd"));
	//				laPaymentSummaryData.setPymntTypeQty(
	//					caDA.getIntFromDB(lrsQry, "PymntTypeQty"));
	//				laPaymentSummaryData.setTotalPymntTypeAmt(
	//					caDA.getDollarFromDB(lrsQry, "TotalPymntTypeAmt"));
	//				// Add element to the Vector
	//				lvRslt.addElement(laPaymentSummaryData);
	//			} //End of While
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(Log.METHOD, this, " - qryPaymentSummary - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryPaymentSummary - Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 

	/**
	 * Method to Query Voided Payments for Substation Summary
	 * RTS I  - TRANSC5
	 * RTS II - Select as negative (as exists) so that will add in 
	 *          updPaymentSummary
	 * 
	 * @param  aaPaymentSummaryData	PaymentSummaryData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryVoidPaymentForPaymentSummary(PaymentSummaryData aaPaymentSummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryVoidPaymentForPaymentSummary - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		int liSummaryEffDate = aaPaymentSummaryData.getSummaryEffDate();

		// KPH (2002.10.18): CQU10004912 Substation Summary Performance
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ " A.OFCISSUANCENO, "
				+ " A.SUBSTAID, "
				+ liSummaryEffDate
				+ " as SummaryEffDate, "
				+ " B.PYMNTTYPECD, "
				+ " A.FEESOURCECD, "
				+ " -1*COUNT(*) as PymntTypeQty, "
				+ " SUM(B.PYMNTTYPEAMT) as TotalPymntTypeAmt "
				+ " FROM "
				+ " RTS.RTS_TRANS_HDR      A, "
				+ " RTS.RTS_TRANS_PAYMENT  B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ?  AND  "
				+ " A.SUBSTAID = ?       AND  "
				+ " A.SUMMARYEFFDATE = ? AND  "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " EXISTS "
				+ " (SELECT * FROM RTS.RTS_TRANS C "
				+ " WHERE "
				+ " C.OFCISSUANCENO = A.OFCISSUANCENO AND  "
				+ " C.SUBSTAID = A.SUBSTAID AND  "
				+ " C.TRANSAMDATE = A.TRANSAMDATE AND "
				+ " C.TRANSWSID = A.TRANSWSID AND "
				+ " C.CUSTSEQNO = A.CUSTSEQNO AND "
				+ " C.TRANSCD IN ('VOID','VOIDNC') ) "
				+ " GROUP BY A.OFCISSUANCENO,A.SUBSTAID,"
				+ " B.PYMNTTYPECD,A.FEESOURCECD "
				+ " ORDER BY 1,2,3,4 ");

		// 1
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaPaymentSummaryData.getOfcIssuanceNo())));
		// 2
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaPaymentSummaryData.getSubstaId())));
		// 3 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaPaymentSummaryData.getSummaryEffDate())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryVoidPaymentForPaymentSummary - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryVoidPaymentForPaymentSummary - SQL - End");
			while (lrsQry.next())
			{
				PaymentSummaryData laPaymentSummaryData =
					new PaymentSummaryData();
				laPaymentSummaryData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laPaymentSummaryData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laPaymentSummaryData.setSummaryEffDate(
					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
				laPaymentSummaryData.setFeeSourceCd(
					caDA.getIntFromDB(lrsQry, "FeeSourceCd"));
				laPaymentSummaryData.setPymntTypeCd(
					caDA.getIntFromDB(lrsQry, "PymntTypeCd"));
				laPaymentSummaryData.setPymntTypeQty(
					caDA.getIntFromDB(lrsQry, "PymntTypeQty"));
				laPaymentSummaryData.setTotalPymntTypeAmt(
					caDA.getDollarFromDB(lrsQry, "TotalPymntTypeAmt"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentSummaryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryVoidPaymentForPaymentSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryVoidPaymentForPaymentSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_PYMNT_SUMMARY - used in Substation Summary
	 * RTS II (vs. RTS I) add to Pymnt_Summary vs. substract
	 *
	 * @param  aaPaymentSummaryData	PaymentSummaryData
	 * @throws RTSException
	 */
	public int updPaymentSummary(PaymentSummaryData aaPaymentSummaryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updPaymentSummary - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_PYMNT_SUMMARY SET "
				+ " PymntTypeQty = PymntTypeQty + ? , "
				+ " TotalPymntTypeAmt = TotalPymntTypeAmt + ? "
				+ " WHERE OFCISSUANCENO = ? AND "
				+ " SubstaId = ?  AND "
				+ " SUMMARYEFFDATE = ? AND "
				+ " FEESOURCECD = ? AND "
				+ " PYMNTTYPECD = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getPymntTypeQty())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getTotalPymntTypeAmt())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getSummaryEffDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getFeeSourceCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentSummaryData.getPymntTypeCd())));

			Log.write(Log.SQL, this, "updPaymentSummary - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updPaymentSummary - SQL - End");
			Log.write(Log.METHOD, this, "updPaymentSummary - End");
			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updPaymentSummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS