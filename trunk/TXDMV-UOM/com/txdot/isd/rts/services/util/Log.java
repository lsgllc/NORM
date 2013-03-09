package com.txdot.isd.rts.services.util;

import java.io.*;
import java.util.*;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * Log.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/20/2002	Close the log after write.
 *							defect 4635
 * Ray Rowehl	10/07/2002	Remove 25 as a valid TraceLevel since it
 *							is a duplication of 20.
 *							defect 4793
 * S Govindappa 01/29/2003	Removed closing of PrintWriter in
 *                          write(...) to log all the information.
 *							Make filesize 4096.
 *							defect 5320. 
 * Ray Rowehl	01/31/2003	Work on archiving logs.
 *							Make filesize 1024.
 *							defect 5112.
 * S Govindappa	02/03/2003	work on archiving logs.
 * Ray Rowehl				defect 5112				
 * K Harrell	10/03/2003	Make log formats consistent between SendCache,
 * 							Batch,RTSApp,Refresh; Use RTSDate format for 
 * 							date
 *							removed constant TIMESTAMP_SIZE 
 *							modified all methods for formatting (remove spaces)
 *							modified write() for log format
 * 							defect 6626
 * Ray Rowehl	11/23/2004	Add a method to reconnect to the log
 *							file if it gets renamed on another jvm.
 *							Set a constant to control the number of
 *							archive logs created.  Set it to 800.
 *							Clean up JavaDocs and source.
 *							Renamed fields for hungarian notation.
 *							add MAXARCHIVES, MESSAGE_TXT_START_POS, 
 *							PURGEDAYS
 *							add verifyFilePointer()
 *							delete import java.text.SimpleDateFormat
 *							modify getNewFileNumber(), removeOldLogs(),
 *							write()
 *							defect 7558 Ver 5.2.2
 * Ray Rowehl	12/14/2004	Revamp how whitespace is added after the
 *							timestamp.  If there is a class provided
 *							and it is not of start stop type, make sure
 *							there the text does not start before 95.
 *							Otherwise, only indent 1 space before text.
 *							delete CLASS_NAME_SIZE
 *							modify WHITE_SPACE
 *							modify write()
 *							defect 7558 Ver 5.2.2
 * Ray Rowehl	12/20/2004	Add back the size before println().  Sounds
 *							like this helps AIX determine how to write
 *							the message to the log.
 *							modify write()
 *							defect 7558 Ver 5.2.2
 * K Harrell	11/03/2005	Java 1.4 cleanup
 * 							Ver 5.2.3 
 * Ray Rowehl	06/14/2006	Reflow a few pieces of code to avoid 
 * 							Null Pointers under Java 1.4.
 * 							Removed references to trace level 25.
 * 							Rename some fields since they are not 
 * 							constants.
 * 							Removed old defect markers.
 * 							Do some RTS 5.2.3 clean up in extracting 
 * 							constants out of the methods.
 * 							format source.
 * 							add slMaxFileSize, ssLogFileName, 
 * 								ssLogFileExt
 * 							delete MAX_FILE_SIZE, LOG_FILE_NAME, 
 * 								LOG_FILE_EXT
 * 							modify static initialize block,
 * 								verifyFilePointer()
 * 							defect 8822 Ver 5.2.3
 * Ray Rowehl	06/15/2006	Need to close the log between writes
 * 							on server side.  Multiple jvms share
 * 							the log file. 
 * 							add closeLog(), openLog()
 * 							modify static initialize block,
 * 								verifyFilePointer(), write()
 * 							defect 8822 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The log class contains the static method write() to write any
 * application messages to the log file defined in the RTS Config file.
 *
 * <p>There are various levels of logging.  The message has
 * a level defined with it.  Depending on the level set in the
 * cfg file, the message is written.
 * If the level is 0, do not write the message.
 * 
 * <p>Log files are limited in size.  Once they grow beyond the size 
 * as defined by MAX_FILE_SIZE, the log file is achived and a
 * new log file is started.
 *
 * <p>The application can inspect and set the Trace Level through the
 * public setters and getters for TraceLevel.
 * There is an edit to verify the number provided in the setter.
 * It is preferable that the cfg parameter is used to affect the
 * trace level.
 * 
 * @version	5.2.3			06/15/2006
 * @author	Nancy Ting
 * <br>Creation Date:		08/29/2001 10:10:34
 * 
 */

