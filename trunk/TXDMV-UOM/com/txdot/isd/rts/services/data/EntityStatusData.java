package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * EntityStatusData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * Data object for Entity Status Data in Funds Processing  
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Bobby Tulsiani
 * <br>Creation Date:	
 */

public class EntityStatusData implements java.io.Serializable
{
	// int 
	private int ciCashWsId;

	//	Object 
	private Dollar caBsnDateTotalAmt;

	// String 
	private String csEmployeeId;
	private String csReportStatus;

	private final static long serialVersionUID = -1233960414675527915L;
	/**
	 * EntityStatusData constructor comment.
	 */
	public EntityStatusData()
	{
		super();
	}
	/**
	 * Return value of BsnDateTotalAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getBsnDateTotalAmt()
	{
		return caBsnDateTotalAmt;
	}
	/**
	* Return value of CashWsId
	 * 
	 * @return int
	 */
	public int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * Return value of EmployeeId
	 * 
	 * @return String
	 */
	public String getEmployeeId()
	{
		return csEmployeeId;
	}
	/**
	 * Return value of ReportStatus
	 * 
	 * @return String
	 */
	public String getReportStatus()
	{
		return csReportStatus;
	}
	/**
	 * Return value of BsnDateTotalAmt
	 * 
	 * @param aaBsnDateTotalAmt Dollar
	 */
	public void setBsnDateTotalAmt(Dollar aaBsnDateTotalAmt)
	{
		caBsnDateTotalAmt = aaBsnDateTotalAmt;
	}
	/**
	 * Set value of CashWsId
	 * 
	 * @param aiCashWsId int
	 */
	public void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * Set value of EmployeeId
	 * 
	 * @param asEmployeeId String
	 */
	public void setEmployeeId(String asEmployeeId)
	{
		csEmployeeId = asEmployeeId;
	}
	/**
	 * Set value of ReportStatus
	 * 
	 * @param asReportStatus String
	 */
	public void setReportStatus(String asReportStatus)
	{
		csReportStatus = asReportStatus;
	}
}
