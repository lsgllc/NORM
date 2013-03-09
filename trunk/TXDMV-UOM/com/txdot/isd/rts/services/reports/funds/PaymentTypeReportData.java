package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * PaymentTypeReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   10/04/2001  No longer extends PaymentSummaryData
 * S Johnston	05/12/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * This class contains the data for payment type reports
 *
 * @version: 5.2.3		05/12/2005
 * @author: Kathy Harrell
 * <br>Creation date: 	09/19/2001 15:04:59
 */
public class PaymentTypeReportData implements Serializable, Comparable
{
	protected Dollar caPymntTypeAmt;
	protected int ciCashWsId;
	protected int ciPymntTypeQty;
	protected int ciFeeSourceCd;
	protected int ciCashDrawerIndi;
	protected String csTransEmpId;
	protected String csPymntTypeCdDesc;
	protected String csKey1;
	protected String csKey2;
	protected String csKey3;
	protected String csKey4;
	
	/**
	 * Compares this object with the specified object for order.
	 * Returns a negative integer, zero, or a positive integer as this
	 * object is less than, equal to, or greater than the specified
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.
	 * (This implies that <tt>x.compareTo(y)</tt> must throw an
	 * exception iff <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt>
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y) == 0
	 * </tt> implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))
	 * </tt>, for all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally
	 * speaking, any class that implements the <tt>Comparable</tt>
	 * interface and violates this condition should clearly indicate
	 * this fact.  The recommended language is "Note: this class has a
	 * natural ordering that is inconsistent with equals."
	 * 
	 * @param aaPaymentTypeReportData Object - the Object to be compared
	 * @return int - a negative integer, zero, or a positive integer as
	 *  this object is less than, equal to, or greater than the
	 *  specified object.
	 */
	public int compareTo(Object aaPaymentTypeReportData)
	{
		// set up the keys for the object that is passed in
		PaymentTypeReportData laPaymentTypeReportData =
			(PaymentTypeReportData) aaPaymentTypeReportData;
		String lsPaymentTypeReportDataKey1 =
			laPaymentTypeReportData.getKey1();
		String lsPaymentTypeReportDataKey2 =
			laPaymentTypeReportData.getKey2();
		String lsPaymentTypeReportDataKey3 =
			laPaymentTypeReportData.getKey3();
		String lsPaymentTypeReportDataKey4 =
			laPaymentTypeReportData.getKey4();

		// set up the keys for this current object, so we can do a
		// comparison.  Note this is not the object that was passed in.
		// it is the current object that is containing the results of
		// the sort
		String lsKey1 = getKey1();
		String lsKey2 = getKey2();
		String lsKey3 = getKey3();
		String lsKey4 = getKey4();

		return (lsKey1 + lsKey2 + lsKey3 + lsKey4).compareTo(
			lsPaymentTypeReportDataKey1
				+ lsPaymentTypeReportDataKey2
				+ lsPaymentTypeReportDataKey3
				+ lsPaymentTypeReportDataKey4);
	}
	
	/**
	 * Returns the value of CashDrawerIndi
	 * 
	 * @return int 
	 */
	public final int getCashDrawerIndi()
	{
		return ciCashDrawerIndi;
	}
	
	/**
	 * Returns the value of CashWsId
	 * 
	 * @return int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}
	
	/**
	 * Returns the value of FeeSourceCd
	 * 
	 * @return int 
	 */
	public final int getFeeSourceCd()
	{
		return ciFeeSourceCd;
	}
	
	/**
	 * Returns the value of key1
	 * 
	 * @return String 
	 */
	public final String getKey1()
	{
		return csKey1;
	}
	
	/**
	 * Returns the value of key2
	 * 
	 * @return String 
	 */
	public final String getKey2()
	{
		return csKey2;
	}
	
	/**
	 * Returns the value of key3
	 * 
	 * @return String 
	 */
	public final String getKey3()
	{
		return csKey3;
	}
	
	/**
	 * Returns the value of key4
	 * 
	 * @return String
	 */
	public final String getKey4()
	{
		return csKey4;
	}
	
	/**
	 * Returns the value of PymntTypeAmt
	 * 
	 * @return Dollar 
	 */
	public final Dollar getPymntTypeAmt()
	{
		return caPymntTypeAmt;
	}
	
	/**
	 * Returns the value of PymntTypeCdDesc
	 * 
	 * @return String 
	 */
	public final String getPymntTypeCdDesc()
	{
		return csPymntTypeCdDesc;
	}
	
	/**
	 * Returns the value of PymntTypeQty
	 * 
	 * @return int 
	 */
	public final int getPymntTypeQty()
	{
		return ciPymntTypeQty;
	}
	
	/**
	 * Returns the value of TransEmpId
	 * 
	 * @return String 
	 */
	public final String getTransEmpId()
	{
		return csTransEmpId;
	}
	
	/**
	 * This method sets the value of CashDrawerIndi.
	 * 
	 * @param aiCashDrawerIndi int 
	 */
	public final void setCashDrawerIndi(int aiCashDrawerIndi)
	{
		ciCashDrawerIndi = aiCashDrawerIndi;
	}
	
	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	
	/**
	 * This method sets the value of FeeSourceCd.
	 * 
	 * @param aiFeeSourceCd int 
	 */
	public final void setFeeSourceCd(int aiFeeSourceCd)
	{
		ciFeeSourceCd = aiFeeSourceCd;
	}
	
	/**
	 * This method sets the value of Key1.
	 * 
	 * @param asKey1 String 
	 */
	public final void setKey1(String asKey1)
	{
		csKey1 = asKey1;
	}
	
	/**
	 * This method sets the value of Key2.
	 * 
	 * @param asKey2 String 
	 */
	public final void setKey2(String asKey2)
	{
		csKey2 = asKey2;
	}
	
	/**
	 * This method sets the value of Key3.
	 * 
	 * @param asKey3 String 
	 */
	public final void setKey3(String asKey3)
	{
		csKey3 = asKey3;
	}
	
	/**
	 * This method sets the value of Key4
	 * 
	 * @param asKey4 String
	 */
	public final void setKey4(String asKey4)
	{
		csKey4 = asKey4;
	}
	
	/**
	 * This method sets the value of PymntTypeAmt.
	 * 
	 * @param aaPymntTypeAmt Dollar 
	 */
	public final void setPymntTypeAmt(Dollar aaPymntTypeAmt)
	{
		caPymntTypeAmt = aaPymntTypeAmt;
	}
	
	/**
	 * This method sets the value of PymntTypeCdDesc.
	 * 
	 * @param asPymntTypeCdDesc String 
	 */
	public final void setPymntTypeCdDesc(String asPymntTypeCdDesc)
	{
		csPymntTypeCdDesc = asPymntTypeCdDesc;
	}
	
	/**
	 * This method sets the value of PymntTypeQty.
	 * 
	 * @param aiPymntTypeQty int 
	 */
	public final void setPymntTypeQty(int aiPymntTypeQty)
	{
		ciPymntTypeQty = aiPymntTypeQty;
	}
	
	/**
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
}
