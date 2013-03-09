package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * DealerData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/03/2009	Created to replace DealersData   
 * 							Implement w/ AdminNameAddressData
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/06/2009	add getDealerInfoVector()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	02/18/2010	add Comparable, compareTo() 
 * 							(omitted in prior implementation) 
 * 							defect 10112 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This data class contains attributes and get/set methods for DealerData
 *
 * @version	POS_640			02/18/2010
 * @author	Administrator
 * <br>Creation Date:		07/03/2009  
 */
public class DealerData
	extends AdminNameAddressData
	implements Serializable, Comparable
{
	private String csContact;
	private String csPhoneNo;

	static final long serialVersionUID = -8824561811106367435L;

	/**
	 * DealerData constructor
	 *
	 */
	public DealerData()
	{
		super();
		csContact = CommonConstant.STR_SPACE_EMPTY;
		csPhoneNo = CommonConstant.STR_SPACE_EMPTY;
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
	 * @param   aaObject Object to be compared.
	 * @return  int  a negative integer, zero, or a positive integer as 
	 * 			this object is less than, equal to, or greater than the 
	 * 			specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		return getId() - ((DealerData) aaObject).getId();
	}

	/**
	 * Returns the value of Contact
	 * 
	 * @return String
	 */
	public String getContact()
	{
		return csContact;
	}

	/**
	 * Returns Vector of Dealer Info 
	 * 
	 * @return Vector
	 */
	public Vector getDealerInfoVector()
	{
		Vector lvDealerInfo = getNameAddressVector();
		lvDealerInfo.add(getFormattedPhoneNo());
		if (!UtilityMethods.isEmpty(csContact))
		{
			lvDealerInfo.add(csContact);
		}
		return lvDealerInfo;
	}

	/**
	 * Returns the value of PhoneNo
	 *
	 * @return  String 
	 */
	public final String getPhoneNo()
	{
		return csPhoneNo;
	}

	/**
	 * Sets the value of Contact
	 * 
	 * @param asContact
	 */
	public void setContact(String asContact)
	{
		csContact = asContact;
	}

	/**
	 * Sets the value of PhoneNo
	 *
	 * @param asPhoneNo   
	 */
	public final void setPhoneNo(String asPhoneNo)
	{
		csPhoneNo = asPhoneNo;
	}

	/**
	 * Return formatted Phone No 
	 * 
	 * @return String
	 */
	private String getFormattedPhoneNo()
	{
		if (UtilityMethods.isEmpty(csPhoneNo))
		{
			return "()-";
		}
		csPhoneNo = csPhoneNo.trim();
		StringBuffer lsStringBuffer = new StringBuffer();

		if (csPhoneNo.length() > 3)
		{
			lsStringBuffer.append("(");
			lsStringBuffer.append(csPhoneNo.substring(0, 3));
			lsStringBuffer.append(") ");
			if (csPhoneNo.length() >= 6)
			{
				lsStringBuffer.append(csPhoneNo.substring(3, 6));
				lsStringBuffer.append("-");
				if (csPhoneNo.length() > 6)
				{
					lsStringBuffer.append(csPhoneNo.substring(6));
				}
			}
			else
			{
				lsStringBuffer.append(csPhoneNo.substring(3));
			}
		}
		else
		{
			lsStringBuffer.append(csPhoneNo);
		}
		return lsStringBuffer.toString();
	}
}
