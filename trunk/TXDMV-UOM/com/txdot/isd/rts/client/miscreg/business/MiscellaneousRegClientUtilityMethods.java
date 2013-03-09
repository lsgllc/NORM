package com.txdot.isd.rts.client.miscreg.business;
import java.util.Vector;

import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * MiscellaneousRegClientUtilityMethods.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/07/2005	change import for Fees
 * 							format source.  
 * 							Clean up field names.
 *							modify import 
 *							defect 7705 Ver 5.2.3
 * B Hargrove	06/29/2005	Refactor\Move 
 * 							MiscellaneousRegClientUtilityMethods class  
 *							from com.txdot.isd.rts.client.miscreg.ui.
 *							defect 7893 Ver 5.2.3
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 *							modify prepFees()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	10/21/2008	New Enhancements for Disabled Placard 
 * 							add getDsabldPlcrdComplTransData()
 * 							modify prepFees(TimedPermitData, MFVehicleData,
 * 							  MFVehicleData, VehMiscData)
 * 							defect 9831 Ver Defect_POS_B  
 * K Harrell	06/29/2009	Implement new OwnerData
 * 							delete constructor
 * 							modify getDsabldPlcrdComplTransData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	05/24/2010	add setupPrmtInvAlloc()
 * 							modify modify prepFees(TimedPermitData, MFVehicleData,
 * 							  MFVehicleData, VehMiscData)
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	05/28/2011	modify prepFees(TimedPermitData,MFVehicleData,
 * 							  MFVehicleData,VehMiscData)
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	10/11/2011 modify getDsabldPlcrdComplTransData(),
 * 							prepFees(TimedPermitData,MFVehicleData,
 * 							  MFVehicleData,VehMiscData)
 * 							defect 11050 Ver 6.9.0 
 * K Harrell	11/12/2011	modify prepFees(TimedPermitData,MFVehicleData,
 * 							  MFVehicleData,VehMiscData)
 * 							defect 11050 Ver 6.9.0 
 * K Harrell	02/05/2012	modify prepFees(TimedPermitData,MFVehicleData,
 * 							  MFVehicleData,VehMiscData)
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Miscellaneous Registration client utility methods.
 * 
 * @version	6.10.0  		02/05/2012
 * @author	Joseph Kwik
 * @since			 		11/09/2001 14:53:10
 */

public class MiscellaneousRegClientUtilityMethods
{
	private static final String TOW_TRUCK_PLT_ITEM_CODE = "TOWP";

	/**
	 * Returns Complete Transaction Data Object for Disabled Placard 
	 * 	Transaction 
	 *  
	 * @param aaDPCustData
	 * @return CompleteTransactionData
	 */
	public static CompleteTransactionData getDsabldPlcrdComplTransData(DisabledPlacardCustomerData aaDPCustData)
	{
		TimedPermitData laTimedPermitData = new TimedPermitData();
		MFVehicleData laMFVehData = new MFVehicleData();
		OwnerData laOwnrData = new OwnerData();
		// defect 10112
		laOwnrData.setName1(aaDPCustData.getOwnerNameShort());
		laOwnrData.setName2("");
		// end defect 10112 
		laOwnrData.setAddressData(aaDPCustData.getAddressData());
		laMFVehData.setOwnerData(laOwnrData);
		laTimedPermitData.setItmCd(aaDPCustData.getItmCd());
		laTimedPermitData.setRTSDateEffDt(
			new RTSDate(
				RTSDate.YYYYMMDD,
				aaDPCustData.getRTSEffDate()));
		laTimedPermitData.setEffDt(aaDPCustData.getRTSEffDate());
		int liExpDate =
			aaDPCustData.getRTSExpYr() * 10000
				+ aaDPCustData.getRTSExpMo() * 100
				+ 1;
		laTimedPermitData.setRTSDateExpDt(
			new RTSDate(RTSDate.YYYYMMDD, liExpDate));
		String lsTransCd = aaDPCustData.getTransCd();
		laTimedPermitData.setTimedPrmtType(lsTransCd);
		
		// defect 11050
		if (UtilityMethods
			.getEventType(lsTransCd)
			.equals(TransCdConstant.ADD_DP_EVENT_TYPE)  
			||UtilityMethods
			.getEventType(lsTransCd)
			.equals(TransCdConstant. RPL_DP_EVENT_TYPE))
		{
			//	laTimedPermitData.setIssueTwoPlacardsIndi(
			//		aaDPCustData.getIssueTwoPlacards());
			laTimedPermitData.setNumPlacardsIssued(
					aaDPCustData.getNumPlacardsIssued());
			// end defect 11050 
		}
		laTimedPermitData.setChrgFeeIndi(aaDPCustData.getChrgFeeIndi());
		laTimedPermitData.setDPCustData(aaDPCustData);
		laTimedPermitData.setOwnrData(laOwnrData);
		return prepFees(laTimedPermitData, laMFVehData, null, null);
	}

