package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.AcctCdConstant;

/*
 * DisabledPlacardData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	07/26/2009	add isPermanent(), resetAcctItmCdForHB3095()
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add Displayable, getAttributes()
 * 							defect 10191 Ver Defect_POS_G         
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * DisabledPlacardData.
 *
 * @version	Defect_POS_G  	10/16/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */
public class DisabledPlacardData implements Serializable, Comparable
// defect 10191 
, Displayable
// end defect 10191
{
	private int ciCustIdntyNo;
	private int ciCompleteIndi;
	private int ciDeleteIndi;
	private int ciDelReasnCd;
	private int ciDsabldPlcrdIdntyNo;
	private int ciRTSEffDate;
	private int ciRTSExpMo;
	private int ciRTSExpYr;
	private int ciTransTypeCd;
	private int ciVoidedIndi;
	private String csAcctItmCd;
	private String csInvItmCd;
	private String csInvItmNo;

	static final long serialVersionUID = -4508375475212227018L;

	/**
	 * DisabledPlacardData.java Constructor 
	 */
	public DisabledPlacardData()
	{
	}

	/**
	 * DisabledPlacardData.java Constructor using 
	 *   DisabledPlacardCustomerData
	 * 
	 * @param aaData
	 */
	public DisabledPlacardData(DisabledPlacardCustomerData aaDPCustData)
	{
		csInvItmCd = aaDPCustData.getItmCd();
		csAcctItmCd = aaDPCustData.getAcctItmCd();
		ciRTSEffDate = aaDPCustData.getRTSEffDate();
		ciRTSExpMo = aaDPCustData.getRTSExpMo();
		ciRTSExpYr = aaDPCustData.getRTSExpYr();
		ciCustIdntyNo = aaDPCustData.getCustIdntyNo();
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
		DisabledPlacardData laDsabldPlcrdData =
			(DisabledPlacardData) aaObject;

		// First compare Exp Months  
		int liReturn =
			getExpMonths() - laDsabldPlcrdData.getExpMonths();

		// Compare InvItmNo w/in Exp Months 
		if (liReturn == 0)
		{
			liReturn =
				csInvItmNo.compareTo(laDsabldPlcrdData.getInvItmNo());
		}
		return liReturn;
	}

	/**
	 * Get Object field attributes
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		Map lhmHash = new java.util.HashMap();
		
		Field[] larrFields = this.getClass().getDeclaredFields();
		
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Get value of csAcctItmCd
	 * 
	 * @return
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Get value of ciCustIdntyNo
	 * 
	 * @return int
	 */
	public int getCustIdntyNo()
	{
		return ciCustIdntyNo;
	}

	/**
	 * Get value of ciCompleteIndi
	 * 
	 * @return int
	 */
	public int getCompleteIndi()
	{
		return ciCompleteIndi;
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
	 * Get value of ciDelReasnCd
	 * 
	 * @return int
	 */
	public int getDelReasnCd()
	{
		return ciDelReasnCd;
	}

	/**
	 * Get value of ciDsabldPlcrdIdntyNo
	 * 
	 * @return int
	 */
	public int getDsabldPlcrdIdntyNo()
	{
		return ciDsabldPlcrdIdntyNo;
	}

	/**
	 * Get value of csInvItmCd
	 * 
	 * @return String
	 */
	public String getInvItmCd()
	{
		return csInvItmCd;
	}

	/**
	 * 
	 * Return number of months for RTSExpMo/Yr for comparison
	 * 
	 * @return int 
	 */
	public int getExpMonths()
	{
		return ciRTSExpYr * 12 + ciRTSExpMo;
	}

	/**
	 * Get value of csInvItmNo
	 * 
	 * @return String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Get value of ciRTSExpMo
	 * 
	 * @return int
	 */
	public int getRTSExpMo()
	{
		return ciRTSExpMo;
	}

	/**
	 * Get value of ciRTSExpYr
	 * 
	 * @return int
	 */
	public int getRTSExpYr()
	{
		return ciRTSExpYr;
	}

	/**
	 * Get value of ciVoidedIndi
	 * 
	 * @return int
	 */
	public int getVoidedIndi()
	{
		return ciVoidedIndi;
	}

	/**
	 * 
	 * Return boolean to denote that record is available for processing
	 * 
	 * @return boolean 
	 */
	public boolean isActive()
	{
		return !isDeleted() && !isExpired();
	}

	/**
	 * Return boolean to denote that record has been logically
	 *  deleted.
	 * 
	 * @return boolean 
	 */
	public boolean isDeleted()
	{
		return ciDeleteIndi == 1;
	}

	/**
	 * Return boolean to denote that record is expired.
	 * 
	 * @return boolean 
	 */
	public boolean isExpired()
	{
		RTSDate laCurrentDate = new RTSDate();
		int liCurrent =
			laCurrentDate.getYear() * 100 + laCurrentDate.getMonth();
		int liRecord = ciRTSExpYr * 100 + ciRTSExpMo;
		return liCurrent > liRecord;
	}
	/**
	 * Return boolean to denote that record is expired.
	 * 
	 * @return boolean 
	 */
	public boolean isInRenewalWindow()
	{
		return CommonValidations.isInRenewalWindow(ciRTSExpMo, ciRTSExpYr);
	}

	/**
	 * Return boolean to denote that record is for Permanent Disability
	 * 
	 * @return boolean
	 */
	public boolean isPermanent()
	{
		return csAcctItmCd.equals(AcctCdConstant.BPM)
			|| csAcctItmCd.equals(AcctCdConstant.RPNM)
			|| csAcctItmCd.equals(AcctCdConstant.PDC);
	}

	/** 
	 * 
	 * After HB3095, AcctItmCd should be only PDC/TDC 
	 * 
	 *
	 */

	public void resetAcctItmCdForHB3095()
	{
		if (isPermanent())
		{
			if (!csAcctItmCd.equals(AcctCdConstant.PDC))
			{
				csAcctItmCd = AcctCdConstant.PDC;
			}
		}
		else
		{
			if (!csAcctItmCd.equals(AcctCdConstant.TDC))
			{
				csAcctItmCd = AcctCdConstant.TDC;
			}
		}
	}

	/**
	 * Set value of csAcctItmCd
	 * 
	 * @param asAcctItmCd
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * Set value of ciCustIdntyNo
	 * 
	 * @param aiCustIdntyNo
	 */
	public void setCustIdntyNo(int aiCustIdntyNo)
	{
		ciCustIdntyNo = aiCustIdntyNo;
	}

	/**
	 * Set value of ciCompleteIndi  
	 * 
	 * @param aiCompleteIndi
	 */
	public void setCompleteIndi(int aiCompleteIndi)
	{
		ciCompleteIndi = aiCompleteIndi;
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
	 * Set value of ciDelReasnCd 
	 * 
	 * @param aiDelReasnCd
	 */
	public void setDelReasnCd(int aiDelReasnCd)
	{
		ciDelReasnCd = aiDelReasnCd;
	}

	/**
	 * Set value of ciDsabldPlcrdIdntyNo 
	 * 
	 * @param aiDsabldPlcrdIdntyNo
	 */
	public void setDsabldPlcrdIdntyNo(int aiDsabldPlcrdIdntyNo)
	{
		ciDsabldPlcrdIdntyNo = aiDsabldPlcrdIdntyNo;
	}

	/**
	 * Set value of csInvItmCd 
	 * 
	 * @param asInvItmCd
	 */
	public void setInvItmCd(String asInvItmCd)
	{
		csInvItmCd = asInvItmCd;
	}

	/**
	 * Set value of csInvItmNo 
	 * 
	 * @param asInvItmNo
	 */
	public void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}

	/**
	 * Set value of ciRTSEffDate 
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Set value of ciRTSExpMo 
	 * 
	 * @param aiRTSExpMo
	 */
	public void setRTSExpMo(int aiRTSExpMo)
	{
		ciRTSExpMo = aiRTSExpMo;
	}

	/**
	 * Set value of ciRTSExpYr 
	 * 
	 * @param aiRTSExpYr
	 */
	public void setRTSExpYr(int aiRTSExpYr)
	{
		ciRTSExpYr = aiRTSExpYr;
	}

	/**
	 * Set value of ciVoidedIndi 
	 * 
	 * @param aiVoidedIndi
	 */
	public void setVoidedIndi(int aiVoidedIndi)
	{
		ciVoidedIndi = aiVoidedIndi;
	}

	/**
	 * Set value of ciTransTypeCd 
	 * 
	 * @return aiTransTypeCd
	 */
	public int getTransTypeCd()
	{
		return ciTransTypeCd;
	}

	/**
	 * Set value of ciTransTypeCd
	 * 
	 * @param aiTransTypeCd
	 */
	public void setTransTypeCd(int aiTransTypeCd)
	{
		ciTransTypeCd = aiTransTypeCd;
	}

}
