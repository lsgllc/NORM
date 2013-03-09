package com.txdot.isd.rts.server.title.blackbook;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.txdot.isd.rts.server.util.ProxyAuthenticator;
import com.txdot.isd.rts.services.data.PresumptiveValueData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * BlackBookClient.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		08/28/2006	Created Class
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		09/15/2006	Added two new properties to the server.cfg.
 * 							Used to lookup presumptive value and used
 * 							these values for the url and the prefix to
 * 							the customer value.
 * 							add CUSTOMER_TAG
 * 							remove BLACKBOOK_URL
 * 							modify BlackBookClient(), 
 * 								getPrivatePartyValue()
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		09/19/2006	Remove the System Properties for proxyHost 
 * 							and proxyPort so that we will never try to 
 * 							use a proxy server when we are not in the 
 * 							development environment.
 * 							add HTTP_PROXY_HOST, HTTP_PROXY_PORT
 * 							modify BlackBookClient()
 * 							defect 8943 Ver 5.2.5
 * T Pederson	08/08/2008	Added security key to the parameters    
 * 							for presumptive value web lookup.  New 
 * 							requirement from vendor selected 
 * 							(pentondata.com).
 * 							modify buildParms()
 *							defect 9748 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * <p>This Class makes a call to the vendors XML Service and returns
 * the private party value along with year, make, and model.</p>
 * 
 * <p>Since we know that when we are in production we will not need a 
 * proxy we will not set the default Authenticator.  We check the OS
 * Name to determine if we are in production.  If the OS Name has 
 * windows in it we know we are in development.  If we are in 
 * development then we will set the default Authenticator and will 
 * prompt for any of the missing properties.</p>
 * 
 * <p>If you do not want to be prompted in development then make sure 
 * that your server system properties have all of the fields below 
 * setup correctly:
 * 
 * <code>
 * <br>http.proxyHost
 * <br>http.proxyPort
 * <br>http.proxyUser
 * <br>http.proxyPassword</code></p>
 * 
 *
 * @version	MyPlates_POS	08/08/2008
 * @author	Jeff Seifert
 * <br>Creation Date:		08/28/2006 12:20:00
 */

public class BlackBookClient
{
	private static final String CUSTOMER_TAG = "cust";
	//private static final String BLACKBOOK_URL =
	//	"http://www.blackbookportals.com/bb/products/texasws.asp";
	// defect 8943
	public static final String HTTP_PROXY_HOST = "http.proxyHost";
	public static final String HTTP_PROXY_PORT = "http.proxyPort";
	// end defect 8943
	private static final String HTTPS = "HTTPS";
	private static final String MAKE_TAG = "make";
	private static final String MILES_TAG = "miles";
	private static final String MODEL_TAG = "model";
	private static final String OS_NAME = "os.name";
	private static final String PARM_SEPARATOR = "&";
	private static final String PRIVATE_PARTY_TAG = "privatepartyvalue";
	private static final String STR_PROTOCOL_HANDLER_PROPERTY =
		"java.protocol.handler.pkgs";
	private static final String STR_PROTOCOL_HANDLER_VALUE =
		"com.sun.net.ssl.internal.www.protocol";
	private final static String URL_PARM_SEPARATOR = "?";
	private final static String USAGE =
		"Usage: BlackBookClient Vin Milage";
	private static final String VEHICLE_TAG = "vehicle";
	private static final String VIN_TAG = "vin";
	private static final String WINDOWS = "Windows";
	private static final String YEAR_TAG = "year";
	private PresumptiveValueData caPresumptiveValueData;
	// defect 9748
	private static final String KEY_TAG = "key";
	private static final String SECURITY_KEY = "UCRIRFdUPDLEEmtlHVcm";
	// end defect 9748

