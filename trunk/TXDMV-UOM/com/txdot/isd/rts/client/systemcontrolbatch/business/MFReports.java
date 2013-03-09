package com.txdot.isd.rts.client.systemcontrolbatch.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.BatchReportManagementCache;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.ReportsCache;
import com.txdot.isd.rts.services.data.ReportsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 *
 * MFReports.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * RHicks		10/03/2002	Keep thread from exiting
 *							Process MF reprots after 7:00am w/o print
 * Ray Rowehl	04/01/2003	Defect 5667
 *							Prevent negative sleep times.
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							Made the call through the Print class
 *							instead of making the call from this class.
 *							Print is isued from the REPORT_DIRECTORY
 *							instead of the DOWNLOAD_DIRECTORY to help
 *							with XP file lockup issues.
 *							delete class variables for FTP
 *							modify MFReports(),
 *							sendReportToPrinter(String)
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		12/09/2004	Changed the processing rules to wake up
 *							every 5 minutes and check for MF reports in 
 *							the DL directory and print only if it is 
 *							before 7AM or after 7PM.  Made code more 
 *							efficient since it will run all day.  Load 
 *							report cache into memory in the super 
 *							instead of everytime.
 *							DO not loop through the Reports Cache if the
 *							directory is empty just go to sleep.
 *							modify run(), processMFReports(boolean),
 *							MFReports()
 *							add getFiles(), copyFile(File), 
 *							coReportNames
 *							deprecate copyFile(String), 
 *							getLogTimeDate(), calculateTimeUntilPMRun(), 
 *							deleteFile(String), fileExists(String)
 *							defect 7702 Ver 5.2.2
 * Jeff S.		12/14/2004	Do not save iterations for the MF Auxilary
 *							reports.
 *							add MF_AUX
 *							modify copyFile(File)
 *							defect 7702 Ver 5.2.2
 * Jeff S.		02/01/2005	We where adding the REPORT_DIRECTORY to the
 *							directory before we sent the report to the
 *							printer.  Now this process is done before 
 *							we call sendReportToPrinter(String) so there
 *							is no need to add it anymore.
 *							modify sendReportToPrinter(String)
 *							defect 7702 Ver 5.2.2
 * Jeff S.		03/03/2005	Get code to standard.
 * 							delete deprecated calculateTimeUntilPMRun(),
 * 								copyFile(String fileName),
 * 								deleteFile(String fileName),
 * 								fileExists(String fileName),
 * 								getLogTimeDate()
 *							defect 7897 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3    
 * Jeff S.		10/07/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3 
 * Ray Rowehl	10/13/2005	Add a keep running flag
 * 							Set sleep time to 30 seconds
 * 							add sbKeepRunning  
 * 							modify SLEEP_TIME
 * 							add isKeepRunning(), setKeepRunning()
 * 							modify run()
 * 							defect 8405 Ver 5.2.3    
 * K Harrell	06/19/2009	UtilityMethods.saveReport() now static. 
 * 							modify copyFile()
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	01/18/2011	modify to handle AutoPrint
 * 							modify processMFReports() 
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Main Frame Reports Processing Class.
 *
 * @version	6.7.0 			01/18/2011 
 * @author	RHicks
 * <br>Creation Date:		10/03/2002
 */

public class MFReports implements Runnable
{
	// defect 8405 
	private static boolean sbKeepRunning = true;
	// end defect 8405

	// defect 7702
	// Added to hold current MFreport filenames from cache
	private static Hashtable chtReportNames = new Hashtable();
	// Mainframe auxillary report filename
	private static final String MF_AUX = "MFAX";
	// end defect 7702

	private static final String DOWNLOAD_DIRECTORY =
		SystemProperty.getRTSAppDirectory() + "../DL/";
	private static final String REPORT_DIRECTORY =
		SystemProperty.getReportsDirectory();

	final public static int AM_THRESHOLD_HOUR = 7;
	final public static int PM_THRESHOLD_HOUR = 19;
	final public static int AM_THRESHOLD_MINUTE = 0;
	final public static int PM_THRESHOLD_MINUTE = 0;

