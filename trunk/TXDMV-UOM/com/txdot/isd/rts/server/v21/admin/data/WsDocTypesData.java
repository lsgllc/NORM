package com.txdot.isd.rts.server.v21.admin.data;
/*
 * WsDocTypesData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/10/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Document Types.
 *
 * @version	3_Amigos_PH_A	01/10/2008
 * @author	B Hargrove
 * <br>Creation Date:		01/10/2008 14:00
 */
public class WsDocTypesData
{
	private int ciDocTypeCd = 0;
	private int ciRegRecIndi = 0;
	
	private String csDocTypeCdDesc = "";
	
	/**
	 * Returns Document Type Code.
	 * 
	 * @return int
	 */
	public int getDocTypeCd()
	{
		return ciDocTypeCd;
	}
	
	/**
	 * Returns Document Type Code Description.
	 * 
	 * @return String
	 */
	public String getDocTypeCdDesc()
	{
		return csDocTypeCdDesc;
	}
	
	/**
	 * Returns Registration Record Indicator.
	 * 
	 * @return int
	 */
	public int getRegRecIndi()
	{
		return ciRegRecIndi;
	}
	
	/**
	 * Sets Document Type Code.
	 * 
	 * @param int aiDocTypeCd
	 */
	public void setDocTypeCd(int aiDocTypeCd)
	{
		ciDocTypeCd = aiDocTypeCd;
	}
	
	/**
	 * Sets Document Type Code Description.
	 * 
	 * @param String aiDocTypeCdDesc
	 */
	public void setDocTypeCdDesc(String asDocTypeCdDesc)
	{
		csDocTypeCdDesc = asDocTypeCdDesc;
	}
	
	/**
	 * Sets Registration Record Indicator.
	 * 
	 * @param int aiRegRecIndi
	 */
	public void setRegRecIndi(int aiRegRecIndi)
	{
		ciRegRecIndi = aiRegRecIndi;
	}

}
