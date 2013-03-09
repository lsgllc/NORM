package com.txdot.isd.rts.services.webapps.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * CommunicationProperty.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/08/2005	Added a method used to set the link prefix
 * 							to the current context of the csEnvironment.
 * 							This is only called when env. is txdot at 
 * 							this time and is called from main menu jsp.
 * 							add setLinkPrefix()
 *							defect 7712 ver 5.2.3
 * Ray Rowehl	03/03/2006	Always use resource stream to find file 
 * 							to load properties from.
 * 							Did some minor 5.2.3 code cleanup.
 * 							modify init()
 * 							defect 8553 Ver 5.2.3
 * Ray Rowehl	04/25/2006	Found that we attempt to load TXO first.
 * Bob Brown				If that fails, then attempt to load TXDOT.
 * 							Note that the prop files are in different
 * 							locations - one "client" and one "server".
 * 							Now we attempt to load teh properties files
 * 							using getSystemResourceAsStream first.
 * 							If that fails, then we use FileInputStream.
 * 							modify init() 
 * 							Make the env and region variables public for 
 * 							the TxO project.
 * 							defect 8553 Ver 5.2.3
 * B Brown		05/15/2006	For conversion of the 5.2.3 RefundData 
 * 							object to the 5.2.2 TxO RefundData object, 
 *  						and the 5.2.3 PaymentData object to the 
 * 							5.2.2 TxO PaymentData object, a TxOVersion
 * 							property was added to check the version
 * 							TxO is running under, before sending refund
 * 							and payment data requests to Txo.
 * 							This code relies on an rtswebapps 
 * 							property file change, which conatains this
 * 							new TxOVersion property value.
 * 							add MSG_COMM_PROP_TxOVersion
 * 							add csTxOVersion
 * 							add getTxOVersion()
 * 							modify setValues()
 * 							modify printValues() 
 * 							defect 8777 Ver 5.2.3
 * Jeff S.		08/01/2006	Renamed cbUseProxy to cbVendorPayRptUseProxy
 * B. Brown					Renamed lbRefundProxyProxyOn to 
 * 							cbRefundPaymentUseProxy 
 *							add MSG_COMM_PROP_VENDORPAYRPT_PROXY_FLAG,
 *								MSG_COMM_PROP_REFUND_PAYMENT_PROXY_FLAG					
 * 							add getVendorPayRptUseProxy()
 * 								getRefundPaymentUseProxy()
 * 							delete getRefundInfoProxyOn()
 * 							delete MSG_MAIN_PRINT_PROPS
 * 							modify setValues()
 * 								   printValues() 
 * 							defect 8368 Ver 5.2.4
 * B. Brown		09/05/2006	Bea Weblogic change 
 * 							modify init(), setValues()
 * 							defect 8925 IVTRS Ver 2.0.0
 * B. Brown		10/03/2006  Add a read to a new version.cfg file
 * 							to get the IVRTS version and present in
 * 							the txdot_vtr_help.jsp	
 *							add IVTRS_VERSION_FILE_NAME
 * 								csIVTRSVersion					
 * 							add getIVTSVersion()
 * 								setIVTSVersion()
 * 							modify setValues()
 * 								   init()	
 * 								   printValues() 
 * 							defect 8250 IVTRS Version 2.0.0
 * B. Brown		10/31/2006	Bea Weblogic change
 *							Use getResourceAsStream to read all 
 *							properties files except 
 *							Configuration.properties
 * 							modify init()
 * 							defect 8925 IVTRS Ver 2.0.0
 * Jeff S.		04/23/2007	Add new property that holds where to go
 * 							to connect to the web service.
 * 							add csWebServiceHost, csWebServiceName, 
 * 								csWebServicePort, 
 * 								MSG_COMM_PROP_WEB_SERVICE_HOST,
 * 								MSG_COMM_PROP_WEB_SERVICE_NAME,
 * 								MSG_COMM_PROP_WEB_SERVICE_PORT
 * 							add getWebServiceHost(), 
 * 								getWebServiceName(), getWebServicePort()
 * 							modify setValues(), printValues()
 * 							defect 9121 Ver Special Plates
 * Jeff S.		06/26/2007	Add new property that decides if we are to
 * 							auto create the POS transaction for Special
 * 							Plates.
 * 							add csAutoCreateSPPOSTrans,
 * 								MSG_COMM_PROP_AUTO_CREATE_SP_TRANS
 * 							add getAutoCreateSPPOSTrans()
 * 							modify setValues(), printValues()
 * 							defect 9121 Ver Special Plates
 * B. Brown		03/24/2008  Add a version date to the version.cfg file
 * 							to get the IVRTS version date and present in
 * 							the txdot_vtr_help.jsp	
 *							add TXT_IVTRS_VERSION_DATE
 * 								ssIVTRSVersionDate					
 * 							add getIVTRSVersionDate()
 * 								setIVTRSVersionDate()
 * 							modify setValues()	
 * 								   printValues() 
 * 							defect 9601 IVTRS Version 2.2.3
 * B. Brown		03/11/2009  Add the ability to set the UseMQ property				
 * 							add setUseMQ()
 * 							defect 9944 Ver Defect_POS_E (6.0.0)
 * B. Brown		01/11/2012	Add new property for SP images.
 * 							add TXT_SP_IMAGES_DIR
 * 							add ssSPImagesDir, getter, setter
 * 							modify setValues(), printValues
 * 							defect 11232 Ver IVTRS 4.2.0
 * ---------------------------------------------------------------------
 */

/**
 * This Class is used to load properties that are unique for different
 * environments.
 *
 * @version	IVTRS 4.2.0		01/11/2012
 * @author	Clifford Chen
 * <br>Creation Date:		01/02/2002 15:59:28
 */

public class CommunicationProperty
{
	public static final int CONNECT_TO_TXDOT = 1;
	public static final int CONNECT_TO_TEXASONLINE = 2;

	private static final String MSG_CANNOT_DETERMINE_IF_TEST_OR_PROD =
		"RTS II PHASE 2, cannot determine it is in dev or test or train or prod!!!";

