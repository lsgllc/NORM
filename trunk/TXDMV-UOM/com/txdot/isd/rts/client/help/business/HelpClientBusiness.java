package com.txdot.isd.rts.client.help.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.MessageData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.help.GenMessageReport;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.BroadcastMsgConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * HelpClientBusiness.java
 * 
 * (c) Texas Department of Transportation  2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/03/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * K Harrell	06/19/2009	UtilityMethods.saveReport(), 
 * 							 Print.getDefaultPageProps() now static
 * 							modify printMessage()
 * 							defect 10023 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/** 
 * Help client business layer.
 *
 * <p>Client side processing for handling messages and other functions
 * of the Help menu.
 *
 * @version Defect_POS_F	06/19/2009
 * @author  Jeff Seifert
 * <br>Creation Date: 		03/21/2006 14:15:00 
 */

public class HelpClientBusiness
{
	// This can be changed if html type of email needs to be used
	private static final String content_Type = "text/plain";
	private static final String COULD_NOT_CONNECT =
		"Could not connect to SMTP host:";
	private static final String DATE = "Date";
	private static final String DEL_DATE = "Del-Date";
	private static final String DELAY_DATETIME = "Delay-DateTime";
	private static final String DO_NOT_REPLY = "DoNotReply";
	private static final String EML = ".eml";
	private static final String EXPIRE_DATE = "Expire-Date";
	private static final String FROM = "From";
	private static final String MAIL_SMTP = "mail.smtp.host";
	private static final String MESSAGE_FROM = "Message sent from ";
	private static final String MSG_FILENAME = "message";
	private static final String MSG_SERVER_DOWN =
		"The Message server is not avaliable at this time.\n"
			+ "Try again later.";
	private static final String NOTIFY = "Notify";
	private static final String OPEN_EML = "open.eml";
	private static final String PRIORITY = "X-Priority";
	private static final String PRIORITY_HIGH = "High";
	private static final String PRIORITY_LOW = "Low";
	private static final String REPLIED = "Replied";
	private static final String REPLYABLE = "Replyable";
	private static final String SERVER_DOWN = "Message Server Down!";
	private static final String SUBJECT = "Subject";
	private static final String TO = "To";
	private static final String TRUE = "true";

	// Used to build an RTSDate from a date in an .eml file
	static private Hashtable shtMonthsTxt;
	static {
		shtMonthsTxt = new Hashtable();
		shtMonthsTxt.put("Jan", "1");
		shtMonthsTxt.put("Feb", "2");
		shtMonthsTxt.put("Mar", "3");
		shtMonthsTxt.put("Apr", "4");
		shtMonthsTxt.put("May", "5");
		shtMonthsTxt.put("Jun", "6");
		shtMonthsTxt.put("Jul", "7");
		shtMonthsTxt.put("Aug", "8");
		shtMonthsTxt.put("Sep", "9");
		shtMonthsTxt.put("Oct", "10");
		shtMonthsTxt.put("Nov", "11");
		shtMonthsTxt.put("Dec", "12");
	}

	/**
	 * MessagesClientBusiness default constructor
	 */
	public HelpClientBusiness()
	{
		super();
	}
	
	/**
	 * Build an RTSDate object from the date string in the email.
	 * 8 Mar 2006 08:25:43 -0600
	 * 
	 * @param asDate
	 */
	private RTSDate buildRTSDate(String asDate)
	{
		String[] lsValue = asDate.split(CommonConstant.STR_SPACE_ONE);

		int liDay = Integer.parseInt(lsValue[0]);
		int liMonth =
			Integer.parseInt(
				String.valueOf(shtMonthsTxt.get(lsValue[1])));
		int liYear = Integer.parseInt(lsValue[2]);
		String lsTime = lsValue[3];
		RTSDate laDate = new RTSDate();
		laDate.set(liYear, liMonth, liDay);

		laDate.setTime(
			lsTime.replaceAll(
				CommonConstant.STR_COLON,
				CommonConstant.STR_SPACE_EMPTY));
		laDate.setMillisecond(0);

		return laDate;
	}
	
