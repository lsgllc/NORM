package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleMakesData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							Moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleMakesData
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	08/30/2001  16:21:34
 */

public class VehicleMakesData implements Serializable, Comparable
{
	// String 
	protected String csVehMk;
	protected String csVehMkDesc;

	private final static long serialVersionUID = 6232997272414410107L;
	
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
	 * @return  int a negative integer, zero, or a positive integer as 
	 * 			this object is less than, equal to, or greater than the
	 * 			specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		String lsStrOrigDes = getVehMkDesc();
		String lsStrCompartToDes =
			((VehicleMakesData) aaObject).getVehMkDesc();

		return lsStrOrigDes.compareTo(lsStrCompartToDes);
	}
	/**
	 * Returns the value of VehMk
	 * @return  String 
	 */
	public final String getVehMk()
	{
		return csVehMk;
	}
	/**
	 * Returns the value of VehMkDesc
	 * @return  String 
	 */
	public final String getVehMkDesc()
	{
		return csVehMkDesc;
	}
	/**
	 * This method sets the value of VehMk.
	 * @param asVehMk   String 
	 */
	public final void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}
	/**
	 * This method sets the value of VehMkDesc.
	 * @param asVehMkDesc   String 
	 */
	public final void setVehMkDesc(String asVehMkDesc)
	{
		csVehMkDesc = asVehMkDesc;
	}
}
