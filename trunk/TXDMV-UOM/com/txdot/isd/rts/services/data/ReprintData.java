package com.txdot.isd.rts.services.data;

import java.lang.reflect.*;
import com.txdot.isd.rts.services.util.*;

/*
 *
 * ReprintData.java
 *
 * (c) Texas Department of Transportation 2002
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New Class. Modified class variable 
 * 							names to meet standards. 
 * 							Ver 5.2.0
 * K Harrell	10/10/2004	add ciReprntQty,csRegPltNo and
 *							associated get/set methods
 *							renamed csVin to csVIN and modified
 *							associated get/set methods
 *							defects 7597, 7598  Ver 5.2.1
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	12/17/2009	add isValidForDBInsert()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	03/15/2010 	Add serialVersionUID
 * 							defect 10363 Ver POS_640
 * K Harrell	07/14/2010	add csInvItmNo, get/set methods
 * 							defect 10491 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get/set methods for 
 * ReprintData
 * 
 * @version	6.5.0  			07/14/2010
 * @author	Michael Abernethy
 * <br>Creation Date:		09/04/2002
 */
public class ReprintData implements java.io.Serializable, Displayable
{


	// Object 
	private RTSDate caReprntDate;
	
	//int
	private int ciCustSeqNo;
	private int ciDiskNum;
	private int ciItmYr;
	private int ciOfcIssuanceNo;
	private int ciOriginId;
	private int ciPrntQty;
	private int ciReprntQty;
	private int ciSubstaId;
	private int ciVoided;
	private int ciWsId;

	//String
	private String csEmpId;
	
	// defect 10491 
	private String csInvItmNo;
	// end defect 10491 
	
	private String csItmCd;
	private String csItmCdDesc;
	private String csOrigin;
	private String csRegPltNo;
	private String csScannerId;
	private String csTransId;
	private String csVIN;
	
	static final long serialVersionUID = -7024777853402386400L;

	/**
	 * ReprintData constructor comment.
	 */
	public ReprintData()
	{
		super();
	}
	/**
	 * Returns a Map of the internal attributes.  Implementers of 
	 * this method should use introspection to display their 
	 * internal variables and values
	 *
	 * @return java.util.Map
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap lhmHashMap = new java.util.HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHashMap;
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
	 * Returns the value of DiskNum
	 * 
	 * @return int
	 */
	public int getDiskNum()
	{
		return ciDiskNum;
	}
	
	/**
	 * Returns the value of EmpId
	 * 
	 * @return String
	 */
	public String getEmpId()
	{
		return csEmpId;
	}
	/**
	 * Returns value of csInvItmNo;
	 * 
	 * @return String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}
	
	/**
	 * Returns the value of ItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}
	
	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return String
	 */
	public String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	
	/**
	 * Returns the value of ItmYr
	 * 
	 * @return int
	 */
	public int getItmYr()
	{
		return ciItmYr;
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
	 * Returns the value of Origin
	 * 
	 * @return String
	 */
	public String getOrigin()
	{
		return csOrigin;
	}
	
	/**
	 * Returns the value of OriginId
	 * 
	 * @return int
	 */
	public int getOriginId()
	{
		return ciOriginId;
	}
	
	/**
	 * Returns the value of PrntQty
	 * 
	 * @return int
	 */
	public int getPrntQty()
	{
		return ciPrntQty;
	}
	
	/**
	 * Return value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	
	/**
	 * Return value of ReprntDate
	 *  
	 * @return RTSDate
	 */
	public RTSDate getReprntDate()
	{
		return caReprntDate;
	}
	
	/**
	 * Returns the value of ReprntQty
	 * 
	 * @return int
	 */
	public int getReprntQty()
	{
		return ciReprntQty;
	}
	
	/**
	 * Returns the value of ScannerId
	 *
	 * @return String
	 */
	public String getScannerId()
	{
		return csScannerId;
	}
	
	/**
	 * Returns the value of Substaid
	 *
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}
	
	/**
	 * Returns the value of TransId
	 *
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
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
	 * Returns the value of Voided
	 *
	 * @return boolean
	 */
	public int getVoided()
	{
		return ciVoided;
	}
	
	/**
	 * Returns the value of Wsid
	 *
	 * @return int
	 */
	public int getWsId()
	{
		return ciWsId;
	}

	/**
	 * Return boolean to denote if object is valid for DB2 Insert, i.e.
	 * following not null will be honored: 
	 * - OriginCd
	 * - ItmCd
	 * - RSPSId
	 * - RegPltNo
	 * 
	 * @return boolean
	 */
	public boolean isValidForDB2Insert()
	{
		return !UtilityMethods.isEmpty(csOrigin)
			&& !UtilityMethods.isEmpty(csItmCd)
			&& !UtilityMethods.isEmpty(csScannerId)
			&& !UtilityMethods.isEmpty(csRegPltNo);
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
	 * Sets the value of DiskNum
	 * 
	 * @param aiDiskNum int
	 */
	public void setDiskNum(int aiDiskNum)
	{
		ciDiskNum = aiDiskNum;
	}
	
	/**
	 * Sets the value of EmpId
	 * 
	 * @param asEmpId String
	 */
	public void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}

	/**
	 * Sets value of csInvItmNo;
	 * 
	 * @param string
	 */
	public void setInvItmNo(String string)
	{
		csInvItmNo = string;
	}
	
	/**
	 * Sets the value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public void setItemDescription(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	
	/**
	 * Sets the value of ItmCd
	 * 
	 * @param asItmCode String
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	
	/**
	 * Sets the value of ItmYr
	 * 
	 * @param aiItmYear int
	 */
	public void setItmYr(int aiItmYr)
	{
		ciItmYr = aiItmYr;
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
	 * Sets the value of Origin
	 * 
	 * @param asOrigin String
	 */
	public void setOrigin(String asOrigin)
	{
		csOrigin = asOrigin;
	}
	
	/**
	 * Sets the value of OriginId
	 * 
	 * @param aiOriginId int
	
	 */
	public void setOriginId(int aiOriginId)
	{
		ciOriginId = aiOriginId;
	}
	
	/**
	 * Sets the value of PrntQty
	 * 
	 * @param aiPrintQty int
	 */
	public void setPrntQty(int aiPrntQty)
	{
		ciPrntQty = aiPrntQty;
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
	 * Sets the value of cReprntDate
	 * 
	 * @param aaReprntDate RTSDate
	 */
	public void setReprntDate(RTSDate aaReprntDate)
	{
		caReprntDate = aaReprntDate;
	}
	
	/**
	 * Sets the value of ReprntQty
	 * 
	 * @param aiReprntQty int
	 */
	public void setReprntQty(int aiReprntQty)
	{
		ciReprntQty = aiReprntQty;
	}
	
	/**
	 * Sets the value of ScannerId
	 * @param asScannerId String
	 */
	public void setScannerId(String asScannerId)
	{
		csScannerId = asScannerId;
	}
	/**
	 * Sets the value of SubstaId
	 * @param aiSubstationId int
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	
	/**
	 * Sets the value of TransId
	 * @param asTransId String
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}
	
	/**
	 * Sets the value of VIN
	 * @param asVIN String
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
	
	/**
	 * Sets the value of Voided
	 * @param aiVoided boolean
	 */
	public void setVoided(int aiVoided)
	{
		ciVoided = aiVoided;
	}

	/**
	 * Sets the value of WsId
	 * 
	 * @param aiWsId int
	 */
	public void setWsId(int aiWsId)
	{
		ciWsId = aiWsId;
	}

}
