package com.txdot.isd.rts.services.reports.funds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.SubstationSummaryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * CountyWideSQL.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/22/2002	Add qrySubstaSummaryCountyWide to 
 * 							support Exception Report.
 *							defect CQU100001964
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify qryInventorySummarySubstationSummary
 *							qryInventoryCountyWide
 * 							Alter cDA.convertToString to 
 * 							DatabaseAccess.convertToString 
 * 							Ver 5.2.0
 * S Johnston	03/14/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/30/2005	Funds/SQL class review
 * 							Moved SubstationSummaryData from services.db
 * 							to services.data
 * 							defect 8163 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database
 *  
 * @version		5.2.3	06/30/2005 
 * @author		Kathy Harrell
 * <br>Creation Date:	10/02/2004
 */
public class CountyWideSQL
{
	DatabaseAccess caDA;
	/**
	 * CountyWideSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public CountyWideSQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	* Method to Retrieve FeeSummaryReportData for CountyWide
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector 
	* @throws RTSException	
	*/
	public Vector qryFeeSummaryCountyWide(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(Log.METHOD, this,
			" - qryFeeSummaryCountyWide - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		lsQry.append(
			" SELECT  "
				+ " A.FEESOURCECD, "
				+ " C.PAYABLETYPECD, "
				+ " B.PAYABLETYPECDDESC, "
				+ " C.ACCTITMGRPCD, "
				+ " C.ACCTITMCRDTINDI, "
				+ " C.ACCTITMCDDESC, "
				+ " SUM(A.TOTALACCTITMQTY) AS TOTALACCTITMQTY, "
				+ " SUM( A.TOTALACCTITMAMT) AS TOTALACCTITMAMT "
				+ " FROM RTS.RTS_FEE_SUMMARY A, "
				+ " RTS.RTS_PAYABLE_TYPE B, "
				+ " RTS.RTS_ACCT_CODES C "
				+ " WHERE  "
				+ " OFCISSUANCENO = ? AND  "
				+ " SUMMARYEFFDATE = ? AND  "
				+ " C.PAYABLETYPECD = B.PAYABLETYPECD AND    "
				+ " A.ACCTITMCD = C.ACCTITMCD AND "
				+ " C.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_ACCT_CODES D "
				+ " WHERE D.ACCTITMCD = "
				+ " C.ACCTITMCD) "
				+ " GROUP BY  "
				+ " A.FEESOURCECD,  C.PAYABLETYPECD,"
				+ " B.PAYABLETYPECDDESC, "
				+ " C.ACCTITMGRPCD,C.ACCTITMCRDTINDI, C.ACCTITMCDDESC "
				+ " ORDER BY C.PAYABLETYPECD,C.ACCTITMGRPCD,"
				+ "C.ACCTITMCRDTINDI,C.ACCTITMCDDESC,A.FEESOURCECD ");
		try
		{
			// OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			//SummaryEffDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			Log.write(
				Log.SQL,
				this,
				" - qryFeeSummaryCountyWide - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryFeeSummaryCountyWide - SQL - End");
			while (laQry.next())
			{
				FeeSummaryReportData laFeeSummaryReportData =
					new FeeSummaryReportData();
				laFeeSummaryReportData.setFeeSourceCd(
					caDA.getIntFromDB(laQry, "FeeSourceCd"));
				laFeeSummaryReportData.setPayableTypeCd(
					caDA.getIntFromDB(laQry, "PayableTypeCd"));
				laFeeSummaryReportData.setPayableTypeCdDesc(
					caDA.getStringFromDB(laQry, "PayableTypeCdDesc"));
				laFeeSummaryReportData.setAcctItmGrpCd(
					caDA.getIntFromDB(laQry, "AcctItmGrpCd"));
				laFeeSummaryReportData.setAcctItmCrdtIndi(
					caDA.getIntFromDB(laQry, "AcctItmCrdtIndi"));
				laFeeSummaryReportData.setAcctItmCdDesc(
					caDA.getStringFromDB(laQry, "AcctItmCdDesc"));
				laFeeSummaryReportData.setTotalAcctItmQty(
					caDA.getIntFromDB(laQry, "TotalAcctItmQty"));
				laFeeSummaryReportData.setTotalAcctItmAmt(
					caDA.getDollarFromDB(laQry, "TotalAcctItmAmt"));
				// Add element to the Vector
				lvRslt.addElement(laFeeSummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryFeeSummaryCountyWide - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryFeeSummaryCountyWide - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Retrieve FeeSummary Data for Substation Summary
	* 
	* @param aaGeneralSearchData GeneralSearchData	
	* @return  Vector 	
	* @throws RTSException
	*/
	public Vector qryFeeSummarySubstationSummary(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryFeeSummarySubstationSummary - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		lsQry.append(
			" SELECT  "
				+ " A.FEESOURCECD, "
				+ " C.PAYABLETYPECD, "
				+ " B.PAYABLETYPECDDESC, "
				+ " C.ACCTITMGRPCD, "
				+ " C.ACCTITMCRDTINDI, "
				+ " C.ACCTITMCDDESC, "
				+ " A.TOTALACCTITMQTY, "
				+ " A.TOTALACCTITMAMT "
				+ " FROM RTS.RTS_FEE_SUMMARY A, "
				+ " RTS.RTS_PAYABLE_TYPE B, "
				+ " RTS.RTS_ACCT_CODES C "
				+ " WHERE  "
				+ " OFCISSUANCENO = ? AND  "
				+ " SUBSTAID = ? AND  "
				+ " SUMMARYEFFDATE = ? AND  "
				+ " C.PAYABLETYPECD = B.PAYABLETYPECD AND    "
				+ " A.ACCTITMCD = C.ACCTITMCD AND "
				+ " C.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_ACCT_CODES D "
				+ " WHERE D.ACCTITMCD = "
				+ " C.ACCTITMCD) "
				+ " ORDER BY C.PAYABLETYPECD,C.ACCTITMGRPCD,"
				+ "C.ACCTITMCRDTINDI,C.ACCTITMCDDESC,A.FEESOURCECD ");
		try
		{
			// OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			// SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			//SummaryEffDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			Log.write(
				Log.SQL,
				this,
				" - qryFeeSummarySubstationSummary - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryFeeSummarySubstationSummary - SQL - End");
			while (laQry.next())
			{
				FeeSummaryReportData laFeeSummaryReportData =
					new FeeSummaryReportData();
				laFeeSummaryReportData.setFeeSourceCd(
					caDA.getIntFromDB(laQry, "FeeSourceCd"));
				laFeeSummaryReportData.setPayableTypeCd(
					caDA.getIntFromDB(laQry, "PayableTypeCd"));
				laFeeSummaryReportData.setPayableTypeCdDesc(
					caDA.getStringFromDB(laQry, "PayableTypeCdDesc"));
				laFeeSummaryReportData.setAcctItmGrpCd(
					caDA.getIntFromDB(laQry, "AcctItmGrpCd"));
				laFeeSummaryReportData.setAcctItmCrdtIndi(
					caDA.getIntFromDB(laQry, "AcctItmCrdtIndi"));
				laFeeSummaryReportData.setAcctItmCdDesc(
					caDA.getStringFromDB(laQry, "AcctItmCdDesc"));
				laFeeSummaryReportData.setTotalAcctItmQty(
					caDA.getIntFromDB(laQry, "TotalAcctItmQty"));
				laFeeSummaryReportData.setTotalAcctItmAmt(
					caDA.getDollarFromDB(laQry, "TotalAcctItmAmt"));
				// Add element to the Vector
				lvRslt.addElement(laFeeSummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryFeeSummarySubstationSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryFeeSummarySubstationSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Select InventorySummaryData for CountyWide
	* 
	* @param aaGeneralSearchData GeneralSearchData	
	* @return Vector 
	* @throws RTSException
	*/
	public Vector qryInventorySummaryCountyWide(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventorySummaryCountyWide - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		// PCR 34 - Added Sum(A.TotalItmQtyReprnt)..
		lsQry.append(
			" SELECT "
				+ "   B.ITMCDDESC, "
				+ "   INVITMYR, "
				+ "   SUM ( TOTALITMQTYSOLD ) AS TOTALITMQTYSOLD, "
				+ "   SUM (TOTALITMQTYVOID ) AS TOTALITMQTYVOID, "
				+ "   SUM (TOTALITMQTYREUSE ) AS TOTALITMQTYREUSE, "
				+ "   SUM (A.TOTALITMQTYREPRNT) AS TOTALITMQTYREPRNT "
				+ " FROM "
				+ "   RTS.RTS_INV_SUMMARY A, "
				+ "   RTS.RTS_ITEM_CODES B "
				+ " WHERE "
				+ "   A.OFCISSUANCENO = ? AND  "
				+ "   A.SUMMARYEFFDATE = ? AND  "
				+ "   A.ITMCD = B.ITMCD "
				+ " GROUP BY "
				+ "   B.ITMCDDESC, "
				+ "   INVITMYR ");
		try
		{
			// add lvValues build  Ray (CQU100001964)
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			Log.write(
				Log.SQL,
				this,
				" - qryInventorySummaryCountyWide - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventorySummaryCountyWide - SQL - End");
			while (laQry.next())
			{
				InventorySummaryReportData laInventorySummaryReportData
					= new InventorySummaryReportData();
				laInventorySummaryReportData.setItmCdDesc(
					caDA.getStringFromDB(laQry, "ItmCdDesc"));
				laInventorySummaryReportData.setInvItmYr(
					caDA.getIntFromDB(laQry, "InvItmYr"));
				laInventorySummaryReportData.setTotalItmQtySold(
					caDA.getIntFromDB(laQry, "TotalItmQtySold"));
				laInventorySummaryReportData.setTotalItmQtyVoid(
					caDA.getIntFromDB(laQry, "TotalItmQtyVoid"));
				laInventorySummaryReportData.setTotalItmQtyReuse(
					caDA.getIntFromDB(laQry, "TotalItmQtyReuse"));
				laInventorySummaryReportData.setTotalItmQtyReprnt(
					caDA.getIntFromDB(laQry, "TOTALITMQTYREPRNT"));
				// Add element to the Vector
				lvRslt.addElement(laInventorySummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventorySummaryCountyWide - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventorySummaryCountyWide - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Select InventorySummaryData for SubstationSummary
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector
	* @throws RTSException
	*/
	public Vector qryInventorySummarySubstationSummary(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventorySummarySubstationSummary - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		//		PCR 34 - Added Sum(A.TotalItmQtyReprnt)..
		lsQry.append(" SELECT "
				+ "   B.ITMCDDESC, "
				+ "   INVITMYR, "
				+ "   TOTALITMQTYSOLD, "
				+ "   TOTALITMQTYVOID, "
				+ "   TOTALITMQTYREUSE, "
				+ "   TOTALITMQTYREPRNT "
				+ " FROM "
				+ "   RTS.RTS_INV_SUMMARY A, "
				+ "   RTS.RTS_ITEM_CODES B "
				+ " WHERE "
				+ "   A.OFCISSUANCENO = ? AND "
				+ "   A.SUBSTAID = ? AND "
				+ "   A.SUMMARYEFFDATE = ?  "
				+ " AND "
				+ "   A.ITMCD = B.ITMCD "
				+ " ORDER BY "
				+ "   B.ITMCDDESC, "
				+ "   INVITMYR  ");
		try
		{
			// Set 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			Log.write(
				Log.SQL,
				this,
				" - qryInventorySummarySubstationSummary - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventorySummarySubstationSummary - SQL - End");
			while (laQry.next())
			{
				InventorySummaryReportData laInventorySummaryReportData =
					new InventorySummaryReportData();
				laInventorySummaryReportData.setItmCdDesc(
					caDA.getStringFromDB(laQry, "ItmCdDesc"));
				laInventorySummaryReportData.setInvItmYr(
					caDA.getIntFromDB(laQry, "InvItmYr"));
				laInventorySummaryReportData.setTotalItmQtySold(
					caDA.getIntFromDB(laQry, "TotalItmQtySold"));
				laInventorySummaryReportData.setTotalItmQtyVoid(
					caDA.getIntFromDB(laQry, "TotalItmQtyVoid"));
				laInventorySummaryReportData.setTotalItmQtyReuse(
					caDA.getIntFromDB(laQry, "TotalItmQtyReuse"));
				laInventorySummaryReportData.setTotalItmQtyReprnt(
					caDA.getIntFromDB(laQry, "TOTALITMQTYREPRNT"));
				// Add element to the Vector
				lvRslt.addElement(laInventorySummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventorySummarySubstationSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventorySummarySubstationSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Select Non-Cash Drawer Operations for County
	*    (PYMSUMC2 - RTS I) 
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector
	* @throws RTSException 	
	*/
	public Vector qryNonCashDrawerCountyWide(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryNonCashDrawerCountyWide - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		lsQry.append(" SELECT                             "
				+ "                            "
				+ "   FEESOURCECD, "
				+ "   B.PAYABLETYPECD, "
				+ "   B.ACCTITMCDDESC, "
				+ "   SUM ( TOTALACCTITMQTY ) AS TOTALACCTITMQTY, "
				+ "   SUM ( TOTALACCTITMAMT ) AS TOTALACCTITMAMT  "
				+ " FROM "
				+ "   RTS.RTS_FEE_SUMMARY A, "
				+ "   RTS.RTS_ACCT_CODES B "
				+ " WHERE "
				+ "   A.ACCTITMCD = B.ACCTITMCD AND "
				+ "   OFCISSUANCENO = ? AND  "
				+ "   SUMMARYEFFDATE = ? AND  "
				+ "   CASHDRAWERINDI = 0 AND "
				+ " B.RTSEFFDATE = "
				+ "  (SELECT MAX(RTSEFFDATE) FROM "
				+ "   RTS.RTS_ACCT_CODES C WHERE B.ACCTITMCD = "
				+ " C.ACCTITMCD) "
				+ " GROUP BY "
				+ "   B.ACCTITMCDDESC, "
				+ "   FEESOURCECD, "
				+ "   B.PAYABLETYPECD "
				+ " UNION ALL "
				+ " SELECT "
				+ "   FEESOURCECD, "
				+ "   2 AS PAYABLETYPECD, "
				+ "   'CHECK CHANGE GIVEN' AS ACCTITMCDDESC, "
				+ "   SUM ( TOTALACCTITMQTY ) AS TOTALACCTITMQTY, "
				+ "   SUM ( TOTALACCTITMAMT ) AS TOTALACCTITMAMT "
				+ " FROM "
				+ "   RTS.RTS_FEE_SUMMARY A "
				+ " WHERE "
				+ "   OFCISSUANCENO = ? AND  "
				+ "   SUMMARYEFFDATE = ? AND  "
				+ "   ACCTITMCD = 'CHKCHNG' "
				+ " GROUP BY "
				+ "   FEESOURCECD "
				+ " ORDER BY 1,4,2,3  ");
		try
		{
			// Set 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			// Set 2 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			Log.write(
				Log.SQL,
				this,
				" - qryNonCashDrawerCountyWide - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryNonCashDrawerCountyWide - SQL - End");
			while (laQry.next())
			{
				FeeSummaryReportData laFeeSummaryReportData =
					new FeeSummaryReportData();
				laFeeSummaryReportData.setFeeSourceCd(
					caDA.getIntFromDB(laQry, "FeeSourceCd"));
				laFeeSummaryReportData.setAcctItmCdDesc(
					caDA.getStringFromDB(laQry, "AcctItmCdDesc"));
				laFeeSummaryReportData.setTotalAcctItmQty(
					caDA.getIntFromDB(laQry, "TotalAcctItmQty"));
				laFeeSummaryReportData.setTotalAcctItmAmt(
					caDA.getDollarFromDB(laQry, "TotalAcctItmAmt"));
				// Add element to the Vector
				lvRslt.addElement(laFeeSummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryNonCashDrawerCountyWide - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryNonCashDrawerCountyWide - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Select Non-Cash Drawer Operations for Substation
	*    (PYMSUMC4 - RTS I) 
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector
	* @throws RTSException
	*/
	public Vector qryNonCashDrawerSubstationSummary(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryNonCashDrawerSubstationSummary - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		lsQry.append(" SELECT SUBSTAID , "
				+ "   FEESOURCECD, "
				+ "   B.PAYABLETYPECD, "
				+ "   SUM(TOTALACCTITMQTY) AS TOTALACCTITMQTY, "
				+ "   SUM(TOTALACCTITMAMT) AS TOTALACCTITMAMT, "
				+ "   B.ACCTITMCDDESC "
				+ " FROM "
				+ "   RTS.RTS_FEE_SUMMARY A, "
				+ "   RTS.RTS_ACCT_CODES B "
				+ " WHERE A.ACCTITMCD = B.ACCTITMCD AND "
				+ "   OFCISSUANCENO = ? AND  "
				+ "   SUBSTAID = ? AND  "
				+ "   SUMMARYEFFDATE =  ? AND  "
				+ "   CASHDRAWERINDI = 0 AND "
				+ "   B.RTSEFFDATE = "
				+ "   (SELECT MAX(RTSEFFDATE) FROM "
				+ "    RTS.RTS_ACCT_CODES C WHERE C.ACCTITMCD = "
				+ " 	B.ACCTITMCD) "
				+ " GROUP BY "
				+ " SUBSTAID, "
				+ " B.ACCTITMCDDESC, "
				+ " FEESOURCECD,  "
				+ " B.PAYABLETYPECD "
				+ " UNION ALL "
				+ " SELECT "
				+ "   SUBSTAID, "
				+ "   FEESOURCECD, "
				+ "   0 AS PAYABLETYPECD, "
				+ "   SUM(TOTALACCTITMQTY) AS TOTALACCTITMQTY , "
				+ "   SUM(TOTALACCTITMAMT) AS TOTALACCTITMAMT, "
				+ "   'CHECK CHANGE GIVEN' AS ACCTITMCDDESC  "
				+ " FROM "
				+ "   RTS.RTS_FEE_SUMMARY A "
				+ " WHERE "
				+ "   OFCISSUANCENO = ? AND  "
				+ "   SUBSTAID = ? AND   "
				+ "   SUMMARYEFFDATE = ? AND "
				+ "   ACCTITMCD = 'CHKCHNG'  "
				+ " GROUP BY "
				+ "   SUBSTAID, "
				+ "   FEESOURCECD "
				+ " ORDER BY "
				+ "  5,2  ");
		try
		{
			// Set 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			// Set 2 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			Log.write(
				Log.SQL,
				this,
				" - qryNonCashDrawerSubstationSummary - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryNonCashDrawerSubstationSummary - SQL - End");
			while (laQry.next())
			{
				FeeSummaryReportData laFeeSummaryReportData =
					new FeeSummaryReportData();
				laFeeSummaryReportData.setFeeSourceCd(
					caDA.getIntFromDB(laQry, "FeeSourceCd"));
				laFeeSummaryReportData.setAcctItmCdDesc(
					caDA.getStringFromDB(laQry, "AcctItmCdDesc"));
				laFeeSummaryReportData.setTotalAcctItmQty(
					caDA.getIntFromDB(laQry, "TotalAcctItmQty"));
				laFeeSummaryReportData.setTotalAcctItmAmt(
					caDA.getDollarFromDB(laQry, "TotalAcctItmAmt"));
				// Add element to the Vector
				lvRslt.addElement(laFeeSummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryNonCashDrawerSubstationSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryNonCashDrawerSubstationSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Query PaymentSummary for County
	*    (PYMSUMC1 - RTS I) 
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector
	* @throws RTSException 	
	*/
	public Vector qryPaymentCountyWide(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPaymentCountyWide - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		lsQry.append(" SELECT "
				+ "   A.FEESOURCECD, "
				+ "   A.PYMNTTYPECD, "
				+ "   C.PYMNTTYPECDDESC, "
				+ "   SUM ( A.PYMNTTYPEQTY ) AS PYMNTTYPEQTY, "
				+ "   SUM ( A.TOTALPYMNTTYPEAMT ) AS TOTALPYMNTTYPEAMT "
				+ " FROM RTS.RTS_PYMNT_SUMMARY A, "
				+ "   RTS.RTS_PAYMENT_TYPE C "
				+ " WHERE "
				+ "   A.OFCISSUANCENO = ? AND  "
				+ "   A.SUMMARYEFFDATE = ? AND "
				+ "   A.PYMNTTYPECD = C.PYMNTTYPECD "
				+ " GROUP BY "
				+ "   C.PYMNTTYPECDDESC, "
				+ "   A.PYMNTTYPECD, "
				+ "   A.FEESOURCECD "
				+ " ORDER BY C.PYMNTTYPECDDESC, A.FEESOURCECD ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentCountyWide - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentCountyWide - SQL - End");
			while (laQry.next())
			{
				PaymentSummaryReportData laPaymentSummaryReportData =
					new PaymentSummaryReportData();
				laPaymentSummaryReportData.setFeeSourceCd(
					caDA.getIntFromDB(laQry, "FeeSourceCd"));
				laPaymentSummaryReportData.setPymntTypeCd(
					caDA.getIntFromDB(laQry, "PymntTypeCd"));
				laPaymentSummaryReportData.setPymntTypeCdDesc(
					caDA.getStringFromDB(laQry, "PymntTypeCdDesc"));
				laPaymentSummaryReportData.setPymntTypeQty(
					caDA.getIntFromDB(laQry, "PymntTypeQty"));
				laPaymentSummaryReportData.setTotalPymntTypeAmt(
					caDA.getDollarFromDB(laQry, "TotalPymntTypeAmt"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentSummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryPaymentCountyWide - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPaymentCountyWide - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	* Method to Query PaymentSummary for given Substation 
	*    (PYMSUMC3 - RTS I) 
	* 
	* @param aaGeneralSearchData GeneralSearchData
	* @return Vector
	* @throws RTSException
	*/
	public Vector qryPaymentSubstationSummary(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryPaymentSubstationSummary - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laQry;
		lsQry.append(" SELECT  A.SUBSTAID, "
				+ "   A.FEESOURCECD, "
				+ "   A.PYMNTTYPECD, "
				+ "   C.PYMNTTYPECDDESC, "
				+ "   A.PYMNTTYPEQTY, "
				+ "   A.TOTALPYMNTTYPEAMT "
				+ " FROM "
				+ "   RTS.RTS_PYMNT_SUMMARY A, "
				+ "   RTS.RTS_PAYMENT_TYPE C "
				+ " WHERE  "
				+ " A.OFCISSUANCENO = ? AND "
				+ " A.SUBSTAID = ? AND "
				+ " A.SUMMARYEFFDATE = ? AND  "
				+ " A.PYMNTTYPECD = C.PYMNTTYPECD "
				+ " ORDER BY C.PYMNTTYPECDDESC, A.FEESOURCECD  ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentSubstationSummary - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentSubstationSummary - SQL - End");
			while (laQry.next())
			{
				PaymentSummaryReportData laPaymentSummaryReportData =
					new PaymentSummaryReportData();
				laPaymentSummaryReportData.setSubstaId(
					caDA.getIntFromDB(laQry, "SubStaId"));
				laPaymentSummaryReportData.setFeeSourceCd(
					caDA.getIntFromDB(laQry, "FeeSourceCd"));
				laPaymentSummaryReportData.setPymntTypeCd(
					caDA.getIntFromDB(laQry, "PymntTypeCd"));
				laPaymentSummaryReportData.setPymntTypeCdDesc(
					caDA.getStringFromDB(laQry, "PymntTypeCdDesc"));
				laPaymentSummaryReportData.setPymntTypeQty(
					caDA.getIntFromDB(laQry, "PymntTypeQty"));
				laPaymentSummaryReportData.setTotalPymntTypeAmt(
					caDA.getDollarFromDB(laQry, "TotalPymntTypeAmt"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentSummaryReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryPaymentSubstationSummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPaymentSubstationSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Retrieve Summary Data for selected Substation
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qrySubstaSummaryCountyWide(
		GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySubstaSummaryCountyWide - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		ResultSet laQry;
		Vector lvValues = new Vector();
		lsQry.append(
			"SELECT "
				+ "SummaryTimestmp "
				+ "FROM RTS.RTS_SUBSTA_SUMMARY "
				+ "Where OfcIssuanceNo = ?"
				+ " and  Substaid = ?"
				+ " and  SummaryEffDate = ?");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			Log.write(
				Log.SQL,
				this,
				" - qrySubstaSummaryCountyWide - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySubstaSummaryCountyWide - SQL - End");
			while (laQry.next())
			{
				SubstationSummaryData laSubstationSummaryData =
					new SubstationSummaryData();
				laSubstationSummaryData.setSummaryTimestmp(
					caDA.getRTSDateFromDB(laQry, "SummaryTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laSubstationSummaryData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySubstaSummaryCountyWide - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstaSummaryCountyWide - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