	/**
	 * Delete the messages that matches the message ID.
	 * 
	 * @param aaData - Message ID (String)
	 * @return Object - Vector of MessageData
	 * @throws RTSException
	 */
	private Object deleteMessage(Object aaData) throws RTSException
	{
		// Get the message that matches the message ID that is passed.
		File[] larrMessages =
			new File(SystemProperty.getMailDir()).listFiles(
				new CustomFileNameFilter(String.valueOf(aaData)));
		// We know that since the message ID is unique we will only
		// get one file object back or none
		if (larrMessages != null && larrMessages.length > 0)
		{
			// Delete the message.
			larrMessages[0].delete();
		}

		// Return the new list of messages sorted
		return getAllMessages();
	}
	
	/**
	 * Gets all of the messages from the mail drop folder.  Retutrn the
	 * messages in a sorted vector of MessageData objects.
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	private Vector getAllMessages() throws RTSException
	{
		Vector lvReturnMes = new Vector();
		try
		{
			// Get all .eml files in the Mail Directory
			File[] larrMessages =
				new File(SystemProperty.getMailDir()).listFiles(
					new CustomFileNameFilter(EML));
			if (larrMessages != null)
			{
				for (int i = 0; i < larrMessages.length; i++)
				{
					MessageData laMess = new MessageData();
					String lsFileName = larrMessages[i].getName();
					if (lsFileName.indexOf(OPEN_EML) > -1)
					{
						laMess.setOpened(true);
						laMess.setMessageID(
							lsFileName.substring(
								0,
								lsFileName.indexOf(OPEN_EML)));
					}
					else if (lsFileName.indexOf(EML) > -1)
					{
						laMess.setOpened(false);
						laMess.setMessageID(
							lsFileName.substring(
								0,
								lsFileName.indexOf(EML)));
					}
					FileInputStream laIn =
						new FileInputStream(larrMessages[i]);
					Message laMessage = new MimeMessage(null, laIn);
					Enumeration laHeaders = laMessage.getAllHeaders();
					RTSDate laDelayDateTime = null;

					while (laHeaders.hasMoreElements())
					{
						Header laHeader =
							(Header) laHeaders.nextElement();

						if (laHeader
							.getName()
							.equalsIgnoreCase(DELAY_DATETIME))
						{

							laDelayDateTime =
								new RTSDate(laHeader.getValue());
						}
						else if (
							laHeader.getName().equalsIgnoreCase(NOTIFY)
								&& laHeader.getValue().equalsIgnoreCase(
									TRUE))
						{
							laMess.setNotify(true);
						}
						else if (
							laHeader.getName().equalsIgnoreCase(
								PRIORITY)
								&& laHeader.getValue().equalsIgnoreCase(
									PRIORITY_HIGH))
						{
							laMess.setHighPriority(true);
						}
						else if (
							laHeader.getName().equalsIgnoreCase(FROM))
						{
							laMess.setFrom(laHeader.getValue());
						}
						else if (
							laHeader.getName().equalsIgnoreCase(
								SUBJECT))
						{
							laMess.setSubject(laHeader.getValue());
						}
						else if (
							laHeader.getName().equalsIgnoreCase(TO))
						{
							laMess.setTo(laHeader.getValue());
						}
						else if (
							laHeader.getName().equalsIgnoreCase(DATE))
						{
							laMess.setDate(
								buildRTSDate(laHeader.getValue()));
						}
						else if (
							laHeader.getName().equalsIgnoreCase(
								REPLYABLE)
								&& laHeader.getValue().equalsIgnoreCase(
									TRUE))
						{
							laMess.setReplyable(true);
						}
						else if (
							laHeader.getName().equalsIgnoreCase(
								REPLIED)
								&& laHeader.getValue().equalsIgnoreCase(
									TRUE))
						{
							laMess.setReplied(true);
						}
					}

					laMess.setMessage(
						laMessage.getContent().toString());
					if (laDelayDateTime == null
						|| laDelayDateTime.compareTo(
							RTSDate.getCurrentDate())
							<= 0)
					{
						lvReturnMes.add(laMess);
					}

					laIn.close();
				}
			}
		}
		// Added because if the mail.jar is not there we get this
		// error and it is not handled by the Exception catch
		catch (NoClassDefFoundError aeNCDFE)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		// Sort the list of messages - see compareto() in the 
		// MessageData class to see sort order
		UtilityMethods.sort(lvReturnMes);

		return lvReturnMes;
	}

	/**
	 * Get All messages that are not opened.  This is used to get the
	 * new message count.  The messages are sorted by all unread 
	 * messages first so when you get to a read message we can break.
	 * 
	 * @return Vector
	 */
	private Vector getUnreadMessages() throws RTSException
	{
		Vector lvAllMessages = getAllMessages();
		Vector lvReturnValue = new Vector();
		for (int i = 0; i < lvAllMessages.size(); i++)
		{
			MessageData laMessage = (MessageData) lvAllMessages.get(i);
			if (!laMessage.isOpened())
			{
				lvReturnValue.add(laMessage);
			}
			else
			{
				// Since they are already sorted by unread, priority, 
				// date we know that there are no more unread messages.
				break;
			}
		}
		return lvReturnValue;
	}
	