	// defect 9121
	private static final String MSG_COMM_PROP_AUTO_CREATE_SP_TRANS = 
		"RTS II PHASE 2, communication property, AutoCreateSpclPltPOSTrans=";
	// end defect 9121
	private static final String MSG_COMM_PROP_BATCH_ERROR_FILE_NAME =
		"RTS II PHASE 2, communication property, BatchErrorFileName=";
	private static final String MSG_COMM_PROP_BATCH_LOG_FILE_NAME =
		"RTS II PHASE 2, communication property, BatchLogFileName=";
	private static final String MSG_COMM_PROP_ENV =
		"RTS II PHASE 2, communication property, Environment=";
	private static final String MSG_COMM_PROP_EPAYURL =
		"RTS II PHASE 2, communication property, EpayURL=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_DATE_END_TAG =
		"RTS II PHASE 2, communication property, EpayVendorDateEndTag=";
	private static final String MAG_COMM_PROP_EPAY_VENDOR_DATE_START_TAG =
		"RTS II PHASE 2, communication property, EpayVendorDateStartTag=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_DISPLAY_NUM_TAG =
		"RTS II PHASE 2, communication property, EpayVendorDisplayNumTag=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_HOST =
		"RTS II PHASE 2, communication property, EpayVendorHost=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_ID_TAG =
		"RTS II PHASE 2, communication property, EpayVendorVendorIdTag=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_LOGIN_PAGE =
		"RTS II PHASE 2, communication property, EpayVendorLoginPage=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_PARSE_METHOD_VERSION =
		"RTS II PHASE 2, communication property, EpayVendorParseMethodVersion=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_PASSWORD =
		"RTS II PHASE 2, communication property, EpayVendorPassword=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_PASSWORD_TAG =
		"RTS II PHASE 2, communication property, EpayVendorPasswordTag=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_PAYMENT_TYPE_TAG =
		"RTS II PHASE 2, communication property, EpayVendorPaymentTypeTag=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_TRACE_NUM_TAG =
		"RTS II PHASE 2, communication property, EpayVendorTraceNumTag=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_USER =
		"RTS II PHASE 2, communication property, EpayVendorUser=";
	private static final String MSG_COMM_PROP_EPAY_VENDOR_USER_TAG =
		"RTS II PHASE 2, communication property, EpayVendorUserTag=";
	private static final String MSG_COMM_PROP_ERROR_FILE_NAME =
		"RTS II PHASE 2, communication property, ErrorFileName=";
	private static final String MSG_COMM_PROP_LINK_PREFIX =
		"RTS II PHASE 2, communication property, LinkPrefix=";
	private static final String MSG_COMM_PROP_LOG_FILE_NAME =
		"RTS II PHASE 2, communication property, LogFileName=";
	private static final String MSG_COMM_PROP_LOG_LEVEL =
		"RTS II PHASE 2, communication property, LogLevel=";
	private static final String MSG_COMM_PROP_MQ_ERROR_FILE_NAME =
		"RTS II PHASE 2, communication property, MQErrorFileName=";
	private static final String MSG_COMM_PROP_MQ_LOG_FILE_NAME =
		"RTS II PHASE 2, communication property,	MQLogFileName=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_CHANNEL =
		"RTS II PHASE 2, communication property, MQTexasOnlineChannel=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_HOST_1 =
		"RTS II PHASE 2, communication property, MQTexasOnlineHost1=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_PORT =
		"RTS II PHASE 2, communication property, MQTexasOnlinePort=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_POST_QUEUE =
		"RTS II PHASE 2, communication property, MQTexasOnlinePostQueue=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_QUEUE_MANAGER =
		"RTS II PHASE 2, communication property, MQTexasOnlineQueueManager=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_QUEUE_MANAGER_1 =
		"RTS II PHASE 2, communication property, MQTexasOnlineQueueManager1=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_RET_QUEUE =
		"RTS II PHASE 2, communication property, MQTexasOnlineRetrieveQueue=";
	private static final String MSG_COMM_PROP_MQ_TEXASONLINE_HOST =
		"RTS II PHASE 2, communication property, MQTexasOnlineHost=";
	private static final String MSG_COMM_PROP_MQ_TXDOT_HOST =
		"RTS II PHASE 2, communication property, MQTxdotHost=";
	private static final String MSG_COMM_PROP_MQ_TXDOT_HOST_1 =
		"RTS II PHASE 2, communication property, MQTxdotHost1=";
	private static final String MSG_COMM_PROP_MQ_TXDOT_PORT =
		"RTS II PHASE 2, communication property, MQTxdotPort=";
	private static final String MSG_COMM_PROP_MQ_TXDOT_POST_QUEUE =
		"RTS II PHASE 2, communication property, MQTxdotPostQueue=";
	private static final String MSG_COMM_PROP_MQ_TXDOT_QUEUE_MANAGER =
		"RTS II PHASE 2, communication property, MQTxdotQueueManager=";
	private static final String MSG_COMM_PROP_MQ_TXDOT_QUEUE_MANAGER_1 =
		"RTS II PHASE 2, communication property, MQTxdotQueueManager1=";
	private static final String MSG_COMM_PROP_OLDURL =
		"RTS II PHASE 2, communication property, OldURL=";
	private static final String MSG_COMM_PROP_PROXY_HOST =
		"RTS II PHASE 2, communication property, ProxyHost=";
	private static final String MSG_COMM_PROP_PROXY_PASSWORD =
		"RTS II PHASE 2, communication property, ProxyPassword=";
	private static final String MSG_COMM_PROP_PROXY_PORT =
		"RTS II PHASE 2, communication property, ProxyPort=";
	private static final String MSG_COMM_PROP_PROXY_REALM =
		"RTS II PHASE 2, communication property, ProxyRealm=";
	private static final String MSG_COMM_PROP_PROXY_USER =
		"RTS II PHASE 2, communication property, ProxyUser=";

	// defect 8368	
	//	private static final String MSG_COMM_PROP_REFUND_INFO_PROXY_ON =
	//		"RTS II PHASE 2, communication property, RefundInfoProxyOn=";	
	//	private static final String MSG_MAIN_PRINT_PROPS =
	//		"RTS II PHASE 2, print the properties";	
	private static final String MSG_COMM_PROP_REFUND_PAYMENT_PROXY_FLAG =
		"RTS II PHASE 2, communication property, RefundPaymentUseProxy=";
	private static final String MSG_COMM_PROP_VENDORPAYRPT_PROXY_FLAG =
		"RTS II PHASE 2, communication property, VendorPayRptUseProxy=";
	// end defect 8368

	private static final String MSG_COMM_PROP_REG =
		"RTS II PHASE 2, communication property,Region=";
	private static final String MSG_COMM_PROP_SERVLET_HOST_1 =
		"RTS II PHASE 2, communication property, ServletHost1=";
	private static final String MSG_COMM_PROP_SERVLET_HOST_2 =
		"RTS II PHASE 2, communication property, ServletHost2=";
	private static final String MSG_COMM_PROP_SERVLET_NAME_1 =
		"RTS II PHASE 2, communication property, ServletName1=";
	private static final String MSG_COMM_PROP_SERVLET_NAME_2 =
		"RTS II PHASE 2, communication property, ServletName2=";
	private static final String MSG_COMM_PROP_SERVLET_PORT_1 =
		"RTS II PHASE 2, communication property, ServletPort1=";
	private static final String MSG_COMM_PROP_TxOVersion =
		"RTS II PHASE 2, communication property, TxOVersion=";
	// defect 8777
	private static final String MSG_COMM_PROP_SERVLET_PORT_2 =
		"RTS II PHASE 2, communication property, ServletPort2=";
	// end defect 8777	
	private static final String MSG_COMM_PROP_TXDOT_CHANNNEL =
		"RTS II PHASE 2, communication property, MQTxdotChannel=";
	private static final String MSG_COMM_PROP_TXDOT_SERVER =
		"RTS II PHASE 2, communication property, TxdotServer=";
	private static final String MSG_COMM_PROP_TXDOT_SERVER_1 =
		"RTS II PHASE 2, communication property, TxdotServer1=";
	private static final String MSG_COMM_PROP_TEXASONLINE_END =
		"=========== RTS II PHASE 2, TexasOnline ===========>>";
	private static final String MSG_COMM_PORT_TEXASONLINE_START =
		"<<========= RTS II PHASE 2, TexasOnline =============";
	private static final String MSG_COMM_PROP_TXDOT_END =
		"========= RTS II PHASE 2, TxDOT =============>>";
	private static final String MSG_COMM_PROP_TXDOT_START =
		"<<========= RTS II PHASE 2, TxDOT =============";
	private static final String MSG_COMM_PROP_URL =
		"RTS II PHASE 2, communication property, URL=";
	private static final String MSG_COMM_PROP_USE_MQ =
		"RTS II PHASE 2, communication property, UseMQ=";
	private static final String MSG_COMM_PROP_VENDOR_REPORT_PAGE =
		"RTS II PHASE 2, communication property, EpayVendorReportPage=";
	// defect 9121
	private static final String MSG_COMM_PROP_WEB_SERVICE_HOST =
		"RTS II PHASE 2, communication property, WebServiceHost=";
	private static final String MSG_COMM_PROP_WEB_SERVICE_NAME =
		"RTS II PHASE 2, communication property, WebServiceName=";
	private static final String MSG_COMM_PROP_WEB_SERVICE_PORT =
		"RTS II PHASE 2, communication property, WebServicePort=";
	// end defect 9121
	private static final String MSG_COULD_NOT_FIND_PROPERTY_FILE =
		", RTS II PHASE 2, could not find environment property file!!!";
	private static final String MSG_MAIN_LOAD_PROPS =
		"RTS II PHASE 2, load the properties";
	private static final String MSG_RUNNING_AS_TXDOT_SERVER =
		"RTS II PHASE 2, Environment determined to be TxDOT";
	private static final String MSG_RUNNING_AS_TEXASONLINE =
		"RTS II PHASE 2, Environment determined to be TexasOnline";
	private static final String MSG_TRYING_TO_RESOLVE =
		"RTS II PHASE 2, Trying to resolve environment";

