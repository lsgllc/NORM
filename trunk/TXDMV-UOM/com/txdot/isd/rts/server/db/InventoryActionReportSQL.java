package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.ReprintData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * InventoryActionReportSQL.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/28/2001  Created 
 * K Harrell    08/19/2003  Incorrect qualifier on not exists
 *                          method qryIARPartF   
 * 							defect 6504. Ver 5.1.5 Fix 2
 * K Harrell	01/29/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add qryIARPartJ(), qryIARPartK()
 * 							Ver 5.2.0
 * K Harrell	10/10/2004	Assign substaid  for Part K
 *							modify qryIARPartK()
 *							defect 7606  Ver 5.2.1
 * K Harrell	10/10/2004	Pass only one date for Part J
 *							modify qryIARPartJ()
 *							defect 7607  Ver 5.2.1
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Ray Rowehl	09/30/2005	Moved report data classes to services.data.
 * 							defect 7890 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */
 
/** 
 * Class provides all SQL calls for the Inventory Action Report
 * 
 * @version	5.2.3			09/30/2005
 * @author	Kathy Harrell 
 * <br>Creation Date:		09/28/2001
 */

public class InventoryActionReportSQL
{
	DatabaseAccess caDA;

	public InventoryActionReportSQL(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Retrieve Data for Part A & Part B
	 * TRINVDCE: PART A: SQL To select Issued and not removed from 
	 *  Inventory(U)
	 * TRINVDCE: PART B: SQL to select Issued and not removed from 
	 *  Inventory(V)
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector
	 * @throws RTSExeption 	
	 */
	public Vector qryIARPartAB(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartAB - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry
			.append(
				"SELECT D.OFCISSUANCENO, A.TRANSWSID, A.TRANSAMDATE, "
				+ " A.TRANSTIME, A.ITMCD, A.INVITMYR, A.INVENDNO as InvItmEndNo, "
				+ " A.INVQTY, A.INVITMNO, A.INVID, A.INVLOCIDCD, "
				+ " A.TRANSCD,B.ITMCDDESC, "
				+ " C.TRANSEMPID "
				+ " FROM RTS.RTS_TR_INV_DETAIL A, "
				+ " RTS.RTS_ITEM_CODES B, "
				+ " RTS.RTS_TRANS C, "
				+ " RTS.RTS_TRANS_HDR D "
				+ " WHERE "
				+ " A.OFCISSUANCENO =  ?           AND "
				+ " A.SUBSTAID      =  ?           AND "
				+ " A.TRANSAMDATE = ?              AND "
				+ " A.INVLOCIDCD = ?               AND "
				+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND " //TRINVDETAIL:TRANS
		+" A.SUBSTAID      = C.SUBSTAID      AND "
			+ " A.TRANSWSID = C.TRANSWSID AND "
			+ " A.TRANSAMDATE = C.TRANSAMDATE AND "
			+ " A.TRANSTIME = C.TRANSTIME AND "
			+ " A.OFCISSUANCENO = D.OFCISSUANCENO AND " //TRINVDETAIL:TRANS_HDR
		+" A.SUBSTAID      = D.SUBSTAID      AND "
			+ " A.TRANSAMDATE = D.TRANSAMDATE AND "
			+ " A.TRANSWSID = D.TRANSWSID AND "
			+ " A.CUSTSEQNO = D.CUSTSEQNO AND "
			+ " D.TRANSTIMESTMP IS NOT NULL AND " //Complete
		+" C.VOIDEDTRANSINDI <> 1  AND "
			+ " A.ITMCD = B.ITMCD  AND "
			+ " A.DELINVREASNCD = 0 "
			+ " ORDER BY "
			+ "  A.ITMCD, A.INVITMYR, A.INVITMNO  ");
		try
		{
			//Set 1
			//OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			//SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			//TransAMDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						Integer.parseInt(
							aaGeneralSearchData.getKey2()))));
			//InvLocIdCd
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getKey1())));
			Log.write(Log.SQL, this, " - qryIARPartAB - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartAB - SQL - End");
			while (laResultSet.next())
			{
				InventoryActionReportData laInventoryActionReportData =
					new InventoryActionReportData();
				laInventoryActionReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OfcIssuanceNo"));
				laInventoryActionReportData.setTransWsId(
					caDA.getIntFromDB(laResultSet, "TransWsId"));
				laInventoryActionReportData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, "TransAMDate"));
				laInventoryActionReportData.setTransTime(
					caDA.getIntFromDB(laResultSet, "TransTime"));
				laInventoryActionReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryActionReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryActionReportData.setInvItmEndNo(
					caDA.getStringFromDB(laResultSet, "InvItmEndNo"));
				laInventoryActionReportData.setInvQty(
					caDA.getIntFromDB(laResultSet, "InvQty"));
				laInventoryActionReportData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, "InvItmNo"));
				laInventoryActionReportData.setInvId(
					caDA.getStringFromDB(laResultSet, "InvId"));
				laInventoryActionReportData.setInvLocIdCd(
					caDA.getStringFromDB(laResultSet, "InvLocIdCd"));
				laInventoryActionReportData.setTransCd(
					caDA.getStringFromDB(laResultSet, "TransCd"));
				laInventoryActionReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				laInventoryActionReportData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, "TransEmpId"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryActionReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartAB - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartAB - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query InventoryActionReport
	 * TRINVDCN:  PART C: SQL to select Inventory whose transactions 
	 * were voided
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryIARPartC(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartC - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry
			.append(
				" SELECT "
				+ " A.VOIDOFCISSUANCENO, A.VOIDTRANSWSID, "
				+ " A.VOIDTRANSAMDATE, "
				+ " A.VOIDTRANSTIME, "
				+ " D.OFCISSUANCENO, A.TRANSWSID, A.TRANSAMDATE, "
				+ " A.TRANSTIME, "
				+ " A.TRANSEMPID, "
				+ " B.ITMCD, B.INVITMYR, B.INVENDNO as InvItmEndNo, "
				+ " B.INVQTY, B.INVITMNO, B.INVID, B.INVLOCIDCD, "
				+ " B.TRANSCD,C.ITMCDDESC FROM "
				+ " RTS.RTS_TRANS A, "
				+ " RTS.RTS_TR_INV_DETAIL B, "
				+ " RTS.RTS_ITEM_CODES C, "
				+ " RTS.RTS_TRANS_HDR D "
				+ " WHERE "
				+ " A.OFCISSUANCENO  = ?  AND "
				+ " A.SUBSTAID   = ?  AND "
				+ " A.TRANSAMDATE = ?  AND "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND " //Trans:TrInvDetail
		+" A.SUBSTAID  = B.SUBSTAID  AND "
			+ " A.TRANSWSID = B.TRANSWSID AND "
			+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
			+ " A.TRANSTIME =   B.TRANSTIME AND "
			+ " A.OFCISSUANCENO = D.OFCISSUANCENO AND " //Trans:TransHdr
		+" A.SUBSTAID  =     D.SUBSTAID  AND "
			+ " A.TRANSWSID =     D.TRANSWSID AND "
			+ " A.TRANSAMDATE =   D.TRANSAMDATE AND "
			+ " A.CUSTSEQNO =     D.CUSTSEQNO AND "
			+ " D.TRANSTIMESTMP IS NOT NULL AND " //Complete
		+" B.ITMCD = C.ITMCD AND "
			+ " B.DELINVREASNCD = 5  "
			+ " ORDER BY "
			+ " B.ITMCD, "
			+ " B.INVITMYR, "
			+ " B.INVITMNO, "
			+ " A.VOIDTRANSWSID, "
			+ " A.VOIDTRANSAMDATE, "
			+ " A.VOIDTRANSTIME  ");
		try
		{
			//Set 1
			//OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			//SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			//TransAMDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						Integer.parseInt(
							aaGeneralSearchData.getKey2()))));
			Log.write(Log.SQL, this, " - qryIARPartC - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartC - SQL - End");
			while (laResultSet.next())
			{
				InventoryActionReportData laInventoryActionReportData =
					new InventoryActionReportData();
				laInventoryActionReportData.setVoidOfcIssuanceNo(
					caDA.getIntFromDB(
						laResultSet,
						"VoidOfcIssuanceNo"));
				laInventoryActionReportData.setVoidTransWsId(
					caDA.getIntFromDB(laResultSet, "VoidTransWsId"));
				laInventoryActionReportData.setVoidTransAMDate(
					caDA.getIntFromDB(laResultSet, "VoidTransAMDate"));
				laInventoryActionReportData.setVoidTransTime(
					caDA.getIntFromDB(laResultSet, "VoidTransTime"));
				laInventoryActionReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OfcIssuanceNo"));
				laInventoryActionReportData.setTransWsId(
					caDA.getIntFromDB(laResultSet, "TransWsId"));
				laInventoryActionReportData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, "TransAMDate"));
				laInventoryActionReportData.setTransTime(
					caDA.getIntFromDB(laResultSet, "TransTime"));
				laInventoryActionReportData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, "TransEmpId"));
				laInventoryActionReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryActionReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryActionReportData.setInvItmEndNo(
					caDA.getStringFromDB(laResultSet, "InvItmEndNo"));
				laInventoryActionReportData.setInvQty(
					caDA.getIntFromDB(laResultSet, "InvQty"));
				laInventoryActionReportData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, "InvItmNo"));
				laInventoryActionReportData.setInvId(
					caDA.getStringFromDB(laResultSet, "InvId"));
				laInventoryActionReportData.setInvLocIdCd(
					caDA.getStringFromDB(laResultSet, "InvLocIdCd"));
				laInventoryActionReportData.setTransCd(
					caDA.getStringFromDB(laResultSet, "TransCd"));
				laInventoryActionReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryActionReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartC - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartC - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Query Inventory Action Report PartDE
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryIARPartDE(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartDE - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry.append(
			"SELECT A.ITMCD, A.INVITMYR, A.INVITMNO, A.INVID, "
				+ " A.INVLOCIDCD, A.INVITMENDNO, A.INVQTY, A.PATRNSEQNO, "
				+ " A.INVSTATUSCD, A.PATRNSEQCD, B.ITMCDDESC FROM "
				+ " RTS.RTS_INV_ALLOCATION A, "
				+ " RTS.RTS_ITEM_CODES B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ?  AND "
				+ " A.SUBSTAID      = ?  AND "
				+ " A.INVSTATUSCD = ?  AND "
				+ " A.ITMCD = B.ITMCD "
				+ " ORDER BY A.ITMCD, A.INVITMYR, A.INVITMNO ");
		try
		{
			//Set 1
			//OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			//SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			//InvStatusCd 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey3())));
			Log.write(Log.SQL, this, " - qryIARPartDE - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartDE - SQL - End");
			while (laResultSet.next())
			{
				InventoryActionReportData laInventoryActionReportData =
					new InventoryActionReportData();
				laInventoryActionReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryActionReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryActionReportData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, "InvItmNo"));
				laInventoryActionReportData.setInvId(
					caDA.getStringFromDB(laResultSet, "InvId"));
				laInventoryActionReportData.setInvLocIdCd(
					caDA.getStringFromDB(laResultSet, "InvLocIdCd"));
				laInventoryActionReportData.setInvItmEndNo(
					caDA.getStringFromDB(laResultSet, "InvItmEndNo"));
				laInventoryActionReportData.setInvQty(
					caDA.getIntFromDB(laResultSet, "InvQty"));
				laInventoryActionReportData.setPatrnSeqNo(
					caDA.getIntFromDB(laResultSet, "PatrnSeqNo"));
				laInventoryActionReportData.setInvStatusCd(
					caDA.getIntFromDB(laResultSet, "InvStatusCd"));
				laInventoryActionReportData.setPatrnSeqCd(
					caDA.getIntFromDB(laResultSet, "PatrnSeqCd"));
				laInventoryActionReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryActionReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartDE - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartDE - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Query Inventory Action Report Part F
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryIARPartF(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartF - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		// Inventory Profile & Inventory Allocation
		lsQry
			.append(
				"SELECT  A.MINQTY,A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, "
				+ " SUM(B.INVQTY) AS InvQty, B.INVID, F.ITMCDDESC "
				+ " FROM RTS.RTS_INV_PROFILE A, RTS.RTS_INV_ALLOCATION B,  "
				+ " RTS.RTS_ITEM_CODES F "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ?  AND " //Set 1
		+" A.SUBSTAID      = ?  AND "
			+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
			+ " A.SUBSTAID      = B.SUBSTAID      AND "
			+ " A.DELETEINDI = 0 AND "
			+ " A.ENTITY = B.INVLOCIDCD AND "
			+ " A.ID = B.INVID AND "
			+ " A.ITMCD = B.ITMCD AND "
			+ " A.INVITMYR = B.INVITMYR AND "
			+ " A.ITMCD = F.ITMCD "
			+ " GROUP BY A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, "
			+ " B.INVID, A.MINQTY, F.ITMCDDESC "
			+ " HAVING A.MINQTY > SUM(B.INVQTY)");
		//Set 1
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		// Inventory Profile and no Inventory Allocation
		// Defect 6504; incorrect qualifier on not exists   
		lsQry
			.append(
				" UNION "
				+ " SELECT A.MINQTY, A.ENTITY, A.ID , A.ITMCD, A.INVITMYR, "
				+ " 0 AS InvQty, A.ID as InvId, F.ITMCDDESC "
				+ " FROM RTS.RTS_INV_PROFILE A, "
				+ " RTS.RTS_ITEM_CODES F WHERE "
				+ " A.OFCISSUANCENO = ?  AND " //Set 2
		+" A.SUBSTAID      = ? AND  "
			+ " A.ID <> 'DEFAULT' AND "
			+ " A.DELETEINDI = 0 AND "
			+ " A.ITMCD = F.ITMCD AND "
			+ " A.MINQTY >0 AND "
			+ " NOT EXISTS "
			+ " (SELECT * FROM RTS.RTS_INV_ALLOCATION B WHERE "
			+ " B.OFCISSUANCENO = ?  AND " //Set 3 - Defect 6504
		+" B.SUBSTAID      = ?  AND " // used to say a.ofcissuanceno = , a.substaid = 
		+" A.ENTITY = B.INVLOCIDCD AND "
			+ " A.ID = B.INVID AND "
			+ " A.ITMCD = B.ITMCD AND "
			+ " A.INVITMYR = B.INVITMYR) ");
		// End Defect 6504   
		//Set 2
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		//Set 3
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		//Inventory Profile = Default & Inventory Allocation
		lsQry
			.append(
				" UNION "
				+ " SELECT A.MINQTY, A.ENTITY, A.ID , A.ITMCD, A.INVITMYR, "
				+ " SUM(B.INVQTY) AS InvQty, B.INVID, F.ITMCDDESC "
				+ " FROM RTS.RTS_INV_PROFILE A, RTS.RTS_INV_ALLOCATION B, "
				+ " RTS.RTS_ITEM_CODES F "
				+ " WHERE"
				+ " A.OFCISSUANCENO =? AND " // Set 4
		+" A.SUBSTAID = ? AND "
			+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
			+ " A.SUBSTAID = B.SUBSTAID AND "
			+ " A.ID = 'DEFAULT'AND "
			+ " A.DELETEINDI = 0 AND "
			+ " A.ENTITY = B.INVLOCIDCD AND A.ITMCD = "
			+ " B.ITMCD AND A.INVITMYR = B.INVITMYR AND A.ITMCD = "
			+ " F.ITMCD AND "
			+ " NOT EXISTS "
			+ " (SELECT * FROM RTS.RTS_INV_PROFILE C "
			+ " WHERE "
			+ " C.OFCISSUANCENO = ? AND " //Set 5
		+" C.SUBSTAID = ? AND "
			+ " C.DELETEINDI = 0 AND "
			+ " B.INVID = C.ID AND "
			+ " C.ENTITY = B.INVLOCIDCD AND "
			+ " C.ITMCD = B.ITMCD AND "
			+ " C.INVITMYR = B.INVITMYR) "
			+ " GROUP BY A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, B.INVID, "
			+ " A.MINQTY, F.ITMCDDESC "
			+ " HAVING A.MINQTY > SUM(B.INVQTY) "
			+ " ORDER BY 2, 7, 4, 5 ");
		//Set 4
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		//Set 5
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		try
		{
			Log.write(Log.SQL, this, " - qryIARPartF - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartF - SQL - End");
			while (laResultSet.next())
			{
				InventoryProfileReportData laInventoryProfileReportData =
					new InventoryProfileReportData();
				laInventoryProfileReportData.setMinQty(
					caDA.getIntFromDB(laResultSet, "MinQty"));
				laInventoryProfileReportData.setEntity(
					caDA.getStringFromDB(laResultSet, "Entity"));
				laInventoryProfileReportData.setId(
					caDA.getStringFromDB(laResultSet, "Id"));
				laInventoryProfileReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryProfileReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryProfileReportData.setInvQty(
					caDA.getIntFromDB(laResultSet, "InvQty"));
				laInventoryProfileReportData.setInvId(
					caDA.getStringFromDB(laResultSet, "InvId"));
				laInventoryProfileReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryProfileReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartF - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartF - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to retrieve IAR Part G
	 * INVALPC2:  PART G: SQL  Select all InvProfile records whose 
	 * inventory is above MaxQty
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryIARPartG(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartG - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry
			.append(
				"SELECT A.MAXQTY, A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, "
				+ " SUM(B.INVQTY) as InvQty, B.INVID, F.ITMCDDESC FROM "
				+ " RTS.RTS_INV_PROFILE A, RTS.RTS_INV_ALLOCATION B, "
				+ " RTS.RTS_ITEM_CODES F "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ?  AND " //Set 1
		+" A.SUBSTAID      = ?  AND "
			+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
			+ " A.SUBSTAID      = B.SUBSTAID      AND "
			+ " A.DELETEINDI = 0 AND "
			+ " A.OfcIssuanceno = B.OfcIssuanceNo and "
		// RTS_INV_PROFILE to RTS_INV_ALLOCATION
		+" A.SubstaId = B.Substaid and "
			+ " A.ENTITY = B.INVLOCIDCD AND "
			+ " A.ID = B.INVID AND "
			+ " A.ITMCD = B.ITMCD AND "
			+ " A.INVITMYR = B.INVITMYR AND "
			+ " A.ITMCD = F.ITMCD AND A.MAXQTY IS NOT NULL AND "
			+ " A.MAXQTY > 0 "
			+ " GROUP BY A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, "
			+ " B.INVID,A.MAXQTY, F.ITMCDDESC "
			+ " HAVING A.MAXQTY < SUM(B.INVQTY) ");
		//Set 1
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		lsQry
			.append(
				" UNION "
				+ " SELECT A.MAXQTY, A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, "
				+ " SUM(B.INVQTY) as InvQty, B.INVID, F.ITMCDDESC "
				+ " FROM RTS.RTS_INV_PROFILE A, RTS.RTS_INV_ALLOCATION B, "
				+ " RTS.RTS_ITEM_CODES F "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ?  AND " //Set 2
		+" A.SUBSTAID      = ?  AND "
			+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
			+ " A.SUBSTAID      = B.SUBSTAID      AND "
			+ " A.DELETEINDI = 0 AND "
			+ " A.ID = 'DEFAULT' AND "
			+ " A.ENTITY = B.INVLOCIDCD AND "
			+ " A.ITMCD = F.ITMCD AND "
			+ " A.ITMCD = B.ITMCD AND "
			+ " A.INVITMYR = B.INVITMYR AND "
			+ " A.MAXQTY IS NOT NULL AND A.MAXQTY > 0 AND "
			+ " NOT EXISTS "
			+ " (SELECT * FROM RTS.RTS_INV_PROFILE C "
			+ " WHERE "
			+ " C.OFCISSUANCENO = ?  AND " //Set 3
		+" C.SUBSTAID      = ?  AND "
			+ " C.DELETEINDI = 0 AND "
			+ " C.ID = B.INVID AND "
			+ " C.ENTITY = B.INVLOCIDCD AND "
			+ " C.ITMCD = B.ITMCD AND "
			+ " C.INVITMYR = B.INVITMYR) "
			+ " GROUP BY A.ENTITY, A.ID, A.ITMCD, A.INVITMYR, "
			+ " B.INVID, A.MAXQTY, F.ITMCDDESC "
			+ " HAVING A.MAXQTY < SUM(B.INVQTY) "
			+ " ORDER BY 2, 7, 4, 5 ");
		//Set 2
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		//Set 3
		//OfcIssuanceNo
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		//SubstaId
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		try
		{
			Log.write(Log.SQL, this, " - qryIARPartG - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartG - SQL - End");
			while (laResultSet.next())
			{
				InventoryProfileReportData laInventoryProfileReportData =
					new InventoryProfileReportData();
				laInventoryProfileReportData.setMaxQty(
					caDA.getIntFromDB(laResultSet, "MaxQty"));
				laInventoryProfileReportData.setEntity(
					caDA.getStringFromDB(laResultSet, "Entity"));
				laInventoryProfileReportData.setId(
					caDA.getStringFromDB(laResultSet, "Id"));
				laInventoryProfileReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryProfileReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryProfileReportData.setInvQty(
					caDA.getIntFromDB(laResultSet, "InvQty"));
				laInventoryProfileReportData.setInvId(
					caDA.getStringFromDB(laResultSet, "InvId"));
				laInventoryProfileReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryProfileReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartG - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartG - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Retrieve Part H 
	 * TRINFDC4:  PART H: SQL to select all Inventory removed thru the 
	 * Inventory Delete Event
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryIARPartH(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartH - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry
			.append(
				"SELECT E.TRANSEMPID, F.OFCISSUANCENO, B.TRANSWSID, "
				+ " B.TRANSAMDATE, B.TRANSTIME, B.ITMCD, C.ITMCDDESC, "
				+ " B.INVITMYR, B.INVQTY, B.INVITMNO, B.INVENDNO as InvItmEndNo, "
				+ " D.DELINVREASN, "
				+ " E.VOIDOFCISSUANCENO, E.VOIDTRANSWSID, "
				+ " E.VOIDTRANSAMDATE, E.VOIDTRANSTIME "
				+ " FROM RTS.RTS_TR_INV_DETAIL B, "
				+ " RTS.RTS_ITEM_CODES C, "
				+ " RTS.RTS_DELETE_REASONS D, "
				+ " RTS.RTS_TRANS E, "
				+ " RTS.RTS_TRANS_HDR F "
				+ " WHERE B.TRANSCD IN ('INVDEL','INVVD')  "
				+ " AND B.OFCISSUANCENO =  ?  "
				+ " AND B.SUBSTAID      =  ?  "
				+ " AND B.TRANSAMDATE = ?     "
				+ " AND B.ITMCD = C.ITMCD "
				+ " AND B.DELINVREASNCD <> 5 "
				+ " AND F.TRANSTIMESTMP IS NOT NULL " //Complete
		+" AND B.DELINVREASNCD = D.DELINVREASNCD "
			+ " AND B.OFCISSUANCENO = E.OFCISSUANCENO "
			+ " AND B.SUBSTAID      = E.SUBSTAID      "
			+ " AND B.TRANSWSID     = E.TRANSWSID "
			+ " AND B.TRANSAMDATE   = E.TRANSAMDATE "
			+ " AND B.TRANSTIME     = E.TRANSTIME "
			+ " AND B.OFCISSUANCENO = F.OFCISSUANCENO "
			+ " AND B.SUBSTAID      = F.SUBSTAID      "
			+ " AND B.TRANSAMDATE   = F.TRANSAMDATE "
			+ " AND B.TRANSWSID     = F.TRANSWSID "
			+ " AND B.CUSTSEQNO     = F.CUSTSEQNO "
			+ " ORDER BY "
			+ " B.ITMCD, B.INVITMYR, B.INVITMNO ");
		try
		{
			//Set 1
			//OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			//SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			//TransAMDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						Integer.parseInt(
							aaGeneralSearchData.getKey2()))));
			Log.write(Log.SQL, this, " - qryIARPartH - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartH - SQL - End");
			while (laResultSet.next())
			{
				InventoryActionReportData laInventoryActionReportData =
					new InventoryActionReportData();
				laInventoryActionReportData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, "TransEmpId"));
				laInventoryActionReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OfcIssuanceNo"));
				laInventoryActionReportData.setTransWsId(
					caDA.getIntFromDB(laResultSet, "TransWsId"));
				laInventoryActionReportData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, "TransAMDate"));
				laInventoryActionReportData.setTransTime(
					caDA.getIntFromDB(laResultSet, "TransTime"));
				laInventoryActionReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryActionReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				laInventoryActionReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryActionReportData.setInvQty(
					caDA.getIntFromDB(laResultSet, "InvQty"));
				laInventoryActionReportData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, "InvItmNo"));
				laInventoryActionReportData.setInvItmEndNo(
					caDA.getStringFromDB(laResultSet, "InvItmEndNo"));
				laInventoryActionReportData.setDelInvReasn(
					caDA.getStringFromDB(laResultSet, "DelInvReasn"));
				laInventoryActionReportData.setVoidOfcIssuanceNo(
					caDA.getIntFromDB(
						laResultSet,
						"VoidOfcIssuanceNo"));
				laInventoryActionReportData.setVoidTransWsId(
					caDA.getIntFromDB(laResultSet, "VoidTransWsId"));
				laInventoryActionReportData.setVoidTransAMDate(
					caDA.getIntFromDB(laResultSet, "VoidTransAMDate"));
				laInventoryActionReportData.setVoidTransTime(
					caDA.getIntFromDB(laResultSet, "VoidTransTime"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryActionReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartH - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartH - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Retrieve IAR Part I Data
	 * TRINVDCH: PART I: SQL Select TrInvDetail which are mismatched
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector 	
	 * @throws RTSException
	 */
	public Vector qryIARPartI(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIARPartI - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry
			.append(
				"SELECT A.TRANSCD, D.OFCISSUANCENO, "
				+ " A.TRANSWSID, E.TRANSEMPID, A.TRANSAMDATE, "
				+ " A.TRANSTIME, A.INVID, A.INVLOCIDCD, A.INVITMNO, "
				+ " A.ITMCD, A.INVITMYR, B.SUBCONID, "
				+ " C.ITMCDDESC FROM "
				+ " RTS.RTS_TR_INV_DETAIL A, "
				+ " RTS.RTS_MV_FUNC_TRANS B, "
				+ " RTS.RTS_ITEM_CODES C, "
				+ " RTS.RTS_TRANS_HDR D, "
				+ " RTS.RTS_TRANS E "
				+ " WHERE "
				+ " A.OFCISSUANCENO =  ?           AND "
				+ " A.SUBSTAID      =  ?           AND "
				+ " A.TRANSAMDATE = ?              AND "
				+ " A.OFCISSUANCENO = D.OFCISSUANCENO AND " //TRINVDETAIL:TRANSHDR
		+" A.SUBSTAID      = D.SUBSTAID      AND "
			+ " A.TRANSWSID=D.TRANSWSID AND "
			+ " A.TRANSAMDATE=D.TRANSAMDATE AND "
			+ " A.CUSTSEQNO=D.CUSTSEQNO AND "
			+ " B.VOIDEDTRANSINDI <> 1  AND "
			+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND " //TRINVDETAIL:MVFUNCTRANS
		+" A.SUBSTAID      = B.SUBSTAID      AND "
			+ " A.TRANSWSID=B.TRANSWSID AND "
			+ " A.TRANSAMDATE=B.TRANSAMDATE AND "
			+ " A.TRANSTIME=B.TRANSTIME AND "
			+ " A.OFCISSUANCENO = E.OFCISSUANCENO AND " //TRINVDETAIL:TRANS
		+" A.SUBSTAID      = E.SUBSTAID      AND "
			+ " A.TRANSWSID=E.TRANSWSID AND "
			+ " A.TRANSAMDATE=E.TRANSAMDATE AND "
			+ " A.TRANSTIME=E.TRANSTIME AND "
			+ " D.TRANSTIMESTMP IS NOT NULL AND " // COMPLETE
		+" A.ITMCD=C.ITMCD AND "
			+ " A.DELINVREASNCD = 0 AND "
			+ " A.ISSUEMISMATCHINDI=1 "
			+ " ORDER BY  A.ITMCD, "
			+ " A.INVITMYR, "
			+ " A.INVITMNO ");
		try
		{
			//Set 1
			//OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			//SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			//TransAMDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						Integer.parseInt(
							aaGeneralSearchData.getKey2()))));
			Log.write(Log.SQL, this, " - qryIARPartI - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIARPartI - SQL - End");
			while (laResultSet.next())
			{
				InventoryActionReportData laInventoryActionReportData =
					new InventoryActionReportData();
				laInventoryActionReportData.setTransCd(
					caDA.getStringFromDB(laResultSet, "TransCd"));
				laInventoryActionReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OfcIssuanceNo"));
				laInventoryActionReportData.setTransWsId(
					caDA.getIntFromDB(laResultSet, "TransWsId"));
				laInventoryActionReportData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, "TransEmpId"));
				laInventoryActionReportData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, "TransAMDate"));
				laInventoryActionReportData.setTransTime(
					caDA.getIntFromDB(laResultSet, "TransTime"));
				laInventoryActionReportData.setInvId(
					caDA.getStringFromDB(laResultSet, "InvId"));
				laInventoryActionReportData.setInvLocIdCd(
					caDA.getStringFromDB(laResultSet, "InvLocIdCd"));
				laInventoryActionReportData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, "InvItmNo"));
				laInventoryActionReportData.setItmCd(
					caDA.getStringFromDB(laResultSet, "ItmCd"));
				laInventoryActionReportData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInventoryActionReportData.setSubconId(
					caDA.getIntFromDB(laResultSet, "SubconId"));
				laInventoryActionReportData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryActionReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, " - qryIARPartI - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIARPartI - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Select Sticker Reprints from TR_INV_DETAIL 
	 *
	 * @param aaGeneralSearchData GeneralSearchData
	 * @param aaReprintDate RTSDate
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryIARPartJ(
		GeneralSearchData aaGeneralSearchData,
		RTSDate aaReprintDate)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			laDBAccess.beginTransaction();
			TransactionInventoryDetail laTrInvDetail =
				new TransactionInventoryDetail(laDBAccess);
			// Set Values in ReprintData 
			ReprintData laReprintData = new ReprintData();
			laReprintData.setOfcIssuanceNo(
				aaGeneralSearchData.getIntKey1());
			laReprintData.setSubstaId(aaGeneralSearchData.getIntKey2());
			// defect 7607
			// Only pass one date for IAR Part J
			//trInvDetail.qryReprintStickerDaily(
			//	reprintData,
			//	reprintDate,
			//	reprintDate);
			// end defect 7607
			Vector lvResults =
				laTrInvDetail.qryReprintStickerDaily(
					laReprintData,
					aaReprintDate);
			return lvResults;
		}
		finally
		{
			laDBAccess.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Select data for RSPS_PRNT
	 *
	 * @param aaSearchData GeneralSearchData
	 * @param aaReprintDate RTSDate
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryIARPartK(
		GeneralSearchData aaSearchData,
		RTSDate aaReprintDate)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			laDBAccess.beginTransaction();
			RSPSPrint laRSPSPrint = new RSPSPrint(laDBAccess);
			ReprintData laReprintData = new ReprintData();
			laReprintData.setOfcIssuanceNo(aaSearchData.getIntKey1());
			// defect 7606 
			// Added substation id 
			laReprintData.setSubstaId(aaSearchData.getIntKey2());
			// end defect 7606 
			Vector lvResults =
				laRSPSPrint.qryRSPSPrint(laReprintData, aaReprintDate);
			return lvResults;
		}
		finally
		{
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
		}
	}
}