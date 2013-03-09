package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * InternetDepositReconData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/12/2009	Created
 * 							defect 9935 Ver Defect_POS_D 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * InternetDepositReconData
 *
 * @version	Defect_POS_D	02/12/2009 
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/12/2009
 */

public class InternetDepositReconData
	implements Serializable, Comparable
{
	// int 
	private int ciOfcIssuanceNo;

	// String
	private String csItrntTraceNo;
	private String csPymntCardType;
	private String csLast4PymntCardNo;
	private Dollar caPymntAmt;

	// Object 
	private RTSDate caBankDepositDate;
	private RTSDate caTOLTransDate;

	static final long serialVersionUID = -3962203130084026972L;

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
	 */
	public int compareTo(Object aaObject)
	{
		InternetDepositReconData laData =
			(InternetDepositReconData) aaObject;
		String lsCurrent =
			getBankDepositDate().toString()
				+ UtilityMethods.addPadding(
					"" + getOfcIssuanceNo(),
					3,
					"0")
				+ getPymntCardType()
				+ getItrntTraceNo();
		String lsCompare =
			laData.getBankDepositDate().toString()
				+ UtilityMethods.addPadding(
					"" + laData.getOfcIssuanceNo(),
					3,
					"0")
				+ laData.getPymntCardType()
				+ laData.getItrntTraceNo();
		return lsCurrent.compareTo(lsCompare);
	}

	/**
	 * Gets value of caBankDepositDate
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getBankDepositDate()
	{
		return caBankDepositDate;
	}

	/**
	 * Gets value of caPymntAmt
	 * 
	 * @return Dollar 
	 */
	public Dollar getPymntAmt()
	{
		return caPymntAmt;
	}

	/**
	 * Gets value of caTOLTransDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getTOLTransDate()
	{
		return caTOLTransDate;
	}

	/**
	 * Gets value of 
	 * 
	 * @return
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Gets value of csItrntTraceNo
	 * 
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Gets value of csLast4PymntCardNo
	 * 
	 * @return String
	 */
	public String getLast4PymntCardNo()
	{
		return csLast4PymntCardNo;
	}

	/**
	 * Gets value of csPymntCardType
	 * 
	 * @return String
	 */
	public String getPymntCardType()
	{
		return csPymntCardType;
	}

	/**
	 * Sets value of caBankDepositDate
	 * 
	 * @param aaBankDepositDate
	 */
	public void setBankDepositDate(RTSDate aaBankDepositDate)
	{
		caBankDepositDate = aaBankDepositDate;
	}

	/**
	 * Sets value of caPymntAmt
	 * 
	 * @param aaPymntAmt
	 */
	public void setPymntAmt(Dollar aaPymntAmt)
	{
		caPymntAmt = aaPymntAmt;
	}

	/**
	 * Sets value of caTOLTransDate
	 * 
	 * @param aaTOLTransDate
	 */
	public void setTOLTransDate(RTSDate aaTOLTransDate)
	{
		caTOLTransDate = aaTOLTransDate;
	}

	/**
	 * Sets value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Sets value of csItrntTraceNo
	 * 
	 * @param asItrntTraceNo
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Sets value of csLast4PymntCardNo
	 * 
	 * @param asLast4PymntCardNo
	 */
	public void setLast4PymntCardNo(String asLast4PymntCardNo)
	{
		csLast4PymntCardNo = asLast4PymntCardNo;
	}

	/**
	 * Sets value of csPymntCardType
	 * 
	 * @param asPymntCardType
	 */
	public void setPymntCardType(String asPymntCardType)
	{
		csPymntCardType = asPymntCardType;
	}

}
