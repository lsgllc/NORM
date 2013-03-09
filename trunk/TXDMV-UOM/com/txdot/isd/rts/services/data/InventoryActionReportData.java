package com.txdot.isd.rts.services.data;

/*
 * InventoryActionReportData.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * K. Harrell   09/28/2001  Added TransCd for IAR Part A,B, etc.
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Ray Rowehl	09/30/2005	Moved to services.data since this is a data
 * 							class.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	02/12/2007	Remove methods moved down to 
 * 							InventoryAllocationData.
 * 							delete ciTransAMDate, ciTransTime, 
 * 								ciTransWsId, csTransEmpId, 
 * 							delete getTransEmpId(), getTransTime(), 
 * 								getTransWsId(), setTransEmpId(), 
 * 								setTransTime(), setTransWsId()
 * 							modify getTransAMDate(), setTransAMDate()
 * 							defect 9116 Ver Special Plates
 * ---------------------------------------------------------------------
 */
 
/**
 * Data Object to receive data for Inventory Action Report
 *
 * @version	Special Plates	02/12/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		09/24/2001 12:54:17
 */

public class InventoryActionReportData extends InventoryAllocationData
{
	protected int ciSubconId;
	protected int ciVoidOfcIssuanceNo;
	protected int ciVoidTransWsId;
	protected int ciVoidTransAMDate;
	protected int ciVoidTransTime;
	protected String csDelInvReasn;
	protected String csItmCdDesc;
	protected String csTransCd;
	
	/**
	 * Returns the value of DelInvReasn
	 * 
	 * @return String
	 */
	public final String getDelInvReasn()
	{
		return csDelInvReasn;
	}
	
	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return String
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	
	/**
	 * Returns the value of SubconId
	 * 
	 * @return int 
	 */
	public final int getSubconId()
	{
		return ciSubconId;
	}
	
	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return int 
	 */
	public final int getTransAMDate()
	{
		return getTransAmDate();
	}
	
	/**
	 * Returns the value of TransCd
	 * 
	 * @return String
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}
	
	/**
	 * Returns the value of VoidOfcIssuanceNo
	 * 
	 * @return int 
	 */
	public final int getVoidOfcIssuanceNo()
	{
		return ciVoidOfcIssuanceNo;
	}
	
	/**
	 * Returns the value of VoidTransAMDate
	 * 
	 * @return int 
	 */
	public final int getVoidTransAMDate()
	{
		return ciVoidTransAMDate;
	}
	
	/**
	 * Returns the value of VoidTransTime
	 * 
	 * @return int 
	 */
	public final int getVoidTransTime()
	{
		return ciVoidTransTime;
	}
	
	/**
	 * Returns the value of VoidTransWsId
	 * 
	 * @return int 
	 */
	public final int getVoidTransWsId()
	{
		return ciVoidTransWsId;
	}
	
	/**
	 * This method sets the value of DelInvReasn
	 * 
	 * @param asDelInvReasn String
	 */
	public final void setDelInvReasn(String asDelInvReasn)
	{
		csDelInvReasn = asDelInvReasn;
	}
	
	/**
	 * This method sets the value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	
	/**
	 * This method sets the value of SubconId.
	 * 
	 * @param aiSubconId int 
	 */
	public final void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}
	
	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		setTransAmDate(aiTransAMDate);
	}
	
	/**
	 * This method sets the value of TransCd
	 * 
	 * @param asTransCd String
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
	
	/**
	 * This method sets the value of VoidOfcIssuanceNo.
	 * 
	 * @param aiVoidOfcIssuanceNo int 
	 */
	public final void setVoidOfcIssuanceNo(int aiVoidOfcIssuanceNo)
	{
		ciVoidOfcIssuanceNo = aiVoidOfcIssuanceNo;
	}
	
	/**
	 * This method sets the value of VoidTransAMDate.
	 * 
	 * @param aiVoidTransAMDate int 
	 */
	public final void setVoidTransAMDate(int aiVoidTransAMDate)
	{
		ciVoidTransAMDate = aiVoidTransAMDate;
	}
	
	/**
	 * This method sets the value of VoidTransTime.
	 * 
	 * @param aiVoidTransTime int 
	 */
	public final void setVoidTransTime(int aiVoidTransTime)
	{
		ciVoidTransTime = aiVoidTransTime;
	}
	
	/**
	 * This method sets the value of VoidTransWsId.
	 * 
	 * @param aiVoidTransWsId int 
	 */
	public final void setVoidTransWsId(int aiVoidTransWsId)
	{
		ciVoidTransWsId = aiVoidTransWsId;
	}
}
