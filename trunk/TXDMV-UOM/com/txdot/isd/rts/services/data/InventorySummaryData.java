package com.txdot.isd.rts.services.data;


import java.io.Serializable;

/*
 *
 * InventorySummaryData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * 
 * R Hicks		07/12/2002  Add call to closeLastStatement() after a query
 * K Harrell	01/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class variable ciTotalItmQtyReprinted
 * 							and associated getters/setters. 
 * 							Modified from original 5.2.0 to conform to 
 *							naming standards. 
 *							ciTotalItmQtyReprnt vs. TotalItmQtyReprinted
 * 							Ver 5.2.0
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	06/30/2005	Java 1.4 Work	
 * 							Moved from server.db
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * InventorySummaryData
 * 
 * @version	5.2.3		06/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date: 	09/30/2001
 *
 */

public class InventorySummaryData implements Serializable
{
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciInvItmYr;
	protected String csItmCd;
	protected int ciSummaryEffDate;
	protected int ciTotalItmQtySold;
	protected int ciTotalItmQtyVoid;
	protected int ciTotalItmQtyReuse;
	// PCR 34 
	protected int ciTotalItmQtyReprnt;
	// End PCR 34
	
	private final static long serialVersionUID = 307060365537754981L;
	/**
	 * Returns the value of InvItmYr
	 *
	 * @return  int 
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}
	/**
	 * Returns the value of ItmCd
	 *
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
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
	 * This method gets the value of TotalItmQtyReprinted
	 *
	 * @return int
	 */
	public int getTotalItmQtyReprnt()
	{
		return ciTotalItmQtyReprnt;
	}
	/**
	 * Returns the value of TotalItmQtyReuse
	 *
	 * @return  int 
	 */
	public final int getTotalItmQtyReuse()
	{
		return ciTotalItmQtyReuse;
	}
	/**
	 * Returns the value of TotalItmQtySold
	 *
	 * @return  int 
	 */
	public final int getTotalItmQtySold()
	{
		return ciTotalItmQtySold;
	}
	/**
	 * Returns the value of TotalItmQtyVoid
	 *
	 * @return  int 
	 */
	public final int getTotalItmQtyVoid()
	{
		return ciTotalItmQtyVoid;
	}
	/**
	 * This method sets the value of InvItmYr.
	 *
	 * @param aiInvItmYr   int 
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	/**
	 * This method sets the value of ItmCd.
	 *
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
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
	 * This method sets the value of TotalItmQtyReprinted
	 *
	 * @param aiTotalItmQtyReprinted
	 */
	public void setTotalItmQtyReprnt(int aiTotalItmQtyReprnt)
	{
		ciTotalItmQtyReprnt = aiTotalItmQtyReprnt;
	}
	/**
	 * This method sets the value of TotalItmQtyReuse.
	 *
	 * @param aiTotalItmQtyReuse   int 
	 */
	public final void setTotalItmQtyReuse(int aiTotalItmQtyReuse)
	{
		ciTotalItmQtyReuse = aiTotalItmQtyReuse;
	}
	/**
	 * This method sets the value of TotalItmQtySold.
	 *
	 * @param aiTotalItmQtySold   int 
	 */
	public final void setTotalItmQtySold(int aiTotalItmQtySold)
	{
		ciTotalItmQtySold = aiTotalItmQtySold;
	}
	/**
	 * This method sets the value of TotalItmQtyVoid.
	 *
	 * @param aiTotalItmQtyVoid   int 
	 */
	public final void setTotalItmQtyVoid(int aiTotalItmQtyVoid)
	{
		ciTotalItmQtyVoid = aiTotalItmQtyVoid;
	}
}
