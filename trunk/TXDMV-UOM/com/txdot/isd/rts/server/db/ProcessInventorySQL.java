package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * ProcessInventorySQL.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/19/2001	Added qryNextAvailableInventoryEntity
 * K Harrell	01/04/2002	Altered qryExceedsMaxInventory to return 
 *							Integer.MIN_VALUE
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	11/05/2002	Altered qryNextAvailableInventoryEntity;
 *                        	for Subcon, select next available where 
 *							patrnseqno > ?
 *							defects 4934,4739
 * K Harrell	12/16/2002	Altered qryNextAvailableInventoryEntity 
 *							(again)
 *							defect 4751 
 * Min Wang		07/08/2003	Modified qryInventoryRange() and 
 *							qryNextAvailableInventory() 
 *                          to set the Boolean and InvEndNo.
 *                          defect 6076 
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	08/11/2009	Modifications for new DB2 Driver	
 * 							modify qryInventoryRange(), 
 * 							 assignSelectVariables()
 * 							defect 10164 Ver Defect_POS_E' 
 * K Harrell	03/19/2010	Remove unused method. 
 *  						delete qryExceedsMaxInventory()
 * 							defect 10239 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_INV_ALLOCATION 
 * 
 * @version	POS_640			03/19/2010
 * @author 	Kathy Harrell
 * <br> Creation Date:		09/17/2001 13:22:23 
 */

public class ProcessInventorySQL
{
	DatabaseAccess caDA;

	Vector lvValues = new Vector();

