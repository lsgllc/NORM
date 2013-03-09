package com.txdot.isd.rts.server.webapps.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;

import com.txdot.isd.rts.server.webapps.order.BatchTurnaroundProcessor;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.EmailUtil;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.exception.RTSWebAppsException;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;

/*
 * P2Log.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * B Brown		10/18/2011	Change exception processing to let us know
 * 							why the error file cannot be written to.
 * 							modify write(), getErrorMessage()
 * 							defect 10995 Ver 6.9.0
 *----------------------------------------------------------------------
 */

/**
 * P2 Log. A class to write to the logs
 *  
 * @version	6.9.0		09/29/2011
 * @author	Clifford Chen
 * <br>Creation Date:	11/07/2001 10:27:57
 */
public class P2Log
{
	public static final int MAIN = 1;
	public static final int MQ = 2;
	public static final int BATCH = 3;

	public static final int FINEST = 0;
	public static final int FINER = 1;
	public static final int FINE = 2;
	public static final int INFO = 3;
	public static final int WARNING = 4;
	public static final int ERROR = 5;

	public static final int DEFAULT = INFO;

	public static int level = DEFAULT;
	
	// defect 10995
	private final static String PRODUCTION = "Production: ";
	private final static String PROD_DATASOURCE = "P0RTSDB";
	private final static String TEST = "Test: ";
	public static int ciMessageEmailedCnt = 0;
	private static String csEmailSubject =
		"P2Log write error";
	private static final String EMAIL_FROM = "TSD-RTS-POS@txdmv.gov";
//	private static final String EMAIL_TO   = "TSD-RTS-POS@txdmv.gov";
	private static final String EMAIL_TO   = "bob.l.brown@txdmv.gov";
	private static final String MESSAGE_BODY =
			"An internet process called the P2Log class to log, "
			+ "but there was a problem logging message: <br>";
	private static final String ENCODETYPE = "UTF-8";
	// end defect 10995

	private static String[] carrLevel =
		new String[] {
			"FINEST",
			"FINER",
			"FINE",
			"INFO",
			"WARNING",
			"ERROR" };

