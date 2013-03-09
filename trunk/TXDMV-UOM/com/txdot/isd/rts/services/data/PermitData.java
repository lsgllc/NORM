package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.BusinessPartnerCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 * PermitData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/09/2010	Add caVIAllocData, csTransEmpId, 
 * 							 csVehRegPltNo, ciDelIndi, get/set methods 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	06/21/2010	add initPartialsForOne()
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	06/30/2010	add VehMkDesc, BulkPrmtVendorId, 
 * 							 get/set methods 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/07/2010	add ciIssuedMFDwnCd, get/set methods
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/12/2010	Only default to TX if Record Found 
 * 							modify PermitData(VehicleInquiryData) 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/14/2010	modify initPartialsForOne()
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	08/11/2010	Do not copy "NOPLATE" to PermitData
 * 							modify PermitData(VehicleInquiryData) 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	01/07/2011	add isIssuedByBulkPrmtVendor(), 
 * 							 getIssuingOfcName(), getIssueDate() 
 * 							defect 10726 Ver 6.7.0
 * K Harrell	06/18/2011	add cbModPtTrans, csVehTypeCd,
 * 							 csAuditTrailTransId, cvPriorModTransList, 
 * 							 get/set methods  
 * 							add isCurrent(), isFuture(), is30DayPT(),
 * 							 isOTPTor30PT(), hasPriorModTrans(),
 * 							 isMotorcycle(), isRegular(), 
 * 							 getLastTransId(), hasIssuedMotorcyclePermit()
 * 							add cvPriorModTransList, get/set methods
 * 							delete getIssueDate() 
 * 							modify getIssuingOfcName() 
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * PermitData 
 *
 * @version	6.8.0 			06/18/2011
 * @author	Kathy Harrell 
 * <br>Creation Date:		05/24/2010 12:59:17
 */
