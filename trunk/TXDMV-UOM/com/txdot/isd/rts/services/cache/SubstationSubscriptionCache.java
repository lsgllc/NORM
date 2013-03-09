package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SubstationSubscriptionData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * SubstationSubscriptionCache.java  
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  	
 * K Harrell	05/13/2008	add isEmpty()
 * 							defect 8550 POS Defect A 		 
 * ---------------------------------------------------------------------
 */
/**
 * The SubstationSubscriptionCache  class provides static methods to 
 * retrieve a particular or a list of SubstationData based 
 * on different input parameters.
 *
 * <p>SubstationSubscriptionCache  is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	POS Defect A	05/13/2008
 * @author	Nancy Ting
 * <br>Creation Date: 
 *
 */

public class SubstationSubscriptionCache
	extends AdminCache
	implements java.io.Serializable
{

	/**
	* A hashtable of vectors with ofcIssuanceNo, subStaId and tblname as key
	*/
	private static Hashtable shtSubstaScr = new Hashtable();

	public static final String DEALER = "RTS_DEALERS";
	public static final String LIENHOLDER = "RTS_LIENHOLDERS";
	public static final String SECURITY = "RTS_SECURITY";
	public static final String SUBCON = "RTS_SUBCON";
	public static final String CREDIT = "RTS_CRDT_CARD_FEE";

	private final static long serialVersionUID = -1526367220130055852L;

	/**
	 * Default SubstationSubscriptionCache constructor.
	 */
	public SubstationSubscriptionCache()
	{
		super();
	}

	/**
	 * Return boolean to denote if Subscriptions Exist for Office 
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty()
	{
		return shtSubstaScr.isEmpty();
	}

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int 
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.SUBSTATION_SUBSCRIPTION_CACHE;
	}

	/**
	 * Get the internally stored hashtable
	 * 
	 * @return the internally stored hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtSubstaScr;
	}

	/**
	 * Return the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param asTblName String
	 * @return String
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		String asTblName)
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				asTblName });
	}

	/**
	 * Get vector of SubstationSubscriptionData objects given 
	 * ofcIssuanceNo, substaId 
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @return Vector
	 */
	public static Vector getSubstaSubscr(
		int aiOfcIssuanceNo,
		int aiSubStaId)
	{
		Vector lvSubStaScrData = new Vector();
		for (Enumeration e1 = shtSubstaScr.elements();
			e1.hasMoreElements();
			)
		{
			SubstationSubscriptionData laSubstationSubScriptionData =
				(SubstationSubscriptionData) e1.nextElement();

			if ((laSubstationSubScriptionData.getOfcIssuanceNo()
				== aiOfcIssuanceNo)
				&& (laSubstationSubScriptionData.getSubstaId()
					== aiSubStaId))
			{
				lvSubStaScrData.addElement(
					laSubstationSubScriptionData);
			}
		}

		if (lvSubStaScrData.size() == 0)
		{
			return null;
		}
		else
		{
			return lvSubStaScrData;
		}
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * avSubstationSubscriptionData
	 * 
	 * @param avSubstationSubscriptionData Vector
	 */
	public void setData(Vector avSubstationSubscriptionData)
	{
		//reset data
		shtSubstaScr.clear();

		for (int i = 0; i < avSubstationSubscriptionData.size(); i++)
		{
			SubstationSubscriptionData laData =
				(
					SubstationSubscriptionData) avSubstationSubscriptionData
						.get(
					i);

			shtSubstaScr.put(
				getKey(
					laData.getOfcIssuanceNo(),
					laData.getSubstaId(),
					laData.getTblName()),
				laData);
		}
	}

	/**
	 * Set the hashtable
	 * 
	 * @param ahtSubstaScr the new hashtable
	 */
	public void setHashtable(Hashtable ahtSubstaScr)
	{
		shtSubstaScr = ahtSubstaScr;
	}
}
