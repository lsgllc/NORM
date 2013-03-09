package com.txdot.isd.rts.server.dataaccess;

import java.util.Date;

import com.ibm.mq.*;

import com.txdot.isd.rts.server.webapps.util.MQLog;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.CompleteRegRenData;
import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.data.RenewalShoppingCart;
import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.data.SpecialPltOrdrCart;
import com.txdot.isd.rts.services.data.SpecialPltOrdrCartItem;
import com.txdot.isd.rts.services.data.VehicleUserData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.SystemProperty;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.CommonConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.RegRenProcessingConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.RegistrationRenewalConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.ServiceConstants;

import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.common
	.data
	.DefaultResponse;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.transaction
	.business
	.TransactionAccessBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.transaction
	.data
	.TransactionRequest;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.virtualinv
	.data
	.VirtualInvResponse;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.registrationrenewal
	.RegRenProcessingServerBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.registrationrenewal
	.RegistrationRenewalServerBusiness;

/*
 *
 * MQAccess.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/05/2003	add time stamp in front 
 *                          of printstacktrace.
 * 							defect 5263  
 * Bob Brown	01/04/2005	Added log write statements when MQ objects
 *                          are retrieved from and posted to the MQ 
 *                          queue. Also added log write statements for 
 *                          every catch block, and error situation.
 *							modify main
 *							modify postMessage
 *							modify processRefund
 *							modify processRenewal
 *							modify retrieveMessage
 *							defect 7168 Ver 5.2.2
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	09/30/2005	Java 1.4 Work; webapps data objects moved
 * 							to services.data
 * 							defect 7889 Ver 5.2.3
 * Ray Rowehl	02/13/2006	Improve handling of null pointer exceptions.
 * 							Do some cleanup and formatting.
 * 							modify main(), postMessage(), 
 * 							processRefundReply(), processRenewal()
 * 							defect 8547 Ver 5.2.2 Fix 7 
 * Ray Rowehl	02/14/2006	Found that skipped message was not going to 
 * 							console.  Now goes to console as well.
 * 							switch system.err to system.out!
 * 							modify main(), postMessage(), 
 * 								processRefundReply(), processRenewal(),
 * 								retrieveMessage()
 * 							defect 8547 Ver 5.2.2 Fix 8 
 * Ray Rowehl	03/03/2005	Did some RTS 5.2.3 cleanup
 * 							create constants.
 * 							format source
 * 							defect 8547 Ver 5.2.2 Fix 8
 * B Brown		05/15/2006	If the MQ object being read is 
 * 							client.webapps.data.RefundData, convert
 * 							it to services.webapps.data.RefundData
 * 							modify processRefundReply()
 * 							defect 8777 Ver 5.2.3
 * B Brown		09/14/2006	Run MQAccess as an application Client on the
 * 							new WAS5 server, and read the MQ parms from
 * 							SystemProperty.
 * 							modify main()
 * 							defect 8914 Ver 5.2.5
 * B Brown		10/20/2006	Put date/time in all logging.
 * 							Don't log empty queue (2033).
 * 							Make sure we go to correct logs.
 * 							add TXT_MISSING_PARMS
 * 							add write()
 * 							modify main(), postMessage(), 
 * 								processRefundReply(), processRenewal()
 * 								retrieveMessage()
 * 							defect 8357 Ver Exempts 
 * B Brown		10/20/2006	Log empty queue (2033)once. 
 * 							add cbEmptyMsgWritten 
 * 							modify 	postMessage(), retrieveMessage()
 * 							defect 8357 Ver Exempts 
 * B Brown		11/27/2006	Log one empty message after a real message. 
 * 							add PROCESSING_RECS 
 * 							modify retrieveMessage(), processRenewal()
 * 							defect 8357 Ver Exempts 
 * B Brown		05/02/2007	Add processing of special plate order 
 * 							records. 
 * 							add MSG_ORDER
 * 							add processOrder() 
 * 							modify main()
 * 							defect 8357 Ver Special Plates 
 * Jeff S.		05/04/2007	Cleanup and change the method that was 
 * 							called to process the MQ update.
 * 							modify processOrder() 
 * 							defect 9121 Ver Special Plates 
 * Bob B.		12/01/2011	Change the MQ empty queue error log
 * 							writing to look at an integer field, so we 
 * 							log only the first empty queue message. Also
 * 							put date/time in the MQ eror writing.
 * 							modify main(), postMessage(), retrieveMessage()
 * 							defect 10995 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * The MQAccess class is used to post and retrieve messages from MQ 
 * Series queues. MQ Series is used as the interface for passing 
 * messages (Java Objects) between TxDOT and TexasOnline for the 
 * processing of Phase 2 transactions.
 *
 * <p>This class is used in 2 ways.  First, the class is used by code 
 * executed by RTSMainServlet on the TxDOT servers for posting messages 
 * to TexasOnline. Second, this class is executed as a separate 
 * application on teh Web Application Servers at TxDOT to retrieve 
 * messages sent from TexasOnline and to call the appropriate functions 
 * to process these messages at TxDOT 
 * (usually an update to the DB2 tables).
 *
 * @version	6.9.0			12/01/2011
 * @author	Richard Hicks
 * <br>Creation Date:		08/28/2001	17:41:17
 */

public class MQAccess
{
	// defect 8914
	//	private static final int DEFAULT_PORT = 1414;
	//	private static final int ITEM0 = 0;
	//	private static final int ITEM1 = 1;
	//	private static final int ITEM2 = 2;
	//	private static final int ITEM3 = 3;
	//	private static final int ITEM4 = 4;
	//	private static final int LENGTH_ARGS_NEEDED = 5;
	//	private static final String MSG_ARGS_LENGTH =
	//		"MQAccess host_name channel_name queue_manager queue_name port";
	// end defect 8914
	private static final int MQ_EMPTY_RETURN_CODE = 2033;
	private static final int SLEEP_30_SECONDS = 30000;
	private static final int SLEEP_60_SECONDS = 60000;

	private static final String MSG_DID_NOT_PROCESS_REG_PAYMENT =
		"MQAccess processRenewal did not process the regisistration "
			+ "payment data for the record below - current method "
			+ "processRenewal, current class = ";
	private static final String MSG_DID_NOT_PROCESS_REGIS_PAYMENT =
		"RegRenProcessingServerBusiness.processData did not process "
			+ "the regisistration payment data for the record below - "
			+ "current method processRenewal, current class = ";
	private static final String MSG_DID_NOT_SEND_REFUND =
		"RegRenProcessingServerBusiness.processData did not send a "
			+ "refund for the record below - "
			+ "processRefundReply method - "
			+ "current class = ";
	private static final String MSG_DO_NOT_REPOST = "Do not re-post!!";
	private static final String MSG_END_OF_REFUND_REPLY_REQUEST =
		"End of re-post refund reply request";
	private static final String MSG_END_OF_REGIS_REPOST =
		"End of regis repost log info";

