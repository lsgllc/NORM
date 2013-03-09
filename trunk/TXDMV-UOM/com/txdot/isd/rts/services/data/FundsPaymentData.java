package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * FundsPaymentData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			09/07/2001  Added comments
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * FundsPaymentData contains all the information needed for a Funds 
 * Payment record. 
 * 
 * @version	5.2.3		05/19/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	07/18/2001 16:11:09  
 */

public class FundsPaymentData
	implements Comparable, java.io.Serializable
{
	// Object
	private Dollar caTotalPaymentAmount;
	private RTSDate caFundsPaymentDate;
	private RTSDate caFundsReceivedDate;
	private RTSDate caFundsReportDate;
	private RTSDate caReportingDate;

	// String
	private String csAccountNoCode;
	private String csCheckNo;
	private String csComptCountyNo;
	private String csFundsCategory;
	private String csOfcIssuanceNo;
	private String csPaymentStatusCode;
	private String csPaymentStatusDesc;
	private String csPaymentTypeCode;
	private String csTransEmpId;
	private String csTraceNo;

	private final static long serialVersionUID = -6948108329724908551L;
	/**
	 * Creates a FundsPaymentData.
	 */
	public FundsPaymentData()
	{
		super();
	}
	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		FundsPaymentData laFundsPymntData = (FundsPaymentData) aaObject;
		return Integer.valueOf(getTraceNo()).compareTo(
			Integer.valueOf(laFundsPymntData.getTraceNo()));
	}
	/**
	 * Returns the Account Number Code.
	 * 
	 * @return String
	 */
	public String getAccountNoCode()
	{
		return csAccountNoCode;
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
	 * @return String 
	 */
	public String getComptCountyNo()
	{
		return csComptCountyNo;
	}
	/**
	 * Returns the Funds Category.
	 * 
	 * @return String 
	 */
	public String getFundsCategory()
	{
		return csFundsCategory;
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
	 * Returns the Funds Received Date.
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getFundsReceivedDate()
	{
		return caFundsReceivedDate;
	}
	/**
	 * Returns the Funds Report Date.
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getFundsReportDate()
	{
		return caFundsReportDate;
	}
	/**
	 * Returns the Office Issuance Number.
	 * 
	 * @return String
	 */
	public String getOfcIssuanceNo()
	{
		return csOfcIssuanceNo;
	}
	/**
	 * Returns the Payment Status Code.
	 * 
	 * @return String 
	 */
	public String getPaymentStatusCode()
	{
		return csPaymentStatusCode;
	}
	/**
	 * Returns the Payment Status Desc.
	 * 
	 * @return String 
	 */
	public String getPaymentStatusDesc()
	{
		return csPaymentStatusDesc;
	}
	/**
	 * Returns the Payment Type Code.
	 * 
	 * @return String 
	 */
	public String getPaymentTypeCode()
	{
		return csPaymentTypeCode;
	}
	/**
	 * Returns the Reporting Date.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getReportingDate()
	{
		return caReportingDate;
	}
	/**
	 * Returns the Total Payment Amount.
	 * 
	 * @return Dollar
	 */
	public Dollar getTotalPaymentAmount()
	{
		return caTotalPaymentAmount;
	}
	/**
	 * Returns the Trace Number.
	 * 
	 * @return String
	 */
	public String getTraceNo()
	{
		return csTraceNo;
	}
	/**
	 * Returns the Trans Emp Id.
	 * 
	 * @return String 
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Sets the Account Number Code.
	 * 
	 * @param asAccountNoCode String
	 */
	public void setAccountNoCode(String asAccountNoCode)
	{
		csAccountNoCode = asAccountNoCode;
	}
	/**
	 * Sets the Check Number.
	 * 
	 * @param asCheckNo  String 
	 */
	public void setCheckNo(String asCheckNo)
	{
		csCheckNo = asCheckNo;
	}
	/**
	 * Sets the Compt County Number.
	 * 
	 * @param asComptCountyNo String
	 */
	public void setComptCountyNo(String asComptCountyNo)
	{
		csComptCountyNo = asComptCountyNo;
	}
	/**
	 * Sets the Funds Category.
	 * 
	 * @param asFundsCategory String 
	 */
	public void setFundsCategory(String asFundsCategory)
	{
		csFundsCategory = asFundsCategory;
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
	 * Sets the Funds Received Date.
	 * 
	 * @param aaFundsReceivedDate  RTSDate 
	 */
	public void setFundsReceivedDate(RTSDate aaFundsReceivedDate)
	{
		caFundsReceivedDate = aaFundsReceivedDate;
	}
	/**
	 * Sets the Funds Report Date.
	 * 
	 * @param aaFundsReportDate  RTSDate 
	 */
	public void setFundsReportDate(RTSDate aaFundsReportDate)
	{
		caFundsReportDate = aaFundsReportDate;
	}
	/**
	 * Sets the Office Issuance Number.
	 * 
	 * @param asOfcIssuanceNo String 
	 */
	public void setOfcIssuanceNo(String asOfcIssuanceNo)
	{
		csOfcIssuanceNo = asOfcIssuanceNo;
	}
	/**
	 * Sets the Payment Status Code.
	 * 
	 * @param asPaymentStatusCode  String 
	 */
	public void setPaymentStatusCode(String asPaymentStatusCode)
	{
		csPaymentStatusCode = asPaymentStatusCode;
	}
	/**
	 * Sets the Payment Status Desc.
	 * 
	 * @param asPaymentStatusDesc
	 */
	public void setPaymentStatusDesc(String asPaymentStatusDesc)
	{
		csPaymentStatusDesc = asPaymentStatusDesc;
	}
	/**
	 * Sets the Payment Type Code.
	 * 
	 * @param asPaymentTypeCode  String
	 */
	public void setPaymentTypeCode(String asPaymentTypeCode)
	{
		csPaymentTypeCode = asPaymentTypeCode;
	}
	/**
	 * Sets the Reporting Date.
	 * 
	 * @param aaReportingDate  RTSDate 
	 */
	public void setReportingDate(RTSDate aaReportingDate)
	{
		caReportingDate = aaReportingDate;
	}
	/**
	 * Sets the Total Payment Amount.
	 * 
	 * @param aaTotalPaymentAmount Dollar 
	 */
	public void setTotalPaymentAmount(Dollar aaTotalPaymentAmount)
	{
		caTotalPaymentAmount = aaTotalPaymentAmount;
	}
	/**
	 * Sets the Trace Number.
	 * 
	 * @param asTraceno  String
	 */
	public void setTraceNo(String asTraceNo)
	{
		while (asTraceNo.substring(0, 1).equals("0"))
		{
			asTraceNo = asTraceNo.substring(1, asTraceNo.length());
		}
		csTraceNo = asTraceNo;
	}
	/**
	 * Sets the Trans Employee Id.
	 * 
	 * @param asTransEmpId  String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
}
