package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * OfficeIdsData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		09/06/2001	Convert to Hungarian notation.
 * R Hicks      11/27/2001  Added fields to support Phase 2.
 * K Harrell    01/07/2002  Remove EFTIndi
 * 							defect 5228   
 * K Harrell	06/20/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
 * J Ralph		10/18/2006	Implemented Comparable for Exempts
 * 							defect 8900 Ver Exempts 		
 * K Harrell	07/14/2010	add isHQ(), isRegion(), isCounty()
 * 							defect 10491 Ver 6.5.0	 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * OfficeIdsData
 * 
 * @version	6.5.0		07/14/2010
 * @author	Administrator
 * <br>Creation Date: 	09/04/2001 
 */

public class OfficeIdsData implements Serializable, Comparable
{
	// int
	protected int ciEFTAccntCd;
	protected int ciItrntRenwlIndi;
	protected int ciOfcIssuanceCd;
	protected int ciOfcIssuanceNo;
	protected int ciOfcZpCd;
	protected int ciRegnlOfcId;
	protected int ciTXDOTCntyNo;

	// String
	protected String csDefrdRemitCd;
	protected String csEMailAddr;
	protected String csOfcCity;
	protected String csOfcName;
	protected String csOfcSt;
	protected String csOfcZpCdP4;
	protected String csOprtgDays;
	protected String csOprtgHrs;
	protected String csPhysOfcLoc;
	protected String csTacAcctNo;
	protected String csTacName;
	protected String csTacPhoneNo;

	/**
	 * Returns the value of DefrdRemitCd
	 * 
	 * @return  String 
	 */
	public final String getDefrdRemitCd()
	{
		return csDefrdRemitCd;
	}
	/**
	 * Returns the value of EFTAccntCd
	 * 
	 * @return  int 
	 */
	public final int getEFTAccntCd()
	{
		return ciEFTAccntCd;
	}
	/**
	 * Returns the value of EMailAddr
	 * 
	 * @return String
	 */
	public String getEMailAddr()
	{
		return csEMailAddr;
	}
	/**
	 * Returns the value of ItrntRenwlIndi
	 * 
	 * @return int
	 */
	public int getItrntRenwlIndi()
	{
		return ciItrntRenwlIndi;
	}
	/**
	 * Returns the value of OfcCity
	 * 
	 * @return  String 
	 */
	public final String getOfcCity()
	{
		return csOfcCity;
	}
	/**
	 * Returns the value of OfcIssuanceCd
	 * 
	 * @return  int  
	 */
	public final int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int  
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of OfcName
	 * 
	 * @return  String 
	 */
	public final String getOfcName()
	{
		return csOfcName;
	}
	/**
	 * Returns the value of OfcSt
	 * 
	 * @return  String 
	 */
	public final String getOfcSt()
	{
		return csOfcSt;
	}
	/**
	 * Returns the value of OfcZpCd
	 * 
	 * @return  int 
	 */
	public final int getOfcZpCd()
	{
		return ciOfcZpCd;
	}
	/**
	 * Returns the value of OfcZpCdP4
	 * 
	 * @return  String 
	 */
	public final String getOfcZpCdP4()
	{
		return csOfcZpCdP4;
	}
	/**
	 * Returns the value of OprtgDays
	 * 
	 * @return String
	 */
	public String getOprtgDays()
	{
		return csOprtgDays;
	}
	/**
	 * Returns the value of OprtgHrs
	 * 
	 * @return String
	 */
	public String getOprtgHrs()
	{
		return csOprtgHrs;
	}
	/**
	 * Returns the value of PhysOfcLoc
	 * 
	 * @return  String 
	 */
	public final String getPhysOfcLoc()
	{
		return csPhysOfcLoc;
	}
	/**
	 * Returns the value of RegnlOfcId
	 * 
	 * @return  int 
	 */
	public final int getRegnlOfcId()
	{
		return ciRegnlOfcId;
	}
	/**
	 * Returns the value of TacAcctNo
	 * 
	 * @return  String 
	 */
	public final String getTacAcctNo()
	{
		return csTacAcctNo;
	}
	/**
	 * Returns the value of TacName
	 * 
	 * @return  String 
	 */
	public final String getTacName()
	{
		return csTacName;
	}
	/**
	 * Returns the value of TacPhoneNo
	 * 
	 * @return  String 
	 */
	public final String getTacPhoneNo()
	{
		return csTacPhoneNo;
	}
	/**
	 * Returns the value of TXDOTCntyNo
	 * 
	 * @return  int  
	 */
	public final int getTXDOTCntyNo()
	{
		return ciTXDOTCntyNo;
	}

	/** 
	 * Return boolean to denote if HQ
	 *
	 * @return boolean  
	 */
	public final boolean isHQ()
	{
		return getOfcIssuanceCd() == CommonConstant.OFCISSUANCECD_HQ;
	}
	
