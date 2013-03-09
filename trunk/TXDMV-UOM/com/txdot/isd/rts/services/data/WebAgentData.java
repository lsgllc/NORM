package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;

/*
 * WebAgentData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0   
 * K Harrell 	01/05/2011 	Renamings per standards 
 *        					defect 10708 Ver 6.7.0  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgentData 
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgentData implements Serializable
{
	private int ciAgntIdntyNo;
	private int ciDeleteIndi;
	private int ciDMVUserIndi;
	private int ciInitOfcNo;
	private int ciUpdtngAgntIdntyNo;
	
	private String csEMail;
	private String csPhone;
	private String csFstName;
	private String csLstName;
	private String csMIName;
	private String csUserName;
	
	private RTSDate caChngTimestmp;
	
	static final long serialVersionUID = 6280748746476666093L;

	/**
	 * WebAgentData.java Constructor
	 * 
	 */
	public WebAgentData()
	{
		super();
	}

	public WebAgentData(RtsWebAgntWS aaObject)
	{
		super();
		setAgntIdntyNo(aaObject.getAgntIdntyNo());
		setDMVUserIndi(aaObject.isDmvUserIndi() ? 1 : 0);
		setInitOfcNo(aaObject.getInitOfcNo());
		setEMail(aaObject.getEMail());
		setPhone(aaObject.getPhone());
		setFstName(aaObject.getFstName());
		setLstName(aaObject.getLstName());
		setMIName(aaObject.getMiName());
		setUserName(aaObject.getUserName());
		setUpdtngAgntIdntyNo(aaObject.getUpdtngAgntIdntyNo());
	}
	
	/**
	 * Get value of csEMail
	 * 
	 * @return int 
	 */
	public String getEMail()
	{
		return csEMail;
	}

	/**
	 * Get value of ciAgntIdntyNo
	 * 
	 * @return int
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * Get value of csPhone
	 * 
	 * @return String 
	 */
	public String getPhone()
	{
		return csPhone;
	}

	/**
	 * Get value of caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}

	/**
	 * Get value of ciDeleteIndi
	 * 
	 * @return int 
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Get value of ciDMVUserIndi
	 * 
	 * @return int 
	 */
	public int getDMVUserIndi()
	{
		return ciDMVUserIndi;
	}

	/**
	 * Get value of csFstName
	 * 
	 * @return String
	 */
	public String getFstName()
	{
		return csFstName;
	}

	/**
	 * Get value of ciInitOfcNo
	 * 
	 * @return int 
	 */
	public int getInitOfcNo()
	{
		return ciInitOfcNo;
	}

	/**
	 * Get value of csLstName
	 * 
	 * @return String
	 */
	public String getLstName()
	{
		return csLstName;
	}

	/**
	 * Get value of csMIName
	 * 
	 * @return String
	 */
	public String getMIName()
	{
		return csMIName;
	}

	/**
	 * Get value of ciUpdtngAgntIdntyNo
	 * 
	 * @return int 
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
	}

	/**
	 * Get value of csUserName
	 * 
	 * @return String
	 */
	public String getUserName()
	{
		return csUserName;
	}

	/**
	 * Set value of csEMail
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
	}

	/**
	 * Set value of ciAgntIdntyNo
	 * 
	 * @param aiAgntIdntyNo
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		ciAgntIdntyNo = aiAgntIdntyNo;
	}

	/**
	 * Set value of csPhone
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set value of ciDMVUserIndi
	 * 
	 * @param aiDMVUserIndi
	 */
	public void setDMVUserIndi(int aiDMVUserIndi)
	{
		ciDMVUserIndi = aiDMVUserIndi;
	}

	/**
	 * Set value of csFstName
	 * 
	 * @param asFstName
	 */
	public void setFstName(String asFstName)
	{
		csFstName = asFstName;
	}

	/**
	 * Set value of ciInitOfcNo
	 * 
	 * @param aiInitOfcNo
	 */
	public void setInitOfcNo(int aiInitOfcNo)
	{
		ciInitOfcNo = aiInitOfcNo;
	}

	/**
	 * Set value of csLstName
	 * 
	 * @param asLstName
	 */
	public void setLstName(String asLstName)
	{
		csLstName = asLstName;
	}

	/**
	 * Set value of csMIName
	 * 
	 * @param asMIName
	 */
	public void setMIName(String asMIName)
	{
		csMIName = asMIName;
	}

	/**
	 * Set value of ciUpdtngAgntIdntyNo
	 * 
	 * @param aiUpdtngAgntIdntyNo
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}

	/**
	 * Set value of csUserName
	 * 
	 * @param asUserName
	 */
	public void setUserName(String asUserName)
	{
		csUserName = asUserName;
	}

}
