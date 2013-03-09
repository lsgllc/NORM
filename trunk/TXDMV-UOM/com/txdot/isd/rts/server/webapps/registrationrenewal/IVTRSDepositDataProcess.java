package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetDepositReconHstry;
import com.txdot.isd.rts.server.db.InternetDepositReconTmp;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.data.InternetDepositReconData;
import com.txdot.isd.rts.services.data.InternetDepositReconHstryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.RegRenProcessingConstants;

/*
 * IVTRSDepositDataProcess.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		02/06/2009	Created Class.
 * 							defect 9936 Ver Defect_POS_D
 * B Brown		03/17/2009	Grab all csv files in the remote deposit 
 * 							directory, store them on the POS server, 
 * 							process them, then move them to the archive
 * 							directory on the remote server via a rename
 * 							command.
 * 							modify getDepositFile(),loadDepositFile(),
 * 								processDepositFile(), main(), 
 * 								retrieveFile().	
 * 							defect 9986 Ver Defect_POS_E
 * B Brown		03/20/2009	Add Final String variables, change static 
 * 							variables to class variables where 
 * 							appropriate, remove unused variables, and a
 *							method parm. Use constants for error 
 *							messages, pulled internal class 
 * 							FTPFile out and created a new class.
 * 							Move preocessing out of main()
 * 							add beginProcessing(), insertIntoHstry(),
 * 								moveRemoteFiles()
 * 							modify all other methods.
 *  						defect 9986 Ver Defect_POS_E
 * B Brown		03/25/2009	Add code for retrying FTP connection after
 * 							insert. Ensure most recent file date is 
 * 							put in history when processing multiple
 * 							files.
 * 							modify beginProcessing(), 
 * 								processDepositFile()
 *  						defect 9986 Ver Defect_POS_E
 * B Brown		10/27/2009	Process multiple deposit dates
 * 							add	   loadReconTmpTable()
 * 							modify beginProcessing(), 
 * 								   processDepositFile(),
 * 								   updateDepositReconHstry()  
 * 							defect 10111 Ver Defect_POS_G
 * B Brown		01/08/2010	Add better error message procesing 
 * 							add 2 more edits: make sure dep_rec_data_ is
 * 							in the file name to be processed, and make
 * 							sure the deposit dates for each file row
 * 							equal the date in the file name.
 * 							add ERROR_NUM_NOT_FOUND      
 *								FILE_BEING_PROCESSED     
 *								DEP_REC_DATA 
 *								DATE_IN_FILE_NAME 
 *								UNDERSCORE          
 *								COULD_NOT_LOAD_STATIC_CACHE
 *							add createMsg()
 * 							modify beginProcessing(),
 * 								   connectAndLogin(),
 * 								   createNewFTPPropFile()	 								   
 *								   getDepositFile()
 *								   insertIntoHstry()
 *								   loadDepositFile()
 *								   loadFTPProperties()
 *								   logError()
 *								   main()
 *								   moveRemoteFiles()
 *								   processDepositFile()
 *								   loadReconTmpTable()
 *								   retrieveFile()
 *								   sendEmailErrorMsg()
 *								   updateDepositReconHstry()
 * 							defect 10262 Ver Defect_POS_H
 * B. Brown		06/16/2010	Change to local passive mode for FTP'ing
 * 							modify connectAndLogin()
 * 							defect 10515 Ver POS_640
 * ---------------------------------------------------------------------
 */

/**
 * Batch process that will handle an FTP get to the TxO FTP server
 * to get the Deposit file.  The Deposit file contains IRENEW 
 * transactions, with each record seperated by a comma. 
 * 
 * <p>Much like the BachTurnaround process, this process will be an 
 * application client residing on PAS1 and started via Unix crontab.
 *
 * @version	POS_640			06/16/2010
 * @author	Bob Brown
 * <br>Creation Date:		02/06/2009 10:40:00
 */
public class IVTRSDepositDataProcess
{
	// defect 10262
	//private static IVTRSDepositDataProcess saIDDP; 
	//	new IVTRSDepositDataProcess();
	// end defect 10262
	private static final String PROCESSING_LOCAL_FTP_FILE =
		"*** Processing Local FTP file: ";
	private static String ssDepositFile = "";	
	
	// defect 9986
	private final String ARCHIVE_DIR = "archive/";
	private RTSDate caBankDepositDate;

	private DatabaseAccess caDBAccess = null;
	private FTPClient caFTPClient = null;
	// end defect 9986	
	
	private FtpFile caFTPFile = null;

	private InternetDepositReconHstry caIVTRSDepositReconHstryAccess =
		null;
	private InternetDepositReconTmp caIVTRSDepositReconTmpAccess =
		null;
	
	private RTSDate caProcsComplTimeStmp;
	private String[] carrFiles = null;
	private RTSDate caTmpInsTimeStmp;
	private int ciErrMsgNo;
//	private int ciNumOfRecsRead = 0;
		
	private int ciReqId = 0;	
	private int ciSuccessIndi;
	private int ciTransCnt;
	private final String CLASSPATH_ENTRY = "Classpath entry = ";
	private final String COMMA = ",";
	private final String CREATING_NEW_PROPS_FILE =
		"Creating new deposit properties file.";
	private String csPropLoc = null;
	private final String CSV_FILE_TYPE = ".csv";
	private Vector cvDepositReconTmp = new Vector(); 
	
