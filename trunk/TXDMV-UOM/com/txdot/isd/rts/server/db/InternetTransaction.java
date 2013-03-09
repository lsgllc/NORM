package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.webapps.reports.InternetTransReportData;
import com.txdot.isd.rts.server.webapps.util.MQLog;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.CompleteRegRenData;
import com.txdot.isd.rts.services.data.EmailData;
import com.txdot.isd.rts.services.data.InternetRegRecData;
import com.txdot.isd.rts.services.data.InternetTransactionData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.PaymentData;
import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.data.VehicleBaseData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 * InternetTransaction.java 
 *
 * (c) Texas Department of Transportation  2001
 *
 *----------------------------------------------------------------------
 * Change History 
 * Name         Date        Description
 *---------------------------------------------------------------------- 
 * Clifford		09/03/2002	DB down handling.
 * 							defect 3700 	
 * Clifford		09/23/2002	REG 103 Escape
 * 							defect 4681	
 * Clifford		10/08/2002	PCR--insurance not required
 * B Brown 	    04/08/2003  In method qryRenewal,commented
 *							out endDate = String.valueOf(new Integer
 *							(endDate).intValue() + 1);so the end date is
 *							not avdanced by 1 day arbitrarily.
 *							defect 5898 
 * B Brown      05/01/2003  In method getInetData,added a 
 *             				iData.getCompleteRegRenData().setInsurance
 *                          Required(true) statementto make sure the
 *                          insurance required button is enabled when
 *                          insurance data is present.
 * 							defect 6051 
 * B Brown      07/01/2003  In method updateCntyStatus
 *							(VehicleBaseData, int, String), changed the
 *							update SQL to update the DB record with MQ
 *							status (cntystatuscd) info only if the
 *				   			cntystatus on the DB is not equal to 7
 *							(refund approved). This is to prevent an MQ
 *							record that was "held up" in TxDOT MQ
 *							(because MQ was down), from updating a
 *                          refund
 *  			   			approved record with refund failed. This
 *							could happen because a valid refund failed
 *          			    record, even though it happened first, could
 *							come in through MQ after the successful  
 *             			    refund record was processed.
 * 							defect 6241 
 * B Brown 		07/08/2003  In method qryAddressChange Report,
 *                 			changed the "to date" part of the query, so
 *							when the user enters the "to date", an 
 *                          ending
 *							time of 23:59:999 is added to the end date
 *                          part
 *        			        of the query, so data from the whole day is
 *							retieved.
 *							defect 6194 
 * B Brown      08/20/2003  Added method qrySpecialPlate
 *                          AddrChgReport() to produce a report of all
 *                          Special
 *							plate address changes.Also, removed method
 *							qrySpecialPlateAddrChgReport(ReportSearch
 *                          Data).
 * 							defect 4885 
 * B Brown		03/01/2004  Added getInetDataForMQTest(ResultSet)
 *							Added getQryRenewalForMQTest()
 *							Added qryBatchConvFeePayments()
 *							Added qryRenewalForMQTest(Hashtable)
 *							Changed qryBatchPayments()
 *							Changed updBatchPymntStatus(PaymentData)               
 *					   	 	defect 6604 Ver 5.1.6
 * B Brown      12/13/2004  There is a need to log all fields in the
 *                          update SQL when processed MQ records cause 
 *                          an sql problem, so we know what fields are
 *                          in error.
 *       					add logErrorMsg
 *                          modify updRenewal  
 *                          defect 7718 Ver 5.2.2  
 * B Brown      12/13/2004  Since the updRenewal method is now allowing
 *                          the cntystatuscd field to be updated to new
 *                          status even when itrntpymntstatuscd indicat
 *                          es an unpaid convenience fee, we need to 
 *                          also allow the INET failed payments process 
 *                          to update itrntpymntstatuscd to an "all fees
 *                          paid" status, when the conv fee payment MQ 
 *                          record comes in.
 *                          modify  updRenewal,
 *       					qryRenewal(VehicleBaseData, String)
 *                          defect 7696 Ver 5.2.2
 * Jeff S.		02/24/2005	Get code up to standards. Changed a 
 * 							non-static call to a static call.
 * 							modify getInetData(),getInetDataForMQTest(),
 * 							qryRenewalCount(),qryTransactionReport(),
 * 							updateCntyStatus(),updateStatusToNew(),
 * 							updBatchPymntStatus()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/17/2005	Made a change to method to return both the 
 * 							search keys and the results of the search so
 * 							that future searches could be done on the 
 * 							same search keys without going back to 
 * 							the search frame.
 * 							modify qryRenewal() - returns Hashtable 
 * 							instead of Vector, updBatchPymntStatus()
 * 							defect 8247 Ver 5.2.3
 * B Brown		08/19/2005	Purge "unpaid" internet records only if
 * 							no Epay information has been received - 
 * 							itrntpymntstatuscd = null. 
 * 							See InternetData.purgeItrntData(int, int)
 * 							modify purgeItrntIRenewTrans(int, int)
 * 							defect 8134 Ver 5.2.3
 * K Harrell	10/25/2005	Renamed from InternetRegistrationRenewalProc
 * 							and moved from server.webapps.db 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	11/13/2005	Moved methods from InternetRegistrationRenewal
 *							Streamline calls for Interent Purge
 * 							rename purgeTrans() to purgeItrntIRenewTrans()
 * 							Moved methods from InternetAddressChange
 * 							rename purgeTrans() to purgeItrntAddr() 
 * 							defect 7889,8385 Ver 5.2.3
 * B Brown      01/25/2006  Do not update the itrnt_trans table with MQ
 *       					information if the cntystatuscd = 1 (unpaid)
 *       					and the itrntpymntstatuscd is not null - 
 *       					still trying to collect fees.
 *       					modify updRenewal(CompleteRegRenData)
 *                          defect 8365 Ver 5.2.2 fix 8.
 * B Brown  	03/24/2006  Changed failed refunds to process cntystatus
 *       					cd's of 6 and 8.
 *      					modify qryBatchRefunds()
 *       					defect 6764 Ver 5.2.2 Fix 8
 * B Brown  	05/01/2006  Commented out code to make a second server
 * 							call from ServerTester, so we use method
 * 							getItrntTransDataForMQTest for its one 
 * 							function of getting data, and push the 
 * 							commented out functionality to the calling
 * 							program.  
 *       					modify getItrntTransDataForMQTest()
 *       					defect 8554 Ver 5.2.3
 * B Brown  	08/16/2006  Change an sql where clause to allow the
 * 							update to occur
 * 							modify updBatchPymntStatus()
 *							defect 8895 Ver 5.2.3
 * B Brown   	09/21/2006 	Added System.out.println for the "where 
 * 							clause"
 *        					SQL parameters to see why the update is not 
 *       					occurring for failed Epay payments and 
 * 							failed refunds
 *        					modify updRenewal()
 *        					modify updateCntyStatus()
 *      	 				defect 8966 Ver 5.2.6 
 * B Brown  	09/27/2006  Check for an MQ payment in process record
 * 							out of order being processed out of 
 * 							chronological order
 * 							modify updRenewal()
 *							defect 8961 Ver 5.2.6
 * B Brown  	10/13/2006  Put row to be updated in a lock status
 * 							to make sure we process records in order
 * 							due to 2 mq procesing queues.
 * 							add updItrntTransForLogicalLock() 
 * 							modify updRenewal()
 *							defect 8972 Ver Exempts
 * K Harrell	02/27/2007	Use PlateTypeCache vs. ItemCodesData
 * 							modify isSpecialPlate()
 * 							defect 9085 Ver Special Plates
 * B Brown		01/08/2009	Undid these changes:
 * 							Check to see if the trace number being sent
 * 							to Epay has 3 characters after the plate 
 * 							number. If it does, we strip off 
 * 							the last byte (credit card type), then send
 * 							to TxO and Epay.
 * 							modify qryBatchConvFeePayments(), 
 * 							qryBatchConvFeePayments(),qryBatchPayments()
 * 							defect 9878 Ver Defect_POS_C
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeIAddrIRnrTrans(), 
 * 							  purgeItrntIRenewTrans() 
 * 							defect 9825 Ver Defect_POS_D
 * B Brown		01/29/2009	Change batch refund process to collect the
 * 							remaining balance to be refunded for each
 * 							trace number trying to collect refunds.
 * 							add updateCntyStatus(int, String)
 * 							modify qryBatchRefunds() 
 * 							defect 8501 Ver Defect_POS_D  
 * K Harrell	02/09/2009  Modify to remove call for SpclPltAddrChngRpt
 * 							as no longer required. 
 * 							delete qrySpecialPlateAddrChngReport()
 * 							defect 9941 Ver Defect_POS_D 
 * B Brown		03/06/2009	Do not update itrnttimestmp on update
 * 							modify updRenewal()
 * 							defect 9944 Ver Defect_POS_E 
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F  
 * B Brown		11/17/2009	Add a query to determine same pymntamts
 * 							within a shopping cart.
 * 							add qryPymntamt()
 * 							defect 10040 Ver Defect_POS_H 
 * K Harrell	02/27/2010	Remove code for Internet Address Change 
 * 							 Report 
 * 							delete qryAddressChangeReport() 
 * 							defect 10387 Ver POS_640  	
 * K Harrell	03/19/2010	Remove unused method. 
 * 							delete updateRenewalAddress() 
 * 							defect 10239 Ver POS_640 
 * K Harrell	04/02/2010	add getQryRenewal(boolean) 
 * 							modify getQryRenewal(), 
 * 							 qryItrntRenew(), qryTransactionReport()
 * 							defect 10421 Ver POS_640
 * B Brown		05/10/2010	change refund amount from getDoubleFromDB
 * 							to getStringFromDB to keep 2 numbers to the
 * 							right of the decimal to get around TPE
 * 							edits.
 * 							modify qryBatchRefunds()
 * 							defect 10281 Ver POS_640
 * K Harrell	06/14/2010	populate TransId in RTS_ITRNT_TRANS 
 * 							modify updateTXInfo()
 * 							defect 10505 Ver 6.5.0 
 * B Brown		12/08/2010	Add udpate for emailrenwlreqcd for eReminder
 * 							modify updRenewalEmailIndi()
 * 							modify insItrntAddr()
 * 							defect 10610 Ver 6.6.0
 * B Brown		05/25/2011	Do not repost to MQ for NumberFormat
 * 							Exceptions.
 * 							modify updRenewal()
 * 							defect 10816 Ver 6.8.0
 * B Brown		09/15/2011	Change conversion of IRENEW vehicles total 
 * 							fees from aaCompRegRenData.getTotalFeesString()
 * 							to aaCompRegRenData.getTotalFees() to avoid 
 * 							Number Format exceptions for amounts $1,000 
 * 							or greater.
 * 							modify updRenewal()
 * 							defect 10998 Ver 6.8.1
 * B Brown		09/29/2011	Change logging to MQLog.info as writing
 * 							to this file is working in production.
 * 							Also add date/time to log writes without
 * 							date/time.
 * 							modify logErrorMsg(), updRenewal()
 * 							defect 10995 Ver 6.9.0
 * B Brown		10/18/2011	Use the new rts_itrnt_trans
 * 							itrntpymnttimestmp column containing regis 
 * 							fee capture time as the column for the 
 * 							process "New" sort order.
 * 							modify getQryRenewal(), qryNext(), 
 * 							updRenewal(), updBatchPymntStatus()
 * 							defect 10996 Ver 6.9.0
 * B Brown		11/09/2011	Use aaCompRegRenData.getTotalFees() to 
 * 							exclude conv fee whne the update to
 * 							rts_itrnt_trans.pymntamt happens.
 * 							Also remove date/time from System.outs
 * 							modify updRenewal().
 * 							defect 11133 Ver 6.9.0	
 *----------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database
 * 
 * @version	6.9.0 			09/21/2011
 * @author	George Donoso 
 * <br>Creation Date:		10/11/2001 11:28:42
 *
 */
public class InternetTransaction extends InternetRegRecData
{
	String csMethod = new String();

	DatabaseAccess caDA;

	private final static String TRUNCATION = "truncation";
	// defect 10816
	private final static String NFE = "NumberFormatException";
	private final String EMAIL_FROM = "TSD-RTS-POS@txdmv.gov";
	private final String EMAIL_TO   = "TSD-RTS-POS@txdmv.gov";
//		"bob.l.brown@txdmv.gov";
	private String csEmailSubject =
		"MQ Processing error";
	private final String PRODUCTION = "Production: ";
	private final String PROD_DATASOURCE = "P0RTSDB";
	private final String TEST = "Test: ";
	private final String EMAIL_BODY =
		"The MQ process failed to process a record and "
			+ "requires attention. <br> /rts/Production/logs/mq_err.log also contains "
			+ "error details.";
	private final String HTML_NEW_LINE = "<br>";


	// end defect 10816

	/**
	 * InternetTransaction Constructor
	 * 
	 * @throws RTSException
	 */
	public InternetTransaction() throws RTSException
	{
		caDA = new DatabaseAccess();
	}

	/**
	 * InternetTransaction Constructor
	 * 
	 * @param aaDateAccess
	 * @throws RTSException
	 */
	public InternetTransaction(DatabaseAccess aaDateAccess)
		throws RTSException
	{
		caDA = aaDateAccess;
	}

	/**
	 * Return the StringBuffer w/ the SQL Query for Renewal.
	 * 
	 * @return StringBuffer
	 */
	private static StringBuffer getQryRenewal()
	{
		// defect 10421 
		return getQryRenewal(false);
		// end defect 10421 
	}

	/**
	 * Return the StringBuffer w/ the SQL Query for Renewal.
	 * 
	 * @param abRefundApproved
	 * @return StringBuffer
	 */
	private static StringBuffer getQryRenewal(boolean abRefundApproved)
	{
		String lsSuffix =
			abRefundApproved
				? "RTS.RTS_ITRNT_TRANS_DEL_LOG "
				: "RTS.RTS_ITRNT_TRANS ";

		StringBuffer lsSqlQry =
			new StringBuffer(
				"SELECT "
					+ "CntyStatusCd,"
					+ "CntyReasnCd,"
					+ "CntyReasnTxt,"
					+ "SendEmailIndi,"
					+ "TransWsId,"
					+ "CntyPrcsdTimestmp,"
					+ "Substaid,"
					+ "RenwlMailngSt1,"
					+ "RenwlMailngSt2,"
					+ "RenwlMailngCity,"
					+ "RenwlMailngState,"
					+ "RenwlMailngZpCd,"
					+ "RenwlMailngZpCd4,"
					+ "FrstName,"
					+ "MidlName,"
					+ "PymntOrderId,"
					+ "LastName,"
					+ "EMailAddr,"
					+ "ItrntTraceNo,"
					+ "ItrntTimestmp,"
					+ "CustPhoneNo,"
					+ "RecpntName,"
					+ "InsCmpnyName,"
					+ "InsPlcyNo,"
					+ "InsAgentName,"
					+ "InsPlcyStartDate,"
					+ "InsPlcyEndDate,"
					+ "InsPhoneNo,"
					+ "RegPltNo,"
					+ "VIN,"
					+ "DocNo,"
					+ "OfcIssuanceNo,"
					+ "TransWsId,"
					+ "TransAmDate,"
					+ "TransTime,"
					+ "RegExpMo,"
					+ "RegExpYr,"
					+ "RegPltAge,"
					+ "VehGrossWt,"
					+ "VehEmptyWt,"
					+ "VehTon,"
					+ "TtlIssueDate,"
					+ "OwnrTtlName1,"
					+ "CnvncFee,"
					+ "PymntAmt"
					+ " FROM "
					+ lsSuffix);
		// + " RTS.RTS_ITRNT_TRANS ");
		return lsSqlQry;
	}

	/**
	 * This method is a changed copy of getQryRenweal(), and is used for
	 * simulated MQ testing.
	 * 
	 * @return StringBuffer
	 */
	private static StringBuffer getQryRenewalForMQTest()
	{
		StringBuffer lsSqlQry =
			new StringBuffer(
				"SELECT "
					+ "CntyStatusCd,"
					+ "CntyReasnCd,"
					+ "CntyReasnTxt,"
					+ "SendEmailIndi,"
					+ "TransWsId,"
					+ "CntyPrcsdTimestmp,"
					+ "Substaid,"
					+ "RenwlMailngSt1,"
					+ "RenwlMailngSt2,"
					+ "RenwlMailngCity,"
					+ "RenwlMailngState,"
					+ "RenwlMailngZpCd,"
					+ "RenwlMailngZpCd4,"
					+ "FrstName,"
					+ "MidlName,"
					+ "PymntOrderId,"
					+ "LastName,"
					+ "EMailAddr,"
					+ "ItrntTraceNo,"
					+ "ItrntTimestmp,"
					+ "CustPhoneNo,"
					+ "RecpntName,"
					+ "InsCmpnyName,"
					+ "InsPlcyNo,"
					+ "InsAgentName,"
					+ "InsPlcyStartDate,"
					+ "InsPlcyEndDate,"
					+ "InsPhoneNo,"
					+ "RegPltNo,"
					+ "VIN,"
					+ "DocNo,"
					+ "OfcIssuanceNo,"
					+ "TransWsId,"
					+ "TransAmDate,"
					+ "TransTime,"
					+ "RegExpMo,"
					+ "RegExpYr,"
					+ "RegPltAge,"
					+ "VehGrossWt,"
					+ "VehEmptyWt,"
					+ "VehTon,"
					+ "TtlIssueDate,"
					+ "OwnrTtlName1,"
					+ "CnvncFee,"
					+ "PymntAmt,"
					+ "ItrntPymntStatusCd"
					+ " FROM RTS.RTS_ITRNT_TRANS ");
		return lsSqlQry;
	}

