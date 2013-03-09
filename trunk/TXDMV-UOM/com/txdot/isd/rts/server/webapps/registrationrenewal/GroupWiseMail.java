package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.StringTokenizer;

import com.txdot.isd.rts.server.webapps.util.BatchLog;

/*
 *
 * GroupWiseMail.java 
 *
 * (c) Texas Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown      03/11/2003  Modify login,sendMail
 *                          Include better logging:
 *                          i.e. catch (Throwable t)
 *                          BatchLog.error(t);
 *							defect 5768
 * B Brown      03/27/2003  Modify login, sendMail
 *							to recapture the catch
 *                          exception while leaving the catch throwable.
 *							defect 5768 
 * B Brown      12/13/2004  add graph variables USER_CONTEXT,
 *							USER_CONTEXT_START,USER_CONTEXT_LENGTH
 * 							modify establishConnection,
 *							modify USER_CONTEXT_LENGTH 
 *                          to 18 characters for the most 
 *                          recent upgrade of Groupwise webmail.
 *							Groupwise admins also had to turn off 
 *							cookies for WebAccess (TXDOT-HQ66) utilized
 *                          by this RTS webmail interface.
 *                          defect 7698 Ver 5.2.2.
 * S Johnston	02/25/2005	Code Cleanup for Java 1.4.2 upgrade
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Ray Rowehl	11/01/2005	Update main to send to rrowehl instead of 
 * 							rjhicks1.
 * 							modify main
 * 							defect 7889 Ver 5.2.3
 * Jeff S.		02/06/2007	Now using javax.mail instead of Groupwise.
 * 							deprecate GroupWiseMail
 * 							defect 8796 Ver 5.3.1
 * ---------------------------------------------------------------------
 */
 
/** 
 * This class is responsible for establishing a connection, logging into
 * and sending emails to Groupwise,.
 *
 * @version 5.3.1			02/06/2007
 * @author: Richard Hicks
 * <br>Creation date: 		08/06/2002 10:54:59
 * 
 * @deprecated
 */

