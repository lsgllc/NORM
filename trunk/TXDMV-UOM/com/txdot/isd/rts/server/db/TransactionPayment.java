package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TransactionPaymentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * TransactionPayment.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/15/2001  Added code to qry by TransPostedMfIndi
 *                          delete updTransactionPayment()
 *							add postTransactionPayment()
 * K Harrell   	10/19/2001  Added purgeTransactionPayment
 * K Harrell   	11/11/2001  Removed OfcIssuanceNo, SubstaId from Purge
 * K Harrell   	12/07/2001  Removed BranchOfcId
 * K Harrell   	04/29/2002  Altered qryTransPayment for SendTrans
 * R Hicks 		07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell   	02/19/2003  Select all records on SendTrans
 *							defect 5520 
 * K Harrell   	06/05/2003  Add parameters for aiStartofc aiEndofc
 *                          add qryTransactionPayment(int,int)
 *							defect 6227  
 * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate
 *							modify purgeTransactionPayment()
 *							defect 7684  Ver 5.2.2
 * K Harrell	11/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. TransactionPaymentData
 * 							Object
 * 							modify purgeTransactionPayment() 
 * 							defect 8423 Ver 5.2.3
 * Ray Rowehl	09/27/2006	Modify the SendTrans query to include the 
 * 							MfVersionNo(VERSIONCD) in the where clause.
 * 							modify qryTransactionPayment(int, int)
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	01/08/2009	Include TransTime in Insert 
 * 							modify insTransactionPayment()
 * 							defect 9891 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeTransactionPayment()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	03/19/2010	Remove unused SQL. 
 * 							delete delTransactionPayment() 
 * 							defect 10239 Ver POS_640    
 * K Harrell	01/25/2011	delete BranchOfcId from SQL
 *  						modify qryTransactionPayment(int,int), 
 * 							 qryTransactionPayment(TransactionPaymentData) 
 * 							defect 10734 Ver 6.7.0      
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_TRANS_PAYMENT 
 *
 * @version	6.7.0			01/25/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001  13:25:27 
 */

public class TransactionPayment extends TransactionPaymentData
{
	DatabaseAccess caDA;

