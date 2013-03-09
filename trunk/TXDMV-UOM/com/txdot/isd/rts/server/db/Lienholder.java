package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.LienholderData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * Lienholder.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/12/2001	Customized Select, Insert, Update, Delete
 * K Harrell	09/18/2001	Altered test for LienhldrId to != -1
 * N Ting		09/18/2001	Altered test for WsIds to Integer.MIN_VALUE
 * K.Harrell	02/20/2002	Altered to preserve ReqSubstaid
 * R Hicks      07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	11/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	08/28/2008	No longer update Admin Cache from this class
 * 							delete updAdminCache()
 * 							modify delLienholders(),
 * 							 insLienholders(),updLienholders()
 * 							defect 8721 Ver Defect_POS_B
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeLienholders()
 * 							defect 9825 Ver Defect_POS_D   
 * K Harrell	06/26/2009	Implement new LienholderData 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add qryLienholder(LienholderData,boolean)
 * 							modify qryLienholder(LienholderData)
 * 							defect 10250 Ver Defect_POS_G
 * K Harrell	10/20/2009	Implement ReportConstant 
 * 							modify qryLienholder(LienholderData)
 * 							defect 10250 Ver Defect_POS_G        
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_LIENHOLDERS 
 * 
 * @version	Defect_POS_G	10/20/2009
 * @author 	Kathy Harrell
 * <br> Creation Date:		09/10/2001 19:33:37 
 */

public class Lienholder extends LienholderData
{
	DatabaseAccess caDA;