	private final String DASHES = "====================";
	// Default FTP properties.
	private final String DEFAULT_FTP_FILE =
		"'/deposit/dep_rec_data_'";
	private final String DEFAULT_FTP_HOST = "204.66.40.185";
	private final String DEFAULT_FTP_LOCAL_FILE = "deposit.csv";
	private final String DEFAULT_FTP_PASS = "123abc";
	private final int DEFAULT_FTP_PORT = 21;
	// don't know if we will be saving the deposit file locally	
	private final String DEFAULT_FTP_USER = "bbrown";
	private final String DEPOSIT_DIR = "/deposit";
	private final String DEPOSIT_FILE_MISSING_OR_BLANK =
		"Deposit file is missing or blank";
	private final String DEPOSIT_FILE_PROCESSING =
		"Deposit File Processing.";
	private final String DEPOSIT_PROPS_FILE =
		"cfg/deposit_file.properties";
	private final String EMAIL_BODY =
		"The deposit file reconciliation process failed and "
			+ "requires attention. <br> Batchlog.log also contains "
			+ "error details.";
	private final String EMAIL_FROM =
		"TSD-RTS-POS@dot.state.tx.us";
	private String csEmailSubject =
		"Deposit File Processing error";
	// if EMAIL_To = "RTS-IVTRS@dot.state.tx.us";, then EMAIL_SUBJECT 
	// must contain RTS for the Groupwise forwarding rule to work 
	// (forwards to TSD-RTS-POS) 
	private final String EMAIL_TO =
		"TSD-RTS-POS@dot.state.tx.us";
	private final String EMPTY_STRING = "";	
	private final String ENCODETYPE = "UTF-8";
	private final String ENCODING_EXCEPTION =
		"UnsupportedEncodingException";
	private final String END_OF = "End of ";
	private final String ERROR_CODE = "Error code = ";
	private final String ERROR_DURING = "Error during ";
	private final String ERROR_HANDLER = "Error handler ";
	private final String ERROR_MESSAGE_PASSED =
		"Error Message passed in = ";
	private final String EXCEPTION_ERROR_RETR_DEP_FILE =
		"Exception Error retrieving deposit file";
	private final String FILE_NOT_FOUND = "File not found";
	private final String FILE_SEPARATOR = "file.separator";
	private final String FILE_TO_RETRIEVE =
		"File to retrieve = ";
	private final String FILENOTFOUNDEX =
		"FileNotFoundException";
	private final String FTP_ERROR_RETRIEVING =
		"FTP error retrieving deposit file";
	public final String FTP_FILE = "FTPFile";
	private final String FTP_FILE_TRANSFER =
		"FTP file transfer.";
	public final String FTP_HOST = "FTPHost";
	public final String FTP_LOCAL_FILE = "FTPLocalFile";
	public final String FTP_PASS = "FTPPass";
	private final String FTP_RETRIEVE = "FTP retrieve process.";
	public final String FTP_USER = "FTPUser";
	private final String HSTRY_ROLLBACK_ERROR =
		"Cound not do an end transaction rollback on the Recon Hstry" +
		" table update";
	private final String HSTRY_UPDATE_RESULT_ERROR =
		"Could not update temp Deposit Recon Hstry with final good " +
		"result";
	private final String HTML_NEW_LINE = " = <br>";
	private final String INSERT_INTO_HSTRY_ERROR =
		"Could not insert into DepositReconHstry";
	private final String INSERTING_INTO_HSTRY =
		"Inserting into DepositReconHstry, siReqId = ";
	private final String IOEXCEPTION = "IOException";
	private final String JAVA_CLASS_PATH = "java.class.path";
	private final String LOAD_DEPOSIT_FILE =
		"loading deposit file.";
	private final String LOADING_FTP_PROPS =
		"FTP Propertie loading....";
	private final String MOVE_REMOTE_FILE =
		"Trying to move files on the remote system = ";
	private final String ON_REMOTE_SYSTEM =
		"on remote system: FTP reply: ";
	private final String PATH_SEPARATOR = "path.separator";
	private final String PROCESS_EMAIL_ERROR =
		"Could not send deposit file recon process error email";
	//private final int NUM_TURN_AROUND_FILES_SAVED = 7;

	private final String PROD_DATASOURCE = "P0RTSDB";
	private final String PRODUCTION = "Production: ";
	private final String RECON_HSTRY_UPDATE_ERROR =
		"Could not update Deposit Recon Hstry";
	private final String RECORD_COUNT_UPDATE_ERROR =
		"Could not update temp Deposit Recon Hstry with record 	count";
	private final String RETRIEVING_FILE =
		"Trying to retrieve file = ";
	private final String SENDING_ERROR_EMAIL =
		"SENDING ERROR EMAIL";
	private final String START_OF = "Start of ";
	private final String STORE_FILE_ERROR =
		"Could not store file ";
	private final String SUCCESSFULL_LOGIN =
		"Successful login to the FTP server.";	
	private final String TEMP_DEP_FILE_DELETED =
		"Temp Deposit file rows deleted";
	private final String TEMP_FILE_ROWS_INSERTED =
		"Temp Deposit file Rows inserted = ";
	private final String TEMP_UPDATE_ERROR =
		"Could not update temp Deposit Recon Hstry with error message";
	private final String TEST = "Test: ";
	private final String TRACE_NUM_EXISTS_ON_PREV_FILE =
		"Trace number exists on previous deposit file";
	private final String USING_FILE = "Using file: ";
	private final String VR = "VR";
	// defect 10262
	private final String ERROR_NUM_NOT_FOUND =
		"Could not find the following error in ErrorMessagesCache: ";
	private final String FILE_BEING_PROCESSED =
		"File being processed = ";
	private final String DEP_REC_DATA = "dep_rec_data_";
	private final String DATE_IN_FILE_NAME = "Date In File Name = ";
	private final String UNDERSCORE = "_";
	private final String DOT = ".";
	private ErrorMessagesData caErrorMsgData = null;
	private final String COULD_NOT_LOAD_STATIC_CACHE =
		"Could not load static cache";
	// end defect 10262	

	/**
	 * Method used to control processing.
	 * 
	 */	
	private void beginProcessing()
	{
		// defect 10262
		try
		{
			// Load the Server Side Cache
			CacheManagerServerBusiness laCMSB =
				new CacheManagerServerBusiness();
			laCMSB.loadStaticCache();
		}
		catch (RTSException aeRTSEx)
		{
			// Error number 2230
			logError(
			aeRTSEx,
			COULD_NOT_LOAD_STATIC_CACHE);
			System.exit(1);
		}
		// end defect 10262
		try
		{
			// defect 10111
			// push the insertIntoHstry down to loop 
			// laIDDP.insertIntoHstry();
			// end defect 10111
			// defect 10262
			//laIDDP.processDepositFile();
			processDepositFile();
			// end defect 10262
			// only do an FTP connection and removal of remote
			// files if ssDepositFile = "" (local file was not 
			// passed in as a parm - we are processing a remote file) 
			if (ssDepositFile.equals(EMPTY_STRING))
			{
				// try 3 times to move the remote files
				for (int x=0;x<3;x++)
				{	
					try
					{
						// defect 10262
						// laIDDP.moveRemoteFiles();
						moveRemoteFiles();
						// end defect 10262
						break;	
					}
					catch (RTSException aeRTSEx)
					{
						throw aeRTSEx;
					}
					catch (IOException aeIOEx)
					{
						// defect 10262
						// email only for the last try, not the first
						// if (x == 0)
						if (x == 2)
						{
							throw createMsg(
								ErrorsConstant
									.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
								aeIOEx.getMessage());
//							try
//							{
//								sendEmailErrorMsg(
//								// defect 10262
//								// IOEx.getMessage()
//								leIOEx.getMessage()
//								// end defect 10262
//										+ HTML_NEW_LINE
//										+ MOVE_FILE_ERROR
//										+ HTML_NEW_LINE);
//							}
//
//							catch (RTSException leRTSEx)
//							{
//								leRTSEx.printStackTrace();
//								BatchLog.error(PROCESS_EMAIL_ERROR);
//								BatchLog.error(leRTSEx.getMessage());
//								BatchLog.error(leRTSEx.getDetailMsg());
//							}
						}
						// connectAndLogin();
						// end defect 10262
					}
				}
			}
		}
			
		catch (RTSException aeRTSEx)
		{
			// defect 10262
			// logError(aeRTSEx1, EMPTY_STRING);
			logError(aeRTSEx, EMPTY_STRING);
			// end defect 10262
		}
		finally
		{
			// defect 9986
			if (caFTPClient != null && caFTPClient.isConnected())
			{
				try
				{
					caFTPClient.disconnect();
				}
				catch(IOException aeIOEx)
				{
					// defect 10262
					// leIOE.printStackTrace();
					aeIOEx.printStackTrace();
					// end defect 10262
				}						
			}								
			// end defect 9986
			BatchLog.write(END_OF + DEPOSIT_FILE_PROCESSING);
			BatchLog.write(DASHES);
		}
	}
	
