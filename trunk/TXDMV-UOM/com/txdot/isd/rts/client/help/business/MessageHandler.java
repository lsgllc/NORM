package com.txdot.isd.rts.client.help.business;

import java.net.URLEncoder;
import java.util.Vector;

import com.txdot.isd.rts.client.help.ui.VCMessagesMES001;
import com.txdot.isd.rts.client.systemcontrolbatch.business.ThreadMessenger;
import com.txdot.isd.rts.services.data.MessageData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.BroadcastMsgConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 * MessageHandler.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------
 * Jeff S.		04/02/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */

/**
 * Threaded class that is used to periodicly check for new messages.
 * When a new message exists this class will call a C++ .exe that will 
 * popup a baloon on the taksbar.  The baloon will tell the user that 
 * there are unread messages.  If the user clicks on the balloon then 
 * a 1 is returned and the MES001 screen is presented.  If the balloon 
 * times out or the user clicks the X then a 0 is returned and nothing 
 * happens.
 * 
 * Only present the message balloon when the user logs in and there are
 * unread messages.  Once the user is presented the message they will
 * not be notified again until a new message is received or they log
 * off and then back in again and there are unread messages.
 * 
 * Messages can also be sent that don't require a notification to the 
 * user.  If the user logs in and there is atleast one unread message
 * that requires a notification then we will notify.
 * 
 * This class useses an inner class as a helper class to launch events
 * while continuing to check for new messages.
 *
 * @version	Broadcast Message	04/02/2007
 * @author	Jeff S.
 * <br>Creation Date:			03/23/2006 07:00:00
 */
public class MessageHandler extends ThreadMessenger implements Runnable
{
	private static final int BALLOON_CLICK = 1;
	private static final String BALLOON_MSG1 =
		"You have unread RTS message(s).";
	private static final String BALLOON_MSG2 =
		"You have a new RTS message.";
	private static final String BALLOON_TITLE = "RTS Messages";
	private static Process caProcess = null;
	private static boolean csMessagesVisible = false;

	private static final String MESSAGING_ERROR =
		"Error in Messaging Thread - ";
	public static final int PRIORITY_HIGH = 1;
	public static final int PRIORITY_LOW = 0;
	private static final String PURGE_MESS_ERROR =
		"Purge Messages Error";
	public final static char QUOTE = (char) 34;
	private static final String RTS_MAIL_EXE = "RTSMail.exe";
	private static final String RTSMAIL_ERROR = "Error running RTSMail";
	// Holds the MessageData for the last message that has arived.
	private static MessageData saLastNewMsg = null;
	private static int siNotifyPriority = PRIORITY_LOW;
	// This is used to determine if we have notified the user that they
	// have unread messages after login.
	private static boolean sbFirstRunAfterLogin = true;
	private static boolean sbKeepRunning = true;
	private static final String SET_RTS_FOCUS = "SetRTSFocus.exe";
	private static final String SETFOCUS_ERROR =
		"Error running setRTSFocus";
	private static final int SLEEP_TIME = 5000;
	private static final String THREAD_OPEN_INBOX =
		"Message Helper - Open Inbox";
	private static final String THREAD_UPDATE_INBOX =
		"Message Helper - Update Inbox";
	private static final String UTF_8 = "UTF-8";
	private static final int WAIT_NUM_TRIES = 20;

	// quarter second wait times 20 = 5 seconds
	private static final int WAIT_TIME = 250;

	HelpClientBusiness caHelpClientBus = new HelpClientBusiness();
	ThreadListener caListener;