	private static final String TXT_COMM_PROP =
		"RTS II PHASE 2, communication property, ";
	private static final String TXT_NOTPARTICIPATELINK =
		"NotParticipateLink_";
	// defect 8250	
	private static final String TXT_IVTRS_VERSION = "IVTRS Version = ";
	// end defect 8250
	
	// defect 9601	
	private static final String TXT_IVTRS_VERSION_DATE = "IVTRS Version Date = ";
	// end defect 9601
	
	// defect 11232
	public static String TXT_SP_IMAGES_DIR = "Special Plates Images Directory = ";
	// end defect 11232


	private static final String TXT_DOT_PROPERTIES = ".properties";
	private static final String TXT_RTSWEBAPPS = "rtswebapps_";

	private static final String TXT_FALSE = "false";
	private static final String TXT_TRUE = "true";

	private static final String TXT_INTEST = "INTEST";
	private static final String TXT_INDEV = "INDEV";

	private static final String TXO_ENV_FILE_NAME =
		"Configuration.properties";
	private static final String TXDOT_ENV_FILE_NAME =
		"txdotrts/rtswebapps_env.properties";

	// defect 8250
	private static final String IVTRS_VERSION_FILE_NAME =
		"txdotrts/version.cfg";
	// end defect 8250	

	// Environment and Region constants
	// defect 8553
	public static final String DESKTOP = "desktop";
	public static final String TRAIN = "train";
	public static final String TXT_DEV = "dev";
	public static final String TXT_PROD = "prod";
	public static final String TXT_TEST = "test";
	public static final String TXT_TEXASONLINE = "texasonline";
	public static final String TXT_TXDOT_SERVER = "txdot";
	public static final String TXT_REGION = "Region";
	// end defect 8553

	private static String csPropertyFileName = "txdotrts/";

	// A. Common
	// Environment
	private static String csEnvironment;
	private static String csRegion;

	// B. TexasOnline
	// (1) Servlet
	private static String csServletHost1;
	private static String csServletName1;
	private static String csServletPort1;

	// Web Service
	private static String csWebServiceHost;
	private static String csWebServiceName;
	private static String csWebServicePort;

	// TxDOT Application servers
	private static String csTxdotServer;
	private static String csTxdotServer1;

	// (2) MQSeries
	private static String csMQTexasOnlineHost;
	// this is extra for test and production
	private static String csMQTexasOnlineHost1;
	private static String csMQTexasOnlineChannel;
	private static String csMQTexasOnlineQueueManager;
	// this is extra for test and production	
	private static String csMQTexasOnlineQueueManager1;
	// for posting Renewal and Refund reply, payment replay
	private static String csMQTexasOnlinePostQueue;
	// for retrieving objects coming from Txdot site
	// e.g. RefundData, PaymentData.
	private static String csMQTexasOnlineRetrieveQueue;
	private static int ciMQTexasOnlinePort;

	// (3) Misc
	// For redirecting to https
	private static String csUrl;
	private static String csOldUrl;
	// Epay
	private static String csEpayUrl;

	// C. Txdot
	// (1) Servlet
	private static String csServletHost2;
	private static String csServletName2;
	private static String csServletPort2;
	// (2) MQSeries
	private static String csMQTxdotHost;
	// This is extra for production
	private static String csMQTxdotHost1;
	private static String csMQTxdotChannel;
	private static String csMQTxdotQueueManager;
	// This is extra for production
	private static String csMQTxdotQueueManager1;
	// for posting RefundData, PaymentData
	private static String csMQTxdotPostQueue;
	// Note: Retrieving objects from Txdot site MQSerices 
	// and updating db is handled in Phase 1 by Richard.
	// so no mqTxdotRetrieveQueue defined in Phase 2.	
	private static int ciMQTxdotPort;
	// (3) Epay Vendor Report
	private static String csEpayVendorHost;
	private static String csEpayVendorLoginPage;
	private static String csEpayVendorReportPage;
	private static String csEpayVendorUser;
	private static String csEpayVendorPassword;
	// Tag names
	private static String csEpayVendorUserTag;
	private static String csEpayVendorPasswordTag;
	private static String csEpayVendorDateStartTag;
	private static String csEpayVendorDateEndTag;
	private static String csEpayVendorVendorIdTag;
	private static String csEpayVendorTraceNumTag;
	private static String csEpayVendorPaymentTypeTag;
	private static String csEpayVendorDisplayNumTag;
	private static String csEpayVendorParseMethodVersion;
	// (4) Proxy Server
	// defect ****
	// default to true so that if all else fails we will
	// use nss-webproxy

	private static String csProxyHost;
	private static int ciProxyPort;
	private static String csProxyRealm;
	private static String csProxyUser;
	private static String csProxyPassword;
	// (5) logs
	private static String csLogLevel;
	private static String csLogFileName;
	private static String csErrorFileName;
	private static String csMQLogFileName;
	private static String csMQErrorFileName;
	private static String csBatchLogFileName;
	private static String csBatchErrorFileName;
	
	// (6) Special Plates
	private static boolean cbAutoCreateSPPOSTrans = false;

	// D. Misc	
	private static boolean lbUseMQ;
	// defect 8368
	// private static boolean lbRefundProxyProxyOn = true;
	// private static boolean cbUseProxy = true;
	private static boolean cbVendorPayRptUseProxy = true;
	private static boolean cbRefundPaymentUseProxy = true;
	// end defect 8368

	// determined by csEnvironment
	private static String lsLinkPrefix;

	// defect 8777
	private static String csTxOVersion;
	// end defect 8777

	// defect 8250
	private static String csIVTRSVersion;
	// end defect 8250	
	
	// defect 9601
	private static String ssIVTRSVersionDate;
	// end defect 9601

	private static Properties caConnectionProperties = new Properties();
	
	// defect 11232
	public static String ssSPImagesDir;
	// end defect 11232

	// initialize on first reference
	static {
		init();
	}

	/**
	 * CommunicationProperty constructor comment.
	 */
	public CommunicationProperty()
	{
		super();
	}

	/**
	 * Returns cbAutoCreateSPPOSTrans
	 * 
	 * @return boolean
	 */
	public static boolean getAutoCreateSPPOSTrans()
	{
		return cbAutoCreateSPPOSTrans;
	}