	private void connectAndLogin() throws RTSException
	{
		// defect 10262
		// RTSException leRTSEx = new RTSException();
		// end defect 10262
		try
		{
			caFTPClient = new FTPClient();

			caFTPClient.connect(
				caFTPFile.getFtpHost(),
				DEFAULT_FTP_PORT);

			if (!FTPReply
				.isPositiveCompletion(caFTPClient.getReplyCode()))
			{
//				leRTSEx.setMessage(FTP_CONNECTION_FAILED
//						+ caFTPClient.getReplyString());
//				throw leRTSEx;
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_CONNECTION_FAILED,
					EMPTY_STRING);
			}

			if (!caFTPClient
				.login(
					caFTPFile.getFtpUser(),
					caFTPFile.getFtpPassword()))
			{
				// defect 10262
//				leRTSEx.setMessage(FTP_FAILED_LOGIN);
//				throw leRTSEx;
				throw createMsg(ErrorsConstant.ERR_NUM_FTP_FAILED_LOGIN,
								EMPTY_STRING);
				// end defect 10262
			}

			BatchLog.write(SUCCESSFULL_LOGIN);
			
			caFTPClient.setFileType(
				org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE);

			if (!FTPReply
				.isPositiveCompletion(caFTPClient.getReplyCode()))
			{
				// defect 10262
//				leRTSEx.setMessage(FAILED_TRANSFER_TYPE
//					+ caFTPClient.getReplyString());
//				throw leRTSEx;
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_FAILED_TRANSFER_TYPE,
					caFTPClient.getReplyString());
				// end defect 10262				
			}
			// defect 10515
			//caFTPClient.enterLocalActiveMode();
			caFTPClient.enterLocalPassiveMode();
			// end defect 10515

