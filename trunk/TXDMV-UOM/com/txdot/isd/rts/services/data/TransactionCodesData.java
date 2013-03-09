package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * TransactionCodesData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/20/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
  * K Harrell	04/03/2008	add csTtlTrnsfrPnltyExmptCd, get/set methods
 * 							defect 9583 Ver 3 Defect POS A
 * K Harrell	05/21/2008	removed csTtlTrnsfrPnltyExmptCd, get/set 
 * 							methods; No longer required. 
 * 							defect 9583 Ver Defect POS A
 * K Harrell	06/20/2008	restored csTtlTrnsfrPnltyExmptCd, 
 * 							 get/set methods
 * 							defect 9724 Ver Defect POS A
 * K Harrell	02/27/2009	add ciETtlTransIndi, get/set methods 
 * 							add isETtlTrans(), compareTo()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	09/16/2010	add ciPndngTransLookupIndi, 
 * 							  ciElgblePndngTransLookupIndi, 
 * 							  is/get/set methods.
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * TransactionCodesData
 *
 * @version	6.6.0			09/16/2010 
 * @author	Kathy Harrell 
 * <br>Creation Date: 		08/31/2001 15:50:40  
 */
public class TransactionCodesData implements Serializable, Comparable
{

	// int
	private int ciCashDrawerIndi;
	private int ciCumulativeTransCd;
	private int ciETtlTransIndi;
	private int ciFeeSourceCd;
	private int ciInvTransCdType;
	private int ciRcptMsgCd;
	private int ciVoidableTransIndi;
	
	// defect 10598
	private int ciElgblePndngTransLookupIndi; 
	private int ciPndngTransLookupIndi;
	// end defect 10598
	
	// String 
	private String csTransCd;
	private String csTransCdDesc;
	private String csTtlTrnsfrPnltyExmptCd;
	
