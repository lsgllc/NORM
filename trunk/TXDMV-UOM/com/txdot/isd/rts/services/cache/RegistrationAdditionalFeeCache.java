package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RegistrationAdditionalFeeData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 *
 * RegistrationAdditionalFeeCache.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/28/2005	Created
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	06/19/2005	Java 1.4 Work 
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/** 
 * RegistrationAdditionalFeeCache class extends GeneralCache and
 * provides static methods to retrieve all registration additional 
 * fees based upon input parameters.
 *
 * @version	5.2.4			06/19/2005
 * @author	Kathy Harrell	
 * <br>Creation Date:		03/28/2005	15:58:41
 */

public class RegistrationAdditionalFeeCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* A hashtable of vectors with regClassCd as key
	*/
	private static Hashtable shtRegAddlFee = new Hashtable();
	private final static long serialVersionUID = -2852729229098029326L;
	/**
	 * RegistrationAdditionalFeeCache constructor comment.
	 */
	public RegistrationAdditionalFeeCache()
	{
		super();
	}
	/**
	 * Return Cache Constant for Cache
	 *
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.REGISTRATION_ADDITIONAL_FEE_CACHE;
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * Class that inherits from Admin cache is required to implement 
	 * this method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtRegAddlFee;
	}
	/**
	 * Return vector of all Registration Additional Fees for a given RegClassCd
	 * and RegEffDate
	 * 
	 * @param  aiRegClassCd int
	 * @param  aiEffDate int
	 * @return Vector
	 */
	public static Vector getRegAddlFee(int aiRegClassCd, int aiEffDate)
	{
		Integer laRegClassCd = new Integer(aiRegClassCd);

		Vector lvRegAddlFee = (Vector) shtRegAddlFee.get(laRegClassCd);

		Vector lvRetRegAddlFee = new Vector();

		if (lvRegAddlFee != null)
		{
			for (int i = 0; i < lvRegAddlFee.size(); i++)
			{
				RegistrationAdditionalFeeData laRegAddlFeeData =
					(RegistrationAdditionalFeeData) lvRegAddlFee.get(i);

				if (laRegAddlFeeData != null)
				{
					if ((aiEffDate >= laRegAddlFeeData.getRTSEffDate())
						&& (aiEffDate
							<= laRegAddlFeeData.getRTSEffEndDate()))
					{
						lvRetRegAddlFee.add(laRegAddlFeeData);
					}
				}
			}
		}
		return lvRetRegAddlFee;
	}
	/**
	 * Clear and populate the hashtable with the vector avRegAddlFeeData
	 * 
	 * @param avRegAddlFeeData Vector
	 */
	public void setData(Vector avRegAddlFeeData)
	{
		//reset the hashtable
		shtRegAddlFee.clear();
		for (int i = 0; i < avRegAddlFeeData.size(); i++)
		{
			RegistrationAdditionalFeeData laRegAddlFeeData =
				(RegistrationAdditionalFeeData) avRegAddlFeeData.get(i);

			int liPrimaryKey = laRegAddlFeeData.getRegClassCd();

			if (shtRegAddlFee.containsKey(new Integer(liPrimaryKey)))
			{
				Vector lvRegAddlFee =
					(Vector) shtRegAddlFee.get(
						new Integer(liPrimaryKey));
				lvRegAddlFee.add(laRegAddlFeeData);
			}
			else
			{
				Vector lvRegAddlFee = new Vector();
				lvRegAddlFee.addElement(laRegAddlFeeData);
				shtRegAddlFee.put(
					new Integer(liPrimaryKey),
					lvRegAddlFee);
			}
		}
	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * Class that inherits from Admin cache is required to implement 
	 * this method.
	 *
	 * @param ahtHashtable Hashtable
	 */
	public void setHashtable(Hashtable ahtRegAddlFee)
	{
		shtRegAddlFee = ahtRegAddlFee;
	}
}
