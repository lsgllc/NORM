package com.txdot.isd.rts.webservices.ren.data;
/*
 * RtsSpecialPlates.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/07/2011	Initial load.
 * 							Defect 10670 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Special Plates Data for Web Agent
 *
 * @version	6.7.0			02/07/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		02/07/2011 09:21:28
 */

public class RtsSpecialPlates
{
	private int ciNewPltExpMo;
	private int ciNewPltExpYr;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private String csOrgNo = " ";
	private int ciAddlSetIndi;
	private int ciPltValidityTerm;
		
	/**
	 * Get the New Expiration Month for the Plate.
	 * 
	 * @return int
	 */
	public int getNewPltExpMo()
	{
		return ciNewPltExpMo;
	}

	/**
	 * Get the New Expiration Year for the Plate.
	 * 
	 * @return int
	 */
	public int getNewPltExpYr()
	{
		return ciNewPltExpYr;
	}

	/**
	 * Set the New Expiration Month for the Plate.
	 * 
	 * @param aiNewPltExpMo
	 */
	public void setNewPltExpMo(int aiNewPltExpMo)
	{
		ciNewPltExpMo = aiNewPltExpMo;
	}

	/**
	 * Set the New Expiration Year for the Plate.
	 * 
	 * @param aiNewPltExpYr
	 */
	public void setNewPltExpYr(int aiNewPltExpYr)
	{
		ciNewPltExpYr = aiNewPltExpYr;
	}
	
	/**
	 * Get the Current Expiration Month for the Plate.
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Get the Current Expiration Year for the Plate.
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}
	
	/**
	 * Set the Expiration Year for the Plate.
	 * 
	 * @param aiPltExpYr
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}
	
	/**
	 * Set the Expiration Month for the Plate.
	 * 
	 * @param aiPltExpMo
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}
	
	/**
	 * Get the Org Number for the Plate.
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}
	
	/**
	 * Set the Org. Number for the plate.
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}
	
	/**
	 * Set the Addtional Set Indicator.
	 * 
	 * @param aiAddlSetIndi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}
	
	/**
	 * Get Additional Set Indicator.
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Get the Plate Validity Term.
	 * 
	 * @return int
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}
	
	/**
	 * Set the Plate Validity Term.
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}
	
}
