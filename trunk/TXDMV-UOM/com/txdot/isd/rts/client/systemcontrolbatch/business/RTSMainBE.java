package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 *
 * RTSMain.java
 *
 * (c) Texas Department of Motor Vehicles 2012.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		03/30/2012	Created class
 * 							Copied from original RTSMain.
 * 							defect 11066 Ver 700
 * R Pilon		04/12/2012  Changes for handling communication with GUI
 * 							  for process complete, database up, and 
 * 							  database down
 * 							add SEND_CACHE_EVENT_MSG
 * 							modify handleThreadEvent(ThreadEvent), run(), 
 *							defect 11073 Ver 7.0.0
 * Min Wang		05/08/2012	Modify start and stop message.
 * 							modify RTS_STARTED_MSG, RTS_STOPPED_MSG
 * 							defect 11066 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
* The Main entry point into the RTSII System .
*
* @version	POS_700			05/08/2012
* @author					Min Wang
* @since 					03/30/2012	12:06:58
*/

public class RTSMainBE implements ThreadListener, Runnable
{
	/**
	 *	The error message to display when an error occurs
	 */
	private final static String ERROR_MESSAGE =
		"An error occurred in RTSMain - Contact System Administrator";

	/**
	 * An graph of the thread running the Send Cache.
	 */
	private Thread caThreadSendCache;

	/**
	 * An graph of the Send Cache.
	 */
	private SendCache caSendCache;

	/**  
	 * An graph for Main Frame Reports printing
	 */
	private MFReports caMFReports;
	
	/**  
	 * An graph of the thread running MF Reports printing
	 */
	private Thread caThreadMFReports;
	private BatchProcessor caBatchProcessor;
	private final static int MILLISECONDS_PER_SECOND = 1000;
	private final static int SECONDS_PER_MINUTE = 60;
	public static PrintStream ORIG_SYSTEM_OUT = System.out;
	public static PrintStream ORIG_SYSTEM_ERR = System.err;
	 
	/**
	 * Constants
	 */
	private static final String ABOUT_TO_REBOOT_MSG =
		"Computer is about to reboot....";
	private static final String CACHE_LOAD_ERROR_MSG =
		": Cache load error: ";
	private static final String CONTACT_HELPDESK_MSG =
		"Please contact the help desk.";
	private static final String ERROR_REDIRECT_STD_MSG =
		": Error Redirecting Standard Error & Out: ";
	private static final String MF_REPORTS_THREAD = "mfReports";
	private static final String OFFICE_NUM_ERROR_MSG =
		"The system's Office Issuance Number, Substation ID, and "
			+ "Workstation ID are not set correctly."; 
	private static final String EMPTY_CACHE_ERROR_MSG =
		"The system's cache is empty.";
	private static final String REBOOT_MSG =
		"RTSMain : received REBOOT event";
	private static final String REMOTE_LISTENER_THREAD =
		"Remote Listener";
	// defect 11066
	private static final String RTS_STARTED_MSG =
		"RTS II Backend has started";
	private static final String RTS_STOPPED_MSG =
		"RTS II Backend has closed";
	// end defect 11066
	private static final String SCHEDULE_THREAD = "Schedule";
	private final static String SEND_CACHE_EVENT_MSG = 
		"Received ThreadEvent.SEND_CACHE event"; 
	private static final String SEND_CACHE_THREAD = "Send Cache";
	private static final String STD_ERROR = "err.";
	private static final String STD_OUT = "out.";
	private static final String SUN_NET_INETADDR =
		"sun.net.inetaddr.ttl";
	private static final String ZERO = "0";

	public static boolean cbStartApp = false;
	
	//private static final String STARTING_REMOTE_LISTENER =
	//	"Starting remote listener";
	
	/**
	 * Creates the RTSMainBE.
	 */
	public RTSMainBE()
	{
	}
	
