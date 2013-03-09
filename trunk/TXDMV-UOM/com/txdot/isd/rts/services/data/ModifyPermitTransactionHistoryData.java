package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * ModifyPermitTransaction.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	05/31/2011	Created
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * elements from/to RTS_MOD_PRMT_TRANS_HSTRY
 *
 * @version	6.8.0			05/31/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		05/31/2011	14:08:17
 */
public class ModifyPermitTransactionHistoryData
	implements Serializable, Displayable
{
	// int 
	private int ciCacheTransAMDate;
	private int ciCacheTransTime;
	private int ciCustSeqNo;
	private int ciEffDate;
	private int ciEffTime;
	private int ciExpDate;
	private int ciExpTime;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransCompleteIndi; 
	private int ciTransTime;
	private int ciTransWsId;
	private int ciVehModlYr;
	private int ciVoidedTransIndi;

	// String 
	private String csAcctItmCd;
	private String csItmCd;
	private String csPrmtIssuanceId;
	private String csPrmtNo;
	private String csTransCd;
	private String csTransEmpId;
	private String csTransId;
	private String csVehMk;
	private String csVin;
	
	private CustomerNameData caCustNameData = new CustomerNameData();
	
	/**
	 * ModifyPermitTransactionHistory.java Constructor
	 * 
	 */
	public ModifyPermitTransactionHistoryData()
	{
		super();
	}

	/**
	 * ModifyPermitTransactionHistoryData Constructor
	 * 
	 * @param aaPrmtData 
	 */
	public ModifyPermitTransactionHistoryData(PermitData aaPrmtData)
	{
		// CUSTOMER NAME
		if (aaPrmtData.getCustomerData() != null
			&& aaPrmtData.getCustomerData().getCustNameData() != null)
		{
			caCustNameData =
				(CustomerNameData) UtilityMethods.copy(
					aaPrmtData.getCustomerData().getCustNameData());
		}

		// PERMIT DETAILS
		csPrmtNo = aaPrmtData.getPrmtNo();
		csItmCd = aaPrmtData.getItmCd();
		csAcctItmCd = aaPrmtData.getAcctItmCd();
		ciEffDate = aaPrmtData.getEffDt();
		ciEffTime = aaPrmtData.getEffTime();
		ciExpDate = aaPrmtData.getExpDt();
		ciExpTime = aaPrmtData.getExpTime();
		csPrmtIssuanceId = aaPrmtData.getPrmtIssuanceId();

		// VEHICLE INFO
		csVehMk = aaPrmtData.getVehMk();
		ciVehModlYr = aaPrmtData.getVehModlYr();
		csVin = aaPrmtData.getVin();
	}

	/**
	 * Gets value of csAcctItmCd
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Method used to return field attributes. 
	 * 
	 * @return  Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHashMap = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}

			catch (IllegalAccessException aeIllAccEx)
			{
				continue;
			}
		}
		return lhmHashMap;
	}

	/**
	 * Gets value of ciCacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Gets value of ciCacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}
	/**
	 * Get value of caCustNameData
	 * 
	 * @return CustomerNameData
	 */
	public CustomerNameData getCustNameData()
	{
		return caCustNameData;
	}

	/**
	 * Returns value of ciCustSeqNo
	 * 
	 * @return  int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Gets value of ciEffDate
	 * 
	 * @return 
	 */
	public int getEffDate()
	{
		return ciEffDate;
	}

	/**
	 * Gets value of ciEffTime
	 * 
	 * @return 
	 */
	public int getEffTime()
	{
		return ciEffTime;
	}

	/**
	 * Gets value of ciExpDate
	 * 
	 * @return 
	 */
	public int getExpDate()
	{
		return ciExpDate;
	}

	/**
	 * Gets value of ciExpTime
	 * 
	 * @return 
	 */
	public int getExpTime()
	{
		return ciExpTime;
	}

	/**
	 * Gets value of csItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}

	/**
	 * Returns value of ciOfcIssuanceNo
	 * 
	 * @return  int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}


	/**
	 * Gets value of csPrmtIssuanceId
	 * 
	 * @return String
	 */
	public String getPrmtIssuanceId()
	{
		return csPrmtIssuanceId;
	}

	/**
	 * Gets value of csPrmtNo
	 * 
	 * @return String
	 */
	public String getPrmtNo()
	{
		return csPrmtNo;
	}

	/**
	 * Returns value of ciSubstaId
	 * 
	 * @return  int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns value of ciTransAMDate
	 * 
	 * @return  int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Gets value of 
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Get value of ciTransCompleteIndi
	 * 
	 * @return int
	 */
	public int getTransCompleteIndi()
	{
		return ciTransCompleteIndi;
	}

	/**
	 * Gets value of 
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
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
	 * Returns value of ciTransTime
	 * 
	 * @return  int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns value of ciTransWsId
	 * 
	 * @return  int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Gets value of csVehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Gets value of ciVehModlYr
	 * 
	 * @return 
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Gets value of csVin
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}

	/**
	 * Return value of ciVoidedTransIndi
	 * 
	 * @return int 
	 */
	public int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * Sets value of csAcctItmCd
	 * 
	 * @param asAcctItmCd
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * Sets value of ciCacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Sets value of ciCacheTransTime
	 * 
	 * @param aiCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * Set value of caCustNameData
	 * 
	 * @param aaCustNameData
	 */
	public void setCustNameData(CustomerNameData aaCustNameData)
	{
		caCustNameData = aaCustNameData;
	}

	/**
	 * Sets value of ciCustSeqNo
	 * 
	 * @param aiCustSeqNo
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Sets value of ciEffDate
	 * 
	 * @param aiEffDate
	 */
	public void setEffDate(int aiEffDate)
	{
		ciEffDate = aiEffDate;
	}

	/**
	 * Sets value of ciEffTime
	 * 
	 * @param aiEffTime
	 */
	public void setEffTime(int aiEffTime)
	{
		ciEffTime = aiEffTime;
	}

	/**
	 * Sets value of ciExpDate
	 * 
	 * @param aiExpDate
	 */
	public void setExpDate(int aiExpDate)
	{
		ciExpDate = aiExpDate;
	}

	/**
	 * Sets value of ciExpTime
	 * 
	 * @param aiExpTime
	 */
	public void setExpTime(int aiExpTime)
	{
		ciExpTime = aiExpTime;
	}

	/**
	 * Sets value of csItmCd
	 * 
	 * @param asItmCd
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}

	/**
	 * Sets value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Sets value of csPrmtIssuanceId
	 * 
	 * @param asPrmtIssuanceId
	 */
	public void setPrmtIssuanceId(String asPrmtIssuanceId)
	{
		csPrmtIssuanceId = asPrmtIssuanceId;
	}

	/**
	 * Sets value of csPrmtNo
	 * 
	 * @param asPrmtNo
	 */
	public void setPrmtNo(String asPrmtNo)
	{
		csPrmtNo = asPrmtNo;
	}

	/**
	 * Sets value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Sets value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Sets value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set value of ciTransCompleteIndi
	 * 
	 * @param aiTransCompleteIndi
	 */
	public void setTransCompleteIndi(int aiTransCompleteIndi)
	{
		ciTransCompleteIndi = aiTransCompleteIndi;
	}

	/**
	 * Sets value of csTransEmpId
	 * 
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * Sets value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Sets value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Sets value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Sets value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Sets value of csVin
	 * 
	 * @param asVin
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}

	/**
	 * Set value of ciVoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi
	 */
	public void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}

}
