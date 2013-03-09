package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DeleteReasonsData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * DeleteReasonsCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C Walker		12/11/2001	Added getDelReasons(int) method
 * K Harrell	06/15/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * The DeleteReasonsCache class provides the static method to 
 * retrieve all DeleteReasonsData 
 *
 * @version	5.2.3		06/15/2005
 * @author	Joe Peters 
 * <br>Creation Date: 	08/30/2001  
 *
 */

public class DeleteReasonsCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtDeleteReasonsCache = new Hashtable();

	private final static long serialVersionUID = -8929537784581325806L;

	/**
	 * DeleteReasonsCache constructor comment.
	 */
	public DeleteReasonsCache()
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
		return CacheConstant.DELETE_REASONS_CACHE;
	}
	/**
	 * Returns the DeleteReasonsData object based on the input
	 * aiDelReasnCd.
	 *
	 * @param aiDelReasnCd int
	 * @return DeleteReasonsData 
	 */
	public static DeleteReasonsData getDelReason(int aiDelReasnCd)
	{
		Object laObject =
			shtDeleteReasonsCache.get(new Integer(aiDelReasnCd));
		if (laObject != null)
		{
			return (DeleteReasonsData) laObject;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Get all DeleteReasonsData
	 *  
	 * @return Vector
	 */

	public static Vector getDelReasons()
	{
		Vector lvReturn = new Vector();
		Enumeration e = shtDeleteReasonsCache.elements();
		while (e.hasMoreElements())
		{
			DeleteReasonsData laDeleteReasonsData =
				(DeleteReasonsData) e.nextElement();
			lvReturn.addElement(laDeleteReasonsData);
		}

		if (lvReturn.size() == 0)
		{
			return null;
		}
		else
		{
			return lvReturn;
		}
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
		return shtDeleteReasonsCache;
	}
	/**
	 * Clear and populate the hashtable with the vector avDelReasnsDataVector
	 * 
	 * @param avDeleteReasonsData Vector
	 */
	public void setData(Vector avDeleteReasonsData)
	{
		//reset the hashtable
		shtDeleteReasonsCache.clear();
		for (int i = 0; i < avDeleteReasonsData.size(); i++)
		{
			DeleteReasonsData laDeleteReasonsData =
				(DeleteReasonsData) avDeleteReasonsData.get(i);
			shtDeleteReasonsCache.put(
				new Integer(laDeleteReasonsData.getDelInvReasnCd()),
				laDeleteReasonsData);
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
		shtDeleteReasonsCache = ahtHashtable;
	}
}
