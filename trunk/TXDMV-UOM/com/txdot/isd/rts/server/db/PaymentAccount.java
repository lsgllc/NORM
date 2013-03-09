package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.PaymentAccountData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * PaymentAccount.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/15/2001	Correction to test for PymntLocId
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/17/2002	Add purgePaymentAccount
 *							defect 4601
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgePaymentAccount()
 * 							defect 9825 Ver Defect_POS_D      
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PYMNT_ACCT
 * 
 * @version	5.2.3		05/03/2005
 * @author 	Kathy Harrell
 * <p> Creation Date:	09/06/2001 18:31:07 
 */

public class PaymentAccount extends PaymentAccountData
{
	DatabaseAccess caDA;
	/**
	 * PaymentAccount constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PaymentAccount(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_PYMNT_ACCT
	 *
	 * @param  aaPaymentAccountData	PaymentAccountData
	 * @throws RTSException 
	 */
	public void delPaymentAccount(PaymentAccountData aaPaymentAccountData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delPaymentAccount - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"UPDATE RTS.RTS_PYMNT_ACCT SET DeleteIndi = 1, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "PymntLocId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getPymntLocId())));
			Log.write(Log.SQL, this, "delPaymentAccount - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delPaymentAccount - SQL - End");
			Log.write(Log.METHOD, this, "delPaymentAccount - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delPaymentAccount - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Insert into RTS.RTS_PYMNT_ACCT
	 *
	 * @param  aaPaymentAccountData	
	 * @throws RTSException
	 */
	public void insPaymentAccount(PaymentAccountData aaPaymentAccountData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insPaymentAccount - Begin");
		Log.write(
			Log.METHOD,
			this,
			"insPaymentAccount - First Try Update");

		int liNumRows = updPaymentAccount(aaPaymentAccountData);
		if (liNumRows == 0)
		{
			Log.write(
				Log.METHOD,
				this,
				"insPaymentAccount - Next Try Insert");
			Vector lvValues = new Vector();
			String lsIns =
				"INSERT into RTS.RTS_PYMNT_ACCT ("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "PymntLocId,"
					+ "PymntLocDesc,"
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
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
							aaPaymentAccountData.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaPaymentAccountData.getSubstaId())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaPaymentAccountData.getPymntLocId())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaPaymentAccountData.getPymntLocDesc())));
				Log.write(
					Log.SQL,
					this,
					"insPaymentAccount - SQL - Begin");
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(
					Log.SQL,
					this,
					"insPaymentAccount - SQL - End");
				Log.write(Log.METHOD, this, "insPaymentAccount - End");
			}
			catch (RTSException leRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insPaymentAccount - Exception - "
						+ leRTSEx.getMessage());
				throw leRTSEx;
			}
		}
	} //END OF INSERT METHOD
	/**
	 * Method to Delete from RTS.RTS_PYMNT_ACCT for Purge
	 *
	 * @param  aiNumDays  int 
	 * @return int
	 * @throws RTSException 
	 */
	public int purgePaymentAccount(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgePaymentAccount - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_PYMNT_ACCT WHERE DELETEINDI = 1 "
				+ "and days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(
				Log.SQL,
				this,
				"purgePaymentAccount - SQL - Begin");

			// defect 9825  
			// Return number of rows returned
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgePaymentAccount - SQL - End");
			Log.write(Log.METHOD, this, "purgePaymentAccount - End");
			return liNumRows;
			// end defect 9825   
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgePaymentAccount - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Query RTS.RTS_PYMNT_ACCT
	 * 
	 * @param  aaPaymentAccountData	PaymentAccountData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryPaymentAccount(PaymentAccountData aaPaymentAccountData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPaymentAccount - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "PymntLocId,"
				+ "PymntLocDesc,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_PYMNT_ACCT where OfcIssuanceNo = ? "
				+ "and SubstaId = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getSubstaId())));
			if (aaPaymentAccountData.getPymntLocId() != null)
			{
				lsQry.append(" and PymntLocId = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaPaymentAccountData.getPymntLocId())));
			}
			if (aaPaymentAccountData.getChngTimestmp() == null)
			{
				lsQry.append(" and DeleteIndi = 0 ");
			}
			else
			{
				lsQry.append(" and ChngTimestmp > ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaPaymentAccountData.getChngTimestmp())));
			}
			lsQry.append(" order by 1,2,3 ");

			Log.write(
				Log.SQL,
				this,
				" - qryPaymentAccount - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentAccount - SQL - End");

			while (lrsQry.next())
			{
				PaymentAccountData laPaymentAccountData =
					new PaymentAccountData();
				laPaymentAccountData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laPaymentAccountData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laPaymentAccountData.setPymntLocId(
					caDA.getStringFromDB(lrsQry, "PymntLocId"));
				laPaymentAccountData.setPymntLocDesc(
					caDA.getStringFromDB(lrsQry, "PymntLocDesc"));
				laPaymentAccountData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laPaymentAccountData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentAccountData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryPaymentAccount - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPaymentAccount - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_PYMNT_ACCT 
	 * 
	 * @param  aaPaymentAccountData  PaymentAccountData	
	 * @return int
	 * @throws RTSExceptino 
	 */
	public int updPaymentAccount(PaymentAccountData aaPaymentAccountData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updPaymentAccount - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_PYMNT_ACCT SET "
				+ "PymntLocDesc = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "PymntLocId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getPymntLocDesc())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaPaymentAccountData.getPymntLocId())));
			Log.write(Log.SQL, this, "updPaymentAccount - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updPaymentAccount - SQL - End");
			Log.write(Log.METHOD, this, "updPaymentAccount - End");
			return (liNumRows);
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updPaymentAccount - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS