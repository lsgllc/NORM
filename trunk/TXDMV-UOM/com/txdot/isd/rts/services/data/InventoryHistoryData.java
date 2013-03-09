package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * InventoryHistoryData.java
 *
 * (c) Texas Department of Transportation 2001 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work	
 * 							Moved from server.db
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	10/12/2009	add isModified()
 * 							defect 10207 Ver Defect_POS_G   
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * InventoryHistoryData
 *
 * @version	6.5.0 			06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001 13:35:39
 */

public class InventoryHistoryData implements Serializable
{
	protected int ciDelInvReasnCd;
	protected int ciInvcCorrIndi;
	protected int ciInvItmYr;
	protected int ciInvQty;
	protected int ciOfcIssuanceNo;
	protected int ciSubStaId;
	protected int ciTransAMDate;
	protected int ciTransTime;
	protected int ciTransWsId;

	protected String csDelInvReasnTxt;
	protected String csInvcNo;
	protected String csInvEndNo;
	protected String csInvId;
	protected String csInvItmNo;
	protected String csItmCd;
	protected String csTransCd;
	protected String csTransEmpId;
	// defect 10505 
	protected String csTransId;
	// end defect 10505

	private final static long serialVersionUID = 829192638431656077L;

	/**
	 * Returns the value of DelInvReasnCd
	 *
	 * @return  int 
	 */
	public final int getDelInvReasnCd()
	{
		return ciDelInvReasnCd;
	}
	/**
	 * Returns the value of DelInvReasnTxt
	 *
	 * @return  String 
	 */
	public final String getDelInvReasnTxt()
	{
		return csDelInvReasnTxt;
	}
	/**
	 * Returns the value of InvcCorrIndi
	 *
	 * @return  int 
	 */
	public final int getInvcCorrIndi()
	{
		return ciInvcCorrIndi;
	}
	/**
	 * Returns the value of InvcNo
	 *
	 * @return  String 
	 */
	public final String getInvcNo()
	{
		return csInvcNo;
	}
	/**
	 * Returns the value of InvEndNo
	 *
	 * @return  String 
	 */
	public final String getInvEndNo()
	{
		return csInvEndNo;
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
	 * Returns the value of InvQty
	 *
	 * @return  int 
	 */
	public final int getInvQty()
	{
		return ciInvQty;
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
	 * Returns the value of OfcIssuanceNo
	 *
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of SubStaId
	 *
	 * @return  int 
	 */
	public final int getSubStaId()
	{
		return ciSubStaId;
	}
	/**
	 * Returns the value of TransAMDate
	 *
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}
	/**
	 * Returns the value of TransCd
	 *
	 * @return  String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * Returns the value of TransEmpId
	 *
	 * @return  String 
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
		if (csTransId == null || csTransId.trim().length() == 0)
		{
			csTransId =
				UtilityMethods.getTransId(
					ciOfcIssuanceNo,
					ciTransWsId,
					ciTransAMDate,
					ciTransTime);
		}
		return csTransId;
	}

	/**
	 * Returns the value of TransTime
	 *
	 * @return  int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Returns the value of TransWsId
	 *
	 * @return  int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/** 
	 * Returns boolean to denote if Invoice Modified 
	 * 
	 */
	public boolean isModified()
	{
		return ciInvcCorrIndi != 0;
	}

	/**
	 * This method sets the value of DelInvReasnCd.
	 *
	 * @param aiDelInvReasnCd  int    
	 */
	public final void setDelInvReasnCd(int aiDelInvReasnCd)
	{
		ciDelInvReasnCd = aiDelInvReasnCd;
	}
	/**
	 * This method sets the value of DelInvReasnTxt.
	 *
	 * @param asDelInvReasnTxt  String  
	 */
	public final void setDelInvReasnTxt(String asDelInvReasnTxt)
	{
		csDelInvReasnTxt = asDelInvReasnTxt;
	}
	/**
	 * This method sets the value of InvcCorrIndi.
	 *
	 * @param aiInvcCorrIndi  int  
	 */
	public final void setInvcCorrIndi(int aiInvcCorrIndi)
	{
		ciInvcCorrIndi = aiInvcCorrIndi;
	}
	/**
	 * This method sets the value of InvcNo.
	 *
	 * @param asInvcNo  String  
	 */
	public final void setInvcNo(String asInvcNo)
	{
		csInvcNo = asInvcNo;
	}
	/**
	 * This method sets the value of InvEndNo.
	 *
	 * @param asInvEndNo  String 
	 */
	public final void setInvEndNo(String asInvEndNo)
	{
		csInvEndNo = asInvEndNo;
	}
	/**
	 * This method sets the value of InvId.
	 *
	 * @param asInvId  String 
	 */
	public final void setInvId(String asInvId)
	{
		csInvId = asInvId;
	}
	/**
	 * This method sets the value of InvItmNo.
	 *
	 * @param asInvItmNo  String  
	 */
	public final void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}
	/**
	 * This method sets the value of InvItmYr.
	 *
	 * @param aiInvItmYr  int
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	/**
	 * This method sets the value of InvQty.
	 *
	 * @param aiInvQty  int 
	 */
	public final void setInvQty(int aiInvQty)
	{
		ciInvQty = aiInvQty;
	}
	/**
	 * This method sets the value of ItmCd.
	 *
	 * @param asItmCd  int  
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 *
	 * @param aiOfcIssuanceNo  int  
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SubStaId.
	 *
	 * @param aiSubStaId  int  
	 */
	public final void setSubStaId(int aiSubStaId)
	{
		ciSubStaId = aiSubStaId;
	}
	/**
	 * This method sets the value of TransAMDate.
	 *
	 * @param aiTransAMDate	int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	/**
	 * This method sets the value of TransCd.
	 *
	 * @param asTransCd   String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
	/**
	 * This method sets the value of TransEmpId.
	 *
	 * @param asTransEmpId   String
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * Sets the value of TransId
	 * 
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * This method sets the value of TransTime.
	 *
	 * @param aiTransTime  int   
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	/**
	 * This method sets the value of TransWsId.
	 *
	 * @param aiTransWsId  int   
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
}