	/**
	 * Delete Internet Transactions.
	 * 
	 * @param aaMFVehData MFVehicleData
	 * @param aaTransCd
	 * @throws RTSException
	 */
	public void delItrntAddr(
		String aaTransCd,
		MFVehicleData aaMFVehData)
		throws RTSException
	{
		csMethod = "delItrntAddr(String,MFVehicleData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE from RTS.RTS_ITRNT_TRANS where "
				+ "TransCd = ? and "
				+ "RegPltNo = ? and "
				+ "Vin = ? and "
				+ "DocNo = ?";
		try
		{
			lvValues.addElement(new DBValue(Types.VARCHAR, aaTransCd));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaMFVehData.getRegData().getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getVehicleData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaMFVehData.getTitleData().getDocNo()));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

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
	 * Only use for delete unpaid, declined_refund_approved records
	 * which are eligible for renewal again.
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 * @throws RTSException
	 */
	public void delItrntRenew(VehicleBaseData aaVehBaseData)
		throws RTSException
	{
		csMethod = "delItrntRenew(VehicleBaseData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelTrans =
			"delete from rts.rts_itrnt_trans "
				+ "where transcd = 'IRENEW' and "
				+ "REGPLTNO=? AND "
				+ "VIN=? AND "
				+ "DOCNO=? AND ";

		lsSqlDelTrans =
			lsSqlDelTrans
				+ "CNTYSTATUSCD IN ("
				+ CommonConstants.UNPAID
				+ ","
				+ CommonConstants.DECLINED_REFUND_APPROVED
				+ ")";

		Vector lvValues = new Vector();
		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getPlateNo()));
		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getVin()));
		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getDocNo()));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsSqlDelTrans, lvValues);

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
	 * Get the INET data.
	 * 
	 * @param aaResultSet
	 * @return InternetRegRecData
	 * @throws RTSException
	 */
	private InternetRegRecData getItrntTransData(ResultSet aaResultSet)
		throws RTSException
	{
		InternetRegRecData laItrntData = new InternetRegRecData();

		//populate InternetRegRegData class  
		laItrntData.setStatus(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "CntyStatusCd")));
		laItrntData.setReasonCd(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "CntyReasnCd")));
		laItrntData.setReasonComments(
			caDA.getStringFromDB(aaResultSet, "CntyReasnTxt"));
		laItrntData.setSendEmail(
			caDA.getIntFromDB(aaResultSet, "SendEmailIndi"));
		laItrntData.setEmployeeId(
			caDA.getStringFromDB(aaResultSet, "TransWsId"));
		laItrntData.setCountyProcessedDt(
			caDA.getRTSDateFromDB(aaResultSet, "CntyPrcsdTimestmp"));
		laItrntData.setSubstationId(
			caDA.getStringFromDB(aaResultSet, "SubstaId"));

		//populate VehicleUserData class
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setSt1(caDA.getStringFromDB(aaResultSet, "RenwlMailngSt1"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setSt2(caDA.getStringFromDB(aaResultSet, "RenwlMailngSt2"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setCity(
				caDA.getStringFromDB(aaResultSet, "RenwlMailngCity"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setState(
				caDA.getStringFromDB(aaResultSet, "RenwlMailngState"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setZpcd(
				caDA.getStringFromDB(aaResultSet, "RenwlMailngZpCd"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setZpcdp4(
				caDA.getStringFromDB(aaResultSet, "RenwlMailngZpCd4"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setFirstName(
			caDA.getStringFromDB(aaResultSet, "FrstName"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setMiddleName(
			caDA.getStringFromDB(aaResultSet, "MidlName"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setLastName(
			caDA.getStringFromDB(aaResultSet, "LastName"));
		laItrntData.getCompleteRegRenData().getVehUserData().setEmail(
			caDA.getStringFromDB(aaResultSet, "EMailAddr"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setTraceNumber(
			caDA.getStringFromDB(aaResultSet, "ItrntTraceNo"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setRenewalDateTime(
			caDA.getRTSDateFromDB(aaResultSet, "ItrntTimestmp"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setPhoneNumber(
			caDA.getStringFromDB(aaResultSet, "CustPhoneNo"));
		laItrntData
			.getCompleteRegRenData()
			.getVehUserData()
			.setRecipientName(
			caDA.getStringFromDB(aaResultSet, "RecpntName"));
		laItrntData.getCompleteRegRenData().setPymntOrderId(
			caDA.getStringFromDB(aaResultSet, "PymntOrderId"));

		//populate VehicleInsuranceData class
		laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setCompanyName(
				caDA.getStringFromDB(aaResultSet, "InsCmpnyName"));
		laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPolicyNo(
			caDA.getStringFromDB(aaResultSet, "InsPlcyNo"));
		laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setAgentName(
			caDA.getStringFromDB(aaResultSet, "InsAgentName"));

		laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPolicyStartDt(
				DatabaseAccess.convertToString(
					caDA.getIntFromDB(
						aaResultSet,
						"InsPlcyStartDate")));
		laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPolicyEndDt(
				DatabaseAccess.convertToString(
					caDA.getIntFromDB(aaResultSet, "InsPlcyEndDate")));

		laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPhoneNo(
			caDA.getStringFromDB(aaResultSet, "InsPhoneNo"));

		if (laItrntData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.getCompanyName()
			.length()
			== 0
			&& laItrntData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPolicyNo()
				.length()
				== 0
			&& laItrntData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getAgentName()
				.length()
				== 0
			&& laItrntData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPolicyStartDt()
				.equals(String.valueOf(Integer.MIN_VALUE))
			&& laItrntData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPolicyEndDt()
				.equals(String.valueOf(Integer.MIN_VALUE))
			&& laItrntData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPhoneNo()
				.length()
				== 0)
		{
			laItrntData.getCompleteRegRenData().setInsuranceRequired(
				false);
		}
		// defect 6051
		else
		{
			laItrntData.getCompleteRegRenData().setInsuranceRequired(
				true);
		}
		// end defect 6051  

		//populate VehicleBaseData class
		laItrntData
			.getCompleteRegRenData()
			.getVehBaseData()
			.setPlateNo(
			caDA.getStringFromDB(aaResultSet, "RegPltNo"));
		laItrntData.getCompleteRegRenData().getVehBaseData().setVin(
			caDA.getStringFromDB(aaResultSet, "VIN"));
		laItrntData.getCompleteRegRenData().getVehBaseData().setDocNo(
			caDA.getStringFromDB(aaResultSet, "DocNo"));

		laItrntData
			.getCompleteRegRenData()
			.getVehBaseData()
			.setOwnerCountyNo(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "OfcIssuanceNo")));

		// Build TransId   
		String lsTransId = "";
		int liOfcIssuanceNo =
			caDA.getIntFromDB(aaResultSet, "OfcIssuanceNo");
		int liTransWsId = caDA.getIntFromDB(aaResultSet, "TransWsId");
		int liTransAMDate =
			caDA.getIntFromDB(aaResultSet, "TransAmDate");
		int liTransTime = caDA.getIntFromDB(aaResultSet, "TransTime");

		if (liTransAMDate > 0 && liTransTime > 0)
		{
			lsTransId =
				UtilityMethods.getTransId(
					liOfcIssuanceNo,
					liTransWsId,
					liTransAMDate,
					liTransTime);
		}
		laItrntData.setTransactionId(lsTransId);

		// populate VehicleDescData class
		laItrntData.getCompleteRegRenData().getVehDescData().setExpMo(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "RegExpMo")));
		laItrntData.getCompleteRegRenData().getVehDescData().setExpYr(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "RegExpYr")));
		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setPlateAge(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "RegPltAge")));

		// Calculate carrying capacity
		int liGrossWt = caDA.getIntFromDB(aaResultSet, "VehGrossWt");
		int liEmptyWt = caDA.getIntFromDB(aaResultSet, "VehEmptyWt");

		// Handle null values in db
		liGrossWt = (liGrossWt < 0) ? 0 : liGrossWt;
		liEmptyWt = (liEmptyWt < 0) ? 0 : liEmptyWt;
		int liCapacity = liGrossWt - liEmptyWt;
		liCapacity = (liCapacity < 0) ? 0 : liCapacity;
		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setEmptyWeight(
			DatabaseAccess.convertToString(liEmptyWt));
		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setGrossWeight(
			DatabaseAccess.convertToString(liGrossWt));

		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setCarryingCapacity(Integer.toString(liCapacity));

		// format double to 2 decimal places - would prefer to use 
		// BigDecimal in future default locale is 
		// Locale.setDefault(Locale.ENGLISH)
		DecimalFormat laDecimalFormat = new DecimalFormat("####.00");

		String lsTonnage =
			DatabaseAccess.convertToString(
				laDecimalFormat.format(
					caDA.getDoubleFromDB(aaResultSet, "VehTon")));
		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setTonnage(
			lsTonnage);

		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setTitleIssDt(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "TtlIssueDate")));

		laItrntData
			.getCompleteRegRenData()
			.getVehDescData()
			.setOwnerName(
			caDA.getStringFromDB(aaResultSet, "OwnrTtlName1"));

		//populate VehicleRegFeesData class -- (not used here!)
		laItrntData.getCompleteRegRenData().setConvFeeString(
			laDecimalFormat.format(
				caDA.getDoubleFromDB(aaResultSet, "CnvncFee")));
		laItrntData.getCompleteRegRenData().setPaymentAmtString(
			laDecimalFormat.format(
				caDA.getDoubleFromDB(aaResultSet, "PymntAmt")));

		return laItrntData;
	}
	/**
	 * This method is a changed copy of getInetData(ResultSet), and is 
	 * used for simulated MQ testing.
	 * 
	 * @param aaResultSet ResultSet
	 * @return InternetRegRecData
	 * @throws RTSException
	 */
	private InternetRegRecData getItrntTransDataForMQTest(ResultSet aaResultSet)
		throws RTSException
	{
		InternetRegRecData laIData = new InternetRegRecData();

		//populate InternetRegRegData class
		laIData.setStatus(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "CntyStatusCd")));
		laIData.setReasonCd(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "CntyReasnCd")));
		laIData.setReasonComments(
			caDA.getStringFromDB(aaResultSet, "CntyReasnTxt"));
		laIData.setSendEmail(
			caDA.getIntFromDB(aaResultSet, "SendEmailIndi"));
		laIData.setEmployeeId(
			caDA.getStringFromDB(aaResultSet, "TransWsId"));
		laIData.setCountyProcessedDt(
			caDA.getRTSDateFromDB(aaResultSet, "CntyPrcsdTimestmp"));
		laIData.setSubstationId(
			caDA.getStringFromDB(aaResultSet, "SubstaId"));

		//populate VehicleUserData class
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setSt1(
			caDA.getStringFromDB(aaResultSet, "RenwlMailngSt1"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setSt2(
			caDA.getStringFromDB(aaResultSet, "RenwlMailngSt2"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setCity(
			caDA.getStringFromDB(aaResultSet, "RenwlMailngCity"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setState(
			caDA.getStringFromDB(aaResultSet, "RenwlMailngState"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setZpcd(
			caDA.getStringFromDB(aaResultSet, "RenwlMailngZpCd"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.getAddress()
			.setZpcdp4(
			caDA.getStringFromDB(aaResultSet, "RenwlMailngZpCd4"));
		laIData.getCompleteRegRenData().getVehUserData().setFirstName(
			caDA.getStringFromDB(aaResultSet, "FrstName"));
		laIData.getCompleteRegRenData().getVehUserData().setMiddleName(
			caDA.getStringFromDB(aaResultSet, "MidlName"));
		laIData.getCompleteRegRenData().getVehUserData().setLastName(
			caDA.getStringFromDB(aaResultSet, "LastName"));
		laIData.getCompleteRegRenData().getVehUserData().setEmail(
			caDA.getStringFromDB(aaResultSet, "EMailAddr"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.setTraceNumber(
			caDA.getStringFromDB(aaResultSet, "ItrntTraceNo"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.setRenewalDateTime(
			caDA.getRTSDateFromDB(aaResultSet, "ItrntTimestmp"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.setPhoneNumber(
			caDA.getStringFromDB(aaResultSet, "CustPhoneNo"));
		laIData
			.getCompleteRegRenData()
			.getVehUserData()
			.setRecipientName(
			caDA.getStringFromDB(aaResultSet, "RecpntName"));
		laIData.getCompleteRegRenData().setPymntOrderId(
			caDA.getStringFromDB(aaResultSet, "PymntOrderId"));
		//populate VehicleInsuranceData class
		laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setCompanyName(
			caDA.getStringFromDB(aaResultSet, "InsCmpnyName"));
		laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPolicyNo(
			caDA.getStringFromDB(aaResultSet, "InsPlcyNo"));
		laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setAgentName(
			caDA.getStringFromDB(aaResultSet, "InsAgentName"));

		laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPolicyStartDt(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "InsPlcyStartDate")));
		laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPolicyEndDt(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "InsPlcyEndDate")));

		laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.setPhoneNo(
			caDA.getStringFromDB(aaResultSet, "InsPhoneNo"));

		if (laIData
			.getCompleteRegRenData()
			.getVehInsuranceData()
			.getCompanyName()
			.length()
			== 0
			&& laIData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPolicyNo()
				.length()
				== 0
			&& laIData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getAgentName()
				.length()
				== 0
			&& laIData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPolicyStartDt()
				.equals(String.valueOf(Integer.MIN_VALUE))
			&& laIData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPolicyEndDt()
				.equals(String.valueOf(Integer.MIN_VALUE))
			&& laIData
				.getCompleteRegRenData()
				.getVehInsuranceData()
				.getPhoneNo()
				.length()
				== 0)
		{
			laIData.getCompleteRegRenData().setInsuranceRequired(false);
		}
		// defect 6051 change
		else
		{
			laIData.getCompleteRegRenData().setInsuranceRequired(true);
		}
		// end defect 6051 change

		//populate VehicleBaseData class
		laIData.getCompleteRegRenData().getVehBaseData().setPlateNo(
			caDA.getStringFromDB(aaResultSet, "RegPltNo"));
		laIData.getCompleteRegRenData().getVehBaseData().setVin(
			caDA.getStringFromDB(aaResultSet, "VIN"));
		laIData.getCompleteRegRenData().getVehBaseData().setDocNo(
			caDA.getStringFromDB(aaResultSet, "DocNo"));

		laIData
			.getCompleteRegRenData()
			.getVehBaseData()
			.setOwnerCountyNo(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "OfcIssuanceNo")));

		// Build TransId 
		int liTransAMDate =
			caDA.getIntFromDB(aaResultSet, "TransAmDate");
		int liTransTime = caDA.getIntFromDB(aaResultSet, "TransTime");

		String lsTransId = "";

		if (liTransAMDate > 0 && liTransTime > 0)
		{
			lsTransId =
				UtilityMethods.getTransId(
					caDA.getIntFromDB(aaResultSet, "OfcIssuanceNo"),
					caDA.getIntFromDB(aaResultSet, "TransWsId"),
					liTransAMDate,
					liTransTime);
		}
		laIData.setTransactionId(lsTransId);

		//populate VehicleDescData class
		laIData.getCompleteRegRenData().getVehDescData().setExpMo(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "RegExpMo")));
		laIData.getCompleteRegRenData().getVehDescData().setExpYr(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "RegExpYr")));
		laIData.getCompleteRegRenData().getVehDescData().setPlateAge(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "RegPltAge")));

		//calculate carrying capacity
		int liGrossWt = caDA.getIntFromDB(aaResultSet, "VehGrossWt");
		int liEmptyWt = caDA.getIntFromDB(aaResultSet, "VehEmptyWt");

		//handle null values in db
		liGrossWt = (liGrossWt < 0) ? 0 : liGrossWt;
		liEmptyWt = (liEmptyWt < 0) ? 0 : liEmptyWt;
		int liCapacity = liGrossWt - liEmptyWt;

		liCapacity = (liCapacity < 0) ? 0 : liCapacity;
		laIData
			.getCompleteRegRenData()
			.getVehDescData()
			.setEmptyWeight(
			DatabaseAccess.convertToString(liEmptyWt));
		laIData
			.getCompleteRegRenData()
			.getVehDescData()
			.setGrossWeight(
			DatabaseAccess.convertToString(liGrossWt));
		laIData
			.getCompleteRegRenData()
			.getVehDescData()
			.setCarryingCapacity(
			Integer.toString(liCapacity));

		// format double to 2 decimal places - would prefer to use 
		// BigDecimal in future
		// default locale is Locale.setDefault(Locale.ENGLISH)
		DecimalFormat laDateFormat = new DecimalFormat("####.00");
		String lsTonnage =
			DatabaseAccess.convertToString(
				laDateFormat.format(
					caDA.getDoubleFromDB(aaResultSet, "VehTon")));
		laIData.getCompleteRegRenData().getVehDescData().setTonnage(
			lsTonnage);
		laIData.getCompleteRegRenData().getVehDescData().setTitleIssDt(
			DatabaseAccess.convertToString(
				caDA.getIntFromDB(aaResultSet, "TtlIssueDate")));
		laIData.getCompleteRegRenData().getVehDescData().setOwnerName(
			caDA.getStringFromDB(aaResultSet, "OwnrTtlName1"));

		//populate VehicleRegFeesData class -- (not used here!)
		laIData.getCompleteRegRenData().setConvFeeString(
			laDateFormat.format(
				caDA.getDoubleFromDB(aaResultSet, "CnvncFee")));
		laIData.getCompleteRegRenData().setPaymentAmtString(
			laDateFormat.format(
				caDA.getDoubleFromDB(aaResultSet, "PymntAmt")));

		// defect 6604 - added for MQ testing
		laIData.getCompleteRegRenData().setPymntStatusCd(
			(caDA.getIntFromDB(aaResultSet, "ItrntPymntStatusCd")));
		// end defect 6604

		// defect 8554
		//		Hashtable lhtHashTable = new Hashtable();
		//		lhtHashTable.put(
		//			"RegPltNo",
		//			laIData
		//				.getCompleteRegRenData()
		//				.getVehBaseData()
		//				.getPlateNo());
		//
		//		InternetData laInternetData = new InternetData();
		//
		//		CompleteTransactionData laComplTransData =
		//			laInternetData.qryItrntDataComplTransData(lhtHashTable);
		//
		//		RegFeesData laRegFees = new RegFeesData();
		//		laRegFees = laComplTransData.getRegFeesData();
		//
		//		Vector lvRegFees = new Vector();
		//		lvRegFees = laRegFees.getVectFees();
		//
		//		Vector lvVehRegFees = new Vector();
		//
		//		for (int i = 0; i < lvRegFees.size(); ++i)
		//		{
		//			VehicleRegFeesData laVehRegFeesData =
		//				new VehicleRegFeesData();
		//			FeesData laFeesData = new FeesData();
		//			laFeesData = (FeesData) lvRegFees.elementAt(i);
		//			laVehRegFeesData.setItemPrice(
		//				Float.parseFloat(laFeesData.getItemPrice().toString()));
		//			laVehRegFeesData.setAcctItemCd(laFeesData.getAcctItmCd());
		//			laVehRegFeesData.setAcctItemDesc(laFeesData.getDesc());
		//			lvVehRegFees.add(laVehRegFeesData);
		//			laIData.getCompleteRegRenData().setVehRegFeesData(
		//				lvVehRegFees);
		//		}		
		// end defect 8554
		return laIData;
	}
	/**
	 * Insert an internet registration renewal into POS database,
	 * the status set to 'UPPAID'.
	 * 
	 * @param aaCompRegRenData
	 * @param aaCompTransData
	 * @throws RTSException
	 */
	public void insItrntRenew(CompleteRegRenData aaCompRegRenData)
		throws RTSException
	{
		csMethod = "insItrntRenew(CompleteRegRenData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		InternetRegRecData laInternetRegRecData =
			new InternetRegRecData();
		laInternetRegRecData.setCompleteRegRenData(aaCompRegRenData);

		String sqlIns =
			"INSERT INTO RTS.RTS_ITRNT_TRANS ("
				+ "TRANSCD, "
				+ "RegPltNo,"
				+ "VIN,"
				+ "DocNo,"
				+ "OfcIssuanceNo,"
				+ "ItrntTimeStmp,"
				+ "CNTYPRCSDTIMESTMP,"
				+ "CntyStatusCd,"
				+ "SendEmailIndi,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "RegPltAge,"
				+ "VehGrossWt,"
				+ "VehEmptyWt,"
				+ "VehTon, "
				+ "OwnrTtlName1,"
				+ "TtlIssueDate"
				+ ") VALUES ( "
				+ " 'IRENEW',"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " CURRENT TIMESTAMP,"
				+ " CURRENT TIMESTAMP,"
				+ " 1,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?"
				+ " )";

		try
		{
			// 4
			// TransCD
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getPlateNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getDocNo()));

			// 4
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehBaseData()
						.getOwnerCountyNo()));
			// internet timestamp
			// county timestamp
			// county status - status should='UNPAID', need to make sure
			// for InternetRegRecData constructor 

			// 4 	
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						laInternetRegRecData.getSendEmail())));
			// 0--> no email need to send
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaCompRegRenData.getVehDescData().getExpMo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCompRegRenData.getVehDescData().getExpYr())));
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehDescData()
							.getPlateAge())));

			//3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehDescData()
							.getGrossWeight())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehDescData()
							.getEmptyWeight())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehDescData()
							.getTonnage())));

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehDescData().getOwnerName()));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehDescData()
							.getTitleIssDt())));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(sqlIns, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			// Moved the following call into separate call 
			//	insCompTransData(aaCompTransData, aaCompRegRenData);
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
	 * Insert Itrnt Address Change 
	 * 
	 * @param asTransCd String
	 * @param aaOrgVehData MFVehicleData
	 * @throws RTSException
	 */
	public void insItrntAddr(
		String asTransCd,
		MFVehicleData aaOrgVehData)
		throws RTSException
	{
		csMethod = "insItrntAddr(String,MFVehicleData)";
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		String lsIns =
			"INSERT into RTS.RTS_ITRNT_TRANS("
				+ "TransCd,"
				+ "RegPltNo,"
				+ "Vin,"
				+ "DocNo,"
				+ "OfcIssuanceNo,"
				+ "RecpntName,"
				+ "RenwlMailngSt1,"
				+ "RenwlMailngSt2,"
				+ "RenwlMailngCity,"
				+ "RenwlMailngState,"
				+ "RenwlMailngZpCd,"
				+ "RenwlMailngZpCd4,"
				+ "IADDRSPCLPLTINDI,"
				// defect 10610
				+ "Emailrenwlreqcd,"
				+ "Emailaddr,"
				// end defect 10610
				+ "ItrntTimeStmp ) VALUES ( "
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
				// defect 10610
				+ " ?,"
				+ " ?,"
				// end defect 10610
				+ " CURRENT TIMESTAMP )";

		// defect 7888
		// removed RegPltNo because it was not used, and added laAddress
		// to reduce the number of calls to getRenwlMailAddr()
		// String RegPltNo = aaOrgVehData.getRegData().getRegPltNo();
		AddressData laAddress =
			aaOrgVehData.getRegData().getRenwlMailAddr();
		// end defect 7888

		try
		{
			lvValues.addElement(new DBValue(Types.VARCHAR, asTransCd));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaOrgVehData.getRegData().getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaOrgVehData.getVehicleData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaOrgVehData.getTitleData().getDocNo()));
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					String.valueOf(
						aaOrgVehData
							.getRegData()
							.getResComptCntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaOrgVehData.getRegData().getRecpntName()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, laAddress.getSt1()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, laAddress.getSt2()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, laAddress.getCity()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, laAddress.getState()));
			lvValues.addElement(
				new DBValue(Types.CHAR, laAddress.getZpcd()));
			lvValues.addElement(
				new DBValue(Types.CHAR, laAddress.getZpcdp4()));

			//defect 4885
			if (asTransCd == "IADDR")
			{
				if (isSpecialPlate(aaOrgVehData))
				{
					lvValues.addElement(
						new DBValue(Types.SMALLINT, String.valueOf(1)));
				}
				else
				{
					lvValues.addElement(
						new DBValue(Types.SMALLINT, String.valueOf(0)));
				}
			}
			else
			{
				lvValues.addElement(
					new DBValue(Types.SMALLINT, String.valueOf(0)));
			}
			// end defect 4885
			
			// defect 10610
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					Integer.toString(
						aaOrgVehData.getRegData().getEMailRenwlReqCd())));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaOrgVehData.getRegData().getRecpntEMail()));
			// end defect 10610

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
	 * Purge rts_itrnt_trans that are older than the number of days
	 * passed in.
	 * 
	 * @param aiDaysOld int
	 * @return int 
	 * @throws RTSException
	 */
	public int purgeItrntIRenewTrans(int aiCntyStatusCd, int aiDaysOld)
		throws RTSException
	{
		csMethod = "purgeItrntIRenewTrans(int,int)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String sqlDelTrans =
			"delete from rts.rts_itrnt_trans "
				+ "where transcd = 'IRENEW' and "
				+ "cntystatuscd=? and "
				+ "CntyPrcsdTimeStmp < (CURRENT TIMESTAMP - ? DAYS)";

		// defect 8134
		if (aiCntyStatusCd == CommonConstants.UNPAID)
		{
			sqlDelTrans += "and itrntpymntstatuscd is null";
		}
		// end defect 8134		

		Vector lvValues = new Vector();

		try
		{
			lvValues.add(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiCntyStatusCd)));
			lvValues.add(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiDaysOld)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(sqlDelTrans, lvValues);

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
	 * This method writes to the mq error log, all fields in the
	 * updateRenewal(CompleteRegRenData) update statement when
	 * there is a problem with the update
	 *
	 * @param aaCompRegRenData CompleteRegRenData
	 */
	public void logErrorMsg(CompleteRegRenData aaCompRegRenData)
	{
		// defect 10995
/*		MQLog.error(
			"Ofcissuanceno = "
				+ aaCompRegRenData.getVehBaseData().getOwnerCountyNo());
		MQLog.error("Cntystatuscd could not be updated due to error");
		MQLog.error(
			"TraceNo = "
				+ aaCompRegRenData.getVehUserData().getTraceNumber());
		MQLog.error(
			"Itrntpymntstatuscd = "
				+ aaCompRegRenData.getItrntPymntStatusCd());
		MQLog.error(
			"Pymntorderid = " + aaCompRegRenData.getPymntOrderId());
		MQLog.error(
			"PmntAmt = " + aaCompRegRenData.getTotalFeesString());
		MQLog.error(
			"ConvFeeString = " + aaCompRegRenData.getConvFeeString());
		MQLog.error(
			"CompanyName = "
				+ aaCompRegRenData
					.getVehInsuranceData()
					.getCompanyName());
		MQLog.error(
			"PolicyNo = "
				+ aaCompRegRenData.getVehInsuranceData().getPolicyNo());
		MQLog.error(
			"AgentName = "
				+ aaCompRegRenData.getVehInsuranceData().getAgentName());
		MQLog.error(
			"Agent PhoneNo = "
				+ aaCompRegRenData.getVehInsuranceData().getPhoneNo());
		MQLog.error(
			"PolicyStartDt = "
				+ aaCompRegRenData
					.getVehInsuranceData()
					.getPolicyStartDt());
		MQLog.error(
			"PolicyEndDt = "
				+ aaCompRegRenData
					.getVehInsuranceData()
					.getPolicyEndDt());
		MQLog.error(
			"FirstName = "
				+ aaCompRegRenData.getVehUserData().getFirstName());
		MQLog.error(
			"MiddleName = "
				+ aaCompRegRenData.getVehUserData().getMiddleName());
		MQLog.error(
			"LastName = "
				+ aaCompRegRenData.getVehUserData().getLastName());
		MQLog.error(
			"Email = " + aaCompRegRenData.getVehUserData().getEmail());
		MQLog.error(
			"BillName ="
				+ aaCompRegRenData.getVehUserData().getRecipientName());
		MQLog.error(
			"St1 = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getSt1());
		MQLog.error(
			"St2 = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getSt2());
		MQLog.error(
			"City = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getCity());
		MQLog.error(
			"State = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getState());
		MQLog.error(
			"ZipCd = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getZpcd());
		MQLog.error(
			"ZipCd4 = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getZpcdp4());
		MQLog.error(
			"PhoneNumber = "
				+ aaCompRegRenData.getVehUserData().getPhoneNumber());
		MQLog.error(
			"Plate = "
				+ aaCompRegRenData.getVehBaseData().getPlateNo());
		MQLog.error(
			"VIN = " + aaCompRegRenData.getVehBaseData().getVin());
		MQLog.error(
			"DocNo = " + aaCompRegRenData.getVehBaseData().getDocNo());
		MQLog.error("=== End of Error Info === ");*/
		MQLog.info(
			"Ofcissuanceno = "
				+ aaCompRegRenData.getVehBaseData().getOwnerCountyNo());
		MQLog.info("Cntystatuscd could not be updated due to error");
		MQLog.info(
			"TraceNo = "
				+ aaCompRegRenData.getVehUserData().getTraceNumber());
		MQLog.info(
			"Itrntpymntstatuscd = "
				+ aaCompRegRenData.getItrntPymntStatusCd());
		MQLog.info(
			"Pymntorderid = " + aaCompRegRenData.getPymntOrderId());
		MQLog.info(
			"PmntAmt = " + aaCompRegRenData.getTotalFeesString());
		MQLog.info(
			"ConvFeeString = " + aaCompRegRenData.getConvFeeString());
		MQLog.info(
			"CompanyName = "
				+ aaCompRegRenData
					.getVehInsuranceData()
					.getCompanyName());
		MQLog.info(
			"PolicyNo = "
				+ aaCompRegRenData.getVehInsuranceData().getPolicyNo());
		MQLog.info(
			"AgentName = "
				+ aaCompRegRenData.getVehInsuranceData().getAgentName());
		MQLog.info(
			"Agent PhoneNo = "
				+ aaCompRegRenData.getVehInsuranceData().getPhoneNo());
		MQLog.info(
			"PolicyStartDt = "
				+ aaCompRegRenData
					.getVehInsuranceData()
					.getPolicyStartDt());
		MQLog.info(
			"PolicyEndDt = "
				+ aaCompRegRenData
					.getVehInsuranceData()
					.getPolicyEndDt());
		MQLog.info(
			"FirstName = "
				+ aaCompRegRenData.getVehUserData().getFirstName());
		MQLog.info(
			"MiddleName = "
				+ aaCompRegRenData.getVehUserData().getMiddleName());
		MQLog.info(
			"LastName = "
				+ aaCompRegRenData.getVehUserData().getLastName());
		MQLog.info(
			"Email = " + aaCompRegRenData.getVehUserData().getEmail());
		MQLog.info(
			"BillName ="
				+ aaCompRegRenData.getVehUserData().getRecipientName());
		MQLog.info(
			"St1 = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getSt1());
		MQLog.info(
			"St2 = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getSt2());
		MQLog.info(
			"City = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getCity());
		MQLog.info(
			"State = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getState());
		MQLog.info(
			"ZipCd = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getZpcd());
		MQLog.info(
			"ZipCd4 = "
				+ aaCompRegRenData
					.getVehUserData()
					.getAddress()
					.getZpcdp4());
		MQLog.info(
			"PhoneNumber = "
				+ aaCompRegRenData.getVehUserData().getPhoneNumber());
		MQLog.info(
			"Plate = "
				+ aaCompRegRenData.getVehBaseData().getPlateNo());
		MQLog.info(
			"VIN = " + aaCompRegRenData.getVehBaseData().getVin());
		MQLog.info(
			"DocNo = " + aaCompRegRenData.getVehBaseData().getDocNo());
		MQLog.info("=== End of Error Info === ");
		// end defect 10995
	}

	/**
	 * Purge rts_itrnt_trans for IAddr/IRnr are older than the number 
	 * of days passed in.
	 * 
	 * @param aiDaysOld int
	 * @return int 
	 * @throws RTSException
	 */
	public int purgeIAddrIRnrTrans(int aiDaysOld) throws RTSException
	{
		csMethod = "purgeIAddrIRnrTrans(int)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelAddrTrans =
			"delete from rts.rts_itrnt_trans "
				+ "where transcd IN ('IADDR', 'IRNR') and "
				+ "ItrntTimeStmp < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();
		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiDaysOld)));
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelAddrTrans,
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

	// defect 10387 
	//	/**
	//	 * Query data for the address change report.
	//	 * 
	//	 * @param aaReportSearchData ReportSearchData
	//	 * @return Vector
	//	 * @throws RTSException
	//	 */
	//	public Vector qryAddressChangeReport(ReportSearchData aaReportSearchData)
	//		throws RTSException
	//	{
	//		csMethod = "qryAddressChangeReport(ReportSearchData)";
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			csMethod + CommonConstant.SQL_METHOD_BEGIN);
	//
	//		StringBuffer lsQry = new StringBuffer();
	//		String lsWhereClause = "";
	//		Vector lvRslt = new Vector();
	//		ResultSet laResultSet;
	//
	//		//defect 6194 variables
	//		RTSDate laRTSFromDate = null;
	//		RTSDate laRTSToDate = null;
	//		String lsFromDate = aaReportSearchData.getKey2();
	//		String lsToDate = aaReportSearchData.getKey3();
	//
	//		//defect 6194 - using RTSdate for from and to date
	//		if (lsFromDate != null && lsFromDate.length() > 0)
	//		{
	//			laRTSFromDate =
	//				new RTSDate(1, Integer.parseInt(lsFromDate));
	//			laRTSFromDate.setHour(00);
	//			laRTSFromDate.setMinute(00);
	//			laRTSFromDate.setSecond(00);
	//			laRTSFromDate.setMillisecond(000);
	//		}
	//
	//		if (lsToDate != null && lsToDate.length() > 0)
	//		{
	//			laRTSToDate = new RTSDate(1, Integer.parseInt(lsToDate));
	//			laRTSToDate.setHour(23);
	//			laRTSToDate.setMinute(59);
	//			laRTSToDate.setSecond(59);
	//			laRTSToDate.setMillisecond(999);
	//		}
	//		else
	//		{
	//			laRTSToDate = new RTSDate();
	//		}
	//		//set report query dates					
	//		lsQry.append(
	//			"SELECT b.ofcName, count(*) as cnt "
	//				+ "FROM RTS.RTS_ITRNT_TRANS a, RTS.RTS_OFFICE_IDS b "
	//				+ "WHERE a.ofcIssuanceNo = b.ofcIssuanceNo "
	//				+ "AND a.TransCd IN ('"
	//				+ TransCdConstant.IADDR
	//				+ "','"
	//				+ TransCdConstant.IRNR
	//				+ "') ");
	//
	//		//apply Timesstamp lsWhereClause if necessary
	//
	//		// 6194 using rtsFromDate.getTimestamp() and rtsToDate.getTimestamp()
	//		if (lsFromDate != null && lsFromDate.length() > 0)
	//			lsWhereClause += "AND a.ItrntTimeStmp BETWEEN TIMESTAMP('"
	//				+ laRTSFromDate.getTimestamp()
	//				+ "') AND TIMESTAMP('"
	//				+ laRTSToDate.getTimestamp()
	//				+ "') ";
	//
	//		lsQry.append(lsWhereClause);
	//		lsQry.append("GROUP BY b.ofcName");
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				csMethod + CommonConstant.SQL_BEGIN);
	//
	//			laResultSet = caDA.executeDBQuery(lsQry.toString());
	//
	//			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
	//
	//			while (laResultSet.next())
	//			{
	//				AddressChangeReportData laAddrChangeRptData =
	//					new AddressChangeReportData();
	//				laAddrChangeRptData.setCountyName(
	//					caDA.getStringFromDB(laResultSet, "ofcName"));
	//				laAddrChangeRptData.setTimeStamp(
	//					caDA.getRTSDateFromDB(
	//						laResultSet,
	//						"ItrntTimeStmp"));
	//				laAddrChangeRptData.setNumChanges(
	//					caDA.getIntFromDB(laResultSet, "cnt"));
	//				// Add element to the Vector
	//				lvRslt.addElement(laAddrChangeRptData);
	//			}
	//			laResultSet.close();
	//			laResultSet = null;
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				csMethod
	//					+ CommonConstant.SQL_EXCEPTION
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//		finally
	//		{
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				csMethod + CommonConstant.SQL_METHOD_END);
	//		}
	//	}
	// end defect 10387 

	/**
	 * This method is a changed copy of qryBatchPayments(), and is used for 
	 * querying failed cond fee records only (ITRNTPYMNTSTATUSCD = 2)
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryBatchConvFeePayments() throws RTSException
	{
		csMethod = "qryBatchConvFeePayments()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvRet = new Vector();
		String lsSqlQry =
			"SELECT "
				+ "ITRNTTRACENO, "
				+ "PYMNTORDERID, "
				+ "ITRNTPYMNTSTATUSCD, "
				+ "SUM(PYMNTAMT) PYMNTAMT, "
				+ "SUM(CNVNCFEE) CNVNCFEE, "
				+ "OFCISSUANCENO "
				+ "FROM RTS.RTS_ITRNT_TRANS "
				+ "WHERE TRANSCD='IRENEW' AND "
				+ "ITRNTPYMNTSTATUSCD = 2"
				+ " GROUP BY ITRNTTRACENO, "
				+ "PYMNTORDERID, "
				+ "ITRNTPYMNTSTATUSCD,"
				+ "OFCISSUANCENO";
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlQry);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				PaymentData laPaymentData = new PaymentData();
				// defect 9878
				// changes backed out here
				// end defect 9878
				laPaymentData.setOrigTraceNo(
					caDA.getStringFromDB(laResultSet, "ITRNTTRACENO"));
				laPaymentData.setPmntOrderId(
					caDA.getStringFromDB(laResultSet, "PYMNTORDERID"));
				laPaymentData.setPmntStatusCd(
					caDA.getIntFromDB(
						laResultSet,
						"ITRNTPYMNTSTATUSCD"));
				laPaymentData.setPmntAmt(
					caDA.getStringFromDB(laResultSet, "PYMNTAMT"));
				laPaymentData.setConvFee(
					caDA.getStringFromDB(laResultSet, "CNVNCFEE"));
				laPaymentData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OFCISSUANCENO"));
				lvRet.addElement(laPaymentData);
			}
			laResultSet.close();
			return lvRet;
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
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
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
	 * Query Batch Payments.
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryBatchPayments() throws RTSException
	{
		csMethod = "qryBatchPayments()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvRet = new Vector(); // the return Vector
		String lsSqlQry =
			"SELECT "
				+ "ITRNTTRACENO, "
				+ "PYMNTORDERID, "
				+ "ITRNTPYMNTSTATUSCD, "
				+ "SUM(PYMNTAMT) PYMNTAMT, "
				+ "SUM(CNVNCFEE) CNVNCFEE, "
				+ "OFCISSUANCENO "
				+ "FROM RTS.RTS_ITRNT_TRANS "
				+ "WHERE TRANSCD='IRENEW' AND "
				+ "ITRNTPYMNTSTATUSCD IN (1,3) AND "
				+ "CNTYSTATUSCD="
				+ CommonConstants.UNPAID
				+ " GROUP BY ITRNTTRACENO, "
				+ "PYMNTORDERID, "
				+ "ITRNTPYMNTSTATUSCD,"
				+ "OFCISSUANCENO";
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlQry);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				PaymentData laPaymentData = new PaymentData();
				// defect 9878
				// changes backed out here
				// end defect 9878
				laPaymentData.setOrigTraceNo(
					caDA.getStringFromDB(laResultSet, "ITRNTTRACENO"));
				laPaymentData.setPmntOrderId(
					caDA.getStringFromDB(laResultSet, "PYMNTORDERID"));
				laPaymentData.setPmntStatusCd(
					caDA.getIntFromDB(
						laResultSet,
						"ITRNTPYMNTSTATUSCD"));
				laPaymentData.setPmntAmt(
					caDA.getStringFromDB(laResultSet, "PYMNTAMT"));
				laPaymentData.setConvFee(
					caDA.getStringFromDB(laResultSet, "CNVNCFEE"));
				laPaymentData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OFCISSUANCENO"));
				lvRet.addElement(laPaymentData);
			}
			laResultSet.close();
			return lvRet;
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
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
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
	 * Query Batch Refunds.
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryBatchRefunds() throws RTSException
	{
		csMethod = "qryBatchRefunds()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvRet = new Vector(); // the return Vector
		String lsSqlQry = "Select "
			// defect 8501
	+"itrnttraceno,"
		+ "sum(pymntamt) as PYMNTAMTTOT,"
		+ "ofcissuanceno "
			//				+ "RegPltNo,"
		//				+ "Vin,"
		//				+ "DocNo,"
		//				+ "ItrntTraceNo,"
		//				+ "PymntOrderId,"
		//				+ "OfcIssuanceNo, "
		//				+ "PymntAmt "
	+"FROM RTS.RTS_ITRNT_TRANS " + "WHERE "
			// defect 6764
	+"CntyStatusCd in ("
		+ CommonConstants.DECLINED_REFUND_PENDING
		+ ","
		+ CommonConstants.DECLINED_REFUND_FAILED
		+ ")"
			// defect 8501
	+" group by itrnttraceno, ofcissuanceno";
		// end defect 6764

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				RefundData lRefundData = new RefundData();
				VehicleBaseData lVehBaseData = new VehicleBaseData();
				// defect 9878
				// changes backed out here
				// end defect 9878
				// defect 8501
				//				lVehBaseData.setPlateNo(
				//					caDA.getStringFromDB(laResultSet, "RegPltNo"));
				//				lVehBaseData.setVin(
				//					caDA.getStringFromDB(laResultSet, "Vin"));
				//				lVehBaseData.setDocNo(
				//					caDA.getStringFromDB(laResultSet, "DocNo"));
				// Need OfcIssuanceNo (CountyNo) for County VID in 
				// the Epay
				// end defect 8501
				lVehBaseData.setOwnerCountyNo(
					caDA.getStringFromDB(laResultSet, "OfcIssuanceNo"));
				lRefundData.setVehBaseData(lVehBaseData);
				lRefundData.setOrigTraceNo(
					caDA.getStringFromDB(laResultSet, "ItrntTraceNo"));
				// defect 8501	
				//				lRefundData.setPymtOrderId(
				//					caDA.getStringFromDB(laResultSet, "PymntOrderId"));
				//				lRefundData.setRefAmt(
				//					caDA.getStringFromDB(laResultSet, "PymntAmt"));
				// defect 10281
				//				lRefundData.setRefAmt(
				//					Double.toString(
				//						caDA.getDoubleFromDB(
				//							laResultSet,
				//							"PYMNTAMTTOT")));
				lRefundData.setRefAmt(
					caDA.getStringFromDB(laResultSet, "PYMNTAMTTOT"));
				// end defect 10281			
				//				end defect 8501	
				lvRet.addElement(lRefundData);
			}
			laResultSet.close();
			return lvRet;
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
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
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
	 * Query Renewal email address.
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryRenewalEmail() throws RTSException
	{
		csMethod = "qryRenewalEmail()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		// Select those records which needs to send the email
		String sqlQry =
			"Select "
				+ "VIN, "
				+ "DOCNO, "
				+ "REGPLTNO, "
				+ "ITRNTTIMESTMP, "
				+ "EMAILADDR, "
				+ "CNTYSTATUSCD "
				+ "from RTS.RTS_ITRNT_TRANS "
				+ "where SendEmailIndi=1 "
				+ "and CNTYSTATUSCD IN ("
				+ CommonConstants.APPROVED
				+ ","
				+ CommonConstants.HOLD
				+ ","
				+ CommonConstants.DECLINED_REFUND_APPROVED
				+ ")";
		// CommonConstants.DECLINED_REFUND_FAILED + "," + 
		// CommonConstants.DECLINED_REFUND_PENDING + ")";

		Vector lvRet = new Vector();
		ResultSet laResultSet = null;
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSet = caDA.executeDBQuery(sqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{

				EmailData laEmailData = new EmailData();
				laEmailData.setEmail(
					caDA.getStringFromDB(laResultSet, "EMAILADDR"));
				laEmailData.setPlateNo(
					caDA.getStringFromDB(laResultSet, "REGPLTNO"));
				laEmailData.setDocNo(
					caDA.getStringFromDB(laResultSet, "DOCNO"));
				laEmailData.setVin(
					caDA.getStringFromDB(laResultSet, "VIN"));
				laEmailData.setRenewalDt(
					caDA.getStringFromDB(laResultSet, "ITRNTTIMESTMP"));
				laEmailData.setCntyStatusCd(
					caDA.getIntFromDB(laResultSet, "CNTYSTATUSCD"));
				lvRet.addElement(laEmailData);
			}
			return lvRet;
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
			if (laResultSet != null)
			{
				try
				{
					laResultSet.close();
				}
				catch (Exception aeEx)
				{
				}
			}
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Query InternetRegRecData.
	 * 
	 * @param aiFunctionID int
	 * @param ahtData Hashtable
	 * @return InternetRegRecData
	 * @throws RTSException
	 */
	public InternetRegRecData qryNext(
		int aiFunctionID,
		Hashtable ahtData)
		throws RTSException
	{
		csMethod = "qryNext()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		ResultSet laResultSet;
		// defect 10996
		// StringBuffer lsSqlQry = InternetTransaction.getQryRenewal();
		StringBuffer lsSqlQry =
			new StringBuffer(
		"SELECT "
		+ "CntyStatusCd,"
		+ "CntyReasnCd,"
		+ "CntyReasnTxt,"
		+ "SendEmailIndi,"
		+ "TransWsId,"
		// defect 10996
		+ "coalesce(ItrntPymntTimeStmp,CntyPrcsdTimestmp),"
		// + "CntyPrcsdTimestmp,"
		// end defect 10996
		+ "Substaid,"
		+ "RenwlMailngSt1,"
		+ "RenwlMailngSt2,"
		+ "RenwlMailngCity,"
		+ "RenwlMailngState,"
		+ "RenwlMailngZpCd,"
		+ "RenwlMailngZpCd4,"
		+ "FrstName,"
		+ "MidlName,"
		+ "PymntOrderId,"
		+ "LastName,"
		+ "EMailAddr,"
		+ "ItrntTraceNo,"
		+ "ItrntTimestmp,"
		+ "CustPhoneNo,"
		+ "RecpntName,"
		+ "InsCmpnyName,"
		+ "InsPlcyNo,"
		+ "InsAgentName,"
		+ "InsPlcyStartDate,"
		+ "InsPlcyEndDate,"
		+ "InsPhoneNo,"
		+ "RegPltNo,"
		+ "VIN,"
		+ "DocNo,"
		+ "OfcIssuanceNo,"
		+ "TransWsId,"
		+ "TransAmDate,"
		+ "TransTime,"
		+ "RegExpMo,"
		+ "RegExpYr,"
		+ "RegPltAge,"
		+ "VehGrossWt,"
		+ "VehEmptyWt,"
		+ "VehTon,"
		+ "TtlIssueDate,"
		+ "OwnrTtlName1,"
		+ "CnvncFee,"
		+ "PymntAmt"
		+ " FROM "
		+ " RTS.RTS_ITRNT_TRANS ");
		String lsWhereClause = "WHERE ";
		// end defect 10996

		// Build lsWhereClause based on function id
		if (aiFunctionID == RegRenProcessingConstants.GET_NEXT_NEW)
		{
			lsWhereClause += "CntyStatusCd = " + CommonConstants.NEW;
		}
		else if (
			aiFunctionID == RegRenProcessingConstants.GET_NEXT_HOLD)
		{
			//fetch next HOLD from QUEUE
			lsWhereClause += "CntyStatusCd = " + CommonConstants.HOLD;
		}
		else if (
			aiFunctionID == RegRenProcessingConstants.GET_NEXT_ANY)
		{
			//fetch any NEW/HOLD from QUEUE
			lsWhereClause += "CntyStatusCd IN ("
				+ CommonConstants.NEW
				+ ","
				+ CommonConstants.HOLD
				+ ")";

		}
		lsWhereClause += " AND TransCd = '"
			+ TransCdConstant.IRENEW
			+ "'";

		// Complete sql query
		lsSqlQry.append(lsWhereClause);
		lsSqlQry.append(
			" AND OfcIssuanceNo = " + ahtData.get("OfcIssuanceNo"));
		// defect 10996
		// lsSqlQry.append(" ORDER BY CNTYPRCSDTIMESTMP, ItrntTimestmp");
		// order by 6 below is the coalesce of itrntpymnttimestmp and CNTYPRCSDTIMESTMP
		// so if the new itrntpymnttimestmp column has no value, the CNTYPRCSDTIMESTMP
		// is used for the sort
		//lsSqlQry.append(" ORDER BY 6");
		lsSqlQry.append(" ORDER BY 6, ItrntTraceNo");
		// end defect 10996

		InternetRegRecData laData = null;
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSet = caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			if (laResultSet.next())
			{
				laData = getItrntTransData(laResultSet);
			}
			laResultSet.close();
			laResultSet = null;
			return laData;
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
	 * Query Renewal
	 * 
	 * @param ahtKeys Hashtable
	 * @return HashTable
	 * @throws RTSException
	 */
	public Hashtable qryItrntRenew(Hashtable ahtKeys)
		throws RTSException
	{
		csMethod = "qryItrntRenew(Hashtable)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvRslt = new Vector();

		//obtain keys from function call
		String lsPlateNo = (String) ahtKeys.get("RegPltNo");
		String lsTraceNo = (String) ahtKeys.get("ItrntTraceNo");
		String lsTransId = (String) ahtKeys.get("TransId");
		String lsFirstName = (String) ahtKeys.get("FrstName");
		String lsMiddleName = (String) ahtKeys.get("MidlName");
		String lsLastName = (String) ahtKeys.get("LastName");
		String lsStatus = (String) ahtKeys.get("CntyStatusCd");
		String lsCountyID = (String) ahtKeys.get("OfcIssuanceNo");

		//obtain date keys
		String lsBeginDate = (String) ahtKeys.get("StartDate");
		String lsEndDate = (String) ahtKeys.get("EndDate");

		//set db and sql parameters
		ResultSet laResultSet;
		StringBuffer lsSqlQry = InternetTransaction.getQryRenewal();

		String lsWhereClause =
			"WHERE OfcIssuanceNo = "
				+ lsCountyID
				+ " AND TransCd = '"
				+ TransCdConstant.IRENEW
				+ "' ";

		// Define lsWhereClause based on user input
		if (lsPlateNo != null && lsPlateNo.length() > 0)
		{
			lsWhereClause += "AND RegPltNo LIKE '%" + lsPlateNo + "%' ";
		}
		if (lsTraceNo != null && lsTraceNo.length() > 0)
		{
			lsWhereClause += "AND ItrntTraceNo LIKE '%"
				+ lsTraceNo
				+ "%' ";
		}
		// If TransId is provided, it must be parsed into pieces that 
		// exist in db table
		// The alternative is to add this column into table (recommended)
		// and avoid costly table scans which occur with non-indexed columns
		if (lsTransId != null && lsTransId.length() > 0)
		{
			if (lsTransId.length() == 17)
			{
				lsWhereClause += "AND TransWsId = "
					+ lsTransId.substring(3, 6)
					+ " ";
				lsWhereClause += "AND TransAmDate = "
					+ lsTransId.substring(6, 11)
					+ " ";
				lsWhereClause += "AND TransTime = "
					+ lsTransId.substring(11, 17)
					+ " ";
			}
		}
		if (lsFirstName != null && lsFirstName.length() > 0)
			lsWhereClause += "AND FrstName LIKE '%"
				+ lsFirstName
				+ "%' ";

		if (lsMiddleName != null && lsMiddleName.length() > 0)
			lsWhereClause += "AND MidlName LIKE '%"
				+ lsMiddleName
				+ "%' ";

		if (lsLastName != null && lsLastName.length() > 0)
			lsWhereClause += "AND (LastName LIKE '%"
				+ lsLastName
				+ "%' OR RecpntName LIKE '%"
				+ lsLastName
				+ "%') ";

		if (lsStatus != null && lsStatus.length() > 0)
		{
			if (lsStatus
				.equals(RegRenProcessingConstants.DECL_ALL_LBL))
			{
				lsWhereClause += "AND CntyStatusCd IN ("
					+ CommonConstants.DECLINED_REFUND_PENDING
					+ ","
					+ CommonConstants.DECLINED_REFUND_APPROVED
					+ ","
					+ CommonConstants.DECLINED_REFUND_FAILED
					+ ") ";
			}
			else
			{
				lsWhereClause += "AND CntyStatusCd = " + lsStatus + " ";
			}
		}
		if (lsBeginDate != null && lsBeginDate.length() > 0)
		{
			RTSDate laRTSBegin =
				new RTSDate(1, Integer.parseInt(lsBeginDate));
			// make it inclusive
			lsWhereClause += "AND CntyPrcsdTimestmp >= '"
				+ laRTSBegin.getTimestamp()
				+ "' ";
		}
		if (lsEndDate != null && lsEndDate.length() > 0)
		{
			lsEndDate =
				String.valueOf(new Integer(lsEndDate).intValue());
			RTSDate laRTSEnd =
				new RTSDate(1, Integer.parseInt(lsEndDate));
			laRTSEnd.setHour(23);
			laRTSEnd.setMinute(59);
			laRTSEnd.setSecond(59);
			laRTSEnd.setMillisecond(999);
			lsWhereClause += "AND CntyPrcsdTimestmp <= '"
				+ laRTSEnd.getTimestamp()
				+ "' ";
		}

		// Complete the SQL Query
		lsSqlQry.append(lsWhereClause);

		// defect 10421 
		lsSqlQry.append(" UNION ");
		lsSqlQry.append(InternetTransaction.getQryRenewal(true));
		lsSqlQry.append(lsWhereClause);
		// defect 10421 

		lsSqlQry.append("ORDER BY CntyPrcsdTimestmp");
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSet = caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				InternetRegRecData laIData =
					getItrntTransData(laResultSet);
				lvRslt.addElement(laIData);
			}
			laResultSet.close();
			laResultSet = null;

			// defect 8247
			// put the results in the hash with the search keys
			// then return hash
			ahtKeys.put("ReturnValues", lvRslt);
			return (ahtKeys);
			// end defect 8247
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
	 * Query Renwal Count.
	 * 
	 * @param aaData Object
	 * @return int
	 * @throws RTSException
	 */
	public int qryItrntRenewCount(Object aaData) throws RTSException
	{
		csMethod = "qryItrntRenewCount(Object)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		int liRenewalCount = 0;
		Vector lvValues = new Vector();
		String lsSqlQry =
			"Select count(*) RenewalCount "
				+ "FROM RTS.RTS_ITRNT_TRANS "
				+ "WHERE "
				+ "TRANSCD = '"
				+ TransCdConstant.IRENEW
				+ "' AND "
				+ "OFCISSUANCENO = ? AND "
				+ "CntyStatusCd = "
				+ CommonConstants.NEW;

		Vector lvData = (Vector) aaData;
		int liOfcIssuanceNo =
			((Integer) lvData.elementAt(0)).intValue();
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liOfcIssuanceNo)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsSqlQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			laResultSet.next();
			liRenewalCount =
				caDA.getIntFromDB(laResultSet, "RenewalCount");
			laResultSet.close();
			return liRenewalCount;
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
	 * This method is a changed copy of qryRenewal(Hashtable), and is 
	 * used for simulated MQ testing.
	 * 
	 * @param ahtKeys Hashtable
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryItrntRenewForMQTest(Hashtable ahtKeys)
		throws RTSException
	{
		csMethod = "qryItrntRenewForMQTest(Hashtable)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvRslt = new Vector();

		//obtain keys from function call
		String lsPlateNo = (String) ahtKeys.get("RegPltNo");
		String lsTraceNo = (String) ahtKeys.get("ItrntTraceNo");
		String lsTransactionID = (String) ahtKeys.get("TransId");
		String lsFirstName = (String) ahtKeys.get("FrstName");
		String lsMiddleName = (String) ahtKeys.get("MidlName");
		String lsLastName = (String) ahtKeys.get("LastName");
		String lsStatus = (String) ahtKeys.get("CntyStatusCd");
		String lsCountyID = (String) ahtKeys.get("OfcIssuanceNo");
		//obtain date keys
		String lsBeginDate = (String) ahtKeys.get("StartDate");
		String lsEndDate = (String) ahtKeys.get("EndDate");
		// defect 6604 new variable
		String lshtWhereClause = (String) ahtKeys.get("WhereClause");
		// end defect 6604
		String lsWhereClause = "";

		ResultSet lsResultSet;

		// defect 6604
		StringBuffer lsSqlQry =
			InternetTransaction.getQryRenewalForMQTest();

		if (lshtWhereClause != "")
		{
			lsWhereClause = lshtWhereClause;
		}
		else
		{
			lsWhereClause =
				"WHERE OfcIssuanceNo = "
					+ lsCountyID
					+ " AND TransCd = '"
					+ TransCdConstant.IRENEW
					+ "' ";
		}

		// use CntyPrcsdTimestmp
		//define lsWhereClause based on user input
		if (lsPlateNo != null && lsPlateNo.length() > 0)
		{
			lsWhereClause += "AND RegPltNo LIKE '%" + lsPlateNo + "%' ";
		}
		if (lsTraceNo != null && lsTraceNo.length() > 0)
		{
			lsWhereClause += "AND ItrntTraceNo LIKE '%"
				+ lsTraceNo
				+ "%' ";
		}
		// if TransId is provided, it must be parsed into pieces 
		// that exist in db table.  The alternative is to add this column 
		// into table (recommended) and avoid costly table scans which 
		// occur with non-indexed columns
		if (lsTransactionID != null && lsTransactionID.length() > 0)
		{
			if (lsTransactionID.length() == 17)
			{
				lsWhereClause += "AND TransWsId = "
					+ lsTransactionID.substring(3, 6)
					+ " ";
				lsWhereClause += "AND TransAmDate = "
					+ lsTransactionID.substring(6, 11)
					+ " ";
				lsWhereClause += "AND TransTime = "
					+ lsTransactionID.substring(11, 17)
					+ " ";
			}
		}
		if (lsFirstName != null && lsFirstName.length() > 0)
		{
			lsWhereClause += "AND FrstName LIKE '%"
				+ lsFirstName
				+ "%' ";
		}
		if (lsMiddleName != null && lsMiddleName.length() > 0)
		{
			lsWhereClause += "AND MidlName LIKE '%"
				+ lsMiddleName
				+ "%' ";
		}
		if (lsLastName != null && lsLastName.length() > 0)
		{
			lsWhereClause += "AND (LastName LIKE '%"
				+ lsLastName
				+ "%' OR RecpntName LIKE '%"
				+ lsLastName
				+ "%') ";
		}
		if (lsStatus != null && lsStatus.length() > 0)
		{
			if (lsStatus
				.equals(RegRenProcessingConstants.DECL_ALL_LBL))
			{
				lsWhereClause += "AND CntyStatusCd IN ("
					+ CommonConstants.DECLINED_REFUND_PENDING
					+ ","
					+ CommonConstants.DECLINED_REFUND_APPROVED
					+ ","
					+ CommonConstants.DECLINED_REFUND_FAILED
					+ ") ";
			}
			else
			{
				lsWhereClause += "AND CntyStatusCd = " + lsStatus + " ";
			}
		}
		if (lsBeginDate != null && lsBeginDate.length() > 0)
		{
			RTSDate laRTSBegin =
				new RTSDate(1, Integer.parseInt(lsBeginDate));
			lsWhereClause += "AND CntyPrcsdTimestmp >= '"
				+ laRTSBegin.getTimestamp()
				+ "' ";
		}
		if (lsEndDate != null && lsEndDate.length() > 0)
		{
			lsEndDate =
				String.valueOf(new Integer(lsEndDate).intValue());
			RTSDate laRTSEnd =
				new RTSDate(1, Integer.parseInt(lsEndDate));
			laRTSEnd.setHour(23);
			laRTSEnd.setMinute(59);
			laRTSEnd.setSecond(59);
			laRTSEnd.setMillisecond(999);
			// make it inclusive
			lsWhereClause += "AND CntyPrcsdTimestmp <= '"
				+ laRTSEnd.getTimestamp()
				+ "' ";
		}
		lsSqlQry.append(lsWhereClause);
		lsSqlQry.append(" ORDER BY CntyPrcsdTimestmp");

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			lsResultSet = caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (lsResultSet.next())
			{
				InternetRegRecData laIData =
					getItrntTransDataForMQTest(lsResultSet);
				lvRslt.addElement(laIData);
			}
			lsResultSet.close();
			lsResultSet = null;

			return (lvRslt);
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
	 * Run the Query that is passed and return HTML table of results.
	 * 
	 * @param asQuery
	 * @return String
	 * @throws RTSException
	 */
	public String qryRenewal(String asQuery) throws RTSException
	{
		csMethod = "qryRenewal(String)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		ResultSet laResultSet = null;
		try
		{
			StringBuffer lsResult = new StringBuffer();
			lsResult.append("<tr>");

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSet = caDA.executeDBQuery(asQuery);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			ResultSetMetaData laResultSetMeta =
				laResultSet.getMetaData();

			lsResult.append("<th>RowNo</th>");

			for (int i = 0; i < laResultSetMeta.getColumnCount(); ++i)
			{
				lsResult.append("<th>");
				lsResult.append(laResultSetMeta.getColumnName(i + 1));
				lsResult.append("</th>");
			}
			lsResult.append("</tr>");
			int liCount = 1;
			String lsCol = null;
			while (laResultSet.next())
			{
				lsResult.append("<tr><td>" + liCount + "</td>");
				for (int i = 0;
					i < laResultSetMeta.getColumnCount();
					++i)
				{
					lsResult.append("<td>");
					lsCol = laResultSet.getString(i + 1);
					if (lsCol != null)
					{
						lsCol = lsCol.trim();
						if (lsCol.equals(""))
						{
							lsCol = "&nbsp;";
						}
					}
					lsResult.append(lsCol);
					lsResult.append("</td>");
				}
				lsResult.append("</tr>");
				liCount++;
			}
			return lsResult.toString();
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
			if (laResultSet != null)
			{
				try
				{
					laResultSet.close();
				}
				catch (Throwable leThrowable)
				{
				}
			}
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}
	/**
	 * Query the current record on the database when MQ recs are
	 * processed
	 *
	 * @param aaVehBaseData VehicleBaseData
	 * @param asStatus String
	 * @throws RTSException
	 */
	public Vector qryRenewal(
		VehicleBaseData aaVehBaseData,
		String asStatus)
		throws RTSException
	{
		csMethod = "qryRenewal(VehicleBaseData,String)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvRet = new Vector(2);

		lvRet.add(new Boolean(false));

		ResultSet laResultSet = null;

		try
		{
			String lsSqlQry =
				"Select "
					+ "REGPLTNO, CNTYSTATUSCD, ITRNTPYMNTSTATUSCD, "
					+ "PYMNTAMT"
					+ " from RTS.RTS_ITRNT_TRANS where "
					+ "REGPLTNO='"
					+ aaVehBaseData.getPlateNo()
					+ "' AND "
					+ "VIN='"
					+ aaVehBaseData.getVin()
					+ "' AND "
					+ "DOCNO='"
					+ aaVehBaseData.getDocNo()
					+ "' AND "
					+ "TRANSCD='IRENEW' AND CNTYSTATUSCD "
					+ asStatus;

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSet = caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			if (laResultSet.next())
			{
				lvRet.set(0, new Boolean(true));
				String lsCntyStatusCd =
					laResultSet.getString("CNTYSTATUSCD");
				String lsItrntPymntStatusCd =
					laResultSet.getString("ITRNTPYMNTSTATUSCD");
				String lsPymntAmt = laResultSet.getString("PYMNTAMT");
				lvRet.add(lsCntyStatusCd);
				lvRet.add(lsItrntPymntStatusCd);
				lvRet.add(lsPymntAmt);
			}
			return lvRet;
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
			if (laResultSet != null)
			{
				try
				{
					laResultSet.close();
				}
				catch (Exception leEx)
				{
				}
			}
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}

	}

	/**
	 * Query the status of the renewal.
	 * 
	 * @param ahtData Hashtable
	 * @return int
	 * @throws RTSException
	 */
	public int qryStatus(Hashtable ahtData) throws RTSException
	{
		csMethod = "qryStatus(Hashtable)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		int liRet = 0;

		ResultSet laResultSet = null;

		String lsSqlQry =
			"SELECT COUNT(*) CNT FROM RTS.RTS_ITRNT_TRANS WHERE "
				+ "CntyStatusCd = ? AND "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND TransCd = '"
				+ TransCdConstant.IRENEW
				+ "'";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					(String) ahtData.get("CntyStatusCd")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("RegPltNo")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("VIN")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("DocNo")));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSet = caDA.executeDBQuery(lsSqlQry, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			if (laResultSet.next())
			{
				liRet = laResultSet.getInt(1);
			}
			return liRet;
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
			if (laResultSet != null)
			{
				try
				{
					laResultSet.close();
				}
				catch (Exception aeEx)
				{
				}
			}
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * This function returns a Vector of InternetTransReportData.
	 * Each Vector is a ResultSet referring to distinct
	 * Transaction Types (NEW,HOLD,DECLINED,APPROVED).
	 * 
	 * @param aaReportSearchData ReportSearchData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTransactionReport(ReportSearchData aaReportSearchData)
		throws RTSException
	{
		csMethod = "qryTransactionReport(ReportSearchData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		StringBuffer lsQry = new StringBuffer();
		StringBuffer lsSQL = new StringBuffer();
		String lsWhereClause = "";
		Vector lvRslt = new Vector();
		Vector lvRslt2 = new Vector();
		Vector lvRslt3 = new Vector();
		Vector lvRslt4 = new Vector();
		Vector lvRslt5 = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSetQry;

		// Obtain OfcIssuanceNo
		int liOfcIssuanceNo = aaReportSearchData.getIntKey1();

		// Timestamp 
		Timestamp laFrom = aaReportSearchData.getDate1().getTimestamp();
		Timestamp laTo = aaReportSearchData.getDate2().getTimestamp();

		lsQry.append(
			"SELECT ItrntTraceNo, PymntAmt, CntyStatusCd "
				+ "FROM RTS.RTS_ITRNT_TRANS "
				+ "WHERE CntyStatusCd = ");

		//for each case - execute same query
		String lsSqlNew = CommonConstants.NEW + " ";
		String lsSqlHold = CommonConstants.HOLD + " ";
		String lsSqlDecline =
			CommonConstants.DECLINED_REFUND_PENDING + " ";

		String lsSqlApprove = CommonConstants.APPROVED + " ";
		String lsSqlCheckedOut = CommonConstants.IN_PROCESS + " ";

		//apply county/type selectors in lsWhereClause
		lsWhereClause += "AND OfcIssuanceNo = "
			+ liOfcIssuanceNo
			+ " AND TransCd = '"
			+ TransCdConstant.IRENEW
			+ "' ";

		// Apply Timestamp lsWhereClause if necessary
		if (laFrom != null)
		{
			lsWhereClause += "AND CNTYPRCSDTIMESTMP >= TIMESTAMP('"
				+ laFrom
				+ "') AND CNTYPRCSDTIMESTMP < TIMESTAMP('"
				+ laTo
				+ "') ";
		}

		// Case 1 (New) 
		String lsSql1 = lsQry.toString();
		lsSql1 += lsSqlNew;
		lsSql1 += lsWhereClause;
		lsSql1 += "ORDER BY CntyStatusCd, CNTYPRCSDTIMESTMP";

		csMethod = "qryTransactionReport() - New";

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSetQry = caDA.executeDBQuery(lsSql1);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSetQry.next())
			{
				InternetTransReportData laTransRptData =
					new InternetTransReportData();
				laTransRptData.setTraceNo(
					caDA.getStringFromDB(
						laResultSetQry,
						"ItrntTraceNo"));
				laTransRptData.setStatusCd(
					caDA.getStringFromDB(
						laResultSetQry,
						"CntyStatusCd"));
				//error checking
				Double laDouble =
					new Double(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				if (laDouble.isNaN())
				{
					laTransRptData.setPaymentAmt(0);
				}
				else
				{
					laTransRptData.setPaymentAmt(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				}

				lvRslt.addElement(laTransRptData);
			}
			laResultSetQry.close();
			laResultSetQry = null;
			lvValues.add(lvRslt);
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
		// Case 2 (Hold) 
		String lsSql2 = lsQry.toString();
		lsSql2 += lsSqlHold;
		lsSql2 += lsWhereClause;
		lsSql2 += "ORDER BY CntyStatusCd, CNTYPRCSDTIMESTMP";

		csMethod = "qryTransactionReport() - Hold";

		try
		{
			caDA.beginTransaction();

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSetQry = caDA.executeDBQuery(lsSql2);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSetQry.next())
			{
				InternetTransReportData laTransRptData =
					new InternetTransReportData();
				laTransRptData.setTraceNo(
					caDA.getStringFromDB(
						laResultSetQry,
						"ItrntTraceNo"));
				laTransRptData.setStatusCd(
					caDA.getStringFromDB(
						laResultSetQry,
						"CntyStatusCd"));
				//error checking
				Double laDouble =
					new Double(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				if (laDouble.isNaN())
				{
					laTransRptData.setPaymentAmt(0);
				}
				else
				{
					laTransRptData.setPaymentAmt(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				}
				lvRslt2.addElement(laTransRptData);
			}
			laResultSetQry.close();
			laResultSetQry = null;
			caDA.endTransaction(DatabaseAccess.NONE);
			lvValues.add(lvRslt2);
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

		csMethod = "qryTransactionReport() - Decline";

		//  Case 3  (Decline) 
		lsSQL = new StringBuffer();

		// defect 10421 
		// Must add CntyProcsdTimestmp when UNION 
		lsSQL.append(
			"SELECT ItrntTraceNo, PymntAmt, CntyStatusCd, CntyReasnCd,"
				+ " CntyPrcsdTimestmp "
				+ "FROM RTS.RTS_ITRNT_TRANS "
				+ "WHERE CntyStatusCd IN (");
		// end defect 10421

		String lsSql3 = lsSQL.toString();
		lsSql3 += lsSqlDecline
			+ ", "
			+ CommonConstants.DECLINED_REFUND_APPROVED
			+ ", "
			+ CommonConstants.DECLINED_REFUND_FAILED
			+ ") ";
		lsSql3 += lsWhereClause;

		// defect 10421 
		String lsUnion =
			" UNION "
				+ "SELECT ItrntTraceNo, PymntAmt, CntyStatusCd, CntyReasnCd,"
				+ "CntyPrcsdTimestmp "
				+ "FROM RTS.RTS_ITRNT_TRANS_DEL_LOG "
				+ "WHERE CntyStatusCd IN (";

		lsUnion += CommonConstants.DECLINED_REFUND_APPROVED + ") ";
		lsUnion += lsWhereClause;

		lsSql3 += lsUnion;
		// end defect 10421 

		lsSql3 += " ORDER BY CntyStatusCd, CNTYPRCSDTIMESTMP";

		try
		{
			caDA.beginTransaction();

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSetQry = caDA.executeDBQuery(lsSql3);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSetQry.next())
			{
				InternetTransReportData laTransRptData =
					new InternetTransReportData();
				laTransRptData.setTraceNo(
					caDA.getStringFromDB(
						laResultSetQry,
						"ItrntTraceNo"));
				laTransRptData.setStatusCd(
					caDA.getStringFromDB(
						laResultSetQry,
						"CntyStatusCd"));
				laTransRptData.setReasonCd(
					caDA.getStringFromDB(
						laResultSetQry,
						"CntyReasnCd"));
				//error checking
				Double laDouble =
					new Double(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				if (laDouble.isNaN())
				{
					laTransRptData.setPaymentAmt(0);
				}
				else
				{
					laTransRptData.setPaymentAmt(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				}
				lvRslt3.addElement(laTransRptData);
			}
			laResultSetQry.close();
			laResultSetQry = null;
			caDA.endTransaction(DatabaseAccess.NONE);
			lvValues.add(lvRslt3);
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

		csMethod = "qryTransactionReport() - Approve";

		// Case 4 (Approve) 
		//build query for case 4 (approve)
		lsSQL = new StringBuffer();
		lsSQL.append(
			"SELECT ItrntTraceNo, PymntAmt, CntyStatusCd, "
				+ "OfcIssuanceNo, TransWsId, TransAmDate, TransTime "
				+ "FROM RTS.RTS_ITRNT_TRANS "
				+ "WHERE CntyStatusCd = ");
		String lsSql4 = lsSQL.toString();
		lsSql4 += lsSqlApprove;
		lsSql4 += lsWhereClause;
		lsSql4 += "ORDER BY CntyStatusCd, CNTYPRCSDTIMESTMP";

		try
		{
			caDA.beginTransaction();

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laResultSetQry = caDA.executeDBQuery(lsSql4);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSetQry.next())
			{
				InternetTransReportData laTransRptData =
					new InternetTransReportData();
				laTransRptData.setTraceNo(
					caDA.getStringFromDB(
						laResultSetQry,
						"ItrntTraceNo"));
				laTransRptData.setStatusCd(
					caDA.getStringFromDB(
						laResultSetQry,
						"CntyStatusCd"));
				//error checking
				Double laDouble =
					new Double(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				if (laDouble.isNaN())
				{
					laTransRptData.setPaymentAmt(0);
				}
				else
				{
					laTransRptData.setPaymentAmt(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				}
				// Build TransId string
				String lsTransId =
					UtilityMethods.getTransId(
						caDA.getIntFromDB(
							laResultSetQry,
							"OfcIssuanceNo"),
						caDA.getIntFromDB(laResultSetQry, "TransWsId"),
						caDA.getIntFromDB(
							laResultSetQry,
							"TransAmDate"),
						caDA.getIntFromDB(laResultSetQry, "TransTime"));
				laTransRptData.setTransId(lsTransId);
				lvRslt4.addElement(laTransRptData);
			}
			laResultSetQry.close();
			laResultSetQry = null;
			caDA.endTransaction(DatabaseAccess.NONE);
			lvValues.add(lvRslt4);
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
		csMethod = "qryTransactionReport() - Checked Out";
		// Case 5 (Checked Out)
		String lsSql5 = lsQry.toString();
		lsSql5 += lsSqlCheckedOut;
		lsSql5 += lsWhereClause;
		lsSql5 += "ORDER BY CntyStatusCd, CNTYPRCSDTIMESTMP";

		try
		{
			caDA.beginTransaction();

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
			laResultSetQry = caDA.executeDBQuery(lsSql5);
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSetQry.next())
			{
				InternetTransReportData laTransRptData =
					new InternetTransReportData();
				laTransRptData.setTraceNo(
					caDA.getStringFromDB(
						laResultSetQry,
						"ItrntTraceNo"));
				laTransRptData.setStatusCd(
					caDA.getStringFromDB(
						laResultSetQry,
						"CntyStatusCd"));
				//error checking
				Double laDOuble =
					new Double(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				if (laDOuble.isNaN())
				{
					laTransRptData.setPaymentAmt(0);
				}
				else
				{
					laTransRptData.setPaymentAmt(
						caDA.getDoubleFromDB(
							laResultSetQry,
							"PymntAmt"));
				}
				lvRslt5.addElement(laTransRptData);
			}
			laResultSetQry.close();
			laResultSetQry = null;
			caDA.endTransaction(DatabaseAccess.NONE);
			lvValues.add(lvRslt5);
			return lvValues;
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
			csMethod = "qryTransactionReport(ReportSearchData)";

			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}

	}
	/**
	 * This method verifies a record exists for the vehicle passed.
	 * 
	 * @param asTransCd String
	 * @param aaMFVehData MFVehicleData
	 * @return boolean
	 * @throws RTSException
	 */
	public boolean recordExists(
		String asTransCd,
		MFVehicleData aaMFVehData)
		throws RTSException
	{
		csMethod = "recordExists(String,MFVehicleData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		boolean lbRecordExists = false;

		String lsSQLQry =
			"Select REGPLTNO from RTS.RTS_ITRNT_TRANS "
				+ "Where "
				+ "TRANSCD=? And "
				+ "REGPLTNO=? And "
				+ "VIN=? And "
				+ "DOCNO=?";

		Vector lvValues = new Vector();
		try
		{
			lvValues.addElement(new DBValue(Types.VARCHAR, asTransCd));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getRegData().getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getVehicleData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getTitleData().getDocNo()));

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
	 * specialPlate - Added this method specialPlate to see if any
	 * invProcsngCd of 2 or 3 got through the front end checks.
	 * 
	 * @param  aaOrgVehData MFVehicleData
	 * @return boolean
	 * @throws RTSException
	 */
	public boolean isSpecialPlate(MFVehicleData aaOrgVehData)
	{
		RegistrationData laRegData = aaOrgVehData.getRegData();
		String lsPlateCd = laRegData.getRegPltCd();
		return PlateTypeCache.isOutOfScopePlate(
			lsPlateCd,
			SpecialPlatesConstant.INTERNET_OFCISSUANCECD,
			SpecialPlatesConstant.REGIS_TYPE_EVENTS);
	}

	/**
	 * Update the County Status Code.
	 * 
	 * @param ahtData Hashtable
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateCntyStatus(Hashtable ahtData)
		throws RTSException
	{
		VehicleBaseData laVehBaseData =
			(VehicleBaseData) ahtData.get("VehBaseData");

		int liCntyStatusCd =
			new Integer((String) ahtData.get("CntyStatusCd"))
				.intValue();

		String lsCachedTransDateTime =
			(String) ahtData.get("TransDateTime");

		return updateCntyStatus(
			laVehBaseData,
			liCntyStatusCd,
			lsCachedTransDateTime);
	}

	/**
	 * Update the County Status Code.
	 * 
	 * @param aaVehBaseData VehicleBaseData 
	 * @param aiCntyStatusCd int
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateCntyStatus(
		VehicleBaseData aaVehBaseData,
		int aiCntyStatusCd)
		throws RTSException
	{
		return updateCntyStatus(aaVehBaseData, aiCntyStatusCd, null);
	}

	/**
	 * Update the County Status Code.
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 * @param aiCntyStatusCd int
	 * @param asCachedTransDateTime String
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateCntyStatus(
		VehicleBaseData aVehBaseData,
		int aiCntyStatusCd,
		String asCachedTransDateTime)
		throws RTSException
	{
		csMethod = "updateCntyStatus(VehicleBaseData,int,String)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ "CntyStatusCd = ? "
				+ "WHERE "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND "
				+ "TransCd = ? AND "
				+ "CntyStatusCd !="
				+ CommonConstants.DECLINED_REFUND_APPROVED;

		if (asCachedTransDateTime != null)
		{
			// Cached transactions are out-of-date if they bear a
			// timestamp which is older than the latest county 
			// processed timestamp.
			lsSqlUpd += " AND CntyPrcsdTimestmp<?";
		}

		Vector lvValues = new Vector();
		Object laRet = null;
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiCntyStatusCd)));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) aVehBaseData.getPlateNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) aVehBaseData.getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) aVehBaseData.getDocNo()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, TransCdConstant.IRENEW));
			if (asCachedTransDateTime != null)
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						asCachedTransDateTime));

			// defect 8966
			System.out.println("lsSqlUpd = " + lsSqlUpd);
			System.out.println("aiCntyStatusCd = " + aiCntyStatusCd);
			System.out.println("aVehBaseData.getPlateNo() = "
					+ aVehBaseData.getPlateNo());
			System.out.println("aVehBaseData.getVin() = "
					+ aVehBaseData.getVin());
			System.out.println("aVehBaseData.getDocNo() = "
					+ aVehBaseData.getDocNo());
			System.out.println("TransCdConstant.IRENEW = "
					+ TransCdConstant.IRENEW);
			System.out.println("asCachedTransDateTime = "
					+ asCachedTransDateTime);
			// end defect 8966

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laRet =
				String.valueOf(
					caDA.executeDBInsertUpdateDelete(
						lsSqlUpd,
						lvValues));
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			// defect 8966
			System.out.println("Rows updated = " + laRet);
			// end defect 8966
			return laRet;

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
	 * Update the County Status Code.
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 * @param aiCntyStatusCd int
	 * @param asCachedTransDateTime String
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateCntyStatus(
		int aiCntyStatusCd,
		String asTraceNo)
		throws RTSException
	{
		csMethod = "updateCntyStatus(int,String)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ "CntyStatusCd = ? "
				+ "WHERE "
				+ "itrnttraceno = "
				+ "'"
				+ asTraceNo
				+ "'"
				+ " and CntyStatusCd in ("
				+ CommonConstants.DECLINED_REFUND_PENDING
				+ ","
				+ CommonConstants.DECLINED_REFUND_FAILED
				+ ")";

		Vector lvValues = new Vector();
		Object laRet = null;
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiCntyStatusCd)));
			System.out.println("lsSqlUpd = " + lsSqlUpd);
			System.out.println("Updating trace number "
					+ asTraceNo
					+ " with aiCntyStatusCd = "
					+ aiCntyStatusCd);

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laRet =
				String.valueOf(
					caDA.executeDBInsertUpdateDelete(
						lsSqlUpd,
						lvValues));

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			System.out.println("Rows updated = " + laRet);

			return laRet;

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
	 * Update the county Status code.
	 * 
	 * @param asTransCd String
	 * @param aiCntyStatusCd int
	 * @param aaMFVehData MFVehicleData
	 * @throws RTSException
	 */
	public void updateCntyStatusCd(
		String asTransCd,
		int aiCntyStatusCd,
		MFVehicleData aaMFVehData)
		throws RTSException
	{
		csMethod = "updateCntyStatus(String,int,MFVehicleData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ "CntyStatusCd = ? "
				+ "WHERE "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND "
				+ "TransCd = ?";

		Vector lvValues = new Vector();
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiCntyStatusCd)));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getRegData().getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getVehicleData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaMFVehData.getTitleData().getDocNo()));
			lvValues.addElement(new DBValue(Types.VARCHAR, asTransCd));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsSqlUpd, lvValues);

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
	 * Method to "update" RTS.RTS_ITRNT_TRANS.  
	 * This has the effect of putting the row(s) in 
	 * exclusive lock status.
	 * 
	 * @param  aaCompRegRenData CompleteRegRenData	
	 * @return int
	 * @throws RTSException 
	 */
	public void updItrntTransForLogicalLock(CompleteRegRenData aaCompRegRenData)
		throws RTSException
	{
		MQLog.info(
			" InternetTransaction, updItrntTransForLogicalLock - "
				+ "Start, plate="
				+ aaCompRegRenData.getVehBaseData().getPlateNo());

		Vector lvValues = new Vector();

		String lsSQLUpdate =
			"UPDATE RTS.RTS_ITRNT_TRANS SET RegPltNo=?"
				+ "WHERE TRANSCD='IRENEW' AND "
				+ "RegPltNo=? AND "
				+ "VIN=? AND "
				+ "DocNo=?";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getPlateNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getPlateNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getDocNo()));

			MQLog.info( 
				" updItrntTransForLogicalLock lsSQLUpdate = "
					+ lsSQLUpdate);

			MQLog.info( 
				" rows locked by updItrntTransForLogicalLock = "
					+ caDA.executeDBInsertUpdateDelete(
						lsSQLUpdate,
						lvValues));

			MQLog.info(
				" InternetTransaction, updItrntTransForLogicalLock - "
					+ "End, plate="
					+ aaCompRegRenData.getVehBaseData().getPlateNo());

			//return (liNumRows);
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updItrntTransForLogicalLock - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD

	/**
	 * Update the internet registration renewal with MQ information
	 *
	 * @param asCompRegRenData CompleteRegRenData
	 * @throws RTSException
	 */
	public void updRenewal(CompleteRegRenData aaCompRegRenData)
		throws RTSException
	{
		csMethod = "updRenewal(CompleteRegRenData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		// defect 8972
		updItrntTransForLogicalLock(aaCompRegRenData);
		// end defect 8972

		String lsPlateNo =
			aaCompRegRenData.getVehBaseData().getPlateNo();
		MQLog.info(
			"InternetTransaction, updRenewal - Start, plate="
				+ lsPlateNo);

		Vector lvValues = new Vector();
		// defect 8547
		boolean lbRecFound = false;
		// end defect 8547

		// defect 8365
		String lsItrntPymntStatusCd = null;
		String lsSQLUpdate = "UPDATE RTS.RTS_ITRNT_TRANS SET "
			// defect 9944
		// don't reset ItrntTimeStmp with CURRENT TIMESTAMP
		// leave the value as it was when inserted
		// + "ItrntTimeStmp=CURRENT TIMESTAMP,"
		// end defect 9944
	+"CntyPrcsdTimeStmp=CURRENT TIMESTAMP,"
			// *********************
		// 7
	+"OfcIssuanceNo=?," + "CntyStatusCd=?," + "ItrntTraceNo=?,";
		// end defect 8365

		try
		{
			// defect 8365
			Vector lvCurrentRecord =
				qryRenewal(aaCompRegRenData.getVehBaseData(), "> 0");
			//defect 7718

			// defect 8547
			lbRecFound =
				((Boolean) lvCurrentRecord.elementAt(0)).booleanValue();
			// if (lvCurrentRecord.isEmpty())
			if (!lbRecFound)
			{
				MQLog.info(
					"MQ Attempt to update a record that "
						+ "does not exist on the database");
				// end defect 8547
				logErrorMsg(aaCompRegRenData);
				return;
			}
			String lsCurrentCntyStatus =
				(String) lvCurrentRecord.elementAt(1);
			String lsCurrentPymntStatus =
				(String) lvCurrentRecord.elementAt(2);
			// defect 8961
			String lsPymntAmt = (String) lvCurrentRecord.elementAt(3);
			// end defect 8961 	
			// end defect 7718
			// end defect 8365

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehBaseData()
						.getOwnerCountyNo()));

			// defect 6604
			if ((aaCompRegRenData.getItrntPymntStatusCd()
				== CommonConstants.PAYMENT_CAPTURE_SUCCESS)
				|| (aaCompRegRenData.getItrntPymntStatusCd()
					== CommonConstants.PAYMENT_CAPTURE_CONVFEE_FAILED))
				// if conv fee not collected, update to new, to let 
				// county approve
			{

				// update cntystatuscd to new only if current record 
				// is unpaid cntystatuscd
				// lvCurrentRecord.elementAt(1) is current cntystatuscd
				if (lsCurrentCntyStatus
					.equals(
						DatabaseAccess.convertToString(
							CommonConstants.UNPAID)))
				{
					lvValues
							.addElement(new DBValue(
									Types.INTEGER,
									DatabaseAccess
											.convertToString(CommonConstants.NEW)));
				}
				else
				{
					// defect 7696
					// lvCurrentRecord.elementAt(2) is
					// itrntpymntstatuscd.  Check to see if current 
					// itrntpymntstatuscd = 2.  If so, this is an INET 
					// attempt to collect the conv fee. Keep 
					// cntystatuscd the same
					if (lsCurrentPymntStatus
						.equals(
							DatabaseAccess.convertToString(
								CommonConstants
									.PAYMENT_CAPTURE_CONVFEE_FAILED)))
					{
						//lsCurrentCntyStatus =
						//	(String) lvCurrentRecord.elementAt(1);
						lvValues.addElement(
							new DBValue(
								Types.INTEGER,
								lsCurrentCntyStatus));
						// end defect 7696    	
					}
					else
					{
						// defect 6604
						// added logging for when MQ attempts to update
						// a record that's not in unpaid status
						//
						// logging moved to logErrorMsg(aCompRegRenData)
						//	 	    	
						//defect 7718
						//Add more logging than defect 6604 had
						MQLog.info(
							"MQ Attempt to update a record with "
								+ "a non-unpaid Cntytatuscd");
						logErrorMsg(aaCompRegRenData);
						// end defect 7718
						return;
					}
				}
			}
			//end defect 6604
			else
			{

				// defect 8961
				// aCompRegRenData.getItrntPymntStatusCd() == 
				// 1 or 3 or 4 or 5, so the record is unpaid
				// end defect 8961
				// defect 8365          
				if (lsCurrentCntyStatus
					.equals(
						DatabaseAccess.convertToString(
							CommonConstants.UNPAID)))
				{
					//Real unwanted not preauthorized unpaid records are 
					//identified with ItrntPymntStatusCd = null, 4, or 5
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								CommonConstants.UNPAID)));
				}
				else
					// end defect 8365   
					{
					MQLog.info(
						"MQ Attempt to update a record "
							+ "previously updated by MQ");
					logErrorMsg(aaCompRegRenData);
					return;
				}

				// defect 8961
				// if (lsPymntAmt != null), then we have previously 
				// gotten Epay payment info back, so a 
				// PAYMENT_IN_PROCESS is out of order

				if (lsPymntAmt != null)
				{
					if (aaCompRegRenData.getItrntPymntStatusCd()
						== CommonConstants.PAYMENT_IN_PROCESS)
						// end defect 8365 	
					{
						MQLog.info(
							"MQ Attempt to update a record "
								+ "previously updated by MQ");
						logErrorMsg(aaCompRegRenData);
						return;
					}
				}
				// end defect 8961 
			}

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getTraceNumber()));

			// defect 8365
			if (aaCompRegRenData.getItrntPymntStatusCd()
				== CommonConstants.PAYMENT_PREAUTH_FAILED)
			{
				lsItrntPymntStatusCd = "ItrntPymntStatusCd=null,";

			}
			else
			{
				lsItrntPymntStatusCd = "ItrntPymntStatusCd=?,";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCompRegRenData.getItrntPymntStatusCd())));
			}

			lsSQLUpdate += lsItrntPymntStatusCd
				+ "PymntOrderId=?,"
				+ "PymntAmt=?,"
				+ "CnvncFee=?,"
			// 6
			+"InsCmpnyName=?,"
				+ "InsPlcyNo=?,"
				+ "InsAgentName=?,"
				+ "InsPhoneNo=?,"
				+ "InsPlcyStartDate=?,"
				+ "InsPlcyEndDate=?,"
			// 4
			+"FrstName=?,"
				+ "MidlName=?,"
				+ "LastName=?,"
				+ "EmailAddr=?,"
			// 8
			+"RecpntName=?,"
				+ "RenwlMailngSt1=?,"
				+ "RenwlMailngSt2=?,"
				+ "RenwlMailngCity=?,"
				+ "RenwlMailngState=?,"
				+ "RenwlMailngZpCd=?,"
				+ "RenwlMailngZpCd4=?, "
				+ "CustPhoneNo=?, "
				// defect 10996
				+ "ItrntPymntTimeStmp=? "
				// end defect 10996
				+ "WHERE "
			// 3
			// **********************						 
			+"TRANSCD='IRENEW' AND "
				+ "RegPltNo=? AND "
				+ "VIN=? AND "
				+ "DocNo=?";
			// end defect 8365
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getPymntOrderId()));
			// defect 10998
