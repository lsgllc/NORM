package com.txdot.isd.rts.services.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.txdot.isd.rts.services.cache.HolidayCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * RTSDate.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------
 * N Ting		09/07/2001	Makes class serializable
 * MAbs			04/15/2002	Correcting daylight savings bug in OS/2 
 * 							CQU100003429
 * Btulsiani	04/17/2002	Removed daylight savings fix
 * MAbs			04/17/2002	OS/2 bug for Daylight Savings CQU100003429
 * K Harrell	10/15/2003	Defect 6650: Correct Millisecond padding for
 * 							getClockTime
 *							modify getClockTime()
 *							defect 6650 Ver 5.1.5.1
 * Jeff S.		07/09/2004	Created method that can be used on
 *							client side that will subtract an hour
 *							from the current time if tz=M.
 *							add getClientTimeZoneAdjustedDate()
 *							defect 7325 Ver 5.1.6 Fix 2
 * Jeff S.		02/10/2005	Removed all references to other classes so
 * 							that RTSDate could be brought back and forth
 * 							from TXO to POS without the baggage.
 * 							add MOUNTAIN_TIME_ZONE
 *							add addPadding()
 *							modify getTimeZoneAdjustedDate, 
 *								getCurrentDate, 
 *								getClientTimeZoneAdjustedDate
 *							removed import
 *							defect 7712 Ver 5.2.3 Fix 2
 * J Zwiener	07/28/2005	Enhancement for Disable Placard event
 * 							add getMMYYYY()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	03/08/2007	add isBusinessDay()
 * 							defect 9085 Ver Special Plates 
 * Ray Rowehl	03/13/2007	Enhance constructor for longs to use 
 * Kathy Harrell			more correct timezone setup.
 * 							modify RTSDate(long date)
 * 							defect 9138
 * Ray Rowehl	03/14/2007	Change to use TimeZone.
 * 							Mark code to review with Todos.
 * 							Do some code cleanup.
 * 							delete os2Offset()
 * 							modify RTSDate(long date)
 * 							defect 9138 Ver Special Plates
 * Ray Rowehl	05/22/2007	Do not print stacktrace when handling wrong
 * 							time zone.
 * 							modify RTSDate(Timestamp)
 * 							defect 9138 Ver Special Plates
 * K Harrell	08/14/2008	add getMMDDYYYY() 
 * 							defect 9799 Ver MyPlates_POS
 * K Harrell	08/26/2008	add getMMDDYY()
 * 							defect 8940 Ver Defect_POS_B
 * K Harrell	02/26/2009	add getClockTimeNoMs()
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	06/03/2009	modify getClockTime()
 * 							defect 9943 Ver Defect_POS_F
 * K Harrell	03/30/2010	add isValidDate()
 * 							defect 8087 Ver POS_640
 * K Harrell	06/30/2010	add getMMDDYYYY(String), getTimeSS() 
 * 							modify getMMDDYYYY() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/10/2010	add DAYSOFWEEK
 * 							add getDayOfWeek()
 * 							defect 10545 Ver 6.5.0 
 * K Harrell	07/12/2010	add HOUR, MINUTE, SECOND
 * 							defect 10545 Ver 6.5.0 
 * K Harrell	10/22/2010	add showMsgForRecentCCO()
 * 							defect 10639 Ver 6.6.0 
 * Ray Rowehl	01/10/2011	Add getter and setter for handling Calendar
 * 							for web services.
 * 							add getCalendar(), setCalendar()
 * 							defect 10673 Ver 6.7.0
 * K Harrell	05/28/2011	add MONTHNAME
 * 							add getMonthName(), getMonthName(int) 
 * 							defect 10831 Ver 6.8.0
 * K Harrell	10/04/2011	modify isBusinessDay()
 * 							defect 9919 Ver 6.9.0 
 * B Woodson    11/02/2011  added getMFFormattedDate
 *                          defect 11052 - VTR-275 Form Project @ POS
 * B Woodson    11/17/2011  added getLongDate()
 *                          defect 11052 - VTR-275 Form Project @ POS
 * B Woodson    03/09/2012  added getMFLongDate()  
 *                          defect 11302 - 275 does not match between pos and mf
 * K Harrell	06/04/2012	correct typo (<31 vs. <=31) 
 * 							modify isValidDate()
 * 							defect 11370 Ver 7.0.0                        
 * ---------------------------------------------------------------------
 */

/**
 * RTSDate handles all the functions needed in RTSII for dealing with 
 * dates.
 * <p>The RTSII deals with dates in many formats
 * <ul><li>an int in the form yyyymmdd, for example 19990506 would 
 * 			equal 05/06/1999
 *        <li>an int called AMDate that represents the number of days 
 * 				since Jan 1, 1900
 *        <li>a java.sql.Date
 *        <li>a java.sql.Timestamp
 * </ul>
 * <p>The RTSDate provides constructors that take each of these examples
 * and allows for a common class to deal with dates.  The goal is to 
 * hide as much of the differing date formats from the programmer.
 * <p><b>NOTE:</b> The RTSDate does not follow java.util.Date in perfect
 * fashion.  RTSDate uses the integers 1-12 for its months, to more 
 * closely match most of the RTS II date needs.  This differs from 
 * java.util.Date, which uses the integers 0-11 for its months.
 *
 *   // Internal notes: FROM --> java.util.Calendar.java
 *   // Calendar contains two kinds of time representations: current 
 *   // "time" in
 *   // milliseconds, and a set of time "fields" representing the 
 *   // current time.
 *   // The two representations are usually in sync, but can get out 
 *   // of sync as follows.
 *   // 1. Initially, no fields are set, and the time is invalid.
 *   // 2. If the time is set, all fields are computed and in sync.
 *   // 3. If a single field is set, the time is invalid.
 *   // Recomputation of the time and fields happens when the object 
 *   // needs to return a result to the user, or use a result for a 
 *   // computation.
 *  So dummy call for getYear() is made in constructors where any 
 *  field is set.
 * 
 *
 * @version 7.0.0	06/04/2012
 * @author 	Michael Abernethy
 * @since			08/28/2001 16:14:19
 */

public class RTSDate implements Comparable, java.io.Serializable
{
	// defect 10831 
	private final static String[] MONTHNAME =
		{
			"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December" };
	// end defect 10831 

	// defect 10545 
	private final static String[] DAYSOFWEEK =
		new String[] {
			"Sunday",
			"Monday",
			"Tuesday",
			"Wednesday",
			"Thusday",
			"Friday",
			"Saturday" };

	public final static int HOUR = java.util.Calendar.HOUR;
	public final static int MINUTE = java.util.Calendar.MINUTE;
	public final static int SECOND = java.util.Calendar.SECOND;
	// end defect 10545 

	public final static int DATE = java.util.Calendar.DATE;
	public final static int MONTH = java.util.Calendar.MONTH;

	/**
	 * To create an RTSDate using an integer in the form yyyymmdd as 
	 * an argument.
	 */
	public final static int YYYYMMDD = 1;

	/**
	 * To create an RTSDate using an AMDate as an argument.
	 */
	public final static int AMDATE = 2;

	// defect 7712
	// Added so that we don't have to refer to another class for this
	// constant.
	private static final String MOUNTAIN_TIME_ZONE = "M";
	// end defect 7712

	private final static long serialVersionUID = 8196850732584118783L;

	private java.util.GregorianCalendar caCalendar;

