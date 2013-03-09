package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * MFInventoryAckData.java
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
 * This Data class contains attributes and get set methods for 
 * MFInventoryAckData 
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Charlie Walker
 * <br>Creation Date:	09/20/2001 13:49:39   
 */

public class MFInventoryAckData implements Serializable
{
	// int
	private int ciInvItmOrderDt;
	private int ciOfcIssuanceCd;
	private int ciOfcIssuanceNo;

	// String
	private String csDest;
	private String csInvcNo;
	private String csRcveInto;
	private String csSpclSrvcEmpId;
	private String csVerificationOn;

	private final static long serialVersionUID = -4904426027940010577L;
	/**
	 * MFInventoryAckData constructor comment.
	 */
	public MFInventoryAckData()
	{
		super();
	}
	/**
	 * Return value of Dest
	 * 
	 * @return String
	 */
	public String getDest()
	{
		return csDest;
	}
	/**
	 * Return value of InvcNo
	 * 
	 * @return String
	 */
	public String getInvcNo()
	{
		return csInvcNo;
	}
	/**
	 * Return value of InvItmOrderDt
	 * 
	 * @return int
	 */
	public int getInvItmOrderDt()
	{
		return ciInvItmOrderDt;
	}
	/**
	 * Return value of OfcIssuanceCd
	 * 
	 * @return int
	 */
	public int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}
	/**
	 * Return value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return value of RcveInto
	 * 
	 * @return String
	 */
	public String getRcveInto()
	{
		return csRcveInto;
	}
	/**
	 * Return value of SpclSrvcEmpId
	 * 
	 * @return String
	 */
	public String getSpclSrvcEmpId()
	{
		return csSpclSrvcEmpId;
	}
	/**
	 * Return value of VerificationOn
	 * 
	 * @return String
	 */
	public String getVerificationOn()
	{
		return csVerificationOn;
	}
	/**
	 * Set value of Dest
	 * 
	 * @param asDest String
	 */
	public void setDest(String asDest)
	{
		csDest = asDest;
	}
	/**
	 * Set value of InvcNo
	 * 
	 * @param asInvcNo String
	 */
	public void setInvcNo(String asInvcNo)
	{
		csInvcNo = asInvcNo;
	}
	/**
	 * Set value of InvItmOrderDt
	 * 
	 * @param aiInvItmOrderDt int
	 */
	public void setInvItmOrderDt(int aiInvItmOrderDt)
	{
		ciInvItmOrderDt = aiInvItmOrderDt;
	}
	/**
	 * Set value of OfcIssuanceCd
	 * 
	 * @param aiOfcIssuanceCd int
	 */
	public void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}
	/**
	 * Set value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set value of RcveInto
	 * 
	 * @param asRcveInto String
	 */
	public void setRcveInto(String asRcveInto)
	{
		csRcveInto = asRcveInto;
	}
	/**
	 * Set value of SpclSrvcEmpId
	 * 
	 * @param asSpclSrvcEmpId String
	 */
	public void setSpclSrvcEmpId(String asSpclSrvcEmpId)
	{
		csSpclSrvcEmpId = asSpclSrvcEmpId;
	}
	/**
	 * Set value of VerificationOn
	 * 
	 * @param asVerificationOn String
	 */
	public void setVerificationOn(String asVerificationOn)
	{
		csVerificationOn = asVerificationOn;
	}
}
