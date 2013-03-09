package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.PaymentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetTransaction;
import com.txdot.isd.rts.server.webapps.util.Log;

/*
 * FailedPaymentProcess.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		09/29/2010	Add the failed Payment processing portion
 * 							of INET to this new class
 * 							defect 10605 Ver 6.6   
 * ---------------------------------------------------------------------
 */

/**
 * This class handles collecting previously failed internet renewal
 * payments
 *
 * @version	POS 6.6			09/24/2010
 * @author	RLBROWN
 * <br>Creation Date:		09/24/2010 HH:MM:SS
 */
public class FailedPaymentProcess 
{
	public final static int PROCESS_FAILED_CONV_FEES = 1;
	public final static int PROCESS_FAILED_OTHER_FEES = 2;
	private final static String PAYMENT_ERR1 =
		"Could not retrieve payment info for fee indicated by: " +		"ItrntPymntStatusCd = ";
	private final static String PAYMENT_ERR2 =
		"Due to the above exception, could not retrieve payment info" +		" for fee indicated by: ItrntPymntStatusCd = ";
	private String csDbUserId;
	private String csDbPassword;
	private static boolean cbManuallyRun = false;
	private static String csPaymentSystem = "";

	/**
	 * BatchProcess constructor
	 */
	public FailedPaymentProcess()
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
	public FailedPaymentProcess(String asUserId, String asPassword)
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
		FailedPaymentProcess laFailedPaymentprocess =
			new FailedPaymentProcess();
		System.out.println ("FailedPaymentProcess processes started "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
					
		try
		{
			boolean lbSuccessful =
				laFailedPaymentprocess.processFailedPayments();
			if (!lbSuccessful)
			{
				System.err.println("At least one payment " + 
						  " was not successfully processed" +
						 (new RTSDate()).getYYYYMMDDDate() + " " +
						 (new RTSDate()).get24HrTime());
			}
		}
		catch (Exception aeEx)
		{
			System.err.println(
				"FailedPaymentProcess "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
			aeEx.printStackTrace();
		}
		finally
		{
			System.out.println ("FailedPaymentProcess processes finished "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime());
		}
	}
	
	/**
	 * processFailedPayments
	 * 
	 * @return boolean
	 */
	public boolean processFailedPayments()
	{
		boolean lbBatchSuccessful = true;

		// process failed conv fees
		if (!processFailedPayments(PROCESS_FAILED_CONV_FEES))
		{
			// there is >=1 failure in the payment capture	
			lbBatchSuccessful = false;
		}
		
		// process other payments
		if (!processFailedPayments(PROCESS_FAILED_OTHER_FEES))
		{
			// there is >=1 failure in the payment capture	
			lbBatchSuccessful = false;
		}
		
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
		DatabaseAccess laDBA = new DatabaseAccess(csDbUserId, csDbPassword);

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
	}
}
