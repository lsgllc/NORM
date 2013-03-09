package com.txdot.isd.rts.webservices.ren.data;

import org.apache.log4j.Logger;

/*
 * RtsRegistrationData.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/15/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/07/2011	Add new fields as required by the table.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/28/2011	Remove Insurance booleans.
 * 							delete cbInsuranceRequired, cbInsuranceVerified
 * 							delete isInsuranceRequired(), 
 * 								isInsuranceVerified(), 
 * 								setInsuranceRequired(), 
 * 								setInsuranceVerified()
 * 							defect 10673 Ver 6.7.1
 * K McKee  	10/10/2011  Added Verified to prevent logging
 * 							modify getInsVerfdStr()
 * 							defect 10729 Ver 6.9.0
 * D Hamilton  	11/10/2011  Added null setter for InsVerfdStr
 * 							defect 10729 Ver 6.9.0
 * K McKee		1/25/2012   Set "V" to Electronic
 * 							modify getInsVerfdStr()
 * 							defect 11239 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Registration Data for Web Agent
 *
 * @version	6.9.0			11/10/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/15/2011 13:27:12
 */

public class RtsRegistrationData
{
	private static final Logger log = Logger.getLogger(RtsRegistrationData.class);

	private boolean cbMustReplPltIndi;
	private int ciNewRegExpMo;
	private int ciNewRegExpYr;
	private int ciPltAge;
	private int ciPltBirthDate;
	private int ciPrntQty;
	private int ciRegClassCd;
	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciResComptCntyNo;
	private int ciVehGrossWt;
	private String csInsVerfdCd = "R";
	private String csInvItmNo = "";
	private String csRegPltCd = "";
	private String csRegPltNo = "";
	private String csResComptCntyName = "";
	private String csStkrItmCd = "";
	
	/**
	 * Get the Insurance Verified Code.
	 * 
	 * @return String
	 */
	public String getInsVerfdCd()
	{
		return csInsVerfdCd;
	}
	
	/**
	 * Get the Insurance Verified Discription.
	 * 
	 * @return String
	 */
	public String getInsVerfdStr()
	{
		String insVerfdStr = "";
		switch (csInsVerfdCd.charAt(0))
		{
			case 'M' :
				insVerfdStr = "Manual";
				break;

			case 'E' :
				insVerfdStr = "Electronic";
				break;

			case 'N' :
				insVerfdStr = "Not Required";
				break;
			
			case 'V' :
			// defect 1
				insVerfdStr = "Electronic";
				break;

			default :
				log.warn("csInsVerfdCd did not contain an expected value, it was: " + csInsVerfdCd);
				break;
		}
		return insVerfdStr;
	}
	
	/**
	 * Get the Inverntory Item Number issued.
	 * 
	 * @return String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}
	
	/**
	 * Get the New Expiration Month for the Vehicle.
	 * 
	 * @return int
	 */
	public int getNewRegExpMo()
	{
		return ciNewRegExpMo;
	}

	/**
	 * Get the New Expiration Year for the Vehicle.
	 * 
	 * @return int
	 */
	public int getNewRegExpYr()
	{
		return ciNewRegExpYr;
	}
	
	/**
	 * get the Plate Age.
	 * 
	 * @return int
	 */
	public int getPltAge()
	{
		return ciPltAge;
	}

	/**
	 * Get the Plate Birth Date.
	 * 
	 * @return int
	 */
	public int getPltBirthDate()
	{
		return ciPltBirthDate;
	}
	
	/**
	 * Get the number of times the Sticker was printed.
	 * 
	 * @return int
	 */
	public int getPrntQty()
	{
		return ciPrntQty;
	}
	
	/**
	 * Get the Registration Class Code.
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}
	
	/**
	 * Get the Current Expiration Month for the Registration.
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Get the Current Expiration Year for the Registration.
	 * 
	 * @return int
	 */
	public int getRegExpYr()
	{
		return ciRegExpYr;
	}
	
