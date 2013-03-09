package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * FraudLogData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * ---------------------------------------------------------------------
 */

/**
 * Represents a row from RTS_FRAUD_LOG
 *
 * @version	X.X.X Fix X		MM/DD/YYYY
 * @author	BWOODS-C
 * <br>Creation Date:		MM/DD/YYYY HH:MM:SS
 */
public class FraudLogData implements Serializable, Displayable
{
	// int
	private int ciAddFraudIndi;
	private int ciCacheTransAMDate;
	private int ciCacheTransTime;
	private int ciFraudCd;
	private int ciFraudIdntyNo;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;

	// String
	private String csDocNo;
	private String csFraudDesc;
	private String csRegPltNo;
	private String csTransEmpId;
	private String csVIN;

	//	Object 
	private RTSDate caTransTimestmp;

	/**
	 * FraudLogData.java Constructor
	 * 
	 */
	public FraudLogData()
	{
		super();
	}
	/**
	 * FraudLogData.java Constructor
	 * 
	 */
	public FraudLogData(MFVehicleData aaMFVehData)
	{
		super();
		setDocNo(aaMFVehData.getTitleData().getDocNo());
		setRegPltNo(aaMFVehData.getRegData().getRegPltNo());
		setVIN(aaMFVehData.getVehicleData().getVin());
	}
	/**
	 * Return value of ciAddFraudIndi
	 * 
	 * @return int 
	 */
	public int getAddFraudIndi()
	{
		return ciAddFraudIndi;
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
	 *  Return value of ciCacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Return value of csDocNo
	 * 
	 * @return String 
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Return value of ciFraudCd
	 * 
	 * @return int 
	 */
	public int getFraudCd()
	{
		return ciFraudCd;
	}

	/**
	 * Return value of csFraudDesc
	 * 
	 * @return String 
	 */
	public String getFraudDesc()
	{
		return csFraudDesc;
	}

	/**
	 * Return value of ciFraudIdntyNo
	 * 
	 * @return int 
	 */
	public int getFraudIdntyNo()
	{
		return ciFraudIdntyNo;
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
	 * Return value of csTransEmpId
	 * 
	 * @return String 
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
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
	 * Return value of caTransTimestmp
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getTransTimestmp()
	{
		return caTransTimestmp;
	}

	/**
	* Return value of ciTransAMDate
	* 
	* @return int 
	*/
	public int getTransWsId()
	{
		return ciTransWsId;
	}
	/**
	 * Return value of csFraudDesc
	 * 
	 * @return String 
	 */
	public String getVIN()
	{
		return csVIN;
	}
	/**
	 * Set value of ciAddFraudIndi
	 *
	 * @param aiAddFraudIndi
	 */
	public void setAddFraudIndi(int aiAddFraudIndi)
	{
		ciAddFraudIndi = aiAddFraudIndi;
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
	 * Set value of csFraudDesc
	 * 
	 * @return String 
	 */
	public void setDocNo(String string)
	{
		csDocNo = string;
	}
	/**
	 * Set value of ciAddFraudIndi
	 *
	 * @param aiFraudCd
	 */
	public void setFraudCd(int aiFraudCd)
	{
		ciFraudCd = aiFraudCd;
	}

	/**
	 * Set value of csFraudDesc
	 * 
	 * @param asFraudDesc
	 */
	public void setFraudDesc(String asFraudDesc)
	{
		csFraudDesc = asFraudDesc;
	}
	/**
	 * Set value of ciAddFraudIndi
	 *
	 * @param aiFraudIdntyNo
	 */
	public void setFraudIdntyNo(int aiFraudIdntyNo)
	{
		ciFraudIdntyNo = aiFraudIdntyNo;
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
	 * Set value of csTransEmpId
	 *
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * Set value of caTransTimestmp
	 *
	 * @param aaTransTimestmp
	 */
	public void setTransTimestmp(RTSDate aaTransTimestmp)
	{
		caTransTimestmp = aaTransTimestmp;
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
}
