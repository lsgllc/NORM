package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InternetDepositReconHstryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetDepositReconHstry.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/12/2009	Created
 * 							defect 9935 Ver Defect_POS_D 
 * B Brown		02/25/2009	Changed from FTPinittimestmp to 
 * 							ProcsInitTimestmp
 * 							defect 9936 Ver Defect_POS_D
 * K Harrell	04/08/2009	add qryBatchDepositReconRptDateRange()
 * 							defect 10027 Ver Defect_POS_E
 * K Harrell	06/09/2009	add purgeInternetDepositReconHstry()
 * 							defect 9955 Ver Defect_POS_F
 * K Harrell	08/29/2009	add qryBatchDepositDate()
 * 							defect 10011 Ver Defect_POS_F
 * B Brown		10/27/2009	add qryInternetDepositReconHstryReqID(),
 * 								qryMaxDepositDates()
 * 							defect 10111 Ver Defect_POS_G
 * B Brown		12/10/2009	use current timestmp when updating rts_itrnt
 * 							_deposit_recon_hstry
 * 							modify updInternetDepositReconHstry() 
 * 							defect 10262 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ITRNT_DEPOSIT_RECON_HSTRY 
 *
 * @version	Defect_POS_H 	12/10/2009 
 * @author	Kathy Harrell
 * <br>Creation Date:		02/12/2009
 */
