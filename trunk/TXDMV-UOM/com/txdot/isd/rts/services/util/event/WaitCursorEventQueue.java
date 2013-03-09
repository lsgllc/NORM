package com.txdot.isd.rts.services.util.event;

import java.awt.*;

import javax.swing.SwingUtilities;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * WaitCursorEventQueue.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * RS CHANDEL  	11/28/2001	Added Comments
 * Ray Rowehl	04/24/2004	Added catch for Null Pointer
 *							reflowed code and added comments.
 *							Renamed class objects to have a "c".
 *							defect 7038 Ver 5.1.6
 * Min Wang 	04/30/2004	Deprecated this class.  We no longer use it.
 *							defect 6416 Ver 5.1.6
 * ---------------------------------------------------------------------
 */
 
/**
 *	This class is used to change cursor to hour glass
 *
 * @version	5.1.6  	04/30/3004  
 * @author	RS Chandel
 * <p>Creation date 11/28/2001 13:06:58
 * @deprecated
 */
 
public class WaitCursorEventQueue extends EventQueue
{

    private int cDelay;
    private WaitCursorTimer cWaitTimer;

    /**
     * Inner class to handle waiting for timer events
     */
    private class WaitCursorTimer extends Thread
    {
		/**
		 * Start the Timer
		 */
        synchronized void startTimer(Object aSource)
        {
            cSource = aSource;
            notify();
        }

        /**
         * Stop the Timer
         */
        synchronized void stopTimer()
        {
            if (cParent == null)
            {
	            interrupt();
            }
            else
            {
                cParent.setCursor(null);
                cParent = null;
            }
        }

        /**
         * Run waiting for state change
         */
        public synchronized void run()
        {
            while (true)
            {
                try
                {
                    //wait for notification from startTimer()
                    wait();

                    //wait for event processing to reach the threshold, or
                    //interruption from stopTimer()
                    wait(cDelay);

                    if (cSource instanceof Component)
                    {
                        cParent = SwingUtilities.getRoot((Component) cSource);
                    }
                    else
                    {
                        if (cSource instanceof MenuComponent)
                        {
                            MenuContainer lMParent = ((MenuComponent) cSource).getParent();
                            if (lMParent instanceof Component)
                            {
                                cParent = SwingUtilities.getRoot((Component) lMParent);
                            }
                        }
                    }

                    if (cParent != null && cParent.isShowing())
                    {
	                    Log.write(Log.SQL_EXCP, this, " Turning on Wait Cursor in DispatchEvent");
	                    //System.out.println("Turning on waitcursor from dispatchEvent.");
                        cParent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    }
                }
                catch (InterruptedException ie)
                {
	                // we got interrupted, so do not show wait.
                }
            }
        }

        private Object cSource;
        private Component cParent;
    }
/**
 * Constructor for WaitCursorEventQueue
 */
public WaitCursorEventQueue(int aDelay) 
{
	cDelay = aDelay;
	cWaitTimer = new WaitCursorTimer();
    cWaitTimer.setDaemon(true);
    cWaitTimer.start();
}
/**
 * Provide interface for dispatching events.
 * Attempts to handle turning wait cursor on when appropriate
 * @parameter  AWTEvent event
 */
protected void dispatchEvent(AWTEvent aEvent)
{
    try
    {
        // start the timer
        cWaitTimer.startTimer(aEvent.getSource());

        //System.out.println(aEvent.toString());
        
        // send off the event
        super.dispatchEvent(aEvent);

        // turn the timer off.
        cWaitTimer.stopTimer();

    }
    catch (NullPointerException npe)
    {
        // catch and handle null pointer exception
        Log.write(Log.SQL_EXCP, this, " dispatchEvent got a null pointer");
        Log.write(Log.SQL_EXCP, aEvent, aEvent.toString());
        RTSException rtse = new RTSException(RTSException.JAVA_ERROR, npe);
    }
    catch (Exception e)
    {
        // catch and handle any other exceptions
        Log.write(Log.SQL_EXCP, this, " dispatchEvent got an Exception");
        RTSException rtse = new RTSException(RTSException.JAVA_ERROR, e);
    }
    finally
    {
	    // turn the timer off.
        cWaitTimer.stopTimer();
    }
}
}
