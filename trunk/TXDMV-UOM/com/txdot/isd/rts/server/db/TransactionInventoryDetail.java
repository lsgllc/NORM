package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ReprintData;
import com.txdot.isd.rts.services.data.TransactionInventoryDetailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.StickerPrintingUtilities;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * TransactionInventoryDetail.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/19/2001  Added purgeTransactionFundsDetail
 * K Harrell   	11/11/2001  Removed OfcIssuanceNo, SubstaId from Purge
 * K Harrell   	01/10/2002  Updated updTransactionInventoryDetail
 * R Hicks 		07/12/2002 	Add call to closeLastStatement() after a 
 *							query
 * K Harrell   	09/27/2002 	Altered updTransactionInventoryDetailForCache
 *							to include invlocidcd = X'
 * K Harrell	01/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add qryAllReprintStickerDaily, 
 *							qryReprintStickerDaily *2
 * 							modify insTransactionInventoryDetail
 * 							Ver 5.2.0
 * K Harrell	05/19/2004	Modify query to determine reprint for IAR
 *							create qryReprintStickerDaily(ReprintData,
 *							RTSDate)
 *							deprecated qryReprintStickerDaily(ReprintData,
 *							RTSDate,RTSDate) 
 *							defect 7018  Ver 5.2.0
 * K Harrell	05/19/2004	RTS_REPRNT_STKR now updated online
 *							deprecate qryAllReprintStickerDaily(RTSDate,
 *							RTSDate)
 *							defect 7020  Ver 5.2.0
 * K Harrell	08/18/2004  Update SQL for RTS_REPRNT_STKR to pull for
 *							SBRNW & DTA
 *							qryAllReprintStickerDaily()
 *							defect   Ver 5.2.1
 * K Harrell	10/10/2004	Modified to match SubconRenewalData change
 *							Vin=> VIN. Removed commented out elements of
 *							qryReprintStickerDaily()
 *							Comment cleanup in qry.. methods
 *							defect   Ver 5.2.1
 * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate
 *							modify purgeTransactionInventoryDetail()
 *							defect 7684  Ver 5.2.2
 * K Harrell	01/07/2005	Streamline SQL for IAR Part J
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							delete deprecated qryAllReprintStickerDaily
 *							(RTSDate,RTSDate) and
 *							qryAllReprintStickerDaily(ReprintData,
 *							RTSDate,RTSDate)  
 *							modify qryReprintStickerDaily(ReprintData,
 *							RTSDate)
 *							defect 7874 Ver 5.2.2
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							TransactionInventoryDetailData Object
 * 							modify purgeTransactionInventoryDetail() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeTransactionInventoryDetail()
 * 							defect 9825 Ver Defect_POS_D    
 * K Harrell	03/19/2010	delete unused method.
 * 							delete updTransactionInventoryDetail() 
 * 							defect 10239 Ver POS_640  
 * K Harrell	06/14/2010	add TransId to insert
 * 							modify insTransactionInventoryDetail()
 * 							defect 10505 Ver 6.5.0    
 * K Harrell	07/14/2010	modify qryReprintStickerDaily() 
 * 							defect 10491 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with the database.
 *  (RTS_TR_INV_DETAIL)
 *  
 * @version	6.5.0			07/14/2010
 * @author 	Kathy Harrell
 * <br>Creation Date:		09/21/2001
 */
