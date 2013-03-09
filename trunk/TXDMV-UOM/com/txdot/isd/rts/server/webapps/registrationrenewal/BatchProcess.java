package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.PaymentData;
import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetTransaction;
import com.txdot.isd.rts.server.webapps.util.Log;

/*
 *
 * BatchProcess.java
 *
 * (c) Department of Transportation  2001
 *----------------------------------------------------------------------
 * Change History
 * Name 		Date 		Description
 * ------------	-----------	-------------------------------------------- 
 * B Brown		07/22/2003	Remove email notification from
 * 							INET. Comment out the call to the sendEmail
 * 							method in processBatch().  Email
 * 							notification is being run from a Unix
 * 							crontab on PAS1 at 9:00PM each evening now.
 *                      	Also, Batch email notification was
 * 							periodically hanging (GroupWise interface
 * 							problem),causing the rest of server batch
 * 							(backups, etc) to not run.
 * 							defect 5823 
 * B Brown  	12/15/2003  Added code to method processFailedPayments()
 * 							to add logging info for payment records not
 * 							found on the Epay server. 
 *							defect 6746 Ver 5.1.5 Fix 2
 * B Brown		03/01/2004  modify declaration
 *							add   processFailedPayments(int)
 *							delete processFailedPayments()
 *							modify main(), processBatch()
 *							defect 6604 Ver 5.1.6.
 * S Johnston	02/24/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processFailedPayments(),
 * 							 processStatuses()
 *							defect 7889 Ver 5.2.3
 * K Harrell	08/31/2005	Continued Java 1.4 cleanup.	
 * 							added DB Rollback logic.
 * 							add PAYMENT_ERR1, PAYMENT_ERR2, REFUND_ERR
 * 							add writeErrorToBatchLog()
 * 							defect 7889 Ver 5.2.3 
 * K Harrell	08/31/2005	Corrected typo.
 * 							modify processStatuses()
 * 							defect 7835 Ver 5.2.3
 * K Harrell	10/05/2005	Movement of classes to server.db
 * 							defect 7889 Ver 5.2.3
 * B Brown 		03/01/2006  Added code so this classes main method will 
 * 							call processBatch() - the INET processes. 
 *      					The main method execution is initiated via 
 * 							crontab executing the inetbatch.sh file on 
 * 							PAS1. 
 *      					Changed main(String[])
 *      					defect 8539 Version 5.2.2 fix 8
 * B Brown 		06/15/2006  Blank out pymntorderid for failed payments  
 *      					and refunds.
 *     						modify processFailedPayments()
 *      					modify processFailedRefunds() 
 *						    defect 8735 Version 5.2.2 fix 8
 * Ray Rowehl	09/11/2006	Use the batch db userid and password when 
 * 							running in db2 restricted mode.
 * 							add csDbPassword, csDbUserId
 * 							add BatchProcess(String, String)
 * 							modify BatchProcess(), 
 * 								processFailedPayments(int),
 * 								processFailedRefunds(), 
 * 								processStatuses()
 * 							defect 8923 Ver 5.2.5
 * B Brown		09/27/2006  Move this process to the InetAP
 * 							project as an application client.
 * 							No code changes.
 *							defect 8958 Ver 5.2.6
 * B Brown		04/09/2010	Process a parameter for telling failed
 * 							payments and failed refunds to go to Epay
 * 							instead of TPE.
 * 							add cbEpayUsed, cbManuallyRun
 * 							modify main()
 * 							defect 10281 Ver 6.4  
 * B Brown		04/26/2010	Use the string "TPE" or "Epay" instead of 
 * 							true of false to direct 
 * 							payments and failed refunds to go to Epay
 * 							or TPE.
 * 							add csPaymentSystem
 * 							delete cbEpayUsed
 * 							modify main(), processFailedPayments(), 
 * 								processFailedRefunds()
 * 							defect 10281 Ver 6.4  \
 * B Brown		09/29/2010	Pulled failed payments out of this process.
 * 							modify main(), processbatch(), 
 * 							processFailedPayments()
 * 							defect 10605 Ver 6.6
 *----------------------------------------------------------------------
 */

