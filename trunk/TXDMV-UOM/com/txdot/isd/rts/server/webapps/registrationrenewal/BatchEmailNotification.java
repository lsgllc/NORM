package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetTransaction;
import com.txdot.isd.rts.server.webapps.util.BatchLog;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.EmailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.EmailUtil;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

/*
 * BatchEmailNotification.java
 *
 * (c) Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford 	09/03/2002	DB down handling.
 *                          defect 3700  
 * Clifford 	09/23/2002	REG 103 Escape
 *  						defect 4681	              
 * Clifford		10/08/2002	Insurance not required
 *							PCR
 * B Brown      01/22/2003  Add do not respond string to all emials sent
 *                          for Approved, Held,and DeclinedRefund failed
 *                          transactions.
 *                          add graph String csDoNotRespond
 *							modify getMessage(EmailData) 
 * B Brown      03/11/2003  allow standalone running of this class.
 * 							modify main
 * B Brown      11/18/2003  Use variable csGroupWiseHost. Change its 
 *							value to webmail-old.
 * 							modify sendEmail
 *                          defect 6699.
 * B Brown      12/13/2004  Modify graph variable csGroupWiseHost
 *                          back to "webmail".
 *                          defect 7698 Ver 5.2.2.
 * S Johnston	02/24/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify getSendMailList(), updateEmailIndi()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3
 * K Harrell	10/31/2005	Moving Internet Classes into Phase 1 
 * 							packages.  Additional DB Access work 
 * 							defect 7889 Ver 5.2.3
 * Jeff S.		02/06/2007	Change the sending email process from using
 * 							GroupWise to using javax.mail. Need to come
 * 							back and remove the refrences to groupwise.
 * 							The methods have been deprecated.
 * 							deprecate setGroupWiseHost()
 * 							add MESSAGE_SENT_LOG, PLATE_NO_LOG
 * 							modify sendEmail(), sendMail()
 * 							defect 8796 Ver 5.3.1
 * ---------------------------------------------------------------------
 */
 
/**  
 * This class is responsible for getting the list of emails that need to
 * be sent (SendEmailIndi=1), and calling the EmailUtil class to 
 * actually send the emails, then updating the SendEmailIndi column to 0
 * so the email does not get sent again.
 *
 * @version 5.3.1			02/06/2007
 * @author	James Giroux
 * <br>Creation Date:		10/09/2001 12:34:17
 */

public class BatchEmailNotification
{
	// defect 8796
	static final String MESSAGE_SENT_LOG = "Message was sent to: ";
	static final String PLATE_NO_LOG = " - PlateNo: ";
	// end defect 8796
	
	// defect 7698 - back to webmail
	String csGroupWiseHost = "webmail";
	// defect 6699
	//String csGroupWiseHost = "webmail-old";
	// end defect 6699
	// end defect 7698
	String csSubject = "RE: Vehicle Registration Renewal";
	static final String csMessageHeader =
		"The vehicle registration renewal you submitted for "
			+ "\nplate number ";
	static final String csApprovedMessage =
		" has been approved. If you do not receive your renewal "
			+ "sticker and registration (RTS) receipt within 7 - 10 "
			+ "business days, please contact your County Tax Assessors"
			+ "-Collector's Office.";
	static final String csHeldMessage =
		" is being held pending further information. "
			+ "No action on your part is required at this time. If you "
			+ "do not receive a request for additional information "
			+ "within 2-3 business days, you may contact your County "
			+ "Tax Assessor-Collector's Office.";
	static final String csDeclinedMessage_1 =
		"We regret to inform you that we are unable to renew your "
			+ "vehicle registration for plate ";
	static final String csDeclinedMessage_2 =
		" at this time. Your Credit Card Account has been credited. "
			+ "Please contact your County Tax Assessor-Collector's "
			+ "Office for further assistance.";

	static final String csDoNotRespond =
		"\nThis email should not be replied to, as the sending "
			+ "mailbox is not designed to receive emails.";

	/**
	 * BatchEmailNotification constructor
	 */
	public BatchEmailNotification()
	{
		super();
	}
	
	/**
	 * Compose the email message based an email data object.
	 * 
	 * @param aaEmailData EmailData
	 * @return String 
	 */
	private String getMessage(EmailData aaEmailData)
	{
		String lsMessage = CommonConstant.STR_SPACE_EMPTY;
		
		switch (aaEmailData.getCntyStatusCd())
		{
			case CommonConstants.APPROVED :
				{
					lsMessage =
						csMessageHeader
							+ aaEmailData.getPlateNo()
							+ csApprovedMessage
							+ csDoNotRespond;
					break;
				}
			case CommonConstants.HOLD :
				{
					lsMessage =
						csMessageHeader
							+ aaEmailData.getPlateNo()
							+ csHeldMessage
							+ csDoNotRespond;
					break;
				}
			case CommonConstants.DECLINED_REFUND_PENDING :
				{
					// empty code block
				}
			case CommonConstants.DECLINED_REFUND_APPROVED :
				{
					// empty code block
				}
			case CommonConstants.DECLINED_REFUND_FAILED :
				{
					lsMessage += csDeclinedMessage_1
						+ aaEmailData.getPlateNo()
						+ csDeclinedMessage_2
						+ csDoNotRespond;
				}
		}
		return lsMessage;
	}
	
