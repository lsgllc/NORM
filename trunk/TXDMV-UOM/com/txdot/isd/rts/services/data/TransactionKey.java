package com.txdot.isd.rts.services.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TransactionKey.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	06/14/2010	add getTransId()
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	06/26/2010	add TransactionKey(int,int,int,int) 
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * Data Object for TransactionKey.  This object is used to signal 
 * the server to release the inventory when transaction set is 
 * cancelled.  
 * 
 * @version	6.5.0 		06/26/2010
 * @author	Nancy Ting
 * <br>Creation Date:	11/16/2001 14:24:55  
 */

public class TransactionKey
	implements java.io.Serializable, Displayable
{
	// int 
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransWsId;
	private int ciCustSeqNo;
	private int ciTransTime;

	private int ciCacheAMDate; // SendCache
	private int ciCacheTime; // SendCache

	private final static long serialVersionUID = -3758915527820686870L;
	/**
	 * TransactionKey constructor comment.
	 */
	public TransactionKey()
	{
		super();
	}
	
	/**
	 * TransactionKey constructor comment.
	 */
	public TransactionKey(
		int aiOfcIssuanceNo,
		int aiTransWsId,
		int aiTransAMDate,
		int aiTransTime)
	{
		super();
		ciOfcIssuanceNo = aiOfcIssuanceNo;
		ciTransWsId = aiTransWsId;
		ciTransAMDate = aiTransAMDate;
		ciTransTime = aiTransTime;
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
	 * Return the value of CacheAMDate
	 * 
	 * @return int
	 */
	public int getCacheAMDate()
	{
		return ciCacheAMDate;
	}
	/**
	 * Return the value of CacheTime
	 * 
	 * @return int
	 */
	public int getCacheTime()
	{
		return ciCacheTime;
	}
	/**
	 * Return the value of CustSeqNo 
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}
	/**
	 * Return the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return the value of SubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Return the value of TransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}
	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public String getTransId()
	{
		return UtilityMethods.getTransId(
			ciOfcIssuanceNo,
			ciTransWsId,
			ciTransAMDate,
			ciTransTime);
	}

	/**
	* Returns representaton of CustNoKey 
	* 
	* @return String 
	*/
	public String getCustNoKey()
	{
		return UtilityMethods.addPadding(
			new String[] {
				String.valueOf(ciOfcIssuanceNo),
				String.valueOf(ciTransWsId),
				String.valueOf(ciTransAMDate),
				String.valueOf(ciCustSeqNo)},
			new int[] {
				CommonConstant.LENGTH_OFFICE_ISSUANCENO,
				CommonConstant.LENGTH_TRANS_WSID,
				CommonConstant.LENGTH_TRANSAMDATE,
				CommonConstant.LENGTH_CUSTSEQNO },
			CommonConstant.STR_ZERO);

	}

	/**
	 * Return the value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Return the value of TransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}
	/**
	 * Set the value of CacheAMDate 
	 * 
	 * @param aiCacheAMDate int
	 */
	public void setCacheAMDate(int aiCacheAMDate)
	{
		ciCacheAMDate = aiCacheAMDate;
	}
	/**
	 * Set the value of CacheTime
	 * 
	 * @param aiCacheTime int
	 */
	public void setCacheTime(int aiCacheTime)
	{
		ciCacheTime = aiCacheTime;
	}
	/**
	 * Set the value of CustSeqNo
	 * 
	 * @param aiCustSeqNo int
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}
	/**
	 * Set the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set the value of SubstaId
	 * 
	 * @param aiSubstaId int
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * Set the value of TransAMDate
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	/**
	 * Set the value of TransTime
	 * 
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	/**
	 * Set the value of TransWsId
	 * 
	 * @param aiTransWsId int
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
}
