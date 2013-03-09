package com.txdot.isd.rts.services.reports;

/*
 * ColumnHeader.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	07/04/2001	New Class
 * S Johnston	05/09/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * Ray Rowehl	09/27/2005	Add public constants for position.
 * 							add CENTER, LEFT, RIGHT
 * 							defect 7890 Ver 5.2.3
 * K Harrell	01/12/2006	Restored RIGHT = 2
 * 							defect 7890 Ver 5.2.3 	  	
 * ---------------------------------------------------------------------
 */

/**
 * The ColumnHeader Class contains information about a particular table
 * column
 *
 * @version	5.2.3			01/12/2006 
 * @author 	Rakesh Duggirala
 * <br>Creation Date:		09/04/2001
 */

public class ColumnHeader
{
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	public int ciStartPoint; //Start Point of the Column
	public int ciLength; //Length of this Column
	public int ciAlignment; //Either Left(0), Right(2) or Center(1)
	public String csDesc; //Description

	/**
	 * ColumnHeader constructor
	 */
	public ColumnHeader()
	{
		super();
	}

	/**
	 * This constructor should be used to instantiate the Class
	 * 
	 * @param asDesc String
	 * @param aiStartPt int
	 * @param aiLength int
	 */
	public ColumnHeader(String asDesc, int aiStartPt, int aiLength)
	{
		csDesc = asDesc;
		ciStartPoint = aiStartPt;
		ciLength = aiLength;
		ciAlignment = LEFT;
	}

	/**
	 * This constructor should be used to instantiate the Class
	 * 
	 * @param asDesc String
	 * @param aiStartPt int
	 * @param aiLength int
	 * @param aiAlignment int
	 */
	public ColumnHeader(
		String asDesc,
		int aiStartPt,
		int aiLength,
		int aiAlignment)
	{
		csDesc = asDesc;
		ciStartPoint = aiStartPt;
		ciLength = aiLength;
		ciAlignment = aiAlignment;
	}

	/**
	 * getAlignment
	 * 
	 * @return int
	 */
	public int getAlignment()
	{
		return ciAlignment;
	}

	/**
	 * setAlignment
	 * 
	 * @param aiNewAlignment int
	 */
	public void setAlignment(int aiNewAlignment)
	{
		ciAlignment = aiNewAlignment;
	}
}
