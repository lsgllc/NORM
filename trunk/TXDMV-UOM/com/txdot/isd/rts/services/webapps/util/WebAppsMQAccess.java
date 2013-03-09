package com.txdot.isd.rts.services.webapps.util;

import java.io.IOException;

import com.ibm.mq.*;
import com.txdot.isd.rts.services.data.CompleteRegRenData;
import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.data.RenewalShoppingCart;
import com.txdot.isd.rts.services.data.SpecialPltOrdrCart;
import com.txdot.isd.rts.services.data.SpecialPltOrdrCartItem;
import com.txdot.isd.rts.services.webapps.exception.RTSWebAppsException;

/*
 *
 * WebAppsMQAccess.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Brown  	08/10/2006  Log trace number, plate number, 
 * 							itrntPymntStatusCd, amount, and 
 * 							pymntorderid when posting 
 * 							RenewalShoppingCart or RefundData objects to
 * 							MQ. Also check the object being 
 * 							posted to make sure it's good.
 *       					modify postMessage()
 *       					defect 8836 Ver 5.2.2 fix 8
 * B. Brown  	06/29/2007  Add a throw to the MQException catch block 
 * 							so MQ records get saved at TxO when they
 * 							can't be sent to the POS RTS appservers.
 * 							defect 9119 Ver Special Plates
 * B. Brown		03/13/2008	Add SpecialPltOrdrCart to objects that
 * 							postMessage will process.
 * 							modify postMessage()
 * 							defect 9599 Ver Tres Amigos Prep. 
 * ---------------------------------------------------------------------
 */
 
/**
 * This is used for TexasOnline only.
 * TxDOT site, use:
 * com.txdot.isd.rts.server.dataaccess.MQAccess. 
 * it is adapted from MQAccess by removing the 
 * dependency on the various RTS II phase 1 classes. 
 *
 * @version Ver Tres Amigos Prep	03/13/2008
 * @author Clifford Chen
 * <br>Creation Date:   			01/21/2002 13:38:32  
 */

