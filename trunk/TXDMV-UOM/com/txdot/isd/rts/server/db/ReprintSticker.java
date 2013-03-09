package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.ReprintData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * ReprintSticker.java
 *
 * (c) Texas Department of Transportation 2002
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Imported. Ver 5.2.0
 * K Harrell	07/30/2004	qryAllReprintSticker will return only data
 *							associate with region 
 *							modify qryAllReprintSticker() 
 *							defect 7385  Ver 5.2.1
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							ReprintStickerData Object.
 * 							modify purgeReprintSticker() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	03/09/2007	Use OfficeIdsCache.isRegion()
 * 							modify qryAllReprintSticker()
 * 							defect 9085 Ver Special Plates
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeReprintSticker()
 * 							defect 9825 Ver Defect_POS_D   
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_REPRNT_STKR
 * 
 * @version	Special Plates	03/09/2007  
 * @author	Michael Abernethy
 * <br>Creation Date:		09/24/2002 11:03:54
 */

public class ReprintSticker implements java.io.Serializable
{
	DatabaseAccess caDA;
	/**
	 * ReprintSticker constructor comment.
	 * 
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public ReprintSticker(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Update/Insert into RTS_REPRNT_STKR
	 * 
	 * @param avReprintData Vector
	 * @throws RTSException 
	 */
	public void insReprintSticker(Vector avReprintData)
		throws RTSException
	{
		// insert rows into ReprintSticker
		// should already be sorted by ofc, lsOrigination, item code, and item year
		// row will always be unique since it's a daily summary, and the
		// reprint date will make each row unique
		int liNumReprintRows = avReprintData.size();
		for (int i = 0; i < liNumReprintRows; i++)
		{
			ReprintData laReprintData =
				(ReprintData) avReprintData.get(i);
			try
			{
				ReprintData laStickerRow = null;
				try
				{
					// Determine if row with given criteria exists
					laStickerRow = qryReprintSticker(laReprintData);
				}
				catch (RTSException aeRTSEx)
				{
				}
				if (laStickerRow != null)
				{
					// Row exists; Update 
					int liPriorReprintCount = laStickerRow.getPrntQty();
					laReprintData.setPrntQty(
						liPriorReprintCount
							+ laReprintData.getPrntQty());
					updateReprintStickerQuantity(laReprintData);
				}
				else
				{
					// No prior row exists with criteria; Insert 
					insSingleReprintSticker(laReprintData);
				}
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
	}
	/**
	 * Insert Single Reprint Sticker
	 * 
	 * @param  aaReprintStickerData   ReprintData
	 * @throws RTSException
	 */
	protected void insSingleReprintSticker(ReprintData aaReprintStickerData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insSingleReprintSticker - Begin");

		Vector lvValues = new Vector();

		int liOfcIssuanceNo = aaReprintStickerData.getOfcIssuanceNo();

		int liQuantity = aaReprintStickerData.getPrntQty();

		int liReprintDate =
			aaReprintStickerData.getReprntDate().getAMDate();

		String lsOrigin = aaReprintStickerData.getOrigin();

		String lsItemCode = aaReprintStickerData.getItmCd();

		int liItemYear = aaReprintStickerData.getItmYr();

		//Use StringBuffer to optimize construction of the query string.
		StringBuffer lsDel = new StringBuffer();
		lsDel.append("INSERT INTO RTS.RTS_REPRNT_STKR (");
		lsDel.append("OFCISSUANCENO, ");
		lsDel.append("REPRNTDATE, ");
		lsDel.append("ORIGINCD, ");
		lsDel.append("ITMCD, ");
		lsDel.append("INVITMYR, ");
		lsDel.append("REPRNTINVQTY) VALUES (");
		lsDel.append("?,?,?,?,?,?)");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liOfcIssuanceNo)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liReprintDate)));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsOrigin)));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsItemCode)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liItemYear)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liQuantity)));
			Log.write(
				Log.SQL,
				this,
				"insSingleReprintSticker - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(
				lsDel.toString(),
				lvValues);
			Log.write(
				Log.SQL,
				this,
				"insSingleReprintSticker - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insSingleReprintSticker - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insSingleReprintSticker - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	/**
	 * Delete from RTS_REPRNT_STKR during batch purge
	 * 
	 * @param  aiPurgeAMDate int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeReprintSticker(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeReprintSticker - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_REPRNT_STKR WHERE  REPRNTDATE <= ?";
		try
		{
			// defect 8423
			// Use passed int 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423		

			Log.write(
				Log.SQL,
				this,
				"purgeReprintSticker - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeReprintSticker - SQL - End");
			Log.write(Log.METHOD, this, "purgeReprintSticker - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeReprintSticker - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	/**
	 * Query  All Reprint Sticker
	 * 
	 * @param aaReprintStickerData	ReprintData
	 * @param aaRTSStartDate  		RTSDate
	 * @param aaRTSEndDate			RTSDate
	 * @return java.util.Vector
	 * @throws RTSException
	 */
	public Vector qryAllReprintSticker(
		ReprintData aaReprintStickerData,
		RTSDate aaRTSStartDate,
		RTSDate aaRTSEndDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "qryReprintSticker - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		int aiRTSStartDate = aaRTSStartDate.getAMDate();

		int aiRTSEndDate = aaRTSEndDate.getAMDate();

		ResultSet lrsQry = null;

		lsQry.append("SELECT ");
		lsQry.append("OFCISSUANCENO, ");
		lsQry.append("ORIGINCD, ");
		lsQry.append("A.ITMCD, ");
		lsQry.append("B.ITMCDDESC, ");
		lsQry.append("INVITMYR, ");
		lsQry.append("SUM(REPRNTINVQTY) AS REPRNTCOUNT ");
		lsQry.append(
			"FROM RTS.RTS_REPRNT_STKR A, RTS.RTS_ITEM_CODES B WHERE A.ITMCD = B.ITMCD AND ");
		lsQry.append("A.REPRNTDATE <= ? ");
		lsQry.append("AND A.REPRNTDATE >= ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiRTSEndDate)));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiRTSStartDate)));
		// defect 9085 
		// defect 7385 
		if (OfficeIdsCache
			.isRegion(aaReprintStickerData.getOfcIssuanceNo()))
		{
			lsQry.append(
				" AND (A.OFCISSUANCENO IN (SELECT OFCISSUANCENO FROM RTS.RTS_OFFICE_IDS C WHERE ");
			lsQry.append(" C.REGNLOFCID = ?) OR A.OFCISSUANCENO = ? )");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintStickerData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintStickerData.getOfcIssuanceNo())));
		}
		// end defect 7385 
		// end defect 9085 
		lsQry.append(
			"GROUP BY OFCISSUANCENO, ORIGINCD, A.ITMCD, B.ITMCDDESC,  INVITMYR ");

		lsQry.append(
			"ORDER BY OFCISSUANCENO, ORIGINCD, A.ITMCD, INVITMYR");

		try
		{
			Log.write(
				Log.SQL,
				this,
				"qryAllReprintSticker - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				"qryAllReprintSticker - SQL - End");

			while (lrsQry.next())
			{
				ReprintData laReprintStickerReportData =
					new ReprintData();
				laReprintStickerReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OFCISSUANCENO"));
				laReprintStickerReportData.setOrigin(
					caDA.getStringFromDB(lrsQry, "ORIGINCD"));
				laReprintStickerReportData.setPrntQty(
					caDA.getIntFromDB(lrsQry, "REPRNTCOUNT"));
				laReprintStickerReportData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ITMCD"));
				laReprintStickerReportData.setItmYr(
					caDA.getIntFromDB(lrsQry, "INVITMYR"));
				laReprintStickerReportData.setItemDescription(
					caDA.getStringFromDB(lrsQry, "ITMCDDESC"));
				lvRslt.add(laReprintStickerReportData);
			}
			Log.write(Log.METHOD, this, "qryAllReprintSticker - End ");
			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryAllReprintSticker - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			//Always close db connections, resultsets, etc in a finally block
			if (lrsQry != null)
			{
				try
				{
					lrsQry.close();
				}
				catch (Exception leEx)
				{
					//eat up the exception here.
				}
			}
			caDA.closeLastDBStatement();
		}
	}
	/**
	 * Query Reprint Sticker
	 * 
	 * @param  aaReprintStickerData  ReprintData
	 * @return ReprintData
	 * @throws RTSException
	 */
	private ReprintData qryReprintSticker(ReprintData aaReprintStickerData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "qryReprintSticker - Begin");

		StringBuffer lsQry = new StringBuffer();

		ReprintData laReprintStickerReportData = null;

		Vector lvValues = new Vector();

		int liOfcIssuanceNo = aaReprintStickerData.getOfcIssuanceNo();

		int liReprintDate =
			aaReprintStickerData.getReprntDate().getAMDate();

		String lsOrigin = aaReprintStickerData.getOrigin();

		String lsItemCode = aaReprintStickerData.getItmCd();

		int liItemYear = aaReprintStickerData.getItmYr();

		ResultSet lrsQry = null;

		lsQry.append("SELECT ");
		lsQry.append("OFCISSUANCENO, ");
		lsQry.append("REPRNTDATE, ");
		lsQry.append("ORIGINCD, ");
		lsQry.append("ITMCD, ");
		lsQry.append("INVITMYR, ");
		lsQry.append("REPRNTINVQTY ");
		lsQry.append("FROM RTS.RTS_REPRNT_STKR A WHERE ");
		lsQry.append("A.OFCISSUANCENO = ? ");
		lsQry.append("AND A.REPRNTDATE = ? ");
		lsQry.append("AND A.ORIGINCD = ? ");
		lsQry.append("AND A.ITMCD = ? ");
		lsQry.append("AND A.INVITMYR = ?");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liOfcIssuanceNo)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liReprintDate)));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsOrigin)));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsItemCode)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liItemYear)));

			Log.write(Log.SQL, this, "qryReprintSticker - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, "qryReprintSticker - SQL - End");

			if (lrsQry.next())
			{
				laReprintStickerReportData = new ReprintData();
				laReprintStickerReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OFCISSUANCENO"));
				laReprintStickerReportData.setOrigin(
					caDA.getStringFromDB(lrsQry, "ORIGINCD"));
				laReprintStickerReportData.setPrntQty(
					caDA.getIntFromDB(lrsQry, "REPRNTINVQTY"));
				laReprintStickerReportData.setReprntDate(
					new RTSDate(
						RTSDate.AMDATE,
						caDA.getIntFromDB(lrsQry, "REPRNTDATE")));
				laReprintStickerReportData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ITMCD"));
				laReprintStickerReportData.setItmYr(
					caDA.getIntFromDB(lrsQry, "INVITMYR"));
			}
			Log.write(Log.METHOD, this, "qryReprintSticker - End ");
			return laReprintStickerReportData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryReprintSticker - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			// Always close db connections, resultsets, 
			// etc in a finally block
			if (lrsQry != null)
			{
				try
				{
					lrsQry.close();
				}
				catch (Exception aeRTSEx)
				{
				}
			}
			caDA.closeLastDBStatement();
		}
	}
	/**
	 * Update ReprintStickerQuantity
	 * 
	 * @param  aaReprintStickerData  ReprintData
	 * @throws RTSException
	 */
	private void updateReprintStickerQuantity(ReprintData aaReprintStickerData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updateReprintStickerQuantity - Begin");

		Vector lvValues = new Vector();

		int liOfcIssuanceNo = aaReprintStickerData.getOfcIssuanceNo();

		int liQuantity = aaReprintStickerData.getPrntQty();

		int liReprintDate =
			aaReprintStickerData.getReprntDate().getAMDate();

		String lsOrigin = aaReprintStickerData.getOrigin();

		String lsItemCode = aaReprintStickerData.getItmCd();

		int liItemYear = aaReprintStickerData.getItmYr();

		//Use StringBuffer to optimize construction of the query string.
		StringBuffer lsDel = new StringBuffer();

		lsDel.append("UPDATE RTS.RTS_REPRNT_STKR SET REPRNTINVQTY = ?");
		lsDel.append("WHERE ");
		lsDel.append("OFCISSUANCENO = ? AND ");
		lsDel.append("REPRNTDATE = ? AND ");
		lsDel.append("ORIGINCD = ? AND ");
		lsDel.append("ITMCD = ? AND ");
		lsDel.append("INVITMYR = ?");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liQuantity)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liOfcIssuanceNo)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liReprintDate)));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsOrigin)));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsItemCode)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liItemYear)));

			Log.write(
				Log.SQL,
				this,
				"updateReprintStickerQuantity - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(
				lsDel.toString(),
				lvValues);
			Log.write(
				Log.SQL,
				this,
				"updateReprintStickerQuantity - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updateReprintStickerQuantity - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updateReprintStickerQuantity - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
