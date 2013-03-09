package com.txdot.isd.rts.server.systemcontrolbatch;

import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.*;

/*
 * SendTrans.java 
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/22/2002	Fixed bug to stop sending empty vectors of 
 *							Trans_Payment to MF 
 *							defect 3606
 * MAbs			04/29/2002  Fixed bug in loop to send one Trans_Payment
 *							at a time to MF 
 *							defect 3531
 * K.Harrell 	02/19/2003 	Include Commits for Improved Units of Work; 
 *							Separate TransPayment from Transaction;
 *							defect 5520
 * K.Harrell 	02/27/2003 	Use Tracelevel 5 vs. 10 for SQL errors.
 *							defect 5613
 * K.Harrell 	03/05/2003 	SendTrans should not run during Batch
 *							defect 5373
 * K Harrell 	05/07/2003 	SendTrans should reset substaid for Special 
 *							Plates run() method
 *							defect 6084
 * K Harrell 	06/04/2003 	SendTrans enhancements
 *                     		- Send Trans, Trans Payment, LogFuncTrans 
 *							  in one call
 *                     		- Send to MFAccess in correct order
 *                     		- Conditional Wait
 *                     		- Write to log 2000 transactions
 *                     		- MF wait to 15 seconds
 *                     		- SendTrans to run as independent method 
 *                     		add    main()
 *                     		modify run() 
 *							defect 6227
 * K Harrell 	07/20/2003 	ciTotalDailyTrans not reset 
 *							modify run()
 *							defect 6331
 * Ray Rowehl	05/17/2004	Add processing to use R99 Transid when 
 *							requested.
 *							modify constructor(), main(), run()
 *							add cbR99Switch
 *							defect 6785 Ver 5.2.0
 * K Harrell	11/18/2004	Write to System.out when SendTrans Sleeps for
 *							Batch and again when Wakes Up after Batch
 *							modify run() 
 *							defect 7741  Ver 5.2.2
 * Ray Rowehl	02/19/2005	Change MFAccess.SuccessFul
 * 							modify run()
 * 							defect 7937 Ver 5.2.3
 * K Harrell	08/16/2005	Incorrect comparison for dates.	Modified to
 *							use int vs. RTSDate.  Added logging for total
 *							daily count.  
 *							add  ciCurrentDate, ciPriorDate, caPrintDate,
 *							SPECIAL_PLATES_OFCISSUANCE_NO, 
 *							EXEMPT_REGCLASSCD 
 *							delete currentDate, priorDate
 *							modify run() 
 *							defect 6882  Ver 5.2.2 Fix 4
 * Jeff S.		10/10/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3
 * B Hargrove 	09/28/2006  Change check for Exempt RegClassCd (39)
 * 							to check for FeeCalcCat = 4 (No Regis Fees).
 * 							modify isSpecialRegistration()
 * 							delete EXEMPT_REGCLASSCD
 * 							defect 8900  Ver 5.3.0 
 * Ray Rowehl	09/28/2006	Use the SendTrans UserId and Password when 
 * 							connecting to the database.
 * 							modify run()
 * 							defect 8933 Ver FallAdminTables
 * Ray Rowehl	10/03/2006	Migrate back into main project.
 * 							defect 8959 Ver FallAdminTables
 * Ray Rowehl	10/04/2006	Add a sleep management method to handle when 
 * 							running batch.  No point in sleeping only 15 
 * 							seconds when batch will run for awhile.
 * 							add IC_BATCH_WINDOW_BEGIN, IC_BATCH_WINDOW_END
 * 							add sleepForMfProblem()
 * 							modify run()
 * 							defect 8959 Ver FallAdminTables
 * Ray Rowehl	10/17/2006	Correct picking up R99 boolean.
 * 							modify main()
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	01/25/2007	Remove Cache lookup of CommonFees.
 * 							Cache is not loaded in SendTrans.
 * 							Also restructured use of DatabaseAccess to
 * 							have the instantiation inside the try
 * 							block.  This ensures catching and handling 
 * 							of any exceptions from DatabaseAccess.
 * 							Organize inmports to remove cache.
 * 							add ERROR_MESSAGE_DBA_NULL
 * 							modify run()
 * 							defect 9090 Ver Exempts
 * J Rue		02/28/2007	Add SR_FUNC_TRANS	
 * 							modify run()
 * 							defect 9086 Ver Special Plates
 * K Harrell	09/15/2008	Use SystemProperty to determine reporting 
 * 							frequency. 
 * 							modify run()
 * 							defect 6440 Ver Defect_POS_B
 * K Harrell	06/17/2010	add processing for RTS_PRMT_TRANS 
 * 							modify run()
 * 							defect 10492 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * SendTrans: Selects RTS_TRANS, RTS_TRANS_PAYMENT, 
 * 			  RTS_LOG_FUNC_TRANSACTON records. 
 *            Calls MFAccess.sendTransaction to send to MF
 *            Sleeps 15 seconds on MF error (not in batch window)
 * 			  Sleeps 15 minutes on MF error (in batch window (4-7))
 *            Sleeps 10 seconds if no records to send to MF 
 *            Logs to System.out
 *               - every n transactions (where n determined by 
 *                  SystemProperty.getSendTransRptFreq()-default 2000)
 *               - on error
 *
 * @version	Defect_POS_B	06/17/2010
 * @author	Michael Abernethy
 * <br>Creation Date:		09/06/2001 09:34:32
 */

