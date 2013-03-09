package com.txdot.isd.rts.services.data;

/*
 * InventoryData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							added ciQtyReprnted, ciTotQtyReprnted and
 *							associated get/set methods
 * 							Ver 5.2.0
 * K Harrell	07/15/2004	Incorrect reprint sticker counts on
 *							Inventory Summary Report
 *							modify setTotReprnted()
 *							defect 7339  Ver 5.2.0
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * InventoryData
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Bobby Tulsiani
 * <br>Creation Date: 
 */

public class InventoryData implements java.io.Serializable, Comparable
{
	// int 
	private int ciCashWsId;
	private int ciInvItmYr;
	private int ciQtyReprnted;
	private int ciQtyReused;
	private int ciQtySold;
	private int ciQtyVoided;
	private int ciTotQtyReprnted;
	private int ciTotQtyReused;
	private int ciTotQtySold;
	private int ciTotQtyVoided;

	// String 
	private String csItmCdDesc;
	private String csKey1;
	private String csKey2;
	private String csTransEmpId;

	private final static long serialVersionUID = 2483426572450007398L;
	/**
	 * InventoryData constructor comment.
	 */
	public InventoryData()
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
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	//Java creates the compareTo(object) method.  Just need to add the keys.
	// To use this, call the sort(avlPaymentTypeReportData - or whatever vector of objects)
	// and the sort code invokes this compareTo !!
	// This compareTo method needs to be in the data object class!!! 
	public int compareTo(Object aaObject)
	{
		// set up the keys for the object that is passed in
		InventoryData laInventoryData = (InventoryData) aaObject;
		String lsInventoryDataKey1 = laInventoryData.getItmCdDesc();
		String lsInventoryDataKey2 =
			Integer.toString(laInventoryData.getInvItmYr());
		String lsKey1 = getKey1();
		String lsKey2 = getKey2();
		return (lsKey1 + lsKey2).compareTo(
			lsInventoryDataKey1 + lsInventoryDataKey2);
	}
	/**
	 * Return the value of CashWsId
	 * 
	 * @return int
	 */
	public int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * Return the value of InvItmYr
	 * 
	 * @return int
	 */
	public int getInvItmYr()
	{
		return ciInvItmYr;
	}
	/**
	 * Return the value of ItmCdDesc
	 * 
	 * @return String
	 */
	public String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	/**
	 * Return the value of Key1
	 * 
	 * @return String
	 */
	public String getKey1()
	{
		return csKey1;
	}
	/**
	 * Return the value of Key2
	 * 
	 * @return String
	 */
	public String getKey2()
	{
		return csKey2;
	}
	/**
	 * Return the value of QtyReprnted
	 * 
	 * @return int
	 */
	public int getQtyReprnted()
	{
		return ciQtyReprnted;
	}
	/**
	 * Return the value of QtyReused
	 * 
	 * @return int
	 */
	public int getQtyReused()
	{
		return ciQtyReused;
	}
	/**
	 * Return the value of QtySold
	 * 
	 * @return int
	 */
	public int getQtySold()
	{
		return ciQtySold;
	}
	/**
	 * Return the value of QtyVoided
	 * 
	 * @return int
	 */
	public int getQtyVoided()
	{
		return ciQtyVoided;
	}
	/**
	 * Return the value of TotQtyReprnted
	 * 
	 * @return int
	 */
	public int getTotQtyReprnted()
	{
		return ciTotQtyReprnted;
	}
	/**
	/**
	 * Return the value of TotQtyReused
	 * 
	 * @return int
	 */
	public int getTotQtyReused()
	{
		return ciTotQtyReused;
	}
	/**
	 * Return the value of TotQtySold
	 * 
	 * @return int
	 */
	public int getTotQtySold()
	{
		return ciTotQtySold;
	}
	/**
	 * Return the value of TotQtyVoided
	 * 
	 * @return int
	 */
	public int getTotQtyVoided()
	{
		return ciTotQtyVoided;
	}
	/**
	 * Return the value of TransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Set the value of CashWsId
	 * 
	 * @param aiCashWsId int
	 */
	public void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * Set the value of InvItmYr
	 * 
	 * @param aiInvItmYr int
	 */
	public void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	/**
	 * Set the value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	/**
	 * Set the value of Key1
	 * 
	 * @param asKey1 String
	 */
	public void setKey1(String asKey1)
	{
		csKey1 = asKey1;
	}
	/**
	 * Set the value of Key2
	 * 
	 * @param asKey2 String
	 */
	public void setKey2(String asKey2)
	{
		csKey2 = asKey2;
	}
	/**
	 * Set the value of QtyReprnted 
	 * 
	 * @param aiQtyReprnted int
	 */
	public void setQtyReprnted(int aiQtyReprnted)
	{
		ciQtyReprnted = aiQtyReprnted;
	}
	/**
	 * Set the value of QtyReused
	 * 
	 * @param aiQtyReused int
	 */
	public void setQtyReused(int aiQtyReused)
	{
		ciQtyReused = aiQtyReused;
	}
	/**
	 * Set the value of QtySold
	 * 
	 * @param aiQtySold int
	 */
	public void setQtySold(int aiQtySold)
	{
		ciQtySold = aiQtySold;
	}
	/**
	 * Set the value of QtyVoided
	 * 
	 * @param aiQtyVoided int
	 */
	public void setQtyVoided(int aiQtyVoided)
	{
		ciQtyVoided = aiQtyVoided;
	}
	/**
	 * Set the value of TotQtyReprnted
	 * 
	 * @param aiTotQtyReprnted int
	 */
	public void setTotQtyReprnted(int aiTotQtyReprnted)
	{
		// defect 7339
		// add passed quantity to TotQtyReprnted 
		//ciTotQtyReprnted = aTotQtyReprnted;
		ciTotQtyReprnted = ciTotQtyReprnted + aiTotQtyReprnted;
		// end defect 7339 
	}
	/**
	 * Set the value of TotQtyReused 
	 * 
	 * @param aiTotQtyReused int
	 */
	public void setTotQtyReused(int aiTotQtyReused)
	{
		ciTotQtyReused = ciTotQtyReused + aiTotQtyReused;
	}
	/**
	 * Set the value of TotQtySold
	 * 
	 * @param aiTotQtySold int
	 */
	public void setTotQtySold(int aiTotQtySold)
	{
		ciTotQtySold = ciTotQtySold + aiTotQtySold;
	}
	/**
	 * Set the value of TotQtyVoided
	 * 
	 * @param aiTotQtyVoided int
	 */
	public void setTotQtyVoided(int aiTotQtyVoided)
	{
		ciTotQtyVoided = ciTotQtyVoided + aiTotQtyVoided;
	}
	/**
	 * Set the value of TransEmpId
	 * 
	 * @param asTransEmpId String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
}
