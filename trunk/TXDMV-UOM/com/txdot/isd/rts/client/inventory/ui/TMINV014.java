package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV014.java
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
 * 							modify getColumnCount(), getColumnName() 
 * 							getValueAt()
 * 							defect 8269 Ver 5.2.2 Fix 6  
 * Ray Rowehl	08/12/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings.
 * 							defect 7890 Ver 5.2.3	
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INV014.
 * 
 * @version 5.2.3		09/29/2005
 * @author	Charlie Walker
 * <br>Creation Date:	09/05/2001 18:07:33
 */

public class TMINV014 extends AbstractTableModel
{
	//private static final int COLUMN_COUNT = 5;
	private final static String[] carrColumn_Name = 
		{InventoryConstant.TXT_ITEM_DESCRIPTION, InventoryConstant.TXT_YEAR, 
		 InventoryConstant.TXT_QUANTITY, InventoryConstant.TXT_BEGIN_NO,
		 InventoryConstant.TXT_END_NO};

	private Vector cvTblData = new Vector();

	/**
	 * TMINV014 constructor comment.
	 */
	public TMINV014()
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
	 * Get the Column Name at the specified location.
	 * <p>Returns empty string if not defined.
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
	 * Get the value at the specified location.
	 * <p>Returns an empty string if not defined.
	 * 
	 * @return Object
	 * @param aiRow	int
	 * @param aiColumn int
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (cvTblData != null)
		{
			switch (aiColumn)
			{
				case 0 :
					{
						String lsItmDesc =
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getItmCdDesc();
						lsItmDesc.trim();
						return lsItmDesc;
					}
				case 1 :
					{
						String lsYr =
							new String(CommonConstant.STR_SPACE_ONE);
						int liYr =
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getInvItmYr();
						if (liYr != 0)
						{
							lsYr = Integer.toString(liYr);
						}
						return lsYr;
					}
				case 2 :
					{
						return Integer.toString(
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getInvQty());
					}
				case 3 :
					{
						return (
							(InventoryAllocationUIData) cvTblData.get(
								aiRow))
							.getInvItmNo();
					}
				case 4 :
					{
						return (
							(InventoryAllocationUIData) cvTblData.get(
								aiRow))
							.getInvItmEndNo();
					}
				default :
					{
						break;
					}
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
