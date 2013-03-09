package com.txdot.isd.rts.webservices.veh.data;/* * RtsVehIndicator.java * * (c) Texas Department of Transportation 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	12/14/2010	Created.  * 							defect 10684 Ver 6.7.0  * --------------------------------------------------------------------- *//** * Data object for Indicators  * * @version	6.7.0 			12/14/2010	 * @author	Kathy Harrell  * <br>Creation Date:		12/14/2010 11:09:17  *//* &RtsVehIndicator& */public class RtsVehIndicator{/* &RtsVehIndicator'csIndiName& */	private String csIndiName = "";/* &RtsVehIndicator'csIndiValue& */	private String csIndiValue = "";	/**	 * Get the Indicator Name.	 * 	 * @return String	 *//* &RtsVehIndicator.getIndiName& */	public String getIndiName()	{		return csIndiName;	}	/**	 * Get the Indicator AbstractValue.	 * 	 * @return String	 *//* &RtsVehIndicator.getIndiValue& */	public String getIndiValue()	{		return csIndiValue;	}	/**	 * Set the Indicator Name.	 * 	 * @param asIndiName	 *//* &RtsVehIndicator.setIndiName& */	public void setIndiName(String asIndiName)	{		csIndiName = asIndiName;	}	/**	 * Set the Indicator AbstractValue.	 * 	 * @param asIndiValue	 *//* &RtsVehIndicator.setIndiValue& */	public void setIndiValue(String asIndiValue)	{		csIndiValue = asIndiValue;	}}/* #RtsVehIndicator# */