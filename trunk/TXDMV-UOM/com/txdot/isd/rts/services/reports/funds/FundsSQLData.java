package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

/*
 *
 * FundsSQLData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   10/04/2001	No longer extends PaymentSummaryData
 * S Johnston	05/10/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * SQL Data for Funds
 *
 * @version	5.2.3		05/10/2005
 * @author: Kathy Harrell
 * <br>Creation Date:	09/19/2001 15:04:59
 */
public class FundsSQLData implements Serializable
{
	protected String csList;
	protected String csSelect;

	/**
	 * Get List statement
	 * 
	 * @return String
	 */
	public String getList()
	{
		return csList;
	}
	/**
	 * Get Select Statement
	 * 
	 * @return String
	 */
	public String getSelect()
	{
		return csSelect;
	}
	/**
	 * Set List Statement
	 * 
	 * @param asNewList String
	 */
	public void setList(String asNewList)
	{
		csList = asNewList;
	}
	/**
	 * Set Select Statement
	 * 
	 * @param asNewSelect String
	 */
	public void setSelect(String asNewSelect)
	{
		csSelect = asNewSelect;
	}
}