	private int liMicros;

	/**
	 * Constructs a default RTSDate using the current time.
	 */
	public RTSDate()
	{
		this(
			Calendar.getInstance().get(Calendar.YEAR),
			Calendar.getInstance().get(Calendar.MONTH) + 1,
			Calendar.getInstance().get(Calendar.DATE),
			Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
			Calendar.getInstance().get(Calendar.MINUTE),
			Calendar.getInstance().get(Calendar.SECOND),
			Calendar.getInstance().get(Calendar.MILLISECOND));
	}

	/**
	 * Constructs an RTSDate using the date information provided by date.
	 * <p>The type can be either a AMDATE or YYYYMMDD.
	 * @param type int the type of date that is being passed
	 * @param date int the date
	 */
	public RTSDate(int type, int date)
	{
		if (type == YYYYMMDD)
		{
			if (date == 0)
			{
				caCalendar = null;
			}
			else
			{
				String num = Integer.toString(date);
				int year = Integer.parseInt(num.substring(0, 4));
				int month = Integer.parseInt(num.substring(4, 6)) - 1;
				int d = Integer.parseInt(num.substring(6, 8));
				caCalendar =
					new GregorianCalendar(year, month, d, 0, 0, 0);
				caCalendar.set(java.util.Calendar.MILLISECOND, 0);
			}
		}
		else if (type == AMDATE)
		{
			Calendar laCalendar = new GregorianCalendar(1900, 0, 1);
			laCalendar.add(java.util.Calendar.DATE, date);
			caCalendar =
				new GregorianCalendar(
					laCalendar.get(java.util.Calendar.YEAR),
					laCalendar.get(java.util.Calendar.MONTH),
					laCalendar.get(java.util.Calendar.DATE));
			caCalendar.set(java.util.Calendar.MILLISECOND, 0);
		}
		//Dummy call so that all fields are in sync.
		getYear();
	}

	/**
	 * Creates an RTSDate with the givin parameters.
	 * @param year int the year
	 * @param month int the month
	 * @param date int the date
	 */
	public RTSDate(int year, int month, int date)
	{
		this(year, month, date, 0, 0, 0, 0);
	}

	/**
	 * Creates an RTSDate with the given parameters.
	 * 
	 * @param year int the year
	 * @param month int the month
	 * @param date int the date
	 * @param hour int the hour
	 * @param min int the minute
	 * @param sec int the second
	 * @param millisec int the millisecond
	 */
	public RTSDate(
		int year,
		int month,
		int date,
		int hour,
		int min,
		int sec,
		int millisec)
	{
		caCalendar =
			new GregorianCalendar(
				year,
				month - 1,
				date,
				hour,
				min,
				sec);
		caCalendar.set(Calendar.MILLISECOND, millisec);
		//Dummy call so that all fields are in sync.
		getYear();
	}

	/**
	 * Creates an RTSDate with the given parameters.
	 * 
	 * @param alDate long the number of milliseconds that have passed 
	 * 	since January 1, 1970 00:00:00.000 GMT. 
	 */
	public RTSDate(long alDate)
	{
		// defect 9139
		//SimpleTimeZone cst =
		//	new SimpleTimeZone(-6 * 60 * 60 * 1000, "America/Chicago");
		TimeZone laRTSTZ = TimeZone.getTimeZone("America/Chicago");

		if (!Comm.isServer())
		{
			// use the client's java time zone.
			laRTSTZ =
				TimeZone.getTimeZone(
					System.getProperty("user.timezone"));
		}

		//		// set up rules for daylight savings time
		//		cst.setStartRule(
		//			Calendar.MARCH,
		//			2,
		//			Calendar.SUNDAY,
		//			2 * 60 * 60 * 1000);
		//		cst.setEndRule(
		//			Calendar.NOVEMBER,
		//			1,
		//			Calendar.SUNDAY,
		//			2 * 60 * 60 * 1000);
		caCalendar = new GregorianCalendar(laRTSTZ);
		//System.out.println("New Date " + caCalendar.toString());
		caCalendar.setTime(new java.util.Date(alDate));

		// this is only here for debug purposes
		// System.out.println(
		// "===========================================");
		// System.out.println("RTSDate(long)");
		// System.out.println("Date is " + new java.util.Date(date));
		// System.out.println("TimeZone " + laRTSTZ);
		// calendar.setTime(new java.util.Date(date));
		// System.out.println("New Date " + calendar.toString());
		// System.out.println("");
		// end defect 9139
	}

	/**
	 * Creates an RTSDate given the DB2 date
	 * @param db2Date java.lang.String
	 */
	public RTSDate(String db2Date)
	{
		int year = Integer.parseInt(db2Date.substring(0, 4));
		int month = Integer.parseInt(db2Date.substring(5, 7));
		int date = Integer.parseInt(db2Date.substring(8, 10));
		int hour = Integer.parseInt(db2Date.substring(11, 13));
		int min = Integer.parseInt(db2Date.substring(14, 16));
		int sec = Integer.parseInt(db2Date.substring(17, 19));
		int millisec = Integer.parseInt(db2Date.substring(20, 23));
		int micros =
			Integer.parseInt(db2Date.substring(24, db2Date.length()));
		caCalendar =
			new GregorianCalendar(
				year,
				month - 1,
				date,
				hour,
				min,
				sec);
		caCalendar.set(Calendar.MILLISECOND, millisec);
		this.liMicros = micros;
		//Dummy call so that all fields are in sync.
		getYear();

	}

