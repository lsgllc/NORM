package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.security.Security;
import java.util.Properties;
import java.util.Vector;

import HTTPClient.*;
import HTTPClient.AuthorizationInfo;
import HTTPClient.CookieModule;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;

/*
 * EpayVendorReportFetcher.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History 
 * Name     	Date        Description
 * ------------ ----------- --------------------------------------------
 * S Johnston	02/25/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify fetch()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * S Johnston	07/22/2005	Add logging on failed logon attempts.
 * 							Also add re-try logic for login.
 * 							Found that we are having problems getting 
 * 							logged in to the EPay site.
 * 							add cvRequestURLs
 *							add getRequestURLs
 * 							modify doFetch()
 * 							defect 8293 Ver 5.2.2 Fix 5a 
 * Ray Rowehl	07/26/2005	Further code cleanup.
 * 							Just following up in 5.2.3 from defect 8293.
 * 							webapps log calls are now fully qualified.
 * 							defect 7889 Ver 5.2.3
 * Ray Rowehl	08/01/2005	Cleanup from walkthrough comments.
 * 							Note that re-try should be used instead of
 * 							retry.  Unsuccessful does not have a dash
 * 							Should be using defect 7889 for this work.
 * 							defect 7889 Ver 5.2.3
 * B Brown  	04/26/2006  Add STR_CHECK_ON_REPORT_PAGE
 *       					Modify doFetch(HTTPConnection)
 *       					defect 8609 Ver 5.2.2 Fix 8
 * B Brown  	05/01/2006  Add Epay login error handling
 *       					Change TXT_ variable names to STR_ 
 *       					modify doFetch()
 * 							modify fetch()
 * 							modify initPage()
 * 							modify setDateRange() 
 *       					defect 8743 Ver 5.2.2 Fix 8
 * B Brown 		06/20/2006  Change the way we login to the new Epay
 *       					CustomerService site. We now have to post
 *       					the username, password, and login button
 *       					parameters.
 *       					modify doFetch()
 *       					modify initPage()
 *       					deprecated setLoginPageFull()
 *       					defect 8735 Ver 5.2.2 Fix 8
 * B Brown		07/27/2006	Comment out the setUseProxy(), since  
 * 							it's initialized to true when defined. 
 *							modify initDefault()
 *							defect 8368 Ver 5.2.4
 * B Brown		11/19/2008	Add date range to the trace number query for
 * 							the new Epay Customer Service release.   
 *							modify initPage()
 *							defect 9872 Ver 5.8.1
 * B Brown		12/16/2008	Add TRANS_HISTORY and DISPLAY_DETAILS fetch 
 * 							modes.
 * 							add TRANS_HISTORY, csTransHistoryReportPage, 
 * 							DISPLAY_DETAILS
 * 							modify initPage()
 * 							defect 9878 Ver Defect_POS_C
 * B Brown		11/18/2009	Reinstate commented out code that reports
 * 							to the POS user and to batch.log if there
 * 							is a login problem to Epay.
 * 							modify doFetch()
 * 							defect 10285 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods for Epay Vendor Report Fetcher
 * 
 * @version	Defect_POS_H	11/18/2009   
 * @author	Clifford Chen
 * <br>Creation Date:		02/12/2002 18:15:53
 */
public class EpayVendorReportFetcher
{
	private static final String DEFAULT_PAYMENT_TYPE = "CCCAP";

