package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * FundsUpdateData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * FundsUpdateData is used by ACC020 to place the Funds Remittance 
 * information before sending it to the MF for an update
 * 
 * @version	5.2.3		05/19/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	09/28/2001 08:07:39  
 */

public class FundsUpdateData implements java.io.Serializable
{

	// RTSDate 
	private RTSDate caFundsPaymentDate;

	// int
	private int ciAccountNoCd;
	private int ciComptCountyNo;
	private int ciOfcIssuanceNo;
	private int ciPaymentTypeCd;
	private int ciTraceNo;

	// String
	private String csCheckNo;
	private String csPaymentStatusCd;
	private String csTransEmpId;

	// Vector
	private Vector cvFundsDue;

	private final static long serialVersionUID = -8982457322859404292L;
	/**
	 * Creates a FundsUpdateData.
	 */
	public FundsUpdateData()
	{
		super();
		cvFundsDue = new Vector();
	}
	/**
	 * Returns the Account Number Code.
	 * 
	 * @return int
	 */
	public int getAccountNoCd()
	{
		return ciAccountNoCd;
	}
	/**
	 * Returns the Check Number.
	 * 
	 * @return String 
	 */
	public String getCheckNo()
	{
		return csCheckNo;
	}
	/**
	 * Returns the Compt County Number.
	 * 
	 * @return int 
	 */
	public int getComptCountyNo()
	{
		return ciComptCountyNo;
	}
	/**
	 * Returns the Funds Payment Date.
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getFundsPaymentDate()
	{
		return caFundsPaymentDate;
	}
	/**
	 * Returns the Office Issuance Number.
	 * 
	 * @return int 
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the Payment Status Code.
	 * 
	 * @return String 
	 */
	public String getPaymentStatusCd()
	{
		return csPaymentStatusCd;
	}
	/**
	 * Returns the Payment Type Code.
	 * 
	 * @return int 
	 */
	public int getPaymentTypeCd()
	{
		return ciPaymentTypeCd;
	}
	/**
	 * Returns the Trace Number.
	 * 
	 * @return int 
	 */
	public int getTraceNo()
	{
		return ciTraceNo;
	}
	/**
	 * Returns the Trans Employee Id.
	 * 
	 * @return String 
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Returns the vector of FundsDueData.
	 * 
	 * @return Vector 
	 */
	public Vector getFundsDue()
	{
		return cvFundsDue;
	}
	/**
	 * Sets the Account Number Code.
	 * 
	 * @param  aiAccountNoCd int  
	 */
	public void setAccountNoCd(int aiAccountNoCd)
	{
		ciAccountNoCd = aiAccountNoCd;
	}
	/**
	 * Sets the Check Number.
	 * 
	 * @param  asCheckNo String 
	 */
	public void setCheckNo(String asCheckNo)
	{
		csCheckNo = asCheckNo;
	}
	/**
	 * Sets the Compt County Number.
	 * 
	 * @param  aiComptCountyNo int
	 */
	public void setComptCountyNo(int aiComptCountyNo)
	{
		ciComptCountyNo = aiComptCountyNo;
	}
	/**
	 * Sets the Funds Payment Date.
	 * 
	 * @param aaFundsPaymentDate RTSDate 
	 */
	public void setFundsPaymentDate(RTSDate aaFundsPaymentDate)
	{
		caFundsPaymentDate = aaFundsPaymentDate;
	}
	/**
	 * Sets the Office Issuance Number.
	 * 
	 * @param  aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Sets the Payment Status Code.
	 * 
	 * @param  asPaymentStatusCd String
	 */
	public void setPaymentStatusCd(String asPaymentStatusCd)
	{
		csPaymentStatusCd = asPaymentStatusCd;
	}
	/**
	 * Sets the Payment Type Code.
	 * 
	 * @param  aiPaymentTypeCd  int
	 */
	public void setPaymentTypeCd(int aiPaymentTypeCd)
	{
		ciPaymentTypeCd = aiPaymentTypeCd;
	}
	/**
	 * Sets the Trace Number.
	 * 
	 * @param  aiTraceNo  int
	 */
	public void setTraceNo(int aiTraceNo)
	{
		ciTraceNo = aiTraceNo;
	}
	/**
	 * Sets the Trans Employee Id.
	 * 
	 * @param  asTransEmpId  String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * Sets the vector full of FundsDueData.
	 * 
	 * @param   avFundsDue  Vector
	 */
	public void setFundsDue(Vector avFundsDue)
	{
		cvFundsDue = avFundsDue;
	}
}
