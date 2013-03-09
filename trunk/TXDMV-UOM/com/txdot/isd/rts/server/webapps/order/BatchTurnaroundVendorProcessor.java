package com.txdot.isd.rts.server.webapps.order;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetSpecialPlateTransaction;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.EmailUtil;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.CommonConstants;

/*
 * BatchTurnaroundVendorProcessor.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Brown		06/17/2008	Created Class.
 * 							defect 9711 Ver MyPlates_POS.
 * B. Brown		07/01/2008	Changed so the main method will thrown an
 * 							error back to the testing JSP.
 * 							Also, do not edit trace numbers coming in
 * 							from the input file. Just attempt to process
 * 							them.
 * 							modify main()
 * 							defect 9711 Ver MyPlates_POS.
 * B. Brown		07/07/2008	Print the stack trace if there is an error 
 * 							processing the TurnAround file
 * 							modify processTurnAroundFile()
 * 							defect 9711 Ver MyPlates_POS.
 * B. Brown		07/09/2008	Code for the flexibility of vendor Plates 
 * 							contained in the usual TurnAround file or
 * 							in our test file format 
 * 							modify loadTurnAoundFile(), main()
 * 							defect 9711 Ver MyPlates_POS.
 * B. Brown		07/14/2008	Change to substring the trace number out  
 * 							when processing a Comptrollers file format.
 * 							modify loadTurnAoundFile()
 * 							defect 9711 Ver MyPlates_POS.
 * B. Brown		07/21/2008  Don't echo the first parm in the main
 * 							method when that parm is null
 * 							modify main()
 * 							defect 9711 Ver MyPlates_POS.
 * B. Brown		07/22/2008  Chnage the properties file name from
 * 							vendor.properties to cpa.properties
 * 							modify VENDOR_PROPS_FILE
 * 							defect 9711 Ver MyPlates_POS.
 * Bob B.		12/10/2009	Catch Exception and report the error to the
 * 							Batchlog.log when there is a send email 
 * 							error.
 * 							modify processErrorTrancenos() 
 * 							defect 10262 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * This Batch process will handle the getting and processing of the
 * file containing the list of Special Plates sold on the Vendors 
 * website (Batch Turnaround file).  
 * This file will have a layout similar to IAPPL's FDOC, and will be
 * turned into POS transactions by this process calling postTrans().
 * Also, the matching special plate transactions in 
 * rts_itrnt_spapp_trans will be marked as completed, and inventory 
 * records in the rts_inv_virtual file marked as "taken".
 *
 * @version	Defect_POS_H	11/10/2009
 * @author	Bob brown
 * <br>Creation Date:		06/17/2008 07:05:00
 */
public class BatchTurnaroundVendorProcessor
{
	/**
	 * Inner Class used to manage the FTP properties for 
	 * accessing the Vendors Special plates sold file, also called the
	 * Batch Turnaround file.
	 *
	 * @version	MyPlates_POS	06/17/2008
	 * @author	Bob brown
	 * <br>Creation Date:		06/17/2008 07:05:00
	 */
	private class FtpFile
	{
		private final String caFTPFile;
		private final String caFTPHost;
		private final String caFTPPassword;
		private final String caFTPUser;
		private final String caLocalFile;

		/**
		 * FtpFile represents a file transfer object.
		 *
		 * @param asFTPHost String
		 * @param asFTPUser String
		 * @param asFTPPassword String
		 * @param asFTPFile String
		 * @param asLocalFile String
		 */
		public FtpFile(
			String asFTPHost,
			String asFTPUser,
			String asFTPPassword,
			String asFTPFile,
			String asLocalFile)
		{
			super();
			this.caFTPHost = asFTPHost;
			this.caFTPUser = asFTPUser;
			this.caFTPPassword = asFTPPassword;
			this.caFTPFile = asFTPFile;
			this.caLocalFile = asLocalFile;
		}

		/**
		 * @return Returns the caFTPFile.
		 */
		public String getFtpFile()
		{
			return this.caFTPFile;
		}

		/**
		 * @return Returns the caFTPHost.
		 */
		public String getFtpHost()
		{
			return this.caFTPHost;
		}

		/**
		 * @return Returns the caFTPPassword.
		 */
		public String getFtpPassword()
		{
			return this.caFTPPassword;
		}

		/**
		 * @return Returns the caFTPUser.
		 */
		public String getFtpUser()
		{
			return this.caFTPUser;
		}

		/**
		 * @return Returns the caLocalFile.
		 */
		public String getLocalFile()
		{
			return this.caLocalFile;
		}
	}

