package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * BusinessPartnerData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/07/2011 	Created
 * 							defect 10726 Ver 6.7.0 
 * K Harrell	04/08/2011	remove csBsnPartnerNo, get/set methods 
 * 							defect 10798 Ver 6.7.1  
 * ---------------------------------------------------------------------
 */

/**
 * Data object for RTS.RTS_BSN_PARTNER
 *
 * @version	6.7.1			04/08/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/07/2011 10:06:17 
 */
public class BusinessPartnerData extends NameAddressData implements Serializable
{
	// int
	private int ciELienRdyIndi;
	private int ciOfcIssuanceNo;
	private int ciRTSEffDate;
	private int ciRTSEffEndDate;
	
	// String
	private String csBsnPartnerId;
	
	// defect 10798 
	// private String csBsnPartnerNo;
	// end defect 10798 
	
	private String csBsnPartnerTypeCd;
	private String csEmail;
	private String csPhone;
	private String csVendorAgntId;
	
	static final long serialVersionUID = -4837237747957063099L;	
	
	/**
	 * BusinessPartnerData.java Constructor
	 */
	public BusinessPartnerData()
	{
		super();
	}

	/**
	 * Get value of csBsnPartnerId
	 * 
	 * @return String
	 */
	public String getBsnPartnerId()
	{
		return csBsnPartnerId;
	}

//	/**
//	 * Get value of csBsnPartnerNo
//	 * 
//	 * @return String
//	 */
//	public String getBsnPartnerNo()
//	{
//		return csBsnPartnerNo;
//	}

	/**
	 * Get value of csBsnPartnerTypeCd
	 * 
	 * @return String
	 */
	public String getBsnPartnerTypeCd()
	{
		return csBsnPartnerTypeCd;
	}

	/**
	 * Get value of ciELienRdyIndi
	 * 
	 * @return int
	 */
	public int getELienRdyIndi()
	{
		return ciELienRdyIndi;
	}

	/**
	 * Get value of csEmail
	 * 
	 * @return String
	 */
	public String getEmail()
	{
		return csEmail;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get value of csPhone
	 * 
	 * @return String
	 */
	public String getPhone()
	{
		return csPhone;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Get value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/**
	 * Get value of csVendorAgntId
	 * 
	 * @return String
	 */
	public String getVendorAgntId()
	{
		return csVendorAgntId;
	}

	/**
	 * Get value of csBsnPartnerId
	 * 
	 * @param asBsnPartnerId
	 */
	public void setBsnPartnerId(String asBsnPartnerId)
	{
		csBsnPartnerId = asBsnPartnerId;
	}

//	/**
//	 * Get value of csBsnPartnerNo
//	 * 
//	 * @param asBsnPartnerNo
//	 */
//	public void setBsnPartnerNo(String asBsnPartnerNo)
//	{
//		csBsnPartnerNo = asBsnPartnerNo;
//	}

	/**
	 * Get value of csBsnPartnerTypeCd
	 * 
	 * @param asBsnPartnerTypeCd
	 */
	public void setBsnPartnerTypeCd(String asBsnPartnerTypeCd)
	{
		csBsnPartnerTypeCd = asBsnPartnerTypeCd;
	}

	/**
	 * Get value of ciELienRdyIndi
	 * 
	 * @param aiELienRdyIndi
	 */
	public void setELienRdyIndi(int aiELienRdyIndi)
	{
		ciELienRdyIndi = aiELienRdyIndi;
	}

	/**
	 * Get value of csEmail
	 * 
	 * @param asEmail
	 */
	public void setEmail(String asEmail)
	{
		csEmail = asEmail;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Get value of csPhone
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Get value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}

	/**
	 * Get value of csVendorAgntId
	 * 
	 * @param asVendorAgntId
	 */
	public void setVendorAgntId(String asVendorAgntId)
	{
		csVendorAgntId = asVendorAgntId;
	}

}