	/**
	 * Returns csBatchErrorFileName
	 * 
	 * @return String
	 */
	public static String getBatchErrorFileName()
	{
		return csBatchErrorFileName;
	}

	/**
	 * Returns csBatchLogFileName
	 * 
	 * @return String
	 */
	public static String getBatchLogFileName()
	{
		return csBatchLogFileName;
	}

	/**
	 * Return the Environment value.
	 * 
	 * @return String
	 */
	public static String getEnvironment()
	{
		return csEnvironment;
	}

	/**
	 * Return the EpayURL value.
	 * 
	 * @return String
	 */
	public static String getEpayURL()
	{
		return csEpayUrl;
	}

	/**
	 * Return the EpayVendorDateEndTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorDateEndTag()
	{
		return csEpayVendorDateEndTag;
	}

	/**
	 * Return the EpayVendorDateStartTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorDateStartTag()
	{
		return csEpayVendorDateStartTag;
	}

	/**
	 * Return the EpayVendorDisplayNumTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorDisplayNumTag()
	{
		return csEpayVendorDisplayNumTag;
	}

	/**
	 * Return the EpayVendorHost value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorHost()
	{
		return csEpayVendorHost;
	}

	/**
	 * Return the EpayVendorLoginPage value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorLoginPage()
	{
		return csEpayVendorLoginPage;
	}

	/**
	 * Return the EpayVendorParseMethodVersion value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorParseMethodVersion()
	{
		return csEpayVendorParseMethodVersion;
	}

	/**
	 * Return the EpayVendorPassword value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorPassword()
	{
		return csEpayVendorPassword;
	}

	/**
	 * Return the EpayVendorPasswordTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorPasswordTag()
	{
		return csEpayVendorPasswordTag;
	}

	/**
	 * Return the EpayVendorPaymentTypeTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorPaymentTypeTag()
	{
		return csEpayVendorPaymentTypeTag;
	}

	/**
	 * Return the EpayVendorReportPage value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorReportPage()
	{
		return csEpayVendorReportPage;
	}

	/**
	 * Return the EpayVendorTraceNumTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorTraceNumTag()
	{
		return csEpayVendorTraceNumTag;
	}

	/**
	 * Return the EpayVendorUser value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorUser()
	{
		return csEpayVendorUser;
	}

	/**
	 * Return the EpayVendorUserTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorUserTag()
	{
		return csEpayVendorUserTag;
	}

	/**
	 * Return the EpayVendorVendorIdTag value.
	 * 
	 * @return String
	 */
	public static String getEpayVendorVendorIdTag()
	{
		return csEpayVendorVendorIdTag;
	}

	/**
	 * Return the ErrorFileName value.
	 * 
	 * @return String
	 */
	public static String getErrorFileName()
	{
		return csErrorFileName;
	}

	/**
	 * Return the LinkPrefix value.
	 * 
	 * @return String
	 */
	public static String getLinkPrefix()
	{
		return lsLinkPrefix;
	}

	/**
	 * Return the LogFileName value.
	 * 
	 * @return String
	 */
	public static String getLogFileName()
	{
		return csLogFileName;
	}

	/**
	 * Return the LogLevel value.
	 * 
	 * @return String
	 */
	public static String getLogLevel()
	{
		return csLogLevel;
	}

	/**
	 * Return the MQErrorFileName value.
	 * 
	 * @return String
	 */
	public static String getMQErrorFileName()
	{
		return csMQErrorFileName;
	}

	/**
	 * Return the MQLogFileName value.
	 * 
	 * @return String
	 */
	public static String getMQLogFileName()
	{
		return csMQLogFileName;
	}

	/**
	 * Return the MQTexasOnlineChannel value.
	 * 
	 * @return String
	 */
	public static String getMQTexasOnlineChannel()
	{
		return csMQTexasOnlineChannel;
	}

	/**
	 * Return the MQTexasOnlineHost value.
	 * 
	 * @return String
	 */
	public static String getMQTexasOnlineHost()
	{
		return csMQTexasOnlineHost;
	}

	/**
	 * Return the MQTexasOnlineHost1 value.
	 * 
	 * @return String
	 */
	public static String getMQTexasOnlineHost1()
	{
		return csMQTexasOnlineHost1;
	}

	/**
	 * Return the MQTexasOnlinePort value.
	 * 
	 * @return int
	 */
	public static int getMQTexasOnlinePort()
	{
		return ciMQTexasOnlinePort;
	}

	/**
	 * Return the MQTexasOnlinePostQueue value.
	 * 
	 * @return String
	 */
	public static String getMQTexasOnlinePostQueue()
	{
		return csMQTexasOnlinePostQueue;
	}

	/**
	 * Return the MQTexasOnlineQueueManager value.
	 * 
	 * @return String
	 */
	public static String getMQTexasOnlineQueueManager()
	{
		return csMQTexasOnlineQueueManager;
	}

	/**
	 * Return the MQTexasOnlineQueueManager1 value.
	 *
	 * @return String
	 */
	public static String getMQTexasOnlineQueueManager1()
	{
		return csMQTexasOnlineQueueManager1;
	}

	/**
	 * Return the MQTexasOnlineRetrieveQueue value.
	 * 
	 * @return String
	 */
	public static String getMQTexasOnlineRetrieveQueue()
	{
		return csMQTexasOnlineRetrieveQueue;
	}

	/**
	 * Return the MQTxdotChannel value.
	 * 
	 * @return String
	 */
	public static String getMQTxdotChannel()
	{
		return csMQTxdotChannel;
	}

	/**
	 * Return the MQTxdotHost value.
	 * 
	 * @return String
	 */
	public static String getMQTxdotHost()
	{
		return csMQTxdotHost;
	}

	/**
	 * Return the MQTxdotHost1 value.
	 * 
	 * @return String
	 */
	public static String getMQTxdotHost1()
	{
		return csMQTxdotHost1;
	}

	/**
	 * Return the MQTxdotPort value.
	 * 
	 * @return int
	 */
	public static int getMQTxdotPort()
	{
		return ciMQTxdotPort;
	}

	/**
	 * Return the MQTxdotPostQueue value.
	 * 
	 * @return String
	 */
	public static String getMQTxdotPostQueue()
	{
		return csMQTxdotPostQueue;
	}

	/**
	 * Return the MQTxdotQueueManager value.
	 * 
	 * @return String
	 */
	public static String getMQTxdotQueueManager()
	{
		return csMQTxdotQueueManager;
	}

	/**
	 * Return the MQTxdotQueueManager1 value.
	 * 
	 * @return String
	 */
	public static String getMQTxdotQueueManager1()
	{
		return csMQTxdotQueueManager1;
	}

	/**
	 * Return the NotParticipateCountyLink value.
	 * 
	 * @return String
	 */
	public static String getNotParticipateCountyLink(String countyNo)
	{
		return caConnectionProperties.getProperty(
			TXT_NOTPARTICIPATELINK + countyNo);
	}

	/**
	 * Return the OldURL value.
	 * 
	 * @return String
	 */
	public static String getOldURL()
	{
		return csOldUrl;
	}

	/**
	 * Return the ProxyHost value.
	 * 
	 * @return String
	 */
	public static String getProxyHost()
	{
		return csProxyHost;
	}

	/**
	 * Return the ProxyPassword value.
	 * 
	 * @return String
	 */
	public static String getProxyPassword()
	{
		return csProxyPassword;
	}

