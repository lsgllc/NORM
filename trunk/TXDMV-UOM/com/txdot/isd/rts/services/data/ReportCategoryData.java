package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * ReportCategoryData.java
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
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * ReportCategoryData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	08/30/2001  
 */

public class ReportCategoryData implements Serializable
{
	// int 
	protected int ciRptCategoryId;

	// String 
	protected String csRptCategoryDesc;

	private final static long serialVersionUID = -7947776835654713515L;
	/**
	 * Returns the value of RptCategoryDesc
	 * @return  String 
	 */
	public final String getRptCategoryDesc()
	{
		return csRptCategoryDesc;
	}
	/**
	 * Returns the value of RptCategoryId
	 * @return  int  
	 */
	public final int getRptCategoryId()
	{
		return ciRptCategoryId;
	}
	/**
	 * This method sets the value of RptCategoryDesc.
	 * @param asRptCategoryDesc   String 
	 */
	public final void setRptCategoryDesc(String asRptCategoryDesc)
	{
		csRptCategoryDesc = asRptCategoryDesc;
	}
	/**
	 * This method sets the value of RptCategoryId.
	 * @param aiRptCategoryId   int  
	 */
	public final void setRptCategoryId(int aiRptCategoryId)
	{
		ciRptCategoryId = aiRptCategoryId;
	}
}
