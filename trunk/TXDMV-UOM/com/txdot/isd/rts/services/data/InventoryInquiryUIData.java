package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * InventoryInquiryUIData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 * 							delete ciInvInqSelection, csSearchType 
 * 							delete getInvInqSelection(), 
 * 							setInvInqSelection(), getSearchType(),
 * 							setSearchType()
 *							defect 7899 Ver 5.2.3 
 * Ray Rowehl	02/12/2007	Remove methods moved down to 
 * 							InventoryAllocationData.
 * 							delete csInvcNo
 * 							delete getInvcNo(), setInvcNo()
 * 							defect 9116 Ver Special Plates
 * Min Wang		02/27/2007	add cbVI for Virtual Inventory report.
 * 							add isVI(), setVI()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/18/2007	Remove cbVI.  Use cbVirtual from IAD.
 * 							defect 9116 Ver Special Plates
 * K Harrell	08/29/2008	add cbAllItmsSelected, isAllItmsSelected(),
 * 							 setAllItmsSelected()
 * 							defect 9706 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * InventoryInquiryUIData
 * 
 * @version	Defect_POS_B	08/29/2008 
 * @author	Charlie Walker
 * <br>Creation Date: 		09/20/2001 09:31:09    
 */

public class InventoryInquiryUIData
	extends InventoryAllocationData
	implements Serializable
{
	// defect 9706 
	// boolean 
	private boolean cbAllItmsSelected;
	// end defect 9706 

	// int
	private int ciExceptionReport;

	// Object  
	private RTSDate caBeginDt;
	private RTSDate caEndDt;

	// String 
	private String csInvIdName;
	private String csInvInqBy;
	private String csItmCdDesc;
	private String csOfcType;
	private String csRptType;

	// Vector 
	private Vector cvInvIds;
	private Vector cvInvItmYrs;
	private Vector cvItmCds;

	// Boolean

	private final static long serialVersionUID = -2485761982525688009L;

	/**
	 * InventoryInquiryUIData constructor comment.
	 */
	public InventoryInquiryUIData()
	{
		super();
	}

	/** 
	 * Return value of AllItems
	 * 
	 * @return boolean 
	 */
	public boolean isAllItmsSelected()
	{
		return cbAllItmsSelected;
	}

	/**
	 * Return value of BeginDt
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBeginDt()
	{
		return caBeginDt;
	}

	/**
	 * Return value of EndDt
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEndDt()
	{
		return caEndDt;
	}

	/**
	 * Return value of ExceptionReport
	 * 
	 * @return int
	 */
	public int getExceptionReport()
	{
		return ciExceptionReport;
	}

	/**
	 * Return value of InvIdName
	 *  
	 * @return String
	 */
	public String getInvIdName()
	{
		return csInvIdName;
	}

	/**
	 * Return value of InvIds
	 * 
	 * @return Vector
	 */
	public Vector getInvIds()
	{
		return cvInvIds;
	}

	/**
	 * Return value of InvInqBy
	 * 
	 * @return String
	 */
	public String getInvInqBy()
	{
		return csInvInqBy;
	}

	/**
	 * Return value of InvItmYrs
	 * 
	 * @return Vector
	 */
	public Vector getInvItmYrs()
	{
		return cvInvItmYrs;
	}

	/**
	 * Return value of ItmCdDesc
	 * 
	 * @return String
	 */
	public String getItmCdDesc()
	{
		return csItmCdDesc;
	}

	/**
	 * Return value of ItmCds
	 * 
	 * @return Vector
	 */
	public Vector getItmCds()
	{
		return cvItmCds;
	}

	/**
	 * Return value of OfcType
	 * 
	 * @return String
	 */
	public String getOfcType()
	{
		return csOfcType;
	}

	/**
	 * Return value of RptType
	 * 
	 * @return String
	 */
	public String getRptType()
	{
		return csRptType;
	}

	/**
	 * Set value of AllItems
	 * 
	 * @param abAllItmsSelected
	 */
	public void setAllItmsSelected(boolean abAllItmsSelected)
	{
		cbAllItmsSelected = abAllItmsSelected;
	}

	/**
	 * Set value of BeginDt
	 * 
	 * @param aaBeginDt RTSDate
	 */
	public void setBeginDt(RTSDate aaBeginDt)
	{
		caBeginDt = aaBeginDt;
	}

	/**
	 * Set value of EndDate
	 * 
	 * @param aaEndDt RTSDate
	 */
	public void setEndDt(RTSDate aaEndDt)
	{
		caEndDt = aaEndDt;
	}

	/**
	 * Set value of ExceptionReport
	 * 
	 * @param aiExceptionReport int
	 */
	public void setExceptionReport(int aiExceptionReport)
	{
		ciExceptionReport = aiExceptionReport;
	}

	/**
	 * Set value of InvIdName
	 * 
	 * @param asInvIdName String
	 */
	public void setInvIdName(String asInvIdName)
	{
		csInvIdName = asInvIdName;
	}

	/**
	 * Set value of InvIds
	 * 
	 * @param avInvIds Vector
	 */
	public void setInvIds(Vector avInvIds)
	{
		cvInvIds = avInvIds;
	}

	/**
	 * Set value of InvInqBy
	 * 
	 * @param asInvInqBy String
	 */
	public void setInvInqBy(String asInvInqBy)
	{
		csInvInqBy = asInvInqBy;
	}

	/**
	 * Set value of InvItmYrs
	 * 
	 * @param avInvItmYrs Vector
	 */
	public void setInvItmYrs(Vector avInvItmYrs)
	{
		cvInvItmYrs = avInvItmYrs;
	}

	/**
	 * Set value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}

	/**
	 * Set value of ItmCds
	 * 
	 * @param avItmCds Vector
	 */
	public void setItmCds(Vector avItmCds)
	{
		cvItmCds = avItmCds;
	}

	/**
	 * Set value of OfcType
	 * 
	 * @param asOfcType String
	 */
	public void setOfcType(String asOfcType)
	{
		csOfcType = asOfcType;
	}

	/**
	 * Set value of RptType
	 * 
	 * @param asRptType String
	 */
	public void setRptType(String asRptType)
	{
		csRptType = asRptType;
	}
}
