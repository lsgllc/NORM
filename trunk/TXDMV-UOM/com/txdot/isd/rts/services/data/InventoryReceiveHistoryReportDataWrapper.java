package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

/*
 * InventoryReceiveHistoryReportDataWrapper.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896
 * Ray Rowehl	09/30/2005	Moved to services.data since this is a 
 * 							data class.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This class contains the wrapper for InventoryReceiveHistoryReportData
 *
 * @version	5.2.3 			09/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/25/2001 14:21:40
 */

public class InventoryReceiveHistoryReportDataWrapper
	implements Serializable
{
	public Vector cvDataVct = null;

	/**
	 * InventoryReceiveHistoryReportDataWrapper constructor
	 */
	public InventoryReceiveHistoryReportDataWrapper()
	{
		super();
		cvDataVct = new Vector();
	}

	/**
	 * Returns the data vector in form of an Array
	 * 
	 * @return InventoryReceiveHistoryReportData[]
	 */
	public InventoryReceiveHistoryReportData[] getDataSet()
	{
		InventoryReceiveHistoryReportData[] larrInvReceiveHistoryRptData =
			new InventoryReceiveHistoryReportData[cvDataVct.size()];
		if (cvDataVct.size() > 0)
		{
			cvDataVct.copyInto(larrInvReceiveHistoryRptData);
		}
		return larrInvReceiveHistoryRptData;
	}

	/**
	 * Sets the data vector
	 * 
	 * @param aarrInvReceiveHistoryRptData 
	 * 	InventoryReceiveHistoryReportData[] 
	 */
	public void setDataSet(InventoryReceiveHistoryReportData[] aarrInvReceiveHistoryRptData)
	{
		cvDataVct = new Vector(aarrInvReceiveHistoryRptData.length);
		for (int i = 0; i < aarrInvReceiveHistoryRptData.length; i++)
		{
			cvDataVct.addElement(aarrInvReceiveHistoryRptData[i]);
		}
	}
}