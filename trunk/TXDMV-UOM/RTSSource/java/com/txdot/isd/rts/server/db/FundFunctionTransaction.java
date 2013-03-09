package com.txdot.isd.rts.server.db;import java.sql.ResultSet;import java.sql.SQLException;import java.sql.Types;import java.util.Vector;import com.txdot.isd.rts.server.dataaccess.DBValue;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;import com.txdot.isd.rts.services.data.FundFunctionTransactionData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.Log;/* * * FundFunctionTransaction.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell   	10/16/2001  Altered Double to Dollar * K Harrell   	10/19/2001  Added purgeFundFunctionTransaction * K Harrell   	10/24/2001  Added OfcIssuanceCd for SendTrans * K Harrell   	11/02/2001  Added voidFundFunctionTransaction * K Harrell   	11/11/2001  Corrected typo for VoidedTransIndi *                          Removed OfcIssuanceNo,SubstaId from Purge * R Hicks      07/12/2002  Add call to closeLastStatement() after a  *							query * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate *							modify purgeFundFunctionTransaction() *							defect 7684  Ver 5.2.2 * K Harrell	03/02/2005	Java 1.4 Work *							defect 7899 Ver 5.2.3  * K Harrell	11/11/2005	Accept passed int vs.  * 							FundFunctionTransactionData Object * 							modify purgeFundFunctionTransaction()  * 							defect 8423 Ver 5.2.3   * K Harrell	05/10/2007	Add ItrntTraceNo to SQL * 							modify insFundFunctionTransaction(), * 							 qryFundFunctionTransaction(), * 							 updFundFunctionTransaction() * 							defect 9085 Ver Special Plates * K Harrell	01/19/2009	Return number of rows purged * 							modify purgeFundFunctionTransaction() * 							defect 9825 Ver Defect_POS_D      * K Harrell	03/18/2010	Removed unused method.  * 							delete updFundFunctionTransaction() * 							defect 10239 Ver POS_640  * K Harrell	06/15/2010  add TransId to insert * 							modify insFundFunctionTransaction() * 							defect 10505 Ver 6.5.0   * --------------------------------------------------------------------- *//** * This class contains methods to interact with the database *  (RTS_FUND_FUNC_TRNS) * * @version	6.5.0 			06/15/2010 * @author	Kathy Harrell * <br>Creation Date:		09/21/2001 10:37:55 *//* &FundFunctionTransaction& */public class FundFunctionTransaction	extends FundFunctionTransactionData{/* &FundFunctionTransaction'caDA& */	DatabaseAccess caDA;	/**	 * FundFunctionTransaction constructor comment.	 *	 * @param  aaDA  DatabaseAccess	 * @throws RTSException	 *//* &FundFunctionTransaction.FundFunctionTransaction& */	public FundFunctionTransaction(DatabaseAccess aaDA)		throws RTSException	{		caDA = aaDA;	}	/**	 * Method to Delete from RTS.RTS_FUND_FUNC_TRNS	 * 	 * @param  aaFundFunctionTransactionData FundFunctionTransactionData		 * @throws RTSException	 *//* &FundFunctionTransaction.delFundFunctionTransaction& */	public void delFundFunctionTransaction(FundFunctionTransactionData aaFundFunctionTransactionData)		throws RTSException	{		Log.write(			Log.METHOD,			this,			"delFundFunctionTransaction - Begin");		Vector lvValues = new Vector();		String lsDel =			"DELETE FROM RTS.RTS_FUND_FUNC_TRNS "				+ "WHERE "				+ "OfcIssuanceNo = ? AND "				+ "SubstaId = ? AND "				+ "TransAMDate = ? AND "				+ "TransWsId = ? AND "				+ "CustSeqNo = ? ";		try		{			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getOfcIssuanceNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getSubstaId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getTransAMDate())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransWsId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getCustSeqNo())));			if (aaFundFunctionTransactionData.getTransTime() != 0)			{				lsDel = lsDel + " AND TransTime = ? ";				lvValues.addElement(					new DBValue(						Types.INTEGER,						DatabaseAccess.convertToString(							aaFundFunctionTransactionData								.getTransTime())));			}			Log.write(				Log.SQL,				this,				"delFundFunctionTransaction - SQL - Begin");			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);			Log.write(				Log.SQL,				this,				"delFundFunctionTransaction - SQL - End");			Log.write(				Log.METHOD,				this,				"delFundFunctionTransaction - End");		}		catch (RTSException aeRTSEx)		{			Log.write(				Log.SQL_EXCP,				this,				"delFundFunctionTransaction - Exception - "					+ aeRTSEx.getMessage());			throw aeRTSEx;		}	} //END OF Delete METHOD	/**	 * Method to Insert into RTS.RTS_FUND_FUNC_TRNS	 * 	 * @param  aaFundFunctionTransactionData FundFunctionTransactionData		 * @throws RTSException	 *//* &FundFunctionTransaction.insFundFunctionTransaction& */	public void insFundFunctionTransaction(FundFunctionTransactionData aaFundFunctionTransactionData)		throws RTSException	{		Log.write(			Log.METHOD,			this,			"insFundFunctionTransaction - Begin");		Vector lvValues = new Vector();		// defect 10505 		// add TransId 		// defect 9085 		String lsIns =			"INSERT into RTS.RTS_FUND_FUNC_TRNS("				+ "OfcIssuanceNo,"				+ "SubstaId,"				+ "TransAMDate,"				+ "TransWsId,"				+ "CustSeqNo,"				+ "TransTime,"				+ "TransCd,"				+ "VoidedTransIndi,"				+ "SubconIssueDate,"				+ "SubconId,"				+ "ApprndComptCntyNo,"				+ "CkNo,"				+ "FundsPymntAmt,"				+ "FundsPymntDate,"				+ "ComptCntyNo,"				+ "AccntNoCd,"				+ "FundsAdjReasn,"				+ "TransId,"				+ "TraceNo,"				+ "ItrntTraceNo ) VALUES ( "				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?,"				+ " ?)";		// end defect 9085		// end defect 10505 		try		{			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getOfcIssuanceNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getSubstaId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getTransAMDate())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransWsId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getCustSeqNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransTime())));			lvValues.addElement(				new DBValue(					Types.CHAR,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransCd())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getVoidedTransIndi())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getSubconIssueDate())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getSubconId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getApprndComptCntyNo())));			lvValues.addElement(				new DBValue(					Types.CHAR,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getCkNo())));			lvValues.addElement(				new DBValue(					Types.DECIMAL,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getFundsPymntAmt())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getFundsPymntDate())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getComptCntyNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getAccntNoCd())));			lvValues.addElement(				new DBValue(					Types.CHAR,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getFundsAdjReasn())));			// defect 10505 			lvValues.addElement(				new DBValue(					Types.CHAR,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransId())));			// end defect 10505 						lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTraceNo())));			// defect 9085 			lvValues.addElement(				new DBValue(					Types.CHAR,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getItrntTraceNo())));			// end defect 9085 			Log.write(				Log.SQL,				this,				"insFundFunctionTransaction - SQL - Begin");			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);			Log.write(				Log.SQL,				this,				"insFundFunctionTransaction - SQL - End");			Log.write(				Log.METHOD,				this,				"insFundFunctionTransaction - End");		}		catch (RTSException aeRTSEx)		{			Log.write(				Log.SQL_EXCP,				this,				"insFundFunctionTransaction - Exception - "					+ aeRTSEx.getMessage());			throw aeRTSEx;		}	} //END OF INSERT METHOD	/**	 * Method to Delete from RTS.RTS_FUND_FUNC_TRNS for purge	 *	 * @param  aiPurgeAMDate int	 * @return int		 * @throws RTSException 	 *//* &FundFunctionTransaction.purgeFundFunctionTransaction& */	public int purgeFundFunctionTransaction(int aiPurgeAMDate)		throws RTSException	{		Log.write(			Log.METHOD,			this,			"purgeFundFunctionTransaction - Begin");		Vector lvValues = new Vector();		String lsDel =			" DELETE FROM RTS.RTS_FUND_FUNC_TRNS A "				+ " WHERE  "				+ " TRANSAMDATE <= ? ";		try		{			// defect 8423			// Used passed int			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(aiPurgeAMDate)));			// end defect 8423			Log.write(				Log.SQL,				this,				"purgeFundFunctionTransaction - SQL - Begin");			// defect 9825 			// Return number of rows purged 			int liNumRows =				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);			Log.write(				Log.SQL,				this,				"purgeFundFunctionTransaction - SQL - End");			Log.write(				Log.METHOD,				this,				"purgeFundFunctionTransaction - End");			return liNumRows;			// end defect 9825  		}		catch (RTSException aeRTSEx)		{			Log.write(				Log.SQL_EXCP,				this,				"purgeFundFunctionTransaction - Exception - "					+ aeRTSEx.getMessage());			throw aeRTSEx;		}	} //END OF Delete METHOD	/**	 * Method to Query from RTS.RTS_FUND_FUNC_TRNS	 *	 * @param 	aaFundFunctionTransactionData FundFunctionTransactionData		 * @return  Vector 	 * @throws 	RTSException 		 *//* &FundFunctionTransaction.qryFundFunctionTransaction& */	public Vector qryFundFunctionTransaction(FundFunctionTransactionData aaFundFunctionTransactionData)		throws RTSException	{		Log.write(			Log.METHOD,			this,			" - qryFundFunctionTransaction - Begin");		StringBuffer lsQry = new StringBuffer();		Vector lvRslt = new Vector();		Vector lvValues = new Vector();		ResultSet lrsQry;		// defect 9085 		lsQry.append(			"SELECT "				+ "A.OfcIssuanceNo,"				+ "B.OfcIssuanceCd,"				+ "A.SubstaId,"				+ "A.TransAMDate,"				+ "A.TransWsId,"				+ "A.CustSeqNo,"				+ "TransTime,"				+ "TransCd,"				+ "VoidedTransIndi,"				+ "SubconIssueDate,"				+ "SubconId,"				+ "ApprndComptCntyNo,"				+ "CkNo,"				+ "FundsPymntAmt,"				+ "FundsPymntDate,"				+ "ComptCntyNo,"				+ "AccntNoCd,"				+ "FundsAdjReasn,"				+ "TraceNo, "				+ "ItrntTraceNo "				+ "FROM RTS.RTS_FUND_FUNC_TRNS A, "				+ "RTS.RTS_TRANS_HDR B "				+ "WHERE "				+ "A.OfcIssuanceNo = ? AND "				+ "A.SubstaId = ? AND "				+ "A.TransAMDate = ? AND "				+ "A.TransWsId = ? AND "				+ "A.CustSeqNo = ? AND "				+ "A.TransTime = ? AND "				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "				+ "A.SUBSTAID = B.SUBSTAID AND "				+ "A.TRANSAMDATE = B.TRANSAMDATE AND "				+ "A.TRANSWSID = B.TRANSWSID AND "				+ "A.CUSTSEQNO = B.CUSTSEQNO ");		// end defect 9085 		try		{			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getOfcIssuanceNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getSubstaId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getTransAMDate())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransWsId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getCustSeqNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransTime())));			Log.write(				Log.SQL,				this,				" - qryFundFunctionTransaction - SQL - Begin");			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);			Log.write(				Log.SQL,				this,				" - qryFundFunctionTransaction - SQL - End");			while (lrsQry.next())			{				FundFunctionTransactionData laFundFunctionTransactionData =					new FundFunctionTransactionData();				laFundFunctionTransactionData.setOfcIssuanceNo(					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));				laFundFunctionTransactionData.setOfcIssuanceCd(					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));				laFundFunctionTransactionData.setSubstaId(					caDA.getIntFromDB(lrsQry, "SubstaId"));				laFundFunctionTransactionData.setTransAMDate(					caDA.getIntFromDB(lrsQry, "TransAMDate"));				laFundFunctionTransactionData.setTransWsId(					caDA.getIntFromDB(lrsQry, "TransWsId"));				laFundFunctionTransactionData.setCustSeqNo(					caDA.getIntFromDB(lrsQry, "CustSeqNo"));				laFundFunctionTransactionData.setTransTime(					caDA.getIntFromDB(lrsQry, "TransTime"));				laFundFunctionTransactionData.setTransCd(					caDA.getStringFromDB(lrsQry, "TransCd"));				laFundFunctionTransactionData.setVoidedTransIndi(					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));				laFundFunctionTransactionData.setSubconIssueDate(					caDA.getIntFromDB(lrsQry, "SubconIssueDate"));				laFundFunctionTransactionData.setSubconId(					caDA.getIntFromDB(lrsQry, "SubconId"));				laFundFunctionTransactionData.setApprndComptCntyNo(					caDA.getIntFromDB(lrsQry, "ApprndComptCntyNo"));				laFundFunctionTransactionData.setCkNo(					caDA.getStringFromDB(lrsQry, "CkNo"));				laFundFunctionTransactionData.setFundsPymntAmt(					caDA.getDollarFromDB(lrsQry, "FundsPymntAmt"));				laFundFunctionTransactionData.setFundsPymntDate(					caDA.getIntFromDB(lrsQry, "FundsPymntDate"));				laFundFunctionTransactionData.setComptCntyNo(					caDA.getIntFromDB(lrsQry, "ComptCntyNo"));				laFundFunctionTransactionData.setAccntNoCd(					caDA.getIntFromDB(lrsQry, "AccntNoCd"));				laFundFunctionTransactionData.setFundsAdjReasn(					caDA.getStringFromDB(lrsQry, "FundsAdjReasn"));				laFundFunctionTransactionData.setTraceNo(					caDA.getIntFromDB(lrsQry, "TraceNo"));				// defect 9085 				laFundFunctionTransactionData.setItrntTraceNo(					caDA.getStringFromDB(lrsQry, "ItrntTraceNo"));				// end defect 9085 				// Add element to the Vector				lvRslt.addElement(laFundFunctionTransactionData);			} //End of While			lrsQry.close();			caDA.closeLastDBStatement();			lrsQry = null;			Log.write(				Log.METHOD,				this,				" - qryFundFunctionTransaction - End ");			return (lvRslt);		}		catch (SQLException aeSQLEx)		{			Log.write(				Log.SQL_EXCP,				this,				" - qryFundFunctionTransaction - Exception "					+ aeSQLEx.getMessage());			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);		}	} //END OF QUERY METHOD	// defect 10239 	//	/**	//	 * Method to update into RTS.RTS_FUND_FUNC_TRNS	//	 * 	//	 * @param  aaFundFunctionTransactionData FundFunctionTransactionData		//	 * @throws RTSException 	//	 */	//	public void updFundFunctionTransaction(FundFunctionTransactionData aaFundFunctionTransactionData)	//		throws RTSException	//	{	//		Log.write(	//			Log.METHOD,	//			this,	//			"updFundFunctionTransaction - Begin");	//	//		Vector lvValues = new Vector();	//		// defect 9085 	//		// (Don't think this is really used)	//		// Add ItrntTraceNo 	//		String lsUpd =	//			"UPDATE RTS.RTS_FUND_FUNC_TRNS SET "	//				+ "OfcIssuanceNo = ?, "	//				+ "SubstaId = ?, "	//				+ "TransAMDate = ?, "	//				+ "TransWsId = ?, "	//				+ "CustSeqNo = ?, "	//				+ "TransTime = ?, "	//				+ "TransCd = ?, "	//				+ "VoidedTransIndi = ?, "	//				+ "SubconIssueDate = ?, "	//				+ "SubconId = ?, "	//				+ "ApprndComptCntyNo = ?, "	//				+ "CkNo = ?, "	//				+ "FundsPymntAmt = ?, "	//				+ "FundsPymntDate = ?, "	//				+ "ComptCntyNo = ?, "	//				+ "AccntNoCd = ?, "	//				+ "FundsAdjReasn = ?, "	//				+ "TraceNo = ?, "	//				+ "ItrntTraceNo = ? "	//				+ "WHERE "	//				+ "OfcIssuanceNo = ? AND "	//				+ "SubstaId = ? AND "	//				+ "TransAMDate = ? AND "	//				+ "TransWsId = ? AND "	//				+ "CustSeqNo = ? AND "	//				+ "TransTime = ? ";	//		// end defect 9085 	//		try	//		{	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getOfcIssuanceNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getSubstaId())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getTransAMDate())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getTransWsId())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getCustSeqNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getTransTime())));	//			lvValues.addElement(	//				new DBValue(	//					Types.CHAR,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getTransCd())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getVoidedTransIndi())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getSubconIssueDate())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getSubconId())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getApprndComptCntyNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.CHAR,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getCkNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.DECIMAL,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getFundsPymntAmt())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getFundsPymntDate())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getComptCntyNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getAccntNoCd())));	//			lvValues.addElement(	//				new DBValue(	//					Types.CHAR,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getFundsAdjReasn())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getTraceNo())));	//	//			// defect 9085 							//			lvValues.addElement(	//				new DBValue(	//					Types.CHAR,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getItrntTraceNo())));	//			// end defect 9085 	//	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getOfcIssuanceNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getSubstaId())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData	//							.getTransAMDate())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getTransWsId())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getCustSeqNo())));	//			lvValues.addElement(	//				new DBValue(	//					Types.INTEGER,	//					DatabaseAccess.convertToString(	//						aaFundFunctionTransactionData.getTransTime())));	//	//			Log.write(	//				Log.SQL,	//				this,	//				"updFundFunctionTransaction - SQL - Begin");	//			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);	//			Log.write(	//				Log.SQL,	//				this,	//				"updFundFunctionTransaction - SQL - End");	//			Log.write(	//				Log.METHOD,	//				this,	//				"updFundFunctionTransaction - End");	//		}	//		catch (RTSException aeRTSEx)	//		{	//			Log.write(	//				Log.SQL_EXCP,	//				this,	//				"updFundFunctionTransaction - Exception - "	//					+ aeRTSEx.getMessage());	//			throw aeRTSEx;	//		}	//	} //END OF Update METHOD	// end defect 10239 	/**	 * Method to void RTS.RTS_FUND_FUNC_TRNS	 * 	 * @param  aaFundFunctionTransactionData FundFunctionTransactionData		 * @throws RTSException 	 *//* &FundFunctionTransaction.voidFundFunctionTransaction& */	public void voidFundFunctionTransaction(FundFunctionTransactionData aaFundFunctionTransactionData)		throws RTSException	{		Log.write(			Log.METHOD,			this,			"voidFundFunctionTransaction - Begin");		Vector lvValues = new Vector();		String lsUpd =			"UPDATE RTS.RTS_FUND_FUNC_TRNS SET "				+ "VoidedTransIndi = 1 "				+ "WHERE "				+ "OfcIssuanceNo = ? AND "				+ "SubstaId = ? AND "				+ "TransAMDate = ? AND "				+ "TransWsId = ? AND "				+ "CustSeqNo = ? AND "				+ "TransTime = ? ";		try		{			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getOfcIssuanceNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getSubstaId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData							.getTransAMDate())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransWsId())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getCustSeqNo())));			lvValues.addElement(				new DBValue(					Types.INTEGER,					DatabaseAccess.convertToString(						aaFundFunctionTransactionData.getTransTime())));			Log.write(				Log.SQL,				this,				"voidFundFunctionTransaction - SQL - Begin");			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);			Log.write(				Log.SQL,				this,				"voidFundFunctionTransaction - SQL - End");			Log.write(				Log.METHOD,				this,				"voidFundFunctionTransaction - End");		}		catch (RTSException aeRTSEx)		{			Log.write(				Log.SQL_EXCP,				this,				"voidFundFunctionTransaction - Exception - "					+ aeRTSEx.getMessage());			throw aeRTSEx;		}	} //END OF Update METHOD}/* #FundFunctionTransaction# */ //END OF CLASS