package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * CreditCardFeeData.java
 *
 * (c) Texas Department of Transportation 2002
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * Data object used in managing Credit Card Fees. 
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	04/26/2002 11:20:41
 */

public class CreditCardFeeData
	implements java.io.Serializable, Comparable
{
	// boolean
	private boolean cbPercentage;

	// int 
	private int ciDeleteIndi;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;

	// Object
	private Dollar caItmPrice;
	private RTSDate caChngTimestmp;
	private RTSDate caRTSEffDate;
	private RTSDate caRTSEffEndDate;

	private final static long serialVersionUID = 5137979654178658094L;
	/**
	 * CreditCardFeesData constructor comment.
	 */
	public CreditCardFeeData()
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
	 * @param   aaObject	Object
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		CreditCardFeeData aaData = (CreditCardFeeData) aaObject;
		return getRTSEffDate().compareTo(aaData.getRTSEffDate());
	}
	/**
	 * Returns the value of ChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Returns the value of DeleteIndi
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}
	/**
	 * Returns the value of ItmPrice
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice()
	{
		return caItmPrice;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of RTSEffDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRTSEffDate()
	{
		return caRTSEffDate;
	}
	/**
	 * Returns the value of RTSEffEndDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRTSEffEndDate()
	{
		return caRTSEffEndDate;
	}
	/**
	 * Returns the value of SubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of Percentage 
	 * 
	 * @return boolean
	 */
	public boolean isPercentage()
	{
		return cbPercentage;
	}
	/**
	 * Sets the value of ChngTimestmp
	 * 
	 * @param aaChngTimestmp RTSDate
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * Sets the value of DeleteIndi
	 * 
	 * @param aiDeleteIndi int
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}
	/**
	 * Sets the value of ItmPrice
	 * 
	 * @param aaItmPrice Dollar
	 */
	public void setItmPrice(Dollar aaItmPrice)
	{
		caItmPrice = aaItmPrice;
	}
	/**
	 * Sets the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Sets the value of Percentage 
	 * 
	 * @param abPercentage boolean
	 */
	public void setPercentage(boolean abPercentage)
	{
		cbPercentage = abPercentage;
	}
	/**
	 * Sets the value of RTSEffDate 
	 * 
	 * @param aaRTSEffDate RTSDate
	 */
	public void setRTSEffDate(RTSDate aaRTSEffDate)
	{
		caRTSEffDate = aaRTSEffDate;
	}
	/**
	 * Sets the value of RTSEffEndDate
	 * 
	 * @param aaRTSEffEndDate RTSDate
	 */
	public void setRTSEffEndDate(RTSDate aaRTSEffEndDate)
	{
		caRTSEffEndDate = aaRTSEffEndDate;
	}
	/**
	 * Sets the value of SubstaId
	 * 
	 * @param aiSubstaId int
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
}
