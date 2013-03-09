package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.desktop.SplashScreen;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.help.business.MessageHandler;
import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.BroadcastMsgConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.event.CommEvent;
import com.txdot.isd.rts.services.util.event.ThreadEvent;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 *
 * RTSMainGUI.java
 *
 * (c) Texas Department of Motor Vehicles 2012.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		03/23/2012	Created class
 * 							Copied from original RTSMain.
 * 							defect 11070 Ver 7.0.0
 * R Pilon		03/26/2012	Remove dependency on BarCode scanner by
 * 							  commenting out System.exit(0).
 * 							modify run()
 * 							defect 11071 Ver 7.0.0
 * R Pilon		03/26/2012	Set the look and feel to the steel metal theme.
 * 							modify run()
 * 							add setLookAndFeel()
 * 							defect 11317 Ver 7.0.0
 * R Pilon		03/26/2012	Changed log file name for GUI standard out 
 * 							  and standard error.
 * 							modify redirectStdErrorOut()
 * 							defect 11318 Ver 7.0.0
 * Min Wang		04/06/2012	Change check of JavaVersion to exit from 
 * 							here.
 * 							add WRONG_JAVA_WAIT_TIME, WRONG_JAVA_VERSION
 * 							modify run()
 * 							defect 10129 Ver 7.0.0
 * R Pilon		04/19/2012	Add logic to handle ThreadEvent.DB_UP.
 * 							add extends ThreadMessenger
 * 							modify handleThreadEvent(), run()
 * 							defect 11073 Ver 7.0.0
 * Min Wang 	04/19/2012  Clean up
 * 							defect 11070 Ver 7.0.0
 * Min Wang		05/14/2012	Change start and stop message.
 * 							Remove start of scheduler.
 * 							modify RTS_STARTED_MSG, RTS_STOPPED_MSG
 * 							modify run()
 * 							defect 11070 Ver 7.0.0
 * R Pilon		05/14/2012	Surround fireThreadEvent with try/catch and exit
 * 							  when exception caught and add logic to perform
 * 							  System.exit(0) on start if current cache file
 * 							  is corrupt.
 * 							modify handleThreadEvent(), run()
 * 							defect 11073 Ver 7.0.0
 * R Pilon		05/17/2012	Use SystemProperty.APP_PROD_STATUS instead of 
 * 							  hardcoded value of 0 to check production mode.
 * 							modify run()
 * 							defect 11071 Ver 7.0.0
 * R Pilon		05/24/2012	Correct port in use msg.
 * 							modify PORT_IN_USE_MSG, NO_SUCH_PORT_MSG
 * 							defect 11073 Ver 7.0.0
 * R Pilon		07/02/2012	Fire thread event to determine if another
 * 							  graph of RTSMainGUI is already running.  The
 * 							  event is fired to the same port that RTSMainGUI
 * 							  should be listening on.  If the connection
 * 							  is accepted, then RTSMainGUI is already running.
 * 							modify run()
 * 							defect 11073 Ver 7.0.0
 * R Pilon		07/05/2012	Implement retry logic for firing of thread event
 * 							  to determine if RTSMainBE is running.
 * 							modify run()
 * 							defect 11407 Ver 7.0.0
 * R Pilon		08/09/2012	Initialize liRetries to 0 instead of 1.
 * 							modify run()
 * 							defect 11407 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
* The GUI Startup for the RTSII System.
* 
* <p> The RTSMainGUI class is individually responsible for:
* <ul>
* <li>Retrieving the Workstation data
* <li>Connecting to the Printer
* <li>Creating the RTSApplicationController, which controls 
* 	the visual portion of the RTSII System.
* <li>Listening for ThreadEvents from the Scheduler and 
* 	acting upon them to perform specific tasks at 
* 	specific times.
* </ul>
* 
* @version	7.0.0			08/09/2012
* @author   Min Wang
* @since	03/23/2012	11:06:58
*/

