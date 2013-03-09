package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TransactionFundsDetailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * TransactionFundsDetail.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/19/2001  Added purgeTransactionFundsDetail
 * K Harrell   	03/22/2002  Removed join to RTS_Acct_Codes
 * R Hicks      07/12/2002  Add call to closeLastStatement() after 
 *							a query
 * K Harrell	01/22/2004	Replaced caDA.convertToString w/ 
 * 							DatabaseAccess.convertToString
 * 							Version 5.2.0
 * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate
 *							modify purgeTransactionFundsDetail()
 *							defect 7684  Ver 5.2.2
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							TransactionFundsDetail Object
 * 							modify purgeTransactionFundsDetail() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeTransactionFundsDetail()
 * 							defect 9825 Ver Defect_POS_D   
 * K Harrell	02/16/2010	Removed previously added columns 
 * 							defect 10366 Ver POS_640 
 * K Harrell	03/19/2010	Remove unused method. 
 * 							delete updTransactionFundsDetail()
 * 							defect 10239 Ver POS_640
 * K Harrell 	06/14/2010 	add TransId to insert
 * 							modify insTransactionFundsDetail()  
 * 							defect 10505 Ver 6.5.0   
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with the database.
 * (RTS_TR_FDS_DETAIL)
 * 
 * @version 6.5.0   		06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001
 */
public class TransactionFundsDetail extends TransactionFundsDetailData
{
	DatabaseAccess caDA;

