package com.txdot.isd.rts.server.dataaccess;

import com.ibm.mq.*;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.CompleteRegRenData;
import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.data.RenewalShoppingCart;
import com.txdot.isd.rts.services.data.VehicleUserData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
/*
 *
 * MQRestore.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		09/27/2006	Create MQRestore as an application Client on 
 * 							the new WAS5 server, and read the MQ parms 
 * 							from SystemProperty.
 * 							defect 8953 Ver 5.2.6
 *----------------------------------------------------------------------
 */

/**
 * The MQRestore class is used to retrieve messages from the MQ backup
 * queues. MQ Series is used as the interface for passing messages (Java
 * Objects) between TxDOT and TexasOnline for the processing of Phase 2
 * transactions.
 *
 * @version	5.2.6		09/27/2006 
 * @author	Richard Hicks
 * <br>Creation Date:	08/12/2002	10:33:49
 */
public class MQRestore
{
	/** The port of the MQ Series Server **/
	private int ciMQPort;
	/** The channel of the MQ Series Server **/
	private String csMQChannel;
	/** The Queue Manager name of the MQ Series Server **/
	private String csMQQueueManager;
	/** The csHostname of the MQ Series Server **/
	private String csHostname;

	//	private final static String RENEWAL_SER = "renewal.ser";
	//	private final static String USER_DIR = "user.dir";
	//	private final static String QUEUE_NAMES =  
	//	"MQRestore host_name channel_name queue_manager 
	//  queue_name port";
	//	private final static String GET_USER_DIR =
	//		"System.getProperty(user.dir) = ";
	private final static String RENEWAL_MSG = "Message is a Renewal";
	private final static String REFUND_MSG = "Message is a Refund";
	private final static String FORMAT_UNKNOWN_MSG =
		"**** ERROR : Message format is unknown.  ****";
	private final static String POST_MSG_ENTRY = 
		" - postMessage() - Entry.";
	private final static String POST_MSG_EXIT = 
" - postMesage() - Exit.";
	private final static String MQ_ERROR_MSG =
		"An MQ error occurred : Completion code ";
	private final static String REASON_CODE = " Reason code ";
	private final static String MESSAGE_BUFFER_MSG =
		"An error occurred while writing to the message buffer: ";
	private final static String PROCESSING_REFUND = 
		"Processing Refund : ";
	//private final static String PROCESSING_RENEWAL = 
	// "Processing a Renewal : ";
	private final static String REFUND_STATUS = "Refund Status Code = ";
	private final static String PROCESS_RENEWAL_ERROR =
		"Error processing renewal : sleeping for 60 seconds";
	private final static String STR_NAME = "Name                 : ";
	private final static String SPACE = " ";
	private final static String STR_EMAIL_ADDR = "e-mail Address : ";
	private final static String STR_PHONE_NUMBER = "Phone Number : ";
	private final static String RETRIEVE_MSG_ENTRY =
		" - retrieveMesage() - Entry.";
	private final static String RETRIEVE_MSG_EXIT =
		" - retrieveMesage() - Exit.";


