package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * CustomerNameData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CustomerNameData
 *
 * @version	6.5.0 			05/24/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		05/24/2010  12:00:17 
 */
public class CustomerNameData implements Serializable, Comparable
{

	// String
	private String csCustBsnName = new String();
	private String csCustFstName = new String();
	private String csCustLstName = new String();
	private String csCustMIName = new String();

	static final long serialVersionUID = 7434356077900785573L;

	/**
	 * CustomerNameData.java Constructor
	 * 
	 */
	public CustomerNameData()
	{
		super();
	}
	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.(This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> 
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that 
	 * <tt>x.compareTo(y)==0</tt> implies that <tt>sgn(x.compareTo(z)) 
	 * == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally 
	 * speaking, any class that implements the <tt>Comparable</tt> 
	 * interface and violates this condition should clearly indicate 
	 * this fact.  The recommended language is "Note: this class has a 
	 * natural ordering that is inconsistent with equals."
	 * 
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this 
	 *		object is less than, equal to, or greater than the specified
	 *		object.
	 * 
	 * @throws ClassCastException if the specified object's type 
	 * 			prevents it from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		CustomerNameData laCompTo = (CustomerNameData) aaObject;
		return getCustNameCompare().compareTo(
			laCompTo.getCustNameCompare());
	}

	/**
	 * Returns value of csCustBsnName
	 * 
	 * @return String
	 */
	public String getCustBsnName()
	{
		return csCustBsnName;
	}

	/**
	 * Returns value of csCustFstName
	 * 
	 * @return String
	 */
	public String getCustFstName()
	{
		return csCustFstName;
	}

	/**
	 * Returns value of csCustLstName
	 * 
	 * @return String
	 */
	public String getCustLstName()
	{
		return csCustLstName;
	}

	/**
	 * Returns value of csCustMIName
	 * 
	 * @return String
	 */
	public String getCustMIName()
	{
		return csCustMIName;
	}

	/**
	 * Return String representing Customer Name, either Business Name 
	 *  or Customer Name with or without MI 
	 * 
	 * @return String 
	 */
	public String getCustName()
	{
		return getCustName(true);
	}

	/**
	 * Return 'calculated' CustName
	 * 
	 * @return String
	 */
	public String getCustName(boolean abShowMI)
	{
		String lsCustName = "";
		{
			if (isIndividual())
			{
				lsCustName = csCustFstName.trim() + " ";

				if (abShowMI && !UtilityMethods.isEmpty(csCustMIName))
				{
					lsCustName = lsCustName + csCustMIName + " ";
				}
				lsCustName = lsCustName + csCustLstName.trim();
			}
			else
			{
				lsCustName = csCustBsnName.trim();
			}

		}
		return lsCustName;
	}

	/**
	 * Return 'calculated' CustName
	 * 
	 * @return String
	 */
	public String getCustNameCompare()
	{
		String lsCustName = "";
		{
			if (isIndividual())
			{
				lsCustName =
					csCustLstName.trim() + " " + csCustFstName.trim();

				if (!UtilityMethods.isEmpty(csCustMIName))
				{
					lsCustName = lsCustName + " " + csCustMIName;
				}
			}
			else
			{
				lsCustName = csCustBsnName.trim();
			}
		}
		return lsCustName;
	}

	/**
	 * Return boolean to denote if Customer is Business 
	 * 
	 * @return boolean
	 */
	public boolean isBusiness()
	{
		return csCustBsnName != null
			&& csCustBsnName.trim().length() != 0;
	}

	/**
	 * Return boolean to denote if Customer is Individual 
	 * 
	 * @return boolean
	 */
	public boolean isIndividual()
	{
		return UtilityMethods.isEmpty(csCustBsnName);
	}

	/**
	 * Sets value of csCustBsnName
	 * 
	 * @param asCustBsnName
	 */
	public void setCustBsnName(String asCustBsnName)
	{
		csCustBsnName = asCustBsnName;
	}

	/**
	 * Sets value of csCustFstName
	 * 
	 * @param asCustFstName
	 */
	public void setCustFstName(String asCustFstName)
	{
		csCustFstName = asCustFstName;
	}

	/**
	 * Sets value of csCustLstName
	 * 
	 * @param asCustLstName
	 */
	public void setCustLstName(String asCustLstName)
	{
		csCustLstName = asCustLstName;
	}

	/**
	 * Sets value of csCustMIName
	 * 
	 * @param asCustMIName
	 */
	public void setCustMIName(String asCustMIName)
	{
		csCustMIName = asCustMIName;
	}
}
