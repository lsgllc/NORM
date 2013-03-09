package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * PaymentAccountData.java 
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get and set methods for 
 * PaymentAccountData
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	09/04/2001    
 */

public class PaymentAccountData implements Serializable, Comparable
{
	// int 
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciDeleteIndi;

	// Object 
	protected RTSDate caChngTimestmp;

	//	String
	protected String csPymntLocDesc;
	protected String csPymntLocId;

	private final static long serialVersionUID = -9066243999354535933L;

	/**
	 * Method to sort the Payment Account Data based on Payment 
	 * Location ID
	 * 
	 * @param  aaObject
	 * @return int 
	 */
	public int compareTo(Object aaObject)
	{
		String lsOrigDes = getPymntLocId();
		String lsCompareToDes =
			((PaymentAccountData) aaObject).getPymntLocId();

		return lsOrigDes.compareTo(lsCompareToDes);
		//return 0;
	}
	/**
	 * Returns the value of ChngTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Returns the value of DeleteIndi
	 * 
	 * @return  int 
	 */
	public final int getDeleteIndi()
	{
		return ciDeleteIndi;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of PymntLocDesc
	 * 
	 * @return  String 
	 */
	public final String getPymntLocDesc()
	{
		return csPymntLocDesc;
	}
	/**
	 * Returns the value of PymntLocId
	 * 
	 * @return  String 
	 */
	public final String getPymntLocId()
	{
		return csPymntLocId;
	}
	/**
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * This method sets the value of ChngTimestmp.
	 * 
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * This method sets the value of DeleteIndi.
	 * 
	 * @param aiDeleteIndi   int 
	 */
	public final void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of PymntLocDesc.
	 * 
	 * @param asPymntLocDesc   String 
	 */
	public final void setPymntLocDesc(String asPymntLocDesc)
	{
		csPymntLocDesc = asPymntLocDesc;
	}
	/**
	 * This method sets the value of PymntLocId.
	 * 
	 * @param asPymntLocId   String 
	 */
	public final void setPymntLocId(String asPymntLocId)
	{
		csPymntLocId = asPymntLocId;
	}
	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
}