	/**
	 * Lienholder constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public Lienholder(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_LIENHOLDERS 
	 * Logical - Set DeleteIndi = 1
	 * 
	 * @param  aaLienholderData  LienholderData
	 * @throws RTSException
	 */
	public void delLienholder(LienholderData aaLienholderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delLienholder - Begin");

		Vector lvValues = new Vector();

		// Determine owning SubstaId     
		int liSubstaId = qryLienholderSubstaId(aaLienholderData);

		String lsDel =
			"UPDATE RTS.RTS_LIENHOLDERS SET DeleteIndi = 1, "
				+ " ChngTimestmp = CURRENT TIMESTAMP "
				+ " WHERE "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = "
				+ liSubstaId
				+ " AND LienHldrId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLienholderData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLienholderData.getId())));

			Log.write(Log.SQL, this, "delLienholder - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delLienholder - SQL - End");

			Log.write(Log.METHOD, this, "delLienholder - End");
			// defect 8721 
			// updAdminCache(aaLienholdersData, liSubstaId);
			// end defect 8721 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delLienholder - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_LIENHOLDERS
	 *
	 * @param  aaLienholderData  LienholderData
	 * @throws RTSException 
	 */
	public void insLienholder(LienholderData aaLienholderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insLienholder - Begin");
		Log.write(Log.METHOD, this, "insLienholder - First Try Update");

		AddressData laLienAddrData = aaLienholderData.getAddressData();

		// First Try Update
		int liNumRows = updLienholder(aaLienholderData);

		if (liNumRows == 0)
		{
			Log.write(
				Log.METHOD,
				this,
				"insLienholder - Next Try Insert");

			Vector lvValues = new Vector();

			// Determine Owning SubstaId			
			int liSubstaId = qryLienholderSubstaId(aaLienholderData);

			String lsIns =
				"INSERT into RTS.RTS_LIENHOLDERS ("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "LienHldrId,"
					+ "LienHldrName1,"
					+ "LienHldrName2,"
					+ "LienHldrSt1,"
					+ "LienHldrSt2,"
					+ "LienHldrCity,"
					+ "LienHldrState,"
					+ "LienHldrZpCd,"
					+ "LienHldrZpCdP4,"
					+ "LienHldrCntry,"
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
					+ " ?,"
					+ liSubstaId
					+ " ,?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " 0,"
					+ " CURRENT TIMESTAMP)";
			try
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaLienholderData.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaLienholderData.getId())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaLienholderData.getName1())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaLienholderData.getName2())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getSt1())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getSt2())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getCity())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getState())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getZpcd())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getZpcdp4())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienAddrData.getCntry())));

				Log.write(Log.SQL, this, "insLienholder - SQL - Begin");
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(Log.SQL, this, "insLienholder - SQL - End");
				Log.write(Log.METHOD, this, "insLienholder - End");
				// defect 8721 
				// updAdminCache(aaLienholdersData, liSubstaId);
				// end defect 8721 
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insLienholder - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} //if (liNumRows == 0) 
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_LIENHOLDERS for Purge
	 *
	 * @param  aiNumDays int 
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeLienholder(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeLienholder - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_LIENHOLDERS WHERE DELETEINDI = 1 AND "
				+ " days(Current Date) - days(ChngTimestmp) > ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(Log.SQL, this, "purgeLienholder - SQL - Begin");
			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeLienholder - SQL - End");
			Log.write(Log.METHOD, this, "purgeLienholder - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeLienholder - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_LIENHOLDERS
	 *
	 * @param  aaLienholderData  LienholderData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryLienholder(LienholderData aaLienholderData)
		throws RTSException
	{
		// defect 10250 
		// Sort by Id 
		return qryLienholder(aaLienholderData, ReportConstant.SORT_BY_ID);
		// end defect 10250 
	}

	/**
	 * Method to Query RTS.RTS_LIENHOLDERS
	 *
	 * @param  aaLienholderData  LienholderData	
	 * @param  abIdSort boolean 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryLienholder(
		LienholderData aaLienholderData,
		boolean abIdSort)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryLienholder - Begin");

		StringBuffer lsQry = new StringBuffer();

		// Determine Owning SubstaId 
		int liSubstaId = qryLienholderSubstaId(aaLienholderData);

		int liReqSubstaId = aaLienholderData.getSubstaId();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ liReqSubstaId
				+ " as SubstaId,"
				+ "LienHldrId,"
				+ "LienHldrName1,"
				+ "LienHldrName2,"
				+ "LienHldrSt1,"
				+ "LienHldrSt2,"
				+ "LienHldrCity,"
				+ "LienHldrState,"
				+ "LienHldrZpCd,"
				+ "LienHldrZpCdP4,"
				+ "LienHldrCntry,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_LIENHOLDERS WHERE "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = "
				+ liSubstaId);

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaLienholderData.getOfcIssuanceNo())));

		if (aaLienholderData.getId() != Integer.MIN_VALUE)
		{
			lsQry.append(" AND LienHldrId = ?");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLienholderData.getId())));
		}
		if (aaLienholderData.getChngTimestmp() == null)
		{
			lsQry.append(" AND DeleteIndi = 0");
		}
		else
		{
			lsQry.append(" AND ChngTimestmp > ?");
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaLienholderData.getChngTimestmp())));
		}

		lsQry.append(" order by 1,2,");

		String lsSortBy =
			abIdSort ? "LienHldrId" : "LienHldrName1,LienHldrName2";

		lsQry.append(lsSortBy);

		try
		{
			Log.write(Log.SQL, this, " - qryLienholder - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryLienholder - SQL - End");
			while (lrsQry.next())
			{
				LienholderData laLienholderData = new LienholderData();
				laLienholderData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laLienholderData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laLienholderData.setId(
					caDA.getIntFromDB(lrsQry, "LienHldrId"));
				laLienholderData.setName1(
					caDA.getStringFromDB(lrsQry, "LienHldrName1"));
				laLienholderData.setName2(
					caDA.getStringFromDB(lrsQry, "LienHldrName2"));
				AddressData laLienAddrData = new AddressData();
				laLienAddrData.setSt1(
					caDA.getStringFromDB(lrsQry, "LienHldrSt1"));
				laLienAddrData.setSt2(
					caDA.getStringFromDB(lrsQry, "LienHldrSt2"));
				laLienAddrData.setCity(
					caDA.getStringFromDB(lrsQry, "LienHldrCity"));
				laLienAddrData.setState(
					caDA.getStringFromDB(lrsQry, "LienHldrState"));
				laLienAddrData.setZpcd(
					caDA.getStringFromDB(lrsQry, "LienHldrZpCd"));
				laLienAddrData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "LienHldrZpCdP4"));
				laLienAddrData.setCntry(
					caDA.getStringFromDB(lrsQry, "LienHldrCntry"));
				laLienholderData.setAddressData(laLienAddrData);
				laLienholderData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laLienholderData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laLienholderData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryLienholder - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryLienholder - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to determine owning SubstaId for Lienholder
	 *
	 * @param  aaLienholderData  LienholdersData	
	 * @return int
	 * @throws RTSException 
	 */
	public int qryLienholderSubstaId(LienholderData aaLienholderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryLienholderSubstaId - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liSubstaId = Integer.MIN_VALUE;

		lsQry.append(
			"SELECT TblSubstaId  "
				+ " from RTS.RTS_SUBSTA_SUBSCR "
				+ "where "
				+ " TblName = 'RTS_LIENHOLDERS' AND "
				+ " OfcIssuanceNo = ? AND  "
				+ " SubstaId = ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaLienholderData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaLienholderData.getSubstaId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryLienholderSubstaId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryLienholderSubstaId - SQL - End");

			while (lrsQry.next())
			{
				liSubstaId = caDA.getIntFromDB(lrsQry, "TblSubstaId");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryLienholderSubstaId - End ");

			if (liSubstaId == Integer.MIN_VALUE)
			{
				liSubstaId = aaLienholderData.getSubstaId();
			}
			return (liSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryLienholderSubstaId - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_LIENHOLDERS
	 *
	 * @param  aaLienholderData  LienholderData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updLienholder(LienholderData aaLienholderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updLienholder - Begin");

		AddressData laLienAddrData = aaLienholderData.getAddressData();

		Vector lvValues = new Vector();

		// Determine owning SubstaId
		int liSubstaId = qryLienholderSubstaId(aaLienholderData);

		String lsUpd =
			"UPDATE RTS.RTS_LIENHOLDERS SET "
				+ "LienHldrName1 = ?, "
				+ "LienHldrName2 = ?, "
				+ "LienHldrSt1 = ?, "
				+ "LienHldrSt2 = ?, "
				+ "LienHldrCity = ?, "
				+ "LienHldrState = ?, "
				+ "LienHldrZpCd = ?, "
				+ "LienHldrZpCdP4 = ?, "
				+ "LienHldrCntry = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = "
				+ liSubstaId
				+ " AND LienHldrId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaLienholderData.getName1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaLienholderData.getName2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getSt1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getSt2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getCity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getState())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getZpcd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getZpcdp4())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laLienAddrData.getCntry())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLienholderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaLienholderData.getId())));

			Log.write(Log.SQL, this, "updLienholder - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updLienholder - SQL - End");
			Log.write(Log.METHOD, this, "updLienholder - End");
			// defect 8721 
			// updAdminCache(aaLienholdersData, liSubstaId);
			// end defect 8721 
			return liNumRows;
		}

		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updLienholders - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS 
