package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * FundFunctionTransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/16/2001  Altered Double to Dollar
 * K Harrell   	10/22/2001  Added OfcIssuanceCd for SendTrans
 * K Harrell   	11/11/2001  Corrected typo in VoidedTransIndi
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	05/10/2007	add ItrntTraceNo, get/set methods
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/14/2010	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0    
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * FundFunctionTransactionData  
 * 
 * @version	6.5.0			06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001 10:37:11
 */

public class FundFunctionTransactionData
	implements Serializable, Displayable
{

	protected int ciOfcIssuanceNo;
	protected int ciOfcIssuanceCd; // SendTrans 
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciCustSeqNo;
	protected int ciTransTime;

	// int
	protected int ciAccntNoCd;
	protected int ciApprndComptCntyNo;
	protected int ciCacheTransAMDate; // SendCache
	protected int ciCacheTransTime; // SendCache 
	protected int ciComptCntyNo;
	protected int ciFundsPymntDate;
	protected int ciSubconIssueDate;
	protected int ciSubconId;
	protected int ciTraceNo;
	protected int ciVoidedTransIndi;

	// Object 
	protected Dollar caFundsPymntAmt;

	//	String 
	protected String csTransCd;
	protected String csCkNo;
	protected String csFundsAdjReasn;
	protected String csItrntTraceNo;

	// defect 10505 
	protected String csTransId;
	// end defect 10505 

	private final static long serialVersionUID = -2183283177277959405L;
	/**
	 * Returns the value of AccntNoCd
	 * 
	 * @return  int 
	 */
	public final int getAccntNoCd()
	{
		return ciAccntNoCd;
	}
	/**
	 * Returns the value of ApprndComptCntyNo
	 * 
	 * @return  int 
	 */
	public final int getApprndComptCntyNo()
	{
		return ciApprndComptCntyNo;
	}
	/**
	 * Returns attributes for display in ShowCache
	 * 
	 * @return HashSet
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
		/*
		HashMap hash = new HashMap();
		hash.put("Account No Cd", new Integer(getAccntNoCd()));
		hash.put("Apprnd Compt Cny No", new Integer(getApprndComptCntyNo()));
		hash.put("Check No", getCkNo());
		hash.put("Compt Cnty No", new Integer(getComptCntyNo()));
		hash.put("Cust Seq No", new Integer(getCustSeqNo()));
		hash.put("Funds Adj Reason", getFundsAdjReasn());
		hash.put("Funds Paymnt Amt", new Dollar(getFundsPymntAmt()));
		hash.put("Funds Pymnt Date", new Integer(getFundsPymntDate()));
		hash.put("Ofc Issuance No", new Integer(getOfcIssuanceNo()));
		hash.put("Subcon Id", new Integer(getSubconId()));
		hash.put("Trace No", new Integer(getTraceNo()));
		hash.put("Trans Am Date", new Integer(getTransAMDate()));
		hash.put("Trans Cd", getTransCd());
		hash.put("Trans Time", new Integer(getTransTime()));
		hash.put("Trans WS ID", new Integer(getTransWsId()));
		hash.put("Voided Trans Indi", new Integer(getVoidedTransIndi()));
		return hash;
		*/
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
	 * Returns the value of CkNo
	 * 
	 * @return  String 
	 */
	public final String getCkNo()
	{
		return csCkNo;
	}
	/**
	 * Returns the value of ComptCntyNo
	 * 
	 * @return  int 
	 */
	public final int getComptCntyNo()
	{
		return ciComptCntyNo;
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
	 * Returns the value of FundsAdjReasn
	 * 
	 * @return  String 
	 */
	public final String getFundsAdjReasn()
	{
		return csFundsAdjReasn;
	}
	/**
	 * Returns the value of FundsPymntAmt
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getFundsPymntAmt()
	{
		return caFundsPymntAmt;
	}
	/**
	 * Returns the value of FundsPymntDate
	 * 
	 * @return  int 
	 */
	public final int getFundsPymntDate()
	{
		return ciFundsPymntDate;
	}

	/**
	 * Returns the ItrntTraceNo.
	 * 
	 * @return String
	 */
	public final String getItrntTraceNo()
	{
		return csItrntTraceNo;
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
	 * Returns the value of SubconId
	 * 
	 * @return  int 
	 */
	public final int getSubconId()
	{
		return ciSubconId;
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
	 * Returns the value of TraceNo
	 * 
	 * @return  int 
	 */
	public final int getTraceNo()
	{
		return ciTraceNo;
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
	 * This method sets the value of AccntNoCd.
	 * 
	 * @param aiAccntNoCd   int 
	 */
	public final void setAccntNoCd(int aiAccntNoCd)
	{
		ciAccntNoCd = aiAccntNoCd;
	}
	/**
	 * This method sets the value of ApprndComptCntyNo.
	 * 
	 * @param aiApprndComptCntyNo   int 
	 */
	public final void setApprndComptCntyNo(int aiApprndComptCntyNo)
	{
		ciApprndComptCntyNo = aiApprndComptCntyNo;
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
	 * This method sets the value of CkNo.
	 * 
	 * @param asCkNo   String 
	 */
	public final void setCkNo(String asCkNo)
	{
		csCkNo = asCkNo;
	}
	/**
	 * This method sets the value of ComptCntyNo.
	 * 
	 * @param aiComptCntyNo   int 
	 */
	public final void setComptCntyNo(int aiComptCntyNo)
	{
		ciComptCntyNo = aiComptCntyNo;
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
	 * This method sets the value of FundsAdjReasn.
	 * 
	 * @param asFundsAdjReasn   String 
	 */
	public final void setFundsAdjReasn(String asFundsAdjReasn)
	{
		csFundsAdjReasn = asFundsAdjReasn;
	}
	/**
	 * This method sets the value of FundsPymntAmt.
	 * 
	 * @param aaFundsPymntAmt   Dollar 
	 */
	public final void setFundsPymntAmt(Dollar aaFundsPymntAmt)
	{
		caFundsPymntAmt = aaFundsPymntAmt;
	}
	/**
	 * This method sets the value of FundsPymntDate.
	 * 
	 * @param aiFundsPymntDate   int 
	 */
	public final void setFundsPymntDate(int aiFundsPymntDate)
	{
		ciFundsPymntDate = aiFundsPymntDate;
	}

	/**
	 * Set the ItrntTraceNo.
	 * 
	 * @param asItrntTraceNo String
	 */
	public final void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
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
	 * This method sets the value of SubconId.
	 * 
	 * @param aiSubconId   int 
	 */
	public final void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
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
	 * This method sets the value of TraceNo.
	 * 
	 * @param aiTraceNo   int 
	 */
	public final void setTraceNo(int aiTraceNo)
	{
		ciTraceNo = aiTraceNo;
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
	  * Set AbstractValue of TransId
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
