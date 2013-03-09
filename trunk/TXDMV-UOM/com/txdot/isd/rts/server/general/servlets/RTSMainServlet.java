
package com.txdot.isd.rts.server.general.servlets;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

import com.txdot.isd.rts.server.accounting.AccountingServerBusiness;
import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.funds.FundsServerBusiness;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.server.localoptions.LocalOptionsServerBusiness;
import com.txdot.isd.rts.server.misc.MiscServerBusiness;
import com.txdot.isd.rts.server.miscreg.MiscRegServerBusiness;
import com.txdot.isd.rts.server.registration.RegistrationServerBusiness;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.specialplates
	.SpecialPlatesServerBusiness;
import com.txdot.isd.rts.server.title.TitleServerBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.addresschange
	.AddrChgServerBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.registrationrenewal
	.RegRenProcessingServerBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.registrationrenewal
	.RegistrationRenewalServerBusiness;

/*
 * RTSMainServlet.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Changed to Hungarian notation
 * 							Added RTSException Propagation
 * RS Chandel	11/27/2001	Case statement added for phase II
 * N Ting		04/16/2002	Catch EOF exception and make it a server 
 * 							down exception
 * 							defect 3520
 * R Hicks      04/16/2002  Made changes to performTask method to call
 * 							endTransaction when beginTransaction fails
 * 							defect 4800
 * K Harrell    02/19/2003  Write using SQL_EXCP vs. APPLICATION on 
 * 							Logging Exceptions
 *                          defect 5259
 * Ray Rowehl	02/20/2003	capture host name
 *							defect 4588
 * Ray Rowehl	03/05/2003	print timestamp before stacktrace
 * 							defect 5263
 * Ray Rowehl	05/15/2003 	Shorten then host name if it of 10.50 type.
 * 							for HT errors.
 * Ray Rowehl	07/21/2003	return server name as part of rtsexceptions
 *							modify performTask()
 *							defect 6238   5.1.4
 * Ray Rowehl	09/17/2003	Make sure RTSException.DetailMsg is 
 *							initialized before appending to it.
 *							modify performTask()
 *							defect 6574  5.1.5
 * Ray Rowehl	09/17/2003	When catching NullPointer, throw it as an 
 * 							RTSException Server Down.  
 * 							This forces sendcache to wake up.
 *							modify performTask()
 *							defect 6575  5.1.5
 * Ray Rowehl	03/28/2005	RTS 5.2.3 Code Cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter for loadCache()
 * 							modify init()
 * 							remove sbCacheloaded  (not used) 
 * 							defect 7864 Ver 5.2.3 
 * Ray Rowehl	05/05/2005	Modify to write the caught 
 * 							NullPointerExceptions to the log.  
 * 							SD Exceptions do not get written to the log.
 * 							Also add some try catches around some data
 * 							areas to ensure they are not causing the 
 * 							NullPointers.  
 * 							Work on adding client host name to 
 * 							messages.
 * 							Migrated from 5.2.2 to 5.2.3.
 * 							formatted code, rename fields.
 * 							add INT10, INT13, WS_NONE
 * 							modify processTask()
 * 							defect 8186 Ver 5.2.2 Fix 4
 * Ray Rowehl	05/11/2005	Use the first RTSException catch to control 
 * 							writeToLog for SD.
 * 							Migrated from 5.2.2 to 5.2.3
 * 							modify performTask()
 * 							defect 8186 Ver 5.2.2 Fix 4
 * Ray Rowehl	07/15/2005	Add some additional logging to try to 
 * 							detect what the Marble Falls problem is.
 * 							add captureHttpParams()
 * 							modify performTask()
 * 							defect 8299 Ver 5.2.2 Fix 5
 * Ray Rowehl	03/31/2006	Print out the Java system properties on init
 * 							modify init()
 * 							defect 8553 Ver 5.2.3
 * Ray Rowehl	07/12/2006	Pass host name into LocalOptionsServer.
 * 							Add braces on switch - case.
 * 							organize imports.
 * 							modify performTask()
 * 							defect 8849 Ver 5.2.3
 * Ray Rowehl	08/02/2006	Pass host name to SystemControlBusiness.
 * 							modify performTask()
 * 							defect 8869 Ver 5.2.4
 * K Harrell	02/17/2007	Modified for Special Plates
 * 							modify performTask()
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	07/27/2007	Modify the IP filter to handle VTR machines.
 * 							add DEFAULT_RTS_WS_IP_PREFIX_VTR
 * 							modify performTask()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	11/27/2007	Add Log4j processing for passing thru to 
 * 							MfAccess for VehInquiry.
 * 							add saLog4jRTSMainServlet
 * 							modify init()
 * 							defect 9441 Ver ?
 * K Harrell	10/13/2008	Modified for Disabled Placard Processing
 * 							modify performTask()
 * 							defect 9831 Ver Defect_POS_B 
 * Ray Rowehl	03/27/2012	Change up the ip address parsing now that 
 * 							county and DMV machines both use 10.*.
 * 							modify performTask()
 * 							defect 11320 Ver POS_700
 * ---------------------------------------------------------------------
 */