	/*
	 * This is the message helper that will do different tasks and allow
	 * the MessageHandler class continue to check for new messages.
	 * <p>
	 * 1. Present the message client screen and still allow the 
	 * MessageHandler to check for new messages.
	 * <p>
	 * 2. Update the message screen with new messages.
	 * 
	 * @version	5.2.3			03/23/2006
	 * @author	Jeff S.
	 * <br>Creation Date:		03/23/2006 07:00:00
	 */
	private class MessageHelper
		extends ThreadMessenger
		implements Runnable
	{
		private int caCommand;

		/**
		 * MessageHandler.java Constructor
		 */
		public MessageHelper(int aiCommand)
		{
			super();
			this.caCommand = aiCommand;
		}

		/**
		 * Run method that fires the event.
		 */
		public void run()
		{
			fireThreadEvent(new ThreadEvent(caCommand));
		}
	}

	/**
	 * Returns if the thread should keep working.
	 * 
	 * @return boolean
	 */
	private static boolean getKeepRunning()
	{
		return sbKeepRunning;
	}

	/**
	 * Returns if the message client is visble.  This determines if any
	 * new message balloons will be presented or the message client will
	 * be updated.
	 * 
	 * @return boolean
	 */
	private static boolean isMsgClientVisible()
	{
		return csMessagesVisible;
	}

	/**
	 * Resets the lst message back to null so that when a new
	 * user logs in they will get a message if there are unread 
	 * notify messages.
	 */
	public static void resetForNewUser()
	{
		sbFirstRunAfterLogin = true;
		saLastNewMsg = null;
	}

	/**
	 * Runs the setRTSFocus.exe that is in the %SystemRoot%\System32
	 * directory on all production machines.  This .exe will look for 
	 * RTS and set focus to the top window.
	 * 
	 * setRTSFocus.exe "RTS Messages  MES001" - focus will be set to the
	 * message screen.
	 * 
	 * @param  asWindowTitle String
	 */
	public static void runSetRTSFocus(String asWindowTitle)
	{
		try
		{
			// Used for testing
			//Process laProcess =
			Runtime.getRuntime().exec(
				SET_RTS_FOCUS
					+ CommonConstant.STR_SPACE_ONE
					+ QUOTE
					+ URLEncoder.encode(asWindowTitle, UTF_8)
					+ QUOTE);
			// Used for testing
			//laProcess.waitFor();
			//System.out.println(laProcess.exitValue());
		}
		catch (Exception aeEx)
		{
			Log.write(
				Log.START_END,
				MessageHandler.class,
				SETFOCUS_ERROR
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeEx.getMessage());
		}
	}

	/**
	 * Sets if the thread should keep working.
	 * 
	 * @param abKeepRunning boolean
	 */
	public static void setKeepRunning(boolean abKeepRunning)
	{
		sbKeepRunning = abKeepRunning;
		// If we are closing down then we neeed to kill the balloon
		// process.
		if (!abKeepRunning && caProcess != null)
		{
			try
			{
				caProcess.destroy();
			}
			catch (NullPointerException aeNPEx)
			{
				// Empty b/c if caProcess is null then it has
				// closed on it's own.
			}
		}
	}

	/**
	 * Sets if the message client is visible.  If the message client is
	 * visible then we will not present any more balloon messages.  This
	 * class will update the message client with new messages.
	 * 
	 * @param abClientVisible boolean
	 */
	public static void setMsgClientVisible(boolean abClientVisible)
	{
		csMessagesVisible = abClientVisible;
	}

	/**
	 * Creates a MessageHandler.
	 * 
	 * @param aaListener ThreadListener
	 */
	public MessageHandler(ThreadListener aaListener)
	{
		super();
		this.caListener = aaListener;
	}

	/**
	 * Return the MessageData Obj of the last message.  The last message
	 * is determined by the message with the latest timestamp.  The
	 * newest message.
	 * 
	 * @param avMessages Vector
	 * @return MessageData
	 */
	private MessageData getLastUnreadMsg(Vector avMessages)
	{
		RTSDate laGreatestTimestamp = null;
		MessageData laNewestMessage = new MessageData();
		for (int i = 0; i < avMessages.size(); i++)
		{
			MessageData laMessage = (MessageData) avMessages.get(i);
			// Save the MessageData Obj of the one with the latest date
			if (laGreatestTimestamp == null
				|| laMessage.getDate().compareTo(laGreatestTimestamp)
					== 1)
			{
				laGreatestTimestamp = laMessage.getDate();
				laNewestMessage = laMessage;
			}
		}

		return laNewestMessage;
	}

