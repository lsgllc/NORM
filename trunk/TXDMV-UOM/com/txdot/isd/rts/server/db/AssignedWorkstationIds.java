package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * AssignedWorkstationIds.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	09/12/2001	Customized Select, Altered Update
 *							Removed Insert,Delete
 * K Harrell 	09/18/2001	Altered test for WsId to != -1
 * N Ting		09/18/2001	Altered test for WsIds to Integer.MIN_VALUE
 * K Harrell  	01/18/2002	add qryMinAssignedWorkstationId()
 * K Harrell	03/18/2002	modify updAssignedWorkstationIds()
 * R Hicks      07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/16/2002	add purgeAssignedWorkstationIds()
 *							defect 4601
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3      
 * Jeff S.		08/15/2006	Added new field to AssignedWorkstations 
 * 							table to manage the sticker version.
 * 							modify qryAssignedWorkstationIds(),
 * 								qryMinAssignedWorkstationId()
 * 							defect 8829 Ver 5.2.4   
 * K Harrell	09/04/2008	Minimal cleaup.  Decided to leave update to 
 * 							RTS_ADMIN_CACHE here. 
 * 							defect 8721 Ver Defect_POS_B
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeAssignedWorkstationIds()
 * 							defect 9825 Ver Defect_POS_D    
 * K Harrell	01/24/2010	add qryAssignedWorkstationIds(int,int) 
 * 							defect 10734 Ver 6.7.0    
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ASSGND_WS_IDS
 *
 * @version	6.7.0  			01/24/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/06/2001 18:16:34 
 */

public class AssignedWorkstationIds extends AssignedWorkstationIdsData
{
	private String csMethod = new String();
	DatabaseAccess caDA;

