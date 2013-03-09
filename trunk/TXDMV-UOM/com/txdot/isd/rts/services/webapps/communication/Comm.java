package com.txdot.isd.rts.services.webapps.communication;

import java.util.*;
import java.io.*;
import java.net.*;

import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.exception.RTSWebAppsException;

/*
 * Comm.java
 * 
 * (c) Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C Chen		01/02/2002	initial
 * C Chen		09/23/2002	added conversation format support
 * C Chen		11/12/2002	no connection cache	
 * K Harrell	10/31/2005	Java 1.4 Work
 * 							defect 7889 Ver 5.2.3 
 * B Brown		08/16/2010	Change connection to TxO app servers (INET)
 * 							to https without using the 80 port number
 * 							modify processRequest()
 * 							defect 10556 Ver POS_650 
 * B Brown		10/04/2010	Change so desktop request to 
 * 							RegRenRTSServiceServlet is vai HTTP and when
 * 							running on one of our DMV servers, the
 * 							request is via HTTPS
 * 							modify processRequest()
 * 							defect 10605 Ver POS_660 
 * ---------------------------------------------------------------------
 */
/** 
 * This class is responsible for communicating between TexasOnline and 
 * TxDOT site servlets.
 * 
 * The protocol for communication is encapsulated in this class
 * The reason for this class is for the decoupling of phase 2
 * TexasOnline part with Phase 1.
 * Adpoted and evolved from Phase 1 Comm class
 *
 * @version	6.6.0			10/04/2010 
 * @author Clifford Chen
 * <br>Creation Date:		08/06/2002 10:54:59
 */
public class Comm
{
	private static String SERVLET_HOST_1 =
		CommunicationProperty.getServletHost1();
	private static String SERVLET_PORT_1 =
		CommunicationProperty.getServletPort1();
	private static String SERVLET_NAME_1 =
		CommunicationProperty.getServletName1();
	private static String SERVLET_HOST_2 =
		CommunicationProperty.getServletHost2();
	private static String SERVLET_PORT_2 =
		CommunicationProperty.getServletPort2();
	private static String SERVLET_NAME_2 =
		CommunicationProperty.getServletName2();

	private static String csProxyAuthorization;
	private static boolean cbUseProxy;

