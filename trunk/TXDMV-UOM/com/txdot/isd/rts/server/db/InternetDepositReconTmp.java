package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InternetDepositReconData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetDepositReconTmp.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/12/2009	Created
 * 							defect 9935 Ver Defect_POS_D 
 * B Brown		02/27/2009	Recheckin due to ClearCase problem.
 * 							defect 9935 Ver Defect_POS_D 
 * B Brown		03/20/2009	Add catch block for NullPointerException
 * 							modify insInternetDepositReconTmp()
 * 							defect 9986 Ver Defect_POS_E 
 * B Brown		12/11/2009	Add an or condition to find dups between
 * 							rts_itrnt_deposit_recon_tmp and 
 * 							rts_itrnt_deposit_recon
 * 							modify qryCountDuplicates() 
 * 							defect 10262 Ver Defect_POS_H
 * K Harrell 	03/19/2010	Removed unused method. 
 * 							delete qryCountDistinctBankDepositDate()
 * 							defect 10239 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ITRNT_DEPOSIT_RECON_TMP 
 *
 * @version	POS_640 			03/19/2010
 * @author	Kathy Harrell
 * <br>Creation Date:			02/12/2009
 */
public class InternetDepositReconTmp extends InternetDepositReconData
{

	protected DatabaseAccess caDA;

	/**
	 * InternetDepositReconTmp constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public InternetDepositReconTmp(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to delete from RTS.RTS_ITRNT_DEPOSIT_RECON_TMP
	 * 
	 * @throws RTSException 
	 */
	public int delInternetDepositReconTmp() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delInternetDepositReconTmp - Begin");

		String lsDel = "DELETE from RTS.RTS_ITRNT_DEPOSIT_RECON_TMP";

		Log.write(
			Log.SQL,
			this,
			"delInternetDepositReconTmp - SQL - Begin");
		try
		{
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());
			Log.write(
				Log.SQL,
				this,
				"delInternetDepositReconTmp - SQL - End");

			return liNumRows;

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delInternetDepositReconTmp - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Insert into RTS.RTS_ITRNT_DEPOSIT_RECON_TMP
	 * 
	 * @param  aaVector	Vector 	
	 * @throws RTSException 
	 */
	public void insInternetDepositReconTmp(Vector aaVector)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insInternetDepositReconTmp - Begin");

		InternetDepositReconData laItrntDepositReconData = null;