	// Location of the property file. Only populated if 
	// loadFTPProperties() is called.
	private String csPropLoc = null;
	private static final String BATCH_TURN_PROCESSING =
		"Batch Turnaround Processing.";
	// defect 9711	
	// private static final String VENDOR_PROPS_FILE = "cfg/vendor.properties";
	private static final String VENDOR_PROPS_FILE = "cfg/cpa.properties";
	// end defect 9711
	private static final String CREATING_POS_TRANS =
		"Creating POS transaction for traceno ";
	private static final String DASHES = "====================";
	// Default FTP properties.
	private static final String DEFAULT_FTP_FILE =
		"'TEST.VENDOR.PPLEXT'";
	private static final String DEFAULT_FTP_HOST =
		"MVS1.VENDOR.TURNAROUND.FILE";
	private static final String DEFAULT_FTP_LOCAL_FILE = "VENDEXT.DAT";
	private static final String DEFAULT_FTP_PASS = "WHOKNOWS";
	private static final int DEFAULT_FTP_PORT = 21;
	private static final String DEFAULT_FTP_USER = "VEND294";
	private static final String EMAIL_BODY =
		"The following trace numbers have ended in error and need "
			+ "some attention. See the batchlog.log for error details.";
	private static final String EMAIL_FROM =
		"RTS-IVTRS@dot.state.tx.us";
	private static final String EMAIL_SUBJECT =
		"RTS Error: Turnaround File Processing";
	// Email address that is used to send the error email to.
	private static final String EMAIL_TO = "RTS-IVTRS@dot.state.tx.us";
	private static final String ENCODETYPE = "UTF-8";
	private static final String END_OF = "End of ";
	private static final String ERROR_DURRING = "Error durring ";
	private static final String ERROR_TRANS_HANDLER =
		"transaction error processing.";
	private static final String FTP_CONNECTION_FAILED =
		"FTP connection failed: ";
	private static final String FTP_FAILED_ACTIVE_MODE =
		"Could not enter into active mode: ";
	private static final String FTP_FAILED_FILE_WRITE =
		"Could not write file: ";
	private static final String FTP_FAILED_LOGIN =
		"Could not login to FTP server";
	private static final String FTP_FAILED_TRANSFER_TYPE =
		"Could not set transfer type: ";
	public static final String FTP_FILE = "FTPFile";
	private static final String FTP_FILE_TRANSFER =
		"FTP file transfer.";
	public static final String FTP_HOST = "FTPHost";
	public static final String FTP_LOCAL_FILE = "FTPLocalFile";
	public static final String FTP_PASS = "FTPPass";
	private static final String FTP_RETRIEVE = "FTP retrieve process.";
	public static final String FTP_USER = "FTPUser";
	private static final String GETTING_TURN_FILE =
		"FTP process to get the turnaround file.";
	private static final String LOAD_TURN_FILE =
		"loading turnaround file.";
	private static final String LOADING_FTP_PROPS =
		"FTP Propertie loading....";
	private static final String NO_RECORDS_FOR_TRACE_NO =
		"No valid records found for traceno.";
	private static final String NO_SP_TRANSACTIONS =
		"No Special Plate transactions where found in the "
			+ "turnaround File.";
	private static final String NULL_TURN_FILE =
		"Because of some error the turnaournd file was not returned.";
	private static final int NUM_TURN_AROUND_FILES_SAVED = 7;
	private static final String POS_TRANS_ALREADY_CREATED =
		"POS transaction already created for traceno ";
	private static final String PROCESSING_TRACE_NO =
		"Processing traceno ";
	// private static final String RECORD_TYPE_HEADER = "HD";
	private static final String SENDING_ERROR_EMAIL =
		"Sending email for error tracenos.";
	private static final String START_OF = "Start of ";
	private static final String SUCCESSFULL_LOGIN =
		"Successful login to the FTP server.";
	private static final int TRACE_NO_START = 14;
	private static final int TRACE_NO_END = 29;
	
	private static final String TURN_FILE_NOT_THERE =
		"Turnaround file does not exist on the FTP server.";
	private static final String TURN_FILE_PROCESSING =
		"turnaround file transaction processing.";
//	private static final String VENDOR_TRACE_NO_PREFIX = "601VP";	
	private static String csPAS1File = "";
	
	private FtpFile caFTPFile = null;
	private Vector cvSpTransactions = new Vector();
	private static final String RECORD_TYPE_HEADER = "HD";
	private static final int APPLICATION_CODE_START = 17;
	
	// defect 9711
	private static boolean cbTurnAroundFileFormat = true;
	// end defect 9711


