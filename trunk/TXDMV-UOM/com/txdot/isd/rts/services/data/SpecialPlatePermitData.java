package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * SpecialPlatePermitData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/21/2010	Created
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * SpecialPlatePermitData
 *
 * @version	6.7.0			12/21/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/21/2010 10:50:17 
 */
public class SpecialPlatePermitData
	implements Serializable, Displayable
{
	// int 
	private int ciCacheTransAMDate;
	private int ciCacheTransTime;
	private int ciCustSeqNo;
	private int ciEffDate;
	private int ciExpDate;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;
	private int ciVehModlYr;

	// String 
	private String csPltOwnrName1;
	private String csPltOwnrName2;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csTransId;
	private String csVehMk;
	private String csVIN;

	static final long serialVersionUID = -5315198259326318254L;

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
	 * SpecialPlatePermitData.java Constructor
	 * 
	 */
	public SpecialPlatePermitData()
	{
		super();
	}

	/**
	 * Get value of ciCacheTransAMDate
	 * 
	 * @return int 
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Get value of ciCacheTransTime
	 * 
	 * @return int 
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Get value of ciCustSeqNo
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getEffDate()
	{
		return ciEffDate;
	}

	/**
	 * Get value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getExpDate()
	{
		return ciExpDate;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get AbstractValue of csPltOwnrName1
	 * 
	 * @return String 
	 */
	public String getPltOwnrName1()
	{
		return csPltOwnrName1;
	}

	/**
	 * Get value of csPltOwnrName1
	 * 
	 * @return String 
	 */
	public String getPltOwnrName2()
	{
		return csPltOwnrName2;
	}

	/**
	 * Get value of csRegPltCd
	 * 
	 * @return String 
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Get value of csRegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Get value of ciSubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Get value of ciTransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Get value of csTransId
	 * 
	 * @return String
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
	 * Get value of ciTransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Get value of ciTransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Get value of csVehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Get value of ciVehModlYr
	 * 
	 * @return
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
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
	 * Get value of ciCustSeqNo
	 * 
	 * @param aiCustSeqNo
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @param aiRTSEffDate
	 */
	public void setEffDate(int aiRTSEffDate)
	{
		ciEffDate = aiRTSEffDate;
	}

	/**
	 * Get value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setExpDate(int aiRTSEffEndDate)
	{
		ciExpDate = aiRTSEffEndDate;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of csPltOwnrName1
	 * 
	 * @param asPltOwnrName1
	 */
	public void setPltOwnrName1(String asPltOwnrName1)
	{
		csPltOwnrName1 = asPltOwnrName1;
	}

	/**
	 * Set value of csPltOwnrName2
	 * 
	 * @param asPltOwnrName2
	 */
	public void setPltOwnrName2(String asPltOwnrName2)
	{
		csPltOwnrName2 = asPltOwnrName2;
	}

	/**
	 * Set value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Get value of csRegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Get value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Get value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Get value of csTransId
	 * 
	 * @param asTransId
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Get value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Get value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Get value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Get value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Get value of csVIN
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
}
