package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OrganizationNumberData;
import com.txdot.isd.rts.services.data.PlateGroupIdData;
import com.txdot.isd.rts.services.data.PlateTypeData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * OrganizationNumberCache.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created
 * 							defect 9085  Ver Special Plates
 * K Harrell	05/29/2007	Fine Tuning 
 * 							defect 9085  Ver Special Plates
 * B Brown		07/08/2008	Trim out blanks in orgno when comparing 
 * 							database value to the static hash table.
 * 							modify getOrgNo()  
 * 							defect 9680 Ver MyPlates_POS
 * B Brown		07/14/2008	Get the baseRegPltCd when going to get  
 * 							the orgno.
 * 							add getBaseRegpltCd()
 * 							modify getOrgNo()  
 * 							defect 9680 Ver MyPlates_POS
 * J Zwiener	02/01/2011	Determine if a plate organizations has crossed over
 * 							add isCrossedOver()
 * 							defect 10704 Ver POS_670
 * J Zwiener	02/01/2011	add getRestyleAcctItmCd
 * 							defect 10627 Ver POS_670
 * ---------------------------------------------------------------------
 */

/**
 * The OrganizationNumberCache class provides static methods to 
 * retrieve a OrganizationNumberData based on different input parameters.
 *
 * <p>OrganizationNumberCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	POS_670			02/01/2011
 * @author	Kathy Harrell  
 * <br>Creation Date: 		01/31/2007  18:03:00	  
 *
 */