			if (!FTPReply
				.isPositiveCompletion(caFTPClient.getReplyCode()))
			{
				// defect 10262
//				leRTSEx.setMessage(FAILED_ACTIVE_MODE
//					+ caFTPClient.getReplyString());
//				throw leRTSEx;
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_FAILED_ACTIVE_MODE,
					caFTPClient.getReplyString());
				// end defect 10262				
			}
			// defect 9986
			// not using the FTP property saFTPClient.getFtpFile()
			caFTPClient.changeWorkingDirectory(DEPOSIT_DIR);
			// end defect 9986
		}
		catch (IOException aeIOEx)
		{
//			leRTSEx.setCode(FILE_RETRIEVAL_ERROR);
//			leRTSEx.setMessage(leIOEx.getMessage());
//			leRTSEx.setDetailMsg(
//				IOEXCEPTION_FTPING);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeIOEx.getMessage());
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10262
//			aeRTSExc.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			aeRTSExc.setDetailMsg(FTP_ERROR_RETRIEVING);
//			throw aeRTSExc;
//			leRTSEx1.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
			aeRTSEx.setDetailMsg(FTP_ERROR_RETRIEVING);
			throw aeRTSEx;
			// end defect 10262
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
		File laNewFile = null;
		try
		{
			URL laURL =
				IVTRSDepositDataProcess
					.class
					.getClassLoader()
					.getResource(
					CommonConstant.STR_SPACE_EMPTY);
			// Decode the URL and create a file object for the new 
			// property
			// file.
			laNewFile =
				new File(
					URLDecoder.decode(
						laURL.getPath(),
						ENCODETYPE).substring(
						1)
						+ DEPOSIT_PROPS_FILE);
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
			OutputStream laFileServerOut =
				new FileOutputStream(laNewFile);
			// Save the properties.
			laServerPrp.store(
				laFileServerOut,
				CommonConstant.STR_SPACE_EMPTY);
			laFileServerOut.flush();
			// Close the output stream.
			laFileServerOut.close();
		}
		// defect 10262
//		catch (UnsupportedEncodingException e)
//		{
//			throw new Exception(ENCODING_EXCEPTION);
//		}
//		catch (FileNotFoundException e)
//		{
//			throw new Exception(FILENOTFOUNDEX);
//		}
//		catch (IOException e)
//		{
//			throw new Exception(IOEXCEPTION);
//		}
		catch (UnsupportedEncodingException aeUEEx)
		{
			throw new Exception(ENCODING_EXCEPTION);
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new Exception(FILENOTFOUNDEX);
		}
		catch (IOException aeIOEx)
		{
			throw new Exception(IOEXCEPTION);
		}
		// end defect 10262
		csPropLoc = laNewFile.getAbsolutePath();
		// Return the input stream to the new property file.
		return new FileInputStream(laNewFile);
	}

	/**
	 * Method uses organizations.apache.commons.net.ftp.FTPClient to go to the
	 * comptroler to retrieve the Deposit file and save the file.
	 * 
	 * We will be saving iterations of this file for debug purposes.
	 * If the number of days that we keep a file needs to be increased
	 * we just increase the NUM_TURN_AROUND_FILES_SAVED value.
	 * 
	 * @return String
	 * @throws RTSException
	 */
	private Vector getDepositFile() throws RTSException
	{
		// defect 9986
		// String lsReDepositFile = null;
		String lsDepositFilePath = null;
		Vector lvDepositFilePath = new Vector();
		// defect 10262
		// RTSException leRTSEx = new RTSException();
		// end defect 10262
		// end defect 9986
		
		try
		{
			// defect 9986
			connectAndLogin();
			
			carrFiles =	caFTPClient.listNames();
			if ((carrFiles != null) && (carrFiles.length != 0))
			{
				for (int liFileCnt = 0;
					liFileCnt < carrFiles.length;
					liFileCnt++)
				{
					if (!carrFiles[liFileCnt].endsWith(CSV_FILE_TYPE))
					{
						continue;
					}
					BatchLog.write(
						RETRIEVING_FILE
							+ carrFiles[liFileCnt]);
					lsDepositFilePath =
						retrieveFile(
							caFTPClient,
						carrFiles[liFileCnt]);
					lvDepositFilePath.add(lsDepositFilePath);
				}
			}
			else
			{
				// defect 9986
//				saFTPClient.disconnect();
				// defect 10262
//				leRTSEx.setMessage(DEPOSIT_FILE_NOT_THERE);
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEPOSIT_FILE_MISSING,
					EMPTY_STRING);
				// throw leRTSEx;		
				// end defect 10262
				// end defect 9986
			}

		}
		catch (NoClassDefFoundError aeNCDFEEx)
		{
			// defect 10262
//			leRTSEx.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			// defect 10262	
//			// leRTSEx.setMessage(aeNCDFE.getMessage());
//			leRTSEx.setMessage(leNCDFEEx.getMessage());
//			// end defect 10262
//			leRTSEx.setDetailMsg(
//				NOCLASSDEFFOUND_ERROR);
//			throw leRTSEx;
			// end defect 10262
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeNCDFEEx.getMessage());
		}
		catch (SocketException aeSEx)
		{
			// defect 10262
//			leRTSEx.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			// leRTSEx.setMessage(aeSExc.getMessage());
//			leRTSEx.setMessage(leSEx.getMessage());
//			leRTSEx.setDetailMsg(
//				SOCKET_EXCEPTION);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeSEx.getMessage());
		}
		catch (IOException aeIOEx)
		{
			// defect 10262
//			leRTSEx.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			// leRTSEx.setMessage(aeIOExc.getMessage());
//			leRTSEx.setMessage(leIOEx.getMessage());
//			leRTSEx.setDetailMsg(
//				IOEXCEPTION_FTPING);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeIOEx.getMessage());
			// end defect 10262
		}
		catch (RTSException aeRTSEx1)
		{
			// defect 10262
//			aeRTSExc.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			aeRTSExc.setDetailMsg(FTP_ERROR_RETRIEVING);
//			throw aeRTSExc;
			throw aeRTSEx1;
			// end defect 10262
		}
		catch (Exception aeEx)
		{
//			leRTSEx.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			// leRTSEx.setMessage(aeExc.getMessage());
//			leRTSEx.setMessage(leEx.getMessage());
//			leRTSEx.setDetailMsg(EXCEPTION_ERROR_RETRIEVING);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeEx.getMessage());
		}
		// defect 9986
		// return lsReDepositFile;
		return lvDepositFilePath;
		// end defect 9986
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
	 * Used for inserting into rts_deposit_recon_hstry.
	 * 
	 */
	private void insertIntoHstry()
	{
		try
		{
			BatchLog.write(DASHES);
			// defect 10262
			// caDBAccess = new DatabaseAccess();
			// end defect 10262
			caDBAccess.beginTransaction();
			caIVTRSDepositReconHstryAccess =
				new InternetDepositReconHstry(caDBAccess);
			ciReqId =
				caIVTRSDepositReconHstryAccess
					.insInternetDepositReconHstry();
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			BatchLog.write(
				INSERTING_INTO_HSTRY
					+ ciReqId);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10262
//			leRTSEx1.setCode(
//				ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR);
//			logError(
//				leRTSEx1,
//				INSERT_INTO_HSTRY_ERROR);
			if (aeRTSEx.getCode() == 0)
			{
				aeRTSEx.setCode(
					ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR);
			}
			logError(
				aeRTSEx,
				INSERT_INTO_HSTRY_ERROR);
			// end defect 10262	

			try
			{
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx1)
			{
				// defect 10262
				// e.printStackTrace();
				aeRTSEx1.printStackTrace();
				// end defect 10262
			}				
			System.exit(1);
		}
	}

	/**
	 * Loads the Deposit file and puts the Special Plate Transactions
	 * into a vector.
	 * 
	 * @param asFile String
	 * @throws RTSException
	 */
	private Vector loadDepositFile(String asFile) throws RTSException
	{
		BatchLog.write(START_OF + LOAD_DEPOSIT_FILE);
		String lsStrLine = EMPTY_STRING;
		// defect 10111
		int liNumOfRecsRead = 0;
		// end defect 10111
		try
		{
			// defect 10262
			// System.out.println("File being processed = " + asFile);
			BatchLog.write(FILE_BEING_PROCESSED + asFile);
			// end defect 10262
			FileInputStream lpfstream = new FileInputStream(asFile);
			// check for empty file
			if(lpfstream.available()==0)
			{
				// defect 10262
//				throw new Exception(
//					EMPTY_DEPOSIT_FILE);
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEPOSIT_FILE_EMPTY,
					EMPTY_STRING);
				// end defect 10262	
			}
			DataInputStream laIn = new DataInputStream(lpfstream);
			BufferedReader laBr =
				new BufferedReader(new InputStreamReader(laIn));
			String lsCurrentColumnData = EMPTY_STRING;
			int liDataposition = 0;
			int liCountyNo = 0;
			StringTokenizer lsDepositData = null;
			InternetDepositReconData laDepositReconData = null;
			
			// defect 10262
			int liDepRecData = asFile.indexOf(DEP_REC_DATA);
			if (liDepRecData < 0)
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEP_REC_DATA_NOT_IN_NAME,
					EMPTY_STRING);
			}
			String lsDateInFileName = asFile.substring(liDepRecData);
			lsDateInFileName =
				lsDateInFileName.replaceFirst(
					DEP_REC_DATA,
					EMPTY_STRING);
			lsDateInFileName =
				lsDateInFileName.substring(
					0,
					lsDateInFileName.indexOf(DOT));
			BatchLog.write(DATE_IN_FILE_NAME + lsDateInFileName);
			// end defect 10262
			
			while ((lsStrLine = laBr.readLine()) != null)
			{
				if (lsStrLine.equals(EMPTY_STRING))
				{
					BatchLog.write(
						"Current record is blank , previous record "
							+ "information below:");
					InternetDepositReconData laIDRD =
						(
							InternetDepositReconData) cvDepositReconTmp
								.get(
							liNumOfRecsRead - 1);
					BatchLog.write(
						"BankDepositDate = "
							+ laIDRD.getBankDepositDate());
					BatchLog.write(
						"ItrntTraceNo = " + laIDRD.getItrntTraceNo());
					BatchLog.write(
						"Last4PymntCardNo = "
							+ laIDRD.getLast4PymntCardNo());
					BatchLog.write(
						"OfcIssuanceNo = " + laIDRD.getOfcIssuanceNo());
					BatchLog.write(
						"PymntCardType = " + laIDRD.getPymntCardType());
					BatchLog.write(
						"PymntAmt = " + laIDRD.getPymntAmt());
					BatchLog.write(
						"TOLTransDate = " + laIDRD.getTOLTransDate());
					BatchLog.write(
						"blank row, previous row = "
							+ cvDepositReconTmp.get(liNumOfRecsRead - 1));
					continue;		
				}
				liDataposition = 0;
				liCountyNo = 0;

				lsDepositData = new StringTokenizer(lsStrLine, COMMA);
				laDepositReconData = new InternetDepositReconData();

				while (lsDepositData.hasMoreTokens())
				{
					lsCurrentColumnData = lsDepositData.nextToken();
					liDataposition = liDataposition + 1;
					switch (liDataposition)
					{
						case 1 :
							// Global deposit date	
							try
							{
								// defect 10262
								if (!lsDateInFileName
									.equals(lsCurrentColumnData))
								{
									throw createMsg(
										ErrorsConstant
											.ERR_NUM_FILE_NAME_DATE_NOT_EQUAL_DEP_DATE,
										EMPTY_STRING);
								}
								// end defect 10262
								RTSDate laRTSDate =
									new RTSDate(
										RTSDate.YYYYMMDD,
										Integer.parseInt(
											lsCurrentColumnData));
								laDepositReconData.setBankDepositDate(
									laRTSDate);
								continue;
							}
							// defect 10262
							catch (RTSException aeRTSEx1)
							{
								throw aeRTSEx1;
							}
							// end defect 10262
							catch (Exception aeEx)
							{
								// defect 10262
//								throw new Exception(
//									YYYYMMDD_RTS_DATE_ERROR
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_DATE_NOT_YYYYMMDD,
										lsStrLine);		
								// end defect 10262
							}
						case 2 :
							// county number
							try
							{
								liCountyNo =
									Integer.parseInt(
										lsCurrentColumnData);
							}
							catch (Exception aeEx)
							{
								// defect 10262
//								throw new Exception(
//									COUNTY_NUMBER_ERROR
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_COUNTY_NUMBER_ERROR,
										lsStrLine);		
								// end defect 10262		
							}
							laDepositReconData.setOfcIssuanceNo(
								Integer.parseInt(lsCurrentColumnData));
							continue;
						case 3 :
							// credit card type
							if (lsCurrentColumnData
								.equals(
									RegRenProcessingConstants
										.VISA_ABBR)
								|| lsCurrentColumnData.equals(
									RegRenProcessingConstants
										.MASTERCARD_ABBR)
								|| lsCurrentColumnData.equals(
									RegRenProcessingConstants
										.AMERICANEXPRESS_ABBR)
								|| lsCurrentColumnData.equals(
									RegRenProcessingConstants
										.DISCOVER_ABBR))
							{
								laDepositReconData.setPymntCardType(
									lsCurrentColumnData);
								continue;
							}
							else
							{
								// defect 10262
//								throw new Exception(
//									INVALID_CREDIT_CARD_TYPE
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_INVALID_CREDIT_CARD_TYPE,
									lsStrLine);	
								// end defect 10262		
							}
						case 4 :
							// trace number
							int liVRLoc =
								lsCurrentColumnData.indexOf(VR);
							if (liCountyNo
								!= Integer.parseInt(
									lsCurrentColumnData.substring(
										0,
										liVRLoc))
								|| liVRLoc == 0
								|| lsCurrentColumnData.length() < 9
								|| lsCurrentColumnData.length() > 17)
							{
								// defect 10262	
//								throw new Exception(
//									INVALID_TRACE_NUMBER
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_INVALID_TRACE_NUMBER,
									lsStrLine);	
								// end defect 10262			
							}
							laDepositReconData.setItrntTraceNo(
								lsCurrentColumnData);
							continue;
						case 5 :
							// credit card last 4
							// comment out till they are 4 on the file	
							if (lsCurrentColumnData.length() < 4)
							{
								// defect 10262	
//								throw new Exception(
//									LAST_4_CREDIT_CARD_NOT_4
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_LAST_4_CREDIT_CARD,
									lsStrLine);	
								// end defect 10262			
							}
							try
							{
								Integer.parseInt(lsCurrentColumnData);
							}
							catch (Exception aeEx)
							{
								// defect 10262	
//								throw new Exception(
//									LAST_4_CREDIT_CARD_NOT_NUM
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_LAST_4_CREDIT_CARD,
									lsStrLine);	
								// end defect 10262			
							}
							laDepositReconData.setLast4PymntCardNo(
								lsCurrentColumnData);
							continue;
						case 6 :
							// payment amount
							try
							{
								Double.parseDouble(lsCurrentColumnData);
							}
							catch (Exception aeEx)
							{
								// defect 10262	
//								throw new Exception(
//									INVALID_PAYMNT_AMT
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_DEPOSIT_INVALID_PAYMNT_AMT,
									lsStrLine);	
								// end defect 10262			
							}
							laDepositReconData.setPymntAmt(
								new Dollar(lsCurrentColumnData));
							continue;
						case 7 :
							// TOL date
							try
							{
								RTSDate laRTSDate =
									new RTSDate(
										RTSDate.YYYYMMDD,
										Integer.parseInt(
											lsCurrentColumnData));
								laDepositReconData.setTOLTransDate(
									laRTSDate);
								continue;
							}
							catch (Exception aeEx)
							{
								// defect 10262	
//								throw new Exception(
//									YYYYMMDD_RTS_DATE_ERROR
//										+ lsStrLine);
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_TOL_DATE_NOT_YYYYMMDD,
									lsStrLine);	
								// end defect 10262			
							}
						default :
							// defect 10262	
