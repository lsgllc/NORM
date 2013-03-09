package com.txdot.isd.rts.client.accounting.ui;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * RefundTableData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	04/27/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * RefundTableData is a simple data object that contains the data used 
 * by the tables in ACC004, and ACC006.
 * 
 * @version	5.2.3		04/27/2005 
 * @author	Michael Abernethy 
 * <br>Creation Date:	08/29/2001 15:22:02 
 */
public class RefundTableData
	implements java.io.Serializable, Comparable
{
	// Object 
	private Dollar caAmount;
	
	// String 
	private String csRedeemType;
	private String csType;

	private final static long serialVersionUID = 5801850299302446508L;
	/**
	 * Creates a RefundTableData.
	 */
	public RefundTableData()
	{
		super();
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
	 * @param   aaObject  the Object to be compared.
	 * @return  int A negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		RefundTableData laData = (RefundTableData) aaObject;
		return getRedeemType().compareTo(laData.getRedeemType());
	}
	/**
	 * Returns the amount.
	 * 
	 * @return Dollar  
	 */
	public Dollar getAmount()
	{
		return caAmount;
	}
	/**
	 * Return the Redeem Type 
	 * 
	 * @return String
	 */
	public String getRedeemType()
	{
		return csRedeemType;
	}
	/**
	 * Returns the type.
	 * 
	 * @return String
	 */
	public String getType()
	{
		return csType;
	}
	/**
	 * Sets the amount.
	 * 
	 * @param aaAmount	Dollar 
	 */
	public void setAmount(Dollar aaAmount)
	{
		caAmount = aaAmount;
	}
	/**
	 * Sets the Redeem Type
	 * 
	 * @param asRedeemType	String
	 */
	public void setRedeemType(String asRedeemType)
	{
		csRedeemType = asRedeemType;
	}
	/**
	 * Sets the type.
	 * 
	 * @param asType	String
	 */
	public void setType(String asType)
	{
		csType = asType;
	}
}
