package com.txdot.isd.rts.services.data;/* * VehicleInsuranceFrvpData.java * * (c) Texas Department of Transportation 2007 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	11/30/2007	Add class * 							defect 9472 Ver FRVP * --------------------------------------------------------------------- *//** * This is the data received from FRVP in response to a query.   * It extends from VehicleInsuranceData since that partially  * defines this data. * * @version		FRVP		11/30/2007 * @author		Ray Rowehl * <br>Creation Date:		11/30/2007 07:11:55 *//* &VehicleInsuranceFrvpData& */public class VehicleInsuranceFrvpData extends VehicleInsuranceData{	/**	 * Insurance was found for this vehicle.	 *//* &VehicleInsuranceFrvpData'AA_CONFIRMED_MC& */	public static String AA_CONFIRMED_MC = "Confirmed";	/**	 * Indicates insurance was not found for this vehicle.	 *//* &VehicleInsuranceFrvpData'AA_UNCONFIRMED_MC& */	public static String AA_UNCONFIRMED_MC = "Unconfirmed";	/**	 * Indicates vehicle was not even found.	 * Probably not registered.	 *//* &VehicleInsuranceFrvpData'AA_NOTFOUND_MC& */	public static String AA_NOTFOUND_MC = "NotFound";	/**	 * Multiple vehicles were found that match the request.	 *//* &VehicleInsuranceFrvpData'AA_MULTIPLE_MC& */	public static String AA_MULTIPLE_MC = "Multiple";		/**	 * Query Id returned by the FRVP server	 *//* &VehicleInsuranceFrvpData'csQueryId& */	private String csQueryId;		/**	 * String containing the result of the check.	 *//* &VehicleInsuranceFrvpData'csResultsString& */	private String csResultsString;	/**	 * NAIC Code for Insurance Company.	 *//* &VehicleInsuranceFrvpData'csNAIC& */	private String csNAIC;	/**	 * Policy Type.  Private indicates normal individual policy.	 *//* &VehicleInsuranceFrvpData'csPolicyType& */	private String csPolicyType;	/**	 * Ododmeter Reading probably taken at last inspection.	 *//* &VehicleInsuranceFrvpData'csOdometerExp& */	private String csOdometerExp;	/**	 * RegPlate of insured vehicle.	 *//* &VehicleInsuranceFrvpData'csInsuredPlate& */	private String csInsuredPlate;	/**	 * VIN of insured vehicle.	 *//* &VehicleInsuranceFrvpData'csInsuredVin& */	private String csInsuredVin;	/**	 * Registration name on insuder vehicle.	 *//* &VehicleInsuranceFrvpData'csInsuredRegName& */	private String csInsuredRegName;	/**	 * Make of insured vehicle.	 *//* &VehicleInsuranceFrvpData'csInsuredVehMake& */	private String csInsuredVehMake;	/**	 * Model of insured vehicle.	 *//* &VehicleInsuranceFrvpData'csInsuredModel& */	private String csInsuredModel;	/**	 * Model Year of insured vehicle.	 *//* &VehicleInsuranceFrvpData'csInsuredYear& */	private String csInsuredYear;	/**	 * VehicleInsuranceFrvpData.java Constructor	 *//* &VehicleInsuranceFrvpData.VehicleInsuranceFrvpData& */	public VehicleInsuranceFrvpData()	{		super();	}	/**	 * Gets the Vehicle Model Insured.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getInsuredModel& */	public String getInsuredModel()	{		return csInsuredModel;	}	/**	 * Return the plate of the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getInsuredPlate& */	public String getInsuredPlate()	{		return csInsuredPlate;	}	/**	 * Return the Registration Name on the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getInsuredRegName& */	public String getInsuredRegName()	{		return csInsuredRegName;	}	/**	 * Return the Make of the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getInsuredVehMake& */	public String getInsuredVehMake()	{		return csInsuredVehMake;	}	/**	 * Return the VIN of the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getInsuredVin& */	public String getInsuredVin()	{		return csInsuredVin;	}	/**	 * Retuirn the Model Year of the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getInsuredYear& */	public String getInsuredYear()	{		return csInsuredYear;	}	/**	 * Return the NIAC Code for the insurance company.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getNAIC& */	public String getNAIC()	{		return csNAIC;	}	/**	 * Return the Odometer setting of the insured vehicle.	 * 	 * <p>Note that the odometer will be read inspection time.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getOdometerExp& */	public String getOdometerExp()	{		return csOdometerExp;	}	/**	 * Return the type of policy on the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getPolicyType& */	public String getPolicyType()	{		return csPolicyType;	}	/**	 * Return the QueryId assigned by the FRVP server.	 * 	 * <p>Used by support to review the results of queries when the 	 * results are questioned.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getQueryId& */	public String getQueryId()	{		return csQueryId;	}	/**	 * Return the Results of the query on the insured vehicle.	 * 	 * @return String	 *//* &VehicleInsuranceFrvpData.getResultsString& */	public String getResultsString()	{		return csResultsString;	}	/* &VehicleInsuranceFrvpData.isInsured& */	public boolean isInsured()	{		boolean lbInsured = false;				if (csResultsString.equalsIgnoreCase(AA_CONFIRMED_MC))		{			lbInsured = true;		}				return lbInsured;	}	/**	 * Sets the Model of the insured vehicle.	 * 	 * @param asInsuredModel	 *//* &VehicleInsuranceFrvpData.setInsuredModel& */	public void setInsuredModel(String asInsuredModel)	{		csInsuredModel = asInsuredModel;	}	/**	 * Sets the Plate of the insured vehicle.	 * 	 * @param asInsuredPlate	 *//* &VehicleInsuranceFrvpData.setInsuredPlate& */	public void setInsuredPlate(String asInsuredPlate)	{		csInsuredPlate = asInsuredPlate;	}	/**	 * Sets the Registration Name for the insured vehicle.	 * 	 * @param asInsuredRegName	 *//* &VehicleInsuranceFrvpData.setInsuredRegName& */	public void setInsuredRegName(String asInsuredRegName)	{		csInsuredRegName = asInsuredRegName;	}	/**	 * Sets the Make of the insured vehicle.	 * 	 * @param asInsuredVehMake	 *//* &VehicleInsuranceFrvpData.setInsuredVehMake& */	public void setInsuredVehMake(String asInsuredVehMake)	{		csInsuredVehMake = asInsuredVehMake;	}	/**	 * Sets the VIN of the insured vehicle.	 * 	 * @param asInsuredVin	 *//* &VehicleInsuranceFrvpData.setInsuredVin& */	public void setInsuredVin(String asInsuredVin)	{		csInsuredVin = asInsuredVin;	}	/**	 * Set the model Year of the insured vehicle.	 * 	 * @param asInsuredYear	 *//* &VehicleInsuranceFrvpData.setInsuredYear& */	public void setInsuredYear(String asInsuredYear)	{		csInsuredYear = asInsuredYear;	}	/**	 * Set the insuance company's NIAC code.	 * 	 * @param asNAIC	 *//* &VehicleInsuranceFrvpData.setNAIC& */	public void setNAIC(String asNAIC)	{		csNAIC = asNAIC;	}	/**	 * Set the odometer setting.	 * 	 * @param asOdometerExp	 *//* &VehicleInsuranceFrvpData.setOdometerExp& */	public void setOdometerExp(String asOdometerExp)	{		csOdometerExp = asOdometerExp;	}	/**	 * Set the policy type for the insured vehicle.	 * 	 * @param asPolicyType	 *//* &VehicleInsuranceFrvpData.setPolicyType& */	public void setPolicyType(String asPolicyType)	{		csPolicyType = asPolicyType;	}	/**	 * Set the Query Id assigned to the request.	 * 	 * @param asQueryId	 *//* &VehicleInsuranceFrvpData.setQueryId& */	public void setQueryId(String asQueryId)	{		csQueryId = asQueryId;	}	/**	 * Set the Results String from the request response.	 * 	 * @param asResultsString	 *//* &VehicleInsuranceFrvpData.setResultsString& */	public void setResultsString(String asResultsString)	{		csResultsString = asResultsString;	}}/* #VehicleInsuranceFrvpData# */