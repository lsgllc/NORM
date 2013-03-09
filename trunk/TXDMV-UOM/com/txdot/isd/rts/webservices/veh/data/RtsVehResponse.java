package com.txdot.isd.rts.webservices.veh.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsVehResponse.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/19/2010	Created class.
 * 							defect 10402 Ver 6.4.0
 * B Hargrove	05/13/2010	Add Plate Validity Term.
 * 							defect 10402 Ver 6.4.0
* ---------------------------------------------------------------------
 */

/**
 * Response data from Vehicle Web service.
 *
 * @version	Ver 6.4.0			05/13/2010
 * @author	William Hargrove
 * <br>Creation Date:			03/08/2010 13:50:00
 */
public class RtsVehResponse  extends RtsAbstractResponse
{

	
	
	private int ciAddlSetIndi;
	private int ciIsaIndi;
	private int ciManufacturingDate;
	private int ciPlateBirthDate;
	private int ciPlateEffectiveDate;
	private int ciPlateExpirationMonth;
	private int ciPlateExpirationYear;
	private int ciPltValidityTerm;
	private int ciPltSetNo;
	private int ciResCompCntyNo;
	
	private long clSRId;

	private String csAuditTrailTrans;
	private String csDocNo;
	private String csManufacturingPltNo;
	private String csManufacturingStatusCd;
	private String csOrgNo;
	private String csPlateCode;
	private String csPlateNo;
	private String csPlateOwnerCity;
	private String csPlateOwnerCountry;
	private String csPlateOwnerDlrGdn;
	private String csPlateOwnerEMail;
	private String csPlateOwnerId;
	private String csPlateOwnerNameLine1;
	private String csPlateOwnerNameLine2;
	private String csPlateOwnerPhone;
	private String csPlateOwnerState;
	private String csPlateOwnerStreetLine1;
	private String csPlateOwnerStreetLine2;
	private String csPlateOwnerZipCode;
	private String csPlateOwnerZipCodeP4;
	private String csResult;
	private String csTransEmpId;

//TODO constructor move vehinfo to response (my extract)

	/**
	 * Return the value of AddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Return the value of IsaIndi
	 * 
	 * @return int
	 */
	public int getIsaIndi()
	{
		return ciIsaIndi;
	}
	
	/**
	 * Returns the value of ManufacturingDate
	 * 
	 * @return int
	 */
	public int getManufacturingDate()
	{
		return ciManufacturingDate;
	}
	
	/**
	 * Returns the value of PlateBirthDate
	 * 
	 * @return int
	 */
	public int getPlateBirthDate()
	{
		return ciPlateBirthDate;
	}
	
	/**
	 * Returns the value of PlateEffectiveDate
	 * 
	 * @return int
	 */
	public int getPlateEffectiveDate()
	{
		return ciPlateEffectiveDate;
	}

	/**
	 * Return the value of PlateExpirationMonth
	 * 
	 * @return int
	 */
	public int getPlateExpirationMonth()
	{
		return ciPlateExpirationMonth;
	}

	/**
	 * Return the value of PlateExpirationYear
	 * 
	 * @return int
	 */
	public int getPlateExpirationYear()
	{
		return ciPlateExpirationYear;
	}

	/**
	 * Return the value of PltSetNo
	 * 
	 * @return int
	 */
	public int getPltSetNo()
	{
		return ciPltSetNo;
	}

	/**
	 * Return the value of ResCompCntyNo
	 * 
	 * @return int
	 */
	public int getResCompCntyNo()
	{
		return ciResCompCntyNo;
	}

	/**
	 * Return the value of SRId
	 * 
	 * @return long
	 */
	public long getSRId()
	{
		return clSRId;
	}

	/**
	 * Return the value of AuditTrailTrans
	 * 
	 * @return String
	 */
	public String getAuditTrailTrans()
	{
		return csAuditTrailTrans;
	}

	/**
	 * Return the value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Return the value of ManufacturingPltNo
	 * 
	 * @return String
	 */
	public String getManufacturingPltNo()
	{
		return csManufacturingPltNo;
	}

	/**
	 * Return the value of ManufacturingStatusCd
	 * 
	 * @return String
	 */
	public String getManufacturingStatusCd()
	{
		return csManufacturingStatusCd;
	}

	/**
	 * Return the value of OrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Return the value of PlateCode
	 * 
	 * @return String
	 */
	public String getPlateCode()
	{
		return csPlateCode;
	}

	/**
	 * Return the value of PlateNo
	 * 
	 * @return String
	 */
	public String getPlateNo()
	{
		return csPlateNo;
	}

	/**
	 * Return the value of PlateOwnerCity
	 * 
	 * @return String
	 */
	public String getPlateOwnerCity()
	{
		return csPlateOwnerCity;
	}

	/**
	 * Return the value of PlateOwnerCountry
	 * 
	 * @return String
	 */
	public String getPlateOwnerCountry()
	{
		return csPlateOwnerCountry;
	}

	/**
	 * Return the value of PlateOwnerDlrGdn
	 * 
	 * @return String
	 */
	public String getPlateOwnerDlrGdn()
	{
		return csPlateOwnerDlrGdn;
	}

