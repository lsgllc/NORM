package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.*;

/*
 *
 * RSPSSysUpdateData.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		07/14/2004	Created
 *							defect 7135 Ver 5.2.1
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	07/11/2005	deprecate class
 * 							defect 8281 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Data object containing sending RPRSTK transaction request 
 *
 * @version	5.2.3		07/11/2005
 * @author	Jeff S. 
 * <br>Creation Date:	07/14/2004 11:47:46
 */

public class RSPSSysUpdateData implements java.io.Serializable
{
	//	Object
	private RTSDate caDateAvailable;

	// String 
	private String csSysUpdate;
	private String csSysUpdateDescription;
	private String csSysUpdateFileName;

	private final static long serialVersionUID = 8469828076953983969L;
	/**
	 * RspsSysUpdtData constructor comment.
	 */
	public RSPSSysUpdateData()
	{
		super();
	}
	/**
	 * Return value of DateAvailable
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDateAvailable()
	{
		return caDateAvailable;
	}
	/**
	 * Return value of SysUpdate
	 * 
	 * @return String
	 */
	public String getSysUpdate()
	{
		return csSysUpdate;
	}
	/**
	 * Return value of SysUpdateDescription
	 * 
	 * @return String
	 */
	public String getSysUpdateDescription()
	{
		return csSysUpdateDescription;
	}
	/**
	 * Return value of SysUpdateFileName
	 * 
	 * @return String
	 */
	public String getSysUpdateFileName()
	{
		return csSysUpdateFileName;
	}
	/**
	 * Set value of DateAvailable
	 * 
	 * @param aaDateAvaliable RTSDate
	 */
	public void setDateAvailable(RTSDate aaDateAvailable)
	{
		caDateAvailable = aaDateAvailable;
	}
	/**
	 * Set value of SysUpdate
	 * 
	 * @param asSysUpdate String
	 */
	public void setSysUpdate(String asSysUpdate)
	{
		csSysUpdate = asSysUpdate;
	}
	/**
	 * Set value of SysUpdateDescription
	 * 
	 * @param asSysUpdateDescription String
	 */
	public void setSysUpdateDescription(String asSysUpdateDescription)
	{
		csSysUpdateDescription = asSysUpdateDescription;
	}
	/**
	 * Set value of SysUpdateFileName 
	 * 
	 * @param asSysUpdateFileName String
	 */
	public void setSysUpdateFileName(String asSysUpdateFileName)
	{
		csSysUpdateFileName = asSysUpdateFileName;
	}
}
