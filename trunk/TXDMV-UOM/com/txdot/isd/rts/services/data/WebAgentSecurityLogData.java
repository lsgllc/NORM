package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebAgentSecurityLogData.java
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
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgentSecurityLogData 
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgentSecurityLogData
	extends WebAgentSecurityData
	implements Serializable
{
	private int ciUpdtngAgntIdntyNo;
	
	private String csUpdtngActn;
	private String csUpdtngIPAddr;
	
	static final long serialVersionUID = 1864225959583843753L;

	/**
	 * WebAgentSecurityLogData.java Constructor
	 * 
	 */
	public WebAgentSecurityLogData()
	{
		super();
	}

	/**
	 * Get value of csUpdtngActn
	 * 
	 * @return String 
	 */
	public String getUpdtngActn()
	{
		return csUpdtngActn;
	}

	/**
	 * Get value of ciUpdtngAgntIdntyNo
	 * 
	 * @return int
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
	}

	/**
	 * Get value of csUpdtngIPAddr
	 * 
	 * @return String 
	 */
	public String getUpdtngIPAddr()
	{
		return csUpdtngIPAddr;
	}

	/**
	 * Set value of csUpdtngActn
	 * 
	 * @param asUpdtngActn
	 */
	public void setUpdtngActn(String asUpdtngActn)
	{
		csUpdtngActn = asUpdtngActn;
	}

	/**
	 * Set value of 
	 * 
	 * @param aiUpdtngAgntIdntyNo
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}

	/**
	 * Set value of csUpdtngIPAddr
	 * 
	 * @param asUpdtngIPAddr
	 */
	public void setUpdtngIPAddr(String asUpdtngIPAddr)
	{
		csUpdtngIPAddr = asUpdtngIPAddr;
	}

}
