package com.txdot.isd.rts.services.reports.funds;import java.io.Serializable;import com.txdot.isd.rts.services.util.Dollar;/* * FeeTypeReportData.java *  * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * S Johnston	05/10/2005	Code Cleanup for Java 1.4.2 upgrade * 							modify  *							defect 7896 Ver 5.2.3 * --------------------------------------------------------------------- *//** * This Data class contains attributes and get set methods for * FeeTypeReportData *   * @version		5.2.3		05/10/2005  * @author		Kathy Harrell * <br>Creation Date:		09/19/2001 16:07:59  *//* &FeeTypeReportData& */public class FeeTypeReportData implements Serializable{/* &FeeTypeReportData'ciCashWsId& */	protected int ciCashWsId;/* &FeeTypeReportData'csTransEmpId& */	protected String csTransEmpId;/* &FeeTypeReportData'ciFeeSourceCd& */	protected int ciFeeSourceCd;/* &FeeTypeReportData'csPayableTypeCd& */	protected String csPayableTypeCd;/* &FeeTypeReportData'csPayableTypeCdDesc& */	protected String csPayableTypeCdDesc;/* &FeeTypeReportData'csAcctItmCdDesc& */	protected String csAcctItmCdDesc;/* &FeeTypeReportData'ciAcctItmGrpCd& */	protected int ciAcctItmGrpCd;/* &FeeTypeReportData'ciAcctItmCrdtIndi& */	protected int ciAcctItmCrdtIndi;/* &FeeTypeReportData'ciItmQty& */	protected int ciItmQty;/* &FeeTypeReportData'caItmPrice& */	protected Dollar caItmPrice;	/**	 * Returns the value of CashWsId	 * 	 * @return int 	 *//* &FeeTypeReportData.getCashWsId& */	public final int getCashWsId()	{		return ciCashWsId;	}	/**	 * This method sets the value of CashWsId.	 * 	 * @param aiCashWsId int 	 *//* &FeeTypeReportData.setCashWsId& */	public final void setCashWsId(int aiCashWsId)	{		ciCashWsId = aiCashWsId;	}	/**	 * Returns the value of TransEmpId	 * 	 * @return String 	 *//* &FeeTypeReportData.getTransEmpId& */	public final String getTransEmpId()	{		return csTransEmpId;	}	/**	 * This method sets the value of TransEmpId.	 * 	 * @param asTransEmpId String 	 *//* &FeeTypeReportData.setTransEmpId& */	public final void setTransEmpId(String asTransEmpId)	{		csTransEmpId = asTransEmpId;	}	/**	 * Returns the value of FeeSourceCd	 * 	 * @return int 	 *//* &FeeTypeReportData.getFeeSourceCd& */	public final int getFeeSourceCd()	{		return ciFeeSourceCd;	}	/**	 * This method sets the value of FeeSourceCd.	 * 	 * @param aiFeeSourceCd int 	 *//* &FeeTypeReportData.setFeeSourceCd& */	public final void setFeeSourceCd(int aiFeeSourceCd)	{		ciFeeSourceCd = aiFeeSourceCd;	}	/**	 * Returns the value of PayableTypeCd	 * 	 * @return String 	 *//* &FeeTypeReportData.getPayableTypeCd& */	public final String getPayableTypeCd()	{		return csPayableTypeCd;	}	/**	 * This method sets the value of PayableTypeCd.	 * 	 * @param asPayableTypeCd String 	 *//* &FeeTypeReportData.setPayableTypeCd& */	public final void setPayableTypeCd(String asPayableTypeCd)	{		csPayableTypeCd = asPayableTypeCd;	}	/**	 * Returns the value of PayableTypeCdDesc	 * 	 * @return String 	 *//* &FeeTypeReportData.getPayableTypeCdDesc& */	public final String getPayableTypeCdDesc()	{		return csPayableTypeCdDesc;	}	/**	 * This method sets the value of PayableTypeCdDesc.	 * 	 * @param asPayableTypeCdDesc String 	 *//* &FeeTypeReportData.setPayableTypeCdDesc& */	public final void setPayableTypeCdDesc(String asPayableTypeCdDesc)	{		csPayableTypeCdDesc = asPayableTypeCdDesc;	}	/**	 * Returns the value of AcctItmCdDesc	 * 	 * @return String 	 *//* &FeeTypeReportData.getAcctItmCdDesc& */	public final String getAcctItmCdDesc()	{		return csAcctItmCdDesc;	}	/**	 * This method sets the value of AcctItmCdDesc.	 * 	 * @param asAcctItmCdDesc String 	 *//* &FeeTypeReportData.setAcctItmCdDesc& */	public final void setAcctItmCdDesc(String asAcctItmCdDesc)	{		csAcctItmCdDesc = asAcctItmCdDesc;	}	/**	 * Returns the value of AcctItmGrpCd	 * 	 * @return int 	 *//* &FeeTypeReportData.getAcctItmGrpCd& */	public final int getAcctItmGrpCd()	{		return ciAcctItmGrpCd;	}	/**	 * This method sets the value of AcctItmGrpCd.	 * 	 * @param aiAcctItmGrpCd int 	 *//* &FeeTypeReportData.setAcctItmGrpCd& */	public final void setAcctItmGrpCd(int aiAcctItmGrpCd)	{		ciAcctItmGrpCd = aiAcctItmGrpCd;	}	/**	 * Returns the value of AcctItmCrdtIndi	 * 	 * @return int 	 *//* &FeeTypeReportData.getAcctItmCrdtIndi& */	public final int getAcctItmCrdtIndi()	{		return ciAcctItmCrdtIndi;	}	/**	 * This method sets the value of AcctItmCrdtIndi.	 * 	 * @param aiAcctItmCrdtIndi int 	 *//* &FeeTypeReportData.setAcctItmCrdtIndi& */	public final void setAcctItmCrdtIndi(int aiAcctItmCrdtIndi)	{		ciAcctItmCrdtIndi = aiAcctItmCrdtIndi;	}	/**	 * Returns the value of ItmQty	 * 	 * @return int 	 *//* &FeeTypeReportData.getItmQty& */	public final int getItmQty()	{		return ciItmQty;	}	/**	 * This method sets the value of ItmQty.	 * 	 * @param aiItmQty int 	 *//* &FeeTypeReportData.setItmQty& */	public final void setItmQty(int aiItmQty)	{		ciItmQty = aiItmQty;	}	/**	 * Returns the value of ItmPrice	 * 	 * @return Dollar 	 *//* &FeeTypeReportData.getItmPrice& */	public final Dollar getItmPrice()	{		return caItmPrice;	}	/**	 * This method sets the value of ItmPrice.	 * 	 * @param aaItmPrice Dollar 	 *//* &FeeTypeReportData.setItmPrice& */	public final void setItmPrice(Dollar aaItmPrice)	{		caItmPrice = aaItmPrice;	}}/* #FeeTypeReportData# */