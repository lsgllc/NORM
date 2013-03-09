package com.txdot.isd.rts.services.reports.funds;

/*
 * PrintVector.java
 * 
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods for print vector
 * 
 * @version	5.2.3			05/31/2005 
 * @author	Administrator
 * <br>Creation Date:		01/07/2002 13:09:25
 */
public class PrintVector
{
	public int ciLineNumber;
	public int ciStartPoint;
	public int ciLength;
	public int ciJustification;
	public int ciTypeOfLineIndi;
	public String csDesc;
	
	/**
	 * PrintVector constructor
	 */
	public PrintVector()
	{
		super();
	}
	/**
	 * Get Description
	 * 
	 * @return String
	 */
	public String getDesc()
	{
		return csDesc;
	}
	/**
	 * Get Justification
	 * 
	 * @return int
	 */
	public int getJustification()
	{
		return ciJustification;
	}
	/**
	 * Get Length
	 * 
	 * @return int
	 */
	public int getLength()
	{
		return ciLength;
	}
	/**
	 * Get Line Number
	 * 
	 * @return int
	 */
	public int getLineNumber()
	{
		return ciLineNumber;
	}
	/**
	 * Get Start Point
	 * 
	 * @return int
	 */
	public int getStartPoint()
	{
		return ciStartPoint;
	}
	/**
	 * Get Type Of Line Indicator
	 * 
	 * @return int
	 */
	public int getTypeOfLineIndi()
	{
		return ciTypeOfLineIndi;
	}
	/**
	 * Set Description
	 * 
	 * @param asNewDesc String
	 */
	public void setDesc(String asNewDesc)
	{
		csDesc = asNewDesc;
	}
	/**
	 * Set Justification
	 * 
	 * @param aiNewJustification int
	 */
	public void setJustification(int aiNewJustification)
	{
		ciJustification = aiNewJustification;
	}
	/**
	 * Set Length
	 * 
	 * @param aiNewLength int
	 */
	public void setLength(int aiNewLength)
	{
		ciLength = aiNewLength;
	}
	/**
	 * Set Line Number
	 * 
	 * @param aiNewLineNumber int
	 */
	public void setLineNumber(int aiNewLineNumber)
	{
		ciLineNumber = aiNewLineNumber;
	}
	/**
	 * Set Start Point
	 * 
	 * @param aiNewStartPoint int
	 */
	public void setStartPoint(int aiNewStartPoint)
	{
		ciStartPoint = aiNewStartPoint;
	}
	/**
	 * Set Type Of Line Indidicator
	 * 
	 * @param aiNewTypeOfLineIndi int
	 */
	public void setTypeOfLineIndi(int aiNewTypeOfLineIndi)
	{
		ciTypeOfLineIndi = aiNewTypeOfLineIndi;
	}
}
