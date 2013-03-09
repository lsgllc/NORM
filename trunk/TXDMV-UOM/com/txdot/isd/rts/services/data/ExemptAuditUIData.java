package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * ExemptAuditUIData.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/27/2006	Created for Exempts Project 
 * 							Version 5.3.0 defect 8900 
 * J Ralph		10/18/2006	Implemented Serializable
 * 							Added serialVersionUID
 * 							Version Exempts defect 8900
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * ExemptAuditUIData
 *
 * @version	Exempts			10/18/2006
 * @author	Kathy Harrell
 * <br>Creation Date:		09/27/2006 17:23:00
 */
public class ExemptAuditUIData implements Serializable
{
	// Object
	private RTSDate caBeginDate;
	private RTSDate caEndDate;

	// Vector 
	private Vector cvSelectedOffices;
	
	static final long serialVersionUID = 7542066333533650939L;
	
	/**
	 * ExemptAuditUIData constructor comment.
	 */
	public ExemptAuditUIData()
	{
		super();

		cvSelectedOffices = new Vector();
	}

	/**
	 * Return the value of BeginDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBeginDate()
	{
		return caBeginDate;
	}

	/**
	 * Return the value of EndDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEndDate()
	{
		return caEndDate;
	}
	/**
	 * Return the value of SelectedOffices
	 * 
	 * @return Vector
	 */
	public Vector getSelectedOffices()
	{
		return cvSelectedOffices;
	}
	/**
	 * Set the value of BeginDate
	 * 
	 * @param aaBeginDate RTSDate
	 */
	public void setBeginDate(RTSDate aaBeginDate)
	{
		caBeginDate = aaBeginDate;
	}
	/**
	 * Set the value of EndDate
	 * 
	 * @param aaEndDate RTSDate
	 */
	public void setEndDate(RTSDate aaEndDate)
	{
		caEndDate = aaEndDate;
	}
	/**
	 * Set the value of SelectedOffices
	 * 
	 * @param avSelectedOffices int
	 */
	public void setSelectedOffices(Vector avSelectedOffices)
	{
		cvSelectedOffices = avSelectedOffices;
	}
}