	static {
		setLevel(CommunicationProperty.getLogLevel());
	}
	protected static String getErrorFileName(int aiWhichLog)
	{
		String lsFileName = null;
		switch (aiWhichLog)
		{
			case BATCH :
				lsFileName =
					CommunicationProperty.getBatchErrorFileName();
				break;
			case MQ :
				lsFileName = CommunicationProperty.getMQErrorFileName();
				break;
			case MAIN :
				lsFileName = CommunicationProperty.getErrorFileName();
				break;
			default :
				lsFileName = CommunicationProperty.getErrorFileName();
				break;
		}
		return lsFileName;
	}
	private static String getErrorMessage(Throwable aaThrow)
	{
		String lsMessage = "";
		com
			.txdot
			.isd
			.rts
			.services
			.webapps
			.util
			.UtilityMethods
			.getStackTrace(
			aaThrow);
		if (aaThrow instanceof RTSException)
			lsMessage += ((RTSException) aaThrow).getDetailMsg();

		if (aaThrow instanceof RTSWebAppsException)
			lsMessage += ((RTSWebAppsException) aaThrow).getDetailedMessage();
		
		// defect 10996
		if (aaThrow instanceof Exception)
			lsMessage += ((Exception) aaThrow).getMessage() + " Cause = " +
			((Exception) aaThrow).getCause();
		// end defect 10996

		return lsMessage;
	}
	protected static String getFileName(int aiWhichLog)
	{
		String lsFileName = null;
		switch (aiWhichLog)
		{
			case BATCH :
				lsFileName = CommunicationProperty.getBatchLogFileName();
				break;
			case MQ :
				lsFileName = CommunicationProperty.getMQLogFileName();
				break;
			case MAIN :
				lsFileName = CommunicationProperty.getLogFileName();
				break;
			default :
				lsFileName = CommunicationProperty.getLogFileName();
				break;
		}
		return lsFileName;
	}
	public static void log(int aiLevel, String asMessage, int aiWhichLog)
	{
		if (!logRequired(aiLevel))
			return;

		write(aiLevel, asMessage, aiWhichLog);
	}
	public static void log(int aiLevel, Throwable aaThrow, int aiWhichLog)
	{
		if (!logRequired(aiLevel))
			return;

		write(aiLevel, getErrorMessage(aaThrow), aiWhichLog);
	}
	private static boolean logRequired(int aiRequestLevel)
	{

		// boundary protection
		if (aiRequestLevel < FINEST)
			aiRequestLevel = FINEST;

		if (aiRequestLevel > ERROR)
			aiRequestLevel = ERROR;

		if (aiRequestLevel < level)
			return false;
		else
			return true;
	}
	/**
	 * Main
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args)
	{

		log(INFO, "This is main message", MAIN);
		log(ERROR, "This is main error message", MAIN);

		log(INFO, "This is MQ message", MQ);
		log(ERROR, "This is MQ error message", MQ);

		log(INFO, "This is BATCH message", BATCH);
		log(ERROR, "This is BATCH error message", BATCH);

	}
	public static void setLevel(int aiNewLevel)
	{
		level = aiNewLevel;
	}
	public static void setLevel(String asNewLevel)
	{

		if (asNewLevel == null)
			return;

		for (int i = 0; i < carrLevel.length; ++i)
		{
			if (asNewLevel.equalsIgnoreCase(carrLevel[i]))
				level = i;
		}
		// if no match, the level remains as the default.
	}
	/**
	 * Writes messages to the Batch Log file
	 * 
	 * @param message java.lang.String
	 */
	public synchronized static void write(
		int aiLevel,
		String asMessage,
		int aiWhichLog)
	{
		// defect 10995
		String lsFileName = "";
		// end defect 10995
		try
		{
			//String lsFileName = getFileName(aiWhichLog);
			lsFileName = getFileName(aiWhichLog);
			if (aiLevel >= WARNING)
				lsFileName = getErrorFileName(aiWhichLog);
			// defect 10995
			String lsFullFileName = lsFileName + "." + SystemProperty.getLogFileExt();
			if (new File(lsFullFileName).exists())
			{
				UtilityMethods.shrinkLogFile(lsFullFileName);
			}
			else
			{
			// create the 
				URL laURL =
					P2Log
						.class
						.getClassLoader()
						.getResource(
						CommonConstant.STR_SPACE_EMPTY);
				// Decode the URL and create a file object for the new property
				// file.
				File laNewFile =
					new File(
						URLDecoder.decode(
							laURL.getPath(),
							ENCODETYPE).substring(
							1)
							+ lsFullFileName);

				// Create the new file.
				laNewFile.createNewFile();
			}
/*			UtilityMethods.shrinkLogFile(
				lsFileName + "." + SystemProperty.getLogFileExt());*/
			// end defect 10995
			FileOutputStream laFOStream =
				new FileOutputStream(
					lsFileName + "." + SystemProperty.getLogFileExt(),
					true);
			PrintWriter laOut = new PrintWriter(laFOStream);
			RTSDate laDate = RTSDate.getCurrentDate();
			laOut.println(
				laDate + " - " + laDate.getClockTime() + " - " + asMessage);
			laOut.flush();
			laOut.close();
			laFOStream.close();
		}
		catch (IOException leIOEx)
		{
			// defect 10995
			String lsExceptionMsg = MESSAGE_BODY + " = <br>"
					+ asMessage + " to log file: " + lsFileName
					+ " due to IOException " + leIOEx.getMessage();
			MQLog.info(lsExceptionMsg);	
			if (ciMessageEmailedCnt == 0)
			{
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
					EmailUtil laEmailUtil = new EmailUtil();
					laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_TO,
					csEmailSubject,
					lsExceptionMsg);
					ciMessageEmailedCnt = ciMessageEmailedCnt + 1;
				}
				catch (Exception aeEx)
				{
					aeEx.printStackTrace();
					MQLog.info("Error emailing message: " + lsExceptionMsg + 
							   ". email problem = " + aeEx.getMessage());
				}
			}
			// end defect 10995
		}
	}
}
