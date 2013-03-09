package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.SubcontractorData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * SubcontractorCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Added comments
 * K Harrell   	11/14/2002  Added check for DeleteIndi in getSubcon
 *							defect 5102 
 * Ray Rowehl	07/24/2003	use checking for dbup instead of serverup
 *							modified 	getSubcon(int, int, int),
 *										getSubcons(int, int)
 *							defect 6110 Ver 5.1.4
 * K Harrell	10/16/2003	Use isDBReady() vs. isDBUp()
 *							modify getSubcon(), getSubcons()
 *							defect 6614 Ver 5.1.5.1
 * K Harrell	12/29/2004	Return sorted vector
 *							modify getSubcons()
 *							defect 7830 Ver 5.2.2 
 * Ray Rowehl	04/18/2005	Change to no longer use Server side code.
 * 							This causes runtime errors when attempting 
 * 							to call getSubcon and getSubcons.
 * 							organize imports, format source,
 * 							rename fields.
 * 							modify getSubcon(), getSubcons()
 * 							defect 8135 Ver 5.2.3
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	07/05/2005	Cleanup comments.
 * 							modify getSubcon(), getSubcons()
 * 							defect 8135 Ver 5.2.3
 * K Harrell	07/06/2005	Use Subcontractor Cache request
 * 							modify getSubcon(), getSubcons()
 * 							defect 8283 Ver 5.2.3  
 * K Harrell	06/09/2009	Use SubcontractorData vs. GSD
 * 							modify getSubcon(), getSubcons()
 * 							defect 10003 Ver Defect_POS_F 
 * K Harrell	02/18/2010  Implement new SubcontractorData 
 * 							modify getSubcon(), setData()
 * 							defect 10161 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * The SubcontractorCache class provides static methods to 
 * retrieve a particular or a list of SubcontractorData based 
 * on different input parameters.
 *
 * <p>SubcontractorCache is initialized at client start up.  The data 
 * will be stored in memory and thus will be accessible until the client
 * shuts down.
 * 
 * <p>It is possible for this class to be called on server side.
 *
 * @version	POS_640			02/18/2010 
 * @author	Nancy Ting
 * <br>Creation Date:		08/10/2001 15:04:09 
 */

public class SubcontractorCache
	extends AdminCache
	implements java.io.Serializable
{
	private static final String MSG_SERVER_CALL_FAILED =
		"Server down, using local Subcon Cache";
	private static Hashtable shtSubcon = new Hashtable();

	private final static long serialVersionUID = -5644314081136495219L;
	/**
	 * SubcontractorCache default constructor
	 */
	public SubcontractorCache()
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
		return CacheConstant.SUBCONTRACTOR_CACHE;
	}
	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable 
	 */
	public Hashtable getHashtable()
	{
		return shtSubcon;
	}
	/**
	 * Get the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param aiSubconId int
	 * @return String 
	 * @throws RTSException 
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiSubconId)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				String.valueOf(aiSubconId)});
	}
	/**
	 * Get the subcontractor data based on office issuance number,
	 * substation id and subcontractor id
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param aiSubconId int 
	 * @return SubcontractorData
	 * @throws RTSException 
	 */
	public static SubcontractorData getSubcon(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiSubconId)
		throws RTSException
	{
		// defect 8283 
		// Use Subcontractor Cache request 
		// defect 8135
		// Access server if available 
		SubcontractorData laSubconData = null;
		try
		{
			if (!Comm.isServerDown())
			{
				// defect 10003
				// Use SubcontractorData vs. GSD
				SubcontractorData laSearchSubconData =
					new SubcontractorData();
				laSearchSubconData.setOfcIssuanceNo(aiOfcIssuanceNo);
				laSearchSubconData.setSubstaId(aiSubStaId);
				laSearchSubconData.setId(aiSubconId);
				laSearchSubconData.setChngTimestmp(null);

				Vector lvReturn =
					(Vector) Comm.sendToServer(
						GeneralConstant.GENERAL,
						CacheConstant.SUBCONTRACTOR_CACHE,
						laSearchSubconData);
				// end defect 10003 

				if (lvReturn.size() != 0)
				{
					laSubconData = (SubcontractorData) lvReturn.get(0);
				}
				return laSubconData;
			}
		}
		catch (RTSException aeRTSEx)
		{
			// Log that server call failed.
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}
		// end defect 8135
		// end defect 8283 
		String lsPrimaryKey =
			getKey(aiOfcIssuanceNo, aiSubStaId, aiSubconId);
		Object laReturnData = shtSubcon.get(lsPrimaryKey);

		if (laReturnData != null)
		{
			// Check for DeleteIndi 
			laSubconData = (SubcontractorData) laReturnData;

			if (laSubconData.getDeleteIndi() == 0)
			{
				return laSubconData;
			}
		}
		return null;
	}

	/**
	 * Get a vector of SubcontractorData
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @return Vector 
	 */
	public static Vector getSubcons(
		int aiOfcIssuanceNo,
		int aiSubstaId)
	{
		// defect 8283 
		// Use Subcontractor Cache request 
		// defect 8135
		// Access server if available 
		try
		{
			if (!Comm.isServerDown())
			{
				// end defect 8135
				// defect 10003 
				// Use SubcontractorData vs. GSD  
				SubcontractorData laSearchSubconData =
					new SubcontractorData();
				laSearchSubconData.setOfcIssuanceNo(aiOfcIssuanceNo);
				laSearchSubconData.setSubstaId(aiSubstaId);
				laSearchSubconData.setChngTimestmp(null);

				return (Vector) Comm.sendToServer(
					GeneralConstant.GENERAL,
					CacheConstant.SUBCONTRACTOR_CACHE,
					laSearchSubconData);
				// end defect 10003 
			}
		}
		catch (RTSException aeRTSEx)
		{
			// defect 8135
			// Log the error and move on to local cache
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
			// end defect 8135
		}

		Vector lvReturnData = new Vector();
		for (Enumeration laE1 = shtSubcon.elements();
			laE1.hasMoreElements();
			)
		{
			SubcontractorData laSubconData =
				(SubcontractorData) laE1.nextElement();
			if ((laSubconData.getOfcIssuanceNo() == aiOfcIssuanceNo)
				&& (laSubconData.getSubstaId() == aiSubstaId))
			{
				lvReturnData.addElement(laSubconData);
			}
		}
		if (lvReturnData.size() == 0)
		{
			return null;
		}
		else
		{
			UtilityMethods.sort(lvReturnData);
			return lvReturnData;
		}
		// end defect 8283 
	}
	/**
	 * Clear and populate the hashtable with the vector
	 *
	 * @param avSubconData Vector
	 * @throws RTSException
	 */
	public void setData(Vector avSubconData) throws RTSException
	{
		//reset data
		shtSubcon.clear();
		for (int i = 0; i < avSubconData.size(); i++)
		{
			SubcontractorData laSubcontractorData =
				(SubcontractorData) avSubconData.get(i);
				
			// defect 10161 
			String lsPrimaryKey =
				getKey(
					laSubcontractorData.getOfcIssuanceNo(),
					laSubcontractorData.getSubstaId(),
					laSubcontractorData.getId());
			// end defect 10161 
			
			shtSubcon.put(lsPrimaryKey, laSubcontractorData);
		}
	}
	/**
	 * Set the hashtable
	 * 
	 * @param ahtSubcon Hashtable
	 */
	public void setHashtable(Hashtable ahtSubcon)
	{
		shtSubcon = ahtSubcon;
	}
}