/**
 * RTS Main Servlet is the point of entry for all client side interaction
 * to the server side object.
 *
 *<p> The client is required to pass in the module name, function id, as
 * well as the object in serialized used by the server side.  The servlet
 * will then convert the object from serialized string back to java 
 * object and call the appropriate business server side layer.
 *
 * @version	POS_700			03/23/2012 
 * @author	Nancy Ting
 * @since 08/08/2001 08:41:52
 */

public class RTSMainServlet extends HttpServlet
{
	// Use constants in this function to make use more clear.
	private static final String CLIENT_STR = "Client ";
	private static final String DATA = "Data";
	// defect 11320
	//private static final String DEFAULT_RTS_WS_IP_PREFIX_COUNTY =
	//	"10.";
	//private static final String DEFAULT_RTS_WS_IP_PREFIX_VTR = "144.";
	// end defect 11320
	private static final String ERR_TXT_CLIENT = " client ";
	private static final String ERR_TXT_DATA_CONVERSION =
		" Data conversion had a problem!";
	private static final String ERR_TXT_DATA_NULL = " - Data is null";
	private static final String ERR_TXT_DATA_SIZE = "Data size ";

	private static final String ERR_TXT_MSG_NULL = "Msg was null";
	private static final String ERR_TXT_NO_SUCH_MODULE =
		"RTSMainServlet - No such module name:";
	private static final String ERR_TXT_FUNCTION =
		" - Function Id has a problem";
	private static final String ERR_TXT_MODULE =
		" - Module Name has a problem";
	private static final String ERR_TXT_SERVLET =
		"RTSMainServlet error ";
	private static final String ERR_TXT_SERVER = "Server    ";
	private static final String ERR_TXT_WEBAPP = "WebApp:name";
	private static final String FUNCTION_ID = "FunctionId";
	private static final String LINE_SEPARATOR = "line.separator";
	private static final String MODULE_NAME = "ModuleName";
	private static final String MSG_ERR_SERVLET =
		"----- Problem with loading cache in servlet for server "
			+ "side ------";
	private static final String SPACE_DASH_SPACE = " - ";
	private static final String WS_NONE = "NONE";

	private static final int INT10 = 10;
	private static final int INT13 = 13;

	// defect 9441
	public static org.apache.log4j.Logger saLog4jRTSMainServlet;
	// end defect 9441

