package com.txdot.isd.rts.client.common.ui;

/*
 *
 * INV007TableData.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							Format comments, Hungarian notation for 
 * 							variables. 
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	10/24/2005	Java Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * INV007 Table Data
 *
 * @version	5.2.3			10/24/2005
 * @author	Michael Abernethy
 * <p>Creation Date:		11/30/2001 10:19:15
 */
public class INV007TableData
{
	private java.lang.String csYear;
	private java.lang.String csDesc;
	private java.lang.String csItemNo;
	/**
	 * INV007TableData constructor comment.
	 */
	public INV007TableData()
	{
		super();
	}
	/**
	 * Return value of csDesc.
	 * @return java.lang.String
	 */
	public java.lang.String getDesc()
	{
		return csDesc;
	}
	/**
	 * Return value of csItemNo.
	 * @return java.lang.String
	 */
	public java.lang.String getItemNo()
	{
		return csItemNo;
	}
	/**
	 * Return value of csYear.
	 * @return java.lang.String
	 */
	public java.lang.String getYear()
	{
		return csYear;
	}
	/**
	 * Set value of asNewDesc.
	 * @param asNewDesc java.lang.String
	 */
	public void setDesc(java.lang.String asNewDesc)
	{
		csDesc = asNewDesc;
	}
	/**
	 * Set value of asNewItemNo.
	 * @param asNewItemNo java.lang.String
	 */
	public void setItemNo(java.lang.String asNewItemNo)
	{
		csItemNo = asNewItemNo;
	}
	/**
	 * Set value of asNewYear.
	 * @param asNewYear java.lang.String
	 */
	public void setYear(java.lang.String asNewYear)
	{
		csYear = asNewYear;
	}
}