	/**
	 * BlackBookClient.java Constructor
	 * 
	 * @param aaPresumptiveData PresumptiveValueData
	 */
	public BlackBookClient(PresumptiveValueData aaPresumptiveData)
	{
		super();
		this.caPresumptiveValueData = aaPresumptiveData;
		
		// Only proxy out when in development env. b/c there is a 
		// hole in the firewall when in a production env.
		if (System
			.getProperty(OS_NAME)
			.toLowerCase()
			.indexOf(WINDOWS.toLowerCase())
			> -1)
		{
			Authenticator.setDefault(new ProxyAuthenticator());
		}
		// defect 8943
		// This is not in the development environment
		else
		{
			// This is done because if another process has set these
			// values we don't want to use those settings.  This is
			// to force the system to not use a proxy server.
			Properties laProperties = System.getProperties();
			laProperties.remove(HTTP_PROXY_HOST);
			laProperties.remove(HTTP_PROXY_PORT);
		}
		// end defect 8943

		// Used when the URL is HTTPS
		//if (BLACKBOOK_URL.toUpperCase().startsWith(HTTPS))
		if (SystemProperty
			.getBlackBookURL()
			.toUpperCase()
			.startsWith(HTTPS))
		{
			Security.addProvider(
				new com.sun.net.ssl.internal.ssl.Provider());
			System.setProperty(
				STR_PROTOCOL_HANDLER_PROPERTY,
				STR_PROTOCOL_HANDLER_VALUE);
		}
	}

	/**
	 * Main class makes a call to BlackBook to test their webservice.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		try
		{
			if (args.length != 2)
			{
				System.out.println(USAGE);
			}
			else
			{
				PresumptiveValueData laPresumtiveData =
					new PresumptiveValueData();
				laPresumtiveData.setVIN(args[0]);
				laPresumtiveData.setOdometerReading(
					Integer.parseInt(args[1]));
				BlackBookClient laClient =
					new BlackBookClient(laPresumtiveData);
				PresumptiveValueData laVehValueData =
					laClient.getPrivatePartyValue();
				System.out.println(laVehValueData.getXMLResponse());
			}
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Builds the parameter to be sent in the URL to the web service.
	 * 
	 * @return String
	 */
	private String buildParms()
	{
		return URL_PARM_SEPARATOR
			+ VIN_TAG
			+ CommonConstant.STR_EQUAL
			+ caPresumptiveValueData.getVIN()
			+ PARM_SEPARATOR
			+ MILES_TAG
			+ CommonConstant.STR_EQUAL
			+ caPresumptiveValueData.getOdometerReading()
			// defect 9748
			+ PARM_SEPARATOR
			+ KEY_TAG
			+ CommonConstant.STR_EQUAL
			+ SECURITY_KEY
			// end defect 9748
			+ PARM_SEPARATOR
			+ CUSTOMER_TAG
			+ CommonConstant.STR_EQUAL
			+ SystemProperty.getBlackBookCustPrefix() 
			+ CommonConstant.STR_DASH 
			+ caPresumptiveValueData.getOfficeIssuanceNo();
	}

	/**
	 * Accepts the URL and returns a connection with the parameters
	 * set.
	 * 
	 * @param asURL String
	 * @return HttpURLConnection
	 * @throws Exception
	 */
	private HttpURLConnection getConnection(String asURL)
		throws Exception
	{
		URL laURL = new URL(asURL);
		HttpURLConnection laCon =
			(HttpURLConnection) laURL.openConnection();

		return laCon;
	}

