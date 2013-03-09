package com.txdot.isd.rts.services.cache;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RSPSUpdData;
import com.txdot.isd.rts.services.data.RSPSWsStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * RSPSUpdCache.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/07/2004	New Class
 * 							defect 7135 Ver 5.2.1	
 * Ray Rowehl	08/18/2004	Clean up some old comments
 * 							defect 7135 Ver 5.2.1
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							delete isEmpty()
 * 							removed reference to SubstaId in 
 * 							RSPSWsStatusData Objects 
 * 							defect 7899 Ver 5.2.3 	
 * ---------------------------------------------------------------
 */

/**
 * This class contains Update Status Information from RSPS Laptops
 * 
 * @version	5.2.3		06/17/2005  
 * @author	Ray Rowehl
 * <br>Creation Date:	07/07/2004
 */

public class RSPSUpdCache implements java.io.Serializable
{
	private static Hashtable shtRSPSUpdCache = null;

	public static final int ADD_TO_QUEUE = 1;
	public static final int GET_QUEUE_ITEMS = 2;
	public static final int DELETE_ITEMS = 3;

	private static boolean sbNotInitialized = true;

	private static final String FILENAME = "RSPSUPD.SER";

	private final static long serialVersionUID = 7635754467917822688L;

	/**
	 * RSPSUpdCache constructor comment.
	 */
	public RSPSUpdCache()
	{
		super();
	}
	/**
	 * Get the key used in the hashtable
	 * 
	 * @param aiOfcIssuanceNo int 
	 * @param asRSPSIdType String
	 * @param aiRSPSId  int
	 * @param asLogDate String
	 * @return String  
	 * @throws RTSException 
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		String asRSPSIdType,
		int aiRSPSId,
		String asLogDate)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				asRSPSIdType,
				String.valueOf(aiRSPSId),
				asLogDate });

	}

	/**
	 * This method processes queue requests.
	 * The request may be an add.
	 * The request may be a get.
	 * The request may be for a delete after the data
	 * has been sent to the server. 
	 * 
	 * @param aiRequest int 
	 * @param avRSPSUpdData Vector 
	 * @throws RTSException  
	 */
	public static synchronized Vector processQueueRequest(
		int aiRequest,
		Vector avRSPSUpdData)
		throws RTSException
	{
		Vector lvReturn = null;

		// initialize the hashset
		if (sbNotInitialized)
		{
			try
			{
				File laFile = new File(FILENAME);
				if (laFile.exists())
				{
					FileInputStream laFileInputStream =
						new FileInputStream(FILENAME);
					ObjectInputStream laObjectInputStream =
						new ObjectInputStream(laFileInputStream);
					shtRSPSUpdCache =
						(Hashtable) laObjectInputStream.readObject();
					laObjectInputStream.close();
					laFileInputStream.close();
					sbNotInitialized = false;
				}

				if (shtRSPSUpdCache == null)
				{
					shtRSPSUpdCache = new Hashtable();
					sbNotInitialized = false;
				}

			}
			catch (StreamCorruptedException aeSCE)
			{
				// No object in file; Continue; 
				shtRSPSUpdCache = new Hashtable();
				sbNotInitialized = false;
			}
			catch (IOException aeIOEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR, aeIOEx);
				throw leRTSEx;
			}
			catch (ClassNotFoundException aeCNFEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR, aeCNFEx);
				throw leRTSEx;
			}
		}

		switch (aiRequest)
		{
			// process add request
			case ADD_TO_QUEUE :
				{
					if (avRSPSUpdData != null)
					{
						for (int i = 0; i < avRSPSUpdData.size(); i++)
						{
							RSPSUpdData laRSPSUpData =
								(RSPSUpdData) avRSPSUpdData.elementAt(
									i);
							RSPSWsStatusData laRSPSWsStatusData =
								laRSPSUpData.getRSPSWsStatusData();
							String lsPrimaryKey =
								getKey(
									laRSPSWsStatusData
										.getOfcIssuanceNo(),
									laRSPSWsStatusData.getLocIdCd(),
									laRSPSWsStatusData.getLocId(),
									laRSPSWsStatusData.getRSPSId());

							shtRSPSUpdCache.put(
								lsPrimaryKey,
								laRSPSUpData);
						}
					}
					break;
				}
				// get next record
			case GET_QUEUE_ITEMS :
				{
					lvReturn = new Vector();

					// TODO Set up an enumeration to build a vector
					for (Enumeration e = shtRSPSUpdCache.elements();
						e.hasMoreElements();
						)
					{
						lvReturn.addElement(e.nextElement());
					}
					break;
				}
				// remove selected items
			case DELETE_ITEMS :
				{
					if (avRSPSUpdData != null)
					{
						for (int i = 0; i < avRSPSUpdData.size(); i++)
						{
							RSPSUpdData laRSPSUpdData =
								(RSPSUpdData) avRSPSUpdData.elementAt(
									i);
							RSPSWsStatusData lRSPSWsStatusData =
								laRSPSUpdData.getRSPSWsStatusData();
							String lsPrimaryKey =
								getKey(
									lRSPSWsStatusData
										.getOfcIssuanceNo(),
									lRSPSWsStatusData.getLocIdCd(),
									lRSPSWsStatusData.getLocId(),
									lRSPSWsStatusData.getRSPSId());
							shtRSPSUpdCache.remove(lsPrimaryKey);
						}
					}
				}
		}

		// re-write the cache file or delete if no more records
		try
		{
			File laFile = new File(FILENAME);

			if (shtRSPSUpdCache.isEmpty())
			{
				// delete if empty
				laFile.delete();
			}
			else
			{
				// rewrite the file
				FileOutputStream laFileOutputStream =
					new FileOutputStream(FILENAME, false);
				ObjectOutputStream laObjectOutputStream =
					new ObjectOutputStream(laFileOutputStream);
				laObjectOutputStream.writeObject(shtRSPSUpdCache);
				laObjectOutputStream.flush();
				laObjectOutputStream.close();
				laFileOutputStream.close();
			}
		}
		catch (IOException aeIOEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			throw leRTSEx;
		}

		return lvReturn;
	}
}
