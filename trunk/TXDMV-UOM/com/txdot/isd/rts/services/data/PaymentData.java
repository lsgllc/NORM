package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * PaymentData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/10/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 
 * B Brown		03/30/2010	Pass a boolean telling the RegRenService 
 * 							servlet at TxO to attempt capture at Epay
 * 							instead of TPE.
 * 							add cbEpay, getters, setters
 * 							defect 10281 Ver 6.4
 * B Brown		04/26/2010	Pass the payment system used instead of a 
 * 							boolean.
 * 							add csPaymentSystem, getters, setters
 * 							delete cbEpay, getters, setters
 * 							defect 10281 Ver 6.4
 * B Brown		10/25/2011	Add capture time
 * 							add csCaptureTime, getter, setter
 * 							defect 10996 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * PaymentData
 * 
 * @version	6.9.0		10/25/2011 
 * @author	Administrator
 * <br>Creation Date: 	01/11/2002 15:02:27
 */

public class PaymentData implements Serializable
{

	// Constants  
	public static final int REG_FEE = 0;
	public static final int CONV_FEE = 1;
	// indicate which Fee to capture
	int ciType = REG_FEE;

	// DB fields
	private String csOrigTraceNo;
	private String csPmntOrderId;
	private String csPmntAmt;
	private String csConvFee;
	private int ciPmntStatusCd;
	private int ciOfcIssuanceNo;

	// Epay fields
	// Epay needs, but not uses, not stored ==> dummy
	private String csEpayCID = "FIRSTPLATENO";
	private String csBillName = "RegRenCustomer";
	// needs 	
	private String csEpayAmt;
	
	// defect 10281
	// private boolean cbEpayUsed;
	private String csPaymentSystem = "";
	// end defect 10281
	
	// defect 10996
	private String csCaptureTime = "";
	// end defect 10996

	private final static long serialVersionUID = 4559960275326776609L;

	/**
	 * PaymentData constructor comment.
	 */
	public PaymentData()
	{
		super();
	}

	/**
	 * PaymentData constructor comment.
	 */
	public PaymentData(PaymentData aCopy)
	{
		super();

		csPmntAmt = aCopy.getPmntAmt();
		csConvFee = aCopy.getConvFee();
		csEpayCID = aCopy.getEpayCID();
		csOrigTraceNo = aCopy.getOrigTraceNo();
		csPmntOrderId = aCopy.getPmntOrderId();
		ciPmntStatusCd = aCopy.getPmntStatusCd();
		csEpayAmt = aCopy.getEpayAmt();
	}
	/**
	 * Return value of BillName
	 * 
	 * @return String
	 */
	public String getBillName()
	{
		return csBillName;
	}
	/**
	 * Return value of ConvFee
	 *
	 * @return String
	 */
	public String getConvFee()
	{
		return csConvFee;
	}
	/**
	 * Return value of EpayAmt
	 * 
	 * @return String
	 */
	public String getEpayAmt()
	{
		return csEpayAmt;
	}
	/**
	 * Return value of EpayCID
	 * 
	 * @return String
	 */
	public String getEpayCID()
	{
		return csEpayCID;
	}
	/**
	 * Return value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return value of OrigTraceNo
	 * 
	 * @return String
	 */
	public String getOrigTraceNo()
	{
		return csOrigTraceNo;
	}
	/**
	 * Return value of PmntAmt
	 * 
	 * @return String
	 */
	public String getPmntAmt()
	{
		return csPmntAmt;
	}
	/**
	 * Return value of PmntOrderId
	 * 
	 * @return String
	 */
	public String getPmntOrderId()
	{
		return csPmntOrderId;
	}
	/**
	 * Return value of PmntStatusCd
	 * 
	 * @return int
	 */
	public int getPmntStatusCd()
	{
		return ciPmntStatusCd;
	}
	/**
	 * 
	 * Return value of Type
	 * 
	 * @return int 
	 */
	public int getType()
	{
		return ciType;
	}
	/**
	 * Set value of BillName
	 * 
	 * @param asBillName String
	 */
	public void setBillName(String asBillName)
	{
		csBillName = asBillName;
	}
	/**
	 * Set value of 
	 * 
	 * @param asConvFee String
	 */
	public void setConvFee(String asConvFee)
	{
		csConvFee = asConvFee;
	}
	/**
	 * Set value of EpayAmt
	 * 
	 * @param asRegFee String
	 */
	public void setEpayAmt(String asEpayAmt)
	{
		csEpayAmt = asEpayAmt;
	}
	/**
	 * Set value of EpayCID
	 * 
	 * @param asEpayCID String
	 */
	public void setEpayCID(String asEpayCID)
	{
		csEpayCID = asEpayCID;
	}
	/**
	 * Set value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set value of OrigTranceNo
	 * 
	 * @param asOrigTraceNo
	 */
	public void setOrigTraceNo(String asOrigTraceNo)
	{
		csOrigTraceNo = asOrigTraceNo;
	}
	/**
	 * Set value of PmntAmt
	 * 
	 * @param asPmntAmt String
	 */
	public void setPmntAmt(String asPmntAmt)
	{
		csPmntAmt = asPmntAmt;
	}
	/**
	 * Set value of PmntOrderId
	 * 
	 * @param asPmntOrderId String
	 */
	public void setPmntOrderId(String asPmntOrderId)
	{
		csPmntOrderId = asPmntOrderId;
	}
	/**
	 * Set value of PmntStatusCd
	 * 
	 * @param aiPmntStatusCd int
	 */
	public void setPmntStatusCd(int aiPymntStatusCd)
	{
		ciPmntStatusCd = aiPymntStatusCd;
	}
	/**
	 * Set value of Type
	 * 
	 * @param aiType
	 */
	public void setType(int aiType)
	{
		ciType = aiType;
	}
	/**
	 * Get the payment system used: TPE or Epay 
	 * 
	 * @return String
	 */
	public String getPaymentSystem()
	{
		return csPaymentSystem;
	}

	/**
	 * Set the payment system used: TPE or Epay 
	 * 
	 * @param asPaymentSystem
	 */
	public void setPaymentSystem(String asPaymentSystem)
	{
		csPaymentSystem = asPaymentSystem;
	}

	/**
	 * @return the csCaptureTime
	 */
	public String getCaptureTime()
	{
		return csCaptureTime;
	}

	/**
	 * @param csCaptureTime the csCaptureTime to set
	 */
	public void setCaptureTime(String asCaptureTime)
	{
		csCaptureTime = asCaptureTime;
	}

}
