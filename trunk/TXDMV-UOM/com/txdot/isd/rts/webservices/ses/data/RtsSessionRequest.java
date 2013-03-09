package com.txdot.isd.rts.webservices.ses.data;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

/*
 * RtsSessionRequest.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/27/2010	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	12/29/2010	Add UserName attribute.
 * 							add getUserName(), setUserName()
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	01/11/2011	Remove UserName attribute.
 * 							del getUserName(), setUserName()
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/09/2011	Add Agent Security Identity Number
 * 							to handle selection from multiple agency 
 * 							choice.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/23/2011	Add field for collecting ip addr.
 * 							add csRequestorIpAddr
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/20/2011	Add Validation.
 * 							add validateSesRequest()
 * 							defect 10670 Ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * The web service to do Login for Web Renewal Requests.
 *
 * @version	6.8.0			05/20/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		12/27/2010 10:42:55
 */

public class RtsSessionRequest extends RtsAbstractRequest
{
	private boolean cbDmvUserIndi;
	private int ciAgntSecrtyIdntyNo;
	private String csPswd;
	private String csRequestorIpAddr;
	
	/**
	 * Get the Agent Security Identity Number.
	 * 
	 * @return int
	 */
	public int getAgntSecrtyIdntyNo()
	{
		return ciAgntSecrtyIdntyNo;
	}

	/**
	 * Get Password.
	 * 
	 * @return String
	 */
	public String getPswd()
	{
		return csPswd;
	}

	/**
	 * Get the IpAddr from the Requestor.
	 * 
	 * @return String
	 */
	public String getRequestorIpAddr()
	{
		return csRequestorIpAddr;
	}


	/**
	 * Get the DMV User Identifier.
	 * 
	 * @return boolean
	 */
	public boolean isDmvUserIndi()
	{
		return cbDmvUserIndi;
	}

	/**
	 * Set the Agent Security Identity Number.
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 */
	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;
	}

	/**
	 * Set the DMV User Identifier.
	 * 
	 * @param abDmvUserIndi
	 */
	public void setDmvUserIndi(boolean abDmvUserIndi)
	{
		cbDmvUserIndi = abDmvUserIndi;
	}

	/**
	 * Set Password.
	 * 
	 * @param asPswd
	 */
	public void setPswd(String asPswd)
	{
		csPswd = asPswd;
	}

	/**
	 * Set the IpAddr from the Requestor.
	 * 
	 * @param asRequestorIpAddr
	 */
	public void setRequestorIpAddr(String asRequestorIpAddr)
	{
		csRequestorIpAddr = asRequestorIpAddr;
	}
	
	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateSesRequest() throws RTSException
	{
		RTSException leRTSEx = null;
		
		// Make sure there is a Caller
		if (getCaller() == null || getCaller().length() < 1)
		{
			// Use constant for error number
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Make sure there is a SessionId
		if (getSessionId() == null || getSessionId().length() < 1)
		{
			// Use constant for error number
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		
		// Note that password can be empty if AgntSecrtyIdntyNo is set.
		
		if (getRequestorIpAddr() == null || getRequestorIpAddr().length() < 1)
		{
			// Use constant for error number
			leRTSEx = new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		else if (getRequestorIpAddr().length() > 8)
		{
			setRequestorIpAddr(getRequestorIpAddr().substring(0, 8));
		}
		
		// if there is an exception, throw it.
		if (leRTSEx != null)
		{
			throw leRTSEx;
		}	
	}

}
