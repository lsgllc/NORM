package com.txdot.isd.rts.services.webapps.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/*
 *
 * WebAgentProperty.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Brown		02/17/2011  Added this class from CommunitcationProperty			
 * 							defect 10727 Ver POS_670
 * K McKee      10/10/2011  Commented out WebAgent Version and Date
 *                          These are handled on WebAgent side and
 *                          were null on POS
 *                          defect 10729 Ver POS-690 
 * ---------------------------------------------------------------------
 */

/**
 * This Class is used to load properties that are unique for different
 * environments.
 *
 * @version	POS_670			02/17/2011
 * @author	Bob Brown
 * <br>Creation Date:		02/17/2011 13:50:00
 */

public class WebAgentProperty
{
	private static final Logger log = Logger.getLogger(WebAgentProperty.class);

	public static final int CONNECT_TO_TXDOT = 1;
	public static final int CONNECT_TO_TEXASONLINE = 2;

	private static final String MSG_CANNOT_DETERMINE_IF_TEST_OR_PROD =
		"WebAgent, cannot determine it is in dev or test or train or prod!!!";

	private static final String MSG_COMM_PROP_ENV =
		"WebAgent, communication property, Environment=";
	private static final String MSG_COMM_PROP_ERROR_FILE_NAME =
		"WebAgent, communication property, ErrorFileName=";
	private static final String MSG_COMM_PROP_LINK_PREFIX =
		"WebAgent, communication property, LinkPrefix=";
		
	private static final String MSG_COMM_PROP_LOG_FILE_NAME =
		"WebAgent, communication property, LogFileName=";
	private static final String MSG_COMM_PROP_LOG_LEVEL =
		"WebAgent, communication property, LogLevel=";
		
	private static final String MSG_COMM_PROP_REG =
		"WebAgent, communication property,Region=";
	
	private static final String MSG_COMM_PROP_SERVLET_HOST_1 =
		"WebAgent, communication property, ServletHost1=";
	private static final String MSG_COMM_PROP_SERVLET_HOST_2 =
		"WebAgent, communication property, ServletHost2=";
	private static final String MSG_COMM_PROP_SERVLET_NAME_1 =
		"WebAgent, communication property, ServletName1=";
	private static final String MSG_COMM_PROP_SERVLET_NAME_2 =
		"WebAgent, communication property, ServletName2=";
	private static final String MSG_COMM_PROP_SERVLET_PORT_1 =
		"WebAgent, communication property, ServletPort1=";
	private static final String MSG_COMM_PROP_SERVLET_PORT_2 =
		"WebAgent, communication property, ServletPort2=";	
		
	private static final String MSG_COMM_PROP_WEB_SERVICE_HOST =
		"WebAgent, communication property, WebServiceHost=";
	private static final String MSG_COMM_PROP_WEB_SERVICE_NAME =
		"WebAgent, communication property, WebServiceName=";
	private static final String MSG_COMM_PROP_WEB_SERVICE_PORT =
		"WebAgent, communication property, WebServicePort=";
	private static final String MSG_COULD_NOT_FIND_PROPERTY_FILE =
		", WebAgent, could not find environment property file!!!";
	private static final String MSG_MAIN_LOAD_PROPS =
		"WebAgent, load the properties";
	private static final String MSG_RUNNING_AS_TXDOT_SERVER =
		"WebAgent, Environment determined to be TxDOT";
	private static final String MSG_RUNNING_AS_TEXASONLINE =
		"WebAgent, Environment determined to be TexasOnline";
	private static final String MSG_TRYING_TO_RESOLVE =
		"WebAgent, Trying to resolve environment";
	private static final String MSG_COMM_PORT_TEXASONLINE_START =
			"<<========= WebAgent, TexasOnline =============";
	private static final String MSG_COMM_PROP_TXDOT_END =
		"========= WebAgent, TxDOT =============>>";
	private static final String MSG_COMM_PROP_TEXASONLINE_END =
		"=========== WebAgent, TexasOnline ===========>>";
	private static final String TXT_COMM_PROP =
		"WebAgent, communication property, ";
	private static final String MSG_COMM_PROP_TXDOT_START =
		"<<========= WebAgent, TxDOT =============";	
/*	private static final String TXT_WEBAGENT_VERSION = "WEBAGENT Version = ";
	private static final String TXT_WEBAGENT_VERSION_DATE = "WEBAGENT Version Date = ";*/

	private static final String TXT_DOT_PROPERTIES = ".properties";
	private static final String TXT_RTSWEBAPPS = "rtswebapps_";

	private static final String TXT_FALSE = "false";
	private static final String TXT_TRUE = "true";

	private static final String TXT_INTEST = "INTEST";
	private static final String TXT_INDEV = "INDEV";

