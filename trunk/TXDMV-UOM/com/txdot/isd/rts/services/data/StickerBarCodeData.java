package com.txdot.isd.rts.services.data;

/*
 *
 * StickerBarCodeData.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
* This Data class contains attributes and get/set methods for 
 * StickerBarCodeData
 * 
 * @version	5.2.3		04/21/2005  
 * @author	Michael Abernethy
 * <br>Creation Date:	09/05/2001 16:45:41 
 */

public class StickerBarCodeData
{
	// String 
	private String csItemCd;
	private String csItemNo;
	private String csItemYr;
	private String csVersion;

	/**
	 * StickerBarCodeData constructor comment.
	 */
	public StickerBarCodeData()
	{
		super();
	}
	/**
	 * Return the value of ItemCd
	 * 
	 * @return String
	 */
	public String getItemCd()
	{
		return csItemCd;
	}
	/**
	 * Return the value of ItemNo
	 * 
	 * @return String
	 */
	public String getItemNo()
	{
		return csItemNo;
	}
	/**
	 * Return the value of ItemYr
	 * 
	 * @return String
	 */
	public String getItemYr()
	{
		return csItemYr;
	}
	/**
	 * Return the value of Version
	 * 
	 * @return String
	 */
	public String getVersion()
	{
		return csVersion;
	}
	/**
	 * Set the value of ItemCd
	 * 
	 * @param asItemCd String
	 */
	public void setItemCd(String asItemCd)
	{
		csItemCd = asItemCd;
	}
	/**
	 * Set the value of ItemNo
	 * 
	 * @param asItemNo String
	 */
	public void setItemNo(String asItemNo)
	{
		csItemNo = asItemNo;
	}
	/**
	 * Set the value of ItemYr
	 * 
	 * @param asItemYr String
	 */
	public void setItemYr(String asItemYr)
	{
		csItemYr = asItemYr;
	}
	/**
	 * Set the value of Version
	 * 
	 * @param asVersion String
	 */
	public void setVersion(String asVersion)
	{
		csVersion = asVersion;
	}
}
