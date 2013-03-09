package com.txdot.isd.rts.client.webapps.data;

import java.io.Serializable;

/*
 *
 * VehicleBaseData.java 
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * B Brown		05/15/2006	This class was added back into 5.2.3 from 
 * 							the 5.2.2 vob for data conversion purposes.
 * 							This assumes that RTS on the TxDOT server 
 * 							side is	going 5.2.3 before the TxO server 
 * 							side, and TxO will expect this class as the 
 * 							part of the RefundData object being sent for
 * 							refunds.
 * 							add constructor 
 * 							(services.data.VehicleBaseData)
 * 							defect 8777 Ver 5.2.3
 * B Brown		09/25/2006	Fix a typo where VIN was being set to the 
 * 							docno value. 
 * 							modify constructor 
 * 							(services.data.VehicleBaseData)
 * 							defect 8950 Ver 5.2.6  
 * ---------------------------------------------------------------------
 */
 
/** 
 * This class contains the VehicleBaseData object
 *
 * @version 5.2.6			09/25/2006
 * @author: G Donoso
 * @deprecated
 * <br>Creation Date: 		10/2/2001 10:46:18
 */

public class VehicleBaseData implements Serializable
{
	private java.lang.String csPlateNo;
	private java.lang.String csVin;
	private java.lang.String csDocNo;
	private java.lang.String csOwnerCountyNo;
	private final static long serialVersionUID = 4415023570340749746L;
/**
 * VehicleBaseData constructor comment.
 */
public VehicleBaseData() 
{
	super();

	csPlateNo = null;
	csVin = null;
	csDocNo = null;
	csOwnerCountyNo = null;
}

public VehicleBaseData(
	com.txdot.isd.rts.services.data.VehicleBaseData aaVehicleBaseData)
{
	this.setDocNo(aaVehicleBaseData.getDocNo());
	// defect 8950
	// this.setVin(aaVehicleBaseData.getDocNo());
	this.setVin(aaVehicleBaseData.getVin());
	// end defect 8950
	this.setOwnerCountyNo(aaVehicleBaseData.getOwnerCountyNo());
	this.setPlateNo(aaVehicleBaseData.getPlateNo());
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:52:59 PM)
 * @return java.lang.String
 */
public java.lang.String getDocNo() {
	return csDocNo;
}
public int getOwnerCounty(){
	return (new Integer(csOwnerCountyNo)).intValue();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:53:45 PM)
 * @return java.lang.String
 */
public java.lang.String getOwnerCountyNo() {
	return csOwnerCountyNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:52:31 PM)
 * @return java.lang.String
 */
public java.lang.String getPlateNo() {
	return csPlateNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:52:45 PM)
 * @return java.lang.String
 */
public java.lang.String getVin() {
	return csVin;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:52:59 PM)
 * @param newDocNo java.lang.String
 */
public void setDocNo(java.lang.String newDocNo) {
	csDocNo = newDocNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:53:45 PM)
 * @param newOwnerCountyNo java.lang.String
 */
public void setOwnerCountyNo(java.lang.String newOwnerCountyNo) {
	csOwnerCountyNo = newOwnerCountyNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:52:31 PM)
 * @param newPlateNo java.lang.String
 */
public void setPlateNo(java.lang.String newPlateNo) {
	csPlateNo = newPlateNo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/01 3:52:45 PM)
 * @param newVin java.lang.String
 */
public void setVin(java.lang.String newVin) {
	csVin = newVin;
}
}
