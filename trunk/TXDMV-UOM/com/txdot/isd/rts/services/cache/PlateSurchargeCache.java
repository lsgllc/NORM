package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateSurchargeData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * PlateSurchargeCache.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/02/2007	Created
 * 							defect 9085  Ver Special Plates
 * K Harrell	02/09/2007	Added serialUID
 *							defect 9085  Ver Special Plates
 * K Harrell	02/03/2010	Add PltValidityTerm
 * 							add getPltSurcharge(String,String,int,int,int,int) 
 * 							modify getPltSurcharge(String,String,int,int,int) 
 * 							defect 10366 Ver POS_640 
 * B Hargrove	02/24/2010	If look-up with Plate Validity Term, return
 * 							one row for the RegPltCd.
 * 							If look-up without Plate Validity  Term,
 * 							return all rows for the RegPltCd.
 * 							modify both getPltSurcharge()
 * 							defect 10357  Ver 6.4.0  
 * ---------------------------------------------------------------------
 */

/**
 * The PlateSurcharge class provides static methods to 
 * retrieve a PlateSurchargeData based on different input parameters.
 *
 * <p>PlateSurcharge is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Ver 6.4.0 		02/24/2010
 * @author	Kathy Harrell  
 * <br>Creation Date: 		02/02/2007  18:03:00	  
 *
 */

public class PlateSurchargeCache
	extends GeneralCache
	implements java.io.Serializable
{

	static final long serialVersionUID = 6722735019240615059L;

	private static Hashtable shtPlateSurcharge = new Hashtable();

	/**
	 * PlateSurcharge constructor comment.
	 */
	public PlateSurchargeCache()
	{
		super();

	}
	/**
	 * Return the CacheConstant for this cache type.
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.PLT_SURCHARGE_CACHE;
	}

	/**
	 * Gets the vector of PlateSurcharge Data Objects.  Returns 
	 * empty vector if doesn't exist. 
	 * 
	 * @param  asRegPltCd  String
	 * @param  asOrgNo String
	 * @param  aiAddlSetIndi
	 * @param  aiApplIndi
	 * @param  aiEffDate 
	 * @return Vector
	 */
	public static Vector getPltSurcharge(
		String asRegPltCd,
		String asOrgNo,
		int aiAddlSetIndi,
		int aiApplIndi,
		int aiEffDate)
	{
		String lsPrimaryKey =
			UtilityMethods.constructPrimaryKey(
				new String[] {
					asRegPltCd,
					asOrgNo,
					String.valueOf(aiAddlSetIndi),
					String.valueOf(aiApplIndi)});

		Vector lvReturnVector = new Vector();

		// defect 10357
		// Return all current rows for a RegPltCd
		if (shtPlateSurcharge.containsKey(lsPrimaryKey))
		{
			Vector lvPltSurchgData =
				(Vector) shtPlateSurcharge.get(lsPrimaryKey);

			for (int i = 0; i < lvPltSurchgData.size(); i++)
			{
				PlateSurchargeData laData =
					(PlateSurchargeData) lvPltSurchgData.get(i);

				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
					lvReturnVector.addElement(laData);
				}
			}
		}
		// end defect 10357 
		return lvReturnVector;
	}

	/**
	 * Gets the vector of PlateSurcharge Data Objects.  Returns 
	 * empty vector if doesn't exist. 
	 * 
	 * @param  asRegPltCd  String
	 * @param  asOrgNo String
	 * @param  aiAddlSetIndi
	 * @param  aiApplIndi
	 * @param  aiEffDate
	 * @param  aiPltValidityTerm  
	 * @return Vector
	 */
	public static Vector getPltSurcharge(
		String asRegPltCd,
		String asOrgNo,
		int aiAddlSetIndi,
		int aiApplIndi,
		int aiEffDate,
		int aiPltValidityTerm)
	{
		String lsPrimaryKey =
			UtilityMethods.constructPrimaryKey(
				new String[] {
					asRegPltCd,
					asOrgNo,
					String.valueOf(aiAddlSetIndi),
					String.valueOf(aiApplIndi)});

		Vector lvReturnVector = new Vector();

		// defect 10357
		// Return one row for a RegPltCd / Plate Valididity Term
		if (shtPlateSurcharge.containsKey(lsPrimaryKey))
		{
			Vector lvPltSurchgData =
				(Vector) shtPlateSurcharge.get(lsPrimaryKey);

			for (int i = 0; i < lvPltSurchgData.size(); i++)
			{
				PlateSurchargeData laData =
					(PlateSurchargeData) lvPltSurchgData.get(i);

				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate())
					&& laData.getPltValidityTerm() == aiPltValidityTerm)
				{
					lvReturnVector.addElement(laData);
				}
			}
		}
		// end defect 10357
		return lvReturnVector;
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
		return shtPlateSurcharge;
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * avPlateSurchargeData 
	 * 
	 * @param avPlateSurchargeData	Vector
	 */
	public void setData(Vector avPlateSurchargeData)
	{
		//reset the hashtable
		shtPlateSurcharge.clear();

		if (avPlateSurchargeData == null)
		{
			return;
		}

		// Populate shtPlateSurcharge 
		for (int i = 0; i < avPlateSurchargeData.size(); i++)
		{
			PlateSurchargeData laData =
				(PlateSurchargeData) avPlateSurchargeData.get(i);

			String lsPrimaryKey =
				UtilityMethods.constructPrimaryKey(
					new String[] {
						laData.getRegPltCd(),
						laData.getOrgNo(),
						String.valueOf(laData.getAddlSetIndi()),
						String.valueOf(laData.getApplIndi())});

			if (shtPlateSurcharge.containsKey(lsPrimaryKey))
			{
				Vector lvPlateSurchargeData =
					(Vector) shtPlateSurcharge.get(lsPrimaryKey);
				lvPlateSurchargeData.add(laData);
			}
			else
			{
				Vector lvPlateSurchargeData = new Vector();
				lvPlateSurchargeData.add(laData);
				shtPlateSurcharge.put(
					lsPrimaryKey,
					lvPlateSurchargeData);
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
		shtPlateSurcharge = ahtHashtable;
	}
}
