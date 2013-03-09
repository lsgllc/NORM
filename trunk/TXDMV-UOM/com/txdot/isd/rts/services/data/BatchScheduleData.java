package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.server.db.BatchSchedule;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * BatchScheduleData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * Ray Rowehl	03/29/2005	Move to services.data
 * 							Move status strings
 * 							add SUCCESSFUL, UNSUCCESSFUL, COMPLETE,
 * 								INCOMPLETE, SKIPPED, TTL_PACK, B_INV_RP,
 * 								SUB_SUMM, CTY_WIDE, COMP_SET, INTERNT,
 * 								SPIADDR, INV_HIST, INV_HIST, PURGE, 
 * 								DBBACKUP, INET, CONS
 * 							modify package.
 * 							defect 7705 Ver 5.2.3
 * K Harrell	04/22/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	05/09/2007	add SPCLPLTS
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/09/2009	delete SPIADDR, SPCLPLTS 
 * 							defect 9941 Ver Defect_POS_D
 * K Harrell	02/09/2009	add IDEP 
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	12/10/2010	add ciJobStartTime, get/set methods
 *  						delete INTERNT, INTERNET, INET_DEPOSIT
 * 							defect 10694 Ver 6.7.0 
 * K Harrell	08/19/2011	add DBBKUP2 
 * 							add BatchScheduleData(), 
 * 							  BatchScheduleData(int,int),
 * 							  BatchScheduleData(int,int)
 * 							add updDateTimeForBatch(), 
 * 							  setJobStatus(boolean) 
 * 							defect 10976 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for BatchScheduleData
 * 
 * @version 6.8.1 	08/19/2011
 * @author Kathy Harrell 
 * @since 			10/24/2001
 */

public class BatchScheduleData implements Serializable
{
	public final static String B_INV_RP = "B_INV_RP";

	public final static String COMP_SET = "COMP_SET";

	public final static String COMPLETE = "COMPLETE";

	public final static String CTY_WIDE = "CTY_WIDE";

	public final static String DBBACKUP = "DBBACKUP";

	// defect 10976
	public final static String DBBKUP2 = "DBBKUP2";
	// end defect 10976

	public final static String INCOMPLETE = "INCOMPLETE";

	public final static String INV_HIST = "INV_HIST";

	public final static String PURGE = "PURGE";

	// defect 10694
	// public final static String INTERNT = "INTERNT";
	// public final static String INTERNET = "INET";
	// public final static String INET_DEPOSIT = "IDEP";
	// end defect 10694

	public final static String SKIPPED = "SKIPPED";

	public final static String SUB_SUMM = "SUB_SUMM";

	public final static String SUCCESSFUL = "SUCCESSFUL";

	public final static String TTL_PACK = "TTL_PACK";

	public final static String UNSUCCESSFUL = "UNSUCCESSFUL";

	// int
	protected int ciJobRunDate;

	protected int ciJobRunTime;

	protected int ciJobSeqNo;

	// defect 10694
	protected int ciJobStartTime;
	// end defect 10694

	protected int ciLastRunDate;

	protected int ciLastRunTime;

	protected int ciOfcIssuanceNo;

	protected int ciStartTime;

	protected int ciSubStaId;

	// String
	protected String csBatchStatus;

	protected String csJobName;

	protected String csJobStatus;

	private final static long serialVersionUID = -6042991601234439644L;

	/**
	 * BatchScheduleData.java Constructor
	 */
	public BatchScheduleData()
	{
		
	}
	
	/**
	 * BatchScheduleData.java Constructor
	 */
	public BatchScheduleData(int aiOfcIssuanceNo, int aiSubstaid, int aiJobSeqNo)
	{
		super(); 
		ciOfcIssuanceNo = aiOfcIssuanceNo; 
		ciSubStaId = aiSubstaid; 
		ciJobSeqNo = aiJobSeqNo; 
	}
	
	/**
	 * BatchScheduleData.java Constructor
	 */
	public BatchScheduleData(int aiOfcIssuanceNo, int aiSubstaid)
	{
		super(); 
		ciOfcIssuanceNo = aiOfcIssuanceNo; 
		ciSubStaId = aiSubstaid; 
	}

	/**
	 * Returns the value of BatchStatus
	 * 
	 * @return String
	 */
	public final String getBatchStatus()
	{
		return csBatchStatus;
	}

	/**
	 * Returns the value of JobName
	 * 
	 * @return String
	 */
	public final String getJobName()
	{
		return csJobName;
	}

	/**
	 * Returns the value of JobRunDate
	 * 
	 * @return int
	 */
	public final int getJobRunDate()
	{
		return ciJobRunDate;
	}

