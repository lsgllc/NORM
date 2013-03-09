package com.txdot.isd.rts.server.common.business;

import java.net.MalformedURLException;
import java.net.URL;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;

import proxy.soap.tdiev_1_9Proxy;

/*
 * TexasSureCall.java
 * VehicleInquiry.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/03/2011	Initial load
 * 							defect 10119 Ver 6.8.1 
 * Ray Rowehl	08/15/2011	Add a few pieces to help enable running 
 * 							with jUnit as proposed by Buck.
 * 							add ciTestSleepMillis
 * 							add getTestSleepMillis(), setTestSleepMillis()
 * 							modify run()
 * 							defect 10119 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * Make the call to check TexasSure for insurance status.
 * 
 * @version 6.8.1 08/15/2011
 * @author Ray Rowehl
 * @since 08/03/2011 09:31:05
 */

public class TexasSureCall implements Runnable
{
	
	private static final String TEXAS_SURE_CALL_SLEEP_INTERRUPTED = "TexasSureCall sleep interrupted ";
	private static final String FOR = " for ";
	private static final String TEXAS_SURE_CALL_SLEEP = "TexasSureCall Sleep ";
	private static final String DMV_CALL_ID		= "TxDOT-Reg";
	private static String	   DOC_NO			= " Doc No     ";
	private static String	   FAILED			= "FAILED ";
	private static String	   TEXAS_SURE_CALL_RESULT		= "TexasSureCall Result ";
	private static final String TEXAS_SURE_CALL_END_OF_CALL = "End of TexasSureCall call";
	private static final String TEXAS_SURE_CALL_START_OF_CALL = "Start of TexasSureCall call";
	private static String	   QUERY_ID		   	= "Query Id    ";
	private static String	   QUERY_TIME		= "Query Time  ";
	private static final String TEST_SWITCH		= "Test Switch ";
	private static final String TEXAS_SURE_CALL_GOT_INTERRUPTED = "TexasSureCall got Interrupted for ";

	/**
     * Main method so we can run stand alone.
     * 
     * @param args
     */
	public static void main(String[] args)
	{
		try
		{
			Comm.setIsServer(true);
			TexasSureCall laTSC = new TexasSureCall();
			laTSC.setDocNo("22710739789103750");
			laTSC.setPlate("RUN50");
			laTSC.setVin("JTDKT903595233614");
			laTSC.setTestSwitch("");
			Thread laThread = new Thread(laTSC);
			laThread.setDaemon(true);
			laThread.start();
			laThread.join(10000);
			laThread.interrupt();
			if (laTSC != null)
			{
				System.out.println(laTSC.getResult());
			}
		}
		catch(Throwable aaThrowable)
		{
			// If we get an exception, we are just going to
			// log it. We will not throw it back.
			RTSException leRTSEx = new RTSException(
							RTSException.JAVA_ERROR, aaThrowable);
			leRTSEx.printStackTrace();
		}

	}

	private int ciTestSleepMillis = -1;
	private String csDocNo	  = "";
	private String csPlate	  = "";
	private String csResult	 = FAILED;
	private String csTestSwitch = "";
	private String csVin		= "";

	/**
     * Get the Document Number.
     * 
     * @return String
     */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
     * Get the Plate Number.
     * 
     * @return String
     */
	public String getPlate()
	{
		return csPlate;
	}

	/**
     * Get the Result.
     * 
     * @return String
     */
	public String getResult()
	{
		return csResult;
	}
	
	/**
	 * Get the Sleep Milliseconds for testing non-response.
	 *
	 * @return int
	 */
	public int getTestSleepMillis()
	{
		return ciTestSleepMillis;
	}
	
	/**
     * Get the Test Switch.
     * 
     * @return string
     */
	public String getTestSwitch()
	{
		return csTestSwitch;
	}

	/**
     * Get the VIN.
     * 
     * @return string
     */
	public String getVin()
	{
		return csVin;
	}