	/**
	 * ProcessInventorySQL constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public ProcessInventorySQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Assignment of selected Variables
	 *
	 * @param aaProcInvData ProcessInventoryData
	 */
	private void assignSelectVariables(ProcessInventoryData aaProcInvData)
	{
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvItmYr())));

		// defect 10164 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				//Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getPatrnSeqCd())));
		// end defect 10164 
	}

	// defect 10239 
	//	/**
	//	 * Method to Query RTS.RTS_INV_ALLOCATION against 
	//	 * RTS.RTS_INV_PROFILE for Exceeds Maximum
	//	 * RTS I - INVALLCO
	//	 * 
	//	 * @param  aaProcInvData  ProcessInventoryData
	//	 * @return ProcessInventoryData
	//	 * @throws RTSException 
	//	 */
	//	public ProcessInventoryData qryExceedsMaxInventory(ProcessInventoryData aaProcInvData)
	//		throws RTSException
	//	{
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			" - qryNextAvailableInventory - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		ProcessInventoryData laProcInvData = new ProcessInventoryData();
	//
	//		laProcInvData.setMaxQty(Integer.MIN_VALUE);
	//
	//		ResultSet lrsQry;
	//
	//		// Profile & Inventory Allocation Exist
	//		lsQry.append(
	//			" SELECT A.MAXQTY - SUM(B.INVQTY) as MaxQty "
	//				+ " FROM RTS.RTS_INV_PROFILE A, "
	//				+ " RTS.RTS_INV_ALLOCATION B "
	//				+ " WHERE "
	//				+ " A.OFCISSUANCENO = ? AND  "
	//				+ " A.SUBSTAID = ? AND  "
	//				+ " A.DELETEINDI = 0 AND "
	//				+ " A.ITMCD =  ?  AND  "
	//				+ " A.INVITMYR =  ?  AND "
	//				+ " A.ENTITY =  ?  AND  "
	//				+ " A.ID =  ?  AND "
	//				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
	//				+ " A.SUBSTAID = B.SUBSTAID AND  "
	//				+ " A.ENTITY = B.INVLOCIDCD AND  "
	//				+ " A.ID = B.INVID AND "
	//				+ " A.ITMCD = B.ITMCD AND  "
	//				+ " A.INVITMYR = B.INVITMYR "
	//				+ " GROUP BY A.MAXQTY ");
	//		//1
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getOfcIssuanceNo())));
	//		//2
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getSubstaId())));
	//		//3
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getItmCd())));
	//		//4
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getInvItmYr())));
	//		//5
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getEntity())));
	//		//6
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(aaProcInvData.getId())));
	//		// Only Profile Exists  "
	//		lsQry.append(
	//			" UNION "
	//				+ " SELECT  MAXQTY as MaxQty FROM RTS.RTS_INV_PROFILE A "
	//				+ " WHERE  "
	//				+ " A.OFCISSUANCENO = ? AND "
	//				+ " A.SUBSTAID = ? AND  "
	//				+ " DELETEINDI = 0 AND "
	//				+ " ITMCD =  ? AND  "
	//				+ " INVITMYR =  ?  AND "
	//				+ " ENTITY = ?  AND  "
	//				+ " ID =  ?  AND  "
	//				+ " NOT EXISTS  "
	//				+ " (SELECT * FROM RTS.RTS_INV_ALLOCATION B "
	//				+ " WHERE "
	//				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
	//				+ " A.SUBSTAID = B.SUBSTAID AND  "
	//				+ " A.ENTITY = B.INVLOCIDCD   AND  "
	//				+ " A.ID = B.INVID AND  "
	//				+ " A.ITMCD = B.ITMCD AND  "
	//				+ " A.INVITMYR =  B.INVITMYR)");
	//		//1
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getOfcIssuanceNo())));
	//		//2
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getSubstaId())));
	//		//3
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getItmCd())));
	//		//4
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getInvItmYr())));
	//		//5
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getEntity())));
	//		//6
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(aaProcInvData.getId())));
	//
	//		//  Default Inventory Profile and no Inventory Allocation Exists  "
	//		lsQry.append(
	//			" UNION "
	//				+ " SELECT MAXQTY as MaxQty FROM RTS.RTS_INV_PROFILE A "
	//				+ " WHERE  "
	//				+ " A.OFCISSUANCENO = ? AND "
	//				+ " A.SUBSTAID = ? AND  "
	//				+ " DELETEINDI = 0 AND  "
	//				+ " ITMCD =  ?  AND "
	//				+ " INVITMYR =  ?  AND "
	//				+ " ENTITY =  ?  AND  "
	//				+ " ID = 'DEFAULT' AND  "
	//				+ " NOT EXISTS  "
	//				+ " (SELECT * FROM "
	//				+ " RTS.RTS_INV_PROFILE B "
	//				+ " WHERE  "
	//				+ " B.DELETEINDI = 0 AND "
	//				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
	//				+ " A.SUBSTAID = B.SUBSTAID AND  "
	//				+ " A.ENTITY =  B.ENTITY AND "
	//				+ " A.ID =  ?  AND    "
	//				+ " A.ITMCD =  B.ITMCD  AND "
	//				+ " A.INVITMYR =  B.INVITMYR ) "
	//				+ " AND NOT EXISTS "
	//				+ " (SELECT * FROM RTS.RTS_INV_ALLOCATION C  "
	//				+ " WHERE "
	//				+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND  "
	//				+ " A.SUBSTAID = C.SUBSTAID AND  "
	//				+ " A.ENTITY = C.INVLOCIDCD   AND  "
	//				+ " C.INVID = ? AND  "
	//				+ " A.ITMCD = C.ITMCD AND  "
	//				+ " A.INVITMYR =  C.INVITMYR) ");
	//		//1
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getOfcIssuanceNo())));
	//		//2
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getSubstaId())));
	//		//3
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getItmCd())));
	//		//4
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getInvItmYr())));
	//		//5
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getEntity())));
	//		//6
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(aaProcInvData.getId())));
	//		//7
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(aaProcInvData.getId())));
	//
	//		lsQry.append(
	//			" UNION "
	//				+ " SELECT A.MAXQTY - "
	//				+ " SUM(B.INVQTY) as MaxQty "
	//				+ " FROM "
	//				+ " RTS.RTS_INV_PROFILE A, RTS.RTS_INV_ALLOCATION B "
	//				+ " WHERE "
	//				+ " A.OFCISSUANCENO = ? AND "
	//				+ " A.SUBSTAID = ? AND "
	//				+ " A.ITMCD =  ?  AND  "
	//				+ " A.INVITMYR =  ?  AND "
	//				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
	//				+ " A.SUBSTAID = B.SUBSTAID AND "
	//				+ " A.ITMCD = B.ITMCD AND  "
	//				+ " A.INVITMYR = B.INVITMYR AND  "
	//				+ " A.ID = 'DEFAULT' AND "
	//				+ " A.ENTITY = B.INVLOCIDCD AND   "
	//				+ " A.DELETEINDI = 0 AND  "
	//				+ " A.ENTITY =  ?  AND  "
	//				+ " B.INVID =  ?  AND "
	//				+ " NOT EXISTS "
	//				+ " (SELECT * FROM RTS.RTS_INV_PROFILE C "
	//				+ " WHERE  "
	//				+ " C.OFCISSUANCENO = A.OFCISSUANCENO AND "
	//				+ " C.SUBSTAID = A.SUBSTAID AND  "
	//				+ " C.ENTITY =  A.ENTITY AND  "
	//				+ " C.DELETEINDI = 0 AND  "
	//				+ " C.ID =  B.INVID  AND  "
	//				+ " C.ITMCD =  A.ITMCD  AND "
	//				+ " C.INVITMYR =  A.INVITMYR )  "
	//				+ " GROUP BY A.MAXQTY ");
	//		//1
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getOfcIssuanceNo())));
	//		//2
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getSubstaId())));
	//		//3
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getItmCd())));
	//		//4
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getInvItmYr())));
	//		//5
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(
	//					aaProcInvData.getEntity())));
	//		//6
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.CHAR,
	//				DatabaseAccess.convertToString(aaProcInvData.getId())));
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryNextAvailableInventory - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryNextAvailableInventory - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				laProcInvData.setMaxQty(
	//					caDA.getIntFromDB(lrsQry, "MaxQty"));
	//			} //End of While 
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryNextAvailableInventory - End ");
	//			return (laProcInvData);
	//		}
	//		catch (SQLException leSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryNextAvailableInventory - Exception "
	//					+ leSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 

	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION for Inventory Ranges
	 * 
	 * @param  aaProcInvData  ProcessInventoryData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryRange(ProcessInventoryData aaProcInvData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryInventoryRange - Begin");

		StringBuffer lsQry = new StringBuffer();

		boolean lbById = false;

		ResultSet lrsQry;

		String lsStringById = "";

		Vector lvRslt = new Vector();

		if (aaProcInvData.getCentralStock() == 1)
		{
			lsStringById = " and InvLocIdCd in ('A','C') ";
		}
		else
		{
			if (aaProcInvData.getInvId() != null)
			{
				lbById = true;
				lsStringById = " and InvId = ? and InvLocIdCd = ? ";
			}
		}

		DBValue lsInvId =
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvId()));
		DBValue lsInvLocIdCd =
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvLocIdCd()));

		// defect 10164 
		DBValue liPatrnSeqNo = 
			new DBValue(
			//Types.CHAR,
			Types.INTEGER,
			DatabaseAccess.convertToString(
			aaProcInvData.getPatrnSeqNo()));

		DBValue liEndPatrnSeqNo = 
			new DBValue(
			//Types.CHAR,
			Types.INTEGER,
			DatabaseAccess.convertToString(
			aaProcInvData.getEndPatrnSeqNo()));
		// end defect 10164 

		String lsSelectStuff =
			" as RangeCd, OFCISSUANCENO, SUBSTAID, ITMCD, INVITMYR, "
				+ " INVITMNO, INVID, INVLOCIDCD, "
				+ " INVITMENDNO, INVQTY, PATRNSEQNO, PATRNSEQNO + "
				+ " INVQTY -1 as PATRNSEQENDNO,INVSTATUSCD, "
				+ " PATRNSEQCD FROM RTS.RTS_INV_ALLOCATION WHERE "
				+ " OFCISSUANCENO= ? AND "
				+ " SUBSTAID = ? AND "
				+ " ITMCD= ? AND "
				+ " INVITMYR= ? AND "
				+ " PATRNSEQCD= ? AND ";

		// 'W' (WITHIN)   >beg,  <end
			String Condition =
				" PATRNSEQNO> ? AND " //:BEGPATRNSEQNO (1)
		+" PATRNSEQNO< ?  AND " //:ENDPATRNSEQNO  (2)
		+" (PATRNSEQNO + INVQTY - 1)> ?  AND " //:BEGPATRNSEQNO (3)
	+" (PATRNSEQNO + INVQTY - 1) < ?  "; //:ENDPATRNSEQNO (4)

		String SelectString =
			"Select 'W' " + lsSelectStuff + Condition + lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);
		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2
		lvValues.addElement(liPatrnSeqNo); //3
		lvValues.addElement(liEndPatrnSeqNo); //4

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //5
			lvValues.addElement(lsInvLocIdCd); //6
		}
		// 'R' (RIGHT) >beg, <=end    "
			Condition = " PATRNSEQNO> ? AND " //:BEG (1)
		+" PATRNSEQNO<= ? AND " //:END (2)
	+" (PATRNSEQNO + INVQTY - 1) > ? "; //:END (3)

		SelectString =
			"UNION Select 'R' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;

		lsQry.append(SelectString);
		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);

		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2
		lvValues.addElement(liEndPatrnSeqNo); //3

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //5
			lvValues.addElement(lsInvLocIdCd); //6
		}
		// 'L'  (LEFT) beg<BEG, beg+qty>= BEG, beg+qty <END  "
			Condition = " PATRNSEQNO< ? AND " //:BEG  (1) 
		+" (PATRNSEQNO + INVQTY - 1)>= ?  AND " //:BEG  (2)
	+" (PATRNSEQNO + INVQTY - 1) < ? "; //:END  (3)

		SelectString =
			"UNION Select 'L' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);

		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liPatrnSeqNo); //2
		lvValues.addElement(liEndPatrnSeqNo); //3

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //4
			lvValues.addElement(lsInvLocIdCd); //5
		}

		//  'BE ' (BeginEnd)
			Condition = " PATRNSEQNO = ? AND " //:BEG (1)
	+" (PATRNSEQNO + INVQTY - 1) = ? "; //:END (2)

		SelectString =
			" UNION Select 'BE' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);
		lvValues.addElement(liPatrnSeqNo);
		lvValues.addElement(liEndPatrnSeqNo);

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //3
			lvValues.addElement(lsInvLocIdCd); //4
		}

		// 'BW' (BeginWithin)
			Condition = " PATRNSEQNO= ? AND " //:BEG (1)
	+" (PATRNSEQNO + INVQTY - 1)< ? "; //:END (2)

		SelectString =
			" UNION Select 'BW' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;

		lsQry.append(SelectString);
		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);

		lvValues.addElement(liPatrnSeqNo);
		lvValues.addElement(liEndPatrnSeqNo);

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //3
			lvValues.addElement(lsInvLocIdCd); //4
		}

		// 'BO' (BeginOutSide)
			Condition = " PATRNSEQNO= ?  AND " //:BEG (1) 
	+" (PATRNSEQNO + INVQTY - 1) > ? "; //:END (2)

		SelectString =
			" UNION Select 'BO' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;
		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);
		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //3
			lvValues.addElement(lsInvLocIdCd); //4
		}
		// 'EW'  (EndWithin) 
			Condition =
				" (PATRNSEQNO + INVQTY - 1) = ?  AND " //:END (1)
				+" PATRNSEQNO> ? "; //:BEG (2)

		SelectString =
			" UNION Select 'EW' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);
		lvValues.addElement(liEndPatrnSeqNo); //1	
		lvValues.addElement(liPatrnSeqNo); //2

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //5
			lvValues.addElement(lsInvLocIdCd); //6
		}

		// 'EO'  (EndOutside)
			Condition =
			" (PATRNSEQNO + INVQTY - 1)= ? AND " //:END  (1)
			+" PATRNSEQNO< ? "; //:BEG  (2)

		SelectString =
			" UNION Select 'EO' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);
		lvValues.addElement(liEndPatrnSeqNo); //1	
		lvValues.addElement(liPatrnSeqNo); //2

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //3
			lvValues.addElement(lsInvLocIdCd); //4
		}
		// 'O'  (Outside)
			Condition = " PATRNSEQNO< ? AND " //:BEG (1)
	+" (PATRNSEQNO + INVQTY - 1)> ? "; //:END (2)

		SelectString =
			" UNION Select 'O' "
				+ lsSelectStuff
				+ Condition
				+ lsStringById
				+ " ORDER BY 11 ";

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaProcInvData);
		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2

		if (lbById == true)
		{
			lvValues.addElement(lsInvId); //3
			lvValues.addElement(lsInvLocIdCd); //4
		}
		lsQry.append(SelectString);

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryRange - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryRange - SQL - End");

			while (lrsQry.next())
			{
				ProcessInventoryData laProcInvData =
					new ProcessInventoryData();
				laProcInvData.setRangeCd(
					caDA.getStringFromDB(lrsQry, "RangeCd"));
				laProcInvData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laProcInvData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laProcInvData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laProcInvData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laProcInvData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laProcInvData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laProcInvData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laProcInvData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laProcInvData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laProcInvData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laProcInvData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laProcInvData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				//defect 6076
				laProcInvData.setEndPatrnSeqNo(
					laProcInvData.getPatrnSeqNo()
						+ laProcInvData.getInvQty()
						- 1);
				laProcInvData.setCalcInv(true);
				//end defect 6076
				// Add element to the Vector
				lvRslt.addElement(laProcInvData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryInventoryRange - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryRange - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION for Next Available
	 * RTS I - INVALLCN, INVALLCP
	 * 
	 * @param  aaProcInvData
	 * @return Vector
	 * @throws RTSException 
	 */
	public ProcessInventoryData qryNextAvailableInventory(ProcessInventoryData aaProcInvData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryNextAvailableInventory - Begin");

		StringBuffer lsQry = new StringBuffer();

		ProcessInventoryData laProcInvData = new ProcessInventoryData();

		ResultSet lrsQry;

		String lsSelect1 =
			" SELECT OFCISSUANCENO, SUBSTAID, ITMCD, INVITMYR, "
				+ " INVITMNO, INVID, INVLOCIDCD, "
				+ " INVITMENDNO, INVQTY, PATRNSEQNO, INVSTATUSCD, "
				+ " PATRNSEQCD  FROM RTS.RTS_INV_ALLOCATION A  WHERE  ";

		//TRANSWSID
		String lsSelectWsId =
			" A.INVLOCIDCD = 'W' AND " + " A.INVID = ? AND ";
		//1
		//TRANSEMPID
		String lsSelectEmpId =
			" A.INVLOCIDCD = 'E' AND " + " A.INVID = ? AND ";
		//7
			String lsSelect2 = " A.OFCISSUANCENO = ? AND " //2, 8
		+" A.SUBSTAID = ? AND " //3, 9
		+" A.ITMCD =  ?  AND  " //4, 10
		+" A.INVITMYR =  ?  AND " //5, 11 
	+" A.INVSTATUSCD = 0 AND  "
		+ " A.PATRNSEQNO = (SELECT MIN(PATRNSEQNO) FROM "
		+ " RTS.RTS_INV_ALLOCATION B "
		+ " WHERE  "
		+ " A.OFCISSUANCENO     =  B.OFCISSUANCENO AND  "
		+ " A.SUBSTAID          =  B.SUBSTAID AND  "
		+ " A.ITMCD             =  B.ITMCD AND  "
		+ " A.INVITMYR          =  B.INVITMYR  AND "
		+ " A.INVLOCIDCD        =  B.INVLOCIDCD AND  "
		+ " A.INVID             =  B.INVID AND "
		+ " A.INVSTATUSCD       =  B.INVSTATUSCD AND "
		+ " B.PATRNSEQCD =  "
		+ " (SELECT "
		+ " MIN(PATRNSEQCD)  "
		+ " FROM RTS.RTS_INV_ALLOCATION C WHERE "
		+ " A.OFCISSUANCENO     =  C.OFCISSUANCENO AND  "
		+ " A.SUBSTAID          =  C.SUBSTAID AND  "
		+ " A.ITMCD             =  C.ITMCD AND  "
		+ " A.INVITMYR          =  C.INVITMYR  AND "
		+ " A.INVLOCIDCD        =  C.INVLOCIDCD AND  "
		+ " A.INVID             =  C.INVID AND "
		+ " A.INVSTATUSCD       =  C.INVSTATUSCD "
		+ " ))";

		String lsSelectNoEmpId =
			" AND  NOT EXISTS (SELECT * FROM RTS.RTS_INV_ALLOCATION D "
				+ " WHERE  "
				+ " A.OFCISSUANCENO     =  D.OFCISSUANCENO AND  "
				+ " A.SUBSTAID          =  D.SUBSTAID AND  "
				+ " A.ITMCD             =  D.ITMCD AND  "
				+ " A.INVITMYR          =  D.INVITMYR  AND "
				+ " A.INVSTATUSCD       =  D.INVSTATUSCD AND "
				+ " D.INVLOCIDCD        =  'E' AND  "
				+ " D.INVID             =  ?  ) ";
		//6
		lsQry.append(
			lsSelect1
				+ lsSelectWsId
				+ lsSelect2
				+ lsSelectNoEmpId
				+ " UNION "
				+ lsSelect1
				+ lsSelectEmpId
				+ lsSelect2);
		try
		{
			//1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getTransWsId())));
			//2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getOfcIssuanceNo())));
			//3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getSubstaId())));
			//4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaProcInvData.getItmCd())));
			//5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getInvItmYr())));
			//6
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaProcInvData.getTransEmpId())));
			//7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaProcInvData.getTransEmpId())));
			//8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getOfcIssuanceNo())));
			//9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getSubstaId())));
			//10
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaProcInvData.getItmCd())));
			//11
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaProcInvData.getInvItmYr())));

			Log.write(
				Log.SQL,
				this,
				" - qryNextAvailableInventory - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryNextAvailableInventory - SQL - End");

			while (lrsQry.next())
			{
				laProcInvData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laProcInvData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laProcInvData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laProcInvData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laProcInvData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laProcInvData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laProcInvData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				//defect 6076
				//laProcInvData.setInvItmEndNo(
				//    caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				//end defect 6076
				laProcInvData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laProcInvData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laProcInvData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laProcInvData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				//defect 6076
				laProcInvData.setEndPatrnSeqNo(
					laProcInvData.getPatrnSeqNo());
				laProcInvData.setInvItmEndNo(
					laProcInvData.getInvItmNo());
				laProcInvData.setInvQty(1);
				laProcInvData.setCalcInv(true);
				//end defect 6076
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryNextAvailableInventory - End ");
			return (laProcInvData);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryNextAvailableInventory - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION for Next Available 
	 * for Entity
	 * RTS I - INVALLCW
	 * 
	 * @param  aaProcInvData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryNextAvailableInventoryEntity(ProcessInventoryData aaProcInvData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryNextAvailableInventoryEntity - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			" SELECT OfcIssuanceNo,SubstaId,ITMCD, INVITMYR, INVITMNO, "
				+ " INVID, "
				+ " INVLOCIDCD, INVITMENDNO, INVQTY, "
				+ " PATRNSEQNO, INVSTATUSCD, PATRNSEQCD "
				+ " FROM RTS.RTS_INV_ALLOCATION A "
				+ " WHERE  "
				+ " OFCISSUANCENO = ? AND  "
				+ " SUBSTAID = ? AND  "
				+ " ITMCD = ? AND "
				+ " INVITMYR = ? AND "
				+ " INVLOCIDCD = ? AND "
				+ " INVID=? AND "
				+ " INVSTATUSCD = 0 AND ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvItmYr())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvLocIdCd())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvId())));

		// defects 4934,4739
		// Select next available where patrnseqno > ?
		if (aaProcInvData.getInvLocIdCd().equals("S"))
		{
			lsQry.append(
				" A.PATRNSEQNO = (SELECT MIN(PATRNSEQNO) FROM "
					+ " RTS.RTS_INV_ALLOCATION B "
					+ " WHERE  "
					+ " A.OFCISSUANCENO     =  B.OFCISSUANCENO AND  "
					+ " A.SUBSTAID          =  B.SUBSTAID AND  "
					+ " A.ITMCD             =  B.ITMCD AND  "
					+ " A.INVITMYR          =  B.INVITMYR  AND "
					+ " A.INVLOCIDCD        =  B.INVLOCIDCD AND  "
					+ " A.INVID             =  B.INVID AND "
					+ " A.INVSTATUSCD       =  B.INVSTATUSCD AND "
					+ " B.PATRNSEQNO > ? ) ");

			// defect 10164 
			lvValues.addElement(new DBValue(
			//Types.CHAR,
			Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getPatrnSeqNo())));
			// end defect 10164 
		}
		else
		{
			lsQry.append(
				" A.PATRNSEQNO = (SELECT MIN(PATRNSEQNO) "
					+ " FROM RTS.RTS_INV_ALLOCATION B "
					+ " WHERE  "
					+ " A.OFCISSUANCENO     =  B.OFCISSUANCENO AND  "
					+ " A.SUBSTAID          =  B.SUBSTAID AND  "
					+ " A.ITMCD             =  B.ITMCD AND  "
					+ " A.INVITMYR          =  B.INVITMYR  AND "
					+ " A.INVLOCIDCD        =  B.INVLOCIDCD AND  "
					+ " A.INVID             =  B.INVID AND "
					+ " A.INVSTATUSCD       =  B.INVSTATUSCD AND "
					+ " B.PATRNSEQCD =  "
					+ " (SELECT "
					+ " MIN(PATRNSEQCD)  "
					+ " FROM RTS.RTS_INV_ALLOCATION C WHERE "
					+ " A.OFCISSUANCENO     =  C.OFCISSUANCENO AND  "
					+ " A.SUBSTAID          =  C.SUBSTAID AND  "
					+ " A.ITMCD             =  C.ITMCD AND  "
					+ " A.INVITMYR          =  C.INVITMYR  AND "
					+ " A.INVLOCIDCD        =  C.INVLOCIDCD AND  "
					+ " A.INVID             =  C.INVID AND "
					+ " A.INVSTATUSCD       =  C.INVSTATUSCD "
					+ " ))");
		}
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryNextAvailableInventoryEntity - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryNextAvailableInventoryEntity - SQL - End");

			while (lrsQry.next())
			{
				ProcessInventoryData laProcInvData =
					new ProcessInventoryData();
				laProcInvData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laProcInvData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laProcInvData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laProcInvData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laProcInvData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laProcInvData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laProcInvData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laProcInvData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laProcInvData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laProcInvData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laProcInvData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laProcInvData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				lvRslt.addElement(laProcInvData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryNextAvailableInventoryEntity - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryNextAvailableInventoryEntity - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
}
