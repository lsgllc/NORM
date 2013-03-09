package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * InventoryProfileData.java  
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	11/11/2001  Added csTransEmpId, csTransWsId
 * C Walker		11/26/2001  Added setTransEmpId, setTransWsId
 * K Harrell	12/11/2001  Removed BranchOfcId
 * M Wang		10/29/2002  Modified compareTo() to handle 
 * 							NumberFormatException. 
 * 							defect 4965
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * InventoryProfileData
 * 
 * @version	5.2.3		05/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	09/17/2001 09:31:09    
 */

public class InventoryProfileData implements Serializable, Comparable
{
	// int
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciInvItmYr;
	protected int ciMaxQty;
	protected int ciMinQty;
	protected int ciNextAvailIndi;
	protected int ciDeleteIndi;

	// Object 
	protected RTSDate caChngTimestmp;

	// String 
	protected String csEntity;
	protected String csId;
	protected String csItmCd;
	protected String csItmCdDesc;
	protected String csTransEmpId;
	protected String csTransWsId;

	private final static long serialVersionUID = -5870177746608415565L;
	/**
	 * Sorts the Data Object by Id 
	 *   (Primarily used to sort the table on INV030)
	 * 
	 * @param aaObject Object 
	 */
	public int compareTo(Object aaObject)
	{

		InventoryProfileData laInvProfileData =
			(InventoryProfileData) aaObject;

		if (csEntity.equals(laInvProfileData.getEntity()))
		{
			if (!csEntity.equals(InventoryConstant.CHAR_E))
			{
				// defect 4965
				try
				{
					Integer laCurrentInt = Integer.valueOf(csId);
					Integer laCompareToInt =
						Integer.valueOf(laInvProfileData.getId());

					return laCurrentInt.compareTo(laCompareToInt);
				}
				catch (NumberFormatException aeNFEx)
				{
					return 0;
				} // end defect 4965
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}
	/**
	 * Returns the value of ChngTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Returns the value of DeleteIndi
	 * 
	 * @return  int 
	 */
	public final int getDeleteIndi()
	{
		return ciDeleteIndi;
	}
	/**
	 * Returns the value of Entity
	 * 
	 * @return  String 
	 */
	public final String getEntity()
	{
		return csEntity;
	}
	/**
	* Returns the value of Id
	* 
	* @return  String 
	*/
	public final String getId()
	{
		return csId;
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
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * Return value of ItmCdDesc
	 * 
	 * @return String
	 */
	public String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	/**
	 * Returns the value of MaxQty
	 * 
	 * @return  int 
	 */
	public final int getMaxQty()
	{
		return ciMaxQty;
	}
	/**
	 * Returns the value of MinQty
	 * 
	 * @return  int 
	 */
	public final int getMinQty()
	{
		return ciMinQty;
	}
	/**
	 * Returns the value of NextAvailIndi
	 * 
	 * @return  int 
	 */
	public final int getNextAvailIndi()
	{
		return ciNextAvailIndi;
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
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Return value of TransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Return value of TransWsId
	 * 
	 * @return String
	 */
	public String getTransWsId()
	{
		return csTransWsId;
	}
	/**
	 * This method sets the value of ChngTimestmp.
	 * 
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * This method sets the value of DeleteIndi.
	 * 
	 * @param aiDeleteIndi   int 
	 */
	public final void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}
	/**
	 * This method sets the value of Entity.
	 * 
	 * @param asEntity   String 
	 */
	public final void setEntity(String asEntity)
	{
		csEntity = asEntity;
	}
	/**
	 * This method sets the value of Id.
	 * 
	 * @param asId   String 
	 */
	public final void setId(String asId)
	{
		csId = asId;
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
	 * This method sets the value of ItmCd.
	 * 
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * Set value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	/**
	 * This method sets the value of MaxQty.
	 * 
	 * @param aiMaxQty   int 
	 */
	public final void setMaxQty(int aiMaxQty)
	{
		ciMaxQty = aiMaxQty;
	}
	/**
	 * This method sets the value of MinQty.
	 * 
	 * @param aiMinQty   int 
	 */
	public final void setMinQty(int aiMinQty)
	{
		ciMinQty = aiMinQty;
	}
	/**
	 * This method sets the value of NextAvailIndi.
	 * 
	 * @param aiNextAvailIndi   int 
	 */
	public final void setNextAvailIndi(int aiNextAvailIndi)
	{
		ciNextAvailIndi = aiNextAvailIndi;
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
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * Set value of TransEmpId
	 * 
	 * @param asTransEmpId String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * Set value of csTransWsId
	 * 
	 * @param asTransWsId String
	 */
	public void setTransWsId(String asTransWsId)
	{
		csTransWsId = asTransWsId;
	}
}