public class PermitData
	extends TimedPermitData
	implements Serializable, Displayable
{

	// Object 
	private CustomerData caCustomerData = null;
	private Dollar caPrmtPdAmt;
	private InventoryAllocationData caVIAllocData;

	// boolean
	private boolean cbByPrmtId;

	//	defect 10844 
	private boolean cbPrmDupTrans;

	// int
	private int ciDelIndi;
	private int ciIssuedMFDwnCd;
	private int ciMFDwnCd;
	private int ciNo30DayPrmts;
	private int ciNoMFRecs;
	private int ciVehModlYr;

	// String 
	private String csAcctItmCd;
	private String csBulkPrmtVendorId;
	private String csPrmtIssuanceId;
	private String csPrmtNo;
	private String csTransCd;
	private String csTransEmpId;
	private String csVehBdyType;
	private String csVehMk;
	private String csVehMkDesc;
	private String csVehRegCntry;
	private String csVehRegPltNo;
	private String csVin;

	// Vector 
	private Vector cvPartialPrmtDataList = new Vector();

	// defect 10844
	private boolean cbModPtTrans;
	private String csAuditTrailTransId;
	private String csVehTypeCd;
	private Vector cvPriorModTransList = new Vector();

	static final long serialVersionUID = 905671892255957164L;

	/**
	 * PermitData.java Constructor
	 * 
	 */
	public PermitData()
	{
		super();
	}

	/**
	 * PermitData.java Constructor
	 * 
	 */
	public PermitData(VehicleInquiryData aaVehInqData)
	{
		super();

		if (aaVehInqData != null)
		{
			if (aaVehInqData.getMfVehicleData() != null)
			{
				MFVehicleData laMFVehData =
					aaVehInqData.getMfVehicleData();

				if (laMFVehData.getVehicleData() != null)
				{
					VehicleData laVehData =
						laMFVehData.getVehicleData();
					ciVehModlYr = laVehData.getVehModlYr();
					csVehBdyType = laVehData.getVehBdyType();
					csVehMk = laVehData.getVehMk();
					ciNoMFRecs = aaVehInqData.getNoMFRecs();
					csVin = laVehData.getVin();
					ciMFDwnCd = aaVehInqData.isMFDown() ? 1 : 0;

					// defect 10844 
					csVehTypeCd = aaVehInqData.getVehTypeCdforPermit();
					// end defect 10844  
				}

				if (aaVehInqData.isRecordFound())
				{
					RegistrationData laRegData =
						laMFVehData.getRegData();

					if (laRegData != null)
					{
						String lsPltNo = laRegData.getRegPltNo();
						if (lsPltNo == null
							|| !lsPltNo.equals(
								RegistrationConstant.NOPLATE))
						{
							csVehRegPltNo = laRegData.getRegPltNo();
						}
						setVehRegState(CommonConstant.STR_TX);
					}
				}
			}
		}
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
	 * @return Map
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
	 * Get value of csAuditTrailTransId
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
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
	 * Gets value of caCustomerData
	 * 
	 * @return CustomerData
	 */
	public CustomerData getCustomerData()
	{
		if (caCustomerData == null)
		{
			caCustomerData = new CustomerData();
		}

		return caCustomerData;
	}

	/**
	 * Returns ciDelIndi
	 * 
	 * @return int
	 */
	public int getDelIndi()
	{
		return ciDelIndi;
	}

	/**
	 * Returns ciIssuedMFDwnCd   
	 * 
	 * @return int 
	 */
	public int getIssuedMfDwnCd()
	{
		return ciIssuedMFDwnCd;
	}

	/** 
	 * Return Issuing Office Name 
	 * 
	 * @return String 
	 */
	public String getIssuingOfcName()
	{
		// defect 10844
		// Implement AuditTrailTransId as data associated w/ last 
		//  transaction affecting Permit

		TransactionIdData laTransIdData =
			new TransactionIdData(getLastTransId());

		String lsIssuingOfcName = new String();

		if (isIssuedByBulkPrmtVendor())
		{
			BusinessPartnerData laBPData =
				BusinessPartnerCache.getTimedPermitVendor(
					getBulkPrmtVendorId(),
					laTransIdData.getTransactionDateYYYYMMDD());
			// end defect 10844

			if (laBPData != null && laBPData.getName1() != null)
			{
				lsIssuingOfcName = laBPData.getName1().trim();
			}
		}
		else
		{
			OfficeIdsData laOfcData =
				OfficeIdsCache.getOfcId(
					laTransIdData.getOfcIssuanceNo());

			if (laOfcData != null)
			{
				lsIssuingOfcName = laOfcData.getOfcName();

				if (laOfcData.isCounty())
				{
					lsIssuingOfcName =
						lsIssuingOfcName + CommonConstant.COUNTY_ABBR;
				}
				else if (laOfcData.isRegion())
				{
					int liPos =
						lsIssuingOfcName.indexOf(
							CommonConstant.REGIONAL_OFFICE);

					// defect 10844 
					if (liPos != -1)
					{
						lsIssuingOfcName =
							lsIssuingOfcName.substring(0, liPos).trim();
					}
					// end defect 10844 
					lsIssuingOfcName =
						lsIssuingOfcName + CommonConstant.REGION_ABBR;
				}
			}
		}

		return lsIssuingOfcName;
	}

	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public String getLastTransId()
	{
		return UtilityMethods.isEmpty(getAuditTrailTransId())
			? getPrmtIssuanceId()
			: getAuditTrailTransId();
	}
	/**
	 * Returns ciMFDwnCd   
	 * 
	 * @return int 
	 */
	public int getMfDwnCd()
	{
		return ciMFDwnCd;
	}

	/**
	 * Return Number of 30 Day Permits 
	 * 
	 * @return int
	 */
	public int getNo30DayPrmts()
	{
		return ciNo30DayPrmts;
	}

	/**
	 * Sets value of ciNoMFRecs 
	 * 
	 * @return int
	 */
	public int getNoMFRecs()
	{
		return ciNoMFRecs;
	}

	/**
	 * Gets value of cvPartialPrmtDataList
	 * 
	 * @return Vector 
	 */
	public Vector getPartialPrmtDataList()
	{
		return cvPartialPrmtDataList;
	}

	/**
	 * Get value of cvPriorModTransList
	 * 
	 * @return Vector
	 */
	public Vector getPriorModTransList()
	{
		return cvPriorModTransList;
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
	 * Gets value of csPrmtPdAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getPrmtPdAmt()
	{
		if (caPrmtPdAmt == null)
		{
			caPrmtPdAmt = new Dollar(0);
		}
		return caPrmtPdAmt;
	}

	/**
	 * Gets value of csTransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		if (UtilityMethods.isEmpty(csTransCd))
		{
			csTransCd = getTimedPrmtType();
		}
		return csTransCd;
	}

	/**
	 * Gets value of csTransEmpId
	 * 
	 * @return
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
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
	 * Return value of csVehMkDesc
	 * 
	 * @return String 
	 */
	public String getVehMkDesc()
	{
		return csVehMkDesc;
	}

	/**
	 * Gets value of ciVehModlYr
	 * 
	 * @return String
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
	 * Gets value of caVIAllocData
	 * 
	 * @return InventoryAllocationData
	 */
	public InventoryAllocationData getVIAllocData()
	{
		return caVIAllocData;
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
	 * Returns boolean to denote if Motorcycle
	 * 
	 * @return boolean 
	 */
	public boolean hasIssuedMotorCyclePermit()
	{
		return getItmCd() != null
			&& (getItmCd().equals(MiscellaneousRegConstant.ITMCD_30MCPT)
				|| getItmCd().equals(
					MiscellaneousRegConstant.ITMCD_OTMCPT));
	}
	/**
	 * Return boolean to denote if cvPriorModTransList is not null, empty
	 */
	public boolean hasPriorModTrans()
	{
		return (
			cvPriorModTransList != null
				&& cvPriorModTransList.size() > 0);
	}

	/** 
	 * Set vector of one Partial Record 
	 *
	 */
	public void initPartialsForOne()
	{
		Vector lvPartials = new Vector();
		MFPartialPermitData laPartialData = new MFPartialPermitData();
		String lsCustName =
			getCustomerData().getCustNameData().getCustName(true);
		laPartialData.setCustName(lsCustName);
		laPartialData.setExpDate(getExpDt());
		laPartialData.setVehMk(getVehMk());
		laPartialData.setVehModlYr(getVehModlYr());
		laPartialData.setVin(getVin());
		laPartialData.setPrmtNo(getPrmtNo());
		laPartialData.setItmCd(getItmCd());
		laPartialData.setPrmtIssuanceId(getPrmtIssuanceId());
		laPartialData.setExpTime(getExpTime());
		lvPartials.add(laPartialData);
		setPartialPrmtDataList(lvPartials);
	}
	/**
	 * Returns boolean to denote if 30PT/30MCPT  
	 * 
	 * @return
	 */
	public boolean is30DayPT()
	{
		return getItmCd() != null
			&& (getItmCd().equals(MiscellaneousRegConstant.ITMCD_30PT)
				|| getItmCd().equals(
					MiscellaneousRegConstant.ITMCD_30MCPT));
	}

	/**
	 * Return boolean to denote if search by PrmtId
	 * 
	 * @return boolean 
	 */
	public boolean isByPrmtId()
	{
		return cbByPrmtId;
	}

	/** 
	 * Return boolean to denote if Permit is "Current" 
	 * 
	 * @return boolean 
	 */
	public boolean isCurrent()
	{
		boolean lbCurrent = false;

		if (getExpDt() != 0)
		{
			if (getRTSDateExpDt() == null)
			{
				setRTSDateExpDt(
					new RTSDate(RTSDate.YYYYMMDD, getExpDt()));

				getRTSDateExpDt().setTime(getExpTime());
			}
			if (getRTSDateEffDt() == null)
			{
				setRTSDateEffDt(
					new RTSDate(RTSDate.YYYYMMDD, getEffDt()));

				getRTSDateEffDt().setTime(getEffTime());
			}
			RTSDate laToday = new RTSDate();

			if (getRTSDateEffDt().compareTo(laToday) <= 0
				&& getRTSDateExpDt().compareTo(laToday) >= 0)
			{
				lbCurrent = true;
			}
		}
		return lbCurrent;
	}

	/**
	 * Return boolean to denote if Permit is Expired 
	 * 
	 * @return boolean 
	 */
	public boolean isExpired()
	{
		boolean lbExpired = true;

		if (getExpDt() != 0)
		{
			if (getRTSDateExpDt() == null)
			{
				setRTSDateExpDt(
					new RTSDate(RTSDate.YYYYMMDD, getExpDt()));
				getRTSDateExpDt().setTime(getExpTime());
			}
			if (getRTSDateExpDt().compareTo(new RTSDate()) >= 0)
			{
				lbExpired = false;
			}
		}
		return lbExpired;
	}

	/** 
	 * Return boolean to denote if Permit is Future 
	 * 
	 * @return boolean 
	 */
	public boolean isFuture()
	{
		return !isCurrent() && !isExpired();
	}

	/**
	 * Returns boolean to denote if issued by Bulk Permit Vendor  
	 * 
	 * @return boolean 
	 */
	public boolean isIssuedByBulkPrmtVendor()
	{
		return !UtilityMethods.isEmpty(getBulkPrmtVendorId());
	}

	/** 
	 * Max Records Exceeded  
	 * 
	 * @return boolean 
	 */
	public boolean isMaxRecordsExceeded()
	{
		return cvPartialPrmtDataList != null
			&& !cvPartialPrmtDataList.isEmpty()
			&& ciNoMFRecs > cvPartialPrmtDataList.size();
	}

	/**
	 * Returns boolean to denote if record created in MFDown  
	 * 
	 * @return boolean 
	 */
	public boolean isMFDown()
	{
		return ciMFDwnCd == 1;
	}

	/**
	 * Return boolean to denote if Modify Permit Trans 
	 * 
	 * @return boolean 
	 */
	public boolean isModPtTrans()
	{
		return cbModPtTrans;
	}

	/**
	 * Return boolean to denote that associated Vehicle is Motorcycle
	 * 
	 * @return boolean 
	 */
	public boolean isMotorcycle()
	{
		return !UtilityMethods.isEmpty(csVehTypeCd)
			&& csVehTypeCd.equals(
				MiscellaneousRegConstant.PERMIT_MOTORCYCLE_VEHTYPECD);
	}

	/**
	 * Returns boolean to denote if OTPT/OTMCPT  
	 * 
	 * @return boolean 
	 */
	public boolean isOTPT()
	{
		return getItmCd() != null
			&& (getItmCd().equals(MiscellaneousRegConstant.ITMCD_OTPT)
				|| getItmCd().equals(
					MiscellaneousRegConstant.ITMCD_OTMCPT));
	}

	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public boolean isOTPTor30PT()
	{
		return !UtilityMethods.isEmpty(csAcctItmCd)
			&& (csAcctItmCd.equals("OTPT") || csAcctItmCd.equals("30PT"));
	}

	/**
	 * Return boolean to denote if Duplicate Permit Receipt 
	 * 
	 * @return boolean 
	 */
	public boolean isPrmDupTrans()
	{
		return cbPrmDupTrans;
	}

	/**
	 * For 30PT, OTPT, is this vehicle "Regular"
	 * 
	 * @return boolean 
	 */
	public boolean isRegular()
	{
		return !UtilityMethods.isEmpty(csVehTypeCd)
			&& csVehTypeCd.equals(
				MiscellaneousRegConstant.PERMIT_REGULAR_VEHTYPECD);
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
	 * Set value of csAuditTrailTransId
	 * 
	 * @param asAuditTrailTransId
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
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
	 * Set boolean to denote if search by PrmtId
	 * 
	 * @param abByPrmtId
	 */
	public void setByPrmtId(boolean abByPrmtId)
	{
		cbByPrmtId = abByPrmtId;
	}

	/**
	 * Sets value of caCustomerData
	 * 
	 * @param aaCustomerData
	 */
	public void setCustomerData(CustomerData aaCustomerData)
	{
		caCustomerData = aaCustomerData;
	}

	/**
	 * Sets value of ciDelIndi
	 * 
	 * @param aiDelIndi
	 */
	public void setDelIndi(int aiDelIndi)
	{
		ciDelIndi = aiDelIndi;
	}

	/**
	 * Sets value of ciIssuedMFDwnCd
	 * 
	 * @param aiIssuedMFDwnCd
	 */
	public void setIssuedMFDwnCd(int aiIssuedMFDwnCd)
	{
		ciIssuedMFDwnCd = aiIssuedMFDwnCd;
	}

	/**
	 * Sets value of ciMFDwnCd
	 * 
	 * @param aiMFDwnCd
	 */
	public void setMFDwnCd(int aiMFDwnCd)
	{
		ciMFDwnCd = aiMFDwnCd;
	}
	/**
	 * Sets value of cbPrmDupTrans
	 * 
	 * @param abPrmDupTrans
	 */
	public void setModPtTrans(boolean abModPtTrans)
	{
		cbModPtTrans = abModPtTrans;
	}

	/**
	 * Sets Number of 30 Day Permits 
	 * 
	 * @param aiNo30DayPrmts
	 */
	public void setNo30DayPrmts(int aiNo30DayPrmts)
	{
		ciNo30DayPrmts = aiNo30DayPrmts;
	}

	/**
	 * Sets value of ciNoMFRecs
	 * 
	 * @param aiNoMFRecs
	 */
	public void setNoMFRecs(int aiNoMFRecs)
	{
		ciNoMFRecs = aiNoMFRecs;
	}

	/**
	 * Sets value of cvPartialPrmtDataList
	 * 
	 * @param avPartialPrmtDataList
	 */
	public void setPartialPrmtDataList(Vector avPartialPrmtDataList)
	{
		cvPartialPrmtDataList = avPartialPrmtDataList;
	}

	/**
	 * Set value of cvPriorModTransList
	 * 
	 * @param avPriorModTransList
	 */
	public void setPriorModTransList(Vector avPriorModTransList)
	{
		cvPriorModTransList = avPriorModTransList;
	}

	/**
	 * Sets value of cbPrmDupTrans
	 * 
	 * @param abPrmDupTrans
	 */
	public void setPrmDupTrans(boolean abPrmDupTrans)
	{
		cbPrmDupTrans = abPrmDupTrans;
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
	 * Set value of csVehMkDesc
	 * 
	 * @param asVehMkDesc
	 */
	public void setVehMkDesc(String asVehMkDesc)
	{
		csVehMkDesc = asVehMkDesc;
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
	 * Sets value of csVehTypeCd
	 * 
	 * @param csVehTypeCd
	 */
	public void setVehTypeCd()
	{
		csVehTypeCd =
			hasIssuedMotorCyclePermit()
				? MiscellaneousRegConstant.PERMIT_MOTORCYCLE_VEHTYPECD
				: MiscellaneousRegConstant.PERMIT_REGULAR_VEHTYPECD;
	}

	/**
	 * Sets value of csVehTypeCd
	 * 
	 * @param csVehTypeCd
	 */
	public void setVehTypeCd(String asVehTypeCd)
	{
		csVehTypeCd = asVehTypeCd;
	}

	/**
	 * Sets value of caVIAllocData
	 * 
	 * @param aaVIAllocData
	 */
	public void setVIAllocData(InventoryAllocationData aaVIAllocData)
	{
		caVIAllocData = aaVIAllocData;
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

}
