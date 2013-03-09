package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.webservices.ren.data.RTSFees;

/*
 *
 * FeesData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B. Brown		08/19/2005  Regain removed fields needed due to Complete
 * 							TransactionData being stored in table. 
 * 							rts_itrnt_data.
 * 							add cItemPrice
 * 							add getCItemPrice()
 * 							modify getItemPrice()			
 *							defect 7899 Ver 5.2.3 
 * K Harrell	11/08/2011	add FeesData(RTSFees) 
 * 							defect 11149 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * FeesData 
 * 
 * @version	6.9.0 		11/08/2011
 * @author	Bobby Tulsiani
 * @since 				08/30/2001 15:03:40
 */

public class FeesData implements java.io.Serializable, Comparable
{
	// int
	private int ciCrdtAllowedIndi;
	private int ciItmQty;

	// Object 
	private Dollar caItemPrice;

	// String 
	private String csAcctItmCd;
	private String csDesc;
	
	//defect 7889
	private Dollar cItemPrice;
	// end defect 7889

	private final static long serialVersionUID = 8000771892835454664L;
	/**
	 * FeesData constructor comment.
	 */
	public FeesData()
	{
		super();
	}
	
	/**
	 * FeesData constructor
	 * 
	 *  @param RTSFees 
	 */
	public FeesData(RTSFees aaFees)
	{
		if (aaFees != null)
		{
			setAcctItmCd(aaFees.getFeesAcctCd()); 
			setDesc(aaFees.getFeesAcctCdDesc()); 
			setItemPrice(new Dollar(aaFees.getItemAmt()));
		}
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
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{

		String lsAcctItmCd = ((FeesData) aaObject).getAcctItmCd();
		String lsThisAcctItmCd = this.getAcctItmCd();
		return lsThisAcctItmCd.compareTo(lsAcctItmCd);
	}
	/**
	 * Return the value of AcctItmCd
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}
	/**
	 * Return the value of CrdtAllowedIndi
	 * 
	 * @return int
	 */
	public int getCrdtAllowedIndi()
	{
		return ciCrdtAllowedIndi;
	}
	/**
	 * Return the value of Desc 
	 * 
	 * @return String
	 */
	public String getDesc()
	{
		return csDesc;
	}
	/**
	 * Return the value of ItemPrice 
	 * 
	 * @return Dollar
	 */
	public Dollar getItemPrice()
	{
		// defect 7889	
		if (caItemPrice==null)
		{
			setItemPrice(cItemPrice);
		}
		// end defect 7889
		return caItemPrice;
	}
	/**
	 * Return the value of ItmQty
	 * 
	 * @return int
	 */
	public int getItmQty()
	{
		return ciItmQty;
	}
	/**
	 * Set the value of AcctItmCd  
	 * 
	 * @param asAcctItmCd String
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}
	/**
	 * Set the value of CrdtAllowedIndi  
	 * 
	 * @param aiCrdtAllowedIndi int
	 */
	public void setCrdtAllowedIndi(int aiCrdtAllowedIndi)
	{
		ciCrdtAllowedIndi = aiCrdtAllowedIndi;
	}
	/**
	 * Set the value of Desc 
	 * 
	 * @param asDesc String
	 */
	public void setDesc(String asDesc)
	{
		csDesc = asDesc;
	}
	/**
	 * Set the value of ItemPrice
	 * 
	 * @param aaItemPrice float
	 */
	public void setItemPrice(Dollar aaItemPrice)
	{
		caItemPrice = aaItemPrice;
	}
	/**
	 * Set the value of ItmQty 
	 * 
	 * @param aiItmQty int
	 */
	public void setItmQty(int aiItmQty)
	{
		ciItmQty = aiItmQty;
	}
	/**
	 * Return the value of cItemPrice
	 * 
	 * @return Dollar
	 * @deprecated
	 */
	public Dollar getCItemPrice()
	{
		return cItemPrice;
	}

}
