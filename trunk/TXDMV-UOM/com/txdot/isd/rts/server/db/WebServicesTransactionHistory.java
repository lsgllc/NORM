package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.data.WebServicesTransactionHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebServicesTransactionHistory.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/25/2010	Created.
 * 							defect 10366 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class provides access to RTS.RTS_SRVC_TRANS_HSTRY 
 *
 * @version	POS_640			03/25/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		03/25/2010 16:00:17
 */
public class WebServicesTransactionHistory
{
	DatabaseAccess caDA;

	String csMethod = new String();

	/**
	 * WebServiceTransactionHistory.java Constructor
	 * 
	 */
	public WebServicesTransactionHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SRVC_TRANS_HSTRY
	 *
	 * @param  aaWebServicesTransactionHistoryData	
	 * @throws RTSException 
	 */
	public void insWebServicesTransactionHistory(WebServicesTransactionHistoryData aaWebServicesTransactionHistoryData)
		throws RTSException
	{
		csMethod = "insWebServicesTransactionHistory";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_SRVC_TRANS_HSTRY("
				+ "SavReqId,"
				+ "TransCd,"
				+ "PltOwnrName1,"
				+ "PltOwnrName2,"
				+ "PltOwnrSt1,"
				+ "PltOwnrSt2,"
				+ "PltOwnrCity,"
				+ "PltOwnrState,"
				+ "PltOwnrZpCd,"
				+ "PltOwnrZpCdP4,"
				+ "PltOwnrEMail,"
				+ "PltOwnrPhone,"
				+ "AuctnPltIndi,"
				+ "ISAIndi,"
				+ "MktngAllowdIndi,"
				+ "PLPIndi,"
				+ "PymntAmt,"
				+ "AddlSetIndi,"
				+ "ItrntPymntStatusCd,"
				+ "PltExpMo,"
				+ "PltExpYr,"
				+ "PltValidityTerm,"
				+ "ResComptCntyNo,"
				+ "SpclRegId,"
				+ "ItrntTraceNo,"
				+ "RegPltNo,"
				+ "MfgPltNo,"
				+ "RegPltCd,"
				+ "OrgNo,"
				+ "PymntOrderId,"
				+ "ResrvReasnCd "
				+ ") VALUES ( "
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
				+ "? )";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getSavReqId())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getTransCd())));

			OwnerData laPltOwnrData =
				aaWebServicesTransactionHistoryData.getPltOwnrData();

			if (laPltOwnrData == null)
			{
				laPltOwnrData = new OwnerData();
			}
			AddressData laPltOwnrAddrData =
				laPltOwnrData.getAddressData();

			if (laPltOwnrAddrData == null)
			{
				laPltOwnrAddrData = new AddressData();
			}

			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnrData.getName1())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnrData.getName2())));

			// 5
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnrAddrData.getSt1())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnrAddrData.getSt2())));
			// 7
			lvValues.addElement(
				new DBValue(Types.CHAR, laPltOwnrAddrData.getCity()));

			// 8
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnrAddrData.getState())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laPltOwnrAddrData.getZpcd())));

			// 10
			lvValues.addElement(
				new DBValue(Types.CHAR, laPltOwnrAddrData.getZpcdp4()));

			// 11
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPltOwnrEMail())));

			// 12
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPltOwnrPhone())));

			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getAuctnPltIndi())));

			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getISAIndi())));
			// 15
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getMktngAllowdIndi())));
			// 16
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPLPIndi())));
			// 17
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPymntAmt())));

			// 18
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getAddlSetIndi())));
			// 19
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getItrntPymntStatusCd())));
			// 20
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPltExpMo())));
			// 21
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPltExpYr())));
			// 22
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPltValidityTerm())));
			// 23
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getResComptCntyNo())));
			// 24
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getSpclRegId())));
			// 25
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getItrntTraceNo())));
			// 26
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getRegPltNo())));
			// 27
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getMfgPltNo())));
			// 28
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getRegPltCd())));
			// 29
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getOrgNo())));
			// 30
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getPymntOrderId())));
			// 31
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebServicesTransactionHistoryData
							.getResrvReasnCd())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
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
	} //END OF INSERT METHOD
}
