package com.txdot.isd.rts.client.misc.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.ReceiptLogData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMRPR001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Zwiener	09/28/2005	Java 1.4
 *							defect 7892 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen RPR001.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	5.2.3 			09/28/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMRPR001 extends javax.swing.table.AbstractTableModel
{

	private java.util.Vector cvVector;
	private final static String[] carrColumn_Name = 
		{"Transaction No", "Transaction Type", "VIN"};
		 
 	/**
	 * TMFUN001 constructor comment.
	 */
	public TMRPR001()
	{
		super();
		cvVector = new java.util.Vector();
	}
	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avV Vector
	 */
	public void add(Vector avV)
	{
		cvVector = new Vector(avV);
		fireTableDataChanged();
	}
	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */

	public int getColumnCount()
	{
		//return 3;
		return carrColumn_Name.length;
	}
	/**
	 * Specify the names of each column in the table.
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
	 * Return values from the table
	 * 
	 * @param aiCol int
	 * @param aiCol int
	 * @return Object 
	 */

	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			return ((ReceiptLogData) cvVector.get(aiRow)).getTransId();

		}
		else if (aiCol == 1)
		{
			return ((ReceiptLogData) cvVector.get(aiRow))
				.getTransType();
		}
		else if (aiCol == 2)
		{
			return ((ReceiptLogData) cvVector.get(aiRow)).getVIN();
		}
		return CommonConstant.STR_SPACE_EMPTY;

	}
}
