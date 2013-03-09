package com.txdot.isd.rts.services.util;

import java.util.Vector;

import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/*
 *
 * BarCodeThread.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							organize imports, rename fields
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Thread for the barcode to interact with the GUI thread.
 * 
 * @version	5.2.3			06/21/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		05/06/2002 11:39:18
 */

public class BarCodeThread implements Runnable
{
	private java.util.Vector cvListeners;
	private java.lang.Object caEventData;
	/**
	 * BarCodeThread constructor comment.
	 * 
	 * @param avListeners - Vector
	 * @param aaEventData - Object
	 */
	public BarCodeThread(Vector avListeners, Object aaEventData)
	{
		super();
		cvListeners = avListeners;
		caEventData = aaEventData;
	}
	/**
	 * When an object implementing interface <code>Runnable</code> is used 
	 * to create a thread, starting the thread causes the object's 
	 * <code>run</code> method to be called in that separately executing 
	 * thread. 
	 * 
	 * <p>The general contract of the method <code>run</code> is that it may 
	 * take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		int liSize = cvListeners.size();
		int liNdx = 0;
		while (liNdx < liSize)
		{
			((BarCodeListener) cvListeners.get(liNdx)).barCodeScanned(
				new BarCodeEvent(caEventData));
			liNdx++;
		}
	}
}