	private static final String MSG_ERR_ATTEMPT_TO_LOAD_NULL_OBJECT =
		" The MQ attempt to load the message to an object, "
			+ "is a null object";
	private static final String MSG_ERR_CAUGHT_EXCEPTION_PROCESS_REFUND_REPLY =
		"The above refund reply error has occurred in the main catch "
			+ "block of processRefundReply in:";
	private static final String MSG_ERR_CLASS_NOT_FOUND_IN_RETREIVEMESSAGE =
		" The above class not found error has occurred in method "
			+ "retrieveMessage, class:";
	private static final String MSG_ERR_EXITING =
		"Exiting for exception!!";
	private static final String MSG_ERR_FORMAT_UNKNOWN =
		"**** ERROR : Message format is unknown.  ****";
	private static final String MSG_ERR_IO_ERROR_IN_RETRIEVEMESSAGE =
		" The above IO error has occurred in method "
			+ "retrieveMessage, class:";
	private static final String MSG_ERR_MAIN_ERR =
		"The above error has occurred in: "
			+ "com.txdot.isd.rts.server.dataaccess.MQAccess.main";
	private static final String MSG_ERR_MAIN_SLEEP =
		"The above sleep error has occurred in: "
			+ "com.txdot.isd.rts.server.dataaccess.MQAccess.main";
	private static final String MSG_ERR_MQ_ATTEMPT_TO_LOAD_NULL_OBJECT_2 =
		" The MQ attempt to load the message to an object, "
			+ "left a null object";
	private static final String MSG_ERR_MQ_ERROR_IN_RETRIEVEMESSAGE =
		" The above MQ error has occurred in method "
			+ "retrieveMessage, class:";
	private static final String MSG_ERR_MQACCESS = "MQAccess error ";
	private static final String MSG_ERR_MQACCESS_JAVA =
		"The above MQAccess java exception has occurred in: "
			+ "com.txdot.isd.rts.server.dataaccess.MQAccess.main";
	private static final String MSG_ERR_NOT_RENEWAL_OR_REFUND =
		"The above MQ message is not a RenewalShoppingCart and not a "
			+ "RefundData object";
	private static final String MSG_ERR_NPR_PROCESSING_EXCEPTION =
		"Got a NullPointer while processing Exception";
	private static final String MSG_ERR_QUEUE_CLOSING_ERR_RETREIVEMESSAGE =
		"The above queue closing error has occurred in method "
			+ "retrieveMessage, class:";
	private static final String MSG_ERR_QUEUE_DISCONNECT =
		"The above queue mgr disconnect error "
			+ "has occurred in method "
			+ "retrieveMessage, class:";
	private static final String MSG_ERR_POSTMSG_JAVA_IO =
		"The above IO MQAccess postMessage error occurred while "
			+ "writing to the message buffer, and has occurred in "
			+ "postMessage in:";
	private static final String MSG_ERR_POST_MSG_EXCEPTION =
		"The above java Exception MQAccess postMessage error has "
			+ "occurred in postMessage in:";
	private static final String MSG_ERR_PROCESSING_REFUND_REPLY =
		"Error processing refund reply : sleeping for 60 seconds";
	private static final String MSG_ERR_PROCESSING_RENEWAL_SLEEP =
		"Error processing renewal : sleeping for 60 seconds";
	private static final String MSG_ERR_REFUND_REPLY_SLEEP_ERROR =
		"The above refund reply sleep error has occurred in method "
			+ "processRefundReply, class:";
	private static final String MSG_ERR_REFUND_REPLY_SLEEP =
		"The above refund reply sleep error has occurred in:";
	private static final String MSG_ERR_SLEEP_INTERRUPTED_PROCESSRENEWAL =
		"The above regis renewal sleep error has occurred in method "
			+ "processRenweal, class:";
	private static final String MSG_ERR_SLEEP_PROCESSRENEWAL =
		"The above regis renewal sleep error has occurred in method "
			+ "processRenewal, class:";
	private static final String MSG_ERR_WRITING_MSG_BUFFER =
		"An error occurred whilst writing to the message buffer: ";

	private static final String MSG_MQEXCEPTION_IN_POSTMESSAGE =
		"The above MQException MQAccess postMessage error has occurred "
			+ "in postMessage in:";
	private static final String MSG_MQERROR_CC =
		"An MQ error occurred : Completion code ";
	private static final String MSG_MQ_MSG_NULL =
		"The MQ message object, is a null object";
	private static final String MSG_REFUND = "Message is a Refund";
	private static final String MSG_REFUND_NOT_OK_SLEEP =
		"Sleeping for 30 seconds.  Refund was not ok.";
	private static final String MSG_RENEWAL = "Message is a Renewal";
	private static final String MSG_REPOSTING_DUE_TO_ERROR =
		"Reposting the registration payment data for the following "
			+ "record, due to the above error";
	private static final String MSG_REPOSTING_REFUND_REQUEST =
		"Reposting a refund request for the following record, due "
			+ "to above error";
	private static final String MSG_RETRIEVED_MSG_IS =
		" The retrieved message is: ";
	private static final String MSG_SHOPPING_CART_EMPTY =
		"Shopping cart is empty!!";

	private static final String STR_ALREADY_UPDATED = 
		"Record already updated.";
	private static final String STR_COMPLETION_CODE_EQUAL =
		"Completion code = ";
	private static final String STR_DOUBLE_LINES =
		"=================================";
	private static final String STR_EMAIL_ADDR_SPACES_COLON_1 =
		"e-mail Address : ";
	private static final String STR_EMAIL_ADDR_SPACES_COLON_2 =
		"e-mail Address		 : ";
	private static final String STR_END_OF_REFUND_REQUEST =
		"End of refund request";
	private static final String STR_END_OF_REGIS_LOG =
		"End of regis log info";
	private static final String STR_FAILED_ATTEMPT_TO_REPOST =
		"Failed attempt to post this message: ";
	private static final String STR_ITRNTPYMNT_STATUS_CD_EQUAL =
		"Itrntpymntstatuscd = ";
	private static final String STR_MQACCESS_ERROR_2 =
		"MQAccess Error ";
	private static final String STR_NAME_SPACES_COLON =
		"Name                 : ";
	private static final String STR_NEEDS_UPDATING = 
		"Record needs updating.";
	private static final String STR_PAYMENT_ORDER_ID_EQUAL =
		"Payment Order ID = ";
	private static final String STR_PHONE_NUMBER_SPACES_COLON_1 =
		"Phone Number   : ";
	private static final String STR_PHONE_NUMBER_SPACES_COLON_2 =
		"Phone Number         : ";
	private static final String STR_PROCESSING_REFUND =
		"Processing Refund : ";
	private static final String STR_PROCESSING_REFUND_FOLLOWING_REC =
		"Processing a refund for the following record:";
	private static final String STR_PROCESSING_RENEWAL_FOLLOWING_REC =
		"Processing a renewal for the following record:";
	private static final String STR_PROCESSING_RENEWAL_TRACE_NO =
		"Processing a Renewal for trace no: ";
	private static final String STR_REASON_CODE_EQUAL =
		"Reason code = ";
	private static final String STR_REASON_CODE = " Reason code ";
	private static final String STR_REFUND_AMOUNT = "Refund amount ";
	private static final String STR_REFUND_ORIG_TRACE_NO =
		"Refund orig trace no = ";
	private static final String STR_REFUND_PAYMENT_ORDER_ID =
		"Refund payment order id = ";
	private static final String STR_REFUND_STATUS =
		"Refund Status Code = ";
	private static final String STR_REGIS_TRACE_NO_EQUAL =
		"Regis trace no = ";
	private static final String STR_TOTAL_FEES_EQUAL = "Total fees = ";
	private static final String STR_VEH_COUNTY_NO = "Veh county no= ";
	private static final String STR_VEH_COUNTY = "Veh county = ";
	private static final String STR_VEH_DOC_NO = "Veh doc no = ";
	private static final String STR_VEH_OWNER_NAME_EQUAL =
		"Veh Owner Name = ";
	private static final String STR_VEH_PLATE_NO = "Veh plate no = ";
	private static final String STR_VEH_VIN = "Veh vin = ";

