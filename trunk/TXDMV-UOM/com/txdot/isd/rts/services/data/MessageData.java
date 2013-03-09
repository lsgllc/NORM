package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * MessageData.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/02/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */

/**
 * Holds the information for each email message.
 *
 * @version	Broadcast Message	04/02/2007
 * @author	Jeff Seifert
 * <br>Creation Date:			03/27/2006 08:09:00
 */
public class MessageData implements Comparable, Serializable
{
	private RTSDate caDate = new RTSDate();
	private boolean cbHighPriority = false;
	private boolean cbNotify = false;
	private boolean cbOpened = false;
	private boolean cbReplied = false;
	private boolean cbReplyable = false;
	private String csFrom = "";
	private String csMessage = "";
	private String csMessageID = "";
	private String csSubject = "";
	private String csTo = "";

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		int liReturnValue = -1;
		MessageData laCompTo = (MessageData) aaObject;

		if (this.isOpened() == laCompTo.isOpened())
		{
			if (this.isHighPriority() == laCompTo.isHighPriority())
			{
				int liDateCompare =
					this.getDate().compareTo(laCompTo.getDate());
				// -1
				if (liDateCompare < 0)
				{
					liReturnValue = 1;
				}
				// 1
				else if (liDateCompare > 0)
				{
					liReturnValue = -1;
				}
				// 0
				else
				{
					liReturnValue = 0;
				}
			}
			else
			{
				if (this.isHighPriority())
				{
					liReturnValue = -1;
				}
				else
				{
					liReturnValue = 1;
				}
			}
		}
		else
		{
			if (this.isOpened())
			{
				liReturnValue = 1;
			}
			else
			{
				liReturnValue = -1;
			}
		}
		return liReturnValue;
	}
	
	/**
	 * Date of the email.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDate()
	{
		return caDate;
	}
	
	/**
	 * From address of the email.
	 * 
	 * @return String
	 */
	public String getFrom()
	{
		return csFrom;
	}
	
	/**
	 * Message
	 * 
	 * @return String
	 */
	public String getMessage()
	{
		return csMessage;
	}
	
	/**
	 * Message ID
	 * 
	 * @return String
	 */
	public String getMessageID()
	{
		return csMessageID;
	}
	
	/**
	 * Subject of the email.
	 * 
	 * @return String
	 */
	public String getSubject()
	{
		return csSubject;
	}
	
	/**
	 * To address of the email.
	 * 
	 * @return String
	 */
	public String getTo()
	{
		return csTo;
	}
	
	/**
	 * Priority setting of the email.  If the this is high
	 * priority then the value is true.
	 *  
	 * @return boolean
	 */
	public boolean isHighPriority()
	{
		return cbHighPriority;
	}

	/**
	 * Returns if we should notify for this message.
	 * 
	 * @return boolean
	 */
	public boolean isNotify()
	{
		return cbNotify;
	}
	
	/**
	 * Open setting.  If the email has been marked as read
	 * then this value is true.
	 * 
	 * @return boolean
	 */
	public boolean isOpened()
	{
		return cbOpened;
	}
	
	/**
	 * Returns if the message has been replied to or not.  The value
	 * should not be true if replyable is false.
	 * 
	 * @return boolean
	 */
	public boolean isReplied()
	{
		return cbReplied;
	}
	
	/**
	 * Replyable setting.  If this email can be replied to then
	 * this value is true.  This has not been implimented.
	 * 
	 * @return boolean
	 */
	public boolean isReplyable()
	{
		return cbReplyable;
	}
	
	/**
	 * Sets the date of the email.
	 * 
	 * @param aaDate RTSDate
	 */
	public void setDate(RTSDate aaDate)
	{
		caDate = aaDate;
	}
	
	/**
	 * Sets the from of the email
	 * 
	 * @param asFrom String
	 */
	public void setFrom(String asFrom)
	{
		csFrom = asFrom;
	}
	
	/**
	 * Sets the Priority status.
	 * 
	 * @param abHighPriority boolean
	 */
	public void setHighPriority(boolean abHighPriority)
	{
		cbHighPriority = abHighPriority;
	}
	
	/**
	 * Sets the message of the email.
	 * 
	 * @param asMessage String
	 */
	public void setMessage(String asMessage)
	{
		csMessage = asMessage;
	}
	
	/**
	 * Sets the message ID if the email.
	 * 
	 * @param asMessId String
	 */
	public void setMessageID(String asMessId)
	{
		csMessageID = asMessId;
	}

	/**
	 * Sets if we should notify for this message.
	 * 
	 * @param abNotify boolean
	 */
	public void setNotify(boolean abNotify)
	{
		cbNotify = abNotify;
	}
	
	/**
	 * Sets the email opened prarameter.
	 * 
	 * @param abOpened boolean
	 */
	public void setOpened(boolean abOpened)
	{
		cbOpened = abOpened;
	}

	/**
	 * Sets if the user has replied to a message.  This value would not 
	 * be set to true if replyable is false.  If this value is true then
	 * the opened value would also be true.
	 * 
	 * @param abReplied boolean
	 */
	public void setReplied(boolean abReplied)
	{
		cbReplied = abReplied;
	}
	
	/**
	 * Sets if the email is replyable.  Not implimented yet.
	 * 
	 * @param abReplyable boolean
	 */
	public void setReplyable(boolean abReplyable)
	{
		cbReplyable = abReplyable;
	}
	
	/**
	 * Sets the subject of the email.
	 * 
	 * @param asSubject String
	 */
	public void setSubject(String asSubject)
	{
		csSubject = asSubject;
	}
	
	/**
	 * Sets the too address of the email.
	 * 
	 * @param asTo String
	 */
	public void setTo(String asTo)
	{
		csTo = asTo;
	}
}