public class GroupWiseMail
{
	private String csContext = null;
	protected final static String LANGUAGE = "en";
	protected final static String GW_SERVLET = "/servlet/webacc";
	protected final static String SERVLET_SEPARATOR = "?";
	protected final static String PARM_SEPARATOR = "&";
	protected final static String URL_START = "http://";
	//  Parameter used to establish connection
	protected final static String PARM_LANGUAGE = "User.lang=en";
	//  Context returned from GroupWise server upon connection
	protected final static String PARM_CONTEXT = "User.context=";
	//  Parameters to login to the GroupWise server
	protected final static String PARM_USERID = "User.id=";
	protected final static String PARM_PASSWORD = "User.password=";
	protected final static String PARM_INTERFACE =
		"User.interface=frames";
	protected final static String PARM_ERROR = "error=login";
	protected final static String PARM_LOGIN_MERGE = "merge=webacc";
	protected final static String PARM_LOGIN_ACTION =
		"action=User.Login";
	protected final static String PARM_JAVASCRIPT =
		"Url.hasJavaScript=1";
	//  Parameters to logout to the GroupWise server
	protected final static String PARM_LOGOUT_MERGE = "merge=login";
	protected final static String PARM_LOGOUT_ACTION =
		"action=User.Logout";
	//  Parameters to send an e-mail message from GroupWise
	protected final static String PARM_SEND_ACTION =
		"action=Compose.Action";
	protected final static String PARM_COMPOSE_ID = "Compose.id=1";
	protected final static String PARM_SEND_MERGE = "merge=send";
	protected final static String PARM_URL_ENCLOSURE_TYPE =
		"Url.Enclosure.type=";
	protected final static String PARM_URL_COMPOSING =
		"Url.composing=1";
	protected final static String PARM_ITEM_TYPE = "Item.type=Mail";
	protected final static String PARM_ITEM_FOLDER_ID =
		"Item.Folder.id=";
	protected final static String PARM_ITEM_COMPOSE_METHOD =
		"Item.Compose.method=";
	protected final static String PARM_ITEM_TO = "Item.to=";
	protected final static String PARM_ITEM_CC = "Item.cc=";
	protected final static String PARM_ITEM_BC = "Item.bc=";
	protected final static String PARM_ITEM_SUBJECT = "Item.subject=";
	protected final static String PARM_ITEM_MESSAGE = "Item.message=";
	// defect 7698
	protected final static String USER_CONTEXT = "User.context";
	protected final static int USER_CONTEXT_START = 21;
	protected final static int USER_CONTEXT_LENGTH = 18;
	// end defect 7698
	/**
	 * GroupWiseMail constructor
	 */
	public GroupWiseMail()
	{
		super();
	}
	/**
	 * This method creates the groupwise URL and uses it to
	 * establishes a conection to Groupwise.
	 *
	 * @param asServer String
	 */
	public void establishConnection(String asServer)
	{
		// Create the URL to establish a connection to the GroupWise server
		String lsURL = URL_START + asServer + GW_SERVLET
			+ SERVLET_SEPARATOR	+ PARM_LANGUAGE;
		try
		{
			String lsHtml = sendHTTPRequest(lsURL);
			// defect 7698 
			// the User.context parameter grew to 18 characters 
			// from 12 for the most recent Groupwise webmail upgrade.
			// From 6.0.3 (webmail-old) to 6.5.2 (webmail).
			//if (html.indexOf(USER_CONTEXT) < 0)
			//{
			//}
			//else
			if (lsHtml.indexOf(USER_CONTEXT) >= 0)
			{
				int liIndex = lsHtml.indexOf(USER_CONTEXT);
				csContext = lsHtml.substring(liIndex
						+ USER_CONTEXT_START, liIndex 
						+ USER_CONTEXT_START
						+ USER_CONTEXT_LENGTH);
				// end defect 7698
			}
		}
		catch (Exception leEx)
		{
			// empty code block
		}
	}
	/**
	 * login
	 * 
	 * @param asServer String
	 * @param asUserid String
	 * @param asPassword String
	 * @return boolean
	 */
	public boolean login(String asServer,
		String asUserid, String asPassword)
	{
		boolean lbResult = false;
		String lsURL = 
				URL_START + asServer + GW_SERVLET + SERVLET_SEPARATOR
				+ PARM_USERID + asUserid + PARM_SEPARATOR
				+ PARM_PASSWORD	+ asPassword + PARM_SEPARATOR
				+ PARM_CONTEXT + csContext + PARM_SEPARATOR
				+ PARM_INTERFACE + PARM_SEPARATOR + PARM_ERROR
				+ PARM_SEPARATOR + PARM_LOGIN_MERGE + PARM_SEPARATOR
				+ PARM_LOGIN_ACTION	+ PARM_SEPARATOR + PARM_JAVASCRIPT;
		try
		{
			String lsHTML = sendHTTPRequest(lsURL);
			// System.out.println(html);
			if (lsHTML.indexOf("Please login again.") < 0)
			{
				lbResult = true;
			}
			else
			{
				BatchLog.warning(
					"Error logging in to the GroupWise mail server.");
				lbResult = false;
			}
		}
		catch (Exception leEx)
		{
			BatchLog.warning(
				"Error logging in to the GroupWise mail server : "
					+ leEx.getMessage());
			lbResult = false;
		}
		catch (Throwable leT)
		{
			BatchLog.error(leT);
			lbResult = false;
		}
		return lbResult;
	}
	/**
	 * logout
	 * 
	 * @param asServer String
	 */
	public void logout(String asServer)
	{
		String lsURL = URL_START + asServer + GW_SERVLET 
			+ SERVLET_SEPARATOR + PARM_CONTEXT + csContext 
			+ PARM_SEPARATOR + PARM_LOGOUT_MERGE + PARM_SEPARATOR
			+ PARM_LOGOUT_ACTION + PARM_SEPARATOR + PARM_LANGUAGE;
		try
		{
			String lsHTML = sendHTTPRequest(lsURL);
		}
		catch (Exception leEx)
		{
			// empty code block
		}
	}
	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		GroupWiseMail lsGWM = new GroupWiseMail();
		lsGWM.establishConnection("webmail");
		if (lsGWM.login("webmail", "RTS-IVTRS", "strsrtvi"))
		{
			for (int i = 0; i < 3; i++)
			{
				lsGWM.sendMail("webmail", "rrowehl@yahoo.com",
					"RE: Testing from Java" + i, 
					"This is a test from Java.");
			}
		}
		lsGWM.logout("webmail");
	}
	/**
	 * replaceSpaces
	 * Creation date: (8/27/02 1:49:27 PM)
	 * 
	 * @param asInString String
	 * @return String
	 */
	private String replaceSpaces(String asInString)
	{
		StringBuffer laResult = new StringBuffer();
		StringTokenizer laST = new StringTokenizer(asInString);
		while (laST.hasMoreTokens())
		{
			laResult.append(laST.nextToken() + "%20");
		}
		return laResult.toString();
	}
	/**
	 * sendHTTPRequest
	 * Creation date: (8/6/02 11:07:10 AM)
	 * 
	 * @param asURL String
	 * @return String
	 */
	public String sendHTTPRequest(String asURL) throws Exception
	{
		String lsResult = null;
		try
		{
			StringBuffer laStringBufferInp = new StringBuffer();
			URL laURL = new URL(asURL);
			HttpURLConnection laURLConnection =
				(HttpURLConnection) laURL.openConnection();
			laURLConnection.setRequestMethod("POST");
			laURLConnection.setAllowUserInteraction(false);
			laURLConnection.setDoOutput(true);
			PrintWriter laOut =
				new PrintWriter(laURLConnection.getOutputStream());
			laOut.flush();
			laOut.close();
			BufferedReader laBufferedReader =
				new BufferedReader(new InputStreamReader(
					laURLConnection.getInputStream()));
			String lsRes;
			while (true)
			{
				lsRes = laBufferedReader.readLine();
				if (lsRes == null)
				{
					break;
				}
				if (lsRes.length() == 0)
				{
					continue;
				}
				laStringBufferInp.append(lsRes);
			}
			// If buffered reader did not return any data, that means
			// there was some problem with server like out of memory
			// error, class not found etc.
			lsResult = laStringBufferInp.toString();
		}
		catch (MalformedURLException leEx)
		{
			throw leEx;
		}
		catch (SocketException leEx)
		{
			throw leEx;
		}
		catch (IOException leEx)
		{
			throw leEx;
		}
		return lsResult;
	}
	/**
	 * sendMail
	 * 
	 * @param asServer String
	 * @param asAddress String
	 * @param asSubject String
	 * @param asMessage String
	 * @return boolean
	 */
	public boolean sendMail(String asServer, String asAddress,
		String asSubject, String asMessage)
	{
		boolean lbResult = false;
		String lsURL = URL_START + asServer + GW_SERVLET
			+ SERVLET_SEPARATOR + PARM_CONTEXT + csContext
			+ PARM_SEPARATOR + PARM_SEND_ACTION + PARM_SEPARATOR
			+ PARM_SEND_MERGE;
		try
		{
			String lsHTML = sendHTTPRequest(lsURL);
			// System.out.println(html);
			if (lsHTML.indexOf("Novell WebAccess Compose Message") < 0)
			{
				lbResult = false;
			}
			else
			{
				asSubject = replaceSpaces(asSubject);
				asMessage = replaceSpaces(asMessage);
				lsURL = URL_START + asServer + GW_SERVLET
					+ SERVLET_SEPARATOR	+ PARM_CONTEXT + csContext
					+ PARM_SEPARATOR + PARM_SEND_ACTION + PARM_SEPARATOR
					+ PARM_COMPOSE_ID + PARM_SEPARATOR + PARM_SEND_MERGE
					+ PARM_SEPARATOR + PARM_URL_ENCLOSURE_TYPE
					+ PARM_SEPARATOR + PARM_URL_COMPOSING
					+ PARM_SEPARATOR + PARM_ITEM_TYPE + PARM_SEPARATOR
					+ PARM_ITEM_FOLDER_ID + PARM_SEPARATOR
					+ PARM_ITEM_COMPOSE_METHOD + PARM_SEPARATOR
					+ PARM_ITEM_TO + asAddress + PARM_SEPARATOR
					+ PARM_ITEM_CC + PARM_SEPARATOR	+ PARM_ITEM_BC
					+ PARM_SEPARATOR + PARM_ITEM_SUBJECT + asSubject
					+ PARM_SEPARATOR + PARM_ITEM_MESSAGE + asMessage
					+ PARM_SEPARATOR + "Compose.Send=Send";
				// System.out.println(url);		
				lsHTML = sendHTTPRequest(lsURL);
				// System.out.println(html);
				if (lsHTML.indexOf("Mail Message was sent") < 0)
				{
					lbResult = false;
				}
				else
				{
					System.out.println("Message was sent.");
					lbResult = true;
				}
			}
		}
		catch (Exception leEx)
		{
			BatchLog.warning("Error sending email : "
				+ leEx.getMessage());
			lbResult = false;
		}
		catch (Throwable leT)
		{
			BatchLog.error(leT);
			lbResult = false;
		}
		return lbResult;
	}
}