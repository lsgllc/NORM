package com.txdot.isd.rts.server.systemcontrolbatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.DisabledPlacard;
import com.txdot.isd.rts.server.db.DisabledPlacardMVDIExportHistory;

/*
 * MVDIExport.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/02/2008	Created.
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/09/2008	Append YYYMMDD to Exported Files Name. Use
 * 							boolean in call to query. 
 * 							add isFullExport(), logMVDIExport()  
 * 							modify run()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	10/16/2009 	Pull data one county at a time for Full 
 * 							Export 
 * 							modify run() 
 * 							defect 9942 Ver Defect_POS_G 
 * ---------------------------------------------------------------------
 */

/**
 * Process to export Placard Data since processing.  Prior run 
 * timestamp maintained as "ExpTimestmp" in 
 * RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY.  ExpTimestamp will be updated upon
 * successful completion. 
 * 
 * POS Cron tab will call this program and upon successful completion, 
 * will FTP to server subsequent processing.     
 *
 * @version	Defect_POS_G	10/16/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		11/02/2008
 */
public class MVDIExport
	extends
		com
		.txdot
		.isd
		.rts
		.client
		.systemcontrolbatch
		.business
		.ThreadMessenger
	implements Runnable
{
	private String csExportType = new String();

	private static final String ARRGS_ERROR =
		"MVDIExport should be run with one parameter "
			+ "use  MVDIExport Partial  or MVDIExport Full";
	private static final String DASHES = "====================";

	private static final int DEFAULT_FTP_PORT = 21;
	private static final String END_OF = "End of ";
	private static final String ERROR_DURING_MVDI_EXPORT_FTP =
		"Error during MVDI Export FTP";
	private final static String ERROR_MESSAGE =
		"There was in error in MVDIExport ";
	private static final String FTP_CONNECTION_FAILED =
		"FTP connection failed: ";
	private static final String FTP_FAILED_FILE_WRITE =
		"FTP FAILED FILE WRITE";
	private static final String FTP_FAILED_LOGIN =
		"Could not login to FTP server";
	private static final String FTP_FAILED_PASSIVE_MODE =
		"Could not enter into passive mode: ";
	private static final String FULL_EXPORT = "FULL";
	private static final String LOADING_FTP_PROPS =
		"Loading FTP Properties";
	public static final String PARTIAL = "PARTIAL";
	private static final String START_OF = "Start of ";
	private static final String SUCCESSFUL_LOGIN =
		"Successful login to the FTP server.";
	private static final String ZIP_EXTENSION = ".zip";

	/**
	 * Inner Class used to mange the properties of the MVDIExport 
	 * file. 
	 *
	 * @version	Defect_POS_B	11/02/2008
	 * @author	Kathy Harrell
	 * <br>Creation Date:		11/02/2008 
	 */
	private class FtpFile
	{
		private final String csFTPDirectory;
		private final String csFTPFile;
		private final String csFTPHost;
		private final String csFTPPassword;
		private final String csFTPUser;

		/**
		 * FtpFile represents a file transfer object.
		 *
		 * @param asFTPHost String
		 * @param asFTPUser String
		 * @param asFTPPassword String
		 * @param asFTPFile String
		 */
		public FtpFile(
			String asFTPHost,
			String asFTPUser,
			String asFTPPassword,
			String asFTPDirectory,
			String asFTPFile)
		{
			super();
			this.csFTPHost = asFTPHost;
			this.csFTPUser = asFTPUser;
			this.csFTPPassword = asFTPPassword;
			this.csFTPDirectory = asFTPDirectory;
			this.csFTPFile = asFTPFile;
		}

		/**
		 * @return csFTPUser.
		 */
		public String getFtpDirectory()
		{
			return this.csFTPDirectory;
		}

		/**
		 * @return caFTPFile.
		 */
		public String getFtpFile()
		{
			return this.csFTPFile;
		}

		/**
		 * @return csFTPHost.
		 */
		public String getFtpHost()
		{
			return this.csFTPHost;
		}

		/**
		 * @return csFTPPassword.
		 */
		public String getFtpPassword()
		{
			return this.csFTPPassword;
		}

		/**
		 * @return csFTPUser.
		 */
		public String getFtpUser()
		{
			return this.csFTPUser;
		}

	}
	private FtpFile caFTPFile = null;

	/**
	 * This is the starting point when running from server.
	 * 
	 * @throws RTSException 
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		MVDIExport laMVDIExport = null;

		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		if (aarrArgs.length != 1)
		{
			System.out.println(ARRGS_ERROR);
		}
		else
		{
			String lsArg1 = aarrArgs[0];

			if (!lsArg1.toUpperCase().equals(PARTIAL.toUpperCase())
				&& !lsArg1.toUpperCase().equals(FULL_EXPORT))
			{
				System.out.println(ARRGS_ERROR);
			}
			else
			{
				laMVDIExport = new MVDIExport(lsArg1);
				com
					.txdot
					.isd
					.rts
					.services
					.communication
					.Comm
					.setIsServer(
					true);
				Thread laThread = new Thread(laMVDIExport);
				laThread.start();
			}
		}
	}

	/**
	 * MVDIExport constructor comment.
	 */
	public MVDIExport(String asType)
	{
		super();
		csExportType = asType;
	}

	/**
	 * Return boolean to denote if Full Export requested 
	 * 
	 * @throws RTSException
	 */
	private boolean isFullExport()
	{
		return csExportType.trim().toUpperCase().equals(FULL_EXPORT);

	}
	/**
	 * Try to load the FTP Properties
	 * 
	 * @throws RTSException
	 */
	public void loadFTPProperties() throws RTSException
	{
		logMVDIExport(START_OF + LOADING_FTP_PROPS);

		try
		{
			String lsFTPHost =
				SystemProperty.getMVDIExportFTPServerName().trim();
			String lsFTPUser =
				SystemProperty.getMVDIExportFTPUserId().trim();
			String lsFTPPass =
				SystemProperty.getMVDIExportFTPPassword().trim();
			String lsFTPDirectory =
				SystemProperty.getMVDIExportFTPDirectory().trim();
			String lsFTPFile =
				SystemProperty.getMVDIExportFTPFileName().trim();

			caFTPFile =
				new FtpFile(
					lsFTPHost,
					lsFTPUser,
					lsFTPPass,
					lsFTPDirectory,
					lsFTPFile);
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		logMVDIExport(END_OF + LOADING_FTP_PROPS);
	}

	/**
	 * Write to SystemOut && Log to BatchLog 
	 * 
	 * @param asMsg
	 */
	private void logMVDIExport(String asMsg)
	{
		asMsg = "MVDIExport: " + asMsg;
		System.out.println(asMsg);
		BatchLog.write(asMsg);
	}

	/**
	 * Put Zipped Export File on Server
	 * 
	 * @return boolean 
	 * @throws RTSException
	 */
	private void putFileOnServer() throws RTSException
	{
		String lsInputFile = caFTPFile.getFtpFile() + ZIP_EXTENSION;

		// Append YYYYMMDD to name for uniqueness 
		RTSDate laCurrentDate = new RTSDate();

		String lsOutputFile =
			caFTPFile.getFtpFile()
				+ laCurrentDate.getYYYYMMDDDate()
				+ (isFullExport() ? FULL_EXPORT : "");

		lsOutputFile = lsOutputFile + ZIP_EXTENSION;

		logMVDIExport(
			"Initiating FTP w/ parm of '" + csExportType.trim() + "'");

		logMVDIExport(
			"Writing "
				+ lsOutputFile
				+ " to server "
				+ caFTPFile.getFtpHost()
				+ ".");

		FTPClient laFTPClient = null;
		try
		{
			laFTPClient = new FTPClient();
			laFTPClient.connect(
				caFTPFile.getFtpHost(),
				DEFAULT_FTP_PORT);

			if (!FTPReply
				.isPositiveCompletion(laFTPClient.getReplyCode()))
			{
				throw new Exception(
					FTP_CONNECTION_FAILED
						+ laFTPClient.getReplyString());
			}

			if (!laFTPClient
				.login(
					caFTPFile.getFtpUser(),
					caFTPFile.getFtpPassword()))
			{
				throw new Exception(FTP_FAILED_LOGIN);
			}

			logMVDIExport(SUCCESSFUL_LOGIN);

			laFTPClient.enterLocalPassiveMode();

			if (!FTPReply
				.isPositiveCompletion(laFTPClient.getReplyCode()))
			{
				throw new Exception(
					FTP_FAILED_PASSIVE_MODE
						+ laFTPClient.getReplyString());
			}

			laFTPClient.setFileType(FTP.BINARY_FILE_TYPE);

			laFTPClient.changeWorkingDirectory(
				caFTPFile.getFtpDirectory());

			String[] larrFiles = laFTPClient.listNames(lsOutputFile);

			InputStream lpfsIn = new FileInputStream(lsInputFile);
			if ((larrFiles != null) && (larrFiles.length != 0))
			{
				logMVDIExport(lsOutputFile + " exists. Deleting.");
				laFTPClient.deleteFile(lsOutputFile);
			}
			logMVDIExport("Storing " + lsOutputFile + ".");
			laFTPClient.storeFile(lsOutputFile, lpfsIn);

			lpfsIn.close();

			if (!FTPReply
				.isPositiveCompletion(laFTPClient.getReplyCode()))
			{
				throw new Exception(
					FTP_FAILED_FILE_WRITE
						+ laFTPClient.getReplyString());
			}
			laFTPClient.disconnect();

		}
		catch (SocketException aeSExc)
		{
			logMVDIExport(ERROR_DURING_MVDI_EXPORT_FTP);

			throw new RTSException(RTSException.JAVA_ERROR, aeSExc);
		}
		catch (IOException aeIOExc)
		{
			logMVDIExport(ERROR_DURING_MVDI_EXPORT_FTP);
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExc);
		}
		catch (Exception aeExc)
		{
			logMVDIExport(ERROR_DURING_MVDI_EXPORT_FTP);
			throw new RTSException(RTSException.JAVA_ERROR, aeExc);
		}
		finally
		{
			if (laFTPClient.isConnected())
			{
				try
				{
					laFTPClient.disconnect();
				}
				catch (IOException aeIOEx)
				{
					// Do Nothing 	
				}
			}
		}
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
		// Write to Batch Log 
		logMVDIExport(DASHES);
		logMVDIExport("Starting with parameter " + csExportType);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		String lsData = new String();

		try
		{
			loadFTPProperties();

			// defect 9942 
			// On Full, run for each County 
			MVDIExportFile.reInitFiles(caFTPFile.getFtpFile());

			DisabledPlacard laDsabldPlcrd =
				new DisabledPlacard(laDBAccess);

			logMVDIExport("Query Disabled Placard Data");

			boolean lbSuccessful = true;

			int liMax = isFullExport() ? 254 : 1;

			for (int i = 1; i <= liMax; i++)
			{
				laDBAccess.beginTransaction();
				lsData =
					laDsabldPlcrd.qryExportDisabledPlacard(
						isFullExport(),
						i);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);

				if (lsData != null)
				{
					if (!isFullExport() && lsData.length() == 0)
					{
						logMVDIExport("No data returned.");
					}

					if (i == 1)
					{
						logMVDIExport("Writing File to Server");
					}
					if (lsData.length() > 0 || i == liMax)
					{
						lbSuccessful =
							MVDIExportFile.write(
								lsData,
								caFTPFile.getFtpFile(),
								i == liMax);

						if (!lbSuccessful)
						{
							break;
						}
					}
				}
			}
			// end defect 9942 

			if (lbSuccessful)
			{
				// Put Zipped Exported File on Server
				logMVDIExport("Putting Zipped File to FTP Server");
				putFileOnServer();

				laDBAccess.beginTransaction();
				DisabledPlacardMVDIExportHistory laDsabldPlcrdMVDIExpHstry =
					new DisabledPlacardMVDIExportHistory(laDBAccess);

				laDsabldPlcrdMVDIExpHstry
					.insDisabledPlacardMVDIExportHistory();
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				logMVDIExport("Successful completion.");
			}
			else
			{
				logMVDIExport("Unsuccessful Write to Server. Process Terminated. ");
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				ERROR_MESSAGE
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ aeRTSEx.getMessage());
			logMVDIExport("Unsuccessful completion.");
			logMVDIExport(DASHES);
		}
	}
}