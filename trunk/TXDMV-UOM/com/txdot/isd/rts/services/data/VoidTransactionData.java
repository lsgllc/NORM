package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * VoidTransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	10/24/2001	Added TransactionId field
 * B Tulsiani	10/25/2001	Added Selected field
 * K Harrell    12/19/2001	Added ciInventoryIndi
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/14/2007	add csSpclPltRegPltNo, get/set methods 
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/13/2009	Cleanup 
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * This contains the Void Transaction Data 
 *
 * @version	Defect_POS_F	07/13/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		10/24/2001 13:04:30
 */

public class VoidTransactionData implements Serializable
{
	//	boolean 
	protected boolean cbSelected;

	// int
	protected int ciCustSeqNo;
	protected int ciInventoryIndi;
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransPostedMfIndi;
	protected int ciTransTime;
	protected int ciTransWsId;
	protected int ciVoidedTransIndi;
	protected int ciVoidOfcIssuanceNo;
	protected int ciVoidTransAMDate;
	protected int ciVoidTransTime;
	protected int ciVoidTransWsId;

	// String
	protected String csSpclPltRegPltNo;
	protected String csTransactionId;
	protected String csTransCd;
	protected String csTransCdDesc;
	protected String csVIN;
	protected String csVoidTransCd;

	private final static long serialVersionUID = -209409805377608137L;
	
	/**
	 * Returns the value of CustSeqNo
	 * 
	 * @return  int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
	}
	
	/**
	 * Returns the value of InventoryIndi
	 * 
	 * @return int
	 */
	public int getInventoryIndi()
	{
		return ciInventoryIndi;
	}
	
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	
	/**
	 * * Return value of SpclPltRegPltNo
	 * 
	 * @return String
	 */
	public String getSpclPltRegPltNo()
	{
		return csSpclPltRegPltNo;
	}
	
	/**
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	
	/**
	 * Returns the value of TransactionId
	 *  
	 * @return String
	 */
	public String getTransactionId()
	{
		return csTransactionId;
	}
	
	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}
	
	/**
	 * Returns the value of TransCd
	 * @return  String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}
	
	/**
	* Returns the value of TransCdDesc
	* 
	* @return  String 
	*/
	public final String getTransCdDesc()
	{
		return csTransCdDesc;
	}
	
	/**
	 * Returns the value of TransPostedMfIndi
	 * 
	 * @return  int 
	 */
	public final int getTransPostedMfIndi()
	{
		return ciTransPostedMfIndi;
	}
	
	/**
	 * Returns the value of TransTime
	 * 
	 * @return  int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}
	
	/**
	 * Returns the value of TransWsId
	 * 
	 * @return  int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}
	
	/**
	 * Returns the value of VIN
	 * 
	 * @return  String 
	 */
	public final String getVIN()
	{
		return csVIN;
	}
	
	/**
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return  int 
	 */
	public final int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}
	
	/**
	 * Returns the value of VoidOfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getVoidOfcIssuanceNo()
	{
		return ciVoidOfcIssuanceNo;
	}
	
	/**
	 * Returns the value of VoidTransAMDate
	 * 
	 * @return  int 
	 */
	public final int getVoidTransAMDate()
	{
		return ciVoidTransAMDate;
	}
	
	/**
	 * Returns the value of VoidTransCd
	 * 
	 * @return  String 
	 */
	public final String getVoidTransCd()
	{
		return csVoidTransCd;
	}
	
	/**
	 * Returns the value of VoidTransTime
	 * 
	 * @return  int 
	 */
	public final int getVoidTransTime()
	{
		return ciVoidTransTime;
	}
	
	/**
	 * Returns the value of VoidTransWsId
	 * 
	 * @return  int 
	 */
	public final int getVoidTransWsId()
	{
		return ciVoidTransWsId;
	}
	
	/**
	 * Returns the value of Selected
	 * 
	 * @return boolean
	 */
	public boolean isSelected()
	{
		return cbSelected;
	}
	
	/**
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo   int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}
	
	/**
	 * This method sets the value of InventoryIndi
	 * 
	 * @param aiInventoryIndi int
	 */
	public void setInventoryIndi(int aiInventoryIndi)
	{
		ciInventoryIndi = aiInventoryIndi;
	}
	
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	
	/**
	 * This method sets the value of Selected
	 * 
	 * @param abSelected boolean
	 */
	public void setSelected(boolean abSelected)
	{
		cbSelected = abSelected;
	}

	/**
	 * Set value of SpclPltRegPltNo
	 * 
	 * @param asSpclPltRegPltNo
	 */
	public void setSpclPltRegPltNo(String asSpclPltRegPltNo)
	{
		csSpclPltRegPltNo = asSpclPltRegPltNo;
	}
	
	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	
	/**
	 * This method sets the value of TransactionId
	 * 
	 * @param asTransactionId String
	 */
	public void setTransactionId(String asTransactionId)
	{
		csTransactionId = asTransactionId;
	}
	
	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate   int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	
	/**
	 * This method sets the value of TransCd.
	 * 
	 * @param asTransCd   String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
	
	/**
	* This method sets the value of TransCdDesc.
	* 
	* @param asTransCd   String 
	*/
	public final void setTransCdDesc(String asTransCdDesc)
	{
		csTransCdDesc = asTransCdDesc;
	}
	
	/**
	 * This method sets the value of TransPostedMfIndi.
	 * 
	 * @param aiTransPostedMfIndi   int 
	 */
	public final void setTransPostedMfIndi(int aiTransPostedMfIndi)
	{
		ciTransPostedMfIndi = aiTransPostedMfIndi;
	}
	
	/**
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime   int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	
	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId   int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
	
	/**
	 * This method sets the value of VIN.
	 * 
	 * @param asVIN   String 
	 */
	public final void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
	
	/**
	 * This method sets the value of VoidedTransIndi.
	 * 
	 * @param aiVoidedTransIndi   int 
	 */
	public final void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
	
	/**
	 * This method sets the value of VoidOfcIssuanceNo.
	 * 
	 * @param aiVoidOfcIssuanceNo   int 
	 */
	public final void setVoidOfcIssuanceNo(int aiVoidOfcIssuanceNo)
	{
		ciVoidOfcIssuanceNo = aiVoidOfcIssuanceNo;
	}
	
	/**
	 * This method sets the value of VoidTransAMDate.
	 * 
	 * @param aiVoidTransAMDate   int 
	 */
	public final void setVoidTransAMDate(int aiVoidTransAMDate)
	{
		ciVoidTransAMDate = aiVoidTransAMDate;
	}
	
	/**
	 * This method sets the value of VoidTransCd.
	 * 
	 * @param asVoidTransCd   String 
	 */
	public final void setVoidTransCd(String asVoidTransCd)
	{
		csVoidTransCd = asVoidTransCd;
	}
	
	/**
	 * This method sets the value of VoidTransTime.
	 * 
	 * @param aiVoidTransTime   int 
	 */
	public final void setVoidTransTime(int aiVoidTransTime)
	{
		ciVoidTransTime = aiVoidTransTime;
	}
	
	/**
	 * This method sets the value of VoidTransWsId.
	 * 
	 * @param aiVoidTransWsId   int 
	 */
	public final void setVoidTransWsId(int aiVoidTransWsId)
	{
		ciVoidTransWsId = aiVoidTransWsId;
	}
}
