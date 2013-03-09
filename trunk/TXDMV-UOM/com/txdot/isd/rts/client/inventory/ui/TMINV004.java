package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMINV004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * Ray Rowehl	08/12/2005	Cleanup pass
 * 							Add white space between methods.
 * 							work on constants.
 * 							defect 7890 Ver 5.2.3
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INV004
 * 
 * @version	5.2.3		09/29/2005
 * @author	Charlie Walker
 * <br>Creation Date:	01/22/2002 18:54:33
 */

public class TMINV004 extends AbstractTableModel
{
	//private static final int COLUMN_COUNT = 1;
	private final static String[] carrColumn_Name = 
		{CommonConstant.STR_SPACE_EMPTY};
	
	private Vector cvTblData = new Vector();
	
	/**
	 * TMINV004 constructor comment.
	 */
	public TMINV004()
	{
		super();
	}
	
	/**
	 * Add the data to the table model.
	 * 
	 * @param avDataIn java.util.Vector
	 */
	public void add(Vector avDataIn)
	{
		cvTblData = new Vector(avDataIn);
		fireTableDataChanged();
	}
	
	/**
	 * Get the Column Count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return COLUMN_COUNT;
		return carrColumn_Name.length;
	}
	
	/**
	 * Get the Column Name for the column specified.
	 * In this case, returns empty string.
	 * Return empty string if not defined.
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
	 * Get the Row Count.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTblData.size();
	}
	
	/**
	 * Get the value from the specified location.
	 * <p>Note that there is only one column so that parameter is not 
	 * actually used.
	 * <p>Return empty string if not defined.
	 * 
	 * @return Object
	 * @param aiRow int
	 * @param aiColumn int
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		String lsRowString = CommonConstant.STR_SPACE_EMPTY;
		
		if (cvTblData != null)
		{
			switch (aiColumn)
			{
				case 0 :
				{
					lsRowString = (String) cvTblData.get(aiRow);
					break;
				}
				default :
				{
					lsRowString = CommonConstant.STR_SPACE_EMPTY;
					break;
				}
			}
		}
		return lsRowString;
	}
}
