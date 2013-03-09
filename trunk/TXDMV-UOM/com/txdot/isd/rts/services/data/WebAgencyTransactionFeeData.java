package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * WebAgencyTransactionFeesData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/10/2011	Created
 * 							defect 10708 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Methods to access RTS_WEB_AGNCY_TRANS_FEE
 *
 * @version	6.7.1 			01/10/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/10/2011 13:00:17
 */
public class WebAgencyTransactionFeeData implements Serializable
{
	private int ciAgncyTransFeeIdntyNo;
	private int ciSavReqId;

	private String csAcctItmCd;
	
	private Dollar caItmPrice;

	static final long serialVersionUID = -1094235674868351334L;

	/**
	 * WebAgencyTransactionFeeData.java Constructor
	 * 
	 */
	public WebAgencyTransactionFeeData()
	{
		super();
	}

	/**
	 * Get value of csAcctItmCd
	 * 
	 * @return String 
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Get value of ciAgncyTransFeeIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyTransFeeIdntyNo()
	{
		return ciAgncyTransFeeIdntyNo;
	}

	/**
	 * Get value of caItmPrice
	 * 
	 * @return Dollar 
	 */
	public Dollar getItmPrice()
	{
		return caItmPrice;
	}

	/**
	 * Get value of ciSavReqId
	 * 
	 * @return int
	 */
	public int getSavReqId()
	{
		return ciSavReqId;
	}

	/**
	 * Set value of csAcctItmCd
	 * 
	 * @param asAcctItmCd
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * Set value of ciAgncyTransFeeIdntyNo
	 * 
	 * @param aiAgncyTransFeeIdntyNo
	 */
	public void setAgncyTransFeeIdntyNo(int aiAgncyTransFeeIdntyNo)
	{
		ciAgncyTransFeeIdntyNo = aiAgncyTransFeeIdntyNo;
	}

	/**
	 * Set value of caItmPrice
	 * 
	 * @param aaItmPrice
	 */
	public void setItmPrice(Dollar aaItmPrice)
	{
		caItmPrice = aaItmPrice;
	}

	/**
	 * Set value of ciSavReqId
	 * 
	 * @param aiSavReqId
	 */
	public void setSavReqId(int aiSavReqId)
	{
		ciSavReqId = aiSavReqId;
	}

}