	/**
	 * Prepares the data needed for calling calcFees().
	 * 
	 * @param aaTimedPermitData TimedPermitData
	 * @param aaNewMFVehData MFVehicleData
	 * @return CompleteTransactionData
	 */
	public static CompleteTransactionData prepFees(
		TimedPermitData aaTimedPermitData,
		MFVehicleData aaNewMFVehData)
	{
		return prepFees(aaTimedPermitData, aaNewMFVehData, null, null);
	}

	/**
	 * Prepares the data needed for calling calcFees().
	 * 
	 * @param aaTimedPermitData  TimedPermitData
	 * @param aaNewMFVehData  MFVehicleData
	 * @param aaOldMFVehData  MFVehicleData
	 * @param aaVehMiscData  VehMiscData
	 * @return CompleteTransactionData
	 */
	public static CompleteTransactionData prepFees(
		TimedPermitData aaTimedPermitData,
		MFVehicleData aaNewMFVehData,
		MFVehicleData aaOldMFVehData,
		VehMiscData aaVehMiscData)
	{
		String lsTransCd = aaTimedPermitData.getTimedPrmtType();

		RegTtlAddlInfoData laRegTtlAddlInfoData =
			new RegTtlAddlInfoData();

		// defect 10491 
		boolean lbPermitTrans = aaTimedPermitData instanceof PermitData;

		if (lbPermitTrans
			&& ((PermitData) aaTimedPermitData).isPrmDupTrans())
		{
			lsTransCd = TransCdConstant.PRMDUP;
			laRegTtlAddlInfoData.setChrgFeeIndi(
				aaTimedPermitData.getChrgFeeIndi());
		}
		// end defect 10491
		// defect 10844 
		else if (
			lbPermitTrans
				&& ((PermitData) aaTimedPermitData).isModPtTrans())
		{
			lsTransCd = TransCdConstant.MODPT;
			laRegTtlAddlInfoData.setChrgFeeIndi(0);
		}
		// end defect 10844 

		// defect 9831 
		boolean lbReiPlcrd = UtilityMethods.getEventType(lsTransCd).equals(
				TransCdConstant.REI_DP_EVENT_TYPE);
		
		
		boolean lbDelPlcrd =
			UtilityMethods.getEventType(lsTransCd).equals(
				TransCdConstant.DEL_DP_EVENT_TYPE);

		//		if (lsTransCd.equals(TransCdConstant.BPM)
		//			|| lsTransCd.equals(TransCdConstant.BTM)
		//			|| lsTransCd.equals(TransCdConstant.RPNM)
		//			|| lsTransCd.equals(TransCdConstant.RTNM))
		// end defect 8268
		
		// defect 11214 
		// Add Renew 
		if (UtilityMethods
			.getEventType(lsTransCd)
			.equals(TransCdConstant.ADD_DP_EVENT_TYPE)
			|| UtilityMethods.getEventType(lsTransCd).equals(
				TransCdConstant.RPL_DP_EVENT_TYPE)
			|| UtilityMethods.getEventType(lsTransCd).equals(
					TransCdConstant.REN_DP_EVENT_TYPE))
			// end defect 11214  
		{
			laRegTtlAddlInfoData.setChrgFeeIndi(
				aaTimedPermitData.getChrgFeeIndi());
		}
		Vector lvInvItms = new Vector();

		// defect 10491
		// Timed Permit no longer issues inventory  
		if (!lsTransCd.equals(TransCdConstant.TAWPT) && !lbPermitTrans)
		{
			// end defect 10491 

			if (aaNewMFVehData.getRegData() == null)
			{
				aaNewMFVehData.setRegData(new RegistrationData());
			}
			// defect 9831 
			// defect 11214 
			if (!lbDelPlcrd && !lbReiPlcrd)
			{
				// end defect 11214 
				
				ProcessInventoryData laInvData =
					new ProcessInventoryData();
				laInvData.setTransEmpId(
					SystemProperty.getCurrentEmpId());
				laInvData.setSubstaId(SystemProperty.getSubStationId());
				laInvData.setTransWsId(
					Integer.toString(
						SystemProperty.getWorkStationId()));
				laInvData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laInvData.setItmCd(aaTimedPermitData.getItmCd());
				
				lvInvItms.add(laInvData);
				
				// defect 8268
				// if (aaTimedPermitData.getIssueTwoPlacardsIndi() == 1)
				// {
				for (int i = 1; i< aaTimedPermitData.getNumPlacardsIssued(); i++)
				{
					lvInvItms.add(laInvData);
				}
				// end defect 11050 
			}
			// end defect 9831 
			// end defect 8268
		}
		if (lsTransCd.equals(TransCdConstant.TOWP))
		{
			if (aaTimedPermitData.getTowTrkPltNo().equals(""))
			{
				// Issue a plate in addition to a sticker
				ProcessInventoryData laInvData =
					new ProcessInventoryData();
				laInvData.setTransEmpId(
					SystemProperty.getCurrentEmpId());
				laInvData.setSubstaId(SystemProperty.getSubStationId());
				laInvData.setTransWsId(
					Integer.toString(
						SystemProperty.getWorkStationId()));
				laInvData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laInvData.setItmCd(TOW_TRUCK_PLT_ITEM_CODE);
				lvInvItms.add(laInvData);
			}
			// set regpltcd for fees
			if (aaNewMFVehData.getRegData() == null)
			{
				aaNewMFVehData.setRegData(new RegistrationData());
			}
			aaNewMFVehData.getRegData().setRegPltCd(
				TransCdConstant.TOWP);
		}
		if (!lsTransCd.equals(TransCdConstant.TOWP))
		{
			aaNewMFVehData.getRegData().setRegEffDt(
				aaTimedPermitData.getRTSDateEffDt().getYYYYMMDDDate());
		}
		CompleteTransactionData laCompTransData =
			new CompleteTransactionData();
		laCompTransData.setRegTtlAddlInfoData(laRegTtlAddlInfoData);
		if (aaOldMFVehData == null)
		{
			laCompTransData.setOrgVehicleInfo(aaNewMFVehData);
		}
		else
		{
			laCompTransData.setOrgVehicleInfo(aaOldMFVehData);
		}
		laCompTransData.setVehicleInfo(aaNewMFVehData);
		laCompTransData.setInvItms(lvInvItms);

		// defect 9831
		// defect 10491
		// Add check for lbPermitTrans 
		int liInvItmCount = (lbDelPlcrd || lbPermitTrans || lbReiPlcrd) ? 0 : 1;
		// end defect 10491 
		laCompTransData.setInvItemCount(liInvItmCount);
		// end defect 9831 

		laCompTransData.setTimedPermitData(aaTimedPermitData);
		laCompTransData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laCompTransData.setVehMisc(aaVehMiscData);
		Fees laFees = new Fees();

		// defect 10491
		// Use TransCd; could be PRMDUP 
		laCompTransData = laFees.calcFees(
			//aaTimedPermitData.getTimedPrmtType(),
	lsTransCd, laCompTransData);
		// end defect 10491 

		if (lsTransCd.equals(TransCdConstant.TOWP))
		{
			// set owner supplied plate number to VehMiscData
			laCompTransData.setOwnrSuppliedPltNo(
				aaTimedPermitData.getTowTrkPltNo());
		}
		return laCompTransData;
	}

	/**
	 * Setup the Inventory Allocation Data Object for Timed Permit 
	 *
	 * @param asItmCd  
	 * @return InventoryAllocationData 
	 * @throws RTSException 
	 */
	public static InventoryAllocationData setupPrmtInvAlloc(String asItmCd)
		throws RTSException
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(asItmCd);
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(false);
		laInvAllocData.setUserPltNo(false);
		laInvAllocData.setItrntReq(false);
		laInvAllocData.setMfgPltNo("");
		laInvAllocData.setInvQty(1);
		laInvAllocData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laInvAllocData.setRequestorRegPltNo("");
		laInvAllocData.setTransWsId(SystemProperty.getWorkStationId());
		laInvAllocData.setTransAmDate(new RTSDate().getAMDate());
		laInvAllocData.setTransEmpId(SystemProperty.getCurrentEmpId());
		String lsHostName = new String();
		try
		{
			lsHostName =
				java.net.InetAddress.getLocalHost().getHostName();
		}

		catch (java.net.UnknownHostException aeUHEx)
		{

		}
		laInvAllocData.setRequestorIpAddress(lsHostName);
		return laInvAllocData;
	}
}