	/**
	 * Get the Registration Plate Code.
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Get the Registration Plate Number.
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	
	/**
	 * Get the Resident's Comptroller County Name.
	 * 
	 * @return String
	 */
	public String getResComptCntyName()
	{
		return csResComptCntyName;
	}

	/**
	 * Get the Resident's Comptroller County Number.
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}
	
	/**
	 * Get the Sticker Item Code.
	 * 
	 * @return String
	 */
	public String getStkrItmCd()
	{
		return csStkrItmCd;
	}
	
	/**
	 * Get the Vehicle Gross Weight.
	 * 
	 * @return int
	 */
	public int getVehGrossWt()
	{
		return ciVehGrossWt;
	}

	/**
	 * Get the Must Replace Plate Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isMustReplPltIndi()
	{
		return cbMustReplPltIndi;
	}

	/**
	 * Set the Insurance Verification Code.
	 * 
	 * @param asInsVerfdCd
	 */
	public void setInsVerfdCd(String asInsVerfdCd)
	{
		csInsVerfdCd = asInsVerfdCd;
	}

	/**
	 * Insurance Verification String is a decorator for Insurance 
	 * Verification Code, so the setter does not change any attributes.
	 * The setter is only used for the generation of the WSDL. 
	 * 
	 * @param asInsVerfdStr
	 */
	public void setInsVerfdStr(String asInsVerfdStr)
	{
		// null setter
	}
	
	/**
	 * Set the Inventory Item Number issued.
	 * 
	 * @param asInvItmNo
	 */
	public void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}
	
	/**
	 * Set the Plate Must be Replaced Indicator.
	 * 
	 * @param abMustReplPltIndi
	 */
	public void setMustReplPltIndi(boolean abMustReplPltIndi)
	{
		cbMustReplPltIndi = abMustReplPltIndi;
	}
	
	/**
	 * Set the New Expiration Month for the Registration.
	 * 
	 * @param aiNewRegExpMo
	 */
	public void setNewRegExpMo(int aiNewRegExpMo)
	{
		ciNewRegExpMo = aiNewRegExpMo;
	}

	/**
	 * Set the New Expiration Year for the Registration.
	 * 
	 * @param aiNewRegExpYr
	 */
	public void setNewRegExpYr(int aiNewRegExpYr)
	{
		ciNewRegExpYr = aiNewRegExpYr;
	}

	/**
	 * Set the Plate Age.
	 * 
	 * @param aiPltAge
	 */
	public void setPltAge(int aiPltAge)
	{
		ciPltAge = aiPltAge;
	}

	/**
	 * Set the Plate Birth Date.
	 * 
	 * @param aiPltBirthDate
	 */
	public void setPltBirthDate(int aiPltBirthDate)
	{
		ciPltBirthDate = aiPltBirthDate;
	}
	
	/**
	 * Set the Print Quantity.
	 * 
	 * @param aiPrntQty
	 */
	public void setPrntQty(int aiPrntQty)
	{
		ciPrntQty = aiPrntQty;
	}
	
	/**
	 * Set the Registration Class Code.
	 * 
	 * @param aiRegClassCd
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	
	/**
	 * Set the Expiration Month for the Registration.
	 * 
	 * @param aiRegExpMo
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Set the Expiration Year for the Registration.
	 * 
	 * @param aiRegExpYr
	 */
	public void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}
	
	/**
	 * Set the Registration Plate Code.
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set the Registration Plate Number.
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set the Resident's Comptroller County Name.
	 * 
	 * @param asResComptCntyName
	 */
	public void setResComptCntyName(String asResComptCntyName)
	{
		csResComptCntyName = asResComptCntyName;
	}

	/**
	 * Set the Resident's Comptroller County Number.
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Set the Sticker Item Code.
	 * 
	 * @param asStkrItmCd
	 */
	public void setStkrItmCd(String asStkrItmCd)
	{
		csStkrItmCd = asStkrItmCd;
	}
	
	/**
	 * Set the Vehicle Gross Weight.
	 * 
	 * @param aiVehGrossWt
	 */
	public void setVehGrossWt(int aiVehGrossWt)
	{
		ciVehGrossWt = aiVehGrossWt;
	}
}
