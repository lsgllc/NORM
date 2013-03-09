package com.txdot.isd.rts.services.webapps.util;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/*
 * UtilityMethods.java 
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/31/2005	Java 1.4 Work
 * 							deprecate prefillStringWithZeros()
 *							defect 7889 Ver 5.2.3
 * Jeff S.		05/03/2007	Add object copy method. And undeprecated a 
 * 							method that we are now using.
 * 							undeprecate prefillStringWithZeros()
 * 							add copy()
 * 							defect 9121 Ver Special Plates
 * Bob B.		08/13/2008	Add a method to generate 2 random Chars
 * 							to reduce Internet trace numbers to 15
 * 							total Characters (including the "A")
 * 							add getTwoRandomChars(), 
 * 							getTwoRandomSPChars()
 * 							defect 9671 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * UtilityMethods
 * 
 * @version	Special Plates	08/13/2007
 * @author	Administrator
 * <br>Creation Date:		11/12/2001 11:07:46   
 */

public class UtilityMethods
{
	public static final char[] carrChar =
		new char[] {
			'0',
			'1',
			'2',
			'3',
			'4',
			'5',
			'6',
			'7',
			'8',
			'9',
			'A',
			'B',
			'C',
			'D',
			'E',
			'F',
			'G',
			'H',
			'I',
			'J',
			'K',
			'L',
			'M',
			'N',
			'O',
			'P',
			'Q',
			'R',
			'S',
			'T',
			'U',
			'V',
			'W',
			'X',
			'Y',
			'Z' };
			
	/**
	 * UtilityMethods constructor comment.
	 */
	public UtilityMethods()
	{
		super();
	}
	
	/**
	 * Add day 
	 * 
	 * @param asDateTime String
	 * @param asPattern  String
	 * @param aiDays int
	 * 
	 * @return String 
	 */
	public static String addDay(
		String asDateTime,
		String asPattern,
		int aiDays)
	{
		SimpleDateFormat laSDF = new SimpleDateFormat(asPattern);
		java.util.Date laDate =
			laSDF.parse(asDateTime, new ParsePosition(0));

		Calendar laCal = Calendar.getInstance();
		laCal.setTime(laDate);
		laCal.add(Calendar.DAY_OF_MONTH, aiDays);

		laDate = laCal.getTime();
		String asDate = laSDF.format(laDate);
		return asDate;
	}
	
	/**
	 * Add Hour 
	 * 
	 * @param asDateTime String
	 * @param asPattern String
	 * @param aiHours int
	 * @return String 
	 */
	public static String addHour(
		String asDateTime,
		String asPattern,
		int aiHours)
	{
		SimpleDateFormat laSDF = new SimpleDateFormat(asPattern);
		java.util.Date laDate =
			laSDF.parse(asDateTime, new ParsePosition(0));

		Calendar laCal = Calendar.getInstance();
		laCal.setTime(laDate);
		laCal.add(Calendar.HOUR_OF_DAY, aiHours);

		laDate = laCal.getTime();
		String asDate = laSDF.format(laDate);
		return asDate;
	}
	
	/**
	 * Add One Day 
	 * 
	 * @param asDateTime String
	 * @param asPattern String
	 * @return String
	 */
	public static String addOneDay(String asDateTime, String asPattern)
	{
		return addDay(asDateTime, asPattern, 1);
	}
	
	/**
	 * Add one hour 
	 * 
	 * @param asDateTime String 
	 * @param asPattern String 
	 * @return String 
	 */
	public static String addOneHour(
		String asDateTime,
		String asPattern)
	{
		return addHour(asDateTime, asPattern, 1);
	}
	
	/**
	 * Add Time To Msg 
	 * 
	 * @param asMessage String 
	 * @return String 
	 */
	public static String addTimeToMsg(String asMessage)
	{
		return "[" + new java.util.Date() + "]" + asMessage;
	}
	
