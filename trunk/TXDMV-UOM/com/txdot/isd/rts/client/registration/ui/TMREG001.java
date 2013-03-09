package com.txdot.isd.rts.client.registration.ui;

import java.awt.Font;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CustomTableData;

/* 
 * TMREG001.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/01/2005	Modify for move to Java 1.4. Bring code to
 * 							standards. Format, Hungarian notation, etc. 
 * 							defect 7894 Ver 5.2.3 
 * K Harrell	01/14/2008	Use Custom Table vs. String so that can 
 * 							pass font. Needed with SINGLE_SELECTION.
 * 							modify getValueAt() 
 *							defect 9796 Ver Defect_POS_D
 * ---------------------------------------------------------------------
 */
/**
 * Sticker Selection Screen REG001
 * Table model for screen REG001.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 * 
 * @version	Defect_POS_D	01/14/2009
 * @author	Joseph Kwik
 * <br>Creation Date:		10/13/2001 13:47:40
 */
public class TMREG001 extends javax.swing.table.AbstractTableModel
{

	private java.util.Vector cvVector;

	/**
	 * TMFUN007 constructor comment.
	 */
	public TMREG001()
	{
		super();
		cvVector = new java.util.Vector();
	}
	
	/**
	 * add
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = new Vector(avVector);
		fireTableDataChanged();
	}
	
	/**
	 * Return Column Count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return 1;
	}
	
	/**
	 *  Return Column Name
	 * 
	 *  @param aiColumn int 
	 *  @return String    Column Name
	 */
	public String getColumnName(int aiColumn)
	{
		if (aiColumn == 0)
		{
			return "";
		}
		return "";
	}
	
	/**
	 * getRowCount method comment.
	 * 
	 * @return int RowCount
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}
	
	/**
	 * Return AbstractValue at row and column parameter values.
	 *
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object AbstractValue at row and column parameter values
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			// defect 9796
			// return (String) cvVector.get(aiRow);
			return new CustomTableData(
				((String) cvVector.get(aiRow)),
				new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			// end defect 9796
		}
		return "";
	}
}
