package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.db.RenewalEMail;
import com.txdot.isd.rts.server.db.RenewalEMailHstry;
import com.txdot.isd.rts.server.db.RenewalEMailTmp;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.cache.VehicleMakesCache;
import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.data.RenewalEMailData;
import com.txdot.isd.rts.services.data.RenewalEMailHstryData;
import com.txdot.isd.rts.services.data.VehicleMakesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
/*
 * EReminderDataProcess.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		08/02/2010	Created Class.
 * 							defect 10512 Ver POS_650
 * B Brown		08/04/2010	Added code for Veh make desc and county name
 * 							formatting.
 * 							modify buildMessage()
 * 							defect 10512 Ver POS_650
 * B Brown		10/08/2010	Refer to SystemProperty for eReminder email
 * 							address.
 * 							modify csToEmailsBounceBacks
 * 							defect 10614 Ver POS_660
 * B Brown		11/22/2010	Make caEReminderDate non-static
 * 							defect 10610 Ver POS_660
 * B Brown		12/28/2010	Refer to new EmailUtilEReminders class,
 * 							code for 1-week and 3-week out emails, and
 * 							for test directory del file access.
 * 							add cs1WeekOnlineMessage, 
 * 								cs3WeekOnlineMessage,
 * 								cs1WeekOfflineMessage, 
 * 								cs3WeekOfflineMessage,
 * 							csEReminder1WeekSubject,
 * 							csEReminder3WeekSubject
 * 							modify getEmailErrors(), connectAndLogin(),
 * 							  sendEmailErrorMsg(), sendEmail(),
 * 							  beginProcessing()	
 * 							defect 10610 Ver POS_670  
 * B Brown		01/24/2011	Put vehicle make in email when make cannot
 * 							be found tin veh makes cache.
 * 							modify buildMessage()
 * 							defect 10736 Ver POS_670
 * B Brown		04/26/2011	Change the word hleping to helping in the 
 * 							"cs3WeekOfflineMessage" variable
 * 							defect 10787 Ver POS_671
 * B Brown		05/03/2011	Change all 4 email messages to look
 * 							for img src on IVTRS (use csWebURL), not 
 * 							POS (csImageURL).
 * 							modify beginProcessing(),processEmail(),
 *							sendMail()
 * 							defect 10807 Ver POS_670 fix 1
 * B Brown		05/09/2011	EReminder records with no VIN should get 
 * 							email directing the user to	the county until
 * 							IVTRS allows renewal with no VIN.
 * 							modify loadEReminderFile(), buildMessage()
 * 							defect 10811 Ver POS_671 
 * B Brown		09/29/2011	Allow the eReminder process to continue
 * 							after encountering a record with a VIN less
 * 							then 4 digits long
 * 							modify buildMessage()
 * 							defect 10970 Ver POS_690 
 * B Brown		10/07/2011	Change directory to look in for the del file
 * 							to a test dir when the region is not prod.
 * 							modify connectAndLogin()
 * 							defect 10922 Ver POS_690 
 * B Brown		10/07/2011	Connect and login to remote server to move 
 * 							the eReminder del file, if there
 * 							is a connection timeout (SocketException).
 * 							modify beginProcessing(), moveRemoteFiles()
 * 							defect 10848 Ver POS_690
 * ---------------------------------------------------------------------
 */

/**
 * Batch process that will handle an FTP get to the HQ-44
 * to get the EReminder file.  This file contains information that will
 * be put into an email. Each row in the file represents variable
 * content in the email to be sent to remind customers to register
 * their vehicles.     
 * 
 * <p>Much like the BachTurnaround process, this process will be an 
 * application client residing on PAS1 and started via Unix crontab.
 *
 * @version	POS_690 		09/29/2011
 * @author	Bob Brown
 * <br>Creation Date:		08/02/2010 08:50:00
 */	

public class EReminderDataProcess
{
	private static final String ONLY_SENDING_EMAIL =
		"Only sending E-Reminder email, no file processing. " +
		" The parm passed to this process = ";	
	private static String ssEReminderFile = "";	
	
	private final String ARCHIVE_DIR = "archive/";
	
	private DatabaseAccess caDBAccess = null;
	private FTPClient caFTPClient = null;
	
	private FtpFile caFTPFile = null;
	
	private RenewalEMailTmp caRenewalEMailTmpAccess =
		null;		
	private RenewalEMailHstry caRenewalEMailHstryAccess =
		null;
	private RenewalEMail caRenewalEMailAccess = 
		null;
	
//	private RenewalEMailHstryData caRenewalEMailHstryData =
//		null;		
	private RenewalEMailData caRenewalEMailData = null;	
		
	private ErrorMessagesData caErrorMsgData = null;		
	
	private RTSDate caProcsComplTimeStmp;
	private String[] carrFiles = null;
	private RTSDate caTmpInsTimeStmp;
	private int ciErrMsgNo = 0;
	
	private int ciReqId = 0;	
	private int ciSuccessIndi;
	private int ciTransCnt;
	private final String CLASSPATH_ENTRY = "Classpath entry = ";
	private final String COMMA = ",";
	private final String CREATING_NEW_PROPS_FILE =
		"Creating new ereminder properties file.";
	private String csPropLoc = null;
	private final String DEL_FILE_TYPE = ".del";
	private Vector cvEReminderTmp = new Vector(); 
	
	private final String DASHES = "====================";
	// Default FTP properties.
	private final String DEFAULT_FTP_FILE =
		"'/RTSFTP/ereminder/erenewals.del'";
	private final String DEFAULT_FTP_HOST = "Txdot-hq44";
	private final String DEFAULT_FTP_LOCAL_FILE = "erenewals.del";
	private final String DEFAULT_FTP_PASS = "rtsftp12";
	private final int DEFAULT_FTP_PORT = 21;
	
	private final String DEFAULT_FTP_USER = "rtsftp";
	private final String EREMINDER_DIR = "ereminder/";
	// defect 10610
	private final String EREMINDER_TEST_DIR = "ereminder/test/";
	// end defect 10610
	private final String EREMINDER_FILE_MISSING_OR_BLANK =
		"EREMINDER file is missing or blank";
	private final String EREMINDER_FILE_PROCESSING =
		"EREMINDER File Processing.";
	private final String EREMINDER_PROPS_FILE =
		"cfg/ereminder_file.properties";
	private final String EMAIL_BODY =
		"The EREMINDER file process failed and "
			+ "requires attention. <br> Batchlog.log also contains "
			+ "error details.";
	// defect 10610		
//	private final String EMAIL_FROM =
//		"TSD-RTS-POS@txdmv.gov";
	private final String EMAIL_FROM = SystemProperty.getEReminderEmail();
	// end defect 10610	
	private String csEmailSubject =
		"EREMINDER File Processing error";
	private final String EMAIL_TO = "TSD-RTS-POS@txdmv.gov";
//	"Bob.L.Brown@txdmv.gov";
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
		"Exception Error retrieving EREMINDER file";
	private final String FILE_NOT_FOUND = "File not found";
	private final String FILE_SEPARATOR = "file.separator";
	private final String FILE_TO_RETRIEVE =
		"File to retrieve = ";
	private final String FILENOTFOUNDEX =
		"FileNotFoundException";
	private final String FTP_ERROR_RETRIEVING =
		"FTP error retrieving EREMINDER file";
	public final String FTP_FILE = "FTPFile";
	private final String FTP_FILE_TRANSFER =
		"FTP file transfer.";
	public final String FTP_HOST = "FTPHost";
	public final String FTP_LOCAL_FILE = "FTPLocalFile";
	public final String FTP_PASS = "FTPPass";
	private final String FTP_RETRIEVE = "FTP retrieve process.";
	public final String FTP_USER = "FTPUser";
	private final String HSTRY_ROLLBACK_ERROR =
		"Cound not do an end transaction rollback on the Email Hstry" +
		" table update";
	private final String HSTRY_UPDATE_RESULT_ERROR =
		"Could not update EREMINDER Email Hstry with final good " +
		"result";
	private final String HTML_NEW_LINE = " = <br>";
	private final String INSERT_INTO_HSTRY_ERROR =
		"Could not insert into RTS_RENWL_EMAIL_HSTRY";
	private final String INSERTING_INTO_HSTRY =
		"Inserting into RTS_RENWL_EMAIL_HSTRY, renwlemailreqid = ";
	private final String IOEXCEPTION = "IOException";
	private final String JAVA_CLASS_PATH = "java.class.path";
	private final String LOAD_EREMINDER_FILE =
		"loading EREMINDER file.";
	private final String LOADING_FTP_PROPS =
		"FTP Propertie loading....";
	private final String MOVE_REMOTE_FILE =
		"Trying to move files on the remote system = ";
	private final String ON_REMOTE_SYSTEM =
		"on remote system: FTP reply: ";
	private final String PATH_SEPARATOR = "path.separator";
	private final String PROCESS_EMAIL_ERROR =
		"Could not send EREMINDER file process error email";
	//private final int NUM_TURN_AROUND_FILES_SAVED = 7;
	
