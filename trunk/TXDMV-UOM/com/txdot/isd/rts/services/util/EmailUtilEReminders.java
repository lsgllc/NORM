package com.txdot.isd.rts.services.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 * EmailUtilEReminders.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name   		Date  		Description
 * ------------ ----------- --------------------------------------------
 * B. Brown		12/17/2010	Initial write for EReminder emails.
 * 							defect 10610 Ver POS_670 
 * B. Brown		03/17/2011	Add code for writing more information when
 * 							a SendFailedException occurs.
 * 							modify sendEmail()
 * 							defect 10781 Ver POS_670
 * B. Brown		10/07/2011	When From or To address is null, load them
 * 							with proper values. Also, log if the email
 * 							cannot be sent.
 * 							modify sendEmail()
 * 							defect 10922 Ver POS_690 
 * S Carlin		10/19/2011	remove "static" from csFileAttachment declration
 * 							defect 10905 Ver POS_690
 * ---------------------------------------------------------------------
 */

/**
 * Used to send emails using the javax.mail service.
 *
 * @version POS_690			10/19/2011
 * @author Bob Brown
 * <br>Creation Date:  		12/17/2010 11:15:00
 */

public class EmailUtilEReminders
{
	private static final String CONTENT_TYPE = "text/html";
	private static final String FROM_ADDRESS = SystemProperty.getEReminderEmail();
	private final String csFileSeparator = 
		System.getProperty("file.separator");
	private final String csClassPath = System.getProperty("java.class.path");
	private final String csPathSeparator = System.getProperty("path.separator");		
	private URL caImagesDir = null;
	private String csCurrentCPEntry = "";
	private static URL caDMVLogoDir = null;
	private static StringTokenizer csClassPaths; 	
	private File caDMVImage = null;			
	private static final String MAIL_SMTP = "mail.smtp.host";
	private static final String OS_NAME = "os.name";
	private static final String TEST_MAIL_GATEWAY = "WT-RTS-PAS1";
	private static final String WINDOWS = "Windows";
	private boolean EMAIL_DEBUG = false;
	private static final String DMV_LOGO = "/images/disclaimer_dmv_small.jpg";
	//defect 10905
	private String csFileAttachment = "";
	//end defect 10905

	/**
	 *  Default Constructor for this class
	 */
	public EmailUtilEReminders ()
	{
		super();
		
		if (csClassPaths==null)
		{
			csClassPaths = 
				new StringTokenizer(csClassPath, csPathSeparator);			
			try
			{
				while (csClassPaths.hasMoreTokens())
				{
					csCurrentCPEntry = csClassPaths.nextToken();
					caDMVImage = 
						new File(csCurrentCPEntry  + 
							csFileSeparator + 
							DMV_LOGO);
					System.out.println("Classpath entry = " + 
						csCurrentCPEntry);
					if (caDMVImage.canRead())
					{
						caDMVLogoDir =
							SystemProperty.class.getClassLoader().getResource(
						caDMVImage.getName());
						break;	
					}
				}
			}	
			catch (NoSuchElementException leNSEEx)
			{
				System.err.println("String tokenizer error with image class path");
				leNSEEx.printStackTrace();
			}
		}
	}
	/**
	 *  Constructor used for loading attachment
	 */	
	public EmailUtilEReminders (String asOutputFile)
	{
		csFileAttachment = asOutputFile;
	}
	/**
	 * Gets the mail.smtp.host property.  If we are on Windows then
	 * use the test mail gateway.  If we are on AIX then use the current
	 * hostname since they are a mail gateway.
	 * 
	 * @return Properties
	 */
	private Properties getMailSMTPHost()
	{
		//Set the host smtp address
		Properties laMailProps = new Properties();
		try
		{
			// When testing in development server side then use the
			// test mail gateway versus the hostname when on AIX
			if (System
				.getProperty(OS_NAME)
				.toLowerCase()
				.indexOf(WINDOWS.toLowerCase())
				> -1)
			{
				laMailProps.put(MAIL_SMTP, TEST_MAIL_GATEWAY);
			}
			else
			{
				InetAddress laInetAddress = InetAddress.getLocalHost();
				laMailProps.put(MAIL_SMTP, laInetAddress.getHostName());
			}
		}
		catch (Exception aeEx)
		{
			new RTSException(RTSException.JAVA_ERROR, aeEx);
		}

		return laMailProps;
	}

