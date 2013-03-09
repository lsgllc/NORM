package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * Transaction.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/15/2001  Altered Query to test Parm of 
 *							TransPostedMFIndi
 *                          delTransaction tests for Integer.MIN_VALUE
 * K Harrell   	10/19/2001	Added purgeTransaction
 * K Harrell 	10/23/2001	Added qryTransactionForVoid
 *                          Added voidTransaction
 *                       	Removed updTransaction
 * B Tulsiani 	11/05/2001	Updated voidTransaction to take 
 *							TransactionData
 * K Harrell	11/11/2001	Removed OfcIssuanceNo, SubstaId from Purge
 * K Harrell	12/07/2001	Removed BranchOfcId from qryTransaction
 * K Harrell 	12/19/2001	Altered qryTransactionForVoid
 * K Harrell	03/14/2002	Altered qryTransaction
 * R Hicks      07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell   	08/07/2002	Added Union in qryMaxTransaction where no 
 * 							transactions exist 
 * 							defect 4581
 * K Harrell  	12/04/2002	Altered qryMaxTransaction to
 *                          always return data.
 * 							defect 4961 
 * K Harrell  	05/13/2003	Gather TransAMDates eligible for purge 
 *							defect 6119 
 *                          add qryTransactionDatesForPurge
 * K Harrell 	06/05/2003  Add parameters for startofc, endofc 
 *                          add qryTransaction(int,int)
 *							defect 6227
 * K Harrell	04/19/2004	Added RprStkOfcIssuanceNo,
 *							RprStkTransWsId, RprStkTransAMDate,
 *							RprStkTransTime to query calls
 *							modify insTransaction
 *							defect 7018  Ver 5.2.0
 * K Harrell	10/13/2004	deprecate qryTransactionForSubcon()
 *							defect 7620  Ver 5.2.1
 * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate
 *							modify purgeTransaction(),
 *							qryTransactionDatesForPurge()
 *							defect 7684  Ver 5.2.2
 * K Harrell	10/17/2005	Java 1.4 Work
 * 							add BEGIN,END,SQL,EXCEPTION,csMethodName
 * 							add qryIncomplTrans() from 
 * 							 server.systemcontrolbatch.BatchSQL
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Return vector of Integers vs. TransactionData
 *  						objects & use < vs. <=  when query for 
 * 							Transaction Dates
 * 							Use int vs. Transaction Object in purge   
 * 							modify qryTransactionDatesForPurge(),
 * 							  purgeTransaction() 
 * 							defect 8423 Ver 5.2.3 
 * Ray Rowehl	09/27/2006	Modify the SendTrans query to include the 
 * 							MfVersionNo(VERSIONCD) in the where clause.
 * 							modify qryTransaction(int, int)
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	11/09/2006	Restore use of <= to reflect Purge Logging
 * 							modify qryTransactionDatesForPurge(),
 * 							  purgeTransaction() 
 * 							defect 8423 Ver Exempts
 * K Harrell	04/04/2007	Include collection of SpecialPlate RegPltNo
 * 							modify qryTransactionForVoid()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/26/2007	Query only for transactions less than
 * 							current date 							
 * 							modify qryIncomplTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/12/2007	Qualify transtime when CustSeqNo = 0
 * 							modify qryTransactionForVoid()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/22/2008	Assign TransEmpId to "LGNERR" if null
 * 							on insert. 
 * 							modify insTransaction()
 * 							defect 9847 Ver Defect_POS_B
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insTransaction()    
 * 							defect 9847 Ver Defect_POS_D
 * K Harrell	01/08/2009	Add Union to Trans_Payment to determine
 * 							 last successful DB insert. Will reduce 
 * 							 number of SQL0803 errors returned and
 * 							 subsequent logging upon workstation startup. 
 * 							modify qryMaxTransaction()  
 * 							defect 9891 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeTransaction()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	08/27/2009	Add check for PrintImmediate = 2 
 * 							add qryPrintImmediateIncomplTrans() 
 * 							delete qryIncomplTrans()
 * 							defect 10184 Ver Defect_POS_F
 * K Harrell	08/28/2009	Collect dates from all Transaction Tables
 * 							for Purge.
 * 							modify qryTransactionDatesForPurge()
 * 							defect 10185 Ver Defect_POS_F   
 * K Harrell	05/28/2010	add AddPrmtRecIndi, DelPrmtRecIndi
 * 							modify insTransaction(), 
 * 							 qryTransaction(int,int), 
 * 							 qryTransaction(TransactionData) 
 * 							deprecate qryTransaction(TransactionData,int,int)  
 * 							defect 10491 Ver 6.5.0 
 * K Harrell 	06/14/2010 	add TransId,CustNoKey to insert
 * 							modify insTransaction()  
 * 							defect 10505 Ver 6.5.0  
 * K Harrell	07/23/2010	add population of VoidTransId
 * 							modify insTransaction()
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	09/16/2010	add qryInProcessTransaction() 
 * 							defect 10598 Ver 6.6.0
 * K Harrell	01/25/2011	add getNextWebAgntTransTime()
 * 							modify qryMaxTransaction()  
 * 							defect 10734 Ver 6.7.0
 * K Harrell	02/07/2011	add query against RTS_WEB_AGNCY_TRANS for 
 * 							 In-Process Transactions 
 * 							modify qryInProcessTransaction() 
 * 							defect 10746 Ver 6.7.1   
 * K Harrell	03/10/2011	add criteria ACCPTVEHINDI = 1 for 
 * 							 in process for RTS_WEB_AGNCY_TRANS 
 * 							modify qryInProcessTransaction() 
 * 							defect 10746 Ver 6.7.1   
 * K Harrell	03/23/2011	modify to use accptTimestmp vs. 
 * 							 transtimestmp for RTS_WEB_AGNCY_TRANS
 * 							modify qryInProcessTransaction()	
 * 							defect 10746 Ver 6.7.1
 * K Harrell	04/23/2011	Use Coalesce in query to pull data from 
 * 							RTS_TRANS if RTS_MV_FUNC_TRANS record does
 * 							not exist. 
 * 							modify qryInProcessTransaction() 
 * 							defect 10780 Ver 6.7.1 
 * K Harrell	05/26/2011	Restore earlier SQL for InProcess 
 * 				  			modify qryInProcessTransaction() 
 * 							defect 10780 Ver 6.7.1 
 * K Harrell	06/12/2011	Add query against RTS_FRAUD_LOG to determine
 * 							last insert 
 * 							modify qryMaxTransaction() 
 * 							defect 10865 Ver 6.8.0 
 * K Harrell	06/30/2012	Modify to include last transaction from 
 * 							RTS_LOG_FUNC_TRANS. Add UtilityMethods.sort()  
 * 							modify qryMaxTransaction()
 * 							defect 11073 Ver 7.0.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with the database.
 * (RTS_TRANS) 
 *
 * @version	7.0.0 			06/30/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 20:35:44 
 */

