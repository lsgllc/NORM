package com.txdot.isd.rts.server.v21.transaction.data;
/*
 * WsPLDV21ResData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/15/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Transaction - Plate Disposition Result.
 *
 * @version	3_Amigos_PH_A	02/15/2008
 * @author	B Hargrove
 * <br>Creation Date:		02/06/2008 13:36
 */
public class WsPLDV21ResData
{
	private String csResult = "";
	
	/**
	 * Returns Result.
	 * 
	 * @return String
	 */
	public String getResult()
	{
		return csResult;
	}
	
	/**
	 * Sets Result.
	 * 
	 * @param String asResult
	 */
	public void setResult(String asResult)
	{
		csResult = asResult;
	}

}
