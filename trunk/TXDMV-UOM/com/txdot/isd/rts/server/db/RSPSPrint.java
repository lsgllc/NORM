package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ReprintData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * RSPSPrint.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * 							** Begin Comments from RemotePrint() 
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Modified variable names to make consistent 
 * 							with standards.
 * 							New Class. Version 5.2.0
 * K Harrell	08/30/2004	Throw exception on insRemotePrint() as
 *							calling routines need to interpret
 *							exception
 *							modify insRemotePrint()
 *							defect 7349  Ver 5.2.1
 * 							** End Comments from RemotePrint() 
 * K Harrell	10/10/2004	Renamed from RemotePrint to better
 * 							match target table: RTS_RSPS_PRNT
 *							renamed all classes, modified to include new
 *							column names RegPltNo, ReprntQty
 *							deleted qryAllRemotePrint()
 *							Cleaned up method parameter names, comments
 *							defects 7597, 7598  Ver 5.2.1
 * K Harrell	03/04/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	11/11/2005	Accept passed int vs. RSPSPrintData Object.
 * 							modify purgeRSPSPrint() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	04/10/2006	Do not throw exception on duplicate key
 * 							modify insRSPSPrint()
 * 							defect 8678 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeRSPSPrint()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	08/11/2009	Modifications for new DB2 Driver 
 * 							modify insRSPSPrint() 
 * 							defect 10164 Ver Defect_POS_E'            
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database
 * 
 * @version	Defect_POS_E'	08/11/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		10/10/2004 
 */
public class RSPSPrint implements java.io.Serializable
{
	DatabaseAccess caDA;

