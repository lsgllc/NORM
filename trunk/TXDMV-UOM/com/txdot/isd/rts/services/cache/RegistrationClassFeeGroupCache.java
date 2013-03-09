package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RegistrationClassFeeGroupData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * RegistrationClassFeeGroupCache.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/07/2011	Created
 * 							defect 10695 Ver 6.7.0 
 * K Harrell	01/12/2011  RegClassFeeGrpCd to String
 * 							defect 10695 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * The RegistrationClassFeeGroupCache class provides static methods to 
 * retrieve a RegistrationClassFeeGroupData object based 
 * on different input parameters.
 * 
 * @version	6.7.0		01/12/2011 
 * @author	Kathy Harrell
 * <br>Creation Date: 	12/08/2010 15:51:17  
 */

public class RegistrationClassFeeGroupCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	* A hashtable of vectors with regClassCd as key
	*/
	private static Hashtable shtRegClassFeeGrp = new Hashtable();
	
	static final long serialVersionUID = -5493041855364839335L;

	/**
	 * RegistrationClassFeeGroupCache.java Constructor
	 * 
	 * 
	 */
	public RegistrationClassFeeGroupCache()
	{
		super();
	}

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.REGISTRATION_CLASS_FEE_GRP_CACHE;
	}

	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return the Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtRegClassFeeGrp;
	}

	/**
	 * Returns the RegistrationClassFeeGroupData object based on the input 
	 * String asRegClassFeeGrpCd
	 * 
	 * @param asRegClassFeeGrpCd String 
	 * @param aiVehGrossWt int
	 * @return RegistrationClassFeeGroupData  
	 */
	public static RegistrationClassFeeGroupData getRegisClass(
		String asRegClassFeeGrpCd,
		int aiVehGrossWt)
	{
		RegistrationClassFeeGroupData laRegClassFeeGrpData = null;

		if (shtRegClassFeeGrp.containsKey(asRegClassFeeGrpCd))
		{
			Vector lvRegClassFeeGrpData =
				(Vector) shtRegClassFeeGrp.get(asRegClassFeeGrpCd);

			for (int i = 0; i < lvRegClassFeeGrpData.size(); i++)
			{
				RegistrationClassFeeGroupData laData =
					(
						RegistrationClassFeeGroupData) lvRegClassFeeGrpData
							.get(
						i);

				if ((aiVehGrossWt >= laData.getBegWtRnge())
					&& (aiVehGrossWt <= laData.getEndWtRnge()))
				{
					laRegClassFeeGrpData = laData;
					break;
				}
			}
		}
		return laRegClassFeeGrpData;
	}

	/**
	 * Clear and populate the hashtable with the vector data
	 * 
	 * @param avRegClassFeeGrpData Vector 
	 */
	public void setData(Vector avRegClassFeeGrpData)
	{
		shtRegClassFeeGrp.clear();

		for (int i = 0; i < avRegClassFeeGrpData.size(); i++)
		{
			RegistrationClassFeeGroupData laData =
				(
					RegistrationClassFeeGroupData) avRegClassFeeGrpData
						.get(
					i);

			String lsPrimaryKey = laData.getRegClassFeeGrpCd();

			if (shtRegClassFeeGrp.containsKey(lsPrimaryKey))
			{
				Vector lvRegClassFeeGrpData =
					(Vector) shtRegClassFeeGrp.get(lsPrimaryKey);
				lvRegClassFeeGrpData.add(laData);
			}
			else
			{
				Vector lvRegClassFeeGrpData = new Vector();
				lvRegClassFeeGrpData.add(laData);
				shtRegClassFeeGrp.put(
					lsPrimaryKey,
					lvRegClassFeeGrpData);
			}
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
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtRegClassFeeGrp = ahtHashtable;
	}
}