	private final String PROD_DATASOURCE = "P0RTSDB";
	private final String PRODUCTION = "Production: ";
	private final String RENWL_HSTRY_UPDATE_ERROR =
		"Could not update RTS_RENWL_EMAIL_HSTRY";
	private final String RECORD_COUNT_UPDATE_ERROR =
		"Could not update RTS_RENWL_EMAIL_HSTRY with record	count";
	private final String RETRIEVING_FILE =
		"Trying to retrieve file = ";
	private final String SENDING_ERROR_EMAIL =
		"SENDING ERROR EMAIL";
	private final String START_OF = "Start of ";
	private final String EMAIL_INSERT_PROCESS =
		"E-mail reminder Insert processing";
	private final String SUCCESSFULL_LOGIN =
		"Successful login to the FTP server.";	
	private final String TEMP_DEP_FILE_DELETED =
		"Temp EREMINDER file rows deleted";
	private final String TEMP_FILE_ROWS_INSERTED =
		"Temp EREMINDER file Rows inserted = ";
	private final String TEMP_UPDATE_ERROR =
		"Could not update RTS_RENWL_EMAIL_HSTRY with error message";
	private final String TEST = "Test: ";
//	private final String TRACE_NUM_EXISTS_ON_PREV_FILE =
//		"Trace number exists on previous EREMINDER file";
	private final String USING_FILE = "Using file: ";
	private final String VR = "VR";
	private final String ERROR_NUM_NOT_FOUND =
		"Could not find the following error in ErrorMessagesCache: ";
	private final String FILE_BEING_PROCESSED =
		"File being processed = ";

	private final String COULD_NOT_LOAD_STATIC_CACHE =
		"Could not load static cache";	
	private int ciEReminderDate = 0;
	// defect 10610 	
	//private static RTSDate caEReminderDate = new RTSDate();	
	private RTSDate caEReminderDate = new RTSDate();
	// end defect 10610
	private final String NUM_ROWS_INSERTED =
		"E-mail file liRetRows inserted = ";
	private final static String INSERT_ERROR =
		"Error inserting into E-mail file";
	private final String UPDATE_HSTRY_ERROR =
		"Could not update E-mail Hstry";
	private String csEBounceBacksSubject = "Email Delivery Errors";
	//	defect 10610	
//	private String csEReminderSubject = "eReminder: Renew your registration";
//	private String csMessage =
//		"<center><b>Vehicle Registration eReminder</b></center><br><br>"
//			+ "The vehicle registration for your #vehmodlyr# #vehmk# " 			
//			+ "(last four digits of the VIN are #vin4#) is due for "			
//			+ "renewal. The registration for this vehicle will expire" 			
//			+ " on the last day of #regexpmo#, #regexpyr#.<br><br>"
//			+ "You can renew #txo# by " 			
//			+ "visiting your #countyname# County Tax " 			
//			+ "Assessor-Collector\'s office.<br><br>"
//			+ "Do not reply to this automated message because the " 			
//		+ "sending mailbox cannot receive emails.";
	// defect 10807 - do not use csImageURL
	//private String csImageURL = "";	
	// end defect 10807			
	private String csWebURL = "";	
	