/*			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					aaCompRegRenData.getTotalFeesString()));
*/
			// defect 11133
//			lvValues.addElement(
//				new DBValue(
//					Types.DECIMAL,
//					DatabaseAccess.convertToString(new Dollar (aaCompRegRenData.getTotal()))));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(new Dollar (aaCompRegRenData.getTotalFees()))));
			// end defect 11133
			// end defect 10998
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					aaCompRegRenData.getConvFeeString()));

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehInsuranceData()
						.getCompanyName()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehInsuranceData()
						.getPolicyNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehInsuranceData()
						.getAgentName()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehInsuranceData()
						.getPhoneNo()));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehInsuranceData()
							.getPolicyStartDt())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCompRegRenData
							.getVehInsuranceData()
							.getPolicyEndDt())));

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehUserData().getFirstName()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehUserData().getMiddleName()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehUserData().getLastName()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehUserData().getEmail()));

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getRecipientName()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getAddress()
						.getSt1()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getAddress()
						.getSt2()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getAddress()
						.getCity()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getAddress()
						.getState()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getAddress()
						.getZpcd()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getAddress()
						.getZpcdp4()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData
						.getVehUserData()
						.getPhoneNumber()));
			// defect 10996
			lvValues.addElement(new DBValue(Types.VARCHAR,
					aaCompRegRenData.getPymntTimeStmp()));
			// end defect 10996
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getPlateNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getDocNo()));

			// defect 8966  
			// MQLog.fine
			// ("InternetRegistrationRenewal - updRenewal 
			// - TRANS - Begin"); 
			// defect 10995
