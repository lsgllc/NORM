package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.server.webapps.order.common.data.Fees;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

/*
 * SpecialPltOrdrCart.java
 *
 * (c) Texas Department of Transportation 2007
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/16/2006	Created Class.
 * 							defect 9120 Ver Special Plates
 * Bob B.		03/16/2008	Add serialVersionUID
 * 							defect 9599 Ver Tres Amigos Prep
 * B Brown		03/25/2008	Let WSAD generate serialVersionUID  
 * 							defect 9599 Ver Tres Amigos Prep  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class used by the special plate order system to hold
 * the shopping cart information and all of the utlility methods
 * needed to maintain the shopping cart.
 * 
 * @version	Tres Amigos Prep	03/25/2008
 * @author	Jeff Seifert
 * <br>Creation Date:			03/16/2006 07:25:00
 */

public class SpecialPltOrdrCart implements Serializable
{
	// defect 9599
	// private final static long serialVersionUID = 8465561818851210316L;	
    static final long serialVersionUID = 5402274083001112198L;	
	// end defect 9599
    // Used to automaticlly create a POS transaction on update

	// instead of letting batch do so.  Used for testing.
	private boolean cbCreatePOSTrans = false;
	private RTSDate caEpayRcveTimeStmp;
	private RTSDate caEpaySendTimeStmp;
	// Individual Convenience Fee
	double cdUnitConvFee = 0.00;
	// Default the payment status code to in process.
	private int ciItrntPymntStatusCd = CommonConstants.PAYMENT_IN_PROCESS;
	private String csPymntOrderID = "";
	private String csTraceNo = "";
	// Vector of Shopping Cart Item(s)
	private Vector cvCartItems;
	

	/**
	 * SpecialPltOrdrCart constructor comment.
	 */
	public SpecialPltOrdrCart()
	{
		super();
		cvCartItems = new Vector();
	}

	/**
	 * Add to vector of SpecialPlate Shopping Cart Items. 
	 * 
	 * @param aaSPOItem SpecialPltOrdrCartItem
	 */
	public void add(SpecialPltOrdrCartItem aaSPOItem)
	{
		cvCartItems.add(aaSPOItem);
	}

	/**
	 * Gets the shopping cart item.
	 * 
	 * @param aiItem int
	 * @return SpecialPltOrdrCartItem
	 */
	public SpecialPltOrdrCartItem elementAt(int aiItem)
	{
		return (SpecialPltOrdrCartItem) cvCartItems.elementAt(aiItem);
	}

	/**
	 * Return the vector of .
	 * 
	 * @return int 
	 */
	public Vector getCopyOfCartItems()
	{
		return (Vector) UtilityMethods.copy(cvCartItems);
	}

	/**
	 * Return the Convenience Fee Total.
	 * 
	 * @return double
	 */
	public double getConvFeeTotal()
	{
		return cvCartItems.size() * cdUnitConvFee;
	}

	/**
	 * Return the Convenience Fee Total.
	 * Returns UtilityMethods.format1() of the value.
	 * 
	 * @return String
	 */
	public String getConvFeeTotalNumberString()
	{
		return UtilityMethods.format1(getConvFeeTotal());
	}

	/**
	 * Return the Convenience Fee Total.
	 * Returns UtilityMethods.format() of the value.
	 * 
	 * @return String
	 */
	public String getConvFeeTotalString()
	{
		return UtilityMethods.format(getConvFeeTotal());
	}

	/**
	 * Gets the date and time that we received an epay response.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEpayRcveTimeStmp()
	{
		return caEpayRcveTimeStmp;
	}

	/**
	 * Gets the date and time that we sent an epay request.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEpaySendTimeStmp()
	{
		return caEpaySendTimeStmp;
	}

	/**
	 * Returns the item price for a given fee. 
	 * 
	 * @param asFee String
	 * @param aarrFeeses Fees[]
	 * @return double
	 */
	public static double getFee(String asFee, Fees[] aarrFeeses)
	{
		double ldFee = 0.0;
		
		for (int i = 0; i < aarrFeeses.length; i++)
		{
			if (aarrFeeses[i].getDesc().equals(asFee))
			{
				ldFee = aarrFeeses[i].getItemPrice();
				break;
			}
		}
		
		return ldFee;
	}
	
