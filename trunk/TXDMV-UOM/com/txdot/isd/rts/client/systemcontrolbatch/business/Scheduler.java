package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.util.*;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;
import com.txdot.isd.rts.services.util.event.ThreadEvent;

/*
 *
 * Scheduler.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Michael A.	09/10/2001	Added comments
 * Michael A. 	09/14/2001	Changed class to use default values
 * R Hicks		08/07/2002 	Changed sleep calculation algorithm
 * S Govindappa 10/10/2002 	fixed defect# 4850(Client Batch doesn't 
 * 							recognize time lessthan 1:00 a.m). Removed
 * 							string arthemetic from run method to 
 * 							calculate shutdownHour and shutdownMinute
 * S Govindappa	10/10/2002	Fixed defect# 4425. Made changes to run 
 * 							method to start the Comm Server and non 
 * 							Data server before the batch start time as 
 * 							specified by CommServerBatchDelta parameter
 * 							in SystemProperty.
 * Jeff S.		03/03/2005	Get code to standard.
 *							defect 7897 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		08/02/2005	Added code to stagger the shutdowns.
 * 							modify run()
 * 							add calcTimeWithStagger()
 *							defect 8259 Ver 5.2.3
 * Jeff S.		10/07/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3 
 * Jeff S.		12/05/2005	During Constant cleanup then variable 
 * 							SHUTDOWN_MIN was set to "Shutdown hour = "
 * 							and needed to be "Shutdown min = ".
 *							defect 7897 Ver 5.2.3
 * K Harrell	04/11/2008	Default Substations to 1:00, Main Office to 
 * 							2:00
 * 							add DEFAULT_MAIN_OFFICE_BATCH_START_TIME, 
 * 							 DEFAULT_SUBSTATION_BATCH_START_TIME
 * 							modify run()
 * 							defect 8605 Ver Defect POS A 
 * M Reyes		09/09/2011	Loses time on its thread sleep in Win7
 * 							modify run()
 * 							defect 10991 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * The Scheduler reads in the system shutdown time from the 
 * RTS_BATCH_SCHEDULE table.  If it doesn't exist, it uses the default 
 * times of 2:00 am for system shutdown if substation 0; else 1:00 am.  
 * Thus, every workstation will shutdown and reload at the same time 
 * unless action is taken otherwise.
 * <p>The Scheduler determines the time until it should shutdown its 
 * 		listeners and then sleeps until that time.
 * 
 * <p>For 5.2.3 staggering has been introduced to the reboots.  All 
 * workstations are re-booted in groups.  Then number in the group is
 * determined by a value in the cfg.  The number of minutes to stagger
 * each group is determined by a value in the cfg as well.  Every group
 * is incrimented by the number of minutes to stagger times the group
 * number minus 1.
 * 
 * <p>E.x. 8 in a group. 5 minute stagger. 12 machines
 * First group of 8 reboot at the specified time.  The next group of 8
 * reboot 5 minutes later.
 * 
 * @version	6.8.1	09/09/2011			
 * @author	Michael Abernethy
 * <br>Creation Date:		09/05/2001	13:28:45
 */

public class Scheduler extends ThreadMessenger implements Runnable
{
	//	defect 8605
	//	private final static int DEFAULT_BATCH = 20000;
	private static final int DEFAULT_SUBSTATION_BATCH_START_TIME =
		10000;
	private static final int DEFAULT_MAIN_OFFICE_BATCH_START_TIME =
		20000;
	// end defect 8605

	/**
	*	Static fields for time computations
	*/
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// defect 10991
	//private static final int SECONDS_PER_MINUTE = 60;
	private static final int SECONDS_PER_MINUTE = 30;
	// end defect 10991
	private static final int HOURS_PER_DAY = 24;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int ONE = 1;
	private static final int ZERO = 0;
	private static final int TEN_THOUSAND = 10000;
	private static final int ONE_HUNDRED = 100;

