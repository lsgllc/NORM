package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.desktop.SplashScreen;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.help.business.MessageHandler;
import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.BroadcastMsgConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 *
 * RTSMain.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Michael Abs	09/10/2001	Added Comments
 * MAbs			04/25/2002	Added code to prevent multiple instances of 
 *							RTSMain from running CQU100003590
 * MAbs			05/02/2002	Took out code cleanup before reboot
 * MAbs			05/22/2002	Moved batch calls to function so reboot 
 *							won't occur until done CQU100004064
 * MAbs			06/04/2002	Installed wait time for printer and shut 
 *							down threads on Shutdown CQU100004200
 * RHicks		09/30/2002	Fixed Defect 4777,Do not cache DNS entries 
 *							for inetaddr by making changes to run method
 * RHicks		09/30/2002	Fixed 4800. Made changes to run method by 
 *							stopping all the threads until caSendCache has
 *							completed to prevent system freeze.
 * Ray Rowehl	02/20/2003	Defect CQU100005533
 *							send port error messages to rtsapp.log as 
 *							well as stderr. Also make message more 
 *							distinctive.
 * Ray Rowehl	05/28/2003	Add new shutdown processing for XP
 *							modify reboot()
 *							defect 5999
 * Kathy H.		10/29/2003	Use new SendCache setters
 *							setShouldRestart vs. restart
 *							modify run(), handleThreadEvent()
 *							defect 6614
 * Jeff S.		03/05/2004	Made call to BatchProcessor method that 
 *							updates the RTS_WS_Status table with the 
 *							current workstation status if it is 
 *							different than what the DB shows.
 *							modify run()
 *							defect 6918 Ver 5.1.6
 * Jeff S.		09/17/2004	Added call to start the thread that moves
 *							rsps source code to WS.
 *							modify run()
 *							defect 7135 Ver 5.2.1
 * Jeff S.		10/04/2004	Only start RSPS refresh thread if not comm
 *							server.
 *							modify run()
 *							defect 7135 Ver 5.2.1
 * Jeff S.		03/03/2005	Get code to standard.
 *							defect 7897 Ver 5.2.3
 * Jeff S.		03/31/2005	Removed code that started the RSPS refresh 
 * 							thread.  This is now done in Refresh since
 * 							the rewrite.
 * 							modify run()
 * 							defect 8014 Ver. 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							deprecated isWorkstation()  
 * 							defect 7884  Ver 5.2.3  
 * Min Wang		07/11/2005  Add check of Java Version to log if it 
 * 							is not the expected version.
 * 							modify run()
 * 							defect 8242 Ver 5.2.3     
 * Jeff S.		09/27/2005  Added redirect of standard err and out to a
 * 							log file.  Show a splash screen and update
 * 							the progress label with the current progress
 * 							while the app is loading.
 * 							add ORIG_SYSTEM_OUT, ORIG_SYSTEM_ERR
 * 							add redirectStdErrorOut(), 
 * 								showSplashScreen()
 * 							modify run()
 * 							defect 8372 Ver 5.2.3
 * Jeff S.		10/06/2005  Move the call to prune log files into 
 * 							RTSMain.
 * 							modify run()
 * 							defect 8366 Ver 5.2.3
 * Ray Rowehl	10/13/2005	Shutdown MFReports more gracefully.
 * 							modify run()
 * 							defect 8405 Ver 5.2.3
 * Ray Rowehl	10/14/2005	Add a System.exit(0) to signal java for a 
 * 							complete shutdown.  Still need some 
 * 							improvement.
 * 							modify run()
 * 							defect 8405 Ver 5.2.3
 * Jeff S.		01/17/2006	Add a System.exit(0) to signal java for a 
 * 							complete shutdown when there is multiple
 * 							instances or the barcode port is not 
 * 							properly setup.
 * 							modify run()
 * 							defect 8405 Ver 5.2.3
 * Jeff S.		04/02/2007	Added the thread for RTS Broadcast Messages
 * 							and adjustd thread event to handle new 
 * 							message events.
 * 							add caThreadMessageHandler, 
 * 								caMessageHandler, LOADING_MESSENGER, 
 * 								RTS_MESS_THREAD
 * 							modify handleThreadEvent(), run()
 * 							defect 7768 Ver Broadcast Message
 * K Harrell	03/09/2007	Add setting of SystemProperty OfficeIssuanceCd
 * 							delete isWorkstations()
 * 							modify retrieveWSData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/18/2009	Add setting of SystemProperty ClientServer
 * 							delete cbWorkstation, WORKSTATION_CODE, 
 * 							 	lsWorkstationCode,SERVER_CODE
 * 							modify retrieveWSData(), run()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	03/29/2010	Log new message (vs. OFFICE_NUM_ERROR_MSG) 
 * 							 when NO Cache."  
 * 							add EMPTY_CACHE_ERROR
 * 							modify retrieveWSData()
 * 							defect 8087 Ver POS_640 
 * M Reyes		09/07/2011	Moved wait time for printing reports
 * 							modify handleThreadEvent()
 * 							defect 10987 Ver 6.8.1
 * R Pilon		05/21/2012	Deprecated class and pop-up error message
 * 							  from main if someone attempts to run.
 * 							add @deprecated in class javadoc
 * 							modify main(String[])
 * 							defect 11367 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