	/**
	 * Return the value of PlateOwnerEMail
	 * 
	 * @return String
	 */
	public String getPlateOwnerEMail()
	{
		return csPlateOwnerEMail;
	}

	/**
	 * Return the value of PlateOwnerId
	 * 
	 * @return String
	 */
	public String getPlateOwnerId()
	{
		return csPlateOwnerId;
	}

	/**
	 * Return the value of PlateOwnerNameLine1
	 * 
	 * @return String
	 */
	public String getPlateOwnerNameLine1()
	{
		return csPlateOwnerNameLine1;
	}

	/**
	 * Return the value of PlateOwnerNameLine2
	 * 
	 * @return String
	 */
	public String getPlateOwnerNameLine2()
	{
		return csPlateOwnerNameLine2;
	}

	/**
	 * Return the value of PlateOwnerPhone
	 * 
	 * @return String
	 */
	public String getPlateOwnerPhone()
	{
		return csPlateOwnerPhone;
	}

	/**
	 * Return the value of PlateOwnerState
	 * 
	 * @return String
	 */
	public String getPlateOwnerState()
	{
		return csPlateOwnerState;
	}

	/**
	 * Return the value of PlateOwnerStreetLine1
	 * 
	 * @return String
	 */
	public String getPlateOwnerStreetLine1()
	{
		return csPlateOwnerStreetLine1;
	}

	/**
	 * Return the value of PlateOwnerStreetLine2
	 * 
	 * @return String
	 */
	public String getPlateOwnerStreetLine2()
	{
		return csPlateOwnerStreetLine2;
	}

	/**
	 * Return the value of PlateOwnerZipCode
	 * 
	 * @return String
	 */
	public String getPlateOwnerZipCode()
	{
		return csPlateOwnerZipCode;
	}

	/**
	 * Return the value of PlateOwnerZipCodeP4
	 * 
	 * @return String
	 */
	public String getPlateOwnerZipCodeP4()
	{
		return csPlateOwnerZipCodeP4;
	}

	/**
	 * Return the value of PltValidityTerm
	 * 
	 * @return
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}
	
	/**
	 * Returns Result.
	 * 
	 * @return String
	 */
	public String getResult()
	{
		return csResult;
	}

	/**
	 * Set the value of AddlSetIndi
	 * 
	 * @param aiAddlSetIndi int
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Set the value of IsaIndi
	 * 
	 * @param aiIsaIndi int
	 */
	public void setIsaIndi(int aiIsaIndi)
	{
		ciIsaIndi = aiIsaIndi;
	}
	
	/**
	 * Sets the value of ManufacturingDate
	 * 
	 * @param aiManufacturingDate int
	 */
	public void setManufacturingDate(int aiManufacturingDate)
	{
		ciManufacturingDate = aiManufacturingDate;
	}
	
	/**
	 * Sets the value of PlateBirthDate
	 * 
	 * @param aaPlateBirthDate int
	 */
	public void setPlateBirthDate(int aiPlateBirthDate)
	{
		ciPlateBirthDate = aiPlateBirthDate;
	}
	
	/**
	 * Sets the value of PlateEffectiveDate
	 * 
	 * @param aaPlateEffectiveDate int
	 */
	public void setPlateEffectiveDate(int aiPlateEffectiveDate)
	{
		ciPlateEffectiveDate = aiPlateEffectiveDate;
	}

	/**
	 * Set the value of PlateExpirationMonth
	 * 
	 * @param aiPlateExpirationMonth int
	 */
	public void setPlateExpirationMonth(int aiPlateExpirationMonth)
	{
		ciPlateExpirationMonth = aiPlateExpirationMonth;
	}

	/**
	 * Set the value of PlateExpirationYear
	 * 
	 * @param aiPlateExpirationYear int
	 */
	public void setPlateExpirationYear(int aiPlateExpirationYear)
	{
		ciPlateExpirationYear = aiPlateExpirationYear;
	}

	/**
	 * Set the value of PltSetNo
	 * 
	 * @param aiPltSetNo int
	 */
	public void setPltSetNo(int aiPltSetNo)
	{
		ciPltSetNo = aiPltSetNo;
	}

	/**
	 * Set the value of ResCompCntyNo
	 * 
	 * @param aiResCompCntyNo int
	 */
	public void setResCompCntyNo(int aiResCompCntyNo)
	{
		ciResCompCntyNo = aiResCompCntyNo;
	}

	/**
	 * Set the value of SRId
	 * 
	 * @param alSRId long
	 */
	public void setSRId(long alSRId)
	{
		clSRId = alSRId;
	}

	/**
	 * Set the value of AuditTrailTrans
	 * 
	 * @param asAuditTrailTrans String
	 */
	public void setAuditTrailTrans(String asAuditTrailTrans)
	{
		csAuditTrailTrans = asAuditTrailTrans;
	}

	/**
	 * Set the value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set the value of ManufacturingPltNo
	 * 
	 * @param asManufacturingPltNo String
	 */
	public void setManufacturingPltNo(String asManufacturingPltNo)
	{
		csManufacturingPltNo = asManufacturingPltNo;
	}

