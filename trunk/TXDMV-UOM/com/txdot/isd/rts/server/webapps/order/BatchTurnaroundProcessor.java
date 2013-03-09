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
 * BatchTurnaroundProcessor.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		07/02/2007	Created Class.
 * 							defect 9121 Ver Special Plates.
 * Bob B.		08/13/2007	Use File.separator to determine which
 *							character is the file separator to determine
 *							the absolute path of the cpa.properties file
 * 							modify getFilename()
 * 							defect 9119 Ver Special Plates.
 * Bob B.		08/29/2007	Add Thread.sleep(1000) to avoid duplicate
 * 							key exceptions on rts_trans insert
 * 							modify processTurnAroundFile()
 * 							defect 9119	Ver Special Plates.
 * Bob B.		09/27/2007	Removed the break statement in 2 places in
 * 							the processing loop so we process every
 * 							plate in the shopping cart.
 * 							Also added regpltNo and mfgPltNo in logging.
 * 							modify processTurnAroundFile()
 * 							defect 9119	Ver Special Plates.
 * Bob B.		10/03/2007	Loop through the "end directory" of the 
 * 							classpath to see where the cpa.properties 
 * 							file is located, so when the change password
 * 							jsp executes, we write the updated file back 
 * 							out, it writes it to the same directory.
 * 							This way, the BatchTurnaroundProcess will
 * 							find the cpa.properties file with the new
 * 							password, and not re-create the file in a
 * 							directory the process can't find.
 * 							modify loadFTPProperties()   
 * 							defect 9119	Ver Special Plates.
 * Bob B.		10/04/2007	Use System.getProperty("file.separator") and
 * 							System.getProperty("path.separator") so the
 * 							classpath is properly used and the 
 * 							cpa.properties file is found. 
 * 							modify loadFTPProperties()   
 * 							defect 9119	Ver Special Plates.
 * Bob B.		11/21/2007	Add the ability to process a previous 
 * 							generation of the BatchTurnAround file
 * 							locally on the server.
 * 							Also, process only (P)ending records and
 * 							add try/catch block around add trans call.
 * 							add csPAS1File 	
 *							modify main(), beginProcessing(), 
 *							processTurnAroundFile(),
 *							processErrorTrancenos()   
 * 							defect 9448	Ver Special Plates.
 * Bob B.		09/04/2008	Add a check for aarrArgs[0].length() > 0
 * 							so when we rerun this process with the jsp
 * 							the empty string that's passed from the JSP
 * 							does not get interpreted as a file name
 * 							we want to process.
 * 							modify main()
 * 							defect 9801 Ver Defect_POS_B
 * Bob B.		12/10/2009	Catch Exception and report the error to the
 * 							Batchlog.log when there is a send email 
 * 							error.
 * 							modify processErrorTrancenos() 
 * 							defect 10262 Ver Defect_POS_H
 * Bob B.		03/22/2010	Add the ability to process a file with
 * 							trace numbers only in case the auto create
 * 							of the transcation did not occur and we
 * 							don't want to process a Turnaround file.
 * 							modify main()
 * 							defect 10391 Ver POS_640
 * Bob B.		06/16/2010	Change to local passive mode for FTP'ing
 * 							modify getTurnAroundFile()
 * 							defect 10515 Ver POS_640
 * ---------------------------------------------------------------------
 */

/**
 * Batch process that will handle an FTP get to the Comptroler to get
 * the turnaround file.  The turnaround file contains transactions
 * seperated tracenumber.  The transactions that are special plate
 * order transactions will be turned into POS transactions and the 
 * internet transaction will be marked as completed.
 *
 * @version	POS_640			06/16/2010
 * @author	Jeff Seifert
 * <br>Creation Date:		07/02/2007 11:20:00
 */
public class BatchTurnaroundProcessor
{
	// defect 10391
	private static boolean cbTurnAroundFileFormat = true;
	// end defect 10391

