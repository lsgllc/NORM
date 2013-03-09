package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * TitleTransferEntityData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/02/2008	Created
 * 							defect 9583 Ver Defect POS A 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * TitleTransferEntityData
 *
 * @version	Defect POS A 		04/02/2008
 * @author	K Harrell
 * <br>Creation Date:			04/02/2008 03:36:00
 */
public class TitleTransferEntityData implements Serializable
{

	private String csTtlTrnsfrEntCd;
	private String csTtlTrnsfrEntDesc;

	private static final long serialVersionUID = 1950990528608488205L;

	/**
	 * Return value of csTtlTrnsfrEntCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrEntCd()
	{
		return csTtlTrnsfrEntCd;
	}

	/**
	 * Return value of csTtlTrnsfrEntCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrEntDesc()
	{
		return csTtlTrnsfrEntDesc;
	}

	/**
	 * Set value of csTtlTrnsfrEntCd
	 * 
	 * @param asTtlTrnsfrEntCd
	 */
	public void setTtlTrnsfrEntCd(String asTtlTrnsfrEntCd)
	{
		csTtlTrnsfrEntCd = asTtlTrnsfrEntCd;
	}

	/**
	 * Set value of csTtlTrnsfrEntDesc
	 * 
	 * @param asTtlTrnsfrEntDesc
	 */
	public void setTtlTrnsfrEntDesc(String asTtlTrnsfrEntDesc)
	{
		csTtlTrnsfrEntDesc = asTtlTrnsfrEntDesc;
	}

}