	private String csEReminderSubject = "";
	private String csMessage = "";
	private String csEReminder1WeekSubject =
		"Urgent eReminder: 1 Week Until TxDMV Sticker Expires";
	// defect 10807
	// everywhere you see "#imagehost# below, /NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/
	// replaced the RTSPOSProject URL for the images
	// this applies to all 4 messages below: 
	// cs1WeekOnlineMessage
	// cs3WeekOnlineMessage
	// cs1WeekOffineMessage
	// cs3WeekOfflineMessage	
	private String cs1WeekOnlineMessage =
			"<html><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"> "
		  + "<title></title></head><body>"
		  +	"<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>"
		  + "<td><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/1_01.jpg\"" 
		  + " alt=\"\" width=\"600\" height=\"153\" align=\"top\" /></td></tr><tr><td>"
		  + "<table width=\"100%\" border=\"0\" cellspacing=\"10\" cellpadding=\"0\"" 
		  + "background=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/email-bg.jpg\"> "
		  + "<tr><td align=\"left\" valign=\"top\"><p><span style=\"font-family: " 		  
		  +	"Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"<font color=\"#0055a4\"><em style=\"font-size: 14px; font-weight: bold;\"> "
		  +	"Hurry - your vehicle registration sticker expires in just one week!</em></font></span></p>"
		  + "<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"The vehicle registration for your #vehmodlyr# #vehmk# " 
		  // defect 10811
		  // + "(the last four digits of your VIN are #vin4#) "
		  + "#vin#"
		  // end defect 10811 
		  +	"expires this month, #regexpmo#/#regexpyr#, in #countyname# County. " 
		  + "To renew online, right now, simply <a href=\"#webhost#/NASApp/txdotrts/RegistrationRenewalServlet\"> "
		  + "<font color=\"#0054a5\">click here</font></a>.</span></p> "
		  +	"<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> " 
		  + "By keeping your sticker current, you\'re helping build and maintain our Texas highways, " 
		  + "roads and bridges, as well as ensuring you won't receive a citation for an expired sticker.</span></p>"
		  +	"<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> " 
		  +	"Thanks for being a Registered Texan!</span><br /></p>"
		  +	"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "
		  +	"<tr><td align=\"center\" valign=\"top\"><p><span style=\"font-family: "		  
		  + "Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  +	"Have you moved? Click here to <a href=\"#webhost#/NASApp/txdotrts/AddressChangeServlet\"> "
		  +	"<font color=\"#0054a5\">update your address with the TxDMV.</font></a></span></p> "
		  +	"<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  + "Join the registration conversation on Facebook and follow us on Twitter@TxDMV.<br /> "
		  + "<a href=\"http://www.facebook.com/TxDMV\">" 		  
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/facebook.png\" " 
		  + "alt=\"Find us on Facebook\" width=\"101\" height=\"31\" border=\"0\" /></a><br /> "
		  + "Make sure you receive your eReminder: Add <strong>eReminder@TxDMV.gov</strong> " 
		  + "to your address book or safe-sender list. <br />"
		  + "And please don't reply to this automated message, because the sending mailbox" 
		  + "cannot receive e-mails. <br /><br />"                
		  + "To remove yourself from eReminder, click the button below.<br /> "
		  + "<a href=\"#webhost#/NASApp/txdotrts/EReminderServlet?XXtask=5 \">" 		  
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/unsubscribe.png\" " 		  
		  +	"alt=\"Unsubscribe\" width=\"101\" height=\"28\" border=\"0\" /></a></span></td>"
		  + "</tr></table></td></tr></table></td></tr></table></BODY></HTML>";				
//		"Hurry -- your Vehicle Registration sticker expires in just one week!"
//			+ "The vehicle registration for your #vehmodlyr# #vehmk# " 			
//			+ "(last four digits of your VIN are #vin4#) expires "			
//			+ "this month, #regexpmo#/#regexpyr#, in #countyname# County. " 			
//			+ " If you\'re planing on mailing in your vehicle registration notice, do it"
//			+ "today, or visit your #countyname# Tax Assessor-Collector\'s office before the month ends!"
//			+ "By keeping your sticker current, you\'re helping build and "
//			+ "maintain our Texas highways, roads and bridges, as well as " 			
//			+ "ensuring you won\'t receive a citation for an expired sticker."
//			+ "Thanks for being a registered Texan!";
	private String csEReminder3WeekSubject =
		"TxDMV eReminder: Vehicle Registration Renewal";
	private String cs3WeekOnlineMessage =
			"<html><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"> "
		  + "<title></title></head><body>"
		  +	"<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>"
		  + "<td><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/1_01.jpg\"" 
		  + " alt=\"\" width=\"600\" height=\"153\" align=\"top\" /></td></tr><tr><td>"
		  + "<table width=\"100%\" border=\"0\" cellspacing=\"10\" cellpadding=\"0\"" 
		  + "background=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/email-bg.jpg\"> "
		  + "<tr><td align=\"left\" valign=\"top\"><p><span style=\"font-family: " 
		  +	"Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"<font color=\"#0055a4\"><em style=\"font-size: 14px; font-weight: bold;\"> "
		  +	"Hey, Registered Texan, it's time to renew!</em></font></span></p>"
		  + "<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"The vehicle registration for your #vehmodlyr# #vehmk# " 
		  // defect 10811
		  // + "(the last four digits of your VIN are #vin4#) "
		  + "#vin#"
		  // end defect 10811 
		  +	"expires this month, #regexpmo#/#regexpyr#, in #countyname# County. " 
		  + "To renew online, right now, simply <a href=\"#webhost#/NASApp/txdotrts/RegistrationRenewalServlet\"> "
		  + "<font color=\"#0054a5\">click here</font></a>.</span></p> "
		  +	"<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> " 
		  + "By keeping your sticker current, you\'re helping build and maintain our Texas highways, " 
		  + "roads and bridges, as well as ensuring you won't receive a citation for an expired sticker.</span></p>"
		  +	"<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> " 
		  +	"Thanks for being a Registered Texan!</span><br /></p>"
		  +	"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "
		  +	"<tr><td align=\"center\" valign=\"top\"><p><span style=\"font-family: "
		  + "Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  +	"Have you moved? Click here to <a href=\"#webhost#/NASApp/txdotrts/AddressChangeServlet\"> "
		  +	"<font color=\"#0054a5\">update your address with the TxDMV.</font></a></span></p> "
		  +	"<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  + "Join the registration conversation on Facebook and follow us on Twitter@TxDMV.<br /> "
		  + "<a href=\"http://www.facebook.com/TxDMV\">" 
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/facebook.png\" " 
		  + "alt=\"Find us on Facebook\" width=\"101\" height=\"31\" border=\"0\" /></a><br /> "
		  + "Make sure you receive your eReminder: Add <strong>eReminder@TxDMV.gov</strong> " 
		  + "to your address book or safe-sender list. <br />"
		  + "And please don't reply to this automated message, because the sending mailbox" 
		  + "cannot receive e-mails. <br /><br />"                
		  + "To remove yourself from eReminder, click the button below.<br /> "
		  + "<a href=\"#webhost#/NASApp/txdotrts/EReminderServlet?XXtask=5 \">" 
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/unsubscribe.png\" " 
		  +	"alt=\"Unsubscribe\" width=\"101\" height=\"28\" border=\"0\" /></a></span></td>"
		  + "</tr></table></td></tr></table></td></tr></table></BODY></HTML>";
//		"Hey, Registered Texan, it\'s time to renew!"
//			+ "The vehicle registration for your #vehmodlyr# #vehmk# " 			
//			+ "(the last four digits of your VIN are #vin4#) expires "			
//			+ "this month #regexpmo#/#regexpyr#, in #countyname# County " 			
//			+ " To renew online right now, simply " //			+ "<a href=https://rts.texasonline.state.tx.us/NASApp/txdotrts/common/jsp/txdot_vtr_main_menu.jsp?language=eng>click here.</a>"			
//			+ "By keeping your sticker current, you\'re helping build and "
//			+ "maintain our Texas highways, roads and bridges, as well as " 			
//			+ "ensuring you won\'t receive a citation for an expired sticker."
//			+ "Thanks for being a registered Texan!";

	private String cs1WeekOfflineMessage =
			"<html><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"> "
		  + "<title></title></head><body>"
		  +	"<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>"
		  + "<td><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/1_01.jpg\"" 
		  + " alt=\"\" width=\"600\" height=\"153\" align=\"top\" /></td></tr><tr><td>"
		  + "<table width=\"100%\" border=\"0\" cellspacing=\"10\" cellpadding=\"0\"" 
		  + "background=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/email-bg.jpg\"> "
		  + "<tr><td align=\"left\" valign=\"top\"><p><span style=\"font-family: " 
		  +	"Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"<font color=\"#0055a4\"><em style=\"font-size: 14px; font-weight: bold;\"> "
		  +	"Hurry - your vehicle registration sticker expires in just one week!</em></font></span></p>"
		  + "<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"The vehicle registration for your #vehmodlyr# #vehmk# " 
		  // defect 10811
		  // + "(the last four digits of your VIN are #vin4#) "
		  + "#vin#"
		  // end defect 10811 
		  +	"expires this month, #regexpmo#/#regexpyr#, in #countyname# County. " 
		  + "If you\'re planning on mailing in your vehicle registration notice, do it today, " 		  
		  +	"or visit the #countyname# tax assessor-collector\'s office before the month ends!</span></p>"
		  + "<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  + "By keeping your sticker current, you're helping build and maintain our Texas highways, "           
		  +	"roads and bridges, as well as ensuring you won't receive a citation for an expired sticker.</span></p> "
		  +	"<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> " 
		  +	"Thanks for being a Registered Texan!</span><br /></p>"
		  +	"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "
		  +	"<tr><td align=\"center\" valign=\"top\"><p><span style=\"font-family: "
		  + "Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  +	"Have you moved? Click here to <a href=\"#webhost#/NASApp/txdotrts/AddressChangeServlet\"> "
		  +	"<font color=\"#0054a5\">update your address with the TxDMV.</font></a></span></p> "
		  +	"<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  + "Join the registration conversation on Facebook and follow us on Twitter@TxDMV.<br /> "
		  + "<a href=\"http://www.facebook.com/TxDMV\">" 
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/facebook.png\" " 
		  + "alt=\"Find us on Facebook\" width=\"101\" height=\"31\" border=\"0\" /></a><br /> "
		  + "Make sure you receive your eReminder: Add <strong>eReminder@TxDMV.gov</strong> " 
		  + "to your address book or safe-sender list. <br />"
		  + "And please don't reply to this automated message, because the sending mailbox" 
		  + "cannot receive e-mails. <br /><br />"                
		  + "To remove yourself from eReminder, click the button below.<br /> "
		  + "<a href=\"#webhost#/NASApp/txdotrts/EReminderServlet?XXtask=5 \">" 
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/unsubscribe.png\" " 
		  +	"alt=\"Unsubscribe\" width=\"101\" height=\"28\" border=\"0\" /></a></span></td>"
		  + "</tr></table></td></tr></table></td></tr></table></BODY></HTML>";
		  