	private static final String TXT_POSTMSG_ENTRY =
		" - postMesage() - Entry.";
	private static final String TXT_POSTMESSAGE_EXIT =
		" - postMesage() - Exit.";
	private static final String TXT_RETREIVE_MESSAGE_BEGIN =
		" - retrieveMessage() - Entry.";
	private static final String TXT_RETREIVEMESSAGE_END =
		" - retrieveMessage() - Exit.";
	// defect 8357
	private static final String TXT_MISSING_PARMS =
		" An MQ parameter is null or missing from server.cfg";
	// defect 10995
	private boolean cbEmptyMsgWritten = false;
	// private int ciEmptyMsgCount = 0;
	// end defect 10995
	private static final String PROCESSING_RECS = "Processing records";
	// end defect 8357

	// defect 9119
	private static final String MSG_ORDER = "Message is a SP Order";
	private static final String STR_PROCESSING_ORDER_FOLLOWING_REC =
		"Processing a SP order for the following record:";
	// end defect 9119	

	/** 
	 * The port of the MQ Series Server 
	 */
	private int ciMQPort;
	/** 
	 * The channel of the MQ Series Server 
	 */
	private String csMQChannel;
	/** 
	 * The Queue Manager name of the MQ Series Server 
	 */
	private String csMQQueueManager;
	/** 
	 * The csHostname of the MQ Series Server 
	 */
	private String csHostname;

	/**
	 * MQAccess Constructor.
	 * 
	 * @param asHost String
	 * @param asChannel String
	 * @param asQueueManager String
	 * @param aiPort int
	 */
	public MQAccess(
		String asHost,
		String asChannel,
		String asQueueManager,
		int aiPort)
	{
		super();
		csHostname = asHost;
		csMQChannel = asChannel;
		csMQQueueManager = asQueueManager;
		ciMQPort = aiPort;
	}

