package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * CompleteRegRenData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C Chen		09/25/2002	PCR--insurance not required, added 
 * 							cbInsuranceRequired field.
 * Ray Rowehl	04/15/2005	Move package to now be a service
 * 							defect 7705 Ver 5.2.3
 * K Harrell	10/05/2005	Java 1.4 work
 * 							defect 7889 Ver 5.2.3  
 * B Brown		01/27/2011	Add ciMaxAllowRegMos, ciNewRegExpYr,
 * 							cbAllowMulitplySPFee
 * 							getters, setters
 * 							defect 10714 Ver POS_670
 * B Brown		09/29/2011	Add ciSPValidityTerm, cbIsVendorPlate,
 * 							getters and setters.
 * 							defect 10957 Ver POS_690
 * B Brown		10/18/2011	Add caPymntTimeStmp, getters and setters
 * 							defect 10996 Ver POS_690
 * ---------------------------------------------------------------------
 */

/**
 * CompleteRegRenewalData
 * 
 * @version	6.9.0		10/18/2011
 * @author	Cliffor Chen
 * <br>Creation Date:	10/02/2001 10:44:39
 * 
 */
public class CompleteRegRenData implements Serializable
{
	// boolean 
	private boolean cbInsuranceRequired = true;

	// The fees have been preauthorized, 
	// this is the captured status.

	// Defined in CommonConstants:
	// 0=Success, 
	// 1=Renewal Failed, 
	// 2=Convenience Fee Failed, 
	// 3=Both Failed
	private int ciPymntStatusCd;

	// HashMap 	
	private HashMap chmAttributes = new HashMap();

	// Object 
	private VehicleBaseData caVehBaseData;
	private VehicleInsuranceData caVehInsuranceData;
	private VehicleUserData caVehUserData;
	private Vector cvVehRegFeesData;
	private VehicleDescData caVehDescData;

	// String 
	private String csConvFee;
	private String csPaymentAmt;
	private String csPymntOrderId;
	
	// defect 10714
	private int ciMaxAllowRegMos = 0;
	private int ciNewRegExpYr = 0;
	private boolean cbAllowMulitplySPFee = true;
	// end defect 10714
	// defect 10957
	private int ciSPValidityTerm = 0;
	private boolean cbVendorPlate = false;
	// end defect 10957
	// defect 10996
	private String csPymntTimeStmp;
	// end defect 10996

	private final static long serialVersionUID = 2087416281928475091L;

	/**
	 * CompleteRegRenData constructor comment.
	 */
	public CompleteRegRenData()
	{
		super();

		caVehBaseData = new VehicleBaseData();
		caVehInsuranceData = new VehicleInsuranceData();
		caVehUserData = new VehicleUserData();
		cvVehRegFeesData = new Vector();
		caVehDescData = new VehicleDescData();
		csConvFee = "";
		csPymntOrderId = "";
	}
	/**
	 * Formats a long number into a string using NumberFormat.
	 * 
	 * @param double adValue
	 * @return String
	 */
	public String format(double adValue)
	{
		NumberFormat laNF = NumberFormat.getInstance();
		laNF.setMaximumFractionDigits(2);
		laNF.setMinimumFractionDigits(2);
		return laNF.format(adValue);
	}
	/**
	 * Get the specified attribute object.
	 * 
	 * @param String asKey
	 * @return Object
	 */
	public Object getAttribute(String asKey)
	{
		return chmAttributes.get(asKey);
	}
	/**
	 * Get the convenience fee value in double form.
	 * 
	 * @return double
	 */
	public double getConvFee()
	{
		return Double.parseDouble(csConvFee);
	}
	/**
	 * Get the convenience fee value.
	 * 
	 * @return String
	 */
	public String getConvFeeString()
	{
		return csConvFee;
	}

