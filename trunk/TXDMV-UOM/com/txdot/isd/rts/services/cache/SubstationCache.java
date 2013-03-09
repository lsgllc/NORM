package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * SubstationCache.java  
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
 * The SubstationCache  class provides static methods to 
 * retrieve a particular or a list of SubstationData based 
 * on different input parameters.
 *
 * <p>SubstationCache  is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	5.2.3		06/17/2005
 * @author	Nancy Ting
 * <br>Creation Date: 
 *
 */

public class SubstationCache
	extends AdminCache
	implements java.io.Serializable
{
	/**
	* A hashtable of vectors with ofcIssuanceNo and subStaId as key
	*/
	private static Hashtable shtSubsta = new Hashtable();

	private final static long serialVersionUID = 8575061979970497862L;
	
	/**
	 * Default SubstationCache constructor.
	 */
	public SubstationCache()
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
		return CacheConstant.SUBSTATION_CACHE;
	}
	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable 
	 */
	public Hashtable getHashtable()
	{
		return shtSubsta;
	}
	/**
	 * Get the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @return String 
	 */
	public static String getKey(int aOfcIssuanceNo, int aSubStaId)
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aOfcIssuanceNo),
				String.valueOf(aSubStaId)});
	}
	/**
	 * Get a vector of SubstationData based on office issuance number
	 * 
	 * @param aiOfcIssuanceNo int 
	 * @return Vector  
	 */
	public static Vector getSubsta(int aiOfcIssuanceNo)
	{
		Vector lvSubsta = new Vector();

		for (Enumeration e = shtSubsta.elements(); e.hasMoreElements();)
		{
			SubstationData lSubstationData =
				(SubstationData) e.nextElement();
				
			if (lSubstationData.getOfcIssuanceNo() == aiOfcIssuanceNo)
			{
				lvSubsta.addElement(lSubstationData);
			}
		}

		if (lvSubsta.size() != 0)
		{
			return lvSubsta;
		}
		return null;
	}
	/**
	 * Get the SubstationData based on office issuance number and 
	 * substation id
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int 
	 * @return SubstationData 
	 */
	public static SubstationData getSubsta(
		int aiOfcIssuanceNo,
		int aiSubStaId)
	{
		Object laSubstationData =
			shtSubsta.get(getKey(aiOfcIssuanceNo, aiSubStaId));

		if (laSubstationData != null)
		{
			return (SubstationData) laSubstationData;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avSubstationData
	 * 
	 * @param avSubstationData Vector 
	 */
	public void setData(Vector avSubstationData)
	{
		//reset data
		shtSubsta.clear();
		
		for (int i = 0; i < avSubstationData.size(); i++)
		{
			SubstationData laSubstaData =
				(SubstationData) avSubstationData.get(i);

			shtSubsta.put(
				getKey(
					laSubstaData.getOfcIssuanceNo(),
					laSubstaData.getSubstaId()),
				laSubstaData);
		}
	}
	/**
	 * Set the hashtable
	 * 
	 * @param ahtSubsta Hashtable
	 */
	public void setHashtable(Hashtable ahtSubsta)
	{
		shtSubsta = ahtSubsta;
	}
}
