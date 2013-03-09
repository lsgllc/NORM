package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

/*
 *
 * MFInventoryAllocationData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * MFInventoryAllocationData 
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Charlie Walker
 * <br>Creation Date:	09/20/2001 14:00:00    
 */

public class MFInventoryAllocationData implements Serializable
{
	// boolean
	private boolean cbProcsdInvItm = false;
	private boolean cbCalcInv = false;

	// int
	private int ciSelctdRowIndx;

	//	Object
	private MFInventoryAckData caMFInvAckData =
		new MFInventoryAckData();

	// String
	private String csRptText;
	private String csTransCd = new String("");

	// Vector 
	private Vector cvInvAlloctnData;
	private Vector cvSelctdSubstaIndx;

	private final static long serialVersionUID = 2107557123169563805L;
	/**
	 * MFInventoryAllocationData constructor comment.
	 */
	public MFInventoryAllocationData()
	{
		super();
	}
	/**
	 * Return value of CalcInv
	 * 
	 * @return boolean
	 */
	public boolean getCalcInv()
	{
		return cbCalcInv;
	}
	/**
	 * Return value of InvAlloctnData
	 * 
	 * @return Vector
	 */
	public Vector getInvAlloctnData()
	{
		return cvInvAlloctnData;
	}
	/**
	 * Return value of MFInvAckData
	 * 
	 * @return MFInventoryAckData
	 */
	public MFInventoryAckData getMFInvAckData()
	{
		return caMFInvAckData;
	}
	/**
	 * Return value of ProcsdInvItm
	 *  
	 * @return boolean
	 */
	public boolean getProcsdInvItm()
	{
		return cbProcsdInvItm;
	}
	/**
	 * Return value of RptText
	 * 
	 * @return String
	 */
	public String getRptText()
	{
		return csRptText;
	}
	/**
	 * Return value of SelctdRowIndx
	 * 
	 * @return int
	 */
	public int getSelctdRowIndx()
	{
		return ciSelctdRowIndx;
	}
	/**
	 * Return value of SelctdSubstaIndx
	 * 
	 * @return Vector
	 */
	public Vector getSelctdSubstaIndx()
	{
		return cvSelctdSubstaIndx;
	}
	/**
	 * Return value of TransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * Set value of CalcInv
	 *  
	 * @param abCalcInv boolean
	 */
	public void setCalcInv(boolean abCalcInv)
	{
		cbCalcInv = abCalcInv;
	}
	/**
	 * Set value of InvAlloctnData
	 * 
	 * @param avInvAlloctnData Vector
	 */
	public void setInvAlloctnData(Vector avInvAlloctnData)
	{
		cvInvAlloctnData = avInvAlloctnData;
	}
	/**
	 * Set value of MFInvAckData
	 * 
	 * @param aaMFInvAckData MFInventoryAckData
	 */
	public void setMFInvAckData(MFInventoryAckData aaMFInvAckData)
	{
		caMFInvAckData = aaMFInvAckData;
	}
	/**
	 * Set value of ProcsdInvItm
	 * 
	 * @param abProcsdInvItm boolean
	 */
	public void setProcsdInvItm(boolean abProcsdInvItm)
	{
		cbProcsdInvItm = abProcsdInvItm;
	}
	/**
	 * Set value of RptText
	 * 
	 * @param asRptText String
	 */
	public void setRptText(String asRptText)
	{
		csRptText = asRptText;
	}
	/**
	 * Set value of SelctdRowIndx
	 * 
	 * @param aiSelctdRowIndx int
	 */
	public void setSelctdRowIndx(int aiSelctdRowIndx)
	{
		ciSelctdRowIndx = aiSelctdRowIndx;
	}
	/**
	 * Set value of SelctdSubstaIndx
	 * 
	 * @param avSelctdSubstaIndx Vector
	 */
	public void setSelctdSubstaIndx(Vector avSelctdSubstaIndx)
	{
		cvSelctdSubstaIndx = avSelctdSubstaIndx;
	}
	/**
	 * Set value of TransCd
	 * 
	 * @param asTransCd String
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
}