	/**
	 * This is used when the default from address is to be used.
	 * 
	 * @param asToAddress String
	 * @param asSubject String
	 * @param asMessage String
	 * @return boolean
	 */
	public boolean sendEmail(
		String asToAddress,
		String asSubject,
		String asMessage)
	{
		return sendEmail(
			FROM_ADDRESS,
			new String[] { asToAddress },
			asSubject,
			asMessage);
	}

	/**
	 * This is used when the default from address is to be used.
	 * 
	 * @param aarrToAddress String[]
	 * @param asSubject String
	 * @param asMessage String
	 * @return boolean
	 */
	public boolean sendEmail(
		String[] aarrToAddress,
		String asSubject,
		String asMessage)
	{
		return sendEmail(
			FROM_ADDRESS,
			aarrToAddress,
			asSubject,
			asMessage);
	}

	/**
	 * This is the method that actually sends the email.
	 * 
	 * @param asFromAddress String
	 * @param asToAddress String
	 * @param asSubject String
	 * @param asMessage String
	 * @return boolean
	 */
	public boolean sendEmail(
		String asFromAddress,
		String[] asToAddress,
		String asSubject,
		String asMessage)
	{
		boolean lbReturn = false;
		int liI = 0;

		try
		{
			// Create a session using the hostname of the mail gateway.
			Session laMailSession =
				Session.getDefaultInstance(getMailSMTPHost(), null);
			// Debus is defaulted to false.
			laMailSession.setDebug(EMAIL_DEBUG);
			// create a mime message
			Message laMailMSG = new MimeMessage(laMailSession);

			InternetAddress laAddressFrom =
				new InternetAddress(asFromAddress);
			laMailMSG.setFrom(laAddressFrom);
					
			String lsToAddress = "";
			int liNumUniqueEmails = 0; 
			for (liI = 0; liI < asToAddress.length; liI++)
			{
				// null signals the end of unique email list
				if (asToAddress[liI] == null)											 
				{
					break;
				}
				else
				{
					liNumUniqueEmails = liNumUniqueEmails + 1;
				}
			}			
			// Set to address
			Address[] larrAddress =
				new Address[liNumUniqueEmails];
			for (int i = 0; i < liNumUniqueEmails; i++)
			{
				lsToAddress = asToAddress[i];
				InternetAddress laAddressTo =
					new InternetAddress(lsToAddress);
				larrAddress[i] = laAddressTo;	
			}

			laMailMSG.setRecipients(
				Message.RecipientType.TO,
				larrAddress);

			// Set the Subject
			laMailMSG.setSubject(asSubject);
			
//			MimeMultipart multipart = new MimeMultipart("related");
//	
//				// first part  (the html)
//				BodyPart messageBodyPart = new MimeBodyPart();
//				String htmlText = "<center><img src=\"cid:image\"></center>" + asMessage;
//				messageBodyPart.setContent(htmlText, "text/html");
//
//				multipart.addBodyPart(messageBodyPart);
//				if (caDMVLogoDir==null)
//				{
//					caDMVLogoDir =
//						SystemProperty.class.getClassLoader().getResource(DMV_LOGO);
//					if (caDMVLogoDir==null)
//					{		
//						BatchLog.write("DMV image not found for EReminder email");
//						laMailMSG.setContent(asMessage, CONTENT_TYPE);
//					}						
//				}
//				if (caDMVLogoDir!=null)
//				{			
//				// second part (the image)
//					messageBodyPart = new MimeBodyPart();
//					DataSource fds = new FileDataSource (caDMVLogoDir.getPath().toString());
//					messageBodyPart.setDataHandler(new DataHandler(fds));
//					messageBodyPart.setHeader("Content-ID","<image>");
//		
//					// add it
//					multipart.addBodyPart(messageBodyPart);
//					// put everything together
//					laMailMSG.setContent(multipart);	
//				}
//			}
//			else 
			if(!csFileAttachment.equals(""))
			{
				MimeBodyPart messageBodyPart = 
					  new MimeBodyPart();
				//fill message
				messageBodyPart.setText(asMessage);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = 
				  new FileDataSource(csFileAttachment);
				messageBodyPart.setDataHandler(
				  new DataHandler(source));
				messageBodyPart.setFileName(csFileAttachment);
				multipart.addBodyPart(messageBodyPart);

				// Put parts in message
				laMailMSG.setContent(multipart);				
			}	
			else
			{
				// Set the Body
				laMailMSG.setContent(asMessage, CONTENT_TYPE);
			}
			// Send the actual email
			Transport.send(laMailMSG);

			// Everything was sent so return true.
			lbReturn = true;
		}
		catch (SendFailedException leSFE)
		{
			// defect 10781
			//BatchLog.write("Message not sent to email: " + asToAddress);
			leSFE.printStackTrace();
			BatchLog.write("SendFailedException message = " + leSFE.getMessage());
			for (liI = 0; liI < asToAddress.length; liI++)
			{
				String lsToAddress = asToAddress[liI];
				BatchLog.write("Message not sent to email address: " + lsToAddress);
			}
			// end defect 10781
		}
		catch (NoClassDefFoundError aeNCDFE)
		{
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			// Instantiation will write to the log.
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		return lbReturn;
	}
	/**
		  * This is the method that actually sends the email.
		  * 
		  * @param asFromAddress String
		  * @param asToAddress String
		  * @param asSubject String
		  * @param asMessage String
		  * @return boolean
		  */
	public boolean sendEmail(
		String asFromAddress,
		String asToAddress,
		String asSubject,
		String asMessage)
		throws Exception
	{
		boolean lbReturn = false;

		try
		{
			// Create a session using the hostname of the mail gateway.
			Session laMailSession =
				Session.getDefaultInstance(getMailSMTPHost(), null);
			// Debus is defaulted to false.
			laMailSession.setDebug(EMAIL_DEBUG);
			// create a mime message
			Message laMailMSG = new MimeMessage(laMailSession);
			// defect 10922	
			// we were getting javax.mail.internet.InternetAddress.parse()
			// NullPointerExceptions, possibly indicating the "from" or "to" address
			// is null for some reason at this point in the code
			// Make sure they are not null when this codes executes.
			if (asFromAddress==null)
			{
				asFromAddress = SystemProperty.getEReminderEmail();
			}
			if (asToAddress==null)
			{
				asToAddress = "TSD-RTS-POS@txdmv.gov";
			}
			// end defect 10922
			// Set from address
			InternetAddress laAddressFrom =
				new InternetAddress(asFromAddress);
			laMailMSG.setFrom(laAddressFrom);

			// Set to address
			InternetAddress laAddressTo =
				new InternetAddress(asToAddress);
			Address[] larrAddress = new Address[1];
			larrAddress[0] = laAddressTo;
			laMailMSG.setRecipients(
				Message.RecipientType.TO,
				larrAddress);

			// Set the Subject
			laMailMSG.setSubject(asSubject);

			// Set the Body
			laMailMSG.setContent(asMessage, CONTENT_TYPE);

			// Send the actual email
			Transport.send(laMailMSG);

			// Everything was sent so return true.
			lbReturn = true;
		}
		// Added because if the mail.jar is not there we get this
		// error and it is not handled by the Exception catch.  This
		// will help us diagnose setup issues.  Log and then return 
		// false.
		catch (NoClassDefFoundError aeNCDFE)
		{
			// Instantiation will write to the log.
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		// Handles all other exceptions.  Log and then return false.
		catch (SendFailedException leSFE)
		{
			// defect 10922
			leSFE.printStackTrace();
			BatchLog.write(" This SendFailedException: " + leSFE.getMessage() + 
					       " occurred while trying to send email from: " +  asFromAddress + 
					       " to: " + asToAddress + " with subject: " + asSubject + 
					       " and message: " + asMessage);
			Exception laEx = new Exception(" This SendFailedException: " + leSFE.getMessage() + 
				       " occurred while trying to send email from: " +  asFromAddress + 
				       " to: " + asToAddress + " with subject: " + asSubject + 
				       " and message: " + asMessage);
			throw laEx;
			// end defect 10922
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			// Instantiation will write to the log.
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			// defect 10922
			BatchLog.write(" This Exception: " + aeEx.getMessage() + 
				           " occurred while trying to send email from: " +  asFromAddress + 
				           " to: " + asToAddress + " with subject: " + asSubject + 
				           " and message: " + asMessage);
			// end defect 10922
			throw aeEx;
		}
		return lbReturn;
	}
}
