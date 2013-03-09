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
 * InventorySummaryTypeReportSQL.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	12/03/2001  Customized for lList and lSelect
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify qryInventorySummaryTypeReportSQL
 * 							Ver 5.2.0
 * K Harrell	06/24/2004	modify qryInventorySummaryTypeReportSQL()
 *							defect 7223 Ver 5.2.0 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/07/2008	Update to handle no workstation in list
 * 							modify qryInventorySummaryTypeReportSQL()
 * 							defect 8913 Ver MyPlates_POS 	   		
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database
 * 
 * @version	MyPlates_POS	08/07/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/04/2001
 */
public class InventorySummaryTypeReportSQL
{
	DatabaseAccess caDA;

	/**
	 * InventorySummaryTypeReportSQL constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public InventorySummaryTypeReportSQL(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Retrieve data for InventorySummaryTypeReport
	 * 
	 * @param aaFundsData FundsData	
	 * @return Vector 	
	 * @throws RTSException
	 */
	public Vector qryInventorySummaryTypeReportSQL(FundsData aaFundsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventorySummaryTypeReportSQL - Begin");
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
		String lsOrderby = "";
		if (lbByEmployee && lbByCashDrawer)
		{
			lsOrderby = "1,2,4,5,3";
		}
		else
		{
			lsOrderby = "1,3,4,2";
		}
		String lsSelect = laFSData.getSelect();
		String lsList = laFSData.getList();
		
		// defect 8913 
		// If no entities in list, return empty vector 
		if (lsList.equals(" () AND "))
		{
			return lvRslt;
		}
		// end defect 8913 
		
		ResultSet laQry;
		lsQry
			.append(
				" SELECT   "
				+ lsSelect
				+ " ,'SOLD' AS INVSTATUS,  "
				+ " C.ITMCDDESC,  "
				+ " B.INVITMYR, "
				+ " SUM(B.PRNTINVQTY-1) AS PRNTINVQTY, "
		// PCR 34 & defect 7223
		+" SUM(B.INVQTY) AS INVQTY   "
			+ " FROM "
			+ " RTS.RTS_TR_INV_DETAIL B,  "
			+ " RTS.RTS_ITEM_CODES C, "
			+ " RTS.RTS_TRANS_HDR H  "
			+ " WHERE  "
			+ lsList
			+ " B.OFCISSUANCENO = ? AND  "
			+ " B.SUBSTAID = ? AND  "
			+ " B.OFCISSUANCENO = H.OFCISSUANCENO AND  "
			+ " B.SUBSTAID = H.SUBSTAID AND  "
			+ " B.TRANSAMDATE =  H.TRANSAMDATE AND  "
			+ " B.TRANSWSID = H.TRANSWSID AND  "
			+ " B.CUSTSEQNO = H.CUSTSEQNO AND   "
			+ " B.TRANSCD NOT LIKE 'INV%' AND   "
			+ " B.INVLOCIDCD<> 'V' AND    "
			+ " B.ITMCD=C.ITMCD AND   "
			+ " B.INVQTY>=1  "
			+ " GROUP BY "
			+ lsSelect
			+ ",C.ITMCDDESC, B.INVITMYR, PRNTINVQTY ");
		// PCR 34 & defect 7223
		// PCR 34
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
		lsQry
			.append(
				" UNION ALL SELECT   "
				+ lsSelect
				+ " ,'REUSED' AS INVSTATUS,  "
				+ " C.ITMCDDESC,  "
				+ " B.INVITMYR,  "
				+ " SUM(B.PRNTINVQTY-1) AS PRNTINVQTY, "
		// PCR 34 & defect 7223
		+" COUNT(*) AS INVQTY  "
			+ " FROM "
			+ " RTS.RTS_TR_INV_DETAIL B, "
			+ " RTS.RTS_ITEM_CODES C, "
			+ " RTS.RTS_TRANS_HDR H   "
			+ " WHERE  "
			+ lsList
			+ " B.OFCISSUANCENO = ? AND  "
			+ " B.SUBSTAID = ? AND  "
			+ " B.OFCISSUANCENO = H.OFCISSUANCENO AND  "
			+ " B.SUBSTAID = H.SUBSTAID AND  "
			+ " B.TRANSAMDATE = H.TRANSAMDATE AND  "
			+ " B.TRANSWSID = H.TRANSWSID AND  "
			+ " B.CUSTSEQNO = H.CUSTSEQNO AND    "
			+ " B.ITMCD=C.ITMCD  AND  "
			+ " B.INVLOCIDCD='V' AND    "
			+ " B.TRANSCD NOT LIKE 'INV%' "
			+ " GROUP BY "
			+ lsSelect
			+ " ,C.ITMCDDESC, B.INVITMYR, PRNTINVQTY ");
		// PCR 34 & defect 7223

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
		lsQry
			.append(
				" UNION ALL SELECT   "
				+ lsSelect
				+ " ,'VOIDED' as INVSTATUS,  "
				+ " C.ITMCDDESC,  "
				+ " B.INVITMYR,  "
				+ " SUM(B.PRNTINVQTY-1) AS PRNTINVQTY, "
		// PCR 34 & defect 7223
		+" SUM(B.INVQTY) AS INVQTY   "
			+ " FROM "
			+ " RTS.RTS_TR_INV_DETAIL B,  "
			+ " RTS.RTS_ITEM_CODES C, "
			+ " RTS.RTS_TRANS_HDR H  "
			+ " WHERE  "
			+ lsList
			+ " B.OFCISSUANCENO = ? AND  "
			+ " B.SUBSTAID = ? AND  "
			+ " B.OFCISSUANCENO = B.OFCISSUANCENO AND  "
			+ " B.ITMCD=C.ITMCD AND   "
			+ " B.DELINVREASNCD = 5 AND   "
			+ " B.OFCISSUANCENO = H.OFCISSUANCENO AND  "
			+ " B.SUBSTAID = H.SUBSTAID AND  "
			+ " B.TRANSAMDATE = H.TRANSAMDATE AND  "
			+ " B.TRANSWSID = H.TRANSWSID AND  "
			+ " B.CUSTSEQNO = H.CUSTSEQNO AND    "
			+ " B.TRANSCD ='INVVD' AND   "
			+ " B.INVQTY>=1 "
			+ " GROUP BY "
			+ lsSelect
			+ ",C.ITMCDDESC, B.INVITMYR, PRNTINVQTY order by "
		// PCR 34 & defect 7223
		+lsOrderby);
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
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventorySummaryTypeReportSQL - SQL - Begin");
			laQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventorySummaryTypeReportSQL - SQL - End");
			while (laQry.next())
			{
				InventorySummaryTypeReportData laInventorySummaryTypeReportData =
					new InventorySummaryTypeReportData();
				if (lbByEmployee)
				{
					laInventorySummaryTypeReportData.setTransEmpId(
						caDA.getStringFromDB(laQry, "TransEmpId"));
				}
				if (lbByCashDrawer)
				{
					laInventorySummaryTypeReportData.setCashWsId(
						caDA.getIntFromDB(laQry, "CashWsId"));
				}
				laInventorySummaryTypeReportData.setInvItmYr(
					caDA.getIntFromDB(laQry, "InvItmYr"));
				laInventorySummaryTypeReportData.setInvStatus(
					caDA.getStringFromDB(laQry, "InvStatus"));
				laInventorySummaryTypeReportData.setItmCdDesc(
					caDA.getStringFromDB(laQry, "ItmCdDesc"));
				laInventorySummaryTypeReportData.setInvQty(
					caDA.getStringFromDB(laQry, "InvQty"));
				// PCR 34 & defect 7223
				int liReprntQty =
					caDA.getIntFromDB(laQry, "PRNTINVQTY");
				if (liReprntQty < 0)
				{
					liReprntQty = 0;
				}
				laInventorySummaryTypeReportData.setReprntQty(
					liReprntQty);
				// PCR 34 & defect 7223 
				// Add element to the Vector
				lvRslt.addElement(laInventorySummaryTypeReportData);
			} //End of While 
			laQry.close();
			laQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventorySummaryTypeReportSQL - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventorySummaryTypeReportSQL - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
