package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.BatchScheduleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * BatchSchedule.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown      08/20/2003  CQU100004885, PCR24  Added constant
 *                          public final static String SPIADDR = "SPIADDR"
 * Ray Rowehl	03/28/2005	Move BatchScheduleData to services.
 * 							Move status constants down to data class
 * 							change imports.
 * 							delete SUCCESSFUL, UNSUCCESSFUL, COMPLETE,
 * 								INCOMPLETE, SKIPPED, TTL_PACK, B_INV_RP,
 * 								SUB_SUMM, CTY_WIDE, COMP_SET, INTERNT,
 * 								SPIADDR, INV_HIST, INV_HIST, PURGE, 
 * 								DBBACKUP, INET, CONS
 * 							defect 7705 Ver 5.2.3
 * K Harrell	05/11/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3  
 * K Harrell	12/10/2010	Record Job Start Time 
 * 							modify updBatchSceduleJob() 
 * 							defect 10694 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_BATCH_SCHEDULE
 *
 * @version	6.7.0		12/10/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	10/24/2001 14:14:19
 */

public class BatchSchedule extends BatchScheduleData
{
	DatabaseAccess caDA;

	/**
	 * BatchSchedule constructor comment.
	 */
	public BatchSchedule()
	{
		super();
	}
	/**
	 * BatchSchedule constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public BatchSchedule(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_BATCH_SCHEDULE
	 * 
	 * @param  aaBatchScheduleData	BatchScheduleData		
	 * @return Vector 
	 * @throws RTSException 	
	 */
	public Vector qryBatchSchedule(BatchScheduleData aaBatchScheduleData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryBatchSchedule - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubStaId,"
				+ "StartTime,"
				+ "BatchStatus,"
				+ "LastRunDate,"
				+ "LastRunTime,"
				+ "JobSeqNo,"
				+ "JobName,"
				+ "JobStatus,"
				+ "JobRunDate,"
				+ "JobRunTime "
				+ "FROM RTS.RTS_Batch_Schedule "
				+ "Where OfcIssuanceno = ? and SubstaId = ? "
				+ "order by JobSeqNo");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getSubStaId())));

			Log.write(
				Log.SQL,
				this,
				" - qryBatchSchedule - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryBatchSchedule - SQL - End");

			while (lrsQry.next())
			{
				BatchScheduleData laBatchScheduleData =
					new BatchScheduleData();
				laBatchScheduleData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laBatchScheduleData.setSubStaId(
					caDA.getIntFromDB(lrsQry, "SubStaId"));
				laBatchScheduleData.setStartTime(
					caDA.getIntFromDB(lrsQry, "StartTime"));
				laBatchScheduleData.setBatchStatus(
					caDA.getStringFromDB(lrsQry, "BatchStatus"));
				laBatchScheduleData.setLastRunDate(
					caDA.getIntFromDB(lrsQry, "LastRunDate"));
				laBatchScheduleData.setLastRunTime(
					caDA.getIntFromDB(lrsQry, "LastRunTime"));
				laBatchScheduleData.setJobSeqNo(
					caDA.getIntFromDB(lrsQry, "JobSeqNo"));
				laBatchScheduleData.setJobName(
					caDA.getStringFromDB(lrsQry, "JobName"));
				laBatchScheduleData.setJobStatus(
					caDA.getStringFromDB(lrsQry, "JobStatus"));
				laBatchScheduleData.setJobRunDate(
					caDA.getIntFromDB(lrsQry, "JobRunDate"));
				laBatchScheduleData.setJobRunTime(
					caDA.getIntFromDB(lrsQry, "JobRunTime"));
				// Add element to the Vector
				lvRslt.addElement(laBatchScheduleData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryBatchSchedule - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryBatchSchedule - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_BATCH_SCHEDULE
	 *
	 * @param  aaBatchScheduleData	BatchScheduleData		
	 * @throws RTSException 
	 */
	public void updBatchSchedule(BatchScheduleData aaBatchSceduleData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updBatchSchedule - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_Batch_Schedule set "
				+ "BatchStatus = ?, "
				+ "LastRunDate = ?, "
				+ "LastRunTime = ? "
				+ "WHERE "
				+ "OfcIssuanceno = ? AND "
				+ "SubStaId = ? AND "
				+ "JobSeqNo = ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaBatchSceduleData.getBatchStatus())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchSceduleData.getLastRunDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchSceduleData.getLastRunTime())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchSceduleData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchSceduleData.getSubStaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchSceduleData.getJobSeqNo())));
			Log.write(Log.SQL, this, "updBatchSchedule - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updBatchSchedule - SQL - End");
			Log.write(Log.METHOD, this, "updBatchSchedule - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updBatchSchedule - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
	/**
	 * Method to update a RTS.RTS_BATCH_SCHEDULE for a given job
	 *
	 * @param  aaBatchScheduleData	BatchScheduleData		
	 * @throws RTSException
	 */
	public void updBatchScheduleJob(BatchScheduleData aaBatchScheduleData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updBatchScheduleJob - Begin");

		Vector lvValues = new Vector();

		// defect 10694 
		// Add JobStartTime 
		String lsUpd =
			"UPDATE RTS.RTS_Batch_Schedule set "
				+ "JobStatus = ?, "
				+ "JobRunDate = ?, "
				+ "JobStartTime = ?, "
				+ "JobRunTime = ? "
				+ "WHERE "
				+ "OfcIssuanceno = ? AND "
				+ "SubStaId = ? AND "
				+ "JobSeqNo = ? ";
		// end defect 10694 
		
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getJobStatus())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getJobRunDate())));
			// defect 10694 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getJobStartTime())));
			// end defect 10694 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getJobRunTime())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getSubStaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaBatchScheduleData.getJobSeqNo())));
			Log.write(
				Log.SQL,
				this,
				"updBatchScheduleJob - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updBatchScheduleJob - SQL - End");
			Log.write(Log.METHOD, this, "updBatchScheduleJob - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updBatchScheduleJob - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
