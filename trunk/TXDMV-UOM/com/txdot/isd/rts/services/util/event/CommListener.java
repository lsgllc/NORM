package com.txdot.isd.rts.services.util.event;

/*
 *
 * CommListener.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * The listener interface for receiving CommEvents.  A class that is
 * interested in receiving CommEvents implements
 * this interface, and the object created with that class is registered
 * with a component, using the component's 
 * addCommEventListener method. When the CommEvents occurs, that 
 * object's handleCommEvent method is invoked. 
 * 
 * @version	5.2.3			06/21/2005
 * @author: Michael Abernethy
 * <br>Creation Date:		12/01/2001 12:13:57
 */

public interface CommListener {
/**
 * Invoked when a CommEvent occurs
 * 
 * @param aaCE - com.txdot.isd.rts.services.util.event.CommEvent
 */
void handleCommEvent(CommEvent aaCE);
}
