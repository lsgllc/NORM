package com.txdot.isd.rts.services.util;

import java.io.*;

/*
 *
 * BatchLog.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * A class to write to the BatchLog
 * 
 * @version	5.2.3			06/21/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		11/07/2001 10:27:57
 */
public class BatchLog
{

	/**
	 * Writes errors to the Batch Error file
	 * 
	 * @param asMessage - String
	 */
	public static synchronized void error(String asMessage)
	{
		try
		{
			UtilityMethods.shrinkLogFile(
				SystemProperty.getBatchErrorFileName()
					+ "."
					+ SystemProperty.getLogFileExt());
			FileOutputStream laFile =
				new FileOutputStream(
					SystemProperty.getBatchErrorFileName()
						+ "."
						+ SystemProperty.getLogFileExt(),
					true);
			PrintWriter laPWOut = new PrintWriter(laFile);
			RTSDate laDate = RTSDate.getCurrentDate();
			laPWOut.println(
				laDate + " - " + laDate.getClockTime() + " - " + asMessage);
			laPWOut.flush();
			laPWOut.close();
			laFile.close();
		}
		catch (IOException aeIOEx)
		{
			System.out.println("Got an IOException writing to the " +				"batch log - Error");
		}
	}
	/**
	 * Writes messages to the Batch Log file
	 * 
	 * @param asMessage - String
	 */
	public synchronized static void write(String asMessage)
	{
		try
		{
			UtilityMethods.shrinkLogFile(
				SystemProperty.getBatchLogFileName()
					+ "."
					+ SystemProperty.getLogFileExt());
			FileOutputStream laFile =
				new FileOutputStream(
					SystemProperty.getBatchLogFileName()
						+ "."
						+ SystemProperty.getLogFileExt(),
					true);
			PrintWriter laPWOut = new PrintWriter(laFile);
			RTSDate laDate = RTSDate.getCurrentDate();
			laPWOut.println(
				laDate + " - " + laDate.getClockTime() + " - " + asMessage);
			laPWOut.flush();
			laPWOut.close();
			laFile.close();
		}
		catch (IOException aeIOEx)
		{
			System.out.println("Got an IOException writing to the " +				"batch log - write");
		}
	}
}