	/**
	 * Returns the value of JobRunTime
	 * 
	 * @return int
	 */
	public final int getJobRunTime()
	{
		return ciJobRunTime;
	}

	/**
	 * Returns the value of JobSeqNo
	 * 
	 * @return int
	 */
	public final int getJobSeqNo()
	{
		return ciJobSeqNo;
	}

	/**
	 * This method sets the value of JobStartTime
	 * 
	 * @return int
	 */
	public int getJobStartTime()
	{
		return ciJobStartTime;
	}

	/**
	 * Returns the value of JobStatus
	 * 
	 * @return String
	 */
	public final String getJobStatus()
	{
		return csJobStatus;
	}

	/**
	 * Returns the value of LastRunDate
	 * 
	 * @return int
	 */
	public final int getLastRunDate()
	{
		return ciLastRunDate;
	}

	/**
	 * Returns the value of LastRunTime
	 * 
	 * @return int
	 */
	public final int getLastRunTime()
	{
		return ciLastRunTime;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of StartTime
	 * 
	 * @return int
	 */
	public final int getStartTime()
	{
		return ciStartTime;
	}

	/**
	 * Returns the value of SubStaId
	 * 
	 * @return int
	 */
	public final int getSubStaId()
	{
		return ciSubStaId;
	}

	/**
	 * Initialize for Server Batch Status Update
	 */
	public final void updDateTimeForBatch()
	{
		RTSDate laRTSDate = RTSDate.getCurrentDate();
		int liRunDate = laRTSDate.getYYYYMMDDDate();
		int liRunTime = laRTSDate.get24HrTime();
		if (getJobSeqNo() == 0)
		{
			setLastRunDate(liRunDate);
			setLastRunTime(liRunTime);
		}
		else
		{
			setJobRunDate(liRunDate);
			setJobRunTime(liRunTime);
			setJobStartTime(liRunTime);
		}
	}

	/**
	 * This method sets the value of BatchStatus.
	 * 
	 * @param asBatchStatus
	 */
	public final void setBatchStatus(String asBatchStatus)
	{
		csBatchStatus = asBatchStatus;
	}

	/**
	 * This method sets the value of JobName.
	 * 
	 * @param asJobName
	 */
	public final void setJobName(String asJobName)
	{
		csJobName = asJobName;
	}

	/**
	 * This method sets the value of JobRunDate.
	 * 
	 * @param aiJobRunDate
	 */
	public final void setJobRunDate(int aiJobRunDate)
	{
		ciJobRunDate = aiJobRunDate;
	}

	/**
	 * This method sets the value of JobRunTime.
	 * 
	 * @param aiJobRunTime
	 */
	public final void setJobRunTime(int aiJobRunTime)
	{
		ciJobRunTime = aiJobRunTime;
	}

	/**
	 * This method sets the value of JobSeqNo.
	 * 
	 * @param aiJobSeqNo
	 */
	public final void setJobSeqNo(int aiJobSeqNo)
	{
		ciJobSeqNo = aiJobSeqNo;
	}

	/**
	 * This method returns the value of JobStartTime
	 * 
	 * @param aiJobStartTime
	 */
	public void setJobStartTime(int aiJobStartTime)
	{
		ciJobStartTime = aiJobStartTime;
	}
	
	/**
	 * This method sets the value of JobStatus.
	 * 
	 * @param abSuccessful
	 */
	public final void setJobStatus(boolean abSuccessful)
	{
		csJobStatus = abSuccessful ?
		BatchSchedule.SUCCESSFUL : 
			BatchSchedule.UNSUCCESSFUL;
	}

	/**
	 * This method sets the value of JobStatus.
	 * 
	 * @param asJobStatus
	 */
	public final void setJobStatus(String asJobStatus)
	{
		csJobStatus = asJobStatus;
	}

	/**
	 * This method sets the value of LastRunDate.
	 * 
	 * @param aiLastRunDate
	 */
	public final void setLastRunDate(int aiLastRunDate)
	{
		ciLastRunDate = aiLastRunDate;
	}

	/**
	 * This method sets the value of LastRunTime.
	 * 
	 * @param aiLastRunTime
	 */
	public final void setLastRunTime(int aiLastRunTime)
	{
		ciLastRunTime = aiLastRunTime;
	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of StartTime.
	 * 
	 * @param aiStartTime
	 */
	public final void setStartTime(int aiStartTime)
	{
		ciStartTime = aiStartTime;
	}

	/**
	 * This method sets the value of SubStaId.
	 * 
	 * @param aiSubStaId
	 */
	public final void setSubStaId(int aiSubStaId)
	{
		ciSubStaId = aiSubStaId;
	}
}
