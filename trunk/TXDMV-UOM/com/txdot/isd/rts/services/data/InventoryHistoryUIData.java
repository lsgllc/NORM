package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * InventoryHistoryUIData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	08/29/2008	add cbAllItmsSelected,isAllItmsSelected(),
 * 							 setAllItmsSelected()
 * 							delete ciAllItmSelected, get/set methods
 * 							defect 9706 Ver Defect_POS_B 
 * K Harrell	10/12/2009	add ciRequestOfcIssuanceNo, get/set methods
 * 							defect 10207 Ver Defect_POS_G
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * InventoryHistoryLogData
 * 
 * @version	Defect_POS_G	10/12/2009
 * @author	Administrator
 * <br>Creation Date: 		11/27/2001 10:52:00   
 */

public class InventoryHistoryUIData implements Serializable
{

	private final static long serialVersionUID = 3526307426725845429L;

	// Object
	private RTSDate caBeginDate;
	private RTSDate caEndDate;
	// defect 9706 
	// boolean
	private boolean cbAllItmsSelected;
	//private int ciAllItmSelected;
	// end defect 9706 

	// int
	private int ciDateRangeIndi;
	private int ciDeleteHisReportIndi;
	private int ciInvoiceIndi;
	private int ciReceiveHisReportIndi;
	private int ciRequestOfcIssuanceNo;

	// String
	private String csInvoiceNo;

	// Vector 
	private Vector cvInvItems;
	private Vector cvSelectedCounties;
	
	/**
	 * InventoryHistoryUIData constructor comment.
	 */
	public InventoryHistoryUIData()
	{
		super();

		cvSelectedCounties = new Vector();
		cvInvItems = new Vector();
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
	 * Return the value of DateRangeIndi
	 * 
	 * @return int
	 */
	public int getDateRangeIndi()
	{
		return ciDateRangeIndi;
	}

	/**
	 * Return the value of DeleteHisReportIndi
	 * 
	 * @return int
	 */
	public int getDeleteHisReportIndi()
	{
		return ciDeleteHisReportIndi;
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
	 * Return the value of InvItems
	 * 
	 * @return Vector
	 */
	public Vector getInvItems()
	{
		return cvInvItems;
	}

	/**
	 * Return the value of InvoiceIndi
	 * 
	 * @return int
	 */
	public int getInvoiceIndi()
	{
		return ciInvoiceIndi;
	}

	/**
	 * Return the value of InvoiceNo
	 * 
	 * @return String
	 */
	public String getInvoiceNo()
	{
		return csInvoiceNo;
	}

	/**
	 * Return the value of ReceiveHisReportIndi
	 * 
	 * @return int
	 */
	public int getReceiveHisReportIndi()
	{
		return ciReceiveHisReportIndi;
	}

	/**
	 * Get the value of RequestOfcissuanceNo
	 * 
	 * @return int
	 */
	public int getRequestOfcIssuanceNo()
	{
		return ciRequestOfcIssuanceNo;
	}

	/**
	 * Return the value of SelectedCounties
	 * 
	 * @return Vector
	 */
	public Vector getSelectedCounties()
	{
		return cvSelectedCounties;
	}

	/**
	 * Return the value of AllItmsSelected
	 * 
	 * @return boolean
	 */
	public boolean isAllItmsSelected()
	{
		return cbAllItmsSelected;
	}

	/**
	 * Set the value of AllItmsSelected
	 * 
	 * @param abAllItmsSelected boolean
	 */
	public void setAllItmsSelected(boolean abAllItmsSelected)
	{
		cbAllItmsSelected = abAllItmsSelected;
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
	 * Set the value of DateRangeIndi
	 * 
	 * @param aiDateRangeIndi int
	 */
	public void setDateRangeIndi(int aiDateRangeIndi)
	{
		ciDateRangeIndi = aiDateRangeIndi;
	}

	/**
	 * Set the value of DeleteHisReportIndi
	 * 
	 * @param aiDeleteHisReportIndi int
	 */
	public void setDeleteHisReportIndi(int aiDeleteHisReportIndi)
	{
		ciDeleteHisReportIndi = aiDeleteHisReportIndi;
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
	 * Set the value of InvItems
	 * 
	 * @param avInvItems Vector
	 */
	public void setInvItems(Vector avInvItems)
	{
		cvInvItems = avInvItems;
	}

	/**
	 * Set the value of InvoiceIndi
	 * 
	 * @param aiInvoiceIndi int
	 */
	public void setInvoiceIndi(int aiInvoiceIndi)
	{
		ciInvoiceIndi = aiInvoiceIndi;
	}

	/**
	 * Set the value of InvoiceNo
	 * 
	 * @param asInvoiceNo String
	 */
	public void setInvoiceNo(String asInvoiceNo)
	{
		csInvoiceNo = asInvoiceNo;
	}

	/**
	 * Set the value of ReceiveHisReportIndi
	 * 
	 * @param aiReceiveHisReportIndi int
	 */
	public void setReceiveHisReportIndi(int aiReceiveHisReportIndi)
	{
		ciReceiveHisReportIndi = aiReceiveHisReportIndi;
	}

	/**
	 * Set the value of RequestOfcissuanceNo
	 * 
	 * @param aiRequestOfcIssuanceNo
	 */
	public void setRequestOfcIssuanceNo(int aiRequestOfcIssuanceNo)
	{
		ciRequestOfcIssuanceNo = aiRequestOfcIssuanceNo;
	}

	/**
	 * Set the value of SelectedCounties
	 * 
	 * @param avSelectedCounties int
	 */
	public void setSelectedCounties(Vector avSelectedCounties)
	{
		cvSelectedCounties = avSelectedCounties;
	}

}
