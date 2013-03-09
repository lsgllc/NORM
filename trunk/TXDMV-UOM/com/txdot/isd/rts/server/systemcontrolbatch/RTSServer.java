package com.txdot.isd.rts.server.systemcontrolbatch;

import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 *
 * RTSServer.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MABs			06/10/2002	Adding Log.write() to ensure proper start
 * Ray Rowehl	05/17/2004	Set up to switch to R99 Trans when needed
 *							modify constructor(), main()
 * 							add cbR99Switch
 *							defect 6785 Ver 5.2.0
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		10/10/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3
 * Ray Rowehl	10/03/2006	Migrate back to main project.
 * 							defect 8959 Ver FallAdminTables
 * ---------------------------------------------------------------------
 */

/**
 * This starts up Server Side Batch and SendTrans on the server.
 *
 * @version	FallAdminTables	10/03/2006
 * @author	Michael Abernethy
 * <p>Creation Date:		09/06/2001 09:33:06
 */

public class RTSServer implements ThreadListener, Runnable
{
	public java.lang.String ERROR_MESSAGE =
		"There was an error in the RTSServer ";
	private static final String ARGS_ERROR =
		"To run specific office issuance numbers in Send Trans,"
			+ " use    RTSServer <start OFC> <end OFC> <boolean>"
			+ " The boolean is optional";
	private static final String TRUE = "true";
	private static final String STARTING_RTS_SERVER =
		"Starting RTSServer";
	private static final String SCHEDULER_THREAD = "Scheduler";
	private static final String SNDCACHE_THREAD = "SendTrans";

	// defect 6785
	private int ciStartOfc;
	private int ciEndOfc;
	private boolean cbR99Switch = false;
	// end defect 6785

	private final static int DEFAULT_FIRST_OFCISSUANCE_NO = 1;
	private final static int DEFAULT_LAST_OFCISSUANCE_NO = 300;
	/**
	 * Constructor for RTSServer.
	 */
	public RTSServer()
	{
		// defect 6785
		this(
			DEFAULT_FIRST_OFCISSUANCE_NO,
			DEFAULT_LAST_OFCISSUANCE_NO,
			false);
		// end defect 6785
	}
	// end defect 6785
	/**
	 * Constructor for RTSServer.
	 * 
	 * @param aiStartOfc int
	 * @param aiEndOfc int
	 */
	public RTSServer(int aiStartOfc, int aiEndOfc)
	{
		// defect 6785
		this(aiStartOfc, aiEndOfc, false);
		// end defect 6785
	}
	/**
	 * Constructor for RTSServer.
	 * 
	 * @param aiStartOfc int
	 * @param aiEndOfc int
	 * @param abR99Switch boolean
	 */
	public RTSServer(int aiStartOfc, int aiEndOfc, boolean abR99Switch)
	{
		// defect 6785
		ciStartOfc = aiStartOfc;
		ciEndOfc = aiEndOfc;
		cbR99Switch = abR99Switch;
		// end defect 6785
	}
	/**
	 * Start the thread.
	 */
	public void go()
	{
		Thread t = new Thread(this);
		t.start();
	}
	/**
	 * Handle Thread Events.
	 * 
	 * @param ThreadEvent aaThreadEvent
	 */
	public void handleThreadEvent(
		com
			.txdot
			.isd
			.rts
			.services
			.util
			.event
			.ThreadEvent aaThreadEvent)
	{
		switch (aaThreadEvent.getEventCode())
		{
			case ThreadEvent.START_BATCH :
				{
					// Create the BatchProcessorServer		
					BatchProcessorServer laBatch = new BatchProcessorServer();
					Thread laThreadBatch = new Thread(laBatch);
					laThreadBatch.start();
					break;
				}

			case ThreadEvent.STOP_MAIN :
				{
					synchronized (this)
					{
						notify();
					}
					break;
				}
		}
	}
	/**
	 * Start up RTSServer.
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args)
	{
		RTSServer laRTSServer = null;

		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);

		// defect 6785
		// use a boolean to determine if we will use R99 transid

		// args length should be 2 or 3, otherwise use default values
		if (args.length < 2 || args.length > 3)
		{
			// invalid numer of args
			System.out.println(ARGS_ERROR);

			// use default values
			laRTSServer =
				new RTSServer(
					DEFAULT_FIRST_OFCISSUANCE_NO,
					DEFAULT_LAST_OFCISSUANCE_NO,
					false);
		}
		else
		{
			// bring in the values
			String lsArg1 = args[0];
			String lsArg2 = args[1];

			// check to see if the boolean is there and use it.
			boolean lbArg3 = false;

			if (args.length == 3)
			{
				if (((String) args[3]).trim().equalsIgnoreCase(TRUE))
				{
					lbArg3 = true;
				}
			}

			laRTSServer =
				new RTSServer(
					Integer.parseInt(lsArg1),
					Integer.parseInt(lsArg2),
					lbArg3);
		}

		// end defect 6785

		// start up the thread..
		Thread laThread = new Thread(laRTSServer);
		laThread.start();
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
		Log.write(Log.APPLICATION, this, STARTING_RTS_SERVER);
		BatchLog.write(STARTING_RTS_SERVER);

		//		Create the Scheduler to keep track of timing
		Scheduler laScheduler = new Scheduler();
		laScheduler.addThreadListener(this);
		Thread laThreadScheduler =
			new Thread(laScheduler, SCHEDULER_THREAD);
		laThreadScheduler.start();

		//		Create the SendTrans thread
		// defect 6785
		// add C99 boolean switch
		SendTrans laSendTrans =
			new SendTrans(ciStartOfc, ciEndOfc, cbR99Switch);
		// end defect 6785

		Thread laThreadSendTrans =
			new Thread(laSendTrans, SNDCACHE_THREAD);
		laThreadSendTrans.start();

		//		Wait until told to shut down RTSServer
		synchronized (this)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException leIEx)
			{
				System.err.println(
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ leIEx.getMessage());
				Log.write(
					Log.START_END,
					this,
					ERROR_MESSAGE
						+ CommonConstant.SYSTEM_LINE_SEPARATOR
						+ leIEx.getMessage());
			}
		}

		// Stop all the threads it created and then break out of 
		// run() method.
		// This will effectively stop the entire RTSII Application on 
		// the server.
		laSendTrans.kill();

		laScheduler = null;
		laThreadScheduler = null;
		laSendTrans = null;
		laThreadSendTrans = null;
	}
}