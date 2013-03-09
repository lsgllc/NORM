package com.txdot.isd.rts.services.data;
/*
 * VehicleInsuranceFrvpData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/30/2007	Add class
 * 							defect 9472 Ver FRVP
 * ---------------------------------------------------------------------
 */

/**
 * This is the data received from FRVP in response to a query.  
 * It extends from VehicleInsuranceData since that partially 
 * defines this data.
 *
 * @version		FRVP		11/30/2007
 * @author		Ray Rowehl
 * <br>Creation Date:		11/30/2007 07:11:55
 */
public class VehicleInsuranceFrvpData extends VehicleInsuranceData
{
	/**
	 * Insurance was found for this vehicle.
	 */
	public static String AA_CONFIRMED_MC = "Confirmed";
	/**
	 * Indicates insurance was not found for this vehicle.
	 */
	public static String AA_UNCONFIRMED_MC = "Unconfirmed";
	/**
	 * Indicates vehicle was not even found.
	 * Probably not registered.
	 */
	public static String AA_NOTFOUND_MC = "NotFound";
	/**
	 * Multiple vehicles were found that match the request.
	 */
	public static String AA_MULTIPLE_MC = "Multiple";
	
	/**
	 * Query Id returned by the FRVP server
	 */
	private String csQueryId;
	
	/**
	 * String containing the result of the check.
	 */
	private String csResultsString;
	/**
	 * NAIC Code for Insurance Company.
	 */
	private String csNAIC;
	/**
	 * Policy Type.  Private indicates normal individual policy.
	 */
	private String csPolicyType;
	/**
	 * Ododmeter Reading probably taken at last inspection.
	 */
	private String csOdometerExp;
	/**
	 * RegPlate of insured vehicle.
	 */
	private String csInsuredPlate;
	/**
	 * VIN of insured vehicle.
	 */
	private String csInsuredVin;
	/**
	 * Registration name on insuder vehicle.
	 */
	private String csInsuredRegName;
	/**
	 * Make of insured vehicle.
	 */
	private String csInsuredVehMake;
	/**
	 * Model of insured vehicle.
	 */
	private String csInsuredModel;
	/**
	 * Model Year of insured vehicle.
	 */
	private String csInsuredYear;

	/**
	 * VehicleInsuranceFrvpData.java Constructor
	 */
	public VehicleInsuranceFrvpData()
	{
		super();
	}

	/**
	 * Gets the Vehicle Model Insured.
	 * 
	 * @return String
	 */
	public String getInsuredModel()
	{
		return csInsuredModel;
	}

	/**
	 * Return the plate of the insured vehicle.
	 * 
	 * @return String
	 */
	public String getInsuredPlate()
	{
		return csInsuredPlate;
	}

	/**
	 * Return the Registration Name on the insured vehicle.
	 * 
	 * @return String
	 */
	public String getInsuredRegName()
	{
		return csInsuredRegName;
	}

	/**
	 * Return the Make of the insured vehicle.
	 * 
	 * @return String
	 */
	public String getInsuredVehMake()
	{
		return csInsuredVehMake;
	}

	/**
	 * Return the VIN of the insured vehicle.
	 * 
	 * @return String
	 */
	public String getInsuredVin()
	{
		return csInsuredVin;
	}

	/**
	 * Retuirn the Model Year of the insured vehicle.
	 * 
	 * @return String
	 */
	public String getInsuredYear()
	{
		return csInsuredYear;
	}

	/**
	 * Return the NIAC Code for the insurance company.
	 * 
	 * @return String
	 */
	public String getNAIC()
	{
		return csNAIC;
	}

	/**
	 * Return the Odometer setting of the insured vehicle.
	 * 
	 * <p>Note that the odometer will be read inspection time.
	 * 
	 * @return String
	 */
	public String getOdometerExp()
	{
		return csOdometerExp;
	}

	/**
	 * Return the type of policy on the insured vehicle.
	 * 
	 * @return String
	 */
	public String getPolicyType()
	{
		return csPolicyType;
	}

	/**
	 * Return the QueryId assigned by the FRVP server.
	 * 
	 * <p>Used by support to review the results of queries when the 
	 * results are questioned.
	 * 
	 * @return String
	 */
	public String getQueryId()
	{
		return csQueryId;
	}

	/**
	 * Return the Results of the query on the insured vehicle.
	 * 
	 * @return String
	 */
	public String getResultsString()
	{
		return csResultsString;
	}
	
	public boolean isInsured()
	{
		boolean lbInsured = false;
		
		if (csResultsString.equalsIgnoreCase(AA_CONFIRMED_MC))
		{
			lbInsured = true;
		}
		
		return lbInsured;
	}

	/**
	 * Sets the Model of the insured vehicle.
	 * 
	 * @param asInsuredModel
	 */
	public void setInsuredModel(String asInsuredModel)
	{
		csInsuredModel = asInsuredModel;
	}

	/**
	 * Sets the Plate of the insured vehicle.
	 * 
	 * @param asInsuredPlate
	 */
	public void setInsuredPlate(String asInsuredPlate)
	{
		csInsuredPlate = asInsuredPlate;
	}

	/**
	 * Sets the Registration Name for the insured vehicle.
	 * 
	 * @param asInsuredRegName
	 */
	public void setInsuredRegName(String asInsuredRegName)
	{
		csInsuredRegName = asInsuredRegName;
	}

	/**
	 * Sets the Make of the insured vehicle.
	 * 
	 * @param asInsuredVehMake
	 */
	public void setInsuredVehMake(String asInsuredVehMake)
	{
		csInsuredVehMake = asInsuredVehMake;
	}

	/**
	 * Sets the VIN of the insured vehicle.
	 * 
	 * @param asInsuredVin
	 */
	public void setInsuredVin(String asInsuredVin)
	{
		csInsuredVin = asInsuredVin;
	}

	/**
	 * Set the model Year of the insured vehicle.
	 * 
	 * @param asInsuredYear
	 */
	public void setInsuredYear(String asInsuredYear)
	{
		csInsuredYear = asInsuredYear;
	}

	/**
	 * Set the insuance company's NIAC code.
	 * 
	 * @param asNAIC
	 */
	public void setNAIC(String asNAIC)
	{
		csNAIC = asNAIC;
	}

	/**
	 * Set the odometer setting.
	 * 
	 * @param asOdometerExp
	 */
	public void setOdometerExp(String asOdometerExp)
	{
		csOdometerExp = asOdometerExp;
	}

	/**
	 * Set the policy type for the insured vehicle.
	 * 
	 * @param asPolicyType
	 */
	public void setPolicyType(String asPolicyType)
	{
		csPolicyType = asPolicyType;
	}

	/**
	 * Set the Query Id assigned to the request.
	 * 
	 * @param asQueryId
	 */
	public void setQueryId(String asQueryId)
	{
		csQueryId = asQueryId;
	}

	/**
	 * Set the Results String from the request response.
	 * 
	 * @param asResultsString
	 */
	public void setResultsString(String asResultsString)
	{
		csResultsString = asResultsString;
	}

}