public class WebAppsMQAccess {
	private int mqPort;
	private String mqChannel;
	private String mqQueueManager;
	private java.lang.String hostname;


/**
 * WebAppsMQAccess constructor comment.
 */
public WebAppsMQAccess() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (1/21/2002 1:43:48 PM)
 * @param channel java.lang.String
 * @param queue java.lang.String
 * @param port int
 */
public WebAppsMQAccess(String host, String channel, String queueManager, int port) 
{
	hostname = host;
	mqChannel = channel;
	mqQueueManager = queueManager;
	mqPort = port;
}
public void postMessage(String asQueue, Object aaObject) 
		throws RTSWebAppsException, MQException, IOException, Exception 
{
	System.out.println(
		"["
			+ new java.util.Date()
			+ "]"
			+ " WebAppsMQAccess - postMessage() - Entry.");

	// defect 8836
	if (aaObject == null)
	{
		System.out.println(
			"["
				+ new java.util.Date()
				+ "]"
				+ "object is null in WebappsMQAccess.postmessage() ");
	}
	else
	{
		if (aaObject instanceof RenewalShoppingCart)
		{
			RenewalShoppingCart laShoppingCart = 
				(RenewalShoppingCart) aaObject;
			if (laShoppingCart.size() == 0)
			{
				System.out.println(
					"["
						+ new java.util.Date()
						+ "]"
						+ "Shopping cart size is zero in " +			
						  "WebappsMQAccess.postmessage() ");
			}
			else
			{
				CompleteRegRenData laCompleteRegRenData = null;
				try
				{
					for (int i = 0; i < laShoppingCart.size(); i++)
					{
						laCompleteRegRenData = (CompleteRegRenData) 
											laShoppingCart.elementAt(i);

						System.out.println(
							"["
								+ new java.util.Date()
								+ "]"
								+ "Sending a renewal to TxDOT via MQ " +								  
								  "for trace no: "
								+ laShoppingCart.getTraceNo()
								+ " and "
								+ "Plate number: "
								+ laCompleteRegRenData.
									getVehBaseData().getPlateNo()
								+ " and "
								+ "ItrntPymntStatuscd = "
								+ laCompleteRegRenData.
									getItrntPymntStatusCd()
								+ " and "
								+ "Paymnt Amt = "
								+ laCompleteRegRenData.
									getTotalFeesString()
								+ " and "
								+ "PymtOrderId: "
								+ laCompleteRegRenData.
									getPymntOrderId());
					}

				}

				catch (Exception ex)
				{
					System.out.println(
						"["
							+ new java.util.Date()
							+ "]"
							+ "The following exception occurred while "
							+ "logging a RenewalShoppingCart/"
							+ "CompleteRegRenData object in " 
							+ "WebappsMQAccess.postmessage()"
							+ ex.getMessage());

					ex.printStackTrace();
				}
			}
		}

		else if (aaObject instanceof RefundData)
		{
			try
			{
				RefundData laRefundData = (RefundData) aaObject;
				System.out.println(
					"["
						+ new java.util.Date()
						+ "]"
						+ "Sending a refund to TxDOT via MQ for trace no: "
						+ laRefundData.getOrigTraceNo()
						+ " and "
						+ "Plate number: "
						+ laRefundData.getVehBaseData().getPlateNo()
						+ " and "
						+ "Refund Status: "
						+ laRefundData.getRefundStatus()
						+ " and "
						+ "Refund Amount: "
						+ laRefundData.getRefAmt()
						+ " and "
						+ "PymtOrderId: "
						+ laRefundData.getPymtOrderId());
			}
			catch (Exception ex)
			{
				System.out.println(
					"["
						+ new java.util.Date()
						+ "]"
						+ "The following exception occurred while " 
						+ "logging a RefundData object in " 
						+ "WebappsMQAccess.postmessage()"
						+ ex.getMessage());

				ex.printStackTrace();
			}
		}
		// defect 9599

		else if  (aaObject instanceof SpecialPltOrdrCart)
		{
			SpecialPltOrdrCart laSpecialPltOrdrCart = 
				(SpecialPltOrdrCart) aaObject;
			if (laSpecialPltOrdrCart.size() == 0)
			{
				System.out.println(
					"["
						+ new java.util.Date()
						+ "]"
						+ "Special plate shopping cart size is zero in " +			
						  "WebappsMQAccess.postmessage() ");
			}
			else
			{
				SpecialPltOrdrCartItem laSpecialPltOrdrCartItem = null;
				try
				{
					for (int i = 0; i < laSpecialPltOrdrCart.size(); i++)
					{
						laSpecialPltOrdrCartItem = (SpecialPltOrdrCartItem) 
						laSpecialPltOrdrCart.elementAt(i);

						System.out.println(
							"["
								+ new java.util.Date()
								+ "]"
								+ "Sending a Special Plate order " 								+ "to TxDOT via MQ " 							  
								+ "for trace no: "
								+ laSpecialPltOrdrCart.getTraceNo()
								+ " and Plate number: "
								+ laSpecialPltOrdrCartItem.getRegPltNo()
								+ " and ItrntPymntStatuscd = "
								+ laSpecialPltOrdrCart.
									getItrntPymntStatusCd()
								+ " and Paymnt Amt = "
								+ laSpecialPltOrdrCart.
									getFeesTotalString()
								+ " and Plate Name: "
								+ laSpecialPltOrdrCartItem.
									getSpecialPlateSelection().getPlateName()
								+ " and regpltcd "
								+ laSpecialPltOrdrCartItem.
									getSpecialPlateSelection().getRegPltCd() 	
								+ " and PymtOrderId: "
								+ laSpecialPltOrdrCart.
									getPymntOrderID());
					}
				}
				catch (Exception ex)
				{
					System.out.println(
						"["
							+ new java.util.Date()
							+ "]"
							+ "The following exception occurred while " 
							+ "logging a Special Plate object in " 
							+ "WebappsMQAccess.postmessage()"
							+ ex.getMessage());

					ex.printStackTrace();
				}
			}
		}
		// end defect 9599
		else
		{
			String lsClass = aaObject.getClass().toString();
			System.out.println(
				"["
					+ new java.util.Date()
					+ "]"
					+ "The MQ Object is neither a RenewalShoppingCart" +
					  " or RefundData object, or Special Plate object"
					+ "The object is "
					+ lsClass);
		}

	}
	// end defect 8836

	try
	{

		MQEnvironment.hostname = hostname;
		MQEnvironment.channel = mqChannel;
		MQEnvironment.port = mqPort;
		MQQueueManager qMgr = null;

		// Create a connection to the queue manager
		qMgr = new MQQueueManager(mqQueueManager);

		// Set up the options on the queue we wish to open...
		int openOptions = MQC.MQOO_OUTPUT;

		// Now specify the queue that we wish to open, 
		// and the open options...

		MQQueue system_default_local_queue =
			qMgr.accessQueue(asQueue, openOptions, null, null, null);

		// Define a simple MQ message
		MQMessage mqMessage = new MQMessage();
		mqMessage.writeObject(aaObject);

		// specify the message options...
		MQPutMessageOptions pmo = new MQPutMessageOptions();

		// put the message on the queue
		system_default_local_queue.put(mqMessage, pmo);

		// Close the queue
		system_default_local_queue.close();

		// Disconnect from the queue manager
		qMgr.disconnect();

	}

	// If an error has occured in the above, try to identify what 
	// went wrong.
	// Was it an MQ error?

	catch (MQException ex)
	{
		System.out.println(
			"["
				+ new java.util.Date()
				+ "]"
				+ "An MQ error occurred : Completion code "
				+ ex.completionCode
				+ " Reason code "
				+ ex.reasonCode);
		ex.printStackTrace();
		// defect 9119
		throw ex;
		// end defect 9119
	}
	// Was it a Java buffer space error?
	catch (java.io.IOException ex)
	{
		System.out.println(
			"["
				+ new java.util.Date()
				+ "]"
				+ "An error occurred while writing to the " 				+  "message buffer: "
				+ ex);
		// defect 9119
		throw ex;
		// end defect 9119		
	}

	catch (Exception ex)
	{
		ex.printStackTrace();
		// defect 9119
		throw ex;
		// end defect 9119
	}
	finally
	{
		System.out.println(
			"["
				+ new java.util.Date()
				+ "]"
				+ "WebAppsMQAccess - postMessage() - Exit.");
	}

}
/**
 * Insert the method's description here.
 * Creation date: (1/21/2002 1:43:10 PM)
 * @return com.txdot.isd.rts.client.webapps.data.CompleteRegRenData
 * @exception com.txdot.isd.rts.services.exception.RTSException The exception description.
 */
public Object retrieveMessage(String queue) throws RTSWebAppsException
{

    System.out.println("WebAppsMQAccess - retrieveMesage() - Entry.");

    Object object = null;
    MQEnvironment.hostname = hostname;
    MQEnvironment.channel = mqChannel;
    MQEnvironment.port = mqPort;
    MQQueueManager qMgr = null;
	MQQueue system_default_local_queue = null;
    try
        {
        // Create a connection to the queue manager
        qMgr = new MQQueueManager(mqQueueManager);

        // Set up the options on the queue we wish to open...
        // Note. All MQ Options are prefixed with MQC in Java.

        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF;

        // Now specify the queue that we wish to open, and the open options...

            system_default_local_queue =
                qMgr.accessQueue(
	                queue, 
	                openOptions,
		            null, // default q manager
	        		null, // no dynamic q name
				    null  // no alternate user id
				 ); 

        // get the message back again...
        // First define a MQ message buffer to receive the message into..
        MQMessage retrievedMessage = new MQMessage();
        // retrievedMessage.messageId = hello_world.messageId;

        // Set the get message options..
        MQGetMessageOptions gmo = new MQGetMessageOptions(); // accept the defaults
        // same as MQGMO_DEFAULT

        // get the message off the queue..
        system_default_local_queue.get(retrievedMessage, gmo);
        // max message size

        // And prove we have the message by displaying the UTF message text
        object = retrievedMessage.readObject();
        // System.out.println("The message is: " + msgText);


    }

    // If an error has occured in the above, try to identify what went wrong.
    // Was it an MQ error?

    catch (MQException ex)
        {
        if (ex.reasonCode != 2033)
        {
        	System.out.println(
         	   "An MQ error occurred : Completion code "
          	      + ex.completionCode
           	     + " Reason code "
            	    + ex.reasonCode);
        				ex.printStackTrace();
	        throw new RTSWebAppsException(RTSWebAppsException.MQ_ERROR, ex);
        }
    }
    
        // Was it a Java buffer space error?
    catch (java.io.IOException ex)
        {
        throw new RTSWebAppsException(RTSWebAppsException.JAVA_ERROR, ex);
    } 
    catch (ClassNotFoundException ex)
        {
        throw new RTSWebAppsException(RTSWebAppsException.JAVA_ERROR, ex);
    }
    finally
        {
	    try 
	    { 
      		  // Close the queue
        	if (system_default_local_queue != null)
	        	system_default_local_queue.close();
	    } catch (MQException ex) {
	    }
	    try
	    {
        	// Disconnect from the queue manager
        	if (qMgr != null)
	        	qMgr.disconnect();
	    } catch (MQException ex)
	    {
	    }
	        
    	System.out.println("WebAppsMQAccess - retrieveMesage() - Exit.");

    }

    return object;
}
}