	// defect 7702
	// Changed from 15 to 5
	// 5 minutes, in milliseconds
	//final public static long SLEEP_TIME = 15 * 60 * 1000;
	// defect 8405
	//final public static long SLEEP_TIME = 5 * 60 * 1000;
	final public static long SLEEP_TIME = 30 * 1000;
	// end defect 8405
	// end defect 7702
	// When the message is wrote to the log the XXX will be replaced
	// with a file name.
	private static final String DELETING_FILE =
		"Deleting file <XXX> b/c doesn't match report cache.";
	private static final String PROCESSED_FILE =
		"MF Report <XXX> processed. Print = ";
	private static final String RPT_NOT_PRINTED =
		"Report file XXX could not be printed";
	private static final String XXX = "XXX";

	/**
	 * Constructor for MFReports.
	 * The report filenames for category 2 (MFReports)
	 * are loaded from cahce into a static hashtable so
	 * that cache does not have to be read every 5 minutes
	 * and to make it easier for lookup.
	 */
	public MFReports()
	{
		// defect 7702
		// Load Report Cache into Hashtable instead of going to Cache
		// to get it everytime.  Loading it into a Hash table gives
		// the ability to do a lookup without having to loop through.
		Vector lvFileNames = ReportsCache.getRpts(2);

		for (int i = 0; i < lvFileNames.size(); i++)
		{
			String lsFileName =
				((ReportsData) lvFileNames.elementAt(i))
					.getRptFileName();
			chtReportNames.put(lsFileName, lsFileName);
			lsFileName = null;
		}

		lvFileNames = null;
		// end defect 7702
	}

	/**
	 * Method Copies the source file that is passed as
	 * a prameter to the destination and uses 
	 * UtilityMethods.saveReport() to create a space for
	 * the new MF report.  This was used since iterations
	 * of MF reports are now saved.
	 *
	 * @param aaSourceFile File
	 * @return String
	 */
	private String copyFile(File aaSourceFile)
	{
		// defect 7702
		// Changed method to accept file instead of string
		// since the file object was already avaliable also
		// instead of just copying the file from DL to RPT
		// we now save iterations of the MF reports
		File laSource_file = aaSourceFile;
		File laDestination_file = null;
		FileInputStream lpfsSource = null;
		FileOutputStream lpfsDestination = null;

		// Routine to remove the extention from the filename
		String lsSaveFilename = laSource_file.getName();
		int liDot = lsSaveFilename.indexOf(CommonConstant.STR_PERIOD);
		//File does not have any extension, discard it.
		if (liDot != -1)
		{
			lsSaveFilename = lsSaveFilename.substring(0, liDot);
		}

		byte[] laBuffer;
		int liBytes_read;
		boolean lbResult = false;

		try
		{
			// Used to clean up the old reports that did not have 
			// iterations
			// This can be removed once it has ran for a while
			laDestination_file =
				new File(REPORT_DIRECTORY + aaSourceFile.getName());
			deleteFile(laDestination_file);

			// Do not save iterations for MF AUX reports
			if (!aaSourceFile.getName().startsWith(MF_AUX))
			{
				// This is just used to return the filename to be used
				// and so that the prior reports will be renamed to make
				// room the new report - an empty string is sent to 
				// create an empty report b/c it will just be deleted

				// defect 10023 
				lsSaveFilename =
					UtilityMethods.saveReport(
						CommonConstant.STR_SPACE_EMPTY,
						lsSaveFilename,
						ReportConstant.RPT_7_COPIES);
				//7);
				// end defect 10023

				// Create file object on the new destination file and 
				// delete the empty file that was created to make room 
				// for the new report
				laDestination_file = new File(lsSaveFilename);
				deleteFile(laDestination_file);
			}

			// Just as before 7702 copy the source to destination using
			// The new destination file
			lpfsSource = new FileInputStream(laSource_file);
			lpfsDestination = new FileOutputStream(laDestination_file);
			int liBytes = (int) laSource_file.length();
			laBuffer = new byte[liBytes];
			while (true)
			{
				liBytes_read = lpfsSource.read(laBuffer);
				if (liBytes_read == -1)
				{
					break;
				}
				lpfsDestination.write(laBuffer, 0, liBytes_read);
			}
			lbResult = true;
		}
		catch (Exception aeEx)
		{
			lbResult = false;
		}
		finally
		{
			if (lpfsSource != null)
			{
				try
				{
					lpfsSource.close();
				}
				catch (IOException aeEx)
				{
					// Nothing to be done
				}
			}
			if (lpfsDestination != null)
			{
				try
				{
					lpfsDestination.close();
				}
				catch (IOException aeEx)
				{
					// Nothing to be done
				}
			}

			// If the result was sucess then return new filename
			if (lbResult)
			{
				return lsSaveFilename;
			}
			else
			{
				return null;
			}
		}
		// end defect 7702
	}

