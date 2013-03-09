package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * TimedPermitData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 * 							add class variable ciIssueTwoPlacardsIndi
 * 							add getIssueTwoPlacardsIndi()
 * 							add setIssueTwoPlacardsIndi()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	10/27/2008	Add DisabledPlacardCustomerData object for
 * 							transition to new Disabled Placard events.
 * 							defect 9831 Ver Defect_POS_B	
 * K Harrell	05/25/2010	delete cbEnterSelected, 
 * 							 getsetEnterSelected(), 
 * 							 isEnterSelected()
 * 							modify getOneTripData()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	10/10/2011	delete ciIssueTwoPlacardsIndi, get/set methods
 * 							defect 11050 Ver 6.9.0	  
 * ---------------------------------------------------------------------
 */
/**
 * Timed permit data class for Miscellaneous Registration.
 *  
 * @version	6.9.0 			10/10/2011
 * @author	Joseph Kwik
 * <br>Creation Date:		11/07/2001 10:31:44  
 */

public class TimedPermitData implements java.io.Serializable
{
	
	private final static long serialVersionUID = 6262243315220354410L;

	// boolean 
	private boolean cbRegistered;
	private boolean cbSameVeh;

	// defect 10491 
	//private boolean cbEnterSelected = false;
	// end defect 10491 
	
	// int
	private int ciChrgFeeIndi;
	private int ciEffDt;
	private int ciEffTime;
	private int ciExpDt;
	private int ciExpTime;
	
	// defect 11050 
	//private int ciIssueTwoPlacardsIndi;
	// end defect 11050 
	
	private int ciNumPlacardsIssued; 
	private int ciTempVehCaryngCap;
	private int ciTempVehGrossWt;

	// String 
	private String csDlsCertNo = "";
	private String csEntryOriginPnt = null;
	private String csItmCd = "";
	private String csTimedPrmtType = "";
	private String csTowTrkPltNo = "";
	private String csVehRegState = "";

	// Object
	 private DisabledPlacardCustomerData caDPCustData = null;
	 private OneTripData caOneTripData = null;
	 private OwnerData caOwnrData = null;
	 private RTSDate caRTSDateEffDt = null;
	 private RTSDate caRTSDateExpDt = null;
	 private VehicleData caVehData = null;

	
	/**
	 * TimedPermitData constructor comment.
	 */
	public TimedPermitData()
	{
		super();
	}

	/**
	 * Return value of ChrgFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgFeeIndi()
	{
		return ciChrgFeeIndi;
	}
	
	/**
	 * Return value of DlsCertNo
	 * 
	 * @return String
	 */
	public String getDlsCertNo()
	{
		return csDlsCertNo;
	}
	
	/**
	 * Get DP Customer Data
	 * 
	 * @return
	 */
	public DisabledPlacardCustomerData getDPCustData()
	{
		return caDPCustData;
	}
	
	/**
	 * Return value of EffDt
	 * 
	 * @return int
	 */
	public int getEffDt()
	{
		return ciEffDt;
	}
	
	/**
	 * Return value of EffTime
	 * 
	 * @return int
	 */
	public int getEffTime()
	{
		return ciEffTime;
	}
	
	/**
	 * Return value of EntryOriginPnt
	 * 
	 * @return String
	 */
	public String getEntryOriginPnt()
	{
		return csEntryOriginPnt;
	}
	
	/**
	 * Return value of ExpDt
	 * 
	 * @return int
	 */
	public int getExpDt()
	{
		return ciExpDt;
	}
	
	/**
	 * Return value of ExpTime
	 * 
	 * @return int
	 */
	public int getExpTime()
	{
		return ciExpTime;
	}
	
	// defect 11050 
	//	/**
	//	* Return value of IssueTwoPlacardsIndi
	//	* 
	//	* @return int
	//	*/
	//	public int getIssueTwoPlacardsIndi()
	//	{
	//	return ciIssueTwoPlacardsIndi;
	//	}
	//	end defect 11050
	
