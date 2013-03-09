package com.txdot.isd.rts.services.reports.localoptions;

/*
 * EventSecurityReportData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/15/2003	Add csUserName, setUserName(), and 
 *							getUserName() for xp.
 *                          Defect 6616. Version 5.1.6.
 * S Johnston	05/13/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	10/05/2008	Make Object Comparable - by Event Name 
 * 							add compareTo()
 * 							defect 9831 Ver Defect_POS_B  
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * EventSecurityReportData
 *
 * @version	Defect_POS_B	10/05/2008
 * @author	Kathy Harrell
 * <br> Creation Date:		09/20/2001 15:11:24
 */
public class EventSecurityReportData implements Comparable
{
	protected String csEmpId;
	protected String csEmpLastName;
	protected String csEmpFirstName;
	protected String csEmpMI;
	protected String csEventName;
	// defect 6616
	protected String csUserName;
	// end defect 6616

	/**
	 * Sorts the Data Object by Event Name
	 * 
	 */
	public int compareTo(Object aaObject)
	{
		EventSecurityReportData laEventSecurityRptData =
			(EventSecurityReportData) aaObject;
		String lsCurrentEventName = csEventName;
		String lsCompareToEventName =
			laEventSecurityRptData.getEventName();
		return lsCurrentEventName.compareTo(lsCompareToEventName);
	}

	/**
	 * Returns the value of EmpFirstName
	 * 
	 * @return String 
	 */
	public final String getEmpFirstName()
	{
		return csEmpFirstName;
	}

	/**
	 * Returns the value of EmpId
	 * 
	 * @return String 
	 */
	public final String getEmpId()
	{
		return csEmpId;
	}

	/**
	 * Returns the value of EmpLastName
	 * 
	 * @return String 
	 */
	public final String getEmpLastName()
	{
		return csEmpLastName;
	}

	/**
	 * Returns the value of EmpMI
	 * 
	 * @return String 
	 */
	public final String getEmpMI()
	{
		return csEmpMI;
	}

	/**
	 * Returns the value of EventName
	 * 
	 * @return String 
	 */
	public final String getEventName()
	{
		return csEventName;
	}

	/**
	 * Returns the value of UserName
	 * 
	 * @return String 
	 */
	public final String getUserName()
	{
		return csUserName;
	}

	/**
	 * This method sets the value of EmpFirstName.
	 * 
	 * @param asEmpFirstName String 
	 */
	public final void setEmpFirstName(String asEmpFirstName)
	{
		csEmpFirstName = asEmpFirstName;
	}

	/**
	 * This method sets the value of EmpId.
	 * 
	 * @param asEmpId String 
	 */
	public final void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}

	/**
	 * This method sets the value of EmpLastName.
	 * 
	 * @param asEmpLastName String 
	 */
	public final void setEmpLastName(String asEmpLastName)
	{
		csEmpLastName = asEmpLastName;
	}

	/**
	 * This method sets the value of EmpMI.
	 * 
	 * @param asEmpMI String 
	 */
	public final void setEmpMI(String asEmpMI)
	{
		csEmpMI = asEmpMI;
	}

	/**
	 * This method sets the value of EventName.
	 * 
	 * @param asEventName String 
	 */
	public final void setEventName(String asEventName)
	{
		csEventName = asEventName;
	}

	/**
	 * This method sets the value of UserName.
	 * 
	 * @param asUserName String 
	 */
	public final void setUserName(String asUserName)
	{
		csUserName = asUserName;
	}
}