	/**
	 * TransactionFundsDetail constructor comment.
	 *
	 * @param  aaDA
	 * @throws RTSException 
	 */
	public TransactionFundsDetail(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Delete from RTS.RTS_TR_FDS_DETAIL
	 *
	 * @param  aaTransactionFundsDetailData	
	 * @throws RTSException 
	 */
	public void delTransactionFundsDetail(TransactionFundsDetailData aaTransactionFundsDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delTransactionFundsDetail - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_TR_FDS_DETAIL "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getCustSeqNo())));
			if (aaTransactionFundsDetailData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionFundsDetailData
								.getTransTime())));
			}
			Log.write(
				Log.SQL,
				this,
				"delTransactionFundsDetail - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delTransactionFundsDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"delTransactionFundsDetail - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delTransactionFundsDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Insert into RTS.RTS_TR_FDS_DETAIL
	 *
	 * @param  aaTransactionFundsDetailData	
	 * @throws RTSException
	 */
	public void insTransactionFundsDetail(TransactionFundsDetailData aaTransactionFundsDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insTransactionFundsDetail - Begin");

		Vector lvValues = new Vector();

		// defect 10505
		// add TransId 
		String lsIns =
			"INSERT into RTS.RTS_TR_FDS_DETAIL ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransId,"
				+ "AcctItmCd,"
				+ "ItmPrice,"
				+ "FundsRptDate,"
				+ "RptngDate,"
				+ "FundsCat,"
				+ "FundsRcvngEnt,"
				+ "FundsRcvdAmt,"
				+ "ItmQty,"
				+ "CashDrawerIndi ) VALUES ( "
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
		// end defect 10505 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransCd())));
						
			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransId())));
			// end defect 10505 
			
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getAcctItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getItmPrice())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getFundsRptDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getRptngDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getFundsCat())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getFundsRcvngEnt())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getFundsRcvdAmt())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getItmQty())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getCashDrawerIndi())));

			Log.write(
				Log.SQL,
				this,
				"insTransactionFundsDetail - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insTransactionFundsDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insTransactionFundsDetail - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insTransactionFundsDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_TR_FDS_DETAIL
	 *
	 * @param  aiPurgeAMDate int	
	 * @return int  
	 * @throws RTSexception
	 */
	public int purgeTransactionFundsDetail(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeTransactionFundsDetail - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_TR_FDS_DETAIL "
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
				"purgeTransactionFundsDetail - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeTransactionFundsDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeTransactionFundsDetail - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeTransactionFundsDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_TR_FDS_DETAIL
	 *
	 * @param  aaTransactionFundsDetailData	
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionFundsDetail(TransactionFundsDetailData aaTransactionFundsDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionFundsDetail - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "AcctItmCd,"
				+ "ItmPrice,"
				+ "FundsRptDate,"
				+ "RptngDate,"
				+ "FundsCat,"
				+ "FundsRcvngEnt,"
				+ "FundsRcvdAmt,"
				+ "ItmQty,"
				+ "CashDrawerIndi "
				+ "FROM RTS.RTS_TR_FDS_DETAIL "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? AND "
				+ "TransTime = ? ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransTime())));
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionFundsDetail - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionFundsDetail - SQL - End");

			while (lrsQry.next())
			{
				TransactionFundsDetailData laTransactionFundsDetailData =
					new TransactionFundsDetailData();
				laTransactionFundsDetailData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionFundsDetailData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionFundsDetailData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionFundsDetailData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionFundsDetailData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionFundsDetailData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionFundsDetailData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionFundsDetailData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laTransactionFundsDetailData.setItmPrice(
					caDA.getDollarFromDB(lrsQry, "ItmPrice"));
				laTransactionFundsDetailData.setFundsRptDate(
					caDA.getIntFromDB(lrsQry, "FundsRptDate"));
				laTransactionFundsDetailData.setRptngDate(
					caDA.getIntFromDB(lrsQry, "RptngDate"));
				laTransactionFundsDetailData.setFundsCat(
					caDA.getStringFromDB(lrsQry, "FundsCat"));
				laTransactionFundsDetailData.setFundsRcvngEnt(
					caDA.getStringFromDB(lrsQry, "FundsRcvngEnt"));
				laTransactionFundsDetailData.setFundsRcvdAmt(
					caDA.getDollarFromDB(lrsQry, "FundsRcvdAmt"));
				laTransactionFundsDetailData.setItmQty(
					caDA.getIntFromDB(lrsQry, "ItmQty"));
				laTransactionFundsDetailData.setCashDrawerIndi(
					caDA.getIntFromDB(lrsQry, "CashDrawerIndi"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionFundsDetailData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionFundsDetail - End ");
			return (lvRslt);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionFundsDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionFundsDetail - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_TR_FDS_DETAIL for Complete Vehicle Transaction
	 *
	 * @param TransactionFundsDetail Data	
	 * @return  Vector 	
	 */
	public Vector qryTransactionFundsDetailForComplete(TransactionFundsDetailData aaTransactionFundsDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionFundsDetailForComplete - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "AcctItmCd,"
				+ "ItmPrice,"
				+ "ItmQty "
				+ "FROM RTS.RTS_TR_FDS_DETAIL "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? AND "
				+ "TransTime = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionFundsDetailData.getTransTime())));
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionFundsDetailForComplete - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionFundsDetailForComplete - SQL - End");
			while (lrsQry.next())
			{
				TransactionFundsDetailData laTransactionFundsDetailData =
					new TransactionFundsDetailData();
				laTransactionFundsDetailData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionFundsDetailData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionFundsDetailData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionFundsDetailData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionFundsDetailData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionFundsDetailData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionFundsDetailData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laTransactionFundsDetailData.setItmPrice(
					caDA.getDollarFromDB(lrsQry, "ItmPrice"));
				laTransactionFundsDetailData.setItmQty(
					caDA.getIntFromDB(lrsQry, "ItmQty"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionFundsDetailData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionFundsDetailForComplete - End ");
			return (lvRslt);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionFundsDetailForComplete - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionFundsDetailForComplete - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	// defect 10239 
	//	/**
	//	 * Method to update a TransactionFundsDetail
	//	 *
	//	 * @param  aaTransactionFundsDetailData	
	//	 * @throws RTSException
	//	 */
	//	public void updTransactionFundsDetail(TransactionFundsDetailData aaTransactionFundsDetailData)
	//		throws RTSException
	//	{
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			"updTransactionFundsDetail - Begin");
	//
	//		Vector lvValues = new Vector();
	//
	//		String lsUpd =
	//			"UPDATE RTS.RTS_TR_FDS_DETAIL SET "
	//				+ "OfcIssuanceNo = ?, "
	//				+ "SubstaId = ?, "
	//				+ "TransAMDate = ?, "
	//				+ "TransWsId = ?, "
	//				+ "CustSeqNo = ?, "
	//				+ "TransTime = ?, "
	//				+ "TransCd = ?, "
	//				+ "AcctItmCd = ?, "
	//				+ "ItmPrice = ?, "
	//				+ "FundsRptDate = ?, "
	//				+ "RptngDate = ?, "
	//				+ "FundsCat = ?, "
	//				+ "FundsRcvngEnt = ?, "
	//				+ "FundsRcvdAmt = ?, "
	//				+ "ItmQty = ?, "
	//				+ "CashDrawerIndi = ? "
	//				+ "WHERE "
	//				+ "OfcIssuanceNo = ? AND "
	//				+ "SubstaId = ? AND "
	//				+ "TransAMDate = ? AND "
	//				+ "TransWsId = ? AND "
	//				+ "CustSeqNo = ? AND "
	//				+ "TransTime = ? ";
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getSubstaId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getTransAMDate())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getTransWsId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getCustSeqNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getTransTime())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getTransCd())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getAcctItmCd())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.DECIMAL,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getItmPrice())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getFundsRptDate())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getRptngDate())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getFundsCat())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getFundsRcvngEnt())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.DECIMAL,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getFundsRcvdAmt())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getItmQty())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getCashDrawerIndi())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getSubstaId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData
	//							.getTransAMDate())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getTransWsId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getCustSeqNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionFundsDetailData.getTransTime())));
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"updTransactionFundsDetail - SQL - Begin");
	//			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"updTransactionFundsDetail - SQL - End");
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				"updTransactionFundsDetail - End");
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"updTransactionFundsDetail - Exception - "
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	} //END OF Update METHOD
	// end defect 10239 
} //END OF CLASS
