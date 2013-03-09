package com.txdot.isd.rts.services.cache;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * AssignedWorkstationIdsCache.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Add comments
 * BTulsiani	04/29/2002	modified get(int, int) to return a copy,
 *							so that the cache will not get updated
 *							when displaying the local printer.
 *							defect #3683
 * K Harrell    9/30/2002   Omit wsid w/ DeleteIndi from Cache 
 * 							defect 4692
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3 
 * K Harrell	06/19/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * Jeff S.		10/31/2005	Sort the vector of Assigned Workstations 
 * 							before returning.
 * 							modify getAsgndWsIds()
 * 							defect 8418 Ver 5.2.3
 * K Harrell	08/25/2009	add isServer(), getTtlPkgWsIds()
 * 							delete SERVER, WORKSTATION 
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	11/02/2009	Handle DB Down/Server Down via   
 * 							 checking returned Boolean 
 * 							modify putAsgndWsIds() 
 * 							defect 10254 Ver Defect_POS_G
 * K Harrell	03/26/2010	add isEmpty() 
 * 							defect 8087 Ver POS_640   
 * K Harrell	09/25/2010	modify getTtlPkgWsIds() 
 * 							defect 10013 Ver 6.6.0 
 * K Harrell	11/20/2011	add getAsgndWsId() 
 * 							defect 11052 Ver 6.9.0 
 *----------------------------------------------------------------------
 */

/**
 * The AssignedWorkstationIdsCache class provides static methods to 
 * retrieve a particular or a list of AssignedWorkstationIdsData based 
 * on different input parameters.
 *
 * <p>AssignedWorkstationIdsCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version6.9.0 			11/20/2011
 * @author	Nancy Ting
 * <br>Creation Date: 		08/10/2001 15:04:09 
 */

