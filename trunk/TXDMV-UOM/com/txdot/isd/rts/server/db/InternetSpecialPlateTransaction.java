package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetSpecialPlateTransaction.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/23/2007	Created Class.
 * Jeff S.		06/26/2007	Created a new method used by commonbusiness
 * 							to mark the special plate transaction as
 * 							completed.
 * 							defect 9121 Ver Special Plates
 * Jeff S.		06/29/2007	Added purge methods.
 * 							defect 9121 Ver Special Plates
 * K Harrell	07/17/2007	Adjustments to purge
 * 							modify purgeCompleteTrans(), 
 * 							 purgeIncompleteTrans()
 * 							defect 9085 Ver Special Plates 
 * B Brown		08/22/2007	Add check for regpltcd being a PLP to update
 * 							the PLP indi.			
 * 							modify qryTransactionRecord()
 * 							defect 9119 Ver Special Plates
 * Ray Rowehl	06/12/2008	Clone insert process to handle Vendor
 * 							Plates inserts. 
 * 							add insVPItrntTrans()
 * 							defect 9680 Ver MyPlates_POS
 * B Brown		06/24/2008  Add PLTEXPMO, PLTEXPYR, PLTERM, RESERVEINDI,
 *							and TRANSEMPID to the query and the VP 
 *							insert
 *							modify qryTransactionRecord(),
 *							insVPItrntTrans() 
 *							defect 9711 Ver MyPlates_POS 
 * B Brown		06/27/2008  Change PLTERM, to PLTTERM
 *							modify qryTransactionRecord(),
 *							insVPItrntTrans() 
 *							defect 9711 Ver MyPlates_POS
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeCompleteTrans(), 
 * 								purgeIncompleteTrans() 
 * 							defect 9825 Ver Defect_POS_D   
 * K Harrell	07/02/2009	implement new OwnerData
 * 							modify insActiveSPItrntTrans(), 
 * 							 insVPItrntTrans(), 
 * 							 qryTransactionRecord()  
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F
 * K Harrell	02/17/2010	Implement PltValidityTerm vs. PltTerm
 * 							add AuctnPltIndi, MktngAllowdIndi,
 * 							  ResrvReasnCd 
 * 							add insSPItrntTrans() 
 * 							delete insActiveSPItrntTrans(), 
 * 							 insVPItrntTrans() 
 * 							modify qryTransactionRecord() 
 * 							defect 10366 Ver POS_640
 * K Harrell	03/22/2010	add SpclRegId to Insert, Query 
 * 							modify insSPItrntTrans(),
 * 							  qryTransactionRecord()  
 * 							defect 10366 Ver POS_640 
 * K Harrell	05/06/2010	ReserveIndi to ResrvIndi 
 * 							defect 10480 Ver POS_640      
 * ---------------------------------------------------------------------
 */

/**
 * Class used to handle communucation with the DB table 
 * RTS_ITRNT_SPAPP_TRANS.
 *
 * @version	POS_640   		05/06/2010
 * @author	Jeff Seifert
 * <br>Creation Date:		04/23/2007 09:55:00
 */
public class InternetSpecialPlateTransaction
{
	String csMethod = new String();

	DatabaseAccess caDA;

	/**
	 * InternetSpecialPlateTransaction Constructor
	 * 
	 * @throws RTSException
	 */
	public InternetSpecialPlateTransaction() throws RTSException
	{
		caDA = new DatabaseAccess();
	}

	/**
	 * InternetSpecialPlateTransaction Constructor
	 * 
	 * @param aaDateAccess
	 * @throws RTSException
	 */
	public InternetSpecialPlateTransaction(DatabaseAccess aaDateAccess)
		throws RTSException
	{
		caDA = aaDateAccess;
	}

