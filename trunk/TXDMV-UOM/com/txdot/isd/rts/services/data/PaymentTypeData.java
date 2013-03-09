package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * PaymentTypeData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
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
 * This Data class contains attributes and get/set methods for 
 * PaymentTypeData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Kathy Harrell 
 * <br>Creation Date: 	08/30/2001 
 */

public class PaymentTypeData implements Serializable, Comparable
{
	// int
	protected int ciCntyUseIndi;
	protected int ciCustUseIndi;
	protected int ciPymntTypeCd;

	// String 
	protected String csPymntTypeCdDesc;

	private final static long serialVersionUID = -6461104282244652703L;
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
	 * @param   aaObject Object 
	 * @return  boolean  a negative integer, zero, or a positive integer
	 * 			as this objectis less than, equal to, or greater than 
	 * 			the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		PaymentTypeData laPaymentTypeData = (PaymentTypeData) aaObject;
		
		if (laPaymentTypeData.getPymntTypeCd() == getPymntTypeCd())
		{
			return 0;
		}
		else if (getPymntTypeCd() > laPaymentTypeData.getPymntTypeCd())
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
	/**
	 * Returns the value of CntyUseIndi
	 * 
	 * @return  int 
	 */
	public final int getCntyUseIndi()
	{
		return ciCntyUseIndi;
	}
	/**
	 * Returns the value of CustUseIndi
	 * 
	 * @return  int 
	 */
	public final int getCustUseIndi()
	{
		return ciCustUseIndi;
	}
	/**
	 * Returns the value of PymntTypeCd
	 * 
	 * @return  int  
	 */
	public final int getPymntTypeCd()
	{
		return ciPymntTypeCd;
	}
	/**
	 * Returns the value of PymntTypeCdDesc
	 * 
	 * @return  String 
	 */
	public final String getPymntTypeCdDesc()
	{
		return csPymntTypeCdDesc;
	}
	/**
	 * This method sets the value of CntyUseIndi
	 * 
	 * @param aiCntyUseIndi   int 
	 */
	public final void setCntyUseIndi(int aiCntyUseIndi)
	{
		ciCntyUseIndi = aiCntyUseIndi;
	}
	/**
	 * This method sets the value of CustUseIndi
	 * 
	 * @param aiCustUseIndi   int 
	 */
	public final void setCustUseIndi(int aiCustUseIndi)
	{
		ciCustUseIndi = aiCustUseIndi;
	}
	/**
	 * This method sets the value of PymntTypeCd
	 * 
	 * @param aiPymntTypeCd   int  
	 */
	public final void setPymntTypeCd(int aiPymntTypeCd)
	{
		ciPymntTypeCd = aiPymntTypeCd;
	}
	/**
	 * This method sets the value of PymntTypeCdDesc
	 * 
	 * @param asPymntTypeCdDesc   String 
	 */
	public final void setPymntTypeCdDesc(String asPymntTypeCdDesc)
	{
		csPymntTypeCdDesc = asPymntTypeCdDesc;
	}
}
