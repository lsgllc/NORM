package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.InventoryPatternsDescriptionData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV024.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Min Wang		08/01/2005	Remove Item code from table.
 * 							modify COLUMN_COUNT
 * 							getColumnName(), getValueAt()
 * 							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	08/13/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings
 * 							defect 7890 Ver 5.2.3
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen INV015.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 * 
 * @version	5.2.3		09/29/005
 * @author	Sunil Givindappa
 * <br>Creation Date:	06/22/2001 13:47:40
 */

public class TMINV024 extends javax.swing.table.AbstractTableModel
{
	//private static final int COLUMN_COUNT = 2;
	private final static String[] carrColumn_Name = 
		{InventoryConstant.TXT_ITEM_DESCRIPTION, 
		 InventoryConstant.TXT_YEAR};

	private java.util.Vector cvData;

	/**
	 * CashDrawerTableModel constructor comment.
	 */
	public TMINV024()
	{
		super();
		cvData = new Vector();
	}

	/**
	 * Add a vector to the table to post rows.
	 *  
	 * @param avDataIn Vector
	 */
	public void add(Vector avDataIn)
	{
		cvData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the number of columns in table.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return COLUMN_COUNT;
		return carrColumn_Name.length;
	}

	/**
	 * Get the names of each column in the table.
	 * 
	 * <p>If the column is not defined, return an empty string.
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
		return cvData.size();
	}

	/**
	 * Return values from the table.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public java.lang.Object getValueAt(int aiRow, int aiColumn)
	{
		switch (aiColumn)
		{
			case 0 :
				{
					InventoryPatternsDescriptionData laIPDD =
						(InventoryPatternsDescriptionData) cvData.get(
							aiRow);
					return laIPDD.getItmCdDesc();
				}
			case 1 :
				{
					InventoryPatternsDescriptionData laIPDD =
						(InventoryPatternsDescriptionData) cvData.get(
							aiRow);
					int liInvItmYr = laIPDD.getInvItmYr();
					if (liInvItmYr != 0)
					{
						return (new Integer(liInvItmYr));
					}
					else
					{
						return new String(
							CommonConstant.STR_SPACE_EMPTY);
					}
				}
			default :
				{
					break;
				}
		}

		return null;
	}
}
