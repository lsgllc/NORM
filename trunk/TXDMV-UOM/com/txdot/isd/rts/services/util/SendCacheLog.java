package com.txdot.isd.rts.services.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/*
 *
 * SendCacheLog.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/03/2005	Java 1.4 Work
 *							Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * A class to write to the SendCacheLog
 *
 * @version	5.2.3			11/03/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		02/11/2002 08:52:55 
 */

public class SendCacheLog
{
	/**
	 * Writes messages to the SendCache Log file
	 * 
	 * @param message String
	 */
	public static synchronized void write(String message)
	{
		try
		{
			//UtilityMethods.shrinkLogFile(SystemProperty.getSendCacheLogFileName() + "." + SystemProperty.getLogFileExt());
			FileOutputStream laFileOut =
				new FileOutputStream(
					SystemProperty.getSendCacheLogFileName()
						+ "."
						+ SystemProperty.getLogFileExt(),
					true);
			PrintWriter laPrintWriter = new PrintWriter(laFileOut);
			RTSDate laCurrentDate = RTSDate.getCurrentDate();
			laPrintWriter.println(
				laCurrentDate
					+ " - "
					+ laCurrentDate.getClockTime()
					+ " - "
					+ message);
			laPrintWriter.flush();
			laPrintWriter.close();
			laFileOut.close();
		}
		catch (IOException aeIOEx)
		{

		}
	}
}