	/**
	 * Main method for execution of the MQAccess class 
	 * as an application.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// defect 8914
			Comm.setIsServer(true);
			int liPort = 0;
			String lsHostName = SystemProperty.getMQHostName();
			String lsChannelName = SystemProperty.getMQChannelName();
			String lsQueueManager = SystemProperty.getMQQueueManager();
			String lsQueueName = SystemProperty.getMQQueueName();
			if (SystemProperty.getMQPort() != null)
			{
				liPort = Integer.parseInt(SystemProperty.getMQPort());
			}
			// defect 8357
			// add date/time
			write(
				"lsHostName = SystemProperty.getMQHostName() = "
					+ lsHostName);

			write(
				"lsChannelName = SystemProperty.getMQChannelName() = "
					+ lsChannelName);

			write(
				"lsQueueManager = "
					+ "SystemProperty.getMQQueueManager() =  "
					+ lsQueueManager);

			write(
				"lsQueueName = SystemProperty.getMQQueueName() = "
					+ lsQueueName);

			write(
				"liPort = Integer.parseInt"
					+ "(SystemProperty.getMQPort() = "
					+ liPort);
			// end defect 8357	

			//			if (args.length < LENGTH_ARGS_NEEDED)
			//			{		
			//				System.out.println
			//					("Incorrect number of parameters = " + 
			//						MSG_ARGS_LENGTH);							
			//			}
			//			else
			//			{
			//				lsHostName = args[ITEM0];
			//				lsChannelName = args[ITEM1];
			//				lsQueueManager = args[ITEM2];
			//				lsQueueName = args[ITEM3];
			//				liPort = Integer.parseInt(args[ITEM4]);

			//			writeAllMessages();		

			if (lsHostName == null
				|| lsChannelName == null
				|| lsQueueManager == null
				|| lsQueueName == null
				|| liPort == 0)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					TXT_MISSING_PARMS);
			}
			// end defect 8914
			else
			{

				MQAccess laReceiver =
					new MQAccess(
						lsHostName,
						lsChannelName,
						lsQueueManager,
						liPort);
				Object laMessage = null;
				//boolean lbRegistrationOk = false;

				while (true)
				{
					// defect 10995
					// no need to instantiate this class again
//					laReceiver =
//						new MQAccess(
//							lsHostName,
//							lsChannelName,
//							lsQueueManager,
//							liPort);
					// end defect 10995
					laMessage = laReceiver.retrieveMessage(lsQueueName);
					if (laMessage != null)
					{
						if (laMessage instanceof RenewalShoppingCart)
						{
							// defect 8357
							write(MSG_RENEWAL);
							// end defect 8357
							laReceiver.processRenewal(
								lsQueueName,
								laMessage);
						}
						else if (laMessage instanceof RefundData)
						{
							// defect 8357
							write(MSG_REFUND);
							// end defect 8357
							laReceiver.processRefundReply(
								lsQueueName,
								laMessage);
						}
						// defect 9119
						else if (
							laMessage instanceof SpecialPltOrdrCart)
						{
							write(MSG_ORDER);
							laReceiver.processOrder(
								lsQueueName,
								laMessage);
						}
						// end defect 9119
						else
						{
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								MSG_ERR_FORMAT_UNKNOWN);
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								laMessage.toString());
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								MSG_ERR_NOT_RENEWAL_OR_REFUND);
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								MSG_ERR_MAIN_ERR);
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								STR_DOUBLE_LINES);
						}
					}
					else
					{
						// System.out.println("No message returned.");
						try
						{
							// if may be common for null messages to 
							// occur - no logging here.
							// defect 8547
							// update the message but do not print 
							// for now.
							// note that this would generate 1400 lines 
							// on a quiet day.
							// System.out.println("Sleeping for 
							// 30 seconds.");
							// Sleep for only 30 seconds
							Thread.sleep(SLEEP_30_SECONDS);
							// end defect 8547
						}
						catch (Exception aeEx)
						{
							aeEx.printStackTrace();
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								aeEx.getMessage());
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								aeEx);
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								MSG_ERR_MAIN_SLEEP);
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								STR_DOUBLE_LINES);
						}
					}
				}
			}

		}
		catch (Exception aeEx)
		{
			// defect 8547
			// make it more clear in the console that we are exiting
			aeEx.printStackTrace();
			// System.out.println(MSG_ERR_EXITING);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(aeEx);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_MQACCESS_JAVA);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);

			// make it more clear in the log that we are exiting.
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_EXITING);
			// end defect 8547
		}
	}

	/**
	 * Posts the Java Object onto the specified MQ Series queue.
	 * 
	 * @param asQueue String
	 * @param aaObject Object
	 * @throws RTSException
	 */
	public void postMessage(String asQueue, Object aaObject)
		throws RTSException
	{
		// defect 8357
		// Log.write(Log.METHOD, this, TXT_POSTMSG_ENTRY);
		write(this.getClass() + " " + TXT_POSTMSG_ENTRY);
		// end defect 8357
		try
		{
			MQEnvironment.hostname = csHostname;
			MQEnvironment.channel = csMQChannel;
			MQEnvironment.port = ciMQPort;
			MQQueueManager laQMgr = null;

			// Create a connection to the queue manager
			laQMgr = new MQQueueManager(csMQQueueManager);

			// Set up the options on the queue we wish to open...
			int liOpenOptions = MQC.MQOO_OUTPUT;

			// Now specify the queue that we wish to open, 
			// and the open options...

			MQQueue system_default_local_queue =
				laQMgr.accessQueue(
					asQueue,
					liOpenOptions,
					null,
					null,
					null);

			// Define a simple MQ message
			MQMessage laMQMessage = new MQMessage();
			laMQMessage.writeObject(aaObject);

			// specify the message options...
			MQPutMessageOptions laPMO = new MQPutMessageOptions();

			// put the message on the queue
			system_default_local_queue.put(laMQMessage, laPMO);

			// Close the queue
			system_default_local_queue.close();

			// Disconnect from the queue manager
			laQMgr.disconnect();

		}
		catch (MQException aeMQEx)
		{
			// If an error has occured in the above, try to 
			// identify what 
			// went wrong. Was it an MQ error?

			// defect 8547
			// do not print stack trace on empty queue
			// defect 8357
			// log only one MQ_EMPTY_RETURN_CODE
			// defect 10995
/*			if ((aeMQEx.reasonCode == MQ_EMPTY_RETURN_CODE
				&& !cbEmptyMsgWritten)
				|| // log only when aeMQEx.reasonCode != MQ_EMPTY_RETURN_CODE	
			 (
					aeMQEx.reasonCode != MQ_EMPTY_RETURN_CODE))
			{
				if (aeMQEx.reasonCode == MQ_EMPTY_RETURN_CODE)
				{
					cbEmptyMsgWritten = true;
				}*/
				// end log only one MQ_EMPTY_RETURN_CODE
			aeMQEx.printStackTrace();
/*				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeMQEx.getMessage());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REASON_CODE_EQUAL + aeMQEx.reasonCode);

				if (aaObject == null)
				{
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_MQ_MSG_NULL);
				}
				else
				{
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_FAILED_ATTEMPT_TO_REPOST
							+ aaObject.toString());
				}
				*/
			com.txdot.isd.rts.server.webapps.util.MQLog
				.error(aeMQEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(STR_REASON_CODE_EQUAL
							+ aeMQEx.reasonCode);

			if (aaObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_MQ_MSG_NULL);
			} 
			else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(STR_FAILED_ATTEMPT_TO_REPOST
								+ aaObject.toString());
			}
				// end defect 10995
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);
				// end defect 8357	
			// defect 10995	
			// }
			// end defect 10995
			// end defect 8547

		}
		catch (java.io.IOException aeIOEx)
		{
			// Was it a Java buffer space error?
			// System.out.println(MSG_ERR_WRITING_MSG_BUFFER + aeIOEx);
			aeIOEx.printStackTrace();
			// defect 10995
/*			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeIOEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_POSTMSG_JAVA_IO + this.getClass());

			if (aaObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_MQ_MSG_NULL);
			}
			else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_FAILED_ATTEMPT_TO_REPOST + aaObject.toString());
			}*/
			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(aeIOEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(MSG_ERR_POSTMSG_JAVA_IO + this.getClass());

			if (aaObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_MQ_MSG_NULL);
			} else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(STR_FAILED_ATTEMPT_TO_REPOST
								+ aaObject.toString());
			}
			// end defect 10995
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);
		}

		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			// defect 7168
			// defect 10995
