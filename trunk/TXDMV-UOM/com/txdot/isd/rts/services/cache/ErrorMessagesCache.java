package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * ErrorMessagesCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * A Yang		08/24/2001	Add comments
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3
 * K Harrell	03/10/2011	add getErrMsgDesc() 
 * 							defect 10768 Ver 6.7.0  
 *----------------------------------------------------------------------
 */

/**
 * The ErrorMessagesCache class provides static methods to 
 * retrieve a DocumentTypesData based on different input parameters.
 *
 * <p>ErrorMessagesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	6.7.0  		03/10/2011
 * @author	Nancy Ting  
 * <br>Creation Date: 	08/02/2001 10:06:39 	  
 *
 */

public class ErrorMessagesCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtErrorMessagesCache = new Hashtable();

	private final static long serialVersionUID = 2721406758377698769L;

	/**
	 * ErrorMsgCache constructor comment.
	 */
	public ErrorMessagesCache()
	{
		super();

	}
	/**
	 * Return the CacheConstant for this cache type.
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.ERROR_MESSAGES_CACHE;
	}
	/**
	 * Gets the error message data object.  Returns null if object 
	 * does not exist.
	 * 
	 * @param  aiErrMsgNo int
	 * @return ErrorMsgsData
	 */
	public static ErrorMessagesData getErrMsg(int aiErrMsgNo)
	{
		Object laObject =
			shtErrorMessagesCache.get(new Integer(aiErrMsgNo));
		if (laObject != null)
		{
			return (ErrorMessagesData) laObject;
		}
		else
		{
			return null;
		}
	}

	/** 
	 * Return ErrMsgDesc
	 * 
	 * @param aiErrMsgNo 
	 * @return String  
	 */
	public static String getErrMsgDesc(int aiErrMsgNo)
	{
		String lsErrMsgDesc = new String();

		Object laObject =
			shtErrorMessagesCache.get(new Integer(aiErrMsgNo));
			
		if (laObject != null)
		{
			lsErrMsgDesc =
				((ErrorMessagesData) laObject).getErrMsgDesc();
		}
		return lsErrMsgDesc;
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
		return shtErrorMessagesCache;
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avErrMsgData 
	 * 
	 * @param avErrMsgData	Vector
	 */
	public void setData(Vector avErrMsgData)
	{
		//reset the hashtable
		shtErrorMessagesCache.clear();

		for (int i = 0; i < avErrMsgData.size(); i++)
		{
			ErrorMessagesData laData =
				(ErrorMessagesData) avErrMsgData.get(i);

			shtErrorMessagesCache.put(
				new Integer(laData.getErrMsgNo()),
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
		shtErrorMessagesCache = ahtHashtable;
	}
}
