package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InternetDepositReconData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetDepositRecon.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/12/2009	Created
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	06/09/2009	add purgeInternetDepositRecon()
 * 							defect 9955 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ITRNT_DEPOSIT_RECON
 *
 * @version	Defect_POS_F	06/09/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/12/2009
 */
public class InternetDepositRecon extends InternetDepositReconData
{

	protected DatabaseAccess caDA;

	/**
	 * InternetDepositRecon constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public InternetDepositRecon(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to insert into RTS.RTS_ITRNT_DEPOSIT_RECON
	 *   from RTS.RTS_ITRNT_DEPOSIT_RECON_TMP
	 * 
	 * @throws RTSException 
	 */
	public int insInternetDepositRecon() throws RTSException
	{
		Log.write(Log.METHOD, this, "insInternetDepositRecon - Begin");

		String lsIns =
			"Insert into RTS.RTS_ITRNT_DEPOSIT_RECON "
				+ " (Select * from RTS.RTS_ITRNT_DEPOSIT_RECON_TMP) ";

		Log.write(
			Log.SQL,
			this,
			"insInternetDepositRecon - SQL - Begin");
		try
		{
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsIns, new Vector());

			Log.write(
				Log.SQL,
				this,
				"insInternetDepositRecon - SQL - End");

			return liNumRows;

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInternetDepositRecon - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Purge from RTS_ITRNT_DEPOSIT_RECON
	 *  
	 * @param aaPurgeDate 
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeInternetDepositRecon(RTSDate aaPurgeDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeInternetDepositRecon- Begin");

		String lsDel =
			"DELETE FROM RTS.RTS_ITRNT_DEPOSIT_RECON WHERE "
				+ "BANKDEPOSITDATE <= '"
				+ aaPurgeDate.toString()+"'";

		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeInternetDepositRecon- SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

			Log.write(
				Log.SQL,
				this,
				"purgeInternetDepositRecon- SQL - End");
			Log.write(Log.METHOD, this, "purgeInternetDepositRecon- End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInternetDepositRecon- Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Query RTS.RTS_ITRNT_DEPOSIT_RECON for Report 
	 * 
	 * @param  aaData ReportSearchData	
	 * @throws RTSException 
	 */
	public Vector qryInternetDepositRecon(ReportSearchData aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInternetDepositRecon - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "ItrntTraceNo,"
				+ "OfcIssuanceNo,"
				+ "BankDepositDate,"
				+ "PymntCardType,"
				+ "Last4PymntCardNo,"
				+ "PymntAmt,"
				+ "TOLTransDate "
				+ "FROM RTS.RTS_ITRNT_DEPOSIT_RECON "
				+ "where "
				+ " OfcIssuanceNo = ? and "
				+ " BankDepositDate between "
				+ " ?  and ?   ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getIntKey1())));

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaData.getDate1().toString())));

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaData.getDate2().toString())));

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryInternetDepositRecon - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.DEBUG,
				this,
				" - qryInternetDepositRecon - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryInternetDepositRecon - SQL - End");

			while (lrsQry.next())
			{
				InternetDepositReconData laData =
					new InternetDepositReconData();

				laData.setItrntTraceNo(
					caDA.getStringFromDB(lrsQry, "ItrntTraceNo"));

				laData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				laData.setBankDepositDate(
					caDA.getRTSDateFromDB(lrsQry, "BankDepositDate"));

				laData.setPymntCardType(
					caDA.getStringFromDB(lrsQry, "PymntCardType"));

				laData.setLast4PymntCardNo(
					caDA.getStringFromDB(lrsQry, "Last4PymntCardNo"));

				laData.setPymntAmt(
					caDA.getDollarFromDB(lrsQry, "PymntAmt"));

				laData.setTOLTransDate(
					caDA.getRTSDateFromDB(lrsQry, "TOLTransDate"));

				lvRslt.add(laData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInternetDepositRecon - End ");

			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInternetDepositRecon - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInternetDepositRecon - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	} //END OF QRY METHOD
}
