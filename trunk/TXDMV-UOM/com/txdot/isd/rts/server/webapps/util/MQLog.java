package com.txdot.isd.rts.server.webapps.util;

/*
 * MQLog.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * MQ Log
 *  
 * @version	5.2.3		05/04/2005
 * @author	Administrator
 * <br>Creation Date:	07/02/2002 12:04:56
 */
public class MQLog {
 
public synchronized static void error(String asMessage) 
{
	P2Log.log(P2Log.ERROR, asMessage,P2Log.MQ);
}
/**
 */
public static synchronized void error(Throwable aaThrow) 
{
	P2Log.log(P2Log.ERROR,aaThrow,P2Log.MQ);	
}
public synchronized static void fine(String asMessage) 
{
	P2Log.log(P2Log.FINE,asMessage,P2Log.MQ);
}
public synchronized static void finer(String asMessage) 
{
	P2Log.log(P2Log.FINER, asMessage,P2Log.MQ);
}
public synchronized static void finest(String asMessage) 
{
	P2Log.log(P2Log.FINEST, asMessage,P2Log.MQ);
}
public synchronized static void info(String asMessage) 
{
	P2Log.log(P2Log.INFO,asMessage,P2Log.MQ);
}
public static void main(String[] args) {
	error("This is a MQ error");
	warning("This is a MQ warning");
	info("This is a MQ info");
	fine("This is a MQ fine");
	finer("This is a MQ finer");
	finest("This is a MQ finest");
}
/**
 * Writes asMessages to the Batch Log file
 * @param asMessage java.lang.String
 */
public synchronized static void warning(String asMessage) 
{
	P2Log.log(P2Log.WARNING,asMessage,P2Log.MQ);
}
public synchronized static void warning(Throwable aaThrow) 
{
	P2Log.log(P2Log.WARNING,aaThrow,P2Log.MQ);
}
}
