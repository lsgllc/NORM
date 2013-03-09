package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * SubstationData.java
 *
 * (c) Texas Department of Transportation 2001
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
 * This Data class contains attributes and get set methods for 
 * SubstationData
 *
 * @version	5.2.3		06/19/2005
 * @author	Administrator 
 * <br>Creation Date: 	09/18/2001
 */

public class SubstationData implements Serializable
{
	// int
	protected int ciDeleteIndi;
	protected int ciOfcIssuanceNo = Integer.MIN_VALUE;
	protected int ciSubstaId = Integer.MIN_VALUE;

	// Object 
	protected RTSDate caChngTimestmp;

	// String
	protected String csSubstaCity;
	protected String csSubstaName;
	protected String csSubstaSt;
	protected String csSubstaZpCd;
	protected String csSubstaZpCdP4;

	private final static long serialVersionUID = -7714695138352856249L;
	/**
	 * Returns the value of ChngTimestmp
	 * @return  RTSDate 
	 */
	public final RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Returns the value of DeleteIndi
	 * @return  int 
	 */
	public final int getDeleteIndi()
	{
		return ciDeleteIndi;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of SubstaCity
	 * @return  String 
	 */
	public final String getSubstaCity()
	{
		return csSubstaCity;
	}
	/**
	 * Returns the value of SubstaId
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of SubstaName
	 * @return  String 
	 */
	public final String getSubstaName()
	{
		return csSubstaName;
	}
	/**
	 * Returns the value of SubstaSt
	 * @return  String 
	 */
	public final String getSubstaSt()
	{
		return csSubstaSt;
	}
	/**
	 * Returns the value of SubstaZpCd
	 * @return  String 
	 */
	public final String getSubstaZpCd()
	{
		return csSubstaZpCd;
	}
	/**
	 * Returns the value of SubstaZpCdP4
	 * @return  String 
	 */
	public final String getSubstaZpCdP4()
	{
		return csSubstaZpCdP4;
	}
	/**
	 * This method sets the value of ChngTimestmp.
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * This method sets the value of DeleteIndi.
	 * @param aiDeleteIndi   int 
	 */
	public final void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SubstaCity.
	 * @param asSubstaCity   String 
	 */
	public final void setSubstaCity(String asSubstaCity)
	{
		csSubstaCity = asSubstaCity;
	}
	/**
	 * This method sets the value of SubstaId.
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of SubstaName.
	 * @param asSubstaName   String 
	 */
	public final void setSubstaName(String asSubstaName)
	{
		csSubstaName = asSubstaName;
	}
	/**
	 * This method sets the value of SubstaSt.
	 * @param asSubstaSt   String 
	 */
	public final void setSubstaSt(String asSubstaSt)
	{
		csSubstaSt = asSubstaSt;
	}
	/**
	 * This method sets the value of SubstaZpCd.
	 * @param asSubstaZpCd   String 
	 */
	public final void setSubstaZpCd(String asSubstaZpCd)
	{
		csSubstaZpCd = asSubstaZpCd;
	}
	/**
	 * This method sets the value of SubstaZpCdP4.
	 * @param aSsubstaZpCdP4   String 
	 */
	public final void setSubstaZpCdP4(String asSubstaZpCdP4)
	{
		csSubstaZpCdP4 = asSubstaZpCdP4;
	}
}
