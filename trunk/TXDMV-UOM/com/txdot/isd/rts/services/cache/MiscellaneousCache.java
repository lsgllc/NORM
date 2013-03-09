package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.MiscellaneousData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * MiscellaneousCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Added comments
 * K Harrell	10/16/2003	Use isDBReady() vs. isServerUp()
 *							modified getMisc()
 *							defect 6614 Ver 5.1.5 Fix 1
 * Ray Rowehl	04/18/2005	Modify to use only client side classes.
 * 							Remove server side class references.
 * 							organize imports, format source,
 * 							rename fields.
 * 							modify getMisc()
 * 							defect 8135 Ver 5.2.3
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3
 * Ray Rowehl   07/05/2005	Clean up comments
 * 							modify getMisc()
 * 							defect 8135 Ver 5.2.3
 * K Harrell	07/06/2005	Use Miscellaneous Cache 
 * 							modify getMisc()
 * 							defect 8283 Ver 5.2.3 
 * K Harrell	06/27/2010	add getOverrideCd()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * The MiscellaneousCache class provides static method to 
 * retrieve a particular MiscellaneousData based 
 * on different input parameters.
 *
 * <p>MiscellaneousCache is being initialized at client start up.  
 * The data will be stored in memory and thus will be accessible 
 * until the client shuts down.
 * 
 * <p>This class is only used on client side.
 * 
 * @version	6.5.0 		06/27/2010
 * @author	Nancy Ting
 * <br>Creation Date:	08/10/2001 15:04:09  
 */

public class MiscellaneousCache
	extends AdminCache
	implements java.io.Serializable
{
	private static final String MSG_SERVER_CALL_FAILED =
		"Server call failed, using local Misc Cache";
	/**
	 * A hashtable of vectors  with ofcIssuanceNo, subStaId as 
	 * primary key
	 */
	private static Hashtable shtMisc = new Hashtable();

	private final static long serialVersionUID = 2124467085726676140L;
	/**
	 * Default MiscellaneousCache constructor.
	 */
	public MiscellaneousCache()
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
		return CacheConstant.MISCELLANEOUS_CACHE;
	}
	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtMisc;
	}
	/**
	 * Create the key to be used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @return String
	 * @throws RTSException
	 */
	public static String getKey(int aiOfcIssuanceNo, int aiSubStaId)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId)});
	}
	/**
	 * Get the MiscellaneousData  based on
	 * office issuance number and substation id 
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @return MiscellaneousData
	 * @throws RTSException
	 */
	public static MiscellaneousData getMisc(
		int aiOfcIssuanceNo,
		int aiSubStaId)
		throws RTSException
	{
		// defect 8283
		// defect 8135
		// Access server if available
		MiscellaneousData laMiscData = null;
		try
		{
			if (!Comm.isServerDown())
			{
				GeneralSearchData laGSD = new GeneralSearchData();
				laGSD.setIntKey1(aiOfcIssuanceNo);
				laGSD.setIntKey2(aiSubStaId);
				laGSD.setDate1(null);
				Vector lvReturn =
					(Vector) Comm.sendToServer(
						GeneralConstant.GENERAL,
						CacheConstant.MISCELLANEOUS_CACHE,
						laGSD);
				if (lvReturn.size() != 0)
				{
					laMiscData = (MiscellaneousData) lvReturn.get(0);
				}
				return laMiscData;
				// end defect 8283 
			}
		}
		catch (RTSException aeRTSEx)
		{
			// Log that server call failed.
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}
		// end defect 8135
		Object laReturnData =
			shtMisc.get(getKey(aiOfcIssuanceNo, aiSubStaId));

		if (laReturnData != null)
		{
			return (MiscellaneousData) laReturnData;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Return Supervisor Override Code for give OfcissuanceNo, SubstaId 
	 * 
	 * @param aiOfcissuanceNo
	 * @param aiSubstaId
	 * @return String 
	 */
	public static String getOverrideCd(
		int aiOfcissuanceNo,
		int aiSubstaId)
	{
		String lsOverrideCd = new String();

		try
		{
			MiscellaneousData laMiscData =
				getMisc(aiOfcissuanceNo, aiSubstaId);
			if (laMiscData != null)
			{
				lsOverrideCd = laMiscData.getSupvOvrideCd();
				lsOverrideCd =
					lsOverrideCd == null
						? new String()
						: UtilityMethods.decryptPassword(lsOverrideCd);
			}
		}
		catch (RTSException aeRTSEx)
		{
		}
		return lsOverrideCd;
	}

	/**
	 * Run a small test to verify cache is there.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			MiscellaneousData laMiscData =
				getMisc(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId());
			System.out.println("DONE");
			System.out.println(laMiscData);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
	}
	/**
	 * Clear and populate the hashtable with the vector avMiscData
	 * 
	 * @param avMiscData Vector
	 * @throws RTSException
	 */
	public void setData(Vector avMiscData) throws RTSException
	{
		//reset data
		shtMisc.clear();
		for (int i = 0; i < avMiscData.size(); i++)
		{
			MiscellaneousData laMiscellaneousData =
				(MiscellaneousData) avMiscData.get(i);
			String lsPrimaryKey =
				getKey(
					laMiscellaneousData.getOfcIssuanceNo(),
					laMiscellaneousData.getSubstaId());
			shtMisc.put(lsPrimaryKey, laMiscellaneousData);
		}
	}
	/**
	 * Set the hashtable
	 * 
	 * @param ahtMisc Hashtable
	 */
	public void setHashtable(Hashtable ahtMisc)
	{
		shtMisc = ahtMisc;
	}
}
