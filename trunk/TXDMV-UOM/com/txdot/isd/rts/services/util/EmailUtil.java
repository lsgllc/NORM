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
 * EmailUtil.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name   		Date  		Description
 * ------------ ----------- --------------------------------------------
 * Jeff S.  	02/06/2007  Created to replace the GroupWiseMail class.
 *        					defect 8796 Ver 5.3.1
 * B. Brown		09/27/2007	Changed the content type to text/html so
 * 							the email body can contain underlining,
 * 							bolding, etc	 
 * 							modify CONTENT_TYPE
 * 							defect 9119 Ver Special Plates
 * B. Brown		12/10/2009	Throw exception in sendEmail (four parms) so
 * 							the calling code can report the detailed
 * 							issue in it's log.
 * 							defect 10262 Ver Defect_POS_H
 * B. Brown		08/03/2010	Add E-Reminder code
 * 							add EREMINDER_FROM_ADDRESS 
 * 							add constructors
 * 							modify sendEmail() 
 * 							defect 10512 Ver POS_650
 * B. Brown		08/11/2010	Change the way the code looks for the dmv
 * 							logo sent with email.
 * 							modify sendEmail() 
 * 							defect 10512 Ver POS_650 
 * B. Brown		10/08/2010	Change name of eReminder variable and refer
 * 							to SystemProperty eReminder value.
 * 							modify EmailUtil(), sendEmail()
 * 							defect 10614 Ver POS_660
 * B. Brown		12/28/2010	Move eReminder code to EmailUtilEReminder 
 * 							class.
 * 							modify sendEmail()
 * 							defect 10610 Ver POS_670 
 * ---------------------------------------------------------------------
 */

/**
 * Used to send emails using the javax.mail service.
 *
 * @version POS_670			12/28/2010
 * @author Jeff Seifert
 * <br>Creation Date:  		01/30/2007 10:30:00
 */

public class EmailUtil
{
	// This can be changed if html type of email needs to be used
	// defect 9119
	// private static final String CONTENT_TYPE = "text/plain";
	private static final String CONTENT_TYPE = "text/html";
	// end defect 9119
	private static final String FROM_ADDRESS = "RTS-IVTRS@txdmv.gov";
	// defect 10512
	// defect 10614
//	private static final String EREMINDER_FROM_ADDRESS =
//		SystemProperty.getEReminderFromEmail();
	private static final String EREMINDER_ADDRESS =
		SystemProperty.getEReminderEmail();
	private final String csFileSeparator = 
		System.getProperty("file.separator");
	private final String csClassPath = System.getProperty("java.class.path");
	private final String csPathSeparator = System.getProperty("path.separator");
		
	private URL caImagesDir = null;
	private String csCurrentCPEntry = "";
	private static URL caDMVLogoDir = null;
	private static StringTokenizer csClassPaths; 	
	private File caDMVImage = null;			
	// end defect 10614	
	// end defect 10512
	private static final String MAIL_SMTP = "mail.smtp.host";
	private static final String OS_NAME = "os.name";
	private static final String TEST_MAIL_GATEWAY = "WT-RTS-PAS1";
	private static final String WINDOWS = "Windows";
	private boolean EMAIL_DEBUG = false;
	
	// defect 10512
	private static final String DMV_LOGO = "/images/disclaimer_dmv_small.jpg";
	private static String csFileAttachment = "";
	//private static String ssClasspath = "";
	// end defect 10512
	/**
	 *  Default Constructor for this class
	 */
	public EmailUtil ()
	{
		super();
		
		// defect 10614
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
		// end defect 10614
	}
	/**
	 *  Constructor used for loading attachment
	 */	
	public EmailUtil (String asOutputFile)
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
		//return sendEmail(FROM_ADDRESS, asToAddress, asSubject, asMessage);

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

			// Set from address
			// defect 10610
			// move the below code to EmailUtilEReminder class
			// defect 10512
//			if (asSubject.indexOf("eReminder") > -1
//				|| asSubject.indexOf("Bounceback") > -1)
//			{
//				// defect 10614
////				asFromAddress = EREMINDER_FROM_ADDRESS;
//				asFromAddress = EREMINDER_ADDRESS;
//				// end defect 10614
//			}
			// end defect 10512
			// end defect 10610
			
//			BatchLog.write(
//				"Before InternetAddress from address instantiation: asFromAddress = "
//					+ asFromAddress);
//			BatchLog.write(
//				"Before InternetAddress from address instantiation: asToAddress = "
//					+ asToAddress[0]);
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
			
			// defect 10512
			// defect 10610
//			if (asSubject.indexOf("eReminder") > -1)
//			{	
//				MimeMultipart multipart = new MimeMultipart("related");
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
			// end defect 10610
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
			// end defect 10512
			// Send the actual email
			Transport.send(laMailMSG);

			// Everything was sent so return true.
			lbReturn = true;
		}
		// Added because if the mail.jar is not there we get this
		// error and it is not handled by the Exception catch.  This
		// will help us diagnose setup issues.  Log and then return 
		// false.
		// defect 10512
		catch (SendFailedException leSFE)
		{
			BatchLog.write("Message not sent to email: " + asToAddress);
		}
		// end defect 10512
		catch (NoClassDefFoundError aeNCDFE)
		{
			// Instantiation will write to the log.
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNCDFE);
		}
		// Handles all other exceptions.  Log and then return false.
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
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			// Instantiation will write to the log.
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			// defect 10262	
			throw aeEx;
			// end defect 10262
		}
		return lbReturn;
	}

}