	/**
	 * Gets the Private-party value from Vin and Mileage.
	 * 
	 * @return PresumptiveValueData
	 * @throws Exception
	 */
	public PresumptiveValueData getPrivatePartyValue()
		throws RTSException
	{
		try
		{
			//HttpURLConnection laURLCon = 
			//	getConnection(BLACKBOOK_URL + buildParms());
			HttpURLConnection laURLCon =
				getConnection(
					SystemProperty.getBlackBookURL() + buildParms());
					
			// Parse the response into an XML doc.
			Document laDoc = parseXML(laURLCon.getInputStream());

			setReturnValues(laDoc);

			return caPresumptiveValueData;
		}
		catch (Exception aeRTSEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeRTSEx);
		}
	}

	/**
	 * Gets the response value from the Response XML Document.
	 * 
	 *  <?xml version = "1.0"?>
	 * 		<vehicle>
	 * 			<vin>1FMDU32X2RUD63094</vin>
	 * 			<miles>30000</miles>
	 * 			<privatepartyvalue>3600</privatepartyvalue>
	 * 		</vehicle>
	 * 
	 * This is what is returned when it does not find the private party
	 * value for the VIN passed.
	 * 
	 *	<?xml version="1.0" ?> 
	 *		<vehicle /> 
	 * 
	 * @param asTagName String 
	 * @param aaDoc Document
	 * @return String
	 */
	private String getResponseValue(String asTagName, Document aaDoc)
	{
		NodeList laNL = aaDoc.getElementsByTagName(VEHICLE_TAG);

		for (int i = 0; i < laNL.getLength(); i++)
		{
			NodeList laParameterNL = laNL.item(i).getChildNodes();
			for (int j = 0; j < laParameterNL.getLength(); j++)
			{
				if (laParameterNL
					.item(j)
					.getNodeName()
					.equalsIgnoreCase(asTagName))
				{
					if (laParameterNL.item(j).getFirstChild() == null)
					{
						return CommonConstant.STR_SPACE_EMPTY;
					}
					else
					{
						return laParameterNL
							.item(j)
							.getFirstChild()
							.getNodeValue();
					}
				}
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}

	/**
	 * Parses the XML InputStream and returns a Document.
	 * 
	 * @param asInput InputStream
	 * @return Document
	 * @throws Exception
	 */
	private Document parseXML(InputStream asInput) throws Exception
	{
		DocumentBuilderFactory laDBF =
			DocumentBuilderFactory.newInstance();
		laDBF.setNamespaceAware(true);
		DocumentBuilder laDB = laDBF.newDocumentBuilder();
		Document laDocument = laDB.parse(asInput);

		return laDocument;
	}

	/**
	 * Get the values from the XML response and add them to the 
	 * Data Object
	 * 
	 * @param laDoc
	 * @throws Exception
	 */
	private void setReturnValues(Document laDoc) throws Exception
	{
		caPresumptiveValueData.setXMLResponse(xmlToString(laDoc));

		String lsPrivatePartyValue =
			getResponseValue(PRIVATE_PARTY_TAG, laDoc);
		String lsVehModlYr = getResponseValue(YEAR_TAG, laDoc);
		String lsVehMk = getResponseValue(MAKE_TAG, laDoc);
		String lsVehModl = getResponseValue(MODEL_TAG, laDoc);

		// Return 0.00 if there is not a private party value for this
		// VIN
		if (lsPrivatePartyValue.length() == 0)
		{
			caPresumptiveValueData.setPrivatePartyValue(
				new Dollar(0.00));
		}
		else
		{
			caPresumptiveValueData.setPrivatePartyValue(
				new Dollar(lsPrivatePartyValue));
		}

		if (lsVehModlYr.length() == 0)
		{
			caPresumptiveValueData.setVehModlYr(0);
		}
		else
		{
			caPresumptiveValueData.setVehModlYr(
				Integer.parseInt(lsVehModlYr));
		}

		caPresumptiveValueData.setVehMk(lsVehMk);
		caPresumptiveValueData.setVehModl(lsVehModl);
	}

	/**
	 * Takes an XML document and converts it to a string to be sent as
	 * a request.
	 * 
	 * @param aaDoc Document
	 * @throws Exception
	 * @return String
	 */
	private String xmlToString(Document aaDoc) throws Exception
	{
		Transformer laTransformer =
			TransformerFactory.newInstance().newTransformer();

		//initialize StreamResult with File object to save to file
		StreamResult laResult = new StreamResult(new StringWriter());
		DOMSource laSource = new DOMSource(aaDoc);
		laTransformer.transform(laSource, laResult);

		return laResult.getWriter().toString();
	}
}