//							throw new Exception(
//								WRONG_NUM_FIELDS);
						throw createMsg(
							ErrorsConstant
								.ERR_NUM_DEPOSIT_WRONG_NUM_FIELDS,
							lsStrLine);	
						// end defect 10262		
					}
				}
				// defect 9986
				// lvDepositReconTmp.add(laDepositReconData);
				liNumOfRecsRead = liNumOfRecsRead + 1;
				cvDepositReconTmp.addElement(laDepositReconData);
				// end defect 9986
			}
			caBankDepositDate = laDepositReconData.getBankDepositDate();
			BatchLog.write(END_OF + LOAD_DEPOSIT_FILE);
			// defect 10111
			BatchLog.write(
				"number of recs for "
					+ caBankDepositDate
					+ " = "
					+ cvDepositReconTmp.size());
			// end defect 10111		
			// defect 9986
			// return lvDepositReconTmp;
			return cvDepositReconTmp;
			// end defect 9986
		}
		catch (IOException aeIOEx)
		{
//			RTSException leRTSEx = new RTSException();
//			leRTSEx.setCode(
//				ErrorsConstant
//					.ERR_NUM_DEPOSIT_FILE_VALUES_PARSING_ERROR);
//			leRTSEx.setMessage(leIOEx.getMessage());
//			leRTSEx.setDetailMsg(
//				IOEX_READING_DEP_FILE);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_VALUES_PARSING_ERROR,
				aeIOEx.getMessage());	
		}
		// defect 10262
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		// end defect 10262
//		catch (Exception leEx)
//		{
//			RTSException leRTSEx = new RTSException();
//			leRTSEx.setCode(
//				ErrorsConstant
//					.ERR_NUM_DEPOSIT_FILE_VALUES_PARSING_ERROR);
//			// defect 10262			
//			// leRTSEx.setDetailMsg(aeExc.getMessage());
//			leRTSEx.setDetailMsg(leEx.getMessage());
//			// end defect 10262
//			throw leRTSEx;
//		}
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
		String lsClassPath = System.getProperty(JAVA_CLASS_PATH);
		String lsFileSeparator = System.getProperty(FILE_SEPARATOR);
		String lsPathSeparator = System.getProperty(PATH_SEPARATOR);
		String lsCurrentCPEntry = EMPTY_STRING;
		// the following code is to find the deposit_file.properties
		// file. When running in WSAD, and getting the file locally, 
		// the file is in:
		// ..\WebContent\WEB-INF\classes\cfg\		
		File laTxOServerProp = null;
		try
		{
			StringTokenizer lsClassPaths =
				new StringTokenizer(lsClassPath, lsPathSeparator);
			while (lsClassPaths.hasMoreTokens())
			{
				lsCurrentCPEntry = lsClassPaths.nextToken();
				laTxOServerProp =
					new File(
						lsCurrentCPEntry
							+ lsFileSeparator
							+ DEPOSIT_PROPS_FILE);
				BatchLog.write(
					CLASSPATH_ENTRY + lsCurrentCPEntry);
				if (laTxOServerProp.canRead())
				{
					laFileServer = new FileInputStream(laTxOServerProp);
					csPropLoc = laTxOServerProp.getAbsolutePath();
					BatchLog.write(USING_FILE + csPropLoc);
					break;
				}
			}
			if (!laTxOServerProp.canRead())
			{
				// defect 10262
				BatchLog.write(DEPOSIT_PROPS_FILE + FILE_NOT_FOUND);
				// end defect 10262
				throw new FileNotFoundException(
					DEPOSIT_PROPS_FILE + FILE_NOT_FOUND);		
			}
		}
		catch (FileNotFoundException aeFNFEx)
		{
			BatchLog.write(CREATING_NEW_PROPS_FILE);
			try
			{
				laFileServer = createNewFTPPropFile();
			}
			catch (Exception aeEx)
			{
				// defect 10262
//				RTSException leRTSEx = new RTSException();	
//				leRTSEx.setCode(
//					ErrorsConstant.ERR_NUM_FTP_PROPERTIES_FILE_ERROR);
//				leRTSEx.setMessage(leEx.getMessage());
//				ceRTSEx.setDetailMsg(
//					COULD_NOT_CREATE_PROPS_FILE);
//				throw leRTSEx	
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_PROPERTIES_FILE_ERROR,
					aeEx.getMessage());		
				// end defect 10262
			}
		}

		catch (NoSuchElementException aeNSEEx)
		{
			// defect 10262
//			RTSException leRTSEx = new RTSException();
//			leRTSEx.setCode(
//				ErrorsConstant
//					.ERR_NUM_DEPOSIT_FILE_STRING_TOKENIZER_ERROR);
//			leRTSEx.setMessage(leNSEEx.getMessage());
//			throw leRTSEx	
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_STRING_TOKENIZER_ERROR,
				aeNSEEx.getMessage());		
			// end defect 10262
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
			// defect 9986
			// get all files in the remote deposit directory
			// the following code loading lsFTPFile and lsFTPLocalFile 
			// will be ignored!!!!
			String lsFileSuffix =
				(new RTSDate().add(RTSDate.DATE, -1)).getYYYYMMDDDate()
					+ CSV_FILE_TYPE;
			//		+ ".csv";		
			// end defect 9986 
			String lsFTPFile =
				laServerPrp.getProperty(FTP_FILE) + lsFileSuffix;
			BatchLog.write(FILE_TO_RETRIEVE + lsFTPFile);
			String lsFTPLocalFile =
				laServerPrp.getProperty(FTP_LOCAL_FILE) + lsFileSuffix;		

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
			// defect 10262	
