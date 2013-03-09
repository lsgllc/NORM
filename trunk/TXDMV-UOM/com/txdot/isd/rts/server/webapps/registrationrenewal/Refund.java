package com.txdot.isd.rts.server.webapps.registrationrenewal;

import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.data.VehicleBaseData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.webapps.communication.Comm;
import com.txdot.isd.rts.services.webapps.exception.RTSWebAppsException;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.util.WebAppsMQAccess;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegistrationRenewalConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetTransaction;
import com.txdot.isd.rts.server.webapps.util.Log;
import com.txdot.isd.rts.server.webapps.util.MQLog;

/*
 *
 * Refund.java 
 *
 * (c) Texas Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown      03/11/2003  Modify login,sendMail
 *                          Include better logging:
 *                          i.e. catch (Throwable t)
 *                          BatchLog.error(t);
 *							defect 5768
 * B Brown      03/27/2003  Modify login, sendMail
 *							to recapture the catch
 *                          exception while leaving the catch throwable.
 *							defect 5768 
 * B Brown      12/13/2004  add graph variables USER_CONTEXT,
 *							USER_CONTEXT_START,USER_CONTEXT_LENGTH
 * 							modify establishConnection,
 *							modify USER_CONTEXT_LENGTH 
 *                          to 18 characters for the most 
 *                          recent upgrade of Groupwise webmail.
 *							Groupwise admins also had to turn off 
 *							cookies for WebAccess (TXDOT-HQ66) utilized
 *                          by this RTS webmail interface.
 *                          defect 7698 Ver 5.2.2.
 * S Johnston	02/28/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processRefundResult
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3
 * K Harrell	10/05/2005	Java 1.4 cleanup, continued
 * 							defect 7889 Ver 5.2.3  
 * B Brown		05/15/2006	Convert the 5.2.3 RefundData object
 * 							to the 5.2.2 TxO RefundData object.
 * 							This change assumes that RTS on the
 * 							TxDOT side is going 5.2.3 before the 
 * 							TxO server side.
 * 							This code relies on an rtswebapps 
 * 							property file change, which will tell
 * 							the code to either convert RefundData
 * 							or not.
 * 							add com.txdot.isd.rts.client.webapps.data.
 * 							RefundData
 * 							modify doRealRefund()
 * 							defect 8777 Ver 5.2.3
 * B Brown		07/27/2006	Look at the properties file value of 
 * 							getRefundPaymentUseProxy() to see if 
 *							the batch refund process will use proxy
 *							or not.
 *							modify Refund()
 *							defect 8368 Ver 5.2.4
 * B Brown		07/30/2007	Comment out the check for TxO version 
 *							modify doRealRefund()
 *							defect 9119 Ver Special Plates
 * B Brown		01/26/2009	Distinguish between a refund coming from the
 *  						POS app, and one coming from the failed 
 * 							refund Batch process.
 * 							modify processRefundResult()
 * 							defect 8501 Ver Defect_POS_D
 * B Brown		04/02/2009	Now that we are not sending plate no in 
 * 							refund requests (sending trace no. now)
 * 							we need to only execute the method that does
 * 							not use plate number for the update.
 * 							defect 10016 Ver Defect_POS_E
 * B Brown		07/15/2009	Undo the 10016 change. The POS client still 
 * 							refunds one plate at a time, so we need to
 * 							call the update for one plate at a time
 * 							when the POS client initiated the refund, 
 * 							and stay with refunding the remainder of
 * 							the shopping cart for INET refunds. 
 * 							modify processRefundResult()
 * 							defect 10083 Ver Defect_POS_F		
 * ---------------------------------------------------------------------
 */
 
/** 
 * This class contains methods for a refund
 *
 * @version Defect_POS_F	07/15/2009
 * @author: J Giroux
 * <br>Creation Date: 		10/19/2001 10:37:43
 */
