package com.txdot.isd.rts.webservices.agncy.data;

import java.util.Calendar;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.common.data.RtsNameAddress;

/*
 * AgncyData.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/11/2011	Initial load.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	01/24/2011	Reflow to make all Agency data one object
 * 							for web services purposes.
 * 							defect 10718 Ver 6.7.0
 * Dan Hamilton 06/29/2011  Sort the setters in the constructor, add 
 *                          setters for ChngAgncy & ChngAgncyCfg 
 * 							defect 10718 Ver 6.8.0
 * K McKee      09/01/2011  Set the setUpdtngUserName 
 *                          defect 10729 Ver 6.8.1
 * K McKee      11/04/2011  add carrAgncyAuthCfgs  get/set methods
 * 							removed the RtsWebAgncyAuthCfg fields and get/set methods
 * 							modify RtsWebAgncy(RtsAgncyRequest)
 *                          defect 11151 Ver 6.9.0
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setAgncyNameAddress()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * The data definition of Agency for web services.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		01/11/2011 16:00:00
 */

public class RtsWebAgncy
{
	// defect 11151
	private RtsWebAgncyAuthCfg[] carrAgncyAuthCfgs;
	// end defect 11151
	
	private Calendar caChngTimeStmpAgncy;
	
	private int ciAgncyIdntyNo;
	private int ciInitOfcNo;
	
	private String csAgncyTypeCd;
	private String csCity;
	private String csCntctName;
	private String csEMail;
	private String csName1;
	private String csName2;
	private String csPhone;
	private String csSt1;
	private String csSt2;
	private String csState;
	private String csUpdtngUserName;
	private String csZpCd;
	private String csZpCdP4;
	
	private boolean cbDeleteIndi;
	private boolean cbChngAgncy;
	private boolean cbChngAgncyCfg;

	/**
	 * AgncyData Constructor 
	 */
	public RtsWebAgncy()
	{
		super();
	}
	
	/**
	 * AgncyData Constructor 
	 * 
	 * @param aaRequest   RtsAgncyRequest
	 */
	public RtsWebAgncy(RtsAgncyRequest aaRequest)
	{
		super();
		// defect 10729 -- removed the setters for the Auth and Cfg fields
		setAgncyIdntyNo(aaRequest.getRtsWebAgncyInput().getAgncyIdntyNo());
		setAgncyTypeCd(aaRequest.getRtsWebAgncyInput().getAgncyTypeCd());
		setChngAgncy(aaRequest.getRtsWebAgncyInput().isChngAgncy());
		setChngAgncyCfg(aaRequest.getRtsWebAgncyInput().isChngAgncyCfg());
		setCity(aaRequest.getRtsWebAgncyInput().getCity());
		setCntctName(aaRequest.getRtsWebAgncyInput().getCntctName());
		setInitOfcNo(aaRequest.getRtsWebAgncyInput().getInitOfcNo());
		setEMail(aaRequest.getRtsWebAgncyInput().getEMail());
		setName1(aaRequest.getRtsWebAgncyInput().getName1());
		setName2(aaRequest.getRtsWebAgncyInput().getName2());
		setPhone(aaRequest.getRtsWebAgncyInput().getPhone());
		setSt1(aaRequest.getRtsWebAgncyInput().getSt1());
		setSt2(aaRequest.getRtsWebAgncyInput().getSt2());
		setState(aaRequest.getRtsWebAgncyInput().getState());
		setZpCd(aaRequest.getRtsWebAgncyInput().getZpCd());
		setZpCdP4(aaRequest.getRtsWebAgncyInput().getZpCdP4());
		// defect 11151
		setAgncyAuthCfgs(aaRequest.getRtsWebAgncyInput().getAgncyAuthCfgs());
		// end defect 11151
	// defect 10729
		setUpdtngUserName(aaRequest.getRtsWebAgncyInput().getUpdtngUserName());
	// end defect 10729
	}
	
	/**
	 * Create a WebAgentData object from the existing object.
	 * 
	 * @return WebAgentData
	 */
	public RtsNameAddress getAgncyNameAddress()
	{
		RtsNameAddress laAgncyNameAddress = new RtsNameAddress();
		laAgncyNameAddress.setName1(getName1());
		laAgncyNameAddress.setName2(getName2());
		laAgncyNameAddress.getRtsAddress().setStreetLine1(getSt1());
		laAgncyNameAddress.getRtsAddress().setStreetLine2(getSt2());
		laAgncyNameAddress.getRtsAddress().setCity(getCity());
		laAgncyNameAddress.getRtsAddress().setState(getState());
		laAgncyNameAddress.getRtsAddress().setZip(getZpCd());
		laAgncyNameAddress.getRtsAddress().setZipP4(getZpCdP4());
		return laAgncyNameAddress;
	}

