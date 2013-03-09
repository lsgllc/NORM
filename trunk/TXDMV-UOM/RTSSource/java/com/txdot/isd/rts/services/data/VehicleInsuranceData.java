package com.txdot.isd.rts.services.data;import java.io.Serializable;/* * VehicleInsuranceData.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	10/05/2005	Java 1.4 Work * 							Moved to services.data	 * 							defect 7889 Ver 5.2.3 * --------------------------------------------------------------------- *//** * This Data class contains attributes and get and set methods for  * VehicleMakesData * * @version	5.2.3		10/05/2005 * @author	Administrator  * <br>Creation Date: 	10/02/2001 10:43:50 *//* &VehicleInsuranceData& */public class VehicleInsuranceData implements Serializable{/* &VehicleInsuranceData'csCompanyName& */	private String csCompanyName;/* &VehicleInsuranceData'csPolicyNo& */	private String csPolicyNo;/* &VehicleInsuranceData'csAgentName& */	private String csAgentName;/* &VehicleInsuranceData'csPhoneNo& */	private String csPhoneNo;/* &VehicleInsuranceData'csPolicyStartDt& */	private String csPolicyStartDt;/* &VehicleInsuranceData'csPolicyEndDt& */	private String csPolicyEndDt;	/* &VehicleInsuranceData'serialVersionUID& */	private final static long serialVersionUID = 4933818111901770799L;		/**	 * VehicleInsuranceData constructor comment.	 *//* &VehicleInsuranceData.VehicleInsuranceData& */	public VehicleInsuranceData()	{		super();		csCompanyName = null;		csPolicyNo = null;		csAgentName = null;		csPhoneNo = null;		csPolicyStartDt = null;		csPolicyEndDt = null;	}	/**	 * Return value of AgentName	 * 	 * @return String	 *//* &VehicleInsuranceData.getAgentName& */	public String getAgentName()	{		return csAgentName;	}		/**	 * Return value of CompanyName	 * 	 * @return String	 *//* &VehicleInsuranceData.getCompanyName& */	public String getCompanyName()	{		return csCompanyName;	}		/**	 * Return value of PhoneNo	 * 	 * @return String	 *//* &VehicleInsuranceData.getPhoneNo& */	public String getPhoneNo()	{		return csPhoneNo;	}		/**	 * Return value of PolicyEndDt	 *	 * @return String	 *//* &VehicleInsuranceData.getPolicyEndDt& */	public String getPolicyEndDt()	{		return csPolicyEndDt;	}		/**	 * Return value of PolicyNo 	 * 	 * @return String	 *//* &VehicleInsuranceData.getPolicyNo& */	public String getPolicyNo()	{		return csPolicyNo;	}		/**	 * Return value of PolicyStartDt	 * 	 * @return String	 *//* &VehicleInsuranceData.getPolicyStartDt& */	public String getPolicyStartDt()	{		return csPolicyStartDt;	}		/**	 * Set value of AgentName	 * 	 * @param asAgentName String	 *//* &VehicleInsuranceData.setAgentName& */	public void setAgentName(String asAgentName)	{		csAgentName = asAgentName;	}		/**	 * Set value of CompanyName	 * 	 * @param asCompanyName String	 *//* &VehicleInsuranceData.setCompanyName& */	public void setCompanyName(String asCompanyName)	{		csCompanyName = asCompanyName;	}		/**	 * Set value of PhoneNo	 * 	 * @param asPhoneNo String	 *//* &VehicleInsuranceData.setPhoneNo& */	public void setPhoneNo(String asPhoneNo)	{		csPhoneNo = asPhoneNo;	}		/**	 * Set value of PolicyEndDt	 * 	 * @param asPolicyEndDt String	 *//* &VehicleInsuranceData.setPolicyEndDt& */	public void setPolicyEndDt(String asPolicyEndDt)	{		csPolicyEndDt = asPolicyEndDt;	}		/**	 * Set value of PolicyNo	 * 	 * @param asPolicyNo String	 *//* &VehicleInsuranceData.setPolicyNo& */	public void setPolicyNo(String asPolicyNo)	{		csPolicyNo = asPolicyNo;	}		/**	 * Set value of PolicyStartDt	 *	 * @param asPolicyStartDt String	 *//* &VehicleInsuranceData.setPolicyStartDt& */	public void setPolicyStartDt(String asPolicyStartDt)	{		csPolicyStartDt = asPolicyStartDt;	}}/* #VehicleInsuranceData# */