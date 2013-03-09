package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * SubstationSummaryData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 * 							query
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work	
 * 							Moved from server.db
 * 							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * SubstationSummaryData
 * 
 * @version	5.2.3		06/30/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	09/17/2001 17:47:30 
 */

public class SubstationSummaryData implements Serializable
{
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciSummaryEffDate;
	
	protected RTSDate caSummaryTimestmp;

	private final static long serialVersionUID = 5893554786576137894L;
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
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of SummaryEffDate
	 * 
	 * @return  int 
	 */
	public final int getSummaryEffDate()
	{
		return ciSummaryEffDate;
	}
	/**
	 * Returns the value of SummaryTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getSummaryTimestmp()
	{
		return caSummaryTimestmp;
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
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of SummaryEffDate.
	 * 
	 * @param aiSummaryEffDate   int 
	 */
	public final void setSummaryEffDate(int aiSummaryEffDate)
	{
		ciSummaryEffDate = aiSummaryEffDate;
	}
	/**
	 * This method sets the value of SummaryTimestmp.
	 * 
	 * @param aaSummaryTimestmp   RTSDate 
	 */
	public final void setSummaryTimestmp(RTSDate aaSummaryTimestmp)
	{
		caSummaryTimestmp = aaSummaryTimestmp;
	}
}
