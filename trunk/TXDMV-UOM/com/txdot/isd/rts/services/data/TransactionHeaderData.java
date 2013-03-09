package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TransactionHeaderData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K.Harrell    12/11/2001  Removed BranchOfcId
 * K Harrell	10/04/2004	Add ciCacheTransAMDate,ciCacheTransTime
 *							and associated get/set methods
 *							defect 7586 Ver 5.2.1
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	01/30/2009	Add boolean/get/set methods to manage 
 * 							 enable/disable of Cancel button on Pending
 * 							 Trans Panel when restoring set-aside. 
 * 							add cbCancelTrans
 * 							add isCancelTrans(), setCancelTrans()   
 * 							defect 7717 Ver Defect POS_D
 * K Harrell 	06/14/2010 	add csCustNoKey, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	09/29/2010 	modify setTransName() 
 * 							defect 10616 Ver 6.5.0
 * K Harrell	01/29/2011	add cbWebAgntTrans, is/set methods 
 * 							defect 10734 Ver 6.7.0  
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * TransactionHeaderData
 *
 * @version	6.7.0			01/29/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001
 */
public class TransactionHeaderData implements Serializable, Displayable
{
	// boolean 
	private boolean cbCancelTrans;
	private boolean cbSubconTrans;
	
	// defect 10734 
	private boolean cbWebAgntTrans;
	// end defect 10734  

	// int 
	private int ciCacheTransAMDate; // SendCache
	private int ciCacheTransTime; // SendCache 
	private int ciCashWsId;
	private int ciCustSeqNo;
	private int ciDbTypeIndi;
	private int ciFeeSourceCd;
	private int ciOfcIssuanceCd;
	private int ciOfcIssuanceNo;
	private int ciPrintImmediate;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;
	private int ciVersionCd;

	// String 
	private String csCustNoKey;
	private String csTransEmpId;
	private String csTransName;

	//	Object 
	private RTSDate cTransTimestmp;
	
	// Constant 
	public static final int TIMESTMP = 0;
	public static final int TRANS_NAME = 1;


	
	private final static long serialVersionUID = -1854200168018736198L;

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
	 * Returns the value of CashWsId
	 * 
	 * @return  int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * Returns the value of CustNoKey
	 * 
	 * @return String 
	 */
	public String getCustNoKey()
	{
		if (csCustNoKey == null || csCustNoKey.trim().length() == 0)
		{
			csCustNoKey =
				UtilityMethods.addPadding(
					new String[] {
						String.valueOf(ciOfcIssuanceNo),
						String.valueOf(ciTransWsId),
						String.valueOf(ciTransAMDate),
						String.valueOf(ciCustSeqNo)},
					new int[] {
						CommonConstant.LENGTH_OFFICE_ISSUANCENO,
						CommonConstant.LENGTH_TRANS_WSID,
						CommonConstant.LENGTH_TRANSAMDATE,
						CommonConstant.LENGTH_CUSTSEQNO},
					CommonConstant.STR_ZERO);

		}
		return csCustNoKey;
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
	 * Return the value of DbTypeIndi
	 * 
	 * @return int
	 */
	public int getDbTypeIndi()
	{
		return ciDbTypeIndi;
	}
	/**
	 * Returns the value of FeeSourceCd
	 * 
	 * @return  int 
	 */
	public final int getFeeSourceCd()
	{
		return ciFeeSourceCd;
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
	 * Returns the value of PrintImmediate
	 * 
	 * @return  int 
	 */
	public final int getPrintImmediate()
	{
		return ciPrintImmediate;
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
	 * Returns the value of TransEmpId
	 * 
	 * @return  String 
	 */
	public final String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Returns the value of TransName
	 * 
	 * @return  String 
	 */
	public final String getTransName()
	{
		return csTransName;
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
	 * Returns the value of TransTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getTransTimestmp()
	{
		return cTransTimestmp;
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
	 * Returns the value of VersionCd
	 * 
	 * @return  int 
	 */
	public final int getVersionCd()
	{
		return ciVersionCd;
	}
	/**
	 * This method returns the value of CancelTrans.
	 * 
	 * @return
	 */
	public boolean isCancelTrans()
	{
		return cbCancelTrans;
	}
	/**
	 * Return the value of SubconTrans
	 * 
	 * @return boolean
	 */
	public boolean isSubconTrans()
	{
		return cbSubconTrans;
	}

	/**
	 * Return value of cbWebAgntTrans
	 * 
	 * @return boolean 
	 */
	public boolean isWebAgntTrans()
	{
		return cbWebAgntTrans;
	}
	/**
	 * Set the value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}
	/**
	 * Set the value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * This method sets the value of CancelTrans.
	 * 
	 * @param abCancelTrans
	 */
	public void setCancelTrans(boolean abCancelTrans)
	{
		cbCancelTrans = abCancelTrans;
	}
	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId   int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * Sets the value of CustNoKey
	 * 
	 * @param asCustNoKey
	 */
	public void setCustNoKey(String asCustNoKey)
	{
		csCustNoKey = asCustNoKey;
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
	 * Set the value of DbTypeIndi
	 * 
	 * @param aiDbTypeIndi int
	 */
	public void setDbTypeIndi(int aiDbTypeIndi)
	{
		ciDbTypeIndi = aiDbTypeIndi;
	}
	/**
	 * This method sets the value of FeeSourceCd.
	 * 
	 * @param aiFeeSourceCd   int 
	 */
	public final void setFeeSourceCd(int aiFeeSourceCd)
	{
		ciFeeSourceCd = aiFeeSourceCd;
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
	 * This method sets the value of PrintImmediate.
	 * 
	 * @param aiPrintImmediate   int 
	 */
	public final void setPrintImmediate(int aiPrintImmediate)
	{
		ciPrintImmediate = aiPrintImmediate;
	}
	/**
	 * Set the value of SubconTrans
	 * 
	 * @param abSubconTrans boolean
	 */
	public void setSubconTrans(boolean abSubconTrans)
	{
		cbSubconTrans = abSubconTrans;
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
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId   String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * This method sets the value of TransName.
	 * 
	 * @param asTransName   String 
	 */
	public final void setTransName(String asTransName)
	{
		csTransName = asTransName;
		
		// defect 10616 
		if (csTransName != null && csTransName.length() > 30)
		{
			csTransName = csTransName.substring(0,30); 
		}
		// end defect 10616 
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
	 * This method sets the value of TransTimestmp.
	 * 
	 * @param aaTransTimestmp   RTSDate 
	 */
	public final void setTransTimestmp(RTSDate aaTransTimestmp)
	{
		cTransTimestmp = aaTransTimestmp;
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
	 * This method sets the value of VersionCd.
	 * 
	 * @param aiVersionCd   int 
	 */
	public final void setVersionCd(int aiVersionCd)
	{
		ciVersionCd = aiVersionCd;
	}

	/**
	 * Set value of cbWebAgntTrans
	 * 
	 * @param abWebAgntTrans
	 */
	public void setWebAgntTrans(boolean abWebAgntTrans)
	{
		cbWebAgntTrans = abWebAgntTrans;
	}

}