public class Refund
{
	private boolean cbUseProxy = true;
	private boolean cbSimAtTxdot = true;
	/**
	 * Refund constructor
	 */
	public Refund()
	{
		super();
		// defect 8368
		// cbUseProxy = CommunicationProperty.getRefundInfoProxyOn();
		cbUseProxy = CommunicationProperty.getRefundPaymentUseProxy();
		// end defect 8368
	}
	/**
	 * doRealRefund
	 * 
	 * @param aaRefundData RefundData
	 * @throws RTSException
	 */
	protected void doRealRefund(RefundData aaRefundData)
		throws RTSException
	{
		if (CommunicationProperty.getUseMQ())
		{
			// Code for using MQ Series
			Log.info("Refund, doRealRefund, use MQ");
			try
			{
				WebAppsMQAccess laMQAccess =
					new WebAppsMQAccess(
						CommunicationProperty.getMQTxdotHost(),
						CommunicationProperty.getMQTxdotChannel(),
						CommunicationProperty.getMQTxdotQueueManager(),
						CommunicationProperty.getMQTxdotPort());
				laMQAccess.postMessage(
					CommunicationProperty.getMQTxdotPostQueue(),
					aaRefundData);
			}
			catch (Exception aeEx)
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeEx);
			}
		}
		else
		{
			Log.info("Refund, doRealRefund, use Connection");
			// =========================================================
			// use standard communication, directly do a post to
			// TexasOnline. For security, VPN from TxDOT app servers to
			// TexasOnline Web servers
			try
			{	
				if (cbUseProxy)
				{
					Log.info("Refund, doRealRefund, use proxy");
					Comm.setUseProxy(true);
					Comm.setProxyInfo(
						CommunicationProperty.getProxyHost(),
						CommunicationProperty.getProxyPort() + "",
						CommunicationProperty.getProxyUser(),
						CommunicationProperty.getProxyPassword());
				}
				else
				{
					Log.info("Refund, doRealRefund, not use proxy");
				}

				// defect 8777
				Object laRefundResult = null;
				
				// defect 9119
				// comment out old code for converting refund objects
//				if (CommunicationProperty.getTxOVersion() == null ||
//					CommunicationProperty.getTxOVersion().equals("5.2.2"))
//				{
//					// instantiate the 5.2.2 RefundData class that TxO
//					// expects
//					com.txdot.isd.rts.client.webapps.data.RefundData 
//						laTxORefundData = 
//						new com.txdot.isd.rts.client.webapps.data.RefundData(aaRefundData);
//					
//					laRefundResult =
//						Comm.sendToServer(
//							CommonConstants.REGISTRATION_RENEWAL,
//							RegistrationRenewalConstants.DO_REFUND,
//							laTxORefundData,
//							CommunicationProperty.CONNECT_TO_TEXASONLINE);
//							
//					com.txdot.isd.rts.client.webapps.data.RefundData laProcessedRefundData =
//						(com.txdot.isd.rts.client.webapps.data.RefundData) laRefundResult;
//						
//					Log.info(
//						"Refund, doRealRefund, finished refund ("
//							+ laProcessedRefundData
//								.getVehBaseData()
//								.getPlateNo()
//							+ ","
//							+ laProcessedRefundData.getRefundStatus()
//							+ ")");				
//				}
//				else
//				{
				// end defect 9119
					laRefundResult =
						Comm.sendToServer(
							CommonConstants.REGISTRATION_RENEWAL,
							RegistrationRenewalConstants.DO_REFUND,
							aaRefundData,
							CommunicationProperty.CONNECT_TO_TEXASONLINE);
							
					RefundData laProcessedRefundData =
						(RefundData) laRefundResult;
					Log.info(
						"Refund, doRealRefund, finished refund ("
							+ laProcessedRefundData
								.getVehBaseData()
								.getPlateNo()
							+ ","
							+ laProcessedRefundData.getRefundStatus()
							+ ")");			
				// }
				// end defect 8777			
						
				// ignore the result
				// the return object is returned to MQSeries				
				// processRefundResult(processedRefundData);
			}
			catch (RTSWebAppsException aeRTSEx)
			{
				Log.error(aeRTSEx);
				throw new RTSException(
					RTSException.JAVA_ERROR,
					aeRTSEx);
			}
			catch (Exception aeEx)
			{
				Log.error(aeEx);
				// other kinds of exception
				throw new RTSException(RTSException.JAVA_ERROR, aeEx);
			}
			// =========================================================
		}
	}
	/**
	 * doRefund
	 * 
	 * @param aaRefundData RefundData 
	 * @throws RTSException
	 */
	public void doRefund(RefundData aaRefundData) throws RTSException
	{

//		if (CommunicationProperty.getRegion().equalsIgnoreCase("test")
//			|| CommunicationProperty.getRegion().equalsIgnoreCase("prod"))
//		{
//			// test or prod, go for real one, send to TexasOnline
			Log.info(
				"Refund, doRefund, test/prod, go to do real refund");
			doRealRefund(aaRefundData);
//		}
//		else // desktop, dev, ...
//			{
//			// simulate the refund
//			Log.info(
//				"Refund, doRefund, desktop/dev..., simulate refund");
//			simulateRefund(aaRefundData);
//		}
	}
	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		//////////////////////////////////////////////
		////////// For testing only  /////////////////
		//////////////////////////////////////////////
		VehicleBaseData laVBD = new VehicleBaseData();
		laVBD.setPlateNo("T11523");
		laVBD.setVin("1EUAN6A14EB031365");
		laVBD.setDocNo("10139136478145121");
		laVBD.setOwnerCountyNo("220");

		RefundData laRD = new RefundData();
		laRD.setConvFee("0.00");
		laRD.setOrigTraceNo("111ABC0000000");
		laRD.setPymtOrderId("123456");
		laRD.setRefAmt("50.00");
		laRD.setVehBaseData(laVBD);
		Refund laRefund = new Refund();
		laRefund.setSimAtTxdot(false);
		if (aarrArgs != null)
		{
			if (aarrArgs.length > 0)
			{
				if (aarrArgs[0].equals("-p"))
				{
					laRefund.setUseProxy(true);
				}
				if (aarrArgs[0].equals("-np"))
				{
					laRefund.setUseProxy(false);
				}
			}
		}
		try
		{
			laRefund.doRefund(laRD);
			System.out.println(
				"A Refund sent to TxO, no error occurred.");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			if (aeEx instanceof RTSWebAppsException)
			{
				System.out.println(
					((RTSWebAppsException) aeEx).getDetailedMessage());
			}
			if (aeEx instanceof RTSException)
			{
				System.out.println(
					((RTSException) aeEx).getDetailMsg());
			}
		}
	}
	/**
	 * processRefundResult
	 * 
	 * @param aaRefundData RefundData
	 * @return boolean
	 */
	public boolean processRefundResult(RefundData aaRefundData)
	{
		int liRefundResult = aaRefundData.getRefundStatus();
		if (liRefundResult == CommonConstants.EPAY_ERROR)
		{
			// if an error occurred at Epay, we keep the status
			// REFUND_PENDING so we do nothing and report to MQ that the
			// transaction was successfully delivered.
			return true;
		}
		// handle refundResults of CommonConstants.REFUND_APPROVED
		// and CommonConstants.REFUND_DECLINED update the CntyStatusCd
		DatabaseAccess laDBA = new DatabaseAccess();
		boolean lbSuccess = false;
		try
		{
			laDBA.beginTransaction();
			InternetTransaction laIntrntRenProc =
				new InternetTransaction(laDBA);
			// defect 8501
			// defect 10016
			// INET is sends null PlateNo in VehBaseData
			// When a clerk initiates the refund, PlateNo is populated
			// in VehBaseData
			// defect 10083
			// the first part of the if statement below is used by the 
			// POS client issuing refunds
			if (aaRefundData.getVehBaseData().getPlateNo() != null &&
			   !aaRefundData.getVehBaseData().getPlateNo().equals(""))
			{    	
				laIntrntRenProc.updateCntyStatus(
					aaRefundData.getVehBaseData(),
					liRefundResult);
			}
			else
			{
				laIntrntRenProc.updateCntyStatus(
					liRefundResult,
					aaRefundData.getOrigTraceNo());
			}
			// end defect 10083 
			// end defect 10016
			// end defect 8501	
			lbSuccess = true;

		}
		catch (RTSException aeEx)
		{
			MQLog.error(aeEx);
			return false;
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
			catch (RTSException aeEx)
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * setSimAtTxdot
	 * 
	 * @param abNewSimAtTxdot boolean
	 */
	public void setSimAtTxdot(boolean abNewSimAtTxdot)
	{
		cbSimAtTxdot = abNewSimAtTxdot;
	}
	/**
	 * setUseProxy
	 * 
	 * @param abNewUseProxy boolean
	 */
	public void setUseProxy(boolean abNewUseProxy)
	{
		cbUseProxy = abNewUseProxy;
	}
	/**
	 * simulateRefund
	 * 
	 * @param aaRefundData RefundData
	 * @throws RTSException
	 */
	protected void simulateRefund(RefundData aaRefundData)
		throws RTSException
	{
		// simulate the refund
		if (cbSimAtTxdot)
		{
			// (1) simulate on TxDOT site.
			aaRefundData.setRefundStatus(
				CommonConstants.DECLINED_REFUND_APPROVED);
			processRefundResult(aaRefundData);
		}
		else
		{
			// (2) Simulate on TexasOnline site.
			// Test sending proper data to TexasOnline site.
			if (CommunicationProperty.getUseMQ())
			{
				// Code for using MQ Series
				try
				{
					WebAppsMQAccess laMQAccess =
						new WebAppsMQAccess(
							CommunicationProperty.getMQTxdotHost(),
							CommunicationProperty.getMQTxdotChannel(),
							CommunicationProperty
								.getMQTxdotQueueManager(),
							CommunicationProperty.getMQTxdotPort());
					laMQAccess.postMessage(
						CommunicationProperty.getMQTxdotPostQueue(),
						aaRefundData);
				}
				catch (Exception aeEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeEx);
				}
			}
			else
			{
				try
				{
					Object laRefundResult =
						Comm.sendToServer(
							CommonConstants.REGISTRATION_RENEWAL,
							RegistrationRenewalConstants.DO_REFUND,
							aaRefundData,
							CommunicationProperty
								.CONNECT_TO_TEXASONLINE);
								
					RefundData laProcessedRefundData =
						(RefundData) laRefundResult;
						
					processRefundResult(laProcessedRefundData);
				}
				catch (RTSWebAppsException aeRTSEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeRTSEx);
				}
				catch (Exception aeEx)
				{
					// other kinds of exception
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeEx);
				}
			}
		}
	}
}