	private static final String MSG_EPAY_DOFETCH_BEGIN =
			"EpayVendorReportFetcher, doFetch, Begin to fetch";
	private static final String MSG_ERR_EPAY_LOGIN_FAILED =
		"EpayVendorReportFetcher, doFetch, could not log in to vendor"
			+ " site https://";
	private static final String MSG_ERR_FAILED_LOG_EXCEPTION =
		"Failed to log on to the Epay Vendor Report site";
	private static final String MSG_ERR_FAILED_REPORT_FETCH_AFTER_LOGIN =
		"EpayVendorReportFetcher, doFetch, could not retrieve the "
			+ "vendor report after logged in from https://";
	private static final String MSG_ERR_FAILED_TO_RETRIEVE_VPR_DATA =
		"Failed to retrieve the Epay Vendor Report data";
	private static final String MSG_LOGIN_DONE =
		"EpayVendorReportFetcher, doFetch, Login Done!";
	private static final String MSG_VPR_FETCH_ADDED_SECURITY_PROVIDER =
		"EpayVendorReportFetcher, fetch, added security provider";
	private static final String MSG_VPR_FETCH_BEGIN =
		"EpayVendorReportFetcher, fetch, Enter";
	private static final String MSG_EPAY_FETCH_DATA_DONE =
		"EpayVendorReportFetcher, doFetch, Grabbing Data Done!";
	private static final String MSG_VPR_FETCH_END =
		"EpayVendorReportFetcher, fetch, Exit";
	private static final String MSG_VPR_INITIATED_CONNECTION =
		"EpayVendorReportFetcher, fetch, initiated connection obj";

	private static final String STR_AMPERSAND = "&";
	private static final String STR_EQUAL_SIGN = "=";
	private static final String STR_PERIOD = ".";
	private static final String STR_QUESTION_MARK = "?";
	
	// defect 8609
	private static final String STR_AT_SPACES = " at: ";
		
	private static final String STR_CHECK_ON_REPORT_PAGE =
	"Welcome";
	
	private static final String STR_DASHED_LINE =
		"===================================";
	private static final String STR_FAILED_LOGON_RE_TRY_ATTEMPT =
		"Failed Logon re-try attempt ";
	private static final String STR_FAILED_LOGON_ATTEMPT_END =
		"End Failed Logon re-try attempt ";
	private static final String STR_FAILED_LOGON_BEGIN =
		"Failed Logon attempt at: ";
	private static final String STR_FAILED_LOGON_END =
		"End Failed Logon attempt.";
	private static final String STR_HTTPS = "https";
	private static final String STR_HTTPS_COLON_SLASHES = "https://";
	private static final String STR_HTTP_RC_OF =
		" with HTTP return code of ";
	private static final String STR_HTTP_PROXYPORT = "http.proxyPort";
	private static final String STR_HTTP_PROXYHOST = "http.proxyHost";
	private static final String STR_IS_EQUAL_MINUS_1 = "=-1";
	private static final String STR_MM_DD_YYYY = "MM/dd/yyyy";
	private static final String STR_NEW_LINE_CHARS = "\r\n";
	private static final String STR_PROTOCOL_HANDLER_VALUE =
		"com.sun.net.ssl.internal.www.protocol";
	private static final String STR_PROTOCOL_HANDLER_PROPERTY =
		"java.protocol.handler.pkgs";
	private static final String STR_SUCCESSFUL_LOGON_AFTER_RE_TRY =
		"Successful logon to Customer Service site on re-try: ";
	private static final String STR_YYYYMMDD = "yyyyMMdd";
	// defect 8735
	private static final String STR_SUBMIT_BUTTON = "SubmitButton";
	private static final String STR_LOGIN = "Login";
	// end defect 8735

	public static final int DATE_RANGE = 0;
	public static final int TRACE_NUM = 1;
	// defect 9878
	public static final int TRANS_HISTORY = 2;
	private String csTransHistoryReportPage = "/customerservice/TransHistory";
	public static final int DISPLAY_DETAILS = 3;
//	private String csDisplayDetailsReportPage = "/customerservice/DisplayDetails";
	// end defect 9878

	private static final int NUMBER_OF_RE_TRIES = 5;
	private static final int STRING_NOT_FOUND = -1;

	// proxy
	private boolean cbUseProxy;
	private String csProxyHost;
	private int ciProxyPort;
	private String csProxyUser;
	private String csProxyPassword;
	private String csProxyRealm;

	// epay
	private String csEpayServer;
	private String csLoginPage;
	private String csLoginPageFull;
	private String csVendorReportPage;
	// defct 9878
	// private String csVendorReportPageFull;
	protected String csVendorReportPageFull;
	// end defect 9878
	private String csTraceNum;
	private String csVendorId;
	private String csPaymentType;

