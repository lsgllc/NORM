package com.txdot.isd.rts.services.reports.funds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * PaymentTypeReportSQL.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * K. Harrell   11/17/2001  Added lSelect, lList, FundsData
 * K. Harrell   01/02/2002  Corrected pull of FeeSourceCd in lsQry2
 * K. Harrell   08/23/2002  Added code to sql to capture ws that had
 *                          transactions but no payments (e.g. vehicle
 *                          inquiry with no fees) when running payment
 *                          reports for multiple ws.  There was not
 *                          a problem when only 1 ws was selected.
 *                          Fixes defect 4620.
 * K. Harrell   12/11/2002  Corrected SQL for above.  Defect 5151
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify qryPaymentTypeReport()
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database 
 * 
 * @version	5.2.3		05/31/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	10/01/2001 12:57:59
 */
public class PaymentTypeReportSQL extends PaymentTypeReportData
{
	DatabaseAccess caDA;
	
	/**
	 * PaymentTypeReportSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PaymentTypeReportSQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Retrieve Data for PaymentType Report
	 * 
	 * @param aaFundsData FundsData	
     * @return Vector
     * @throws RTSException 	
	 */
	public Vector qryPaymentTypeReport(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPaymentTypeReport - Begin");
		StringBuffer lsQry = new StringBuffer();
		StringBuffer lsQry2 = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		Vector lvValues2 = new Vector();
		FundsSQL laFS = new FundsSQL();
		FundsSQLData laFSData =
			(FundsSQLData)laFS.genFundsSQL(aaFundsData);
		boolean lbByEmployee =
			aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE
				|| aaFundsData.getFundsReportData().getPrimarySplit()
					== FundsConstant.EMPLOYEE;
		boolean lbByCashDrawer =
			aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER
				|| aaFundsData.getFundsReportData().getPrimarySplit()
					== FundsConstant.CASH_DRAWER;
		String lsSelect = laFSData.getSelect();
		String lsList = laFSData.getList();
		if (lsList.equals(" () AND "))
		{
			return lvRslt;
		}
		if (aaFundsData.getFundsReportData().isShowSourceMoney())
		{
			lsSelect = lsSelect + ", H.FeesourceCd";
		}
		ResultSet laQry;
		// No Payments
		// Defect 5151 
		lsQry.append(" SELECT  "
				+ lsSelect
				+ " ,1 as CashDrawerIndi,  "
				+ " 'CASH' AS PymntTypeCdDesc, "
				+ " COUNT(*) as PymntTypeQty,  "
				+ " 0 as PymntTypeAmt "
				+ " FROM RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND "
				+ " H.FEESOURCECD <> 0 AND    "
				+ " NOT EXISTS (SELECT * FROM RTS.RTS_TRANS_PAYMENT C "
				+ " WHERE  "
				+ " H.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ " H.SUBSTAID = C.SUBSTAID AND  "
				+ " H.TRANSAMDATE = C.TRANSAMDATE AND  "
				+ " H.TRANSWSID=C.TRANSWSID AND  "
				+ " H.CUSTSEQNO = C.CUSTSEQNO)  "
				+ " GROUP BY "
				+ lsSelect);
		// Payments Exist
		lsQry.append(" UNION ALL "
				+ " SELECT "
				+ lsSelect
				+ " ,1 as CashDrawerIndi,  "
				+ " C.PYMNTTYPECDDESC,  "
				+ " COUNT(*) AS PymntTypeQty, "
				+ " SUM(B.PYMNTTYPEAMT) as PymntTypeAmt  "
				+ " FROM  "
				+ " RTS.RTS_TRANS_HDR H,  "
				+ " RTS.RTS_TRANS_PAYMENT B,  "
				+ " RTS.RTS_PAYMENT_TYPE C  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " H.SUBSTAID = B.SUBSTAID AND    "
				+ " H.TRANSWSID = B.TRANSWSID AND    "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND   "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND  "
				+ " B.PYMNTTYPECD = C.PYMNTTYPECD AND   "
				+ " B.PYMNTTYPEAMT >= 0    "
				+ " GROUP BY "
				+ lsSelect
				+ ",C.PYMNTTYPECDDESC ");
		// Cash Change Due     
		lsQry.append(" UNION ALL "
				+ " SELECT "
				+ lsSelect
				+ " ,1 as CashDrawerIndi,  "
				+ " C.PYMNTTYPECDDESC,  "
				+ " 0 as PymntTypeQty,  "
				+ " -SUM(B.CHNGDUE) as PymntTypeAmt  "
				+ " FROM RTS.RTS_TRANS_HDR H,  "
				+ " RTS.RTS_TRANS_PAYMENT B, "
				+ " RTS.RTS_PAYMENT_TYPE C   "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " H.SUBSTAID = B.SUBSTAID AND    "
				+ " H.TRANSWSID = B.TRANSWSID AND    "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND   "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " B.CHNGDUEPYMNTTYPECD = C.PYMNTTYPECD AND   "
				+ " B.CHNGDUEPYMNTTYPECD  =1   "
				+ " GROUP BY "
				+ lsSelect
				+ ",C.PYMNTTYPECDDESC ");
		// 4 NonCashDrawer; Qty = 0 
		lsQry.append(" UNION ALL  "
				+ " SELECT  "
				+ lsSelect
				+ " ,0 as CashDrawerIndi,  "
				+ " C.ACCTITMCDDESC AS PymntTypeCdDesc,  "
				+ " COUNT(*) as PymntTypeQty,  "
				+ " SUM(B.ITMPRICE) as PymntTypeAmt  "
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B,  "
				+ " RTS.RTS_ACCT_CODES C, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND "
				+ " B.ITMQTY = 0 AND  "
				+ " B.TRANSCD NOT IN ('VOID','VOIDNC') AND    "
				+ " B.OFCISSUANCENO = H.OFCISSUANCENO AND "
				+ " B.SUBSTAID = H.SUBSTAID AND  "
				+ " B.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " B.TRANSWSID = H.TRANSWSID AND  "
				+ " B.CUSTSEQNO = H.CUSTSEQNO AND    "
				+ " B.CASHDRAWERINDI=0 AND   "
				+ " B.ACCTITMCD=C.ACCTITMCD AND   "
				+ " C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)"
				+ " FROM RTS.RTS_ACCT_CODES G  "
				+ " WHERE G.ACCTITMCD=C.ACCTITMCD) "
				+ " GROUP BY "
				+ lsSelect
				+ ",C.ACCTITMCDDESC ");
		// 5 NonCashDrawer; Qty >0, Not in Void
		lsQry2.append(" SELECT "
				+ lsSelect
				+ ",0 AS CASHDRAWERINDI, "
				+ " C.ACCTITMCDDESC AS PymntTypeCdDesc, "
				+ " SUM(B.ITMQTY) AS PymntTypeQty, "
				+ " SUM(B.ITMPRICE) AS PymntTypeAmt"
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B, "
				+ " RTS.RTS_ACCT_CODES C, "
				+ " RTS.RTS_TRANS_HDR H "
				+ " WHERE "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND "
				+ " B.ITMQTY > 0 AND "
				+ " B.TRANSCD NOT IN('VOID', 'VOIDNC') AND "
				+ " B.OFCISSUANCENO = H.OFCISSUANCENO AND "
				+ " B.SUBSTAID = H.SUBSTAID AND "
				+ " B.TRANSAMDATE = H.TRANSAMDATE AND "
				+ " B.TRANSWSID = H.TRANSWSID AND "
				+ " B.CUSTSEQNO = H.CUSTSEQNO AND "
				+ " B.CASHDRAWERINDI = 0 AND "
				+ " B.ACCTITMCD = C.ACCTITMCD AND C.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_ACCT_CODES G "
				+ " WHERE G.ACCTITMCD = C.ACCTITMCD) "
				+ " GROUP BY "
				+ lsSelect
				+ ",C.ACCTITMCDDESC ");
		// 6 NonCashDrawer; Qty =0, In Void
		lsQry2.append(" UNION ALL "
				+ " SELECT "
				+ lsSelect
				+ " ,0 AS CASHDRAWERINDI,  "
				+ " C.ACCTITMCDDESC AS PymntTypeCdDesc,  "
				+ " 1*COUNT(*) AS PymntTypeQty, "
				+ " SUM(B.ITMPRICE) AS PymntTypeAmt "
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B,  "
				+ " RTS.RTS_ACCT_CODES C, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND  "
				+ " H.SUBSTAID = ? AND "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND   "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " B.ITMQTY = 0 AND  "
				+ " B.TRANSCD IN ('VOID','VOIDNC') AND    "
				+ " B.CASHDRAWERINDI=0 AND   "
				+ " B.ACCTITMCD=C.ACCTITMCD  "
				+ " AND  C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)  "
				+ " FROM RTS.RTS_ACCT_CODES G  "
				+ " WHERE G.ACCTITMCD=C.ACCTITMCD)  "
				+ " GROUP BY "
				+ lsSelect
				+ ", C.ACCTITMCDDESC ");
		// 7 NonCashDrawer; Qty !=0, In Void 
		lsQry2.append(" UNION ALL  "
				+ " SELECT "
				+ lsSelect
				+ ",0 as CashdrawerIndi, C.ACCTITMCDDESC AS"
				+ " PymntTypeCdDesc,  "
				+ " -1*SUM(ITMQTY) AS PymntTypeQty, "
				+ " SUM(B.ITMPRICE) as PymntTypeAmt "
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B, "
				+ " RTS.RTS_ACCT_CODES C, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND  "
				+ " H.SUBSTAID = ? AND  "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND   "
				+ " H.TRANSAMDATE =   B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " B.ITMQTY > 0 AND   "
				+ " B.TRANSCD IN ('VOID','VOIDNC') AND "
				+ " B.CASHDRAWERINDI=0 AND   "
				+ " B.ACCTITMCD=C.ACCTITMCD AND   "
				+ " C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)"
				+ " FROM RTS.RTS_ACCT_CODES G  "
				+ " WHERE G.ACCTITMCD=C.ACCTITMCD)   "
				+ " GROUP BY "
				+ lsSelect
				+ ",C.ACCTITMCDDESC ");
		// 8 CashDrawer; Negative Payment 
		lsQry2.append(" UNION ALL  "
				+ " SELECT "
				+ lsSelect
				+ ",1 AS CASHDRAWERINDI, C.PYMNTTYPECDDESC,  "
				+ " -COUNT(*) AS PymntTypeQty, "
				+ " SUM(B.PYMNTTYPEAMT) AS PymntTypeAmt from  "
				+ " RTS.RTS_TRANS_HDR H, "
				+ " RTS.RTS_TRANS_PAYMENT B,  "
				+ " RTS.RTS_PAYMENT_TYPE C  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND  "
				+ " H.SUBSTAID = ? AND "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND    "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND   "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " B.PYMNTTYPECD = C.PYMNTTYPECD AND   "
				+ " B.PYMNTTYPEAMT < 0 "
				+ " GROUP BY "
				+ lsSelect
				+ ",C.PYMNTTYPECDDESC");
		// 9 NonCashDrawer; Check Change Given
		lsQry2.append(" UNION ALL "
				+ " SELECT "
				+ lsSelect
				+ ",0 AS CASHDRAWERINDI,  "
				+ " 'CHECK CHANGE GIVEN' as PymntTypeCdDesc,  "
				+ " COUNT(*) AS PymntTypeQty,  "
				+ " -SUM(B.CHNGDUE) AS PymntTypeAmt  "
				+ " FROM RTS.RTS_TRANS_HDR H,  "
				+ " RTS.RTS_TRANS_PAYMENT B,  "
				+ " RTS.RTS_PAYMENT_TYPE C  WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND  "
				+ " H.SUBSTAID = ? AND "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND    "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND    "
				+ " B.CHNGDUEPYMNTTYPECD = C.PYMNTTYPECD AND   "
				+ " B.CHNGDUEPYMNTTYPECD =2 "
				+ " GROUP BY "
				+ lsSelect);
		try
		{
			// 1 No Payment
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
			// defect 7896
			// changed convertToString calls to static references
			// Ex. caDA.convertToString ->
			// DatabaseAccess.convertToString
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			
			/*  Defect 5151
			lvValues.addElement(
			    new DBValue(
			        Types.INTEGER,
			        cDA.convertToString(
			        aFundsData.getOfficeIssuanceNo())));
			lvValues.addElement(
			    new DBValue(Types.INTEGER,
			    cDA.convertToString(aFundsData.getSubStationId())));
			*/
			// 2 Payments Exist 
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
			// 3 Cash Change Due 
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
			// 4 NonCashDrawer; Qty = 0 
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
			// 5 NonCashDrawer; Qty >0, Not in Void
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			// 6 NonCashDrawer; Qty =0, In Void
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			// 7 NonCashDrawer; Qty !=0, In Void
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			// 8 CashDrawer; Negative Payment
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			//  9 Non CashDrawer; Check Change Given
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			// end defect 7896
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentTypeReportA - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentTypeReportA - SQL - End");
			while (laQry.next())
			{
				PaymentTypeReportData laPaymentTypeReportData =
					new PaymentTypeReportData();
				if (lbByEmployee)
				{
					laPaymentTypeReportData.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				if (lbByCashDrawer)
				{
					laPaymentTypeReportData.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}
				// lPaymentTypeReportData.setCashWsId(
				//  cDA.getIntFromDB(lrsQry, "CashWsId"));
				// lPaymentTypeReportData.setTransEmpId(
				//  cDA.getStringFromDB(lrsQry, "TransEmpId"));
				if (aaFundsData.getFundsReportData()
					.isShowSourceMoney())
				{
					laPaymentTypeReportData.setFeeSourceCd(
						caDA.getIntFromDB(laQry, "FeeSourceCd"));
					//       lPaymentTypeReportData.setFeeSourceCd(1);
				}
				// lPaymentTypeReportData.setCashWsId(
				//  cDA.getIntFromDB(lrsQry, "CashWsId"));
				// lPaymentTypeReportData.setTransEmpId(
				//  cDA.getStringFromDB(lrsQry, "TransEmpId"));
				// lPaymentTypeReportData.setFeeSourceCd(
				//  cDA.getIntFromDB(lrsQry, "FeeSourceCd"));
				laPaymentTypeReportData.setCashDrawerIndi(
					caDA.getIntFromDB(laQry, "CashDrawerIndi"));
				laPaymentTypeReportData.setPymntTypeCdDesc(
					caDA.getStringFromDB(laQry, "PymntTypeCdDesc"));
				laPaymentTypeReportData.setPymntTypeQty(
					caDA.getIntFromDB(laQry, "PymntTypeQty"));
				laPaymentTypeReportData.setPymntTypeAmt(
					caDA.getDollarFromDB(laQry, "PymntTypeAmt"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentTypeReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryPaymentTypeReportA - End ");
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentTypeReportB - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry2.toString(), lvValues2);
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentTypeReportB - SQL - End");
			while (laQry.next())
			{
				PaymentTypeReportData laPaymentTypeReportData =
					new PaymentTypeReportData();
				if (lbByEmployee)
				{
					laPaymentTypeReportData.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				if (lbByCashDrawer)
				{
					laPaymentTypeReportData.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}
				// lPaymentTypeReportData.setCashWsId(
				//  cDA.getIntFromDB(lrsQry, "CashWsId"));
				// lPaymentTypeReportData.setTransEmpId(
				//  cDA.getStringFromDB(lrsQry, "TransEmpId"));
				if (aaFundsData
					.getFundsReportData()
					.isShowSourceMoney())
				{
					laPaymentTypeReportData.setFeeSourceCd(
						caDA.getIntFromDB(laQry, "FeeSourceCd"));
					// lPaymentTypeReportData.setFeeSourceCd(1);  //
				}
				laPaymentTypeReportData.setCashDrawerIndi(
					caDA.getIntFromDB(laQry, "CashDrawerIndi"));
				laPaymentTypeReportData.setPymntTypeCdDesc(
					caDA.getStringFromDB(laQry, "PymntTypeCdDesc"));
				laPaymentTypeReportData.setPymntTypeQty(
					caDA.getIntFromDB(laQry, "PymntTypeQty"));
				laPaymentTypeReportData.setPymntTypeAmt(
					caDA.getDollarFromDB(laQry, "PymntTypeAmt"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentTypeReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryPaymentTypeReportB - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPaymentTypeReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF  CLASS
