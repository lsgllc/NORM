package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * PostalStateData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2010	Created
 * 							defect 10396 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * PostalStateData.java
 *
 * @version	POS_640 		03/24/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		03/24/2010  13:11:00 
 */
public class PostalStateData implements Serializable
{
	// String 
	private String csStateAbrvn;
	private String csStateName;
	private String csStateTypeCd;

	// long 
	static final long serialVersionUID = 1960936607935113432L;

	/**
	 * PostalStateData constructor comment.
	 */
	public PostalStateData()
	{
		super();
	}

	/**
	 * Get value of csStateAbrvn
	 * 
	 * @return String
	 */
	public String getStateAbrvn()
	{
		return csStateAbrvn;
	}

	/**
	 * Get value of csStateName
	 * 
	 * @return String
	 */
	public String getStateName()
	{
		return csStateName;
	}

	/**
	 * Get value of csStateTypeCd
	 * 
	 * @return String
	 */
	public String getStateTypeCd()
	{
		return csStateTypeCd;
	}

	/**
	 * Set value of csStateAbrvn
	 * 
	 * @param asStateAbrvn
	 */
	public void setStateAbrvn(String asStateAbrvn)
	{
		csStateAbrvn = asStateAbrvn;
	}

	/**
	 * Set value of csStateName
	 * 
	 * @param asStateName
	 */
	public void setStateName(String asStateName)
	{
		csStateName = asStateName;
	}

	/**
	 * Set value of csStateTypeCd
	 * 
	 * @param asStateTypeCd
	 */
	public void setStateTypeCd(String asStateTypeCd)
	{
		csStateTypeCd = asStateTypeCd;
	}
}