public class Log
{
	// defect 8822
	// RTS 5.2.3 constant cleanup
	private static final int IC_ERR_MSG_NO_INVALID_VALUE = 740;

	private static final String S1_0 = "0";
	private static final String S1_CURRENT_DIRECTORY = ".";
	private static final String S1_SPACE_DASH_SPACE =
		CommonConstant.STR_SPACE_ONE
			+ CommonConstant.STR_DASH
			+ CommonConstant.STR_SPACE_ONE;

	private static final String SP_MSG_GOT_NPR_SWITCHING_LOG_FILES =
		"Got an NullPointerException trying to switch to the current log file";
	private static final String SP_MSG_IOEXP_OPENING_LOG_FILE =
		"IOException while opening log file";
	private static final String SP_MSG_GOT_IOEXCP_SWITCHING_LOG_FILES =
		"Got an IOException trying to switch to the current log file";
	private static final String SP_MSG_LOG_DELETE_PROBLEM =
		"Log Delete had a problem";
	private static final String SP_MSG_SWITCHING_LOG_FILES =
		"Switching log files";
	private static final String SP_TRACE_LEVEL_COLON = "Trace level: ";
	// end defect 8822

	/*Log Types*/
	/**
	 * Log Type - SQL Exception Message
	 */
	public static int SQL_EXCP = 1;
	/**
	 * Log Type - SQL Statement Message
	 */
	public static int SQL = 2;
	/**
	 * Log Type - Method Entry/Exit Message
	 */
	public static int METHOD = 3;
	/**
	 * Log Type - Debug Message
	 */
	public static int DEBUG = 4;
	/**
	 * Log Type - Application Start/End Message
	 */
	public static int START_END = 5;
	/**
	 * Log Type - Application
	 */
	public static int APPLICATION = 6;

	/*Trace Level*/
	/**
	 * Trace Level 0 - No Trace
	 */
	public static final int TRACE_LEVEL_0 = 0;
	/**
	 * Trace Level 5
	 */
	public static final int TRACE_LEVEL_5 = 5;
	/**
	 * Trace Level 10
	 */
	public static final int TRACE_LEVEL_10 = 10;
	/**
	 * Trace Level 15
	 */
	public static final int TRACE_LEVEL_15 = 15;
	/**
	 * Trace Level 20
	 */
	public static final int TRACE_LEVEL_20 = 20;

	//size of each record in the log file     
	private static final int RECORD_SIZE = 100;

	/**
	 * Number of spaces to print before text if there is no class.
	 */
	private static final int WHITE_SPACE = 1;

	private static String ARCH_LOG_FILE_NAME = "LOG";

	/**
	 * Number of days of logs to keep.
	 * Any log older than this time frame is deleted.
	 */
	private static int PURGEDAYS = 7;

	/**
	 * Max number of generations of logs to 
	 * create in one day.
	 */
	private static int MAXARCHIVES = 800;

	/**
	 * Message start position.
	 */
	public static final int MESSAGE_TXT_START_POS = 95;

	// defect 8822
	// these are not constants
	private static final String ssLogFileName;
	private static final String ssLogFileExt;
	private static final long slMaxFileSize;
	// end defect 8822

	//The system level trace level which is set at start up
	private static int siTraceLevel = 5;

	//present size of the log file in bytes
	private static long slFileSize;
	private static File saFileLog;
	private static FileOutputStream saFileOutputStream;
	private static PrintWriter saPrintWriter;
	private static int[][] sarrTrace;

	// end defect 7558

