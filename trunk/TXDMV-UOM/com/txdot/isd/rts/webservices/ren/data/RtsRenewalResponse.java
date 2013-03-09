package com.txdot.isd.rts.webservices.ren.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.PlateTypeData;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;

/*
 * RtsRenewalResponse.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/18/2010	Initial load.
 * 							defect 10670 Ver WebSub
 * Ray Rowehl	12/15/2010	Add Identity Number.	
 * 							add cbInsuranceRequired, ciRequestIdntyNo
 * 							add getRequestIdntyNo(), isInsuranceRequired(), 
 * 								setInsuranceRequired(), setRequestIdntyNo()
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	12/29/2010	Add OfcName so WebAgent can display it.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/03/2011	Add the Vehicle Gross Weight.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/07/2011	Incorporate RtsRenewalData.
 * 							defect 10670 Ver 6.7.0
 * K Harrell	03/09/2011	add setupFromVehRecord()
 * 							defect 10768 Ver 6.7.1
 * Ray Rowehl	03/28/2011	Fix errors from removing insurance booleans.
 * 							modify setupFromVehRecord()
 * 							defect 10673 Ver 6.7.1  
 * K Harrell	04/22/2011	Do not create RtsSpecialPlates unless 
 * 							Special Plate attached to Vehicle.  Use  
 * 							minimum PltBirthDate between Regis & SpclPlt
 * 							Data to determine Plate Age.   
 * 							modify setupFromVehRecord() 
 * 							defect 10768 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * Intial Renewal Reponse for WebSub.
 *
 * @version	6.7.1			04/22/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		11/18/2010 10:00:42
 */

public class RtsRenewalResponse extends RtsAbstractResponse
{
	private RtsVehicleInfo caRtsVehInfo;

	/**
	 * RtsRenewalResponse Constructor
	 */
	public RtsRenewalResponse()
	{
		super();
	}

	/**
	 * Get the Rts Web Service Vehicle Info.
	 * 
	 * @return RtsVehicleInfo
	 */
	public RtsVehicleInfo getRtsVehInfo()
	{
		return caRtsVehInfo;
	}

	/**
	 * Set the Rts Web Service Vehicle Info.
	 * 
	 * @param aaRtsVehInfo
	 */
	public void setRtsVehInfo(RtsVehicleInfo aaRtsVehInfo)
	{
		caRtsVehInfo = aaRtsVehInfo;
	}

