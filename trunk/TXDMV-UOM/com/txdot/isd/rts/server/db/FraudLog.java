package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.FraudLogData;
import com.txdot.isd.rts.services.data.FraudStateData;
import com.txdot.isd.rts.services.data.FraudUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * FraudLog.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/29/2011	Created
 * 							defect 10865 Ver 6.8.0
 * K Harrell	06/03/2011	Update for Report/Export 
 * 							add COLUMN_NAME_ARRAY, getColumnHdrs(), 
 * 							 getStringToAppend(), qryExportFraudLog(), 
 * 							 qryReportFraudLog() 
 * 							defect 10900 Ver 6.8.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_FRAUD_LOG 
 *
 * @version	6.8.0			06/03/2011
 * @author	Kathy Harrell	
 * <br>Creation Date:		05/29/2011 20:51:17
 */
public class FraudLog
{
	DatabaseAccess caDA;
	String csMethod = new String();

	private final static String[] COLUMN_NAME_ARRAY =
		{
			"FraudIdntyNo",
			"TransDate",
			"OfcIssuanceNo",
			"SubstaId",
			"TransWsId",
			"TransAMDate",
			"TransTime",
			"TransEmpId",
			"DocNo",
			"VIN",
			"RegPltNo",
			"Action",
			"FraudDesc" };

	/**
	 * FraudLog constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public FraudLog(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to return String Buffer 
	 *    - preface with ","
	 *    - add quotes around String
	 *
	 * @throws RTSException 
	 */
	private String getStringToAppend(String asData)
	{
		String lsData =
			CommonConstant.STR_COMMA
				+ CommonConstant.STR_DOUBLE_QUOTE
				+ asData
				+ CommonConstant.STR_DOUBLE_QUOTE;
		return lsData;
	}

	/**
	 * Method to build initial delimited string of column headers
	 *
	 * @throws RTSException 
	 */
	public StringBuffer getColumnHdrs()
	{
		StringBuffer lsDelimitedHdrs = new StringBuffer();
		lsDelimitedHdrs.append(
			CommonConstant.STR_DOUBLE_QUOTE
				+ "FraudIdntyNo"
				+ CommonConstant.STR_DOUBLE_QUOTE);

		for (int i = 1; i < COLUMN_NAME_ARRAY.length; i++)
		{
			lsDelimitedHdrs.append(
				getStringToAppend(COLUMN_NAME_ARRAY[i]));
		}
		return lsDelimitedHdrs;
	}

