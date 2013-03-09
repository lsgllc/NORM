package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * FeeTypeReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * S Johnston	05/10/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for
 * FeeTypeReportData
 *  
 * @version		5.2.3		05/10/2005 
 * @author		Kathy Harrell
 * <br>Creation Date:		09/19/2001 16:07:59 
 */
public class FeeTypeReportData implements Serializable
{
	protected int ciCashWsId;
	protected String csTransEmpId;
	protected int ciFeeSourceCd;
	protected String csPayableTypeCd;
	protected String csPayableTypeCdDesc;
	protected String csAcctItmCdDesc;
	protected int ciAcctItmGrpCd;
	protected int ciAcctItmCrdtIndi;
	protected int ciItmQty;
	protected Dollar caItmPrice;

	/**
	 * Returns the value of CashWsId
	 * 
	 * @return int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * Returns the value of TransEmpId
	 * 
	 * @return String 
	 */
	public final String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * Returns the value of FeeSourceCd
	 * 
	 * @return int 
	 */
	public final int getFeeSourceCd()
	{
		return ciFeeSourceCd;
	}
	/**
	 * This method sets the value of FeeSourceCd.
	 * 
	 * @param aiFeeSourceCd int 
	 */
	public final void setFeeSourceCd(int aiFeeSourceCd)
	{
		ciFeeSourceCd = aiFeeSourceCd;
	}
	/**
	 * Returns the value of PayableTypeCd
	 * 
	 * @return String 
	 */
	public final String getPayableTypeCd()
	{
		return csPayableTypeCd;
	}
	/**
	 * This method sets the value of PayableTypeCd.
	 * 
	 * @param asPayableTypeCd String 
	 */
	public final void setPayableTypeCd(String asPayableTypeCd)
	{
		csPayableTypeCd = asPayableTypeCd;
	}
	/**
	 * Returns the value of PayableTypeCdDesc
	 * 
	 * @return String 
	 */
	public final String getPayableTypeCdDesc()
	{
		return csPayableTypeCdDesc;
	}
	/**
	 * This method sets the value of PayableTypeCdDesc.
	 * 
	 * @param asPayableTypeCdDesc String 
	 */
	public final void setPayableTypeCdDesc(String asPayableTypeCdDesc)
	{
		csPayableTypeCdDesc = asPayableTypeCdDesc;
	}
	/**
	 * Returns the value of AcctItmCdDesc
	 * 
	 * @return String 
	 */
	public final String getAcctItmCdDesc()
	{
		return csAcctItmCdDesc;
	}
	/**
	 * This method sets the value of AcctItmCdDesc.
	 * 
	 * @param asAcctItmCdDesc String 
	 */
	public final void setAcctItmCdDesc(String asAcctItmCdDesc)
	{
		csAcctItmCdDesc = asAcctItmCdDesc;
	}
	/**
	 * Returns the value of AcctItmGrpCd
	 * 
	 * @return int 
	 */
	public final int getAcctItmGrpCd()
	{
		return ciAcctItmGrpCd;
	}
	/**
	 * This method sets the value of AcctItmGrpCd.
	 * 
	 * @param aiAcctItmGrpCd int 
	 */
	public final void setAcctItmGrpCd(int aiAcctItmGrpCd)
	{
		ciAcctItmGrpCd = aiAcctItmGrpCd;
	}
	/**
	 * Returns the value of AcctItmCrdtIndi
	 * 
	 * @return int 
	 */
	public final int getAcctItmCrdtIndi()
	{
		return ciAcctItmCrdtIndi;
	}
	/**
	 * This method sets the value of AcctItmCrdtIndi.
	 * 
	 * @param aiAcctItmCrdtIndi int 
	 */
	public final void setAcctItmCrdtIndi(int aiAcctItmCrdtIndi)
	{
		ciAcctItmCrdtIndi = aiAcctItmCrdtIndi;
	}
	/**
	 * Returns the value of ItmQty
	 * 
	 * @return int 
	 */
	public final int getItmQty()
	{
		return ciItmQty;
	}
	/**
	 * This method sets the value of ItmQty.
	 * 
	 * @param aiItmQty int 
	 */
	public final void setItmQty(int aiItmQty)
	{
		ciItmQty = aiItmQty;
	}
	/**
	 * Returns the value of ItmPrice
	 * 
	 * @return Dollar 
	 */
	public final Dollar getItmPrice()
	{
		return caItmPrice;
	}
	/**
	 * This method sets the value of ItmPrice.
	 * 
	 * @param aaItmPrice Dollar 
	 */
	public final void setItmPrice(Dollar aaItmPrice)
	{
		caItmPrice = aaItmPrice;
	}

}