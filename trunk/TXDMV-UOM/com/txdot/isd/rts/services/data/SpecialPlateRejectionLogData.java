package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * SpecialPlateRejectionLogData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/06/2007	Created
 *							defect 9805 Ver Special Plates
 * Ray Rowehl	02/19/2007	Add inetindi setter from boolean.
 * 							add setInetReqIndi(boolean)
 * 							defect 9117 Ver Special Plates
 * Ray Rowehl	04/12/2007	Add handling so Error Message Desc can be 
 * 							handled for the report.
 * 							add getErrMsgDesc(), setErrMsgDesc()
 * 							defect 9117 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * SpecialPlateRejectionLogData.
 *
 * @version	Special Plates	04/12/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		02/06/2007 15:25:00
 */
public class SpecialPlateRejectionLogData implements Serializable
{
	// int
	protected int ciItrntReqIndi;
	protected int ciOfcIssuanceNo;
	protected int ciRejReasnCd;
	protected int ciTransAMDate;
	protected int ciTransWsId;

	//	String 
	private String csErrMsgDesc;
	protected String csInvItmNo;
	protected String csMfgPltNo;
	protected String csRegPltNo;
	protected String csReqIPAddr;
	protected String csTransEmpId;

	// Object
	protected RTSDate caReqTimestmp;

	static final long serialVersionUID = 8787132201994290603L;

	/**
	 * Returns the Error Message Description.
	 * 
	 * @return String
	 */
	public String getErrMsgDesc()
	{
		return csErrMsgDesc;
	}
	
	/**
	 * Return value of caReqTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getReqTimestmp()
	{
		return caReqTimestmp;
	}

	/**
	 * Return value of ciItrntReqIndi
	 * 
	 * @return int
	 */
	public int getItrntReqIndi()
	{
		return ciItrntReqIndi;
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
	 * Return value of ciRejReasnCd
	 * 
	 * @return int
	 */
	public int getRejReasnCd()
	{
		return ciRejReasnCd;
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
	 * Return value of ciTransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Return value of csInvItmNo
	 * 
	 * @return String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Return value of csMfgPltNo
	 * 
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
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
	 * Return value of csReqIPAddr
	 * 
	 * @return String
	 */
	public String getReqIPAddr()
	{
		return csReqIPAddr;
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
	 * Sets the Error Message Description.
	 * 
	 * @param asErrMsgDesc String
	 */
	public void setErrMsgDesc(String asErrMsgDesc)
	{
		csErrMsgDesc = asErrMsgDesc;
	}
	
	/**
	 * Set value of caReqTimestmp
	 * 
	 * @param aaReqTimestmp
	 */
	public void setReqTimestmp(RTSDate aaReqTimestmp)
	{
		caReqTimestmp = aaReqTimestmp;
	}

	/**
	 * Set value of ciItrntReqIndi
	 * 
	 * @param aiItrntReqIndi
	 */
	public void setItrntReqIndi(int aiItrntReqIndi)
	{
		ciItrntReqIndi = aiItrntReqIndi;
	}
	
	/**
	 * Set value of ciItrntReqIndi from a boolean.
	 * 
	 * @param abInetReqIndi boolean
	 */
	public void setItrntReqIndi(boolean abItrntReqIndi)
	{
		if (abItrntReqIndi)
		{
			ciItrntReqIndi = 1;
		}
		else
		{
			ciItrntReqIndi = 0;
		}
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
	 * Set value of ciRejReasnCd 
	 * 
	 * @param aiRejReasnCd
	 */
	public void setRejReasnCd(int aiRejReasnCd)
	{
		ciRejReasnCd = aiRejReasnCd;
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
	 * Set value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set value of csInvItmNo
	 * 
	 * @param asInvItmNo
	 */
	public void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}

	/**
	 * Set value of csMfgPltNo
	 * 
	 * @param asMfgPltNo
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
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
	 * Set value of csReqIPAddr
	 * 
	 * @param asReqIPAddr
	 */
	public void setReqIPAddr(String asReqIPAddr)
	{
		csReqIPAddr = asReqIPAddr;
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
}