	/**
	 * TransactionPayment constructor 
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public TransactionPayment(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_TRANS_PAYMENT
	 *
	 * @param  aaTransactionPaymentData	
	 * @throws RTSException 
	 */
	public void insTransactionPayment(TransactionPaymentData aaTransactionPaymentData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insTransactionPayment - Begin");

		Vector lvValues = new Vector();

		// defect 9891 
		// Add TransTime 
		String lsIns =
			"INSERT into RTS.RTS_TRANS_PAYMENT("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransTime,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "PymntNo,"
				+ "PymntTypeCd,"
				+ "PymntTypeAmt,"
				+ "PymntCkNo,"
				+ "ChngDue,"
				+ "ChngDuePymntTypeCd,"
				+ "TransPostedMfIndi ) VALUES ( "
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
		// end defect 9891 
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransAMDate())));

			// defect 9891						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransTime())));
			// end defect 9891 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getPymntNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getPymntTypeCd())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getPymntTypeAmt())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getPymntCkNo())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getChngDue())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData
							.getChngDuePymntTypeCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData
							.getTransPostedMfIndi())));

			Log.write(
				Log.SQL,
				this,
				"insTransactionPayment - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insTransactionPayment - SQL - End");
			Log.write(Log.METHOD, this, "insTransactionPayment - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insTransactionPayment - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to update RTS.RTS_TRANS_PAYMENT w/ TransPostedMfIndi = 1
	 *
	 * @param  aaTransactionPaymentData TransactionPaymentData		
	 * @throws RTSException
	 */
	public void postTransactionPayment(TransactionPaymentData aaTransactionPaymentData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "postTransactionPayment - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_TRANS_PAYMENT SET "
				+ "TransPostedMfIndi = 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? AND "
				+ "PymntNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getPymntNo())));
			Log.write(
				Log.SQL,
				this,
				"postTransactionPayment - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"postTransactionPayment - SQL - End");
			Log.write(Log.METHOD, this, "postTransactionPayment - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"postTransactionPayment - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Delete RTS.RTS_TRANS_PAYMENT
	 *
	 * @param  aiPurgeAMDate int	
	 * @return int  	
	 * @throws RTSException 
	 */
	public int purgeTransactionPayment(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeTransactionPayment - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_TRANS_PAYMENT "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			// defect 8423
			// use passed int 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423	

			Log.write(
				Log.SQL,
				this,
				"purgeTransactionPayment - SQL - Begin");

			// defect 9825 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeTransactionPayment - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeTransactionPayment - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeTransactionPayment - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to query RTS_TRANS_PAYMENT data for SendTrans.
	 * 
	 * @param  aiStartOfc
	 * @param  aiEndOfc
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTransactionPayment(int aiStartOfc, int aiEndOfc)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTransactionPayment - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		ResultSet lrsQry;

		// defect 10734
		// removed BranchOfcId
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "0 as BsnDate,"
				+ "A.TransWsId, B.CashWsId,"
				+ "A.CustSeqNo,"
				+ "PymntNo,"
				+ "A.PymntTypeCd,"
				+ "PymntTypeAmt,"
				+ "PymntCkNo,"
				+ "ChngDue,"
				+ "ChngDuePymntTypeCd,"
				+ "A.TransPostedMfIndi, 1 as TransPostedLANIndi,"
				+ "B.VersionCd,"
				+ "B.TransEmpId "
				+ "FROM "
				+ "RTS.RTS_TRANS_PAYMENT A, "
				+ "RTS.RTS_TRANS_HDR B "
				+ "WHERE  "
				+ "A.OfcIssuanceno = B.OfcIssuanceNo and "
				+ "A.SubstaId = B.SubstaId and "
				+ "A.TransAMDate = B.TransAMDate and "
				+ "A.TransWsId = B.TransWsId and "
				+ "A.CustSeqNo = B.CustSeqNo and "
				+ "TransPostedMFIndi = 0 AND "
				+ "a.OfcIssuanceNo between "
				+ aiStartOfc
				+ " AND "
				+ aiEndOfc
				+ " AND B.VERSIONCD = "
				+ SystemProperty.getMainFrameVersion());
		// end defect 8959

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionPayment - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionPayment - SQL - End");

			while (lrsQry.next())
			{
				TransactionPaymentData laTransactionPaymentData =
					new TransactionPaymentData();
				laTransactionPaymentData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionPaymentData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionPaymentData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionPaymentData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionPaymentData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionPaymentData.setPymntNo(
					caDA.getStringFromDB(lrsQry, "PymntNo"));
				laTransactionPaymentData.setPymntTypeCd(
					caDA.getIntFromDB(lrsQry, "PymntTypeCd"));
				laTransactionPaymentData.setPymntTypeAmt(
					caDA.getDollarFromDB(lrsQry, "PymntTypeAmt"));
				laTransactionPaymentData.setPymntCkNo(
					caDA.getStringFromDB(lrsQry, "PymntCkNo"));
				laTransactionPaymentData.setChngDue(
					caDA.getDollarFromDB(lrsQry, "ChngDue"));
				laTransactionPaymentData.setChngDuePymntTypeCd(
					caDA.getIntFromDB(lrsQry, "ChngDuePymntTypeCd"));
				laTransactionPaymentData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMfIndi"));
				laTransactionPaymentData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laTransactionPaymentData.setTransPostedLANIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedLANIndi"));
				laTransactionPaymentData.setVersionCd(
					caDA.getIntFromDB(lrsQry, "VersionCd"));
				laTransactionPaymentData.setBsnDate(
					caDA.getIntFromDB(lrsQry, "BsnDate"));
				laTransactionPaymentData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionPaymentData);
			} //End of While
			// end defect 10734 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionPayment - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionPayment - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Method to Query RTS.RTS_TRANS_PAYMENT
	 * 
	 * @param  aaTransactionPaymentData	
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionPayment(TransactionPaymentData aaTransactionPaymentData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTransactionPayment - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10734 
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "0 as BsnDate,"
				+ "A.TransWsId, B.CashWsId,"
				+ "A.CustSeqNo,"
				+ "PymntNo,"
				+ "A.PymntTypeCd,"
				+ "PymntTypeAmt,"
				+ "PymntCkNo,"
				+ "ChngDue,"
				+ "ChngDuePymntTypeCd,"
				+ "A.TransPostedMfIndi, 1 as TransPostedLANIndi,"
				+ "B.VersionCd,"
				+ "B.TransEmpId "
				+ "FROM "
				+ "RTS.RTS_TRANS_PAYMENT A, "
				+ "RTS.RTS_TRANS_HDR B "
				+ " WHERE  "
				+ "A.OfcIssuanceno = B.OfcIssuanceNo and "
				+ "A.SubstaId = B.SubstaId and "
				+ "A.TransAMDate = B.TransAMDate and "
				+ "A.TransWsId = B.TransWsId and "
				+ "A.CustSeqNo = B.CustSeqNo and ");

		if (aaTransactionPaymentData.getTransPostedMfIndi() == 1)
		{
			lsQry.append(" TransPostedMFIndi = 0 ");
		}
		else
		{
			lsQry.append(
				" a.OfcIssuanceNo = ? AND a.SubstaId = ? AND "
					+ " a.TransAMDate = ? AND "
					+ " a.TransWsId = ? AND "
					+ " a.CustSeqNo = ?  order by PymntNo");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionPaymentData.getCustSeqNo())));
		}
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionPayment - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionPayment - SQL - End");

			while (lrsQry.next())
			{
				TransactionPaymentData laTransactionPaymentData =
					new TransactionPaymentData();
				laTransactionPaymentData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionPaymentData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionPaymentData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionPaymentData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionPaymentData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionPaymentData.setPymntNo(
					caDA.getStringFromDB(lrsQry, "PymntNo"));
				laTransactionPaymentData.setPymntTypeCd(
					caDA.getIntFromDB(lrsQry, "PymntTypeCd"));
				laTransactionPaymentData.setPymntTypeAmt(
					caDA.getDollarFromDB(lrsQry, "PymntTypeAmt"));
				laTransactionPaymentData.setPymntCkNo(
					caDA.getStringFromDB(lrsQry, "PymntCkNo"));
				laTransactionPaymentData.setChngDue(
					caDA.getDollarFromDB(lrsQry, "ChngDue"));
				laTransactionPaymentData.setChngDuePymntTypeCd(
					caDA.getIntFromDB(lrsQry, "ChngDuePymntTypeCd"));
				laTransactionPaymentData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMfIndi"));
				laTransactionPaymentData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laTransactionPaymentData.setTransPostedLANIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedLANIndi"));
				laTransactionPaymentData.setVersionCd(
					caDA.getIntFromDB(lrsQry, "VersionCd"));
				laTransactionPaymentData.setBsnDate(
					caDA.getIntFromDB(lrsQry, "BsnDate"));
				laTransactionPaymentData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionPaymentData);
			} //End of While
			// end defect 10734 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionPayment - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionPayment - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}
}