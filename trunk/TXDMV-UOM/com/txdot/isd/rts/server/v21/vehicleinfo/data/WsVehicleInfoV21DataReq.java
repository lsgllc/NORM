package com.txdot.isd.rts.server.v21.vehicleinfo.data;

/*
 * WsVehicleInfoV21DataReq.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/14/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * Contains Vehicle Information Request Data from V21 to lookup a 
 * Vehicle Record.
 *
 * @version	3_Amigos_PH_A	01/14/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		01/14/2008 11:11:55
 */
public class WsVehicleInfoV21DataReq
{
	/**
	 * Doument Number.
	 * 
	 * <p>If provided, this overrides all other parameters.
	 */
	private String csDocumentNumberReq = "";
	
	/**
	 * Last four charaters of the VIN.
	 * 
	 * <p>Used to confirm the plate number returned.
	 */
	private String csLastFourOfVin = "";
	
	/**
	 * Plate Number.
	 * 
	 * <p>This is the most likely path to getting a record.
	 * Must be paired with the last for of the VIN to find a match.
	 */
	private String csRegPlateReq = "";
	
	/**
	 * Vehicle Identification Number.
	 * 
	 * <p>This is used before plate number if both are provided.
	 */
	private String csVINReq = "";
	
	/**
	 * Used to determine if we want a VINA response when looking up 
	 * by VIN.
	 */
	private boolean cbVinaLookupNeeded = true;
	
	
	/**
	 * WsVehicleInfoV21DataReq.java Constructor
	 */
	public WsVehicleInfoV21DataReq()
	{
		super();
	}

	/**
	 * The requested Document Number
	 * 
	 * @return String
	 */
	public String getDocumentNumberReq()
	{
		return csDocumentNumberReq;
	}

	/**
	 * The requested Last Four characters of the VIN.
	 * 
	 * @return String
	 */
	public String getLastFourOfVin()
	{
		return csLastFourOfVin;
	}

	/**
	 * The requested Plate Number.
	 * 
	 * @return String
	 */
	public String getRegPlateReq()
	{
		return csRegPlateReq;
	}

	/**
	 * The requested VIN.
	 * 
	 * @return String
	 */
	public String getVINReq()
	{
		return csVINReq;
	}
	
	/**
	 * Returns VINA Lookup Needed Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isVinaLookupNeeded()
	{
		return cbVinaLookupNeeded;
	}

	/**
	 * Set the requested Document Number
	 * 
	 * @param asDocumentNumberReq
	 */
	public void setDocumentNumber(String asDocumentNumberReq)
	{
		if (asDocumentNumberReq != null)
		{
			csDocumentNumberReq = asDocumentNumberReq;
		}
		else
		{
			csDocumentNumberReq = "";
		}
	}

	/**
	 * Set the requested Last Four characters of the VIN.
	 * 
	 * @param asLastFourOfVin
	 */
	public void setLastFourOfVin(String asLastFourOfVin)
	{
		if (asLastFourOfVin != null)
		{
			csLastFourOfVin = asLastFourOfVin;
		}
		else
		{
			csLastFourOfVin = "";
		}
	}

	/**
	 * Set the requested Plate Number.
	 * 
	 * @param asRegPlateReq
	 */
	public void setRegPlate(String asRegPlateReq)
	{
		if (asRegPlateReq != null)
		{
			csRegPlateReq = asRegPlateReq;
		}
		else
		{
			csRegPlateReq = "";
		}
	}

	/**
	 * Set the requested VIN.
	 * 
	 * @param asVINReq
	 */
	public void setVIN(String asVINReq)
	{
		if (asVINReq != null)
		{
			csVINReq = asVINReq;
		}
		else
		{
			csVINReq = "";
		}
	}
	
	/**
	 * Set the VINA Lookup boolean.
	 * 
	 * @param abVinaLookupNeeded
	 */
	public void setVinaLookupNeeded(boolean abVinaLookupNeeded)
	{
		cbVinaLookupNeeded = abVinaLookupNeeded;
	}
}