	/** 
	 * Return boolean to denote if Region
	 *
	 * @return boolean  
	 */
	public final boolean isRegion()
	{
		return getOfcIssuanceCd()
			== CommonConstant.OFCISSUANCECD_REGION;
	}
	
	/** 
	 * Return boolean to denote if County
	 *
	 * @return boolean  
	 */
	public final boolean isCounty()
	{
		return getOfcIssuanceCd()
			== CommonConstant.OFCISSUANCECD_COUNTY;
	}

	/**
	 * This method sets the value of DefrdRemitCd.
	 * 
	 * @param asDefrdRemitCd   String 
	 */
	public final void setDefrdRemitCd(String asDefrdRemitCd)
	{
		csDefrdRemitCd = asDefrdRemitCd;
	}
	/**
	 * This method sets the value of EFTAccntCd.
	 * 
	 * @param aiEFTAccntCd   int 
	 */
	public final void setEFTAccntCd(int aiEFTAccntCd)
	{
		ciEFTAccntCd = aiEFTAccntCd;
	}
	/**
	 * This method sets the value of CsEMailAddr
	 * 
	 * @param asCsEMailAddr String
	 */
	public void setEMailAddr(String asCsEMailAddr)
	{
		csEMailAddr = asCsEMailAddr;
	}
	/**
	 * This method sets the value of ItrntRenwlIndi
	 * 
	 * @param aiItrntRenwlIndi int
	 */
	public void setItrntRenwlIndi(int aiItrntRenwlIndi)
	{
		ciItrntRenwlIndi = aiItrntRenwlIndi;
	}
	/**
	 * This method sets the value of OfcCity.
	 * 
	 * @param asOfcCity   String 
	 */
	public final void setOfcCity(String asOfcCity)
	{
		csOfcCity = asOfcCity;
	}
	/**
	 * This method sets the value of OfcIssuanceCd.
	 * 
	 * @param aiOfcIssuanceCd   int  
	 */
	public final void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int  
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of OfcName.
	 * 
	 * @param asOfcName   String 
	 */
	public final void setOfcName(String asOfcName)
	{
		csOfcName = asOfcName;
	}
	/**
	 * This method sets the value of OfcSt.
	 * 
	 * @param asOfcSt   String 
	 */
	public final void setOfcSt(String asOfcSt)
	{
		csOfcSt = asOfcSt;
	}
	/**
	 * This method sets the value of OfcZpCd.
	 * 
	 * @param aiOfcZpCd   int 
	 */
	public final void setOfcZpCd(int aiOfcZpCd)
	{
		ciOfcZpCd = aiOfcZpCd;
	}
	/**
	 * This method sets the value of OfcZpCdP4.
	 * 
	 * @param asOfcZpCdP4   String 
	 */
	public final void setOfcZpCdP4(String asOfcZpCdP4)
	{
		csOfcZpCdP4 = asOfcZpCdP4;
	}
	/**
	 * This method sets the value of OprtgDays
	 * 
	 * @param asOprtgDays String
	 */
	public void setOprtgDays(String asOprtgDays)
	{
		csOprtgDays = asOprtgDays;
	}
	/**
	 * This method sets the value of OprtgHrs
	 * 
	 * @param asOprtgHrs String
	 */
	public void setOprtgHrs(String asOprtgHrs)
	{
		csOprtgHrs = asOprtgHrs;
	}
	/**
	 * This method sets the value of PhysOfcLoc.
	 * 
	 * @param aiPhysOfcLoc   String 
	 */
	public final void setPhysOfcLoc(String asPhysOfcLoc)
	{
		csPhysOfcLoc = asPhysOfcLoc;
	}
	/**
	 * This method sets the value of RegnlOfcId.
	 * 
	 * @param aiRegnlOfcId   int 
	 */
	public final void setRegnlOfcId(int aiRegnlOfcId)
	{
		ciRegnlOfcId = aiRegnlOfcId;
	}
	/**
	 * This method sets the value of TacAcctNo.
	 * 
	 * @param asTacAcctNo   String 
	 */
	public final void setTacAcctNo(String asTacAcctNo)
	{
		csTacAcctNo = asTacAcctNo;
	}
	/**
	 * This method sets the value of TacName.
	 * 
	 * @param asTacName   String 
	 */
	public final void setTacName(String asTacName)
	{
		csTacName = asTacName;
	}
	/**
	 * This method sets the value of TacPhoneNo.
	 * 
	 * @param asTacPhoneNo   String 
	 */
	public final void setTacPhoneNo(String asTacPhoneNo)
	{
		csTacPhoneNo = asTacPhoneNo;
	}
	/**
	 * This method sets the value of TXDOTCntyNo.
	 * 
	 * @param aiTXDOTCntyNo   int  
	 */
	public final void setTXDOTCntyNo(int aiTXDOTCntyNo)
	{
		ciTXDOTCntyNo = aiTXDOTCntyNo;
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
		return getOfcIssuanceNo()
			- ((OfficeIdsData) aaObject).getOfcIssuanceNo();
	}
}