	/**
	 * Return a vector of Message Data objects that we received since
	 * the last one that we know about.  If the last one is null
	 * then we will return all of them.
	 * 
	 * @param avMessages Vector
	 * @return MessageData
	 */
	private Vector getNewUnreadMsgs(Vector avMessages)
	{
		Vector lvNewUnreadMsgs = new Vector();
		if (saLastNewMsg == null)
		{
			lvNewUnreadMsgs = avMessages;
		}
		else
		{
			for (int i = 0; i < avMessages.size(); i++)
			{
				MessageData laMessage = (MessageData) avMessages.get(i);
				if (laMessage
					.getDate()
					.compareTo(saLastNewMsg.getDate())
					== 1)
				{
					lvNewUnreadMsgs.add(laMessage);
				}
			}
		}
		return lvNewUnreadMsgs;
	}

	/**
	 * Returns if we should notify the user or not.
	 * Steps:
	 * 1.	Get all unread messages.
	 * 2.	Looking at the last message timestamp get all new unread
	 * 		messages.
	 * 3.	If there are new unread messages then check if we should
	 * 		prompt for any of them.  If so then collect if any of them
	 * 		are high priority.
	 * 4.	If the message client is open then initiate an update of the
	 * 		contents of the message client and return false to not 
	 * 		prompt.
	 * 5.	If the message client is not open then and there are new
	 * 		messages to prompt return true to prompt.
	 * 6.	Save the lastest message so that we can determine if a new
	 * 		one has arrived.
	 * 
	 * @return boolean
	 * @throws RTSException
	 */
	private boolean ifNewMsgsToNotify() throws RTSException
	{
		boolean lbReturnValue = false;
		try
		{
			// Get all the unread messages
			Vector lvUnreadMessages =
				(Vector) caHelpClientBus.processData(
					GeneralConstant.HELP,
					BroadcastMsgConstants.GET_UNREAD_MESSAGES,
					null);

			// Get all of the new unread messages buy using the
			// last unread messages date.	
			Vector lvNewUnreadMessages =
				getNewUnreadMsgs(lvUnreadMessages);

			// If there are new unread messages then we need to check
			// if we should notify and update the last message.
			if (lvNewUnreadMessages.size() > 0)
			{
				// If the message client is visble then there is
				// no reason to present a balloon just update
				// the message client with the new message and return
				// false.
				if (isMsgClientVisible())
				{
					MessageHelper laMesHelper =
						new MessageHelper(ThreadEvent.UPDATE_INBOX);
					Thread caThreadHelper =
						new Thread(laMesHelper, THREAD_UPDATE_INBOX);
					laMesHelper.addThreadListener(caListener);
					caThreadHelper.setDaemon(true);
					caThreadHelper.start();
				}
				else
				{
					// Message client is not visible therefore we need
					// to check the new messages to see if we should 
					// prompt.
					lbReturnValue = shouldNotify(lvNewUnreadMessages);
				}
				// Save the last message so that we can see if there
				// are any new ones later.
				saLastNewMsg = getLastUnreadMsg(lvNewUnreadMessages);
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.START_END,
				this,
				MESSAGING_ERROR
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());
		}
		finally
		{
			return lbReturnValue;
		}
	}

