package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * InventoryAllocationData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/14/2001  Added ciEndPatrnSeqNo
 * K Harrell	01/15/2002  Added NewSubstaId
 * Min Wang		07/07/2003  Added CalcInv boolean so processInvData 
 * 							can see.
 *                          defect 6076
 * K Harrell	04/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * Min Wang		05/13/2005  use an "is" getter when type of data 
 * 							is boolean.
 * 							add isCalcInv()
 * 							delete getCalcInv()
 * 							defect 6370 Ver 5.2.3
 * Ray Rowehl	02/12/2007	Add booleans for handling Virtual Inventory
 * 							needs.
 * 							add caInvHoldTimeStmp, cbItrntReq, cbISA, 
 * 								cbPersonalized, cbVirtual, ciInvcDate, 
 * 								ciTransAmDate, ciTransTime, ciTransWsId, 
 * 								csInvNo, csMfgPltNo, csTransEmpId
 * 							add getInvcDate(), getInvcNo(), 
 * 								getInvHoldTimeStmp(), getMfgPltNo(), 
 * 								getTransAmDate(), getTransEmpId(), 
 * 								getTransTime(), getTransWsId(),  
 * 								isCalcInv(), isInetReq(), isPersonalized(), 
 * 								isVirtual(), setInetReq(), setInvcDate(), 
 * 								setInvcNo(), setInvHoldTimeStmp(), 
 * 								setISA(), setMfgPltNo(), 
 * 								setPersonalized(), setTransAmDate(), 
 * 								setTransEmpId(), setTransTime(),  
 * 								setTransWsId(), setVirtual()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/13/2007	Move RangeCd to InventoryAllocationData 
 * 							as part of creating Virtual Inventory.
 * 							add csRangeCd
 * 							add getRangeCd(), setRangeCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/20/2007	Move Already Issued boolean.
 * 							add cbAlreadyIssued, cbProcessInvData, 
 * 								ciErrorCode, csViItmCd
 * 							add getErrorCode(), getRequestorIpAddress(),
 * 								getRequestorRegPltNo(), getViItmCd(), 
 * 								isAlreadyIssued(), setAlreadyIssued(), 
 * 								setErrorCode(), setProcessInvData(), 
 * 								setRequestorIpAddress(), 
 * 								setRequestorRegPltNo(), setViItmCd(), 
 * 								wasProcessInvData()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/23/2007	Add indicator that determines if the plate
 * 							number was provided by the customer or 
 * 							generated from the system.
 * 							This field is controlled by the Inventory 
 * 							module.
 * 							add cbCustSupplied
 * 							defect 9116 Ver Special Plates
 * K Harrell	06/18/2007	add ciCacheTransTime,ciCacheTransAMDATE, 
 * 							get/set methods. 
 * 							defect 9085 Ver Special Plates 
 * Ray Rowehl	06/21/2007	Add support for new csHoopsRegPltNo field.
 * 							This field stores the item number in 
 * 							Hoops form.
 * 							add csHoopsRegPltNo
 * 							add getHoopsRegPltNo(), setHoopsRegPltNo()
 * 							defect 9116 Ver Special Plates 
 * Ray Rowehl	06/11/2008	Add temp flag so we can determine if the 
 * 							request is only temporary or permanent.
 * 							Used for validatePLP.
 * 							add cbTempChange
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/18/2008	Add fromReserve boolean to detect when 
 * 							the item is fromReserve.
 * 							add cbFromReserve
 * 							add isFromReserve(), setFromReserve()
 * 							defect 9679 Ver MyPlates_POS 
 * K Harrell	06/26/2010	add getTransId()
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	03/17/2011	add cbVerifyInvOwner, is/set method
 * 							defect 10769 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * InventoryAllocationData 
 * 
 * @version	6.7.1 			03/17/2011
 * @author	Marx Rajang
 * <br>Creation Date:		09/12/2001 	
 */

