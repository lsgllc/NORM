package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * MFPartialPermitData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10492 Ver 6.5.0
 * K Harrell	06/09/2010	add ciExpDate, csVehRegPltNo, get/set methods
 * 							delete ciPrmtExpDate, get/set methods 
 * 							defect 10492 Ver 6.5.0 
 * K Harrell	06/20/2010	Use CustName v.s. CustomerNameData
 * 							add csCustName, get/set method
 * 							delete caCustNameData, get/set methods
 * 						 	modify MFParitalPermitData(), compareTo()
 * 							defect 10492 Ver 6.5.0
 * K Harrell	07/08/2010	add isExpired() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/14/2010	add ciExpTime, get/set methods
 * 							add getRTSExpDateTime() 
 * 							modify isExpired() 
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * MFPartialPermitData 
 *
 * @version	6.5.0			07/14/2010
 * @author	K Harrell
 * <br>Creation Date:		05/24/2010  11:17:17 
 */
public class MFPartialPermitData implements Serializable, Comparable
{

	static final long serialVersionUID = 8141272579064792218L;

	// Integer 
	private int ciExpDate;
	private int ciExpTime;
	private int ciVehModlYr;

	// String
	private String csCustName;
	private String csItmCd;
	private String csPrmtIssuanceId;
	private String csPrmtNo;
	private String csVehMk;
	private String csVin;

	/**
	 * MFPartialPermitData.java Constructor
	 * 
	 */
	public MFPartialPermitData()
	{
		super();
	}

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
		MFPartialPermitData laCompTo = (MFPartialPermitData) aaObject;
		return csCustName.compareTo(laCompTo.getCustName());
	}

	/**
	 * Returns value of csCustName
	 * 
	 * @return String
	 */
	public String getCustName()
	{
		return csCustName;
	}

	/**
	 * Returns value of ciExpDate
	 * 
	 * @return int
	 */
	public int getExpDate()
	{
		return ciExpDate;
	}

	/**
	 * Returns value of ciExpTime
	 * 
	 * @return int
	 */
	public int getExpTime()
	{
		return ciExpTime;
	}

	/**
	 * Returns value of csItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}

	/**
	 * Returns value of csPrmtIssuanceId
	 * 
	 * @return String
	 */
	public String getPrmtIssuanceId()
	{
		return csPrmtIssuanceId;
	}

	/**
	 * Returns value of csPrmtNo
	 * 
	 * @return String
	 */
	public String getPrmtNo()
	{
		return csPrmtNo;
	}

	/** 
	 * Returns RTSDate of Expiration Date/Time
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getRTSExpDateTime()
	{
		RTSDate laRTSExpDate =
			new RTSDate(RTSDate.YYYYMMDD, getExpDate());

		laRTSExpDate.setTime(getExpTime());

		return laRTSExpDate;
	}

	/**
	 * Returns value of csVehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Returns value of ciVehModlYr
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Returns value of csVin
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}

	/**
	 * Return boolean to denote if Partial Permit is Expired
	 * 
	 * @return boolean 
	 */
	public boolean isExpired()
	{
		boolean lbExpired = true;

		if (getExpDate() != 0)
		{
			RTSDate laRTSExpDate = getRTSExpDateTime();

			lbExpired = laRTSExpDate.compareTo(new RTSDate()) < 0;
		}

		return lbExpired;
	}

	/**
	 * Sets value of csCustName
	 * 
	 * @param aaCustName
	 */
	public void setCustName(String asCustName)
	{
		csCustName = asCustName;
	}

	/**
	 * Sets value of ciExpDate
	 * 
	 * @param aiExpDate
	 */
	public void setExpDate(int aiExpDate)
	{
		ciExpDate = aiExpDate;
	}

	/**
	 * Sets value of ciExpTime
	 * 
	 * @param aiExpTime
	 */
	public void setExpTime(int aiExpTime)
	{
		ciExpTime = aiExpTime;
	}

	/**
	 * Sets value of csItmCd
	 * 
	 * @param asItmCd
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}

	/**
	 * Sets value of csPrmtIssuanceId
	 * 
	 * @param asPrmtIssuanceId
	 */
	public void setPrmtIssuanceId(String asPrmtIssuanceId)
	{
		csPrmtIssuanceId = asPrmtIssuanceId;
	}

	/**
	 * Sets value of csPrmtNo
	 * 
	 * @param asPrmtNo
	 */
	public void setPrmtNo(String asPrmtNo)
	{
		csPrmtNo = asPrmtNo;
	}

	/**
	 * Sets value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Sets value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Sets value of csVin
	 * 
	 * @param asVin
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
}
