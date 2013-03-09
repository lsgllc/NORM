package com.txdot.isd.rts.services.reports.funds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * TransactionReconciliationReportSQL.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   08/02/2002  Added SQL for VoidTransCd
 * 							defect 4541  
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 * 							 qryTransactionReconciliationReportFees
 * 							 qryTransactionReconciliationReportInventory
 * 							 qryTransactionReconciliationReportPayment
 * 							 qryTransactionReconciliationReportTransact~
 *							defect 7896 Ver 5.2.3
 * K Harrell	04/12/2007	Added union to SR_FUNC_TRANS for 
 * 							manufactured inventory 
 * 							modify qryTransactionReconciliationReportInventory()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/07/2008	If HQ, include all FeeSourceCd transactions
 * 							modify 
 * 							 qryTransactionReconciliationReportFees(),
 * 							 qryTransactionReconciliationReportInventory(),
 * 							 qryTransactionReconciliationReportPayment(),
 * 							 qryTransactionReconciliationReportTransact~()
 * 							defect 9653 Ver Defect POS A   
 * B Hargrove	07/11/2008	Add pilot for RTS_PLT_TYPE.  
 * 							modify qryTransactionReconciliationReportInventory()
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	10/27/2008	Added union to DSABLD_PLCRD_TRANS for 
 * 							 deleted placards. 
 * 							modify qryTransactionReconciliationReportInventory()
 * 							defect 9831 Ver Defect_POS_B	
 * K Harrell	05/20/2009	Replace reference to SR_FUNC_TRANS.REGEXPYR
 * 							 with PLTEXPYR. 
 * 							modify qryTransactionReconciliationReportInventory()
 * 							defect 10078 Ver Defect_POS_F
 * ---------------------------------------------------------------------                   
 */
/**
 * This class contains methods to interact with database 
 * 
 * @version	Defect_POS_F	05/20/2009    
 * @author	Kathy Harrell
 * <br>Creation Date:		10/04/2001 14:48:14
 */