	/**
	 * Initialization block.
	 */
	static {
		// defect 8822
		// Move this intialization before SystemProperty calls to 
		// avoid null pointer.
		sarrTrace = new int[][] { { TRACE_LEVEL_5, SQL_EXCP }, {
				TRACE_LEVEL_10, SQL_EXCP, APPLICATION }, {
				TRACE_LEVEL_15, SQL_EXCP, APPLICATION, SQL }, {
				TRACE_LEVEL_20,
					SQL_EXCP,
					APPLICATION,
					SQL,
					METHOD,
					DEBUG }
		};
		// end defect 8822

		slMaxFileSize = SystemProperty.getMaxLogFileSize() * 1024;
		ssLogFileName = SystemProperty.getLogFileName();
		ssLogFileExt = SystemProperty.getLogFileExt();
		siTraceLevel = SystemProperty.getLogTraceLevel();

		// defect 8822
		// Go open the log.
		openLog();
		//saFileLog = new File(ssLogFileName + ssLogFileExt);
		//slFileSize = saFileLog.length();
		//try
		//{
		//	saFileOutputStream =
		//		new FileOutputStream(saFileLog.getName(), true);
		//	saPrintWriter = new PrintWriter(saFileOutputStream);
		//}
		//catch (IOException aeIOEx)
		//{
		//	System.err.println(SP_MSG_IOEXP_OPENING_LOG_FILE);
		//	aeIOEx.printStackTrace(System.err);
		//}
		// end defect 8822
	}

	/**
	 * Log constructor.
	 */
	public Log()
	{
		super();
	}

	/**
	 * Close the Log.
	 */
	private static void closeLog()
	{
		try
		{
			saFileOutputStream.close();
			saFileLog = null;
		}
		catch (IOException aeIOE)
		{
			aeIOE.printStackTrace();
		}
	}

	/**
	 * This method checks to see if there is any old logfiles for Today 
	 * and returns the next number.
	 *
	 * <p>Returns:
	 * <ul>
	 * <li>1      			  - if there are no old logfiles for Today.
	 * <li>2 - MAXARCHIVES-1 - if there are previous files
	 * <li>MAXARCHIVES		  - if there are already MAXARCHIVES files.
	 * <eul>
	 *
	 * @param asLogName String
	 * @return String 
	 */
	private static String getNewFileNumber(String asLogName)
	{
		// get the directory of all old log files matching the passed 
		// logfile name

		File laDir = new File(S1_CURRENT_DIRECTORY);
		File[] larrFiles = laDir.listFiles();

		//Get all files which have supplied name as file name
		Vector lvFile = new Vector();

		for (int i = 0; i < larrFiles.length; i++)
		{
			String lsFileName = larrFiles[i].getName();
			String lsFileNameLower = asLogName.toLowerCase();
			if (lsFileName.toLowerCase().startsWith(lsFileNameLower))
			{
				lvFile.add(larrFiles[i]);
			}
		}

		// Determine number of files
		int liReturnNumber = lvFile.size();

		// if this the first time for the day, run the cleaning routine
		if (liReturnNumber == 0)
		{
			try
			{
				removeOldLogs();
			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					SP_MSG_LOG_DELETE_PROBLEM + asLogName);
			}
		}

		// use a constant instead of hard coding the number.
		// increment return number.  
		// set it to MAXARCHIVES if at MAXARCHIVES.
		if (liReturnNumber < MAXARCHIVES)
		{
			liReturnNumber = liReturnNumber + 1;
		}
		else
		{
			liReturnNumber = MAXARCHIVES;
		}

		// setup the return string
		String lsReturnNumber = String.valueOf(liReturnNumber);
		int liLoopControl = lsReturnNumber.length();
		for (int j = 0; j < (3 - liLoopControl); j++)
		{
			lsReturnNumber = S1_0 + lsReturnNumber;
		}

