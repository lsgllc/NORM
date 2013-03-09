package com.txdot.isd.rts.server.cris;

import java.util.Date;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.txdot.isd.rts.services.util.RTSDate;
/*
 * CrisServlet.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob Brown	06/18/2004	Initial writing.
 *                          5.2.0. 
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * CrisServlet is the point of entry for DPS request for MF Vehicle info
 *
 * The client is required to pass in license plate number, username, and 
 * password, via a HEAD request.
 * CrisMfAccess.getMFRec is used to accesss the MF.
 * The servlet will then return a MF data in the response header.
 * 
 * @version	5.2.3		05/02/2005
 * @author	Bob Brown
 * <br>Creation Date:	06/18/2004 16:40:00
 */
public class CrisServlet extends HttpServlet
{

	/**
	 * Process incoming HTTP GET requests 
	 * 
	 * @param aaHttpServletRequest Object that encapsulates the request
	 *  to the servlet 
	 * @param aaHttpServletResponse Object that encapsulates the response
	 *  from the servlet
	 */

	public void doGet(
		HttpServletRequest aaHttpServletRequest,
		HttpServletResponse aaHttpServletResponse)
		throws javax.servlet.ServletException, java.io.IOException
	{
		performTask(aaHttpServletRequest, aaHttpServletResponse);
	}
	/**
	 * Process incoming HTTP POST requests 
	 * 
	 * @param aaHttpServletRequest Object that encapsulates the request 
	 * to the servlet 
	 * @param aaHttpServletResponse Object that encapsulates the response
	 *  from the servlet
	 */

	public void doPost(
		HttpServletRequest aaHttpServletRequest,
		HttpServletResponse aaHttpServletResponse)
		throws javax.servlet.ServletException, java.io.IOException
	{
		performTask(aaHttpServletRequest, aaHttpServletResponse);
	}
	/**
	 * Returns the servlet info string.
	 * 
	 * @return String
	 */

	public String getServletInfo()
	{
		return super.getServletInfo();
	}
	/**
	 * Initializes the servlet.
	 */

	public void init()
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);

	}
	/**
	 * Process incoming requests for information
	 * <p> 
	 * @param aaHttpServletRequest Object that encapsulates the request 
	 *  to the servlet 
	 * @param aaHttpServletResponse Object that encapsulates the response
	 *  from the servlet
	 */
	public void performTask(
		HttpServletRequest aaHttpServletRequest,
		HttpServletResponse aaHttpServletResponse)
	{
		String lsPlateNo = null;

		try
		{
			/*
			String lsPlateNo = aHttpServletRequest.getParameter("REG_PLATE_NO").toUpperCase();
			String lsUserName = aHttpServletRequest.getParameter("USERNAME");
			String lsPassword = aHttpServletRequest.getParameter("PASSWORD");
			*/
			lsPlateNo =
				aaHttpServletRequest
					.getHeader("REG_PLATE_NO")
					.toUpperCase();
			String lsUserName =
				aaHttpServletRequest.getHeader("USERNAME");
			String lsPassword =
				aaHttpServletRequest.getHeader("PASSWORD");

			//Enumeration e = aHttpServletRequest.getAttributeNames();

			String lsData = lsUserName + lsPassword;
			//lsData=lsData.toUpperCase();

			// get info about request
			String lsHostname = aaHttpServletRequest.getRemoteHost();
			// if no host name found, set it to none
			if (lsHostname == null || lsHostname.length() == 0)
			{
				lsHostname = "NONE";
			}
			if (lsHostname.substring(0, 6).equals("10.50."))
			{
				// if this is a 10.50. rts address, chop off the "10.50."
				lsHostname = lsHostname.substring(6);
			}

			LogData laLogData = new LogData();

			// get the remote user
			laLogData.setUserId(aaHttpServletRequest.getRemoteUser());
			laLogData.setLogType(
				"CrisServlet performTask - informational");
			laLogData.setLogDate(Logger.getTimeStamp());

			Logger.writeToLog("=======================");
			Logger.writeToLog(new Date().toString());

			Logger.writeToLog(
				"Request received from "
					+ aaHttpServletRequest.getRemoteAddr());

			//for(Enumeration e = aHttpServletRequest.getHeaderNames(); e.hasMoreElements(); )
			Enumeration laEnum = aaHttpServletRequest.getHeaderNames();

			while (laEnum.hasMoreElements())
			{
				String lsHeaderName = (String) laEnum.nextElement();
				if (lsHeaderName == null
					|| lsHeaderName.equals("password"))
				{
					continue;
				}
				else
				{
					Logger.writeToLog(
						lsHeaderName
							+ ": "
							+ aaHttpServletRequest.getHeader(
								lsHeaderName));
				}

			}

			Logger.writeToLog("end of logging for this request");

			//Logger.writeToLog(logData);

			CrisMfAccess laCrisMFA = new CrisMfAccess();

			String lsReturnString = laCrisMFA.getMFRec(lsPlateNo, lsData);

			String lsBrowserReturnString = new String();

			//for (int x=0;x<=10;x++)
			//{

			String lsNextToken = new String();
			String laLabel = new String();
			String lsValue = new String();
			StringTokenizer lsStBR = null;
			StringTokenizer laStEquals = null;

			try
			{
				lsStBR = new StringTokenizer(lsReturnString, "#");
				//aHttpServletResponse.setContentType("text/html");

				while (lsStBR.hasMoreTokens())
				{
					lsNextToken = lsStBR.nextToken();
					laStEquals = new StringTokenizer(lsNextToken, "*");
					while (laStEquals.hasMoreTokens())
					{
						laLabel = laStEquals.nextToken();
						//label = stEquals.nextToken()+Integer.toString(x);
						lsValue = laStEquals.nextToken();
						aaHttpServletResponse.addHeader(laLabel, lsValue);
						// lsBrowserReturnString for testing
						//lsBrowserReturnString+=label+"="+value+"<br>";
					}
				}

			}

			catch (NoSuchElementException leNSEEx)
			{
				System.err.println(
					"CrisServlet error "
						+ " "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime()
						+ "Plate no = "
						+ lsPlateNo
						+ "Token = "
						+ laStEquals
						+ "==============");
				leNSEEx.printStackTrace();
			}

			//}

			//PrintWriter out = aHttpServletResponse.getWriter();	
			//out.println(lsBrowserReturnString);
			System.out.println("finished with cris servlet response");

			//OutputStream os = aHttpServletResponse.getOutputStream();
			//	os.write(postParams.getBytes());

		}

		catch (Throwable leEx)
		{
			System.err.println(
				"CrisServlet error "
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime()
					+ " Plate no = "
					+ lsPlateNo
					+ "==============");
			leEx.printStackTrace();
		}

	}
}
