package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.data.TransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;
import com.txdot.isd.rts.services.util.event.CommEvent;
import com.txdot.isd.rts.services.util.event.CommListener;
import com.txdot.isd.rts.services.util.event.ThreadEvent;

/*
 *
 * SendCache.java
 *
 * (c) Texas Department of Transportation  2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/07/2002	Fixed CQU100004581. Getting null date object
 *							back from database and failing on it.
 * 							Set comparision date to RTSDate(0) in that 
 *							case. Add much more logging to the process 
 *							so support can see activity
 * Ray Rowehl	08/12/2002	Fixed CQU100004583.  Add logging to Log when 
 *							db is up.
 * RHicks      	09/11/2002  Update logging & exit thread on RTSException
 * RHicks      	09/12/2002  Fix last trans date to use latest date
 * SGovindappa 	09/19/2002 	Fixing Defect 4732. Added more log on 
 *							information for sendcache in run method and 
 *                          and made changes to log "sleeping while 
 *							waiting for server to come up" message 
 *                          only once when waiting for DB to come up.
 * SGovindappa 	09/27/2002 	Fixing Defect 4732. Added more log on info
 *							for sendcache in run method and made changes
 *							to log "Waiting for DB to come back" message 
 *                          only once when waiting for DB to come up.
 * Richard, Mike, Govindappa 
 * 			 	09/30/2002  Fixing 4800 Created svCacheQueue to post all 
 *							the transactions from trxCache to prevent
 *                          the exposure of losing transactions done
 *							during posting of Trans from sendCache. 
 *							Changed isDBUp() & isServerUp() not to fire
 *							com events to prevent application showing DB 
 *							up when transactions are being restored.
 * K. Harrell	12/04/2002  Remove code for 4581; Implementation of 4961
 *							Queries no longer return null.
 * K. Harrell	12/12/2002  CQU100005175 Delete files from TRXCACHE
 * 							older than 7 days from current date vs 7 
 * 							days from lastTransDate
 * Ray Rowehl	07/12/2003  Use Constants for PING module and function
 *							modify isDBUp() and isServerUp()
 *							defect 6110
 * K. Harrell	10/10/2003	Reorganized for readability/recoverability
 *							Removed call for RTS_LOG_FUNC_TRANS
 *							Removed call to isServerUp
 *							Added detection/retry code for -911 deadlock
 *							Enhanced Main to have start/end date/time
 *							add logSendCacheTrans(),processArguments(),
 *							  postSendCacheTrans(),purgeCache(),
 *							  qryLastTransDate(),resetInitialStart(),
 *							  waitForDBServerDown(),waitForDBServerUp(),
 *							  logSendCache(),getTransCd,processDateTime
 *							modify run(),isDBUp(),SendCache(),
 *							  isServerUp(), main() 
 *							deprecate isServerUp() 
 *							Reorganized variable definitions
 *							Defect 6614 Ver 5.1.5 Fix 1
 * K. Harrell	10/20/2003	Sendcache should retry on unknown error.
 *							methods: main(),postSendCacheTrans()
 *							defect 6571 Ver 5.1.5 Fix 1 
 * K. Harrell	10/27/2003  Remove synch in run(), use HTML tags in doc.
 *							add resetErrorRetryCount and assoc class 
 *							  variables
 *							defect 6614 Ver 5.1.5 Fix 1 (cont'd) 
 * K. Harrell	10/29/2003	Added getters/setters
 *							add setKeepRunning(),setProcessing(),
 *							  setShouldRestart(), isKeepRunning(),
 *							  isShouldRestart().
 *							Removed restart()
 *							Defect 6614 Ver 5.1.5 Fix 1 (cont'd) 
 * K. Harrell	12/17/2003  Do not exit on errors from 
 *							qryLastTransDate()
 *							modifyd waitForDBServerUp()
 * 							  postSendCacheTrans()
 *							defect 6749 Ver 5.1.5 Fix 1a
 * K. Harrell	01/30/2004	On initial startup, Sendcache can issue 
 *							fireCommEvent for DBUp before the Listeners 
 *							are ready. Moved sleep before fireCommEvent.
 *							modify postSendCacheTrans() 
 *							defect 6863. Ver 5.1.5 Fix 3
 * K Harrell	11/24/2004	Max date/time returned in TransactionData 
 *							object vs RTSDate to eliminate problems with
 *							TZ issues between server/client. 
 *							modify qryLastTransDate() 
 *							defect 7747 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Fall out from Transaction change
 * 							cacheQueue became svCacheQueue
 * 							modify postSendCacheTrans()
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		02/16/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * K Harrell	10/07/2005	Modify to use Transaction.getCacheQueue() 
 * 							vs. svCacheQueue 
 * 							modify postSendCacheTrans() 
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	11/01/2008	Added translation for new processes:  
 * 							"Update Virtual Inventory TransTime" and 
 * 							  "Reset In Process" 
 * 							modify getProcName() 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	08/11/2009	Modifications for new DB2 Driver 
 * 							modify main(), postSendCacheTrans() 
 * 							defect 10164 Ver Defect_POS_E'   
 * K Harrell	10/20/2010	Include check for SQLCODE=-911
 * 							modify main() 
 * 							defect 10636 Ver 6.6.0     
 * R Pilon		04/19/2012	Thread Messenger changes for communication between 
 * 							  background and gui.  
 * 							delete cbInitialStart, resetInitialStart()
 * 							add extends ThreadMessenger, cbRTSAppControllDBUp,
 * 							  isRTSAppControllerDBUp(), setRTSAppControllerDBUp()
 * 							modify postSendCacheTrans(), waitForDBServerDown(), 
 * 							  waitForDBServerUp()
 * 							defect 11073 Ver 7.0.0
 * R Pilon		05/14/2012	Surround fireThreadEvent with try/catch and exit
 * 							  when exception caught.
 * 							modify postSendCacheTrans()
 * 							defect 11073 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
 * SendCache runs on its own thread and sends cached transactions to the 
 * database when a connection to the DB is lost and then reestablished.
 *
 * SendCache will exit under the following conditions:
 *   1) Unexpected error from PostTrans
 *	    Examples that we've seen: 
 *		 a) Error on ProcessInventoryData (ErrorMsg 11)
 *		 b) Error on updTransactionHeader when no RTS_TRANS_HDR record 
 *          exists
 *	 2) Exceed retry limit on DeadLock from PostTrans
 *	 3) Error reading Transaction Cache
 *	 4) Exceeds max error limit of 10 on qryLastTransDate in 
 *		waitForDBServerUp()
 *
 * @version	POS_700			05/14/2012
 * @author 	Michael Abernethy
 * @author	Kathy Harrell
 * <br>Creation Date:		09/05/2001 14:11:01 
 */
