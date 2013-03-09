package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.SubstationSummaryData;
import com.txdot.isd.rts.services.data.TransactionHeaderData;
import com.txdot.isd.rts.services.data.VoidTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * TransactionHeader.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/21/2001  Created	
 * K Harrell   	10/16/2001	Added qryIncompleteTransactionHeader for 
 *             	            Complete Vehicle Transaction
 * K Harrell   	10/17/2001  Added qryIncompleteTransactionCount for 
 *							CloseOut
 * K Harrell   	10/23/2001  Added qryTransactionHeaderForVoid
 * K Harrell   	12/11/2001  Removed BranchOfcId
 * K Harrell   	12/13/2001  Added updIncompleteTransactionHeader
 * K Harrell   	12/17/2001  Removed OfcIssuanceNo and SubstaId from 
 *							purge(s)
 *                          Altered qryIncomplete.. to check only for 
 *							current date  
 * R Hicks 		07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell   	10/11/2002  Altered purge3TransactionHeader to include 
 *							qualifiers of Ofcissuanceno/Substaid
 *                          defect 4749
 * K Harrell  	10/18/2002  Substation Summary Performance
 *							defect 4912 
 * K Harrell  	12/11/2002  Consolidated purge1,2,3 to purge
 *							defect 5161 
 * K. Harrell  	01/24/2003  Removed debug comments
 * Min Wang    	07/22/2003  When updTransTimeTransactionHeader is not 
 *							updating a row, 
 *                         	ThrowRTSException.DB_ERROR.
 *                         	Modified updTransTimeTransactionHeader().
 *                         	Defect 6328
 * K Harrell	12/31/2003	Do not qualify on TransAMDate for querying 
 *							for incomplete trans
 *							for both "Complete Vehicle Transaction" or 
 *							"CloseOut"
 *							modify qryIncompleteTransactionCount(),
 *							qryIncompleteTransactionHeader()
 *							defect 6729 Ver 5.1.5 Fix 2
 * K Harrell	12/31/2003	Prefix error for updTransTimeTransactionHeader 
 *							updTransTimeTransactionHeader()
 *							defect 6772 Ver 5.1.5 Fix 2
 * K Harrell	11/23/2004	Alter delete stmt to only regard TransAMDate
 *							vs. retaining the trans headers for the
 *							max date. 
 *							modify purgeTransactionHeader()
 *							defect 7684  Ver 5.2.2
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	08/19/2005	Add method to insert TransactionHeader 
 * 							record for IADDR & IRNR
 * 							add insItrntBackOfcTransHdr() 
 * 							defect 8285  Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. TransactionHeaderData
 * 							Object
 * 							modify purgeTransactionHeader() 
 * 							defect 8423 Ver 5.2.3  
 * K Harrell	10/20/2006	Fixed insert into RTS.RTS_TRANS_HDR 
 * Jeff S.					for IRNR/IADDR
 * 							modify insItrntBackOfcTransHdr()
 * 							defect 8989 Ver Fall_Admin_Tables
 * K Harell		06/26/2007	Do not update transactions in batch if from 
 * 							current date.
 * 							modify updIncompleteTransactionHeader()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/27/2008	Assign TransEmpId to "LGNERR" if null
 * 							on insert. 
 * 							modify insTransactionHeader()
 * 							defect 9847 Ver Defect_POS_B   
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insTransactionHeader()    
 * 							defect 9847 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeTransactionHeader()
 * 							defect 9825 Ver Defect_POS_D    
 * K Harrell	08/27/2009	Add check for PrintImmediate = 2 
 * 							add updPrintImmediateIncomplTransHeader()
 * 							delete updtIncompleteTransactionHeader()
 * 							defect 10184 Ver Defect_POS_F
 * K Harrell	03/19/2010  delete unused method.
 * 							delete complTransactionHeader() 
 * 							defect 10239 Ver POS_640 
 * K Harrell 	06/14/2010 	add CustNoKey to insert
 * 							modify insTransactionHeader(), 
 * 							 insItrntBackOfcTransHdr()
 * 							defect 10505 Ver 6.5.0   
 * K Harrell	01/24/2011	add csMethod 
 * 							add getNextWebAgntCustSeqNo(),
 * 							 insNextWebAgntTransHdr() 
 * 							defect 10734 Ver 6.7.0         
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with the database.
 *	(RTS_TRANS_HDR)  
 *
 * @version	6.7.0  				01/24/2011
 * @author	Kathy Harrell
 * <br>Creation Date:			09/21/2001
 */

