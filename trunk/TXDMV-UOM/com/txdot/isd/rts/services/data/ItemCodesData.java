package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * ItemCodesData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add ciPrntCd, ciPrintableIndi
 * 							getters/setters.
 * 							Ver 5.2.0
 * K Harrell	06/20/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3 			 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get/set methods for 
 * ItemCodesData
 * 
 * @version	5.2.3		06/20/2005 
 * @author	Administrator
 * <br>Creation Date: 	08/30/2001 
 */
public class ItemCodesData
	implements Serializable, java.lang.Comparable
{
	// int
	protected int ciInvProcsngCd;
	protected int ciPrintableIndi;
	protected int ciPrntCd;

	// String 
	protected String csItmCd;
	protected String csItmCdDesc;
	protected String csItmTrckngType;

	private final static long serialVersionUID = 4903068837411814121L;
	/**
	 * Sorts the Data Object by Item Cd Desc
	 * 
	 */
	public int compareTo(Object aaObject)
	{
		ItemCodesData aaItemCdsCacheData = (ItemCodesData) aaObject;
		String lsCurrentString = csItmCdDesc;
		String lsCompareToString = aaItemCdsCacheData.getItmCdDesc();
		return lsCurrentString.compareTo(lsCompareToString);
	}
	/**
	 * Returns the value of InvProcsngCd
	 * 
	 * @return  int  
	 */
	public final int getInvProcsngCd()
	{
		return ciInvProcsngCd;
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
	 * Returns the value of ItmCdDesc
	 * 
	 * @return  String 
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	/**
	 * Returns the value of ItmTrckngType
	 * 
	 * @return  String 
	 */
	public final String getItmTrckngType()
	{
		return csItmTrckngType;
	}
	/**
	 * Returns the value of PrintableIndi
	 * 
	 * @return int
	 */
	public int getPrintableIndi()
	{
		return ciPrintableIndi;
	}
	/**
	 * Returns the value of PrntCd
	 * 			
	 * @return int
	 */
	public int getPrntCd()
	{
		return ciPrntCd;
	}
	/**
	 * Returns boolean to determine if Printable
	 * 
	 * @return boolean
	 */
	public boolean isPrintable()
	{
		return (getPrintableIndi() > 0);
	}
	/**
	 * This method sets the value of InvProcsngCd.
	 * 
	 * @param aiInvProcsngCd   int  
	 */
	public final void setInvProcsngCd(int aiInvProcsngCd)
	{
		ciInvProcsngCd = aiInvProcsngCd;
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
	 * This method sets the value of ItmCdDesc.
	 * 
	 * @param asItmCdDesc   String 
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	/**
	 * This method sets the value of ItmTrckngType.
	 * 
	 * @param asItmTrckngType   String 
	 */
	public final void setItmTrckngType(String asItmTrckngType)
	{
		csItmTrckngType = asItmTrckngType;
	}
	/**
	 * Sets the value of PrintableIndi
	 * 
	 * @param aiPrintableIndi int
	 */
	public void setPrintableIndi(int aiPrintableIndi)
	{
		ciPrintableIndi = aiPrintableIndi;
	}
	/**
	 * Sets the value of PrntCd
	 * 
	 * @param aiPrntCd int
	 */
	public void setPrntCd(int aiPrntCd)
	{
		ciPrntCd = aiPrntCd;
	}
}
