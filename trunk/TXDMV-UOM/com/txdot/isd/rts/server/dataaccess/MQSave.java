package com.txdot.isd.rts.server.dataaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import com.ibm.mq.*;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * MQSave.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	09/30/2005	Java 1.4 Work; webapps data objects moved
 * 							to services.data
 * 							defect 7889 Ver 5.2.3  
 * B Brown		05/01/2006	Remove the "D" drive specification and  
 * 							add many variables
 * 							modify main()
 * 							defect 8548 Ver 5.2.3
 * Ray Rowehl	05/16/2006	Move to MQSave App Client.
 * 							defect 8782 Ver 5.2.3  
 * B Brown		09/14/2006	Run MQSave as an application Client on the
 * 							new WAS5 server, and read the MQ parms from
 * 							SystemProperty.
 * 							modify main()
 * 							defect 8914 Ver 5.2.5
 * B Brown		05/26/2011	Loop through the class path if 
 * 							SystemProperty access cannot find server.cfg
 * 							defect 10846 Ver 6.8.0
 *----------------------------------------------------------------------
 */

/**
 * The MQSave class is used to post and retrieve messages from MQ Series
 * queues. MQ Series is used as the interface for passing messages (Java
 * Objects) between TxDOT and TexasOnline for the processing of Phase 2
 * transactions.
 *
 * This class is used in 2 ways.  First, the class is used by code 
 * executed by RTSMainServlet on the TxDOT servers for posting messages 
 * to TexasOnline. Second, this class is executed as a separate 
 * application on the Web Application Servers at TxDOT to retrieve 
 * messages sent from TexasOnline and to call the appropriate functions 
 * to process these messages at TxDOT (usually an update to the DB2 tables).
 * 
 * @version	5.8.0		05/26/2011 
 * @author	Richard Hicks
 * <br>Creation Date:	08/12/2002	10:33:49
 */
public class MQSave
{
	/** The port of the MQ Series Server **/
	private int ciMQPort;
	/** The channel of the MQ Series Server **/
	private String csMQChannel;
	/** The Queue Manager name of the MQ Series Server **/
	private String csMQQueueManager;
	/** The csHostname of the MQ Series Server **/
	private String csHostname;
	
	// defect 8548
	private final static String RENEWAL_SER = "renewal.ser";
	private final static String USER_DIR = "user.dir";
	private final static String QUEUE_NAMES =  
	"MQSave host_name channel_name queue_manager queue_name port";
	private final static String GET_USER_DIR = "System.getProperty(user.dir) = ";
	private final static String RENEWAL_MSG = "Message is a Renewal";
	private final static String REFUND_MSG = "Message is a Refund";
	private final static String FORMAT_UNKNOWN_MSG = "**** ERROR : Message format is unknown.  ****";
	private final static String POST_MSG_ENTRY = " - postMessage() - Entry.";
	private final static String POST_MSG_EXIT = " - postMesage() - Exit.";
	private final static String MQ_ERROR_MSG = "An MQ error occurred : Completion code ";
	private final static String REASON_CODE = " Reason code ";
	private final static String MESSAGE_BUFFER_MSG = "An error occurred while writing to the message buffer: ";	
	private final static String PROCESSING_REFUND = "Processing Refund : "; 
	private final static String PROCESSING_RENEWAL = "Processing a Renewal : ";
	private final static String REFUND_STATUS = "Refund Status Code = ";
	private final static String PROCESS_RENEWAL_ERROR = "Error processing renewal : sleeping for 60 seconds";
	private final static String STR_NAME = "Name                 : ";
	private final static String SPACE = " ";
	private final static String STR_EMAIL_ADDR = "e-mail Address		 : ";
	private final static String STR_PHONE_NUMBER = "Phone Number         : ";
	private final static String RETRIEVE_MSG_ENTRY = " - retrieveMesage() - Entry.";
	private final static String RETRIEVE_MSG_EXIT = " - retrieveMesage() - Exit.";

	// end defect 8548
	
	//defect 10846
	private static String csServerCfgLoc = null;
	//end defect 10846
	
