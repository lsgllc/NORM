package com.txdot.isd.rts.webservices.agncy.data;

import java.util.Calendar;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.WebAgencyData;

/*
 * AgncyData.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/27/2010	Initial load.
 * 							defect 10670 Ver 670
 * Ray Rowehl	12/29/2010	Update to match table.
 * 							defect 10670 Ver 670
 * ---------------------------------------------------------------------
 */

/**
 * The data definition of Agent for web services.
 *
 * @version	6.7.0			12/29/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		12/27/2010 12:37:10
 */

public class AgncyData
{
	private Calendar caChngTimestmp;
	private int ciAgencyIdntyNo;
	private int ciInitOfcNo;
	private int ciUpdtngAgentIdntyNo;
	private String csCity;
	private String csCntctName;
	private String csEMail;
	private String csName1;
	private String csName2;
	private String csPhone;
	private String csSt1;
	private String csSt2;
	private String csState;
	private String csAgencyTypeCd;
	private String csZpCd;
	private String csZpCdP4;
	private String csUpdtngAgentUserName;

	/**
	 * AgncyData Constructor 
	 */
	public AgncyData()
	{
		super();
	}

	/**
	 * AgncyData Constructor
	 * 
	 * <p>Take WebAgencyData as input. 
	 */
	public AgncyData(WebAgencyData aaObj)
	{
		super();
		setAgencyIdntyNo(aaObj.getAgncyIdntyNo());
		setName1(aaObj.getName1());
		setName2(aaObj.getName2());
		AddressData laAddrData = aaObj.getAddressData();
		setSt1(laAddrData.getSt1());
		setSt2(laAddrData.getSt2());
		setCity(laAddrData.getCity());
		setState(laAddrData.getState());
		setZpCd(laAddrData.getZpcd());
		setZpCdP4(laAddrData.getZpcdp4());
		setCntctName(aaObj.getCntctName());
		setPhone(aaObj.getPhone());
		setEMail(aaObj.getEMail());
		setAgencyTypeCd(aaObj.getAgncyTypeCd());
		setInitOfcNo(aaObj.getInitOfcNo());
		// TODO We don't have this?
		// will probably have to go check logs!
		setUpdtngAgentIdntyNo(0);
		setUpdtngAgentUserName("");

		// TODO Add get Calendar to RTSDate!
		//setChngTimestmp(aaObj.getChngTimestmp());
	}

	/**
	 * Get Agency City.
	 * 
	 * @return String
	 */
	public String getCity()
	{
		return csCity;
	}

	/**
	 * Get Agency Contact Name.
	 * 
	 * @return String
	 */
	public String getCntctName()
	{
		return csCntctName;
	}

	/**
	 * Get Agency EMail Address.
	 * 
	 * @return String
	 */
	public String getEMail()
	{
		return csEMail;
	}

	/**
	 * Get the Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgencyIdntyNo()
	{
		return ciAgencyIdntyNo;
	}

	/**
	 * Get Agency Name Line 1.
	 * 
	 * @return String
	 */
	public String getName1()
	{
		return csName1;
	}

	/**
	 * Get Agency Name Line 2.
	 * 
	 * @return String
	 */
	public String getName2()
	{
		return csName2;
	}

	/**
	 * Get Agency Phone Number.
	 * 
	 * @return String
	 */
	public String getPhone()
	{
		return csPhone;
	}

	/**
	 * Get Agency Street Line 1.
	 * 
	 * @return String
	 */
	public String getSt1()
	{
		return csSt1;
	}

	/**
	 * Get Agency Street Line 2.
	 * 
	 * @return String
	 */
	public String getSt2()
	{
		return csSt2;
	}

	/**
	 * Get Agency State.
	 * 
	 * @return String
	 */
	public String getState()
	{
		return csState;
	}

	/**
	 * Get Agency Type Code.
	 * 
	 * @return String
	 */
	public String getAgencyTypeCd()
	{
		return csAgencyTypeCd;
	}

	/**
	 * Get Agency Zip Code.
	 * 
	 * @return String
	 */
	public String getZpCd()
	{
		return csZpCd;
	}