	/**
	 * Constants
	 */
	private static final String OFFICE = AccountingConstant.OFC;
	private static final String SUBSTATION = AccountingConstant.SUB;
	private static final String HOUR = "Hour";
	private static final String MINUTE = "Minute";
	private static final String EMPTY_STRING = "";
	private static final String SHUTDOWN_HOUR = "Shutdown hour = ";
	private static final String SHUTDOWN_MIN = "Shutdown min  = ";
	private static final String START_TIME = "Start Time = ";
	private static final String STARTING_THREAD =
		"Starting Scheduler Thread.";
	private static final String SLEEP = "Sleep = ";
	private static final String FIRE_REBOOT = "Scheduler::Fire reboot";
	private static final String STAGGER_ERROR =
		"Error getting stagger reboot time using default - ";
		
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
	 * @param aarArgs String[]
	 */
	public static void main(String[] aarArgs)
	{
		Scheduler laScheduler = new Scheduler();
		laScheduler.run();
	}

	/**
	 * Scheduler.run() is designed to run correctly forever.
	 * <p>It sleeps until the next shutdown time and then awakens 
	 * 		to fire a ThreadEvent to its listeners.
	 * 
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		boolean lbRetry = true;
		BatchLog.write(STARTING_THREAD);

		// Get the current time and shutdown time
		Calendar laCurrentTime = Calendar.getInstance();
		Integer liStartTimeObj = null;

		try
		{
			Map hmMap = new HashMap();
			hmMap.put(
				OFFICE,
				new Integer(SystemProperty.getOfficeIssuanceNo()));
			hmMap.put(
				SUBSTATION,
				new Integer(SystemProperty.getSubStationId()));

			liStartTimeObj =
				(Integer) Comm.sendToServer(
					GeneralConstant.SYSTEMCONTROLBATCH,
					SystemControlBatchConstant.GET_START_TIME,
					hmMap);
		}
		// defect 8605 
		// Assign Start Time as a function of Substaid 
		catch (RTSException leRTSEx)
		{
			//liStartTimeObj = new Integer(DEFAULT_BATCH);
			liStartTimeObj = null;
		}

		//if (liStartTimeObj.intValue() == Integer.MIN_VALUE)
		if (liStartTimeObj == null
			|| liStartTimeObj.intValue() == Integer.MIN_VALUE)
		{
			//liStartTimeObj = new Integer(DEFAULT_BATCH);
			if (SystemProperty.getSubStationId() == 0)
			{
				liStartTimeObj =
					new Integer(DEFAULT_MAIN_OFFICE_BATCH_START_TIME);
			}
			else
			{
				liStartTimeObj =
					new Integer(DEFAULT_SUBSTATION_BATCH_START_TIME);
			}
		}
		// end defect 8605 

		int liStartTime = liStartTimeObj.intValue();

		BatchLog.write(START_TIME + liStartTime);
		// Removed string arthemetic to calculate shutdownHour and 
		// shutdownMinute to fix defect# 4850(Client Batch doesn't 
		// recognize time < 1:00 a.m)
		int liShutdownHour = liStartTime / TEN_THOUSAND;
		int liShutdownMinute =
			(liStartTime / ONE_HUNDRED) % ONE_HUNDRED;

		// defect 4425 
		if (SystemProperty.isCommServer()
			&& SystemProperty.isWorkstation())
		{
			int liCommServerBatchDelta =
				SystemProperty.getCommServerBatchDelta();

			if (liShutdownMinute >= liCommServerBatchDelta)
			{
				liShutdownMinute -= liCommServerBatchDelta;
			}
			else
			{
				if (liShutdownHour > ZERO)
				{
					liShutdownHour -= ONE;
					liShutdownMinute =
						liShutdownMinute
							+ MINUTES_PER_HOUR
							- liCommServerBatchDelta;
				}
				else
				{
					liShutdownHour = HOURS_PER_DAY - ONE;
					liShutdownMinute =
						liShutdownMinute
							+ MINUTES_PER_HOUR
							- liCommServerBatchDelta;
				}
			}
		}
		// defect 8259
		// Used to stagger the shutdown for all other workstations.
		// This will help with limiting the network usage between the
		// code server and the workstation.
		else
		{
			Hashtable lastaggerTime =
				calcTimeWithStagger(liShutdownHour, liShutdownMinute);
			liShutdownHour =
				Integer.parseInt(
					lastaggerTime.get(HOUR) + EMPTY_STRING);
			liShutdownMinute =
				Integer.parseInt(
					lastaggerTime.get(MINUTE) + EMPTY_STRING);
		}
		// end defect 8259
		// Defect 10991
		BatchLog.write(SHUTDOWN_HOUR + liShutdownHour);
		BatchLog.write(SHUTDOWN_MIN + liShutdownMinute);
		Log.write(Log.APPLICATION, this, "Reboot  Time is " 
			+ liShutdownHour 
			+ " " 
			+ liShutdownMinute);
//		System.out.println("Reboot  Time is " 
//						+ liShutdownHour 
//						+ " " 
//						+ liShutdownMinute);


		while (lbRetry)
		{
			laCurrentTime = Calendar.getInstance();
	int liCurrentHour = laCurrentTime.get(Calendar.HOUR_OF_DAY);
	int liCurrentMinute = laCurrentTime.get(Calendar.MINUTE);
	int liCurrentSeconds = laCurrentTime.get(Calendar.SECOND);
	
	Log.write(Log.APPLICATION, this, "Current Time is " 
		+ liCurrentHour 
		+ " " 
		+ liCurrentMinute);
//	System.out.println("Current Time is " 
//					+ liCurrentHour 
//					+ " " 
//					+ liCurrentMinute
//					+ " "
//					+ liCurrentSeconds);

	// When we hit the shutdown time, exit the loop.
	if ((liCurrentHour == liShutdownHour)
		&& (liCurrentMinute == liShutdownMinute))
	{
		lbRetry = false;
	}
	else
	{
		// Sleep and then check the time again.
		try
		{
			Thread.sleep(SECONDS_PER_MINUTE 
							* MILLISECONDS_PER_SECOND);
			lbRetry = true;
		}
		catch (InterruptedException aeIEx)
		{
			lbRetry = true;
		}
				
			}
		}
		
		//BatchLog.write(SHUTDOWN_HOUR + liShutdownHour);
		//BatchLog.write(SHUTDOWN_MIN + liShutdownMinute);

		//while (lbRetry)
//		{
//			lbRetry = false;
//			long llSleepTime = ZERO;
//			int liCurrentHour = laCurrentTime.get(Calendar.HOUR_OF_DAY);
//			int liCurrentMinute = laCurrentTime.get(Calendar.MINUTE);
//
//			//  if we happen to be at the time to shutdown, do not sleep
//			if ((liCurrentHour == liShutdownHour)
//				&& (liCurrentMinute == liShutdownMinute))
//			{
//				llSleepTime = ZERO;
//			}
//
//			// If it should shut down today at some point, calculate 
//			// time and then sleep until then
//			else if (
//				(liCurrentHour < liShutdownHour)
//					|| (liCurrentHour == liShutdownHour
//						&& liCurrentMinute < liShutdownMinute))
//			{
//				if (liCurrentMinute > liShutdownMinute)
//				{
//					llSleepTime =
//						(liShutdownHour - ONE - liCurrentHour)
//							* (MINUTES_PER_HOUR
//								* MILLISECONDS_PER_SECOND
//								* SECONDS_PER_MINUTE)
//							+ (liShutdownMinute
//								+ MINUTES_PER_HOUR
//								- liCurrentMinute)
//								* (MILLISECONDS_PER_SECOND
//									* SECONDS_PER_MINUTE);
//				}
//				else
//				{
//					llSleepTime =
//						(liShutdownHour - liCurrentHour)
//							* (MINUTES_PER_HOUR
//								* MILLISECONDS_PER_SECOND
//								* SECONDS_PER_MINUTE)
//							+ (liShutdownMinute - liCurrentMinute)
//								* (MILLISECONDS_PER_SECOND
//									* SECONDS_PER_MINUTE);
//				}
//			}
//			else
//			{
//				llSleepTime =
//					(HOURS_PER_DAY - ONE - liCurrentHour)
//						* (MINUTES_PER_HOUR
//							* MILLISECONDS_PER_SECOND
//							* SECONDS_PER_MINUTE)
//						+ (MINUTES_PER_HOUR - liCurrentMinute)
//							* (MILLISECONDS_PER_SECOND
//								* SECONDS_PER_MINUTE)
//						+ (liShutdownHour)
//							* (MINUTES_PER_HOUR
//								* MILLISECONDS_PER_SECOND
//								* SECONDS_PER_MINUTE)
//						+ (liShutdownMinute)
//							* (MILLISECONDS_PER_SECOND
//								* SECONDS_PER_MINUTE);
//			}
//
//			try
//			{
//				BatchLog.write(SLEEP + llSleepTime);
//				Thread.sleep(llSleepTime);
//
//			}
//			catch (InterruptedException leIEx)
//			{
//				lbRetry = true;
//			}
//		}
		// end defect 10991

		BatchLog.write(FIRE_REBOOT);
		fireThreadEvent(new ThreadEvent(ThreadEvent.REBOOT_SYS));
	}

	/**
	 * This method returns a hash that contains two int values.
	 * AbstractValue 1 Hour = Shutdown Hour
	 * AbstractValue 2 Minute = Shutdown Minute
	 * 
	 * These values represent the time the machine will reboot after
	 * the stagger has been added to the original time that was passed
	 * to this method.
	 * 
	 * The formula used is to get the assigned workstations from cache 
	 * and find out what group number the workstation is in.  The 
	 * workstations are put into groups of what ever number is in the 
	 * rtscls.cfg file then the goup number times the reboot number
	 * are multiplied.
	 * 
	 * e.x. There are 8 workstations in a group and a 5 second stagger.
	 * 		If there 10 workstations in a substation and when you get
	 * 		the list of workstations from cache and your seat is seat 7
	 * 		then your workstation in in the first group therefore there
	 * 		is no stagger time.  If the workstation in in seat 9 then 
	 * 		it would be part of the second group and the reboot would
	 * 		occur 5 minutes after the first group.
	 * 
	 * @param aiShutdownHour int
	 * @param aiShutdownMin int
	 * @return Hashtable
	 */
	private Hashtable calcTimeWithStagger(
		int aiShutdownHour,
		int aiShutdownMin)
	{
		Hashtable laReturnMap = new Hashtable();
		int liNewShutdownHour = aiShutdownHour;
		int liNewShutdownMin = aiShutdownMin;

		try
		{
			// Get the properties needed for the stagger.
			int listaggerDelta = SystemProperty.getRebootStaggerTime();
			int liRebootGroupNum = SystemProperty.getRebootNumber();

			// Get the workstations from cache
			Vector lvAsgndWsIds =
				AssignedWorkstationIdsCache.getAsgndWsIds(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId());

			// If the list of workstations is more than the max in a
			// group then we want to calculate the stagger time.
			if (lvAsgndWsIds.size() > liRebootGroupNum)
			{
				for (int i = 0; i < lvAsgndWsIds.size(); i++)
				{
					// If the WS id matches this workstation this 
					// is not the first group of workstaions then we
					// need to calculate the new reboot time.
					if (((AssignedWorkstationIdsData) lvAsgndWsIds
						.get(i))
						.getWsId()
						== SystemProperty.getWorkStationId())
					{
						// If this is not the first group then add 
						// stagger time to the current time passed
						if ((i + ONE) / liRebootGroupNum > 0)
						{
							// Take the group that you are in and
							// multiply that times the stagger value.
							// e.x. 11/8 = 1.3 - 1 * 5 min = 5 minutes
							listaggerDelta =
								((((i + ONE) / liRebootGroupNum))
									* listaggerDelta);

							// If the minutes passed plus the stagger
							// time is greaterthan or equal to 60 min
							// then we need to go to the next hour.
							if (liNewShutdownMin + listaggerDelta
								>= MINUTES_PER_HOUR)
							{
								// go to the next hour
								liNewShutdownHour++;
								// If the next hour is more than the max
								// then go back to 1
								if (liNewShutdownHour > HOURS_PER_DAY)
								{
									liNewShutdownHour = ONE;
								}

								// calculate the minutes that where more
								// than an hour.
								liNewShutdownMin =
									(liNewShutdownMin + listaggerDelta)
										- MINUTES_PER_HOUR;
							}
							// Add the stagger time to the current time
							else
							{
								liNewShutdownMin += listaggerDelta;
							}
						}
						// Must break because we found the correct WS.
						break;
					}
				}
			}
		}
		catch (RTSException leRTSEx)
		{
			BatchLog.write(STAGGER_ERROR + leRTSEx.toString());
		}

		// Put the hour and minutes in the hash
		laReturnMap.put(HOUR, liNewShutdownHour + EMPTY_STRING);
		laReturnMap.put(MINUTE, liNewShutdownMin + EMPTY_STRING);

		// Return the hashtable with the hours and minutes
		return laReturnMap;
	}
}