	/**
	 * MQSave Constructor.
	 * 
	 * @param asHost String
	 * @param asChannel String
	 * @param asQueue String
	 * @param aiPort int
	 */
	public MQSave(
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
	 * Main method for execution of the MQSave class as an application.
	 * 
	 * @param aarrArgs 
	 */
	public static void main(String[] aarrArgs)
	{

		try
		{
			Comm.setIsServer(true);			
			int liPort = 0 ;
			// defect 10846
//			String lsHostName = SystemProperty.getMQHostName();			
//			String lsChannelName = SystemProperty.getMQChannelName();
//			String lsQueueManager = SystemProperty.getMQQueueManager();
//			String lsQueueName = SystemProperty.getMQQueueName();
//			String lsBackupQueueName = 
//				SystemProperty.getMQBackupQueueName();
			String lsHostName = null;			
			String lsChannelName = null;
			String lsQueueManager = null;
			String lsQueueName = null;
			String lsBackupQueueName = null;
			InputStream laFileServer = null;
			
			lsHostName = SystemProperty.getMQHostName();
			if 	(lsHostName==null)
			{
				System.out.println(
				"Looping through the class path in findServerCfg()");
				laFileServer = findServerCfg();
				Properties laServerPrp = new Properties();
				laServerPrp.load(laFileServer);
				lsHostName = java.net.InetAddress.getLocalHost().getHostName();		
				lsChannelName = laServerPrp.getProperty("MQChannelName");
				lsQueueManager = laServerPrp.getProperty("MQQueueManager");
				lsQueueName = laServerPrp.getProperty("MQQueueName");
				lsBackupQueueName = laServerPrp.getProperty("MQBackupQueueName");
				if (laServerPrp.getProperty("MQPort")!= null)
				{
					liPort = Integer.parseInt(laServerPrp.getProperty("MQPort"));
				}
			}
			else
			{
				lsChannelName = SystemProperty.getMQChannelName();
				lsQueueManager = SystemProperty.getMQQueueManager();
				lsQueueName = SystemProperty.getMQQueueName();
				lsBackupQueueName = 
					SystemProperty.getMQBackupQueueName();
				if (SystemProperty.getMQPort()!= null)
				{
					liPort = Integer.parseInt(SystemProperty.getMQPort());
				}
			}
			// below are the parms in 
			// /usr/WebSphere5/AppServer/Production_SvrGrp/cfg/server.cfg
			// that we care about here
//			MQChannelName=SYSTEM.AUTO.SVRCONN
//			MQQueueManager=RTSII.R500.QM.PAS1
//			MQQueueName=RTSII.R500.INET.REQUEST.QUEUE 
//			MQBackupQueueName=RTSII.R500.BACKUP.QUEUE
//			MQPort=1414
			System.out.println("lsHostName = " + lsHostName);
			System.out.println("lsChannelName = " + lsChannelName);
			System.out.println("lsQueueManager = " + lsQueueManager);
			System.out.println("lsQueueName = " + lsQueueName);
			System.out.println("lsBackupQueueName = " + lsBackupQueueName);	
			System.out.println("liPort = " + liPort);	
			// end defect 10846
			if (lsHostName==null || lsChannelName==null ||
				lsQueueManager==null || lsQueueName==null ||
				lsBackupQueueName==null || liPort==0)
			{
				System.out.println(new java.util.Date() + 
				" An MQ parameter is null or missing from server.cfg"); 					 
			}
			// end defect 8914
			else
			{
				MQSave laReceiver =
					new MQSave(
						lsHostName,
						lsChannelName,
						lsQueueManager,
						liPort);
				Object laMessage = null;
				
				// defect 8548
				try
				{
					System.out.println
						(GET_USER_DIR + 
							System.getProperty(USER_DIR));
					// if the file does not exist, create it.	
					if (!new File(RENEWAL_SER).exists())
					{
						new File(RENEWAL_SER);
					}
				}
				
				catch (Exception aeEx)
				{
					aeEx.printStackTrace();
				}
				// end defect 8548
	
				while (true)
				{
					laReceiver =
						new MQSave(
							lsHostName,
							lsChannelName,
							lsQueueManager,
							liPort);
					laMessage = laReceiver.retrieveMessage(lsQueueName);
					if (laMessage != null)
					{
						if (laMessage instanceof RenewalShoppingCart)
						{
							System.out.println(RENEWAL_MSG);
							try
							{
								// defect 8548
								FileOutputStream laFOS =
									new FileOutputStream(RENEWAL_SER);
								
								// wrote another defect to handle the 
								// reading of the ser file, which will
								// only contain the last record written
								// because we are not currently 
								// appending each shopping cart object
								// to the file			
								// end defect 8548
									
								ObjectOutputStream laOOS =
									new ObjectOutputStream(laFOS);
								laOOS.writeObject(laMessage);
								laOOS.flush();
								laOOS.close();
								laFOS.close();
	
							}
							
							catch (Exception aeEx)
							{
								aeEx.printStackTrace();
							}
	
							laReceiver.processRenewal(
								lsQueueName,
								lsBackupQueueName,
								laMessage);
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
							System.out.println(
								FORMAT_UNKNOWN_MSG);
			
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

			// Now specify the queue that we wish to open, and the open options...

			MQQueue system_default_local_queue =
				laQMgr.accessQueue(
					asQueue,
					liOpenOptions,
					null,
					null,
					null);

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
			System.out.println(
				MESSAGE_BUFFER_MSG
					+ aeIOEx);
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
	 * This method is called by teh MQSave application when a 
	 * RefundObject is retrieved from the MQ Series queue.  If the 
	 * processing of the objects fails, the object is placed back on the
	 * queue for retry at a later time.
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
			System.out.println(
				PROCESSING_REFUND + laRefund.getOrigTraceNo());
			System.out.println(
				REFUND_STATUS + laRefund.getRefundStatus());
			postMessage(asBackupQueueName, laRefund);
		}
		catch (Exception aeEx)
		{
			postMessage(asBackupQueueName, laRefund);
			System.out.println(
				PROCESS_RENEWAL_ERROR);
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
	 * This method is called by teh MQSave application when a 
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
			System.out.println(
				PROCESSING_RENEWAL + laPullCart.getTraceNo());
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
				System.out.println(
					STR_EMAIL_ADDR
						+ laVehUserData.getEmail());
				System.out.println(
					STR_PHONE_NUMBER
						+ laVehUserData.getPhoneNumber());
				VehicleInsuranceData laVehInsData =
					laData.getVehInsuranceData();
				String lsTemp = laVehInsData.getPolicyStartDt();
				lsTemp = lsTemp.replace(' ', '0');
				laVehInsData.setPolicyStartDt(lsTemp);
				lsTemp = laVehInsData.getPolicyEndDt();
				lsTemp = lsTemp.replace(' ', '0');
				laVehInsData.setPolicyEndDt(lsTemp);
			}
			postMessage(asBackupQueueName, laPullCart);
		}
		catch (Exception aeEx)
		{
			//  If we encountered an error while servicing the
			//  message, put it back onto the queue
			postMessage(asBackupQueueName, laPullCart);
			System.out.println(
				PROCESS_RENEWAL_ERROR);
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
	 * Retrieves a Java Objects from the specified MQ Series queue.
	 * 
	 * @param asQueue String
	 * @return CompleteRegRenData
	 */
	public Object retrieveMessage(String asQueue)
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

			// Now specify the queue that we wish to open, and the open options...
			// last three parameters = null
			// default q manager, dynamic q name, alternate userid 

			system_default_local_queue =
				laQMgr.accessQueue(
					asQueue,
					liOpenOptions,
					null,
					null,
					null);

			// get the message back again...
			// First define a MQ message buffer to receive the message into..
			MQMessage laRetrievedMessage = new MQMessage();
			// retrievedMessage.messageId = hello_world.messageId;

			// Set the get message options..
			MQGetMessageOptions laGMO = new MQGetMessageOptions();
			// accept the defaults
			// same as MQGMO_DEFAULT

			// get the message off the queue..
			system_default_local_queue.get(laRetrievedMessage, laGMO);
			// max message size

			// And prove we have the message by displaying the UTF message text
			aaObject = (Object) laRetrievedMessage.readObject();
			// System.out.println("The message is: " + msgText);

		}

		// If an error has occured in the above, try to identify what went wrong.
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
	
	public static InputStream findServerCfg() throws RTSException
	{
		BatchLog.write("start loading classpath");
		InputStream laFileServer = null;
		String lsClassPath = System.getProperty("java.class.path");
		String lsFileSeparator = System.getProperty("file.separator");
		String lsPathSeparator = System.getProperty("path.separator");
		String lsCurrentCPEntry = "";

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
							+ "cfg/server.cfg");
				BatchLog.write(
					"Classpath entry = " + lsCurrentCPEntry);
				if (laTxOServerProp.canRead())
				{
					laFileServer = new FileInputStream(laTxOServerProp);
					csServerCfgLoc = laTxOServerProp.getAbsolutePath();
					BatchLog.write("using file "+ csServerCfgLoc);
					break;
				}
			}
			if (!laTxOServerProp.canRead())
			{
				BatchLog.write("cannot read server.cfg file");
				throw new FileNotFoundException(
						"server.cfg file not found");		
			}
		}
		catch (FileNotFoundException aeFNFEx)
		{
			BatchLog.write("server.cfg file not found in class path");	
		}

		catch (NoSuchElementException aeNSEEx)
		{
			BatchLog.write("MQSave class path read error");		
		}
		return laFileServer;
	}
}