public class TransactionReconciliationReportSQL
	extends TransactionReconciliationReportData
{
	DatabaseAccess caDA;

	/**
	 * TransactionReconciliationReportSQL Constructor
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public TransactionReconciliationReportSQL(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Retrieve Item (Fees & Inventory) Data for
	 * TransactionReconciliationReport
	 * 
	 * @param aaFundsData FundsData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionReconciliationReportFees(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionReconciliationReportFees - Begin");
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

		// defect 9653
		String lsFeeSourceCd =
			OfficeIdsCache.isHQ(aaFundsData.getOfficeIssuanceNo())
				? CommonConstant.STR_SPACE_EMPTY
				: " H.FeeSourceCd <> 0 and ";

		// Note: Always have to have EMPID for Transaction Data 
		ResultSet laQry;
		lsQry.append(
			" SELECT  "
				+ lsSelect
				+ " ,H.OFCISSUANCENO,  "
				+ " H.TRANSAMDATE,  "
				+ " H.TRANSWSID,  "
				+ " H.CUSTSEQNO,  "
				+ " A.TRANSTIME,  "
				+ " B.AcctItmCdDesc,"
				+ " B.AcctItmCd,"
				+ " A.ItmPrice "
				+ " FROM  "
				+ " RTS.RTS_TR_FDS_DETAIL A,  "
				+ " RTS.RTS_ACCT_CODES B, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsFeeSourceCd
				+ " H.OfcIssuanceNo = ? AND "
				+ " H.SubstaId = ? AND  "
				+ lsList
				+ " A.OfcIssuanceNo = H.OfcIssuanceNo AND "
				+ " A.SubstaId = H.Substaid  AND  "
				+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " A.TRANSWSID = H.TRANSWSID AND  "
				+ " A.CUSTSEQNO = H.CUSTSEQNO and "
				+ " A.AcctItmCd = B.AcctItmCd and "
				+ " B.RTSEFFDATE = (SELECT MAX(RTSEFFDATE)"
				+ " FROM RTS.RTS_ACCT_CODES C "
				+ " WHERE A.ACCTITMCD = C.ACCTITMCD ) "
				+ " order by "
				+ lsSelect
				+ ",H.TransWsId, H.TransAmDate,H.CUSTSEQNO,"
				+ " A.TransTime, B.AcctItmCdDesc");
		// end defect 9653 
		try
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
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionReconciliationReportFees"
					+ " - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionReconciliationReportFees - SQL"
					+ " - End");

			while (laQry.next())
			{
				TransactionReconciliationReportFeesData laTransactionReconciliationReportFeesData =
					new TransactionReconciliationReportFeesData();

				laTransactionReconciliationReportFeesData
					.setOfcIssuanceNo(
					caDA.getIntFromDB(laQry, "OfcIssuanceNo"));

				if (lbByCashDrawer)
				{
					laTransactionReconciliationReportFeesData
						.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}
				if (lbByEmployee)
				{
					laTransactionReconciliationReportFeesData
						.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				laTransactionReconciliationReportFeesData.setTransWsId(
					caDA.getIntFromDB(laQry, "TransWsId"));
				laTransactionReconciliationReportFeesData
					.setTransAMDate(
					caDA.getIntFromDB(laQry, "TransAMDate"));
				laTransactionReconciliationReportFeesData.setCustSeqNo(
					caDA.getIntFromDB(laQry, "CustSeqNo"));
				laTransactionReconciliationReportFeesData.setTransTime(
					caDA.getIntFromDB(laQry, "TransTime"));
				laTransactionReconciliationReportFeesData
					.setAcctItmCdDesc(
					caDA.getStringFromDB(laQry, "AcctItmCdDesc"));
				laTransactionReconciliationReportFeesData.setAcctItmCd(
					caDA.getStringFromDB(laQry, "AcctItmCd"));
				laTransactionReconciliationReportFeesData.setItmPrice(
					caDA.getDollarFromDB(laQry, "ItmPrice"));

				// Add element to the Vector

				lvRslt.addElement(
					laTransactionReconciliationReportFeesData);

			} //End of While 

			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionReconciliationReportFees - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionReconciliationReportFees - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryTransactionReconciliationReportFees - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Retrieve Item (Fees & Inventory) Data for
	 * TransactionReconciliationReport
	 * 
	 * @param aaFundsData FundsData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionReconciliationReportInventory(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionReconciliationReportInventory - Begin");
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

		ResultSet laQry;

		// defect 9689
		// Again, use reference to PILOT qualifier
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}

		/// defect 9653
		String lsFeeSourceCd =
			OfficeIdsCache.isHQ(aaFundsData.getOfficeIssuanceNo())
				? CommonConstant.STR_SPACE_EMPTY
				: " H.FeeSourceCd <> 0 and ";



		// defect 9085 
		// Add union to SR_FUNC_TRANS 
		lsQry
			.append(
				" SELECT  "
				+ lsSelect
				+ " ,H.OFCISSUANCENO,  "
				+ " H.TRANSAMDATE,  "
				+ " H.TRANSWSID,  "
				+ " H.CUSTSEQNO,  "
				+ " A.TRANSTIME,  "
				+ " B.ITMCDDESC,  "
				+ " B.ITMCD, "
				+ " A.INVITMNO,  "
				+ " A.INVITMYR   "
				+ " FROM  "
				+ " RTS.RTS_TR_INV_DETAIL A, "
				+ " RTS.RTS_ITEM_CODES B,  "
				+ " RTS.RTS_TRANS_HDR H "
				+ " WHERE  "
				+ lsFeeSourceCd
				+ " a.itmcd = b.itmcd and "
				+ " H.OfcIssuanceNo = ? AND "
				+ " H.SubstaId = ? AND  "
				+ " A.OfcIssuanceNo = H.OfcIssuanceNo AND "
				+ " A.SubstaId = H.Substaid  AND  "
				+ lsList
				+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " A.TRANSWSID = H.TRANSWSID AND  "
				+ " A.CUSTSEQNO = H.CUSTSEQNO AND    "
				+ " ((a.TRANSCD ='INVDEL' AND a.DELINVREASNCD=5) OR   "
				+ " a.TRANSCD NOT IN ('INVADJ', 'INVRCV', 'INVALL',"
				+ " 'INVOFC', 'INVDEL')) "
				+ " UNION "
				+ " SELECT  "
				+ lsSelect
				+ " ,H.OFCISSUANCENO,  "
				+ " H.TRANSAMDATE,  "
				+ " H.TRANSWSID,  "
				+ " H.CUSTSEQNO,  "
				+ " A.TRANSTIME,  "
				+ " B.ITMCDDESC,"
				+ " B.ITMCD, "
				+ " ('*' CONCAT A.REGPLTNO) AS InvItmNo,  "
				// defect 10078
				//+ " A.REGEXPYR  AS InvItmYr "
				+ " A.PLTEXPYR  AS InvItmYr "
				// end defect 10078
				+ " FROM  "
				+ " RTS.RTS_SR_FUNC_TRANS  A, "
				+ " RTS.RTS_ITEM_CODES B,  "
				+ " RTS.RTS_TRANS_HDR H "
				+ " WHERE  "
				+ lsFeeSourceCd
				+ " A.MFGSTATUSCD = 'M' and "
				+ " a.REGPLTCD = b.itmcd and "







		//+ " EXISTS (SELECT * FROM RTS.RTS_PLT_TYPE C "
		+" EXISTS (SELECT * FROM "
			+ lsTableCreator
			+ ".RTS_PLT_TYPE C "
			+ " WHERE A.REGPLTCD = C.REGPLTCD AND C.ANNUALPLTINDI = 1 "
			+ " AND C.RTSEFFENDDATE = 99991231 )"
			+ " AND "
			+ " H.OfcIssuanceNo = ? AND "
			+ " H.SubstaId = ? AND  "
			+ " A.OfcIssuanceNo = H.OfcIssuanceNo AND "
			+ " A.SubstaId = H.Substaid  AND  "
			+ lsList
			+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
			+ " A.TRANSWSID = H.TRANSWSID AND  "
			+ " A.CUSTSEQNO = H.CUSTSEQNO  "
			+ " UNION "
			+ " SELECT  "
			+ lsSelect
			+ " ,H.OFCISSUANCENO,  "
			+ " H.TRANSAMDATE,  "
			+ " H.TRANSWSID,  "
			+ " H.CUSTSEQNO,  "
			+ " A.TRANSTIME,  "
			+ " B.ITMCDDESC, "
			+ " B.ITMCD, "
			+ " ('*' CONCAT A.REGPLTNO) AS InvItmNo,  "
			+ " 0 as InvItmYr  "
			+ " FROM  "
			+ " RTS.RTS_SR_FUNC_TRANS  A, "
			+ " RTS.RTS_ITEM_CODES B,  "
			+ " RTS.RTS_TRANS_HDR H "
			+ " WHERE  "
			+ lsFeeSourceCd
			+ " A.MFGSTATUSCD = 'M' and "
			+ " a.REGPLTCD = b.itmcd and "
		//+ " EXISTS (SELECT * FROM RTS.RTS_PLT_TYPE C "
		+" EXISTS (SELECT * FROM "
			+ lsTableCreator
			+ ".RTS_PLT_TYPE C "
			+ " WHERE A.REGPLTCD = C.REGPLTCD AND C.ANNUALPLTINDI = 0 "
			+ " AND C.RTSEFFENDDATE = 99991231 )"
			+ " AND "
			+ " H.OfcIssuanceNo = ? AND "
			+ " H.SubstaId = ? AND  "
			+ " A.OfcIssuanceNo = H.OfcIssuanceNo AND "
			+ " A.SubstaId = H.Substaid  AND  "
			+ lsList
			+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
			+ " A.TRANSWSID = H.TRANSWSID AND  "
			+ " A.CUSTSEQNO = H.CUSTSEQNO  "
			+ " UNION "
			+ " SELECT  "
			+ lsSelect
			+ " ,H.OFCISSUANCENO,  "
			+ " H.TRANSAMDATE,  "
			+ " H.TRANSWSID,  "
			+ " H.CUSTSEQNO,  "
			+ " A.TRANSTIME,  "
			+ " B.ITMCDDESC, "
			+ " B.ITMCD, "
			+ " ('*' CONCAT E.INVITMNO) AS InvItmNo,  "
			+ " 0 as InvItmYr  "
			+ " FROM  "
			+ " RTS.RTS_DSABLD_PLCRD_TRANS A, "
			+ " RTS.RTS_ITEM_CODES B,  "
			+ " RTS.RTS_TRANS_HDR H, "
			+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD D, "
			+ " RTS.RTS_DSABLD_PLCRD E "
			+ " WHERE  "
			+ lsFeeSourceCd
			+ " A.TRANSIDNTYNO = D.TRANSIDNTYNO AND "
			+ " D.TRANSTYPECD = 1 AND "
			+ " D.DSABLDPLCRDIDNTYNO = E.DSABLDPLCRDIDNTYNO AND "
			+ " E.INVITMCD = b.itmcd and "
			+ " H.OfcIssuanceNo = ? AND "
			+ " H.SubstaId = ? AND  "
			+ " A.OfcIssuanceNo = H.OfcIssuanceNo AND "
			+ " A.SubstaId = H.Substaid AND  "
			+ lsList
			+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
			+ " A.TRANSWSID = H.TRANSWSID AND  "
			+ " A.CUSTSEQNO = H.CUSTSEQNO  "
			+ " order by "
			+ " 1,2,3,4,5,6,7,8,9,10");
		//+ lsSelect
		//+ ",H.TransAmDate,H.TransWsId, H.CUSTSEQNO,"
		//+ " A.TransTime,B.ITMCDDESC");
		// end defect 9689 
		// end defect 9085 
		// end defect 9653 
		try
		{
			// TR_INV_DETAIL 
			// SR_FUNC_TRANS  - ANNUAL
			// SR_FUNC_TRANS  - NOT ANNUAL
			for (int i = 0; i < 4; i++)
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
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionReconciliationReportInventory -"
					+ " SQL - Begin");

			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionReconciliationReportInventory - SQL"
					+ " - End");

			while (laQry.next())
			{
				TransactionReconciliationReportInventoryData laTransactionReconciliationReportInventoryData =
					new TransactionReconciliationReportInventoryData();

				laTransactionReconciliationReportInventoryData
					.setOfcIssuanceNo(
					caDA.getIntFromDB(laQry, "OfcIssuanceNo"));
				if (lbByCashDrawer)
				{
					laTransactionReconciliationReportInventoryData
						.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				};
				if (lbByEmployee)
				{
					laTransactionReconciliationReportInventoryData
						.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				laTransactionReconciliationReportInventoryData
					.setTransWsId(
					caDA.getIntFromDB(laQry, "TransWsId"));
				laTransactionReconciliationReportInventoryData
					.setTransAMDate(
					caDA.getIntFromDB(laQry, "TransAMDate"));
				laTransactionReconciliationReportInventoryData
					.setCustSeqNo(
					caDA.getIntFromDB(laQry, "CustSeqNo"));
				laTransactionReconciliationReportInventoryData
					.setTransTime(
					caDA.getIntFromDB(laQry, "TransTime"));
				laTransactionReconciliationReportInventoryData
					.setInvItmNo(
					caDA.getStringFromDB(laQry, "InvItmNo"));
				laTransactionReconciliationReportInventoryData
					.setItmCdDesc(
					caDA.getStringFromDB(laQry, "ItmCdDesc"));
				laTransactionReconciliationReportInventoryData
					.setItmCd(
					caDA.getStringFromDB(laQry, "ItmCd"));
				laTransactionReconciliationReportInventoryData
					.setInvItmYr(
					caDA.getIntFromDB(laQry, "InvItmYr"));

				// Add element to the Vector

				lvRslt.addElement(
					laTransactionReconciliationReportInventoryData);

			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionReconciliationReportInventory - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionReconciliationReportInventory - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryTransactionReconciliationReportInventory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Retrieve Payment Data for
	 * TransactionReconciliationReport
	 * 
	 * @param aaFundsData FundsData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionReconciliationReportPayment(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionReconciliationReportPayment - Begin");
		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		ResultSet laQry;
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

		// defect 9653
		String lsFeeSourceCd =
			OfficeIdsCache.isHQ(aaFundsData.getOfficeIssuanceNo())
				? CommonConstant.STR_SPACE_EMPTY
				: " H.FeeSourceCd <> 0 and ";

		lsQry.append(
			"SELECT DISTINCT  "
				+ lsSelect
				+ " ,H.OFCISSUANCENO,  "
				+ " H.TRANSWSID,  "
				+ " H.TRANSAMDATE,  "
				+ " H.CUSTSEQNO,  "
				+ " 'PAYMENT' as RecordType, "
				+ " B.PYMNTTYPECDDESC,  "
				+ " A.PYMNTNO,  "
				+ " A.PYMNTTYPEAMT  "
				+ " FROM "
				+ " RTS.RTS_TRANS_PAYMENT A,  "
				+ " RTS.RTS_PAYMENT_TYPE B,  "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsFeeSourceCd
				+ " H.OfcIssuanceNo = ? AND "
				+ " H.SubstaId = ? AND  "
				+ lsList
				+ " H.OfcIssuanceNo = A.OfcIssuanceNo AND "
				+ " H.SubstaId = A.Substaid  AND  "
				+ " H.TRANSWSID = A.TRANSWSID AND    "
				+ " H.TRANSAMDATE = A.TRANSAMDATE AND   "
				+ " H.CUSTSEQNO = A.CUSTSEQNO AND    "
				+ " A.PYMNTTYPECD = B.PYMNTTYPECD ");

		//  Change Due 
		lsQry.append(
			" UNION ALL "
				+ " SELECT DISTINCT  "
				+ lsSelect
				+ ", H.OFCISSUANCENO,  "
				+ " H.TRANSWSID,  "
				+ " H.TRANSAMDATE,  "
				+ " H.CUSTSEQNO,  "
				+ "'CHANGE' as RecordType, "
				+ "B.PYMNTTYPECDDESC,  "
				+ " A.PYMNTNO,  "
				+ " A.CHNGDUE as PYMNTTYPEAMT  FROM "
				+ " RTS.RTS_TRANS_PAYMENT A,  "
				+ " RTS.RTS_PAYMENT_TYPE B,  "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsFeeSourceCd
				+ " H.OfcIssuanceNo = ? AND "
				+ " H.SubstaId = ? AND  "
				+ lsList
				+ " H.OfcIssuanceNo = A.OfcIssuanceNo AND "
				+ " H.SubstaId = A.Substaid  AND  "
				+ " H.TRANSWSID = A.TRANSWSID AND    "
				+ " H.TRANSAMDATE = A.TRANSAMDATE AND   "
				+ " H.CUSTSEQNO = A.CUSTSEQNO AND    "
				+ " A.CHNGDUEPYMNTTYPECD = B.PYMNTTYPECD "
				+ " order by  "
				+ "1,2,3,4,5,6,7,8,9");
		// end defect 9653 
		try
		{
			// Payment 
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

			// Change Due
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
				" - qryTransactionReconciliationReportPayment"
					+ " - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionReconciliationReportPayment"
					+ " - SQL - End");

			while (laQry.next())
			{

				TransactionReconciliationReportPaymentData laTransactionReconciliationReportPaymentData =
					new TransactionReconciliationReportPaymentData();

				laTransactionReconciliationReportPaymentData
					.setRecordType(
					caDA.getStringFromDB(laQry, "RecordType"));
				laTransactionReconciliationReportPaymentData
					.setOfcIssuanceNo(
					caDA.getIntFromDB(laQry, "OfcIssuanceNo"));
				if (lbByCashDrawer)
				{
					laTransactionReconciliationReportPaymentData
						.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}
				if (lbByEmployee)
				{
					laTransactionReconciliationReportPaymentData
						.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				laTransactionReconciliationReportPaymentData
					.setTransWsId(
					caDA.getIntFromDB(laQry, "TransWsId"));
				laTransactionReconciliationReportPaymentData
					.setTransAMDate(
					caDA.getIntFromDB(laQry, "TransAMDate"));
				laTransactionReconciliationReportPaymentData
					.setCustSeqNo(
					caDA.getIntFromDB(laQry, "CustSeqNo"));
				laTransactionReconciliationReportPaymentData
					.setPymntNo(
					caDA.getStringFromDB(laQry, "PymntNo"));
				laTransactionReconciliationReportPaymentData
					.setPymntTypeCdDesc(
					caDA.getStringFromDB(laQry, "PymntTypeCdDesc"));
				laTransactionReconciliationReportPaymentData
					.setPymntTypeAmt(
					caDA.getDollarFromDB(laQry, "PymntTypeAmt"));

				// Add element to the Vector

				lvRslt.addElement(
					laTransactionReconciliationReportPaymentData);

			} //End of While 

			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionReconciliationReportPayment - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionReconciliationReportPayment"
					+ " - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryTransactionReconciliationReportPayment - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD - TransReconReportPayment

	/**
	 * Method to Retrieve Item (Fees & Inventory) Data for
	 * TransactionReconciliationReport
	 * 
	 * @param aaFundsData FundsData 
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryTransactionReconciliationReportTransaction(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionReconciliationReportTransaction - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		FundsSQL laFS = new FundsSQL();
		FundsSQLData laFSData =
			(FundsSQLData) laFS.genFundsSQL(aaFundsData);

		boolean lbByCashDrawer =
			aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER
				|| aaFundsData.getFundsReportData().getPrimarySplit()
					== FundsConstant.CASH_DRAWER;

		String lsSelect = laFSData.getSelect();
		String lsList = laFSData.getList();

		// defect 9653
		String lsFeeSourceCd =
			OfficeIdsCache.isHQ(aaFundsData.getOfficeIssuanceNo())
				? CommonConstant.STR_SPACE_EMPTY
				: " H.FeeSourceCd <> 0 and ";

		ResultSet laQry;
		lsQry.append(
			" SELECT  "
				+ lsSelect
				+ " ,H.OFCISSUANCENO,  "
				+ " H.TRANSWSID,  "
				+ " H.TRANSAMDATE,  "
				+ " H.CUSTSEQNO,  "
				+ " A.TRANSTIME,  "
				+ " A.TRANSEMPID, "
				+ " A.CUSTNAME1, "
				+ " B.TRANSCDDESC,  "
				+ " A.TRANSCD, "
				+ " A.VOIDTRANSCD "
				+ " FROM  "
				+ " RTS.RTS_TRANS A,  "
				+ " RTS.RTS_TRANS_CDS B, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ lsFeeSourceCd
				+ " H.OfcIssuanceNo = ? AND "
				+ " H.SubstaId = ? AND  "
				+ " A.OfcIssuanceNo = H.OfcIssuanceNo AND "
				+ " A.SubstaId = H.Substaid  AND  "
				+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " A.TRANSWSID = H.TRANSWSID AND  "
				+ " A.CUSTSEQNO = H.CUSTSEQNO AND    "
				+ lsList
				+ " A.TRANSCD=B.TRANSCD "
				+ " order by "
				+ "1,2,3,4,5,6,7,8,9");
		// end defect 9653 

		try
		{
			// Transaction
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
				" - qryTransactionReconciliationReportTransaction"
					+ " - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionReconciliationReportTransaction"
					+ " - SQL - End");

			while (laQry.next())
			{
				TransactionReconciliationReportTransactionData laTransactionReconciliationReportTransactionData =
					new TransactionReconciliationReportTransactionData();

				laTransactionReconciliationReportTransactionData
					.setOfcIssuanceNo(
					caDA.getIntFromDB(laQry, "OfcIssuanceNo"));
				if (lbByCashDrawer)
				{
					laTransactionReconciliationReportTransactionData
						.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				};
				laTransactionReconciliationReportTransactionData
					.setTransEmpId(
					caDA.getStringFromDB(laQry, "TransEmpId"));
				laTransactionReconciliationReportTransactionData
					.setTransWsId(
					caDA.getIntFromDB(laQry, "TransWsId"));
				laTransactionReconciliationReportTransactionData
					.setTransAMDate(
					caDA.getIntFromDB(laQry, "TransAMDate"));
				laTransactionReconciliationReportTransactionData
					.setCustSeqNo(
					caDA.getIntFromDB(laQry, "CustSeqNo"));
				laTransactionReconciliationReportTransactionData
					.setTransTime(
					caDA.getIntFromDB(laQry, "TransTime"));
				laTransactionReconciliationReportTransactionData
					.setCustName1(
					caDA.getStringFromDB(laQry, "CustName1"));
				laTransactionReconciliationReportTransactionData
					.setTransCdDesc(
					caDA.getStringFromDB(laQry, "TransCdDesc"));
				laTransactionReconciliationReportTransactionData
					.setTransCd(
					caDA.getStringFromDB(laQry, "TransCd"));
				laTransactionReconciliationReportTransactionData
					.setVoidTransCd(
					caDA.getStringFromDB(laQry, "VoidTransCd"));
				// Add element to the Vector

				lvRslt.addElement(
					laTransactionReconciliationReportTransactionData);

			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionReconciliationReportTransaction - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionReconciliationReportTransaction"
					+ " - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryTransactionReconciliationReportTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
} //END OF CLASS
