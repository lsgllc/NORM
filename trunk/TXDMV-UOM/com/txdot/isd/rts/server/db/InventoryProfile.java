package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.InventoryProfileData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * InventoryProfile.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/09/2001  Added Code for Logical Delete
 * K Harrell    11/11/2001  Added qryInventoryProfileNextAvailIndi
 * K Harrell    12/07/2001  Removed BranchOfcId
 * K Harrell    01/10/2002  Added qryMaxSubstaIdForInventoryProfile()
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	11/24/2004	Add cleanup of RTS_INV_PROFILE to purge
 *							add purgeInventoryProfile()
 *							defect 7689 Ver 5.2.2
 * K Harrell	11/03/2005	Java 1.4 Work
 *							Removed unnecessary imports.
 *							Removed commented out references to
 *							BranchOfcId 
 *							defect 7899 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeInventoryProfile()
 * 							defect 9825 Ver Defect_POS_D   
 * K Harrell	03/19/2010	Corrected Log.write() statements 
 * 							modify qryMaxSubstaIdForInventoryProfile()
 * 							defect 10239 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database
 *
 * @version	POS_640			03/19/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001 16:53:23
 */
public class InventoryProfile extends InventoryProfileData
{
	DatabaseAccess caDA;

	/**
	 * InventoryProfile constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public InventoryProfile(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_INV_PROFILE (Mark as deleted)
	 *
	 * @param  aaInventoryProfileData  InventoryProfileData	
	 * @throws RTSException 
	 */
	public void delInventoryProfile(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delInventoryProfile - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"UPDATE RTS.RTS_INV_PROFILE SET DeleteIndi = 1, ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "Entity = ? AND "
				+ "Id = ? AND "
				+ "ItmCd = ? AND "
				+ "InvItmYr = ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getEntity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getInvItmYr())));
			Log.write(
				Log.SQL,
				this,
				"delInventoryProfile - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delInventoryProfile - SQL - End");
			Log.write(Log.METHOD, this, "delInventoryProfile - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delInventoryProfile - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_INV_PROFILE
	 *
	 * @param  aaInventoryProfileData	InventoryProfileData
	 * @throws RTSException 
	 */
	public void insInventoryProfile(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insInventoryProfile - First Tryp Update");

		int liNumRows = updInventoryProfile(aaInventoryProfileData);

		if (liNumRows == 0)
		{
			Log.write(
				Log.METHOD,
				this,
				"insInventoryProfile - Next Try Insert");

			Vector lvValues = new Vector();

			String lsIns =
				"INSERT into RTS.RTS_INV_PROFILE ("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "Entity,"
					+ "Id,"
					+ "ItmCd,"
					+ "InvItmYr,"
					+ "MaxQty,"
					+ "MinQty,"
					+ "NextAvailIndi,"
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " 0 ,"
					+ " CURRENT TIMESTAMP)";
			try
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryProfileData
								.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getSubstaId())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getEntity())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getId())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getItmCd())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getInvItmYr())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getMaxQty())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getMinQty())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryProfileData
								.getNextAvailIndi())));
				Log.write(
					Log.SQL,
					this,
					"insInventoryProfile - SQL - Begin");
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(
					Log.SQL,
					this,
					"insInventoryProfile - SQL - End");
				Log.write(
					Log.METHOD,
					this,
					"insInventoryProfile - End");
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insInventoryProfile - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} // if liNumRows 
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_INV_PROFILE for Purge
	 * 
	 * @param  aiNumDays int
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeInventoryProfile(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeInventoryProfile - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_INV_PROFILE WHERE DELETEINDI = 1 and days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryProfile - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryProfile - SQL - End");
			Log.write(Log.METHOD, this, "purgeInventoryProfile - End");
			return liNumRows;
			// end defect 9825   
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInventoryProfile - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_INV_PROFILE
	 * 
	 * @param  aaInventoryProfileData  InventoryProfileData	
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryInventoryProfile(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryInventoryProfile - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "Entity,"
				+ "Id,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "MaxQty,"
				+ "MinQty,"
				+ "NextAvailIndi,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_INV_PROFILE where DeleteIndi = 0 and OfcIssuanceNo = ? and SubstaId = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getSubstaId())));
			// If specified Entity
			if (aaInventoryProfileData.getEntity() != null)
			{
				lsQry.append("and Entity = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getEntity())));
			}
			// If specified Id
			if (aaInventoryProfileData.getId() != null)
			{
				lsQry.append("and Id = ?");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getId())));
			}
			// If specified ItmCd
			if (aaInventoryProfileData.getItmCd() != null)
			{
				lsQry.append("and ItmCd = ?");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getItmCd())));
			}
			// If specified InvItmYr
			if (aaInventoryProfileData.getInvItmYr()
				!= Integer.MIN_VALUE)
			{
				lsQry.append("and InvItmYr = ?");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryProfileData.getInvItmYr())));
			}

			lsQry.append(" order by Entity,Id,ItmCd,InvitmYr ");
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryProfile - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryProfile - SQL - End");

			while (lrsQry.next())
			{
				InventoryProfileData laInventoryProfileData =
					new InventoryProfileData();
				laInventoryProfileData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryProfileData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryProfileData.setEntity(
					caDA.getStringFromDB(lrsQry, "Entity"));
				laInventoryProfileData.setId(
					caDA.getStringFromDB(lrsQry, "Id"));
				laInventoryProfileData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryProfileData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryProfileData.setMaxQty(
					caDA.getIntFromDB(lrsQry, "MaxQty"));
				laInventoryProfileData.setMinQty(
					caDA.getIntFromDB(lrsQry, "MinQty"));
				laInventoryProfileData.setNextAvailIndi(
					caDA.getIntFromDB(lrsQry, "NextAvailIndi"));
				laInventoryProfileData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laInventoryProfileData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryProfileData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryProfile - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryProfile - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
		 * Method to Query InventoryProfile for NextAvailIndi
	 * RTS I - INVPRFC3
	 *
	 * @param  aaInventoryProfileData  InventoryProfileData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryProfileNextAvailIndi(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryProfileNextAvailIndi - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// CENTRAL:  (No Employee or Default or Workstation) 
		lsQry.append(
			" SELECT OFCISSUANCENO, SUBSTAID, ENTITY, ID, ITMCD, INVITMYR, MAXQTY, "
				+ " MINQTY, NEXTAVAILINDI, DELETEINDI, CHNGTIMESTMP  FROM "
				+ " RTS.RTS_INV_PROFILE A "
				+ " WHERE  "
				+ " OFCISSUANCENO = ? AND "
				+ " SubstaId = ? and "
				+ " ITMCD = ? AND "
				+ " INVITMYR = ? AND "
				+ " ENTITY = 'C' AND ID = '0' AND DELETEINDI = 0 AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getInvItmYr())));
		// No Workstation
		lsQry.append(
			" NOT EXISTS "
				+ " (SELECT *  FROM "
				+ " RTS.RTS_INV_PROFILE B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID AND "
				+ " A.ITMCD = B.ITMCD AND "
				+ " A.INVITMYR = B.INVITMYR AND "
				+ " ENTITY = 'W' AND DELETEINDI = 0 AND "
				+ " ID = ? ) ");
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getTransWsId())));
		// No Employee or Default   
		lsQry.append(
			" AND "
				+ " NOT EXISTS "
				+ " (SELECT *  FROM "
				+ " RTS.RTS_INV_PROFILE C "
				+ " WHERE ENTITY = 'E' AND DELETEINDI = 0 AND "
				+ " ID IN (? ,'DEFAULT') AND "
				+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ " A.SUBSTAID = C.SUBSTAID AND "
				+ " A.ITMCD = C.ITMCD AND "
				+ " A.INVITMYR = C.INVITMYR )");
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getTransEmpId())));
		// WORKSTATION:  (No Employee or Default) 
		lsQry.append(
			" UNION "
				+ " SELECT OFCISSUANCENO, SUBSTAID, ENTITY, ID, ITMCD, INVITMYR, MAXQTY, "
				+ " MINQTY, NEXTAVAILINDI,  DELETEINDI, CHNGTIMESTMP  FROM "
				+ " RTS.RTS_INV_PROFILE A WHERE "
				+ " A.OFCISSUANCENO = ? AND "
				+ " A.SUBSTAID = ? AND "
				+ " A.ITMCD = ? AND "
				+ " A.INVITMYR = ? AND "
				+ " A.ENTITY = 'W' AND "
				+ " A.ID = ? AND "
				+ " A.DELETEINDI = 0 AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getInvItmYr())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getTransWsId())));
		// No Employee or Default
		lsQry.append(
			" NOT EXISTS "
				+ " (SELECT *  FROM "
				+ " RTS.RTS_INV_PROFILE C "
				+ " WHERE ENTITY = 'E' AND DELETEINDI = 0 AND  "
				+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ " A.SUBSTAID = C.SUBSTAID AND "
				+ " A.ITMCD = C.ITMCD AND "
				+ " A.INVITMYR = C.INVITMYR AND "
				+ " ID IN (?,'DEFAULT') ) ");
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getTransEmpId())));
		// DEFAULT:  (No Employee) 
		lsQry.append(
			" UNION "
				+ " SELECT OFCISSUANCENO, SUBSTAID, ENTITY, ID, ITMCD, INVITMYR, MAXQTY, "
				+ " MINQTY, NEXTAVAILINDI,  DELETEINDI, CHNGTIMESTMP  FROM "
				+ " RTS.RTS_INV_PROFILE A "
				+ " WHERE  ENTITY = 'E' AND DELETEINDI = 0 AND "
				+ " ID = 'DEFAULT'   AND "
				+ " A.OFCISSUANCENO = ? AND "
				+ " A.SUBSTAID = ? AND "
				+ " ITMCD = ? AND "
				+ " INVITMYR = ? AND ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getInvItmYr())));
		// No Employee Id
		lsQry.append(
			" NOT EXISTS "
				+ " (SELECT *  FROM "
				+ " RTS.RTS_INV_PROFILE D "
				+ " WHERE ENTITY = 'E' AND DELETEINDI = 0 AND "
				+ " ID=? AND "
				+ " A.OFCISSUANCENO = D.OFCISSUANCENO AND "
				+ " A.SUBSTAID = D.SUBSTAID AND "
				+ " A.ITMCD = D.ITMCD AND "
				+ " A.INVITMYR = D.INVITMYR ) ");
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getTransEmpId())));
		// EMPLOYEE:
		lsQry.append(
			" UNION "
				+ " SELECT OFCISSUANCENO, SUBSTAID, ENTITY, ID, ITMCD, INVITMYR, MAXQTY, "
				+ " MINQTY, NEXTAVAILINDI,  DELETEINDI, CHNGTIMESTMP  FROM "
				+ " RTS.RTS_INV_PROFILE "
				+ " WHERE ENTITY = 'E' AND DELETEINDI = 0 AND "
				+ " OFCISSUANCENO = ? AND "
				+ " SUBSTAID = ? AND "
				+ " ID = ? AND "
				+ " ITMCD = ? AND "
				+ " INVITMYR = ? ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getTransEmpId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getInvItmYr())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryProfileNextAvailIndi - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryProfileNextAvailIndi - SQL - End");
			while (lrsQry.next())
			{
				InventoryProfileData laInventoryProfileData =
					new InventoryProfileData();
				laInventoryProfileData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryProfileData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryProfileData.setEntity(
					caDA.getStringFromDB(lrsQry, "Entity"));
				laInventoryProfileData.setId(
					caDA.getStringFromDB(lrsQry, "Id"));
				laInventoryProfileData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryProfileData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryProfileData.setMaxQty(
					caDA.getIntFromDB(lrsQry, "MaxQty"));
				laInventoryProfileData.setMinQty(
					caDA.getIntFromDB(lrsQry, "MinQty"));
				laInventoryProfileData.setNextAvailIndi(
					caDA.getIntFromDB(lrsQry, "NextAvailIndi"));
				laInventoryProfileData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laInventoryProfileData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryProfileData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryProfileNextAvailIndi - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryProfileNextAvailIndi - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query if Subscribing
	 *
	 * @param  aaInventoryProfileData  InventoryProfileData
	 * @return int
	 * @throws RTSException 
	 */
	public int qryIsSubscribing(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " -qyrIsSubscribing - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liIsSubscribing = 0;
		String lsTblName = "";
		if (aaInventoryProfileData.getEntity() == "D")
		{
			lsTblName = "RTS_DEALERS";
		}
		if (aaInventoryProfileData.getEntity() == "S")
		{
			lsTblName = "RTS_SUBCON";
		}
		if (aaInventoryProfileData.getEntity() == "E")
		{
			lsTblName = "RTS_SECURITY";
		}

		lsQry.append(
			"Select 1 as IsSubscribing from RTS.RTS_SUBSTA_SUBSCR where "
				+ " tblname = '"
				+ lsTblName
				+ "' and "
				+ " ofcissuanceno = ? and "
				+ " (substaid = ? or "
				+ " (? = 0 and "
				+ " exists "
				+ " (select * from RTS.RTS_SUBSTA_SUBSCR where ofcissuanceno = ? and tblname = "
				+ "'"
				+ lsTblName
				+ "'"
				+ " )"
				+ " )"
				+ " )");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryProfileData.getOfcIssuanceNo())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" -qyrIsSubscribing - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " -qyrIsSubscribing - SQL - End");

			while (lrsQry.next())
			{
				liIsSubscribing =
					caDA.getIntFromDB(lrsQry, "IsSubscribing");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " -qryIsSubscribing - End ");
			return (liIsSubscribing);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIsSubscribing - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to determine maximum substaid InventoryProfile 
	 * for given entity.  Used when attempt to delete Employee, 
	 * Dealer, Subcon
	 *
	 * @param  aaInventoryProfileData  InventoryProfileData	
	 * @return int
	 * @throws RTSException 
	 */
	public int qryMaxSubstaIdForInventoryProfile(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryMaxSubstaIdForInventoryProfile - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		int liMaxSubstaId = Integer.MIN_VALUE;

		String lsTblName = "";

		if (aaInventoryProfileData.getEntity() == "D")
		{
			lsTblName = "RTS_DEALERS";
		}
		if (aaInventoryProfileData.getEntity() == "S")
		{
			lsTblName = "RTS_SUBCON";
		}
		if (aaInventoryProfileData.getEntity() == "E")
		{
			lsTblName = "RTS_SECURITY";
		}

		int liIsSubscribing = qryIsSubscribing(aaInventoryProfileData);

		if (liIsSubscribing == 1)
		{
			lsQry.append(
				"Select DISTINCT SubstaId from RTS.RTS_INV_PROFILE a where "
					+ " deleteindi = 0 and "
					+ " entity = ? and "
					+ " id = ? and "
					+ " ofcissuanceno = ? and "
					+ " (substaid = 0 or substaid = "
					+ " (select max(substaid) from RTS.RTS_INV_PROFILE B "
					+ " where a.ofcissuanceno = b.ofcissuanceno and a.substaid = b.substaid and B.entity = ? "
					+ " and B.id = ? and deleteindi = 0 and "
					+ " substaid in "
					+ " (select SubstaId from RTS.RTS_SUBSTA_SUBSCR where ofcissuanceno = ? and tblname = '"
					+ lsTblName
					+ "')))");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getEntity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getEntity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getOfcIssuanceNo())));
		}
		else
		{
			lsQry.append(
				"Select distinct SubstaId from RTS.RTS_INV_PROFILE where "
					+ " entity = ? and deleteindi = 0 and "
					+ " id = ? and "
					+ " OfcIssuanceNo = ? and "
					+ " substaid = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getEntity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getSubstaId())));
		}
		try
		{
			// defect 10239 
			// Incorrect method name 
			Log.write(Log.SQL, this,
			//qryInventoryProfile - SQL - Begin");
			" - qryMaxSubstaIdForInventoryProfile - SQL - Begin");
			// end defect 10239 
			
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			
			// defect 10239
			// Incorrect method name 
			Log.write(Log.SQL, this,
			//" - qryInventoryProfile - SQL - End");
			" - qryMaxSubstaIdForInventoryProfile - SQL - End");
			// end defect 10239 

			while (lrsQry.next())
			{
				liMaxSubstaId = caDA.getIntFromDB(lrsQry, "SubstaId");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" -  qryMaxSubstaIdForInventoryProfile - End ");
			return (liMaxSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMaxSubstaIdForInventoryProfile - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update a InventoryProfile
	 *
	 * @param  aaInventoryProfileData  InventoryProfileData
	 * @return int
	 * @throws RTSException 
	 */
	public int updInventoryProfile(InventoryProfileData aaInventoryProfileData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updInventoryProfile - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_INV_PROFILE SET "
				+ "MaxQty = ?, "
				+ "MinQty = ?, "
				+ "NextAvailIndi = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "Entity = ? AND "
				+ "Id = ? AND "
				+ "ItmCd = ? AND "
				+ "InvItmYr = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getMaxQty())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getMinQty())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getNextAvailIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getEntity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryProfileData.getInvItmYr())));

			Log.write(
				Log.SQL,
				this,
				"updInventoryProfile - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updInventoryProfile - SQL - End");
			Log.write(Log.METHOD, this, "updInventoryProfile - End");
			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryProfile - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
