package com.txdot.isd.rts.services.data;import java.io.Serializable;import java.lang.reflect.Field;import java.util.HashMap;import java.util.Map;import com.txdot.isd.rts.services.util.Displayable;import com.txdot.isd.rts.services.util.RTSDate;/* * FraudLogData.java * * (c) Texas Department of Transportation 2011 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * --------------------------------------------------------------------- *//** * Represents a row from RTS_FRAUD_LOG * * @version	X.X.X Fix X		MM/DD/YYYY * @author	BWOODS-C * <br>Creation Date:		MM/DD/YYYY HH:MM:SS *//* &FraudLogData& */public class FraudLogData implements Serializable, Displayable{	// int/* &FraudLogData'ciAddFraudIndi& */	private int ciAddFraudIndi;/* &FraudLogData'ciCacheTransAMDate& */	private int ciCacheTransAMDate;/* &FraudLogData'ciCacheTransTime& */	private int ciCacheTransTime;/* &FraudLogData'ciFraudCd& */	private int ciFraudCd;/* &FraudLogData'ciFraudIdntyNo& */	private int ciFraudIdntyNo;/* &FraudLogData'ciOfcIssuanceNo& */	private int ciOfcIssuanceNo;/* &FraudLogData'ciSubstaId& */	private int ciSubstaId;/* &FraudLogData'ciTransAMDate& */	private int ciTransAMDate;/* &FraudLogData'ciTransTime& */	private int ciTransTime;/* &FraudLogData'ciTransWsId& */	private int ciTransWsId;	// String/* &FraudLogData'csDocNo& */	private String csDocNo;/* &FraudLogData'csFraudDesc& */	private String csFraudDesc;/* &FraudLogData'csRegPltNo& */	private String csRegPltNo;/* &FraudLogData'csTransEmpId& */	private String csTransEmpId;/* &FraudLogData'csVIN& */	private String csVIN;	//	Object /* &FraudLogData'caTransTimestmp& */	private RTSDate caTransTimestmp;	/**	 * FraudLogData.java Constructor	 * 	 *//* &FraudLogData.FraudLogData& */	public FraudLogData()	{		super();	}	/**	 * FraudLogData.java Constructor	 * 	 *//* &FraudLogData.FraudLogData$1& */	public FraudLogData(MFVehicleData aaMFVehData)	{		super();		setDocNo(aaMFVehData.getTitleData().getDocNo());		setRegPltNo(aaMFVehData.getRegData().getRegPltNo());		setVIN(aaMFVehData.getVehicleData().getVin());	}	/**	 * Return value of ciAddFraudIndi	 * 	 * @return int 	 *//* &FraudLogData.getAddFraudIndi& */	public int getAddFraudIndi()	{		return ciAddFraudIndi;	}	/**	 * Return value of ciCacheTransAMDate	 * 	 * @return int	 *//* &FraudLogData.getCacheTransAMDate& */	public int getCacheTransAMDate()	{		return ciCacheTransAMDate;	}	/**	 *  Return value of ciCacheTransTime	 * 	 * @return int	 *//* &FraudLogData.getCacheTransTime& */	public int getCacheTransTime()	{		return ciCacheTransTime;	}	/**	 * Return value of csDocNo	 * 	 * @return String 	 *//* &FraudLogData.getDocNo& */	public String getDocNo()	{		return csDocNo;	}	/**	 * Return value of ciFraudCd	 * 	 * @return int 	 *//* &FraudLogData.getFraudCd& */	public int getFraudCd()	{		return ciFraudCd;	}	/**	 * Return value of csFraudDesc	 * 	 * @return String 	 *//* &FraudLogData.getFraudDesc& */	public String getFraudDesc()	{		return csFraudDesc;	}	/**	 * Return value of ciFraudIdntyNo	 * 	 * @return int 	 *//* &FraudLogData.getFraudIdntyNo& */	public int getFraudIdntyNo()	{		return ciFraudIdntyNo;	}	/**	 * Return value of ciOfcIssuanceNo	 * 	 * @return int 	 *//* &FraudLogData.getOfcIssuanceNo& */	public int getOfcIssuanceNo()	{		return ciOfcIssuanceNo;	}	/**	 * Return value of csRegPltNo	 * 	 * @return String 	 *//* &FraudLogData.getRegPltNo& */	public String getRegPltNo()	{		return csRegPltNo;	}	/**	 * Return value of ciSubstaId	 * 	 * @return int 	 *//* &FraudLogData.getSubstaId& */	public int getSubstaId()	{		return ciSubstaId;	}	/**	 * Return value of ciTransAMDate	 * 	 * @return int 	 *//* &FraudLogData.getTransAMDate& */	public int getTransAMDate()	{		return ciTransAMDate;	}	/**	 * Return value of csTransEmpId	 * 	 * @return String 	 *//* &FraudLogData.getTransEmpId& */	public String getTransEmpId()	{		return csTransEmpId;	}	/**	 * Return value of ciTransTime	 * 	 * @return int 	 *//* &FraudLogData.getTransTime& */	public int getTransTime()	{		return ciTransTime;	}	/**	 * Return value of caTransTimestmp	 * 	 * @return RTSDate 	 *//* &FraudLogData.getTransTimestmp& */	public RTSDate getTransTimestmp()	{		return caTransTimestmp;	}	/**	* Return value of ciTransAMDate	* 	* @return int 	*//* &FraudLogData.getTransWsId& */	public int getTransWsId()	{		return ciTransWsId;	}	/**	 * Return value of csFraudDesc	 * 	 * @return String 	 *//* &FraudLogData.getVIN& */	public String getVIN()	{		return csVIN;	}	/**	 * Set value of ciAddFraudIndi	 *	 * @param aiAddFraudIndi	 *//* &FraudLogData.setAddFraudIndi& */	public void setAddFraudIndi(int aiAddFraudIndi)	{		ciAddFraudIndi = aiAddFraudIndi;	}	/**	 * Set value of ciCacheTransAMDate	 * 	 * @param aiCacheTransAMDate	 *//* &FraudLogData.setCacheTransAMDate& */	public void setCacheTransAMDate(int aiCacheTransAMDate)	{		ciCacheTransAMDate = aiCacheTransAMDate;	}	/**	 * Set value of ciCacheTransTime	 * 	 * @param aiCacheTransTime	 *//* &FraudLogData.setCacheTransTime& */	public void setCacheTransTime(int aiCacheTransTime)	{		ciCacheTransTime = aiCacheTransTime;	}	/**	 * Set value of csFraudDesc	 * 	 * @return String 	 *//* &FraudLogData.setDocNo& */	public void setDocNo(String string)	{		csDocNo = string;	}	/**	 * Set value of ciAddFraudIndi	 *	 * @param aiFraudCd	 *//* &FraudLogData.setFraudCd& */	public void setFraudCd(int aiFraudCd)	{		ciFraudCd = aiFraudCd;	}	/**	 * Set value of csFraudDesc	 * 	 * @param asFraudDesc	 *//* &FraudLogData.setFraudDesc& */	public void setFraudDesc(String asFraudDesc)	{		csFraudDesc = asFraudDesc;	}	/**	 * Set value of ciAddFraudIndi	 *	 * @param aiFraudIdntyNo	 *//* &FraudLogData.setFraudIdntyNo& */	public void setFraudIdntyNo(int aiFraudIdntyNo)	{		ciFraudIdntyNo = aiFraudIdntyNo;	}	/**	 * Set value of ciOfcIssuanceNo	 *	 * @param aiOfcIssuanceNo	 *//* &FraudLogData.setOfcIssuanceNo& */	public void setOfcIssuanceNo(int aiOfcIssuanceNo)	{		ciOfcIssuanceNo = aiOfcIssuanceNo;	}	/**	 * Set value of csRegPltNo	 *	 * @param asRegPltNo	 *//* &FraudLogData.setRegPltNo& */	public void setRegPltNo(String asRegPltNo)	{		csRegPltNo = asRegPltNo;	}	/**	 * Set value of ciSubstaId	 *	 * @param aiSubstaId	 *//* &FraudLogData.setSubstaId& */	public void setSubstaId(int aiSubstaId)	{		ciSubstaId = aiSubstaId;	}	/**	 * Set value of ciTransAMDate	 *	 * @param aiTransAMDate	 *//* &FraudLogData.setTransAMDate& */	public void setTransAMDate(int aiTransAMDate)	{		ciTransAMDate = aiTransAMDate;	}	/**	 * Set value of csTransEmpId	 *	 * @param asTransEmpId	 *//* &FraudLogData.setTransEmpId& */	public void setTransEmpId(String asTransEmpId)	{		csTransEmpId = asTransEmpId;	}	/**	 * Set value of ciTransTime	 *	 * @param aiTransTime	 *//* &FraudLogData.setTransTime& */	public void setTransTime(int aiTransTime)	{		ciTransTime = aiTransTime;	}	/**	 * Set value of caTransTimestmp	 *	 * @param aaTransTimestmp	 *//* &FraudLogData.setTransTimestmp& */	public void setTransTimestmp(RTSDate aaTransTimestmp)	{		caTransTimestmp = aaTransTimestmp;	}	/**	 * Set value of ciTransWsId	 *	 * @param aiTransWsId	 *//* &FraudLogData.setTransWsId& */	public void setTransWsId(int aiTransWsId)	{		ciTransWsId = aiTransWsId;	}	/**	 * Set value of csVIN	 *	 * @param asVIN	 *//* &FraudLogData.setVIN& */	public void setVIN(String asVIN)	{		csVIN = asVIN;	}		/**	 * Method used to return field attributes. 	 * 	 * @return Map	 *//* &FraudLogData.getAttributes& */	public Map getAttributes()	{		HashMap lhmHashMap = new HashMap();		Field[] larrFields = this.getClass().getDeclaredFields();		for (int i = 0; i < larrFields.length; i++)		{			try			{				lhmHashMap.put(					larrFields[i].getName(),					larrFields[i].get(this));			}			catch (IllegalAccessException aeIllAccEx)			{				continue;			}		}		return lhmHashMap;	}}/* #FraudLogData# */