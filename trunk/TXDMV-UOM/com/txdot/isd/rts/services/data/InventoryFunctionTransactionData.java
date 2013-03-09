package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * InventoryFunctionTransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   10/18/2001  Added OfcIssuanceCd,TransEmpId for SendTrans
 * K. Harrell   10/22/2001  Renamed TransEmpId to EmpId
 * 							added ciQtyReprnted, ciTotQ
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * InventoryFunctionTransactionData
 * 
 * @version	6.5.0 		06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date: 	09/21/2001 10:13:40 
 */

public class InventoryFunctionTransactionData
	implements Serializable, Displayable
{
	// int 
	protected int ciOfcIssuanceNo;
	protected int ciOfcIssuanceCd; // SendTrans 
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciCustSeqNo;
	protected int ciTransTime;

	protected int ciCacheTransAMDate; // SendCache
	protected int ciCacheTransTime; // SendCache 
	protected int ciInvcCorrIndi;
	protected int ciSubconIssueDate;
	protected int ciVoidedTransIndi;

	// String 
	protected String csEmpId;
	protected String csInvcNo;
	protected String csTransCd;

	// defect 10505
	protected String csTransId;
	// end defect 10505 

	private final static long serialVersionUID = 7159548457669586110L;
	/**
	 * Determine field attributes  
	 * 
	 * @return java.util.Map
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap lhmHash = new java.util.HashMap();
		Field[] arrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < arrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					arrFields[i].getName(),
					arrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;

	}
	/**
	 * Return value of CacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}
	/**
	 * Return value of CacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}
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
	* Returns the value of TransCd
	* 
	* @return  String 
	*/
	public final String getEmpId()
	{
		return csEmpId;
	}
	/**
	 * Returns the value of InvcCorrIndi
	 * 
	 * @return  int 
	 */
	public final int getInvcCorrIndi()
	{
		return ciInvcCorrIndi;
	}
	/**
	 * Returns the value of InvcNo
	 * 
	 * @return  String 
	 */
	public final String getInvcNo()
	{
		return csInvcNo;
	}
	/**
	* Returns the value of OfcIssuanceCd
	* 
	* @return  int 
	*/
	public final int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
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
	 * Returns the value of SubconIssueDate
	 * 
	 * @return  int 
	 */
	public final int getSubconIssueDate()
	{
		return ciSubconIssueDate;
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
	 * 
	 * @return  String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}
	
	/**
	 * Returns the value of TransId
	 * 
	 * @return String 
	 */
	public String getTransId()
	{
		if (csTransId == null || csTransId.trim().length() == 0)
		{
			csTransId =
				UtilityMethods.getTransId(
					ciOfcIssuanceNo,
					ciTransWsId,
					ciTransAMDate,
					ciTransTime);
		}
		return csTransId;
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
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return  int 
	 */
	public final int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}
	/**
	 * Return value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}
	/**
	 * Return value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
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
	* This method sets the value of EmpId.
	* 
	* @param asEmpId   String 
	*/
	public final void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}
	/**
	 * This method sets the value of InvcCorrIndi.
	 * 
	 * @param aiInvcCorrIndi   int 
	 */
	public final void setInvcCorrIndi(int aiInvcCorrIndi)
	{
		ciInvcCorrIndi = aiInvcCorrIndi;
	}
	/**
	 * This method sets the value of InvcNo.
	 * 
	 * @param asInvcNo   String 
	 */
	public final void setInvcNo(String asInvcNo)
	{
		csInvcNo = asInvcNo;
	}
	/**
	* This method sets the value of OfcIssuanceCd.
	* 
	* @param aiOfcIssuanceCd   int 
	*/
	public final void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
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
	 * This method sets the value of SubconIssueDate.
	 * 
	 * @param aiSubconIssueDate   int 
	 */
	public final void setSubconIssueDate(int aiSubconIssueDate)
	{
		ciSubconIssueDate = aiSubconIssueDate;
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
	 * Sets the value of TransId
	 * 
	 */
	public void setTransId(String asTransId)
	{
	 csTransId = asTransId;
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
	 * This method sets the value of VoidedTransIndi.
	 * 
	 * @param aiVoidedTransIndi   int 
	 */
	public final void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
}
