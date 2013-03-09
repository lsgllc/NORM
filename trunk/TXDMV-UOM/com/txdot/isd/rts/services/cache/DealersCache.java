package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * DealerCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/29/2004	Get dealer's data 
 *							add getDealers()
 *							defect 7135 Ver 5.2.1
 * K Harrell	12/29/2004	JavaDoc/Formatting/Variable Name cleanup
 *							return sorted vector
 *							modify getDealers()
 *							defect 7830 Ver 5.2.2
 * Ray Rowehl	04/18/2005	Modify getDealers method so that it does not
 * 							use a server class on the client side.
 * 							Also ensure that we do not use a client
 * 							class on the server.  This class is not
 * 							used on server side.
 * 							also organize imports, format source,
 * 								rename fields
 * 							modify getDealers()
 * 							defect 8135 Ver 5.2.3
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	07/05/2005	Modify comments to be more clear.
 * 							modify getDealers()
 * 							defect 8135 Ver 5.2.3
 * K Harrell	07/06/2005	Use Server for Dealer lookup if available
 * 							modify getDlr()
 * 							defect 8283 Ver 5.2.3	
 * K Harrell	06/09/2009	Use DealerData vs. GSD
 * 							modify getDlr(), getDealers() 
 * 							defect 10003 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * This data class contains attributes and get/set methods for dealer 
 * cache.
 * 
 * <p>This class is only used on client side.
 *
 * @version Defect_POS_F 	06/09/2009 
 * @author	Nancy Ting
 * <br>Creation Date:		08/14/2001 08:46:39
 */

public class DealersCache extends AdminCache
{
	private static final String MSG_SERVER_CALL_FAILED =
		"Server down, using local Dealer cache";
	private static Hashtable shtDealer = new Hashtable();

	private final static long serialVersionUID = -837777378675852924L;
	
	/**
	 * DealerCache constructor comment.
	 */
	public DealersCache()
	{
		super();
	}
	/**
	 * Return CacheFunctionId for DealerCache 
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.DEALERS_CACHE;
	}
	
	/**
	 * Returns a vector containing all dealers for a substation.
	 * 
	 * <p>We always try to go to the server first since that data is 
	 * more current.  Cache is not normally refreshed until the 
	 * morning reboots.
	 *
	 * @param aiOfc int
	 * @param aiSubsta int 
	 * @return Vector
	 */
	public static Vector getDealers(int aiOfc, int aiSubsta)
	{
		// defect 8135
		// Access Server if available
		try
		{
			if (!Comm.isServerDown())
			{
				// end defect 8135
				// defect 1003 
				// Use DealerData vs. GSD
				DealerData laDealerData = new DealerData();
				laDealerData.setOfcIssuanceNo(aiOfc);
				laDealerData.setSubstaId(aiSubsta);
				laDealerData.setChngTimestmp(null);
				return (Vector) Comm.sendToServer(
					GeneralConstant.GENERAL,
					CacheConstant.DEALERS_CACHE,
					laDealerData);
				// end defect 10003 
			}
		}
		catch (RTSException aeRTSEx)
		{
			// Log the error and move on to local cache
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}
		// end defect 8135

		// Access cache on client workstation
		Vector lvReturnData = new Vector();

		for (Enumeration laE1 = shtDealer.elements();
			laE1.hasMoreElements();
			)
		{
			DealerData laDealerData = (DealerData) laE1.nextElement();
			if ((laDealerData.getOfcIssuanceNo() == aiOfc)
				&& (laDealerData.getSubstaId() == aiSubsta))
			{
				lvReturnData.addElement(laDealerData);
			}
		}
		if (lvReturnData.size() == 0)
		{
			return null;
		}
		else
		{
			// defect 7830
			// Return sorted vector
			UtilityMethods.sort(lvReturnData);
			// end defect 7830 
			return lvReturnData;
		}
	}
	
	/**
	 * Return DealerData given OfcIssuanceno, SubstaId, DealerId
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param aiDealerId int
	 * @return DealerData 
	 * @throws RTSException
	 */
	public static DealerData getDlr(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiDealerId)
		throws RTSException
	{
		// defect 8283 
		// Access Server if available
		DealerData laDealerData = null;
		try
		{
			if (!Comm.isServerDown())
			{
				// defect 10003 
				// Use DealerData vs. GSD
				DealerData laSearchDealerData = new DealerData();
				laSearchDealerData.setOfcIssuanceNo(aiOfcIssuanceNo);
				laSearchDealerData.setSubstaId(aiSubStaId);
				// defect 8626
				laSearchDealerData.setId(aiDealerId);
				// end defect 8626 

				laSearchDealerData.setChngTimestmp(null);
				Vector lvReturn =
					(Vector) Comm.sendToServer(
						GeneralConstant.GENERAL,
						CacheConstant.DEALERS_CACHE,
						laSearchDealerData);
				// end defect 10003

				if (lvReturn.size() != 0)
				{
					laDealerData = (DealerData) lvReturn.get(0);
				}
				return laDealerData;
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}

		// Access cache on client workstation 
		Object laReturnData =
			shtDealer.get(
				getKey(aiOfcIssuanceNo, aiSubStaId, aiDealerId));

		if (laReturnData != null)
		{
			// Check for DeleteIndi 
			laDealerData = (DealerData) laReturnData;
			if (laDealerData.getDeleteIndi() == 0)
			{
				return laDealerData;
			}
		}
		return null;
		// end defect 8283
	}
	
	/**
	 * Return Dealer Hashtable 
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtDealer;
	}
	
	/**
	 * Return Primary Key for DealerData
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param aiDealerId int
	 * @return String
	 * @throws RTSException 
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiDealerId)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				String.valueOf(aiDealerId)});
	}
	
	/**
	 * Set Data for Dealer Cache
	 * 
	 * @param avDealerData Vector
	 * @throws RTSException
	 */
	public void setData(Vector avDealerData) throws RTSException
	{
		//reset data
		shtDealer.clear();

		for (int i = 0; i < avDealerData.size(); i++)
		{
			DealerData laDealerData =
				(DealerData) avDealerData.get(i);
			// defect 8626
			String lsPrimaryKey =
				getKey(
					laDealerData.getOfcIssuanceNo(),
					laDealerData.getSubstaId(),
				//laDealerData.getDealerId());
				laDealerData.getId());
			// end defect 8626 
			shtDealer.put(lsPrimaryKey, laDealerData);
		}
	}
	
	/**
	 * Set the Hashtable for Dealer 
	 * 
	 * @param ahtDealer Hashtable
	 */
	public void setHashtable(Hashtable ahtDealer)
	{
		shtDealer = ahtDealer;
	}
}
