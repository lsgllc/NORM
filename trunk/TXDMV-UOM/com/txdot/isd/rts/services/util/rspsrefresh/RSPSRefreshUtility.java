package com.txdot.isd.rts.services.util.rspsrefresh;

import java.io.*;
import java.util.*;

/**
 * RSPSRefreshUtility.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * Jeff S.		07/21/2004	Created Class
 *							defect 7135 Ver 5.2.1
 * Jeff S.		08/24/2004	B/C of class path issues I have moved
 *							this class inside of the Phase1 jar
 *							defect 7135 Ver 5.2.1
 * Jeff S.		09/13/2004	Made class runnable so RTSMain could
 *							call class as a thread.  Cleaned
 *							up logging.  Added folder exclusions.
 *							defect 7135 Ver 5.2.1
 * Jeff S.		09/27/2004	Add .VBS to valid extensions.
 *							modify isValidFile(String, String)
 *							Ver 5.2.1
 * Jeff S.		10/07/2004	Final Cleanup on logging.  Removed dup
 *							log writes.
 *							modify isValidFolder(String)
 *							modify getFilesFromSource()
 *							Ver 5.2.1
 * K Harrell	12/28/2004	Typo corrections.
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							defect 7806  Ver 5.2.2 
 * Jeff S.		03/04/2005	Added .csv to the allowed extentions so
 * 							that RSPS can recieve the del file updates
 * 							for the DB.			
 * 							modify isValidFile()
 * 							defect 8032 Ver 5.2.2 Fix 2
 * Jeff S.		04/26/2005	Allow .msi files fot txdot hot fixes.
 * 							modify isValidFile()
 * 							defect 8175 Ver. 5.2.2 Fix 4
 * Jeff S.		04/04/2005	Class has been re-written and is now 
 * 							deprecated.  All refresh processed have been
 * 							combined into one project and added as an 
 * 							external jar.
 * 							deprecate class
 * 							defect 8014 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * This Class is used to distribute RSPS code, dat files, and 
 * System Updates.
 *
 * There are three phases of the distribution process:
 * 		TYPE_COM_TO_COUNTY:
 *			Moving files from Comm Server to the County machines. 
 *			If a file is removed from the Comm server side then 
 *			the file will also be removed from the county machine.
 *			This phase is started by the RTS refresh process that 
 *			is started every time the application is started.
 * 		TYPE_COUNTY_TO_FLASH:
 *			Moving files from the County machine to the flash 
 *			drive.  This phase is called from within the RTS 
 *			project when an RSPS clerk brings the flash drive to 
 *			the county to be processed.  This phase will also 
 *			clean up any files that are not on the county machine 
 *			but are on the flash drive. Another feature that is 
 *			different in this phase is that a vector of files to
 *			exclude are passed and refresh will not move these 
 *			files.  Along with not moving the files the refresh 
 *			will also clean them from the flash drive. 
 * 		TYPE_FLASH_TO_NOTEBOOK:
 *			Moving files from the FlashDrive to the RSPS Unit. If 
 *			a file is not on the FlashDrive then the file will 
 *			also be removed from the RSPS Unit. This phase is 
 *			started by the C++ program on the RSPS unit which is 
 *			started by the RSPS clerk.
 *
 * Note: All files will be moved using the following criteria.
 * 1) If destination file does not exist
 * 2) If the date modified on the destination file is different 
 *		than that of the source.
 * 3) If the file is not in the notAllowedFiles hash table
 * 4) If the file is not in the sysUpdatesNotNeeded hash table 
 *		(only in TYPE_COUNTY_TO_FLASH)
 * 5) If the file contains a valid file extension.
 * 		Valid Types: .jar, .cfg, .bat, .cmd, .gif, .jpg, .bmp, 
 *			.exe, .txt, .mdb, .dat
 *
 * In order for the first phase to work a network connection is to
 * be made from the County machine to the Comm Server.  A network 
 * Drive is mapped.  The Windows Net Use command is used to map 
 * the network drive. Refer to connect() for more information on 
 * the net use command.  For the second and third phase of 
 * distribution no network drive is needed since the source and 
 * destination are local drives to the system via a flash drive.
 *
 *	Possible Problems:
 *		While testing there was some issues with a file not taking the
 *		the lastmodified date that was sent to it.  This would caused the
 *		file to be refreshed everytime the program is run b/c the date was
 *		different.  I found out that when a file had a value other than 0
 *		for micro seconds on the last modified date java would round up when
 *		it set the lastmodified on the destination.  The only files that had
 *		micro seconds other than 0 where files that I touched on my machine.
 *		I am unsure if it is just a problem with settings on my machine.
 *		I touched the files on another machine and found out that the last
 *		modified date was set correctly.
 * 
 *
 * @version	5.2.3		04/04/2005
 * @author	Jeff Seifert
 * <p>Creation Date: 	07/08/04 15:58:15 
 * @deprecated
 */
 
public class RSPSRefreshUtility implements Runnable
{
	// List of files which should not be copied or removed.
	private Hashtable caNotAllowedFiles = new Hashtable();
	// List of folders which should not be copied or removed.
	private Hashtable caNotAllowedFolders = new Hashtable();
	// List of System Updates that do not need to be moved from
	// the county machine to the flash drive
	private Hashtable caSysUpdatesNotNeeded = new Hashtable();
	
	private static final String REFRESH_LOG				= "refresh.log";
	private static final String PHASE_THREE_LOG_DIR		= "logs\\";
	private static final String RTS_STATIC_PROP_FILE	= "cfg/rtscls.cfg";
	private static final String RSPS_PROP_FILE 			= "cfg/rsps.cfg";

	private static final String RSPS_REFRESH_JAR		= "RSPSRefresh.jar";
	private static final String RSPS_UPDATE_EXE			= "rspsupdate.exe";
	private static final String RSPS_DB					= "rsps.mdb";
	
	private static int ciRefreshType;
	private static final int TYPE_COM_TO_COUNTY 		= 1;
	private static final int TYPE_COUNTY_TO_FLASH 		= 2;
	private static final int TYPE_FLASH_TO_NOTEBOOK 	= 3;
	
	private static String csCommServerName;
	private static final String COMM_SERVER_NAME		= "CommServerName";
	
	private static String csCommDriveLetter;
	private static final String COMM_DRIVE_LETTER		= "CommDriveLetter";
	
