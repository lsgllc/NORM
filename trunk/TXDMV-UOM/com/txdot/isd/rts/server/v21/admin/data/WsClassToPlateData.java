package com.txdot.isd.rts.server.v21.admin.data;
/*
 * WsClassToPlateData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/16/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Class To Plate.
 *
 * @version	3_Amigos_PH_A	01/16/2008
 * @author	B Hargrove
 * <br>Creation Date:		01/14/2008 13:54
 */
public class WsClassToPlateData
{
	private int ciPTOEligibleIndi = 0;
	private int ciRegistrationClassCode = 0;
	
	private String csRegistrationPlateCode = "";
	private String csReplacementPlateCode = "";
	
	/**
	 * Returns Plate To Owner Eligible Indicator.
	 * 
	 * @return int
	 */
	public int getPTOEligibleIndi()
	{
		return ciPTOEligibleIndi;
	}
	
	/**
	 * Returns Registration Class Code.
	 * 
	 * @return int
	 */
	public int getRegistrationClassCode()
	{
		return ciRegistrationClassCode;
	}
	
	/**
	 * Returns Registration Plate Code.
	 * 
	 * @return String
	 */
	public String getRegistrationPlateCode()
	{
		return csRegistrationPlateCode;
	}
	
	/**
	 * Returns Replacement Plate Code.
	 * 
	 * @return String
	 */
	public String getReplacementPlateCode()
	{
		return csReplacementPlateCode;
	}
	
	/**
	 * Sets Plate To Owner Eligible Indicator.
	 * 
	 * @param int aiPTOEligibleIndi
	 */
	public void setPTOEligibleIndi(int aiPTOEligibleIndi)
	{
		ciPTOEligibleIndi = aiPTOEligibleIndi;
	}
	
	/**
	 * Sets Registration Class Code.
	 * 
	 * @param int aiRegistrationClassCode
	 */
	public void setRegistrationClassCode(int aiRegistrationClassCode)
	{
		ciRegistrationClassCode = aiRegistrationClassCode;
	}
	
	/**
	 * Sets Registration Plate Code.
	 * 
	 * @param String aiRegistrationPlateCode
	 */
	public void setRegistrationPlateCode(String asRegistrationPlateCode)
	{
		csRegistrationPlateCode = asRegistrationPlateCode;
	}
	
	/**
	 * Sets Replacement Plate Code.
	 * 
	 * @param String aiReplacementPlateCode
	 */
	public void setReplacementPlateCode(String asReplacementPlateCode)
	{
		csReplacementPlateCode = asReplacementPlateCode;
	}
	
}