	/**
	 * Return if a user is logged in or not.
	 * 
	 * @return boolean
	 */
	private boolean loggedIn()
	{
		if (SystemProperty.getCurrentEmpId() != null
			&& !SystemProperty.getCurrentEmpId().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Used to purge any expired messages.  A message is considered
	 * expired and either two actions are taken.  The message is deleted
	 * or the message is marked as read.  There are two properties 
	 * in the message header that determine the expiration.
	 */
	private void purgeMessages()
	{
		try
		{
			caHelpClientBus.processData(
				GeneralConstant.HELP,
				BroadcastMsgConstants.PURGE_EXPIRED,
				null);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.START_END,
				this,
				PURGE_MESS_ERROR
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());
		}
	}

	/**
	 * Method used to notify the user and popup the balloon.
	 * If this is the first time after login we will use a different
	 * message than we do everytime after that.
	 * 
	 * The balloon will react to 3 things. It returns a 1 or 0.
	 * 1.	Timeout (Set focus to the top level RTS window)
	 * 		return 0
	 * 2.	User clicks on the X (Set focus to the top level RTS window)
	 * 		return 0
	 * 3.	User clicks on the balloon (Set focus to message window)
	 * 		return 1
	 */
	private void notifyUser()
	{
		// Use BALLOON_MSG2 for the message unless
		// this is the first run. 
		String lsTitle = BALLOON_MSG2;
		if (sbFirstRunAfterLogin)
		{
			lsTitle = BALLOON_MSG1;
			sbFirstRunAfterLogin = false;
		}
		// Popup the balloon - If the user clicks on
		// the balloon then present the message 
		// client and set focus back to the 
		// application.  If the user clicks the X or
		// the balloon times out then we need to set 
		// focus back on the application.
		if (runBalloonPopup(BALLOON_TITLE, lsTitle, siNotifyPriority)
			== BALLOON_CLICK)
		{
			// Check to see if the user opened the
			// message client while the balloon
			// is up or they logged out.
			if (!isMsgClientVisible() && loggedIn())
			{
				MessageHelper laMesHelper =
					new MessageHelper(ThreadEvent.OPEN_INBOX);
				Thread caThreadHelper =
					new Thread(laMesHelper, THREAD_OPEN_INBOX);
				laMesHelper.addThreadListener(caListener);
				caThreadHelper.setDaemon(true);
				caThreadHelper.start();
				// Set focus back to RTS after the 
				// balloon click, timeout first to 
				// allow the window to be presented.
				waitForMES001();
				runSetRTSFocus(ScreenConstant.MES001_FRAME_TITLE);
			}
			else
			{
				// Set focus back to the app.  The
				// user must have opened the message
				// frame without using the balloon.
				// We are putting focus back on the
				// app because they might have 
				// clicked the balloon.
				fireThreadEvent(
					new ThreadEvent(ThreadEvent.FOCUS_TOP_FRAME));
			}
		}
		else
		{
			// Since we are returned a 0 for both 
			// timeout and user clicking X we will 
			// attempt to set focus  back to the top
			// level window.  Have to call RTSMain
			// because he can get the top level 
			// window.  No need for Threading.
			fireThreadEvent(
				new ThreadEvent(ThreadEvent.FOCUS_TOP_FRAME));
		}

	}

	/**
	 * Main entry of the program.  This will continue to run until it
	 * is told to stop.  This is the heart of the messaging client.  
	 * This is a threaded class that will continuously monitor the 
	 * message count and take the appropriate steps when a message is
	 * received.
	 * 
	 * If a user is not logged in to RTS we will not do anything.
	 * 
	 * When a user logs into RTS and he/she has any unread messages they
	 * are prompted.  If any of the unread messages are of high priority
	 * then the popup will be a high priority popup.
	 * 
	 * This method also handles the purging of all old or expired 
	 * messages.  This process will only happen one time when the 
	 * RTS is started.
	 */
	public void run()
	{
		// Purge the messages that have expired
		purgeMessages();

		while (getKeepRunning())
		{
			try
			{
				// Only check for new messages if a user is logged in.
				if (loggedIn())
				{
					// If there are new messages to notify
					if (ifNewMsgsToNotify())
					{
						notifyUser();
					}
					// The user is logged on and does not have any 
					// unread messages at this time.  Setting this
					// boolean to false so that we will not treat any
					// new messages as the user has just logged on.
					else
					{
						sbFirstRunAfterLogin = false;
					}
				}
			}
			catch (Exception aeEx)
			{
				Log.write(
					Log.START_END,
					this,
					MESSAGING_ERROR
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ aeEx.getMessage());
			}
			finally
			{
				timeout(SLEEP_TIME);
			}
		}
	}

	/**
	 * Runs the RTSMail.exe and passes the title and message and the 
	 * priority. If the user clicks the balloon than 1 is returned if 
	 * the user clicks the X or the balloon times out then 0 is 
	 * returned.
	 *
	 * @param asTitle String
	 * @param asMessage String
	 * @param aiPriority int
	 * @return int
	 */
	private int runBalloonPopup(
		String asTitle,
		String asMessage,
		int aiPriority)
	{
		try
		{
			if (asTitle == null
				|| asTitle.equals(CommonConstant.STR_SPACE_EMPTY)
				|| asMessage == null
				|| asMessage.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				return 0;
			}
			// Using class variable so that we can destroy this process
			// when the thread closes.
			caProcess =
				Runtime.getRuntime().exec(
					RTS_MAIL_EXE
						+ CommonConstant.STR_SPACE_ONE
						+ QUOTE
						+ asTitle
						+ QUOTE
						+ CommonConstant.STR_SPACE_ONE
						+ QUOTE
						+ asMessage
						+ QUOTE
						+ CommonConstant.STR_SPACE_ONE
						+ aiPriority);
			caProcess.waitFor();
			int liReturnCode = caProcess.exitValue();
			caProcess = null;
			return liReturnCode;
		}
		catch (Exception aeEx)
		{
			Log.write(
				Log.START_END,
				this,
				RTSMAIL_ERROR
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeEx.getMessage());
			return 0;
		}
	}

	/**
	 * Loops through the given vector of messages and if one of them
	 * says to notify then we will return true.  If we hit a message
	 * that we need to notify and the priority is high then we will
	 * set the priority to high.  We can not break once we get one 
	 * message that we need to notify on b/c we have to see if there
	 * are any that are high priority.  We can break before looping
	 * through all of the messages if we get one that is a high priority
	 * and we need to notify.
	 * 
	 * @param avUnreadMessages
	 * @return boolean
	 */
	private boolean shouldNotify(Vector avUnreadMessages)
	{
		// Set the notify priority to low
		siNotifyPriority = PRIORITY_LOW;

		boolean lbShouldNotify = false;
		for (int i = 0; i < avUnreadMessages.size(); i++)
		{
			MessageData laMessage =
				(MessageData) avUnreadMessages.get(i);
			if (laMessage.isNotify())
			{
				lbShouldNotify = true;
				// If we hit a high priority along the way that we
				// can notify then set the notify priority to high.
				// We can not break until we get both a message that
				// we need to notify about and it is a high priority.
				if (laMessage.isHighPriority())
				{
					siNotifyPriority = PRIORITY_HIGH;
					break;
				}
			}
		}
		return lbShouldNotify;
	}

	/**
	 * Sleep method.
	 * 
	 * @param aiSleepTime
	 */
	private void timeout(int aiSleepTime)
	{
		try
		{
			Thread.sleep(aiSleepTime);
		}
		catch (InterruptedException aeIEx)
		{
			// Empty Catch
		}
	}

	/**
	 * Wait for the message client to be visible.  Once it is visble 
	 * then return.  If we try the max number of tries before it is
	 * visible then just return.
	 */
	private void waitForMES001()
	{
		int liTries = 0;
		while (true)
		{
			liTries += 1;
			if (isMsgClientVisible())
			{
				break;
			}
			else
			{
				if (liTries == WAIT_NUM_TRIES)
				{
					break;
				}
				else
				{
					timeout(WAIT_TIME);
				}
			}
		}
	}
}