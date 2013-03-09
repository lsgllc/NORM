package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ReportCategoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * ReportCategoryCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  			 
 * ---------------------------------------------------------------------
 */

/**
 * The ReportCategoryCache class provides static methods to 
 * retrieve a particular or a list of RegistrationWeightFeesData based 
 * on different input parameters.
 *
 * <p>ReportCategoryCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	5.2.3		06/17/2005
 * @author	 Ashish Mahajan
 * <br>Creation Date:	08/14/2001 15:32:30 
 */

public class ReportCategoryCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtReportCategory = new Hashtable();
	private final static long serialVersionUID = 3006659251055212725L;

	/**
	 * RptCatCache constructor comment.
	 */
	public ReportCategoryCache()
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
		return CacheConstant.REPORT_CATEGORY_CACHE;
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
		return shtReportCategory;
	}

	/**
	 * Returns vector of all report categories.
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public static Vector getRptCats() throws RTSException
	{
		Vector lvData = new Vector();

		for (Enumeration e = shtReportCategory.elements();
			e.hasMoreElements();
			)
		{
			ReportCategoryData laData =
				(ReportCategoryData) e.nextElement();
			lvData.add(laData);
		}

		return lvData;
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 *  avReportCategoryData
	 *
	 * @param avReportCategoryData Vector
	 */
	public void setData(Vector avReportCategoryData)
	{
		//reset data
		shtReportCategory.clear();

		for (int i = 0; i < avReportCategoryData.size(); i++)
		{
			ReportCategoryData laData =
				(ReportCategoryData) avReportCategoryData.get(i);
			Integer laPrimaryKey =
				new Integer(laData.getRptCategoryId());
			shtReportCategory.put(laPrimaryKey, laData);
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
		shtReportCategory = ahtHashtable;
	}
}