	// Tag names
	private String csVendorUserTag;
	private String csVendorPasswordTag;
	private String csVendorDateStartTag;
	private String csVendorDateEndTag;
	private String csVendorIdTag;
	private String csVendorTraceNumTag;
	private String csVendorPaymentTypeTag;
	private String csVendorDisplayNumTag;
	

	// epay user
	private String csEpayUser;
	private String csEpayPassword;

	// date range
	private String csFromDate;
	private String csToDate;

	// the method to fetch, by DATE_RANGE or TRACE_NUM
	private int ciFetchMode;

	// the pages to fetch
	// defect 9878
	// private Vector cvPages = new Vector();
	protected Vector cvPages = new Vector();
	// end defect 9878

	// defect 8293
	private Vector cvRequestURLs = new Vector();
	// end defect 8293

	/**
	 * URLRetriever constructor.
	 */
	public EpayVendorReportFetcher()
	{
		super();
		initDefault();
	}

	/**
	 * Add the Vendor Report Page Full to the pages vector.
	 */
	public void addPage()
	{
		initPage();
		if (!cvPages.contains(csVendorReportPageFull))
		{
			cvPages.add(csVendorReportPageFull);
		}
	}

	/**
	 * addTraceNum
	 * 
	 * @param asTraceNum String
	 */
	public void addTraceNum(String asTraceNum)
	{
		csTraceNum = asTraceNum;
		initPage();
		addPage();
	}

	/**
	 * addTraceNum
	 * 
	 * @param avTraceNum Vector
	 */
	public void addTraceNum(Vector avTraceNum)
	{
		for (int i = 0; i < avTraceNum.size(); ++i)
		{
			String lsTraceNum = (String) avTraceNum.elementAt(i);
			addTraceNum(lsTraceNum);
		}
	}