//			RTSException leRTSEx = new RTSException();
//			leRTSEx.setCode(
//				ErrorsConstant
//					.ERR_NUM_DEPOSIT_PROPERTIES_FILE_LOAD_ERROR);				
//			leRTSEx.setMessage(aeEx.getMessage());
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_PROPERTIES_FILE_LOAD_ERROR,
				aeEx.getMessage());
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
					// don't report this error
				}
			}
		}
		BatchLog.write(END_OF + LOADING_FTP_PROPS);
		return laServerPrp;
	}
	
	/**
	 * Method logError
	 * 
	 * @param aeRTSEx RTSException
	 * @param asMsg String
	 * 
	 */
	private void logError(RTSException aeRTSEx, String asMsg)
	{
		aeRTSEx.printStackTrace();
		BatchLog.write(ERROR_DURING + DEPOSIT_FILE_PROCESSING);
		BatchLog.write(ERROR_CODE + aeRTSEx.getCode());
		// defect 10262
		// if (asMsg != null && !asMsg.trim().equals(EMPTY_STRING))
		if (!UtilityMethods.isEmpty(asMsg))
		// end defect 10262
		{
			BatchLog.write(ERROR_MESSAGE_PASSED + asMsg);
		}
		// defect 10262
//		if (aeRTSEx.getMessage() != null
//			&& !aeRTSEx.getMessage().equals(EMPTY_STRING))
		if (!UtilityMethods.isEmpty(aeRTSEx.getMessage()))
		{
			BatchLog.write(aeRTSEx.getMessage());
		}
//		if (aeRTSEx.getDetailMsg() != null
//			&& !aeRTSEx.getDetailMsg().equals(EMPTY_STRING))
		if (!UtilityMethods.isEmpty(aeRTSEx.getDetailMsg()))
		{
			BatchLog.write(aeRTSEx.getDetailMsg());
		}	
		// end defect 10262	
		caBankDepositDate = null;
		ciErrMsgNo = aeRTSEx.getCode();
		ciTransCnt = Integer.MIN_VALUE;
		ciSuccessIndi = 0;
		caTmpInsTimeStmp = null;
		caProcsComplTimeStmp = null;

		try
		{
			updateDepositReconHstry();
		}
		catch (RTSException aeRTSEx1)
		{
			aeRTSEx1.printStackTrace();
			// defect 10262
//			BatchLog.write(TEMP_UPDATE_ERROR + aeRTSEx.getCode());
//			BatchLog.write(leRTSEx.getMessage());
//			BatchLog.write(leRTSEx.getDetailMsg());
			BatchLog.error(
				TEMP_UPDATE_ERROR
					+ aeRTSEx.getCode());
			BatchLog.error(aeRTSEx1.getMessage());
			BatchLog.error(aeRTSEx1.getDetailMsg());
			// end defect 10262
		}
		finally
		{
			try
			{
				sendEmailErrorMsg(
					aeRTSEx.getMessage()
						+ HTML_NEW_LINE
						+ aeRTSEx.getDetailMsg()
						+ HTML_NEW_LINE
						+ asMsg);
			}

			catch (RTSException aeRTSEx1)
			{
				aeRTSEx1.printStackTrace();
				BatchLog.write(
					PROCESS_EMAIL_ERROR);
				BatchLog.write(aeRTSEx1.getMessage());
				BatchLog.write(aeRTSEx1.getDetailMsg());
			}
		}
	}
	
	/**
	 * Main method used for testing.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		IVTRSDepositDataProcess laIDDP = new IVTRSDepositDataProcess();

		if (aarrArgs.length > 0
			&& aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
		{
			BatchLog.write(
				PROCESSING_LOCAL_FTP_FILE + aarrArgs[0]);
			ssDepositFile = aarrArgs[0];
		}
		
		// defect 10262
		// laIDDP.beginProcessing();
		laIDDP.beginProcessing();
		// end defect 10262
	}
	/**
	 * Used for renaming remote server files.
	 * 
	 * @throws RTSException
	 */
	private void moveRemoteFiles() throws RTSException, IOException
	{
		for (int liFileCnt = 0;
			liFileCnt < carrFiles.length;
			liFileCnt++)
		{
			if (!carrFiles[liFileCnt].endsWith(CSV_FILE_TYPE))
			{
				continue;
			}
			BatchLog.write(
				MOVE_REMOTE_FILE
					+ carrFiles[liFileCnt]);
			// renaming the file: move it to the archive directory 	
			try
			{
				caFTPClient.rename(
					carrFiles[liFileCnt],
					ARCHIVE_DIR + carrFiles[liFileCnt]);
				if (!FTPReply
					.isPositiveCompletion(
						caFTPClient.getReplyCode()))
				{
					// defect 10262
//					throw new RTSException(
//						STORE_FILE_ERROR
//							+ carrFiles[liFileCnt]
//							+ ON_REMOTE_SYSTEM
//							+ caFTPClient.getReplyString());
					throw createMsg(
						ErrorsConstant
							.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
						caFTPClient.getReplyString());
					// end defect 10262		
				}
			}
			catch (IOException aeIOEx)
			{
				// defect 10262
//				leIOE.printStackTrace();				
//				throw leIOE;
				aeIOEx.printStackTrace();				
				throw aeIOEx;
				// end defect 10262
			}
		}
	}

	/**
	 * Main method that does all of the deposit file processing.
	 * This method will do the following:
	 * 
	 * 1. Load the FTP properties that are stored in a separate file.
	 * 2. Do an FTP get to retrieve the deposit file from BP.
	 * 3. Load the deposit file.
	 * 4. Insert the deposit csv file contents into a tmp db table and
	 * 	  edit the contents. 	
	 * 
	 * @throws RTSException
	 */
	private void processDepositFile() throws RTSException
	{
		BatchLog.write(START_OF + DEPOSIT_FILE_PROCESSING);
		
		// defect 9986
//		String lsDepositFileName = EMPTY_STRING;
		Vector lvDepositFiles = new Vector();
		// end defect 9986
		// when ssDepositFile is blank, get the Deposit file from
		// the TxO ftp server,
		// otherwise, a filename was passed to this process, so get 
		// that file locally from PAS1. 
		if (ssDepositFile.equals(EMPTY_STRING))
		{
			try
			{
				// Load the FTP Properties file
				loadFTPProperties();
				// Use FTP to get the Deposit File
				// defect 9986
				// lsDepositFileName = getDepositFile();
				lvDepositFiles = getDepositFile();
				// end defect 9986
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
		else
		{
			// defect 9986
			// lsDepositFileName = ssDepositFile;
			lvDepositFiles.add(ssDepositFile);
			// end defect 9986
		}
		// defect 9986
//		if (!lsDepositFileName.equals(EMPTY_STRING))
		if (lvDepositFiles.size() > 0)
		// end defect 9986	
//			&& lsDepositFileName.length() > 0)
		{
			// defect 9986
			// Vector lvDepositReconTmp = null;
			// use svDepositReconTmp below
			// end defect 9986
						
			Collections.sort(lvDepositFiles);			
//			defect 10111
			// defect 10262	
			// caDBAccess already established 
			// caDBAccess = new DatabaseAccess();
			// end defect 10263
			
			// defect 10262
			caDBAccess = new DatabaseAccess();
			// end defect 10262
			caDBAccess.beginTransaction();
			caIVTRSDepositReconTmpAccess =
				new InternetDepositReconTmp(caDBAccess);
			// delete all rts_itrnt_deposit_recon_tmp recs	
			int liRetRows =
				caIVTRSDepositReconTmpAccess
					.delInternetDepositReconTmp();
			BatchLog.write(
				TEMP_DEP_FILE_DELETED
					+ liRetRows);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// end defect 10111	
						
			try
			{
				// Load the Deposit records into a Vector.
				// defect 9986
				for (int liDepostFile = 0;
					liDepostFile < lvDepositFiles.size();
					liDepostFile++)
				{
					// defect 10262
					// saIDDP.insertIntoHstry();
					insertIntoHstry();
					// end defect 10262
					loadDepositFile(
						(String) lvDepositFiles.get(liDepostFile));
					loadReconTmpTable();
				}
			// end defect 10111		
				// end defect 9986
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
		else
		{
			// defect 10262
//			RTSException leRTSEx = new RTSException();
//			// defect 10111
//			//leRTSEx.setCode(3008);
//			leRTSEx.setCode(ErrorsConstant
//			.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			// end defect 10111
//			leRTSEx.setMessage(PROCESS_DEP_FILE_ERROR);
//			leRTSEx.setDetailMsg(DEPOSIT_FILE_MISSING_OR_BLANK);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				DEPOSIT_FILE_MISSING_OR_BLANK);
			// end defect 10262	
		}
	}
	
	/**
	 * Method loadReconTmpTable loads each deposit file rec into
	 * the rts_itrnt_deposit_recon_tmp
	 * 
	 * @throws RTSException
	 */
	
	private void loadReconTmpTable() throws RTSException
	{
		try
			{
				// defect 10111
//				caDBAccess.beginTransaction();
//				// delete all rts_itrnt_deposit_recon_tmp recs	
//				int liRetRows =
//					caIVTRSDepositReconTmpAccess
//						.delInternetDepositReconTmp();
//				BatchLog.write(
//					TEMP_DEP_FILE_DELETED
//						+ liRetRows);
//				caDBAccess.endTransaction(DatabaseAccess.COMMIT);							
				// defect 10262	
				// caDBAccess already established 
				// caDBAccess = new DatabaseAccess();
				// end defect 10263
				caDBAccess.beginTransaction();
				caIVTRSDepositReconTmpAccess =
					new InternetDepositReconTmp(caDBAccess);
//				end defect 10111		
				caIVTRSDepositReconTmpAccess
					.insInternetDepositReconTmp(
					cvDepositReconTmp);
				BatchLog.write(
					TEMP_FILE_ROWS_INSERTED
						+ cvDepositReconTmp.size());
				caDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// get current date/time for the insert
				// defect 10262 
				// caTmpInsTimeStmp = new RTSDate();
				// end defect 10262
			}

			catch (RTSException aeRTSEx)
			{
				try
				{
					caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (RTSException aeRTSEx1)
				{
					// defect 10262
					// e.printStackTrace();
					aeRTSEx1.printStackTrace();
					// end defect 10262
				}
				finally
				{
					throw aeRTSEx;
				}
			}

			try
			{
				ciErrMsgNo = 0;
				// defect 9986
				// siTransCnt = lvDepositReconTmp.size();
				// ciTransCnt = cvDepositReconTmp.size();
				// end defect 9986
				caProcsComplTimeStmp = null;
				// update with temp rec count		
				updateDepositReconHstry();
			}
			catch (RTSException aeRTSEx)
			{
				BatchLog.write(
					RECORD_COUNT_UPDATE_ERROR);
				throw aeRTSEx;
			}

			try
			{
				// defect 10111
				// defect 10262	
				// caDBAccess already established 
				// caDBAccess = new DatabaseAccess();
				// end defect 10263
				caDBAccess.beginTransaction();
				caIVTRSDepositReconTmpAccess =
					new InternetDepositReconTmp(caDBAccess);
				// end defect 10111
				// Allow for processing multiple days by removing the
				// following edit check block
				// defect 9986
//				if (saIVTRSDepositReconTmpAccess
//					.qryCountDistinctBankDepositDate()
//					> 1)
//				{
//					RTSException leRTSEx = new RTSException();
//					leRTSEx.setCode(2208);
//					leRTSEx.setMessage(
//						"Error loading deposit properties file");
//					leRTSEx.setDetailMsg(
//						"Multiple deposit dates "
//							+ "exist in the deposit file");
//					throw leRTSEx;
//				}
				// end defect 9986
				if (caIVTRSDepositReconTmpAccess.qryCountDuplicates()
					> 0)
				{
					// defect 10262
//					RTSException leRTSEx = new RTSException();
//					leRTSEx.setCode(
//						ErrorsConstant
//						.ERR_NUM_TRACE_NUM_EXISTS_ON_PREVIOUS_FILE_ERROR);
//					leRTSEx.setMessage(ERROR_LOADING_DEP_PROP);
//					leRTSEx.setDetailMsg(TRACE_NUM_EXISTS_ON_PREV_FILE);
//					throw leRTSEx;
					throw createMsg(
						ErrorsConstant
							.ERR_NUM_TRACE_NUM_EXISTS_ON_PREVIOUS_FILE_ERROR,
							 EMPTY_STRING);
					// end defect 10262		
				}
				caDBAccess.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
			try
			{
				ciErrMsgNo = 0;
				// defect 9986
				// siTransCnt = lvDepositReconTmp.size();
				ciTransCnt = cvDepositReconTmp.size();
				// defect 10111
				BatchLog.write(
					"number of ciTransCnt recs for "
						+ caBankDepositDate
						+ " = "
						+ ciTransCnt);
				// end defect 10111		
				// end defect 9986
				ciSuccessIndi = 0;
				caProcsComplTimeStmp = null;
				// update with a good process result		
				updateDepositReconHstry();
				// defect 10111
				cvDepositReconTmp.clear();
				// end defect 10111
			}
			catch (RTSException aeRTSEx)
			{
				BatchLog.write(
					HSTRY_UPDATE_RESULT_ERROR);
				throw aeRTSEx;
			}
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
		String asFileName)
		throws RTSException
	{
		BatchLog.write(START_OF + FTP_RETRIEVE);
		File laFile = null;
		try
		{
			// defect 9986
			// laFile = new File(aaFTPFile.getLocalFile());
			laFile = new File(asFileName);
			// end defect 9986
			BatchLog.write(START_OF + FTP_FILE_TRANSFER);
			OutputStream lpfsOut =
				new FileOutputStream(laFile.getAbsolutePath());
			// defect 9986
			//aaFTPClient.retrieveFile(aaFTPFile.getFtpFile(), lpfsOut);
			aaFTPClient.retrieveFile(asFileName, lpfsOut);
			// end defect 9986
			lpfsOut.close();
			if (!FTPReply
				.isPositiveCompletion(aaFTPClient.getReplyCode()))
			{
				// defect 10262
//				throw new RTSException(
//					FTP_FAILED_FILE_WRITE
//						+ aaFTPClient.getReplyString());
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
						aaFTPClient.getReplyString());
				// end defect 10262		
			}
			BatchLog.write(END_OF + FTP_FILE_TRANSFER);
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() == 0)
			{
			// defect 10262
//				leRTSEx.setCode(
//					ErrorsConstant
//						.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//				leRTSEx.setMessage(leRTSEx.getMsgType());
//				leRTSEx.setDetailMsg(FTP_ERROR_RETRIEVING);
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
						FTP_ERROR_RETRIEVING);		
			}
			else
			{
				throw aeRTSEx;
			}
			//end defect 10262	
		}
		catch (Exception aeEx)
		{
			// defect 10262
//			RTSException leRTSEx = new RTSException();
//			leRTSEx.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR);
//			leRTSEx.setMessage(leEx.getMessage());
//			leRTSEx.setDetailMsg(EXCEPTION_ERROR_RETR_DEP_FILE);
//			throw leRTSEx;
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
					EXCEPTION_ERROR_RETR_DEP_FILE);
			// end defect 10262	
		}
		// defect 9986		
//		finally
//		{
//			try
//			{
//				aaFTPClient.disconnect();
//			}
//			catch (Exception aeEx)
//			{ // Swallow error
//			}
		return laFile.getAbsolutePath();
//		}
		// end defect 9986
	}

	/**
	 * Handles the Error Processing for the transactions that need some
	 * attention.
	 * 
	 * @param avErrorTraceNos Vector
	 */
	private void sendEmailErrorMsg(String asEmailMsg)
		throws RTSException
	{
		BatchLog.write(START_OF + ERROR_HANDLER);
		BatchLog.write(SENDING_ERROR_EMAIL);

		if (SystemProperty.getDatasource().equals(PROD_DATASOURCE))
		{
			// defect 10262
			// EMAIL_SUBJECT = PRODUCTION + EMAIL_SUBJECT;
			csEmailSubject = PRODUCTION + csEmailSubject;
			// end defect 10262
		}
		else
		{
			// defect 10262
			// EMAIL_SUBJECT = TEST + EMAIL_SUBJECT;
			csEmailSubject = TEST + csEmailSubject;
			// end defect 10262
		}
		// Send email
		// defect 10262
		
		try
		{
			EmailUtil laEmailUtil = new EmailUtil();
		// defect 10262
//		if (!laEmailUtil
//			.sendEmail(
//				EMAIL_FROM,
//				EMAIL_TO,
//				// defect 10262
//				// EMAIL_SUBJECT,
//				csEmailSubject,
//				// end defect 10262
//				EMAIL_BODY + HTML_NEW_LINE + asEmailMsg))
//		{
			laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_TO,
			// defect 10262
			// EMAIL_SUBJECT,
			csEmailSubject,
			// end defect 10262
			EMAIL_BODY + HTML_NEW_LINE + asEmailMsg);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,
			aeEx.getMessage());
		}
			// defect 10262
//			RTSException leRTSEx = new RTSException();
//			leRTSEx.setCode(
//				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR);
//			leRTSEx.setMessage(
//				DEPOSIT_FILE_PROCESS_ERROR);
//			throw leRTSEx;
			// end defect 10262	
//		}
		BatchLog.write(END_OF + SENDING_ERROR_EMAIL);
	}
	
	/**
	 * Method createMsg
	 * @param aiMsgNo int
	 */
	private RTSException createMsg(int aiMsgNo, String lsDtlMsg)
	{
		RTSException leRTSEx =
			new RTSException(aiMsgNo);
		caErrorMsgData =
			ErrorMessagesCache.getErrMsg(aiMsgNo);
		if (caErrorMsgData == null)
		{
			leRTSEx.setMessage(ERROR_NUM_NOT_FOUND + aiMsgNo);
		}
		else
		{
			leRTSEx.setMessage(caErrorMsgData.getErrMsgDesc());
		}
		if (!UtilityMethods.isEmpty(lsDtlMsg))
		{
			leRTSEx.setDetailMsg(lsDtlMsg);
		}
		caErrorMsgData = null;
		return leRTSEx;
	}
	/**
	 * Method updateDepositReconHstry
	 * throws RTSException
	 */
	private void updateDepositReconHstry()
		throws RTSException
	{
		try
		{
			// defect 10111
			// caDBAccess.beginTransaction();
			// end defect 10111
			InternetDepositReconHstryData laIVTRSDRHD =
				new InternetDepositReconHstryData();
			laIVTRSDRHD.setProcsComplTimestmp(null);
			laIVTRSDRHD.setDepositReconHstryReqId(ciReqId);
			laIVTRSDRHD.setErrMsgNo(ciErrMsgNo);
			laIVTRSDRHD.setTransCount(cvDepositReconTmp.size());
			laIVTRSDRHD.setSuccessfulIndi(ciSuccessIndi);
			laIVTRSDRHD.setBankDepositDate(caBankDepositDate);
			// defect 10262
			// laIVTRSDRHD.setTmpInsrtTimestmp(caTmpInsTimeStmp);
			// the updInternetDepositReconHstry method called below
			// will get the DB date with setTmpInsrtTimestmp is not null
			laIVTRSDRHD.setTmpInsrtTimestmp(new RTSDate());
			// end defect 10262
			// defect 10111
			caIVTRSDepositReconHstryAccess =
				new InternetDepositReconHstry(caDBAccess);			
			// defect 10262	
			// caDBAccess already established 
			// caDBAccess = new DatabaseAccess();			
//			caIVTRSDepositReconHstryAccess =
//				new InternetDepositReconHstry(caDBAccess);
			// end defect 10262
			caDBAccess.beginTransaction();
			// end defect 10111
			caIVTRSDepositReconHstryAccess
				.updInternetDepositReconHstry(
				laIVTRSDRHD);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				BatchLog.write(
					RECON_HSTRY_UPDATE_ERROR);
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);

			}
			catch (RTSException aeRTSEx1)
			{
				BatchLog.write(
					HSTRY_ROLLBACK_ERROR);
			}
			finally
			{
				// defect 10262
				// throw aeRTSEx1;
				throw aeRTSEx;
				// end defect 10262
			}
		}
		// defect 10262
		catch (Exception aeEx)
		{
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_DATE_NOT_YYYYMMDD,
				aeEx.getMessage());		
		}
		// end defect 10262
	}
}