		for (int i = 0; i < aaVector.size(); i++)
		{
			try
			{
				laItrntDepositReconData =
					(InternetDepositReconData) aaVector.elementAt(i);

				Vector lvValues = new Vector();

				String lsIns =
					"INSERT into RTS.RTS_ITRNT_DEPOSIT_RECON_TMP("
						+ "ItrntTraceNo,"
						+ "OfcIssuanceNo,"
						+ "BankDepositDate,"
						+ "PymntCardType,"
						+ "PymntAmt,"
						+ "Last4PymntCardNo,"
						+ "TOLTransDate) VALUES ( "
						+ " ?,"
						+ " ?,"
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ? )";

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laItrntDepositReconData
								.getItrntTraceNo())));

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laItrntDepositReconData
								.getOfcIssuanceNo())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laItrntDepositReconData
								.getBankDepositDate()
								.toString())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laItrntDepositReconData
								.getPymntCardType())));

				lvValues.addElement(
					new DBValue(
						Types.DECIMAL,
						DatabaseAccess.convertToString(
							laItrntDepositReconData.getPymntAmt())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laItrntDepositReconData
								.getLast4PymntCardNo())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laItrntDepositReconData
								.getTOLTransDate()
								.toString())));

				Log.write(
					Log.SQL,
					this,
					"insInternetDepositReconTmp - SQL - Begin");

				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

				Log.write(
					Log.SQL,
					this,
					"insInternetDepositReconTmp - SQL - End");
				// testing
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insInternetDepositReconTmp - Exception - "
						+ aeRTSEx.getMessage());

				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getItrntTraceNo() = "
						+ laItrntDepositReconData.getItrntTraceNo());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getBankDepositDate() = "
						+ laItrntDepositReconData.getBankDepositDate());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getLast4PymntCardNo() = "
						+ laItrntDepositReconData.getLast4PymntCardNo());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getOfcIssuanceNo() = "
						+ laItrntDepositReconData.getOfcIssuanceNo());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getPymntCardType() = "
						+ laItrntDepositReconData.getPymntCardType());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getPymntAmt() = "
						+ laItrntDepositReconData.getPymntAmt());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getTOLTransDate() = "
						+ laItrntDepositReconData.getTOLTransDate());
				throw aeRTSEx;
			}
			// defect 9986
			catch (NullPointerException aeNPEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insInternetDepositReconTmp - Exception - "
						+ aeNPEx.getMessage());

				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getItrntTraceNo() = "
						+ laItrntDepositReconData.getItrntTraceNo());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getBankDepositDate() = "
						+ laItrntDepositReconData.getBankDepositDate());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getLast4PymntCardNo() = "
						+ laItrntDepositReconData.getLast4PymntCardNo());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getOfcIssuanceNo() = "
						+ laItrntDepositReconData.getOfcIssuanceNo());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getPymntCardType() = "
						+ laItrntDepositReconData.getPymntCardType());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getPymntAmt() = "
						+ laItrntDepositReconData.getPymntAmt());
				Log.write(
					Log.SQL_EXCP,
					this,
					"laItrntDepositReconData.getTOLTransDate() = "
						+ laItrntDepositReconData.getTOLTransDate());

				RTSException leRTSEx = new RTSException();
				leRTSEx.setDetailMsg(aeNPEx.getMessage());
				leRTSEx.setMessage(
					"NullPointerException while inserting into "
						+ "RTS_ITRNT_DEPOSIT_RECON_TMP");
				throw leRTSEx;
			}
			// end defect 9986
		}
		Log.write(Log.METHOD, this, "insInternetDepositReconTmp - End");

	} //END OF INSERT METHOD

	// defect 10239 
	//	/**
	//	 * Select Count(Distinct BankDepositDate) from table to 
	//	 * determine if valid data
	//	 *
	//	 * @return int
	//	 * @throws RTSException 
	//	 */
	//	public int qryCountDistinctBankDepositDate() throws RTSException
	//	{
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			" - qryCountDistinctBankDepositDate - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ " Count(Distinct "
	//				+ "BankDepositDate) as Count "
	//				+ "FROM RTS.RTS_ITRNT_DEPOSIT_RECON_TMP ");
	//
	//		try
	//		{
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryCountDistinctBankDepositDate - SQL - Begin");
	//
	//			lrsQry =
	//				caDA.executeDBQuery(lsQry.toString(), new Vector());
	//
	//			Log.write(
	//				Log.DEBUG,
	//				this,
	//				" - qryCountDistinctBankDepositDate - SQL - End");
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryCountDistinctBankDepositDate - SQL - End");
	//
	//			int liNumDays = 0;
	//
	//			while (lrsQry.next())
	//			{
	//				liNumDays = caDA.getIntFromDB(lrsQry, "Count");
	//
	//				break;
	//			}
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryCountDistinctBankDepositDate - End ");
	//
	//			return liNumDays;
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryCountDistinctBankDepositDate - Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryCountDistinctBankDepositDate - Exception "
	//					+ aeRTSEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
	//		}
	//	}
	// end defect 10239 

	/**
	 * Select duplicate rows between Temp table and Production  
	 *
	 * @return int
	 * @throws RTSException 
	 */
	public int qryCountDuplicates() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCountDuplicates - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		lsQry
			.append(
				"SELECT "
				+ " Count(*) as Count "
				+ " FROM RTS.RTS_ITRNT_DEPOSIT_RECON_TMP A "
				+ " Where Exists (Select * from "
				+ " RTS.RTS_ITRNT_DEPOSIT_RECON B Where "
		// defect 10262
		// + " A.ItrntTraceNo = B.ItrntTraceNo)");
		+" A.ItrntTraceNo = B.ItrntTraceNo or"
			+ " A.BankDepositDate = B.BankDepositDate)");
		// end defect 10262

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryCountDuplicates - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(
				Log.DEBUG,
				this,
				" - qryCountDuplicates - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryCountDuplicates - SQL - End");

			int liCount = 0;

			while (lrsQry.next())
			{
				liCount = caDA.getIntFromDB(lrsQry, "Count");

				break;
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryCountDuplicates - End ");

			return liCount;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCountDuplicates - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCountDuplicates - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}
}
