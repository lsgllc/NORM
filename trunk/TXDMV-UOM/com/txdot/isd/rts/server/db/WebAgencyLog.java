package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.WebAgencyLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyLog.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNCY_LOG 
 *
 * @version	6.7.0			12/28/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyLog
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyLog Constructor
	 * 
	 */
	public WebAgencyLog(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY_LOG 
	 *
	 * @param  aaData	WebAgencyLogData	
	 * @throws RTSException 
	 */
	public WebAgencyLogData insWebAgencyLog(WebAgencyLogData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyLog";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_LOG("
				+ "AGNCYTYPECD,"
				+ "NAME1,"
				+ "NAME2,"
				+ "ST1,"
				+ "ST2,"
				+ "CITY,"
				+ "STATE,"
				+ "ZPCD,"
				+ "ZPCDP4,"
				+ "PHONE,"
				+ "EMAIL,"
				+ "CNTCTNAME,"
				+ "INITOFCNO,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP, "
				+ "AGNCYIDNTYNO,"
				+ "UPDTNGAGNTIDNTYNO )"
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getAgncyTypeCd())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getName1())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getName2())));

			AddressData laAddrData = aaData.getAddressData();

			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt1())));

			// 5
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt2())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getCity())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getState())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcd())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcdp4())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getPhone())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getEMail())));
			// 12
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getCntctName())));
			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getInitOfcNo())));
			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			// 15
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getChngTimestmp())));
			// 16
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));
			// 17
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
			return aaData;
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
	 * Purge RTS_WEB_AGNCY_LOG
	 * 
	 * @param aiPurgeDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeWebAgencyLog(int aiPurgeDays) throws RTSException
	{
		csMethod = "purgeWegAgencyLog";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelData =
			"DELETE FROM RTS.RTS_WEB_AGNCY_LOG A "
				+ " WHERE "
				+ " CHNGTIMESTMP "
				+ " < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();

		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeDays)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

}
