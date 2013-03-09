package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * ElectronicTitleHistoryData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2009	Created
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	04/15/2009	Sorted members 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell 	06/14/2010  modify getTransId() 
 * 							add csTransId, setTransId() 
 *        					defect 10505 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * ElectronicTitleHistory
 *
 * @version	6.5.0 			06/14/2010
 * @author	K Harrell
 * <br>Creation Date:		03/20/2009
 */
public class ElectronicTitleHistoryData
	implements Serializable, Displayable
{

	// int 	
	private int ciCacheTransAMDate; // SendCache
	private int ciCacheTransTime; // SendCache
	private int ciCustSeqNo;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransCompleteIndi;
	private int ciTransTime;
	private int ciTransWsId;
	private int ciVoidedTransIndi;

	//	String
	private String csCertfdLienHldrName1;
	private String csCertfdLienHldrName2;
	private String csOwnrTtlName1;
	private String csOwnrTtlName2;
	private String csPermLienhldrId;
	private String csRegPltNo;
	private String csTransCd;
	private String csTransEmpId;
	private String csVIN;

	// defect 10505
	private String csTransId;
	// end defect 10505 
	
	static final long serialVersionUID = 2777634945989926383L;

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
	}

	/**
	 * Return value of ciCacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Return value of ciCacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Get value of csCertfdLienHldrName1
	 * 
	 * @return String
	 */
	public String getCertfdLienHldrName1()
	{
		return csCertfdLienHldrName1;
	}

	/**
	 * Get value of csCertfdLienHldrName2
	 * 
	 * @return String
	 */
	public String getCertfdLienHldrName2()
	{
		return csCertfdLienHldrName2;
	}

	/**
	 * Return value of ciCustSeqNo
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Return value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Return value of csOwnrTtlName1
	 * 
	 * @return String
	 */
	public String getOwnrTtlName1()
	{
		return csOwnrTtlName1;
	}

	/**
	 * Return value of csOwnrTtlName2
	 * 
	 * @return String
	 */
	public String getOwnrTtlName2()
	{
		return csOwnrTtlName2;
	}

	/**
	 * Get value of csPermLienhldrId
	 * 
	 * @return String
	 */
	public String getPermLienhldrId()
	{
		return csPermLienhldrId;
	}

	/**
	 * Return value of csRegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Return value of ciSubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Return value of ciTransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Return value of csTransCd
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
	 * Return value of csTransEmpId
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
	 * Return value of ciTransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Return value of ciTransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Get value of csVIN 
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
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
	 * Set value of ciCacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Set value of ciCacheTransTime
	 * 
	 * @param aiCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * Set value of CertfdLienHldrName1
	 * 
	 * @param asCertfdLienHldrName1
	 */
	public void setCertfdLienHldrName1(String asCertfdLienHldrName1)
	{
		csCertfdLienHldrName1 = asCertfdLienHldrName1;
	}

	/**
	 * Set value of CertfdLienHldrName2
	 * 
	 * @param asCertfdLienHldrName2
	 */
	public void setCertfdLienHldrName2(String asCertfdLienHldrName2)
	{
		csCertfdLienHldrName2 = asCertfdLienHldrName2;
	}

	/**
	 * Set value of ciCustSeqNo
	 * 
	 * @param aiCustSeqNo
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcissuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of csOwnrTtlName1
	 * 
	 * @param asOwnrTtlName1
	 */
	public void setOwnrTtlName1(String asPltOwnrName1)
	{
		csOwnrTtlName1 = asPltOwnrName1;
	}

	/**
	 * Set value of csOwnrTtlName2
	 * 
	 * @param asOwnrTtlName2
	 */
	public void setOwnrTtlName2(String asOwnrTtlName2)
	{
		csOwnrTtlName2 = asOwnrTtlName2;
	}

	/**
	 * Set value of csPermLienhldrId
	 * 
	 * @param asPermLienhldrId
	 */
	public void setPermLienhldrId(String asPermLienhldrId)
	{
		csPermLienhldrId = asPermLienhldrId;
	}

	/**
	 * Set value of csRegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Set value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Set value of csTransCd
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
	 * Set value of csTransEmpId
	 * 
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * Set value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Set value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set value of csVIN
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
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
