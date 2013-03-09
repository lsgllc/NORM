package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CreditCardFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * CreditCardFeesCache.java 
 * 
 * (c) Texas Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell  	07/12/2002	Added check for DeleteIndi in 
 * 							getCurrentCreditCardFees()
 * K Harrell	06/15/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * The CreditCardFeesCache class provides the static method to 
 * retrieve a particular CreditCardFeesData based on
 * on OfcIssuanceNo, or OfcIssuanceNo & RTSDate.
 *
 * @version	5.2.3		06/15/2005
 * @author	Michael Abernethy
 * <br>Creation Date: 	04/29/2002 15:56:50  
 *
 */

public class CreditCardFeesCache
	extends AdminCache
	implements java.io.Serializable
{
	private static Hashtable shtCreditCardFee;

	private final static long serialVersionUID = -8286447281767684981L;

	/**
	 * CreditCardFeesCache constructor comment.
	 */
	public CreditCardFeesCache()
	{
		super();
		shtCreditCardFee = new Hashtable();
	}
	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.CREDIT_CARD_FEES_CACHE;
	}
	/**
	 * Get the Credit Card Fees based upon OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubstaId int
	 * @return Vector 
	 */
	public static Vector getCreditCardFees(int aiOfcIssuanceNo)
	{
		if (shtCreditCardFee == null)
		{
			return new Vector();
		}

		Vector lvReturn =
			(Vector) shtCreditCardFee.get("" + aiOfcIssuanceNo);

		if (lvReturn == null)
		{
			return new Vector();
		}
		else
		{
			return lvReturn;
		}
	}
	/**
	 * Get Current Credit Card Fees for OfcIssuanceno, Current Date
	 * 
	 * @param  aiOfcIssuanceNo int
	 * @param  aiCurrentDate 
	 * @return Vector 
	 */
	public static CreditCardFeeData getCurrentCreditCardFees(
		int aiOfcIssuanceNo,
		RTSDate aiCurrentDate)
	{
		if (shtCreditCardFee == null)
		{
			return null;
		}

		Vector lvReturn =
			(Vector) shtCreditCardFee.get("" + aiOfcIssuanceNo);
		if (lvReturn == null)
		{
			return null;
		}

		int liCurrentAMDate = aiCurrentDate.getAMDate();

		for (int i = 0; i < lvReturn.size(); i++)
		{
			CreditCardFeeData laCreditCardFeeData =
				(CreditCardFeeData) lvReturn.get(i);
			int liEffDate =
				laCreditCardFeeData.getRTSEffDate().getAMDate();
			int liEndDate =
				laCreditCardFeeData.getRTSEffEndDate().getAMDate();
			int liDeleteIndi = laCreditCardFeeData.getDeleteIndi();

			if (liCurrentAMDate <= liEndDate
				&& liCurrentAMDate >= liEffDate
				&& liDeleteIndi == 0)
			{
				return laCreditCardFeeData;
			}
		}
		return null;
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtCreditCardFee;
	}

	/**
	 * Clear and populate the hashtable with the vector
	 *  
	 * @param avDataVector 
	 * @throws RTSException 
	 */
	public void setData(Vector avData) throws RTSException
	{
		shtCreditCardFee.clear();
		for (int i = 0; i < avData.size(); i++)
		{
			CreditCardFeeData laCreditCardData =
				(CreditCardFeeData) avData.get(i);
			String lsPrimaryKey =
				"" + laCreditCardData.getOfcIssuanceNo();

			if (shtCreditCardFee.get(lsPrimaryKey) == null)
			{
				shtCreditCardFee.put(lsPrimaryKey, new Vector());
			}
			Vector lvCreditCardFeeData =
				(Vector) shtCreditCardFee.get(lsPrimaryKey);

			lvCreditCardFeeData.add(laCreditCardData);
			shtCreditCardFee.put(lsPrimaryKey, lvCreditCardFeeData);
		}
	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable Hashtable
	 */
	public void setHashtable(Hashtable ahtCreditCardFee)
	{
		shtCreditCardFee = ahtCreditCardFee;
	}
}
