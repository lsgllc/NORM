package com.txdot.isd.rts.services.data;


/*
 * SubscriptionReportData.java 
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3                   
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3
 * K Harrell	10/17/2005	Moved to services.data
 * 							defect 7896 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * SubscriptionReportData
 *
 * @version	5.2.3 			10/17/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 13:50:08
 */
public class SubscriptionReportData extends SubstationSubscriptionData
{
	protected String csSubstaName;

	/**
	 * Returns the value of SubstaName
	 * 
	 * @return String
	 */
	public final String getSubstaName()
	{
		return csSubstaName;
	}

	/**
	 * This method sets the value of SubstaName
	 * 
	 * @param asSubstaName String
	 */
	public final void setSubstaName(String asSubstaName)
	{
		csSubstaName = asSubstaName;
	}
}