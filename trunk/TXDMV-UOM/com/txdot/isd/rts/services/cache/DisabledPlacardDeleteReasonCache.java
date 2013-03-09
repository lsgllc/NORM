package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DisabledPlacardDeleteReasonData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * DisabledPlacardDeleteReasonCache.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver Defect _POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * The DisabledPlacardDeleteReasonCache class provides static methods 
 * to retrieve a DisabledPlacardDeleteReasonData based on provided 
 * key.
 *
 * <p>DisabledPlacardDeleteReasonCache is being initialized and 
 * populated by the CacheManager when the system starts up.  The data 
 * will be stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Defect _POS_B	10/27/2008	
 * @author	K Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class DisabledPlacardDeleteReasonCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtDsabldPlcrdDelReasnCache =
		new Hashtable();

	static final long serialVersionUID = 4248914843619528237L;

	/**
	 * DisabledPlacardDeleteReasonCache constructor comment.
	 */
	public DisabledPlacardDeleteReasonCache()
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
		return CacheConstant.DSABLD_PLCRD_DEL_REASN_CACHE;
	}

	/**
	 * Returns the DisabledPlacardDeleteReasonData object based on
	 * the input aiDelReasnCd
	 *
	 * @param aiDelReasnCd
	 * @return DisabledPlacardDeleteReasonData 
	 */
	public static DisabledPlacardDeleteReasonData getDsabldPlcrdDelReasn(int aiDelReasnCd)
	{
		Object laObject =
			shtDsabldPlcrdDelReasnCache.get(new Integer(aiDelReasnCd));

		return laObject == null
			? null
			: (DisabledPlacardDeleteReasonData) laObject;
	}

	/**
	 * Get all DisabledPlacardDeleteReasonData for Delete 
	 *  
	 * @return Vector
	 */
	public static Vector getDsabldPlcrdDelReasnForDel()
	{
		Vector lvReturn = new Vector();
		Enumeration e = shtDsabldPlcrdDelReasnCache.elements();
		while (e.hasMoreElements())
		{
			DisabledPlacardDeleteReasonData laData =
				(DisabledPlacardDeleteReasonData) e.nextElement();

			if (laData.getDelUseIndi() == 1)
			{
				lvReturn.addElement(laData);
			}
		}

		return lvReturn.size() == 0 ? null : lvReturn;
	}

	/**
	 * Get all DisabledPlacardDeleteReasonData for Replacements 
	 *  
	 * @return Vector
	 */
	public static Vector getDsabldPlcrdDelReasnForRepl()
	{
		Vector lvReturn = new Vector();
		Enumeration e = shtDsabldPlcrdDelReasnCache.elements();
		while (e.hasMoreElements())
		{
			DisabledPlacardDeleteReasonData laData =
				(DisabledPlacardDeleteReasonData) e.nextElement();

			if (laData.getReplUseIndi() == 1)
			{
				lvReturn.addElement(laData);
			}
		}
		return lvReturn.size() == 0 ? null : lvReturn;
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
		return shtDsabldPlcrdDelReasnCache;
	}

	/**
	 * Clear and populate the hashtable with the vector avData
	 * 
	 * @param avData Vector
	 */
	public void setData(Vector avData)
	{
		//reset the hashtable
		shtDsabldPlcrdDelReasnCache.clear();
		for (int i = 0; i < avData.size(); i++)
		{
			DisabledPlacardDeleteReasonData laData =
				(DisabledPlacardDeleteReasonData) avData.get(i);
			shtDsabldPlcrdDelReasnCache.put(
				new Integer(laData.getDelReasnCd()),
				laData);
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
		shtDsabldPlcrdDelReasnCache = ahtHashtable;
	}
}