	/**
	 * Handles all the ThreadEvents that are thrown during the life of 
	 * RTSMainBE.
	 * <p>ThreadEvents tell the RTSMain what action to perform.  RTSMainBE 
	 * will always perform the action immediately upon receiving it
	 * in this method.
	 *
	 * @param aaThreadEvent TheadEvent
	 */
	public void handleThreadEvent(ThreadEvent aaThreadEvent)
	{
		switch (aaThreadEvent.getEventCode())
		{
			case ThreadEvent.STOP_MAIN :
				{
					synchronized (this)
					{
						notify();
					}
					break;
				}
			case ThreadEvent.REBOOT_SYS :
				{
					BatchLog.write(REBOOT_MSG);

					if (SystemProperty.isClientServer())
					{
						caBatchProcessor.printReports();
						try
						{
							Thread.sleep(
								MILLISECONDS_PER_SECOND
									* SECONDS_PER_MINUTE
									* SystemProperty.getPrintWaitTime());
						}
						catch (InterruptedException aeIEx)
						{
						}
					}

					BatchLog.write(ABOUT_TO_REBOOT_MSG);
					System.out.println(ABOUT_TO_REBOOT_MSG);
					reboot();
					break;
				}
			case ThreadEvent.SEND_CACHE :
				{
					caSendCache.setShouldRestart(true);
					// defect 11073
					caSendCache.setRTSAppControllerDBUp(false);
					if (!SendCache.isProcessing())
					{
						// if not currently processing, force SendCache to 
						// wake up and process
						caThreadSendCache.interrupt();
					}
					BatchLog.write(SEND_CACHE_EVENT_MSG);
					// end defect 11073
					break;
				}
		}
	}

	/**
	 * Starts the entire RTSII System.  The single most important 
	 * function in all these hundreds of thousands of lines of code!
	 *
	 * @param args java.lang.String[]
	 */
	public static void main(String[] aarrArgs)
	{
		RTSMainBE laRTS = new RTSMainBE();
		Thread laRTSThread = new Thread(laRTS);
		laRTSThread.start();
	}
	
	/**
	 * Call the appropriate reboot command
	 */
	private void reboot()
	{
		UtilityMethods.doShutdown();
	}
	
	/**
	 * This method is used to redirect the standard error and standard
	 * out to a file.  This is done so that we can rid the application 
	 * of the debug console window while in production.  This will only
	 * happen when we are in production status of zero.
	 */
	private void redirectStdErrorOut()
	{
		// Only redirect Standard error & out in production
		if (SystemProperty.getProdStatus()
			== SystemProperty.APP_PROD_STATUS)
		{
			try
			{
				File laStdErrorFile =
					new File(
						SystemProperty.getLogFileName()
							+ STD_ERROR
							+ SystemProperty.getLogFileExt());
				if (!laStdErrorFile.exists())
				{
					laStdErrorFile.createNewFile();
				}
				System.setErr(
					new DateTimePrintStream(
						new FileOutputStream(laStdErrorFile, true)));

				File laStdOutFile =
					new File(
						SystemProperty.getLogFileName()
							+ STD_OUT
							+ SystemProperty.getLogFileExt());
				if (!laStdOutFile.exists())
				{
					laStdOutFile.createNewFile();
				}
				System.setOut(
					new DateTimePrintStream(
						new FileOutputStream(laStdOutFile, true)));
			}
			catch (Exception aeEx)
			{
				Log.write(
					Log.START_END,
					this,
					ERROR_MESSAGE
						+ ERROR_REDIRECT_STD_MSG
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ aeEx.getMessage());
			}
		}
	}
	
	/**
	 * Retrieves the Workstation data.
	 */
	private void retrieveWSData()
	{

		// Read the 3 fields to determine workstation status
		int liWorkstationId = SystemProperty.getWorkStationId();
		int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
		int liSubstationId = SystemProperty.getSubStationId();

		// Reload the cache
		try
		{
			CacheManager.loadCache();
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println(
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());
			aeRTSEx.printStackTrace();
			Log.write(
				Log.START_END,
				this,
				ERROR_MESSAGE
					+ CACHE_LOAD_ERROR_MSG
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());

			System.exit(0);
		}

		// Read the cache to determine if valid settings in rtscld.cfg
		try
		{
			if (AssignedWorkstationIdsCache.isEmpty() 
			|| OfficeIdsCache.isEmpty())
			{
				System.err.println(
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ EMPTY_CACHE_ERROR_MSG
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ CONTACT_HELPDESK_MSG);
						
				Log.write(
					Log.START_END,
					this,
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ EMPTY_CACHE_ERROR_MSG
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ CONTACT_HELPDESK_MSG);
						
				System.exit(0);
			}
			AssignedWorkstationIdsData laWSData =
				AssignedWorkstationIdsCache.getAsgndWsId(
					liOfcIssuanceNo,
					liSubstationId,
					liWorkstationId);

			if (laWSData == null)
			{
				System.err.println(
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ OFFICE_NUM_ERROR_MSG
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ CONTACT_HELPDESK_MSG);
				Log.write(
					Log.START_END,
					this,
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ OFFICE_NUM_ERROR_MSG
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ CONTACT_HELPDESK_MSG);
				System.exit(0);
			}
			SystemProperty.setClientServer(); 
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println(
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());
			aeRTSEx.printStackTrace();
			Log.write(
				Log.START_END,
				this,
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());
			System.exit(0);
		}
		OfficeIdsData laOfcIdsData =
			OfficeIdsCache.getOfcId(liOfcIssuanceNo);
		if (laOfcIdsData == null)
		{
			System.err.println(
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ OFFICE_NUM_ERROR_MSG
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ CONTACT_HELPDESK_MSG);
			Log.write(
				Log.START_END,
				this,
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ OFFICE_NUM_ERROR_MSG
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ CONTACT_HELPDESK_MSG);
			System.exit(0);
		}
		SystemProperty.setOfficeIssuanceCd(
			laOfcIdsData.getOfcIssuanceCd());
	}
	