	private static String csCommShareName;
	private static final String COMM_SHARE_NAME			= "CommShareName";
	
	private static String csCountyRSPSSourceDirectory;
	private static final String COUNTY_RSPS_SOURCE_DIR	= "CountyRSPSSourceDirectory";
	
	private static String csFlashDriveSourceDirectory;
	private static final String FLASH_DRIVE_SOURCE_DIR	= "FlashDriveSourceDirectory";
	
	private static String csNotebookSourceDirectory;
	private static final String NOTEBOOK_SOURCE_DIR		= "NotebookSourceDirectory";

	private static int ciRSPSRefreshDebug;
	private static final String RSPS_REFRESH_DEBUG		= "RSPSRefreshDebug";

	private static String csSourceDirectory;
	private static String csDestDirectory;

	// flag to test if log file is already open
	private static boolean cbLogFileOpened				= false;
	// flag to test if connection was made
	private static boolean cbNetworkDriveConnected 		= false;
	// Printwriter for writing into file.
	private static PrintWriter caPrintWriter 			= null;
	// FileOutputStream for the log file
	private static FileOutputStream cpfsFileOutputStream	= null;

	// Net Use System Errors
	private static final String NET_USE_ERROR_85 		= "System error 85 has occurred.";
	private static final String NET_USE_ERROR_67 		= "System error 67 has occurred.";
	private static final String NET_USE_PERMISSION 		= "The password is invalid for";
/**
 * RSPSRefreshUtility constructor comment.
 */
public RSPSRefreshUtility()
{
	// Used to exclude files
	// Files that will be removed from list when 
	// Refresh type is not in Phase 3 (TYPE_FLASH_TO_NOTEBOOK)
	caNotAllowedFiles.put(RSPS_REFRESH_JAR.toLowerCase(), RSPS_REFRESH_JAR);
	caNotAllowedFiles.put(RSPS_UPDATE_EXE.toLowerCase(), RSPS_UPDATE_EXE);
	// Files that are always excluded
	caNotAllowedFiles.put(RSPS_DB.toLowerCase(), RSPS_DB);
	caNotAllowedFiles.put("rspsprn.cfg".toLowerCase(), "rspsprn.cfg");
	caNotAllowedFiles.put("refresh.log".toLowerCase(), "refresh.log");
	caNotAllowedFiles.put("RSPSRefresh.bat".toLowerCase(), "RSPSRefresh.bat");
	caNotAllowedFiles.put("control.dat".toLowerCase(), "control.dat");
	caNotAllowedFiles.put("rspsapp.log".toLowerCase(), "rspsapp.log");
	// RSPS folders that are excluded - this is only used 
	// in Phase 3 - TYPE_FLASH_TO_NOTEBOOK
	caNotAllowedFolders.put("rpt".toLowerCase(), "rpt");
	caNotAllowedFolders.put("logs".toLowerCase(), "logs");
	caNotAllowedFolders.put("ico".toLowerCase(), "ico");
	caNotAllowedFolders.put("datfile".toLowerCase(), "datfile");
	caNotAllowedFolders.put("systemupdates".toLowerCase(), "systemupdates");
}
/**
 * The Local Directory listing is compared to the remote directory listing
 * and if there is a file or folder on the local side that is not on the
 * remote side it is deleted.  Created a method to delete directories using
 * a process, since java will not delete a directory is there are sub files
 * or folders inside.
 * 
 * @param lvSourceDirListing java.util.Vector
 * @param lvDestDirListing java.util.Vector
 *
 * @throws Exception
 */
private void cleanUpOldFiles(
	Vector lvSourceDirListing, 
	Vector lvDestDirListing) 
	throws Exception
{
	
	Vector lvDestFileList 		= (Vector) lvDestDirListing.elementAt(0);
	Vector lvSourceFileList		= (Vector) lvSourceDirListing.elementAt(0);
	
	Vector lvDestFolderList 	= (Vector) lvDestDirListing.elementAt(1);
	Vector lvSourceFolderList 	= (Vector) lvSourceDirListing.elementAt(1);

	// Loop through all the files on the Destination and make sure
	// that a file with the same name exists on the Source.  If the file
	// does not exist on the source it is deleted unless it is excluded.
	for (int i = 0; i < lvDestFileList.size(); i++)
	{
		File laLocalFile = (File)lvDestFileList.elementAt(i);
		boolean lbTest = false;
		for (int j = 0; j < lvSourceFileList.size(); j++)
		{
			File laSourceFile = (File)lvSourceFileList.elementAt(j);
			if (laLocalFile.getName().equalsIgnoreCase(laSourceFile.getName()))
			{
				// File was found on the remote side or is excluded
				lbTest = true;
				break;
			}
		}
		if (!lbTest)
		{
			// Only delete if it is allowed.
			if (!caNotAllowedFiles.containsKey(laLocalFile.getName().toLowerCase()))
			{
				// if the file was not found on the remote side then it needs to be removed
				writeLog("Deleting file: <" + laLocalFile.getAbsolutePath() + ">");
				laLocalFile.delete();
			}
			else
			{
				writeLog("No delete - file excluded: <" + laLocalFile.getAbsolutePath() + ">");
			}
		}
	}

	// Loop through all the folders on the Destination and make sure
	// that a folder with the same name exists on the Source.  If the folder
	// does not exist on the source the folder is deleted unless it is excluded.
	for (int i = 0; i < lvDestFolderList.size(); i++)
	{
		File laLocalFolder = (File)lvDestFolderList.elementAt(i);
		boolean lbTest = false;
		for (int j = 0; j < lvSourceFolderList.size(); j++)
		{
			File laSourceFolder = (File)lvSourceFolderList.elementAt(j);
			if (laLocalFolder.getName().equalsIgnoreCase(laSourceFolder.getName()))
			{
				// Folder was found on the remote side or is excluded
				lbTest = true;
				break;
			}
		}
		if (!lbTest)
		{
			// had to create a call to the system to remove the directory
			// since java will not remove a directory that contains sub files
			// or folders.  Only delete if it is allowed.
			if (!caNotAllowedFolders.containsKey(laLocalFolder.getName().toLowerCase()))
			{
				writeLog("Deleting directory: <" + laLocalFolder.getAbsolutePath() + ">");
				deleteDirectory(laLocalFolder.getAbsolutePath());
			}
			else
			{
				writeLog("No delete - folder excluded: <" + laLocalFolder.getAbsolutePath() + ">");
			}
		}
	}
}
/**
 * This method will close the log file 
 * if it has been opened.
 * 
 */
public void closeLogFile()
{
	if (isLogFileOpened())
	{
		try
		{
			caPrintWriter.close();
			caPrintWriter = null;
			cpfsFileOutputStream.close();
			cpfsFileOutputStream	= null;
			setLogFileOpened(false);
		}
		catch (Exception ex)
		{
			// Don't do anything since we have closed log file
		}
	}
}
/**
 * Uses Windows Net Use command to map a network drive to
 * the source of the refresh.  If the connection attemp fails
 * an exception is thrown contailing the result from the net use
 * command.  The Networkdrive Connection status is updated is the
 * connection attempt was sucessfull.
 *
 * NET USE
 * [devicename | *] [\\computername\sharename[\volume] [password | *]]
 *         [/USER:[domainname\]username]
 *         [/USER:[dotted domain name\]username]
 *         [/USER:[username@dotted domain name]
 *         [/SMARTCARD]
 *         [/SAVECRED]
 *         [[/DELETE] | [/PERSISTENT:{YES | NO}]]
 * 
 * NET USE {devicename | *} [password | *] /HOME
 * 
 * NET USE [/PERSISTENT:{YES | NO}]
 *
 * @throws Exception
 * @throws InterruptedException
 */
 
private void connect() throws Exception, InterruptedException
{
	if (getRefreshType() == TYPE_COM_TO_COUNTY)
	{
		Process laProcess = Runtime.getRuntime().exec(
			"cmd.exe /c net use " 
			+ csCommDriveLetter.substring(0,2) 
			+ " \\\\" 
			+ csCommServerName 
			+ "\\" 
			+ csCommShareName);
		laProcess.waitFor();
		String lsOutput = " " + readOutput(laProcess) + " " + readError(laProcess);
		if (lsOutput.indexOf(NET_USE_ERROR_85) > 0 
			|| lsOutput.indexOf(NET_USE_ERROR_67) > 0 
			|| lsOutput.indexOf(NET_USE_PERMISSION) > 0)
		{
			throw new Exception(lsOutput);
		}
		else
		{
			setNetworkDriveConnected(true);
		}
	}
	else
	{
		if (!isFlashDriveConnected())
		{
			throw new Exception("Flash Drive is not connected!");
		}
	}
}
/**
 * Copies src file to dst file.
 * If the dst file does not exist, it is created
 *
 * @param aaSourceFile File
 * @param aaDestinationFile File 
 * @throws IOException
 */
 
void copyFile(File aaSourceFile, File aaDestinationFile) throws IOException
{
	InputStream lpfsInputStream = new FileInputStream(aaSourceFile);
	OutputStream lpfsOutputStream = new FileOutputStream(aaDestinationFile); 
	// Transfer bytes from in to out
	byte[] larrBuffer = new byte[1024];
	int liLen;
	while ((liLen = lpfsInputStream.read(larrBuffer)) > 0)
	{
		lpfsOutputStream.write(larrBuffer, 0, liLen);
	}
	lpfsOutputStream.flush();
	lpfsInputStream.close();
	lpfsOutputStream.close();
}
/**
 * Removes (deletes) a directory.
 * 
 * RMDIR [/S] [/Q] [drive:]path
 * RD [/S] [/Q] [drive:]path
 * 
 *     /S      Removes all directories and files in the specified directory
 *             in addition to the directory itself.  Used to remove a directory
 *             tree.
 * 
 *     /Q      Quiet mode, do not ask if ok to remove a directory tree with /S
 * 
 * @param asDirectory java.lang.String
 * @throws Exception
 * @throws InterruptedException
 */
private void deleteDirectory(String asDirectory) throws Exception, InterruptedException
{
	Process laProcess = Runtime.getRuntime().exec(
		"cmd.exe /c rd \""
		+ asDirectory
		+ "\" /S /Q");
	laProcess.waitFor();
}
/**
 * Uses Windows Net Use command to disconnect a mapped network drive.
 * The command will only be issued if the drive was connected.
 *
 * NET USE
 * [devicename | *] [\\computername\sharename[\volume] [password | *]]
 *         [/USER:[domainname\]username]
 *         [/USER:[dotted domain name\]username]
 *         [/USER:[username@dotted domain name]
 *         [/SMARTCARD]
 *         [/SAVECRED]
 *         [[/DELETE] | [/PERSISTENT:{YES | NO}]]
 * 
 * NET USE {devicename | *} [password | *] /HOME
 * 
 * NET USE [/PERSISTENT:{YES | NO}]
 *
 * @throws Exception
 * @throws InterruptedException
 */
 
private void disconnect() throws Exception, InterruptedException
{
	if (isNetworkDriveConnected())
	{
		Process p = Runtime.getRuntime().exec(
			"cmd.exe /c net use " 
			+ csCommDriveLetter.substring(0,2) 
			+ " /d /YES");
		p.waitFor();
		setNetworkDriveConnected(false);
	}
}
/**
 * This method loops through the Source File list and
 * determines if the file is to be coppied over to the 
 * destination.  The file is only going to be moved if it
 * is a valid file type and the date modified is different
 * than the sources date modified.
 *
 * @param avSourceFileList java.util.Vector
 * @param asSourceDirectory String 
 * @throws Exception
 */
 
private void getBinaryFiles(Vector avSourceFileList, String asSourceDirectory) throws Exception
{
	try
    {
	    String lsLocalDir;
	    String lsLocalFile;
	    
		// loop through the directory of files
	    for (int i = 0; i < avSourceFileList.size(); i++)
	    {
		    
	    	File laSourceFile = (File) avSourceFileList.elementAt(i);
	    	
	  		// get the file name from the directory
	    	int liSourceFileNameLoc = laSourceFile.getAbsolutePath().lastIndexOf("\\") + 1;
	    	String lsSourceFileName = laSourceFile.getAbsolutePath().substring(liSourceFileNameLoc);

			if (asSourceDirectory.equals(csSourceDirectory))
			{
				//
				lsLocalFile = csDestDirectory + "\\"+ lsSourceFileName;
				lsLocalDir  = csDestDirectory;
			}
			else
			{
				//
				lsLocalFile = csDestDirectory + "\\" + asSourceDirectory + "\\" + lsSourceFileName;
				lsLocalDir  = csDestDirectory + "\\" + asSourceDirectory;
			}
			
	    	// check to see if the file should be retrieved
	        if(isValidFile(lsSourceFileName, lsLocalFile) && isDifferent(laSourceFile, lsLocalFile))
	        {
			    File laDestFile = new File(lsLocalFile);
			    
		    	writeLog("Getting file from <" + laSourceFile.getAbsolutePath() + ">");
		    	writeLog("Copying file to <" + laDestFile.getAbsolutePath() + ">");
		    	
	  			//writeLog("Source (" 
		  		//	+ new java.util.Date(laSourceFile.lastModified())  
		  		//	+ ") different than destination (" 
		  		//	+ new java.util.Date(laDestFile.lastModified()) 
		  		//	+ ").");
	  			
		        //Delete destination file so that the source file can be copied to the destination.
	  		 	if(laDestFile.exists() && laDestFile.isFile())
	  			{
	       	 		laDestFile.delete();
	  			}

				copyFile(laSourceFile, laDestFile);
				//writeLog("Setting last modified on destination to " +  new java.util.Date(laSourceFile.lastModified()));
				laDestFile.setLastModified(laSourceFile.lastModified());
				//writeLog("Destination last modified (" +  new java.util.Date(laDestFile.lastModified()) + ")");
	         }
         }
	}
	catch (Exception ex)
    {
    	ex.printStackTrace();
        writeLog("Error in getBinaryFiles(Vector, String) - " + ex.getMessage());
        throw ex;
    }
}
/**
 * Returns the date and clock time (e.g. 07/16/2004 15:35.34)
 * @return java.lang.String
 */
 
private static String getDateClockTime()
{
	Calendar laRightNow = Calendar.getInstance();
	
	int liHour = laRightNow.get(Calendar.HOUR_OF_DAY);
	int liMinute = laRightNow.get(Calendar.MINUTE);
	int liSecond = laRightNow.get(Calendar.SECOND);
	int liMillisec = laRightNow.get(Calendar.MILLISECOND);
	
	String lsTime = "";
	
	if (liHour < 10)
	{
		lsTime += "0" + liHour;
	}
	else
	{
		lsTime += liHour;
	}
	lsTime += ":";
	
	if (liMinute < 10)
	{
		lsTime += "0" + liMinute;
	}
	else
	{
		lsTime += liMinute;
	}
	lsTime += ":";
	
	if (liSecond < 10)
	{
		lsTime += "0" + liSecond;
	}
	else
	{
		lsTime += liSecond;
	}
	if (liMillisec < 10)
	{
		lsTime = lsTime + ".00" + liMillisec;
	}
	else
		{
		if (liMillisec < 100)
		{
			lsTime = lsTime + ".0" + liMillisec;
		}
		else
		{
			lsTime = lsTime + "." + liMillisec;
		}
	}

	String lsMonth = (laRightNow.get(Calendar.MONTH) + 1) + "";
	String lsDay = laRightNow.get(Calendar.DATE) + "";
	String lsYear = laRightNow.get(Calendar.YEAR) + "";

	if (lsMonth.length() < 2)
	{
		lsMonth = "0" + lsMonth;
	}

	if (lsDay.length() < 2)
	{
		lsDay = "0" + lsDay;
	}
		
	String lsDateClockTime =
		(lsMonth
			+ "/"
			+ lsDay
			+ "/"
			+ lsYear
			+ " - "
			+ lsTime + " - ");
			
	return lsDateClockTime;
}
/**
 * This method drives the whole file refresh program.  All methods that
 * are called previous to this are used to setup the refresh variables that
 * determine how things are refreshed and what phase of the refresh is being done.
 *
 * This was developed to only dig three directory structures deep.
 * ie. root/dir1/dir2  only files in dir2 will be coppied.  Any folders in dir2
 * will be ignored.
 *
 * If an error happens to occur anytime during the refresh process then the
 * process will start over and try again.  After the second unsucessfull try 
 * the error will be written to the log and the program will exit.
 *
 * @throws Exception
 */
 
private void getFilesFromSource() throws Exception
{
	Vector lvSeparatedList;

	Vector lvSourceFileList;
	Vector lvSubFileList;
	Vector lvSubSubFileList;
	
	Vector lvSourceFolderList;
	Vector lvSubDirectoryList;

	String lsSourceFolderPath;
	String lsSourceFolder;
			
	// counter to keep track of tries
	int liRetryCount = 0;
	int liMAXTRIES = 5;
	// loop control
	boolean lbLoopControl = true;
	// success indicator
	boolean lbSuccessfulLoad = false;

	while (lbLoopControl)
	{
   		try
    	{
	    	connect();

	    	// Create the destination directory if it does not exist
    		File laMdr = new File(csDestDirectory);
    		if(!laMdr.exists())
    		{
	    		writeLog("Creating destination directory: <" + laMdr.getAbsolutePath() + ">");
	    		// Use mkdirs instead of mkdir so sub directories are created
    			laMdr.mkdirs();
    		}
 
			// call cleanup to go through all the files on the destination directory
			// and make sure that they exist on the source directory.  If not then remove the file
			// or folder from the destination directory
			cleanUpOldFiles(separateDirectory(listDirectory(csSourceDirectory)), separateDirectory(listDirectory(csDestDirectory)));

			// Get the list of the directory and split the list into a vector of files and folders
			lvSeparatedList = separateDirectory(listDirectory(csSourceDirectory));
			lvSourceFileList      = (Vector) lvSeparatedList.elementAt(0);
			lvSourceFolderList	= (Vector) lvSeparatedList.elementAt(1);
				
	    	// Pass the file list and the source directory to actually get the files
	    	if (lvSourceFileList.size() > 0)
	    	{
    			getBinaryFiles(lvSourceFileList, csSourceDirectory);
	    	}

    		// Go into every folder in the root directory ie. root\folder1, root:\folder2, root:\folder3
			for (int i = 0; i < lvSourceFolderList.size(); i++)
			{
				lsSourceFolderPath 	= ((File)lvSourceFolderList.elementAt(i)).getAbsolutePath();
				// Getting the source folder ie. geting (folder1) out of (root:\folder1)
				lsSourceFolder 	= lsSourceFolderPath.substring(csSourceDirectory.length());

				// check to see if folder is valid
				if (isValidFolder(lsSourceFolder))
				{
					// Create the folder in the root directory
		    		File laMdr2 = new File(csDestDirectory + "\\" + lsSourceFolder);
		    		if(!laMdr2.exists())
		    		{
			    		writeLog("Creating directory: <" + laMdr2.getAbsolutePath() + ">");
		    			laMdr2.mkdir();
		    		}

					// call cleanup to go through all the files on the destination directory
					// and make sure that they exist on the source directory.  If not then remove the file
					// or folder from the destination directory
					cleanUpOldFiles(separateDirectory(listDirectory(lsSourceFolderPath)), separateDirectory(listDirectory(csDestDirectory + "\\" + lsSourceFolder)));

					// Get the list of the directory and split the list into a vector of files and folders
					lvSeparatedList		= separateDirectory(listDirectory(lsSourceFolderPath));
					lvSubFileList		= (Vector) lvSeparatedList.elementAt(0);
					lvSubDirectoryList	= (Vector) lvSeparatedList.elementAt(1);
		    		
			    	// Pass the file list and the source directory to actually get the files
			    	if (lvSubFileList.size() > 0)
			    	{
						getBinaryFiles(lvSubFileList, lsSourceFolder);
			    	}

			    	// Go into every folder in the sub directory ie. root:\folder1\subfolder1, root:\folder1\subfolder2
					if (lvSubDirectoryList.size() > 0)
					{
						for (int j = 0; j < lvSubDirectoryList.size(); j++)
						{
							lsSourceFolderPath 	= ((File)lvSubDirectoryList.elementAt(j)).getAbsolutePath();
							// Getting the source folder ie. geting (folder1\subfolder1) out of (root\folder1\subfolder1)
							lsSourceFolder 	= lsSourceFolderPath.substring(csSourceDirectory.length());

							// check to see if folder is valid
							if (isValidFolder(lsSourceFolder))
							{
								// Create the folder in the sub directory ie. root:\folder1\"create this folder"
					    		File laMdr3 = new File(csDestDirectory + "\\" + lsSourceFolder);
					    		if(!laMdr3.exists())
					    		{
						    		writeLog("Creating directory: <" + laMdr3.getAbsolutePath() + ">");
					    			laMdr3.mkdir();
					    		}

								// call cleanup to go through all the files on the destination directory
								// and make sure that they exist on the source directory.  If not then remove the file
								// or folder from the destination directory
								cleanUpOldFiles(separateDirectory(listDirectory(lsSourceFolderPath)), separateDirectory(listDirectory(csDestDirectory + "\\" + lsSourceFolder)));

								// Get the list of the directory and split the list into a vector of files
								lvSeparatedList		= separateDirectory(listDirectory(lsSourceFolderPath));
								lvSubSubFileList	= (Vector) lvSeparatedList.elementAt(0);

						    	// Pass the file list and the source directory to actually get the files
						    	if (lvSubSubFileList.size() > 0)
						    	{
									getBinaryFiles(lvSubSubFileList, lsSourceFolder);
						    	}
							}
							else
							{
								writeLog("Folder excluded: <" + lsSourceFolder + ">");
							}
						}
					}
				}
			}
   			
	    	// exit the loop, we are successful.
	    	lbLoopControl = false;
	    	lbSuccessfulLoad = true;
		}
    	catch(Exception ex)
    	{
			ex.printStackTrace();
			// increment retry count
			liRetryCount = liRetryCount + 1;
	    	writeLog(ex.getMessage() 
		    	+ " Try Number " 
		    	+ liRetryCount);

	    	// check to see if we already tried the max times
	    	if (liRetryCount >= liMAXTRIES)
	    	{
		    	writeLog("Already tried the max times, exit in error!");
		    	lbLoopControl = false;
	    		lbSuccessfulLoad = false;
	    		if (getRefreshType() == TYPE_COUNTY_TO_FLASH)
	    		{
		    		throw ex;
	    		}
	    	}
    	}
    	finally
    	{
	    	disconnect();
    	}
	} // end loop

	if (lbSuccessfulLoad)
	{
		writeLog("***RSPS Refresh Done     - Type = " + getRefreshType() + "***");
	}

	// Need to close log file in case some ohter program needs it
	closeLogFile();
}
/**
 * Gets the Refresh Type.
 *
 * TYPE_COM_TO_COUNTY = 1;
 * TYPE_COUNTY_TO_FLASH = 2;
 * TYPE_FLASH_TO_NOTEBOOK = 3;
 * 
 * @return int
 */
private static int getRefreshType()
{
	return ciRefreshType;
}
/**
 * Gets debug mode.
 * This will determine if anything is written to the
 * console.
 *
 * 0=off
 * 1=on
 * 
 * @return int
 */
private static int getRSPSRefreshDebug()
{
	return ciRSPSRefreshDebug;
}
/**
 * Determines if the code server file is newer.
 *
 * @param aaSourceFile File
 * @param asDestFileName java.lang.String
 * @return boolean
 */
 
private boolean isDifferent(File aaSourceFile, String asDestFileName) 
{
	File laLocalFile = new File(asDestFileName);
	Date laLocalLastModified = new java.util.Date(laLocalFile.lastModified());
	
	Date laRemoteLastModified = new java.util.Date(aaSourceFile.lastModified());
	
	if(!laLocalFile.exists() || !laLocalLastModified.equals(laRemoteLastModified))
	{
		return true;
	}
	else
	{
		return false;
	}
}
/**
 * This will check to see if the flash drive is connected.
 * 
 * @return boolean
 */
private boolean isFlashDriveConnected()
{
	File laFlashDrive = new File(csFlashDriveSourceDirectory);
	if (laFlashDrive.exists())
	{
		return true;
	}
	else
	{
		return false;
	}
}
/**
 * Returns if the log file has been opened.
 * 
 * @return boolean
 */
private static boolean isLogFileOpened()
{
	return cbLogFileOpened;
}
/**
 * When moving files from Comm to County a network drive
 * is connected.  This will return if the network drive is connected.
 * 
 * @return boolean
 */
private static boolean isNetworkDriveConnected()
{
	return cbNetworkDriveConnected;
}
/**
 * Check if valid file type and if the actual filname is to be excluded.
 * This method will also remove any system updates that have been
 * determined to be excluded.
 * 
 * @param asFileName java.lang.String
 * @param asDestinationFile java.lang.String
 * @return boolean
 */
 
private boolean isValidFile(String asFileName, String asDestinationFile) 
{
	if(asFileName == null || asFileName.length() < 1)
	{
		// file name is null or ""
		return false;
	}	
	// defect 8032
	// Allow .csv used by rsps to update the DB with
	// new item codes and etc.
	// defect 8175
	// Allow .msi for DOT hot fixes
	if(	asFileName.toLowerCase().indexOf(".jar") != -1 ||
		asFileName.toLowerCase().indexOf(".cfg") != -1 ||
		asFileName.toLowerCase().indexOf(".bat") != -1 ||
		asFileName.toLowerCase().indexOf(".cmd") != -1 ||
		asFileName.toLowerCase().indexOf(".gif") != -1 ||
		asFileName.toLowerCase().indexOf(".jpg") != -1 ||
		asFileName.toLowerCase().indexOf(".bmp") != -1 || 
		asFileName.toLowerCase().indexOf(".exe") != -1 ||
		asFileName.toLowerCase().indexOf(".txt") != -1 ||
		asFileName.toLowerCase().indexOf(".mdb") != -1 ||
		asFileName.toLowerCase().indexOf(".vbs") != -1 ||
		asFileName.toLowerCase().indexOf(".csv") != -1 ||
		asFileName.toLowerCase().indexOf(".msi") != -1 ||
		asFileName.toLowerCase().indexOf(".dat") != -1)
	{
	// end defect 8175
	// end defect 8032
		if(caNotAllowedFiles.containsKey(asFileName.toLowerCase()))
		{
			// file has proper extension but is "not allowed"
			writeLog("File <" + asFileName + "> valid extension but excluded.");
			return false;
		}
		// This is used to determine what system updates to not move
		else if(caSysUpdatesNotNeeded.containsKey(asFileName.toLowerCase()))
		{
			// If the file is not to be moved and exists on the destination
			// then it needs to be removed.
			File loLocalFile = new File(asDestinationFile);
			if (loLocalFile.exists() && loLocalFile.isFile())
			{
				loLocalFile.delete();
			}
			// file has proper extension but is an System Update that is not needed
			// the file is actally deleted from the destination to clean up
			writeLog("File <" + asFileName + "> valid extension already installed.");
			return false;
		}
		else
		{
			// this file can be copied
			return true;
		}	
	}
	else
	{
		// invalid file extension for refresh
		writeLog("Filname <" + asFileName + "> invalid extension.");
		return false;
	}
}
/**
 * Check if valid folder is to be excluded.
 * 
 * @param asFolderName java.lang.String
 * @return boolean
 */
 
private boolean isValidFolder(String asFolderName) 
{
	if(asFolderName == null || asFolderName.length() < 1)
	{
		// file name is null or ""
		return false;
	}

	if (getRefreshType() == TYPE_FLASH_TO_NOTEBOOK)
	{
		if(caNotAllowedFolders.containsKey(asFolderName.toLowerCase()))
		{
			// file has proper extension but is "not allowed"
			writeLog("Folder <" + asFolderName + "> is excluded.");
			return false;
		}
		else
		{
			// this folder can be copied
			return true;
		}
	}
	else
	{
		return true;
	}
}
/**
 * Get the directory listing
 *
 * @param aDirectory java.lang.String
 * @return File[]
 */
 
private File[] listDirectory(String asDirectory)
{
	File laDirectory = new File(asDirectory);
	return laDirectory.listFiles();
}
/**
 * This method is called from FTPUtility.  It is used in
 * conjunction with the RTS refresh.  After RTS refresh has done
 * its thing then RSPS Refresh will be called using
 * RefreshType 1 (TYPE_COM_TO_COUNTY).  This will move all RSPS source
 * from the comm server to each workstation.
 */
 
public void loadFilesToCounty()
{
	try
	{
		setRefreshType(TYPE_COM_TO_COUNTY);
		loadProperties();
		getFilesFromSource();
	}
	catch (Exception ex)
    {
        ex.printStackTrace();
        writeLog(
	        "Error while refreshing RSPS code and sytem updates Type = " 
	        + getRefreshType() 
	        + " " 
	        + ex.getMessage());
    }
}
/**
 * This method is called from within the RTS project and starts 
 * RefreshType 2 (TYPE_COUNTY_TO_FLASH).  This will move all RSPS
 * source from the rts\rsps directory to the flash drive.
 * Any system update that is not needed anymore is passed in the
 * vector and will not be coppied to the flash drive.  If any file in
 * the notneeded list is already on the flash drive the it is removed
 * from the flash drive.
 *
 * @param avUpdatesNotNeeded Vector
 * @throws Exception
 */
 
public void loadFilesToFlashDrive(Vector avUpdatesNotNeeded) throws Exception
{
		for (int i = 0; i < avUpdatesNotNeeded.size(); i++)
		{
			String lsSysUpdtFileName = 
				(String) avUpdatesNotNeeded.elementAt(i);
			caSysUpdatesNotNeeded.put(
				lsSysUpdtFileName.toLowerCase(), 
				lsSysUpdtFileName.toLowerCase());
		}
		
		setRefreshType(TYPE_COUNTY_TO_FLASH);
		loadProperties();
		getFilesFromSource();
}
/**
 * This method is called from the C++ program on the RSPS Unit.
 * Starts RefreshType 3 (TYPE_FLASH_TO_NOTEBOOK).  This will move all RSPS
 * source from the flash drive to the notbook source directory.
 */
 
public void loadFilesToNotebook()
{
	try
	{
		setRefreshType(TYPE_FLASH_TO_NOTEBOOK);
		loadProperties();
		getFilesFromSource();
	}
	catch (Exception ex)
    {
        ex.printStackTrace();
        writeLog(
	        "Error while refreshing RSPS code and sytem updates Type = " 
	        + getRefreshType() 
	        + " " 
	        + ex.getMessage());
    }
}
/**
 * All properties used in the RSPS Refresh process are loaded here.
 * Depending on the Refresh type the Source Directory and the Destination
 * Directory is set.
 */
 
private void loadProperties() throws Exception
{
	    
	// string to hold property name
	String lsPropertyName = null;

    try
    {
	    // load up the properties 
       	Properties laProperties = new Properties();
       	FileInputStream lpfsFileInputStream;

		if (getRefreshType() == TYPE_FLASH_TO_NOTEBOOK)
		{
	       	lpfsFileInputStream = new FileInputStream(RSPS_PROP_FILE);
		   	laProperties.load(lpfsFileInputStream);
		   	// Close prop file
		   	lpfsFileInputStream.close();
	   	
			// get Notebook Source Directory
			lsPropertyName = NOTEBOOK_SOURCE_DIR;
			csNotebookSourceDirectory = laProperties.getProperty(lsPropertyName).trim();

			// get FlashDrive Source Directory
			lsPropertyName = FLASH_DRIVE_SOURCE_DIR;
			csFlashDriveSourceDirectory = laProperties.getProperty(lsPropertyName).trim();

			// Turn debug mode on for phase 3 so that results are sent tot he console
			ciRSPSRefreshDebug = 1;
			csSourceDirectory	= csFlashDriveSourceDirectory;
			csDestDirectory		= csNotebookSourceDirectory;
		}
		else
		{
		    // These are removed from the list b/c they are only exclude
		    // during the 3rd phase
			caNotAllowedFiles.remove(RSPS_REFRESH_JAR.toLowerCase());
			caNotAllowedFiles.remove(RSPS_UPDATE_EXE.toLowerCase());
			
	       	lpfsFileInputStream = new FileInputStream(RTS_STATIC_PROP_FILE);
		   	laProperties.load(lpfsFileInputStream);
		   	// Close prop file
		   	lpfsFileInputStream.close();
		   	
			// get CommServer Name
			lsPropertyName = COMM_SERVER_NAME;
			csCommServerName = laProperties.getProperty(lsPropertyName).trim();

			// get Comm Drive Letter
			lsPropertyName = COMM_DRIVE_LETTER;
			csCommDriveLetter = laProperties.getProperty(lsPropertyName).trim();

			// get Comm ShareName
			lsPropertyName = COMM_SHARE_NAME;
			csCommShareName = laProperties.getProperty(lsPropertyName).trim();

			// get County RSPS dir
			lsPropertyName = COUNTY_RSPS_SOURCE_DIR;
			csCountyRSPSSourceDirectory = laProperties.getProperty(lsPropertyName).trim();

			// get FlashDrive Source Directory
			lsPropertyName = FLASH_DRIVE_SOURCE_DIR;
			csFlashDriveSourceDirectory = laProperties.getProperty(lsPropertyName).trim();

			// get RSPS Refresh Debug AbstractValue - do not trim on this value
			// since it will only be there for development
			lsPropertyName = RSPS_REFRESH_DEBUG;
			setRSPSRefreshDebug(laProperties.getProperty(lsPropertyName));

	   		if (getRefreshType() == TYPE_COM_TO_COUNTY)
	   		{
		   		csSourceDirectory	= csCommDriveLetter;
				csDestDirectory		= csCountyRSPSSourceDirectory;
	   		}
	   		else if (getRefreshType() == TYPE_COUNTY_TO_FLASH)
	   		{
			   	csSourceDirectory	= csCountyRSPSSourceDirectory;
				csDestDirectory		= csFlashDriveSourceDirectory;
	   		}
		}
	   	lsPropertyName = null;
	   	
	   	// Add a message indicating start of run - Can not do this any earlier
	   	// since we don't know where the log file should be and the debug mode.
	    writeLog("***Starting RSPS Refresh - Type = " + getRefreshType() + "***");
    }
    catch (Exception ex)
    {
        if (lsPropertyName != null)
        {
        	writeLog(lsPropertyName + " last property handled.");
        }
        throw ex;
    }
}
/**
 * Starts the refresh process.  A param must be sent.
 *
 * TYPE_COM_TO_COUNTY = 1;
 * TYPE_COUNTY_TO_FLASH = 2;
 * TYPE_FLASH_TO_NOTEBOOK = 3;
 *
 * @param str java.lang.String[]
 */
 
public static void main(String[] aarrString)
{
	try
	{
		if (aarrString.length != 1)
		{
			writeLog("Usage: Main <PARAMETERS>");
		}
		else
		{
			int liArgs = Integer.parseInt(aarrString[0].trim());
			// instantiate utility and start processing 
			RSPSRefreshUtility laRefreshUtil = new RSPSRefreshUtility();

			// This is for testing TYPE_COM_TO_COUNTY RSPS Refresh
			if (liArgs == TYPE_COM_TO_COUNTY)
			{
				// Start thread that will Refresh all RSPS Source code & updates
				// This simulates what is done in RTSMain
				Thread laRSPSRefreshThread = new Thread(laRefreshUtil, "rspsRefresh");
				laRSPSRefreshThread.start();
			}
			// This is for testing standalone RSPS Refresh
			else if (liArgs == TYPE_COUNTY_TO_FLASH)
			{		
				Vector lv = new Vector();

				lv.add("KB823182.EXE");
				lv.add("KB823559.EXE");
				lv.add("KB823980.EXE");
				lv.add("KB824105.EXE");
				lv.add("KB824141.EXE");
				lv.add("KB824146.EXE");
				lv.add("KB825119.EXE");
				lv.add("KB828035.EXE");
				lv.add("KB835732.EXE");

				laRefreshUtil.loadFilesToFlashDrive(lv);
			}
			// This is the main class for the Notebook side of Refresh
			else if (liArgs == TYPE_FLASH_TO_NOTEBOOK)
			{
				laRefreshUtil.loadFilesToNotebook();
			}
			else
			{
				System.out.println("<PARAMETERS> " + liArgs + " Is not used.");
			}
		}
		
	}
	catch (Exception ex)
    {
        ex.printStackTrace();
        writeLog(
	        "Error while refreshing RSPS code and sytem updates - " 
	        + ex.getMessage());
    }
}
/**
 * Returns the error message sent back to local LPR process by the
 * <code>lpd</code> daemon on the print server. 
 * 
 * @return java.lang.String
 * @param laLPRProcess java.lang.Process
 * @exception com.txdot.isd.rts.services.exception.RTSException
 */
private String readError(Process laLPRProcess) throws IOException
{
	try
	{
		//create the result holder
		StringBuffer laLPRError = new StringBuffer();
		/** 
		 * create reader to read error message from lpd on remote server
		 * read the process' error stream to receive the error message
		 */
		BufferedReader laBufferedReader =
			new BufferedReader(
				new InputStreamReader(laLPRProcess.getErrorStream()));
		//read all lines in the error message 
		String lsLine = "";
		while (true)
		{
			lsLine = laBufferedReader.readLine();
			if (lsLine == null)
			{
				break;
			}
			if (lsLine.length() == 0)
			{
				continue;
			}
			laLPRError.append(lsLine);
		}
		return laLPRError.toString();
	}
	catch (IOException laIOE)
	{
		throw laIOE;
	}
}
/**
 * Returns the output message sent back to local LPR process by the
 * <code>lpd</code> daemon on the print server. 
 * 
 * @return java.lang.String
 * @param laLPRProcess java.lang.Process
 * @exception com.txdot.isd.rts.services.exception.RTSException
 */
private String readOutput(Process laLPRProcess) throws IOException
{
	try
	{
		//create the result holder
		StringBuffer laLPROutput = new StringBuffer();
		/** 
		 * create reader to read output message from lpd on remote server.
		 * read the local process' input stream to receive the output message
		 * from the server
		 */
		BufferedReader laBufferedReader =
			new BufferedReader(
				new InputStreamReader(laLPRProcess.getInputStream()));
		//read all lines in the error message 
		String lsLine = "";
		while (true)
		{
			lsLine = laBufferedReader.readLine();
			if (lsLine == null)
			{
				break;
			}
			if (lsLine.length() == 0)
			{
				continue;
			}
			laLPROutput.append(lsLine);
		}
		//print the error message to stdout
		// System.out.println("Err " + laLPROutput.toString());
		//return the error message
		return laLPROutput.toString();
	}
	catch (IOException laIOE)
	{
		throw laIOE;
	}
}
/**
 * When an object implementing interface <code>Runnable</code> is used 
 * to create a thread, starting the thread causes the object's 
 * <code>run</code> method to be called in that separately executing 
 * thread. 
 * <p>
 * The general contract of the method <code>run</code> is that it may 
 * take any action whatsoever.
 *
 * This is used by RTSMain to start Phase1 of the refresh process.
 *
 * @see     java.lang.Thread#run()
 */
public void run()
{
	loadFilesToCounty();
}
/**
 * Separate directory listing.  Returning a vector of vectors.  The first item
 *	is a vector of files and the second item is a vector of folders.
 *
 * @param aarrFileList File[]
 * @return Vector
 */
private Vector separateDirectory(File[] aarrFileList)
{
	Vector lvFileList = new Vector();
	Vector lvDirectoryList = new Vector();
	
	Vector lvReturnList = new Vector();
	
	for (int i = 0; i < aarrFileList.length; i++)
	{
		if (aarrFileList[i].isFile())
		{
			lvFileList.addElement(aarrFileList[i]);
		}
		else if (aarrFileList[i].isDirectory())
		{
			lvDirectoryList.addElement(aarrFileList[i]);
		}
	}
	lvReturnList.addElement(lvFileList);
	lvReturnList.addElement(lvDirectoryList);
	
	return lvReturnList;
}
/**
 * Used to set log file open status.
 * 
 * @param abLogFileOpened boolean
 */
private static void setLogFileOpened(boolean abLogFileOpened)
{
	cbLogFileOpened = abLogFileOpened;
}
/**
 * When moving files from Comm to County a network drive
 * is connected.  This is used to set network drive connection status.
 * 
 * @param abDriveConnected boolean
 */
private static void setNetworkDriveConnected(boolean abNetworkDriveConnected)
{
	cbNetworkDriveConnected = abNetworkDriveConnected;
}
/**
 * Used to set the Refresh Type.
 *
 * TYPE_COM_TO_COUNTY = 1;
 * TYPE_COUNTY_TO_FLASH = 2;
 * TYPE_FLASH_TO_NOTEBOOK = 3;
 * 
 * @param aiRefreshType int
 */
private static void setRefreshType(int aiRefreshType)
{
	ciRefreshType = aiRefreshType;
}
/**
 * Sets debug mode.
 * This will determine if anything is written to the
 * console.
 * 
 * @param aiRSPSRefreshDebug int
 */
private static void setRSPSRefreshDebug(int aiRSPSRefreshDebug)
{
	ciRSPSRefreshDebug = aiRSPSRefreshDebug;
}
/**
 * Sets debug mode.
 * This will determine if anything is written to the
 * console.
 * 
 * @param asRSPSRefreshDebug String
 */
private static void setRSPSRefreshDebug(String asRSPSRefreshDebug)
{
	try
	{
		setRSPSRefreshDebug(Integer.parseInt(asRSPSRefreshDebug.trim()));
	}
	catch (Exception ex)
	{
		// Set it to off if there is an error getting value
		setRSPSRefreshDebug(0);
	}
}
/**
 * Write messages to the log.
 *
 * @param asStr java.lang.String
 */
private static void writeLog(String asStr) 
{
	try
    {
	    // open the file if it is not already open
	    if(!isLogFileOpened())
		{
			String lsLogFileName = REFRESH_LOG; 
			if (getRefreshType() == 3)
			{
				// If in phase 3 then the log file is written to the flash
				// drive and named after the host name
				lsLogFileName = 
					csSourceDirectory 
					+ "\\"
					+ PHASE_THREE_LOG_DIR
					+ java.net.InetAddress.getLocalHost().getHostName() 
					+ REFRESH_LOG; 
			}
			
			File laFile = new File(lsLogFileName);
			if (!laFile.exists())
			{
				laFile.createNewFile();
			}
					
	        cpfsFileOutputStream = new FileOutputStream(
		        lsLogFileName, 
		        true);
 	        caPrintWriter = new PrintWriter(cpfsFileOutputStream);
 	        setLogFileOpened(true);
	    }
   	    // write the message to the log
   	    String lsMsg = getDateClockTime() + asStr;
        caPrintWriter.println(lsMsg);
        caPrintWriter.flush();

        // If debug is turned on
        if (getRSPSRefreshDebug() == 1)
        {
	        // Write all log writes to console for debugging
	        System.out.println(lsMsg);
        }
    }
    catch (IOException ioException)
    {
          ioException.printStackTrace();
          System.out.println(asStr);
    }
}
}