	/**
	 * Constructs an RTSDate using the date information provided by 
	 * the java.sql.Date.
	 * @param date java.sql.Date the date
	 */
	public RTSDate(java.sql.Date date)
	{
		this(date.getTime());
	}
	/**
	 * Creates an RTSDate with the givin parameters.
	 * @param date java.sql.Timestamp the date
	 */
	public RTSDate(Timestamp date)
	{
		this(date.getTime());

		// defect 9139
		// print some information if the time zone is not 6
		if (date.getTimezoneOffset() / 60 != 6)
		{
			try
			{
				throw new Exception();
			}
			catch (Exception aeEx)
			{
				//System.out.println("=================================");
				//System.out.println("RTSDate TimeStamp");
				//System.out.println(
				//	"TimeStamp "
				//		+ date.toString()
				//		+ " LONG "
				//		+ date.getTime()
				//		+ " GMT "
				//		+ date.toGMTString()
				//		+ " TZ "
				//		+ date.getTimezoneOffset() / 60);
				// defect 9139
				// do not attempt to print this.
				// save the log space
				//aeEx.printStackTrace();
				// end defect 9139
				//System.out.println(" ");
			}
		}
		// end defect 9139

		int nanoseconds = date.getNanos();
		setMillisecond(nanoseconds / 1000000);
		setMicroseconds((nanoseconds / 1000) % 1000);
		//Dummy  call so that all fields are in sync.
		getYear();
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of 
	 * time to the given time field, based on the calendar's rules. 
	 * For example, to subtract 5 days from the current time of the 
	 * calendar, you can achieve it by calling: 
	 * <ul>add(RTSDate.DATE, -5).</ul>
	 * <p>In addition, add() follows the following rules:
	 * <p>Add rule 1. The value of field <code>identifier</code> after 
	 * 		the call minus the value of field <code>identifier</code> 
	 * 		before the call is <code>amount</code>, modulo any overflow 
	 * 		that has occurred in field <code>identifier</code>. 
	 *      Overflow occurs when a field value exceeds its range and, 
	 * 		as a result, the next larger field is incremented or 
	 * 		decremented and the field value is adjusted back into its 
	 * 		range.
	 *
	 * <p>Add rule 2. If a smaller field is expected to be invariant, 
	 * 		but it is impossible for it to be equal to its prior value 
	 * 		because of changes in its minimum or maximum after field 
	 * 		<code>identifier</code> is changed, then its value is 
	 * 		adjusted to be as close as possible to its expected value. 
	 * 		A smaller field represents a smaller unit of time. DATE  is 
	 * 		a smaller field than MONTH. No adjustment is made to smaller 
	 *      fields that are not expected to be invariant. The calendar 
	 * 		system determines what fields are expected to be invariant.
	 * <p>Example: Consider a RTSDate originally set to August 31, 1999.
	 * 		Calling add(RTSDate.MONTH, 13) sets the RTSDate to 
	 * 		September 30, 2000. Add rule 1 sets the MONTH field to 
	 * 		September, since adding 13 months to August gives September 
	 * 		of the next year. Since DAY_OF_MONTH cannot be 31 in 
	 * 		September in a RTSDate, add rule 2 sets the DATE to 30, the 
	 * 		closest possible value. Although it is a smaller field, 
	 * 		DATE is not adjusted by rule 2, since it is expected to 
	 * 		change when the month changes in a RTSDate.
	 *
	 * @return com.txdot.isd.rts.client.general.gui.RTSDate
	 * @param identifier the time field
	 * @param amount the amount of date or time to be added to the field
	 */
	public RTSDate add(int identifier, int amount)
	{
		Calendar temp = new GregorianCalendar();
		temp.set(
			java.util.Calendar.YEAR,
			caCalendar.get(java.util.Calendar.YEAR));
		temp.set(
			java.util.Calendar.MONTH,
			caCalendar.get(java.util.Calendar.MONTH));
		temp.set(
			java.util.Calendar.DATE,
			caCalendar.get(java.util.Calendar.DATE));
		temp.set(
			Calendar.HOUR_OF_DAY,
			caCalendar.get(Calendar.HOUR_OF_DAY));
		temp.set(
			java.util.Calendar.MINUTE,
			caCalendar.get(java.util.Calendar.MINUTE));
		temp.set(
			java.util.Calendar.SECOND,
			caCalendar.get(java.util.Calendar.SECOND));
		temp.set(
			java.util.Calendar.MILLISECOND,
			caCalendar.get(java.util.Calendar.MILLISECOND));
		temp.add(identifier, amount);
		//Dummy call so that all fields are in sync.
		getYear();

		return new RTSDate(
			temp.get(java.util.Calendar.YEAR),
			temp.get(java.util.Calendar.MONTH) + 1,
			temp.get(java.util.Calendar.DATE),
			temp.get(Calendar.HOUR_OF_DAY),
			temp.get(java.util.Calendar.MINUTE),
			temp.get(java.util.Calendar.SECOND),
			temp.get(java.util.Calendar.MILLISECOND));
	}

	/**
	 * Determine if Business Day
	 * 
	 * Excludes 
	 *   - Saturday
	 *   - Sunday
	 *   - Days in RTS_HOLIDAY table 
	 * 
	 * @return boolean 
	 */
	public boolean isBusinessDay()
	{
		boolean lbBusinessDay = true;
		int j = caCalendar.get(Calendar.DAY_OF_WEEK);
		
		// defect 9919 
		if (j == 1
			|| j == 7
			|| HolidayCache.isHoliday(this.getYYYYMMDDDate()))
			//|| TxDOTHolidayCache.isHoliday(this.getYYYYMMDDDate()))
		{
			// end defect 9919 
			
			lbBusinessDay = false;
		}
		return lbBusinessDay;
	}

	/**
	 * This method takes three arguments: an array of string, an array 
	 * of integer that specifies the length of each corresponding 
	 * element in the string array, and the padding required.  It will 
	 * then sized each of the element of the string array according to 
	 * the length specified by the corresponding element in the integer 
	 * array, and return a concatenation of the string elements.
	 *
	 * <P>If the size of the element in the string array is less than 
	 * that specified in the corresponding element in integer array, 
	 * the element will be padded with asPad.<br> If the size of the 
	 * element in the string array is greater than that specified in the 
	 * corresponding element in integer array, the element will 
	 * truncated.<br>
	 *
	 * <P>Example:
	 * String result = 
	 * 		addPadding(new String[]{"aaa","bbb","ccc"}, 
	 * 		new int[]{2,3,4}, "0");
	 * result will be equal to aabbb0ccc
	 * 
	 * @return java.lang.String
	 * @param aarrStr an array of String to be concatenated
	 * @param aiPadLength an array of integer specifying the 
	 * 		length of each String element in aarrStr
	 * @param asPad padding
	 */
	private static String addPadding(
		String[] aarrStr,
		int[] aiPadLength,
		String asPad)
	{
		StringBuffer lsStringBuffer = new StringBuffer();
		for (int i = 0; i < aarrStr.length; i++)
		{
			if (aarrStr[i].length() == aiPadLength[i])
			{
				lsStringBuffer.append(aarrStr[i]);
			}
			else if (aarrStr[i].length() < aiPadLength[i])
			{
				for (int j = 0;
					j < aiPadLength[i] - aarrStr[i].length();
					j++)
				{
					lsStringBuffer.append(asPad);
				}
				lsStringBuffer.append(aarrStr[i]);
			}
			else
			{
				lsStringBuffer.append(
					aarrStr[i].substring(0, aiPadLength[i]));
			}
		}
		return lsStringBuffer.toString();
	}

	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.
	 * (This implies that <tt>x.compareTo(y)</tt> must throw an 
	 * exception iff <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt>
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that 
	 * <tt>x.compareTo(y)==0</tt> implies that 
	 * <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all 
	 * <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally 
	 * speaking, any class that implements the <tt>Comparable</tt> 
	 * interface and violates this condition should clearly indicate 
	 * this fact.  The recommended language is "Note: this class has 
	 * a natural ordering that is inconsistent with equals."
	 * 
	 * @param   aaObj the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.
	 * 
	 * @throws ClassCastException if the specified object's type 
	 * prevents it from being compared to this Object.
	 */
	public int compareTo(java.lang.Object aaObj)
	{
		RTSDate d = (RTSDate) aaObj;

		if (getYear() < d.getYear())
		{
			return -1;
		}
		else if (getYear() > d.getYear())
		{
			return 1;
		}
		else if (getYear() == d.getYear())
		{
			if (getMonth() < d.getMonth())
			{
				return -1;
			}
			else if (getMonth() > d.getMonth())
			{
				return 1;
			}
			else if (getMonth() == d.getMonth())
			{
				if (getDate() < d.getDate())
				{
					return -1;
				}
				else if (getDate() > d.getDate())
				{
					return 1;
				}
				else if (getDate() == d.getDate())
				{
					if (getHour() < d.getHour())
					{
						return -1;
					}
					else if (getHour() > d.getHour())
					{
						return 1;
					}
					else if (getHour() == d.getHour())
					{
						if (getMinute() < d.getMinute())
						{
							return -1;
						}
						else if (getMinute() > d.getMinute())
						{
							return 1;
						}
						else if (getMinute() == d.getMinute())
						{
							if (getSecond() < d.getSecond())
							{
								return -1;
							}
							else if (getSecond() > d.getSecond())
							{
								return 1;
							}
							else if (getSecond() == d.getSecond())
							{
								if (getMillisecond()
									< d.getMillisecond())
								{
									return -1;
								}
								else if (
									getMillisecond()
										> d.getMillisecond())
								{
									return 1;
								}
								else if (
									getMillisecond()
										== d.getMillisecond())
								{
									if (getMicroseconds()
										< d.getMicroseconds())
									{
										return -1;
									}
									else if (
										getMicroseconds()
											> d.getMicroseconds())
									{
										return 1;
									}
									else
									{
										return 0;
									}
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Returns true if the RTSDate is equal.
	 * @return boolean
	 * @param aaObj java.lang.Object
	 */
	public boolean equals(Object aaObj)
	{
		if (aaObj instanceof RTSDate)
		{
			RTSDate laTemp = (RTSDate) aaObj;
			if (laTemp.getYear() == getYear()
				&& laTemp.getMonth() == getMonth()
				&& laTemp.getDate() == getDate()
				&& laTemp.getHour() == getHour()
				&& laTemp.getMinute() == getMinute()
				&& laTemp.getSecond() == getSecond()
				&& laTemp.getMillisecond() == getMillisecond()
				&& laTemp.getMicroseconds() == getMicroseconds())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns the internal time in a 6 digit integer format 
	 * (e.g. 5:34:35PM will be returned as 173434)
	 * 
	 * @return int
	 */
	public int get24HrTime()
	{
		return Integer.parseInt(
			addPadding(
				new String[] {
					String.valueOf(this.getHour()),
					String.valueOf(this.getMinute()),
					String.valueOf(this.getSecond())},
				new int[] { 2, 2, 2 },
				"0"));

	}

	/**
	 * Returns this RTSDate in AMDate format.
	 * 
	 * @return int the AMDate
	 */
	public int getAMDate()
	{
		int liNumDays = 0;
		for (int i = 1900; i < getYear(); i++)
		{
			if (caCalendar.isLeapYear(i))
			{
				liNumDays = liNumDays + 366;
			}
			else
			{
				liNumDays = liNumDays + 365;
			}
		}
		liNumDays = liNumDays + caCalendar.get(Calendar.DAY_OF_YEAR);
		return liNumDays - 1;
	}

	/**
	 * Get Calendar from RTSDate.
	 * 
	 * @return Calendar
	 */
	public java.util.Calendar getCalendar()
	{
		// TODO review settings to make consistent results!
		return caCalendar;
	}

	/**
	 * Adjusts the time given the timezone, either "M", or "C"
	 * 
	 * @param asTimezone java.lang.String
	 */
	public RTSDate getClientTimeZoneAdjustedDate(String asTimezone)
	{
		// TODO should use time zone here?
		// defect 7712
		// Changed to use the local constant so that the 
		// AssignedWorkstationIdsCache class does not have to be used.
		//if (timezone.equals(
		//				AssignedWorkstationIdsCache.MOUNTAIN_TIME_ZONE))
		if (asTimezone.equals(MOUNTAIN_TIME_ZONE))
			// end defect 7712
		{
			setHour(getHour() - 1);
		}
		return this;
	}

	/**
	 * Returns the clock time (e.g. 15:35:34.000)
	 * @return String
	 */
	public String getClockTime()
	{
		// defect 9943 
		String lsClockTime = getClockTimeNoMs();

		if (getMillisecond() < 10)
		{
			lsClockTime = lsClockTime + ".00" + getMillisecond();
		}
		else
		{
			if (getMillisecond() < 100)
			{
				lsClockTime = lsClockTime + ".0" + getMillisecond();
			}
			else
			{
				lsClockTime = lsClockTime + "." + getMillisecond();
			}
		}

		return lsClockTime;
		// end defect 9943 
	}

	/**
	 * Returns the clock time (e.g. 15:35:34)
	 * 
	 * @return String
	 */
	public String getClockTimeNoMs()
	{
		String lsClockTimeNoMs = "";
		if (getHour() < 10)
		{
			lsClockTimeNoMs += "0" + getHour();
		}
		else
		{
			lsClockTimeNoMs += getHour();
		}
		lsClockTimeNoMs += ":";
		if (getMinute() < 10)
		{
			lsClockTimeNoMs += "0" + getMinute();
		}
		else
		{
			lsClockTimeNoMs += getMinute();
		}
		lsClockTimeNoMs += ":";
		if (getSecond() < 10)
		{
			lsClockTimeNoMs += "0" + getSecond();
		}
		else
		{
			lsClockTimeNoMs += getSecond();
		}
		return lsClockTimeNoMs;
	}

	/**
	 * Returns today's date in RTSDate format.
	 * 
	 * @return com.txdot.isd.rts.client.general.gui.RTSDate the 
	 * 		RTSDate at this instant
	 */
	public static RTSDate getCurrentDate()
	{
		Calendar c = Calendar.getInstance();
		return new RTSDate(
			c.get(Calendar.YEAR),
			c.get(Calendar.MONTH) + 1,
			c.get(Calendar.DATE),
			c.get(Calendar.HOUR_OF_DAY),
			c.get(Calendar.MINUTE),
			c.get(Calendar.SECOND),
			c.get(Calendar.MILLISECOND));
	}

	/**
	 * Returns today's date and time in RTSDate format for a given 
	 * timezone.
	 * 
	 * <p>This is used on Server side to ensure time shown is 
	 * appropriate to the client.
	 * 
	 * @param asTimeZone String timezone either "C" or "M"
	 * @return com.txdot.isd.rts.client.general.gui.RTSDate the RTSDate 
	 * 		at this instant
	 */
	public static RTSDate getCurrentDate(String asTimeZone)
	{

		// defect 9139
		// change how this works
		//		int liTimeZoneOffset = 0;
		//		// defect 7712
		//		// Changed to use the local constant so that the 
		//		// AssignedWorkstationIdsCache class does not have to be used.
		//		//if (timeZone.equals(
		//		//			AssignedWorkstationIdsCache.MOUNTAIN_TIME_ZONE))
		//		if (asTimeZone.equals(MOUNTAIN_TIME_ZONE))
		//			// end defect 7712
		//		{
		//			liTimeZoneOffset = 1;
		//		}
		//		Calendar laCal = Calendar.getInstance();
		//
		//		return new RTSDate(
		//			laCal.get(Calendar.YEAR),
		//			laCal.get(Calendar.MONTH) + 1,
		//			laCal.get(Calendar.DATE),
		//			laCal.get(Calendar.HOUR_OF_DAY) - liTimeZoneOffset,
		//			laCal.get(Calendar.MINUTE),
		//			laCal.get(Calendar.SECOND),
		//			laCal.get(Calendar.MILLISECOND));

		RTSDate laNewDate = new RTSDate();
		laNewDate.setTimeZone(asTimeZone);
		return laNewDate;
		// end defect 9139
	}

	/**
	 *  Return Day of Week 
	 * 
	 * @return String 
	 */
	public String getDayOfWeek()
	{
		return DAYSOFWEEK[Calendar.DAY_OF_WEEK - 1];
	}

	/**
	 * Returns the date.
	 * 
	 * @return int
	 */
	public int getDate()
	{
		return caCalendar.get(java.util.Calendar.DATE);
	}

	/**
	 * Returns the date in DB2 format
	 * 
	 * @return java.lang.String
	 */
	public String getDB2Date()
	{
		String lsDb2Date = "" + getYear() + "-";

		if (getMonth() < 10)
		{
			lsDb2Date = lsDb2Date + "0" + getMonth() + "-";
		}
		else
		{
			lsDb2Date = lsDb2Date + getMonth() + "-";
		}

		if (getDate() < 10)
		{
			lsDb2Date = lsDb2Date + "0" + getDate() + "-";
		}
		else
		{
			lsDb2Date = lsDb2Date + getDate() + "-";
		}

		if (getHour() < 10)
		{
			lsDb2Date = lsDb2Date + "0" + getHour() + ".";
		}
		else
		{
			lsDb2Date = lsDb2Date + getHour() + ".";
		}

		if (getMinute() < 10)
		{
			lsDb2Date = lsDb2Date + "0" + getMinute() + ".";
		}
		else
		{
			lsDb2Date = lsDb2Date + getMinute() + ".";
		}

		if (getSecond() < 10)
		{
			lsDb2Date = lsDb2Date + "0" + getSecond() + ".";
		}
		else
		{
			lsDb2Date = lsDb2Date + getSecond() + ".";
		}

		if (getMillisecond() < 10)
		{
			lsDb2Date = lsDb2Date + "00" + getMillisecond();
		}
		else if (getMillisecond() < 100)
		{
			lsDb2Date = lsDb2Date + "0" + getMillisecond();
		}
		else
		{
			lsDb2Date = lsDb2Date + getMillisecond();
		}

		if (getMicroseconds() < 10)
		{
			lsDb2Date = lsDb2Date + "00" + getMicroseconds();
		}
		else if (getMicroseconds() < 100)
		{
			lsDb2Date = lsDb2Date + "0" + getMicroseconds();
		}
		else
		{
			lsDb2Date = lsDb2Date + getMicroseconds();
		}

		return lsDb2Date;
	}
	/**
	 * Returns the hour.
	 * 
	 * @return int
	 */
	public int getHour()
	{
		return caCalendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public String getLongDate()
	{
		return getMonthName() + " " + getDate() + "," + getYear();
	}
	
	/**
	 * Returns date in MM/DD/YYYY format sans possible leading zeros.
	 * 
	 * @return string
	 */
	public String getMFFormattedDate()
	{
		if (caCalendar == null)
			return "";

		int year = getYear();
		int month = getMonth();
		int day = getDate();
		String dateString = "";
		if (month < 10)
		{
			dateString += " ";
		}

		if (day < 10)
		{
			dateString += " ";
		}

		dateString += Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
		return dateString;
	}	
	
	/**
	 * Returns date in March 06, 2012 format because the requirement is to "make everything like mainframe".
	 * 
	 * @return string
	 */
	public String getMFLongDate()
	{
		int day = getDate();
		String dateString = "";
		if (day < 10)
		{
			dateString += "0";
		}
		return getMonthName() + " " + dateString + getDate() + "," + getYear();
	}

	/**
	 * Returns the microseconds
	 * 
	 * @return int
	 */
	public int getMicroseconds()
	{
		return liMicros;
	}

	/**
	 * Returns the millisecond.
	 * 
	 * @return int
	 */
	public int getMillisecond()
	{
		return caCalendar.get(java.util.Calendar.MILLISECOND);
	}

	/**
	 * Returns the minute.
	 * 
	 * @return int
	 */
	public int getMinute()
	{
		return caCalendar.get(java.util.Calendar.MINUTE);
	}

	/**
	 * Returns date in MM/YYYY format.
	 * 
	 * @return string
	 */
	public String getMMYYYY()
	{
		if (caCalendar == null)
			return "  /    ";

		int year = getYear();
		int month = getMonth();

		String dateString = "";
		if (month < 10)
		{
			dateString += "0" + Integer.toString(month);
		}
		else
		{
			dateString += Integer.toString(month);
		}
		dateString += "/";
		dateString += Integer.toString(year);
		return dateString;
	}
	/**
	 * Returns date in MM/DD/YYYY format.
	 * 
	 * @param asDelimiter 
	 * @return String
	 */
	public String getMMDDYYYY(String asDelimiter)
	{
		String lsDateString = new String();
		if (UtilityMethods.isEmpty(asDelimiter))
		{
			asDelimiter = "/";
		}
		if (caCalendar == null)
		{
			lsDateString = " " + asDelimiter + " " + asDelimiter;
		}
		else
		{
			int liYear = getYear();
			int liMonth = getMonth();
			int liDay = getDate();

			if (liMonth < 10)
			{
				lsDateString += "0" + Integer.toString(liMonth);
			}
			else
			{
				lsDateString += Integer.toString(liMonth);
			}
			lsDateString += asDelimiter;

			if (liDay < 10)
			{
				lsDateString += "0" + Integer.toString(liDay);
			}
			else
			{
				lsDateString += Integer.toString(liDay);
			}
			lsDateString += asDelimiter;
			lsDateString += Integer.toString(liYear);
		}
		return lsDateString;
	}

	/**
	 * Returns date in MM/DD/YYYY format.
	 * 
	 * @return string
	 */
	public String getMMDDYYYY()
	{
		return getMMDDYYYY("/");
		//		if (caCalendar == null)
		//			return "  /  /    ";
		//
		//		int year = getYear();
		//		int month = getMonth();
		//		int day = getDate();
		//
		//		String dateString = "";
		//		if (month < 10)
		//		{
		//			dateString += "0" + Integer.toString(month);
		//		}
		//		else
		//		{
		//			dateString += Integer.toString(month);
		//		}
		//		dateString += "/";
		//
		//		if (day < 10)
		//		{
		//			dateString += "0" + Integer.toString(day);
		//		}
		//		else
		//		{
		//			dateString += Integer.toString(day);
		//		}
		//		dateString += "/";
		//		dateString += Integer.toString(year);
		//		return dateString;
	}
	/**
	 * Returns date in MMDDYY format.
	 * 
	 * @return string
	 */
	public String getMMDDYY()
	{
		if (caCalendar == null)
			return "      ";

		int year = getYear();
		int month = getMonth();
		int day = getDate();

		String dateString = "";
		if (month < 10)
		{
			dateString += "0" + Integer.toString(month);
		}
		else
		{
			dateString += Integer.toString(month);
		}
		dateString += "/";
		if (day < 10)
		{
			dateString += "0" + Integer.toString(day);
		}
		else
		{
			dateString += Integer.toString(day);
		}
		dateString += "/";
		dateString += Integer.toString(year).substring(2);
		return dateString;
	}
	
	/**
	 * Returns the month.
	 * 
	 * @return int
	 */
	public int getMonth()
	{
		return caCalendar.get(java.util.Calendar.MONTH) + 1;
	}

	/**
	 * Returns name of Month 
	 * 
	 * @return String
	 */
	public String getMonthName()
	{
		return MONTHNAME[caCalendar.get(java.util.Calendar.MONTH)];
	}
	
	/**
	 * Returns name of Month 
	 * 
	 * @return String
	 */
	public static String getMonthName(int aiMonth)
	{
		return MONTHNAME[aiMonth];
	}

	/**
	 * Calculates the number of months difference between two dates
	 * 
	 * @param aaRTSDate com.txdot.isd.rts.services.util.RTSDate
	 * @return int
	 */
	public int getMonthsDifference(RTSDate aaRTSDate)
	{
		RTSDate laMin = null;
		RTSDate laMax = null;
		int liMultiplier = 1;
		if (this.compareTo(aaRTSDate) == 1)
		{
			laMin = aaRTSDate;
			laMax = this;
		}
		else if (this.compareTo(aaRTSDate) == -1)
		{
			laMin = this;
			laMax = aaRTSDate;
			liMultiplier = -1;
		}
		else
		{
			return 0;
		}

		if (laMin.getYear() == laMax.getYear())
		{
			return (laMax.getMonth() - laMin.getMonth()) * liMultiplier;
		}
		else
		{
			return (
				laMax.getMonth()
					+ 12 * (laMax.getYear() - laMin.getYear())
					- laMin.getMonth())
				* liMultiplier;
		}
	}

	/**
	 * Returns the second.
	 * 
	 * @return int
	 */
	public int getSecond()
	{
		return caCalendar.get(java.util.Calendar.SECOND);
	}

	/**
	 * Returns this RTSDate in java.sql.Date format.
	 * 
	 * @return java.sql.Date
	 */
	public java.sql.Date getSQLDate()
	{
		return new java.sql.Date(caCalendar.getTime().getTime());
	}

	/**
	 * Returns the time as a 6 character string and either "AM" or "PM"
	 * @return java.lang.String
	 */
	public String getTime()
	{
		String lsTime = "";
		if (getHour() == 0)
		{
			lsTime = lsTime + "0" + getHour();
		}
		else
		{
			lsTime = lsTime + getHour();
		}

		if (getMinute() < 10)
		{
			return getTime(lsTime = lsTime + "0" + getMinute());
		}
		else
		{
			return getTime(lsTime = lsTime + getMinute());
		}
	}
	/**
	 * Returns the time as a 8 character string and either "AM" or "PM"
	 * 
	 * @return String
	 */
	public String getTimeSS()
	{
		int liHour = getHour();
		boolean lbPM = liHour >= 12;
		String lsSuffix = lbPM ? "PM" : "AM";
		liHour = lbPM && liHour > 12 ? liHour - 12 : liHour;
		liHour = !lbPM && liHour == 0 ? 12 : liHour;
		String lsHour = UtilityMethods.addPadding("" + liHour, 2, "0");
		String lsMinute =
			UtilityMethods.addPadding("" + getMinute(), 2, "0");
		String lsSecond =
			UtilityMethods.addPadding("" + getSecond(), 2, "0");
		String lsTime =
			lsHour + ":" + lsMinute + ":" + lsSecond + " " + lsSuffix;
		return lsTime;
	}

	/**
	 * Returns a String representation of the time given a 6 digit 
	 * 			number (e.g. 142345 will return 2:23:45PM)
	 * 
	 * @param time aiTime
	 * @return java.lang.String
	 */
	public static String getTime(int aiTime)
	{
		return getTime(Integer.toString(aiTime));
	}

	/**
	 * Returns a String representation of the time given a 6 digit 
	 * 			number (e.g. 142345 will return 2:23:45PM)
	 * 
	 * @param time java.lang.String
	 * @return java.lang.String
	 */
	public static String getTime(String asTime)
	{
		asTime =
			asTime.substring(0, asTime.length() - 2)
				+ ":"
				+ asTime.substring(asTime.length() - 2, asTime.length());

		if (Integer.parseInt(asTime.substring(0, asTime.length() - 3))
			== 0)
		{
			return "12"
				+ asTime.substring(asTime.length() - 3, asTime.length())
				+ "AM";
		}
		else if (
			Integer.parseInt(asTime.substring(0, asTime.length() - 3))
				< 10)
		{
			return "0" + asTime + "AM";
		}
		else if (
			Integer.parseInt(asTime.substring(0, asTime.length() - 3))
				< 12)
		{
			return asTime + "AM";
		}
		else if (
			Integer.parseInt(asTime.substring(0, asTime.length() - 3))
				== 12)
		{
			return asTime + "PM";
		}
		else if (
			Integer.parseInt(asTime.substring(0, asTime.length() - 3))
				< 21)
		{
			return "0"
				+ (Integer
					.parseInt(asTime.substring(0, asTime.length() - 3))
					- 12)
				+ asTime.substring(asTime.length() - 3, asTime.length())
				+ "PM";
		}
		else
		{
			return ""
				+ (Integer
					.parseInt(asTime.substring(0, asTime.length() - 3))
					- 12)
				+ asTime.substring(asTime.length() - 3, asTime.length())
				+ "PM";
		}
	}

	/**
	 * Returns this RTSDate in java.sql.Timestamp format.
	 * 
	 * @return java.sql.Timestamp
	 */
	public Timestamp getTimestamp()
	{
		if (caCalendar == null)
		{
			return null;
		}
		Timestamp laTimestamp =
			new Timestamp(caCalendar.getTime().getTime());

		int liMicroseconds =
			(getMillisecond() * 1000000) + (getMicroseconds() * 1000);
		laTimestamp.setNanos(liMicroseconds);

		return laTimestamp;
	}

	/**
	 * Adjusts the time given the timezone, either "M", or "C"
	 * 
	 * @param timezone java.lang.String
	 */
	public RTSDate getTimeZoneAdjustedDate(String asTimezone)
	{
		// TODO review need
		// defect 7712
		// Changed to use the local constant so that the 
		// AssignedWorkstationIdsCache class does not have to be used.
		//if (timezone.equals(
		//			AssignedWorkstationIdsCache.MOUNTAIN_TIME_ZONE))
		if (asTimezone.equals(MOUNTAIN_TIME_ZONE))
			// end defect 7712
		{
			return add(java.util.Calendar.HOUR, -1);
		}
		else
		{
			return this;
		}
	}

	/**
	 * Returns the year.
	 * 
	 * @return int
	 */
	public int getYear()
	{
		return caCalendar.get(java.util.Calendar.YEAR);
	}

	/**
	 * Returns this RTSDate in yyyymmdd format.
	 * 
	 * @return int
	 */
	public int getYYYYMMDDDate()
	{
		String lsStrMonth = "";
		int liMonth = getMonth();
		if (liMonth < 10)
		{
			lsStrMonth = "0" + liMonth;
		}
		else
		{
			lsStrMonth = "" + liMonth;
		}

		String lsStrDate = "";
		int liDate = getDate();
		if (liDate < 10)
		{
			lsStrDate = "0" + liDate;
		}
		else
		{
			lsStrDate = "" + liDate;
		}

		String lsNum = "" + getYear() + lsStrMonth + lsStrDate;
		return Integer.parseInt(lsNum);
	}
	
	/** 
	 * Return boolean to denote if date in Future
	 * 
	 * @return boolean 
	 */
	public boolean isFutureDate() 
	{
		return this.getYYYYMMDDDate() > new RTSDate().getYYYYMMDDDate(); 
	}
	
	/** 
	 * Return boolean to denote if date in Future
	 * 
	 * @return boolean 
	 */
	public boolean isFutureDate(RTSDate aaDate) 
	{
		return aaDate.getYYYYMMDDDate() > new RTSDate().getYYYYMMDDDate(); 
	}
	
	
	/** 
	 * 
	 * Is ValidDate()
	 * 
	 * Derived (somewhat) from client.general.ui.RTSDateField  
	 * 
	 * @return boolean 
	 */
	public boolean isValidDate()
	{
		int liMonth = getMonth();
		int liDay = getDate();
		int liYear = getYear();

		// defect 11370
		// <31 should be <= 31 
		boolean lbValid =
			liMonth >= 1
				&& liMonth <= 12
				&& liDay > 0
				&& liDay <= 31
				//&& liDay < 31
				&& (liYear >= 1900 && liYear < 2099);
		// end defect 11370 

		if (lbValid)
		{
			if (liMonth == 2)
			{
				if (liDay > 29)
				{
					lbValid = false;
				}
			}
			else if (
				liMonth == 4
					|| liMonth == 6
					|| liMonth == 9
					|| liMonth == 11)
			{
				if (liDay > 30)
				{
					lbValid = false;
				}
			}
		}
		return lbValid;
	}

	/**
	 * Used to test RTSDate class
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args)
	{
		RTSDate d = new RTSDate(RTSDate.YYYYMMDD, 19871005);
		System.out.println(
			"Should be equal...  " + d + " = " + d.getYYYYMMDDDate());

		RTSDate d2 = new RTSDate(1987, 12, 25);
		System.out.println("Should be Xmas = " + d2.getYYYYMMDDDate());

		System.out.println("Should be false = " + d.equals(d2));

		System.out.println(
			"Should be equal ... " + d + " = " + d.getTimestamp());
		System.out.println("Today = " + RTSDate.getCurrentDate());

		System.out.println("*** Test of Long constructor *** ");
		RTSDate d3 = new RTSDate(new java.sql.Timestamp(257290210000l));
		RTSDate d4 = new RTSDate(2001, 2, 25, 15, 30, 10, 10);
		System.out.println(
			"Should be equal ... " + d3.getTimestamp() + " = " + d4);

		System.out.println("***  Test of 7 digit constructor ***");
		RTSDate d5 = new RTSDate(1999, 4, 5, 10, 31, 15, 10);
		System.out.println("hour = " + d5.getHour());
		System.out.println("min = " + d5.getMinute());
		System.out.println("sec = " + d5.getSecond());

		System.out.println("*** Test of hour again***");
		RTSDate d6 = new RTSDate(2001, 9, 27, 2, 15, 45, 6);
		System.out.println(
			d6 + " " + d6.getHour() + ":" + d6.getMinute());
		System.out.println(d6.getClockTime());

		System.out.println("*** Test of add ***");
		RTSDate d7 = new RTSDate(2001, 8, 23);
		RTSDate d8 = d7.add(java.util.Calendar.DATE, -1);
		System.out.println(d7);
		System.out.println(d8);
		System.out.println(RTSDate.getCurrentDate());
		System.out.println(new RTSDate());

		System.out.println("*** Test of DB2 ***");
		RTSDate d9 = new RTSDate("2001-11-06-11.16.00.000002");
		System.out.println(d9);
		System.out.println(d9.getClockTime());
		System.out.println(d9.getDB2Date());
		RTSDate xx = new RTSDate("2002-01-08 14:45:25.000002");
		System.out.println(
			"2002-01-08 14:45:25.000002 should equal "
				+ xx.getDB2Date());

		System.out.println(RTSDate.getTime(934));
		System.out.println(RTSDate.getTime(1734));
		System.out.println(RTSDate.getTime(2235));

		System.out.println(RTSDate.getTime("0016"));
		System.out.println(RTSDate.getTime("1123"));
		System.out.println(RTSDate.getTime("1634"));

		System.out.println(
			"\n*** Test of Timestamp and milliseconds***\n");
		RTSDate yy1 = new RTSDate(2002, 2, 25, 3, 30, 25, 6);
		java.sql.Timestamp timestamp = yy1.getTimestamp();
		timestamp.setNanos(6070000);
		RTSDate yy2 = new RTSDate(timestamp);
		java.sql.Timestamp timestamp2 = yy2.getTimestamp();
		System.out.println(
			"Should be equal = " + timestamp2 + " " + timestamp);
		System.out.println("Should be false = " + yy1.equals(yy2));
		System.out.println(yy1 + " " + yy1.getClockTime());
		System.out.println(yy2 + " " + yy2.getClockTime());
		System.out.println(yy2.getDB2Date());
		timestamp.setNanos(6000000);
		yy2 = new RTSDate(timestamp);
		System.out.println("Should be true = " + yy1.equals(yy2));

		System.out.println("*** Test of 7 int constructor *** ");
		RTSDate u = new RTSDate(2002, 1, 9, 0, 0, 0, 0);
		System.out.println(u + " " + u.getClockTime());
		System.out.println(u.get24HrTime());
		System.out.println(u.getTime());

		System.out.println("*** Test of Xmas discrepency ***");
		RTSDate xmas = new RTSDate("2002-12-31 14:14:14.003003");
		System.out.println(xmas);
		java.sql.Timestamp tstamp = xmas.getTimestamp();
		System.out.println(tstamp);
		System.out.println("time = " + tstamp.getTime());
		RTSDate xmas2 = new RTSDate(tstamp);
		System.out.println(xmas2 + " " + xmas2.getClockTime());

		System.out.println("\n***Test of long construct ***");
		java.io.File f = new java.io.File("d:\\rts\\rpt\\Iarbat3.rpt");
		RTSDate aDate = new RTSDate(2001, 9, 24, 3, 12, 36, 0);
		RTSDate qDate = new RTSDate(f.lastModified());
		RTSDate fDate = new RTSDate(aDate.getTimestamp().getTime());
		System.out.println(
			"long value of lastModified() = " + f.lastModified());
		System.out.println(
			"long value of RTSDate = "
				+ aDate.getTimestamp().getTime());
		System.out.println(aDate + " " + aDate.getClockTime());
		System.out.println(fDate + " " + fDate.getClockTime());
		System.out.println(qDate + " " + qDate.getClockTime());

		System.out.println("** Test of Timezone ** ");
		RTSDate tz = new RTSDate(2002, 2, 25, 0, 30, 30, 6);
		System.out.println(tz + " " + tz.getClockTime());
		System.out.println(
			tz.getTimeZoneAdjustedDate("M")
				+ " "
				+ tz.getTimeZoneAdjustedDate("M").getClockTime());

		System.out.println("\n");
		RTSDate xxi = new RTSDate();
		xxi.setTime(131);
		System.out.println(xxi.getClockTime());

		System.out.println(RTSDate.getTime(1202));
		System.out.println(RTSDate.getTime("1202"));

	}

	// defect 9139
	//	/**
	//	 * Subtract an hour from time if it's during daylight savings
	//	 * OS/2 seems to not store daylight savings and by doing this, we 
	//	 * can correct that bug in OS/2 technically, the above code should 
	//	 * work. The problem arises because the time is stored as the 
	//	 * number of milliseconds since 1.1.1970. Since 2:00am to 3:00am 
	//	 * never occurred, the above code "knows" to move the time ahead 
	//	 * an hour the OS/2 doesn't seem to calculate this, and thus 
	//	 * believes 2:00am to 3:00am did happen is an hour behind the right 
	//	 * time.
	//	 * 
	//	 * @return com.txdot.isd.rts.services.util.RTSDate
	//	 */
	//	public void os2Offset()
	//	{
	//		SimpleTimeZone cst =
	//			new SimpleTimeZone(-6 * 60 * 60 * 1000, "CST");
	//
	//		// set up rules for daylight savings time
	//		cst.setStartRule(
	//			Calendar.APRIL,
	//			1,
	//			Calendar.SUNDAY,
	//			2 * 60 * 60 * 1000);
	//		cst.setEndRule(
	//			Calendar.OCTOBER,
	//			-1,
	//			Calendar.SUNDAY,
	//			2 * 60 * 60 * 1000);
	//
	//		if (cst.inDaylightTime(caCalendar.getTime()))
	//			caCalendar.add(RTSDate.HOUR, -1);
	//	}
	// end defect 9139

	/**
	 * Sets the RTSDate.
	 * 
	 * @param aiYear int the year
	 * @param aiMonth int the month
	 * @param aiDay int the date
	 */
	public void set(int aiYear, int aiMonth, int aiDay)
	{
		setYear(aiYear);
		setMonth(aiMonth);
		setDate(aiDay);
	}

	/**
	 * Sets the RTSDate.
	 * 
	 * @param aiYear int the year
	 * @param aiMonth int the month
	 * @param aiDay int the date
	 * @param aiHour int the hour
	 * @param aiMin int the minute
	 * @param aiSec int the second
	 * @param aiMilliSec int the millisecond
	 */
	public void set(
		int aiYear,
		int aiMonth,
		int aiDay,
		int aiHour,
		int aiMin,
		int aiSec,
		int aiMilliSec)
	{
		setYear(aiYear);
		setMonth(aiMonth);
		setDate(aiDay);
		setHour(aiHour);
		setMinute(aiMin);
		setSecond(aiSec);
		setMillisecond(aiMilliSec);
	}

	/**
	 * Set RTSDate via Calendar.
	 * 
	 * @param aaCalendar
	 */
	public void setCalendar(java.util.Calendar aaCalendar)
	{
		// TODO review settings to make consistent results!
		caCalendar = (GregorianCalendar) aaCalendar;
	}

	/**
	 * Sets the date.
	 * 
	 * @param aiNewDate int
	 */
	public void setDate(int aiNewDate)
	{
		caCalendar.set(java.util.Calendar.DATE, aiNewDate);
	}

	/**
	 * Sets the hour.
	 * 
	 * @param aiNewHour int
	 */
	public void setHour(int aiNewHour)
	{
		caCalendar.set(Calendar.HOUR_OF_DAY, aiNewHour);
	}

	/**
	 * Sets the microseconds
	 * 
	 * @param aiMicros int
	 */
	public void setMicroseconds(int aiMicros)
	{
		this.liMicros = aiMicros;
	}

	/**
	 * Sets the millisecond.
	 * 
	 * @param aiNewMillisecond int
	 */
	public void setMillisecond(int aiNewMillisecond)
	{
		caCalendar.set(
			java.util.Calendar.MILLISECOND,
			aiNewMillisecond);
	}

	/**
	 * Sets the minute.
	 * 
	 * @param aiNewMinute int
	 */
	public void setMinute(int aiNewMinute)
	{
		caCalendar.set(java.util.Calendar.MINUTE, aiNewMinute);
	}

	/**
	 * Sets the month.
	 * 
	 * @param aiNewMonth int
	 */
	public void setMonth(int aiNewMonth)
	{
		caCalendar.set(java.util.Calendar.MONTH, aiNewMonth - 1);
	}

	/**
	 * Sets the second.
	 * 
	 * @param aiNewSecond int
	 */
	public void setSecond(int aiNewSecond)
	{
		caCalendar.set(java.util.Calendar.SECOND, aiNewSecond);
	}

	/**
	 * Sets the time where the integer argument is a 6 digit integer 
	 * (e.g. 134811 will become internally 13:48.11)
	 * 
	 * @param aiTime int
	 */
	public void setTime(int aiTime)
	{
		String strTime = Integer.toString(aiTime);

		while (strTime.length() < 6)
		{
			strTime = "0" + strTime;
		}

		setHour(
			Integer.parseInt(
				strTime.substring(0, strTime.length() - 4)));

		setMinute(
			Integer.parseInt(
				strTime.substring(
					strTime.length() - 4,
					strTime.length() - 2)));
		setSecond(
			Integer.parseInt(
				strTime.substring(
					strTime.length() - 2,
					strTime.length())));
	}

	/**
	 * Sets the time where the integer argument is a 6 char String 
	 * (e.g. 134811 will become internally 13:48.11)
	 * 
	 * @param asTime java.lang.String
	 */
	public void setTime(String asTime)
	{
		setTime(Integer.parseInt(asTime));
	}

	/**
	 * Sets the TimeZone according to where the workstation is.
	 * 
	 * <ul>
	 * <li>"C" is Central (America/Chicago).
	 * <li>"M" is Mountain (America/Denver).
	 * <eul>
	 * 
	 * <p>If we do not know the string, use the system default.
	 * 
	 * @param asTimeZone java.lang.String
	 */
	public void setTimeZone(String asTimeZone)
	{
		TimeZone laTZ = null;

		if (asTimeZone.equalsIgnoreCase("C"))
		{
			// use the Central TimeZone
			laTZ = TimeZone.getTimeZone("America/Chicago");
		}
		else if (asTimeZone.equalsIgnoreCase("M"))
		{
			// use the Mountain TimeZone
			laTZ = TimeZone.getTimeZone("America/Denver");
		}
		else
		{
			// use the system default
			laTZ =
				TimeZone.getTimeZone(
					System.getProperty("user.timezone"));
		}

		caCalendar.setTimeZone(laTZ);
	}

	/**
	 * Sets the year.
	 * 
	 * @param aiNewYear int
	 */
	public void setYear(int aiNewYear)
	{
		caCalendar.set(java.util.Calendar.YEAR, aiNewYear);
	}

	/**
	 * Show Message For Recent CCO 
	 * 
	 * @return boolean 
	 */
	public boolean showMsgForRecentCCO()
	{
		RTSDate laCCOCompDate =
			this.add(RTSDate.DATE, TitleConstant.CCO_COMPARE_DAYS);

		RTSDate laToday =
			new RTSDate(
				RTSDate.YYYYMMDD,
				new RTSDate().getYYYYMMDDDate());

		if (SystemProperty.isDevStatus())
		{
			System.out.println("     Issue Date: " + this.toString());
			System.out.println(
				"Compare Date "
					+ TitleConstant.CCO_COMPARE_DAYS
					+ ": "
					+ laCCOCompDate.toString());
			System.out.println(
				"   Today's Date: " + laToday.toString());

		}
		return laCCOCompDate.compareTo(laToday) >= 0;
	}

	/**
	 * Prints out the RTSDate in the format  MM/DD/YYYY.
	 * 
	 * @return java.lang.String
	 */
	public String toString()
	{
		if (caCalendar == null)
		{
			return "  /  /    ";
		}

		int liYear = getYear();
		int liMonth = getMonth();
		int liDate = getDate();

		String lsDateString = "";
		if (liMonth < 10)
		{
			lsDateString =
				lsDateString + "0" + Integer.toString(liMonth);
		}
		else
		{
			lsDateString = lsDateString + Integer.toString(liMonth);
		}

		lsDateString = lsDateString + "/";

		if (liDate < 10)
		{
			lsDateString =
				lsDateString + "0" + Integer.toString(liDate);
		}
		else
		{
			lsDateString = lsDateString + Integer.toString(liDate);
		}

		lsDateString = lsDateString + "/";
		lsDateString = lsDateString + Integer.toString(liYear);

		return lsDateString;
	}

}
