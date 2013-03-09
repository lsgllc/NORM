package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * TitleTERPPercentData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * TitleTERPPercentData
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	07/30/2003 21:35:49  
 */

public class TitleTERPPercentData implements Serializable
{
	// int 
	protected int ciBegDieselWtRnge;
	protected int ciBegModlYrRnge;
	protected int ciDieselIndi;
	protected int ciEndDieselWtRnge;
	protected int ciEndModlYrRnge;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	// Object 
	protected Dollar caTtlTERPPrcnt;

	// String 
	protected String csSalesTaxCat;
	protected String csTERPPrcntAcctItmCd;

	private final static long serialVersionUID = -5047626302256561302L;

	/**
	 * Returns the value of BegDieselWtRnge
	 * @return int
	 */
	public final int getBegDieselWtRnge()
	{
		return ciBegDieselWtRnge;
	}
	/**
	 * Returns the value of BegModlYrRnge
	 * @return int
	 */
	public final int getBegModlYrRnge()
	{
		return ciBegModlYrRnge;
	}
	/**
	 * Returns the value of DieselIndi
	 * @return int
	 */
	public final int getDieselIndi()
	{
		return ciDieselIndi;
	}
	/**
	 * Returns the value of EndDieselWtRnge
	 * @return int
	 */
	public final int getEndDieselWtRnge()
	{
		return ciEndDieselWtRnge;
	}
	/**
	 * Returns the value of EndModlYrRnge
	 * @return int
	 */
	public final int getEndModlYrRnge()
	{
		return ciEndModlYrRnge;
	}
	/**
	 * Returns the value of RTSEffDate
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Returns the value of RTSEffEndDate
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	/**
	 * Returns the value of SalesTaxCat 
	 * @return int
	 */
	public final String getSalesTaxCat()
	{
		return csSalesTaxCat;
	}
	/**
	 * Returns the value of TERPPrcntAcctItmCd 
	 * @return String
	 */
	public final String getTERPPrcntAcctItmCd()
	{
		return csTERPPrcntAcctItmCd;
	}
	/**
	 * Returns the value of TtlTERPPrcnt
	 * @return Dollar
	 */
	public final Dollar getTtlTERPPrcnt()
	{
		return caTtlTERPPrcnt;
	}
	/**
	 * Sets the value of BegDieselWtRnge .
	 * @param aiBegDieselWtRnge int
	 */
	public final void setBegDieselWtRnge(int aiBegDieselWtRnge)
	{
		ciBegDieselWtRnge = aiBegDieselWtRnge;
	}
	/**
	 * Sets the value of BegModlYrRnge .
	 * @param aiBegModlYrRnge int
	 */
	public final void setBegModlYrRnge(int aiBegModlYrRnge)
	{
		ciBegModlYrRnge = aiBegModlYrRnge;
	}
	/**
	 * Sets the value of DieselIndi  .
	 * @param aiDieselIndi int
	 */
	public final void setDieselIndi(int aiDieselIndi)
	{
		ciDieselIndi = aiDieselIndi;
	}
	/**
	 * Sets the value of EndDieselWtRnge
	 * @param aiEndDieselWtRnge int
	 */
	public final void setEndDieselWtRnge(int aiEndDieselWtRnge)
	{
		ciEndDieselWtRnge = aiEndDieselWtRnge;
	}
	/**
	 * Sets the value of EndModlYrRnge  
	 * @param aiEndModlYrRnge int
	 */
	public final void setEndModlYrRnge(int aiEndModlYrRnge)
	{
		ciEndModlYrRnge = aiEndModlYrRnge;
	}
	/**
	 * Sets the value of RTSEffDate
	 * @param aiRTSEffDate int
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * Sets the value of RTSEffEndDate
	 * @param aiRTSEffEndDate int
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
	/**
	 * Sets the value of SalesTaxCat    
	 * @param aiSalesTaxCat int
	 */
	public final void setSalesTaxCat(String aiSalesTaxCat)
	{
		csSalesTaxCat = aiSalesTaxCat;
	}
	/**
	 * Sets the value of TERPPrcntAcctItmCd
	 * @param asTERPPrcntAcctItmCd String
	 */
	public final void setTERPPrcntAcctItmCd(String asTERPPrcntAcctItmCd)
	{
		csTERPPrcntAcctItmCd = asTERPPrcntAcctItmCd;
	}
	/**
	 * Sets the value of TtlTERPPrcnt
	 * @param aaTtlTERPPrcnt Dollar
	 */
	public void setTtlTERPPrcnt(Dollar aaTtlTERPPrcnt)
	{
		caTtlTERPPrcnt = aaTtlTERPPrcnt;
	}
}
