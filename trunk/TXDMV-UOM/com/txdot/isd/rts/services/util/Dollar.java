package com.txdot.isd.rts.services.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/**
 *
 * Dollar
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * N Ting		09/07/2001	Makes class serializable
 * N Ting		04/23/2002	Fix CQU100003460, printDollar()
 * K Harrell	01/02/2004	Deprecate divide(Dollar). 
 *							Modified main(); Removed reference to divide(Dollar) 
 *							Defect 6379.  Ver 5.1.5 Fix 2
 * T Pederson	10/13/2006	Remove Double.toString since value passed 
 * 							in is string already. 
 * 							modify printDollar(boolean) 
 * 							defect 8900 Ver Exempts
 * T Pederson	11/29/2006	Modified the constructor to make sure the  
 * 							Dollar value always has two decimals. 
 * 							modify Dollar(String) 
 * 							defect 9038 Ver Exempts
 * R Pilon		02/02/2012	Added default constructor and missing setter
 * 							  method to prevent web service validation error.
 * 							add Dollar() constructor, setValue()
 * 							defect 11135 Ver 6.10.0
 * R Pilon		05/21/2012	Implement interim code for BigDecimal for toString
 * 							  as BigDecimal.toString() may return the scientific 
 * 							  notation for an exponent.  The toPlainString()
 * 							  should be used, but until the absolute cut over to
 * 							  the JRE 1.6, it cannot be used.
 * 							add toPlainString()
 * 							modify Dollar(double) constructor, Dollar(String) 
 * 							  constructor, add(Dollar), addNoRound(Dollar),
 * 							  divide(Dollar), divideNoRound(Dollar), 
 * 							  multiply(Dollar), multiplyNoRound(Dollar),
 * 							  round(), subtract(Dollar), subtractNoRound(Dollar)
 * 							defect 11366 Ver 7.0.0 
 * ---------------------------------------------------------------
 */
 /**
 * The Dollar class acts as an easy to use interface to the 
 * java.math.BigDecimal class.
 * <br>It assumes all values will be in US Dollars, will have a scale 
 * of 2, and will round up any half values (for example $4.555 to $4.56)
 * <br>In addition, the Dollar class overrides the <code>toString()</code> 
 * to display itself properly.
 *
 * @version	POS_700		05/21/2012
 * @author  Michael Abernethy
 * <br>Creation Date:	06/30/2001 
 */ 
public class Dollar implements Comparable, java.io.Serializable{
	private java.lang.String value;
	private java.math.BigDecimal bigDecimal;
	private final static long serialVersionUID = 4116606123997607656L;
	
