package com.txdot.isd.rts.services.data;import java.io.Serializable;/* * TitleTransferPenaltyExemptCodeData.java * * (c) Texas Department of Transportation 2008 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	06/22/2008	Recreated  * 							defect 9724 Ver Defect POS A  * --------------------------------------------------------------------- *//** * This Data class contains attributes and get set methods for  * TitleTransferPenaltyExemptCodeData. * * @version	Defect POS A	06/22/2008 * @author	Kathy Harrell * <br>Creation Date:		06/22/2008  *//* &TitleTransferPenaltyExemptCodeData& */public class TitleTransferPenaltyExemptCodeData implements Serializable{/* &TitleTransferPenaltyExemptCodeData'csTtlTrnsfrPnltyExmptCd& */	private String csTtlTrnsfrPnltyExmptCd;/* &TitleTransferPenaltyExemptCodeData'csTtlTrnsfrPnltyExmptDesc& */	private String csTtlTrnsfrPnltyExmptDesc;	/* &TitleTransferPenaltyExemptCodeData'serialVersionUID& */	static final long serialVersionUID = 7346495355151350015L;	/**	 * Get value of csTtlTrnsfrPnltyExmptCd	 * 	 * @return String	 *//* &TitleTransferPenaltyExemptCodeData.getTtlTrnsfrPnltyExmptCd& */	public String getTtlTrnsfrPnltyExmptCd()	{		return csTtlTrnsfrPnltyExmptCd;	}	/**	 * Get value of csTtlTrnsfrPnltyExmptDesc	 * 	 * @return String	 *//* &TitleTransferPenaltyExemptCodeData.getTtlTrnsfrPnltyExmptDesc& */	public String getTtlTrnsfrPnltyExmptDesc()	{		return csTtlTrnsfrPnltyExmptDesc;	}	/**	 * Set value of csTtlTrnsfrPnltyExmptCd	 * 	 * @param asTtlTrnsfrPnltyExmptCd	 *//* &TitleTransferPenaltyExemptCodeData.setTtlTrnsfrPnltyExmptCd& */	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)	{		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;	}	/**	 * Set value of csTtlTrnsfrPnltyExmptDesc	 * 	 * @param asTtlTrnsfrPnltyExmptDesc	 *//* &TitleTransferPenaltyExemptCodeData.setTtlTrnsfrPnltyExmptDesc& */	public void setTtlTrnsfrPnltyExmptDesc(String asTtlTrnsfrPnltyExmptDesc)	{		csTtlTrnsfrPnltyExmptDesc = asTtlTrnsfrPnltyExmptDesc;	}}/* #TitleTransferPenaltyExemptCodeData# */