public class Transaction extends TransactionData
{
	private static final String BEGIN = "Begin";
	private static final String END = "End";
	private static final String SQL = "SQL - ";
	private static final String EXCEPTION = "Exception";

	private String csMethod = "";
	private DatabaseAccess caDA;

	/**
	 * Transaction.java Constructor
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public Transaction(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_TRANS
	 * 
	 * @param  aaTransactionData TransactionData	
	 * @throws RTSException  	
	 */
	public void delTransaction(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- delTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		Vector lvValues = new Vector();
		String lsDel =
			"DELETE FROM RTS.RTS_TRANS "
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
						aaTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSeqNo())));
			if (aaTransactionData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ?";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionData.getTransTime())));
			}
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);
			Log.write(Log.METHOD, this, csMethod + END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	/** 
	 * Get Next WebAgentTransTime 
	 * 
	 * @param laTransHdrData
	 * @return int 
	 * @throws RTSException
	 */
	public int getNextWebAgntTransTime(TransactionHeaderData laTransHdrData)
		throws RTSException
	{
		csMethod = "getNextWebAgntTransTime";
		int liTransTime = Integer.MIN_VALUE;
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		String lsQry =
			"Select coalesce(max(transtime),"
				+ CommonConstant.MAX_POS_TRANSTIME
				+ ") as TransTime from "
				+ " RTS.RTS_TRANS where "
				+ " OfcissuanceNo = ? and "
				+ " TransWsId = ? and "
				+ " TransAMDate = ? "
				+ " and TransTime > "
				+ CommonConstant.MAX_POS_TRANSTIME;

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
				liTransTime = caDA.getIntFromDB(lrsQry, "TransTime");
			}
			lrsQry.close();
			return liTransTime;
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
	 * Method to Insert into RTS.RTS_TRANS
	 * 
	 * @param  aaTransactionData TransactionData	
	 * @throws RTSException 	
	 */
	public void insTransaction(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- insTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		// defect 9847 
		if (aaTransactionData.getTransEmpId() == null
			|| aaTransactionData.getTransEmpId().trim().length() == 0)
		{
			aaTransactionData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		Vector lvValues = new Vector();

		// defect 10505
		// add TransId 

		// defect 10491 
		// add AddPrmtRecIndi, DelPrmtRecIndi 
		// end defect 10491 
		String lsIns =
			"INSERT into RTS.RTS_TRANS  ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "CustNoKey,"
				+ "TransId,"
				+ "TransEmpId,"
				+ "TransPostedMfIndi,"
				+ "VoidedTransIndi,"
				+ "RegPltNo,"
				+ "DocNo,"
				+ "OwnrId,"
				+ "VIN,"
				+ "RegStkrNo,"
				+ "CustName1,"
				+ "CustName2,"
				+ "CustLstName,"
				+ "CustFstName,"
				+ "CustMIName,"
				+ "CustSt1,"
				+ "CustSt2,"
				+ "CustCity,"
				+ "CustState,"
				+ "CustZpCd,"
				+ "CustZpCdP4,"
				+ "CustCntry,"
				+ "DLSCertNo,"
				+ "EffDate,"
				+ "EffTime,"
				+ "ExpDate,"
				+ "ExpTime,"
				+ "RegClassCd,"
				+ "TireTypeCd,"
				+ "VehBdyType,"
				+ "VehCaryngCap,"
				+ "VehEmptyWt,"
				+ "VehGrossWt,"
				+ "VehMk,"
				+ "VehModlYr,"
				+ "VehRegState,"
				+ "VehUnitNo,"
				+ "VoidOfcIssuanceNo,"
				+ "VoidTransWsId,"
				+ "VoidTransAMDate,"
				+ "VoidTransTime,"
				+ "RprStkOfcIssuanceNo,"
				+ "RprStkTransWsId,"
				+ "RprStkTransAMDate,"
				+ "RprStkTransTime,"
				+ "DieselIndi,"
				+ "RegPnltyChrgIndi,"
				+ "ProcsdByMailIndi,"
				+ "BsnDate,"
				+ "BsnDateTotalAmt,"
				+ "VoidTransCd, "
				+ "AddPrmtRecIndi,"
				+ "DelPrmtRecIndi,"
				+ "VoidTransId "
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
		// end defect 10491
		// end defect 10505 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransCd())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaTransactionData.getCustNoKey()));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaTransactionData.getTransId()));
			// end defect 10505

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransPostedMfIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidedTransIndi())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getRegPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getDocNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getOwnrId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVIN())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getRegStkrNo())));

			String lsName = aaTransactionData.getCustName1();

			if (lsName != null && lsName.length() > 30)
			{
				lsName = lsName.substring(0, 30);
			}

			lvValues
				.addElement(new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
			//aaTransactionData.getCustName1())));
			lsName)));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustName2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustLstName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustFstName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustMIName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSt1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSt2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustCity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustState())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustZpCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustZpCdP4())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustCntry())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getDLSCertNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getEffDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getEffTime())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getExpDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getExpTime())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getRegClassCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getTireTypeCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehBdyType())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehCaryngCap())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehEmptyWt())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehGrossWt())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehMk())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehModlYr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehRegState())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVehUnitNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidTransTime())));
			// defect 7018 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getRprStkOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getRprStkTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getRprStkTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getRprStkTransTime())));
			// end defect 7018 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getDieselIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getRegPnltyChrgIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getProcsdByMailIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getBsnDate())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaTransactionData.getBsnDateTotalAmt())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidTransCd())));

			// defect 10491 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getAddPrmtRecIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getDelPrmtRecIndi())));
			// end defect 10491 

			// defect 10505
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaTransactionData.getVoidTransId())));
			// end defect 10505 

			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);
			Log.write(Log.METHOD, this, csMethod + END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to update RTS.RTS_TRANS w/ TransPostedMfIndi = 1
	 * 
	 * @param  aaTransactionData TransactionData	
	 * @throws RTSException 	
	 */
	public void postTransaction(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- postTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		Vector lvValues = new Vector();
		String lsUpd =
			"UPDATE RTS.RTS_TRANS SET "
				+ "TransPostedMfIndi = 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? AND "
				+ "TransTime  = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransTime())));

			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);
			Log.write(Log.METHOD, this, csMethod + END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Delete from RTS.RTS_TRANS
	 * 
	 * @param  aiPurgeAMDate int
	 * @return int  	
	 * @throws RTSException 	
	 */
	public int purgeTransaction(int aiPurgeAMDate) throws RTSException
	{
		csMethod = "- purgeTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		Vector lvValues = new Vector();
		String lsDel =
			"DELETE FROM RTS.RTS_TRANS A "
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

			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);
			Log.write(Log.METHOD, this, csMethod + END);
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/** 
	 * Query In-Process Transaction 
	 * 
	 * @param asDocNo
	 * @return Vector 
	 * @throws RTSException
	 */
	public Vector qryInProcessTransaction(GeneralSearchData aaGSD)
		throws RTSException
	{
		csMethod = "- qryInProcessTransaction - ";

		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();

		int liStartTransAMDate = new RTSDate().getAMDate() - 3;

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		String lsAppend =
			aaGSD.getKey1().equals("DOCNO")
				? " B.DOCNO = ? "
				: " B.VIN = ? ";

		lsQry.append(
			" SELECT A.OFCISSUANCENO, A.TRANSWSID,A.TRANSAMDATE, "
				+ " A.TRANSTIME, A.TRANSCD,A.VIN, A.DOCNO, "
				+ " B.REGPLTNO, A.TRANSEMPID, A.TRANSPOSTEDMFINDI "
				+ " FROM "
				+ " RTS.RTS_TRANS A, RTS.RTS_MV_FUNC_TRANS B, "
				+ " RTS.RTS_TRANS_CDS C "
				+ " WHERE  "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID AND "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.TRANSTIME = B.TRANSTIME AND "
				+ " A.TRANSCD = C.TRANSCD AND "
				+ " C.ELGBLEPNDNGTRANSLOOKUPINDI = 1 AND "
				+ " A.VOIDEDTRANSINDI = 0 AND "
				+ lsAppend
				+ " AND A.TRANSAMDATE >= ? ");

		try
		{
			// DocNo or VIN 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaGSD.getKey2())));

			// TransAMDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						liStartTransAMDate)));

			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);

			while (lrsQry.next())
			{
				InProcessTransactionData laTransactionData =
					new InProcessTransactionData();

				laTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				laTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));

				laTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));

				laTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));

				laTransactionData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));

				laTransactionData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));

				laTransactionData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));

				laTransactionData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));

				laTransactionData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));

				laTransactionData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMFIndi"));

				laTransactionData.initTransId();

				// Add element to the Vector
				lvRslt.addElement(laTransactionData);

			} //End of While 

			lrsQry.close();

			if (aaGSD.getKey1().equals("DOCNO"))
			{
				lsQry = new StringBuffer();
				lvValues = new Vector();

				// defect 10746 
				lsQry.append(
					" SELECT Ofcissuanceno,Days(Date(ItrntTimestmp)) - 693596 as "
						+ " TransAMDate,Hour(ItrntTimestmp) * 10000 + "
						+ " Minute(ItrntTimestmp) * 100 + "
						+ " Second(ItrntTimestmp) as TransTime, "
						+ " 'PENDING' as TRANSID, VIN, DOCNO, "
						+ " REGPLTNO,TransCd "
						+ " FROM "
						+ " RTS.RTS_ITRNT_TRANS "
						+ " WHERE TransCd = 'IRENEW' AND "
						+ " DOCNO = ? AND "
						+ " CNTYSTATUSCD IN (1,2,3,5) AND "
						+ " ITRNTPYMNTSTATUSCD IS NOT NULL "
						+ " UNION "
						+ " SELECT "
						+ " RESCOMPTCNTYNO as Ofcissuanceno,"
						+ " Days(Date(AccptTimestmp)) - 693596 as "
						+ " TransAMDate,Hour(AccptTimestmp) * 10000 + "
						+ " Minute(AccptTimestmp) * 100 + "
						+ " Second(AccptTimestmp) as TransTime, "
						+ " 'PENDING' as TRANSID, VIN, DOCNO, "
						+ " REGPLTNO,'WRENEW' as TransCd "
						+ " FROM "
						+ " RTS.RTS_WEB_AGNCY_TRANS "
						+ " WHERE "
						+ " DOCNO = ? AND "
						+ " AGNCYVOIDINDI = 0 AND "
						+ " CNTYVOIDINDI = 0 AND "
						+ " ACCPTVEHINDI = 1 AND "
						+ " TRANSID IS NULL ");

				for (int i = 0; i < 2; i++)
				{
					// DocNo 
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
								aaGSD.getKey2())));
				}
				// end defect 10746

				Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
				lrsQry =
					caDA.executeDBQuery(lsQry.toString(), lvValues);
				Log.write(Log.SQL, this, csMethod + SQL + END);
				while (lrsQry.next())
				{
					InProcessTransactionData laTransactionData =
						new InProcessTransactionData();
					laTransactionData.setOfcIssuanceNo(
						caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
					laTransactionData.setTransAMDate(
						caDA.getIntFromDB(lrsQry, "TransAMDate"));
					laTransactionData.setTransTime(
						caDA.getIntFromDB(lrsQry, "TransTime"));
					laTransactionData.setTransCd(
						caDA.getStringFromDB(lrsQry, "TransCd"));
					laTransactionData.setTransId(
						caDA.getStringFromDB(lrsQry, "TransId"));
					laTransactionData.setDocNo(
						caDA.getStringFromDB(lrsQry, "DocNo"));
					laTransactionData.setVIN(
						caDA.getStringFromDB(lrsQry, "VIN"));
					laTransactionData.setRegPltNo(
						caDA.getStringFromDB(lrsQry, "RegPltNo"));
					laTransactionData.setTransEmpId(new String());
					laTransactionData.setPndngIRenew(true);
					lvRslt.add(laTransactionData);
				}
			}
			lrsQry = null;

			Log.write(Log.METHOD, this, csMethod + END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Query Incomplete trans where PrintImmediate in (1,2) 
	 * 
	 * @param aaGeneralSearchData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryPrintImmediateIncomplTrans(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		csMethod = "- qryPrintImmediateIncomplTrans - ";

		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// Determine if any transactions exist where TransAMDate < 
		// Current Date
		lsQry.append(
			" SELECT A.OFCISSUANCENO, A.TRANSWSID,A.TRANSAMDATE, "
				+ " B.TRANSTIME,A.CUSTSEQNO,B.CUSTNAME1,B.TRANSCD,B.VIN "
				+ " FROM "
				+ " RTS.RTS_TRANS_HDR A, "
				+ " RTS.RTS_TRANS B "
				+ " WHERE  "
				+ " A.OFCISSUANCENO = ? AND  "
				+ " A.SUBSTAID = ? AND  "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID  = B.SUBSTAID AND "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " A.TRANSWSID = B.TRANSWSID AND A.CUSTSEQNO = "
				+ " B.CUSTSEQNO AND TRANSTIMESTMP IS NULL AND "
				+ " A.TRANSAMDATE < ? AND "
				+ " PRINTIMMEDIATE in (1,2) ");

		try
		{
			// OfcIssuanceNo 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));

			// SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));

			// TransAMDate 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey4())));

			// end defect 9085 

			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);

			while (lrsQry.next())
			{
				SetAsideTransactionReportData laTransactionData =
					new SetAsideTransactionReportData();
				laTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionData.setCustName1(
					caDA.getStringFromDB(lrsQry, "CustName1"));
				laTransactionData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));

				// Add element to the Vector

				lvRslt.addElement(laTransactionData);

			} //End of While 

			lrsQry.close();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	* Method to Determine Max Transaction TransAMDate/TransTime
	* 
	* @param  aaTransactionData  TransactionData	
	* @return Vector
	* @throws RTSException 	
	*/
	public Vector qryMaxTransaction(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- qryMaxTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		// defect 10734 
		lsQry.append(
			"SELECT OFCISSUANCENO,SUBSTAID,TRANSWSID,TRANSAMDATE,"
				+ " CUSTSEQNO,TRANSTIME "
				+ " FROM RTS.RTS_TRANS A "
				+ " WHERE  "
				+ " OFCISSUANCENO = ? AND "
				+ " SUBSTAID  = ? AND "
				+ " TRANSWSID = ?  AND "
				+ " TRANSTIME <= "
				+ CommonConstant.MAX_POS_TRANSTIME
				+ " AND "
				+ " TRANSAMDATE*1000000.0+TRANSTIME =  "
				+ " (SELECT MAX(TRANSAMDATE*1000000.0+TRANSTIME ) "
				+ " FROM RTS.RTS_TRANS B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND B.TRANSTIME <="
				+ CommonConstant.MAX_POS_TRANSTIME
				+ " )");
				
		// defect 10865 
		// Union to RTS_FRAUD_LOG to pull latest insert  
		lsQry.append(
					"UNION "
					+ " SELECT OFCISSUANCENO,SUBSTAID,TRANSWSID,TRANSAMDATE,"
						+ " 0 AS CUSTSEQNO,TRANSTIME "
						+ " FROM RTS.RTS_FRAUD_LOG A "
						+ " WHERE  "
						+ " OFCISSUANCENO = ? AND "
						+ " SUBSTAID  = ? AND "
						+ " TRANSWSID = ?  AND "
						+ " TRANSAMDATE*1000000.0+TRANSTIME =  "
						+ " (SELECT MAX(TRANSAMDATE*1000000.0+TRANSTIME ) "
						+ " FROM RTS.RTS_FRAUD_LOG B "
						+ " WHERE "
						+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
						+ " A.SUBSTAID = B.SUBSTAID AND  "
						+ " A.TRANSWSID = B.TRANSWSID " 
						+ " )");
		// end defect 10865 

		// defect 9891 
		// Union to TransPayment for same Workstation to determine
		// last DB insert.  Order by TransAMDate Desc, TransTime Desc  		 
		lsQry.append(
			" UNION "
				+ " SELECT OFCISSUANCENO,SUBSTAID,TRANSWSID,TRANSAMDATE,"
				+ " CUSTSEQNO,TRANSTIME "
				+ " FROM RTS.RTS_TRANS_PAYMENT A "
				+ " WHERE  "
				+ " OFCISSUANCENO = ? AND "
				+ " SUBSTAID  = ? AND "
				+ " TRANSWSID = ?  AND "
				+ " TRANSTIME IS NOT NULL AND "
				+ " A.TRANSTIME <= "
				+ CommonConstant.MAX_POS_TRANSTIME
				+ " AND "
				+ " TRANSAMDATE*1000000.0+TRANSTIME =  "
				+ " (SELECT MAX(TRANSAMDATE*1000000.0+TRANSTIME ) "
				+ " FROM RTS.RTS_TRANS_PAYMENT B "
				+ " WHERE "
				+ " B.TRANSTIME IS NOT NULL AND "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND B.TRANSTIME <= "
				+ CommonConstant.MAX_POS_TRANSTIME
				+ " )"
				+ " ORDER BY 4 DESC ,6 DESC");
		// end defect 10734 

	
		for (int i = 0; i < 3; i++)
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransWsId())));
		}
		// end defect 9891 
		try
		{
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN+"1");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END+"1");

			while (lrsQry.next())
			{
				TransactionData laTransactionData =
					new TransactionData();
				laTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionData);
			} //End of While 

			lrsQry.close();
			caDA.closeLastDBStatement();
			
			// defect 11073
			//lrsQry = null;
			lsQry = new StringBuffer();
			lvValues = new Vector(); 
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionData.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionData.getSubstaId())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionData.getTransWsId())));
			lsQry.append(
					"SELECT OFCISSUANCENO,SUBSTAID,WSID,SYSDATE,"
						+ " SYSTIME "
						+ " FROM RTS.RTS_LOG_FUNC_TRANS A "
						+ " WHERE  "
						+ " OFCISSUANCENO = ? AND "
						+ " SUBSTAID  = ? AND "
						+ " WSID = ?  AND "
						+ " SYSDATE*100000000.0+SYSTIME =  "
						+ " (SELECT MAX(SYSDATE*100000000.0+SYSTIME ) "
						+ " FROM RTS.RTS_LOG_FUNC_TRANS B "
						+ " WHERE "
						+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
						+ " A.SUBSTAID = B.SUBSTAID AND  "
						+ " A.WSID = B.WSID "
						+ " )");
			
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN+"2");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END+"2");

			while (lrsQry.next())
			{
				TransactionData laTransactionData =
					new TransactionData();
				laTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				
				int liSysDate = caDA.getIntFromDB(lrsQry, "SysDate");
				RTSDate laRTSSysDate = new RTSDate(RTSDate.YYYYMMDD,liSysDate); 
				laTransactionData.setTransAMDate(laRTSSysDate.getAMDate());
				
				laTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "WsId"));
				laTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "SysTime"));
				// Add element to the Vector
				lvRslt.addElement(laTransactionData);
			} //End of While 

			lrsQry.close();
			caDA.closeLastDBStatement();
			// end defect 11073 
			
			Log.write(Log.METHOD, this, csMethod + END);

			if (lvRslt.size() == 0)
			{
				TransactionData laTransactionData =
					new TransactionData();
				laTransactionData.setOfcIssuanceNo(
					aaTransactionData.getOfcIssuanceNo());
				laTransactionData.setSubstaId(
					aaTransactionData.getSubstaId());
				laTransactionData.setTransAMDate(0);
				laTransactionData.setTransWsId(
					aaTransactionData.getTransWsId());
				laTransactionData.setCustSeqNo(0);
				laTransactionData.setTransTime(0);
				// Add element to the Vector
				lvRslt.addElement(laTransactionData);
			}
			
			// defect 11073 
			UtilityMethods.sort(lvRslt);
			// end defect 11073 
			
			return lvRslt;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Method to Count Title Transactions
	 * 
	 * @param  aaTransactionData  TransactionData	
	 * @return Vector
	 * @throws RTSException 	
	 */
	public int qryTitleTransactionCount(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- qryTitleTransactionCount - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		int liTitleTransCount = 0;

		lsQry.append(
			"SELECT COUNT(*) as TitleTransCount "
				+ " FROM RTS.RTS_TRANS_HDR A, RTS.RTS_TRANS B "
				+ " WHERE "
				+ " A.OFCISSUANCENO = ? AND "
				+ " A.SUBSTAID  = ? AND "
				+ " A.TRANSAMDATE  = ? AND "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.SUBSTAID = B.SUBSTAID AND  "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ " A.TRANSTIMESTMP IS NOT NULL AND "
				+ " B.TRANSCD IN ('TITLE','CORTTL','REJCOR','DTAORD',"
				+ " 'DTAORK')");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransactionData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransactionData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaTransactionData.getTransAMDate())));
		try
		{
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);

			while (lrsQry.next())
			{
				liTitleTransCount =
					caDA.getIntFromDB(lrsQry, "TitleTransCount");
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + END);
			return (liTitleTransCount);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Method to select data for SendTrans.
	 * Data is split into a given range of offices. 
	 * 
	 * @param  liStartOfc int
	 * @param  liEndOfc   int
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryTransaction(int liStartOfc, int liEndOfc)
		throws RTSException
	{
		csMethod = "- qryTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		ResultSet lrsQry;

		// defect 10491 
		// add AddPrmtRecIndi, DelPrmtRecIndi 
		// defect 8959
		// add MfVersionNo to the where clause
		lsQry.append(
			"SELECT A.OfcIssuanceNo,"
				+ "B.OfcIssuanceCd,"
				+ "a.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "B.CashWsId,"
				+ "0 as IncomplTransIndi, "
				+ "TransTime,"
				+ "TransCd,"
				+ "A.TransEmpId,"
				+ "TransPostedMfIndi,"
				+ "1 as TransPostedLANIndi,"
				+ "B.VersionCd,"
				+ " 0 as BranchOfcId, "
				+ "VoidedTransIndi,"
				+ "RegPltNo,"
				+ "DocNo,"
				+ "OwnrId,"
				+ "VIN,"
				+ "RegStkrNo,"
				+ "CustName1,"
				+ "CustName2,"
				+ "CustLstName,"
				+ "CustFstName,"
				+ "CustMIName,"
				+ "CustSt1,"
				+ "CustSt2,"
				+ "CustCity,"
				+ "CustState,"
				+ "CustZpCd,"
				+ "CustZpCdP4,"
				+ "CustCntry,"
				+ "DLSCertNo,"
				+ "EffDate,"
				+ "EffTime,"
				+ "ExpDate,"
				+ "ExpTime,"
				+ "RegClassCd,"
				+ "TireTypeCd,"
				+ "VehBdyType,"
				+ "VehCaryngCap,"
				+ "VehEmptyWt,"
				+ "VehGrossWt,"
				+ "VehMk,"
				+ "VehModlYr,"
				+ "VehRegState,"
				+ "VehUnitNo,"
				+ "VoidOfcIssuanceNo,"
				+ "VoidTransWsId,"
				+ "VoidTransAMDate,"
				+ "VoidTransTime,"
				+ "DieselIndi,"
				+ "RegPnltyChrgIndi,"
				+ "ProcsdByMailIndi,"
				+ "0 as BsnDate,"
				+ "0 as BsnDateTotalAmt,"
				+ "VoidTransCd, "
				+ "AddPrmtRecIndi,"
				+ "DelPrmtRecIndi "
				+ "FROM  RTS.RTS_TRANS A, RTS.RTS_TRANS_HDR B  "
				+ "WHERE "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "TransPostedMfIndi = 0 "
				+ "AND B.TRANSTIMESTMP IS NOT NULL "
				+ "AND a.OfcIssuanceNo between "
				+ liStartOfc
				+ " AND "
				+ liEndOfc
				+ " AND B.VERSIONCD = "
				+ SystemProperty.getMainFrameVersion());
		// end defect 8959
		// end defect 10491 

		try
		{
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + SQL + END);

			while (lrsQry.next())
			{
				TransactionData laTransactionData =
					new TransactionData();
				laTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laTransactionData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMfIndi"));
				laTransactionData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				laTransactionData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laTransactionData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laTransactionData.setOwnrId(
					caDA.getStringFromDB(lrsQry, "OwnrId"));
				laTransactionData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laTransactionData.setRegStkrNo(
					caDA.getStringFromDB(lrsQry, "RegStkrNo"));
				laTransactionData.setCustName1(
					caDA.getStringFromDB(lrsQry, "CustName1"));
				laTransactionData.setCustName2(
					caDA.getStringFromDB(lrsQry, "CustName2"));
				laTransactionData.setCustLstName(
					caDA.getStringFromDB(lrsQry, "CustLstName"));
				laTransactionData.setCustFstName(
					caDA.getStringFromDB(lrsQry, "CustFstName"));
				laTransactionData.setCustMIName(
					caDA.getStringFromDB(lrsQry, "CustMIName"));
				laTransactionData.setCustSt1(
					caDA.getStringFromDB(lrsQry, "CustSt1"));
				laTransactionData.setCustSt2(
					caDA.getStringFromDB(lrsQry, "CustSt2"));
				laTransactionData.setCustCity(
					caDA.getStringFromDB(lrsQry, "CustCity"));
				laTransactionData.setCustState(
					caDA.getStringFromDB(lrsQry, "CustState"));
				laTransactionData.setCustZpCd(
					caDA.getStringFromDB(lrsQry, "CustZpCd"));
				laTransactionData.setCustZpCdP4(
					caDA.getStringFromDB(lrsQry, "CustZpCdP4"));
				laTransactionData.setCustCntry(
					caDA.getStringFromDB(lrsQry, "CustCntry"));
				laTransactionData.setDLSCertNo(
					caDA.getStringFromDB(lrsQry, "DLSCertNo"));
				laTransactionData.setEffDate(
					caDA.getIntFromDB(lrsQry, "EffDate"));
				laTransactionData.setEffTime(
					caDA.getIntFromDB(lrsQry, "EffTime"));
				laTransactionData.setExpDate(
					caDA.getIntFromDB(lrsQry, "ExpDate"));
				laTransactionData.setExpTime(
					caDA.getIntFromDB(lrsQry, "ExpTime"));
				laTransactionData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laTransactionData.setTireTypeCd(
					caDA.getStringFromDB(lrsQry, "TireTypeCd"));
				laTransactionData.setVehBdyType(
					caDA.getStringFromDB(lrsQry, "VehBdyType"));
				laTransactionData.setVehCaryngCap(
					caDA.getIntFromDB(lrsQry, "VehCaryngCap"));
				laTransactionData.setVehEmptyWt(
					caDA.getIntFromDB(lrsQry, "VehEmptyWt"));
				laTransactionData.setVehGrossWt(
					caDA.getIntFromDB(lrsQry, "VehGrossWt"));
				laTransactionData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laTransactionData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laTransactionData.setVehRegState(
					caDA.getStringFromDB(lrsQry, "VehRegState"));
				laTransactionData.setVehUnitNo(
					caDA.getStringFromDB(lrsQry, "VehUnitNo"));
				laTransactionData.setVoidOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "VoidOfcIssuanceNo"));
				laTransactionData.setVoidTransWsId(
					caDA.getIntFromDB(lrsQry, "VoidTransWsId"));
				laTransactionData.setVoidTransAMDate(
					caDA.getIntFromDB(lrsQry, "VoidTransAMDate"));
				laTransactionData.setVoidTransTime(
					caDA.getIntFromDB(lrsQry, "VoidTransTime"));
				laTransactionData.setDieselIndi(
					caDA.getIntFromDB(lrsQry, "DieselIndi"));
				laTransactionData.setRegPnltyChrgIndi(
					caDA.getIntFromDB(lrsQry, "RegPnltyChrgIndi"));
				laTransactionData.setProcsdByMailIndi(
					caDA.getIntFromDB(lrsQry, "ProcsdByMailIndi"));
				laTransactionData.setBsnDate(
					caDA.getIntFromDB(lrsQry, "BsnDate"));
				laTransactionData.setBsnDateTotalAmt(
					caDA.getDollarFromDB(lrsQry, "BsnDateTotalAmt"));
				laTransactionData.setVoidTransCd(
					caDA.getStringFromDB(lrsQry, "VoidTransCd"));
				// SendTrans
				laTransactionData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laTransactionData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laTransactionData.setIncomplTransIndi(
					caDA.getIntFromDB(lrsQry, "IncomplTransIndi"));
				laTransactionData.setTransPostedLANIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedLANIndi"));
				laTransactionData.setVersionCd(
					caDA.getIntFromDB(lrsQry, "VersionCd"));
				laTransactionData.setBranchOfcId(
					caDA.getIntFromDB(lrsQry, "BranchOfcId"));

				// defect 10491 
				laTransactionData.setAddPrmtRecIndi(
					caDA.getIntFromDB(lrsQry, "AddPrmtRecIndi"));
				laTransactionData.setDelPrmtRecIndi(
					caDA.getIntFromDB(lrsQry, "DelPrmtRecIndi"));
				// end defect 10491 

				// Add element to the Vector
				lvRslt.addElement(laTransactionData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	* Method to Query RTS.RTS_TRANS where complete
	* 
	* @param  aaTransactionData  TransactionData	
	* @return Vector 	
	* @throws RTSException
	*/
	public Vector qryTransaction(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- qryTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		// defect 10491 
		// add AddPrmtRecIndi, DelPrmtRecIndi 
		// SendTrans	
		lsQry.append(
			"SELECT A.OfcIssuanceNo,"
				+ "B.OfcIssuanceCd,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "B.CashWsId,"
				+ "0 as IncomplTransIndi, "
				+ "TransTime,"
				+ "TransCd,"
				+ "A.TransEmpId,"
				+ "TransPostedMfIndi,"
				+ "1 as TransPostedLANIndi,"
				+ "B.VersionCd,"
				+ " 0 as BranchOfcId, "
				+ "VoidedTransIndi,"
				+ "RegPltNo,"
				+ "DocNo,"
				+ "OwnrId,"
				+ "VIN,"
				+ "RegStkrNo,"
				+ "CustName1,"
				+ "CustName2,"
				+ "CustLstName,"
				+ "CustFstName,"
				+ "CustMIName,"
				+ "CustSt1,"
				+ "CustSt2,"
				+ "CustCity,"
				+ "CustState,"
				+ "CustZpCd,"
				+ "CustZpCdP4,"
				+ "CustCntry,"
				+ "DLSCertNo,"
				+ "EffDate,"
				+ "EffTime,"
				+ "ExpDate,"
				+ "ExpTime,"
				+ "RegClassCd,"
				+ "TireTypeCd,"
				+ "VehBdyType,"
				+ "VehCaryngCap,"
				+ "VehEmptyWt,"
				+ "VehGrossWt,"
				+ "VehMk,"
				+ "VehModlYr,"
				+ "VehRegState,"
				+ "VehUnitNo,"
				+ "VoidOfcIssuanceNo,"
				+ "VoidTransWsId,"
				+ "VoidTransAMDate,"
				+ "VoidTransTime,"
				+ "DieselIndi,"
				+ "RegPnltyChrgIndi,"
				+ "ProcsdByMailIndi,"
				+ "BsnDate,"
				+ "BsnDateTotalAmt,"
				+ "VoidTransCd, "
				+ "AddPrmtRecIndi,"
				+ "DelPrmtRecIndi "
				+ "FROM  RTS.RTS_TRANS A, RTS.RTS_TRANS_HDR B  "
				+ "WHERE "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND ");
		// end defect 10491 

		if (aaTransactionData.getTransPostedMfIndi() == 1)
		{
			lsQry.append(
				" TransPostedMfIndi = 0 AND  B.TRANSTIMESTMP IS "
					+ " NOT NULL ORDER BY TRANSAMDATE,TRANSWSID,"
					+ " CUSTSEQNO,TRANSTIME ");
		}
		else
		{
			lsQry.append(
				"A.OfcIssuanceNo = ? AND "
					+ "A.SubstaId = ? AND "
					+ " A.TransAMDate = ? AND"
					+ " A.TransWsId = ? AND"
					+ " A.CustSeqNo = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSeqNo())));
			if (aaTransactionData.getTransTime() != Integer.MIN_VALUE)
			{
				lsQry.append(" AND A.TransTime = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaTransactionData.getTransTime())));
			}
		}
		try
		{
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);

			while (lrsQry.next())
			{
				TransactionData laTransactionData =
					new TransactionData();
				laTransactionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laTransactionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laTransactionData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laTransactionData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laTransactionData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laTransactionData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laTransactionData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMfIndi"));
				laTransactionData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				laTransactionData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laTransactionData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laTransactionData.setOwnrId(
					caDA.getStringFromDB(lrsQry, "OwnrId"));
				laTransactionData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laTransactionData.setRegStkrNo(
					caDA.getStringFromDB(lrsQry, "RegStkrNo"));
				laTransactionData.setCustName1(
					caDA.getStringFromDB(lrsQry, "CustName1"));
				laTransactionData.setCustName2(
					caDA.getStringFromDB(lrsQry, "CustName2"));
				laTransactionData.setCustLstName(
					caDA.getStringFromDB(lrsQry, "CustLstName"));
				laTransactionData.setCustFstName(
					caDA.getStringFromDB(lrsQry, "CustFstName"));
				laTransactionData.setCustMIName(
					caDA.getStringFromDB(lrsQry, "CustMIName"));
				laTransactionData.setCustSt1(
					caDA.getStringFromDB(lrsQry, "CustSt1"));
				laTransactionData.setCustSt2(
					caDA.getStringFromDB(lrsQry, "CustSt2"));
				laTransactionData.setCustCity(
					caDA.getStringFromDB(lrsQry, "CustCity"));
				laTransactionData.setCustState(
					caDA.getStringFromDB(lrsQry, "CustState"));
				laTransactionData.setCustZpCd(
					caDA.getStringFromDB(lrsQry, "CustZpCd"));
				laTransactionData.setCustZpCdP4(
					caDA.getStringFromDB(lrsQry, "CustZpCdP4"));
				laTransactionData.setCustCntry(
					caDA.getStringFromDB(lrsQry, "CustCntry"));
				laTransactionData.setDLSCertNo(
					caDA.getStringFromDB(lrsQry, "DLSCertNo"));
				laTransactionData.setEffDate(
					caDA.getIntFromDB(lrsQry, "EffDate"));
				laTransactionData.setEffTime(
					caDA.getIntFromDB(lrsQry, "EffTime"));
				laTransactionData.setExpDate(
					caDA.getIntFromDB(lrsQry, "ExpDate"));
				laTransactionData.setExpTime(
					caDA.getIntFromDB(lrsQry, "ExpTime"));
				laTransactionData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laTransactionData.setTireTypeCd(
					caDA.getStringFromDB(lrsQry, "TireTypeCd"));
				laTransactionData.setVehBdyType(
					caDA.getStringFromDB(lrsQry, "VehBdyType"));
				laTransactionData.setVehCaryngCap(
					caDA.getIntFromDB(lrsQry, "VehCaryngCap"));
				laTransactionData.setVehEmptyWt(
					caDA.getIntFromDB(lrsQry, "VehEmptyWt"));
				laTransactionData.setVehGrossWt(
					caDA.getIntFromDB(lrsQry, "VehGrossWt"));
				laTransactionData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laTransactionData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laTransactionData.setVehRegState(
					caDA.getStringFromDB(lrsQry, "VehRegState"));
				laTransactionData.setVehUnitNo(
					caDA.getStringFromDB(lrsQry, "VehUnitNo"));
				laTransactionData.setVoidOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "VoidOfcIssuanceNo"));
				laTransactionData.setVoidTransWsId(
					caDA.getIntFromDB(lrsQry, "VoidTransWsId"));
				laTransactionData.setVoidTransAMDate(
					caDA.getIntFromDB(lrsQry, "VoidTransAMDate"));
				laTransactionData.setVoidTransTime(
					caDA.getIntFromDB(lrsQry, "VoidTransTime"));
				laTransactionData.setDieselIndi(
					caDA.getIntFromDB(lrsQry, "DieselIndi"));
				laTransactionData.setRegPnltyChrgIndi(
					caDA.getIntFromDB(lrsQry, "RegPnltyChrgIndi"));
				laTransactionData.setProcsdByMailIndi(
					caDA.getIntFromDB(lrsQry, "ProcsdByMailIndi"));
				laTransactionData.setBsnDate(
					caDA.getIntFromDB(lrsQry, "BsnDate"));
				laTransactionData.setBsnDateTotalAmt(
					caDA.getDollarFromDB(lrsQry, "BsnDateTotalAmt"));
				laTransactionData.setVoidTransCd(
					caDA.getStringFromDB(lrsQry, "VoidTransCd"));
				// SendTrans
				laTransactionData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laTransactionData.setCashWsId(
					caDA.getIntFromDB(lrsQry, "CashWsId"));
				laTransactionData.setIncomplTransIndi(
					caDA.getIntFromDB(lrsQry, "IncomplTransIndi"));
				laTransactionData.setTransPostedLANIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedLANIndi"));
				laTransactionData.setVersionCd(
					caDA.getIntFromDB(lrsQry, "VersionCd"));
				laTransactionData.setBranchOfcId(
					caDA.getIntFromDB(lrsQry, "BranchOfcId"));

				// defect 10491 
				laTransactionData.setAddPrmtRecIndi(
					caDA.getIntFromDB(lrsQry, "AddPrmtRecIndi"));
				laTransactionData.setDelPrmtRecIndi(
					caDA.getIntFromDB(lrsQry, "DelPrmtRecIndi"));
				// end defect 10491 	

				// Add element to the Vector
				lvRslt.addElement(laTransactionData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Method to Query RTS.RTS_TRANS for TransAMDates eligible for purge
	 * 
	 * @param  aiPurgeAMDate int 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTransactionDatesForPurge(int aiPurgeAMDate)
		throws RTSException
	{
		csMethod = "- qryTransactionDatesForPurge - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		// defect 10185
		// Pull dates from 9 Transaction Tables 
		// UNION will only pull the distinct dates   
		// defect 9011 
		// Use <= vs. < 
		lsQry.append(
			"SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_TRANS_HDR WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_TRANS WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_TR_FDS_DETAIL WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_TR_INV_DETAIL WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_MV_FUNC_TRANS WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_FUND_FUNC_TRNS WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_INV_FUNC_TRANS WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_SR_FUNC_TRANS WHERE  "
				+ " TRANSAMDATE <= ? "
				+ " UNION SELECT DISTINCT TRANSAMDATE "
				+ "FROM  RTS.RTS_TRANS_PAYMENT WHERE  "
				+ " TRANSAMDATE <= ?");
		// end defect 9011

		// Implement Loop
		for (int i = 0; i < 9; i++)
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
		}
		// end defect 10185

		try
		{
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);

			// defect 8423
			// Return Vector of Integers vs. 
			//  Vector of TransactionData Objects
			while (lrsQry.next())
			{
				Integer laPurgeAMDate =
					new Integer(
						caDA.getIntFromDB(lrsQry, "TransAMDate"));

				lvRslt.addElement(laPurgeAMDate);
			}
			// end defect 8423

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		// defect 10185 
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		// end defect 10185 
	}

	/**
	* Method to Query RTS.RTS_TRANS for Void Transaction
	* 
	* @param  aaVoidTransactionData VoidTransactionData	
	* @return Vector
	* @throws RTSException  	
	*/
	public Vector qryTransactionForVoid(VoidTransactionData aaVoidTransactionData)
		throws RTSException
	{
		csMethod = "- qryTransactionForVoid - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		// defect 9085 
		// add inclusion of Special Plate RegPltNo  
		// Note use of Coalesce 
		lsQry.append(
			"SELECT A.OfcIssuanceNo,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "A.TransTime,"
				+ "a.TransCd,"
				+ "TransCdDesc,"
				+ "TransPostedMfIndi,"
				+ "VoidedTransIndi,"
				+ "VIN,"
				+ "VoidOfcIssuanceNo,"
				+ "VoidTransCd,"
				+ "VoidTransWsId,"
				+ "VoidTransAMDate,"
				+ "VoidTransTime, "
				+ "0 as InventoryIndi, "
				+ " Coalesce(C.REGPLTNO,'') as SpclPltRegPltNo "
				+ "FROM RTS.RTS_TRANS_CDS B, RTS.RTS_TRANS A  "
				+ "LEFT OUTER JOIN "
				+ "RTS.RTS_SR_FUNC_TRANS C ON "
				+ "A.OfcIssuanceNo = C.OfcIssuanceNo and "
				+ "A.SubstaId = C.SubstaId and "
				+ "A.TransAMDate = C.TransAMDate and "
				+ "A.TransWsId = C.TransWsId and "
				+ "A.TransTime = C.TransTime and "
				+ "A.CustSeqNo = C.CustSeqNo "
				+ " WHERE "
				+ "A.TRANSCD = B.TRANSCD AND "
				+ "A.OfcIssuanceNo = ? AND "
				+ "A.SubstaId = ? AND "
				+ "A.TransAMDate = ? AND "
				+ "A.TransWsId = ? AND "
				+ "A.CustSeqNo = ? ");

		// end defect 9085 
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
					aaVoidTransactionData.getCustSeqNo())));

		if (aaVoidTransactionData.getCustSeqNo() == 0)
		{
			lsQry.append(" and a.TransTime = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaVoidTransactionData.getTransTime())));
		}
		else
		{
			lsQry.append(
				" and not exists (select * from "
					+ " RTS.RTS_TR_INV_DETAIL C WHERE "
					+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND "
					+ " A.SUBSTAID = C.SUBSTAID AND "
					+ " A.TRANSAMDATE = C.TRANSAMDATE AND "
					+ " A.CUSTSEQNO = C.TRANSAMDATE AND "
					+ " A.TRANSTIME = C.TRANSTIME ) ");

			lsQry.append(
				"UNION SELECT A.OfcIssuanceNo,"
					+ "A.SubstaId,"
					+ "A.TransAMDate,"
					+ "A.TransWsId,"
					+ "A.CustSeqNo,"
					+ "A.TransTime,"
					+ "a.TransCd,"
					+ "TransCdDesc,"
					+ "TransPostedMfIndi,"
					+ "VoidedTransIndi,"
					+ "VIN,"
					+ "VoidOfcIssuanceNo,"
					+ "VoidTransCd,"
					+ "VoidTransWsId,"
					+ "VoidTransAMDate,"
					+ "VoidTransTime, "
					+ "1 as InventoryIndi, "
					+ " Coalesce(C.REGPLTNO,'') as SpclPltRegPltNo "
					+ "FROM RTS.RTS_TRANS_CDS B, RTS.RTS_TRANS A  "
					+ "LEFT OUTER JOIN "
					+ "RTS.RTS_SR_FUNC_TRANS C ON "
					+ "A.OfcIssuanceNo = C.OfcIssuanceNo and "
					+ "A.SubstaId = C.SubstaId and "
					+ "A.TransAMDate = C.TransAMDate and "
					+ "A.TransWsId = C.TransWsId and "
					+ "A.TransTime = C.TransTime and "
					+ "A.CustSeqNo = C.CustSeqNo "
					+ " WHERE "
					+ "A.TRANSCD = B.TRANSCD AND "
					+ "A.OfcIssuanceNo = ? AND "
					+ "A.SubstaId = ? AND "
					+ "A.TransAMDate = ? AND "
					+ "A.TransWsId = ? AND "
					+ "A.CustSeqNo = ? "
					+ " and exists (select * from "
					+ " RTS.RTS_TR_INV_DETAIL C WHERE "
					+ " A.OFCISSUANCENO = C.OFCISSUANCENO AND "
					+ " A.SUBSTAID = C.SUBSTAID AND "
					+ " A.TRANSAMDATE = C.TRANSAMDATE AND "
					+ " A.CUSTSEQNO = C.TRANSAMDATE AND "
					+ " A.TRANSTIME = C.TRANSTIME ) order by 6 ");

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
						aaVoidTransactionData.getCustSeqNo())));
		}

		try
		{
			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);
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
				laVoidTransactionData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laVoidTransactionData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laVoidTransactionData.setTransCdDesc(
					caDA.getStringFromDB(lrsQry, "TransCdDesc"));
				laVoidTransactionData.setTransPostedMfIndi(
					caDA.getIntFromDB(lrsQry, "TransPostedMfIndi"));
				laVoidTransactionData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				laVoidTransactionData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laVoidTransactionData.setVoidOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "VoidOfcIssuanceNo"));
				laVoidTransactionData.setVoidTransWsId(
					caDA.getIntFromDB(lrsQry, "VoidTransWsId"));
				laVoidTransactionData.setVoidTransAMDate(
					caDA.getIntFromDB(lrsQry, "VoidTransAMDate"));
				laVoidTransactionData.setVoidTransTime(
					caDA.getIntFromDB(lrsQry, "VoidTransTime"));
				laVoidTransactionData.setInventoryIndi(
					caDA.getIntFromDB(lrsQry, "InventoryIndi"));
				laVoidTransactionData.setVoidTransCd(
					caDA.getStringFromDB(lrsQry, "VoidTransCd"));
				laVoidTransactionData.setSpclPltRegPltNo(
					caDA.getStringFromDB(lrsQry, "SpclPltRegPltNo"));
				// Add element to the Vector
				lvRslt.addElement(laVoidTransactionData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Method to update RTS.RTS_TRANS for Void
	 * 
	 * @param aaTransactionData  TransactionData	
	 * @throws RTSException 	
	 */
	public void voidTransaction(TransactionData aaTransactionData)
		throws RTSException
	{
		csMethod = "- voidTransaction - ";
		Log.write(Log.METHOD, this, csMethod + BEGIN);

		Vector lvValues = new Vector();
		String lsUpd =
			"UPDATE RTS.RTS_TRANS SET "
				+ "VOIDEDTRANSINDI = 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId  = ? AND "
				+ "TransWsId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransTime = ? AND "
				+ "CustSeqNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransactionData.getCustSeqNo())));

			Log.write(Log.SQL, this, csMethod + SQL + BEGIN);
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, csMethod + SQL + END);
			Log.write(Log.METHOD, this, csMethod + END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}