	/**
	 * Returns the Fees Object for a given fee. 
	 * 
	 * @param asFee String
	 * @param aarrFeeses Fees[]
	 * @return Fees
	 */
	public Fees getFeeObj(String asFee, Fees[] aarrFeeses)
	{
		Fees laFees = null;
		
		for (int i = 0; i < aarrFeeses.length; i++)
		{
			if (aarrFeeses[i].getDesc().equals(asFee))
			{
				laFees = aarrFeeses[i];
				break;
			}
		}
		
		return laFees;
	}
	
	/**
	 * Calculate the total fees associated with the shopping cart.
	 * This total does not include the convenience fees.
	 * 
	 * @return double
	 */
	public double getFeesTotal()
	{
		double ldTotalFees = 0;
		for (int i = 0; i < cvCartItems.size(); i++)
		{
			ldTotalFees += getItemFeesTotal(i);
		}
		return ldTotalFees;
	}

	/**
	 * Calculate the total fees associated with the shopping cart.
	 * This total does not include the convenience fees.
	 * Returns UtilityMethods.format1() of the value.
	 * 
	 * @return String
	 */
	public String getFeesTotalNumberString()
	{
		return UtilityMethods.format1(getFeesTotal());
	}

	/**
	 * Calculate the total fees associated with the shopping cart.
	 * This total does not include the convenience fees.
	 * Returns UtilityMethods.format() of the value.
	 * 
	 * @return String
	 */
	public String getFeesTotalString()
	{
		return UtilityMethods.format(getFeesTotal());
	}

	/**
	 * Calculate the total fees associated with the Item in the shopping
	 * cart. This total does not include the convenience fees.
	 * 
	 * @param aiItem int
	 * @return double
	 */
	public double getItemFeesTotal(int aiItem)
	{
		double ldSubTotalFees = 0;
		SpecialPltOrdrCartItem laCartItem = elementAt(aiItem);

		// Decide what fee to use. Single or Additional
		if (laCartItem.isAdditionalSet())
		{
			ldSubTotalFees =
				getFee(
					ServiceConstants.SPI_FEE_ADD_SET,
					laCartItem.getSpecialPlateSelection().getFees());
		}
		else
		{
			ldSubTotalFees =
				getFee(
					ServiceConstants.SPI_FEE_SINGLE_SET,
					laCartItem.getSpecialPlateSelection().getFees());
		}

		// Add the PLP fee.
		if (laCartItem.isPLP())
		{
			ldSubTotalFees
				+= getFee(
					ServiceConstants.SPI_FEE_PERSONALIZATION,
					laCartItem.getSpecialPlateSelection().getFees());
		}

		return ldSubTotalFees;
	}

	/**
	 * Method calls getItemFeesTotal(int) but will lookup the item in
	 * the cart that you are looking for comparing the shopping cart 
	 * item objects.
	 * 
	 * @param aaItem SpecialPltOrdrCartItem
	 * @return double
	 */
	public double getItemFeesTotal(SpecialPltOrdrCartItem aaItem)
	{
		double ldTotalFees = 0;
		if (aaItem != null)
		{
			for (int i = 0; i < cvCartItems.size(); i++)
			{
				if (aaItem
					.equals(
						(SpecialPltOrdrCartItem) cvCartItems.elementAt(
							i)))
				{
					ldTotalFees = getItemFeesTotal(i);
					break;
				}
			}
		}

		return ldTotalFees;
	}

	/**
	 * Calculate the total fees associated with the Item in the shopping
	 * cart. This total does not include the convenience fees.
	 * Returns UtilityMethods.format1() of the value.
	 * 
	 * @param aiItem int
	 * @return String
	 */
	public String getItemFeesTotalNumberString(int aiItem)
	{
		return UtilityMethods.format1(getItemFeesTotal(aiItem));
	}