public class SendCache extends ThreadMessenger implements Runnable
{
	// Constants
	private final static int MILLISECONDS_PER_SECOND = 1000;
	private final static int WAIT_TIME = 30;
	/**
	* Maximum number of posttrans retries after deadlock.
	*/
	private final static int DEADLOCK_LIMIT = 3;
	/**
	* Maximum number of qryLastTrans retries 
	*/
	private final static int QRYLASTTRANSERROR_LIMIT = 10;
	/**
	* End Date passed as parameter.
	*/
	private final static int END_DATE = 1;
	/**
	* Start Date passed as parameter.
	*/
	private final static int START_DATE = 0;
	/**
	* Logging request from Application SendCache vs. Direct.
	*/
	private final static int SENDCACHE = 0;
	/**
	* Logging request from Direct vs Application SendCache.
	*/
	private final static int DIRECT = 1;
	/**
	* Dashes to be logged between SendCache starts.
	*/
	private final static String DASHES =
		"========================================";
	/**
	* Sendcache should keep running indicator. 
	*/
	private volatile boolean cbKeepRunning = true;
	// defect 11073
//	/**
//	* Sendcache startup indicator. 
//	*/
//	private static boolean cbInitialStart = true;
	// end defect 11073
	/**
	* Sleep message logged indicator. 
	*/
	private boolean cbSleepMsgLoggedIndi = false;
	/**
	* DB Down message logged indicator. 
	*/
	private boolean cbDBDownLoggedIndi = false;
	/** 
	* SendCache should restart processing indicator.
	*/
	private volatile boolean cbShouldRestart = false;
	/**
	* SendCache is currently posting transactions indicator. 
	*/
	private static volatile boolean cbProcessing = false;
	// defect 11073
	/**
	* RTSApplicationController has been informed the database available. 
	*/
	private boolean cbRTSAppControllerDBUp = false;
	// end defect 11073
	
	// Other
	private Vector cvListeners;
	/**
	* Count of Deadlock Retries.
	*/
	private static int ciDeadLockRetry = 0;
	/**
	* Count of Error Retries.
	*/
	private static int ciErrorRetry = 0;
	/**
	* Start date from DB or from Direct.
	*/
	private static RTSDate caRTSDateStart;
	/**
	* End date created from Direct parameter
	*/
	private static RTSDate caRTSDateEnd;

	/**
	 * Creates a SendCache.
	 */
	public SendCache()
	{
		super();
		cvListeners = new Vector();
	}

	/**
	 * addCommListener - Adds a CommListener.
	 * 
	 * @param aaCListener CommListener
	 */
	public void addCommListener(CommListener aaCListener)
	{
		if (!cvListeners.contains(aaCListener))
		{
			cvListeners.add(aaCListener);
		}
	}

	/**
	 * fireCommEvent - Fires a CommEvent
	 * 
	 * @param aaCE CommEvent
	 */
	private void fireCommEvent(CommEvent aaCE)
	{
		for (int i = 0; i < cvListeners.size(); i++)
		{
			((CommListener) cvListeners.get(i)).handleCommEvent(aaCE);
		}
	}

