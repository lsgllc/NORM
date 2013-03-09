package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * PresumptiveValueData.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson	09/08/2006	New class.
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		09/15/2006	Added the Office of Issuance as a value
 * 							that is passed from the client so that
 * 							we can send it to BlackBook for reporting.
 * 							add ciOfficeIssuanceNo
 * 							add setOfficeIssuanceNo(), 
 * 								getOfficeIssuanceNo()
 * 							defect 8926 Ver 5.2.5
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for
 * PresumptiveValueData
 *
 * @version	5.2.5 			09/15/2006
 * @author	Todd Pederson
 * <br>Creation Date:		09/08/2006 10:00:12
 */
public class PresumptiveValueData implements Serializable
{
	static final long serialVersionUID = 6714878882632659278L;
	private Dollar caPrivatePartyValue;
	private int ciOfficeIssuanceNo;
	private int ciVehOdmtrReadng;
	private String csVIN;
	private int ciVehModlYr;
	private String csVehMk;
	private String csVehModl;
	private String csXMLResponse;

	/**
	 * Return value of VehOdmtrReadng
	 * 
	 * @return int
	 */
	public int getOdometerReading()
	{
		return ciVehOdmtrReadng;
	}

	/**
	 * Sets the Office of Issuance that is making the request.
	 * This is used to send tro black book for record keeping
	 * and reporting.
	 * 
	 * @return int
	 */
	public int getOfficeIssuanceNo()
	{
		return ciOfficeIssuanceNo;
	}
	
	/**
	 * Return the Private-Party AbstractValue
	 * 
	 * @return Dollar
	 */
	public Dollar getPrivatePartyValue()
	{
		return caPrivatePartyValue;
	}

	/**
	 * Return value of VehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Return value of VehModl
	 * 
	 * @return String
	 */
	public String getVehModl()
	{
		return csVehModl;
	}

	/**
	 * Return value of VehModlYr
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Return value of Vin
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Return value of XMLResponse
	 * 
	 * @return String
	 */
	public String getXMLResponse()
	{
		return csXMLResponse;
	}

	/**
	 * Set value of VehOdmtrReadng
	 * 
	 * @param aiVehOdmtrReadng String
	 */
	public void setOdometerReading(int aiVehOdmtrReadng)
	{
		ciVehOdmtrReadng = aiVehOdmtrReadng;
	}

	/**
	 * Gets the Office of Issuance that is making the request.
	 * This is used to send tro black book for record keeping
	 * and reporting.
	 * 
	 * @param aiOfficeIssuanceNo int
	 */
	public void setOfficeIssuanceNo(int aiOfficeIssuanceNo)
	{
		ciOfficeIssuanceNo = aiOfficeIssuanceNo;
	}
	
	/**
	 * Set the Private-Party AbstractValue
	 * 
	 * @param aaPrivatePartyValue Dollar
	 */
	public void setPrivatePartyValue(Dollar aaPrivatePartyValue)
	{
		caPrivatePartyValue = aaPrivatePartyValue;
	}

	/**
	 * Set value of VehModlYr
	 * 
	 * @param aiVehModlYr int
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Set value of VehModl
	 * 
	 * @param asVehModl String
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * Set value of VehMk
	 * 
	 * @param asVehMk String
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set value of Vin
	 * 
	 * @param asVIN String
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/**
	 * Set value of XMLResponse
	 * 
	 * @param asXMLResponse String
	 */
	public void setXMLResponse(String asXMLResponse)
	{
		csXMLResponse = asXMLResponse;
	}

}