	/**
	 * Calculate the total fees associated with the Item in the shopping
	 * cart. This total does not include the convenience fees.
	 * Returns UtilityMethods.format() of the value.
	 * 
	 * @param aiItem int
	 * @return String
	 */
	public String getItemFeesTotalString(int aiItem)
	{
		return UtilityMethods.format(getItemFeesTotal(aiItem));
	}

	/**
	 * Calculate the special plate fee. This total does not include 
	 * the convenience fees.
	 * 
	 * @param aiItem int
	 * @return double
	 */
	public double getItemSpecialPltFee(int aiItem)
	{
		double ldSubTotalFees = 0;
		SpecialPltOrdrCartItem laCartItem = elementAt(aiItem);

		// Decide what fee to use. Single or Additional
		if (laCartItem.isAdditionalSet())
		{
			ldSubTotalFees =
				getFee(
					ServiceConstants.SPI_FEE_ADD_SET,
					laCartItem.getSpecialPlateSelection().getFees());
		}
		else
		{
			ldSubTotalFees =
				getFee(
					ServiceConstants.SPI_FEE_SINGLE_SET,
					laCartItem.getSpecialPlateSelection().getFees());
		}

		return ldSubTotalFees;
	}
	
	/**
	 * Gets the Special Plate Fees array for the given cart item.
	 * 
	 * @param aaItem SpecialPltOrdrCartItem
	 * @return Fees[]
	 */
	public Fees[] getItemSpecialPltFees(SpecialPltOrdrCartItem aaItem)
	{
		Fees[] larrFees = null;
		
		if (aaItem.isPLP())
		{
			larrFees = new Fees[2];
			larrFees[1] =
				getFeeObj(
					ServiceConstants.SPI_FEE_PERSONALIZATION,
					aaItem.getSpecialPlateSelection().getFees());
		}
		else
		{
			larrFees = new Fees[1];
		}

		// Decide what fee to use. Single or Additional
		if (aaItem.isAdditionalSet())
		{
			larrFees[0] =
				getFeeObj(
					ServiceConstants.SPI_FEE_ADD_SET,
					aaItem.getSpecialPlateSelection().getFees());
		}
		else
		{
			larrFees[0] =
				getFeeObj(
					ServiceConstants.SPI_FEE_SINGLE_SET,
					aaItem.getSpecialPlateSelection().getFees());
		}

		return larrFees;
	}

	/**
	 * Calculate the special plate fee. This total does not include 
	 * the convenience fees.
	 * Returns UtilityMethods.format1() of the value.
	 * 
	 * @param aiItem int
	 * @return String
	 */
	public String getItemSpecialPltFeeNumberString(int aiItem)
	{
		return UtilityMethods.format1(getItemSpecialPltFee(aiItem));
	}

	/**
	 * Calculate the special plate fee. This total does not include 
	 * the convenience fees.
	 * Returns UtilityMethods.format() of the value.
	 * 
	 * @param aiItem int
	 * @return String
	 */
	public String getItemSpecialPltFeeString(int aiItem)
	{
		return UtilityMethods.format(getItemSpecialPltFee(aiItem));
	}

	/**
	 * Calculate the total fees associated with the shopping cart item,
	 * including convenience fees.
	 *
	 * @param aiItem int
	 * @return double
	 */
	public double getItemTotal(int aiItem)
	{
		return getItemFeesTotal(aiItem) + cdUnitConvFee;
	}

	/**
	 * Calculate the total fees associated with the shopping cart item,
	 * including convenience fees.
	 * Returns UtilityMethods.format1() of the value.
	 *
	 * @param aiItem int
	 * @return String
	 */
	public String getItemTotalNumberString(int aiItem)
	{
		return UtilityMethods.format1(getItemTotal(aiItem));
	}

	/**
	 * Calculate the total fees associated with the shopping cart item,
	 * including convenience fees.
	 * Returns UtilityMethods.format() of the value.
	 *
	 * @param aiItem int
	 * @return String
	 */
	public String getItemTotalString(int aiItem)
	{
		return UtilityMethods.format(getItemTotal(aiItem));
	}