	/**
	 * Main method used for testing.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		
		// defect 9711
		if (aarrArgs.length > 0
			&& aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
		// end defect 9711	
		{
			BatchLog.write(
				" *** Processing server file: " + aarrArgs[0]);
			csPAS1File = aarrArgs[0];
		}		
		// defect 9711
		if (aarrArgs.length > 1
			&& aarrArgs[1] != null
			&& aarrArgs[1].length() > 0)
		{
		// end defetc 9711
			if (aarrArgs[1].equals("true"))
			{
				BatchLog.write(
					"Expecting file "
						+ aarrArgs[0]
						+ " to be Comptrollers file format");
			}
			else if (aarrArgs[1].equals("false"))
			{
				BatchLog.write(
					"Expecting file "
						+ aarrArgs[0]
						+ " to contain trace numbers only");
			}
			else
			{
				BatchLog.write(
					"The value of the second parameter must be true "
						+ "or false. AbstractValue = "
						+ aarrArgs[1]);
				System.exit(0);
			}
			cbTurnAroundFileFormat =
				Boolean.valueOf(aarrArgs[1]).booleanValue();
		}
		else
		{
//			defect 9711
			BatchLog.write(
				"Expecting to process a Comptrollers file");				
//					+ aarrArgs[0]
//					+ "Comptrollers file");
//			end defect 9711			
		}

		System.out.println(System.getProperties());

		BatchTurnaroundVendorProcessor laBTP =
			new BatchTurnaroundVendorProcessor();
		try
		{
			// Load the Server Side Cache
			CacheManagerServerBusiness laCMSB =
				new CacheManagerServerBusiness();
			laCMSB.loadStaticCache();
			// Start the Process.
			laBTP.beginProcessing();
		}
		catch (RTSException aeRTSex)
		{
			BatchLog.write(
				"["
					+ new java.util.Date()
					+ "] "
					+ ERROR_DURRING
					+ BATCH_TURN_PROCESSING);
			BatchLog.write(
				"["
					+ new java.util.Date()
					+ "] "
					+ aeRTSex.getMessage());
			BatchLog.write(
				"["
					+ new java.util.Date()
					+ "] "
					+ aeRTSex.getDetailMsg());
			aeRTSex.printStackTrace();
	
			BatchLog.write(
				"["
					+ new java.util.Date()
					+ "] "
					+ END_OF
					+ BATCH_TURN_PROCESSING);
			BatchLog.write(DASHES);
			System.out.println(
				"["
					+ new java.util.Date()
					+ "] "
					+ END_OF
					+ BATCH_TURN_PROCESSING);					
			throw aeRTSex;		
		}
	}

	/**
	 * Main method that does all of the turn around file processing.
	 * This method will do the following:
	 * 
	 * 1. Load the FTP properties that are stored in a separate file.
	 * 2. Do an FTP get to retrieve the Turn Around file from the 
	 *    vendor.
	 * 3. Load the Turn Around file.
	 * 4. Create POS transactions for the given Turn Around file.
	 * 
	 * @throws RTSException
	 */
	private void beginProcessing() throws RTSException
	{
		System.out.println(START_OF + BATCH_TURN_PROCESSING);
		BatchLog.write(DASHES);
		BatchLog.write(START_OF + BATCH_TURN_PROCESSING);
		String lsTurnFileName = "";
		// when csPAS1File is blank, nothing was passed to
		// the this process, so get the Turnaround file from
		// the VENDOR's ftp server, else get the file locally
		// from PAS1.
		if (csPAS1File.equals(""))
		{
			// Load the FTP Properties
			loadFTPProperties();
			// Use FTP to get the Turn Around File
			lsTurnFileName = getTurnAroundFile();
		}
		else
		{
			lsTurnFileName = csPAS1File;
		}
		// Use line below to test with static file, or test with 
		// test Application client, passing in the below file name
		// lsTurnFileName = "D:\\" + DEFAULT_FTP_LOCAL_FILE;
		// If the File is not Null then we will process the file.
		if (lsTurnFileName != null && lsTurnFileName.length() > 0)
		{
			// Load the Special Plate Transactions into a Vector.
			loadTurnAoundFile(lsTurnFileName);
			// Process the Specail Plate Transaction Vector.
			if (cvSpTransactions.size() > 0)
			{
				Vector lvErrorTraceNos = processTurnAroundFile();
				if (lvErrorTraceNos.size() > 0)
				{
					processErrorTrancenos(lvErrorTraceNos);
				}
			}
			else
			{
				System.out.println(NO_SP_TRANSACTIONS);
				BatchLog.write(NO_SP_TRANSACTIONS);
			}
		}
		else
		{
			throw new RTSException(NULL_TURN_FILE);
		}
	}
	/**
	 * Creates a new property file using the default properties.
	 * 
	 * @return InputStream
	 * @throws Exception
	 */
	private InputStream createNewFTPPropFile() throws Exception
	{
		// Get the URL of the Class loader directory.
		URL laURL =
			BatchTurnaroundVendorProcessor
				.class
				.getClassLoader()
				.getResource(
				CommonConstant.STR_SPACE_EMPTY);
		// Decode the URL and create a file object for the new property
		// file.
		File laNewFile =
			new File(
				URLDecoder.decode(
					laURL.getPath(),
					ENCODETYPE).substring(
					1)
					+ VENDOR_PROPS_FILE);
		// If the parent directory does not exists then created it.
		if (!laNewFile.getParentFile().exists())
		{
			laNewFile.getParentFile().mkdirs();
		}
		// Create the new file.
		laNewFile.createNewFile();
		// Build the default properties
		Properties laServerPrp = new Properties();
		laServerPrp.put(FTP_HOST, DEFAULT_FTP_HOST);
		laServerPrp.put(FTP_USER, DEFAULT_FTP_USER);
		laServerPrp.put(
			FTP_PASS,
			UtilityMethods.encryptPassword(DEFAULT_FTP_PASS));
		laServerPrp.put(FTP_FILE, DEFAULT_FTP_FILE);
		laServerPrp.put(FTP_LOCAL_FILE, DEFAULT_FTP_LOCAL_FILE);
		// Get the output stream used to save the default properties.
		OutputStream laFileServerOut = new FileOutputStream(laNewFile);
		// Save the properties.
		laServerPrp.store(
			laFileServerOut,
			CommonConstant.STR_SPACE_EMPTY);
		laFileServerOut.flush();
		// Close the output stream.
		laFileServerOut.close();

		csPropLoc = laNewFile.getAbsolutePath();

		// Return the input stream to the new property file.
		return new FileInputStream(laNewFile);
	}

