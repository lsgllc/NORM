package com.txdot.isd.rts.services.reports.localoptions;

import com.txdot.isd.rts.services.data.SubstationSubscriptionData;

/*
 * PublishingReportData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3                  
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Data Object to receive data for Publishing Report
 *
 * @version	5.2.3 			06/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/24/2001 15:24:02
 */
public class PublishingReportData extends SubstationSubscriptionData
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
