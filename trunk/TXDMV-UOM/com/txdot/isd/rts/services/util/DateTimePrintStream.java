package com.txdot.isd.rts.services.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

/*
 * DateTimePrintStream.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * Jeff S.		09/09/2005	New Class.
 * 							defect 8014 Ver 5.2.3
 * Jeff S.		09/14/2005	Moved to RTS so that it can be also used for
 * 							redirect of the standard error and out for
 * 							RTS.
 * 							defect 8372 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * A custom made PrintStream which overrides method println(String).
 * 
 * Thus, when the out stream is set as this PrintStream, all calls to 
 * println(String) will result in an output stream of Date - Time - MSG.
 * 
 * When this class changes make sure to update the one that is part of
 * the refresh project.  Try not to reference any other classes since
 * this class is used somewhere else outside this project.
 *
 * @version	5.2.3		09/14/2005
 * @author	Jeff Seifert
 * <p>Creation Date: 	09/09/2005 08:06:15 
 */

public class DateTimePrintStream extends PrintStream
{
	// Did not use common constants because this class is used in the
	// Refresh project and did not want to bring CommonConstants along
	// as well.
	private static final String DASH = " - ";
	private static final String EMPTY_STRING = "";
	private static final String FOR_SLASH = "/";
	private static final String PERIOD = ".";
	private static final String SEMI = ":";
	private static final String ZERO = "0";
	/**
	* The constructor of the class.
	* 
	* @param a standard output stream (needed by super method)
	*/
	public DateTimePrintStream(OutputStream out)
	{
		super(out);
	}
	/**
	 * Returns the date and clock time (e.g. 07/16/2004 15:35.34) for
	 * writing a timestamp in the log.
	 * 
	 * @return String
	 */
	private static String getDateClockTime()
	{
		Calendar laRightNow = Calendar.getInstance();
		int liHour = laRightNow.get(Calendar.HOUR_OF_DAY);
		int liMinute = laRightNow.get(Calendar.MINUTE);
		int liSecond = laRightNow.get(Calendar.SECOND);
		int liMillisec = laRightNow.get(Calendar.MILLISECOND);
		String lsTime = EMPTY_STRING;

		if (liHour < 10)
		{
			lsTime += ZERO + liHour;
		}
		else
		{
			lsTime += liHour;
		}
		lsTime += SEMI;

		if (liMinute < 10)
		{
			lsTime += ZERO + liMinute;
		}
		else
		{
			lsTime += liMinute;
		}
		lsTime += SEMI;

		if (liSecond < 10)
		{
			lsTime += ZERO + liSecond;
		}
		else
		{
			lsTime += liSecond;
		}
		if (liMillisec < 10)
		{
			lsTime = lsTime + PERIOD + ZERO + ZERO + liMillisec;
		}
		else
		{
			if (liMillisec < 100)
			{
				lsTime = lsTime + PERIOD + ZERO + liMillisec;
			}
			else
			{
				lsTime = lsTime + PERIOD + liMillisec;
			}
		}

		String lsMonth =
			(laRightNow.get(Calendar.MONTH) + 1) + EMPTY_STRING;
		String lsDay = laRightNow.get(Calendar.DATE) + EMPTY_STRING;
		String lsYear = laRightNow.get(Calendar.YEAR) + EMPTY_STRING;

		if (lsMonth.length() < 2)
		{
			lsMonth = ZERO + lsMonth;
		}

		if (lsDay.length() < 2)
		{
			lsDay = ZERO + lsDay;
		}

		String lsDateClockTime =
			(lsMonth
				+ FOR_SLASH
				+ lsDay
				+ FOR_SLASH
				+ lsYear
				+ DASH
				+ lsTime
				+ DASH);

		return lsDateClockTime;
	}
	/**
	* Method println - Overides the PrintStream.println()
	* Done so that the date and time could be added to each new line.
	* 
	* @param string
	*/
	public void println(String string)
	{
		super.println(getDateClockTime() + string);
	}
}