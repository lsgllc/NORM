package com.txdot.isd.rts.services.data;

/*
 *
 * IndicatorData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/24/2008	add csIndiName, get/set methods()
 * 							defect 9651 Ver Defect_POS_B  
 * K Harrell	07/27/2010	add cbJnkAttrib, get/set methods()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * IndicatorData 
 * 
 * @version	6.5.0 			07/27/2010
 * @author	Michael Abernethy
 * <br>Creation Date:		10/17/2001 13:51:00 
 */

public class IndicatorData implements Comparable
{
	// defect 10491 
	// boolean 
	private boolean cbJnkAttrib = false;
	// end defect 10491  
	
	// int 
	private int ciPriority;

	// String
	// defect 9651 
	private String csIndiName;
	// end defect 9651  
	 
	private String csDesc;
	private String csStopCode;
	
	/**
	 * IndicatorData constructor comment.
	 */
	public IndicatorData()
	{
		super();
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
	 * @return  int a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		IndicatorData laIndicatorData = (IndicatorData) aaObject;
		if (getPriority() < laIndicatorData.getPriority())
		{
			return -1;
		}
		else if (getPriority() > laIndicatorData.getPriority())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Returns the value of Desc
	 * 
	 * @return String
	 */
	public String getDesc()
	{
		return csDesc;
	}
	
	/**
	 * Returns the value of Priority
	 * 
	 * @return int
	 */
	public int getPriority()
	{
		return ciPriority;
	}
	
		/**
		 * Return value of JnkAttrib
		 * 
		 * @return boolean 
		 */
		public boolean isJnkAttrib()
		{
			return cbJnkAttrib;
		}

	
	/**
	 * Returns the value of StopCode
	 * 
	 * @return String
	 */
	public String getStopCode()
	{
		return csStopCode;
	}
	
	/**
	 * Sets  the value of Desc
	 * 
	 * @param asDesc String
	 */
	public void setDesc(String asDesc)
	{
		csDesc = asDesc;
	}
	
	/**
	 * Sets  the value of Priority
	 * 
	 * @param aiPriority int
	 */
	public void setPriority(int aiPriority)
	{
		ciPriority = aiPriority;
	}
	
	/**
	 * Sets  the value of StopCode
	 * 
	 * @param asStopCode String
	 */
	public void setStopCode(String asStopCode)
	{
		csStopCode = asStopCode;
	}
	
	/**
	 * Returns value of IndiName
	 * 
	 * @return
	 */
	public String getIndiName()
	{
		return csIndiName;
	}

	/**
	 * Sets value of IndiName
	 * 
	 * @param asIndiName
	 */
	public void setIndiName(String asIndiName)
	{
		csIndiName = asIndiName;
	}
	
	/**
	 * Set value of JnkAttrib  
	 * 
	 * @param abJnkAttrib
	 */
	public void setJnkAttrib(boolean abJnkAttrib)
	{
		cbJnkAttrib = abJnkAttrib;
	}

}