	/**
	 * Capture HTTP Parameters and add them to the exception 
	 * detail message.
	 * 
	 * @param aaHttpServletRequest HttpServletRequest
	 * @param aeRTSEx RTSException
	 */
	private void captureHttpParams(
		HttpServletRequest aaHttpServletRequest,
		RTSException aeRTSEx)
	{
		aeRTSEx.setDetailMsg(
			aeRTSEx.getDetailMsg()
				+ System.getProperty(LINE_SEPARATOR));
		Enumeration laParamNames =
			aaHttpServletRequest.getParameterNames();
		while (laParamNames.hasMoreElements())
		{
			String lsParamName = (String) laParamNames.nextElement();
			aeRTSEx.setDetailMsg(
				aeRTSEx.getDetailMsg()
					+ System.getProperty(LINE_SEPARATOR)
					+ lsParamName
					+ SPACE_DASH_SPACE
					+ aaHttpServletRequest.getParameter(lsParamName));
		}

		aeRTSEx.setDetailMsg(
			aeRTSEx.getDetailMsg()
				+ System.getProperty(LINE_SEPARATOR));
		Enumeration laHeaderNames =
			aaHttpServletRequest.getHeaderNames();
		while (laHeaderNames.hasMoreElements())
		{
			String lsHeaderName = (String) laHeaderNames.nextElement();
			aeRTSEx.setDetailMsg(
				aeRTSEx.getDetailMsg()
					+ System.getProperty(LINE_SEPARATOR)
					+ lsHeaderName
					+ SPACE_DASH_SPACE
					+ aaHttpServletRequest.getHeader(lsHeaderName));
		}

		aeRTSEx.setDetailMsg(
			aeRTSEx.getDetailMsg()
				+ System.getProperty(LINE_SEPARATOR));
	}
	/**
	 * Process incoming HTTP GET requests 
	 * 
	 * @param aaHttpServletRequest Object that encapsulates the request 
	 * to the servlet 
	 * @param aaHttpServletResponse Object that encapsulates the response
	 * from the servlet
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
	 * @param aaHttpServletResponse Object that encapsulates the 
	 * response from the servlet
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
		// Populate the cache.
		try
		{
			com.txdot.isd.rts.services.communication.Comm.setIsServer(
				true);

			// defect 9441
			// Call SystemProperty to force log4j environment init
			com.txdot.isd.rts.services.util.SystemProperty.initialize();

			// initialize the log4j log for this class
			saLog4jRTSMainServlet = Logger.getLogger("RTSMainServlet");

			if (saLog4jRTSMainServlet == null)
			{
				saLog4jRTSMainServlet = Logger.getRootLogger();
				saLog4jRTSMainServlet.info(
					"Did not find config file entry for RTSMainServlet");
			}
			// end defect 9441

			// defect 11320
			// Add some new status messages so we know the version and 
			// datasource.
			System.out.println("Running " + SystemProperty.getVersionNo() + " Created " + SystemProperty.getVersionDate());
			System.out.println("Using datasource " + SystemProperty.getDatasource());
			// end defect 11320
			//Setting properties on server
			CacheManagerServerBusiness laCMSB =
				new CacheManagerServerBusiness();
			// defect 7864
			// remove parameter 
			laCMSB.loadStaticCache();
			// end defect 7864

			// defect 8553
			// print out the system properties
			System.out.println(System.getProperties());
			// end defect 8553  
		}
		catch (Exception leEx)
		{
			System.out.println(MSG_ERR_SERVLET);
			System.err.println(
				ERR_TXT_SERVLET
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).getYYYYMMDDDate()
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).get24HrTime());
			leEx.printStackTrace();
		}
	}
	/**
	 * Process incoming requests for information.
	 * 
	 * <p>Based on Module Name and Function Id, the destinated server 
	 * side business class is called.  Subsequently, the response is 
	 * returned back to the client using HttpServletResponse.
	 *
	 * @param aaHttpServletRequest HttpServletRequest 
	 * @param aaHttpServletResponse HttpServletResponse 
	 */
	public void performTask(
		HttpServletRequest aaHttpServletRequest,
		HttpServletResponse aaHttpServletResponse)
	{
		try
		{
			MDC.put("RemoteAddr", aaHttpServletRequest.getRemoteAddr());
			MDC.put("RemoteHost", aaHttpServletRequest.getRemoteHost());
			MDC.put(
				MODULE_NAME,
				aaHttpServletRequest.getParameter(MODULE_NAME));
			MDC.put(
				FUNCTION_ID,
				aaHttpServletRequest.getParameter(FUNCTION_ID));
			saLog4jRTSMainServlet.info("Entering RTSMainServlet");

			// Get the hostname first to help with logging.
			// get info about request
			String lsHostname = aaHttpServletRequest.getRemoteHost();
			// if no host name found, set it to WS_NONE
			if (lsHostname == null || lsHostname.length() == 0)
			{
				lsHostname = WS_NONE;
			}
			// defect 11320
			// Remove the cp parsing from this point!
			// end defect 11320

			// verify Module Name is numeric
			// report if not	 
			int liModuleName = 0;
			try
			{
				String lsModuleName =
					aaHttpServletRequest.getParameter(MODULE_NAME);
				liModuleName = Integer.parseInt(lsModuleName);
			}
			catch (Exception leEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.SERVER_DOWN, leEx);
				// defect 8299
				captureHttpParams(aaHttpServletRequest, leRTSEx);
				// end defect 8299
				leRTSEx.setDetailMsg(
					leRTSEx.getDetailMsg()
						+ System.getProperty(LINE_SEPARATOR)
						+ CLIENT_STR
						+ lsHostname
						+ ERR_TXT_MODULE);
				throw leRTSEx;
			}

			// verify Function Id is numeric
			// report if not
			//String lsFunctionId = null;
			int liFunctionId = 0;
			try
			{
				String lsFunctionId =
					aaHttpServletRequest.getParameter(FUNCTION_ID);
				liFunctionId = Integer.parseInt(lsFunctionId);
			}
			catch (Exception leEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.SERVER_DOWN, leEx);
				// defect 8299
				captureHttpParams(aaHttpServletRequest, leRTSEx);
				// end defect 8299
				leRTSEx.setDetailMsg(
					leRTSEx.getDetailMsg()
						+ System.getProperty(LINE_SEPARATOR)
						+ CLIENT_STR
						+ lsHostname
						+ ERR_TXT_FUNCTION);
				throw leRTSEx;
			}

