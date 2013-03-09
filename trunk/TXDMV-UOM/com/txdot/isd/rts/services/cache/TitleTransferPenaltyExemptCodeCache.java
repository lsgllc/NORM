package com.txdot.isd.rts.services.cache;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTransferPenaltyExemptCodeData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TitleTransferPenaltyExemptCodeCache.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/22/2008	Recreated
 * 							defect 9724 Ver Defect POS A
 * ---------------------------------------------------------------------
 */

/**
 * The TitleTransferPenaltyExemptCodeCache class provides static methods to 
 * retrieve a particular TitleTransferPenaltyExemptCodeData based 
 * on input parameter.
 *
 * <p>TitleTransferPenaltyExemptCodeCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Defect POS A		06/22/2008
 * @author	K Harrell
 * <br>Creation Date:			06/22/2008	16:16:00
 */
public class TitleTransferPenaltyExemptCodeCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static Hashtable shtTtlTrnsfrPnltyExmptCd = new Hashtable();

	static final long serialVersionUID = -1940072463985936642L;

	/**
	 * TitleTransferEntityCache constructor
	 */
	public TitleTransferPenaltyExemptCodeCache()
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
		return CacheConstant.TTL_TRNSFR_PNLTY_EXMPT_CD_CACHE;
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
		return shtTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * get TitleTransferPenaltyExemptCode Object
	 */
	public static TitleTransferPenaltyExemptCodeData getTitleTransferPenaltyExemptCode(String asTtlTrnsfrPnltyExmptCd)
	{
		Object laObject =
			shtTtlTrnsfrPnltyExmptCd.get(asTtlTrnsfrPnltyExmptCd);
			
		if (laObject != null)
		{
			return (TitleTransferPenaltyExemptCodeData) laObject;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * avTtlTrnsfrPnltyExmptCdData
	 * 
	 * @param avTtlTrnsfrPnltyExmptCdData  Vector
	 */
	public void setData(Vector avTtlTrnsfrPnltyExmptCdData)
	{
		//reset data
		shtTtlTrnsfrPnltyExmptCd.clear();

		for (int i = 0; i < avTtlTrnsfrPnltyExmptCdData.size(); i++)
		{
			TitleTransferPenaltyExemptCodeData laData =
				(
					TitleTransferPenaltyExemptCodeData) avTtlTrnsfrPnltyExmptCdData
						.get(
					i);

			String lsPrimaryKey = laData.getTtlTrnsfrPnltyExmptCd();

			shtTtlTrnsfrPnltyExmptCd.put(lsPrimaryKey, laData);
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
		shtTtlTrnsfrPnltyExmptCd = ahtHashtable;
	}
}