//			RTSDate laDate = RTSDate.getCurrentDate();
			System.out.println("InternetRegistrationRenewal - updRenewal - "
					+ "executeDBInsertUpdateDelete - Begin");
			System.out.println(
					"lsSQLUpdate = " + lsSQLUpdate);
			System.out.println(  
				"aCompRegRenData.getItrntPymntStatusCd() = "
					+ aaCompRegRenData.getItrntPymntStatusCd());
			System.out.println( 
				"aCompRegRenData.getVehBaseData().getPlateNo() = "
					+ aaCompRegRenData.getVehBaseData().getPlateNo());
			System.out.println(  
				"aCompRegRenData.getVehBaseData().getVin() = "
					+ aaCompRegRenData.getVehBaseData().getVin());
			System.out.println( 
				"aCompRegRenData.getVehBaseData().getDocNo() = "
					+ aaCompRegRenData.getVehBaseData().getDocNo());
			// end defect 10995

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			//cDA.executeDBInsertUpdateDelete(lsSQLUpdate, lvValues);            
			MQLog.info(
				"rows updated = "
					+ caDA.executeDBInsertUpdateDelete(
						lsSQLUpdate,
						lvValues));

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			//MQLog.fine
			//("InternetRegistrationRenewal - updRenewal - TRANS - End");
			MQLog.info( 
				"InternetRegistrationRenewal - updRenewal - "
					+ "executeDBInsertUpdateDelete - End");
			// end defect 8966
			MQLog.info(
				"InternetRegistrationRenewal, updRenewal - End, plate="
					+ lsPlateNo);
		}
		catch (RTSException aeRTSEx)
		{
			MQLog.error(aeRTSEx);
			// defect 7718
			// When interent users get aroung the web page length edits
			// with a data entry product like RoboForm, the SQL update
			// uses the web form data that's too long, and as a result
			// the sql returns with a "truncation" error
			// defect 10816
			// do not repost to MQ for NumberFormatExceptions 
//			if (aeRTSEx.getDetailMsg() != null
//				&& aeRTSEx.getDetailMsg().indexOf(TRUNCATION) > -1)
			if (aeRTSEx.getDetailMsg() != null)
			{				
				if (aeRTSEx.getDetailMsg().indexOf(TRUNCATION) > -1 ||
 					aeRTSEx.getDetailMsg().indexOf(NFE) > -1)
 				{		
					MQLog.info(
						"MQ Attempt to update a record encountered "
							+ "the above RTSException");
					logErrorMsg(aaCompRegRenData);
					// defect 10816
					if (SystemProperty.getDatasource().equals(PROD_DATASOURCE))
					{
						csEmailSubject = PRODUCTION + csEmailSubject;
					}
					else
					{
						csEmailSubject = TEST + csEmailSubject;
					}
	
					try
					{
						// defect 10610										
						// EmailUtil laEmailUtil = new EmailUtil();
						EmailUtil laEmailUtil = new EmailUtil();
						// end defect 10610
						laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_TO,
						csEmailSubject,
						EMAIL_BODY + HTML_NEW_LINE +
						// defect 10995
						"TraceNo = "
						+ aaCompRegRenData.getVehUserData().getTraceNumber()
						+ HTML_NEW_LINE +
						"PlateNo = "
						+ aaCompRegRenData.getVehBaseData().getPlateNo()
						+ HTML_NEW_LINE +
						"RTSEx.getDetailMsg() = " +
						// end defect 10995
						aeRTSEx.getDetailMsg());
					}
					catch (Exception aeEx)
					{
						aeEx.printStackTrace();
						MQLog.info(
							"Error attempting to email MQ error msg "
								+ "for the above RTSException");
					}
				}
 			}
			// end defect 10816
			else
			{
				// throwing will put this back in the MQ queue, 
				// which we don't want for the truncation error	
				// end defect 7718    	
				throw aeRTSEx;
			}
		}
		catch (Exception aeEx)
		{
			MQLog.error(aeEx);
			// defect 7718
			// When interent users get around the web page length edits
			// with a data entry product like RoboForm, the SQL update
			// uses the web form data that's too long, and as a result
			// the sql returns with a "truncation" error 
			// defect 10816
			// do not repost to MQ for NumberFormatExceptions  
//			if (aeEx.getMessage() != null
//				&& aeEx.getMessage().indexOf(TRUNCATION) > -1)
//			{
			if (aeEx.getMessage() != null)
			{
				if (aeEx.toString().indexOf(TRUNCATION) > -1 ||
					aeEx.toString().indexOf(NFE) > -1)
				{			
					MQLog.info(
						"MQ Attempt to update a record encountered "
							+ "the above Exception");
					logErrorMsg(aaCompRegRenData);
//					return;
					if (SystemProperty.getDatasource().equals(PROD_DATASOURCE))
					{
						csEmailSubject = PRODUCTION + csEmailSubject;
					}
					else
					{
						csEmailSubject = TEST + csEmailSubject;
					}
	
					try
					{
						// defect 10610										
						// EmailUtil laEmailUtil = new EmailUtil();
						EmailUtil laEmailUtil = new EmailUtil();
						// end defect 10610
						laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_TO,
						csEmailSubject,
						EMAIL_BODY + HTML_NEW_LINE +
						// defect 10995
						"TraceNo = "
						+ aaCompRegRenData.getVehUserData().getTraceNumber()
						+ HTML_NEW_LINE +
						// end defect 10995
						aeEx.getMessage());
					}
					catch (Exception aeEx2)
					{
						aeEx2.printStackTrace();
						MQLog.info(
							"Error attempting to email MQ error msg "
								+ "for the above RTSException");
					}
				// end defect 10816	
				}
				else
				{
					// throwing will put this back in the MQ queue, 
					// which we don't want for the truncation error
					// end defect 7718    	
					throw new RTSException(RTSException.JAVA_ERROR, aeEx);
				}
			}
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
	 * Qry Pymntamt.
	 * 
	 * @param ahtData Hashtable
	 * @return int
	 * @throws RTSException
	 */
	public int qryPymntamt(Hashtable ahtData) throws RTSException
	{
		csMethod = "checkPymntamt(Hashtable)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsItrntTraceNo = (String) ahtData.get("TraceNo");
		String lsPymntAmt = (String) ahtData.get("RefAmt");
		String lsRegPltNo = (String) ahtData.get("RegPltNo");
		int liNumDupPymntAmts = 0;
		ResultSet lrsQry;
		String lsQry =
			"Select count(*) as Count from "
				+ "rts.rts_itrnt_trans a where itrnttraceno = "
				+ "'"
				+ lsItrntTraceNo
				+ "'"
				+ " and pymntamt = "
				+ lsPymntAmt
				+ " and  regpltno = "
				+ "'"
				+ lsRegPltNo
				+ "'"
				+ " and exists (select * from "
				+ " rts.rts_itrnt_trans b where a.itrnttraceno = "
				+ " b.itrnttraceno and a.pymntamt = b.pymntamt and "
				+ " a.regpltno != b.regpltno)";

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			while (lrsQry.next())
			{
				liNumDupPymntAmts = caDA.getIntFromDB(lrsQry, "Count");

				break;
			}

		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCountDuplicates - Exception "
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
		return liNumDupPymntAmts;
	}

	/**
	 * Update Renewal.
	 * 
	 * @param ahtData Hashtable
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateRenewal(Hashtable ahtData) throws RTSException
	{
		csMethod = "updateRenewal(Hashtable)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		String lsStatusCd = (String) ahtData.get("CntyStatusCd");
		int liActionCode = Integer.parseInt(lsStatusCd);
		if (liActionCode == CommonConstants.APPROVED)
		{
			lsStatusCd = String.valueOf(CommonConstants.IN_PROCESS);
		}

		// address change part	
		Hashtable lhtAddressUpdate =
			(Hashtable) ahtData.get("AddressUpdateHash");
		String lsAddressUpdate = "";

		if (lhtAddressUpdate != null)
		{
			lsAddressUpdate =
				"RecpntName = ?, "
					+ "RenwlMailngSt1 = ?, "
					+ "RenwlMailngSt2 = ?, "
					+ "RenwlMailngCity = ?, "
					+ "RenwlMailngState = ?, "
					+ "RenwlMailngZpCd = ?, "
					+ "RenwlMailngZpCd4 = ?, "
					+ "CustPhoneNo = ?,";
		}
		String lsFromCache = (String) ahtData.get("FromCache");
		String lsCondition =
			" AND cntystatuscd=" + CommonConstants.IN_PROCESS;

		if (lsFromCache != null)
		{
			// if from cache, the last (by timestamp) cache updates the record.
			lsCondition = " AND cntyprcsdtimestmp<?";
		}

		Object laRet = null;
		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ lsAddressUpdate
				+ "CntyStatusCd = ?, "
				+ "CntyPrcsdTimestmp = CURRENT TIMESTAMP, "
				+ "CntyReasnCd = ?, "
				+ "CntyReasnTxt = ?, "
				+ "SendEmailIndi = ?"
				+ " WHERE "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND TransCd = '"
				+ TransCdConstant.IRENEW
				+ "'"
				+ lsCondition;
		// + " AND cntystatuscd="+CommonConstants.IN_PROCESS;   
		try
		{
			// address change part
			if (lhtAddressUpdate != null)
			{
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get("RecpntName")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get(
							"RenwlMailngSt1")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get(
							"RenwlMailngSt2")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get(
							"RenwlMailngCity")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get(
							"RenwlMailngState")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get(
							"RenwlMailngZpCd")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get(
							"RenwlMailngZpCd4")));
				lvValues.addElement(
					new DBValue(
						Types.VARCHAR,
						(String) lhtAddressUpdate.get("CustPhoneNo")));
			}
			lvValues.addElement(new DBValue(Types.INTEGER, lsStatusCd));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					(String) ahtData.get("CntyReasnCd")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("CntyReasnTxt")));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					(String) ahtData.get("SendEmailIndi")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("RegPltNo")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("VIN")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("DocNo")));
			if (lsFromCache != null)
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						(String) ahtData.get("TransDateTime")));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laRet =
				String.valueOf(
					caDA.executeDBInsertUpdateDelete(
						lsSqlUpd,
						lvValues));

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return laRet;
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
	 * This method is used for Phase1's calling back for updating
	 * the renewal for approve transactions, and for cached
	 * transactions sent from Phase1.
	 * 
	 * @param aaData InternetTransactionData
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateRenewal(InternetTransactionData aaData)
		throws RTSException
	{
		Hashtable lhtData = (Hashtable) aaData.getData();
		String lsAction = (String) lhtData.get("Action");
		lhtData.put("FromCache", "TRUE");
		lhtData.put(
			"TransDateTime",
			aaData.getTransDateTime().getTimestamp().getTime() + "");
		if (lsAction != null
			&& lsAction.equalsIgnoreCase("UndoCheckout"))
		{
			// only undoCheckout has action stored in the hashtable object.
			return updateCntyStatus(lhtData);
		}
		else
		{
			return updateRenewal(lhtData);
		}
	}

	/**
	 * Update a record after sending the email, indicate the email 
	 * is sent
	 * 
	 * @param aaEmailData EmailData
	 * @throws RTSException
	 */
	public void updRenewalEmailIndi(EmailData aaEmailData)
		throws RTSException
	{
		csMethod = "updRenewalEmailIndi(EmailData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Log.write(Log.METHOD, this, "updRenewalEmailIndi - Start");
		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS "
				+ "Set SendEmailIndi=0 "
				+ "where SendEmailIndi=1 "
				+ "AND RegPltNo=? "
				+ "AND DocNo=? "
				+ "AND Vin=?";
		Vector lvValues = new Vector();

		try
		{
			lvValues.addElement(
				new DBValue(Types.VARCHAR, aaEmailData.getPlateNo()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, aaEmailData.getDocNo()));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, aaEmailData.getVin()));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsSqlUpd, lvValues);

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
	 * Update every record in a shopping cart with the eReminder
	 * code for emailing eReminders for renewals 
	 * 
	 * @param asTraceNumber String, aiEmailReqCd int
	 * @throws RTSException
	 */
	public void updRenewalEmailReqCd(String asTraceNumber, int aiEmailReqCd)
		throws RTSException
	{
		csMethod = "updRenewalEmailReqCd(asTraceNumber)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Log.write(Log.METHOD, this, "updRenewalEmailReqCd - Start");
		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS "
				+ "Set updRenewalEmailReqCd = " + aiEmailReqCd
				+ " where itrnttraceno = " + asTraceNumber;
		Vector lvValues = new Vector();

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiEmailReqCd)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsSqlUpd, lvValues);

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

	// defect 10239 
	//	/**
	//	 * Update Renewal Address.
	//	 * 
	//	 * @param ahtData Hashtable
	//	 * @return Object
	//	 * @throws RTSException
	//	 */
	//	public Object updateRenewalAddress(Hashtable ahtData)
	//		throws RTSException
	//	{
	//		csMethod = "updateRenewalAddress(Hashtable)";
	//
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			csMethod + CommonConstant.SQL_METHOD_BEGIN);
	//
	//		Vector lvValues = new Vector();
	//
	//		Object laRet = null;
	//		String lsSqlUpd =
	//			"UPDATE RTS.RTS_ITRNT_TRANS SET "
	//				+ "RecpntName = ?, "
	//				+ "RenwlMailngSt1 = ?, "
	//				+ "RenwlMailngSt2 = ?, "
	//				+ "RenwlMailngCity = ?, "
	//				+ "RenwlMailngState = ?, "
	//				+ "RenwlMailngZpCd = ?, "
	//				+ "CustPhoneNo = ?"
	//				+ " WHERE "
	//				+ "RegPltNo = ? AND "
	//				+ "VIN = ? AND "
	//				+ "DocNo = ? AND "
	//				+ "TransCd = '"
	//				+ TransCdConstant.IRENEW
	//				+ "'";
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RecpntName")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RenwlMailngSt1")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RenwlMailngSt2")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RenwlMailngCity")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RenwlMailngState")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RenwlMailngZpCd")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("CustPhoneNo")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("RegPltNo")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("VIN")));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.VARCHAR,
	//					(String) ahtData.get("DocNo")));
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				csMethod + CommonConstant.SQL_BEGIN);
	//
	//			laRet =
	//				String.valueOf(
	//					caDA.executeDBInsertUpdateDelete(
	//						lsSqlUpd,
	//						lvValues));
	//
	//			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
	//			return laRet;
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
	//	}
	// end defect 10239 

	/**
	 * Update the Status.
	 * 
	 * @param ahtData Hashtable
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateStatus(Hashtable ahtData) throws RTSException
	{
		csMethod = "updateStatus(Hashtable)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		Object laRet = null;

		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ "CntyStatusCd = ?, "
				+ "TransWsId = ?, "
				+ "SubstaId = ?"
				+ " WHERE "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND TransCd = '"
				+ TransCdConstant.IRENEW
				+ "'";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					(String) ahtData.get("NewStatus")));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					(String) ahtData.get("TransWsId")));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					(String) ahtData.get("SubstaId")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("RegPltNo")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("VIN")));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					(String) ahtData.get("DocNo")));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laRet =
				String.valueOf(
					caDA.executeDBInsertUpdateDelete(
						lsSqlUpd,
						lvValues));

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			return laRet;
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
	 * Update the County Status Code to New.
	 * 
	 * @param aiOldStatus int
	 * @throws RTSException
	 */
	public void updateStatusToNew(int aiOldStatus) throws RTSException
	{
		csMethod = "updateStatusToNew(int)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ "CntyStatusCd = ? "
				+ "WHERE "
				+ "TransCd = ? AND "
				+ "CntyStatusCd = ?";
		Vector lvValues = new Vector();
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						CommonConstants.NEW)));
			lvValues.addElement(
				new DBValue(Types.VARCHAR, TransCdConstant.IRENEW));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOldStatus)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsSqlUpd, lvValues);

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
	 * Update the TX information.
	 * 
	 * @param aaData TransactionCacheData
	 * @return Object
	 * @throws RTSException
	 */
	public Object updateTXInfo(TransactionCacheData aaData)
		throws RTSException
	{
		csMethod = "updateTXInfo(TransactionCacheData)";
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		TransactionCacheData laTransactionCacheData = aaData;
		MFVehicleData laMFVehicleData =
			(MFVehicleData) laTransactionCacheData.getObj();

		Vector lvValues = new Vector();

		Object laRet = null;
		String lsSqlUpd =
			"UPDATE RTS.RTS_ITRNT_TRANS SET "
				+ "TransAMDate = ?, "
				+ "TransTime = ?"
				+ " WHERE "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND TransCd = '"
				+ TransCdConstant.IRENEW
				+ "'";

		// defect 10505 
		Vector lvValuesTransTime = new Vector();
		
		String lsSqlUpdTransTime =
			"Update RTS.RTS_ITRNT_TRANS SET TransId = "
				+ "CONCAT(RIGHT( '00' CONCAT RTRIM(CHAR(OFCISSUANCENO)),3),"
				+ "CONCAT(RIGHT( '00' CONCAT RTRIM(CHAR(TRANSWSID)),3), "
				+ "CONCAT(RTRIM(CHAR(TRANSAMDATE)),"
				+ "RIGHT( '0' CONCAT RTRIM(CHAR(TRANSTIME)),6)))) "
				+ " WHERE "
				+ "RegPltNo = ? AND "
				+ "VIN = ? AND "
				+ "DocNo = ? AND TransCd = '"
				+ TransCdConstant.IRENEW
				+ "'";
		// end defect 10505 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					String.valueOf(laMFVehicleData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					String.valueOf(laMFVehicleData.getTransTime())));

			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					laMFVehicleData.getRegData().getRegPltNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					laMFVehicleData.getVehicleData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					laMFVehicleData.getTitleData().getDocNo()));

			// defect 10505 
			lvValuesTransTime.addElement(
				new DBValue(
					Types.VARCHAR,
					laMFVehicleData.getRegData().getRegPltNo()));
			lvValuesTransTime.addElement(
				new DBValue(
					Types.VARCHAR,
					laMFVehicleData.getVehicleData().getVin()));
			lvValuesTransTime.addElement(
				new DBValue(
					Types.VARCHAR,
					laMFVehicleData.getTitleData().getDocNo()));
			// end defect 10505 

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			laRet =
				String.valueOf(
					caDA.executeDBInsertUpdateDelete(
						lsSqlUpd,
						lvValues));

			// defect 10505 
			caDA.executeDBInsertUpdateDelete(
				lsSqlUpdTransTime,
				lvValuesTransTime);
			// end defect 10505 

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			return laRet;
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
	 * Update the Batch Payment Status.
	 * 
	 * @param aaData PaymentData
	 * @throws RTSException
	 */
	public void updBatchPymntStatus(PaymentData aaData)
		throws RTSException
	{
		csMethod = "updBatchPymntStatus(PaymentData)";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlUpd = "";
		// the following 2 if blocks are to test bad Epay return codes
		//if //(aData.getOrigTraceNo().substring(
		//			0,aData.getOrigTraceNo().length() - 1).
		//					equals("43VRR92FJSGL2") ||
		//	(aData.getOrigTraceNo().substring(
		//			0,aData.getOrigTraceNo().length() - 1).
		//					equals("43VRRYF33XQLQ") || 
		//	aData.getOrigTraceNo().substring(
		//			0,aData.getOrigTraceNo().length() - 1).
		//					equals("43VRS07JBDBE7") ||  
		//	aData.getOrigTraceNo().substring(
		//			0,aData.getOrigTraceNo().length() - 1).
		//					equals("43VRS13HBKLM0"))
		//	{
		//	aData.setPmntStatusCd(
		//				CommonConstants.PAYMENT_CAPTURE_CONVFEE_FAILED);					
		//	}
		//
		//if (aData.getOrigTraceNo().substring(
		//			0,aData.getOrigTraceNo().length() - 1).
		//					equals("43VRR92FJSGL2"))
		//{
		//	aData.setPmntStatusCd(
		//				CommonConstants.PAYMENT_CAPTURE_RENEWAL_FAILED);					
		//	}

		// defect 6604 - check current record for its status	
		// defect 10849
//		String lsTracenoLookup =
//			aaData.getOrigTraceNo().substring(
//				0,
//				aaData.getOrigTraceNo().length() - 1);
		String lsTracenoLookup =
			aaData.getOrigTraceNo().substring(
				0,
				aaData.getOrigTraceNo().length() - 2);
		// end defect 10849

		// defect 6604 - check current record for its status
		Hashtable lhtHashTable = new Hashtable();
		lhtHashTable.put("ItrntTraceNo", lsTracenoLookup);
		//ht.put("RegPltNo",
		//			cCompleteRegRenData.getVehBaseData().getPlateNo());
		lhtHashTable.put(
			"OfcIssuanceNo",
			Integer.toString(aaData.getOfcIssuanceNo()));
		Vector lvCurrentRecord = new Vector();

		// defect 8247
		// Made qryRenewal return hash table with search keys as well as
		// a vector of results.  This is used when we are searching and
		// we return to the search results screen and need to re-search
		// using the same criteria.
		//lvCurrentRecord = qryRenewal(lhtHashTable);
		lvCurrentRecord =
			(Vector) qryItrntRenew(lhtHashTable).get("ReturnValues");

		// end defect 8247
		//		if (lvCurrentRecord.size() > 1)
		//		{
		//			InternetRegRecData laICurrentRegData =
		//				(InternetRegRecData) lvCurrentRecord.elementAt(0);
		//
		//		}

		lsSqlUpd = "UPDATE RTS.RTS_ITRNT_TRANS SET ";

		// since payment success/failure is for all recs with the same 
		// trace no, we can get just the first current record returned

		InternetRegRecData laICurrentRegData =
			(InternetRegRecData) lvCurrentRecord.elementAt(0);

		// only update to new if current status is unpaid

		if ((aaData.getPmntStatusCd()
			== CommonConstants.PAYMENT_CAPTURE_SUCCESS)
			|| (aaData.getPmntStatusCd()
				== CommonConstants.PAYMENT_CAPTURE_CONVFEE_FAILED))
		{
			if (laICurrentRegData
				.getStatus()
				.equals(
					DatabaseAccess.convertToString(
						CommonConstants.UNPAID)))
			{
				lsSqlUpd += "CNTYSTATUSCD=" + CommonConstants.NEW + ",";
				// defect 10996
				if (aaData.getCaptureTime() != null && !aaData.getCaptureTime().equals(""))
				{	
					lsSqlUpd += "ITRNTPYMNTTIMESTMP=" + "'" + aaData.getCaptureTime() + "'" + ",";
				}
				// end defect 10996
			}
		}

		lsSqlUpd += "ITRNTPYMNTSTATUSCD=? "
			+ "WHERE TRANSCD='IRENEW' AND "
			+ "ITRNTTRACENO LIKE '"
			+ lsTracenoLookup
		// defect 8895
		+"%'";
		// comment out the PYMNTORDERID part of the where clause
		// PaymentData.pymntorderid is sent to Epay null, but
		// has a value on rts_itrnt_trans
		//			+ "%' AND PYMNTORDERID=?";
		// end defect 8895		

		Vector lvValues = new Vector();
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					String.valueOf(aaData.getPmntStatusCd())));
			// defect 8895		
			//			lvValues.addElement(
			//				new DBValue(
			//					Types.VARCHAR,
			//					(String) aaData.getPmntOrderId()));
			// end defect 8895	

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsSqlUpd, lvValues);

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
		// end defect 6604    
	}

	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		PaymentData laPaymentData = new PaymentData();
		laPaymentData.setOrigTraceNo("220VR912FNTDRBZ");
		laPaymentData.setPmntOrderId("12345");
		laPaymentData.setPmntStatusCd(3);
		laPaymentData.setPmntAmt("52.80");
		laPaymentData.setConvFee("2.00");
		laPaymentData.setOfcIssuanceNo(220);
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();
			InternetTransaction loIT = new InternetTransaction(laDBA);
			loIT.updBatchPymntStatus(laPaymentData);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
}
