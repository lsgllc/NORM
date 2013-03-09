package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/**
 *
 * MFSpecialRegisData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/18/2005	Move to services.data and deprecate
 *							defect 7705 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Was created to handle Special Regis Data.
 * <br>Not used.
 *
 * @version	5.2.3			02/18/2005
 * @author	Joseph Peters
 * <br>Creation Date:		08/30/2001 15:48:33
 * @deprecated
 */

public class MFSpecialRegisData implements Serializable
{
	private RegistrationData cRegisData;
	private OwnerData cOwnerData;
	private final static long serialVersionUID = 3765828067057004723L;
	/**
	 * MFSpecialRegisData constructor comment.
	 */
	public MFSpecialRegisData()
	{
		super();
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @return com.txdot.isd.rts.client.common.ui.OwnerData
	 */
	public OwnerData getOwnerData()
	{
		return cOwnerData;
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @return com.txdot.isd.rts.client.common.ui.RegistrationData
	 */
	public RegistrationData getRegisData()
	{
		return cRegisData;
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @param newOwnerData com.txdot.isd.rts.client.common.ui.OwnerData
	 */
	public void setOwnerData(OwnerData newOwnerData)
	{
		cOwnerData = newOwnerData;
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @param newRegisData com.txdot.isd.rts.client.common.ui.RegistrationData
	 */
	public void setRegisData(RegistrationData newRegisData)
	{
		cRegisData = newRegisData;
	}
}