	/**
	 * getProcName - Translates Procname for TransactionCacheData.
	 * 
	 * @param aiProcName int
	 * @return String 
	 */
	private static String getProcName(int aiProcName)
	{
		switch (aiProcName)
		{
			case TransactionCacheData.DELETE :
				return "DELETE";
			case TransactionCacheData.INSERT :
				return "INSERT";
			case TransactionCacheData.UPDATE :
				return "UPDATE";
			case TransactionCacheData.VOID :
				return "VOID";
				// defect 9831 
			case TransactionCacheData.UPDVITRANSTIME :
				return "UPVITM";
			case TransactionCacheData.RESETINPROCESS :
				return "RSTINP";
				// end defect 9831 
			default :
				return "UNKNOWN";
		}
	}

	/**
	 * Method to derive transcd from the Transaction Cache Data Object.
	 * 
	 * @param aaTransCacheData TransactionCacheData
	 * @return String
	 */
	private static String getTransCd(TransactionCacheData aaTransCacheData)
	{
		String lsTransCd = null;
		Object laDBObj = aaTransCacheData.getObj();
		if (laDBObj instanceof TransactionData)
		{
			TransactionData laTransData = (TransactionData) laDBObj;
			lsTransCd = laTransData.getTransCd();
		}
		return lsTransCd;
	}

	/**
	 * Pings the DB and determines if the DB connection is up
	 * 
	 * @return boolean
	 */
	private boolean isDBUp()
	{
		try
		{
			Ping laP = new Ping();
			laP.setCheckDB(true);
			// defect 6110
			// use constants for PING
			Ping laPing =
				(Ping) Comm.sendToServer(
					GeneralConstant.PING,
					GeneralConstant.PING,
					laP);
			// end defect 6110
			if (laPing.isOk())
			{
				logSendCache(SENDCACHE, "Notified DB is up.");
				Log.write(
					Log.SQL_EXCP,
					this,
					"SendCache notified DB is up.");
				cbDBDownLoggedIndi = false;
				return true;
			}
			else
			{
				if (!cbDBDownLoggedIndi)
				{
					cbDBDownLoggedIndi = true;
					logSendCache(SENDCACHE, "Notified DB is down.");
				}
				return false;
			}
		}
		catch (RTSException aeEx)
		{
			if (!cbDBDownLoggedIndi)
			{
				cbDBDownLoggedIndi = true;
				logSendCache(SENDCACHE, "Notified DB is down.");
			}
			return false;
		}
	}

	/**
	 * Returns whether SendCache should keep running.
	 * 
	 * @return boolean
	 */
	public boolean isKeepRunning()
	{
		return cbKeepRunning;
	}

	/**
	 * Returns whether SendCache is currently processing.
	 * 
	 * @return boolean
	 */
	public static boolean isProcessing()
	{
		return cbProcessing;
	}

	/**
	 * Returns whether RTSApplicationController has been informed the database
	 * available.
	 * 
	 * @return boolean
	 */
	public boolean isRTSAppControllerDBUp()
	{
		return cbRTSAppControllerDBUp;
	}
	
	/**
	 * Returns whether SendCache should restart
	 * 
	 * @return boolean
	 */
	public boolean isShouldRestart()
	{
		return cbShouldRestart;
	}

	/**
	 * Writes to SendCacheLog and console.
	 * If Direct, prefaces with "Direct:".
	 * 
	 * @param aiSource int 
	 * @param asMessage String
	 */
	private static void logSendCache(int aiSource, String asMessage)
	{
		if (aiSource == DIRECT)
		{
			asMessage = "Direct: " + asMessage;
		}
		RTSDate laRTSDate = RTSDate.getCurrentDate();
		System.out.println(
			laRTSDate
				+ " - "
				+ laRTSDate.getClockTime()
				+ " - "
				+ asMessage);
		SendCacheLog.write(asMessage);
	}

	/**
	 * Method to log transaction information to SendCacheLog.
	 * 
	 * @param aiSource int
	 * @param avTemp Vector
	 * @param asTransCd String
	 */
	private static void logSendCacheTrans(
		int aiSource,
		Vector avTemp,
		String asTransCd)
	{
		TransactionCacheData laTransData =
			(TransactionCacheData) avTemp.get(0);
		String lsClassName = laTransData.getObj().getClass().getName();
		StringTokenizer laST1 = new StringTokenizer(lsClassName, ".");
		while (laST1.hasMoreTokens())
		{
			lsClassName = laST1.nextToken();
		}
		String lsProcName = getProcName(laTransData.getProcName());
		String lsTransString =
			UtilityMethods.addPaddingRight(lsClassName, 22, " ")
				+ " UOW "
				+ laTransData.getDateTime().getAMDate()
				+ " "
				+ laTransData.getDateTime().get24HrTime();
		lsTransString =
			lsTransString
				+ "    "
				+ UtilityMethods.addPaddingRight(lsProcName, 8, " ");
		if (asTransCd != null)
		{
			lsTransString = lsTransString + asTransCd;
		}
		logSendCache(aiSource, lsTransString);
	}