	/**
	 * Dollar.java Constructor
	 */
	public Dollar()
	{
		super();
	}
	
/**
  * Creates a Dollar object with the value of val
 * <br>Note: Only the String constructor in java.math.BigDecimal is used, since it is the only constuctor that exactly represents the number.
 * The constructor <code>new java.math.BigDecimal(1.5)</code> does not create a value of 1.5, but instead approximates it
 * 1.49999999999 since 1.5 is impossible to represent in binary form.
 * <br>However, using the constructor <code>new java.math.BigDecimal("1.5")</code> does create a value of exactly
 * 1.5.
 * <p>This constructor of Dollar, which uses a <code>double</code> as a parameter DOES NOT call 
 * <code>java.math.Double(double)</code>
 * but instead calls <code>java.math.Double(String)</code>.
 * @param val double
 */
public Dollar(double val) 
{	
	if (val == Double.MIN_VALUE)
	{
		val = 0.00;
	}
	bigDecimal = new java.math.BigDecimal(Double.toString(val));
	// defect 11366
//	value = bigDecimal.toString();
	value = this.toPlainString(bigDecimal);
	// end defect 11366
}
/**
 * Creates a Dollar object with the value of val
 * <br>Note: Only the String constructor in java.math.BigDecimal is used, since it is the only constuctor that exactly represents the number.
 * The constructor <code>new java.math.BigDecimal(1.5)</code> does not create a value of 1.5, but instead approximates it
 * 1.49999999999 since 1.5 is impossible to represent in binary form.
 * <br>However, using the constructor <code>new java.math.BigDecimal("1.5")</code> does create a value of exactly
 * 1.5.
 * @param val java.lang.String
 */
public Dollar(String val) 
{
	val = val.trim();
	while (val.substring(0,1).equals("0") && val.length() > 4)
		val = val.substring(1, val.length());
		
	// defect 9038
	// Make sure the value has two decimal places
	int liIndxOfDecPnt = val.indexOf(".");
	if (liIndxOfDecPnt == -1)
	{
		val = val + ".00";
	}
	else if (val.substring(liIndxOfDecPnt, val.length()).length() == 1)
	{
		val = val + "00";
	}
	else if (val.substring(liIndxOfDecPnt, val.length()).length() == 2)
	{
		val = val + "0";
	}
	// end defect 9038
	bigDecimal = new java.math.BigDecimal(val);
	// defect 11366
//	value = bigDecimal.toString();
	value = this.toPlainString(bigDecimal);
	// end defect 11366
}
/**
 * Returns a new Dollar.  Since "+" cannot be used on Dollar, this function is operationally equivelent to Dollar + Dollar.
 * <br>After the add is performed, it will perform a round on the Dollar it will return to ensure proper 2 digit accuracy.
 * @return com.txdot.isd.rts.client.general.gui.Dollar a new graph of a Dollar, with a value of the result of the addition
 * @param d com.txdot.isd.rts.client.general.gui.Dollar
 */
public Dollar add(Dollar d) 
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	b = b.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	java.math.BigDecimal result = bigDecimal.add(b);
	result = result.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/**
 * Returns a new Dollar.  Since "+" cannot be used on Dollar, this function is operationally equivelent to Dollar + Dollar.
 * <br>After the add is performed, it does not perform a round on the Dollar it will return and therefore is accurate to as many digits as needed.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 */
public Dollar addNoRound(Dollar d) 
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	java.math.BigDecimal result = bigDecimal.add(b);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
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
	 * @param   o the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
public int compareTo(java.lang.Object o) 
{
	Dollar d = (Dollar)o;
	if (Double.parseDouble(value) < Double.parseDouble(d.getValue()))
		return -1;
	else if (Double.parseDouble(value) > Double.parseDouble(d.getValue()))
		return 1;
	else
		return 0;
}
/**
 * Returns a new Dollar.  Since "/" cannot be used on Dollar, this function is operationally equivelent to Dollar / Dollar.
 * <br>After the divide is performed, it will perform a round on the Dollar it will return to ensure proper 2 digit accuracy.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 * @deprecated
 */
public Dollar divide(Dollar d) 
{	
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	java.math.BigDecimal result = bigDecimal.divide(b, java.math.BigDecimal.ROUND_HALF_UP);
	result = result.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/**
 * Returns a new Dollar.  Since "/" cannot be used on Dollar, this function is operationally equivelent to Dollar / Dollar.
 * <br>After the divide is performed, it does not perform a round on the Dollar it will return and therefore is accurate to as many digits as needed.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 */
public Dollar divideNoRound(Dollar d) 
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	java.math.BigDecimal result = bigDecimal.divide(b, 16, java.math.BigDecimal.ROUND_HALF_UP);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/**
 * Returns true if the two Dollar objects have the same value
 * @return boolean
 * @param d com.txdot.isd.rts.client.general.ui.Dollar
 */
public boolean equals(Dollar d) 
{
	return toString().equals(d.toString());
}
/**
 * Returns the value of the Dollar
 * @return java.lang.String
 */
public java.lang.String getValue() 
{
	return value;
}
/**
 * Insert the method's description here.
 * @param args java.lang.String[]
 */
public static void main(String[] args) 
{
	Dollar d1 = new Dollar("1000.00");
	Dollar d2 = new Dollar("5.00");
	// Defect 6379 - Deprecated divide(Dollar)
	// Dollar d3 = d2.divide(d1);
	// System.out.println(d3);
	// End Defect 6379 

	Dollar e1 = new Dollar("887.50");
	Dollar e2 = new Dollar("0.05");
	Dollar e3 = e1.multiply(e2);
	System.out.println(e3);

	double dbl = Double.parseDouble("12.24");
	Dollar f = new Dollar(dbl);
	System.out.println(f.getValue());

	Dollar e = new Dollar("8.239999999");
	Dollar s = new Dollar(6.2399999);
	System.out.println(e);
	System.out.println(s);
	System.out.println(s.round());
	System.out.println("Real value = " + (8.2399999/6.2399999));
	// Defect 6379 - Deprecated divide(Dollar)
	// System.out.println(e.divide(s));
	// End Defect 6379 
	Dollar q = e.divideNoRound(s);
	System.out.println(q.getValue());

	Dollar c = new Dollar("8.125");
	Dollar v = new Dollar("4.555");
	System.out.println("Real value = " + (8.125 * 4.555));
	System.out.println(c.multiply(v));
	Dollar x = c.multiplyNoRound(v);
	Dollar x2 = x.round();
	System.out.println(x.getValue());
	System.out.println(x2);

	Dollar g = new Dollar("-999");
	System.out.println(g.printDollar());

	Dollar y = new Dollar(-5.00);
	Dollar a = new Dollar("0.00");
	System.out.println(a.add(y));

	Dollar e4 = new Dollar(0.0);
	Dollar e5 = new Dollar("0.00");
	System.out.println(e4.equals(e5));

	Dollar y1 = new Dollar("1.00");
	Dollar y2 = new Dollar("12.00");
	Dollar y3 = new Dollar("40.5");
	Dollar y4 = y1.divideNoRound(y2);
	System.out.println(y4.multiplyNoRound(y3));
	System.out.println(y4.multiplyNoRound(y3).round());
}
/**
 * Returns a new Dollar.  Since "*" cannot be used on Dollar, this function is operationally equivelent to Dollar * Dollar.
 * <br>After the multiply is performed, it will perform a round on the Dollar it will return to ensure proper 2 digit accuracy.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 */
public Dollar multiply(Dollar d)
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	java.math.BigDecimal result = bigDecimal.multiply(b);
	result = result.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/** 
* Returns a new Dollar.  Since "*" cannot be used on Dollar, this function is operationally equivelent to Dollar * Dollar.
 * <br>After the multiply is performed, it does not perform a round on the Dollar it will return and therefore is accurate to as many digits as needed.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 */
public Dollar multiplyNoRound(Dollar d) 
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	java.math.BigDecimal result = bigDecimal.multiply(b);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/**
 * Prints the dollar with commas, decimal points, and the dollar sign (e.g. $1,000,000)
 * @return java.lang.String
 */
public String printDollar() 
{
	return printDollar(true);
}
/**
 * Prints the dollar with commas, decimal points, and the dollar sign if true, without the dollar sign if false (e.g. $1,000,000)
 * @param boolean whether to print the "$" or not
 * @return java.lang.String
 */
public String printDollar(boolean abDollarSign) {
    String dollar = value;
    boolean isNegative = false;
	double d = 0.00;
    try {
        d = Double.parseDouble(dollar);
        if (d == 0.00) {
            if (abDollarSign) {
                return "$0.00";
            } else {
                return "0.00";
            }

        }
    } catch (NumberFormatException numberFormationException) {
        if (abDollarSign) {
            return "$0.00";
        } else {
            return "0.00";
        }

    }

	// defect 8900
	// Commented out because a value of 10,000,000 or more has 
	// a double value of 1.0E7 instead of 10000000.
    //dollar = Double.toString(d);
	// end defect 8900
    if (dollar.substring(0, 1).equals("-")) {
        isNegative = true;
        dollar = dollar.substring(1, dollar.length());
    }
    

    int commas = (dollar.substring(0, dollar.indexOf(".")).length() - 1) / 3;
    StringBuffer decimals =
        new StringBuffer(dollar.substring(0, dollar.indexOf(".")));
    int offset = 0;
    for (int i = 0; i < commas; i++) 
    {
        decimals.insert(decimals.length() - (3 * (i + 1) + offset), ",");
        offset++;
    }
    dollar =
        decimals.toString() + dollar.substring(dollar.indexOf("."), dollar.length());

    if (dollar.substring(dollar.lastIndexOf("."), dollar.length()).length() == 1)
        dollar = dollar.substring(0, dollar.lastIndexOf(".") + 1) + "00";

    else if (
        dollar.substring(dollar.lastIndexOf("."), dollar.length()).length() == 2)
        dollar = dollar.substring(0, dollar.lastIndexOf(".") + 2) + "0";
    else
        dollar = dollar.substring(0, dollar.lastIndexOf(".") + 3);

    if (isNegative)
        dollar = "-" + dollar;

    if (abDollarSign) {
        return "$" + dollar;
    } else {
        return dollar;
    }

}
/**
 * Rounds the dollar to its proper 2 decimal point precision.  It will round up half cents (e.g. 1.225 will become 1.23) to reflect
 * proper accounting methods.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 */
public Dollar round() 
{
	java.math.BigDecimal temp = new java.math.BigDecimal(value);
	//temp = temp.setScale(3, java.math.BigDecimal.ROUND_HALF_UP);
	temp = temp.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	// defect 11366
	return new Dollar(this.toPlainString(temp));
	// end defect 11366 
}

	/**
	 * Set the value.
	 * 
	 * @param asValue
	 */
	public void setValue(String asValue)
	{
		value = asValue;
	}

/**
 * Returns a new Dollar.  Since "-" cannot be used on Dollar, this function is operationally equivelent to Dollar - Dollar.
 * <br>After the subtract is performed, it will perform a round on the Dollar it will return to ensure proper 2 digit accuracy.
 * @return com.txdot.isd.rts.client.general.gui.Dollara new graph of a Dollar, with a value of the result of the subtraction
 * @param d com.txdot.isd.rts.client.general.gui.Dollar
 */
public Dollar subtract(Dollar d) 
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	b = b.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	java.math.BigDecimal result = bigDecimal.subtract(b);
	result = result.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/**
 * Returns a new Dollar.  Since "-" cannot be used on Dollar, this function is operationally equivelent to Dollar - Dollar.
 * <br>After the subtract is performed, it does not perform a round on the Dollar it will return and therefore is accurate to as many digits as needed.
 * @return com.txdot.isd.rts.services.util.Dollar
 * @param d com.txdot.isd.rts.services.util.Dollar
 */
public Dollar subtractNoRound(Dollar d) 
{
	java.math.BigDecimal b = new java.math.BigDecimal(d.getValue());
	java.math.BigDecimal result = bigDecimal.subtract(b);
	// defect 11366
	return new Dollar(this.toPlainString(result));
	// end defect 11366 
}
/**
 * Prints out the Dollar object to ensure that there are always 2 digits after the decimal point
 * @return java.lang.String
 */
public String toString() 
{
	Dollar d = this.round();
	String toReturn = d.getValue();
	if (toReturn.lastIndexOf(".") == -1)
			return "0.00";
			
	else if (toReturn.substring(toReturn.lastIndexOf("."), toReturn.length()).length() == 1)
			return toReturn.substring(0, toReturn.lastIndexOf(".")+1) + "00";
	
	else if (toReturn.substring(toReturn.lastIndexOf("."), toReturn.length()).length() == 2)
			return toReturn.substring(0, toReturn.lastIndexOf(".")+2) + "0";
	else	
		return toReturn.substring(0, toReturn.lastIndexOf(".")+3);
}

	/**
	 * Return a String representation for a BigDecimal. This is an interim
	 * solution for the java JRE 1.4 to JRE 1.6 migration as the
	 * BigDecimal.toString() method may return the scientific notation for an
	 * exponent.
	 * 
	 * @param aaValue
	 * @return String
	 */
	private String toPlainString(BigDecimal aaValue)
	{
		String lsStringValue = null;
		float javaVersion = Float.parseFloat(System
				.getProperty("java.specification.version"));
		if (javaVersion >= 1.5)
		{
			// as of Java 5.0 and above, BigDecimal.toPlainString() should be
			// used.
			Method method;
			try
			{
				method = BigDecimal.class.getMethod("toPlainString",
						(Class[]) null);
				lsStringValue = (String) method.invoke(aaValue,
						(Object[]) null);
			}
			catch (Exception aeEx)
			{
				Log.write(Log.START_END, this,
						"Exception when invoking "
								+ "toPlainString() for BigDecimal = "
								+ aaValue
								+ CommonConstant.SYSTEM_LINE_SEPARATOR
								+ aeEx.getMessage());
			}
		}
		else
		{
			// use BigDecimal.toString() with Java 1.4 and below
			lsStringValue = aaValue.toString();
		}
		return lsStringValue;
	}
}
