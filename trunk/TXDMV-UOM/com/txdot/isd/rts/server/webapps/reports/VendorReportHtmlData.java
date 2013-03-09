package com.txdot.isd.rts.server.webapps.reports;

/*
 * VendorReportHtmlData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * Vendor Report Html Data
 *  
 * @version	5.2.3		05/04/2005
 * @author	Administrator
 * <br>Creation Date:	07/17/2002 9:30:50
 */
public class VendorReportHtmlData
{
	int ciType;
	String csHtml;
	
	/**
	 * EpayVendorReportHtmlData constructor comment.
	 * 
	 * @param int aiType
	 * @param String asHtml
	 */
	public VendorReportHtmlData(int aiType, String asHtml)
	{
		ciType = aiType;
		csHtml = asHtml;
	}

	/**
	 * Get HTML
	 * 
	 * @return String
	 */
	public String getHtml()
	{
		return csHtml;
	}

	/**
	 * Get Type
	 * 
	 * @return int
	 */
	public int getType()
	{
		return ciType;
	}
}