	/**
	 * Make the connection to EPay.
	 * 
	 * @param aaConn HTTPConnection
	 * @return Vector
	 * @throws Exception
	 */
	private Vector doFetch(HTTPConnection aaConn) throws Exception
	{
		initPage();

		com.txdot.isd.rts.server.webapps.util.Log.info(
			MSG_EPAY_DOFETCH_BEGIN);
			
		// defect 8735
	  	// HTTPResponse laResponse=aaConn.Get(csLoginPageFull); 
	  	NVPair[] laParms = new NVPair[3];
	  	laParms[0] = new NVPair(csVendorUserTag,csEpayUser);
	  	laParms[1] = new NVPair(csVendorPasswordTag,csEpayPassword);
	  	laParms[2] = new NVPair(STR_SUBMIT_BUTTON,STR_LOGIN);
	  	HTTPResponse laResponse=aaConn.Post(csLoginPage, laParms);
	  	// end defect 8735


		// defect 8293
		
		// - new Welcome"Welcome to the ePay Customer Service System"; 
		// - old Welcome "CSWelcome";
		// make it flexible for both old and new Customer service sites
		
		// for the first logon attempt to a county for the VPR, the 
		// welcome string will not be found, because the welcome screen 
		// is not presented until the username and password login is 
		// successful
		
		// defect 8609
		// rename STR_ variables to STR_ throughout this method
		// end defect 8609
		int liStart = laResponse.getText().indexOf(STR_CHECK_ON_REPORT_PAGE);
		
		// defect 8743
		boolean lbSuccess = false;
		// end defect 8743

		
		if (liStart == STRING_NOT_FOUND)
		{
			// if we are still at login screen
			// log that we failed the first logon attempt
			String lsError =
				STR_NEW_LINE_CHARS
					+ STR_DASHED_LINE
					+ STR_NEW_LINE_CHARS
					+ STR_FAILED_LOGON_BEGIN
					+ aaConn
					// defect 8735
					// + csLoginPageFull
					+ csLoginPage
					// end defect 8735
					+ STR_NEW_LINE_CHARS
					+ STR_DASHED_LINE
					+ STR_NEW_LINE_CHARS
					+ laResponse.getText()
					+ STR_DASHED_LINE
					+ STR_NEW_LINE_CHARS
					+ STR_FAILED_LOGON_END
					+ STR_NEW_LINE_CHARS
					+ STR_DASHED_LINE;
			com.txdot.isd.rts.services.util.Log.write(
				com.txdot.isd.rts.services.util.Log.SQL_EXCP,
				this,
				lsError);
				

			// re-try logging in NUMBER_OF_RE_TRIES times or 
			// until you have success
			for (int i = 1; i <= NUMBER_OF_RE_TRIES; i++)
			{
				// defect 8735
				// laResponse = aaConn.Get(csLoginPageFull);
				laResponse=aaConn.Post(csLoginPage, laParms);
				// end defect 8735
				
				//lsStart = STR_CHECK_ON_REPORT_PAGE;
				// liStart = laResponse.getText().indexOf(lsStart);
				liStart = laResponse.getText().indexOf(STR_CHECK_ON_REPORT_PAGE);
				if (liStart >= 0)
				{
					// log the fact that we successfully logged into 
					// Customer Service and break from loop
					com.txdot.isd.rts.services.util.Log.write(
						com.txdot.isd.rts.services.util.Log.SQL_EXCP,
						this,
						STR_SUCCESSFUL_LOGON_AFTER_RE_TRY + i);
					lbSuccess = true;
					break;
				}
				else
				{
					lsError =
						STR_NEW_LINE_CHARS
							+ STR_DASHED_LINE
							+ STR_NEW_LINE_CHARS
							+ STR_FAILED_LOGON_RE_TRY_ATTEMPT
							+ i
							+ STR_AT_SPACES
							+ aaConn
							// defect 8735
							// + csLoginPageFull
							+ csLoginPage
							// end defect 8735
							+ STR_NEW_LINE_CHARS
							+ STR_DASHED_LINE
							+ STR_NEW_LINE_CHARS
							+ laResponse.getText()
							+ STR_DASHED_LINE
							+ STR_NEW_LINE_CHARS
							+ STR_FAILED_LOGON_ATTEMPT_END
							+ 1
							+ STR_PERIOD
							+ STR_NEW_LINE_CHARS
							+ STR_DASHED_LINE;
					com.txdot.isd.rts.services.util.Log.write(
						com.txdot.isd.rts.services.util.Log.SQL_EXCP,
						this,
						lsError);
				}
			}
			
			// defect 10285
			// reinstate the below code indicating the login error
			// defect 8609
			// added || lbSuccess == false in the if below this if 

			if (lbSuccess == false)
			{
				// log the fact that we did not successfully log into 
				// Customer Service after all subsequent retries
				com.txdot.isd.rts.services.util.Log.write(
					com.txdot.isd.rts.services.util.Log.SQL_EXCP,
					this,
					MSG_ERR_FAILED_LOG_EXCEPTION);
				
				// defect 8609
				// this is where the login failure is caught!!
				throw new Exception(MSG_ERR_FAILED_LOG_EXCEPTION);
				// end defect 8609	
			// end defect 10285	
//					
			}

		}
		else
		{
			lbSuccess = true;
		}
		// end defect 8609
		
		// end defect 8293
		//String lsLoginResult = null;

		// defect 8293
		// If the HTTP return code is not 200 log it.
		
		 			
		if (laResponse.getStatusCode() != 200 || lbSuccess == false)
		{
			com.txdot.isd.rts.services.util.Log.write(
				com.txdot.isd.rts.services.util.Log.SQL_EXCP,
				this,
				STR_NEW_LINE_CHARS
					+ MSG_ERR_EPAY_LOGIN_FAILED
					+ csEpayServer
					// defect 8735
					// + csLoginPageFull
					+ csLoginPage
					// end defect 8735
					+ STR_HTTP_RC_OF
					+ laResponse.getStatusCode());
			
			// defect 8743
			// throw exception
			//if (laResponse.getStatusCode() >= 300)
			//{
			// throw new Exception(MSG_ERR_FAILED_LOG_EXCEPTION);
			throw new RTSException(MSG_ERR_FAILED_LOG_EXCEPTION);
			//}
			// end defect 8743
			// end defect 8293
		}

		com.txdot.isd.rts.server.webapps.util.Log.info(MSG_LOGIN_DONE);
		// (2) get the vendor reports
		Vector lvData = new Vector();
		for (int i = 0; i < cvPages.size(); ++i)
		{
			String lsReportPage = (String) cvPages.elementAt(i);
			laResponse = aaConn.Get(lsReportPage);
			// defect 8293
			cvRequestURLs.add(
				STR_HTTPS_COLON_SLASHES + csEpayServer + lsReportPage);
			// end defect 8293
			if (laResponse.getStatusCode() >= 300)
			{
				// defect 8293
				com.txdot.isd.rts.services.util.Log.write(
					com.txdot.isd.rts.services.util.Log.SQL_EXCP,
					this,
					STR_NEW_LINE_CHARS
						+ MSG_ERR_FAILED_REPORT_FETCH_AFTER_LOGIN
						+ csEpayServer
						+ lsReportPage
						+ STR_HTTP_RC_OF
						+ laResponse.getStatusCode());
				// end defect 8293
				throw new Exception(MSG_ERR_FAILED_TO_RETRIEVE_VPR_DATA);
				
			}
			else
			{
				lvData.add(laResponse.getText());
			}
		}
		com.txdot.isd.rts.server.webapps.util.Log.info(
			MSG_EPAY_FETCH_DATA_DONE);
		return lvData;
	}