		// return the return string.
		return lsReturnNumber;
	}

	/**
	 * Get the trace level
	 * 
	 * @return int trace level
	 */
	public static int getTraceLevel()
	{
		return siTraceLevel;
	}

	/**
	 * Inspect the Log Message type and trace level to determine
	 * whether logging is required
	 *
	 * @param aiMsgType int  
	 * @return boolean
	 */
	private static boolean logRequired(int aiMsgType)
	{
		boolean lbLogRequired = false;

		for (int i = 0; i < sarrTrace.length; i++)
		{
			if (siTraceLevel == sarrTrace[i][0])
			{
				for (int j = 1; j < sarrTrace[i].length; j++)
				{
					if (aiMsgType == sarrTrace[i][j])
					{
						lbLogRequired = true;
						break;
					}
				}
			}
			if (lbLogRequired)
			{
				break;
			}
		}

		return lbLogRequired;
	}

	/**
	 * This method opens the log.
	 */
	private static void openLog()
	{
		saFileLog = new File(ssLogFileName + ssLogFileExt);
		slFileSize = saFileLog.length();
		try
		{
			saFileOutputStream =
				new FileOutputStream(saFileLog.getName(), true);
			saPrintWriter = new PrintWriter(saFileOutputStream);
		}
		catch (IOException aeIOEx)
		{
			System.err.println(SP_MSG_IOEXP_OPENING_LOG_FILE);
			aeIOEx.printStackTrace(System.err);
		}
	}

	/**
	 * This method delete log files that are older than a certain date.
	 * 
	 * @throws RTSException
	 */
	private static void removeOldLogs() throws RTSException
	{
		// get the list of old log files
		// format "RT" for rtsapp.log, then *.* for all files matching
		// "LOG"
		File laDir = new File(S1_CURRENT_DIRECTORY);
		File[] larrFiles = laDir.listFiles();

		// setup the purge date
		int liPurgeAMDate = (new RTSDate()).getAMDate() - PURGEDAYS;

		RTSDate laPurgeDate =
			new RTSDate(RTSDate.AMDATE, liPurgeAMDate);

		// begin purge of old files
		// do not delete if there is only the one file
		if (larrFiles.length > 1)
		{
			// loop through all the files
			for (int i = 0; i < larrFiles.length; i++)
			{
				//Get all files which have supplied name as file name
				String lsFileName =
					larrFiles[i].getName().toLowerCase();
				RTSDate laLastMod =
					new RTSDate(larrFiles[i].lastModified());
				if (laLastMod.compareTo(laPurgeDate) < 0
					&& lsFileName.startsWith(
						ARCH_LOG_FILE_NAME.toLowerCase()))
				{
					larrFiles[i].delete();
				}
			}
		}
	}

	/**
	 * Set the trace level of Log
	 * 
	 * @param aiTraceLevel Trace Level
	 * @throws RTSException
	 */
	public static void setTraceLevel(int aiTraceLevel)
		throws RTSException
	{
		if ((aiTraceLevel != TRACE_LEVEL_0)
			&& (aiTraceLevel != TRACE_LEVEL_5)
			&& (aiTraceLevel != TRACE_LEVEL_10)
			&& (aiTraceLevel != TRACE_LEVEL_15)
			&& (aiTraceLevel != TRACE_LEVEL_20))
		{
			throw (
				new RTSException(
					IC_ERR_MSG_NO_INVALID_VALUE,
					new String[] {
						 SP_TRACE_LEVEL_COLON + aiTraceLevel }));
		}
		siTraceLevel = aiTraceLevel;
	}

	/**
	 * This method verifies that we are pointing
	 * to the correct file.
	 * If we are pointing to the wrong file name,
	 * switch to the new file.
	 *
	 * <p>It appears possible that one jvm could
	 * rename the file on AIX without other
	 * jvm's realizing it.
	 */
	private static void verifyFilePointer()
	{
		try
		{
			// defect 8822
			// open the file if it is not already open
			if (saFileLog == null)
			{
				openLog();
			}

			// Check for null before comparision.
			// a null would indicate there is no file.
			if (saFileLog == null
				|| !saFileLog.getName().equalsIgnoreCase(
					ssLogFileName + ssLogFileExt))
			{
				// end defect 8822
				System.out.println(SP_MSG_SWITCHING_LOG_FILES);
				// defect 8822
				// ensure that the stream is not null before closing
				if (saFileOutputStream != null)
				{
					saFileOutputStream.close();
				}
				// end defect 8822
				saFileLog = new File(ssLogFileName + ssLogFileExt);
				slFileSize = saFileLog.length();
				saFileOutputStream =
					new FileOutputStream(saFileLog.getName(), true);
				saPrintWriter = new PrintWriter(saFileOutputStream);
			}
		}
		catch (IOException aeIOE)
		{
			// defect 8822
			// there is not much we can do about it except log it to 
			// std err.
			System.err.println(SP_MSG_GOT_IOEXCP_SWITCHING_LOG_FILES);
			aeIOE.printStackTrace();
		}
		catch (NullPointerException aeNPR)
		{
			// catch and log nullpointers.
			System.err.println(SP_MSG_GOT_NPR_SWITCHING_LOG_FILES);
			aeNPR.printStackTrace();
		}
		// end defect 8822
	}

	/**
	 * Writes the message to the log.
	 *
	 * <p>Major steps:
	 * <ol>
	 * <li>Check to see if the message level requires a write.
	 * <li>Verify that we are pointing to the right file.
	 * <li>Check the file size.  Archive if filesize is big enough.
	 * <li>Format and write the message.
	 * <eol>
	 * 
	 * @param aiMsgType int
	 * @param aaObj Object
	 * @param asDesc String 
	 */
	public synchronized static void write(
		int aiMsgType,
		Object aaObj,
		String asDesc)
	{
		boolean lbLogRequired = false;

		// 1. Determine whether logging is required 
		if (siTraceLevel == TRACE_LEVEL_0)
		{
			return;
		}

		lbLogRequired = logRequired(aiMsgType);
		if ((!lbLogRequired) && (aiMsgType != START_END))
		{
			return;
		}

		// Make sure we are using the current rtsapp.log.
		// It appears that it is possible for the log to be renamed
		// by a different jvm without the local jvm knowing about it.
		// This should be done before checking size.
		verifyFilePointer();

		// If log file size has grown beyond the Max. limit, we need to 
		// rename the old log file by appending the current time stamp and create 
		// a new log file 

		slFileSize = saFileLog.length();
		if (slFileSize + RECORD_SIZE > slMaxFileSize)
		{
			saPrintWriter.close();
			String lsRenameFileName =
				ARCH_LOG_FILE_NAME + (new RTSDate()).getAMDate();

			// check to see what the next file number should be
			String lsFileExt = getNewFileNumber(lsRenameFileName);

			// set up the backup file name
			String lsOldLogFile =
				lsRenameFileName + S1_CURRENT_DIRECTORY + lsFileExt;
			try
			{
				saFileLog.renameTo(new File(lsOldLogFile));
				saFileLog = new File(ssLogFileName + ssLogFileExt);
				slFileSize = saFileLog.length();
				saFileOutputStream =
					new FileOutputStream(saFileLog.getName(), true);
				saPrintWriter = new PrintWriter(saFileOutputStream);
			}
			catch (IOException aeIOEx)
			{
				aeIOEx.printStackTrace();
			}
		}

		// Use consistent format for logging 
		RTSDate laDate = RTSDate.getCurrentDate();

		StringBuffer lsMsgStr = new StringBuffer();
		lsMsgStr.append(
			laDate
				+ S1_SPACE_DASH_SPACE
				+ laDate.getClockTime()
				+ S1_SPACE_DASH_SPACE);

		if (aiMsgType != START_END)
		{
			if (aaObj != null)
			{
				if (aaObj instanceof Class)
				{
					lsMsgStr.append(((Class) aaObj).getName());
				}
				else
				{
					lsMsgStr.append(aaObj.getClass().getName());
				}

				// use addPadding to add blanks to log. 
				// This ensures that the message always starts in the same place in the log.
				int liPadding =
					MESSAGE_TXT_START_POS - lsMsgStr.length();

				// if there is was a problem with the class, add 3 blanks.
				if (liPadding < 1)
				{
					liPadding = 3;
				}

				lsMsgStr.append(
					UtilityMethods.addPadding(
						CommonConstant.STR_SPACE_ONE,
						liPadding,
						CommonConstant.STR_SPACE_ONE));
			}
			else
			{
				lsMsgStr.append(
					UtilityMethods.addPadding(
						CommonConstant.STR_SPACE_ONE,
						WHITE_SPACE,
						CommonConstant.STR_SPACE_ONE));
			}

		}
		else
		{
			lsMsgStr.append(
				UtilityMethods.addPadding(
					CommonConstant.STR_SPACE_ONE,
					WHITE_SPACE,
					CommonConstant.STR_SPACE_ONE));
		}

		if (asDesc != null)
		{
			lsMsgStr.append(asDesc);
		}

		// this apparently helps AIX determine how to do the println.
		lsMsgStr.setLength(lsMsgStr.toString().length());
		saPrintWriter.println(lsMsgStr.toString());
		saPrintWriter.flush();

		// get the size from the file!  not calculation.
		// there can be multiple jvms writing to the log.
		slFileSize = saFileLog.length();

		// defect 8822
		// close the log on the server since multiple jvms are
		// accessing it.
		if (Comm.isServer())
		{
			closeLog();
		}
		// end defect 8822
	}
}
