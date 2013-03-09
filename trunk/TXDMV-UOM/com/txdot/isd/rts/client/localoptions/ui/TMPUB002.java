package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Vector;

import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.localoptions
	.PublishingReportData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMPUB002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang 	04/16/2005	RTS 5.2.3 Code Cleanup
 * Min Wang		09/02/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7891 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */

/**
 * Table model for screen PUB002.  Provides methods to get 
 * the column count, column names and data value as well as 
 * a method for inserting rows into the table.
 * 
 * @version	5.2.3		09/30/2005
 * @author 	Administrator
 * <br>Creation Date:	  
 */

public class TMPUB002 extends javax.swing.table.AbstractTableModel
{
	private Vector cvVector;
	private Vector cvRevisedObjects = null;
	
	private static final String YES = "YES";
	private static final String NO = "NO";
	private static final String UPDATED = "UPDATED";
	private static final String SECURITY = "RTS_SECURITY";
	private static final String NO_SEC_UPDT =
		"Update on Security Not Allowed";
	private final static String[] carrColumn_Name = 
		{"Id", "Substation Name", "Table Name", "Update Access"};

	/**
	 * CashDrawerTableModel constructor comment.
	 */
	public TMPUB002()
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
		//return COLUMN_COUNT;
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
		if (aiCol == 0)
		{
			return new Integer(
				((PublishingReportData) cvVector.get(aiRow))
					.getSubstaId());
		}
		else if (aiCol == 1)
		{
			return ((PublishingReportData) cvVector.get(aiRow))
				.getSubstaName();
		}
		else if (aiCol == 2)
		{
			return ((PublishingReportData) cvVector.get(aiRow))
				.getTblName();
		}
		else if (aiCol == 3)
		{
			String strAddUpdt = CommonConstant.STR_SPACE_THREE;
			PublishingReportData pubDat =
				(PublishingReportData) cvVector.get(aiRow);
			if (pubDat.getChngTimestmp() != null)
			{
				if (pubDat.getTblName().equals(SECURITY))
				{
					strAddUpdt = strAddUpdt + NO_SEC_UPDT;
				}
				else
				{
					strAddUpdt =
						strAddUpdt
							+ UPDATED
							+ CommonConstant.STR_SPACE_ONE
							+ pubDat.getChngTimestmp().getTime();
				}
			}
			if (((PublishingReportData) cvVector.get(aiRow))
				.getTblUpdtIndi()
				== 1)
			{
				return YES + strAddUpdt;
			}
			else
			{
				return NO + strAddUpdt;
			}
		}
		return null;
	}
}