	/**
	 * SendCache standalone method.
	 * Rewritten for defect 6614 to include the following features: 
	 * <ul>
	 *	<li>Accept EndDate EndTime as parameters 
	 *	<li>Retry on SQL0911 / -911 (Deadlock)
	 *	<li>Retry once on other errors (e.g. HTTP error)
	 *  </ul>
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		Vector lvTrans = new Vector();
		Vector lvTransToSend = new Vector();
		TransactionCacheData laTransCacheData = null;
		if (aarrArgs.length == 0)
		{
			logSendCache(
				DIRECT,
				"Usage: SendCash <StartAMDate>"
					+ " [StartTime] [EndAMDate] [EndTime]");
			logSendCache(DIRECT, "  Exiting.");
			System.exit(0);
		}
		processArguments(aarrArgs);
		// Get cached transaction between caRTSDateStart and caRTSDateEnd
		// (inclusive)
		lvTransToSend =
			Transaction.directSendCacheRead(
				caRTSDateStart,
				caRTSDateEnd);
		logSendCache(
			DIRECT,
			lvTransToSend.size() + " cached transactions to process.");
		while (lvTransToSend.size() > 0)
		{
			try
			{
				if (ciDeadLockRetry == 0 && ciErrorRetry == 0)
				{
					lvTrans = (java.util.Vector) lvTransToSend.get(0);
					String lsTransCd = null;
					for (int k = 0;(k < lvTrans.size()); k++)
					{
						laTransCacheData =
							(TransactionCacheData) lvTrans.get(k);
						laTransCacheData.setFromSendCache(true);
						if (lsTransCd == null)
						{
							lsTransCd = getTransCd(laTransCacheData);
						}
					}
					// Write trans details to Console/Log
					logSendCacheTrans(DIRECT, lvTrans, lsTransCd);
					logSendCache(
						DIRECT,
						"  " + lvTrans.size() + " cached objects.");
				}
				Object laResult = Transaction.postTrans(lvTrans);
				// Exit on Server or DB Down 
				if (laResult != null
					&& laResult instanceof Boolean
					&& !((Boolean) laResult).booleanValue())
				{
					logSendCache(DIRECT, "Exiting. Server or DB Down.");
					System.exit(0);
				}
				lvTransToSend.remove(0);
				logSendCache(
					DIRECT,
					"  Transaction posted" + " successfully.");
				resetErrorRetryCount(); // Reset error counts
			}
			catch (RTSException aeEx)
			{
				// defect 10164 
				//if (aeEx.getDetailMsg().indexOf("SQL0803") > -1)
				if (aeEx
					.getDetailMsg()
					.indexOf(CommonConstant.DUPLICATE_KEY_EXCEPTION)
					> -1)
				{
					// end defect 10164 
					logSendCache(
						DIRECT,
						"  Transaction is already" + " posted.");
					lvTransToSend.remove(0);
					resetErrorRetryCount(); // Reset error counts
				} // end SQL0803
				else
				{
					// defect 10636
					if (aeEx.getDetailMsg().indexOf("SQL0911") > -1
						|| aeEx.getDetailMsg().indexOf("SQLCODE=-911")
							> -1)
					{
						// end defect 10636 
						if (ciDeadLockRetry == 0)
						{
							logSendCache(
								DIRECT,
								"  Transaction"
									+ " ROLLBACK due to DEADLOCK/TIMEOUT.");
						}
						try
						{
							Thread.sleep(MILLISECONDS_PER_SECOND);
						}
						catch (InterruptedException leIE)
						{
							logSendCache(
								DIRECT,
								"Interrupted while"
									+ " sleeping. SendCache was waiting\n"
									+ aeEx.getMessage());
						}
						if (ciDeadLockRetry >= DEADLOCK_LIMIT)
						{
							logSendCache(
								DIRECT,
								"Exiting; Deadlock"
									+ " limit exceeded.");
							System.exit(0);
						}
						ciDeadLockRetry = ciDeadLockRetry + 1;
						logSendCache(
							DIRECT,
							"  Retrying. Attempt "
								+ ciDeadLockRetry
								+ ".");
					} // End SQL0911  
					else
					{
						if (ciErrorRetry == 0)
						{
							logSendCache(
								DIRECT,
								"  Encountered error"
									+ " while posting to DB\n"
									+ aeEx.getMessage()
									+ "\n"
									+ aeEx.getDetailMsg()
									+ "\nError Code: "
									+ aeEx.getCode());
							ciErrorRetry = ciErrorRetry + 1;
							logSendCache(
								DIRECT,
								"  Retrying. Attempt "
									+ ciErrorRetry
									+ ".");
							try
							{
								Thread.sleep(MILLISECONDS_PER_SECOND);
							}
							catch (InterruptedException leIE)
							{
								logSendCache(
									DIRECT,
									"Interrupted while sleeping."
										+ " SendCache was waiting\n"
										+ leIE.getMessage());
							}
						}
						else // ciErrorRetry != 0 
							{
							try
							{
								FileOutputStream lpfsFOStream =
									new FileOutputStream(
										"sndcach.log",
										true);
								PrintWriter laOut =
									new PrintWriter(lpfsFOStream);
								aeEx.printStackTrace(laOut);
							}
							catch (FileNotFoundException aeEX)
							{
							}
							logSendCache(DIRECT, "  Exiting.");
							System.exit(0);
						}
					} // Not SQL0911; Not SQL0803 
				} // Not SQL0803
			}
		}
		logSendCache(DIRECT, "Processing complete.");
	}

	/**
	 * Posts transactions from Transaction.svCacheQueue
	 * Processes:
	 * <ul>
	 *  <li>Determines last posted transaction date/time.
	 * <ul>
	 *    <li>On error, set shouldRestart = true. Return.
	 *  </ul>
	  
	 *  <li>Reads from TRXCache since last posted trans, populates 
	 * 	Transaction.svCacheQueue.
	 *  <li>Posts transactions while !isDBReady  
	 * 	!(Transaction.svCacheQueue.size =0 && isDBUp).
	 *	<ul>
	 *    <li>If DBDown, setProcessing(false). setShouldRestart(true).
	 * 		  Return.
	 *    <li>If -803, remove transaction. Continue.
	 *    <li>If -911, retry to DEADLOCK_LIMIT.
	 *    <li>If other exception, retry once. 
	 *    <li>After required retries, set cbKeepRunning=false. Return.
	 * 		  Sendcache will exit. 
	 *  </ul>
	 *  <li>When Transaction.svCacheQueue.size() = 0, fires 
	 * 		CommEvent.DB_UP.
	 *  </ul>
	 *  
	 * @throws InterruptedException
	 * @throws RTSException
	 */
	private void postSendCacheTrans()
		throws InterruptedException, RTSException
	{
		setProcessing(true); // Sendcache is processing transactions
		boolean lbFiredDBUp = false;
		// Sendcache has not yet Fired DB_UP
		Vector lvTrans = new Vector();
		// Vector of cached transactions 	 
		TransactionCacheData laTransCacheData = null;
		try
		{
			// Retrieve cached transactions since last successful 
			// posting
			// defect 7705 && 7885
			Transaction.getCacheQueue().clear();
			// end defect 7005 && 7885
			Transaction.readFromCache(caRTSDateStart);
			logSendCache(
				SENDCACHE,
				Transaction.getCacheQueue().size()
					+ " cached transactions to process.");
			// Continue processing until both
			// RTSApplicationController.isDBUp() && 
			// Transaction.cacheQueue.size()=0
			// defect 11073
//			while (!(RTSApplicationController.isDBReady()))
			// Continue processing until RTSApplicationController has been 
			// notified that the database is up
			while (!(cbRTSAppControllerDBUp))
			// end defect 11073
			{
				if (Transaction.getCacheQueue().size() == 0)
				{
					// defect 6863
					// Moved sleep before fireCommEvent to allow time
					// for cvListeners to become available at startup. 
					Thread.sleep(MILLISECONDS_PER_SECOND);

					if (!(lbFiredDBUp))
						// has not yet fired CommEvent for DB_UP
					{
						fireCommEvent(new CommEvent(CommEvent.DB_UP));
						// defect 11073
						// fire a thread event to signal database is available
						logSendCache(SENDCACHE,
								"Firing ThreadEvent.DB_UP event signaling database up.");
						// TODO - ADD NEW LOGIC TO GET THE HOSTNAME FROM
						// SOMEWHERE....SHOULD NOT BE HARDCODED AS localhost!!!
						try 
						{
							fireThreadEvent(new ThreadEvent(
									ThreadEvent.DB_UP), "localhost",
									SystemProperty.getRemoteListenerPortGUI()); 
						} 
						catch (RTSException leRTSEx)
						{
							// RTSMainGUI not running, log and continue 
							// processing
							logSendCache(SENDCACHE,
									"  Unable to connect to RTSMainGUI to send "
											+ "ThreadEvent.DB_UP event.");
						}
						setRTSAppControllerDBUp(true);
						// end defect 11073
						lbFiredDBUp = true;
						setShouldRestart(false);
					}
					//	Thread.sleep(MILLISECONDS_PER_SECOND);
					// end defect 6863
				}
				else
				{
					// Pull from Transaction.cacheQueue if not retrying
					if (ciDeadLockRetry == 0 && ciErrorRetry == 0)
					{
						lvTrans =
							(Vector) Transaction.getCacheQueue().get(0);
						String lsTransCd = null;
						int liProcName = 0;
						for (int k = 0;(k < lvTrans.size()); k++)
						{
							laTransCacheData =
								(TransactionCacheData) lvTrans.get(k);
							laTransCacheData.setFromSendCache(true);
							// for postTrans
							liProcName = laTransCacheData.getProcName();
							// Insert, Update, etc.
							if (lsTransCd == null)
								// Only get once from lvTrans
							{
								lsTransCd =
									getTransCd(laTransCacheData);
							}
						}
						logSendCacheTrans(
							SENDCACHE,
							lvTrans,
							lsTransCd);
						logSendCache(
							SENDCACHE,
							"  " + lvTrans.size() + " cache objects.");
					}
					try
					{
						Object laResult =
							Transaction.postTrans(lvTrans);
						// Break & return on Server or DB Down 
						if (laResult != null
							&& laResult instanceof Boolean
							&& !((Boolean) laResult).booleanValue())
						{
							setProcessing(false);
							setShouldRestart(true);
							logSendCache(
								SENDCACHE,
								"  Server or DB Down error.");
							break;
						}
						// Successful Posting 
						Transaction.getCacheQueue().remove(0);
						// remove from cacheQueue
						logSendCache(
							SENDCACHE,
							"  Transaction posted successfully.");
						resetErrorRetryCount(); // reset error counts
					}
					catch (RTSException aeEx)
					{
						//  If duplicate, ignore and continue
						// defect 10164 
						//if (aeEx.getDetailMsg().indexOf("SQL0803")
						if (aeEx
							.getDetailMsg()
							.indexOf(
								CommonConstant.DUPLICATE_KEY_EXCEPTION)
							> -1)
						{
							// end defect 10164 

							logSendCache(
								SENDCACHE,
								"  Transaction already posted.");
							Transaction.getCacheQueue().remove(0);
							// end defect 7705
							resetErrorRetryCount();
							// Reset error counts
						}
						// If rollback, do not remove from cacheQueue,
						// sleep && retry	
						else
						{
							if (aeEx.getDetailMsg().indexOf("SQL0911")
								> -1)
							{
								if (ciDeadLockRetry == 0)
								{
									logSendCache(
										SENDCACHE,
										"  Transaction ROLLBACK due"
											+ " to DEADLOCK/TIMEOUT.");
								}
								Thread.sleep(MILLISECONDS_PER_SECOND);
								ciDeadLockRetry = ciDeadLockRetry + 1;
								if (ciDeadLockRetry > DEADLOCK_LIMIT)
								{
									logSendCache(
										SENDCACHE,
										"Exiting; Deadlock"
											+ " limit exceeded.");
									throw aeEx;
								}
								logSendCache(
									SENDCACHE,
									"  Retrying: Attempt "
										+ ciDeadLockRetry
										+ ".");
							} // 911 
							else
							{ // Not 803 or 911
								logSendCache(
									SENDCACHE,
									"  Encountered error while"
										+ " posting to DB\n"
										+ aeEx.getMessage()
										+ "\n"
										+ aeEx.getDetailMsg()
										+ "\nError Code: "
										+ aeEx.getCode());
								if (ciErrorRetry == 0)
								{
									Thread.sleep(
										MILLISECONDS_PER_SECOND);
									ciErrorRetry = ciErrorRetry + 1;
								} // ciErrorRetry = 0 
								else
								{
									logSendCache(SENDCACHE, "Exiting.");
									setKeepRunning(false);
									break;
								} // ciErrorRetry !=0 
							} // Not 803 or 911 
						} // Not 803
					}
				}
			}
		}
		catch (RTSException aeEX)
		{
			throw aeEX;
		}
		// shouldRestart set on ServerDown or DBDown
		// cbKeepRunning reset on unrecoverable errors
		// (max retries on 911, retry on other error) 
		if ((!isShouldRestart()) && isKeepRunning())
			// No error during processing
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"SendCache processing complete.");
			logSendCache(
				SENDCACHE,
				"SendCache processing complete." + " Sleeping.");
		}
		return;
	}

	/**
	 * This method parses the arguments provided with DirectSendCache
	 * into two RTSDates, lastTransDate and endTransDate.  These dates
	 * will be used to read from transaction cache inclusively.
	 *  
	 * @param aarrArgs String[]
	 */
	private static void processArguments(String[] aarrArgs)
	{
		String lsStartDate = "";
		String lsStartTime = "";
		String lsEndDate = "";
		String lsEndTime = "";
		// Get Begin Date/Time
		lsStartDate = (String) aarrArgs[0];
		if (aarrArgs.length > 1)
		{
			lsStartTime = (String) aarrArgs[1];
		}
		caRTSDateStart =
			processDateTime(lsStartDate, lsStartTime, START_DATE);
		String lsInitStmt =
			"Initiated with argument(s) "
				+ lsStartDate
				+ " "
				+ lsStartTime;
		// Get End Date/Time
		if (aarrArgs.length > 2)
		{
			lsEndDate = (String) aarrArgs[2];
		}
		if (aarrArgs.length > 3)
		{
			lsEndTime = (String) aarrArgs[3];
		}
		lsInitStmt = lsInitStmt + " " + lsEndDate + " " + lsEndTime;
		logSendCache(DIRECT, DASHES);
		logSendCache(DIRECT, lsInitStmt);
		caRTSDateEnd = processDateTime(lsEndDate, lsEndTime, END_DATE);
	}

	/**
	 * Returns RTSDate given input of Date and Time.
	 * 
	 * @param asDate String 
	 * @param asTime String
	 * @param aiDateType int
	 * 
	 * @return RTSDate
	 */
	private static RTSDate processDateTime(
		String asDate,
		String asTime,
		int aiDateType)
	{
		int liAMDate = 0;
		int liAMHour = 0;
		int liAMMin = 1;
		int liAMSec = 0;
		if (aiDateType == END_DATE)
		{
			if (asDate.equals(""))
			{
				asDate = "99999";
			}
			if (asTime.equals(""))
			{
				asTime = "235959";
			}
		}
		else // dateType = START_DATE
			{
			if (asTime.equals(""))
			{
				asTime = "000100";
			}
		}
		// Process date / time
		try
		{
			asTime = UtilityMethods.addPadding(asTime, 6, "0");
			liAMDate = Integer.parseInt(asDate);
			liAMHour = Integer.parseInt(asTime.substring(0, 2));
			liAMMin = Integer.parseInt(asTime.substring(2, 4));
			liAMSec = Integer.parseInt(asTime.substring(4, 6));
		}
		catch (NumberFormatException aeNFE)
		{
			logSendCache(
				DIRECT,
				"Direct Send Cache encountered error parsing date /"
					+ " time parameters");
			logSendCache(DIRECT, aeNFE.getMessage());
			System.exit(0);
		}
		RTSDate laRTSDate = new RTSDate(RTSDate.AMDATE, liAMDate);
		laRTSDate.setHour(liAMHour);
		laRTSDate.setMinute(liAMMin);
		laRTSDate.setSecond(liAMSec);
		return laRTSDate;
	}

	/**
	 * Purge cache files older than 7 days.
	 */
	private void purgeCache()
	{
		int liPurgedFiles = 0; // Count of files to be purged
		RTSDate laPurgeRTSDate =
			RTSDate.getCurrentDate().add(RTSDate.DATE, -7);
		// Get directory listing 
		File laDir = new File(SystemProperty.getTransactionDirectory());
		File[] larrFiles = laDir.listFiles();
		for (int i = 0; i < larrFiles.length; i++)
		{
			// defect 6614
			if (larrFiles[i].isDirectory())
			{
				continue;
			}
			// end defect 6614
			RTSDate laLastModRTSDate =
				new RTSDate(larrFiles[i].lastModified());
			if (laLastModRTSDate.compareTo(laPurgeRTSDate) < 0)
			{
				liPurgedFiles++;
				larrFiles[i].delete();
			}
		}
		logSendCache(
			SENDCACHE,
			"Purged " + liPurgedFiles + " cache files.");
	}

	/**
	 * Query the last transaction date/time for the Ofcissuanceno,
	 * SubstaId, WorkstationId.
	 * 
	 * @return RTSDate
	 * @throws RTSException
	 */
	private RTSDate qryLastTransDate() throws RTSException
	{
		// defect 7747
		// Receive laTransactionData vs. RTSDate to derive Max 
		// TransAMDate/TransTime
		HashMap lhmMap = new HashMap();
		lhmMap.put(
			"OFC",
			new Integer(SystemProperty.getOfficeIssuanceNo()));
		lhmMap.put(
			"SUB",
			new Integer(SystemProperty.getSubStationId()));
		lhmMap.put(
			"WSID",
			new Integer(SystemProperty.getWorkStationId()));
		try
		{
			TransactionData laTransactionData =
				(TransactionData) Comm.sendToServer(
					GeneralConstant.SYSTEMCONTROLBATCH,
					SystemControlBatchConstant.MAX_TRANS,
					lhmMap);
			int liTransAMDate = laTransactionData.getTransAMDate();
			int liTransTime = laTransactionData.getTransTime();
			RTSDate laLastRTSDate =
				new RTSDate(RTSDate.AMDATE, liTransAMDate);
			laLastRTSDate.setTime(liTransTime);
			logSendCache(
				SENDCACHE,
				"Selected Trans Time: "
					+ laLastRTSDate.getTimestamp()
					+ "00");
			return laLastRTSDate;
			// end defect 7747 
		}
		catch (RTSException aeEx)
		{
			throw aeEx;
		}
	}

	/**
	 * Removes the CommListener.
	 * 
	 * @param aaCListener CommListener
	 */
	public void removeCommListener(CommListener aaCListener)
	{
		cvListeners.remove(aaCListener);
	}

	/**
	 * Reset the Error Retry Count.
	 */
	private static void resetErrorRetryCount()
	{
		ciDeadLockRetry = 0; // Count of Deadlock Retries
		ciErrorRetry = 0; // Count of Error Retries
	}

	// defect 11073
	// TODO - HOW DOES THIS NEED TO BE HANDLED NOW?  THIS PREVENTS RTSMainBE 
	// FROM STARTING AS IT WILL WAIT UNTIL THIS BOOLEAN IS TRUE TO CONTINUE TO
	// LOAD THE MAINFRAME REPORTS AND SCHEDULER CLASSES.