/*			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeEx.getMessage());

			//			com.txdot.isd.rts.server.webapps.util.MQLog.error
			//				(aeEx);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_POST_MSG_EXCEPTION + this.getClass());

			if (aaObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_MQ_MSG_NULL);
			}
			else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_FAILED_ATTEMPT_TO_REPOST + aaObject.toString());
			}*/
			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(aeEx.getMessage());
			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(MSG_ERR_POST_MSG_EXCEPTION
							+ this.getClass());

			if (aaObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_MQ_MSG_NULL);
			} else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(STR_FAILED_ATTEMPT_TO_REPOST
								+ aaObject.toString());
			}
			// end defect 10995
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);
		}

		finally
		{
			// defect 8357
			// Log.write(Log.METHOD, this, TXT_POSTMESSAGE_EXIT);
			write(this.getClass() + " " + TXT_POSTMESSAGE_EXIT);
			// end defect 8357
		}
	}

	/**
	 * This method is called by the MQAccess application when a 
	 * RefundObject is retrieved from the MQ Series queue.  If the 
	 * processing of the objects fails, the object is placed back on the
	 * queue for retry at a later time.
	 * 
	 * @param asQueuename String
	 * @param aaObject Object
	 * @throws RTSException
	 */
	private void processRefundReply(
		String asQueueName,
		Object aaObject)
		throws RTSException
	{
		RegRenProcessingServerBusiness laRegRenBO =
			new RegRenProcessingServerBusiness();

		// defect 8777
		com.txdot.isd.rts.services.data.RefundData laRefund = null;
		// com.txdot.isd.rts.client.webapps.data.RefundData represents
		// the 5.2.2 (TxO) version of the RefundData object
		if (aaObject
			instanceof com.txdot.isd.rts.client.webapps.data.RefundData)
		{
			laRefund =
				new com.txdot.isd.rts.services.data.RefundData(
					(com
						.txdot
						.isd
						.rts
						.client
						.webapps
						.data
						.RefundData) aaObject);
		}
		else
		{
			laRefund =
				(com.txdot.isd.rts.services.data.RefundData) aaObject;
		}

		// RefundData laRefund = (RefundData) aaObject;
		// end defect 8777	

		Object laResult = null;
		boolean lbRefundOk = false;

		try
		{
			// defect 8357
			write(STR_PROCESSING_REFUND + laRefund.getOrigTraceNo());

			write(STR_REFUND_STATUS + laRefund.getRefundStatus());

			write(STR_PROCESSING_REFUND_FOLLOWING_REC);

			write(
				STR_VEH_PLATE_NO
					+ laRefund.getVehBaseData().getPlateNo());

			write(STR_VEH_VIN + laRefund.getVehBaseData().getVin());

			write(
				STR_VEH_DOC_NO + laRefund.getVehBaseData().getDocNo());

			write(
				STR_VEH_COUNTY
					+ laRefund.getVehBaseData().getOwnerCounty());

			write(
				STR_VEH_COUNTY_NO
					+ laRefund.getVehBaseData().getOwnerCountyNo());

			write(STR_REFUND_ORIG_TRACE_NO + laRefund.getOrigTraceNo());

			write(STR_REFUND_STATUS + laRefund.getRefundStatus());

			write(
				STR_REFUND_PAYMENT_ORDER_ID
					+ laRefund.getPymtOrderId());

			write(STR_REFUND_AMOUNT + laRefund.getRefAmt());

			write(STR_END_OF_REFUND_REQUEST);

			//com.txdot.isd.rts.server.webapps.util.MQLog.error
			//(STR_DOUBLE_LINES);
			write(STR_DOUBLE_LINES);
			// end defect 8357

			laResult =
				laRegRenBO.processData(
					CommonConstants.REGISTRATION_RENEWAL,
					RegRenProcessingConstants.PROC_REFUND_RESULT,
					laRefund);
			lbRefundOk = ((Boolean) laResult).booleanValue();

			if (!lbRefundOk)
			{
				postMessage(asQueueName, laRefund);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_DID_NOT_SEND_REFUND + this.getClass());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_REPOSTING_DUE_TO_ERROR);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_VEH_PLATE_NO
						+ laRefund.getVehBaseData().getPlateNo());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_VEH_VIN + laRefund.getVehBaseData().getVin());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_VEH_DOC_NO
						+ laRefund.getVehBaseData().getDocNo());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_VEH_COUNTY
						+ laRefund.getVehBaseData().getOwnerCounty());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_VEH_COUNTY_NO
						+ laRefund.getVehBaseData().getOwnerCountyNo());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REFUND_ORIG_TRACE_NO
						+ laRefund.getOrigTraceNo());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REFUND_STATUS + laRefund.getRefundStatus());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REFUND_PAYMENT_ORDER_ID
						+ laRefund.getPymtOrderId());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REFUND_AMOUNT + laRefund.getRefAmt());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_END_OF_REFUND_REPLY_REQUEST);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);

				try
				{
					// defect 8547
					// uncomment message and enhance
					// only sleep 30 seconds.  Why are we sleeping?
					// defect 8357
					write(MSG_REFUND_NOT_OK_SLEEP);
					// end defect 8357	
					Thread.sleep(SLEEP_30_SECONDS);
					// end defect 8547
				}
				catch (Exception aeEx)
				{
					aeEx.printStackTrace();

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						aeEx.getMessage());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						aeEx);

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_ERR_REFUND_REPLY_SLEEP + this.getClass());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_DOUBLE_LINES);

				}
			}
		}
		catch (Exception aeEx)
		{
			// rts.printStackTrace();
			//  If we encountered an error while servicing the
			//  message, put it back onto the queue
			postMessage(asQueueName, laRefund);
			// System.out.println(MSG_ERR_PROCESSING_REFUND_REPLY);
			aeEx.printStackTrace();
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(aeEx);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_CAUGHT_EXCEPTION_PROCESS_REFUND_REPLY
					+ this.getClass());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_REPOSTING_REFUND_REQUEST);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_VEH_PLATE_NO
					+ laRefund.getVehBaseData().getPlateNo());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_VEH_VIN + laRefund.getVehBaseData().getVin());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_VEH_DOC_NO + laRefund.getVehBaseData().getDocNo());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_VEH_COUNTY
					+ laRefund.getVehBaseData().getOwnerCounty());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_VEH_COUNTY_NO
					+ laRefund.getVehBaseData().getOwnerCountyNo());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_REFUND_ORIG_TRACE_NO + laRefund.getOrigTraceNo());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_REFUND_STATUS + laRefund.getRefundStatus());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_REFUND_PAYMENT_ORDER_ID
					+ laRefund.getPymtOrderId());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_REFUND_AMOUNT + laRefund.getRefAmt());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_END_OF_REFUND_REPLY_REQUEST);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);

			try
			{
				// System.out.println("Sleeping for 60 seconds.");
				Thread.sleep(SLEEP_60_SECONDS);
			}
			catch (Exception aeEx2)
			{
				aeEx2.printStackTrace();
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx2.getMessage());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx2);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_REFUND_REPLY_SLEEP_ERROR + this.getClass());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);
			}
		}
	}

	/**
	 * This method is called by the MQAccess application when a 
	 * RenewalObject is retrieved from the MQ Series queue.  If the 
	 * processing of the objects fails, the object is placed back on the
	 * queue for retry at a later time.
	 * 
	 * @param asQueuename String
	 * @param aaObject Object
	 * @throws RTSException
	 *
	 */
	private void processRenewal(String asQueueName, Object aaObject)
		throws RTSException
	{
		RegistrationRenewalServerBusiness aaRegRenBO =
			new RegistrationRenewalServerBusiness();
		RenewalShoppingCart laPullCart = (RenewalShoppingCart) aaObject;
		Object laResult = null;
		boolean lbRegistrationOk = false;

		CompleteRegRenData laCRRD = null;
		VehicleUserData laVUD = null;

		try
		{
			// defect 8357
			//			write(	
			//				STR_PROCESSING_RENEWAL_TRACE_NO +
			//				laPullCart.getTraceNo());
			// end defect 8357		

			for (int i = 0; i < laPullCart.size(); i++)
			{
				laCRRD = (CompleteRegRenData) laPullCart.elementAt(i);
				laVUD = laCRRD.getVehUserData();
				// defect 8357
				write(STR_PROCESSING_RENEWAL_FOLLOWING_REC);

				write(
					STR_NAME_SPACES_COLON
						+ laVUD.getFirstName()
						+ CommonConstant.STR_SPACE_ONE
						+ laVUD.getMiddleName()
						+ CommonConstant.STR_SPACE_ONE
						+ laVUD.getLastName());

				write(STR_EMAIL_ADDR_SPACES_COLON_2 + laVUD.getEmail());

				write(
					STR_PHONE_NUMBER_SPACES_COLON_2
						+ laVUD.getPhoneNumber());

				write(
					STR_REGIS_TRACE_NO_EQUAL + laPullCart.getTraceNo());

				write(
					STR_VEH_PLATE_NO
						+ laCRRD.getVehBaseData().getPlateNo());

				write(STR_VEH_VIN + laCRRD.getVehBaseData().getVin());

				write(
					STR_VEH_OWNER_NAME_EQUAL
						+ laVUD.getFirstName()
						+ CommonConstant.STR_SPACE_ONE
						+ laVUD.getMiddleName()
						+ CommonConstant.STR_SPACE_ONE
						+ laVUD.getLastName());

				write(STR_EMAIL_ADDR_SPACES_COLON_1 + laVUD.getEmail());

				write(
					STR_PHONE_NUMBER_SPACES_COLON_1
						+ laVUD.getPhoneNumber());

				write(
					STR_ITRNTPYMNT_STATUS_CD_EQUAL
						+ laCRRD.getItrntPymntStatusCd());

				write(
					STR_PAYMENT_ORDER_ID_EQUAL
						+ laCRRD.getPymntOrderId());

				write(STR_TOTAL_FEES_EQUAL + laPullCart.getFeesTotal());

				write(STR_END_OF_REGIS_LOG);

				//com.txdot.isd.rts.server.webapps.util.MQLog.error
				write(STR_DOUBLE_LINES);
				// end defect 8357		
			}
			laResult =
				aaRegRenBO.processData(
					CommonConstants.REGISTRATION_RENEWAL,
					RegistrationRenewalConstants.DO_REG_RENEWAL,
					laPullCart);

			lbRegistrationOk = ((Boolean) laResult).booleanValue();

			// defect 8547
			// handle empty shopping cart differently
			// Thre was a problem doing the renewal
			if (!lbRegistrationOk)
			{
				// There is shopping cart data
				if (laPullCart.size() > 0)
				{
					// end defect 8547
					postMessage(asQueueName, laPullCart);
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_DID_NOT_PROCESS_REGIS_PAYMENT
							+ this.getClass());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_REPOSTING_DUE_TO_ERROR);

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_REGIS_TRACE_NO_EQUAL
							+ laPullCart.getTraceNo());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_VEH_PLATE_NO
							+ laCRRD.getVehBaseData().getPlateNo());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_VEH_VIN + laCRRD.getVehBaseData().getVin());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_VEH_OWNER_NAME_EQUAL
							+ laVUD.getFirstName()
							+ CommonConstant.STR_SPACE_ONE
							+ laVUD.getMiddleName()
							+ CommonConstant.STR_SPACE_ONE
							+ laVUD.getLastName());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_EMAIL_ADDR_SPACES_COLON_1
							+ laVUD.getEmail());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_PHONE_NUMBER_SPACES_COLON_1
							+ laVUD.getPhoneNumber());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_ITRNTPYMNT_STATUS_CD_EQUAL
							+ laCRRD.getItrntPymntStatusCd());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_PAYMENT_ORDER_ID_EQUAL
							+ laCRRD.getPymntOrderId());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_TOTAL_FEES_EQUAL
							+ laPullCart.getFeesTotal());

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_END_OF_REGIS_REPOST);

					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_DOUBLE_LINES);

					try
					{
						// System.out.println
						// ("Sleeping for 60 seconds.");
						Thread.sleep(SLEEP_60_SECONDS);
					}
					catch (Exception aeEx)
					{
						// defect 7168
						aeEx.printStackTrace();
						com
							.txdot
							.isd
							.rts
							.server
							.webapps
							.util
							.MQLog
							.error(
							aeEx.getMessage());
						com
							.txdot
							.isd
							.rts
							.server
							.webapps
							.util
							.MQLog
							.error(
							aeEx);
						com
							.txdot
							.isd
							.rts
							.server
							.webapps
							.util
							.MQLog
							.error(
							MSG_ERR_SLEEP_INTERRUPTED_PROCESSRENEWAL
								+ this.getClass());
						com
							.txdot
							.isd
							.rts
							.server
							.webapps
							.util
							.MQLog
							.error(
							STR_DOUBLE_LINES);
						// end defect 7168
					}
					// defect 8547
				}
				else
				{
					// There is no shopping cart data
					// This is assumed to be a funky record
					// Front end editting should never let this happen
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_SHOPPING_CART_EMPTY);
					//System.out.println(MSG_SHOPPING_CART_EMPTY);
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_DO_NOT_REPOST);
					//System.out.println(MSG_DO_NOT_REPOST);
				}
				// end defect 8547
			}
		}

		// defect 8547
		// handle null pointer in this code more cleanly
		catch (NullPointerException aeNPEx)
		{
			aeNPEx.printStackTrace();
			// defect 8357
			//System.out.println(laPullCart.toString());
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				laPullCart.toString());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_NPR_PROCESSING_EXCEPTION);
		}
		// end defect 8547

		catch (Exception aeEx)
		{
			//  If we encountered an error while servicing the
			//  message, put it back onto the queue
			postMessage(asQueueName, laPullCart);

			// System.out.println(MSG_ERR_PROCESSING_RENEWAL_SLEEP);
			// print the stack trace to give more information
			aeEx.printStackTrace();

			// defect 7168
			// only print the message if it is not null

			if (aeEx.getMessage() != null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx.getMessage());
			}

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeEx.toString());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(aeEx);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_DID_NOT_PROCESS_REG_PAYMENT + this.getClass());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_REPOSTING_DUE_TO_ERROR);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_END_OF_REGIS_REPOST);

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);

			// end defect 7168
			try
			{
				// defect 8547
				// only sleep for 30 seconds
				// System.out.println("Sleeping for 30 seconds.");
				Thread.sleep(SLEEP_30_SECONDS);
				// end defect 8547
			}
			catch (Exception aeEx2)
			{
				// defect 7168
				aeEx2.printStackTrace();

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx2.getMessage());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx2);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_SLEEP_PROCESSRENEWAL + this.getClass());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);

				// end defect 7168
			}
		}
	}

	/**
	 * This method is called by the MQAccess application when a 
	 * Special Plate Order Object is retrieved from the MQ Series queue.
	 * If the processing of the objects fails, the object is placed back
	 * on the queue for retry at a later time.
	 * 
	 * @param asQueuename String
	 * @param aaObject Object
	 * @throws RTSException
	 *
	 */
	private void processOrder(String asQueueName, Object aaObject)
		throws RTSException
	{
		SpecialPltOrdrCart laSPOrderCart =
			(SpecialPltOrdrCart) aaObject;
		TransactionAccessBusiness laTransAccBus =
			new TransactionAccessBusiness();
		// Used to determine if we should re-post the shopping cart.
		boolean lbRepost = false;
		
		try
		{
			for (int i = 0; i < laSPOrderCart.size(); i++)
			{
				// Get the shopping cart Item.
				SpecialPltOrdrCartItem laSPOrderCartItem =
					(SpecialPltOrdrCartItem) laSPOrderCart.elementAt(i);
					
				write(STR_PROCESSING_ORDER_FOLLOWING_REC);
				write(
					STR_REGIS_TRACE_NO_EQUAL
						+ laSPOrderCartItem.getItrntTraceNo());
				write(
					STR_VEH_PLATE_NO + laSPOrderCartItem.getRegPltNo());
				write(
					STR_ITRNTPYMNT_STATUS_CD_EQUAL
						+ laSPOrderCart.getItrntPymntStatusCd());
				write(
					STR_PAYMENT_ORDER_ID_EQUAL
						+ laSPOrderCart.getPymntOrderID());
				write(
					STR_TOTAL_FEES_EQUAL
						+ laSPOrderCart.getFeesTotal());

				// Only try to update the transaction if it has not been
				// updated already.  If one item in the shopping cart
				// does not get updated correctly at TexasOnline then
				// we put the entire shopping cart on MQ.
				if (!laSPOrderCartItem.isUpdateTrans())
				{
					write(STR_NEEDS_UPDATING);
					// Create the Special Plate Trans Object and load
					// it with the needed data from the shopping cart 
					// item and shopping cart values.
					SpecialPlateItrntTransData laSPTransData =
						new SpecialPlateItrntTransData();
					laSPTransData.setRegPltCd(
						laSPOrderCartItem
							.getSpecialPlateSelection()
							.getRegPltCd());
					laSPTransData.setRegPltNo(
						laSPOrderCartItem.getRegPltNo());
					laSPTransData.setItrntPymntStatusCd(
						laSPOrderCart.getItrntPymntStatusCd());
					laSPTransData.setEpayRcveTimeStmp(
						laSPOrderCart.getEpayRcveTimeStmp());
					laSPTransData.setEpaySendTimeStmp(
						laSPOrderCart.getEpayRcveTimeStmp());
					laSPTransData.setPymntOrderId(
						laSPOrderCart.getPymntOrderID());
					laSPTransData.setPymntAmt(laSPOrderCart.getItemFeesTotal(i));
					laSPTransData.setItrntTraceNo(laSPOrderCart.getTraceNo());
						
					// Issue the actual update.  This call will also
					// confirm the inventory order.  If we where to get
					// and error we will set the boolean that puts the
					// entire cart back on the queue.
					try
					{
						laTransAccBus.updateTransaction(
							laSPTransData,
							true,
							laSPOrderCart.isCreatePOSTrans());
						laSPOrderCartItem.setUpdateTrans(true);
					}
					catch (RTSException aeRTSEx)
					{
						lbRepost = true;
						// print the stack trace to give more information
						aeRTSEx.printStackTrace();

						if (aeRTSEx.getMessage() != null)
						{
							com
								.txdot
								.isd
								.rts
								.server
								.webapps
								.util
								.MQLog
								.error(
								aeRTSEx.getMessage());
						}
					}
				}
				else
				{
					write(STR_ALREADY_UPDATED);
				}
				write(STR_END_OF_REGIS_LOG);
				write(STR_DOUBLE_LINES);
			}

			if (lbRepost && laSPOrderCart.size() > 0)
			{
				postMessage(asQueueName, laSPOrderCart);
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Did not process the SP Order" + this.getClass());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Reposting the SP order payment data for the following "
						+ "record, due to the above error");
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REGIS_TRACE_NO_EQUAL
						+ laSPOrderCart.getTraceNo());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_VEH_PLATE_NO
						+ laSPOrderCart.elementAt(0).getRegPltNo());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_ITRNTPYMNT_STATUS_CD_EQUAL
						+ laSPOrderCart.getItrntPymntStatusCd());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_PAYMENT_ORDER_ID_EQUAL
						+ laSPOrderCart.getPymntOrderID());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_TOTAL_FEES_EQUAL
						+ laSPOrderCart.getFeesTotal());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"END OF ORDER REPOST");
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);

				try
				{
					Thread.sleep(SLEEP_60_SECONDS);
				}
				catch (Exception aeEx)
				{
					aeEx.printStackTrace();
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						aeEx.getMessage());
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						aeEx);
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_ERR_SLEEP_INTERRUPTED_PROCESSRENEWAL
							+ this.getClass());
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						STR_DOUBLE_LINES);
				}
			}
			else
			{
				// There is no shopping cart data
				// This is assumed to be a funky record
				// Front end editting should never let this happen
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_SHOPPING_CART_EMPTY);
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_DO_NOT_REPOST);
			}
		}
		catch (NullPointerException aeNPEx)
		{
			aeNPEx.printStackTrace();
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				laSPOrderCart.toString());
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_NPR_PROCESSING_EXCEPTION);
		}
		catch (Exception aeEx)
		{
			//  If we encountered an error while servicing the
			//  message, put it back onto the queue
			postMessage(asQueueName, laSPOrderCart);

			// print the stack trace to give more information
			aeEx.printStackTrace();

			if (aeEx.getMessage() != null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx.getMessage());
			}

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeEx.toString());
			com.txdot.isd.rts.server.webapps.util.MQLog.error(aeEx);
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				"Did not process the SP Order"
					+ this.getClass());
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
			"Reposting the SP order payment data for the following "
			+ "record, due to the above error");
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
			"End of SP Order repost log info");

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);
			try
			{
				Thread.sleep(SLEEP_30_SECONDS);
			}
			catch (Exception aeEx2)
			{
				aeEx2.printStackTrace();
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx2.getMessage());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeEx2);
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
				"The above SP order sleep error has occurred in method "
				+ "processRenewal, class:" + this.getClass());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);
			}
		}
	}

	/**
	 * Retrieves a Java Objects from the specified MQ Series queue.
	 * 
	 * @param asQueue String
	 * @return CompleteRegRenData
	 */
	public Object retrieveMessage(String asQueue)
	{

		// defect 8357
		Log.write(Log.METHOD, this, TXT_RETREIVE_MESSAGE_BEGIN);
		//		write(this.getClass() + 
		//			" " + TXT_RETREIVE_MESSAGE_BEGIN);
		// end defect 8357

		Object laObject = null;
		MQEnvironment.hostname = csHostname;
		MQEnvironment.channel = csMQChannel;
		MQEnvironment.port = ciMQPort;
		MQQueueManager laQMgr = null;
		MQQueue system_default_local_queue = null;

		try
		{
			// Create a connection to the queue manager
			laQMgr = new MQQueueManager(csMQQueueManager);

			// Set up the options on the queue we wish to open...
			// Note. All MQ Options are prefixed with MQC in Java.

			int liOpenOptions = MQC.MQOO_INPUT_AS_Q_DEF;

			// Now specify the queue that we wish to open, and the open 
			// options...

			system_default_local_queue =
				laQMgr.accessQueue(asQueue, liOpenOptions, null,
				// default q manager
		null, // no dynamic q name
	null); // no alternate user id

			// get the message back again...
			// First define a MQ message buffer to receive the message 
			// into..
			MQMessage laRetrievedMessage = new MQMessage();
			// retrievedMessage.messageId = hello_world.messageId;

			// Set the get message options..
			MQGetMessageOptions leGMO = new MQGetMessageOptions();
			// accept the defaults
			// same as MQGMO_DEFAULT

			// get the message off the queue..
			system_default_local_queue.get(laRetrievedMessage, leGMO);
			// max message size

			// And prove we have the message by displaying the
			//UTF message text
			laObject = (Object) laRetrievedMessage.readObject();
			// System.out.println("The message is: " + msgText);

			// defect 8357
			// set this boolean to false so we write an empty
			// message
			// defect 10995
/*			if (cbEmptyMsgWritten)
			{
				MQLog.info(PROCESSING_RECS);
				cbEmptyMsgWritten = false;
			}*/
			// end defect 10995
			// end defect 8357

		}
		catch (MQException aeMQEx)
		{
			//			 defect 10995
			/*if ((aeMQEx.reasonCode == MQ_EMPTY_RETURN_CODE
				&& !cbEmptyMsgWritten)
				|| // log only when aeMQEx.reasonCode != MQ_EMPTY_RETURN_CODE	
				(
					aeMQEx.reasonCode != MQ_EMPTY_RETURN_CODE))
				{
				if (aeMQEx.reasonCode == MQ_EMPTY_RETURN_CODE)
				{
					cbEmptyMsgWritten = true;
				}*/
			if (aeMQEx.reasonCode == MQ_EMPTY_RETURN_CODE)
			{				
				if (!cbEmptyMsgWritten)
				{
					cbEmptyMsgWritten = true;
					com.txdot.isd.rts.server.webapps.util.MQLog
							.error(" : MQ has been started." +
								   " MQException error code = "
									+ MQ_EMPTY_RETURN_CODE
									+ " occurred. ");
				}
			}
			else
			{
			// end defect 10995	
				// end log only one MQ_EMPTY_RETURN_CODE
				// defect 8547
				// move the stack trace into the if block
				aeMQEx.printStackTrace();
				// end defect 8547
				// write to mq error log
				// date/time in error log
				// throw new RTSException(RTSException.MQ_ERROR, ex);
				laObject = null;
				// defect 7168
				// defect 10995
/*				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeMQEx.getMessage());*/
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(" : aeMQEx.getMessage() = "
								+ aeMQEx.getMessage());
				// end defect 10995

				//				com.txdot.isd.rts.server.webapps.util.MQLog.error
				//					(aeMQEx);

				//				com.txdot.isd.rts.server.webapps.util.MQLog.error
				//					(STR_COMPLETION_CODE_EQUAL + 
				//					aeMQEx.completionCode);
				//					
				//				com.txdot.isd.rts.server.webapps.util.MQLog.error
				//					(STR_REASON_CODE_EQUAL + 
				//					aeMQEx.reasonCode);
				// defect 10995
/*				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_MQ_ERROR_IN_RETRIEVEMESSAGE
						+ this.getClass());*/
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_ERR_MQ_ERROR_IN_RETRIEVEMESSAGE
								+ this.getClass());
				// end defect 10995

				// don't log a null message error
				// defect 10995
/*				if (laObject == null)
				{
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_ERR_ATTEMPT_TO_LOAD_NULL_OBJECT);
					
				}
				else
				{
					com.txdot.isd.rts.server.webapps.util.MQLog.error(
						MSG_RETRIEVED_MSG_IS + laObject.toString());
				}
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);*/
				if (laObject == null)
				{
					com.txdot.isd.rts.server.webapps.util.MQLog
							.error(MSG_ERR_ATTEMPT_TO_LOAD_NULL_OBJECT);
				} else
				{
					com.txdot.isd.rts.server.webapps.util.MQLog
							.error(MSG_RETRIEVED_MSG_IS
									+ laObject.toString());
				}
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);
				// end defect 10995
				// end defect 7168
				// end defect 8357
				// defect 8547
				//aeMQEx.printStackTrace();
				// end defect 8547
			}

		}
		catch (java.io.IOException aeIOEx)
		{
			// Was it a Java buffer space error?
			// throw new RTSException(RTSException.JAVA_ERROR, ex);
			laObject = null;
			// defect 7168
			// defect 10995
/*			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeIOEx.getMessage());
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_IO_ERROR_IN_RETRIEVEMESSAGE + this.getClass());

			if (laObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_MQ_ATTEMPT_TO_LOAD_NULL_OBJECT_2);
			}
			else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_RETRIEVED_MSG_IS + laObject.toString());
			}	*/
			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(aeIOEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(MSG_ERR_IO_ERROR_IN_RETRIEVEMESSAGE
							+ this.getClass());

			if (laObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_ERR_MQ_ATTEMPT_TO_LOAD_NULL_OBJECT_2);
			} else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_RETRIEVED_MSG_IS
								+ laObject.toString());
			}
			// end defect 10995
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);

			aeIOEx.printStackTrace();
			// end defect 7168
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			// throw new RTSException(RTSException.JAVA_ERROR, ex);
			laObject = null;
			// defect 7168
			// defect 10995
