package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * SpecialPlateFixedExpirationMonthData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/26/2007	Created 
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods
 * for SpecialPlateFixedExpirationMonthData.java
 *
 * @version	Special Plates	03/26/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		03/26/2007 16:14:00 
 */
public class SpecialPlateFixedExpirationMonthData
	implements Serializable
{
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	protected int ciFxdExpMo;
	protected String csRegPltCd;

	static final long serialVersionUID = -6647967371646735580L;

	/**
	 * Return value of ciFxdExpMo
	 * 
	 * @return
	 */
	public int getFxdExpMo()
	{
		return ciFxdExpMo;
	}

	/**
	 * Return value of ciRTSEffDate
	 * 
	 * @return
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Return value of ciRTSEffEndDate 
	 * 
	 * @return
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/**
	 * Return value of csRegPltCd
	 * 
	 * @return
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Set value of ciFxdExpMo
	 * 
	 * @param aiFxdExpMo
	 */
	public void setFxdExpMo(int aiFxdExpMo)
	{
		ciFxdExpMo = aiFxdExpMo;
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

	/**
	 * Set value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

}