/** 
 * This class is responsible for running the Phase II Server Batch
 * Process (INET)
 *
 * @version 6.6.0			09/29/2010
 * @author	J Giroux
 * <br>Creation Date: 		11/21/2001 16:48:56 
 */

public class BatchProcess
{
	public final static int PROCESS_FAILED_CONV_FEES = 1;
	public final static int PROCESS_FAILED_OTHER_FEES = 2;
	// end defect 6604

	private final static String PAYMENT_ERR1 =
		"Could not retrieve payment info for fee indicated by: ItrntPymntStatusCd = ";

	private final static String PAYMENT_ERR2 =
		"Due to the above exception, could not retrieve payment info for fee indicated by: ItrntPymntStatusCd = ";

	private final static String REFUND_ERR =
		"The above error was for refund request below:";

	// defect 8923
	private String csDbUserId;
	private String csDbPassword;
	// end defect 8923
	
	// defect 10281
//	private static boolean cbEpayUsed = false;
	private static boolean cbManuallyRun = false;
	private static String csPaymentSystem = "";
	// end defect 10281
	
	// defect 10605
	private static String csProcess = "";
	// end defect 10605

	/**
	 * BatchProcess constructor
	 */
	public BatchProcess()
	{
		this(
			SystemProperty.getDBUser(),
			SystemProperty.getDBPassword());
	}

	/**
	 * BatchProcess.java Constructor
	 * 
	 * @param asUserId String
	 * @param asPassword String
	 */
	public BatchProcess(String asUserId, String asPassword)
	{
		super();
		csDbUserId = asUserId;
		csDbPassword = asPassword;
	}
	
	/**
	 * main - used for testing purposes
	 *  
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		Comm.setIsServer(true);
		BatchProcess laBatchProc = new BatchProcess();
		// defect 10281
		if (aarrArgs.length > 0
			&& aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
		{
			BatchLog.write(
				" *** request to process :  "
					+ aarrArgs[0]);
			// end defect 10391
			// cbEpayUsed = Boolean.getBoolean(aarrArgs[0]);
			// defct 10605
			// csPaymentSystem = aarrArgs[0];
			csProcess = aarrArgs[0];
			// end defect 10605
			// cbManuallyRun = true;			
		}
		// end defect 10281
		// defect 8539
		System.out.println ("INET processes started "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
					
		try
		{
			boolean lbSuccessful = laBatchProc.processBatch();
			if (!lbSuccessful)
			{
				System.err.println("At least one refund or payment " + 
						  " was not successfully processed" +
						  " or update statuses failed " +
				   		 (new RTSDate()).getYYYYMMDDDate() + " " +
				   		 (new RTSDate()).get24HrTime());
			}
		}
		catch (Exception aeEx)
		{
			System.err.println(
				"BatchProcess "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
			aeEx.printStackTrace();
		}
		finally
		{
			System.out.println ("INET processes finished "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime());
		}
	}
	
	/**
	 * processBatch
	 * 
	 * @return boolean
	 */
	public boolean processBatch()
	{
		boolean lbBatchSuccessful = true;
		
		// defect 10605
		// if csProcess != "", then this process started from the 
		// RunProcessRunner.jsp and parms were passed from that jsp
		if (csProcess.equals("")
			|| csProcess.equals("refunds")
			|| csProcess.equals("both"))
		{
			if (!processFailedRefunds())
			{
				// there is >=1 failure in the refund.	
				lbBatchSuccessful = false;
			}
		}
		// comment out processing failed payments
		// process failed conv fees
//		if (!processFailedPayments(PROCESS_FAILED_CONV_FEES))
//		{
//			// there is >=1 failure in the payment capture	
//			lbBatchSuccessful = false;
//		}
//		
//		// process other payments
//		if (!processFailedPayments(PROCESS_FAILED_OTHER_FEES))
//		{
//			// there is >=1 failure in the payment capture	
//			lbBatchSuccessful = false;
//		}
		// end defect 10605
		
		// defect 10281 
		// don't process cntystatuscd updates when this process is run
		// manually: cbEpayUsed = true
		
		// defect 10605
		// if (!cbManuallyRun)
		if (csProcess.equals("")
			|| csProcess.equals("status")
			|| csProcess.equals("both"))
		{
		// end defect 10605	
			try
			{
				if (!processStatuses())
				{
					lbBatchSuccessful = false;
				}
			}
			catch (Exception aeEx)
			{
				com.txdot.isd.rts.server.webapps.util.BatchLog.error(aeEx);
				lbBatchSuccessful = false;
			}
		}
		// end defect 10281
		return lbBatchSuccessful;
	}
	
