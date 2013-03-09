package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 * PlateTypeData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created;
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/07/2007	Add compareTo()
 * 							defect 9085 Ver Special Plates   
 * K Harrell	02/12/2007	add getLocAuthCd()
 * 							defect 9085 Ver Special Plates
 * B Hargrove	07/15/2008	Add new field and getter\setter for 
 * 							Vendor Plate Indi.
 * 							add ciVendorPltIndi, getVendorPltIndi(),
 * 							setVendorPltIndi()
 * 							defect 9689 Ver MyPlates_POS 
 * K Harrell	12/20/2009	add isVendorPlt()
 * 							defect 10295 Defect_POS_H
 * K Harrell	12/28/2009	add isROPBaseRegPltCd()
 * 							defect 10293 Defect_POS_H
 * K Harrell	02/26/2010	add ciDropFirstCharIndi, ciVendorElgbleIndi,  
 * 							 get/set methods.
 * 							defect 10366 Ver POS_640 
 * K Harrell	04/10/2010	add isAnnualPlt()
 * 							defect 10441 Ver POS_640 
 * K Harrell	07/12/2010	add isMultiYrOfclPlt() 
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	12/08/2010	add csReplAcctItmCd, get/set methods 
 *							defect 10695 Ver 6.7.0 
 * K Harrell	04/22/2011	add isSpclPlt()
 * 							defect 10678 Ver 6.7.1 
 * K Harrell	08/22/2011	add csPltSizeCd, get/set methods 
 * 							add SMALL_PLTSIZECD
 * 							defect 10804 Ver 6.8.1 
 * K Harrell	10/31/2011	add ciPLPDVIndi, get/is/set methods 
 * 							defect 11061 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * PlateTypeData
 *
 * @version	6.9.0 			10/31/2011
 * @author	Kathy Harrell
 * @since					01/31/2007 13:12:00
 */
public class PlateTypeData implements Serializable
{

	// Dollar
	protected Dollar caAddlSetApplFee;
	protected Dollar caAddlSetRenwlFee;
	protected Dollar caFirstSetApplFee;
	protected Dollar caFirstSetRenwlFee;
	protected Dollar caPLPFee;
	protected Dollar caRemakeFee;
	protected Dollar caReplFee;

	// int
	protected int ciAnnualPltIndi;
	protected int ciDropFirstCharIndi;
	protected int ciDuplsAllowdCd;
	protected int ciMandPltReplAge;
	protected int ciMaxByteCount;
	protected int ciMfgProcsCd;
	protected int ciOptPltReplAge;
	protected int ciPltSetImprtnceCd;
	protected int ciPltSurchargeIndi;
	protected int ciRegRenwlCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	protected int ciSpclPrortnIncrmnt;
	protected int ciUserPltNoIndi;
	protected int ciVendorElgbleIndi;
	protected int ciVendorPltIndi;
	
	// defect 11061
	protected int ciPLPDVIndi;
	// end defect 11061 

	// String
	protected String csBaseRegPltCd;
	protected String csDispPltGrpId;
	protected String csISAAllowdCd;
	protected String csLimitedPltGrpId;
	protected String csLocCntyAuthCd;
	protected String csLocHQAuthCd;
	protected String csLocInetAuthCd;
	protected String csLocRegionAuthCd;
	protected String csNeedsProgramCd;
	protected String csPLPAcctItmCd;
	protected String csPltOwnrshpCd;
	// defect 10804 
	protected String csPltSizeCd; 
	// end defect 10804 
	protected String csRegPltCd;
	protected String csRegPltCdDesc;
	protected String csRegPltDesign;
	protected String csRenwlRtrnAddrCd;
	protected String csShpngAddrCd;
	protected String csSpclPltType;
	protected String csTrnsfrCd;

	//defect 10695 
	protected String csReplAcctItmCd;
	// end defect 10695 
	
	// defect 10804 
	private static final String SMALL_PLTSIZECD = "S";
	// end defect 10804

