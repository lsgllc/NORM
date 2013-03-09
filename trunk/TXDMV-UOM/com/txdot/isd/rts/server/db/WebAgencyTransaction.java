package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.webservices.bat.data.RtsBatchListSummaryLine;

import com.txdot.isd.rts.services.data.WebAgencyTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyTransaction.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/10/2011	Created 
 * 							defect 10708 Ver 6.7.0 
 * Ray Rowehl	02/14/2011	Limit size on ReqIpAddr.  Seems we only made
 * 							it 15 bytes instead of 30.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/20/2011	Add qry to get request count.
 * 							add qryWebAgencyTransactionCnt()
 * 							defect 10673 Ver 6.7.0
 * K Harrell	03/14/2011	Substitute TransTimestmp w/ InitReqTimestmp 
 * 							modify insWebAgencyTransaction() 
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	03/17/2011	add accptWebAgencyTransaction()
 * 							defect 10769 Ver 6.7.1 
 * K Harrell	03/21/2011	add PltAge 
 * 							defect 10768 Ver 6.7.1
 * K Harrell	03/25/2011	add qryWebAgencyTransactionSum()
 * 							defect 10785 Ver 6.7.1 
 * Ray Rowehl	03/28/2011	Rename of ReprintCnt.	
 * 							modify qryWebAgencyTransactionSum()
 * 							defect 10673 Ver 6.7.1 
 * K Harrell	03/28/2011	remove CitationIndi, ReqIPAddr; add 
 * 							WebSessionId 
 * 							modify insWebAgencyTransaction(), 
 * 						 	 qryWebAgencyTransaction() 
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	03/31/2011	add qryWebAgenctTransaction
 * 							 (WebAgencyTransactionData,boolean, boolean)
 * 							defect 10768 Ver 6.7.1  
 * K Harrell	04/28/2011	modify accptWebAgencyTransaction()
 * 							defect 10768 Ver 6.7.1
 * K McKee      11/02/2011  modify qryWebAgencyTransaction()
 * 							added UserName and AgntIdntyNo
 * 							modified the joined tables in the where clause
 * 							defect 11145 Ver 6.9.0
 * K Harrell	11/05/2011	modify insWebAgencyTransaction(), 
 * 							qryWebAgenctTransaction
 * 							(WebAgencyTransactionData,boolean, boolean)
 * 							defect 11137 Ver 6.9.0 
 * K McKee      12/30/2011  add qryWebAgencyTransactionForPlate()
 * 							defect 11239 Ver 6.10.0
 * D Hamilton   01/12/2012  modify the SQL update statement to ignore 
 *                          similar records that have a different renewal
 *                          month/year combination.
 *                          modify accptWebAgencyTransaction()
 *                          defect 11281 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Provides methods to access RTS_WEB_AGNCY_TRANS
 *
 * @version	6.10.0 			01/12/2012
 * @author	Kathy Harrell
 * @since 					01/10/2011 13:19:17
 */