public class InternetDepositReconHstry
	extends InternetDepositReconHstryData
{
	protected DatabaseAccess caDA;

	/**
	 * InternetDepositReconHstry constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public InternetDepositReconHstry(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Initial Insert 
	 *
	 * @return int
	 * @throws RTSException 
	 */
	public int insInternetDepositReconHstry() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insInternetDepositReconHstry - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY( "
				+ "ProcsInitTimestmp) values (Current Timestamp)";

		try
		{
			Log.write(
				Log.SQL,
				this,
				"insInternetDepositReconHstry - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as CustIdntyNo  from "
					+ " RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY";

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
				"insInternetDepositReconHstry - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insInternetDepositReconHstry - End");

			return liIdntyNo;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - insInternetDepositReconHstry - SQL Exception "
					+ aeSQLEx.getMessage());
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
	 * Purge from RTS_ITRNT_DEPOSIT_RECON_HSTRY
	 *  
	 * @param aaPurgeDate
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeInternetDepositReconHstry(RTSDate aaPurgeDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeInternetDepositReconHstry- Begin");

		String lsDel =
			"DELETE FROM RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY WHERE "
				+ "BANKDEPOSITDATE <= '"
				+ aaPurgeDate.toString()
				+ "'";

		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeInternetDepositReconHstry- SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

			Log.write(
				Log.SQL,
				this,
				"purgeInternetDepositReconHstry- SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeInternetDepositReconHstry- End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInternetDepositReconHstry- Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Select row for max(DepositReconReqId) to determine if successful.
	 * Used in Evening Batch process to move data from temp to prod table. 
	 *
	 * @return InternetDepositReconHstryData
	 * @throws RTSException 
	 */
	public InternetDepositReconHstryData qryMaxInternetDepositReconHstry()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryMaxInternetDepositReconHstry - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "DepositReconHstryReqId,"
				+ "BankDepositDate,"
				+ "ProcsInitTimestmp,"
				+ "ProcsComplTimestmp,"
				+ "TmpInsrtTimestmp,"
				+ "TransCount,"
				+ "SuccessfulIndi,"
				+ "ErrMsgNo "
				+ "FROM RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY "
				+ "where TmpInsrtTimestmp is not null and "
				+ "ProcsComplTimestmp is null and "
				+ "DepositReconHstryReqId = (Select "
				+ "max(DepositReconHstryReqId) from "
				+ "RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY)");
		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryMaxInternetDepositReconHstry - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(
				Log.DEBUG,
				this,
				" - qryMaxInternetDepositReconHstry - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryMaxInternetDepositReconHstry - SQL - End");

			InternetDepositReconHstryData laData = null;

			while (lrsQry.next())
			{
				laData = new InternetDepositReconHstryData();

				laData.setDepositReconHstryReqId(
					caDA.getIntFromDB(
						lrsQry,
						"DepositReconHstryReqId"));

				laData.setBankDepositDate(
					caDA.getRTSDateFromDB(lrsQry, "BankDepositDate"));

				laData.setProcsInitTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ProcsInitTimestmp"));

				laData.setProcsComplTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"ProcsComplTimestmp"));

				laData.setTmpInsrtTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "TmpInsrtTimestmp"));

				laData.setTransCount(
					caDA.getIntFromDB(lrsQry, "TransCount"));

				laData.setSuccessfulIndi(
					caDA.getIntFromDB(lrsQry, "SuccessfulIndi"));

				laData.setErrMsgNo(
					caDA.getIntFromDB(lrsQry, "ErrMsgNo"));
				break;
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryMaxInternetDepositReconHstry - End ");
			return (laData);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMaxInternetDepositReconHstry - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMaxInternetDepositReconHstry - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);

		}
	}
	
	/**
	 * Get the InternetDepositReconHstryData based on 
	 * DepositReconHstryReqId
	 *
	 * @param aiReqID int
	 * @return InternetDepositReconHstryData
	 * @throws RTSException 
	 */
	public InternetDepositReconHstryData qryInternetDepositReconHstryReqID(int aiReqID)
		throws RTSException
	{
		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "DepositReconHstryReqId,"
				+ "BankDepositDate,"
				+ "ProcsInitTimestmp,"
				+ "ProcsComplTimestmp,"
				+ "TmpInsrtTimestmp,"
				+ "TransCount,"
				+ "SuccessfulIndi,"
				+ "ErrMsgNo "
				+ "FROM RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY "
				+ "where "
				+ "DepositReconHstryReqId = " 
				+ aiReqID);
		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryInternetDepositReconHstryReqID - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(
				Log.DEBUG,
				this,
				" - qryInternetDepositReconHstryReqID - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryInternetDepositReconHstryReqID - SQL - End");

			InternetDepositReconHstryData laData = null;

			while (lrsQry.next())
			{
				laData = new InternetDepositReconHstryData();

				laData.setDepositReconHstryReqId(
					caDA.getIntFromDB(
						lrsQry,
						"DepositReconHstryReqId"));

				laData.setBankDepositDate(
					caDA.getRTSDateFromDB(lrsQry, "BankDepositDate"));

				laData.setProcsInitTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ProcsInitTimestmp"));

				laData.setProcsComplTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"ProcsComplTimestmp"));

				laData.setTmpInsrtTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "TmpInsrtTimestmp"));

				laData.setTransCount(
					caDA.getIntFromDB(lrsQry, "TransCount"));

				laData.setSuccessfulIndi(
					caDA.getIntFromDB(lrsQry, "SuccessfulIndi"));

				laData.setErrMsgNo(
					caDA.getIntFromDB(lrsQry, "ErrMsgNo"));
				break;
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryMaxInternetDepositReconHstry - End ");
			return (laData);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInternetDepositReconHstryReqID - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInternetDepositReconHstryReqID - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);

		}
	}
	
	/**
	 * Get the DepositReconHstryReqId's to update, that matches the
	 * tmp records we are processing
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	
	public Vector qryMaxDepositDates()
		throws RTSException
	{
		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry.append(
			"select max(DepositReconHstryReqId) as DepositReconHstryReqId, "
				+ "a.BankDepositDate from " 
				+ "rts.rts_itrnt_deposit_recon_hstry a, " 
				+ "rts.rts_itrnt_deposit_recon_tmp b where " 
				+ "PROCSCOMPLTIMESTMP is null and a.BANKDEPOSITDATE " 
				+ "is not null and TRANSCOUNT > 0 and " 
				+ "a.bankdepositdate = b.bankdepositdate " 
				+ "group by a.BANKDEPOSITDATE");
		try
		{
			Log.write(
				Log.METHOD,
				this,
				" - qryMaxDepositDates - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(
				Log.DEBUG,
				this,
				" - qryMaxDepositDates - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryMaxDepositDates - SQL - End");
			InternetDepositReconHstryData laData = null;
			Vector lvReturnData = new Vector();
			while (lrsQry.next())
			{
				laData = new InternetDepositReconHstryData();
				laData.setDepositReconHstryReqId(
					caDA.getIntFromDB(
						lrsQry,
						"DepositReconHstryReqId"));	

				laData.setBankDepositDate(
					caDA.getRTSDateFromDB(lrsQry, "BankDepositDate"));
									
				lvReturnData.add(laData);		
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryMaxInternetDepositReconHstry - End ");
			return lvReturnData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMaxInternetDepositReconHstry - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMaxInternetDepositReconHstry - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}
	
	/**
	 * Return boolean to denote whether row exists in 
	 * RTS_ITRNT_DEPOSIT_RECON_HSTRY exists where ProcsComplTimestmp
	 * is populted  
	 * 
	 * @return boolean 
	 * @throws RTSException 
	 */
	public boolean qryBatchDepositDate(String asBatchDepositDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryBatchDepositDate - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		lsQry.append(
			"SELECT  1 as NumRows  "
				+ " from RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY "
				+ " where BankDepositDate = ? "
				+ " and ProcsComplTimestmp is not null ");

		lvValues.addElement(
			new DBValue(Types.CHAR, asBatchDepositDate));

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryBatchDepositDate - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.DEBUG,
				this,
				" - qryBatchDepositDate - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryBatchDepositDate - SQL - End");

			int liNumRows = 0;
			while (lrsQry.next())
			{
				liNumRows = caDA.getIntFromDB(lrsQry, "NumRows");
				break;
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryBatchDepositDate - End ");
			return liNumRows != 0;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryBatchDepositDate - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryBatchDepositDate - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}

	/**
	 * Select row for max(DepositReconReqId) to determine if successful.
	 * Used in Evening Batch process to move data from temp to prod table. 
	 *
	 * @return Hashtable
	 * @throws RTSException 
	 */
	public Hashtable qryBatchDepositReconRptDateRange()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryBatchDepositReconRptDateRange - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;
		Hashtable lhtDates = new Hashtable();

		lsQry.append(
			"SELECT  MIN(BankDepositDate) as MinDate,"
				+ "MAX(BankDepositDate)as MaxDate "
				+ " from RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY "
				+ " where DATE(ProcsComplTimestmp) = "
				+ " Current Date - 1 day and BankDepositDate > "
				+ " (Select MAX(BankDepositDate) from "
				+ " RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY where  "
				+ " ProcsComplTimestmp is not null and "
				+ " DATE(ProcsComplTimestmp) != Current Date - 1 Day)");

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryBatchDepositReconRptDateRange - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(
				Log.DEBUG,
				this,
				" - qryBatchDepositReconRptDateRange - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryBatchDepositReconRptDateRange - SQL - End");

			while (lrsQry.next())
			{
				RTSDate laMinDate =
					caDA.getRTSDateFromDB(lrsQry, "MinDate");

				if (laMinDate != null)
				{
					lhtDates.put(
						RegRenProcessingConstants.MINDATE,
						laMinDate);
				}

				RTSDate laMaxDate =
					caDA.getRTSDateFromDB(lrsQry, "MaxDate");

				if (laMaxDate != null)
				{
					lhtDates.put(
						RegRenProcessingConstants.MAXDATE,
						laMaxDate);
				}
				break;
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryBatchDepositReconRptDateRange - End ");
			return (lhtDates);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryBatchDepositReconRptDateRange - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryBatchDepositReconRptDateRange - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);

		}
	}

	/**
	 * Method to update RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY
	 *
	 * @param  aaData 
	 * @return int 
	 * @throws RTSException
	 */
	public int updInternetDepositReconHstry(InternetDepositReconHstryData aaData)
		throws RTSException, Exception
	{
		Log.write(
			Log.METHOD,
			this,
			"updInternetDepositReconHstry - Begin");

		Vector lvValues = new Vector();

		String lsUpd = "UPDATE RTS.RTS_ITRNT_DEPOSIT_RECON_HSTRY SET ";

		if (aaData.getBankDepositDate() != null)
		{
			lsUpd = lsUpd + " BankDepositDate = ? ,";
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getBankDepositDate().toString())));

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
			// defect 10262
