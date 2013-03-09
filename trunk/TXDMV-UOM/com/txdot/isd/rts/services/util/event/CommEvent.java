package com.txdot.isd.rts.services.util.event;

/*
 *
 * CommEvent.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/21/2005	Code cleanup	
 * 							defect 7885 Ver 5.2.3
 * K Harrell	10/20/2005	delete SERVER_UP 
 * 							defect 8337 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * An event that has defined that the server status, DB status, or 
 * MF status has changed.  It contains a data object that defines
 * the type of change that has occurred.
 * 
 * <p>An object implementing the interface <code>CommListener</code> 
 * will be able to receive CommEvents
 * and will be spared all the details of keeping track of if the DB, 
 * MF, and Server go up and down.  It will just receive
 * notification of this happening.
 * 
 * @version	5.2.3			10/20/2005	
 * @author Michael Abernethy
 * <br>Creation Date:		12/01/2001 12:13:44
 */
public class CommEvent
{
	// defect 8337 
	// Not used 
	//public final static int SERVER_UP = 0;
	// end defect 8337 
	public final static int SERVER_DOWN = 1;
	public final static int MF_UP = 2;
	public final static int MF_DOWN = 3;
	public final static int DB_DOWN = 4;
	public final static int DB_UP = 5;
	private int ciEvent;
	/**
	 * Creates a CommEvent
	 * 
	 * @param aiEvent - int
	 */
	public CommEvent(int aiEvent)
	{
		ciEvent = aiEvent;
	}
	/**
	 * Returns the type of CommEvent
	 * 
	 * @return int
	 */
	public int getCommEvent()
	{
		return ciEvent;
	}
}