	/**
	 * Return the ProxyPort value.
	 * 
	 * @return int
	 */
	public static int getProxyPort()
	{
		return ciProxyPort;
	}

	/**
	 * Return the ProxyRealm value.
	 * 
	 * @return String
	 */
	public static String getProxyRealm()
	{
		return csProxyRealm;
	}

	/**
	 * Return the ProxyUser value.
	 * 
	 * @return String
	 */
	public static String getProxyUser()
	{
		return csProxyUser;
	}

	/**
	 * Return the RefundInfoProxyOn value.
	 * 
	 * @return boolean
	 */
	public static boolean getRefundPaymentUseProxy()
	{
		// defect 8368
		// lbRefundInfoProxyOn;
		return cbRefundPaymentUseProxy;
		// end defect 8368
	}

	/**
	 * Return the Region value.
	 * 
	 * @return String
	 */
	public static String getRegion()
	{
		return csRegion;
	}

	/**
	 * Return the ServletHost1 value.
	 * 
	 * @return String
	 */
	public static String getServletHost1()
	{
		return csServletHost1;
	}

	/**
	 * Return the ServletHost2 value.
	 * 
	 * @return String
	 */
	public static String getServletHost2()
	{
		return csServletHost2;
	}

	/**
	 * Return the ServletName1 value.
	 * 
	 * @return String
	 */
	public static String getServletName1()
	{
		return csServletName1;
	}

	/**
	 * Return the ServletName2 value.
	 * 
	 * @return String
	 */
	public static String getServletName2()
	{
		return csServletName2;
	}

	/**
	 * Return the ServletPort1 value.
	 * 
	 * @return String
	 */
	public static String getServletPort1()
	{
		return csServletPort1;
	}

	/**
	 * Return the ServletPort2 value.
	 * 
	 * @return String
	 */
	public static String getServletPort2()
	{
		return csServletPort2;
	}

	/**
	 * Return the TxOVersion value.
	 * 
	 * @return String
	 */
	public static String getTxOVersion()
	{
		return csTxOVersion;
	}

	/**
	 * Return the TxdotServer value.
	 * 
	 * @return String
	 */
	public static String getTxdotServer()
	{
		return csTxdotServer;
	}

	/**
	 * Return the TxdotServer1 value.
	 * 
	 * @return String
	 */
	public static String getTxdotServer1()
	{
		return csTxdotServer1;
	}

	/**
	 * Return the URL value.
	 * 
	 * @return String
	 */
	public static String getURL()
	{
		return csUrl;
	}
	
	/**
	 * Return the UseMQ value.
	 * 
	 * @return boolean
	 */
	public static boolean getUseMQ()
	{
		return lbUseMQ;
	}
	
	/**
	 * Set the UseMQ value.
	 * 
	 */
	public static void setUseMQ(boolean abUseMQ)
	{
		lbUseMQ = abUseMQ;
	}

	/**
	 * Used to determine if we are going to use the proxy or not.
	 * 
	 * @return boolean
	 */
	public static boolean getVendorPayRptUseProxy()
	{
		// defect 8368
		return cbVendorPayRptUseProxy;
		// end defect 8368
	}

	/**
	 * Gets the Web Service Host Name.
	 * 
	 * @return String
	 */
	public static String getWebServiceHost()
	{
		return csWebServiceHost;
	}

	/**
	 * Gets the Web Service Name.
	 * 
	 * @return String
	 */
	public static String getWebServiceName()
	{
		return csWebServiceName;
	}

	/**
	 * Gets the Web Service Port.
	 * 
	 * @return String
	 */
	public static String getWebServicePort()
	{
		return csWebServicePort;
	}

