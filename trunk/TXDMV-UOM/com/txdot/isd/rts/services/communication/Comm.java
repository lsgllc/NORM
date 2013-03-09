package com.txdot.isd.rts.services.communication;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.event.CommEvent;
import com.txdot.isd.rts.services.util.event.CommListener;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 *
 * Comm.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		9/10/2001	Handles Server down
 * R Hicks      2/22/2002   Compress data objects before sending to 
 *							network
 * N Ting		4/16/2002	Fix CQU100003520, catch server down scenario
 * RS Chandel   5/21/2002   Fix CQU100003973, catch EOF exception when 
 *							server fails like out of memory
 * Ray Rowehl	8/07/2002	Fix CQU100004583, write db and rr start back 
 * R Hicks, Mike			to operation messages. Only put the messages 
 *							where an exception is not thrown.
 * S Govindappa 10/03/2002  Fixing 4800. Added code in sendToServer() to
 *							block all the database calls to server when 
 *							database is down except calls from sendCache.
 * J Rue 		10/03/2002	Defect 4799, Add to the log file when the 
 *							return string from the server is not 
 *							formatted correctly. method StringToObj()
 * J Rue		11/01/2002	Defect 4799, Change Log.APPLICATION to 
 *							Log.SQL_EXCP. This will write the error time
 *							it occurs.
 * S Govindappa 01/29/2003  Fixing Defect # 5320. Added log information 
 *							on data being sent to server and received 
 *							from server by making changes to 
 *							sendToServer(..) and processRequest(..).
 * S Govindappa 02/10/2003  Fixing defect # 5235. Added timeouts in 
 *							processRequest(..) for checking server 
 *							connectivity before sending any information 
 *							to server.
 * S Govindappa 02/28/2003  Fixing defect #5625. Changed StringToObj(..)
 *							to log the string value in case of a 
 *							exception while converting from a string to 
 *							Hex(hexadecimal) Object.
 * Ray Rowehl	03/03/2003	Fixing defect 5565.  catch numberformat and 
 *							report it. Check for unexpected response 
 *							code from URL.
 * Ray Rowehl	06/15/2003	Exclude more events from updating Database.
 *							Do not want to trigger a DB Up by accident.
 *							update fireCommEvent(), sendToServer().
 *							add isNotUpdatingDB()
 *							deprecate isUpdatingDB()
 *							defect 6110
 * Ray Rowehl	07/09/2003	check for null before checking return code 
 *							from server. try to parse the object out of 
 *							the return string.
 *							modify processRequest()
 *							defect 6238
 * Ray Rowehl	07/17/2003	If an exception is returned, write it to the
 *							log
 *							modify processRequest()
 *							defect 6059
 * Ray Rowehl	07/23/2003	Correct logic problem introduced in 6110.
 *							Need to throw DB Down on DB Down status if 
 *							object is not a vector. Allow SendCache to 
 *							do its queries. Allow admin cache to go 
 *							through on server down, but not subcon cache.
 *							Return null instead of throwing an exception 
 *							on db down.
 *							modify sendToServer()
 *							defect 6110
 * Ray Rowehl	07/29/2003	Found that returning null caused other 
 *							problems. login did not handle cleanly.
 *							validateInvNum interpreted differently. Both
 *							of these methods expected an exception to be
 *							returned if DB Down. Put that back in.  
 *							Opened defect 6391 to cover request for
 *							exception clean up.
 *							modify sendToServer()
 *							defect 6110
 * Ray Rowehl	07/30/2003	Had to allow batch to go through. DB is Down
 *							when first starting.
 *							modify isNotUpdatingDB()
 *							defect 6110
 * B Brown	    08/20/2003	Defect 4885 Modified isNotUpdatingDB():
 *                          allow batch to process Special Plate 
 *							Internet Address Chang report by checking 
 *							for moduleName == GeneralConstant.
 *							INTERNET_REG_REN_PROCESSING && functionId ==
 *							com.txdot.isd.rts.services.webapps.util.
 *							constants. RegRenProcessingConstants.
 *							GET_SPCL_PLT_ADDR_CHG_RPT
 * Ray Rowehl	08/26/2003	On Mondays and Sundays, we query number of 
 *							title batches to see if run is needed
 *							modify isNotUpdatingDB()
 *							defect 6527
 * Ray Rowehl	09/26/2003	On "HTTP" not "OK" receives, clean up 
 *							parsing of the object so that it can be 
 *							handled.
 *							modified processRequest()
 *							defect 6612  Ver 5.1.5
 * Ray Rowehl	11/04/2003	Do not send MF Up unless we got successful 
 *							MF Data. Previous code would send MF Up if 
 *							we were MF Down and the RTSException 
 *							received was not MF Down
 *							Modify processTask()
 *							defect 6666  ver 5.1.5 fix 1
 * Jeff S.		02/25/2004	Added function ID to let through to the
 *							server when DB is down.  This is for
 *							redirecting batch outside of the current
 *							substation. (county wide). Used in Print.
 *							setConnectionStatus(,).
 *							delete isUpdatingDB(int, int).
 *							modify isNotUpdatingDB(int, int)
 *							defect 6848, 6894 Ver 5.1.6
 * Jeff S.		03/01/2004	ProdStatus of 2 not used anymore so changed
 *							a check to see (if prodstatus == 2) to (if 
 *							prodstatus > 0).
 *							modify isNotUpdatingDB(int, int)
 *							defect 6771 Ver 5.1.6
 * Jeff S.		03/05/2004	Added function ID to let through to the
 *							server when DB is down.  This function is
 *							for updating the RTS_WS_Status with the
 *							current status of the workstion if it is
 *							different than what the DB shows.
 *							modify isNotUpdatingDB(int, int)
 *							defect 6918 Ver 5.1.6
 * Ray Rowehl	09/20/2004	On Server Down, do not attempt to send a
 *							small vector through.  It is not trxcache.
 *							modify sendToServer().
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/29/2004	Parse out any blanks that are in the
 *							response string.  This is a new flavor of
 *							HTTP error we are getting now.  Maybe
 *							related to WAS 4.0.7 upgrade.
 *							Also added main to allow testing of error
 *							handling outside of the application.
 *							add main()
 *							modify processRequest()
 *							defect 7466 Vew 5.2.1
 * Ray Rowehl	06/17/2005	Code Cleanup
 * 							format source, organize imports,
 * 							rename fields, use Throwable instead of 
 * 							Exception.
 * 							delete dt, mfAccessors
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	07/16/2005	Additional constants work.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	10/14/2005	Removed reference to MAX_LOG
 * 							modify isNotUpdatingDB()
 * 							defect 7897 Ver 5.2.3
 * Ray Rowehl	04/05/2006	Add more precise logging on various 
 * 							exceptions in processRequest().
 * 							add MSG_INTERRUPTED_EXCEPTION, 
 * 								MSG_IO_EXCEPTION, 
 * 								MSG_MALFORMED_URL,
 * 								MSG_NO_ROUTE, 
 * 								MSG_SOCKET_EXCEPTION
 * 							modify processRequest()
 * 							defect 8663 Ver 5.2.3
 * Ray Rowehl	06/01/2006	Make sure Server Connection is closed after 
 * 							receiving data from the server.
 * 							This is a recommendation from IBM coming
 * 							out of looking into the chunked issue.
 * 							modify processRequest()
 * 							defect 8811 Ver 5.2.3
 * Ray Rowehl	11/07/2006	Setup the HTTPS string as needed.
 * 							add HTTPS_STRING
 * 							add ssHTTPString
 * 							modify processRequest()
 * 							defect 8248 Ver Exempts
 * K Harrell	05/09/2007	enable new batch report to process @ startup
 * 							modify isNotUpdatingDB()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/06/2009  Remove reference to Special Plates Batch 
 * 							 report.  Not implemented. 
 * 							modify isNotUpdatingDB() 
 * 							defect 9941 Ver Defect_POS_D 
 * K Harrell	02/06/2009  Enable new Itrnt Deposit Recon Report to 
 * 							process @ startup. 
 * 							modify isNotUpdatingDB() 
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	08/17/2009	Implement GET_SUBSTA_INFO (vs. GET_SUBSA_INFO_
 * 							modify isNotUpdatingDB()
 * 							defect 8628 Ver Defect_POS_F
 * M Reyes		09/06/2011	Windows 7 wkstations are having time issues
 * 							modify isNotUpdatingDB()
 * 							defect 10986 Ver. 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * This class is responsible for communicating with servlets. 
 * The protocol for communication is encapsulated in this class 
 * so GUI is not aware whether Java serialization is being used
 * or something else.
 *
 * @version	6.8.1	09/06/2009 
 * @author	Micheal Abernethy
 * <br>Creation Date:		08/06/2001  15:37:00
 */

public class Comm
{

	private static final int HEX_DECIMAL_VALUE = 16;
	private static final int HIGH_END_SHIFT = 4;
	private static final int HTTP_RESPONSE_GOOD = 200;

	private static final String ATTEMPTING_TO_SEND_FUNCTION_STRING =
		" and functionId = ";
	private static final String ATTEMPTING_TO_SEND_MODULE_STRING =
		"Attempting Send to Server, moduleName = ";
	private static final String CHUNKED_STRING = "chunked";
	private static final String COMM_LAYER_NULL_MSG =
		"Server returned null value. Caught in Comm layer";
	private static final String CONTENT_WAS_CHUNKED_STRING =
		"Content was chucked! ";
	private static final String DATA_STRING = "&Data=";
	private static final String DB_CONN_DOWN_MSG =
		"Database connection is now down";
	private static final String DB_DOWN_NOT_SENDCACHE_MSG =
		" DB is Down.  Not Sendcache data.  Do not send";
	private static final String DB_DOWN_SEND_QUESTION_MSG =
		" DB is Down.  Checking to see if send should be done.";
	private static final String ERR_MSG_DEHEX_NFE =
		" Comm.deHextoByte got a numberFormatException";
	private static final String ERR_MSG_STRINGS_ARE_NOT_EQUAL =
		"Strings are not equal ";
	private static final String ERR_TXT_COUNT_EQUAL = " Count = ";
	private static final String ERR_TXT_DATA_EQUAL = " data = ";
	private static final String ERR_TXT_STRING_TO_OBJ =
		"Error in StringToObj() ";
	private static final String FUNCTION_ID_STRING = "&FunctionId=";
	private static final String HAD_A_BLANK_MSG =
		" You had a blank in there!!  At position ";
	private static final String HT_PARSING_PROBLEM_MSG =
		" There was a problem handling HT parsing ";
	private static final String HTTP_STRING = "http://";

	// defect 8248
	private static final String HTTPS_STRING = "https://";
	// end defect 8248

	private static final String LOADTEST_FILE_STRING = "LoadTest.dat";
	private static final String MF_UP_MSG =
		"Record Retrieval connection is now up.";
	private static final String MF_DOWN_MSG =
		"Record Retrieval connection is now down";
	private static final String MODULE_NAME_STRING = "ModuleName=";

	// defect 8663
	private static final String MSG_INTERRUPTED_EXCEPTION =
		" Got a InterruptedException";
	private static final String MSG_IO_EXCEPTION = " Got a IOException";
	private static final String MSG_MALFORMED_URL =
		" Got a MalformedURLException";
	private static final String MSG_NO_ROUTE =
		" Got a NoRouteToHostException";
	private static final String MSG_SOCKET_EXCEPTION =
		" Got a SocketException";
	// end defect 8663

	private static final String NOT_200_STRING =
		"Return code is not 200 (OK) ";
	private static final String NOT_VALID_RTS_DATA_STREAM_MSG =
		" Not a valid RTS data stream";
	private static final String POST_STRING = "POST";
	private static final String RECEIVE_FROM_SERVER_MSG =
		"Receive from Server";
	private static final String SRVR_DOWN_MSG =
		"Server connection is now down";
	private static final String STR_BEGIN_CONTENT_STRING = "1f";
	private static final String STR_COLON = ":";
	private static final String STR_FORWARD_SLASH = "/";
	private static final String STR_ZERO = "0";
	private static final String TXT_LENGTH_IS = "Length is ";
	private static final String UNEXPECTED_HTTP_RC_NEWSTR_MSG =
		" Unexpected HTTP Return Code - newstr ";
	private static final String UNEXPECTED_HTTP_RC_DATA_MSG =
		" Unexpected HTTP Return Code - data   ";
	private static final String UNEXPECTED_HTTP_RC_MSG =
		" Unexpected HTTP Return Code - RC     ";

	private static final char[] HEX_CHARS =
		{
			'0',
			'1',
			'2',
			'3',
			'4',
			'5',
			'6',
			'7',
			'8',
			'9',
			'a',
			'b',
			'c',
			'd',
			'e',
			'f' };

	private static boolean sbMFUp = true;
	// To indicate whether this jvm is for server.
	private static boolean sbServer;
	private static boolean sbServerDown = false;

	private static int siModuleName;
	private static int siFunctionId;
	private static int siLastTransactionDate;

	private static String ssServletHost;
	private static String ssServletPort;
	private static String ssServletName;

	// defect 8248
	private static String ssHTTPString;
	// end defect 8248

	private static java.util.Vector svListeners;
	public static java.net.Socket saSocket = null;

	/**
	 * Comm constructor
	 */
	public Comm()
	{
	}
	/**
	 * add Comm listeners
	 * 
	 * @param aaCommListener CommListener
	 */
	public static void addCommListener(CommListener aaCommListener)
	{
		if (svListeners == null)
		{
			svListeners = new Vector();
		}
		if (!svListeners.contains(aaCommListener))
		{
			svListeners.add(aaCommListener);
		}
	}
	/** 
	 * Converts a byte to hex digit and writes to the supplied buffer
	 * 
	 * @param abByte byte
	 * @param asString StringBuffer
	 */
	private static void byte2hex(byte abByte, StringBuffer asString)
	{
		int high = ((abByte & 0xf0) >> HIGH_END_SHIFT);
		int low = (abByte & 0x0f);
		asString.append(HEX_CHARS[high]);
		asString.append(HEX_CHARS[low]);
	}
	/**     
	 * Converts hex string into byte array.
	 * 
	 * @param aaData  String
	 * @return byte[]
	 */
	public static byte[] deHextoByte(String aaData)
	{
		String lsHex;
		Integer laCharValue;
		byte[] larrRet = new byte[aaData.length() / 2];
		int liCount = 0;
		try
		{
			for (int i = 0; i < aaData.length(); i = i + 2)
			{
				lsHex = aaData.substring(i, i + 2);
				laCharValue = Integer.valueOf(lsHex, HEX_DECIMAL_VALUE);
				int liMyInt = laCharValue.intValue();
				larrRet[liCount] = (byte) (0x00ff & liMyInt);
				liCount = liCount + 1;
			}
			return larrRet;
		}
		catch (NumberFormatException aeNFE)
		{
			Log.write(Log.SQL_EXCP, aaData, ERR_MSG_DEHEX_NFE);
			Log.write(Log.SQL_EXCP, aeNFE, ERR_TXT_DATA_EQUAL + aaData);
			Log.write(
				Log.SQL_EXCP,
				aeNFE,
				ERR_TXT_COUNT_EQUAL + liCount + 1);
			System.err.println(
				ERR_MSG_DEHEX_NFE
					+ RTSDate.getCurrentDate()
					+ (new RTSDate()).getClockTime());
			aeNFE.printStackTrace();
			throw aeNFE;
		}
	}
	/**
	 * Fire Comm Event
	 * 
	 * @param aaCE CommEvent
	 */
	public static void fireCommEvent(CommEvent aaCE)
	{
		// make this method public so it can be called from Transaction
		if (svListeners == null)
		{
			svListeners = new Vector();
		}
		for (int i = 0; i < svListeners.size(); i++)
		{
			((CommListener) svListeners.get(i)).handleCommEvent(aaCE);
		}
	}
	/**
	 * Determines if call is coming back from MainFrame
	 * 
	 * @return boolean
	 */
	private static boolean fromMF()
	{
		boolean lbFromMF = false;

		if (siModuleName == GeneralConstant.ACCOUNTING)
		{
			if (siFunctionId
				== AccountingConstant.REMIT_FUNDS_DUE_RECORDS
				|| siFunctionId
					== AccountingConstant
						.RETRIEVE_FUNDS_DUE_SUMMARY_RECORDS
				|| siFunctionId == AccountingConstant.VOID_PAYMENT
				|| siFunctionId
					== AccountingConstant.SEARCH_PAYMENT_RECORDS)
			{
				lbFromMF = true;
			}
		}
		else if (siModuleName == GeneralConstant.INVENTORY)
		{
			if (siFunctionId
				== InventoryConstant.GET_INVENTORY_FROM_MF)
			{
				lbFromMF = true;
			}
		}
		else if (siModuleName == GeneralConstant.TITLE)
		{
			if (siFunctionId == TitleConstant.GET_NUM_DOC_RECORD
				|| siFunctionId == TitleConstant.DELETE_TITLE_IN_PROCESS)
			{
				lbFromMF = true;
			}
		}
		else if (siModuleName == GeneralConstant.MISC)
		{
			if (siFunctionId == MiscellaneousConstant.VOID_TRANSACTION)
			{
				lbFromMF = true;
			}
		}
		return lbFromMF;
	}
	/**
	 * Return last transaction date
	 *  
	 * @return int
	 */

	public static int getLastTransactionDate()
	{
		return siLastTransactionDate;
	}
	/**
	 * Return the Socket value.
	 *  
	 * @return java.net.Socket
	 */

	public static java.net.Socket getSocket()
	{
		return saSocket;
	}
	/**
	 * This method checks to see if the request does not update the 
	 * server.
	 * This allows us to be very restrictive of server access when DB 
	 * Down.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @return boolean
	 */
	private static boolean isNotUpdatingDB(
		int aiModuleName,
		int aiFunctionId)
	{
		// This is all new.  based on isUpdatingDB.
		boolean lbNotUpdatingDB = false;

		// allow record retreival through.
		if (aiModuleName == GeneralConstant.COMMON
			&& aiFunctionId == CommonConstant.GET_VEH)
		{
			lbNotUpdatingDB = true;
		}
		// allow sendcache to get his data to start his process
		else if (
			aiModuleName == GeneralConstant.SYSTEMCONTROLBATCH
				&& aiFunctionId == SystemControlBatchConstant.MAX_TRANS)

			// defect 7897
			// No longer check MAX_LOG
			//				
			//	|| (aiModuleName == GeneralConstant.SYSTEMCONTROLBATCH
			//		&& aiFunctionId
			//		== SystemControlBatchConstant.MAX_LOG))
			// end defect 7897 
		{
			lbNotUpdatingDB = true;
		}
		// also allow GET_TITLE_NUM through for batch.
		// allow batch to run.
		// added function GET_WS_IDS for redirecting batch reports - 
		// let it through.
		// Added function UPDATE_WORKSTATION_STATUS for updating ws 
		// status at startup.
		else if (
			(aiModuleName == GeneralConstant.SYSTEMCONTROLBATCH)
				&& (aiFunctionId
					== SystemControlBatchConstant.GET_BATCH_PROCESS
					|| aiFunctionId
						== SystemControlBatchConstant.GEN_BATCH_REPORT
					|| aiFunctionId
						== SystemControlBatchConstant.GEN_COUNTY_WIDE
					|| aiFunctionId
						== SystemControlBatchConstant.GEN_SUBSTATION
					|| aiFunctionId
						== SystemControlBatchConstant.GEN_TITLE_PACKAGE
					|| aiFunctionId
						== SystemControlBatchConstant.GET_TITLE_NUM
					|| aiFunctionId
						== SystemControlBatchConstant
							.GEN_COMPLETE_SETASIDE  
					|| aiFunctionId
						== SystemControlBatchConstant.UPDATE_BATCH_RESULTS
					// defect 8628 
					|| aiFunctionId
						== SystemControlBatchConstant.GET_SUBSTA_INFO
					// end defect 8628 
					|| aiFunctionId
						== SystemControlBatchConstant.GET_WS_IDS
					|| aiFunctionId
						== SystemControlBatchConstant
							.UPDATE_WORKSTATION_STATUS
					// defect 10986
					|| aiFunctionId
						== SystemControlBatchConstant
						.GET_START_TIME))
					// end defect 10986
		{
			lbNotUpdatingDB = true;
		}
		// allow CountyWide / Substation Summary.
		else if (
			aiModuleName == GeneralConstant.FUNDS
				&& aiFunctionId
					== FundsConstant.GENERATE_SUBSTATION_SUMMARY_REPORT)
		{
			lbNotUpdatingDB = true;
		}
		// defect 9935 
		// allow batch to Vendor Payment / Deposit Reconciliation Rpts  
		else if (
			aiModuleName == GeneralConstant.INTERNET_REG_REN_PROCESSING
				&& (aiFunctionId
					== RegRenProcessingConstants.GET_VENDOR_RPT
					|| aiFunctionId
						== RegRenProcessingConstants
							.GET_DEPOSIT_RECON_RPT))
		{
			lbNotUpdatingDB = true;
		}
		// end defect 9935 
		
		// defect 9941 
		// No longer used   
		//		// allow batch to Special Plate Internet Address Chang report
		//		else if (
		//			aiModuleName == GeneralConstant.INTERNET_REG_REN_PROCESSING
		//				&& aiFunctionId
		//					== com
		//						.txdot
		//						.isd
		//						.rts
		//						.services
		//						.webapps
		//						.util
		//						.constants
		//						.RegRenProcessingConstants
		//						.GET_SPCL_PLT_ADDR_CHG_RPT)
		//		{
		//			lbNotUpdatingDB = true;
		//		}
		// end defect 9941 
		// allow all cache except subcon to flow through.
		else if (aiModuleName == GeneralConstant.GENERAL)
		{
			lbNotUpdatingDB = true;
		}
		// allow ping through.
		else if (aiModuleName == GeneralConstant.PING)
		{
			lbNotUpdatingDB = true;
		}

		return lbNotUpdatingDB;
	}
	/**
	 * Indicate whether it is running on server.
	 * <br>True indicates we are running on the server (WAS)
	 * <br>False indicates we are on client side.
	 * 
	 * @return boolean
	 */
	public static boolean isServer()
	{
		return sbServer;
	}
	/**
	 * Returns the boolean indicating whether or not the server is up.
	 *  
	 * @return boolean
	 */
	public static boolean isServerDown()
	{
		return sbServerDown;
	}
	/**
	 * Running the main will allow for testing of HTTP Errors.
	 * These changes will find their way into processRequest.
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// substitue the string pulled out of the log here for testing.
		String lsTestString =
			"1f8b0800000000000000ed595d6c235715beb69338b6f3bf615941773b0f886a91f04f92cd6eb44268624f9269c73feb99fc39daaa93991b67baf68c77e6c6719040459516242a8aa0152fa002629190828aa0a002e2a14f208410f0c08f14a46aa522a8542ac456947d209c7be7c776e20468e12d37f2f8dc73ef9c73ee77be7b6772bcff06ea776c34f6a4da54933bc4a82557b0462cfbf75ffcf8c653d75f0c85514444e39ada503583ec89a666e33a36898886708d49596bc7241b28e175732a51093ab7215183a99a6a5653c5cd27c1e4f55603b92dbc63fb13927442d29bf0b95facbd30e65cae85116273e3ce6df409e4b7c40eed45409a8478539a554f92966e91a4e1e8499b384907db4d43c34e5253b56d9c9477361d08c5b04c1ad2736fbe76772df1f46218c52074cdc841b8048ba66e886854338a5b9ae8383baaa9e18225a2b866b8b78bba8486b5ecb659558c3a7648bd41d065097ca798ef14f84e81ef94ef3b45114c9515197ce2eb1278725c4359008fa0890e5464621b66b5734e41ad63bac6980401783a99789a6056a591d53ddd48a7ae34c3b401ca07f402483d720a522cdf5eb4b75f192ba12f7cf3308cc2221aa81b9a6d39121ad4d41a3675d526e8a21b3c5be1a28dab966da866d61bf6b20bfededf66d2b159cf7ee4b7bf79eddbbf7c258c428fa2b16a30bc43ac26b65b369a68dfecdff3c7d7f3977e7ab0f1d9084a54d0906ae30503d77447c6c0c1912dc37680727bc5ad558c6f5550cc7068a260b082a270bfc1a87abe6e98465dadc144473417e83d74ba8862266e1199a8f58688de03a8186a6d05db0e70a668427eb05a7f14f51130b88106b6985b82c21be206ea376800b4539150dfc72c131334d9810f0da2025a802504b08442ec0a9f88f7fdc6f94f3f40b00fc05afe874f7cb0f9fa77eefab41f77a7447fed4d1fa55b063e83f0d9f4e4b06786b621afdf07d3b97f7edf4068f62f4f31d395d567b9b9c9976fde0b4c878e3648d88536e6b2516fd4b01ffc83eacd277ef5f85bdf88a009d812b076596d02651da0076406c0a48700135cf84514856eded2b188069964926d57494d8a7046b8923b2566abbbc5ad2d8726b237f86006c86d13e66ac4177d6731a6706dc53d993974075c97c381ec3965fd75acda1538511c0cf66a46759b1e60757ab784cd2ad9a6899defa6a30f4ae6bb7f7e6b37b7ff55a0b0043b25e76d3a82225959a1c81f42f3b243b31243f53fb034b1ccb433e78f47fc71da422c6df3fb7f7befdb0383caab7eda861e7ee8e14bf0c72ebb71d00c84428760811d8993cc12b5029f56ebf0f0febd7fe8fb88a0f842b1ac70ab705922e87d0a5f2ef30585cb16970bca3a97e7c502575c5810b3029c4b99749a1392dcaac02b4b4219eecb11d47f753633374b6053532fd18e6325c43409b6e1a934721ac7fd10ff37b4a6d6cef766321dba402f0fbdeb34502b97de21d0e7165639455424819397e7658557c46281a07186702f80a9e50f1c83387c06f12910678b40e4b2b0d805f12924ee8d71e40ce393311e071abf6b84fbce103e19e1d152515aefc237313d9599e2f2a224096517d8cc5c6f60fbcf80ed02362617979525897f0c1e669381dc05ee706606d8cb1e7bb29264f0a6e7a67ac33b7006efc9bc9d2cd0ef554156ba0f87d9ab99694e1124a1b4542c085c39e7829c99bed21be4e819c8a780cc487c0ce4c9e96b53731c2f293c9717649e9b97560298a77bc33c78067317ccfd4bcb6559f1692cf0c7683c73855b2cf325614504162fadae7b67c59513f08d9de1db7d14f365492c2c2a8cae81dc7d145f9d9aa1ef11f3f002e13de9d299746f78e367f076c39be70bf2822848f0ea3519c847fed7f0ded3e6cb453ed77edacdba0c6ef80dfef79f3ba538a4ab444de61744b3894d62d97b7cad66694141ede2479f89ddb9f9d297c368b08262da6656ad6930b58246b4cd926d698e0e3d91d459794dc6358de8656b5734f516ada831b3bc76cbad16ce9c5651a361a43ac370efba2e8157a7dc200a6ef935325028b66a3a41816c4c6b523f347062babe263aaa346ea5132c4d684d3744afea0751520bcfb71a8c6861406aeabf41ca0df1476f3e78fa32b9fb2240c450700129da3ab673e468d931ab1f2f444a68407372d8f1d737a83960432b58ed2a61596b62119c7a9a51cd911b5a4db69b9a506f886d1c9c156c1b5b869bbfa2e9552d42999f1f785be4e0df9408062a5353e9749aa068161669ab35904a3333b39999598222003141c0ce48be98ebaada4682aa6dfc3fa15b0fb22d8b14cbe4d6f71e7ce9fc4bdf0aa3c90aac72d31bc73a1d64c8e5700dee2e63d531299631cd106cdbb2a93c04b8da4615866f903d11b2ed76493d6fe95b7b6e117858331877f83c2d868a28e1f569dd8701d8615f091897d09c05dbaad3cceaed1404aa8e8aee58a0962c4dd4038ac65c3d28db2621b2ac0e79f755e39ae3c50f2382a9070418e91ce8a4850c54df69ef83215fd3617418368b7534ca2855b663f13794b7b8968daebe830cd214fdf8271f7a7bece5c7ff1a46a39dc705cb1caca8a412db94f16daff8ee2e68dd1681f2869fb66126fa2b63fd02def5ebf4bdcaf89077dfb04783237eda45fe28db5a411287dd6e37da715f19688698a63ba1512f7ffe1643e8c67d6f2ffc09b5db81fb5518a6d706ecaf743ac3f3ebbeb0462b8a25b9848eb49ff9023bc1093a0773b80f73255e9685c2a250e64a9242f5fdd96dacdd22684454843cc7e772428e2b3e96a4a73e0dec6b21643ef379f70174e7852361c1f3a9ffbe1fd67a6e2d33770d84b5b53578acfcdfc3428f5c8007f7efe07bf44858f4248e7b61f5a5e716e1dce9cba4f90c74c3fcb18882e64534c6d380e074136f2c0b6e389ded849f9a1270688db77fa882931657b13d71ef2b5ffffb273f752d8c4222ea6faab51ddcf27f4263f30a3bf54d6cdfd97ffe62e2b9573fe39771dd17981f20bf804aa570204502a92f90fa03692090a281341848b1408afb0bfa176fb36ba0d81b00002  0";
		String lsContentString = STR_BEGIN_CONTENT_STRING;

		int liIndexEN = lsTestString.indexOf(lsContentString);

		if (liIndexEN < 0)
		{
			System.out.println(NOT_VALID_RTS_DATA_STREAM_MSG);
		}
		else
		{
			// position at where data should be
			//liIndexEN = liIndexEN - 2 ;
			String lsNewString = lsTestString.substring(liIndexEN);
			System.out.println(lsNewString);
			try
			{
				System.out.println(
					TXT_LENGTH_IS + lsNewString.length());
				// remove the blank out it it is there!
				int liBlankNdx =
					lsNewString.indexOf(CommonConstant.STR_SPACE_ONE);

				while (liBlankNdx > -1)
				{
					System.out.println(HAD_A_BLANK_MSG + liBlankNdx);
					lsNewString =
						lsNewString.substring(0, liBlankNdx - 1)
							+ STR_ZERO
							+ lsNewString.substring(liBlankNdx + 1);
					System.out.println(lsNewString);
					liBlankNdx =
						lsNewString.indexOf(
							CommonConstant.STR_SPACE_ONE);
				}

				// check to see this is an even number of charaters
				int liCheckLength = (lsNewString.length() / 2);
				liCheckLength = liCheckLength * 2;
				if (lsNewString.length() != liCheckLength)
				{
					System.out.println(
						ERR_MSG_STRINGS_ARE_NOT_EQUAL
							+ lsNewString.length()
							+ CommonConstant.STR_SPACE_ONE
							+ liCheckLength);
					lsNewString =
						lsNewString.substring(0, liCheckLength);
				}
				Object laObj = Comm.StringToObj(lsNewString);
				System.out.println(laObj);
			}
			catch (Throwable aeThrowable)
			{
				aeThrowable.printStackTrace();
			}
		}
	}
	/**
	 * This method makes parameter string for servlet
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param asString String
	 * @return String
	 */
	private static String makeParamString(
		int aiModuleName,
		int aiFunctionId,
		String asString)
	{
		String lsParamString =
			MODULE_NAME_STRING
				+ aiModuleName
				+ FUNCTION_ID_STRING
				+ aiFunctionId
				+ DATA_STRING
				+ asString;

		return lsParamString;
	}
	/**
	 * This method converts object into Java serilized String
	 * 
	 * @param aaObject Object
	 * @return String
	 * @throws RTSException
	 */
	public static String objToString(Object aaObject)
		throws RTSException
	{
		String lsReturnStr = null;

		try
		{
			ByteArrayOutputStream laBAOS = new ByteArrayOutputStream();
			GZIPOutputStream laGOS = new GZIPOutputStream(laBAOS);
			ObjectOutputStream laOOS = new ObjectOutputStream(laGOS);
			laOOS.writeObject(aaObject);
			laGOS.finish();
			lsReturnStr = toHexString(laBAOS.toByteArray());
			return (lsReturnStr);
		}
		catch (Throwable aeThrowable)
		{
			aeThrowable.printStackTrace();
			throw new RTSException(
				RTSException.JAVA_ERROR,
				aeThrowable);
		}
	}
	/**
	 * Make a connection with servlet and get the result back
	 * 
	 * @param asDataString String
	 * @return Object
	 * @throws RTSException
	 */
	private static Object processRequest(String asDataString)
		throws RTSException
	{
		StringBuffer lsStringBufferInp =
			new StringBuffer(CommonConstant.STR_SPACE_EMPTY);

		// defect 8811
		// Bring this out so it can be referenced in catches.
		HttpURLConnection laURLConnection = null;
		// end defect 8811

		try
		{
			//Check for server connectivity
			if (SystemProperty.getProdStatus() == 0)
			{
				int liTimeOut = 0;

				if (isServerDown())
				{
					liTimeOut =
						SystemProperty.getConnectTimeOutServDown();
				}
				else
				{
					liTimeOut =
						SystemProperty.getConnectTimeOutServUp();
				}

				Comm.setSocket(null);
				OpenThread laOT = new OpenThread();
				Thread laThread = new Thread(laOT);
				laThread.setDaemon(true);
				laThread.start();
				laThread.join(liTimeOut);
				try
				{
					laThread.interrupt();
				}
				catch (Throwable aeThrowable)
				{
					new RTSException(
						RTSException.SERVER_DOWN,
						aeThrowable);
				}
				Socket laSocket = Comm.getSocket();
				if (laSocket != null)
				{
					laSocket.close();
				}
				else
				{
					throw new IOException();
				}
			}

			// defect 8248
			// check to see if the strings should be initialized
			if (ssServletHost == null
				|| ssServletPort == null
				|| ssServletName == null
				|| ssHTTPString == null)
			{
				ssServletHost = SystemProperty.getServletHost();
				ssServletPort = SystemProperty.getServletPort();
				ssServletName = SystemProperty.getServletName();

				// initialize the HTTP String depending on which mode 
				// we are running.
				if (SystemProperty.isHTTPMode())
				{
					ssHTTPString = HTTP_STRING;
				}
				else
				{
					ssHTTPString = HTTPS_STRING;
				}
			}

			// create the URL
			URL laURL =
				new URL(
					ssHTTPString
						+ ssServletHost
						+ STR_COLON
						+ ssServletPort
						+ STR_FORWARD_SLASH
						+ ssServletName);
			// end defect 8248

			// defect 8811
			// Get the connection and assign it to laURLConnection
			laURLConnection =
				(HttpURLConnection) laURL.openConnection();
			// end defect 8811

			laURLConnection.setRequestMethod(POST_STRING);
			laURLConnection.setAllowUserInteraction(false);
			laURLConnection.setDoOutput(true);

			PrintWriter laOut =
				new PrintWriter(laURLConnection.getOutputStream());
			laOut.println(asDataString);
			laOut.flush();
			laOut.close();

			// Write the data into a file. This will be used for load 
			// testing.
			if (SystemProperty.getProdStatus() > 0)
			{
				//Full test mode.
				com.txdot.isd.rts.services.util.FileUtil.writeInFile(
					LOADTEST_FILE_STRING,
					asDataString);
			}

			BufferedReader laBufferedReader =
				new BufferedReader(
					new InputStreamReader(
						laURLConnection.getInputStream()));
			String lsRes;
			while (true)
			{
				lsRes = laBufferedReader.readLine();

				// check for strange results
				int liLength = laURLConnection.getContentLength();
				int liRC = laURLConnection.getResponseCode();
				String lsEncoding =
					laURLConnection.getContentEncoding();

				if (liRC != HTTP_RESPONSE_GOOD)
				{
					System.out.println(
						NOT_200_STRING
							+ liRC
							+ CommonConstant.STR_SPACE_ONE
							+ liLength);
				}

				if (lsEncoding != null
					&& lsEncoding.equalsIgnoreCase(CHUNKED_STRING))
				{
					System.out.println(
						CONTENT_WAS_CHUNKED_STRING
							+ liLength
							+ CommonConstant.STR_SPACE_ONE
							+ liRC);
					continue;
				}

				if (lsRes == null)
				{
					break;
				}
				if (lsRes.length() == 0)
				{
					continue;
				}
				lsStringBufferInp.append(lsRes);
			}

			// defect 6238
			// make sure buffer exists before checking return code
			// If buffered reader did not return any data, 
			// that means there was some problem with server.
			if (lsStringBufferInp == null
				|| lsStringBufferInp.toString().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
				RTSException rtsException =
					new RTSException(
						RTSException.SERVER_DOWN,
						new RTSException(COMM_LAYER_NULL_MSG));
				sbServerDown = true;
				sbMFUp = false;
				throw rtsException;
			}

			// 6612
			// create test data for testing http error
			//String lsTestReplacement = lStringBufferInp.toString();
			//lStringBufferInp = new StringBuffer();
			//lStringBufferInp.append("rrrContent-Language: en");
			//lStringBufferInp.append("223");
			//lStringBufferInp.append(lsTestReplacement);
			//lStringBufferInp.append("0");
			// end test block

			// check the return code
			if (laURLConnection.getResponseCode()
				!= HttpURLConnection.HTTP_OK)
			{
				// make the messages more clear
				Log.write(
					Log.SQL_EXCP,
					laURLConnection,
					UNEXPECTED_HTTP_RC_MSG
						+ laURLConnection.getResponseCode());
				Log.write(
					Log.SQL_EXCP,
					laURLConnection,
					UNEXPECTED_HTTP_RC_DATA_MSG + lsStringBufferInp);

				// find out if this is an object
				//String lsContentString = "Content-Language: en";
				//int liIndexEN =
				//	lsStringBufferInp.toString().indexOf(
				//		lsContentString);

				// find the "1f" position
				String ls1f = STR_BEGIN_CONTENT_STRING;
				int liIndex1f =
					lsStringBufferInp.toString().indexOf(ls1f);

				if (liIndex1f < 0)
				{
					Log.write(
						Log.SQL_EXCP,
						laURLConnection,
						NOT_VALID_RTS_DATA_STREAM_MSG);
					// throw an exception if we do not like the results
					fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
					RTSException rtsException =
						new RTSException(
							RTSException.SERVER_DOWN,
							new RTSException(COMM_LAYER_NULL_MSG));
					sbServerDown = true;
					sbMFUp = false;
					throw rtsException;
				}
				else
				{
					// parse out the length parameter at the begining 
					// of the string!
					// also check for extra byte and remove it.
					try
					{
						// parse out the length
						String lsNewString =
							lsStringBufferInp
								.substring(liIndex1f)
								.toString();

						// defect 7466
						// remove the blank if it is there!
						// It is interesting that the index does not change
						int liBlankNdx =
							lsNewString.indexOf(
								CommonConstant.STR_SPACE_ONE);
						while (liBlankNdx > -1)
						{
							System.out.println(
								HAD_A_BLANK_MSG + liBlankNdx);
							lsNewString =
								lsNewString.substring(
									0,
									liBlankNdx - 1)
									+ STR_ZERO
									+ lsNewString.substring(
										liBlankNdx + 1);
							System.out.println(lsNewString);
							liBlankNdx =
								lsNewString.indexOf(
									CommonConstant.STR_SPACE_ONE);
						}
						// end defect 7466

						int liCheckLength = lsNewString.length() / 2;
						liCheckLength = liCheckLength * 2;

						// if lengths do not match up, take off the 
						// last byte.
						if (liCheckLength != lsNewString.length())
						{
							lsNewString =
								lsNewString.substring(0, liCheckLength);
						}

						// write this new string to the log for 
						// comparisions.
						Log.write(
							Log.SQL_EXCP,
							laURLConnection,
							UNEXPECTED_HTTP_RC_NEWSTR_MSG
								+ lsNewString);

						// replace the bad string with the update.
						lsStringBufferInp = new StringBuffer();
						lsStringBufferInp.append(lsNewString);
					}
					catch (Throwable aeThrowable)
					{
						Log.write(
							Log.SQL_EXCP,
							laURLConnection,
							HT_PARSING_PROBLEM_MSG);

						// throw an error if there is an object problem
						fireCommEvent(
							new CommEvent(CommEvent.SERVER_DOWN));
						RTSException leRtsException =
							new RTSException(
								RTSException.SERVER_DOWN,
								new RTSException(COMM_LAYER_NULL_MSG));
						sbServerDown = true;
						sbMFUp = false;
						throw leRtsException;
					}
				}
			}

			sbServerDown = false;
			Log.write(Log.START_END, null, RECEIVE_FROM_SERVER_MSG);
		}
		catch (NoRouteToHostException aeNRTHEx)
		{
			// defect 8663
			// Log that there is no route
			Log.write(Log.SQL_EXCP, aeNRTHEx, MSG_NO_ROUTE);
			// end defect 8663
			fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
			RTSException leRtsException =
				new RTSException(RTSException.SERVER_DOWN, aeNRTHEx);
			sbServerDown = true;
			sbMFUp = false;
			throw leRtsException;
		}
		catch (MalformedURLException aeMURLEx)
		{
			// defect 8663
			// Log that there is no route
			Log.write(Log.SQL_EXCP, aeMURLEx, MSG_MALFORMED_URL);
			Log.write(
				Log.SQL_EXCP,
				aeMURLEx,
				aeMURLEx.getLocalizedMessage());
			// end defect 8663
			RTSException rtsException =
				new RTSException(RTSException.JAVA_ERROR, aeMURLEx);
			throw rtsException;
		}
		catch (SocketException aeSE)
		{
			// defect 8663
			// Log that there is no route
			Log.write(Log.SQL_EXCP, aeSE, MSG_SOCKET_EXCEPTION);
			// end defect 8663
			fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
			RTSException leRtsException =
				new RTSException(RTSException.SERVER_DOWN, aeSE);
			sbServerDown = true;
			sbMFUp = false;
			throw leRtsException;
		}
		catch (IOException aeIOEx)
		{
			// defect 8663
			// Log that there is no route
			Log.write(Log.SQL_EXCP, aeIOEx, MSG_IO_EXCEPTION);
			// end defect 8663
			fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
			RTSException leRtsException =
				new RTSException(RTSException.SERVER_DOWN, aeIOEx);
			sbServerDown = true;
			sbMFUp = false;
			throw leRtsException;
		}
		catch (InterruptedException aeIEx)
		{
			// defect 8663
			// Log that there is no route
			Log.write(Log.SQL_EXCP, aeIEx, MSG_INTERRUPTED_EXCEPTION);
			// end defect 8663
			fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
			RTSException leRtsException =
				new RTSException(RTSException.SERVER_DOWN, aeIEx);
			sbServerDown = true;
			sbMFUp = false;
			throw leRtsException;

			// defect 8811
		}
		finally
		{
			// use finally to make sure the connection is closed.
			if (laURLConnection != null)
			{
				laURLConnection.disconnect();
				laURLConnection = null;
			}
		}
		// end defect 881

		Object laObj = StringToObj(lsStringBufferInp.toString());

		if (laObj instanceof RTSException)
		{
			RTSException leRTSEx = (RTSException) laObj;
			if (leRTSEx.getMsgType().equals(RTSException.DB_DOWN))
			{
				fireCommEvent(new CommEvent(CommEvent.DB_DOWN));
				// log that database is down
				Log.write(Log.SQL_EXCP, leRTSEx, DB_CONN_DOWN_MSG);
			}
			if (leRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
			{
				fireCommEvent(new CommEvent(CommEvent.SERVER_DOWN));
				sbServerDown = true;
				sbMFUp = false;
				// log that server is down
				Log.write(Log.SQL_EXCP, leRTSEx, SRVR_DOWN_MSG);
			}
			if (sbMFUp == true
				&& leRTSEx.getMsgType().equals(RTSException.MF_DOWN))
			{
				sbMFUp = false;
				fireCommEvent(new CommEvent(CommEvent.MF_DOWN));
				// log that mainframe is down
				Log.write(Log.SQL_EXCP, leRTSEx, MF_DOWN_MSG);
			}
			leRTSEx.writeExceptionToLog();
			throw leRTSEx;
		}
		else if (sbMFUp == true && laObj instanceof VehicleInquiryData)
		{
			VehicleInquiryData laVehData = (VehicleInquiryData) laObj;
			if (laVehData.getMfDown() == 1)
			{
				sbMFUp = false;
				fireCommEvent(new CommEvent(CommEvent.MF_DOWN));
				// log that mainframe is down
				Log.write(Log.SQL_EXCP, laObj, MF_DOWN_MSG);
			}
			return laObj;
		}
		else if (sbMFUp == false && fromMF())
		{
			sbMFUp = true;
			fireCommEvent(new CommEvent(CommEvent.MF_UP));
			// log that mainframe is now up
			Log.write(Log.SQL_EXCP, laObj, MF_UP_MSG);
			return laObj;
		}
		else if (
			sbMFUp == false && laObj instanceof VehicleInquiryData)
		{
			VehicleInquiryData vehData = (VehicleInquiryData) laObj;
			if (vehData.getMfDown() == 0)
			{
				sbMFUp = true;
				fireCommEvent(new CommEvent(CommEvent.MF_UP));
				// log that mainframe is now up
				Log.write(Log.SQL_EXCP, laObj, MF_UP_MSG);
			}
			return laObj;
		}
		else
		{
			return laObj;
		}
	}
	/**
	 * Remove comm listener
	 * 
	 * @param aaCL CommListener
	 */
	public static void removeCommListener(CommListener aaCL)
	{
		if (svListeners == null)
		{
			svListeners = new Vector();
		}
		svListeners.remove(aaCL);
	}
	/**
	 * This method sends the data and method name to servlet and 
	 * servlet calls appropriate controller to handle the request.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaObject Object
	 * @return Object
	 * @throws RTSException
	 */
	public synchronized static Object sendToServer(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		siModuleName = aiModuleName;
		siFunctionId = aiFunctionId;

		Log.write(
			Log.START_END,
			new SystemProperty(),
			ATTEMPTING_TO_SEND_MODULE_STRING
				+ aiModuleName
				+ ATTEMPTING_TO_SEND_FUNCTION_STRING
				+ aiFunctionId);

		// code to make sure Send Cache and DB's are in sync
		// we don't want any calls to go to the DB when it is up, but 
		// Send Cache hasn't woken up yet and run
		// essentially, the DB isn't up until SendCache says it is

		// use isNotUpdatingDB instead of isUpdatingDB.
		// This new method indicates what can be sent through.
		if (!isNotUpdatingDB(siModuleName, siFunctionId)
			&& !com
				.txdot
				.isd
				.rts
				.client
				.desktop
				.RTSApplicationController
				.isDBUp())
		{
			// add a message to indicate we are checking for DB Down 
			// Processing
			Log.write(
				Log.START_END,
				new SystemProperty(),
				DB_DOWN_SEND_QUESTION_MSG);
			// if the module & function combination is not covered 
			// under isNotUpdatingDB
			// then it must only be sendCache is DB Down.
			if (aaObject instanceof Vector)
			{
				Vector lvVectorIn = (Vector) aaObject;
				// do not attempt to send small vectors
				// They are not sendcache
				if (lvVectorIn == null
					|| lvVectorIn.size() < CommonConstant.ELEMENT_2)
				{
					Log.write(
						Log.START_END,
						new SystemProperty(),
						DB_DOWN_NOT_SENDCACHE_MSG);
					throw new RTSException(
						RTSException.DB_DOWN,
						new Exception());
				}

				if (!(lvVectorIn.get(CommonConstant.ELEMENT_1)
					instanceof Vector))
				{
					Log.write(
						Log.START_END,
						new SystemProperty(),
						DB_DOWN_NOT_SENDCACHE_MSG);
					throw new RTSException(
						RTSException.DB_DOWN,
						new Exception());
				}

				Vector lvSubVector2 =
					(Vector) lvVectorIn.get(CommonConstant.ELEMENT_1);

				if (lvSubVector2 == null)
				{
					Log.write(
						Log.START_END,
						new SystemProperty(),
						DB_DOWN_NOT_SENDCACHE_MSG);
					throw new RTSException(
						RTSException.DB_DOWN,
						new Exception());
				}

				if (!(lvSubVector2.get(CommonConstant.ELEMENT_0)
					instanceof TransactionCacheData))
				{
					Log.write(
						Log.START_END,
						new SystemProperty(),
						DB_DOWN_NOT_SENDCACHE_MSG);
					throw new RTSException(
						RTSException.DB_DOWN,
						new Exception());
				}

				TransactionCacheData laCacheData =
					(TransactionCacheData) lvSubVector2.get(
						CommonConstant.ELEMENT_0);

				if (laCacheData == null)
				{
					Log.write(
						Log.START_END,
						new SystemProperty(),
						DB_DOWN_NOT_SENDCACHE_MSG);
					throw new RTSException(
						RTSException.DB_DOWN,
						new Exception());
				}

				if (!laCacheData.isFromSendCache())
				{
					Log.write(
						Log.START_END,
						new SystemProperty(),
						DB_DOWN_NOT_SENDCACHE_MSG);
					throw new RTSException(
						RTSException.DB_DOWN,
						new Exception());
				}
			}
			else
			{
				// this can not be transaction cache being sent.
				Log.write(
					Log.START_END,
					new SystemProperty(),
					DB_DOWN_NOT_SENDCACHE_MSG);
				throw new RTSException(
					RTSException.DB_DOWN,
					new Exception());
			}
		}

		String lsString = objToString(aaObject);
		String lsParamString =
			makeParamString(aiModuleName, aiFunctionId, lsString);

		return (processRequest(lsParamString));
	}
	/**
	 * Set is server boolean
	 * 
	 * @param abServer boolean
	 */
	public static void setIsServer(boolean abServer)
	{
		sbServer = abServer;
	}
	/**
	 * set last transaction date
	 *  
	 * @param aiNewLastTransactionDate int
	 */
	public static void setLastTransactionDate(int aiNewLastTransactionDate)
	{
		siLastTransactionDate = aiNewLastTransactionDate;
	}
	/**
	 * Set the Socket Object
	 * 
	 * @param aaSocket Socket
	 */
	public static void setSocket(java.net.Socket aaSocket)
	{
		saSocket = aaSocket;
	}
	/**
	 * This method converts Java serialized String to an Object
	 * 
	 * @param asString String
	 * @return Object
	 * @throws RTSException	
	 */
	public static Object StringToObj(String asString)
		throws RTSException
	{
		Object laToReturn = null;

		try
		{
			ByteArrayInputStream laBAIS =
				new ByteArrayInputStream(deHextoByte(asString));
			GZIPInputStream laGIS = new GZIPInputStream(laBAIS);
			ObjectInputStream laOIS = new ObjectInputStream(laGIS);
			laToReturn = laOIS.readObject();
			return laToReturn;
		}
		catch (Throwable aeThrowable)
		{
			aeThrowable.printStackTrace();
			if (asString != null)
			{
				Log.write(
					Log.SQL_EXCP,
					laToReturn,
					ERR_TXT_STRING_TO_OBJ + asString);
			}
			throw new RTSException(
				RTSException.JAVA_ERROR,
				aeThrowable);
		}
	}
	/**
	 * This method converts an array of bytes into Hex String.
	 *
	 * @param aarrByteBlock byte[]
	 * @return String
	 */
	public static String toHexString(byte[] aarrByteBlock)
	{
		StringBuffer lsBuffer = new StringBuffer();

		//int len = aarrByteBlock.length;
		for (int i = 0; i < aarrByteBlock.length; i++)
		{
			byte2hex(aarrByteBlock[i], lsBuffer);
		}
		return lsBuffer.toString();
	}
}