public class InventoryAllocationData
	implements Displayable, Serializable
{

	private final static long serialVersionUID = -4771798035254976783L;

	protected RTSDate caInvHoldTimeStmp;

	// boolean 
	protected boolean cbAlreadyIssued = false;
	protected boolean cbCalcInv = false;

	/**
	 * Indicates if the Customer provided the plate number.
	 * 
	 * <p>If false, this indicates the plate number was generated 
	 * by the system.
	 * 
	 * <p>This value is set by the Inventory module.
	 */
	protected boolean cbCustSupplied = false;

	// defect 9679
	protected boolean cbFromReserve = false;
	// end defect 9679

	protected boolean cbISA = false;
	protected boolean cbItrntReq = false;
	// indicates if the original form was in ProcessInventoryData.
	protected boolean cbProcessInvData = false;
	

	// defect 9679
	/**
	 * Indicates that there should be no change to state of data.
	 * This is only a temporary request.
	 */
	protected boolean cbTempChange = false;
	// end defect 9679

	protected boolean cbUserPltNo = false;
	protected boolean cbVirtual = false;
	
	// defect 10769 
	protected boolean cbVerifyInvOwner = false;
	// end defect 10769  

	// int 
	private int ciCacheTransAMDate;
	private int ciCacheTransTime;
	protected int ciEndPatrnSeqNo;
	protected int ciErrorCode;
	protected int ciInvcDate;
	protected int ciInvItmYr;
	protected int ciInvQty;
	protected int ciInvStatusCd;
	protected int ciNewSubstaId;
	protected int ciOfcIssuanceNo;
	protected int ciPatrnSeqCd;
	protected int ciPatrnSeqNo;
	protected int ciSubstaId;
	protected int ciTransAmDate;
	protected int ciTransTime;
	protected int ciTransWsId;
	protected String csHoopsRegPltNo;

	// String 
	protected String csInvcNo;
	protected String csInvId;
	protected String csInvItmEndNo;
	protected String csInvItmNo;
	protected String csInvLocIdCd;
	protected String csItmCd;
	protected String csMfgPltNo;
	protected String csRangeCd;
	protected String csRequestorIpAddress;
	protected String csRequestorRegPltNo;
	protected String csTransEmpId;
	protected String csViItmCd;

	/**
	 * Get Object field attributes
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		java.util.HashMap lhmHash = new java.util.HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}
	/**
	 * Return value of ciCacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Set value of CacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Returns the value of EndPatrnSeqNo
	 * 
	 * @return  int 
	 */
	public final int getEndPatrnSeqNo()
	{
		return ciEndPatrnSeqNo;
	}

	/**
	 * Return the error code.
	 * 
	 * @return int
	 */
	public final int getErrorCode()
	{
		return ciErrorCode;
	}

	/**
	 * Returns the Hoops Reg Plate Number form of the Item.
	 * 
	 * @return String
	 */
	public final String getHoopsRegPltNo()
	{
		return csHoopsRegPltNo;
	}

	/**
	 * Returns the date of the Invoice used to receive this item.
	 * 
	 * @return int
	 */
	public final int getInvcDate()
	{
		return ciInvcDate;
	}

	/**
	 * Returns the Invoice Number used to add this item.
	 * 
	 * @return String InvcNo
	 */
	public final String getInvcNo()
	{
		return csInvcNo;
	}

	/**
	 * Return the Hold TimeStamp.  This is used for timing out the hold.
	 * 
	 * @return RTSDate
	 */
	public final RTSDate getInvHoldTimeStmp()
	{
		return caInvHoldTimeStmp;
	}

	/**
	 * Returns the value of InvId
	 * 
	 * @return  String 
	 */
	public final String getInvId()
	{
		return csInvId;
	}

	/**
	 * Returns the value of InvItmEndNo
	 * 
	 * @return  String 
	 */
	public final String getInvItmEndNo()
	{
		return csInvItmEndNo;
	}

	/**
	 * Returns the value of InvItmNo
	 * 
	 * @return  String 
	 */
	public final String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Returns the value of InvItmYr
	 * 
	 * @return  int 
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}

	/**
	 * Returns the value of InvLocIdCd
	 * 
	 * @return  String 
	 */
	public final String getInvLocIdCd()
	{
		return csInvLocIdCd;
	}

	/**
	 * Returns the value of InvQty
	 * 
	 * @return  int 
	 */
	public final int getInvQty()
	{
		return ciInvQty;
	}

	/**
	 * Returns the value of InvStatusCd
	 * 
	 * @return  int 
	 */
	public final int getInvStatusCd()
	{
		return ciInvStatusCd;
	}

	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}

	/**
	 * Returns the Manufacturing Plate Number.
	 * 
	 * <p>This only means something if it is personalized.
	 * 
	 * @return String
	 */
	public final String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Returns the value of NewSubstaId
	 * 
	 * @return int
	 */
	public final int getNewSubstaId()
	{
		return ciNewSubstaId;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of PatrnSeqCd
	 * 
	 * @return  int 
	 */
	public final int getPatrnSeqCd()
	{
		return ciPatrnSeqCd;
	}

	/**
	 * Returns the value of PatrnSeqNo
	 * 
	 * @return  int 
	 */
	public final int getPatrnSeqNo()
	{
		return ciPatrnSeqNo;
	}

	/**
	 * Returns the value of RangeCd
	 * 
	 * @return  String
	 */
	public final String getRangeCd()
	{
		return csRangeCd;
	}

	/**
	 * Return the Requestors tcp/ip address.
	 * This is mainly for Internet requests.
	 * POS can also populate this field with control point.
	 * 
	 * @return String
	 */
	public final String getRequestorIpAddress()
	{
		return csRequestorIpAddress;
	}

	/**
	 * Get the Requestor's RegPltNo.
	 * Used for Special Plate Applications.
	 * 
	 * @return String
	 */
	public final String getRequestorRegPltNo()
	{
		return csRequestorRegPltNo;
	}

	/**
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns the TransAmDate of when this row was updated.
	 * 
	 * @return int
	 */
	public final int getTransAmDate()
	{
		return ciTransAmDate;
	}

	/**
	 * Returns the Transaction Employee Id last updating the record.
	 * 
	 * @return String 
	 */
	public final String getTransEmpId()
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
		String lsTransId = new String();

		if (ciTransTime != 0
			&& ((!isItrntReq()
				&& ciInvStatusCd != InventoryConstant.HOLD_INV_NOT)
				|| (isItrntReq()
					&& ciInvStatusCd
						== InventoryConstant.HOLD_INV_ORDER_COMPLETE)))
		{
			lsTransId =
				UtilityMethods.getTransId(
					ciOfcIssuanceNo,
					ciTransWsId,
					ciTransAmDate,
					ciTransTime);
		}
		return lsTransId;
	}

	/**
	 * Return the Transaction Time from the Order Confirmation.
	 * 
	 * @return int
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the Transaction Workstion Id last working with this row.
	 * 
	 * @return int
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Return the VI Item Code.
	 * 
	 * @return String
	 */
	public final String getViItmCd()
	{
		return csViItmCd;
	}

	/**
	 * Returns the value of AlreadyIssued
	 * 
	 * @return boolean
	 */
	public final boolean isAlreadyIssued()
	{
		return cbAlreadyIssued;
	}

	/**
	 * Returns value of CalcInv boolean.
	 * 
	 * @return boolean
	 */
	public final boolean isCalcInv()
	{
		return cbCalcInv;
	}

	/**
	 * Indicates if the Plate Number is provided by the Customer.
	 * 
	 * @return boolean
	 */
	public final boolean isCustSupplied()
	{
		return cbCustSupplied;
	}

	/**
	 * Indicates if source will be Reserve.
	 * 
	 * @return boolean
	 */
	public boolean isFromReserve()
	{
		return cbFromReserve;
	}

	/**
	 * Boolean indicating if this item has an ISA.
	 * 
	 * @return boolean
	 */
	public final boolean isISA()
	{
		return cbISA;
	}

	/**
	 * Returns boolean indicating that the transaction was done outside
	 * of POS.
	 * 
	 * @return boolean
	 */
	public final boolean isItrntReq()
	{
		return cbItrntReq;
	}

	/**
	 * Returns indication if the change is Temporary or Permanent.
	 * 
	 * @return boolean
	 */
	public boolean isTempChange()
	{
		return cbTempChange;
	}

	/**
	 * Boolean that indicates if this is Personalized.
	 * 
	 * @return boolean
	 */
	public final boolean isUserPltNo()
	{
		return cbUserPltNo;
	}
	
	/**
	 * Return value of cbVerifyInvOwner
	 * 
	 * @return boolean 
	 */
	public boolean isVerifyInvOwner()
	{
		return cbVerifyInvOwner;
	}

	/**
	 * Boolean that indicates this is a Virtual Item.
	 * 
	 * @return boolean
	 */
	public final boolean isVirtual()
	{
		return cbVirtual;
	}

	/**
	 * Set the value of AlreadyIssued
	 *  
	 * @param abAlreadyIssued boolean
	 */
	public final void setAlreadyIssued(boolean abAlreadyIssued)
	{
		cbAlreadyIssued = abAlreadyIssued;
	}

	/**
	 * Set value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Set value of CacheTransTime
	 * 
	 * @param aiCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * Sets the value of CalcInv
	 * 
	 * @param abCalcInv int
	 */
	public final void setCalcInv(boolean abCalcInv)
	{
		cbCalcInv = abCalcInv;
	}

	/**
	 * Set the Customer Supplied Indicator.
	 * 
	 * @param abCustSupplied boolean
	 */
	public final void setCustSupplied(boolean abCustSupplied)
	{
		cbCustSupplied = abCustSupplied;
	}

	/**
	* This method sets the value of EndPatrnSeqNo.
	* 
	* @param aiEndPatrnSeqNo   int 
	*/
	public final void setEndPatrnSeqNo(int aiEndPatrnSeqNo)
	{
		ciEndPatrnSeqNo = aiEndPatrnSeqNo;
	}

	/**
	 * Set the Error Code
	 * 
	 * @param aiNewErrorCode int
	 */
	public final void setErrorCode(int aiNewErrorCode)
	{
		ciErrorCode = aiNewErrorCode;
	}

	/**
	 * Sets the Indicator that the source will be Reserve.
	 * 
	 * @param abFromReserve
	 */
	public void setFromReserve(boolean abFromReserve)
	{
		cbFromReserve = abFromReserve;
	}

	/**
	 * Sets the Hoops Reg Plate Number form of the Item.
	 * 
	 * @param asHoopsRegPltNo String
	 */
	public final void setHoopsRegPltNo(String asHoopsRegPltNo)
	{
		csHoopsRegPltNo = asHoopsRegPltNo;
	}

	/**
	 * Set the Invoice Date for this item.
	 * 
	 * @param int aiNewInvcDate
	 */
	public final void setInvcDate(int aiNewInvcDate)
	{
		ciInvcDate = aiNewInvcDate;
	}

	/**
	 * Set the Invoice Number used for this item.
	 * 
	 * @param String
	 */
	public final void setInvcNo(String asNewInvcNo)
	{
		csInvcNo = asNewInvcNo;
	}

	/**
	 * Set the Inventory Hold TimeStamp.
	 * 
	 * @param RTSDate aoInvHoldTimeStmp
	 */
	public final void setInvHoldTimeStmp(RTSDate aoInvHoldTimeStmp)
	{
		caInvHoldTimeStmp = aoInvHoldTimeStmp;
	}

	/**
	 * This method sets the value of InvId.
	 * 
	 * @param asInvId   String 
	 */
	public final void setInvId(String asInvId)
	{
		csInvId = asInvId;
	}

	/**
	 * This method sets the value of InvItmEndNo.
	 * 
	 * @param asInvItmEndNo   String 
	 */
	public final void setInvItmEndNo(String asInvItmEndNo)
	{
		csInvItmEndNo = asInvItmEndNo;
	}

	/**
	 * This method sets the value of InvItmNo.
	 * 
	 * @param asInvItmNo   String 
	 */
	public final void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}

	/**
	 * This method sets the value of InvItmYr.
	 * 
	 * @param aiInvItmYr   int 
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}

	/**
	 * This method sets the value of InvLocIdCd.
	 * 
	 * @param asInvLocIdCd   String 
	 */
	public final void setInvLocIdCd(String asInvLocIdCd)
	{
		csInvLocIdCd = asInvLocIdCd;
	}

	/**
	 * This method sets the value of InvQty.
	 * 
	 * @param aiInvQty   int 
	 */
	public final void setInvQty(int aiInvQty)
	{
		ciInvQty = aiInvQty;
	}

	/**
	 * This method sets the value of InvStatusCd.
	 * 
	 * @param aiInvStatusCd   int 
	 */
	public final void setInvStatusCd(int aiInvStatusCd)
	{
		ciInvStatusCd = aiInvStatusCd;
	}

	/**
	 * Set the ISA boolean.
	 * 
	 * @param boolean abNewISA
	 */
	public final void setISA(boolean abNewISA)
	{
		cbISA = abNewISA;
	}

	/**
	 * This method sets the value of Item Code.
	 * 
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}

	/**
	 * Sets the Inet Request Indicator.
	 * 
	 * @param boolean
	 */
	public final void setItrntReq(boolean abNewInetReq)
	{
		cbItrntReq = abNewInetReq;
	}

	/**
	 * Set the Manufacturing Plate Number.
	 * 
	 * @param String asMfgPltNo
	 */
	public final void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Sets the value of NewSubstaId
	 * 
	 * @param aiNewSubstaId int
	 */
	public final void setNewSubstaId(int aiNewSubstaId)
	{
		ciNewSubstaId = aiNewSubstaId;
	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of PatrnSeqCd.
	 * 
	 * @param aiPatrnSeqCd   int 
	 */
	public final void setPatrnSeqCd(int aiPatrnSeqCd)
	{
		ciPatrnSeqCd = aiPatrnSeqCd;
	}

	/**
	 * This method sets the value of PatrnSeqNo.
	 * 
	 * @param aiPatrnSeqNo   int 
	 */
	public final void setPatrnSeqNo(int aiPatrnSeqNo)
	{
		ciPatrnSeqNo = aiPatrnSeqNo;
	}

	/**
	 * Set the boolean indicating the original form of this data was
	 * ProcessInventoryData.
	 * 
	 * @param abNewPID boolean
	 */
	public final void setProcessInvData(boolean abNewPID)
	{
		cbProcessInvData = abNewPID;
	}

	/**
	 * This method sets the value of RangeCd
	 * @param asRangeCd   String
	 */
	public final void setRangeCd(String asRangeCd)
	{
		csRangeCd = asRangeCd;
	}

	/**
	 * Sets the Requestor tcp/ip address.
	 * 
	 * <p>Note that the storage of this field has a limited length.
	 * 
	 * @param asNewIpAddress String
	 */
	public final void setRequestorIpAddress(String asNewIpAddress)
	{
		csRequestorIpAddress = asNewIpAddress;
	}

	/**
	 * Set the Requestor's RegPltNo.
	 * 
	 * @param asNewRegPltNo String
	 */
	public final void setRequestorRegPltNo(String asNewRegPltNo)
	{
		csRequestorRegPltNo = asNewRegPltNo;
	}

	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Set the Temp Change flag.
	 * 
	 * @param abTempChange
	 */
	public void setTempChange(boolean abTempChange)
	{
		cbTempChange = abTempChange;
	}

	/**
	 * Set the TransAmDate.
	 * 
	 * @param int aiTransAmDate
	 */
	public final void setTransAmDate(int aiTransAmDate)
	{
		ciTransAmDate = aiTransAmDate;
	}

	/**
	 * Set the Transaction Employee Id for last update.
	 * 
	 * @param String asTransEmpId
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Set the Transaction Time for the Order Confirmation.
	 * 
	 * @param int aiTransTime
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Sets the Transaction Workstion Id.
	 * 
	 * @param int aiTransWsId
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set the User Plate Number boolean
	 * 
	 * @param boolean abNewUserPltNo
	 */
	public final void setUserPltNo(boolean abNewUserPltNo)
	{
		cbUserPltNo = abNewUserPltNo;
	}

	/**
	 * Set value of cbVerifyInvOwner
	 * 
	 * @param abVerifyInvOwner
	 */
	public void setVerifyInvOwner(boolean abVerifyInvOwner)
	{
		cbVerifyInvOwner = abVerifyInvOwner;
	}

	/**
	 * Set the VI Item Code.
	 * 
	 * @param asViItmCd String
	 */
	public final void setViItmCd(String asViItmCd)
	{
		csViItmCd = asViItmCd;
	}

	/**
	 * Set the Virtual Inventory boolean.
	 * 
	 * @param boolean abNewVirtual
	 */
	public final void setVirtual(boolean abNewVirtual)
	{
		cbVirtual = abNewVirtual;
	}

	/**
	 * Returns boolean indicating that the original form of this 
	 * data was ProcessInventoryData.
	 * 
	 * @return boolean
	 */
	public final boolean wasProcessInvData()
	{
		return cbProcessInvData;
	}



}
