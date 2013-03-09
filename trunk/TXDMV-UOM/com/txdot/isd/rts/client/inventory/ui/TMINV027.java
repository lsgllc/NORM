package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV027.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INV027.
 * 
 * @version 5.2.3		08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:	11/30/2001 15:47:03
 */

public class TMINV027 extends AbstractTableModel
{
	private static final int COLUMN_COUNT = 2;

	private Vector cvTblData = new Vector();
	public static String csTblType = new String();

	/**
	 * TMINV027 constructor comment.
	 */
	public TMINV027()
	{
		super();
	}

	/**
	 * Add data to the table model.
	 * 
	 * @param avDataIn Vector
	 */
	public void add(Vector avDataIn)
	{
		cvTblData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	/**
	 * Get the column name at the specified location.
	 * 
	 * <p>Return empty string is the location is not defined.
	 * 
	 * @param aiColumn int
	 * @return String
	 */
	public String getColumnName(int aiColumn)
	{
		String lsColumnName = CommonConstant.STR_SPACE_EMPTY;

		switch (aiColumn)
		{
			case 0 :
				{
					if (csTblType.equals(InventoryConstant.EMP))
					{
						lsColumnName =
							InventoryConstant.TXT_EMPLOYEE_ID;
					}
					else if (csTblType.equals(InventoryConstant.WS))
					{
						lsColumnName =
							InventoryConstant.TXT_WORKSTATION_ID;
					}
					else if (csTblType.equals(InventoryConstant.DLR))
					{
						lsColumnName = InventoryConstant.TXT_DEALER_ID;
					}
					else if (
						csTblType.equals(InventoryConstant.SUBCON))
					{
						lsColumnName =
							InventoryConstant.TXT_SUBCONTRACTOR_ID;
					}
					break;
				}
			case 1 :
				{
					if (csTblType.equals(InventoryConstant.EMP))
					{
						lsColumnName =
							InventoryConstant.TXT_EMPLOYEE_NAME;
					}
					else if (csTblType.equals(InventoryConstant.WS))
					{
						lsColumnName = CommonConstant.STR_SPACE_EMPTY;
					}
					else if (csTblType.equals(InventoryConstant.DLR))
					{
						lsColumnName =
							InventoryConstant.TXT_DEALER_NAME;
					}
					else if (
						csTblType.equals(InventoryConstant.SUBCON))
					{
						lsColumnName =
							InventoryConstant.TXT_SUBCONTRACTOR_NAME;
					}
					break;
				}
			default :
				{
					break;
				}
		}

		return lsColumnName;
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
	 * Get the value at a specified location.
	 * 
	 * <p>Return empty string is the location is not defined.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (cvTblData != null)
		{
			if (aiColumn == 0)
			{
				return CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_SPACE_ONE
					+ ((String[]) cvTblData.get(aiRow))[aiColumn];
			}
			else if (aiColumn == 1)
			{
				return ((String[]) cvTblData.get(aiRow))[aiColumn];
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
