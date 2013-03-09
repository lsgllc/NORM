package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * PermitTransactionData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/30/2010	add VehMkDesc, BulkPrmtVendorId,
 * 							 VoidedTransIndi, get/set methods 
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/07/2010	add VehMkDesc, VehRegPltNo assignment when 
 * 							 creating PermitData for Void
 * 							modify getPermitData()
 * 							defect 10491 Ver 6.5.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * PermitTransactionData 
 *
 * @version	6.5.0			07/07/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		05/24/2010 14:22:17 
 */
public class PermitTransactionData implements Serializable, Displayable
{
	private Dollar caPrmtPdAmt;
	private int ciCacheTransAMDate;
	private int ciCacheTransTime;
	private int ciCustSeqNo;
	private int ciEffDate;
	private int ciEffTime;
	private int ciExpDate;
	private int ciExpTime;
	private int ciMfDwnCd;
	private int ciOfcIssuanceCd;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;
	private int ciVehModlYr;
	private int ciVoidedTransIndi;
	private String csAcctItmCd;
	private String csBulkPrmtVendorId;
	private String csCustBsnName;
	private String csCustCity;
	private String csCustCntry;
	private String csCustEMail;
	private String csCustFstName;
	private String csCustLstName;
	private String csCustMIName;
	private String csCustPhone;
	private String csCustSt1;
	private String csCustSt2;
	private String csCustState;
	private String csCustZpCd;
	private String csCustZpCdP4;
	private String csItmCd;
	private String csOneTripPrmtDestPnt;
	private String csOneTripPrmtOrigPnt;
	private String csOneTripPrmtPnt1;
	private String csOneTripPrmtPnt2;
	private String csOneTripPrmtPnt3;
	private String csPrmtIssuanceId;
	private String csPrmtNo;
	private String csTransCd;
	private String csTransId;
	private String csTransEmpId;
	private String csVehBdyType;
	private String csVehMk;
	private String csVehMkDesc;
	private String csVehRegCntry;
	private String csVehRegPltNo;
	private String csVehRegState;
	private String csVin;

	static final long serialVersionUID = 7283817471888179730L;

	/**
	 * PermitTransactionData.java Constructor
	 * 
	 */
	public PermitTransactionData()
	{
		super();
	}

	/**
	 * PermitTransactionData.java Constructor
	 * 
	 */
	public PermitTransactionData(PermitData aaPrmtData)
	{
		// CUSTOMER 
		CustomerData laCustData = aaPrmtData.getCustomerData();

		// CUSTOMER NAME 
		CustomerNameData laCustNameData = laCustData.getCustNameData();
		csCustFstName = laCustNameData.getCustFstName();
		csCustMIName = laCustNameData.getCustMIName();
		csCustLstName = laCustNameData.getCustLstName();
		csCustBsnName = laCustNameData.getCustBsnName();

		// CUSTOMER ADDRESS 
		AddressData laCustAddrData = laCustData.getAddressData();
		csCustSt1 = laCustAddrData.getSt1();
		csCustSt2 = laCustAddrData.getSt2();
		csCustCity = laCustAddrData.getCity();
		csCustState = laCustAddrData.getState();
		csCustCntry = laCustAddrData.getCntry();
		csCustZpCd = laCustAddrData.getZpcd();
		csCustZpCdP4 = laCustAddrData.getZpcdp4();

		csCustPhone = laCustData.getPhoneNo();
		csCustEMail = laCustData.getEMail();

		csPrmtNo = aaPrmtData.getPrmtNo();
		csItmCd = aaPrmtData.getItmCd();
		csAcctItmCd = aaPrmtData.getAcctItmCd();
		caPrmtPdAmt = aaPrmtData.getPrmtPdAmt();
		ciEffDate = aaPrmtData.getEffDt();
		ciEffTime = aaPrmtData.getEffTime();
		ciExpDate = aaPrmtData.getExpDt();
		ciExpTime = aaPrmtData.getExpTime();
		csVehBdyType = aaPrmtData.getVehBdyType();
		csVehMk = aaPrmtData.getVehMk();
		ciVehModlYr = aaPrmtData.getVehModlYr();
		csVin = aaPrmtData.getVin();
		csVehRegState = aaPrmtData.getVehRegState();
		csVehRegCntry = aaPrmtData.getVehRegCntry();
		csVehRegPltNo = aaPrmtData.getVehRegPltNo();
		OneTripData laOTData = aaPrmtData.getOneTripData();
		csOneTripPrmtOrigPnt = laOTData.getOrigtnPnt();
		csOneTripPrmtPnt1 = laOTData.getIntrmdtePnt1();
		csOneTripPrmtPnt2 = laOTData.getIntrmdtePnt2();
		csOneTripPrmtPnt3 = laOTData.getIntrmdtePnt3();
		csOneTripPrmtDestPnt = laOTData.getDestPnt();
		csPrmtIssuanceId = aaPrmtData.getPrmtIssuanceId();
		ciMfDwnCd = aaPrmtData.getMfDwnCd();
		
		csBulkPrmtVendorId = aaPrmtData.getBulkPrmtVendorId();
		csVehMkDesc = aaPrmtData.getVehMkDesc();
	}