	/**
	 * AssignedWorkstationIds constructor comment.
	 * 
	 * @param  aaDA	DatabaseAccess
	 * @throws RTSException 
	 */
	public AssignedWorkstationIds(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_ASSGND_WS_IDS for Purge
	 *
	 * @param  aiNumDays	int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeAssignedWorkstationIds(int aiNumDays)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeAssignedWorkstationIds - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_ASSGND_WS_IDS WHERE DELETEINDI = 1 "
				+ "and days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(
				Log.SQL,
				this,
				"purgeAssignedWorkstationIds - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 			
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeAssignedWorkstationIds - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeAssignedWorkstationIds - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeAssignedWorkstationIds - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to query RTS.RTS_ASSGND_WS_IDS
	 * 
	 * @param  aaAssgndWsIdsData	AssignedWorkstationIdsData 	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryAssignedWorkstationIds(AssignedWorkstationIdsData aaAssgndWsIdsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryAssignedWorkstationIds - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 8829 - Added sticker version
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "WsId,"
				+ "WsCd,"
				+ "CashWsId,"
				+ "CashWsCd,"
				+ "OfcIssuanceCd,"
				+ "CPName,"
				+ "RedirPrtWsId,"
				+ "TimeZone,"
				+ "ProdStatusCd,"
				+ "DeleteIndi,"
				+ "STKRVERSIONNO,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_ASSGND_WS_IDS where OfcIssuanceNo = ? "
				+ "and SubstaId = ? ");
		// end defect 8829
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAssgndWsIdsData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaAssgndWsIdsData.getSubstaId())));

			if (aaAssgndWsIdsData.getWsId() != Integer.MIN_VALUE)
			{
				lsQry.append(" and WsId = ?");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaAssgndWsIdsData.getWsId())));
			}

			if (aaAssgndWsIdsData.getChngTimestmp() == null)
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
							aaAssgndWsIdsData.getChngTimestmp())));
			}
			lsQry.append(" order by 1,2,3");

			Log.write(
				Log.SQL,
				this,
				" - qryAssignedWorkstationIds - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryAssignedWorkstationIds - SQL - End");

			while (lrsQry.next())
			{
				AssignedWorkstationIdsData laAssgndWsIdsData =
					new AssignedWorkstationIdsData();
				laAssgndWsIdsData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laAssgndWsIdsData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laAssgndWsIdsData.setWsId(
					caDA.getIntFromDB(lrsQry, "WsId"));
				laAssgndWsIdsData.setWsCd(
					caDA.getStringFromDB(lrsQry, "WsCd"));
				laAssgndWsIdsData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laAssgndWsIdsData.setCashWsCd(
					caDA.getStringFromDB(lrsQry, "CashWsCd"));
				laAssgndWsIdsData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laAssgndWsIdsData.setCPName(
					caDA.getStringFromDB(lrsQry, "CPName"));
				laAssgndWsIdsData.setRedirPrtWsId(
					caDA.getIntFromDB(lrsQry, "RedirPrtWsId"));
				laAssgndWsIdsData.setTimeZone(
					caDA.getStringFromDB(lrsQry, "TimeZone"));
				laAssgndWsIdsData.setProdStatusCd(
					caDA.getStringFromDB(lrsQry, "ProdStatusCd"));
				laAssgndWsIdsData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				// defect 8829
				// Added sticker version
				laAssgndWsIdsData.setStkrVersionNo(
					caDA.getIntFromDB(lrsQry, "STKRVERSIONNO"));
				// end defect 8829
				laAssgndWsIdsData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laAssgndWsIdsData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryAssignedWorkstationIds - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryAssignedWorkstationIds - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to query RTS.RTS_ASSGND_WS_IDS
	 * 
	 * @param  aiOfcIssuanceNo
	 * @param  aiWsId  	
	 * @return AssignedWorkstationIdsData
	 * @throws RTSException 
	 */
	public AssignedWorkstationIdsData qryAssignedWorkstationIds(
		int aiOfcIssuanceNo,
		int aiWsId)
		throws RTSException
	{
		csMethod = "qryAssignedWorkstationIds";

		Log.write(Log.METHOD, this, csMethod = " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "WsId,"
				+ "WsCd,"
				+ "CashWsId,"
				+ "CashWsCd,"
				+ "OfcIssuanceCd,"
				+ "CPName,"
				+ "RedirPrtWsId,"
				+ "TimeZone,"
				+ "ProdStatusCd,"
				+ "DeleteIndi,"
				+ "STKRVERSIONNO,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_ASSGND_WS_IDS where OfcIssuanceNo = ? "
				+ "and WsId = ? and DeleteIndi = 0 ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOfcIssuanceNo)));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiWsId)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			AssignedWorkstationIdsData laAssgndWsIdsData = null;

			while (lrsQry.next())
			{
				laAssgndWsIdsData = new AssignedWorkstationIdsData();
				laAssgndWsIdsData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laAssgndWsIdsData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laAssgndWsIdsData.setWsId(
					caDA.getIntFromDB(lrsQry, "WsId"));
				laAssgndWsIdsData.setWsCd(
					caDA.getStringFromDB(lrsQry, "WsCd"));
				laAssgndWsIdsData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laAssgndWsIdsData.setCashWsCd(
					caDA.getStringFromDB(lrsQry, "CashWsCd"));
				laAssgndWsIdsData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laAssgndWsIdsData.setCPName(
					caDA.getStringFromDB(lrsQry, "CPName"));
				laAssgndWsIdsData.setRedirPrtWsId(
					caDA.getIntFromDB(lrsQry, "RedirPrtWsId"));
				laAssgndWsIdsData.setTimeZone(
					caDA.getStringFromDB(lrsQry, "TimeZone"));
				laAssgndWsIdsData.setProdStatusCd(
					caDA.getStringFromDB(lrsQry, "ProdStatusCd"));
				laAssgndWsIdsData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laAssgndWsIdsData.setStkrVersionNo(
					caDA.getIntFromDB(lrsQry, "STKRVERSIONNO"));
				laAssgndWsIdsData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				break;
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return laAssgndWsIdsData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_ASSGND_WS_IDS, giving the minimum 
	 * id number for an ofcissuanceno, substaid
	 * 
	 * @param  aiOfcIssuanceNo	int 
	 * @param  aiSubstaId  		int
	 * @return AssignedWorkstationIdsData
	 * @throws RTSException 
	 */
	public AssignedWorkstationIdsData qryMinAssignedWorkstationId(
		int aiOfcIssuanceNo,
		int aiSubstaId)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryMinAssignedWorkstationId - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 8829 - Added sticker version
		lsQry.append(
			"SELECT "
				+ "a.OfcIssuanceNo,"
				+ "a.SubstaId,"
				+ "a.WsId,"
				+ "a.WsCd,"
				+ "a.CashWsId,"
				+ "a.CashWsCd,"
				+ "a.OfcIssuanceCd,"
				+ "a.CPName,"
				+ "a.RedirPrtWsId,"
				+ "a.TimeZone,"
				+ "a.ProdStatusCd,"
				+ "a.DeleteIndi,"
				+ "a.STKRVERSIONNO,"
				+ "a.ChngTimestmp "
				+ "FROM RTS.RTS_ASSGND_WS_IDS a where "
				+ "a.OfcIssuanceNo = ? and a.SubstaId = ? "
				+ "and a.wsid = (select min(b.wsid)  "
				+ "FROM RTS.RTS_ASSGND_WS_IDS b where "
				+ "b.OfcIssuanceNo = ? and b.SubstaId = ?) ");
		// end defect 8829
		try
		{
			for (int i = 0; i < 2; i++)
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aiOfcIssuanceNo)));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(aiSubstaId)));
			}

			Log.write(
				Log.SQL,
				this,
				" - qryMinAssignedWorkstationId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryMinAssignedWorkstationId - SQL - End");

			AssignedWorkstationIdsData laAssgndWsIdsData = null;
			while (lrsQry.next())
			{
				laAssgndWsIdsData = new AssignedWorkstationIdsData();
				laAssgndWsIdsData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laAssgndWsIdsData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laAssgndWsIdsData.setWsId(
					caDA.getIntFromDB(lrsQry, "WsId"));
				laAssgndWsIdsData.setWsCd(
					caDA.getStringFromDB(lrsQry, "WsCd"));
				laAssgndWsIdsData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laAssgndWsIdsData.setCashWsCd(
					caDA.getStringFromDB(lrsQry, "CashWsCd"));
				laAssgndWsIdsData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laAssgndWsIdsData.setCPName(
					caDA.getStringFromDB(lrsQry, "CPName"));
				laAssgndWsIdsData.setRedirPrtWsId(
					caDA.getIntFromDB(lrsQry, "RedirPrtWsId"));
				laAssgndWsIdsData.setTimeZone(
					caDA.getStringFromDB(lrsQry, "TimeZone"));
				laAssgndWsIdsData.setProdStatusCd(
					caDA.getStringFromDB(lrsQry, "ProdStatusCd"));
				laAssgndWsIdsData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				// defect 8829
				// Added sticker version
				laAssgndWsIdsData.setStkrVersionNo(
					caDA.getIntFromDB(lrsQry, "STKRVERSIONNO"));
				// end defect 8829
				laAssgndWsIdsData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryMinAssignedWorkstationId - End ");
			return (laAssgndWsIdsData);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMinAssignedWorkstationId - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update the RTS.RTS_ASSGND_WS_IDS for the RedirPrtWsId
	 *
	 * @param  aaAssgndWsIdsData	AssignedWorkstationIdsData 
	 * @throws RTSException	
	 */
	public void updAssignedWorkstationIds(AssignedWorkstationIdsData aaAssgndWsIdsData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updAssignedWorkstationIds - Begin");

		Vector lvValues = new Vector();
		Vector lvValues1 = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_ASSGND_WS_IDS SET "
				+ "RedirPrtWsId = ?, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "WsId = ? ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaAssgndWsIdsData.getRedirPrtWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaAssgndWsIdsData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaAssgndWsIdsData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaAssgndWsIdsData.getWsId())));

		String lsUpd1 =
			"UPDATE RTS.RTS_ADMIN_CACHE SET "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE CACHETBLNAME = 'RTS_ASSGND_WS_IDS' AND "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? ";

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaAssgndWsIdsData.getOfcIssuanceNo())));
		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaAssgndWsIdsData.getSubstaId())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				"updAssignedWorkstationIds - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updAssignedWorkstationIds - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"updAssignedWorkstationIds - End");

			Log.write(
				Log.METHOD,
				this,
				"updAdminCache for AssignedWorkstationIds - Begin");
			Log.write(
				Log.SQL,
				this,
				"updAdminCache for updAssignedWorkstationIds - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd1, lvValues1);
			Log.write(
				Log.SQL,
				this,
				"updAdminCache for updAssignedWorkstationIds - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updAdminCache for updAssignedWorkstationIds - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updAssignedWorkstationIds - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