	/**
	 * processFailedPayments
	 * 
	 * @param aiPaymentProcess int
	 * @return boolean
	 */
	public boolean processFailedPayments(int aiPaymentProcess)
	{
		BatchLog.write("Starting Internet Process_Failed_Payments");
		boolean lbSuccessful = true;

		// get the list
		Vector lvPaymentList = new Vector();
		
		// defect 8923
		DatabaseAccess laDBA = new DatabaseAccess(csDbUserId, csDbPassword);
		// end defect 8923

		try
		{
			laDBA.beginTransaction();
			InternetTransaction laIntRegRenProc =
				new InternetTransaction(laDBA);

			// change from processFailedPayments()
			if (aiPaymentProcess == PROCESS_FAILED_CONV_FEES)
			{
				lvPaymentList =
					laIntRegRenProc.qryBatchConvFeePayments();
			}
			else
			{
				lvPaymentList = laIntRegRenProc.qryBatchPayments();
			}
		}
		catch (Exception aeEx)
		{
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(aeEx);
			lbSuccessful = false;
			return lbSuccessful;
		}
		finally
		{
			try
			{
				if (lbSuccessful)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
			catch (RTSException aeEx)
			{
				// empty code block
			}
		}
		// loop through the list and process each one.
		if (lvPaymentList != null && lvPaymentList.size() > 0)
		{
			BatchPayment laBatchPayment = new BatchPayment();
			PaymentData laPaymentData = null;
			for (int i = 0; i < lvPaymentList.size(); i++)
			{
				laPaymentData =
					(PaymentData) lvPaymentList.elementAt(i);
				
				// qryBatchConvFeePayments and qryBatchPayments are 
				// populating PmntOrderId, so we are unpopulataing here 
				// defect 10281
				// laPaymentData.setEpayUsed(cbEpayUsed);
				// defect 10605 - csPaymentSystem no longer used
				// the system now uses TPE as the default
				// laPaymentData.setPaymentSystem(csPaymentSystem);
				// end defect 10605
				// end defect 10281
				laPaymentData.setPmntOrderId(null); 
	
				try
				{
					if (!laBatchPayment.doPayment(laPaymentData))
						// one not good does not mean next one will not
						// be good. do not stop because of one error.
					{
						lbSuccessful = false;
  
						String lsMsg =
							PAYMENT_ERR1
								+ laPaymentData.getPmntStatusCd();

						writeErrorToBatchLog(lsMsg, laPaymentData);
					}
				}
				catch (Exception aeEx)
				{
					// change from processFailedPayments()
					aeEx.printStackTrace();

					com
						.txdot
						.isd
						.rts
						.server
						.webapps
						.util
						.BatchLog
						.error(
						aeEx);
					com
						.txdot
						.isd
						.rts
						.server
						.webapps
						.util
						.BatchLog
						.error(
						aeEx.toString());

					String lsMsg =
						PAYMENT_ERR2 + laPaymentData.getPmntStatusCd();

					writeErrorToBatchLog(lsMsg, laPaymentData);
					lbSuccessful = false;

				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"Due to the above exception, could not"
						+ " retrieve payment info for fee"
						+ " indicated by: itrntpymntstatuscd = "
						+ laPaymentData.getPmntStatusCd());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"Type=" + laPaymentData.getType());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"OrigTraceNo="
						+ laPaymentData.getOrigTraceNo());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"PmntOrderId="
						+ laPaymentData.getPmntOrderId());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"PmntAmt=" + laPaymentData.getPmntAmt());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"ConvFee=" + laPaymentData.getConvFee());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"OfcIssuanceNo="
						+ laPaymentData.getOfcIssuanceNo());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"EpayCID=" + laPaymentData.getEpayCID());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"BillName=" + laPaymentData.getBillName());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					"EpayAmt=" + laPaymentData.getEpayAmt());
				com
					.txdot
					.isd
					.rts
					.server
					.webapps
					.util
					.BatchLog
					.error(
					" ");
				}
			}
		}
		
		if (lbSuccessful)
		{
			BatchLog.write(
				"Internet Process_Failed_Payments Succeeded");
		}
		else
		{
			// there is >=1 failure in the payment capture	
			BatchLog.write("Internet Process_Failed_Payments Failed");
		}
		return lbSuccessful;
	}
	
	/**
	 * Write to the Batch Log.
	 * 
	 * @param aaText
	 * @param aaObject
	 */
	private void writeErrorToBatchLog(String aaText, Object aaObject)
	{
		com.txdot.isd.rts.server.webapps.util.BatchLog.error(aaText);

		if (aaObject instanceof PaymentData)
		{
			PaymentData laPaymentData = (PaymentData) aaObject;

			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"Type=" + laPaymentData.getType());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"OrigTraceNo=" + laPaymentData.getOrigTraceNo());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"PmntOrderId=" + laPaymentData.getPmntOrderId());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"PmntAmt=" + laPaymentData.getPmntAmt());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"ConvFee=" + laPaymentData.getConvFee());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"OfcIssuanceNo=" + laPaymentData.getOfcIssuanceNo());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"EpayCID=" + laPaymentData.getEpayCID());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"BillName=" + laPaymentData.getBillName());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"EpayAmt=" + laPaymentData.getEpayAmt());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(" ");
		}
		else if (aaObject instanceof RefundData)
		{
			RefundData laRefundData = (RefundData) aaObject;
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"Original Trace No = " + laRefundData.getOrigTraceNo());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"Conv fee = " + laRefundData.getConvFee());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"Payment Order ID = " + laRefundData.getPymtOrderId());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"Refund Amount = " + laRefundData.getRefAmt());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(
				"Refund Status = " + laRefundData.getRefundStatus());
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(" ");
		}
	}

	/**
	 * processFailedRefunds
	 * 
	 * @return boolean
	 */
	public boolean processFailedRefunds()
	{
		BatchLog.write("Starting Internet Process_Failed_Refunds");
		boolean lbSuccessful = true;
		Vector lvRefundList = null;
		Refund laRefund = new Refund();

		// improving logging
		RefundData laRefundData = null;

		// defect 8923
		DatabaseAccess laDBA = new DatabaseAccess(csDbUserId, csDbPassword);
		// end defect 8923
		
		try
		{
			laDBA.beginTransaction();
			InternetTransaction laIRRP = new InternetTransaction(laDBA);
			lvRefundList = laIRRP.qryBatchRefunds();
		}
		catch (RTSException aeEx)
		{
			com.txdot.isd.rts.server.webapps.util.BatchLog.error(aeEx);

			// cannot retrieve the list, error
			lbSuccessful = false;
		}
		finally
		{
			try
			{
				if (lbSuccessful)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
			catch (RTSException ex)
			{
				// empty code block
			}
		}
		
		// Do the refund after retrieving the list
		if (lvRefundList != null && lvRefundList.size() > 0)
		{
			for (int i = 0; i < lvRefundList.size(); i++)
			{
				try
				{
					// Improved logging
					laRefundData =
						(RefundData) lvRefundList.elementAt(i);
					
					// qryBatchRefunds is populating PymtOrderId, so we 
					// are unpopulating here
					// defect 10281
					// laRefundData.setEpayUsed(cbEpayUsed);
					// defect 10605 - csPaymentSystem no longer used
					// the system now uses TPE as the default
					// laRefundData.setPaymentSystem(csPaymentSystem);
					// end defect 10605
					// end defect 10281
					laRefundData.setPymtOrderId(null);
			
					laRefund.doRefund(laRefundData);
					Log.info("Sending refund request");
					Log.info(
						"Original Trace No = "
							+ laRefundData.getOrigTraceNo());
					Log.info("Conv fee = " + laRefundData.getConvFee());
					Log.info(
						"Payment Order ID = "
							+ laRefundData.getPymtOrderId());
					Log.info(
						"Refund Amount = " + laRefundData.getRefAmt());
					Log.info(
						"Refund Status = "
							+ laRefundData.getRefundStatus());
					Log.info(" ");
				}
				catch (Exception aeEx)
				{
					aeEx.printStackTrace();
					com
						.txdot
						.isd
						.rts
						.server
						.webapps
						.util
						.BatchLog
						.error(
						aeEx);
					com
						.txdot
						.isd
						.rts
						.server
						.webapps
						.util
						.BatchLog
						.error(
						aeEx.toString());

					writeErrorToBatchLog(REFUND_ERR, laRefundData);
					lbSuccessful = false;
				}
			}
		}
		if (lbSuccessful)
		{
			BatchLog.write("Internet Process_Failed_Refunds Succeeded");
		}
		else
		{
			BatchLog.write("Internet Process_Failed_Refunds Failed");
		}
		return lbSuccessful;
	}

	/**
	 * processStatuses
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean processStatuses() throws Exception
	{
		BatchLog.write("Starting Internet Process Statuses");
		
		// defect 8923
		DatabaseAccess laDBA = new DatabaseAccess(csDbUserId, csDbPassword);
		// end defect 8923
		
		boolean lbSuccessful = false;

		try
		{
			laDBA.beginTransaction();
			InternetTransaction laIRRP = new InternetTransaction(laDBA);
			updateHoldToNew(laIRRP);
			updateCheckedOutToNew(laIRRP);
			lbSuccessful = true;
		}
		catch (RTSException aeEx)
		{
			throw aeEx;
		}
		finally
		{
			try
			{
				if (lbSuccessful)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					
					// Corrected spelling of Statuses from "Statues"
					BatchLog.write(
						"Internet Process Statuses Succeeded"); 	
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
					BatchLog.write("Internet Process Statuses Failed");
				}
			}
			catch (RTSException aeEx)
			{
				// empty code block
			}
		}
		return lbSuccessful;
	}
	
	/**
	 * sendEmail
	 * 
	 * @return boolean
	 */
	public boolean sendEmail()
	{
		BatchLog.write("Starting Internet Email Batch");
		boolean lbSuccessful = true;
		BatchEmailNotification laBatchEmail =
			new BatchEmailNotification();
		lbSuccessful = laBatchEmail.sendMail();

		if (lbSuccessful)
		{
			BatchLog.write("Internet Email Batch Succeeded");
		}
		else
		{
			BatchLog.write("Internet Email Batch Failed");
		}
		return lbSuccessful;
	}
	
	/**
	 * Updates the County Status Code from IN_PROCESS to NEW
	 * 
	 * @param aaIRRP InternetTransaction
	 * @throws RTSException
	 * @throws Exception
	 */
	private void updateCheckedOutToNew(InternetTransaction aaIRRP)
		throws RTSException, Exception
	{
		aaIRRP.updateStatusToNew(CommonConstants.IN_PROCESS);
	}
	
	/**
	 * Updates the County Status Code from HOLD to NEW
	 * 
	 * @param aaIRRP InternetTransaction
	 * @throws RTSException
	 * @throws Exception
	 */
	private void updateHoldToNew(InternetTransaction aaIRRP)
		throws RTSException, Exception
	{
		aaIRRP.updateStatusToNew(CommonConstants.HOLD);
	}
}