public class SendTrans
	extends
		com
		.txdot
		.isd
		.rts
		.client
		.systemcontrolbatch
		.business
		.ThreadMessenger
	implements Runnable
{
	// boolean 
	private volatile boolean cbKeepRunning;

	// defect 6785
	private boolean cbR99Switch = false;
	// end defect 6785

	// int
	private int ciStartOfc;
	private int ciEndOfc;
	private int ciTotalDailyTrans = 0;
	// defect 6882 
	private int ciCurrentDate = 0;
	private int ciPriorDate = 0;
	// defect 6331
	//private RTSDate currentDate = RTSDate.getCurrentDate();
	//private RTSDate priorDate = currentDate;
	// end defect 6331
	// end defect 6882 

	// Object 
	private RTSDate caPrintDate;

	// Vector
	private Vector cvLogFuncData = new Vector();
	private Vector cvTransData = new Vector();
	private Vector cvTransPayData = new Vector();

	// Constant
	// defect 6882 
	private final static int SPECIAL_PLATES_OFCISSUANCE_NO = 291;
	// defect 8900
	//private final static int EXEMPT_REGCLASSCD = 39;
	// end 8900
	// end defect 6882 
	private final static int DEFAULT_FIRST_OFCISSUANCE_NO = 1;
	private final static int DEFAULT_LAST_OFCISSUANCE_NO = 300;
	private final static int EXEMPT_SUBSTA_ID = 12;
	private final static int MILLISECONDS_PER_SEC = 1000;

	// defect 8959
	private static final int IC_BATCH_WINDOW_END = 7;
	private static final int IC_BATCH_WINDOW_BEGIN = 4;
	// end defect 8959

	// Sleep 10 seconds when no transactions 
	private final static long SLEEP_ALL_POSTED =
		MILLISECONDS_PER_SEC * 10 * 1;
	// Sleep 15 seconds on MF error       
	private final static long SLEEP_MF_ERROR =
		MILLISECONDS_PER_SEC * 15 * 1;

	// defect 8959
	// define amount of time to sleep on MF error if in the "batch window"
	private final static long LC_SLEEP_MF_ERROR_BATCH_WINDOW =
		MILLISECONDS_PER_SEC * 60 * 15;
	// end defect 8959

	// defect 9090
	// define new message.
	private static final String ERROR_MESSAGE_DBA_NULL = "DBA was null";
	// end defect 9090

	private final static String ERROR_MESSAGE =
		"There was in error in SendTrans ";
	private static final String ARRGS_ERROR =
		"To run specific office issuance numbers in Send Trans, "
			+ "use    SendTrans <start OFC> <end OFC>";
	private static final String TRUE = "true";
	private static final String SND_TRANS = " SendTrans: ";
	private static final String TOTAL_RPOC_ON =
		" total transactions processed on ";
	private static final String TOTAL_RPOC = " transactions processed.";
	private static final String SND_TRANS_ERROR_TRANS =
		" SendTrans Error: RTS_TRANS ";
	private static final String SND_TRANS_ERROR_PAYMENT =
		" SendTrans Error: RTS_TRANS_PAYMENT ";
	private static final String SND_TRANS_ERROR_FUNC_TRANS =
		" SendTrans Error: RTS_LOG_FUNC_TRANS ";
	private static final String SND_TRANS_SLEEP =
		" SendTrans Sleeping For Batch";
	private static final String SND_TRANS_WAKING =
		" SendTrans Waking After Batch";

	/**
	 * SendTrans constructor comment.
	 * 
	 * @param aiStartOfc int
	 * @param aiEndOfc int
	 */
	public SendTrans(int aiStartOfc, int aiEndOfc)
	{
		// defect 6785
		// pass false as last parameter
		this(aiStartOfc, aiEndOfc, false);
		// end defect 6785
	}

	/**
	 * SendTrans constructor comment.
	 * 
	 * @param aiStartOfc int
	 * @param aiEndOfc int
	 * @param abR99Switch boolean
	 */
	public SendTrans(int aiStartOfc, int aiEndOfc, boolean abR99Switch)
	{
		super();
		ciStartOfc = aiStartOfc;
		ciEndOfc = aiEndOfc;
		cbR99Switch = abR99Switch;
	}

	/**
	 * Sets the switch to signal that termination is requested.
	 */
	public void kill()
	{
		cbKeepRunning = false;
	}

	/**
	 * This is the starting point when running from server.
	 * <p>Three parameters:
	 * <ol>
	 * <li>Start Office - int
	 * <li>End Office - int
	 * <li>R99 Trans Switch - boolean
	 * <eol>
	 * These parameters determine which offices to to run SendTrans
	 * for.  The default is 1, 300, false.
	 * 
	 * @param aarrArgs String[]
	 * @throws RTSException 
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		SendTrans laSendTrans = null;
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		// defect 6785
		if (aarrArgs.length < 2 || aarrArgs.length > 3)
		{
			System.out.println(ARRGS_ERROR);
			laSendTrans =
				new SendTrans(
					DEFAULT_FIRST_OFCISSUANCE_NO,
					DEFAULT_LAST_OFCISSUANCE_NO,
					false);
		}
		else
		{
			// bring in the values
			String lsArg1 = aarrArgs[0];
			String lsArg2 = aarrArgs[1];

			// check to see if the boolean is there and use it.
			boolean lbArg3 = false;
			if (aarrArgs.length == 3)
			{
				if (((String) aarrArgs[2])
					.trim()
					.equalsIgnoreCase(TRUE))
				{
					lbArg3 = true;
				}
			}
			laSendTrans =
				new SendTrans(
					Integer.parseInt(lsArg1),
					Integer.parseInt(lsArg2),
					lbArg3);
		}
		// end defect 6785
		Thread laThread = new Thread(laSendTrans);
		laThread.start();
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is 
	 * used to create a thread, starting the thread causes the object's 
	 * <code>run</code> method to be called in that separately executing 
	 * thread. 
	 * <p>
	 * The general contract of the method <code>run</code> is that it
	 * may take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		cbKeepRunning = true;
		boolean lbHasSlept = true;
		int liLoopNumTrans = 0;

		// defect sendtrans testing
		System.out.println("Starting the run method");

		System.out.println(
			"The datasource is " + SystemProperty.getDatasource());
		// end defect sendtrans testing

		// defect 6440 
		System.out.println(
			"The reporting frequency is "
				+ SystemProperty.getSendTransRptFreq());
		// end defect 6440 

		while (cbKeepRunning)
		{
			// defect 6882
			// Use int vs. RTSDate for comparison
			// Add logging for total daily count
			// defect 6331 	
			ciCurrentDate = RTSDate.getCurrentDate().getAMDate();

			if (ciCurrentDate != ciPriorDate)
			{
				if (ciPriorDate != 0)
				{
					caPrintDate = RTSDate.getCurrentDate();
					System.out.println(
						caPrintDate
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ caPrintDate.getClockTime()
							+ SND_TRANS
							+ UtilityMethods.addPadding(
								Integer.toString(ciTotalDailyTrans),
								6,
								CommonConstant.STR_SPACE_ONE)
							+ TOTAL_RPOC_ON
							+ new RTSDate(RTSDate.AMDATE, ciPriorDate));
				}
				// Reset for new date
				ciPriorDate = ciCurrentDate;
				ciTotalDailyTrans = 0;
			}
			// end defect 6331
			// end defect 6882

			// create the DatabaseAccess to use through queries and 
			// updates

			// defect 9090
			// instantiate the dba within the try block.
			// defect 8933
			DatabaseAccess laDBAccess = null;
			//	new DatabaseAccess(
			//		SystemProperty.getDBUserSendTrans(),
			//		SystemProperty.getDBPasswordSendTrans());
			// end defect 8933
			// end defect 9090

			try
			{
				// defect 9090
				// instantiate dba within the try block so exceptions 
				// can be caught and logged.
				laDBAccess =
					new DatabaseAccess(
						SystemProperty.getDBUserSendTrans(),
						SystemProperty.getDBPasswordSendTrans());
				// end defect 9090

				// Query Unposted Transactions Records
				TransactionData laSaveTransData = new TransactionData();
				Transaction laTransaction = new Transaction(laDBAccess);

				// UOW #1 - RTS_TRANS
				laDBAccess.beginTransaction();
				cvTransData =
					laTransaction.qryTransaction(ciStartOfc, ciEndOfc);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// END UOW #1

				// Query Unposted Transaction Payment Records
				TransactionPayment laTransPayment =
					new TransactionPayment(laDBAccess);

				// UOW #2 - RTS_TRANS_PAYMENT
				laDBAccess.beginTransaction();
				cvTransPayData =
					laTransPayment.qryTransactionPayment(
						ciStartOfc,
						ciEndOfc);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// END UOW #2 
				//
				// Query Unposted Log Func Records
				LogonFunctionTransaction laLogFuncTrans =
					new LogonFunctionTransaction(laDBAccess);

				// UOW #3 - RTS_LOG_FUNC_TRANS 	
				laDBAccess.beginTransaction();
				cvLogFuncData =
					laLogFuncTrans.qryLogonFunctionTransaction(
						ciStartOfc,
						ciEndOfc);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// END UOW #3 
				//
				// Loop through all the TransactionData objects in the 
				// vector and get all the other associated transaction 
				// objects and put them in a vector
				liLoopNumTrans =
					cvTransData.size()
						+ cvLogFuncData.size()
						+ cvTransPayData.size();

				while (cvTransData.size() > 0
					|| cvLogFuncData.size() > 0
					|| cvTransPayData.size() > 0)
				{
					// Create the vector to put all the transaction 
					// objects
					Vector lvTransToMF = new Vector();
					TransactionData laTransData = new TransactionData();
					TransactionPaymentData laTransPaymentData =
						new TransactionPaymentData();
					LogonFunctionTransactionData laLogFuncData =
						new LogonFunctionTransactionData();

					boolean lbTrans = false;
					boolean lbTransPay = false;
					boolean lbLogFunc = false;

					// Process Vehicle, SR, Inventory, Funds Transactions
					if (cvTransData.size() > 0)
					{
						lbTrans = true;
						laTransData =
							(TransactionData) cvTransData.get(0);
						/**********************************************/
						// get records from 
						// MotorVehicleFunctionTransaction
						MotorVehicleFunctionTransaction laMVFuncTrans =
							new MotorVehicleFunctionTransaction(laDBAccess);
						MotorVehicleFunctionTransactionData laMVFuncTransData =
							new MotorVehicleFunctionTransactionData();
						// Build MV_FUNC_TRANS key
						// Build SR_FUNC_TRANS key
						laMVFuncTransData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laMVFuncTransData.setSubstaId(
							laTransData.getSubstaId());
						laMVFuncTransData.setTransAMDate(
							laTransData.getTransAMDate());
						laMVFuncTransData.setTransWsId(
							laTransData.getTransWsId());
						laMVFuncTransData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laMVFuncTransData.setTransTime(
							laTransData.getTransTime());

						// UOW #4 - RTS_MV_FUNC_TRANS 
						laDBAccess.beginTransaction();
						Vector lvMVFuncTransDataToMF =
							laMVFuncTrans
								.qryMotorVehicleFunctionTransaction(
								laMVFuncTransData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #4 

						/**********************************************/
						// defect 9086
						//	Add SR_FUNC_TRANS
						// get records from 
						// SpecialRegistrationFunctionTransaction
						SpecialRegistrationFunctionTransaction laSRFuncTrans =
							new SpecialRegistrationFunctionTransaction(laDBAccess);
						SpecialRegistrationFunctionTransactionData laSRFuncTransData =
							new SpecialRegistrationFunctionTransactionData();
						// Build SR_FUNC_TRANS key
						laSRFuncTransData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laSRFuncTransData.setSubstaId(
							laTransData.getSubstaId());
						laSRFuncTransData.setTransAMDate(
							laTransData.getTransAMDate());
						laSRFuncTransData.setTransWsId(
							laTransData.getTransWsId());
						laSRFuncTransData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laSRFuncTransData.setTransTime(
							laTransData.getTransTime());

						// UOW #5 - RTS_SR_FUNC_TRANS 
						laDBAccess.beginTransaction();
						Vector lvSRFuncTransDataToMF =
							laSRFuncTrans
								.qrySpecialRegistrationFunctionTransaction(
								laSRFuncTransData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #5 

						/**********************************************/
						// get records from TransactionFundsDetail
						// Build TR_FND_DETAIL key
						TransactionFundsDetail laTransFdsDetail =
							new TransactionFundsDetail(laDBAccess);
						TransactionFundsDetailData laTransFdsDetailData =
							new TransactionFundsDetailData();
						// Build SR_FUNC_TRANS key
						laTransFdsDetailData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laTransFdsDetailData.setSubstaId(
							laTransData.getSubstaId());
						laTransFdsDetailData.setTransAMDate(
							laTransData.getTransAMDate());
						laTransFdsDetailData.setTransWsId(
							laTransData.getTransWsId());
						laTransFdsDetailData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laTransFdsDetailData.setTransTime(
							laTransData.getTransTime());

						// UOW #6 - RTS_TR_FDS_DETAIL 
						laDBAccess.beginTransaction();
						Vector lvTransFdsDetailDataToMF =
							laTransFdsDetail.qryTransactionFundsDetail(
								laTransFdsDetailData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #6 

						/**********************************************/
						// get records from TransactionInventoryDetail
						TransactionInventoryDetail laTransInvDetail =
							new TransactionInventoryDetail(laDBAccess);
						TransactionInventoryDetailData laTransInvDetailData =
							new TransactionInventoryDetailData();
						// Build TR_INV_DETAIL key
						laTransInvDetailData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laTransInvDetailData.setSubstaId(
							laTransData.getSubstaId());
						laTransInvDetailData.setTransAMDate(
							laTransData.getTransAMDate());
						laTransInvDetailData.setTransWsId(
							laTransData.getTransWsId());
						laTransInvDetailData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laTransInvDetailData.setTransTime(
							laTransData.getTransTime());
						//
						// UOW #7 - RTS_TR_INV_DETAIL 
						laDBAccess.beginTransaction();
						Vector lvTransInvDetailDataToMF =
							laTransInvDetail
								.qryTransactionInventoryDetail(
								laTransInvDetailData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #7 

						/**********************************************/
						// get records from FundFunctionTransaction
						FundFunctionTransaction laFundFuncTrans =
							new FundFunctionTransaction(laDBAccess);
						FundFunctionTransactionData laFundFuncTransData =
							new FundFunctionTransactionData();
						// Build FUND_FUNC_TRNS key
						laFundFuncTransData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laFundFuncTransData.setSubstaId(
							laTransData.getSubstaId());
						laFundFuncTransData.setTransAMDate(
							laTransData.getTransAMDate());
						laFundFuncTransData.setTransWsId(
							laTransData.getTransWsId());
						laFundFuncTransData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laFundFuncTransData.setTransTime(
							laTransData.getTransTime());

						// UOW #8 - RTS_FUND_FUNC_TRANS 
						laDBAccess.beginTransaction();
						Vector lvFundFuncTransDataToMF =
							laFundFuncTrans.qryFundFunctionTransaction(
								laFundFuncTransData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #8

						/**********************************************/
						// get records from InventoryFunctionTransaction
						InventoryFunctionTransaction laInvFuncTrans =
							new InventoryFunctionTransaction(laDBAccess);
						InventoryFunctionTransactionData laInvFuncTransData =
							new InventoryFunctionTransactionData();
						// Build INV_FUNC_TRANS key
						laInvFuncTransData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laInvFuncTransData.setSubstaId(
							laTransData.getSubstaId());
						laInvFuncTransData.setTransAMDate(
							laTransData.getTransAMDate());
						laInvFuncTransData.setTransWsId(
							laTransData.getTransWsId());
						laInvFuncTransData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laInvFuncTransData.setTransTime(
							laTransData.getTransTime());
						//
						// UOW #9 - RTS_INV_FUNC_TRANS 
						laDBAccess.beginTransaction();
						Vector lvInvFuncTransToMF =
							laInvFuncTrans
								.qryInventoryFunctionTransaction(
								laInvFuncTransData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #9 

						// defect 10492 
						PermitTransactionData laPrmtTransData =
							new PermitTransactionData();
							
						// Build PRMT_TRANS key
						laPrmtTransData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laPrmtTransData.setSubstaId(
							laTransData.getSubstaId());
						laPrmtTransData.setTransAMDate(
							laTransData.getTransAMDate());
						laPrmtTransData.setTransWsId(
							laTransData.getTransWsId());
						laPrmtTransData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laPrmtTransData.setTransTime(
							laTransData.getTransTime());

						// UOW #10 - RTS_PRMT_TRANS 							
						PermitTransaction laPrmtTrans =
							new PermitTransaction(laDBAccess);
						laDBAccess.beginTransaction();
						Vector lvPrmtTransToMF =
							laPrmtTrans.qryPermitTransaction(
								laPrmtTransData);
						laDBAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// END UOW #10 
						// end defect 10492 

						/**********************************************/
						// defect 6084
						//    Create laSaveTransData for updating DB 
						//    upon MF Posting
						//    Alter laTransData, laMVFuncTransData if 
						//    required
						//    Finally add all vectors 
						// Build SR_FUNC_TRANS key
						laSaveTransData.setOfcIssuanceNo(
							laTransData.getOfcIssuanceNo());
						laSaveTransData.setSubstaId(
							laTransData.getSubstaId());
						laSaveTransData.setTransAMDate(
							laTransData.getTransAMDate());
						laSaveTransData.setTransWsId(
							laTransData.getTransWsId());
						laSaveTransData.setCustSeqNo(
							laTransData.getCustSeqNo());
						laSaveTransData.setTransTime(
							laTransData.getTransTime());
						if (lvMVFuncTransDataToMF != null
							&& lvMVFuncTransDataToMF.size() != 0)
						{
							laMVFuncTransData =
								(
									MotorVehicleFunctionTransactionData) lvMVFuncTransDataToMF
										.elementAt(
									0);

							// defect 9090
							// remove the common fees lookup
							// defect 8900
							// Change check for Exempt RegClassCd to 
							// FeeCalcCat = NOREGISFEES
							//int liRegClassCd = laMVFuncTransData.
							//	getRegClassCd();
							//int liEffDate = new RTSDate().
							//	getYYYYMMDDDate();

							//CommonFeesData laCommonFeesData =
							//	CommonFeesCache.getCommonFee(
							//	liRegClassCd,
							//	liEffDate);

							// remove the common fees reference
							if (laMVFuncTransData.getOfcIssuanceNo()
								== SPECIAL_PLATES_OFCISSUANCE_NO
								&& laMVFuncTransData.getExmptIndi() == 0
								&& (laMVFuncTransData
									.getTransCd()
									.equals(TransCdConstant.TITLE)
									|| laMVFuncTransData
										.getTransCd()
										.equals(
										TransCdConstant.REJCOR)
									|| laMVFuncTransData
										.getTransCd()
										.equals(
										TransCdConstant.NONTTL)
									|| laMVFuncTransData
										.getTransCd()
										.equals(
										TransCdConstant.CORTTL)))
								// end 8900
							{
								// end defect 9090
								laTransData.setSubstaId(
									EXEMPT_SUBSTA_ID);
								laMVFuncTransData.setSubstaId(
									EXEMPT_SUBSTA_ID);
							}
						}
						// end defect 6084  
						//  Transactions must be in the following order
						//   ****   IMPORTANT IMPORTANT IMPORTANT ****
						//          TRANS MVFUNC SRFUNCTRANS INVFUNC  
						//          FUNDFUNC TRINVDTL TRFDSDTL TRANPYMNT 
						//			LOGFUNC
						//   ****   IMPORTANT IMPORTANT IMPORTANT ****
						lvTransToMF.add(laTransData);
						lvTransToMF.addAll(lvMVFuncTransDataToMF);
						// defect 9086
						// Add SRFUNCTRANS
						lvTransToMF.addAll(lvSRFuncTransDataToMF);
						// end defect 9086
						lvTransToMF.addAll(lvInvFuncTransToMF);
						lvTransToMF.addAll(lvFundFuncTransDataToMF);
						lvTransToMF.addAll(lvTransInvDetailDataToMF);
						lvTransToMF.addAll(lvTransFdsDetailDataToMF);
						
						// defect 10492 
						lvTransToMF.addAll(lvPrmtTransToMF);
						// end defect 10492
						  
						cvTransData.remove(0);
					}

					// Process Payment data
					if (cvTransPayData.size() > 0)
					{
						lbTransPay = true;
						laTransPaymentData =
							(
								TransactionPaymentData) cvTransPayData
									.get(
								0);
						lvTransToMF.add(laTransPaymentData);
						cvTransPayData.remove(0);
					}

					// Process Log Func Trans
					if (cvLogFuncData.size() > 0)
					{
						lbLogFunc = true;
						laLogFuncData =
							(
								LogonFunctionTransactionData) cvLogFuncData
									.get(
								0);
						lvTransToMF.add(laLogFuncData);
						cvLogFuncData.remove(0);
					}
					// Send Transaction and its related objects to MF
					ciTotalDailyTrans = ciTotalDailyTrans + 1;

					// defect 6440 
					// Use SystemProperty to customize for different 
					// environments 
					//if ((ciTotalDailyTrans % 2000) == 0)
					if ((ciTotalDailyTrans
						% SystemProperty.getSendTransRptFreq())
						== 0)
					{
						// end defect 6440 
						caPrintDate = RTSDate.getCurrentDate();
						System.out.println(
							caPrintDate
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_DASH
								+ CommonConstant.STR_SPACE_ONE
								+ caPrintDate.getClockTime()
								+ SND_TRANS
								+ UtilityMethods.addPadding(
									Integer.toString(ciTotalDailyTrans),
									6,
									CommonConstant.STR_SPACE_ONE)
								+ TOTAL_RPOC);
					}
					MFTrans laMFTrans = new MFTrans();
					laMFTrans.setTransObjects(lvTransToMF);
					com
						.txdot
						.isd
						.rts
						.server
						.dataaccess
						.MfAccess laMfAccess =
						new com
							.txdot
							.isd
							.rts
							.server
							.dataaccess
							.MfAccess();
					// defect 6785
					// set the trans type to 99 for testing
					if (cbR99Switch)
					{
						MfAccess.R77 = MfAccess.R99;
					}
					// end defect 6785
					int liResponse =
						laMfAccess.sendTransaction(laMFTrans);
					// if liResponse from MF was that the record was 
					// successfully posted update DB so MFIndi = 1
					if (liResponse == MfAccess.TRANSACTION_SUCCESSFUL)
					{
						// update Transaction with new TransactionData 
						// where MFIndi = 1
						// defect 6084 
						// Use laSaveTransData to update 
						// TransPostedMfIndi 
						if (lbTrans)
						{
							// UOW #10 - RTS_TRANS
							laDBAccess.beginTransaction();
							laTransaction.postTransaction(
								laSaveTransData);
							laDBAccess.endTransaction(
								DatabaseAccess.COMMIT);
							// END UOW #10 
						}
						if (lbTransPay)
						{
							// UOW #11 - RTS_TRANS_PAYMENT
							laDBAccess.beginTransaction();
							laTransPayment.postTransactionPayment(
								laTransPaymentData);
							laDBAccess.endTransaction(
								DatabaseAccess.COMMIT);
							// END UOW #11
						}
						if (lbLogFunc)
						{
							// UOW #12 - RTS_LOG_FUNC_TRANS 
							laDBAccess.beginTransaction();
							laLogFuncTrans
								.postLogonFunctionTransaction(
								laLogFuncData);
							laDBAccess.endTransaction(
								DatabaseAccess.COMMIT);
							// END UOW #12
						}
						// Successful Trans Posting
					}

					// Log to System.out on MF Posting error
					//   (already log to VSAM in 
					// 				MFAccess.sendTransaction)  
					else
					{
						caPrintDate = RTSDate.getCurrentDate();
						if (lbTrans)
						{
							String[] larrStringPad =
								{
									Integer.toString(
										laTransData.getOfcIssuanceNo()),
									Integer.toString(
										laTransData.getTransWsId()),
									Integer.toString(
										laTransData.getTransAMDate()),
									Integer.toString(
										laTransData.getTransTime())};

							int[] larrIntPad = { 3, 3, 5, 6 };

							String lsTransId =
								UtilityMethods.addPadding(
									larrStringPad,
									larrIntPad,
									CommonConstant.STR_ZERO);

							System.out.println(
								caPrintDate
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_DASH
									+ CommonConstant.STR_SPACE_ONE
									+ caPrintDate.getClockTime()
									+ SND_TRANS_ERROR_TRANS
									+ lsTransId);

						} // (lbTrans)
						if (lbTransPay)
						{
							String[] larrStringPad =
								{
									Integer.toString(
										laTransPaymentData
											.getOfcIssuanceNo()),
									Integer.toString(
										laTransPaymentData
											.getTransWsId()),
									Integer.toString(
										laTransPaymentData
											.getTransAMDate()),
									Integer.toString(
										laTransPaymentData
											.getCustSeqNo()),
									laTransPaymentData.getPymntNo()};

							int[] larrIntPad = { 3, 3, 5, 3, 2 };

							String lsTransPayInfo =
								UtilityMethods.addPadding(
									larrStringPad,
									larrIntPad,
									CommonConstant.STR_ZERO);

							System.out.println(
								caPrintDate
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_DASH
									+ CommonConstant.STR_SPACE_ONE
									+ caPrintDate.getClockTime()
									+ SND_TRANS_ERROR_PAYMENT
									+ lsTransPayInfo);
						} // (lbTransPay)

						if (lbLogFunc)
						{
							String[] larrStringPad =
								{
									Integer.toString(
										laLogFuncData
											.getOfcIssuanceNo()),
									Integer.toString(
										laLogFuncData.getWsId()),
									Integer.toString(
										laLogFuncData.getSysDate()),
									Integer.toString(
										laLogFuncData.getSysTime())};

							int[] larrIntPad = { 3, 3, 8, 6 };

							String lsLogFuncInfo =
								UtilityMethods.addPadding(
									larrStringPad,
									larrIntPad,
									CommonConstant.STR_ZERO)
									+ laLogFuncData.getEmpId();

							System.out.println(
								caPrintDate
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_DASH
									+ CommonConstant.STR_SPACE_ONE
									+ caPrintDate.getClockTime()
									+ SND_TRANS_ERROR_FUNC_TRANS
									+ lsLogFuncInfo);
						} // (lbLogFunc)
					} // End else                     
				} // while( cvTransData.size() > 0 || 
				//			cvLogFuncData.size() > 0 || 
				//				cvTransPayData.size() > 0))

				// defect 5373
				// Do not run SendTrans during Batch 
				// sleep until it's time to check for more unposted 
				// transactions or batch running
				if (liLoopNumTrans < 100)
				{
					lbHasSlept = false;
				}
				// defect 7741
				// Write to log when Sleep For Batch 
				boolean lbSleepForBatch = false;
				while (Scheduler.BATCH_RUNNING || !lbHasSlept)
				{
					if (Scheduler.BATCH_RUNNING && !lbSleepForBatch)
					{
						caPrintDate = RTSDate.getCurrentDate();
						lbSleepForBatch = true;
						System.out.println(
							caPrintDate
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_DASH
								+ CommonConstant.STR_SPACE_ONE
								+ caPrintDate.getClockTime()
								+ SND_TRANS_SLEEP);
					}
					Thread.sleep(SLEEP_ALL_POSTED);
					lbHasSlept = true;
				}
				if (lbSleepForBatch)
				{
					lbSleepForBatch = false;
					caPrintDate = RTSDate.getCurrentDate();
					System.out.println(
						caPrintDate
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ caPrintDate.getClockTime()
							+ SND_TRANS_WAKING);
				}
				// end defect 7741 
			} // end try

			// highly unlikely this exception will get thrown
			catch (InterruptedException aeIntEx)
			{
				System.err.println(
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ aeIntEx.getMessage());
				Log.write(
					Log.START_END,
					this,
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ aeIntEx.getMessage());
			}
			// this exception will get thrown when the DB or MF is down, 
			// in which case SendTrans will sleep and then try again
			catch (RTSException aeRTSEx)
			{
				try
				{
					// defect 9090
					// Only do a rollback if dba has been instantiated.
					if (laDBAccess != null)
					{
						laDBAccess.endTransaction(
							DatabaseAccess.ROLLBACK);
					}
					else
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							ERROR_MESSAGE_DBA_NULL);
					}
					// end defect 9090

					Log.write(
						Log.SQL_EXCP,
						this,
						ERROR_MESSAGE
							+ CommonConstant.SYSTEM_LINE_SEPARATOR
							+ aeRTSEx.getMessage());
				}
				catch (RTSException aeRTSEx1)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						ERROR_MESSAGE
							+ CommonConstant.SYSTEM_LINE_SEPARATOR
							+ aeRTSEx.getMessage());
				}
				finally
				{
					// defect 8959
					sleepForMfProblem();
					// end defect 8959
					continue;
				}
			}
		} // end while loop
	}

	/**
	 * This method sleeps on RTSException.
	 * It is expected that these exceptions are due to MainFrame or 
	 * Database exceptions.
	 * The length of time to sleep depends on whether or not Batch 
	 * might be running.  
	 * The Batch Window is roughly between 4 AM and 7 AM.
	 * 
	 * <p>
	 * <ul>
	 * <li>Non-Batch is a 15 second sleep.
	 * <li>Batch is a 15 minute sleep.
	 * <eul>
	 */
	private void sleepForMfProblem()
	{
		try
		{
			RTSDate laToday = RTSDate.getCurrentDate();
			if (laToday.getHour() >= IC_BATCH_WINDOW_BEGIN
				&& laToday.getHour() <= IC_BATCH_WINDOW_END)
			{
				Thread.sleep(LC_SLEEP_MF_ERROR_BATCH_WINDOW);
			}
			else
			{
				Thread.sleep(SLEEP_MF_ERROR);
			}
		}
		catch (InterruptedException aeIntEx)
		{
			System.err.println(
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeIntEx.getMessage());
			Log.write(
				Log.SQL_EXCP,
				this,
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeIntEx.getMessage());
		}
	}
}