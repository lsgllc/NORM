package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RenewalEMailHstryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RenewalEMailHstry
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/12/2010	Created
 * 							defect 10514 Ver 6.5.0 
 * B Brown		07/26/2010	For EReminder project to return int
 * 							for getting the CustIdntyNo 
 * 							modify insRenewalEMailHstry() 
 * 							defect 10512 Ver 6.5.0
 * ---------------------------------------------------------------------
 */

/**
 * Methods to access RTS_RENWL_EMAIL_HSTRY
 *
 * @version	6.5.0			07/26/2010	
 * @author	Kathy Harrell
 * <br>Creation Date:		07/12/2010  16:59:17 
 */
public class RenewalEMailHstry
{
	private String csMethod;
	private DatabaseAccess caDA;

	/**
	 * RenewalEMailHstry.java Constructor
	 * 
	 */
	public RenewalEMailHstry(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Initial Insert 
	 *
	 * @throws RTSException 
	 */
	public int insRenewalEMailHstry(RTSDate aaBatchDate)
		throws RTSException
	{
		csMethod = "insRenewalEMailHstry";

		Log.write(Log.METHOD, this, csMethod + "- Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_RENWL_EMAIL_HSTRY ("
				+ " BatchDate,"
				+ " ProcsInitTimestmp)"
				+ " values ( "
				+ "?,"
				+ " Current Timestamp)";
		
//		System.out.println ("aaBatchDate.toString() = " + aaBatchDate.toString());		

		try
		{
			lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaBatchDate.toString())));

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			// defect 10512
			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as CustIdntyNo  from "
					+ " RTS.RTS_RENWL_EMAIL_HSTRY";

			ResultSet lrsQry = caDA.executeDBQuery(lsSel, null);

			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo = caDA.getIntFromDB(lrsQry, "CustIdntyNo");
				break;
			} //End of While

			Log.write(
				Log.SQL,
				this,
				"insRenwlEmailHstry - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insRenwlEmailHstry - End");

