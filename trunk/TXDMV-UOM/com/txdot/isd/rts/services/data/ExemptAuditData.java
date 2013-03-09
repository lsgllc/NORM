package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * ExemptAuditData.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/25/2006	Created for Exempt Project
 * 							defect 8900 Ver Exempts
 * K Harrell	09/27/2006	Added new columns 
 * 							add csRegPltCdDesc,csPriorRegPltCdDesc,
 * 							csRegClassCdDesc,csPriorRegClassCdDesc,
 * 							csPriorRegPltCd,csPriorRegPltNo,
 * 							csTransId  
 * 							plus get/set methods 
 * 							defect 8900 Ver Exempts
 * K Harrell	10/02/2006	Added new column 
 * 							add ciTransCompleteIndi
 * 							add getAttributes(), 
 * 								getTransCompleteIndi(), 
 * 								setTransCompleteIndi()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/14/2006	Add new column
 * 							add ciMfDwnCd 
 * 							add getMfDwnCd(),setMfDwnCd()
 * 							defect 9017 Ver Exempts   
 * K Harrell 	06/14/2010 	modify getTransId()
 * 							defect 10505 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This data class contains attributes and get/set methods for 
 * ExemptAuditData
 *
 * @version	6.5.0 			06/14/2010 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/25/2006   15:04:00
 */
public class ExemptAuditData implements Serializable, Displayable
{
	protected int ciCacheTransAMDate; // SendCache
	protected int ciCacheTransTime; // SendCache

	// int
	protected int ciCustSeqNo;
	protected int ciExmptIndi;
	// defect 9017 
	protected int ciMfDwnCd;
	// end defect 9017 
	protected int ciOfcIssuanceNo;
	protected int ciPriorExmptIndi;
	protected int ciPriorRegClassCd;
	protected int ciRegClassCd;
	protected int ciRegExpMo;
	protected int ciRegExpYr;
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransCompleteIndi;
	protected int ciTransTime;
	protected int ciTransWsId;
	protected int ciTtlFeeIndi;
	protected int ciVehModlYr;
	protected int ciVoidedTransIndi;

	// String
	protected String csDocNo;
	protected String csLienHldr1Name1;
	protected String csOwnrTtlName1;
	protected String csOwnrTtlName2;
	protected String csPriorRegClassCdDesc;
	protected String csPriorRegPltCd;
	protected String csPriorRegPltCdDesc;
	protected String csPriorRegPltNo;
	protected String csRegClassCdDesc;
	protected String csRegPltCd;
	protected String csRegPltCdDesc;
	protected String csRegPltNo;
	protected String csTransCd;
	protected String csTransEmpId;
	protected String csTransId;
	protected String csVehClassCd;
	protected String csVehMk;
	protected String csVIN;
	
	static final long serialVersionUID = 923229667001457957L;

	public ExemptAuditData()
	{
		super();
	}

	/**
	 * Method used to return field attributes. 
	 * 
	 * @return Map
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
	 * Returns the value of CacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Returns the value of CacheTransTime
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
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Returns the value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Returns the value of ExmptIndi
	 * 
	 * @return int
	 */
	public int getExmptIndi()
	{
		return ciExmptIndi;
	}

