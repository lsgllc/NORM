package com.txdot.isd.rts.server.webapps.registrationrenewal;

import com.txdot.isd.rts.services.data.PaymentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.webapps.communication.Comm;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegistrationRenewalConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetTransaction;
import com.txdot.isd.rts.server.webapps.util.BatchLog;
import com.txdot.isd.rts.server.webapps.util.Log;

/*
 * BatchPayment.java
 *
 * (c) Texas Department of Transportation  2001
 *
 *----------------------------------------------------------------------
 * Change History 
 * Name     	Date        Description
 * ------------	-----------	--------------------------------------------
 * Clifford		03/10/2003	Connection control (through proxy or VPN) by
 *                          a property value, dealing with a potential.
 * B Brown  	12/15/2003  Added code to method doPayment to add
 * 							logging info for payment records not found
 * 							on the Epay server.   
 *                      	defect 6746 
 * B Brown		03/01/2004  Modified method sendPayment for logging
 *                          improvements for payments.
 *					   	 	defect 6604 Ver 5.1.6
 * S Johnston	08/24/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify doPayment, processPaymentResult
 *							defect 7889 Ver 5.2.3
 * K Harrell	10/05/2005	InternetTransaction rename & movement 
 * 							to server.db; DataAccess Cleanup
 * 							modify processPaymentResult()	
 * 							defect 7889 Ver 5.2.3 
 * B Brown		05/15/2006	Convert the 5.2.3 PaymentData object
 * 							to the 5.2.2 TxO PaymentData object.
 * 							This change assumes that RTS on the
 * 							TxDOT side is going 5.2.3 before the 
 * 							TxO server side.
 * 							This code relies on an rtswebapps 
 * 							property file change, which will tell
 * 							the code to either convert PaymentData
 * 							or not.
 * 							add com.txdot.isd.rts.client.webapps.data.
 * 							PaymentData
 * 							modify sendPayment()
 * 							defect 8777 Ver 5.2.3 
 * B Brown		07/27/2006	Look at the properties file value of 
 * 							getRefundPaymentUseProxy() to see if 
 *							the batch payment process will use proxy
 *							or not.
 *							modify BatchPayment()
 *							defect 8368 Ver 5.2.4
 * B Brown		01/08/2009  Calculate original conv fee trace number by
 * 							removing the credit card byte trace number
 * 							from what's stored on the DB first, then 
 * 							doing the conv fee trace number calculation
 * 							from that, then adding back in the credit
 * 							card byte, before sending to Epay for failed
 * 							conv fee payments.
 * 							modify doConvFeePayment()
 * 							defect 9878 Ver Defect_POS_C
 * B Brown		04/09/2010  Send payment to test Epay/TPE even when
 * 							running in desktop mode.
 * 							modify sendPayment()
 * 							defect 10281 Ver Defect_POS_640
 * B Brown		10/04/2010  Remove commented out 5.2.3 referenced code
 * 							modify sendPayment()
 * 							defect 10605 Ver Defect_POS_660 
 * B Brown		10/27/2011	Add code for returning the regis fee capture
 * 							date/time.
 * 							modify doPayment(),doRegFeePayment(),
 * 							sendPayment(), doConvFeePayment()
 * 							defect 10996 Ver POS_690
 *----------------------------------------------------------------------
 */
/**
 * This class contains methods to process Batch Payments
 * 
 * @version POS_690 		10/27/2011   
 * @author 	J Giroux
 * <br>Creation Date:		02/05/2002 17:06:07 
 */
