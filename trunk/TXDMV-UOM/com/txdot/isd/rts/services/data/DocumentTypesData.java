package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * DocumentTypesData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * K Harrell	04/03/2008	add csTtlTrnsfrPnltyExmptCd, 
 * 							  get/set methods
 * 							defect 9583 Ver Defect POS A 
 * K Harrell	05/21/2008	removed csTtlTrnsfrPnltyExmptCd, get/set 
 * 							methods; No longer required. 
 * 							defect 9583 Ver Defect POS A
 * K Harrell	06/20/2008	restored csTtlTrnsfrPnltyExmptCd, 
 * 							 get/set methods
 * 							defect 9724 Ver Defect POS A
 * K Harrell	02/26/2009	add ciETtlAllowdIndi, get/set methods
 * 							add isETtlAllowd() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	04/06/2009	add ciDefaultETtlCd, get/set methods  
 * 							defect 9969  Ver Defect_POS_E	
 * K Harrell	02/01/2012	add csDocTypeGrpCd, get/set methods  
 * 							defect 11228 Ver 6.10.0				  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * DocumentTypesData
 *
 * @version	6.10.0			02/01/2012  
 * @author	Kathy Harrell 
 * <br>Creation Date:		08/30/2001
 */

public class DocumentTypesData implements Serializable
{
	// int
	protected int ciDocTypeCd;

	// defect 9969
	protected int ciDefaultETtlCd;
	protected int ciETtlAllowdIndi;
	// end defect 9969 

	protected int ciRegRecIndi;

	// String 
	protected String csDocTypeCdDesc;

	// defect 9724 
	protected String csTtlTrnsfrPnltyExmptCd;
	// end defect 9724
	
	// defect 11228 
	protected String csDocTypeGrpCd; 
	// end defect 11228 

	private final static long serialVersionUID = -6330438765619270805L;

	/**
	 * Returns value of ciDefaultETtlCd 
	 * 
	 * @return int
	 */
	public int getDefaultETtlCd()
	{
		return ciDefaultETtlCd;
	}

	/**
	 * Returns the value of DocTypeCd
	 * 
	 * @return  int  
	 */
	public final int getDocTypeCd()
	{
		return ciDocTypeCd;
	}

	/**
	 * Returns the value of DocTypeCdDesc
	 * 
	 * @return  String 
	 */
	public final String getDocTypeCdDesc()
	{
		return csDocTypeCdDesc;
	}

	/**
	 * Returns value of csDocTypeGrpCd
	 * 
	 * @return String
	 */
	public String getDocTypeGrpCd()
	{
		return csDocTypeGrpCd;
	}
	
	/**
	 * Returns value of ETtlAllowdIndi
	 * 
	 * @return int 
	 */
	public int getETtlAllowdIndi()
	{
		return ciETtlAllowdIndi;
	}

	/**
	 * Returns the value of RegRecIndi
	 * 
	 * @return  int  
	 */
	public final int getRegRecIndi()
	{
		return ciRegRecIndi;
	}

	/**
	 * Return value of TtlTrnsfrPnltyExmptCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrPnltyExmptCd()
	{
		return csTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * 
	 * Is ETtlAllowd 
	 * 
	 * @return boolean
	 */
	public final boolean isETtlAllowd()
	{
		return ciETtlAllowdIndi == 1;
	}

	/**
	 * Set value of ciDefaultETtlCd 
	 * 
	 * @param aiDefaultETtlCd
	 */
	public void setDefaultETtlCd(int aiDefaultETtlCd)
	{
		ciDefaultETtlCd = aiDefaultETtlCd;
	}

	/**
	 * This method sets the value of DocTypeCd.
	 * 
	 * @param aiDocTypeCd   int  
	 */
	public final void setDocTypeCd(int aiDocTypeCd)
	{
		ciDocTypeCd = aiDocTypeCd;
	}

	/**
	 * This method sets the value of DocTypeCdDesc.
	 * 
	 * @param asDocTypeCdDesc   String 
	 */
	public final void setDocTypeCdDesc(String asDocTypeCdDesc)
	{
		csDocTypeCdDesc = asDocTypeCdDesc;
	}

	/**
	 * Set value of DocTypeGrpCd 
	 * 
	 * @param asDocTypeGrpCd 
	 */
	public void setDocTypeGrpCd(String asDocTypeGrpCd)
	{
		csDocTypeGrpCd = asDocTypeGrpCd;
	}
	
	/**
	 * Set value of ETtlAllowdIndi
	 * 
	 * @param aiETtlAllowdIndi
	 */
	public void setETtlAllowdIndi(int aiETtlAllowdIndi)
	{
		ciETtlAllowdIndi = aiETtlAllowdIndi;
	}

	/**
	 * This method sets the value of RegRecIndi.
	 * 
	 * @param aiRegRecIndi   int  
	 */
	public final void setRegRecIndi(int aiRegRecIndi)
	{
		ciRegRecIndi = aiRegRecIndi;
	}

	/**
	 * Set value of TtlTrnsfrPnltyExmptCd
	 * 
	 * @param asTtlTrnsfrPnltyExmptCd
	 */
	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)
	{
		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;
	}


}
