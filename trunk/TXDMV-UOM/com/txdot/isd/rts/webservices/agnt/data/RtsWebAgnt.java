package com.txdot.isd.rts.webservices.agnt.data;

import java.util.Calendar;
import java.util.Date;

/*
 * RtsWebAgnt.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/11/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * The data definition of Agent for web services.
 *
 * @version	6.7.0			01/11/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/11/2011 11:43:20
 */

public class RtsWebAgnt
{
	private Calendar caChngTimeStmp;
	private boolean cbDmvUserIndi;
	private int ciAgntIdntyNo;
	private int ciInitOfcNo;
	private String csEMail;
	private String csPhone;
	private String csFstName;
	private String csLstName;
	private String csMiName;
	private String csUserName;

	/**
	 * Get the Agent's EMail Address.
	 * 
	 * @return String
	 */
	public String getEMail()
	{
		return csEMail;
	}

	/**
	 * Get the Agent Identity Number.
	 * 
	 * @return int
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * Get the Agent's Phone Number.
	 * 
	 * @return String
	 */
	public String getPhone()
	{
		return csPhone;
	}

	/**
	 * Get the Last Change Time Stamp
	 * 
	 * @return Calendar
	 */
	public Calendar getChngTimeStmp()
	{
		return caChngTimeStmp;
	}
	
	/**
	 * Get the Last Change Date
	 * 
	 * @return Date
	 */
	public Date getChngTimeStmpDate()
	{
		return caChngTimeStmp.getTime();
	}

	/**
	 * Get the Agent's First Name.
	 *  
	 * @return String
	 */
	public String getFstName()
	{
		return csFstName;
	}

	/**
	 * Get the Agent's Initial Office Number
	 * 
	 * @return int
	 */
	public int getInitOfcNo()
	{
		return ciInitOfcNo;
	}

	/**
	 * Get the Agent's Last Name
	 * 
	 * @return String
	 */
	public String getLstName()
	{
		return csLstName;
	}

	/**
	 * Get the Agent's Middle Initial.
	 * 
	 * @return String
	 */
	public String getMiName()
	{
		return csMiName;
	}

	/**
	 * Get the Agent's User Name.
	 * 
	 * @return String
	 */
	public String getUserName()
	{
		return csUserName;
	}

	/**
	 * Get the DMV User Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isDmvUserIndi()
	{
		return cbDmvUserIndi;
	}

	/**
	 * Set the Agent's EMail Address.
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
	}

	/**
	 * Set the Agent Identity Number.
	 * 
	 * @param aiAgntIdntyNo
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		ciAgntIdntyNo = aiAgntIdntyNo;
	}

	/**
	 * Get the TimeStamp of the last change.
	 * 
	 * @param aaChngTimeStmp
	 */
	public void setChngTimeStmp(Calendar aaChngTimeStmp)
	{
		caChngTimeStmp = aaChngTimeStmp;
	}

	/**
	 * Set the DMV User Indi.
	 * 
	 * @param abDmvUserIndi
	 */
	public void setDmvUserIndi(boolean abDmvUserIndi)
	{
		cbDmvUserIndi = abDmvUserIndi;
	}

	/**
	 * Set the Agent's Phone Number.
	 * 
	 * @param asPhone
	 */
	public void setEmpPhone(String asPhone)
	{
		csPhone = asPhone;
	}

	/**
	 * Set the Agent's First Name.
	 * 
	 * @param asFstName
	 */
	public void setFstName(String asFstName)
	{
		csFstName = asFstName;
	}

	/**
	 * Set the Agent's Initial Office.
	 * 
	 * @param aiInitOfcNo
	 */
	public void setInitOfcNo(int aiInitOfcNo)
	{
		ciInitOfcNo = aiInitOfcNo;
	}

	/**
	 * Set the Agent's Last Name.
	 * 
	 * @param asLstName
	 */
	public void setLstName(String asLstName)
	{
		csLstName = asLstName;
	}

	/**
	 * Set the Agent's Middle Initial.
	 * 
	 * @param asMiName
	 */
	public void setMiName(String asMiName)
	{
		csMiName = asMiName;
	}

	/**
	 * Set the Agent's UserName.
	 * 
	 * @param asUserName
	 */
	public void setUserName(String asUserName)
	{
		csUserName = asUserName;
	}

}
