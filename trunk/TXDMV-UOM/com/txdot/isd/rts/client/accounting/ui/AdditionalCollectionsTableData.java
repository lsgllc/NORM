package com.txdot.isd.rts.client.accounting.ui;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * AdditionalCollectionsTableData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			09/07/2001	Added comments
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	04/27/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * A container class for the information that is used in the table in 
 * ACC001.
 *
 * @version	5.2.3		04/27/2005  
 * @author	Michael Abernethy
 * <br>Creation Date:	08/06/2001 09:41:42
 */

public class AdditionalCollectionsTableData
{
	// int 
	private int ciQty;
	private int ciCreditIndi;

	// Object 
	private Dollar caTotal;

	// String 
	private String csFee;
	private String csFeeCd;

	/**
	 * Creates an AdditionalCollectionsTableData.
	 */
	public AdditionalCollectionsTableData()
	{
		super();
	}
	/**
	 * Creates an AdditionalCollectionsTableData
	 * 
	 * @param asFee		String
	 * @param aiQty 	int
	 * @param aaTotal 	Dollar 
	 */
	public AdditionalCollectionsTableData(
		String asFee,
		int aiQty,
		Dollar aaTotal)
	{
		this.csFee = asFee;
		this.ciQty = aiQty;
		this.caTotal = aaTotal;
	}
	/**
	 * Returns the credit indi.
	 * 
	 * @return int
	 */
	public int getCreditIndi()
	{
		return ciCreditIndi;
	}
	/**
	 * Returns the fee.
	 * 
	 * @return String
	 */
	public String getFee()
	{
		if (csFee == null)
		{
			return "";
		}
		else
		{
			return csFee;
		}
	}
	/**
	 * Returns the fee code.
	 * 
	 * @return String
	 */
	public String getFeeCd()
	{
		return csFeeCd;
	}
	/**
	 * Returns the quantity
	 * 
	 * @return int
	 */
	public int getQty()
	{
		return ciQty;
	}
	/**
	 * Returns the total
	 * 
	 * @return Dollar
	 */
	public Dollar getTotal()
	{
		if (caTotal == null)
		{
			return new Dollar("0.00");
		}
		else
		{
			return caTotal;
		}
	}
	/**
	 * Sets the credit indicator.
	 * 
	 * @param aiCreditIndi	int 
	 */
	public void setCreditIndi(int aiCreditIndi)
	{
		ciCreditIndi = aiCreditIndi;
	}
	/**
	 * Sets the fee
	 * 
	 * @param asFee	String 
	 */
	public void setFee(String asFee)
	{
		csFee = asFee;
	}
	/**
	 * Sets the fee code.
	 * 
	 * @param asFeeCd	String 
	 */
	public void setFeeCd(String asFeeCd)
	{
		csFeeCd = asFeeCd;
	}
	/**
	 * Sets the quantity
	 * 
	 * @param aiQty	int 
	 */
	public void setQty(int aiQty)
	{
		ciQty = aiQty;
	}
	/**
	 * Sets the total
	 * 
	 * @param aaTotal	Dollar 
	 */
	public void setTotal(Dollar aaTotal)
	{
		caTotal = aaTotal;
	}
}