	/**
	 * fetch
	 * 
	 * @return Vector
	 * @throws Exception
	 */
	public Vector fetch() throws Exception
	{
		com.txdot.isd.rts.server.webapps.util.Log.fine(
			MSG_VPR_FETCH_BEGIN);
		if (cbUseProxy)
		{
			// defect 8609
			Properties laProperties = System.getProperties();
			laProperties.put(STR_HTTP_PROXYHOST, csProxyHost);
			laProperties.put(
				STR_HTTP_PROXYPORT,
				String.valueOf(ciProxyPort));
		}
		// NOT this one !!!
		// properties.put("https.proxyHost", tunnelHost);
		// properties.put("https.ciProxyPort",
		// String.valueOf(tunnelPort));										
		System.setProperty(
			STR_PROTOCOL_HANDLER_PROPERTY,
			STR_PROTOCOL_HANDLER_VALUE);
		Security.addProvider(
			new com.sun.net.ssl.internal.ssl.Provider());
		com.txdot.isd.rts.server.webapps.util.Log.finer(
			MSG_VPR_FETCH_ADDED_SECURITY_PROVIDER);
		HTTPConnection laConn =
			new HTTPConnection(
				STR_HTTPS,
				csEpayServer,
				STRING_NOT_FOUND);
		// end defect 8609		
		if (cbUseProxy)
		{
			AuthorizationInfo.addBasicAuthorization(
				csProxyHost,
				ciProxyPort,
				csProxyRealm,
				csProxyUser,
				csProxyPassword);
			// defect 7889
			// Removed graph of DatabaseAccess and changed
			// method call to be a staticly accessed
			// laConn.setProxyServer(csProxyHost, ciProxyPort);
			HTTPConnection.setProxyServer(csProxyHost, ciProxyPort);
			// end defect 7889
		}
		// accept cookies
		CookieModule.setCookiePolicyHandler(null);
		com.txdot.isd.rts.server.webapps.util.Log.finer(
			MSG_VPR_INITIATED_CONNECTION);
		Vector lvResult = doFetch(laConn);
		com.txdot.isd.rts.server.webapps.util.Log.fine(
			MSG_VPR_FETCH_END);
		return lvResult;
	}

