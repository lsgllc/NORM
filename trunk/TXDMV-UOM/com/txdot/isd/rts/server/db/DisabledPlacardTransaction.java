package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.data.DisabledPlacardData;
import com.txdot.isd.rts.services.data.DisabledPlacardTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * DisabledPlacardTransaction.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/01/2008	add population of VoidTransEmpId 
 * 							add delete for RTS_DSABLD_PLCRD_TRANS_TO_PLCRD
 * 							 as did not incorporate Foreign Key w/ 
 * 							 Cascade Delete.
 * 							modify delDsabldPlcrdTrans(), 
 * 							 voidDsabldPlcrdTrans()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/25/2008	Retrieve SubstaId on qryDsabldPlcrdForDelete()
 * 							modify qryDsabldPlcrdForDelete()
 * 							defect 9873 Ver Defect_POS_B  
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insDisabldPlcrdTrans()    
 *	 						defect 9847 Ver Defect_POS_D   
 * K Harrell	09/03/2009	Remove extra ";" after statements
 * 							modify voidDsabldPlcrdTrans() 
 * 							defect 10033/10064 Ver Defect_POS_E/F 
 * K Harrell	05/03/2010	Added TransTime parameter
 * 							modify voidDsabldPlcrdTrans() 
 * 							defect 10468 Ver POS_640 
 * K Harrell	06/15/2010  add TransId to insert
 * 							modify insDsabldPlcrdTrans()
 * 							defect 10505 Ver 6.5.0
 * K Harrell	01/23/2012	Modify to handle Reinstate/Renew 
 *  						modify delDsabldPlcrdTrans(),
 *  						 updDsabldPlcrdTrans(), voidDsabldPlcrdTrans()
 *  						defect 11214 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access tables involved in Disabled Placard 
 *   transactions.   
 *
 * @version	6.10.0			01/23/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class DisabledPlacardTransaction
	extends DisabledPlacardTransactionData
{
	DatabaseAccess caDA;

	/**
	 * DisabledPlacardTransaction constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DisabledPlacardTransaction(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD_TRANS for transaction associated
	 * with Adding of Disabled Placard to determine if Voidable vs. 
	 * Deleteable. 
	 *
	 * @return Vector
	 * @throws RTSException  	
	 */
	public DisabledPlacardTransactionData qryDsabldPlcrdTransForDelete(DisabledPlacardCustomerData aaDPCustData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryDsabldPlcrdTransForDelete - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		DisabledPlacardTransactionData laRtrnDPTransData = null;

		ResultSet lrsQry;

		Vector lvDPData = aaDPCustData.getDsabldPlcrd();

		DisabledPlacardData laDPData =
			(DisabledPlacardData) lvDPData.elementAt(0);

		// defect 9873
		// Add SubstaId  
		lsQry.append(
			"SELECT "
				+ " OFCISSUANCENO,  "
				+ " SUBSTAID,"
				+ " TRANSWSID,  "
				+ " TRANSAMDATE,  "
				+ " TRANSTIME,  "
				+ " CUSTSEQNO  "
				+ " FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE A.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " B.DSABLDPLCRDIDNTYNO = ? AND B.TRANSTYPECD IN ("
				+ MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD
				+ ","
				+ MiscellaneousRegConstant.DP_UNDEL_TRANS_TYPE_CD
				+ ") ORDER BY TRANSAMDATE DESC, TRANSTIME DESC "); 
		// end defect 9873 

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					laDPData.getDsabldPlcrdIdntyNo())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDsabldPlcrdTransForDelete - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryDsabldPlcrdTransForDelete - SQL - End");

			while (lrsQry.next())
			{
				laRtrnDPTransData =
					new DisabledPlacardTransactionData();

				laRtrnDPTransData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				// defect 9873 
				laRtrnDPTransData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				// end defect 9873 

				laRtrnDPTransData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));

				laRtrnDPTransData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));

				laRtrnDPTransData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));

				laRtrnDPTransData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				
				break; 

			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryDsabldPlcrdTransForDelete - End ");
			return laRtrnDPTransData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDsabldPlcrdTransForDelete - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Insert into RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD
	 * 
	 * @param  aaDisabledPlacardData 		
	 * @throws RTSException 	
	 */
	public void insDsabldPlcrdTransToPlcrd(
		int aiTransIdntyNo,
		int aiDsabldPlcrdIdntyNo,
		int aiTransTypeCd,
		int aiDelReasonCd)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insDsabldPlcrdTransToPlcrd - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT INTO RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD( "
				+ "TRANSTYPECD, "
				+ "DELREASNCD, "
				+ "TRANSIDNTYNO,"
				+ "DSABLDPLCRDIDNTYNO) "
				+ " VALUES ("
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ? )";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiTransTypeCd)));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aiDelReasonCd)));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiTransIdntyNo)));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aiDsabldPlcrdIdntyNo)));

			Log.write(
				Log.SQL,
				this,
				"insDsabldPlcrdTransToPlcrd - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insDsabldPlcrdTransToPlcrd - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insDsabldPlcrdTransToPlcrd  - End");
		}

		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insDsabldPlcrdTransToPlcrd - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Insert into RTS.RTS_DSABLD_PLCRD_TRANS
	 * 
	 * @param  aaDisabledPlacardData 		
	 * @throws RTSException 	
	 */
	public void insDisabldPlcrdTrans(DisabledPlacardTransactionData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insDisabledPlacardTrans - Begin");

		Vector lvValues = new Vector();

		// defect 9847 
		if (aaData.getTransEmpId() == null
			|| aaData.getTransEmpId().trim().length() == 0)
		{
			aaData.setTransEmpId(CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		ResultSet lrsQry;

		// defect 10505 
		// add TransId 
		String lsIns =
			"INSERT INTO RTS.RTS_DSABLD_PLCRD_TRANS( "
				+ "CUSTIDNTYNO, "
				+ "OFCISSUANCENO,"
				+ "SUBSTAID,"
				+ "TRANSWSID,"
				+ "TRANSAMDATE, "
				+ "CUSTSEQNO, "
				+ "TRANSTIME, "
				+ "TRANSCD, "
				+ "TRANSEMPID, "
				+ "TRANSID )"
				+ " VALUES ("
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ? )";
		// end defect 10505 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getCustIdntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSubstaId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getTransAMDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getCustSeqNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getTransTime())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getTransCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getTransEmpId())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getTransId())));
			// end defect 10505 

			Log.write(
				Log.SQL,
				this,
				"insDisabledPlacardTrans - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as TransIdntyNo  from RTS.RTS_DSABLD_PLCRD_TRANS ";

			lrsQry = caDA.executeDBQuery(lsSel, null);

			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo = caDA.getIntFromDB(lrsQry, "TransIdntyNo");
				aaData.setTransIdntyNo(liIdntyNo);
				break;
			} //End of While

			// Also insert into RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD
			Vector lvData = aaData.getDsabldPlcrd();

			// Either "ADD" or "DELETE" 
			//   Do not touch Placard table if Delete 
			if (lvData != null && lvData.size() != 0)
			{
				for (int i = 0; i < lvData.size(); i++)
				{
					DisabledPlacardData laDPData =
						(DisabledPlacardData) lvData.elementAt(i);

					if (laDPData.getTransTypeCd()
						== MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD)
					{
						laDPData = insDsabldPlcrd(laDPData);
					}

					insDsabldPlcrdTransToPlcrd(
						aaData.getTransIdntyNo(),
						laDPData.getDsabldPlcrdIdntyNo(),
						laDPData.getTransTypeCd(),
						laDPData.getDelReasnCd());
				}
			}
			Log.write(
				Log.SQL,
				this,
				"insDisabledPlacardTrans - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insDisabledPlacardTrans  - End");
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - insDisabledPlacard - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insDisabledPlacard - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to DELETE from RTS.RTS_DSABLD_PLCRD_TRANS, etc. upon 
	 * Cancel Transaction from Pending Transaction Screen  
	 * 
	 * @param  aaData 
	 * @throws RTSException 	
	 */
	public void delDsabldPlcrdTrans(DisabledPlacardTransactionData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delDsabldPlcrdTrans - Begin");

		Vector lvValues = new Vector();

		// Update InProcs timestamp for Customer 
		String lsUpd0 =
			" UPDATE RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS A SET "
				+ " COMPLTIMESTMP = CURRENT TIMESTAMP WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS B,  "
				+ " RTS.RTS_DSABLD_PLCRD_CUST C  "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND  TransWsId = ? AND "
				+ " CustSeqNo = ? AND A.CUSTIDNTYNO = B.CUSTIDNTYNO AND "
				+ " A.CUSTIDNTYNO = C.CUSTIDNTYNO AND "
				+ " A.INPROCSIDNTYNO = C.INPROCSIDNTYNO )";

		// Reset InProcsIndi, InProcsIdntyNo for Customer
		String lsUpd1 =
			" UPDATE RTS.RTS_DSABLD_PLCRD_CUST A SET INPROCSINDI = 0, "
				+ " INPROCSIDNTYNO = 0 WHERE EXISTS ("
				+ " SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS B  "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND  TransWsId = ? AND "
				+ " CustSeqNo = ? AND A.CUSTIDNTYNO = B.CUSTIDNTYNO) ";

		// Delete any added Placards 
		String lsDel0 =
			" DELETE FROM RTS.RTS_DSABLD_PLCRD C WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
				+ " A.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " B.DSABLDPLCRDIDNTYNO = C.DSABLDPLCRDIDNTYNO "
				+ " AND  B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD
				+ ")";

		// "Un-Delete" any deleted Placards  
		String lsUpdt2 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET DELETEINDI = 0 "
				+ " WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
				+ " A.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " B.DSABLDPLCRDIDNTYNO = C.DSABLDPLCRDIDNTYNO "
				+ " AND B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_DEL_TRANS_TYPE_CD
				+ ")";
		
		// defect 11214 
		// "Ren-Delete" any deleted Placards
		String lsUpdt3 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET DELETEINDI = 1 "
				+ " WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
				+ " A.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " B.DSABLDPLCRDIDNTYNO = C.DSABLDPLCRDIDNTYNO "
				+ " AND B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_UNDEL_TRANS_TYPE_CD
				+ ")";
		// end defect 11214 
		
		// Delete the transaction to placard connection  
		String lsDel1 =
			"DELETE FROM RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD A  "
				+ " WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
				+ " A.TRANSIDNTYNO = B.TRANSIDNTYNO ) ";

		// Finally, Delete the Transaction 				
		String lsDel2 =
			"DELETE FROM RTS.RTS_DSABLD_PLCRD_TRANS "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getSubstaId())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getTransAMDate())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransWsId())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getCustSeqNo())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				"delDsabldPlcrdTrans - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd0, lvValues);
			caDA.executeDBInsertUpdateDelete(lsUpd1, lvValues);
			caDA.executeDBInsertUpdateDelete(lsDel0, lvValues);
			caDA.executeDBInsertUpdateDelete(lsUpdt2, lvValues);
			
			// defect 11214
			caDA.executeDBInsertUpdateDelete(lsUpdt3, lvValues);
			// end defect 11214
			
			caDA.executeDBInsertUpdateDelete(lsDel1, lvValues);
			caDA.executeDBInsertUpdateDelete(lsDel2, lvValues);

			Log.write(Log.SQL, this, "delDsabldPlcrdTrans - SQL - End");
			Log.write(Log.METHOD, this, "delDsabldPlcrdTrans - End");
			return;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delDsabldPlcrdTrans - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF DELETE METHOD

	/**
	 * Method to Insert into RTS.RTS_DSABLD_PLCRD
	 * 
	 * @param  aaDisabledPlacardData 		
	 * @throws RTSException 	
	 */
	public DisabledPlacardData insDsabldPlcrd(DisabledPlacardData aaDisabledPlacardData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insDisabledPlacard - Begin");

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		String lsIns =
			"INSERT INTO RTS.RTS_DSABLD_PLCRD( "
				+ "CUSTIDNTYNO, "
				+ "ACCTITMCD,"
				+ "INVITMCD,"
				+ "INVITMNO,"
				+ "RTSEFFDATE, "
				+ "RTSEXPMO, "
				+ "RTSEXPYR) "
				+ " VALUES ("
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ? )";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getCustIdntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getAcctItmCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getInvItmCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getInvItmNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getRTSEffDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getRTSExpMo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDisabledPlacardData.getRTSExpYr())));

			Log.write(
				Log.SQL,
				this,
				"insDisabledPlacard - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as DsabldPlcrdIdntyNo  from RTS.RTS_DSABLD_PLCRD ";

			lrsQry = caDA.executeDBQuery(lsSel, null);

			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo =
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdIdntyNo");
				aaDisabledPlacardData.setDsabldPlcrdIdntyNo(liIdntyNo);
				break;
			} //End of While

			aaDisabledPlacardData.setDsabldPlcrdIdntyNo(liIdntyNo);

			Log.write(Log.SQL, this, "insDisabledPlacard - SQL - End");
			Log.write(Log.METHOD, this, "insDisabledPlacard - End");
			return aaDisabledPlacardData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - insDisabledPlacard - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insDisabledPlacard - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to complete RTS.RTS_DSABLD_PLCRD
	 *
	 * @param  aaDisabledPlacardData 
	 * @return int 
	 * @throws RTSException
	 */
	public int updDsabldPlcrdTrans(DisabledPlacardTransactionData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updDisabledPlacardTrans - Begin");

		Vector lvValues = new Vector();

		Vector lvValues1 = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_DSABLD_PLCRD_TRANS "
				+ "SET TRANSTIMESTMP = ? "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ";

		String lsUpd0 =
			" UPDATE RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS A SET "
				+ " COMPLTIMESTMP = CURRENT TIMESTAMP WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS B,  "
				+ " RTS.RTS_DSABLD_PLCRD_CUST C  "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND  TransWsId = ? AND "
				+ " CustSeqNo = ? AND A.CUSTIDNTYNO = B.CUSTIDNTYNO AND "
				+ " A.CUSTIDNTYNO = C.CUSTIDNTYNO AND "
				+ " A.INPROCSIDNTYNO = C.INPROCSIDNTYNO )";

		String lsUpd00 =
			" UPDATE RTS.RTS_DSABLD_PLCRD_CUST A SET INPROCSINDI = 0, "
				+ " INPROCSIDNTYNO = 0 WHERE EXISTS ("
				+ " SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS B  "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND "
				+ " TransAMDate = ? AND  TransWsId = ? AND "
				+ " CustSeqNo = ? AND A.CUSTIDNTYNO = B.CUSTIDNTYNO) ";

		String lsUpd1 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET COMPLETEINDI = 1 WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD
				+ " AND A.TRANSIDNTYNO = B.TRANSIDNTYNO AND B.DSABLDPLCRDIDNTYNO "
				+ " = C.DSABLDPLCRDIDNTYNO) ";

		String lsUpd2 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET DELETEINDI  = 1 WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_DEL_TRANS_TYPE_CD
				+ " AND A.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " B.DSABLDPLCRDIDNTYNO = C.DSABLDPLCRDIDNTYNO) ";
		
		// defect 11214 
		//  For ReIntate  ("UnDelete")
		String lsUpd3 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET DELETEINDI  = 0 WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_UNDEL_TRANS_TYPE_CD
				+ " AND A.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " B.DSABLDPLCRDIDNTYNO = C.DSABLDPLCRDIDNTYNO) ";
		// end defect 11214 
		

		lvValues.addElement(
			new DBValue(
				Types.TIMESTAMP,
				DatabaseAccess.convertToString(
					aaData.getTransTimestmp())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getTransAMDate())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getCustSeqNo())));

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));
		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getSubstaId())));
		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getTransAMDate())));
		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransWsId())));
		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getCustSeqNo())));

		try
		{
			Log.write(
				Log.SQL,
				this,
				"updDisabledPlacardTrans - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			// Update Added Rows w/ CompleteIndi 
			int liNumRows0 =
				caDA.executeDBInsertUpdateDelete(lsUpd0, lvValues1);

			int liNumRows00 =
				caDA.executeDBInsertUpdateDelete(lsUpd00, lvValues1);

			int liNumRows1 =
				caDA.executeDBInsertUpdateDelete(lsUpd1, lvValues1);

			// Update Deleted Rows / DeleteIndi 
			int liNumRows2 =
				caDA.executeDBInsertUpdateDelete(lsUpd2, lvValues1);
			
			int liNumRows3 =
				caDA.executeDBInsertUpdateDelete(lsUpd3, lvValues1);

			Log.write(
				Log.SQL,
				this,
				"updDisabledPlacardTrans - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"updDisabledPlacardTrans - End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updDisabledPlacardTrans - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Method to void Disabled Placard Transactions 
	 *
	 * @param  aaDisabledPlacardData 
	 * @return int 
	 * @throws RTSException
	 */
	public int voidDsabldPlcrdTrans(DisabledPlacardTransactionData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "voidDsabldPlcrdTrans - Begin");

		Vector lvValues = new Vector();

		Vector lvValues1 = new Vector();

		String lsUpd0 =
			"UPDATE RTS.RTS_DSABLD_PLCRD_TRANS "
				+ "SET VOIDEDTRANSINDI = 1, VOIDTRANSID = ?,"
				+ "VOIDTRANSEMPID = ?, "
				+ "VOIDTRANSTIMESTMP = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? "
			// defect 10468 
	+" AND TransTime  = ? ";
		// end defect 10468 

		// Where Voiding Add Transaction, Set VoidedIndi  
		String lsUpd1 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET VOIDEDINDI = 1 WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
			// defect 10468 
	+"TransTime = ? AND "
			// end defect 10468 
	+" B.TransTypeCd = "
		+ MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD
		+ " AND A.TRANSIDNTYNO = B.TRANSIDNTYNO AND B.DSABLDPLCRDIDNTYNO "
		+ " = C.DSABLDPLCRDIDNTYNO)";

		// Where Voiding Delete Transaction, Set DeleteIndi = 0 
		String lsUpd2 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET DELETEINDI = 0  WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
			// defect 10468 
	+" TransTime = ? AND "
			// end defect 10468  
	+" B.TransTypeCd = "
		+ MiscellaneousRegConstant.DP_DEL_TRANS_TYPE_CD
		+ " AND A.TRANSIDNTYNO = B.TRANSIDNTYNO AND B.DSABLDPLCRDIDNTYNO = "
		+ " C.DSABLDPLCRDIDNTYNO)";
		
		// defect 11214 
		// Where Voiding Re-Instate Transaction, Set DeleteIndi = 1 
		String lsUpd3 =
			" UPDATE RTS.RTS_DSABLD_PLCRD C SET DELETEINDI = 1  WHERE EXISTS "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD_TRANS A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " WHERE  OfcIssuanceNo = ? AND SubstaId = ? AND TransAMDate = ? AND "
				+ " TransWsId = ? AND CustSeqNo = ? AND "
				+" TransTime = ? AND "
				+" B.TransTypeCd = "
				+ MiscellaneousRegConstant.DP_UNDEL_TRANS_TYPE_CD
				+ " AND A.TRANSIDNTYNO = B.TRANSIDNTYNO AND B.DSABLDPLCRDIDNTYNO = "
				+ " C.DSABLDPLCRDIDNTYNO)";
		// end defect 11214 

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaData.getVoidTransId())));

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaData.getVoidTransEmpId())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getSubstaId())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getTransAMDate())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransWsId())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getCustSeqNo())));

		// defect 10468 
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransTime())));
		// end defect 10468 

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getOfcIssuanceNo())));

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getSubstaId())));

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaData.getTransAMDate())));

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransWsId())));

		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getCustSeqNo())));

		// defect 10468 
		lvValues1.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aaData.getTransTime())));
		// end defect 10468 

		try
		{
			Log.write(
				Log.SQL,
				this,
				"voidDsabldPlcrdTrans - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd0, lvValues);

			// defect 10468 
			// Do not continue if no records found 
			if (liNumRows != 0)
			{
				int liNumRows1 =
					caDA.executeDBInsertUpdateDelete(lsUpd1, lvValues1);

				int liNumRows2 =
					caDA.executeDBInsertUpdateDelete(lsUpd2, lvValues1);
				
				// defect 11214 
				int liNumRows3 =
					caDA.executeDBInsertUpdateDelete(lsUpd3, lvValues1);
				// end defect 11214 
			}
			// end defect 10468 

			Log.write(
				Log.SQL,
				this,
				"voidDsabldPlcrdTrans - SQL - End");
			Log.write(Log.METHOD, this, "voidDsabldPlcrdTrans - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"voidDsabldPlcrdTrans - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD

}