			return liIdntyNo;
			// end defect 10512
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				// defect 10512
				" - insInternetDepositReconHstry - SQL Exception "
					+ aeSQLEx.getMessage());
				// end defect 10512	
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInternetDepositReconHstry - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Return boolean to denote whether row exists in 
	 * RTS_RENWL_EMAIL_HSTRY for given BatchDate where 
	 * ProcsComplTimestmp is populated  
	 * 
	 * @return boolean 
	 * @throws RTSException 
	 */
	public boolean qryBatchDate(String asBatchDate) throws RTSException
	{
		csMethod = "qryBatchDate";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		lsQry.append(
			"SELECT  1 as NumRows  "
				+ " from RTS.RTS_RENWL_EMAIL_HSTRY "
				+ " where BatchDate = ? "
				+ " and ProcsComplTimestmp is not null ");

		lvValues.addElement(new DBValue(Types.CHAR, asBatchDate));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.DEBUG, this, csMethod + " - SQL - End");
			Log.write(Log.SQL, this, csMethod = " - SQL - End");

			int liNumRows = 0;
			while (lrsQry.next())
			{
				liNumRows = caDA.getIntFromDB(lrsQry, "NumRows");
				break;
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return liNumRows != 0;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}

	/**
	 * Get the RenwlEMailReqIds to update, that matches the
	 * tmp records we are processing
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryMaxBatchDates() throws RTSException
	{
		csMethod = "qryMaxBatchDates";

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry.append(
			"select max(RenwlEMailReqId) as RenwlEMailReqId, "
				+ "a.BatchDate  from "
				+ "RTS.RTS_RENWL_EMAIL_HSTRY A, "
				+ "RTS.RTS_RENWL_EMAIL_TMP B where "
				+ "PROCSCOMPLTIMESTMP is null and a.BATCHDATE "
				+ "is not null and RENWLCOUNT > 0 and "
				+ "a.BATCHDATE = b.BATCHDATE "
				+ "group by a.BATCHDATE");
		try
		{
			Log.write(Log.METHOD, this, csMethod + " - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(Log.DEBUG, this, csMethod + " - SQL - End");
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			RenewalEMailHstryData laData = null;
			Vector lvReturnData = new Vector();
			while (lrsQry.next())
			{
				laData = new RenewalEMailHstryData();

				laData.setRenwlEMailReqId(
					caDA.getIntFromDB(lrsQry, "RenwlEMailReqId"));

				laData.setBatchDate(
					caDA.getRTSDateFromDB(lrsQry, "BatchDate"));

				lvReturnData.add(laData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return lvReturnData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}

	/**
	 * Select row for max(RenwlEMailReqId) to determine if successful.
	 * 
	 * Used in Evening Batch process to move data from temp to prod table. 
	 *
	 * @return RenewalEMailHstryData
	 * @throws RTSException 
	 */
	public RenewalEMailHstryData qryMaxRenewalEMailHstry()
		throws RTSException
	{
		csMethod = "qryMaxRenewalEMailHstry";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RenwlEMailReqId,"
				+ "BatchDate,"
				+ "ProcsInitTimestmp,"
				+ "ProcsComplTimestmp,"
				+ "TmpInsrtTimestmp,"
				+ "RenwlCount,"
				+ "SuccessfulIndi,"
				+ "ErrMsgNo "
				+ "FROM RTS.RTS_RENWL_EMAIL_HSTRY "
				+ "where TmpInsrtTimestmp is not null and "
				+ "ProcsComplTimestmp is null and "
				+ "RenwlEMailReqId = (Select "
				+ "max(RenwlEMailReqId) from "
				+ "RTS.RTS_RENWL_EMAIL_HSTRY)");
		try
		{

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(Log.DEBUG, this, csMethod + " - SQL - End");
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			RenewalEMailHstryData laData = null;

			while (lrsQry.next())
			{
				laData = new RenewalEMailHstryData();

				laData.setRenwlEMailReqId(
					caDA.getIntFromDB(lrsQry, "RenwlEMailReqId"));

				laData.setBatchDate(
					caDA.getRTSDateFromDB(lrsQry, "BatchDate"));

				laData.setProcsInitTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ProcsInitTimestmp"));

				laData.setProcsComplTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"ProcsComplTimestmp"));

				laData.setTmpInsrtTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "TmpInsrtTimestmp"));

				laData.setRenwlCount(
					caDA.getIntFromDB(lrsQry, "RenwlCount"));

				laData.setSuccessfulIndi(
					caDA.getIntFromDB(lrsQry, "SuccessfulIndi"));

				laData.setErrMsgNo(
					caDA.getIntFromDB(lrsQry, "ErrMsgNo"));
				break;
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (laData);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);

		}
	}
	/**
	 * Get RenewalEMailHstryData based on RenwlEMailReqId 
	 *
	 * @param aiReqId 
	 * @return RenewalEMailHstryData
	 * @throws RTSException 
	 */
	public RenewalEMailHstryData qryMaxRenewalEMailHstryReqId(int aiReqId)
		throws RTSException
	{
		csMethod = "qryMaxRenewalEMailHstryReqId";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RenwlEMailReqId,"
				+ "BatchDate,"
				+ "ProcsInitTimestmp,"
				+ "ProcsComplTimestmp,"
				+ "TmpInsrtTimestmp,"
				+ "RenwlCount,"
				+ "SuccessfulIndi,"
				+ "ErrMsgNo "
				+ "FROM RTS.RTS_RENWL_EMAIL_HSTRY "
				+ "where "
				+ "RenwlEMailReqId = "
				+ aiReqId);
		try
		{

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(Log.DEBUG, this, csMethod + " - SQL - End");
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			RenewalEMailHstryData laData = null;

			while (lrsQry.next())
			{
				laData = new RenewalEMailHstryData();

				laData.setRenwlEMailReqId(
					caDA.getIntFromDB(lrsQry, "RenwlEMailReqId"));

				laData.setBatchDate(
					caDA.getRTSDateFromDB(lrsQry, "BatchDate"));

				laData.setProcsInitTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ProcsInitTimestmp"));

				laData.setProcsComplTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"ProcsComplTimestmp"));

				laData.setTmpInsrtTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "TmpInsrtTimestmp"));

				laData.setRenwlCount(
					caDA.getIntFromDB(lrsQry, "RenwlCount"));

				laData.setSuccessfulIndi(
					caDA.getIntFromDB(lrsQry, "SuccessfulIndi"));

				laData.setErrMsgNo(
					caDA.getIntFromDB(lrsQry, "ErrMsgNo"));
				break;
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (laData);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}

	/**
	 * Purge from RTS_RENWL_EMAIL_HSTRY 
	 *  
	 * @param aaPurgeDate
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeRenewalEMailHstry(RTSDate aaPurgeDate)
		throws RTSException
	{
		csMethod = "purgeRenewalEMailHstry";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsDel =
			"DELETE FROM RTS.RTS_RENWL_EMAIL_HSTRY WHERE "
				+ "BATCHDATE <= '"
				+ aaPurgeDate.toString()
				+ "'";

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");

			return liNumRows;
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
	 * Method to update RTS.RTS_RENWL_EMAIL_HSTRY
	 *
	 * @param  aaData 
	 * @return int 
	 * @throws RTSException
	 */
	public int updRenewalEMailHstry(RenewalEMailHstryData aaData)
		throws RTSException, Exception
	{
		csMethod = "updRenewalEMailHstry";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsUpd = "UPDATE RTS.RTS_RENWL_EMAIL_HSTRY SET ";

		if (aaData.getBatchDate() != null)
		{
			lsUpd = lsUpd + " BatchDate  = ? ,";
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getBatchDate().toString())));

		}
		if (aaData.getProcsInitTimestmp() != null)
		{
			lsUpd = lsUpd + " ProcsInitTimestmp = ? ,";
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaData.getProcsInitTimestmp())));

		}
		if (aaData.getTmpInsrtTimestmp() != null)
		{
			lsUpd = lsUpd + " TmpInsrtTimestmp = Current Timestamp ,";
		}

		if (aaData.getProcsComplTimestmp() != null)
		{
			lsUpd = lsUpd + " ProcsComplTimestmp = Current Timestamp ,";
		}

		if (aaData.getSuccessfulIndi() != Integer.MIN_VALUE)
		{
			lsUpd = lsUpd + " SuccessFulIndi = ?,";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSuccessfulIndi())));
		}

		if (aaData.getRenwlCount() != Integer.MIN_VALUE)
		{
			lsUpd = lsUpd + " RenwlCount = ?,";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRenwlCount())));
		}

		if (aaData.getErrMsgNo() != Integer.MIN_VALUE)
		{
			lsUpd = lsUpd + " ErrMsgNo = ?,";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getErrMsgNo())));

		}
		// Remove last comma 
		lsUpd = lsUpd.substring(0, lsUpd.length() - 1);

		lsUpd = lsUpd + " Where RenwlEMailReqId = ? ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getRenwlEMailReqId())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				"updRenewalEMailHstry - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(
				Log.SQL,
				this,
				"updRenewalEMailHstry - SQL - End");
			Log.write(Log.METHOD, this, "updRenewalEMailHstry - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updRenewalEMailHstry - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (Exception leEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updRenewalEMailHstry - Exception - "
					+ leEx.getMessage());
			throw leEx;
		}
	} //END OF Update METHOD
}
