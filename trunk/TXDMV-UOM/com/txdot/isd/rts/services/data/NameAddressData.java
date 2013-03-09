package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Vector;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * NameAddressData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/24/2009	Created
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/17/2009	add getNameAddressStringBuffer()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add Displayable, getAttributes(), 
 * 							 NameAddressData(NameAddressData)  
 * 							defect 10191 Ver Defect_POS_G 
 * ---------------------------------------------------------------------
 */

/**
 * Base Data Object for entities, e.g. DealerData
 *
 * @version	Defect_POS_G 	10/16/2009
 * @author	Kathy Harrell 
 * <br>Creation Date:		06/24/2009	16:38:00
 */
public class NameAddressData implements Serializable
// defect 10191
, Displayable
// end defect 10191 
{
	protected String csName1;
	protected String csName2;
	protected AddressData caAddressData;

	static final long serialVersionUID = -830450057333123032L;

	/**
	 * NameAddressData.java Constructor
	 *
	 */
	public NameAddressData()
	{
		super();
		csName1 = new String();
		csName2 = new String();
		caAddressData = new AddressData();
	}

	/**
	 * NameAddressData.java Constructor
	 * 
	 * @param aaObject
	 * @return
	 */
	public NameAddressData(NameAddressData aaData)
	{
		csName1 = aaData.getName1();
		csName2 = aaData.getName2();
		caAddressData =
			(AddressData) (UtilityMethods
				.copy(aaData.getAddressData()));
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
		NameAddressData laCompData = (NameAddressData) aaObject;

		boolean lbComp = laCompData.getName1().equals(getName1());

		lbComp = lbComp && laCompData.getName2().equals(getName2());

		lbComp =
			lbComp
				&& getAddressData().compareTo(laCompData.getAddressData())
					== 0;

		return lbComp ? 0 : 1;
	}

	/**
	 * Get attributes for Object fields
	 * 
	 * @return java.util.Map
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap lhmHash = new java.util.HashMap();

		Field[] larrFields = getClass().getDeclaredFields();

		for (int i = 0; i < larrFields.length; i++)
		{
			String lsFieldName = larrFields[i].getName();

			try
			{
				if (lsFieldName.equals("caAddressData"))
				{
					UtilityMethods.addAttributesForObject(
						lsFieldName,
						getAddressData().getAttributes(),
						lhmHash);
				}
				else
				{
					lhmHash.put(lsFieldName, larrFields[i].get(this));
				}
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Return StringBuffer of Names separated by "line.separator"
	 * 
	 * @return StringBuffer 
	 */
	public StringBuffer getNameStringBuffer()
	{
		StringBuffer laName = new StringBuffer();
		Vector lvName = getNameVector();

		for (int i = 0; i < lvName.size(); i++)
		{
			String lsData = (String) lvName.elementAt(i);
			laName.append(
				lsData + CommonConstant.SYSTEM_LINE_SEPARATOR);
		}
		return laName;
	}

	/**
	 * Return StringBuffer of Name, Address separated by "line.separator"
	 * 
	 * @return StringBuffer 
	 */
	public StringBuffer getNameAddressStringBuffer()
	{
		return getNameStringBuffer().append(
			getAddressData().getAddressStringBuffer());
	}

	/**
	 * Return value of caAddressData 
	 * 
	 * @return AddressData
	 */
	public AddressData getAddressData()
	{
		if (caAddressData == null)
		{
			caAddressData = new AddressData();
		}
		return caAddressData;
	}

	/**
	 * Return value of csName1 
	 * 
	 * @return String
	 */
	public String getName1()
	{
		return csName1;
	}

	/**
	 * Return value of csName2 
	 * 
	 * @return String
	 */
	public String getName2()
	{
		return csName2;
	}

	/**
	 * Return vector of Strings from Name   
	 * 
	 * @return Vector 
	 */
	public Vector getNameVector()
	{
		Vector lvVector = new Vector();
		if (!UtilityMethods.isEmpty(csName1))
		{
			lvVector.add(csName1);
		}
		if (!UtilityMethods.isEmpty(csName2))
		{
			lvVector.add(csName2);
		}
		return lvVector;
	}

	/**
	 * 
	 * Return Vector of Name/Address Data
	 * 
	 * @return Vector 
	 */
	public Vector getNameAddressVector()
	{
		Vector lvVector = getNameVector();
		lvVector.addAll(getAddressData().getAddressVector(false));
		return lvVector;
	}

	/**
	 * initWhereNull
	 * 
	 */
	public void initWhereNull()
	{
		csName1 = (csName1 == null ? new String() : csName1);
		csName2 = (csName2 == null ? new String() : csName2);
		getAddressData().initWhereNull();
	}

	/**
	 * 
	 * Return boolean to determine if Object is populated 
	 * 
	 * @return boolean 
	 */
	public boolean isPopulated()
	{
		return !(
			UtilityMethods.isEmpty(csName1)
				&& UtilityMethods.isEmpty(csName1))
			|| getAddressData().isPopulated();

	}

	/**
	 * Set caAddressData 
	 * 
	 * @param aaAddressData
	 */
	public void setAddressData(AddressData aaAddressData)
	{
		caAddressData = aaAddressData;
	}

	/**
	 * Set value of csName1
	 * 
	 * @param asName1
	 */
	public void setName1(String asName1)
	{
		csName1 = asName1;
	}

	/**
	 * Set value of csName2
	 * 
	 * @param asName2
	 */
	public void setName2(String asName2)
	{
		csName2 = asName2;
	}

}
