package com.txdot.isd.rts.client.specialplates.business;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 * SpecialPlatesClientBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/11/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	add processData(), renamed class 
 * 							defect 9085 Ver Special Plates  
 * J Rue		03/13/2007	Add CALC_FEES case to get fees
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/21/2007	Working...
 * J Rue		03/20/2007	Add getVeh(). save RequestType and RegPltCd
 * 							add saveData()
 * 							Check request type for save data options
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/23/2007	Add Validate Mf record for errors and 
 * 							application
 * 							Add check for SPRNWL and in regis window
 * 							add isMFRecordValid()
 * 							modify getVeh()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/23/2007	Move saveSPData() to 
 * 							SpecialPlatesClientUtilityMethods 
 * 							modift getVeh()
 * 							defect 9086 Ver Special Plates
 * T Pederson	03/26/2007	add saveReports() 
 * 							defect 9123 Ver Special Plates 
 * J Rue		03/30/2007	Add SPRSRV and SPUNAC processing
 * 							found
 * 							modify processingContinues() 
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/23/2007	Modify VI calls for new VI architecture
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/06/2007	Use method local to SpecialPlatesRegisData
 * 							to determine MfgPltNo
 * 							delete determineMfgPltNo() 
 * 							modify setVIItem()
 * 							defect 9085 Ver Special Plates 	
 * K Harrell	08/17/2009	Implement UtilityMethods.addPCLandSaveReport(),
 * 							delete saveReports() 
 * 							modify processData()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	04/08/2010	Do not convert I's and O's; Handled by 
 * 							 VehicleInquiry.
 * 							modify getVeh()
 * 							defect 9858 Ver POS_640 
 * K Harrell	12/26/2010	modify processData() 
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Main entry class for Special Plates client business layer.
 * 
 * @version	6.7.0			12/26/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		02/11/2007	12:55:54
 */
public class SpecialPlatesClientBusiness
{
	/**
	 * SpecialPlatesClientBusiness constructor comment.
	 */
	public SpecialPlatesClientBusiness()
	{
		super();
	}

	/**
	 * Get vehicle data from MF
	 * 
	 * @param aiModule		int
	 * @param aiFunctionId	int
	 * @param aaData		Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVeh(int aiFunctionId, Object aaData)
		throws RTSException
	{
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		GeneralSearchData laGSD = new GeneralSearchData();

		try
		{
			laGSD = (GeneralSearchData) aaData;

			// defect 9858 
			// No need to convert; Handled by VehicleInquiry
			//laGSD.setKey2(
			//	CommonValidations.convert_i_and_o_to_1_and_0(
			//		laGSD.getKey2()));
			// end defect 9858 

			// Get vehicle record from MF
			VehicleInquiryData laVehInqData =
				(
					VehicleInquiryData) laCommonClientBusiness
						.processData(
					GeneralConstant.COMMON,
					aiFunctionId,
					laGSD);

			return laVehInqData;

		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN)
				|| aeRTSEx.getMsgType().equals(RTSException.MF_DOWN))
			{
				// Display "RECORD RETRIEVAL DOWN" message
				aeRTSEx.setCode(1);
				throw aeRTSEx;
			}
			else if (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
			{
				// Display "DB SERVER DOWN" message
				aeRTSEx.setCode(611);
				throw aeRTSEx;
			}
			else
			{
				throw aeRTSEx;
			}
		}
	}

	/**
	 * Main entry method for Special Plates Client Business.
	 *
	 * @return Object 
	 * @param aiModule		int 
	 * @param aiFunctionId 	int 
	 * @param aaData 		Object  
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			// defect 10700 
			case SpecialPlatesConstant.GET_DUPL_INSIG :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
			// end defect 10700 
			
			case SpecialPlatesConstant.GENERATE_SPCL_PLT_APPL_REPORT :
				{
					// defect 8628 
					return UtilityMethods.addPCLandSaveReport(
						Comm.sendToServer(
							aiModule,
							aiFunctionId,
							aaData));
					// end defect 8628 
				}

				// Intentionally dropping through 
			case InventoryConstant
				.INV_VI_UPDATE_INV_STATUS_CD_RECOVER :
			case InventoryConstant.INV_GET_NEXT_VI_ITEM_NO :
			case InventoryConstant.INV_VI_VALIDATE_PER_PLT :
				{
					InventoryAllocationData laInvAllocData =
						((VehicleInquiryData) aaData)
							.getMfVehicleData()
							.getSpclPltRegisData()
							.getVIAllocData();
					Object laObject =
						Comm.sendToServer(
							GeneralConstant.INVENTORY,
							aiFunctionId,
							laInvAllocData);

					if (laObject instanceof InventoryAllocationData
						&& aaData instanceof VehicleInquiryData)
					{
						return (
							setVIItem(
								(VehicleInquiryData) aaData,
								(InventoryAllocationData) laObject));
					}
					else
					{
						return aaData;
					}
				}
			case SpecialPlatesConstant.GET_VEH :
				{
					return getVeh(aiFunctionId, aaData);
				}
			default :
				{
					return null;
				}
		}
	}

	/**
	 * Set values in VehicleInquiry Object
	 * 
	 * @param aaVehInqData		VehicleInquiryData
	 * @param aaInvAllocData	InventoryAllocationData
	 * @return VehicleInquiryData 
	 */
	private VehicleInquiryData setVIItem(
		VehicleInquiryData aaVehInqData,
		InventoryAllocationData aaInvAllocData)
	{
		if (aaVehInqData != null)
		{
			MFVehicleData laMFVehData = aaVehInqData.getMfVehicleData();
			if (laMFVehData != null)
			{
				SpecialPlatesRegisData laSpclPltRegisData =
					laMFVehData.getSpclPltRegisData();
				if (laSpclPltRegisData != null)
				{
					laSpclPltRegisData.setVIAllocData(aaInvAllocData);
					laSpclPltRegisData.setRegPltNo(
						aaInvAllocData.getInvItmNo());
					laSpclPltRegisData.setMfgPltNo();
				}
			}
		}
		return aaVehInqData;
	}
}