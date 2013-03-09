package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * ProductServiceData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	12/08/2010	delete csInvItmCd, ciYrRqrdIndi, get/set
 * 							 methods
 * 							add csItmCd, ciYrReqdIndi, get/set methods
 * 							defect 10695 Ver 6.7.0 			 
 * B Hargrove	01/10/2012	add ciIRPIndi,  get/set methods
 * 							defect 11218 Ver 6.10.0 			 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * ProductServiceData
 * 
 * @version	6.10.0 		01/10/2012 
 * @author	Administrator
 * <br>Creation Date: 	08/30/2001 
 */

public class ProductServiceData implements Serializable, Comparable
{
	// int 
	protected int ciFxdChrgIndi;
	protected int ciCrdtItmIndi;

	// Object 
	protected Dollar caPrice;

	// String 
	protected String csAcctItmCd;
	protected String csPrdctSrvcDesc;

	// defect 10695
	protected int ciYrReqdIndi;
	protected String csItmCd;
	// end defect 10695

	// defect 106218
	protected int ciIRPIndi;
	// end defect 10218

	private final static long serialVersionUID = 2697669061272124842L;

	/**
	 * Compare based upon ProductServiceData.getPrdctSrvcDesc()
	 * 
	 * @param  aaObject Object
	 * 
	 * @return int
	 */
	public int compareTo(Object aaObject)
	{
		ProductServiceData aaProdData = (ProductServiceData) aaObject;

		return getPrdctSrvcDesc().compareTo(
			aaProdData.getPrdctSrvcDesc());
	}
	/**
	 * Returns the value of AcctItmCd
	 * 
	 * @return  String 
	 */
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
	}
	/**
	 * Returns the value of CrdtItmIndi
	 * 
	 * @return  int 
	 */
	public final int getCrdtItmIndi()
	{
		return ciCrdtItmIndi;
	}
	/**
	 * Returns the value of FxdChrgIndi
	 * 
	 * @return  int 
	 */
	public final int getFxdChrgIndi()
	{
		return ciFxdChrgIndi;
	}
	/**
	 * Returns the value of IRPIndi
	 * 
	 * @return  int 
	 */
	public final int getIRPIndi()
	{
		return ciIRPIndi;
	}
	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * Returns the value of PrdctSrvcDesc
	 * 
	 * @return  String 
	 */
	public final String getPrdctSrvcDesc()
	{
		return csPrdctSrvcDesc;
	}
	/**
	 * Returns the value of Price 
	 * 
	 * @return Dollar
	 */
	public Dollar getPrice()
	{
		return caPrice;
	}
	/**
	 * Returns the value of YrReqdIndi
	 * 
	 * @return  int 
	 */
	public final int getYrReqdIndi()
	{
		return ciYrReqdIndi;
	}
	/**
	 * This method sets the value of AcctItmCd.
	 * 
	 * @param asAcctItmCd   String 
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}
	/**
	 * This method sets the value of CrdtItmIndi.
	 * 
	 * @param aiCrdtItmIndi   int 
	 */
	public final void setCrdtItmIndi(int aiCrdtItmIndi)
	{
		ciCrdtItmIndi = aiCrdtItmIndi;
	}
	/**
	 * This method sets the value of FxdChrgIndi.
	 * 
	 * @param aiFxdChrgIndi   int 
	 */
	public final void setFxdChrgIndi(int aiFxdChrgIndi)
	{
		ciFxdChrgIndi = aiFxdChrgIndi;
	}
	/**
	 * This method sets the value of IRPIndi.
	 * 
	 * @param aiIRPIndi   int 
	 */
	public final void setIRPIndi(int aiIRPIndi)
	{
		ciIRPIndi = aiIRPIndi;
	}
	/**
	 * This method sets the value of ItmCd.
	 * 
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * This method sets the value of PrdctSrvcDesc.
	 * 
	 * @param asPrdctSrvcDesc   String 
	 */
	public final void setPrdctSrvcDesc(String asPrdctSrvcDesc)
	{
		csPrdctSrvcDesc = asPrdctSrvcDesc;
	}
	/**
	 * This method sets the value of Price 
	 * 
	 * @param aaPrice Dollar
	 */
	public void setPrice(Dollar aaPrice)
	{
		caPrice = aaPrice;
	}
	/**
	 * This method sets the value of YrReqdIndi.
	 * 
	 * @param aiYrReqdIndi   int 
	 */
	public final void setYrReqdIndi(int aiYrReqdIndi)
	{
		ciYrReqdIndi = aiYrReqdIndi;
	}
}
