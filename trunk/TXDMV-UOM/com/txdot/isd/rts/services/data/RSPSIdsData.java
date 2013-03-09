package com.txdot.isd.rts.services.data;

/*
 *
 * RSPSIdsList.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		08/16/2004	New Class
 * 							defect 7135 Ver 5.2.1
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3			  
 * ---------------------------------------------------------------
 */

/**
 * This class contains Id Information for RSPS ids for a county.
 * RSPS Ids are made up of Dealers and Subcons
 * 
 * @version	5.2.3		04/21/2005 
 * @author	Min Wang
 * <br>Creation Date:	08/16/2004
 */

public class RSPSIdsData
{
	// int
	private int ciId = 0;

	//String 
	private String csIdName = "";
	private String csIdType = "";
	/**
	 * RSPSIdsList constructor comment.
	 */
	public RSPSIdsData()
	{
		super();
	}
	/**
	 * Returns the value of Id
	 * 
	 * @return int
	 */

	public int getId()
	{
		return ciId;
	}
	/**
	 * Returns the value of IdName
	 * 
	 * @return String
	 */

	public String getIdName()
	{
		return csIdName;
	}
	/**
	 * Returns the value of IdType
	 * 
	 * @return String
	 */

	public String getIdType()
	{
		return csIdType;
	}
	/**
	 * Sets the value of IdName
	 * 
	 * @param asIdName String
	 */

	public void setEmpId(String asIdName)
	{
		csIdName = asIdName;
	}
	/**
	 * Sets the value of Id
	 * 
	 * @param aiId int
	 */

	public void setId(int aiId)
	{
		ciId = aiId;
	}
	/**
	 * Sets the value of IdName
	 * 
	 * @param asIdName String
	 */

	public void setIdName(String asIdName)
	{
		csIdName = asIdName;
	}
	/**
	 * Sets the value of IdType
	 * 
	 * @param asIdType String
	 */

	public void setIdType(String asIdType)
	{
		csIdType = asIdType;
	}
}
