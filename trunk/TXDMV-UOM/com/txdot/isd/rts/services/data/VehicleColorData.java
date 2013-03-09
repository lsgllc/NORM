package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleColor.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/05/2011	Created 
 * 							defect 10712 Ver 6.7.0 
 * S Carlin		09/06/2011	Added constructor with cd, desc, and grp parameters 
 * 							defect 10985 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * Data Class for RTS_VEH_COLOR
 *
 * @version	6.8.1			09/06/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/05/2011 12:43:17
 */
public class VehicleColorData implements Serializable, Comparable
{
	private String csVehColorCd;
	private String csVehColorDesc;
	private String csVehColorGrpCd;

	static final long serialVersionUID = -5887198293573051626L;

	/**
	 * VehicleColorData.java Constructor
	 * 
	 */
	public VehicleColorData()
	{
		super();
	}

	/**
	 * @param csVehColorCd
	 * @param csVehColorDesc
	 */
	public VehicleColorData(String csVehColorCd, String csVehColorDesc, String csVehColorGrpCd)
	{
		super();
		this.csVehColorCd = csVehColorCd;
		this.csVehColorDesc = csVehColorDesc;
		this.csVehColorGrpCd = csVehColorGrpCd;
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
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		VehicleColorData laCompData = (VehicleColorData) aaObject;

		return (
			getVehColorDesc().compareTo(laCompData.getVehColorDesc()));
	}

	/**
	 * Get value of csVehColorCd
	 * 
	 * @return String
	 */
	public String getVehColorCd()
	{
		return csVehColorCd;
	}

	/**
	 * Get value of csVehColorDesc
	 * 
	 * @return String
	 */
	public String getVehColorDesc()
	{
		return csVehColorDesc;
	}

	/**
	 * Get value of csVehColorGrpCd
	 * 
	 * @return String
	 */
	public String getVehColorGrpCd()
	{
		return csVehColorGrpCd;
	}

	/**
	 * Set value of csVehColorCd
	 * 
	 * @param asVehColorCd
	 */
	public void setVehColorCd(String asVehColorCd)
	{
		csVehColorCd = asVehColorCd;
	}

	/**
	 * Set value of csVehColorDesc
	 * 
	 * @param asVehColorDesc
	 */
	public void setVehColorDesc(String asVehColorDesc)
	{
		csVehColorDesc = asVehColorDesc;
	}

	/**
	 * Set value of csVehColorGrpCd
	 * 
	 * @param asVehColorGrpCd
	 */
	public void setVehColorGrpCd(String asVehColorGrpCd)
	{
		csVehColorGrpCd = asVehColorGrpCd;
	}

}
