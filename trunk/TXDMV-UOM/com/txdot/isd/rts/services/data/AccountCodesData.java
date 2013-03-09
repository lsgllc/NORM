package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * AccountCodesData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		08/24/2001	setData() method change
 * N Ting		08/27/2001	Add comments
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	Java 1.4 Work
 * 							defect 7899 Ver 5.2.3
 * K Harrell	01/19/2011	add caBaseFee, get/set methods 
 * 							defect 10741 Ver 6.7.0   
 *----------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get and set methods for 
 * AccountCodesData
 * 
 * @version	6.7.0 		01/19/2011
 * @author	Administrator
 * <br>Creation Date:	08/30/2001
 *---------------------------------------------------------------
*/

public class AccountCodesData implements Serializable, Comparable
{
	// int
	protected int ciAcctItmCrdtIndi;
	protected int ciAcctItmGrpCd;
	protected int ciCrdtAllowdIndi;
	protected int ciPayableTypeCd;
	protected int ciPrmtStndrdExpTime;
	protected int ciPrmtValidtyPeriod;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	protected int ciShortAcctItmCd;

	// String
	protected String csAcctItmCd;
	protected String csAcctItmCdDesc;
	protected String csAcctProcsCd;
	protected String csRedemdAcctItmCd;
	protected String csRenwlPrntSmryCd;
	
