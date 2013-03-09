package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * IndicatorDescriptionsData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	06/22/2010	add ciERenwlRteIndi, get/set methods  
 * 							defect 10514 Ver 6.5.0 							  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * IndicatorDescriptionsData
 *
 * @version	6.5.0 			06/22/2010	
 * @author	Administrator
 * <br>Creation Date:		08/30/2001
 */

public class IndicatorDescriptionsData implements Serializable
{
	// int
	protected int ciIndiCCOPriority;
	protected int ciIndiRcptPriority;
	protected int ciIndiSalvPriority;
	protected int ciIndiScrnPriority;
	protected int ciJnkCdSysIndi;

	// defect 10514 Ver 6.5.0 
	protected int ciERenwlRteIndi;

	// String 
	protected String csIndiDesc;
	protected String csIndiFieldValue;
	protected String csIndiName;

	private final static long serialVersionUID = 8686429003231949802L;
	
	/**
	 * Creates an IndicatorDescriptionsData
	 */
	public IndicatorDescriptionsData()
	{
		super();
	}

	/**
	 * Returns ERenwlRteIndi
	 * 
	 * @return int 
	 */
	public int getERenwlRteIndi()
	{
		return ciERenwlRteIndi;
	}

	/**
	 * Returns the IndiCCOPriority
	 * 
	 * @return  int 
	 */
	public final int getIndiCCOPriority()
	{
		return ciIndiCCOPriority;
	}
	/**
	 * Returns the IndiDesc.
	 * 
	 * @return  String 
	 */
	public final String getIndiDesc()
	{
		return csIndiDesc;
	}
	/**
	 * Returns the IndiFieldValue.
	 * 
	 * @return  String 
	 */
	public final String getIndiFieldValue()
	{
		return csIndiFieldValue;
	}
	/**
	 * Returns the IndiName.
	 * 
	 * @return  String 
	 */
	public final String getIndiName()
	{
		return csIndiName;
	}
	/**
	 * Returns the IndiRcptPriority.
	 * 
	 * @return  int 
	 */
	public final int getIndiRcptPriority()
	{
		return ciIndiRcptPriority;
	}
	/**
	 * Returns the IndiSalvPriority.
	 * 
	 * @return  int 
	 */
	public final int getIndiSalvPriority()
	{
		return ciIndiSalvPriority;
	}
	/**
	 * Returns the IndiScrnPriority.
	 * 
	 * @return  int 
	 */
	public final int getIndiScrnPriority()
	{
		return ciIndiScrnPriority;
	}
	/**
	 * Returns the JnkCdSysIndi.
	 * 
	 * @return  int 
	 */
	public final int getJnkCdSysIndi()
	{
		return ciJnkCdSysIndi;
	}
	/**
	 * Sets ERenwlRteIndi
	 * 
	 * @param aiERenwlRteIndi
	 */
	public void setERenwlRteIndi(int aiERenwlRteIndi)
	{
		ciERenwlRteIndi = aiERenwlRteIndi;
	}
	/**
	 * Sets the IndiCCOPriority
	 * 
	 * @param aiIndiCCOPriority
	 */
	public final void setIndiCCOPriority(int aiIndiCCOPriority)
	{
		ciIndiCCOPriority = aiIndiCCOPriority;
	}
	/**
	 * Sets the IndiDesc
	 * 
	 * @param asIndiDesc
	 */
	public final void setIndiDesc(String asIndiDesc)
	{
		csIndiDesc = asIndiDesc;
	}
	/**
	 * Sets the IndiFieldValue
	 * 
	 * @param asIndiFieldValue
	 */
	public final void setIndiFieldValue(String asIndiFieldValue)
	{
		csIndiFieldValue = asIndiFieldValue;
	}
	/**
	 * Sets the IndiName.
	 * 
	 * @param asIndiName
	 */
	public final void setIndiName(String asIndiName)
	{
		csIndiName = asIndiName;
	}
	/**
	 * Sets the IndiRcptPriority.
	 * 
	 * @param aIndiRcptPriority
	 */
	public final void setIndiRcptPriority(int aIndiRcptPriority)
	{
		ciIndiRcptPriority = aIndiRcptPriority;
	}
	/**
	 * Sets the IndiSalvPriority.
	 * 
	 * @param aiIndiSalvPriority
	 */
	public final void setIndiSalvPriority(int aiIndiSalvPriority)
	{
		ciIndiSalvPriority = aiIndiSalvPriority;
	}
	/**
	 * Sets the IndiScrnPriority.
	 * 
	 * @param aiIndiScrnPriority
	 */
	public final void setIndiScrnPriority(int aiIndiScrnPriority)
	{
		ciIndiScrnPriority = aiIndiScrnPriority;
	}
	/**
	 * Sets the JnkCdSysIndi.
	 * 
	 * @param aiJnkCdSysIndi
	 */
	public final void setJnkCdSysIndi(int aiJnkCdSysIndi)
	{
		ciJnkCdSysIndi = aiJnkCdSysIndi;
	}
}
