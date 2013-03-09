package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * WebSessionData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0
 * K Harrell 	01/05/2011 	Renamings per standards 
 *        					defect 10708 Ver 6.7.0
 * Ray Rowehl	04/01/2011	Add ReqIpAddr. 
 * 							add csReqIpAddr
 * 							add getReqIpAddr(), setReqIpAddr()
 * 							defect 10670 Ver 6.7.1   
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebSessionData 
 *
 * @version	6.7.1			04/01/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */

public class WebSessionData implements Serializable
{

	static final long serialVersionUID = -592651086377764163L;

	private RTSDate caLastAccsTimestmp;
	
	private int ciAgntSecrtyIdntyNo;

	private String csEDirSessionId;
	private String csReqIpAddr;
	private String csWebSessionId;

	/**
	 * WebSessionData.java Constructor
	 */
	public WebSessionData()
	{
		super();
	}

	/**
	 * Get value of ciAgntSecrtyIdntyNo
	 * 
	 * @return int 
	 */
	public int getAgntSecrtyIdntyNo()
	{
		return ciAgntSecrtyIdntyNo;
	}

	/**
	 * Get value of csEDirSessionId
	 * 
	 * @return String
	 */
	public String getEDirSessionId()
	{
		return csEDirSessionId;
	}

	/**
	 * Get value of caLastAccsTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getLastAccsTimestmp()
	{
		return caLastAccsTimestmp;
	}

	/**
	 * Set the Requestors Ip Address.
	 * 
	 * @return String
	 */
	public String getReqIpAddr()
	{
		return csReqIpAddr;
	}

	/**
	 * Get value of csWebSessionId
	 * 
	 * @return String
	 */
	public String getWebSessionId()
	{
		return csWebSessionId;
	}

	/**
	 * Set value of ciAgntSecrtyIdntyNo
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 */
	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;
	}

	/**
	 * Set value of csEDirSessionId
	 * 
	 * @param asEDirSessionId
	 */
	public void setEDirSessionId(String asEDirSessionId)
	{
		csEDirSessionId = asEDirSessionId;
	}

	/**
	 * Set value of caLastAccsTimestmp
	 * 
	 * @param aaLastAccsTimestmp
	 */
	public void setLastAccsTimestmp(RTSDate aaLastAccsTimestmp)
	{
		caLastAccsTimestmp = aaLastAccsTimestmp;
	}

	/**
	 * Set the Requestors Ip Address.
	 * 
	 * @param asReqIpAddr
	 */
	public void setReqIpAddr(String asReqIpAddr)
	{
		csReqIpAddr = asReqIpAddr;
	}

	/**
	 * Set value of csWebSessionId
	 * 
	 * @param asWebSessionId
	 */
	public void setWebSessionId(String asWebSessionId)
	{
		csWebSessionId = asWebSessionId;
	}

}