	private String cs3WeekOfflineMessage =
			"<html><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"> "
		  + "<title></title></head><body>"
		  +	"<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>"
		  + "<td><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/1_01.jpg\"" 
		  + " alt=\"\" width=\"600\" height=\"153\" align=\"top\" /></td></tr><tr><td>"
		  + "<table width=\"100%\" border=\"0\" cellspacing=\"10\" cellpadding=\"0\"" 
		  + "background=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/email-bg.jpg\"> "
		  + "<tr><td align=\"left\" valign=\"top\"><p><span style=\"font-family: " 
		  +	"Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"<font color=\"#0055a4\"><em style=\"font-size: 14px; font-weight: bold;\"> "
		  +	"Hey, Registered Texan, it's time to renew!</em></font></span></p>"
		  + "<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  +	"The vehicle registration for your #vehmodlyr# #vehmk# " 
		  // defect 10811
		  // + "(the last four digits of your VIN are #vin4#) "
		  + "#vin#"
		  // end defect 10811 
		  +	"expires this month, #regexpmo#/#regexpyr#, in #countyname# County. " 
		  + "To renew your registration, simply mail in the registration renewal notice "
		  + "you received in the mail or visit the #countyname# tax assessor-collector\'s office.</span></p>"
		  + "<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> "
		  + "By keeping your sticker current, you\'re helping build and maintain our Texas highways, "
		  + "roads and bridges, as well as ensuring you won\'t receive a citation for an expired sticker.</span></p> "
		  +	"<p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"> " 
		  +	"Thanks for being a Registered Texan!</span><br /></p>"
		  +	"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "
		  +	"<tr><td align=\"center\" valign=\"top\"><p><span style=\"font-family: "
		  + "Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  +	"Have you moved? Click here to <a href=\"#webhost#/NASApp/txdotrts/AddressChangeServlet\"> "
		  +	"<font color=\"#0054a5\">update your address with the TxDMV.</font></a></span></p> "
		  +	"<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px;\"> "
		  + "Join the registration conversation on Facebook and follow us on Twitter@TxDMV.<br /> "
		  + "<a href=\"http://www.facebook.com/TxDMV\">" 
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/facebook.png\" " 
		  + "alt=\"Find us on Facebook\" width=\"101\" height=\"31\" border=\"0\" /></a><br /> "
		  + "Make sure you receive your eReminder: Add <strong>eReminder@TxDMV.gov</strong> " 
		  + "to your address book or safe-sender list. <br />"
		  + "And please don't reply to this automated message, because the sending mailbox" 
		  + "cannot receive e-mails. <br /><br />"                
		  + "To remove yourself from eReminder, click the button below.<br /> "
		  + "<a href=\"#webhost#/NASApp/txdotrts/EReminderServlet?XXtask=5 \">" 
		  +	"<img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/unsubscribe.png\" " 
		  +	"alt=\"Unsubscribe\" width=\"101\" height=\"28\" border=\"0\" /></a></span></td>"
		  + "</tr></table></td></tr></table></td></tr></table></BODY></HTML>";	
		  
	private int ciCurrentDay = Calendar.getInstance().get(Calendar.DATE);			

	// end defect 10610		
	
	private final String CURRENT_REC_BLANK = 
		"Current record is blank , previous record information below:";
	private final String BACTH_DATE = "BatchDate = ";
	private final String DOC_NO = "DocNo = " ;
	private final String ERENEW_INDI = "ERenwlRteIndi = ";
	private final String EXEMPT_INDI = 	"ExmptIndi = " ;
	private final String ORG_NO = "Orgno = " ;
	private final String RECPT_EMAIL = "RecpntEMail = " ;
	private final String REG_CLS_CODE =	"RegClassCd = " ;
	private final String BLANK_ROW = "blank row, previous row = ";	
	
	private final String EMAIL_LIST_ERROR = "Error retrieving email list";
	private final String EMAIL_SEND_ERROR = "Error sending email to: "	;
	private final String EMAIL_INDI_UPDATE_ERROR = "Error updating email Indicator"
		+ " after sending the email to: ";
	private final String EMAIL_NOT_SENT =
		"Below is a list of Ereminder emails that could not be sent:<br>";
	private final String EMAIL_ADDRESS = "Email address = ";
	private final String CUSTOMER_NAME = "   Customer name = "; 
	private final String DOCNO = "   DOC No = "; 
	private final String VIN = "   VIN = ";
	private final String BREAK = "<br>";
	private final String[] csToEmailsBounceBacks =
		new String[] {
			"TSD-RTS-POS@txdmv.gov",
			// defect 10614 
			// "VTR_RTS-EReminder@txdmv.gov"};
			SystemProperty.getEReminderEmail()};
			// end defect 10614	
	private final String[] csToEmailsBounceBacksTest =
		new String[] {
			"Bob.L.Brown@txdmv.gov"};					
		
