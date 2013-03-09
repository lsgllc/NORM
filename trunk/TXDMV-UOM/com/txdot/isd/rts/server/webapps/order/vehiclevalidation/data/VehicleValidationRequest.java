package com.txdot.isd.rts.server.webapps.order.vehiclevalidation.data;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest;

/*
 * VehicleValidationRequest.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/02/2007	Created class.
 * 							defect 9120 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Object used to handle the Vehicle Validation Request Function.
 *
 * @version	Special Plates	03/02/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/02/2007 14:30:00
 */
public class VehicleValidationRequest extends AbstractRequest
{
	private String lastFourVin = "";
	private String regPltCd = "";
	private String regPltNo = "";

	/**
	 * Last four digits of VIN.
	 * 
	 * @return String
	 */
	public String getLastFourVin()
	{
		return lastFourVin;
	}

	/**
	 * Plate code.
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return regPltCd;
	}

	/**
	 * Plate number.
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return regPltNo;
	}

	/**
	 * Sets the last four digits of VIN.
	 * 
	 * @param asLastFourVin String
	 */
	public void setLastFourVin(String asLastFourVin)
	{
		lastFourVin = asLastFourVin;
	}

	/**
	 * Sets the Reg Plate Code.
	 * 
	 * @param asRegPltCd String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		regPltCd = asRegPltCd;
	}

	/**
	 * Sets the Plate Number.
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		regPltNo = asRegPltNo;
	}

}