	/**
	 * 
	 * Return value of PymntStatusCd
	 * 
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return ciPymntStatusCd;
	}
	/**
	 * 
	 * Return value of PaymentAmt
	 * 
	 * @return String 
	 */
	public String getPaymentAmtString()
	{
		return csPaymentAmt;
	}
	/**
	 * Return value of PymntOrderId
	 * 
	 * @return String
	 */
	public String getPymntOrderId()
	{
		return csPymntOrderId;
	}
	/**
	 * 
	 * Return value of TotalFees + ConvFee 
	 * 
	 * @return double 
	 */
	public double getTotal()
	{
		return getTotalFees() + getConvFee();
	}
	/**
	 * Return value of TotalFees
	 * 
	 * @return double 
	 */
	public double getTotalFees()
	{
		double ldTotal = 0.0;

		for (int i = 0; i < cvVehRegFeesData.size(); ++i)
		{
			VehicleRegFeesData laRegFeesData =
				(VehicleRegFeesData) cvVehRegFeesData.elementAt(i);
			ldTotal += laRegFeesData.getItemPrice();
		}
		return ldTotal;
	}
	/**
	 * 
	 * Return value of TotalFees
	 * 
	 * @return
	 */
	public String getTotalFeesString()
	{
		return format(getTotalFees());
	}
	/**
	 * 
	 * Return Total
	 * 
	 * @return
	 */
	public String getTotalString()
	{
		return format(getTotal());
	}
	/**
	 * Return value of VehBaseData
	 * 
	 * @return VehicleBaseData
	 */
	public VehicleBaseData getVehBaseData()
	{
		return caVehBaseData;
	}
	/**
	 * Return value of VehDescData
	 * 
	 * @return VehicleDescData
	 */
	public VehicleDescData getVehDescData()
	{
		return caVehDescData;
	}
	/**
	 * Return value of VehInsuranceData
	 * 
	 * @return VehicleInsuranceData
	 */
	public VehicleInsuranceData getVehInsuranceData()
	{
		return caVehInsuranceData;
	}
	/**
	 * Return value of VehRegFeesData
	 *
	 * @return VehicleRegFeesData
	 */
	public Vector getVehRegFeesData()
	{
		return cvVehRegFeesData;
	}
	/**
	 * Return value of VehUserData
	 * 
	 * @return VehicleUserData
	 */
	public VehicleUserData getVehUserData()
	{
		return caVehUserData;
	}
	/**
	 * Return value of InsuranceRequired
	 * 
	 * @return boolean
	 */
	public boolean isInsuranceRequired()
	{
		return cbInsuranceRequired;
	}
	/**
	 * Set Attributes 
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value)
	{
		chmAttributes.put(key, value);
	}
	/**
	 * Set value of ConvFee
	 * 
	 * @param asConvFee String
	 */
	public void setConvFeeString(String asConvFee)
	{
		csConvFee = asConvFee;
	}
	/**
	 * Set value of InsuranceRequired
	 * 
	 * @param abInsuranceRequired boolean
	 */
	public void setInsuranceRequired(boolean abInsuranceRequired)
	{
		cbInsuranceRequired = abInsuranceRequired;
	}
	/**
	 * Set value of PaymentAmt
	 * 
	 * @param asPaymentAmtString String
	 */
	public void setPaymentAmtString(String asPaymentAmtString)
	{
		csPaymentAmt = asPaymentAmtString;
	}
	/**
	 * Set value of PymntOrderId
	 * 
	 * @param aaPymntOrderId String
	 */
	public void setPymntOrderId(String aaPymntOrderId)
	{
		csPymntOrderId = aaPymntOrderId;
	}
	/**
	 * 
	 * Set value of PymntStatusCd
	 * 
	 * @param aiPymntStatusCd
	 */
	public void setPymntStatusCd(int aiPymntStatusCd)
	{
		ciPymntStatusCd = aiPymntStatusCd;
	}
	/**
	 * Set value of VehBaseData 
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 */
	public void setVehBaseData(VehicleBaseData aaVehBaseData)
	{
		caVehBaseData = aaVehBaseData;
	}
	/**
	 * Set value of VehDescData
	 * 
	 * @param aaVehDescData VehicleDescData
	 */
	public void setVehDescData(VehicleDescData aaVehDescData)
	{
		caVehDescData = aaVehDescData;
	}
	/**
	 * Set value of VehInsuranceData
	 * 
	 * @param aaVehInsuranceData VehicleInsuranceData
	 */
	public void setVehInsuranceData(VehicleInsuranceData aaVehInsuranceData)
	{
		caVehInsuranceData = aaVehInsuranceData;
	}
	/**
	 * Set value of VehRegFeesData
	 * 
	 * @param aaVehRegFeesData VehicleRegFeesData
	 */
	public void setVehRegFeesData(Vector aaVehRegFeesData)
	{
		cvVehRegFeesData = aaVehRegFeesData;
	}
	/**
	 * Set value of VehUserData
	 * 
	 * @param aaVehUserData VehicleUserData
	 */
	public void setVehUserData(VehicleUserData aaVehUserData)
	{
		caVehUserData = aaVehUserData;
	}
	/**
	 * get ciMaxAllowRegMos
	 * 
	 * @return
	 */
	public int getMaxAllowRegMos() 
	{
		return ciMaxAllowRegMos;
	}

	/**
	 * set ciMaxAllowRegMos
	 * 
	 * @param i
	 */
	public void setMaxAllowRegMos(int aiMaxAllowRegMos) 
	{
		ciMaxAllowRegMos = aiMaxAllowRegMos;
	}

	/**
	 * get the new reg exp yr
	 * 
	 * @return
	 */
	public int getNewRegExpYr() 
	{
		return ciNewRegExpYr;
	}

	/**
	 * set the new reg exp yr
	 * 
	 * @param i
	 */
	public void setNewRegExpYr(int aiNewRegExpYr) 
	{
		ciNewRegExpYr = aiNewRegExpYr;
	}

	/**
	 * get cbAllowMulitplySPFee
	 * 
	 * @return boolean
	 */
	public boolean isMulitplySPFeeAllowed() 
	{
		return cbAllowMulitplySPFee;
	}	

	/**
	 * set cbAllowMulitplySPFee
	 * 
	 */
	public void setAllowMulitplySPFee (boolean abAllowMulitplySPFee) 
	{
		cbAllowMulitplySPFee = abAllowMulitplySPFee;
	}
	
	/**
	 * get ciSPValidityTerm
	 * 
	 * @return boolean
	 */
	public int getSPValidityTerm() 
	{
		return ciSPValidityTerm;
	}	

	/**
	 * set ciSPValidityTerm
	 * 
	 */
	public void setSPValidityTerm (int aiSPValidityTerm) 
	{
		ciSPValidityTerm = aiSPValidityTerm;
	}
	/**
	 * @return the cbVendorPlate
	 */
	public boolean isVendorPlate()
	{
		return cbVendorPlate;
	}
	/**
	 * @param cbVendorPlate the cbVendorPlate to set
	 */
	public void setVendorPlate(boolean abVendorPlate)
	{
		cbVendorPlate = abVendorPlate;
	}
	/**
	 * @return the caPymntTimeStmp
	 */
	public String getPymntTimeStmp()
	{
		return csPymntTimeStmp;
	}
	/**
	 * @param caPymntTimeStmp the caPymntTimeStmp to set
	 */
	public void setPymntTimeStmp(String asPymntTimeStmp)
	{
		csPymntTimeStmp = asPymntTimeStmp;
	}

}
