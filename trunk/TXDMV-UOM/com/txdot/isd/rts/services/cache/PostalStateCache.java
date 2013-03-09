package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PostalStateData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * PostalStateCache
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2010	Created
 * 							defect 10396 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * The PostalStateCache class provides static methods to 
 * retrieve a PostalStateDataobject/objects.
 *
 * <p>PostalStateCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	POS_640		03/24/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	03/24/2010
 */
public class PostalStateCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* A hashtable of Objects with StateAbrvn as key
	*/
	private static Hashtable shtPostalState = new Hashtable();
	
	static final long serialVersionUID = 6363907978050652787L;

	/**
	 * Return Vector of Plate Symbols Data  
	 * 
	 * @return Vector 
	 */
	public static Vector getPostalStates()
	{
		return new Vector(shtPostalState.values());
	}

	/** 
	 * Return boolean to denote if passed State Abbreviation  
	 * in PostalStateCache
	 * 
	 * @param asStateAbrvn
	 * @return boolean 
	 */
	public static boolean isValidState(String asStateAbrvn)
	{
		boolean lbValidState = false;

		if (asStateAbrvn != null)
		{
			PostalStateData laPostalStateData =
				(PostalStateData) shtPostalState.get(asStateAbrvn.trim());
				
			lbValidState = laPostalStateData != null;
		}
		return lbValidState;
	}

	/**
	 * PostalStateCache constructor comment.
	 */
	public PostalStateCache()
	{
		super();
	}

	/** 
	 * Return Vector of all Postal States
	 * 
	 * @return Vector 
	 */
	public Vector getAllPostalStates()
	{
		return new Vector(shtPostalState.values());
	}

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.POSTAL_STATE_CACHE;
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
		return shtPostalState;
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * PostalStateData
	 * 
	 * @param avData Vector 
	 */
	public void setData(Vector avData)
	{
		//reset data
		shtPostalState.clear();

		for (int i = 0; i < avData.size(); i++)
		{
			PostalStateData laData = (PostalStateData) avData.get(i);

			shtPostalState.put(laData.getStateAbrvn(),
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
		shtPostalState = ahtHashtable;
	}
}