	/**
	 * Setup the default initialization
	 */
	public void initDefault()
	{
		// default 
		csPaymentType = DEFAULT_PAYMENT_TYPE;
		ciFetchMode = DATE_RANGE;
		setLoginTag(
			CommunicationProperty.getEpayVendorUserTag(),
			CommunicationProperty.getEpayVendorPasswordTag());
		setPageTag(
			CommunicationProperty.getEpayVendorDateStartTag(),
			CommunicationProperty.getEpayVendorDateEndTag(),
			CommunicationProperty.getEpayVendorVendorIdTag(),
			CommunicationProperty.getEpayVendorTraceNumTag(),
			CommunicationProperty.getEpayVendorPaymentTypeTag(),
			CommunicationProperty.getEpayVendorDisplayNumTag());
		setEpayAccount(
			CommunicationProperty.getEpayVendorUser(),
			CommunicationProperty.getEpayVendorPassword());
		setServer(CommunicationProperty.getEpayVendorHost());
		setLoginPage(CommunicationProperty.getEpayVendorLoginPage());
		setVendorReportPage(
			CommunicationProperty.getEpayVendorReportPage());
		setProxyServer(
			CommunicationProperty.getProxyHost(),
			CommunicationProperty.getProxyPort());
		setProxyAuthInfo(
			CommunicationProperty.getProxyUser(),
			CommunicationProperty.getProxyPassword(),
			CommunicationProperty.getProxyRealm());
		// user control
		
		// defect 8368
		// setUseProxy is called from EpayVendorReport.getVendorReport
		// at the time the report is fetched.
		// setUseProxy(true);
		// end defect 8368
	}

	/**
	 * Initialize the page
	 */
	private void initPage()
	{
		// defect 8735
//		if (csLoginPageFull == null)
//		{
//			csLoginPageFull =
//				csLoginPage
//					+ STR_QUESTION_MARK
//					+ csVendorUserTag
//					+ STR_EQUAL_SIGN
//					+ csEpayUser
//					+ STR_AMPERSAND
//					+ csVendorPasswordTag
//					+ STR_EQUAL_SIGN
//					+ csEpayPassword;
//		}
		// end defect 8735
		
		// defect 9878
//		if (ciFetchMode == DATE_RANGE)
//		{
		switch (ciFetchMode)
		{
			case DATE_RANGE:
			{		
				csVendorReportPageFull =
					csVendorReportPage
						+ STR_QUESTION_MARK
						+ csVendorDateStartTag
						+ STR_EQUAL_SIGN
						+ csFromDate
						+ STR_AMPERSAND
						+ csVendorDateEndTag
						+ STR_EQUAL_SIGN
						+ csToDate
						+ STR_AMPERSAND
						+ csVendorIdTag
						+ STR_EQUAL_SIGN
						+ csVendorId
						+ STR_AMPERSAND
						+ csVendorPaymentTypeTag
						+ STR_EQUAL_SIGN
						+ csPaymentType
						+ STR_AMPERSAND
						+ csVendorDisplayNumTag
						// defect 8609
						+ STR_IS_EQUAL_MINUS_1;
						// end defect 8609
				break;
			}
//		if (ciFetchMode == TRACE_NUM)
//		{
			case TRACE_NUM:
			{
				csVendorReportPageFull =
					csVendorReportPage
						+ STR_QUESTION_MARK
						// defect 9872
						+ csVendorDateStartTag
						+ STR_EQUAL_SIGN
						+ csFromDate
						+ STR_AMPERSAND
						+ csVendorDateEndTag
						+ STR_EQUAL_SIGN
						+ csToDate
						+ STR_AMPERSAND
						// end defect 9872
						+ csVendorIdTag
						+ STR_EQUAL_SIGN
						+ csVendorId
						+ STR_AMPERSAND
						+ csVendorTraceNumTag
						+ STR_EQUAL_SIGN
						+ csTraceNum
						+ STR_AMPERSAND
						+ csVendorPaymentTypeTag
						+ STR_EQUAL_SIGN
						+ csPaymentType
						+ STR_AMPERSAND
						+ csVendorDisplayNumTag
						// defect 8609
						+ STR_IS_EQUAL_MINUS_1;
						// end defct 8609
				break;		
			}
			case TRANS_HISTORY:
			{
				csVendorReportPageFull =
					csTransHistoryReportPage
						+ STR_QUESTION_MARK
						+ csVendorIdTag
						+ STR_EQUAL_SIGN
						+ csVendorId
						+ STR_AMPERSAND
						+ csVendorTraceNumTag
						+ STR_EQUAL_SIGN
						+ csTraceNum
						+ STR_AMPERSAND
						+ csVendorPaymentTypeTag
						+ STR_EQUAL_SIGN
						+ csPaymentType;
				break;		
			}
//			case DISPLAY_DETAILS:
//				{
//					csVendorReportPageFull =
//						csDisplayDetailsReportPage
//							+ STR_QUESTION_MARK
//							+ csVendorIdTag
//							+ STR_EQUAL_SIGN
//							+ csVendorId
//							+ STR_AMPERSAND
//							+ csVendorTraceNumTag
//							+ STR_EQUAL_SIGN
//							+ csTraceNum
//							+ STR_AMPERSAND
//							+ csVendorPaymentTypeTag
//							+ STR_EQUAL_SIGN
//							+ csPaymentType;
//					break;		
//				}					
			default:
			{
			}
			// end defect 9878
		}
	}