public class RTSMainGUI extends ThreadMessenger implements ThreadListener, Runnable
{
	/**
	 *	The error message to display when an error occurs
	 */
	private final static String ERROR_MESSAGE =
		"An error occurred in RTSMain GUI - Contact System Administrator";
	// defect 10129
	private static final String WRONG_JAVA_VERSION = "Wrong Java Version!";
	private static final int WRONG_JAVA_WAIT_TIME = 10000;
	// end defect 10129
	/**
	 * An graph of the thread running the RTSApplicationController.
	 */
	private Thread caThreadAppController;

	/**
	 * An graph of the RTSApplicationController.
	 */
	private RTSApplicationController caAppController;
 
	/**
	 * An graph of the thread running the MessageHandler.
	 */
	private Thread caThreadMessageHandler;

	/**
	 * An graph of the MessageHandler.
	 */
	private MessageHandler caMessageHandler;
	
	//defect 11070
	/**
	 * An graph of the thread running the Send Cache.
	 */
	 
	//private Thread caThreadSendCache;
  
	/**
	 * An graph of the Send Cache.
	 */
	//private SendCache caSendCache;

	/**  An graph for Main Frame Reports printing **/
	//private MFReports caMFReports;
	/**  An graph of the thread running MF Reports printing **/
	//private Thread caThreadMFReports;
    // end defect 11070
	
	// defect 11070
	//private BatchProcessor caBatchProcessor;
	// end defect 11070
	private final static int MILLISECONDS_PER_SECOND = 1000;
	private final static int SECONDS_PER_MINUTE = 60;

	// Get the original Print Streams in case we need to write to the 
	// console
	public static PrintStream ORIG_SYSTEM_OUT = System.out;
	public static PrintStream ORIG_SYSTEM_ERR = System.err;
	private static SplashScreen caSplashScreen = new SplashScreen();

	/**
	 * Constants
	 */
	private static final String ABOUT_TO_REBOOT_MSG =
		"Computer is about to reboot....";
	private static final String BARCODE_SCANNER = "Barcode Scanner";
	private static final String CACHE_LOAD_ERROR_MSG =
		": Cache load error: ";
	// defect 11073
	private static final String CHECKING_DB_UP = "Checking for database up";
	// end defect 11073
	private static final String CONTACT_HELPDESK_MSG =
		"Please contact the help desk.";
	private static final String ERROR_REDIRECT_STD_MSG =
		": Error Redirecting Standard Error & Out: ";
	private static final String LOADING = "Loading";
	private static final String LOADING_CACHE = "Loading Cache";
	private static final String LOADING_DESKTOP = "Loading the Desktop";
	// defect 7768
	private static final String LOADING_MESSENGER = "Loading Messenger";
	
	// defect 11070
	//private static final String MF_REPORTS_THREAD = "mfReports";
	// end defect 1070
	// defect 11073
	private static final String NO_SUCH_PORT_MSG =
		" Only one graph of RTSMainGUI can be run at "
			+ "one time.  There is currently a version already "
			+ "running.  Contact Help Desk if you have "
			+ "problems.  NoSuchPort";
	// end defect 11073
	private static final String OFFICE_NUM_ERROR_MSG =
		"The system's Office Issuance Number, Substation ID, and "
			+ "Workstation ID are not set correctly.";

	private static final String EMPTY_CACHE_ERROR_MSG =
		"The system's cache is empty.";

