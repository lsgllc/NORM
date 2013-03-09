package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;

/*
 *
 * RenewalShoppingCart.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add csCntyNo, cdRenwlPrice 
 *							and get/set methods
 * 							add details for method descriptions
 * 									Ver 5.2.0	
 * J Rue		06/08/2004 	Add Barcode Type and Version
 *							getter/setter for Barcode Utility
 *							add getVersion(), setVersion
 *							getType(), setType()
 *							Defect 7108,  ver 5.2.1
 * B Hargrove	07/29/2004 	Add getter/setters for acct codes and
 *							fees 8 - 15.									
 *							add fields (ie: csAcctItmCd8, cfItmPrice8)
 *							add getters, setters
 *							defect 7348  Ver 5.2.1
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	10/10/2005	Moved to services.data
 * 							defect 7889 Ver 5.2.3 
 * B Brown		10/18/2011	Add getter/setters for new CompleteRegRenData
 * 							pymnttimestmp field.
 * 							defect 10996 Ver 6.9.0
 * S Carlin		11/01/2011	Add flag to indicate if shopping cart items need
 * 							to be validated against the database.
 * 							defect 10945 Ver 6.9.0
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * RenewalShoppingCart
 * 
 * 
 * @version	6.9.0		11/01/2011
 * @author	Administrator
 * <br>Creation Date:	11/02/2001 17:37:16
 */

public class RenewalShoppingCart implements Serializable
{
	private Vector cvItems;

	// individual convenience fee
	double cdUnitConvFee;

	private final static long serialVersionUID = 8465561818851217811L;
	
	/**
	 * RenewalShoppingCart constructor comment.
	 */
	public RenewalShoppingCart()
	{
		super();
		cvItems = new Vector();
	}
	/**
	 * RenewalShoppingCart constructor comment.
	 * 
	 * @param aiInitialCapacity int
	 */
	public RenewalShoppingCart(int aiInitialCapacity)
	{
		cvItems = new Vector(aiInitialCapacity);
	}
	/**
	 * RenewalShoppingCart constructor comment.
	 * 
	 * @param aiInitialCapacity int
	 * @param aiCapacityIncrement int
	 */
	public RenewalShoppingCart(
		int aiInitialCapacity,
		int aiCapacityIncrement)
	{
		cvItems = new Vector(aiInitialCapacity, aiCapacityIncrement);
	}
	/**
	 * 
	 * Add to vector of Items 
	 * 
	 * @param aaObj
	 */
	public void add(Object aaObj)
	{
		cvItems.add(aaObj);
	}
	/**
	 * 
	 * ElementAt 
	 * 
	 * @param i
	 * @return
	 */
	public Object elementAt(int i)
	{
		return cvItems.elementAt(i);
	}
	/**
	 * 
	 * Return the Convenience Fee 
	 * 
	 * @return
	 */
	public double getConvFeeTotal()
	{
		return cvItems.size() * cdUnitConvFee;
	}
	/**
	 * 
	 * Return the Convenience Fee Total 
	 * 
	 * @return
	 */
	public String getConvFeeTotalNumberString()
	{
		return UtilityMethods.format1(getConvFeeTotal());
	}
	/**
	 * 
	 * Return the Convenience Fee Total - String
	 * 
	 * @return
	 */
	public String getConvFeeTotalString()
	{
		return UtilityMethods.format(getConvFeeTotal());
	}
	/**
	 * Calculate the total fees associated with this registration 
	 * renewal shopping, excluding convenience fees.
	 * C
	 * @return double
	 */
	public double getFeesTotal()
	{
		double subTotalFees = 0;
		for (int i = 0; i < cvItems.size(); i++)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(i);
			subTotalFees += lCompRegRenData.getTotalFees();
		}
		return subTotalFees;

	}
	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public String getFeesTotalNumberString()
	{
		return UtilityMethods.format1(getFeesTotal());
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @return String
	 */
	public String getFeesTotalString()
	{
		return UtilityMethods.format(getFeesTotal());
	}
	/**
	 * Calculate the total fees associated with the registration
	 * renewal shopping, including convenience fees.
	 *
	 * @return double
	 */
	public double getTotal()
	{
		return getFeesTotal() + getConvFeeTotal();
	}
	public String getTotalNumberString()
	{
		return UtilityMethods.format1(getTotal());
	}
	/**
	 * Return String value of Total 
	 * 
	 * @return String
	 */
	public String getTotalString()
	{
		return UtilityMethods.format(getTotal());
	}
	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public String getTraceNo()
	{
		String lsTraceNo = null;
		if (cvItems.size() > 0)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(0);
			lsTraceNo =
				lCompRegRenData.getVehUserData().getTraceNumber();
		}
		return lsTraceNo;
	}
	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public double getUnitConvFee()
	{
		return cdUnitConvFee;
	}
	/**
	 * 
	 * Method description
	 * 
	 * @param i
	 */
	public void remove(int i)
	{
		cvItems.remove(i);
	}
	/**
	 * 
	 * Method description
	 * 
	 * @param newPymntOrderId
	 */
	public void setPymntOrderId(String newPymntOrderId)
	{
		for (int i = 0; i < cvItems.size(); i++)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(i);
			lCompRegRenData.setPymntOrderId(newPymntOrderId);
		}
	}
	/**
	 * 
	 * Method description
	 * 
	 * @param newPymntStatusCd
	 */
	public void setPymntStatusCd(int newPymntStatusCd)
	{
		for (int i = 0; i < cvItems.size(); i++)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(i);
			lCompRegRenData.setPymntStatusCd(newPymntStatusCd);
		}
	}
	/**
	 * Set the trace number 
	 *  
	 * @param asTraceNo String
	 */
	public void setTraceNo(String asTraceNo)
	{

		for (int i = 0; i < cvItems.size(); ++i)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(i);
			lCompRegRenData.getVehUserData().setTraceNumber(asTraceNo);
		}
	}
	/**
	 * 
	 * Method description
	 * 
	 * @param adUnitConvFee
	 */
	public void setUnitConvFee(double adUnitConvFee)
	{
		cdUnitConvFee = adUnitConvFee;
	}
	/**
	 * 
	 * Return size of the shoping cart
	 * 
	 * @return int 
	 */
	public int size()
	{
		return cvItems.size();
	}
	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public String getPymntDate()
	{
		String lsPymntDate = null;
		if (cvItems.size() > 0)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(0);
			lsPymntDate =
				lCompRegRenData.getPymntTimeStmp();
		}
		return lsPymntDate;
	}
	
	/**
	 * 
	 * Method description
	 * 
	 * @param newPymntStatusCd
	 */
	public void setPymntDate(String asPymntDate)
	{
		for (int i = 0; i < cvItems.size(); i++)
		{
			CompleteRegRenData lCompRegRenData =
				(CompleteRegRenData) cvItems.elementAt(i);
			lCompRegRenData.setPymntTimeStmp(asPymntDate);
		}
	}

	//defect 10945
	private boolean cbNeedToCompareFees = false;

	/**
	 * @return the needToCompareFees
	 */
	public boolean getNeedToCompareFees()
	{
		return cbNeedToCompareFees;
	}
	/**
	 * @param needToCompareFees the needToCompareFees to set
	 */
	public void setNeedToCompareFees(boolean needToCompareFees)
	{
		this.cbNeedToCompareFees = needToCompareFees;
	}
	//end defect 10945

}
