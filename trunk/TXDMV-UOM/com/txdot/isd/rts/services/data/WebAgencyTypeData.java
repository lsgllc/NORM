package com.txdot.isd.rts.services.data;
/*
 * WebAgencyTypeData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0 
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0      
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyTypeData
 *
 * @version	6.7.0  			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 17:48:17
 */
public class WebAgencyTypeData implements java.io.Serializable
{
	private String csAgncyTypeCd;
	private String csAgncyTypeDesc;
	
	static final long serialVersionUID = -427899398763305022L;

	/**
	 * WebAgencyTypeData.java Constructor
	 * 
	 */
	public WebAgencyTypeData()
	{
		super();
	}

	/**
	 * Get value of csAgncyTypeCd
	 * 
	 * @return String 
	 */
	public String getAgncyTypeCd()
	{
		return csAgncyTypeCd;
	}

	/**
	 * Get value of csAgncyTypeDesc
	 * 
	 * @return String 
	 */
	public String getAgncyTypeDesc()
	{
		return csAgncyTypeDesc;
	}

	/**
	 * Set value of csAgncyTypeCd
	 * 
	 * @param asAgncyTypeCd
	 */
	public void setAgncyTypeCd(String asAgncyTypeCd)
	{
		csAgncyTypeCd = asAgncyTypeCd;
	}

	/**
	 * Set value of csAgncyTypeDesc
	 * 
	 * @param asAgncyTypeDesc
	 */
	public void setAgncyTypeDesc(String asAgncyTypeDesc)
	{
		csAgncyTypeDesc = asAgncyTypeDesc;
	}
}
