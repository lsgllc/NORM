package com.txdot.isd.rts.webservices.inv.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * RtsInvRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/01/2008	Created Class
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/25/2008	Modify data handling to now use 
 * 							SystemProperty to set the office and wsid. 
 * 							modify BuildIAD()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/12/2008	Add a validate methos to ensure some basic 
 * 							editting is done.
 * 							add validateInvRequest()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	10/19/2009	Vendor Plates can now issue from patterns.
 * 							Allow non-PLP to pass validation.
 * 							modify validateInvRequest()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	10/20/2009	Set the UserPltNo boolean to true always.
 * 							We will have to review this in another pass. 
 * 							modify BuildIAD()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	10/21/2009	Revert the UserPltNo boolean.
 * 							modify BuildIAD()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	10/22/2009	Use Constants for edit check errors.
 * 							modify validateInvRequest()
 * 							defect 10253 Ver Defect_POS_G
 * Ray Rowehl	03/09/2010	Allow zero length on the plate fields for 
 * 							getNext function.
 * 							modify validateInvRequest()
 * 							defect 10400 Ver 6.4.0
 * Ray Rowehl	05/05/2010	Limit field lengths to ensure db2 can insert.
 * 							modify validateInvRequest
 * 							defect 10400 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * Request data for Inventory Service.
 *
 * @version	6.4.0			05/05/2010
 * @author	Min Wang
 * <br>Creation Date:		06/01/2008 12:30:00
 */

public class RtsInvRequest extends RtsAbstractRequest
{
	private boolean cbFromReserveFlag;
	private boolean cbIsaFlg;
	private boolean cbPlpFlag;
	private int ciItmYr;
	private int ciRequestingOfcIssuanceNo;
	private String csItmCd;
	private String csItmNo;
	private String csManufacturingPltNo;
	private String csRegPltNo;

	/**
	 * Build the InventoryAllocationData for working with Inventory.
	 * 
	 * @return InventoryAllocationData
	 */
	public InventoryAllocationData BuildIAD()
	{
		InventoryAllocationData laIAD = new InventoryAllocationData();

		laIAD.setItmCd(csItmCd);
		laIAD.setInvItmNo(csItmNo);
		laIAD.setInvItmEndNo(csItmNo);
		laIAD.setInvQty(1);
		laIAD.setInvItmYr(ciItmYr);
		laIAD.setFromReserve(cbFromReserveFlag);
		laIAD.setISA(cbIsaFlg);
		// defect 10253
		// TODO we may have to have them pass this indicator.  Review.
		// This flag is used to determine if this is a PLP type request 
		// or a patterned request.
		laIAD.setUserPltNo(cbPlpFlag);
		// end defect 10253
		laIAD.setMfgPltNo(csManufacturingPltNo);
		laIAD.setItrntReq(true);
		laIAD.setOfcIssuanceNo(SystemProperty.getVpOfcIssuanceNo());
		laIAD.setTransWsId(SystemProperty.getVpWsId());
		if (getCaller() != null && getCaller().length() > 7)
		{
			laIAD.setTransEmpId(getCaller().substring(0, 7));
		}
		else
		{
			laIAD.setTransEmpId(getCaller());
		}

		String lsRIP = getSessionId();
		if (lsRIP.length() > 15)
		{
			lsRIP = lsRIP.substring(lsRIP.length() - 15);
		}
		laIAD.setRequestorIpAddress(lsRIP);
		//laIAD.setRequestorIpAddress(getSessionId());

		laIAD.setRequestorRegPltNo(CommonConstant.STR_SPACE_EMPTY);
		laIAD.setTransAmDate(new RTSDate().getAMDate());

		try
		{
			com
				.txdot
				.isd
				.rts
				.services
				.common
				.Transaction laTransactionEngine =
				new com.txdot.isd.rts.services.common.Transaction(
					TransCdConstant.IAPPL);
			laIAD.setTransTime(laTransactionEngine.getTransTime());
		}
		catch (RTSException aeRTSEx)
		{
			// just use now!
			laIAD.setTransTime(new RTSDate().get24HrTime());
		}

		return laIAD;
	}

	/**
	 * Return the value of ItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * Return the value of ItmNo
	 * 
	 * @return String
	 */
	public String getItmNo()
	{
		return csItmNo;
	}