	static final long serialVersionUID = -2013045050832037954L;

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
		return getRegPltCdDesc().compareTo(
			((PlateTypeData) aaObject).getRegPltCdDesc());
	}
	/**
	 * Return AddlSetApplFee
	 * 
	 * @return Dollar
	 */
	public Dollar getAddlSetApplFee()
	{
		return caAddlSetApplFee;
	}

	/**
	 * Return value of caAddlSetRenwlFee
	 * 
	 * @return Dollar
	 */
	public Dollar getAddlSetRenwlFee()
	{
		return caAddlSetRenwlFee;
	}

	/**
	 * Return value of ciAnnualPltIndi
	 * 
	 * @return int
	 */
	public int getAnnualPltIndi()
	{
		return ciAnnualPltIndi;
	}

	/**
	 * Return value of csBaseRegPltCd
	 * 
	 * @return String
	 */
	public String getBaseRegPltCd()
	{
		return csBaseRegPltCd;
	}

	/**
	 * Return value of csDispPltGrpId 
	 * 
	 * @return String
	 */
	public String getDispPltGrpId()
	{
		return csDispPltGrpId;
	}

	/**
	 * Gets value of ciDropFirstCharIndi
	 * 
	 * @return int
	 */
	public int getDropFirstCharIndi()
	{
		return ciDropFirstCharIndi;
	}

	/**
	 * Return value of ciDuplsAllowdCd
	 * 
	 * @return  int
	 */
	public int getDuplsAllowdCd()
	{
		return ciDuplsAllowdCd;
	}

	/**
	 * Return value of caFirstSetApplFee 
	 * 
	 * @return Dollar
	 */
	public Dollar getFirstSetApplFee()
	{
		return caFirstSetApplFee;
	}

	/**
	 * Return value of caFirstSetRenwlFee 
	 * 
	 * @return Dollar
	 */
	public Dollar getFirstSetRenwlFee()
	{
		return caFirstSetRenwlFee;
	}

	/**
	 * Return value of csISAAllowdCd
	 * 
	 * @return String
	 */
	public String getISAAllowdCd()
	{
		return csISAAllowdCd;
	}

	/**
	 * Return value of csLimitedPltGrpId
	 * 
	 * @return String
	 */
	public String getLimitedPltGrpId()
	{
		return csLimitedPltGrpId;
	}

	/**
	 * Return value of Authorization for the OfcissuanceCd
	 * 
	 * @return String
	 */
	public String getLocAuthCd(int aiOfcIssuanceCd)
	{
		String lsAuthCd = "";

		if (aiOfcIssuanceCd == 1 && csLocHQAuthCd != null)
		{
			lsAuthCd = csLocHQAuthCd;
		}
		else if (aiOfcIssuanceCd == 2 && csLocRegionAuthCd != null)
		{

			lsAuthCd = csLocRegionAuthCd;
		}
		else if (aiOfcIssuanceCd == 3 && csLocCntyAuthCd != null)
		{
			lsAuthCd = csLocCntyAuthCd;
		}
		else if (aiOfcIssuanceCd == 5 && csLocInetAuthCd != null)
		{
			lsAuthCd = csLocInetAuthCd;
		}
		return lsAuthCd;
	}

	/**
	 * Return value of csLocCntyAuthCd
	 * 
	 * @return String
	 */
	public String getLocCntyAuthCd()
	{
		return csLocCntyAuthCd;
	}

	/**
	 * Return value of csLocHQAuthCd
	 * 
	 * @return String
	 */
	public String getLocHQAuthCd()
	{
		return csLocHQAuthCd;
	}

	/**
	 * Return value of csLocInetAuthCd
	 * 
	 * @return String
	 */
	public String getLocInetAuthCd()
	{
		return csLocInetAuthCd;
	}

	/**
	 * Return value of csLocRegionAuthCd
	 * 
	 * @return String
	 */
	public String getLocRegionAuthCd()
	{
		return csLocRegionAuthCd;
	}

	/**
	 * Return value of ciMandPltReplAge
	 * 
	 * @return  int
	 */
	public int getMandPltReplAge()
	{
		return ciMandPltReplAge;
	}

	/**
	 * Return value of ciMaxByteCount
	 * 
	 * @return  int
	 */
	public int getMaxByteCount()
	{
		return ciMaxByteCount;
	}

	/**
	 * Return value of ciMfgProcsCd
	 * 
	 * @return  int
	 */
	public int getMfgProcsCd()
	{
		return ciMfgProcsCd;
	}

	/**
	 * Return value of csNeedsProgramCd
	 * 
	 * @return String
	 */
	public String getNeedsProgramCd()
	{
		return csNeedsProgramCd;
	}

	/**
	 * Return value of ciOptPltReplAge
	 * 
	 * @return  int
	 */
	public int getOptPltReplAge()
	{
		return ciOptPltReplAge;
	}

	/**
	 * Return value of csPLPAcctItmCd
	 * 
	 * @return String
	 */
	public String getPLPAcctItmCd()
	{
		return csPLPAcctItmCd;
	}
	/**
	 * @return the ciPLPDVIndi
	 */
	public int getPLPDVIndi()
	{
		return ciPLPDVIndi;
	}
	/**
	 * Return value of caPLPFee 
	 * 
	 * @return Dollar
	 */
	public Dollar getPLPFee()
	{
		return caPLPFee;
	}

	/**
	 * Return value of csPltOwnrshpCd
	 * 
	 * @return String
	 */
	public String getPltOwnrshpCd()
	{
		return csPltOwnrshpCd;
	}

	/**
	 * Return the value of csPltSizeCd 
	 * 
	 * @return csPltSizeCd
	 */
	public String getPltSizeCd()
	{
		return csPltSizeCd;
	}
	
	/**
	 * Return value of ciPltSetImprtnceCd
	 * 
	 * @return  int
	 */
	public int getPltSetImprtnceCd()
	{
		return ciPltSetImprtnceCd;
	}

	/**
	 * Return value of ciPltSurchargeIndi
	 * 
	 * @return  int
	 */
	public int getPltSurchargeIndi()
	{
		return ciPltSurchargeIndi;
	}

	/**
	 * Return value of csRegPltCd
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Gets value of csRegPltCdDesc
	 * 
	 * @return String
	 */
	public String getRegPltCdDesc()
	{
		return csRegPltCdDesc;
	}

	/**
	 * Return value of csRegPltDesign
	 * 
	 * @return String
	 */
	public String getRegPltDesign()
	{
		return csRegPltDesign;
	}

	/**
	 * Return value of ciRegRenwlCd
	 * 
	 * @return  int
	 */
	public int getRegRenwlCd()
	{
		return ciRegRenwlCd;
	}

	/**
	 * Return value of caRemakeFee
	 * 
	 * @return Dollar
	 */
	public Dollar getRemakeFee()
	{
		return caRemakeFee;
	}

	/**
	 * Return value of getRenwlRtrnAddrCd
	 * 
	 * @return String
	 */
	public String getRenwlRtrnAddrCd()
	{
		return csRenwlRtrnAddrCd;
	}

	/**
	 * Get value of csReplAcctItmCd
	 * 
	 * @return String
	 */
	public String getReplAcctItmCd()
	{
		return csReplAcctItmCd;
	}

	/**
	 * Return value of caReplFee 
	 * 
	 * @return Dollar
	 */
	public Dollar getReplFee()
	{
		return caReplFee;
	}

	/**
	 * Return value of ciRTSEffDate
	 * 
	 * @return  int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Return value of ciRTSEffEndDate
	 * 
	 * @return  int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/**
	 * Return value of csShpngAddrCd
	 * 
	 * @return String
	 */
	public String getShpngAddrCd()
	{
		return csShpngAddrCd;
	}

	/**
	 * Return value of csSpclPltType
	 * 
	 * @return String
	 */
	public String getSpclPltType()
	{
		return csSpclPltType;
	}

	/**
	 * Return value of ciSpclPrortnIncrmnt
	 * 
	 * @return  int
	 */
	public int getSpclPrortnIncrmnt()
	{
		return ciSpclPrortnIncrmnt;
	}

	/**
	 * Return value of csTrnsfrCd
	 * 
	 * @return String
	 */
	public String getTrnsfrCd()
	{
		return csTrnsfrCd;
	}

	/**
	 * Return value of ciUserPltNoIndi
	 * 
	 * @return  int
	 */
	public int getUserPltNoIndi()
	{
		return ciUserPltNoIndi;
	}

	/**
	 * Gets value of ciVendorElgbleIndi
	 * 
	 * @return int
	 */
	public int getVendorElgbleIndi()
	{
		return ciVendorElgbleIndi;
	}

	/**
	 * Return value of ciVendorPltIndi
	 * 
	 * @return int
	 */
	public int getVendorPltIndi()
	{
		return ciVendorPltIndi;
	}

	/** 
	 * Return boolean to denote if Annual Plate 
	 * 
	 * @return boolean 
	 */
	public boolean isAnnualPlt()
	{
		return ciAnnualPltIndi == 1;
	}

	/** 
	 * Is Multi-Year Official Plate 
	 * 
	 * @return boolean 
	 */
	public boolean isMultiYrOfclPlt()
	{
		return csPltOwnrshpCd != null
			&& csPltOwnrshpCd.equals(SpecialPlatesConstant.ENTITLED_OWNER)
			&& !isAnnualPlt();
	}
	
	/**
	 * @return boolean to denote if ciPLPDVIndi ==1 
	 */
	public boolean isPLPDV()
	{
		return ciPLPDVIndi ==1;
	}
	
	/** 
	 * Return boolean to denote if ROP BaseRegPltCd 
	 * 
	 * @return boolean 
	 */
	public boolean isROPBaseRegPltCd()
	{
		return csBaseRegPltCd != null
			&& csBaseRegPltCd.equals(
				SpecialPlatesConstant.RADIO_OPERATOR_PLATE);
	}
	
	/** 
	 * Return boolean to denote if Special Plate 
	 * 
	 * @return boolean 
	 */
	public boolean isSpclPlt()
	{
		return csPltOwnrshpCd != null
			&& !csPltOwnrshpCd.equals(SpecialPlatesConstant.VEHICLE);
	}

	/** 
	 * Return boolean to denote if is Small Plate 
	 */
	public boolean isSmallPlt()
	{
		return !(UtilityMethods.isEmpty(csPltSizeCd)) 
			&&	csPltSizeCd.equals(SMALL_PLTSIZECD); 
	}
	
	/** 
	 * Return boolean to denote if is Vendor Plate 
	 * 
	 */
	public boolean isVendorPlt()
	{
		return ciVendorPltIndi == 1;
	}

	/**
	 * Sets value of caAddlSetApplFee
	 * 
	 * @param aaAddlSetApplFee
	 */
	public void setAddlSetApplFee(Dollar aaAddlSetApplFee)
	{
		caAddlSetApplFee = aaAddlSetApplFee;
	}

	/**
	 * Sets value of caAddlSetRenwlFee
	 * 
	 * @param dollar
	 */
	public void setAddlSetRenwlFee(Dollar aaAddlSetRenwlFee)
	{
		caAddlSetRenwlFee = aaAddlSetRenwlFee;
	}

	/**
	 * Sets value of ciAnnualPltIndi
	 * 
	 * @param aiAnnualPltIndi
	 */
	public void setAnnualPltIndi(int aiAnnualPltIndi)
	{
		ciAnnualPltIndi = aiAnnualPltIndi;
	}

	/**
	 * Sets value of csBaseRegPltCd
	 * 
	 * @param asBaseRegPltCd
	 */
	public void setBaseRegPltCd(String asBaseRegPltCd)
	{
		csBaseRegPltCd = asBaseRegPltCd;
	}

	/**
	 * Sets value of csDispPltGrpId
	 * 
	 * @param asDispPltGrpId
	 */
	public void setDispPltGrpId(String asDispPltGrpId)
	{
		csDispPltGrpId = asDispPltGrpId;
	}

	/**
	 * Sets value of ciDropFirstCharIndi
	 * 
	 * @param aiDropFirstCharIndi
	 */
	public void setDropFirstCharIndi(int aiDropFirstCharIndi)
	{
		ciDropFirstCharIndi = aiDropFirstCharIndi;
	}

	/**
	 * Sets value of ciDuplsAllowdCd
	 * 
	 * @param aiDuplsAllowdCd
	 */
	public void setDuplsAllowdCd(int aiDuplsAllowdCd)
	{
		ciDuplsAllowdCd = aiDuplsAllowdCd;
	}

	/**
	 * Sets value of caFirstSetApplFee
	 * 
	 * @param aaFirstSetApplFee
	 */
	public void setFirstSetApplFee(Dollar aaFirstSetApplFee)
	{
		caFirstSetApplFee = aaFirstSetApplFee;
	}

	/**
	 * Sets value of caFirstSetRenwlFee
	 * 
	 * @param aaFirstSetRenwlFee
	 */
	public void setFirstSetRenwlFee(Dollar aaFirstSetRenwlFee)
	{
		caFirstSetRenwlFee = aaFirstSetRenwlFee;
	}

	/**
	 * Sets value of csISAAllowdCd
	 * 
	 * @param asISAAllowdCd
	 */
	public void setISAAllowdCd(String asISAAllowdCd)
	{
		csISAAllowdCd = asISAAllowdCd;
	}

	/**
	 * Sets value of csLimitedPltGrpId
	 * 
	 * @param asLimitedPltGrpId
	 */
	public void setLimitedPltGrpId(String asLimitedPltGrpId)
	{
		csLimitedPltGrpId = asLimitedPltGrpId;
	}

	/**
	 * Sets value of csLocCntyAuthCd
	 * 
	 * @param asLocCntyAuthCd
	 */
	public void setLocCntyAuthCd(String asLocCntyAuthCd)
	{
		csLocCntyAuthCd = asLocCntyAuthCd;
	}

	/**
	 * Sets value of csLocHQAuthCd
	 * 
	 * @param asLocHQAuthCd
	 */
	public void setLocHQAuthCd(String asLocHQAuthCd)
	{
		csLocHQAuthCd = asLocHQAuthCd;
	}

	/**
	 * Sets value of csLocInetAuthCd
	 * 
	 * @param asLocInetAuthCd
	 */
	public void setLocInetAuthCd(String asLocInetAuthCd)
	{
		csLocInetAuthCd = asLocInetAuthCd;
	}

	/**
	 * Sets value of csLocRegionAuthCd
	 * 
	 * @param asLocRegionAuthCd
	 */
	public void setLocRegionAuthCd(String asLocRegionAuthCd)
	{
		csLocRegionAuthCd = asLocRegionAuthCd;
	}

	/**
	 * Sets value of ciMandPltReplAge
	 * 
	 * @param aiMandPltReplAge
	 */
	public void setMandPltReplAge(int aiMandPltReplAge)
	{
		ciMandPltReplAge = aiMandPltReplAge;
	}

	/**
	 * Sets value of ciMaxByteCount
	 * 
	 * @param aiMaxByteCount
	 */
	public void setMaxByteCount(int aiMaxByteCount)
	{
		ciMaxByteCount = aiMaxByteCount;
	}

	/**
	 * Sets value of ciMfgProcsCd 
	 * 
	 * @param aiMfgProcsCd
	 */
	public void setMfgProcsCd(int aiMfgProcsCd)
	{
		ciMfgProcsCd = aiMfgProcsCd;
	}

	/**
	 * Sets value of csNeedsProgramCd
	 * 
	 * @param asNeedsProgramCd
	 */
	public void setNeedsProgramCd(String asNeedsProgramCd)
	{
		csNeedsProgramCd = asNeedsProgramCd;
	}

	/**
	 * Sets value of ciOptPltReplAge
	 * 
	 * @param aiOptPltReplAge
	 */
	public void setOptPltReplAge(int aiOptPltReplAge)
	{
		ciOptPltReplAge = aiOptPltReplAge;
	}

	/**
	 * Sets value of csPLPAcctItmCd
	 * 
	 * @param asPLPAcctItmCd
	 */
	public void setPLPAcctItmCd(String asPLPAcctItmCd)
	{
		csPLPAcctItmCd = asPLPAcctItmCd;
	}

	/**
	 * @param ciPLPDVIndi the ciPLPDVIndi to set
	 */
	public void setPLPDVIndi(int aiPLPDVIndi)
	{
		ciPLPDVIndi =aiPLPDVIndi;
	}

	/**
	 * Sets value of caPLPFee
	 * 
	 * @param aaPLPFee
	 */
	public void setPLPFee(Dollar aaPLPFee)
	{
		caPLPFee = aaPLPFee;
	}

	/**
	 * Sets value of csPltOwnrshpCd 
	 * 
	 * @param asPltOwnrshpCd
	 */
	public void setPltOwnrshpCd(String asPltOwnrshpCd)
	{
		csPltOwnrshpCd = asPltOwnrshpCd;
	}

	/**
	 * Sets value of ciPltSetImprtnceCd
	 * 
	 * @param aiPltSetImprtnceCd
	 */
	public void setPltSetImprtnceCd(int aiPltSetImprtnceCd)
	{
		ciPltSetImprtnceCd = aiPltSetImprtnceCd;
	}
	
	/**
	 * Sets the value of csPltSizeCd
	 *  
	 * @param asPltSizeCd 
	 */
	public void setPltSizeCd(String asPltSizeCd)
	{
		csPltSizeCd = asPltSizeCd;
	}
	
	/**
	 * Sets value of ciPltSurchargeIndi
	 * 
	 * @param aiPltSurchargeIndi
	 */
	public void setPltSurchargeIndi(int aiPltSurchargeIndi)
	{
		ciPltSurchargeIndi = aiPltSurchargeIndi;
	}

	/**
	 * Sets value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Sets value of csRegPltCdDesc
	 * 
	 * @param asRegPltCdDesc
	 */
	public void setRegPltCdDesc(String asRegPltCdDesc)
	{
		csRegPltCdDesc = asRegPltCdDesc;
	}

	/**
	 * Sets value of csRegPltDesign
	 * 
	 * @param asRegPltDesign
	 */
	public void setRegPltDesign(String asRegPltDesign)
	{
		csRegPltDesign = asRegPltDesign;
	}

	/**
	 * Sets value of ciRegRenwlCd
	 * 
	 * @param ciRegRenwlCd
	 */
	public void setRegRenwlCd(int aiRegRenwlCd)
	{
		ciRegRenwlCd = aiRegRenwlCd;
	}

	/**
	 * Sets value of caRemakeFee
	 * 
	 * @param aaRemakeFee
	 */
	public void setRemakeFee(Dollar aaRemakeFee)
	{
		caRemakeFee = aaRemakeFee;
	}

	/**
	 * Sets value of csRenwlRtrnAddrCd
	 * 
	 * @param asRenwlRtrnAddrCd
	 */
	public void setRenwlRtrnAddrCd(String asRenwlRtrnAddrCd)
	{
		csRenwlRtrnAddrCd = asRenwlRtrnAddrCd;
	}

	/**
	 * Set value of csReplAcctItmCd
	 * 
	 * @param asReplAcctItmCd
	 */
	public void setReplAcctItmCd(String asReplAcctItmCd)
	{
		csReplAcctItmCd = asReplAcctItmCd;
	}

	/**
	 * Sets value of caReplFee
	 * 
	 * @param aaReplFee
	 */
	public void setReplFee(Dollar aaReplFee)
	{
		caReplFee = aaReplFee;
	}

	/**
	 * Sets value of ciRTSEffDate 
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Sets value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}

	/**
	 * Sets value of csShpngAddrCd
	 * 
	 * @param asShpngAddrCd
	 */
	public void setShpngAddrCd(String asShpngAddrCd)
	{
		csShpngAddrCd = asShpngAddrCd;
	}

	/**
	 * Sets value of csSpclPltType
	 * 
	 * @param asSpclPltType
	 */
	public void setSpclPltType(String asSpclPltType)
	{
		csSpclPltType = asSpclPltType;
	}

	/**
	 * Sets value of ciSpclPrortnIncrmnt
	 * 
	 * @param aiSpclPrortnIncrmnt
	 */
	public void setSpclPrortnIncrmnt(int aiSpclPrortnIncrmnt)
	{
		ciSpclPrortnIncrmnt = aiSpclPrortnIncrmnt;
	}

	/**
	 * Sets value of csTrnsfrCd
	 * 
	 * @param asTrnsfrCd
	 */
	public void setTrnsfrCd(String asTrnsfrCd)
	{
		csTrnsfrCd = asTrnsfrCd;
	}

	/**
	 * Sets value of ciUserPltNoIndi
	 * 
	 * @param aiUserPltNoIndi
	 */
	public void setUserPltNoIndi(int aiUserPltNoIndi)
	{
		ciUserPltNoIndi = aiUserPltNoIndi;
	}

	/**
	 * Sets value of ciVendorElgbleIndi
	 * 
	 * @param aiVendorElgbleIndi
	 */
	public void setVendorElgbleIndi(int aiVendorElgbleIndi)
	{
		ciVendorElgbleIndi = aiVendorElgbleIndi;
	}

	/**
	 * Sets value of ciVendorPltIndi
	 * 
	 * @param aiVendorPltIndi
	 */
	public void setVendorPltIndi(int aiVendorPltIndi)
	{
		ciVendorPltIndi = aiVendorPltIndi;
	}
}
