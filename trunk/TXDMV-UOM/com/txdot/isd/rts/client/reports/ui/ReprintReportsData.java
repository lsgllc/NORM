package com.txdot.isd.rts.client.reports.ui;

import java.io.File;
import java.util.Comparator;

/*
 * ReprintReportsData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	06/29/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify  
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * ReprintReportsData handels reprinting of reports
 *
 * @version	5.2.3			06/29/2005
 * @author	Administrator
 * <br>Creation Date:		12/08/2001 16:43:00
 */
public class ReprintReportsData implements Comparator, Comparable
{
	private File caRptFile = null;
	private int ciRptNo;
	private int ciWsId;
	private String csDate;
	private String csRptDesc = "";
	private String csTime;
	
	/**
	 * ReprintReportsData constructor
	 */
	public ReprintReportsData()
	{
		super();
		//This value indicates that Workstation ID is not set
		ciWsId = -1;
	}
	
	/**
	 * Compares its two arguments for order.  Returns a negative integer
	 * zero, or a positive integer as the first argument is less than,
	 * equal to, or greater than the second.<p>
	 *
	 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
	 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.
	 * (This implies that <tt>compare(x, y)</tt> must throw an exception
	 * if and only if <tt>compare(y, x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt>
	 * implies <tt>compare(x, z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>compare(x, y)==0
	 * </tt> implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))
	 * </tt> for all <tt>z</tt>.<p>
	 *
	 * It is generally the case, but <i>not</i> strictly required that 
	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking
	 * any comparator that violates this condition should clearly
	 * indicate this fact.  The recommended language is "Note: this
	 * comparator imposes orderings that are inconsistent with equals."
	 * 
	 * @param aaO1 Object
	 * @param aaO2 Object
	 * @return a negative integer, zero, or a positive integer as the
	 * 	       first argument is less than, equal to, or greater than
	 * 			the second. 
	 */
	public int compare(Object aaO1, Object aaO2)
	{
		ReprintReportsData laReprintReportsData1 =
			(ReprintReportsData) aaO1;
		ReprintReportsData laReprintReportsData2 =
			(ReprintReportsData) aaO2;
		int liCompare = 0;
		//description
		liCompare =
			laReprintReportsData1.getRptDesc().compareTo(
				laReprintReportsData2.getRptDesc());
		if (liCompare != 0)
		{
			return liCompare;
		}
		if (laReprintReportsData1.getWsId()
			> laReprintReportsData2.getWsId())
		{
			return -1;
		}
		else if (
			laReprintReportsData1.getWsId()
				< laReprintReportsData2.getWsId())
		{
			return 1;
		}
		liCompare =
			laReprintReportsData1.getDate().compareTo(
				laReprintReportsData2.getDate());
		if (liCompare != 0)
		{
			return liCompare * -1;
		}
		liCompare =
			laReprintReportsData1.getTime().compareTo(
				laReprintReportsData2.getTime());
		if (liCompare != 0)
		{
			return liCompare * -1;
		}
		return 0;
	}
	
	/**
	 * Compares this object with the specified object for order.
	 * Returns a negative integer, zero, or a positive integer as this
	 * object is less than, equal to, or greater than the specified
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.
	 * (This implies that <tt>x.compareTo(y)</tt> must throw an
	 * exception iff <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt>
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0
	 * </tt>implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))
	 * </tt>, for all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally
	 * speaking, any class that implements the <tt>Comparable</tt>
	 * interface and violates this condition should clearly indicate
	 * this fact.  The recommended language is "Note: this class has a 
	 * natural ordering that is inconsistent with equals."
	 *
	 * Sorts the Data Object by Item Cd Desc 
	 * 
	 * @param   aaObj Object to be compared.
	 * @return  int a negative integer, zero, or a positive integer as 
	 * 		this object is less than, equal to, or greater than the 
	 * 		specified object.
	 */
	public int compareTo(Object aaObj)
	{
		ReprintReportsData laReprintReportsData =
			(ReprintReportsData) aaObj;
		String lsCurrentString = csRptDesc;
		String laCompareToString = laReprintReportsData.getRptDesc();
		return lsCurrentString.compareTo(laCompareToString);
	}
	
	/**
	 * getDate
	 * 
	 * @return String
	 */
	public String getDate()
	{
		return csDate;
	}
	
	/**
	 * getRptDesc
	 * 
	 * @return String
	 */
	public String getRptDesc()
	{
		return csRptDesc;
	}
	
	/**
	 * getRptFile
	 * 
	 * @return File
	 */
	public File getRptFile()
	{
		return caRptFile;
	}
	
	/**
	 * getRptNo
	 * 
	 * @return int
	 */
	public int getRptNo()
	{
		return ciRptNo;
	}
	
	/**
	 * getTime
	 * 
	 * @return String
	 */
	public String getTime()
	{
		return csTime;
	}
	
	/**
	 * getWsId
	 * 
	 * @return int
	 */
	public int getWsId()
	{
		return ciWsId;
	}
	
	/**
	 * setDate
	 * 
	 * @param aaNewDate String
	 */
	public void setDate(String aaNewDate)
	{
		csDate = aaNewDate;
	}
	
	/**
	 * setRptDesc
	 * 
	 * @param asNewRptDesc int
	 */
	public void setRptDesc(String asNewRptDesc)
	{
		csRptDesc = asNewRptDesc;
	}
	
	/**
	 * setRptFile
	 * 
	 * @param aaNewRptFile File
	 */
	public void setRptFile(File aaNewRptFile)
	{
		caRptFile = aaNewRptFile;
	}
	
	/**
	 * setRptNo
	 * 
	 * @param aiNewRptNo int
	 */
	public void setRptNo(int aiNewRptNo)
	{
		ciRptNo = aiNewRptNo;
	}
	
	/**
	 * setTime
	 * 
	 * @param asNewTime String
	 */
	public void setTime(String asNewTime)
	{
		csTime = asNewTime;
	}
	
	/**
	 * setWsId
	 * 
	 * @param aiNewWsId int
	 */
	public void setWsId(int aiNewWsId)
	{
		ciWsId = aiNewWsId;
	}
}