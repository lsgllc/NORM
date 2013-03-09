package com.txdot.isd.rts.services.data;

import java.util.Vector;

/*
 *
 * RSPSUpdData.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		07/14/2004	New Class
 * 							defect 7135 Ver 5.2.1
 * Min Wang		09/15/2004 	Add XML parameter for Scan Engine.
 *							add SCAN_ENGINE
 *							defect 7135 Ver 5.2.1
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This class contains Update Status Information from RSPS Laptops
 * 
 * @version	5.2.3		05/19/2005  
 * @author	Min Wang
 * <br>Creation Date:	07/14/2004  08:00:00
 */

public class RSPSUpdData implements java.io.Serializable
{
	/**
	 * object for all the update information besides the vector
	 */
	RSPSWsStatusData caRSPSWsStatusData;

	// Vector 
	private Vector cvSysUpdates;

	/**
	 * Constant for Code Version
	 */
	public final static String CODE_VERSION = "<Code_Version>";

	/**
	 * Constant for Jar Size
	 */
	public final static String JAR_SIZE = "<Code_Jar_Size>";

	/**
	 * Constant for Jar Date
	 */
	public final static String JAR_DATE = "<Code_Jar_Date>";

	/**
	 * Constant for DB Version
	 */
	public final static String DB_VERSION = "<DB_Version>";

	/**
	 * Constant for Log Date
	 */
	public final static String LOG_DATE = "<Log_Date>";

	/**
	 * Constant for DAT Level
	 */
	public final static String DAT_LEVEL = "<Dat_Level>";

	/**
	 * Constant for Scan Engine
	 */
	public final static String SCAN_ENGINE = "<Scan_Engine>";

	/**
	 * Constant for Host Name
	 */
	public final static String HOST_NAME = "<Host_Name>";

	/**
	 * Constant for IP Address
	 */
	public final static String IP_ADDRESS = "<Host_IpAddress>";

	/**
	 * Constant to mark the end of an XML Group
	 */
	public final static String XML_END_MARKER = "</";

	/**
	 * Constant for System Updates List
	 */
	public final static String SYSTEM_UPDATES_LIST =
		"<System_Updates_List>";

	public final static String UPDATES = "<Update>";

	/**
	 * Constant for System Updates List End Marker
	 */
	public final static String SYSTEM_UPDATES_LIST_END =
		"</System_Updates_List>";

	private final static long serialVersionUID = -4665873514466160471L;
	/**
	 * Returns the value of caRSPSWsStatusData
	 * 
	 * @return RSPSWsStatusData
	 */
	public RSPSWsStatusData getRSPSWsStatusData()
	{
		return caRSPSWsStatusData;
	}
	/**
	 * Returns the value of SysUpdates
	 * 
	 * @return Vector
	 */

	public Vector getSysUpdates()
	{
		return cvSysUpdates;
	}
	/**
	 * Sets the value of RSPSWsStatusData
	 * 
	 * @param aaRSPSWsStatusData RSPSWsStatusData
	 */
	public void setRSPSWsStatusData(RSPSWsStatusData aaRSPSWsStatusData)
	{
		caRSPSWsStatusData = aaRSPSWsStatusData;
	}
	/**
	 * Sets the value of SysUpdates
	 * 
	 * @param avSysUpdates Vector
	 */

	public void setSysUpdates(Vector avSysUpdates)
	{
		cvSysUpdates = avSysUpdates;
	}
}
