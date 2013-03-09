package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * CertifiedLienholderData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/25/2009	Created 
 * 							defect 9969 Ver Defect_POS_E 
 * K Harrell	03/23/2009	modify CertifiedLienholderData(),  
 * 							 compareTo() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/10/2009	Implement NameAddressData
 * 							delete csCertifiedName1, 
 * 							 csCertifiedName2, caAddressData, 
 * 							 get/set methods.
 * 							modify getLienholderData(), 
 * 							 CertifiedLienholderData()
 * 							defect 10112 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CertifiedLienholderData
 *
 * @version	Defect_POS_F 	07/10/2009 
 * @author	Kathy Harrell
 * <br>Creation Date:		02/25/2009 
 */
public class CertifiedLienholderData
	extends NameAddressData
	implements Serializable, Comparable
{
	private int ciElienRdyIndi;
	private int ciRTSEffDate;
	private int ciRTSEffEndDate;

	private String csPermLienHldrId;

	static final long serialVersionUID = -2270192225635313208L;

	/**
	 * CertifiedLienholderData Constructor
	 */
	public CertifiedLienholderData()
	{
		super();

		// defect 10112 
		csPermLienHldrId = CommonConstant.STR_SPACE_EMPTY;
		// end defect 10112 
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
		CertifiedLienholderData laCompData =
			(CertifiedLienholderData) aaObject;

		return (getName1().compareTo(laCompData.getName1()));
	}

	/**
	 * Gets value of ciElienRdyIndi
	 * 
	 * @return int
	 */
	public int getElienRdyIndi()
	{
		return ciElienRdyIndi;
	}

	/**
	 * Get LienholdersData
	 * 
	 * @return LienholdersData
	 */
	public LienholderData getLienholderData()
	{
		LienholderData laData = new LienholderData();
		laData.setName1(csName1);
		laData.setName2(csName2);
		laData.setAddressData(
			(AddressData) UtilityMethods.copy(caAddressData));
		return laData;
	}

	/**
	 * Gets value of csPermLienHldrId
	 * 
	 * @return String
	 */
	public String getPermLienHldrId()
	{
		return csPermLienHldrId == null
			? CommonConstant.STR_SPACE_EMPTY
			: csPermLienHldrId.trim();
	}

	/**
	 * Gets value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Gets value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/** 
	 * Return boolean to reflect if ciElienRdyIndi == 1
	 * 
	 */
	public boolean isElienRdy()
	{
		return ciElienRdyIndi == 1;
	}

	/**
	 * Sets value of ciElienRdyIndi
	 * 
	 * @param aiElienRdyIndi
	 */
	public void setElienRdyIndi(int aiElienRdyIndi)
	{
		ciElienRdyIndi = aiElienRdyIndi;
	}

	/**
	 * Sets value of csPermLienHldrId
	 * 
	 * @param asPermLienHldrId
	 */
	public void setPermLienHldrId(String asPermLienHldrId)
	{
		csPermLienHldrId = asPermLienHldrId;
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
}
