package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * InventoryPatternsData.java  
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * K Harrell	02/06/2007	Added csVIItmCd, ciISAIndi 
 * 							get/set methods 
 * 							defect 9085 Ver Special Plates 							  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * InventoryPatternsData
 *
 * @version	Special Plates	02/06/2007 
 * @author	Kathy Harrell
 * <br>Creation Date:		08/30/2001
 */

public class InventoryPatternsData implements Serializable, Comparable
{
	// int
	protected int ciInvItmPatrnCd;
	protected int ciInvItmYr;
	protected int ciPatrnSeqCd;
	protected int ciISAIndi;

	// String 
	protected String csInvItmEndNo;
	protected String csInvItmNo;
	protected String csItmCd;
	protected String csValidInvLtrs;
	protected String csVIItmCd;

	private final static long serialVersionUID = 1941398223488889426L;

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
	 * @param   o the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		return getItmCd().compareTo(
			((InventoryPatternsData) aaObject).getItmCd());
	}
	/**
	 * Returns the value of InvItmEndNo
	 * 
	 * @return  String 
	 */
	public final String getInvItmEndNo()
	{
		return csInvItmEndNo;
	}
	/**
	 * Returns the value of InvItmNo
	 * 
	 * @return  String 
	 */
	public final String getInvItmNo()
	{
		return csInvItmNo;
	}
	/**
	 * Returns the value of InvItmPatrnCd
	 * 
	 * @return  int  
	 */
	public final int getInvItmPatrnCd()
	{
		return ciInvItmPatrnCd;
	}
	/**
	 * Returns the value of InvItmYr
	 * 
	 * @return  int  
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}
	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * Returns the value of PatrnSeqCd
	 * 
	 * @return  int 
	 */
	public final int getPatrnSeqCd()
	{
		return ciPatrnSeqCd;
	}
	/**
	 * Returns the value of ValidInvLtrs
	 * 
	 * @return  String 
	 */
	public final String getValidInvLtrs()
	{
		return csValidInvLtrs;
	}
	/**
	 * This method sets the value of InvItmEndNo.
	 * 
	 * @param asInvItmEndNo   String 
	 */
	public final void setInvItmEndNo(String asInvItmEndNo)
	{
		csInvItmEndNo = asInvItmEndNo;
	}
	/**
	 * This method sets the value of InvItmNo.
	 * 
	 * @param asInvItmNo   String 
	 */
	public final void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}
	/**
	 * This method sets the value of InvItmPatrnCd.
	 * 
	 * @param aiInvItmPatrnCd   int  
	 */
	public final void setInvItmPatrnCd(int aiInvItmPatrnCd)
	{
		ciInvItmPatrnCd = aiInvItmPatrnCd;
	}
	/**
	 * This method sets the value of InvItmYr.
	 * 
	 * @param aiInvItmYr   int  
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	/**
	 * This method sets the value of ItmCd.
	 * 
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * This method sets the value of PatrnSeqCd.
	 * 
	 * @param aiPatrnSeqCd   int 
	 */
	public final void setPatrnSeqCd(int aiPatrnSeqCd)
	{
		ciPatrnSeqCd = aiPatrnSeqCd;
	}
	/**
	 * This method sets the value of ValidInvLtrs.
	 * 
	 * @param asValidInvLtrs   String 
	 */
	public final void setValidInvLtrs(String asValidInvLtrs)
	{
		csValidInvLtrs = asValidInvLtrs;
	}
	/**
	 * Return AbstractValue of ciISAIndi
	 * 
	 * @return int
	 */
	public int getISAIndi()
	{
		return ciISAIndi;
	}

	/**
	 * Return AbstractValue of csVIItmCd
	 * 
	 * @return String
	 */
	public String getVIItmCd()
	{
		return csVIItmCd;
	}

	/**
	 * Set AbstractValue of ciISAIndi
	 * 
	 * @param aiISAIndi
	 */
	public void setISAIndi(int aiISAIndi)
	{
		ciISAIndi = aiISAIndi;
	}

	/**
	 * Set AbstractValue of csVIItmCd
	 * 
	 * @param asVIItmCd
	 */
	public void setVIItmCd(String asVIItmCd)
	{
		csVIItmCd = asVIItmCd;
	}

}