	/**
	 * Updates the Internet Special Plate Transaction to completed and 
	 * updates the audit trail trans id for the transaction that is in
	 * a pending state for the regpltno that is passed.
	 * 
	 * The only fiels that thos SQL updates is:
	 * 	TRANSSTATUSCD
	 * 	AUDITTRAILTRANSID
	 * 
	 * @param aaSPITransData SpecialPlateItrntTransData
	 * @throws RTSException
	 */
	public void completeSPItrntTrans(SpecialPlateItrntTransData aaSPITransData)
		throws RTSException
	{
		csMethod = "completeSPItrntTrans()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		String lsUpd =
			"UPDATE RTS.RTS_ITRNT_SPAPP_TRANS SET "
				+ "TRANSSTATUSCD=?, "
				+ "AUDITTRAILTRANSID=? "
				+ "WHERE REGPLTNO=? AND TRANSSTATUSCD = ?";
		try
		{
			// Trans Status Code
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_COMPLETED));
			// Audit Trail Trans Id
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSPITransData.getAuditTrailTransId()));
			// Where
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSPITransData.getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_PENDING));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	// defect 10366 
	//	/**
	//	 * Insert Iternet Special Plate Transaction.
	//	 * 
	//	 * @param aaSPITransData SpecialPlateItrntTransData
	//	 * @throws RTSException
	//	 */
	//	public void insActiveSPItrntTrans(SpecialPlateItrntTransData aaSPITransData)
	//		throws RTSException
	//	{
	//		csMethod = "insActiveSPItrntTrans()";
	//
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			csMethod + CommonConstant.SQL_METHOD_BEGIN);
	//
	//		Vector lvValues = new Vector();
	//		String lsIns =
	//			"INSERT INTO RTS.RTS_ITRNT_SPAPP_TRANS("
	//				+ "REGPLTNO,"
	//				+ "TRANSSTATUSCD,"
	//				+ "REGPLTCD,"
	//				+ "ORGNO,"
	//				+ "RESCOMPTCNTYNO,"
	//				+ "PLTOWNRNAME1,"
	//				+ "PLTOWNRNAME2,"
	//				+ "PLTOWNRST1,"
	//				+ "PLTOWNRST2,"
	//				+ "PLTOWNRCITY,"
	//				+ "PLTOWNRSTATE,"
	//				+ "PLTOWNRZPCD,"
	//				+ "PLTOWNRZPCD4,"
	//				+ "PLTOWNRPHONE,"
	//				+ "PLTOWNREMAIL,"
	//				+ "MFGPLTNO,"
	//				+ "ISAINDI,"
	//				+ "ADDLSETINDI,"
	//				+ "PYMNTAMT,"
	//				+ "ITRNTTRACENO,"
	//				+ "ITRNTPYMNTSTATUSCD,"
	//				+ "REQIPADDR,"
	//				+ "INITREQTIMESTMP) VALUES ("
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " ?,"
	//				+ " CURRENT TIMESTAMP)";
	//		try
	//		{
	//			OwnerData laOwnerData = aaSPITransData.getOwnerData();
	//			lvValues.addElement(
	//				new DBValue(Types.CHAR, aaSPITransData.getRegPltNo()));
	//			// All new records are in active status
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					CommonConstants.TRANS_STATUS_ACTIVE));
	//			lvValues.addElement(
	//				new DBValue(Types.CHAR, aaSPITransData.getRegPltCd()));
	//			lvValues.addElement(
	//				new DBValue(Types.VARCHAR, aaSPITransData.getOrgNo()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.SMALLINT,
	//					String.valueOf(
	//						aaSPITransData.getResComptCntyNo())));
	//			// defect 10112 
	//			lvValues.addElement(
	//				new DBValue(Types.VARCHAR, laOwnerData.getName1()));
	//			lvValues.addElement(
	//				new DBValue(Types.VARCHAR, laOwnerData.getName2()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					laOwnerData.getAddressData().getSt1()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					laOwnerData.getAddressData().getSt2()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					laOwnerData.getAddressData().getCity()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					laOwnerData.getAddressData().getState()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					laOwnerData.getAddressData().getZpcd()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					laOwnerData.getAddressData().getZpcdp4()));
	//			// end defect 10112 
	//
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.CHAR,
	//					aaSPITransData.getPltOwnrPhone()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					aaSPITransData.getPltOwnrEmail()));
	//			lvValues.addElement(
	//				new DBValue(Types.CHAR, aaSPITransData.getMfgPltNo()));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.SMALLINT,
	//					String.valueOf(aaSPITransData.isISAIndi())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.SMALLINT,
	//					String.valueOf(aaSPITransData.isAddlSetIndi())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.DECIMAL,
	//					String.valueOf(aaSPITransData.getPymntAmt())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					String.valueOf(aaSPITransData.getItrntTraceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.SMALLINT,
	//					String.valueOf(
	//						aaSPITransData.getItrntPymntStatusCd())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					aaSPITransData.getReqIPAddr()));
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				csMethod + CommonConstant.SQL_BEGIN);
	//
	//			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
	//
	//			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				csMethod
	//					+ CommonConstant.SQL_EXCEPTION
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//		finally
	//		{
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				csMethod + CommonConstant.SQL_METHOD_END);
	//		}
	//
	//	}
	// end defect 10366 

	/**
	 * Insert VP Special Plate Transaction.
	 * 
	 * @param aaSPITransData SpecialPlateItrntTransData
	 * @throws RTSException
	 */
	public void insSPItrntTrans(SpecialPlateItrntTransData aaSPITransData)
		throws RTSException
	{
		csMethod = "insSPItrntTrans()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		// defect 10366
		// Implement PltValidityTerm vs. PltTerm
		// add AuctnPltIndi, MktngAllowdIndi, ResrvReasnCd,
		//    SpclRegId   

		String lsIns =
			"INSERT INTO RTS.RTS_ITRNT_SPAPP_TRANS("
				+ "REGPLTNO,"
				+ "TRANSSTATUSCD,"
				+ "REGPLTCD,"
				+ "ORGNO,"
				+ "RESCOMPTCNTYNO,"
				+ "PLTOWNRNAME1,"
				+ "PLTOWNRNAME2,"
				+ "PLTOWNRST1,"
				+ "PLTOWNRST2,"
				+ "PLTOWNRCITY,"
				+ "PLTOWNRSTATE,"
				+ "PLTOWNRZPCD,"
				+ "PLTOWNRZPCD4,"
				+ "PLTOWNRPHONE,"
				+ "PLTOWNREMAIL,"
				+ "MFGPLTNO,"
				+ "ISAINDI,"
				+ "ADDLSETINDI,"
				+ "PYMNTAMT,"
				+ "ITRNTTRACENO,"
				+ "ITRNTPYMNTSTATUSCD,"
				+ "REQIPADDR,"
				+ "EPAYSENDTIMESTMP, "
				+ "EPAYRCVETIMESTMP, "
				+ "INITREQTIMESTMP, "
				+ "UPDTTIMESTMP, "
				+ "PYMNTORDERID, "
				+ "PLTEXPMO, "
				+ "PLTEXPYR, "
				+ "RESRVINDI, "
				+ "TRANSEMPID, "
				+ "PLTVALIDITYTERM, "
				+ "MKTNGALLOWDINDI,"
				+ "AUCTNPLTINDI, "
				+ "RESRVREASNCD, "
				+ "SPCLREGID "
				+ ") VALUES ("
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

		try
		{
			OwnerData laOwnerData = aaSPITransData.getOwnerData();
			// 1
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSPITransData.getRegPltNo()));
			// 2 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSPITransData.getTransStatusCd()));
			// 3 
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSPITransData.getRegPltCd()));
			// 4
			lvValues.addElement(
				new DBValue(Types.VARCHAR, aaSPITransData.getOrgNo()));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaSPITransData.getResComptCntyNo())));

			// 6
			// defect 10112 
			lvValues.addElement(
				new DBValue(Types.VARCHAR, laOwnerData.getName1()));

			// 7
			lvValues.addElement(
				new DBValue(Types.VARCHAR, laOwnerData.getName2()));

			// 8
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					laOwnerData.getAddressData().getSt1()));

			// 9
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					laOwnerData.getAddressData().getSt2()));

			// 10
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					laOwnerData.getAddressData().getCity()));

			// 11
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					laOwnerData.getAddressData().getState()));

			// 12
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					laOwnerData.getAddressData().getZpcd()));

			// 13
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					laOwnerData.getAddressData().getZpcdp4()));
			// end defect 10112 

			// 14
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSPITransData.getPltOwnrPhone()));

			// 15
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaSPITransData.getPltOwnrEmail()));

			// 16
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSPITransData.getMfgPltNo()));

			// 17
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(aaSPITransData.isISAIndi())));

			// 18
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(aaSPITransData.isAddlSetIndi())));

			// 19
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					String.valueOf(aaSPITransData.getPymntAmt())));

			// 20
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					String.valueOf(aaSPITransData.getItrntTraceNo())));

			// 21
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaSPITransData.getItrntPymntStatusCd())));
			// 22    
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaSPITransData.getReqIPAddr()));
			// 23
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getEpaySendTimeStmp())));
			// 24
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getEpayRcveTimeStmp())));
			// 25
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getInitReqTimeStmp())));
			// 26
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getUpdtTimeStmp())));
			// 27
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					String.valueOf(aaSPITransData.getPymntOrderId())));

			// 28
			// defect 9711
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(aaSPITransData.getPltExpMo())));

			// 29
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(aaSPITransData.getPltExpYr())));
			// 30
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaSPITransData.getFromReserveIndi())));

			// 31
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					String.valueOf(aaSPITransData.getTransEmpID())));
			// end defect 9711

			// 32
			// defect 10366 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaSPITransData.getPltValidityTerm())));

			// 33
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaSPITransData.getMktngAllowdIndi())));
			// 34
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(aaSPITransData.getAuctnPltIndi())));

			// 35
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSPITransData.getResrvReasnCd()));

			// 36
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaSPITransData.getSpclRegId())));
			// end defect 10366 

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}

	}

	/**
	 * Purge Special Plate Internet Trans that are older than the 
	 * number of days passed and have been completed.
	 * 
	 * @param aiDaysOld int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeCompleteTrans(int aiDaysOld) throws RTSException
	{
		csMethod = "purgeCompleteTrans()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		try
		{
			RTSDate laNow = new RTSDate();
			laNow = laNow.add(RTSDate.DATE, -aiDaysOld);
			int liPurgeAMDate = laNow.getAMDate();

			// defect 9085 
			//			String lsSqlDelTrans =
			//				"DELETE FROM RTS.RTS_ITRNT_SPAPP_TRANS WHERE "
			//					+ "(TRANSSTATUSCD = ? AND "
			//					+ "INTEGER(SUBSTR(AUDITTRAILTRANSID,7,5)) < ?) "
			//					+ "OR "
			//					+ "(TRANSSTATUSCD = ? AND "
			//					+ "REGPLTNO IN ("
			//					+ "SELECT REGPLTNO FROM "
			//					+ "RTS.RTS_ITRNT_SPAPP_TRANS WHERE "
			//					+ "TRANSSTATUSCD = ? AND "
			//					+ "INTEGER(SUBSTR(AUDITTRAILTRANSID,7,5)) < ?))";

			String lsSqlDelTrans =
				"DELETE FROM RTS.RTS_ITRNT_SPAPP_TRANS A WHERE "
					+ " EXISTS (SELECT * "
					+ " FROM RTS.RTS_ITRNT_SPAPP_TRANS B "
					+ " WHERE A.REGPLTNO = B.REGPLTNO AND "
					+ " B.TRANSSTATUSCD = ? AND "
					+ "INTEGER(SUBSTR(B.AUDITTRAILTRANSID,7,5)) < ?) ";

			Vector lvValues = new Vector();
			lvValues.add(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_COMPLETED));
			//			lvValues.add(
			//				new DBValue(
			//					Types.INTEGER,
			//					DatabaseAccess.convertToString(aiDaysOld)));
			lvValues.add(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liPurgeAMDate)));
			//			lvValues.add(
			//				new DBValue(
			//					Types.CHAR,
			//					CommonConstants.TRANS_STATUS_INACTIVE));
			//			lvValues.add(
			//				new DBValue(
			//					Types.CHAR,
			//					CommonConstants.TRANS_STATUS_COMPLETED));
			//			lvValues.add(
			//				new DBValue(
			//					Types.INTEGER,
			//					DatabaseAccess.convertToString(aiDaysOld)));
			// end defect 9085 

			// defect 9825 
			// Return number of rows purged 
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelTrans,
					lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{

			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Purge Special Plate Internet Trans that are older than the 
	 * number of days passed and are incomplete.
	 * 
	 * @param aiDaysOld int
	 * @return int 
	 * @throws RTSException
	 */
	public int purgeIncompleteTrans(int aiDaysOld) throws RTSException
	{
		csMethod = "purgeIncompleteTrans()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		try
		{
			// defect 9085 
			//			String lsSqlDelTrans =
			//				"DELETE FROM RTS.RTS_ITRNT_SPAPP_TRANS "
			//					+ "WHERE TRANSSTATUSCD IN (?,?) AND "
			//					+ "INITREQTIMESTMP < (CURRENT TIMESTAMP - ? DAYS) "
			//					+ "AND REGPLTNO NOT IN ("
			//					+ "SELECT REGPLTNO FROM RTS.RTS_ITRNT_SPAPP_TRANS "
			//					+ "WHERE TRANSSTATUSCD IN (?,?))";

			String lsSqlDelTrans =
				"DELETE FROM RTS.RTS_ITRNT_SPAPP_TRANS A "
					+ "WHERE TRANSSTATUSCD IN (?,?) AND "
					+ "INITREQTIMESTMP < (CURRENT TIMESTAMP - ? DAYS) "
					+ "AND NOT EXISTS ( "
					+ "SELECT * FROM RTS.RTS_ITRNT_SPAPP_TRANS B "
					+ "WHERE A.REGPLTNO = B.REGPLTNO AND "
					+ "TRANSSTATUSCD IN (?,?))";

			// end defect 9085 
			Vector lvValues = new Vector();
			lvValues.add(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_INACTIVE));
			lvValues.add(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_ACTIVE));
			lvValues.add(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiDaysOld)));
			lvValues.add(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_PENDING));
			lvValues.add(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_COMPLETED));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelTrans,
					lvValues);
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			return liNumRows;
			// end defect 9825
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Query the Internet Special Plate Transaction records to get the 
	 * Max ReqPltNoReqId field for a plate number.
	 * 
	 * @param asRegPltNo String
	 * @return int
	 * @throws RTSException
	 */
	public int qryMaxReqPltNoReqId(String asRegPltNo)
		throws RTSException
	{
		csMethod = "qryMaxReqPltNoReqId()";
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		String lsSQLQry =
			"SELECT MAX(REGPLTNOREQID) AS MAXREQID FROM "
				+ "RTS.RTS_ITRNT_SPAPP_TRANS WHERE REGPLTNO = ?";
		int liReturnValue = 0;

		try
		{
			lvValues.addElement(new DBValue(Types.CHAR, asRegPltNo));
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
			ResultSet laResultSet =
				caDA.executeDBQuery(lsSQLQry, lvValues);
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			if (laResultSet.next())
			{
				liReturnValue =
					caDA.getIntFromDB(laResultSet, "MAXREQID");
			}
			laResultSet.close();
			return liReturnValue;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryMaxReqPltNoReqId - Exception - "
					+ aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Query the Internet Special Plate Transaction records that match
	 * the trace number provided.
	 * 
	 * @param asTraceNo String
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionRecord(String asTraceNo)
		throws RTSException
	{
		csMethod = "qryTransactionRecord()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		// defect 10366
		// Implement PltValidityTerm vs. PltTerm
		//	add AuctnPltIndi, MktngAllowdIndi 
		String lsSQLQry =
			"SELECT "
				+ "REGPLTNO,"
				+ "REGPLTNOREQID,"
				+ "TRANSSTATUSCD,"
				+ "REGPLTCD,"
				+ "ORGNO,"
				+ "RESCOMPTCNTYNO,"
				+ "PLTOWNRNAME1,"
				+ "PLTOWNRNAME2,"
				+ "PLTOWNRST1,"
				+ "PLTOWNRST2,"
				+ "PLTOWNRCITY,"
				+ "PLTOWNRSTATE,"
				+ "PLTOWNRZPCD,"
				+ "PLTOWNRZPCD4,"
				+ "PLTOWNRPHONE,"
				+ "PLTOWNREMAIL,"
				+ "MFGPLTNO,"
				+ "ISAINDI,"
				+ "ADDLSETINDI,"
				+ "PYMNTAMT,"
				+ "AUDITTRAILTRANSID,"
				+ "ITRNTTRACENO,"
				+ "ITRNTPYMNTSTATUSCD,"
				+ "PYMNTORDERID,"
				+ "REQIPADDR,"
				+ "INITREQTIMESTMP,"
				+ "UPDTTIMESTMP, "
				+ "PLTEXPMO, "
				+ "PLTEXPYR, "
				+ "RESRVINDI, "
				+ "TRANSEMPID, "
				+ "PLTVALIDITYTERM, "
				+ "MKTNGALLOWDINDI,"
				+ "AUCTNPLTINDI, "
				+ "RESRVREASNCD, "
				+ "SPCLREGID "
				+ "FROM "
				+ "RTS.RTS_ITRNT_SPAPP_TRANS WHERE ITRNTTRACENO = ?";
		// end defect 10366 

		Vector lvData = new Vector();
		try
		{
			lvValues.addElement(new DBValue(Types.VARCHAR, asTraceNo));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsSQLQry, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				SpecialPlateItrntTransData laSPTransData =
					new SpecialPlateItrntTransData();
				laSPTransData.setRegPltNo(
					caDA.getStringFromDB(laResultSet, "REGPLTNO"));
				laSPTransData.setTransStatusCd(
					caDA.getStringFromDB(laResultSet, "TRANSSTATUSCD"));
				laSPTransData.setRegPltCd(
					caDA.getStringFromDB(laResultSet, "REGPLTCD"));
				// defect 9119	
				if (laSPTransData.getRegPltCd().indexOf("PLP") > -1)
				{
					laSPTransData.setPLPIndi(1);
				}
				// end defect 9119
				laSPTransData.setOrgNo(
					caDA.getStringFromDB(laResultSet, "ORGNO"));
				laSPTransData.setResComptCntyNo(
					caDA.getIntFromDB(laResultSet, "RESCOMPTCNTYNO"));
				OwnerData laOwnerData = new OwnerData();

				// defect 10112 
				laOwnerData.setName1(
					caDA.getStringFromDB(laResultSet, "PLTOWNRNAME1"));
				laOwnerData.setName2(
					caDA.getStringFromDB(laResultSet, "PLTOWNRNAME2"));
				// end defect 10112 

				AddressData laAddressData = new AddressData();
				laAddressData.setSt1(
					caDA.getStringFromDB(laResultSet, "PLTOWNRST1"));
				laAddressData.setSt2(
					caDA.getStringFromDB(laResultSet, "PLTOWNRST2"));
				laAddressData.setCity(
					caDA.getStringFromDB(laResultSet, "PLTOWNRCITY"));
				laAddressData.setState(
					caDA.getStringFromDB(laResultSet, "PLTOWNRSTATE"));
				laAddressData.setZpcd(
					caDA.getStringFromDB(laResultSet, "PLTOWNRZPCD"));
				laAddressData.setZpcdp4(
					caDA.getStringFromDB(laResultSet, "PLTOWNRZPCD4"));

				// defect 10112 
				laOwnerData.setAddressData(laAddressData);
				// end defect 10112 

				laSPTransData.setOwnerData(laOwnerData);
				laSPTransData.setPltOwnrPhone(
					caDA.getStringFromDB(laResultSet, "PLTOWNRPHONE"));
				laSPTransData.setPltOwnrEmail(
					caDA.getStringFromDB(laResultSet, "PLTOWNREMAIL"));
				laSPTransData.setMfgPltNo(
					caDA.getStringFromDB(laResultSet, "MFGPLTNO"));
				laSPTransData.setISAIndi(
					caDA.getIntFromDB(laResultSet, "ISAINDI"));
				laSPTransData.setAddlSetIndi(
					caDA.getIntFromDB(laResultSet, "ADDLSETINDI"));
				laSPTransData.setPymntAmt(
					caDA.getDoubleFromDB(laResultSet, "PYMNTAMT"));
				laSPTransData.setAuditTrailTransId(
					caDA.getStringFromDB(
						laResultSet,
						"AUDITTRAILTRANSID"));
				laSPTransData.setItrntTraceNo(
					caDA.getStringFromDB(laResultSet, "ITRNTTRACENO"));
				laSPTransData.setItrntPymntStatusCd(
					caDA.getIntFromDB(
						laResultSet,
						"ITRNTPYMNTSTATUSCD"));
				laSPTransData.setPymntOrderId(
					caDA.getStringFromDB(laResultSet, "PYMNTORDERID"));
				laSPTransData.setReqIPAddr(
					caDA.getStringFromDB(laResultSet, "REQIPADDR"));
				laSPTransData.setInitReqTimeStmp(
					caDA.getRTSDateFromDB(
						laResultSet,
						"INITREQTIMESTMP"));
				laSPTransData.setUpdtTimeStmp(
					caDA.getRTSDateFromDB(laResultSet, "UPDTTIMESTMP"));

				// defect 9711
				laSPTransData.setPltExpMo(
					caDA.getIntFromDB(laResultSet, "PLTEXPMO"));
				laSPTransData.setPltExpYr(
					caDA.getIntFromDB(laResultSet, "PLTEXPYR"));
				laSPTransData.setFromReserveIndi(
					caDA.getIntFromDB(laResultSet, "RESRVINDI"));
				laSPTransData.setTransEmpID(
					caDA.getStringFromDB(laResultSet, "TRANSEMPID"));
				// end defect 9711	
				InternetSpecialPlateTransactionFees laIntrntTransFeesAccess =
					new InternetSpecialPlateTransactionFees(caDA);
				laSPTransData.setFeesData(
					laIntrntTransFeesAccess.qrySPItrntTransFees(
						caDA.getIntFromDB(laResultSet, "REGPLTNOREQID"),
						laSPTransData.getRegPltNo()));

				// defect 10366 
				laSPTransData.setPltValidityTerm(
					caDA.getIntFromDB(laResultSet, "PLTVALIDITYTERM"));
				laSPTransData.setAuctnPltIndi(
					caDA.getIntFromDB(laResultSet, "AUCTNPLTINDI"));
				laSPTransData.setMktngAllowdIndi(
					caDA.getIntFromDB(laResultSet, "MKTNGALLOWDINDI"));
				laSPTransData.setResrvReasnCd(
					caDA.getStringFromDB(laResultSet, "RESRVREASNCD"));
				laSPTransData.setSpclRegId(
					caDA.getLongFromDB(laResultSet, "SPCLREGID"));
				// end defect 10366 

				lvData.add(laSPTransData);
			}
			laResultSet.close();
			return lvData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * This method verifies a record exists for the RegPltNo passed.
	 * 
	 * @param asRegPltNo String
	 * @throws RTSException
	 */
	public boolean recordExists(String asRegPltNo) throws RTSException
	{
		csMethod = "recordExists()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		boolean lbRecordExists = false;

		String lsSQLQry =
			"SELECT REGPLTNO FROM RTS.RTS_ITRNT_SPAPP_TRANS "
				+ "WHERE REGPLTNO=?";

		Vector lvValues = new Vector();
		try
		{
			lvValues.addElement(new DBValue(Types.CHAR, asRegPltNo));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsSQLQry, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			if (laResultSet.next())
			{
				lbRecordExists = true;
			}

			laResultSet.close();
			return lbRecordExists;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Update Iternet Special Plate Transaction for active transactions
	 * that match the regplateno passed.
	 * 
	 * The only fiels that thos SQL updates is:
	 * 	PYMNTORDERID
	 * 	ITRNTPYMNTSTATUSCD
	 * 	EPAYSENDTIMESTMP
	 * 	EPAYRCVETIMESTMP
	 * 
	 * @param aaSPITransData SpecialPlateItrntTransData
	 * @param asTransStatusCd String
	 * @throws RTSException
	 */
	public void updActiveSPItrntTrans(
		SpecialPlateItrntTransData aaSPITransData,
		String asTransStatusCd,
		boolean abMQUpdate)
		throws RTSException
	{
		csMethod = "updActiveSPItrntTrans()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		String lsUpd =
			"UPDATE RTS.RTS_ITRNT_SPAPP_TRANS SET "
				+ "TRANSSTATUSCD=?, "
				+ "PYMNTORDERID=?, "
				+ "ITRNTPYMNTSTATUSCD=?, "
				+ "EPAYSENDTIMESTMP=?, "
				+ "EPAYRCVETIMESTMP=?, "
				+ "UPDTTIMESTMP=?, "
				+ "MQUPDTINDI=? "
				+ "WHERE REGPLTNO=? AND TRANSSTATUSCD = ?";
		try
		{
			// Trans Status Code
			lvValues.addElement(
				new DBValue(Types.CHAR, asTransStatusCd));
			// Payment Order Id
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					String.valueOf(aaSPITransData.getPymntOrderId())));
			// Payment Status Code
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaSPITransData.getItrntPymntStatusCd())));
			// Epay Send and Receive TimeStamps
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getEpaySendTimeStmp())));
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getEpayRcveTimeStmp())));

			// Update Timestamp
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSPITransData.getUpdtTimeStmp())));

			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf((abMQUpdate) ? 1 : 0)));

			// Where
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSPITransData.getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					CommonConstants.TRANS_STATUS_ACTIVE));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Update all Internet Special Plate Transaction records to 
	 * transstatuscd of the "To Status Code" that match the regpltno 
	 * passed and the "From Status Code".
	 * 
	 * @param asRegPltNo String
	 * @param asFromTransStatusCd String
	 * @param asToTransStatusCd String
	 * @throws RTSException
	 */
	public void updTransStatusCd(
		String asRegPltNo,
		String asFromTransStatusCd,
		String asToTransStatusCd)
		throws RTSException
	{
		csMethod = "updTransStatusCd()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		String lsUpd =
			"UPDATE RTS.RTS_ITRNT_SPAPP_TRANS "
				+ "SET TRANSSTATUSCD = ? "
				+ "WHERE REGPLTNO = ? "
				+ "AND TRANSSTATUSCD = ?";
		try
		{
			lvValues.addElement(
				new DBValue(Types.CHAR, asToTransStatusCd));
			lvValues.addElement(new DBValue(Types.CHAR, asRegPltNo));
			lvValues.addElement(
				new DBValue(Types.CHAR, asFromTransStatusCd));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}
}
