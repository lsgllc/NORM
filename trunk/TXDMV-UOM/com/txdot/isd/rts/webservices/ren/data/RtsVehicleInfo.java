package com.txdot.isd.rts.webservices.ren.data;

import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.WebAgencyTransactionData;

/*
 * RtsVehicleInfo.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/15/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/07/2011	Add new objects as required by table changes.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/17/2011	Add constructor to bring in WebAgencyTransactionData.
 * 							defect 10670 Ver 6.7.0
 * K Harrell	03/21/2011	Add assignment of PltAge from WebAgencyTransData
 * 							modify constructor
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	03/25/2011	add assignment of AccptTimestmp in constructor
 * 							renove assignment of CitationIndi, ReqIPAddr
 * 							defect 10768 Ver 6.7.1 
 * Ray Rowehl	03/28/2011	Add VoidAccs and ReprntAccs.
 * 							add cbReprtAccs, cbVoidAccs
 * 							add isReprtAccs(), isVoidAccs(),
 * 								setReprtAccs(), setVoidAccs()
 * 							defect 10673 Ver 6.7.1
 * K McKee      11/02/2011  modify  Constructor
 *  						Added sets for UserName and AgntIdntyNo to 
 *  						RtsVehicleInfo(WebAgencyTransactionData aaWATransData)
 *  						defect 11145 Ver 6.9.0
 * 						
 * ---------------------------------------------------------------------
 */

/**
 * Wrapper for all things about a vehicle for Web Agent.
 *
 * @version	6.9.0.			11/02/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/15/2011 13:23:41
 */

public class RtsVehicleInfo
{
	private RtsRegistrationData caRegistrationData;

	private RTSFees[] carrFees;
	private RtsSpecialPlates caSpecialPlts;
	private RtsTitleData caTitleData;

	private double caTotalFees;

	private RtsTransactionData caTransData;
	private RtsVehicleData caVehicleData;

	private boolean cbReprtAccs;
	private boolean cbVoidAccs;

	/**
	 * RtsVehicleInfo.java Constructor
	 */
	public RtsVehicleInfo()
	{
		super();
	}

	/**
	 * RtsVehicleInfo.java Constructor
	 * 
	 * @param aaData
	 */
	public RtsVehicleInfo(WebAgencyTransactionData aaWATransData)
	{
		super();
		setRegistrationData(new RtsRegistrationData());
		setTitleData(new RtsTitleData());
		setTransData(new RtsTransactionData());
		setVehicleData(new RtsVehicleData());

		getRegistrationData().setInsVerfdCd(
			aaWATransData.getInsVerfdCd());
		getRegistrationData().setMustReplPltIndi(
			aaWATransData.getMustReplPltIndi() == 1);
		getRegistrationData().setNewRegExpMo(
			aaWATransData.getNewRegExpMo());
		getRegistrationData().setNewRegExpYr(
			aaWATransData.getNewRegExpYr());
		getRegistrationData().setPltBirthDate(
			aaWATransData.getPltBirthDate());
		getRegistrationData().setPltAge(
			aaWATransData.getPltAge());
		getRegistrationData().setPrntQty(
			aaWATransData.getPrntQty());
		getRegistrationData().setRegClassCd(
			aaWATransData.getRegClassCd());
		getRegistrationData().setRegExpMo(
			aaWATransData.getRegExpMo());
		getRegistrationData().setRegExpYr(
			aaWATransData.getRegExpYr());
		getRegistrationData().setResComptCntyNo(
			aaWATransData.getResComptCntyNo());
		getRegistrationData().setVehGrossWt(
			aaWATransData.getVehGrossWt());
		getRegistrationData().setInvItmNo(
			aaWATransData.getInvItmNo());
		getRegistrationData().setRegPltCd(
			aaWATransData.getRegPltCd());
		getRegistrationData().setRegPltNo(
			aaWATransData.getRegPltNo());
		getRegistrationData().setStkrItmCd(
			aaWATransData.getStkrItmCd());

		if (PlateTypeCache.isSpclPlate(aaWATransData.getRegPltCd()))
		{
			setSpecialPlts(new RtsSpecialPlates());
			getSpecialPlts().setAddlSetIndi(
				aaWATransData.getAddlSetIndi());
			getSpecialPlts().setNewPltExpMo(
				aaWATransData.getNewPltExpMo());
			getSpecialPlts().setNewPltExpYr(
				aaWATransData.getNewPltExpYr());
			getSpecialPlts().setPltExpMo(
				aaWATransData.getPltExpMo());
			getSpecialPlts().setPltExpYr(
				aaWATransData.getPltExpYr());
			getSpecialPlts().setOrgNo(
				aaWATransData.getOrgNo());
			getSpecialPlts().setPltValidityTerm(
				aaWATransData.getPltValidityTerm());
		}

		getTitleData().setDocNo(
			aaWATransData.getDocNo());
		getTransData().setAccptVehIndi(
			aaWATransData.getAccptVehIndi() == 1);
		getTransData().setAgncyVoidIndi(
			aaWATransData.getAgncyVoidIndi() == 1);
		getTransData().setCntyVoidIndi(
			aaWATransData.getCntyVoidIndi() == 1);
		getTransData().setAgncyBatchIdntyNo(
			aaWATransData.getAgncyBatchIdntyNo());
		getTransData().setAgntSecrtyIdntyNo(
			aaWATransData.getAgntSecrtyIdntyNo());
		getTransData().setSAVReqId(
			aaWATransData.getSavReqId());
		getTransData().setSubconId(
			aaWATransData.getSubconId());
		getTransData().setAuditTrailTransId(
			aaWATransData.getAuditTrailTransId());
		getTransData().setBarCdVersionNo(
			aaWATransData.getBarCdVersionNo());
		getTransData().setKeyTypeCd(
			aaWATransData.getKeyTypeCd());
		getTransData().setPosTransId(
			aaWATransData.getTransId());
		// defect 11145  
		getTransData().setUserName(
			aaWATransData.getUserName());
		getTransData().setAgntIdntyNo(
			aaWATransData.getAgntIdntyNo());
		// end defect 11145
		if (aaWATransData.getAccptTimestmp() != null)
		{
			getTransData().setAccptTimestmp(
				aaWATransData.getAccptTimestmp().getCalendar());
		}
		getVehicleData().setVehMk(
			aaWATransData.getVehMk());
		getVehicleData().setVehModl(
			aaWATransData.getVehModl());
		getVehicleData().setVehModlYr(
			aaWATransData.getVehModlYr());
		getVehicleData().setVIN(
			aaWATransData.getVIN());
	}

