package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * SubcontractorData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/23/2004	Make SubcontractorData comparable
 *							add implements Comparable
 *							add compareTo()
 *							Ver 5.2.2 defect 7830
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * K Harrell	02/18/2010	Implement AdminNameAddressData; Add 
 * 								constructor.
 * 							add SubcontractorData() 
 * 							modify compareTo() 
 * 							defect 10161 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * SubcontractorData
 *
 * @version	POS_640		02/18/2010
 * @author	Kathy Harrell 
 * <br>Creation Date: 	09/05/2001
 * 
 */

public class SubcontractorData
	extends AdminNameAddressData
	implements Serializable, Comparable
{

	private final static long serialVersionUID = -5796096259931466156L;

	/**
	 * SubcontractorData constructor comment.
	 */
	public SubcontractorData()
	{
		super();
		getAddressData().setState(CommonConstant.STR_TX);
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
	 * @param   aaObject Object to be compared.
	 * @return  int  a negative integer, zero, or a positive integer as 
	 * 			this object is less than, equal to, or greater than the 
	 * 			specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		// defect 10161 
		return getId() - ((SubcontractorData) aaObject).getId();
		// end defect 10161 
	}
}
