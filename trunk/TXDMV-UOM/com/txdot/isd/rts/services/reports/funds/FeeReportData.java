package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * FeeReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/04/2001	New Class
 * K Harrell	04/28/2005	Funds/SQL class review
 * 							defect 8163 Ver 5.2.3
 * S Johnston	05/10/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for
 * FeeReportData
 *  
 * @version		5.2.3	05/10/2005 
 * @author		Min Wang
 * <br>Creation Date:	10/04/2001 04:10:20
 */
public class FeeReportData
	extends FeeTypeReportData
	implements Serializable
{
	protected int ciCustAcctItmQty;
	protected int ciDealerAcctItmQty;
	protected int ciInternetAcctItmQty;
	protected int ciSubconAcctItmQty;
	protected int ciTotalItmQty;
	protected Dollar caCustAcctItmAmt;
	protected Dollar caDealerAcctItmAmt;
	protected Dollar caInternetAcctItmAmt;
	protected Dollar caSubconAcctItmAmt;
	protected Dollar caTotalItmAmt;

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
	 * Get Dealer Account Item Quantity
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
	 * Get Total Item Amount
	 * 
	 * @return Dollar
	 */
	public Dollar getTotalItmAmt()
	{
		return caTotalItmAmt;
	}
	/**
	 * Get Total Item Quantity
	 * 
	 * @return int
	 */
	public int getTotalItmQty()
	{
		return ciTotalItmQty;
	}
	/**
	 * Set Customer Account Item Amount
	 * 
	 * @param aaCustAcctItmAmt Dollar
	 */
	public void setCustAcctItmAmt(Dollar aaCustAcctItmAmt)
	{
		caCustAcctItmAmt = aaCustAcctItmAmt;
	}
	/**
	 * Set Customer Account Item Quantity
	 * 
	 * @param aiCustAcctItmQty int
	 */
	public void setCustAcctItmQty(int aiCustAcctItmQty)
	{
		ciCustAcctItmQty = aiCustAcctItmQty;
	}
	/**
	 * Set Dealer Account Item Amount
	 * 
	 * @param aaDealerAcctItmAmt Dollar
	 */
	public void setDealerAcctItmAmt(Dollar aaDealerAcctItmAmt)
	{
		caDealerAcctItmAmt = aaDealerAcctItmAmt;
	}
	/**
	 * Set Dealer Account Item Quantity
	 * 
	 * @param aiDealerAcctItmQty int
	 */
	public void setDealerAcctItmQty(int aiDealerAcctItmQty)
	{
		ciDealerAcctItmQty = aiDealerAcctItmQty;
	}
	/**
	 * Set Internet Account Item Amount
	 * 
	 * @param aaInternetAcctItmAmt Dollar
	 */
	public void setInternetAcctItmAmt(Dollar aaInternetAcctItmAmt)
	{
		caInternetAcctItmAmt = aaInternetAcctItmAmt;
	}
	/**
	 * Set Internet Account Item Quantity
	 * 
	 * @param aiInternetAcctItmQty int
	 */
	public void setInternetAcctItmQty(int aiInternetAcctItmQty)
	{
		ciInternetAcctItmQty = aiInternetAcctItmQty;
	}
	/**
	 * Set Subcon Account Item Amount
	 * 
	 * @param aaSubconAcctItmAmt Dollar
	 */
	public void setSubconAcctItmAmt(Dollar aaSubconAcctItmAmt)
	{
		caSubconAcctItmAmt = aaSubconAcctItmAmt;
	}
	/**
	 * Set Subcon Account Item Quantity
	 * 
	 * @param aiSubconAcctItmQty int
	 */
	public void setSubconAcctItmQty(int aiSubconAcctItmQty)
	{
		ciSubconAcctItmQty = aiSubconAcctItmQty;
	}
	/**
	 * Set Total Item Amount
	 * 
	 * @param aaTotalItmAmt Dollar
	 */
	public void setTotalItmAmt(Dollar aaTotalItmAmt)
	{
		caTotalItmAmt = aaTotalItmAmt;
	}
	/**
	 * Set Total Item Quantity
	 * 
	 * @param aiTotalItmQty int
	 */
	public void setTotalItmQty(int aiTotalItmQty)
	{
		ciTotalItmQty = aiTotalItmQty;
	}
}