public class OrganizationNumberCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtOrganizationNumber = new Hashtable();

	static final long serialVersionUID = -5077843290473179409L;

	/**
	 * OrganizationNumberCache constructor comment.
	 */
	public OrganizationNumberCache()
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
		return CacheConstant.ORG_NO_CACHE;
	}
	/**
	 * Gets the OrganizationNumber Data Object.  Returns null if object 
	 * does not exist.
	 * 
	 * @param  asBaseRegPltCd  String
	 * @param  asOrgNo String
	 * @param  aiEffDate int
	 * @return OrganizationNumberData
	 */
	public static OrganizationNumberData getOrgNo(
		String asRegPltCd,
		String asOrgNo,
		int aiEffDate)
	{
		OrganizationNumberData laOrgNoData = null;
		// defect 9680
		String lsBaseRegPltCd = getBaseRegPltCd(asRegPltCd,asOrgNo);
		
		if (shtOrganizationNumber.containsKey(lsBaseRegPltCd))
		{
			Vector lvOrgNoData =
				(Vector) shtOrganizationNumber.get(lsBaseRegPltCd);
		// end defect 9680		

			for (int i = 0; i < lvOrgNoData.size(); i++)
			{
				OrganizationNumberData laData =
					(OrganizationNumberData) lvOrgNoData.get(i);

				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate())
					// defect 9680
//					&& laData.getOrgNo().equals(asOrgNo))					
					&& laData.getOrgNo().trim().equals(asOrgNo.trim()))
					// end defect 9680
				{
					laOrgNoData = laData;
					break;
				}
			}
		}
		return laOrgNoData;

	}
	/**
	 * 
	 * Return RestyleAcctItmCd
	 * 
	 * @param asRegPltCd
	 * @param asOrgNo
	 * @return String 
	 */
	public static String getRestyleAcctItmCd(
		String asRegPltCd,
		String asOrgNo)
	{
		String lsRestyleAcctItmCd = null;
		int liToday = new RTSDate().getYYYYMMDDDate();

		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(asRegPltCd);

		if (laPltTypeData != null)
		{
			String lsBaseRegPltCd = laPltTypeData.getBaseRegPltCd();

			OrganizationNumberData laOrgNoData =
				getOrgNo(lsBaseRegPltCd, asOrgNo, liToday);

			if (laOrgNoData != null)
			{
				lsRestyleAcctItmCd = laOrgNoData.getRestyleAcctItmCd();
			}
		}
		return lsRestyleAcctItmCd;
	}

	/**
	 * 
	 * Is Organization Sunsetted
	 * 
	 * @param asRegPltCd
	 * @param asOrgNo
	 * @return boolean 
	 */
	public static boolean isSunsetted(
		String asRegPltCd,
		String asOrgNo)
	{
		boolean lbSunsetted = false;
		int liToday = new RTSDate().getYYYYMMDDDate();

		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(asRegPltCd);

		if (laPltTypeData != null)
		{
			String lsBaseRegPltCd = laPltTypeData.getBaseRegPltCd();

			OrganizationNumberData laOrgNoData =
				getOrgNo(lsBaseRegPltCd, asOrgNo, liToday);

			if (laOrgNoData != null)
			{
				int liSunsetDate = laOrgNoData.getSunsetDate();
				if (liSunsetDate != 0 && liSunsetDate < liToday)
				{
					lbSunsetted = true;
				}
			}
		}
		return lbSunsetted;
	}
	
	/**
	 * 
	 * Is Organization's plate crossed over to MyPlates
	 * 
	 * @param asRegPltCd
	 * @param asOrgNo
	 * @return boolean 
	 */
	public static boolean isCrossedOver(
		String asRegPltCd,
		String asOrgNo)
	{
		boolean lbCrossedOver = false;
		int liToday = new RTSDate().getYYYYMMDDDate();

		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(asRegPltCd);

		if (laPltTypeData != null)
		{
			String lsBaseRegPltCd = laPltTypeData.getBaseRegPltCd();

			OrganizationNumberData laOrgNoData =
				getOrgNo(lsBaseRegPltCd, asOrgNo, liToday);

			if (laOrgNoData != null)
			{
				if ((laOrgNoData.getCrossoverIndi()==1)
				&& (laOrgNoData.getCrossoverPosDate()<=liToday))
				{
					lbCrossedOver = true;
				}
			}
		}
		return lbCrossedOver;
	}

	/**
	 * Gets the OrganizationName. Returns empty string if   
	 * not found. 
	 * 
	 * @param  asRegPltCd String
	 * @param  asOrgNo String
	 * @return String
	 */
	public static String getOrgName(String asRegPltCd, String asOrgNo)
	{
		String lsOrgName = "";

		int liToday = new RTSDate().getYYYYMMDDDate();

		if (asOrgNo != null && asRegPltCd != null)
		{
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(asRegPltCd, liToday);

			if (laPltTypeData != null)
			{
				String lsBaseRegPltCd = laPltTypeData.getBaseRegPltCd();

				OrganizationNumberData laOrgNoData =
					getOrgNo(lsBaseRegPltCd, asOrgNo, liToday);

				if (laOrgNoData != null)
				{
					String lsSponsorId =
						laOrgNoData.getSponsorPltGrpId();

					PlateGroupIdData laPltGrpData =
						PlateGroupIdCache.getPltGrpId(lsSponsorId);

					if (laPltGrpData != null)
					{
						lsOrgName = laPltGrpData.getPltGrpDesc();
					}
				}
			}
		}
		return lsOrgName;
	}
	
	/**
	 * Gets the BaseRegPltcd. Returns empty string if   
	 * not found. 
	 * 
	 * @param  asRegPltCd String
	 * @param  asOrgNo String
	 * @return String
	 */
	public static String getBaseRegPltCd(String asRegPltCd, String asOrgNo)
	{
		int liToday = new RTSDate().getYYYYMMDDDate();
		
		String lsBaseRegPltCd = "";

		if (asOrgNo != null && asRegPltCd != null)
		{
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(asRegPltCd, liToday);

			if (laPltTypeData != null)
			{
				lsBaseRegPltCd = laPltTypeData.getBaseRegPltCd();
			}
		}
		return lsBaseRegPltCd;
	}
	/**
	 * Returns Vector of OrgNames given RegPltCd and the indicator to 
	 * determine if plates are current (i.e. not sunset given current 
	 * date)   
	 * 
	 * @param  asRegPltCd String
	 * @param  abCurrent boolean 
	 * @return Vector
	 */
	public static Vector getOrgsPerPlt(
		String asRegPltCd,
		boolean abCurrent)
	{
		Vector lvOrgNoData = new Vector();
		
		int liToday = new RTSDate().getYYYYMMDDDate();
		
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(
				asRegPltCd,liToday);

		if (laPltTypeData != null)
		{
			String lsBaseRegPltCd = laPltTypeData.getBaseRegPltCd();

			if (shtOrganizationNumber.containsKey(lsBaseRegPltCd))
			{
				Vector lvBaseRegPltCd =
					(Vector) shtOrganizationNumber.get(lsBaseRegPltCd);

				for (int i = 0; i < lvBaseRegPltCd.size(); i++)
				{
					OrganizationNumberData laOrgNoData =
						(OrganizationNumberData) lvBaseRegPltCd.get(i);

					if (laOrgNoData != null
						&& (liToday >= laOrgNoData.getRTSEffDate())
						&& (liToday <= laOrgNoData.getRTSEffEndDate()))
					{
						boolean lbSunset =
							(laOrgNoData.getSunsetDate() != 0
								&& liToday > laOrgNoData.getSunsetDate());

						// Only return those applicable Organizations 
						// e.g. SBRNW will not consider sunset, hence return all
						//      SPAPPL will only want current 
						if ((!abCurrent) || (!lbSunset))
						{
							lvOrgNoData.add(laOrgNoData);
						}
					}
				}
			}
		}
		return lvOrgNoData;
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
		return shtOrganizationNumber;
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avOrgNoData 
	 * 
	 * @param avOrgNoData	Vector
	 */
	public void setData(Vector avOrgNoData)
	{
		//reset the hashtable
		shtOrganizationNumber.clear();
		{
			for (int i = 0; i < avOrgNoData.size(); i++)
			{
				OrganizationNumberData laOrgNoData =
					(OrganizationNumberData) avOrgNoData.get(i);

				String lsPrimaryKey = laOrgNoData.getBaseRegPltCd();

				if (shtOrganizationNumber.containsKey(lsPrimaryKey))
				{
					Vector lvOrgNoData =
						(Vector) shtOrganizationNumber.get(
							lsPrimaryKey);
					lvOrgNoData.add(laOrgNoData);
				}
				else
				{
					Vector lvOrgNoDataVector = new Vector();
					lvOrgNoDataVector.add(laOrgNoData);
					shtOrganizationNumber.put(
						lsPrimaryKey,
						lvOrgNoDataVector);
				}
			}
		}
		return;
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
		shtOrganizationNumber = ahtHashtable;
	}
}
