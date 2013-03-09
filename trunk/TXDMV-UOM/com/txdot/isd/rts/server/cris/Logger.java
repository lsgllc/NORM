package com.txdot.isd.rts.server.cris;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

//import com.txdot.isd.services.util.*;
//import com.txdot.isd.services.common.data.*;

/*
 * Logger.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * Logger
 *  
 * @version	5.2.3		05/02/2005
 * @author	Jeff Seifert
 * <br>Creation Date:	01/29/2003 13:05:51
 */
public class Logger
{
	private static LinkedList caLogQueue;
	private static final String csLogfilename = "CrisServlet.log";

	/* Logging constants */
	public static final String LOG_TYPE_WRITE_TO_FILE = "LOG TO FILE";
	/**
	 * Logger constructor comment.
	 */

	public Logger()
	{
		super();
	}
	/**
	 * Get Time Stamp
	 * 
	 * @return String
	 */

	public static String getTimeStamp()
	{
		String lsDateTimeStamp = new Date().toString();
		return lsDateTimeStamp;
	}
	/**
	 * Insert the method's description here.
	 *
	 * @param aaHttpServletRequest Object  
	 * @param asMessage java.lang.String
	 */

	public static void log(
		HttpServletRequest aaHttpServletRequest,
		String asMessage)
	{
		LogData laLogData = new LogData();

		laLogData.setLogDate(getTimeStamp());

		laLogData.setUserId(aaHttpServletRequest.getRemoteHost());

		laLogData.setLogMessage(asMessage);
		caLogQueue.addLast(laLogData);
	}
	/**
	 * Log
	 * 
	 * @param aaHttpServletRequest Object  
	 * @param asType java.lang.String
	 * @param asMessage java.lang.String
	 */

	public static void log(
		HttpServletRequest aaHttpServletRequest,
		String asType,
		String asMessage)
	{
		LogData laLogData = new LogData();

		laLogData.setLogDate(getTimeStamp());

		laLogData.setLogType(asType);

		laLogData.setUserId(aaHttpServletRequest.getRemoteHost());

		laLogData.setLogMessage(asMessage);
		caLogQueue.addLast(laLogData);

	}
	/**
	 * Process Log Queue
	 */

	public static void processLogQueue()
	{
		LogData laMessage;
		try
		{
			if (!caLogQueue.isEmpty())
			{
				File laFile = new File(csLogfilename);
				if (!laFile.exists())
				{
					laFile.createNewFile();
				}

				FileOutputStream laFOStream =
					new FileOutputStream(csLogfilename, true);
				PrintWriter laOut = new PrintWriter(laFOStream);

				while (!caLogQueue.isEmpty())
				{
					laMessage = (LogData) caLogQueue.removeFirst();
					laOut.println(
						laMessage.getLogDate()
							+ " - "
							+ laMessage.getUserId()
							+ " "
							+ laMessage.getLogMessage());
					laOut.flush();
				}

				laOut.close();
				laFOStream.close();
			}
		}
		catch (IOException leIOEx)
		{
			// Can't write to log because error writting to log
		}
	}
	/**
	 * Shrink Log File
	 *
	 * @param filename java.lang.String
	 */
	public static synchronized void shrinkLogFile()
	{
		final int MAX_OFFSET = 4096;
		final int LINES_TO_KEEP = 1000;
		int MAX_SIZE = 100 * 1000;

		try
		{
			FileInputStream laFIStream = new FileInputStream(csLogfilename);
			BufferedReader laIn =
				new BufferedReader(new InputStreamReader(laFIStream));
			if (laFIStream.available() > MAX_SIZE - MAX_OFFSET)
			{
				Vector lvLines = new Vector();
				String lsLine = "";
				while ((lsLine = laIn.readLine()) != null)
				{
					lvLines.add(lsLine);
				}
				laFIStream.close();
				FileOutputStream laFOStream =
					new FileOutputStream(csLogfilename, false);
				PrintWriter laOut = new PrintWriter(laFOStream);
				int liSize = lvLines.size();
				for (int i = (liSize - LINES_TO_KEEP); i < liSize; i++)
					laOut.println(lvLines.get(i));
				laOut.flush();
				laOut.close();
			}
		}
		catch (IOException leIOEx)
		{

		}
	}
	/**
	 * Write To Log
	 */

	public static void writeToLog()
	{
		LogData laMessage;
		try
		{
			File laFile = new File(csLogfilename);
			if (!laFile.exists())
			{
				laFile.createNewFile();
			}

			FileOutputStream laFOStream =
				new FileOutputStream(csLogfilename, true);
			PrintWriter laOut = new PrintWriter(laFOStream);

			laMessage = (LogData) caLogQueue.removeFirst();
			laOut.println(
				laMessage.getLogDate()
					+ " - "
					+ laMessage.getUserId()
					+ " "
					+ laMessage.getLogMessage());
			laOut.flush();
			laOut.close();
			laFOStream.close();

		}
		catch (IOException leIOEx)
		{
			leIOEx.printStackTrace();
			// Can't write to log because error writting to log
		}
	}
	/**
	 * Write To Log
	 * 
	 * @param LogData aaMessage
	 */

	public static void writeToLog(LogData aaMessage)
	{
		//LogData message;
		try
		{
			File laFile = new File(csLogfilename);
			if (!laFile.exists())
			{
				laFile.createNewFile();
			}

			FileOutputStream laFOStream =
				new FileOutputStream(csLogfilename, true);
			PrintWriter laOut = new PrintWriter(laFOStream);

			//message = (LogData) caLogQueue.removeFirst();
			laOut.println(
				aaMessage.getLogDate()
					+ " - "
					+ aaMessage.getUserId()
					+ " "
					+ aaMessage.getLogMessage());
			laOut.flush();
			laOut.close();
			laFOStream.close();

		}
		catch (IOException leIOEx)
		{
			leIOEx.printStackTrace();
			// Can't write to log because error writting to log
		}
	}
	/**
	 * Write To Log
	 * 
	 * @param String asString
	 */

	public static void writeToLog(String asString)
	{
		try
		{

			File laFile = new File(csLogfilename);
			if (!laFile.exists())
			{
				laFile.createNewFile();
			}

			FileOutputStream laFOStream =
				new FileOutputStream(csLogfilename, true);
			PrintWriter out = new PrintWriter(laFOStream);

			out.println(asString);
			out.flush();
			out.close();
			laFOStream.close();
		}

		catch (IOException leIOEx)
		{
			leIOEx.printStackTrace();
			// Can't write to log because error writting to log
		}
	}
}