	/**
	 * Setup From Veh Record
	 * 
	 * @param aaVID 
	 */
	public void setupFromVehRecord(
		VehicleInquiryData aaVID,
		RtsRenewalRequest aaRequest)
	{
		if (aaVID != null && aaVID.getMfVehicleData() != null)
		{
			if (getRtsVehInfo() == null)
			{
				setRtsVehInfo(new RtsVehicleInfo());
			}

			if (getRtsVehInfo().getVehicleData() == null)
			{
				getRtsVehInfo().setVehicleData(new RtsVehicleData());
			}

			if (getRtsVehInfo().getRegistrationData() == null)
			{
				getRtsVehInfo().setRegistrationData(
					new RtsRegistrationData());
			}

			if (getRtsVehInfo().getTitleData() == null)
			{
				getRtsVehInfo().setTitleData(new RtsTitleData());
			}

			if (getRtsVehInfo().getTransData() == null)
			{
				getRtsVehInfo().setTransData(new RtsTransactionData());
			}

			// set up the Vehicle info
			if (aaVID.getMfVehicleData().getVehicleData() != null)
			{
				getRtsVehInfo().getVehicleData().setVIN(
					aaVID.getMfVehicleData().getVehicleData().getVin());

				getRtsVehInfo().getVehicleData().setVehModlYr(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehModlYr());

				getRtsVehInfo().getVehicleData().setVehMk(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehMk());

				getRtsVehInfo().getVehicleData().setVehModl(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehModl());
			}

			// set up the regis info
			// Verify RegData is not null 
			if (aaVID.getMfVehicleData().getRegData() != null)
			{
				RegistrationData laRegData =
					aaVID.getMfVehicleData().getRegData();

				getRtsVehInfo()
					.getRegistrationData()
					.setResComptCntyNo(
					laRegData.getResComptCntyNo());

				getRtsVehInfo()
					.getRegistrationData()
					.setResComptCntyName(
					OfficeIdsCache.getOfcName(
						laRegData.getResComptCntyNo()));

				getRtsVehInfo().getRegistrationData().setRegPltNo(
					laRegData.getRegPltNo());

				getRtsVehInfo().getRegistrationData().setRegExpMo(
					laRegData.getRegExpMo());

				getRtsVehInfo().getRegistrationData().setRegExpYr(
					laRegData.getRegExpYr());

				getRtsVehInfo().getRegistrationData().setVehGrossWt(
					laRegData.getVehGrossWt());

				getRtsVehInfo().getRegistrationData().setInvItmNo("");

				getRtsVehInfo().getRegistrationData().setPrntQty(0);

				getRtsVehInfo().getRegistrationData().setRegPltCd(
					laRegData.getRegPltCd());

				getRtsVehInfo().getRegistrationData().setStkrItmCd(
					laRegData.getRegStkrCd());

				getRtsVehInfo().getRegistrationData().setRegClassCd(
					laRegData.getRegClassCd());

				// Set up the Insurance Code
				String lsInsVerfdCd = "M";

				if (!laRegData.isInsuranceRequired())
				{
					lsInsVerfdCd = "N";
				}
				else if (laRegData.isInsuranceVerified())
				{
					lsInsVerfdCd = "V";
				}
				getRtsVehInfo().getRegistrationData().setInsVerfdCd(
					lsInsVerfdCd);
			}

			int liPltBirthDate =
				aaVID.getMfVehicleData().getRegData().getPltBirthDate();

			// set up the special plate data
			if (aaVID.getMfVehicleData().getSpclPltRegisData() != null)
			{
				getRtsVehInfo().setSpecialPlts(new RtsSpecialPlates());

				getRtsVehInfo().getSpecialPlts().setOrgNo(
					aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getOrgNo());
				getRtsVehInfo().getSpecialPlts().setPltExpMo(
					aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getPltExpMo());
				getRtsVehInfo().getSpecialPlts().setPltExpYr(
					aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getPltExpYr());
				getRtsVehInfo().getSpecialPlts().setPltValidityTerm(
					aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getPltValidityTerm());
				getRtsVehInfo().getSpecialPlts().setAddlSetIndi(
					aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getAddlSetIndi());

				// Reset RegisData PltBirthDate if greater than Special
				// Plate PltBirthDate    		
				if (liPltBirthDate
					> aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getPltBirthDate())
				{
					aaVID
						.getMfVehicleData()
						.getRegData()
						.setPltBirthDate(
						aaVID
							.getMfVehicleData()
							.getSpclPltRegisData()
							.getPltBirthDate());
				}
			}
			getRtsVehInfo().getRegistrationData().setPltBirthDate(
				aaVID
					.getMfVehicleData()
					.getRegData()
					.getPltBirthDate());

			// set plate age
			// have to set it to negative for the computation to work.
			aaVID.getMfVehicleData().getRegData().setRegPltAge(-1);

			// Boolean to true (Renewal) 
			getRtsVehInfo().getRegistrationData().setPltAge(
				aaVID.getMfVehicleData().getRegData().getRegPltAge(
					true));

			// set up the manditory plate replacement indi
			PlateTypeData laPlateTypeData =
				PlateTypeCache.getPlateType(
					getRtsVehInfo()
						.getRegistrationData()
						.getRegPltCd());

			if (laPlateTypeData != null)
			{
				// If New Plate or over Mandatory Replacement Age  
				getRtsVehInfo()
					.getRegistrationData()
					.setMustReplPltIndi(
					laPlateTypeData.isAnnualPlt()
						|| laPlateTypeData.getMandPltReplAge()
							<= getRtsVehInfo()
								.getRegistrationData()
								.getPltAge());
			}

			// set up title info
			if (aaVID.getMfVehicleData().getTitleData() != null)
			{
				getRtsVehInfo().getTitleData().setDocNo(
					aaVID.getMfVehicleData().getTitleData().getDocNo());
			}

			// set up transaction info
			getRtsVehInfo().getTransData().setAuditTrailTransId(
				aaVID
					.getMfVehicleData()
					.getVehicleData()
					.getAuditTrailTransId());

			getRtsVehInfo().getTransData().setSAVReqId(
				aaRequest.getRequestIdntyNo());

			getRtsVehInfo().getTransData().setAuditTrailTransId(
				aaVID
					.getMfVehicleData()
					.getVehicleData()
					.getAuditTrailTransId());

			int liAgntSecurityIdntyNo = 0;

			try
			{
				liAgntSecurityIdntyNo =
					Integer.parseInt(aaRequest.getCaller());
			}
			catch (NumberFormatException aeNFEx)
			{
			}

			getRtsVehInfo().getTransData().setAgntSecrtyIdntyNo(
				liAgntSecurityIdntyNo);

			// Replace w/ WebSessionId 
			//	getRtsVehInfo().getTransData().setReqIpAddr(
			//	aaRequest.getSessionId());

			getRtsVehInfo().getTransData().setKeyTypeCd(
				aaRequest.getKeyTypeCd());

			getRtsVehInfo().getTransData().setBarCdVersionNo(
				aaRequest.getBarCdVersionNo());
		}

	}

}
