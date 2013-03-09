/**
 * 
 */
package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebAgencyTransactionAddressData.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/03/2011	Created
 * 							defect 11137 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/** 
 * Data class for RTS_WEB_AGNCY_TRANS_ADDR 
 * 
 * @version	6.9.0		11/03/2011 
 * @author	Kathy Harrell 
 * @since				11/03/2011	15:20:17 
 */
public class WebAgencyTransactionAddressData implements Serializable
{
	private int ciSavReqId; 
	private String csAddrTypeCd; 
	private AddressData caAddrData;
	
	private static final long serialVersionUID = -1187838264476768975L;
	
	/**
	 * WebAgencyTransactionAddressData.java Constructor
	 * 
	 * @param aaRtsVehInfo
	 */
	public WebAgencyTransactionAddressData(int aiSavReqId, String asAddrTypeCd, AddressData aaAddrData)
	{
		super();
		ciSavReqId = aiSavReqId; 
		csAddrTypeCd = asAddrTypeCd; 
		caAddrData = aaAddrData; 
	}
	
	/**
	 * @return the caAddrData
	 */
	public AddressData getAddrData()
	{
		return caAddrData;
	}
	
	/**
	 * @param caAddr the caAddr to set
	 */
	public void setAddrData(AddressData aaAddrData)
	{
		caAddrData = aaAddrData;
	}
	/**
	 * @return the ciSavReqId
	 */
	public int getSavReqId()
	{
		return ciSavReqId;
	}
	
	/**
	 * @param ciSavReqId the ciSavReqId to set
	 */
	public void setSavReqId(int ciSavReqId)
	{
		this.ciSavReqId = ciSavReqId;
	}
	/**
	 * @return the csAddrTypeCd
	 */
	public String getAddrTypeCd()
	{
		return csAddrTypeCd;
	}
	/**
	 * @param csAddrTypeCd the csAddrTypeCd to set
	 */
	public void setAddrTypeCd(String csAddrTypeCd)
	{
		this.csAddrTypeCd = csAddrTypeCd;
	} 

}
