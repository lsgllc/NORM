package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * FundsReportData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Data Object contains all the report criteria selected from screens
 * FUN006 and FUN007.
 * 
 * @version	5.2.3		05/19/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59   
 */

public class FundsReportData implements Serializable
{

	// boolean
	private boolean cbDisplayReports;
	private boolean cbShowSourceMoney;

	// int 
	private int ciEntity;
	private int ciPrimarySplit;
	private int ciRange;

	// Object
	private RTSDate caFromRange;
	private RTSDate caToRange;

	// Vector
	private Vector cvReportNames;

	private final static long serialVersionUID = -2156450545466468012L;
	/**
	 * FundsReportData constructor comment.
	 */
	public FundsReportData()
	{
		super();
	}
	/**
	 * Returns the value of Entity
	 * 
	 * @return int
	 */
	public int getEntity()
	{
		return ciEntity;
	}
	/**
	 * Returns the value of FromRange
	 * 
	 * @return RTSDate
	 */
	public RTSDate getFromRange()
	{
		return caFromRange;
	}
	/**
	 * Returns the value of PrimarySplit
	 * 
	 * @return int
	 */
	public int getPrimarySplit()
	{
		return ciPrimarySplit;
	}
	/**
	 * Returns the value of Range 
	 * 
	 * @return int
	 */
	public int getRange()
	{
		return ciRange;
	}
	/**
	 * Returns the value of ReportNames 
	 * 
	 * @return Vector
	 */
	public Vector getReportNames()
	{
		return cvReportNames;
	}
	/**
	 * Returns the value of ToRange
	 * 
	 * @return RTSDate
	 */
	public RTSDate getToRange()
	{
		return caToRange;
	}
	/**
	 * Returns the value of DisplayReports 
	 * 
	 * @return boolean
	 */
	public boolean isDisplayReports()
	{
		return cbDisplayReports;
	}
	/**
	 * Returns the value of ShowSourceMoney 
	 * 
	 * @return boolean
	 */
	public boolean isShowSourceMoney()
	{
		return cbShowSourceMoney;
	}
	/**
	 * Sets the value of DisplayReports  
	 * 
	 * @param abDisplayReports boolean
	 */
	public void setDisplayReports(boolean abDisplayReports)
	{
		cbDisplayReports = abDisplayReports;
	}
	/**
	 * Sets the value of Entity
	 * 
	 * @param aiEntity int
	 */
	public void setEntity(int aiEntity)
	{
		ciEntity = aiEntity;
	}
	/**
	 * Sets the value of FromRange
	 * 
	 * @param aaFromRange RTSDate
	 */
	public void setFromRange(RTSDate aaFromRange)
	{
		caFromRange = aaFromRange;
	}
	/**
	 * Sets the value of PrimarySplit 
	 * 
	 * @param aiPrimarySplit int
	 */
	public void setPrimarySplit(int aiPrimarySplit)
	{
		ciPrimarySplit = aiPrimarySplit;
	}
	/**
	 * Sets the value of Range 
	 * 
	 * @param aiRange int
	 */
	public void setRange(int aiRange)
	{
		ciRange = aiRange;
	}
	/**
	 * Sets the value of ReportNames
	 * 
	 * @param avReportNames Vector
	 */
	public void setReportNames(Vector avReportNames)
	{
		cvReportNames = avReportNames;
	}
	/**
	 * Sets the value of ShowSourceMoney 
	 * 
	 * @param abShowSourceMoney boolean
	 */
	public void setShowSourceMoney(boolean abShowSourceMoney)
	{
		cbShowSourceMoney = abShowSourceMoney;
	}
	/**
	 * Sets the value of ToRange 
	 * 
	 * @param aaToRange RTSDate
	 */
	public void setToRange(RTSDate aaToRange)
	{
		caToRange = aaToRange;
	}
}
