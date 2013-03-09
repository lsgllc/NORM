package com.txdot.isd.rts.webservices.dsabldplcrd.data;

import java.io.Serializable;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

/*
 * RtsDsabldPlcrdRequest.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/27/2010	Created
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	10/03/2010	add isValidRequest() 
 * 							defect 10607 Ver 6.6.0
 * K Harrell	10/23/2010	added check for SessionId <= 30
 * 							defect 10607 Ver 6.6.0 
 * R Pilon		02/02/2012	Add missing setter to prevent web service validation
 * 							  error.
 * 							add setValidRequest()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Request data for Disabled Placard Request
 *
 * @version	6.10.0			02/02/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		09/27/2010	14:52:17 
 */
public class RtsDsabldPlcrdRequest
	extends RtsAbstractRequest
	implements Serializable
{
	/**
	 * RtsDsabldPlcrdRequest.java Constructor
	 */
	public RtsDsabldPlcrdRequest()
	{
		super();
	}

	private String csInvItmNo;

	static final long serialVersionUID = 76828692562656852L;

	/**
	 * Get value of csInvItmNo
	 * 
	 * @return String 
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Set value of csInvItmNo
	 * 
	 * @param asInvItmNo
	 */
	public void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}

	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @returns boolean 
	 */
	public boolean isValidRequest()
	{
		return getCaller() != null
			&& getCaller().length() > 0
			&& getCaller().length() <= 30
			&& getSessionId() != null
			&& getSessionId().length() > 0
			&& getSessionId().length() <= 30
			&& csInvItmNo != null
			&& csInvItmNo.length() > 0
			&& csInvItmNo.length() <= 10
			&& getAction() == 92
			&& getVersionNo() == 0;
	}

	/**
	 * Null setter method required to prevent web services validation 
	 * error.
	 * 
	 * @param abValidRequest
	 */
	public void setValidRequest(boolean abValidRequest)
	{
		// null setter
	}
}
