package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMPMT004.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown &	04/26/2002	CQU100003680 - Line up total amounts in 
 * Robin Taylor             getValueAt method with detail amounts.
 * B Hargrove	09/27/2005	Modify code for move to Java 1.4. 
 *							Code clean-up, etc.
 *							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for PMT004
 * 
 * @version	5.2.3		03/16/2005
 * @author	Nancy Ting
 * <br>Creation Date:	09/13/2001 15:24:38
 */

public class TMPMT004 extends javax.swing.table.AbstractTableModel
{
	protected Vector cvVector = new Vector();
	private final static String[] carrColumn_Name = 
		{"Fees Description", "Item Price"};
	private final static String ERROR = "error";
	
	/**
	 * TMAPMT004 constructor.
	 */
	public TMPMT004()
	{
		super();
		cvVector = new Vector();
	}
	
	/**
	 * Put the vector in the table model
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		if (avVector != null)
		{
			cvVector.clear();
			for (int i = 0; i < avVector.size(); i++)
			{
				FeesData laFeesData = (FeesData) avVector.get(i);
				cvVector.addElement(laFeesData);
			}
			fireTableDataChanged();
		}
	}
	
	/**
	 * get the number of columns in the table model
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 2;
		return carrColumn_Name.length;
	}
	
	/**
	 * Get the individual column names by column number.
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
	 * Get the number of rows
	 * 
	 * @return int 
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}
	
	/**
	 * Get the value at a specific row/column
	 *  
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			return CommonConstant.STR_SPACE_ONE + 
				((FeesData) cvVector.get(aiRow)).getDesc();
		}
		else if (aiCol == 1)
		{
			return ((FeesData) cvVector.get(aiRow))
				.getItemPrice().printDollar()
				+ CommonConstant.STR_SPACE_ONE;
		}
		else
		{
			return ERROR;
		}
	}
}
