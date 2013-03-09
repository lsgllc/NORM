package com.txdot.isd.rts.services.util.event;

/*
 *
 * BarCodeListener.java
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
 * The listener interface for receiving BarCodeEvents.  A class that 
 * is interested in receiving BarCodeEvents implements
 * this interface, and the object created with that class is registered
 * with a component, using the component's 
 * addBarCodeListener method. When the BarCodeEvent occurs, that 
 * object's barCodeScanned method is invoked. 
 *
 * @version	5.2.3			06/21/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		09/05/2001 12:06:58
 */
public interface BarCodeListener {
/**
 * Invoked when a barcode is scanned.
 * 
 * @param aaBCE com.txdot.isd.rts.sevices.util.event.BarCodeEvent
 */
void barCodeScanned(BarCodeEvent aaBCE);
}
