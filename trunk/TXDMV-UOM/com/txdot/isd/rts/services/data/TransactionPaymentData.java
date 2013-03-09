package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;

/*
 * TransactionPaymentData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/18/2001  Added columns for SendTrans
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	01/28/2009	sorted members
 * 							delete ciChngDuePymntType, 
 * 							 get/set methods.
 * 							defect 8469 Defect_POS_D
 * K Harrell	01/25/2011	delete ciBranchOfcId, get/set methods 
 * 							add TransactionPaymentData(),
 * 							 TransactionPaymentData(TransactionHeaderData) 
 * 							defect 10734 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * TransactionPaymentData
 *
 * @version	6.7.0 			01/25/2011 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001 13:25:07 
 **/

public class TransactionPaymentData
	implements Serializable, Displayable
{

	// boolean 
	protected boolean cbCreditCardFee;

	// Object  
	protected Dollar cdChngDue;
	protected Dollar cdPymntTypeAmt;

	// defect 10734 
	//protected int ciBranchOfcId;
	// end defect 10734 

	protected int ciBsnDate;
	protected int ciCashWsId;

	// defect 8469
	// Referenced ChngDue in RTS I; Removed in RTS II  
	//protected int ciChngDuePymntType;
	// end defect 8469 
	protected int ciChngDuePymntTypeCd;
	protected int ciCustSeqNo;

	// int
	protected int ciOfcIssuanceNo;
	protected int ciPymntTypeCd;
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransPostedLANIndi;
	protected int ciTransPostedMfIndi;
	protected int ciTransTime;
	protected int ciTransWsId;
	protected int ciVersionCd;

	// String
	protected String csPymntCkNo;
	protected String csPymntNo;
	protected String csPymntType;
	protected String csTransEmpId;

	private final static long serialVersionUID = -773845545298527858L;

	/**
	 * TransactionPayment  comment.
	 * 
	 * @throws RTSException 
	 */
	public TransactionPaymentData()
	{
		super();
	}

	/**
	 * TransactionPayment  comment.
	 * 
	 * @param  aaTransHdrData
	 * @throws RTSException 
	 */
	public TransactionPaymentData(TransactionHeaderData aaTransHdrData)
	{
		super();
		ciOfcIssuanceNo = aaTransHdrData.getOfcIssuanceNo();
		ciSubstaId = aaTransHdrData.getSubstaId();
		ciTransAMDate = aaTransHdrData.getTransAMDate();
		ciTransWsId = aaTransHdrData.getTransWsId();
		ciCashWsId = aaTransHdrData.getCashWsId();
		ciCustSeqNo = aaTransHdrData.getCustSeqNo();
	}

	/**
	 * Method used to return field attributes
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();
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

	//	/**
	//	 * Returns the value of BranchOfcId
	//	 * 
	//	 * @return  int 
	//	 */
	//	public final int getBranchOfcId()
	//	{
	//		return ciBranchOfcId;
	//	}

	/**
	 * Returns the value of BsnDate
	 * 
	 * @return  int 
	 */
	public final int getBsnDate()
	{
		return ciBsnDate;
	}

	/**
	 * Returns the value of CashWsId
	 * 
	 * @return  int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}

	/**
	 * Returns the value of ChngDue
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getChngDue()
	{
		return cdChngDue;
	}

	// defect 8469 
	//	/**
	//	* Returns the value of ChngDuePymntType
	//	* 
	//	* @return  int 
	//	*/
	//	public final int getChngDuePymntType()
	//	{
	//		return ciChngDuePymntType;
	//	}
	// end defect 8469 

	/**
	 * Returns the value of ChngDuePymntTypeCd
	 * 
	 * @return  int 
	 */
	public final int getChngDuePymntTypeCd()
	{
		return ciChngDuePymntTypeCd;
	}

	/**
	 * Returns the value of CustSeqNo
	 * 
	 * @return  int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
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
	 * Returns the value of PymntCkNo
	 * 
	 * @return  String 
	 */
	public final String getPymntCkNo()
	{
		return csPymntCkNo;
	}

	/**
	 * Returns the value of PymntNo
	 * 
	 * @return  String 
	 */
	public final String getPymntNo()
	{
		return csPymntNo;
	}

	/**
	* Returns the value of PymntType
	* 
	* @return  int 
	*/
	public final String getPymntType()
	{
		return csPymntType;
	}

	/**
	 * Returns the value of PymntTypeAmt
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getPymntTypeAmt()
	{
		return cdPymntTypeAmt;
	}

	/**
	 * Returns the value of PymntTypeCd
	 * 
	 * @return  int 
	 */
	public final int getPymntTypeCd()
	{
		return ciPymntTypeCd;
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
	 * Returns the value of TransAMDate
	 * 
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
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
	 * Returns the value of TransPostedLANIndi
	 * 
	 * @return  int 
	 */
	public final int getTransPostedLANIndi()
	{
		return ciTransPostedLANIndi;
	}

	/**
	 * Returns the value of TransPostedMfIndi
	 * 
	 * @return  int 
	 */
	public final int getTransPostedMfIndi()
	{
		return ciTransPostedMfIndi;
	}

	/**
	 * Returns the value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
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
	* Returns the value of VersionCd
	* 
	* @return  int 
	*/
	public final int getVersionCd()
	{
		return ciVersionCd;
	}

	/**
	 * Returns the value of CreditCardFee
	 * 
	 * @return boolean
	 */
	public boolean isCreditCardFee()
	{
		return cbCreditCardFee;
	}

	//	/**
	//	* This method sets the value of BranchOfcId.
	//	* 
	//	* @param aBranchOfcId   int 
	//	*/
	//	public final void setBranchOfcId(int aBranchOfcId)
	//	{
	//		ciBranchOfcId = aBranchOfcId;
	//	}

	/**
	 * This method sets the value of BsnDate.
	 * 
	 * @param aiBsnDate   int 
	 */
	public final void setBsnDate(int aiBsnDate)
	{
		ciBsnDate = aiBsnDate;
	}

	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aCashWsId   int 
	 */
	public final void setCashWsId(int aCashWsId)
	{
		ciCashWsId = aCashWsId;
	}

	/**
	 * This method sets the value of ChngDue.
	 * 
	 * @param aaChngDue   Dollar 
	 */
	public final void setChngDue(Dollar aaChngDue)
	{
		cdChngDue = aaChngDue;
	}

	// defect 8469 
	//	/**
	//	* This method sets the value of ChngDuePymntType.
	//	* 
	//	* @param aiChngDuePymntType   int 
	//	*/
	//	public final void setChngDuePymntType(int aiChngDuePymntType)
	//	{
	//		ciChngDuePymntType = aiChngDuePymntType;
	//	}
	// end defect 8469 

	/**
	 * This method sets the value of ChngDuePymntTypeCd.
	 * 
	 * @param aiChngDuePymntTypeCd   int 
	 */
	public final void setChngDuePymntTypeCd(int aiChngDuePymntTypeCd)
	{
		ciChngDuePymntTypeCd = aiChngDuePymntTypeCd;
	}

	/**
	 * This method sets the value of CreditCardFee
	 * 
	 * @param abCreditCardFee boolean
	 */
	public void setCreditCardFee(boolean abCreditCardFee)
	{
		cbCreditCardFee = abCreditCardFee;
	}

	/**
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo   int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
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
	 * This method sets the value of PymntCkNo.
	 * 
	 * @param asPymntCkNo   String 
	 */
	public final void setPymntCkNo(String asPymntCkNo)
	{
		csPymntCkNo = asPymntCkNo;
	}

	/**
	 * This method sets the value of PymntNo.
	 * 
	 * @param aPymntNo   String 
	 */
	public final void setPymntNo(String aPymntNo)
	{
		csPymntNo = aPymntNo;
	}

	/**
	* This method sets the value of PymntType.
	* 
	* @param asPymntType   String 
	*/
	public final void setPymntType(String asPymntType)
	{
		csPymntType = asPymntType;
	}

	/**
	 * This method sets the value of PymntTypeAmt.
	 * 
	 * @param aaPymntTypeAmt   Dollar 
	 */
	public final void setPymntTypeAmt(Dollar aaPymntTypeAmt)
	{
		cdPymntTypeAmt = aaPymntTypeAmt;
	}

	/**
	 * This method sets the value of PymntTypeCd.
	 * 
	 * @param aiPymntTypeCd   int 
	 */
	public final void setPymntTypeCd(int aiPymntTypeCd)
	{
		ciPymntTypeCd = aiPymntTypeCd;
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
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate   int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
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
	 * This method sets the value of TransPostedLANIndi.
	 * 
	 * @param aiTransPostedLANIndi   int 
	 */
	public final void setTransPostedLANIndi(int aiTransPostedLANIndi)
	{
		ciTransPostedLANIndi = aiTransPostedLANIndi;
	}

	/**
	 * This method sets the value of TransPostedMfIndi.
	 * 
	 * @param aiTransPostedMfIndi   int 
	 */
	public final void setTransPostedMfIndi(int aiTransPostedMfIndi)
	{
		ciTransPostedMfIndi = aiTransPostedMfIndi;
	}

	/**
	 * This method sets the value of TransTime
	 * 
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId   int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	* This method sets the value of VersionCd.
	* 
	* @param aiVersionCd   int 
	*/
	public final void setVersionCd(int aiVersionCd)
	{
		ciVersionCd = aiVersionCd;
	}
}
