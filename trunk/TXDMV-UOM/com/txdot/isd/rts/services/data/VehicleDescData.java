package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleDescData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/05/2005	Java 1.4 Work
 * 							Moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * S Carlin		09/06/2011	Added major and minor color code 
 * 							defect 10985 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleDescData
 *
 * @version	6.8.1		09/06/2011
 * @author	Administrator 
 * <br>Creation Date: 	10/02/2001 10:44:07 
 */

public class VehicleDescData implements Serializable
{
	private String csModelYr;
	private String csMake;
	private String csExpMo;
	private String csExpYr;
	private String csPlateAge;
	private String csEmptyWeight;
	private String csCarryingCapacity;
	private String csGrossWeight;
	private String csTonnage;
	private String csTitleIssDt;
	private String csOwnerName;
	private String csMajorColorCd;
	private String csMinorColorCd;

	private final static long serialVersionUID = 8973166982446679676L;

	/**
	 * VehicleDescData constructor comment.
	 */
	public VehicleDescData()
	{
		super();

		csModelYr = null;
		csMake = null;
		csExpMo = null;
		csExpYr = null;
		csPlateAge = null;
		csEmptyWeight = null;
		csCarryingCapacity = null;
		csGrossWeight = null;
		csTonnage = null;
		csTitleIssDt = null;
		csOwnerName = null;
	}
	/**
	 * Return value of CarryingCapacity
	 * 
	 * @return String 
	 */
	public String getCarryingCapacity()
	{
		return csCarryingCapacity;
	}
	/**
	 * Return value of EmptyWeight
	 * 
	 * @return String 
	 */
	public String getEmptyWeight()
	{
		return csEmptyWeight;
	}
	/**
	 * Return value of ExpMo
	 * 
	 * @return String 
	 */
	public String getExpMo()
	{
		return csExpMo;
	}
	/**
	 * Return value of ExpYr
	 * 
	 * @return String
	 */
	public String getExpYr()
	{
		return csExpYr;
	}
	/**
	 * Return value of GrossWeight
	 * 
	 * @return String 
	 */
	public String getGrossWeight()
	{
		return csGrossWeight;
	}
	/**
	 * Return value of Make
	 * 
	 * @return String
	 */
	public String getMake()
	{
		return csMake;
	}
	/**
	 * Return value of ModelYr
	 * 
	 * @return String
	 */
	public String getModelYr()
	{
		return csModelYr;
	}
	/**
	 * Return value of OwnerName
	 * 
	 * @return String
	 */
	public String getOwnerName()
	{
		return csOwnerName;
	}
	/**
	 * Return value of PlateAge
	 * 
	 * @return String 
	 */
	public String getPlateAge()
	{
		return csPlateAge;
	}
	/**
	 * Return value of TitleIssDt
	 * 
	 * @return String 
	 */
	public String getTitleIssDt()
	{
		return csTitleIssDt;
	}
	/**
	 * Return value of Tonnage
	 * 
	 * @return String 
	 */
	public String getTonnage()
	{
		return csTonnage;
	}
	/**
	 * Set value of CarryingCapacity
	 * 
	 * @param asCarryingCapacity String 
	 */
	public void setCarryingCapacity(String asCarryingCapacity)
	{
		csCarryingCapacity = asCarryingCapacity;
	}
	/**
	 * Set value of EmptyWeight
	 * 
	 * @param asEmptyWeight String 
	 */
	public void setEmptyWeight(String asEmptyWeight)
	{
		csEmptyWeight = asEmptyWeight;
	}
	/**
	 * Set value of ExpMo
	 * 
	 * @param asExpMo String 
	 */
	public void setExpMo(String asExpMo)
	{
		csExpMo = asExpMo;
	}
	/**
	 * Set value of ExpYr
	 * 
	 * @param asExpYr String 
	 */
	public void setExpYr(String asExpYr)
	{
		csExpYr = asExpYr;
	}
	/**
	 * Set value of GrossWeight
	 * 
	 * @param asGrossWeight String
	 */
	public void setGrossWeight(String asGrossWeight)
	{
		csGrossWeight = asGrossWeight;
	}
	/**
	 * Set value of Make 
	 * 
	 * @param asMake String
	 */
	public void setMake(String asMake)
	{
		csMake = asMake;
	}
	/**
	 * Set value of ModelYr 
	 *
	 * @param asModelYr String
	 */
	public void setModelYr(String asModelYr)
	{
		csModelYr = asModelYr;
	}
	/**
	 * Set value of OwnerName
	 * 
	 * @param asOwnerName String
	 */
	public void setOwnerName(String asOwnerName)
	{
		csOwnerName = asOwnerName;
	}
	/**
	 * Set value of PlateAge
	 * 
	 * @param asPlateAge String
	 */
	public void setPlateAge(String asPlateAge)
	{
		csPlateAge = asPlateAge;
	}
	/**
	 * Set value of TitleIssDt
	 * 
	 * @param asTitleIssDt String
	 */
	public void setTitleIssDt(String asTitleIssDt)
	{
		csTitleIssDt = asTitleIssDt;
	}
	/**
	 * Set value of Tonnage
	 * 
	 * @param asTonnage String
	 */
	public void setTonnage(String asTonnage)
	{
		csTonnage = asTonnage;
	}
	/**
	 * @return the majorColorCd
	 */
	public String getMajorColorCd()
	{
		return csMajorColorCd;
	}
	/**
	 * @param majorColorCd the majorColorCd to set
	 */
	public void setMajorColorCd(String majorColorCd)
	{
		this.csMajorColorCd = majorColorCd;
	}

	/**
	 * @return the minorColorCd
	 */
	public String getMinorColorCd()
	{
		return csMinorColorCd;
	}
	/**
	 * @param minorColorCd the minorColorCd to set
	 */
	public void setMinorColorCd(String minorColorCd)
	{
		this.csMinorColorCd = minorColorCd;
	}

}
