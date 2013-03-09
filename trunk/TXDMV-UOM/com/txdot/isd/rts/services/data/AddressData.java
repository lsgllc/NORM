package com.txdot.isd.rts.services.data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * AddressData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/01/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	10/27/2008	add MAX_NO_ADDRESS_LINES
 * 							add isUSA(), getAddressVector(), 
 * 							  getAddressVector(boolean), getCntryZpcd()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/04/2008	add getStateCntry()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	02/18/2009	add compareTo() 
 * 							defect 9893 Ver Defect_POS_D
 * K Harrell	07/03/2009  move initialization to constructor
 * 							add	getAddressStringBuffer(), 
 * 							 isPopulated(), getCntryUSAZpcd(), 
 * 							 initWhereNull(), 
 * 							modify AddressData(), getCityStateCntryZip() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/17/2009	add isCntryValidState(), isValidAddress()
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add Displayable, getAttributes()
 * 							delete MAX_NO_ADDRESS_LINES
 * 							modify getAddressVector(boolean)
 * 							defect 10191 Ver Defect_POS_G
 * K Harrell	12/29/2009	Add check for invalid MF characters
 * 							modify isValidAddress()
 * 							defect 10299 Ver Defect_POS_H       
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * AddressData
 *
 * @version	Defect_POS_H	12/29/2009
 * @author	Administrator
 * <br>Creation Date:		08/30/2001 15:01:21 
 */

