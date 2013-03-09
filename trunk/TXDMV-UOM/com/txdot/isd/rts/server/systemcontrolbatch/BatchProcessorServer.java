package com.txdot.isd.rts.server.systemcontrolbatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.server.db.BatchSchedule;
import com.txdot.isd.rts.services.data.BatchScheduleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;

/*
 *
 * BatchProcessorServer.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			06/05/2002	Added Phase2 Batch
 * MAbs			06/14/2002	defect 4255
 * MAbs			07/02/2002	Changing catch (RTSException) to catch 
 * 							(Exception)
 * Ray Rowehl	07/11/2002	Set the run time after each job.
 *							defect 4424  
 * Ray Rowehl	11/11/2002	Put second backup in a finally block so it 
 *							always executes.
 *							defect 4740.  
 * Ray Rowehl	01/18/2003	Remove InvHistExtract from Batch. 
 * 							defect 5111
 * K Harrell   	02/28/2003  Select from Batch_Schedule
 * 							defect 4857  
 * K Harrell   	03/05/2003  Set Batch_Running so that SendTrans will
 *                        	not run during batch
 * 							defect 5373  
 * Ray Rowehl	03/10/2003	Catch RTSException before catching Exception
 * 							defect 5769  
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add wasReprintSummarySuccessful
 * 							add consolidateReprintTables()
 * 							modify run()
 * 							Ver 5.2.0
 * K Harrell	04/29/2004	add	rollback/commit logic for reprint 
 *							sticker
 *							deprecate consolidateReprintTables()
 *							modify run()
 *							defect 7020 Ver 5.2.0
 * K Harrell	01/06/2004	delete deprecated consolidateReprintTables
 *							delete wasReprintSummarySuccessful
 *							Formatting/JavaDoc/Variable Name Cleanup 
 *							defect 7874 Ver 5.2.2
 * Ray Rowehl	03/29/2005	Change reference to BatchScheduleData
 * 							modify imports
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		10/07/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3
 * K Harrell	02/14/2006	Corrected typo on spelling of 
 * 							WRITING_RESULTS_SUCESSFUL
 * 							defect 7897 Ver 5.2.3
 * Ray Rowehl	09/11/2006	Use the Batch DB UserId and Password when 
 * 							in db restricted mode.
 * 							modify BatchProcessorServer(), run()
 * 							defect 8923 Ver 5.2.5
 * Ray Rowehl	10/03/2006	Migrate back into main stream.
 * 							defect 8959 Ver FallAdminTables
 * M Reyes		09/14/2007	Added System.getProperties()
 * K Harrell	12/10/2010	Add assignment of JobStartTime. 
 * 							Remove Logic for "Internet Processes" 
 * 							modify run()
 * 							defect 10694 Ver 6.7.0 
 * K Harrell	08/19/2011	Add ogic to update for 2nd DB Backup
 * 							add caBatchSchedData, caSysCtrlBatchSrvrBus
 * 							add getBatchProcesses(), updBatchProcess()
 * 							modify run()
 * 							defect 10976 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * Runs server side batch.
 * 
 * @version	6.8.1  	08/19/2011
 * @author 	Michael Abernethy 
 * @since			09/28/2001 14:19:48
 */

public class BatchProcessorServer implements Runnable
{
	private Purge caPurge;

	// Constants
	private static final String DASHES = "====================";

	private static final String WRITING_RESULTS = "Writing Batch Schedule results to BatchSchedule";

	private static final String WRITING_RESULTS_ERROR = "Batch Schedule results not written.  RTSException caught.";

	private static final String EXCEPTION_EQUALS = "Exception=";

	private static final String BATCH_PRO_ERROR = " Batch Processor Error ";

	private static final String WRITING_RESULTS_SUCESSFUL = "Batch Schedule results successfully written";

	private static final String SECOND_BACKUP_FAILED = "Second Backup Failed";

	// defect 10976
	private BatchScheduleData caBatchSchedData;
	private SystemControlBatchServerBusiness caSysCtrlBatchSrvrBus = new SystemControlBatchServerBusiness();
	// end defect 10976

	/**
	 * BatchProcesser constructor comment.
	 */
	public BatchProcessorServer()
	{
		super();
		// defect 8923
		caPurge = new Purge(SystemProperty.getDBUserBatch(),
				SystemProperty.getDBPasswordBatch());
		// end defect 8923
	}