	/**
	 * Object copy.
	 * 
	 * @param aaToCopy Object
	 * @return Object
	 */
	public static Object copy(Object aaToCopy)
	{
		try
		{
			ByteArrayOutputStream lpfsBAOS =
				new ByteArrayOutputStream();
			ObjectOutputStream lpfsOS =
				new ObjectOutputStream(lpfsBAOS);
			lpfsOS.writeObject(aaToCopy);
			lpfsOS.flush();
			ByteArrayInputStream lpfsIS =
				new ByteArrayInputStream(lpfsBAOS.toByteArray());
			ObjectInputStream lpfsIN = new ObjectInputStream(lpfsIS);
			Object laToReturn = lpfsIN.readObject();
			lpfsBAOS.close();
			lpfsOS.close();
			lpfsIS.close();
			lpfsIN.close();
			return laToReturn;
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			aeCNFEx.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Format Double 
	 * 
	 * @param adDouble Double 
	 * @return String 
	 */
	public static String format(double adDouble)
	{
		NumberFormat laNumFormat = NumberFormat.getInstance();
		laNumFormat.setMaximumFractionDigits(2);
		laNumFormat.setMinimumFractionDigits(2);
		return laNumFormat.format(adDouble);
	}
	
	/**
	 * Format Date 
	 * 
	 * @param adDouble double
	 * @return String 
	 */
	public static String format1(double adDouble)
	{
		String lsFormatedNumber = format(adDouble);
		return format1(lsFormatedNumber);
	}
	
	/**
	 * Format Date 
	 * 
	 * @param asFormatedNumber String 
	 * @return String
	 */
	
	public static String format1(String asFormatedNumber)
	{
		StringBuffer lsResult = new StringBuffer();
		for (int i = 0; i < asFormatedNumber.length(); ++i)
		{
			char c = asFormatedNumber.charAt(i);
			if ((c >= '0' && c <= '9') || c == '.')
				lsResult.append(c);
		}
		return lsResult.toString();
	}
	
	/**
	 * Format Date
	 * 
	 * @param asDate String
	 * @param asOrigPattern String 
	 * @param asPattern String 
	 * @return String
	 */
	public static String formatDate(
		String asDate,
		String asOrigPattern,
		String asPattern)
	{
		SimpleDateFormat laSDF = new SimpleDateFormat(asOrigPattern);
		Date laDate = laSDF.parse(asDate, new ParsePosition(0));
		return formatDate(laDate, asPattern);
	}
	
	/**
	 * 
	 * Format Date 
	 * 
	 * @param aaDate Date
	 * @param asPattern String 
	 * @return String 
	 */
	public static String formatDate(Date aaDate, String asPattern)
	{
		SimpleDateFormat laSDF = new SimpleDateFormat(asPattern);
		return laSDF.format(aaDate);
	}
	
	/**
	 * 
	 * Format Today's Date 
	 * 
	 * @param lsPattern String 
	 * @return String 
	 */
	public static String formatToday(String lsPattern)
	{
		return formatDate(new java.util.Date(), lsPattern);
	}
	
	/**
	 * Calculate Convenience Fee Trace Number from Registration
	 * Renewal Fee Trace Number.
	 * 
	 * @param asRegRenTraceNo String 
	 * @return String 
	 */
	public static String getConvFeeTraceNo(String asRegRenTraceNo)
	{
		char lcLastChar =
			asRegRenTraceNo.charAt(asRegRenTraceNo.length() - 1);
			
		for (int i = 0; i < carrChar.length; ++i)
		{
			if (lcLastChar == carrChar[i])
			{
				lcLastChar = carrChar[(i + 1) % carrChar.length];
				break;
			}
		}
		String lsConvFeeTraceNo =
			asRegRenTraceNo.substring(0, asRegRenTraceNo.length() - 1)
				+ lcLastChar;
		return lsConvFeeTraceNo;
	}
	
	/**
	 * 
	 * Return Random Integer 
	 * 
	 * @return String 
	 */
	public static String getRandom()
	{
		Random r = new Random();
		int n1 = r.nextInt(36);
		int n2 = r.nextInt(36);
		int n3 = r.nextInt(36);
		int n4 = r.nextInt(36);
		return "" + carrChar[n1] + carrChar[n2] + carrChar[n3] + carrChar[n4];
	}
	
	/**
	 * 
	 * Return Random Integer 
	 * 
	 * @return String 
	 */
	public static String getTwoRandomSPChars()
	{
		Random r = new Random();
		int n1 = r.nextInt(36);
		int n2 = r.nextInt(36);
		return "" + carrChar[n1] + carrChar[n2];
	}
	
	/**
	 * 
	 * Return String Array of 4 Random Characters 
	 * 
	 * @return String[] 
	 */
	public static String[] getRandomPair()
	{
		Random r = new Random();
		int n1 = r.nextInt(36);
		int n2 = r.nextInt(36);
		int n3 = r.nextInt(36);
		int n4 = r.nextInt(36);
		String[] s = new String[2];
		s[0] = "" + carrChar[n1] + carrChar[n2] + carrChar[n3] + carrChar[n4];
		n4 = (n4 + 1) % 36;
		s[1] = "" + carrChar[n1] + carrChar[n2] + carrChar[n3] + carrChar[n4];
		return s;
	}

	/**
	 * 
	 * Return String Array of 2 Random Characters 
	 * 
	 * @return String[] 
	 */	
	public static String[] getTwoRandomChars()
	{
		Random r = new Random();
		int n1 = r.nextInt(36);
		int n2 = r.nextInt(36);
		String[] s = new String[2];
		s[0] = "" + carrChar[n1] + carrChar[n2];
		n2 = (n2 + 1) % 36;
		s[1] = "" + carrChar[n1] + carrChar[n2];
		return s;
	}
	
	/**
	 * Get Stack Trace 
	 * 
	 * @param aeThrowable Throwable
	 * @return String 
	 */
	public static String getStackTrace(Throwable aeThrowable)
	{
		StringWriter laStringWriter = new StringWriter();
		aeThrowable.printStackTrace(new PrintWriter(laStringWriter));
		
		try
		{
			laStringWriter.close();
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
		}
		return new java.util.Date().toString()
			+ "\n:"
			+ laStringWriter.toString();
	}
	
	/**
	 * Determine if current date
	 * 
	 * @param asDateTime String
	 * @param asPattern String 
	 * @return boolean 
	 */
	public static boolean isToday(String asDateTime, String asPattern)
	{
		SimpleDateFormat laSDF = new SimpleDateFormat(asPattern);
		Date laDate = laSDF.parse(asDateTime, new ParsePosition(0));

		laSDF.applyPattern("yyyyMMdd");
		Calendar laCal = Calendar.getInstance();
		Date laToday = laCal.getTime();

		String lsDate = laSDF.format(laDate);
		String lsToday = laSDF.format(laToday);
		
		if (lsDate.equals(lsToday))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 
	 * Main 
	 * 
	 * @param aarrArgs
	 */
	public static void main(String[] aarrArgs)
	{
		/*
		for(int i=0;i<100;++i){
			String[] s=UtilityMethods.getRandomPair();
			System.out.println(s[0]+" "+s[1]);
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
			}
		}
		*/
		getConvFeeTraceNo("101VRABCDEAABZ");
	}
	
	/**
	 * objToByteArray
	 * 
	 * @param aaObject Object
	 * @return byte[] 
	 * @throws IOException 
	 */
	public static byte[] objToByteArray(Object aaObject)
		throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(aaObject);
		oos.flush();
		return baos.toByteArray();
	}
	
	/**
	 * 
	 * Parse Date 
	 * 
	 * @param asDate String
	 * @param asPattern String 
	 * @return Date 
	 */
	public static Date parseDate(String asDate, String asPattern)
	{
		SimpleDateFormat laSDF = new SimpleDateFormat(asPattern);
		Date laDate = laSDF.parse(asDate, new ParsePosition(0));
		return laDate;
	}
	
	/**
	 * prefillStringWithZeros
	 * 
	 * @param aiDesiredStringLenth int
	 * @param asOriginalString String 
	 * @return String  
	 */
	public static String prefillStringWithZeros(
		int aiDesiredStringLength,
		String asOriginalString)
	{

		String lsNewString = asOriginalString;
		int stringLength = asOriginalString.length();

		for (int i = stringLength; i < aiDesiredStringLength; i++)
		{
			lsNewString = "0" + lsNewString;
		}

		return lsNewString;
	}
	
	/**
	 * Return formatted Phone No 
	 * 
	 * @param asPhone String
	 * @return String
	 */
	public static String toPhoneFormat(String asPhone)
	{
		if (asPhone == null || asPhone.equals(""))
		{
			return "()-";
		}
		asPhone = asPhone.trim();
		StringBuffer lsStringBuffer = new StringBuffer();
		if (asPhone == null || asPhone.length() == 0)
		{
			return "";
		}
		else
		{
			if (asPhone.length() > 3)
			{
				lsStringBuffer.append("(");
				lsStringBuffer.append(asPhone.substring(0, 3));
				lsStringBuffer.append(") ");
				if (asPhone.length() >= 6)
				{
					lsStringBuffer.append(asPhone.substring(3, 6));
					lsStringBuffer.append("-");
					if (asPhone.length() > 6)
					{
						lsStringBuffer.append(asPhone.substring(6));
					}
				}
				else
				{
					lsStringBuffer.append(asPhone.substring(3));
				}
			}
			else
			{
				lsStringBuffer.append(asPhone);
			}
		}
		return lsStringBuffer.toString();
	}
}
