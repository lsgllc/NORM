package com.txdot.isd.rts.services.data;import java.io.Serializable;import java.lang.reflect.Field;import java.util.HashMap;import java.util.Map;import com.txdot.isd.rts.services.util.Displayable;import com.txdot.isd.rts.services.util.RTSDate;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.CommonConstant;/* * * TransactionHeaderData.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K.Harrell    12/11/2001  Removed BranchOfcId * K Harrell	10/04/2004	Add ciCacheTransAMDate,ciCacheTransTime *							and associated get/set methods *							defect 7586 Ver 5.2.1 * K Harrell	05/19/2005	Java 1.4 Work *							defect 7899 Ver 5.2.3 * K Harrell	01/30/2009	Add boolean/get/set methods to manage  * 							 enable/disable of Cancel button on Pending * 							 Trans Panel when restoring set-aside.  * 							add cbCancelTrans * 							add isCancelTrans(), setCancelTrans()    * 							defect 7717 Ver Defect POS_D * K Harrell 	06/14/2010 	add csCustNoKey, get/set methods * 							defect 10505 Ver 6.5.0  * K Harrell	09/29/2010 	modify setTransName()  * 							defect 10616 Ver 6.5.0 * K Harrell	01/29/2011	add cbWebAgntTrans, is/set methods  * 							defect 10734 Ver 6.7.0   * --------------------------------------------------------------------- *//** * This Data class contains attributes and get set methods for  * TransactionHeaderData * * @version	6.7.0			01/29/2011 * @author	Kathy Harrell * <br>Creation Date:		09/21/2001 *//* &TransactionHeaderData& */public class TransactionHeaderData implements Serializable, Displayable{	// boolean /* &TransactionHeaderData'cbCancelTrans& */	private boolean cbCancelTrans;/* &TransactionHeaderData'cbSubconTrans& */	private boolean cbSubconTrans;		// defect 10734 /* &TransactionHeaderData'cbWebAgntTrans& */	private boolean cbWebAgntTrans;	// end defect 10734  	// int /* &TransactionHeaderData'ciCacheTransAMDate& */	private int ciCacheTransAMDate; // SendCache/* &TransactionHeaderData'ciCacheTransTime& */	private int ciCacheTransTime; // SendCache /* &TransactionHeaderData'ciCashWsId& */	private int ciCashWsId;/* &TransactionHeaderData'ciCustSeqNo& */	private int ciCustSeqNo;/* &TransactionHeaderData'ciDbTypeIndi& */	private int ciDbTypeIndi;/* &TransactionHeaderData'ciFeeSourceCd& */	private int ciFeeSourceCd;/* &TransactionHeaderData'ciOfcIssuanceCd& */	private int ciOfcIssuanceCd;/* &TransactionHeaderData'ciOfcIssuanceNo& */	private int ciOfcIssuanceNo;/* &TransactionHeaderData'ciPrintImmediate& */	private int ciPrintImmediate;/* &TransactionHeaderData'ciSubstaId& */	private int ciSubstaId;/* &TransactionHeaderData'ciTransAMDate& */	private int ciTransAMDate;/* &TransactionHeaderData'ciTransTime& */	private int ciTransTime;/* &TransactionHeaderData'ciTransWsId& */	private int ciTransWsId;/* &TransactionHeaderData'ciVersionCd& */	private int ciVersionCd;	// String /* &TransactionHeaderData'csCustNoKey& */	private String csCustNoKey;/* &TransactionHeaderData'csTransEmpId& */	private String csTransEmpId;/* &TransactionHeaderData'csTransName& */	private String csTransName;	//	Object /* &TransactionHeaderData'cTransTimestmp& */	private RTSDate cTransTimestmp;		// Constant /* &TransactionHeaderData'TIMESTMP& */	public static final int TIMESTMP = 0;/* &TransactionHeaderData'TRANS_NAME& */	public static final int TRANS_NAME = 1;	/* &TransactionHeaderData'serialVersionUID& */	private final static long serialVersionUID = -1854200168018736198L;	/**	 * Method used to return field attributes	 * 	 * @return Map	 *//* &TransactionHeaderData.getAttributes& */	public Map getAttributes()	{		HashMap lhmHash = new HashMap();		Field[] larrFields = this.getClass().getDeclaredFields();		for (int i = 0; i < larrFields.length; i++)		{			try			{				lhmHash.put(					larrFields[i].getName(),					larrFields[i].get(this));			}			catch (IllegalAccessException leIllAccEx)			{				continue;			}		}		return lhmHash;	}	/**	 * Return the value of CacheTransAMDate	 * 	 * @return int	 *//* &TransactionHeaderData.getCacheTransAMDate& */	public int getCacheTransAMDate()	{		return ciCacheTransAMDate;	}	/**	 * Return the value of CacheTransTime	 * 	 * @return int	 *//* &TransactionHeaderData.getCacheTransTime& */	public int getCacheTransTime()	{		return ciCacheTransTime;	}	/**	 * Returns the value of CashWsId	 * 	 * @return  int 	 *//* &TransactionHeaderData.getCashWsId& */	public final int getCashWsId()	{		return ciCashWsId;	}	/**	 * Returns the value of CustNoKey	 * 	 * @return String 	 *//* &TransactionHeaderData.getCustNoKey& */	public String getCustNoKey()	{		if (csCustNoKey == null || csCustNoKey.trim().length() == 0)		{			csCustNoKey =				UtilityMethods.addPadding(					new String[] {						String.valueOf(ciOfcIssuanceNo),						String.valueOf(ciTransWsId),						String.valueOf(ciTransAMDate),						String.valueOf(ciCustSeqNo)},					new int[] {						CommonConstant.LENGTH_OFFICE_ISSUANCENO,						CommonConstant.LENGTH_TRANS_WSID,						CommonConstant.LENGTH_TRANSAMDATE,						CommonConstant.LENGTH_CUSTSEQNO},					CommonConstant.STR_ZERO);		}		return csCustNoKey;	}	/**	 * Returns the value of CustSeqNo	 * 	 * @return  int 	 *//* &TransactionHeaderData.getCustSeqNo& */	public final int getCustSeqNo()	{		return ciCustSeqNo;	}	/**	 * Return the value of DbTypeIndi	 * 	 * @return int	 *//* &TransactionHeaderData.getDbTypeIndi& */	public int getDbTypeIndi()	{		return ciDbTypeIndi;	}	/**	 * Returns the value of FeeSourceCd	 * 	 * @return  int 	 *//* &TransactionHeaderData.getFeeSourceCd& */	public final int getFeeSourceCd()	{		return ciFeeSourceCd;	}	/**	 * Returns the value of OfcIssuanceCd	 * 	 * @return  int 	 *//* &TransactionHeaderData.getOfcIssuanceCd& */	public final int getOfcIssuanceCd()	{		return ciOfcIssuanceCd;	}	/**	 * Returns the value of OfcIssuanceNo	 * 	 * @return  int 	 *//* &TransactionHeaderData.getOfcIssuanceNo& */	public final int getOfcIssuanceNo()	{		return ciOfcIssuanceNo;	}	/**	 * Returns the value of PrintImmediate	 * 	 * @return  int 	 *//* &TransactionHeaderData.getPrintImmediate& */	public final int getPrintImmediate()	{		return ciPrintImmediate;	}	/**	 * Returns the value of SubstaId	 * 	 * @return  int 	 *//* &TransactionHeaderData.getSubstaId& */	public final int getSubstaId()	{		return ciSubstaId;	}	/**	 * Returns the value of TransAMDate	 * 	 * @return  int 	 *//* &TransactionHeaderData.getTransAMDate& */	public final int getTransAMDate()	{		return ciTransAMDate;	}	/**	 * Returns the value of TransEmpId	 * 	 * @return  String 	 *//* &TransactionHeaderData.getTransEmpId& */	public final String getTransEmpId()	{		return csTransEmpId;	}	/**	 * Returns the value of TransName	 * 	 * @return  String 	 *//* &TransactionHeaderData.getTransName& */	public final String getTransName()	{		return csTransName;	}	/**	 * Return the value of TransTime	 * 	 * @return int	 *//* &TransactionHeaderData.getTransTime& */	public int getTransTime()	{		return ciTransTime;	}	/**	 * Returns the value of TransTimestmp	 * 	 * @return  RTSDate 	 *//* &TransactionHeaderData.getTransTimestmp& */	public final RTSDate getTransTimestmp()	{		return cTransTimestmp;	}	/**	 * Returns the value of TransWsId	 * 	 * @return  int 	 *//* &TransactionHeaderData.getTransWsId& */	public final int getTransWsId()	{		return ciTransWsId;	}	/**	 * Returns the value of VersionCd	 * 	 * @return  int 	 *//* &TransactionHeaderData.getVersionCd& */	public final int getVersionCd()	{		return ciVersionCd;	}	/**	 * This method returns the value of CancelTrans.	 * 	 * @return	 *//* &TransactionHeaderData.isCancelTrans& */	public boolean isCancelTrans()	{		return cbCancelTrans;	}	/**	 * Return the value of SubconTrans	 * 	 * @return boolean	 *//* &TransactionHeaderData.isSubconTrans& */	public boolean isSubconTrans()	{		return cbSubconTrans;	}	/**	 * Return value of cbWebAgntTrans	 * 	 * @return boolean 	 *//* &TransactionHeaderData.isWebAgntTrans& */	public boolean isWebAgntTrans()	{		return cbWebAgntTrans;	}	/**	 * Set the value of CacheTransAMDate	 * 	 * @param aiCacheTransAMDate int	 *//* &TransactionHeaderData.setCacheTransAMDate& */	public void setCacheTransAMDate(int aiCacheTransAMDate)	{		ciCacheTransAMDate = aiCacheTransAMDate;	}	/**	 * Set the value of CacheTransTime	 * 	 * @param aiCacheTransTime int	 *//* &TransactionHeaderData.setCacheTransTime& */	public void setCacheTransTime(int aiCacheTransTime)	{		ciCacheTransTime = aiCacheTransTime;	}	/**	 * This method sets the value of CancelTrans.	 * 	 * @param abCancelTrans	 *//* &TransactionHeaderData.setCancelTrans& */	public void setCancelTrans(boolean abCancelTrans)	{		cbCancelTrans = abCancelTrans;	}	/**	 * This method sets the value of CashWsId.	 * 	 * @param aiCashWsId   int 	 *//* &TransactionHeaderData.setCashWsId& */	public final void setCashWsId(int aiCashWsId)	{		ciCashWsId = aiCashWsId;	}	/**	 * Sets the value of CustNoKey	 * 	 * @param asCustNoKey	 *//* &TransactionHeaderData.setCustNoKey& */	public void setCustNoKey(String asCustNoKey)	{		csCustNoKey = asCustNoKey;	}	/**	 * This method sets the value of CustSeqNo.	 * 	 * @param aiCustSeqNo   int 	 *//* &TransactionHeaderData.setCustSeqNo& */	public final void setCustSeqNo(int aiCustSeqNo)	{		ciCustSeqNo = aiCustSeqNo;	}	/**	 * Set the value of DbTypeIndi	 * 	 * @param aiDbTypeIndi int	 *//* &TransactionHeaderData.setDbTypeIndi& */	public void setDbTypeIndi(int aiDbTypeIndi)	{		ciDbTypeIndi = aiDbTypeIndi;	}	/**	 * This method sets the value of FeeSourceCd.	 * 	 * @param aiFeeSourceCd   int 	 *//* &TransactionHeaderData.setFeeSourceCd& */	public final void setFeeSourceCd(int aiFeeSourceCd)	{		ciFeeSourceCd = aiFeeSourceCd;	}	/**	 * This method sets the value of OfcIssuanceCd.	 * 	 * @param aiOfcIssuanceCd   int 	 *//* &TransactionHeaderData.setOfcIssuanceCd& */	public final void setOfcIssuanceCd(int aiOfcIssuanceCd)	{		ciOfcIssuanceCd = aiOfcIssuanceCd;	}	/**	 * This method sets the value of OfcIssuanceNo.	 * 	 * @param aiOfcIssuanceNo   int 	 *//* &TransactionHeaderData.setOfcIssuanceNo& */	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)	{		ciOfcIssuanceNo = aiOfcIssuanceNo;	}	/**	 * This method sets the value of PrintImmediate.	 * 	 * @param aiPrintImmediate   int 	 *//* &TransactionHeaderData.setPrintImmediate& */	public final void setPrintImmediate(int aiPrintImmediate)	{		ciPrintImmediate = aiPrintImmediate;	}	/**	 * Set the value of SubconTrans	 * 	 * @param abSubconTrans boolean	 *//* &TransactionHeaderData.setSubconTrans& */	public void setSubconTrans(boolean abSubconTrans)	{		cbSubconTrans = abSubconTrans;	}	/**	 * This method sets the value of SubstaId.	 * 	 * @param aiSubstaId   int 	 *//* &TransactionHeaderData.setSubstaId& */	public final void setSubstaId(int aiSubstaId)	{		ciSubstaId = aiSubstaId;	}	/**	 * This method sets the value of TransAMDate.	 * 	 * @param aiTransAMDate   int 	 *//* &TransactionHeaderData.setTransAMDate& */	public final void setTransAMDate(int aiTransAMDate)	{		ciTransAMDate = aiTransAMDate;	}	/**	 * This method sets the value of TransEmpId.	 * 	 * @param asTransEmpId   String 	 *//* &TransactionHeaderData.setTransEmpId& */	public final void setTransEmpId(String asTransEmpId)	{		csTransEmpId = asTransEmpId;	}	/**	 * This method sets the value of TransName.	 * 	 * @param asTransName   String 	 *//* &TransactionHeaderData.setTransName& */	public final void setTransName(String asTransName)	{		csTransName = asTransName;				// defect 10616 		if (csTransName != null && csTransName.length() > 30)		{			csTransName = csTransName.substring(0,30); 		}		// end defect 10616 	}	/**	 * Set the value of TransTime	 * 	 * @param aiTransTime int	 *//* &TransactionHeaderData.setTransTime& */	public void setTransTime(int aiTransTime)	{		ciTransTime = aiTransTime;	}	/**	 * This method sets the value of TransTimestmp.	 * 	 * @param aaTransTimestmp   RTSDate 	 *//* &TransactionHeaderData.setTransTimestmp& */	public final void setTransTimestmp(RTSDate aaTransTimestmp)	{		cTransTimestmp = aaTransTimestmp;	}	/**	 * This method sets the value of TransWsId.	 * 	 * @param aiTransWsId   int 	 *//* &TransactionHeaderData.setTransWsId& */	public final void setTransWsId(int aiTransWsId)	{		ciTransWsId = aiTransWsId;	}	/**	 * This method sets the value of VersionCd.	 * 	 * @param aiVersionCd   int 	 *//* &TransactionHeaderData.setVersionCd& */	public final void setVersionCd(int aiVersionCd)	{		ciVersionCd = aiVersionCd;	}	/**	 * Set value of cbWebAgntTrans	 * 	 * @param abWebAgntTrans	 *//* &TransactionHeaderData.setWebAgntTrans& */	public void setWebAgntTrans(boolean abWebAgntTrans)	{		cbWebAgntTrans = abWebAgntTrans;	}}/* #TransactionHeaderData# */