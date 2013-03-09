package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 *
 * ThreadMessenger.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Change History:
 * Name        	Date        Description
 * M Abernethy	09/10/01	Added comments
 * Jeff S.		02/16/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		10/07/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3 
 * R Pilon		04/09/2012	Add new fireThreadEvent to fire the event
 * 							  to a listening port.
 * 							add ATTEMPT_TO_CONNECT, CONNECTED, MESSAGE_SENT, 
 * 							  PROBLEMS_CONNECTING, 
 * 							  fireThreadEvent(ThreadEvent, String, int)
 * 							defect 11073 Ver 7.0.0
 * R Pilon		05/14/2012	Added throws RTSException to fireThreadEvent.
 * 							deleted fireThreadEvent(ThreadEvent,Sring,int)
 * 							added fireThreadEvent(ThreadEvent,Sring,int) throws 
 * 							  RTSException
 * 							defect 11073 Ver 7.0.0
 *  --------------------------------------------------------------------
 */
/**
 * Superclass for any class that can fire ThreadEvents.
 * It defines the methods required to implement the ThreadListener/Event
 * model. Subclasses are therefore free from implementing the
 * addThreadListener() and removeListener() functions.
 * 
 * @version	POS_700		05/14/2012
 * @author	Michael Abernethy
 * @Creation Date: 		09/05/01 01:30:59 PM
 */
public class ThreadMessenger
{
	private static final String FIRING_REBOOT = 
		"Firing REBOOT event to ";

	// defect 11073
	private static final String ATTEMPT_TO_CONNECT = 
		"Attempting to connect to ";

	private static final String CONNECTED = "Connected to ";

	private static final String MESSAGE_SENT = "Message sent to ";

	private static final String PROBLEMS_CONNECTING = "Sending of thread "
			+ "event failed. Unable to connect to ";
	// end defect 11073

	/**
	 * Contains all the listeners for ThreadEvents.
	 */
	private Vector cvListeners = null;
	/**
	 * Creates an AbstractThreadMessenger.
	 */
	public ThreadMessenger()
	{
		super();
		cvListeners = new Vector();
	}
	/**
	 * addThreadListener - Adds a listener for ThreadEvents
	 * 
	 * @param aaTListener ThreadListener
	 */
	public void addThreadListener(ThreadListener aaTListener)
	{
		if (!cvListeners.contains(aaTListener))
			cvListeners.add(aaTListener);
	}
	/**
	 * fireThreadEvent - Loops through all the listeners and calls their
	 * handleThreadEvent method
	 * 
	 * @param leTE ThreadEvent
	 */
	protected void fireThreadEvent(ThreadEvent leTE)
	{
		for (int i = 0; i < cvListeners.size(); i++)
		{
			ThreadListener laTListener =
				(ThreadListener) cvListeners.get(i);
			if (leTE.getEventCode() == ThreadEvent.REBOOT_SYS)
			{
				BatchLog.write(FIRING_REBOOT + laTListener);
			}
			laTListener.handleThreadEvent(leTE);
		}
	}
	/**
	 * fireThreadEvent - Connect to a given ServerSocket and send a message.  
	 * This fireThreadEvent is used to fire a thread event to a thread 
	 * running under a different JVM.
	 * 
	 * @param aaThreadEvent ThreadEvent
	 * @param asHost String
	 * @param aiPort int
	 * @throws RTSException 
	 */
	protected void fireThreadEvent(ThreadEvent aaThreadEvent,
			String asHost, int aiPort) throws RTSException
	{
		Socket laSocket = null;
		PrintWriter laOut = null;
		String lsHostPort = asHost + ":" + aiPort;
		try
		{
			BatchLog.write(ATTEMPT_TO_CONNECT + lsHostPort);
			laSocket = new Socket(asHost, aiPort);
			laOut = new PrintWriter(new BufferedOutputStream(laSocket
					.getOutputStream()));
			BatchLog.write(CONNECTED + lsHostPort);
			laOut.println(aaThreadEvent.getEventCode());
			BatchLog.write(MESSAGE_SENT + lsHostPort);
		}
		catch (ConnectException aeCEx)
		{
			BatchLog.error(PROBLEMS_CONNECTING + lsHostPort + ". "
					+ aeCEx.getMessage());
			throw new RTSException(
					ErrorsConstant.ERR_NUM_SOCKET_CONNECTION_EXCEPTION);
		}
		catch (IOException aeIOEx)
		{
			BatchLog.error(PROBLEMS_CONNECTING + lsHostPort + ". "
					+ aeIOEx.getMessage());
			throw new RTSException(PROBLEMS_CONNECTING + lsHostPort
					+ ". ", aeIOEx);
		}
		finally
		{
			try
			{
				laOut.flush();
				laOut.close();
				laOut = null;
				laSocket.close();
				laSocket = null;
			}
			catch (IOException aeIOEx)
			{
				// already closed
			}
			catch (Exception aeEx)
			{
				// not reporting as we are only attempting to clean up
				// the Socket and PrintWriter
			}
		}
	}
	/**
	 * removeThreadListener - Removes a listener for ThreadEvents
	 * 
	 * @param aaTListener ThreadListener
	 */
	public void removeThreadListener(ThreadListener aaTListener)
	{
		cvListeners.remove(aaTListener);
	}
}
