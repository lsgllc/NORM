package com.txdot.isd.rts.server.webapps.order.virtualinv.business;

import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.server.webapps.order.common.business.AbstractBusiness;
import com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest;
import com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvResponse;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

/*
 * VirtualInventoryBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/06/2007	Created Class.
 * 							defect 9120 Ver Special Plates
 * Bob B.		10/10/2007	Comment out the code that sends in the
 * 							request to update transtime in inv_virtual
 * 							Transtime is used (when it is still 0) to 
 * 							let other users get the plate when one user
 * 							does not finish the order.
 * 							defect 9358 Ver Special Plates.    
 * ---------------------------------------------------------------------
 */

/**
 * Business class used to handle the Virtual Inventory Requests.
 *
 * @version	Special Plates	10/10/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/06/2007 13:40:00
 */
public class VirtualInventoryBusiness extends AbstractBusiness
{

	RTSDate caRTSDate = new RTSDate();

	/**
	 * Confirms the hold on all plates that were in the process 
	 * of being ordered
	 * 
	 * @param aarrVIReq VirtualInvRequest[]
	 * @param aarrVIResp VirtualInvResponse[]
	 * @param aiInd int 
	 * @return VirtualInvResponse[]
	 */
	public VirtualInvResponse[] confirmHold(
		VirtualInvRequest[] aarrVIReq,
		VirtualInvResponse[] aarrVIResp,
		int aiInd)
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(aarrVIReq[aiInd].getItemCode());
		laInvAllocData.setInvItmNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setInvItmEndNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(aarrVIReq[aiInd].isIsaFlag());
		laInvAllocData.setUserPltNo(aarrVIReq[aiInd].isPlpFlag());
		laInvAllocData.setMfgPltNo(
			aarrVIReq[aiInd].getManufacturingPltNoRequest());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		laInvAllocData.setTransEmpId("IAPPL");
		laInvAllocData.setRequestorIpAddress(
			aarrVIReq[aiInd].getRequestingIP());
		laInvAllocData.setRequestorRegPltNo("");