	/**
	 * Return value of ItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}
	
	/**
	 * Return value of OneTripData
	 * 
	 * @return OneTripData
	 */
	public OneTripData getOneTripData()
	{
		if (caOneTripData == null) 
		{
			caOneTripData = new OneTripData(); 	
		}
		return caOneTripData;
	}
	
	/**
	 * Return value of OwnrData
	 * 
	 * @return OwnerData
	 */
	public OwnerData getOwnrData()
	{
		return caOwnrData;
	}
	
	/**
	 * Return value of RTSDateEffDt
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRTSDateEffDt()
	{
		return caRTSDateEffDt;
	}
	
	/**
	 * Return value of RTSDateExpDt
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRTSDateExpDt()
	{
		return caRTSDateExpDt;
	}
	
	/**
	 * Return value of TempVehCaryngCap
	 * 
	 * @return int
	 */
	public int getTempVehCaryngCap()
	{
		return ciTempVehCaryngCap;
	}
	
	/**
	 * Return value of TempVehGrossWt
	 * 
	 * @return int
	 */
	public int getTempVehGrossWt()
	{
		return ciTempVehGrossWt;
	}
	
	/**
	 * Return value of TimedPrmtType
	 * 
	 * @return String
	 */
	public String getTimedPrmtType()
	{
		return csTimedPrmtType;
	}
	
	/**
	 * Return value of TowTrkPltNo
	 * 
	 * @return String
	 */
	public String getTowTrkPltNo()
	{
		return csTowTrkPltNo;
	}
	
	/**
	 * Return value of VehData
	 * 
	 * @return VehicleData
	 */
	public VehicleData getVehData()
	{
		return caVehData;
	}
	
	/**
	 * Return value of VehRegState
	 * 
	 * @return String
	 */
	public String getVehRegState()
	{
		return csVehRegState;
	}
	
	/**
	 * Return value of Registered
	 * 
	 * @return boolean
	 */
	public boolean isRegistered()
	{
		return cbRegistered;
	}
	
	/**
	 * Return value of SameVeh
	 * 
	 * @return boolean
	 */
	public boolean isSameVeh()
	{
		return cbSameVeh;
	}
	
	/**
	 * Set value of ChrgFeeIndi
	 * 
	 * @param aiChrgFeeIndi int
	 */
	public void setChrgFeeIndi(int aiChrgFeeIndi)
	{
		ciChrgFeeIndi = aiChrgFeeIndi;
	}
	
	/**
	 * Set value of DlsCertNo
	 * 
	 * @param asDlsCertNo String
	 */
	public void setDlsCertNo(String asDlsCertNo)
	{
		csDlsCertNo = asDlsCertNo;
	}

	/**
	 * Set DP Customer Data
	 * 
	 * @param data
	 */
	public void setDPCustData(DisabledPlacardCustomerData data)
	{
		caDPCustData = data;
	}
	
	/**
	 * Set value of EffDt
	 * 
	 * @param aiEffDt int
	 */
	public void setEffDt(int aiEffDt)
	{
		ciEffDt = aiEffDt;
	}
	
	/**
	 * Set value of EffTime
	 * 
	 * @param aiEffTime int
	 */
	public void setEffTime(int aiEffTime)
	{
		ciEffTime = aiEffTime;
	}
	
	/**
	 * Set value of EntryOriginPnt
	 * 
	 * @param asEntryOriginPnt String
	 */
	public void setEntryOriginPnt(String asEntryOriginPnt)
	{
		csEntryOriginPnt = asEntryOriginPnt;
	}
	
	/**
	 * Set value of ExpDt
	 * 
	 * @param aiExpDt int
	 */
	public void setExpDt(int aiExpDt)
	{
		ciExpDt = aiExpDt;
	}
	
	/**
	 * Set value of ExpTime
	 * 
	 * @param aiExpTime int
	 */
	public void setExpTime(int aiExpTime)
	{
		ciExpTime = aiExpTime;
	}

