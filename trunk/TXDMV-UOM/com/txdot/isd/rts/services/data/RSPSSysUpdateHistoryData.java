package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * RSPSSysUpdateHistoryData.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/19/2004	New Class
 * 							defect 7135 Ver 5.2.1
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	07/11/2005	add csSysUpdate
 * 							add getSysUpdate(),setSysUpdate()
 * 							removes "extends RSPSSysUpdateData" 
 * 							defect 8281 Ver 5.2.3 			  
 * ---------------------------------------------------------------
 */

/**
 * This class stores data related to the RSPS_WS_SYSUPDT_HSTRY table
 * 
 * @version	5.2.3		07/11/2005 
 * @author	Ray Rowehl
 * <br>Creation Date:	07/19/2004  07:33:11
 */

public class RSPSSysUpdateHistoryData
// defect 8281  
//extends RSPSSysUpdateData
// end defect 8281 
{
	// int
	private int ciDeleteIndi;
	private int ciOfcIssuanceNo;

	// Object 
	private RTSDate caAppliedTimeStamp;

	//String 
	private String csRSPSId;
	// defect 8281 
	private String csSysUpdate;
	// end defect 8281

	private final static long serialVersionUID = 3492495188412069927L;
	/**
	 * RSPSSysUpdateHistoryData constructor comment.
	 */
	public RSPSSysUpdateHistoryData()
	{
		super();
	}
	/**
	 * Return the value of AppliedTimeStamp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getAppliedTimeStamp()
	{
		return caAppliedTimeStamp;
	}
	/**
	 * Return the value of DeleteIndi
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}
	/**
	 * Return the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return the value of RSPSId
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRSPSId()
	{
		return csRSPSId;
	}
	/**
	 * Set the value of AppliedTimeStamp
	 * 
	 * @param aaAppliedTimeStamp RTSDate
	 */
	public void setAppliedTimeStamp(RTSDate aaAppliedTimeStamp)
	{
		caAppliedTimeStamp = aaAppliedTimeStamp;
	}
	/**
	 * Set the value of DeleteIndi
	 * 
	 * @param aiDeleteIndi int
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}
	/**
	 * Set the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set the value of RSPSId
	 * 
	 * @param asRSPSId String
	 */
	public void setRSPSId(String asRSPSId)
	{
		csRSPSId = asRSPSId;
	}
	/**
	 * Return the value of SysUpdate
	 * 
	 * @return
	 */
	public String getSysUpdate()
	{
		return csSysUpdate;
	}

	/**
	 * Set the value of SysUpdate
	 * 
	 * @param string
	 */
	public void setSysUpdate(String string)
	{
		csSysUpdate = string;
	}

}
