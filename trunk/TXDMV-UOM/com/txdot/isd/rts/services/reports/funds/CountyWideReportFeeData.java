package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * CountyWideReportFeeData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * Ray Rowehl	12/15/2001	new class copied from FeeReportData
 * Ray Rowehl	12/18/2001	new method addUpTotals,
 * 							initializeMoneyAndQty
 * S Johnston	03/11/2005	Code Cleanup for Java 1.4.2 upgrade
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/30/2005	Funds/SQL class review
 * 							defect 8163 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This class contains the data definitions for Fees and Payment
 * used in the CountyWide and Substation Summary Reports.
 *
 * @version	5.2.3 			06/30/2005
 * @author	Ray Rowehl
 * <br>Creation Date:		12/15/2001 20:10:20
 */
public class CountyWideReportFeeData
	extends FeeSummaryReportData
	implements Serializable
{
	// int
	protected int ciCustAcctItmQty;
	protected int ciDealerAcctItmQty;
	protected int ciInternetAcctItmQty;
	protected int ciSubconAcctItmQty;

	// Object
	protected Dollar caCustAcctItmAmt;
	protected Dollar caDealerAcctItmAmt;
	protected Dollar caInternetAcctItmAmt;
	protected Dollar caSubconAcctItmAmt;

	/**
	 * Add the passed in object to the totals object.
	 * 
	 * @param aaDollarLine CountyWideReportFeeData
	 */
	public void addUpTotals(CountyWideReportFeeData aaDollarLine)
	{
		// add up the dollar fields
		caCustAcctItmAmt =
			caCustAcctItmAmt.add(aaDollarLine.getCustAcctItmAmt());
		caDealerAcctItmAmt =
			caDealerAcctItmAmt.add(aaDollarLine.getDealerAcctItmAmt());
		caInternetAcctItmAmt =
			caInternetAcctItmAmt.add(
				aaDollarLine.getInternetAcctItmAmt());
		caSubconAcctItmAmt =
			caSubconAcctItmAmt.add(aaDollarLine.getSubconAcctItmAmt());
		caTotalAcctItmAmt =
			caTotalAcctItmAmt.add(aaDollarLine.getTotalAcctItmAmt());

		// initialize the qty fields
		ciCustAcctItmQty =
			ciCustAcctItmQty + aaDollarLine.getCustAcctItmQty();
		ciDealerAcctItmQty =
			ciDealerAcctItmQty + aaDollarLine.getDealerAcctItmQty();
		ciInternetAcctItmQty =
			ciInternetAcctItmQty + aaDollarLine.getInternetAcctItmQty();
		ciSubconAcctItmQty =
			ciSubconAcctItmQty + aaDollarLine.getSubconAcctItmQty();
		ciTotalAcctItmQty =
			ciTotalAcctItmQty + aaDollarLine.getTotalAcctItmQty();
	}
	/**
	 * Get Customer Account Item Amount
	 * 
	 * @return Dollar
	 */
	public Dollar getCustAcctItmAmt()
	{
		return caCustAcctItmAmt;
	}
	/**
	 * Get Customer Account Item Quantity
	 * 
	 * @return int
	 */
	public int getCustAcctItmQty()
	{
		return ciCustAcctItmQty;
	}
	/**
	 * Get Dealer Account Item Amount
	 * 
	 * @return Dollar
	 */
	public Dollar getDealerAcctItmAmt()
	{
		return caDealerAcctItmAmt;
	}
	/**
	 * getDealerAcctItmQty
	 * 
	 * @return int
	 */
	public int getDealerAcctItmQty()
	{
		return ciDealerAcctItmQty;
	}
	/**
	 * Get Internet Account Item Amount
	 * 
	 * @return Dollar
	 */
	public Dollar getInternetAcctItmAmt()
	{
		return caInternetAcctItmAmt;
	}
	/**
	 * Get Internet Account Item Quantity
	 * 
	 * @return int
	 */
	public int getInternetAcctItmQty()
	{
		return ciInternetAcctItmQty;
	}
	/**
	 * Get Subcon Account Item Amount
	 * 
	 * @return Dollar
	 */
	public Dollar getSubconAcctItmAmt()
	{
		return caSubconAcctItmAmt;
	}
	/**
	 * Get Subcon Account Item Quantity
	 * 
	 * @return int
	 */
	public int getSubconAcctItmQty()
	{
		return ciSubconAcctItmQty;
	}
	/**
	 * Initialize Money and Qty fields to Zero.
	 */
	public void initializeMoneyAndQty()
	{
		// set up the zero dollar
		Dollar laZeroDollar = new Dollar("0.00");

		// initialize the dollar fields
		caCustAcctItmAmt = laZeroDollar;
		caDealerAcctItmAmt = laZeroDollar;
		caInternetAcctItmAmt = laZeroDollar;
		caSubconAcctItmAmt = laZeroDollar;
		caTotalAcctItmAmt = laZeroDollar;

		// initialize the qty fields
		ciCustAcctItmQty = 0;
		ciDealerAcctItmQty = 0;
		ciInternetAcctItmQty = 0;
		ciSubconAcctItmQty = 0;
		ciTotalAcctItmQty = 0;
	}
	/**
	 * Set Customer Account Item Amount
	 * 
	 * @param aaNewCustAcctItmAmt Dollar
	 */
	public void setCustAcctItmAmt(Dollar aaNewCustAcctItmAmt)
	{
		caCustAcctItmAmt = aaNewCustAcctItmAmt;
	}
	/**
	 * Set Customer Account Item Quantity
	 * 
	 * @param aiNewCustAcctItmQty int
	 */
	public void setCustAcctItmQty(int aiNewCustAcctItmQty)
	{
		ciCustAcctItmQty = aiNewCustAcctItmQty;
	}
	/**
	 * Set Dealer Account Item Amount
	 * 
	 * @param aaNewDealerAcctItmAmt Dollar
	 */
	public void setDealerAcctItmAmt(Dollar aaNewDealerAcctItmAmt)
	{
		caDealerAcctItmAmt = aaNewDealerAcctItmAmt;
	}
	/**
	 * Set Dealer Account Item Quantity
	 * 
	 * @param aiNewDealerAcctItmQty int
	 */
	public void setDealerAcctItmQty(int aiNewDealerAcctItmQty)
	{
		ciDealerAcctItmQty = aiNewDealerAcctItmQty;
	}
	/**
	 * Set Internet Account Item Amount
	 * 
	 * @param aaNewInternetAcctItmAmt Dollar
	 */
	public void setInternetAcctItmAmt(Dollar aaNewInternetAcctItmAmt)
	{
		caInternetAcctItmAmt = aaNewInternetAcctItmAmt;
	}
	/**
	 * Set Internet Account Item Quantity
	 * 
	 * @param aiNewInternetAcctItmQty int
	 */
	public void setInternetAcctItmQty(int aiNewInternetAcctItmQty)
	{
		ciInternetAcctItmQty = aiNewInternetAcctItmQty;
	}
	/**
	 * Set Subcon Account Item Amount
	 * 
	 * @param aaNewSubconAcctItmAmt Dollar
	 */
	public void setSubconAcctItmAmt(Dollar aaNewSubconAcctItmAmt)
	{
		caSubconAcctItmAmt = aaNewSubconAcctItmAmt;
	}
	/**
	 * Set Subcon Account Item Quantity
	 * 
	 * @param aiNewSubconAcctItmQty int
	 */
	public void setSubconAcctItmQty(int aiNewSubconAcctItmQty)
	{
		ciSubconAcctItmQty = aiNewSubconAcctItmQty;
	}
}