	private static final String TXO_ENV_FILE_NAME =
		"webagent.properties";
	private static final String TXDOT_ENV_FILE_NAME =
		"txdotrts/rtswebapps_env.properties";

	public static final String DESKTOP = "desktop";
	public static final String TRAIN = "train";
	public static final String TXT_DEV = "dev";
	public static final String TXT_PROD = "prod";
	public static final String TXT_TEST = "test";
	public static final String TXT_TEXASONLINE = "texasonline";
	public static final String TXT_TXDOT_SERVER = "txdot";
	public static final String TXT_REGION = "Region";
	
	private static final String TXT_URL =  "Url = ";    
	private static final String TXT_INPUTDIR = "InputDir = ";  
	private static final String TXT_OUTPUTDIR = "OutputDir = ";
	private static final String TXT_JETPCLCMD = "JetPclCmd = ";

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

	// C. Txdot
	// (1) Servlet
	private static String csServletHost2;
	private static String csServletName2;
	private static String csServletPort2;
	// (5) logs
	private static String csLogLevel;
	private static String csLogFileName;
	private static String csErrorFileName;
	private static String csMQLogFileName;
	private static String csMQErrorFileName;
	private static String csBatchLogFileName;
	private static String csBatchErrorFileName;
	
	private static String csWebAgentPropertyFileName =
		"txdotrts/webagent_version.cfg";
	
	// determined by csEnvironment
	private static String ssLinkPrefix = "/WebAgent";;

	private static Properties caConnectionProperties = new Properties();
	
	private static String ssWebAgentVersion;
	private static String ssWebAgentVersionDate;
	
	private static String ssUrl;
	private static String ssInputDir;
	private static String ssOutputDir;
	private static String ssJetPclCmd;

	// initialize on first reference
	static {
		init();
	}

