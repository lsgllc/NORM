package com.txdot.isd.rts.server.webapps.util;

/*
 * BatchLog.java
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
 * Batch Log
 *  
 * @version	5.2.3		05/04/2005
 * @author	Administrator
 * <br>Creation Date:	07/02/2002 12:04:56
 */
public class BatchLog
{
	public synchronized static void error(String asMessage)
	{
		P2Log.log(P2Log.ERROR, asMessage, P2Log.BATCH);
	}
	public static synchronized void error(Throwable aaThrow)
	{
		P2Log.log(P2Log.ERROR, aaThrow, P2Log.BATCH);
	}
	public synchronized static void fine(String asMessage)
	{
		P2Log.log(P2Log.FINE, asMessage, P2Log.BATCH);
	}
	public synchronized static void finer(String asMessage)
	{
		P2Log.log(P2Log.FINER, asMessage, P2Log.BATCH);
	}
	public synchronized static void finest(String asMessage)
	{
		P2Log.log(P2Log.FINEST, asMessage, P2Log.BATCH);
	}
	public synchronized static void info(String asMessage)
	{
		P2Log.log(P2Log.INFO, asMessage, P2Log.BATCH);
	}
	/**
	 * Main
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args)
	{
		error("This is a batch error");
		warning("This is a batch warning");
		info("This is a batch info");
		fine("This is a batch fine");
		finer("This is a batch finer");
		finest("This is a batch finest");
	}

	public synchronized static void warning(String asMessage)
	{
		P2Log.log(P2Log.WARNING, asMessage, P2Log.BATCH);
	}
	public synchronized static void warning(Throwable aaThrow)
	{
		P2Log.log(P2Log.WARNING, aaThrow, P2Log.BATCH);
	}
}
