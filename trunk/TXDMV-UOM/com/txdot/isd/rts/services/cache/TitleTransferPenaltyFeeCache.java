package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTransferPenaltyFeeData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TitleTransferPenaltyFeeCache.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/02/2008 	Created 
 * 							defect 9583 Ver Defect POS A 
 * K Harrell	05/25/2008	Renamed *WorkDays* to *CalndrDays* 
 * 							modified getTitleTransferPenaltyFeeCache()
 * 							defect 9583 Ver Defect POS A 
 * ---------------------------------------------------------------------
 */

/**
 * The TitleTransferPenaltyFeeCache class provides static methods to 
 * retrieve a particular TitleTransferPenaltyFeeData based 
 * on different input parameters.
 *
 * <p>TitleTransferPenaltyFeeCache is being initialized and 
 * populated by the CacheManager when the system starts up.  The data 
 * will be stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Defect POS A		05/25/2008
 * @author	K Harrell
 * <br>Creation Date:			04/02/2008	16:32:00
 */
public class TitleTransferPenaltyFeeCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static Hashtable shtTtlTrnsfrPnltyFee = new Hashtable();

	private static final long serialVersionUID = -2661914003783187800L;

	/**
	 * TitleTransferPenaltyFeeCache constructor
	 */
	public TitleTransferPenaltyFeeCache()
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
		return CacheConstant.TTL_TRNSFR_PNLTY_FEE_CACHE;
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
		return shtTtlTrnsfrPnltyFee;
	}

	/**
	 * get TitleTransferPenaltyFeeData Object
	 */
	public static TitleTransferPenaltyFeeData getTitleTransferPenaltyFeeCache(
		String asTtlTrnsfrEntCd,
		int aiEffDate,
		int aiCalndrDaysCt)
	{
		TitleTransferPenaltyFeeData laReturnData = null;

		if (shtTtlTrnsfrPnltyFee.containsKey(asTtlTrnsfrEntCd))
		{
			Vector lvData =
				(Vector) shtTtlTrnsfrPnltyFee.get(asTtlTrnsfrEntCd);

			for (int i = 0; i < lvData.size(); i++)
			{
				TitleTransferPenaltyFeeData laData =
					(TitleTransferPenaltyFeeData) lvData.get(i);

				// If aiEffDate between RTSEffDate & RTSEffEndData   
				// and 
				// aiCalndrDaysCt between BegCalndrDaysCount & 
				//    EndCalndrDaysCount
				if (((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
					&& ((aiCalndrDaysCt >= laData.getBegCalndrDaysCount())
						&& (aiCalndrDaysCt
							<= laData.getEndCalndrDaysCount())))
				{
					laReturnData = laData;
					break;
				}
			}
		}
		return laReturnData;
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * avTtlTrnsfrPnltyFeeData
	 * 
	 * @param avTtlTrnsfrPnltyFeeData  Vector
	 */
	public void setData(Vector avTtlTrnsfrPnltyFeeData)
	{
		//reset data
		shtTtlTrnsfrPnltyFee.clear();

		for (int i = 0; i < avTtlTrnsfrPnltyFeeData.size(); i++)
		{
			TitleTransferPenaltyFeeData laTtlTrnsfrPnltyFeeData =
				(
					TitleTransferPenaltyFeeData) avTtlTrnsfrPnltyFeeData
						.get(
					i);

			String lsPrimaryKey =
				laTtlTrnsfrPnltyFeeData.getTtlTrnsfrEntCd();

			if (shtTtlTrnsfrPnltyFee.containsKey(lsPrimaryKey))
			{
				Vector lvTtlTrnsfrPnltyFeeData =
					(Vector) shtTtlTrnsfrPnltyFee.get(lsPrimaryKey);
				lvTtlTrnsfrPnltyFeeData.add(laTtlTrnsfrPnltyFeeData);
			}
			else
			{
				Vector lvTtlTrnsfrPnltyFeeData = new Vector();
				lvTtlTrnsfrPnltyFeeData.add(laTtlTrnsfrPnltyFeeData);
				shtTtlTrnsfrPnltyFee.put(
					lsPrimaryKey,
					lvTtlTrnsfrPnltyFeeData);
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
		shtTtlTrnsfrPnltyFee = ahtHashtable;
	}
}