	// Object
	// defect 10741 
	protected Dollar caBaseFee;
	// end defect 10741 
	protected Dollar caMiscFee;
	protected Dollar caPrmtFeePrcnt;
	protected Dollar caPrmtFlatFee;

	
	private final static long serialVersionUID = 3755682057002948766L;

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
	 */
	public int compareTo(Object aaObject)
	{
		AccountCodesData laAcctCdsData = (AccountCodesData) aaObject;
		return getAcctItmCdDesc().compareTo(
			laAcctCdsData.getAcctItmCdDesc());
	}
	/**
	 * Returns the value of AcctItmCd
	 * 
	 * @return  String 
	 */
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
	}
	/**
	 * Returns the value of AcctItmCdDesc
	 * 
	 * @return  String 
	 */
	public final String getAcctItmCdDesc()
	{
		return csAcctItmCdDesc;
	}
	/**
	 * Returns the value of AcctItmCrdtIndi
	 * 
	 * @return  int 
	 */
	public final int getAcctItmCrdtIndi()
	{
		return ciAcctItmCrdtIndi;
	}
	/**
	 * Returns the value of AcctItmGrpCd
	 * 
	 * @return  int 
	 */
	public final int getAcctItmGrpCd()
	{
		return ciAcctItmGrpCd;
	}
	/**
	 * Returns the value of AcctProcsCd
	 * 
	 * @return  String 
	 */
	public final String getAcctProcsCd()
	{
		return csAcctProcsCd;
	}
	/**
	 * Returns value of BaseFee
	 * 
	 * @return Dollar
	 */
	public Dollar getBaseFee()
	{
		return caBaseFee;
	}
	/**
	 * Returns the value of CrdtAllowdIndi
	 * 
	 * @return  int  
	 */
	public final int getCrdtAllowdIndi()
	{
		return ciCrdtAllowdIndi;
	}
	/**
	 * Returns the value of MiscFee
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getMiscFee()
	{
		return caMiscFee;
	}
	/**
	 * Returns the value of PayableTypeCd
	 * @return  int 
	 */
	public final int getPayableTypeCd()
	{
		return ciPayableTypeCd;
	}
	/**
	 * Returns the value of PrmtFeePrcnt
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getPrmtFeePrcnt()
	{
		return caPrmtFeePrcnt;
	}
	/**
	 * Returns the value of PrmtFlatFee
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getPrmtFlatFee()
	{
		return caPrmtFlatFee;
	}
	/**
	 * Returns the value of PrmtStndrdExpTime
	 * 
	 * @return  int 
	 */
	public final int getPrmtStndrdExpTime()
	{
		return ciPrmtStndrdExpTime;
	}
	/**
	 * Returns the value of PrmtValidtyPeriod
	 * 
	 * @return  int 
	 */
	public final int getPrmtValidtyPeriod()
	{
		return ciPrmtValidtyPeriod;
	}
	/**
	 * Returns the value of RedemdAcctItmCd
	 * 
	 * @return  String 
	 */
	public final String getRedemdAcctItmCd()
	{
		return csRedemdAcctItmCd;
	}
	/**
	 * Returns the value of RenwlPrntSmryCd
	 * 
	 * @return  String 
	 */
	public final String getRenwlPrntSmryCd()
	{
		return csRenwlPrntSmryCd;
	}
	/**
	 * Returns the value of RTSEffDate
	 * 
	 * @return  int 
	 */
	public final int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Returns the value of RTSEffEndDate
	 * 
	 * @return  int 
	 */
	public final int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	/**
	 * Returns the value of ShortAcctItmCd
	 * 
	 * @return  int  
	 */
	public final int getShortAcctItmCd()
	{
		return ciShortAcctItmCd;
	}
	/**
	 * This method sets the value of AcctItmCd
	 * 
	 * @param asAcctItmCd   String 
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}
	/**
	 * This method sets the value of AcctItmCdDesc
	
	 * @param asAcctItmCdDesc   String 
	 */
	public final void setAcctItmCdDesc(String asAcctItmCdDesc)
	{
		csAcctItmCdDesc = asAcctItmCdDesc;
	}
	/**
	 * This method sets the value of AcctItmCrdtIndi
	
	 * @param aiAcctItmCrdtIndi   int 
	 */
	public final void setAcctItmCrdtIndi(int aiAcctItmCrdtIndi)
	{
		ciAcctItmCrdtIndi = aiAcctItmCrdtIndi;
	}
	/**
	 * This method sets the value of AcctItmGrpCd
	
	 * @param aiAcctItmGrpCd   int 
	 */
	public final void setAcctItmGrpCd(int aiAcctItmGrpCd)
	{
		ciAcctItmGrpCd = aiAcctItmGrpCd;
	}
	/**
	 * This method sets the value of AcctProcsCd
	 * @param aAcctProcsCd   String 
	 */
	public final void setAcctProcsCd(String aAcctProcsCd)
	{
		csAcctProcsCd = aAcctProcsCd;
	}

	/**
	 * This method sets the value of BaseFee
	 * 
	 * @param aaBaseFee
	 */
	public void setBaseFee(Dollar aaBaseFee)
	{
		caBaseFee = aaBaseFee;
	}
	/**
	 * This method sets the value of CrdtAllowdIndi
	 * 
	 * @param asCrdtAllowdIndi   int  
	 */
	public final void setCrdtAllowdIndi(int asCrdtAllowdIndi)
	{
		ciCrdtAllowdIndi = asCrdtAllowdIndi;
	}
	/**
	 * This method sets the value of MiscFee
	 * 
	 * @param aaMiscFee   Dollar 
	 */
	public final void setMiscFee(Dollar aaMiscFee)
	{
		caMiscFee = aaMiscFee;
	}
	/**
	 * This method sets the value of PayableTypeCd
	 * 
	 * @param aiPayableTypeCd   int 
	 */
	public final void setPayableTypeCd(int aiPayableTypeCd)
	{
		ciPayableTypeCd = aiPayableTypeCd;
	}
	/**
	 * This method sets the value of PrmtFeePrcnt
	 * 
	 * @param aaPrmtFeePrcnt   Dollar 
	 */
	public final void setPrmtFeePrcnt(Dollar aaPrmtFeePrcnt)
	{
		caPrmtFeePrcnt = aaPrmtFeePrcnt;
	}
	/**
	 * This method sets the value of PrmtFlatFee
	 * 
	 * @param aaPrmtFlatFee   Dollar 
	 */
	public final void setPrmtFlatFee(Dollar aaPrmtFlatFee)
	{
		caPrmtFlatFee = aaPrmtFlatFee;
	}
	/**
	 * This method sets the value of PrmtStndrdExpTime
	 * 
	 * @param aiPrmtStndrdExpTime   int 
	 */
	public final void setPrmtStndrdExpTime(int aiPrmtStndrdExpTime)
	{
		ciPrmtStndrdExpTime = aiPrmtStndrdExpTime;
	}
	/**
	 * This method sets the value of PrmtValidtyPeriod
	 * 
	 * @param aiPrmtValidtyPeriod   int 
	 */
	public final void setPrmtValidtyPeriod(int aiPrmtValidtyPeriod)
	{
		ciPrmtValidtyPeriod = aiPrmtValidtyPeriod;
	}
	/**
	 * This method sets the value of RedemdAcctItmCd
	 * 
	 * @param asRedemdAcctItmCd   String 
	 */
	public final void setRedemdAcctItmCd(String asRedemdAcctItmCd)
	{
		csRedemdAcctItmCd = asRedemdAcctItmCd;
	}
	/**
	 * This method sets the value of RenwlPrntSmryCd
	 * 
	 * @param asRenwlPrntSmryCd   String 
	 */
	public final void setRenwlPrntSmryCd(String asRenwlPrntSmryCd)
	{
		csRenwlPrntSmryCd = asRenwlPrntSmryCd;
	}
	/**
	 * This method sets the value of RTSEffDate
	 * 
	 * @param aiRTSEffDate   int  
	 */
	public final void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * This method sets the value of RTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate   int  
	 */
	public final void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
	/**
	 * This method sets the value of ShortAcctItmCd
	 * 
	 * @param aiShortAcctItmCd   int  
	 */
	public final void setShortAcctItmCd(int aiShortAcctItmCd)
	{
		ciShortAcctItmCd = aiShortAcctItmCd;
	}

}