	/**
	 * EReminderDataProcess.java Constructor
	 * 
	 * 
	 */
	public EReminderDataProcess()
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
			ciEReminderDate = caEReminderDate.getYYYYMMDDDate();
			caEReminderDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					ciEReminderDate);
			// defect 10807 - change to using csWebURL for images
			if (CommunicationProperty
				.getRegion()
				.equalsIgnoreCase("desktop")) 
			{
//				csImageURL = "localhost:8080";
				csWebURL = "http://localhost:9081";
			} 
			else 
			{ 
				if (CommunicationProperty.getRegion().equals("prod")) 
				{
//					csImageURL = "wt-rts-appl:86";
					csWebURL = "https://rts.texasonline.state.tx.us";
				} 
				else 
				{
//					csImageURL = "wt-rts-ts1:8588";
					csWebURL = "https://stage.rts.texasonline.state.tx.us";
				}
			}
			
			// end defect 10807-end change to using csWebURL for images
			processEReminderFile();
			
			insertIntoRenwlEmail();
			
			ciSuccessIndi = 1;
			caProcsComplTimeStmp = new RTSDate();
			updateEReminderHstry();
			
			Vector lvSendErrors = sendEmail();
			
			if (!lvSendErrors.isEmpty())
			{
				getEmailErrors(lvSendErrors);		
			}

			if (ssEReminderFile.equals(EMPTY_STRING))
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
						// defect 10848
						// throw aeRTSEx;
						if (x == 2)
						{
							throw createMsg(
								ErrorsConstant
									.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
									aeRTSEx.getMessage());
						}
						if (aeRTSEx.getCode()==ErrorsConstant
								.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR)
						{
							continue;
						}
						// end defect 10848
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
			BatchLog.write(END_OF + EREMINDER_FILE_PROCESSING);
			BatchLog.write(DASHES);
		}
	}
	/**
	 * Method used to check for email errors.
	 * 
	 * @param avSendErrors Vector
	 * 
	 */	
	private void getEmailErrors(Vector avSendErrors)
	{		
		StringBuffer lsMessage = new StringBuffer(EMAIL_NOT_SENT);
		lsMessage.append(BREAK);
		for (int x = 0; x < avSendErrors.size(); x++)
		{
			RenewalEMailData laRenewalEMailData =
				(RenewalEMailData) avSendErrors.get(x);
				lsMessage	
				.append(EMAIL_ADDRESS)
				.append(laRenewalEMailData.getRecpntEMail())
				.append(BREAK)
				.append(CUSTOMER_NAME)
				.append(laRenewalEMailData.getWindwAddrName1())
				.append(BREAK)
				.append(DOCNO)
				.append(laRenewalEMailData.getDocNo())
				.append(BREAK)
				.append(VIN)
				.append(laRenewalEMailData.getVin())
				.append(BREAK)
				.append(BREAK);	
		}
		// defect 10610										
		// EmailUtil laEmailService = new EmailUtil();
		EmailUtilEReminders laEmailService = new EmailUtilEReminders();
		// end defect 10610
		boolean lbEmailSent = laEmailService.sendEmail(
				csToEmailsBounceBacks,
				csEBounceBacksSubject,
				lsMessage.toString());
		if (!lbEmailSent)
		{
			BatchLog.write(
				EMAIL_SEND_ERROR
				+ csToEmailsBounceBacks);
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
			
			System.out.println("Connect and login: present working dir = " + caFTPClient.printWorkingDirectory());
		
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
			// defect 10610
			// defect 10922
/*			if (CommunicationProperty.getRegion().equals(
					CommunicationProperty.DESKTOP))
			{
				caFTPClient.changeWorkingDirectory(EREMINDER_TEST_DIR);
			} else
			{
				caFTPClient.changeWorkingDirectory(EREMINDER_DIR);
			}*/
			// end defect 10610
			if (CommunicationProperty.getRegion().equals(CommunicationProperty.TXT_PROD))
			{
				caFTPClient.changeWorkingDirectory(EREMINDER_DIR);
			}
			else
			{
				caFTPClient.changeWorkingDirectory(EREMINDER_TEST_DIR);
			}
			// end defect 10922
			System.out.println("Connect and login: present working dir = " + caFTPClient.printWorkingDirectory());
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
				EReminderDataProcess
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
						+ EREMINDER_PROPS_FILE);
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
	 * comptroler to retrieve the EREMINDER file and save the file.
	 * 
	 * We will be saving iterations of this file for debug purposes.
	 * If the number of days that we keep a file needs to be increased
	 * we just increase the NUM_TURN_AROUND_FILES_SAVED value.
	 * 
	 * @return String
	 * @throws RTSException
	 */
	private Vector getEReminderFile() throws RTSException
	{
		String lsEReminderFilePath = null;
		Vector lvEReminderFilePath = new Vector();
	
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
					lsEReminderFilePath =
						retrieveFile(
							caFTPClient,
						carrFiles[liFileCnt]);
					lvEReminderFilePath.add(lsEReminderFilePath);
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
		return lvEReminderFilePath;
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
	 * Used for inserting into rts_renwl_email_hstry.
	 * 
	 */
	private void insertIntoHstry()
	{
		try
		{
			BatchLog.write(DASHES);
			caDBAccess = new DatabaseAccess();
			caDBAccess.beginTransaction();
			caRenewalEMailHstryAccess =
				new RenewalEMailHstry(caDBAccess);
			ciReqId =
				caRenewalEMailHstryAccess.insRenewalEMailHstry(
					new RTSDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			BatchLog.write(
				INSERTING_INTO_HSTRY
					+ ciReqId);
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() == 0)
			{
				aeRTSEx.setCode(
					ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR);
			}
			logError(
				aeRTSEx,
				INSERT_INTO_HSTRY_ERROR);	

			try
			{
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx1.printStackTrace();
			}				
			System.exit(1);
		}
	}

	/**
	 * Loads the E-mailReminder file and puts the Special Plate 
	 * Transactions into a vector.
	 * 
	 * @param asFile String
	 * @throws RTSException
	 */
	private Vector loadEReminderFile(String asFile) throws RTSException
	{
		BatchLog.write(START_OF + LOAD_EREMINDER_FILE);
		String lsStrLine = EMPTY_STRING;
		int liNumOfRecsRead = 0;
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
			String lsCurrentColumnData = EMPTY_STRING;
//			int liDataposition = 0;
//			int liCountyNo = 0;
//			StringTokenizer lsRenewalEMailData = null;
			RenewalEMailData laRenewalEMailData = null;
		
			while ((lsStrLine = laBr.readLine()) != null)
			{
				if (lsStrLine.equals(EMPTY_STRING))
				{
					BatchLog.write(
						CURRENT_REC_BLANK);
					RenewalEMailData laREMD =
						(
							RenewalEMailData) cvEReminderTmp
								.get(
							liNumOfRecsRead - 1);
					BatchLog.write(
						BACTH_DATE
							+ laREMD.getBatchDate());
					BatchLog.write(
						DOC_NO + laREMD.getDocNo());
					BatchLog.write(
						ERENEW_INDI 
							+ laREMD.getERenwlRteIndi());
					BatchLog.write(
						EXEMPT_INDI+ laREMD.getExmptIndi());
					BatchLog.write(
						ORG_NO + laREMD.getOrgNo());
					BatchLog.write(
						RECPT_EMAIL + laREMD.getRecpntEMail());
					BatchLog.write(
						REG_CLS_CODE + laREMD.getRegClassCd());
					BatchLog.write(
						BLANK_ROW
							+ cvEReminderTmp.get(liNumOfRecsRead - 1));
					continue;		
				}
				
				laRenewalEMailData = new RenewalEMailData();
				String[] larrEmailData = lsStrLine.split(COMMA);
				for (int liDataposition = 0; liDataposition < larrEmailData.length; liDataposition++)
				{				
					switch (liDataposition)
					{
						case 0 :
							// docno	
							laRenewalEMailData.setDocNo(larrEmailData[liDataposition]);
							continue;

						case 1 :
							// VIN
							// defect 10811
							if (!validString(larrEmailData[liDataposition]))
							{
//								throw createMsg(
//									ErrorsConstant
//										.ERR_NUM_VIN_NOT_17_DIGITS,
//										lsStrLine);
								// send them to the county (ERenwlRteIndi=1)
								// until IVTRS lets you renew vehicles (trailers)
								// with no VIN 
								laRenewalEMailData.setERenwlRteIndi(1);			
							}
							// end defect 10811
							laRenewalEMailData.setVin(larrEmailData[liDataposition]);
							continue;
						case 2 :
							//regpltno
							laRenewalEMailData.setRegPltNo(larrEmailData[liDataposition]);
							continue;
						case 3 :
							// vehclasscd
							laRenewalEMailData.setVehClassCd(larrEmailData[liDataposition]);
							continue;
						case 4 :
							// vehbdytype
							laRenewalEMailData.setVehBdyType(larrEmailData[liDataposition]);
							continue;
						case 5 :
							// vehmk
							if (!validString(larrEmailData[liDataposition]))
							{
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_INVALID_VEHICLE_MAKE,
										lsStrLine);		
							}
							laRenewalEMailData.setVehMk(larrEmailData[liDataposition]);
							continue;
						case 6 :
							// vehmodl
							laRenewalEMailData.setVehModl(larrEmailData[liDataposition]);
							continue;							
						case 7 :
							// vehmodlyr
							if (!validString(larrEmailData[liDataposition]))
							{
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_MODEL_YEAR_NOT_VALID,
										lsStrLine);		
							}							
							laRenewalEMailData.setVehModlYr(Integer.parseInt(larrEmailData[liDataposition]));
							continue;	
						case 8 :
							// regpltcd
							laRenewalEMailData.setRegPltCd(larrEmailData[liDataposition]);
							continue;
						case 9 :
							// regexpmo
							if (!validString(larrEmailData[liDataposition]))
							{
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_INVALID_REG_EXP_MO,
										lsStrLine);		
							}
							laRenewalEMailData.setRegExpMo(Integer.parseInt(larrEmailData[liDataposition]));
							continue;
						case 10 :
							// regstkrcd
							laRenewalEMailData.setRegStkrCd(larrEmailData[liDataposition]);
							continue;
						case 11 :
							// rescomptcntyno
						if (!validString(larrEmailData[liDataposition]))
						{
							throw createMsg(
								ErrorsConstant
									.ERR_NUM_DEPOSIT_COUNTY_NUMBER_ERROR,
									lsStrLine);		
						}
							laRenewalEMailData.setResComptCntyNo(Integer.parseInt(larrEmailData[liDataposition]));
							continue;
						case 12 :
							// organizations no
							laRenewalEMailData.setOrgNo(larrEmailData[liDataposition]);
							continue;
						case 13 :
							// regclasscd
							laRenewalEMailData.setRegClassCd(Integer.parseInt(larrEmailData[liDataposition]));
							continue;
						case 14 :
							// exemptindi
							laRenewalEMailData.setExmptIndi(Integer.parseInt(larrEmailData[liDataposition]));
							continue;
						case 15 :
							// regrenwlexpyear
							if (!validString(larrEmailData[liDataposition]))
							{
								throw createMsg(
									ErrorsConstant
										.ERR_NUM_INVALID_REG_EXP_YR,
										lsStrLine);		
							}
							laRenewalEMailData.setRegRenwlExpYr(Integer.parseInt(larrEmailData[liDataposition]));
							continue;
						case 16 :
							// recpntemail
							laRenewalEMailData.setRecpntEMail(larrEmailData[liDataposition]);
							continue;
						case 17 :
							// windwaddrname1
							laRenewalEMailData.setWindwAddrName1(larrEmailData[liDataposition]);
							continue;
						case 18 :
							// windwaddrname2
							laRenewalEMailData.setWindwAddrName2(larrEmailData[liDataposition]);
							continue;
						case 19 :
							// erenwlindi
							// defect 10811
							if (laRenewalEMailData.getERenwlRteIndi() < 1)
							{
								laRenewalEMailData.setERenwlRteIndi(Integer.parseInt(larrEmailData[liDataposition]));
							}
							// end defect 10811
							continue;
						default :
						throw createMsg(
							ErrorsConstant
								.ERR_NUM_DEPOSIT_WRONG_NUM_FIELDS,
							lsStrLine);		
					}
				}
				liNumOfRecsRead = liNumOfRecsRead + 1;
				laRenewalEMailData.setBatchDate(caEReminderDate);
				cvEReminderTmp.addElement(laRenewalEMailData);
			}
			laRenewalEMailData.setBatchDate(caEReminderDate);
			BatchLog.write(END_OF + LOAD_EREMINDER_FILE);
			BatchLog.write(
				"number of recs for "
					+ caEReminderDate
					+ " = "
					+ cvEReminderTmp.size());
			return cvEReminderTmp;
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
							+ EREMINDER_PROPS_FILE);
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
				BatchLog.write(EREMINDER_PROPS_FILE + FILE_NOT_FOUND);
				throw new FileNotFoundException(
					EREMINDER_PROPS_FILE + FILE_NOT_FOUND);		
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
		BatchLog.write(ERROR_DURING + EREMINDER_FILE_PROCESSING);
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
//		caEReminderDate = null;
		ciErrMsgNo = aeRTSEx.getCode();
		ciTransCnt = Integer.MIN_VALUE;
		ciSuccessIndi = 0;
		caTmpInsTimeStmp = null;
		caProcsComplTimeStmp = null;

		try
		{
			updateEReminderHstry();
		}
		catch (RTSException aeRTSEx1)
		{
			aeRTSEx1.printStackTrace();
			BatchLog.error(
				TEMP_UPDATE_ERROR
					+ aeRTSEx.getCode());
			BatchLog.error(aeRTSEx1.getMessage());
			BatchLog.error(aeRTSEx1.getDetailMsg());
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
		EReminderDataProcess laERDP = new EReminderDataProcess();
		laERDP.getStaticCache();
		
		if (aarrArgs.length > 0
			&& aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
		{
			BatchLog.write(
			ONLY_SENDING_EMAIL + aarrArgs[0]);
			try
			{
				laERDP.processEmail(laERDP);
			}
			catch (RTSException aeRTSEx)
			{
				laERDP.logError(aeRTSEx, "");
			}
		}
		else
		{
			laERDP.beginProcessing();
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
	 * sendEmail only. Called from main when parms are passed to this 
	 * process
	 * 
	 */
	private void processEmail(EReminderDataProcess aaEReminderDataProcess)
		throws RTSException
	{
		try
		{
			// defect 10807
			// need the below check if the main method was called with 
			// a parm to send emails only 
			if (csWebURL.equals(""))
			{
				if (CommunicationProperty
					.getRegion()
					.equalsIgnoreCase("desktop")) 
				{
					csWebURL = "http://localhost:9081";
				} 
				else 
				{ 
					if (CommunicationProperty.getRegion().equals("prod")) 
					{
						csWebURL = "https://rts.texasonline.state.tx.us";
					} 
					else 
					{
						csWebURL = "https://stage.rts.texasonline.state.tx.us";
					}
				}
			}
			// end defect 10807	
			Vector lvSendErrors = aaEReminderDataProcess.sendEmail();
			if (!lvSendErrors.isEmpty())
			{
				aaEReminderDataProcess.getEmailErrors(lvSendErrors);		
			}			
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
			//logError(aeRTSEx, EMPTY_STRING);
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
						carrFiles[liFileCnt].indexOf("."));
				caFTPClient.rename(
					carrFiles[liFileCnt],
					ARCHIVE_DIR + lsFilename + ciEReminderDate + DEL_FILE_TYPE);
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
			// defect 10848
			catch(SocketException leSE)
			{
				connectAndLogin();
				leSE.printStackTrace();
				// defect 10848
				// throw aeIOEx;
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
						leSE.getMessage());	
			}
			// end defect 10848
			catch (IOException aeIOEx)
			{
				aeIOEx.printStackTrace();
				// defect 10848
				// throw aeIOEx;
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR,
						aeIOEx.getMessage());	
				// end defect 10848
			}
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
	private void processEReminderFile() throws RTSException
	{
		BatchLog.write(START_OF + EREMINDER_FILE_PROCESSING);
	
		Vector lvEReminderFiles = new Vector();
		// when ssEReminderFile is blank, get the E-mail file from
		// the server txdot-hq44 (T:\ drive)
		// otherwise, a filename was passed to this process, so get 
		// that file another location. 
		if (ssEReminderFile.equals(EMPTY_STRING))
		{
			try
			{
				// Load the FTP Properties file
				loadFTPProperties();
				lvEReminderFiles = getEReminderFile();
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
		else
		{
			lvEReminderFiles.add(ssEReminderFile);
		}
		if (lvEReminderFiles.size() > 0)
		{
			try
			{
				Collections.sort(lvEReminderFiles);			
				caDBAccess = new DatabaseAccess();
				caDBAccess.beginTransaction();
				caRenewalEMailTmpAccess =
					new RenewalEMailTmp(caDBAccess);
				// delete all rts_renwl_email_tmp recs	
				int liRetRows =
					caRenewalEMailTmpAccess
						.delRenewalEMailTmp();
				BatchLog.write(
					TEMP_DEP_FILE_DELETED
						+ liRetRows);
				caDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// Load the e-mail records into a Vector.
				for (int liDepostFile = 0;
					liDepostFile < lvEReminderFiles.size();
					liDepostFile++)
				{
					insertIntoHstry();
					loadEReminderFile(
						(String) lvEReminderFiles.get(liDepostFile));
					insertIntoEmailTmp();
				}
			}
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx.getCode() == 0)
				{
					ciErrMsgNo = 
						ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR;
					aeRTSEx.setCode(
						ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR);
				}
				throw aeRTSEx;
			}
		}
		else
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,
				EREMINDER_FILE_MISSING_OR_BLANK);
		}
	}

	/**
	 * Method insertIntoEmailTmp loads each e-mail file rec into
	 * the rts_renwl_email_tmp
	 * 
	 * @throws RTSException
	 */

	private void insertIntoEmailTmp() throws RTSException
	{
		try
		{
			caDBAccess = new DatabaseAccess();
			caDBAccess.beginTransaction();
			caRenewalEMailTmpAccess =
				new RenewalEMailTmp(caDBAccess);		
			caRenewalEMailTmpAccess
				.insRenewalEMailTmp(
				cvEReminderTmp);
			BatchLog.write(
				TEMP_FILE_ROWS_INSERTED
					+ cvEReminderTmp.size());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
		}

		catch (RTSException aeRTSEx)
		{
			try
			{
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				throw aeRTSEx;
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx1.printStackTrace();
			}
			// defect 10848
/*			finally
			{
				throw aeRTSEx;
			}*/
			// end defect 10848
		}

		try
		{
			//ciErrMsgNo = 0;
			ciTransCnt = cvEReminderTmp.size();
			BatchLog.write(
				"number of ciTransCnt recs for "
					+ caEReminderDate
					+ " = "
					+ ciTransCnt);
			ciSuccessIndi = 0;
			caProcsComplTimeStmp = null;	
			updateEReminderHstry();
			cvEReminderTmp.clear();
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
	 * Method insertIntoRenwlEmail
	 * throws RTSException
	 * 
	 */
	
	private void insertIntoRenwlEmail() throws RTSException
	{

		BatchLog.write(START_OF + EMAIL_INSERT_PROCESS);
		try
		{
			caDBAccess = new DatabaseAccess();
			caDBAccess.beginTransaction();
			caRenewalEMailAccess = new RenewalEMail(caDBAccess);
			int liNumRecsInserted =
			caRenewalEMailAccess.insRenewalEMail();
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			BatchLog.write(NUM_ROWS_INSERTED + liNumRecsInserted);
			
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() == 0)
			{
				ciErrMsgNo = 
					ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR;
				aeRTSEx.setCode(
					ErrorsConstant.ERR_NUM_RECON_HSTRY_INSERT_ERROR);
			}
			try
			{
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				// defect 10848
				BatchLog.write(INSERT_ERROR);		
				throw aeRTSEx;
				// end defect 10848
			}
			catch (RTSException aeEx)
			{
				aeEx.printStackTrace();
				throw aeEx;
			}
			// defect 10848
/*			finally
			{
				BatchLog.write(INSERT_ERROR);		
				throw aeRTSEx;
			}*/
			// end defect 10848
		}
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
			laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_TO,
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
	 * Method updateEReminderHstry
	 * throws RTSException
	 */
	private void updateEReminderHstry()
		throws RTSException
	{
		try
		{
			RenewalEMailHstryData laRenwlEMHD =
				new RenewalEMailHstryData();
			laRenwlEMHD.setProcsComplTimestmp(caProcsComplTimeStmp);
			laRenwlEMHD.setRenwlEMailReqId(ciReqId);
			laRenwlEMHD.setErrMsgNo(ciErrMsgNo);
			laRenwlEMHD.setRenwlCount(ciTransCnt);
			laRenwlEMHD.setSuccessfulIndi(ciSuccessIndi);
			laRenwlEMHD.setBatchDate(caEReminderDate);
			laRenwlEMHD.setTmpInsrtTimestmp(new RTSDate());
			caDBAccess = new DatabaseAccess();			
			caDBAccess.beginTransaction();
			caRenewalEMailHstryAccess =
				new RenewalEMailHstry(caDBAccess);
			caRenewalEMailHstryAccess
				.updRenewalEMailHstry(
				laRenwlEMHD);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				BatchLog.write(
					RENWL_HSTRY_UPDATE_ERROR);
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				// defect 10848
				throw aeRTSEx;
				// end defet 10848
			}
			catch (RTSException aeRTSEx1)
			{
				BatchLog.write(
					HSTRY_ROLLBACK_ERROR);
				// defect 10848
				throw aeRTSEx1;
				// end defet 10848
			}
			// defect 01848
/*			finally
			{
				throw aeRTSEx;
			}*/
			// end defect 10848
		}
		catch (Exception aeEx)
		{
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_DATE_NOT_YYYYMMDD,
				aeEx.getMessage());		
		}
	}
	
	/**
	 * Method sendMail
	 * throws RTSException
	 */
	
	private Vector sendEmail() throws RTSException
	{
		Vector lvRenewalEMailList = new Vector();
		Vector lvRenewalEMailErrorList = new Vector();
		try
		{
			caDBAccess = new DatabaseAccess();
			caDBAccess.beginTransaction();
			caRenewalEMailAccess = new RenewalEMail(caDBAccess);
			lvRenewalEMailList =
				caRenewalEMailAccess.qryProcsTimeStmp();
			if (lvRenewalEMailList.isEmpty())
			{
				throw createMsg(
					ErrorsConstant.ERR_NUM_ERROR_RETRIEVING_EMAIL_LIST,
					EMPTY_STRING);	
				
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

		try
		{
			for (int i = 0; i < lvRenewalEMailList.size(); ++i)
			{
				caRenewalEMailData = (RenewalEMailData) lvRenewalEMailList.elementAt(i);
				
//				boolean lbEmailSent = sendEmail(laEmailData);
				// defect 10610
				
				if (ciCurrentDay < 15)
				{
					if (caRenewalEMailData.getERenwlRteIndi()==1)
					{
						csMessage = cs3WeekOfflineMessage;
						System.out.println(
							"cs3WeekOfflineMessage for plate "
								+ caRenewalEMailData.getRegPltNo()
								+ " and vin "
								+ caRenewalEMailData.getVin());		
					}
					else
					{
						csMessage = cs3WeekOnlineMessage;
						System.out.println(
							"cs3WeekOnlineMessage for plate "
								+ caRenewalEMailData.getRegPltNo()
								+ " and vin "
								+ caRenewalEMailData.getVin());		
					}
					csEReminderSubject = csEReminder3WeekSubject;	
				}
				else
				{
					if (caRenewalEMailData.getERenwlRteIndi()==1)
					{
						csMessage = cs1WeekOfflineMessage;	
						System.out.println(
							"cs1WeekOfflineMessage for plate "
								+ caRenewalEMailData.getRegPltNo()
								+ " and vin "
								+ caRenewalEMailData.getVin());			
					}
					else
					{
						csMessage = cs1WeekOnlineMessage;
						System.out.println(
							"cs1WeekOnlineMessage for plate "
								+ caRenewalEMailData.getRegPltNo()
								+ " and vin "
								+ caRenewalEMailData.getVin());				
					}
					csEReminderSubject = csEReminder1WeekSubject;
				}
				// defect 10807 - use csWebURL for images,not csImageURL 
				csMessage = csMessage.replaceAll(
					"#imagehost#", csWebURL);	
				// end defect 10807	
				csMessage = csMessage.replaceAll(
					"#webhost#", csWebURL);									
				// EmailUtil laEmailService = new EmailUtil();
				EmailUtilEReminders laEmailService = new EmailUtilEReminders();
				// end defect 10610
				boolean lbEmailSent = laEmailService.sendEmail(
						caRenewalEMailData.getRecpntEMail(),
						csEReminderSubject,
						buildMessage(caRenewalEMailData));
				if (lbEmailSent)
				{
					// only if succeed, do update the indicator
					try
					{
						caDBAccess = new DatabaseAccess();
						caDBAccess.beginTransaction();
						caRenewalEMailAccess = new RenewalEMail(caDBAccess);
						caRenewalEMailAccess.updRenewalEMail(
							caRenewalEMailData.getDocNo());
					}
					catch (Exception aeEx)
					{
						// Cannot update the database after sending out the
						// email.  Write to batch log.
						caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
						BatchLog.write(
							EMAIL_INDI_UPDATE_ERROR
								+ caRenewalEMailData.getRecpntEMail());
						throw createMsg(
							ErrorsConstant
								.ERR_NUM_DEPOSIT_RECON_HSTRY_UPDATE_ERROR,
								EMPTY_STRING);
					}
					finally
					{
						caDBAccess.endTransaction(DatabaseAccess.NONE);
					}
				}
				else
				{
					BatchLog.write(
						EMAIL_SEND_ERROR
							+ caRenewalEMailData.getRecpntEMail());
					lvRenewalEMailErrorList.add(lvRenewalEMailList.get(i));		
//					throw createMsg(
//						ErrorsConstant
//							.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,
//							 EMPTY_STRING);				
				}
			}
		}
		catch (RTSException aeEx)
		{
			throw aeEx;
		}		
		return lvRenewalEMailErrorList;
	}
	
	/**
	 * Method buildMessage
	 * 
	 * @param aaRenewalEMailData RenewalEMailData
	 * @throws RTSException
	 */	
	public String buildMessage(RenewalEMailData aaRenewalEMailData) throws RTSException
	{
		String lsMessage = csMessage;
		try
		{
			lsMessage = lsMessage.replaceFirst(
				"#vehmodlyr#",
				Integer.toString(aaRenewalEMailData.getVehModlYr()));
			String lsVehMk = "";
//			VehicleMakesCache laVehicleMakesCache = new VehicleMakesCache();
			Vector lvMakeDesc = VehicleMakesCache.getVehMkDesc(aaRenewalEMailData.getVehMk());
			// defect 10736
			//if (lvMakeDesc.size() > 1)
			if (lvMakeDesc.isEmpty() || lvMakeDesc.size() > 1)
			// end defect 10736
			{
				lsVehMk = aaRenewalEMailData.getVehMk(); 
			}
			else
			{
				lsVehMk = ((lvMakeDesc.elementAt(0)).toString()).toLowerCase();
				lsVehMk = lsVehMk.substring(0,1).toUpperCase() + lsVehMk.substring(1);
			}
			lsMessage = lsMessage.replaceFirst(
				"#vehmk#",lsVehMk);
			// defect 10811
			if (aaRenewalEMailData.getVin().length() > 0)
			{
				// defect 10970
/*				lsMessage = lsMessage.replaceFirst(
					"#vin#",
					"(the last four digits of your VIN are " +  
					aaRenewalEMailData.getVin().substring(
						aaRenewalEMailData.getVin().length() - 4,
						aaRenewalEMailData.getVin().length())
						+ ") ");*/
				if (aaRenewalEMailData.getVin().length() < 4)
				{
					lsMessage = lsMessage.replaceFirst(
						"#vin#",
						"(your VIN is " +  
						aaRenewalEMailData.getVin()
							+ ") ");
					
				}
				else
				{	
					lsMessage = lsMessage.replaceFirst(
						"#vin#",
						"(the last four digits of your VIN are " +  
						aaRenewalEMailData.getVin().substring(
							aaRenewalEMailData.getVin().length() - 4,
							aaRenewalEMailData.getVin().length())
							+ ") ");
				}
			// end defect 10970
			}
			else
			{
				lsMessage = lsMessage.replaceFirst(
					"#vin#","(your vehicle has no vin) ");		
			}
			// end defect 10811		
			lsMessage = lsMessage.replaceFirst(
				"#regexpmo#",
				getMonthName(aaRenewalEMailData.getRegExpMo()));
			lsMessage = lsMessage.replaceFirst(
				"#regexpyr#",
				Integer.toString(aaRenewalEMailData.getRegRenwlExpYr()));
			// defect 10610		
//			if (aaRenewalEMailData.getERenwlRteIndi()==0)
//			{
//				lsMessage = lsMessage.replaceFirst(
//				"#txo#","online through " +					"<a href=http://www.texas.gov>texas.gov</a> or");
//			}
//			else
//			{
//				lsMessage = lsMessage.replaceFirst("#txo#","");
//			}				
			// end defect 10610									
			String lsCountyname = (getCountyName(aaRenewalEMailData.getResComptCntyNo())).toLowerCase();
			int liSpacePlace = lsCountyname.indexOf(" ");
			if (liSpacePlace > 0)
			{
				String lsFirstName = lsCountyname.substring(0,liSpacePlace);
				String lsLastName = lsCountyname.substring(liSpacePlace + 1,lsCountyname.length());
				lsFirstName = lsFirstName.substring(0,1).toUpperCase() + lsFirstName.substring(1);
				lsLastName = lsLastName.substring(0,1).toUpperCase() + lsLastName.substring(1);
				lsCountyname = lsFirstName + " " + lsLastName;
			}
			else
			{
				lsCountyname = lsCountyname.substring(0,1).toUpperCase() + lsCountyname.substring(1);
			}
			if (lsCountyname.startsWith("Mc"))
			{
				lsCountyname = "Mc" + lsCountyname.substring(2,3).toUpperCase() + lsCountyname.substring(3);
			}

			lsMessage = lsMessage.replaceAll(
				"#countyname#", lsCountyname);	
				
//			System.out.println("county name = " + lsCountyname);						
		}
		catch (Exception aeEx)
		{
			throw createMsg(
				ErrorsConstant.ERR_NUM_BUILD_EMAIL_MESSAGE_ERROR,
				aeEx.getMessage());	
		}		
		return lsMessage;
	}
	
	/**
	 * Method getCountyName
	 * throws RTSException
	 */
	private String getCountyName(int aiCountyNo) throws RTSException
	{
		String lsOfficeName = "";
		caDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN 
			caDBAccess = new DatabaseAccess();
			caDBAccess.beginTransaction();
			OfficeIds laOfficeIds = new OfficeIds(caDBAccess);
			lsOfficeName =
				laOfficeIds.qryOfficeId(aiCountyNo);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				throw aeRTSEx;
			}
			catch (RTSException aeRTSEx2)
			{
				throw aeRTSEx2;
			}
		}
		return lsOfficeName;
	}
	
	/**
	 * Method getMonthName - convert number to text
	 * @param aiMonthyNo int
	 * @return String
	 */
	private String getMonthName(int aiMonthyNo) throws RTSException
	{
		//String lsMonthName = "";
		switch (aiMonthyNo)
		{
			case 1 :
				return "January";
			case 2 :
				return "February";
			case 3 :
				return "March";
			case 4 :
				return "April";
			case 5 :
				return "May";
			case 6 :
				return "June";
			case 7 :
				return "July";
			case 8 :
				return "August";
			case 9 :
				return "September";
			case 10 :
				return "October";
			case 11 :
				return "November";
			case 12 :
				return "December";
			default :
				return Integer.toString(aiMonthyNo);
		}				
	}
	/**
	 * check for valid String data
	 * @param asColumnData String
	 * @return boolean
	 */
	
	private boolean validString(String asColumnData)
	{
		if (asColumnData==null || asColumnData.equals("") || asColumnData.length()==0)
		{
			return false;	
		}
		return true;
	} 
}
