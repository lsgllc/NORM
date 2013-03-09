package com.txdot.isd.rts.client.webapps.data;

import java.io.Serializable;

/*
 *
 * RefundData.java 
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
 * 							object being sent for refunds.
 * 							Since this object also contains 
 * 							VehicleBaseData, com.txdot.isd.rts.client.
 * 							webapps.data.VehicleBaseData also had to be
 * 							brought back from the 5.2.2 vob.
 * 							add constructor (services.data.RefundData)
 * 							defect 8777 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
 
/** 
 * This class contains the RefundData object
 *
 * @version 5.2.3			05/15/2006
 * @author: C Chen
 * @deprecated
 * <br>Creation Date: 		01/21/2002 11:50:09
 */

public class RefundData implements Serializable{
	private java.lang.String csPymtOrderId;
	private java.lang.String csOrigTraceNo;
	private String csRefAmt;
	private VehicleBaseData cVehBaseData;
	private java.lang.String csConvFee;
	private final static long serialVersionUID = 3533281748002449697L;
	private int ciRefundStatus;
/**
 * RefundData constructor comment.
 */
public RefundData() {
	super();
}

public RefundData(com.txdot.isd.rts.services.data.RefundData aaRefundData)

{
	
	this.setVehBaseData(new com.txdot.isd.rts.client.webapps.data.VehicleBaseData(aaRefundData.getVehBaseData()));				
	this.setRefundStatus(aaRefundData.getRefundStatus());
	this.setOrigTraceNo(aaRefundData.getOrigTraceNo());
	this.setPymtOrderId(aaRefundData.getPymtOrderId());
	this.setRefAmt(aaRefundData.getRefAmt());
	this.setRefundStatus(aaRefundData.getRefundStatus());

}
/**
 * Insert the method's description here.
 * Creation date: (10/30/01 5:29:31 PM)
 * @return java.lang.String
 */
public java.lang.String getConvFee() {
	return csConvFee;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:35:28 AM)
 * @return java.lang.String
 */
public java.lang.String getOrigTraceNo() {
	return csOrigTraceNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:34:57 AM)
 * @return java.lang.String
 */
public java.lang.String getPymtOrderId() {
	return csPymtOrderId;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:49:25 AM)
 * @return com.txdot.isd.rts.services.util.Dollar
 */
public String getRefAmt() {
	return csRefAmt;
}
/**
 * Insert the method's description here.
 * Creation date: (2/20/02 10:59:18 AM)
 * @return int
 */
public int getRefundStatus() {
	return ciRefundStatus;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:37:44 AM)
 * @return com.txdot.isd.rts.client.webapps.data.VehicleBaseData
 */
public VehicleBaseData getVehBaseData() {
	return cVehBaseData;
}
/**
 * Insert the method's description here.
 * Creation date: (10/30/01 5:29:31 PM)
 * @param newConvFee java.lang.String
 */
public void setConvFee(java.lang.String newConvFee) {
	csConvFee = newConvFee;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:35:28 AM)
 * @param newOrigTraceNo java.lang.String
 */
public void setOrigTraceNo(java.lang.String newOrigTraceNo) {
	csOrigTraceNo = newOrigTraceNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:34:57 AM)
 * @param newPymtOrderId java.lang.String
 */
public void setPymtOrderId(java.lang.String newPymtOrderId) {
	csPymtOrderId = newPymtOrderId;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:49:25 AM)
 * @param newRefAmt com.txdot.isd.rts.services.util.Dollar
 */
public void setRefAmt(String newRefAmt) {
	csRefAmt = newRefAmt;
}
/**
 * Insert the method's description here.
 * Creation date: (2/20/02 10:59:18 AM)
 * @param newRefundStatus int
 */
public void setRefundStatus(int newRefundStatus) {
	ciRefundStatus = newRefundStatus;
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/01 11:37:44 AM)
 * @param newVehBaseData com.txdot.isd.rts.client.webapps.data.VehicleBaseData
 */
public void setVehBaseData(VehicleBaseData newVehBaseData) {
	cVehBaseData = newVehBaseData;
}
}