* The Main entry point into the RTSII System.
* 
* <p>This thread creates all the other threads in the application and 
* controls their operation.  It gets created by the native start-up 
* code upon bootup of the system.
* 
* <p> The RTSMain class is individually responsible for:
* <ul>
* <li>Creating a Scheduler graph, which controls the timing of
*	each operation
* <li>Retrieving the Workstation data
* <li>Connecting to the Printer
* <li>Creating the SendCache and running it the first time
* <li>Creating a Shutdown Listener to listen for remote 
* 	shutdown calls
* <li>Creating the RTSApplicationController, which controls 
* 	the visual portion of the RTSII System.
* <li>Rebooting the computer when needed
* <li>Listening for ThreadEvents from the Scheduler and 
* 	acting upon them to perform specific tasks at 
* 	specific times.
* </ul>
* 
* <p>RTSMain, under normal operation will reboot the box as scheduled 
* by Scheduler.
* 
* @version	POS_700			05/21/2012
* @author	Michael Abernethy
* <br>Creation Date:		09/05/2001	12:06:58
* @deprecated as of 7.0.0
*/

public class RTSMain implements ThreadListener, Runnable
{
	/**
	 *	The error message to display when an error occurs
	 */
	private final static String ERROR_MESSAGE =
		"An error occurred in RTSMain - Contact System Administrator";

	/**
	 * An graph of the thread running the RTSApplicationController.
	 */
	private Thread caThreadAppController;

	/**
	 * An graph of the RTSApplicationController.
	 */
	private RTSApplicationController caAppController;

	// defect 7768
	/**
	 * An graph of the thread running the MessageHandler.
	 */
	private Thread caThreadMessageHandler;

	/**
	 * An graph of the MessageHandler.
	 */
	private MessageHandler caMessageHandler;
	// end defect 7768

	/**
	 * An graph of the thread running the Send Cache.
	 */
	private Thread caThreadSendCache;

	/**
	 * An graph of the Send Cache.
	 */
	private SendCache caSendCache;

	/**  An graph for Main Frame Reports printing **/
	private MFReports caMFReports;
	/**  An graph of the thread running MF Reports printing **/
	private Thread caThreadMFReports;

	private boolean lbNormalStop;

	private BatchProcessor caBatchProcessor;

	private final static int MILLISECONDS_PER_SECOND = 1000;
	private final static int SECONDS_PER_MINUTE = 60;

	// defect 8372
	// Get the original Print Streams in case we need to write to the 
	// console
	public static PrintStream ORIG_SYSTEM_OUT = System.out;
	public static PrintStream ORIG_SYSTEM_ERR = System.err;
	private static SplashScreen caSplashScreen = new SplashScreen();
	// end defect 8372

	/**
	 * Constants
	 */
	private static final String ABOUT_TO_REBOOT_MSG =
		"Computer is about to reboot....";
	private static final String BARCODE_SCANNER = "Barcode Scanner";
	private static final String CACHE_LOAD_ERROR_MSG =
		": Cache load error: ";
	private static final String CONTACT_HELPDESK_MSG =
		"Please contact the help desk.";
	private static final String ERROR_REDIRECT_STD_MSG =
		": Error Redirecting Standard Error & Out: ";
	private static final String LOADING = "Loading";
	private static final String LOADING_CACHE = "Loading Cache";
	private static final String LOADING_DESKTOP = "Loading the Desktop";
	// defect 7768
	private static final String LOADING_MESSENGER = "Loading Messenger";
	// end defect 7768
	private static final String MF_REPORTS_THREAD = "mfReports";
	private static final String NO_SUCH_PORT_MSG =
		" Only one graph of RTSMain can be run at "
			+ "one time.  There is currently a version already "
			+ "running.  Contact Help Desk if you have "
			+ "problems.  NoSuchPort";
	private static final String OFFICE_NUM_ERROR_MSG =
		"The system's Office Issuance Number, Substation ID, and "
			+ "Workstation ID are not set correctly.";