public class AddressData implements java.io.Serializable, Comparable
// defect 10191
, Displayable
// end defect 10191 
{

	// defect 10191 
	//private static final int MAX_NO_ADDRESS_LINES = 3;
	// end defect 10191 

	protected String csCity;
	protected String csCntry;
	protected String csSt1;
	protected String csSt2;
	protected String csState;
	protected String csZpcd;
	protected String csZpcdp4;

	private final static long serialVersionUID = 61063917003056816L;

	/**
	 * AddressData constructor comment.
	 */
	public AddressData()
	{
		super();

		// defect 10112 
		csCity = new String();
		csCntry = new String();
		csSt1 = new String();
		csSt2 = new String();
		csState = new String();
		csZpcd = new String();
		csZpcdp4 = new String();
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
		AddressData laCompAddress = (AddressData) aaObject;

		boolean lbComp =
			(laCompAddress.getSt1().compareTo(getSt1()) == 0
				? true
				: false);
		lbComp =
			lbComp
				&& (laCompAddress.getSt2().compareTo(getSt2()) == 0
					? true
					: false);
		lbComp =
			lbComp
				&& (laCompAddress.getCity().compareTo(getCity()) == 0
					? true
					: false);
		lbComp =
			lbComp
				&& (laCompAddress.getState().compareTo(getState()) == 0
					? true
					: false);
		lbComp =
			lbComp
				&& (laCompAddress.getCntry().compareTo(getCntry()) == 0
					? true
					: false);
		lbComp =
			lbComp
				&& (laCompAddress.getZpcd().compareTo(getZpcd()) == 0
					? true
					: false);
		lbComp =
			lbComp
				&& (laCompAddress.getZpcdp4().compareTo(getZpcdp4()) == 0
					? true
					: false);
		return lbComp ? 0 : 1;
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
	 * Return String Buffer of AddressData 
	 */
	public StringBuffer getAddressStringBuffer()
	{
		StringBuffer laAddress = new StringBuffer();
		Vector lvAddr = getAddressVector();
		for (int i = 0; i < lvAddr.size(); i++)
		{
			String lsData = (String) lvAddr.elementAt(i);

			laAddress.append(
				lsData + CommonConstant.SYSTEM_LINE_SEPARATOR);
		}
		return laAddress;
	}

	/** 
	 * Return a vector of Strings of Address Data with info 
	 * correctly formatted according to isUSA(). EmptyFill. 
	 * 
	 * @return Vector 
	 */
	public Vector getAddressVector()
	{
		return getAddressVector(true);
	}

	/**
	 * Return a vector of Strings of Address Data with info 
	 * correctly formatted according to isUSA(). If EmptyFill,
	 * will fill in last element w/ " "
	 * 
	 * @return Vector 
	 */
	public Vector getAddressVector(boolean abEmptyFill)
	{
		Vector lvAddressLines = new Vector();
		if (!UtilityMethods.isEmpty(csSt1))
		{
			lvAddressLines.add(csSt1);
		}
		if (!UtilityMethods.isEmpty(csSt2))
		{
			lvAddressLines.add(csSt2);
		}
		String lsCityState = getCityStateCntryZip();
		if (!UtilityMethods.isEmpty(lsCityState))
		{
			lvAddressLines.add(lsCityState);
		}
		// defect 10191 
		//if (lvAddressLines.size() != MAX_NO_ADDRESS_LINES
		if (lvAddressLines.size()
			!= CommonConstant.MAX_NO_ADDRESS_LINES
			&& abEmptyFill)
		{
			// end defect 10191 
			lvAddressLines.add(" ");
		}
		return lvAddressLines;
	}

	/**
	 * Return value of City
	 * 
	 * @return String
	 */
	public String getCity()
	{
		return csCity;
	}

	/** 
	 * 
	 * Return Formatted City, (State/Cntry) Zpcd(+Zpcdp4)
	 * 
	 * @return String; 
	 */
	public String getCityStateCntryZip()
	{
		String lsCityState = "";
		if (!UtilityMethods.isEmpty(csCity))
		{
			// defect 10112 
			lsCityState =
				csCity
					+ ", "
					+ (isUSA() ? csState : csCntry)
					+ " "
					+ getCntryUSAZpcd();

			// end defect 10112 
		}
		return lsCityState;
	}

	/**
	 * Return value of Cntry
	 * 
	 * @return String
	 */
	public String getCntry()
	{
		return csCntry;
	}

	/**
	 * Return Complete ZipCode for either !USA or USA
	 * 
	 * @return String
	 */
	public String getCntryUSAZpcd()
	{
		String lsZpCd = csZpcd;

		if (!UtilityMethods.isEmpty(csZpcdp4))
		{
			String lsDash = isUSA() ? "-" : "";
			lsZpCd = lsZpCd + lsDash + csZpcdp4;
		}
		return lsZpCd;
	}

	/**
	 * Return 'calculated' CntryZip 
	 * 
	 * @return String 
	 */
	public String getCntryZpcd()
	{
		String lsCntryZip = csZpcd.trim();
		if (csZpcdp4 != null)
		{
			lsCntryZip = lsCntryZip + csZpcdp4.trim();
		}
		return lsCntryZip;
	}

	/**
	 * Return value of St1
	 * 
	 * @return String
	 */
	public String getSt1()
	{
		return csSt1;
	}

	/**
	 * Return value of St2
	 * 
	 * @return String
	 */
	public String getSt2()
	{
		return csSt2;
	}

	/**
	 * Return value of State
	 * 
	 * @return String
	 */
	public String getState()
	{
		return csState;
	}

	/** 
	 * Return State/Cntry as appropriate
	 * 
	 * @return String
	 */
	public String getStateCntry()
	{
		String lsStateCntry = csState;
		if (!isUSA())
		{
			lsStateCntry = csCntry;
		}
		return lsStateCntry;
	}

	/**
	 * Return value of Zpcd
	 * 
	 * @return String
	 */
	public String getZpcd()
	{
		return csZpcd;
	}

	/**
	 * Set value of Zpcdp4
	 * 
	 * @return String
	 */
	public String getZpcdp4()
	{
		return csZpcdp4;
	}

	/**
	 * 
	 * initWhere Null 
	 * 
	 */
	public void initWhereNull()
	{
		csSt1 = (csSt1 == null ? new String() : csSt1);
		csSt2 = (csSt2 == null ? new String() : csSt2);
		csCity = (csCity == null ? new String() : csCity);
		csState = (csState == null ? new String() : csState);
		csCntry = (csCntry == null ? new String() : csCntry);
		csZpcd = (csZpcd == null ? new String() : csZpcd);
		csZpcdp4 = (csZpcdp4 == null ? new String() : csZpcdp4);
	}

	/** 
	 * Is !USA and Valid State in Country Field
	 * 
	 * @return boolean
	 */
	public boolean isCntryValidState()
	{
		return !isUSA() && CommonValidations.isValidState(csCntry);
	}

	/**
	 * Return boolean to denote if AddressData is Complete
	 * 
	 * @return boolean 
	 */
	public boolean isComplete()
	{
		boolean lbComplete =
			!UtilityMethods.isEmpty(csSt1)
				&& !UtilityMethods.isEmpty(csCity);

		if (lbComplete)
		{
			if (isUSA())
			{
				lbComplete = !UtilityMethods.isEmpty(csState);
			}
			else
			{
				lbComplete = !UtilityMethods.isEmpty(csCntry);
			}

			lbComplete = lbComplete && !UtilityMethods.isEmpty(csZpcd);
		}
		return lbComplete;
	}

	/**
	 * 
	 * Return boolean to denote if AddressData is Populated
	 * 
	 * @return boolean 
	 */
	public boolean isPopulated()
	{
		return (
			!(UtilityMethods.isEmpty(csSt1)
				&& UtilityMethods.isEmpty(csSt2)
				&& UtilityMethods.isEmpty(csState)
				&& UtilityMethods.isEmpty(csCntry)
				&& UtilityMethods.isEmpty(csZpcd)
				&& UtilityMethods.isEmpty(csZpcdp4)));
	}

	/** 
	 * 
	 * Is Valid Address 
	 * 
	 * @return boolean 
	 */
	public boolean isValidAddress(boolean abOKIfEmpty)
	{
		boolean lbValid = true;

		if (!(abOKIfEmpty && !isPopulated()))
		{
			lbValid = isComplete() && isValidStateCntry();
		}

		// defect 10299 
		lbValid =
			lbValid
				&& CommonValidations
					.isValidForMF(getAddressVector(false));
		// end defect 10299 

		return lbValid;
	}

	/**
	 * 
	 * Return boolean to denote if USA Address
	 * 
	 * @return boolean 
	 */
	public boolean isUSA()
	{
		return (
			UtilityMethods.isEmpty(csCntry)
				|| !UtilityMethods.isEmpty(csState));
	}

	/**
	 * Set value of City
	 * 
	 * @param asCity String
	 */
	public void setCity(String asCity)
	{
		csCity = asCity;
	}

	/** 
	 * IsValidStateCntry
	 *
	 * @return boolean  
	 */
	public boolean isValidStateCntry()
	{
		boolean lbValid = true;
		if (isUSA())
		{
			lbValid = CommonValidations.isValidState(csState);
		}
		else
		{
			lbValid =
				!UtilityMethods.isEmpty(csCntry)
					&& !CommonValidations.isValidState(csCntry);
		}
		return lbValid;
	}

	/**
	 * Set value of Cntry
	 * 
	 * @param asCntry String
	 */
	public void setCntry(String asCntry)
	{
		csCntry = asCntry;
	}

	/**
	 * Set CntryZpCd
	 * 
	 * @param asCntry String
	 */
	public void setCntryZpcd(String asCntryZpcd)
	{
		// Must split Country Zip Code if length > 5 
		if (asCntryZpcd.length() > CommonConstant.LENGTH_ZIPCODE)
		{
			setZpcd(
				asCntryZpcd.substring(
					0,
					CommonConstant.LENGTH_ZIPCODE));

			setZpcdp4(
				asCntryZpcd.substring(CommonConstant.LENGTH_ZIPCODE));
		}
		else
		{
			setZpcd(asCntryZpcd);
			setZpcdp4(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Set value of St1
	 * 
	 * @param asSt1 String
	 */
	public void setSt1(String asSt1)
	{
		csSt1 = asSt1;
	}

	/**
	 * Set value of St2
	 * 
	 * @param asSt2 String
	 */
	public void setSt2(String asSt2)
	{
		csSt2 = asSt2;
	}

	/**
	 * Set value of State
	 * 
	 * @param asState String
	 */
	public void setState(String asState)
	{
		csState = asState;
	}

	/**
	 * Set value of Zpcd
	 * 
	 * @param asZpcd String
	 */
	public void setZpcd(String asZpcd)
	{
		csZpcd = asZpcd;
	}

	/**
	 * Set value of Zpcd4
	 * 
	 * @param asZpcdp4 String
	 */
	public void setZpcdp4(String asZpcdp4)
	{
		csZpcdp4 = asZpcdp4;
	}
}
