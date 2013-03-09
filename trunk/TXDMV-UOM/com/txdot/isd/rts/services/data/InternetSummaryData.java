package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * InternetSummaryData.java 
 *
 * (c) Texas Department of Transportation  2003
 *
 *----------------------------------------------------------------------
 * Change History 
 * Name         Date        Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/23/2003 	Created 
 * K Harrell	09/29/2005	Moved from webapps.data. Java 1.4 cleanup. 
 * 							defect 7889 Ver 5.2.3    
 *----------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database
 * 
 * @version	5.2.3 		09/29/2005    
 * @author	Kathy Harrell 
 * <br>Creation Date:	06/23/2003 14:12;57
 *
 */

public class InternetSummaryData implements Serializable
{
	private int ciOfcIssuanceNo;
	private int ciTransYrMo;
	private int ciTransCount;
	
	private String csTransCd;

	private final static long serialVersionUID = -7474266419051692914L;

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of TransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * Returns the value of TransCount 
	 * 
	 * @return int
	 */
	public int getTransCount()
	{
		return ciTransCount;
	}
	/**
	 * Returns the value of TransYrMo
	 * 
	 * @return int
	 */
	public int getTransYrMo()
	{
		return ciTransYrMo;
	}
	/**
	 * Assign value to OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Assign value to TransCd
	 * 
	 * @param asTransCd String
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
	/**
	 * Assign value to TransCount
	 * 
	 * @param aiTransCount int
	 */
	public void setTransCount(int aiTransCount)
	{
		ciTransCount = aiTransCount;
	}
	/**
	 * Assign value to TransYrMo
	 * 
	 * @param aiTransYrMo int
	 */
	public void setTransYrMo(int aiTransYrMo)
	{
		ciTransYrMo = aiTransYrMo;
	}
}