	/**
	 * Returns the value of LienHldr1Name1
	 * 
	 * @return String
	 */
	public String getLienHldr1Name1()
	{
		return csLienHldr1Name1;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of OwnrTtlName1
	 * 
	 * @return String
	 */
	public String getOwnrTtlName1()
	{
		return csOwnrTtlName1;
	}

	/**
	 * Returns the value of OwnrTtlName2
	 * 
	 * @return String
	 */
	public String getOwnrTtlName2()
	{
		return csOwnrTtlName2;
	}

	/**
	 * Returns the value of MfDwnCd
	 * 
	 * @return int
	 */
	public int getMfDwnCd()
	{
		return ciMfDwnCd;
	}
	/**
	 * Returns the value of PriorExmptIndi
	 * 
	 * @return int
	 */
	public int getPriorExmptIndi()
	{
		return ciPriorExmptIndi;
	}

	/**
	 * Returns the value of PriorRegClassCd
	 * 
	 * @return int 
	 */
	public int getPriorRegClassCd()
	{
		return ciPriorRegClassCd;
	}

	/**
	 * Returns the value of csPriorRegClassCdDesc
	 * 
	 * @return String
	 */
	public String getPriorRegClassCdDesc()
	{
		return csPriorRegClassCdDesc;
	}

	/**
	 * Returns the value of PriorRegPltCd
	 * 
	 * @return String
	 */
	public String getPriorRegPltCd()
	{
		return csPriorRegPltCd;
	}

	/**
	 * Returns the value of PriorRegPltCdDesc
	 * 
	 * @return String
	 */
	public String getPriorRegPltCdDesc()
	{
		return csPriorRegPltCdDesc;
	}

	/**
	 * Returns the value of PriorRegPltNo
	 * 
	 * @return String
	 */
	public String getPriorRegPltNo()
	{
		return csPriorRegPltNo;
	}

	/**
	 * Returns the value of RegClassCd
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}

	/**
	 * Returns the value of RegClassCdDesc
	 * 
	 * @return String
	 */
	public String getRegClassCdDesc()
	{
		return csRegClassCdDesc;
	}

	/**
	 * Returns the value of RegExpMo
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Returns the value of RegExpYr 
	 * 
	 * @return int
	 */
	public int getRegExpYr()
	{
		return ciRegExpYr;
	}

	/**
	 * Returns the value of RegPltCd 
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Returns the value of RegPltCdDesc
	 * 
	 * @return String
	 */
	public String getRegPltCdDesc()
	{
		return csRegPltCdDesc;
	}

	/**
	 * Returns the value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Returns the value of SubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Returns the value of TransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Return value of ciTransCompleteIndi
	 * 
	 * @return int
	 */
	public int getTransCompleteIndi()
	{
		return ciTransCompleteIndi;
	}

	/**
	 * Returns the value of TransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	
	/**
	  * Get AbstractValue of TransId
	  * 
	  * @return String 
	  */
	public String getTransId()
	{
		// defect 10505 
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
		// end defect 10505 
	}

	/**
	 * Returns the value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the value of TransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Returns the value of TtlFeeIndi 
	 * 
	 * @return int
	 */
	public int getTtlFeeIndi()
	{
		return ciTtlFeeIndi;
	}
	/**
	 * Returns the value of VehClassCd
	 * 
	 * @return String
	 */
	public String getVehClassCd()
	{
		return csVehClassCd;
	}

	/**
	 * Returns the value of VehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Returns the value of VehModlYr
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Returns the value of VIN
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return int
	 */
	public int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * Sets the value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Sets the value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * Sets the value of CustSeqNo
	 * 
	 * @param aiCustSeqNo int
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Sets the value of DocNo
	 * 
	 * @param asDocNo String 
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Sets the value of ExmptIndi
	 * 
	 * @param aiExmptIndi int
	 */
	public void setExmptIndi(int aiExmptIndi)
	{
		ciExmptIndi = aiExmptIndi;
	}
	/**
	 * Sets the value of MfDwnCd
	 * 
	 * @param aiMfDwnCd int
	 */
	public void setMfDwnCd(int aiMfDwnCd)
	{
		ciMfDwnCd = aiMfDwnCd;
	}
	/**
	 * Sets the value of LienHldr1Name1
	 * 
	 * @param asLienHldr1Name1 String
	 */
	public void setLienHldr1Name1(String asLienHldr1Name1)
	{
		csLienHldr1Name1 = asLienHldr1Name1;
	}

	/**
	 * Sets the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Sets the value of OwnrTtlName1
	 * 
	 * @param asOwnrTtlName1
	 */
	public void setOwnrTtlName1(String asOwnrTtlName1)
	{
		csOwnrTtlName1 = asOwnrTtlName1;
	}

	/**
	 * Sets the value of OwnrTtlName2
	 * 
	 * @param asOwnrTtlName2 String
	 */
	public void setOwnrTtlName2(String asOwnrTtlName2)
	{
		csOwnrTtlName2 = asOwnrTtlName2;
	}

	/**
	 * Sets the value of PriorExmptIndi
	 * 
	 * @param aiPriorExmptIndi int
	 */
	public void setPriorExmptIndi(int aiPriorExmptIndi)
	{
		ciPriorExmptIndi = aiPriorExmptIndi;
	}

	/**
	 * Sets the value of PriorRegClassCd
	 * 
	 * @param aiPriorRegClassCd int
	 */
	public void setPriorRegClassCd(int aiPriorRegClassCd)
	{
		ciPriorRegClassCd = aiPriorRegClassCd;
	}

	/**
	 * Sets the value of PriorRegClassCdDesc
	 * 
	 * @param asPriorRegClassCdDesc String
	 */
	public void setPriorRegClassCdDesc(String asPriorRegClassCdDesc)
	{
		csPriorRegClassCdDesc = asPriorRegClassCdDesc;
	}

	/**
	 * Sets the value of PriorRegPltCd
	 * 
	 * @param asPriorRegPltCd String 
	 */
	public void setPriorRegPltCd(String asPriorRegPltCd)
	{
		csPriorRegPltCd = asPriorRegPltCd;
	}

	/**
	 * Sets the value of PriorRegPltCdDesc
	 * 
	 * @param asPriorRegPltCdDesc String
	 */
	public void setPriorRegPltCdDesc(String asPriorRegPltCdDesc)
	{
		csPriorRegPltCdDesc = asPriorRegPltCdDesc;
	}

	/**
	 * Sets the value of PriorRegPltNo
	 * 
	 * @param asPriorRegPltNo String
	 */
	public void setPriorRegPltNo(String asPriorRegPltNo)
	{
		csPriorRegPltNo = asPriorRegPltNo;
	}

	/**
	 * Sets the value of RegClassCd
	 * 
	 * @param aiRegClassCd int
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * Sets the value of RegClassCdDesc
	 * 
	 * @param asRegClassCdDesc String
	 */
	public void setRegClassCdDesc(String asRegClassCdDesc)
	{
		csRegClassCdDesc = asRegClassCdDesc;
	}

	/**
	 * Sets the value of RegExpMo
	 * 
	 * @param aiRegExpMo int
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Sets the value of RegExpYr
	 * 
	 * @param aiRegExpYr int
	 */
	public void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}

