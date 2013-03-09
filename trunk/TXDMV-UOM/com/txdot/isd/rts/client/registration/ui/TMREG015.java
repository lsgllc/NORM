package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.services.data.*;

import java.util.Vector;

/* 
 * TMREG015.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/20/2005	Modify for move to Java 1.4. Bring code to
 * 							standards. Format, Hungarian notation, etc. 
 * 							defect 7894 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Quick Counter Screen REG015
 * Table model for screen REG015.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 * 
 * @version	5.2.3		07/20/2005
 * @author	Joseph Kwik
 * <br>Creation Date:	10/13/2001 13:47:40
 */
public class TMREG015 extends javax.swing.table.AbstractTableModel
{

	private java.util.Vector cvVector;

	private final static String DESCRIPTION = "Description";
	private final static String DESC_PAD = "  ";
	private final static String AMT_PAD = "   ";
	private final static String ITM_PRICE = "Item Price";
	
	/**
	 * TMREG015 constructor.
	 */
	public TMREG015()
	{
		super();
		cvVector = new java.util.Vector();
	}
	/**
	 * Return Column Count.
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
		return 2;
	}
	/**
	 * Return Column Name
	 *   
	 * @param aiColumn int Column Number
	 * @return String Column Name
	 */
	public String getColumnName(int aiColumn)
	{
		if (aiColumn == 0)
		{
			return DESCRIPTION;
		}
		else if (aiColumn == 1)
		{
			return ITM_PRICE;
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
			return DESC_PAD
				+ ((FeesItemsTableData) cvVector.get(aiRow)).getDesc();
		}
		else if (aiColumn == 1)
		{
			return ((FeesItemsTableData) cvVector.get(aiRow))
				.getAmount()
				.printDollar()
				+ AMT_PAD;
		}
		return "";
	}
}
