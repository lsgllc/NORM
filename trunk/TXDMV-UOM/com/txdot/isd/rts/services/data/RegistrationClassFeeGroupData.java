package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * RegistrationClassFeeGroupData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/07/2011	Created
 * 							defect 10695 Ver 6.7.0 
 * K Harrell	01/11/2011	RegClassFeeGrpCd to String
 * 							defect 10695 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * RegistrationClassFeeGroupData
 * 
 *
 * @version	6.7.0			01/11/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/08/2010 15:37:17
 */
public class RegistrationClassFeeGroupData implements Serializable
{
	
	private int ciBegWtRnge;
	private int ciEndWtRnge;
	
	private String csRegClassFeeGrpCd;
	
	private Dollar cdRegClassGrpRegFee;

	static final long serialVersionUID = -4269301627441422474L;

	/**
	 * Get value of ciBegWtRnge
	 * 
	 * @return int
	 */
	public int getBegWtRnge()
	{
		return ciBegWtRnge;
	}

	/**
	 * Get value of ciEndWtRnge
	 * 
	 * @return int
	 */
	public int getEndWtRnge()
	{
		return ciEndWtRnge;
	}

	/**
	 * Get value of csRegClassFeeGrpCd
	 * 
	 * @return String
	 */
	public String getRegClassFeeGrpCd()
	{
		return csRegClassFeeGrpCd;
	}

	/**
	 * Get value of cdRegClassGrpRegFee
	 * 
	 * @return Dollar
	 */
	public Dollar getRegClassGrpRegFee()
	{
		return cdRegClassGrpRegFee;
	}

	/**
	 * Set value of ciBegWtRnge
	 * 
	 * @param aiBegWtRnge
	 */
	public void setBegWtRnge(int aiBegWtRnge)
	{
		ciBegWtRnge = aiBegWtRnge;
	}

	/**
	 * Set value of ciEndWtRnge
	 * 
	 * @param aiEndWtRnge
	 */
	public void setEndWtRnge(int aiEndWtRnge)
	{
		ciEndWtRnge = aiEndWtRnge;
	}

	/**
	 * Set value of csRegClassFeeGrpCd
	 * 
	 * @param asRegClassFeeGrpCd
	 */
	public void setRegClassFeeGrpCd(String asRegClassFeeGrpCd)
	{
		csRegClassFeeGrpCd = asRegClassFeeGrpCd;
	}

	/**
	 * Set value of cdRegClassGrpRegFee
	 * 
	 * @param adRegClassGrpRegFee
	 */
	public void setRegClassGrpRegFee(Dollar adRegClassGrpRegFee)
	{
		cdRegClassGrpRegFee = adRegClassGrpRegFee;
	}

}