	/**
	 * Used to save iterations of the file.  The oldest file that 
	 * matches the pattern of the new file will be deleted.
	 * 
	 * @param aaFTPFile FtpFile
	 * @return String
	 * @throws RTSException
	 */
	private String getFileName(FtpFile aaFTPFile) throws RTSException
	{
		File laFile = null;
		File laParentFolder = null;
		File[] larrFile = null;
		Hashtable lhtHsh = new Hashtable();
		String lsReturnFileName = CommonConstant.STR_SPACE_EMPTY;
		String lsLocalFileExt = CommonConstant.STR_SPACE_EMPTY;
		String lsPartialLocalFileName = CommonConstant.STR_SPACE_EMPTY;
		boolean lbRename = false;
		int liNum = 0;

		try
		{
			laFile = new File(aaFTPFile.getLocalFile());
			// When running in WSAD, we are reading the vendor.properties
			// file in D:\Program Files\IBM\WSAD.v5.1.2\runtimes\
			// base_v51\properties\cfg (backslashes)

			// When running in TS1 (or PAS1), we are reading the 
			// vendor.properties file in /rts/POS2/usr/WebSphere5/
			// AppServer/lib/cfg (forward slashes)
			// Use File.separator to determine which character is the
			// file separator, according to the file system
			int liFileSepLoc =
				laFile.getAbsolutePath().lastIndexOf(File.separator);
			// if liFileSepLoc is still less than zero,
			// laParentFolder will not be able to be determined
			// so the file name we are trying to retrieve will not
			// be able to be retrieved from the vendor.properties file
			// so write a message 
			if (liFileSepLoc < 0)
			{
				System.out.println(
					"Slash or backslash not found in "
						+ laFile.getAbsolutePath());
			}
			int liDotLoc =
				laFile.getName().indexOf(CommonConstant.STR_PERIOD);
			lsPartialLocalFileName =
				laFile.getName().substring(0, liDotLoc);
			if (laFile.getName().length() > liDotLoc)
			{
				lsLocalFileExt =
					laFile.getName().substring(liDotLoc + 1);
			}

			// Get the parent folder and see if it needs to be created.
			laParentFolder =
				new File(
					laFile.getAbsolutePath().substring(
						0,
						liFileSepLoc));
			if (!laParentFolder.exists())
			{
				laParentFolder.mkdirs();
			}

			larrFile = laParentFolder.listFiles();
			for (int liIndex = 0; liIndex < larrFile.length; liIndex++)
			{
				//If directory, continue
				if (larrFile[liIndex].isDirectory())
				{
					continue;
				}
				String lsCurrFullFileName = larrFile[liIndex].getName();
				liDotLoc =
					lsCurrFullFileName.indexOf(
						CommonConstant.STR_PERIOD);
				//File does not have any extension, discard it.
				if (liDotLoc == -1)
				{
					continue;
				}
				String lsCurrPartialFileName =
					lsCurrFullFileName.substring(0, liDotLoc);

				// If we are just saving 1 and this is the file we will
				// delete it.
				if (NUM_TURN_AROUND_FILES_SAVED == 1
					&& lsCurrFullFileName.toUpperCase().equals(
						laFile.getName()))
				{
					larrFile[liIndex].delete();
					break;
				}
				else if (
					lsCurrPartialFileName.toUpperCase().indexOf(
						lsPartialLocalFileName.toUpperCase())
						> -1)
				{
					String lsStrNum =
						lsCurrFullFileName.substring(
							lsPartialLocalFileName.length(),
							liDotLoc);
					if (lsStrNum != null
						&& !lsStrNum.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						lhtHsh.put(
							new Integer(lsStrNum),
							larrFile[liIndex]);
						int liTempInt =
							new Integer(lsStrNum).intValue();
						if (liNum < liTempInt)
						{
							liNum = liTempInt;
						}
					}
					else
					{
						lhtHsh.put(new Integer(0), larrFile[liIndex]);
					}
					lbRename = true;
				}
			}

			// Rename prior Versions
			if (lbRename)
			{
				for (int liJIndex = liNum; liJIndex >= 0; liJIndex--)
				{
					File laTfile =
						(File) lhtHsh.get(new Integer(liJIndex));
					if (laTfile == null
						|| liJIndex > NUM_TURN_AROUND_FILES_SAVED + 1)
					{
						continue;
					}

					if (liJIndex == NUM_TURN_AROUND_FILES_SAVED - 1)
					{
						laTfile.delete();
					}
					else
					{
						laTfile.renameTo(
							new File(
								laParentFolder.getAbsolutePath()
									+ File.separator
									+ lsPartialLocalFileName
									+ UtilityMethods.addPadding(
										String.valueOf((liJIndex + 1)),
										3,
										CommonConstant.STR_ZERO)
									+ CommonConstant.STR_PERIOD
									+ lsLocalFileExt));
					}
				}
			}

			// Produce the File Name
			if (NUM_TURN_AROUND_FILES_SAVED > 1)
			{
				lsReturnFileName =
					laParentFolder.getAbsolutePath()
						+ File.separator
						+ lsPartialLocalFileName
						+ CommonConstant.STR_ZERO
						+ CommonConstant.STR_ZERO
						+ CommonConstant.STR_ZERO
						+ CommonConstant.STR_PERIOD
						+ lsLocalFileExt;
			}
			else
			{
				lsReturnFileName = laFile.getAbsolutePath();
			}
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}

		return lsReturnFileName;
	}

