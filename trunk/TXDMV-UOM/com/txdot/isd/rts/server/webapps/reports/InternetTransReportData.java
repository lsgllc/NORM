package com.txdot.isd.rts.server.webapps.reports;

/*
 * InternetTransReportData.java
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
 * Internet Trans Report Data
 *  
 * @version	5.2.3		05/04/2005
 * @author	Administrator
 * <br>Creation Date:	11/26/2001 16:21:35
 */
public class InternetTransReportData
{
	private String csTraceNo;
	private double cdPaymentAmt;
	private String csStatusCd;
	private String csReasonCd;
	private String csTransId;
	
	/**
	 * InternetTransReportData constructor comment.
	 */
	public InternetTransReportData()
	{
		super();
	}
	
	/**
	 * Get Payment Amt
	 * 
	 * @return double
	 */
	public double getPaymentAmt()
	{
		return cdPaymentAmt;
	}
	
	/**
	 * Get Reason Code
	 * 
	 * @return String
	 */
	public String getReasonCd()
	{
		return csReasonCd;
	}
	
	/**
	 * Get Status Code
	 * 
	 * @return String
	 */
	public String getStatusCd()
	{
		return csStatusCd;
	}
	
	/**
	 * Get Trace Number
	 * 
	 * @return String
	 */
	public String getTraceNo()
	{
		return csTraceNo;
	}
	
	/**
	 * Get Trans Id
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
	}
	
	/**
	 * Set Payment Amt
	 * 
	 * @param double adValue
	 */
	public void setPaymentAmt(double adValue)
	{
		cdPaymentAmt = adValue;
	}
	
	/**
	 * Set Reason Code
	 * 
	 * @param String asValue
	 */
	public void setReasonCd(String asValue)
	{
		csReasonCd = asValue;
	}
	
	/**
	 * Set Status Code
	 * 
	 * @param String asValue
	 */
	public void setStatusCd(String asValue)
	{
		csStatusCd = asValue;
	}
	
	/**
	 * Set Trace Number
	 * 
	 * @param String asValue
	 */
	public void setTraceNo(String asValue)
	{
		csTraceNo = asValue;
	}
	
	/**
	 * Set Trans Id
	 * 
	 * @param String asValue
	 */
	public void setTransId(String asValue)
	{
		csTransId = asValue;
	}
}
