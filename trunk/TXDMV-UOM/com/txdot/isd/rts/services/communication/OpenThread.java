package com.txdot.isd.rts.services.communication;

import java.net.Socket;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * OpenThread.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Govindappa 01/10/2003  Added timeouts for checking the server 
 * 							connectivity.
 *							defect 5235
 * Ray Rowehl	06/17/2005	Code Cleanup
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/** 
 * This class is a thread which creates a socket after connecting to 
 * the host specified through hostname and port number and 
 * sets the socket field in Comm class
 * 
 * @version	5.2.3		06/17/2005
 * @author	S Govindappa
 * <br>Creation Date	01/10/2003 14:00:00
 */

public class OpenThread implements Runnable
{
	/**
	 * Run to create new Socket to the server.
	 * 
	 * <p>Note that exceptions are logged but not returned.
	 */
	public void run()
	{
		try
		{
			Socket laSocket =
				new Socket(
					SystemProperty.getServletHost(),
					Integer.parseInt(SystemProperty.getServletPort()));

			Comm.setSocket(laSocket);
		}
		catch (Throwable aeThrowable)
		{
			// instantiate the exception to log it.
			new RTSException(RTSException.JAVA_ERROR, aeThrowable);
		}
	}
}
