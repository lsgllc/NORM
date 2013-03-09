package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * DisabledPlacardCustomerIdTypeData.java
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
 * DisabledPlacardCustomerIdTypeData.
 *
 * @version	POS_Defect_B	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */
public class DisabledPlacardCustomerIdTypeData
	implements Serializable, Comparable
{

	private int ciCustIdTypeDsplyOrdr;
	private int ciCustIdTypeCd;
	private int ciInstIndi;
	private String csCustIdTypeDesc;

	static final long serialVersionUID = 1731856971726775823L;

	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.(This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> 
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that 
	 * <tt>x.compareTo(y)==0</tt> implies that <tt>sgn(x.compareTo(z)) 
	 * == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally 
	 * speaking, any class that implements the <tt>Comparable</tt> 
	 * interface and violates this condition should clearly indicate 
	 * this fact.  The recommended language is "Note: this class has a 
	 * natural ordering that is inconsistent with equals."
	 * 
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this 
	 *		object is less than, equal to, or greater than the specified
	 *		object.
	 * 
	 * @throws ClassCastException if the specified object's type 
	 * 			prevents it from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		return getCustIdTypeDsplyOrdr()
			- ((DisabledPlacardCustomerIdTypeData) aaObject)
				.getCustIdTypeDsplyOrdr();
	}

	/**
	 * Get value of ciCustIdTypeCd
	 * 
	 * @return int
	 */
	public int getCustIdTypeCd()
	{
		return ciCustIdTypeCd;
	}

	/**
	 * Get value of csCustIdTypeDesc
	 * 
	 * @return String 
	 */
	public String getCustIdTypeDesc()
	{
		return csCustIdTypeDesc;
	}

	/**
	 * Get value of ciCustIdTypeDsplyOrdr
	 * 
	 * @return int 
	 */
	public int getCustIdTypeDsplyOrdr()
	{
		return ciCustIdTypeDsplyOrdr;
	}

	/**
	 * Get value of ciInstIndi 
	 * 
	 * @return int
	 */
	public int getInstIndi()
	{
		return ciInstIndi;
	}

	/**
	 * Is Institution
	 *   
	 * return boolean 
	 */
	public boolean isInst()
	{
		return ciInstIndi == 1;
	}

	/**
	 * Set value of ciCustIdTypeCd
	 * 
	 * @param aiCustIdTypeCd
	 */
	public void setCustIdTypeCd(int aiCustIdTypeCd)
	{
		ciCustIdTypeCd = aiCustIdTypeCd;
	}

	/**
	 * Set value of csCustIdTypeDesc
	 * 
	 * @param asCustIdTypeDesc
	 */
	public void setCustIdTypeDesc(String asCustIdTypeDesc)
	{
		csCustIdTypeDesc = asCustIdTypeDesc;
	}

	/**
	 * Set value of ciCustIdTypeDsplyOrdr
	 * 
	 * @param aiCustIdTypeDsplyOrdr
	 */
	public void setCustIdTypeDsplyOrdr(int aiCustIdTypeDsplyOrdr)
	{
		ciCustIdTypeDsplyOrdr = aiCustIdTypeDsplyOrdr;
	}

	/**
	 * Set value of ciInstIndi 
	 * 
	 * @param aiInstIndi
	 */
	public void setInstIndi(int aiInstIndi)
	{
		ciInstIndi = aiInstIndi;
	}
}
