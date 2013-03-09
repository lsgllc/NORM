package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebAgentLog.java
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
 * WebAgentLogData 
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgentLogData
	extends WebAgentData
	implements Serializable
{
	private int ciUpdtngAgntIdntyNo;

	static final long serialVersionUID = 2116021117115507100L;

	/**
	 * WebAgentLog.java Constructor
	 * 
	 */
	public WebAgentLogData()
	{
		super();
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
	 * Set value of ciUpdtngAgntIdntyNo
	 * 
	 * @param aiUpdtngAgntIdntyNo
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}

}