	/**
	 * Inner Class used to mange the properties of the Turn Around File
	 * processing.
	 *
	 * @version	Special Plates	07/03/2007
	 * @author	Jeff Seifert
	 * <br>Creation Date:		07/03/2007 11:30:00
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
	// Position in the Turnaround File where the Application Code starts
	private static final int APPLICATION_CODE_START = 17;
	private static final String BATCH_TURN_PROCESSING =
		"Batch Turnaround Processing.";
	private static final String CPA_PROPS_FILE = "cfg/cpa.properties";
	private static final String CREATING_POS_TRANS =
		"Creating POS transaction for traceno ";
	private static final String DASHES = "====================";
	// Default FTP properties.
	private static final String DEFAULT_FTP_FILE =
		"'TEST.USA.AGY601.PPLEXT'";
	private static final String DEFAULT_FTP_HOST =
		"MVS1.CPA.STATE.TX.US";
	private static final String DEFAULT_FTP_LOCAL_FILE = "PPLEXT.DAT";
	private static final String DEFAULT_FTP_PASS = "DRR05LYN";
	private static final int DEFAULT_FTP_PORT = 21;
	private static final String DEFAULT_FTP_USER = "CPA601";
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
			+ "trunaround File.";
	private static final String NULL_TURN_FILE =
		"Because of some error the turnaournd file was not returned.";
	private static final int NUM_TURN_AROUND_FILES_SAVED = 7;
	private static final String POS_TRANS_ALREADY_CREATED =
		"POS transaction already created for traceno ";
	private static final String PROCESSING_TRACE_NO =
		"Processing traceno ";
	private static final String RECORD_TYPE_HEADER = "HD";
	private static final String SENDING_ERROR_EMAIL =
		"Sending email for error tracenos.";
	private static final String START_OF = "Start of ";
	private static final String SUCCESSFULL_LOGIN =
		"Successful login to the FTP server.";
	private static final int TRACE_NO_END = 29;
	private static final int TRACE_NO_START = 14;
	private static final String TURN_FILE_NOT_THERE =
		"Turnaround file does not exist on the FTP server.";
	private static final String TURN_FILE_PROCESSING =
		"turnaround file transaction processing.";
		
	// defect 9448
	private static String csPAS1File = "";
	// end defect 9448	

	/**
	 * Main method used for testing.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{

		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		
		// defect 9448
		// defect 9801
		if (aarrArgs.length > 0
			&& aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
//		if (aarrArgs.length > 0 && aarrArgs[0] != null)
		// end defect 9801
		{
			// defect 10391
			// BatchLog.write(" *** Processing PAS1 file: " + aarrArgs[0]);
			BatchLog.write(" *** Processing local file: " + aarrArgs[0]);
			// end defect 10391
			csPAS1File = aarrArgs[0];			
		}
		// defect 10391
		else
		{
			BatchLog.write(
				"Expecting to process a Comptrollers file");					
		}
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

//		BatchLog.write(
//				START_OF
//				+ BATCH_TURN_PROCESSING);
//		System.out.println(
//			"["
//				+ new java.util.Date()
//				+ "] "
//				+ START_OF
//				+ BATCH_TURN_PROCESSING);
		// end defect 9448
		System.out.println(System.getProperties());

		BatchTurnaroundProcessor laBTP = new BatchTurnaroundProcessor();
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
		}
		finally
		{
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
		}
	}

	private FtpFile caFTPFile = null;
	private Vector cvSpTransactions = new Vector();

	/**
	 * Main method that does all of the turn around file processing.
	 * This method will do the following:
	 * 
	 * 1. Load the FTP properties that are stored in a separate file.
	 * 2. Do an FTP get to retrieve the Turn Around file from the cpa.
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

		// defect 9448
		String lsTurnFileName = "";
		// when csPAS1File is blank, nothing was passed to
		// the this process, so get the Turnaround file from
		// the CPA's ftp server, else get the file locally
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
		// end defect 9448
		// Use line below to test with static file.
		// String lsTurnFileName = "D:\\PPLEXT000.DAT";
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
			BatchTurnaroundProcessor
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
					+ CPA_PROPS_FILE);
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
			// When running in WSAD, we are reading the cpa.properties
			// file in D:\Program Files\IBM\WSAD.v5.1.2\runtimes\
			// base_v51\properties\cfg (backslashes)

			// defect 9119
			// When running in TS1 (or PAS1), we are reading the 
			// cpa.properties file in /rts/POS2/usr/WebSphere5/
			// AppServer/lib/cfg (forward slashes)
			// Use File.separator to determine which character is the
			// file separator, according to the file system
			int liFileSepLoc =
				laFile.getAbsolutePath().lastIndexOf(File.separator);
			// if liFileSepLoc is still less than zero,
			// laParentFolder will not be able to be determined
			// so the file name we are trying to retrieve will not
			// be able to be retrieved from the cpa.properties file
			// so write a message 
			if (liFileSepLoc < 0)
			{
				System.out.println(
					"Slash or backslash not found in "
						+ laFile.getAbsolutePath());
			}
			// end defect 9119
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
			
			// defect 10515
			//laFTPClient.enterLocalActiveMode();
			laFTPClient.enterLocalPassiveMode();
			// end defect 10515

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
		// defect 9119
		String lsClassPath = System.getProperty("java.class.path");
		String lsFileSeparator = System.getProperty("file.separator");
		String lsPathSeparator = System.getProperty("path.separator");
		String lsCurrentCPEntry = "";
		// the following statement is for desktop testing 
		// need to add to the classpath for the cpa.properties file
		// to be found when desktop testing
//		 lsClassPath = lsClassPath + ";" + 
//			"D:\\ClearCaseViews\\rlbrown_Special_Plates_dev_2\\" +
//				"rtspos_cvob\\pos\\rtsposproject\\JavaSource\\";
		StringTokenizer lsClassPaths = 
			new StringTokenizer(lsClassPath, lsPathSeparator);			
		File laCPAProp = null;			
		try
		{
			while (lsClassPaths.hasMoreTokens())
			{
				lsCurrentCPEntry = lsClassPaths.nextToken();
				laCPAProp = 
					new File(lsCurrentCPEntry  + 
						lsFileSeparator + 
						CPA_PROPS_FILE);
				System.out.println("Classpath entry = " + 
					lsCurrentCPEntry);
				 if (laCPAProp.canRead())
				 {
					 laFileServer = new FileInputStream(laCPAProp);
					 csPropLoc = laCPAProp.getAbsolutePath();
					 System.out.println("Using file: " + csPropLoc);
					 break;
				 }
			 // below was a possible file finder implementation
			 // URL laURL = 
			 // findResource(lsURLs.nextToken()+ CPA_PROPS_FILE);
			 // larrURL[i] = new URL(lsURLs.nextToken());
			 // i++;				
			}
			if (!laCPAProp.canRead())
			{
				System.err.println(CPA_PROPS_FILE + " File not found");
				throw new FileNotFoundException(CPA_PROPS_FILE + 
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
//			try
//			{
//				// Lookup the resource URL
//				URL laURL =
//					BatchTurnaroundProcessor
//						.class
//						.getClassLoader()
//						.getResource(
//						CommonConstant.STR_SPACE_EMPTY);
//				// Decode the URL and create a file object for the 
//				// new property file.
//				if (laURL != null)
//				{
//					File laNewFile =
//						new File(
//							URLDecoder.decode(
//								laURL.getPath(),
//								ENCODETYPE).substring(
//								1)
//								+ CPA_PROPS_FILE);
//					// If the file does not exist then it will throw
//					// a FileNotFoundException then we will create it.
//					laFileServer = new FileInputStream(laNewFile);
//					csPropLoc = laNewFile.getAbsolutePath();
//					System.out.println("Using file: " + csPropLoc);
//				}
//				else
//				{
//					throw new FileNotFoundException();
//				}
//			}
//			catch (FileNotFoundException aeFNFEx)
//			{
//				System.out.println("Creating new properties file.");
//				laFileServer = createNewFTPPropFile();
//			}
//
//			// end defect 8553
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

	// end defect 9119

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
				// defect 10391
				if (cbTurnAroundFileFormat)
				{
				// end defect 10391	
					if (lsStrLine.startsWith(RECORD_TYPE_HEADER)
						&& lsStrLine.indexOf(CommonConstants.APPLICATION_CODE)
							== APPLICATION_CODE_START)
					{
						cvSpTransactions.add(
							lsStrLine
								.substring(TRACE_NO_START, TRACE_NO_END)
								.trim());
					}
				// defect 10391	
				}
				else
				{
					cvSpTransactions.add(lsStrLine.trim());
				}	
				// end defect 10391
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
			// defect 9448
//			BatchLog.write(String.valueOf(avErrorTraceNos.get(i)));
//			System.out.println(avErrorTraceNos.get(i));		
//			lsTraceNos += avErrorTraceNos.get(i)
//				+ CommonConstant.SYSTEM_LINE_SEPARATOR;
			lsTraceNos += avErrorTraceNos.get(i)
				+ "<br>";
//			end defect 9448	
		}
		

		BatchLog.write(SENDING_ERROR_EMAIL);
		System.out.println(SENDING_ERROR_EMAIL);

		// Send email
		// defect 10262
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
		// end defect 10262

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
		// defect 9119
		String lsRegPltNo = "";
		String lsMfgPltNo = "";
		String lsTransStatus = "";
		// end defect 9119
		DatabaseAccess laDBAccess = new DatabaseAccess();

		// Loop through trace numbers that were in turn file.
		for (int i = 0; i < cvSpTransactions.size(); i++)
		{
			// defect 9119
			lsRegPltNo = "";
			lsMfgPltNo = "";
			lsTransStatus = "";
			// end defect 9119
			String lsTranceNo = String.valueOf(cvSpTransactions.get(i));
			BatchLog.write(PROCESSING_TRACE_NO + lsTranceNo);
			System.out.println(PROCESSING_TRACE_NO + lsTranceNo);

			try
			{
				laDBAccess.beginTransaction();
				InternetSpecialPlateTransaction laSPTransAccess =
					new InternetSpecialPlateTransaction(laDBAccess);
				// Might be needed as some other time.
				//String lsPltNo = getPlateNo(lsTranceNo);

				// Get the internet transaction records for the given
				// Trace No.
				Vector lvRecords =
					laSPTransAccess.qryTransactionRecord(lsTranceNo);
				// End Trans so that table is not on hold when we update
				// the internet trans to completed.
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);

				// defect 9119
				boolean lbTransCreated = true;
				// end defect 9119
				
				// defect 9448
				String lsMessage = "";
				// end defect 9448
				if (lvRecords.size() > 0)
				{
					for (int j = 0; j < lvRecords.size(); j++)
					{
						SpecialPlateItrntTransData laSPTransData =
							(SpecialPlateItrntTransData) lvRecords.get(
								j);
						// defect 9119
						lsRegPltNo = laSPTransData.getRegPltNo();
						lsMfgPltNo = laSPTransData.getMfgPltNo();
						lsTransStatus = laSPTransData.getTransStatusCd();	
						if (laSPTransData
							.getTransStatusCd()
							.equals(
								CommonConstants.TRANS_STATUS_PENDING))
						// defect 9448
						// under normal circumstances,
						// the active and inactive trans status records 
						// will have a trace no different than the
						// ones in the Turnaround file (pending on our
						// database) so comment out the below code. 
						// write an error when an active or inactive 
						// database record matches a turnaround record
						// 	
//							|| laSPTransData
//								.getTransStatusCd()
//								.equals(
//									CommonConstants.TRANS_STATUS_ACTIVE)
//									|| laSPTransData
//										.getTransStatusCd()
//										.equals(
//										CommonConstants
//											.TRANS_STATUS_INACTIVE))
						// end defect 9448
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
						// end defect 9119	
							// Create POS trans and finalize Inventory.
							CommonServerBusiness laCommServerBuss =
									new CommonServerBusiness();
							// defect 9448		
							try
							{
								laCommServerBuss.processData(
									GeneralConstant.COMMON,
									CommonConstant.PROC_IAPPL,
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
							}
							// end defect 9448

							// sleep one second between trans to avoid
							// duplicate key inserts into rts_trans	
							// defect 9119
							try
							{
								Thread.sleep(1000); 
							}
							catch (InterruptedException aeIEx)
							{ // we will not take any action on this exception
							} // end defect 9119
							// defect 9119
							// lbTransCreated = true;
							// don't break from loop - keep processing
							// other records for this trace number
							// break;
							// end defect 9119
						}
						else if (
							laSPTransData.getTransStatusCd().equals(
								CommonConstants
									.TRANS_STATUS_COMPLETED))
						{
							// defect 9119
							BatchLog.write(
							POS_TRANS_ALREADY_CREATED
								+ lsTranceNo
								+ " "
								+ "Reg Plate No = "
								+ lsRegPltNo
								+ " "
								+ "Mfg Plate No = "
								+ lsMfgPltNo);
							// lbTransCreated = true;
							// break;
							// end defect 9119
						}
						else
						{
							lbTransCreated = false;
						// defect 9119
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
						// defect 9448
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
						// end defect 9448					
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
					// defect 9448	
					lsMessage = "<br>" + "Transaction not found on " 
							+ "rts_itrnt_spapp_trans for trace no: " 
							+ lsTranceNo;
					// end defect 9448				
				// end defect 9119			
				}

				if (!lbTransCreated)
				{
					// defect 9448
					lvErrorTrans.add(lsMessage);
//						lsTranceNo
//							+ CommonConstant.STR_SPACE_ONE
//							+ NO_RECORDS_FOR_TRACE_NO);
//						BatchLog.write(
//							lsTranceNo
//								+ CommonConstant.STR_SPACE_ONE
//								+ NO_RECORDS_FOR_TRACE_NO);
					// end defect 9448			
				}
			}
			catch (RTSException aeRTSEx)
			{
				// defect 9119
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
				// end defect 9119				
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
			} /**
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