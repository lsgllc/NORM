package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * InProcessTransactionData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/17/2010	Created
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Data Object for collection of In Process Transaction Info
 *
 * @version	6.6.0			09/17/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2010 16:21:17
 */
public class InProcessTransactionData implements Serializable, Comparable
{
	// boolean 
	private boolean cbPndngIRenew;

	// int 
	private int ciOfcIssuanceNo;
	private int ciTransAMDate;
	private int ciTransPostedMfIndi;
	private int ciTransTime;
	private int ciTransWsId;

	// String 
	private String csDocNo;
	private String csRegPltNo;
	private String csTransCd;
	private String csTransEmpId;
	private String csTransId;
	private String csVIN;

	static final long serialVersionUID = -9192372546145468841L;

	/**
	 * InProcessTransactionData Constructor
	 * 
	 */
	public InProcessTransactionData()
	{
		super();
	}

	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.(This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> 
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that 
	 * <tt>x.compareTo(y)==0</tt> implies that <tt>sgn(x.compareTo(z)) 
	 * == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally 
	 * speaking, any class that implements the <tt>Comparable</tt> 
	 * interface and violates this condition should clearly indicate 
	 * this fact.  The recommended language is "Note: this class has a 
	 * natural ordering that is inconsistent with equals."
	 * 
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this 
	 *		object is less than, equal to, or greater than the specified
	 *		object.
	 * 
	 * @throws ClassCastException if the specified object's type 
	 * 			prevents it from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		InProcessTransactionData laCompTo =
			(InProcessTransactionData) aaObject;

		String lsCompare =
			ciTransAMDate
				+ UtilityMethods.addPadding("" + ciTransTime, 6, "0");

		String lsCompareTo =
			laCompTo.getTransAMDate()
				+ UtilityMethods.addPadding(
					"" + laCompTo.getTransTime(),
					6,
					"0");
		return lsCompare.compareTo(lsCompareTo);
	}

	/**
	 * Get value of csDocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get value of csRegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Get value of ciTransAMDate
	 * 
	 * @return 
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Get value of csTransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Get value of csTransEmpId
	 * 
	 * @return 
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}

	/**
	 * Get value of csTransId
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
	}

	/**
	 * Get value of ciTransPostedMfIndi
	 * 
	 * @return String
	 */
	public int getTransPostedMfIndi()
	{
		return ciTransPostedMfIndi;
	}

	/**
	 * Get value of ciTransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Get value of ciTransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Get value of csVIN
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Initialize TransId
	 *
	 */
	public void initTransId()
	{
		csTransId =
			UtilityMethods.getTransId(
				ciOfcIssuanceNo,
				ciTransWsId,
				ciTransAMDate,
				ciTransTime);
	}

	/**
	 * Is Pending IRENEW Transaction 
	 * 
	 * @return boolean 
	 */
	public boolean isPndngIRenew()
	{
		return cbPndngIRenew;
	}

	/**
	 * Set value of csDocNo
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of cbPndngIRenew
	 * 
	 * @param abPndngIRenew
	 */
	public void setPndngIRenew(boolean abPndngIRenew)
	{
		cbPndngIRenew = abPndngIRenew;
	}

	/**
	 * Set value of csRegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Set value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set value of csTransEmpId
	 * 
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Set value of csTransId
	 * 
	 * @param asTransId
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Set value of ciTransPostedMfIndi
	 * 
	 * @param aiTransPostedMfIndi
	 */
	public void setTransPostedMfIndi(int aiTransPostedMfIndi)
	{
		ciTransPostedMfIndi = aiTransPostedMfIndi;
	}

	/**
	 * Set value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Set value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set value of csVIN
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

}
