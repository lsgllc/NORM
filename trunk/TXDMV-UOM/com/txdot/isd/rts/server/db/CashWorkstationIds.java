package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.CashWorkstationIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * CashWorkstationIds.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell  	09/12/2001  Customized Select, Removed Delete,Insert
 *                          Modified Update 
 * K Harrell	09/18/2001  Altered test for CashWsId to != -1
 * N Ting		09/18/2001	Altered test for WsIds to Integer.MIN_VALUE
 * K Harrell	10/17/2001  Altered updCashWorkstationIds for CloseOut
 * K Harrell 	10/18/2001  In upd parm, altered to CashWorkstationIdsData
 * K Harrell  	03/18/2002  Added update of Admin_Cache
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell    08/16/2002  Added purgeCashWorkstationIds
 *							defect 4601
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	03/17/2006	Do not update RTS_ADMIN_CACHE on update
 * 							of RTS_CASH_WS_IDS 
 * 							modify updCashWorkstationIds() 
 * 							defect 8623 Ver 5.2.3 
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeCashWorkstationIds()
 * 							defect 9825 Ver Defect_POS_D  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_CASH_WS_IDS
 * 
 * @version	5.2.3		03/17/2006
 * @author	Kathy Harrell
 * <br>Creation Date:	09/05/2001 12:23:23 
 */

