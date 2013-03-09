package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change HistorSets the value of y:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created;
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/08/2007	Add csOrgNo, get/set methods
 * 							defect 9085 Ver Special Plates  
 * K Harrell	02/16/2010	add ciTermBasedIndi, get/set methods. 
 * 							defect 10366 Ver POS_640
 * J Zwiener	01/24/2011	add csRestyleAcctItmCd, get/set methods.
 * 							defect 10627 Ver POS_670
 * J Zwiener	02/01/2011	add ciCrossoverIndi, get/set methods.
 * 							add ciCrossoverPosDate, get/set methods.
 * 							defect 10704 Ver POS_670 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * OrganizationNumberData
 *
 * @version	POS_670 	02/01/2011
 * @author	Kathy Harrell
 * <br>Creation Date:	01/31/2007 16:13:00
 */
public class OrganizationNumberData implements Serializable
{
	//int
	protected int ciMfgPndingIndi;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	protected int ciSunsetDate;
	
	// defect 10366 
	protected int ciTermBasedIndi;
	// end defect 10366

	// defect 10704
	protected int ciCrossoverIndi;
	protected int ciCrossoverPosDate;
	// end defect 10704   

	// String
	protected String csAcctItmCd;
	protected String csBaseRegPltCd;
	protected String csOrgNo;
	protected String csOrgNoDesc;
	protected String csSponsorPltGrpId;
	
	// defect 10627
	protected String csRestyleAcctItmCd;
	// end defect 10627
	
	static final long serialVersionUID = -3459117487943637328L;

	/**
	 * Returns the value of csAcctItmCd
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Returns the value of csBaseRegPltCdd
	 * 
	 * @return String
	 */
	public String getBaseRegPltCd()
	{
		return csBaseRegPltCd;
	}
	
	/**
	 * Returns the value of ciCrossoverIndi
	 * 
	 * @return int
	 */
	public int getCrossoverIndi()
	{
		return ciCrossoverIndi;
	}

	/**
	 * Returns the value of ciCrossoverPosDate
	 * 
	 * @return int
	 */
	public int getCrossoverPosDate()
	{
		return ciCrossoverPosDate;
	}

	/**
	 * Returns the value of ciMfgPndingIndi
	 * 
	 * @return int
	 */
	public int getMfgPndingIndi()
	{
		return ciMfgPndingIndi;
	}

	/**
	 * Returns the value of csOrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Returns the value of csOrgNoDesc
	 * 
	 * @return String
	 */
	public String getOrgNoDesc()
	{
		return csOrgNoDesc;
	}

	/**
	 * Returns the value of csRestyleAcctItmCd
	 * 
	 * @return String
	 */
	public String getRestyleAcctItmCd()
	{
		return csRestyleAcctItmCd;
	}

	/**
	 * Returns the value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Returns the value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	
	/**
	 * Returns the value of csSponsorPltGrpId
	 * 
	 * @return String
	 */
	public String getSponsorPltGrpId()
	{
		return csSponsorPltGrpId;
	}

	/**
	 * Returns the value of ciSunsetDate
	 * 
	 * @return int
	 */
	public int getSunsetDate()
	{
		return ciSunsetDate;
	}

	/**
	 * Gets the value of ciVendorPrtpIndi
	 * 
	 * @return int
	 */
	public int getTermBasedIndi()
	{
		return ciTermBasedIndi;
	}

	/**
	 * Sets the value of csAcctItmCd
	 * 
	 * @param asAcctItmCd
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * Sets the value of csBaseRegPltCd
	 * 
	 * @param asBaseRegPltCd
	 */
	public void setBaseRegPltCd(String asBaseRegPltCd)
	{
		csBaseRegPltCd = asBaseRegPltCd;
	}

	/**
	 * Sets the value of ciCrossoverIndi
	 * 
	 * @param aiCrossoverIndi
	 */
	public void setCrossoverIndi(int aiCrossoverIndi)
	{
		ciCrossoverIndi = aiCrossoverIndi;
	}

	/**
	 * Sets the value of ciCrossoverPosDate
	 * 
	 * @param aiCrossoverPosDate
	 */
	public void setCrossoverPosDate(int aiCrossoverPosDate)
	{
		ciCrossoverPosDate = aiCrossoverPosDate;
	}

	/**
	 * Sets the value of ciMfgPndingIndi
	 * 
	 * @param aiMfgPndingIndi
	 */
	public void setMfgPndingIndi(int aiMfgPndingIndi)
	{
		ciMfgPndingIndi = aiMfgPndingIndi;
	}

	/**
	 * Sets the value of csOrgNo
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Sets the value of csOrgNoDesc
	 * 
	 * @param asOrgNoDesc
	 */
	public void setOrgNoDesc(String asOrgNoDesc)
	{
		csOrgNoDesc = asOrgNoDesc;
	}
	/**
	 * Sets the value of csRestyleAcctItmCd
	 * 
	 * @param asRestyleAcctItmCd
	 */
	public void setRestyleAcctItmCd(String asRestyleAcctItmCd)
	{
		csRestyleAcctItmCd = asRestyleAcctItmCd;
	}

	/**
	 * Sets the value of ciRTSEffDate
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Sets the value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}

	/**
	 * Sets the value of csSponsorPltGrpId
	 * 
	 * @param asSponsorPltGrpId
	 */
	public void setSponsorPltGrpId(String asSponsorPltGrpId)
	{
		csSponsorPltGrpId = asSponsorPltGrpId;
	}

	/**
	 * Sets the value of ciSunsetDate
	 * 
	 * @param aiSunsetDate
	 */
	public void setSunsetDate(int aiSunsetDate)
	{
		ciSunsetDate = aiSunsetDate;
	}

	/**
	 * Sets the value of ciTermBasedIndi
	 * 
	 * @param aiTermBasedIndi
	 */
	public void setTermBasedIndi(int aiTermBasedIndi)
	{
		ciTermBasedIndi = aiTermBasedIndi;
	}
}