	/**
	 * Retrieve the list of internet registration renewal records which
	 * require sending the email. The retrieved is a vector of EmailData 
	 * Object, whose fields are a subset of a internet registration 
	 * renewal record.
	 * 
	 * @return Vector
	 */
	public Vector getSendMailList()
	{
		Vector lvMailList = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		// defect 7889 
		// Add Rollback Logic 
		try
		{
			laDBA.beginTransaction();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBA);
			lvMailList = laItrntTrans.qryRenewalEmail();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (Throwable aeT)
		{
			BatchLog.error(aeT);
			try
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx)
			{
			}
		}
		// end defect 7889 
		return lvMailList;
	}
	
	/** 
	 * Send out internet registration renewal email notification.
	 * aarrArgs[0] is used for smtp host
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		EmailData laEmailData = new EmailData();
		laEmailData.setEmail("rlbrown@dot.state.tx.us");
		laEmailData.setDocNo("11112222");
		laEmailData.setPlateNo("L3308");
		laEmailData.setVin("3333");
		laEmailData.setRenewalDt("06/03/2002");
		laEmailData.setCntyStatusCd(8);

		BatchEmailNotification laBatchEmail =
			new BatchEmailNotification();

		try
		{
			// lBatchEmail.sendEmail(lEmailData);
			// System.out.println("An Email Message Sent");
			Comm.setIsServer(true);
			//lBatchEmail.sendEmail(lEmailData);
			laBatchEmail.sendMail();
		}
		catch (Exception aeEx)
		{
			// empty code block
		}
	}
	
	/**
	 * Send out an email message
	 * The EmailData Object contains the fields for sending the email. 
	 * 
	 * @param aaEmailData EmailData  
	 * @return boolean
	 */
	private boolean sendEmail(EmailData aaEmailData)
	{
		// defect 8796
		// Changed to javax.mail instead of using groupwise. 
		boolean lbResult = false;
		//GroupWiseMail laGWM = new GroupWiseMail();
		//// defect 6699 
		//// Changed the value of csGroupWiseHost
		//// used in 4 places in this method
		//laGWM.establishConnection(csGroupWiseHost);
		//if (laGWM.login(csGroupWiseHost, "RTS-IVTRS", "strsrtvi"))
		//{
		//	lbResult =
		//		laGWM.sendMail(
		//			csGroupWiseHost,
		//			aaEmailData.getEmail(),
		//			csSubject,
		//			getMessage(aaEmailData));
		//}
		//laGWM.logout(csGroupWiseHost);

		EmailUtil laEmailService = new EmailUtil();
		lbResult = laEmailService.sendEmail(
			aaEmailData.getEmail(),
			csSubject,
			getMessage(aaEmailData));
			
		return lbResult;
		// end defect 8796
	}
	
	/**
	 * Send out internet registration renewal email notification
	 * and update the record
	 * 
	 * @return boolean
	 */
	public boolean sendMail()
	{
		EmailData laEmailData = null;
		Vector lvMailList = getSendMailList();
		if (lvMailList == null)
		{
			BatchLog.warning(
				"Error retrieving the mailing list from"
					+ " RTS.RTS_ITRNT_TRANS table");
			return false;
		}
		// defect 7889
		// Remove lbSuccessful as was set but never used 
		for (int i = 0; i < lvMailList.size(); ++i)
		{
			laEmailData = (EmailData) lvMailList.elementAt(i);
			boolean lbEmailSent = sendEmail(laEmailData);
			if (lbEmailSent)
			{
				// only if succeed, do update the indicator
				try
				{
					// defect 8796
					// This should go to the same log that 
					// GroupWiseMail.send() was logging to.
					RTSDate laDate = RTSDate.getCurrentDate();
					String lsSpaceDash =
						CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE;
					System.out.println(
						laDate
							+ lsSpaceDash
							+ laDate.getClockTime()
							+ lsSpaceDash
							+ MESSAGE_SENT_LOG
							+ laEmailData.getEmail()
							+ PLATE_NO_LOG
							+ laEmailData.getPlateNo());
					// end defect 8796
					updateEmailIndi(laEmailData);
				}
				catch (Exception aeEx)
				{
					// Cannot update the database after sending out the
					// email.  Write to batch log.
					BatchLog.error(
						"Error updating email Indicator"
							+ " after sending the email to: "
							+ laEmailData.getEmail());
					BatchLog.error(aeEx);
				}
			}
			else
			{
				BatchLog.info(
					"Error sending email to: "
						+ laEmailData.getEmail());
			}
		}
		// end defect 7889 
		return true;
	}
	
	/**
	 * Set the group wise email server.
	 * asGroupWiseHost is the new STMP email server 
	 * 
	 * @param asGroupWiseHost String 
	 * @deprecated 
	 */
	public void setGroupWiseHost(String asGroupWiseHost)
	{
		csGroupWiseHost = asGroupWiseHost;
	}
	
	/**
	 * Update the the SendEmailIndi field of the internet
	 * registration renewal record after
	 * 
	 * The passed parameter is a EmailData object containing the fields
	 * for updating the SendEmailIndi field of a internet registration 
	 * renewal record.
	 * 
	 * @param aaEmailData EmailData
	 */
	private void updateEmailIndi(EmailData aaEmailData)
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		// defect 7889 
		// Reorganize for Commit/Rollback 
		try
		{
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBA);
			laDBA.beginTransaction();
			laItrntTrans.updRenewalEmailIndi(aaEmailData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (Throwable aeT)
		{
			BatchLog.error(aeT);
			try
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeEx)
			{
				// empty code block
			}
		}
		// end defect 7889 
	}
}