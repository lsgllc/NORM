package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * InventoryAllocationUIData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang     07/07/2003  Moved Boolean cbCalcInv from 
 *                          InventoryAllocationUIData into 
 *                          InventoryAllocationData ( parent class).
 *                          defect 6076. 
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * Ray Rowehl	02/12/2007	Remove methods moved down to 
 * 							InventoryAllocationData.
 * 							delete ciTransAMDate, ciTransTime
 * 							delete getTransTime(), setTransTime()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/18/2007	Re-clean TransAMDate.
 * 							delete ciTransAMDate
 * 							defect 9116 Ver Special Plates
 * K Harrell	06/18/2007	add convertToInventoryAllocationData()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * InventoryAllocationUIData 
 * 
 * @version	Special Plates	06/18/2007 
 * @author	Marx Rajang
 * <br>Creation Date:		08/23/2001 09:31:09  	
 */

public class InventoryAllocationUIData
	extends InventoryAllocationData
	implements Serializable
{
	// boolean
	private boolean cbAllocatedData = false;

	// int 	
	private int ciDelInvReasnCd;
	private int ciErrorCd;
	private int ciOrigItmModfyIndi;
	private int ciOrigInvQty;
	//private int ciTransAMDate;

	// String 
	private String csDelInvReasnTxt;
	private String csFromInvId;
	private String csFromInvIdName;
	private String csFromInvLocIdCd;
	private String csFromLoc;
	private String csItmCdDesc;
	private String csOrigInvItmNo;
	private String csOrigInvItmEndNo;
	private String csStatusCd;
	private String csStatusDesc;
	private String csToInvIdName;
	private String csToLoc;
	private String csTransId;

	private final static long serialVersionUID = 3343559861334357165L;
	/**
	 * InventoryAllocationUIData constructor comment.
	 */
	public InventoryAllocationUIData()
	{
		super();
	}
	
	/**
	 * InventoryAllocationUIData constructor comment.
	 * 
	 * <p>Create from a base InventoryAllocationData object.
	 */
	public InventoryAllocationUIData(InventoryAllocationData aaIAD)
	{
		this();
		
		// copy the data over
		this.setAllocatedData(false);
		this.setAlreadyIssued(aaIAD.isAlreadyIssued());
		this.setCacheTransAMDate(aaIAD.getCacheTransAMDate());
		this.setCacheTransTime(aaIAD.getCacheTransTime());
		this.setCalcInv(aaIAD.isCalcInv());
		this.setCustSupplied(aaIAD.isCustSupplied());
		this.setDelInvReasnCd(0);
		this.setDelInvReasnTxt("");
		this.setEndPatrnSeqNo(aaIAD.getEndPatrnSeqNo());
		this.setErrorCd(aaIAD.getErrorCode());
		this.setErrorCode(aaIAD.getErrorCode());
		this.setFromInvId("");
		this.setFromInvIdName("");
		this.setFromInvLocIdCd("");
		this.setFromLoc("");
		this.setHoopsRegPltNo(aaIAD.getHoopsRegPltNo());
		this.setInvcDate(aaIAD.getInvcDate());
		this.setInvcNo(aaIAD.getInvcNo());
		this.setInvHoldTimeStmp(aaIAD.getInvHoldTimeStmp());
		this.setInvId(aaIAD.getInvId());
		this.setInvItmEndNo(aaIAD.getInvItmEndNo());
		this.setInvItmNo(aaIAD.getInvItmNo());
		this.setInvItmYr(aaIAD.getInvItmYr());
		this.setInvLocIdCd(aaIAD.getInvLocIdCd());
		this.setInvQty(aaIAD.getInvQty());
		this.setInvStatusCd(aaIAD.getInvStatusCd());
		this.setISA(aaIAD.isISA());
		this.setItmCd(aaIAD.getItmCd());
		this.setItmCdDesc("");
		this.setItrntReq(aaIAD.isItrntReq());
		this.setMfgPltNo(aaIAD.getMfgPltNo());
		this.setNewSubstaId(aaIAD.getNewSubstaId());
		this.setOfcIssuanceNo(aaIAD.getOfcIssuanceNo());
		this.setOrigInvItmEndNo("");
		this.setOrigInvItmNo("");
		this.setOrigInvQty(0);
		this.setOrigItmModfyIndi(0);
		this.setPatrnSeqCd(aaIAD.getPatrnSeqCd());
		this.setPatrnSeqNo(aaIAD.getPatrnSeqNo());
		this.setProcessInvData(aaIAD.cbProcessInvData);
		this.setRangeCd(aaIAD.getRangeCd());
		this.setRequestorIpAddress(aaIAD.getRequestorIpAddress());
		this.setRequestorRegPltNo(aaIAD.getRequestorRegPltNo());
		this.setStatusCd("");
		this.setStatusDesc("");
		this.setSubstaId(aaIAD.getSubstaId());
		this.setToInvIdName("");
		this.setToLoc("");
		this.setTransAmDate(aaIAD.getTransAmDate());
		this.setTransEmpId(aaIAD.getTransEmpId());
		this.setTransTime(aaIAD.getTransTime());
		this.setTransWsId(aaIAD.getTransWsId());
		//this.setTransId("");
		this.setUserPltNo(aaIAD.isUserPltNo());
		this.setViItmCd(aaIAD.getViItmCd());
		this.setVirtual(aaIAD.isVirtual());
		
	}
	
	/**
	 * 
	 * Create new InventoryAllocationData from current object
	 * 
	 * @return InventoryAllocationData
	 */
	public InventoryAllocationData convertToInventoryAllocationData()
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setOfcIssuanceNo(getOfcIssuanceNo());
		laInvAllocData.setSubstaId(getSubstaId());
		laInvAllocData.setNewSubstaId(getNewSubstaId());
		laInvAllocData.setCacheTransAMDate(getCacheTransAMDate());
		laInvAllocData.setCacheTransTime(getCacheTransTime());
		laInvAllocData.setTransAmDate(getTransAmDate());
		laInvAllocData.setTransEmpId(getTransEmpId());
		laInvAllocData.setTransTime(getTransTime());
		laInvAllocData.setItmCd(getItmCd());
		laInvAllocData.setInvItmYr(getInvItmYr());
		laInvAllocData.setInvItmNo(getInvItmNo());
		laInvAllocData.setInvId(getInvId());
		laInvAllocData.setInvLocIdCd(getInvLocIdCd());
		laInvAllocData.setInvItmEndNo(getInvItmEndNo());
		laInvAllocData.setInvQty(getInvQty());
		laInvAllocData.setPatrnSeqNo(getPatrnSeqNo());
		laInvAllocData.setInvStatusCd(getInvStatusCd());
		laInvAllocData.setPatrnSeqCd(getPatrnSeqCd());
		laInvAllocData.setCalcInv(isCalcInv());
		laInvAllocData.setEndPatrnSeqNo(getEndPatrnSeqNo());
		laInvAllocData.setViItmCd(getViItmCd());
		laInvAllocData.setMfgPltNo(getMfgPltNo());
		laInvAllocData.setErrorCode(getErrorCode());
		laInvAllocData.setInvcDate(getInvcDate());
		laInvAllocData.setInvcNo(getInvcNo());
		laInvAllocData.setRangeCd(getRangeCd());
		laInvAllocData.setRequestorIpAddress(getRequestorIpAddress());
		laInvAllocData.setRequestorRegPltNo(getRequestorRegPltNo());
		laInvAllocData.setInvHoldTimeStmp(getInvHoldTimeStmp());
		laInvAllocData.setISA(isISA());
		laInvAllocData.setCustSupplied(isCustSupplied());
		return laInvAllocData;
	}
	/**
	 * Return the value of AllocatedData
	 * 
	 * @return boolean
	 */
	public boolean getAllocatedData()
	{
		return cbAllocatedData;
	}
	/**
	 * Return the value of DelInvReasnCd
	 * 
	 * @return int
	 */
	public int getDelInvReasnCd()
	{
		return ciDelInvReasnCd;
	}
	/**
	 * Return the value of DelInvReasnTxt
	 * 
	 * @return String
	 */
	public String getDelInvReasnTxt()
	{
		return csDelInvReasnTxt;
	}
	/**
	 * Return the value of ErrorCd
	 * 
	 * @return int
	 */
	public int getErrorCd()
	{
		return ciErrorCd;
	}
	/**
	 * Return the value of FromInvId
	 * 
	 * @return String
	 */
	public String getFromInvId()
	{
		return csFromInvId;
	}
	/**
	 * Return the value of FromInvIdName
	 * 
	 * @return String
	 */
	public String getFromInvIdName()
	{
		return csFromInvIdName;
	}
	/**
	 * Return the value of FromInvLocIdCd
	 * 
	 * @return String
	 */
	public String getFromInvLocIdCd()
	{
		return csFromInvLocIdCd;
	}
	/**
	 * Return the value of FromLoc
	 * 
	 * @return String
	 */
	public String getFromLoc()
	{
		return csFromLoc;
	}
	/**
	 * Return the value of ItmCdDesc
	 * 
	 * @return String
	 */
	public String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	/**
	 * Return the value of OrigInvItmEndNo
	 * 
	 * @return String
	 */
	public String getOrigInvItmEndNo()
	{
		return csOrigInvItmEndNo;
	}
	/**
	 * Return the value of OrigInvItmNo
	 * 
	 * @return String
	 */
	public String getOrigInvItmNo()
	{
		return csOrigInvItmNo;
	}
	/**
	 * Return the value of OrigInvQty
	 * 
	 * @return int
	 */
	public int getOrigInvQty()
	{
		return ciOrigInvQty;
	}
	/**
	 * Return the value of OrigItmModfyIndi
	 * 
	 * @return int
	 */
	public int getOrigItmModfyIndi()
	{
		return ciOrigItmModfyIndi;
	}
	/**
	 * Return the value of StatusCd 
	 * 
	 * @return String
	 */
	public String getStatusCd()
	{
		return csStatusCd;
	}
	/**
	 * Return the value of StatusDesc
	 * 
	 * @return String
	 */
	public String getStatusDesc()
	{
		return csStatusDesc;
	}
	/**
	 * Return the value of ToInvIdName
	 * 
	 * @return String
	 */
	public String getToInvIdName()
	{
		return csToInvIdName;
	}
	/**
	 * Return the value of ToLoc 
	 * 
	 * @return String
	 */
	public String getToLoc()
	{
		return csToLoc;
	}
	/**
	 * Return the value of TransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return getTransAmDate();
	}
	/**
	 * Return the value of TransId
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
	}

	/**
	 * Set the  value of AllocatedData
	 * 
	 * @param abAllocatedData boolean
	 */
	public void setAllocatedData(boolean abAllocatedData)
	{
		cbAllocatedData = abAllocatedData;
	}
	/**
	 * Set the  value of DelInvReasnCd
	 * 
	 * @param aiDelInvReasnCd int
	 */
	public void setDelInvReasnCd(int aiDelInvReasnCd)
	{
		ciDelInvReasnCd = aiDelInvReasnCd;
	}
	/**
	 * Set the  value of DelInvReasnTxt
	 * 
	 * @param asDelInvReasnTxt String
	 */
	public void setDelInvReasnTxt(String asDelInvReasnTxt)
	{
		csDelInvReasnTxt = asDelInvReasnTxt;
	}
	/**
	 * Set the  value of ErrorCd
	 * 
	 * @param aiErrorCd int
	 */
	public void setErrorCd(int aiErrorCd)
	{
		ciErrorCd = aiErrorCd;
	}
	/**
	 * Set the  value of FromInvId
	 * 
	 * @param asFromInvId String
	 */
	public void setFromInvId(String asFromInvId)
	{
		csFromInvId = asFromInvId;
	}
	/**
	 * Set the  value of FromInvIdName
	 * 
	 * @param asFromInvIdName String
	 */
	public void setFromInvIdName(String asFromInvIdName)
	{
		csFromInvIdName = asFromInvIdName;
	}
	/**
	 * Set the  value of FromInvLocIdCd
	 * 
	 * @param asFromInvLocIdCd String
	 */
	public void setFromInvLocIdCd(String asFromInvLocIdCd)
	{
		csFromInvLocIdCd = asFromInvLocIdCd;
	}
	/**
	 * Set the  value of FromLoc
	 * 
	 * @param asFromLoc String
	 */
	public void setFromLoc(String asFromLoc)
	{
		csFromLoc = asFromLoc;
	}
	/**
	 * Set the  value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	/**
	 * Set the  value of OrigInvItmEndNo
	 * 
	 * @param asOrigInvItmEndNo String
	 */
	public void setOrigInvItmEndNo(String asOrigInvItmEndNo)
	{
		csOrigInvItmEndNo = asOrigInvItmEndNo;
	}
	/**
	 * Set the  value of OrigInvItmNo
	 * 
	 * @param asOrigInvItmNo String
	 */
	public void setOrigInvItmNo(String asOrigInvItmNo)
	{
		csOrigInvItmNo = asOrigInvItmNo;
	}
	/**
	 * Set the  value of OrigInvQty
	 * 
	 * @param aiOrigInvQty int
	 */
	public void setOrigInvQty(int aiOrigInvQty)
	{
		ciOrigInvQty = aiOrigInvQty;
	}
	/**
	 * Set the  value of OrigItmModfyIndi
	 * 
	 * @param aiOrigItmModfyIndi int
	 */
	public void setOrigItmModfyIndi(int aiOrigItmModfyIndi)
	{
		ciOrigItmModfyIndi = aiOrigItmModfyIndi;
	}
	/**
	 * Set the  value of StatusCd
	 * 
	 * @param asStatusCd String
	 */
	public void setStatusCd(String asStatusCd)
	{
		csStatusCd = asStatusCd;
	}
	/**
	 * Set the  value of StatusDesc 
	 * 
	 * @param asStatusDesc String
	 */
	public void setStatusDesc(String asStatusDesc)
	{
		csStatusDesc = asStatusDesc;
	}
	/**
	 * Set the  value of ToInvIdName
	 * 
	 * @param asToInvIdName String
	 */
	public void setToInvIdName(String asToInvIdName)
	{
		csToInvIdName = asToInvIdName;
	}
	/**
	 * Set the value of  csToLoc
	 * 
	 * @param asToLoc String
	 */
	public void setToLoc(String asToLoc)
	{
		csToLoc = asToLoc;
	}
	/**
	 * Set the value of TransAMDate 
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		setTransAmDate(aiTransAMDate);
	}
	/**
	 * Set the value of TransId 
	 * 
	 * @param asTransId String
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}
}
