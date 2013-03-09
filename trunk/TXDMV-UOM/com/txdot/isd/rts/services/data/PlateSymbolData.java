package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * PlateSymbolData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/09/2010	Created 
 * 							defect 10366 Ver POS_640
 * K Harrell	03/25/2010	add serialVersionUID
 * 							defect 10366 Ver POS_640  
 * K Harrell	09/14/2010	add implements Comparable
 * 							add compareTo() 
 * 							defect 10571 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * PlateSymbolData
 *
 * @version	 6.6.0  	09/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	02/09/2010  16:27:00 
 */
public class PlateSymbolData implements Serializable
// defect 10571 
, Comparable
// end defect 10571 
{
	// int
	private int ciSymCharLngth;

	// String 
	private String csPltSymCd;
	private String csPltSymDesc;

	// long 
	static final long serialVersionUID = 8531052937287173036L;

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
		PlateSymbolData laCompData = (PlateSymbolData) aaObject;

		return (getPltSymCd().compareTo(laCompData.getPltSymCd()));
	}
	/**
	 * Gets value of csPltSymCd
	 * 
	 * @return String 
	 */
	public String getPltSymCd()
	{
		return csPltSymCd;
	}

	/**
	 * Gets value of csPltSymDesc
	 * 
	 * @return String
	 */
	public String getPltSymDesc()
	{
		return csPltSymDesc;
	}

	/**
	 * Gets value of ciSymCharLngth
	 * 
	 * @return int 
	 */
	public int getSymCharLngth()
	{
		return ciSymCharLngth;
	}

	/**
	 * Sets value of csPltSymCd
	 * 
	 * @param asPltSymCd
	 */
	public void setPltSymCd(String asPltSymCd)
	{
		csPltSymCd = asPltSymCd;
	}

	/**
	 * Sets value of csPltSymDesc
	 * 
	 * @param asPltSymDesc
	 */
	public void setPltSymDesc(String asPltSymDesc)
	{
		csPltSymDesc = asPltSymDesc;
	}

	/**
	 * Sets value of ciSymCharLngth
	 * 
	 * @param aiSymCharLngth
	 */
	public void setSymCharLngth(int aiSymCharLngth)
	{
		ciSymCharLngth = aiSymCharLngth;
	}
}