	// defect 11073
	private static final String PORT_IN_USE_MSG =
		" Only one graph of RTSMainGUI can be run at one time."
			+ "  There is currently a version already running."
			+ "  Contact Help Desk if you have problems."
			+ "  PortInUse";
	// end defect 11073
	// defect 11070
	//private static final String PRINTING_BATCH =
	//	"Printing batch reports";
	// end deefect 11070
	private static final String PRUNNING_LOG_FILES =
		"Pruning Log Files";
	private static final String REBOOT_MSG =
		"RTSMain : received REBOOT event";
	private static final String REMOTE_LISTENER_THREAD =
		"Remote Listener";
	private static final String RTS_APP_CONTROL_THREAD =
		"RTS Application Controller";
	private static final String RTS_MESS_THREAD =
		"RTS Message Controler";
	// defect 11070
	private static final String RTS_STARTED_MSG =
		"RTS II GUI has started";
	private static final String RTS_STOPPED_MSG =
		"RTS II GUI has closed";
	// end defect 11070
	private static final String SCHEDULE_THREAD = "Schedule";
	// defect 11070
	//private static final String SEND_CACHE_THREAD = "Send Cache";
	// end eefect 11070
	private static final String STD_ERROR = "err.";
	private static final String STD_OUT = "out.";
	// defect 11070
	//private static final String STARTING_MF_REPORT_THREAD =
	//	"Starting mainframe reports thread";
	// end defect 11070
	private static final String STARTING_REMOTE_LISTENER =
		"Starting remote listener";
	// defect 11070
	//private static final String STARTING_SCHEDULER =
	//	"Starting scheduler";
	//private static final String START_SND_CACHE = "Starting Send Cache";
	// end defect 11070
	private static final String SUN_NET_INETADDR =
		"sun.net.inetaddr.ttl";
	// defect 11070
	//private static final String UPDATING_WS_STATUS =
	//	"Updating workstation status";
	// end defect 11070
	
	// defect 11073
	private static final String VERIFYING_TRXCACHE = 
		"Verifying Transaction Cache";
	
	private static final String CHECKING_FOR_ANOTHER_INSTANCE =
		"Checking for another graph of RTS running";
	// end defect 11073
	
	private static final String ZERO = "0";
	
	// defect 11070
	//public static boolean cbStartApp = false;
	public static boolean cbStartApp = true;
	// end defect 11070
	/**
	 * Creates the RTSMain.
	 */
	public RTSMainGUI()
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
					
