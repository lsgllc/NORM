package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * DisabledPlacardDeleteReasonData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * DisabledPlacardDeleteReasonData.
 *
 * @version	POS_Defect_B	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */
public class DisabledPlacardDeleteReasonData
	implements Serializable, Comparable
{

	private int ciDelReasnCd;
	private int ciDelUseIndi;
	private int ciReplUseIndi;
	private String csDelReasnDesc;

	static final long serialVersionUID = 8807232120462758587L;

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
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		return ciDelReasnCd
			- ((DisabledPlacardDeleteReasonData) aaObject)
				.getDelReasnCd();
	}

	/**
	 * Get value of ciDelReasnCd
	 * 
	 * @return int
	 */
	public int getDelReasnCd()
	{
		return ciDelReasnCd;
	}

	/**
	 * Get value of csDelReasnDesc
	 * 
	 * @return String
	 */
	public String getDelReasnDesc()
	{
		return csDelReasnDesc;
	}

	/**
	 * Get value of ciDelUseIndi
	 * 
	 * @return int
	 */
	public int getDelUseIndi()
	{
		return ciDelUseIndi;
	}

	/**
	 * Get value of ciReplUseIndi
	 * 
	 * @return int
	 */
	public int getReplUseIndi()
	{
		return ciReplUseIndi;
	}

	/**
	 * Set value of ciDelReasnCd
	 * 
	 * @param aiDelReasnCd
	 */
	public void setDelReasnCd(int aiDelReasnCd)
	{
		ciDelReasnCd = aiDelReasnCd;
	}

	/**
	 * Set value of csDelReasnDesc
	 * 
	 * @param asDelReasnDesc
	 */
	public void setDelReasnDesc(String asDelReasnDesc)
	{
		csDelReasnDesc = asDelReasnDesc;
	}

	/**
	 * Set value of ciDelUseIndi
	 * 
	 * @param aiDelUseIndi
	 */
	public void setDelUseIndi(int aiDelUseIndi)
	{
		ciDelUseIndi = aiDelUseIndi;
	}

	/**
	 * Set value of ciReplUseIndi
	 * 
	 * @param aiReplUseIndi
	 */
	public void setReplUseIndi(int aiReplUseIndi)
	{
		ciReplUseIndi = aiReplUseIndi;
	}
}