	/**
	 * main
	 * 
	 * @param aarrArgs
	 */
	public static void main(String[] aarrArgs)
	{
		// these values change depending on what is being tested.
		String lsVendorId = "2246";
		String lsFromDate = "20020626";
		String lsToDate = "20020627";
		// for testing purpose only	 
		EpayVendorReportFetcher laFetcher =
			new EpayVendorReportFetcher();
		laFetcher.setVendorId(lsVendorId);
		/////////////////////////////////////////////
		// do NOT set a date range which is too large
		laFetcher.setDateRange(lsFromDate, lsToDate);
		laFetcher.addPage();
		laFetcher.setPaymentType("CCCR");
		laFetcher.addPage();
		try
		{
			Vector lvResult = laFetcher.fetch();
			System.out.println((String) lvResult.elementAt(0));
			System.out.println((String) lvResult.elementAt(1));
			// OrigTransDate 
			laFetcher.setFetchMode(TRACE_NUM);
			laFetcher.setPaymentType(DEFAULT_PAYMENT_TYPE);
			Vector lvTraceNum = new Vector();
			lvTraceNum.add("246VRL67RXP6IA8A");
			lvTraceNum.add("246VRYV6622DWVSA");
			laFetcher.addTraceNum(lvTraceNum);
			lvResult = laFetcher.fetch();
			for (int i = 0; i < lvResult.size(); ++i)
			{
				System.out.println((String) lvResult.elementAt(i));
			}
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	// defect 8293
	/**
	 * getRequestURLs - holds the URLs for our request pages
	 * 
	 * @return Vector
	 */
	public Vector getRequestURLs()
	{
		return cvRequestURLs;
	}
	// end defect 8293

	/**
	 * setDateRange - newFromDate, newToDate : yyyyMMdd format
	 * need mm/dd/yyyy format
	 * 
	 * @param asNewFromDate String
	 * @param asNewToDate String
	 */
	public void setDateRange(String asNewFromDate, String asNewToDate)
	{
		// defect 8609
		csFromDate =
			UtilityMethods.formatDate(
				asNewFromDate,
				STR_YYYYMMDD,
				STR_MM_DD_YYYY);
		csToDate =
			UtilityMethods.formatDate(
				asNewToDate,
				STR_YYYYMMDD,
				STR_MM_DD_YYYY);
		// end defect 8609		
	}

	/**
	 * setEpayAccount
	 * 
	 * @param asNewEpayUser String
	 * @param asNewEpayPassword String
	 */
	public void setEpayAccount(
		String asNewEpayUser,
		String asNewEpayPassword)
	{
		csEpayUser = asNewEpayUser;
		csEpayPassword = asNewEpayPassword;
	}

	/**
	 * setFetchMode
	 * 
	 * @param aiNewFetchMode int
	 */
	public void setFetchMode(int aiNewFetchMode)
	{
		if (ciFetchMode != aiNewFetchMode)
		{
			ciFetchMode = aiNewFetchMode;
			// switch to another fetch mode,clear the pages 
			// stored in the previous mode
			cvPages.clear();
		}
	}

	/**
	 * setLoginPage
	 * 
	 * @param asNewLoginPage String
	 */
	public void setLoginPage(String asNewLoginPage)
	{
		csLoginPage = asNewLoginPage;
	}

	/**
	 * setLoginPageFull
	 * 
	 * @param asNewLoginPageFull String
	 * @deprecated
	 */
	public void setLoginPageFull(String asNewLoginPageFull)
	{
		csLoginPageFull = asNewLoginPageFull;
	}

	/**
	 * setLoginTag
	 * 
	 * @param asNewUserTag String
	 * @param asNewPasswordTag String
	 */
	public void setLoginTag(
		String asNewUserTag,
		String asNewPasswordTag)
	{
		csVendorUserTag = asNewUserTag;
		csVendorPasswordTag = asNewPasswordTag;
	}

	/**
	 * setPageTag
	 * 
	 * @param asNewDateStartTag String
	 * @param asNewDateEndTag String
	 * @param asNewVendorIdTag String
	 * @param asNewVendorTraceNumTag String
	 * @param asNewPaymentTypeTag String
	 * @param asNewDisplayNumTag String
	 */
	public void setPageTag(
		String asNewDateStartTag,
		String asNewDateEndTag,
		String asNewVendorIdTag,
		String asNewVendorTraceNumTag,
		String asNewPaymentTypeTag,
		String asNewDisplayNumTag)
	{

		csVendorDateStartTag = asNewDateStartTag;
		csVendorDateEndTag = asNewDateEndTag;
		csVendorIdTag = asNewVendorIdTag;
		csVendorTraceNumTag = asNewVendorTraceNumTag;
		csVendorPaymentTypeTag = asNewPaymentTypeTag;
		csVendorDisplayNumTag = asNewDisplayNumTag;
	}

	/**
	 * setPaymentType
	 * 
	 * @param asNewPaymentType String
	 */
	public void setPaymentType(String asNewPaymentType)
	{
		csPaymentType = asNewPaymentType;
	}

	/**
	 * setProxyAuthInfo
	 * 
	 * @param asNewProxyUser String
	 * @param asNewProxyPassword String
	 * @param asNewProxyRealm String
	 */
	public void setProxyAuthInfo(
		String asNewProxyUser,
		String asNewProxyPassword,
		String asNewProxyRealm)
	{
		csProxyUser = asNewProxyUser;
		csProxyPassword = asNewProxyPassword;
		csProxyRealm = asNewProxyRealm;
	}

	/**
	 * setProxyServer
	 * 
	 * @param asNewProxyHost String
	 * @param aiNewProxyPort int
	 */
	public void setProxyServer(
		String asNewProxyHost,
		int aiNewProxyPort)
	{
		csProxyHost = asNewProxyHost;
		ciProxyPort = aiNewProxyPort;
	}

	/**
	 * setServer
	 * 
	 * @param asNewEpayServer String
	 */
	public void setServer(String asNewEpayServer)
	{
		csEpayServer = asNewEpayServer;
	}

	/**
	 * setUseProxy
	 * 
	 * @param abNewUseProxy boolean
	 */
	public void setUseProxy(boolean abNewUseProxy)
	{
		cbUseProxy = abNewUseProxy;
	}

	/**
	 * setVendorId
	 * 
	 * @param asNewVendorId String
	 */
	public void setVendorId(String asNewVendorId)
	{
		csVendorId = asNewVendorId;
	}

	/**
	 * setVendorReportPage
	 * 
	 * @param asNewVendorReportPage String
	 */
	public void setVendorReportPage(String asNewVendorReportPage)
	{
		csVendorReportPage = asNewVendorReportPage;
	}
}