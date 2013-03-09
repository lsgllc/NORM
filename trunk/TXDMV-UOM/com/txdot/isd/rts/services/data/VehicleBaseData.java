package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleBaseData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/05/2005	Java 1.4 Work
 * 							Moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * B Brown		05/15/2006  Since this class is part of the Refund
 *  						Data class, add constructor
 * 							client.webapps.data.VehicleBaseData to 
 * 							convert the 5.2.2 TxO VehicleBaseData 
 * 							object back to 5.2.3 
 * 							services.data.VehicleData object.
 * 							add constructor VehicleBaseData(VehicleBase
 * 							Data).
 * 							defect 8777 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleBaseData
 *
 * @version	5.2.3		05/15/2006
 * @author	Administrator 
 * <br>Creation Date: 	10/02/2001 10:43:30 
 */

public class VehicleBaseData implements Serializable
{
	private String csPlateNo;
	private String csVin;
	private String csDocNo;
	private String csOwnerCountyNo;

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
		com.txdot.isd.rts.client.webapps.data.VehicleBaseData aaVehicleBaseData)
	{
		this.setDocNo(aaVehicleBaseData.getDocNo());
		this.setVin(aaVehicleBaseData.getDocNo());
		this.setOwnerCountyNo(aaVehicleBaseData.getOwnerCountyNo());
		this.setPlateNo(aaVehicleBaseData.getPlateNo());
	}
	/**
	 * Return value of DocNo
	 *  
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * 
	 * Return value of OwnerCounty
	 * 
	 * @return int
	 */
	public int getOwnerCounty()
	{
		return (new Integer(csOwnerCountyNo)).intValue();
	}
	/**
	 * Return value of OwnerCountyNo
	 * 
	 * @return String
	 */
	public String getOwnerCountyNo()
	{
		return csOwnerCountyNo;
	}
	/**
	 * Return value of PlateNo
	 * 
	 * @return String
	 */
	public String getPlateNo()
	{
		return csPlateNo;
	}
	/**
	 * Return value of Vin
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}
	/**
	 * Set value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * Set value of OwnerCountyNo
	 * 
	 * @param asOwnerCountyNo String
	 */
	public void setOwnerCountyNo(String asOwnerCountyNo)
	{
		csOwnerCountyNo = asOwnerCountyNo;
	}
	/**
	 * Set value of PlateNo
	 * 
	 * @param asPlateNo String
	 */
	public void setPlateNo(String asPlateNo)
	{
		csPlateNo = asPlateNo;
	}
	/**
	 * Set value of Vin
	 * 
	 * @param asVin String
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
}