public class CashWorkstationIds extends CashWorkstationIdsData
{
	DatabaseAccess caDA;
	/**
	 * CashWorkstationIds constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public CashWorkstationIds(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Delete from RTS.RTS_CASH_WS_IDS for Purge
	 * 
	 * @param  aiNumDays int 
	 * @return int
	 * @throws RTSException
	 */
	public int purgeCashWorkstationIds(int aiNumDays)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeCashWorkstationIds - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_CASH_WS_IDS WHERE DELETEINDI = 1 and "
				+ " days(Current Date) - days(ChngTimestmp) > ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(
				Log.SQL,
				this,
				"purgeCashWorkstationIds - SQL - Begin");
				
			// defect 9825 
			// Return number of rows purged 
			int liNumRows = caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeCashWorkstationIds - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeCashWorkstationIds - End");
			return liNumRows;
			// end defect 9825  
		}

		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeCashWorkstationIds - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Query RTS.RTS_CASH_WS_IDS
	 * 
	 * @param  aaCashWsIdsData	CashWorkstationIdsData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCashWorkstationIds(CashWorkstationIdsData aaCashWsIdsData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCashWorkstationIds - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "CashWsId,"
				+ "CurrStatTimestmp,"
				+ "CloseOutReqWsId,"
				+ "CloseOutReqIndi,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_CASH_WS_IDS where OfcIssuanceNo = ? "
				+ "and SubstaId = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsIdsData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsIdsData.getSubstaId())));

			if (aaCashWsIdsData.getCloseOutReqIndi() == 1)
			{
				lsQry.append(" and CloseOutReqIndi = 1");
			}

			if (aaCashWsIdsData.getCashWsId() != Integer.MIN_VALUE)
			{
				lsQry.append(" and CashWsId = ?");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCashWsIdsData.getCashWsId())));
			}

			if (aaCashWsIdsData.getChngTimestmp() == null)
			{
				lsQry.append(" and DeleteIndi = 0");
			}

			else
			{
				lsQry.append(" and ChngTimestmp > ?");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaCashWsIdsData.getChngTimestmp())));
			}

			lsQry.append(" order by 1,2,3");

			Log.write(
				Log.SQL,
				this,
				" - qryCashWorkstationIds - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCashWorkstationIds - SQL - End");

			while (lrsQry.next())
			{
				CashWorkstationIdsData laCashWSIdsData =
					new CashWorkstationIdsData();
				laCashWSIdsData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCashWSIdsData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCashWSIdsData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laCashWSIdsData.setCurrStatTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "CurrStatTimestmp"));
				laCashWSIdsData.setCloseOutReqWsId(
					caDA.getIntFromDB(lrsQry, "CloseOutReqWsId"));
				laCashWSIdsData.setCloseOutReqIndi(
					caDA.getIntFromDB(lrsQry, "CloseOutReqIndi"));
				laCashWSIdsData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laCashWSIdsData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laCashWSIdsData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCashWorkstationIds - End ");

			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCashWorkstationIds - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_CASH_WS_IDS
	 * 
	 * @param  aaCashWsIdsData CashWorkstationIdsData
	 * @return int 
	 * @throws RTSException
	 */
	public int updCashWorkstationIds(CashWorkstationIdsData aaCashWsIdsData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updCashWorkstationIds - Begin");

		Vector lvValues = new Vector();

		boolean lbResetIndi = false;

		try
		{
			// Fixed every Update 
			String lsUpd =
				"UPDATE RTS.RTS_CASH_WS_IDS SET CloseOutReqIndi = ?, "
					+ " ChngTimestmp = CURRENT TIMESTAMP ";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsIdsData.getCloseOutReqIndi())));
			String lsWhere =
				"WHERE "
					+ " OfcIssuanceNo = ? AND SubstaId = ? AND "
					+ " CashWsId = ? ";

			// For Current Status 
			if (aaCashWsIdsData.getCloseOutReqWsId()
				== Integer.MIN_VALUE)
			{
				lsUpd = lsUpd + " ,CloseOutReqWsId = null ";
			}
			else // For CloseOut Request 
				{
				lbResetIndi = true;
				lsUpd = lsUpd + " ,CloseOutReqWsId = ?,  ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCashWsIdsData.getCloseOutReqWsId())));
			}
			if (aaCashWsIdsData.getCurrStatTimestmp() != null)
			{
				lsUpd = lsUpd + " , CurrStatTimestmp = ? ";
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaCashWsIdsData.getCurrStatTimestmp())));
			}
			// Where Clause 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsIdsData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsIdsData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCashWsIdsData.getCashWsId())));
			if (lbResetIndi)
			{
				//Altered where clause 
				lsWhere =
					lsWhere
						+ " and (CloseOutReqIndi = 0 or "
						+ " CloseOutReqWsId = ?  )";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCashWsIdsData.getCloseOutReqWsId())));
			}
			lsUpd = lsUpd + lsWhere;

			Log.write(
				Log.SQL,
				this,
				"updCashWorkstationIds - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updCashWorkstationIds - SQL - End");
			Log.write(Log.METHOD, this, "updCashWorkstationIds - End");

			// defect 8623
			// Do not update Admin_Cache for RTS_CASH_WS_IDS
			// 
			// Vector lvValues1 = new Vector();
			//  
			//			String lsUpd1 =
			//				"UPDATE RTS.RTS_ADMIN_CACHE SET "
			//					+ " ChngTimestmp = CURRENT TIMESTAMP "
			//					+ " WHERE CACHETBLNAME = 'RTS_CASH_WS_IDS' AND "
			//					+ " OfcIssuanceNo = ? AND "
			//					+ " SubstaId = ? ";
			//			lvValues1.addElement(
			//				new DBValue(
			//					Types.INTEGER,
			//					DatabaseAccess.convertToString(
			//						aaCashWsIdsData.getOfcIssuanceNo())));
			//			lvValues1.addElement(
			//				new DBValue(
			//					Types.INTEGER,
			//					DatabaseAccess.convertToString(
			//						aaCashWsIdsData.getSubstaId())));
			//			Log.write(
			//				Log.SQL,
			//				this,
			//				"updAdminCache for updCashWorkstationIds - SQL - Begin");
			//			caDA.executeDBInsertUpdateDelete(lsUpd1, lvValues1);
			//			Log.write(
			//				Log.SQL,
			//				this,
			//				"updAdminCache for updCashWorkstationIds - SQL - End");
			//			Log.write(
			//				Log.METHOD,
			//				this,
			//				"updAdminCache for updCashWorkstationIds - End");
			// end defect 8623
			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updCashWorkstationIds - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
