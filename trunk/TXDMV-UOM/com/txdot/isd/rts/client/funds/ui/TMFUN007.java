package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMFUN007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/30/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7886 Ver 5.2.3 
 * K Harrell	06/08/2009	Further cleanup
 * 							defect 9943 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen FUN007.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMFUN007 extends javax.swing.table.AbstractTableModel
{

	private Vector cvVector;
	
	/**
	 * TMFUN007 constructor comment.
	 */
	public TMFUN007()
	{
		super();
		cvVector = new Vector();
	}
	
	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = new Vector(avVector);
		fireTableDataChanged();
	}
	
	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return 1;
	}
	
	/**
	 * Specify the names of each column in the table.
	 * 
	 * @param aiColumn int
	 * @return String
	 */
	public String getColumnName(int aiColumn)
	{
		if (aiColumn == 0)
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
		return CommonConstant.STR_SPACE_EMPTY;
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
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return ((String) cvVector.get(aiRow));
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
