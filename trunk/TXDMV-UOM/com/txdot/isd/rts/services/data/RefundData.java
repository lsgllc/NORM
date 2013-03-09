package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * RefundData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/30/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 
 * B Brown		05/15/2006  For the MQ processing of refunds,
 * 							add constructor(client.webapps.data.Refund
 * 							Data to convert the 5.2.2 TxO RefundData
 * 							object back to 5.2.3 (services.data) 
 * 							RefundData object.
 * 							Also add this same conversion in the
 * 							VehicleBaseData class since VehicleBaseData
 * 							is part of this class.
 * 							add constructor RefundData(RefundData).
 * 							defect 8777 Ver 5.2.3
 * B Brown		03/30/2010	Pass a boolean telling the RegRenService 
 * 							servlet at TxO to attempt refund at Epay
 * 							instead of TPE.
 * 							add cbEpay, getters, setters
 * 							defect 10281 Ver 6.4
 * B Brown		04/26/2010	Pass the payment system used instead of a 
 * 							boolean.
 * 							add csPaymentSystem, getters, setters
 * 							delete cbEpay, getters, setters
 * 							defect 10281 Ver 6.4				 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * RefundData
 * 
 * @version	6.4			04/26/2010	 
 * @author	Administrator
 * <br>Creation Date: 	10/29/2001 11:30:42
 */

public class RefundData implements Serializable
{
	// int
	private int ciRefundStatus;

	// String
	private String csConvFee; 
	private String csOrigTraceNo;
	private String csPymtOrderId;
	private String csRefAmt;

	// Object 
	private VehicleBaseData caVehBaseData;
	
	// defect 10281
	// private boolean cbEpayUsed;
	private String csPaymentSystem = "";
	// end defect 10281
	private final static long serialVersionUID = 3533281748002449697L;
	/**
	 * RefundData constructor comment.
	 */
	public RefundData()
	{
		super();
	}
	
	public RefundData(com.txdot.isd.rts.client.webapps.data.RefundData aaRefundData)
	{
		super();
		this.setConvFee(aaRefundData.getConvFee());
		this.setOrigTraceNo(aaRefundData.getOrigTraceNo());
		this.setPymtOrderId(aaRefundData.getPymtOrderId());
		this.setRefAmt(aaRefundData.getRefAmt());
		this.setRefundStatus(aaRefundData.getRefundStatus());
		this.setVehBaseData(new com.txdot.isd.rts.services.data.VehicleBaseData(aaRefundData.getVehBaseData()));
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
	 * Return value of OrigTraceNo
	 * 
	 * @return String
	 */
	public String getOrigTraceNo()
	{
		return csOrigTraceNo;
	}
	/**
	 * Return value of PymtOrderId
	 * 
	 * @return String
	 */
	public String getPymtOrderId()
	{
		return csPymtOrderId;
	}
	/**
	 * Return value of RefAmt
	 * 
	 * @return String 
	 */
	public String getRefAmt()
	{
		return csRefAmt;
	}
	/**
	 * Return value of RefundStatus
	 * 
	 * @return int
	 */
	public int getRefundStatus()
	{
		return ciRefundStatus;
	}
	/**
	 * Return value of VehBaseData
	 * 
	 * @return VehicleBaseData
	 */
	public VehicleBaseData getVehBaseData()
	{
		return caVehBaseData;
	}
	/**
	 * Set value of ConvFee
	 * 
	 * @param asConvFee String
	 */
	public void setConvFee(String asConvFee)
	{
		csConvFee = asConvFee;
	}
	/**
	 * Set value of OrigTraceNo
	 * 
	 * @param asOrigTraceNo String
	 */
	public void setOrigTraceNo(String asOrigTraceNo)
	{
		csOrigTraceNo = asOrigTraceNo;
	}
	/**
	 * Set value of PymtOrderId
	 * 
	 * @param asPymtOrderId String
	 */
	public void setPymtOrderId(String asPymtOrderId)
	{
		csPymtOrderId = asPymtOrderId;
	}
	/**
	 * Set value of RefAmt
	 * 
	 * @param asRefAmt String
	 */
	public void setRefAmt(String asRefAmt)
	{
		csRefAmt = asRefAmt;
	}
	/**
	 * Set value of RefundStatus
	 * 
	 * @param aiRefundStatus int
	 */
	public void setRefundStatus(int aiRefundStatus)
	{
		ciRefundStatus = aiRefundStatus;
	}
	/**
	 * Return value of VehBaseData
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 */
	public void setVehBaseData(VehicleBaseData aaVehBaseData)
	{
		caVehBaseData = aaVehBaseData;
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

}