	/**
	 * Return the value of ItmYr
	 * 
	 * @return int
	 */
	public int getItmYr()
	{
		return ciItmYr;
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
	 * Return the value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	/**
	 * Return the value of RequestingOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getRequestingOfcIssuanceNo()
	{
		return ciRequestingOfcIssuanceNo;
	}
	/**
	 * Return the value of FromReserveFlag
	 * 
	 * @return boolean
	 */
	public boolean isFromReserveFlag()
	{
		return cbFromReserveFlag;
	}
	/**
	 * Return the value of IsaFlg
	 * 
	 * @return boolean
	 */
	public boolean isIsaFlg()
	{
		return cbIsaFlg;
	}
	/**
	 * Return the value of PlpFlag
	 * 
	 * @return boolean
	 */
	public boolean isPlpFlag()
	{
		return cbPlpFlag;
	}
	/**
	 * Set the value of FromReserveFlag
	 * 
	 * @param abFromReserveFlag boolean
	 */
	public void setFromReserveFlag(boolean abFromReserveFlag)
	{
		cbFromReserveFlag = abFromReserveFlag;
	}
	/**
	 * Set the value of IsaFlg
	 * 
	 * @param abIsaFlg boolean
	 */
	public void setIsaFlg(boolean abIsaFlg)
	{
		cbIsaFlg = abIsaFlg;
	}
	/**
	 * Set the value of ItmCd
	 * 
	 * @param asItmCd String
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * Set the value of ItmNo
	 * 
	 * @param asItmNo String
	 */
	public void setItmNo(String asItmNo)
	{
		csItmNo = asItmNo;
	}
	/**
	 * Set the value of ItmYr
	 * 
	 * @param aiItmYr int
	 */
	public void setItmYr(int aiItmYr)
	{
		ciItmYr = aiItmYr;
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
	 * Set the value of IsaFlg
	 * 
	 * @param abIsaFlg boolean
	 */
	public void setPlpFlag(boolean abPlpFlag)
	{
		cbPlpFlag = abPlpFlag;
	}
	/**
	 * Set the value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}
	/**
	 * Set the value of RequestingOfcIssuanceNo
	 * 
	 * @param aiRequestingOfcIssuanceNo int
	 */
	public void setRequestingOfcIssuanceNo(int aiRequestingOfcIssuanceNo)
	{
		ciRequestingOfcIssuanceNo = aiRequestingOfcIssuanceNo;
	}

	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateInvRequest() throws RTSException
	{
		// defect 10400
		// create an RTSException to throw if needed at the end.
		RTSException leRTSEx = null;
		// end defect 10400
		if (getCaller() == null || getCaller().length() < 1)
		{
			// Use constant for error number
			// defect 10400
			//throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			// end defect 10400
		}

		// Make sure there is a SessionId
		if (getSessionId() == null || getSessionId().length() < 1)
		{
			// Use constant for error number
			// defect 10400
			//throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			// end defect 10400
		}

		if (csItmCd == null || csItmCd.length() < 1)
		{
			// Use constant for error number
			// defect 10400
			//throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			// end defect 10400
		}
		else if (csItmCd.length() > 8)
		{
			csItmCd = csItmCd.substring(0, 8);
			// defect 10400
			//throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			// end defect 10400
		}

		
		// Use constant for plate number length and error number
		// defect 10400
		// If PLP, there must be a length
		if (csItmNo == null
			|| (csItmNo.length() < 1 && cbPlpFlag)
			|| csItmNo.length() > CommonConstant.LENGTH_PLTNO)
		{
			if (csItmNo.length() > CommonConstant.LENGTH_PLTNO)
			{
				csItmNo = csItmNo.substring(0, 
					CommonConstant.LENGTH_PLTNO);
			}
		
			//throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			// end defect 10400
		}

		// Use constant for plate number length and error number
		// defect 10400
		// If PLP, there must be a length.
		if (csManufacturingPltNo == null
			|| (csManufacturingPltNo.length() < 1 && cbPlpFlag)
			|| csManufacturingPltNo.length() > 
				CommonConstant.LENGTH_MFG_PLTNO_MAX)
		{
			if (csManufacturingPltNo.length() > 
				CommonConstant.LENGTH_MFG_PLTNO_MAX)
			{
				csManufacturingPltNo = csManufacturingPltNo.substring(0, 
					CommonConstant.LENGTH_MFG_PLTNO_MAX);
			}
			
			//throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			// end defect 10400
		}
		
		// defect 10400
		if (leRTSEx != null)
		{
			throw leRTSEx;
		}
		// end defect 10400
	}
}