					if (SystemProperty.isClientServer())
					{
						// defect 11070
						//caBatchProcessor.printReports();
						// end defect 11070

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
				
					System.out.println(ABOUT_TO_REBOOT_MSG);
					// defect 11070
					//reboot();
					System.exit(0);
					// end defect 11070
					break;
				}
			// defect 11070
			// defect 11073
			case ThreadEvent.SEND_CACHE :
				{
					// TODO - ADD NEW LOGIC TO GET THE HOSTNAME FROM 
					// SOMEWHERE....SHOULD NOT BE HARDCODED AS localhost!!!
					// fire the thread event across to the JVM where SendCache 
					// is running
					try
					{
						fireThreadEvent(new ThreadEvent(
								ThreadEvent.SEND_CACHE), "localhost",
								SystemProperty.getRemoteListenerPortBE());
					}
					catch (RTSException leRTSEx)
					{
						// Unable to connect to ServerSocket for RTSMainBE, 
						// exiting application
						Log.write(Log.START_END, this, 
								"Unable to connect to RTSMainBE to send " +
								"ThreadEvent.SEND_CACHE event.  " +
								"Performing System.exit(0)");
						// TODO - WANT TO GIVE THE USER A POPUP WITH THE 'SYSTEM ERROR' 
						// MSG....WHAT IS THE BEST SOLUTION FOR THIS ONE?
						RTSException leRTSEx2 = new RTSException(
								RTSException.SYSTEM_ERROR,
								"A System Error has occurred. \n\nPlease contact " +
								"your System Administrator.",
								"System Error");
						leRTSEx2.displayError();
						System.exit(0);
					}
					break;
				}
			// end defect 11073
			// end defect 11070

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
				// Used by the message process when the user clicks on 
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
			// defect 11073
			case ThreadEvent.DB_UP:
				caAppController.handleCommEvent(new CommEvent(
						CommEvent.DB_UP));
			// end defect 11073
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
		RTSMainGUI laRTS = new RTSMainGUI();
		Thread laRTSThread = new Thread(laRTS);
		laRTSThread.start();
	}
	
	// defect 11070
	///**
	// * Call the appropriate reboot command
	// */
	//private void reboot()
	//{
	//	UtilityMethods.doShutdown();
	//}
	// end defct 11070
	
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
				// defect 11318
				File laStdErrorFile =
					new File(
						SystemProperty.getGUILogFileName()
							+ STD_ERROR
							+ SystemProperty.getLogFileExt());
				// end defect 11318
				if (!laStdErrorFile.exists())
				{
					laStdErrorFile.createNewFile();
				}
				System.setErr(
					new DateTimePrintStream(
						new FileOutputStream(laStdErrorFile, true)));

				// defect 11318
				File laStdOutFile =
					new File(
						SystemProperty.getGUILogFileName()
							+ STD_OUT
							+ SystemProperty.getLogFileExt());
				// end defect 11318
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
	 * @throws InterruptedException 
	 *
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		// Redirect the standard error and standard out to a file when 
		// ProdStatus is production.
		redirectStdErrorOut();

		// defect 11317
		// set the look and feel to the default steel metal theme
		setLookAndFeel();
		// end defect 11317
		
		// Show the splash screen and update the splash screen with the
		// progress.
		showSplashScreen(true);
		updateSplashScreenProgress(LOADING);

		BatchLog.write(RTS_STARTED_MSG);
		// defect 11070
		//lbNormalStop = false;
		// end defect 11070

		//  Set system property so that DNS names are not cached by the 
		// java.net inet address classes
		System.setProperty(SUN_NET_INETADDR, ZERO);

		// defect 10129
		// If we have the wrong version, do not launch the gui.
		// We are moving the check from Disclaimer to here.
		if (!SystemProperty.isJavaVersionCorrect())
		{
			updateSplashScreenProgress(WRONG_JAVA_VERSION);
			try
			{
				Thread.sleep(WRONG_JAVA_WAIT_TIME);
			}
			catch (InterruptedException aeIEx)
			{
				// just ignore
			}
			System.exit(0);
		}
		// end defect 10129
		
		// Used to prune the log files. This was called from the service
		// but has been moved to RTSMain.
		updateSplashScreenProgress(PRUNNING_LOG_FILES);
		PruneLogFiles.pruneLogFiles();

		// defect 11073
		// attempt to connect to RTSMainGUI to determine if an graph
		// is already running
		updateSplashScreenProgress(CHECKING_FOR_ANOTHER_INSTANCE);
		try
		{
			fireThreadEvent(new ThreadEvent(
					ThreadEvent.CHECK_GUI_INSTANCE), "localhost",
					SystemProperty.getRemoteListenerPortGUI());

			// if no exception was thrown from the fireThreadEvent and 
			// processing continues to here, then an graph of RTSMainGUI
			// is already running...log and inform user of exit
			Log.write(Log.START_END, this, 
					PORT_IN_USE_MSG 
					+ "Performing System.exit(0)");
			RTSException leRTSEx2 = new RTSException(
					RTSException.SYSTEM_ERROR,
					"A System Error has occurred. \n\nPlease contact your " +
					"System Administrator.",
					"System Error");
			leRTSEx2.setBeep(RTSException.BEEP);
			leRTSEx2.displayError();
			System.exit(0);
		}
		catch (RTSException leRTSEx)
		{
			// another graph of RTS is NOT running
		} 
		// end defect 11073
		
		// defect 11073
		// verify that the current cache is not corrupted...if corrupted perform
		// System.exit(0)
		updateSplashScreenProgress(VERIFYING_TRXCACHE);
		if (!Transaction.isCurrentCacheValid()) 
		{
			Log.write(Log.START_END, this, "Current cache file is corrupted. "
					+ "Performing System.exit(0).");
			// TODO - WANT TO GIVE THE USER A POPUP WITH THE 'SYSTEM ERROR' 
			// STUFF....WHAT IS THE BEST SOLUTION FOR THIS ONE?
			RTSException leRTSEx = new RTSException(
					RTSException.SYSTEM_ERROR,
					"A System Error has occurred. \n\nPlease contact your " +
					"System Administrator.",
					"System Error");
			leRTSEx.setBeep(RTSException.BEEP);
			leRTSEx.displayError();
			System.exit(0);
		}
		// end defect 11073

		// Code to test if more than one graph of RTSMain is running
		// Only do this code in production, otherwise it's a pain to 
		// developers
		// defect 11071
		if (SystemProperty.getProdStatus() == SystemProperty.APP_PROD_STATUS)
		// end defect 11071
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
				// The application would still be running and the splash
				// screen showed PRUNNING_LOG_FILES since that was the 
				// last thing that was done.
				//return;
				// defect 11071
				// System.exit(0);
				// end defect 11071
			}
			catch (PortInUseException aePIUEx)
			{
				String lsErrMsgPortInUse = PORT_IN_USE_MSG;
				System.err.println(lsErrMsgPortInUse);
				Log.write(Log.START_END, this, lsErrMsgPortInUse);
				
				// The application would still be running and the splash
				// screen showed PRUNNING_LOG_FILES since that was the 
				// last thing that was done.
				//return;
				// defect 11071
				// System.exit(0);
				// end defect 11071
			}
		}

		updateSplashScreenProgress(LOADING_CACHE);

		retrieveWSData();
		// defect 11070
		//caBatchProcessor = new BatchProcessor();
		//if (SystemProperty.isClientServer())
		//{
		//	 updateSplashScreenProgress(PRINTING_BATCH);
		//	 caBatchProcessor.printReports();
		//}
		// end defect 11070

		// defect 11070
		// Added to update the RTS_WS_Status table with the
		// Workstations current status ie. Version, Version Date
		// updateSplashScreenProgress(UPDATING_WS_STATUS);
		// caBatchProcessor.updateWorkstationStatus();
		// end defect 11070
		
		// defect 11070
		///*
		//		Start the Send Cache thread
		//*/
		//updateSplashScreenProgress(START_SND_CACHE);
		//caSendCache = new SendCache();
		//caThreadSendCache = new Thread(caSendCache, SEND_CACHE_THREAD);
		//caThreadSendCache.setDaemon(true);
		//caThreadSendCache.start();
		// end defect 11070
		caAppController = new RTSApplicationController();
		caAppController.addThreadListener(this);
		// defect 11070
		//caSendCache.addCommListener(caAppController);
		// end defect 11070

		// defect 11070
		///*
		//		Launch the thread that handles main frame
		//		reports printing.
		//*/
		//updateSplashScreenProgress(STARTING_MF_REPORT_THREAD);
		//caMFReports = new MFReports();
		//caThreadMFReports = new Thread(caMFReports, MF_REPORTS_THREAD);
		//caThreadMFReports.setDaemon(true);
		//caThreadMFReports.start();
		// end defect 11070

		// defect 11070
		///*
		//	Create the Scheduler to keep track of timing
		//*/
		//updateSplashScreenProgress(STARTING_SCHEDULER);
		//Scheduler scheduler = new Scheduler();
		//scheduler.addThreadListener(this);
		//Thread laThreadScheduler =
		//	new Thread(scheduler, SCHEDULE_THREAD);
		//laThreadScheduler.setDaemon(true);
		//laThreadScheduler.start();
		// end defect 11070
	
		//		Perform the various tasks needed before starting the 
		//		application

		// defect 11073
		// Listen for any messages from outside services to shut down
		updateSplashScreenProgress(STARTING_REMOTE_LISTENER);
		RemoteListener laRemote = new RemoteListener(SystemProperty
				.getRemoteListenerPortGUI());
		// end defect 11073
		laRemote.addThreadListener(this);
		Thread laThreadRemote =
			new Thread(laRemote, REMOTE_LISTENER_THREAD);
		laThreadRemote.setDaemon(true);
		laThreadRemote.start();
		
		// defect 11073
		// Fire thread event for SendCache to process and respond for DB_UP
		updateSplashScreenProgress(CHECKING_DB_UP);
		// defect 11407
		// setup for retry in case RTSMainBE not available yet
		int liMaxRetryAttemps = SystemProperty
				.getGUISendCacheCommRetryAttempts();
		int liRetries = 0;
		boolean lbRetry = true;
		while (lbRetry)
		{
			try
			{
				fireThreadEvent(
						new ThreadEvent(ThreadEvent.SEND_CACHE),
						"localhost", SystemProperty
								.getRemoteListenerPortBE());

				// reset retry boolean to force exit of while loop
				lbRetry = false;
			}
			catch (RTSException leRTSEx)
			{
				if (liRetries >= liMaxRetryAttemps)
				{
					// Unable to connect to ServerSocket for RTSMainBE AND retry
					// count exceeded...log unsuccessful communication
					// with RTSMainBE and exit
					Log.write(Log.START_END, this,
							"Unable to connect to RTSMainBE to send "
									+ "ThreadEvent.SEND_CACHE event.  "
									+ "Performing System.exit(0)");
					// TODO - WANT TO GIVE THE USER A POPUP WITH THE 'SYSTEM
					// ERROR' MSG....WHAT IS THE BEST SOLUTION FOR THIS ONE?
					RTSException leRTSEx2 = new RTSException(
							RTSException.SYSTEM_ERROR,
							"A System Error has occurred. \n\nPlease contact your "
									+ "System Administrator.",
							"System Error");
					leRTSEx2.setBeep(RTSException.BEEP);
					leRTSEx2.displayError();
					System.exit(0);
				}
				else
				{
					// log connection failure
					Log.write(Log.START_END, this,
							"Unable to connect to RTSMainBE to send "
									+ "ThreadEvent.SEND_CACHE event.  "
									+ "Retrying: " + liRetries + " of "
									+ liMaxRetryAttemps);
					
					// increment retry count
					liRetries++;

					// Unable to connect to ServerSocket for RTSMainBE...sleep
					// and try again
					try
					{
						// give RTSMainBE time to start up
						Thread.sleep(5000);
					}
					catch (InterruptedException e)
					{
						// not reported
					}		
				}
			}
		}
		// end defect 11407
		try
		{
			// give SendCache time to respond and turn DB_UP light green
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			// not reported
		}		
		// end defect 11073

		// Start the Messenger Service
		updateSplashScreenProgress(LOADING_MESSENGER);
		caMessageHandler = new MessageHandler(this);
		caMessageHandler.addThreadListener(this);
		caThreadMessageHandler =
			new Thread(caMessageHandler, RTS_MESS_THREAD);
		caThreadMessageHandler.setDaemon(true);
		caThreadMessageHandler.start();

		//	Start the RTS Application
		updateSplashScreenProgress(LOADING_DESKTOP);
		caThreadAppController =
			new Thread(caAppController, RTS_APP_CONTROL_THREAD);
		caThreadAppController.setDaemon(true);
		caThreadAppController.start();
		// defect 11070
		showSplashScreen(false);
		// end defct 11070

		//	Wait until told to shut down RTSMain
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

		//	Stop all the threads it created and then break out of run() 
		//	method. This will effectively stop the entire RTSII 
		//	Application.
		// Do not need to set SendCache.lbShouldRestart
		// caSendCache.kill();  
		// defect 11070
		//caSendCache.setKeepRunning(false);
		// end defect 11070
		//caSendCache.restart();

		synchronized (caAppController)
		{
			caAppController.notify();
		}

		MessageHandler.setKeepRunning(false);
		caThreadMessageHandler = null;

		// shutdown MFReport
		// defect 11070
		// MFReports.setKeepRunning(false);
		//caMFReports.notify();
		//caThreadMFReports = null;
		//caMFReports = null;
		// end defect 11070
		// end defect 8405

		caAppController.finalize();
		caAppController = null;
		caThreadAppController = null;
		// defect 11070
		//caSendCache = null;
		//caThreadSendCache = null;
		
		//scheduler = null;
		//laThreadScheduler = null;
		// end defect 11070
		laRemote = null;
		laThreadRemote = null;

		BatchLog.write(RTS_STOPPED_MSG);

		System.exit(0);
	}
	
	/**
	 * Set the look and feel to the default steel metal theme.
	 */
	private void setLookAndFeel()
	{
		try
		{
			MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException aeULFEx)
		{
			System.err.println(ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeULFEx.getMessage());
			Log.write(Log.START_END, this, ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeULFEx.getMessage());
		}
	}
	
	/**
	 * This is the static method that is used to show and remove the splash
	 * screen. The boolean that is passed determines if the splash screen is
	 * shown or not.
	 * 
	 * @param abShowScreen
	 *            boolean
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