package com.txdot.isd.rts.services.webapps.exception;

import java.io.Serializable;

import com.txdot.isd.rts.services.webapps.util.UtilityMethods;

/*
 *
 * RTSWebAppsException.java 
 *
 * (c) Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown      11/30/2004  Change the spelling of "Sever" to Server"
 *                          for 2 graph variables.
 *                          Add a main method for testing.
 *                          defect 7577 Ver 5.2.2
 * K Harrell	09/19/2005	Java 1.4. Cleanup 
 * 							rename makeDetailedMessage() to 
 * 							setDetailedMessage()
 * 							Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/** 
 * This class is responsible for getting the stack trace info and 
 * returning it when this class is instantiated. 
 *
 * @version	5.2.3			09/19/2005 
 * @author Administrator
 * <br>Creation Date:		08/06/2002 10:54:59
 */

public class RTSWebAppsException
	extends Exception
	implements Serializable
{

	private String csDetailedMessage;

	// Message Type 

	// Error Message type -Java Error
	public static final String JAVA_ERROR = "JAVA_ERROR";

	// defect 7577
	// Error Message type - Server Down
	public final static String SERVER_DOWN = "SERVER_DOWN";

	// Error Message type - Epay Server Down
	public final static String EPAY_SERVER_DOWN = "EPAY_SERVER_DOWN";

	// end defect 7577

	// MQSeries Error	
	public final static String MQ_ERROR = "MQSERIES_ERROR";

	private final static long serialVersionUID = -5991343042294969455L;

	/**
	 * RTSWebAppsException constructor comment.
	 */
	public RTSWebAppsException()
	{
		super();
	}

	/**
	 * Constructs a RTSWebAppsException with the specified detail message.
	 * 
	 * @param asMsg String
	 */
	public RTSWebAppsException(String asMsg)
	{
		super(asMsg);
	}

	/**
	 * Constructs a RTSWebAppsException with the specified detail message.
	 * 
	 * @param asMsg String
	 * @param aeThrowable Throwable 
	 */

	public RTSWebAppsException(String asMsg, Throwable aeThrowable)
	{
		super(asMsg);
		setDetailedMessage(aeThrowable);
	}

	/**
	 * Constructs a RTSWebAppsException with the specified detail message.
	 * 
	 * @param aeThrowable Throwable 
	 */
	public RTSWebAppsException(Throwable aeThrowable)
	{
		setDetailedMessage(aeThrowable);
	}

	/**
	 * Return value of csDetailedMessage
	 */
	public String getDetailedMessage()
	{
		return csDetailedMessage;
	}

	/**
	 * This method is for testing this class
	 *
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// defect 7577
		RTSWebAppsException leRTSWebAppsException =
			new RTSWebAppsException(
				new Throwable(SERVER_DOWN + " " + EPAY_SERVER_DOWN));

		System.out.println(leRTSWebAppsException.csDetailedMessage);
		// end defect 7577

	}

	/**
	 * Assign value to csDetailedMesssage
	 */
	private void setDetailedMessage(Throwable aeThrowable)
	{
		csDetailedMessage = UtilityMethods.getStackTrace(aeThrowable);
	}
}
