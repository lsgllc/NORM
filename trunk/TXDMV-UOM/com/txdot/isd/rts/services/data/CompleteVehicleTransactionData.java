package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import java.util.Vector;

/*
 *
 * CompleteVehicleTransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/22/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * Data object in using Complete Vehicle Transaction under
 * Miscellaneous.
  * 
 * @version	5.2.3		04/22/2005 
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001	13:30:59
 */

public class CompleteVehicleTransactionData implements Serializable
{
	// boolean 
	private boolean cbMultipleTrans;

	// Object 
	private MFVehicleData caMFVehicleData;
	private TransactionHeaderData caTransactionHeader;

	// Vector 
	private Vector cvRegFeesData;
	private Vector cvProcessInventoryData;
	private Vector cvTransIdList;

	/**
	 * CompleteVehicleTransactionData constructor comment.
	 */
	public CompleteVehicleTransactionData()
	{
		super();
	}
	/**
	 * Return value of MFVehicleData
	 * 
	 * @return MFVehicleData
	 */
	public MFVehicleData getMFVehicleData()
	{
		return caMFVehicleData;
	}

	/**
	 * Return value of ProcessInventoryData
	 * 
	 * @return Vector
	 */
	public Vector getProcessInventoryData()
	{
		return cvProcessInventoryData;
	}

	/**
	 * Return value of RegFeesData
	 * 
	 * @return Vector
	 */
	public Vector getRegFeesData()
	{
		return cvRegFeesData;
	}

	/**
	 * Return value of TransactionHeader
	 * 
	 * @return caTransactionHeader
	 */
	public TransactionHeaderData getTransactionHeader()
	{
		return caTransactionHeader;
	}

	/**
	 * Return value of TransIdList
	 * 
	 * @return Vector
	 */
	public Vector getTransIdList()
	{
		return cvTransIdList;
	}

	/**
	 * Return value of MultipleTrans
	 * 
	 * @return boolean
	 */
	public boolean isMultipleTrans()
	{
		return cbMultipleTrans;
	}

	/**
	 * Set value of MFVehicleData
	 * 
	 * @param aaMFVehicleData MFVehicleData
	 */
	public void setMFVehicleData(MFVehicleData aaMFVehicleData)
	{
		caMFVehicleData = aaMFVehicleData;
	}

	/**
	 * Set value of MultipleTrans 
	 * 
	 * @param abMultipleTrans boolean
	 */
	public void setMultipleTrans(boolean abMultipleTrans)
	{
		cbMultipleTrans = abMultipleTrans;
	}

	/**
	 * Set value of ProcessInventoryData
	 * 
	 * @param avProcessInventoryData Vector
	 */
	public void setProcessInventoryData(Vector avProcessInventoryData)
	{
		cvProcessInventoryData = avProcessInventoryData;
	}

	/**
	 * Set value of RegFeesData
	 * 
	 * @param avRegFeesData Vector
	 */
	public void setRegFeesData(Vector avRegFeesData)
	{
		cvRegFeesData = avRegFeesData;
	}

	/**
	 * Set value of TransactionHeader
	 * 
	 * @param aaTransactionHeader TransactionHeaderData
	 */
	public void setTransactionHeader(TransactionHeaderData aaTransactionHeader)
	{
		caTransactionHeader = aaTransactionHeader;
	}

	/**
	 * Set value of TransIdList
	 * 
	 * @param avTransIdList Vector
	 */
	public void setTransIdList(Vector avTransIdList)
	{
		cvTransIdList = avTransIdList;
	}
}
