package com.txdot.isd.rts.services.data;

/*
 *
 * EmployeeData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/22/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * EmployeeData 
 * 
 * @version	5.2.3		04/22/2005 
 * @author	Bobby Tulsiani
 * <br>Creation Date:	
 */

public class EmployeeData implements java.io.Serializable
{
	// String 
	private String csEmployeeId;
	private String csFirstName;
	private String csLastName;
	private String csRptStatus;
	
	private final static long serialVersionUID = -1451695556590014125L;
	/**
	 * EmployeeData constructor comment.
	 */
	public EmployeeData()
	{
		super();
	}
	/**
	 * Returns the value of EmployeeId
	 * 
	 * @return String
	 */
	public String getEmployeeId()
	{
		return csEmployeeId;
	}
	/**
	 * Returns the value of FirstName
	 * 
	 * @return String
	 */
	public String getFirstName()
	{
		return csFirstName;
	}
	/**
	 * Returns the value of LastName 
	 * 
	 * @return String
	 */
	public String getLastName()
	{
		return csLastName;
	}
	/**
	 * Returns the value of RptStatus
	 * 
	 * @return String
	 */
	public String getRptStatus()
	{
		return csRptStatus;
	}
	/**
	 * Sets the value of EmployeeId
	 * 
	 * @param asEmployeeId String
	 */
	public void setEmployeeId(String asEmployeeId)
	{
		csEmployeeId = asEmployeeId;
	}
	/**
	 * Sets the value of FirstName
	 * 
	 * @param asFirstName String
	 */
	public void setFirstName(String asFirstName)
	{
		csFirstName = asFirstName;
	}
	/**
	 * Sets the value of LastName
	 * 
	 * @param asLastName String
	 */
	public void setLastName(String asLastName)
	{
		csLastName = asLastName;
	}
	/**
	 * Sets the value of RptStatus 
	 * 
	 * @param asRptStatus String
	 */
	public void setRptStatus(String asRptStatus)
	{
		csRptStatus = asRptStatus;
	}
}
