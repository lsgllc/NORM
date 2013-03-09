package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * SalesTaxCategoryData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 	
 * K Harrell	07/26/2009	add isExmptReasonsReqd(), 
 * 							 isSalesTaxPriceReqd(), isTradeInReqd()
 * 							modify compareTo()
 * 							defect 10134 Defect_POS_F
 * K Harrell	10/31/2011	add csTtlTrnsfrPnltyExmptCd, 
 * 							 get/set method 
 * 							defect 11048 Ver 6.9.0 	 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * SalesTaxCategoryData
 * 
 * @version	6.9.0 	10/31/2011
 * @author	Administrator
 * @since 	 		08/30/2001  
 */

public class SalesTaxCategoryData implements Serializable, Comparable
{
	// int
	protected int ciExmptReasonsReqd;
	protected int ciSalesTaxBegDate;
	protected int ciSalesTaxEndDate;
	protected int ciSalesTaxPriceReqd;
	protected int ciTradeInReqd;

	// Object 
	protected Dollar caSalesTaxFee;
	protected Dollar caSalesTaxPrcnt;

	// String 
	protected String csSalesTaxCat;
	
	// defect 11048 
	protected String csTtlTrnsfrPnltyExmptCd; 
	// end defect 11048 

	private final static long serialVersionUID = -1041200764586599266L;

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
	 *  this object is less than, equal to, or greater than the specified
	 *  object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		String lsStrCompareToCat =
			((SalesTaxCategoryData) aaObject).getSalesTaxCat();

		// defect 10134	
		// multiplying by -1 will sort the data in descending order
		return -1 * (csSalesTaxCat.compareTo(lsStrCompareToCat));
		// end defect 10134
	}

	/**
	 * Returns the value of ExmptReasonsReqd
	 * @return  int  
	 */
	public final int getExmptReasonsReqd()
	{
		return ciExmptReasonsReqd;
	}

	/**
	 * Returns the value of SalesTaxBegDate
	 * @return  int  
	 */
	public final int getSalesTaxBegDate()
	{
		return ciSalesTaxBegDate;
	}

	/**
	 * Returns the value of SalesTaxCat
	 * @return  String 
	 */
	public final String getSalesTaxCat()
	{
		return csSalesTaxCat;
	}

	/**
	 * Returns the value of SalesTaxEndDate
	 * @return  int  
	 */
	public final int getSalesTaxEndDate()
	{
		return ciSalesTaxEndDate;
	}

	/**
	 * Returns the value of SalesTaxFee
	 * @return  Dollar  
	 */
	public final Dollar getSalesTaxFee()
	{
		return caSalesTaxFee;
	}

	/**
	 * Returns the value of SalesTaxPrcnt
	 * @return  Dollar  
	 */
	public final Dollar getSalesTaxPrcnt()
	{
		return caSalesTaxPrcnt;
	}

	/**
	 * Returns the value of SalesTaxPriceReqd
	 * @return  int  
	 */
	public final int getSalesTaxPriceReqd()
	{
		return ciSalesTaxPriceReqd;
	}

	/**
	 * Returns the value of TradeInReqd
	 * @return  int  
	 */
	public final int getTradeInReqd()
	{
		return ciTradeInReqd;
	}

	/**
	 * @return csTtlTrnsfrPnltyExmptCd
	 */
	public String getTtlTrnsfrPnltyExmptCd()
	{
		return csTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * This method sets the value of ExmptReasonsReqd.
	 * @param aiExmptReasonsReqd   int  
	 */
	public final void setExmptReasonsReqd(int aiExmptReasonsReqd)
	{
		ciExmptReasonsReqd = aiExmptReasonsReqd;
	}

	/**
	 * This method returns boolean based upon setting of  
	 *    ciExmptReasonsReqd
	 * 
	 * @return boolean 
	 */
	public boolean isExmptReasonsReqd()
	{
		return ciExmptReasonsReqd == 1;
	}

	/**
	 * This method returns boolean based upon setting of  
	 *    ciSalesTaxPriceReqd
	 * 
	 * @return boolean 
	 */
	public boolean isSalesTaxPriceReqd()
	{
		return ciSalesTaxPriceReqd == 1;
	}

	/**
	 * This method returns boolean based upon setting of  
	 *    ciTradeInReqd
	 * 
	 * @return boolean 
	 */
	public boolean isTradeInReqd()
	{
		return ciTradeInReqd == 1;
	}

	/**
	 * This method sets the value of SalesTaxBegDate.
	 * @param aiSalesTaxBegDate   int  
	 */
	public final void setSalesTaxBegDate(int aiSalesTaxBegDate)
	{
		ciSalesTaxBegDate = aiSalesTaxBegDate;
	}

	/**
	 * This method sets the value of SalesTaxCat.
	 * @param asSalesTaxCat   String 
	 */
	public final void setSalesTaxCat(String asSalesTaxCat)
	{
		csSalesTaxCat = asSalesTaxCat;
	}

	/**
	 * This method sets the value of SalesTaxEndDate.
	 * @param aiSalesTaxEndDate   int  
	 */
	public final void setSalesTaxEndDate(int aiSalesTaxEndDate)
	{
		ciSalesTaxEndDate = aiSalesTaxEndDate;
	}

	/**
	 * This method sets the value of SalesTaxFee.
	 * @param aSalesTaxFee   Dollar  
	 */
	public final void setSalesTaxFee(Dollar aaSalesTaxFee)
	{
		caSalesTaxFee = aaSalesTaxFee;
	}

	/**
	 * This method sets the value of SalesTaxPrcnt.
	 * @param aaSalesTaxPrcnt   Dollar  
	 */
	public final void setSalesTaxPrcnt(Dollar aaSalesTaxPrcnt)
	{
		caSalesTaxPrcnt = aaSalesTaxPrcnt;
	}

	/**
	 * This method sets the value of SalesTaxPriceReqd.
	 * @param aiSalesTaxPriceReqd   int  
	 */
	public final void setSalesTaxPriceReqd(int aiSalesTaxPriceReqd)
	{
		ciSalesTaxPriceReqd = aiSalesTaxPriceReqd;
	}

	/**
	 * This method sets the value of TradeInReqd.
	 * @param aiTradeInReqd   int  
	 */
	public final void setTradeInReqd(int aiTradeInReqd)
	{
		ciTradeInReqd = aiTradeInReqd;
	}

	/**
	 * @param asTtlTrnsfrPnltyExmptCd
	 */
	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)
	{
		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;
	}
}
