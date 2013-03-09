package com.txdot.isd.rts.services.reports.help;

import com.txdot.isd.rts.services.data.MessageData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * GenMessageReport.java
 *
 * (c) Texas Department of Transporation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * Jeff S.		04/03/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */
 
/**
 * Prepares a message to be printed.
 *
 * @version	Broadcast Message 	04/03/2007
 * @author	Jeff S.
 * <br>Creation Date:			03/27/2006 10:34:22
 */
public class GenMessageReport
{
	private static final String DATE = "Date:    ";
	private static final String FROM = "From:    ";
	private static final String LINE =
		"---------------------------------------------------------"
			+ "----------------------------------------------------"
			+ "------------------------";
	private static final char NEW_LINE = '\n';
	private static final char SPACE = ' ';
	private static final String STR_NEW_LINE = "\n";
	private static final String STR_RETURN = "\r";
	private static final String SUBJECT = "Subject: ";
	private static final String TO = "To:      ";
	
	/**
	 * starts the application to format a message to be printed.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		GenMessageReport laGVR = new GenMessageReport();
		System.out.println(laGVR.formatReport(new MessageData()));
	}
	
	/**
	 * GenVoidReport constructor
	 */
	public GenMessageReport()
	{
		super();
	}
	
	/**
	 * Format email message to be printed.
	 * 
	 * @param aaMessage MessageData
	 * @return String
	 */
	public String formatReport(MessageData aaMessage)
	{
		StringBuffer lsSB = new StringBuffer();
		lsSB.append(LINE + CommonConstant.SYSTEM_LINE_SEPARATOR);
		lsSB.append(
			FROM
				+ aaMessage.getFrom()
				+ CommonConstant.SYSTEM_LINE_SEPARATOR);
		lsSB.append(
			TO
				+ aaMessage.getTo()
				+ CommonConstant.SYSTEM_LINE_SEPARATOR);
		lsSB.append(
			DATE
				+ aaMessage.getDate()
				+ CommonConstant.STR_SPACE_ONE
				+ aaMessage.getDate().getTime()
				+ CommonConstant.SYSTEM_LINE_SEPARATOR);
		lsSB.append(
			SUBJECT
				+ wordWrapString(aaMessage.getSubject(), 131)
				+ CommonConstant.SYSTEM_LINE_SEPARATOR);
		lsSB.append(LINE + CommonConstant.SYSTEM_LINE_SEPARATOR);
		lsSB.append(CommonConstant.SYSTEM_LINE_SEPARATOR);

		lsSB.append(
			wordWrapString(aaMessage.getMessage(), 131)
				+ CommonConstant.SYSTEM_LINE_SEPARATOR);

		return lsSB.toString();
	}
	
	/**
	 * Return a string that is chopped into a max lenth of each line.
	 * 
	 * @param asMessage String
	 * @param aiMaxLength int
	 */
	private String wordWrapString(String asMessage, int aiMaxLength)
	{
		StringBuffer lsBuf = new StringBuffer(asMessage);
		//String lsFinalString = CommonConstant.STR_SPACE_EMPTY;
		int liLastspace = -1;
		int liLinestart = 0;
		int i = 0;

		while (i < lsBuf.length())
		{
			if (lsBuf.charAt(i) == SPACE)
			{
				liLastspace = i;
			}
			else if (lsBuf.charAt(i) == NEW_LINE)
			{
				liLastspace = -1;
				liLinestart = i + 1;
			}

			if (i > liLinestart + aiMaxLength - 1)
			{
				if (liLastspace != -1)
				{
					lsBuf.setCharAt(liLastspace, NEW_LINE);
					liLinestart = liLastspace + 1;
					liLastspace = -1;
				}
				else
				{
					lsBuf.insert(i, NEW_LINE);
					liLinestart = i + 1;
				}
			}
			i++;
		}

		return lsBuf.toString().replaceAll(
			STR_NEW_LINE,
			STR_RETURN + STR_NEW_LINE);
	}
}
