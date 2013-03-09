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
 *
 * InventoryDetailReportSQL.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	11/17/2001  Added lSelect, lList, FundsData
 * K Harrell	01/23/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify qryInventoryDetailReport()
 *   						Ver 5.2.0	
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3 		
 * K Harrell	08/07/2008	Update to handle no workstation in list
 * 							modify qryInventoryDetailReport()
 * 							defect 8913 Ver MyPlates_POS 	 
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database.
 * 
 * @version	MyPlates_POS	08/07/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/19/2001 
 */
public class InventoryDetailReportSQL extends InventoryDetailReportData
{
	public DatabaseAccess caDA;

	/**
	 * InventoryDetailReportSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public InventoryDetailReportSQL(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Retrieve Data for InventoryDetailReport
	 * 
	 * @param aaFundsData FundsData	
	 * @return Vector 	
	 * @throws RTSException
	 */
	public Vector qryInventoryDetailReport(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryDetailReport - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		Vector lvRslt = new Vector();
		FundsSQL laFS = new FundsSQL();
		FundsSQLData laFSData =
			(FundsSQLData) laFS.genFundsSQL(aaFundsData);
		String lsSelect = laFSData.getSelect();
		String lsList = laFSData.getList();
		
		// defect 8913 
		// If no entities in list, return empty vector 
		if (lsList.equals(" () AND "))
		{
			return lvRslt;
		}
		// end defect 8913 
		
		String lsOrderBy = "";
		// or can this simply be = null     
		if (aaFundsData.getFundsReportData().getPrimarySplit()
			== FundsConstant.CASH_DRAWER
			|| aaFundsData.getFundsReportData().getPrimarySplit()
				== FundsConstant.EMPLOYEE)
		{
			lsOrderBy = " order by 1,2,12,13,14,15,8 ";
		}
		else
		{
			lsOrderBy = " order by 1,11,12,13,14,7 ";
		}
		ResultSet laQry;
		// PCR 34 - Added PrntInvQty
		lsQry.append(
			" SELECT "
				+ lsSelect
				+ " , B.TRANSCD,  "
				+ " H.OFCISSUANCENO,  "
				+ " A.TRANSAMDATE,  "
				+ " A.TRANSTIME,  "
				+ " A.TRANSWSID,  "
				+ " A.VOIDEDTRANSINDI,  "
				+ " B.INVLOCIDCD,  "
				+ " B.INVID,  "
				+ " C.ITMCD,  "
				+ " C.ITMCDDESC,  "
				+ " B.INVITMYR,  "
				+ " B.INVITMNO,  "
				+ " B.INVENDNO,  "
				+ " B.PRNTINVQTY, "
				+ " B.INVQTY,  "
				+ " '' AS DELINVREASN "
				+ " FROM RTS.RTS_TRANS A,  "
				+ " RTS.RTS_TR_INV_DETAIL B,  "
				+ " RTS.RTS_ITEM_CODES C, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ? AND  "
				+ " A.SUBSTAID = ? AND  "
				+ lsList
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND   "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " A.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " A.TRANSTIME = B.TRANSTIME AND   "
				+ " A.TRANSCD NOT LIKE 'INV%' AND   "
				+ " B.INVLOCIDCD <> 'V' AND   "
				+ " B.ITMCD=C.ITMCD AND  "
				+ " A.OFCISSUANCENO = H.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = H.SUBSTAID AND  "
				+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " A.TRANSWSID = H.TRANSWSID AND  "
				+ " A.CUSTSEQNO = H.CUSTSEQNO AND "
				+ " B.INVQTY>=1  "
				+ " UNION ALL "
				+ " SELECT  "
				+ lsSelect
				+ " , B.TRANSCD,  "
				+ " H.OFCISSUANCENO,  "
				+ " A.TRANSAMDATE,  "
				+ " A.TRANSTIME,  "
				+ " A.TRANSWSID,  "
				+ " A.VOIDEDTRANSINDI,  "
				+ " B.INVLOCIDCD,  "
				+ " B.INVID,  "
				+ " C.ITMCD,  "
				+ " C.ITMCDDESC,  "
				+ " B.INVITMYR,  "
				+ " B.INVITMNO,  "
				+ " B.INVENDNO,  "
				+ " B.PRNTINVQTY, "
				+ " B.INVQTY,  "
				+ " '' AS DELINVREASN "
				+ " FROM RTS.RTS_TRANS A,  "
				+ " RTS.RTS_TR_INV_DETAIL B,  "
				+ " RTS.RTS_ITEM_CODES C, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE  "
				+ " A.TRANSCD NOT LIKE 'INV%' AND   "
				+ " B.INVLOCIDCD='V' AND   "
				+ " A.OFCISSUANCENO = ? AND  "
				+ " A.SUBSTAID = ? AND  "
				+ lsList
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND   "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " A.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " A.TRANSTIME = B.TRANSTIME AND    "
				+ " A.OFCISSUANCENO = H.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = H.SUBSTAID AND  "
				+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " A.TRANSWSID = H.TRANSWSID AND  "
				+ " A.CUSTSEQNO = H.CUSTSEQNO AND    "
				+ " B.ITMCD=C.ITMCD "
				+ " UNION ALL "
				+ " SELECT  "
				+ lsSelect
				+ " ,B.TRANSCD,  "
				+ " H.OFCISSUANCENO,  "
				+ " A.TRANSAMDATE,  "
				+ " A.TRANSTIME,  "
				+ " A.TRANSWSID,  "
				+ " A.VOIDEDTRANSINDI,  "
				+ " B.INVLOCIDCD,  "
				+ " B.INVID,  "
				+ " C.ITMCD,  "
				+ " C.ITMCDDESC,  "
				+ " B.INVITMYR,  "
				+ " B.INVITMNO,  "
				+ " B.INVENDNO,  "
				+ " B.PRNTINVQTY, "
				+ " B.INVQTY,  "
				+ " D.DELINVREASN  "
				+ " FROM  "
				+ " RTS.RTS_TRANS A,  "
				+ " RTS.RTS_TR_INV_DETAIL B,  "
				+ " RTS.RTS_ITEM_CODES C,  "
				+ " RTS.RTS_DELETE_REASONS D, "
				+ " RTS.RTS_TRANS_HDR H  "
				+ " WHERE "
				+ " A.TRANSCD ='INVVD' AND   "
				+ " A.OFCISSUANCENO = ? AND  "
				+ lsList
				+ " A.SUBSTAID = ? AND  "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND   "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND  "
				+ " A.CUSTSEQNO = B.CUSTSEQNO AND   "
				+ " A.TRANSTIME = B.TRANSTIME AND    "
				+ " B.ITMCD=C.ITMCD AND   "
				+ " B.DELINVREASNCD = D.DELINVREASNCD AND   "
				+ " B.DELINVREASNCD = 5 AND   "
				+ " A.OFCISSUANCENO = H.OFCISSUANCENO AND  "
				+ " A.SUBSTAID = H.SUBSTAID AND  "
				+ " A.TRANSAMDATE = H.TRANSAMDATE AND  "
				+ " A.TRANSWSID = H.TRANSWSID AND  "
				+ " A.CUSTSEQNO = H.CUSTSEQNO AND    "
				+ " B.INVQTY>=1 "
				+ lsOrderBy);
		// End PCR 34 
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
				" - qryInventoryDetailReport - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryDetailReport - SQL - End");
			while (laQry.next())
			{
				InventoryDetailReportData laInventoryDetailReportData =
					new InventoryDetailReportData();
				laInventoryDetailReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laQry, "OfcIssuanceNo"));
				laInventoryDetailReportData.setTransAMDate(
					caDA.getIntFromDB(laQry, "TransAMDate"));
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.EMPLOYEE
					|| aaFundsData.getFundsReportData().getPrimarySplit()
						== FundsConstant.EMPLOYEE)
				{
					laInventoryDetailReportData.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER
					|| aaFundsData.getFundsReportData().getPrimarySplit()
						== FundsConstant.CASH_DRAWER)
				{
					laInventoryDetailReportData.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}
				laInventoryDetailReportData.setTransWsId(
					caDA.getIntFromDB(laQry, "TransWsId"));
				// lInventoryDetailReportData.setCashWsId(
				// cDA.getIntFromDB(lrsQry, "CashWsId"));
				laInventoryDetailReportData.setTransTime(
					caDA.getIntFromDB(laQry, "TransTime"));
				// lInventoryDetailReportData.setTransEmpId(
				// cDA.getStringFromDB(lrsQry, "TransEmpId"));
				laInventoryDetailReportData.setTransCd(
					caDA.getStringFromDB(laQry, "TransCd"));
				laInventoryDetailReportData.setVoidedTransIndi(
					caDA.getIntFromDB(laQry, "VoidedTransIndi"));
				laInventoryDetailReportData.setInvLocIdCd(
					caDA.getStringFromDB(laQry, "InvLocIdCd"));
				laInventoryDetailReportData.setInvId(
					caDA.getStringFromDB(laQry, "InvId"));
				laInventoryDetailReportData.setItmCd(
					caDA.getStringFromDB(laQry, "ItmCd"));
				laInventoryDetailReportData.setItmCdDesc(
					caDA.getStringFromDB(laQry, "ItmCdDesc"));
				laInventoryDetailReportData.setInvItmYr(
					caDA.getIntFromDB(laQry, "InvItmYr"));
				laInventoryDetailReportData.setInvItmNo(
					caDA.getStringFromDB(laQry, "InvItmNo"));
				laInventoryDetailReportData.setInvEndNo(
					caDA.getStringFromDB(laQry, "InvEndNo"));
				laInventoryDetailReportData.setInvQty(
					caDA.getIntFromDB(laQry, "InvQty"));
				// PCR 34
				laInventoryDetailReportData.setReprntQty(
					caDA.getIntFromDB(laQry, "PRNTINVQTY"));
				// End PCR 34
				laInventoryDetailReportData.setDelInvReasn(
					caDA.getStringFromDB(laQry, "DelInvReasn"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryDetailReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryDetailReport - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryDetailReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}