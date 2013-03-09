package com.txdot.isd.rts.webservices.ren.data;
/*
 * RtsTitleData.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/15/2011	Initial load.
 * 							Defect 10670 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Title Data for Web Agent
 *
 * @version	6.7.0			01/15/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/15/2011 13:51:13
 */

public class RtsTitleData
{
	private String csDocNo = "";
	
	/**
	 * Get the Document Number for the Title.
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Set the Document Number for the Title.
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

}
