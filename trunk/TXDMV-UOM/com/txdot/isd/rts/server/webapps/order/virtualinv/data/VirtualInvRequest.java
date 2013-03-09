package com.txdot.isd.rts.server.webapps.order.virtualinv.data;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest;

/*
 * VirtualInvRequest.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		03/05/2007	New class.
 * 							defect 9121 Ver Special Plates
 * Bob B.		03/24/2008	Remove regIPAddr, getRegIPAddr(), 
 * 							setRegIPAddr()
 * 							Add sessionID
 * 							getSessionID(), setSessionID()  
 *							defect 9601 Ver Tres Amigos Prep 
 * ---------------------------------------------------------------------
 */

/**
 * This is the VirtualInvRequest class.
 *
 * @version	Tres Amigos Prep	03/24/2008
 * @author	bbrown
 * <br>Creation Date:			03/05/2007 14:30:00
 */
public class VirtualInvRequest extends AbstractRequest
{
	private int grpId = 0;
	private int grpPltId = 0;
	// this is UserPltNo in inventory 
	private boolean InetReq = true;
	private boolean isaFlag = false;
	private String itemCode = null;
	private String itemNo = null;
	private int itemYr = 0;

	//	this is RequestorRegPltNo used for PLP requests
	private String manufacturingPltNoRequest = null;
	private boolean plpFlag = false;
	// private String ofcIssuanceno = null;
	//	private String transWSId = null;
	//	private String transEmpId = null;
	//	private String ofcIssuanceno = null;
	// defect 9601
	// private String regIPAddr = null;
	private String sessionID = null;
	// end defect 9601
	private String regPltNo = null;
	private String requestingIP = null;

	/**
	 * Gets the Group ID
	 * 
	 * @return int
	 */
	public int getGrpId()
	{
		return grpId;
	}

	/**
	 * Gets the groug plate ID
	 * 
	 * @return int
	 */
	public int getGrpPltId()
	{
		return grpPltId;
	}

	/**
	 * gets itemCode
	 *
	 * @return String
	 */
	public String getItemCode()
	{
		return itemCode;
	}

	/**
	 * Gets the Item number
	 * 
	 * @return
	 */
	public String getItemNo()
	{
		return itemNo;
	}

	/**
	 * Gets the Item year
	 * 
	 * @return
	 */
	public int getItemYr()
	{
		return itemYr;
	}

	/**
	 * Gets the Manufacturing Plate Number Requested.
	 * 
	 * @return String
	 */
	public String getManufacturingPltNoRequest()
	{
		return manufacturingPltNoRequest;
	}

	/**
	 * Gets the requesting IP address
	 * 
	 * @return String
	 */
//	public String getRegIPAddr()
//	{
//		return regIPAddr;
//	}

	/**
	 * Gets the Registration Plate Number.
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return regPltNo;
	}

	/**
	 * Gets the Requesting IP
	 * 
	 * @return String
	 */
	public String getRequestingIP()
	{
		return requestingIP;
	}

	/**
	 * Gets the Inet Request boolean.
	 * 
	 * @return boolean
	 */
	public boolean isInetReq()
	{
		return InetReq;
	}

	/**
	 * Gets the Isa flag boolean.
	 * 
	 * @return boolean
	 */
	public boolean isIsaFlag()
	{
		return isaFlag;
	}

	/**
	 * Gets the PLP boolean flag
	 * 
	 * @return boolean
	 */
	public boolean isPlpFlag()
	{
		return plpFlag;
	}

	/**
	 * Sets the group ID
	 * 
	 * @param aiGrpId
	 */
	public void setGrpId(int aiGrpId)
	{
		grpId = aiGrpId;
	}

	/**
	 * Sets the group plate ID
	 * 
	 * @param aiGrpPltId
	 */
	public void setGrpPltId(int aiGrpPltId)
	{
		grpPltId = aiGrpPltId;
	}

	/**
	 * Sets the Inet request boolean
	 * 
	 * @param abInetReq
	 */
	public void setInetReq(boolean abInetReq)
	{
		InetReq = abInetReq;
	}

	/**
	 * Sets the Isa boolean flag
	 * 
	 * @param abIsaFlag
	 */
	public void setIsaFlag(boolean abIsaFlag)
	{
		isaFlag = abIsaFlag;
	}

	/**
	 * sets itemCode
	 * 
	 * @param asItemCode
	 */
	public void setItemCode(String asItemCode)
	{
		itemCode = asItemCode;
	}

	/**
	 * sets itemNo
	 * 
	 * @param asItemNo
	 */
	public void setItemNo(String asItemNo)
	{
		itemNo = asItemNo;
	}

	/**
	 * Sets the Item year
	 * 
	 * @param aiItemYr
	 */
	public void setItemYr(int aiItemYr)
	{
		itemYr = aiItemYr;
	}

	/**
	 * Sets the Manufacturing request number.
	 * 
	 * @param asManuPltNoRequest String
	 */
	public void setManufacturingPltNoRequest(String asManuPltNoRequest)
	{
		manufacturingPltNoRequest = asManuPltNoRequest;
	}

	/**
	 * Sets the Plp bolean flag
	 * 
	 * @param abPlpFlag
	 */
	public void setPlpFlag(boolean abPlpFlag)
	{
		plpFlag = abPlpFlag;
	}

	/**
	 * Sets the requesting IP
	 * 
	 * @param asRegIPAddr
	 */
//	public void setRegIPAddr(String asRegIPAddr)
//	{
//		regIPAddr = asRegIPAddr;
//	}

	/**
	 * Sets the Registration Plate Number.
	 * 
	 * @param string
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		regPltNo = asRegPltNo;
	}

	/**
	 * Sets the Requesting IP
	 * 
	 * @param asRequestingIP
	 */
	public void setRequestingIP(String asRequestingIP)
	{
		requestingIP = asRequestingIP;
	}

	/**
	 * gets the SessionID of the internet user
	 * 
	 * @return String
	 */
	public String getSessionID()
	{
		return sessionID;
	}

	/**
	 * sets the SessionID of the internet user
	 * 
	 * @param string
	 */
	public void setSessionID(String lsSessionID)
	{
		sessionID = lsSessionID;
	}

}