	/**
	 * Gets the location on the property file for saving.
	 * 
	 * If you called loadFTPProperties() prior then this value will be
	 * populated if the property file was loaded sucesfully.
	 * 
	 * @return String
	 */
	public String getLocationOfPropFile()
	{
		return csPropLoc;
	}

	/**
	 * Gets the Plate Number from the Trace No.
	 * 
	 * @param asTranceNo String
	 * @return String
	 */
	private String getPlateNo(String asTranceNo)
	{
		String lsPltNo = CommonConstant.STR_SPACE_EMPTY;
		int liAppCodeLoc =
			asTranceNo.indexOf(CommonConstants.APPLICATION_CODE);
		lsPltNo =
			asTranceNo.substring(
				liAppCodeLoc
					+ CommonConstants.APPLICATION_CODE.length(),
				asTranceNo.length());
		// If the total plate no and random numbers == 10 then we
		// could have a 6 or 7 digit plate we will return 7 just incase
		// if we do SQL with this plate no we will use both the 6 and 
		// 7 digit version.
		if (lsPltNo.length() == 10)
		{
			lsPltNo = lsPltNo.substring(0, lsPltNo.length() - 2);
		}
		else
		{
			lsPltNo = lsPltNo.substring(0, lsPltNo.length() - 4);
		}

		return lsPltNo;
	}

	/**
	 * Method uses organizations.apache.commons.net.ftp.FTPClient to go to the
	 * comptroler to retrieve the turnaround file and save the file.
	 * 
	 * We will be saving iterations of this file for debug purposes.
	 * If the number of days that we keep a file needs to be increased
	 * we just increase the NUM_TURN_AROUND_FILES_SAVED value.
	 * 
	 * @return String
	 * @throws RTSException
	 */
	private String getTurnAroundFile() throws RTSException
	{
		BatchLog.write(START_OF + GETTING_TURN_FILE);
		String lsReturnFile = null;
		try
		{
			FTPClient laFTPClient = new FTPClient();
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

			BatchLog.write(SUCCESSFULL_LOGIN);
			System.out.println(SUCCESSFULL_LOGIN);

			laFTPClient.setFileType(
				org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE);

			if (!FTPReply
				.isPositiveCompletion(laFTPClient.getReplyCode()))
			{
				throw new Exception(
					FTP_FAILED_TRANSFER_TYPE
						+ laFTPClient.getReplyString());
			}

			laFTPClient.enterLocalActiveMode();

			if (!FTPReply
				.isPositiveCompletion(laFTPClient.getReplyCode()))
			{
				throw new Exception(
					FTP_FAILED_ACTIVE_MODE
						+ laFTPClient.getReplyString());
			}

			String[] larrFiles =
				laFTPClient.listNames(caFTPFile.getFtpFile());
			if ((larrFiles != null) && (larrFiles.length != 0))
			{
				lsReturnFile = retrieveFile(laFTPClient, caFTPFile);
			}
			else
			{
				BatchLog.write(TURN_FILE_NOT_THERE);
				laFTPClient.disconnect();
			}
			
		}
		catch (SocketException aeSExc)
		{
			BatchLog.write(ERROR_DURRING + GETTING_TURN_FILE);
			throw new RTSException(RTSException.JAVA_ERROR, aeSExc);
		}
		catch (IOException aeIOExc)
		{
			BatchLog.write(ERROR_DURRING + GETTING_TURN_FILE);
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExc);
		}
		catch (Exception aeExc)
		{
			BatchLog.write(ERROR_DURRING + GETTING_TURN_FILE);
			throw new RTSException(RTSException.JAVA_ERROR, aeExc);
		}

		BatchLog.write(END_OF + GETTING_TURN_FILE);