	/**
	 * Sets the value of RegPltCd
	 * 
	 * @param asRegPltCd String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Sets the value of RegPltCdDesc
	 * 
	 * @param asRegPltCdDesc String
	 */
	public void setRegPltCdDesc(String asRegPltCdDesc)
	{
		csRegPltCdDesc = asRegPltCdDesc;
	}

	/**
	 * Sets the value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Sets the value of SubstaId
	 * 
	 * @param aiSubstaId int
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Sets the value of TransAMDate
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Sets the value of TransCd
	 * 
	 * @param asTransCd String
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set value of TransCompleteIndi
	 * 
	 * @param aiTransCompleteIndi int
	 */
	public void setTransCompleteIndi(int aiTransCompleteIndi)
	{
		ciTransCompleteIndi = aiTransCompleteIndi;
	}
	/**
	 * Sets the value of TransEmpId
	 * 
	 * @param asTransEmpId String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Sets the value of TransCd
	 * 
	 * @param asTransCd String
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Sets the value of TransTime
	 * 
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Sets the value of TransWsId
	 * 
	 * @param aiTransWsId int
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Sets the value of TtlFeeIndi
	 * 
	 * @param aiTtlFeeIndi int
	 */
	public void setTtlFeeIndi(int aiTtlFeeIndi)
	{
		ciTtlFeeIndi = aiTtlFeeIndi;
	}

	/**
	 * Sets the value of VehClassCd
	 * 
	 * @param asVehClassCd String
	 */
	public void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}

	/**
	 * Sets the value of VehMk
	 * 
	 * @param asVehMk String
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Sets the value of VehModlYr
	 * 
	 * @param aiVehModlYr int
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Sets the value of VIN
	 * 
	 * @param asVIN String 
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/**
	 * Sets the value of VoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi int
	 */
	public void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}

}