public class TransactionHeader extends TransactionHeaderData
{
	// defect 10734 
	private String csMethod = new String();
	// end defect 10734 
	
	DatabaseAccess caDA;

	/**
	 * TransactionHeader constructor comment.
	 *
	 * @param  aaDA
	 * @throws RTSException 
	 */
	public TransactionHeader(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_TRANS_HDR
	 *
	 * @param  aaTransactionHeaderData	TransactionHeaderData
	 * @throws RTSException 
	 */
	public void delTransactionHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delTransactionHeader - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_TRANS_HDR "
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
						aaTransactionHeaderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getCustSeqNo())));

			Log.write(
				Log.SQL,
				this,
				"delTransactionHeader - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delTransactionHeader - SQL - End");
			Log.write(Log.METHOD, this, "delTransactionHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_TRANS_HDR
	 * 
	 * @param  aaTransactionHeaderData	TransactionHeaderData
	 * @throws RTSException 
	 */
	public void insTransactionHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insTransactionHeader - Begin");

		Vector lvValues = new Vector();

		// defect 9847 
		if (aaTransactionHeaderData.getTransEmpId() == null
			|| aaTransactionHeaderData.getTransEmpId().trim().length()
				== 0)
		{
			aaTransactionHeaderData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		// defect 10505 
		// add CustNoKey 
		String lsIns =
			"INSERT into RTS.RTS_TRANS_HDR ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "CustNoKey,"
				+ "OfcIssuanceCd,"
				+ "VersionCd,"
				+ "TransEmpId,"
				+ "CashWsId,"
				+ "TransTimestmp,"
				+ "FeeSourceCd,"
				+ "PrintImmediate,"
				+ "TransName ) VALUES ( "
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
						aaTransactionHeaderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getCustSeqNo())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaTransactionHeaderData.getCustNoKey()));
			// end defect 10505 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getOfcIssuanceCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getVersionCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getCashWsId())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransTimestmp())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getFeeSourceCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getPrintImmediate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransName())));
			Log.write(
				Log.SQL,
				this,
				"insTransactionHeader - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insTransactionHeader - SQL - End");
			Log.write(Log.METHOD, this, "insTransactionHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Insert into RTS.RTS_TRANS_HDR for IRNR/IADDR
	 * 
	 * @param  aaTranHdrData TransactionHeaderData
	 * @return int	
	 * @throws RTSException 
	 */
	public int insItrntBackOfcTransHdr(TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insItrntBackOfcTransHdr - Begin");

		Vector lvValues = new Vector();

		int liNumRows = 0;

		// defect 10505 
		// add CustNoKey 
		// defect 8989 
		// Build String to concatenate vs. pass parameters
		String lsInsert =
			aaTransHdrData.getOfcIssuanceNo()
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getSubstaId()
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getTransAMDate()
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getTransWsId()
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getCustSeqNo()
				+ CommonConstant.STR_COMMA
				+ CommonConstant.STR_SINGLE_QUOTE
				+ aaTransHdrData.getCustNoKey()
				+ CommonConstant.STR_SINGLE_QUOTE
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getOfcIssuanceCd()
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getVersionCd()
				+ CommonConstant.STR_COMMA
				+ CommonConstant.STR_SINGLE_QUOTE
				+ aaTransHdrData.getTransEmpId()
				+ CommonConstant.STR_SINGLE_QUOTE
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getCashWsId()
				+ CommonConstant.STR_COMMA
				+ CommonConstant.STR_SINGLE_QUOTE
				+ aaTransHdrData.getTransTimestmp().getDB2Date()
				+ CommonConstant.STR_SINGLE_QUOTE
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getFeeSourceCd()
				+ CommonConstant.STR_COMMA
				+ aaTransHdrData.getPrintImmediate()
				+ CommonConstant.STR_COMMA
				+ CommonConstant.STR_SINGLE_QUOTE
				+ aaTransHdrData.getTransName()
				+ CommonConstant.STR_SINGLE_QUOTE;

		String lsIns =
			"INSERT into RTS.RTS_TRANS_HDR ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "CustNoKey,"
				+ "OfcIssuanceCd,"
				+ "VersionCd,"
				+ "TransEmpId,"
				+ "CashWsId,"
				+ "TransTimestmp,"
				+ "FeeSourceCd,"
				+ "PrintImmediate,"
				+ "TransName ) "
				+ "SELECT "
				+ lsInsert
				+ " from RTS.RTS_OFFICE_IDS WHERE "
				+ " OFCISSUANCENO = ? "
				+ " AND NOT EXISTS ("
				+ " SELECT * FROM RTS.RTS_TRANS_HDR WHERE "
				+ " OFCISSUANCENO = ? AND "
				+ " SUBSTAID = ?  AND "
				+ " TRANSAMDATE = ? AND "
				+ " TRANSWSID = ? AND "
				+ " CUSTSEQNO = ? )";
		// end defect 8989 
		// end defect 10505 

		try
		{

			// Not Exists
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransHdrData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransHdrData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransHdrData.getSubstaId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransHdrData.getTransAMDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransHdrData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransHdrData.getCustSeqNo())));
			Log.write(
				Log.SQL,
				this,
				"insItrntBackOfcTransHdr - SQL - Begin");

			liNumRows =
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insItrntBackOfcTransHdr - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insItrntBackOfcTransHdr - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insItrntBackOfcTransHdr - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			return liNumRows;
		}
	} //END OF INSERT METHOD

	/** 
	 * Method to determine the next CustSeqNo for Web Agent Batch 
	 * Processing 
	 * 
	 * @param  laTransHdrData
	 * @return int 
	 * @throws RTSException
	 */
	public int getNextWebAgntCustSeqNo(TransactionHeaderData laTransHdrData)
		throws RTSException
	{
		csMethod = "getNextWebAgntCustSeqNo";
		int liCustSeqNo = Integer.MIN_VALUE;
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		String lsQry =
			"Select coalesce(max(custseqno),"
				+ CommonConstant.MAX_POS_CUSTSEQNO
				+ ") as CustSeqNo from "
				+ " RTS.RTS_TRANS_HDR where "
				+ " OfcissuanceNo = ? and "
				+ " TransWsId = ? and "
				+ " TransAMDate = ? "
				+ " and CustSeqNo >"
				+ CommonConstant.MAX_POS_CUSTSEQNO;

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					laTransHdrData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					laTransHdrData.getTransWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					laTransHdrData.getTransAMDate())));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			while (lrsQry.next())
			{
				liCustSeqNo = caDA.getIntFromDB(lrsQry, "CustSeqNo");
			}
			lrsQry.close();
			return liCustSeqNo;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

	}

	/**
	 * Method to Delete from RTS.RTS_TRANS_HDR
	 * 
	 * @param  aiPurgeDays int
	 * @return int  
	 * @throws RTSException 
	 */
	// defect 5161; One purge vs 3 for TransHdr 
	public int purgeTransactionHeader(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeTransactionHeader - Begin");
		Vector lvValues = new Vector();
		String lsDel =
			" DELETE FROM RTS.RTS_TRANS_HDR "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		// defect 8423
		// Used passed int 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeAMDate)));
		// end defect 8423		
		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeTransactionHeader - SQL - Begin");
			// defect 9825 
			// Return number of rows purged
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeTransactionHeader - SQL - End");
			Log.write(Log.METHOD, this, "purgeTransactionHeader - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			BatchLog.write(
				"purgeTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/** 
	 * Insert Next WebAgent Transaction Header 
	 * 
	 * @param aaTransHdrData
	 * @return TransactionHeaderData 
	 */
	public TransactionHeaderData insNextWebAgntTransHdr(TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		csMethod = "insNextWebAgntTransHdr";
		Vector lvValues = new Vector();

		// Get Next CustSeqNo 
		int liNextSeqNo = getNextWebAgntCustSeqNo(aaTransHdrData) + 1;

		aaTransHdrData.setCustSeqNo(liNextSeqNo);

		String lsIns =
			"INSERT into RTS.RTS_TRANS_HDR ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "CustNoKey,"
				+ "OfcIssuanceCd,"
				+ "VersionCd,"
				+ "TransEmpId,"
				+ "CashWsId,"
				+ "FeeSourceCd,"
				+ "PrintImmediate,"
				+ "TransName ) VALUES ( "
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

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getTransAMDate())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getTransWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getCustSeqNo())));
		lvValues.addElement(
			new DBValue(Types.CHAR, aaTransHdrData.getCustNoKey()));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getOfcIssuanceCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getVersionCd())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaTransHdrData.getTransEmpId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getCashWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getFeeSourceCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransHdrData.getPrintImmediate())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaTransHdrData.getTransName())));
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, "insTransactionHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		return aaTransHdrData;
	} //END OF INSERT METHOD

	/**
	 * Method to Select Count Incomplete RTS.RTS_TRANS_HDR for CashWsId
	 * 
	 * @param  aaGeneralSearchData  GeneralSearchData
	 * @return int
	 * @throws RTSException 
	 */
	public int qryIncompleteTransactionCount(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryIncompleteTransactionCount - Begin");

		StringBuffer lsQry = new StringBuffer();

		int liIncomplTransCount = 0;

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 6729:  Do not qualify on TransAMDate  
		lsQry
			.append(
				" SELECT Count(*) as IncomplTransCount "
				+ " FROM "
				+ " RTS.RTS_TRANS_HDR "
				+ " WHERE  "
				+ " OFCISSUANCENO= ? AND  " //1
		+" SUBSTAID = ? AND " //2
		+" CASHWSID = ? AND " //3
		+" TRANSTIMESTMP IS NULL ");
		// +" DATE(TransAmDate + 693596) = CURRENT DATE " + " AND TRANSTIMESTMP IS NULL ");
		// end defect 6729

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey3())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryIncompleteTransactionCount - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryIncompleteTransactionCount - SQL - End");
			while (lrsQry.next())
			{
				liIncomplTransCount =
					caDA.getIntFromDB(lrsQry, "IncomplTransCount");
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryIncompleteTransactionCount - End ");
			return (liIncomplTransCount);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIncompleteTransactionCount - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * "Complete Vehicle Transaction"
	 * Method to Select Incomplete RTS.RTS_TRANS_HDR
	 *
	 * @param  aaTransactionHeaderData	TransactionHeaderData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryIncompleteTransactionHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryIncompleteTransactionHeader - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 6729:  Do not qualify on TransAMDate  
		lsQry
			.append(
				" SELECT OFCISSUANCENO, "
				+ " SUBSTAID, "
				+ " TRANSAMDATE,  "
				+ " TRANSWSID,  "
				+ " CUSTSEQNO, "
				+ " OFCISSUANCECD,  "
				+ " VERSIONCD, "
				+ " TRANSEMPID,  "
				+ " CASHWSID,  "
				+ " FEESOURCECD, "
				+ " PRINTIMMEDIATE, "
				+ " TRANSNAME, "
				+ " TRANSTIMESTMP "
				+ " FROM "
				+ " RTS.RTS_TRANS_HDR "
				+ " WHERE  TransTimestmp is null and "
				+ " OFCISSUANCENO= ? AND  " //1
		+" SUBSTAID = ? AND " //2
		+" TRANSWSID = ? " //3
		// +" and DATE(TransAmDate + 693596) = CURRENT DATE "
		+" order by OfcIssuanceNo,SubstaId,TransAMDate,CustSeqNo");
		// end defect 6729 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransactionHeaderData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransactionHeaderData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransactionHeaderData.getTransWsId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryIncompleteTransactionHeader - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryIncompleteTransactionHeader - SQL - End");

			while (lrsQry.next())
			{
				TransactionHeaderData laTransactionHeaderData =
					new TransactionHeaderData();
				laTransactionHeaderData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionHeaderData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionHeaderData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionHeaderData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionHeaderData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionHeaderData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laTransactionHeaderData.setVersionCd(
					caDA.getIntFromDB(lrsQry, "VersionCd"));
				laTransactionHeaderData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laTransactionHeaderData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laTransactionHeaderData.setFeeSourceCd(
					caDA.getIntFromDB(lrsQry, "FeeSourceCd"));
				laTransactionHeaderData.setPrintImmediate(
					caDA.getIntFromDB(lrsQry, "PrintImmediate"));
				laTransactionHeaderData.setTransName(
					caDA.getStringFromDB(lrsQry, "TransName"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionHeaderData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryIncompleteTransactionHeader - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIncompleteTransactionHeader - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_TRANS_HDR
	 * 
	 * @param  aaTransactionHeaderData	TransactionHeaderData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTransactionHeader - Begin");

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
				+ "OfcIssuanceCd,"
				+ "VersionCd,"
				+ "TransEmpId,"
				+ "CashWsId,"
				+ "TransTimestmp,"
				+ "FeeSourceCd,"
				+ "PrintImmediate,"
				+ "TransName "
				+ "FROM RTS.RTS_TRANS_HDR "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getCustSeqNo())));
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionHeader - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionHeader - SQL - End");

			while (lrsQry.next())
			{
				TransactionHeaderData laTransactionHeaderData =
					new TransactionHeaderData();
				laTransactionHeaderData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionHeaderData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionHeaderData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionHeaderData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionHeaderData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionHeaderData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laTransactionHeaderData.setVersionCd(
					caDA.getIntFromDB(lrsQry, "VersionCd"));
				laTransactionHeaderData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laTransactionHeaderData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laTransactionHeaderData.setTransTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "TransTimestmp"));
				laTransactionHeaderData.setFeeSourceCd(
					caDA.getIntFromDB(lrsQry, "FeeSourceCd"));
				laTransactionHeaderData.setPrintImmediate(
					caDA.getIntFromDB(lrsQry, "PrintImmediate"));
				laTransactionHeaderData.setTransName(
					caDA.getStringFromDB(lrsQry, "TransName"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionHeaderData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionHeader - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionHeader - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_TRANS_HDR
	 * 
	 * @param  aaVoidTransactionData  VoidTransactionData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTransactionHeaderForVoid(VoidTransactionData aaVoidTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionHeaderForVoid - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			" SELECT OFCISSUANCENO, SUBSTAID,TRANSAMDATE, TRANSWSID, CUSTSEQNO "
				+ " FROM RTS.RTS_TRANS_HDR A "
				+ " WHERE TRANSTIMESTMP IS NOT NULL AND EXISTS "
				+ " (SELECT * FROM RTS.RTS_TRANS  B WHERE  "
				+ " TRANSCD NOT IN ('EFTFND','FNDREM') AND "
				+ " OFCISSUANCENO = ? AND  "
				+ " SUBSTAID = ? AND  "
				+ " TRANSAMDATE = ? AND  "
				+ " TRANSWSID = ? AND  "
				+ " TRANSTIME = ? AND   "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND  "
				+ " A.SUBSTAID      = B.SUBSTAID AND  "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.CUSTSEQNO = B.CUSTSEQNO) ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaVoidTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaVoidTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaVoidTransactionData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaVoidTransactionData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaVoidTransactionData.getTransTime())));

			Log.write(
				Log.SQL,
				this,
				" - qryTransactionHeaderForVoid - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionHeaderForVoid - SQL - End");

			while (lrsQry.next())
			{
				VoidTransactionData laVoidTransactionData =
					new VoidTransactionData();
				laVoidTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laVoidTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laVoidTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laVoidTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laVoidTransactionData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				// Add element to the Vector
				lvRslt.addElement(laVoidTransactionData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionHeaderForVoid - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionHeaderForVoid - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_TRANS_HDR Transtimestmp for Print Immediate
	 *
	 * @param  aaTransactionHeaderData	TransactionHeaderData
	 * @throws RTSException 
	 */
	public void updPrintImmediateIncomplTransHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updPrintImmediateIncomplTransHeader - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_TRANS_HDR SET TransTimestmp = Current Timestamp "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAmDate < ? AND "
				+ "PrintImmediate in (1,2) and Transtimestmp is null";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getSubstaId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransAMDate())));

			Log.write(
				Log.SQL,
				this,
				"updPrintImmediateIncomplTransHeader - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updPrintImmediateIncomplTransHeader - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updPrintImmediateIncomplTransHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updPrintImmediateIncomplTransHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Method to update RTS.RTS_TRANS_HDR w/ SummaryEffDate
	 *
	 * @param  aaSubstationSummaryData SubstationSummaryData
	 * @throws RTSException
	 */
	public void updSummaryEffDateTransactionHeader(SubstationSummaryData aaSubstationSummaryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updSummaryEffDateTransactionHeader - Begin");

		Vector lvValues = new Vector();

		// KPH (2002.10.18): CQU10004912 Substation Summary Performance
		String csSummaryEffDate =
			Integer.toString(
				aaSubstationSummaryData.getSummaryEffDate());

			String lsUpd1 =
				"UPDATE RTS.RTS_TRANS_HDR A SET SummaryEffDate = null "
					+ "WHERE "
					+ "A.OfcIssuanceNo = ? AND " // 1
		+"A.SubstaId = ? AND " // 2
	+"A.SummaryEffDate = ? "; // 3 
			String lsUpd2 =
				"UPDATE RTS.RTS_TRANS_HDR A SET SummaryEffDate = "
					+ csSummaryEffDate
					+ " WHERE "
					+ " A.OfcIssuanceNo = ? AND " // 4
		+" A.SubstaId = ? AND " // 5
		+" A.TRANSTIMESTMP >= "
			+ "  (SELECT MIN(CLOSEOUTBEGTSTMP) "
			+ "   FROM RTS.RTS_CLOSEOUT_HSTRY K "
			+ "   WHERE "
			+ "   A.OFCISSUANCENO = K.OFCISSUANCENO AND  "
			+ "   A.SubstaId = K.SubstaId AND  "
			+ "   A.CASHWSID = K.CASHWSID AND "
			+ "   K.SUMMARYEFFDATE = ? ) AND " //6
	+"   A.TRANSTIMESTMP <= "
		+ "  (SELECT MAX(CLOSEOUTENDTSTMP) "
		+ "   FROM RTS.RTS_CLOSEOUT_HSTRY K "
		+ "   WHERE "
		+ "   A.OFCISSUANCENO = K.OFCISSUANCENO AND  "
		+ "   A.SubstaId = K.SubstaId AND  "
		+ "   A.CASHWSID = K.CASHWSID AND "
		+ "   K.SUMMARYEFFDATE =  ? )";
		//7

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getSubstaId())));
			// 3 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				"updSummaryEffDateTransactionHeader (to Null) - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd1, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updSummaryEffDateTransactionHeader (to Null) - SQL - End");
			lvValues = new Vector();
			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getOfcIssuanceNo())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getSubstaId())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getSummaryEffDate())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSubstationSummaryData.getSummaryEffDate())));
			Log.write(
				Log.SQL,
				this,
				"updSummaryEffDateTransactionHeader (to SummaryEffDate) - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd2, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updSummaryEffDateTransactionHeader (to SummaryEffDate) - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updSummaryEffDateTransactionHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updSummaryEffDateTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Method to update RTS.RTS_TRANS_HDR w/ TransName
	 * 
	 * @param  aaTransactionHeaderData	TransactionHeaderData
	 * @throws RTSException 
	 */
	public void updTransactionHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updTransactionHeader - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_TRANS_HDR SET TransName = ? "
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
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransName())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getCustSeqNo())));

			Log.write(
				Log.SQL,
				this,
				"updTransactionHeader - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updTransactionHeader - SQL - End");
			Log.write(Log.METHOD, this, "updTransactionHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Method to update a TransactionHeader w/ TransTimestmp
	 * 
	 * @param  aaTransactionHeaderData  TransactionHeaderData	
	 * @throws RTSException  
	 */
	public void updTransTimeTransactionHeader(TransactionHeaderData aaTransactionHeaderData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updTransTimeTransactionHeader - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_TRANS_HDR SET TransTimestmp = ? "
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
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransTimestmp())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionHeaderData.getCustSeqNo())));

			Log.write(
				Log.SQL,
				this,
				"updTransTimeTransactionHeader - SQL - Begin");
			// defect 6328
			// caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			int liRC =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			if (liRC < 1)
			{
				RTSException rtse =
					new RTSException(RTSException.DB_ERROR);
				// defect 6772. 
				// Added prefix to DetailMsg "RTS II Application Error."
				rtse.setDetailMsg(
					"RTS II Application Error. No row found to update in TransHeader.");
				// end defect 6772 
				throw rtse;
			}
			// end defect 6328
			Log.write(
				Log.SQL,
				this,
				"updTransTimeTransactionHeader - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updTransTimeTransactionHeader - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updTransTimeTransactionHeader - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS