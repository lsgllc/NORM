package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * TransactionIdData.java 
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/20/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/01/2011	add getTransactionDateYYYYMMDD()
 *							defect 10844 Ver 6.8.0 
 * K Harrell	06/19/2011	add compareTo, getCompareDateTime(), 
 * 							 getCentralTime() 
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	10/08/2011	Modified to use Numerics vs. String to 
 * 							compare date/time
 * 							modify getCompareDateTime()
 * 							defect 11025 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * Class to Dissect TransId 
 *
 * @version	6.9.0 		10/08/2011
 * @author	Kathy Harrell
 * @since				06/20/2010	16:35:17 
 */
public class TransactionIdData implements Comparable
{
	private int ciOfcIssuanceNo = 0;
	private int ciTransAMDate = 0;
	private int ciTransTime = 0;
	private int ciTransWsId = 0;

	private final static int TRANSID_LENGTH = 17;

	/**
	 * TransactionIdData Constructor
	 * 
	 */
	public TransactionIdData(String asTransId)
	{
		super();
		String lsConvert = new String();
		if (asTransId.length() == TRANSID_LENGTH)
		{
			// OfcIssuanceNo 
			lsConvert = asTransId.substring(0, 3);
			ciOfcIssuanceNo = getInt(lsConvert);

			// TransWsId 
			lsConvert = asTransId.substring(3, 6);
			ciTransWsId = getInt(lsConvert);

			// TransAMDate 
			lsConvert = asTransId.substring(6, 11);
			ciTransAMDate = getInt(lsConvert);

			// TransTime 
			lsConvert = asTransId.substring(11, 17);
			ciTransTime = getInt(lsConvert);
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
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		return (
			getCompareDateTime().compareTo(
				((TransactionIdData) aaObject).getCompareDateTime()));
	}

	/**
	 * Return Double of TransAMDate * 1000000.0 + getCentralTime());
	 * 
	 * @return Double  
	 */
	private Double getCompareDateTime()
	{
		return new Double(getTransAMDate()* 1000000.0 + getCentralTime());
	}

	/**
	 * Return int representation of String  
	 * 
	 * @param asConvert
	 * @return int 
	 */
	private int getInt(String asConvert)
	{
		int liConvert = 0;
		if (!UtilityMethods.isEmpty(asConvert))
		{
			try
			{
				liConvert = (new Integer(asConvert)).intValue();
			}
			catch (Exception aeEx)
			{
			}
		}
		return liConvert;
	}

	/**
	 * Return value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Return value of ciTransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Return Transaction Date in YYYYMMDD format   
	 * 
	 * @return int  
	 */
	public int getTransactionDateYYYYMMDD()
	{
		int liYYYYMMDD = 0;

		if (ciTransAMDate != 0)
		{
			liYYYYMMDD =
				new RTSDate(RTSDate.AMDATE, ciTransAMDate)
					.getYYYYMMDDDate();
		}
		return liYYYYMMDD;
	}

	/**
	 * Return Transaction Date  
	 * 
	 * @return 
	 */
	public String getTransactionDateMMDDYYYY()
	{
		String lsMMDDYYYY = new String();
		if (ciTransAMDate != 0)
		{
			RTSDate laIssueDate =
				new RTSDate(RTSDate.AMDATE, ciTransAMDate);
			lsMMDDYYYY = laIssueDate.getMMDDYYYY();
		}
		return lsMMDDYYYY;
	}

	/**
	 * Return value of ciTransTime
	 * 
	 * @return int 
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Return value of ciTransWsId
	 * 
	 * @return int 
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Return Central Time
	 * 
	 * @return int
	 */
	public int getCentralTime()
	{
		int liTransTime = ciTransTime;
		if (OfficeTimeZoneCache.isMountainTimeZone(getOfcIssuanceNo()))
		{
			liTransTime = liTransTime + 10000;
		}

		return liTransTime;
	}

}