	/**
	 * Mark a message as read.  To mark a message as read we rename the
	 * .eml file to messageId.eml to messageIdopen.eml.
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object markMessageAsRead(Object aaData) throws RTSException
	{
		// Get just the message with the message ID passed in the 
		// filename
		File[] larrMessages =
			new File(SystemProperty.getMailDir()).listFiles(
				new CustomFileNameFilter(String.valueOf(aaData)));
		// We know that since the message ID is unique we will only
		// get one file object back or none
		if (larrMessages != null && larrMessages.length > 0)
		{
			// Only mark a file as read if it hasn't already been
			// marked
			if (larrMessages[0].getName().indexOf(OPEN_EML) == -1)
			{
				renameToOpen(larrMessages[0]);
			}
		}

		// Return the new list of messages sorted
		return getAllMessages();
	}
	
	/**
	 * Mark a message as replied and opened.  The replied parameter is
	 * set in the message header.
	 * 
	 * @param asMessageId String
	 * @return Object
	 * @throws RTSException
	 */
	private Object markMessageReplied(String asMessageId)
		throws RTSException
	{
		try
		{
			// Get just the message with the message ID passed in the 
			// filename
			File[] larrMessages =
				new File(SystemProperty.getMailDir()).listFiles(
					new CustomFileNameFilter(asMessageId));
			// We know that since the message ID is unique we will only
			// get one file object back or none
			if (larrMessages != null && larrMessages.length > 0)
			{
				FileInputStream laIn =
					new FileInputStream(larrMessages[0]);
				Message laMessage = new MimeMessage(null, laIn);
				// Only mark a message as replied if the header does
				// not already exists.
				if (laMessage.getHeader(REPLIED) == null)
				{
					laMessage.addHeader(REPLIED, TRUE);
					laMessage.saveChanges();
					FileOutputStream lpfsOut =
						new FileOutputStream(larrMessages[0]);
					laMessage.writeTo(lpfsOut);
					lpfsOut.flush();
					lpfsOut.close();
				}
				laIn.close();
			}

			// Mark the message a read and return all of the messages
			return markMessageAsRead(asMessageId);
		}
		// Added because if the mail.jar is not there we get this
		// error and it is not handled by the Exception catch
		catch (NoClassDefFoundError aeNCDFE)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
	}
	
