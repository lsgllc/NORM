package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.InventoryFunctionTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * InventoryFunctionTransaction.java
 *
 * (c) Texas Department of Transportation 2001 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/16/2001  Added qryInventoryFunctionTransactionInvoice
 * C Walker		10/16/2001  Added a space after the table name in the 
 *							qryInvFuncTransInvc
 * K Harrell   	10/18/2001  Added columns/join (OfcIssuanceCd,TransEmpId
 *                          to qryInvFunctionTransaction for SendTrans
 * K Harrell   	10/19/2001  add purgeInvFunctionTransaction()
 * K Harrell   	10/23/2001  add voidInvFunctionTransaction()
 *							delete updInvFunctionTransaction()
 * K Harrell   	10/24/2001  Renamed TransEmpId to EmpId for SendTrans
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate
 *							modify purgeInventoryFunctionTransaction()
 *							defect 7684  Ver 5.2.2
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							InventoryFunctionTransactionData Object
 * 							modify purgeInventoryFunctionTransaction() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeInventoryHistory()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	06/15/2010  add TransId to insert
 * 							modify insInventoryFunctionTransaction()
 * 							defect 10505 Ver 6.5.0        
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with the database.
 * (RTS_INV_FUNC_TRANS) 
 *
 * @version	6.5.0  			06/15/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001 10:14:10 
 */

public class InventoryFunctionTransaction
	extends InventoryFunctionTransactionData
{
	DatabaseAccess caDA;

	/**
	 * InventoryFunctionTransaction constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public InventoryFunctionTransaction(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Delete from RTS.RTS_INV_FUNC_TRANS
	 *  
	 * @param  aaInventoryFunctionTransactionData InventoryFunctionTransactionData	
	 * @throws RTSException
	 */
	public void delInventoryFunctionTransaction(InventoryFunctionTransactionData aaInventoryFunctionTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delInventoryFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_INV_FUNC_TRANS "
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
						aaInventoryFunctionTransactionData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getCustSeqNo())));

			if (aaInventoryFunctionTransactionData.getTransTime() != 0)
			{
				lsDel = lsDel + "AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryFunctionTransactionData
								.getTransTime())));
			}
			Log.write(
				Log.SQL,
				this,
				"delInventoryFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delInventoryFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"delInventoryFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delInventoryFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Insert into RTS.RTS_INV_FUNC_TRANS
	 *
	 * @param  aaInventoryFunctionTransactionData InventoryFunctionTransactionData	
	 * @throws RTSException 
	 */
	public void insInventoryFunctionTransaction(InventoryFunctionTransactionData aaInventoryFunctionTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insInventoryFunctionTransaction - Begin");
		Vector lvValues = new Vector();

		// defect 10505
		// Add TransId  
		String lsIns =
			"INSERT into RTS.RTS_INV_FUNC_TRANS("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransId,"
				+ "VoidedTransIndi,"
				+ "SubconIssueDate,"
				+ "InvcNo,"
				+ "InvcCorrIndi ) VALUES ( "
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
						aaInventoryFunctionTransactionData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransCd())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransId())));
			// end defect 10505 
			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getVoidedTransIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getSubconIssueDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getInvcNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getInvcCorrIndi())));
			Log.write(
				Log.SQL,
				this,
				"insInventoryFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insInventoryFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insInventoryFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInventoryFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	 * Method to Delete from RTS.RTS_INV_FUNC_TRANS - Purge
	 * RTS I - INVFUND1
	 * 
	 * @param  aiPurgeAMDate int
	 * @return int 	
	 * @throws RTSException
	 */
	public int purgeInventoryFunctionTransaction(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeInventoryFunctionTransaction - Begin");
		Vector lvValues = new Vector();
		String lsDel =
			" DELETE FROM RTS.RTS_INV_FUNC_TRANS A "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			// defect 8423
			// Used passed int
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423

			Log.write(
				Log.SQL,
				this,
				"purgeInventoryFunctionTransaction - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeInventoryFunctionTransaction - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInventoryFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Query RTS.RTS_INV_FUNC_TRANS
	 * 
	 * @param  aaInventoryFunctionTransactionData InventoryFunctionTransactionData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryFunctionTransaction(InventoryFunctionTransactionData aaInventoryFunctionTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryFunctionTransaction - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// Added OfcIssuanceCd from TransHdr,
		//       EmpId from Trans
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "B.OfcIssuanceCd,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "A.TransTime,"
				+ "A.TransCd,"
				+ "C.TransEmpId as EmpId,"
				+ "A.VoidedTransIndi,"
				+ "A.SubconIssueDate,"
				+ "A.InvcNo,"
				+ "A.InvcCorrIndi "
				+ "FROM "
				+ "RTS.RTS_INV_FUNC_TRANS A,"
				+ "RTS.RTS_TRANS_HDR B, "
				+ "RTS.RTS_TRANS C "
				+ "WHERE "
				+ "A.OfcIssuanceNo = ? AND "
				+ "A.SubstaId = ? AND "
				+ "A.TransAMDate = ? AND "
				+ "A.TransWsId = ? AND "
				+ "A.CustSeqNo = ? AND "
				+ "A.TransTime = ? AND "
				+ "A.OfcIssuanceNo = B.OfcIssuanceNo AND "
				+ "A.SubstaId = B.SubstaId AND "
				+ "A.TransAMDate = B.TransAMDate AND "
				+ "A.CustSeqNo = B.CustSeqNo AND "
				+ "A.TransWsId = B.TransWsId AND "
				+ "A.OfcIssuanceNo = C.OfcIssuanceNo AND "
				+ "A.SubstaId = C.SubstaId AND "
				+ "A.TransAMDate = C.TransAMDate AND "
				+ "A.CustSeqNo = C.CustSeqNo AND "
				+ "A.TransWsId = C.TransWsId AND "
				+ "A.TransTime = C.TransTime ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransTime())));
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryFunctionTransaction - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryFunctionTransaction - SQL - End");
			while (lrsQry.next())
			{
				InventoryFunctionTransactionData laInventoryFunctionTransactionData =
					new InventoryFunctionTransactionData();
				laInventoryFunctionTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryFunctionTransactionData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laInventoryFunctionTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryFunctionTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laInventoryFunctionTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laInventoryFunctionTransactionData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laInventoryFunctionTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laInventoryFunctionTransactionData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laInventoryFunctionTransactionData.setEmpId(
					caDA.getStringFromDB(lrsQry, "EmpId"));
				laInventoryFunctionTransactionData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				laInventoryFunctionTransactionData.setSubconIssueDate(
					caDA.getIntFromDB(lrsQry, "SubconIssueDate"));
				laInventoryFunctionTransactionData.setInvcNo(
					caDA.getStringFromDB(lrsQry, "InvcNo"));
				laInventoryFunctionTransactionData.setInvcCorrIndi(
					caDA.getIntFromDB(lrsQry, "InvcCorrIndi"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryFunctionTransactionData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryFunctionTransaction - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryFunctionTransaction - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Query RTS.RTS_INV_FUNC_TRANS for a given Invoice Number
	 * RTS I - INVFUNS4
	 * 
	 * @param  aaGeneralSearchData GeneralSearchData
	 * @return Vector 	
	 */
	public int qryInventoryFunctionTransactionInvoice(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryFunctionTransactionInvoice - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		int liInvcCount = 0;

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "COUNT(*) as InvcCount "
				+ "FROM RTS.RTS_INV_FUNC_TRANS "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "InvcNo = ? ");
		try
		{
			// OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
			// SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
			// InvcNo
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getKey1())));

			Log.write(
				Log.SQL,
				this,
				" - qryInventoryFunctionTransactionInvoice - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryFunctionTransactionInvoice - SQL - End");

			while (lrsQry.next())
			{
				liInvcCount = caDA.getIntFromDB(lrsQry, "InvcCount");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryFunctionTransactionInvoice - End ");
			return (liInvcCount);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryFunctionTransactionInvoice - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_INV_FUNC_TRANS for voided transaction
	 * 
	 * @param  aaInventoryFunctionTransactionData InventoryFunctionTransactionData	
	 * @throws RTSException 
	 */
	public void voidInventoryFunctionTransaction(InventoryFunctionTransactionData aaInventoryFunctionTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"voidInventoryFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_INV_FUNC_TRANS SET "
				+ "VoidedTransIndi = 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? AND "
				+ "TransTime = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryFunctionTransactionData
							.getTransTime())));

			Log.write(
				Log.SQL,
				this,
				"voidInventoryFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"voidInventoryFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"voidInventoryFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"voidInventoryFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
