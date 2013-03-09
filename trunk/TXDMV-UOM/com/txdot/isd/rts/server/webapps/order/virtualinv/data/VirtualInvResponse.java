package com.txdot.isd.rts.server.webapps.order.virtualinv.data;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractResponse;

/*
 * VirtualInvResponse.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		03/05/2007	New class.
 * 							defect 9121 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * This is the VirtualInvResponse class.
 *
 * @version	Special Plates	03/05/2007
 * @author	bbrown
 * <br>Creation Date:		03/05/2007 14:30:00
 */
public class VirtualInvResponse extends AbstractResponse
{
	private String itemCode = null;
	private String itemNo = null;
	private boolean isa = false;
	private String manufacturingPlateNo = null;
	private String reqIPAddress = null;
	private String regPlateNo = null;
	

	/**	
	 * VirtualInvResponse constructor comment.
	 */
	
	public VirtualInvResponse()
	{
		super();
	}


	/**
	 * gets itemCode
	 *
	 * @return String
	 */
	public String getItemCode()
	{
		return itemCode;
	}

	/**
	 * gets itemNo
	 *
	 * @return String
	 */
	public String getItemNo()
	{
		return itemNo;
	}
	
	/**
	 * gets isa indicator
	 *
	 * @return boolean
	 */
	public boolean isIsa()
	{
		return isa;
	}

	/**
	 * gets manufacturingPlateNo
	 *
	 * @return String
	 */
	public String getManufacturingPlateNo()
	{
		return manufacturingPlateNo;
	}

	/**
	 * gets regPlateNo
	 *
	 * @return String
	 */
	public String getRegPlateNo()
	{
		return regPlateNo;
	}

	/**
	 * gets reqIPAddress
	 *
	 * @return String
	 */
	public String getReqIPAddress()
	{
		return reqIPAddress;
	}


	/**
	 * sets itemCode
	 * 
	 * @param asItemCode
	 */
	public void setItemCode(String asItemCode)
	{
		itemCode = asItemCode;
	}

	/**
	 * sets itemNo
	 * 
	 * @param asItemNo
	 */
	public void setItemNo(String asItemNo)
	{
		itemNo = asItemNo;
	}

	/**
	 * sets isa
	 * 
	 * @param abIsa
	 */
	public void setIsa(boolean abIsa)
	{
		isa = abIsa;
	}

	/**
	 * sets manufacturingPlateNo
	 * 
	 * @param asManufacturingPlateNo
	 */
	public void setManufacturingPlateNo(String asManufacturingPlateNo)
	{
		manufacturingPlateNo = asManufacturingPlateNo;
	}

	/**
	 * sets regPlateNo
	 * 
	 * @param asRegPlateNo
	 */
	public void setRegPlateNo(String asRegPlateNo)
	{
		regPlateNo = asRegPlateNo;
	}

	/**
	 * sets reqIPAddress
	 * 
	 * @param asReqIPAddress
	 */
	public void setReqIPAddress(String asReqIPAddress)
	{
		reqIPAddress = asReqIPAddress;
	}

}
