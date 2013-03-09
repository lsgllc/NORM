package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CustomerNameData;
import com
	.txdot
	.isd
	.rts
	.services
	.data
	.ModifyPermitTransactionHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * ModifyPermitTransaction.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/31/2011	Created
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Insert the class's description here. (do not leave this)
 *
 * @version	6.8.0			05/31/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		05/31/2011  14:58:17 
 */
public class ModifyPermitTransactionHistory
{
	private String csMethod = new String();

	private DatabaseAccess caDA;

	/**
	 * ModifyPermitTransactionHistory constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public ModifyPermitTransactionHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Delete from RTS.RTS_MOD_PRMT_TRANS_HSTRY
	 * 
	 * @param  aaModPrmtTransData ModifyPermitTransactionHistoryData	
	 * @throws RTSException
	 */
	public void delModifyPermitTransactionHistory(ModifyPermitTransactionHistoryData aaModPrmtTransData)
		throws RTSException
	{
		csMethod = "delModifyPermitTransactionHistory";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_MOD_PRMT_TRANS_HSTRY "
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
						aaModPrmtTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getCustSeqNo())));

			if (aaModPrmtTransData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaModPrmtTransData.getTransTime())));
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
	 * Method to Insert into RTS.RTS_MOD_PRMT_TRANS
	 * 
	 * @param  aaModPrmtTransData ModifyPermitTransactionHistoryData	
	 * @throws RTSException
	 */
	public void insModifyPermitTransactionHistory(ModifyPermitTransactionHistoryData aaModPrmtTransData)
		throws RTSException
	{
		csMethod = "insPermitTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_MOD_PRMT_TRANS_HSTRY("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "TransId,"
				+ "CustFstName,"
				+ "CustMIName,"
				+ "CustLstName,"
				+ "CustBsnName,"
				+ "PrmtNo,"
				+ "ItmCd,"
				+ "AcctItmCd,"
				+ "EffDate,"
				+ "EffTime,"
				+ "ExpDate,"
				+ "ExpTime,"
				+ "VehMk,"
				+ "VehModlYr,"
				+ "VIN,"
				+ "PrmtIssuanceId )"
				+ "VALUES ( ? ";

		String lsAdd = new String();

		for (int i = 0; i < 23; i++)
		{
			lsAdd = lsAdd + " ,? ";
		}
		lsIns = lsIns + lsAdd + ")";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getOfcIssuanceNo())));
			// 2				
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getSubstaId())));
			// 3			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransAMDate())));
			// 4			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransWsId())));
			// 5			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getCustSeqNo())));
			// 6			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransTime())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData.getTransCd()));
			// 7a
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData.getTransEmpId()));

			// 8
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData.getTransId()));

			// 9 - CustFstName 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData
						.getCustNameData()
						.getCustFstName()));

			// 10 - CustMIName 			
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData
						.getCustNameData()
						.getCustMIName()));

			// 11 - CustLstName 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData
						.getCustNameData()
						.getCustLstName()));

			// 12	- CustBsnName
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData
						.getCustNameData()
						.getCustBsnName()));

			// 13   - PrmtNo		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData.getPrmtNo()));

			// 14   - ItmCd		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaModPrmtTransData.getItmCd()));

			// 15   - AcctItmCd		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData.getAcctItmCd()));

			// 16  - EffDate		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getEffDate())));
			// 17  - EffTime		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getEffTime())));

			// 18  - ExpDate		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getExpDate())));

			// 19  - ExpTime		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getExpTime())));

			// 20 - VehMk		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaModPrmtTransData.getVehMk()));

			// 21  - VehModlYr		
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getVehModlYr())));

			// 22  - VIN		
			lvValues.addElement(
				new DBValue(Types.CHAR, aaModPrmtTransData.getVin()));

			// 23 - PrmtIssuanceId
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaModPrmtTransData.getPrmtIssuanceId()));

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
	 * Method to Delete from RTS.RTS_MOD_PRMT_TRANS_HSTRY for purge
	 *
	 * @param  aiPurgeAMDate int
	 * @return int	
	 * @throws RTSException 
	 */
	public int purgeModifyPermitTransactionHistory(int aiPurgeAMDate)
		throws RTSException
	{
		csMethod = "purgePermitTransaction";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_MOD_PRMT_TRANS_HSTRY  "
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
	 * Method to Query from RTS.RTS_MOD_PRMT_TRANS
	 *
	 * @param 	asPrmtNo	
	 * @return  Vector 
	 * @throws 	RTSException 	
	 */
	public Vector qryModifyPermitTransaction(String asPrmtIssuanceId)
		throws RTSException
	{
		csMethod = "qryModifyPermitTransaction";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

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
				+ "TransEmpId,"
				+ "CustFstName,"
				+ "CustMIName,"
				+ "CustLstName,"
				+ "CustBsnName,"
				+ "PrmtNo,"
				+ "ItmCd,"
				+ "AcctItmCd,"
				+ "EffDate,"
				+ "EffTime,"
				+ "ExpDate,"
				+ "ExpTime,"
				+ "VehMk,"
				+ "VehModlYr,"
				+ "VIN,"
				+ "PrmtIssuanceId, "
				+ "VoidedTransIndi "
				+ "FROM RTS.RTS_MOD_PRMT_TRANS_HSTRY A "
				+ "WHERE "
				+ "PrmtIssuanceId = ? ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(asPrmtIssuanceId)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				ModifyPermitTransactionHistoryData laModPrmtTransData =
					new ModifyPermitTransactionHistoryData();

				laModPrmtTransData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laModPrmtTransData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laModPrmtTransData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laModPrmtTransData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laModPrmtTransData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laModPrmtTransData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laModPrmtTransData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laModPrmtTransData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				CustomerNameData laCustNameData =
					new CustomerNameData();
				laCustNameData.setCustBsnName(
					caDA.getStringFromDB(lrsQry, "CustBsnName"));
				laCustNameData.setCustFstName(
					caDA.getStringFromDB(lrsQry, "CustFstName"));
				laCustNameData.setCustMIName(
					caDA.getStringFromDB(lrsQry, "CustMIName"));
				laCustNameData.setCustLstName(
					caDA.getStringFromDB(lrsQry, "CustLstName"));
				laModPrmtTransData.setCustNameData(laCustNameData);
				laModPrmtTransData.setPrmtNo(
					caDA.getStringFromDB(lrsQry, "PrmtNo"));
				laModPrmtTransData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laModPrmtTransData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laModPrmtTransData.setEffDate(
					caDA.getIntFromDB(lrsQry, "EffDate"));
				laModPrmtTransData.setEffTime(
					caDA.getIntFromDB(lrsQry, "EffTime"));
				laModPrmtTransData.setExpDate(
					caDA.getIntFromDB(lrsQry, "ExpDate"));
				laModPrmtTransData.setExpTime(
					caDA.getIntFromDB(lrsQry, "ExpTime"));
				laModPrmtTransData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laModPrmtTransData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laModPrmtTransData.setVin(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laModPrmtTransData.setPrmtIssuanceId(
					caDA.getStringFromDB(lrsQry, "PrmtIssuanceId"));

				// Add element to the Vector
				lvRslt.addElement(laModPrmtTransData);
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
	 * Method to mark RTS.RTS_SPCL_PLT_TRANS_HSTRY as complete
	 * 
	 * @param  aaSpecialPlateTransactionHistoryData SpecialPlateTransactionHistoryData	
	 * @throws RTSException
	 */
	public void updModifyPermitTransactionHistory(ModifyPermitTransactionHistoryData aaModPrmtTransData)
		throws RTSException
	{
		csMethod = "updModifyPermitTransactionHistory";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_MOD_PRMT_TRANS_HSTRY "
				+ " SET TRANSCOMPLETEINDI = 1 WHERE  "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = ? AND "
				+ " TransAMDate = ? AND "
				+ " TransWsId = ? AND "
				+ " CustSeqNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getCustSeqNo())));
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
	} //END OF UPDATE METHOD

	/**
	 * Method to VOID RTS.RTS_MOD_PRMT_TRANS_HSTRY
	 * 
	 * @param  aaModPrmtTransData ModifyPermitTransactionHistoryData	
	 * @throws RTSException
	 */
	public void voidModifyPermitTransactionHistory(ModifyPermitTransactionHistoryData aaModPrmtTransData)
		throws RTSException
	{
		csMethod = "voidModifyPermitTransactionHistory";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_MOD_PRMT_TRANS_HSTRY "
				+ " SET VOIDEDTRANSINDI= 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "Transtime = ? AND "
				+ "TransCompleteIndi = 1 ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaModPrmtTransData.getTransTime())));
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
	} //END OF VOID METHOD

}