	/**
	 * Formats the message and sends it to the printer.
	 * The formated message is deleted after it is printed.
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object printMessage(Object aaData) throws RTSException
	{
		if (aaData != null && aaData instanceof MessageData)
		{
			GenMessageReport laMessageRPT = new GenMessageReport();
			ReportSearchData laRptSearchData = new ReportSearchData();
			laRptSearchData.setKey1(
				laMessageRPT.formatReport((MessageData) aaData)
					+ ReportConstant.FF);
			laRptSearchData.setKey2(MSG_FILENAME);
			laRptSearchData.setKey3(CommonConstant.STR_SPACE_EMPTY);
			laRptSearchData.setIntKey1(1);
			
			// defect 10023 
			//Print laPrint = new Print();
			String lsPageProps = Print.getDefaultPageProps();
			String lsRpt =
				lsPageProps
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ laRptSearchData.getKey1();
					
			//UtilityMethods laUtil = new UtilityMethods();
			String lsFileName =
			UtilityMethods.saveReport(
					lsRpt,
					laRptSearchData.getKey2(),
					laRptSearchData.getIntKey1());
			
			if (lsFileName != null)
			{
				Print laPrint = new Print();
				laPrint.sendToPrinter(
					lsFileName,
					laRptSearchData.getKey2());
				new File(lsFileName).delete();
			}
			// end defect 10023 
		}
		// Nothing has changed so there is no reason to return the
		// message vector.
		return null;
	}
	
	/**
	 * Process data method to direct requests to client layer or on 
	 * to server layer.
	 * 
	 * @return Object
	 * @param aiModule String Module name
	 * @param aiFunctionId int Function id of the method
	 * @param aaData Object data object
	 * @throws RTSException
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case BroadcastMsgConstants.GET_MESSAGES :
				{
					return getAllMessages();
				}
			case BroadcastMsgConstants.GET_UNREAD_MESSAGES :
				{
					return getUnreadMessages();
				}
			case BroadcastMsgConstants.DEL_MESSAGE :
				{
					return deleteMessage(aaData);
				}
			case BroadcastMsgConstants.MARK_MESS_READ :
				{
					return markMessageAsRead(aaData);
				}
			case BroadcastMsgConstants.PRINT_MESSAGE :
				{
					return printMessage(aaData);
				}
			case BroadcastMsgConstants.PURGE_EXPIRED :
				{
					return purgeExpiredMessages();
				}
			case BroadcastMsgConstants.SND_MESSAGE :
				{
					return replyToMessage(aaData);
				}
			default :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
		}
	}
	
	/**
	 * Used to purge any expired messages.  A message is considered
	 * expired and either two actions are taken.  The message is deleted
	 * or the message is marked as read.  There are two properties 
	 * in the message header that determine the expiration.
	 * 
	 * Expire-Date - date that the message will be automaticlly marked
	 * as read. (if not allready marked)
	 * Del-Date - date that the message will be deleted from the system.
	 * 
	 * Note: date is sent in the format below.
	 * YYYYMMDD
	 * 
	 * If the message's Expire-Date is before or the same as the 
	 * Del-Date then the message is just deleted and never automaticlly
	 * marked as read.
	 * 
	 * When a message is marked as read the user will not be prompted 
	 * to read that message again.
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	private Vector purgeExpiredMessages() throws RTSException
	{
		try
		{
			RTSDate laDateNow = new RTSDate();
			RTSDate laDateExpire = null;
			RTSDate laDateDel = null;

			// Get all .eml files in the Mail Directory
			File[] larrMessages =
				new File(SystemProperty.getMailDir()).listFiles(
					new CustomFileNameFilter(EML));
			if (larrMessages != null)
			{
				for (int i = 0; i < larrMessages.length; i++)
				{
					FileInputStream laIn =
						new FileInputStream(larrMessages[i]);
					Message laMessage = new MimeMessage(null, laIn);
					Enumeration laHeaders = laMessage.getAllHeaders();
					while (laHeaders.hasMoreElements())
					{
						Header laHeader =
							(Header) laHeaders.nextElement();
						// get the Expire-Date
						if (laHeader
							.getName()
							.equalsIgnoreCase(EXPIRE_DATE)
							&& !laHeader.getValue().equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							laDateExpire =
								new RTSDate(
									RTSDate.YYYYMMDD,
									Integer.parseInt(
										laHeader.getValue()));
							laDateExpire.getTime();
						}
						// get the Del-Date
						else if (
							laHeader.getName().equalsIgnoreCase(
								DEL_DATE)
								&& !laHeader.getValue().equals(
									CommonConstant.STR_SPACE_EMPTY))
						{
							laDateDel =
								new RTSDate(
									RTSDate.YYYYMMDD,
									Integer.parseInt(
										laHeader.getValue()));
							laDateDel.getTime();
						}
					}
					// close the file so we can delete or rename
					laIn.close();

					// delete messages that have a del-date that is
					// equal or less than todays date
					if (laDateDel != null
						&& laDateDel.compareTo(laDateNow) <= 0)
					{
						larrMessages[i].delete();
					}
					// If not deleting then check to see of it expired
					// only rename files that are not already marked as
					// open.
					else if (
						larrMessages[i].getName().indexOf(OPEN_EML)
							== -1
							&& laDateExpire != null
							&& laDateExpire.compareTo(laDateNow) <= 0)
					{
						renameToOpen(larrMessages[i]);
					}
				}
			}
		}
		// Added because if the mail.jar is not there we get this
		// error and it is not handled by the Exception catch
		catch (NoClassDefFoundError aeNCDFE)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		finally
		{
			// No need to return anything but we are forced to return
			return null;
		}
	}
	
	/**
	 * Renames the file passed to messageIDopen.eml.
	 * 
	 * @param aaFile File
	 */
	private void renameToOpen(File aaFile)
	{
		String lsNewFileName =
			aaFile.getAbsolutePath().substring(
				0,
				aaFile.getAbsolutePath().length() - 4)
				+ OPEN_EML;
		aaFile.renameTo(new File(lsNewFileName));
	}
	
