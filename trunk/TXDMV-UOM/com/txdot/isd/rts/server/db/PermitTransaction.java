package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PermitTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * PermitTransaction.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10491 Ver 6.5.0
 * K Harrell	06/14/2010 	added TransId to insert
 * 							modify insPermitTransaction()
 * 							defect 10505 Ver 6.5.0  
 * K Harrell	06/20/2010	For testing prior to MF access availability
 * 							modify qryPermitTransaction(GeneralSearchData) 
 * 							for new PermitPartialsData
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/30/2010	add VehMkDesc, BulkPrmtVendorId,
 * 							 VoidedTransIndi to SQL 
 * 							add voidPermitTransaction() 
 * 							modify insPermitTransaction(), 
 * 							 qryPermitTransaction(PermitTransactionData)
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/19/2011	add qryUnpostedPermitTransaction()
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database
 *
 * @version	6.8.0 			06/30/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		05/24/2010	14:53:17
 */
public class PermitTransaction
{
	private String csMethod = new String();

	private DatabaseAccess caDA;

	/**
	 * PermitTransaction constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public PermitTransaction(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_PRMT_TRANS
	 * 
	 * @param  aaPrmtTransData PermitTransactionData	
	 * @throws RTSException
	 */
	public void delPermitTransaction(PermitTransactionData aaPrmtTransData)
		throws RTSException
	{
		csMethod = "delPermitTransaction";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_PRMT_TRANS "
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
						aaPrmtTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getCustSeqNo())));

			if (aaPrmtTransData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaPrmtTransData.getTransTime())));
			}
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_PRMT_TRANS
	 * 
	 * @param  aaPrmtTransData PermitTransactionData	
	 * @throws RTSException
	 */
	public void insPermitTransaction(PermitTransactionData aaPrmtTransData)
		throws RTSException
	{
		csMethod = "insPermitTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_PRMT_TRANS("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransId,"
				+ "CustFstName,"
				+ "CustMIName,"
				+ "CustLstName,"
				+ "CustBsnName,"
				+ "CustSt1,"
				+ "CustSt2,"
				+ "CustCity,"
				+ "CustState,"
				+ "CustCntry,"
				+ "CustZpCd,"
				+ "CustZpCdP4,"
				+ "CustEMail,"
				+ "CustPhone,"
				+ "PrmtNo,"
				+ "ItmCd,"
				+ "AcctItmCd,"
				+ "PrmtPdAmt,"
				+ "EffDate,"
				+ "EffTime,"
				+ "ExpDate,"
				+ "ExpTime,"
				+ "VehBdyType,"
				+ "VehMk,"
				+ "VehMkDesc,"
				+ "VehModlYr,"
				+ "VIN,"
				+ "VehRegCntry,"
				+ "VehRegState,"
				+ "VehRegPltNo,"
				+ "OneTripPrmtOrigPnt,"
				+ "OneTripPrmtPnt1,"
				+ "OneTripPrmtPnt2,"
				+ "OneTripPrmtPnt3,"
				+ "OneTripPrmtDestPnt,"
				+ "MfDwnCd,"
				+ "PrmtIssuanceId,"
				+ "BulkPrmtVendorId)";

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
				+ " ?)";
		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getOfcIssuanceNo())));
			// 2				
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getSubstaId())));
			// 3			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransAMDate())));
			// 4			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransWsId())));
			// 5			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getCustSeqNo())));
			// 6			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransTime())));
			// 7
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getTransCd()));

			// 8
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getTransId()));

			// 9 - CustFstName 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustFstName()));

			// 10 - CustMIName 			
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustMIName()));

			// 11 - CustLstName 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustLstName()));

			// 12	- CustBsnName
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustBsnName()));

			// 13   - CustSt1		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getCustSt1()));

			// 14   - CustSt2		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getCustSt2()));

			// 15   - CustCity		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getCustCity()));

			// 16   - CustState		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustState()));

			// 17   - CustCntry		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustCntry()));

			// 18   - CustZpCd		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getCustZpcd()));

			// 19   - CustZpCdP4		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustZpcdP4()));

			// 20   - CustEMail		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustEMail()));

			// 21   - CustPhone		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getCustPhone()));

			// 22   - PrmtNo		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getPrmtNo()));

			// 23   - ItmCd		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getItmCd()));

			// 24   - AcctItmCd		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getAcctItmCd()));

			// 25   - PrmtPdAmt		
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getPrmtPdAmt())));

			// 26  - EffDate		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getEffDate())));
			// 27  - EffTime		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getEffTime())));

			// 28  - ExpDate		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getExpDate())));

			// 29  - ExpTime		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getExpTime())));

			// 30  - VehBdyType		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getVehBdyType()));

			// 31  - VehMk		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getVehMk()));

			// 31a  - VehMkDesc		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getVehMkDesc()));

			// 32  - VehModlYr		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getVehModlYr())));

			// 33  - VIN		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaPrmtTransData.getVin()));

			// 34  - VehRegCntry		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getVehRegCntry()));

			// 35  - VehRegState		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getVehRegState()));

			// 36  - VehRegPltNo		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getVehRegPltNo()));

			// 37  - OneTripPrmtOrigPnt		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getOneTripPrmtOrigPnt()));

			// 38  - OneTripPrmtPnt1		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getOneTripPrmtPnt1()));

			// 39  - OneTripPrmtPnt2		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getOneTripPrmtPnt2()));

			// 40  - OneTripPrmtPnt3		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getOneTripPrmtPnt3()));

			// 41  - OneTripPrmtDestPnt		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getOneTripPrmtDestPnt()));

			// 42  - ExpTime		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getMfDwnCd())));

			// 43 - PrmtIssuanceId
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getPrmtIssuanceId()));

			// 44 - BulkPrmtVendorId
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaPrmtTransData.getBulkPrmtVendorId()));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + "  - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_PRMT_TRANS for purge
	 *
	 * @param  aiPurgeAMDate int
	 * @return int	
	 * @throws RTSException 
	 */
	public int purgePermitTransaction(int aiPurgeAMDate)
		throws RTSException
	{
		csMethod = "purgePermitTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_PRMT_TRANS  "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query from RTS.RTS_PRMT_TRANS
	 *
	 * @param 	aaPrmtTransData PermitTransactionData	
	 * @return  Vector 
	 * @throws 	RTSException 	
	 */
	public Vector qryPermitTransaction(PermitTransactionData aaPrmtTransData)
		throws RTSException
	{
		csMethod = "qryPermitTransaction";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
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
				+ "C.TransEmpId,"
				+ "A.CustFstName,"
				+ "A.CustMIName,"
				+ "A.CustLstName,"
				+ "A.CustBsnName,"
				+ "A.CustSt1,"
				+ "A.CustSt2,"
				+ "A.CustCity,"
				+ "A.CustState,"
				+ "A.CustZpCd,"
				+ "A.CustZpCdP4,"
				+ "A.CustCntry,"
				+ "A.CustEMail,"
				+ "A.CustPhone,"
				+ "A.PrmtNo,"
				+ "A.ItmCd,"
				+ "A.AcctItmCd,"
				+ "PrmtPdAmt,"
				+ "A.EffDate,"
				+ "A.EffTime,"
				+ "A.ExpDate,"
				+ "A.ExpTime,"
				+ "A.VehBdyType,"
				+ "A.VehMk,"
				+ "A.VehMkDesc,"
				+ "A.VehModlYr,"
				+ "A.VIN,"
				+ "A.VehRegCntry,"
				+ "A.VehRegState,"
				+ "A.VehRegPltNo,"
				+ "A.OneTripPrmtOrigPnt,"
				+ "A.OneTripPrmtPnt1,"
				+ "A.OneTripPrmtPnt2,"
				+ "A.OneTripPrmtPnt3,"
				+ "A.OneTripPrmtDestPnt,"
				+ "A.PrmtIssuanceId, "
				+ "A.MfDwnCd, "
				+ "A.BulkPrmtVendorId, "
				+ "A.VoidedTransIndi "
				+ "FROM RTS.RTS_PRMT_TRANS A, "
				+ "RTS.RTS_TRANS_HDR B, "
				+ "RTS.RTS_TRANS C "
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
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "A.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ "A.SUBSTAID = C.SUBSTAID AND "
				+ "A.TRANSAMDATE = C.TRANSAMDATE AND "
				+ "A.TRANSWSID = C.TRANSWSID AND "
				+ "A.TRANSTIME = C.TRANSTIME AND "
				+ "A.CUSTSEQNO = C.CUSTSEQNO ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransTime())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				PermitTransactionData laPrmtTransData =
					new PermitTransactionData();

				laPrmtTransData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laPrmtTransData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laPrmtTransData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laPrmtTransData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laPrmtTransData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laPrmtTransData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laPrmtTransData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laPrmtTransData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laPrmtTransData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));

				laPrmtTransData.setCustBsnName(
					caDA.getStringFromDB(lrsQry, "CustBsnName"));
				laPrmtTransData.setCustFstName(
					caDA.getStringFromDB(lrsQry, "CustFstName"));
				laPrmtTransData.setCustMIName(
					caDA.getStringFromDB(lrsQry, "CustMIName"));
				laPrmtTransData.setCustLstName(
					caDA.getStringFromDB(lrsQry, "CustLstName"));

				laPrmtTransData.setCustSt1(
					caDA.getStringFromDB(lrsQry, "CustSt1"));
				laPrmtTransData.setCustSt2(
					caDA.getStringFromDB(lrsQry, "CustSt2"));
				laPrmtTransData.setCustCity(
					caDA.getStringFromDB(lrsQry, "CustCity"));
				laPrmtTransData.setCustState(
					caDA.getStringFromDB(lrsQry, "CustState"));
				laPrmtTransData.setCustZpCd(
					caDA.getStringFromDB(lrsQry, "CustZpCd"));
				laPrmtTransData.setCustZpCdP4(
					caDA.getStringFromDB(lrsQry, "CustZpCdP4"));
				laPrmtTransData.setCustCntry(
					caDA.getStringFromDB(lrsQry, "CustCntry"));

				laPrmtTransData.setCustEMail(
					caDA.getStringFromDB(lrsQry, "CustEMail"));
				laPrmtTransData.setCustPhone(
					caDA.getStringFromDB(lrsQry, "CustPhone"));

				laPrmtTransData.setPrmtNo(
					caDA.getStringFromDB(lrsQry, "PrmtNo"));
				laPrmtTransData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laPrmtTransData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laPrmtTransData.setPrmtPdAmt(
					caDA.getDollarFromDB(lrsQry, "PrmtPdAmt"));
				laPrmtTransData.setEffDate(
					caDA.getIntFromDB(lrsQry, "EffDate"));
				laPrmtTransData.setEffTime(
					caDA.getIntFromDB(lrsQry, "EffTime"));
				laPrmtTransData.setExpDate(
					caDA.getIntFromDB(lrsQry, "ExpDate"));
				laPrmtTransData.setExpTime(
					caDA.getIntFromDB(lrsQry, "ExpTime"));
				laPrmtTransData.setVehBdyType(
					caDA.getStringFromDB(lrsQry, "VehBdyType"));
				laPrmtTransData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laPrmtTransData.setVehMkDesc(
					caDA.getStringFromDB(lrsQry, "VehMkDesc"));
				laPrmtTransData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laPrmtTransData.setVin(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laPrmtTransData.setVehRegCntry(
					caDA.getStringFromDB(lrsQry, "VehRegCntry"));
				laPrmtTransData.setVehRegState(
					caDA.getStringFromDB(lrsQry, "VehRegState"));
				laPrmtTransData.setVehRegPltNo(
					caDA.getStringFromDB(lrsQry, "VehRegPltNo"));
				laPrmtTransData.setOneTripPrmtOrigPnt(
					caDA.getStringFromDB(lrsQry, "OneTripPrmtOrigPnt"));
				laPrmtTransData.setOneTripPrmtPnt1(
					caDA.getStringFromDB(lrsQry, "OneTripPrmtPnt1"));
				laPrmtTransData.setOneTripPrmtPnt2(
					caDA.getStringFromDB(lrsQry, "OneTripPrmtPnt2"));
				laPrmtTransData.setOneTripPrmtPnt3(
					caDA.getStringFromDB(lrsQry, "OneTripPrmtPnt3"));
				laPrmtTransData.setOneTripPrmtDestPnt(
					caDA.getStringFromDB(lrsQry, "OneTripPrmtDestPnt"));
				laPrmtTransData.setPrmtIssuanceId(
					caDA.getStringFromDB(lrsQry, "PrmtIssuanceId"));
				laPrmtTransData.setMfDwnCd(
					caDA.getIntFromDB(lrsQry, "MfDwnCd"));
				laPrmtTransData.setBulkPrmtVendorId(
					caDA.getStringFromDB(lrsQry, "BulkPrmtVendorId"));
				// Add element to the Vector
				lvRslt.addElement(laPrmtTransData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query from RTS_PRMT_TRANS, RTS_TRANS, RTS_TRANS_HDR
	 *  for unposted Permit Transactions 
	 * 
	 * @param 	asTransId	
	 * @return  int 
	 * @throws 	RTSException 
	 */
	public int qryUnpostedPermitTransaction(String asTransId)
		throws RTSException
	{
		csMethod = "qryUnpostedPermitTransaction";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
		lsQry.append(
			"SELECT "
				+ "count(*) as COUNTPERMIT "
				+ "FROM RTS.RTS_PRMT_TRANS A, RTS.RTS_TRANS_HDR B, "
				+ "RTS.RTS_TRANS C "
				+ "WHERE B.CUSTNOKEY = C.CUSTNOKEY AND "
				+ "C.TRANSID = A.TRANSID AND "
				+ "B.TRANSTIMESTMP IS NOT NULL AND "
				+ "C.TRANSPOSTEDMFINDI = 0 AND "
				+ "C.VOIDEDTRANSINDI  = 0 AND "
				+ "C.TRANSID = ?");

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(asTransId)));

		try
		{

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			int liCount = 0;
			while (lrsQry.next())
			{
				liCount = caDA.getIntFromDB(lrsQry, "CountPermit");
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");

			return liCount;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_PRMT_TRANS for Void
	 * 
	 * @param aaPrmtTransData	PermitTransactionData
	 * @throws RTSException 
	 */
	public void voidPermitTransaction(PermitTransactionData aaPrmtTransData)
		throws RTSException
	{
		csMethod = "voidPermitTransaction";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_PRMT_TRANS SET "
				+ "VoidedTransIndi = 1 "
				+ "WHERE OfcIssuanceNo = ? and "
				+ "SubstaId = ? and TransAMDate = ? "
				+ "and TransWsId = ? and CustSeqNo = ? and TransTime = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaPrmtTransData.getTransTime())));
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