/*			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				aeCNFEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				MSG_ERR_CLASS_NOT_FOUND_IN_RETREIVEMESSAGE
					+ this.getClass());

			if (laObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_MQ_ATTEMPT_TO_LOAD_NULL_OBJECT_2);
			}
			else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_RETRIEVED_MSG_IS + laObject.toString());
			}
			*/
			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(aeCNFEx.getMessage());

			com.txdot.isd.rts.server.webapps.util.MQLog
					.error(MSG_ERR_CLASS_NOT_FOUND_IN_RETREIVEMESSAGE
							+ this.getClass());

			if (laObject == null)
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_ERR_MQ_ATTEMPT_TO_LOAD_NULL_OBJECT_2);
			} else
			{
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_RETRIEVED_MSG_IS
								+ laObject.toString());
			}
			 // end defect 10995
			com.txdot.isd.rts.server.webapps.util.MQLog.error(
				STR_DOUBLE_LINES);

			aeCNFEx.printStackTrace();
			// end defect 7168
		}
		finally
		{
			try
			{
				// Close the queue
				if (system_default_local_queue != null)
				{
					system_default_local_queue.close();
				}
			}

			catch (MQException aeMQEx)
			{
				// defect 7168
				// defect8357
				// defect 10995
/*				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeMQEx.getMessage());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_QUEUE_CLOSING_ERR_RETREIVEMESSAGE
						+ this.getClass());*/
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(aeMQEx.getMessage());
				com.txdot.isd.rts.server.webapps.util.MQLog
						.error(MSG_ERR_QUEUE_CLOSING_ERR_RETREIVEMESSAGE
								+ this.getClass());
				// end defect 10995	
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);
				aeMQEx.printStackTrace();
				// end defect 7168				
			}

			try
			{
				// Disconnect from the queue manager
				if (laQMgr != null)
				{
					laQMgr.disconnect();
				}
			}
			catch (MQException aeMQEx)
			{
				// defect 7168
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeMQEx.getMessage());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					aeMQEx);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_COMPLETION_CODE_EQUAL + aeMQEx.completionCode);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_REASON_CODE_EQUAL + aeMQEx.reasonCode);

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					MSG_ERR_QUEUE_DISCONNECT + this.getClass());

				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					STR_DOUBLE_LINES);

				aeMQEx.printStackTrace();
				// end defect 7168
			}
			// defect 8357
			Log.write(Log.METHOD, this, TXT_RETREIVEMESSAGE_END);
			//			write(this.getClass() + 
			//				" " + TXT_RETREIVEMESSAGE_END);
			// end defect 8357
		}
		return laObject;
	}

	/**
	 * Writes messages to the Batch Log file
	 * 
	 * @param message java.lang.String
	 */
	public static void write(String asMessage)
	{
		RTSDate laDate = RTSDate.getCurrentDate();
		System.out.println(
			laDate + " - " + laDate.getClockTime() + " - " + asMessage);
	}

}
