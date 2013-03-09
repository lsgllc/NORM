package com.txdot.isd.rts.webservices.webapps.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

/*
 * RtsWebAppsRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/01/2008	Created Class
 * 							defect 9676 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * Request data for WebApps Service.
 *
 * @version	MyPlates_POS		06/26/2008
 * @author	mwang
 * <br>Creation Date:			06/26/2008 12:06:00
 */
public class RtsWebAppsRequest extends RtsAbstractRequest
{
	private String csItrntTraceNo;
	private String csPymntOrderId;
	private String csRegPltNo;
	private String csReqIpAddr;
	
	/**
	 * Return the value of ItrntTraceNo
	 * 
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Return the value of PymntOrderId
	 * 
	 * @return String
	 */
	public String getPymntOrderId()
	{
		return csPymntOrderId;
	}

	/**
	 * Return the value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Return the value of ReqIpAddr
	 * 
	 * @return String
	 */
	public String getReqIpAddr()
	{
		return csReqIpAddr;
	}

	/**
	 * Set the value of ItrntTraceNo
	 * 
	 * @param asItrntTraceNo String
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Set the value of PymntOrderId
	 * 
	 * @param asPymntOrderId String
	 */
	public void setPymntOrderId(String asPymntOrderId)
	{
		csPymntOrderId = asPymntOrderId;
	}

	/**
	 * Set the value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set the value of ReqIpAddr
	 * 
	 * @param asReqIpAddr String
	 */
	public void setReqIpAddr(String asReqIpAddr)
	{
		csReqIpAddr = asReqIpAddr;
	}

}
