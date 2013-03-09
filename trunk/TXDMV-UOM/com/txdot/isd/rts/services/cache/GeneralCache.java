package com.txdot.isd.rts.services.cache;

import java.util.*;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 * GeneralCache.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/05/2001	Add comments
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * Super class of all cache
 *
 * @version 5.2.3		07/01/2005   
 * @author	Nancy Ting
 * <br>Creation Date: 	08/14/2001 08:49:37 
 */

public abstract class GeneralCache implements java.io.Serializable
{
	private final static long serialVersionUID = 2158818101213987003L;
	
	/**
	 * GeneralCache constructor comment.
	 */
	public GeneralCache()
	{
		super();
	}
	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public abstract int getCacheFunctionId();
	
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public abstract Hashtable getHashtable();

	/**
	 * Clear and populate the hashtable with the vector
	 *  
	 * @param avDataVector Vector
	 * @throws RTSException  
	 */
	public abstract void setData(Vector avDataVector)
		throws RTSException;

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable HashTable
	 */
	public abstract void setHashtable(java.util.Hashtable ahtHashtable);
}
