package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * WebAgencyLogData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0
 * K Harrell	01/10/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0       
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyLogData 
 *
 * @version	6.7.0			01/10/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:16:17 
 */
public class WebAgencyLogData
	extends WebAgencyData
	implements java.io.Serializable
{
	private int ciUpdtngAgntIdntyNo;

	static final long serialVersionUID = 6221869177127032210L;

	/**
	 * WebAgencyLogData.java Constructor
	 * 
	 */
	public WebAgencyLogData()
	{
		super();
	}
	/**
	 * WebAgencyLogData.java Constructor
	 * 
	 */
	public WebAgencyLogData(WebAgencyData aaWebAgencyData)
	{
		super();
		setAgncyIdntyNo(aaWebAgencyData.getAgncyIdntyNo());
		setAgncyTypeCd(aaWebAgencyData.getAgncyTypeCd());
		setName1(aaWebAgencyData.getName1());
		setName2(aaWebAgencyData.getName2());
		AddressData laFromAddrData = aaWebAgencyData.getAddressData();
		AddressData laToAddrData = (AddressData) UtilityMethods.copy(laFromAddrData);
		setAddressData(laToAddrData); 
		setPhone(aaWebAgencyData.getPhone());
		setEMail(aaWebAgencyData.getEMail());
		setCntctName(aaWebAgencyData.getCntctName());
		setInitOfcNo(aaWebAgencyData.getInitOfcNo());
		setDeleteIndi(aaWebAgencyData.getDeleteIndi());
		setChngTimestmp(aaWebAgencyData.getChngTimestmp());
	}

	/**
	 * Get value of ciUpdtngAgntIdntyNo
	 * 
	 * @return int
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
	}

	/**
	 * Ste value of ciUpdtngAgntIdntyNo
	 * 
	 * @param aiUpdtngAgntIdntyNo
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}

}