	/**
	 * Main method that is called at first load.
	 */
	private static void init()
	{
		try
		{
			// A. Common (Environment and csRegion)		
			System.out.println(MSG_TRYING_TO_RESOLVE);
			InputStream pfsInputStream = null;
			pfsInputStream =
				ClassLoader.getSystemResourceAsStream(
					TXO_ENV_FILE_NAME);
			// defect 8553	
			FileInputStream laFIS = null;
			Properties laEnvProp = new Properties();
			// end defect 8553	

			if (pfsInputStream != null)
			{
				// find the env property file in TexasOnline
				csEnvironment = TXT_TEXASONLINE;
				// defect 8925
				laEnvProp.load(pfsInputStream);
				// end defect 8925
				System.out.println(MSG_RUNNING_AS_TEXASONLINE);
			}
			else
			{
				// try to find it in Txdot 
				try
				{
					// defect 8553
					// Try to load txdot properties if texasonline is 
					// not found.

					// System.out.println(
					// "Getting property file "
					// + System.getProperty("user.dir")
					// + "/"
					// + TXDOT_ENV_FILE_NAME);

					System.out.println(
						"Getting property file" + TXDOT_ENV_FILE_NAME);

					// defect 8925
					// use getResourceAsStream to read all properties
					// files except Configuration.properties
					//					pfsInputStream =
					//						ClassLoader.getSystemResourceAsStream(
					//							TXDOT_ENV_FILE_NAME);
					pfsInputStream =
						CommunicationProperty
							.class
							.getClassLoader()
							.getResourceAsStream(
							TXDOT_ENV_FILE_NAME);
					// end defect 8925 				

					//new FileInputStream(TXDOT_ENV_FILE_NAME);

					if (pfsInputStream == null)
					{
						try
						{
							laFIS =
								new FileInputStream(TXDOT_ENV_FILE_NAME);
							if (laFIS != null)
							{
								laEnvProp.load(laFIS);
								// find the env property file in TxDOT
								csEnvironment = TXT_TXDOT_SERVER;
								System.out.println(
									MSG_RUNNING_AS_TXDOT_SERVER);
							}
						}

						catch (Exception ex)
						{
							ex.printStackTrace();
						}
						// find the env property file in TxDOT
					}
					else
					{
						laEnvProp.load(pfsInputStream);
						// find the env property file in TxDOT
						csEnvironment = TXT_TXDOT_SERVER;
						System.out.println(MSG_RUNNING_AS_TXDOT_SERVER);
					}
				}

				// end defect 8553

				catch (Exception aeEx)
				{
					aeEx.printStackTrace();
				}
			}

			// defect 8553
			if (pfsInputStream == null && laFIS == null)
				// end defect 8553
			{
				System.out.println(
					new java.util.Date()
						+ MSG_COULD_NOT_FIND_PROPERTY_FILE);
			}

			// defect 8553
			// pfsInputStream.close();
			// end defect 8553

			// if the csEnvironment file is not loaded, 
			// let it error out,
			// so we know for sure the application will not start.

			// defect 8553
			// laEnvProp loaded above
			// Properties laEnvProp = new Properties();
			// laEnvProp.load(pfsInputStream);
			// end defect 8553

			if (csEnvironment.equalsIgnoreCase(TXT_TEXASONLINE))
			{
				String lsDev = laEnvProp.getProperty(TXT_INDEV);
				String lsTest = laEnvProp.getProperty(TXT_INTEST);

				// The Configuration.properties file setting:
				// INDEV=true if in Dev
				// INTEST=true if in Test
				// INDEV=false, INTEST=false if in Prod
				// This is the way it is.

				if (lsDev != null && lsDev.equalsIgnoreCase(TXT_TRUE))
				{
					csRegion = TXT_DEV;
				}
				else if (
					lsTest != null
						&& lsTest.equalsIgnoreCase(TXT_TRUE))
				{
					csRegion = TXT_TEST;
				}
				else if (
					lsDev != null
						&& lsDev.equalsIgnoreCase(TXT_FALSE)
						&& lsTest != null
						&& lsTest.equalsIgnoreCase(TXT_FALSE))
				{
					csRegion = TXT_PROD;
				}
			}
			else
			{
				csRegion = laEnvProp.getProperty(TXT_REGION);
			}

			// defect 8553
			if (pfsInputStream != null)
			{
				pfsInputStream.close();
			}

			if (laFIS != null)
			{
				laFIS.close();
			}
			// end defect 8553

			if (csRegion == null)
			{
				System.out.println(
					MSG_CANNOT_DETERMINE_IF_TEST_OR_PROD);
			}

			// let it error out if csRegion cannot be determined.	
			csPropertyFileName =
				csPropertyFileName
					+ TXT_RTSWEBAPPS
					+ csRegion.toLowerCase()
					+ TXT_DOT_PROPERTIES;

			// defect 8553

			//			System.out.println(
			//				"Getting property file "
			//					+ System.getProperty("user.dir")
			//					+ "/"
			//					+ csPropertyFileName);
			System.out.println(
				"Getting property file" + csPropertyFileName);

			// always use getSystemResourceAsStream
			// if (csEnvironment.equalsIgnoreCase("texasonline"))

			// defect 8925
			// use getResourceAsStream to read all properties
			// files except Configuration.properties
			//			pfsInputStream =
			//				ClassLoader.getSystemResourceAsStream
			//				(csPropertyFileName);
			pfsInputStream =
				CommunicationProperty
					.class
					.getClassLoader()
					.getResourceAsStream(
					csPropertyFileName);
			// end defect 8925 		

			if (pfsInputStream == null)
			{
				try
				{
					laFIS = new FileInputStream(csPropertyFileName);
					if (laFIS != null)
					{
						// find the env property file in TxDOT
						caConnectionProperties.load(laFIS);
					}

				}

				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			else
				//pfsInputStream = new FileInputStream
				//(csPropertyFileName);			
				{
				caConnectionProperties.load(pfsInputStream);
			}

			// defect 8250

			if (pfsInputStream != null)
			{
				pfsInputStream.close();
			}

			if (laFIS != null)
			{
				laFIS.close();
			}

			// defect 8925
			// use getResourceAsStream to read all properties
			// files except Configuration.properties
			//			pfsInputStream =
			//				ClassLoader.getSystemResourceAsStream
			//				(IVTRS_VERSION_FILE_NAME);
			pfsInputStream =
				CommunicationProperty
					.class
					.getClassLoader()
					.getResourceAsStream(
					IVTRS_VERSION_FILE_NAME);
			// end defect 8925 								

			if (pfsInputStream == null)
			{
				try
				{
					laFIS =
						new FileInputStream(IVTRS_VERSION_FILE_NAME);
					if (laFIS != null)
					{
						// find the env property file in TxDOT
						caConnectionProperties.load(laFIS);
					}
				}

				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			else
				//pfsInputStream = new FileInputStream
				//(csPropertyFileName);			
				{
				caConnectionProperties.load(pfsInputStream);
			}

			// end defect 8250

			// end defect 8553

			trimValues();
			setValues();
			printValues();

			// defect 8553
			if (pfsInputStream != null)
			{
				pfsInputStream.close();
			}

			if (laFIS != null)
			{
				laFIS.close();
			}

			// end defect 8553	
		}

		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Main method this will call init.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println(MSG_MAIN_LOAD_PROPS);
	}

	/**
	 * Used to print all the values out to the console.
	 */
	private static void printValues()
	{

		// A. Common
		System.out.println(MSG_COMM_PROP_ENV + getEnvironment());
		System.out.println(MSG_COMM_PROP_REG + getRegion());

		// B. TexasOnline	
		System.out.println(MSG_COMM_PORT_TEXASONLINE_START);
		System.out.println(
			MSG_COMM_PROP_SERVLET_HOST_1 + getServletHost1());
		System.out.println(
			MSG_COMM_PROP_SERVLET_PORT_1 + getServletPort1());
		System.out.println(
			MSG_COMM_PROP_SERVLET_NAME_1 + getServletName1());
		// defect 9121
		System.out.println(
			MSG_COMM_PROP_WEB_SERVICE_HOST + getWebServiceHost());
		System.out.println(
			MSG_COMM_PROP_WEB_SERVICE_NAME + getWebServiceName());
		System.out.println(
			MSG_COMM_PROP_WEB_SERVICE_PORT + getWebServicePort());
		// end defect 9121
		System.out.println(
			MSG_COMM_PROP_TXDOT_SERVER + getTxdotServer());
		System.out.println(
			MSG_COMM_PROP_TXDOT_SERVER_1 + getTxdotServer1());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_HOST + getMQTexasOnlineHost());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_HOST_1
				+ getMQTexasOnlineHost1());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_CHANNEL
				+ getMQTexasOnlineChannel());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_QUEUE_MANAGER
				+ getMQTexasOnlineQueueManager());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_QUEUE_MANAGER_1
				+ getMQTexasOnlineQueueManager1());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_POST_QUEUE
				+ getMQTexasOnlinePostQueue());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_RET_QUEUE
				+ getMQTexasOnlineRetrieveQueue());
		System.out.println(
			MSG_COMM_PROP_MQ_TEXASONLINE_PORT + getMQTexasOnlinePort());
		System.out.println(MSG_COMM_PROP_URL + getURL());
		System.out.println(MSG_COMM_PROP_OLDURL + getOldURL());
		System.out.println(MSG_COMM_PROP_EPAYURL + getEpayURL());

		Enumeration propNames = caConnectionProperties.propertyNames();
		while (propNames.hasMoreElements())
		{
			String name = (String) propNames.nextElement();
			if (name.startsWith(TXT_NOTPARTICIPATELINK))
			{
				System.out.println(
					TXT_COMM_PROP
						+ name
						+ CommonConstant.STR_EQUAL
						+ caConnectionProperties.getProperty(name));
			}
		}

		System.out.println(MSG_COMM_PROP_TEXASONLINE_END);

		// C. Txdot
		System.out.println(MSG_COMM_PROP_TXDOT_START);
		System.out.println(
			MSG_COMM_PROP_SERVLET_HOST_2 + getServletHost2());
		System.out.println(
			MSG_COMM_PROP_SERVLET_PORT_2 + getServletPort2());
		System.out.println(
			MSG_COMM_PROP_SERVLET_NAME_2 + getServletName2());

		// defect 8777
		System.out.println(MSG_COMM_PROP_TxOVersion + getTxOVersion());
		// end defect 8777	
		System.out.println(
			MSG_COMM_PROP_MQ_TXDOT_HOST + getMQTxdotHost());
		System.out.println(
			MSG_COMM_PROP_MQ_TXDOT_HOST_1 + getMQTxdotHost1());
		System.out.println(
			MSG_COMM_PROP_TXDOT_CHANNNEL + getMQTxdotChannel());
		System.out.println(
			MSG_COMM_PROP_MQ_TXDOT_QUEUE_MANAGER
				+ getMQTxdotQueueManager());
		System.out.println(
			MSG_COMM_PROP_MQ_TXDOT_QUEUE_MANAGER_1
				+ getMQTxdotQueueManager1());
		System.out.println(
			MSG_COMM_PROP_MQ_TXDOT_POST_QUEUE + getMQTxdotPostQueue());
		System.out.println(
			MSG_COMM_PROP_MQ_TXDOT_PORT + getMQTxdotPort());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_HOST + getEpayVendorHost());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_LOGIN_PAGE
				+ getEpayVendorLoginPage());
		System.out.println(
			MSG_COMM_PROP_VENDOR_REPORT_PAGE
				+ getEpayVendorReportPage());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_USER + getEpayVendorUser());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_PASSWORD
				+ getEpayVendorPassword());
		// defect 8368
		System.out.println(
			MSG_COMM_PROP_VENDORPAYRPT_PROXY_FLAG
				+ getVendorPayRptUseProxy());
		System.out.println(
			MSG_COMM_PROP_REFUND_PAYMENT_PROXY_FLAG
				+ getRefundPaymentUseProxy());
		// end defect 8368
		System.out.println(MSG_COMM_PROP_PROXY_HOST + getProxyHost());
		System.out.println(MSG_COMM_PROP_PROXY_PORT + getProxyPort());
		System.out.println(MSG_COMM_PROP_PROXY_REALM + getProxyRealm());
		System.out.println(MSG_COMM_PROP_PROXY_USER + getProxyUser());
		System.out.println(
			MSG_COMM_PROP_PROXY_PASSWORD + getProxyPassword());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_USER_TAG
				+ getEpayVendorUserTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_PASSWORD_TAG
				+ getEpayVendorPasswordTag());
		System.out.println(
			MAG_COMM_PROP_EPAY_VENDOR_DATE_START_TAG
				+ getEpayVendorDateStartTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_DATE_END_TAG
				+ getEpayVendorDateEndTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_ID_TAG
				+ getEpayVendorVendorIdTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_TRACE_NUM_TAG
				+ getEpayVendorTraceNumTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_PAYMENT_TYPE_TAG
				+ getEpayVendorPaymentTypeTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_DISPLAY_NUM_TAG
				+ getEpayVendorDisplayNumTag());
		System.out.println(
			MSG_COMM_PROP_EPAY_VENDOR_PARSE_METHOD_VERSION
				+ getEpayVendorParseMethodVersion());
		// logs
		System.out.println(MSG_COMM_PROP_LOG_LEVEL + getLogLevel());
		System.out.println(
			MSG_COMM_PROP_LOG_FILE_NAME + getLogFileName());
		System.out.println(
			MSG_COMM_PROP_ERROR_FILE_NAME + getErrorFileName());
		System.out.println(
			MSG_COMM_PROP_MQ_LOG_FILE_NAME + getMQLogFileName());
		System.out.println(
			MSG_COMM_PROP_MQ_ERROR_FILE_NAME + getMQErrorFileName());
		System.out.println(
			MSG_COMM_PROP_BATCH_LOG_FILE_NAME + getBatchLogFileName());
		System.out.println(
			MSG_COMM_PROP_BATCH_ERROR_FILE_NAME
				+ getBatchErrorFileName());
		// defect 9121
		System.out.println(
			MSG_COMM_PROP_AUTO_CREATE_SP_TRANS
				+ getAutoCreateSPPOSTrans());
		// end defect 9121

		System.out.println(MSG_COMM_PROP_TXDOT_END);

		// D. Misc
		// defect 8368
		//System.out.println(
		//			MSG_COMM_PROP_REFUND_INFO_PROXY_ON
		//				+ getRefundInfoProxyOn());
		// end defect 8368

		System.out.println(MSG_COMM_PROP_USE_MQ + getUseMQ());
		System.out.println(MSG_COMM_PROP_LINK_PREFIX + getLinkPrefix());
		// defect 8250
		System.out.println(TXT_IVTRS_VERSION + getIVTRSVersion());
		// end defect 8250
		
		// defect 9601
		System.out.println(TXT_IVTRS_VERSION_DATE + getIVTRSVersionDate());
		// end defect 9601
		
		// defect 11232
		System.out.println(TXT_SP_IMAGES_DIR + getSPImagesDir());
		// end defect 11232
	}

	/**
	 * This allows anyone to call this method to set
	 * the Link Prefix.  This is usually the Context.
	 * This is called at the top of the main menu jsp.
	 * 
	 * @param asLinkPrefix String
	 */
	public static void setLinkPrefix(String asLinkPrefix)
	{
		lsLinkPrefix = asLinkPrefix;
	}

	/**
	 * Change the communication properties to communicate
	 * to another resource.
	 */
	private static void setValues()
	{
		// A. Common (Environment and csRegion, done is init)
		// B. TexasOnline
		// (1) Server
		csServletHost1 =
			caConnectionProperties.getProperty("ServletHost_1");
		csServletPort1 =
			caConnectionProperties.getProperty("ServletPort_1");
		csServletName1 =
			caConnectionProperties.getProperty("ServletName_1");

		// defect 9121
		// Web Service
		csWebServiceHost =
			caConnectionProperties.getProperty("WebServiceHost");
		csWebServiceName =
			caConnectionProperties.getProperty("WebServiceName");
		csWebServicePort =
			caConnectionProperties.getProperty("WebServicePort");
		// end defect 9121

		// TxDOT app servers 
		csTxdotServer =
			caConnectionProperties.getProperty("TxdotServer");
		csTxdotServer1 =
			caConnectionProperties.getProperty("TxdotServer1");

		// (1) MQ Series
		csMQTexasOnlineHost =
			caConnectionProperties.getProperty("MQTexasOnlineHost");
		// This is extra for test, production	
		csMQTexasOnlineHost1 =
			caConnectionProperties.getProperty("MQTexasOnlineHost1");
		csMQTexasOnlineChannel =
			caConnectionProperties.getProperty("MQTexasOnlineChannel");
		csMQTexasOnlineQueueManager =
			caConnectionProperties.getProperty(
				"MQTexasOnlineQueueManager");
		// This is extra for test, production	
		csMQTexasOnlineQueueManager1 =
			caConnectionProperties.getProperty(
				"MQTexasOnlineQueueManager1");
		csMQTexasOnlinePostQueue =
			caConnectionProperties.getProperty(
				"MQTexasOnlinePostQueue");
		csMQTexasOnlineRetrieveQueue =
			caConnectionProperties.getProperty(
				"MQTexasOnlineRetrieveQueue");
		ciMQTexasOnlinePort =
			(new Integer(caConnectionProperties
				.getProperty("MQTexasOnlinePort")))
				.intValue();

		// for redirecting to https
		csUrl = caConnectionProperties.getProperty("URL");
		csOldUrl = caConnectionProperties.getProperty("OldURL");
		// Epay
		csEpayUrl = caConnectionProperties.getProperty("EpayURL");

		// C. Txdot
		// (1) Server
		csServletHost2 =
			caConnectionProperties.getProperty("ServletHost_2");
		csServletPort2 =
			caConnectionProperties.getProperty("ServletPort_2");
		csServletName2 =
			caConnectionProperties.getProperty("ServletName_2");

		// defect 8777
		csTxOVersion = caConnectionProperties.getProperty("TxOVersion");
		// end defect 8777		

		// (2) MQSeries 	
		csMQTxdotHost =
			caConnectionProperties.getProperty("MQTxdotHost");
		// This is extra for production
		csMQTxdotHost1 =
			caConnectionProperties.getProperty("MQTxdotHost1");
		csMQTxdotChannel =
			caConnectionProperties.getProperty("MQTxdotChannel");

		csMQTxdotQueueManager =
			caConnectionProperties.getProperty("MQTxdotQueueManager");
		// This is extra for production
		csMQTxdotQueueManager1 =
			caConnectionProperties.getProperty("MQTxdotQueueManager1");
		csMQTxdotPostQueue =
			caConnectionProperties.getProperty("MQTxdotPostQueue");
		ciMQTxdotPort =
			(new Integer(caConnectionProperties
				.getProperty("MQTxdotPort")))
				.intValue();

		// (3) Epay Vendor Report
		csEpayVendorHost =
			caConnectionProperties.getProperty("EpayVendorHost");
		csEpayVendorLoginPage =
			caConnectionProperties.getProperty("EpayVendorLoginPage");
		csEpayVendorReportPage =
			caConnectionProperties.getProperty("EpayVendorReportPage");
		csEpayVendorUser =
			caConnectionProperties.getProperty("EpayVendorUser");
		csEpayVendorPassword =
			caConnectionProperties.getProperty("EpayVendorPassword");

		csEpayVendorUserTag =
			caConnectionProperties.getProperty("EpayVendorUserTag");
		csEpayVendorPasswordTag =
			caConnectionProperties.getProperty("EpayVendorPasswordTag");
		csEpayVendorDateStartTag =
			caConnectionProperties.getProperty(
				"EpayVendorDateStartTag");
		csEpayVendorDateEndTag =
			caConnectionProperties.getProperty("EpayVendorDateEndTag");
		csEpayVendorVendorIdTag =
			caConnectionProperties.getProperty("EpayVendorVendorIdTag");
		csEpayVendorTraceNumTag =
			caConnectionProperties.getProperty("EpayVendorTraceNumTag");
		csEpayVendorPaymentTypeTag =
			caConnectionProperties.getProperty(
				"EpayVendorPaymentTypeTag");
		csEpayVendorDisplayNumTag =
			caConnectionProperties.getProperty(
				"EpayVendorDisplayNumTag");
		csEpayVendorParseMethodVersion =
			caConnectionProperties.getProperty(
				"EpayVendorParseMethodVersion");

		// (4) Proxy Server
		// defect 8368
		// Get the value that determines if we are going to use the
		// proxy or not.
		String lsVendorPaymentRptUseProxy =
			caConnectionProperties.getProperty(
				"VendorPaymentRptUseProxy");

		if (lsVendorPaymentRptUseProxy != null)
		{
			cbVendorPayRptUseProxy =
				(new Boolean(lsVendorPaymentRptUseProxy))
					.booleanValue();
		}
		// end defect 8368

		csProxyHost = caConnectionProperties.getProperty("ProxyHost");
		ciProxyPort =
			(new Integer(caConnectionProperties
				.getProperty("ProxyPort")))
				.intValue();
		csProxyRealm = caConnectionProperties.getProperty("ProxyRealm");
		csProxyUser = caConnectionProperties.getProperty("ProxyUser");
		csProxyPassword =
			caConnectionProperties.getProperty("ProxyPassword");

		// (5) log and error log file names
		csLogLevel = caConnectionProperties.getProperty("LogLevel");
		csLogFileName =
			caConnectionProperties.getProperty("LogFileName");
		csErrorFileName =
			caConnectionProperties.getProperty("ErrorFileName");
		csMQLogFileName =
			caConnectionProperties.getProperty("MQLogFileName");
		csMQErrorFileName =
			caConnectionProperties.getProperty("MQErrorFileName");
		csBatchLogFileName =
			caConnectionProperties.getProperty("BatchLogFileName");
		csBatchErrorFileName =
			caConnectionProperties.getProperty("BatchErrorFileName");
			
		// defect 9121
		// (6) Special Plates
		String lsAutoCreateSPPOSTrans =
			caConnectionProperties.getProperty(
				"AutoCreateSpclPltPOSTrans");
		if (lsAutoCreateSPPOSTrans != null)
		{
			cbAutoCreateSPPOSTrans =
				(new Boolean(lsAutoCreateSPPOSTrans)).booleanValue();
		}
		// end defect 9121

		// D. Misc
		lbUseMQ =
			(new Boolean(caConnectionProperties.getProperty("UseMQ")))
				.booleanValue();
		// defect 8368
		//		String refundInfoProxy =
		//		caConnectionProperties.getProperty("RefundInfoProxyOn");
		//		if (refundInfoProxy != null)
		//		{
		//			lbRefundInfoProxyOn =
		//				(new Boolean(refundInfoProxy)).booleanValue();
		//		}		
		String lsRefundPaymentUseProxy =
			caConnectionProperties.getProperty("RefundPaymentUseProxy");
		if (lsRefundPaymentUseProxy != null)
		{
			cbRefundPaymentUseProxy =
				(new Boolean(lsRefundPaymentUseProxy)).booleanValue();
			// end defect 8368		
		}

		// defect 8925
		//		if (csEnvironment.equalsIgnoreCase(TXT_TXDOT_SERVER))
		//		{
		//			lsLinkPrefix = CommonConstant.STR_SPACE_EMPTY;
		//		}
		//		else
		//		{
		lsLinkPrefix = "/NASApp/txdotrts";
		//		}
		// end defect 8925

		// defect 8250
		csIVTRSVersion =
			caConnectionProperties.getProperty("IVTRSVersionNo");
		// end defect 8250
		
		// defect 9601
		ssIVTRSVersionDate =
			caConnectionProperties.getProperty("IVTRSVersionDate");
		// end defect 9601
		
		// defect 11232
		ssSPImagesDir =
			caConnectionProperties.getProperty("spimagesdir");
		// end defect 11232
	}

	/**
	 * Trims the values in the properties file.
	 */
	private static void trimValues()
	{
		Enumeration propNames = caConnectionProperties.propertyNames();
		while (propNames.hasMoreElements())
		{
			String lsName = (String) propNames.nextElement();
			String lsValue = caConnectionProperties.getProperty(lsName);

			if (lsValue != null)
			{
				lsValue = lsValue.trim();
				caConnectionProperties.setProperty(lsName, lsValue);
			}
		}
	}
	/**
	 * Return the csIVTRSVersion value.
	 * This is called from the txdot_vtr_help.jsp
	 * 
	 * @return String
	 */
	public static String getIVTRSVersion()
	{
		return csIVTRSVersion;
	}

	/**
	 * This allows the csIVTRSVersion to be set from 
	 * CommunicationPropety file read
	 * 
	 * @param asIVTRSVersion String
	 */
	public static void setIVTRSVersion(String asIVTRSVersion)
	{
		csIVTRSVersion = asIVTRSVersion;
	}

	/**
	 * get the ssIVTRSVersionDate 
	 * 
	 * @return String
	 */
	public static String getIVTRSVersionDate()
	{
		return ssIVTRSVersionDate;
	}

	/**
	 * set the IVTRSVersionDate gotten from the version.cfg file
	 * 
	 * @param string asIVTRSVersionDate
	 */
	public static void setIVTRSVersionDate(String asIVTRSVersionDate)
	{
		ssIVTRSVersionDate = asIVTRSVersionDate;
	}

	/**
	 * @return the ssSPImagesDir
	 */
	public static String getSPImagesDir()
	{
		return ssSPImagesDir;
	}

	/**
	 * @param ssSPImagesDir the ssSPImagesDir to set
	 */
	public static void setSPImagesDir(String asSPImagesDir)
	{
		CommunicationProperty.ssSPImagesDir = asSPImagesDir;
	}

}