	// defect 8087 
	private static final String EMPTY_CACHE_ERROR_MSG =
		"The system's cache is empty.";
	// end defect 8087 

	private static final String PORT_IN_USE_MSG =
		" Only one graph of RTSMain can be run at one time."
			+ "  There is currently a version already running."
			+ "  Contact Help Desk if you have problems."
			+ "  PortInUse";
	private static final String PRINTING_BATCH =
		"Printing batch reports";
	private static final String PRUNNING_LOG_FILES =
		"Pruning Log Files";
	private static final String REBOOT_MSG =
		"RTSMain : received REBOOT event";
	private static final String REMOTE_LISTENER_THREAD =
		"Remote Listener";
	private static final String RTS_APP_CONTROL_THREAD =
		"RTS Application Controller";
	// defect 7768
	private static final String RTS_MESS_THREAD =
		"RTS Message Controler";
	// end defect 7768
	private static final String RTS_STARTED_MSG =
		"RTS II Application has started";
	private static final String RTS_STOPPED_MSG =
		"RTS II Application has closed";
	private static final String SCHEDULE_THREAD = "Schedule";
	private static final String SEND_CACHE_THREAD = "Send Cache";
	private static final String STD_ERROR = "err.";
	private static final String STD_OUT = "out.";
	private static final String STARTING_MF_REPORT_THREAD =
		"Starting mainframe reports thread";
	private static final String STARTING_REMOTE_LISTENER =
		"Starting remote listener";
	private static final String STARTING_SCHEDULER =
		"Starting scheduler";
	private static final String START_SND_CACHE = "Starting Send Cache";
	private static final String SUN_NET_INETADDR =
		"sun.net.inetaddr.ttl";
	private static final String UPDATING_WS_STATUS =
		"Updating workstation status";
	private static final String ZERO = "0";

