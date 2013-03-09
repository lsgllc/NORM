package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.RenewalEMail;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.data.RenewalEMailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
/*
 * EBouncebackBouncebacks.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		08/30/2010	Created Class.
 * 							defect 10512 Ver POS_650
 * B Brown		10/08/2010	Refer to the system property for the 
 * 							eReminder email address
 * 							modify EMAIL_TO
 * 							defect 10614 Ver POS_660
 * B Brown		12/28/2010	Refer to new EmailUtil class for eReminder
 * 							project.
 * 							modify sendEmailErrorMsg(),
 * 							beginProcessing(), sendEmail()
 * 							defect 10610 Ver POS_670
 * B Brown		03/17/2011	Change the instatiation of class 
 * 							EmailUtilEReminders to pass the attachment
 * 							String that will be sent with the 
 * 							eReminder email.
 * 							modify sendEmail()
 * 							defect 10781 Ver POS_670
 * ---------------------------------------------------------------------
 */

/**
 * Batch process that will handle an FTP get to the HQ-44
 * to get the EBounceback file.  This file contains information that will
 * be put into an email. Each row in the file represents variable
 * content in the email to be sent to remind customers to register
 * their vehicles.     
 * 
 * <p>Much like the BachTurnaround process, this process will be an 
 * application client residing on PAS1 and started via Unix crontab.
 *
 * @version	POS_670			03/17/2011
 * @author	Bob Brown
 * <br>Creation Date:		08/03/2010 08:50:00
 */	

public class EReminderBouncebacks 
{
	private static String ssEBouncebackFile = "";	
	
	private final String ARCHIVE_DIR = "archive/";
	
	private DatabaseAccess caDBAccess = null;
	private FTPClient caFTPClient = null;
	
	private FtpFile caFTPFile = null;
	
	private RenewalEMail caRenewalEMailAccess = 
		null;
	
	private ErrorMessagesData caErrorMsgData = null;		
	
	private String[] carrFiles = null;
	private final String CLASSPATH_ENTRY = "Classpath entry = ";
	private final String CREATING_NEW_PROPS_FILE =
		"Creating new EBounceback properties file.";
	private String csPropLoc = null;
	private final String DEL_FILE_TYPE = ".del";
	private Vector cvEBouncebackEmail = new Vector(); 
	
	private final String DASHES = "====================";
	// Default FTP properties.
	private final String DEFAULT_FTP_FILE =
		"'/RTSFTP/ereminder/Bouncebacks/bouncebacks.del'";
	private final String DEFAULT_FTP_HOST = "Txdot-hq44";
	private final String DEFAULT_FTP_LOCAL_FILE = "bouncebacks.del";
	private final String DEFAULT_FTP_PASS = "rtsftp12";
	private final int DEFAULT_FTP_PORT = 21;
	
	private final String DEFAULT_FTP_USER = "rtsftp";
	private final String EBOUNCEBACK_DIR = "ereminder/Bouncebacks/";
	private final String EBOUNCEBACK_FILE_MISSING_OR_BLANK =
		"EBounceback file is missing or blank";
	private final String EBOUNCEBACK_FILE_PROCESSING =
		"EBounceback File Processing.";
	private final String EBOUNCEBACK_PROPS_FILE =
		"cfg/ereminder_file.properties";
	private final String EMAIL_BODY =
		"The EBounceback file process failed and "
			+ "requires attention. <br> Batchlog.log also contains "
			+ "error details.";
	private String csEmailSubject =
		"EBounceback File Processing error";		
	private final String EMAIL_FROM =
		"TSD-RTS-POS@txdmv.gov";
	// defect 10614	
//	private final String EMAIL_TO =
//		"VTR_RTS-EReminder@txdmv.gov";
	private final String EMAIL_TO =
		SystemProperty.getEReminderEmail();
	// end defect 10614	
	private final String EMAIL_ERROR_TO =
		"TSD-RTS-POS@txdmv.gov";
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
		"Exception Error retrieving EBounceback file";
	private final String FILE_NOT_FOUND = "File not found";
	private final String FILE_SEPARATOR = "file.separator";
	private final String FILE_TO_RETRIEVE =
		"File to retrieve = ";
	private final String FILENOTFOUNDEX =
		"FileNotFoundException";
	private final String FTP_ERROR_RETRIEVING =
		"FTP error retrieving EBounceback file";
	public final String FTP_FILE = "FTPFile";
	private final String FTP_FILE_TRANSFER =
		"FTP file transfer.";
	public final String FTP_HOST = "FTPHost";
	public final String FTP_LOCAL_FILE = "FTPLocalFile";
	public final String FTP_PASS = "FTPPass";
	private final String FTP_RETRIEVE = "FTP retrieve process.";
	public final String FTP_USER = "FTPUser";
	private final String HTML_NEW_LINE = " = <br>";
	private final String IOEXCEPTION = "IOException";
	private final String JAVA_CLASS_PATH = "java.class.path";
	private final String LOAD_EBOUNCEBACK_FILE =
		"loading EBounceback file.";
	private final String LOADING_FTP_PROPS =
		"FTP Propertie loading....";
	private final String MOVE_REMOTE_FILE =
		"Trying to move files on the remote system = ";
	private final String PATH_SEPARATOR = "path.separator";
	private final String PROCESS_EMAIL_ERROR =
		"Could not send EBounceback file process error email";
	