	/**
     * Run method to call TDI Interface.
     */
	public void run()
	{
		// sleep if requested.
		if (getTestSleepMillis() > 0)
		{
			Log.write(Log.DEBUG, this, TEXAS_SURE_CALL_SLEEP + 
							getTestSleepMillis() + 
							FOR + 
							getPlate() + 
							" " + 
							getDocNo());
			try
			{
				Thread.sleep(getTestSleepMillis());
			}
			catch (InterruptedException aeIEx)
			{
				Log.write(Log.DEBUG, this, TEXAS_SURE_CALL_SLEEP_INTERRUPTED + 
								getTestSleepMillis() + 
								FOR + 
								getPlate() + 
								" " + 
								getDocNo());
			}
		}
		
		
		// setup client interface
		tdiev_1_9Proxy laFrvpWs = new tdiev_1_9Proxy();

		try
		{
			laFrvpWs.setEndPoint(new URL(SystemProperty.getTexasSureURL()));

			// prepare data for call
			String lsQueryId = "";
			String lsQueryOriginator = DMV_CALL_ID;
			String lsQueryTime = "";

			String lsDriversLicense = "";
			String lsRegName = "";
			String lsFirstName = "";
			String lsLastName = "";
			String lsDOB = "";
			String lsEffDate = "";

			// make the call
			try
			{
				// Test Call
				//System.out.println(TEXAS_SURE_CALL_START_OF_CALL);
				
				Log.write(Log.DEBUG, this, TEXAS_SURE_CALL_START_OF_CALL);

				// test setup
				// 180000 = 30 minutes
				 //Thread.sleep(180000);

				// Put parameters in order to match WSDL
				csResult = laFrvpWs.tdireqhandler(lsQueryOriginator,
								lsQueryId, lsQueryTime, getDocNo(),
								getVin(), getPlate(), lsDriversLicense,
								lsRegName, lsFirstName, lsLastName, lsDOB,
								lsEffDate, getTestSwitch());

				Log.write(Log.DEBUG, this, TEXAS_SURE_CALL_RESULT + csResult);
				Log.write(Log.DEBUG, this, QUERY_ID + lsQueryId);
				Log.write(Log.DEBUG, this, QUERY_TIME + lsQueryTime);
				Log.write(Log.DEBUG, this, DOC_NO + csDocNo);
				Log.write(Log.DEBUG, this, TEST_SWITCH + getTestSwitch());
				
				// Test Call
				//System.out.println(TEXAS_SURE_CALL_END_OF_CALL);
			}
			catch (InterruptedException aeIEx)
			{
				RTSException leRTSEx = new RTSException(
								RTSException.JAVA_ERROR, aeIEx);
				
				String lsErrMsg = TEXAS_SURE_CALL_GOT_INTERRUPTED + 
					getPlate() + 
					" " + 
					getDocNo();
				Log.write(Log.SQL_EXCP, this, lsErrMsg);
				// leave this here for WAS Admin
				System.out.println(lsErrMsg);
			}
			catch(Exception aeEx)
			{
				// If we get an exception, we are just going to
				// log it. We will not throw it back.
				RTSException leRTSEx = new RTSException(
								RTSException.JAVA_ERROR, aeEx);
				aeEx.printStackTrace();
			}
		}
		catch(MalformedURLException aeMURLEx)
		{
			// If we get an exception, we are just going to
			// log it. We will not throw it back.
			RTSException leRTSEx = new RTSException(
							RTSException.JAVA_ERROR, aeMURLEx);
		}

	}

	/**
     * Set the Document Number.
     * 
     * @param asDocNo
     */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
     * Set the Plate Number.
     * 
     * @param asPlate
     */
	public void setPlate(String asPlate)
	{
		csPlate = asPlate;
	}
	
	/**
	 * Set the Sleep Milliseconds for testing non-reponse.
	 *
	 * @param aiTestSleepMillis
	 */
	public void setTestSleepMillis(int aiTestSleepMillis)
	{
		this.ciTestSleepMillis = aiTestSleepMillis;
	}
	
	/**
     * Set the Test Switch
     * 
     * @param asTestSwitch
     */
	public void setTestSwitch(String asTestSwitch)
	{
		csTestSwitch = asTestSwitch;
	}
	
	/**
     * Set the VIN
     * 
     * @param asVin
     */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
}