	/**
	 * Get the Fees array.
	 * 
	 * @return RTSFees[]
	 */
	public RTSFees[] getFees()
	{
		return carrFees;
	}

	/**
	 * Get the Registration Data.
	 * 
	 * @return RtsRegistrationData
	 */
	public RtsRegistrationData getRegistrationData()
	{
		return caRegistrationData;
	}

	/**
	 * Get the Special Plates data.
	 * 
	 * @return RtsSpecialPlates
	 */
	public RtsSpecialPlates getSpecialPlts()
	{
		return caSpecialPlts;
	}

	/**
	 * Get the Title data.
	 * 
	 * @return RtsTitleData
	 */
	public RtsTitleData getTitleData()
	{
		return caTitleData;
	}

	/**
	 * Get the Total Fees.
	 * 
	 * @return double
	 */
	public double getTotalFees()
	{
		return caTotalFees;
	}

	/**
	 * Get the Rts Transaction Data.
	 * 
	 * @return RtsTransactionData
	 */
	public RtsTransactionData getTransData()
	{
		return caTransData;
	}

	/**
	 * Get the Vehicle Specific Data;
	 * 
	 * @return RtsVehicleData
	 */
	public RtsVehicleData getVehicleData()
	{
		return caVehicleData;
	}

	/**
	 * Check on existance of Fees Data.
	 * 
	 * @return boolean
	 */
	public boolean hasFees()
	{
		return (carrFees != null && carrFees.length > 0);
	}

	/**
	 * Check on existance of Registration Data.
	 * 
	 * @return boolean
	 */
	public boolean hasRegistrationData()
	{
		return (caRegistrationData != null);
	}

	/**
	 * Check on existance of Title Data.
	 * 
	 * @return boolean
	 */
	public boolean hasTitleData()
	{
		return (caTitleData != null);
	}

	/**
	 * Check on existance of Vehicle Data.
	 * 
	 * @return boolean
	 */
	public boolean hasVehicleData()
	{
		return (caVehicleData != null);
	}

	/**
	 * Indicates if the Agent has Reprint Access.
	 * 
	 * @return boolean
	 */
	public boolean isReprtAccs()
	{
		return cbReprtAccs;
	}

	/**
	 * Indicates if the Agent has Void Access.
	 * 
	 * @return boolean
	 */
	public boolean isVoidAccs()
	{
		return cbVoidAccs;
	}

	/**
	 * Set the array of Fees.
	 * 
	 * @param aarrFees
	 */
	public void setFees(RTSFees[] aarrFees)
	{
		carrFees = aarrFees;
	}

	/**
	 * Set the Registration Data.
	 * 
	 * @param aaRegistrationData
	 */
	public void setRegistrationData(RtsRegistrationData aaRegistrationData)
	{
		caRegistrationData = aaRegistrationData;
	}

	/**
	 * Set the Agent's Reprint Access.
	 * 
	 * @param abReprtAccs
	 */
	public void setReprtAccs(boolean abReprtAccs)
	{
		cbReprtAccs = abReprtAccs;
	}

	/**
	 * Set the Special Plates data.
	 * 
	 * @param aaSpecialPlts
	 */
	public void setSpecialPlts(RtsSpecialPlates aaSpecialPlts)
	{
		caSpecialPlts = aaSpecialPlts;
	}

	/**
	 * Set the Title Data.
	 * 
	 * @param aaTitleData
	 */
	public void setTitleData(RtsTitleData aaTitleData)
	{
		caTitleData = aaTitleData;
	}

	/**
	 * Set the Total Fees.
	 * 
	 * @param aaTotalFees
	 */
	public void setTotalFees(double aaTotalFees)
	{
		caTotalFees = aaTotalFees;
	}

	/**
	 * Set the Rts Transaction Data.
	 * 
	 * @param aaTransData
	 */
	public void setTransData(RtsTransactionData aaTransData)
	{
		caTransData = aaTransData;
	}

	/**
	 * Set the Vehicle Specific Data;
	 * 
	 * @param aaVehicleData
	 */
	public void setVehicleData(RtsVehicleData aaVehicleData)
	{
		caVehicleData = aaVehicleData;
	}

	/**
	 * Set the Agent's Void Access.
	 * 
	 * @param abVoidAccs
	 */
	public void setVoidAccs(boolean abVoidAccs)
	{
		cbVoidAccs = abVoidAccs;
	}

}