			// Verify Data is not null
			// report if not
			String lsData = null;
			try
			{
				lsData = aaHttpServletRequest.getParameter(DATA);
				if (lsData == null)
				{
					throw new NullPointerException();
				}
			}
			catch (Exception leEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.SERVER_DOWN, leEx);
				// defect 8299
				captureHttpParams(aaHttpServletRequest, leRTSEx);
				// end defect 8299
				leRTSEx.setDetailMsg(
					leRTSEx.getDetailMsg()
						+ System.getProperty(LINE_SEPARATOR)
						+ CLIENT_STR
						+ lsHostname
						+ ERR_TXT_DATA_NULL);
				throw leRTSEx;
			}
			// end defect 8186

			// get the remote user
			//String lsHostUser = aaHttpServletRequest.getRemoteUser();

			int liLastChar = lsData.length();
			int liTwoCharsFromLast = lsData.charAt(liLastChar - 2);
			int liOneCharFromLast = lsData.charAt(liLastChar - 1);
			int liCounter = 0;

			// IMPORTANT  IMPORTANT  IMPORTANT  IMPORTANT  IMPORTANT     
			// This portion is added for the behavior of websphere on NT
			// and AIX server and Java client.
			if (liTwoCharsFromLast == INT13)
			{
				liCounter = liCounter + 1;
				if (liOneCharFromLast == INT10)
				{
					liCounter = liCounter + 1;
				}
				else
				{
					liCounter = 0;
				}
			}
			// This is added for NT and AIX with another servlet as
			// client.
			else
			{
				if (liOneCharFromLast == INT10)
				{
					liCounter = 1;
				}
			}
			// ENDS.......

			// defect 8186
			// surround StringToObj with try catch
			Object laObject = null;
			try
			{
				laObject =
					Comm.StringToObj(
						lsData.substring(
							0,
							lsData.length() - liCounter));
			}
			catch (Exception leEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.SERVER_DOWN, leEx);
				leRTSEx.setDetailMsg(
					leRTSEx.getDetailMsg()
						+ System.getProperty(LINE_SEPARATOR)
						+ CLIENT_STR
						+ lsHostname
						+ ERR_TXT_DATA_CONVERSION);
				throw leRTSEx;
			}
			// end defect 8186

			// if ping, just respond
			if (laObject
				instanceof com.txdot.isd.rts.services.util.Ping)
			{
				com.txdot.isd.rts.services.util.Ping laPing =
					(com.txdot.isd.rts.services.util.Ping) laObject;
				if (laPing.isCheckDB())
				{
					// TODO do we need to check on null here defect 8186 
					DatabaseAccess laDBAccess = new DatabaseAccess();
					try
					{
						laDBAccess.beginTransaction();
						laDBAccess.endTransaction(DatabaseAccess.NONE);
						laDBAccess = null;
						laPing.setOk(true);
					}
					catch (RTSException leRTSEx)
					{
						laDBAccess.endTransaction(DatabaseAccess.NONE);
						laDBAccess = null;
						laPing.setOk(false);
					}
					PrintWriter laOut =
						aaHttpServletResponse.getWriter();
					laOut.println(Comm.objToString(laPing));
					return;
				}
				else
				{
					PrintWriter laOut =
						aaHttpServletResponse.getWriter();
					laOut.println(Comm.objToString(laObject));
					return;
				}
			}

			//in each scenario, one need to call the processData, and
			//wrap the return object in the cache wrapper format
			// defect 8186
			// already checked for null
			//if (lsModuleName != null)
			//{
			// end defect 8186
			//to be completed when server objects are fully defined
			switch (liModuleName)
			{
				case GeneralConstant.ACCOUNTING :
					{
						AccountingServerBusiness laAccBusiness =
							new AccountingServerBusiness(lsHostname);
						laObject =
							laAccBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.GENERAL :
					{
						// defect 8849
						// Pass the host name into the business 
						// for use on messages.
						CacheManagerServerBusiness laCacheManagerServerBusiness =
							new CacheManagerServerBusiness(lsHostname);
						// end defect 8849

						laObject =
							laCacheManagerServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.FUNDS :
					{
						FundsServerBusiness laFundsServerBusiness =
							new FundsServerBusiness();
						laObject =
							laFundsServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.COMMON :
					{
						CommonServerBusiness laCommonServerBusiness =
							new CommonServerBusiness(lsHostname);
						laObject =
							laCommonServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.LOCAL_OPTIONS :
					{
						// defect 8849
						// Pass the host name into the business 
						// for use on messages.
						LocalOptionsServerBusiness laLocalOptionsServerBusiness =
							new LocalOptionsServerBusiness(lsHostname);
						// end defect 8849
						laObject =
							laLocalOptionsServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.INVENTORY :
					{
						InventoryServerBusiness laInventoryServerBusiness =
							new InventoryServerBusiness(lsHostname);
						laObject =
							laInventoryServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.REPORTS :
					{
						ReportsServerBusiness laReportsServerBusiness =
							new ReportsServerBusiness();
						laObject =
							laReportsServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.SYSTEMCONTROLBATCH :
					{
						// defect 8869
						com
							.txdot
							.isd
							.rts
							.server
							.systemcontrolbatch
							.SystemControlBatchServerBusiness laSCBSB =
							new com
								.txdot
								.isd
								.rts
								.server
								.systemcontrolbatch
								.SystemControlBatchServerBusiness(lsHostname);
						// end defect 8869
						laObject =
							laSCBSB.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.TITLE :
					{
						TitleServerBusiness laTitleServerBusiness =
							new TitleServerBusiness(lsHostname);
						laObject =
							laTitleServerBusiness.processData(
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.REGISTRATION :
					{
						RegistrationServerBusiness laRegistrationServerBusiness =
							new RegistrationServerBusiness();
						laObject =
							laRegistrationServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.MISC :
					{
						MiscServerBusiness laMiscServerBusiness =
							new MiscServerBusiness(lsHostname);
						laObject =
							laMiscServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.INTERNET_ADDRESS_CHANGE :
					{
						AddrChgServerBusiness laAddrChrgServerBusiness =
							new AddrChgServerBusiness();
						laObject =
							laAddrChrgServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.INTERNET_REG_REN :
					{
						RegistrationRenewalServerBusiness laRegRenServerBusiness =
							new RegistrationRenewalServerBusiness();
						laObject =
							laRegRenServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
				case GeneralConstant.INTERNET_REG_REN_PROCESSING :
					{
						RegRenProcessingServerBusiness laRegRenProcessingServerBusiness =
							new RegRenProcessingServerBusiness();
						laObject =
							laRegRenProcessingServerBusiness
								.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
					// defect 9085 
				case GeneralConstant.SPECIALPLATES :
					{
						SpecialPlatesServerBusiness laSpclPltsServerBusiness =
							new SpecialPlatesServerBusiness();
						laObject =
							laSpclPltsServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
					// end defect 9085 
					
					// defect 9831
				case GeneralConstant.MISCELLANEOUSREG :
					{
						MiscRegServerBusiness laServerBusiness =
							new MiscRegServerBusiness();
						laObject =
							laServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
						break;
					}
					// end defect 9831 
					
				default :
					{
						// change to throw server down since this is a 
						// serious error
						throw new RTSException(
							RTSException.SERVER_DOWN,
							new Exception(
								ERR_TXT_NO_SUCH_MODULE + liModuleName));
					}
			}
			// defect 8186
			//}
			//else
			//{
			//	throw new RTSException(
			//		RTSException.JAVA_ERROR,
			//		new Exception("RTSMainServlet - Module Name null"));
			//}
			// end defect 8186
			PrintWriter out = aaHttpServletResponse.getWriter();
			out.println(Comm.objToString(laObject));
		}
		catch (RTSException leRTSEx)
		{
			try
			{
				// defect 6574
				// make sure DetailMsg is initialized before use
				if (leRTSEx.getDetailMsg() == null)
				{
					leRTSEx.setDetailMsg(ERR_TXT_MSG_NULL);
				}
				// end defect 6574

				// defect 6238
				leRTSEx.setDetailMsg(
					leRTSEx.getDetailMsg()
						+ System.getProperty(LINE_SEPARATOR)
						+ ERR_TXT_SERVER
						+ aaHttpServletRequest.getServerName()
						+ System.getProperty(LINE_SEPARATOR)
						+ ERR_TXT_DATA_SIZE
						+ aaHttpServletRequest.getContentLength()
						+ System.getProperty(LINE_SEPARATOR)
						+ aaHttpServletRequest.getRequestDispatcher(
							ERR_TXT_WEBAPP));
				// end defect 6238

				// defect 8186
				// if this is an SD or DD exception, write it to 
				// the server log
				if (leRTSEx
					.getMsgType()
					.equals(RTSException.SERVER_DOWN)
					|| leRTSEx.getMessage().equals(RTSException.DB_DOWN))
				{
					// add the client hostname to the detail to write
					// to the server log.
					leRTSEx.setDetailMsg(
						leRTSEx.getDetailMsg()
							+ ERR_TXT_CLIENT
							+ aaHttpServletRequest.getRemoteHost());
					leRTSEx.writeExceptionToLog();
				}
				// end defect 8186

				PrintWriter laOut = aaHttpServletResponse.getWriter();
				laOut.println(Comm.objToString(leRTSEx));
			}
			catch (IOException leIOException)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					leIOException.getMessage());
				System.err.println(
					ERR_TXT_SERVLET
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).getYYYYMMDDDate()
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).get24HrTime());
				leIOException.printStackTrace();
			}
			catch (RTSException leRTSEx2)
			{
				Log.write(Log.SQL_EXCP, this, leRTSEx2.getMessage());
				System.err.println(
					ERR_TXT_SERVLET
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).getYYYYMMDDDate()
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).get24HrTime());
				leRTSEx2.printStackTrace();
			}
		}
		//If any runtime Exception, catch it and throw it.
		catch (Exception leEx2)
		{
			try
			{
				RTSException leRTSEx3 = null;

				// defect 6575
				// Also throw SD on NullPointerException
				if (leEx2 instanceof EOFException
					|| leEx2 instanceof NullPointerException)
				{
					leRTSEx3 =
						new RTSException(
							RTSException.SERVER_DOWN,
							leEx2);
					leRTSEx3
						.setDetailMsg(
							leRTSEx3.getDetailMsg()
							+ System.getProperty(LINE_SEPARATOR)
							+ ERR_TXT_SERVER
							+ aaHttpServletRequest.getServerName()
							+ System.getProperty(LINE_SEPARATOR)
							+ ERR_TXT_DATA_SIZE
							+ aaHttpServletRequest.getContentLength()
							+ System.getProperty(LINE_SEPARATOR)
							+ aaHttpServletRequest.getRequestDispatcher(
								ERR_TXT_WEBAPP)
					// defect 8186
					// add client hostname to exception
					+ERR_TXT_CLIENT
						+ aaHttpServletRequest.getRemoteHost()
					// end defect 8186
					);
					// defect 8186
					// write to the log since sd does not log.
					leRTSEx3.writeExceptionToLog();
					// end defect 8186
				}
				else
				{
					leRTSEx3 =
						new RTSException(
							RTSException.JAVA_ERROR,
							leEx2);
					leRTSEx3.setDetailMsg(
						leRTSEx3.getDetailMsg()
							+ System.getProperty(LINE_SEPARATOR)
							+ ERR_TXT_SERVER
							+ aaHttpServletRequest.getServerName()
							+ System.getProperty(LINE_SEPARATOR)
							+ ERR_TXT_DATA_SIZE
							+ aaHttpServletRequest.getContentLength()
							+ System.getProperty(LINE_SEPARATOR)
							+ aaHttpServletRequest.getRequestDispatcher(
								ERR_TXT_WEBAPP));
				}
				// end defect 6575

				PrintWriter laOut = aaHttpServletResponse.getWriter();
				laOut.println(Comm.objToString(leRTSEx3));
			}
			catch (IOException leIOException2)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					leIOException2.getMessage());
				System.err.println(
					ERR_TXT_SERVLET
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).getYYYYMMDDDate()
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).get24HrTime());
				leIOException2.printStackTrace();
			}
			catch (RTSException leRTSEx4)
			{
				Log.write(Log.SQL_EXCP, this, leRTSEx4.getMessage());
				System.err.println(
					ERR_TXT_SERVLET
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).getYYYYMMDDDate()
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).get24HrTime());
				leRTSEx4.printStackTrace();
			}
		}
		catch (Throwable leThrowable)
		{
			System.err.println(
				ERR_TXT_SERVLET
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).getYYYYMMDDDate()
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).get24HrTime());
			leThrowable.printStackTrace();
		}
	}
}
