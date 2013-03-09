package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * DeleteReasonsData.java
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
 * This Data class contains attributes and get and set methods for 
 * DeleteReasonsData
 *
 * @version	5.2.3			06/19/2005 
 * @author	Administrator
 * <br>Creation Date:		08/30/2001
 */

public class DeleteReasonsData implements Serializable, Comparable
{
	// int
	protected int ciDelInvReasnCd;

	// String 
	protected String csDelInvReasn;

	private final static long serialVersionUID = 551062411276391388L;
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

		DeleteReasonsData laDelReasnsData =
			(DeleteReasonsData) aaObject;

		Integer laDelInvReasonCd =
			new Integer(laDelReasnsData.getDelInvReasnCd());

		Integer laDelInvReasonCdthis =
			new Integer(this.getDelInvReasnCd());

		return laDelInvReasonCdthis.compareTo(laDelInvReasonCd);
	}
	/**
	 * Returns the value of DelInvReasn
	 * 
	 * @return  String 
	 */
	public final String getDelInvReasn()
	{
		return csDelInvReasn;
	}
	/**
	 * Returns the value of DelInvReasnCd
	 * 
	 * @return  int  
	 */
	public final int getDelInvReasnCd()
	{
		return ciDelInvReasnCd;
	}
	/**
	 * This method sets the value of DelInvReasn.
	 * 
	 * @param asDelInvReasn   String 
	 */
	public final void setDelInvReasn(String asDelInvReasn)
	{
		csDelInvReasn = asDelInvReasn;
	}
	/**
	 * This method sets the value of DelInvReasnCd.
	 * 
	 * @param aiDelInvReasnCd   int  
	 */
	public final void setDelInvReasnCd(int aiDelInvReasnCd)
	{
		ciDelInvReasnCd = aiDelInvReasnCd;
	}
}
