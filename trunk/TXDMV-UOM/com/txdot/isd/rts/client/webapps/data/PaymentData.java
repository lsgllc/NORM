package com.txdot.isd.rts.client.webapps.data;

import java.io.Serializable;

/*
 *
 * PaymentData.java 
 *
 * (c) Texas Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * B Brown		05/15/2006	This class was added back into 5.2.3 from 
 * 							the 5.2.2 vob for data conversion purposes.
 * 							This assumes that RTS on the TxDOT server 
 * 							side is	going 5.2.3 before the TxO server 
 * 							side, and TxO will expect this class as the 
 * 							object being sent for failed payments.
 * 							add constructor (services.data.PaymentData)
 * 							defect 8777 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
 
/** 
 * This class contains the PaymentData object
 *
 * @version 5.2.3			05/15/2006
 * @author: C Chen
 * @deprecated
 * <br>Creation Date: 		01/21/2002 11:49:45
 */

public class PaymentData implements Serializable{ 
	public static final int REG_FEE=0;
	public static final int CONV_FEE=1;
	// indicate which Fee to capture
	int ciType=REG_FEE;
	
	// DB fields
	private String csOrigTraceNo;
	private String csPmntOrderId;
	private int ciPmntStatusCd;
	private java.lang.String csPmntAmt;
	private java.lang.String csConvFee;
	private int ciOfcIssuanceNo;

	// Epay fields
	// Epay needs, but not uses, not stored ==> dummy
	private String csEpayCID="FIRSTPLATENO";
	private java.lang.String csBillName="RegRenCustomer";
	// needs 	
	private java.lang.String csEpayAmt;
	private final static long serialVersionUID = 4559960275326776609L;
/**
 * PaymentData constructor comment.
 */
public PaymentData() {
	super();
}
public PaymentData(PaymentData aCopy) {
	super();

	csPmntAmt=aCopy.getPmntAmt();
	csConvFee=aCopy.getConvFee();
	csEpayCID=aCopy.getEpayCID();
	csOrigTraceNo=aCopy.getOrigTraceNo();
	csPmntOrderId=aCopy.getPmntOrderId();
	ciPmntStatusCd=aCopy.getPmntStatusCd();
	csEpayAmt=aCopy.getEpayAmt();	
}

public PaymentData(com.txdot.isd.rts.services.data.PaymentData aaPaymentData)
{
	super();

	this.setPmntAmt(aaPaymentData.getPmntAmt());
	this.setConvFee(aaPaymentData.getConvFee());
	this.setEpayCID(aaPaymentData.getEpayCID());
	this.setOrigTraceNo(aaPaymentData.getOrigTraceNo());
	this.setPmntOrderId(aaPaymentData.getPmntOrderId());
	this.setPmntStatusCd(aaPaymentData.getPmntStatusCd());
	this.setEpayAmt(aaPaymentData.getEpayAmt());
	this.setBillName(aaPaymentData.getBillName());
	this.setOfcIssuanceNo(aaPaymentData.getOfcIssuanceNo());
	this.setType(aaPaymentData.getType());
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/02 5:54:02 PM)
 * @return java.lang.String
 */
public java.lang.String getBillName() {
	return csBillName;
}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:04:55 PM)
 * @return java.lang.String
 */
public java.lang.String getConvFee() {
	return csConvFee;
}
/**
 * Insert the method's description here.
 * Creation date: (2/1/02 12:51:58 PM)
 * @return java.lang.String
 */
public java.lang.String getEpayAmt() {
	return csEpayAmt;
}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:05:34 PM)
 * @return java.lang.String
 */
public java.lang.String getEpayCID() {
	return csEpayCID;
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/02 4:28:00 PM)
 * @return int
 */
public int getOfcIssuanceNo() {
	return ciOfcIssuanceNo;
}
	public String getOrigTraceNo(){
		return csOrigTraceNo;
	}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:04:28 PM)
 * @return java.lang.String
 */
public java.lang.String getPmntAmt() {
	return csPmntAmt;
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/02 10:22:33 AM)
 * @return java.lang.String
 */
public java.lang.String getPmntOrderId() {
	return csPmntOrderId;
}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:06:00 PM)
 * @return int
 */
public int getPmntStatusCd() {
	return ciPmntStatusCd;
}
public int getType(){
	return ciType;
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/02 5:54:02 PM)
 * @param newBillName java.lang.String
 */
public void setBillName(java.lang.String newBillName) {
	csBillName = newBillName;
}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:04:55 PM)
 * @param newConvFee java.lang.String
 */
public void setConvFee(java.lang.String newConvFee) {
	csConvFee = newConvFee;
}
/**
 * Insert the method's description here.
 * Creation date: (2/1/02 12:51:58 PM)
 * @param newRegFee java.lang.String
 */
public void setEpayAmt(java.lang.String newEpayAmt) {
	csEpayAmt = newEpayAmt;
}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:05:34 PM)
 * @param newEpayCID java.lang.String
 */
public void setEpayCID(java.lang.String newEpayCID) {
	csEpayCID = newEpayCID;
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/02 4:28:00 PM)
 * @param newOfcIssuanceNo int
 */
public void setOfcIssuanceNo(int newOfcIssuanceNo) {
	ciOfcIssuanceNo = newOfcIssuanceNo;
}
	public void setOrigTraceNo(String newOrigTraceNo){
		csOrigTraceNo=newOrigTraceNo;
	}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:04:28 PM)
 * @param newPmntAmt java.lang.String
 */
public void setPmntAmt(java.lang.String newPmntAmt) {
	csPmntAmt = newPmntAmt;
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/02 10:22:33 AM)
 * @param newPymntOrderId java.lang.String
 */
public void setPmntOrderId(java.lang.String newPmntOrderId) {
	csPmntOrderId = newPmntOrderId;
}
/**
 * Insert the method's description here.
 * Creation date: (1/11/02 3:06:00 PM)
 * @param newPmntStatusCd int
 */
public void setPmntStatusCd(int newPymntStatusCd) {
	ciPmntStatusCd = newPymntStatusCd;
}
public void setType(int newType){
	ciType=newType;
}
}
