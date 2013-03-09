package com.txdot.isd.rts.server.dataaccess;

import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import com.ibm.ctg.client.JavaGateway;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * MFConnectionPool.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/18/2003 	add timestamp in front of stack trace
 * 							defect 5263
 * Ray Rowehl	04/14/2005	Deprecate the class.  It is never used.
 * 							defect 7937 Ver 5.2.3
 * ---------------------------------------------------------------------
 */


/**
 * Manager of connections maintained with the TxDOT mainframe. Provides 
 * methods that let clients get/return a connection to the mainframe. 
 * 
 * MFConnectionPool maintains a set of connections to the TxDOT MainFrame. 
 * 
 * @version 5.2.3		04/14/2005
 * @author	Marx Rajangam
 * <br>Creation Date:	08/24/2001 14:09:28
 * @deprecated
 */
public class MFConnectionPool
{
	
	/**
	 * Contains the list of connections to the CICS transaction gateway
	 */
	private static Vector aConnections = new Vector();

	/**
	 * Specifies the number of connections to create suring the initialization
	 * of the connection pool 
	 */
	private static int ciInitialPoolSize;

	/**
	* Specifies the maximum size the connection pool is allowed to grow
	* during peak usage periods
	*/ 
	private static int ciMaximumPoolSize;
	
/**
* Specifies the number of connections that are maintained initially in the
* connection pool for communicating with the TxDOT mainframe.
*/	
private final static int iINITIAL_POOL_SIZE = 10;

/** 
* Specifies the maximum number of connections that can be maintained in the
* connection pool for communicating with the TxDOT mainframe.
*/ 	
private final static int iMAXIMUM_POOL_SIZE = 20;


	/*
	* Specifies the number of connections that are available at a given time. 
	*/
	private static int ciCurrentPoolSize = 0;
/**
 * Default constructor. Sets the Initial Pool Size and Maximum Pool Size of
 * the connection pool.
 */
public MFConnectionPool()
{
	super();
	
	ciInitialPoolSize = iINITIAL_POOL_SIZE;
	ciMaximumPoolSize = iMAXIMUM_POOL_SIZE; 
}
/**
 * This method destroys all the connections with the mainframe and frees up
 * all the resources
 * 
 * Creation date: (8/24/01 4:59:13 PM)
 * Author: Marx Rajangam
 */
public void destroyConnectionPool()
{
}
/**
 * Gets a connection to the TxDOT mainframe from the connection pool, 
 * if a connection is available
 *
 * <p> getConnectionFromPool tries to get a connection from the pool, if a 
 * connection object is available. Picks up the first connection object and
 * decrements the currentPoolSize.
 * 
 * NOTE: will need a connections_in_use value to know whether we have reached
 * the max # of connections. for example, 
 * <code>if ( (conn_in_use + curr_pool_size) > max_pool_size) </code> 
 *
 * knowing this would help us to create connection objects as an when needed and 
 * add them to the pool in <code>getConnectionFromPool</code> method.
 *
 * NOTE: This method is incomplete now. 
 * 
 * Creation date: (8/24/01 5:00:58 PM)
 * Author: Marx Rajangam
 * 
 * @return com.ibm.ctg.client.JavaGateway
 */
public JavaGateway getConnectionFromPool()
{
	JavaGateway laJGateway = new JavaGateway();

	synchronized (this)
	{
		// get a connection, delete it from the pool and decrement 
		// the pool size
		boolean ljgate_available = ciCurrentPoolSize > 0; 
		if (ljgate_available) 
		{
			laJGateway = (JavaGateway) aConnections.firstElement();
			aConnections.removeElementAt(0);
			ciCurrentPoolSize--; 
		}

		// if the connection is not open, try to get a new conn.
		// recursively.
		try
		{
			if (!laJGateway.isOpen())
					laJGateway = getConnectionFromPool();  
		}
		// NOTE: The caught exception should be changed! 
		catch (Exception e)
		{
			System.err.println("MFConnectionPool Error " +
				 			" " + (new RTSDate()).getYYYYMMDDDate() +
				 			" " + (new RTSDate()).get24HrTime());
			e.printStackTrace(); 
		}
	}

	// returning a valid connection to MF
   	return laJGateway;

}
/**
 * Creates the connection pool with number of connections specified
 * by ciInitialPoolSize and updates cCurrentPoolSize
 * 
 * Creation date: (8/24/01 4:56:05 PM)
 * Author: Marx Rajangam
 */
public void initializeConnectionPool() 
{
	/*
	* create initial number of connection objects and add them
	* to the Vector caConnections
	*/

	Properties lMFConnectionProperties = new Properties(); //Java Gateway properties
	String lGatewayURL; //Java Gateway URL
	int lGatewayPort; //Java Gateway port
	
	try
	{

		//get the Java Gate URL and Java gate port
		String lsGateway = SystemProperty.getMFJGateway();
		int liGatewayPort = SystemProperty.getMFJGatewayPort(); 
		
		for (int i=0; i<ciInitialPoolSize; i++)
		{

			//Create a java gate and add it to the connection pool
  	     	JavaGateway gate = new JavaGateway(lsGateway, liGatewayPort);
   		   	aConnections.addElement(gate);
		}
		//set the current size of the pool
		ciCurrentPoolSize = ciInitialPoolSize; 
	}
	catch (IOException e)
	{
		System.err.println("MFConnectionPool Error " +
				 			" " + (new RTSDate()).getYYYYMMDDDate() +
				 			" " + (new RTSDate()).get24HrTime());
		e.printStackTrace(); 
	} 	
	
}
/**
 * Main Entry to the program.
 * <b> Note: </b> This main method exists for testing purposes. It
 * should be deleted when packaging for production
 * 
 * Creation date: (8/27/01 11:29:04 AM)
 * Author: Marx Rajangam
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	System.out.println("Creating MFConnection Pool"); 
	MFConnectionPool lMFPool = new MFConnectionPool();
	System.out.println("Creation Successful. Initializing the Connection Pool");
	lMFPool.initializeConnectionPool();
	System.out.println("Initialization successful");
	System.out.println("Current Pool Size is : " + ciCurrentPoolSize); 
}
/**
 * Returns a TxDOT mainframe connection to the connection pool.
 * 
 * Creation date: (8/24/01 5:04:24 PM)
 * Author: Marx Rajangam
 * 
 * @param connection com.ibm.ctg.client.JavaGateway
 */
public void returnConnectionToPool(JavaGateway param)
{
}
}