public class TransactionInventoryDetail
	extends TransactionInventoryDetailData
{
	DatabaseAccess caDA;

	/**
	 * TransactionInventoryDetail constructor comment.
	 *
	 * @param aaDA
	 * @throws RTSException 
	 */

	public TransactionInventoryDetail(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to DELETE from RTS.RTS_TR_INV_DETAIL given:
	 *   OfcIssuanceno, SubStaId,TransAMDate, TransWsId, CustSeqNo
	 * Used when cancel transaction set.
	 * 
	 * @param  aaTransactionInventoryDetailData TransactionInventoryDetailData	
	 * @throws RTSException 
	 */
	public void delTransactionInventoryDetail(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delTransactionInventoryDetail - Begin");

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// SQL Delete Statement 
		String lsDel =
			"DELETE FROM RTS.RTS_TR_INV_DETAIL  "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getCustSeqNo())));
			if (aaTransactionInventoryDetailData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionInventoryDetailData
								.getTransTime())));
			}
			Log.write(
				Log.SQL,
				this,
				"delTransactionInventoryDetail - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delTransactionInventoryDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"delTransactionInventoryDetail - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delTransactionInventoryDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to INSERT into RTS.RTS_TR_INV_DETAIL for transaction add.
	 *
	 * @param  aaTransactionInventoryDetailData
	 * @throws RTSException 
	 */
	public void insTransactionInventoryDetail(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insTransactionInventoryDetail - Begin");

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// defect 10505 
		// SQL Insert Statement
		String lsIns =
			"INSERT into RTS.RTS_TR_INV_DETAIL  ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvEndNo,"
				+ "InvLocIdCd,"
				+ "InvId,"
				+ "InvQty,"
				+ "InvItmReorderLvl,"
				+ "OfcInvReorderQty,"
				+ "InvItmTrckngOfc,"
				+ "DelInvReasnCd,"
				+ "DetailStatusCd,"
				+ "IssueMisMatchIndi,"
				+ "DelInvReasnTxt, "
				+ "PrntInvQty ) "
				+ " VALUES ( "
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
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?)";
		//end defect 10505 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransCd())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransId())));
			// end defect 10505 

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvEndNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getInvId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getInvQty())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmReorderLvl())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcInvReorderQty())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmTrckngOfc())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getDelInvReasnCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getDetailStatusCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getIssueMisMatchIndi())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getDelInvReasnTxt())));
			// PCR 34 
			// Determine liPrntInvQty for insertion
			int liPrntInvQty =
				aaTransactionInventoryDetailData.getReprntCount();

			if (liPrntInvQty == 0)
			{
				liPrntInvQty = 1;
			}
			if (liPrntInvQty == -1)
			{
				liPrntInvQty = 0;
			}

			// defect 10491 
			String lsTransCd =
				aaTransactionInventoryDetailData.getTransCd();

			boolean lbPrntPrmt = UtilityMethods.printsPermit(lsTransCd);

			if (!lbPrntPrmt
				&& !StickerPrintingUtilities.isStickerPrintable(
					aaTransactionInventoryDetailData.getItmCd()))
			{
				// end defect 10491 
				liPrntInvQty = 0;
			}
			// Assign calculated PrintInvQty	
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liPrntInvQty)));
			// End PCR 34 

			Log.write(
				Log.SQL,
				this,
				"insTransactionInventoryDetail - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insTransactionInventoryDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insTransactionInventoryDetail - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insTransactionInventoryDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	 * Method to DELETE from RTS.RTS_TR_INV_DETAIL for Purge
	 *  
	 * @param  aiPurgeAMDate int
	 * @return int  		
	 * @throws RTSException
	 */
	public int purgeTransactionInventoryDetail(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeTransactionInventoryDetail - Begin");

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// SQL Delete Statement 
		String lsDel =
			" DELETE FROM RTS.RTS_TR_INV_DETAIL "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			// defect 8423
			// used passed int 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423

			Log.write(
				Log.SQL,
				this,
				"purgeTransactionInventoryDetail - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeTransactionInventoryDetail - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeTransactionInventoryDetail - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeTransactionInventoryDetail - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to SELECT from RTS_TR_INV_DETAIL for reprinted inventory 
	 * from InventoryActionReportSQL.qryIARPartJ() 
	 * 
	 * @param  aaReprintStickerData ReprintData
	 * @param  aaIARDate RTSDate
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryReprintStickerDaily(
		ReprintData aaReprintStickerData,
		RTSDate aaIARDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "qryReprintStickerDaily - Begin");

		// SQL Select Statement 
		StringBuffer lsQry = new StringBuffer();

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// Vector of Returned data
		Vector lvRslt = new Vector();

		// Object for maintaining cursor positioning in returned data
		ResultSet lrsQry = null;
		// defect 10491 
		// add InvItmNo 
		// defect 7874 
		lsQry.append("SELECT ");
		lsQry.append("B.OFCISSUANCENO,");
		lsQry.append("B.TRANSAMDATE,");
		lsQry.append("B.TRANSWSID,");
		lsQry.append("B.TRANSTIME,");
		lsQry.append("A.TRANSEMPID,");
		lsQry.append("C.ITMCD,");
		lsQry.append("C.INVITMNO,");
		lsQry.append("C.INVITMYR, ");
		lsQry.append("COUNT(*) as PrntInvQty,");
		lsQry.append("B.VIN FROM ");
		lsQry.append("RTS.RTS_TRANS A, "); // RPRSTK Trans 
		lsQry.append("RTS.RTS_TRANS B, "); // Original Trans 
		lsQry.append("RTS.RTS_TR_INV_DETAIL C ");
		// Reprinted Inventory
		lsQry.append("WHERE ");
		lsQry.append("A.OFCISSUANCENO = ? AND ");
		lsQry.append("A.SUBSTAID = ? AND ");
		lsQry.append("A.TRANSAMDATE = ? AND ");
		// defect 10491
		// add RPRPMT  
		// lsQry.append("A.TRANSCD = 'RPRSTK' AND ");
		lsQry.append("A.TRANSCD in ('RPRSTK', 'RPRPRM') AND ");
		// end defect 10491 
		lsQry.append("A.OFCISSUANCENO = B.OFCISSUANCENO AND ");
		lsQry.append("A.SUBSTAID = B.SUBSTAID AND ");
		lsQry.append("A.TRANSAMDATE = B.TRANSAMDATE AND  ");
		lsQry.append("A.TRANSWSID   = B.TRANSWSID   AND  ");
		lsQry.append("B.OFCISSUANCENO = C.OFCISSUANCENO AND ");
		lsQry.append("B.SUBSTAID = C.SUBSTAID AND ");
		lsQry.append("B.TRANSWSID = C.TRANSWSID AND ");
		lsQry.append("B.TRANSAMDATE = C.TRANSAMDATE AND ");
		lsQry.append("B.TRANSTIME = C.TRANSTIME AND ");
		lsQry.append("B.CUSTSEQNO = C.CUSTSEQNO AND ");
		lsQry.append("A.RPRSTKOFCISSUANCENO = B.OFCISSUANCENO AND ");
		lsQry.append("A.RPRSTKTRANSAMDATE = B.TRANSAMDATE AND  ");
		lsQry.append("A.RPRSTKTRANSWSID   = B.TRANSWSID   AND  ");
		lsQry.append("A.RPRSTKTRANSTIME   = B.TRANSTIME   AND  ");
		lsQry.append("C.PRNTINVQTY > 1   ");
		lsQry.append(
			"GROUP BY B.OFCISSUANCENO,B.TRANSAMDATE,B.TRANSWSID,");
		lsQry.append(
			"B.TRANSTIME,A.TRANSEMPID,C.ITMCD,C.INVITMNO,C.INVITMYR,B.VIN ");
		// end defect 10491 
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintStickerData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaReprintStickerData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaIARDate.getAMDate())));
			Log.write(
				Log.SQL,
				this,
				"qryReprintStickerDaily - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				"qryReprintStickerDaily - SQL - End");
			while (lrsQry.next())
			{
				ReprintData laReprintData = new ReprintData();
				// Build Transaction Id for assignment 
				int liOfcIssuanceNo =
					(caDA.getIntFromDB(lrsQry, "OFCISSUANCENO"));
				int liTransWsId =
					(caDA.getIntFromDB(lrsQry, "TRANSWSID"));
				int liTransAMDate =
					(caDA.getIntFromDB(lrsQry, "TRANSAMDATE"));
				int liTransTime =
					(caDA.getIntFromDB(lrsQry, "TRANSTIME"));

				// defect 10491 
				String lsTransId =
					UtilityMethods.getTransId(
						liOfcIssuanceNo,
						liTransWsId,
						liTransAMDate,
						liTransTime);
				// end defect 10491 

				laReprintData.setTransId(lsTransId);
				laReprintData.setEmpId(
					caDA.getStringFromDB(lrsQry, "TRANSEMPID"));
				laReprintData.setWsId(
					caDA.getIntFromDB(lrsQry, "TRANSWSID"));
				laReprintData.setPrntQty(
					caDA.getIntFromDB(lrsQry, "PRNTINVQTY"));
				laReprintData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ITMCD"));

				// defect 10491 
				laReprintData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "INVITMNO"));
				// end defect 10491 

				laReprintData.setItmYr(
					caDA.getIntFromDB(lrsQry, "INVITMYR"));
				laReprintData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				lvRslt.add(laReprintData);
			}
			// end defect 7874 
			Log.write(Log.METHOD, this, "qryReprintStickerDaily - End");
			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryReprintStickerDaily - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			if (lrsQry != null)
			{
				try
				{
					lrsQry.close();
				}
				catch (Exception leException)
				{
				}
			}
			caDA.closeLastDBStatement();
			lrsQry = null;
		}
	}
	/**
	 * Method to SELECT from RTS.RTS_TR_INV_DETAIL given:
	 *   OfcIssuanceno, Subtaid,TransAMDate,TransWsid,Custseqno,TransTime
	 *
	 * @param  aaTransactionInventoryDetailData	TransactionInventoryDetailData	
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryTransactionInventoryDetail(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionInventoryDetail - Begin");

		// SQL Select Statement 
		StringBuffer lsQry = new StringBuffer();

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// Vector of Returned data
		Vector lvRslt = new Vector();

		// Object for maintaining cursor positioning in returned data
		ResultSet lrsQry = null;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvEndNo,"
				+ "InvLocIdCd,"
				+ "InvId,"
				+ "InvQty,"
				+ "InvItmReorderLvl,"
				+ "OfcInvReorderQty,"
				+ "InvItmTrckngOfc,"
				+ "DelInvReasnCd,"
				+ "DetailStatusCd,"
				+ "IssueMisMatchIndi,"
				+ "DelInvReasnTxt, "
				+ "PRNTINVQTY "
				+ "FROM RTS.RTS_TR_INV_DETAIL "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
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
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransTime())));
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionInventoryDetail - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionInventoryDetail - SQL - End");
			while (lrsQry.next())
			{
				TransactionInventoryDetailData laTransactionInventoryDetailData =
					new TransactionInventoryDetailData();
				laTransactionInventoryDetailData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionInventoryDetailData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionInventoryDetailData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionInventoryDetailData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionInventoryDetailData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionInventoryDetailData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionInventoryDetailData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionInventoryDetailData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laTransactionInventoryDetailData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laTransactionInventoryDetailData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laTransactionInventoryDetailData.setInvEndNo(
					caDA.getStringFromDB(lrsQry, "InvEndNo"));
				laTransactionInventoryDetailData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laTransactionInventoryDetailData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laTransactionInventoryDetailData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laTransactionInventoryDetailData.setInvItmReorderLvl(
					caDA.getIntFromDB(lrsQry, "InvItmReorderLvl"));
				laTransactionInventoryDetailData.setOfcInvReorderQty(
					caDA.getIntFromDB(lrsQry, "OfcInvReorderQty"));
				laTransactionInventoryDetailData.setInvItmTrckngOfc(
					caDA.getIntFromDB(lrsQry, "InvItmTrckngOfc"));
				laTransactionInventoryDetailData.setDelInvReasnCd(
					caDA.getIntFromDB(lrsQry, "DelInvReasnCd"));
				laTransactionInventoryDetailData.setDetailStatusCd(
					caDA.getIntFromDB(lrsQry, "DetailStatusCd"));
				laTransactionInventoryDetailData.setIssueMisMatchIndi(
					caDA.getIntFromDB(lrsQry, "IssueMisMatchIndi"));
				laTransactionInventoryDetailData.setDelInvReasnTxt(
					caDA.getStringFromDB(lrsQry, "DelInvReasnTxt"));
				// PCR 34 
				laTransactionInventoryDetailData.setReprntCount(
					caDA.getIntFromDB(lrsQry, "PRNTINVQTY"));
				// End PCR 34 
				// Add element to the Vector
				lvRslt.addElement(laTransactionInventoryDetailData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionInventoryDetail - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionInventoryDetail - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to SELECT from RTS.RTS_TR_INV_DETAIL given:
	 *   OfcIssuanceno, Subtaid,ItmCd, InvItmYr, InvItmNo, InvEndNo 
	 *
	 * @param  aaTransactionInventoryDetailData	TransactionInventoryDetailData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTransactionInventoryDetailForSpecificItem(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionInventoryDetailForSpecificItem - Begin");

		// SQL Select Statement 
		StringBuffer lsQry = new StringBuffer();

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// Vector of Returned data
		Vector lvRslt = new Vector();

		// Object for maintaining cursor positioning in returned data
		ResultSet lrsQry = null;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "A.ItmCd,"
				+ "B.ItmCdDesc,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvEndNo,"
				+ "InvLocIdCd,"
				+ "InvId,"
				+ "InvQty,"
				+ "InvItmReorderLvl,"
				+ "OfcInvReorderQty,"
				+ "InvItmTrckngOfc,"
				+ "DelInvReasnCd,"
				+ "DetailStatusCd,"
				+ "IssueMisMatchIndi,"
				+ "DelInvReasnTxt "
				+ "FROM RTS.RTS_TR_INV_DETAIL A, RTS.RTS_ITEM_CODES B "
				+ "WHERE "
				+ "A.OfcIssuanceNo = ? AND "
				+ "A.SubStaId = ? AND "
				+ "A.ItmCd = ? AND "
				+ "A.ItmCd = B.ItmCd AND "
				+ "InvItmYr = ? AND "
				+ "InvItmNo = ? AND "
				+ "InvEndNo = ? AND TransCd not in "
				+ "('INVRCV', 'INVOFC','INVALL')");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvEndNo())));
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionInventoryDetailForSpecificItem - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionInventoryDetailForSpecificItem - SQL - End");
			while (lrsQry.next())
			{
				TransactionInventoryDetailData laTransactionInventoryDetailData =
					new TransactionInventoryDetailData();
				laTransactionInventoryDetailData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionInventoryDetailData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionInventoryDetailData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionInventoryDetailData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionInventoryDetailData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionInventoryDetailData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionInventoryDetailData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionInventoryDetailData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laTransactionInventoryDetailData.setItmCdDesc(
					caDA.getStringFromDB(lrsQry, "ItmCdDesc"));
				laTransactionInventoryDetailData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laTransactionInventoryDetailData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laTransactionInventoryDetailData.setInvEndNo(
					caDA.getStringFromDB(lrsQry, "InvEndNo"));
				laTransactionInventoryDetailData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laTransactionInventoryDetailData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laTransactionInventoryDetailData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laTransactionInventoryDetailData.setInvItmReorderLvl(
					caDA.getIntFromDB(lrsQry, "InvItmReorderLvl"));
				laTransactionInventoryDetailData.setOfcInvReorderQty(
					caDA.getIntFromDB(lrsQry, "OfcInvReorderQty"));
				laTransactionInventoryDetailData.setInvItmTrckngOfc(
					caDA.getIntFromDB(lrsQry, "InvItmTrckngOfc"));
				laTransactionInventoryDetailData.setDelInvReasnCd(
					caDA.getIntFromDB(lrsQry, "DelInvReasnCd"));
				laTransactionInventoryDetailData.setDetailStatusCd(
					caDA.getIntFromDB(lrsQry, "DetailStatusCd"));
				laTransactionInventoryDetailData.setIssueMisMatchIndi(
					caDA.getIntFromDB(lrsQry, "IssueMisMatchIndi"));
				laTransactionInventoryDetailData.setDelInvReasnTxt(
					caDA.getStringFromDB(lrsQry, "DelInvReasnTxt"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionInventoryDetailData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionInventoryDetailForSpecificItem - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionInventoryDetailForSpecificItem - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to SELECT from RTS.RTS_TR_INV_DETAIL given:
	 *   OfcIssuanceno, Subtaid,TransAMDate,TransWsid,Custseqno
	 *
	 * @param  aaTransactionInventoryDetailData TransactionInventoryDetailData		
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTransactionInventoryDetailSet(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTransactionInventoryDetailSet - Begin");

		// SQL Select Statement 
		StringBuffer lsQry = new StringBuffer();

		// Vector of SQL Statement Parameters
		Vector lvValues = new Vector();

		// Vector of Returned data
		Vector lvRslt = new Vector();

		// Object for maintaining cursor positioning in returned data
		ResultSet lrsQry = null;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvEndNo,"
				+ "InvLocIdCd,"
				+ "InvId,"
				+ "InvQty,"
				+ "InvItmReorderLvl,"
				+ "OfcInvReorderQty,"
				+ "InvItmTrckngOfc,"
				+ "DelInvReasnCd,"
				+ "DetailStatusCd,"
				+ "IssueMisMatchIndi,"
				+ "DelInvReasnTxt "
				+ "FROM RTS.RTS_TR_INV_DETAIL "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getCustSeqNo())));
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionInventoryDetailSet - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionInventoryDetailSet - SQL - End");
			while (lrsQry.next())
			{
				TransactionInventoryDetailData laTransactionInventoryDetailData =
					new TransactionInventoryDetailData();
				laTransactionInventoryDetailData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionInventoryDetailData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionInventoryDetailData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionInventoryDetailData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionInventoryDetailData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionInventoryDetailData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionInventoryDetailData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionInventoryDetailData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laTransactionInventoryDetailData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laTransactionInventoryDetailData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laTransactionInventoryDetailData.setInvEndNo(
					caDA.getStringFromDB(lrsQry, "InvEndNo"));
				laTransactionInventoryDetailData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laTransactionInventoryDetailData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laTransactionInventoryDetailData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laTransactionInventoryDetailData.setInvItmReorderLvl(
					caDA.getIntFromDB(lrsQry, "InvItmReorderLvl"));
				laTransactionInventoryDetailData.setOfcInvReorderQty(
					caDA.getIntFromDB(lrsQry, "OfcInvReorderQty"));
				laTransactionInventoryDetailData.setInvItmTrckngOfc(
					caDA.getIntFromDB(lrsQry, "InvItmTrckngOfc"));
				laTransactionInventoryDetailData.setDelInvReasnCd(
					caDA.getIntFromDB(lrsQry, "DelInvReasnCd"));
				laTransactionInventoryDetailData.setDetailStatusCd(
					caDA.getIntFromDB(lrsQry, "DetailStatusCd"));
				laTransactionInventoryDetailData.setIssueMisMatchIndi(
					caDA.getIntFromDB(lrsQry, "IssueMisMatchIndi"));
				laTransactionInventoryDetailData.setDelInvReasnTxt(
					caDA.getStringFromDB(lrsQry, "DelInvReasnTxt"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionInventoryDetailData);
			} //End of While
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionInventoryDetailSet - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionInventoryDetailSet - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	// defect 10239 
	//	/**
	//	 * Method to UPDATE TransactionInventoryDetail InvLocIdCd, InvId given:
	//	 *   OfcIssuanceno, Subtaid,TransAMDate,TransWsid,Custseqno,TransTime,
	//	 *   ItmCd, InvItmYr, InvItmNo, InvEndNo 
	//	 * 
	//	 * @param  aaTransactionInventoryDetailData TransactionInventoryDetailData	
	//	 * @throws RTSException 
	//	 */
	//	public void updTransactionInventoryDetail(TransactionInventoryDetailData aaTransactionInventoryDetailData)
	//		throws RTSException
	//	{
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			"updTransactionInventoryDetail - Begin");
	//
	//		// Vector of SQL Statement Parameters
	//		Vector lvValues = new Vector();
	//
	//		// SQL Update Statement 
	//		String lsUpd =
	//			"UPDATE RTS.RTS_TR_INV_DETAIL  SET "
	//				+ "InvLocIdCd = ?, "
	//				+ "InvId = ? "
	//				+ "WHERE "
	//				+ "OfcIssuanceNo = ? AND "
	//				+ "SubStaId = ? AND "
	//				+ "TransAMDate = ? AND "
	//				+ "TransWsId = ? AND "
	//				+ "TransTime = ? AND "
	//				+ "ItmCd = ? AND "
	//				+ "InvItmYr = ? AND "
	//				+ "InvItmNo = ? AND "
	//				+ "InvEndNo = ? ";
	//		try
	//		{
	//			// SET 
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getInvLocIdCd())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData.getInvId())));
	//			// WHERE 
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getSubstaId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getTransAMDate())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getTransWsId())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getTransTime())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData.getItmCd())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getInvItmYr())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getInvItmNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					DatabaseAccess.convertToString(
	//						aaTransactionInventoryDetailData
	//							.getInvEndNo())));
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"updTransactionInventoryDetail - SQL - Begin");
	//			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"updTransactionInventoryDetail - SQL - End");
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				"updTransactionInventoryDetail - End");
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"updTransactionInventoryDetail - Exception - "
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	} //END OF Update METHOD
	// end defect 10239 

	/**
	 * Method to UPDATE TransactionInventoryDetail given:
	 *   OfcIssuanceno, Subtaid,TransAMDate,TransWsid,ItmCd, InvItmYr, InvItmNo,
	 *   InvEndNo
	 *
	 * Used in PostTrans to assign inventory ownership when Inventory assigned 
	 * in DB Server Down.
	 *
	 * @param  aaTransactionInventoryDetailData		
	 * @throws RTSException 
	 */
	public void updTransactionInventoryDetailForCache(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updTransactionInventoryDetailForCache - Begin");
		Vector lvValues = new Vector();
		String lsUpd =
			"UPDATE RTS.RTS_TR_INV_DETAIL  SET "
				+ "InvLocIdCd = ?, "
				+ "InvId = ? "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "ItmCd = ? AND "
				+ "InvItmYr = ? AND "
				+ "InvItmNo = ? AND "
				+ "InvEndNo = ? AND "
				+ "InvLocIdCd = 'X' ";
		try
		{
			// SET 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getInvId())));
			// WHERE 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvItmNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getInvEndNo())));
			Log.write(
				Log.SQL,
				this,
				"updTransactionInventoryDetailForCache - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updTransactionInventoryDetailForCache - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updTransactionInventoryDetailForCache - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updTransactionInventoryDetailForCache - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
	/**
	 * Method to UPDATE TransactionInventoryDetail PrntInvQty given:
	 *   OfcIssuanceno, Subtaid, TransAMDate, TransWsid, TransTime, ItmCd
	 * 
	 * @param  aaTransactionInventoryDetailData TransactionInventoryDetailData 
	 * @throws RTSException 
	 */
	public void updTransactionInventoryDetailReprint(TransactionInventoryDetailData aaTransactionInventoryDetailData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updTransactionInventoryDetailReprint - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_TR_INV_DETAIL  SET "
				+ "PRNTINVQTY = ? "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "TransTime = ? AND "
				+ "ItmCd = ?";
		try
		{
			// SET 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getReprntCount())));
			// WHERE 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData
							.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionInventoryDetailData.getItmCd())));
			Log.write(
				Log.SQL,
				this,
				"updTransactionInventoryDetailReprint - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updTransactionInventoryDetailReprint - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updTransactionInventoryDetailReprint - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updTransactionInventoryDetailReprint - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
