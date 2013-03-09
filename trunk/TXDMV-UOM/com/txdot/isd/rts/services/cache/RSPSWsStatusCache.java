package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.RSPSWsStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * RSPSWsStatusCache.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/14/2004	New Class
 * 							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/18/2004	Include Laptop Id as part of the key.
 *							defect 7135 Ver 5.2.1
 * K Harrell	12/29/2004	Retrieve data from DB if available
 *							add import: rts.services.communication.Comm
 *							modify getRSPSWsList()
 *							Formatting/JavaDoc/Variable Name cleanup 
 *							defect 7810 Ver 5.2.2
 * K Harrell	06/17/2005	Java 1.4 Work 
 * 							removed reference to SubstaId in 
 * 							RSPSWsStatusData Objects
 * 							defect 7899 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/**
 * Cache processing for RSPSWsStatus
 * 
 * @version	5.2.3		06/17/2005  
 * @author	Ray Rowehl
 * <br>Creation Date:	07/14/2004 13:11:11
 */

public class RSPSWsStatusCache extends AdminCache
{
	private static Hashtable shtRSPSWSStatus = new Hashtable();

	private final static long serialVersionUID = 4710366782860035034L;

	/**
	 * RSPSWsStatusCache constructor comment.
	 */
	public RSPSWsStatusCache()
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
		return CacheConstant.RSPS_WS_STATUS;
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
		return shtRSPSWSStatus;
	}
	/**
	 * Get the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param asRSPSLocIdType String
	 * @param aiRSPSLocId the int
	 * @param asRSPSLaptopId  String 
	 * @return String
	 * @throws RTSException
	 */

	public static String getKey(
		int aiOfcIssuanceNo,
		String asRSPSLocIdType,
		int aiRSPSLocId,
		String asRSPSLaptopId)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				asRSPSLocIdType,
				String.valueOf(aiRSPSLocId),
				asRSPSLaptopId });
	}
	/**
	 * Get the RSPSWsStatus data based on office issuance number,
	 * RSPS Type and RSPS Id
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param asLocIdCd       String
	 * @param aiLocId         int
	 * @return Vector
	 * @throws RTSException
	 */
	public static Vector getRSPSWsList(
		int aiOfcIssuanceNo,
		String asLocIdCd,
		int aiLocId)
		throws RTSException
	{
		// defect 7810
		// Retrieve from DB if available 
		try
		{
			if (com
				.txdot
				.isd
				.rts
				.client
				.desktop
				.RTSApplicationController
				.isDBReady())
			{
				GeneralSearchData laGSD = new GeneralSearchData();
				laGSD.setIntKey1(aiOfcIssuanceNo);
				laGSD.setIntKey2(aiLocId);
				laGSD.setKey1(asLocIdCd);
				return (Vector) Comm.sendToServer(
					GeneralConstant.GENERAL,
					CacheConstant.RSPS_WS_STATUS,
					laGSD);
			}
		}
		catch (RTSException aeRTSEx)
		{
		}
		// end defect 7810 
		Vector lvWsStatus = new Vector();
		for (Enumeration laE1 = shtRSPSWSStatus.elements();
			laE1.hasMoreElements();
			)
		{
			RSPSWsStatusData laData =
				(RSPSWsStatusData) laE1.nextElement();

			if (laData.getLocIdCd().equalsIgnoreCase(asLocIdCd)
				&& laData.getLocId() == aiLocId)
			{
				lvWsStatus.add(laData);
			}
		}
		return lvWsStatus;
	}
	/**
	 * Clear and populate the hashtable with the vector
	 *
	 * @param  avData Vector
	 * @throws RTSException
	 */
	public void setData(Vector avData) throws RTSException
	{
		//reset the hashtable
		shtRSPSWSStatus.clear();

		for (int i = 0; i < avData.size(); i++)
		{
			RSPSWsStatusData laRSPSData =
				(RSPSWsStatusData) avData.get(i);

			String lsPrimaryKey =
				getKey(
					laRSPSData.getOfcIssuanceNo(),
					laRSPSData.getLocIdCd(),
					laRSPSData.getLocId(),
					laRSPSData.getRSPSId());
			shtRSPSWSStatus.put(lsPrimaryKey, laRSPSData);
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
		shtRSPSWSStatus = ahtHashtable;
	}
}