	/**
	 * Get Agency Zip Code Plus 4.
	 * 
	 * @return String
	 */
	public String getZpCdP4()
	{
		return csZpCdP4;
	}

	/**
	 * Get Agency Change Timestamp.
	 * 
	 * @return Calendar
	 */
	public Calendar getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * get the Office Issuance Number of Office that initially 
	 * created this Agency.
	 * 
	 * @return int
	 */
	public int getInitOfcNo()
	{
		return ciInitOfcNo;
	}

	/**
	 * Get the Agent Identity Number that last updated this Agency.
	 * 
	 * @return int
	 */
	public int getUpdtngAgentIdntyNo()
	{
		return ciUpdtngAgentIdntyNo;
	}

	/**
	 * Set Agency City.
	 * 
	 * @param asAgencyCity
	 */
	public void setCity(String asCity)
	{
		csCity = asCity;
	}

	/**
	 * Set Agency Contact Name.
	 * 
	 * @param asAgencyCntctName
	 */
	public void setCntctName(String asCntctName)
	{
		csCntctName = asCntctName;
	}

	/**
	 * Set Agency EMail Address.
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
	}

	/**
	 * Set Agency Identity Number.
	 * 
	 * @param aiAgencyIdntyNo
	 */
	public void setAgencyIdntyNo(int aiAgencyIdntyNo)
	{
		ciAgencyIdntyNo = aiAgencyIdntyNo;
	}

	/**
	 * Set Agency Name Line 1.
	 * 
	 * @param asName1
	 */
	public void setName1(String asName1)
	{
		csName1 = asName1;
	}

	/**
	 * Set Agency Name Line 2.
	 * 
	 * @param asName2
	 */
	public void setName2(String asName2)
	{
		csName2 = asName2;
	}

	/**
	 * Set Agency Phone Number.
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
	}

	/**
	 * Set Agency Street Line 1.
	 * 
	 * @param asSt1
	 */
	public void setSt1(String asSt1)
	{
		csSt1 = asSt1;
	}

	/**
	 * Set Agency Street Line 2.
	 * 
	 * @param asSt2
	 */
	public void setSt2(String asSt2)
	{
		csSt2 = asSt2;
	}

	/**
	 * Set Agency State.
	 * 
	 * @param asState
	 */
	public void setState(String asState)
	{
		csState = asState;
	}

	/**
	 * Set Agency Type Code.
	 * 
	 * @param asAgencyTypeCd
	 */
	public void setAgencyTypeCd(String asAgencyTypeCd)
	{
		csAgencyTypeCd = asAgencyTypeCd;
	}

	/**
	 * Set Agency Zip Code.
	 * 
	 * @param asZpCd
	 */
	public void setZpCd(String asZpCd)
	{
		csZpCd = asZpCd;
	}

	/**
	 * Set Agency Zip Code Plus 4.
	 * 
	 * @param asZpCdP4
	 */
	public void setZpCdP4(String asZpCdP4)
	{
		csZpCdP4 = asZpCdP4;
	}

	/**
	 * Set Agency Change Timestamp.
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(Calendar aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set the Office Issuance Number of Office that initially 
	 * created this Agency.
	 * 
	 * @param aiInitOfcNo
	 */
	public void setInitOfcNo(int aiInitOfcNo)
	{
		ciInitOfcNo = aiInitOfcNo;
	}

	/**
	 * Set the Agent Identity Number that last updated this Agency.
	 * 
	 * @param aiUpdtngAgentIdntyNo
	 */
	public void setUpdtngAgentIdntyNo(int aiUpdtngAgentIdntyNo)
	{
		ciUpdtngAgentIdntyNo = aiUpdtngAgentIdntyNo;
	}

	/**
	 * Get the Updating Agent's UserName.
	 * 
	 * @return String
	 */
	public String getUpdtngAgentUserName()
	{
		return csUpdtngAgentUserName;
	}

	/**
	 * Set the Updating Agent's UserName.
	 * 
	 * @param asUpdtngAgentUserName
	 */
	public void setUpdtngAgentUserName(String asUpdtngAgentUserName)
	{
		csUpdtngAgentUserName = asUpdtngAgentUserName;
	}

}
