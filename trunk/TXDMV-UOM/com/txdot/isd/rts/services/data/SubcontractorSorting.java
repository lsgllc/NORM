package com.txdot.isd.rts.services.data;

/*
 *
 * SubcontractorSorting.java
 *
 * (c) Texas Department of Transportation 2002
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  Imported new class.
 *							Ver 5.2.0
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3			  
 * ---------------------------------------------------------------------
 */
/**
 * This class provides the compare method for Subcontractor
 *  
 * @version	5.2.3		05/19/2005 
 * @author	Nancy Ting
 * <br>Creation Date:	09/11/2002 
 */
public class SubcontractorSorting
	implements java.util.Comparator, java.io.Serializable
{
	/**
	 * SubcontractorSorting constructor comment.
	 */
	public SubcontractorSorting()
	{
		super();
	}
	/**
	 * Compares its two arguments for order.  Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.<p>
	 *
	 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
	 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>compare(x, y)</tt> must throw an exception if and only
	 * if <tt>compare(y, x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
	 * <tt>compare(x, z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
	 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
	 * <tt>z</tt>.<p>
	 *
	 * It is generally the case, but <i>not</i> strictly required that 
	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
	 * any comparator that violates this condition should clearly indicate
	 * this fact.  The recommended language is "Note: this comparator
	 * imposes orderings that are inconsistent with equals."
	 * 
	 * @param  aaObject1  Object 
	 * @param  aaObject2  Object 
	 * @return a negative integer, zero, or a positive integer as the
	 * 	       first argument is less than, equal to, or greater than the
	 *	       second. 
	 * @throws ClassCastException if the arguments' types prevent them from
	 * 	       being compared by this Comparator.
	 */
	public int compare(Object aaObject1, Object aaObject2)
	{
		Integer laInteger1 = (Integer) aaObject1;
		Integer laInteger2 = (Integer) aaObject2;
		return laInteger1.compareTo(laInteger2) * -1;
	}
}
