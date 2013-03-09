package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InventorySummaryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * InventorySummary.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/29/2001	Added SQL for SubstaSummary
 * K Harrell	06/12/2002  Added OfcIssuanceno to qryReuseInventory
 * R Hicks		07/12/2002  Add call to closeLastStatement() after a 
 * 							query
 * K Harrell	10/18/2002  Substation Summary Performance
 * 							defect 	4912
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify insInventorySummaryFromTrInvDetail
 * 							save insInventorySummaryFromTrInvDetailOld
 *							Throughout class, changed caDA to  
 *							DataBaseAccess for convertToString.  
 *							Ver 5.2.0
 * K Harrell	11/03/2005	Java 1.4 Work
 *							delete insInventorySummaryFromTrInvDetailOld()
 *							delete qryInventorySummary() 
 *							defect 7899 Ver 5.2.3
 * K Harrell	12/09/2005	Add "ALL" to UNION
 * 							modify insInventorySummaryFromTrInvDetail()
 * 							defect 8462 Ver 5.2.2 Fix 8   
 * K Harrell	03/19/2010	Remove unused method. 
 * 							Note: No delete needed as use Foreign Key 
 * 							 on RTS_SUBSTA_SUMMARY w/ cascade delete. 
 * 							delete delInventorySummary() 
 * 							defect 10239 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database 
 * 
 * @version	POS_640		03/19/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	09/17/2001 
 */
public class InventorySummary extends InventorySummaryData
{
	DatabaseAccess caDA;

