package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMRPR005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Ralph		10/10/2006	New Class - Exempts
 * J Ralph		10/25/2006	Object naming standard per Walkthru
 * 							modify getValueAt()
 * 							defect 8900 Ver Exempts
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen RPR005.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 * 
 * @version Exempts		10/25/2006
 * @author	John Ralph
 * <br>Creation Date:	10/10/2006 13:47:40
 */

public class TMRPR005 extends javax.swing.table.AbstractTableModel
{
	private final static String[] carrColumn_Name =
		{	CommonConstant.TBL_TXT_OFFICENO,
			CommonConstant.TBL_TXT_OFFICENAME };

	private java.util.Vector lvData;

	/**
	 * TableModel constructor comment.
	 */
	public TMRPR005()
	{
		super();
		lvData = new Vector();
	}

	/**
	 * Add Data to the table to post rows.
	 *  
	 * @param avDataIn Vector
	 */
	public void add(java.util.Vector avDataIn)
	{
		lvData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the number of columns in the table.
	 * 
	 * @return	int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * Get the column name at the specified location.
	 * 
	 * <p>Returns empty string if location is not defined.
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
	 * Return the number of rows in the table.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return lvData.size();
	}

	/**
	 * Return the value at the specified location.
	 * 
	 * <p>Returns <b>null</b> if the location is not defined.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		switch (aiColumn)
		{
			case 0 :
				{
					OfficeIdsData laOfcIdsData =
						(OfficeIdsData) lvData.get(aiRow);
					return new Integer(laOfcIdsData.getOfcIssuanceNo());
				}
			case 1 :
				{
					OfficeIdsData laOfcIdsData =
						(OfficeIdsData) lvData.get(aiRow);
					return laOfcIdsData.getOfcName();
				}
			default :
				{
					return null;
				}
		}
	}
}
