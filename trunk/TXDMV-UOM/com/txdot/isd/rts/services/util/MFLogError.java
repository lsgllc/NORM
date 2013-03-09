package com.txdot.isd.rts.services.util;

import java.net.*;
import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.exception.*; 
/**
 * Creats the error log data required by the mainframe for logging errors
 * 
 * @date (12/13/01 1:14:30 PM)
 * @author: Marx Rajangam
 * 
 * ------------------------------------------------------------------
 * Change History:
 * Name				Date		Description
 * Marx Rajangam	4/11/02		Added javadoc, comments
 * ------------------------------------------------------------------
 */
public class MFLogError {
	/**
	 * Position to insert the buffer characters
	 */
	private final static String csINSERT_AT_END = "END";
	/**
	 * Position to insert the buffer characters
	 */
	private final static String csINSERT_AT_BEGINNING = "BEGINNING";

/**
 * MainFrameLog constructor comment.
 */
public MFLogError() {
	super();
}
/**
 * Returns the error log string used by the mainframe. Builds the
 * string from the machine and county properties. 
 * 
 * @date (12/17/01 2:56:17 PM)
 * @author: Marx Rajangam
 * 
 * ---------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * Marx Rajangam	(12/17/01 2:56:17 PM)	Created the Method
 * ---------------------------------------------------------------
 * 
 * @return java.lang.String
 */
public static String getErrorString() 
{
	char lchBufferChar = ' ';
	char lchBufferInt = '0';

	final int liMF_DATE_LENGTH = 10;
	final int liMF_TIME_LENGTH = 8; 
	final int liCOMPT_CNTY_LENGTH = 3;
	final int liWS_NAME_LENGTH = 3;
	final int liWSLU_NAME_LENGTH = 8;
	final int liPC_DATE_LENGTH = 10;
	final int liPC_TIME_LENGTH = 8;
	final int liPC_MODULE_NAME_LENGTH = 14;
	
	try
	{
		String lsErrorString = "";
		
		String lsMfDate = resizeStringtoLength("", liMF_DATE_LENGTH, lchBufferChar, csINSERT_AT_BEGINNING);
		String lsMfTime = resizeStringtoLength("", liMF_TIME_LENGTH, lchBufferChar, csINSERT_AT_BEGINNING);
		String lsComptCntyNo = resizeStringtoLength(Integer.toString(SystemProperty.getOfficeIssuanceNo()), liCOMPT_CNTY_LENGTH, lchBufferInt, csINSERT_AT_BEGINNING);
		String lsWSID = resizeStringtoLength(Integer.toString(SystemProperty.getWorkStationId()), liWS_NAME_LENGTH, lchBufferChar, csINSERT_AT_END); 
		//get the logical name here
		String lsWSLUName = resizeStringtoLength(InetAddress.getLocalHost().getHostName(), liWSLU_NAME_LENGTH, lchBufferChar, csINSERT_AT_END);
		RTSDate laRTSDate = new RTSDate(); 
		String lsPCDate = resizeStringtoLength(laRTSDate.toString(), liPC_DATE_LENGTH, lchBufferInt, csINSERT_AT_END);
		String lsPCTime = resizeStringtoLength(laRTSDate.getTime(), liPC_TIME_LENGTH, lchBufferChar, csINSERT_AT_END);
		//There is no Module Name in RTS II. Hence empty string is sent
		String lsModuleName = resizeStringtoLength("", liPC_MODULE_NAME_LENGTH, lchBufferChar, csINSERT_AT_END);
		
		lsErrorString = lsMfDate + lsMfTime + lsComptCntyNo + lsWSID + lsWSLUName + lsPCDate + lsPCTime + lsModuleName; 
		return lsErrorString; 

	} 
	catch (Exception e) 
	{
		e.printStackTrace();
		return null; 
	} 	
}
/**
 * Resizes a string to the length. Can insert <code>0</code>s or
 * spaces. Can insert at the end or at the beginning. 
 * 
 * @date (10/16/01 2:26:07 PM)
 * @author: Marx Rajangam
 * 
 * ---------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * Marx Rajangam	(10/16/01 2:26:07 PM)	Created the Method
 * ---------------------------------------------------------------
 * 
 * @return java.lang.String
 * @param lsValue java.lang.String
 * @param liLength int
 * @param lchBufferChar char
 * @param lsInsertPosition java.lang.String
 */
private static String resizeStringtoLength(String lsValue, int liLength, char lchBufferChar, String lsInsertPosition) 
{
	//create the return object
	String lsResizedString = "";

	if (lsValue == null)
		lsValue = ""; 

	StringBuffer laResizedString = new StringBuffer(lsValue); 
	int liFillLength = liLength - lsValue.length();
	boolean lbInsertAtEnd = lsInsertPosition.equals(MFLogError.csINSERT_AT_END);
	boolean lbInsertAtBeginning = lsInsertPosition.equals(MFLogError.csINSERT_AT_BEGINNING);
	int liInsertPosition = Integer.MIN_VALUE; 

	if (liFillLength < 0)
	{
		//if the actual length is more than the Length to be set, truncate at the end
		laResizedString = new StringBuffer(lsValue.substring(0, liLength)); 
	} 
	else if (lbInsertAtEnd)
	{
		liInsertPosition = lsValue.length();
		for (int i = liInsertPosition; i < liLength; i++)
		{
			laResizedString.insert(i, lchBufferChar);			
		} 
	}
	else if (lbInsertAtBeginning)
	{
		liInsertPosition = 0;
		for (int i = liInsertPosition; (i + lsValue.length() < liLength); i++)
		{
			laResizedString.insert(i, lchBufferChar);			
		} 
	}

	lsResizedString = laResizedString.toString(); 
		
	//return result
	return lsResizedString;
}
}