	/** 
	 * Converts a byte to hex digit and writes to the supplied buffer
	 * 
	 * @param abByte byte 
	 * @param asStrBuf StringBuffer
	 *
	 */
	private static void byte2hex(byte abByte, StringBuffer asStrBuf)
	{
		char[] larrHexChars =
			{
				'0',
				'1',
				'2',
				'3',
				'4',
				'5',
				'6',
				'7',
				'8',
				'9',
				'a',
				'b',
				'c',
				'd',
				'e',
				'f' };
		int liHigh = ((abByte & 0xf0) >> 4);
		int liLow = (abByte & 0x0f);
		asStrBuf.append(larrHexChars[liHigh]);
		asStrBuf.append(larrHexChars[liLow]);
	}
	/**
	 * Converts hex string into byte array.
	 * 
	 * @param asData String 
	 * @return byte[]
	 */
	public static byte[] deHextoByte(String asData)
	{
		String lsHex;
		Integer laCharValue;
		byte[] larrRet = new byte[asData.length() / 2];
		int liCount = 0;

		for (int i = 0; i < asData.length(); i += 2)
		{
			lsHex = asData.substring(i, i + 2);
			laCharValue = Integer.valueOf(lsHex, 16);
			int liInt = laCharValue.intValue();
			larrRet[liCount] = (byte) (0x00ff & liInt);
			liCount = liCount + 1;
		}
		return larrRet;
	}
	/**
	 * This method makes parameter string for servlet
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param asString String 
	 * @return String 
	 * 
	 */
	private static String makeParamString(
		int aiModuleName,
		int aiFunctionId,
		String asString)
	{
		String lsParamString =
			"ModuleName="
				+ aiModuleName
				+ "&FunctionId="
				+ aiFunctionId
				+ "&Data="
				+ asString;

		return lsParamString;
	}
	/**
	 * This method converts object into Java serilized String
	 * 
	 * @param aaObject Object
	 * @return String
	 * @throws RTSWebAppsException 
	 */
	public static String objToString(Object aaObject)
		throws RTSWebAppsException
	{
		String lsReturnStr = null;
		try
		{
			ByteArrayOutputStream laByteArrOutStrm =
				new ByteArrayOutputStream();
			ObjectOutputStream laObjOutStrm =
				new ObjectOutputStream(laByteArrOutStrm);
			laObjOutStrm.writeObject(aaObject);
			lsReturnStr = toHexString(laByteArrOutStrm.toByteArray());
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSWebAppsException(
				RTSWebAppsException.JAVA_ERROR,
				aeEx);
		}
		return (lsReturnStr);
	}
	/**
	 * Make a connection with servlet and get the result back
	 * 
	 * @param asDataString String
	 * @param aiCommDestination int
	 * @param asFormat String 
	 * @return Object 
	 * @throws RTSWebAppsException 
	*/
	private static Object processRequest(
		String asDataString,
		int aiCommDestination,
		String asFormat)
		throws RTSWebAppsException
	{
		StringBuffer lsStringBufferInp = new StringBuffer("");
		try
		{
			URL laURL = null;
			if (aiCommDestination
				== CommunicationProperty.CONNECT_TO_TXDOT)
			{
				laURL =
					new URL(
						"http://"
							+ SERVLET_HOST_1
							+ ":"
							+ SERVLET_PORT_1
							+ "/"
							+ SERVLET_NAME_1);
			}
			else
			{
				// defect 10605
				// use http + port for localhost
				// use https and no port for TxO server communications
				String lsURL = "";
				if (CommunicationProperty
					.getRegion()
					.equalsIgnoreCase("desktop"))
				{
					lsURL = "http://" 
							+ SERVLET_HOST_2
							+ ":"
							+ SERVLET_PORT_2	
							+ "/"
							+ SERVLET_NAME_2;
				}
				else
				{
					lsURL = "https://"
							+ SERVLET_HOST_2
							+ "/"
							+ SERVLET_NAME_2;
				}
//				laURL =
//					new URL(
						// defect 10556
						// change INET to https without the port and it 
						// will deafault to 443 for https
//						"https://"
//							+ SERVLET_HOST_2
//							+ ":"
//							+ SERVLET_PORT_2
							// end defect 10556
//							+ "/"
//							+ SERVLET_NAME_2);
				laURL =	new URL(lsURL);	
				// end defect 10605		
			}

			HttpURLConnection laURLConnection =
				(HttpURLConnection) laURL.openConnection();
			laURLConnection.setUseCaches(false);

			if (aiCommDestination
				== CommunicationProperty.CONNECT_TO_TEXASONLINE
				&& cbUseProxy)
			{
				laURLConnection.setRequestProperty(
					"Proxy-Authorization",
					csProxyAuthorization);
			}

			if (asFormat != null)
			{
				laURLConnection.setRequestProperty("Format", asFormat);
			}

			laURLConnection.setRequestMethod("POST");
			laURLConnection.setAllowUserInteraction(false);
			laURLConnection.setDoOutput(true);

			PrintWriter laOut =
				new PrintWriter(laURLConnection.getOutputStream());
			laOut.println(asDataString);
			laOut.flush();
			laOut.close();

			BufferedReader laBufferedReader =
				new BufferedReader(
					new InputStreamReader(
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
				lsStringBufferInp.append(lsRes);
			}
		}
		catch (MalformedURLException ae)
		{
			RTSWebAppsException leRTSWebAppsException =
				new RTSWebAppsException(
					RTSWebAppsException.JAVA_ERROR,
					ae);
			throw leRTSWebAppsException;

		}
		catch (IOException ae)
		{

			RTSWebAppsException leRTSWebAppsException =
				new RTSWebAppsException(
					RTSWebAppsException.SERVER_DOWN,
					ae);
			// cbServerDown = true;
			throw leRTSWebAppsException;
		}

		Object laObj = lsStringBufferInp.toString();

		if (asFormat != null && asFormat.equalsIgnoreCase("object"))
		{
			laObj = StringToObj(lsStringBufferInp.toString());
		}
		else
		{
			if (((String) laObj).startsWith("aced"))
				// returned an object ==> exception
			{
				laObj = StringToObj((String) laObj);
			}
		}

		if (laObj instanceof RTSWebAppsException)
		{
			RTSWebAppsException leRTSWebAppsEx =
				(RTSWebAppsException) laObj;
			throw leRTSWebAppsEx;
		}
		return laObj;
	}
	/**
	 * This method sends the data and method name to servlet and servlet 
	 * calls appropriate controller to handle the request.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaObject Object
	 * @throws RTSWebAppsException 
	 */
	public static Object sendToServer(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSWebAppsException
	{

		return sendToServer(
			aiModuleName,
			aiFunctionId,
			aaObject,
			CommunicationProperty.CONNECT_TO_TXDOT);
	}
	/**
	 * This method sends the data and method name to servlet and servlet 
	 * calls appropriate controller to handle the request.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaObject Object
	 * @param aiCommDestination int
	 * @throws RTSWebAppsException  
	 */
	public static Object sendToServer(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject,
		int aiCommDestination)
		throws RTSWebAppsException
	{
		return sendToServer(
			aiModuleName,
			aiFunctionId,
			aaObject,
			aiCommDestination,
			"object");
	}
	/**
	 * This method sends the data and method name to servlet and servlet 
	 * calls appropriate controller to handle the request.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaObject Object
	 * @param aiCommDestination int
	 * @param asFormat String
	 * @throws RTSWebAppsException   
	 */
	public static Object sendToServer(
		int asModuleName,
		int aiFunctionId,
		Object aaObject,
		int aiCommDestination,
		String asFormat)
		throws RTSWebAppsException
	{
		String lsString = objToString(aaObject);
		String lsParamString =
			makeParamString(asModuleName, aiFunctionId, lsString);

		return (
			processRequest(lsParamString, aiCommDestination, asFormat));
	}
	/**
	 * This method sends the data and method name to servlet and servlet 
	 * calls appropriate controller to handle the request.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaObject Object
	 * @param asFormat  String 
	 * @throws RTSWebAppsException 
	 */
	public static Object sendToServer(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject,
		String asFormat)
		throws RTSWebAppsException
	{

		return sendToServer(
			aiModuleName,
			aiFunctionId,
			aaObject,
			CommunicationProperty.CONNECT_TO_TXDOT,
			asFormat);
	}
	/**
	 * Set Proxy Info 
	 * 
	 * @param asProxyHost String
	 * @param asProxyPort String
	 * @param asProxyUser String
	 * @param asProxyPassword String 
	 */
	public static void setProxyInfo(
		String asProxyHost,
		String asProxyPort,
		String asProxyUser,
		String asProxyPassword)
	{
		Properties lsProp = System.getProperties();
		lsProp.put("http.proxyHost", asProxyHost);
		lsProp.put("http.proxyPort", asProxyPort);
		String lsCredential = asProxyUser + ":" + asProxyPassword;
		csProxyAuthorization =
			"Basic "
				+ new sun.misc.BASE64Encoder().encode(
					lsCredential.getBytes());

	}
	/**
	 * Set the value of cbUseProxy
	 * 
	 * @param abUseProxy boolean 
	 */
	public static void setUseProxy(boolean abUseProxy)
	{
		cbUseProxy = abUseProxy;
	}
	/**
	 * This method converts Java serialized String to an Object
	 * 
	 * @param asString String
	 * @throws RTSWebAppsException 	
	 */
	public static Object StringToObj(String asString)
		throws RTSWebAppsException
	{

		Object laReturnObj = null;
		try
		{
			ByteArrayInputStream laByteArrayInputStream =
				new ByteArrayInputStream(deHextoByte(asString));
			ObjectInputStream laObjInputStream =
				new ObjectInputStream(laByteArrayInputStream);
			laReturnObj = laObjInputStream.readObject();

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw new RTSWebAppsException(
				RTSWebAppsException.JAVA_ERROR,
				aeEx);

		}
		return laReturnObj;
	}
	/** 
	 * This method converts an array of bytes into Hex String.
	 * 
	 * @param aarrBlock byte{}
	 * @return String 
	 *
	 */
	public static String toHexString(byte[] aarrBlock)
	{
		StringBuffer lsBuffer = new StringBuffer();

		int liLen = aarrBlock.length;
		for (int i = 0; i < liLen; i++)
		{
			byte2hex(aarrBlock[i], lsBuffer);
		}
		return lsBuffer.toString();
	}
}
