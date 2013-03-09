package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * PlateSurchargeData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created;
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/03/2010	add ciPltValidityTerm, get/set methods
 * 							defect 10366 Ver POS_640    
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * PlateSurchargeData
 *
 * @version	POS_640			02/03/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		01/31/2007 16:52:00
 */

public class PlateSurchargeData implements Serializable
{
	// int
	protected int ciAddlSetIndi;
	protected int ciApplIndi;
	// defect 10366 
	protected int ciPltValidityTerm;
	// end defect 10366
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	
	// Dollar 
	protected Dollar caPltSurchargeFee;

	// String
	protected String csAcctItmCd;
	protected String csOrgNo;
	protected String csRegPltCd;
	
	static final long serialVersionUID = 8579120217562689685L;

	/**
	 * Return value of csAcctItmCd
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Return value of ciAddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Return value of ciApplIndi
	 * 
	 * @return int
	 */
	public int getApplIndi()
	{
		return ciApplIndi;
	}

	/**
	 * Return value of csOrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Return value of caPltSurchargeFee
	 * 
	 * @return Dollar
	 */
	public Dollar getPltSurchargeFee()
	{
		return caPltSurchargeFee;
	}

	/**
	 * Return value of PltValidityTerm
	 * 
	 * @return int
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}

	/**
	 * Return value of csRegPltCd 
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Return value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Return value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
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
	 * Set value of ciAddlSetIndi
	 * 
	 * @param aiAddlSetIndi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Set value of ciApplIndi
	 * 
	 * @param aiApplIndi
	 */
	public void setApplIndi(int aiApplIndi)
	{
		ciApplIndi = aiApplIndi;
	}

	/**
	 * Set value of csOrgNo 
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Set value of caPltSurchargeFee
	 * 
	 * @param aaPltSurchargeFee
	 */
	public void setPltSurchargeFee(Dollar aaPltSurchargeFee)
	{
		caPltSurchargeFee = aaPltSurchargeFee;
	}

	/**
	 * Set value of PltValidityTerm
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}

	/**
	 * Set value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set value of ciRTSEffDate 
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Set value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
}
