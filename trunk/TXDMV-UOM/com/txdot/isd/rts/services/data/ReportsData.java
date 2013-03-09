package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * ReportsData.java
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
 * ReportsData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	08/30/2001  
 */

public class ReportsData implements Serializable
{
	// int
	protected int ciRptCategoryId;
	protected int ciRptNumber;

	// String 
	protected String csRptDesc;
	protected String csRptFileName;

	private final static long serialVersionUID = -1365451540161417402L;

	/**
	 * Returns the value of RptCategoryId
	 * @return  int 
	 */
	public final int getRptCategoryId()
	{
		return ciRptCategoryId;
	}
	/**
	 * Returns the value of RptDesc
	 * @return  String 
	 */
	public final String getRptDesc()
	{
		return csRptDesc;
	}
	/**
	 * Returns the value of RptFileName
	 * @return  String 
	 */
	public final String getRptFileName()
	{
		return csRptFileName;
	}
	/**
	 * Returns the value of RptNumber
	 * @return  int 
	 */
	public final int getRptNumber()
	{
		return ciRptNumber;
	}
	/**
	 * This method sets the value of RptCategoryId.
	 * @param aiRptCategoryId   int 
	 */
	public final void setRptCategoryId(int aiRptCategoryId)
	{
		ciRptCategoryId = aiRptCategoryId;
	}
	/**
	 * This method sets the value of RptDesc.
	 * @param asRptDesc   String 
	 */
	public final void setRptDesc(String asRptDesc)
	{
		csRptDesc = asRptDesc;
	}
	/**
	 * This method sets the value of RptFileName.
	 * @param asRptFileName   String 
	 */
	public final void setRptFileName(String asRptFileName)
	{
		csRptFileName = asRptFileName;
	}
	/**
	 * This method sets the value of RptNumber.
	 * @param aiRptNumber   int 
	 */
	public final void setRptNumber(int aiRptNumber)
	{
		ciRptNumber = aiRptNumber;
	}
}
