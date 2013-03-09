package com.txdot.isd.rts.server.systemcontrolbatch;

import java.util.Calendar;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.BatchSchedule;

import com.txdot.isd.rts.services.data.BatchScheduleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;

/*
 *
 * Scheduler.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M. Abernethy	09/10/2001	Added comments
 * M. Abernethy 09/14/2001	Changed class to use default values
 * MAbs			05/21/2002	CQU100004024
 * K. Harrell	03/05/2003	New variable Batch_Running
 *                          SendTrans should sleep during Batch
 * 							defect 5373
 * Ray Rowehl	03/29/2005	Change reference to BatchScheduleData
 * 							modify imports
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		10/10/2005	Constant cleanup.
 * 							delete DAYS_IN_WEEK (not used)
 *							defect 7897 Ver 5.2.3
 * Ray Rowehl	10/03/2006	Migrate back to main project.
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	08/19/2011	implement new BatchScheduleData constructor
 * 							modify run()
 * 							defect 10976 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * The Scheduler reads in the system shutdown time and the system reboot time
 * from the SystemProperty. If it doesn't exist, it uses the default times of
 * 2:00am for system shutdown and monday for reboot time. Thus, every
 * workstation will shutdown and reload at the same time unless action is taken
 * otherwise.
 * 
 * <p>
 * The Scheduler determines the time until it should shutdown its listeners and
 * then sleeps until that time.
 * 
 * @version 6.8.1			08/19/2011
 * @author: Michael Abernethy
 * @since					09/05/2001 13:28:45
 */

public class Scheduler
		extends
		com.txdot.isd.rts.client.systemcontrolbatch.business.ThreadMessenger
		implements Runnable
{

	/**
	 * Default times should the value not be found in the SystemProperty.
	 */
	private final static String DEFAULT_BATCH = "000100";

	/**
	 * Static fields for time computations
	 */
	private final static int MILLISECONDS_PER_SECOND = 1000;

	private final static int SECONDS_PER_MINUTE = 60;

	private final static int HOURS_PER_DAY = 23;

	private final static int MINUTES_PER_HOUR = 60;

	public static boolean BATCH_RUNNING = false;

	/**
	 * The error message to display.
	 */
	private final static java.lang.String ERROR_MESSAGE = "An error occurred while the Scheduler was sleeping.";

	private final static int SERVER_OFC = 0;

	private final static int SERVER_SUB = 0;

	private final static int SERVER_JOBNO = 0;

	/**
	 * Creates a Scheduler.
	 */
	public Scheduler()
	{
		super();
	}

	/**
	 * For testing the Scheduler.
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(String[] args)
	{
		Scheduler s = new Scheduler();
	}

	/**
	 * Scheduler.run() is designed to run correctly forever.
	 * <p>
	 * It sleeps until the next shutdown time and then awakens to fire a
	 * ThreadEvent to its listeners. If it is the day for a reboot, it fires a
	 * ThreadEvent.REBOOT_SYS instead.
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		while (true)
		{
			Calendar laCurrentTime = Calendar.getInstance();
			Integer liStartTimeObj = null;
			String lsStartTime = CommonConstant.STR_SPACE_EMPTY;
			DatabaseAccess laDBAccess = null;
			try
			{
				laDBAccess = new DatabaseAccess();
				laDBAccess.beginTransaction();
				BatchSchedule laBatchSchedule = new BatchSchedule(
						laDBAccess);
				// defect 10976
				// BatchScheduleData laBatchData = new BatchScheduleData();
				// laBatchData.setOfcIssuanceNo(SERVER_OFC);
				// laBatchData.setSubStaId(SERVER_SUB);
				// laBatchData.setJobSeqNo(SERVER_JOBNO);
				BatchScheduleData laBatchData = new BatchScheduleData(
						SERVER_OFC, SERVER_SUB, SERVER_JOBNO);
				// end defect 10976
				Vector lvBatchScheduleList = laBatchSchedule
						.qryBatchSchedule(laBatchData);
				if (lvBatchScheduleList.size() == 0)
				{
					lsStartTime = DEFAULT_BATCH;
				}
				else
				{
					BatchScheduleData laResultData = (BatchScheduleData) lvBatchScheduleList
							.get(0);
					liStartTimeObj = new Integer(laResultData
							.getStartTime());
				}
			}
			catch (RTSException leRTSEx)
			{
				lsStartTime = DEFAULT_BATCH;
			}
			finally
			{
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.NONE);
				}
				catch (RTSException leRTSEx)
				{
					// Take no action on the exception
				}
				finally
				{
					// null out the dbaccess to clear is out
					laDBAccess = null;
				}
			}
			if (lsStartTime.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				lsStartTime = Integer.toString(liStartTimeObj
						.intValue());
			}
			int liCurrentHour = laCurrentTime.get(Calendar.HOUR_OF_DAY);
			int liCurrentMinute = laCurrentTime.get(Calendar.MINUTE);
			int liShutdownHour = 0;
			if (lsStartTime.length() > 4)
			{
				liShutdownHour = Integer.parseInt(lsStartTime
						.substring(0, lsStartTime.length() - 4));
			}
			while (lsStartTime.length() < 4)
			{
				lsStartTime = CommonConstant.STR_ZERO + lsStartTime;
			}
			int liShutdownMinute = Integer.parseInt(lsStartTime
					.substring(lsStartTime.length() - 4, lsStartTime
							.length() - 2));
			if ((liCurrentHour < liShutdownHour)
					|| (liCurrentHour == liShutdownHour && liCurrentMinute <= liShutdownMinute))
			{
				long llSleepTime = (MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE)
						* ((liShutdownHour * MINUTES_PER_HOUR + liShutdownMinute) - (liCurrentHour
								* MINUTES_PER_HOUR + liCurrentMinute));
				try
				{
					Thread.sleep(llSleepTime);
					fireThreadEvent(new ThreadEvent(
							ThreadEvent.START_BATCH));
					// sleep so that a loop of events don't get fired
					Thread.sleep(MILLISECONDS_PER_SECOND
							* SECONDS_PER_MINUTE * 2);
				}
				catch (InterruptedException leIE)
				{
					System.err.println(ERROR_MESSAGE);
					Log.write(Log.START_END, this, ERROR_MESSAGE);
				}
			}
			else
			{
				long llSleepTime = (MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE)
						* ((HOURS_PER_DAY * MINUTES_PER_HOUR + MINUTES_PER_HOUR) - (liCurrentHour
								* MINUTES_PER_HOUR + liCurrentMinute));
				try
				{
					Thread.sleep(llSleepTime);
				}
				catch (InterruptedException leIE)
				{
					System.err.println(ERROR_MESSAGE);
					Log.write(Log.START_END, this, ERROR_MESSAGE);
				}
			}
		}
	}
}