		return lsReturnFile;
	}

	/**
	 * Try to load the properties file using the class loader but if
	 * it can not find it there then try the user.dir.  If the file
	 * does not exist then create one using the default properties in
	 * the classloader directory.
	 * 
	 * @return Properties
	 * @throws RTSException
	 */
	public Properties loadFTPProperties() throws RTSException
	{
		BatchLog.write(START_OF + LOADING_FTP_PROPS);
		InputStream laFileServer = null;
		Properties laServerPrp = null;
		String lsClassPath = System.getProperty("java.class.path");
		String lsFileSeparator = System.getProperty("file.separator");
		String lsPathSeparator = System.getProperty("path.separator");
		String lsCurrentCPEntry = "";
		// the following statement is for desktop testing 
		// need to add to the classpath for the vendor.properties file
		// to be found when desktop testing
//		 lsClassPath = lsClassPath + ";" + 
//			"D:\\ClearCaseViews\\rlbrown_Special_Plates_dev_2\\" +
//				"rtspos_cvob\\pos\\rtsposproject\\JavaSource\\";
		StringTokenizer lsClassPaths = 
			new StringTokenizer(lsClassPath, lsPathSeparator);			
		File laVENDORProp = null;			
		try
		{
			while (lsClassPaths.hasMoreTokens())
			{
				lsCurrentCPEntry = lsClassPaths.nextToken();
				laVENDORProp = 
					new File(lsCurrentCPEntry  + 
						lsFileSeparator + 
						VENDOR_PROPS_FILE);
				System.out.println("Classpath entry = " + 
					lsCurrentCPEntry);
				 if (laVENDORProp.canRead())
				 {
					 laFileServer = new FileInputStream(laVENDORProp);
					 csPropLoc = laVENDORProp.getAbsolutePath();
					 System.out.println("Using file: " + csPropLoc);
					 break;
				 }			
			}
			if (!laVENDORProp.canRead())
			{
				System.err.println(VENDOR_PROPS_FILE + " File not found");
				throw new FileNotFoundException(VENDOR_PROPS_FILE + 
				" File not found");
			}		
		}
	
		 catch (FileNotFoundException leFNFEx)
		 {
			 System.err.println("File not found exception");
			 leFNFEx.printStackTrace();
		 }
	
		 catch (NoSuchElementException leNSEEx)
		 {
			 System.err.println("string tokenizer error");
			 leNSEEx.printStackTrace();
		 }

		try
		{
			laServerPrp = new Properties();
			laServerPrp.load(laFileServer);
			laFileServer.close();
			String lsFTPHost = laServerPrp.getProperty(FTP_HOST);
			String lsFTPUser = laServerPrp.getProperty(FTP_USER);
			String lsFTPPass =
				UtilityMethods.decryptPassword(
					laServerPrp.getProperty(FTP_PASS));
			String lsFTPFile = laServerPrp.getProperty(FTP_FILE);
			String lsFTPLocalFile =
				laServerPrp.getProperty(FTP_LOCAL_FILE);

			caFTPFile =
				new FtpFile(
					lsFTPHost,
					lsFTPUser,
					lsFTPPass,
					lsFTPFile,
					lsFTPLocalFile);
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		finally
		{
			if (laFileServer != null)
			{
				try
				{
					laFileServer.close();
				}
				catch (IOException aeIOEx)
				{
				}
			}
		}

		BatchLog.write(END_OF + LOADING_FTP_PROPS);
		return laServerPrp;
	}

	/**
	 * Loads the turnaround file and puts the Special Plate Transactions
	 * into a vector.
	 * 
	 * @param asFile String
	 * @throws RTSException
	 */
	private void loadTurnAoundFile(String asFile) throws RTSException
	{
		BatchLog.write(START_OF + LOAD_TURN_FILE);
		System.out.println(START_OF + LOAD_TURN_FILE);

		try
		{
			FileInputStream lpfstream = new FileInputStream(asFile);
			DataInputStream laIn = new DataInputStream(lpfstream);
			BufferedReader laBr =
				new BufferedReader(new InputStreamReader(laIn));
			String lsStrLine;
			while ((lsStrLine = laBr.readLine()) != null)
			{
				// defect 9711
				if (cbTurnAroundFileFormat)
				{
					if (lsStrLine.startsWith(RECORD_TYPE_HEADER)
						&& lsStrLine.indexOf(
							CommonConstants.VENDOR_APPLICATION_CODE)
							== APPLICATION_CODE_START)
					{
						// defect 9711
						// cvSpTransactions.add(lsStrLine.trim());
						cvSpTransactions.add(
							lsStrLine
								.substring(TRACE_NO_START, TRACE_NO_END)
								.trim());
						// end defect 9711		
					}
				}
				else
				{
					cvSpTransactions.add(lsStrLine.trim());
				}
				// end defect 9711
			}
		}
		catch (IOException aeIOExc)
		{
			BatchLog.write(ERROR_DURRING + LOAD_TURN_FILE);
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExc);
		}
		catch (Exception aeExc)
		{
			BatchLog.write(ERROR_DURRING + LOAD_TURN_FILE);
			throw new RTSException(RTSException.JAVA_ERROR, aeExc);
		}

		BatchLog.write(END_OF + LOAD_TURN_FILE);
	}

	/**
	 * Handles the Error Processing for the transactions that need some
	 * attention.
	 * 
	 * @param avErrorTraceNos Vector
	 */
	private void processErrorTrancenos(Vector avErrorTraceNos)
	{
		BatchLog.write(START_OF + ERROR_TRANS_HANDLER);
		System.out.println(START_OF + ERROR_TRANS_HANDLER);

		// Build s list of trace numbers to be emailed.
		String lsTraceNos = CommonConstant.STR_SPACE_EMPTY;		
		// the error detail is in 
		for (int i = 0; i < avErrorTraceNos.size(); i++)
		{
			lsTraceNos += avErrorTraceNos.get(i)
				+ "<br>";	
		}
		BatchLog.write(SENDING_ERROR_EMAIL);
		System.out.println(SENDING_ERROR_EMAIL);

		// Send email
		// defect 10262
		// EmailUtil laEmailUtil = new EmailUtil();
//		if (!laEmailUtil
//			.sendEmail(
//				EMAIL_FROM,
//				EMAIL_TO,
//				EMAIL_SUBJECT,
//				EMAIL_BODY 
//			//defect 9448
//				+ "<br>"
//				+ CommonConstant.SYSTEM_LINE_SEPARATOR))					
//				+ lsTraceNos))
			// end defect 9338
		try
		{
			EmailUtil laEmailUtil = new EmailUtil();
			laEmailUtil.sendEmail(
				EMAIL_FROM,
				EMAIL_TO,
				EMAIL_SUBJECT,
				EMAIL_BODY + "<br>" + lsTraceNos);	
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			BatchLog.write(ERROR_DURRING + SENDING_ERROR_EMAIL +
			leEx.getMessage());
		}
//		{
//			BatchLog.write(ERROR_DURRING + SENDING_ERROR_EMAIL);
//		}
		BatchLog.write(END_OF + ERROR_TRANS_HANDLER);
	}
	/**
	 * Used to process the Special Plate Transaction that where taken
	 * from the Tunraround file.  If no transaction where found for a
	 * given traceno or we did not find one already completed or in
	 * a pending state will will place that traceno in a vector to be
	 * returned to the calling program for error processing.
	 */
	private Vector processTurnAroundFile()
	{
		BatchLog.write(START_OF + TURN_FILE_PROCESSING);
		System.out.println(START_OF + TURN_FILE_PROCESSING);

		Vector lvErrorTrans = new Vector();
		String lsRegPltNo = "";
		String lsMfgPltNo = "";
		String lsTransStatus = "";
		DatabaseAccess laDBAccess = new DatabaseAccess();

		// Loop through trace numbers that were in turn file.
		for (int i = 0; i < cvSpTransactions.size(); i++)
		{
			lsRegPltNo = "";
			lsMfgPltNo = "";
			lsTransStatus = "";
			String lsTranceNo = String.valueOf(cvSpTransactions.get(i));
			BatchLog.write(PROCESSING_TRACE_NO + lsTranceNo);
			System.out.println(PROCESSING_TRACE_NO + lsTranceNo);

			try
			{
				laDBAccess.beginTransaction();
				InternetSpecialPlateTransaction laSPTransAccess =
					new InternetSpecialPlateTransaction(laDBAccess);

				// Get the internet transaction records for the given
				// Trace No.
				Vector lvRecords =
					laSPTransAccess.qryTransactionRecord(lsTranceNo);
				// End Trans so that table is not on hold when we update
				// the internet trans to completed.
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				boolean lbTransCreated = true;
				String lsMessage = "";
				if (lvRecords.size() > 0)
				{
					for (int j = 0; j < lvRecords.size(); j++)
					{
						SpecialPlateItrntTransData laSPTransData =
							(SpecialPlateItrntTransData) lvRecords.get(
								j);
						lsRegPltNo = laSPTransData.getRegPltNo();
						lsMfgPltNo = laSPTransData.getMfgPltNo();
						lsTransStatus = laSPTransData.getTransStatusCd();	
						if (laSPTransData
							.getTransStatusCd()
							.equals(
								CommonConstants.TRANS_STATUS_PENDING))
						{
							BatchLog.write(
									CREATING_POS_TRANS
										+ lsTranceNo
										+ " "
										+ "Reg Plate No = "
										+ lsRegPltNo
										+ " "
										+ "Mfg Plate No = "
										+ lsMfgPltNo
										+ " "
										+ "Trans status before update = "
										+ lsTransStatus);
							// Create POS trans and finalize Inventory.
							CommonServerBusiness laCommServerBuss =
									new CommonServerBusiness();	
							try
							{
								laCommServerBuss.processData(
									GeneralConstant.COMMON,
									CommonConstant.PROC_VENDOR_PLATES,
									laSPTransData);
							}						
							catch (RTSException leRTSEx)
							{
								lbTransCreated = false;
								BatchLog.write(
									"RTSException Occurred");
								BatchLog.write(" Code = "
										+ leRTSEx.getCode());
								BatchLog.write("Unable to " 
										+ "create transaction for " 
										+ "rts_itrnt_spapp_trans " 										
										+ "Trace no = "
										+ lsTranceNo
										+ " Reg Plate No = "
										+ lsRegPltNo
										+ " Mfg Plate No = "
										+ lsMfgPltNo
										+ " and trans status = "
										+ lsTransStatus);
								lsMessage = "<br>" + "Unable to " 
										+ "create transaction for " 
										+ "rts_itrnt_spapp_trans " 										
										+ "trace no: "
										+ lsTranceNo
										+ " Reg Plate No = "
										+ lsRegPltNo
										+ " Mfg Plate No = "
										+ lsMfgPltNo
										+ " and trans status = "
										+ lsTransStatus
										+ ": See batchlog.log ";	
								// defect 9711
								leRTSEx.printStackTrace();	
								// end defect 9711		
							}						
							catch (Exception leEx)
							{
								lbTransCreated = false;
								BatchLog.write(
									"Exception Message = "
										+ leEx.getMessage());
								BatchLog.write(
									"Unable to "
										+ "create transaction for "
										+ "rts_itrnt_spapp_trans "
										+ "trace no: "
										+ lsTranceNo
										+ " Reg Plate No = "
										+ lsRegPltNo
										+ " Mfg Plate No = "
										+ lsMfgPltNo
										+ " and trans status = "
										+ lsTransStatus);
								lsMessage = "<br>" + "Unable to " 
										+ "create transaction for " 
										+ "rts_itrnt_spapp_trans " 										
										+ "trace no: "
										+ lsTranceNo
										+ " Reg Plate No = "
										+ lsRegPltNo
										+ " Mfg Plate No = "
										+ lsMfgPltNo
										+ " and trans status = "
										+ lsTransStatus
										+ ": See batchlog.log ";
								// defect 9711		
								leEx.printStackTrace();
								// end defect 9711	
							}

							try
							{
								Thread.sleep(1000); 
							}
							catch (InterruptedException aeIEx)
							{ // we will not take any action on this 
							  // exception
							} 
						}
						else if (
							laSPTransData.getTransStatusCd().equals(
								CommonConstants
									.TRANS_STATUS_COMPLETED))
						{
							BatchLog.write(
							POS_TRANS_ALREADY_CREATED
								+ lsTranceNo
								+ " "
								+ "Reg Plate No = "
								+ lsRegPltNo
								+ " "
								+ "Mfg Plate No = "
								+ lsMfgPltNo);

						}
						else
						{
							lbTransCreated = false;
							BatchLog.write(
							"Trasnsaction not created for trace no: "
								+ lsTranceNo
								+ " "
								+ "Reg Plate No = "
								+ lsRegPltNo
								+ " "
								+ "Mfg Plate No = "
								+ lsMfgPltNo
								+ " "
								+ "Current Trans status = "
								+ lsTransStatus);
						lsMessage = "<br>" + "Unable to " 
								+ "create transaction for " 
								+ "rts_itrnt_spapp_trans " 										
								+ "trace no: "
								+ lsTranceNo
								+ " Reg Plate No = "
								+ lsRegPltNo
								+ " Mfg Plate No = "
								+ lsMfgPltNo
								+ " and trans status = "
								+ lsTransStatus;			
						}
					}
				}
				else
				// TurnAround trace number not found on spapp_trans
				{
					lbTransCreated = false;
					BatchLog.write("Transaction not found on " +
						"rts_itrnt_spapp_trans for trace no: " + 
						lsTranceNo);	
					lsMessage = "<br>" + "Transaction not found on " 
							+ "rts_itrnt_spapp_trans for trace no: " 
							+ lsTranceNo;
				}
				if (!lbTransCreated)
				{
					lvErrorTrans.add(lsMessage);
				}
			}
			catch (RTSException aeRTSEx)
			{
				BatchLog.write(
					ERROR_DURRING
						+ TURN_FILE_PROCESSING
						+ CommonConstant.STR_SPACE_ONE
						+ "Trasnsaction not created for trace no: "
								+ lsTranceNo
								+ " "
								+ "Reg Plate No = "
								+ lsRegPltNo
								+ " "
								+ "Mfg Plate No = "
								+ lsMfgPltNo
								+ " "
								+ "Current Trans status = "
								+ lsTransStatus);				
					BatchLog.write(aeRTSEx.getMessage());
					BatchLog.write(aeRTSEx.getDetailMsg());
					lvErrorTrans.add(
						lsTranceNo
							+ CommonConstant.STR_DASH
							+ aeRTSEx.getMessage());
				try
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (RTSException aeRTSEx2)
				{
					aeRTSEx2.printStackTrace(); 
				}
			}
		}
		BatchLog.write(END_OF + TURN_FILE_PROCESSING);
			return lvErrorTrans;
	} 	
	/**
	 * Method retrieveFile starts a new thread to get the specified file.
	 * 
	 * @param aaFTPClient FTPClient
	 * @param aaFTPFile FtpFile
	 * @return String
	 * @throws Exception
	 */
	private String retrieveFile(
		FTPClient aaFTPClient,
		FtpFile aaFTPFile)
		throws Exception
	{
		BatchLog.write(START_OF + FTP_RETRIEVE);
			String lsOutFile = null;
		try
		{
			lsOutFile = getFileName(aaFTPFile);
			BatchLog.write(START_OF + FTP_FILE_TRANSFER);
			System.out.println(START_OF + FTP_FILE_TRANSFER);
			OutputStream lpfsOut = new FileOutputStream(lsOutFile);
			aaFTPClient.retrieveFile(
				aaFTPFile.getFtpFile(),
				lpfsOut);
			lpfsOut.close();
			if (!FTPReply
				.isPositiveCompletion(aaFTPClient.getReplyCode()))
			{
				throw new Exception(
					FTP_FAILED_FILE_WRITE
						+ aaFTPClient.getReplyString());
			}
			BatchLog.write(END_OF + FTP_FILE_TRANSFER);
				System.out.println(END_OF + FTP_FILE_TRANSFER);
		}
		finally
		{
			try
			{
				aaFTPClient.disconnect(); }
			catch (Exception aeEx)
			{ // Swallow error
			}

			return lsOutFile; 
		}
	}
}