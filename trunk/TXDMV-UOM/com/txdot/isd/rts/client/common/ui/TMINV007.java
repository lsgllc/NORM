package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINV007.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/27/2005	Modify code for move to Java 1.4. 
 *							Code clean-up, etc.
 *							defect 7885 Ver 5.2.3
 * K Harrell	12/16/2009	Implement Constants 
 * 							modify getValueAt()
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for INV007
 * 
 * @version	Defect_POS_H		12/16/2009
 * @author	Michael Abernethy
 * <br>Creation Date:			10/25/2001 13:17:02
 */

public class TMINV007 extends javax.swing.table.AbstractTableModel
{
	private Vector cvVector;
	
	private final static String[] carrColumn_Name =
		{ "Year", "Item Description", "Item No" };

	/**
	 * TMINV007 constructor comment.
	 */
	public TMINV007()
	{
		super();
		cvVector = new Vector();
	}

	/**
	 * Adds information to the INV007 table
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = avVector;
		fireTableDataChanged();
	}

	/**
	 * Returns the column count
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * Returns the column name
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
	 * Returns the row count
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * Returns the value
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		// defect 10290
		// Implement Constants  
		INV007TableData laData = (INV007TableData) cvVector.get(aiRow);

		if (aiColumn == CommonConstant.INV007_COL_YEAR)
		{
			return laData.getYear();
		}
		else if (aiColumn == CommonConstant.INV007_COL_ITMDESC)
		{
			return CommonConstant.STR_SPACE_TWO + laData.getDesc();
		}
		else if (aiColumn == CommonConstant.INV007_COL_ITMNO)
		{
			return laData.getItemNo() + CommonConstant.STR_SPACE_TWO;
		// end defect 10290 
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
	}
}