	/**
	 * RSPSPrint constructor comment.
	 */
	public RSPSPrint()
	{
		super();
	}
	/**
	 * RSPSPrint constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess 
	 * @throws RTSException
	 */
	public RSPSPrint(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Insert into RTS.RTS_RSPS_PRNT during Subcontractor Renewal 
	 * or DTA RSPS Diskette processing
	 *
	 * @param ReprintData aaReprintData
	 * @throws RTSException
	 */
	public void insRSPSPrint(ReprintData aaReprintData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insRSPSPrint - Begin");
		// SQL Insert Statement 
		StringBuffer lsIns = new StringBuffer();
		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// defects 7597, 7598
		// Add reference to ReprntQty(7597), RegPltNo (7598)
		lsIns.append("INSERT INTO RTS.RTS_RSPS_PRNT (");
		lsIns.append("OFCISSUANCENO, ");
		lsIns.append("SUBSTAID, ");
		lsIns.append("ORIGINCD, ");
		lsIns.append("ORIGINID, ");
		lsIns.append("REGPLTNO, ");
		lsIns.append("VIN, ");
		lsIns.append("PRNTINVQTY, ");
		lsIns.append("REPRNTQTY, ");
		lsIns.append("VOIDPRNTQTY, ");
		lsIns.append("DISKSEQNO, ");
		lsIns.append("RSPSID, ");
		lsIns.append("PROCSDATE, ");
		lsIns.append("ITMCD, ");
		lsIns.append("INVITMYR) ");
		lsIns.append(" VALUES (");
		lsIns.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaReprintData.getOrigin())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getOriginId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaReprintData.getRegPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaReprintData.getVIN())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getPrntQty())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getReprntQty())));
			// end defects 7597, 7598 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getVoided())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getDiskNum())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaReprintData.getScannerId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getReprntDate().getAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaReprintData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintData.getItmYr())));
			Log.write(Log.SQL, this, "insRSPSPrint - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(
				lsIns.toString(),
				lvValues);
			Log.write(Log.SQL, this, "insRSPSPrint - SQL - End");
			Log.write(Log.METHOD, this, "insRSPSPrint - End");
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10164 
			// defect 8678
			// Do not throw exception as counties are repeatedly 
			// inserting diskettes; Client code ignores.
			//if (aeRTSEx.getDetailMsg().indexOf("SQL0803") == -1)
			if (aeRTSEx
				.getDetailMsg()
				.indexOf(CommonConstant.DUPLICATE_KEY_EXCEPTION)
				== -1)
			{
				// end defect 10164 
				Log.write(
					Log.SQL_EXCP,
					this,
					"insRSPSPrint - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
			// end defect 8678 
		}

	}
	/**
	 * Purge from RSPS_Prnt in Batch
	 * 
	 * @param   aiPurgeAMDate int
	 * @return  int
	 * @throws 	RTSException 
	 */
	public int purgeRSPSPrint(int aiPurgeAMDate) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeRSPSPrint - Begin");

		// SQL Delete Statement 
		String lsDel =
			"DELETE FROM RTS.RTS_RSPS_PRNT WHERE PROCSDATE <= ?";

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		try
		{
			// defect 8423
			// use passed int
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423

			Log.write(Log.SQL, this, "purgeRSPSPrint - SQL - Begin");

			// defect 9825 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeRSPSPrint - SQL - End");
			Log.write(Log.METHOD, this, "purgeRSPSPrint - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeRSPSPrint - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	/**
	 * Select from RTS.RTS_RSPS_PRNT for the Inventory Action Report, 
	 * Part K.
	 *
	 * @param aaReprintData ReprintData
	 * @param aaIARDate  RTSDate
	 * @return Vector
	 * @throws RTSException  
	 */
	public Vector qryRSPSPrint(
		ReprintData aaReprintData,
		RTSDate aaIARDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "qryRSPSPrint - Begin");

		// SQL Select Statement 
		StringBuffer lsQry = new StringBuffer();

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// Vector of Returned data
		Vector lvRslt = new Vector();

		// Object for maintaining cursor positioning in returned data
		ResultSet lrsQry = null;

		// defects 7597, 7598
		// Added RePrntQty (7597), RegPltNo(7598)
		// Select those with only 1 print for summarizing on IAR Report
		lsQry.append("SELECT ");
		//lsQry.append("ORIGINCD, ");
		//lsQry.append("ORIGINID, ");
		lsQry.append("RSPSID, ");
		lsQry.append("ITMCD, ");
		lsQry.append("INVITMYR, ");
		lsQry.append("-1 AS DISKSEQNO, ");
		lsQry.append("' ' AS VIN, ");
		lsQry.append("' ' AS REGPLTNO, ");
		lsQry.append("SUM(PRNTINVQTY) AS PRNTINVQTY, ");
		lsQry.append("0 as REPRNTQTY, ");
		lsQry.append("0 as VOIDPRNTQTY, ");
		lsQry.append("'1' AS SORTHELPER ");
		lsQry.append("FROM RTS.RTS_RSPS_PRNT WHERE ");
		lsQry.append("OFCISSUANCENO = ? ");
		lsQry.append("AND SUBSTAID = ? ");
		lsQry.append("AND PROCSDATE = ? ");
		lsQry.append("AND PRNTINVQTY = 1 ");
		lsQry.append("AND VOIDPRNTQTY = 0 ");
		lsQry.append("AND REPRNTQTY = 0 ");
		lsQry.append("GROUP BY RSPSID,ITMCD,INVITMYR ");
		//"GROUP BY ORIGINCD, ORIGINID,RSPSID,ITMCD,INVITMYR,DISKSEQNO ");

		// Select those with either Print>1, Reprint, Void
		//  to be reported by VIN, RegPltNo on IAR Report 
		lsQry.append("UNION ");
		lsQry.append("SELECT ");
		//lsQry.append("ORIGINCD, ");
		//lsQry.append("ORIGINID, ");
		lsQry.append("RSPSID, ");
		lsQry.append("ITMCD, ");
		lsQry.append("INVITMYR, ");
		lsQry.append("DISKSEQNO, ");
		lsQry.append("VIN, ");
		lsQry.append("REGPLTNO, ");
		lsQry.append("PRNTINVQTY, ");
		lsQry.append("REPRNTQTY, ");
		lsQry.append("VOIDPRNTQTY, ");
		lsQry.append("'2' AS SORTHELPER ");
		lsQry.append("FROM RTS.RTS_RSPS_PRNT WHERE ");
		lsQry.append("OFCISSUANCENO = ? ");
		lsQry.append("AND SUBSTAID = ? ");
		lsQry.append("AND PROCSDATE = ? ");
		lsQry.append(
			"AND (PRNTINVQTY > 1 or VOIDPRNTQTY >0 OR REPRNTQTY >0) ");

		lsQry.append(
			"ORDER BY RSPSID,SORTHELPER,DISKSEQNO,ITMCD,INVITMYR,"
				+ "REGPLTNO,VIN ");
		//"ORDER BY RSPSID,SORTHELPER,ITMCD,INVITMYR,DISKSEQNO,REGPLTNO,VIN ");
		//"ORDER BY ORIGINCD, ORIGINID,RSPSID,SORTHELPER,DISKSEQNO,ITMCD,INVITMYR,REGPLTNO,VIN ");
		// end defect 7597, 7598
		try
		{
			for (int i = 0; i < 2; i++)
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaReprintData.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaReprintData.getSubstaId())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaIARDate.getAMDate())));
			}
			Log.write(Log.SQL, this, "qryRSPSPrint - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, "qryRSPSPrint - SQL - End");
			while (lrsQry.next())
			{
				ReprintData qryData = new ReprintData();
				//qryData.setOrigin(cDA.getStringFromDB(lrsQry, "ORIGINCD"));
				//qryData.setOriginId(cDA.getIntFromDB(lrsQry, "ORIGINID"));
				// Add RSPSID 
				qryData.setScannerId(
					caDA.getStringFromDB(lrsQry, "RSPSID"));
				qryData.setItmCd(caDA.getStringFromDB(lrsQry, "ITMCD"));
				qryData.setItmYr(caDA.getIntFromDB(lrsQry, "INVITMYR"));
				qryData.setDiskNum(
					caDA.getIntFromDB(lrsQry, "DISKSEQNO"));
				qryData.setVIN(caDA.getStringFromDB(lrsQry, "VIN"));
				qryData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "REGPLTNO"));
				qryData.setPrntQty(
					caDA.getIntFromDB(lrsQry, "PRNTINVQTY"));
				qryData.setReprntQty(
					caDA.getIntFromDB(lrsQry, "REPRNTQTY"));
				qryData.setVoided(
					caDA.getIntFromDB(lrsQry, "VOIDPRNTQTY"));
				lvRslt.add(qryData);
			}
			Log.write(Log.METHOD, this, "qryRSPSPrint - End ");
			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryRSPSPrint - Exception " + aeSQLEx.getMessage());
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
				catch (Exception e)
				{
					//eat up the exception here.
				}
			}
			caDA.closeLastDBStatement();
			lrsQry = null;
		}
	}
}