	public static boolean cbStartApp = false;
	/**
	 * Creates the RTSMain.
	 */
	public RTSMain()
	{
	}
	/**
	 * Handles all the ThreadEvents that are thrown during the life of 
	 * RTSMain.
	 * <p>ThreadEvents tell the RTSMain what action to perform.  RTSMain 
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
					/*
							Run the batch processes
					*/
					// defect 8628 
					if (SystemProperty.isClientServer())
					{
						// end defect 8628 
						caBatchProcessor.printReports();
						// defect 10987
						// Wait for printer to finish printing documents 
						// before rebooting
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
					// Wait for printer to finish printing documents 
					// before rebooting
				//	try
					//{
					//	Thread.sleep(
					//		MILLISECONDS_PER_SECOND
					//			* SECONDS_PER_MINUTE
					//			* SystemProperty.getPrintWaitTime());
					//}
					//catch (InterruptedException aeIEx)
					//{
					//}
					// end defect 10987

					BatchLog.write(ABOUT_TO_REBOOT_MSG);
					System.out.println(ABOUT_TO_REBOOT_MSG);
					reboot();
					break;
				}
			case ThreadEvent.SEND_CACHE :
				{
					// defect 6614
					// Use setShouldRestart
					caSendCache.setShouldRestart(true);
					// end defect 6614
					break;
				}
				// defect 7768
				// Used my the message process to open the inbox.
			case ThreadEvent.OPEN_INBOX :
				{
					AbstractViewController laVC =
						caAppController.getMediator().getController();
					try
					{
						laVC.setNextController(ScreenConstant.MES001);
						laVC.setDirectionFlow(
							AbstractViewController.NEXT);
						caAppController.getMediator().processData(
							GeneralConstant.HELP,
							BroadcastMsgConstants.GET_MESSAGES,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(laVC.getFrame());
					}
					break;
				}
				// Used my the message process to update the inbox with new
				// messages
			case ThreadEvent.UPDATE_INBOX :
				{
					caAppController
						.getMediator()
						.getController()
						.processData(
						BroadcastMsgConstants.GET_MESSAGES,
						null);
					break;
				}
				// Used my the message process when the user clicks on 
				// the alert balloon.
			case ThreadEvent.FOCUS_TOP_FRAME :
				{
					if (caAppController == null
						|| caAppController.getMediator() == null
						|| caAppController.getMediator().getController()
							== null
						|| caAppController
							.getMediator()
							.getController()
							.getFrame()
							== null)
					{
						MessageHandler.runSetRTSFocus(
							caAppController.getDesktop().getTitle());
					}
					else
					{
						MessageHandler.runSetRTSFocus(
							caAppController
								.getMediator()
								.getController()
								.getFrame()
								.getTitle());
					}
				}
				// end defect 7768
		}
	}

	/**
	 * Starts the entire RTSII System.  The single most important 
	 * function in all these hundreds of thousands of lines of code!
	 *
	 * @param args java.lang.String[]
	 * @throws RTSException 
	 */

	public static void main(String[] aarrArgs)
	{
		// defect 11367
//		RTSMain laRTS = new RTSMain();
//		Thread laRTSThread = new Thread(laRTS);
//		laRTSThread.start();
		RTSException leRTSEx = new RTSException(
				RTSException.SYSTEM_ERROR,
				"Class has been deprecated. \n\nPlease contact your "
						+ "System Administrator.", "System Error");
		leRTSEx.displayError();
		// end defect 11367
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
			// defect 8087 
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
			// end defect 8087 

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
			// defect 8628 
			SystemProperty.setClientServer();
			// end defect 8628   
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

		// defect 9085 
		// Use cache to determine OfcIssuanceCd ONCE! 
		// SystemProperty.isHQ(), isRegion(), isCounty() subsequently
		// available 
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
		// end defect 9085

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
		// defect 8372
		// Redirect the standard error and standard out to a file when 
		// ProdStatus is production.
		redirectStdErrorOut();
		// Show the splash screen and update the splash screen with the
		// progress.
		showSplashScreen(true);
		updateSplashScreenProgress(LOADING);
		// end defect 8372

		BatchLog.write(RTS_STARTED_MSG);
		lbNormalStop = false;

		//  Set system property so that DNS names are not cached by the 
		// java.net inet address classes
		System.setProperty(SUN_NET_INETADDR, ZERO);

		// defect 8242
		SystemProperty.isJavaVersionCorrect();
		// end defect 8242

		// defect 8366
		// Used to prune the log files. This was called from the service
		// but has been moved to RTSMain.
		updateSplashScreenProgress(PRUNNING_LOG_FILES);
		PruneLogFiles.pruneLogFiles();
		// end defect 8366

		// defect 8014
		// Removed call that started RSPS refresh because this is now
		// done in Refresh since the re-write.
		// defect 7135
		// Start thread that will move all RSPS Source code & updates
		// from the code server. Only run if the current WS is not the
		// Comm server.
		//if (!SystemProperty.isCommServer())
		//{
		//	RSPSRefreshUtility laRSPSRefreshUtil =
		//		new RSPSRefreshUtility();
		//	Thread laRSPSRefreshThread =
		//		new Thread(laRSPSRefreshUtil, "rspsRefresh");
		//	laRSPSRefreshThread.setDaemon(true);
		//	laRSPSRefreshThread.start();
		//}
		// end defect 7135
		// end defect 8014

		// Code to test if more than one graph of RTSMain is running
		// Only do this code in production, otherwise it's a pain to 
		// developers
		if (SystemProperty.getProdStatus() == 0)
		{
			try
			{
				CommPortIdentifier laCommPort =
					CommPortIdentifier.getPortIdentifier(
						SystemProperty.getBarcodePort());
				SerialPort laSerialPort =
					(javax.comm.SerialPort) laCommPort.open(
						BARCODE_SCANNER,
						30);

				laSerialPort.close();
			}
			catch (NoSuchPortException aeNSPEx)
			{
				String lsErrMsgNoPort = NO_SUCH_PORT_MSG;
				System.err.println(lsErrMsgNoPort);
				Log.write(Log.START_END, this, lsErrMsgNoPort);
				// defect 8405
				// The application would still be running and the splash
				// screen showed PRUNNING_LOG_FILES since that was the 
				// last thing that was done.
				//return;
				System.exit(0);
				// end defect 8405
			}
			catch (PortInUseException aePIUEx)
			{
				String lsErrMsgPortInUse = PORT_IN_USE_MSG;
				System.err.println(lsErrMsgPortInUse);
				Log.write(Log.START_END, this, lsErrMsgPortInUse);
				// defect 8405
				// The application would still be running and the splash
				// screen showed PRUNNING_LOG_FILES since that was the 
				// last thing that was done.
				//return;
				System.exit(0);
				// end defect 8405
			}
		}

		updateSplashScreenProgress(LOADING_CACHE);

		retrieveWSData();

		caBatchProcessor = new BatchProcessor();

		// defect 8628
		if (SystemProperty.isClientServer())
		{
			// end defect 8628 
			updateSplashScreenProgress(PRINTING_BATCH);
			caBatchProcessor.printReports();
		}

		// defect 6918
		// Added to update the RTS_WS_Status table with the
		// Workstations current status ie. Version, Version Date
		updateSplashScreenProgress(UPDATING_WS_STATUS);
		caBatchProcessor.updateWorkstationStatus();
		// end defect 6918

		/*
				Start the Send Cache thread
		*/
		updateSplashScreenProgress(START_SND_CACHE);
		caSendCache = new SendCache();
		caThreadSendCache = new Thread(caSendCache, SEND_CACHE_THREAD);
		caThreadSendCache.setDaemon(true);
		caThreadSendCache.start();

		caAppController = new RTSApplicationController();
		caAppController.addThreadListener(this);
		caSendCache.addCommListener(caAppController);

		while (!cbStartApp)
		{
			try
			{
				Thread.sleep(10000);
			}
			catch (InterruptedException aeIEx)
			{
			}
		}

		/*
				Launch the thread that handles main frame
				reports printing.
		*/
		updateSplashScreenProgress(STARTING_MF_REPORT_THREAD);
		caMFReports = new MFReports();
		caThreadMFReports = new Thread(caMFReports, MF_REPORTS_THREAD);
		caThreadMFReports.setDaemon(true);
		caThreadMFReports.start();

		/*
				Create the Scheduler to keep track of timing
		*/
		updateSplashScreenProgress(STARTING_SCHEDULER);
		Scheduler scheduler = new Scheduler();
		scheduler.addThreadListener(this);
		Thread laThreadScheduler =
			new Thread(scheduler, SCHEDULE_THREAD);
		laThreadScheduler.setDaemon(true);
		laThreadScheduler.start();
		/*
				Perform the various tasks needed before starting the 
				application
		*/

		/*
				Listen for any messages from outside services to shut down
		*/
		updateSplashScreenProgress(STARTING_REMOTE_LISTENER);
		RemoteListener laRemote = new RemoteListener();
		laRemote.addThreadListener(this);
		Thread laThreadRemote =
			new Thread(laRemote, REMOTE_LISTENER_THREAD);
		laThreadRemote.setDaemon(true);
		laThreadRemote.start();

		// defect 7768
		/*
				Start the Messenger Service
		*/
		updateSplashScreenProgress(LOADING_MESSENGER);
		caMessageHandler = new MessageHandler(this);
		caMessageHandler.addThreadListener(this);
		caThreadMessageHandler =
			new Thread(caMessageHandler, RTS_MESS_THREAD);
		caThreadMessageHandler.setDaemon(true);
		caThreadMessageHandler.start();
		// end defect 7768

		/*
				Start the RTS Application
		*/
		updateSplashScreenProgress(LOADING_DESKTOP);
		caThreadAppController =
			new Thread(caAppController, RTS_APP_CONTROL_THREAD);
		caThreadAppController.setDaemon(true);
		caThreadAppController.start();

		/*
				Wait until told to shut down RTSMain
		*/
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

		/*
				Stop all the threads it created and then break out of run() 
				method. This will effectively stop the entire RTSII 
				Application.
		*/
		// Defect 6614  Alter to use the set method
		// Do not need to set SendCache.lbShouldRestart
		// caSendCache.kill();  
		caSendCache.setKeepRunning(false);
		//caSendCache.restart();
		// end defect 6614 

		synchronized (caAppController)
		{
			caAppController.notify();
		}

		// defect 7768
		MessageHandler.setKeepRunning(false);
		caThreadMessageHandler = null;
		// end defect 7768

		// defect 8405 
		// shutdown MFReport
		MFReports.setKeepRunning(false);
		//caMFReports.notify();
		caThreadMFReports = null;
		caMFReports = null;
		// end defect 8405

		caAppController.finalize();
		caAppController = null;
		caThreadAppController = null;
		caSendCache = null;
		caThreadSendCache = null;
		scheduler = null;
		laThreadScheduler = null;
		laRemote = null;
		laThreadRemote = null;

		BatchLog.write(RTS_STOPPED_MSG);

		// defect 8405
		System.exit(0);
		// end defect 8405
	}
	/**
	 * This is the static method that is used to show and remove the 
	 * splash screen.  The boolean that is passed determines if the 
	 * splash screen is shown or not.
	 * 
	 * @param abShowScreen boolean
	 */
	public static void showSplashScreen(boolean abShowScreen)
	{
		caSplashScreen.setVisible(abShowScreen);
	}
	/**
	 * This is the static method that is used to update the progress
	 * label on the splash screen.
	 * 
	 * @param asProgress String
	 */
	public static void updateSplashScreenProgress(String asProgress)
	{
		caSplashScreen.updateProgress(asProgress);
	}
}