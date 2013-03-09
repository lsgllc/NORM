package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SalesTaxCategoryData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * SalesTaxCategoryCache.java 
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							delete getSalesTaxCat(asSalesTaxCd,aiBegDate,
 * 							aiFlag)
 * 							defect 7899 Ver 5.2.3  	
 * K Harrell	10/31/2011	Return current rows for TTL012 
 * 							modify getDistinctSalesTaxCat()
 * 							defect 11048 Ver 6.9.0 		 
 * ---------------------------------------------------------------------
 */

/**
 * The SalesTaxCategoryCache class provides static methods to 
 * retrieve a particular or a list of SalesTaxCategoryData based 
 * on different input parameters.
 *
 * <p>SalesTaxCategoryCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	6.9.0		10/31/2011
 * @author	 
 * @since 	 
 */
public class SalesTaxCategoryCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtSalesTaxCats = new Hashtable();

	private final static long serialVersionUID = -8036753330369370745L;

	/**
	 * SalesTaxCategoryCache constructor comment.
	 */
	public SalesTaxCategoryCache()
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
		return CacheConstant.SALES_TAX_CATEGORY_CACHE;
	}
	/**
	 * Return vector of Distinct Sales Tax Categories 
	 * 
	 * Return Vector 
	 */
	public static Vector getDistinctSalesTaxCat()
	{
		Vector lvReturn = new Vector();

		if (shtSalesTaxCats != null)
		{
			for (Enumeration e = shtSalesTaxCats.keys();
			e.hasMoreElements();
			)
			{
				String lsSalesTaxCat = (String) e.nextElement();
				Vector lvData =
					(Vector) shtSalesTaxCats.get(lsSalesTaxCat);
				 
				
				if (lvData != null && lvData.size() > 0)
				{
					// defect 11048
					int liCurrentDate = new RTSDate().getYYYYMMDDDate(); 
					
					for (int i=0; i< lvData.size(); i++)
					{
						SalesTaxCategoryData laData =
							(SalesTaxCategoryData) lvData.get(i);
						
						if (laData.getSalesTaxBegDate() <= liCurrentDate &&
								laData.getSalesTaxEndDate() >= liCurrentDate)
						{
							lvReturn.add(laData);
						}
					}
					// end defect 11048  
				}
			}
		}
		return lvReturn;
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
		return shtSalesTaxCats;
	}

	/**
	 * Return vector of Distinct Sales Tax Categories 
	 * 
	 * @param asSalesTaxCd String
	 * @param aiBegDate int
	 * @return SalesTaxCategoryData 
	 */
	public static SalesTaxCategoryData getSalesTaxCats(
		String asSalesTaxCd,
		int aiBegDate)
	{
		SalesTaxCategoryData laData = null;

		if (shtSalesTaxCats.containsKey(asSalesTaxCd))
		{
			Vector lvSalesTaxCat =
				(Vector) shtSalesTaxCats.get(asSalesTaxCd);

			for (int i = 0; i < lvSalesTaxCat.size(); i++)
			{
				SalesTaxCategoryData laData1 =
					(SalesTaxCategoryData) lvSalesTaxCat.get(i);

				if ((aiBegDate >= laData1.getSalesTaxBegDate())
					&& (aiBegDate <= laData1.getSalesTaxEndDate()))
				{
					laData = laData1;
					break;
				}
			}

		}
		return laData;

	}
	/**
	 * Clear and populate the hashtable with the vector
	 * 
	 * @param avSalesTaxCatData Vector
	 */
	public void setData(Vector avSalesTaxCatData)
	{
		//reset data
		shtSalesTaxCats.clear();

		for (int i = 0; i < avSalesTaxCatData.size(); i++)
		{
			SalesTaxCategoryData laData =
				(SalesTaxCategoryData) avSalesTaxCatData.get(i);

			String lsPrimaryKey = laData.getSalesTaxCat();

			if (shtSalesTaxCats.containsKey(lsPrimaryKey))
			{
				Vector lvData =
					(Vector) shtSalesTaxCats.get(lsPrimaryKey);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laData);
				shtSalesTaxCats.put(lsPrimaryKey, lvData);
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
		shtSalesTaxCats = ahtHashtable;
	}
}