		try
		{
			InventoryServerBusiness laInvServBus =
				new InventoryServerBusiness();
			laInvAllocData =
				(InventoryAllocationData) laInvServBus.processData(
					0,
					InventoryConstant.INV_VI_CONFIRM_HOLD,
					laInvAllocData);

			if (laInvAllocData.getErrorCode() == 0)
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_SUCCESS);
			}
			else
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
			}

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			setError(aarrVIResp[aiInd], aeRTSEx);
		}

		return aarrVIResp;
	}

	/**
	 * Gets the next available inventory item from virtual inventory
	 * 
	 * @param aarrVIReq VirtualInvRequest[]
	 * @param aarrVIResp VirtualInvResponse[]
	 * @param aiInd int 
	 * @return VirtualInvResponse[]
	 */
	public VirtualInvResponse[] getNextItem(
		VirtualInvRequest[] aarrVIReq,
		VirtualInvResponse[] aarrVIResp,
		int aiInd)
	{

		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();

		laInvAllocData.setItmCd(aarrVIReq[aiInd].getItemCode());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(aarrVIReq[aiInd].isIsaFlag());
		laInvAllocData.setUserPltNo(aarrVIReq[aiInd].isPlpFlag());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		//laRTSDate = new RTSDate();
		laInvAllocData.setTransAmDate(caRTSDate.getAMDate());
		laInvAllocData.setTransEmpId("IAPPL");
		laInvAllocData.setRequestorIpAddress(
			aarrVIReq[aiInd].getRequestingIP());
		laInvAllocData.setRequestorRegPltNo("");

		try
		{
			InventoryServerBusiness laInvServBus =
				new InventoryServerBusiness();
			laInvAllocData =
				(InventoryAllocationData) laInvServBus.processData(
					0,
					InventoryConstant.INV_GET_NEXT_VI_ITEM_NO,
					laInvAllocData);

			if (laInvAllocData.getErrorCode() == 0)
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_SUCCESS);
				aarrVIResp[aiInd].setRegPlateNo(
					laInvAllocData.getInvItmNo());
			}
			else
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
			}

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			setError(aarrVIResp[aiInd], aeRTSEx);
		}

		return aarrVIResp;
	}
	
	/**
	 * Inherited method used to handle all of the functions within this
	 * module.
	 */
	public Object processData(Object aaObject)
	{
		if (aaObject == null)
		{
			return null;
		}

		VirtualInvRequest[] larrVIReq = (VirtualInvRequest[]) aaObject;
		VirtualInvResponse[] larrVIResp =
			new VirtualInvResponse[larrVIReq.length];

		for (int i = 0; i < larrVIReq.length; i++)
		{
			larrVIResp[i] = new VirtualInvResponse();

			switch (larrVIReq[i].getAction())
			{
				case ServiceConstants
					.VI_ACTION_VALIDATE_PLP_NORESERVE :
					{
						larrVIResp =
							validatePLPNoReserve(
								larrVIReq,
								larrVIResp,
								i);
						break;
					}
				case ServiceConstants.VI_ACTION_RELEASE_HOLD :
					{
						larrVIResp =
							releaseHold(larrVIReq, larrVIResp, i);
						break;
					}

				case ServiceConstants.VI_ACTION_CONFIRM_HOLD :
					{
						larrVIResp =
							confirmHold(larrVIReq, larrVIResp, i);
						break;
					}
				case ServiceConstants.VI_ACTION_GET_NEXT_ITEM :
					{
						larrVIResp =
							getNextItem(larrVIReq, larrVIResp, i);
						break;
					}

				case ServiceConstants.VI_ACTION_VALIDATE_PLP :
					{
						larrVIResp =
							validatePLP(larrVIReq, larrVIResp, i);
						break;
					}
				default :
				{
					return null;
				}
			}
		}

		return larrVIResp;
	}

	/**
	 * Cancels all plates that were in the process of being ordered
	 * 
	 * @param aarrVIReq VirtualInvRequest[]
	 * @param aarrVIResp VirtualInvResponse[]
	 * @param aiInd int 
	 * @return VirtualInvResponse[]
	 */
	public VirtualInvResponse[] releaseHold(
		VirtualInvRequest[] aarrVIReq,
		VirtualInvResponse[] aarrVIResp,
		int aiInd)
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(aarrVIReq[aiInd].getItemCode());
		laInvAllocData.setInvItmNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setInvItmEndNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(aarrVIReq[aiInd].isIsaFlag());
		laInvAllocData.setUserPltNo(aarrVIReq[aiInd].isPlpFlag());
		laInvAllocData.setMfgPltNo(
			aarrVIReq[aiInd].getManufacturingPltNoRequest());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		laInvAllocData.setTransEmpId("IAPPL");
		// SET to internet till I change the inv request obj
		laInvAllocData.setRequestorIpAddress(
			aarrVIReq[aiInd].getRequestingIP());
		laInvAllocData.setRequestorRegPltNo("");

		try
		{
			InventoryServerBusiness laInvServBus =
				new InventoryServerBusiness();
			laInvAllocData =
				(InventoryAllocationData) laInvServBus.processData(
					0,
					InventoryConstant.INV_VI_RELEASE_HOLD,
					laInvAllocData);

			if (laInvAllocData.getErrorCode() == 0)
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_SUCCESS);
			}
			else
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
			}

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			setError(aarrVIResp[aiInd], aeRTSEx);
		}

		return aarrVIResp;
	}

	/**
	 * Validates the PLP so the internet user can buy it
	 * 
	 * @param aarrVIReq VirtualInvRequest[]
	 * @param aarrVIResp VirtualInvResponse[]
	 * @param aiInd int 
	 * @return VirtualInvResponse[]
	 */
	public VirtualInvResponse[] validatePLP(
		VirtualInvRequest[] aarrVIReq,
		VirtualInvResponse[] aarrVIResp,
		int aiInd)
	{

		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(aarrVIReq[aiInd].getItemCode());
		laInvAllocData.setInvItmNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setInvItmEndNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(aarrVIReq[aiInd].isIsaFlag());
		laInvAllocData.setUserPltNo(aarrVIReq[aiInd].isPlpFlag());
		laInvAllocData.setMfgPltNo(
			aarrVIReq[aiInd].getManufacturingPltNoRequest());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		//laRTSDate = new RTSDate();
		laInvAllocData.setTransAmDate(caRTSDate.getAMDate());
		// defect 9358 
		// laInvAllocData.setTransTime(caRTSDate.get24HrTime());
		// end defect 9358 
		laInvAllocData.setTransEmpId("IAPPL");
		// SET to internet till I change the inv request obj
		laInvAllocData.setRequestorIpAddress(
			aarrVIReq[aiInd].getRequestingIP());
		laInvAllocData.setRequestorRegPltNo("");

		try
		{
			InventoryServerBusiness laInvServBus =
				new InventoryServerBusiness();
			laInvAllocData =
				(InventoryAllocationData) laInvServBus.processData(
					0,
					InventoryConstant.INV_VI_VALIDATE_PER_PLT,
					laInvAllocData);
			if (laInvAllocData.getErrorCode() == 0)
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_SUCCESS);
			}
			else
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
			}

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			setError(aarrVIResp[aiInd], aeRTSEx);
		}
		return aarrVIResp;
	}

	/**
	 * Validates a PLP to see if it can be purchased
	 * 
	 * @param aarrVIReq VirtualInvRequest[]
	 * @param aarrVIResp VirtualInvResponse[]
	 * @param aiInd int 
	 * @return VirtualInvResponse[]
	 */
	public VirtualInvResponse[] validatePLPNoReserve(
		VirtualInvRequest[] aarrVIReq,
		VirtualInvResponse[] aarrVIResp,
		int aiInd)
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(aarrVIReq[aiInd].getItemCode());
		laInvAllocData.setInvItmNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setInvItmEndNo(aarrVIReq[aiInd].getRegPltNo());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(aarrVIReq[aiInd].isIsaFlag());
		laInvAllocData.setMfgPltNo(
			aarrVIReq[aiInd].getManufacturingPltNoRequest());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		laInvAllocData.setTransAmDate(caRTSDate.getAMDate());
		laInvAllocData.setTransTime(caRTSDate.get24HrTime());
		laInvAllocData.setTransEmpId("IAPPL");
		// SET to internet till I change the inv request obj
		laInvAllocData.setRequestorIpAddress(
			aarrVIReq[aiInd].getRequestingIP());
		laInvAllocData.setUserPltNo(aarrVIReq[aiInd].isPlpFlag());
		laInvAllocData.setRequestorRegPltNo("");

		try
		{
			//VI_ACTION_VALIDATE_PLP_NORESERVE used to return laInvAllocData
			//now it returns true or false - boolean
			InventoryServerBusiness laInvServBus =
				new InventoryServerBusiness();
			Object laPlateAvailable =
				(Object) laInvServBus.processData(
					0,
					InventoryConstant.INV_VI_VALIDATE_PLP_NO_HOLD,
					laInvAllocData);
			//if (laInvAllocData.getErrorCode() == 0)
			if (((Boolean) laPlateAvailable).booleanValue())
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_SUCCESS);
			}
			else
			{
				aarrVIResp[aiInd].setAck(
					ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
			}

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			setError(aarrVIResp[aiInd], aeRTSEx);
		}
		return aarrVIResp;
	}

}
