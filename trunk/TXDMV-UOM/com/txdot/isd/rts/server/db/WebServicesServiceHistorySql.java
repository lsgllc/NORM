package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebServicesServiceHistorySql.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/10/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * K Harrell	01/19/2009	add purgeWebServicesServiceHistory() 
 * 							defect 9803 Ver Defect_POS_D
 * Ray Rowehl	01/27/2009	Add a method to determine the value of the 
 * 							Identity column for the row.
 * 							add qryWebServicesServiceHistoryIdentity()
 * 							modify insWebServicesServiceHistory()
 * 							defect 9804 Ver Defect_POS_D
 * Ray Rowehl	04/04/2011	Add a method to retreive the latest history
 * 							entry for a given CallerId and SAVId.
 * 							add qryMaxSelectedSavForCaller()
 * 							defect 10670 Ver 6.7.1
 * ---------------------------------------------------------------------
 */

/**
 * This class provides sql access to RTS_SRVC_HSTRY.
 *
 * @version	6.7.1			04/04/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		07/10/2008 13:07:27
 */

public class WebServicesServiceHistorySql
{
	DatabaseAccess caDA;

	/**
	 * WebServicesServiceHistorySql.java Constructor
	 * 
	 * <p>Sets the DatabaseAccess object.
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public WebServicesServiceHistorySql(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SRVC_HSTRY
	 * 
	 * @param  aaWebServicesServiceHistory	WebServiceHistoryData	
	 * @throws RTSException 
	 */
	public void insWebServicesServiceHistory(WebServiceHistoryData aaWebServicesServiceHistory)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insWebServicesServiceHistory - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT INTO RTS.RTS_SRVC_HSTRY ("
				+ "SavId, "
				+ "CallerId, "
				+ "SessionId, "
				+ "SuccessfulIndi, "
				+ "ErrMsgNo, "
				+ "ReqTimeStmp "
				+ ") VALUES ( "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "? "
				+ ")";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory.getSAVId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory.getCallerId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory.getSessionId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory
							.getSuccessfulIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory.getErrMsgNo())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory.getReqTimeStmp())));

			Log.write(
				Log.SQL,
				this,
				"insWebServicesServiceHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insWebServicesServiceHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insWebServicesServiceHistory - End");

			// defect 9804
			aaWebServicesServiceHistory.setSavReqId(
				qryWebServicesServiceHistoryIdentity());
			// end defect 9804
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insWebServicesServiceHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Delete from RTS.RTS_SRVC_HSTRY for Purge
	 * Delete only for Purge
	 * 
	 * @param  aaPurgeRTSDate int	
	 * @throws RTSException
	 */
	public int purgeWebServicesServiceHistory(RTSDate aaPurgeDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeWebServicesServiceHistory- Begin");

		String lsDel =
			"DELETE FROM RTS.RTS_SRVC_HSTRY WHERE ReqTimestmp <= '"
				+ aaPurgeDate.getTimestamp()
				+ "'";
		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeWebServicesServiceHistory- SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

			Log.write(
				Log.SQL,
				this,
				"purgeWebServicesServiceHistory- SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeWebServicesServiceHistory- End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeWebServicesServiceHistory- Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	
	/**
	 * Get the latest SAV entry for a named CallerId.
	 * 
	 * <p>SAVId is optional.  Set to -1 to not use it in the query.
	 * 
	 * <p>ErrMsgNo is optional.  Set to -1 to not use it in the query.
	 * 
	 * @param asCallerId
	 * @param aiSavId
	 * @param aiErrMsgNo
	 * @return RTSDate
	 * @throws RTSException
	 */
	public RTSDate qryMaxSelectedSavForCaller(
		String asCallerId,
		int aiSavId,
		int aiErrMsgNo)
		throws RTSException
	{
		String lsMethodName = " - qryMaxSelectedSavForCaller -";
		Log.write(Log.METHOD, this, lsMethodName + " Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();

		RTSDate laRespTimeStmp = null;

		ResultSet lrsQry;

		lsQry.append(
			"SELECT MAX(RespTimeStmp) AS RespTimeStmp FROM RTS.RTS_SRVC_HSTRY");

		// Add the CallerId to the query
		lsQry.append(" WHERE CallerId = ? ");
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(asCallerId)));

		if (aiSavId > -1)
		{
			lsQry.append(" AND SavId = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSavId)));
		}

		if (aiErrMsgNo > -1)
		{
			lsQry.append(" AND ErrMsgNo = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiErrMsgNo)));
		}

		try
		{
			Log.write(Log.SQL, this, lsMethodName + " SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, lsMethodName + " SQL - End");

			while (lrsQry.next())
			{
				laRespTimeStmp =
					caDA.getRTSDateFromDB(lrsQry, "RespTimeStmp");
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, lsMethodName + " End ");
			return (laRespTimeStmp);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				lsMethodName
					+ " SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

	/**
	 * Get the Identity field out of the table.
	 * 
	 * @param aaWebServicesServiceHistory
	 * @throws RTSException
	 */
	public int qryWebServicesServiceHistoryIdentity()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"qryWebServicesServiceHistoryIdentity - Begin");

		int liIdentity = -1;

		ResultSet lrsQry;

		String lsQry =
			"Select IDENTITY_VAL_LOCAL() as SaveReqId from "
				+ "RTS.RTS_SRVC_HSTRY";
		try
		{
			Log.write(
				Log.SQL,
				this,
				"qryWebServicesServiceHistoryIdentity - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry, null);
			Log.write(
				Log.SQL,
				this,
				"qryWebServicesServiceHistoryIdentity - SQL - End");

			while (lrsQry.next())
			{
				liIdentity = caDA.getIntFromDB(lrsQry, "SaveReqid");
				break;
			}

			Log.write(
				Log.METHOD,
				this,
				"qryWebServicesServiceHistoryIdentity - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryWebServicesServiceHistoryIdentity - RTSException - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryWebServicesServiceHistoryIdentity - SQLException - "
					+ aeSQLEx.getLocalizedMessage());
		}

		return liIdentity;
	}


	/**
	 * Method to update RTS.RTS_SRVC_HSTRY
	 * 
	 * @param  aaWebServicesServiceHistory	WebServiceHistoryData	
	 * @throws RTSException 
	 */
	public void updWebServicesServiceHistory(WebServiceHistoryData aaWebServicesServiceHistory)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updWebServicesServiceHistory - Begin");

		Vector lvValues = new Vector();

		// defect 9804
		// Modify the sql to use the identityi field if it is set.
		String lsIns =
			"UPDATE RTS.RTS_SRVC_HSTRY SET "
				+ "SuccessfulIndi = ?, "
				+ "ErrMsgNo = ?, "
				+ "RespTimeStmp = ? "
				+ "WHERE ";
		// end defect 9804

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory
							.getSuccessfulIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory.getErrMsgNo())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaWebServicesServiceHistory
							.getRespTimeStmp())));
			// defect 9804
			if (aaWebServicesServiceHistory.getSavReqId() > 0)
			{
				lsIns = lsIns + "SavReqId = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaWebServicesServiceHistory
								.getSavReqId())));
			}
			else
			{
				lsIns = lsIns + "SavId = ? " + "AND ReqTimeStmp = ?";
				// end defect 9804
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaWebServicesServiceHistory.getSAVId())));
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaWebServicesServiceHistory
								.getReqTimeStmp())));
			}
			// end defect 9804

			Log.write(
				Log.SQL,
				this,
				"updWebServicesServiceHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updWebServicesServiceHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updWebServicesServiceHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updWebServicesServiceHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

}
