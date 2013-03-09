package com.txdot.isd.rts.webservices.veh.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsVehPublicResponse.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/13/2010	Created 
 * 							defect 10684 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Response data for Public Web Service for Vehicle Requests
 *
 * @version	6.7.0			12/13/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/13/2010 11:49:17
 */
public class RtsVehPublicResponse extends RtsAbstractResponse
{
	private RtsVehIndicator[] carrIndicators = new RtsVehIndicator[0];

	private int ciErrMsgNo;
	private int ciTtlIssueDate;
	private int ciVehModlYr;
	
	private String csDocNo;
	private String csVehMk;
	private String csVehModl;
	private String csVIN; 

	/**
	 * Get value of csDocNo
	 * 
	 * @return String 
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Get value of ciErrMsgNo
	 * 
	 * @return
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Get value of carrIndicators
	 * 
	 * @return RtsVehIndicator[]
	 */
	public RtsVehIndicator[] getIndicators()
	{
		return carrIndicators;
	}

	/**
	 * Get value of ciTtlIssueDate
	 * 
	 * @return int
	 */
	public int getTtlIssueDate()
	{
		return ciTtlIssueDate;
	}

	/**
	 * Get value of csVehMk
	 * 
	 * @return String 
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Get value of csVehModl
	 * 
	 * @return String 
	 */
	public String getVehModl()
	{
		return csVehModl;
	}

	/**
	 * Get value of ciVehModlYr
	 * 
	 * @return int 
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Get value of csVIN
	 * 
	 * @return String 
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Set value of csDocNo
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set value of ciErrMsgNo
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Set value of carrIndicators
	 * 
	 * @param aarrIndicators
	 */
	public void setIndicators(RtsVehIndicator[] aarrIndicators)
	{
		carrIndicators = aarrIndicators;
	}

	/**
	 * Set value of ciTtlIssueDate
	 * 
	 * @param aiTtlIssueDate
	 */
	public void setTtlIssueDate(int aiTtlIssueDate)
	{
		ciTtlIssueDate = aiTtlIssueDate;
	}

	/**
	 * Set value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set value of csVehModl
	 * 
	 * @param asVehModl
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * Set value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Set value of csVIN 
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

}
