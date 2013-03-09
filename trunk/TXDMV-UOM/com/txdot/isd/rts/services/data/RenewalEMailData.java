package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * RenewalEMailData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/12/2010	Created
 * 							defect 10514 Ver 6.5.0
 * B Brown		08/03/2010	Add caProcsComplTimestmp, 
 * 							getters and setters
 * 							Add compareTo() for sorting.
 * 							defect 10512 Ver 6.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * RenewalEMailData
 *
 * @version	6.5.0			08/03/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		07/12/13:46:17 
 */
public class RenewalEMailData implements Serializable, Comparable
{
	private String csRecpntEMail;
	private int ciERenwlRteIndi;
	private int ciExmptIndi;
	private int ciRegClassCd;
	private int ciRegExpMo;
	private int ciRegRenwlExpYr;
	private int ciResComptCntyNo;
	private int ciVehModlYr;

	private RTSDate caBatchDate;
	private RTSDate caProcsTimestmp;

	private String csDocNo;
	private String csOrgNo;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csRegStkrCd;
	private String csVehBdyType;
	private String csVehClassCd;
	private String csVehMk;
	private String csVehModl;
	private String csVin;
	private String csWindwAddrName1;
	private String csWindwAddrName2;

	static final long serialVersionUID = -1713896114238902286L;

	/**
	 * RenewalEMailData.java Constructor
	 * 
	 */
	public RenewalEMailData()
	{
		super();
	}

	/**
	 * Return value of BatchDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchDate()
	{
		return caBatchDate;
	}

	/**
	 * Get value of csDocNo 
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Get value of ciERenwlRteIndi 
	 * 
	 * @return int
	 */
	public int getERenwlRteIndi()
	{
		return ciERenwlRteIndi;
	}

	/**
	 * Get value of ciExmptIndi 
	 * 
	 * @return int
	 */
	public int getExmptIndi()
	{
		return ciExmptIndi;
	}

	/**
	 * Get value of csOrgNo 
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Get value of csRecpntEMail 
	 * 
	 * @return String
	 */
	public String getRecpntEMail()
	{
		return csRecpntEMail;
	}

	/**
	 * Get value of ciRegClassCd 
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}

	/**
	 * Get value of ciRegExpMo 
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Get value of csRegPltCd 
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Get value of csRegPltNo 
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Get value of ciRegRenwlExpYr 
	 * 
	 * @return int
	 */
	public int getRegRenwlExpYr()
	{
		return ciRegRenwlExpYr;
	}

	/**
	 * Get value of csRegStkrCd
	 * 
	 * @return String
	 */
	public String getRegStkrCd()
	{
		return csRegStkrCd;
	}

	/**
	 * Get value of ciResComptCntyNo 
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Get value of csVehBdyType 
	 * 
	 * @return String
	 */
	public String getVehBdyType()
	{
		return csVehBdyType;
	}

	/**
	 * Get value of csVehClassCd 
	 * 
	 * @return String
	 */
	public String getVehClassCd()
	{
		return csVehClassCd;
	}

	/**
	 * Get value of csVehMk 
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Get value of csVehModl 
	 * 
	 * @return String
	 */
	public String getVehModl()
	{
		return csVehModl;
	}

	/**
	 * Get value of ciVehModlYr 
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Get value of csVin 
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}

	/**
	 * Get value of csWindwAddrName1 
	 * 
	 * @return String
	 */
	public String getWindwAddrName1()
	{
		return csWindwAddrName1;
	}

	/**
	 * Get value of csWindwAddrName2  
	 * 
	 * @return String
	 */
	public String getWindwAddrName2()
	{
		return csWindwAddrName2;
	}

	/**
	 * Set caBatchDate
	 * 
	 * @param aaBatchDate
	 */
	public void setBatchDate(RTSDate aaBatchDate)
	{
		caBatchDate = aaBatchDate;
	}

	/**
	 * Set value of csDocNo
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set value of ciERenwlRteIndi
	 * 
	 * @param aiERenwlRteIndi
	 */
	public void setERenwlRteIndi(int aiERenwlRteIndi)
	{
		ciERenwlRteIndi = aiERenwlRteIndi;
	}

	/**
	 * Set value of ciExmptIndi
	 * 
	 * @param aiExmptIndi
	 */
	public void setExmptIndi(int aiExmptIndi)
	{
		ciExmptIndi = aiExmptIndi;
	}

	/**
	 * Set value of csOrgNo
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Set value of csRecpntEMail
	 * 
	 * @param asRecpntEMail
	 */
	public void setRecpntEMail(String asRecpntEMail)
	{
		csRecpntEMail = asRecpntEMail;
	}

	/**
	 * Set value of ciRegClassCd
	 * 
	 * @param aiRegClassCd
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * Set value of ciRegExpMo
	 * 
	 * @param aiRegExpMo
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Set value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set value of csRegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set value of ciRegRenwlExpYr
	 * 
	 * @param aiRegRenwlExpYr
	 */
	public void setRegRenwlExpYr(int aiRegRenwlExpYr)
	{
		ciRegRenwlExpYr = aiRegRenwlExpYr;
	}

	/**
	 * Set value of csRegStkrCd
	 * 
	 * @param asRegStkrCd
	 */
	public void setRegStkrCd(String asRegStkrCd)
	{
		csRegStkrCd = asRegStkrCd;
	}

	/**
	 * Set value of ciResComptCntyNo
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Set value of csVehBdyType
	 * 
	 * @param asVehBdyType
	 */
	public void setVehBdyType(String asVehBdyType)
	{
		csVehBdyType = asVehBdyType;
	}

	/**
	 * Set value of csVehClassCd
	 * 
	 * @param asVehClassCd
	 */
	public void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}

	/**
	 * Set value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set value of csVehModl
	 * 
	 * @param asVehModl
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * Set value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Set value of csVin
	 * 
	 * @param asVin
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}

	/**
	 * Set value of csWindwAddrName1
	 * 
	 * @param asWindwAddrName1
	 */
	public void setWindwAddrName1(String asWindwAddrName1)
	{
		csWindwAddrName1 = asWindwAddrName1;
	}

	/**
	 * Set value of csWindwAddrName2
	 * 
	 * @param asWindwAddrName2
	 */
	public void setWindwAddrName2(String asWindwAddrName2)
	{
		csWindwAddrName2 = asWindwAddrName2;
	}
	/**
	 * Get value of caProcsComplTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getProcsTimestmp()
	{
		return caProcsTimestmp;
	}

	/**
	 * Set value of caProcsComplTimestmp
	 * 
	 * @param date
	 */
	public void setProcsTimestmp(RTSDate aaProcsTimestmp)
	{
		caProcsTimestmp = aaProcsTimestmp;
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
	 * @return  boolean a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 */
	public int compareTo(Object aaObject)
	{
		RenewalEMailData laData =
			(RenewalEMailData) aaObject;
		return csRecpntEMail.compareTo(laData.getRecpntEMail());
	}
}
