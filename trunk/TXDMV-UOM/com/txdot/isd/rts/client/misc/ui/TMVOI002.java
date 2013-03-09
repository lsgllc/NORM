package com.txdot.isd.rts.client.misc.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.VoidTransactionData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMVOI002.java
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
 * Table model for screen VOI002.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	5.2.3 			09/28/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMVOI002 extends javax.swing.table.AbstractTableModel
{

	private Vector cvVector;
	private static final String TXT_VOIDED = " ** VOIDED ** ";
	private final static String[] carrColumn_Name = 
		{"Transaction Id", "VIN", "Trans Code Description"};
		 
	/**
	 * TMFUN001 constructor comment.
	 */
	public TMVOI002()
	{
		super();
		cvVector = new java.util.Vector();
	}
	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param lvV Vector
	 */
	public void add(Vector lvV)
	{
		cvVector = new Vector(lvV);
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
	 * @param aiRow
	 * @param aiCol
	 * @return Object
	 */

	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			return (String) ((VoidTransactionData) cvVector.get(aiRow))
				.getTransactionId();

		}
		else if (aiCol == 1)
		{
			return (String) ((VoidTransactionData) cvVector.get(aiRow))
				.getVIN();
		}
		else if (aiCol == 2)
		{
			if (((VoidTransactionData) cvVector.get(aiRow))
				.getVoidedTransIndi()
				== 1)
			{
				return (String)
					((VoidTransactionData) cvVector.get(aiRow))
					.getTransCd()
					+ TXT_VOIDED;
			}
			else
			{
				return (String)
					((VoidTransactionData) cvVector.get(aiRow))
					.getTransCd()
					+ CommonConstant.STR_SPACE_ONE
					+ ((VoidTransactionData) cvVector.get(aiRow))
						.getTransCdDesc();
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;

	}
}
