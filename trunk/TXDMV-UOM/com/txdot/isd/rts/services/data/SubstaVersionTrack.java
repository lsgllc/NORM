package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * SubstaVersionTrack.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang 	07/27/2006	Created Class - See which version of code is 
 * 							expected at a substation.
 *							defect 7726 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * This contains the data pertaining to version of code at a substation.
 *
 * @version	5.2.4 		07/27/2006
 * @author	min wang
 * <br>Creation Date:	07/27/2006 13:41:00
 */
public class SubstaVersionTrack implements Serializable
{
	protected int ciOfcIssuanceNo;
	protected int ciSubStaId;
	protected int ciVersionTrackNo;
	
	private final static long serialVersionUID = 8573817843645547727L;

	/**
	 * Returns the value of OfcIssuanceNo
	 *
	 * @return  int 
	 */

	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of VersionTrackNo
	 *
	 * @return  int 
	 */

	public final int getVersionTrackNo()
	{
		return ciVersionTrackNo;
	}

	/**
	 * Returns the value of SubStaId
	 *
	 * @return  int 
	 */

	public final int getSubStaId()
	{
		return ciSubStaId;
	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 *
	 * @param aiOfcIssuanceNo   int 
	 */

	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of SubStaId.
	 *
	 * @param aiSubStaId   int 
	 */

	public final void setSubStaId(int aiSubStaId)
	{
		ciSubStaId = aiSubStaId;
	}
	/**
	 * This method sets the value of SubStaId.
	 *
	 * @param aiSubStaId   int 
	 */

	public final void setVersionTrackNo(int aiVersionTrackNo)
	{
		ciVersionTrackNo = aiVersionTrackNo;
	}
}
