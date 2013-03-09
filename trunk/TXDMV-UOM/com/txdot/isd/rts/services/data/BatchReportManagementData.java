package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * BatchReportManagementData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/14/2011	Created
 * 							defect 10701 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Data class for RTS.RTS_BATCH_RPT_MGMT
 *
 * @version	6.7.0 			01/14/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/14/2011 16:16:17
 */
public class BatchReportManagementData
	implements Serializable, Comparable
{
	private int ciAutoPrntIndi;
	private int ciDeleteIndi;
	private int ciOfcIssuanceNo;
	private int ciRptNumber;
	private int ciSubstaId;

	private String csFileName;
	private String csRptDesc;

	private RTSDate caChngTimestmp;

	static final long serialVersionUID = -8837859879234462665L;

	/**
	 * BatchReportManagementData.java Constructor
	 */
	public BatchReportManagementData()
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
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		return getRptDesc()
			.compareTo(((BatchReportManagementData) aaObject)
			.getRptDesc());
	}

	/**
	 * Get value of ciAutoPrntIndi
	 * 
	 * @return int
	 */
	public int getAutoPrntIndi()
	{
		return ciAutoPrntIndi;
	}

	/**
	 * Get value of caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}

	/**
	 * Get value of ciDeleteIndi
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Get value of csFileName
	 * 
	 * @return String 
	 */
	public String getFileName()
	{
		return csFileName;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get value of csRptDesc
	 * 
	 * @return String 
	 */
	public String getRptDesc()
	{
		return csRptDesc;
	}

	/**
	 * Get value of ciRptNumber
	 * 
	 * @return int
	 */
	public int getRptNumber()
	{
		return ciRptNumber;
	}

	/**
	 * Get value of ciSubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Set value of ciAutoPrntIndi
	 * 
	 * @return boolean 
	 */
	public boolean isAutoPrnt()
	{
		return ciAutoPrntIndi == 1;
	}

	/**
	 * Set value of ciAutoPrntIndi
	 * 
	 * @param aiAutoPrntIndi
	 */
	public void setAutoPrntIndi(int aiAutoPrntIndi)
	{
		ciAutoPrntIndi = aiAutoPrntIndi;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Return value of csFileName
	 * 
	 * @param asFileName
	 */
	public void setFileName(String asFileName)
	{
		csFileName = asFileName;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of csRptDesc
	 * 
	 * @param asRptDesc
	 */
	public void setRptDesc(String asRptDesc)
	{
		csRptDesc = asRptDesc;
	}

	/**
	 * Set value of ciRptNumber
	 * 
	 * @param aiRptNumber
	 */
	public void setRptNumber(int aiRptNumber)
	{
		ciRptNumber = aiRptNumber;
	}

	/**
	 * Set value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

}
