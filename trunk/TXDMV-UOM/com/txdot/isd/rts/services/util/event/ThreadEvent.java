package com.txdot.isd.rts.services.util.event;

/*
 *
 * ThreadEvent.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M. Abernethy 09/10/2001	Added Comments
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		04/03/2007	Added constant for Help.
 * 							add OPEN_INBOX, UPDATE_INBOX, 
 * 								FOCUS_TOP_FRAME
 * 							defect 7768 Ver Broadcast Message
 * R Pilon		07/02/2012	Add new thread event for database available.
 * 							add DB_UP, CHECK_GUI_INSTANCE
 * 							defect 11073 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * An event that has defined that a thread event should occur. It 
 * contains a String code containing the
 * thread event that should occur.
 * 
 * <p>An object implementing the interface <code>ThreadListener</code> 
 * will be able to receive ThreadEvents.
 *
 * @version	POS_700				07/02/2012
 * @author	Michael Abernethy
 * <br>Creation Date:			09/05/2001	12:06:58
 */

public class ThreadEvent
{
	/**
	 * 	Stops the entire RTSII application	
	 */
	public final static int STOP_MAIN = 0;

	/**
	 *	Reboots the system
	 */
	public final static int REBOOT_SYS = 3;

	/**
	 *	Starts the Send Cache function
	 */
	public final static int SEND_CACHE = 4;

	/**
	 *	An graph of the eventcode String
	 */
	private int ciEventCode;
	public final static int START_BATCH = 5;
	
	// defect 7768
	/**
	 *	Opens the message frame on top of any frame that you have open.
	 */
	public final static int OPEN_INBOX = 6;
	/**
	 *	Update the message frame with any new messages.
	 */
	public final static int UPDATE_INBOX = 7;
	/**
	 *	Puts focus on the top most frame in the application.
	 *  Used when returning from clicking on the balloon.
	 */
	public final static int FOCUS_TOP_FRAME = 8;
	// end defect 7768

	// defect 11073
	// send cache processing completed and database available
	public final static int DB_UP = 9;
	
	// checking for an graph of RTSMainGUI already running
	public final static int CHECK_GUI_INSTANCE = 10;
	// end defect 11073

	/**
	 * Creates a ThreadEvent.
	 * 
	 * @param aiEventCode - int
	 */
	public ThreadEvent(int aiEventCode)
	{
		super();
		ciEventCode = aiEventCode;
	}
	/**
	 * Returns the event code.
	 * 
	 * @return int
	 */
	public int getEventCode()
	{
		return ciEventCode;
	}
}