	/**
	 * This method creates all the threads that will be in operation 
	 * during the life of RTSMain. It then waits until it is told to 
	 * shut down all of its threads and then return from the run method.
	 * This will effectively stop the RTSMain thread and will stop the 
	 * RTSII application.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		redirectStdErrorOut();
		
		BatchLog.write(RTS_STARTED_MSG);
		
		//  Set system property so that DNS names are not cached by the 
		// java.net inet address classes
		System.setProperty(SUN_NET_INETADDR, ZERO);
		SystemProperty.isJavaVersionCorrect();

		PruneLogFiles.pruneLogFiles();

		retrieveWSData();

		caBatchProcessor = new BatchProcessor();

		if (SystemProperty.isClientServer())
		{
			caBatchProcessor.printReports();
		}

		caBatchProcessor.updateWorkstationStatus();

		// defect 11073
		// Listen for any messages from outside services
		// NOTE:  The listener must be started before SendCache so that
		// any thread events that are fired will be processed
		RemoteListener laRemote = new RemoteListener();
		laRemote.addThreadListener(this);
		Thread laThreadRemote = new Thread(laRemote,
				REMOTE_LISTENER_THREAD);
		laThreadRemote.setDaemon(true);
		laThreadRemote.start();
		// end defect 11073
		
		// Start the Send Cache thread
		caSendCache = new SendCache();
		caThreadSendCache = new Thread(caSendCache, SEND_CACHE_THREAD);
		caThreadSendCache.setDaemon(true);
		caThreadSendCache.start();

		// defect 11073
		// TODO - HOW SHOULD THIS BE HANDLED NOW? SINCE WE ARE IN A DIFFERENT
		// JVM, HOW TO HANDLE RTSMainGUI STARTING WHEN SendCache IS NOT READY?
//		while (!cbStartApp)
//		{
//			try
//			{
//				Thread.sleep(10000);
//			}
//			catch (InterruptedException aeIEx)
//			{
//			}
//		}
		// end defect 11073

		//		Launch the thread that handles main frame
		//		reports printing.
		caMFReports = new MFReports();
		caThreadMFReports = new Thread(caMFReports, MF_REPORTS_THREAD);
		caThreadMFReports.setDaemon(true);
		caThreadMFReports.start();

		//		Create the Scheduler to keep track of timing
		Scheduler scheduler = new Scheduler();
		scheduler.addThreadListener(this);
		Thread laThreadScheduler =
			new Thread(scheduler, SCHEDULE_THREAD);
		laThreadScheduler.setDaemon(true);
		laThreadScheduler.start();

		//		Perform the various tasks needed before starting the 
		//		application
		//		Listen for any messages from outside services to shut down


		//		Wait until told to shut down RTSMain
		synchronized (this)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException aeIEx)
			{
				System.err.println(
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ aeIEx.getMessage());
				Log.write(
					Log.START_END,
					this,
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ aeIEx.getMessage());
			}
		}


		caSendCache.setKeepRunning(false);

		MFReports.setKeepRunning(false);
		caThreadMFReports = null;
		caMFReports = null;
		
		caSendCache = null;
		caThreadSendCache = null;
		scheduler = null;
		laThreadScheduler = null;
		laRemote = null;
		laThreadRemote = null;

		BatchLog.write(RTS_STOPPED_MSG);
		System.exit(0);
	}
}