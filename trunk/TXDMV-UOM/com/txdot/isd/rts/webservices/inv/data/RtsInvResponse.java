package com.txdot.isd.rts.webservices.inv.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsInvResponse.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/01/2008	Created Class
 * 							defect 9679 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * Response data from Inventory Web service.
 *
 * @version	MyPlates_POS		06/01/2008
 * @author	Min Wang
 * <br>Creation Date:			06/01/2008 03:00:00
 */
public class RtsInvResponse extends RtsAbstractResponse
{
	private String csInvItmNo;
	/**
	 * Return the value of InvItmNo
	 * 
	 * @return String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Set the value of InvItmNo
	 * 
	 * @param asInvItmNo
	 */
	public void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}

}