	// defect 11050 
	//	/**
	//	* Set value of IssueTwoPlacardsIndi
	//	* 
	//	* @param aiChrgFeeIndi int
	//	*/
	//	public void setIssueTwoPlacardsIndi(int aiIssueTwoPlacardsIndi)
	//	{
	//	ciIssueTwoPlacardsIndi = aiIssueTwoPlacardsIndi;
	//	}
	// end defect 11050 
	
	/**
	 * Set value of ItmCd
	 * 
	 * @param asItmCd String
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	
	/**
	 * Set value of OneTripData
	 * 
	 * @param aaOneTripData OneTripData
	 */
	public void setOneTripData(OneTripData aaOneTripData)
	{
		caOneTripData = aaOneTripData;
	}
	
	/**
	 * Set value of OwnrData
	 * 
	 * @param aaOwnrData OwnerData
	 */
	public void setOwnrData(OwnerData aaOwnrData)
	{
		caOwnrData = aaOwnrData;
	}
	
	/**
	 * Set value of Registered
	 * 
	 * @param abRegistered boolean
	 */
	public void setRegistered(boolean abRegistered)
	{
		cbRegistered = abRegistered;
	}
	
	/**
	 * Set value of RTSDateEffDt
	 * 
	 * @param aaRTSDateEffDt RTSDate
	 */
	public void setRTSDateEffDt(RTSDate aaRTSDateEffDt)
	{
		caRTSDateEffDt = aaRTSDateEffDt;
	}
	
	/**
	 * Set value of RTSDateExpDt
	 * 
	 * @param aaRTSDateExpDt RTSDate
	 */
	public void setRTSDateExpDt(RTSDate aaRTSDateExpDt)
	{
		caRTSDateExpDt = aaRTSDateExpDt;
	}
	
	/**
	 * Set value of SameVeh
	 * 
	 * @param abSameVeh boolean
	 */
	public void setSameVeh(boolean abSameVeh)
	{
		cbSameVeh = abSameVeh;
	}
	
	/**
	 * Set value of TempVehCaryngCap
	 * 
	 * @param aiTempVehCaryngCap int
	 */
	public void setTempVehCaryngCap(int aiTempVehCaryngCap)
	{
		ciTempVehCaryngCap = aiTempVehCaryngCap;
	}
	
	/**
	 * Set value of TempVehGrossWt
	 * 
	 * @param aiTempVehGrossWt int
	 */
	public void setTempVehGrossWt(int aiTempVehGrossWt)
	{
		ciTempVehGrossWt = aiTempVehGrossWt;
	}
	
	/**
	 * Set value of TimedPrmtType
	 * 
	 * @param asTimedPrmtType String
	 */
	public void setTimedPrmtType(String asTimedPrmtType)
	{
		csTimedPrmtType = asTimedPrmtType;
	}
	
	/**
	 * Set value of TowTrkPltNo
	 * 
	 * @param asTowTrkPltNo String
	 */
	public void setTowTrkPltNo(String asTowTrkPltNo)
	{
		csTowTrkPltNo = asTowTrkPltNo;
	}
	
	/**
	 * Set value of VehData
	 * 
	 * @param aaVehData VehicleData
	 */
	public void setVehData(VehicleData aaVehData)
	{
		caVehData = aaVehData;
	}
	
	/**
	 * Set value of VehRegState
	 * 
	 * @param asVehRegState String
	 */
	public void setVehRegState(String asVehRegState)
	{
		csVehRegState = asVehRegState;
	}

	/**
	 * @return the ciNumPlacardsIssued
	 */
	public int getNumPlacardsIssued()
	{
		return ciNumPlacardsIssued;
	}

	/**
	 * @param ciNumPlacardsIssued the ciNumPlacardsIssued to set
	 */
	public void setNumPlacardsIssued(int ciNumPlacardsIssued)
	{
		this.ciNumPlacardsIssued = ciNumPlacardsIssued;
	}

}