	/**
	 * Deletes file if it exists and is a file.
	 *
	 * @param aaFileName File
	 */
	private void deleteFile(File aaFileName)
	{
		if (aaFileName.exists() && aaFileName.isFile())
		{
			aaFileName.delete();
		}
	}

	/**
	 * Returns the long time for the current time.
	 *
	 * @return long
	 */
	static private long getCurrentTime()
	{
		//Calendar laCalendar = Calendar.getInstance();
		long result = Calendar.getInstance().getTime().getTime();
		return result;
	}

	/**
	 * This method gets the files that meet all of
	 * criteria to be moved.  The criteria is :
	 *	1. must be file and not a folder
	 *	2. Must be able to read, write, and rename file.
	 *		by checking these things we can tell if the file
	 *		is ready to be coppied.  If any of the checks
	 *		return false then the file is still being 
	 *		downloaded.
	 *	3. The filename must be in the reports cache table.
	 *		When a report fails this check it is deleted
	 *		since it should not be in the directory.
	 *
	 *  @return Vector
	 */
	private Vector getFiles()
	{
		// defect 7702
		// This was used instead of the method fileExists()
		File laSource = new File(DOWNLOAD_DIRECTORY);

		// if the DL directory does not exist create it
		if (!laSource.exists())
		{
			laSource.mkdirs();
		}

		File[] laFileList = laSource.listFiles();
		laSource = null;

		Vector lvReturnFiles = new Vector();

		for (int i = 0; i < laFileList.length; i++)
		{
			// Added canWrite and renameTo b/c after some testing 
			// canRead gave false positives
			if (laFileList[i].exists()
				&& laFileList[i].isFile()
				&& laFileList[i].canRead()
				&& laFileList[i].canWrite()
				&& laFileList[i].renameTo(laFileList[i]))
			{
				if (chtReportNames
					.containsKey(laFileList[i].getName()))
				{
					lvReturnFiles.addElement(laFileList[i]);
				}
				else
				{
					// Delete any files that are not a match to our
					// pre-determined report names
					BatchLog.write(
						DELETING_FILE.replaceFirst(
							XXX,
							laFileList[i].getName()));
					laFileList[i].delete();
				}
			}
		}
		return lvReturnFiles;
		// end defect 7702
	}