	/**
	 * @return the carrAgncyAuthCfg
	 */
	public RtsWebAgncyAuthCfg[] getAgncyAuthCfgs()
	{
		return carrAgncyAuthCfgs;
	}
	
	/**
	 * Get the Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Get Agency Type Code.
	 * 
	 * @return String
	 */
	public String getAgncyTypeCd()
	{
		return csAgncyTypeCd;
	}

	/**
	 * Get the Agency Change TimeStamp.
	 * 
	 * @return Calendar
	 */
	public Calendar getChngTimeStmpAgncy()
	{
		return caChngTimeStmpAgncy;
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
	 * Get the Updating Agent's UserName.
	 * 
	 * @return String
	 */
	public String getUpdtngUserName()
	{
		return csUpdtngUserName;
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
	 * Get change Agency Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isChngAgncy()
	{
		return cbChngAgncy;
	}

	/**
	 * Get change Agency Auth_Cfg Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isChngAgncyCfg()
	{
		return cbChngAgncyCfg;
	}
	
	/**
	 * @return the cbDeleteIndi
	 */
	public boolean isDeleteIndi()
	{
		return cbDeleteIndi;
	}

	/**
	 * @param aarrAgncyAuthCfg the AgncyAuthCfg[] to set
	 */
	public void setAgncyAuthCfgs(RtsWebAgncyAuthCfg[] aarrAgncyAuthCfgs)
	{
		this.carrAgncyAuthCfgs = aarrAgncyAuthCfgs;
	}
	
	/**
	 * Set Agency Identity Number.
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Null setter method required to prevent for web services validation 
	 * error.
	 * 
	 * @param aaRtsNameAddress
	 */
	public void setAgncyNameAddress(RtsNameAddress aaRtsNameAddress)
	{
		// null setter
	}
	
	/**
	 * Set Agency Type Code.
	 * 
	 * @param asAgncyTypeCd
	 */
	public void setAgncyTypeCd(String asAgncyTypeCd)
	{
		csAgncyTypeCd = asAgncyTypeCd;
	}

	/**
	 * Set the Agency Changed indicator.
	 * 
	 * @param abChngAgncy
	 */
	public void setChngAgncy(boolean abChngAgncy)
	{
		cbChngAgncy = abChngAgncy;
	}

	/**
	 * Set the Agency Auth_Cfg Changed indicator.
	 * 
	 * @param abChngAgncyCfg
	 */
	public void setChngAgncyCfg(boolean abChngAgncyCfg)
	{
		cbChngAgncyCfg = abChngAgncyCfg;
	}

	/**
	 * Set the Agency Change TimeStamp.
	 * 
	 * @param aaChngTimeStmpAgncy
	 */
	public void setChngTimeStmpAgncy(Calendar aaChngTimeStmpAgncy)
	{
		caChngTimeStmpAgncy = aaChngTimeStmpAgncy;
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
	 * @param cbDeleteIndi the cbDeleteIndi to set
	 */
	public void setDeleteIndi(boolean abDeleteIndi)
	{
		this.cbDeleteIndi = abDeleteIndi;
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
	 * Set Agency Phone Number.
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
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
	 * Set the Updating Agent's UserName.
	 * 
	 * @param asUpdtngUserName
	 */
	public void setUpdtngUserName(String asUpdtngUserName)
	{
		csUpdtngUserName = asUpdtngUserName;
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
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateWebAgncy() throws RTSException
	{
		RTSException leRTSEx = null;
		
		// Make sure there is a Agency Type Code
		if (getAgncyTypeCd() == null || getAgncyTypeCd().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a City
		if (getCity() == null || getCity().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a Contact Name
		if (getCntctName() == null || getCntctName().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is an EMail
		if (getEMail() == null || getEMail().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is an Name 1
		if (getName1() == null || getName1().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a Phone
		if (getPhone() == null || getPhone().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a Street 1
		if (getSt1() == null || getSt1().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a State
		if (getState() == null || getState().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a Zipcode
		if (getZpCd() == null || getZpCd().length() < 1)
		{
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// if there is an exception, throw it.
		if (leRTSEx != null)
		{
			throw leRTSEx;
		}	
	}
}
