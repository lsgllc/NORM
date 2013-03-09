package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;

/*
 *
 * RemoteListener.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Michael A.	09/10/2001	Added comments
 * Jeff S.		02/17/2005	Get code to standard.
 *							defect 7897 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		10/07/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3    
 * R Pilon		05/18/2012	Change to keep server socket accepting if the
 * 							  event is a ThreadEvent.DB_UP or 
 * 							  ThreadEvent.SEND_CACHE.  Also, added logic to 
 * 							  perform System.exit(0) is the port is already 
 * 							  in use as we assume that an graph of the
 * 							  calling process is already running.
 * 							add STOP_MESSAGE, ciRemoteListenerPort, 
 * 							  RemoteListener(int), constructor
 * 							modify ERROR_MESSAGE, RemoteListener zero arg 
 * 							  contructor, run()
 * 							defect 11073 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
 * Listens for remote calls to shutdown or startup.  This program is 
 * used to immediately shut down the RTSMain thread for updates to the
 * code or cache.
 *
 * @version	POS_700			07/02/2012
 * @author	Michael Abernethy
 * <p>Creation Date:		09/05/2001 14:52:00
 */
public class RemoteListener extends ThreadMessenger implements Runnable
{
	// defect 11073
	private final static String ERROR_MESSAGE = 
		"Error in RemoteListener - Unable to start ServerSocket on port ";

	private final static String STOP_MESSAGE = 
		"Performing System.exit(0) for calling process";

	private int ciRemoteListenerPort;
	// end defect 11073
	
	/**
	 * Creates a RemoteListener.
	 */
	public RemoteListener()
	{
		super();
		
		// defect 11073
		// default remote listener port to the back end processing port
		ciRemoteListenerPort = SystemProperty.getRemoteListenerPortBE();
		// end defect 11073
	}
	/**
	 * Creates a RemoteListener listening on a given port.
	 */
	public RemoteListener(int aiRemoteListenerPort)
	{
		super();
		
		ciRemoteListenerPort = aiRemoteListenerPort;
	}
	/**
	 * Creates a serversocket which sits and waits for any 
	 * messages from remote computers telling RTSMain to shutdown 
	 * or startup.  Upon receipt of a message, RemoteListener will 
	 * tell RTSMain to shut itself down or start itself up.
	 * 
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		try
		{
			// defect 11073
			ServerSocket laServerSocket = new ServerSocket(
					ciRemoteListenerPort);

			// continue accepting input if DB_UP event
			Socket laSocket;
			BufferedReader laIn;
			while (true)
			{
				laSocket = laServerSocket.accept();
				laIn = new BufferedReader(new InputStreamReader(
						laSocket.getInputStream()));
				int liMessage = Integer.parseInt(laIn.readLine());
				if (liMessage == ThreadEvent.STOP_MAIN
						|| liMessage == ThreadEvent.DB_UP
						|| liMessage == ThreadEvent.SEND_CACHE)
				{
					fireThreadEvent(new ThreadEvent(liMessage));
				}

				// if not DB_UP, SEND_CACHE or CHECK_GUI_INSTANCE event, stop 
				// listening
				// TODO - SHOULD WE NOW ONLY EXIT FOR STOP_MAIN EVENT AS ALL
				// COMMUNITCATION BETWEEN THE GUI AND SERVICE WILL BE FIRED
				// THREAD EVENTS ACROSS THE JVMS?
				if (liMessage != ThreadEvent.DB_UP
						&& liMessage != ThreadEvent.SEND_CACHE
						&& liMessage != ThreadEvent.CHECK_GUI_INSTANCE)
				{
					break;
				}
			}
			// end defect 11073
			laIn.close();
			laSocket.close();
			laServerSocket.close();
		}
		// defect 11073
		catch (BindException leBEx)
		{
			// Port is already in use so assume that an graph of the
			// calling application is already running.  Log message and 
			// shutdown calling application.
			System.err.println(ERROR_MESSAGE + ciRemoteListenerPort
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ leBEx.getMessage()
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ STOP_MESSAGE);
			Log.write(Log.START_END, this, ERROR_MESSAGE
					+ ciRemoteListenerPort
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ leBEx.getMessage()
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ STOP_MESSAGE);
			System.exit(0);
		}
		catch (IOException leIOEx)
		{
			System.err.println(ERROR_MESSAGE + ciRemoteListenerPort
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ leIOEx.getMessage());
			Log.write(Log.START_END, this, ERROR_MESSAGE
					+ ciRemoteListenerPort
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ leIOEx.getMessage());
			// end defect 11073
		}
	}
}