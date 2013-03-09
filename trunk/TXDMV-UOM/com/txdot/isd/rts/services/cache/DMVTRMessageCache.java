package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DMVTRMessageData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * DMVTRMessageCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/16/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * The DMVTRMessageCache class provides the static method to 
 * retrieve all DMVTRMessageData 
 * 
 * Note:  The data in this table has never been populated.  Neither is
 *  the code written to use this data at the bottom of the receipt.
 *
 * @version	5.2.3		06/16/2005
 * @author	Administrator 
 * <br>Creation Date: 	08/02/2001 10:06:39  
 *
 */

public class DMVTRMessageCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtDMVTRMsg = new Hashtable();

	private final static long serialVersionUID = -4705898510335971011L;

	/**
	 * DMVTRMsgCache constructor comment.
	 */
	public DMVTRMessageCache()
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
		return CacheConstant.DMVTR_MESSAGE_CACHE;
	}
	/**
	 * Return DMVTRMessageData
	 * 
	 * @return String
	 */
	public static DMVTRMessageData getDMVTRMsg(String asRTSDMVTRMsgNo)
	{
		//	Object laObject  =  shtDMVTRMsg.get(new String(asRTSDMVTRMsgNo));
		//	if (laObject != null)
		// 	{
		//		return (DMVTRMessageData) laObject;
		//  }
		//	else
		//	{
		//		return null;
		//	}
		return null;

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
		return null;
	}
	/**
	 * Assign data to Hashtable
	 * 
	 * @param avDMVTRMessage Vector
	 */
	public void setData(Vector avDMVTRMessage)
	{
		//reset the hashtable
		//	shtDMVTRMsg.clear();
		//	for (int i=0; i< avDMVTRMessage.size(); i++)
		//	{
		//	    DMVTRMessageData laData = (DMVTRMessageData) avDMVTRMessage.get(i);
		//	    shtDMVTRMsg.put(new Integer(laData.getDMVTRMsg()), laData);
		//	}

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
	}
}
