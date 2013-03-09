package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.LienholderData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * LienholdersCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Added comments
 * K Harrell	06/20/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/06/2005	Use Server for Lienholder lookup if 
 * 							available
 * 							add MSG_SERVER_CALL_FAILED
 * 							modify getLienhldr()
 * 							defect 8283 Ver 5.2.3 
 * K Harrell	06/09/2009	Use LienholderData vs. GSD 
 * 							modify getLiehnhldr() 
 * 							defect 10003 Ver Defect_POS_F	
 * K Harrell	07/03/2009	Implement new LienholderData 
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * The LienholdersCache class provides static methods to 
 * retrieve a particular LienholdersData based 
 * on different input parameters.
 *
 * <p>LienholdersCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	Defect_POS_F	07/03/2009
 * @author Nancy Ting
 * <br>Creation Date:		08/16/2001 15:56:57 
 */
public class LienholdersCache
	extends AdminCache
	implements java.io.Serializable
{
	private static final String MSG_SERVER_CALL_FAILED =
		"Server down, using local Lienholders cache";
	private static Hashtable shtLienhldr = new Hashtable();

	private final static long serialVersionUID = -977390273145595850L;

	/**
	 * LienholdersCache default constructor
	 */
	public LienholdersCache()
	{
		super();
	}
	/**
	 * Get the cache function id
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.LIENHOLDERS_CACHE;
	}
	
	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtLienhldr;
	}
	
	/**
	 * Get the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param aiLienhldrId int
	 * @return String  
	 * @throws RTSException 
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiLienhldrId)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				String.valueOf(aiLienhldrId)});
	}
	
	/**
	 * Get the Lienholder data
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param aiLienhldrId int
	 * @return LienholderData 
	 * @throws RTSException 
	 */
	public static LienholderData getLienhldr(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiLienhldrId)
		throws RTSException
	{
		// defect 8283 
		// Access Server if available 
		LienholderData laLienholderData = null;
		try
		{
			if (!Comm.isServerDown())
			{
				// defect 10003 
				// Use LienholderData vs. GSD 
				LienholderData laSearchLienholderData = new LienholderData(); 
				laSearchLienholderData.setOfcIssuanceNo(aiOfcIssuanceNo);
				laSearchLienholderData.setSubstaId(aiSubStaId);
				laSearchLienholderData.setId(aiLienhldrId);
				laSearchLienholderData.setChngTimestmp(null);
				Vector lvReturn =
					(Vector) Comm.sendToServer(
						GeneralConstant.GENERAL,
						CacheConstant.LIENHOLDERS_CACHE,
						laSearchLienholderData);
				// end defect 10003 

				if (lvReturn.size() != 0)
				{
					laLienholderData = (LienholderData) lvReturn.get(0);
				}
				return laLienholderData;
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}
		
		// Access cache on client workstation 
		String lsPrimaryKey =
			getKey(aiOfcIssuanceNo, aiSubStaId, aiLienhldrId);

		Object laReturnData = shtLienhldr.get(lsPrimaryKey);
		if (laReturnData != null)
		{
			return (LienholderData) laReturnData;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Clear and populate the hashtable with the vector 
	 * 
	 * @param avLienHoldrsData Vector
	 * @throws RTSException 
	 */
	public void setData(Vector avLienHldrData) throws RTSException
	{
		//reset data
		shtLienhldr.clear();

		for (int i = 0; i < avLienHldrData.size(); i++)
		{
			LienholderData laLienholderData =
				(LienholderData) avLienHldrData.get(i);

			String lsPrimaryKey =
				getKey(
					laLienholderData.getOfcIssuanceNo(),
					laLienholderData.getSubstaId(),
					laLienholderData.getId());
			shtLienhldr.put(lsPrimaryKey, laLienholderData);
		}
	}
	
	/**
	 * Set the hashtable
	 * 
	 * @param ahtLienhldrs Hashtable 
	 */
	public void setHashtable(Hashtable ahtLienhldrs)
	{
		shtLienhldr = ahtLienhldrs;
	}
}
