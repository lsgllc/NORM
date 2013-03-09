package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ReportsData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * ReportsCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	08/28/2001	Added Javadoc comments
 * B Tulsiani	08/30/2001  Added getRpts method 
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	03/30/2009	add isLandscape()
 * 							defect 9972 Ver Defect_POS_E  			 
 * ---------------------------------------------------------------------
 */

/**
 * The ReportCache class provides static methods to 
 * retrieve a particular or a list of RegistrationWeightFeesData based 
 * on different input parameters.
 *
 * <p>ReportCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	Defect_POS_E	03/30/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		08/24/2001 16:44:52 
 */

public class ReportsCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtReports = new Hashtable();

	private final static long serialVersionUID = -8542640979424078937L;

	/**
	 * ReportsCache constructor comment.
	 */
	public ReportsCache()
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
		return CacheConstant.REPORTS_CACHE;
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
		return shtReports;
	}

	/**
	 * Gets Vector of ReportsData objects associated with the Report
	 * Category. Returns null if no objects exist. 
	 * 
	 * @param  aiRptCatId int 
	 * @return Vector 
	 */

	public static Vector getRpts(int aiRptCatId)
	{
		Object laObject = shtReports.get(new Integer(aiRptCatId));
		if (laObject != null)
		{
			return (Vector) laObject;
		}
		else
		{
			return null;
		}
	}

	/** 
	 * Return boolean to denote if Report is to be presented in 
	 * LandScape
	 */
	public static boolean isLandscape(String asRptNo)
	{
		boolean lbLandscape = false;
		if (asRptNo != null)
		{
			StringTokenizer laLandscapeRpts =
				new StringTokenizer(
					ReportConstant.LANDSCAPE_REPORT_LIST,
					" ");

			String lsRptNo;

			while (laLandscapeRpts.hasMoreTokens())
			{
				lsRptNo = laLandscapeRpts.nextToken();
				if (lsRptNo.equalsIgnoreCase(asRptNo))
				{
					lbLandscape = true;
					break;
				}
			}
		}
		return lbLandscape;
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 *  avReportsData
	 * 
	 * @param avReportsData Vector
	 */
	public void setData(Vector avReportsData)
	{
		//reset the hashtable
		shtReports.clear();

		ReportsData laRptData = null;
		Vector lvRptData = null;
		Object laObject = null;

		for (int i = 0; i < avReportsData.size(); i++)
		{
			laRptData = (ReportsData) avReportsData.get(i);
			laObject =
				shtReports.get(
					new Integer(laRptData.getRptCategoryId()));

			if (laObject == null)
			{
				lvRptData = new Vector();
			}
			else
			{
				lvRptData = (Vector) laObject;
			}

			lvRptData.add(laRptData);
			shtReports.put(
				new Integer(laRptData.getRptCategoryId()),
				lvRptData);
		}
	}

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable the hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtReports = ahtHashtable;
	}
}
