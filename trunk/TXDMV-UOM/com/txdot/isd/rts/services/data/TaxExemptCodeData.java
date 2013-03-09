package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * TaxExemptCodeData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/26/2009	add isIMCNoReqd(), isSellerFinanced()
 * 							add SELLER_FINANCED_SALE
 * 							modify compareTo() 
 * 							defect 10134 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * TaxExemptCodeData
 *
 * @version	Defect_POS_F	07/26/2009 
 * @author	Administrator 
 * <br>Creation Date: 		08/30/2001  13:36:05
 */

public class TaxExemptCodeData implements Serializable, Comparable
{
	// int
	private int ciIMCNoReqd;
	private int ciSalesTaxExmptCd;

	// String 
	private String csSalesTaxExmptDesc;
	
	// Constant 
	// defect 10134 
	private final static int SELLER_FINANCED_SALE_CD = 16;
	// end defect 10134  

	private final static long serialVersionUID = -913467888091914824L;

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
	 * @return  int a negative integer, zero, or a positive integer as 
	 *          this object is less than, equal to, or greater than the
	 * 			specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		String lsStrCompareToDesc =
			((TaxExemptCodeData) aaObject).getSalesTaxExmptDesc();

		// defect 10134
		// Sort according to description 
		return csSalesTaxExmptDesc.compareTo(lsStrCompareToDesc);
		// end defect 10134
	}

	/**
	 * Returns the value of IMCNoReqd
	 * 
	 * @return  int  
	 */
	public final int getIMCNoReqd()
	{
		return ciIMCNoReqd;
	}

	/**
	 * Returns the value of SalesTaxExmptCd
	 * 
	 * @return  int  
	 */
	public final int getSalesTaxExmptCd()
	{
		return ciSalesTaxExmptCd;
	}

	/**
	 * Returns the value of SalesTaxExmptDesc
	 * 
	 * @return  String 
	 */
	public final String getSalesTaxExmptDesc()
	{
		return csSalesTaxExmptDesc;
	}

	/**
	 * This method returns boolean based upon setting of  
	 *    ciIMCNoReqd
	 * 
	 * @return boolean 
	 */
	public final boolean isIMCNoReqd()
	{
		return ciIMCNoReqd == 1;
	}

	/**
	 * This method returns boolean based upon setting of  
	 *    ciSalesTaxExmptCd
	 * 
	 * @return boolean 
	 */
	public final boolean isSellerFinanced()
	{
		return ciSalesTaxExmptCd == SELLER_FINANCED_SALE_CD;
	}

	/**
	 * This method sets the value of IMCNoReqd
	 * 
	 * @param aiIMCNoReqd   int  
	 */
	public final void setIMCNoReqd(int aiIMCNoReqd)
	{
		ciIMCNoReqd = aiIMCNoReqd;
	}

	/**
	 * This method sets the value of SalesTaxExmptCd
	 * 
	 * @param aiSalesTaxExmptCd   int  
	 */
	public final void setSalesTaxExmptCd(int aiSalesTaxExmptCd)
	{
		ciSalesTaxExmptCd = aiSalesTaxExmptCd;
	}

	/**
	 * This method sets the value of SalesTaxExmptDesc
	 * 
	 * @param asSalesTaxExmptDesc   String 
	 */
	public final void setSalesTaxExmptDesc(String asSalesTaxExmptDesc)
	{
		csSalesTaxExmptDesc = asSalesTaxExmptDesc;
	}
}
