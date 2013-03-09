package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.AddlWtTableData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMMRG011.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/28/2005	Modify code for move to Java 1.4. Bring code
 *							to standards.
 *  					 	Refactor\Move AddlWtTableData class to 
 *							com.txdot.isd.rts.services.data.
 *							defect 7893 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The table model for the table in the MRG011 screen.  It stores the 
 * table data and knows how to display it.
 *
 * @version	5.2.3			09/28/2005
 * @author	Joseph Kwik
 * <br>Creation Date:		12/06/2001 10:36:49
 */

public class TMMRG011 extends javax.swing.table.AbstractTableModel
{

	//An graph of the table data
	private java.util.Vector cvVector;
	
	private final static String[] carrColumn_Name = 
		{"Period", "Expiration", "Fee"};
	
	/**
	 * Creates a TMMRG011.
	 */
	public TMMRG011()
	{
		super();
		cvVector = new Vector();
	}
	/**
	 * Updates the table data with the new data in the cvVector
	 * 
	 * @param avVector Vector 
	 */
	public void add(Vector avVector)
	{
		cvVector = avVector;
		fireTableDataChanged();
	}
	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 3;
		return carrColumn_Name.length;
	}
	/**
	 * Returns the column name.
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
	 * Returns the number of rows.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}
	/**
	 * Returns the value in the table.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return CommonConstant.STR_SPACE_TWO
				+ ((AddlWtTableData) cvVector.get(aiRow)).getPeriod();
		}
		else if (aiColumn == 1)
		{
			return ((AddlWtTableData) cvVector.get(aiRow))
				.getExpirationDate()
				+ CommonConstant.STR_SPACE_THREE;
		}
		else if (aiColumn == 2)
		{
			return ((AddlWtTableData) cvVector.get(aiRow)).getAmount()
				+ CommonConstant.STR_SPACE_THREE;
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
