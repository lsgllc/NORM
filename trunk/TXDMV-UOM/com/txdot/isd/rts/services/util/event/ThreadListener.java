package com.txdot.isd.rts.services.util.event;

/*
 *
 * ThreadListener.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M. Abernethy 09/10/2001	Added Comments
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 *
 * The listener interface for receiving ThreadEvents.  A class that is
 * interested in receiving ThreadEvents implements
 * this interface, and the object created with that class is registered
 * with a component, using the component's 
 * addThreadListener method. When the ThreadEvent occurs, that 
 * object's <code>handleThreadEvent()</code>
 * method is invoked. 
 *
 * @version	5.2.3			06/21/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		09/05/2001 12:06:58
 */
public interface ThreadListener
{
	/**
	 * Invoked when a thread event should occur.
	 * 
	 * @param aaTE - com.txdot.isd.rts.services.util.event.ThreadEvent
	 */
	void handleThreadEvent(ThreadEvent aaTE);
}