	/**
	 * Main method. Launch BatchProcessorServer.
	 * 
	 * @param aaArgs
	 */
	public static void main(String[] aaArgs)
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		// needed to view classpath
		System.out.println(System.getProperties());
		BatchProcessorServer laBatch = new BatchProcessorServer();
		// start up the thread..
		Thread laThread = new Thread(laBatch);
		laThread.start();
	}

	/**
	 * Return Batch Processes
	 * 
	 * @throws RTSException
	 */
	private Vector getBatchProcesses() throws RTSException
	{
		// Set OfcIssuanceno, SubStaId for Server Batch
		Map laMap = new HashMap();
		laMap.put(AccountingConstant.OFC, new Integer(0));
		laMap.put(AccountingConstant.SUB, new Integer(0));

		// Use RTS_Batch_Schedule
		// Get Batch Processes
		return (Vector) caSysCtrlBatchSrvrBus.processData(
				GeneralConstant.SYSTEMCONTROLBATCH,
				SystemControlBatchConstant.GET_BATCH_PROCESS, laMap);
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used to
	 * create a thread, starting the thread causes the object's <code>run</code>
	 * method to be called in that separately executing thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may take
	 * any action whatsoever.
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		boolean lbJobSuccessful = false;
		
		// defect 10976 
		int liDBBkUp2JobSeqNo = -1;
		// end defect 10976 

		try
		{
			// Add dashes mark beginning of batch
			BatchLog.write(DASHES);
			BatchLog.write(WRITING_RESULTS);

			// Get Batch Processes
			Vector lvProcesses = getBatchProcesses();

			// Set BATCH_RUNNING so that SendTrans will pause
			Scheduler.BATCH_RUNNING = true;

			// For each Batch Process
			for (int i = 0; i < lvProcesses.size(); i++)
			{
				caBatchSchedData = (BatchScheduleData) lvProcesses
						.get(i);

				if (caBatchSchedData.getJobSeqNo() > 0)
				{
					// defect 10976
					caBatchSchedData.updDateTimeForBatch();
					String lsJobName = caBatchSchedData.getJobName();

					if (!UtilityMethods.isEmpty(lsJobName))
					{
						// end defect 10976

						// DB Backup (DBBACKUP)
						if (lsJobName.equals(BatchSchedule.DBBACKUP))
						{
							lbJobSuccessful = DBBatchProcess
									.callDBBackup1();
						}
						// Purge (PURGE)
						else if (lsJobName.equals(BatchSchedule.PURGE))
						{
							lbJobSuccessful = caPurge.beginPurge();
						}
						// defect 10976
						else if (lsJobName
								.equals(BatchSchedule.DBBKUP2))
						{
							liDBBkUp2JobSeqNo = caBatchSchedData
									.getJobSeqNo();
							break;
						}
						updBatchProcess();

						// Do Not Continue w/ Purge if 1st DB Backup Failed
						// Grab JobSeqNo for DBBCKUP2 
						if (!lbJobSuccessful
								&& lsJobName
										.equals(BatchSchedule.DBBACKUP))
						{
							for (int j=i+1; j< lvProcesses.size(); j++)
							{
								if (lsJobName
										.equals(BatchSchedule.DBBKUP2))
								{
									liDBBkUp2JobSeqNo = caBatchSchedData
											.getJobSeqNo();
								}
							}
							// end defect 10976
							break;
						}
					}
				}
			}
		}
		catch (RTSException aeRTSex)
		{
			BatchLog.write(WRITING_RESULTS_ERROR);

			BatchLog.write(aeRTSex.getMessage());
			BatchLog.write(aeRTSex.getDetailMsg());
		}
		catch (Exception aeEx)
		{
			BatchLog.write(WRITING_RESULTS_ERROR);
			BatchLog.write(aeEx.getMessage());
			BatchLog.write(EXCEPTION_EQUALS + aeEx);
			System.err.println(BATCH_PRO_ERROR
					+ (new RTSDate()).getDate()
					+ (new RTSDate()).get24HrTime());
			aeEx.printStackTrace();
		}

		// put second backup in a the finally block so it will
		// get executed even if other jobs fail.
		finally
		{
			try
			{
				// defect 10976
				// Update BATCH_SCHEDULE if entry exists 
				lbJobSuccessful = DBBatchProcess.callDBBackup2();

				// Update entry for DBBKUP2
				if (liDBBkUp2JobSeqNo != -1)
				{
					caBatchSchedData = new BatchScheduleData(0, 0,
							liDBBkUp2JobSeqNo);
					caBatchSchedData.setJobStatus(lbJobSuccessful);
					caBatchSchedData.updDateTimeForBatch();
					updBatchProcess();
				}
				// Update entry for JobSeqNo 0
				caBatchSchedData = new BatchScheduleData(0, 0, 0);
				caBatchSchedData.updDateTimeForBatch();
				updBatchProcess();
				// end defect 10976

				Scheduler.BATCH_RUNNING = false;
				BatchLog.write(WRITING_RESULTS_SUCESSFUL);
			}
			catch (Exception aeEx)
			{
				Scheduler.BATCH_RUNNING = false;
				BatchLog.write(SECOND_BACKUP_FAILED);
				if (aeEx.getMessage() != null)
				{
					BatchLog.write(aeEx.getMessage());
				}
			}
		}
	}

	/**
	 * Update Batch Schedule
	 * 
	 * @throws RTSException 
	 */
	private void updBatchProcess() throws RTSException
	{
		caSysCtrlBatchSrvrBus.processData(
				GeneralConstant.SYSTEMCONTROLBATCH,
				SystemControlBatchConstant.UPDATE_BATCH_RESULTS_SERVER,
				caBatchSchedData);
	}
}