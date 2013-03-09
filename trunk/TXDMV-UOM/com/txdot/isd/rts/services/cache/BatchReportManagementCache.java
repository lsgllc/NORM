package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.BatchReportManagementData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * BatchReportManagementCache.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/16/2011  Created
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Cache object for RTS.RTS_BATCH_RPT_MGMT
 *
 * @version	6.7.0			01/16/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/16/2011 12:53:17
 */
public class BatchReportManagementCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static final String MSG_SERVER_CALL_FAILED =
		"Server down, using local BatchReportManagement cache";

	static final long serialVersionUID = -6432211922407347469L;

	/**
	 * BatchReportManagementCache.java Constructor
	 * 
	 */
	public BatchReportManagementCache()
	{
		super();
	}
	/**
	 * A hashtable of vectors with OfcIssuanceNo, SubstaId, 
	 *   FileName as Key 
	 */
	private static Hashtable shtBatchRptMgmt = new Hashtable();

	/**
	 * Return boolean to denote if AutoPrint on for given Report File
	 * Name
	 * 
	 * @param asFileName
	 * @return boolean 
	 */
	public static boolean isAutoPrint(String asFileName)
		throws RTSException
	{
		boolean lbAutoPrint = true;
		int liOfcNo = SystemProperty.getOfficeIssuanceNo();
		int liSubstaId = SystemProperty.getSubStationId();

		String lsPrimaryKey = getKey(liOfcNo, liSubstaId, asFileName);

		// Access Server if available
		try
		{
			if (!Comm.isServerDown())
			{
				BatchReportManagementData laBatchRptMgmtData =
					new BatchReportManagementData();
				laBatchRptMgmtData.setOfcIssuanceNo(liOfcNo);
				laBatchRptMgmtData.setSubstaId(liSubstaId);
				laBatchRptMgmtData.setFileName(asFileName);
				Vector lvData =
					(Vector) Comm.sendToServer(
						GeneralConstant.GENERAL,
						CacheConstant.BATCH_REPORT_MANAGEMENT_CACHE,
						laBatchRptMgmtData);

				if (lvData != null && lvData.size() != 0)
				{
					laBatchRptMgmtData =
						(BatchReportManagementData) lvData.elementAt(0);
					lbAutoPrint = laBatchRptMgmtData.isAutoPrnt();
				}
				return lbAutoPrint;
			}
		}
		catch (RTSException aeRTSEx)
		{
			// Log the error and move on to local cache
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}

		BatchReportManagementData laData =
			(BatchReportManagementData) shtBatchRptMgmt.get(
				lsPrimaryKey);

		if (laData != null)
		{
			lbAutoPrint = laData.isAutoPrnt();
		}
		return lbAutoPrint;
	}

	/**
	 * Return Primary Key for BatchReportManagementData
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubStaId int
	 * @param asFileName String
	 * @return String
	 * @throws RTSException 
	 */
	public static String getKey(
		int aiOfcIssuanceNo,
		int aiSubStaId,
		String asFileName)
		throws RTSException
	{
		return UtilityMethods.constructPrimaryKey(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiSubStaId),
				asFileName });
	}

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.BATCH_REPORT_MANAGEMENT_CACHE;
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
		return shtBatchRptMgmt;
	}

	/**
	 * Clear and populate the hashtable with the vector avBatchRptMgmtData
	 * 
	 * @param avBatchRptMgmtData 
	 */
	public void setData(Vector avBatchRptMgmtData) throws RTSException
	{
		//reset the hashtable
		shtBatchRptMgmt.clear();

		for (int i = 0; i < avBatchRptMgmtData.size(); i++)
		{
			BatchReportManagementData laData =
				(BatchReportManagementData) avBatchRptMgmtData.get(i);
				
			if (laData.getDeleteIndi() == 0)
			{
				String lsPrimaryKey =
					getKey(
						laData.getOfcIssuanceNo(),
						laData.getSubstaId(),
						laData.getFileName());
				shtBatchRptMgmt.put(lsPrimaryKey, laData);
			}
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
		shtBatchRptMgmt = ahtHashtable;
	}

}