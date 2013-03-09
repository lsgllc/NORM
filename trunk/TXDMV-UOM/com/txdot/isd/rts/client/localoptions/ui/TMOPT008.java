package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.BatchReportManagementData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 * TMOPT008.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/14/2011	Created
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for OPT008
 *
 * @version	6.7.0  			01/14/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/14/2011 17:36:17
 */
public class TMOPT008 extends javax.swing.table.AbstractTableModel
{

	private Vector cvVector;
	private Vector cvRevisedObjects = null;

	private static final String YES = "Yes";
	private static final String NO = "No";
	private static final String UPDATED = "Updated";
	private final static String[] carrColumn_Name =
		{ "Report Number", "Report Name", "Auto Print" };

	/**
	 * TableModel constructor comment.
	 */
	public TMOPT008()
	{
		super();
		cvVector = new Vector();
		cvRevisedObjects = new Vector();
	}

	/**
	 * Add data to the table to post rows.
	 *  
	 * @param avData Vector
	 */
	public void add(Vector avData)
	{
		cvVector = new Vector(avData);
		fireTableDataChanged();
	}

	/**
	 * Add item to RevisedObj
	 *  
	 * @param aaObj Object
	 */
	public void addRevisedObj(Object aaObj)
	{
		if (cvRevisedObjects != null)
		{
			cvRevisedObjects.add(aaObj);
		}
	}

	/**
	 * Get the number of columns in table.
	 * 
	 * @return int  
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * Get the name of each column in the table.
	 *  
	 * @param aiCol int
	 * @return String
	 */
	public String getColumnName(int aiCol)
	{
		if (aiCol >= 0 && aiCol < carrColumn_Name.length)
		{
			return carrColumn_Name[aiCol];
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
	}

	/**
	 * Return the number of rows in the table
	 * 
	 * @return int  
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * Return data values from the table
	 * 
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object  
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		BatchReportManagementData laData =
			(BatchReportManagementData) cvVector.get(aiRow);

		switch (aiCol)
		{
			case LocalOptionConstant.OPT008_COL_RPT_NUMBER :
				{
					return CommonConstant.STR_SPACE_THREE
						+ laData.getRptNumber();
				}
			case LocalOptionConstant.OPT008_COL_RPT_DESC :
				{
					return CommonConstant.STR_SPACE_ONE
						+ laData.getRptDesc();
				}
			case LocalOptionConstant.OPT008_COL_RPT_AUTOPRINT :
				{
					String lsAddUpdt = CommonConstant.STR_SPACE_THREE;

					if (laData.getChngTimestmp() != null)
					{
						lsAddUpdt =
							lsAddUpdt
								+ UPDATED
								+ CommonConstant.STR_SPACE_ONE
								+ laData.getChngTimestmp().getTimeSS();
					}

					return CommonConstant.STR_SPACE_THREE
						+ (laData.isAutoPrnt() ? YES : NO)
						+ lsAddUpdt;
				}
		}
		return null;
	}
}
