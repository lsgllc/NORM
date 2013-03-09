package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.data.SpecialPlatesRegisData;
import com.txdot.isd.rts.services.data.SpecialRegistrationFunctionTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * SpecialRegistrationFunctionTransaction.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/06/2007	Created
 *							defect 9805 Ver Special Plates
 * K Harrell	08/03/2007	Do not 'convertToString' for MfgPltNo on 
 * 							insert. Do not trim on qry for SendTrans
 * 							modify insSpecialRegistrationFunctionTransaction(),
 *                  		 qrySpecialRegistrationFunctionTransaction()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/22/2008	Add DissociateCd
 * 							modify qrySpecialRegistrationFunctionTransaction(),
 * 							  insSpecialRegistrationFunctionTransaction()
 * 							defect 9581 Ver 3 Amigos PH B 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/22/2008	Add DissociateCd
 * 							modify qrySpecialRegistrationFunctionTransaction(),
 * 							  insSpecialRegistrationFunctionTransaction()
 * 							defect 9581 Ver 3 Amigos PH B
 * K Harrell	01/07/2009	Implement PltExpMo/Yr vs. RegExpMo/Yr
 * 							modify insSpecialRegistrationFunctionTransaction(),
 * 							 qrySpecialRegistrationFunctionTransaction()
 *  						defect 9864 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSpecialRegistrationFunctionTransaction()
 * 							defect 9825 Ver Defect_POS_D 
 * K Harrell	07/02/2009	Implement new OwnerData
 * 							modify insSpecialRegistrationFunctionTransaction(), 
 * 							 insSpecialRegistrationFunctionTransaction()
 * 							defect 10112 Ver Defect_POS_F   
 * K Harrell	02/17/2010	Remove SpclRegStkrNo, VrimsMfgCd 
 * 							Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi, 
 *							 AuctnPdAmt, PltValidityTerm, ItrntTraceNo, 
 *							 PltSoldMos 
 * 							modify insSpecialRegistrationFunctionTransaction(), 
 * 							 qrySpecialRegistrationFunctionTransaction()
 * 							defect 10366 Ver POS_640 
 * K Harrell	04/10/2010	Add query for Internet Special Plate 
 * 							 Applications
 * 							add csMethod 
 * 							add qrySRFuncTransForSpclPltApp()
 * 							defect 9858 Ver POS_640
 * K Harrell 	06/14/2010 	add TransId to insert
 * 							modify insSpecialRegistrationFunctionTransaction() 
 * 							defect 10505 Ver 6.5.0
 * K Harrell	06/15/2010	add ElectionPndngIndi 
 * 							modify insSpecialRegistrationFunctionTransaction(),
 * 							 qrySpecialRegistrationFunctionTransaction(), 
 * 							 qrySRFuncTransForSpclPltApp()
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	12/27/2010	modify to set PrntPrmt for records retrieved
 * 							for internet transactions
 * 							modify qrySRFuncTransForSpclPltApp()
 * 							defect 10700 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * SpecialRegistrationFunctionTransaction  
 * 
 * @version	6.7.0 			12/27/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		02/06/2007  13:41:00  
 */

public class SpecialRegistrationFunctionTransaction
{
	String csMethod = new String();

	DatabaseAccess caDA;

