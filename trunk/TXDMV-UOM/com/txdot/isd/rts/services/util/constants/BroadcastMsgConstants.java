package com.txdot.isd.rts.services.util.constants;

/*
 * BroadcastMsgConstants.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/06/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */

/**
 * This class holds all constants for the message event under help.
 * 
 * @version	Broadcast Message	04/06/2007
 * @author	Jeff Seifert
 * <br>Creation Date:			04/06/2007 07:51:00 
 */

public class BroadcastMsgConstants
{
	// Functions
	public static final int DEL_MESSAGE = 8;
	public static final int GET_MESSAGES = 7;
	public static final int GET_UNREAD_MESSAGES = 11;
	public static final int MARK_MESS_READ = 9;
	public static final int PRINT_MESSAGE = 10;
	public static final int PURGE_EXPIRED = 12;
	public static final int REPLY_TO_MESSAGE = 13;
	public static final int SND_MESSAGE = 14;
	
	// String Constants
	public static final String SEND_ICON = "/images/send.gif";
	public static final String SEND_TIP = "Send Message";
	public static final String SUB_LABEL = "Subject:";
	public static final String TO_LABEL = "To:";
	public static final String FROM_LABEL = "From:";
}