	private final static long serialVersionUID = -7749510870564215378L;

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
		TransactionCodesData laTransCdData =
			(TransactionCodesData) aaObject;
		return getTransCd().compareTo(laTransCdData.getTransCd());
	}

	/**
	 * Returns the value of CashDrawerIndi
	 * 
	 * @return  int 
	 */
	public final int getCashDrawerIndi()
	{
		return ciCashDrawerIndi;
	}

	/**
	 * Returns the value of CumulativeTransCd
	 * 
	 * @return  int  
	 */
	public final int getCumulativeTransCd()
	{
		return ciCumulativeTransCd;
	}

	/**
	 * Return value of ciElgblePndngTransLookupIndi
	 * 
	 * @return int
	 */
	public int getElgblePndngTransLookupIndi()
	{
		return ciElgblePndngTransLookupIndi;
	}

	/**
	 * Get value of ETtlTransIndi
	 * 
	 * @return int 
	 */
	public int getETtlTransIndi()
	{
		return ciETtlTransIndi;
	}

	/**
	 * Returns the value of FeeSourceCd
	 * 
	 * @return  int 
	 */
	public final int getFeeSourceCd()
	{
		return ciFeeSourceCd;
	}

	/**
	 * Returns the value of InvTransCdType
	 * 
	 * @return  int 
	 */
	public final int getInvTransCdType()
	{
		return ciInvTransCdType;
	}

	/**
	 * Return value of ciPndngTransLookupIndi
	 * 
	 * @return int 
	 */
	public int getPndngTransLookupIndi()
	{
		return ciPndngTransLookupIndi;
	}

	/**
	 * Returns the value of RcptMsgCd
	 * 
	 * @return  int 
	 */
	public final int getRcptMsgCd()
	{
		return ciRcptMsgCd;
	}

	/**
	 * Returns the value of TransCd
	 * 
	 * @return  String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Returns the value of TransCdDesc
	 * 
	 * @return  String 
	 */
	public final String getTransCdDesc()
	{
		return csTransCdDesc;
	}

	/**
	 * Return value of TtlTrnsfrPnltyExmptCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrPnltyExmptCd()
	{
		return csTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * Returns the value of VoidableTransIndi
	 * 
	 * @return  int 
	 */
	public final int getVoidableTransIndi()
	{
		return ciVoidableTransIndi;
	}

	/**
	 * Is ElgblePndngTransLookup
	 * 
	 * @return boolean 
	 */
	public boolean isElgblePndngTransLookup()
	{
		return ciElgblePndngTransLookupIndi == 1;
	}

	/**
	 * Is ETtlTrans
	 * 
	 * @return boolean 
	 */
	public boolean isETtlTrans()
	{
		return ciETtlTransIndi == 1;
	}

	/**
	 * Is PndngTransLookup
	 * 
	 * @return boolean 
	 */
	public boolean isPndngTransLookup()
	{
		return ciPndngTransLookupIndi == 1;
	}

	/**
	 * This method sets the value of CashDrawerIndi.
	 * 
	 * @param aiCashDrawerIndi   int 
	 */
	public final void setCashDrawerIndi(int aiCashDrawerIndi)
	{
		ciCashDrawerIndi = aiCashDrawerIndi;
	}

	/**
	 * This method sets the value of CumulativeTransCd.
	 * 
	 * @param aiCumulativeTransCd   int  
	 */
	public final void setCumulativeTransCd(int aiCumulativeTransCd)
	{
		ciCumulativeTransCd = aiCumulativeTransCd;
	}

	/**
	 * Set value of ciElgblePndngTransLookupIndi
	 * 
	 * @param aiElgblePndngTransLookupIndi
	 */
	public void setElgblePndngTransLookupIndi(int aiElgblePndngTransLookupIndi)
	{
		ciElgblePndngTransLookupIndi = aiElgblePndngTransLookupIndi;
	}

	/**
	 * Set value of EttlTransIndi
	 * 
	 * @param aiETtlTransIndi
	 */
	public void setETtlTransIndi(int aiETtlTransIndi)
	{
		ciETtlTransIndi = aiETtlTransIndi;
	}

	/**
	 * This method sets the value of FeeSourceCd.
	 * 
	 * @param aiFeeSourceCd   int 
	 */
	public final void setFeeSourceCd(int aiFeeSourceCd)
	{
		ciFeeSourceCd = aiFeeSourceCd;
	}

	/**
	 * This method sets the value of InvTransCdType.
	 * 
	 * @param aiInvTransCdType   int 
	 */
	public final void setInvTransCdType(int aiInvTransCdType)
	{
		ciInvTransCdType = aiInvTransCdType;
	}

	/**
	 * Set value of ciPndngTransLookupIndi
	 * 
	 * @param aiPndngTransLookupIndi
	 */
	public void setPndngTransLookupIndi(int aiPndngTransLookupIndi)
	{
		ciPndngTransLookupIndi = aiPndngTransLookupIndi;
	}

	/**
	 * This method sets the value of RcptMsgCd.
	 * 
	 * @param aiRcptMsgCd   int 
	 */
	public final void setRcptMsgCd(int aiRcptMsgCd)
	{
		ciRcptMsgCd = aiRcptMsgCd;
	}

	/**
	 * This method sets the value of TransCd.
	 * 
	 * @param asransCd   String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * This method sets the value of TransCdDesc.
	 * 
	 * @param asransCdDesc   String 
	 */
	public final void setTransCdDesc(String asTransCdDesc)
	{
		csTransCdDesc = asTransCdDesc;
	}

	/**
	 * Set value of TtlTrnsfrPnltyExmptCd
	 * 
	 * @param asTtlTrnsfrPnltyExmptCd
	 */
	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)
	{
		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * This method sets the value of VoidableTransIndi.
	 * 
	 * @param aioidableTransIndi   int 
	 */
	public final void setVoidableTransIndi(int aiVoidableTransIndi)
	{
		ciVoidableTransIndi = aiVoidableTransIndi;
	}
}
