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
 * FeeTypeReportSQL.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * K. Harrell   11/17/2001  Added lSelect, lList, FundsData
 * K. Harrell   01/03/2002  Altered order by for Report
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify qryFeeTypeReport()
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/07/2008	Update to handle no workstation in list
 * 							modify qryFeeTypeReport()
 * 							defect 8913 Ver MyPlates_POS 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains SQL for FeeTypeReport
 *  
 * @version		MyPlates_POS	08/07/2008 
 * @author		Kathy Harrell
 * <br>Creation Date:			10/04/2001 10:49:13
 */
public class FeeTypeReportSQL extends FeeTypeReportData
{
	DatabaseAccess caDA;
	
	/**
	 * FeeTypeReportSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public FeeTypeReportSQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Retrieve data for FeeTypeReport
	 * 
	 * @param aaFundsData FundsData
	 * @return Vector 	
	 * @throws RTSException
	 */
	public Vector qryFeeTypeReport(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryFeeTypeReport - Begin");
		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		FundsSQL laFS = new FundsSQL();
		FundsSQLData laFSData =
			(FundsSQLData) laFS.genFundsSQL(aaFundsData);

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
		
		// defect 8913 
		// If no entities in list, return empty vector 
		if (lsList.equals(" () AND "))
		{
			return lvRslt;
		}
		// end defect 8913 
		
		if (aaFundsData.getFundsReportData().isShowSourceMoney())
		{
			lsSelect = lsSelect + ", H.FeesourceCd";
		}

		String lsOrderBy = "";

		// Or can this simply be = null     
		if (aaFundsData.getFundsReportData().getPrimarySplit()
			== FundsConstant.CASH_DRAWER
			|| aaFundsData.getFundsReportData().getPrimarySplit()
				== FundsConstant.EMPLOYEE)
		{
			lsOrderBy = " ORDER BY 1,2,8,7,6";
			if (aaFundsData.getFundsReportData().isShowSourceMoney())
			{
				lsOrderBy = lsOrderBy + ",3 ";
			}

		}
		else
		{
			lsOrderBy = " order by 1,7,6,5";

			if (aaFundsData.getFundsReportData().isShowSourceMoney())
			{
				lsOrderBy = lsOrderBy + ",2 ";
			}
		}

		ResultSet laQry;

		// ItmQty > 0; Not In ('VOID','VOIDNC') 
		lsQry.append("SELECT "
				+ lsSelect
				+ ",E.PAYABLETYPECDDESC,  "
				+ " C.PAYABLETYPECD,  "
				+ " C.ACCTITMCDDESC,  "
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,  "
				+ " SUM(B.ITMQTY) as ITMQTY,  "
				+ " SUM(B.ITMPRICE) AS ITMPRICE "
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B,  "
				+ " RTS.RTS_ACCT_CODES C,  "
				+ " RTS.RTS_PAYABLE_TYPE E, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND  "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " B.ACCTITMCD=C.ACCTITMCD AND "
				+ " C.PAYABLETYPECD=E.PAYABLETYPECD AND "
				+ " B.ITMQTY>0 AND "
				+ " B.TRANSCD NOT IN ('VOID','VOIDNC') AND  "
				+ " C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)"
				+ " FROM RTS.RTS_ACCT_CODES G  "
				+ " WHERE G.ACCTITMCD=C.ACCTITMCD) "
				+ " GROUP BY "
				+ lsSelect
				+ " ,E.PAYABLETYPECDDESC, C.PAYABLETYPECD,"
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,C.ACCTITMCDDESC ");

		// ItmQty > 0; in ('VOID','VOIDNC') 
		lsQry.append(" UNION ALL "
				+ " SELECT  "
				+ lsSelect
				+ " ,E.PAYABLETYPECDDESC,  "
				+ " C.PAYABLETYPECD,  "
				+ " C.ACCTITMCDDESC,  "
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,  "
				+ " -1*SUM(B.ITMQTY) AS ITMQTY,  "
				+ " SUM(B.ITMPRICE) AS ITMPRICE  "
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B,  "
				+ " RTS.RTS_ACCT_CODES C, "
				+ " RTS.RTS_PAYABLE_TYPE E,"
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO =? AND "
				+ " H.SUBSTAID = ? AND  "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " B.ACCTITMCD=C.ACCTITMCD AND "
				+ " C.PAYABLETYPECD=E.PAYABLETYPECD AND "
				+ " B.ITMQTY>0 AND "
				+ " B.TRANSCD IN ('VOID','VOIDNC') AND  "
				+ " C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)" 
				+ " FROM RTS.RTS_ACCT_CODES G WHERE"
				+ " G.ACCTITMCD=C.ACCTITMCD) "
				+ " GROUP BY "
				+ lsSelect
				+ " ,E.PAYABLETYPECDDESC, C.PAYABLETYPECD, C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,C.ACCTITMCDDESC ");

		// ItmQty = 0; Not in ('VOID','VOIDNC') 
		lsQry.append(" UNION ALL "
				+ " SELECT "
				+ lsSelect
				+ " ,E.PAYABLETYPECDDESC,  "
				+ " C.PAYABLETYPECD,  "
				+ " C.ACCTITMCDDESC,  "
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,  "
				+ " COUNT(*) AS ITMQTY,  "
				+ " SUM(B.ITMPRICE) AS ITMPRICE  "
				+ " FROM  "
				+ " RTS.RTS_TR_FDS_DETAIL B,  "
				+ " RTS.RTS_ACCT_CODES C,  "
				+ " RTS.RTS_PAYABLE_TYPE E, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO =? AND "
				+ " H.SUBSTAID = ? AND  "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND  "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND  "
				+ " B.ACCTITMCD=C.ACCTITMCD AND  "
				+ " C.PAYABLETYPECD=E.PAYABLETYPECD AND "
				+ " B.ITMQTY=0 AND  "
				+ " B.TRANSCD NOT IN  "
				+ " ('VOID','VOIDNC') AND  "
				+ " C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)  "
				+ " FROM RTS.RTS_ACCT_CODES G WHERE"
				+ " G.ACCTITMCD=C.ACCTITMCD) "
				+ " GROUP BY "
				+ lsSelect
				+ ",E.PAYABLETYPECDDESC, C.PAYABLETYPECD,"
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,C.ACCTITMCDDESC ");

		// ItmQty = 0; In ('VOID','VOIDNC') 
		lsQry.append(" UNION ALL "
				+ " SELECT "
				+ lsSelect
				+ ",E.PAYABLETYPECDDESC,  "
				+ " C.PAYABLETYPECD,  "
				+ " C.ACCTITMCDDESC,  "
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,  "
				+ " -COUNT(*) AS ITMQTY,  "
				+ " SUM(B.ITMPRICE) AS ITMPRICE "
				+ " FROM "
				+ " RTS.RTS_TR_FDS_DETAIL B,  "
				+ " RTS.RTS_ACCT_CODES  "
				+ " C, RTS.RTS_PAYABLE_TYPE E, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsList
				+ " H.OFCISSUANCENO = ? AND "
				+ " H.SUBSTAID = ? AND  "
				+ " H.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " H.SUBSTAID = B.SUBSTAID AND  "
				+ " H.TRANSWSID = B.TRANSWSID AND "
				+ " H.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " H.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " B.ACCTITMCD=C.ACCTITMCD AND "
				+ " C.PAYABLETYPECD=E.PAYABLETYPECD AND "
				+ " B.ITMQTY=0 AND "
				+ " B.TRANSCD IN ('VOID','VOIDNC') AND "
				+ " C.RTSEFFDATE=(SELECT MAX(RTSEFFDATE)  "
				+ " FROM RTS.RTS_ACCT_CODES G WHERE"
				+ " G.ACCTITMCD=C.ACCTITMCD) "
				+ " GROUP BY "
				+ lsSelect
				+ " ,E.PAYABLETYPECDDESC, C.PAYABLETYPECD,"
				+ " C.ACCTITMGRPCD,  "
				+ " C.ACCTITMCRDTINDI,C.ACCTITMCDDESC "
				+ lsOrderBy);
		try
		{
			// 1 ItmQty>0; Not in ('VOID','VOIDNC')
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					// defect 7896
					// changed call to a static reference
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
					// end defect 7896
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					// defect 7896
					// changed call to a static reference
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
					// end defect 7896
			// 2 ItmQty>0; In ('VOID','VOIDNC')
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					// defect 7896
					// changed call to a static reference
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
					// end defect 7896
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					// defect 7896
					// changed call to a static reference
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
					// end defect 7896
			// 3 ItmQty=0; Not in ('VOID','VOIDNC')
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					// defect 7896
					// changed call to a static reference
					DatabaseAccess.convertToString(
						aaFundsData.getOfficeIssuanceNo())));
					// end defect 7896
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFundsData.getSubStationId())));
			// 4 ItmQty=0; In ('VOID','VOIDNC')
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
			Log.write(
				Log.SQL,
				this,
				" - qryFeeTypeReport - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryFeeTypeReport - SQL - End");

			while (laQry.next())
			{
				FeeTypeReportData laFeeTypeReportData =
					new FeeTypeReportData();

				if (lbByEmployee)
				{
					laFeeTypeReportData.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}

				if (lbByCashDrawer)
				{
					laFeeTypeReportData.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}

				if (aaFundsData
					.getFundsReportData()
					.isShowSourceMoney())
				{
					laFeeTypeReportData.setFeeSourceCd(
						caDA.getIntFromDB(laQry, "FeeSourceCd"));
				}

				laFeeTypeReportData.setPayableTypeCd(
					caDA.getStringFromDB(laQry, "PayableTypeCd"));
				laFeeTypeReportData.setPayableTypeCdDesc(
					caDA.getStringFromDB(laQry, "PayableTypeCdDesc"));
				laFeeTypeReportData.setAcctItmCdDesc(
					caDA.getStringFromDB(laQry, "AcctItmCdDesc"));
				laFeeTypeReportData.setAcctItmGrpCd(
					caDA.getIntFromDB(laQry, "AcctItmGrpCd"));
				laFeeTypeReportData.setAcctItmCrdtIndi(
					caDA.getIntFromDB(laQry, "AcctItmCrdtIndi"));
				laFeeTypeReportData.setItmQty(
					caDA.getIntFromDB(laQry, "ItmQty"));
				laFeeTypeReportData.setItmPrice(
					caDA.getDollarFromDB(laQry, "ItmPrice"));

				// Add element to the Vector
				lvRslt.addElement(laFeeTypeReportData);

			} //End of While 

			laQry.close();
			laQry = null;
			Log.write(Log.METHOD, this, " - qryFeeTypeReport - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryFeeTypeReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