public class WebAgencyTransaction
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyTransaction Constructor
	 * 
	 */
	public WebAgencyTransaction(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to update RTS.RTS_WEB_AGNCY_TRANS when Vehicle Accepted 
	 * 
	 * @param  aiSavReqId  
	 * @param aiPrntQty   
	 * @param asNewInvItmNo 
	 * @param aaAccptTstmp 	
	 * @return int
	 * @throws RTSException 
	 */
	public int accptWebAgencyTransaction(
		int aiSavReqId,
		int aiPrntQty,
		String asNewInvItmNo,
		RTSDate aaAccptTstmp)
		throws RTSException
	{
		csMethod = "accptWebAgencyTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();
		String lsAccptTstmp = "'" + aaAccptTstmp.getDB2Date() + "'";
		
		// TODO - Checking for previously accepted transaction.
		//        Final acceptance should fail. 
		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_TRANS A SET "
					+ " AccptVehIndi = 1 , "
					+ " PrntQty = ?, "
					+ " InvItmNo = ?, "
					+ " AccptTimestmp = " 
					+ lsAccptTstmp 
					+ " where "
					+ " SavReqId = ? "
					+ " and AccptVehIndi = 0 and "
					+ " not exists (select * from "
					+ " RTS.RTS_WEB_AGNCY_TRANS B where "
					+ " A.RegPltNo = B.RegPltNo and "
					+ " A.DocNo = B.DocNo and "
					// defect 11281
					+ " (A.RegExpMo = B.RegExpMo and "
					+ " A.RegExpYr = B.RegExpYr) and "
					// end defect 11281
					+ " B.AccptVehIndi = 1 and "
					+ " B.AgncyVoidIndi = 0 and "
					+ " B.CntyVoidIndi = 0)";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPrntQty)));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(asNewInvItmNo)));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSavReqId)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Method to update RTS.RTS_WEB_AGNCY_TRANS for Void, Reprint  
	 * 
	 * @param  aiSavReqId
	 * @param asUpdtStmt   
	 * @throws RTSException 
	 */
	public int voidReprintWebAgencyTransaction(
		int aiSavReqId,
		String asUpdtStmt)
		throws RTSException
	{
		csMethod = "voidReprintWebAgencyTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_TRANS SET "
					+ asUpdtStmt
					+ " where "
					+ " SavReqId = ?";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSavReqId)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY_TRANS 
	 *
	 * @param  aaData	WebAgencyTransactionData
	 * @throws RTSException 
	 */
	public void insWebAgencyTransaction(WebAgencyTransactionData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyTransaction";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");
		String lsValues = new String();

		// defect 11137 
		for (int i = 0; i < 43; i++)
		{
			// end defect 11137 
			lsValues = lsValues + "?,";
		}

		// defect 11137 
		// add  OwnrTtlName1,OwnrTtlName2,RecpntName 
		
		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_TRANS("
				+ "AccptVehIndi,"
				+ "AddlSetIndi,"
				+ "AgncyBatchIdntyNo,"
				+ "AgncyVoidIndi,"
				+ "AgntSecrtyIdntyNo,"
				+ "CntyVoidIndi,"
				+ "MustReplPltIndi,"
				+ "NewPltExpMo,"
				+ "NewPltExpYr,"
				+ "NewRegExpMo,"
				+ "NewRegExpYr,"
				+ "PltAge,"
				+ "PltBirthDate,"
				+ "PltExpMo,"
				+ "PltExpYr,"
				+ "PltValidityTerm,"
				+ "PrntQty,"
				+ "RegClassCd,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "ResComptCntyNo,"
				+ "SavReqId,"
				+ "SubconId,"
				+ "VehModlYr,"
				+ "AuditTrailTransId,"
				+ "BarCdVersionNo,"
				+ "DocNo,"
				+ "InsVerfdCd,"
				+ "InvItmNo,"
				+ "KeyTypeCd,"
				+ "OrgNo,"
				+ "RegPltCd,"
				+ "RegPltNo,"
				+ "WebSessionId,"
				+ "StkrItmCd,"
				+ "TransId,"
				+ "VehGrossWt,"
				+ "VehMk,"
				+ "VehModl,"
				+ "VIN,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "RecpntName," 
				+ "InitReqTimeStmp)"
				+ " VALUES ( "
				+ lsValues
				+ "CURRENT TIMESTAMP)";
		// end defect 11137 
		
		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAccptVehIndi())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAddlSetIndi())));
			// 3 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyBatchIdntyNo())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyVoidIndi())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntSecrtyIdntyNo())));

			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getCntyVoidIndi())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getMustReplPltIndi())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getNewPltExpMo())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getNewPltExpYr())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getNewRegExpMo())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getNewRegExpYr())));

			// 12
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPltAge())));
			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPltBirthDate())));
			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPltExpMo())));
			// 15
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPltExpYr())));
			// 16
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPltValidityTerm())));
			// 17
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPrntQty())));
			// 18
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRegClassCd())));
			// 19
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRegExpMo())));
			// 20
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRegExpYr())));
			// 21
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getResComptCntyNo())));
			// 22
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSavReqId())));
			// 23
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSubconId())));
			// 24
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getVehModlYr())));

			// 25
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getAuditTrailTransId())));
			// 26
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getBarCdVersionNo())));
			// 27
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getDocNo())));
			// 28
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getInsVerfdCd())));
			// 29
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getInvItmNo())));
			// 30
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getKeyTypeCd(),
						false)));
			// 31	
			String lsOrgNo =
				UtilityMethods.isEmpty(aaData.getOrgNo())
					? "  "
					: aaData.getOrgNo();

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsOrgNo, false)));
			// 32
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRegPltCd())));
			// 33
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getRegPltNo())));
			// 34
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getWebSessionId())));

			// 35
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getStkrItmCd())));

			// 36
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getTransId())));
			// 37
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getVehGrossWt())));
			// 38
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getVehMk())));
			// 39
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getVehModl())));
			// 40  
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getVIN())));
			
			// defect 11137 
			// 41  
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getOwnrTtlName1())));
			// 42  
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getOwnrTtlName2())));
			// 43  
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getRecpntName())));
			// end defect 11137 

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

	/**
	 * Purge RTS_WEB_AGNCY_TRANS
	 * 
	 * @param aiPurgeDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeWebAgencyTransaction(int aiPurgeDays)
		throws RTSException
	{
		csMethod = "purgeWebAgencyTransaction";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelData =
			"DELETE FROM RTS.RTS_WEB_AGNCY_TRANS "
				+ " WHERE "
				+ " TRANSTIMESTMP "
				+ " < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();

		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeDays)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return liNumRows;
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
	 * Method to Query RTS_WEB_AGNCY_TRANS
	 * 		
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyTransaction(
		WebAgencyTransactionData aaWATransData,
		boolean abAccptVeh)
		throws RTSException
	{
		return qryWebAgencyTransaction(
			aaWATransData,
			abAccptVeh,
			false);
	}

	/**
	 * Method to Query RTS_WEB_AGNCY_TRANS
	 * 		
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyTransaction(
		WebAgencyTransactionData aaWATransData,
		boolean abAccptVeh,
		boolean abIncludeVoid)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
		
		// defect 11145  

		lsQry.append(
			"SELECT "
				+ "RescomptCntyNo,"
				+ "A.AgncyBatchIdntyNo,"
				+ "WebSessionId,"
				+ "BarCdVersionNo,"
				+ "VIN,"
				+ "DocNo,"
				+ "VehGrossWt,"
				+ "VehMk,"
				+ "VehModl,"
				+ "VehModlYr,"
				+ "RegPltNo,"
				+ "RegClassCd,"
				+ "RegPltCd,"
				+ "StkrItmCd,"
				+ "AuditTrailTransId,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "NewRegExpMo,"
				+ "NewRegExpYr,"
				+ "InvItmNo,"
				+ "OrgNo,"
				+ "AddlSetIndi,"
				+ "PltValidityTerm,"
				+ "PltBirthDate,"
				+ "PltAge,"
				+ "PltExpMo,"
				+ "PltExpYr,"
				+ "PrntQty,"
				+ "NewPltExpMo,"
				+ "NewPltExpYr,"
				+ "PrntQty,"
				+ "InsVerfdCd,"
				+ "AgncyVoidIndi,"
				+ "CntyVoidIndi,"
				+ "MustReplPltIndi,"
				+ "KeyTypeCd,"
				+ "BatchStatusCd,"
				+ "AccptVehIndi,"
				+ "A.AgntSecrtyIdntyNo,"
				+ "SubconId,"
				+ "SavReqId,"
				+ "TransId,"
				+ "InitReqTimestmp, "
				// defect 11145 
				+ "UserName, "
				+ "D.AgntIdntyNo, "
				// end defect 11145 
				+ "AccptTimestmp, "
				// defect 11137 
				+ "OwnrTtlName1, "
				+ "OwnrTtlName2, "
				+ "RecpntName "
				// end defect 11137 
				// defect 11145
				+ "from RTS.RTS_WEB_AGNCY_TRANS A, "
				+ " RTS.RTS_WEB_AGNCY_BATCH B,"  
				+ " RTS.RTS_WEB_AGNT_SECURITY C," 
				+ " RTS.RTS_WEB_AGNT D " 	  
				+ " WHERE "
				+ " A.AgncyBatchIdntyNo = B.AgncyBatchIdntyNo and " 
				+ " A.AgntSecrtyIdntyNo = C.AgntSecrtyIdntyNo and "
				+ " C.AgntIdntyNo = D.AgntIdntyNo and ");
		       // end defect 11145
			;

		if (aaWATransData.getAgncyBatchIdntyNo() != 0)
		{
			lsQry.append(" A.AGNCYBATCHIDNTYNO = ? ");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWATransData.getAgncyBatchIdntyNo())));
		}
		else
		{
			lsQry.append(" SAVREQID = ? ");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWATransData.getSavReqId())));
		}

		lsQry.append(" AND ACCPTVEHINDI = " + (abAccptVeh ? "1" : "0"));

		if (!abIncludeVoid)
		{
			lsQry.append(" AND AGNCYVOIDINDI = 0 AND CNTYVOIDINDI = 0");
		}

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyTransactionData laData =
					new WebAgencyTransactionData();

				laData.setAccptVehIndi(
					caDA.getIntFromDB(lrsQry, "AccptVehIndi"));
				laData.setAgncyBatchIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyBatchIdntyNo"));
				laData.setAddlSetIndi(
					caDA.getIntFromDB(lrsQry, "AddlSetIndi"));
				laData.setAgncyVoidIndi(
					caDA.getIntFromDB(lrsQry, "AgncyVoidIndi"));
				laData.setAgntSecrtyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo"));
				laData.setCntyVoidIndi(
					caDA.getIntFromDB(lrsQry, "CntyVoidIndi"));
				laData.setMustReplPltIndi(
					caDA.getIntFromDB(lrsQry, "MustReplPltIndi"));
				laData.setNewPltExpMo(
					caDA.getIntFromDB(lrsQry, "NewPltExpMo"));
				laData.setNewPltExpYr(
					caDA.getIntFromDB(lrsQry, "NewPltExpYr"));
				laData.setNewRegExpMo(
					caDA.getIntFromDB(lrsQry, "NewRegExpMo"));
				laData.setNewRegExpYr(
					caDA.getIntFromDB(lrsQry, "NewRegExpYr"));
				laData.setPltAge(
					caDA.getIntFromDB(lrsQry, "PltAge"));
				laData.setPltBirthDate(
					caDA.getIntFromDB(lrsQry, "PltBirthDate"));
				laData.setPltExpMo(
					caDA.getIntFromDB(lrsQry, "PltExpMo"));
				laData.setPltExpYr(
					caDA.getIntFromDB(lrsQry, "PltExpYr"));
				laData.setPltValidityTerm(
					caDA.getIntFromDB(lrsQry, "PltValidityTerm"));
				laData.setPrntQty(
					caDA.getIntFromDB(lrsQry, "PrntQty"));
				laData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laData.setRegExpMo(
					caDA.getIntFromDB(lrsQry, "RegExpMo"));
				laData.setRegExpYr(
					caDA.getIntFromDB(lrsQry, "RegExpYr"));
				laData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
				laData.setSavReqId(
					caDA.getIntFromDB(lrsQry, "SavReqId"));
				laData.setSubconId(
					caDA.getIntFromDB(lrsQry, "SubconId"));
				laData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laData.setVehGrossWt(
					caDA.getIntFromDB(lrsQry, "VehGrossWt"));
				laData.setAuditTrailTransId(
					caDA.getStringFromDB(lrsQry, "AuditTrailTransId"));
				laData.setBarCdVersionNo(
					caDA.getStringFromDB(lrsQry, "BarCdVersionNo"));
				laData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laData.setInsVerfdCd(
					caDA.getStringFromDB(lrsQry, "InsVerfdCd"));
				laData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laData.setKeyTypeCd(
					caDA.getStringFromDB(lrsQry, "KeyTypeCd"));
				laData.setBatchStatusCd(
					caDA.getStringFromDB(lrsQry, "BatchStatusCd"));
				laData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "OrgNo"));
				laData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laData.setWebSessionId(
					caDA.getStringFromDB(lrsQry, "WebSessionId"));
				laData.setStkrItmCd(
					caDA.getStringFromDB(lrsQry, "StkrItmCd"));
				laData.setTransId(
					caDA.getStringFromDB(lrsQry, "TransId"));
				laData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laData.setVehModl(
					caDA.getStringFromDB(lrsQry, "VehModl"));
				laData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laData.setInitReqTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "InitReqTimestmp"));
				laData.setAccptTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "AccptTimestmp"));

				// defect 11145
				laData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));
				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));
				// end defect 11145

				// defect 11137 
				laData.setOwnrTtlName1(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));
				laData.setOwnrTtlName2(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));
				laData.setRecpntName(
						caDA.getStringFromDB(lrsQry, "RecpntName"));
				// end defect 11137 
				// Add element to the Vector
				lvRslt.addElement(laData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Get the Count of Requests for a Batch.
	 * 
	 * <p>This excludes voids.
	 * 		
	 * @throws 	RTSException 
	 */
	public int qryWebAgencyTransactionCnt(int aiAgncyBatchIdntyNo)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransactionCnt";

		int liReqCt = 0;

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "Count(*) as ReqCount "
				+ "from "
				+ " RTS.RTS_WEB_AGNCY_TRANS A "
				+ " WHERE "
				+ "AGNCYBATCHIDNTYNO = ? "
				+ "AND AGNCYVOIDINDI = 0 "
				+ "AND CNTYVOIDINDI = 0 "
				+ "AND ACCPTVEHINDI = 1 ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiAgncyBatchIdntyNo)));
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			while (lrsQry.next())
			{
				liReqCt = caDA.getIntFromDB(lrsQry, "ReqCount");
				break;
			} //End of While

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return liReqCt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
	/**
	 * Get the Count of Requests for a Batch.
	 * 
	 * <p>This excludes voids.
	 * 		
	 * @throws 	RTSException 
	 */
	public void qryWebAgencyTransactionSum(RtsBatchListSummaryLine aaBLSL)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransactionSum";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry1 = new StringBuffer();
		StringBuffer lsQry2 = new StringBuffer();
		StringBuffer lsQry3 = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// TODO Make this ONE SQL w/ Coalesce ??? (KPH) 

		lsQry1.append(
			"SELECT "
				+ "Count(*) as ReqCount "
				+ "from "
				+ "RTS.RTS_WEB_AGNCY_TRANS A "
				+ "WHERE "
				+ "AGNCYBATCHIDNTYNO = ? "
				+ "AND AGNCYVOIDINDI = 0 "
				+ "AND CNTYVOIDINDI = 0 "
				+ "AND ACCPTVEHINDI = 1 ");

		lsQry2.append(
			"SELECT "
				+ "SUM(PRNTQTY - 1) as ReprntCount "
				+ "from "
				+ "RTS.RTS_WEB_AGNCY_TRANS A, "
				+ "RTS.RTS_ITEM_CODES B "
				+ "WHERE "
				+ "A.STKRITMCD = B.ITMCD AND "
				+ "B.PRINTABLEINDI = 1 AND "
				+ "A.PRNTQTY !=0 "
				+ "AND AGNCYBATCHIDNTYNO = ? "
				+ "AND ACCPTVEHINDI = 1 ");

		lsQry3.append(
			"SELECT "
				+ "Count(*) as VoidCount "
				+ "from "
				+ "RTS.RTS_WEB_AGNCY_TRANS A "
				+ "WHERE "
				+ "AGNCYBATCHIDNTYNO = ? "
				+ "AND (AGNCYVOIDINDI != 0 "
				+ "OR CNTYVOIDINDI != 0)  "
				+ "AND ACCPTVEHINDI = 1 ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBLSL.getAgncyBatchIdntyNo())));
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry1.toString(), lvValues);

			while (lrsQry.next())
			{
				aaBLSL.setRenewalRequestCnt(
					caDA.getIntFromDB(lrsQry, "ReqCount"));
				break;
			} //End of While

			lrsQry = caDA.executeDBQuery(lsQry2.toString(), lvValues);

			while (lrsQry.next())
			{
				aaBLSL.setReprintCnt(
					caDA.getIntFromDB(lrsQry, "ReprntCount"));
				break;
			} //End of While

			lrsQry = caDA.executeDBQuery(lsQry3.toString(), lvValues);

			while (lrsQry.next())
			{
				aaBLSL.setVoidCnt(
					caDA.getIntFromDB(lrsQry, "VoidCount"));
				break;
			} //End of While

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_WEB_AGNCY_TRANS 
	 * 
	 * @param  aaData WebAgencyTransactionData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgencyTransaction(WebAgencyTransactionData aaData)
		throws RTSException
	{
		csMethod = "updWebAgencyTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_TRANS SET "
					+ " AgncyVoidIndi = ?, "
					+ " CntyVoidIndi = ?,"
					+ " PrntQty = ?,"
					+ " TransId = ?, "
					+ " Transtimestmp = Current Timestamp "
					+ " where "
					+ " SavReqId = ?";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyVoidIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getCntyVoidIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getPrntQty())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getTransId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSavReqId())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");

			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
	
	/**
	 * Method to Query transactions for a specific plate number
	 * @param   aaWATransData  WebAgencyTransactionData
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyTransactionForPlate(
		WebAgencyTransactionData aaWATransData)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransactionForPlate";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
	
		lsQry.append(
			"SELECT "
				+ " RescomptCntyNo,"
				+ " A.AgncyBatchIdntyNo,"
				+ " WebSessionId,"
				+ " BarCdVersionNo,"
				+ " VIN,"
				+ " DocNo,"
				+ " VehGrossWt,"
				+ " VehMk,"
				+ " VehModl,"
				+ " VehModlYr,"
				+ " RegPltNo,"
				+ " RegClassCd,"
				+ " RegPltCd,"
				+ " StkrItmCd,"
				+ " AuditTrailTransId,"
				+ " RegExpMo,"
				+ " RegExpYr,"
				+ " NewRegExpMo,"
				+ " NewRegExpYr,"
				+ " InvItmNo,"
				+ " OrgNo,"
				+ " AddlSetIndi,"
				+ " PltValidityTerm,"
				+ " PltBirthDate,"
				+ " PltAge,"
				+ " PltExpMo,"
				+ " PltExpYr,"
				+ " PrntQty,"
				+ " NewPltExpMo,"
				+ " NewPltExpYr,"
				+ " PrntQty,"
				+ " InsVerfdCd,"
				+ " AgncyVoidIndi,"
				+ " CntyVoidIndi,"
				+ " MustReplPltIndi,"
				+ " KeyTypeCd,"
				+ " BatchStatusCd,"
				+ " AccptVehIndi,"
				+ " A.AgntSecrtyIdntyNo,"
				+ " SubconId,"
				+ " SavReqId,"
				+ " TransId,"
				+ " InitReqTimestmp, "
				+ " UserName, "
				+ " D.AgntIdntyNo, "
				+ " AccptTimestmp, "
				+ " OwnrTtlName1, "
				+ " OwnrTtlName2, "
				+ " RecpntName "
				+ " FROM RTS.RTS_WEB_AGNCY_TRANS A, "
				+ " RTS.RTS_WEB_AGNCY_BATCH B,"  
				+ " RTS.RTS_WEB_AGNT_SECURITY C," 
				+ " RTS.RTS_WEB_AGNT D " 	  
				+ " WHERE "
				+ " A.AgncyBatchIdntyNo = B.AgncyBatchIdntyNo AND " 
				+ " A.AgntSecrtyIdntyNo = C.AgntSecrtyIdntyNo AND "
				+ " C.AgntIdntyNo = D.AgntIdntyNo AND "
				+ " AccptVehIndi = 1");

			;

			lsQry.append(" AND ( RegPltNo = ? ");
			
	 		lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(aaWATransData.getRegPltNo())));
	 		
			lsQry.append(" OR InvItmNo = ? ");
			
	 		lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(aaWATransData.getRegPltNo())));
	 		
	 		lsQry.append(") AND RescomptCntyNo = ? ");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWATransData.getResComptCntyNo())));
    
			lsQry.append(" ORDER BY  A.AgncyBatchIdntyNo , AccptTimestmp  ");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyTransactionData laData =
					new WebAgencyTransactionData();

				laData.setAccptVehIndi(
					caDA.getIntFromDB(lrsQry, "AccptVehIndi"));
				laData.setAgncyBatchIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyBatchIdntyNo"));
				laData.setAddlSetIndi(
					caDA.getIntFromDB(lrsQry, "AddlSetIndi"));
				laData.setAgncyVoidIndi(
					caDA.getIntFromDB(lrsQry, "AgncyVoidIndi"));
				laData.setAgntSecrtyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo"));
				laData.setCntyVoidIndi(
					caDA.getIntFromDB(lrsQry, "CntyVoidIndi"));
				laData.setMustReplPltIndi(
					caDA.getIntFromDB(lrsQry, "MustReplPltIndi"));
				laData.setNewPltExpMo(
					caDA.getIntFromDB(lrsQry, "NewPltExpMo"));
				laData.setNewPltExpYr(
					caDA.getIntFromDB(lrsQry, "NewPltExpYr"));
				laData.setNewRegExpMo(
					caDA.getIntFromDB(lrsQry, "NewRegExpMo"));
				laData.setNewRegExpYr(
					caDA.getIntFromDB(lrsQry, "NewRegExpYr"));
				laData.setPltAge(
					caDA.getIntFromDB(lrsQry, "PltAge"));
				laData.setPltBirthDate(
					caDA.getIntFromDB(lrsQry, "PltBirthDate"));
				laData.setPltExpMo(
					caDA.getIntFromDB(lrsQry, "PltExpMo"));
				laData.setPltExpYr(
					caDA.getIntFromDB(lrsQry, "PltExpYr"));
				laData.setPltValidityTerm(
					caDA.getIntFromDB(lrsQry, "PltValidityTerm"));
				laData.setPrntQty(
					caDA.getIntFromDB(lrsQry, "PrntQty"));
				laData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laData.setRegExpMo(
					caDA.getIntFromDB(lrsQry, "RegExpMo"));
				laData.setRegExpYr(
					caDA.getIntFromDB(lrsQry, "RegExpYr"));
				laData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
				laData.setSavReqId(
					caDA.getIntFromDB(lrsQry, "SavReqId"));
				laData.setSubconId(
					caDA.getIntFromDB(lrsQry, "SubconId"));
				laData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laData.setVehGrossWt(
					caDA.getIntFromDB(lrsQry, "VehGrossWt"));
				laData.setAuditTrailTransId(
					caDA.getStringFromDB(lrsQry, "AuditTrailTransId"));
				laData.setBarCdVersionNo(
					caDA.getStringFromDB(lrsQry, "BarCdVersionNo"));
				laData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laData.setInsVerfdCd(
					caDA.getStringFromDB(lrsQry, "InsVerfdCd"));
				laData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laData.setKeyTypeCd(
					caDA.getStringFromDB(lrsQry, "KeyTypeCd"));
				laData.setBatchStatusCd(
					caDA.getStringFromDB(lrsQry, "BatchStatusCd"));
				laData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "OrgNo"));
				laData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laData.setWebSessionId(
					caDA.getStringFromDB(lrsQry, "WebSessionId"));
				laData.setStkrItmCd(
					caDA.getStringFromDB(lrsQry, "StkrItmCd"));
				laData.setTransId(
					caDA.getStringFromDB(lrsQry, "TransId"));
				laData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laData.setVehModl(
					caDA.getStringFromDB(lrsQry, "VehModl"));
				laData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laData.setInitReqTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "InitReqTimestmp"));
				laData.setAccptTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "AccptTimestmp"));
				laData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));
				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));
				laData.setOwnrTtlName1(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));
				laData.setOwnrTtlName2(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));
				laData.setRecpntName(
						caDA.getStringFromDB(lrsQry, "RecpntName"));
				// Add element to the Vector
				lvRslt.addElement(laData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
}