	private final String PROD_DATASOURCE = "P0RTSDB";
	private final String PRODUCTION = "Production: ";
	private final String RETRIEVING_FILE =
		"Trying to retrieve file = ";
	private final String SENDING_ERROR_EMAIL =
		"SENDING ERROR EMAIL";
	private final String START_OF = "Start of ";
	private final String SUCCESSFULL_LOGIN =
		"Successful login to the FTP server.";	
	private final String TEST = "Test: ";
	private final String USING_FILE = "Using file: ";
	private final String ERROR_NUM_NOT_FOUND =
		"Could not find the following error in ErrorMessagesCache: ";
	private final String FILE_BEING_PROCESSED =
		"File being processed = ";

	private final String COULD_NOT_LOAD_STATIC_CACHE =
		"Could not load static cache";	
	private int ciEBouncebackDate = 0; 	
	private static RTSDate caEBouncebackDate = new RTSDate();	
	
	private final String csEBouncebackReportSubject = "EBounceback: Bounceback report attached";
	private final String csEBouncebackErrors = "email addresses not found";
	
	private final String CURRENT_REC_BLANK = 
		"Current record is blank , previous record information below:";
	
	private final String EMAIL_LIST_ERROR = "Error retrieving email list";
	private final String[] csToEmailsBounceBacks =
		new String[] {
			"Kip.Thomas@txdmv.gov",
			"Bob.L.Brown@txdmv.gov"};	
	private final String[] csToEmailsBounceBacksTest =
		new String[] {
			"Bob.L.Brown@txdmv.gov"};
	private final String NO_PROCESSING =
		"No processing occurring, a parm was passed. Parm = ";
	private final String NUM_OF_RECS = "Number of bouncebacks = ";
	private Vector cvRenewalEMailData = new Vector();  	
	private File caOutputFile;
	private final String REPORT_NAME = "RTS.POS.2601";
	private final String REPORT_TITLE = "TEXAS DEPARTMENT OF MOTOR VEHICLES";
	private final String REPORT_PREFIX = "bounceback";
	private final String DOT = ".";
	private final String REPORT_SUFFIX = "txt";
	private final String REPORT_SEND_PROBLEM = "Could not send email bounceback report.";
	private Vector cvRenewalEMailErrors = new Vector();
	private final String EMAIL_NOT_FOUND = "The following returned emails could not be found in renewal email table<br>";
	private final int PAGE_HEIGHT = 52;
							
		
	/**
	 * EBouncebackBouncebacks.java Constructor
	 * 
	 * 
	 */
	public EReminderBouncebacks()
	{
		super();
	}
	
