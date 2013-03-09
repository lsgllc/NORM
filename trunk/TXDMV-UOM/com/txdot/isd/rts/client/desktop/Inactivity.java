package com.txdot.isd.rts.client.desktop;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * Inactivity.java
 * 
 * (c) Texas Department of Transaporation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/20/2003	Logout if on Windows
 *							modify run()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	01/29/2004	Do not logout if there is an active event.
 *							Also add console outputs in development mode
 *							to track timer on and offs.
 *							modify run(), stopTimer(), startTimer()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * K Harrell	03/28/2004	Remove reference to isWindowsPlatform()
 *							modify run()
 *							defect 6955 Ver 5.2.0
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							Format comments, Hungarian notation for 
 * 							variables. 
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	07/16/2005	More constants work
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	10/12/2005	Check keep running before sleeping.
 * 							modify run()
 * 							defect 8405 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Keeps a thread running to throw up the login screen after 10 minutes
 * have elapsed.
 *
 * @version	5.2.3  			10/12/2005
 * @author	Michael Abernethy
 * <br>Creation date: 		10/01/2001 09:31:44
 */

public class Inactivity implements Runnable
{
	private static final String TXT_TIMER_OFF = "Turning timer off";
	private static final String TXT_TIMER_ON = "Turning timer on";
	private static final int LOOP_10_TIMES = 10;
	private static final int MINUTES_1 = 1000 * 60;
	private volatile boolean cbKeepRunning;
	private static volatile int ciMinIdle;
	private RTSApplicationController caAppController;
	private static volatile boolean cbTimerOff;
	/**
	 * Creates the Inactivity thread with an graph of the
	 * RTSApplicationController
	 * 
	 * @param RTSApplicationController aaAppController
	 */
	public Inactivity(RTSApplicationController aaAppController)
	{
		super();
		this.caAppController = aaAppController;
		cbTimerOff = true;
		cbKeepRunning = true;
	}
	/**
	 * Kills the Inactivity thread
	 */
	public void kill()
	{
		cbKeepRunning = false;
	}
	/**
	 * When an object implementing interface <code>Runnable</code> is  
	 * used to create a thread, starting the thread causes the object's 
	 * <code>run</code> method to be called in that separately executing 
	 * thread. 
	 * <p>
	 * The general contract of the method <code>run</code> is that it  
	 * may take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		while (cbKeepRunning)
		{
			try
			{
				if (cbTimerOff)
				{
					synchronized (this)
					{
						this.wait();
					}
				}
				// Sleep for a minute
				
				// defect 8405 
				// Do not sleep if we are being killed
				if (cbKeepRunning)
				{
					Thread.sleep(MINUTES_1);
				}
				// end defect 8405
				
				ciMinIdle = ciMinIdle + 1;
				// If it's idle for 10 minutes, throw up the login  
				// screen if the pending trans screen isn't showing
				if (ciMinIdle == LOOP_10_TIMES)
				{
					ciMinIdle = 0;
					// check controller stack size before calling logout
					if (!caAppController.isPendingTransVisible()
						&& !caAppController.getMediator().
						hasActiveEvent())
					{
						// do a logout 
						UtilityMethods.doWindowsLogout(caAppController);
					}
				}
			}
			catch (InterruptedException aeInterruptEx)
			{
				// nothing to do is interrupted
			}
		}
	}
	/**
	 * Starts the timer
	 */
	public void startTimer()
	{
		// print a message to the console indicating timer on
		if (com
			.txdot
			.isd
			.rts
			.services
			.util
			.SystemProperty
			.getProdStatus()
			!= 0)
		{
			System.out.println(TXT_TIMER_ON);
		}
		ciMinIdle = 0;
		cbTimerOff = false;
		synchronized (this)
		{
			this.notify();
		}
	}
	/**
	 * Stops the timer
	 */
	public static void stopTimer()
	{
		// print a message to the console indicating timer off
		if (com
			.txdot
			.isd
			.rts
			.services
			.util
			.SystemProperty
			.getProdStatus()
			!= 0)
		{
			System.out.println(TXT_TIMER_OFF);
		}
		cbTimerOff = true;
	}
}