public class AssignedWorkstationIdsCache
	extends AdminCache
	implements java.io.Serializable
{
	/**
	* A hashtable of vectors with ofcIssuanceNo, subStaId, wsId as key
	*/
	private static Hashtable shtAsgndWsIds = new Hashtable();

	public final static String PRODUCTION = "P";
	public final static String TEST3 = "T";
	public final static String TEST5 = "U";
	public final static String MOUNTAIN_TIME_ZONE = "M";
	public final static String CENTRAL_TIME_ZONE = "C";
	private final static String ASS_WS_SER_FILE = "cache/ASSGNDWS.ser";

	private final static String ASSIGNED_WS_DATA =
		"Assigned Workstation Data with office issuance number ";
	private final static String SUBSTATION_ID = " ,substation id ";
	private final static String WS_ID = " , workstationd id ";
	private final static String IS_NULL = " is null";

	private final static long serialVersionUID = 3667188823924222134L;

	/**
	 * AccountCodesCache default constructor.
	 */
	public AssignedWorkstationIdsCache()
	{
		super();
	}

	/**
	 * Get the AssignedWorkstationIdsData based on
	 * office issuance number, substation id and
	 * workstation id
	 * 
	 * @param  aiOfcIssuanceNo	int
	 * @param  aiSubStaId		int
	 * @param  aiWsId 			int
	 * @return AssignedWorkstationIdsData
	 * @throws RTSException 
	 */
	public static AssignedWorkstationIdsData getAsgndWsId(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiWsId)
		throws RTSException
	{
		String lsPrimaryKey =
			getKey(aiOfcIssuanceNo, aiSubStaId, aiWsId);
		Object laObjectReturnData = shtAsgndWsIds.get(lsPrimaryKey);
		if (laObjectReturnData != null)
		{
			return (AssignedWorkstationIdsData) laObjectReturnData;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Get the AssignedWorkstationIdsData based on
	 * office issuance number, substation id and
	 * workstation id
	 * 
	 * @return AssignedWorkstationIdsData
	 */
	public static AssignedWorkstationIdsData getAsgndWsId()
	{
		int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo(); 
		int liSubStaId = SystemProperty.getSubStationId(); 
		int liWsId = SystemProperty.getWorkStationId();
		Object laObjectReturnData = null; 
		try
		{
			String lsPrimaryKey =
				getKey(liOfcIssuanceNo, liSubStaId, liWsId);
			laObjectReturnData = shtAsgndWsIds.get(lsPrimaryKey);
		}
		catch (RTSException aeRTSEx)
		{
			
		}
		
		if (laObjectReturnData != null)
		{
			return (AssignedWorkstationIdsData) laObjectReturnData;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get a vector of AssignedWorkstationIds based on office issuance number
	 * and substation 
	 * 
	 * @param aiOfcIssuanceNo 	int
	 * @param aiSubStaId 		int
	 * @return Vector
	 * @throws RTSException  
	 */
	public static Vector getAsgndWsIds(
		int aiOfcIssuanceNo,
		int aiSubStaId)
		throws RTSException
	{
		Vector lvReturnData = new Vector();
		for (Enumeration e1 = shtAsgndWsIds.elements();
			e1.hasMoreElements();
			)
		{
			AssignedWorkstationIdsData laAssignedWorkstationIdsData =
				(AssignedWorkstationIdsData) e1.nextElement();
			if ((laAssignedWorkstationIdsData.getOfcIssuanceNo()
				== aiOfcIssuanceNo)
				&& (laAssignedWorkstationIdsData.getSubstaId()
					== aiSubStaId)
				&& (laAssignedWorkstationIdsData.getDeleteIndi() == 0))
			{
				lvReturnData.addElement(laAssignedWorkstationIdsData);
			}
		}
		if (lvReturnData.size() == 0)
		{
			return null;
		}
		else
		{
			// defect 8418
			// Sort Vector before returning.
			//return (Vector) UtilityMethods.copy(lvReturnData);
			lvReturnData = (Vector) UtilityMethods.copy(lvReturnData);
			UtilityMethods.sort(lvReturnData);
			return lvReturnData;
			// end defect 8418
		}
	}

	/**
	 * Get a vector of String of WsIds based on office issuance number
	 * and substation id
	 * 
	 * @param aiOfcIssuanceNo 	int
	 * @param aiSubStaId 		int
	 * @return Vector
	 * @throws RTSException  
	 */
	public static Vector getTtlPkgWsIds(
		int aiOfcIssuanceNo,
		int aiSubStaId)
		throws RTSException
	{
		Vector lvWsId = new Vector();
		for (Enumeration e1 = shtAsgndWsIds.elements();
			e1.hasMoreElements();
			)
		{
			AssignedWorkstationIdsData laAssignedWorkstationIdsData =
				(AssignedWorkstationIdsData) e1.nextElement();

			if ((laAssignedWorkstationIdsData.getOfcIssuanceNo()
				== aiOfcIssuanceNo)
				&& (laAssignedWorkstationIdsData.getSubstaId()
					== aiSubStaId)
				&& (laAssignedWorkstationIdsData.getDeleteIndi() == 0))
			{
				// defect 10013
				// Add Padding  
				lvWsId.addElement(
				UtilityMethods.addPadding(Integer.toString(
					laAssignedWorkstationIdsData.getWsId()),3,"0"));
				// end defect 10013 
			}
		}
		UtilityMethods.sort(lvWsId);
		return lvWsId;
	}

	/**
	 * Get the cache function id
	 * 
	 * @return int 
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.ASSIGNED_WORKSTATION_IDS_CACHE;
	}

	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtAsgndWsIds;
	}

	/**
	 * Get the key used in the hashtable
	 * 	 
	 * @param aiOfcIssuanceNo the office issuance number
	 * @param aiSubStaId the substation id
	 * @param aiWsId the workstation id
	 * @return String
	 * @throws RTSException  
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiWsId)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				String.valueOf(aiWsId)});
	}

	/** 
	 * Is Empty
	 */
	public static boolean isEmpty()
	{
		return shtAsgndWsIds.isEmpty();
	}

	/**
	 * Is Server 
	 * 
	 * @param  aiOfcIssuanceNo	int
	 * @param  aiSubStaId		int
	 * @param  aiWsId 			int
	 * @return boolean 
	 */
	public static boolean isServer(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiWsId)
	{
		boolean lbServer = false;

		try
		{
			AssignedWorkstationIdsData laAssgndWsData =
				getAsgndWsId(aiOfcIssuanceNo, aiSubStaId, aiWsId);

			lbServer = laAssgndWsData.isServer();
		}
		catch (RTSException aeRTSEx)
		{

		}
		return lbServer;
	}

	/**
	 * Main 
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FileInputStream lFileInputStream =
				new FileInputStream(ASS_WS_SER_FILE);
			ObjectInputStream lObjectInputStream =
				new ObjectInputStream(lFileInputStream);
			Hashtable lhtReturnHashtable =
				(java.util.Hashtable) lObjectInputStream.readObject();
			AssignedWorkstationIdsCache laAssgndWksIdsCache =
				new AssignedWorkstationIdsCache();
			laAssgndWksIdsCache.setHashtable(lhtReturnHashtable);
			AssignedWorkstationIdsData laAssgndWksIdsData =
				AssignedWorkstationIdsCache.getAsgndWsId(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId(),
					SystemProperty.getWorkStationId());
			System.out.println(laAssgndWksIdsData.getRedirPrtWsId());
			AssignedWorkstationIdsCache.putAsgndWsIds(
				SystemProperty.getOfficeIssuanceNo(),
				SystemProperty.getSubStationId(),
				SystemProperty.getWorkStationId(),
				300);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Write the AsgndWsIds to disk
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubstaId
	 * @param aiWsId
	 * @param aiRedirPrtWsId
	 * @throws RTSException  
	 */
	public static void putAsgndWsIds(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		int aiWsId,
		int aiRedirPrtWsId)
		throws RTSException
	{
		AssignedWorkstationIdsData laAssignedWorkstationIdsData =
			getAsgndWsId(aiOfcIssuanceNo, aiSubStaId, aiWsId);
		if (laAssignedWorkstationIdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				ASSIGNED_WS_DATA
					+ aiOfcIssuanceNo
					+ SUBSTATION_ID
					+ aiSubStaId
					+ WS_ID
					+ aiWsId
					+ IS_NULL,
				CommonConstant.STR_ERROR);
		}
		else
		{
			//rewrite local cache
			String lsPrimaryKey =
				getKey(aiOfcIssuanceNo, aiSubStaId, aiWsId);
			laAssignedWorkstationIdsData.setRedirPrtWsId(
				aiRedirPrtWsId);

			shtAsgndWsIds.put(
				lsPrimaryKey,
				laAssignedWorkstationIdsData);
			Hashtable lhtAsgndWsIdsCopy =
				(Hashtable) UtilityMethods.copy(shtAsgndWsIds);
			CacheManager.writeToDisk(
				lhtAsgndWsIdsCopy,
				CacheManager.getSerializedClassName(
					AssignedWorkstationIdsCache.class));

			//update database
			RTSDate laRTSDateTrans = new RTSDate();
			String lsHour = String.valueOf(laRTSDateTrans.getHour());
			String lsMinute =
				String.valueOf(laRTSDateTrans.getMinute());
			String lsSecond =
				String.valueOf(laRTSDateTrans.getSecond());

			int liTransTime =
				Integer.parseInt(
					UtilityMethods.addPadding(
						new String[] { lsHour, lsMinute, lsSecond },
						new int[] { 2, 2, 2 },
						CommonConstant.STR_ZERO));

			laAssignedWorkstationIdsData.setAMDate(
				laRTSDateTrans.getAMDate());
			laAssignedWorkstationIdsData.setTranstime(liTransTime);

			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			laTransactionCacheData.setObj(laAssignedWorkstationIdsData);
			laTransactionCacheData.setProcName(
				TransactionCacheData.UPDATE);

			Vector lvTrans = new Vector();
			lvTrans.addElement(laTransactionCacheData);

			try
			{
				// defect 10254 
				// Transaction.postTrans() returns Boolean if DB_DOWN, 
				//    SERVER_DOWN 
				// Transaction.postTrans(lvTrans); 
				Object laResult = Transaction.postTrans(lvTrans);

				if (laResult != null
					&& laResult instanceof Boolean
					&& !((Boolean) laResult).booleanValue())
				{
					Transaction.writeToCache(lvTrans);
				}
			}
			catch (RTSException aeRTSEx)
			{
				//if (aeRTSEx
				//	.getMsgType()
				//	.equals(RTSException.SERVER_DOWN)
				//	|| aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
				//{
				//	Transaction.writeToCache(lvTrans);
				//}
				//else
				//{
				throw aeRTSEx;
				//}
				// end defect 10254
			}
		}
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * 
	 * @param  avAssignedWorkstationIdsDataVector Vector
	 * @throws RTSException
	 */
	public void setData(Vector avAssignedWorkstationIdsData)
		throws RTSException
	{
		//reset data
		shtAsgndWsIds.clear();
		for (int i = 0; i < avAssignedWorkstationIdsData.size(); i++)
		{
			AssignedWorkstationIdsData laAssignedWorkstationIdsData =
				(
					AssignedWorkstationIdsData) avAssignedWorkstationIdsData
						.get(
					i);
			String lsPrimaryKey =
				getKey(
					laAssignedWorkstationIdsData.getOfcIssuanceNo(),
					laAssignedWorkstationIdsData.getSubstaId(),
					laAssignedWorkstationIdsData.getWsId());
			shtAsgndWsIds.put(
				lsPrimaryKey,
				laAssignedWorkstationIdsData);
		}
	}

	/**
	 * Set the hashtable
	 * 
	 * @param ahtAsgndWsIds Hashtable
	 */
	public void setHashtable(Hashtable ahtAsgndWsIds)
	{
		shtAsgndWsIds = ahtAsgndWsIds;
	}
}