	/**
	 * MQRestore Constructor.
	 * 
	 * @param asHost String
	 * @param asChannel String
	 * @param asQueueManager String
	 * @param aiPort int
	 */
	public MQRestore(
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
	 * Main method for execution of the MQRestore class 
	 * as a client application.
	 * 
	 * @param aarrArgs String[] 
	 */
	public static void main(String[] aarrArgs)
	{

		try
		{

			Comm.setIsServer(true);
			int liPort = 0;
			String lsHostName = SystemProperty.getMQHostName();
			String lsChannelName = SystemProperty.getMQChannelName();
			String lsQueueManager = SystemProperty.getMQQueueManager();
			String lsQueueName = SystemProperty.getMQQueueName();
			String lsBackupQueueName = 
				SystemProperty.getMQBackupQueueName();
			if (SystemProperty.getMQPort() != null)
			{
				liPort = Integer.parseInt(SystemProperty.getMQPort());
			}

			System.out.println(
				"lsHostName = SystemProperty.getMQHostName() = " + 
					lsHostName);
			System.out.println(
				"lsChannelName = SystemProperty.getMQChannelName() = "
					+ lsChannelName);
			System.out.println(
				"lsQueueManager = SystemProperty.getMQQueueManager() = "
					+ lsQueueManager);
			System.out.println(
				"lsQueueName = SystemProperty.getMQQueueName() = "
					+ lsQueueName);
			System.out.println(
				"lsBackupQueueName = "
					+ "SystemProperty.getMQBackupQueueName() = "
					+ lsBackupQueueName);
			System.out.println(
				"liPort = Integer.parseInt" +				"(SystemProperty.getMQPort() = "
					+ liPort);

			if (lsHostName == null
				|| lsChannelName == null
				|| lsQueueManager == null
				|| lsQueueName == null
				|| lsBackupQueueName == null
				|| liPort == 0)
			{
				System.out.println(
					new java.util.Date()
						+ " An MQ parameter is null or missing " +							"from server.cfg");
			}

			else
			{
				MQRestore laReceiver = null;
				Object laMessage = null;
				// the below code is something MSSave does
				//			try
				//			{
				//				System.out.println
				//					(GET_USER_DIR + 
				//						System.getProperty(USER_DIR));
				//				// if the file does not exist, create it	
				//				if (!new File(RENEWAL_SER).exists())
				//				{
				//					new File(RENEWAL_SER);
				//				}
				//			}
				//			
				//			catch (Exception aeEx)
				//			{
				//				aeEx.printStackTrace();
				//			}
				while (true)
				{
					laReceiver =
						new MQRestore(
							lsHostName,
							lsChannelName,
							lsQueueManager,
							liPort);
					laMessage = 
						laReceiver.retrieveMessage(lsBackupQueueName);
						
					if (laMessage != null)
					{
						if (laMessage instanceof RenewalShoppingCart)
						{
							System.out.println(RENEWAL_MSG);
							try
							{
								laReceiver.processRenewal(
									lsQueueName,
									lsBackupQueueName,
									laMessage);

							}
							catch (Exception aeEx)
							{
								aeEx.printStackTrace();
							}
						}
						else if (laMessage instanceof RefundData)
						{
							System.out.println(REFUND_MSG);
							laReceiver.processRefundReply(
								lsQueueName,
								lsBackupQueueName,
								laMessage);
						}
						else
						{
							System.out.println(FORMAT_UNKNOWN_MSG);
						}						
					}
				}
			}

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
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
		Log.write(Log.METHOD, this, POST_MSG_ENTRY);

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
				laQMgr.accessQueue
					(asQueue, liOpenOptions, null, null, null);

			// Define a simple MQ message
			MQMessage mqMessage = new MQMessage();
			mqMessage.writeObject(aaObject);

			// specify the message options...
			MQPutMessageOptions laPMO = new MQPutMessageOptions();

			// put the message on the queue
			system_default_local_queue.put(mqMessage, laPMO);

			// Close the queue
			system_default_local_queue.close();

			// Disconnect from the queue manager
			laQMgr.disconnect();

		}

		// If an error has occured in the above, try to identify what 
		// went wrong. Was it an MQ error?

		catch (MQException aeMQEx)
		{
			System.out.println(
				MQ_ERROR_MSG
					+ aeMQEx.completionCode
					+ REASON_CODE
					+ aeMQEx.reasonCode);
			aeMQEx.printStackTrace();
		}
		// Was it a Java buffer space error?
		catch (java.io.IOException aeIOEx)
		{
			System.out.println(MESSAGE_BUFFER_MSG + aeIOEx);
		}

		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
		finally
		{
			Log.write(Log.METHOD, this, POST_MSG_EXIT);
		}

	}
	/**
	 * This method is called by the MQRestore application when a 
	 * Refund object is retrieved from the MQ Series queue.  
	 * If the processing of the objects fails, the object is 
	 * placed back on the queue for retry at a later time.
	 * 
	 * @param asQueueName String
	 * @param asBackupQueueName String
	 * @param aaObject Object
	 * @throws RTSException
	 */
	private void processRefundReply(
		String asQueueName,
		String asBackupQueueName,
		Object aaObject)
		throws RTSException
	{
		RefundData laRefund = (RefundData) aaObject;

		try
		{
			System.out.println(PROCESSING_REFUND + 
				laRefund.getOrigTraceNo());
			System.out.println(REFUND_STATUS + 
				laRefund.getRefundStatus());
			postMessage(asQueueName, laRefund);
		}
		catch (Exception aeEx)
		{
			postMessage(asQueueName, laRefund);
			System.out.println(PROCESS_RENEWAL_ERROR);
			try
			{
				// System.out.println("Sleeping for 60 seconds.");
				Thread.sleep(60000);
			}
			catch (Exception aeEx2)
			{
				// empty code block
			}
		}
	}
	/**
	 * This method is called by the MQRestore application when a 
	 * RenewalObject is retrieved from the MQ Series queue.  If the 
	 * processing of the objects fails, the object is placed back on the
	 * queue for retry at a later time.
	 * 
	 * @param asQueuename String
	 * @param asBackupQueueName String
	 * @param aaObject Object
	 * @throws RTSException
	 *
	**/
	private void processRenewal(
		String asQueueName,
		String asBackupQueueName,
		Object aaObject)
		throws RTSException
	{
		RenewalShoppingCart laPullCart = (RenewalShoppingCart) aaObject;

		try
		{
			System.out.println("Restoring" + laPullCart.getTraceNo());

			for (int i = 0; i < laPullCart.size(); i++)
			{
				CompleteRegRenData laData =
					(CompleteRegRenData) laPullCart.elementAt(i);
				VehicleUserData laVehUserData = laData.getVehUserData();
				System.out.println(
					STR_NAME
						+ laVehUserData.getFirstName()
						+ SPACE
						+ laVehUserData.getMiddleName()
						+ SPACE
						+ laVehUserData.getLastName());
				System.out.println(STR_EMAIL_ADDR + 
					laVehUserData.getEmail());
				System.out.println(
					STR_PHONE_NUMBER + laVehUserData.getPhoneNumber());
			}
			postMessage(asQueueName, laPullCart);
		}
		catch (Exception aeEx)
		{
			//  If we encountered an error while servicing the
			//  message, put it back onto the queue
			postMessage(asQueueName, laPullCart);
			System.out.println(PROCESS_RENEWAL_ERROR);
			try
			{
				Thread.sleep(60000);
			}
			catch (Exception aeEx2)
			{
			}
		}
	}
	/**
	 * Retrieves a Java Object from the specified MQ Series queue.
	 * 
	 * @param asQueue String
	 * @return CompleteRegRenData
	 */
	public Object retrieveMessage(String asBackupQueue)
	{
		Log.write(Log.METHOD, this, RETRIEVE_MSG_ENTRY);
		Object aaObject = null;
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

			// Now specify the queue that we wish to open, 
			// and the open options...
			// last three parameters = null
			// default q manager, dynamic q name, alternate userid 

			system_default_local_queue =
				laQMgr.accessQueue(
					asBackupQueue,
					liOpenOptions,
					null,
					null,
					null);

			// get the message back again...
			// First define a MQ message buffer to receive the message 
			// into..
			MQMessage laRetrievedMessage = new MQMessage();
			// retrievedMessage.messageId = hello_world.messageId;

			// Set the get message options..
			MQGetMessageOptions laGMO = new MQGetMessageOptions();
			// accept the defaults
			// same as MQGMO_DEFAULT

			// get the message off the queue..
			system_default_local_queue.get(laRetrievedMessage, laGMO);
			// max message size

			// And prove we have the message by displaying the UTF 
			// message text
			aaObject = (Object) laRetrievedMessage.readObject();
			System.out.println("The message is: " + 
				aaObject.toString());

		}

		// If an error has occured in the above, try to identify what 
		// went wrong.
		// Was it an MQ error?

		catch (MQException aeMQEx)
		{
			if (aeMQEx.reasonCode != 2033)
			{
				System.out.println(
					MQ_ERROR_MSG
						+ aeMQEx.completionCode
						+ REASON_CODE
						+ aeMQEx.reasonCode);
				aeMQEx.printStackTrace();
				// throw new RTSException(RTSException.MQ_ERROR, ex);
				aaObject = null;
			}
		}

		// Was it a Java buffer space error?
		catch (java.io.IOException aeIOEx)
		{
			// throw new RTSException(RTSException.JAVA_ERROR, ex);
			aaObject = null;
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			// throw new RTSException(RTSException.JAVA_ERROR, ex);
			aaObject = null;
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
			}

			Log.write(Log.METHOD, this, RETRIEVE_MSG_EXIT);
		}
		return aaObject;
	}
}
