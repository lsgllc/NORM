package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.BusinessPartnerData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

/*
 * BusinessPartnerCache.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/07/2011	Created
 * 							defect 10726 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Cache object for RTS_BSN_PARTNER 
 *
 * @version	6.7.0			01/07/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/07/2011 10:57:17 
 */
public class BusinessPartnerCache
	extends GeneralCache
	implements java.io.Serializable
{

	static final long serialVersionUID = 7584585468675204454L;

	/**
	 * A hashtable of vectors with BsnPartnerId, BsnTypeCd as Key 
	 */
	private static Hashtable shtBsnPartner = new Hashtable();

	/**
	 * Gets an object of BusinessPartnerData for Timed Permit Vendor
	 * 
	 * @param asBulkPrmtVendorId 
	 * @param aiEffDate
	 * @return BusinessPartnerData 
	 */
	public static BusinessPartnerData getTimedPermitVendor(
		String asBulkPrmtVendorId,
		int aiEffDate)
	{
		String lsPrimaryKey =
			asBulkPrmtVendorId.trim()
				+ "#"
				+ MiscellaneousRegConstant
					.TIMED_PERMIT_VENDOR_BSN_TYPE_CD;

		Vector lvData = (Vector) shtBsnPartner.get(lsPrimaryKey);

		BusinessPartnerData laBsnPartnerData = null;

		for (int i = 0; i < lvData.size(); i++)
		{
			BusinessPartnerData laData =
				(BusinessPartnerData) lvData.get(i);

			if (aiEffDate >= laData.getRTSEffDate()
				&& (aiEffDate <= laData.getRTSEffEndDate()))
			{
				laBsnPartnerData = laData;
				break;
			}
		}
		return laBsnPartnerData;
	}

	/**
	 * BusinessPartnerCache constructor comment.
	 */
	public BusinessPartnerCache()
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
		return CacheConstant.BUSINESS_PARTNER_CACHE;
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
		return shtBsnPartner;
	}

	/**
	 * Clear and populate the hashtable with the vector avBsnPartnerData
	 * 
	 * @param avBsnPartnerData 
	 */
	public void setData(Vector avBsnPartnerData)
	{
		//reset the hashtable
		shtBsnPartner.clear();

		for (int i = 0; i < avBsnPartnerData.size(); i++)
		{
			BusinessPartnerData laData =
				(BusinessPartnerData) avBsnPartnerData.get(i);

			String lsPrimaryKey =
				laData.getBsnPartnerId().trim()
					+ "#"
					+ laData.getBsnPartnerTypeCd().trim();

			if (shtBsnPartner.containsKey(lsPrimaryKey))
			{
				Vector lvBsnPartnerData =
					(Vector) shtBsnPartner.get(lsPrimaryKey);

				lvBsnPartnerData.add(laData);
			}
			else
			{
				Vector lvBsnPartnerData = new Vector();
				lvBsnPartnerData.addElement(laData);
				shtBsnPartner.put(lsPrimaryKey, lvBsnPartnerData);
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
		shtBsnPartner = ahtHashtable;
	}
}