//			lsUpd = lsUpd + " TmpInsrtTimestmp = ? ,";
			lsUpd = lsUpd + " TmpInsrtTimestmp = Current Timestamp ,";
//			lvValues.addElement(
//				new DBValue(
//					Types.TIMESTAMP,
//					DatabaseAccess.convertToString(
//						aaData.getTmpInsrtTimestmp())));
			// end defect 10262			
		}
		// defect 10262
		if (aaData.getProcsComplTimestmp() != null)
		{
//			lsUpd = lsUpd + " ProcsComplTimestmp = ? ,";
			lsUpd = lsUpd + " ProcsComplTimestmp = Current Timestamp ,";
//			lvValues.addElement(
//				new DBValue(
//					Types.TIMESTAMP,
//					DatabaseAccess.convertToString(
//						aaData.getProcsComplTimestmp())));
			// end defect 10262
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
		if (aaData.getTransCount() != Integer.MIN_VALUE)
		{
			lsUpd = lsUpd + " TransCount = ?,";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getTransCount())));
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

		lsUpd = lsUpd + " Where DepositReconHstryReqId = ? ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getDepositReconHstryReqId())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				"updInternetDepositReconHstry - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(
				Log.SQL,
				this,
				"updInternetDepositReconHstry - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInternetDepositReconHstry - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInternetDepositReconHstry - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (Exception leEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInternetDepositReconHstry - Exception - "
					+ leEx.getMessage());
			throw leEx;
		}
	} //END OF Update METHOD

}