public class BatchPayment
{
	private boolean cbUseProxy = true;
	/**
	 * BatchPayment constructor comment.
	 */
	public BatchPayment()
	{
		super();
		// defect 8368
		// cbUseProxy = CommunicationProperty.getRefundInfoProxyOn();
		cbUseProxy = CommunicationProperty.getRefundPaymentUseProxy();
		// end defect 8368
	}
	/**
	 * doConvFeePayment
	 * 
	 * @param aaPymntData PaymentData
	 * @return boolean
	 * @throws Exception
	 */
	private String doConvFeePayment(PaymentData aaPymntData)
		throws Exception
	{
		aaPymntData.setType(PaymentData.CONV_FEE);
		aaPymntData.setEpayAmt(aaPymntData.getConvFee());
		// Original trace number for Convenience Fee is not
		// stored, need to be calculated from the Registration
		// Renewal Fee trace number.
		// defect 9878
//		String lsConvFeeTraceNo =
//			UtilityMethods.getConvFeeTraceNo(
//				aaPymntData.getOrigTraceNo());
		String lsOrigTraceNo = aaPymntData.getOrigTraceNo();
		String lsConvFeeTraceWithoutCCType =
			lsOrigTraceNo.substring(0, lsOrigTraceNo.length() - 1);
		String lsCCType =
			lsOrigTraceNo.substring(
				lsOrigTraceNo.length() - 1,
				lsOrigTraceNo.length());
		String lsConvFeeTraceNo =
			UtilityMethods.getConvFeeTraceNo(
				lsConvFeeTraceWithoutCCType)
				+ lsCCType;
		// end defect 9878		
		aaPymntData.setOrigTraceNo(lsConvFeeTraceNo);
		return sendPayment(aaPymntData);
	}
	/**
	 * doPayment
	 * 
	 * @param aaPymntData PaymentData
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doPayment(PaymentData aaPymntData) throws Exception
	{
		int liOrigPymntStatusCd = aaPymntData.getPmntStatusCd();
		// defect 10996
		String lsCaptureTime = "";
		// end defect 10996
		// Set Epay Amount to the Transaction Payment Amount or
		// the Conv Fee Amount based on what failed
		if (liOrigPymntStatusCd
			== CommonConstants.PAYMENT_CAPTURE_RENEWAL_FAILED)
		{
			// Only Registration Renewal Fee capture is needed
			// defect 10996
//			if (doRegFeePayment(aaPymntData))
//			{
//				aaPymntData.setPmntStatusCd(
//					CommonConstants.PAYMENT_CAPTURE_SUCCESS);
//				return processPaymentResult(aaPymntData);
//			}
//			else
//			{
//				return false;
//			}
			lsCaptureTime = doRegFeePayment(aaPymntData);
			if (lsCaptureTime==null || lsCaptureTime.equals(""))
			{
				BatchLog.error("Could not retrieve payment info for");
				BatchLog.error("Type=" + aaPymntData.getType());
				BatchLog.error(
					"OrigTraceNo=" + aaPymntData.getOrigTraceNo());
				BatchLog.error(
					"PmntOrderId=" + aaPymntData.getPmntOrderId());
				BatchLog.error(
					"PmntStatusCd=" + aaPymntData.getPmntStatusCd());
				BatchLog.error("PmntAmt=" + aaPymntData.getPmntAmt());
				BatchLog.error("ConvFee=" + aaPymntData.getConvFee());
				BatchLog.error(
					"OfcIssuanceNo=" + aaPymntData.getOfcIssuanceNo());
				BatchLog.error("EpayCID=" + aaPymntData.getEpayCID());
				BatchLog.error("BillName=" + aaPymntData.getBillName());
				BatchLog.error("EpayAmt=" + aaPymntData.getEpayAmt());
				return false;
			}
			else
			{
				aaPymntData.setPmntStatusCd(
					CommonConstants.PAYMENT_CAPTURE_SUCCESS);
				aaPymntData.setCaptureTime(lsCaptureTime);
				return processPaymentResult(aaPymntData);
			}
			// end defect 10996
		}
		else if (
			liOrigPymntStatusCd
				== CommonConstants.PAYMENT_CAPTURE_CONVFEE_FAILED)
		{
			// Only Convenience Fee capture is needed
			// defect 10996
//			if (doConvFeePayment(aaPymntData))
//			{
//				aaPymntData.setPmntStatusCd(
//					CommonConstants.PAYMENT_CAPTURE_SUCCESS);
//				return processPaymentResult(aaPymntData);
//			}
			lsCaptureTime = doConvFeePayment(aaPymntData);
			if (lsCaptureTime==null || lsCaptureTime.equals(""))
			{
				BatchLog.error("Could not retrieve payment info for");
				BatchLog.error("Type=" + aaPymntData.getType());
				BatchLog.error(
					"OrigTraceNo=" + aaPymntData.getOrigTraceNo());
				BatchLog.error(
					"PmntOrderId=" + aaPymntData.getPmntOrderId());
				BatchLog.error(
					"PmntStatusCd=" + aaPymntData.getPmntStatusCd());
				BatchLog.error("PmntAmt=" + aaPymntData.getPmntAmt());
				BatchLog.error("ConvFee=" + aaPymntData.getConvFee());
				BatchLog.error(
					"OfcIssuanceNo=" + aaPymntData.getOfcIssuanceNo());
				BatchLog.error("EpayCID=" + aaPymntData.getEpayCID());
				BatchLog.error("BillName=" + aaPymntData.getBillName());
				BatchLog.error("EpayAmt=" + aaPymntData.getEpayAmt());
				return false;
			}
			else
			{
				aaPymntData.setPmntStatusCd(
					CommonConstants.PAYMENT_CAPTURE_SUCCESS);
				// don't update capture time for conv fee capture
				// aaPymntData.setCaptureTime(lsCaptureTime);
				return processPaymentResult(aaPymntData);
			}
			// end defect 7889
		}
		else if (
			liOrigPymntStatusCd
				== CommonConstants.PAYMENT_CAPTURE_BOTH_FAILED)
		{
			// Both Registration Renewal Fee and Convenience Fee 
			// captures are needed
			PaymentData laConvFee = new PaymentData(aaPymntData);
			// initial status	
			int liResult = CommonConstants.PAYMENT_CAPTURE_BOTH_FAILED;
			// (1) Send to Epay for Conv Fee capture
			// defect 10996
			// if (doConvFeePayment(laConvFee))
			lsCaptureTime = doConvFeePayment(laConvFee);
			if (lsCaptureTime!=null && !lsCaptureTime.equals(""))
			// end defect 10996	
			{
				// if the ConFee goes through, we have only Renewal fee
				// uncaptured.
				liResult =
					CommonConstants.PAYMENT_CAPTURE_RENEWAL_FAILED;
			}
			// else
			// Conv Fee capture not OK, still both Failed.
			// (2) Send to Epay for Reg Ren Fee capture	
			// defect 10996
			// if (doRegFeePayment(laConvFee))
			lsCaptureTime = doRegFeePayment(aaPymntData);
			if (lsCaptureTime!=null && !lsCaptureTime.equals(""))
			// end defect 10996	
			{
				// Reg Ren capture is good, see what we have for ConvFee
				if (liResult
					== CommonConstants.PAYMENT_CAPTURE_RENEWAL_FAILED)
				{
					// Conv Fee capture is OK, renewal fee also captured
					// everything is good.
					liResult = CommonConstants.PAYMENT_CAPTURE_SUCCESS;
				}
				else
				{
					// Conv Fee capture not OK, Reg Ren Fee capture OK.
					liResult =
						CommonConstants.PAYMENT_CAPTURE_CONVFEE_FAILED;
				}
			}
			aaPymntData.setPmntStatusCd(liResult);
			// defect 10996
			aaPymntData.setCaptureTime(lsCaptureTime);
			// end defect 10996

			if (liResult
				!= CommonConstants.PAYMENT_CAPTURE_BOTH_FAILED)
			{
				// have something different than the original,
				// update it to the database.
				return processPaymentResult(aaPymntData);
			}
			else
			{
				return false;
			}
		}
		else
		{
			// no need to do anything
			return true;
		}
	}
	/**
	 * doRegFeePayment
	 * 
	 * @param aaPymntData PaymentData
	 * @return boolean
	 * @throws Exception
	 */
	private String doRegFeePayment(PaymentData aaPymntData)
		throws Exception
	{
		aaPymntData.setType(PaymentData.REG_FEE);
		aaPymntData.setEpayAmt(aaPymntData.getPmntAmt());
		return sendPayment(aaPymntData);
	}
	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		PaymentData laPaymentData = new PaymentData();
		laPaymentData.setOrigTraceNo("220VRABCD00004");
		laPaymentData.setPmntOrderId("12345");
		laPaymentData.setPmntStatusCd(3);
		laPaymentData.setPmntAmt("52.80");
		laPaymentData.setConvFee("2.00");
		laPaymentData.setOfcIssuanceNo(220);
		BatchPayment laBatchPayment = new BatchPayment();
		try
		{
			laBatchPayment.doPayment(laPaymentData);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
	/**
	 * Process Payment Result
	 * 
	 * @param aaPaymentData PaymentData
	 * @return boolean
	 */
	public boolean processPaymentResult(PaymentData aaPaymentData)
	{
		int liPaymentStatusCd = aaPaymentData.getPmntStatusCd();

		// If not valid status code, do not process 
		if (!(liPaymentStatusCd
			== CommonConstants.PAYMENT_CAPTURE_SUCCESS
			|| liPaymentStatusCd
				== CommonConstants.PAYMENT_CAPTURE_RENEWAL_FAILED
			|| liPaymentStatusCd
				== CommonConstants.PAYMENT_CAPTURE_CONVFEE_FAILED
			|| liPaymentStatusCd
				== CommonConstants.PAYMENT_CAPTURE_BOTH_FAILED))
		{
			return true;
		}

		// defect 7889 
		// Reorganize database access 
		DatabaseAccess laDBA = new DatabaseAccess();
		boolean lbSuccess = false;
		try
		{
			laDBA.beginTransaction();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBA);
			// includes updating the itrntpymntstatuscd and potentially
			// the cntystatuscd if all captures done.
			laItrntTrans.updBatchPymntStatus(aaPaymentData);
			lbSuccess = true;
		}
		catch (RTSException aeEx)
		{
			BatchLog.error(aeEx);
			// Note that epay has the response, but cannot update db.		
			String lsPymntInfo =
				"The PaymentData with the ItrntTraceNo="
					+ aaPaymentData.getOrigTraceNo()
					+ " PymntOrderId="
					+ aaPaymentData.getPmntOrderId()
					+ " ItrntPymntStatusCd="
					+ aaPaymentData.getPmntStatusCd()
					+ " could not be updated";
			BatchLog.error(lsPymntInfo);
		}
		finally
		{
			try
			{
				if (lbSuccess)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
			catch (RTSException leEx)
			{
			}
			return lbSuccess;
		}
		// end defect 7889 
	}
	/**
	 * sendPayment
	 * 
	 * @param aaPmntData PaymentData 
	 * @return boolean
	 * @throws Exception 
	 */
	private String sendPayment(PaymentData aaPmntData) throws Exception
	{
		// defect 10996
		// Object laPaymentResult = null;
		String lsPaymentResult = null;
		// end defect 10996

		// defect 6604
		// Add logging for additional feedback on payment requests
		Log.info(
			"Sending payment request for fee indicated by:"
				+ " itrntpymntstatuscd = "
				+ aaPmntData.getPmntStatusCd());
		Log.info("Type=" + aaPmntData.getType());
		Log.info("OrigTraceNo=" + aaPmntData.getOrigTraceNo());
		Log.info("PmntOrderId=" + aaPmntData.getPmntOrderId());
		Log.info("PmntAmt=" + aaPmntData.getPmntAmt());
		Log.info("ConvFee=" + aaPmntData.getConvFee());
		Log.info("OfcIssuanceNo=" + aaPmntData.getOfcIssuanceNo());
		Log.info("EpayCID=" + aaPmntData.getEpayCID());
		Log.info("BillName=" + aaPmntData.getBillName());
		Log.info("EpayAmt=" + aaPmntData.getEpayAmt());
		Log.info(" ");
		// end defect 6604

		if (CommunicationProperty.getRegion().equalsIgnoreCase("test")
			|| CommunicationProperty.getRegion().equalsIgnoreCase("prod"))
		{
		 //Going to the real TexasOnline site.
			if (cbUseProxy)
			{
				Log.info("BatchPayment, sendPayment, use proxy");
				Comm.setUseProxy(true);
				Comm.setProxyInfo(
					CommunicationProperty.getProxyHost(),
					CommunicationProperty.getProxyPort() + "",
					CommunicationProperty.getProxyUser(),
					CommunicationProperty.getProxyPassword());
			}
			else
			{
				Log.info("BatchPayment, sendPayment, not use proxy");
			}

			try
			{
				// defect 10996
				// laPaymentResult =
				// Comm.sendToServer( 
				lsPaymentResult =
					(String)Comm.sendToServer(
				// end defect 10996	
						CommonConstants.REGISTRATION_RENEWAL,
						RegistrationRenewalConstants.DO_BATCH_PAYMENT,
						aaPmntData,
						CommunicationProperty.CONNECT_TO_TEXASONLINE);
				Log.info("BatchPayment, sendPayment, end");

			}
			catch (Exception aeEx)
			{
				// other kinds of exception
				throw aeEx;
			}
		}
		else
		{
			// desktop, dev, ...
			// Simulate success on TxDOT site.
			//laPaymentResult = "true";
			// ***************************************
			// Send to TexasOnline site (not real TexasOnline site)
			// defect 10281 - uncomment for desktop testing
			try
			{	
				// defect 10996
				// laPaymentResult =
				// Comm.sendToServer( 
				lsPaymentResult =
					(String)Comm.sendToServer(
				// end defect 10996	
						CommonConstants.REGISTRATION_RENEWAL,
						RegistrationRenewalConstants.DO_BATCH_PAYMENT,
						aaPmntData,
						CommunicationProperty.CONNECT_TO_TEXASONLINE);
				Log.info("BatchPayment, sendPayment, end");
			}
			catch(Exception e)
			{
				// other kinds of exception
					throw e;
			}
			// end defect 10281
			// **************************************
		}
		// defect 10996
		// return Boolean.valueOf((String) laPaymentResult).booleanValue();
		return lsPaymentResult;
		// end defect 10996
	}
	/**
	 * 
	 * setUseProxy
	 * 
	 * @param abUseProxy boolean
	 */
	public void setUseProxy(boolean abUseProxy)
	{
		cbUseProxy = abUseProxy;
	}
}