	/**
	 * Method used to control processing.
	 * 
	 */	
	private void beginProcessing()
	{
		try
		{
			ciEBouncebackDate = caEBouncebackDate.getYYYYMMDDDate();
			caEBouncebackDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					ciEBouncebackDate);
			
			processEBouncebackFile();
			
			try
			{
				ReportProperties laRptProps =
					new ReportProperties(REPORT_NAME,PAGE_HEIGHT);	
				EReminderBouncebackReport laERBR =
					new EReminderBouncebackReport(REPORT_TITLE, laRptProps);
				// remove duplicates from the bounceback email list 
				if (!cvRenewalEMailData.isEmpty())
				{
					Collections.sort(cvRenewalEMailData);
					laERBR.formatReport(cvRenewalEMailData);
					storeBouncebackReport(laERBR);
				
					boolean lbEmailSent = sendEmail(caOutputFile.toString());
			
					if (!lbEmailSent)
					{
						throw createMsg(
							ErrorsConstant.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,
							null);
					}
				}
				
				if (!cvRenewalEMailErrors.isEmpty())
				{
					StringBuffer lsEmailsNotFound = new StringBuffer();
					lsEmailsNotFound.append(
						EMAIL_NOT_FOUND);
					for (int x = 0; x < cvRenewalEMailErrors.size(); x++)
					{
						lsEmailsNotFound.append(cvRenewalEMailErrors.elementAt(x) + HTML_NEW_LINE);
					}
					// defect 10610
					// EmailUtil laEmailService = new EmailUtil();
					EmailUtilEReminders laEmailService = new EmailUtilEReminders();
					// end defect 10610
					boolean lbEmailSent = laEmailService.sendEmail(
									EMAIL_TO,
									csEBouncebackErrors,
									lsEmailsNotFound.toString());
					if (!lbEmailSent)
					{
						BatchLog.write(REPORT_SEND_PROBLEM);		
						throw createMsg(
							ErrorsConstant
								.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,
								 EMPTY_STRING);				
					}	
				}
			}

			catch (IOException aeIOEx)
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
					aeIOEx.getMessage());
			}
			catch (RTSException leRTSEx)
			{
				throw leRTSEx;
			}
			catch (Exception aeEx)
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
					aeEx.getMessage());
			}
			
			if (ssEBouncebackFile.equals(EMPTY_STRING))
			{
				// try 3 times to move the remote files
				for (int x=0;x<3;x++)
				{	
					try
					{
						moveRemoteFiles();
						break;	
					}
					catch (RTSException aeRTSEx)
					{
						throw aeRTSEx;
					}
					catch (IOException aeIOEx)
					{
						if (x == 2)
						{
							throw createMsg(
								ErrorsConstant
									.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
								aeIOEx.getMessage());
						}
					}
				}
			}
		}
		
		catch (RTSException aeRTSEx)
		{
			logError(aeRTSEx, EMPTY_STRING);
		}
		finally
		{
			if (caFTPClient != null && caFTPClient.isConnected())
			{
				try
				{
					caFTPClient.disconnect();
				}
				catch(IOException aeIOEx)
				{

					aeIOEx.printStackTrace();
				}						
			}								
			BatchLog.write(END_OF + EBOUNCEBACK_FILE_PROCESSING);
			BatchLog.write(DASHES);
		}
	}
	/**
	 * Method used to connect and login to the T:\ drive (txdot-hq44)
	 * 
	 * @throws RTSException
	 * 
	 */	
	private void connectAndLogin() throws RTSException
	{
		try
		{
			caFTPClient = new FTPClient();

			caFTPClient.connect(
				caFTPFile.getFtpHost(),
				DEFAULT_FTP_PORT);

			if (!FTPReply
				.isPositiveCompletion(caFTPClient.getReplyCode()))
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_CONNECTION_FAILED,
					EMPTY_STRING);
			}

			if (!caFTPClient
				.login(
					caFTPFile.getFtpUser(),
					caFTPFile.getFtpPassword()))
			{
				throw createMsg(ErrorsConstant.ERR_NUM_FTP_FAILED_LOGIN,
								EMPTY_STRING);
			}

			BatchLog.write(SUCCESSFULL_LOGIN);
		
			caFTPClient.setFileType(
				org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE);

			if (!FTPReply
				.isPositiveCompletion(caFTPClient.getReplyCode()))
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_FAILED_TRANSFER_TYPE,
					caFTPClient.getReplyString());				
			}
			caFTPClient.enterLocalPassiveMode();

			if (!FTPReply
				.isPositiveCompletion(caFTPClient.getReplyCode()))
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_FAILED_ACTIVE_MODE,
					caFTPClient.getReplyString());			
			}
			caFTPClient.changeWorkingDirectory(EBOUNCEBACK_DIR);
			
			System.out.println("present working dir = " + caFTPClient.printWorkingDirectory());
		}
		catch (IOException aeIOEx)
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeIOEx.getMessage());
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.setDetailMsg(FTP_ERROR_RETRIEVING);
			throw aeRTSEx;
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
			EReminderBouncebacks
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
						+ EBOUNCEBACK_PROPS_FILE);
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
		csPropLoc = laNewFile.getAbsolutePath();
		// Return the input stream to the new property file.
		return new FileInputStream(laNewFile);
	}

	/**
	 * Method uses organizations.apache.commons.net.ftp.FTPClient to go to the
	 * comptroler to retrieve the EBounceback file and save the file.
	 * 
	 * We will be saving iterations of this file for debug purposes.
	 * If the number of days that we keep a file needs to be increased
	 * we just increase the NUM_TURN_AROUND_FILES_SAVED value.
	 * 
	 * @return String
	 * @throws RTSException
	 */
	private Vector getEBouncebackFile() throws RTSException
	{
		String lsEBouncebackFilePath = null;
		Vector lvEBouncebackFilePath = new Vector();
	
		try
		{
			connectAndLogin();
		
			carrFiles =	caFTPClient.listNames();
			if ((carrFiles != null) && (carrFiles.length != 0))
			{
				for (int liFileCnt = 0;
					liFileCnt < carrFiles.length;
					liFileCnt++)
				{
					if (!carrFiles[liFileCnt].endsWith(DEL_FILE_TYPE))
					{
						continue;
					}
					BatchLog.write(
						RETRIEVING_FILE
							+ carrFiles[liFileCnt]);
					lsEBouncebackFilePath =
						retrieveFile(
							caFTPClient,
						carrFiles[liFileCnt]);
					lvEBouncebackFilePath.add(lsEBouncebackFilePath);
				}
			}
			else
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEPOSIT_FILE_MISSING,
					EMPTY_STRING);
			}

		}
		catch (NoClassDefFoundError aeNCDFEEx)
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeNCDFEEx.getMessage());
		}
		catch (SocketException aeSEx)
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeSEx.getMessage());
		}
		catch (IOException aeIOEx)
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeIOEx.getMessage());
		}
		catch (RTSException aeRTSEx1)
		{
			throw aeRTSEx1;
		}
		catch (Exception aeEx)
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				aeEx.getMessage());
		}
		return lvEBouncebackFilePath;
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
	 * Loads the E-mailReminder file and puts the Special Plate 
	 * Transactions into a vector.
	 * 
	 * @param asFile String
	 * @throws RTSException
	 */
	private Vector loadEBouncebackFile(String asFile) throws RTSException
	{
		BatchLog.write(START_OF + LOAD_EBOUNCEBACK_FILE);
		String lsStrLine = EMPTY_STRING;

		try
		{
			BatchLog.write(FILE_BEING_PROCESSED + asFile);
			FileInputStream lpfstream = new FileInputStream(asFile);
			// check for empty file
			if(lpfstream.available()==0)
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_DEPOSIT_FILE_EMPTY,
					EMPTY_STRING);
			}
			DataInputStream laIn = new DataInputStream(lpfstream);
			BufferedReader laBr =
				new BufferedReader(new InputStreamReader(laIn));
		
			while ((lsStrLine = laBr.readLine()) != null)
			{
				if (lsStrLine.equals(EMPTY_STRING))
				{
					BatchLog.write(
						CURRENT_REC_BLANK);
					continue;		
				}
				cvEBouncebackEmail.addElement(lsStrLine);
			}
			BatchLog.write(END_OF + LOAD_EBOUNCEBACK_FILE);
			BatchLog.write(
					NUM_OF_RECS
					+ cvEBouncebackEmail.size());
			return cvEBouncebackEmail;
		}
		catch (IOException aeIOEx)
		{
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_VALUES_PARSING_ERROR,
				aeIOEx.getMessage());	
		}

		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
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
							+ EBOUNCEBACK_PROPS_FILE);
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
				BatchLog.write(EBOUNCEBACK_PROPS_FILE + FILE_NOT_FOUND);
				throw new FileNotFoundException(
					EBOUNCEBACK_PROPS_FILE + FILE_NOT_FOUND);		
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
				throw createMsg(
					ErrorsConstant.ERR_NUM_FTP_PROPERTIES_FILE_ERROR,
					aeEx.getMessage());		
			}
		}

		catch (NoSuchElementException aeNSEEx)
		{
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_STRING_TOKENIZER_ERROR,
				aeNSEEx.getMessage());		
		}

		try
		{
			laServerPrp = new Properties();
			laServerPrp.load(laFileServer);
			laFileServer.close();
			String lsFTPHost = laServerPrp.getProperty(FTP_HOST);
			String lsFTPUser = laServerPrp.getProperty(FTP_USER);
//			String lsFTPPass =
//				UtilityMethods.decryptPassword(
//					laServerPrp.getProperty(FTP_PASS));
			String lsFTPPass = laServerPrp.getProperty(FTP_PASS);
//			String lsFileSuffix =
//				(new RTSDate().add(RTSDate.DATE, -1)).getYYYYMMDDDate()
//					+ CSV_FILE_TYPE;
			String lsFTPFile =
				laServerPrp.getProperty(FTP_FILE);
			BatchLog.write(FILE_TO_RETRIEVE + lsFTPFile);
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
		BatchLog.write(ERROR_DURING + EBOUNCEBACK_FILE_PROCESSING);
		BatchLog.write(ERROR_CODE + aeRTSEx.getCode());
		if (!UtilityMethods.isEmpty(asMsg))
		{
			BatchLog.write(ERROR_MESSAGE_PASSED + asMsg);
		}
		if (!UtilityMethods.isEmpty(aeRTSEx.getMessage()))
		{
			BatchLog.write(aeRTSEx.getMessage());
		}
		if (!UtilityMethods.isEmpty(aeRTSEx.getDetailMsg()))
		{
			BatchLog.write(aeRTSEx.getDetailMsg());
		}		
//		caEBouncebackDate = null;	
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

	/**
	 * Main method used for testing.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		EReminderBouncebacks laERB = new EReminderBouncebacks();
		laERB.getStaticCache();
		
		if (aarrArgs.length > 0
			&& aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
		{
			BatchLog.write(
			laERB.NO_PROCESSING + aarrArgs[0]);
		}
		else
		{
			laERB.beginProcessing();
		}

	}
	
	private void getStaticCache()
	{
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
			if (!carrFiles[liFileCnt].endsWith(DEL_FILE_TYPE))
			{
				continue;
			}
			BatchLog.write(
				MOVE_REMOTE_FILE
					+ carrFiles[liFileCnt]);
			// renaming the file: move it to the archive directory 	
			try
			{
				String lsFilename =
					carrFiles[liFileCnt].substring(
						0,
						carrFiles[liFileCnt].indexOf(DOT));
				caFTPClient.rename(
					carrFiles[liFileCnt],
					ARCHIVE_DIR + lsFilename + ciEBouncebackDate + DEL_FILE_TYPE);
				if (!FTPReply
					.isPositiveCompletion(
						caFTPClient.getReplyCode()))
				{
					throw createMsg(
						ErrorsConstant
							.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
						caFTPClient.getReplyString());	
				}
			}
			catch (IOException aeIOEx)
			{
				aeIOEx.printStackTrace();				
				throw aeIOEx;
			}
		}
	}
	
	private void storeBouncebackReport(EReminderBouncebackReport aaEReminderBouncebackReport)
		throws RTSException, IOException
	{
		//write completed report to network hard drive
//		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		RTSDate laEReminderDate = new RTSDate();
		try
		{
			caOutputFile =
				new File(
					REPORT_PREFIX
						+ laEReminderDate.getYYYYMMDDDate()
						+ DOT + REPORT_SUFFIX);
			laFout = new FileOutputStream(caOutputFile);
			laPout = new PrintStream(laFout);
			laPout.print(aaEReminderBouncebackReport.caRpt.getReport().toString());
			laPout.close();
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
			BatchLog.write(EMAIL_LIST_ERROR);
			throw createMsg(
				ErrorsConstant.ERR_NUM_ERROR_RETRIEVING_EMAIL_LIST,
				EMPTY_STRING);	
		}
	}

	/**
	 * Main method that does all of the E-mail reminder file processing.
	 * This method will do the following:
	 * 
	 * 1. Load the FTP properties that are stored in a separate file.
	 * 2. Do an FTP get to retrieve the E-mail file from BP.
	 * 3. Load the e-reminder file.
	 * 4. Insert the E-mail csv file contents into a tmp db table and
	 * 	  edit the contents. 	
	 * 
	 * @throws RTSException
	 */
	private void processEBouncebackFile() throws RTSException
	{
		BatchLog.write(START_OF + EBOUNCEBACK_FILE_PROCESSING);
	
		Vector lvEBouncebackFiles = new Vector();
		// when ssEBouncebackFile is blank, get the E-mail file from
		// the server txdot-hq44 (T:\ drive)
		// otherwise, a filename was passed to this process, so get 
		// that file another location. 
		if (ssEBouncebackFile.equals(EMPTY_STRING))
		{
			try
			{
				// Load the FTP Properties file
				loadFTPProperties();
				lvEBouncebackFiles = getEBouncebackFile();
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
		else
		{
			lvEBouncebackFiles.add(ssEBouncebackFile);
		}
		if (lvEBouncebackFiles.size() > 0)
		{
			try
			{
//				Collections.sort(lvEBouncebackFiles);
//				Vector lvRenewalEMailErrorList = new Vector();
				for (int liBouncebackFile = 0;
					liBouncebackFile < lvEBouncebackFiles.size();
					liBouncebackFile++)
				{
					cvEBouncebackEmail = loadEBouncebackFile(
						(String) lvEBouncebackFiles.get(liBouncebackFile));
						
					Collections.sort(cvEBouncebackEmail);
					Iterator laIter = cvEBouncebackEmail.iterator();
					Object prev = null;
					while (laIter.hasNext())
					{
						Object laObj = laIter.next();
						if (prev != null && laObj.equals(prev))
						{
							laIter.remove();
						}
						else
						{
							prev = laObj;
						}
					}	
					if (cvEBouncebackEmail.isEmpty())
					{
						throw createMsg(
							ErrorsConstant.ERR_NUM_ERROR_RETRIEVING_EMAIL_LIST,
							EMPTY_STRING);			
					}
					for (int liErrorEmail = 0;
						liErrorEmail < cvEBouncebackEmail.size();
						liErrorEmail++)
					{
						caDBAccess = new DatabaseAccess();
						caDBAccess.beginTransaction();
						caRenewalEMailAccess = new RenewalEMail(caDBAccess);
						Vector lvRenewalEMailData =
							caRenewalEMailAccess.qryRecpntEMail(
								(String) cvEBouncebackEmail.get(
									liErrorEmail));
						if (lvRenewalEMailData.isEmpty())
						{
							cvRenewalEMailErrors.add(
								(String) cvEBouncebackEmail
									.elementAt(liErrorEmail)); 		
						}
						else
						{
							for (int x = 0; x < lvRenewalEMailData.size(); x++)
							{
								cvRenewalEMailData.add(
									(RenewalEMailData) 
									lvRenewalEMailData.elementAt(x));
							}
						}
						caDBAccess.endTransaction(DatabaseAccess.NONE);
					}
				}

			}
			catch (RTSException aeRTSEx)
			{
					BatchLog.write(EMAIL_LIST_ERROR);
					throw createMsg(
						ErrorsConstant.ERR_NUM_ERROR_RETRIEVING_EMAIL_LIST,
						EMPTY_STRING);	
			}
			finally
			{
				caDBAccess.endTransaction(DatabaseAccess.NONE);
			}
		}
		else
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				EBOUNCEBACK_FILE_MISSING_OR_BLANK);
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
			laFile = new File(asFileName);
			BatchLog.write(START_OF + FTP_FILE_TRANSFER);
			OutputStream lpfsOut =
				new FileOutputStream(laFile.getAbsolutePath());
			aaFTPClient.retrieveFile(asFileName, lpfsOut);
			lpfsOut.close();
			if (!FTPReply
				.isPositiveCompletion(aaFTPClient.getReplyCode()))
			{
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
						aaFTPClient.getReplyString());
			}
			BatchLog.write(END_OF + FTP_FILE_TRANSFER);
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() == 0)
			{
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
						FTP_ERROR_RETRIEVING);		
			}
			else
			{
				throw aeRTSEx;
			}
		}
		catch (Exception aeEx)
		{
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
					EXCEPTION_ERROR_RETR_DEP_FILE);
		}
		return laFile.getAbsolutePath();
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
			csEmailSubject = PRODUCTION + csEmailSubject;
		}
		else
		{
			csEmailSubject = TEST + csEmailSubject;
		}
	
		try
		{
			// defect 10610
			// EmailUtil laEmailUtil = new EmailUtil();
			EmailUtilEReminders laEmailUtil = new EmailUtilEReminders();
			// end defect 10610
			laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_ERROR_TO,
			csEmailSubject,
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
	 * Method sendMail
	 * throws RTSException
	 */
	
	private boolean sendEmail(String asFileAttachment) throws RTSException
	{
		boolean lbEmailSent = false;
		try
		{
			// defect 10610
			// EmailUtil laEmailService = new EmailUtil();
			// defect 10781
			// EmailUtilEReminders laEmailService = new EmailUtilEReminders();
			EmailUtilEReminders laEmailService =
				new EmailUtilEReminders(asFileAttachment);
			// end defect 10781
			// end defect 10610
			lbEmailSent = laEmailService.sendEmail(
					EMAIL_TO,
					csEBouncebackReportSubject,
					EMPTY_STRING);
			if (!lbEmailSent)
			{
				BatchLog.write(REPORT_SEND_PROBLEM);		
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,
						 EMPTY_STRING);				
			}
		}
		catch (RTSException aeEx)
		{
			throw aeEx;
		}		
		return lbEmailSent;
	}

}
