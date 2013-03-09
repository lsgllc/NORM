package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * WebAgencyDatajava
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0
 * K Harrell	01/10/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0    
 * K McKee      09/01/2011  added ciUpdtngAgntIdntyNo;
 *                          defect 10729 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyData
 *
 *
 * @version	6.8.1			01/10/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010  18:00:17 
 */
public class WebAgencyData
	extends NameAddressData
	implements java.io.Serializable
{
	private int ciAgncyIdntyNo;
	private int ciDeleteIndi;
	private int ciInitOfcNo;
	private int ciUpdtngAgntIdntyNo;

	private String csAgncyTypeCd;
	private String csCntctName;
	private String csEMail;
	private String csPhone;

	private RTSDate caChngTimestmp;
	

	static final long serialVersionUID = 4277456900096008162L;

	/**
	 * WebAgencyData.java Constructor
	 * 
	 */
	public WebAgencyData()
	{
		super();
	}

	/**
	 * Get value of ciAgncyIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Get value of csAgncyTypeCd
	 * 
	 * @return String 
	 */
	public String getAgncyTypeCd()
	{
		return csAgncyTypeCd;
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
	 * Get value of csCntctName
	 * 
	 * @return String 
	 */
	public String getCntctName()
	{
		return csCntctName;
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
	 * Get value of csEMail
	 * 
	 * @return String 
	 */
	public String getEMail()
	{
		return csEMail;
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
	 * Get value of csPhone
	 * 
	 * @return String 
	 */
	public String getPhone()
	{
		return csPhone;
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
	 * Set value of ciAgncyIdntyNo
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Set value of csAgncyTypeCd
	 * 
	 * @param asAgncyTypeCd
	 */
	public void setAgncyTypeCd(String asAgncyTypeCd)
	{
		csAgncyTypeCd = asAgncyTypeCd;
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
	 * Set value of csCntctName
	 * 
	 * @param asCntctName
	 */
	public void setCntctName(String asCntctName)
	{
		csCntctName = asCntctName;
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
	 * Set value of csEMail
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
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
	 * Set value of csPhone
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
	}



	/**
	 * Set value of ciUpdtngAgntIdntyNo
	 * @param as ciUpdtngAgntIdntyNo  
	 */
	public void setUpdtngAgntIdntyNo(int ciUpdtngAgntIdntyNo)
	{
		this.ciUpdtngAgntIdntyNo = ciUpdtngAgntIdntyNo;
	}

}
