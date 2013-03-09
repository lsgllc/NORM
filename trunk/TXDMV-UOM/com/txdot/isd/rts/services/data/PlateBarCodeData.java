package com.txdot.isd.rts.services.data;

/*
 *
 * PlateBarCodeData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		06/23/2004 	Add Barcode Type and Version
 *							getter/setter for Barcode Utility
 *							add getVersion(), setVersion
 *							getType(), setType()
 *							defect 7108,  Ver 5.2.1
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3										
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * PlateBarCodeData
 * 
 * 
 * @version	5.2.3			04/21/2005
 * @author 	Michael Abernethy
 * <br>Creation Date:		09/05/2001
 */

public class PlateBarCodeData
{
	// String 
	private String csItemCd;
	private String csItemNo;
	private String csItemYr;
	private String csType = "";
	private String csVersion;

	/**
	 * PlateBarCodeData constructor comment.
	 */
	public PlateBarCodeData()
	{
		super();
	}
	/**
	 * Return value of ItemCd
	 * 
	 * @return String
	 */
	public String getItemCd()
	{
		return csItemCd;
	}
	/**
	 * Return value of ItemNo
	 * 
	 * @return String
	 */
	public String getItemNo()
	{
		return csItemNo;
	}
	/**
	 * Return value of ItemYr
	 * 
	 * @return String
	 */
	public String getItemYr()
	{
		return csItemYr;
	}
	/**
	 * Return value of Type
	 * 
	 * @return String
	 */
	public String getType()
	{
		return csType;
	}
	/**
	 * Return value of Version
	 * 
	 * @return String
	 */
	public String getVersion()
	{
		return csVersion;
	}
	/**
	 * Set value of ItemCd
	 * 
	 * @param asItemCd String
	 */
	public void setItemCd(String asItemCd)
	{
		csItemCd = asItemCd;
	}
	/**
	 * Set value of ItemNo
	 * 
	 * @param asItemNo String
	 */
	public void setItemNo(String asItemNo)
	{
		csItemNo = asItemNo;
	}
	/**
	 * Set value of ItemYr
	 * 
	 * @param asItemYr String
	 */
	public void setItemYr(String asItemYr)
	{
		csItemYr = asItemYr;
	}
	/**
	 * Set value of Type
	 * 
	 * @param asType String
	 */
	public void setType(String asType)
	{
		csType = asType;
	}
	/**
	 * Set value of Version
	 * 
	 * @param asVersion String
	 */
	public void setVersion(String asVersion)
	{
		csVersion = asVersion;
	}
}
