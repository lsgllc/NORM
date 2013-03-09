package com.txdot.isd.rts.services.util;import java.io.*;import java.util.*;/* * * Version.java * * (c) Texas Department of Transportation 2002 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	07/16/2005	RTS 5.2.3 Code Cleanup * 							work on prolog and javadocs. * 							Deprecated since this is no longer used.  * 							defect 7885 Ver 5.2.3 * --------------------------------------------------------------------- */ /** * A class that allows easy lookup of the version information of RTSII *  * @version	5.2.3			07/16/2005 * @author	Michael Abernethy * <br>Creation Date:		02/27/2002 09:07:16 * @deprecated *//* &Version& */public class Version {/* &Version'DEFAULT_VERSION_NUMBER& */	private static final String DEFAULT_VERSION_NUMBER = "5.0";/* &Version'DEFAULT_VERSION_DATE& */	private static final String DEFAULT_VERSION_DATE = "06/01/2002";/* &Version'FILE_NAME& */	private final static java.lang.String FILE_NAME = "cfg\\Version.cfg";/* &Version'DATE& */	private final static java.lang.String DATE = "VersionDate";/* &Version'NUM& */	private final static java.lang.String NUM = "VersionNo";	/** * Returns the date of this RTS version. *  * @return String *//* &Version.getDate& */public static String getDate() {	try	{		FileInputStream laFIS = new FileInputStream(FILE_NAME);		Properties laProp = new Properties();		laProp.load(laFIS);	      		return laProp.getProperty(DATE);	}	catch (IOException aeEx)	{		return DEFAULT_VERSION_DATE;	}}/** * Returns the version number of RTS II *  * @return String *//* &Version.getVersion& */public static String getVersion(){	try	{		FileInputStream laFIS = new FileInputStream(FILE_NAME);		Properties laProp = new Properties();		laProp.load(laFIS);	      		return laProp.getProperty(NUM);	}	catch (IOException aeEx)	{		return DEFAULT_VERSION_NUMBER;	}}}/* #Version# */