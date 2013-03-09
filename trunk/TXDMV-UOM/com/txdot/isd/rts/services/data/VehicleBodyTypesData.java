package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleBodyTypesData.java
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
 * VehicleBodyTypesData
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	08/31/2001 16:06:03  
 */

public class VehicleBodyTypesData implements Serializable, Comparable
{
	// String 
	protected String csVehBdyType;
	protected String csVehBdyTypeDesc;

	private final static long serialVersionUID = -1444824166426676693L;
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
		VehicleBodyTypesData laVehBdyTypesData =
			(VehicleBodyTypesData) aaObject;

		String lsFirst = getVehBdyTypeDesc();
		String lsSecond = laVehBdyTypesData.getVehBdyTypeDesc();
		return lsFirst.compareTo(lsSecond);

	}
	/**
	 * Returns the value of VehBdyType
	 * 
	 * @return  String 
	 */
	public final String getVehBdyType()
	{
		return csVehBdyType;
	}
	/**
	 * Returns the value of VehBdyTypeDesc
	 * 
	 * @return  String 
	 */
	public final String getVehBdyTypeDesc()
	{
		return csVehBdyTypeDesc;
	}
	/**
	 * This method sets the value of VehBdyType
	 * 
	 * @param asVehBdyType   String 
	 */
	public final void setVehBdyType(String asVehBdyType)
	{
		csVehBdyType = asVehBdyType;
	}
	/**
	 * This method sets the value of VehBdyTypeDesc
	 * 
	 * @param asVehBdyTypeDesc   String 
	 */
	public final void setVehBdyTypeDesc(String asVehBdyTypeDesc)
	{
		csVehBdyTypeDesc = asVehBdyTypeDesc;
	}
}
