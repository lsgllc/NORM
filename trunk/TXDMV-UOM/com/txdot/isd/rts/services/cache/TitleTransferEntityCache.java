package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTransferEntityData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TitleTransferEntityCache.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/02/2008	Created
 * 							defect 9583 Ver Defect POS A
 * ---------------------------------------------------------------------
 */

/**
 * The TitleTransferEntityCache class provides static methods to 
 * retrieve a particular TitleTransferEntityData based 
 * on input parameter.
 *
 * <p>TitleTransferEntityCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Defect POS A		04/02/2008
 * @author	K Harrell
 * <br>Creation Date:			04/02/2008	16:29:00
 */
public class TitleTransferEntityCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtTtlTrnsfrEnt = new Hashtable();

	private static final long serialVersionUID = -259632903659722929L;

	/**
	 * TitleTransferEntityCache constructor
	 */
	public TitleTransferEntityCache()
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
		return CacheConstant.TTL_TRNSFR_ENT_CACHE;
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
		return shtTtlTrnsfrEnt;
	}

	/**
	 * get TitleTransferEntityData Object
	 */
	public static TitleTransferEntityData getTitleTransferEntity(String asTtlTrnsfrEntCd)
	{
		Object laObject = shtTtlTrnsfrEnt.get(asTtlTrnsfrEntCd);
		if (laObject != null)
		{
			return (TitleTransferEntityData) laObject;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * avTtlTrnsfrEntData
	 * 
	 * @param avTtlTrnsfrEntData  Vector
	 */
	public void setData(Vector avTtlTrnsfrEntData)
	{
		//reset data
		shtTtlTrnsfrEnt.clear();

		for (int i = 0; i < avTtlTrnsfrEntData.size(); i++)
		{
			TitleTransferEntityData laData =
				(TitleTransferEntityData) avTtlTrnsfrEntData.get(i);

			String lsPrimaryKey = laData.getTtlTrnsfrEntCd();

			shtTtlTrnsfrEnt.put(lsPrimaryKey, laData);
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
		shtTtlTrnsfrEnt = ahtHashtable;
	}
}
