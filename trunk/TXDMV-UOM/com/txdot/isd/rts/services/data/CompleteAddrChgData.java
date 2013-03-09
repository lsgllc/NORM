package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * CompleteAddrChgData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	04/15/2005	Change package to reflect that this is a 
 * 							service class.
 * 							defect 7705 Ver 5.2.3
 * K Harrell	10/05/2005	Move to services.data
 * 							defect 7889 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Holds Internet Address Change data.
 * 
 * @version	5.2.3		10/05/2005
 * @author	Clifford Chen
 * <br>Creation Date:	10/04/2001 16:14:22
 */
public class CompleteAddrChgData implements Serializable
{

	// String
	private String csOldOwnrCntyNo;

	// Object 
	private VehicleBaseData caVehBaseData;
	private VehicleUserData caVehUserData;
	private VehicleDescData caVehDescData;
	private VehicleUserData caOldVehUserData;

	private final static long serialVersionUID = -6244680097790837913L;

	/**
	 * CompleteAddressChgData constructor comment.
	 */
	public CompleteAddrChgData()
	{
		super();
	}
	/**
	 * Get OldOwnrCntyNo.
	 * 
	 * @return String
	 */
	public String getOldOwnrCntyNo()
	{
		return csOldOwnrCntyNo;
	}
	/**
	 * Get OldVehUserData
	 * 
	 * @return VehicleUserData
	 */
	public VehicleUserData getOldVehUserData()
	{
		return caOldVehUserData;
	}
	/**
	 * Get VehBaseData
	 * 
	 * @return VehicleBaseData
	 */
	public VehicleBaseData getVehBaseData()
	{
		return caVehBaseData;
	}
	/**
	 * Get VehDescData
	 * 
	 * @return VehicleDescData
	 */
	public VehicleDescData getVehDescData()
	{
		return caVehDescData;
	}
	/**
	 * Get VehUserData
	 * 
	 * @return VehicleUserData
	 */
	public VehicleUserData getVehUserData()
	{
		return caVehUserData;
	}
	/**
	 * Set OldOwnrCntyNo
	 * 
	 * @param aaNewOldOwnrCntyNo String
	 */
	public void setOldOwnrCntyNo(String aaNewOldOwnrCntyNo)
	{
		csOldOwnrCntyNo = aaNewOldOwnrCntyNo;
	}
	/**
	 * Set OldVehUserData
	 * 
	 * @param aaNewOldVehUserData VehicleUserData
	 */
	public void setOldVehUserData(VehicleUserData aaNewOldVehUserData)
	{
		caOldVehUserData = aaNewOldVehUserData;
	}
	/**
	 * Set VehBaseData
	 * 
	 * @param aaNewVehBaseData VehicleBaseData
	 */
	public void setVehBaseData(VehicleBaseData aaNewVehBaseData)
	{
		caVehBaseData = aaNewVehBaseData;
	}
	/**
	 * Set VehDescData
	 * 
	 * @param aaNewVehDescData VehicleDescData
	 */
	public void setVehDescData(VehicleDescData aaNewVehDescData)
	{
		caVehDescData = aaNewVehDescData;
	}
	/**
	 * Set VehUserData
	 * 
	 * @param aaNewVehUserData VehicleUserData
	 */
	public void setVehUserData(VehicleUserData aaNewVehUserData)
	{
		caVehUserData = aaNewVehUserData;
	}
}
