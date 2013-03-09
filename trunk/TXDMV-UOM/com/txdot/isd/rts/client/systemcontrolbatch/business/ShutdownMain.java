package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;

/*
 *
 * ShutdownMain.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Change History:
 * Name        	Date        Description
 * M Abernethy	9/10/01		Added comments
 * Jeff S.		02/16/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		10/07/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3 
 * R Pilon		04/25/2012	Change to handle shutdown for both gui front end
 * 							  and back end.
 * 							modify ATTEMPT_TO_CONNECT, CONNECTED, MESSAGE_SENT, 
 * 							  PROBLEMS_CONNECTING, ShutdownMain(String) contructor
 * 							add ShutdownMain(String,int) constructor, 
 * 							  shutdown(String, int)
 * 							defect 11073 Ver 7.0.0
 *  --------------------------------------------------------------------
 */
/**
 * The ShutdownMain program tells a remote invocation of RTSMain to 
 * shutdown. <p>It can be used by someone to stop RTSII on anyone's
 * computer, given the computer name.
 * 
 * @version	POS_700		04/25/2012
 * @author	Michael Abernethy
 * @Creation Date: 		9/5/01 2:08:48 PM
 */
public class ShutdownMain
{
	// defect 11073
	private static final String ATTEMPT_TO_CONNECT =
		"Attempting to connect to application at ";
	private static final String CONNECTED = "Connection made to ";
	private static final String MESSAGE_SENT =
		"Message sent to shut down application at ";
	private static final String PROBLEMS_CONNECTING =
		"There were problems connecting to the"
			+ " host machine.  You will not be able to remotely"
			+ " shut down the RTS applications at ";
	// end defect 11073
	private static final String USAGE_MSG = 
		"Usage: ShutdownMain <computer name>"
			+ CommonConstant.SYSTEM_LINE_SEPARATOR
			+ "- or -"
			+ CommonConstant.SYSTEM_LINE_SEPARATOR
			+ "Usage: ShutdownMain <computer name>, <port>";
	/**
	 * Creates a ShutdownMain for a given host.  Will shutdown both front 
	 * end and back end for the host.
	 * 
	 * @param hostname String - the name of the computer to shutdown.
	 */
	public ShutdownMain(String asHostname)
	{
		// defect 11073
		shutdown(asHostname, SystemProperty.getRemoteListenerPortGUI());

		shutdown(asHostname, SystemProperty.getRemoteListenerPortBE());
		// end defect 11073
	}
	
	/**
	 * Creates a ShutdownMain for a given host and port.
	 * 
	 * @param hostname String - the name of the computer to shutdown.
	 */
	public ShutdownMain(String asHostname, int aiPort)
	{
		shutdown(asHostname, aiPort);
	}

	/**
	 * Fire shutdown thread event for a given host and port.
	 * 
	 * @param asHostname
	 * @param aiPort
	 * @throws IOException
	 */
	private void shutdown(String asHostname, int aiPort)
	{
		Socket laSocket = null;
		PrintWriter laOut = null;
		String lsHostPort = asHostname + ":" + aiPort; 
		try
		{
			System.out.println(ATTEMPT_TO_CONNECT + lsHostPort);
			laSocket = new Socket(asHostname, aiPort);
			laOut = new PrintWriter(new BufferedOutputStream(laSocket
					.getOutputStream()));
			System.out.println(CONNECTED + lsHostPort);
			laOut.println(ThreadEvent.STOP_MAIN);
			System.out.println(MESSAGE_SENT + lsHostPort);
		}
		catch (IOException leIOEx)
		{
			System.err.println(PROBLEMS_CONNECTING + lsHostPort + "."
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ leIOEx.getMessage());
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
	 * Runs the ShutdownMain program
	 * 
	 * @param aarrArgs String[] - arg[0] should be the name of the
	 * 	remote computer to shutdown.
	 */
	public static void main(String[] aarrArgs)
	{
		if (aarrArgs.length == 0 || aarrArgs.length > 2)
		{
			System.out.println(USAGE_MSG);
		}
		else
		{
			if (aarrArgs.length == 1)
			{
				new ShutdownMain(aarrArgs[0]);
			}
			else
			{
				new ShutdownMain(aarrArgs[0], Integer
						.parseInt(aarrArgs[1]));
			}
		}
	}
}