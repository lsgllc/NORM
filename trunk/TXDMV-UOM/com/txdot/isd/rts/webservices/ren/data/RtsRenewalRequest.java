package com.txdot.isd.rts.webservices.ren.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

/*
 * RtsRenewalRequest.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/18/2010	Initial load.
 * 							defect 10670 Ver WebSub
 * Ray Rowehl	11/22/2010	Add processing indicators.
 * 							defect 10670 Ver WebSub
 * Ray Rowehl	11/29/2010	Rename cdProcess to cbProcessed.
 * 							defect 10670 Ver WebSub
 * Ray Rowehl	12/14/2010	Add Identity Number.	
 * 							add ciRequestIdntyNo
 * 							add getRequestIdntyNo(), setRequestIdntyNo()
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/07/2011	Add fields for managing invitmno and 
 * 							new reg expiration.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/16/2011	Add Key Type Code.
 * 							defect 10670 Ver 6.7.0
 * K Harrell	03/10/2011	add setKeyTypeCd() 
 * 							defect 10768 Ver 6.7.1 
 * Ray Rowehl	03/22/2011	Add BarCdVersionNo.
 * 							Remove cbProcessed, ciNewRegExpMo, 
 * 								ciNewRegExpYr.
 * 							Make all appropriate getter and setter 
 * 								changes. 
 * 							add csBarCdVersionNo
 * 							defect 10670 Ver 6.7.1
 * K Harrell	04/24/2011	delete setKeyTypeCd(String) 
 * 							defect 10768 Ver 6.7.1 
 * D Hamilton	11/07/2011	add carrOptionalFees 
 * 							defect 11148 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Initial Renewal Request for WebSub.
 *
 * @version	6.9.0 			11/07/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		11/18/2010 08:27:38
 */

public class RtsRenewalRequest extends RtsAbstractRequest
{
	private boolean cbManuallyVerified;
	private int ciRequestIdntyNo;
	private String csBarCdVersionNo;
	private String csDocNo;
	private String csKeyTypeCd;
	private String csLast4OfVin;
	private String csNewInvItmNo;
	private String csRegPltNo;
	// Defect 11148 - optional fees for WebAgent
	private RTSFees[] carrOptionalFees;

	/**
	 * Set the Bar Code Version Number.
	 * 
	 * @return String
	 */
	public String getBarCdVersionNo()
	{
		return csBarCdVersionNo;
	}
	
	/**
	 * Get Document Number.
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Get the Key Type Code for this request.
	 * 
	 * @return String
	 */
	public String getKeyTypeCd()
	{
		return csKeyTypeCd;
	}

	/**
	 * Get Last Four of Vin.
	 * 
	 * @return String
	 */
	public String getLast4OfVin()
	{
		return csLast4OfVin;
	}

	/**
	 * Get the New Inventory Item Number to use.
	 * 
	 * @return String
	 */
	public String getNewInvItmNo()
	{
		return csNewInvItmNo;
	}

	/**
	 * Get the Registration Plate Number.
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Get the Request Identity Number.
	 * 
	 * @return int
	 */
	public int getRequestIdntyNo()
	{
		return ciRequestIdntyNo;
	}

	/**
	 * Set the Insurance Manually Verified indicator.
	 * 
	 * @return boolean
	 */
	public boolean isManuallyVerified()
	{
		return cbManuallyVerified;
	}

	/**
	 * Set the Bar Code Version Number.
	 * 
	 * @param asBarCdVersionNo
	 */
	public void setBarCdVersionNo(String asBarCdVersionNo)
	{
		csBarCdVersionNo = asBarCdVersionNo;
	}

	/**
	 * Set The Document Number (Title Number).
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set the Key Type Code for this request.
	 * 
	 * @param asKeyTypeCd
	 */
	public void setKeyTypeCd(String asKeyTypeCd)
	{
		csKeyTypeCd = asKeyTypeCd;
	}

	/**
	 * Set the Last Four of The VIN.
	 * 
	 * @param asLast4OfVin
	 */
	public void setLast4OfVin(String asLast4OfVin)
	{
		csLast4OfVin = asLast4OfVin;
	}

	/**
	 * Set the Insurance Manually Verified indicator.
	 * 
	 * @param abManuallyVerified
	 */
	public void setManuallyVerified(boolean abManuallyVerified)
	{
		cbManuallyVerified = abManuallyVerified;
	}

	/**
	 * Set the New Inventory Item Number to use.
	 * 
	 * @param asNewInvItmNo
	 */
	public void setNewInvItmNo(String asNewInvItmNo)
	{
		csNewInvItmNo = asNewInvItmNo;
	}

	/**
	 * Set the Registration Plate Number.
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set the Request Identity Number.
	 * 
	 * @param aiRequestIdntyNo
	 */
	public void setRequestIdntyNo(int aiRequestIdntyNo)
	{
		ciRequestIdntyNo = aiRequestIdntyNo;
	}

	/**
	 * @return the carrOptionalFees
	 */
	public RTSFees[] getOptionalFees()
	{
		return carrOptionalFees;
	}

	/**
	 * @param i element of the carrOptionalFees array to get
	 * @return an element of the carrOptionalFees array
	 */
	public RTSFees getOptionalFees(int i)
	{
		return carrOptionalFees[i];
	}

	/**
	 * @param carrOptionalFees the carrOptionalFees to set
	 */
	public void setOptionalFees(RTSFees[] carrOptionalFees)
	{
		this.carrOptionalFees = carrOptionalFees;
	}

	/**
	 * @param i element of the carrOptionalFees array to set
	 * @param aaOptionalFees the element to set in the carrOptionalFees array
	 */
	public void setOptionalFees(int i, RTSFees aaOptionalFees)
	{
		this.carrOptionalFees[i] = aaOptionalFees;
	}

}
