package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * DisabledPlacardTransToPlacardData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * K Harrell	10/21/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * DisabledPlacardTransToPlacardData.
 *
 * @version	POS_Defect_B	10/21/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008
 */
public class DisabledPlacardTransToPlacardData implements Serializable
{

	int ciDsabldPlcrdIdntyNo;
	int ciTransIdntyNo;
	int ciTransToPlcrdIdntyNo;
	int ciTransTypeCd;
	
	static final long serialVersionUID = -605695298759581128L;

	/**
	 * Get value of ciDsabldPlcrdIdntyNo
	 * 
	 * @return int
	 */
	public int getDsabldPlcrdIdntyNo()
	{
		return ciDsabldPlcrdIdntyNo;
	}

	/**
	 * Get value of ciTransIdntyNo
	 * 
	 * @return int
	 */
	public int getTransIdntyNo()
	{
		return ciTransIdntyNo;
	}

	/**
	 * Get value of ciTransToPlcrdIdntyNo
	 * 
	 * @return int
	 */
	public int getTransToPlcrdIdntyNo()
	{
		return ciTransToPlcrdIdntyNo;
	}

	/**
	 * Get value of ciTransTypeCd
	 * 
	 * @return int
	 */
	public int getTransTypeCd()
	{
		return ciTransTypeCd;
	}

	/**
	 * Set value of ciDsabldPlcrdIdntyNo
	 * 
	 * @param aiDsabldPlcrdIdntyNo
	 */
	public void setDsabldPlcrdIdntyNo(int aiDsabldPlcrdIdntyNo)
	{
		ciDsabldPlcrdIdntyNo = aiDsabldPlcrdIdntyNo;
	}

	/**
	 * Set value of ciTransIdntyNo
	 * 
	 * @param aiTransIdntyNo
	 */
	public void setTransIdntyNo(int aiTransIdntyNo)
	{
		ciTransIdntyNo = aiTransIdntyNo;
	}

	/**
	 * Set value of ciTransToPlcrdIdntyNo
	 * 
	 * @param aiTransToPlcrdIdntyNo
	 */
	public void setTransToPlcrdIdntyNo(int aiTransToPlcrdIdntyNo)
	{
		ciTransToPlcrdIdntyNo = aiTransToPlcrdIdntyNo;
	}

	/**
	 * Set value of ciTransTypeCd
	 * 
	 * @param aiTransTypeCd
	 */
	public void setTransTypeCd(int aiTransTypeCd)
	{
		ciTransTypeCd = aiTransTypeCd;
	}
}