	/**
	 * SpecialRegistrationFunctionTransaction constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public SpecialRegistrationFunctionTransaction(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_SR_FUNC_TRANS
	 * 
	 * @param  aaSpecialRegistrationFunctionTransactionData SpecialRegistrationFunctionTransactionData	
	 * @throws RTSException
	 */
	public void delSpecialRegistrationFunctionTransaction(SpecialRegistrationFunctionTransactionData aaSpecialRegistrationFunctionTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delSpecialRegistrationFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_SR_FUNC_TRANS "
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
						aaSpecialRegistrationFunctionTransactionData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getCustSeqNo())));

			if (aaSpecialRegistrationFunctionTransactionData
				.getTransTime()
				!= 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSpecialRegistrationFunctionTransactionData
								.getTransTime())));
			}
			Log.write(
				Log.SQL,
				this,
				"delSpecialRegistrationFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delSpecialRegistrationFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"delSpecialRegistrationFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delSpecialRegistrationFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_SR_FUNC_TRANS
	 * 
	 * @param  aaSRFuncTransData SpecialRegistrationFunctionTransactionData	
	 * @throws RTSException
	 */
	public void insSpecialRegistrationFunctionTransaction(SpecialRegistrationFunctionTransactionData aaSRFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insSpecialRegistrationFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		// defect 10507 
		// add ElectionPndngIndi 

		// defect 10505 
		// add TransId 

		// defect 10336  
		// Remove SpclRegStkrNo, VrimsMfgCd 
		// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi, 
		//		AuctnPdAmt, PltValidityTerm, PltSoldMos, ItrntTraceNo   
		String lsIns =
			"INSERT into RTS.RTS_SR_FUNC_TRANS("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransId,"
				+ "SpclRegId,"
				+ "RegPltCd,"
				+ "OrgNo,"
				+ "RegPltNo,"
				+ "AddlSetIndi,"
				+ "PltExpMo,"
				+ "PltExpYr,"
				+ "MfgPltNo,"
				+ "MfgStatusCd,"
				+ "PltOwnrDlrGDN,"
				+ "PltOwnrId,"
				+ "PltOwnrName1,"
				+ "PltOwnrName2,"
				+ "PltOwnrSt1,"
				+ "PltOwnrSt2,"
				+ "PltOwnrCity,"
				+ "PltOwnrState,"
				+ "PltOwnrZpCd,"
				+ "PltOwnrZpCd4,"
				+ "PltOwnrCntry,"
				+ "PltOwnrPhone,"
				+ "PltOwnrEMail,"
				+ "SAuditTrailTransId,"
				+ "MfgDate,"
				+ "PltSetNo,"
				+ "ISAIndi,"
				+ "PltOwnrOfcCd,"
				+ "PltOwnrDist,"
				+ "SpclDocNo,"
				+ "SpclRemks, "
				+ "DissociateCd,"
				+ "ResrvReasnCd,"
				+ "MktngAllowdIndi, "
				+ "AuctnPltIndi, "
				+ "AuctnPdAmt, "
				+ "PltValidityTerm, "
				+ "PltSoldMos,"
				+ "ItrntTraceNo,"
				+ "ElectionPndngIndi)";
		// end defect 10366  

		lsIns =
			lsIns
				+ "VALUES ( "
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
				+ " ?,"
				+ " ?,"
				+ " ?)";
		// end defect 10366  
		// end defect 10505 
		// end defect 10507

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getOfcIssuanceNo())));
			// 2				
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getSubstaId())));
			// 3			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getTransAMDate())));
			// 4			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getTransWsId())));
			// 5			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getCustSeqNo())));
			// 6			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getTransTime())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSRFuncTransData.getTransCd()));

			// 7a
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSRFuncTransData.getTransId()));
			// 8		
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getSpclRegId())));
			// 9			
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getRegPltCd())));
			// 10			
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSRFuncTransData.getOrgNo()));

			// 11	
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getRegPltNo())));
			// 12			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getAddlSetIndi())));
			// 13
			// defect 9864 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltExpMo())));
			// 14			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltExpYr())));
			// end defect 9864 
			// 15
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSRFuncTransData.getMfgPltNo()));
			// 16		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getMfgStatusCd())));
			// 17
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltOwnrDlrGDN())));

			// defect 10112 
			OwnerData laPltOwnerData =
				aaSRFuncTransData.getPltOwnerData();
			// 18
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnerData.getOwnrId())));
			//	aaSRFuncTransData.getPltOwnrId())));
			// 19
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnerData.getName1())));
			//aaSRFuncTransData.getPltOwnrName1())));
			// 20 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnerData.getName2())));
			//aaSRFuncTransData.getPltOwnrName2())));

			AddressData laPltAddress = laPltOwnerData.getAddressData();
			// 21
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getSt1())));
			//aaSRFuncTransData.getPltOwnrSt1())));
			// 22
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getSt2())));
			//aaSRFuncTransData.getPltOwnrSt2())));
			// 23
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getCity())));
			// aaSRFuncTransData.getPltOwnrCity())));
			// 24
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getState())));
			// aaSRFuncTransData.getPltOwnrState())));
			// 25
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getZpcd())));
			// aaSRFuncTransData.getPltOwnrZpCd())));
			// 26
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getZpcdp4())));
			//aaSRFuncTransData.getPltOwnrZpCdP4())));
			// 27
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltAddress.getCntry())));
			// aaSRFuncTransData.getPltOwnrCntry())));
			// end defect 10112
			// 281
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltOwnrPhone())));
			// 29
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltOwnrEMail())));
			// 30 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getSAuditTrailTransId())));
			// 31
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getMfgDate())));
			// 32
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltSetNo())));
			// 33
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getISAIndi())));
			// 34
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltOwnrOfcCd())));
			// 35
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltOwnrDist())));
			// 36
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getSpclDocNo())));
			// 37
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getSpclRemks())));

			// defect 9581 
			// 38			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getDissociateCd())));
			// end defect 9581 

			// defect 10336  
			// Remove SpclRegStkrNo, VrimsMfgCd 
			// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi, 
			//		AuctnPdAmt, PltValidityTerm, ItrntTraceNo 
			// 39 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getResrvReasnCd())));
			// 40 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getMktngAllowdIndi())));
			// 41	
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getAuctnPltIndi())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getAuctnPdAmt())));

			//42		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltValidityTerm())));

			//43		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getPltSoldMos())));
			// 44		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getItrntTraceNo())));

			// end defect 10366 

			// defect 10507 
			// 45		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSRFuncTransData.getElectionPndngIndi())));
			// end defect 10507 

			Log.write(
				Log.SQL,
				this,
				"insSpecialRegistrationFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insSpecialRegistrationFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insSpecialRegistrationFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insSpecialRegistrationFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_SR_FUNC_TRANS for purge
	 *
	 * @param  aiPurgeAMDate int
	 * @return int	
	 * @throws RTSException 
	 */
	public int purgeSpecialRegistrationFunctionTransaction(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeSpecialRegistrationFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_SR_FUNC_TRANS  "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log.write(
				Log.SQL,
				this,
				"purgeSpecialRegistrationFunctionTransaction - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 			
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeSpecialRegistrationFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeSpecialRegistrationFunctionTransaction - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSpecialRegistrationFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query from RTS.RTS_SR_FUNC_TRANS
	 *
	 * @param 	aaSpecialRegistrationFunctionTransactionData SpecialRegistrationFunctionTransactionData	
	 * @return  Vector 
	 * @throws 	RTSException 	
	 */
	public Vector qrySpecialRegistrationFunctionTransaction(SpecialRegistrationFunctionTransactionData aaSpecialRegistrationFunctionTransactionData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySpecialRegistrationFunctionTransaction - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 10507 
		// add ElectionPndngIndi 

		// defect 10336  
		// Remove SpclRegStkrNo, VrimsMfgCd 
		// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi, 
		//		AuctnPdAmt, PltValidityTerm, PltSoldMos, ItrntTraceNo   
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "B.OfcIssuanceCd,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "B.TransEmpId,"
				+ "SpclRegId,"
				+ "RegPltCd,"
				+ "OrgNo,"
				+ "RegPltNo,"
				+ "AddlSetIndi,"
				+ "PltExpMo,"
				+ "PltExpYr,"
				+ "MfgPltNo,"
				+ "MfgStatusCd,"
				+ "PltOwnrDlrGDN,"
				+ "PltOwnrId,"
				+ "PltOwnrName1,"
				+ "PltOwnrName2,"
				+ "PltOwnrSt1,"
				+ "PltOwnrSt2,"
				+ "PltOwnrCity,"
				+ "PltOwnrState,"
				+ "PltOwnrZpCd,"
				+ "PltOwnrZpCd4,"
				+ "PltOwnrCntry,"
				+ "PltOwnrPhone,"
				+ "PltOwnrEMail,"
				+ "SAuditTrailTransId,"
				+ "MfgDate,"
				+ "PltSetNo,"
				+ "ISAIndi,"
				+ "PltOwnrOfcCd,"
				+ "PltOwnrDist,"
				+ "SpclDocNo, "
				+ "SpclRemks, "
				+ "DissociateCd, "
				+ "ResrvReasnCd,"
				+ "MktngAllowdIndi, "
				+ "AuctnPltIndi, "
				+ "AuctnPdAmt, "
				+ "PltValidityTerm, "
				+ "PltSoldMos, "
				+ "ItrntTraceNo, "
				+ "ElectionPndngIndi "
				+ "FROM RTS.RTS_SR_FUNC_TRANS A, "
				+ "RTS.RTS_TRANS_HDR B "
				+ "WHERE "
				+ "A.OfcIssuanceNo = ? AND "
				+ "A.SubstaId = ? AND "
				+ "A.TransAMDate = ? AND "
				+ "A.TransWsId = ? AND "
				+ "A.CustSeqNo = ? AND "
				+ "A.TransTime = ? AND "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO ");
		// end defect 10336   
		// end defect 10507 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialRegistrationFunctionTransactionData
							.getTransTime())));

			Log.write(
				Log.SQL,
				this,
				" - qrySpecialRegistrationFunctionTransaction - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySpecialRegistrationFunctionTransaction - SQL - End");

			while (lrsQry.next())
			{

				SpecialRegistrationFunctionTransactionData laSRFuncTransData =
					new SpecialRegistrationFunctionTransactionData();
				laSRFuncTransData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSRFuncTransData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laSRFuncTransData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laSRFuncTransData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laSRFuncTransData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laSRFuncTransData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laSRFuncTransData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laSRFuncTransData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laSRFuncTransData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laSRFuncTransData.setSpclRegId(
					caDA.getLongFromDB(lrsQry, "SpclRegId"));
				laSRFuncTransData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laSRFuncTransData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "OrgNo"));
				laSRFuncTransData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laSRFuncTransData.setAddlSetIndi(
					caDA.getIntFromDB(lrsQry, "AddlSetIndi"));
				laSRFuncTransData.setPltExpMo(
					caDA.getIntFromDB(lrsQry, "PltExpMo"));
				laSRFuncTransData.setPltExpYr(
					caDA.getIntFromDB(lrsQry, "PltExpYr"));

				// false ==> No Trim
				laSRFuncTransData.setMfgPltNo(
					caDA.getStringFromDB(lrsQry, "MfgPltNo", false));
				laSRFuncTransData.setMfgStatusCd(
					caDA.getStringFromDB(lrsQry, "MfgStatusCd"));
				laSRFuncTransData.setPltOwnrDlrGDN(
					caDA.getStringFromDB(lrsQry, "PltOwnrDlrGDN"));

				// defect 10112 
				OwnerData laPltOwnerData =
					laSRFuncTransData.getPltOwnerData();

				laPltOwnerData.setOwnrId(
					caDA.getStringFromDB(lrsQry, "PltOwnrId"));

				laPltOwnerData.setName1(
					caDA.getStringFromDB(lrsQry, "PltOwnrName1"));

				laPltOwnerData.setName2(
					caDA.getStringFromDB(lrsQry, "PltOwnrName2"));

				AddressData laPltAddressData =
					laPltOwnerData.getAddressData();

				laPltAddressData.setSt1(
					caDA.getStringFromDB(lrsQry, "PltOwnrSt1"));

				laPltAddressData.setSt2(
					caDA.getStringFromDB(lrsQry, "PltOwnrSt2"));

				laPltAddressData.setCity(
					caDA.getStringFromDB(lrsQry, "PltOwnrCity"));

				laPltAddressData.setState(
					caDA.getStringFromDB(lrsQry, "PltOwnrState"));

				laPltAddressData.setZpcd(
					caDA.getStringFromDB(lrsQry, "PltOwnrZpCd"));

				laPltAddressData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "PltOwnrZpCd4"));

				laPltAddressData.setCntry(
					caDA.getStringFromDB(lrsQry, "PltOwnrCntry"));
				// end defect 10112 

				laSRFuncTransData.setPltOwnrPhone(
					caDA.getStringFromDB(lrsQry, "PltOwnrPhone"));
				laSRFuncTransData.setPltOwnrEMail(
					caDA.getStringFromDB(lrsQry, "PltOwnrEmail"));
				laSRFuncTransData.setSAuditTrailTransId(
					caDA.getStringFromDB(lrsQry, "SAuditTrailTransId"));
				laSRFuncTransData.setMfgDate(
					caDA.getIntFromDB(lrsQry, "MfgDate"));
				laSRFuncTransData.setPltSetNo(
					caDA.getIntFromDB(lrsQry, "PltSetNo"));
				laSRFuncTransData.setISAIndi(
					caDA.getIntFromDB(lrsQry, "ISAIndi"));
				laSRFuncTransData.setPltOwnrOfcCd(
					caDA.getStringFromDB(lrsQry, "PltOwnrOfcCd"));
				laSRFuncTransData.setPltOwnrDist(
					caDA.getStringFromDB(lrsQry, "PltOwnrDist"));
				laSRFuncTransData.setSpclDocNo(
					caDA.getStringFromDB(lrsQry, "SpclDocNo"));
				laSRFuncTransData.setSpclRemks(
					caDA.getStringFromDB(lrsQry, "SpclRemks"));
				// defect 9581 
				laSRFuncTransData.setDissociateCd(
					caDA.getIntFromDB(lrsQry, "DissociateCd"));
				// end defect 9581

				// defect 10336  
				// Remove SpclRegStkrNo, VrimsMfgCd 
				// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi, 
				//		AuctnPdAmt, PltValidityTerm, PltSoldMos
				//      ItrntTraceNo 
				laSRFuncTransData.setResrvReasnCd(
					caDA.getStringFromDB(lrsQry, "ResrvReasnCd"));

				laSRFuncTransData.setMktngAllowdIndi(
					caDA.getIntFromDB(lrsQry, "MktngAllowdIndi"));

				laSRFuncTransData.setAuctnPltIndi(
					caDA.getIntFromDB(lrsQry, "AuctnPltIndi"));

				laSRFuncTransData.setAuctnPdAmt(
					caDA.getDollarFromDB(lrsQry, "AuctnPdAmt"));

				laSRFuncTransData.setPltValidityTerm(
					caDA.getIntFromDB(lrsQry, "PltValidityTerm"));

				laSRFuncTransData.setPltSoldMos(
					caDA.getIntFromDB(lrsQry, "PltSoldMos"));

				laSRFuncTransData.setItrntTraceNo(
					caDA.getStringFromDB(lrsQry, "ItrntTraceNo"));
				// end defect 10366

				// defect 10507 					
				laSRFuncTransData.setElectionPndngIndi(
					caDA.getIntFromDB(lrsQry, "ElectionPndngIndi"));
				// end defect 10507 

				// Add element to the Vector
				lvRslt.addElement(laSRFuncTransData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySpecialRegistrationFunctionTransaction - End ");
			return (lvRslt);
		}
		// defect 10366 
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySpecialRegistrationFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		// end defect 10366 
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySpecialRegistrationFunctionTransaction - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query from RTS.RTS_SR_FUNC_TRANS for Special Plate 
	 * Application
	 *
	 * @param 	asRegPltNo	
	 * @return  SpecialPlateRegisData 
	 * @throws 	RTSException 	
	 */
	public SpecialPlatesRegisData qrySRFuncTransForSpclPltApp(String asRegPltNo)
		throws RTSException
	{
		csMethod = " - qrySRFuncTransForSpclPltApp";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		SpecialPlatesRegisData laSpclPltRegisData = null;

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 10507
		// add ElectionPndngIndi 
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "A.TransWsId,"
				+ "A.TransAMDate,"
				+ "A.TransTime,"
				+ "SpclRegId,"
				+ "A.RegPltCd,"
				+ "OrgNo,"
				+ "A.RegPltNo,"
				+ "AddlSetIndi,"
				+ "PltExpMo,"
				+ "PltExpYr,"
				+ "MfgPltNo,"
				+ "PltOwnrId,"
				+ "PltOwnrName1,"
				+ "PltOwnrName2,"
				+ "PltOwnrSt1,"
				+ "PltOwnrSt2,"
				+ "PltOwnrCity,"
				+ "PltOwnrState,"
				+ "PltOwnrZpCd,"
				+ "PltOwnrZpCd4,"
				+ "PltOwnrCntry,"
				+ "PltOwnrPhone,"
				+ "PltOwnrEMail,"
				+ "MfgDate,"
				+ "ISAIndi,"
				+ "ResrvReasnCd,"
				+ "MktngAllowdIndi, "
				+ "AuctnPltIndi, "
				+ "AuctnPdAmt, "
				+ "A.PltValidityTerm, "
				+ "A.PltSoldMos, "
				+ "A.ItrntTraceNo, "
				+ "C.ResComptCntyNo, "
				+ "A.ElectionPndngIndi "
				+ "FROM RTS.RTS_SR_FUNC_TRANS A, "
				+ "RTS.RTS_TRANS_HDR B, "
				+ "RTS.RTS_MV_FUNC_TRANS C "
				+ "WHERE "
				+ "A.OfcIssuanceNo in ("
				+ SystemProperty.getVpOfcIssuanceNo()
				+ ","
				+ SpecialPlatesConstant.IAPPL_OFCISSUANCENO
				+ ") "
				+ " AND "
				+ "A.RegPltNo = ? AND "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ "A.SUBSTAID = C.SUBSTAID AND "
				+ "A.TRANSAMDATE = C.TRANSAMDATE AND "
				+ "A.TRANSWSID = C.TRANSWSID AND "
				+ "A.CUSTSEQNO = C.CUSTSEQNO AND "
				+ "A.TRANSTIME  = C.TRANSTIME AND "
				+ "A.TRANSCD IN ('"
				+ TransCdConstant.VPAPPL
				+ "','"
				+ TransCdConstant.VPAPPR
				+ "','"
				+ TransCdConstant.IAPPL
				+ "') AND "
				+ "B.TRANSTIMESTMP IS NOT NULL ");
		// end defect 10507

		// TODO 
		// TO CONSIDER 
		//	+ "AND "
		//	+ "DATE(TRANSTIMESTMP) IN "
		//	+ "(CURRENT DATE,CURRENT DATE - 3 DAY) ");

		try
		{
			lvValues.addElement(new DBValue(Types.CHAR, asRegPltNo));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				laSpclPltRegisData = new SpecialPlatesRegisData();
				int liOfcIssuanceNo =
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo");

				int liTransWsId =
					caDA.getIntFromDB(lrsQry, "TransWsId");

				int liTransAMDate =
					caDA.getIntFromDB(lrsQry, "TransAMDate");

				int liTransTime =
					caDA.getIntFromDB(lrsQry, "TransTime");

				// Build AuditTrailTransId
				String lsTransId =
					UtilityMethods.getTransId(
						liOfcIssuanceNo,
						liTransWsId,
						liTransAMDate,
						liTransTime);

				laSpclPltRegisData.setSAuditTrailTransId(lsTransId);

				int liApplDate =
					new RTSDate(RTSDate.AMDATE, liTransAMDate)
						.getYYYYMMDDDate();
				laSpclPltRegisData.setPltApplDate(liApplDate);

				laSpclPltRegisData.setSpclRegId(
					caDA.getLongFromDB(lrsQry, "SpclRegId"));
				laSpclPltRegisData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laSpclPltRegisData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "OrgNo"));
				laSpclPltRegisData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laSpclPltRegisData.setAddlSetIndi(
					caDA.getIntFromDB(lrsQry, "AddlSetIndi"));
				laSpclPltRegisData.setPltExpMo(
					caDA.getIntFromDB(lrsQry, "PltExpMo"));
				laSpclPltRegisData.setPltExpYr(
					caDA.getIntFromDB(lrsQry, "PltExpYr"));

				// false ==> No Trim
				laSpclPltRegisData.setMfgPltNo(
					caDA.getStringFromDB(lrsQry, "MfgPltNo", false));

				laSpclPltRegisData.setMFGStatusCd(new String());

				OwnerData laPltOwnerData =
					laSpclPltRegisData.getOwnrData();

				laPltOwnerData.setOwnrId(
					caDA.getStringFromDB(lrsQry, "PltOwnrId"));

				laPltOwnerData.setName1(
					(caDA.getStringFromDB(lrsQry, "PltOwnrName1"))
						.toUpperCase());

				laPltOwnerData.setName2(
					caDA.getStringFromDB(lrsQry, "PltOwnrName2"));

				AddressData laPltAddressData =
					laPltOwnerData.getAddressData();

				laPltAddressData.setSt1(
					(caDA.getStringFromDB(lrsQry, "PltOwnrSt1"))
						.toUpperCase());

				laPltAddressData.setSt2(
					(caDA.getStringFromDB(lrsQry, "PltOwnrSt2"))
						.toUpperCase());

				laPltAddressData.setCity(
					(caDA.getStringFromDB(lrsQry, "PltOwnrCity"))
						.toUpperCase());

				laPltAddressData.setState(
					(caDA.getStringFromDB(lrsQry, "PltOwnrState"))
						.toUpperCase());

				laPltAddressData.setZpcd(
					caDA.getStringFromDB(lrsQry, "PltOwnrZpCd"));

				laPltAddressData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "PltOwnrZpCd4"));

				laPltAddressData.setCntry(
					(caDA.getStringFromDB(lrsQry, "PltOwnrCntry"))
						.toUpperCase());

				laSpclPltRegisData.setPltOwnrPhoneNo(
					caDA.getStringFromDB(lrsQry, "PltOwnrPhone"));

				laSpclPltRegisData.setPltOwnrEMail(
					(caDA.getStringFromDB(lrsQry, "PltOwnrEmail"))
						.toUpperCase());

				laSpclPltRegisData.setMFGDate(
					caDA.getIntFromDB(lrsQry, "MfgDate"));

				laSpclPltRegisData.setISAIndi(
					caDA.getIntFromDB(lrsQry, "ISAIndi"));

				// New Columns in 6.4.0  
				laSpclPltRegisData.setResrvReasnCd(
					caDA.getStringFromDB(lrsQry, "ResrvReasnCd"));

				laSpclPltRegisData.setMktngAllowdIndi(
					caDA.getIntFromDB(lrsQry, "MktngAllowdIndi"));

				laSpclPltRegisData.setAuctnPltIndi(
					caDA.getIntFromDB(lrsQry, "AuctnPltIndi"));

				laSpclPltRegisData.setAuctnPdAmt(
					caDA.getDollarFromDB(lrsQry, "AuctnPdAmt"));

				laSpclPltRegisData.setPltValidityTerm(
					caDA.getIntFromDB(lrsQry, "PltValidityTerm"));

				laSpclPltRegisData.setItrntTraceNo(
					caDA.getStringFromDB(lrsQry, "ItrntTraceNo"));

				laSpclPltRegisData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));

				// defect 10507 					
				laSpclPltRegisData.setElectionPndngIndi(
					caDA.getIntFromDB(lrsQry, "ElectionPndngIndi"));
				// end defect 10507 

				// defect 10700 
				laSpclPltRegisData.setPrntPrmt(true);
				// end defect 10700

				break;
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return laSpclPltRegisData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "- Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

} //END OF CLASS