	/**
	 * Gets value of csAcctItmCd
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Method used to return field attributes. 
	 * 
	 * @return  Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHashMap = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}

			catch (IllegalAccessException aeIllAccEx)
			{
				continue;
			}
		}
		return lhmHashMap;
	}

	/**
	 * Gets value of ciCacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Gets value of ciCacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Gets value of csCustBsnName
	 * 
	 * @return csCustBsnName
	 */
	public String getCustBsnName()
	{
		return csCustBsnName;
	}

	/**
	 * Gets value of csCustCity
	 * 
	 * @return String
	 */
	public String getCustCity()
	{
		return csCustCity;
	}

	/**
	 * Gets value of csCustCntry
	 * 
	 * @return String
	 */
	public String getCustCntry()
	{
		return csCustCntry;
	}

	/**
	 * Gets value of csCustEMail
	 * 
	 * @return String
	 */
	public String getCustEMail()
	{
		return csCustEMail;
	}

	/**
	 * Gets value of csCustFstName
	 * 
	 * @return String
	 */
	public String getCustFstName()
	{
		return csCustFstName;
	}

	/**
	 * Gets value of csCustLstName
	 * 
	 * @return String
	 */
	public String getCustLstName()
	{
		return csCustLstName;
	}

	/**
	 * Gets value of csCustMIName
	 * 
	 * @return String
	 */
	public String getCustMIName()
	{
		return csCustMIName;
	}

	/**
	 * Gets value of csCustPhone
	 * 
	 * @return String
	 */
	public String getCustPhone()
	{
		return csCustPhone;
	}

	/**
	 * Returns value of ciCustSeqNo
	 * 
	 * @return  int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Gets value of csCustSt1
	 * 
	 * @return String
	 */
	public String getCustSt1()
	{
		return csCustSt1;
	}

	/**
	 * Gets value of csCustSt2
	 * 
	 * @return String
	 */
	public String getCustSt2()
	{
		return csCustSt2;
	}

	/**
	 * Gets value of csCustState
	 * 
	 * @return String
	 */
	public String getCustState()
	{
		return csCustState;
	}

	/**
	 * Gets value of csCustZpCd
	 * 
	 * @return String
	 */
	public String getCustZpcd()
	{
		return csCustZpCd;
	}

	/**
	 * Gets value of csCustZpCdP4
	 * 
	 * @return String
	 */
	public String getCustZpcdP4()
	{
		return csCustZpCdP4;
	}

	/**
	 * Gets value of ciEffDate
	 * 
	 * @return 
	 */
	public int getEffDate()
	{
		return ciEffDate;
	}

	/**
	 * Gets value of ciEffTime
	 * 
	 * @return 
	 */
	public int getEffTime()
	{
		return ciEffTime;
	}