	/**
	 * Method to Insert into RTS.RTS_FRAUD_LOG
	 * 
	 * @param  aaFraudLogData  	
	 * @throws RTSException 	
	*/
	public void insFraudLog(FraudLogData aaFraudLogData)
		throws RTSException
	{
		csMethod = "insFraudLog";

		Log.write(Log.METHOD, this, csMethod + " - begin");

		Vector lvValues = new Vector();

		if (aaFraudLogData.getTransEmpId() == null
			|| aaFraudLogData.getTransEmpId().trim().length() == 0)
		{
			aaFraudLogData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}

		String lsIns =
			"INSERT into RTS.RTS_FRAUD_LOG  ("
				+ "OfcissuanceNo,"
				+ "SubstaId,"
				+ "TransWsId,"
				+ "TransAMDate,"
				+ "TransTime,"
				+ "TransEmpId,"
				+ "DocNo,"
				+ "VIN,"
				+ "RegPltNo,"
				+ "AddFraudIndi,"
				+ "FraudCd,"
				+ "TransTimestmp)"
				+ " VALUES ( ? ";

		String lsAdd = new String();

		for (int i = 0; i < 10; i++)
		{
			lsAdd = lsAdd + " ,? ";
		}
		lsIns = lsIns + lsAdd + ", Current Timestamp)";

		try
		{
			// OfcIssuanceNo 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFraudLogData.getOfcIssuanceNo())));

			// SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFraudLogData.getSubstaId())));

			// TransWsId 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFraudLogData.getTransWsId())));

			// TransAMDate 			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFraudLogData.getTransAMDate())));

			// TransTime 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaFraudLogData.getTransTime())));

			// TransEmpId
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaFraudLogData.getTransEmpId())));

			// DocNo
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaFraudLogData.getDocNo())));

			// VIN
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaFraudLogData.getVIN())));

			// RegPltNo
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaFraudLogData.getRegPltNo())));

			// AddFraudIndi
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaFraudLogData.getAddFraudIndi())));

			// FraudCd
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaFraudLogData.getFraudCd())));

			Log.write(Log.SQL, this, csMethod + " - SQL - begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - end");
			Log.write(Log.METHOD, this, csMethod + " - end");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method description
	 * 
	 * @param asDocNo
	 * @return Hashtable 
	 * @throws RTSException
	 */
	public FraudStateData qryFraudCdForDocNo(String asDocNo)
		throws RTSException
	{
		csMethod = "getFraudCdForDocNo";
		FraudStateData laFraudStateData;
		boolean lbIdentification = false;
		boolean lbLetterOfAuthorization = false;
		boolean lbPowerOfAttorney = false;
		boolean lbReleaseOfLien = false;

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "FraudCd from RTS.RTS_FRAUD_LOG a "
				+ "Where a.DocNo = ? and "
				+ "AddFraudIndi = 1 and "
				+ "Not Exists "
				+ "(select * from RTS.RTS_FRAUD_LOG b "
				+ "where a.docno = b.docno and a.fraudcd = b.fraudcd "
				+ "and b.Fraudidntyno > a.Fraudidntyno and b.Addfraudindi = 0)");

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(asDocNo)));

		lsQry.append(" order by 1");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				int liFraudCd = caDA.getIntFromDB(lrsQry, "FraudCd");
				if (liFraudCd == FraudStateData.IDENTIFICATION)
				{
					lbIdentification = true;
				}
				else if (
					liFraudCd
						== FraudStateData.LETTER_OF_AUTHORIZATION)
				{
					lbLetterOfAuthorization = true;
				}
				else if (liFraudCd == FraudStateData.POWER_OF_ATTORNEY)
				{
					lbPowerOfAttorney = true;
				}
				else if (liFraudCd == FraudStateData.RELEASE_OF_LIEN)
				{
					lbReleaseOfLien = true;
				}
			} //End of While

			laFraudStateData =
				new FraudStateData(
					lbIdentification,
					lbReleaseOfLien,
					lbPowerOfAttorney,
					lbLetterOfAuthorization);

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return laFraudStateData;
	}

	/**
	 * Method to Query RTS.RTS_FRAUD_LOG
	 * 
	 * @param  aaFraudUIData  	
	 * @throws RTSException 	
	*/
	public String qryExportFraudLog(FraudUIData aaFraudUIData)
		throws RTSException
	{
		csMethod = "qryExportFraudLog";

		Log.write(Log.METHOD, this, csMethod + " - begin");

		ResultSet lrsQry;

		String lsQry =
			"Select "
				+ "FraudIdntyNo,"
				+ "OfcissuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "TransTime,"
				+ "TransEmpId,"
				+ "DocNo,"
				+ "VIN,"
				+ "RegPltNo,"
				+ "AddFraudIndi,"
				+ "FraudDesc "
				+ " FROM RTS.RTS_FRAUD_LOG A, "
				+ " RTS.RTS_FRAUD B"
				+ " WHERE "
				+ " A.FRAUDCD = B.FRAUDCD AND "
				+ aaFraudUIData.getWhereClauseData()
				+ " order by 1"; 

		try
		{
			System.out.println(lsQry);
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			StringBuffer lsExportData = new StringBuffer();

			/******************************************************
			 *         WARNING, WARNING, WARNING
			 ******************************************************
			 *   THE ORDER OF THESE COLUMNS MUST MATCH THE ORDER OF 
			 *   FIELDS IN COLUMN_NAME_ARRAY !!!!
			 *******************************************************/
			boolean lbFirst = true;

			while (lrsQry.next())
			{
				if (lbFirst)
				{
					lsExportData = getColumnHdrs();
					lbFirst = false;
				}
				// New Line 
				lsExportData.append(
					CommonConstant.SYSTEM_LINE_SEPARATOR);

				lsExportData.append(
					+caDA.getIntFromDB(lrsQry, "FraudIdntyNo"));

				int liTransAMDate =
					caDA.getIntFromDB(lrsQry, "TransAMDate");

				RTSDate laCalcDate =
					new RTSDate(RTSDate.AMDATE, liTransAMDate);

				lsExportData.append(
					CommonConstant.STR_COMMA
						+ CommonConstant.STR_DOUBLE_QUOTE
						+ laCalcDate.toString()
						+ CommonConstant.STR_DOUBLE_QUOTE);

				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "SubStaId"));

				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "TransWsId"));

				lsExportData.append(
					CommonConstant.STR_COMMA + liTransAMDate);

				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "TransTime"));

				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "TransEmpId")));
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "DocNo")));
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "VIN")));
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "RegPltNo")));
				int liAddIndi =
					caDA.getIntFromDB(lrsQry, "AddFraudIndi");
				lsExportData.append(
					getStringToAppend(
						liAddIndi == 1 ? "Add" : "Delete"));

				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "FraudDesc")));
			}
			Log.write(Log.METHOD, this, csMethod + " - end");
			return lsExportData.toString();
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}

	}
	/**
	 * Method to Query RTS.RTS_FRAUD_LOG for Export
	 * 
	 * @param  aaFraudUIData  	
	 * @throws RTSException 	
	*/
	public Vector qryReportFraudLog(FraudUIData aaFraudUIData)
		throws RTSException
	{
		csMethod = "qryReportFraudLog";

		Log.write(Log.METHOD, this, csMethod + " - begin");

		ResultSet lrsQry;
		Vector lvRslt = new Vector();

		String lsQry =
			"Select "
				+ "FraudIdntyNo,"
				+ "OfcissuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "TransTime,"
				+ "TransEmpId,"
				+ "DocNo,"
				+ "VIN,"
				+ "RegPltNo,"
				+ "AddFraudIndi,"
				+ "FraudDesc "
				+ " FROM RTS.RTS_FRAUD_LOG A, "
				+ " RTS.RTS_FRAUD B"
				+ " WHERE "
				+ " A.FRAUDCD = B.FRAUDCD AND "
				+ aaFraudUIData.getWhereClauseData()
				+ " order by 1"; 

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				FraudLogData laFraudLogData = new FraudLogData();
				laFraudLogData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laFraudLogData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubStaId"));
				laFraudLogData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laFraudLogData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laFraudLogData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laFraudLogData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laFraudLogData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laFraudLogData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laFraudLogData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laFraudLogData.setAddFraudIndi(
					caDA.getIntFromDB(lrsQry, "AddFraudIndi"));
				laFraudLogData.setFraudDesc(
					caDA.getStringFromDB(lrsQry, "FraudDesc"));
				lvRslt.add(laFraudLogData);
			}
			Log.write(Log.METHOD, this, csMethod + " - end");
			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
