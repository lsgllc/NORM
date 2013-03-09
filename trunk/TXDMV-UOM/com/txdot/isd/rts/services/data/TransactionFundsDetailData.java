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
 * TransactionFundsDetailData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/15/2001  Altered Double to Dollar
 * K Harrell   	11/06/2001  Added CrdtAllowdIndi, AcctItmCdDesc
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	02/16/2010	Removed previously added columns 
 * 							defect 10366 Ver POS_640  
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * TransactionFundsDetailData
 *
 * @version	6.5.0 		06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	09/21/2001 12:59:33 
 */

public class TransactionFundsDetailData
	implements Serializable, Displayable
{
	// int 
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciCustSeqNo;
	protected int ciTransTime;

	protected int ciCacheTransAMDate; // SendCache
	protected int ciCacheTransTime; // SendCache 
	protected int ciCashDrawerIndi;
	protected int ciCrdtAllowdIndi;
	protected int ciFundsRptDate;
	protected int ciItmQty;
	protected int ciRptngDate;

	//	Object
	protected Dollar caFundsRcvdAmt;
	protected Dollar caItmPrice;

	// String
	protected String csAcctItmCd;
	protected String csAcctItmCdDesc;
	protected String csFundsCat;
	protected String csFundsRcvngEnt;
	protected String csTransCd;

	// defect 10505 
	protected String csTransId;
	// end defect 10505

	private final static long serialVersionUID = 517725415970542930L;

	/**
	 * Returns the value of AcctItmCd
	 * 
	 * @return  String 
	 */
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	* Returns the value of AcctItmCdDesc
	* 
	* @return  String 
	*/
	public final String getAcctItmCdDesc()
	{
		return csAcctItmCdDesc;
	}

	/**
	 * Method used to return field attributes
	 * 
	 * @return Map
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
	}

	/**
	 * Return the value of CacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Return the value of CacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Returns the value of CashDrawerIndi
	 * 
	 * @return  int 
	 */
	public final int getCashDrawerIndi()
	{
		return ciCashDrawerIndi;
	}

	/**
	* Returns the value of CrdtAllowdIndi
	* 
	* @return  int 
	*/
	public final int getCrdtAllowdIndi()
	{
		return ciCrdtAllowdIndi;
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
	 * Returns the value of FundsCat
	 * 
	 * @return  String 
	 */
	public final String getFundsCat()
	{
		return csFundsCat;
	}

	/**
	 * Returns the value of FundsRcvdAmt
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getFundsRcvdAmt()
	{
		return caFundsRcvdAmt;
	}

	/**
	 * Returns the value of FundsRcvngEnt
	 * 
	 * @return  String 
	 */
	public final String getFundsRcvngEnt()
	{
		return csFundsRcvngEnt;
	}

	/**
	 * Returns the value of FundsRptDate
	 * 
	 * @return  int 
	 */
	public final int getFundsRptDate()
	{
		return ciFundsRptDate;
	}

	/**
	 * Returns the value of ItmPrice
	 * 
	 * @return  double 
	 */
	public final Dollar getItmPrice()
	{
		return caItmPrice;
	}

	/**
	 * Returns the value of ItmQty
	 * 
	 * @return  int 
	 */
	public final int getItmQty()
	{
		return ciItmQty;
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
	 * Returns the value of RptngDate
	 * 
	 * @return  int 
	 */
	public final int getRptngDate()
	{
		return ciRptngDate;
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
	 * This method sets the value of AcctItmCd.
	 * 
	 * @param asAcctItmCd   String 
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * This method sets the value of AcctItmCdDesc.
	 * 
	 * @param asAcctItmCdDesc   String 
	 */
	public final void setAcctItmCdDesc(String asAcctItmCdDesc)
	{
		csAcctItmCdDesc = asAcctItmCdDesc;
	}

	/**
	 * Return the value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Return the value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * This method sets the value of CashDrawerIndi.
	 * 
	 * @param aiCashDrawerIndi   int 
	 */
	public final void setCashDrawerIndi(int aiCashDrawerIndi)
	{
		ciCashDrawerIndi = aiCashDrawerIndi;
	}

	/**
	 * This method sets the value of CrdtAllowdIndi.
	 * 
	 * @param aiCrdtAllowdIndi   int 
	 */
	public final void setCrdtAllowdIndi(int aiCrdtAllowdIndi)
	{
		ciCrdtAllowdIndi = aiCrdtAllowdIndi;
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
	 * This method sets the value of FundsCat.
	 * 
	 * @param asFundsCat   String 
	 */
	public final void setFundsCat(String asFundsCat)
	{
		csFundsCat = asFundsCat;
	}

	/**
	 * This method sets the value of FundsRcvdAmt.
	 * 
	 * @param aaFundsRcvdAmt  Dollar 
	 */
	public final void setFundsRcvdAmt(Dollar aaFundsRcvdAmt)
	{
		caFundsRcvdAmt = aaFundsRcvdAmt;
	}

	/**
	 * This method sets the value of FundsRcvngEnt.
	 * 
	 * @param asFundsRcvngEnt   String 
	 */
	public final void setFundsRcvngEnt(String asFundsRcvngEnt)
	{
		csFundsRcvngEnt = asFundsRcvngEnt;
	}

	/**
	 * This method sets the value of FundsRptDate.
	 * 
	 * @param aiFundsRptDate   int 
	 */
	public final void setFundsRptDate(int aiFundsRptDate)
	{
		ciFundsRptDate = aiFundsRptDate;
	}

	/**
	 * This method sets the value of ItmPrice.
	 * 
	 * @param aaItmPrice   Dollar 
	 */
	public final void setItmPrice(Dollar aaItmPrice)
	{
		caItmPrice = aaItmPrice;
	}

	/**
	 * This method sets the value of ItmQty.
	 * 
	 * @param aiItmQty   int 
	 */
	public final void setItmQty(int aiItmQty)
	{
		ciItmQty = aiItmQty;
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
	 * This method sets the value of RptngDate.
	 * 
	 * @param aiRptngDate   int 
	 */
	public final void setRptngDate(int aiRptngDate)
	{
		ciRptngDate = aiRptngDate;
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
	 * @param asTransid  
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
}