//	/**
//	 * Reset initialization parameters.
//	 */
//	private void resetInitialStart()
//	{
//		if (cbInitialStart)
//		{
//			RTSMainBE.cbStartApp = true; // RTSMainBE can continue
//			cbInitialStart = false;
//		}
//	}
	// end defect 11073

	/**
	 * Process though transaction cache to make sure it is all posted
	 * to the database.  Also, the "old" cache files are deleted.
	 *
	 * <p>When an object implementing interface <code>Runnable</code>
	 * is used to create a thread, starting the thread causes the
	 * object's <code>run</code> method to be called in that separately
	 * executing thread. 
	 * <p>
	 * The general contract of the method <code>run</code> is that it
	 * may take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		logSendCache(SENDCACHE, DASHES);
		logSendCache(SENDCACHE, "SendCache initated at RTS startup.");
		// Purge cache files older than 7 days
		purgeCache();
		//
		while (isKeepRunning())
		{
			try
			{
				setShouldRestart(false);
				// Set to true when app encounters DB Down
				waitForDBServerUp(); // Wait until DB Up
				postSendCacheTrans(); // Post transactions to DB
				waitForDBServerDown();
				// Wait until DB Down || notified to start again					
			}
			catch (RTSException aeEx)
			{
				logSendCache(
					SENDCACHE,
					"Error encountered while operating\n"
						+ aeEx.getDetailMsg());
				setKeepRunning(false);
			}
			catch (InterruptedException aeIE)
			{
				logSendCache(
					SENDCACHE,
					"Interrupted while sleeping."
						+ " SendCache was waiting\n"
						+ aeIE.getMessage());
			}
		}
		logSendCache(SENDCACHE, "Exiting; isKeepRunning() = false;");
		return;
	}

	/**
	 * Sets the indicator to determine if SendCache should keep running.
	 * If false, the SendCache thread will stop.
	 * 
	 * @param abKeepRunning boolean
	 */
	public void setKeepRunning(boolean abKeepRunning)
	{
		cbKeepRunning = abKeepRunning;
	}

	/**
	 * Sets the indicator to denote that SendCache is processing
	 * transactions.
	 * 
	 * @param abProcessing boolean
	 */
	public static void setProcessing(boolean abProcessing)
	{
		cbProcessing = abProcessing;
	}

	/**
	 * Sets the indicator to denote that RTSApplicationController has been
	 * informed the database available.
	 * 
	 * @param abRTSAppControllerDBUp
	 *            boolean
	 */
	public void setRTSAppControllerDBUp(
			boolean abRTSAppControllerDBUp)
	{
		cbRTSAppControllerDBUp = abRTSAppControllerDBUp;
	}
	/**
	 * Set the indicator to determine if SendCache should Restart
	 * 
	 * @param aShouldRestart boolean
	 */
	public void setShouldRestart(boolean abShouldRestart)
	{
		if (abShouldRestart) // if turning on
		{
			if (!isProcessing())
				// only turn on if SendCache not currently processing
			{
				cbShouldRestart = abShouldRestart;
			}
		}
		else // if turning off 
			{
			cbShouldRestart = abShouldRestart;
		}
	}

	/**
	 * Sleep while DB Server Up or until notified to restart.
	 * 
	 * @throws InterruptedException
	 */
	private void waitForDBServerDown() throws InterruptedException
	{
		setProcessing(false);
		// defect 11073
//		resetInitialStart();
//		while (RTSApplicationController.isDBUp()
		while (isRTSAppControllerDBUp()
		// end defect 11073
			&& !(isShouldRestart()))
		{
			Thread.sleep(MILLISECONDS_PER_SECOND * WAIT_TIME);
		}
		logSendCache(SENDCACHE, "Notified DB is down.");
		cbDBDownLoggedIndi = true;
	}

	/**
	 * Method waits until connectivity between client and db server
	 * are confirmed. Logs only once that is waiting for DB up.
	 * 
	 * @throws InterruptedException
	 * @throws RTSException
	 */
	private void waitForDBServerUp()
		throws InterruptedException, RTSException
	{
		// defect 6749
		// DB is not Available until successful qryLastTransDate()
		boolean lbDBAvailIndi = false;
		int liNumRetry = 0;
		while (!lbDBAvailIndi)
		{
			cbSleepMsgLoggedIndi = false;
			while (!isDBUp())
			{
				if (!cbSleepMsgLoggedIndi)
				{
					liNumRetry = 0;
					// If DB Down after error, should reset
					logSendCache(
						SENDCACHE,
						"Waiting for DB up. Sleeping.");
					cbSleepMsgLoggedIndi = true;
				}
				// defect 11073
//				resetInitialStart();
				// end defect 11073
				Thread.sleep(MILLISECONDS_PER_SECOND * WAIT_TIME);
			} // End while DB is down
			cbSleepMsgLoggedIndi = false; // Sleep has not been logged
			try
			{
				caRTSDateStart = qryLastTransDate();
				lbDBAvailIndi = true;
				// Exit loop, return to run(), call postSendCacheTrans()
			}
			catch (RTSException aeEx)
			{
				liNumRetry = liNumRetry + 1;
				if (liNumRetry > QRYLASTTRANSERROR_LIMIT)
				{
					logSendCache(
						SENDCACHE,
						"Retry limit of "
							+ QRYLASTTRANSERROR_LIMIT
							+ " exceeded.");
					throw aeEx;
				}
				logSendCache(
					SENDCACHE,
					"Error encountered in waitForDBServerUp: "
						+ aeEx.getDetailMsg());
				// defect 11073
//				resetInitialStart();
				// end defect 11073
				Thread.sleep(MILLISECONDS_PER_SECOND * WAIT_TIME);
				logSendCache(
					SENDCACHE,
					"Retry number "
						+ liNumRetry
						+ " of max "
						+ QRYLASTTRANSERROR_LIMIT
						+ ".");
			}
			catch (Throwable aeThrEx)
			{
				System.out.println("ERROR");
				System.out.println(aeThrEx.getStackTrace());
			}
		} // end defect 6749
	}
}