	/**
	 * WebAgentProperty constructor comment.
	 */
	public WebAgentProperty()
	{
		super();
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
		return ssLinkPrefix;
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
		// A. Common (Environment and csRegion)		
		log.info(MSG_TRYING_TO_RESOLVE);
		InputStream pfsInputStream = null;
		pfsInputStream = ClassLoader.getSystemResourceAsStream(TXO_ENV_FILE_NAME);
		FileInputStream laFIS = null;
		Properties laEnvProp = new Properties();

		// Check to see if we are in the TxO environment first.
		// if the webagent.properties file exists, we are deemed to
		// be in the TxO environment 
		if (pfsInputStream != null)
		{
			log.info("WebAgent: Established property file" + TXO_ENV_FILE_NAME);
			// find the env property file in TexasOnline
			csEnvironment = TXT_TEXASONLINE;
			try
			{
				laEnvProp.load(pfsInputStream);
			}
			catch (IOException exception)
			{
				log.error(exception.getStackTrace().toString());
			}
			log.info("WebAgent: Loaded property file" + TXO_ENV_FILE_NAME);
			log.info(MSG_RUNNING_AS_TEXASONLINE);
		}
		else
		{
			// try to find it in Txdot 
			try
			{
				log.info("Getting property file" + TXDOT_ENV_FILE_NAME);

				pfsInputStream =
					WebAgentProperty.class.getClassLoader().getResourceAsStream(
						TXDOT_ENV_FILE_NAME);

				if (pfsInputStream == null)
				{
					laFIS = new FileInputStream(TXDOT_ENV_FILE_NAME);
					if (laFIS != null)
					{
						laEnvProp.load(laFIS);
						// find the env property file in TxDOT
						csEnvironment = TXT_TXDOT_SERVER;
						log.info(MSG_RUNNING_AS_TXDOT_SERVER);
					}

					// find the env property file in TxDOT
				}
				else
				{
					laEnvProp.load(pfsInputStream);
					// find the env property file in TxDOT
					csEnvironment = TXT_TXDOT_SERVER;
					log.info(MSG_RUNNING_AS_TXDOT_SERVER);
				}
			}
			catch (FileNotFoundException fileNotFoundException)
			{
				log.error(fileNotFoundException.getStackTrace().toString());
			}
			catch (IOException exception)
			{
				log.error(exception.getStackTrace().toString());
			}
		}

		if (pfsInputStream == null && laFIS == null)
		{
			log.info(new java.util.Date() + MSG_COULD_NOT_FIND_PROPERTY_FILE);
		}

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
				log.info("Webagent DEV ==> INDEV=true, INTEST=false");
			}
			else if (lsTest != null && lsTest.equalsIgnoreCase(TXT_TRUE))
			{
				csRegion = TXT_TEST;
				log.info("Webagent TEST ==> INDEV=false, INTEST=true");
			}
			else if (
				lsDev != null
					&& lsDev.equalsIgnoreCase(TXT_FALSE)
					&& lsTest != null
					&& lsTest.equalsIgnoreCase(TXT_FALSE))
			{
				csRegion = TXT_PROD;
				log.info("Webagent PROD ! ==> INDEV=false, INTEST=false");
			}
		}
		else
		{
			csRegion = laEnvProp.getProperty(TXT_REGION);
		}

		try
		{
			if (pfsInputStream != null)
			{
				pfsInputStream.close();
			}

			if (laFIS != null)
			{
				laFIS.close();
			}

			if (csRegion == null)
			{
				log.info(MSG_CANNOT_DETERMINE_IF_TEST_OR_PROD);
			}

			// let it error out if csRegion cannot be determined.	
			csPropertyFileName =
				csPropertyFileName
					+ TXT_RTSWEBAPPS
					+ csRegion.toLowerCase()
					+ TXT_DOT_PROPERTIES;

			log.info("Getting property file" + csPropertyFileName);

			pfsInputStream =
				WebAgentProperty.class.getClassLoader().getResourceAsStream(
					csPropertyFileName);

			if (pfsInputStream == null)
			{
				laFIS = new FileInputStream(csPropertyFileName);
				if (laFIS != null)
				{
					// find the env property file in TxDOT
					caConnectionProperties.load(laFIS);
				}

			}

			else
			{
				caConnectionProperties.load(pfsInputStream);
			}
			if (pfsInputStream != null)
			{
				pfsInputStream.close();
			}

			if (laFIS != null)
			{
				laFIS.close();
			}
		}
		catch (FileNotFoundException fileNotFoundException)
		{
			log.error(fileNotFoundException.getStackTrace().toString());
		}
		catch (IOException exception)
		{
			log.error(exception.getStackTrace().toString());
		}

		//
		//			The below code will be used when Webagent is ready to
		//			start using a "txdotrts/webagent_version.cfg" file
		//			
		//			pfsInputStream =
		//				CommunicationProperty
		//					.class
		//					.getClassLoader()
		//					.getResourceAsStream(
		//					WEBAGENT_VERSION_FILE_NAME);							
		//
		//			if (pfsInputStream == null)
		//			{
		//				try
		//				{
		//					laFIS =
		//						new FileInputStream(WEBAGENT_VERSION_FILE_NAME);
		//					if (laFIS != null)
		//					{
		//						// find the env property file in TxDOT
		//						caConnectionProperties.load(laFIS);
		//					}
		//				}
		//
		//				catch (Exception ex)
		//				{
		//					ex.printStackTrace();
		//				}
		//			}
		//			else
		//				//pfsInputStream = new FileInputStream
		//				//(csPropertyFileName);			
		//				{
		//				caConnectionProperties.load(pfsInputStream);
		//			}
		trimValues();
		setValues();
		printValues();
		//          The below closes would be for the above
		//			"txdotrts/webagent_version.cfg" file
		//			if (pfsInputStream != null)
		//			{
		//				pfsInputStream.close();
		//			}
		//
		//			if (laFIS != null)
		//			{
		//				laFIS.close();
		//			}

	}

	/**
	 * Main method this will call init.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		log.info(MSG_MAIN_LOAD_PROPS);
	}

	/**
	 * Used to print all the values out to the console.
	 * Use log4j level 'debug' to view all values.
	 */
	private static void printValues()
	{

		// A. Common
		log.info(MSG_COMM_PROP_ENV + getEnvironment());
		log.info(MSG_COMM_PROP_REG + getRegion());

		// B. TexasOnline	
		log.debug(MSG_COMM_PORT_TEXASONLINE_START);
		log.debug(MSG_COMM_PROP_SERVLET_HOST_1 + getServletHost1());
		log.debug(MSG_COMM_PROP_SERVLET_PORT_1 + getServletPort1());
		log.debug(MSG_COMM_PROP_SERVLET_NAME_1 + getServletName1());
		log.debug(MSG_COMM_PROP_WEB_SERVICE_HOST + getWebServiceHost());
		log.debug(MSG_COMM_PROP_WEB_SERVICE_NAME + getWebServiceName());
		log.debug(MSG_COMM_PROP_WEB_SERVICE_PORT + getWebServicePort());
		log.debug(MSG_COMM_PROP_TEXASONLINE_END);

		// C. Txdot
		log.debug(MSG_COMM_PROP_TXDOT_START);
		log.debug(MSG_COMM_PROP_SERVLET_HOST_2 + getServletHost2());
		log.debug(MSG_COMM_PROP_SERVLET_PORT_2 + getServletPort2());
		log.debug(MSG_COMM_PROP_SERVLET_NAME_2 + getServletName2());

		// logs
		log.debug(MSG_COMM_PROP_LOG_LEVEL + getLogLevel());
		log.debug(MSG_COMM_PROP_LOG_FILE_NAME + getLogFileName());
		log.debug(MSG_COMM_PROP_ERROR_FILE_NAME + getErrorFileName());
		log.debug(MSG_COMM_PROP_TXDOT_END);
		log.debug(MSG_COMM_PROP_LINK_PREFIX + getLinkPrefix());
/*		log.debug(TXT_WEBAGENT_VERSION + getWebAgentVersion());
		log.debug(TXT_WEBAGENT_VERSION_DATE + getWebAgentVersionDate());*/
		log.debug(TXT_URL + getUrl());
		log.debug(TXT_INPUTDIR + getInputDir());
		log.debug(TXT_OUTPUTDIR + getOutputDir());
		log.debug(TXT_JETPCLCMD + getJetPclCmd());
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
		ssLinkPrefix = asLinkPrefix;
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

		// Web Service
		csWebServiceHost =
			caConnectionProperties.getProperty("WebServiceHost");
		csWebServiceName =
			caConnectionProperties.getProperty("WebServiceName");
		csWebServicePort =
			caConnectionProperties.getProperty("WebServicePort");
		// C. Txdot
		// (1) Server
		csServletHost2 =
			caConnectionProperties.getProperty("ServletHost_2");
		csServletPort2 =
			caConnectionProperties.getProperty("ServletPort_2");
		csServletName2 =
			caConnectionProperties.getProperty("ServletName_2");


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
			
		// (6) Special Plates
		String lsAutoCreateSPPOSTrans =
			caConnectionProperties.getProperty(
				"AutoCreateSpclPltPOSTrans");		
		String lsRefundPaymentUseProxy =
			caConnectionProperties.getProperty("RefundPaymentUseProxy");
			
		// new Webagent properties
		ssUrl =
			caConnectionProperties.getProperty("Url");	
		ssInputDir =
			caConnectionProperties.getProperty("InputDir");	
		ssOutputDir =
			caConnectionProperties.getProperty("OutputDir");	
		ssJetPclCmd =
			caConnectionProperties.getProperty("JetPclCmd");		
		
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
	 * Return the WebAgentVersion value.
	 * 
	 * @return String
	 */
	public static String getWebAgentVersion()
	{
		return ssWebAgentVersion;
	}

	/**
	 * This allows the csWebAgentVersion to be set from 
	 * WebAgentPropety file read
	 * 
	 * @param asWebAgentVersion String
	 */
	public static void setWebAgentVersion(String asWebAgentVersion)
	{
		ssWebAgentVersion = asWebAgentVersion;
	}

	/**
	 * get the ssWebAgentVersionDate 
	 * 
	 * @return String
	 */
	public static String getWebAgentVersionDate()
	{
		return ssWebAgentVersionDate;
	}

	/**
	 * set the WebAgentVersionDate gotten from the version.cfg file
	 * 
	 * @param string asWebAgentVersionDate
	 */
	public static void setWebAgentVersionDate(String asWebAgentVersionDate)
	{
		ssWebAgentVersionDate = asWebAgentVersionDate;
	}

	/**
	 * get the input directory for the file to convert to PDF
	 * 
	 * @return String ssInputDir
	 */
	public static String getInputDir() {
		return ssInputDir;
	}

	/**
	 * get the JetPclCmd
	 * 
	 * @return String ssJetPclCmd
	 */
	public static String getJetPclCmd() {
		return ssJetPclCmd;
	}

	/**
	 * get the output directory for the file to convert to PDF
	 * 
	 * @return String ssOutputDir
	 */
	public static String getOutputDir() {
		return ssOutputDir;
	}

	/**
	 * get the URL for WebAgent retrieval of PDFreceipt
	 * 
	 * @return String ssUrl
	 */
	public static String getUrl() {
		return ssUrl;
	}

	/**
	 * set the input directory for the file to convert to PDF
	 * 
	 * @param String asInputDir
	 */
	public static void setInputDir(String asInputDir) {
		ssInputDir = asInputDir;
	}

	/**
	 * set the jet pcl command for receipt file conversion
	 * 
	 * @param String asJetPclCmd
	 */
	public static void setJetPclCmd(String asJetPclCmd) {
		ssJetPclCmd = asJetPclCmd;
	}

	/**
	 * set the output directory for the file to convert to PDF
	 * 
	 * @param String asOutputDir
	 */
	public static void setOutputDir(String asOutputDir) {
		ssOutputDir = asOutputDir;
	}

	/**
	 * set the URL for the receipt file URL for WebAgent
	 * 
	 * @param String asUrl
	 */
	public static void setUrl(String asUrl) {
		ssUrl = asUrl;
	}

}