	/**
	 * Gets value of ciExpDate
	 * 
	 * @return 
	 */
	public int getExpDate()
	{
		return ciExpDate;
	}

	/**
	 * Gets value of ciExpTime
	 * 
	 * @return 
	 */
	public int getExpTime()
	{
		return ciExpTime;
	}

	/**
	 * Gets value of csItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}

	/**
	 * Gets value of ciMfDwnCd
	 * 
	 * @return 
	 */
	public int getMfDwnCd()
	{
		return ciMfDwnCd;
	}
	/**
	 * Gets value of 
	 * 
	 * @return ciOfcIssuanceCd
	 */
	public int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}

	/**
	 * Returns value of ciOfcIssuanceNo
	 * 
	 * @return  int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Gets value of csOneTripPrmtDestPnt
	 * 
	 * @return String
	 */
	public String getOneTripPrmtDestPnt()
	{
		return csOneTripPrmtDestPnt;
	}

	/**
	 * Gets value of csOneTripPrmtOrigPnt
	 * 
	 * @return String
	 */
	public String getOneTripPrmtOrigPnt()
	{
		return csOneTripPrmtOrigPnt;
	}

	/**
	 * Gets value of csOneTripPrmtPnt1
	 * 
	 * @return String
	 */
	public String getOneTripPrmtPnt1()
	{
		return csOneTripPrmtPnt1;
	}

	/**
	 * Gets value of csOneTripPrmtPnt2
	 * 
	 * @return String
	 */
	public String getOneTripPrmtPnt2()
	{
		return csOneTripPrmtPnt2;
	}

	/**
	 * Gets value of csOneTripPrmtPnt3
	 * 
	 * @return String
	 */
	public String getOneTripPrmtPnt3()
	{
		return csOneTripPrmtPnt3;
	}

	/**
	 * Create Permit Data Object for Void
	 * 
	 * @return PermitData 
	 */
	public PermitData getPermitData()
	{
		PermitData laPrmtData = new PermitData();

		CustomerData laCustData = new CustomerData();
		CustomerNameData laCustNameData = new CustomerNameData();
		AddressData laAddrData = new AddressData();

		// CUSTOMER NAME
		laCustNameData.setCustFstName(csCustFstName);
		laCustNameData.setCustMIName(csCustMIName);
		laCustNameData.setCustLstName(csCustLstName);
		laCustNameData.setCustBsnName(csCustBsnName);

		laCustData.setCustNameData(laCustNameData);

		// CUSTOMER ADDRESS
		laAddrData.setSt1(csCustSt1);
		laAddrData.setSt2(csCustSt2);
		laAddrData.setCity(csCustCity);
		laAddrData.setState(csCustState);
		laAddrData.setCntry(csCustCntry);
		laAddrData.setZpcd(csCustZpCd);
		laAddrData.setZpcdp4(csCustZpCdP4);

		laCustData.setAddressData(laAddrData);

		laCustData.setPhoneNo(csCustPhone);
		laCustData.setEMail(csCustEMail);

		// ONE TRIP DATA 
		OneTripData laOTData = new OneTripData();
		laOTData.setOrigtnPnt(csOneTripPrmtOrigPnt);
		laOTData.setIntrmdtePnt1(csOneTripPrmtPnt1);
		laOTData.setIntrmdtePnt2(csOneTripPrmtPnt2);
		laOTData.setIntrmdtePnt3(csOneTripPrmtPnt3);
		laOTData.setDestPnt(csOneTripPrmtDestPnt);

		laPrmtData.setCustomerData(laCustData);
		laPrmtData.setOneTripData(laOTData);
		laPrmtData.setPrmtIssuanceId(csPrmtIssuanceId);

		laPrmtData.setPrmtNo(csPrmtNo);
		laPrmtData.setItmCd(csItmCd);
		laPrmtData.setAcctItmCd(csAcctItmCd);
		laPrmtData.setPrmtPdAmt(caPrmtPdAmt);

		laPrmtData.setEffDt(ciEffDate);
		laPrmtData.setEffTime(ciEffTime);
		laPrmtData.setExpDt(ciExpDate);
		laPrmtData.setExpTime(ciExpTime);
		laPrmtData.setVehBdyType(csVehBdyType);
		laPrmtData.setVehMk(csVehMk);
		laPrmtData.setVehMkDesc(csVehMkDesc); 
		laPrmtData.setVehRegPltNo(csVehRegPltNo);
		laPrmtData.setVehModlYr(ciVehModlYr);
		laPrmtData.setVin(csVin);
		laPrmtData.setVehRegState(csVehRegState);
		laPrmtData.setVehRegCntry(csVehRegCntry);
		return laPrmtData;
	}

	/**
	 * Gets value of csPrmtIssuanceId
	 * 
	 * @return String
	 */
	public String getPrmtIssuanceId()
	{
		return csPrmtIssuanceId;
	}

	/**
	 * Gets value of csPrmtNo
	 * 
	 * @return String
	 */
	public String getPrmtNo()
	{
		return csPrmtNo;
	}

	/**
	 * Gets value of caPrmtPdAmt
	 * 
	 * @return 
	 */
	public Dollar getPrmtPdAmt()
	{
		return caPrmtPdAmt;
	}

	/**
	 * Returns value of ciSubstaId
	 * 
	 * @return  int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns value of ciTransAMDate
	 * 
	 * @return  int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Gets value of 
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Gets value of 
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	* Returns the value of TransId
	* 
	* @return String 
	*/
	public String getTransId()
	{
		if (csTransId == null || csTransId.trim().length() == 0)
		{
			csTransId =
				UtilityMethods.getTransId(
					ciOfcIssuanceNo,
					ciTransWsId,
					ciTransAMDate,
					ciTransTime);
		}
		return csTransId;
	}

	/**
	 * Returns value of ciTransTime
	 * 
	 * @return  int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns value of ciTransWsId
	 * 
	 * @return  int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Gets value of csVehBdyType
	 * 
	 * @return String
	 */
	public String getVehBdyType()
	{
		return csVehBdyType;
	}

	/**
	 * Gets value of csVehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Gets value of ciVehModlYr
	 * 
	 * @return 
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Gets value of csVehRegCntry
	 * 
	 * @return String
	 */
	public String getVehRegCntry()
	{
		return csVehRegCntry;
	}

	/**
	 * Gets value of csVehRegPltNo
	 * 
	 * @return String
	 */
	public String getVehRegPltNo()
	{
		return csVehRegPltNo;
	}

	/**
	 * Gets value of csVehRegState
	 * 
	 * @return String
	 */
	public String getVehRegState()
	{
		return csVehRegState;
	}

	/**
	 * Gets value of csVin
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}

	/**
	 * 
	 * Return boolean to denote if MfDwnCd == 1
	 * 
	 */
	public boolean isMFDown()
	{
		return ciMfDwnCd == 1;
	}

	/**
	 * Sets value of csAcctItmCd
	 * 
	 * @param asAcctItmCd
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * Sets value of ciCacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Sets value of ciCacheTransTime
	 * 
	 * @param aiCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * Sets value of csCustBsnName
	 * 
	 * @param asCustBsnName
	 */
	public void setCustBsnName(String asCustBsnName)
	{
		csCustBsnName = asCustBsnName;
	}

	/**
	 * Sets value of csCustCity
	 * 
	 * @param asCustCity
	 */
	public void setCustCity(String asCustCity)
	{
		csCustCity = asCustCity;
	}

	/**
	 * Sets value of csCustCntry
	 * 
	 * @param asCustCntry
	 */
	public void setCustCntry(String asCustCntry)
	{
		csCustCntry = asCustCntry;
	}

	/**
	 * Sets value of csCustEMail
	 * 
	 * @param asCustEMail
	 */
	public void setCustEMail(String asCustEMail)
	{
		csCustEMail = asCustEMail;
	}

	/**
	 * Sets value of csCustFstName
	 * 
	 * @param asCustFstName
	 */
	public void setCustFstName(String asCustFstName)
	{
		csCustFstName = asCustFstName;
	}

	/**
	 * Sets value of csCustLstName
	 * 
	 * @param asCustLstName
	 */
	public void setCustLstName(String asCustLstName)
	{
		csCustLstName = asCustLstName;
	}

	/**
	 * Sets value of csCustMIName
	 * 
	 * @param asCustMIName
	 */
	public void setCustMIName(String asCustMIName)
	{
		csCustMIName = asCustMIName;
	}

	/**
	 * Sets value of csCustPhone
	 * 
	 * @param asCustPhone
	 */
	public void setCustPhone(String asCustPhone)
	{
		csCustPhone = asCustPhone;
	}

	/**
	 * Sets value of ciCustSeqNo
	 * 
	 * @param aiCustSeqNo
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Sets value of csCustSt1
	 * 
	 * @param asCustSt1
	 */
	public void setCustSt1(String asCustSt1)
	{
		csCustSt1 = asCustSt1;
	}

	/**
	 * Sets value of csCustSt2
	 * 
	 * @param asCustSt2
	 */
	public void setCustSt2(String asCustSt2)
	{
		csCustSt2 = asCustSt2;
	}

	/**
	 * Sets value of csCustState
	 * 
	 * @param asCustState
	 */
	public void setCustState(String asCustState)
	{
		csCustState = asCustState;
	}

	/**
	 * Sets value of csCustZpCd
	 * 
	 * @param asCustZpCd
	 */
	public void setCustZpCd(String asCustZpCd)
	{
		csCustZpCd = asCustZpCd;
	}

	/**
	 * Sets value of csCustZpCdP4
	 * 
	 * @param asCustZpCdP4
	 */
	public void setCustZpCdP4(String asCustZpCdP4)
	{
		csCustZpCdP4 = asCustZpCdP4;
	}

	/**
	 * Sets value of ciEffDate
	 * 
	 * @param aiEffDate
	 */
	public void setEffDate(int aiEffDate)
	{
		ciEffDate = aiEffDate;
	}

	/**
	 * Sets value of ciEffTime
	 * 
	 * @param aiEffTime
	 */
	public void setEffTime(int aiEffTime)
	{
		ciEffTime = aiEffTime;
	}

	/**
	 * Sets value of ciExpDate
	 * 
	 * @param aiExpDate
	 */
	public void setExpDate(int aiExpDate)
	{
		ciExpDate = aiExpDate;
	}

	/**
	 * Sets value of ciExpTime
	 * 
	 * @param aiExpTime
	 */
	public void setExpTime(int aiExpTime)
	{
		ciExpTime = aiExpTime;
	}

	/**
	 * Sets value of csItmCd
	 * 
	 * @param asItmCd
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}

	/**
	 * Sets value of ciMfDwnCd
	 * 
	 * @param aiMfDwnCd
	 */
	public void setMfDwnCd(int aiMfDwnCd)
	{
		ciMfDwnCd = aiMfDwnCd;
	}

	/**
	 * Sets value of ciOfcIssuanceCd
	 * 
	 * @param aiOfcIssuanceCd
	 */
	public void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}

	/**
	 * Sets value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Sets value of csOneTripPrmtDestPnt
	 * 
	 * @param asOneTripPrmtDestPnt
	 */
	public void setOneTripPrmtDestPnt(String asOneTripPrmtDestPnt)
	{
		csOneTripPrmtDestPnt = asOneTripPrmtDestPnt;
	}

	/**
	 * Sets value of csOneTripPrmtOrigPnt
	 * 
	 * @param asOneTripPrmtOrigPnt
	 */
	public void setOneTripPrmtOrigPnt(String asOneTripPrmtOrigPnt)
	{
		csOneTripPrmtOrigPnt = asOneTripPrmtOrigPnt;
	}

	/**
	 * Sets value of csOneTripPrmtPnt1
	 * 
	 * @param asOneTripPrmtPnt1
	 */
	public void setOneTripPrmtPnt1(String asOneTripPrmtPnt1)
	{
		csOneTripPrmtPnt1 = asOneTripPrmtPnt1;
	}

	/**
	 * Sets value of csOneTripPrmtPnt2
	 * 
	 * @param asOneTripPrmtPnt2
	 */
	public void setOneTripPrmtPnt2(String asOneTripPrmtPnt2)
	{
		csOneTripPrmtPnt2 = asOneTripPrmtPnt2;
	}

	/**
	 * Sets value of csOneTripPrmtPnt3
	 * 
	 * @param asOneTripPrmtPnt3ng
	 */
	public void setOneTripPrmtPnt3(String asOneTripPrmtPnt3ng)
	{
		csOneTripPrmtPnt3 = asOneTripPrmtPnt3ng;
	}

	/**
	 * Sets value of csPrmtIssuanceId
	 * 
	 * @param asPrmtIssuanceId
	 */
	public void setPrmtIssuanceId(String asPrmtIssuanceId)
	{
		csPrmtIssuanceId = asPrmtIssuanceId;
	}

	/**
	 * Sets value of csPrmtNo
	 * 
	 * @param asPrmtNo
	 */
	public void setPrmtNo(String asPrmtNo)
	{
		csPrmtNo = asPrmtNo;
	}

	/**
	 * Sets value of caPrmtPdAmt
	 * 
	 * @param aaPrmtPdAmt
	 */
	public void setPrmtPdAmt(Dollar aaPrmtPdAmt)
	{
		caPrmtPdAmt = aaPrmtPdAmt;
	}

	/**
	 * Sets value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Sets value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Sets value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Sets value of csTransEmpId
	 * 
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Sets the value of TransId
	 * 
	 * @param asTransid  
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Sets value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Sets value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Sets value of csVehBdyType
	 * 
	 * @param asVehBdyType
	 */
	public void setVehBdyType(String asVehBdyType)
	{
		csVehBdyType = asVehBdyType;
	}

	/**
	 * Sets value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Sets value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Sets value of csVehRegCntry
	 * 
	 * @param asVehRegCntry
	 */
	public void setVehRegCntry(String asVehRegCntry)
	{
		csVehRegCntry = asVehRegCntry;
	}

	/**
	 * Sets value of csVehRegPltNo
	 * 
	 * @param asVehRegPltNo
	 */
	public void setVehRegPltNo(String asVehRegPltNo)
	{
		csVehRegPltNo = asVehRegPltNo;
	}

	/**
	 * Sets value of csVehRegState
	 * 
	 * @param asVehRegState
	 */
	public void setVehRegState(String asVehRegState)
	{
		csVehRegState = asVehRegState;
	}

	/**
	 * Sets value of csVin
	 * 
	 * @param asVin
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
	/**
	 * Return value of csBulkPrmtVendorId
	 * 
	 * @return String
	 */
	public String getBulkPrmtVendorId()
	{
		return csBulkPrmtVendorId;
	}

	/**
	 * Return value of csVehMkDesc
	 * 
	 * @return String 
	 */
	public String getVehMkDesc()
	{
		return csVehMkDesc;
	}

	/**
	 * Set value of csBulkPrmtVendorId
	 * 
	 * @param asBulkPrmtVendorId
	 */
	public void setBulkPrmtVendorId(String asBulkPrmtVendorId)
	{
		csBulkPrmtVendorId = asBulkPrmtVendorId;
	}

	/**
	 * Set value of csVehMkDesc
	 * 
	 * @param asVehMkDesc
	 */
	public void setVehMkDesc(String asVehMkDesc)
	{
		csVehMkDesc = asVehMkDesc;
	}
	/**
	 * Return value of ciVoidedTransIndi
	 * 
	 * @return int 
	 */
	public int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * Set value of ciVoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi
	 */
	public void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
}