	/**
	 * Returns the long time for the hour and minute
	 * that is passed.
	 *
	 * @param aiHour int
	 * @param aiMinute int
	 * @return long
	 */
	private static long getThresholdTime(int aiHour, int aiMinute)
	{
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR_OF_DAY, aiHour);
		time.set(Calendar.MINUTE, aiMinute);
		time.set(Calendar.SECOND, 0);
		long result = time.getTime().getTime();
		time = null;
		return result;
	}

	/**
	 * Returns the boolean indicating if MFReports should keep running.
	 * 
	 * @return boolean
	 */
	public static boolean isKeepRunning()
	{
		return sbKeepRunning;
	}

	/**
	 * This is the main method that is used to test
	 * MFReports in a standalone environment.
	 *
	 * @param args String[]
	 */
	public static void main(String[] aarrAgs)
	{
		try
		{
			// Get the list of Main Frame report files 
			// from cache
			CacheManager.loadCache();
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
		MFReports laMfr = new MFReports();
		Thread laThread = new Thread(laMfr);
		laThread.start();
	}

	/**
	 * This is the method that processes the MF report.
	 * If true is passed the report will be printed.
	 * If false is passed the report will not be printed
	 * but just moved from the DL directory to the RPT dir.
	 *
	 * @param abPrint boolean
	 */
	public void processMFReports(boolean abPrint)
	{

		// defect 7702
		// Loop through the files that qualify to be moved
		// instead of looping through the Reports Cache then
		// going to the filing system to check if the file exists
		// everytime.
		//
		// Get all files in the DL dir that
		// are to be copied and possibly printed
		Vector lvDLFileNames = getFiles();

		// Loop through the file list
		for (int i = 0; i < lvDLFileNames.size(); i++)
		{
			File laFile = (File) lvDLFileNames.elementAt(i);
			String lsDestination = copyFile(laFile);
			
			// defect 10701 
			boolean lbAutoPrint = true;
			try
			{
				lbAutoPrint = BatchReportManagementCache.isAutoPrint(
					laFile.getName());
			}
			catch (RTSException aeRTSEx)
			{

			}
			// end defect 10701  
			if (lsDestination != null)
			{
				// defect 10701 
				if (abPrint && lbAutoPrint)
				{
					// end defect 10701 
					
					sendReportToPrinter(lsDestination);
				}
				deleteFile(laFile);
				BatchLog.write(
					PROCESSED_FILE.replaceAll(XXX, lsDestination)
						+ abPrint);
			}
			laFile = null;
		}
		// end defect 7702
	}

	/**
	 * This is the main method for MFReports.
	 * This class will run all day long and wake up
	 * every 5 minutes and either move and print the MF
	 * report or just move the MF report.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		// defect 7702
		// process reports all the time and not just after hours
		// defect 8405
		// use keep running to check if exit is needed.
		while (isKeepRunning())
		{
			// end defect 8405
			long loCurrentTime = getCurrentTime();

			// If Current time is less than AM time or greater than PM
			// time then print otherwise just move the MF report to the 
			// RPT directory
			// ie. less than 7:00 AM or greater than 7:00 PM

			// Check if we need to print or not
			boolean lbPrint =
				loCurrentTime
					< getThresholdTime(
						AM_THRESHOLD_HOUR,
						AM_THRESHOLD_MINUTE)
					|| loCurrentTime
						> getThresholdTime(
							PM_THRESHOLD_HOUR,
							PM_THRESHOLD_MINUTE);

			// Pass if we need to print or not
			processMFReports(lbPrint);

			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException aeEx)
			{
				// defect 8405
				// empty block
				// just ignore interuption
				// end defect 8405
			}
		}
		// end defect 7702
	}

	/**
	 * Send the MF report to the printer.
	 *
	 * @param asFileName String
	 */
	private void sendReportToPrinter(String asFileName)
	{
		try
		{
			Print laPrint = new Print();
			// Passing local SubstaId and local WkId so that Print will
			// know that batch is the calling class and throw the exception
			// back to the calling class so that batch will not be stopped
			int liSubStaId = SystemProperty.getSubStationId();
			int liWkId = SystemProperty.getWorkStationId();

			// Using the report directory to issue the print
			// from instead of the DOWNLOAD_RIRECTORY  this should
			// help with file lockup issues that XP is having
			// defect 7702
			// Adding the REPORT_DIRECTORY to the filename is now
			// done in copyFile(File) and is not needed here anymore
			//lPrint.sendToPrinter(
			//	REPORT_DIRECTORY + asFileName, 
			//	null, 
			//	liSubStaId, 
			//	liWkId);
			laPrint.sendToPrinter(asFileName, null, liSubStaId, liWkId);
			// end defect 7702
		}
		catch (Exception aeEx)
		{
			// Based this off of how BatchProcessor handles Exceptions
			BatchLog.error(RPT_NOT_PRINTED.replaceAll(XXX, asFileName));
		}
	}

	/**
	 * Set the keep running boolean
	 * 
	 * @param abKeepRunning boolean
	 */
	public static void setKeepRunning(boolean abKeepRunning)
	{
		sbKeepRunning = abKeepRunning;
	}

}