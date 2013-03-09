package com.txdot.isd.rts.services.data;

import java.util.Vector;

/*
 * ElectronicTitleUIData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/12/2009	Created 
 * 							defect 9769 Ver Defect_POS_E 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * ElectronicTitleHistoryUIData
 *
 * @version	Defect_POS_E	03/12/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		03/12/2009
 */
public class ElectronicTitleHistoryUIData extends ExemptAuditUIData
{
	//	Vector 
	private Vector cvSelectedPermLienhldrId;
	private Vector cvSelectedTransCd;

	/**
	 * ElectronicTitleHistoryUIData constructor comment.
	 */
	public ElectronicTitleHistoryUIData()
	{
		super();

		cvSelectedPermLienhldrId = new Vector();
		cvSelectedTransCd = new Vector();
	}

	/**
	 * Get value of cvSelectedPermLienhldrId
	 * 
	 * @return Vector
	 */
	public Vector getSelectedPermLienhldrId()
	{
		return cvSelectedPermLienhldrId;
	}

	/**
	 * Get value of cvSelectedTransCd
	 * 
	 * @return Vector
	 */
	public Vector getSelectedTransCd()
	{
		return cvSelectedTransCd;
	}

	/**
	 * Set value of cvSelectedPermLienhldrId
	 * 
	 * @param avSelectedPermLienhldrId
	 */
	public void setSelectedPermLienhldrId(Vector avSelectedPermLienhldrId)
	{
		cvSelectedPermLienhldrId = avSelectedPermLienhldrId;
	}

	/**
	 * Set value of cvSelectedTransCd
	 * 
	 * @param avSelectedTransCd
	 */
	public void setSelectedTransCd(Vector avSelectedTransCd)
	{
		cvSelectedTransCd = avSelectedTransCd;
	}
}
