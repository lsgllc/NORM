package com.txdot.isd.rts.services.data;

import java.util.Vector;

/*
 *
 * ReportStatusData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * ReportStatusData
 * 
 * 
 * @version		5.2.3	04/21/2005
 * @author 		Bobby Tulsiani
 * <br>Creation Date:	
 */

public class ReportStatusData implements java.io.Serializable
{
	// String 
	private String csReportName;

	// Vector 
	private Vector cvStatusVector;

	private final static long serialVersionUID = -4647488767843942383L;
	/**
	 * ReportStatusData constructor comment.
	 */
	public ReportStatusData()
	{
		super();
	}
	/**
	 * Return value of ReportName
	 * 
	 * @return String
	 */
	public String getReportName()
	{
		return csReportName;
	}
	/**
	 * Return value of StatusVector
	 * 
	 * @return Vector
	 */
	public Vector getStatusVector()
	{
		return cvStatusVector;
	}
	/**
	 * Set value of ReportName
	 * 
	 * @param asReportName String
	 */
	public void setReportName(String asReportName)
	{
		csReportName = asReportName;
	}
	/**
	 * Set value of StatusVector
	 * 
	 * @param avStatusVector Vector
	 */
	public void setStatusVector(Vector avStatusVector)
	{
		cvStatusVector = avStatusVector;
	}
}