	/**
	 * Gets the payment status code.
	 * 
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return ciItrntPymntStatusCd;
	}

	/**
	 * Gets the payment order ID.
	 * 
	 * @return String
	 */
	public String getPymntOrderID()
	{
		return csPymntOrderID;
	}

	/**
	 * Calculate the total fees associated with the shopping cart,
	 * including convenience fees.
	 *
	 * @return double
	 */
	public double getTotal()
	{
		return getFeesTotal() + getConvFeeTotal();
	}

	/**
	 * Calculate the total fees associated with the shopping cart,
	 * including convenience fees.
	 * Returns UtilityMethods.format1() of the value.
	 *
	 * @return String
	 */
	public String getTotalNumberString()
	{
		return UtilityMethods.format1(getTotal());
	}

	/**
	 * Calculate the total fees associated with the shopping cart,
	 * including convenience fees.
	 * Returns UtilityMethods.format() of the value.
	 *
	 * @return String
	 */
	public String getTotalString()
	{
		return UtilityMethods.format(getTotal());
	}

	/**
	 * Gets the Internet Trace Number
	 * 
	 * @return String
	 */
	public String getTraceNo()
	{
		return csTraceNo;
	}

	/**
	 * Gets the cost of the convenience fee.
	 * 
	 * @return double
	 */
	public double getUnitConvFee()
	{
		return cdUnitConvFee;
	}

	/**
	 * Determines if we are going to create a POS Transaction upon
	 * update of a successfull internet transaction or are we going to
	 * let batch do so.  This is used for testing.
	 * 
	 * @return boolean
	 */
	public boolean isCreatePOSTrans()
	{
		return cbCreatePOSTrans;
	}

	/**
	 * Returns if the shopping cart is empty.
	 * 
	 * @return boolean 
	 */
	public boolean isEmpty()
	{
		return (cvCartItems.size() == 0);
	}

	/**
	 * Removes a shopping cart item.
	 * 
	 * @param aiItem int
	 */
	public void remove(int aiItem)
	{
		cvCartItems.remove(aiItem);
	}

	/**
	 * Determines if we are going to create a POS Transaction upon
	 * update of a successfull internet transaction or are we going to
	 * let batch do so.  This is used for testing.
	 * 
	 * @param abCreatePOSTrans boolean
	 */
	public void setCreatePOSTrans(boolean abCreatePOSTrans)
	{
		cbCreatePOSTrans = abCreatePOSTrans;
	}

	/**
	 * Sets the date and time that we sent received an epay response.
	 * 
	 * @param aaEpayRcveTimeStmp RTSDate
	 */
	public void setEpayRcveTimeStmp(RTSDate aaEpayRcveTimeStmp)
	{
		caEpayRcveTimeStmp = aaEpayRcveTimeStmp;
	}

	/**
	 * Sets the date and time that we sent an epay request.
	 * 
	 * @param aaEpaySendTimeStmp RTSDate
	 */
	public void setEpaySendTimeStmp(RTSDate aaEpaySendTimeStmp)
	{
		caEpaySendTimeStmp = aaEpaySendTimeStmp;
	}

	/**
	 * Sets the payment status code.
	 * 
	 * @param aiItrntPymntStatusCd int
	 */
	public void setItrntPymntStatusCd(int aiItrntPymntStatusCd)
	{
		ciItrntPymntStatusCd = aiItrntPymntStatusCd;
	}

	/**
	 * Sets the Payment Order Id.
	 * 
	 * @param asPymntOrderID String
	 */
	public void setPymntOrderID(String asPymntOrderID)
	{
		csPymntOrderID = asPymntOrderID;
	}

	/**
	 * Sets the Internet Trace Number.
	 * 
	 * @param asTraceNo String
	 */
	public void setTraceNo(String asTraceNo)
	{
		csTraceNo = asTraceNo;
	}

	/**
	 * Sets the cost of the convenience fee.
	 * 
	 * @param adUnitConvFee double
	 */
	public void setUnitConvFee(double adUnitConvFee)
	{
		cdUnitConvFee = adUnitConvFee;
	}

	/**
	 * Return size of the shoping cart.
	 * 
	 * @return int 
	 */
	public int size()
	{
		return cvCartItems.size();
	}

}