	/**
	 * Set the value of ManufacturingStatusCd
	 * 
	 * @param asManufacturingStatusCd String
	 */
	public void setManufacturingStatusCd(String asManufacturingStatusCd)
	{
		csManufacturingStatusCd = asManufacturingStatusCd;
	}

	/**
	 * Set the value of OrgNo
	 * 
	 * @param aiOrgNo String
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Set the value of PlateCode
	 * 
	 * @param asPlateCode String
	 */
	public void setPlateCode(String asPlateCode)
	{
		csPlateCode = asPlateCode;
	}

	/**
	 * Set the value of PlateNo
	 * 
	 * @param asPlateNo String
	 */
	public void setPlateNo(String asPlateNo)
	{
		csPlateNo = asPlateNo;
	}

	/**
	 * Set the value of PlateOwnerCity
	 * 
	 * @param asPlateOwnerCity String
	 */
	public void setPlateOwnerCity(String asPlateOwnerCity)
	{
		csPlateOwnerCity = asPlateOwnerCity;
	}

	/**
	 * Set the value of PlateOwnerCountry
	 * 
	 * @param asPlateOwnerCountry String
	 */
	public void setPlateOwnerCountry(String asPlateOwnerCountry)
	{
		csPlateOwnerCountry = asPlateOwnerCountry;
	}

	/**
	 * Set the value of PlateOwnerDlrGdn
	 * 
	 * @param asPlateOwnerDlrGdn String
	 */
	public void setPlateOwnerDlrGdn(String asPlateOwnerDlrGdn)
	{
		csPlateOwnerDlrGdn = asPlateOwnerDlrGdn;
	}

	/**
	 * Set the value of PlateOwnerEMail
	 * 
	 * @param asPlateOwnerEMail String
	 */
	public void setPlateOwnerEMail(String asPlateOwnerEMail)
	{
		csPlateOwnerEMail = asPlateOwnerEMail;
	}

	/**
	 * Set the value of PlateOwnerId
	 * 
	 * @param asPlateOwnerId String
	 */
	public void setPlateOwnerId(String asPlateOwnerId)
	{
		csPlateOwnerId = asPlateOwnerId;
	}

	/**
	 * Set the value of PlateOwnerNameLine1
	 * 
	 * @param asPlateOwnerNameLine1 String
	 */
	public void setPlateOwnerNameLine1(String asPlateOwnerNameLine1)
	{
		csPlateOwnerNameLine1 = asPlateOwnerNameLine1;
	}

	/**
	 * Set the value of PlateOwnerNameLine2
	 * 
	 * @param asPlateOwnerNameLine2 String
	 */
	public void setPlateOwnerNameLine2(String asPlateOwnerNameLine2)
	{
		csPlateOwnerNameLine2 = asPlateOwnerNameLine2;
	}

	/**
	 * Set the value of PlateOwnerPhone
	 * 
	 * @param asPlateOwnerPhone String
	 */
	public void setPlateOwnerPhone(String asPlateOwnerPhone)
	{
		csPlateOwnerPhone = asPlateOwnerPhone;
	}

	/**
	 * Set the value of PlateOwnerState
	 * 
	 * @param asPlateOwnerState String
	 */
	public void setPlateOwnerState(String asPlateOwnerState)
	{
		csPlateOwnerState = asPlateOwnerState;
	}

	/**
	 * Set the value of PlateOwnerStreetLine1
	 * 
	 * @param asPlateOwnerStreetLine1 String
	 */
	public void setPlateOwnerStreetLine1(String asPlateOwnerStreetLine1)
	{
		csPlateOwnerStreetLine1 = asPlateOwnerStreetLine1;
	}

	/**
	 * Set the value of PlateOwnerStreetLine2
	 * 
	 * @param asPlateOwnerStreetLine2 String
	 */
	public void setPlateOwnerStreetLine2(String asPlateOwnerStreetLine2)
	{
		csPlateOwnerStreetLine2 = asPlateOwnerStreetLine2;
	}

	/**
	 * Set the value of PlateOwnerZipCode
	 * 
	 * @param asPlateOwnerZipCode String
	 */
	public void setPlateOwnerZipCode(String asPlateOwnerZipCode)
	{
		csPlateOwnerZipCode = asPlateOwnerZipCode;
	}

	/**
	 * Set the value of PlateOwnerZipCodeP4
	 * 
	 * @param asPlateOwnerZipCodeP4 String
	 */
	public void setPlateOwnerZipCodeP4(String asPlateOwnerZipCodeP4)
	{
		csPlateOwnerZipCodeP4 = asPlateOwnerZipCodeP4;
	}

	/**
	 * Set the value of PltValidityTerm
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}
	
	/**
	 * Sets Result.
	 * 
	 * @param String asResult
	 */
	public void setResult(String asResult)
	{
		csResult = asResult;
	}

	/**
	 * Set the value of TransEmpId
	 * 
	 * @param asTransEmpId String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}


}
