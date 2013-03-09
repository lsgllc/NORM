package com.txdot.isd.rts.client.localoptions.business;

import com.txdot.isd.rts.services.exception.RTSException;
import java.util.Vector;
import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * RSPSUpdateProcess.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/07/2004	new class
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/20/2004	Reset up to act as a cache catch up instead
 *							as main stream update.
 *							modify run()
 *							defect 7135 Ver 5.2.1
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7891 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This class runs as a thread to process RSPS Status Updates
 *
 * @version	5.2.1			04/27/2005
 * @author	Ray Rowehl
 * <br>Creation Date:		07/07/2004 08:11:11
 */

public class RSPSUpdateProcess implements Runnable
{
	private boolean cbKeepRunning = true;
	private static boolean cbRunning = false;
	/**
	 * RSPSUpdateProcess constructor comment.
	 */
	public RSPSUpdateProcess()
	{
		super();
	}
	/**
	 * Returns the indicator of whether or not the thread
	 * should keep running
	 * @return boolean
	 */
	public boolean isKeepRunning()
	{
		return cbKeepRunning;
	}
	/**
	 * Returns the running state of this class
	 * @return boolean
	 */
	public static boolean isRunning()
	{
		return cbRunning;
	}
	/**
	 * Starts the application.
	 * @param args an array of command-line arguments
	 */
	public static void main(java.lang.String[] args)
	{
		// Insert code to start the application here.
	}
	/**
	 * This process runs as a thread that continues to attempt to
	 * post any RSPS Updates that have been posted to the queue.
	 *
	 * <p>When an object implementing interface <code>Runnable</code> is 
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
		Log.write(Log.SQL_EXCP, this, "Started Processing RSPS Queue.");
		setKeepRunning(true);
		setRunning(true);

		while (isKeepRunning())
		{
			try
			{
				Vector lvItemsToProcess =
					RSPSUpdCache.processQueueRequest(
						RSPSUpdCache.GET_QUEUE_ITEMS,
						null);

				// no more items to process
				if (lvItemsToProcess.size() < 1)
				{
					setKeepRunning(false);
					break;
				}

				// send request to server for processing
				if (com
					.txdot
					.isd
					.rts
					.client
					.desktop
					.RTSApplicationController
					.isDBUp())
				{
					Vector lvReturn =
						(Vector) Comm.sendToServer(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.PROCESS_RSPS_UPDT,
							lvItemsToProcess);

					lvReturn =
						RSPSUpdCache.processQueueRequest(
							RSPSUpdCache.DELETE_ITEMS,
							lvItemsToProcess);
				}
			}
			catch (RTSException leRTSEx)
			{
				// already logged by instantiation.  no need to log again.
				// sleep for a short time
				try
				{
					// sleep for a period of time to avoid filling the log
					Thread.sleep(2 * 60 * 1000);
				}
				catch (InterruptedException leIEx)
				{
					// just ignore this one.  Time to wake up.
				}
			}
		}

		try
		{
			// notify caller if he is still there.
			notify();
		}
		catch (IllegalMonitorStateException leIMSEx)
		{
			// Notify did not work.
			// just ignore
		}

		Log.write(
			Log.SQL_EXCP,
			this,
			"Finished Processing RSPS Queue.");
		setRunning(false);
	}
	/**
	 * Sets the boolean value
	 * 
	 * @param boolean abValue
	 */
	public void setKeepRunning(boolean abValue)
	{
		cbKeepRunning = abValue;
	}
	/**
	 * Sets the boolean value
	 * 
	 * @param boolean abValue
	 */
	public static void setRunning(boolean abValue)
	{
		cbRunning = abValue;
	}
}