	/**
	 * InventorySummary constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public InventorySummary(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	// defect 10239 
	//	/**
	//	 * Method to Delete from RTS.RTS_INV_SUMMARY
	//	 * 
	//	 * @param aaInventorySummaryData  InventorySummaryData	
	//	 * @throws RTSException
	//	 */
	//	public void delInventorySummary(InventorySummaryData aaInventorySummaryData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, "delInventorySummary - Begin");
	//
	//		Vector lvValues = new Vector();
	//
	//		String lsDel =
	//			"DELETE FROM RTS.RTS_INV_SUMMARY "
	//				+ "WHERE "
	//				+ "OfcIssuanceNo = ? AND "
	//				+ "SubstaId = ? AND "
	//				+ "SummaryEffDate = ? AND "
	//				+ "ItmCd = ? AND "
	//				+ "InvItmYr = ? ";
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInventorySummaryData.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInventorySummaryData.getSubstaId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInventorySummaryData.getSummaryEffDate())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaInventorySummaryData.getItmCd())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInventorySummaryData.getInvItmYr())));
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"delInventorySummary - SQL - Begin");
	//			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
	//			Log.write(Log.SQL, this, "delInventorySummary - SQL - End");
	//			Log.write(Log.METHOD, this, "delInventorySummary - End");
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"delInventorySummary - Exception - "
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	} //END OF Delete METHOD
	// end defect 10239 

	/**
	 * Method to Insert into RTS.RTS_INV_SUMMARY
	 *
	 * @param  aaInventorySummaryData  InventorySummaryData	
	 * @throws RTSException 
	 */
	public void insInventorySummary(InventorySummaryData aaInventorySummaryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insInventorySummary - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_INV_SUMMARY ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "SummaryEffDate,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "TotalItmQtySold,"
				+ "TotalItmQtyVoid,"
				+ "TotalItmQtyReuse ) VALUES ( "
				+ " ?,"
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
						aaInventorySummaryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSummaryEffDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getTotalItmQtySold())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getTotalItmQtyVoid())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getTotalItmQtyReuse())));

			Log.write(
				Log.SQL,
				this,
				"insInventorySummary - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insInventorySummary - SQL - End");
			Log.write(Log.METHOD, this, "insInventorySummary - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInventorySummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	* Method to Insert into RTS.RTS_INV_SUMMARY from TR_INV_DETAIL
	* RTS I - INVSUMI2
	*
	* @param  aaInventorySummaryData  InventorySummaryData	
	* @throws RTSException 
	*/
	public void insInventorySummaryFromTrInvDetail(InventorySummaryData aaInventorySummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insInventorySummaryFromTrInvDetail - Begin");

		String csSummaryEffDate =
			Integer.toString(
				aaInventorySummaryData.getSummaryEffDate());

		Vector lvValues = new Vector();
		// defect  8462
		// Add "ALL" to UNION  
		String lsIns =
			" INSERT INTO RTS.RTS_INV_SUMMARY "
				+ " ( "
				+ " OFCISSUANCENO, "
				+ " SUBSTAID, "
				+ " SUMMARYEFFDATE, "
				+ " ITMCD, "
				+ " INVITMYR, "
				+ " TOTALITMQTYSOLD, "
				+ " TOTALITMQTYVOID, "
				+ " TOTALITMQTYREUSE, "
				+ " TOTALITMQTYREPRNT) "
				+ " SELECT "
				+ " P.OFCISSUANCENO, "
				+ " P.SUBSTAID, "
				+ " P.SUMMARYEFFDATE, "
				+ " P.ITMCD, "
				+ " P.INVITMYR, "
				+ " SUM(P.TOTALITMQTYSOLD), "
				+ " SUM(P.TOTALITMQTYVOID), "
				+ " SUM(P.TOTALITMQTYREUSE), "
				+ " SUM(P.TOTALITMQTYREPRNT)"
				+ " FROM "
				+ " (SELECT "
				+ " A.OfcIssuanceno as OFCISSUANCENO, "
				+ " A.SubstaId AS SUBSTAID, "
				+ csSummaryEffDate
				+ " AS SUMMARYEFFDATE "
				+ "   , A.ITMCD AS ITMCD, "
				+ "   A.INVITMYR AS INVITMYR, "
				+ "   SUM(A.INVQTY) as TotalItmQtySold, "
				+ "   0 as TotalItmQtyVoid, "
				+ "   0 as TotalItmQtyReUse, "
				+ "   SUM(A.PRNTINVQTY-1) AS TOTALITMQTYREPRNT "
				+ "  FROM "
				+ "   RTS.RTS_TR_INV_DETAIL A, "
				+ "   RTS.RTS_TRANS_HDR     B "
				+ " WHERE "
				+ " (A.DELINVREASNCD = 0 OR A.DELINVREASNCD IS NULL) "
				+ " AND "
				+ " A.INVLOCIDCD <> 'V' "
				+ " AND A.PRNTINVQTY > 0 "
				+ " AND "
				+ " A.OfcIssuanceNo  = ? AND "
				+ " A.SubStaId       = ? AND "
				+ " A.OfcIssuanceNo  = B.OfcIssuanceNo  AND "
				+ " A.SubStaId       = B.SubstaId       AND "
				+ " A.TRANSWSID      = B.TRANSWSID      AND "
				+ " A.TRANSAMDATE   = B.TRANSAMDATE AND "
				+ " A.CUSTSEQNO      = B.CUSTSEQNO  AND "
				+ " B.FEESOURCECD <> 0 AND "
				+ " B.TRANSTIMESTMP  >= "
				+ " (SELECT MIN(CLOSEOUTBEGTSTMP) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY K "
				+ "    WHERE "
				+ " B.OfcIssuanceNo  = K.OfcIssuanceNo  AND "
				+ " B.SubStaId       = K.SubstaId       AND "
				+ "    B.CASHWSID = K.CASHWSID AND "
				+ "    K.SUMMARYEFFDATE= "
				+ csSummaryEffDate
				+ ") "
				+ " AND "
				+ " B.TRANSTIMESTMP  <= "
				+ "   (SELECT MAX(CLOSEOUTENDTSTMP) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY K "
				+ "    WHERE "
				+ " B.OfcIssuanceNo  = K.OfcIssuanceNo  AND "
				+ " B.SubStaId       = K.SubstaId       AND "
				+ "    B.CASHWSID = K.CASHWSID AND "
				+ "     K.SUMMARYEFFDATE ="
				+ csSummaryEffDate
				+ ") "
				+ " GROUP BY A.OfcIssuanceNo,A.SubstaId,"
				+ " A.ITMCD,A.INVITMYR "
				+ " UNION ALL SELECT "
				+ " A.OfcIssuanceno AS OFCISSUANCENO, "
				+ " A.SubstaId AS SUBSTAID, "
				+ csSummaryEffDate
				+ " AS SUMMARYEFFDATE "
				+ "   , A.ITMCD AS ITMCD, "
				+ "   A.INVITMYR AS INVITMYR, "
				+ "   SUM(A.INVQTY) as TotalItmQtySold, "
				+ "   0 as TotalItmQtyVoid, "
				+ "   0 as TotalItmQtyReUse, "
				+ "   0 AS TOTALITMQTYREPRNT "
				+ "  FROM "
				+ "   RTS.RTS_TR_INV_DETAIL A, "
				+ "   RTS.RTS_TRANS_HDR     B "
				+ " WHERE "
				+ " (A.DELINVREASNCD = 0 OR A.DELINVREASNCD IS NULL) "
				+ " AND "
				+ " A.INVLOCIDCD <> 'V' "
				+ " AND "
				+ " A.PRNTINVQTY = 0 AND "
				+ " A.OfcIssuanceNo  = ? AND "
				+ " A.SubStaId       = ? AND "
				+ " A.OfcIssuanceNo  = B.OfcIssuanceNo  AND "
				+ " A.SubStaId       = B.SubstaId       AND "
				+ " A.TRANSWSID      = B.TRANSWSID      AND "
				+ " A.TRANSAMDATE   = B.TRANSAMDATE AND "
				+ " A.CUSTSEQNO      = B.CUSTSEQNO  AND "
				+ " B.FEESOURCECD <> 0 AND "
				+ " B.TRANSTIMESTMP  >= "
				+ " (SELECT MIN(CLOSEOUTBEGTSTMP) "
				+ " FROM RTS.RTS_CLOSEOUT_HSTRY K "
				+ "    WHERE "
				+ " B.OfcIssuanceNo  = K.OfcIssuanceNo  AND "
				+ " B.SubStaId       = K.SubstaId       AND "
				+ "    B.CASHWSID = K.CASHWSID AND "
				+ "    K.SUMMARYEFFDATE= "
				+ csSummaryEffDate
				+ ") "
				+ " AND "
				+ " B.TRANSTIMESTMP  <= "
				+ "   (SELECT MAX(CLOSEOUTENDTSTMP) FROM RTS.RTS_CLOSEOUT_HSTRY K "
				+ "    WHERE "
				+ " B.OfcIssuanceNo  = K.OfcIssuanceNo  AND "
				+ " B.SubStaId       = K.SubstaId       AND "
				+ "    B.CASHWSID = K.CASHWSID AND "
				+ "     K.SUMMARYEFFDATE ="
				+ csSummaryEffDate
				+ ") "
				+ " GROUP BY A.OfcIssuanceNo,A.SubstaId,A.ITMCD,A.INVITMYR) "
				+ " AS P GROUP BY OFCISSUANCENO, SUBSTAID, "
				+ " SUMMARYEFFDATE, ITMCD, INVITMYR";
		// end defect 8462 
		try
		{
			for (int i = 0; i < 2; i++)
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventorySummaryData
								.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventorySummaryData.getSubstaId())));
			}

			Log.write(
				Log.SQL,
				this,
				"insInventorySummaryFromTrInvDetail - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insInventorySummaryFromTrInvDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insInventorySummaryFromTrInvDetail - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInventorySummaryFromTrInvDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	// defect 7899 
	//	/**
	//	 * Method to Query RTS.RTS_INV_SUMMARY
	//	 *
	//	 * @param  aaInventorySummaryData  InventorySummaryData	
	//	 * @return Vector
	//	 * @throws RTSException
	//	 * @deprecated  
	//	 */
	//	public Vector qryInventorySummary(InventorySummaryData aaInventorySummaryData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - qryInventorySummary - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		Vector lvRslt = new Vector();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ "OfcIssuanceNo,"
	//				+ "SubstaId,"
	//				+ "SummaryEffDate,"
	//				+ "ItmCd,"
	//				+ "InvItmYr,"
	//				+ "TotalItmQtySold,"
	//				+ "TotalItmQtyVoid,"
	//				+ "TotalItmQtyReuse "
	//				+ "FROM RTS.RTS_INV_SUMMARY ");
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryInventorySummary - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryInventorySummary - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				InventorySummaryData laInventorySummaryData =
	//					new InventorySummaryData();
	//				laInventorySummaryData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	//				laInventorySummaryData.setSubstaId(
	//					caDA.getIntFromDB(lrsQry, "SubstaId"));
	//				laInventorySummaryData.setSummaryEffDate(
	//					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
	//				laInventorySummaryData.setItmCd(
	//					caDA.getStringFromDB(lrsQry, "ItmCd"));
	//				laInventorySummaryData.setInvItmYr(
	//					caDA.getIntFromDB(lrsQry, "InvItmYr"));
	//				laInventorySummaryData.setTotalItmQtySold(
	//					caDA.getIntFromDB(lrsQry, "TotalItmQtySold"));
	//				laInventorySummaryData.setTotalItmQtyVoid(
	//					caDA.getIntFromDB(lrsQry, "TotalItmQtyVoid"));
	//				laInventorySummaryData.setTotalItmQtyReuse(
	//					caDA.getIntFromDB(lrsQry, "TotalItmQtyReuse"));
	//				// Add element to the Vector
	//				lvRslt.addElement(laInventorySummaryData);
	//			} //End of While
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryInventorySummary - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryInventorySummary - Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 7899 

	/**
	 * Method to Query RTS.RTS_TR_INV_DETAIL for insert 
	 * into RTS.RTS_INV_SUMMARY 
	 * RTS I - TRINVDCJ
	 *
	 * @param  aaInventorySummaryData  InventorySummaryData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryReuseInventoryForInventorySummary(InventorySummaryData aaInventorySummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryReuseInventoryForInventorySummary - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		String csSummaryEffDate =
			Integer.toString(
				aaInventorySummaryData.getSummaryEffDate());

		// defect 4912 
		// Substation Summary Performance
		ResultSet lrsQry;

		lsQry.append(
			" SELECT "
				+ "   A.OFCISSUANCENO, "
				+ "   A.SUBSTAID, "
				+ csSummaryEffDate
				+ "  as SummaryEffDate ,A.ITMCD, "
				+ "   A.INVITMYR, "
				+ "   COUNT(*) as TotalItmQtyReuse "
				+ "  FROM "
				+ "   RTS.RTS_TR_INV_DETAIL A, "
				+ "   RTS.RTS_TRANS_HDR        B "
				+ " WHERE "
				+ " B.OfcIssuanceNo = ? AND "
				+ "  B.SubstaId  = ? AND  "
				+ "  B.SummaryEffDate  = ? AND  "
				+ " A.OFCISSUANCENO  = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID       = B.SUBSTAID      AND "
				+ " A.TRANSAMDATE    = B.TRANSAMDATE   AND "
				+ " A.TRANSWSID      = B.TRANSWSID     AND "
				+ " A.CUSTSEQNO      = B.CUSTSEQNO     AND "
				+ " INVLOCIDCD = 'V' AND "
				+ " A.TRANSCD NOT IN ('INVDEL' ,'INVVD') "
				+ " GROUP BY A.OFCISSUANCENO,A.SUBSTAID,"
				+ " A.ITMCD,A.INVITMYR ");
		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSubstaId())));
			// 3 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				" - qryReuseInventoryForInventorySummary - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryReuseInventoryForInventorySummary - SQL - End");

			while (lrsQry.next())
			{
				InventorySummaryData laInventorySummaryData =
					new InventorySummaryData();
				laInventorySummaryData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventorySummaryData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventorySummaryData.setSummaryEffDate(
					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
				laInventorySummaryData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventorySummaryData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventorySummaryData.setTotalItmQtyReuse(
					caDA.getIntFromDB(lrsQry, "TotalItmQtyReuse"));
				// Add element to the Vector
				lvRslt.addElement(laInventorySummaryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryReuseInventoryForInventorySummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryReuseInventoryForInventorySummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Query RTS.RTS_INV_SUMMARY
	 * RTS I - TRINVDCI
	 *
	 * @param  aaInventorySummaryData  InventorySummaryData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryVoidInventoryForInventorySummary(InventorySummaryData aaInventorySummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryVoidInventoryForInventorySummary - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		String csSummaryEffDate =
			Integer.toString(
				aaInventorySummaryData.getSummaryEffDate());

		// KPH (2002.10.18): CQU10004912 Substation Summary Performance
		ResultSet lrsQry;

		lsQry.append(
			" SELECT "
				+ "   A.OFCISSUANCENO, "
				+ "   A.SUBSTAID,  "
				+ csSummaryEffDate
				+ "  as SummaryEffDate   ,"
				+ "   B.ITMCD, "
				+ "   B.INVITMYR, "
				+ "   SUM(B.INVQTY) as TotalItmQtyVoid  "
				+ "  FROM "
				+ "   RTS.RTS_TRANS_HDR   A, "
				+ "   RTS.RTS_TR_INV_DETAIL B "
				+ " WHERE "
				+ " A.OfcIssuanceNo  = ?  AND "
				+ " A.SubstaId       = ? AND "
				+ " A.SummaryEffDate = ? AND "
				+ " A.OfcIssuanceNo  = B.OfcIssuanceNo  AND "
				+ " A.SubstaId       = B.SubstaId       AND "
				+ " A.TRANSWSID      = B.TRANSWSID      AND "
				+ " A.TRANSAMDATE    = B.TRANSAMDATE    AND "
				+ " A.CUSTSEQNO      = B.CUSTSEQNO      AND "
				+ " B.TRANSCD IN ('INVDEL','INVVD') "
				+ " AND "
				+ " b.DELINVREASNCD = 5 "
				+ " GROUP BY A.OFCISSUANCENO,A.SUBSTAID, "
				+ " B.ITMCD,B.INVITMYR ");
		try
		{
			// 1 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSubstaId())));
			// 3 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				" - qryVoidInventoryForInventorySummary - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryVoidInventoryForInventorySummary - SQL - End");

			while (lrsQry.next())
			{
				InventorySummaryData laInventorySummaryData =
					new InventorySummaryData();
				laInventorySummaryData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventorySummaryData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventorySummaryData.setSummaryEffDate(
					caDA.getIntFromDB(lrsQry, "SummaryEffDate"));
				laInventorySummaryData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventorySummaryData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventorySummaryData.setTotalItmQtyVoid(
					caDA.getIntFromDB(lrsQry, "TotalItmQtyVoid"));
				// Add element to the Vector
				lvRslt.addElement(laInventorySummaryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryVoidInventoryForInventorySummary - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryVoidInventoryForInventorySummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_INV_SUMMARY
	 * RTS I - INVSUMU1
	 * 
	 * @param  aaInventorySummaryData  InventorySummaryData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updInventorySummary(InventorySummaryData aaInventorySummaryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updInventorySummary - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_INV_SUMMARY SET "
				+ "TotalItmQtyVoid = TotalItmQtyVoid + ?, "
				+ "TotalItmQtyReuse = TotalItmQtyReUse + ? "
				+ "WHERE OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "SummaryEffDate = ? AND "
				+ "ItmCd = ? AND "
				+ "InvItmYr = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getTotalItmQtyVoid())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getTotalItmQtyReuse())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getSummaryEffDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventorySummaryData.getInvItmYr())));

			Log.write(
				Log.SQL,
				this,
				"updInventorySummary - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updInventorySummary - SQL - End");
			Log.write(Log.METHOD, this, "updInventorySummary - End");
			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventorySummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