	/**
	 * This is the method that actually sends the reply.  The 
	 * MessageData object that is passed contains all of the 
	 * information to be placed in the message that will be sent.  The 
	 * message object is the original message that has been altered for 
	 * the reply.  The from address will become the to address and visa 
	 * versa.
	 * 
	 * After the message has been sent the message header will be 
	 * marked so that you can tell that the message has been replied.
	 * The message will also be marked as opened.
	 * 
	 * @param aaMessage Object
	 * @return Object
	 */
	private Object replyToMessage(Object aaMessage) throws RTSException
	{
		MessageData laMailMess = (MessageData) aaMessage;
		try
		{
			//Set the host smtp address
			Properties laMailProps = new Properties();
			laMailProps.put(MAIL_SMTP, SystemProperty.getMailGateway());
			Session laMailSession =
				Session.getDefaultInstance(laMailProps, null);
			laMailSession.setDebug(false);
			// create a message
			Message laMailMSG = new MimeMessage(laMailSession);
			if (laMailMess.isHighPriority())
			{
				laMailMSG.setHeader(PRIORITY, PRIORITY_HIGH);
			}
			else
			{
				laMailMSG.setHeader(PRIORITY, PRIORITY_LOW);
			}

			// set the from and to address
			InternetAddress laAddressFrom =
				new InternetAddress(DO_NOT_REPLY);
			laMailMSG.setFrom(laAddressFrom);
			InternetAddress laAddressTo =
				new InternetAddress(laMailMess.getFrom());
			Address[] larrAddress = new Address[1];
			larrAddress[0] = laAddressTo;
			laMailMSG.setRecipients(
				Message.RecipientType.TO,
				larrAddress);
			laMailMSG.setSubject(
				MESSAGE_FROM
					+ laMailMess.getTo()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_DASH
					+ CommonConstant.STR_SPACE_ONE
					+ laMailMess.getSubject());
			laMailMSG.setContent(laMailMess.getMessage(), content_Type);

			// Send the actual email
			Transport.send(laMailMSG);

			return markMessageReplied(laMailMess.getMessageID());
		}
		// Added because if the mail.jar is not there we get this
		// error and it is not handled by the Exception catch
		catch (NoClassDefFoundError aeNCDFE)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		catch (MessagingException aeMEx)
		{
			if (aeMEx.getMessage().indexOf(COULD_NOT_CONNECT) > -1)
			{
				RTSException laRTSEx =
					new RTSException(RTSException.JAVA_ERROR, aeMEx);
				laRTSEx.setMessage(MSG_SERVER_DOWN);
				laRTSEx.setTitle(SERVER_DOWN);
				throw laRTSEx;
			}
			else
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeMEx);
			}
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
	}
}